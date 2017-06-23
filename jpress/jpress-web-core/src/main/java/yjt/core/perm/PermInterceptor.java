package yjt.core.perm;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import io.jpress.interceptor.InterUtils;
import io.jpress.model.User;

import yjt.core.perm.PermAnnotation;

/**
 * 全局权限检查拦截器（在AdminInterceptor之后调用）
 * @author lzr
 */
public class PermInterceptor implements Interceptor{

	/**
	 * 页面访问权限检查（仅action不要求权限或登陆用户拥有权限才不被拦截）
	 * @Override
	 */
	public void intercept(Invocation inv) {
		// TODO Auto-generated method stub
		boolean pass = false;
		PermAnnotation pa = inv.getMethod().getAnnotation(PermAnnotation.class);
		if(pa == null) {
			pass = true;
		} else{
			pass = false;
			User user = InterUtils.tryToGetUser(inv);
			if(user != null){
				// 角色为Administrator时为超级管理员，任何接口都可调用
				String role = user.getRole();
				if("administrator".equalsIgnoreCase(role)) pass = true;
				else pass = PermKit.permCheck(pa, user.getPerm());
			}
		}
		if(pass) inv.invoke();
		else inv.getController().redirect("/admin/unauthority");
	}
}
