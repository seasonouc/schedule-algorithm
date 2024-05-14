package com.hanson.schedule.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class Order {
    // 订单id
    private String id;

    // 订单剩余工序
    private Integer leftProcedure;

    // 是否加急订单 0:否,1:是
    private Integer orderPriority;

    // 是否是重点客户 0:否,1:是
    private Integer clientPriority;

    // 订单要求完成时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime targetCompleteTime;

    private Component[] components;

}
