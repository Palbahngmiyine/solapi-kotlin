package com.solapi.sdk.message.model.kakao.bms

import kotlinx.serialization.Serializable

@Serializable
data class BmsCarousel(
    var head: BmsCarouselHead? = null,
    var list: List<BmsCarouselItem>? = null,
    var tail: BmsCarouselTail? = null
)
