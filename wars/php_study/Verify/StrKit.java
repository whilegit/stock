package Verify;

public class StrKit {
	public static boolean isBlank(String str){
		return str == null || "".equals(str) == true;
	}
	
	public static boolean notBlank(String ... ary){
		if(ary == null || ary.length == 0){
			return false;
		}
		for(String str : ary){
			if(isBlank(str)){
				return false;
			}
		}
		return true;
	}
}
