package com.wxjfkg.mybatis.plugins;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxjfkg.mybatis.dialect.Dialect;
import com.wxjfkg.mybatis.dialect.DialectFactory;
import com.wxjfkg.mybatis.jdbc.Page;
import com.wxjfkg.mybatis.jdbc.Pagination;
import com.wxjfkg.mybatis.utils.JdbcUtils;

/**
 * PaginationInterceptor-MyBatis分页插件
 * 
 * @author GuoRui
 *
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = {
	MappedStatement.class, Object.class, RowBounds.class,
	ResultHandler.class }) })
public class PaginationInterceptor implements Interceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(PaginationInterceptor.class);
	
	private String dialect;
	
	private static final String DEFAULT_DIALECT = "mysql";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		final MappedStatement mappedStatement = (MappedStatement) invocation
				.getArgs()[0];

		Object parameter = invocation.getArgs()[1];
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);

		if (StringUtils.isNotBlank(boundSql.getSql())
				&& parameter instanceof Pagination) {
			Pagination paging = (Pagination) parameter;
			Page page = new Page(paging.getLength(), paging.getStart());
			page.setLazy(paging.isLazy());
			page.setTotalRecord(paging.getTotal());

			Dialect dialectToUse = null;
			Dialect defaultDialect = DialectFactory.getDialect(DEFAULT_DIALECT);
			if (dialect == null) {
				logger.warn("Dialect is null, default dialect will be used.");
				dialectToUse = defaultDialect;
			} else {
				dialectToUse = DialectFactory.getDialect(dialect);
				if(dialectToUse == null) {
					logger.warn("dialect {} can't be found!", dialect);
					dialectToUse = defaultDialect;
				}
			}

			/*
			 * 1.获取分页记录总数
			 */
			String countSql = dialectToUse.getCountSql(boundSql.getSql(), page);

			if (page.isLazy() && page.getTotal() > 0) {
				page.setTotalRecord(page.getTotal());
			} else {
				Environment environment = mappedStatement.getConfiguration()
						.getEnvironment();
				DataSource dataSource = environment.getDataSource();

				int totalRecord = queryTotalRecord(countSql, dataSource,
						mappedStatement, boundSql);
				page.setTotalRecord(totalRecord);
			}

			/*
			 * 2.改写分页SQL
			 */
			String pageSql = dialectToUse.getPageSql(boundSql.getSql(), page);

			BoundSql newBoundSql = new BoundSql(
					mappedStatement.getConfiguration(), pageSql,
					boundSql.getParameterMappings(),
					boundSql.getParameterObject());
			MappedStatement newStatement = newMappedStatement(mappedStatement,
					new BoundSqlSqlSource(newBoundSql));

			invocation.getArgs()[0] = newStatement;
			invocation.getArgs()[2] = new RowBounds(RowBounds.NO_ROW_OFFSET,
					RowBounds.NO_ROW_LIMIT);

			Object data = invocation.proceed();
			page.setData((List) data);

			List<Page> result = new ArrayList<Page>();
			result.add(page);
			return result;
		} else {
			return invocation.proceed();
		}
	}
	
	private int queryTotalRecord(String sql, DataSource dataSource,
			MappedStatement mappedStatement, BoundSql boundSql)
			throws SQLException {
		int total = 0;

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			stmt = connection.prepareStatement(sql);
			BoundSql newBoundSql = new BoundSql(
					mappedStatement.getConfiguration(), sql,
					boundSql.getParameterMappings(),
					boundSql.getParameterObject());
			ParameterHandler parameterHandler = new DefaultParameterHandler(
					mappedStatement, boundSql.getParameterObject(), newBoundSql);
			parameterHandler.setParameters(stmt);
			rs = stmt.executeQuery();
			if (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException ex) {
			logger.error("Execute SQL query error.", ex);
			throw ex;
		} finally {
			JdbcUtils.closeQuietly(rs);
			JdbcUtils.closeQuietly(stmt);
			JdbcUtils.closeQuietly(connection);
		}

		return total;
	}
	
	private MappedStatement newMappedStatement(MappedStatement ms,
			SqlSource newSqlSource) {
		MappedStatement.Builder builder = new MappedStatement.Builder(
				ms.getConfiguration(), ms.getId(), newSqlSource,
				ms.getSqlCommandType());
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		if (ms.getKeyProperties() != null) {
			for (String keyProperty : ms.getKeyProperties()) {
				builder.keyProperty(keyProperty);
			}
		}
		builder.timeout(ms.getTimeout());
		builder.parameterMap(ms.getParameterMap());
		builder.resultMaps(ms.getResultMaps());
		builder.cache(ms.getCache());
		return builder.build();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
		String dialect = properties.getProperty("dialect");
		if (dialect != null) {
			this.dialect = dialect;
		}
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

}
