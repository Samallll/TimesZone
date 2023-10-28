
# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY application.properties .
RUN mvn clean install

# Stage 2: Run the application
FROM openjdk:17-alpine
WORKDIR /app
ENV PORT 8080
COPY --from=build /app/target/timezone-jar.jar ./timeszone-aws.jar
EXPOSE 8080