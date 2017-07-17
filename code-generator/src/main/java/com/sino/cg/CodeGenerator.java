package com.sino.cg;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.nutz.lang.Files;
import org.nutz.lang.Stopwatch;
import org.nutz.lang.Strings;
import org.nutz.log.Logs;

import com.sino.cg.meta.DatabaseConfig;
import com.sino.cg.meta.Dialect;
import com.sino.cg.meta.IntrospectedTable;
import com.sino.cg.meta.SimpleDataSource;
import com.sino.cg.meta.utils.DBMetadataUtils;
import com.sino.cg.model.DataBase;
import com.sino.cg.model.Project;

/**
 * @author kerbores
 *
 */
public class CodeGenerator {

	static GroupTemplate gt;

	public static void main(String[] args) throws IOException, SQLException {

		Project project = new Project();
		project.setDescription("测试一下");
		project.setGroup("com.sino.cg");
		project.setHome("~/cc/sso");
		project.setName("sso");
		project.setVersion("1.0");
		DataBase dataBase = new DataBase();
		dataBase.setType(Dialect.MYSQL);
		dataBase.setDbAddress("127.0.0.1");
		dataBase.setPassword("123456");
		dataBase.setPort(3306);
		dataBase.setSchame("test");
		dataBase.setUser("root");
		project.setDataBase(dataBase);
		loadTeamplate();
		gen(project);
	}

	/**
	 * @throws IOException
	 * 
	 */
	private static void loadTeamplate() throws IOException {
		ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("");
		Configuration cfg = Configuration.defaultConfiguration();
		gt = new GroupTemplate(resourceLoader, cfg);
	}

	public static void gen(Project project) throws IOException, SQLException {
		Stopwatch stopwatch = Stopwatch.begin();
		Files.clearDir(Files.createDirIfNoExists(project.getHome()));// 清空
		File pom = Files.createFileIfNoExists(String.format("%s%s", project.getHome(), "/pom.xml"));

		// POM 文件
		Template pomTemplate = gt.getTemplate("pom.template");
		pomTemplate.binding("project", project);
		Files.write(pom, pomTemplate.render());

		// 启动文件

		File mainClass = Files.createFileIfNoExists(String.format("%s%s/%s/%s/%sApplication.java",
				project.getHome(),
				"/src/main/java",
				project.getGroup().replaceAll("\\.", "/"),
				project.getName(),
				Strings.upperFirst(project.getName())));

		Template applicationTemplate = gt.getTemplate("Application.java");
		applicationTemplate.binding("project", project);
		Files.write(mainClass, applicationTemplate.render());

		// YML 配置
		File ymlConfig = Files.createFileIfNoExists((String.format("%s%s", project.getHome(), "/src/main/resources/application.yml")));

		Template ymlTemplate = gt.getTemplate("application.yml.template");
		ymlTemplate.binding("project", project);
		Files.write(ymlConfig, ymlTemplate.render());

		// static 目录
		Files.createDirIfNoExists(String.format("%s%s", project.getHome(), "/src/main/resources/static"));
		// templates 目录
		Files.createDirIfNoExists(String.format("%s%s", project.getHome(), "/src/main/resources/templates"));
		// sqls 目录
		Files.createDirIfNoExists(String.format("%s%s", project.getHome(), "/src/main/resources/sqls"));

		// basecontroller
		File baseController = Files.createFileIfNoExists(String.format("%s%s/%s/%s/%s/BaseController.java",
				project.getHome(),
				"/src/main/java",
				project.getGroup().replaceAll("\\.", "/"),
				project.getName(),
				"controller/base"));

		Template baseControllerTamplate = gt.getTemplate("BaseController.java");
		baseControllerTamplate.binding("project", project);
		Files.write(baseController, baseControllerTamplate.render());

		// Result
		File resultClass = Files.createFileIfNoExists(String.format("%s%s/%s/%s/%s/Result.java",
				project.getHome(),
				"/src/main/java",
				project.getGroup().replaceAll("\\.", "/"),
				project.getName(),
				"utils"));

		Template resultTemplate = gt.getTemplate("Result.java");
		resultTemplate.binding("project", project);
		Files.write(resultClass, resultTemplate.render());

		// 数据库
		SimpleDataSource dataSource = new SimpleDataSource(
				project.getDataBase().getType(),
				project.getDataBase().getJDBCUrl(),
				project.getDataBase().getUser(),
				project.getDataBase().getPassword());

		DBMetadataUtils dbMetadataUtils = new DBMetadataUtils(dataSource);
		DatabaseConfig config = new DatabaseConfig();
		List<IntrospectedTable> list = dbMetadataUtils.introspectTables(config);
		for (IntrospectedTable table : list) {
			Logs.get().debug("正在生成表 '" + table.getName() + "' 相关代码");
			// BEAN
			File modelClass = Files.createFileIfNoExists(String.format("%s%s/%s/%s/%s/%s.java",
					project.getHome(),
					"/src/main/java",
					project.getGroup().replaceAll("\\.", "/"),
					project.getName(),
					"bean",
					table.getJavaClassName()));
			Template modelTemplate = gt.getTemplate("Model.java");
			modelTemplate.binding("project", project);
			modelTemplate.binding("table", table);
			Logs.get().debug("正在写入实体文件--> " + table.getJavaClassName() + ".java...");
			Files.write(modelClass, modelTemplate.render());

			// SERVICE
			File serviceClass = Files.createFileIfNoExists(String.format("%s%s/%s/%s/%s/%sService.java",
					project.getHome(),
					"/src/main/java",
					project.getGroup().replaceAll("\\.", "/"),
					project.getName(),
					"service",
					table.getJavaClassName()));
			Template serviceTamplate = gt.getTemplate("Service.java");
			serviceTamplate.binding("project", project);
			serviceTamplate.binding("table", table);
			Logs.get().debug("正在写入业务文件--> " + table.getJavaClassName() + "Service.java...");
			Files.write(serviceClass, serviceTamplate.render());

			// Controller
			File controllerClass = Files.createFileIfNoExists(String.format("%s%s/%s/%s/%s/%sController.java",
					project.getHome(),
					"/src/main/java",
					project.getGroup().replaceAll("\\.", "/"),
					project.getName(),
					"controller",
					table.getJavaClassName()));
			Template controllerTemplate = gt.getTemplate("Controller.java");
			controllerTemplate.binding("project", project);
			controllerTemplate.binding("table", table);
			Logs.get().debug("正在写入控制器文件--> " + table.getJavaClassName() + "Controller.java...");
			Files.write(controllerClass, controllerTemplate.render());
			Logs.get().debug("表 '" + table.getName() + "' 相关代码生成完毕");
			Logs.get().debug(Strings.dup("---------", 10));
		}
		stopwatch.stop();
		Logs.get().debug("代码生成成功!源代码已保存至: " + Files.createDirIfNoExists(project.getHome()).getAbsolutePath() + " 耗时: " + stopwatch.getDuration() + " 毫秒");
	}

}
