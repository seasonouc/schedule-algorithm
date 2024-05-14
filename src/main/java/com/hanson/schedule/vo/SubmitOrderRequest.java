package com.hanson.schedule.vo;

import com.hanson.schedule.model.Order;
import lombok.Data;

@Data
public class SubmitOrderRequest {
    private Order[] orders;
}
