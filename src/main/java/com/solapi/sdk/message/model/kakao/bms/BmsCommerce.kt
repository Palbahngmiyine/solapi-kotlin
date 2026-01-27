package com.solapi.sdk.message.model.kakao.bms

import kotlinx.serialization.Serializable

@Serializable
data class BmsCommerce(
    var title: String? = null,
    var regularPrice: Long? = null,
    var discountPrice: Long? = null,
    var discountRate: Int? = null,
    var discountFixed: Long? = null
)
