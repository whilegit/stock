package yjt.api.v1;

import io.jpress.router.RouterMapping;

@RouterMapping(url="/v1")
public class IndexController extends ApiBaseController {
	
	
	public void login(){
		renderJson(getReturnJson(Code.OK, "Hello login", EMPTY_OBJECT));
	}
}
