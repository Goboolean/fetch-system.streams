FROM gradle:jdk11 AS build

WORKDIR /home/gradle/project

COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/project/

COPY --chown=gradle:gradle src /home/gradle/project/src

RUN gradle build --no-daemon

FROM openjdk:11-jre-slim

WORKDIR /app

COPY --from=build /home/gradle/project/build/libs/*.jar /app/myapp.jar

ENTRYPOINT ["java", "-jar", "/app/myapp.jar"]

EXPOSE 8080