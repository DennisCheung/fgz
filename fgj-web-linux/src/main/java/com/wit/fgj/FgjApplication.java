package com.wit.fgj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.wit.fgj.runtime.EnableFgjRuntime;

@EnableFgjRuntime
public class FgjApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(FgjApplication.class);

    @Autowired ApplicationContext applicationContext;

    public static void main(String[] args) throws Exception {
        LOGGER.info("启动福来平台-微信企业号端-产品模式。");
        SpringApplication.run(FgjApplication.class, args);
    }

}
