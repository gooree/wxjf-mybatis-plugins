package com.wxjfkg.mybatis.dialect;

/**
 * EnumDatabase
 * 
 * @author guorui@wxjfkg.com
 */
public enum EnumDatabase {

	ORACLE("ORACLE"), MYSQL("MYSQL"), SQLSERVER("SQLSERVER"), DB2("DB2");

	private String databaseName;

	private EnumDatabase(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	
}
