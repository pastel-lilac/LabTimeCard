package com.batch.labtimecard.ui.log

import com.airbnb.epoxy.TypedEpoxyController
import com.batch.labtimecard.itemLog
import com.batch.labtimecard.model.LoginLog

class LogListController : TypedEpoxyController<List<LoginLog>>() {
    override fun buildModels(data: List<LoginLog>?) {
        data?.forEach { log ->
            itemLog {
                id(log.hashCode())
                log(log)
            }
        }
    }
}