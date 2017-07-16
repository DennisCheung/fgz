package com.wit.fgj.qic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wit.qix.client.spi.GuestAccessHandler;
import com.wit.qix.client.user.CurrentSession;

/**
 * 游客访问事件监听器。
 *
 */
@Service
public class FgjGuestAccessHandler implements GuestAccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FgjGuestAccessHandler.class);

    @Autowired private CurrentSession currentSession;

    @Override
    public void handle(String openId) {
        LOGGER.debug("收到游客访问事件，openId = {}", openId);

        currentSession.setOpenId(openId);
    }

}
