package com.hanson.schedule.model;

import lombok.Data;

@Data
public class Component {
    // 零件 id
    private String id;

    // 前序零件id, 为空表示没有
    private String preId;

    // 是否已完成 0:未完成 1: 进行中 2:已完成
    private Integer status;

    // 原材料是否已到位
    private boolean isOriginalReady;

    // 是否为加急零件 0: 正常 1: 加急
    private Integer priority;

    // 零件数量
    // private Integer amount;

    // 工序
    private Procedure[] procedures;

}
