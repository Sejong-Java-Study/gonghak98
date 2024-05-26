CONTAINER_NAME=gonghak-backend

# 도커 컨테이너가 있는지 확인 (-a 옵션으로 정지된 컨테이너도 확인)
RUNNING_CONTAINER_ID=$(docker ps -aq --filter "name=$CONTAINER_NAME")
echo "실행중인 컨테이너 ID: $RUNNING_CONTAINER_ID"

# 이전 도커 컨테이너 종료
if [ -n "$RUNNING_CONTAINER_ID" ]; then # RUNNING_CONTAINER_ID의 길이가 0이 아니면 참(-n 옵션)
  echo "이전 도커 컨테이너 종료 및 삭제"
  docker stop $CONTAINER_NAME && docker rm $CONTAINER_NAME
fi # if문 종료 코드
