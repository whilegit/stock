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
package io.jpress.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

import io.jpress.model.base.BaseUser;
import io.jpress.model.core.Table;
import io.jpress.model.query.UserQuery;
import yjt.Utils;
import yjt.location.Amap;
import yjt.model.CreditLog;
import yjt.model.query.ContractQuery;
import yjt.model.query.FollowQuery;
import yjt.verify.BankcardDetail;

@Table(tableName = "user", primaryKey = "id")
public class User extends BaseUser<User> {
	protected static final Log log = Log.getLog(User.class);
	private static final long serialVersionUID = 1L;

	public static final String ROLE_ADMINISTRATOR = "administrator";
	public static final String STATUS_NORMAL = "normal";
	public static final String STATUS_FROZEN = "frozen";

	protected static final SimpleDateFormat sdfYmd = new SimpleDateFormat("yyyy-MM-dd");
	
	public boolean isAdministrator() {
		return ROLE_ADMINISTRATOR.equals(getRole());
	}

	public boolean isFrozen() {
		return STATUS_FROZEN.equals(getStatus());
	}

	@Override
	public boolean save() {
		if (getCreated() == null) {
			setCreated(new Date());
		}
		return super.save();
	}

	@Override
	public boolean update() {
		removeCache(getId());
		removeCache(getMobile());
		removeCache(getUsername());
		removeCache(getEmail());
		return super.update();
	}

	@Override
	public boolean delete() {
		removeCache(getId());
		removeCache(getMobile());
		removeCache(getUsername());
		removeCache(getEmail());
		return super.delete();
	}

	public String getUrl() {
		return "/user/" + getId();
	}
	
	@Override
	public Integer getCanBorrowMoney() {
		int canBollowMoney = 0;
		
		//canBollowMoney = super.getCanBorrowMoney();
		Date birthday = this.getBirthday();
		if(birthday != null) {
			canBollowMoney = (int)Option.calCanBorrowMoney(birthday);
		}
		
		if("1".equals(getMobileStatus()) == false || this.getAuthBank() != 1 || this.getAuthCard() != 1) {
			canBollowMoney = 0;
		}
		
		return canBollowMoney;
	}
	
	public HashMap<String, Object> getMemberProfile(){
		HashMap<String, Object> profile = new HashMap<String, Object>();
		BigInteger id = getId();
		double income = ContractQuery.me().queryCurDebits(id);
		double outcome = ContractQuery.me().queryCurCredits(id);
		Date birthday = getBirthday();
		String birthdayStr = (birthday != null) ? sdfYmd.format(birthday) : "";

		int canBollowMoney = getCanBorrowMoney();
		BankcardDetail bankcardDetail = null;
		String bankTypeJson = this.getBanktype();
		if(bankTypeJson != null){
			bankcardDetail = JSON.parseObject(bankTypeJson, BankcardDetail.class);
		}
		
		String authMobileStart = "", authMobileEnd = "";
		Date auth_expire = this.getAuthExpire();
		if(auth_expire != null){
			authMobileEnd = Utils.toYmd(auth_expire);
			authMobileStart = Utils.toYmd(Utils.getPrevMonthDay(auth_expire));
		}
		
		profile.put("memberID", id.toString());
		profile.put("avatar", Utils.toMedia(getAvatar()));
		profile.put("mobile", getMobile());
		profile.put("nickname", getNickname());
		profile.put("name", getAuthCard() == 1 ? getRealname() : "未认证");
		profile.put("bankType", bankcardDetail != null ? bankcardDetail.getResult().getBankname() : "");
		profile.put("bankNum", getBankcard());
		profile.put("gender", getGender());
		profile.put("birthday", birthdayStr);
		profile.put("score", ""+getScore());
		profile.put("income", String.format("%.2f", income));
		profile.put("outcome", String.format("%.2f", outcome));
		profile.put("balance", yjt.Utils.bigDecimalRound2(getAmount()));
		profile.put("interest", String.format("%.2f", ContractQuery.me().queryInterest(getId())));
		profile.put("canBorrowMoney", canBollowMoney + ".00");
		profile.put("canLend", "" + getCanLend());
		profile.put("sysPush", ""+getSysPush());
		profile.put("salePush", ""+getSalePush());
		profile.put("inPush", ""+getInPush());
		profile.put("outPush", ""+getOutPush());
		profile.put("overdue",  "" + getOverdue());
		profile.put("authAlipay", "" + getAuthAlipay());
		profile.put("authBank", "" + getAuthBank());
		profile.put("authBook", "" + getAuthBook());
		profile.put("authCard", "" + getAuthCard());
		profile.put("authFace", "" + getAuthFace());
		profile.put("authGjj", "" + getAuthGjj());
		profile.put("authMobile", "" + getMobileStatusLogic());
		profile.put("authXuexing", "" + getAuthXuexing());
		profile.put("authZhima", "" + getAuthZhima());
		profile.put("issetDealPassword", StrKit.notBlank(getDealPassword()) ? "1" : "0");
		profile.put("authMobileStart", authMobileStart);
		profile.put("authMobileEnd", authMobileEnd);
		return profile;
	}
	
	public JSONObject getUserProfile(boolean detail){
		JSONObject profile = new JSONObject();
		BigInteger id = getId();
		
		profile.put("userID", id.toString());
		profile.put("userAvatar", Utils.toMedia(getAvatar()));
		profile.put("userMobile", getMobile());
		profile.put("userName", getRealname());
		
		if(detail == true){
			profile.put("userGender", getGender());
			profile.put("userNickname", getNickname());
			double income = ContractQuery.me().queryCurDebits(id);
			double outcome = ContractQuery.me().queryCurCredits(id);
			Date birthday = getBirthday();
			String birthdayStr = (birthday != null) ? sdfYmd.format(birthday) : "";
			profile.put("birthday", birthdayStr);
			profile.put("score", ""+getScore());
			profile.put("income", String.format("%.2f", income));
			profile.put("outcome", String.format("%.2f", outcome));
			profile.put("userLocation", getUserLocation());
			profile.put("userCard", getIdcard());
			profile.put("userAddress", getAddress());
			profile.put("zhimaScore", getZhimaScore().toString());
			profile.put("validZhifubao", getAuthAlipay().toString());
			profile.put("validXuexing", getAuthXuexing().toString());
			profile.put("regDate", Utils.toYmdHms(getCreated()));
			profile.put("creditRecord", getCreditRecord());
			profile.put("friendCount", FollowQuery.me().getFollowedList(getId()).length + "");
		}
		
		return profile;
	}
	
	// 0:未验证; 1: 已验证； 2: 验证中
	public String getMobileStatusLogic() {
		String phy = this.getMobileStatus();
		Date expire = this.getAuthExpire();
		if("1".equals(phy)) {
			if(expire != null) {
				long expire_time = expire.getTime();
				long cur_time = System.currentTimeMillis();
				if(expire_time < cur_time) {
					phy = "0";
				}
			} else {
				phy = "0";
			}
			return phy;
		} else {
			return phy;
		}
	}
	
	//获取地理位置使用高德地图接口，暂时放一下
	public String getUserLocation(){
		String location = "";
		BigDecimal lat = getLat();
		BigDecimal lng = getLng();
		if(lat != null && lng != null) {
			double dlat = lat.doubleValue();
			double dlng = lng.doubleValue();
			//非在中国范围内，不查询地图
			if(dlat > 18.0 && dlat < 54.0 && dlng > 73.0 && dlng < 135.0) {
				Amap.Regeo regeo = Amap.trans(dlat, dlng);
				if(regeo != null) {
					location = regeo.getRegeocode().getFormattedAddress();
				}
			}
		}
		
		return location;
	}
	
	public void uploadUserLocation(String latStr, String lngStr) {
		if(StrKit.notBlank(latStr, lngStr)){
			double lat = 0.00, lng = 0.00;
			
			try {
			    lat = Double.valueOf(latStr);
			    lng = Double.valueOf(lngStr);
			    if(lat > 1.00 && lat < 90.0 && lng > 1.0 && lng < 180.0 ) {
			    	setLat(lat);
					setLng(lng);
					update();
			    }
			} catch(Exception e) {
				
			}
			
		}
	}
	
	public boolean changeBalance(double change, String reason, BigInteger clerk, CreditLog.Platfrom platform) {
		double amount = getAmount().doubleValue();
		setAmount(BigDecimal.valueOf(amount + change));
		boolean ret = update();
		if(ret) {
			log.info("用户 " + getRealname() + "(编号"+getId().toString()+") 变动金额成功，变动额："+change+", 余额: " + getAmount() + ", 理由：" + reason);
		} else {
			log.info("用户 " + getRealname() + "(编号"+getId().toString()+") 扣款金额失败，变动额："+change+", 余额: " + getAmount() + ", 理由：" + reason);
			return false;
		}
		
		CreditLog creditLog = new CreditLog();
		creditLog.setChange(change);
		creditLog.setClerk(clerk);
		creditLog.setCreateTime(new Date());
		creditLog.setCreditType(1);
		creditLog.setCur(getAmount().doubleValue());
		creditLog.setLog(reason);
		creditLog.setPlatform(platform.getIndex());
		creditLog.setUserId(getId());
		creditLog.save();
		return ret;
	}
	
}
