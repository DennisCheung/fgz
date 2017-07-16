package com.wit.fgj.properties;

import javax.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "fgj.roster.department")
@Data
public class RosterDepartConfigure {

    /** 游客部门 */
    @Min(1)
    private int guest;

    /** 散客部门 */
    @Min(1)
    private int individual;

    /** 服务商部门 */
    @Min(1)
    private int supplier;

}
