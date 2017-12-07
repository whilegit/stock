package yjt.core.perm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.render.Render;

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
				// 角色超级管理员，任何接口都可调用
				pass = PermKit.permCheck(pa, user);
			}
		}
		if(pass) inv.invoke();
		else {
			Controller controller = inv.getController();
			HttpServletRequest request = controller.getRequest();
			HttpServletResponse response = controller.getResponse();
			String ref = request.getHeader("referer");
			
			controller.setAttr("perm", pa.value());
			controller.setAttr("ref", ref);
			controller.render("not_permit.html");
			Render render = controller.getRender();
			render.setContext(request, response, "/WEB-INF/admin/").render();
		}
	}
}
