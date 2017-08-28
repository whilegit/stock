package yjt.api.v1;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfinal.kit.StrKit;

public class Utils {
	protected static final SimpleDateFormat sdfYmd = new SimpleDateFormat("yyyy-MM-dd");
	protected static final SimpleDateFormat sdfYmdHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected static final Pattern MOBILE_PATTERN = Pattern.compile("^1[345789][0-9]{9}$");
	
	public static long getTodayStartTime(){
		Date d = new Date();
		String t = sdfYmd.format(d) + " 00:00:00";
		try {
			d = sdfYmdHms.parse(t);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d.getTime();
	}
	
	public static Date getYmd(String s){
		Date d = null;
		try{
			d = sdfYmd.parse(s);
		} catch (Exception e){
			//
		}
		return d;
	}
	
	/**
	 * 拆分以逗号隔开的BigInteger组成的字符串
	 * @param source
	 * @param delimiter
	 * @param filter 是否过滤相同的值
	 * @return
	 */
	public static ArrayList<BigInteger> splitToBigInteger(String source, String delimiter, boolean filter){
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		if(StrKit.notBlank(source, delimiter)){
			String[] ary = source.split(delimiter);
			if(ary != null && ary.length > 0){
				for(String s : ary){
					long l = Long.parseLong(s);
					BigInteger bi = BigInteger.valueOf(l);
					if(filter && ret.contains(bi)){
						continue;
					} else {
						ret.add(bi);
					}
				}
			}
		}
		return ret;
	}
	
	
	public static boolean isMobile(String mobile){
		boolean ret = false;
		if(StrKit.notBlank(mobile)){
			Matcher m = MOBILE_PATTERN.matcher(mobile);
			ret = m.matches();
		}
		return ret;
	}
	
	
}
