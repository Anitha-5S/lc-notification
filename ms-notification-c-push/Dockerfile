FROM amazoncorretto:15
VOLUME /tmp
COPY ./build/libs/ms-notification-c-push-2.0.0.jar app.jar
EXPOSE 1974
ENTRYPOINT ["java","-jar","/app.jar"]