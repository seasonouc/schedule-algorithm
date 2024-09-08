package com.hanson.utils;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import com.hanson.schedule.model.WorkTimeLine;

public class TimeUtil {

    private static WorkTimeLine[] workTimeLines = { new WorkTimeLine(LocalTime.of(8, 0, 0), LocalTime.of(12, 0, 0)),
            new WorkTimeLine(LocalTime.of(14, 0, 0), LocalTime.of(20, 0, 0)) };

    public static LocalDateTime getStartDateTime() {
        LocalDateTime now = LocalDateTime.now();
        if (now.getDayOfWeek() == DayOfWeek.SATURDAY) {
            LocalDate temp = now.toLocalDate().plusDays(2L);
            now = LocalDateTime.of(temp.getYear(), temp.getMonth(), temp.getDayOfMonth(), 8, 0, 0);
        } else if (now.getDayOfWeek() == DayOfWeek.SUNDAY) {
            LocalDate temp = now.toLocalDate().plusDays(1);
            now = LocalDateTime.of(temp.getYear(), temp.getMonth(), temp.getDayOfMonth(), 8, 0, 0);
        }
        return now;
    }

    public static LocalDateTime getCompleteTime(LocalDateTime fromTime, long sec) {
        LocalTime fromLocalTime = fromTime.toLocalTime();
        LocalDate targetDate = LocalDate.of(fromTime.getYear(), fromTime.getMonthValue(), fromTime.getDayOfMonth());

        int i = 0;
        int pos = 1; // front = -1, middle = 0, behind = 1
        for (; i < workTimeLines.length; i++) {
            if (fromLocalTime.compareTo(workTimeLines[i].getFrom()) <= 0) {
                pos = -1;
                break;
            }
            if (fromLocalTime.compareTo(workTimeLines[i].getTo()) < 0) {
                pos = 0;
                break;

            }
        }

        if (pos == 1) {
            i = 0;
            pos = -1;
            targetDate = targetDate.plusDays(1);
            if (targetDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                targetDate = targetDate.plusDays(1);
            }
        }
        if (pos == 0) {
            LocalTime temp = fromLocalTime.plus(sec, ChronoUnit.SECONDS);
            if (sec <= workTimeLines[i].getDuration() && temp.compareTo(workTimeLines[i].getTo()) <= 0
                    && temp.compareTo(workTimeLines[i].getFrom()) > 0) {
                return LocalDateTime.of(targetDate, fromLocalTime.plus(sec, ChronoUnit.SECONDS));
            }
            Duration duration = Duration.between(fromLocalTime, workTimeLines[i].getTo());
            sec -= duration.getSeconds();

            if (i == workTimeLines.length - 1) {
                targetDate = targetDate.plusDays(1);
                if (targetDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    targetDate = targetDate.plusDays(1);
                }
            }
            i = (i + 1) % workTimeLines.length;
        }

        while (sec > 0) {
            if (workTimeLines[i].getDuration() < sec) {
                sec -= workTimeLines[i].getDuration();
                if (i == workTimeLines.length - 1) {
                    targetDate = targetDate.plusDays(1);
                    if (targetDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                        targetDate = targetDate.plusDays(1);
                    }
                }
                i = (i + 1) % workTimeLines.length;
            } else {
                LocalTime engLocalTime = workTimeLines[i].getFrom().plusSeconds(sec);
                return LocalDateTime.of(targetDate, engLocalTime);
            }
        }
        return fromTime.plus(sec, ChronoUnit.SECONDS);

    }

}
