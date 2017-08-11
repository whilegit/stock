package yjt.api.v1;

import java.math.BigInteger;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;

import io.jpress.model.User;
import io.jpress.model.query.UserQuery;

public class MemberTokenInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		// TODO Auto-generated method stub
		boolean pass = false;
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
