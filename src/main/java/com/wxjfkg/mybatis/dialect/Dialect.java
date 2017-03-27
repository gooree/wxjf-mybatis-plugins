package com.wxjfkg.mybatis.dialect;

import com.wxjfkg.mybatis.jdbc.Page;

/**
 * Dialect
 * 
 * @author guorui@wxjfkg.com
 */
public interface Dialect {

	/**
	 * 获取分页计数SQL
	 * 
	 * @return
	 */
	public String getCountSql(String sql, Page<?> page);

	/**
	 * 获取分页SQL
	 * 
	 * @param sql
	 * @return
	 */
	public String getPageSql(String sql, Page<?> page);
	
	/**
	 * 获取系统时间
	 * @return
	 */
	public String getSystime();

}
