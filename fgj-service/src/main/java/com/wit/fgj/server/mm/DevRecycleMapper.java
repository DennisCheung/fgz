package com.wit.fgj.server.mm;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.wit.fgj.server.mm.DevRecycleModel.FamilyInfos;
import com.wit.fgj.server.mm.DevRecycleModel.PersonInfo;
import com.wit.fxp.domain.model.device.DevRecycleFamily;
import com.wit.fxp.domain.model.device.DevRecyclePerson;

@Mapper
public interface DevRecycleMapper {

    DevRecycleMapper INSTANCE = Mappers.getMapper(DevRecycleMapper.class);

    DevRecycleModel map(DevRecyclePerson entity, PersonInfo personInfo, FamilyInfos familyInfos, Integer userNo);

    DevRecycleModel map(DevRecycleFamily entity, PersonInfo personInfo, FamilyInfos familyInfos, Integer userNo);

    DevRecycleModel.DeviceInfo map(DevRecycleFamily.DeviceInfo entity);

    DevRecycleModel.DeviceInfo map(DevRecyclePerson.DeviceInfo entity);

    DevRecycleModel.RecycleInfo map(DevRecycleFamily.RecycleInfo entity);

    DevRecycleModel.RecycleInfo map(DevRecyclePerson.RecycleInfo entity);

}
