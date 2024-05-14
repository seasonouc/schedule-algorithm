package com.hanson.schedule.service;

import com.hanson.schedule.model.Device;
import com.hanson.schedule.model.WorkTimeLine;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceService {
    private WorkTimeLine[] workTimeLines = { new WorkTimeLine(LocalTime.of(8, 0, 0), LocalTime.of(12, 0, 0)),
            new WorkTimeLine(LocalTime.of(14, 0, 0), LocalTime.of(20, 0, 0)) };

    private List<Device> devices;

    public Device[] refreshDevice(Device[] devices) {
        if (devices != null && devices.length > 0) {
            this.devices = new ArrayList<>();
            for (Device device : devices) {
                this.devices.add(device);
            }
        }
        for (int i = 0; i < 100; i++) {
            Device device = new Device();
            device.setCode("outsource");
            device.setId("outsource" + i);
            device.setStatus(1);
            device.setType(1);
            this.devices.add(device);
        }

        return this.devices.toArray(new Device[0]);
    }

    public Device[] getAvailableDevices() {
        return this.devices.toArray(new Device[0]);
    }

    public LocalDateTime getCompleteTime(LocalDateTime fromTime, long sec) {
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
        return null;

    }
}
