package com.sino.scaffold.ext.shiro.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.shiro.authz.annotation.Logical;

import com.sino.scaffold.bean.InstallPermission;

/**
 * 
 * @author kerbores
 *
 * @email kerbores@gmail.com
 *
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SINORequiresPermissions {
	Logical logical() default Logical.AND;

	InstallPermission[] value();
}