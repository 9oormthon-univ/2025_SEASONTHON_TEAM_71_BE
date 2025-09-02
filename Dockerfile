FROM openjdk:17

# 빌드된 Jar 파일 경로를 인자로 받음
ARG JAR_FILE=build/libs/*.jar

# Jar 파일을 app.jar 라는 이름으로 이미지에 복사
COPY ${JAR_FILE} app.jar

# entrypoint.sh 스크립트를 이미지 루트 경로에 복사
COPY entrypoint.sh /entrypoint.sh

# entrypoint.sh 스크립트에 실행 권한 부여
RUN chmod +x /entrypoint.sh

# 컨테이너가 노출할 포트
EXPOSE 8080

# 컨테이너 시작 시 java 앱 대신 entrypoint.sh 스크립트 실행
ENTRYPOINT ["/entrypoint.sh"]