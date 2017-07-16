package com.wit.fgj.it;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wit.annotations.BeanOverride;
import com.wit.qix.client.auth.QicAuthenticationClient;
import com.wit.qix.client.chat.QicChatClient;
import com.wit.qix.client.roster.QicRosterClient;
import com.wit.qix.client.support.config.QicClientConfiguration;
import com.wit.qix.prot.auth.AuthenticationService;
import com.wit.qix.prot.chat.WxChatService;
import com.wit.qix.prot.roster.WxRosterService;

/**
 * 配置模拟{@link QicClient}，使得可重复测试。
 *
 * <p>经本配置后，测试时将自动用户登录，不更新企业号通讯录。
 *
 * @author yw
 *
 */
@Configuration
public class ItQicClientConfiguration {

    @Autowired QicClientConfiguration qicClientConfiguration;

    @Bean
    @BeanOverride(QicAuthenticationClient.BEAN_NAME)
    public AuthenticationService itQicAuthenticationClient() {
        return new ItQicAuthenticationClient();
    }

    @Bean
    @BeanOverride(QicRosterClient.BEAN_NAME)
    public WxRosterService itQicRosterClient() {
        QicRosterClient delegate = new QicRosterClient(
                qicClientConfiguration.getQisServer(), qicClientConfiguration.qicHttpTemplate());
        return new ItQicRosterClient(delegate);
    }

    @Bean
    @BeanOverride(QicChatClient.BEAN_NAME)
    public WxChatService itQicChatClient() {
        QicChatClient delegate = new QicChatClient(
                qicClientConfiguration.getQisServer(), qicClientConfiguration.qicHttpTemplate());
        return new ItQicChatClient(delegate);
    }

}
