
  
package other;  

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.web.context.ContextLoader;

import com.alibaba.druid.pool.DruidDataSource;

/** 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2014年8月14日 下午2:43:12 <br/> 
 * @author   shenqn 
 * @version  1.0  
 * Copyright (c) 2014, 河北腾翔软件科技有限公司 All Rights Reserved.
 */
public class DBUtility {

	public static DataSource getDataSource(){
		DruidDataSource bs = (DruidDataSource)ContextLoader.getCurrentWebApplicationContext().getBean("dataSource");
    	return bs;
	}
}
  
