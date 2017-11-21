package yjt.core.Utils;

import java.util.ArrayList;

import com.jfinal.kit.StrKit;

import io.jpress.model.query.OptionQuery;

public class Common {
	protected static ArrayList<String> sensitiveTable = null;
	
	public static void loadSensitive() {
		String content = OptionQuery.me().findValueNoCache("sensitive");
		content = content.replaceAll("[\r\n\t]", "");
		String[] words = content.split(",");
		sensitiveTable = new ArrayList<String>(4068);
		for(int i = 0; i<words.length; i++) {
			String word = words[i].trim();
			if(StrKit.notBlank(word)) {
				sensitiveTable.add(word);
			}
		}
	}
	
	public static boolean checkSensitive(String s) {
		if(StrKit.isBlank(s)) return false;
		if(sensitiveTable == null) {
			loadSensitive();
		}
		if(sensitiveTable == null || sensitiveTable.size() == 0) return false;
		
		for(String word : sensitiveTable) {
			if(s.contains(word)) return true;
		}
		return false;
	}
}
