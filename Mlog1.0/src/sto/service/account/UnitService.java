package sto.service.account;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sto.common.Md5Encrypt;
import sto.common.MlogPM;
import sto.common.service.BaseServiceImpl;
import sto.dao.account.UnitDao;
import sto.form.RegUnitForm;
import sto.model.account.Dept;
import sto.model.account.Unit;
import sto.model.account.User;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: RoleService
 * @Description: service
 * @author chenxiaojia
 * @date 2014-7-25 11:07:12
 * 
 */
@Service
@SuppressWarnings("unchecked")
public class UnitService extends BaseServiceImpl<Unit>{
	@Resource
	UnitDao unitDao;
	@Resource
	DeptService deptService;
	@Resource
	RoleService roleService;
	@Resource
	UserService userService;
	public String getNewDivid(){
		return String.valueOf(unitDao.createSqlQuery("select rand_string(5) ", null).uniqueResult());
	}
	//事物操作，保证 初始化单位、部门、单位管理员用户是原子操作
	@Transactional(readOnly = false)
	public void initUnit(RegUnitForm form){
		Unit unit = new Unit();
		unit.setDivid(getNewDivid());
		unit.setProjectid(MlogPM.get("online.projectid"));
		unit.setParentid("0");//设置为顶级单位
		unit.setDivid(getNewDivid());
		unit.setDivname(form.getDivname());  
		unit.setAddr(form.getAddr());        
		unit.setLinkman(form.getLinkman());     
		unit.setCorporation(form.getCorporation()); 
		unitDao.save(unit);
		
		//默认为单位注册一个顶级部门,注册顶级部门
		Dept dept = new Dept();
		dept.setDeptid("10");
		dept.setDeptname(unit.getDivname());
		dept.setParent(null);
		dept.setLevel(1);
		dept.setOrderid(1);
		dept.setIsdelete(0);
		dept.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		dept.setDivid(unit.getDivid());
		deptService.save(dept);
		
		//注册单位管理员
		User user = new User();
		user.setName(form.getName());
		user.setUsername(form.getUsername());
		user.setUnit(unit);
		user.setDept(dept);
		user.setRole(roleService.findUniqueBy("enname", "unitmanager"));
		//user.setClientrole(0);
		user.setIsdelete(0);
		user.setIsenable(1);
		user.setIdentitycard(form.getIdentitycard());
		user.setMobilephone(form.getMobilephone());
		user.setPassword(Md5Encrypt.md5(form.getPassword()));
		userService.save(user);
	}
	@Transactional(readOnly = false)
	public JSONObject initUnit1(final RegUnitForm form) throws SQLException{
		Connection conn = SessionFactoryUtils.getDataSource(unitDao.getSession().getSessionFactory()).getConnection();  
		CallableStatement cstmt  = conn.prepareCall("{Call p_regunit(?,?,?,?,?,?,?,?,?,?,?,?)}");  
		cstmt.setString(1, MlogPM.get("online.projectid"));
		cstmt.setString(2, form.getDivname());
		cstmt.setString(3, form.getAddr());
		cstmt.setString(4, form.getLinkman());
		cstmt.setString(5, form.getCorporation());
		cstmt.setString(6, form.getName());
		cstmt.setString(7, form.getIdentitycard());
		cstmt.setString(8, form.getMobilephone());
		cstmt.setString(9, form.getUsername());
		cstmt.setString(10, Md5Encrypt.md5(form.getPassword()));
		cstmt.registerOutParameter(11, Types.VARCHAR);
		cstmt.registerOutParameter(12, Types.INTEGER);
		cstmt.execute();
		JSONObject json = new JSONObject();
		int ret = Integer.parseInt(String.valueOf(cstmt.getObject(12)));
		if(ret == 0){
			json.put("success", true);
		}else {
			json.put("success", false);
			json.put("msg", String.valueOf(cstmt.getObject(11)));
		}
		cstmt.close();
		conn.close();
		return json;
	}
}
