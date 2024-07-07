# Use a base image that includes Java
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file to the container
COPY target/hook4startup-backend-java.jar /app/hook4startup-backend-java.jar

# Expose the port your application runs on (if necessary)
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar",  "hook4startup-backend-java.jar", "--spring.profiles.active=dev"]

# Command to run the application
ENTRYPOINT ["java", "-jar", "hook4startup-backend-java.jar"]
