# Step 1: Use an image with Maven and JDK installed
FROM maven:latest AS build

# Set the working directory inside the container
WORKDIR /app

# Step 2: Copy the entire project to the container
COPY . .

# Step 3: Build the Spring Boot application
RUN mvn clean package -DskipTests

# Step 4: Use java image to run the application
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Step 5: Copy the built .jar file from the build stage
COPY --from=build /app/target/receipt-processor-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 for the Spring Boot application
EXPOSE 8080

# Step 6: Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
