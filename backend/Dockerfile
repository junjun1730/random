# 개발용 Dockerfile
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Gradle Wrapper 및 설정 먼저 복사 (캐시 활용)
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# gradlew 실행 권한 부여
RUN chmod +x gradlew

# 의존성 미리 다운로드 (캐시 활용)
RUN ./gradlew dependencies --no-daemon || true

# 소스코드 복사
COPY src src

EXPOSE 8080

# Spring Boot 실행
CMD ["./gradlew", "bootRun", "--no-daemon"]
