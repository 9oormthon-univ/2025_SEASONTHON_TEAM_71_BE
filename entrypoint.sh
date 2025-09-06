#!/bin/sh

# GOOGLE_APPLICATION_CREDENTIALS_JSON 환경 변수의 내용이 존재하는지 확인
if [ -n "$GOOGLE_APPLICATION_CREDENTIALS_JSON" ]; then
  # 환경 변수의 내용을 /tmp/gcp-key.json 파일로 저장
  echo "$GOOGLE_APPLICATION_CREDENTIALS_JSON" > /tmp/gcp-key.json

  # Google Cloud 라이브러리가 사용하는 표준 환경 변수에, 방금 만든 파일의 경로를 지정
  export GOOGLE_APPLICATION_CREDENTIALS=/tmp/gcp-key.json
fi

# 원래 Dockerfile의 ENTRYPOINT 였던 java 실행 명령어를 실행
# exec: 현재 셸 프로세스를 java 프로세스로 대체하여 효율적으로 실행
exec java -jar /app.jar