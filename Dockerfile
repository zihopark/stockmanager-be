# 1. 베이스 이미지 설정 (JDK 17 사용)
FROM eclipse-temurin:17-jdk-focal

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. Gradle Wrapper 및 소스 복사
COPY . .

# 4. Gradle 빌드 수행 (테스트 제외, JAR 파일 생성)
RUN chmod +x ./gradlew
RUN ./gradlew build -x test

# 5. JAR 파일 실행 설정
CMD ["java", "-jar", "build/libs/stockmanager-be-0.0.1-SNAPSHOT.jar"]

