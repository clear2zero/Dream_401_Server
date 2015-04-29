package other;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.InitializingBean;
/**
 * Reason: TODO ADD REASON. <br/>
 * Date: 2014年8月4日 上午11:20:47 <br/>
 * 
 * @author shenqn
 * @version 1.0 Copyright (c) 2014, 河北腾翔软件科技有限公司 All Rights Reserved.
 */
public class DataSourceAdapter extends BasicDataSource implements
		InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		Properties cfg = new Properties();
		InputStream is = DataSourceAdapter.class
				.getResourceAsStream("/db.properties");
		try {
			cfg.load(is);
			boolean isOpen=cfg.getProperty("rwsEngine.open").equals("true");
			
			String dispatchClass=cfg.getProperty("rwsEngine.dispatcher");
			Set<Object> keys = cfg.keySet();
			Set<String>  readDB=new HashSet<String>();
			for (Object okey : keys) {
				String key = (String) okey;
				if(key.startsWith("read.connection")){
					readDB.add(key.split("\\.")[1]);
				}
			}
			List<ReadDBConfig> rdbConfigs=new ArrayList<ReadDBConfig>();
			for(String rdb:readDB){
				String prefix="read."+rdb+".";
				ReadDBConfig dcfg=new ReadDBConfig();
				dcfg.setDriver(cfg.getProperty(prefix+"driver"));
				dcfg.setUrl(cfg.getProperty(prefix+"url"));
				dcfg.setName(cfg.getProperty(prefix+"user"));
				dcfg.setPassword(cfg.getProperty(prefix+"password"));
				dcfg.setInitialSize(cfg.getProperty(prefix+"initialSize"));
				dcfg.setMaxActive(cfg.getProperty(prefix+"maxActive"));
				dcfg.setMinIdle(cfg.getProperty(prefix+"minIdle"));
				dcfg.setMaxIdle(cfg.getProperty(prefix+"maxIdle"));
				dcfg.setMaxWait(cfg.getProperty(prefix+"maxWait"));
				rdbConfigs.add(dcfg);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
