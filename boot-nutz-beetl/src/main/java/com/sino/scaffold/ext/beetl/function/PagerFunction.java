package com.sino.scaffold.ext.beetl.function;

import org.beetl.core.Context;
import org.beetl.core.Function;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.plugin.spring.boot.service.entity.PageredData;
import org.springframework.stereotype.Service;

/**
 * @author kerbores
 *
 */
@Service("pager")
public class PagerFunction implements Function {

	Log logger = Logs.get();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beetl.core.Function#call(java.lang.Object[], org.beetl.core.Context)
	 */
	@Override
	public Object call(Object[] paras, Context ctx) {
		if (paras == null || paras.length < 2 || !(paras[0] instanceof PageredData)) {
			logger.debug("参数异常");
			return null;
		}
		PageredData pager = (PageredData) paras[0];
		String url = (String) paras[1];
		return getPagerBar(pager, url);
	}

	public String getPagerBar(PageredData pager, String url) {
		// url = Mvcs.getReq().getAttribute("base") + url;
		String size = "sm";
		int maxLength = 10;
		int page = pager.getPager().getPageNumber();
		int pages = pager.getPager().getPageCount();
		if (pager.getPager().getPageCount() == 0) {
			return "";
		}
		url = url.indexOf('?') > 0 ? url + "page=" : url + "?page=";
		// 处理url结束
		String bar = "<ul class='pagination " + (size == null ? "" : "pagination-" + size) + "'>";
		bar += "<li " + (page <= 1 ? "class='disabled'" : "") + "><a href='" + url + (page - 1) + "'>&laquo;</a></li>";

		if (pages < maxLength) {
			maxLength = pages;
		}

		// 页码小于分页条的一半的时候，从第一页开始显示到barLength页//page 1 barLength 2
		if (page <= maxLength / 2) {
			bar += genPagerBarNode(url, 1, maxLength, page);
		}
		// 页码大于页数减去分页长度的一半的时候 显示 pages - maxLength到pages页
		else if (page >= pages - maxLength / 2) {
			bar += genPagerBarNode(url, pages - maxLength == 0 ? 1 : pages - maxLength, pages, page);
		}
		// 中间情况 显示 page - maxLength/2到page+maxLength/2页
		else {
			bar += genPagerBarNode(url, page - maxLength / 2, page + maxLength / 2, page);
		}
		// 处理结尾
		bar += "<li " + (page == pages ? "class='disabled'" : "") + "><a href='" + url + (page + 1) + "'>&raquo;</a></li>";
		bar += "</ul>";
		return bar;
	}

	private String genPagerBarNode(String url, long start, long end, int page) {
		String target = "";
		for (long i = start; i <= end; i++) {
			target += "<li " + (page == i ? "class='active'" : "") + "><a href='" + url + i + "'>" + i + (page == i ? "<span class='sr-only'>(current)</span>" : "") + "</a></li>";
		}
		return target;
	}

}
