# statistics-service
Service for giving statistics of transactions recorded.

Project Structure :
1. logging : holds logging configurations
2. interfaces : contains interfaces that will be exposed to controller
3. common : common utitlity classes
4. repository : exposed api's which interacts with data store.
5. modules : implementating interfaces, service level implementation
6. rest-application : controller with rest api's
7. main-application : spring boot main application class
8. parent : contains pom.xml used for dependency management in sub modules

To run statistics-service : run 'mvn clean install' in statistics-service.

To deploy application : goto /main-application/target/ and run
java -jar statisticsServiceMainApp-1.0.0-SNAPSHOT.jar

It will deploy application on localhost:8181
