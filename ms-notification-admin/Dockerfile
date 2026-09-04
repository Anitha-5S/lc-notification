FROM amazoncorretto:15
VOLUME /tmp
COPY ./build/libs/ms-notification-admin-2.0.0.jar app.jar
EXPOSE 1970
ENTRYPOINT ["java","-jar","/app.jar"]