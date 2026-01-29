package com.solapi.example;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.dto.response.MultipleDetailMessageSentResponse;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;

/**
 * SMS 단건 발송 예제
 *
 * 환경변수 설정:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 수신번호
 */
public class SendSmsExample {

    public static void main(String[] args) {
        // 환경변수에서 설정 로드
        String apiKey = System.getenv("SOLAPI_API_KEY");
        String apiSecret = System.getenv("SOLAPI_API_SECRET");
        String sender = System.getenv("SOLAPI_SENDER");
        String recipient = System.getenv("SOLAPI_RECIPIENT");

        if (apiKey == null || apiSecret == null) {
            System.err.println("SOLAPI_API_KEY and SOLAPI_API_SECRET must be set");
            System.exit(1);
        }
        if (sender == null || recipient == null) {
            System.err.println("SOLAPI_SENDER and SOLAPI_RECIPIENT must be set");
            System.exit(1);
        }

        // SDK 클라이언트 생성
        DefaultMessageService messageService = SolapiClient.INSTANCE.createInstance(apiKey, apiSecret);

        // 메시지 생성
        Message message = new Message();
        message.setFrom(sender);
        message.setTo(recipient);
        message.setText("안녕하세요. SOLAPI SDK Java 예제입니다.");

        try {
            // 메시지 발송
            MultipleDetailMessageSentResponse response = messageService.send(message, null);

            System.out.println("SMS 발송 성공!");
            System.out.println("Group ID: " + response.getGroupInfo().getGroupId());
            System.out.println("Message Count: " + response.getGroupInfo().getCount());

        } catch (Exception e) {
            System.err.println("SMS 발송 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
