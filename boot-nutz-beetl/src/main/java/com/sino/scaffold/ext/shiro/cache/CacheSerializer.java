package com.sino.scaffold.ext.shiro.cache;

/**
 * @author kerbores
 *
 */
public interface CacheSerializer {

	/**
	 * 如果对象无法序列化,返回null
	 */
	Object fromObject(Object obj);

	/**
	 * 要求: 如果对象无法还原,返回null
	 */
	Object toObject(Object obj);

}
