package com.solapi.sdk.message.model.kakao.bms

import kotlinx.serialization.Serializable

@Serializable
data class BmsMainWideItem(
    var title: String? = null,
    var imageId: String? = null,
    var linkMobile: String? = null,
    var linkPc: String? = null,
    var linkAndroid: String? = null,
    var linkIos: String? = null
)
