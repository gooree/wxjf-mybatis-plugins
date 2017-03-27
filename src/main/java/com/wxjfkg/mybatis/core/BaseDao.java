package com.wxjfkg.mybatis.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.wxjfkg.mybatis.jdbc.Page;
import com.wxjfkg.mybatis.jdbc.Pagination;

public interface BaseDao<PK extends Serializable , T> {
	
	public T get(PK id);

	public List<T> findAll();
	
	public List<T> queryForList(Map<String, Object> parameterMap);
	
	public Page<T> queryForPage(Pagination pagination);
	
	public int save(T entity);
	
	public int update(T entity);
	
	public int delete(PK id);
	
}
