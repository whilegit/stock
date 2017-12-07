package chinapay.bean;
/**
 * @author huang.xuting
 * 
 */

public class TransactionBean {
	private String MerId;       //商户号，数字，定长15位
	private String MerKeyPath;  //商户私钥路径
	private String PubKeyPath;  //商户公钥路径
	private String FileName;    //上传txt文件名
	private String Plain;       //存款批量交易信息
	private String FileHeader;  //txt文件头
	private String FileMatter;  //txt文件体
	private String FilePath;    //txt文件路径
	private String ChkValue;    //交易签名，字符，定长256位
	
	//应答数据
	private String ResponseCode; //应答码
	private String Message;      //应答信息
	
	//返回报文数据
	private String Data;         //控台返回报文
	

	public void setMerId(String merId) {
		MerId = merId;
	}
	public String getMerId() {
		return MerId;
	}


	public void setResponseCode(String responseCode) {
		ResponseCode = responseCode;
	}


	public String getResponseCode() {
		return ResponseCode;
	}

	public void setData(String data) {
		Data = data;
	}

	public String getData() {
		return Data;
	}

	public void setMerKeyPath(String merKeyPath) {
		MerKeyPath = merKeyPath;
	}

	public String getMerKeyPath() {
		return MerKeyPath;
	}

	public void setPubKeyPath(String pubKeyPath) {
		PubKeyPath = pubKeyPath;
	}

	public String getPubKeyPath() {
		return PubKeyPath;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}

	public String getFileName() {
		return FileName;
	}

	public void setPlain(String plain) {
		Plain = plain;
	}

	public String getPlain() {
		return Plain;
	}

	public void setFileHeader(String fileHeader) {
		FileHeader = fileHeader;
	}

	public String getFileHeader() {
		return FileHeader;
	}

	public void setFileMatter(String fileMatter) {
		FileMatter = fileMatter;
	}

	public String getFileMatter() {
		return FileMatter;
	}
	public void setFilePath(String filePath) {
		FilePath = filePath;
	}
	public String getFilePath() {
		return FilePath;
	}
	public void setChkValue(String chkValue) {
		ChkValue = chkValue;
	}
	public String getChkValue() {
		return ChkValue;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getMessage() {
		return Message;
	}







}
