package com.wit.fgj.server.mm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.BuilderCall;

@Data
@AllArgsConstructor
@NoArgsConstructor
@BuilderCall
public class DeviceIssModel {

    /** 设备ID */
    private String id;

    /** 设备使用编号 */
    private int userNo;

    /** [组件类] 基本信息 */
    private DeviceModel.BasicInfo basicInfo;

    /** [组件类] 使用信息 */
    private DeviceModel.UsedInfo usedInfo;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @BuilderCall
    public static class BasicInfo {

        /** 1. [不可空] 设备序列号 */
        private String deviceSn;

        /** 2. [不可空] 设备类型名称 */
        private String deviceTypeName;

        /** 3. [不可空] 设备型号名称 */
        private String deviceModelName;

        /** 4. [不可空] 设备类型ID */
        private String deviceTypeId;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @BuilderCall
    public static class UsedInfo {

        /** [不可空] 是否被使用 */
        private Boolean isUsed;

    }

}
