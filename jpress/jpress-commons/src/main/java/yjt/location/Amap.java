package yjt.location;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;

import yjt.Utils;

/**
 * 高德地图类
 * @author Administrator
 *
 */
public class Amap {
	protected static final String key = "043cac9e3f604219a478a860e2a6c580";
	
	/**
	 * 逆地理转换 (经纬度转日常地址)
	 */
	public static Regeo trans(double lat, double lng) {
		Regeo ret = null;
		String url = "http://restapi.amap.com/v3/geocode/regeo";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("key", key));
		params.add(new BasicNameValuePair("output", "json"));
		params.add(new BasicNameValuePair("batch", "false"));
		params.add(new BasicNameValuePair("extentions", "base"));
		params.add(new BasicNameValuePair("location", lng + "," + lat));
		try {
			String result = Utils.get(url, params, null);
			if(result != null) {
				ret = JSON.parseObject(result, Regeo.class);
				//如果里面的地址位置为空的，则认为获取地址位置不成功
				if(ret.getRegeocode() == null) {
					ret = null;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * 
	 * @author Administrator
	 * {"status":"1",
	 *  "info":"OK",
	 *  "infocode":"10000",
	 *  "regeocode":{
	 *  	"formatted_address":"浙江省衢州市江山市坛石镇前坂",
	 *  	"addressComponent":{
	 *  		"country":"中国",
	 *  		"province":"浙江省",
	 *  		"city":"衢州市",
	 *  		"citycode":"0570",
	 *  		"district":"江山市",
	 *  		"adcode":"330881",
	 *  		"township":"坛石镇",
	 *  		"towncode":"330881105000",
	 *  		"neighborhood":{"name":[],"type":[]},"building":{"name":[],"type":[]},"streetNumber":{"street":[],"number":[],"direction":[],"distance":[]},"businessAreas":[[]]}}}
	 */
	public static class Regeo{
		
		//返回值为 0 或 1，0 表示请求失败；1 表示请求成功。
		protected int status;
		//当 status 为 0 时，info 会返回具体错误原因，否则返回“OK”。详情可以参考
		protected String info;
		//地址对象
		protected Regeocode Regeocode;
		
		public int getStatus() { return status; }
		public void setStatus(int status) { this.status = status; }
		public String getInfo() { return info; }
		public void setInfo(String info) { this.info = info; }
		public Regeocode getRegeocode() { return Regeocode; }
		public void setRegeocode(Regeocode regeocode) { Regeocode = regeocode; }
		
	}
	
	public static class Regeocode{
		protected String formatted_address;
		protected AddressComponent addressComponent;
		
		public String getFormattedAddress() { return formatted_address; }
		public void setFormattedAddress(String formatted_address) { this.formatted_address = formatted_address; }
		public AddressComponent getAddressComponent() { return addressComponent; }
		public void setAddressComponent(AddressComponent addressComponent) { this.addressComponent = addressComponent; }
	}
	
	public static class AddressComponent{
		protected String province;
		protected String city;
		protected String citycode;
		protected String district;
		protected String adcode;
		protected String township;
		protected String towncode;
		
		public String getProvince() { return province;	}
		public void setProvince(String province) {	this.province = province; }
		public String getCity() {	return city;	}
		public void setCity(String city) {	this.city = city;	}
		public String getCitycode() {	return citycode;	}
		public void setCitycode(String citycode) {	this.citycode = citycode;	}
		public String getDistrict() {	return district; }
		public void setDistrict(String district) {	this.district = district;	}
		public String getAdcode() {	return adcode;	}
		public void setAdcode(String adcode) {	this.adcode = adcode;	}
		public String getTownship() {	return township;	}
		public void setTownship(String township) {	this.township = township;	}
		public String getTowncode() {	return towncode;	}
		public void setTowncode(String towncode) {	this.towncode = towncode;	}
	}
}
