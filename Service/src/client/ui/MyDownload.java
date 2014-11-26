package client.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;
import android.util.Log;

public class MyDownload {
	private String urlstr;
	private String filepath;
	private String sdcard;
	private HttpURLConnection conn;
	
	public MyDownload(String urlstr) {
		this.urlstr = urlstr;
		sdcard = Environment.getExternalStorageDirectory() + "/";
	}
	
	public String getFilePath(){
		return filepath;
	}
	
	public void makeFile(String filename) throws IOException {
		filepath = sdcard + filename;
		File file = new File(filepath);
		if (file.exists()) {
			file.delete();
		}
	}
	
	public void getAsBinary(String filename) {
		FileOutputStream outputstream = null;
		try {
			URL url = new URL(urlstr);
			conn = (HttpURLConnection) url.openConnection();
			makeFile(filename);
			InputStream inputstream = conn.getInputStream();
			outputstream = new FileOutputStream(filepath);
			byte[] buffer = new byte[1024];
			int filelen = 0;
			int bufferlength;
			while ((bufferlength = inputstream.read(buffer)) != -1) {
				outputstream.write(buffer, 0, bufferlength);
				filelen += bufferlength;
				Log.v("file length", String.valueOf(filelen));
			}
			inputstream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputstream != null) {
					outputstream.close();
				}
				Log.v("file", "finish");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
