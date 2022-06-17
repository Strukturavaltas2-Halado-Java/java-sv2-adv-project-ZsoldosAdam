FROM eclipse-temurin:18
WORKDIR /opt/app
COPY target/*.jar languageschool.jar
CMD ["java", "-jar", "languageschool.jar"]