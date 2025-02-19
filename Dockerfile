FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/app-1.jar app-1.jar
EXPOSE 9081
ENTRYPOINT ["java","-jar","/app-1.jar"]