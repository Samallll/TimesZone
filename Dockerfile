
# Stage 2: Run the application
FROM openjdk:17-alpine
RUN apk add --no-cache maven
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY /src/main/resources/application.properties .
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]
ENV PORT 8080
