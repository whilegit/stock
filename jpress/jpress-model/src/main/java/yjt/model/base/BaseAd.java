package yjt.model.base;

import java.math.BigInteger;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

import io.jpress.message.MessageKit;
import io.jpress.model.Metadata;
import io.jpress.model.core.JModel;
import io.jpress.model.query.MetaDataQuery;


public class BaseAd<M extends BaseAd<M>> extends JModel<M> implements IBean {

	private static final long serialVersionUID = 1L;
	
	
	public static final String CACHE_NAME = "ad";
	public static final String METADATA_TYPE = "ad";

	public static final String ACTION_ADD = "ad:add";
	public static final String ACTION_DELETE = "ad:delete";
	public static final String ACTION_UPDATE = "ad:update";
	
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
		if(!(o instanceof BaseAd<?>)){return false;}

		BaseAd<?> m = (BaseAd<?>) o;
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
	
	public void setImg(String img){
		set("img", img);
	}
	public String getImg(){
		String img =  get("img");
		return StrKit.notBlank(img) ? img : "";
	}
	
	public void setUrl(String url){
		set("url", url);
	}
	public String getUrl(){
		String url =  get("url");
		return StrKit.notBlank(url) ? url : "";
	}
	
	public void setDscrpt(String dscrpt){
		set("dscrpt", dscrpt);
	}
	public String getDscrpt(){
		String dscrpt =  get("dscrpt");
		return StrKit.notBlank(dscrpt) ? dscrpt : "";
	}
	
	public void setOrderNum(int orderNum){
		set("order_num", orderNum);
	}
	public Integer getOrderNum(){
		Integer orderNum =  get("order_num");
		return orderNum != null ? orderNum : 0;
	}
	
	public void setStatus(int status){
		set("status", status);
	}
	public Integer getStatus(){
		Integer status =  get("status");
		return status != null ? status : 0;
	}

}
