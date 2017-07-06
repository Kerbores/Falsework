package com.sino.scaffold.bean.log;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.lang.Times;

import com.sino.scaffold.bean.Entity;

/**
 * 
 * @author kerbores@gmail.com
 *
 */
@Table("t_user_login")
public class LoginLog extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column("login_user_id")
	@Comment("登录用户 id")
	private long userId;

	@Column("login_account")
	@Comment("登录账户")
	private String account;

	@Column("login_time")
	@Comment("登录时间")
	private Date loginTime = Times.now();

	@Column("login_ip")
	@Comment("登录 ip")
	private String ip;

	@Column("login_status")
	@Comment("登录成功与否")
	private boolean success;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getAccount() {
		return account;
	}

	public String getIp() {
		return ip;
	}

	public long getUserId() {
		return userId;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the loginTime
	 */
	public Date getLoginTime() {
		return loginTime;
	}

	/**
	 * @param loginTime
	 *            the loginTime to set
	 */
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}
