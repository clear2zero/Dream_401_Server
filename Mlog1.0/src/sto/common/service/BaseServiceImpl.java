package sto.common.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import sto.common.dao.BaseDao;
import sto.common.dao.GenericsUtils;
import sto.common.dao.HibernateEntityDao;
import sto.common.util.Page;
import sto.common.util.Parameter;

/**
 * 
* @ClassName: BaseServiceImpl 
* @Description: 业务基类
* @author chenxiaojia
* @date 2014-7-19 下午7:27:17 
* 
* @param <T>
 */
@Service
@Transactional(readOnly = true)
public class BaseServiceImpl<T>  {
	
	@Autowired
	private BaseDao<T> baseDao;
	protected Logger logger = LoggerFactory.getLogger(getClass());
	Class entityClass;
	
	public BaseServiceImpl() {
		entityClass = GenericsUtils.getSuperClassGenricType(getClass());
	}
	
	public List getAll(){
		return baseDao.findAll();
	}
	
	public T get(Serializable id){
		return baseDao.get(id);
	}
	
	@Transactional(readOnly = false)
	public void update(T t) {
		baseDao.save(t);
		baseDao.flush();
	}
	
	@Transactional(readOnly = false)
	public void update(String qlString, Parameter parameter) {
		baseDao.update(qlString, parameter);
		baseDao.flush();
	}
	
	@Transactional(readOnly = false)
	public void delete(String qlString, Parameter parameter) {
		baseDao.delete(qlString, parameter);
		baseDao.flush();
	}
	
	
	@Transactional(readOnly = false)
	public void save(T t){
		baseDao.save(t);
		baseDao.flush();
	}
	@Transactional(readOnly = false)
	public void delete(T t){
		baseDao.delete(t);
		baseDao.flush();
	}
	@Transactional(readOnly = false)
	public int updateBySql(String sqlString, Parameter parameter){
		return baseDao.updateBySql(sqlString, parameter);
	}
	
	/**
	 * 根据属性名和属性值查询对象.
	 *
	 * @return 符合条件的对象列表
	 */
	public  List<T> findBy(String name, Object value) {
		Assert.hasText(name);
		DetachedCriteria dc = baseDao.createDetachedCriteria();
		dc.add(Restrictions.eq(name, value));
		return baseDao.find(dc);
	}

	/**
	 * 根据属性名和属性值查询唯一对象.
	 *
	 * @return 符合条件的唯一对象
	 */
	public T findUniqueBy(String name, Object value) {
		Assert.hasText(name);
		return (T) baseDao.getCriteria(entityClass, Restrictions.eq(name, value)).uniqueResult();
	}
	
	/**
	 * 根据属性名和属性值以Like AnyWhere方式查询对象.
	 */
	public  List<T> findByLike( String name, String value) {
		Assert.hasText(name);
		DetachedCriteria dc = baseDao.createDetachedCriteria();
		dc.add(Restrictions.like(name, value, MatchMode.ANYWHERE));
		return baseDao.find(dc);
	}
	
	/**
	 * 根据sql语句分页查询.
	 */
	public Page<T> findBySql(Page<T> page, String sqlString, Parameter parameter, Class<?> resultClass) {
		return baseDao.findBySql(page, sqlString, parameter, resultClass);
	}
	
	/**
	 * QL 分页查询
	 * @param page
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public <E> Page<E> find(Page<E> page, String qlString, Parameter parameter){
		return baseDao.find(page, qlString, parameter);
	}
	
	/**
	 * QL 查询
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> find(String qlString, Parameter parameter){
		return baseDao.find(qlString, parameter);
	}
	
	@SuppressWarnings("unchecked")
	public <E> List<E> findBySql(String qlString,Parameter parameter,Class<?> resultClass){
		return baseDao.findBySql(qlString,parameter,resultClass);
	}
	
}
