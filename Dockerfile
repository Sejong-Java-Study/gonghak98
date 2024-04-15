# 빌드된 파일을 실행하는 Base 이미지 : JRE만 포함된 이미지
FROM amazoncorretto:17-alpine3.19

WORKDIR /app

# 빌드된 jar 파일을 "app.jar"라는 이름으로 복사
COPY build/libs/*.jar /app/app.jar

# 어플리케이션 실행 (/ 루트(절대 경로)에 있는 app.jar 파일을 실행)
ENTRYPOINT ["java","-jar","app.jar"]
