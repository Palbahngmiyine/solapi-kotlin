package com.solapi.sdk.message.e2e

import com.solapi.sdk.message.e2e.base.BaseE2ETest
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * 잔액 조회 E2E 테스트
 *
 * 환경변수 설정 필요:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 */
class BalanceE2ETest : BaseE2ETest() {

    @Test
    fun `잔액 조회`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // When
        val balance = messageService!!.getBalance()

        // Then
        assertNotNull(balance)
        println("잔액 조회 성공")
        println("  balance: ${balance.balance}")
        println("  point: ${balance.point}")
    }

    @Test
    fun `일일 발송량 한도 조회`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // When
        val quota = messageService!!.getQuota()

        // Then
        assertNotNull(quota)
        println("일일 발송량 한도 조회 성공")
        println("  quota: $quota")
    }
}
