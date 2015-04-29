package sto.service.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sto.common.service.BaseServiceImpl;
import sto.common.util.Page;
import sto.common.util.Parameter;
import sto.dao.account.ButtonDao;
import sto.model.account.Button;

@Service
public class ButtonService extends BaseServiceImpl<Button> {
	@Autowired
	ButtonDao buttonDao;
	public List<Map<String,Object>> getButtonsContainCheckstatus(Page<Object[]> page,Map<String,Object> parammap){
		Parameter parameter = new Parameter();
		parameter.put("p1", parammap.get("mid"));
		String sql = "select a.id,a.name,a.enname,case when a.id=b.buttonid then 1 else 0 end checked "
				+" from platform_t_button a "
				+" left join (select * from platform_t_module_button where moduleid=:p1 ) b on a.id = b.buttonid "
				+" where a.status=1 order by a.id ";
		
		List<Object[]> list = buttonDao.findBySql(page, sql, parameter).getResult();
		List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();
		if(list != null){
			for(Object[] o : list){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", String.valueOf(o[0]));
				map.put("name", String.valueOf(o[1]));
				map.put("enname", String.valueOf(o[2]));
				map.put("checked", String.valueOf(o[3]));
				list1.add(map);
			}
		}
		return list1;
	}
}
