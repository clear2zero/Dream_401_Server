package sto.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class HttpUtil {
	private static HttpClient httpclient = null; 
	private static String getHeaderValue(HttpResponse response, String headerName) {
		Header[] headers = response.getHeaders(headerName);
		if (headers.length != 0) {
			String header = headers[0].toString();
			String[] headerArr = header.split(":");
			if (headerArr.length != 2)
				return "";
			else
				return headerArr[1].trim();
		} else
			return "";
	}
	
	public static JSONObject invoke(String serverurl,Map<String,String> map){
		httpclient = new DefaultHttpClient();
		JSONObject json = new JSONObject();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Entry<String,String> entry = it.next();
			nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		try {
			HttpPost httppost = new HttpPost(serverurl); 
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {  
				//String msg = getHeaderValue(response,"errorMessage");
				BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				StringBuffer sb = new StringBuffer("");
				String line = "";

				while ((line = in.readLine()) != null) {
					sb.append(line);
				}

				String ret = sb.toString().trim();
				String msg = new String(sb.toString().getBytes("gbk"),"utf-8");//URLDecoder.decode(sb.toString().getBytes("utf-8"), "gbk");
				json = JSON.parseObject(msg);
			} else {  
				String err = response.getStatusLine().getStatusCode()+"";  
				json.put("success", false);
				json.put("msg", err);
			}  
		} catch (ClientProtocolException e) {
			json.put("success", false);
			json.put("msg", e.getMessage());
			e.printStackTrace(); 
		} catch (IOException e) {
			json.put("success", false);
			json.put("msg", e.getMessage());
			e.printStackTrace();
		}finally{
			httpclient.getConnectionManager().shutdown();
		}
		return json;
	}
}
