package com.sino.scaffold;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
@MapperScan(basePackages = "com.sino.scaffold.mapper")
public class BootMybatisBeetlApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootMybatisBeetlApplication.class, args);
	}
}
