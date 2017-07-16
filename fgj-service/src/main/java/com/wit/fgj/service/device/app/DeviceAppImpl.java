package com.wit.fgj.service.device.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wit.datatype.IdAndName;
import com.wit.fgj.service.device.model.DeviceHmoIssueModel;
import com.wit.fgj.service.device.model.DeviceIssueCallModel;
import com.wit.fgj.service.device.model.DeviceIssueModel;
import com.wit.fgj.service.device.model.DeviceRecycleCallModel;
import com.wit.fgj.service.device.model.DeviceRecycleModel;
import com.wit.fxp.domain.invocation.Actor;
import com.wit.fxp.domain.invocation.InvocationContext;
import com.wit.fxp.domain.model.device.DevIssueCall;
import com.wit.fxp.domain.model.device.DevIssueFamily;
import com.wit.fxp.domain.model.device.DevIssueHmo;
import com.wit.fxp.domain.model.device.DevIssuePerson;
import com.wit.fxp.domain.model.device.DevIssuePerson.IssueInfo;
import com.wit.fxp.domain.model.device.DevIssuePerson.PersonInfo;
import com.wit.fxp.domain.model.device.DevIssueTeh;
import com.wit.fxp.domain.model.device.DevIssueWristband;
import com.wit.fxp.domain.model.device.DevRecycleFamily;
import com.wit.fxp.domain.model.device.DevRecyclePerson;
import com.wit.fxp.domain.model.device.Device;
import com.wit.fxp.domain.model.device.DeviceCall;
import com.wit.fxp.domain.model.device.DeviceHmo;
import com.wit.fxp.domain.model.device.DeviceTeh;
import com.wit.fxp.domain.model.device.DeviceWristband;
import com.wit.fxp.domain.model.device.repo.DevIssueRepo;
import com.wit.fxp.domain.model.device.repo.DeviceRepo;
import com.wit.fxp.domain.model.device.repo.DeviceTehRepo;
import com.wit.fxp.domain.model.device.repo.DeviceWristbandRepo;
import com.wit.fxp.domain.model.family.Family;
import com.wit.fxp.domain.model.family.repo.FamilyRepo;
import com.wit.fxp.domain.model.person.Person;
import com.wit.fxp.domain.service.device.DeviceServiceImpl;

@Component
@Transactional
public class DeviceAppImpl implements DeviceApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceServiceImpl.class);
    @Autowired private InvocationContext ic;
    @Autowired private DevIssueRepo devIssueRepo;
    @Autowired private DeviceRepo deviceRepo;
    @Autowired private DeviceWristbandRepo deviceWristbandRepo;
    @Autowired private DeviceTehRepo deviceTehRepo;
    @Autowired private FamilyRepo familyRepo;

    /**
     * 幸福手环
     */
    private static final  String DEVICE_TYPE_WRISTBAND = "WRISTBAND";

    /**
     * 定位手表
     */
    private static final String DEVICE_TYPE_TEH = "TEH";

    @Override
    public void deviceIssue(DeviceIssueModel model) {
        Actor actor = ic.createActor(model.getTeamId(), model.getUserId());

        Device device = deviceRepo.findByDeviceSn(model.getDeviceSn());

        //验证设备是否已经发放，如果发放抛出异常
        DevIssuePerson record = devIssueRepo.findDevIssuePersonByDeviceId(device.getId());
        if (record != null) {
            LOGGER.info("该设备已经发放DeviceId:{},deviceSn:{},客户姓名{}",
                    device.getId(), device.getBasicInfo().getDeviceSn(), record.getPersonInfo().getPersonName());
            throw new RuntimeException("该设备已经发放给" + record.getPersonInfo().getPersonName());
        }
        String deviceTypeId = device.getBasicInfo().getDeviceTypeId();
        //验证客户是否已经发放此类型的设备
        boolean flag = devIssueRepo.findDevIssuePersonByDeviceTypeId(deviceTypeId, model.getPersonId());
        if (flag) {
            LOGGER.info("已经发放{},给该客户，PersonId:{},DeviceTypeId{}", device.getBasicInfo().getDeviceTypeName(),
                    model.getPersonId(), device.getBasicInfo().getDeviceTypeId());
            throw new RuntimeException("已经发放" +  device.getBasicInfo().getDeviceTypeName() + "给该客户，不能重复发放");
        }

        Person person = ic.getEntityDaoService().findById(Person.class, model.getPersonId());

        IdAndName idAndNamePerson = new IdAndName(person.getId(), person.getBasicInfo().getPersonName());

        //1、 新增 客户设备发放表
        DevIssuePerson devIssuePerson = DevIssuePerson.新建()
                ._ic________(ic)
                ._actor_____(actor)
                ._device____(device)
                ._person____(idAndNamePerson)
                ._invoke_();

        devIssueRepo.saveDevIssuePerson(devIssuePerson);

        DeviceWristband deviceWristband = null;
        DeviceTeh deviceTeh = null;
        if (DEVICE_TYPE_WRISTBAND.equals(deviceTypeId)) {
            deviceWristband = deviceWristbandRepo.findByDeviceSn(model.getDeviceSn());
        }
        else if (DEVICE_TYPE_TEH.equals(deviceTypeId)) {
            deviceTeh = deviceTehRepo.findByDeviceSn(model.getDeviceSn());
        }
        else {
            LOGGER.error(" 不支持的设备类型,deviceTypeId{}", deviceTypeId);
            throw new RuntimeException("不支持的设备类型,deviceTypeId:" + deviceTypeId);
        }
        //2、新增设备发放表 数据，不同的设备类型插入到不同的表
        if (DEVICE_TYPE_WRISTBAND.equals(deviceTypeId)) {
            //----2、1 新增 手环设备发放表  fxp.p_dev_issue_wristband
            DevIssueWristband devIssueWristband = DevIssueWristband.新建()
                    ._ic________(ic)
                    ._actor_____(actor)
                    ._device____(deviceWristband)
                    ._person____(idAndNamePerson)
                    ._invoke_();

            devIssueRepo.saveDevIssueWristband(devIssueWristband);
        }
        else if (DEVICE_TYPE_TEH.equals(deviceTypeId)) {
            //----2.2 新增 定位设备发放表  fxp.p_dev_issue_teh
            DevIssueTeh devIssueTeh = DevIssueTeh.新建()
                    ._ic________(ic)
                    ._actor_____(actor)
                    ._device____(deviceTeh)
                    ._person____(idAndNamePerson)
                    ._invoke_();

            devIssueRepo.saveDevIssueTeh(devIssueTeh);
        }

        //3、更新 设备表 FXP.P_DEVICE 使用标志
        device.updateUsedInfo(actor.getUser(), ic.now());
        devIssueRepo.saveDevice(device);

        //4、更新 相应设备类型的设备使用标志
        if (DEVICE_TYPE_WRISTBAND.equals(deviceTypeId) && (deviceWristband != null)) {
            //----4.1、更新 手环设备 FXP.P_DEVICE_WRISTBAND 使用标志
            deviceWristband.updateUsedInfo(actor.getUser(), ic.now());
            ic.getEntityDaoService().saveOrUpdate(deviceWristband);
        }
        else if (DEVICE_TYPE_TEH.equals(deviceTypeId) && (deviceTeh != null)) {
            //----4.2、更新 定位设备 FXP.P_DEVICE_TEH 使用标志
            deviceTeh.updateUsedInfo(actor.getUser(), ic.now());
            ic.getEntityDaoService().saveOrUpdate(deviceTeh);
        }

    }

    @Override
    public void deviceRecycle(DeviceRecycleModel model) {
        Actor actor = ic.createActor(model.getTeamId(), model.getUserId());

        Device device = deviceRepo.findByDeviceSn(model.getDeviceSn());
        String deviceTypeId = device.getBasicInfo().getDeviceTypeId();

        DevIssuePerson devIssuePerson = devIssueRepo.findDevIssuePerson(model.getPersonId(), model.getDeviceSn());
        if (devIssuePerson == null) {
            LOGGER.error("根据客户和设备编码查找不到发放记录，personId{},deviceSn{}", model.getPersonId(), model.getDeviceSn());
            throw new RuntimeException("客户和设备编码不匹配");
        }
        DeviceWristband deviceWristband = null;
        DeviceTeh deviceTeh = null;
        if (DEVICE_TYPE_WRISTBAND.equals(deviceTypeId)) {
            deviceWristband = deviceWristbandRepo.findByDeviceSn(model.getDeviceSn());
        }
        else if (DEVICE_TYPE_TEH.equals(deviceTypeId)) {
            deviceTeh = deviceTehRepo.findByDeviceSn(model.getDeviceSn());
        }
        else {
            LOGGER.error(" 不支持的设备类型,deviceTypeId{}", deviceTypeId);
            throw new RuntimeException("不支持的设备类型,前选择其他设备类型");
        }

        //1、新增 客户设备回收
        DevRecyclePerson devRecyclePerson = DevRecyclePerson.新建()
                ._ic____(ic)
                ._actor_(actor)
                ._dev___(devIssuePerson)
                ._invoke_();
        devIssueRepo.saveDevRecyclePerson(devRecyclePerson);

        //2、删除 客户设备发放记录
        ic.getEntityDaoService().delete(devIssuePerson);

       //3、删除相应设备设备发放记录
        if (DEVICE_TYPE_WRISTBAND.equals(deviceTypeId)) {
            //3.1、删除手环设备发放记录
            devIssueRepo.deleteDevIssueWristband(device.getId());
        }
        else if (DEVICE_TYPE_TEH.equals(deviceTypeId)) {
            //3.2、删除 定位设备发放记录
            devIssueRepo.deleteDevIssueTeh(device.getId());
        }

        //4、更新 设备表 FXP.P_DEVICE 使用标志
        device.updateUnUsedInfo();
        devIssueRepo.saveDevice(device);

        //5、更新 相应设备类型的设备使用标志
        if (DEVICE_TYPE_WRISTBAND.equals(deviceTypeId) && (deviceWristband != null)) {
            //----4.1、更新 手环设备 FXP.P_DEVICE_WRISTBAND 使用标志
            deviceWristband.updateUnUsedInfo();
            ic.getEntityDaoService().saveOrUpdate(deviceWristband);
        }
        else if (DEVICE_TYPE_TEH.equals(deviceTypeId) && (deviceTeh != null)) {
            //----4.2、更新 定位设备 FXP.P_DEVICE_TEH 使用标志
            deviceTeh.updateUnUsedInfo();
            ic.getEntityDaoService().saveOrUpdate(deviceTeh);
        }

    }

    @Override
    public void deviceHmoIssue(DeviceHmoIssueModel model) {
        LOGGER.info("用于监测userno,model.getUserNo() = {}", model.getUserNo());
        Actor actor = ic.createActor(model.getTeamId(), model.getUserId());

        Device device = deviceRepo.findDeviceById(model.getDeviceId());
        DeviceHmo deviceHmo = deviceRepo.findDeviceHmoById(model.getDeviceId());
        if (deviceHmo == null) {
            deviceHmo = deviceRepo.findDeviceHmoByUniqueId(device.getBasicInfo().getCompanyId(),
                    device.getBasicInfo().getDeviceModelId(), device.getBasicInfo().getDeviceSn());
        }
        Person person = this.ic.getEntityDaoService().findById(Person.class, model.getPersonId());
        DevIssueHmo devIssueHmo = this.deviceRepo.findDevIssueHmoByDeviceId(model.getDeviceId(), model.getUserNo());
        DevIssuePerson devIssuePerson = this.devIssueRepo.findDevIssuePersonByDeviceId(device.getId(), model.getUserNo());
        if (model.getIsDelete()) {
            if (devIssueHmo != null && devIssuePerson != null) {
                //新增客户设备回收
                DevRecyclePerson devRecyclePerson = DevRecyclePerson.新建()
                        ._ic____(ic)
                        ._actor_(actor)
                        ._dev___(devIssuePerson)
                        ._invoke_();
                devIssueRepo.saveDevRecyclePerson(devRecyclePerson);
                deviceHmo.decreateUserNo();
                if (deviceHmo.getBasicInfo().getUsedNo() != null && deviceHmo.getBasicInfo().getUsedNo() >= 2) {
                    return;
                }
                else {
                    if (this.deviceRepo.findDevHmoIssueNumBySn(device.getBasicInfo().getDeviceSn()) == 1) {
                        LOGGER.info("被回收的设备为该设备在发放表中最后一条记录，应该将抽象设备表与子类设备表同时更新使用标志，deviceSn={}", device.getBasicInfo().getDeviceSn());
                        device.updateUnUsedInfo();
                        deviceHmo.unIssue();
                    }
                }
                ic.getEntityDaoService().delete(devIssuePerson);
                ic.getEntityDaoService().delete(devIssueHmo);
            }
            return;
        }
        if (devIssueHmo != null && devIssuePerson != null) {
            devIssueHmo.updateIssueDesc(model.getIssueDesc());
            devIssuePerson.updateIssueDesc(model.getIssueDesc());
        }
        else {
            deviceHmo.increateUserNo();
            DevIssueHmo issueDevice = DevIssueHmo.fullDevIssueHmoBuilder()
                    ._id____________(ic.generateId(DevIssueHmo.class))
                    ._personInfo____(DevIssueHmo.PersonInfo.fullBuilder()
                        ._personId______(person.getId())
                        ._personName____(person.getBasicInfo().getPersonName())
                        ._build_())
                    ._deviceInfo____(DevIssueHmo.DeviceInfo.fullBuilder()
                            ._deviceId__________(device.getId())
                            ._companyId_________(device.getBasicInfo().getCompanyId())
                            ._companyName_______(device.getBasicInfo().getCompanyName())
                            ._deviceModelId_____(device.getBasicInfo().getDeviceModelId())
                            ._deviceModelName___(device.getBasicInfo().getDeviceModelName())
                            ._deviceSn__________(device.getBasicInfo().getDeviceSn())
                            ._deviceUuid________(device.getBasicInfo().getDeviceUuid())
                            ._productionDate____(device.getBasicInfo().getProductionDate())
                            ._purchaseDate______(device.getBasicInfo().getPurchaseDate())
                            ._deviceDesc________(device.getBasicInfo().getDeviceDesc())
                            ._maxUser___________(device.getBasicInfo().getMaxUser())
                            ._userNo____________(model.getUserNo())
                            ._build_())
                    ._circleInfo____(DevIssueHmo.CircleInfo.fullBuilder()
                            ._circleId__(actor.getCircle().getId())
                            ._build_())
                    ._issueInfo_____(DevIssueHmo.IssueInfo.fullBuilder()
                            ._issueDate_____(ic.now())
                            ._issueCircleId_(actor.getCircle().getId())
                            ._issueTeamId___(actor.getTeam().getId())
                            ._issueTeamName_(actor.getTeam().getTeamName())
                            ._issueUserId___(actor.getUser().getId())
                            ._issueNickname_(actor.getUser().getBasicInfo().getNickname())
                            ._issueDesc_____(model.getIssueDesc())
                            ._appUsername___(null)
                            ._appPassword___(null)
                            ._build_())
                    ._build_();
            DevIssuePerson issuePerson = DevIssuePerson.fullDevIssuePersonBuilder()
                    ._id________________(ic.generateId(DevIssuePerson.class))
                    ._maxUser___________(device.getBasicInfo().getMaxUser())
                    ._productionDate____(device.getBasicInfo().getProductionDate())
                    ._purchaseDate______(device.getBasicInfo().getPurchaseDate())
                    ._userNo____________(model.getUserNo())
                    ._deviceDesc________(device.getBasicInfo().getDeviceDesc())
                    ._personInfo________(PersonInfo.fullBuilder()
                            ._personId______(person.getId())
                            ._personName____(person.getBasicInfo().getPersonName())
                            ._build_())
                    ._deviceInfo________(DevIssuePerson.DeviceInfo.fullBuilder()
                            ._deviceId__________(device.getId())
                            ._deviceTypeId______(device.getBasicInfo().getDeviceTypeId())
                            ._deviceTypeName____(device.getBasicInfo().getDeviceTypeName())
                            ._deviceModelId_____(device.getBasicInfo().getDeviceModelId())
                            ._deviceModelName___(device.getBasicInfo().getDeviceModelName())
                            ._companyId_________(device.getBasicInfo().getCompanyId())
                            ._companyName_______(device.getBasicInfo().getCompanyName())
                            ._deviceSn__________(device.getBasicInfo().getDeviceSn())
                            ._deviceUuid________(device.getBasicInfo().getDeviceUuid())
                            ._build_())
                    ._circleInfo________(DevIssuePerson.CircleInfo.fullBuilder()
                            ._circleId__(actor.getCircle().getId())
                            ._build_())
                    ._issueInfo_________(IssueInfo.fullBuilder()
                            ._issueDate_____(ic.now())
                            ._issueCircleId_(actor.getCircle().getId())
                            ._issueTeamId___(actor.getTeam().getId())
                            ._issueTeamName_(actor.getTeam().getTeamName())
                            ._issueUserId___(actor.getUser().getId())
                            ._issueNickname_(actor.getUser().getBasicInfo().getNickname())
                            ._issueDesc_____(model.getIssueDesc())
                            ._appUsername___(null)
                            ._appPassword___(null)
                            ._build_())
                    ._build_();
            this.devIssueRepo.saveDevIssuePerson(issuePerson);
            this.devIssueRepo.saveDeviceIssueHmo(issueDevice);
            device.updateUsedInfo(actor.getUser(), ic.now());
            deviceHmo.issue(actor.getUser(), ic.now());
        }
    }

//    private Integer getUserNo(DeviceHmo deviceHmo) {
//        List<Integer> un = new ArrayList<>();
//        for (Integer i = 1; i <= deviceHmo.getBasicInfo().getMaxUser(); i++) {
//            un.add(i);
//        }
//        //因为java缓存问题,用对象contains应该有匹配
//        for (com.wit.fxp.domain.model.device.DevIssueHmo issue : deviceHmo.getDevIssueHmoSet()) {
//            if (un.contains(issue.getDeviceInfo().getUserNo())) {
//                un.remove(issue.getDeviceInfo().getUserNo());
//            }
//        }
//        return un.get(0);
//    }

    @Override
    public void decreateDevIssueHmoUserNo(String deviceId) {
        DeviceHmo deviceHmo = deviceRepo.findDeviceHmoById(deviceId);
        deviceHmo.decreateUserNo();
    }

    @Override
    public void deviceCall(DeviceIssueCallModel model) {
        //1. 找到操作员
        Actor actor = this.ic.createActor(model.getTeamId(), model.getUserId());

        //2. 找到设备(抽象设备)
        Device device = this.deviceRepo.findByDeviceSn(model.getDeviceSn());

        //3. 找到 呼叫型设备
        DeviceCall deviceCall = this.deviceRepo.findDeviceCallBySn(model.getDeviceSn());

        //4. 找到家庭
        Family family = this.familyRepo.findFamilyById(model.getFamilyId());

        //验证是否有对已拥有设备客户进行再次发放
        boolean flag = devIssueRepo.findDevIssueFamilyByDeviceTypeId(device.getBasicInfo().getDeviceTypeId(), model.getFamilyId());
        if (flag) {
            LOGGER.info("已经发放{},给该客户，FamilyId:{},DeviceTypeId{}", device.getBasicInfo().getDeviceTypeName(),
                    model.getFamilyId(), device.getBasicInfo().getDeviceTypeId());
            throw new RuntimeException("已经发放" +  device.getBasicInfo().getDeviceTypeName() + "给该客户，不能重复发放");
        }
        //5. 新增 家庭设备发放表 fxp.p_dev_issue_family
        DevIssueFamily devIssueFamilyEntity = DevIssueFamily.新建家庭设备发放记录()
                ._ic________(ic)
                ._actor_____(actor)
                ._device____(device)
                ._family____(family)
                ._desc______(model.getDesc())
                ._invoke_();
        this.devIssueRepo.saveDevIssueFamily(devIssueFamilyEntity);

        //6. 新增 呼叫型发放表 fxp.p_device_call
        DevIssueCall devIssueCallEntity = DevIssueCall.新建呼叫型设备发放记录()
                ._ic________(ic)
                ._actor_____(actor)
                ._device____(deviceCall)
                ._family____(family)
                ._desc______(model.getDesc())
                ._invoke_();
        this.devIssueRepo.saveDevIssueCall(devIssueCallEntity);

        //7. 更新 设备表 FXP.P_DEVICE 使用标志
        device.updateUsedInfo(actor.getUser(), ic.now());

        //8. 更新 定位设备 FXP.P_DEVICE_CALL 使用标志
        deviceCall.updateUsedInfo(actor.getUser(), ic.now());

    }

    @Override
    public void deviceCallRecycle(DeviceRecycleCallModel model) {
        //找到操作员
        Actor actor = this.ic.createActor(model.getTeamId(), model.getUserId());

        //找到设备(抽象设备)
        Device device = this.deviceRepo.findByDeviceSn(model.getDeviceSn());

        //找到 呼叫型设备
        DeviceCall deviceCall = this.deviceRepo.findDeviceCallBySn(model.getDeviceSn());

        //找到家庭
        Family family = this.familyRepo.findFamilyById(model.getFamilyId());

        //找到发放记录
        DevIssueFamily devIssueFamily = devIssueRepo.findDevIssueFamilyByDeviceId(model.getFamilyId(), model.getDeviceSn());
        if (devIssueFamily == null) {
            LOGGER.error("根据家庭ID和设备编码查找不到发放记录，FamilyId{},deviceSn{}", model.getFamilyId(), model.getDeviceSn());
            throw new RuntimeException("家庭ID和设备编码不匹配");
        }

        //1、新增 客户设备回收
        DevRecycleFamily devRecycleFamily = DevRecycleFamily.fullDevRecycleFamilyBuilder()
                ._id____________(ic.generateId(DevRecycleFamily.class))
                ._familyInfo____(DevRecycleFamily.FamilyInfo.fullBuilder()
                        ._familyId__(family.getId())
                        ._build_())
                ._deviceInfo____(DevRecycleFamily.DeviceInfo.fullBuilder()
                        ._deviceId__________(device.getId())
                        ._deviceTypeId______(device.getBasicInfo().getDeviceTypeId())
                        ._deviceTypeName____(device.getBasicInfo().getDeviceTypeName())
                        ._companyId_________(device.getBasicInfo().getCompanyId())
                        ._companyName_______(device.getBasicInfo().getCompanyName())
                        ._deviceModelId_____(device.getBasicInfo().getDeviceModelId())
                        ._deviceModelName___(device.getBasicInfo().getDeviceModelName())
                        ._deviceSn__________(device.getBasicInfo().getDeviceSn())
                        ._deviceUuid________(device.getBasicInfo().getDeviceUuid())
                        ._productionDate____(device.getBasicInfo().getProductionDate())
                        ._purchaseDate______(device.getBasicInfo().getPurchaseDate())
                        ._deviceDesc________(device.getBasicInfo().getDeviceDesc())
                        ._build_())
                ._circleInfo____(DevRecycleFamily.CircleInfo.fullBuilder()
                        ._circleId__(device.getCircleInfo().getCircleId())
                        ._build_())
                ._issueInfo_____(DevRecycleFamily.IssueInfo.fullBuilder()
                        ._issueDate_____(devIssueFamily.getIssueInfo().getIssueDate())
                        ._issueCircleId_(devIssueFamily.getIssueInfo().getIssueCircleId())
                        ._issueTeamId___(devIssueFamily.getIssueInfo().getIssueTeamId())
                        ._issueTeamName_(devIssueFamily.getIssueInfo().getIssueTeamName())
                        ._issueUserId___(devIssueFamily.getIssueInfo().getIssueUserId())
                        ._issueNickname_(devIssueFamily.getIssueInfo().getIssueNickname())
                        ._issueDesc_____(devIssueFamily.getIssueInfo().getIssueDesc())
                        ._build_())
                ._recycleInfo___(DevRecycleFamily.RecycleInfo.fullBuilder()
                        ._recycleDate_______(ic.now())
                        ._recycleCircleId___(actor.getCircle().getId())
                        ._recycleTeamId_____(actor.getTeam().getId())
                        ._recycleUserId_____(actor.getUser().getId())
                        ._recycleNickname___(actor.getUser().getBasicInfo().getNickname())
                        ._recycleDesc_______(null)
                        ._build_())
                ._build_();
        devIssueRepo.saveDevRecycleFamily(devRecycleFamily);

        //2、删除客户设备发放表
        devIssueRepo.deleteDevIssueFamily(device.getId());

        //3、删除CALL设备发放记录
        devIssueRepo.deleteDevIssueCall(device.getId());

        //4.1、更新 设备表 FXP.P_DEVICE 使用标志
        device.updateUnUsedInfo();
        devIssueRepo.saveDevice(device);

        //4.2、更新 设备表 FXP.P_DEVICE_call 使用标志
        deviceCall.updateUnUsedInfo();
        ic.getEntityDaoService().saveOrUpdate(deviceCall);
    }

}
