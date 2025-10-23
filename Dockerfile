# -------------------------------
# Stage 1: Build ứng dụng với Maven và JDK 17
# -------------------------------
FROM maven:3.9.9-eclipse-temurin-17 AS builder

# Đặt thư mục làm việc trong container
WORKDIR /app

# Sao chép file pom.xml trước để tận dụng cache của Docker (chỉ tải dependency khi cần)
COPY app/pom.xml .

# Sao chép mã nguồn ứng dụng
COPY app/src ./src

# Biên dịch và đóng gói ứng dụng (bỏ qua test để tăng tốc độ build)
RUN mvn clean package -DskipTests


# -------------------------------
# Stage 2: Chạy ứng dụng với lightweight JRE 17
# -------------------------------
FROM eclipse-temurin:17.0.15_6-jre

# Đặt thư mục làm việc
WORKDIR /app

# Sao chép file JAR đã build từ stage trước
COPY --from=builder /app/target/back-end-0.0.1-SNAPSHOT.jar app.jar

# Mở cổng 8080 cho ứng dụng Spring Boot
EXPOSE 8080

# Định nghĩa lệnh khởi chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
