package sto.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sto.common.service.BaseServiceImpl;
import sto.dao.account.MsgReceiverDao;
import sto.model.account.MsgReceiver;

/**
 * @ClassName: RoleService
 * @Description: service
 * @author zzh
 * @date 2014-11-5 11:07:12
 * 
 */
@Service
@SuppressWarnings("unchecked")
public class MsgReceiverService extends BaseServiceImpl<MsgReceiver>{
	@Autowired
	private MsgReceiverDao msgReceiverDao;
}
