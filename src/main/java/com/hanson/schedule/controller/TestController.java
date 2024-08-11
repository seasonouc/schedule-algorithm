package com.hanson.schedule.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanson.schedule.service.DeviceService;
import com.hanson.utils.TimeUtil;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public String getCompleteDateTime(@RequestParam(value = "fromTime", required = false) String fromTime,
            @RequestParam("completeSec") Long completeSec) {
        LocalDateTime fromDateTime;
        if (fromTime == null || fromTime == "") {
            fromDateTime = LocalDateTime.now();
        } else {
            fromDateTime = LocalDateTime.parse(fromTime);
        }

        return TimeUtil.getCompleteTime(fromDateTime, completeSec).toString();
    }

}
