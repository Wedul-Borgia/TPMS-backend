package com.tpms.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wld
 * @date 2023/5/16 - 14:54
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.tpms"})
public class BaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);
    }
}
