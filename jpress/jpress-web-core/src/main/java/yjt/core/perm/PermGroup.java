package yjt.core.perm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 权限分组
 * @author lzr
 *
 */
public class PermGroup implements Cloneable{
	public String key;
	public String desc;
	public List<Perm> perms;
	
	public String getKey() {	return key;	}
	public void setKey(String key) {	this.key = key;	}

	public String getDesc() {	return desc;	}
	public void setDesc(String desc) {	this.desc = desc;	}
	public List<Perm> getPerms() {	return perms;	}
	public void setPerms(List<Perm> perms) {	this.perms = perms;	}
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return (o == null || !(o instanceof PermGroup)) ? false : ((PermGroup)o).key.equals(this.key);
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		if(this.key != null)
			return this.key.hashCode();
		else return super.hashCode();
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		PermGroup pg = new PermGroup();
		pg.setKey(key);
		pg.setDesc(desc);
		List<Perm> newlist = new ArrayList<Perm>();
		Iterator<Perm> iter = perms.iterator();
		while(iter.hasNext()){
			Perm p = iter.next();
			newlist.add((Perm) p.clone());
		}
		pg.setPerms(newlist);

		return pg;
	}
	
	public static class Perm implements Cloneable{
		private String key;
		private String desc;
		private boolean checked = false; 	//为Freemarker使用
		
		public String getKey() {	return key;	}
		public void setKey(String key) {	this.key = key;	}
		public String getDesc() {	return desc;	}
		public void setDesc(String desc) {	this.desc = desc;	}
		public boolean getChecked(){return checked;}
		public void setChecked(boolean checked){ this.checked = checked;}
		
		@Override
		protected Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			Perm p = new Perm();
			p.setKey(key);
			p.setDesc(desc);
			p.setChecked(false);
			return p;
		}
	}
}
