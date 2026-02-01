package com.solapi.sdk.message.model.kakao

import com.solapi.sdk.message.model.kakao.bms.*
import kotlinx.serialization.Serializable

@Serializable
data class KakaoBmsOption(
    var targeting: KakaoBmsTargeting? = null,
    var chatBubbleType: BmsChatBubbleType? = null,
    var adult: Boolean? = null,
    var header: String? = null,
    var imageId: String? = null,
    var imageLink: String? = null,
    var additionalContent: String? = null,
    var content: String? = null,
    var carousel: BmsCarousel? = null,
    var mainWideItem: BmsMainWideItem? = null,
    var subWideItemList: List<BmsSubWideItem>? = null,
    var buttons: List<BmsButton>? = null,
    var coupon: BmsCoupon? = null,
    var commerce: BmsCommerce? = null,
    var video: BmsVideo? = null
)

