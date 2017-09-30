=======================================================
ChinaPay 批量代扣接口演示程序 (Java版) 使用说明
=======================================================
/**
 * @author huang.xuting
 * 2012-06-04
 */

ChinaPay接口调用实例: 批量代扣接口调用实例使用说明
1、本实例主要实现批量代扣文件上传、批量代扣文件查询、回盘文件通知和回盘文件内容下载 4个接口的调用。
2、运行本实例前，请修改/src/res/cp_config.properties文件中相应的配置信息。
 
 需要修改的配置如下：
#MerchantID（商户号）
chinapay.merid = 808080080988041

#BatchContent FilePath（批量代扣文件存储文件夹路径）
chinapay.BatchContent.filepath = F\:\\WithBatch\\

#MerchantKey FilePath（私钥路径）
chinapay.merkey.filepath = F\:\\Workspace\\\u4EE3\u6263-key\\808080080988041-key\\MerPrk.key

#PublicKey FilePath（公钥路径）
chinapay.pubkey.filepath = F\:\\Workspace\\\u4EE3\u6263-key\\808080080988041-key\\PgPubk.key

3、启动工程后，可访问诸如：http://localhost:8080/chinapay_WithBatch/index.jsp页面使用本实例。		
