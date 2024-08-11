package com.hanson.schedule.service;

import com.hanson.schedule.model.Device;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceService {

    private List<Device> devices;

    public Device[] refreshDevice(Device[] devices) {
        if (devices != null && devices.length > 0) {
            this.devices = new ArrayList<>();
            for (Device device : devices) {
                this.devices.add(device);
            }
        }
        // for (int i = 0; i < 100; i++) {
        Device device = new Device();
        device.setCode("outsource");
        device.setId("outsource");
        device.setStatus(1);
        device.setType(1);
        this.devices.add(device);
        // }

        return this.devices.toArray(new Device[0]);
    }

    public Device[] getAvailableDevices() {
        return this.devices.toArray(new Device[0]);
    }
}
