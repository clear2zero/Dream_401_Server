/**   
 * @Title: BaseAPIModel.java 
 * @Package sto.common.model 
 * @author chenxiaojia  
 * @date 2014-9-25 下午2:31:28 
 * @version V1.0   
 */
package sto.common.model;

/**
 * @ClassName: BaseAPIModel
 * @Description:
 * @author chenxiaojia
 * @date 2014-9-25 下午2:31:28
 * 
 */
public class BaseAPIModel {
	private int code=0;
	private String message;
	private Object data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
