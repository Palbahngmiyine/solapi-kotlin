package com.solapi.example

import com.solapi.sdk.SolapiClient
import com.solapi.sdk.message.dto.request.SendRequestConfig
import com.solapi.sdk.message.model.Message

/**
 * 대량 메시지 발송 예제
 *
 * 환경변수 설정:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 수신번호 (테스트용으로 동일 번호 사용)
 *
 * 참고:
 * - 한 번에 최대 10,000건까지 발송 가능
 * - allowDuplicates 옵션으로 중복 수신번호 허용 가능
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

    // 여러 메시지 생성 (테스트를 위해 동일 수신자에게 발송)
    val messages = (1..3).map { i ->
        Message(
            from = sender,
            to = recipient,
            text = "대량 발송 테스트 메시지 $i/3"
        )
    }

    // 발송 설정 (중복 수신번호 허용)
    val config = SendRequestConfig(allowDuplicates = true)

    try {
        // 대량 메시지 발송
        val response = messageService.send(messages, config)

        println("대량 메시지 발송 성공!")
        println("Group ID: ${response.groupInfo?.groupId}")
        println("Total Count: ${response.groupInfo?.count}")
        response.groupInfo?.count?.let { count ->
            println("  - Total: ${count.total}")
            println("  - Sent Total: ${count.sentTotal}")
        }

    } catch (e: Exception) {
        System.err.println("대량 메시지 발송 실패: ${e.message}")
        e.printStackTrace()
    }
}
