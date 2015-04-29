<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>map</title>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.4"></script>
<style type="text/css">
html,body {
	width: 100%;
	height: 100%;
	margin: 0;
	overflow: hidden;
}
</style>

</head>
<body>
	<div style="width: 100%; height: 100%; border: 1px solid gray"
		id="container"></div>
</body>
</html>
<%
	Map m= (Map) request.getAttribute("map");
	Set set = m.keySet();
	Iterator it = set.iterator();
	int i = 0;
	int x= 0 ;
	int size = set.size();
	Iterator it2 = set.iterator();
	Iterator it3 = set.iterator();	
	String lgt=(String)request.getAttribute("lgt");
	String lat=(String)request.getAttribute("lat");
%>
<script type="text/javascript">
	var map = new BMap.Map("container");//创建地图实例
	var point = new BMap.Point(<%=lgt%>,<%=lat%>);//创建中心点
	map.centerAndZoom(point,15);//初始化 地图 
	<%
		while(it2.hasNext()){
			String value=(String)it2.next();
			String arr[] = ((String)m.get(value)).split("&");
 			String lats =(String) arr[0];
 			String address =(String) arr[1];
 			String time =(String) arr[2]; 
			String phone=(String)arr[3];
			x++;
			%>
				
				var p=new BMap.Point(<%=value%>,<%=lats%>);
				var marker = new BMap.Marker(p);        // 创建标注     
				marker.setTitle("<%=address%> <%=time %> <%=phone%>");
				marker.setLabel("坐标");
				map.addOverlay(marker);                     // 将标注添加到地图中
				var infoWindow<%=x%> = new BMap.InfoWindow("<%=address%>,<%=time %>, <%=phone%>");
				marker.addEventListener("click", function(){this.openInfoWindow(infoWindow<%=x%>);});
			<%
		}
	%>
	//创建折线 图  描点   
	var polyline = new BMap.Polyline([ 
								 <%while (it.hasNext()) {
				String next=(String) it.next();
				String arr2[] = ((String)m.get(next)).split("&");
	 			String lats2 =(String) arr2[0];
				if (i == size - 1) {%>
							             new BMap.Point(<%=next%>,<%=lats2%>)
							            <%break;
				} else {
					i++;%>
							             new BMap.Point(<%=next%>,<%=lats2%>),
							            <%}
			}%>         
						   
	                      	       ], {
	                      		strokeColor : "blue",
	                      		strokeWeight : 6,
	                      		strokeOpacity : 0.5
	                      		
	                      	});
	
	map.addOverlay(polyline);
	map.addControl(new BMap.NavigationControl()); //左上角控件
    map.enableScrollWheelZoom(); //滚动放大
</script>
