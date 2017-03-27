package com.wxjfkg.mybatis.jdbc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Page 分页结果
 * 
 * @author guorui@wxjfkg.com
 */
public class Page<E> implements Serializable {

	private int pageNo = 1;

	private int pageSize;

	private long totalPage = 0;

	private long totalRecord = 0;

	private boolean paging = true;

	private List<E> data;
	
	private boolean lazy = false;
	
	private Map<String,Object> extData;

	public Page() {
	}
	
	public Page(int pageSize, int start) {
		this.setPageSize(pageSize);

		if (0 == pageSize) {
			this.setPaging(false);
		} else {
			int pageNo = (start / pageSize) + 1;
			this.setPageNo(pageNo);
		}
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalPage() {
		if (!getPaging() || pageSize == 0) {
			return 1l;
		}

		if (totalPage == 0) {
			totalPage = totalRecord / pageSize;
			if (totalRecord % pageSize != 0) {
				totalPage = totalPage + 1;
			}
		}
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	public long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}

	public long getTotal() {
		return totalRecord;
	}

	public List<E> getRows() {
		if(data == null) {
			data = new ArrayList<E>();
		}
		return data;
	}

	public void setData(List<E> data) {
		this.data = data;
	}

	public boolean getPaging() {
		return paging;
	}

	public void setPaging(boolean paging) {
		this.paging = paging;
	}

	public boolean isLazy() {
		return lazy;
	}

	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}

	public Map<String, Object> getExtData() {
		return extData;
	}

	public void setExtData(Map<String, Object> extData) {
		this.extData = extData;
	}

}
