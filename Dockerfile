FROM jelastic/maven:3.9.5-openjdk-21 AS build
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn clean package
FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /build/target/courier-tracking-service-0.0.1-SNAPSHOT.jar /app/
VOLUME /tmp
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
ENTRYPOINT ["sh", "-c", "/wait-for-it.sh courier-postgis:5432 -- java -jar /app/courier-tracking-service-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
