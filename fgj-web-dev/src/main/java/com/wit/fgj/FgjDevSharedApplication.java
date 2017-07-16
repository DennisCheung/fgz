package com.wit.fgj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Profile;

import com.wit.fgj.runtime.EnableFgjRuntime;

@Profile("shared")
@EnableFgjRuntime
public class FgjDevSharedApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(FgjDevSharedApplication.class);

    public static void main(String[] args) throws Exception {
        LOGGER.info("启动福来平台-微信企业号端-共享开发模式。");

        // 增加参数
        String[] a = new String[args.length + 1];
        for (int i = 0; i < args.length; i++) {
            a[i] = args[i];
        }
        a[a.length - 1] = "--spring.profiles.active=dev,shared";

        SpringApplication.run(FgjDevSharedApplication.class, a);
    }

}
