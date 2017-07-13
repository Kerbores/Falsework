package com.sino.scaffold.ext.pdf;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.lang.Strings;
import org.springframework.web.servlet.view.document.AbstractPdfStamperView;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfStamper;

/**
 * @author kerbores
 *
 */
public class PdfStamperView extends AbstractPdfStamperView {

	/**
	 * 下载的文件名称
	 */
	String fileName;

	/**
	 * pdf 模板位置
	 */
	String templateLocation;

	/**
	 * @param fileName
	 * @param templateLocation
	 */
	public PdfStamperView(String fileName, String templateLocation) {
		super();
		this.fileName = fileName;
		this.templateLocation = templateLocation;
		setUrl(templateLocation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.view.document.AbstractPdfStamperView#
	 * mergePdfDocument(java.util.Map, com.lowagie.text.pdf.PdfStamper,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void mergePdfDocument(Map<String, Object> model, PdfStamper stamper, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
		AcroFields fields = stamper.getAcroFields();
		fillData(fields, model);
		stamper.setFormFlattening(true);
	}

	/**
	 * @param fields
	 * @param model
	 * @throws DocumentException
	 * @throws IOException
	 */
	private void fillData(AcroFields fields, Map<String, Object> model) throws IOException, DocumentException {
		for (String key : model.keySet()) {
			fields.setField(key, Strings.safeToString(model.get(key), ""));
		}
	}

}
