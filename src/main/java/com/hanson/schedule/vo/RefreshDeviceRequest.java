package com.hanson.schedule.vo;

import com.hanson.schedule.model.Device;
import com.hanson.schedule.model.WorkTimeLine;
import lombok.Data;

@Data
public class RefreshDeviceRequest {
    private WorkTimeLine[] workTimeLine;
    private Device[] devices;
}
