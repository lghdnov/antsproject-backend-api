FROM eclipse-temurin:17.0.8_7-jre
EXPOSE 8080
WORKDIR /opt/app
COPY build/libs/*.jar app.jar
RUN mkdir data
ENTRYPOINT ["java", "-jar", "app.jar"]