package com.wit.fgj.runtime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wit.fgj.runtime.db.DatabaseChecker;

/**
 * 主控制器。
 *
 * @author yw
 *
 */
@RestController
public class FgjMainController {

    @Autowired private DatabaseChecker databaseChecker;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/public/sim-error")
    public void simError() {
        throw new RuntimeException("这是用于测试的模拟异常！");
    }

    @GetMapping("/public/check-database")
    public void checkDatabase() {
        databaseChecker.checkDatabase();
    }

}
