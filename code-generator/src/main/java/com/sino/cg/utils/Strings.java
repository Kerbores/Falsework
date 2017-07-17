package com.sino.cg.utils;

/**
 * @author kerbores
 *
 */
public class Strings extends org.nutz.lang.Strings {

	public static String line2Hump(String str, boolean smartCut) {
		if (isBlank(str)) {
			return str;
		}
		if (smartCut && str.split("_").length > 1 && str.split("_")[0].length() < 4) {// 2段以上,第一段字符少于3个
			return line2Hump(str.substring(str.indexOf("_")));
		}
		return line2Hump(str);
	}
}
