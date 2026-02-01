package com.solapi.example

import com.solapi.sdk.SolapiClient
import com.solapi.sdk.message.dto.request.SendRequestConfig
import com.solapi.sdk.message.model.Message
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * 예약 발송 예제
 *
 * 환경변수 설정:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 수신번호
 *
 * 참고:
 * - 예약 시간은 현재 시간으로부터 최소 10분 이후여야 함
 * - 최대 6개월 이내로 예약 가능
 * - 과거 시간 지정 시 즉시 발송 처리됨
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

    // 메시지 생성
    val message = Message(
        from = sender,
        to = recipient,
        text = "안녕하세요. 예약 발송 테스트 메시지입니다."
    )

    // 10분 후 예약 발송 설정
    val scheduledTime = LocalDateTime.now().plusMinutes(10)
    val seoulZone = ZoneId.of("Asia/Seoul")

    val config = SendRequestConfig().apply {
        setScheduledDateFromLocalDateTime(scheduledTime, seoulZone)
    }

    println("예약 시간: $scheduledTime")

    try {
        // 예약 메시지 발송
        val response = messageService.send(message, config)

        println("예약 발송 성공!")
        println("Group ID: ${response.groupInfo?.groupId}")
        println("Scheduled Date: ${response.groupInfo?.scheduledDate}")

    } catch (e: Exception) {
        System.err.println("예약 발송 실패: ${e.message}")
        e.printStackTrace()
    }
}
