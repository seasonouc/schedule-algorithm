package com.hanson.schedule.vo;

import java.util.Map;

import com.hanson.schedule.model.ProducePlan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("创建订单返回生产计划结果")
public class SubmitOrderResponse {

    @ApiModelProperty(value = "生产计划", required = true)
    private Map<String, ProducePlan> producePlans;
}
