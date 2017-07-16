package com.wit.fgj.runtime.job;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.benewit.core.env.RuntimeContext;
import com.wit.fgj.runtime.ErrorMailProperties;

@Aspect
@Component
public class 作业遇错发邮件拦截器 {

    private static final Logger LOGGER = LoggerFactory.getLogger(作业遇错发邮件拦截器.class);

    private final RuntimeContext runtimeContext;
    private final ErrorMailProperties errorMailProperties;

    public 作业遇错发邮件拦截器(RuntimeContext runtimeContext, ErrorMailProperties errorMailProperties) {
        this.runtimeContext = runtimeContext;
        this.errorMailProperties = errorMailProperties;
    }

    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void hasScheduledAnnotation() {

    }

    @Pointcut("@annotation(org.apache.camel.Handler)")
    public void hasHandlerAnnotation() {

    }

    @Around("hasScheduledAnnotation() || hasHandlerAnnotation()")
    public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!errorMailProperties.isSendMailOnError()) {
            return joinPoint.proceed();
        }
        else {
            Date begin = runtimeContext.getCurrentDate();
            try {
                LOGGER.debug("开始拦截：{}", joinPoint.toString());
                Object ret = joinPoint.proceed();
                LOGGER.debug("结束拦截：{}", joinPoint.toString());
                return ret;
            }
            catch (Throwable e) {
                try {
                    if (!errorMailProperties.ignoreException(e)) {
                        Date end = runtimeContext.getCurrentDate();
                        LOGGER.debug("发生异常，准备发送邮件通知：{}", joinPoint.toString());

                        //发邮件
                        sendExceptionReport(begin, end);
                    }
                }
                catch (Exception e1) {
                    LOGGER.warn("发送邮件失败。", e1);
                }
                throw e;
            }
        }
    }

    private void sendExceptionReport(Date begin, Date end) throws IOException {
        String[] cmd = generateCommand(begin, end);
        Runtime.getRuntime().exec(cmd);
    }

    private String[] generateCommand(Date begin, Date end) {
        String beginString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(begin);
        String endString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(end);

        String sh = System.getProperty("user.home")
                + File.separator + "bin"
                + File.separator + "exrpt-job.sh";

        return new String[] {sh, beginString, endString};
    }
}
