package yjt.model.query;

import java.math.BigInteger;
import java.util.LinkedList;

import com.jfinal.kit.StrKit;

import io.jpress.model.query.JBaseQuery;
import yjt.model.Captcha;

public class CaptchaQuery extends JBaseQuery{
	protected static final Captcha DAO = new Captcha();
	private static final CaptchaQuery QUERY = new CaptchaQuery();
	
	public static CaptchaQuery me(){
		return QUERY;
	}
	
	public Captcha getCaptcha(String mobile){
		if(StrKit.isBlank(mobile)) return null;
		
		StringBuilder sqlBuilder = new StringBuilder();
		LinkedList<Object> params = new LinkedList<Object>();

		appendAndIfNotEmpty(sqlBuilder, "mobile", mobile, params);
		sqlBuilder.append(" ORDER BY id DESC Limit 1");

		Captcha captcha = DAO.doFindFirst(sqlBuilder.toString(), params.toArray());
		return captcha;

	}
}
