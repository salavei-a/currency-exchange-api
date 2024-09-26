FROM maven:3 AS maven_builder
WORKDIR /app
COPY pom.xml ./pom.xml
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package

FROM tomcat:jre17
COPY --from=maven_builder /app/target/*.war /usr/local/tomcat/webapps/api.war
CMD ["catalina.sh", "run"]