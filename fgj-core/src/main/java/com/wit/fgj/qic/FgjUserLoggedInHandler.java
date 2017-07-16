package com.wit.fgj.qic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wit.qix.client.spi.UserLoggedInHandler;
import com.wit.qix.client.user.CurrentUser;
import com.wit.qix.prot.domain.WxUserId;

/**
 * 用户登录事件监听器。
 *
 * @author yw
 *
 */
@Service
public class FgjUserLoggedInHandler implements UserLoggedInHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FgjUserLoggedInHandler.class);

    @Autowired private CurrentUser currentUser;

    @Override
    public void handle(String sysUserId, WxUserId wxUserId) {
        LOGGER.debug("收到用户已登录事件：sysUserId={}, wxUserId={}", sysUserId, wxUserId);
        currentUser.initCurrentUser(sysUserId, wxUserId);
    }

}
