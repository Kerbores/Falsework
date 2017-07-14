package ${project.group}.${project.name};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableRedisHttpSession
@EnableSwagger2
@EnableAsync
public class ${project.upperedName}Application {

	public static void main(String[] args) {
		SpringApplication.run(${project.upperedName}Application.class, args);
	}
}
