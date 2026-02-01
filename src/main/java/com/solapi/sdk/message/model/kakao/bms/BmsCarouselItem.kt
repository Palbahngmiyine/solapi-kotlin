package com.solapi.sdk.message.model.kakao.bms

import kotlinx.serialization.Serializable

@Serializable
data class BmsCarouselItem(
    // FeedItem 전용 필드
    var header: String? = null,
    var content: String? = null,
    // CommerceItem 전용 필드
    var commerce: BmsCommerce? = null,
    var additionalContent: String? = null,
    // 공통 필드
    var imageId: String? = null,
    var imageLink: String? = null,
    var buttons: List<BmsButton>? = null,
    var coupon: BmsCoupon? = null
)
