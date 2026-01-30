package com.solapi.sdk.message.e2e.lib

import com.solapi.sdk.message.model.Message
import com.solapi.sdk.message.model.MessageType
import com.solapi.sdk.message.model.kakao.KakaoButton
import com.solapi.sdk.message.model.kakao.KakaoButtonType
import com.solapi.sdk.message.model.kakao.KakaoOption
import com.solapi.sdk.message.model.voice.VoiceOption
import com.solapi.sdk.message.model.voice.VoiceType

/**
 * E2E 테스트를 위한 메시지 팩토리 및 유틸리티 함수들
 */
object E2ETestUtils {

    // ==================== SMS/LMS/MMS 메시지 팩토리 ====================

    /**
     * SMS 메시지 생성
     */
    fun createSmsMessage(
        from: String,
        to: String,
        text: String = "[SDK 테스트] SMS 메시지입니다."
    ): Message = Message(
        from = from,
        to = to,
        text = text
    )

    /**
     * LMS 메시지 생성 (명시적 타입 지정)
     */
    fun createLmsMessage(
        from: String,
        to: String,
        text: String = generateLongText(100),
        subject: String? = null
    ): Message = Message(
        type = MessageType.LMS,
        from = from,
        to = to,
        text = text,
        subject = subject
    )

    /**
     * MMS 메시지 생성
     */
    fun createMmsMessage(
        from: String,
        to: String,
        text: String = "[SDK 테스트] MMS 메시지입니다.",
        imageId: String,
        subject: String? = null
    ): Message = Message(
        type = MessageType.MMS,
        from = from,
        to = to,
        text = text,
        imageId = imageId,
        subject = subject
    )

    // ==================== 카카오 메시지 팩토리 ====================

    /**
     * 알림톡 메시지 생성
     */
    fun createAlimtalkMessage(
        from: String,
        to: String,
        pfId: String,
        templateId: String,
        variables: Map<String, String>? = null
    ): Message = Message(
        type = MessageType.ATA,
        from = from,
        to = to,
        kakaoOptions = KakaoOption(
            pfId = pfId,
            templateId = templateId,
            variables = variables
        )
    )

    /**
     * 알림톡 메시지 생성 (대체 발송 비활성화)
     */
    fun createAlimtalkMessageWithoutFallback(
        from: String,
        to: String,
        pfId: String,
        templateId: String,
        variables: Map<String, String>? = null
    ): Message = Message(
        type = MessageType.ATA,
        from = from,
        to = to,
        kakaoOptions = KakaoOption(
            pfId = pfId,
            templateId = templateId,
            variables = variables,
            disableSms = true
        )
    )

    // ==================== 음성 메시지 팩토리 ====================

    /**
     * 음성 메시지 생성
     */
    fun createVoiceMessage(
        from: String,
        to: String,
        text: String = "안녕하세요. 테스트 음성 메시지입니다.",
        voiceType: VoiceType = VoiceType.FEMALE,
        headerMessage: String? = null,
        tailMessage: String? = null
    ): Message = Message(
        type = MessageType.VOICE,
        from = from,
        to = to,
        text = text,
        voiceOptions = VoiceOption(
            voiceType = voiceType,
            headerMessage = headerMessage,
            tailMessage = tailMessage
        )
    )

    // ==================== 카카오 버튼 팩토리 ====================

    /**
     * 웹링크 버튼 생성
     */
    fun createWebLinkButton(
        buttonName: String = "바로가기",
        linkMo: String = "https://example.com",
        linkPc: String? = null
    ): KakaoButton = KakaoButton(
        buttonName = buttonName,
        buttonType = KakaoButtonType.WL,
        linkMo = linkMo,
        linkPc = linkPc
    )

    /**
     * 앱링크 버튼 생성
     */
    fun createAppLinkButton(
        buttonName: String = "앱 열기",
        linkAnd: String = "intent://main",
        linkIos: String = "iosapp://main"
    ): KakaoButton = KakaoButton(
        buttonName = buttonName,
        buttonType = KakaoButtonType.AL,
        linkAnd = linkAnd,
        linkIos = linkIos
    )

    /**
     * 봇키워드 버튼 생성
     */
    fun createBotKeywordButton(
        buttonName: String = "문의하기"
    ): KakaoButton = KakaoButton(
        buttonName = buttonName,
        buttonType = KakaoButtonType.BK
    )

    /**
     * 메시지전달 버튼 생성
     */
    fun createMessageDeliveryButton(
        buttonName: String = "전달하기"
    ): KakaoButton = KakaoButton(
        buttonName = buttonName,
        buttonType = KakaoButtonType.MD
    )

    /**
     * 채널 추가 버튼 생성
     */
    fun createChannelAddButton(
        buttonName: String = "채널 추가"
    ): KakaoButton = KakaoButton(
        buttonName = buttonName,
        buttonType = KakaoButtonType.AC
    )

    // ==================== Custom Fields 메시지 팩토리 ====================

    /**
     * Custom Fields 포함 메시지 생성
     */
    fun createMessageWithCustomFields(
        from: String,
        to: String,
        text: String = "[SDK 테스트] Custom Fields 테스트",
        customFields: MutableMap<String, String>
    ): Message = Message(
        from = from,
        to = to,
        text = text,
        customFields = customFields
    )

    // ==================== 배치 메시지 팩토리 ====================

    /**
     * 배치 SMS 메시지 생성
     */
    fun createBatchSmsMessages(
        from: String,
        toList: List<String>,
        textPrefix: String = "[SDK 테스트] 배치 SMS"
    ): List<Message> = toList.mapIndexed { index, to ->
        Message(
            from = from,
            to = to,
            text = "$textPrefix ${index + 1}/${toList.size}"
        )
    }

    // ==================== 유틸리티 함수 ====================

    /**
     * LMS 테스트용 긴 텍스트 생성
     * @param byteLength 목표 바이트 길이 (한글 기준 약 2배 문자 수)
     */
    fun generateLongText(byteLength: Int = 100): String {
        val prefix = "[SDK 테스트] LMS 긴 메시지 테스트입니다. "
        val filler = "가나다라마바사아자차카타파하"
        val sb = StringBuilder(prefix)

        while (sb.toString().toByteArray(Charsets.UTF_8).size < byteLength) {
            sb.append(filler)
        }

        return sb.toString()
    }

    /**
     * 최대 길이 LMS 메시지 생성 (약 2000바이트)
     */
    fun generateMaxLengthLmsText(): String {
        return generateLongText(1900) // 약간의 여유를 두고 1900바이트
    }
}
