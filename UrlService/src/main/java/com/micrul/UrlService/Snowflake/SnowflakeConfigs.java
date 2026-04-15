package com.micrul.UrlService.Snowflake;


import com.microp.snowflake.Snowflake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SnowflakeConfigs {

    @Bean
    public Snowflake snowflakeConfig() {
        long datacenterId = IdGeneratorHelper.getDatacenterId();
        long workerId = IdGeneratorHelper.getWorkerId();

        log.info("[SnowflakeConfig] Using datacenterId={}, workerId={}", datacenterId, workerId);

        return new Snowflake(workerId, datacenterId);
    }
}
