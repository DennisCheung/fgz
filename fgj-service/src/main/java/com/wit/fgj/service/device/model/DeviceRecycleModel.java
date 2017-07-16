package com.wit.fgj.service.device.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.BuilderCall;

/**
 * 设备回收参数类
 * @author zyj
 *
 */
@Data
@BuilderCall
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRecycleModel {
    /** 客户ID */
    private String personId;

    /** 设备编号 */
    private String deviceSn;

    private String teamId;
    private String userId;
}
