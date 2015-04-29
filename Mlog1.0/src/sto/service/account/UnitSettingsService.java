package sto.service.account;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sto.common.service.BaseServiceImpl;
import sto.common.util.Parameter;
import sto.dao.account.UnitSettingsDao;
import sto.model.account.SysSettings;
import sto.model.account.UnitSettings;

@Service
public class UnitSettingsService extends BaseServiceImpl<UnitSettings> {

	@Autowired
	private UnitSettingsDao unitSettingsDao;
	@Resource  
    UnitSettingsService unitSettingsService;
	@Resource  
    SysSettingsService sysSettingsService;
	
	//单位设置恢复默认系统设置
	@Transactional(readOnly = false)
	public void restoreDefaultSettings(String divid){
		Parameter parameter = new Parameter();
		parameter.put("p1", divid);
		unitSettingsService.delete("delete from UnitSettings where divid=:p1", parameter);
		List<SysSettings> list = sysSettingsService.find("from SysSettings where iscommon=0 ", null);
		for(SysSettings ss:list){
			UnitSettings us = new UnitSettings();
			us.setDivid(divid);
			us.setName(ss.getName());
			us.setSkey(ss.getSkey());
			us.setValue(ss.getValue());
			unitSettingsService.save(us);
		}
	}
}
