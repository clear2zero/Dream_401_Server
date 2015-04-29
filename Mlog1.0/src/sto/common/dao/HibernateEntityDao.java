package sto.common.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * 
* @ClassName: HibernateEntityDao 
* @Description: 业务基类调用此类
* @author chenxiaojia
* @date 2014-7-19 下午6:51:26 
*
 */
@Repository
public class HibernateEntityDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public <T> List<T> getAll(Class<T> t) {
		return sessionFactory.getCurrentSession()
				.createQuery("from " + t.getSimpleName()).list();
	}
	
	public <T> void save(T t){
		sessionFactory.getCurrentSession().save(t);
	}
	public <T> void update(T t){
		sessionFactory.getCurrentSession().update(t);
	}
	public <T> void delete(T t){
		sessionFactory.getCurrentSession().delete(t);
	}
	
	/**
	 * 创建Query对象.
	 * 对于需要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以返回Query后自行设置.
	 * 留意可以连续设置,如 dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
	 *
	 * @param values 可变参数
	 *               用户可以如下四种方式使用
	 *               dao.getQuery(hql)
	 *               dao.getQuery(hql,arg0);
	 *               dao.getQuery(hql,arg0,arg1);
	 *               dao.getQuery(hql,new Object[arg0,arg1,arg2])
	 */
	public Query getQuery(String hql, Object... values) {
		Assert.hasText(hql);
		Query query = getSession().createQuery(hql);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}

	/**
	 * 创建Criteria对象
	 *
	 * @param criterion 可变条件列表,Restrictions生成的条件
	 */
	public <T> Criteria getCriteria(Class<T> entityClass, Criterion... criterion) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterion) {
			criteria.add(c);
		}
		return criteria;
	}

	public Session getSession(){
		return sessionFactory.getCurrentSession();
	}

	/**
	 * hql查询.
	 *
	 * @param values 可变参数
	 *               用户可以如下四种方式使用
	 *               dao.find(hql)
	 *               dao.find(hql,arg0);
	 *               dao.find(hql,arg0,arg1);
	 *               dao.find(hql,new Object[arg0,arg1,arg2])
	 */
	public List find(String hql, Object... values) {
		Assert.hasText(hql);
		return getQuery(hql, values).list();
	}

	/**
	 * 根据属性名和属性值查询对象.
	 *
	 * @return 符合条件的对象列表
	 */
	public <T> List<T> findBy(Class<T> entityClass, String name, Object value) {
		Assert.hasText(name);
		return getCriteria(entityClass, Restrictions.eq(name, value)).list();
	}

	/**
	 * 根据属性名和属性值查询唯一对象.
	 *
	 * @return 符合条件的唯一对象
	 */
	public <T> T findUniqueBy(Class<T> entityClass, String name, Object value) {
		Assert.hasText(name);
		return (T) getCriteria(entityClass, Restrictions.eq(name, value)).uniqueResult();
	}

	/**
	 * 根据属性名和属性值以Like AnyWhere方式查询对象.
	 */
	public <T> List<T> findByLike(Class<T> entityClass, String name, String value) {
		Assert.hasText(name);
		return getCriteria(entityClass, Restrictions.like(name, value, MatchMode.ANYWHERE)).list();
	}

	
	public String getIdName(Class entityClass) {
		Assert.notNull(entityClass);
		String idName = sessionFactory.getClassMetadata(entityClass).getIdentifierPropertyName();
		Assert.hasText(idName, entityClass.getSimpleName() + "has no id column define");
		return idName;
	}
	
}
