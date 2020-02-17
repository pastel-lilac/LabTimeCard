package com.batch.labtimecard.member.usecase

import com.batch.labtimecard.data.TimeCardRepository
import com.batch.labtimecard.data.api.Group
import com.batch.labtimecard.data.model.Member
import com.batch.labtimecard.data.model.MemberData
import kotlinx.coroutines.*
import kotlin.coroutines.resume

class MemberUseCaseImpl(
    private val repository: TimeCardRepository
) : MemberUseCase {

    @ExperimentalCoroutinesApi
    private suspend fun getAllMemberProfile(scope: CoroutineScope) =
        suspendCancellableCoroutine<List<Member>> { cont ->
            val members = mutableListOf<Member>()
            scope.launch {
                Group.values().map {
                    async {
                        Pair(
                            it,
                            repository.getGroupMembers(it.groupId).users?.filterNotNull()
                        )
                    }
                }.awaitAll()
                    .forEach { groupMembers ->
                        val member = fetchGroupMemberProf(
                            scope,
                            groupMembers.first.groupName,
                            groupMembers.second
                        )
                        members.addAll(member)
                    }
                cont.resume(members)
            }
        }

    private suspend fun fetchGroupMemberProf(
        scope: CoroutineScope,
        groupName: String,
        memberIds: List<String>?
    ) = suspendCancellableCoroutine<List<Member>> {
        scope.launch {
            val members = memberIds?.map { memberId ->
                async { Pair(memberId, repository.getMemberProfile(memberId)) }
            }?.awaitAll()
                ?.mapNotNull {
                    it.second.profile?.let { prof ->
                        Member(
                            prof.real_name_normalized,
                            groupName,
                            it.first,
                            prof.image_192,
                            false
                        )

                    }
                } ?: emptyList()
            it.resume(members)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun updateYear(scope: CoroutineScope) {
        val existingMembers = repository.fetchMembersOnce()
        val slackMembers = getAllMemberProfile(scope)
        removeOldMember(scope, existingMembers, slackMembers)
        updateGrade(scope, existingMembers, slackMembers)
        registerMembers(scope, existingMembers, slackMembers)
    }

    private suspend fun removeOldMember(
        scope: CoroutineScope,
        existingMembers: List<MemberData>,
        newMembers: List<Member>
    ) = suspendCancellableCoroutine<Unit> { cont ->
        scope.launch {
            val oldMembers = existingMembers
                .filterNot { member ->
                    newMembers.find { it.slackId == member.member?.slackId }?.let { true } ?: false
                }
            oldMembers.mapNotNull {
                val key = it.key ?: return@mapNotNull null
                async { repository.removeMember(key) }
            }.awaitAll()
            cont.resume(Unit)
        }

    }

    private suspend fun updateGrade(
        scope: CoroutineScope,
        existingMembers: List<MemberData>,
        newMembers: List<Member>
    ) = suspendCancellableCoroutine<Unit> { cont ->
        scope.launch {
            val newGradeMembers = existingMembers.mapNotNull { member ->
                newMembers.firstOrNull { member.member?.slackId == it.slackId }?.let {
                    val newProfile = member.member?.copy(
                        name = it.name,
                        grade = it.grade,
                        iconUrl = it.iconUrl
                    )
                    MemberData(member.key, newProfile)
                }
            }
            newGradeMembers.mapNotNull {
                val key = it.key ?: return@mapNotNull null
                val member = it.member ?: return@mapNotNull null
                async { repository.updateMemberProfile(key, member) }
            }.awaitAll()
            cont.resume(Unit)
        }
    }

    private suspend fun registerMembers(
        scope: CoroutineScope,
        existingMembers: List<MemberData>,
        members: List<Member>
    ) = suspendCancellableCoroutine<Unit> { cont ->
        scope.launch {
            members.filterNot { member ->
                existingMembers.find { it.member?.slackId == member.slackId }?.let { true }
                    ?: false
            }.map { newMember ->
                async { repository.registerMember(newMember) }
            }.awaitAll()
            cont.resume(Unit)
        }
    }

    override suspend fun refreshMembers(
        scope: CoroutineScope,
        members: List<MemberData>
    ) = suspendCancellableCoroutine<Unit> { cont ->
        scope.launch {
            members.map { member ->
                async {
                    val slackId = member.member?.slackId ?: return@async null
                    val prof = repository.getMemberProfile(slackId).profile ?: return@async null
                    Pair(member, prof)
                }
            }.awaitAll()
                .filterNotNull()
                .map {
                    Pair(
                        it.first.key,
                        it.first.member?.copy(
                            name = it.second.real_name,
                            iconUrl = it.second.image_192
                        )
                    )
                }
                .map {
                    async {
                        val key = it.first ?: return@async
                        val member = it.second ?: return@async
                        repository.updateMemberProfile(key, member)
                    }
                }.awaitAll()
            cont.resume(Unit)
        }
    }
}