package com.hanson.schedule.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("设备实体")
@Data
public class Device {
    // 设备id
    @ApiModelProperty("设备ID")
    private String id;

    // 设备编码
    @ApiModelProperty("设备编码")
    private String code;

    // 设备类型 0: 实体设备机器 1: 外协加工机器
    @ApiModelProperty("设备类型")
    private Integer type;

    // 设备状态 0: 可用 1: 不可用
    @ApiModelProperty("设备状态")
    private Integer status;

}
