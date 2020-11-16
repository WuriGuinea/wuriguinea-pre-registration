FROM openjdk:8

ENV SENDRIG_API_KEY==${key}

WORKDIR /app

COPY ./target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]



