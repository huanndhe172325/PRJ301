# ---------------------------------------------------------
# 🧱 Stage 1: Build the Spring Boot application with Maven and JDK 17
# 🇻🇳 Giai đoạn 1: Build ứng dụng Spring Boot bằng Maven và JDK 17
# ---------------------------------------------------------
FROM maven:3.9.9-eclipse-temurin-17 AS builder

# Set working directory inside the container
# 🇻🇳 Đặt thư mục làm việc trong container
WORKDIR /app

# Copy pom.xml first to leverage Docker cache for dependency installation
# 🇻🇳 Sao chép pom.xml trước để Docker chỉ tải lại dependency khi cần
COPY app/pom.xml .

# Copy the source code into the container
# 🇻🇳 Sao chép mã nguồn ứng dụng
COPY app/src ./src

# Build the application and skip tests to speed up build time
# 🇻🇳 Biên dịch và đóng gói ứng dụng (bỏ qua test để tăng tốc độ build)
RUN mvn clean package -DskipTests


# ---------------------------------------------------------
# 🚀 Stage 2: Run the Spring Boot JAR using lightweight JRE 17
# 🇻🇳 Giai đoạn 2: Chạy ứng dụng với JRE 17 nhẹ
# ---------------------------------------------------------
FROM eclipse-temurin:17.0.15_6-jre

# Set working directory for runtime container
# 🇻🇳 Đặt thư mục làm việc khi chạy ứng dụng
WORKDIR /app

# Copy the built JAR file from the builder stage
# 🇻🇳 Sao chép file JAR đã build từ giai đoạn trước
COPY --from=builder /app/target/back-end-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 for Spring Boot
# 🇻🇳 Mở cổng 8080 cho ứng dụng Spring Boot
EXPOSE 8080

# Run the application
# 🇻🇳 Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
