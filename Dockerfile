FROM amazoncorretto:21

EXPOSE 4000

COPY target/polirubro-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]