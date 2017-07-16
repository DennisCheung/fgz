package com.wit.fgj.server.mm;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.BuilderCall;

@Data
@AllArgsConstructor
@NoArgsConstructor
@BuilderCall
public class DevRecycleModel {

    /** 1. 客户设备回收ID */
    private String id;

    /** 2. 用户编号  */
    private Integer userNo;

    /** [组件] 客户信息 */
    private PersonInfo personInfo;

    /** [组件] 设备信息 */
    private DevRecycleModel.DeviceInfo deviceInfo;

    /** [组件] 回收信息 */
    private DevRecycleModel.RecycleInfo recycleInfo;

    /** [组件] 家庭信息 */
    private FamilyInfos familyInfos;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @BuilderCall
    public static class PersonInfo {

        /** [不可空] 客户ID */
        private String personId;

        /** [不可空] 客户姓名 */
        private String personName;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @BuilderCall
    public static class DeviceInfo {

        /** [不可空] 设备类型ID */
        private String deviceTypeId;

        /** [不可空] 设备类型名称 */
        private String deviceTypeName;

        /** [不可空] 设备型号名称 */
        private String deviceModelName;

        /** 设备序列号 */
        private String deviceSn;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @BuilderCall
    public static class RecycleInfo {

        /** [不可空] 回收时间 */
        private Date recycleDate;

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
