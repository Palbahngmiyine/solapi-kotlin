package com.solapi.sdk.message.model.kakao.bms

import kotlinx.serialization.Serializable

/**
 * BMS Free 버튼 타입
 */
@Serializable
enum class BmsButtonType {
    /**
     * 웹링크
     */
    WL,

    /**
     * 앱링크
     */
    AL,

    /**
     * 채널 추가
     */
    AC,

    /**
     * 봇 키워드
     */
    BK,

    /**
     * 메시지 전달
     */
    MD,

    /**
     * 상담 요청
     */
    BC,

    /**
     * 봇 전환
     */
    BT,

    /**
     * 비즈니스폼
     */
    BF
}
