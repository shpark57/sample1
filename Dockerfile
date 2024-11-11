# 1. Java 17 JDK 이미지를 기본 이미지로 사용
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리를 /app으로 설정
WORKDIR /app

# 3. 로컬에서 빌드한 Spring Boot JAR 파일을 컨테이너로 복사
#    (예: target 폴더에 있는 sample1.jar 파일)
COPY build/libs/sample1-0.0.1-SNAPSHOT.jar /app/sample1.jar


# 4. 8080 포트 노출 (Spring Boot 기본 포트)
EXPOSE 8080

# 5. 애플리케이션을 실행할 명령어
CMD ["java", "-jar", "/app/sample1.jar"]
