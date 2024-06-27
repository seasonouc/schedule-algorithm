package com.hanson.schedule.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hanson.schedule.model.Component;
import com.hanson.schedule.model.Device;
import com.hanson.schedule.model.DeviceWorkLoad;
import com.hanson.schedule.model.Order;
import com.hanson.schedule.model.Procedure;
import com.hanson.schedule.model.ProducePlan;
import com.hanson.schedule.model.Task;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskService {

    @Autowired
    private DeviceService deviceService;

    public Map<String, ProducePlan> receiveOrder(Order[] orders) {

        Device[] devices = deviceService.getAvailableDevices();

        Map<String, PriorityQueue<DeviceWorkLoad>> deviceMap = new HashMap<>();

        List<Task> taskQueue = new ArrayList<>();

        Map<String, Component> componentMap = new HashMap<>();
        for (Order order : orders) {
            for (Component component : order.getComponents()) {
                componentMap.put(component.getId(), component);
            }
        }

        // key = deviceId
        Map<String, DeviceWorkLoad> presetWorkload = new HashMap<>();
        Map<String, ProducePlan> producePlanMap = new HashMap<>();

        for (Order order : orders) {
            for (Component component : order.getComponents()) {
                String preProcedureId = null;
                if (component.getPreId() != null && componentMap.get(component.getPreId()) != null) {
                    Procedure[] lastProcedures = componentMap.get(component.getPreId()).getProcedures();
                    preProcedureId = lastProcedures[lastProcedures.length - 1].getId();
                }
                for (Procedure procedure : component.getProcedures()) {

                    Task task = new Task();
                    // TODO 组装 task
                    if (preProcedureId != null) {
                        task.setPreProcedureId(preProcedureId);
                    }
                    task.setRank(procedure.getRank());
                    task.setClientPriority(order.getClientPriority());
                    task.setComponentId(component.getId());
                    task.setTargetCompleteTime(order.getTargetCompleteTime());
                    task.setCompleteTime(procedure.getProduceTime());
                    task.setLeftProcedure(order.getLeftProcedure());
                    task.setOrderPriority(order.getOrderPriority());
                    task.setOrderId(order.getId());
                    task.setProcedureId(procedure.getId());
                    task.setDeviceCode(procedure.getDeviceCode());
                    task.setComponentPriority(component.getPriority());
                    task.setOriginalReady(component.isOriginalReady());
                    task.setStatus(procedure.getStatus());
                    task.setPreReady(procedure.isPreReady());
                    if (procedure.getDeviceId() != null && !"".equals(procedure.getDeviceId())) {
                        task.setDeviceId(procedure.getDeviceId());
                        DeviceWorkLoad workLoad = new DeviceWorkLoad();
                        workLoad.setDeviceId(procedure.getDeviceId());
                        workLoad.setTaskCount(1);
                        workLoad.setTotalTime(task.getCompleteTime());
                        presetWorkload.put(procedure.getDeviceId(), workLoad);

                        ProducePlan plan = new ProducePlan();
                        plan.setStartTime(procedure.getStartTime());
                        plan.setDeviceId(workLoad.getDeviceId());
                        List<Procedure> procedures = new ArrayList<>();
                        procedures.add(procedure);
                        plan.setProcedures(procedures);

                        producePlanMap.put(procedure.getDeviceId(), plan);
                    }

                    taskQueue.add(task);
                    // if (procedure.getStatus() != 2) {
                    // preProcedureId = procedure.getId();
                    // }
                }
            }
        }

        for (Device device : devices) {
            String code = device.getCode();
            PriorityQueue<DeviceWorkLoad> deviceQueue = deviceMap.get(code);
            if (deviceQueue == null) {
                deviceQueue = new PriorityQueue<>();
                deviceMap.put(code, deviceQueue);
            }

            DeviceWorkLoad workLoad = presetWorkload.get(device.getId());

            if (workLoad == null) {
                workLoad = new DeviceWorkLoad();
                workLoad.setDeviceId(device.getId());
                workLoad.setTaskCount(0);
                workLoad.setTotalTime(0);
            }
            deviceQueue.add(workLoad);
        }

        Map<String, Procedure> procedureMap = new HashMap<>();
        Map<String, List<LocalDateTime>> componentTimeLine = new HashMap<>();

        // Task[] tasks = taskQueue.toArray(new Task[0]);
        // // 分配任务
        // while (taskQueue.size() > 0) {
        // Task task = taskQueue.
        taskQueue.sort(new Comparator<Task>() {

            @Override
            public int compare(Task o1, Task o2) {
                return o1.compareTo(o2);
            }

        });
        for (Task task : taskQueue) {
            log.info(task.getComponentId() + ": " + task.getProcedureId() + ": " +
                    task.getRank());
            if (task.getStatus() == 2) {
                continue;
            }
            String deviceCode = task.getDeviceCode();
            PriorityQueue<DeviceWorkLoad> deviceQueue = deviceMap.get(deviceCode);
            if (deviceQueue == null) {
                log.error("deviceQueue is null, deviceCode: {}", deviceCode);
                continue;
            }
            DeviceWorkLoad workLoad = deviceQueue.poll();
            workLoad.setTaskCount(workLoad.getTaskCount() + 1);

            ProducePlan plan = producePlanMap.get(workLoad.getDeviceId());

            if (plan == null) {
                plan = new ProducePlan();
                plan.setDeviceId(workLoad.getDeviceId());

                if (task.getTaskStartTime() != null) {
                    // 设置真实开始计划的时间，比如有一些任务还在生产过程中
                    plan.setStartTime(task.getTaskStartTime());
                } else {
                    // 设置开始时间为当前时间(即任务开始时间)
                    plan.setStartTime(LocalDateTime.now());
                }

                workLoad.setCurrentDateTime(plan.getStartTime());
                List<Procedure> procedures = new ArrayList<>();
                plan.setProcedures(procedures);
                producePlanMap.put(workLoad.getDeviceId(), plan);
            }
            LocalDateTime predictStartTime = task.getTaskStartTime();
            if (predictStartTime == null) {
                predictStartTime = workLoad.getCurrentDateTime();
            }

            Procedure preProcedure = procedureMap.get(task.getPreProcedureId());
            if (preProcedure != null && preProcedure.getPredictCompleteTime().isBefore(predictStartTime)) {
                predictStartTime = preProcedure.getPredictCompleteTime();
            } else if (deviceCode.equalsIgnoreCase("outsource")) {
                predictStartTime = LocalDateTime.now();
            }

            List<LocalDateTime> timeLine = componentTimeLine.get(task.getComponentId());

            if (timeLine != null) {
                LocalDateTime lastEndTime = timeLine.get(timeLine.size() - 1);
                if (lastEndTime.isAfter(predictStartTime)) {
                    predictStartTime = lastEndTime;
                }
            }

            workLoad.setTotalTime(workLoad.getTotalTime() + task.getCompleteTime());
            deviceQueue.add(workLoad);

            LocalDateTime predictCompleteTime = this.deviceService.getCompleteTime(predictStartTime,
                    task.getCompleteTime());
            workLoad.setCurrentDateTime(predictCompleteTime);

            if (timeLine == null) {
                timeLine = new ArrayList<>();
                componentTimeLine.put(task.getComponentId(), timeLine);
            }
            timeLine.add(predictCompleteTime);

            Procedure procedure = new Procedure();
            procedure.setDeviceId(workLoad.getDeviceId());
            procedure.setId(task.getProcedureId());
            procedure.setDeviceCode(deviceCode);
            procedure.setStatus(task.getStatus());
            procedure.setProduceTime(task.getCompleteTime());
            procedure.setPredictCompleteTime(predictCompleteTime);
            procedure.setPredictStartTime(predictStartTime);
            procedure.setStartTime(task.getTaskStartTime());

            procedureMap.put(procedure.getId(), procedure);
            plan.getProcedures().add(procedure);
        }

        componentTimeLine.forEach((componentId, timeLine) -> {
            log.info("=======================");
            timeLine.forEach(time -> {
                log.info(componentId + ": " + time.toString());
            });

        });

        return producePlanMap;
    }

}
