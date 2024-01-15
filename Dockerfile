FROM maven:3-openjdk-17 AS build
COPY . .
RUN mvn clean package -Dmaven.test.skip=true
FROM openjdk:17-alpine
COPY --from=build /target/simulateAssetFormationWithNISA-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar", "demo.jar"]