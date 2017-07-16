package com.wit.fgj.runtime;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

import com.wit.fxp.domain.EnableFxpDomain;
import com.wit.hec.client.EnableHecClient;
import com.wit.qix.client.EnableQic;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited

@Order(0)
@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableConfigurationProperties
@EnableQic
@EnableFxpDomain
@EnableHecClient
@Import(FgjRuntimeConfiguration.class)
public @interface EnableFgjRuntime {

}
