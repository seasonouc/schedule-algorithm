package com.hanson.schedule.controller;

import com.hanson.schedule.model.ProducePlan;
import com.hanson.schedule.service.DeviceService;
import com.hanson.schedule.service.TaskService;
import com.hanson.schedule.vo.RefreshDeviceRequest;
import com.hanson.schedule.vo.SubmitOrderRequest;
import com.hanson.schedule.vo.SubmitOrderResponse;

import io.swagger.annotations.ApiOperation;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping("/Order/receive")
    @ApiOperation(value = "订单批量提交", response = SubmitOrderResponse.class)
    @ResponseBody
    public SubmitOrderResponse sortAndSchedule(@RequestBody SubmitOrderRequest request) {
        SubmitOrderResponse response = new SubmitOrderResponse();
        Map<String, ProducePlan> producePlan = taskService.receiveOrder(request.getOrders());

        response.setProducePlans(producePlan);
        return response;

    }

    @PostMapping("/Device/refresh")
    @ApiOperation(value = "刷新设备列表")
    public String refreshDevice(@RequestBody RefreshDeviceRequest request) {
        deviceService.refreshDevice(request.getDevices());
        return "success";
    }

}