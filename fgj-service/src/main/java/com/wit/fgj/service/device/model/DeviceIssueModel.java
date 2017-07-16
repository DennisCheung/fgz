package com.wit.fgj.service.device.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.BuilderCall;

/**
 * 设备发放参数类
 * @author zyj
 *
 */
@Data
@BuilderCall
@AllArgsConstructor
@NoArgsConstructor
public class DeviceIssueModel {
    /** 1. [不可空] 客户ID */
    private String personId;

    /** 2. [不可空] 设备sn编号 */
    private String deviceSn;

    /**发放备注*/
    private String issueDesc = null;

    /** 3. [不可空] 操作发放的工作组id */
    private String teamId;

    /** 4. [不可空] 操作发放的操作员id */
    private String userId;
}
