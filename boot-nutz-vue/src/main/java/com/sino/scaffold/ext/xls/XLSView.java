package com.sino.scaffold.ext.xls;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;

/**
 * @author kerbores
 *
 */
public abstract class XLSView extends AbstractXlsxStreamingView {

	/**
	 * 下载的文件名称
	 */
	String fileName;

	/**
	 * @param fileName
	 */
	public XLSView(String fileName) {
		super();
		this.fileName = fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.view.document.AbstractXlsView#
	 * buildExcelDocument(java.util.Map, org.apache.poi.ss.usermodel.Workbook,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
		merge(model, workbook);
	}

	/**
	 * 我不知道你应该怎么去填充 excel 表格<br>
	 * 如果有这样的需求建议使用http://easypoi.mydoc.io/进行 excel 的操作
	 * 
	 * @param model
	 *            模型
	 * @param workbook
	 *            excel 的 workbook
	 * @throws Exception
	 */
	protected abstract void merge(Map<String, Object> model, Workbook workbook) throws Exception;

}
