package com.zh.java.rabbitmqconsumer.receiver;

import com.rabbitmq.client.Channel;
import com.zh.java.rabbitmqconsumer.dto.MessageDto;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class DirectReceiver {

    @RabbitListener(queues = "TestDirectQueue")
    @RabbitHandler
    public void process(Map testMassage) {
        System.out.println("DirectReceiver消费者收到消息：" + testMassage.toString());
    }

    /**
     * 多台监听绑定到同一个直连交互的同一个队列，实现了轮询的方式对消息进行消费，而且不存在重复消费
     *
     * @param testMassage
     */
    @RabbitListener(queues = "TestDirectQueue")
    @RabbitHandler
    public void process1(Map testMassage) {
        System.out.println("DirectReceiver1消费者收到消息：" + testMassage.toString());
    }

    /**
     * 接收类，需要配置反序列化，containerFactory测试不加好像也可以
     *
     * @param dto
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = "NoExchangeQueue", containerFactory = "rabbitListenerContainerFactory")
    @RabbitHandler
    public void process2(MessageDto dto, Channel channel, Message message) throws IOException {
        System.out.println("NoExchangeQueue消费者收到消息：" + dto.toString());
        /**
         *  设置限流机制
         *  param1: prefetchSize，消息本身的大小 如果设置为0  那么表示对消息本身的大小不限制
         *  param2: prefetchCount，告诉rabbitmq不要一次性给消费者推送大于N个消息
         *  param3：global，是否将上面的设置应用于整个通道，false表示只应用于当前消费者
         */
        channel.basicQos(0, 10, false);
        /**
         * 手动确认消费
         * deliveryTag:该消息的index
         * multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息。
         */
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
