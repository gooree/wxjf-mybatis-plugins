package com.wxjfkg.mybatis.plugins;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;

/**
 * BoundSqlSqlSource
 * 
 * @author guorui@wxjfkg.com
 */
public class BoundSqlSqlSource implements SqlSource {
	
	private BoundSql boundSql;

	public BoundSqlSqlSource(BoundSql boundSql) {
		this.boundSql = boundSql;
	}

	public BoundSql getBoundSql(Object parameterObject) {
		return boundSql;
	}
	
}
