package com.solapi.example;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.dto.request.SendRequestConfig;
import com.solapi.sdk.message.dto.response.MultipleDetailMessageSentResponse;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;

import java.util.ArrayList;
import java.util.List;

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
public class SendBatchExample {

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

        // 여러 메시지 생성 (테스트를 위해 동일 수신자에게 발송)
        List<Message> messages = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Message message = new Message();
            message.setFrom(sender);
            message.setTo(recipient);
            message.setText("대량 발송 테스트 메시지 " + i + "/3");
            messages.add(message);
        }

        // 발송 설정 (중복 수신번호 허용)
        SendRequestConfig config = new SendRequestConfig();
        config.setAllowDuplicates(true);

        try {
            // 대량 메시지 발송
            MultipleDetailMessageSentResponse response = messageService.send(messages, config);

            System.out.println("대량 메시지 발송 성공!");
            System.out.println("Group ID: " + response.getGroupInfo().getGroupId());
            System.out.println("Total Count: " + response.getGroupInfo().getCount());
            if (response.getGroupInfo().getCount() != null) {
                System.out.println("  - Total: " + response.getGroupInfo().getCount().getTotal());
                System.out.println("  - Sent Total: " + response.getGroupInfo().getCount().getSentTotal());
            }

        } catch (Exception e) {
            System.err.println("대량 메시지 발송 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
