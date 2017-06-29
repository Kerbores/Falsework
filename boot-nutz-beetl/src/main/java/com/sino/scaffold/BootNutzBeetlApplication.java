package com.sino.scaffold;

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
 * nutz深度集成,包含nutz-dao作为orm层,nutz-json作为json支持,以及你可以方便的时候nutz提供的各种工具类,手册地址:http://nutzam.com/core/nutz_preface.html,问题咨询论坛https://nutz.cn
 * 
 * @author kerbores
 *
 */
@SpringBootApplication
@EnableRedisHttpSession
@EnableSwagger2
public class BootNutzBeetlApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootNutzBeetlApplication.class, args);
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
