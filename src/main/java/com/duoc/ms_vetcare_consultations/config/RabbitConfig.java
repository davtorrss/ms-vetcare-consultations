package com.duoc.ms_vetcare_consultations.config;

import com.duoc.ms_vetcare_consultations.messaging.RabbitNames;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public DefaultClassMapper classMapper() {

        DefaultClassMapper classMapper =
                new DefaultClassMapper();

        classMapper.setTrustedPackages(
                "com.duoc.ms_vetcare_consultations.dto"
        );

        return classMapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter(
            DefaultClassMapper classMapper
    ) {

        JacksonJsonMessageConverter converter =
                new JacksonJsonMessageConverter();

        converter.setClassMapper(classMapper);

        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter
    ) {

        RabbitTemplate rabbitTemplate =
                new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMessageConverter(
                jsonMessageConverter
        );

        return rabbitTemplate;
    }

    @Bean
    public DirectExchange directExchange() {

        return new DirectExchange(
                RabbitNames.DIRECT_EXCHANGE,
                true,
                false
        );
    }

    @Bean
    public TopicExchange topicExchange() {

        return new TopicExchange(
                RabbitNames.TOPIC_EXCHANGE,
                true,
                false
        );
    }

    @Bean
    public DirectExchange deadLetterExchange() {

        return new DirectExchange(
                RabbitNames.DLX_EXCHANGE,
                true,
                false
        );
    }

    @Bean
    public Queue consultationsQueue() {

        return QueueBuilder
                .durable(
                        RabbitNames.CONSULTATIONS_QUEUE
                )
                .deadLetterExchange(
                        RabbitNames.DLX_EXCHANGE
                )
                .deadLetterRoutingKey(
                        RabbitNames.CONSULTATIONS_DLQ_ROUTING_KEY
                )
                .ttl(60000)
                .build();
    }

    @Bean
    public Queue consultationsDeadLetterQueue() {

        return QueueBuilder
                .durable(
                        RabbitNames.CONSULTATIONS_DLQ
                )
                .build();
    }

    @Bean
    public Binding consultationCreatedBinding(
            Queue consultationsQueue,
            DirectExchange directExchange
    ) {

        return BindingBuilder
                .bind(consultationsQueue)
                .to(directExchange)
                .with(
                        RabbitNames.CONSULTATION_CREATED
                );
    }

    @Bean
    public Binding consultationTopicBinding(
            Queue consultationsQueue,
            TopicExchange topicExchange
    ) {

        return BindingBuilder
                .bind(consultationsQueue)
                .to(topicExchange)
                .with("consultation.#");
    }

    @Bean
    public Binding consultationsDeadLetterBinding(
            Queue consultationsDeadLetterQueue,
            DirectExchange deadLetterExchange
    ) {

        return BindingBuilder
                .bind(
                        consultationsDeadLetterQueue
                )
                .to(deadLetterExchange)
                .with(
                        RabbitNames.CONSULTATIONS_DLQ_ROUTING_KEY
                );
    }
}