
# Stage 1: Install jdk and maven
FROM openjdk:17-alpine
RUN apk add --no-cache maven
WORKDIR /app
COPY src ./src
COPY pom.xml ./pom.xml
COPY entrypoint.sh ./entrypoint.sh
COPY docker-compose.yml ./docker-compose.yml
RUN chmod +x ./entrypoint.sh
ENV PORT 8080
EXPOSE 8080
