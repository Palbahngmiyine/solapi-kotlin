package com.solapi.example;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.dto.request.SendRequestConfig;
import com.solapi.sdk.message.dto.response.MultipleDetailMessageSentResponse;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;

import java.time.LocalDateTime;
import java.time.ZoneId;

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
public class SendScheduledExample {

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
        message.setText("안녕하세요. 예약 발송 테스트 메시지입니다.");

        // 10분 후 예약 발송 설정
        LocalDateTime scheduledTime = LocalDateTime.now().plusMinutes(10);
        ZoneId seoulZone = ZoneId.of("Asia/Seoul");

        SendRequestConfig config = new SendRequestConfig();
        config.setScheduledDateFromLocalDateTime(scheduledTime, seoulZone);

        System.out.println("예약 시간: " + scheduledTime);

        try {
            // 예약 메시지 발송
            MultipleDetailMessageSentResponse response = messageService.send(message, config);

            System.out.println("예약 발송 성공!");
            System.out.println("Group ID: " + response.getGroupInfo().getGroupId());
            System.out.println("Scheduled Date: " + response.getGroupInfo().getScheduledDate());

        } catch (Exception e) {
            System.err.println("예약 발송 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
