package com.hanson.schedule.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hanson.utils.TimeUtil;

import lombok.Data;

@Data
public class DeviceWorkLoad implements Comparable<DeviceWorkLoad> {

    private int taskCount;

    private long totalTime;

    private String deviceId;

    private boolean isFirstAvailable;

    private List<TimeRange> timeRanges;

    public void initTimeRage() {
        timeRanges = new ArrayList<>();
        isFirstAvailable = true;
    }

    @Override
    public int compareTo(DeviceWorkLoad o) {
        if (totalTime != o.totalTime) {
            return (int) (totalTime - o.totalTime);
        }
        return taskCount - o.taskCount;
    }

    public boolean freeAndSave(LocalDateTime from, LocalDateTime to) {
        TimeRange newRange = new TimeRange(from, to);
        timeRanges.add(newRange);
        return true;
    }

    public TimeRange getAvailableTimeRange(long sec) {
        timeRanges.sort((a, b) -> a.getFrom().compareTo(b.getFrom()));
        TimeRange segment = null;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = TimeUtil.getCompleteTime(start, sec);

        for (int i = 0; i < timeRanges.size(); i++) {
            TimeRange current = timeRanges.get(i);
            if (current.getFrom().isAfter(end)) {
                segment = new TimeRange(start, end);
                break;
            }
            start = current.getTo();
            end = TimeUtil.getCompleteTime(start, sec);
        }
        if (segment == null) {
            segment = new TimeRange(start, end);
        }

        return segment;
    }

    public TimeRange getAvailableTimeRange(LocalDateTime start, LocalDateTime end, long sec) {
        if (timeRanges.size() == 0) {
            return new TimeRange(start, end);
        }
        timeRanges.sort((a, b) -> a.getFrom().compareTo(b.getFrom()));
        for (int i = 0; i < timeRanges.size(); i++) {
            TimeRange current = timeRanges.get(i);
            if (i == 0) {
                if (current.getFrom().isAfter(end)) {
                    return new TimeRange(start, end);
                }

            } else {
                TimeRange pre = timeRanges.get(i - 1);
                if (pre.getTo().isBefore(start) && current.getFrom().isAfter(end)) {
                    return new TimeRange(start, end);
                }
            }
            if (getDeviceId().equals("17")) {
                System.out.println("debug deviceId: " + getDeviceId());
            }
        }
        int size = timeRanges.size();
        TimeRange last = timeRanges.get(size - 1);
        if (last.getTo().isAfter(start)) {
            start = last.getTo();
            end = TimeUtil.getCompleteTime(start, sec);
        }

        return new TimeRange(start, end);
    }
}
