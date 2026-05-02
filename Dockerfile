FROM tomcat:9.0-jdk21

# Remove default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your WAR into Tomcat as ROOT
COPY doctor-patient-management-system.war
/usr/local/tomcat/webapps/ROOT.war
	 
EXPOSE 8080

CMD ["cataline.sh", "run"]