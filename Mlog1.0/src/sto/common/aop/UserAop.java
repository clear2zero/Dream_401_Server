package sto.common.aop;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import sto.model.account.SysSettings;
import sto.service.account.SysSettingsService;

@Component//通过spring配置component-scan自动加载注册
@Aspect//定义一个切面 相当于aop:aspect
public class UserAop {
	@Resource
	SysSettingsService sysSettingsService;
	/*@Before("execution(* sto.service.account.UserService.save*(..))")
	public void before(){
		System.out.println("before");
		
	}
	@After("execution(* sto.service.account.UserService.save*(..))")
	public void after(){
		System.out.println("after");
	}*/
	/*@Around("execution(* sto.service.account.UserService.save*(..))||"+ 
			"execution(* sto.service.account.UserService.update*(..))||"+ 
			"execution(* sto.service.account.UserService.delete*(..))")
	public Object userAround(ProceedingJoinPoint point) throws Throwable{
		//获取目标类名
		Object result = point.proceed();
		SysSettings sysSettings = sysSettingsService.findUniqueBy("skey", "lastupdated");
		sysSettings.setValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.out.println("更新通讯录最后更新时间");
		sysSettingsService.save(sysSettings);
		return result;
	}*/
}
