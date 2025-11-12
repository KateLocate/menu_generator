FROM alpine:3.22 AS build
WORKDIR /app
RUN apk add npm make maven
COPY . .
RUN make react
RUN make jar

FROM bitnami/java:latest
WORKDIR /app
COPY --from=build app/backend/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
