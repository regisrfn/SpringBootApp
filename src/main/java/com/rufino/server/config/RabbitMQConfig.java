package com.rufino.server.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${sample.rabbitmq.queue}")
    String queueName;
    @Value("${sample.rabbitmq.exchange}")
    String exchange;
    @Value("${sample.rabbitmq.routingkey}")
    private String routingkey;

    @Bean
    Queue queue() {
        return new Queue(queueName);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate() throws URISyntaxException {
        URI rabbitMqUrl = URI.create(getEnvOrThrow("CLOUDAMQP_URL"));

        if (rabbitMqUrl == null)
            rabbitMqUrl = URI.create("amqp://guest:guest@localhost");

        final CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUsername(rabbitMqUrl.getUserInfo().split(":")[0]);
        factory.setPassword(rabbitMqUrl.getUserInfo().split(":")[1]);
        factory.setHost(rabbitMqUrl.getHost());
        factory.setPort(rabbitMqUrl.getPort());
        factory.setVirtualHost(rabbitMqUrl.getPath().substring(1));
        
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    private static String getEnvOrThrow(String name) {
        final String env = System.getenv(name);
        if (env == null) {
            throw new IllegalStateException("Environment variable [" + name + "] is not set.");
        }
        return env;
    }
}