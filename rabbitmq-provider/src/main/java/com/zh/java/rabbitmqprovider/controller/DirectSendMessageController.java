package com.zh.java.rabbitmqprovider.controller;

import com.zh.java.rabbitmqprovider.dto.MessageDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class DirectSendMessageController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 发送直连消息
     * @return
     */
    @GetMapping("sendDirectMessage")
    public String sendDirectMessage() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, String> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        // 将消息携带绑定键值：TestDirectRouting 发送到交换机 TestDirectExchange
        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);

        return "ok";
    }

    /**
     * 测试发送直连对象消息
     * @return
     */
    @GetMapping("sendDirectDtoMessage")
    public String sendDirectDtoMessage() {
        rabbitTemplate.convertAndSend("NoExchangeQueue", new MessageDto(1, "zh"));
        return "ok";
    }
}
