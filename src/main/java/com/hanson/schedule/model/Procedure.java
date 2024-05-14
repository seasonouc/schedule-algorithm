package com.hanson.schedule.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class Procedure {
    // 工序 id
    private String id;

    // 设备编码
    private String deviceCode;

    // 前序是否ok
    private boolean preReady;

    // 排序字段
    private Integer rank;

    // 加工时长
    private Long produceTime;

    // 工序状态 0: 未开始, 1: 进行中, 2: 已完成
    private Integer status;

    // 如果是在进行中, 才会有设备id
    private String deviceId;

    // 开始加工时间, 只应用已经开始加工而又没有完成的工序,为空表示并没有开始
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    // 预计开始时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime predictStartTime;

    // 预计完成时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime predictCompleteTime;

}
