package sto.service.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sto.common.service.BaseServiceImpl;
import sto.common.util.Parameter;
import sto.dao.account.DistrictDao;
import sto.model.account.District;

@Service
public class DistrictService extends BaseServiceImpl<District> {

	@Autowired
	private DistrictDao districtDao;

	public District get(String id) {
		return districtDao.get(id);
	}
	
	public List<District> find(String qlString, Parameter parameter) {
		return districtDao.find(qlString, parameter);
	}
	
}
