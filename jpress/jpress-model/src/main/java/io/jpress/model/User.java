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

import com.alibaba.fastjson.JSONObject;
import com.jfinal.log.Log;

import io.jpress.model.base.BaseUser;
import io.jpress.model.core.Table;
import io.jpress.model.query.UserQuery;
import yjt.Utils;
import yjt.model.query.ContractQuery;
import yjt.model.query.FollowQuery;

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
	
	public HashMap<String, Object> getMemberProfile(){
		HashMap<String, Object> profile = new HashMap<String, Object>();
		BigInteger id = getId();
		double income = ContractQuery.me().queryCurDebits(id);
		double outcome = ContractQuery.me().queryCurCredits(id);
		Date birthday = getBirthday();
		String birthdayStr = (birthday != null) ? sdfYmd.format(birthday) : "";
		
		profile.put("memberID", id.toString());
		profile.put("avatar", getAvatar());
		profile.put("mobile", getMobile());
		profile.put("nickname", getNickname());
		profile.put("name", getRealname());
		profile.put("gender", getGender());
		profile.put("birthday", birthdayStr);
		profile.put("score", ""+getScore());
		profile.put("income", ""+income);
		profile.put("outcome", ""+outcome);
		profile.put("balance", yjt.Utils.bigDecimalRound2(getAmount()));
		profile.put("interest", yjt.Utils.bigDecimalRound2(getInterest()));
		profile.put("canBorrowMoney", ""+getCanBorrowMoney());
		profile.put("canLend", "" + getCanLend());
		profile.put("sysPush", ""+getSysPush());
		profile.put("salePush", ""+getSalePush());
		profile.put("inPush", ""+getInPush());
		profile.put("outPush", ""+getOutPush());
		profile.put("overdue",  "" + getOverdue());
		profile.put("authAlipay", "" + getAuthAlipay());
		profile.put("authBook", "" + getAuthBook());
		profile.put("authCard", "" + getAuthCard());
		profile.put("authFace", "" + getAuthFace());
		profile.put("authGjj", "" + getAuthGjj());
		profile.put("authMobile", "" + getMobileStatus());
		profile.put("authXuexing", "" + getAuthXuexing());
		profile.put("authZhima", "" + getAuthZhima());
		
		return profile;
	}
	
	public JSONObject getUserProfile(boolean detail){
		JSONObject profile = new JSONObject();
		BigInteger id = getId();
		
		profile.put("userID", id.toString());
		profile.put("userAvatar", getAvatar());
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
			profile.put("income", ""+income);
			profile.put("outcome", ""+outcome);
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
	
	
	//获取地理位置使用高德地图接口，暂时放一下
	public String getUserLocation(){
		return "";
	}
	
	public boolean changeBalance(double change, String reason) {
		double amount = getAmount().doubleValue();
		setAmount(BigDecimal.valueOf(amount + change));
		boolean ret = update();
		if(ret) {
			log.info("用户 " + getRealname() + "(编号"+getId().toString()+") 变动金额成功，变动额："+change+", 余额: " + getAmount() + ", 理由：" + reason);
		} else {
			log.info("用户 " + getRealname() + "(编号"+getId().toString()+") 扣款金额失败，变动额："+change+", 余额: " + getAmount() + ", 理由：" + reason);
		}
		return ret;
	}
}
