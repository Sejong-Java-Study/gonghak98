#!bin/sh
set -e # 실패한 명령어가 있을 경우, 스크립트 실행 중지 (모든 명령어가 순차적으로 실행되어야 하기 때문)

# envsubst SHELL-FORMAT을 통해 특정 변수를 환경변수로 바인딩 후, default.conf 파일로 덮어쓰기
# 컨테이너를 실행할 때, WAS_HOST, WAS_PORT에 대한 환경변수를 주입하면(-e 옵션) 이 변수 값이 envsubst 명령어와 만나
# default.conf.template에 있는 WAS_HOST와 WAS_PORT의 값을 변경
# 변경된 템플릿 파일을 Nginx 설정파일인 default.conf로 복사 -> 최종 실행되는 Nginx 컨테이너는 동적인 환경변수 값을 갖게 됨
envsubst '${WAS_HOST} ${WAS_PORT}' < /etc/nginx/conf.d/default.conf.template > /etc/nginx/conf.d/default.conf

# 도커파일에서 CMD 값, 옵션으로 전달한 nginx -g daemon off 명령어를 실행함
# .sh 파일의 명령어와 도커파일의 CMD 명령어를 순차적으로 실행할 수 있도록 하는 명령어
exec "$@"
