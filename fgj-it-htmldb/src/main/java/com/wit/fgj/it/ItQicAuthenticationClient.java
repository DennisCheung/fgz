package com.wit.fgj.it;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wit.qix.prot.auth.AuthToken;
import com.wit.qix.prot.auth.AuthenticationService;

/**
 * 集成测试时模拟实现认证服务。
 *
 * @author yw
 *
 */
public class ItQicAuthenticationClient implements AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItQicAuthenticationClient.class);

    private static final String PREFIX = "AUTH-TOKEN-";

    @Override
    public String findKey(AuthToken authToken) {
        LOGGER.warn("测试时模拟认证");
        String token = authToken.getToken();
        if (!token.startsWith(PREFIX)) {
            throw new IllegalArgumentException("模拟的AuthToken字符串须以" + PREFIX + "开头");
        }
        return token.substring(PREFIX.length());
    }

}
