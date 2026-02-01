package com.solapi.sdk.message.model.kakao.bms

import kotlinx.serialization.Serializable

@Serializable
data class BmsButton(
    var linkType: BmsButtonType? = null,
    var name: String? = null,
    var linkMobile: String? = null,
    var linkPc: String? = null,
    var linkAndroid: String? = null,
    var linkIos: String? = null,
    var chatExtra: String? = null,
    var targetOut: Boolean? = null
)
