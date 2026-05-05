FROM tomcat:9.0-jdk21

# Enable environment variable resolution in context.xml
ENV CATALINA_OPTS="-Dorg.apache.tomcat.util.digester.PROPERTY_SOURCE=org.apache.tomcat.util.digester.EnvironmentPropertySource"

# Remove default Tomcat apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR file into Tomcat as ROOT
COPY doctor-patient-management-system.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]