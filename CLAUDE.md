# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

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
