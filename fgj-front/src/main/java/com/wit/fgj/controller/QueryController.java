package com.wit.fgj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wit.fgj.server.mm.DevIssueModel;
import com.wit.fgj.server.mm.DevRecycleModel;
import com.wit.fgj.server.mm.DeviceIssModel;
import com.wit.fgj.server.mm.DeviceModel;
import com.wit.fgj.server.mm.PersonModel;
import com.wit.fgj.server.query.Queryapp;

/**
 *  查询接口
 */
@RestController
public class QueryController {

    @Autowired private Queryapp queryapp;

    /**
     * 查询最近七天的客户设备发放信息。
     * @param teamId
     * @return 发放时间issueDate  客户姓名personName 设备类型名称deviceTypeName 设备序列号deviceSn
     */
    @GetMapping(value = "/fgj/query/devissuenote")
    public List<DevIssueModel> queryDevIssue(@RequestParam String teamId) {
        List<DevIssueModel> models = queryapp.findDevIssueNote(teamId);
        return models;
    }

    /**
     * 查询最近七天的客户设备回收信息。
     * @param teamId
     * @return 回收时间recycleDate 客户姓名personName 设备类型名称deviceTypeName 设备序列号deviceSn
     */
    @GetMapping(value = "/fgj/query/devrecyclenote")
    public List<DevRecycleModel> queryDevRecycle(@RequestParam String teamId) {
        List<DevRecycleModel> models = queryapp.findDevRecycleNote(teamId);
        return models;
    }

    /**
     * 查询客户。
     * @param circleId
     * @param text
     * @return
     */
    @GetMapping(value = "/fgj/query/personbytext")
    public List<PersonModel> queryPersonBytext(@RequestParam String teamId, @RequestParam String text) {
        List<PersonModel> models = queryapp.findPersonBytext(teamId, text);
        return models;
    }

    /**
     * 根据设备sn号在设备库中进行查询
     *
     *  (1)这里是查找所有最大使用人数等于1的：已经被使用的设备
     *  (2)最大使用人数大于1的设备：将查找所有已经被使用的设备，并且在相应设备发放表中查找记录
     * @param snStr
     * @param teamId
     * @return
     */
    @GetMapping(value = "/fgj/query/deviceAllUsed")
    public List<DeviceModel> queryDeviceBySnTrue(@RequestParam String snStr, String teamId) {
        List<DeviceModel> models = queryapp.findDeviceBySnForRecycle(snStr, teamId);
        return models;
    }

    /**
     * 根据设备sn号在设备库中进行查询
     *
     * (1)这里是查找所有最大使用人数等于1的：为未被使用的设备
     * (2)最大使用人数大于1的设备：
     *    (a)当满足设备未被使用，将根据可使用的编号遍历给出设备可使用编号
     *    (b)当设备已经被使用，将根据已经使用的使用编号互斥给出设备可使用编号
     * @param snStr
     * @param teamId
     * @return
     */
    @GetMapping(value = "/fgj/query/deviceUnUsed")
    public List<DeviceIssModel> queryDeviceBySnFalse(@RequestParam String snStr, String teamId) {
        List<DeviceIssModel> models = queryapp.findDeviceBySnFalse(snStr, teamId);
        return models;
    }

}
