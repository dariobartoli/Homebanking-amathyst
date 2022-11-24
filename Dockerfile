FROM gradle:7.5.1-jdk11-alpine
COPY . .
RUN gradle build
EXPOSE 8080
ENTRYPOINT ["java","-jar","build/libs/homebanking-0.0.1-SNAPSHOT.jar"]

# docker run --rm -u gradle -v "$PWD":/home/gradle/project -w /home/gradle/project gradle gradle <gradle-task>