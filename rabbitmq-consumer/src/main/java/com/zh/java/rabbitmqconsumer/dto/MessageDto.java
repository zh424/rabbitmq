package com.zh.java.rabbitmqconsumer.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageDto implements Serializable {

    private Integer id;

    private String name;
}
