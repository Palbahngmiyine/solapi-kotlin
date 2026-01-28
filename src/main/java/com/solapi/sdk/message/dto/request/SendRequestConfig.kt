package com.solapi.sdk.message.dto.request

import com.solapi.sdk.message.lib.toKotlinInstant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class SendRequestConfig(
    var appId: String? = null,
    var allowDuplicates: Boolean = false,
    var showMessageList: Boolean = false,
    @Contextual
    var scheduledDate: Instant? = null
) {
    @JvmOverloads
    fun setScheduledDateFromLocalDateTime(
        localDateTime: LocalDateTime,
        zoneId: ZoneId = ZoneId.systemDefault()
    ) {
        this.scheduledDate = localDateTime.toKotlinInstant(zoneId)
    }
}
