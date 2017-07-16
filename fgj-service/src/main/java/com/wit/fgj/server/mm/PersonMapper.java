package com.wit.fgj.server.mm;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.wit.fxp.domain.model.person.Person;

@Mapper
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonModel map(Person entity);

    PersonModel.BasicInfo map(Person.BasicInfo entity);

    PersonModel.RelationInfo map(Person.RelationInfo entity);

    PersonModel.FamilyInfo map(Person.FamilyInfo entity);

    List<PersonModel> map(List<Person> entitys);

}
