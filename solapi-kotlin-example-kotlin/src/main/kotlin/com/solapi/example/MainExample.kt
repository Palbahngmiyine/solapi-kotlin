package com.solapi.example

/**
 * SOLAPI SDK 예제 메인 클래스
 *
 * 환경변수 설정:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 테스트 수신번호
 *
 * 개별 예제 실행:
 * ./gradlew :solapi-kotlin-example-kotlin:run -Pexample=SendSms
 * ./gradlew :solapi-kotlin-example-kotlin:run -Pexample=GetBalance
 */
fun main() {
    println("=".repeat(60))
    println("SOLAPI SDK Kotlin Examples")
    println("=".repeat(60))
    println()
    println("Available examples:")
    println()
    println("  SMS/LMS/MMS:")
    println("    SendSms       - SMS 단건 발송")
    println("    SendMms       - MMS 이미지 첨부 발송")
    println("    SendBatch     - 대량 메시지 발송")
    println("    SendScheduled - 예약 발송")
    println("    SendVoice     - 음성 메시지 발송")
    println()
    println("  Account:")
    println("    GetBalance    - 잔액 조회")
    println("    GetMessageList - 발송 내역 조회")
    println()
    println("  Kakao:")
    println("    KakaoAlimtalk     - 알림톡 발송")
    println("    KakaoBrandMessage - 브랜드 메시지 발송")
    println()
    println("Usage:")
    println("  ./gradlew :solapi-kotlin-example-kotlin:run -Pexample=<ExampleName>")
    println()
    println("Example:")
    println("  ./gradlew :solapi-kotlin-example-kotlin:run -Pexample=SendSms")
    println("  ./gradlew :solapi-kotlin-example-kotlin:run -Pexample=GetBalance")
    println()
    println("Environment variables:")
    println("  SOLAPI_API_KEY     - ${maskValue(System.getenv("SOLAPI_API_KEY"))}")
    println("  SOLAPI_API_SECRET  - ${maskValue(System.getenv("SOLAPI_API_SECRET"))}")
    println("  SOLAPI_SENDER      - ${System.getenv("SOLAPI_SENDER") ?: "(not set)"}")
    println("  SOLAPI_RECIPIENT   - ${System.getenv("SOLAPI_RECIPIENT") ?: "(not set)"}")
    println("  SOLAPI_KAKAO_PF_ID - ${System.getenv("SOLAPI_KAKAO_PF_ID") ?: "(not set)"}")
    println()
}

private fun maskValue(value: String?): String {
    if (value.isNullOrEmpty()) return "(not set)"
    if (value.length <= 8) return "****"
    return "${value.take(4)}****${value.takeLast(4)}"
}
