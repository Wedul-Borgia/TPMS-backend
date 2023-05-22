package com.tpms.tpms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author wld
 * @date 2023/5/17 - 20:45
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.tpms"})
public class TpmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(TpmsApplication.class, args);
    }
}
