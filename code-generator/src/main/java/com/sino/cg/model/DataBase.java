package com.sino.cg.model;

import org.nutz.lang.Lang;

import com.sino.cg.meta.Dialect;

/**
 * @author kerbores
 *
 */
public class DataBase {

	private Dialect type;

	private String dbAddress;

	private int port;

	private String schame;

	private String user;

	private String password;

	/**
	 * @return the type
	 */
	public Dialect getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Dialect type) {
		this.type = type;
	}

	/**
	 * @return the dbAddress
	 */
	public String getDbAddress() {
		return dbAddress;
	}

	/**
	 * @param dbAddress
	 *            the dbAddress to set
	 */
	public void setDbAddress(String dbAddress) {
		this.dbAddress = dbAddress;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the schame
	 */
	public String getSchame() {
		return schame;
	}

	/**
	 * @param schame
	 *            the schame to set
	 */
	public void setSchame(String schame) {
		this.schame = schame;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 获取jdbc连接
	 * 
	 * @return
	 */
	public String getJDBCUrl() {
		switch (type) {
		case MYSQL:
			return String.format("jdbc:mysql://%s:%d/%s", dbAddress, port, schame);
		case ORACLE:
			return String.format("jdbc:oracle:thin:@%s:%d:%s", dbAddress, port, schame);
		case SQLSERVER:
			return String.format("jdbc:microsoft:sqlserver://%s:%d;DatabaseName=%s",
					dbAddress,
					port,
					schame);
		case POSTGRESQL:
			return String.format("jdbc:postgresql://%s:%d/%s", dbAddress, port, schame);
		default:
			break;
		}
		throw Lang.makeThrow("db %s not support", type);
	}

}
