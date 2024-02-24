#
# Set a variable that can be used in all stages.
#
ARG BUILD_HOME=/rinha-backend-kotlin

#
# Gradle image for the build stage.
#
FROM gradle:7.5.1 as build-image

#
# Set the working directory.
#
ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME
WORKDIR $APP_HOME

#
# Copy the Gradle config, source code, and static analysis config
# into the build container.
#
COPY --chown=gradle:gradle build.gradle.kts settings.gradle.kts gradle.properties $APP_HOME/
COPY --chown=gradle:gradle src $APP_HOME/src
# COPY --chown=gradle:gradle config $APP_HOME/config

#
# Build the application.
#
RUN gradle --no-daemon -x test clean build

#
# Java image for the application to run in.
#
FROM openjdk:17.0.2-jdk-oracle

#
# Copy the jar file in and name it app.jar.
#
ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME
COPY --from=build-image $APP_HOME/build/libs/rinha-backend-kotlin-*-all.jar app.jar
EXPOSE 9001
ENTRYPOINT ["java","-jar","app.jar"]
