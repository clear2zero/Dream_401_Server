package sto.dao.account;

import java.util.List;

import org.springframework.stereotype.Repository;

import sto.common.dao.BaseDao;

import sto.model.account.Role;


/** 
 * @ClassName: RoleDao 
 * @Description: dao
 * @author zzh
 * @date 2014-7-25 11:07:12
 *  
 */
@Repository
public class RoleDao extends BaseDao<Role> {
	
	public List getPermission(String roleId){
		return getSession().createSQLQuery("select res.permission,res.url from oa_role_resourcesto rr ,oa_resourcesto res where rr.role_id=? and rr.res_id=res.id").setString(0, roleId)
				.list();
	}
	
}
