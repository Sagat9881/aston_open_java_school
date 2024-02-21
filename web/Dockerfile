#FROM tomcat:9-jre8-alpine
#
#ARG JAR_FILE=target/*.war
#COPY ${JAR_FILE} /usr/local/tomcat/webapps/healtservice.war
#
#WORKDIR /usr/local/tomcat
## Открываем порт 8080 для контейнера, т.к. его слушает томкат
#EXPOSE 8080

FROM maven:3.5-jdk-8 AS buildserver
WORKDIR /usr/mygitproject
ENV SERV_GIT;
RUN git clone ${SERV_GIT}  /usr/mygitproject
WORKDIR /usr/mygitproject /src
RUN mvn -f /usr/mygitproject /pom.xml clean package -DskipTests
FROM tomcat:latest
COPY --from=buildserver /usr/mygitproject /target/*.war /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]

EXPOSE 8080