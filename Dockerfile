FROM openjdk:8-jdk-alpine

MAINTAINER jm5619

RUN mkdir /app

WORKDIR /app

ADD ./target/ir-messaging-1.0.0-SNAPSHOT.jar /app

EXPOSE 8086

CMD ["java", "-jar", "ir-messaging-1.0.0-SNAPSHOT.jar"]
