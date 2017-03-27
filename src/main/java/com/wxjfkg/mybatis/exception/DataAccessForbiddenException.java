package com.wxjfkg.mybatis.exception;

import org.apache.ibatis.exceptions.PersistenceException;

/**
 * Page 分页结果
 * 
 * @author guorui@wxjfkg.com
 */
public class DataAccessForbiddenException extends PersistenceException {

	private static final long serialVersionUID = 1317784694086458479L;

	public DataAccessForbiddenException() {
	}

	public DataAccessForbiddenException(String message) {
		super(message);
	}

	public DataAccessForbiddenException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataAccessForbiddenException(Throwable cause) {
		super(cause);
	}

}
