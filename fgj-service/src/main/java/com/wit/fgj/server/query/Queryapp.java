package com.wit.fgj.server.query;

import java.util.List;

import com.wit.fgj.server.mm.DevIssueModel;
import com.wit.fgj.server.mm.DevRecycleModel;
import com.wit.fgj.server.mm.DeviceIssModel;
import com.wit.fgj.server.mm.DeviceModel;
import com.wit.fgj.server.mm.PersonModel;

/**
 * 查询app接口
 * @author ROCKY
 *
 */
public interface Queryapp {

    List<DevIssueModel> findDevIssueNote(String teamId);

    List<DevRecycleModel> findDevRecycleNote(String teamId);

    List<PersonModel> findPersonBytext(String teamId, String text);

    List<DeviceIssModel> findDeviceBySnFalse(String snStr, String teamId);

    List<DeviceModel> findDeviceBySnForRecycle(String snStr, String teamId);

}
