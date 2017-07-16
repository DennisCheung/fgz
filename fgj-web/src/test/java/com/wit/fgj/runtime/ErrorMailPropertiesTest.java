package com.wit.fgj.runtime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ErrorMailPropertiesTest {

    @Test
    public void test() {
        ErrorMailProperties properties = new ErrorMailProperties();
        properties.setIgnoredExceptions(new ErrorMailProperties.ExceptionDef[] {
                new ErrorMailProperties.ExceptionDef(IllegalArgumentException.class, "无效或已过期的认证凭证")
        });

        assertTrue("异常类和异常信息都匹配时应返回true", properties.ignoreException(new IllegalArgumentException("无效或已过期的认证凭证")));
        assertFalse("异常类不匹配时应返回false", properties.ignoreException(new RuntimeException("无效或已过期的认证凭证")));
    }

}
