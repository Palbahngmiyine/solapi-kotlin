package com.solapi.sdk.message.dto.request.kakao

import com.solapi.sdk.message.lib.toKotlinInstant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.Instant

data class KakaoTemplateDateQuery(
    val date: Instant,
    val queryCondition: KakaoAlimtalkTemplateDateQueryCondition,
) {
    @JvmOverloads
    constructor(
        localDateTime: LocalDateTime,
        queryCondition: KakaoAlimtalkTemplateDateQueryCondition,
        zoneId: ZoneId = ZoneId.systemDefault()
    ) : this(
        date = localDateTime.toKotlinInstant(zoneId),
        queryCondition = queryCondition
    )

    enum class KakaoAlimtalkTemplateDateQueryCondition {
        EQUALS,
        GREATER_THEN_OR_EQUAL,
        GREATER_THEN,
        LESS_THEN_OR_EQUAL,
        LESS_THEN
    }

    companion object {
        @JvmStatic
        @JvmOverloads
        fun fromLocalDateTime(
            localDateTime: LocalDateTime,
            queryCondition: KakaoAlimtalkTemplateDateQueryCondition,
            zoneId: ZoneId = ZoneId.systemDefault()
        ): KakaoTemplateDateQuery {
            return KakaoTemplateDateQuery(
                date = localDateTime.toKotlinInstant(zoneId),
                queryCondition = queryCondition
            )
        }
    }
}
