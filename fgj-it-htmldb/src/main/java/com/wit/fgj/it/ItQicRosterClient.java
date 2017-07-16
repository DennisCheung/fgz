package com.wit.fgj.it;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wit.qix.client.roster.QicRosterClient;
import com.wit.qix.prot.department.WxDepartmentList;
import com.wit.qix.prot.department.command.WxCreateDepartmentCommand;
import com.wit.qix.prot.department.command.WxDeleteDepartmentCommand;
import com.wit.qix.prot.department.command.WxFindDepartmentListCommand;
import com.wit.qix.prot.department.command.WxUpdateDepartmentCommand;
import com.wit.qix.prot.domain.roster.WxCreateTagUserResult;
import com.wit.qix.prot.domain.roster.WxDeleleTagUserResult;
import com.wit.qix.prot.domain.roster.WxMemberInfo;
import com.wit.qix.prot.domain.roster.WxMembersDetails;
import com.wit.qix.prot.domain.roster.WxMembersInfo;
import com.wit.qix.prot.domain.roster.WxTagList;
import com.wit.qix.prot.domain.roster.WxTagUsers;
import com.wit.qix.prot.roster.WxRosterService;
import com.wit.qix.prot.roster.command.WxBatchDeleteMemberInfoCommand;
import com.wit.qix.prot.roster.command.WxCreateMemberInfoCommand;
import com.wit.qix.prot.roster.command.WxCreateTagCommand;
import com.wit.qix.prot.roster.command.WxCreateTagUsersCommand;
import com.wit.qix.prot.roster.command.WxDeleteMemberInfoCommand;
import com.wit.qix.prot.roster.command.WxDeleteTagCommand;
import com.wit.qix.prot.roster.command.WxDeleteTagUsersCommand;
import com.wit.qix.prot.roster.command.WxFindMemberDetailsByDepartmentIdCommand;
import com.wit.qix.prot.roster.command.WxFindMemberInfoByDepartmentIdCommand;
import com.wit.qix.prot.roster.command.WxFindMemberInfoCommand;
import com.wit.qix.prot.roster.command.WxFindTagUsersByTagIdCommand;
import com.wit.qix.prot.roster.command.WxUpdateMemberInfoCommand;
import com.wit.qix.prot.roster.command.WxUpdateTagCommand;

public class ItQicRosterClient implements WxRosterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItQicRosterClient.class);

    private final QicRosterClient delegate;

    public ItQicRosterClient(QicRosterClient delegate) {
        this.delegate = delegate;
    }

    @Override
    public WxMemberInfo findMemberInfo(WxFindMemberInfoCommand c) {
        return delegate.findMemberInfo(c);
    }

    @Override
    public void createMember(WxCreateMemberInfoCommand c) {
        LOGGER.warn("测试时不更改企业号通讯录：createMember，{}", c);
    }

    @Override
    public void updateMemberInfo(WxUpdateMemberInfoCommand c) {
        LOGGER.warn("测试时不更改企业号通讯录：updateMemberInfo，{}", c);
    }

    @Override
    public void deleteMember(WxDeleteMemberInfoCommand c) {
        LOGGER.warn("测试时不更改企业号通讯录：deleteMember，{}", c);
    }

    @Override
    public void batchDeleteMember(WxBatchDeleteMemberInfoCommand c) {
        LOGGER.warn("测试时不更改企业号通讯录：batchDeleteMember，{}", c);
    }

    @Override
    public WxMembersInfo findMemberInfoByDepartmentId(WxFindMemberInfoByDepartmentIdCommand c) {
        return delegate.findMemberInfoByDepartmentId(c);
    }

    @Override
    public WxMembersDetails findMemberDetailsByDepartmentId(WxFindMemberDetailsByDepartmentIdCommand c) {
        return delegate.findMemberDetailsByDepartmentId(c);
    }

    @Override
    public void createDepartment(WxCreateDepartmentCommand c) {
        LOGGER.warn("测试时不更改企业号通讯录：createDepartment，{}", c);
    }

    @Override
    public void updateDepartment(WxUpdateDepartmentCommand c) {
        LOGGER.warn("测试时不更改企业号通讯录：updateDepartment，{}", c);
    }

    @Override
    public void deleteDepartment(WxDeleteDepartmentCommand c) {
        LOGGER.warn("测试时不更改企业号通讯录：deleteDepartment，{}", c);
    }

    @Override
    public WxDepartmentList findDepartmentList(WxFindDepartmentListCommand c) {
        return delegate.findDepartmentList(c);
    }

    @Override
    public void createTag(WxCreateTagCommand c) {
        LOGGER.warn("测试时不更改企业号通讯录：createTag，{}", c);
    }

    @Override
    public void updateTag(WxUpdateTagCommand c) {
        LOGGER.warn("测试时不更改企业号通讯录：updateTag，{}", c);
    }

    @Override
    public void deleteTag(WxDeleteTagCommand c) {
        LOGGER.warn("测试时不更改企业号通讯录：deleteTag，{}", c);
    }

    @Override
    public WxTagUsers findTagUsersByTagId(WxFindTagUsersByTagIdCommand c) {
        return delegate.findTagUsersByTagId(c);
    }

    @Override
    public WxCreateTagUserResult createTagUsers(WxCreateTagUsersCommand c) {
        LOGGER.warn("测试时不更改企业号通讯录：createTagUsers，{}", c);
        return WxCreateTagUserResult.builder()
                ._invalIdList___("")
                ._invalIdParty__(new int[] {})
                ._build_();
    }

    @Override
    public WxDeleleTagUserResult deleteTagUsers(WxDeleteTagUsersCommand c) {
        LOGGER.warn("测试时不更改企业号通讯录：deleteTagUsers，{}", c);
        return WxDeleleTagUserResult.builder()
                ._invalIdList___("")
                ._invalIdParty__(new int[] {})
                ._build_();
    }

    @Override
    public WxTagList findTagList() {
        return delegate.findTagList();
    }

}
