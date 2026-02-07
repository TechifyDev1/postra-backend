FROM maven:3.9.10-openjdk-23.0.1-almalinux-9 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:27-ea-oraclelinux9
COPY --from=build /target/postra-0.0.1-SNAPSHOT.jar demo.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "demo.jar"]