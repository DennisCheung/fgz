package com.wit.fgj.server.mm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.BuilderCall;

@Data
@AllArgsConstructor
@NoArgsConstructor
@BuilderCall
public class DevIssueModel {

    /** 1. id 客户设备发放ID */
    private String id;

    /** 用户编号 userNo*/
    private Integer userNo;

    /** [组件类] 客户信息 */
    private DevIssueModel.PersonInfo personInfo;

    /** [组件类] 设备信息 */
    private DevIssueModel.DeviceInfo deviceInfo;

    /** [组件类] 发放信息 */
    private DevIssueModel.IssueInfo issueInfo;

    /** [组件] 家庭信息 */
    private FamilyInfos familyInfos;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @BuilderCall
    public static class PersonInfo {

        /** 1. [不可空] 客户ID */
        private String personId;

        /** 2. [不可空] 客户姓名 */
        private String personName;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @BuilderCall
    public static class DeviceInfo {

        /** 1. [不可空] 设备序列号 */
        private String deviceSn;

        /** 2. [不可空] 设备类型名称 */
        private String deviceTypeName;

        /** 3. [不可空] 设备型号名称 */
        private String deviceModelName;

        /** [不可空] 设备类型ID */
        private String deviceTypeId;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @BuilderCall
    public static class IssueInfo {

        /** [不可空] 发放时间 */
        private java.util.Date issueDate;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @BuilderCall
    public static class FamilyInfos {

        /** [不可空] 家庭ID */
        private String familyId;

        /** 户主名字 */
        private String housename;

    }

}
