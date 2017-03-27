package com.wxjfkg.mybatis.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JdbcUtils
 * 
 * @author guorui@wxjfkg.com
 */
public class JdbcUtils {

	private static final Logger logger = LoggerFactory.getLogger(JdbcUtils.class);

	public static void closeQuietly(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException sqlEx) {
				logger.error("sql state: [{}]", sqlEx.getSQLState());
				logger.error("error code: [{}]", sqlEx.getErrorCode());
				logger.error(sqlEx.getMessage(), sqlEx);
			}
		}
	}

	public static void closeQuietly(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException sqlEx) {
				logger.error("sql state: [{}]", sqlEx.getSQLState());
				logger.error("error code: [{}]", sqlEx.getErrorCode());
				logger.error(sqlEx.getMessage(), sqlEx);
			}
		}
	}

	public static void closeQuietly(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException sqlEx) {
				logger.error("sql state: [{}]", sqlEx.getSQLState());
				logger.error("error code: [{}]", sqlEx.getErrorCode());
				logger.error(sqlEx.getMessage(), sqlEx);
			}
		}
	}

}
