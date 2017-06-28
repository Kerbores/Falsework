package com.sino.scaffold.biz.base;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.ReflectUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sino.scaffold.common.SinoMapper;
import com.sino.scaffold.model.base.BaseEntity;

/**
 * 
 * @author kerbores
 *
 * @param <T>
 */
public abstract class BaseService<T extends BaseEntity> implements IBaseService<T> {

	@Autowired
	protected SinoMapper<T> mapper;

	protected Class<T> klass;

	protected Field idField;

	protected Class<T> getEntityClass() {
		return klass;
	}

	@PostConstruct
	private void init() {
		ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
		@SuppressWarnings("unchecked")
		Class<T> entityClass = (Class<T>) (parameterizedType.getActualTypeArguments()[0]);
		this.klass = entityClass;
		for (Field field : klass.getFields()) {
			if (field.getAnnotation(Id.class) != null) {
				idField = field;
				break;
			}
		}
	}

	@Override
	public int save(T obj) {
		return mapper.insert(obj);
	}

	@Override
	public int saveList(List<T> list) {
		return mapper.insertList(list);
	}

	@Override
	public int saveIgnoreNull(T obj) {
		return mapper.insertSelective(obj);
	}

	@Override
	public T findById(Integer id) {
		@SuppressWarnings("unchecked")
		T entity = (T) ReflectUtils.newInstance(klass);
		try {
			idField.setAccessible(true);
			idField.set(entity, id);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return mapper.selectByPrimaryKey(entity);
	}

	@Override
	public List<T> listAll() {
		return mapper.selectAll();
	}

	@Override
	public List<T> searchByExample(T example) {
		return mapper.selectByExample(example);
	}

	@Override
	public int update(T obj) {
		return mapper.updateByPrimaryKey(obj);
	}

	@Override
	public int updateIgnoreNull(T obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	@Override
	public int deleteById(long id) {
		@SuppressWarnings("unchecked")
		T entity = (T) ReflectUtils.newInstance(klass);
		try {
			idField.setAccessible(true);
			idField.set(entity, id);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return mapper.deleteByPrimaryKey(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sino.scaffold.biz.base.IBaseService#listByPage(int, int)
	 */
	@Override
	public Pager<T> listByPage(int page, int pageSize) {
		Page<T> p = PageHelper.startPage(page, pageSize);
		mapper.selectAll();

		Pager<T> pager = new Pager<>(page, pageSize);
		pager.setData(p.getResult());
		pager.setTotal(p.getTotal());

		return pager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sino.scaffold.biz.base.IBaseService#listByPage(int, int,
	 * com.sino.scaffold.model.base.BaseEntity)
	 */
	@Override
	public Pager<T> listByPage(int page, int pageSize, T example) {
		Page<T> p = PageHelper.startPage(page, pageSize);
		mapper.selectByExample(example);

		Pager<T> pager = new Pager<>(page, pageSize);
		pager.setData(p.getResult());
		pager.setTotal(p.getTotal());

		return pager;
	}

}
