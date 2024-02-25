FROM gradle:jdk21-alpine AS build
COPY --chown=gradle:gradle . /src
WORKDIR /src
RUN gradle build

FROM openjdk:21
RUN mkdir /app
COPY --from=build /src/build/libs/trb-loans-0.0.1.jar /app/trb-loans-0.0.1.jar

ENTRYPOINT ["java", "-jar","/app/trb-loans-0.0.1.jar"]