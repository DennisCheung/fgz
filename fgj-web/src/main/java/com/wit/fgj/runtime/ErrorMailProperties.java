package com.wit.fgj.runtime;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ConfigurationProperties(prefix = "fgj")
@Configuration
public class ErrorMailProperties {

    private boolean sendMailOnError = false;

    private ExceptionDef[] ignoredExceptions;

    /**
     * 是否忽略指定的异常。
     *
     * @param ex
     * @return
     */
    public boolean ignoreException(Throwable ex) {
        if (ignoredExceptions != null) {
            for (ExceptionDef def : ignoredExceptions) {
                if (def.matches(ex)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExceptionDef {
        private Class<? extends Exception> exceptionClass;
        private String exceptionMessage;

        public boolean matches(Throwable ex) {
            if (exceptionClass != ex.getClass()) {
                return false;
            }
            else {
                if (exceptionMessage == null) {
                    return true;
                }
                else {
                    return ex.getMessage() != null && ex.getMessage().contains(exceptionMessage);
                }
            }
        }
    }

}
