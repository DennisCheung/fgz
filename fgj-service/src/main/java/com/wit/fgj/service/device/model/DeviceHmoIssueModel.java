package com.wit.fgj.service.device.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.BuilderCall;

/**
 * 判定是发放还是回收操作
 * @author zyj
 *
 */
@Data
@BuilderCall
@AllArgsConstructor
@NoArgsConstructor
public class DeviceHmoIssueModel {
    /** 1. [不可空] 客户编号 */
    private String personId;

    /** 2. [不可空] 操作发放工作组id */
    private String teamId;

    /** 3. [不可空] 操作发放操作员id */
    private String userId;

    /** 4. [不可空] 设备编号id */
    private String deviceId;

    /** 5. [不可空] 设备sn编号id */
    private String deviceSn;

    /** 6. [可空] 发放备注 */
    private String issueDesc = null;

    /** 7. [不可空] 判定是发放还是回收操作，false为发放监测设备，true为回收监测设备 */
    private Boolean isDelete;

    /** 8. [不可空] 用户数量(可扩展，默认为零，进行调用时暂时为1) */
    private Integer userNo;

}
