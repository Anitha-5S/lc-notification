FROM amazoncorretto:15
VOLUME /tmp
COPY ./build/libs/ms-notification-p-2.0.0.jar app.jar
EXPOSE 1972
ENTRYPOINT ["java","-jar","/app.jar"]