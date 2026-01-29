package com.solapi.example

import com.solapi.sdk.SolapiClient

/**
 * 잔액 조회 예제
 *
 * 환경변수 설정:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
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
        // 잔액 조회
        val balance = messageService.getBalance()

        println("=== 잔액 정보 ===")
        println("Balance: ${balance.balance}")
        println("Point: ${balance.point}")

        // 일일 발송량 한도 조회
        val quota = messageService.getQuota()

        println()
        println("=== 일일 발송량 한도 ===")
        println("Quota: $quota")

    } catch (e: Exception) {
        System.err.println("조회 실패: ${e.message}")
        e.printStackTrace()
    }
}
