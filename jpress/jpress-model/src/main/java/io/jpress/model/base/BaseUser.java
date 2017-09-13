/**
 * Copyright (c) 2015-2016, Michael Yang 杨福海 (fuhai999@gmail.com).
 *
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jpress.model.base;

import io.jpress.message.MessageKit;
import io.jpress.model.Metadata;
import io.jpress.model.core.JModel;
import io.jpress.model.query.MetaDataQuery;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

/**
 *  Auto generated by JPress, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseUser<M extends BaseUser<M>> extends JModel<M> implements IBean {

	public static final String CACHE_NAME = "user";
	public static final String METADATA_TYPE = "user";

	public static final String ACTION_ADD = "user:add";
	public static final String ACTION_DELETE = "user:delete";
	public static final String ACTION_UPDATE = "user:update";

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
		if(!(o instanceof BaseUser<?>)){return false;}

		BaseUser<?> m = (BaseUser<?>) o;
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

	public void setUsername(java.lang.String username) {
		set("username", username);
	}

	public java.lang.String getUsername() {
		String username = get("username");
		return username != null ? username : "";
	}

	public void setNickname(java.lang.String nickname) {
		set("nickname", nickname);
	}

	public java.lang.String getNickname() {
		String nickname = get("nickname");
		return nickname != null ? nickname : "";
	}

	public void setRealname(java.lang.String realname) {
		set("realname", realname);
	}

	public java.lang.String getRealname() {
		String realname = get("realname");
		return realname != null ? realname : "";
	}

	public void setPassword(java.lang.String password) {
		set("password", password);
	}

	public java.lang.String getPassword() {
		return get("password");
	}

	public void setSalt(java.lang.String salt) {
		set("salt", salt);
	}

	public java.lang.String getSalt() {
		return get("salt");
	}

	public void setDealPassword(java.lang.String dealPassword) {
		set("deal_password", dealPassword);
	}

	public java.lang.String getDealPassword() {
		return get("deal_password");
	}

	public void setDealSalt(java.lang.String dealSalt) {
		set("deal_salt", dealSalt);
	}

	public java.lang.String getDealSalt() {
		return get("deal_salt");
	}
	
	public void setEmail(java.lang.String email) {
		set("email", email);
	}

	public java.lang.String getEmail() {
		return get("email");
	}

	public void setEmailStatus(java.lang.String emailStatus) {
		set("email_status", emailStatus);
	}

	public java.lang.String getEmailStatus() {
		return get("email_status");
	}

	public void setMobile(java.lang.String mobile) {
		set("mobile", mobile);
	}

	public java.lang.String getMobile() {
		return get("mobile");
	}

	public void setMobileStatus(java.lang.String mobileStatus) {
		set("mobile_status", mobileStatus);
	}

	public java.lang.String getMobileStatus() {
		String mobileStatus = get("mobile_status");
		return !StrKit.isBlank(mobileStatus) ? mobileStatus : "0";
	}

	public void setTelephone(java.lang.String telephone) {
		set("telephone", telephone);
	}

	public java.lang.String getTelephone() {
		return get("telephone");
	}

	public void setAmount(java.math.BigDecimal amount) {
		set("amount", amount);
	}

	public java.math.BigDecimal getAmount() {
		return get("amount");
	}

	public void setGender(java.lang.String gender) {
		set("gender", gender);
	}

	public java.lang.String getGender() {
		String gender = get("gender");
		return gender != null ? gender : "";
	}

	public void setRole(java.lang.String role) {
		set("role", role);
	}

	public java.lang.String getRole() {
		return get("role");
	}
	
	public void setPerm(java.lang.String perm) {
		set("perm", perm);
	}

	public java.lang.String getPerm() {
		return get("perm");
	}
	
	public void setSignature(java.lang.String signature) {
		set("signature", signature);
	}

	public java.lang.String getSignature() {
		return get("signature");
	}

	public void setContentCount(java.lang.Long contentCount) {
		set("content_count", contentCount);
	}

	public java.lang.Long getContentCount() {
		return get("content_count");
	}

	public void setCommentCount(java.lang.Long commentCount) {
		set("comment_count", commentCount);
	}

	public java.lang.Long getCommentCount() {
		return get("comment_count");
	}

	public void setQq(java.lang.String qq) {
		set("qq", qq);
	}

	public java.lang.String getQq() {
		return get("qq");
	}

	public void setWechat(java.lang.String wechat) {
		set("wechat", wechat);
	}

	public java.lang.String getWechat() {
		return get("wechat");
	}

	public void setWeibo(java.lang.String weibo) {
		set("weibo", weibo);
	}

	public java.lang.String getWeibo() {
		return get("weibo");
	}

	public void setFacebook(java.lang.String facebook) {
		set("facebook", facebook);
	}

	public java.lang.String getFacebook() {
		return get("facebook");
	}

	public void setLinkedin(java.lang.String linkedin) {
		set("linkedin", linkedin);
	}

	public java.lang.String getLinkedin() {
		return get("linkedin");
	}

	public void setBirthday(java.util.Date birthday) {
		set("birthday", birthday);
	}

	public java.util.Date getBirthday() {
		return get("birthday");
	}

	public void setCompany(java.lang.String company) {
		set("company", company);
	}

	public java.lang.String getCompany() {
		return get("company");
	}

	public void setOccupation(java.lang.String occupation) {
		set("occupation", occupation);
	}

	public java.lang.String getOccupation() {
		return get("occupation");
	}

	public void setAddress(java.lang.String address) {
		set("address", address);
	}

	public java.lang.String getAddress() {
		String address = get("address");
		return address != null ? address : "";
	}

	public void setZipcode(java.lang.String zipcode) {
		set("zipcode", zipcode);
	}

	public java.lang.String getZipcode() {
		return get("zipcode");
	}

	public void setSite(java.lang.String site) {
		set("site", site);
	}

	public java.lang.String getSite() {
		return get("site");
	}

	public void setGraduateschool(java.lang.String graduateschool) {
		set("graduateschool", graduateschool);
	}

	public java.lang.String getGraduateschool() {
		return get("graduateschool");
	}

	public void setEducation(java.lang.String education) {
		set("education", education);
	}

	public java.lang.String getEducation() {
		return get("education");
	}

	public void setAvatar(java.lang.String avatar) {
		set("avatar", avatar);
	}

	public java.lang.String getAvatar() {
		String avatar = get("avatar");
		return avatar != null ? avatar : "";
	}

	public void setIdcardtype(java.lang.String idcardtype) {
		set("idcardtype", idcardtype);
	}

	public java.lang.String getIdcardtype() {
		return get("idcardtype");
	}

	public void setIdcardFront(java.lang.String idcardFront) {
		set("idcard_front", idcardFront);
	}
	
	public java.lang.String getIdcardFront() {
		return get("idcard_front");
	}

	public void setIdcardBack(java.lang.String idcardBack) {
		set("idcard_back", idcardBack);
	}
	
	public java.lang.String getIdcardBack() {
		return get("idcard_back");
	}

	public void setIdcard(java.lang.String idcard) {
		set("idcard", idcard);
	}

	public java.lang.String getIdcard() {
		String idCard = get("idcard");
		return idCard != null ? idCard : "";
	}

	public void setStatus(java.lang.String status) {
		set("status", status);
	}

	public java.lang.String getStatus() {
		return get("status");
	}

	public void setCreated(java.util.Date created) {
		set("created", created);
	}

	public java.util.Date getCreated() {
		return get("created");
	}

	public void setCreateSource(java.lang.String createSource) {
		set("create_source", createSource);
	}

	public java.lang.String getCreateSource() {
		return get("create_source");
	}

	public void setLogged(java.util.Date logged) {
		set("logged", logged);
	}

	public java.util.Date getLogged() {
		return get("logged");
	}

	public void setActivated(java.util.Date activated) {
		set("activated", activated);
	}

	public java.util.Date getActivated() {
		return get("activated");
	}
	
	public void setMemberToken(String memberToken){
		set("member_token", memberToken);
	}
	public String getMemberToken(){
		return get("member_token");
	}
	
	public void setScore(java.lang.Integer score){
		set("score", score);
	}
	
	public java.lang.Integer getScore(){
		return get("score");
	}
	
	public void setCanBorrowMoney(java.lang.Integer canBorrowMoney){
		set("can_borrow_money", canBorrowMoney);
	}
	
	public java.lang.Integer getCanBorrowMoney(){
		return get("can_borrow_money");
	}
	
	public void setSysPush(java.lang.Integer sysPush){
		set("sys_push", sysPush);
	}
	
	public java.lang.Integer getSysPush(){
		Integer sysPush = get("sys_push");
		return sysPush != null ? sysPush : Integer.valueOf(0);
	}
	
	public void setSalePush(java.lang.Integer salePush){
		set("sale_push", salePush);
	}
	
	public java.lang.Integer getSalePush(){
		return get("sale_push");
	}
	
	public void setInPush(java.lang.Integer inPush){
		set("in_push", inPush);
	}
	
	public java.lang.Integer getInPush(){
		return get("in_push");
	}
	
	public void setOutPush(java.lang.Integer outPush){
		set("out_push", outPush);
	}
	
	public java.lang.Integer getOutPush(){
		return get("out_push");
	}
	
	public void setOverdue(java.lang.Integer overdue){
		set("overdue", overdue);
	}
	
	public java.lang.Integer getOverdue(){
		return get("overdue");
	}
	
	public void setCreditRecord(java.lang.Integer creditRecord){
		set("credit_record", creditRecord);
	}
	
	public String getCreditRecord(){
		String creditRecord = get("credit_record");
		return creditRecord != null ? creditRecord : "";
	}
	
	public void setCanLend(java.lang.Integer canLend){
		set("can_lend", canLend);
	}
	
	public java.lang.Integer getCanLend(){
		return get("can_lend");
	}
	
	public void setLat(double lat){
		set("lat", BigDecimal.valueOf(lat));
	}
	
	public java.math.BigDecimal getLat(){
		return get("lat");
	}
	
	public void setLng(double lng){
		set("lng", BigDecimal.valueOf(lng));
	}
	
	public java.math.BigDecimal getLng(){
		return get("lng");
	}
	
	public void setZhimaScore(java.lang.Integer zhimaScore){
		set("zhima_score", zhimaScore);
	}
	
	public java.lang.Integer getZhimaScore(){
		return get("zhima_score");
	}
	
	public void setAuthZhima(java.lang.Integer authZhima){
		set("auth_zhima", authZhima);
	}
	
	public java.lang.Integer getAuthZhima(){
		return get("auth_zhima");
	}
	
	public void setAuthAlipay(java.lang.Integer authAlipay){
		set("auth_alipay", authAlipay);
	}
	
	public java.lang.Integer getAuthAlipay(){
		return get("auth_alipay");
	}
	
	public void setAuthXuexing(java.lang.Integer authXuexing){
		set("auth_xuexing", authXuexing);
	}
	
	public java.lang.Integer getAuthXuexing(){
		return get("auth_xuexing");
	}
	
	public void setAuthBook(java.lang.Integer authBook){
		set("auth_book", authBook);
	}
	
	public java.lang.Integer getAuthBook(){
		return get("auth_book");
	}
	
	public void setAuthCard(java.lang.Integer authCard){
		set("auth_card", authCard);
	}
	
	public java.lang.Integer getAuthCard(){
		return get("auth_card");
	}
	
	public void setAuthFace(java.lang.Integer authFace){
		set("auth_face", authFace);
	}
	
	public java.lang.Integer getAuthFace(){
		return get("auth_face");
	}
	
	public void setAuthGjj(java.lang.Integer authGjj){
		set("auth_gjj", authGjj);
	}
	
	public java.lang.Integer getAuthGjj(){
		return get("auth_gjj");
	}
	
	public void setInterest(double interest){
		set("interest", BigDecimal.valueOf(interest));
	}
	
	public java.math.BigDecimal getInterest(){
		return get("interest");
	}
	
}
