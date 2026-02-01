package com.solapi.sdk.message.lib

import kotlin.time.Instant
import kotlin.time.toKotlinInstant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * LocalDateTime을 kotlin.time.Instant로 변환하는 유틸리티.
 *
 * LocalDateTime은 시간대 정보가 없으므로, 변환 시 시간대를 지정해야 합니다.
 * 기본값은 시스템 기본 시간대(ZoneId.systemDefault())입니다.
 */
object LocalDateTimeSupport {
    /**
     * LocalDateTime을 kotlin.time.Instant로 변환합니다.
     *
     * @param localDateTime 변환할 LocalDateTime
     * @param zoneId 적용할 시간대 (기본값: 시스템 기본 시간대)
     * @return 변환된 kotlin.time.Instant
     */
    @JvmStatic
    @JvmOverloads
    fun toKotlinInstant(localDateTime: LocalDateTime, zoneId: ZoneId = ZoneId.systemDefault()): Instant {
        return localDateTime.atZone(zoneId).toInstant().toKotlinInstant()
    }
}

/**
 * LocalDateTime을 kotlin.time.Instant로 변환하는 확장 함수.
 * 
 * @param zoneId 적용할 시간대 (기본값: 시스템 기본 시간대)
 * @return 변환된 kotlin.time.Instant
 */
@JvmOverloads
fun LocalDateTime.toKotlinInstant(zoneId: ZoneId = ZoneId.systemDefault()): Instant {
    return LocalDateTimeSupport.toKotlinInstant(this, zoneId)
}
