package com.xgf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author xgf
 * @create 2021-10-31 17:46
 * @description
 **/

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class WebFacadeApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebFacadeApplication.class, args);
    }

}
