package com.wit.fgj.runtime.mvc;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.wit.fgj.runtime.ErrorMailProperties;

@ControllerAdvice
@Order(110)
public class WebMvcExceptionHandlers {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebMvcExceptionHandlers.class);

    private final ErrorMailProperties errorMailProperties;

    public WebMvcExceptionHandlers(ErrorMailProperties errorMailProperties) {
        this.errorMailProperties = errorMailProperties;
    }

    @ExceptionHandler(Exception.class)
    public void handleException(Exception ex) throws Exception {
        // 遇错发邮件
        if (errorMailProperties.isSendMailOnError() && !errorMailProperties.ignoreException(ex)) {
            try {
                LOGGER.debug("发生异常，准备发送邮件通知：{}: {}", ex.getClass(), ex.getMessage());
                sendExceptionReport(ex);
            }
            catch (Exception e) {
                LOGGER.warn("发送异常报告邮件失败。", e);
            }
        }

        throw ex;
    }

    private void sendExceptionReport(Throwable t) throws IOException {
        String cmd = generateCommand(t);
        Runtime.getRuntime().exec(cmd);
    }

    private String generateCommand(Throwable t) throws IOException {
        String username = MDC.get("username");
        String times = MDC.get("times");

        String stack; {
            StringWriter sw = null;
            try {
                sw = new StringWriter();
                t.printStackTrace(new PrintWriter(sw));
                stack = sw.toString();
            }
            finally {
                if (sw != null) {
                    try {
                        sw.close();
                    }
                    catch (IOException e) {
                        LOGGER.warn("无法关闭内部资源。", e);
                    }
                }
            }
        }

        String exPathname = System.getProperty("user.home")
                + File.separator + "exrpt"
                + File.separator + username + "-" + times + ".ex";
        FileUtils.write(new File(exPathname), stack, "UTF-8");

        return System.getProperty("user.home")
                + File.separator + "bin"
                + File.separator + "exrpt.sh"
                + " " + username + " " + times;
    }

}
