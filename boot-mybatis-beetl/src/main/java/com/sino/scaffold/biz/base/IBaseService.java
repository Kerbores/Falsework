package com.sino.scaffold.biz.base;

import java.util.List;

import com.sino.scaffold.model.base.BaseEntity;

/**
 * 
 * @author kerbores
 *
 * @param <T>
 */
public interface IBaseService<T extends BaseEntity> {

	// ============C==========

	int save(T obj);

	int saveIgnoreNull(T obj);

	int saveList(List<T> list);

	// ============R==========

	T findById(Integer id);

	List<T> listAll();
	
	Pager<T> listByPage(int page,int pageSize);
	
	Pager<T> listByPage(int page,int pageSize,T example);

	List<T> searchByExample(T example);

	// ============U==========

	int update(T obj);

	int updateIgnoreNull(T obj);

	// ============D==========

	int deleteById(long id);

}
