# REST
App test using: java 14 (Spring Boot), H2 (in-memory database) and RabbiMq.
Running in Ubuntu Focal 20.04 (LTS)

# PREREQUISITES
- Java
- Docker | https://docs.docker.com/engine/install/ubuntu/ 

# Running RabbitMq in linux
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

# Database

|       Orders       |
|--------------------|
|idOrder: UIID       |
|idParcel: String    |
|idClient: String    |
|totalValue:float    |
|orderAddress: String|


The values are inserted in the delivery database after receiving the messages from a separated service.
|       Delivery     |
|--------------------|
|idOrder: UIID       |
|idClient: String    |
|orderAddress: String|

H2 in-memory -
Running in the server. Can be accessed by tcp connection: jdbc:h2:tcp://localhost:5050/mem:orderDATABASE

# REST API

* create order: http://localhost:5000/api/v1/order
  post method - Ex. Json: 
  
  {
    "idClient":456,
    "idParcel": 789,
    "totalValue": 10.99,
    "orderAddress": "rua de baixo"
  }

# RabbitMq consumer api:
https://github.com/regisrfn/rabbitMqConsumer


# Deploy To heroku
 - The application was deployed to heroku 
 - RabbitMq add on installed on heroku app ->  heroku addons:create cloudamqp:lemur
 
 - Can be tested on:
 https://rufino-spring-boot.herokuapp.com/api/v1/order/