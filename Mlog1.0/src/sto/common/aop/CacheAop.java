package sto.common.aop;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import sto.common.annotation.CacheInfo;
import sto.service.account.SysSettingsService;
import sto.utils.CacheUtils;

@Component//通过spring配置component-scan自动加载注册
@Aspect//定义一个切面 相当于aop:aspect
public class CacheAop {
	/*@Before("execution(* sto.service.account.UserService.save*(..))")
	public void before(){
		System.out.println("before");
		
	}
	@After("execution(* sto.service.account.UserService.save*(..))")
	public void after(){
		System.out.println("after");
	}*/
	/*@Around("execution(* sto.web.account.*.*(..))")
	public Object cache(ProceedingJoinPoint point) throws Throwable{
		System.out.println("------------aop :: cache-------------");
		String methodname = point.getSignature().getName();
		MethodSignature joinPoint = (MethodSignature) point.getSignature();
		Method method = joinPoint.getMethod();
		boolean isExistAnnotation = method.isAnnotationPresent(CacheInfo.class);
		Object result = null;
		if(isExistAnnotation){
			CacheInfo info = method.getAnnotation(CacheInfo.class);
			String name = info.name();//缓存名称
			String isSign = info.isSign();//是否试用缓存
			String key = info.key();
			if(isSign != null && !isSign.equals("")){
				if(isSign.equals("true")){
					System.out.println(name+"使用缓存");
					if(!name.equals("")){
						result = CacheUtils.get(name,key);
						if(result ==  null){
							System.out.println(name+":"+key+"缓存为空，直接查询");
							result = point.proceed();
							CacheUtils.put(name,key,result);
						}
					}else{
						result = point.proceed();
					}
				}else {
					System.out.println(name+":"+key+"不使用缓存，直接查询");
					result = point.proceed();
					CacheUtils.put(name,key,result);
				}
			}else {
				result = point.proceed();
				CacheUtils.removeAll(name);
			}
		}else {
			result = point.proceed();
		}
		return result;
	}*/
}
