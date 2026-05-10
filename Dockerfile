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

# Remove default Tomcat apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the built WAR from the build stage
COPY --from=build /app/target/doctor-patient-management-system.war /usr/local/tomcat/webapps/ROOT.war

# Copy startup script that injects env vars into context.xml
COPY docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["docker-entrypoint.sh"]
