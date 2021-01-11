FROM openjdk:8
ADD target/mongodbapplication-0.0.1-SNAPSHOT.jar .
EXPOSE 8000
CMD java -jar mongodbapplication-0.0.1-SNAPSHOT.jar