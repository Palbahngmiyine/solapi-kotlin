package com.solapi.sdk.message.e2e

import com.solapi.sdk.SolapiClient
import com.solapi.sdk.message.dto.request.SendRequestConfig
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException
import com.solapi.sdk.message.model.Message
import com.solapi.sdk.message.service.DefaultMessageService
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Instant

/**
 * 예약 발송 E2E 테스트
 *
 * 환경변수 설정 필요:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 테스트 수신번호
 */
class ScheduledMessageE2ETest {

    private val apiKey: String? = System.getenv("SOLAPI_API_KEY")
    private val apiSecret: String? = System.getenv("SOLAPI_API_SECRET")
    private val senderNumber: String = System.getenv("SOLAPI_SENDER") ?: "01000000000"
    private val testPhoneNumber: String = System.getenv("SOLAPI_RECIPIENT") ?: "01000000000"

    private val messageService: DefaultMessageService? by lazy {
        if (apiKey != null && apiSecret != null) {
            SolapiClient.createInstance(apiKey, apiSecret)
        } else {
            null
        }
    }

    private fun assumeEnvironmentConfigured(): Boolean {
        if (apiKey.isNullOrBlank() || apiSecret.isNullOrBlank()) {
            println("환경변수가 설정되지 않아 테스트를 건너뜁니다. (SOLAPI_API_KEY, SOLAPI_API_SECRET 필요)")
            return false
        }
        return true
    }

    private fun printExceptionDetails(e: Exception) {
        println("예상된 에러 발생: ${e.message}")
        if (e is SolapiMessageNotReceivedException) {
            println("  실패한 메시지 목록 (${e.failedMessageList.size}건):")
            e.failedMessageList.forEachIndexed { index, failed ->
                println("    [${index + 1}] to: ${failed.to}, statusCode: ${failed.statusCode}, statusMessage: ${failed.statusMessage}")
            }
        }
    }

    // ==================== LocalDateTime 예약 발송 테스트 ====================

    @Test
    fun `예약 발송 - LocalDateTime 시스템 기본 타임존 사용`() {
        if (!assumeEnvironmentConfigured()) return

        // Given
        val message = Message(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] LocalDateTime 예약 발송 테스트 (시스템 기본 타임존)"
        )

        val scheduledTime = LocalDateTime.now().plusMinutes(10)
        val config = SendRequestConfig()
        config.setScheduledDateFromLocalDateTime(scheduledTime)

        println("예약 시간 (LocalDateTime): $scheduledTime")
        println("예약 시간 (Instant): ${config.scheduledDate}")

        // When
        val response = messageService!!.send(message, config)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("예약 발송 성공 - groupId: ${response.groupInfo?.groupId}")
        println("  scheduledDate: ${response.groupInfo?.scheduledDate}")
    }

    @Test
    fun `예약 발송 - LocalDateTime 명시적 타임존 사용 (Asia Seoul)`() {
        if (!assumeEnvironmentConfigured()) return

        // Given
        val message = Message(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] LocalDateTime 예약 발송 테스트 (Asia/Seoul)"
        )

        val seoulZone = ZoneId.of("Asia/Seoul")
        val scheduledTime = LocalDateTime.now(seoulZone).plusMinutes(15)
        val config = SendRequestConfig()
        config.setScheduledDateFromLocalDateTime(scheduledTime, seoulZone)

        println("예약 시간 (LocalDateTime, Seoul): $scheduledTime")
        println("예약 시간 (Instant/UTC): ${config.scheduledDate}")

        // When
        val response = messageService!!.send(message, config)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("예약 발송 성공 - groupId: ${response.groupInfo?.groupId}")
        println("  scheduledDate: ${response.groupInfo?.scheduledDate}")
    }

    @Test
    fun `예약 발송 - 기존 Instant API 하위호환성 확인`() {
        if (!assumeEnvironmentConfigured()) return

        // Given
        val message = Message(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] Instant 예약 발송 테스트 (하위호환성)"
        )

        val scheduledInstant = Instant.fromEpochMilliseconds(
            System.currentTimeMillis() + 20 * 60 * 1000 // 20분 후
        )
        val config = SendRequestConfig(scheduledDate = scheduledInstant)

        println("예약 시간 (Instant): $scheduledInstant")

        // When
        val response = messageService!!.send(message, config)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("예약 발송 성공 (Instant API) - groupId: ${response.groupInfo?.groupId}")
        println("  scheduledDate: ${response.groupInfo?.scheduledDate}")
    }

    @Test
    fun `예약 발송 - 다중 메시지 LocalDateTime 사용`() {
        if (!assumeEnvironmentConfigured()) return

        // Given
        val messages = listOf(
            Message(
                from = senderNumber,
                to = testPhoneNumber,
                text = "[SDK 테스트] 다중 메시지 예약 발송 1/2"
            ),
            Message(
                from = senderNumber,
                to = testPhoneNumber,
                text = "[SDK 테스트] 다중 메시지 예약 발송 2/2"
            )
        )

        val scheduledTime = LocalDateTime.now().plusMinutes(25)
        val config = SendRequestConfig()
        config.setScheduledDateFromLocalDateTime(scheduledTime)

        println("예약 시간: $scheduledTime")
        println("메시지 수: ${messages.size}")

        // When
        val response = messageService!!.send(messages, config)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("다중 메시지 예약 발송 성공 - groupId: ${response.groupInfo?.groupId}")
        println("  count: ${response.groupInfo?.count}")
        println("  scheduledDate: ${response.groupInfo?.scheduledDate}")
    }

    // ==================== 특수 케이스 테스트 ====================

    @Test
    fun `예약 발송 - 과거 시간 지정시 즉시 발송 처리`() {
        if (!assumeEnvironmentConfigured()) return

        // Given
        val message = Message(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] 과거 시간 예약 발송 (즉시 발송 예상)"
        )

        val pastTime = LocalDateTime.now().minusMinutes(10)
        val config = SendRequestConfig()
        config.setScheduledDateFromLocalDateTime(pastTime)

        println("예약 시간 (과거): $pastTime")

        // When
        val response = messageService!!.send(message, config)

        // Then - 과거 시간은 에러 없이 즉시 발송 처리됨
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("과거 시간 예약 → 즉시 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `예약 발송 - 6개월 이내 미래 시간은 성공`() {
        if (!assumeEnvironmentConfigured()) return

        // Given
        val message = Message(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] 5개월 후 예약 발송"
        )

        val fiveMonthsLater = LocalDateTime.now().plusMonths(5)
        val config = SendRequestConfig()
        config.setScheduledDateFromLocalDateTime(fiveMonthsLater)

        println("예약 시간 (5개월 후): $fiveMonthsLater")

        // When
        val response = messageService!!.send(message, config)

        // Then - 6개월 이내는 성공
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("5개월 후 예약 발송 성공 - groupId: ${response.groupInfo?.groupId}")
        println("  scheduledDate: ${response.groupInfo?.scheduledDate}")
    }

    @Test
    fun `예약 발송 - 6개월 초과 미래 시간 지정시 에러`() {
        if (!assumeEnvironmentConfigured()) return

        // Given
        val message = Message(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] 7개월 후 예약 발송 (에러 예상)"
        )

        val sevenMonthsLater = LocalDateTime.now().plusMonths(7)
        val config = SendRequestConfig()
        config.setScheduledDateFromLocalDateTime(sevenMonthsLater)

        println("예약 시간 (7개월 후): $sevenMonthsLater")

        // When & Then
        var errorOccurred = false
        try {
            messageService!!.send(message, config)
        } catch (e: Exception) {
            errorOccurred = true
            printExceptionDetails(e)
        }

        assertTrue(errorOccurred, "6개월 초과 예약 발송 시 에러가 발생해야 함")
    }

    // ==================== 나노초 정밀도 테스트 ====================

    @Test
    fun `예약 발송 - 나노초 정밀도 유지 확인`() {
        if (!assumeEnvironmentConfigured()) return

        // Given
        val message = Message(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] 나노초 정밀도 테스트"
        )

        // 나노초가 포함된 시간
        val scheduledTime = LocalDateTime.now().plusMinutes(30).withNano(123456789)
        val config = SendRequestConfig()
        config.setScheduledDateFromLocalDateTime(scheduledTime)

        println("예약 시간 (나노초 포함): $scheduledTime")
        println("변환된 Instant: ${config.scheduledDate}")

        // 나노초가 보존되었는지 확인
        val instantString = config.scheduledDate.toString()
        println("Instant 문자열: $instantString")
        assertTrue(
            instantString.contains(".123456789Z") || instantString.contains(".123456789"),
            "나노초 정밀도가 유지되어야 함"
        )

        // When
        val response = messageService!!.send(message, config)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("나노초 정밀도 테스트 성공 - groupId: ${response.groupInfo?.groupId}")
    }
}
