package com.solapi.example;

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
 * ./gradlew :solapi-kotlin-example-java:run -Pexample=SendSms
 * ./gradlew :solapi-kotlin-example-java:run -Pexample=GetBalance
 */
public class MainExample {

    public static void main(String[] args) {
        printLine(60);
        System.out.println("SOLAPI SDK Java Examples");
        printLine(60);
        System.out.println();
        System.out.println("Available examples:");
        System.out.println();
        System.out.println("  SMS/LMS/MMS:");
        System.out.println("    SendSms       - SMS 단건 발송");
        System.out.println("    SendMms       - MMS 이미지 첨부 발송");
        System.out.println("    SendBatch     - 대량 메시지 발송");
        System.out.println("    SendScheduled - 예약 발송");
        System.out.println("    SendVoice     - 음성 메시지 발송");
        System.out.println();
        System.out.println("  Account:");
        System.out.println("    GetBalance    - 잔액 조회");
        System.out.println("    GetMessageList - 발송 내역 조회");
        System.out.println();
        System.out.println("  Kakao:");
        System.out.println("    KakaoAlimtalk     - 알림톡 발송");
        System.out.println("    KakaoBrandMessage - 브랜드 메시지 발송");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  ./gradlew :solapi-kotlin-example-java:run -Pexample=<ExampleName>");
        System.out.println();
        System.out.println("Example:");
        System.out.println("  ./gradlew :solapi-kotlin-example-java:run -Pexample=SendSms");
        System.out.println("  ./gradlew :solapi-kotlin-example-java:run -Pexample=GetBalance");
        System.out.println();
        System.out.println("Environment variables:");
        System.out.println("  SOLAPI_API_KEY     - " + maskValue(System.getenv("SOLAPI_API_KEY")));
        System.out.println("  SOLAPI_API_SECRET  - " + maskValue(System.getenv("SOLAPI_API_SECRET")));
        System.out.println("  SOLAPI_SENDER      - " + getEnvOrDefault("SOLAPI_SENDER", "(not set)"));
        System.out.println("  SOLAPI_RECIPIENT   - " + getEnvOrDefault("SOLAPI_RECIPIENT", "(not set)"));
        System.out.println("  SOLAPI_KAKAO_PF_ID - " + getEnvOrDefault("SOLAPI_KAKAO_PF_ID", "(not set)"));
        System.out.println();
    }

    private static void printLine(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append("=");
        }
        System.out.println(sb.toString());
    }

    private static String maskValue(String value) {
        if (value == null || value.isEmpty()) {
            return "(not set)";
        }
        if (value.length() <= 8) {
            return "****";
        }
        return value.substring(0, 4) + "****" + value.substring(value.length() - 4);
    }

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }
}
