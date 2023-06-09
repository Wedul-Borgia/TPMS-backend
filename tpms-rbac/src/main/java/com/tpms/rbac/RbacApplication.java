package com.tpms.rbac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wld
 * @date 2023/5/3 - 20:19
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.tpms"})
public class RbacApplication {
    public static void main(String[] args) {
        SpringApplication.run(RbacApplication.class, args);
    }
}
