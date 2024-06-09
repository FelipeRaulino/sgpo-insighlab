FROM maven:3.9.7-eclipse-temurin-21 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
#ARG JAR_FILE=target/*.jar
COPY --from=build /home/app/target/*.jar /usr/local/lib/demo.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "/usr/local/lib/demo.jar"]