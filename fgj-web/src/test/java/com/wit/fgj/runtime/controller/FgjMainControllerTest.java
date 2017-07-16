package com.wit.fgj.runtime.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.wit.fgj.runtime.db.DatabaseChecker;
import com.wit.fxp.it.base.controller.AbstractFxpControllerTest;

public class FgjMainControllerTest extends AbstractFxpControllerTest {

    @MockBean DatabaseChecker databaseChecker;
    @SpyBean FgjMainController cut;

    @Test
    public void ping() throws Exception {
        mockMvc.perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }

}
