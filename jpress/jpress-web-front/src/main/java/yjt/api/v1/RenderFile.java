package yjt.api.v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.render.Render;

public class RenderFile extends Render{

	protected boolean deleteFlag = false;
	protected String filepath;
	protected String mime;

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
	
	public Render setContext(HttpServletRequest request, HttpServletResponse response, String filepath, String mime, boolean deleteFlag) {
		this.request = request;
		this.response = response;
		this.filepath = filepath;
		this.mime = mime;
		this.deleteFlag = deleteFlag;
		return this;
	}

}
