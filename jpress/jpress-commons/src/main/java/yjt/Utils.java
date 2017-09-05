package yjt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
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
	protected static final SimpleDateFormat sdfNumber = new SimpleDateFormat("yyyyMMddHHmmss");
	protected static final Pattern MOBILE_PATTERN = Pattern.compile("^1[345789][0-9]{9}$");
	
	public static long getTodayStartTime(){
		Date d = new Date();
		String t = sdfYmd.format(d) + " 00:00:00";
		try {
			d = sdfYmdHms.parse(t);
		} catch (ParseException e) {
		}
		return d.getTime();
	}
	
	public static long getDayStartTime(Date d) {
		String t = sdfYmd.format(d) + " 00:00:00";
		try {
			d = sdfYmdHms.parse(t);
		} catch (ParseException e) {
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
	
	protected static final DecimalFormat DF0_00   =new DecimalFormat("#0.00");
	public static String bigDecimalRound2(BigDecimal bd){
		return DF0_00.format(bd);
	}
	
	public static String toYmd(Date d) {
		if(d == null) return "";
		return sdfYmd.format(d);
	}
	
	public static String toYmdHms(Date d) {
		if(d == null) return "";
		return sdfYmdHms.format(d);
	}
	
	public static String getDayNumber(Date d) {
		if(d == null) return "";
		return sdfNumber.format(d);
	}
	
	public static int random(int min, int max) {
		return (int) (Math.random() * (max -min) + min);
	}
	
	public static int days(Date fromDate, Date toDate) {
		long internal = (getDayStartTime(toDate) - getDayStartTime(fromDate)) / 1000;
		return (int) (internal / 86400);
	}
	
	public static String getFileExtention(String path){
		int len = path.length();
		int last_slash = path.lastIndexOf('/');
		if(last_slash == -1 || last_slash >= len -1){
			return "";
		}
		String filename = path.substring(last_slash + 1, len);
		int lash_dot = filename.lastIndexOf('.');
		if(lash_dot == -1 || lash_dot >= len -1){
			return "";
		}
		return filename.substring(lash_dot+1, filename.length());
	}
}
