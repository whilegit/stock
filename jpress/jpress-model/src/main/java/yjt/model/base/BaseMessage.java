package yjt.model.base;

import java.math.BigInteger;
import java.util.Date;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

import io.jpress.message.MessageKit;
import io.jpress.model.Metadata;
import io.jpress.model.core.JModel;
import io.jpress.model.query.MetaDataQuery;

public class BaseMessage<M extends BaseMessage<M>> extends JModel<M> implements IBean {

	private static final long serialVersionUID = 1L;
	
	
	public static final String CACHE_NAME = "message";
	public static final String METADATA_TYPE = "message";

	public static final String ACTION_ADD = "message:add";
	public static final String ACTION_DELETE = "message:delete";
	public static final String ACTION_UPDATE = "message:update";
	
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

	public BigInteger getId() {
		Object id = get("id");
		if (id == null)
			return null;

		return id instanceof BigInteger ? (BigInteger)id : new BigInteger(id.toString());
	}
	
	public void setToUserid(BigInteger toUserId) {
		set("to_userid", toUserId);
	}

	public BigInteger getToUserId() {
		Object toUserId = get("to_userid");
		if (toUserId == null)
			return null;

		return toUserId instanceof BigInteger ? (BigInteger)toUserId : new BigInteger(toUserId.toString());
	}
	
	public void setFromuserid(BigInteger fromUserId) {
		set("from_userid", fromUserId);
	}

	public BigInteger getFromUserId() {
		Object fromUserId = get("from_userid");
		if (fromUserId == null)
			return null;

		return fromUserId instanceof BigInteger ? (BigInteger)fromUserId : new BigInteger(fromUserId.toString());
	}
	
	public void setType(int type){
		set("type", type);
	}
	public int getType(){
		return get("type");
	}
	
	public void setContent(String content){
		set("content", content);
	}
	public String getContent(){
		String content = get("content");
		return StrKit.notBlank(content) ? content : "";
	}
	
	public void setIsRead(int isRead){
		set("is_read", isRead);
	}
	public int getIsRead(){
		return get("is_read");
	}
	
	public void setReadTime(java.util.Date readTime){
		set("read_time", readTime);
	}
	public java.util.Date getReadTime(){
		return get("read_time");
	}
	
	public void setIsAction(int isAction){
		set("is_action", isAction);
	}
	public int getIsAction(){
		return get("is_action");
	}
	
	public void setAct(String act){
		set("act", act);
	}
	public String getAct(){
		String act = get("act");
		return StrKit.notBlank(act) ? act : "";
	}
	
	public void setContractId(BigInteger contractId) {
		set("contract_id", contractId);
	}

	public BigInteger getContractId() {
		Object contract_id = get("contract_id");
		if (contract_id == null)
			return null;

		return contract_id instanceof BigInteger ? (BigInteger)contract_id : new BigInteger(contract_id.toString());
	}
	
	public void setApplyId(BigInteger applyId) {
		set("apply_id", applyId);
	}

	public BigInteger getApplyId() {
		Object applyId = get("apply_id");
		if (applyId == null)
			return null;

		return applyId instanceof BigInteger ? (BigInteger)applyId : new BigInteger(applyId.toString());
	}
	
	public void setUrl(String url){
		set("url", url);
	}
	public String getUrl(){
		String url =  get("url");
		return StrKit.notBlank(url) ? url : "";
	}
	
	public void setCreateTime(java.util.Date createTime){
		set("create_time", createTime);
	}
	public java.util.Date getCreateTime(){
		return get("create_time");
	}
	
	public void setDeleted(int deleted){
		set("deleted", deleted);
	}
	public int getDeleted(){
		return get("deleted");
	}
	
}
