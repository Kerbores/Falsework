package com.sino.cg.meta;

import com.sino.cg.utils.Strings;

public class IntrospectedBase {
	protected String name;
	protected String remarks;

	/**
	 * 根据条件进行过滤
	 *
	 * @param searchText
	 * @param searchComment
	 * @param matchType
	 * @param caseSensitive
	 * @return
	 */
	public boolean filter(String searchText, String searchComment, MatchType matchType, boolean caseSensitive) {
		if (Strings.isNotBlank(searchText)) {
			if (matchType == MatchType.EQUALS) {
				if (caseSensitive) {
					if (!getName().equals(searchText)) {
						return false;
					}
				} else {
					if (!getName().equalsIgnoreCase(searchText)) {
						return false;
					}
				}
			} else {
				if (caseSensitive) {
					if (getName().indexOf(searchText) == -1) {
						return false;
					}
				} else {
					if (getName().toUpperCase().indexOf(searchText.toUpperCase()) == -1) {
						return false;
					}
				}
			}
		}
		if (Strings.isNotBlank(searchComment)) {
			if (matchType == MatchType.EQUALS) {
				if (caseSensitive) {
					if (getRemarks() == null || !getRemarks().equals(searchComment)) {
						return false;
					}
				} else {
					if (getRemarks() == null || !getRemarks().equalsIgnoreCase(searchComment)) {
						return false;
					}
				}
			} else {
				if (caseSensitive) {
					if (getRemarks() == null || getRemarks().indexOf(searchComment) == -1) {
						return false;
					}
				} else {
					if (getRemarks() == null || getRemarks().indexOf(searchComment) == -1) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
