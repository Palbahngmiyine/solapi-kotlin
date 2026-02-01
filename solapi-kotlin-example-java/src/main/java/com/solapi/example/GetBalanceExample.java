package com.solapi.example;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.model.Balance;
import com.solapi.sdk.message.model.Quota;
import com.solapi.sdk.message.service.DefaultMessageService;

/**
 * 잔액 조회 예제
 *
 * 환경변수 설정:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 */
public class GetBalanceExample {

    public static void main(String[] args) {
        // 환경변수에서 설정 로드
        String apiKey = System.getenv("SOLAPI_API_KEY");
        String apiSecret = System.getenv("SOLAPI_API_SECRET");

        if (apiKey == null || apiSecret == null) {
            System.err.println("SOLAPI_API_KEY and SOLAPI_API_SECRET must be set");
            System.exit(1);
        }

        // SDK 클라이언트 생성
        DefaultMessageService messageService = SolapiClient.INSTANCE.createInstance(apiKey, apiSecret);

        try {
            // 잔액 조회
            Balance balance = messageService.getBalance();

            System.out.println("=== 잔액 정보 ===");
            System.out.println("Balance: " + balance.getBalance());
            System.out.println("Point: " + balance.getPoint());

            // 일일 발송량 한도 조회
            Quota quota = messageService.getQuota();

            System.out.println();
            System.out.println("=== 일일 발송량 한도 ===");
            System.out.println("Quota: " + quota);

        } catch (Exception e) {
            System.err.println("조회 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
