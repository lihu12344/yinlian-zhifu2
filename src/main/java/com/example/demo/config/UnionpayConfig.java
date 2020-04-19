package com.example.demo.config;

import com.unionpay.acp.sdk.SDKConfig;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnionpayConfig implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        SDKConfig.getConfig().loadPropertiesFromSrc();
    }
}
