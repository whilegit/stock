/**
 * Author: lzr
 */
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

@SuppressWarnings("serial")
public class BaseContract<M extends BaseContract<M>> extends JModel<M> implements IBean {
	public static final String CACHE_NAME = "contract";
	public static final String METADATA_TYPE = "contract";

	public static final String ACTION_ADD = "contract:add";
	public static final String ACTION_DELETE = "contract:delete";
	public static final String ACTION_UPDATE = "contract:update";
	
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
		if(!(o instanceof BaseContract<?>)){return false;}

		BaseContract<?> m = (BaseContract<?>) o;
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

	public void setContractNumber(java.lang.String contractNumber) {
		set("contract_number", contractNumber);
	}

	public java.lang.String getContractNumber() {
		return get("contract_number");
	}

	public void setContractTypeId(int contractTypeId) {
		set("contract_type_id", contractTypeId);
	}

	public int getContractTypeId() {
		return get("contract_type_id");
	}

	public void setCreditId(java.math.BigInteger creditId) {
		set("credit_id", creditId);
	}

	public BigInteger getCreditId() {
		return get("credit_id");
	}

	public void setDebitId(java.math.BigInteger debitId){
		set("debit_id", debitId);
	}
	public BigInteger getDebitId(){
		return get("debit_id");
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
	
	public void setCreateTime(java.util.Date createTime ){
		set("create_time", createTime);
	}
	public java.util.Date getCreateTime(){
		return get("create_time");
	}
	
	public void setValueDate(java.util.Date valueDate ){
		set("value_date", valueDate);
	}
	public java.util.Date getValueDate(){
		return get("value_date");
	}
	
	public void setLoanTerm(int loanTerm ){
		set("loan_term", loanTerm);
	}
	public int getLoanTerm(){
		return get("loan_term");
	}
	
	public void setMaturityDate(java.util.Date maturityDate){
		set("maturity_date", maturityDate);
	}
	public java.util.Date getMaturityDate(){
		return get("maturity_date");
	}
	
	public void setFee1(double fee1){
		set("fee1", fee1);
	}
	public double getFee1(){
		return get("fee1");
	}
	public void setFee2(double fee2){
		set("fee2", fee2);
	}
	public double getFee2(){
		return get("fee2");
	}
	public void setFee3(double fee3){
		set("fee3", fee3);
	}
	public double getFee3(){
		return get("fee3");
	}
	
	public void setAppovalU1(int approvalU1){
		set("approval_u1", approvalU1);
	}
	public BigInteger getAppovalU1(){
		return get("approval_u1");
	}
	public void setApprovalTime1(java.util.Date approvalTime1 ){
		set("approval_time1", approvalTime1);
	}
	public java.util.Date getApprovalTime1(){
		return get("approval_time1");
	}
	
	public void setAppovalU2(int approvalU2){
		set("approval_u2", approvalU2);
	}
	public BigInteger getAppovalU2(){
		return get("approval_u2");
	}
	public void setApprovalTime2(java.util.Date approvalTime2 ){
		set("approval_time2", approvalTime2);
	}
	public java.util.Date getApprovalTime2(){
		return get("approval_time2");
	}
	
	public void setAppovalU3(int approvalU3){
		set("approval_u3", approvalU3);
	}
	public BigInteger getAppovalU3(){
		return get("approval_u3");
	}
	public void setApprovalTime3(java.util.Date approvalTime3 ){
		set("approval_time3", approvalTime3);
	}
	public java.util.Date getApprovalTime3(){
		return get("approval_time3");
	}
	
	
	public void setStatus(int status){
		set("status", status);
	}
	public int getStatus(){
		return get("status");
	}
	
	public void setRepaymentStatus(int repaymentStatus){
		set("repayment_status", repaymentStatus);
	}
	public int getRepaymentStatus(){
		return get("repayment_status");
	}
	
	public void setApplyId(BigInteger applyId){
		set("apply_id", applyId);
	}
	public BigInteger getApplyId(){
		return get("apply_id");
	}
	
	
	public void setUpdateTime(java.util.Date updateTime ){
		set("update_time", updateTime);
	}
	public java.util.Date getUpdateTime(){
		return get("update_time");
	}

}

