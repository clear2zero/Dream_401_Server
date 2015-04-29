package sto.service.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sto.common.service.BaseServiceImpl;
import sto.common.util.Parameter;
import sto.dao.account.ModuleDao;
import sto.model.account.Module;

@Service
public class ModuleService extends BaseServiceImpl<Module> {

	@Autowired
	private ModuleDao moduleDao;

	public Module get(String id) {
		return moduleDao.get(id);
	}
	
	public List<Module> find(String qlString, Parameter parameter) {
		return moduleDao.find(qlString, parameter);
	}
	
}
