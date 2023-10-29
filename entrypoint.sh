#!/bin/sh

# Step 1: Navigate to the app directory
cd /app

# Step 3: Build the project with Maven
mvn clean install

# Step 4: Run the Spring Boot application
java -jar "target/timeszone-jar.jar"
