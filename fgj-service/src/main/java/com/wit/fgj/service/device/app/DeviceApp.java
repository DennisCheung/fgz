package com.wit.fgj.service.device.app;

import com.wit.fgj.service.device.model.DeviceHmoIssueModel;
import com.wit.fgj.service.device.model.DeviceIssueCallModel;
import com.wit.fgj.service.device.model.DeviceIssueModel;
import com.wit.fgj.service.device.model.DeviceRecycleCallModel;
import com.wit.fgj.service.device.model.DeviceRecycleModel;

public interface DeviceApp {
    /**
     * 设备发放
     * 支持两种设备：定位手表、幸福手环
     * @param deviceIssueData
     */
    void deviceIssue(DeviceIssueModel model);

    /**
     * 设备回收
     * 支持两种设备：定位手表、幸福手环
     * @param deviceIssueData
     */
    void deviceRecycle(DeviceRecycleModel model);

    /**
     * 设备发放
     * 支持呼叫型设备发放
     * @param DeviceIssueCallModel
     */
    void deviceCall(DeviceIssueCallModel model);

    /**
     * 用于监测设备的发放跟回收
     * 两个操作调用同一个接口，区别在于date.isDelete
     * false为发放监测设备，true为回收监测设备
     * @param data
     */
    void deviceHmoIssue(DeviceHmoIssueModel model);

    /**
     * 减少用户使用数量
     * @param deviceId
     */
    void decreateDevIssueHmoUserNo(String deviceId);

    /**
     * 用于呼叫(CALL)设备的回收
     * @param model
     */
    void deviceCallRecycle(DeviceRecycleCallModel model);
}
