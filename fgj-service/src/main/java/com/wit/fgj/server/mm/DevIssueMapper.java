package com.wit.fgj.server.mm;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.wit.fgj.server.mm.DevIssueModel.FamilyInfos;
import com.wit.fgj.server.mm.DevIssueModel.PersonInfo;
import com.wit.fxp.domain.model.device.DevIssueFamily;
import com.wit.fxp.domain.model.device.DevIssuePerson;

@Mapper
public interface DevIssueMapper {

    DevIssueMapper INSTANCE = Mappers.getMapper(DevIssueMapper.class);

    DevIssueModel map(DevIssuePerson entity, PersonInfo personInfo, FamilyInfos familyInfos);

    DevIssueModel map(DevIssueFamily entity, PersonInfo personInfo, FamilyInfos familyInfos, Integer userNo);

    DevIssueModel.DeviceInfo map(DevIssueFamily.DeviceInfo entity);

    DevIssueModel.DeviceInfo map(DevIssuePerson.DeviceInfo entity);

    DevIssueModel.IssueInfo map(DevIssuePerson.IssueInfo entity);

    DevIssueModel.IssueInfo map(DevIssueFamily.IssueInfo entity);

}
