package com.wit.fgj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Profile;

import com.wit.fgj.runtime.EnableFgjRuntime;

@Profile("indep")
@EnableFgjRuntime
public class FgjDevApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(FgjDevApplication.class);

    public static void main(String[] args) throws Exception {
        LOGGER.info("启动福来平台-微信企业号端-独立开发模式。");

        // 增加参数
        String[] a = new String[args.length + 1];
        for (int i = 0; i < args.length; i++) {
            a[i] = args[i];
        }
        a[a.length - 1] = "--spring.profiles.active=dev,indep";

        SpringApplication.run(FgjDevApplication.class, a);
    }

}
