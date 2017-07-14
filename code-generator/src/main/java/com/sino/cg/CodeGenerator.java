package com.sino.cg;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;

import com.github.abel533.database.DatabaseConfig;
import com.github.abel533.database.Dialect;
import com.github.abel533.database.IntrospectedColumn;
import com.github.abel533.database.IntrospectedTable;
import com.github.abel533.database.SimpleDataSource;
import com.github.abel533.utils.DBMetadataUtils;
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
		project.setGroup("com.sino");
		project.setHome("~/cg/test");
		project.setName("test");
		project.setVersion("1.0");
		DataBase dataBase = new DataBase();
		dataBase.setType(Dialect.MYSQL);
		dataBase.setDbAddress("127.0.0.1");
		dataBase.setPassword("123456");
		dataBase.setPort(3306);
		dataBase.setSchame("spring-thunder");
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
			String clazzName = line2Hump(table.getName(), true);
			List<NutMap> fields = new ArrayList<NutMap>();
			for (IntrospectedColumn column : table.getAllColumns()) {
				fields.add(Lang.map(Json.toJson(column))
						.setv("javaProperty", Strings.lowerFirst(line2Hump(column.getName(), true)))
						.setv("javaName", handleJavaName(column))
						.setv("upperjavaProperty", line2Hump(column.getName(), true)));
			}

			// BEAN
			File modelClass = Files.createFileIfNoExists(String.format("%s%s/%s/%s/%s/%s.java",
					project.getHome(),
					"/src/main/java",
					project.getGroup().replaceAll("\\.", "/"),
					project.getName(),
					"bean",
					clazzName));
			Template modelTemplate = gt.getTemplate("Model.java");
			modelTemplate.binding("project", project);
			modelTemplate.binding("table", Lang.map(Json.toJson(table)).addv("className", clazzName).addv("fields", fields));
			Files.write(modelClass, modelTemplate.render());

			// SERVICE
			File serviceClass = Files.createFileIfNoExists(String.format("%s%s/%s/%s/%s/%sService.java",
					project.getHome(),
					"/src/main/java",
					project.getGroup().replaceAll("\\.", "/"),
					project.getName(),
					"service",
					clazzName));
			Template serviceTamplate = gt.getTemplate("Service.java");
			serviceTamplate.binding("project", project);
			serviceTamplate.binding("table", Lang.map(Json.toJson(table)).addv("className", clazzName).addv("fields", fields));
			Files.write(serviceClass, serviceTamplate.render());

			// Controller
			File controllerClass = Files.createFileIfNoExists(String.format("%s%s/%s/%s/%s/%sController.java",
					project.getHome(),
					"/src/main/java",
					project.getGroup().replaceAll("\\.", "/"),
					project.getName(),
					"controller",
					clazzName));
			Template controllerTemplate = gt.getTemplate("Controller.java");
			controllerTemplate.binding("project", project);
			controllerTemplate.binding("table",
					Lang.map(Json.toJson(table)).addv("className", clazzName).addv("lowerClassName", Strings.lowerFirst(clazzName)).addv("fields", fields));
			Files.write(controllerClass, controllerTemplate.render());
		}
	}

	/**
	 * @param column
	 * @return
	 */
	private static String handleJavaName(IntrospectedColumn column) {
		String name = column.getFullyQualifiedJavaType().getFullyQualifiedName();
		if (Strings.equals(name, "java.lang.Integer")) {
			return "int";
		}
		if (Strings.equals(name, "java.lang.Long")) {
			return "long";
		}
		if (Strings.equals(name, "java.lang.Float")) {
			return "float";
		}
		if (Strings.equals(name, "java.lang.String")) {
			return "String";
		}
		if (Strings.equals(name, "java.math.BigDecimal") || Strings.equals(name, "java.lang.Double")) {
			return "double";
		}
		return name;
	}

	public static String line2Hump(String str, boolean cutFirst) {
		if (cutFirst && str.indexOf("_") > 0) {
			return Strings.line2Hump(str.substring(str.indexOf("_")));
		}
		return Strings.line2Hump(str);
	}

}
