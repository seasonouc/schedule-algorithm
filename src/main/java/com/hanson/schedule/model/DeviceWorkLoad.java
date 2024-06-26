package com.hanson.schedule.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DeviceWorkLoad implements Comparable<DeviceWorkLoad> {

    private int taskCount;

    private long totalTime;

    private String deviceId;

    private LocalDateTime currentDateTime;

    @Override
    public int compareTo(DeviceWorkLoad o) {
        if (totalTime != o.totalTime) {
            return (int) (totalTime - o.totalTime);
        }
        return taskCount - o.taskCount;
    }

}
