FROM openjdk:17 AS build
COPY ./ /home/app
RUN cd /home/app && ./gradlew build

FROM openjdk:17-alpine
COPY --from=build /home/app/build/libs/SimulateAssetFormationWithNisa-0.0.1-SNAPSHOT.jar /usr/local/lib/SimulateAssetFormationWithNisa.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar","-Dfile.encoding=UTF-8","/usr/local/lib/SimulateAssetFormationWithNisa-0.0.1-SNAPSHOT.jar"]