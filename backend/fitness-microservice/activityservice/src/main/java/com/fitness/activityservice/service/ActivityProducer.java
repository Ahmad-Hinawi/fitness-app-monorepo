package com.fitness.activityservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(Object activity) {
        rabbitTemplate.convertAndSend("fitness.activityservice.exchange",
                "fitness.activityservice.routingkey",activity);
        log.info("Sent message to ai service");

    }
}
