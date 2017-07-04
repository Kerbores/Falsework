package com.sino.scaffold.dto;

/**
 * @author kerbores
 *
 */
public class GrantDTO {
	private int id;

	private int[] grantIds;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	public int[] getGrantIds() {
		return grantIds;
	}

	public void setGrantIds(int[] grantIds) {
		this.grantIds = grantIds;
	}
}
