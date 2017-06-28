package com.sino.scaffold.biz.base;

import java.util.List;

/**
 * @author kerbores
 *
 */
public class Pager<T> {

	private long page;

	private long pageSize;

	private long total;

	private List<T> data;

	/**
	 * @param page
	 * @param pageSize
	 */
	public Pager(long page, long pageSize) {
		super();
		this.page = page;
		this.pageSize = pageSize;
	}

	/**
	 * @return the page
	 */
	public long getPage() {
		return page;
	}

	/**
	 * @return the pageSize
	 */
	public long getPageSize() {
		return pageSize;
	}

	/**
	 * @return the total
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(long total) {
		this.total = total;
	}

	/**
	 * @return the pages
	 */
	public long getPages() {
		return total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
	}

	/**
	 * @return the data
	 */
	public List<T> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<T> data) {
		this.data = data;
	}

}
