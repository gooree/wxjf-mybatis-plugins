package com.wxjfkg.mybatis.dialect;

import java.util.HashMap;
import java.util.Map;

public class DialectFactory {

	private static final Map<String, Dialect> dialectMap = new HashMap<String, Dialect>();

	static {
		dialectMap.put(EnumDatabase.MYSQL.name().toLowerCase(),
				new MySQLDialect());
	}
	
	public static Dialect getDialect(String dialectName) {
		return dialectMap.get(dialectName);
	}

}
