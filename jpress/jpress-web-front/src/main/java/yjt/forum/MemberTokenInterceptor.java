package yjt.forum;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;

import io.jpress.core.BaseFrontController;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;

public class MemberTokenInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		// TODO Auto-generated method stub
		boolean pass = false;
		HttpServletRequest request = inv.getController().getRequest();
		request.setAttribute("invocation", inv);
		
		String memberIDStr = request.getHeader("memberID");
		String memberToken = request.getHeader("memberToken"); 
		
		if(StrKit.notBlank(memberIDStr) && StrKit.notBlank(memberToken)){
			long userId = Long.parseLong(memberIDStr);
			User user = UserQuery.me().findByIdNoCache(BigInteger.valueOf(userId));
			if(user != null && memberToken.equals(user.getMemberToken())){
				request.setAttribute("memberID", user.getId());
				pass = true;
			}
		}
		
		if(pass) {
			inv.invoke();
		}
		else{
			BaseFrontController bc = (BaseFrontController) inv.getController();
			if(bc.isAjaxRequest()) {
				bc.renderAjaxResult("未登陆", -1);
			} else {
				bc.renderText("未登陆");
			}
		}
	}
}
