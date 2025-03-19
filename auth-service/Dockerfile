FROM openjdk:17-jdk

WORKDIR /app

COPY target/auth-service-0.0.1-SNAPSHOT.jar /app/auth-service-0.0.1-SNAPSHOT.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar" ,"/app/auth-service-0.0.1-SNAPSHOT.jar*"]