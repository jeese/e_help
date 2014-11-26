package communicate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.json.simple.JSONObject;

import android.util.Log;

class GetuiSdkHttpPost {

	public static String httpPost(String action, Map<String, Object> map, int CONNECTION_TIMEOUT, int READ_TIMEOUT) {

		String param = JSONObject.toJSONString(map);

		if (param != null) {

			URL url = null;

			try {
				url = new URL(PushConfig.SERVICEURL + action);
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				urlConn.setDoInput(true); // 设置输入流采用字节流
				urlConn.setDoOutput(true); // 设置输出流采用字节流
				urlConn.setRequestMethod("POST");
				urlConn.setUseCaches(false); // 设置缓存
				urlConn.setRequestProperty("Charset", "utf-8");
				urlConn.setConnectTimeout(CONNECTION_TIMEOUT);
				urlConn.setReadTimeout(READ_TIMEOUT);

				urlConn.connect(); // 连接既往服务端发送消息
				Log.i("HttpPost", "connected to server");

				DataOutputStream dop = new DataOutputStream(urlConn.getOutputStream());
				dop.write(param.getBytes("utf-8"));
				Log.i("HttpPost", "sending data");
				dop.flush(); // 发送，清空缓存
				dop.close(); // 关闭

				// 下面开始做接收工作
				BufferedReader bufferReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
				Log.i("HttpPost", "sending finished");
				String result = ""; // 获取服务器返回数据
				String readLine = null;
				while ((readLine = bufferReader.readLine()) != null) {
					result += readLine;
				}
				bufferReader.close();
				urlConn.disconnect();

				return result;

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 连接服务器失败
			return "error";
		}
		return "param is null";
	}
	

}
