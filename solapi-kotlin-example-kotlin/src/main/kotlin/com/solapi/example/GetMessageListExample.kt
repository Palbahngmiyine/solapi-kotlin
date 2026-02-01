package com.solapi.example

import com.solapi.sdk.SolapiClient
import com.solapi.sdk.message.dto.request.MessageListRequest
import com.solapi.sdk.message.dto.response.MessageListResponse
import com.solapi.sdk.message.model.MessageStatusType
import java.time.LocalDateTime

/**
 * 발송 내역 조회 예제
 *
 * 환경변수 설정:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 *
 * 다양한 필터 옵션:
 * - from: 발신번호
 * - to: 수신번호
 * - type: 메시지 타입 (SMS, LMS, MMS, ATA 등)
 * - status: 메시지 상태 (PENDING, SENDING, COMPLETE, FAILED)
 * - startDate/endDate: 날짜 범위
 */
fun main() {
    // 환경변수에서 설정 로드
    val apiKey = System.getenv("SOLAPI_API_KEY")
        ?: error("SOLAPI_API_KEY must be set")
    val apiSecret = System.getenv("SOLAPI_API_SECRET")
        ?: error("SOLAPI_API_SECRET must be set")

    // SDK 클라이언트 생성
    val messageService = SolapiClient.createInstance(apiKey, apiSecret)

    try {
        // 1. 기본 조회 (최근 메시지)
        println("=== 기본 메시지 목록 (최근 10건) ===")
        val basicResponse = messageService.getMessageList(
            MessageListRequest(limit = 10)
        )
        printMessageList(basicResponse)

        // 2. 발송 완료 메시지 조회
        println("\n=== 발송 완료 메시지 ===")
        val completedResponse = messageService.getMessageList(
            MessageListRequest(
                status = MessageStatusType.COMPLETE,
                limit = 5
            )
        )
        printMessageList(completedResponse)

        // 3. 날짜 범위 조회 (최근 7일)
        println("\n=== 최근 7일간 메시지 ===")
        val dateRequest = MessageListRequest(limit = 5).apply {
            setStartDateFromLocalDateTime(LocalDateTime.now().minusDays(7))
            setEndDateFromLocalDateTime(LocalDateTime.now())
        }
        val dateResponse = messageService.getMessageList(dateRequest)
        printMessageList(dateResponse)

        // 4. SMS 타입만 조회
        println("\n=== SMS 타입 메시지 ===")
        val smsResponse = messageService.getMessageList(
            MessageListRequest(type = "SMS", limit = 5)
        )
        printMessageList(smsResponse)

    } catch (e: Exception) {
        System.err.println("메시지 목록 조회 실패: ${e.message}")
        e.printStackTrace()
    }
}

private fun printMessageList(response: MessageListResponse?) {
    val messageList = response?.messageList
    if (messageList.isNullOrEmpty()) {
        println("  (조회 결과 없음)")
        return
    }

    println("  조회 건수: ${messageList.size}")

    messageList.entries.take(3).forEach { (id, msg) ->
        println("  - ID: $id")
        println("    Type: ${msg.type}, To: ${msg.to}")
        println("    Status: ${msg.statusCode}, Text: ${msg.text?.take(30)}${if ((msg.text?.length ?: 0) > 30) "..." else ""}")
    }

    if (messageList.size > 3) {
        println("  ... (외 ${messageList.size - 3}건)")
    }
}
