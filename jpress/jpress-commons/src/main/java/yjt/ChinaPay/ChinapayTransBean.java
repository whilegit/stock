package yjt.ChinaPay;

public class ChinapayTransBean {
	
	private String merId;
	private String merDate;
	private String merSeqId;
	private String cardNo;
	private String usrName;
	private String openBank;
	private String prov;
	private String city;
	private String transAmt;
	private String purpose;
	private String subBank;
	
	private String flag = "00";
	private String version = "20160530";
	private String termType = "08";
	private String payMode = "1";
	
	/*
	 * 	private String merId = "808080211306315";
	private String merDate = "20171009";
	private String merSeqId = String.format("%08d", (int)(Math.random() * 99999999));
	private String cardNo = "6236681480007046323"; //6228480362349687719
	private String usrName = "朱千增";    //林忠仁
	private String openBank = "建设银行"; //农业银行
	private String prov = "浙江省";
	private String city = "台州市"; //温岭市
	private String transAmt = "100";
	private String purpose = "test";
	private String subBank = ""; //玉环支行
	
	private String flag = "00";
	private String version = "20160530";
	private String termType = "08";
	private String payMode = "1";
	 */
	
	
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	
	public String getMerDate() {
		return merDate;
	}
	public void setMerDate(String merDate) {
		this.merDate = merDate;
	}
	public String getMerSeqId() {
		return merSeqId;
	}
	public void setMerSeqId(String merSeqId) {
		this.merSeqId = merSeqId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getUsrName() {
		return usrName;
	}
	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}
	public String getOpenBank() {
		return openBank;
	}
	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getSubBank() {
		return subBank;
	}
	public void setSubBank(String subBank) {
		this.subBank = subBank;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getTermType() {
		return termType;
	}
	public void setTermType(String termType) {
		this.termType = termType;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
}
