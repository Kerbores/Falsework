package com.sino.scaffold;

import org.apache.log4j.Logger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.context.request.async.DeferredResult;

import com.sino.scaffold.bean.InstallPermission;
import com.sino.scaffold.bean.InstalledRole;
import com.sino.scaffold.bean.acl.Permission;
import com.sino.scaffold.bean.acl.Role;
import com.sino.scaffold.bean.acl.RolePermission;
import com.sino.scaffold.bean.acl.User;
import com.sino.scaffold.bean.acl.User.Status;
import com.sino.scaffold.bean.acl.UserRole;
import com.sino.scaffold.ext.shiro.matcher.SINOCredentialsMatcher;
import com.sino.scaffold.service.acl.PermissionService;
import com.sino.scaffold.service.acl.RolePermissionService;
import com.sino.scaffold.service.acl.RoleService;
import com.sino.scaffold.service.acl.UserRoleService;
import com.sino.scaffold.service.acl.UserService;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableRedisHttpSession
@EnableSwagger2
@EnableAsync
public class BootNutzVueApplication {

	public static final String CAPTCHA_KEY = "SINO_CAPTCHA";
	public static final String USER_KEY = "SINO_USER_KEY";
	public static final String USER_COOKIE_KEY = "SINO_USER_COOKIE";

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(BootNutzVueApplication.class);
		application.addListeners(new ApplicationListener<ContextRefreshedEvent>() {
			Logger log = Logger.getLogger(getClass());

			Role admin;

			@Override
			public void onApplicationEvent(ContextRefreshedEvent event) {
				// 这里的逻辑将在应用启动之后执行
				ApplicationContext context = event.getApplicationContext();
				Dao dao = context.getBean(Dao.class);
				if (context.getParent() == null) {
					log.debug("application starter...");
					Daos.createTablesInPackage(dao, "com.sino.scaffold.bean", false);// 确保表结构正确
					Daos.migration(dao, "com.sino.scaffold.bean", true, true);
					initAcl(context);
				}
			}

			private void initAcl(ApplicationContext context) {
				log.debug("init acl...");
				final UserService userService = context.getBean(UserService.class);
				final RoleService roleService = context.getBean(RoleService.class);
				final PermissionService permissionService = context.getBean(PermissionService.class);
				final UserRoleService userRoleService = context.getBean(UserRoleService.class);
				final RolePermissionService rolePermissionService = context.getBean(RolePermissionService.class);

				Lang.each(InstalledRole.values(), new Each<InstalledRole>() {// 内置角色

					@Override
					public void invoke(int index, InstalledRole role, int length) throws ExitLoop, ContinueLoop, LoopException {
						if (roleService.fetch(Cnd.where("name", "=", role.getName())) == null) {
							Role temp = new Role();
							temp.setName(role.getName());
							temp.setDescription(role.getDescription());
							roleService.save(temp);
						}
					}
				});

				admin = roleService.fetch(Cnd.where("name", "=", InstalledRole.SU.getName()));

				if (admin == null) {// 这里理论上是进不来的,防止万一吧
					admin = new Role();
					admin.setName(InstalledRole.SU.getName());
					admin.setDescription(InstalledRole.SU.getDescription());
					admin = roleService.save(admin);
				}

				Lang.each(InstallPermission.values(), new Each<InstallPermission>() {// 内置权限

					@Override
					public void invoke(int index, InstallPermission permission, int length) throws ExitLoop, ContinueLoop, LoopException {
						Permission temp = null;
						if ((temp = permissionService.fetch(Cnd.where("name", "=", permission.getName()))) == null) {
							temp = new Permission();
							temp.setName(permission.getName());
							temp.setDescription(permission.getDescription());
							temp.setInstalled(true);
							temp = permissionService.save(temp);
						}

						// 给SU授权
						if (rolePermissionService.fetch(Cnd.where("permissionId", "=", temp.getId()).and("roleId", "=", admin.getId())) == null) {
							RolePermission rp = new RolePermission();
							rp.setRoleId(admin.getId());
							rp.setPermissionId(temp.getId());
							rolePermissionService.save(rp);
						}
					}
				});

				User surperMan = null;
				if ((surperMan = userService.fetch(Cnd.where("name", "=", "admin"))) == null) {
					surperMan = new User();
					surperMan.setEmail("kerbores@zhcs.club");
					surperMan.setName("admin");
					surperMan.setPassword(SINOCredentialsMatcher.password("admin", "123456"));
					surperMan.setPhone("18996359755");
					surperMan.setRealName("Kerbores");
					surperMan.setNickName("Kerbores");
					surperMan.setStatus(Status.ACTIVED);
					surperMan = userService.save(surperMan);
				}

				UserRole ur = null;
				if ((ur = userRoleService.fetch(Cnd.where("userId", "=", surperMan.getId()).and("roleId", "=", admin.getId()))) == null) {
					ur = new UserRole();
					ur.setUserId(surperMan.getId());
					ur.setRoleId(admin.getId());
					userRoleService.save(ur);
				}
			}

		});
		application.run(args);
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.genericModelSubstitutes(DeferredResult.class)
				.useDefaultResponseMessages(false)
				.forCodeGeneration(true)
				.pathMapping("/")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.sino"))
				.build()
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("F")
				.description("接口手册")// 详细描述
				.version("1.0")// 版本
				.termsOfServiceUrl("http://www.sinosoft.com.cn")
				.contact(new Contact("王贵源", "http://www.sinosoft.com.cn", "wangguiyuan@sinosoft.com.cn"))// 作者
				.license("The Apache License, Version 2.0")
				.licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
				.build();
	}
}
