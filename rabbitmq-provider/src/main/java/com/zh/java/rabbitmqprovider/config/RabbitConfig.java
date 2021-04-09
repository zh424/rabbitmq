package com.zh.java.rabbitmqprovider.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 生产者推送消息的消息确认回调配置类
 * <p>
 * 先从总体的情况分析，推送消息存在四种情况：
 * 1、消息推送到server，但是在server里找不到交换机
 * 2、消息推送到server，找到交换机了，但是没找到队列
 * 3、消息推送到sever，交换机和队列啥都没找到
 * 4、消息推送成功
 */
@Configuration
public class RabbitConfig {

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            System.out.println("ConfirmCallback:     " + "相关数据：" + correlationData);
            System.out.println("ConfirmCallback:     " + "确认情况：" + ack);
            System.out.println("ConfirmCallback:     " + "原因：" + cause);
        });

        // 过时了,用下面的setReturnsCallback
//        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
//            @Override
//            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
//                System.out.println("ReturnCallback:     "+"消息："+message);
//                System.out.println("ReturnCallback:     "+"回应码："+replyCode);
//                System.out.println("ReturnCallback:     "+"回应信息："+replyText);
//                System.out.println("ReturnCallback:     "+"交换机："+exchange);
//                System.out.println("ReturnCallback:     "+"路由键："+routingKey);
//            }
//        });

        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            System.out.println("ReturnsCallback:     " + "消息：" + returnedMessage.getMessage());
            System.out.println("ReturnsCallback:     " + "回应码：" + returnedMessage.getMessage());
            System.out.println("ReturnsCallback:     " + "回应信息：" + returnedMessage.getMessage());
            System.out.println("ReturnsCallback:     " + "交换机：" + returnedMessage.getExchange());
            System.out.println("ReturnsCallback:     " + "路由键：" + returnedMessage.getRoutingKey());
        });

        return rabbitTemplate;
    }
}
