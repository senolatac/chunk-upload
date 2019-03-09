FROM java:8
VOLUME /tmp
COPY wait-for-it.sh /wait-for-mysql.sh
RUN chmod +x /wait-for-mysql.sh
ADD build/libs/file-uploader-server-0.0.1.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container","-jar","/app.jar"]