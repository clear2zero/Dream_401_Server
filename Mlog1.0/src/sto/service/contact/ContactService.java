package sto.service.contact;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sto.common.service.BaseServiceImpl;
import sto.dao.contact.ContactDao;
import sto.model.account.Role;
import sto.model.contacts.Contacts;

/**
 * @ClassName: ContactService
 * @Description: service
 * @author wuyanhui
 * @date 2014-9-25
 * 
 */
@Service
@SuppressWarnings("unchecked")
public class ContactService extends BaseServiceImpl<Role> {
	@Autowired
	public ContactDao contactdao;

	public Contacts getContact(final Long contactId)throws SQLException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Contacts contact = new Contacts();
		List<Object[]> list = contactdao.getContact(contactId);
		List<Object[]> listnot;
		Object object[]={};
		Object objectnot[];
		 try {
			 
			object = list.get(0);
			
			contact.setContactId(Long.parseLong(String.valueOf(object[0])));
			contact.setGroupId(Long.parseLong(String.valueOf(object[1])));			
			contact.setGroupName(String.valueOf(object[2]));
			if(StringUtils.isNotBlank(String.valueOf(object[3]))){
				try {
					contact.setBirthday(String.valueOf(object[3]).substring(0, 10));
				} catch (Exception e1) {
					contact.setBirthday("");
				}
			}else{
				contact.setBirthday(String.valueOf(""));
			}
			contact.setGsmNoPrimary(String.valueOf(object[4])==null?"":String.valueOf(object[4]));
			contact.setLastName(String.valueOf(object[5]));
			contact.setSex(String.valueOf(object[6])==null?"":String.valueOf(object[6]));
			contact.setEmailPrimary(String.valueOf(object[7])==null?"":String.valueOf(object[7]));
			contact.setWorkDepartment(String.valueOf(object[8])==null?"":String.valueOf(object[8]));
			if(null==object[9]){
				contact.setWorkAddress("");
			}else{
			contact.setWorkAddress(new String((byte[]) object[9]));
			}
			contact.setWorkPhone(String.valueOf(object[10])==null?"":String.valueOf(object[10]));
			contact.setWorkCompany(String.valueOf(object[11])==null?"":String.valueOf(object[11]));
			if (null==object[12]) {
				contact.setCertInfo("");
			}else{
				contact.setCertInfo(new String((byte[]) object[12]));
			}
		} catch (Exception e) {
			e.printStackTrace();
			listnot=contactdao.getContactNotGroup(contactId);
			objectnot = listnot.get(0);
			contact.setContactId(Long.parseLong(String.valueOf(objectnot[0])));
			contact.setGroupId(0L);
			contact.setGroupName("");
			
			if(StringUtils.isNotBlank(String.valueOf(objectnot[1]))){
				try {
					contact.setBirthday(String.valueOf(objectnot[1]).substring(0, 10));
				} catch (Exception e1) {
					contact.setBirthday("");
				}
			}else{
				contact.setBirthday("");
			}
			
			
			contact.setGsmNoPrimary(String.valueOf(objectnot[2])==null?"":String.valueOf(objectnot[2]));
			contact.setLastName(String.valueOf(objectnot[3]));
			contact.setSex(String.valueOf(objectnot[4])==null?"":String.valueOf(objectnot[4]));
			contact.setEmailPrimary(String.valueOf(objectnot[5]));
			contact.setWorkDepartment(String.valueOf(objectnot[6])==null?"":String.valueOf(objectnot[6]));
			if(null==objectnot[7]){
				contact.setWorkAddress("");
			}else{
			contact.setWorkAddress(new String((byte[]) objectnot[7]));
			}
			contact.setWorkPhone(String.valueOf(objectnot[8])==null?"":String.valueOf(objectnot[8]));
			contact.setWorkCompany(String.valueOf(objectnot[9])==null?"":String.valueOf(objectnot[9]));
			if (null==objectnot[10]) {
				contact.setCertInfo(String.valueOf(""));
			}else{
				contact.setCertInfo(new String((byte[]) objectnot[10]));
			}
		}
		
		return contact;
	}

	public int updateContact(final Contacts contact)throws SQLException {
		return contactdao.updateContact(contact);
	}
   public int addContact(Contacts contact,final String userName)throws SQLException{
	   return contactdao.addContact(contact,userName);
   }
   public void moveContacts(final List<Long> contactIds,final long groupId)throws SQLException{
	    contactdao.moveContacts(contactIds, groupId);
   }
}
