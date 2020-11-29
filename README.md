# REST
App test using: java 14 (Spring Boot), H2 (in-memory database) and RabbiMq.
Running in Ubuntu Focal 20.04 (LTS)

# PREREQUISITES
- Java
- Docker | https://docs.docker.com/engine/install/ubuntu/ 

# Docker
# Running RabbitMq in linux
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

# Database

|       Orders       |
|--------------------|
|idOrder: UUID       |
|idParcel: String    |
|idClient: String    |
|totalValue:float    |
|orderAddress: String|


The values are inserted in the delivery database inside the consumer app after receiving the messages.
|       Delivery     |
|--------------------|
|idOrder: UUID       |
|idClient: String    |
|orderAddress: String|

H2 in-memory

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
 - RabbitMq add on installed on heroku app ->  heroku addons:create cloudamqp:lemur -> (https://elements.heroku.com/addons/cloudamqp)
 
 - Can be tested on:
 https://rufino-spring-boot.herokuapp.com/api/v1/order/