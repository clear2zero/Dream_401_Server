package sto.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sto.common.MlogPM;

public class StringReg {
	public static String replaceToHref(String s){
		//String url = "("+MlogPM.get("url.protocol") + "((?:" + MlogPM.get("url.hostname") + "|" + MlogPM.get("url.ip") + "))" + MlogPM.get("url.port") + MlogPM.get("url.path")+")";
		String url1 = "("+"(?:(mailto|ssh|ftp|https?)://)?" + "((?:" + "(?:[a-z0-9](?:[-a-z0-9]*[a-z0-9])?\\.)+(?:com|net|edu|biz|gov|org|in(?:t|fo)|(?-i:[a-z][a-z]))" + "|" + "(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])" + "))" + "(?::(\\d{1,5}))?" + "(/.*)?"+")";
		String url = "(<a.+>)?"+url1+"(</a>)?";
		Pattern p = Pattern.compile(url);
		Matcher m = p.matcher(s);
		while (m.find()) {
			String group0 = m.group();
			//String group1 = m.group(1);//a起始标签
			String group2 = m.group(2);//地址
			String group3 = m.group(3);//协议
			if(group3 != null){
				s = s.replace(group0, "<a href='"+group2+"' target='_blank'>"+group2+"</a>");
			}else {
				s = s.replace(group0, "<a href='http://"+group2+"' target='_blank'>"+group2+"</a>");
			}
			  
		}
		return s;
	}
	
	public static void main(String[] args){
		/*String url1 = "("+"(?:(mailto|ssh|ftp|https?)://)?" + "((?:" + "(?:[a-z0-9](?:[-a-z0-9]*[a-z0-9])?\\.)+(?:com|net|edu|biz|gov|org|in(?:t|fo)|(?-i:[a-z][a-z]))" + "|" + "(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])" + "))" + "(?::(\\d{1,5}))?" + "(/.*)?"+")";
		String url = "(<a.+>)?"+url1+"(</a>)?";
		//String url2 = "(</a>)";
		Pattern p = Pattern.compile(url);
		String s = "阿红的咖啡哈斯卡电话费<a href='baidu.com'>baidu.com</a>水水水水对方的开发商的可持续是电风扇的";
		 Matcher m = p.matcher(s);
		 StringBuffer sb = new StringBuffer();
		 while (m.find()) {
				String group0 = m.group();
				String group1 = m.group(1);//a起始标签
				String group2 = m.group(2);//地址
				String group3 = m.group(3);//协议
				if(group3 != null){
					s = s.replace(group0, "<a href='"+group2+"' target='_blank'>"+group2+"</a>");
				}else {
					s = s.replace(group0, "<a href='http://"+group2+"' target='_blank'>"+group2+"</a>");
				}
				System.out.println(s.toString()); 
				System.out.println(m.group(7)); 
			}*/
		 //m.appendTail(sb);
		 //System.out.println(sb.toString());
	}
}


