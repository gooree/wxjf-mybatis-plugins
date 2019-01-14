package com.wxjfkg.mybatis.jdbc;

import java.io.Serializable;

/**
 * Pageable 分页参数
 * 
 * @author guorui@wxjfkg.com
 */
public class Pageable implements Serializable {

	private int start = -1;

	private int length = 0;

	private int page = 1;
	
	private int total = -1;

	private int rows = 10;
	
	private boolean lazy = false;

	private String sort;

	private String order;

	private String orderBy;

	public int getStart() {
		if (start < 0) {
			start = (page - 1) * rows;
		}
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLength() {
		if (length <= 0) {
			length = rows;
		}
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getOrderBy() {
		if (sort != null) {
			orderBy = sort + " " + order;
		}
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return Returns the page.
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page The page to set.
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return Returns the rows.
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @param rows The rows to set.
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * @return Returns the sort.
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * @param sort The sort to set.
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * @return Returns the order.
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * @param order The order to set.
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public boolean isLazy() {
		return lazy;
	}

	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}

}
