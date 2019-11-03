# Implementation of transaction service

## How to build the service

### Using an IDE

1. Clone or download the repository
2. Open the project in your favorite IDE (e.q. Intellij Idea)
3. The service uses an in-memory database, so no need to configure it. See initial data in `data.sql`.
4. Run spring-boot `Application` class of `transaction-service-ws` module. The service will be running on port `8080`

### Using command line

1. Clone or download the repository
2. Build the project using `mvn clean package`
3. The service uses an in-memory database, so no need to configure it. See initial data in `data.sql`.
4. Start the service: `java -jar transaction-service-ws/target/transaction-service-ws-1.0-SNAPSHOT.jar`.


## How to use the service

1. Open swagger-api ui: `http://localhost:8080/swagger-ui.html`
2. Here you can see all available endpoints and models and try to execute examples.

## HTTP java client

If you need to use the service from another java program, you can use client provided by the service. The usage is pretty simple. Just build and import maven module `transaction-service-client`. If your service is using Spring you need to import the module in your configuration: `@Import(TransactionServiceClientConfig.class)` and override the service base url if necessary: `-Dtransaction-service.base.url=YOUR_SERVICE_URL`. If you use plain java program you can create the client by your self. See a usage example in `TransactionServiceClientUsage`. Also using the example you can try how the service works. Just open it in your IDEA and run `main` method (Note: the service should be running).