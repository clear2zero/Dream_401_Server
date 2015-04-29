package sto.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
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

public class HttpOnline {
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
	
	public static JSONObject invoke(OnlineIType onlineIType,Map<String,String> map){
		httpclient = new DefaultHttpClient();
		JSONObject json = new JSONObject();
		//String smsUrl = MlogPM.get("online.url")+onlineIType.getName();
		String smsUrl = "http://hebcaonline.hebca.com:9001/Hebca/interface/"+onlineIType.getName();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Entry<String,String> entry = it.next();
			nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		try {
			HttpPost httppost = new HttpPost(smsUrl); 
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {  
				String msg = getHeaderValue(response,"errorMessage");
				msg = URLDecoder.decode(msg, "utf-8");
				if(msg != null && !msg.equals("")){
					json.put("success", false);
					json.put("msg", msg);
				}else {
					BufferedReader in = null;
					in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

					StringBuffer sb = new StringBuffer("");
					String line = "";

					while ((line = in.readLine()) != null) {
						sb.append(line);
					}

					String ret = sb.toString().trim();
					msg = URLDecoder.decode(sb.toString(), "utf-8");
					json = JSON.parseObject(msg);
				}
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
	
	public enum OnlineIType{
		GETCHECKCODE {public String getName(){return "getCheckCode.action";}}, 
		GETAPPLYFORM {public String getName(){return "getApplyForm.action";}},
		GETAPPLYDATA {public String getName(){return "getApplyData.action";}},
		GETCHARGEPLAN {public String getName(){return "getChargePlan.action";}},
		ACCEPTNOQUERY {public String getName(){return "acceptNoQuery.action";}},
		ACCEPTNOQUERYBYSERIAL {public String getName(){return "acceptNoQueryBySerial.action";}},
		GETAPPLYDATAALL {public String getName(){return "getApplyDataAll.action";}},
		UNITAPPLYQUERY {public String getName(){return "unitApplyQuery.action";}},
		CERTLISTQUERY {public String getName(){return "certListQuery.action";}},
		CERTSTATUSQUERY {public String getName(){return "certStatusQuery.action";}},
		
		CERTAPPLY {public String getName(){return "certApply.action";}},
		CHARGEPLANAPPLY {public String getName(){return "chargeplanApply.action";}},
		CERTBATCHAPPLY {public String getName(){return "certBatchApply.action";}},
		CERTBATCHAUDIT {public String getName(){return "certBatchAudit.action";}},
		CERTAUDIT {public String getName(){return "certAudit.action";}},
		CERTINSTALL {public String getName(){return "certInstall.action";}},
		CERTINSTALLCALLBACK {public String getName(){return "certInstallCallBack.action";}},
		CERTINSTALLCHECK {public String getName(){return "certInstallCheck.action";}},
		CERTINSTALLSOFT {public String getName(){return "certInstallSoft.action";}},
		CERTINSTALLEDSKIP {public String getName(){return "certInstalledSkip.action";}},
		CERTDOWNLOAD {public String getName(){return "certDownLoad.action";}},
		INSTALLSOFTCERT {public String getName(){return "installSoftCert.action";}},
		INSERTPROJECT {public String getName(){return "insertProject.action";}},
		INITUNIT {public String getName(){return "initUnit.action";}};
		
	    public abstract String getName();
	}
	
	public static void main(String[] args){
		Map<String,String> map = new HashMap<String,String>();
		map.put("projectid", "402849ee4a4d162c014a51a870a200f1");
		map.put("businesstype", "04");
		JSONObject json = HttpOnline.invoke(OnlineIType.GETAPPLYFORM, map);
		if(json.getBooleanValue("success") == false){
			System.out.println(json.getString("msg"));
		}else {
			System.out.println(json.toJSONString());
		}
	}
	
}
