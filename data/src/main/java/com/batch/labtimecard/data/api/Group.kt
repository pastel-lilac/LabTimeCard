package com.batch.labtimecard.data.api

enum class Group(val orderNumber: Int, val groupId: String, val groupName: String) {
    M2(0, "SSP6URD47", "M2"),
    M1(1, "SS8H9V93L", "M1"),
    B4(2, "SSB4JKZJ5", "B4"),
    B3(3, "SSLSU7QPN", "B3");

    companion object {
        fun nameToEnum(name: String): Group {
            return when (name) {
                M2.groupName -> M2
                M1.groupName -> M1
                B4.groupName -> B4
                B3.groupName -> B3
                else -> throw IllegalArgumentException()
            }
        }
    }

}