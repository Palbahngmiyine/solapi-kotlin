package com.solapi.example

import com.solapi.sdk.SolapiClient
import com.solapi.sdk.message.model.Message
import com.solapi.sdk.message.model.MessageType
import com.solapi.sdk.message.model.voice.VoiceOption
import com.solapi.sdk.message.model.voice.VoiceType

/**
 * 음성 메시지 발송 예제
 *
 * 환경변수 설정:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 수신번호
 *
 * 음성 메시지는 TTS(Text-to-Speech)를 통해 텍스트를 음성으로 변환하여 발송합니다.
 * VoiceType: FEMALE(여성), MALE(남성)
 */
fun main() {
    // 환경변수에서 설정 로드
    val apiKey = System.getenv("SOLAPI_API_KEY")
        ?: error("SOLAPI_API_KEY must be set")
    val apiSecret = System.getenv("SOLAPI_API_SECRET")
        ?: error("SOLAPI_API_SECRET must be set")
    val sender = System.getenv("SOLAPI_SENDER")
        ?: error("SOLAPI_SENDER must be set")
    val recipient = System.getenv("SOLAPI_RECIPIENT")
        ?: error("SOLAPI_RECIPIENT must be set")

    // SDK 클라이언트 생성
    val messageService = SolapiClient.createInstance(apiKey, apiSecret)

    // 음성 옵션 설정
    val voiceOption = VoiceOption(
        voiceType = VoiceType.FEMALE,    // 여성 음성
        headerMessage = "안녕하세요.",    // 헤더 메시지
        tailMessage = "감사합니다."       // 테일 메시지
    )

    // 음성 메시지 생성
    val message = Message(
        type = MessageType.VOICE,
        from = sender,
        to = recipient,
        text = "음성 메시지 본문입니다. 이 메시지는 TTS로 변환되어 발송됩니다.",
        voiceOptions = voiceOption
    )

    try {
        // 음성 메시지 발송
        val response = messageService.send(message)

        println("음성 메시지 발송 성공!")
        println("Group ID: ${response.groupInfo?.groupId}")

    } catch (e: Exception) {
        System.err.println("음성 메시지 발송 실패: ${e.message}")
        e.printStackTrace()
    }
}
