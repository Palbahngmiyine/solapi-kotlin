package com.solapi.example

import com.solapi.sdk.SolapiClient
import com.solapi.sdk.message.model.Message
import com.solapi.sdk.message.model.MessageType
import com.solapi.sdk.message.model.kakao.KakaoOption

/**
 * 카카오 알림톡 발송 예제
 *
 * 환경변수 설정:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 수신번호
 * - SOLAPI_KAKAO_PF_ID: 카카오 비즈니스 채널 ID
 * - SOLAPI_KAKAO_TEMPLATE_ID: 카카오 알림톡 템플릿 ID
 *
 * 알림톡 특징:
 * - 사전에 검수 승인된 템플릿만 사용 가능
 * - 정보성 메시지 전용 (광고 불가)
 * - 변수 치환을 통해 동적 내용 전달 가능
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
    val pfId = System.getenv("SOLAPI_KAKAO_PF_ID")
        ?: error("SOLAPI_KAKAO_PF_ID must be set for Kakao Alimtalk")
    val templateId = System.getenv("SOLAPI_KAKAO_TEMPLATE_ID")
        ?: error("SOLAPI_KAKAO_TEMPLATE_ID must be set for Kakao Alimtalk")

    // SDK 클라이언트 생성
    val messageService = SolapiClient.createInstance(apiKey, apiSecret)

    // 템플릿 변수 설정 (템플릿에 맞게 조정 필요)
    val variables = mapOf(
        "name" to "홍길동",
        "code" to "123456"
    )

    // 알림톡 메시지 생성
    val message = Message(
        type = MessageType.ATA,
        from = sender,
        to = recipient,
        kakaoOptions = KakaoOption(
            pfId = pfId,
            templateId = templateId,
            variables = variables
        )
    )

    try {
        // 알림톡 발송
        val response = messageService.send(message)

        println("알림톡 발송 성공!")
        println("Group ID: ${response.groupInfo?.groupId}")

    } catch (e: Exception) {
        System.err.println("알림톡 발송 실패: ${e.message}")
        e.printStackTrace()
    }
}
