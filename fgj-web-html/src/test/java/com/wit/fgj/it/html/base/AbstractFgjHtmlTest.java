package com.wit.fgj.it.html.base;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;

import com.wit.fxp.it.base.html.AbstractFxpHtmlTest;

@AutoConfigureMockMvc(print = MockMvcPrint.LOG_DEBUG)
public abstract class AbstractFgjHtmlTest extends AbstractFxpHtmlTest {

}
