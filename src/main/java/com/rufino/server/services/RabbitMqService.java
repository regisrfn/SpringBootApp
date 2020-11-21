package com.rufino.server.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqService {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${sample.rabbitmq.exchange}")
    private String exchange;

    @Value("${sample.rabbitmq.routingkey}")
    private String routingkey;

    @Scheduled
    public void send(String message) {
        rabbitTemplate.convertAndSend(exchange, routingkey, message);
        System.out.println("Send msg to consumer= " + message + " ");
    }
}