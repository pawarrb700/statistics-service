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

Statistics service exposes two apis :
1. /transactions : This POST api records transactions with amount of transaction and timestamp i.e transaction time.
2. /statistics : This GET api returns statistics of transactions from last 60 seconds. Statistics involves sum of transaction amount, average of amount, maximum and minimum amount amongs transactions and count of transaction.
 
To run statistics-service : run 'mvn clean install' in statistics-service.

To deploy application : goto /main-application/target/ and run
java -jar statisticsServiceMainApp-1.0.0-SNAPSHOT.jar

It will deploy application on localhost:8181
