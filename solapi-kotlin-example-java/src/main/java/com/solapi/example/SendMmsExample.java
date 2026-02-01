package com.solapi.example;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.dto.response.MultipleDetailMessageSentResponse;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.model.MessageType;
import com.solapi.sdk.message.model.StorageType;
import com.solapi.sdk.message.service.DefaultMessageService;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * MMS 이미지 첨부 발송 예제
 *
 * 환경변수 설정:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 수신번호
 *
 * 참고: MMS 이미지 규격
 * - 지원 포맷: JPG, JPEG
 * - 최대 용량: 200KB
 * - 권장 해상도: 1000x1000 이하
 */
public class SendMmsExample {

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

        try {
            // 이미지 파일 로드 (리소스에서)
            URL imageUrl = SendMmsExample.class.getClassLoader().getResource("images/sample.jpg");
            if (imageUrl == null) {
                System.err.println("Sample image not found in resources/images/sample.jpg");
                System.err.println("Please add a JPG image file (max 200KB) to run this example.");
                System.exit(1);
            }

            // 임시 파일로 복사 (URL에서 File로 변환)
            File tempFile = File.createTempFile("mms-image", ".jpg");
            tempFile.deleteOnExit();
            try (InputStream is = imageUrl.openStream()) {
                Files.copy(is, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            // 이미지 업로드
            System.out.println("이미지 업로드 중...");
            String imageId = messageService.uploadFile(tempFile, StorageType.MMS, null);
            System.out.println("이미지 업로드 완료 - imageId: " + imageId);

            // MMS 메시지 생성
            Message message = new Message();
            message.setType(MessageType.MMS);
            message.setFrom(sender);
            message.setTo(recipient);
            message.setText("안녕하세요. MMS 이미지 첨부 메시지입니다.");
            message.setSubject("MMS 제목");
            message.setImageId(imageId);

            // 메시지 발송
            MultipleDetailMessageSentResponse response = messageService.send(message, null);

            System.out.println("MMS 발송 성공!");
            System.out.println("Group ID: " + response.getGroupInfo().getGroupId());

        } catch (Exception e) {
            System.err.println("MMS 발송 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
