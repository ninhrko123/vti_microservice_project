FROM openjdk:17-jdk

WORKDIR /app

COPY target/eureka-server-0.0.1-SNAPSHOT.jar /app/eureka-server-0.0.1-SNAPSHOT.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar" ,"/app/eureka-server-0.0.1-SNAPSHOT.jar*"]