package com.sino.scaffold.captcha;

/**
 * @author kerbores
 *
 */
public interface CaptchaGener {
	/**
	 * 生成指定长度验证码
	 * 
	 * @param length
	 *            长度
	 * @return 验证码
	 */
	String gen(int length);
}
