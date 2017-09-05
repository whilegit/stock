package yjt.api.v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.render.Render;

import yjt.Utils;

public class RenderFile extends Render{

	protected String filepath;
	protected String mime;

	@Override
	public void render() {
		FileInputStream fis = null;
		ServletOutputStream sos = null;
		// TODO Auto-generated method stub
		String newpath;
		try {
			newpath = new String(filepath.getBytes("ISO-8859-1"), "UTF-8");
			File image = new File(newpath);
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
		}
		
	}
	
	public Render setContext(HttpServletRequest request, HttpServletResponse response, String filepath, String mime) {
		this.request = request;
		this.response = response;
		this.filepath = filepath;
		this.mime = mime;
		return this;
	}

}
