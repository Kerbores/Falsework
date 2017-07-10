package com.sino.scaffold.config.wechat;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author kerbores
 *
 */
@ConfigurationProperties(prefix = "wechat")
public class WechatConfigProperties {

	protected String token;
	protected String appid;
	protected String appsecret;
	protected String encodingAesKey;

	protected boolean redisCacheEnable = false;

	/**
	 * @return the redisCacheEnable
	 */
	public boolean isRedisCacheEnable() {
		return redisCacheEnable;
	}

	/**
	 * @param redisCacheEnable
	 *            the redisCacheEnable to set
	 */
	public void setRedisCacheEnable(boolean redisCacheEnable) {
		this.redisCacheEnable = redisCacheEnable;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the appid
	 */
	public String getAppid() {
		return appid;
	}

	/**
	 * @param appid
	 *            the appid to set
	 */
	public void setAppid(String appid) {
		this.appid = appid;
	}

	/**
	 * @return the appsecret
	 */
	public String getAppsecret() {
		return appsecret;
	}

	/**
	 * @param appsecret
	 *            the appsecret to set
	 */
	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	/**
	 * @return the encodingAesKey
	 */
	public String getEncodingAesKey() {
		return encodingAesKey;
	}

	/**
	 * @param encodingAesKey
	 *            the encodingAesKey to set
	 */
	public void setEncodingAesKey(String encodingAesKey) {
		this.encodingAesKey = encodingAesKey;
	}

}
