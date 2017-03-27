package com.wxjfkg.mybatis.plugins;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxjfkg.mybatis.exception.DataAccessForbiddenException;

/**
 * PerformanceInterceptor-MyBatis性能调试插件
 * 
 * @author GuoRui
 *
 */
@Intercepts({
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class }),
		@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class PerformanceInterceptor implements Interceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(PerformanceInterceptor.class);

	/**
	 * SQL执行最大时长，超过该时间输出告警日志。
	 */
	private long timeoutMillis = 0;

	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object parameterObject = null;
		if (invocation.getArgs().length > 1) {
			parameterObject = invocation.getArgs()[1];
		}

		String statementId = mappedStatement.getId();
		BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
		
		String newSql = sql.toLowerCase();
		if (StringUtils.contains(newSql, "select")) {
			if(!StringUtils.contains(newSql, "where")) {
				logger.warn("未指定查询条件。sql statement:{}", newSql);
			}
		} else if (StringUtils.contains(newSql, "update")
				|| StringUtils.contains(newSql, "delete")) {
			if (!StringUtils.contains(newSql, "where")) {
				throw new DataAccessForbiddenException(
						"不允许对全表进行更新或删除操作。sql statement:" + newSql);
			}
		}

		long beginTimeMillis = System.currentTimeMillis();
		Object result = invocation.proceed();
		long costTimeMillis = System.currentTimeMillis() - beginTimeMillis;
		logger.debug("sql statement [{}] cost {} millis.", statementId, costTimeMillis);
		if (timeoutMillis > 0 && timeoutMillis < costTimeMillis) {
			logger.warn("sql statement {} timeout.", statementId);
			logger.warn("sql: {}", sql);
		}
		
		return result;
	}

	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	public void setProperties(Properties prop) {
		// ignore
	}

	public long getTimeoutMillis() {
		return timeoutMillis;
	}

	public void setTimeoutMillis(long timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
	}

}
