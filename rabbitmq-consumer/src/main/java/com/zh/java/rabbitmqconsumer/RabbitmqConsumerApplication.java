package com.zh.java.rabbitmqconsumer;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RabbitmqConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqConsumerApplication.class, args);
    }

    /**
     * 解析反序列化设置，可放配置类
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        // 设置全局消息消费确认
        // AcknowledgeMode.NONE:rabbitmq server默认推送的所有消息都已经消费成功，会不断地向消费端推送消息
        // AcknowledgeMode.AUTO:由spring-rabbit依据消息处理逻辑是否抛出异常自动发送ack（无异常）或nack（异常）到server端
        // AcknowledgeMode.MANUAL:需要人为地获取到channel之后调用方法向server发送ack（或消费失败时的nack）信息
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 暂时不知道是干嘛的
        factory.setAutoStartup(true);
        return factory;
    }
}
