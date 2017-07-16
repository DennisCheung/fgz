package com.wit.fgj.service.device.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.BuilderCall;

/**
 * 设备回收类
 * @author ROCKY
 *
 */
@Data
@BuilderCall
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRecycleCallModel {

    /** 1. [不可空] 家庭ID */
    private String familyId;

    /** 2. [不可空] 设备sn编号 */
    private String deviceSn;

    /** 3. [不可空] 操作发放的工作组id */
    private String teamId;

    /** 4. [不可空] 操作发放的操作员id */
    private String userId;

}
