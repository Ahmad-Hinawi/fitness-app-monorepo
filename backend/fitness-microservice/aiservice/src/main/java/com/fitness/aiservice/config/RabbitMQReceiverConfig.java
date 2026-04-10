package com.fitness.aiservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQReceiverConfig {

    // 1. Define the Queue
    @Bean
    public Queue activityQueue() {
        return QueueBuilder.durable("fitness.activityservice.rabbitmq")
                .withArgument("x-dead-letter-exchange", "dlx.exchange") // Where to send failures
                .withArgument("x-dead-letter-routing-key", "deadLetter")
                .build();
    }


    // 2. Define the Direct Exchange
    @Bean
    public DirectExchange activityExchange() {
        return new DirectExchange("fitness.activityservice.exchange");
    }

    // 3. Bind the Queue to the Exchange using the Routing Key
    @Bean
    public Binding binding(Queue activityQueue, DirectExchange activityExchange) {
        return BindingBuilder.bind(activityQueue)
                .to(activityExchange)
                .with("fitness.activityservice.routingkey");
    }

    // dlq queue
    @Bean
    public Queue deadLetterQueue() {
        return new Queue("fitness.activityservice.dlq"); // The "Safety Bin"
    }
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("dlx.exchange");
    }
    @Bean
    public Binding  deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with("dlx.routingkey");
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
