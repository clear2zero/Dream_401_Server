package sto.service.account;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sto.common.service.BaseServiceImpl;
import sto.common.util.StringReg;
import sto.dao.account.JournalReplyDao;
import sto.form.JournalReplyForm;
import sto.form.MsgReplyForm;
import sto.model.account.JournalReply;

/**
 * @ClassName: RoleService
 * @Description: service
 * @author zzh
 * @date 2014-11-5 11:07:12
 * 
 */
@Service
@SuppressWarnings("unchecked")
public class JournalReplyService extends BaseServiceImpl<JournalReply>{
	@Autowired
	private JournalReplyDao journalReplyDao;
	public List<JournalReplyForm> getJournalReplyList(int journalid){
		String sql = "select a.id,b.id userid,case when (a.replyname='' or a.replyname is null) then b.name else concat(b.name,' 回复 ',a.replyname) end username,a.recontent content,a.redate time "
				+" from (select c.*,d.name replyname from mlog_journal_reply c left join platform_t_user d on c.replyto=d.id) a,platform_t_user b "
				+" where a.replyer=b.id and a.journalid="+journalid;
		List<Object[]> list = journalReplyDao.findBySql(sql, null);
		List<JournalReplyForm> formlist = new ArrayList<JournalReplyForm>();
		if(list != null){
			for(Object[] o:list){
				JournalReplyForm form = new JournalReplyForm();
				form.setId(Integer.parseInt(String.valueOf(o[0])));
				form.setUserid(Integer.parseInt(String.valueOf(o[1])));
				form.setUsername(String.valueOf(o[2]));
				form.setContent(String.valueOf(o[3]));
				form.setTime(String.valueOf(o[4]));
				formlist.add(form);
			}
		}
		return formlist;
	}
	
}
