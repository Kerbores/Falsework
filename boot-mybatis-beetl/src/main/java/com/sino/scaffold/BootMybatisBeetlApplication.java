package com.sino.scaffold;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.context.request.async.DeferredResult;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 通用mapper手册地址:http://mybatis.tk/
 * 
 * @author kerbores
 *
 */
@SpringBootApplication
@EnableRedisHttpSession
@EnableSwagger2
@MapperScan(basePackages = "com.sino.scaffold.mapper")
public class BootMybatisBeetlApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootMybatisBeetlApplication.class, args);
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).genericModelSubstitutes(DeferredResult.class)
				.useDefaultResponseMessages(false).forCodeGeneration(true).pathMapping("/").select()
				.apis(RequestHandlerSelectors.basePackage("com.sino")).build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("F").description("接口手册")// 详细描述
				.version("1.0")// 版本
				.termsOfServiceUrl("http://www.sinosoft.com.cn")
				.contact(new Contact("王贵源", "http://www.sinosoft.com.cn", "wangguiyuan@sinosoft.com.cn"))// 作者
				.license("The Apache License, Version 2.0")
				.licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html").build();
	}
}
