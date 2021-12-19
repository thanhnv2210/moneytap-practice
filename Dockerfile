FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} firstPractice.jar
ENTRYPOINT ["java","-DlogFileLocation=/opt/apps/moneytap/logs/first-practice","-jar","/firstPractice.jar"]