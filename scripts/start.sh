IMAGE_FILE_PATH="/home/ubuntu/deploy/image.txt" # 새로 실행할 이미지의 버전 이름이 저장된 파일
IMAGE_NAME=$(cat "$IMAGE_FILE_PATH") # image.txt 파일의 내용을 출력한 후, 해당 내용을 IMAGE_NAME의 변수 값으로 저장

# 새로운 버전의 도커 이미지 실행
echo "IMAGE_NAME: $IMAGE_NAME 도커 실행"
# 8080 포트로 WAS 이미지 실행
docker run -d -p 8080:8080 ${IMAGE_NAME}
