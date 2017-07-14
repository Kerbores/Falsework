package com.sino.cg.model;

import org.nutz.lang.Strings;

/**
 * @author kerbores
 *
 */
public class Project {

	private String home;

	private String name;

	private String group;

	private String version;

	private String description;

	DataBase dataBase;

	/**
	 * @return the dataBase
	 */
	public DataBase getDataBase() {
		return dataBase;
	}

	/**
	 * @param dataBase
	 *            the dataBase to set
	 */
	public void setDataBase(DataBase dataBase) {
		this.dataBase = dataBase;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	public String getUpperedName() {
		return Strings.upperFirst(name);
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @return the home
	 */
	public String getHome() {
		return home;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @param home
	 *            the home to set
	 */
	public void setHome(String home) {
		this.home = home;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

}
