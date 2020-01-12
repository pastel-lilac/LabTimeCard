package com.batch.labtimecard.model

import java.nio.channels.spi.AbstractSelectionKey

//data class Member (
//    // ここで設定した変数名でDatabaseに保存される
//    val name: String,
//    val affiliation_lab_name: String
//) {
//    class Member() {
//
//    }
//}

class Member {
    var name: String? = null
    var affiliationLabName: String? = null

    constructor() {}
    constructor(name: String?, affiliationLabName: String?) {
        this.name = name
        this.affiliationLabName = affiliationLabName
    }
}