# ============================================
# Stage 1: Build the WAR with Maven
# ============================================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml first to cache dependency downloads
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================================
# Stage 2: Deploy to Tomcat
# ============================================
FROM tomcat:9.0-jdk21

# Enable environment variable resolution in context.xml
ENV CATALINA_OPTS="-Dorg.apache.tomcat.util.digester.PROPERTY_SOURCE=org.apache.tomcat.util.digester.EnvironmentPropertySource"

# Remove default Tomcat apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the built WAR from the build stage
COPY --from=build /app/target/doctor-patient-management-system.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
