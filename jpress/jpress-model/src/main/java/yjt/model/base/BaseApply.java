package yjt.model.base;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

import io.jpress.message.MessageKit;
import io.jpress.model.Metadata;
import io.jpress.model.core.JModel;
import io.jpress.model.query.MetaDataQuery;

public class BaseApply<M extends BaseApply<M>> extends JModel<M> implements IBean {

	private static final long serialVersionUID = 1L;
	
	
	public static final String CACHE_NAME = "apply";
	public static final String METADATA_TYPE = "apply";

	public static final String ACTION_ADD = "apply:add";
	public static final String ACTION_DELETE = "apply:delete";
	public static final String ACTION_UPDATE = "apply:update";
	
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
		if(!(o instanceof BaseApply<?>)){return false;}

		BaseApply<?> m = (BaseApply<?>) o;
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

	public java.math.BigInteger getId() {
		Object id = get("id");
		if (id == null)
			return null;

		return id instanceof BigInteger ? (BigInteger)id : new BigInteger(id.toString());
	}
	
	public void setApplyUid(java.math.BigInteger applyUid) {
		set("apply_uid", applyUid);
	}

	public java.math.BigInteger getApplyUid() {
		Object apply_uid = get("apply_uid");
		if (apply_uid == null) return null;
		return apply_uid instanceof BigInteger ? (BigInteger)apply_uid : new BigInteger(apply_uid.toString());
	}

	public void setAmount(double amount){
		set("amount", amount);
	}
	public BigDecimal getAmount(){
		return get("amount");
	}
	
	public void setAnnualRate(double annualRate ){
		set("annual_rate", annualRate);
	}
	public BigDecimal getAnnualRate(){
		return get("annual_rate");
	}
	
	public void setRepaymentMethod(int repaymentMethod){
		set("repayment_method", repaymentMethod);
	}
	public int getRepaymentMethod(){
		return get("repayment_method");
	}
	
	public void setValueDate(java.util.Date valueDate ){
		set("value_date", valueDate);
	}
	public java.util.Date getValueDate(){
		return get("value_date");
	}
	
	public void setMaturityDate(java.util.Date maturityDate){
		set("maturity_date", maturityDate);
	}
	public java.util.Date getMaturityDate(){
		return get("maturity_date");
	}
	
	public void setPurpose(int purpose){
		set("purpose", purpose);
	}
	public int getPurpose(){
		return get("purpose");
	}
	
	public void setDescription(String description){
		set("description", description);
	}
	public String getDescription(){
		return get("description");
	}
	
	public void setToFriends(List<BigInteger> toFriends){
		StringBuilder sb = new StringBuilder();
		
		if(toFriends != null && toFriends.size() > 0){
			boolean first = true;
			for(BigInteger id : toFriends){
				if(first == true) first = false;
				else sb.append(",");

				sb.append(id.toString());
			}
		}
		set("to_friends", sb.toString());
	}
	public List<BigInteger> getToFriends(){
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		String str =  get("to_friends");
		if(StrKit.notBlank(str)){
			String[] ary = str.split(",");
			for(String idStr : ary){
				if(StrKit.isBlank(idStr)) continue;
				Long l = Long.valueOf(idStr);
				BigInteger id = BigInteger.valueOf(l);
				ret.add(id);
			}
		}
		return ret;
	}
	
	public void setVideo(String video){
		set("video", video);
	}
	public String getVideo(){
		return get("video");
	}
	
	public void setCreateTime(java.util.Date createTime ){
		set("create_time", createTime);
	}
	public java.util.Date getCreateTime(){
		return get("create_time");
	}
	
	public void setStatus(int status){
		set("status", status);
	}
	public int getStatus(){
		return get("status");
	}
	
}
