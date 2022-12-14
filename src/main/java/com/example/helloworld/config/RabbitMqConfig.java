package com.example.helloworld.config;

import com.example.helloworld.constants.RabbitConsts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMqConfig {
    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause));
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",
                        returnedMessage.getMessage(),
                        returnedMessage.getReplyCode(), returnedMessage.getReplyText(),
                        returnedMessage.getExchange(), returnedMessage.getRoutingKey());
            }
        });

        return rabbitTemplate;
    }

    /**
     * Simple队列模式
     */
    @Bean
    public Queue directOneQueue() {
        return new Queue(RabbitConsts.SIMPLE_MODE_QUEUE_SUBMISSION, true);
    }
}
