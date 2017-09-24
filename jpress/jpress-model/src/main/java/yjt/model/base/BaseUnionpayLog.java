package yjt.model.base;

import java.math.BigInteger;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

import io.jpress.message.MessageKit;
import io.jpress.model.Metadata;
import io.jpress.model.core.JModel;
import io.jpress.model.query.MetaDataQuery;


public class BaseUnionpayLog<M extends BaseUnionpayLog<M>> extends JModel<M> implements IBean {

	private static final long serialVersionUID = 1L;
	
	
	public static final String CACHE_NAME = "unionpaylog";
	public static final String METADATA_TYPE = "unionpaylog";

	public static final String ACTION_ADD = "unionpaylog:add";
	public static final String ACTION_DELETE = "unionpaylog:delete";
	public static final String ACTION_UPDATE = "unionpaylog:update";
	
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
		if(!(o instanceof BaseUnionpayLog<?>)){return false;}

		BaseUnionpayLog<?> m = (BaseUnionpayLog<?>) o;
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
	
	public void setPaySn(String paySn) {
		set("pay_sn", paySn);
	}
	
	public String getPaySn() {
		return get("pay_sn");
	}
	
	public void setUserId(java.math.BigInteger userId) {
		set("user_id", userId);
	}

	public BigInteger getUserId() {
		Object userId = get("user_id");
		if(userId == null) return BigInteger.ZERO;
		return userId instanceof BigInteger ? (BigInteger)userId : new BigInteger(userId.toString());
	}
	
	public void setFee(double fee) {
		set("fee", fee);
	}
	
	public Double getFee() {
		return get("fee");
	}
	
	public void setCreateTime(java.util.Date createTime ){
		set("create_time", createTime);
	}
	public java.util.Date getCreateTime(){
		return get("create_time");
	}
	
	public void setTn(String tn) {
		set("tn", tn);
	}
	public String getTn() {
		return get("tn");
	}
	
	public void setStatus(int status){
		set("status", status);
	}
	public int getStatus(){
		return get("status");
	}
	
	public void setUpdateTime(java.util.Date updateTime ){
		set("update_time", updateTime);
	}
	public java.util.Date getUpdateTime(){
		return get("update_time");
	}
}
