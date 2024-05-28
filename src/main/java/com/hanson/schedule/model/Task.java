package com.hanson.schedule.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class Task implements Comparable<Task> {

    // 订单id
    private String orderId;

    // 零件id
    private String componentId;

    // 工序id
    private String procedureId;

    // 任务前一个工序id
    private String preProcedureId;

    // 订单剩余工序
    private Integer leftProcedure;

    // 是否加急订单 0:否,1:是
    private Integer orderPriority;

    // 是否是重点客户 0:否,1:是
    private Integer clientPriority;

    // 是否为加急零件 0:否,1:是
    private Integer componentPriority;

    // 设备编码
    private String deviceCode;

    // 预计生产时长
    private Long completeTime;

    // 生产材料是否已入库
    private boolean isOriginalReady;

    // 前序是否ok
    private boolean isPreReady;

    // 任务状态 0: 未开始, 1: 进行中, 2: 已完成
    private Integer status;

    // 如果是在进行中, 才会有设备id
    private String deviceId;

    // 订单要求完成时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime targetCompleteTime;

    // 如果任务已经在执行中，才会有开始时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime taskStartTime;

    @Override
    public int compareTo(Task o) {
        if (preProcedureId != null && preProcedureId.contains(o.getProcedureId())) {
            return 1;
        }
        if (o.getPreProcedureId() != null && o.getPreProcedureId().contains(procedureId)) {
            return -1;

        }
        if (status != o.status) {
            return o.status - status;
        }
        if (isOriginalReady != o.isOriginalReady) {
            return o.isOriginalReady ? 1 : -1;

        }
        if (leftProcedure <= 4) {
            return -1;
        }
        if (o.getLeftProcedure() <= 4) {
            return 1;
        }
        if (isPreReady != o.isPreReady) {
            return o.isPreReady ? 1 : -1;
        }

        if (orderPriority != o.orderPriority) {
            return o.orderPriority - orderPriority;
        }
        if (clientPriority != o.clientPriority) {
            return o.clientPriority - clientPriority;
        }
        if (leftProcedure != o.leftProcedure) {
            return leftProcedure - o.leftProcedure;
        }
        return targetCompleteTime.compareTo(o.targetCompleteTime);
    }

}
