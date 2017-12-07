package yjt.api.v1.Interceptor;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;

import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import yjt.api.v1.ApiBaseController;

public class MemberTokenInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		// TODO Auto-generated method stub
		boolean pass = false;
		HttpServletRequest request = inv.getController().getRequest();
		request.setAttribute("invocation", inv);
		String memberIDStr = inv.getController().getPara("memberID"); 
		String memberToken = inv.getController().getPara("memberToken");
		if(StrKit.notBlank(memberIDStr) && StrKit.notBlank(memberToken)){
			long userId = Long.parseLong(memberIDStr);
			User user = UserQuery.me().findByIdNoCache(BigInteger.valueOf(userId));
			if(user != null && memberToken.equals(user.getMemberToken())){
				pass = true;
			}
		}
		
		if(pass) inv.invoke();
		else{
			ApiBaseController bc = (ApiBaseController) inv.getController();
			bc.memberTokenFail();
		}
	}
}
