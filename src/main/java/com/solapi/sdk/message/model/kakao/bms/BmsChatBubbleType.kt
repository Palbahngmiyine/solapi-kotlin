package com.solapi.sdk.message.model.kakao.bms

import kotlinx.serialization.Serializable

/**
 * BMS Free 채팅 버블 타입
 */
@Serializable
enum class BmsChatBubbleType {
    /**
     * 텍스트 타입
     */
    TEXT,

    /**
     * 이미지 타입
     */
    IMAGE,

    /**
     * 와이드 타입
     */
    WIDE,

    /**
     * 와이드 리스트 타입
     */
    WIDE_ITEM_LIST,

    /**
     * 커머스 타입
     */
    COMMERCE,

    /**
     * 캐러셀 피드 타입
     */
    CAROUSEL_FEED,

    /**
     * 캐러셀 커머스 타입
     */
    CAROUSEL_COMMERCE,

    /**
     * 프리미엄 비디오 타입
     */
    PREMIUM_VIDEO
}
