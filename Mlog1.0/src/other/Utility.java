package other;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.jenkov.mrpersister.itf.IGenericDao;

/**
 * @author Umut Gokbayrak
 */
public class Utility {

	public static IGenericDao getDbConnection() throws Exception {
		return getDbConnection("file");
	}

	
	public static IGenericDao getDbConnection(String name) throws Exception {
		DruidDataSource bs = (DruidDataSource) getDataSource();

		Connection con = bs.getConnection();
		return Constants.persistMan.getGenericDaoFactory().createDao(con);
	}
	
	public static DataSource getDataSource(){
//		DbConfig db = (DbConfig) DbConfigList.dbList.get("file");
		return DBUtility.getDataSource();
	
	}
	
}
