# 소스 빌드용 Base 이미지(Gradle,JDK 17 - 알파인 버전(경량화))
FROM gradle:8.6.0-jdk17-alpine
WORKDIR /build

# 빌드에 필요한 파일들을 컨테이너에 복사
COPY build.gradle settings.gradle ./
# Gradle 빌드 명령어 실행
RUN gradle dependencies --no-daemon

COPY . /build
RUN gradle build --no-daemon


ENTRYPOINT ["java", "-jar"]


