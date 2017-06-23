package yjt.core.perm;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import yjt.core.perm.PermAnnotation;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils; 
/**
 * 权限组件
 * @author lzr
 */
public class PermKit {
	
	static Log log = Log.getLog(PermKit.class);
	private static Map<String, String> permMap = new HashMap<String, String>();
	private static PermGroup[] permGroups = null;
	
	public static void init() {
		String path = PathKit.getRootClassPath() + "/perm.json";
		File f = new File(path);
		if(f.exists() == false) {
			log.error("perm.json文件没有找到，路径：" + path);
			return;
		}
		try {
			String content = FileUtils.readFileToString(f, "utf-8");
			if(StrKit.notBlank(content)){
				List<PermGroup> raw = JSONObject.parseArray(content, PermGroup.class);
				if(raw == null){
					log.error("perm.json文件解析错误，路径：" + path);
					return;
				}
				permGroups = raw.toArray(new PermGroup[raw.size()]);
				Iterator<PermGroup> iter = raw.iterator();
				while( iter.hasNext() ){
					PermGroup pg = iter.next();
					List<PermGroup.Perm> list = pg.getPerms();
					if(list != null && list.size() > 0){
						Iterator<PermGroup.Perm> iter1 = list.iterator();
						while(iter1.hasNext()){
							PermGroup.Perm e = iter1.next();
							permMap.put(e.getKey(), e.getDesc());
						}
					}
				}
				log.info("完成权限列表初始化操作");
				System.out.println(JSONObject.toJSONString(permGroups));
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("perm.json文件读取或解析错误，路径：" + path);
		}
	}
	
	public static Map<String,String> getPermMap(){
		return permMap;
	}
	
	public static boolean isLegalPerm(String p){
		boolean ret = false;
		if(StrKit.notBlank(p)){
			ret = permMap.containsKey(p);
		}
		return ret;
	}
	
	public static PermGroup[] getCopyofPermGroups(){
		PermGroup[] newPermGroup = new PermGroup[permGroups.length];
		for(int i = 0; i<permGroups.length; i++){
			try {
				newPermGroup[i] = (PermGroup) permGroups[i].clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				log.error("克隆权限列表时出错");
			}
		}
		return newPermGroup;
	}
	
	/**
	 * 权限比较函数（用户的权限列表应包含action所要求的权限)
	 * @param pa 注解要求的权限(使用PermAnnotation注解，以逗号分隔)
	 * @param usrPermStr　用户所拥有的权限（存放于user表的perm字段，以逗号分隔）
	 * @return　boolean
	 */
	public static boolean permCheck(PermAnnotation pa, String usrPermStr){
		boolean check = false;
		String paStr = pa.value();
		if(StrKit.notBlank(paStr)){
			if(StrKit.notBlank(usrPermStr)){
				String[] must = paStr.split(",");
				List<String> have = Arrays.asList(usrPermStr.split(","));
				for(String m : must){
					if(have.contains(m) == false){
						check = false;
						break;
					}
				}
			}else{
				check = false;
			}
		}else{
			check = true;
		}
		return check;
	}
}
