package com.hanson.schedule.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TimeRange {

    public TimeRange(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public boolean overlaps(TimeRange other) {
        return !this.to.isBefore(other.from) && !this.from.isAfter(other.to);
    }

    private LocalDateTime from;
    private LocalDateTime to;

}
