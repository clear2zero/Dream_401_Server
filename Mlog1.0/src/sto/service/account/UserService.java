package sto.service.account;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sto.common.service.BaseServiceImpl;
import sto.common.util.Parameter;
import sto.common.util.RoleType;
import sto.common.util.StringUtils;
import sto.dao.account.UserDao;
import sto.model.account.User;

/**
 * @ClassName: RoleService
 * @Description: service
 * @author zzh
 * @date 2014-11-5 11:07:12
 * 
 */
@Service
@SuppressWarnings("unchecked")
public class UserService extends BaseServiceImpl<User>{
	@Autowired
	private UserDao userDao;
	
	@Transactional(readOnly = false)
	public void save(User user, String[] roleids) {
		// 保存user信息到user表
		userDao.save(user);
		// 保存角色信息 , 先删除原来的角色信息，再插入新的角色信息
		userDao.delete("delete from RoleUser where user.id=:p1", new Parameter(user.getId()));
		for(int i=0;i<roleids.length;i++){
			userDao.updateBySql("insert into platform_t_user_role(roleid,userid) values(" + roleids[i] + "," + user.getId() +")", null);
		}
		userDao.flush();
	}
	
	public int checkUserNameAndPassWordByDivid(User user) {
		Parameter parameter = new Parameter();
		parameter.put("p1", user.getUsername());
		parameter.put("p2", user.getUnit().getDivid());
		String hql = "from User u where 1=1 and u.username=:p1 and u.unit.divid=:p2 ";
		List<User> list = userDao.find(hql, parameter);
		if(list != null && list.size()>0){
			User u = list.get(0);
			if(u.getIsdelete() == null || u.getIsdelete() == 1
					||u.getIsenable() == null || u.getIsenable() == 0){
				return 3;//账户已冻结
			}else {
				if(user.getPassword().equals(u.getPassword())){
					return 1;
				}else {
					return 4;//密码错误
				}
			}
		}else {
			return 2;//该单位无此用户
		}
	}
	
	public User getUserByNameAndPassWordByDivid(User user) {
		Parameter parameter = new Parameter();
		parameter.put("p1", user.getUsername());
		parameter.put("p2", user.getUnit().getDivid());
		String hql = "from User u where 1=1 and u.username=:p1 and u.unit.divid=:p2 ";
		return (User)userDao.find(hql,parameter).get(0);
	}
	
	public User getUnitManagerByNameAndPassWord(User user) {
		Parameter parameter = new Parameter();
		parameter.put("p1", user.getUsername());
		parameter.put("p2", user.getPassword());
		String sql = " select a.* from platform_t_user a,platform_t_role b where a.roleid=b.id "
			+" and b.enname in ('"+RoleType.NOREPEAT_USERGROUP.getName().replace(",", "','")+"') "
			+" and a.username=:p1 and a.password=:p2 ";
		List<User> list = userDao.findBySql(sql, parameter, User.class);
		return list == null || list.size()<=0 ? null : list.get(0);
	}
	
	public int checkScertcn(String scertcn) {
		Parameter parameter = new Parameter();
		parameter.put("p1", scertcn);
		String hql = "from User u  where 1=1";
		if (!StringUtils.isBlank(scertcn)) {
			hql += " and u.scertcn = '" + scertcn.trim() + "'";
			// unameFlag 0表示不存在用户名
			List<User> list = userDao.find(hql);
			if (list != null && list.size() > 0) {
				return 1;
			} else {
				return 2;
			}
		} else {
			return 3;
		}
	}
	
	public int checkScertcn(Map<String,String> map) {//接口调用
		Parameter parameter = new Parameter();
		parameter.put("p1", map.get("scertcn"));
		String hql = "from User u where 1=1 ";
		if (!StringUtils.isBlank(map.get("scertcn"))) {
			hql += " and u.scertcn = '" + map.get("scertcn").trim() + "'";
			// unameFlag 0表示不存在用户名
			List<User> list = userDao.find(hql);
			if (list != null && list.size() > 0) {
				User user = list.get(0);
				if(user.getIsdelete() == null || user.getIsdelete() == 1
						||user.getIsenable() == null || user.getIsenable() == 0){
					return 6;//账户已冻结
				}else{
					if(StringUtils.isBlank(map.get("deviceid"))){
						if(!StringUtils.isBlank(user.getDeviceid())){
							return 5;//已与其他设备绑定
						}else {
							if(StringUtils.isBlank(user.getImei())){
								return 4;//未与设备绑定
							}else if(user.getImei().equals(map.get("imei"))){
								return 1;//与该设备绑定
							}else {
								return 5;//已与其他设备绑定
							}
						}
					}else {
						if(StringUtils.isBlank(user.getDeviceid())){
							return 4;//未与设备绑定
						}else if(user.getDeviceid().equals(map.get("deviceid"))){
							return 1;//与该设备绑定
						}else {
							return 5;//已与其他设备绑定
						}
					}
				}
			} else {
				return 2;
			}
		} else {
			return 3;
		}
	}
	
	public User getUserByScertcn(String scertcn) {
		String hql = "from User u where 1=1 and u.isdelete = 0 and u.isenable=1 ";
		if (!StringUtils.isBlank(scertcn)) {
			hql += " and u.scertcn = '" + scertcn.trim() + "'";
		}
		List userList = userDao.find(hql);
		User uu = (User) userList.get(0);
		return uu;
	}
	
	//PC端登录验证 1成功 2证书未绑定 3证书不存在
	public int checkHcertcn(String hcertcn) {
		String hql = "from User u where 1=1";
		if (!StringUtils.isBlank(hcertcn)) {
			hql += " and u.hcertcn = '" + hcertcn.trim() + "'";
			// unameFlag 0表示不存在用户名
			List<User> list = userDao.find(hql);
			if (list != null && list.size() > 0) {
				return 1;
			} else {
				return 2;
			}
		} else {
			return 3;
		}
	}
	
	
	public boolean checkUsernameIsExist(String username,String divid) {
		String sql = " select a.* from platform_t_user a,platform_t_role b where a.roleid=b.id ";
		Parameter parameter = new Parameter();
		parameter.put("p1", username);
		String whereSql = " and a.username=:p1 ";
		if(!StringUtils.isBlank(divid)){
			sql = " select a.* from platform_t_user a where a.divid=:p2 ";
			parameter.put("p2", divid);
		}else {
			sql = " select a.* from platform_t_user a,platform_t_role b where a.roleid=b.id "
				+ " and b.enname in ('"+RoleType.NOREPEAT_USERGROUP.getName().replace(",", "','")+"') ";
		}
		sql += whereSql;
			
		List<User> list = userDao.findBySql(sql, parameter, User.class);
		return list == null || list.size()<=0 ? false : true;
	}
}
