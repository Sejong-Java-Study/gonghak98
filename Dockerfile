# 소스 빌드용 Base 이미지(Maven, Gradle에 호환되는 JDK 이미지)
FROM eclipse-temurin:17-alpine

WORKDIR /app

# 의존성을 가져오기 위해 필요한 파일만 우선 복사
COPY gradle gradle
COPY build.gradle settings.gradle gradlew ./

# Gradle 빌드 명령어 실행 (CI/CD 환경에서 일관된 빌드를 위해 데몬 프로세스 실행 X)
RUN ./gradlew dependencies --no-daemon


