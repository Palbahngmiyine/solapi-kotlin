package com.solapi.example

import com.solapi.sdk.SolapiClient
import com.solapi.sdk.message.model.Message

/**
 * SMS 단건 발송 예제
 *
 * 환경변수 설정:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 수신번호
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

    // 메시지 생성 (Kotlin data class)
    val message = Message(
        from = sender,
        to = recipient,
        text = "안녕하세요. SOLAPI SDK Kotlin 예제입니다."
    )

    try {
        // 메시지 발송
        val response = messageService.send(message)

        println("SMS 발송 성공!")
        println("Group ID: ${response.groupInfo?.groupId}")
        println("Message Count: ${response.groupInfo?.count}")

    } catch (e: Exception) {
        System.err.println("SMS 발송 실패: ${e.message}")
        e.printStackTrace()
    }
}
