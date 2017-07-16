package com.wit.fgj.server.mm;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.wit.fxp.domain.model.device.Device;

@Mapper
public interface DeviceIssMapper {

    DeviceIssMapper INSTANCE = Mappers.getMapper(DeviceIssMapper.class);

    DeviceIssModel map(Device entity, int userNo);

    DeviceIssModel.UsedInfo map(Device.UsedInfo entity);

    DeviceIssModel.BasicInfo map(Device.BasicInfo entity);

}
