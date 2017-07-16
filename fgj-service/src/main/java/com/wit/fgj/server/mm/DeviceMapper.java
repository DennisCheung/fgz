package com.wit.fgj.server.mm;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.wit.fgj.server.mm.DeviceModel.DevPerson;
import com.wit.fxp.domain.model.device.Device;

@Mapper
public interface DeviceMapper {

    DeviceMapper INSTANCE = Mappers.getMapper(DeviceMapper.class);

    DeviceModel map(Device entity, List<DevPerson> devPersonList, int userNo);

    DeviceModel.UsedInfo map(Device.UsedInfo entity);

    DeviceModel.BasicInfo map(Device.BasicInfo entity);

}
