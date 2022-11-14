FROM openjdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} messenger.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/messenger.jar"]