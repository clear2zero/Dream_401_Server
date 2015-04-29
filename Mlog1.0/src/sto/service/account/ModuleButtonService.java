package sto.service.account;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sto.common.service.BaseServiceImpl;
import sto.common.util.Parameter;
import sto.dao.account.ModuleButtonDao;
import sto.model.account.ModuleButton;

/**
 * @ClassName: RoleService
 * @Description: service
 * @author chenxiaojia
 * @date 2014-7-25 11:07:12
 * 
 */
@Service
@SuppressWarnings("unchecked")
public class ModuleButtonService extends BaseServiceImpl<ModuleButton>{
	@Resource
	ModuleButtonDao moduleButtonDao;
	@Transactional(readOnly = false)
	public void save(int mid,String bids){
		Parameter parameter1 = new Parameter();
		parameter1.put("p1", mid);
		List<ModuleButton> list = this.find("from ModuleButton a where a.module.id=:p1", parameter1);
		for(ModuleButton mb : list){
			this.delete(mb);
		}
		String[] bidsarr = bids.split(",");
		String sql = " insert into platform_t_module_button(moduleid,buttonid) values(:p1,:p2) ";
		for(String bid:bidsarr){
			Parameter parameter = new Parameter();
			parameter.put("p1", mid);
			parameter.put("p2", Integer.parseInt(bid));
			this.updateBySql(sql, parameter);
		}
	}
	
	public List<Map<String,Object>> getModuleButtonsAuth(Map<String,Object> parammap){
		Parameter parameter = new Parameter();
		parameter.put("p1", parammap.get("mid"));
		parameter.put("p2", parammap.get("rid"));
		String sql = "select a.id,a.moduleid,a.buttonid,c.name bname,c.enname bename,case when a.buttonid=b.buttonid then 1 else 0 end checked " 
				+" from platform_t_module_button a "
				+" left join (select * from platform_t_role_module_button where roleid=:p2) b on a.moduleid=b.moduleid and a.buttonid=b.buttonid "
				+" left join platform_t_button c on a.buttonid=c.id "
				+ " where 1=1 and a.moduleid=:p1 ";
		
		List<Object[]> list = moduleButtonDao.findBySql(sql, parameter);
		List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();
		if(list != null){
			for(Object[] o : list){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", String.valueOf(o[0]));
				map.put("moduleid", String.valueOf(o[1]));
				map.put("buttonid", String.valueOf(o[2]));
				map.put("bname", String.valueOf(o[3]));
				map.put("bename", String.valueOf(o[4]));
				map.put("checked", String.valueOf(o[5]));
				list1.add(map);
			}
		}
		return list1;
	}
	
}
