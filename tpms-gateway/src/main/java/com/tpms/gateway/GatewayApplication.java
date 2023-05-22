package com.tpms.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wld
 * @date 2023/5/18 - 0:38
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.tpms"})
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
