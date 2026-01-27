package com.solapi.sdk.message.model.kakao.bms

import kotlinx.serialization.Serializable

@Serializable
data class BmsVideo(
    var videoUrl: String? = null,
    var imageId: String? = null,
    var imageLink: String? = null
)
