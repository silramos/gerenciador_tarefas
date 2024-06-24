FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/gerenciadortarefas-1.0.0.jar /app/gerenciadortarefas-1.0.0.jar
CMD ["java", "-jar", "/app/gerenciadortarefas-1.0.0.jar"]
