package sto.service.account;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sto.common.service.BaseServiceImpl;
import sto.dao.account.SysSettingsDao;
import sto.model.account.SysSettings;

@Service
public class SysSettingsService extends BaseServiceImpl<SysSettings> {

	@Autowired
	private SysSettingsDao sysSettingsDao;
}
