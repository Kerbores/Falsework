package com.sino.cg.meta;

import java.util.List;

/**
 * 内省数据库时的处理接口
 */
public abstract class AbstractDatabaseProcess implements DatabaseProcess {

	public void processStart() {

	}

	public void processColumn(IntrospectedTable table, IntrospectedColumn column) {

	}

	public void processTable(IntrospectedTable table) {

	}

	public void processComplete(List<IntrospectedTable> introspectedTables) {

	}
}
