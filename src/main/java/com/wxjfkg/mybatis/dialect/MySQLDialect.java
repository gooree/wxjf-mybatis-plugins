package com.wxjfkg.mybatis.dialect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxjfkg.mybatis.jdbc.Page;

/**
 * Page 分页结果
 * 
 * @author guorui@wxjfkg.com
 */
public class MySQLDialect implements Dialect {

	private static final Logger logger = LoggerFactory.getLogger(MySQLDialect.class);

	@Override
	public String getCountSql(String sql, Page<?> page) {
		String countSql = "select count(0) from (" + sql + ") t";
		logger.debug("count sql: [{}]", countSql);
		return countSql;
	}

	@Override
	public String getPageSql(String sql, Page<?> page) {
		StringBuilder pageSql = new StringBuilder(sql);
		String beginrow = String.valueOf((page.getPageNo() - 1)
				* page.getPageSize());
		pageSql.append(" limit ").append(beginrow).append(",").append(page.getPageSize());
		logger.debug("paging sql: [{}]", pageSql.toString());
		return pageSql.toString();
	}

	@Override
	public String getSystime() {
		return "now()";
	}

}
