package com.wit.fgj.server.mm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.BuilderCall;

@Data
@AllArgsConstructor
@NoArgsConstructor
@BuilderCall
public class PersonModel {

    /**  [可空] 用户id */
    private String id;

    /** [组件] 个人基本信息 */
    private PersonModel.BasicInfo basicInfo;

    /** [组件] 联系信息 */
    private PersonModel.RelationInfo relationInfo;

    private PersonModel.FamilyInfo familyInfo;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @BuilderCall
    public static class BasicInfo {

        /**  [可空] 居民姓名 */
        private String personName;

        /**  [可空] 证件编号（身份证）*/
        private String idNo;

        /**  [可空] 性别中文 */
        private String sexName;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @BuilderCall
    public static class RelationInfo {

        /**  [可空] 居民手机 */
        private String personMobile;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @BuilderCall
    public static class FamilyInfo {

        /**  [可空] 家庭id */
        private String familyId;
    }

}
