package sto.dao.contact;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import sto.common.dao.BaseDao;
import sto.model.contacts.Contacts;

/**
 * @ClassName: ContactDao
 * @Description: dao
 * @author wuyanhui
 * @date 2014-9-25
 * 
 */
@Repository
public class ContactDao extends BaseDao<Contacts> {
	private static final String ADD_CONTACT = "insert into  contacts(first_name,birthday,gsm_no_primary,last_name,sex,email_primary,work_department,work_address,work_phone,work_company,PERSONAL_NOTE,user) values(?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String ADD_CONTACT1 = "insert into  contacts(first_name,gsm_no_primary,last_name,sex,email_primary,work_department,work_address,work_phone,work_company,PERSONAL_NOTE,user) values(?,?,?,?,?,?,?,?,?,?,?)";

	private static final String DELETE_CONTACT = "";
	private static final String UPDATE_CONTACT = "";
	private static final String SEARCH_CONTACT = "";

	private static final String ADD_GROUP = "";
	private static final String DELETE_GROUP = "";
	private static final String UPDATE_GROUP = "";
	private static final String SEARCH_GROUP = "";
    private static final String MOVE_CONTACTS="update contact_group_items set group_id=? where contact_id in (";
	private static final String ADD_CONTACT_ITEM = "insert into contact_group_items(user,group_id,contact_id) values(?,?,?)";
    private static final String GET_CONTACT_ID="select max(id) from contacts where user=?";
	public List getContact(final Long contactId) throws SQLException{
		return getSession()
				.createSQLQuery(
						"SELECT i.CONTACT_ID,i.GROUP_ID,g.SHORT_NAME,c.BIRTHDAY,c.GSM_NO_PRIMARY,c.LAST_NAME,c.SEX,c.EMAIL_PRIMARY,c.WORK_DEPARTMENT,c.WORK_ADDRESS,c.WORK_PHONE ,c.WORK_COMPANY,c.PERSONAL_NOTE from contacts c,contact_groups g,contact_group_items i WHERE i.GROUP_ID=g.id AND i.CONTACT_ID=c.ID AND i.CONTACT_ID=?")
				.setLong(0, contactId).list();
	}
	public List getContactNotGroup(final Long contactId)throws SQLException {
		return getSession()
				.createSQLQuery(
						"SELECT c.ID,c.BIRTHDAY,c.GSM_NO_PRIMARY,c.LAST_NAME,c.SEX,c.EMAIL_PRIMARY,c.WORK_DEPARTMENT,c.WORK_ADDRESS,c.WORK_PHONE ,c.WORK_COMPANY,c.PERSONAL_NOTE from contacts c  WHERE   c.ID=?")
				.setLong(0, contactId).list();
	}
	public int updateContact(final Contacts contact)throws SQLException {
		int ret = 1;
		final String sqlg = "update contact_group_items set group_id=? where contact_id=?";
		final String sql = "update contacts set last_name=?,sex=?,birthday=?,gsm_no_primary=?,email_primary=?,work_department=?,work_address=?,work_phone=?,work_company=?,PERSONAL_NOTE=? where id=?";
		final String sqll = "update contacts set last_name=?,sex=?,gsm_no_primary=?,email_primary=?,work_department=?,work_address=?,work_phone=?,work_company=?,PERSONAL_NOTE=? where id=?";

		Query query=null;
		if (StringUtils.isNotBlank(contact.getBirthday())) {
			query = getSession().createSQLQuery(sql)
					.setString(0, contact.getLastName())
					.setString(1, contact.getSex())
					.setString(2, contact.getBirthday())
					.setString(3, contact.getGsmNoPrimary())
					.setString(4, contact.getEmailPrimary())
					.setString(5, contact.getWorkDepartment())
					.setString(6, contact.getWorkAddress())
					.setString(7, contact.getWorkPhone())
					.setString(8, contact.getWorkCompany())
					.setString(9, contact.getCertInfo())
					.setLong(10, contact.getContactId());
		}else{
			query = getSession().createSQLQuery(sqll)
					.setString(0, contact.getLastName())
					.setString(1, contact.getSex())
					//.setString(2, contact.getBirthday())
					.setString(2, contact.getGsmNoPrimary())
					.setString(3, contact.getEmailPrimary())
					.setString(4, contact.getWorkDepartment())
					.setString(5, contact.getWorkAddress())
					.setString(6, contact.getWorkPhone())
					.setString(7, contact.getWorkCompany())
					.setString(8, contact.getCertInfo())
					.setLong(9, contact.getContactId());
		}
		Query query1 = getSession().createSQLQuery(sqlg)
				.setLong(0, contact.getGroupId()).setLong(1, contact.getContactId());
		Transaction transaction = getSession().beginTransaction();
		try {
			ret = query.executeUpdate();
			if (-1L!=contact.getGroupId()) {
				query1.executeUpdate();
			}
			transaction.commit();
		} catch (HibernateException e) {

			transaction.rollback();
		}

		return ret;
	}

	public int addContact(final Contacts contact,final String userName)throws SQLException {
		int ret = 1;
		long groupId;
		long id;
		if(-1L==contact.getGroupId()){
			groupId=0L;
		}else{
			groupId=contact.getGroupId();
		}
		Query addContact = getSession().createSQLQuery(ADD_CONTACT);
		Query addContactGroupItem = getSession().createSQLQuery(
				ADD_CONTACT_ITEM);
		Query contactId = getSession().createSQLQuery(GET_CONTACT_ID);
		contactId.setString(0,userName);
		if (StringUtils.isNotBlank(contact.getBirthday())) {
			addContact.setString(0, "").setString(1, contact.getBirthday())
					.setString(2, contact.getGsmNoPrimary())
					.setString(3, contact.getLastName())
					.setString(4, contact.getSex())
					.setString(5, contact.getEmailPrimary())
					.setString(6, contact.getWorkDepartment())
					.setString(7, contact.getWorkAddress())
					.setString(8, contact.getWorkPhone())
					.setString(9, contact.getWorkCompany())
					.setString(10, contact.getCertInfo())
					.setString(11, userName);
		}else{
			 addContact = getSession().createSQLQuery(ADD_CONTACT1);
			addContact.setString(0, "")
			.setString(1, contact.getGsmNoPrimary())
			.setString(2, contact.getLastName())
			.setString(3, contact.getSex())
			.setString(4, contact.getEmailPrimary())
			.setString(5, contact.getWorkDepartment())
			.setString(6, contact.getWorkAddress())
			.setString(7, contact.getWorkPhone())
			.setString(8, contact.getWorkCompany())
			.setString(9, contact.getCertInfo())
			.setString(10, userName);
		}
		addContactGroupItem.setString(0, userName).setLong(1, 0L);
		Transaction transaction = getSession().beginTransaction();
		try {
			addContact.executeUpdate();
			List<Object[]> list=contactId.list();
			Object obj=list.get(0);
			id=Long.parseLong(String.valueOf(obj));
			addContactGroupItem.setString(0, userName).setLong(1, groupId).setLong(2, id);
			addContactGroupItem.executeUpdate();
			transaction.commit();
		} catch (HibernateException e) {

			transaction.rollback();
		}
		return ret;
	}
	public void moveContacts(final List <Long>contactIds,final long groupId)throws SQLException{
		StringBuffer sb=new StringBuffer();
		sb.append(MOVE_CONTACTS);
		for (int j = 0; j < contactIds.size()-1; j++) {
			sb.append(contactIds.get(j));
			sb.append(",");
		}
		sb.append(contactIds.get(contactIds.size()-1));
		sb.append(")");
		Query moveContact = getSession().createSQLQuery(sb.toString());
		moveContact.setLong(0, groupId);
		try {
			moveContact.executeUpdate();
			
		} catch (HibernateException e) {

			
		}
	}
	
}
