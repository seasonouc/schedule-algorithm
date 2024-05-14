package com.hanson.schedule.model;

import java.time.Duration;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class WorkTimeLine {
    public WorkTimeLine(LocalTime from, LocalTime to) {
        this.from = from;
        this.to = to;
    }

    public WorkTimeLine() {
    }

    // 起始工作时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime from;

    // public void setFrom(String from) {
    // this.from = LocalTime.parse(from);
    // }

    // 截止工作时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime to;

    // public void setTo(String to) {
    // this.to = LocalTime.parse(to);
    // }

    // public LocalTime getFrom() {
    // return from;
    // }

    // public LocalTime getTo() {
    // return to;
    // }

    public long getDuration() {
        return Duration.between(from, to).getSeconds();
    }
}
