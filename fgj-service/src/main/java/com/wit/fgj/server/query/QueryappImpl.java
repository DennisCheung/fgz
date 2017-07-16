package com.wit.fgj.server.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wit.fgj.server.mm.DevIssueMapper;
import com.wit.fgj.server.mm.DevIssueModel;
import com.wit.fgj.server.mm.DevRecycleMapper;
import com.wit.fgj.server.mm.DevRecycleModel;
import com.wit.fgj.server.mm.DevRecycleModel.FamilyInfos;
import com.wit.fgj.server.mm.DevRecycleModel.PersonInfo;
import com.wit.fgj.server.mm.DeviceIssMapper;
import com.wit.fgj.server.mm.DeviceIssModel;
import com.wit.fgj.server.mm.DeviceMapper;
import com.wit.fgj.server.mm.DeviceModel;
import com.wit.fgj.server.mm.DeviceModel.DevPerson;
import com.wit.fgj.server.mm.PersonMapper;
import com.wit.fgj.server.mm.PersonModel;
import com.wit.fxp.domain.model.circle.Team;
import com.wit.fxp.domain.model.circle.repo.TeamRepo;
import com.wit.fxp.domain.model.device.DevIssueFamily;
import com.wit.fxp.domain.model.device.DevIssuePerson;
import com.wit.fxp.domain.model.device.DevRecycleFamily;
import com.wit.fxp.domain.model.device.DevRecyclePerson;
import com.wit.fxp.domain.model.device.Device;
import com.wit.fxp.domain.model.device.repo.DevIssueRepo;
import com.wit.fxp.domain.model.family.Family;
import com.wit.fxp.domain.model.family.repo.FamilyRepo;
import com.wit.fxp.domain.model.person.Person;
import com.wit.fxp.domain.model.person.repo.PersonRepo;
import com.wit.fxp.domain.service.device.TehDeviceService;
import com.wit.fxp.domain.service.team.GzqTeamService;

@Service
@Transactional
public class QueryappImpl implements Queryapp {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryappImpl.class);

    @Autowired private TehDeviceService devservice;
    @Autowired private TeamRepo teamrepo;
    @Autowired private PersonRepo personrepo;
    @Autowired private GzqTeamService gzqTeamService;
    @Autowired private FamilyRepo familyrepo;
    @Autowired private DevIssueRepo devissuerepo;

    @Override
    public List<DevIssueModel> findDevIssueNote(String teamId) {
        List<DevIssueModel> list = new ArrayList<>();
        List<DevIssuePerson> personlist = devservice.findDevIssuePerson(teamId);
        List<DevIssueFamily> familylist = devservice.findDevIssueFamily(teamId);
        personlist.stream().forEach(e -> {
            com.wit.fgj.server.mm.DevIssueModel.PersonInfo personInfos = com.wit.fgj.server.mm.DevIssueModel.PersonInfo.builder()
                    ._personId______(e.getPersonInfo().getPersonId())
                    ._personName____(e.getPersonInfo().getPersonName())
                    ._build_();
            list.add(DevIssueMapper.INSTANCE.map(e, personInfos, null));
        });
        familylist.stream().forEach(e -> {
            Family family = familyrepo.findFamilyById(e.getFamilyInfo().getFamilyId());
            com.wit.fgj.server.mm.DevIssueModel.FamilyInfos familyInfoss = com.wit.fgj.server.mm.DevIssueModel.FamilyInfos.builder()
                    ._familyId__(e.getFamilyInfo().getFamilyId())
                    ._housename_(family.findOwner().getBasicInfo().getPersonName())
                    ._build_();
            list.add(DevIssueMapper.INSTANCE.map(e, null, familyInfoss, null));
        });
        Collections.sort(list, new Comparator<DevIssueModel>() {
            @Override
            public int compare(DevIssueModel o1, DevIssueModel o2) {
                if (o1.getIssueInfo().getIssueDate().after(o2.getIssueInfo().getIssueDate())) {
                    return -1;
                }
                else if (o1.getIssueInfo().getIssueDate().before(o2.getIssueInfo().getIssueDate())) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
        });
        return list;
    }

    @Override
    public List<DevRecycleModel> findDevRecycleNote(String teamId) {
        List<DevRecycleModel> list = new ArrayList<>();
        List<DevRecyclePerson> personlist = devservice.findDevRecyclePerson(teamId);
        List<DevRecycleFamily> familylist = devservice.findDevRecycleFamily(teamId);
        personlist.stream().forEach(e -> {
            PersonInfo personInfo = PersonInfo.builder()
                    ._personId______(e.getPersonInfo().getPersonId())
                    ._personName____(e.getPersonInfo().getPersonName())
                    ._build_();
            list.add(DevRecycleMapper.INSTANCE.map(e, personInfo, null, e.getDeviceInfo().getUserNo()));
        });
        familylist.stream().forEach(e -> {
            Family family = familyrepo.findFamilyById(e.getFamilyInfo().getFamilyId());
            FamilyInfos familyInfos = FamilyInfos.builder()
                    ._familyId__(e.getFamilyInfo().getFamilyId())
                    ._housename_(family.findOwner().getBasicInfo().getPersonName())
                    ._build_();
            list.add(DevRecycleMapper.INSTANCE.map(e, null, familyInfos, null));
        });
        Collections.sort(list, new Comparator<DevRecycleModel>() {
            @Override
            public int compare(DevRecycleModel o1, DevRecycleModel o2) {
                if (o1.getRecycleInfo().getRecycleDate().after(o2.getRecycleInfo().getRecycleDate())) {
                    return -1;
                }
                else if (o1.getRecycleInfo().getRecycleDate().before(o2.getRecycleInfo().getRecycleDate())) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
        });
        return list;
    }

    @Override
    public List<PersonModel> findPersonBytext(String teamId, String text) {
        Team team = teamrepo.findById(teamId);
        return PersonMapper.INSTANCE.map(this.devservice.findPerson(team.getCircle().getId(), text));
    }

    @Override
    public List<DeviceIssModel> findDeviceBySnFalse(String snStr, String teamId) {
        List<DeviceIssModel> list = new ArrayList<DeviceIssModel>();

        String circleId = this.gzqTeamService.findTeamById(teamId).getCircle().getId();

        //1. 得到所有最大使用人数为1，且未被使用的设备
        List<Device> maxUserOnelist = devservice.findDeviceBySnAllUnUsedAndMaxUserOne(snStr, circleId);

        //2. 得到所有最大使用人数大于1，且未被使用的设备
        List<Device> maxUserGtOneUnUsedlist = devservice.findDeviceBySnAllUnUsedAndMaxUserGtOne(snStr, false, circleId);

        //3. 得到所有最大使用人数大于1，且已被使用,但是任存在可用编号的设备
        List<Device> maxUserGtOneUsedlist = devservice.findDeviceBySnAllUnUsedAndMaxUserGtOne(snStr, true, circleId);

        //清除list中的null
        maxUserGtOneUsedlist.removeAll(Collections.singleton(null));

        //4. 入列使用人数为1的设备
        if (maxUserOnelist.isEmpty()) {
            LOGGER.info("未找到所有使用人数等于1的设备");
        }
        else {
            maxUserOnelist.forEach(e -> {
                list.add(DeviceIssMapper.INSTANCE.map(e, 1));
            });

        }

        //5. 入列使用人数大于1，且未被使用的设备
        if (maxUserGtOneUnUsedlist.isEmpty()) {
            LOGGER.info("未找到所有使用人数大于1的，但是未被使用的设备");
        }
        else {
            for (int i = 0; i < maxUserGtOneUnUsedlist.size(); i++) {
                for (int j = 1; j < maxUserGtOneUnUsedlist.get(i).getBasicInfo().getMaxUser() + 1; j++) {
                    list.add(DeviceIssMapper.INSTANCE.map(maxUserGtOneUnUsedlist.get(i), j));
                }
            }
        }

        //6. 入列使用人数大于1，且已被使用，但是任有可用编号的设备
        if (maxUserGtOneUsedlist.isEmpty()) {
            LOGGER.info("未找到所有使用人数大于1的，且未被使用的设备，但是仍存在可用编号的设备");
        }
        else {
            maxUserGtOneUsedlist.forEach(e -> {
                //1.该设备所有可用编号列表
                List<Integer> allList = new ArrayList<Integer>();
                for (int i = 1; i < e.getBasicInfo().getMaxUser() + 1; i++) {
                    allList.add(i);
                }

                //2.该设备已使用的编号列表
                List<Integer> userdList = this.devservice.findDevIssueHmo(e.getBasicInfo().getDeviceSn());

                //3.得到该设备可使用的编号列表
                allList.removeAll(userdList);

                //4.入列
                allList.forEach(f -> {
                    list.add(DeviceIssMapper.INSTANCE.map(e, f));
                });
            });
        }

        return list;
    }

    @Override
    public List<DeviceModel> findDeviceBySnForRecycle(String snStr, String teamId) {
        List<DeviceModel> list = new ArrayList<DeviceModel>();

        String circleId = this.gzqTeamService.findTeamById(teamId).getCircle().getId();

        // 1. 查找所有使用人数等于1，并且已被使用的设备
        List<Device> usedNumOnelist = this.devservice.findDeviceBySnAllUsed(snStr, circleId);

        // 2. 查找所有使用人数大于1，并且已被使用的设备
        List<Device> usedNumGtOneList = this.devservice.findDeviceByAllUsedAndMaxUserGtOne(snStr, true, circleId);

        // 3.入列所有使用人数等于1，并且已被使用的设备
        if (usedNumOnelist.isEmpty()) {
            LOGGER.info("未找到所有使用人数等于1，并且已经使用的设备列表");
        }
        else {
            usedNumOnelist.forEach(e -> {
                if (e.getBasicInfo().getDeviceTypeId().equals("CALL")) {
                    DevIssueFamily devIssueFamily = devissuerepo.findDevIssueFamilyByOnlySn(e.getBasicInfo().getDeviceSn());
                    Family family = familyrepo.findFamilyById(devIssueFamily.getFamilyInfo().getFamilyId());
                    List<DevPerson> listperson = new ArrayList<DevPerson>();
                    DevPerson devPerson = DevPerson.builder()
                            ._personId______(null)
                            ._personName____(null)
                            ._sexName_______(null)
                            ._idNo__________(null)
                            ._familyId______(family.getId())
                            ._famPerson_____(family.findOwner().getBasicInfo().getPersonName())
                            ._build_();
                    listperson.add(devPerson);
                    list.add(DeviceMapper.INSTANCE.map(e, listperson, 1));
                }
                else {
                    list.add(DeviceMapper.INSTANCE.map(e, e.getDevIssuePersonSet().stream().map(i -> {
                        Person person = personrepo.findById(i.getPersonInfo().getPersonId());
                        return DevPerson.builder()
                                ._personId______(i.getPersonInfo().getPersonId())
                                ._personName____(person.getBasicInfo().getPersonName())
                                ._sexName_______(person.getBasicInfo().getSexName())
                                ._idNo__________(person.getBasicInfo().getIdNo())
                                ._familyId______(null)
                                ._famPerson_____(null)
                                ._build_();
                    }).collect(Collectors.toList()), 1));
                }
            });
        }

        // 4. 入列所有使用人数大于1，并且已被使用的设备
        if (usedNumGtOneList.isEmpty()) {
            LOGGER.info("未找到所有使用人数大于1，并且已经使用的设备列表");
        }
        else {
            usedNumGtOneList.forEach(e -> {
                //
                List<Integer> usedUserNoList = this.devservice.findDevIssueHmo(e.getBasicInfo().getDeviceSn());
                usedUserNoList.forEach(f -> {
                    list.add(DeviceMapper.INSTANCE.map(e, e.getDevIssuePersonSet().stream().map(i -> {
                        Person person = personrepo.findById(i.getPersonInfo().getPersonId());
                        return DevPerson.builder()
                                ._personId______(i.getPersonInfo().getPersonId())
                                ._personName____(person.getBasicInfo().getPersonName())
                                ._sexName_______(person.getBasicInfo().getSexName())
                                ._idNo__________(person.getBasicInfo().getIdNo())
                                ._familyId______(null)
                                ._famPerson_____(null)
                                ._build_();
                    }).collect(Collectors.toList()), f));
                });
            });
        }
        return list;
    }

}
