package com.dingsai.dingsaibackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zzs
 */
@MapperScan(value = {"com.dingsai.dingsaibackend.mapper"})
@SpringBootApplication
public class DingsaiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DingsaiBackendApplication.class, args);
    }

}
