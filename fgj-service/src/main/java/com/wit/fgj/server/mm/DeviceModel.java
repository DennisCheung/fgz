package com.wit.fgj.server.mm;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.BuilderCall;

@Data
@AllArgsConstructor
@NoArgsConstructor
@BuilderCall
public class DeviceModel {

    /** 设备ID */
    private String id;

    /** 使用编号 */
    private int userNo;

    /** [组件类] 基本信息 */
    private DeviceModel.BasicInfo basicInfo;

    /** [组件类] 使用信息 */
    private DeviceModel.UsedInfo usedInfo;

    /** [可空] 发放信息 */
    private List<DevPerson> devPersonList;

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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @BuilderCall
    public static class DevPerson {
        /** [不可空] 客户id */
        private String personId;

        /** [不可空] 客户名称 */
        private String personName;

        /** [不可空] 客户性别 */
        private String sexName;

        /** [不可空] 客户身份证编号 */
        private String idNo;

        /** 家庭Id */
        private String familyId;

        /** 户主名字 */
        private String famPerson;

    }

}
