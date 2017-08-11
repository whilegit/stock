package yjt.api.v1;

import com.jfinal.aop.Before;

import io.jpress.router.RouterMapping;

@RouterMapping(url="/v1")
@Before(AccessTokenInterceptor.class)
public class IndexController extends ApiBaseController {
	
	public void login(){
		renderJson(getReturnJson(Code.OK, "Hello login", EMPTY_OBJECT));
	}
	
	@Before(MemberTokenInterceptor.class)
	public void index(){
		renderJson(getReturnJson(Code.OK, "Hello Index", EMPTY_OBJECT));
	}
}
