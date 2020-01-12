package com.batch.labtimecard.model

class MemberData {
    var key: String? = null
    var member: Member? = null

    constructor() {}
    constructor(key: String?, member: Member?) {
        this.key = key
        this.member = member
    }
}