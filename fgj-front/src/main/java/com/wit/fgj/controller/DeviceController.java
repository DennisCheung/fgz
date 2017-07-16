package com.wit.fgj.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wit.fgj.service.device.app.DeviceApp;
import com.wit.fgj.service.device.model.DeviceHmoIssueModel;
import com.wit.fgj.service.device.model.DeviceIssueCallModel;
import com.wit.fgj.service.device.model.DeviceIssueModel;
import com.wit.fgj.service.device.model.DeviceRecycleCallModel;
import com.wit.fgj.service.device.model.DeviceRecycleModel;
import com.wit.qix.client.user.CurrentUser;

@RestController
public class DeviceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

    @Autowired private CurrentUser currentUser;
    @Autowired private DeviceApp deviceApp;

    /**
     * 减少用户使用数量
     * @param deviceId
     */
    @PostMapping(value = "/device/decreateDevIssueHmoUserNo")
    public void decreateDevIssueHmoUserNo(String deviceId) {
        LOGGER.debug("减少用户使用数量，请求参数deviceId = {}", deviceId);
        deviceApp.decreateDevIssueHmoUserNo(deviceId);
    }

    /**
     * 设备发放,支持两种设备：定位手表、幸福手环
     * @param model
     */
    @PostMapping(value = "/device/deviceIssue")
    public void deviceIssue(@RequestBody DeviceIssueModel model) {
        model.setUserId(currentUser.getSysUserId());
        LOGGER.debug("设备发放,支持两种设备：定位手表、幸福手环，请求参数model = {}", model);
        deviceApp.deviceIssue(model);
    }

    /**
     * 设备回收
     * 支持两种设备：定位手表、幸福手环
     * @param model
     */
    @PostMapping(value = "/device/deviceRecycle")
    public void deviceRecycle(@RequestBody DeviceRecycleModel model) {
        model.setUserId(currentUser.getSysUserId());
        LOGGER.debug("设备回收 ,请求参数model = {}", model);
        deviceApp.deviceRecycle(model);
    }

    /**
     * 用于监测设备的发放
     * 两个操作调用同一个接口，区别在于model.isDelete
     * false为发放监测设备
     * @param model
     */
    @PostMapping(value = "/device/grantDeviceHmoIssue")
    public void grantDeviceHmoIssue(@RequestBody DeviceHmoIssueModel model) {
        LOGGER.debug("用于监测设备的发放,model = {}", model);
        model.setIsDelete(false);
        model.setUserId(currentUser.getSysUserId());
        deviceApp.deviceHmoIssue(model);
    }

    /**
     * 用于监测设备的回收
     * 两个操作调用同一个接口，区别在于model.isDelete
     * true回收监测设备
     * @param model
     */
    @PostMapping(value = "/device/recoverDeviceHmoIssue")
    public void recoverDeviceHmoIssue(@RequestBody DeviceHmoIssueModel model) {
        LOGGER.debug("用于监测设备的回收,model = {}", model);
        model.setUserId(currentUser.getSysUserId());
        model.setIsDelete(true);
        deviceApp.deviceHmoIssue(model);
    }

    /**
     * 用呼叫设备的发放
     * @param model
     */
    @PostMapping(value = "/device/devcallissue")
    public void deviceCallIssue(@RequestBody DeviceIssueCallModel model) {
        model.setUserId(currentUser.getSysUserId());
        LOGGER.debug("用于检测CALL设备的发放,model = {}", model);
        deviceApp.deviceCall(model);
    }

    /**
     * 用于CALL设备的回收
     * @param model
     */
    @PostMapping(value = "/device/recycleCall")
    public void deviceCallRecycle(@RequestBody DeviceRecycleCallModel model) {
        model.setUserId(currentUser.getSysUserId());
        LOGGER.debug("用于监测CALL设备的回收,model = {}", model);
        deviceApp.deviceCallRecycle(model);
    }
}
