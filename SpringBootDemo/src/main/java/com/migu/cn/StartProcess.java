package com.migu.cn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("mapper")
public class StartProcess  {
    public static void main(String[] args) {
        SpringApplication.run(StartProcess.class,args);
    }
}
