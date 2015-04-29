/*
名称：服务器时钟（一次读取，实时显示）
 功能：在客户端浏览器上显示服务器端的时间。
 原理：    
     算法步骤：
    1. 获取服务端的日期时间。
    2. 根据客户端浏览器的时间可以得到服务器和客户端的时间差。
    3. 服务器的时钟 = 客户端的时钟（变化值）+ 时间差（固定值）
    
     这样客户端就没有必要实时的到服务器端去取时间。

 作者：三月三
 来源：http://www.cnblogs.com/march3/archive/2009/05/14/1456720.html
说明：
    1. 多浏览器支持
    2. 由于网络延时无法估计的原因，会有一定的误差。
        用户可以通过 set_delay() 方法来减少误差。
 参数：
    s_year, s_month, s_day, s_hour, s_min, s_sec  
     分别为服务器端的 年 月 日 时 分 秒，

    例如：2008,9,19,0,9,0 表示 2008年9月19日 0点9分0秒
*/
function ServerClock(s_year,s_month,s_day,s_hour,s_min,s_sec) {
	//估计从服务器下载网页到达客户端的延时时间，默认为1秒。
    var _delay = 1000;
     
    //服务器端的时间
    var serverTime = null;
    if(arguments.length == 0){
	    //没有设置服务器端的时间，按当前时间处理
	    serverTime = new Date(); 
	    _delay = 0;
	}else{
		serverTime = new Date(s_year,s_month,s_day,s_hour,s_min,s_sec);
	}
	
    //客户端浏览器的时间
	var clientTime = new Date();
	//获取时间差
	var _diff = serverTime.getTime() - clientTime.getTime(); 
	
	//设置从服务器下载网页到达客户端的延时时间，默认为1秒。
	this.set_delay = function(value){_delay=value;};
	
	//获取服务的日期时间
	this.get_ServerTime = function(formatstring) {
	     clientTime = new Date();
	     serverTime.setTime(clientTime.getTime()+_diff+_delay);
	     if(formatstring == null)
	         return serverTime;
	     else
	         return serverTime.pattern(formatstring);
	 };    
 }

Date.prototype.pattern=function(fmt) { 
    var o = {         
    //"M+" : this.getMonth()+1, //月份         
    "d+" : this.getDate(), //日         
    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时         
    "H+" : this.getHours(), //小时         
    "m+" : this.getMinutes(), //分         
    "s+" : this.getSeconds(), //秒         
    "q+" : Math.floor((this.getMonth()+3)/3), //季度         
    "S" : this.getMilliseconds(), //毫秒
    "a" : this.getHours() <= 12 ? "AM" : "PM"
    };         
    var week = {         
    "0" : "\u65e5",         
    "1" : "\u4e00",         
    "2" : "\u4e8c",         
    "3" : "\u4e09",         
    "4" : "\u56db",         
    "5" : "\u4e94",         
    "6" : "\u516d"        
    };
    var month = {         
    "0" : "\u65e5",         
    "1" : "\u4e00",         
    "2" : "\u4e8c",         
    "3" : "\u4e09",         
    "4" : "\u56db",         
    "5" : "\u4e94",         
    "6" : "\u4E03",
    "7" : "\u516B",
    "8" : "\u4E5D",
    "9" : "\u5341",
    "10" : "\u5341\u4E00",
    "11" : "\u5341\u4E8C"
    };
    if(/(y+)/.test(fmt)){         
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));         
    }         
    if(/(E+)/.test(fmt)){         
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "\u661f\u671f" : "\u5468") : "")+week[this.getDay()+""]);         
    }
    if(/(M+)/.test(fmt)){         
        fmt=fmt.replace(RegExp.$1, month[(this.getMonth()+1)+""]+"\u6708");         
    }
    for(var k in o){     
        if(new RegExp("("+ k +")").test(fmt)){         
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));         
        }         
    }         
    return fmt;         
} 