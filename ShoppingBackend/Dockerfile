FROM openjdk:8-jdk-alpine
## build with gradlew build
COPY build/libs/ShoppingBackend-1.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]