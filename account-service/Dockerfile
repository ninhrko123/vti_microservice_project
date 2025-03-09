FROM openjdk:17-jdk

LABEL authors="ninh"

WORKDIR /app

COPY target/account-service-0.0.1-SNAPSHOT.jar /app/account-service-0.0.1-SNAPSHOT.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar" ,"/app/account-service-0.0.1-SNAPSHOT.jar*"]