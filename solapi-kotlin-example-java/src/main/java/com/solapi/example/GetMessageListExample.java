package com.solapi.example;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.dto.request.MessageListRequest;
import com.solapi.sdk.message.dto.response.MessageListResponse;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.model.MessageStatusType;
import com.solapi.sdk.message.service.DefaultMessageService;

import java.time.LocalDateTime;
import java.util.Map;

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
 * - type: 메시지 타입 (SMS, LMS, MMS, ATA, CTA 등)
 * - status: 메시지 상태 (PENDING, SENDING, COMPLETE, FAILED)
 * - startDate/endDate: 날짜 범위
 */
public class GetMessageListExample {

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
            // 1. 기본 조회 (최근 메시지)
            System.out.println("=== 기본 메시지 목록 (최근 10건) ===");
            MessageListRequest basicRequest = new MessageListRequest();
            basicRequest.setLimit(10);

            MessageListResponse basicResponse = messageService.getMessageList(basicRequest);
            printMessageList(basicResponse);

            // 2. 발송 완료 메시지 조회
            System.out.println("\n=== 발송 완료 메시지 ===");
            MessageListRequest completedRequest = new MessageListRequest();
            completedRequest.setStatus(MessageStatusType.COMPLETE);
            completedRequest.setLimit(5);

            MessageListResponse completedResponse = messageService.getMessageList(completedRequest);
            printMessageList(completedResponse);

            // 3. 날짜 범위 조회 (최근 7일)
            System.out.println("\n=== 최근 7일간 메시지 ===");
            MessageListRequest dateRequest = new MessageListRequest();
            dateRequest.setStartDateFromLocalDateTime(LocalDateTime.now().minusDays(7));
            dateRequest.setEndDateFromLocalDateTime(LocalDateTime.now());
            dateRequest.setLimit(5);

            MessageListResponse dateResponse = messageService.getMessageList(dateRequest);
            printMessageList(dateResponse);

            // 4. SMS 타입만 조회
            System.out.println("\n=== SMS 타입 메시지 ===");
            MessageListRequest smsRequest = new MessageListRequest();
            smsRequest.setType("SMS");
            smsRequest.setLimit(5);

            MessageListResponse smsResponse = messageService.getMessageList(smsRequest);
            printMessageList(smsResponse);

        } catch (Exception e) {
            System.err.println("메시지 목록 조회 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printMessageList(MessageListResponse response) {
        if (response == null || response.getMessageList() == null) {
            System.out.println("  (조회 결과 없음)");
            return;
        }

        Map<String, Message> messageList = response.getMessageList();
        System.out.println("  조회 건수: " + messageList.size());

        int count = 0;
        for (Map.Entry<String, Message> entry : messageList.entrySet()) {
            if (count >= 3) {
                System.out.println("  ... (외 " + (messageList.size() - 3) + "건)");
                break;
            }

            Message msg = entry.getValue();
            System.out.println("  - ID: " + entry.getKey());
            System.out.println("    Type: " + msg.getType() + ", To: " + msg.getTo());
            System.out.println("    Status: " + msg.getStatusCode() + ", Text: " + truncate(msg.getText(), 30));
            count++;
        }
    }

    private static String truncate(String str, int maxLength) {
        if (str == null) return "(null)";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength) + "...";
    }
}
