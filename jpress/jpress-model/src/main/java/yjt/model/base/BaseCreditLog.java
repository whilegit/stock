package yjt.model.base;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

import io.jpress.message.MessageKit;
import io.jpress.model.Metadata;
import io.jpress.model.core.JModel;
import io.jpress.model.query.MetaDataQuery;


public class BaseCreditLog<M extends BaseCreditLog<M>> extends JModel<M> implements IBean {

	private static final long serialVersionUID = 1L;
	
	
	public static final String CACHE_NAME = "creditlog";
	public static final String METADATA_TYPE = "creditlog";

	public static final String ACTION_ADD = "creditlog:add";
	public static final String ACTION_DELETE = "creditlog:delete";
	public static final String ACTION_UPDATE = "creditlog:update";
	
	public void removeCache(Object key){
		if(key == null) return;
		CacheKit.remove(CACHE_NAME, key);
	}

	public void putCache(Object key,Object value){
		CacheKit.put(CACHE_NAME, key, value);
	}

	public M getCache(Object key){
		return CacheKit.get(CACHE_NAME, key);
	}

	public M getCache(Object key,IDataLoader dataloader){
		return CacheKit.get(CACHE_NAME, key, dataloader);
	}

	public Metadata createMetadata(){
		Metadata md = new Metadata();
		md.setObjectId(getId());
		md.setObjectType(METADATA_TYPE);
		return md;
	}

	public Metadata createMetadata(String key,String value){
		Metadata md = new Metadata();
		md.setObjectId(getId());
		md.setObjectType(METADATA_TYPE);
		md.setMetaKey(key);
		md.setMetaValue(value);
		return md;
	}
	
	public boolean saveOrUpdateMetadta(String key,String value){
		Metadata metadata = MetaDataQuery.me().findByTypeAndIdAndKey(METADATA_TYPE, getId(), key);
		if (metadata == null) {
			metadata = createMetadata(key, value);
			return metadata.save();
		}
		metadata.setMetaValue(value);
		return metadata.update();
	}

	public String metadata(String key) {
		Metadata m = MetaDataQuery.me().findByTypeAndIdAndKey(METADATA_TYPE, getId(), key);
		if (m != null) {
			return m.getMetaValue();
		}
		return null;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null){ return false; }
		if(!(o instanceof BaseCreditLog<?>)){return false;}

		BaseCreditLog<?> m = (BaseCreditLog<?>) o;
		if(m.getId() == null){return false;}

		return m.getId().compareTo(this.getId()) == 0;
	}

	@Override
	public boolean save() {
		boolean saved = super.save();
		if (saved) { MessageKit.sendMessage(ACTION_ADD, this); }
		return saved;
	}

	@Override
	public boolean delete() {
		boolean deleted = super.delete();
		if (deleted) { MessageKit.sendMessage(ACTION_DELETE, this); }
		return deleted;
	}
	
	@Override
	public boolean deleteById(Object idValue) {
		boolean deleted = super.deleteById(idValue);
		if (deleted) { MessageKit.sendMessage(ACTION_DELETE, this); }
		return deleted;
	}

	@Override
	public boolean update() {
		boolean update = super.update();
		if (update) { MessageKit.sendMessage(ACTION_UPDATE, this); }
		return update;
	}
	
	public void setId(java.math.BigInteger id) {
		set("id", id);
	}

	public BigInteger getId() {
		Object id = get("id");
		if (id == null)
			return null;

		return id instanceof BigInteger ? (BigInteger)id : new BigInteger(id.toString());
	}
	
	public void setUserId(java.math.BigInteger userId) {
		set("user_id", userId);
	}

	public BigInteger getUserId() {
		Object userId = get("user_id");
		if(userId == null) return BigInteger.ZERO;
		return userId instanceof BigInteger ? (BigInteger)userId : new BigInteger(userId.toString());
	}
	
	public void setCreditType(int creditType){
		set("credit_type", creditType);
	}
	public int getCreditType(){
		return get("credit_type");
	}
	
	public void setPlatform(int platform){
		set("platform", platform);
	}
	public int getPlatform(){
		return get("platform");
	}
	
	public void setChange(double change) {
		set("change", change);
	}
	
	public Double getChange() {
		Object change = get("change");
		return change instanceof BigDecimal ? ((BigDecimal)change).doubleValue() : (Double)change;
	}
	
	public void setCur(double cur) {
		set("cur", cur);
	}
	
	public Double getCur() {
		Object cur = get("cur");
		return cur instanceof BigDecimal ? ((BigDecimal)cur).doubleValue() : (Double)cur;
	}
	
	public void setClerk(BigInteger clerk) {
		set("clerk", clerk);
	}
	public BigInteger getClerk() {
		Object val = get("clerk");
		if(val == null) return BigInteger.ZERO;
		return val instanceof Long ? BigInteger.valueOf((Long)val) : (BigInteger)val;
	}
	
	public void setLog(String log) {
		set("log", log);
	}
	public String getLog() {
		String log = get("log");
		return log != null ? log : "";
	}
	
	public void setCreateTime(java.util.Date createTime ){
		set("create_time", createTime);
	}
	public java.util.Date getCreateTime(){
		return get("create_time");
	}
}
