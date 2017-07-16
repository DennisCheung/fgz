package com.wit.fgj.it;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wit.qix.client.chat.QicChatClient;
import com.wit.qix.prot.chat.WxChatService;
import com.wit.qix.prot.chat.command.WxCreateChatCommand;
import com.wit.qix.prot.chat.command.WxDeleteChatCommand;
import com.wit.qix.prot.chat.command.WxDeleteChatNotifyCommand;
import com.wit.qix.prot.chat.command.WxFindChatInfoByChatIdCommand;
import com.wit.qix.prot.chat.command.WxSendChatFileMessageCommand;
import com.wit.qix.prot.chat.command.WxSendChatImageMessageCommand;
import com.wit.qix.prot.chat.command.WxSendChatLinkMessageCommand;
import com.wit.qix.prot.chat.command.WxSendChatTextMessageCommand;
import com.wit.qix.prot.chat.command.WxSendChatVoiceMessageCommand;
import com.wit.qix.prot.chat.command.WxUpdateChatInfoCommand;
import com.wit.qix.prot.chat.command.WxUpdateMuteCommand;
import com.wit.qix.prot.domain.chat.WxChatInfo;
import com.wit.qix.prot.domain.chat.WxInvalidUserList;

public class ItQicChatClient implements WxChatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItQicChatClient.class);

    private final QicChatClient delegate;

    public ItQicChatClient(QicChatClient delegate) {
        this.delegate = delegate;
    }

    @Override
    public void createChat(WxCreateChatCommand c) {
        LOGGER.warn("测试时不创建企业号会话：createChat，{}", c);
    }

    @Override
    public WxChatInfo findChatInfoByChatId(WxFindChatInfoByChatIdCommand c) {
        return delegate.findChatInfoByChatId(c);
    }

    @Override
    public void updateChatInfo(WxUpdateChatInfoCommand c) {
        LOGGER.warn("测试时不更新企业号会话信息：updateChatInfo，{}", c);
    }

    @Override
    public void deleteChat(WxDeleteChatCommand c) {
        LOGGER.warn("测试时不删除企业号会话：deleteChat，{}", c);
    }

    @Override
    public void deleteChatNotify(WxDeleteChatNotifyCommand c) {
        LOGGER.warn("测试时不删除企业号会话通知：deleteChatNotify，{}", c);
    }

    @Override
    public void sendChatTextMessage(WxSendChatTextMessageCommand c) {
        LOGGER.warn("测试时企业号不发送文本类型消息：sendChatTextMessage，{}", c);
    }

    @Override
    public void sendChatImageMessage(WxSendChatImageMessageCommand c) {
        LOGGER.warn("测试时企业号不发送图片类型消息：sendChatImageMessage，{}", c);
    }

    @Override
    public void sendChatFileMessage(WxSendChatFileMessageCommand c) {
        LOGGER.warn("测试时企业号不发送文件类型消息：sendChatFileMessage，{}", c);
    }

    @Override
    public void sendChatVoiceMessage(WxSendChatVoiceMessageCommand c) {
        LOGGER.warn("测试时企业号不发送语音类型消息：sendChatVoiceMessage，{}", c);
    }

    @Override
    public void sendChatLinkMessage(WxSendChatLinkMessageCommand c) {
        LOGGER.warn("测试时企业号不发送链接类型消息：sendChatLinkMessage，{}", c);
    }

    @Override
    public WxInvalidUserList updateMute(WxUpdateMuteCommand c) {
        LOGGER.warn("测试时不更改企业号消息提示设置：updateMute，{}", c);
        return null;
    }

}
