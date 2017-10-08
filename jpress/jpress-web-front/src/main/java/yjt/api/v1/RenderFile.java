package yjt.api.v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.kit.StrKit;
import com.jfinal.render.Render;

public class RenderFile extends Render{

	protected boolean deleteFlag = false;
	protected String filepath;
	protected String mime;
	protected String download_filename;

	public RenderFile () {
		super();
	}
	public void render() {
		FileInputStream fis = null;
		ServletOutputStream sos = null;
		// TODO Auto-generated method stub
		File image = null;
		try {
			image = new File(filepath);
			fis = org.apache.commons.io.FileUtils.openInputStream(image);
			byte[] byt = new byte[fis.available()];
			fis.read(byt);
			
			response.setContentType(mime); 
			response.setContentLength(byt.length);
			if(StrKit.notBlank(download_filename))
				response.setHeader("Content-Disposition", "attachment; filename=" +download_filename);
			sos = response.getOutputStream();
			sos.write(byt);
			sos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} finally {
			try {
				if(fis != null) fis.close();
				if(sos != null) sos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			if(deleteFlag && image != null && image.exists()) {
				image.delete();
			}
		}
	}
	
	public Render setContext(HttpServletRequest request, HttpServletResponse response, String filepath, String mime, String download_filename, boolean deleteFlag) {
		this.request = request;
		this.response = response;
		this.filepath = filepath;
		this.mime = mime;
		this.download_filename = download_filename;
		this.deleteFlag = deleteFlag;
		return this;
	}

}
