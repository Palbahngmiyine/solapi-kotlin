# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Principles

### Tidy First (Kent Beck)

코드 수정 및 기능 추가 시 반드시 Kent Beck의 **Tidy First** 원칙을 적용합니다.

> References:
> - https://tidyfirst.substack.com/p/augmented-coding-beyond-the-vibes
> - https://tidyfirst.substack.com/p/taming-the-genie-like-kent-beck

#### 핵심 원칙: 구조적 변경과 행위적 변경의 분리

모든 코드 변경은 두 가지 유형으로 분류됩니다:

| 유형 | 설명 | 예시 |
|------|------|------|
| **구조적 변경 (Structural)** | 동작 변경 없이 코드 구조만 개선 | 리네이밍, 메서드 추출, 파일 재구성, 중복 제거 |
| **행위적 변경 (Behavioral)** | 실제 기능 추가 또는 수정 | 새 API 추가, 버그 수정, 로직 변경 |

**절대 규칙**: 구조적 변경과 행위적 변경을 **하나의 커밋에 혼합하지 않습니다**.

#### 작업 순서

1. **Tidy First**: 기능 추가 전, 해당 영역의 코드를 먼저 정리
   - 변수/함수명 명확하게 변경
   - 복잡한 메서드 분리 (작고 특화된 클래스, 단일 책임)
   - 중복 코드 제거
   - 테스트 실행하여 동작 보존 확인
   - **별도 커밋으로 분리** (예: `refactor: ...`)

2. **Behavioral Change**: 정리된 코드 위에 기능 구현
   - TDD 사이클 적용: Red → Green → Refactor
   - 가장 단순한 실패 테스트 먼저 작성
   - 테스트 통과를 위한 최소한의 코드 구현
   - **별도 커밋으로 분리** (예: `feat: ...`, `fix: ...`)

#### AI(Claude) 코딩 지침

Kent Beck 스타일로 코드를 작성합니다:

1. **페르소나 적용**: "code like Kent Beck"
   - 모듈형 단위 테스트 작성 (모놀리식 테스트 스크립트 지양)
   - 명확한 변수/함수 명명
   - TDD 스타일 개발 습관

2. **아키텍처 명시**
   - 적절한 디자인 패턴 선택 및 적용
   - 작고 특화된 클래스로 행위 분리
   - 단일 책임 원칙 준수

3. **변경 분리 필수**
   - 구조적 변경 요청 시: 동작 변경 없이 리팩토링만 수행
   - 기능 추가 요청 시: 필요한 경우 먼저 tidy 커밋을 분리하여 제안

#### 커밋 전략

```bash
# 좋은 예 - 분리된 커밋
git commit -m "refactor: extract validation logic to separate method"
git commit -m "feat: add phone number format validation"

# 나쁜 예 - 혼합된 커밋 (금지)
git commit -m "feat: add validation and refactor code"
```

#### 체크리스트

**기능 추가 또는 코드 수정 전:**
- [ ] 수정할 영역에 정리가 필요한 코드가 있는가?
- [ ] 있다면, 구조적 변경을 먼저 별도 커밋으로 완료했는가?
- [ ] 구조적 변경 후 테스트가 통과하는가?

**기능 구현 시:**
- [ ] 테스트를 먼저 작성했는가? (TDD)
- [ ] 최소한의 코드로 테스트를 통과시켰는가?
- [ ] 행위적 변경만 포함된 커밋인가?
- [ ] 클래스/메서드가 단일 책임을 가지는가?

## Development Commands

### Build and Test
```bash
# Clean build and run tests
./gradlew clean build test

# Run tests only
./gradlew test

# Run a single test
./gradlew test --tests "ClassName.methodName"

# Build without tests
./gradlew build -x test
```

### Documentation Generation
```bash
# Generate Dokka documentation (outputs to ./docs/)
./gradlew dokkaGeneratePublicationHtml

# Generate Javadoc JAR
./gradlew dokkaJavadocJar

# Generate sources JAR
./gradlew sourcesJar
```

### Publishing
```bash
# Publish to Maven Central and auto-release
./gradlew publishAndReleaseToMavenCentral

# Build shadow JAR with relocated dependencies
./gradlew shadowJar
```

## Project Architecture

### Package Structure
```
com.solapi.sdk/
├── SolapiClient.kt              # Main entry point, factory methods
├── NurigoApp.kt                 # Application configuration
└── message/
    ├── dto/
    │   ├── request/             # API request DTOs
    │   │   └── kakao/           # Kakao-specific requests
    │   └── response/            # API response DTOs
    │       ├── common/          # Shared response types
    │       └── kakao/           # Kakao-specific responses
    ├── exception/               # Custom exception hierarchy
    ├── lib/                     # Utility classes (Authenticator, helpers)
    ├── model/                   # Core domain models
    │   ├── fax/                 # Fax options
    │   ├── group/               # Group messaging models
    │   ├── kakao/               # Kakao templates and options
    │   ├── naver/               # Naver options
    │   ├── rcs/                 # RCS options
    │   └── voice/               # Voice message options
    └── service/                 # Service layer implementations
```

### Core Structure
- **Package Migration**: Recently migrated from `net.nurigo.sdk` to `com.solapi.sdk`
- **Main Entry Point**: `SolapiClient` object provides factory methods for creating service instances
- **Service Layer**: `DefaultMessageService` implements all messaging functionality
- **HTTP Layer**: Uses Retrofit with OkHttp for API communication with automatic authentication

### Key Components

#### Message System
- **Message Model**: `Message.kt` - Core data class with support for SMS, LMS, MMS, Kakao, RCS, Fax, and Voice
- **Service Interface**: `MessageService` defines the contract, `DefaultMessageService` provides implementation
- **HTTP Service**: `MessageHttpService` handles Retrofit API definitions

#### Authentication
- **Authenticator**: `Authenticator.kt` handles HMAC-based API authentication
- **Auto-injection**: Authentication headers are automatically added via OkHttp interceptor

#### Exception Hierarchy
- `SolapiException` - Base exception class
  - `SolapiApiKeyException` - API key related errors
  - `SolapiInvalidApiKeyException` - Invalid API key
  - `SolapiBadRequestException` - Bad request errors
  - `SolapiEmptyResponseException` - Empty response from server
  - `SolapiFileUploadException` - File upload failures
  - `SolapiMessageNotReceivedException` - Message delivery failures
  - `SolapiUnknownException` - Unclassified errors

#### Specialized Features
- **Kakao Integration**: Full support for Alimtalk and Brand Message templates
- **File Upload**: Base64 encoding for MMS, Fax, and other file-based messages
- **Error Handling**: Custom exception hierarchy with specific error types

### Data Flow
1. Client creates `SolapiClient` instance with API credentials
2. `SolapiClient` instantiates `DefaultMessageService` with authenticated HTTP client
3. Service methods use `MessageHttpService` (Retrofit) for API calls
4. Responses are deserialized using Kotlinx Serialization
5. Errors are mapped to specific exception types

## Build Configuration
- **Target**: Java 8 compatibility
- **Kotlin**: Version 2.2.10 with kotlinx-serialization
- **Gradle**: Version 8.14.3 (via wrapper)
- **Dependencies**: OkHttp 5.1.0, Retrofit 3.0.0, Kotlinx Serialization 1.9.0, Apache Commons Codec 1.18.0
- **Shadow JAR**: Dependencies relocated to `com.solapi.shadow` namespace
- **Version Generation**: Build script auto-generates `Version.kt` at `build/generated/source/kotlin/com/solapi/sdk/Version.kt`

## Testing
- **Framework**: JUnit 5 Jupiter (configured but no tests written yet)
- **Run Command**: `./gradlew test`
- **Test Location**: `src/test/java/` (to be created)

## Documentation
- **API Docs**: Generated with Dokka to `./docs/` directory
- **Examples**: Referenced external repository for usage examples
