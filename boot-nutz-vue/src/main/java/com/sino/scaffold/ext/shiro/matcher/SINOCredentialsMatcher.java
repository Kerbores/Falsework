package com.sino.scaffold.ext.shiro.matcher;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.nutz.lang.Lang;

/**
 * @author kerbores(kerbores@gmail.com)
 *
 */
public class SINOCredentialsMatcher extends SimpleCredentialsMatcher {

	@Override
	public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		return equals(Lang.digest("MD5", new String(token.getPassword()).getBytes(), token.getUsername().getBytes(), 2), getCredentials(info));
	}
}
