package com.solapi.sdk.message.dto.request

import com.solapi.sdk.message.lib.toKotlinInstant
import com.solapi.sdk.message.model.Message
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class MultipleDetailMessageSendingRequest(
    var messages: List<Message> = emptyList(),
    @Contextual
    var scheduledDate: Instant? = null,
    var showMessageList: Boolean = false,
) : AbstractDefaultMessageRequest() {
    @JvmOverloads
    fun setScheduledDateFromLocalDateTime(
        localDateTime: LocalDateTime,
        zoneId: ZoneId = ZoneId.systemDefault()
    ) {
        this.scheduledDate = localDateTime.toKotlinInstant(zoneId)
    }
}
