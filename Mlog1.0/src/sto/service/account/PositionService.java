package sto.service.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import other.AuthProfile;
import sto.common.service.BaseServiceImpl;
import sto.common.util.Page;
import sto.common.util.Parameter;
import sto.dao.account.PositionDao;
import sto.form.MsgForm;
import sto.form.MsgReplyForm;
import sto.form.PositionForm;
import sto.model.account.Position;

/**
 * @ClassName: RoleService
 * @Description: service
 * @author zzh
 * @date 2014-11-5 11:07:12
 * 
 */
@Service
@SuppressWarnings("unchecked")
public class PositionService extends BaseServiceImpl<Position>{
	@Autowired
	private PositionDao positionDao;
	@Resource
	private DataRulesService dataRulesService;
	public List<PositionForm> getLastPosition(Integer managerid,String getuserids){
		String whereSql = "";
		if(!StringUtils.isBlank(getuserids)){
			String userids = dataRulesService.filterAuthUser(3,managerid,getuserids);
			whereSql = " and sender in ("+userids+") ";
		}else {
			String userids = dataRulesService.getDataAuth(managerid, 3);
			whereSql = " and sender in ("+userids+") ";
		}
		long bb = System.currentTimeMillis();
		String sql = "select b.sender userid,d.name username,b.lgt longitude,b.lat latitude,b.address,b.time "
				+" from mlog_position b ,platform_t_user d,(select max(id) id from mlog_position c where 1=1 "
				+ whereSql
				+ " group by c.sender) e where b.sender=d.id "
				+ " and b.id=e.id "
				+ whereSql;
		List<Object[]> list = positionDao.findBySql(sql, null);
		List<PositionForm> formlist = new ArrayList<PositionForm>();
		if(list != null){
			for(Object[] o : list){
				PositionForm form = new PositionForm();
				form.setUserid(Integer.parseInt(String.valueOf(o[0])));
				form.setUsername(String.valueOf(o[1]));
				form.setLongitude(String.valueOf(o[2]));
				form.setLatitude(String.valueOf(o[3]));
				form.setAddress(String.valueOf(o[4]));
				form.setTime(String.valueOf(o[5]));
				formlist.add(form);
			}
		}
		return formlist;
	}
	public List<PositionForm> getPositions(Integer managerid,String date,String getuserids){
		String whereSql = "";
		if(!StringUtils.isBlank(getuserids)){
			String userids = dataRulesService.filterAuthUser(3,managerid,getuserids);
			whereSql = " and a.sender in ("+userids+") ";
		}else {
			String userids = dataRulesService.getDataAuth(managerid, 3);
			whereSql = " and a.sender in ("+userids+") ";
		}
		String sql = "select a.sender userid,b.name username,a.lgt longitude,a.lat latitude,a.address,a.time "
				+" from mlog_position a,platform_t_user b "
				+" where a.sender=b.id and date_format(a.time,'%Y-%m-%d')=date_format('"+date+"','%Y-%m-%d') "
				+ whereSql
				+" order by a.time asc ";
		List<Object[]> list = positionDao.findBySql(sql, null);
		List<PositionForm> formlist = new ArrayList<PositionForm>();
		if(list != null){
			for(Object[] o : list){
				PositionForm form = new PositionForm();
				form.setUserid(Integer.parseInt(String.valueOf(o[0])));
				form.setUsername(String.valueOf(o[1]));
				form.setLongitude(String.valueOf(o[2]));
				form.setLatitude(String.valueOf(o[3]));
				form.setAddress(String.valueOf(o[4]));
				form.setTime(String.valueOf(o[5]));
				formlist.add(form);
			}
		}
		return formlist;
	}
	
	public List<Map<String,Object>> getPositionsByUserid(Map<String,Object> parammap){
		Parameter parm = new Parameter();
		parm.put("p1", parammap.get("date"));
		parm.put("p2", parammap.get("userid"));
		String sql = "select a.sender userid,b.name username,a.lgt longitude,a.lat latitude,a.address,a.time "
				+" from mlog_position a,platform_t_user b "
				+" where a.sender=b.id and date_format(a.time,'%Y-%m-%d')=date_format(:p1,'%Y-%m-%d') "
				+" and a.sender=:p2 "
				+" order by a.time asc ";
		List<Object[]> list = positionDao.findBySql(sql, parm);
		List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();
		if(list != null){
			for(Object[] o : list){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("userid", String.valueOf(o[0]));
				map.put("username", String.valueOf(o[1]));
				map.put("longitude", String.valueOf(o[2]));
				map.put("latitude", String.valueOf(o[3]));
				map.put("address", String.valueOf(o[4]));
				map.put("time", String.valueOf(o[5]));
				list1.add(map);
			}
		}
		return list1;
	}
}
