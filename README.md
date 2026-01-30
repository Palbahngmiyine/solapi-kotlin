# SOLAPI Kotlin SDK

[![Maven Central](https://img.shields.io/maven-central/v/com.solapi/sdk)](https://central.sonatype.com/artifact/com.solapi/sdk)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Kotlin과 Java에서 SOLAPI 메시지 발송 서비스를 사용할 수 있는 공식 SDK입니다.

## 설치

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("com.solapi:sdk:1.1.0")
}
```

### Gradle (Groovy)

```groovy
dependencies {
    implementation 'com.solapi:sdk:1.1.0'
}
```

### Maven

```xml
<dependency>
    <groupId>com.solapi</groupId>
    <artifactId>sdk</artifactId>
    <version>1.1.0</version>
</dependency>
```

## 빠른 시작

### Kotlin

```kotlin
import com.solapi.sdk.SolapiClient
import com.solapi.sdk.message.model.Message

fun main() {
    val messageService = SolapiClient.createInstance("API_KEY", "API_SECRET")

    val message = Message(
        from = "발신번호",
        to = "수신번호",
        text = "안녕하세요. SOLAPI SDK 테스트입니다."
    )

    val response = messageService.send(message)
    println("Group ID: ${response.groupInfo?.groupId}")
}
```

### Java

```java
import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;

public class Main {
    public static void main(String[] args) {
        DefaultMessageService messageService = SolapiClient.INSTANCE.createInstance("API_KEY", "API_SECRET");

        Message message = new Message();
        message.setFrom("발신번호");
        message.setTo("수신번호");
        message.setText("안녕하세요. SOLAPI SDK 테스트입니다.");

        var response = messageService.send(message, null);
        System.out.println("Group ID: " + response.getGroupInfo().getGroupId());
    }
}
```

## 예제 실행

### 환경변수 설정

| 환경변수 | 설명 | 필수 |
|----------|------|:----:|
| `SOLAPI_API_KEY` | SOLAPI API 키 | O |
| `SOLAPI_API_SECRET` | SOLAPI API 시크릿 | O |
| `SOLAPI_SENDER` | 등록된 발신번호 | O |
| `SOLAPI_RECIPIENT` | 수신번호 | O |
| `SOLAPI_KAKAO_PF_ID` | 카카오 비즈니스 채널 ID | 카카오 발송 시 |
| `SOLAPI_KAKAO_TEMPLATE_ID` | 카카오 알림톡 템플릿 ID | 알림톡 발송 시 |

### 실행 명령어

```bash
# Kotlin 예제 실행
./gradlew :solapi-kotlin-example-kotlin:run -Pexample=SendSms

# Java 예제 실행
./gradlew :solapi-kotlin-example-java:run -Pexample=SendSms
```

### 예제 목록

| 예제 | 설명 |
|------|------|
| `SendSms` | SMS 단건 발송 |
| `SendMms` | MMS 이미지 첨부 발송 |
| `SendBatch` | 대량 메시지 발송 |
| `SendScheduled` | 예약 발송 |
| `SendVoice` | 음성 메시지 발송 |
| `KakaoAlimtalk` | 카카오 알림톡 발송 |
| `KakaoBrandMessage` | 카카오 브랜드 메시지 발송 |
| `GetBalance` | 잔액 조회 |
| `GetMessageList` | 발송 내역 조회 |

## 지원 메시지 타입

| 타입 | 설명 |
|------|------|
| `SMS` | 단문문자 (80 byte 미만) |
| `LMS` | 장문문자 (80 byte 이상, 2,000 byte 미만) |
| `MMS` | 이미지 포함 문자 (200KB 이내 이미지 1장) |
| `ATA` | 카카오 알림톡 |
| `CTA` / `CTI` | 카카오 친구톡 / 이미지 친구톡 |
| `BMS_*` | 카카오 브랜드 메시지 (TEXT, IMAGE, WIDE, WIDE_ITEM_LIST, FREE) |
| `RCS_*` | RCS 문자 (SMS, LMS, MMS, TPL) |
| `NSA` | 네이버 스마트 알림 |
| `FAX` | 팩스 |
| `VOICE` | 음성 메시지 |

## 주요 기능

### MMS 이미지 첨부 발송

```kotlin
// 이미지 업로드
val imageId = messageService.uploadFile(imageFile, StorageType.MMS)

// MMS 발송
val message = Message(
    type = MessageType.MMS,
    from = "발신번호",
    to = "수신번호",
    text = "MMS 메시지 내용",
    subject = "MMS 제목",
    imageId = imageId
)
messageService.send(message)
```

**이미지 규격:** JPG/JPEG, 최대 200KB, 권장 해상도 1000x1000 이하

### 대량 메시지 발송

```kotlin
val messages = (1..100).map { i ->
    Message(from = sender, to = "010XXXX000$i", text = "메시지 $i")
}

// 중복 수신번호 허용 옵션
val config = SendRequestConfig(allowDuplicates = true)
messageService.send(messages, config)
```

- 한 번에 최대 **10,000건** 발송 가능
- `allowDuplicates = true`로 동일 수신번호 중복 발송 허용

### 예약 발송

```kotlin
val config = SendRequestConfig().apply {
    setScheduledDateFromLocalDateTime(
        LocalDateTime.now().plusMinutes(10),
        ZoneId.of("Asia/Seoul")
    )
}
messageService.send(message, config)
```

- 최소 **10분 후**부터 최대 **6개월 이내** 예약 가능
- 과거 시간 지정 시 즉시 발송 처리

### 카카오 알림톡

```kotlin
val message = Message(
    type = MessageType.ATA,
    from = "발신번호",
    to = "수신번호",
    kakaoOptions = KakaoOption(
        pfId = "카카오채널ID",
        templateId = "템플릿ID",
        variables = mapOf("name" to "홍길동", "code" to "123456")
    )
)
messageService.send(message)
```

- 검수 승인된 템플릿만 사용 가능
- 정보성 메시지 전용 (광고 불가)

## API 레퍼런스

### 메시지 발송

| 메서드 | 설명 |
|--------|------|
| `send(message)` | 단건 메시지 발송 |
| `send(messages)` | 다건 메시지 발송 (최대 10,000건) |
| `send(message, config)` | 설정과 함께 발송 (예약, 중복 허용 등) |
| `uploadFile(file, type)` | 파일 업로드 (MMS, FAX 등) |

### 조회

| 메서드 | 설명 |
|--------|------|
| `getBalance()` | 잔액 조회 |
| `getQuota()` | 일일 발송량 한도 조회 |
| `getMessageList(request)` | 메시지 발송 내역 조회 |

### 카카오 템플릿 관리

| 메서드 | 설명 |
|--------|------|
| `getKakaoAlimtalkTemplates()` | 알림톡 템플릿 목록 조회 |
| `getKakaoAlimtalkTemplate(id)` | 알림톡 템플릿 상세 조회 |
| `createKakaoAlimtalkTemplate(request)` | 알림톡 템플릿 생성 |
| `getSendableKakaoAlimtalkTemplates()` | 발송 가능한 템플릿 조회 |
| `getKakaoBrandMessageTemplates()` | 브랜드 메시지 템플릿 조회 |

## 에러 처리

```kotlin
try {
    messageService.send(message)
} catch (e: SolapiBadRequestException) {
    println("잘못된 요청: ${e.message}")
} catch (e: SolapiInvalidApiKeyException) {
    println("잘못된 API 키: ${e.message}")
} catch (e: SolapiMessageNotReceivedException) {
    println("발송 실패: ${e.message}")
} catch (e: SolapiException) {
    println("기타 오류: ${e.message}")
}
```

| 예외 클래스 | 설명 |
|-------------|------|
| `SolapiBadRequestException` | 잘못된 요청 파라미터 |
| `SolapiInvalidApiKeyException` | 유효하지 않은 API 키 |
| `SolapiApiKeyException` | API 키 관련 오류 |
| `SolapiFileUploadException` | 파일 업로드 실패 |
| `SolapiMessageNotReceivedException` | 메시지 수신 실패 |
| `SolapiEmptyResponseException` | 빈 응답 수신 |
| `SolapiUnknownException` | 알 수 없는 오류 |

## 요구 사항

- **Java 8** 이상
- **Kotlin 1.8** 이상 (Kotlin 사용 시)

## 관련 링크

- [SOLAPI 공식 문서](https://docs.solapi.com/)
- [API 키 발급](https://console.solapi.com/)
- [발신번호 등록](https://console.solapi.com/senderids)
- [GitHub Issues](https://github.com/solapi/solapi-kotlin/issues)
- [API Reference (Dokka)](https://solapi.github.io/solapi-kotlin/)

## 라이선스

MIT License - 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.
