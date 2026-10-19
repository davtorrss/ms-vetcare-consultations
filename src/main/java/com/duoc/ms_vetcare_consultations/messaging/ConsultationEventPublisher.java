package com.duoc.ms_vetcare_consultations.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConsultationEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ConsultationEventPublisher(
            RabbitTemplate rabbitTemplate
    ) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreated(Object payload) {

        rabbitTemplate.convertAndSend(
                RabbitNames.DIRECT_EXCHANGE,
                RabbitNames.CONSULTATION_CREATED,
                payload
        );
    }

    public void publishStatusChanged(Object payload) {

        rabbitTemplate.convertAndSend(
                RabbitNames.TOPIC_EXCHANGE,
                RabbitNames.CONSULTATION_STATUS_CHANGED,
                payload
        );
    }
}