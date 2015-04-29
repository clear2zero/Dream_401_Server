package sto.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	
	public static String dateToStr(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}
	
	// 显示/隐藏
		public static final String SHOW = "1";
		public static final String HIDE = "0";
		
		// 是/否
		public static final String YES = "1";
		public static final String NO = "0";

		// 删除标记（0：停用、删除；1：正常；）
		public static final String FIELD_DEL_FLAG = "delFlag";
		public static final int DEL_FLAG_NORMAL = 1;
		public static final int DEL_FLAG_DELETE = 0;
		
		//分页初始化页数和每页条数
		public static final int PAGE = 1;
		public static final int ROWS = 10;
}
