package sto.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class MlogPM{
	private static String countFilePath = ((ServletRequestAttributes)RequestContextHolder
			.getRequestAttributes()).getRequest().getSession().getServletContext().getRealPath(
			        "WEB-INF" + File.separator + "classes" + File.separator + "mlog.properties");
	private static Properties pro = new Properties();
	public static boolean set(String key, String value, String descp) {
		try {
			FileOutputStream fos = new FileOutputStream(countFilePath);
			pro.setProperty(key, value);
			pro.store(fos, descp);
			fos.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	public static boolean set(String key, String value) {
		try {
			FileOutputStream fos = new FileOutputStream(countFilePath);
			pro.setProperty(key, value);
			pro.store(fos, "");
			fos.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	public static String get(String key) {
		try {
			FileInputStream fileInputStream = new FileInputStream(countFilePath);
			pro.load(fileInputStream);
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pro.getProperty(key);
	}
	/**
	 * 解析指定字符串 "1-2,2-3,3-4"
	 * @return
	 */
	public static Map parseSpecifyString(String str){
		if(StringUtils.isBlank(str) || str.indexOf("-")==-1){
			return null;
		}
		Map map = new LinkedHashMap();
		StringTokenizer stoken = new StringTokenizer(str,",");
		while(stoken.hasMoreTokens()){
			String tmp = stoken.nextToken();
			String key = tmp.substring(0,tmp.indexOf("-")).trim();
			String value = tmp.substring(tmp.indexOf("-")+1,tmp.length()).trim();
			map.put(Integer.valueOf(key), Integer.valueOf(value));
//			System.out.println(tmp + "|" +key + "|" +value);
		}
		return map;
	}
}
