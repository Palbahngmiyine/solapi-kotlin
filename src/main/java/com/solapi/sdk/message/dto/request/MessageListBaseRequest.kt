package com.solapi.sdk.message.dto.request

import com.solapi.sdk.message.lib.toKotlinInstant
import com.solapi.sdk.message.model.CommonMessageProperty
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class MessageListBaseRequest(
    override var to: String? = null,
    override var from: String? = null,
    var startKey: String? = null,
    var limit: Int? = null,
    var messageId: String? = null,
    var groupId: String? = null,
    var type: String? = null,
    var statusCode: String? = null,
    var criteria: String? = null,
    var cond: String? = null,
    var value: String? = null,

    @Contextual
    var startDate: Instant? = null,

    @Contextual
    var endDate: Instant? = null
) : CommonMessageProperty {
    @JvmOverloads
    fun setStartDateFromLocalDateTime(
        localDateTime: LocalDateTime,
        zoneId: ZoneId = ZoneId.systemDefault()
    ) {
        this.startDate = localDateTime.toKotlinInstant(zoneId)
    }

    @JvmOverloads
    fun setEndDateFromLocalDateTime(
        localDateTime: LocalDateTime,
        zoneId: ZoneId = ZoneId.systemDefault()
    ) {
        this.endDate = localDateTime.toKotlinInstant(zoneId)
    }
}
