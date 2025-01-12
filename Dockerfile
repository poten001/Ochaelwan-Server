FROM amd64/amazoncorretto:17
WORKDIR /app
COPY ./build/libs/TCC-0.0.1-SNAPSHOT.jar /app/TCC-SERVER.jar
CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "TCC-SERVER.jar"]