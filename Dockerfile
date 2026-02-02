FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

RUN chmod +x mvnw && ./mvnw clean package -DskipTests && mv target/bank-1.0.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
