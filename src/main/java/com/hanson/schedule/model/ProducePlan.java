package com.hanson.schedule.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("生产计划")
public class ProducePlan {

    // 设备id
    @ApiModelProperty(value = "设备id", required = true)
    private String deviceId;

    @ApiModelProperty(value = "设备开始生产时间", required = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "会在该设备上生产的工序排序结果", required = true)
    private List<Procedure> procedures;

}
