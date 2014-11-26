package otherlogin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SinaWeiboAPI {
	private static final String appkey = "880517188";
	private static final String appsecret = "d5c2f15e5437ebf4a7ea39110c114d78";
	private static final String redirect_uri = "114.215.133.61:8888";

	private static final String encoding = "utf-8";
	
	private static final String api_authorize = "https://open.weibo.cn/oauth2/authorize";
	private static final Map<String, String> params_authorize = new HashMap<String, String>();
	static {
		params_authorize.put("client_id", appkey);
		params_authorize.put("redirect_uri", redirect_uri);
		params_authorize.put("scope", "friendships_groups_read");
		params_authorize.put("display", "mobile");
		params_authorize.put("forcelogin", "true");
	}
	private static final String api_access_token = "https://api.weibo.com/oauth2/access_token";
	private static final Map<String, String> params_access_token = new HashMap<String, String>();
	static {
		params_access_token.put("client_id", appkey);
		params_access_token.put("client_secret", appsecret);
		params_access_token.put("grant_type", "authorization_code");
		params_access_token.put("code", "");
		params_access_token.put("redirect_uri", redirect_uri);
	}
	private static final String api_revokeauthorize = "https://api.weibo.com/oauth2/revokeoauth2";
	private static final Map<String, String> params_revokeauthorize = new HashMap<String, String>();
	static {
		params_revokeauthorize.put("access_token", "");
	}

	
	private static String getParams(Map<String, String> params, Map<String, String> default_params) {
		String paramStr = "";
		String key;
		try {
			if (params == null) {
				for (Map.Entry<String, String> entry : default_params.entrySet()) {
					paramStr += "&" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), encoding);
				}
			} else {
				for (Map.Entry<String, String> entry : default_params.entrySet()) {
					key = entry.getKey();
					Log.v("key", key);
					paramStr += "&" + key + "=";
					if (params.containsKey(key)) {
						paramStr += URLEncoder.encode(params.get(key), encoding);
					} else {
						paramStr += URLEncoder.encode(entry.getValue(), encoding);
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return paramStr.substring(1, paramStr.length());
	}
	
	public static String getAuthorizeUrl(Map<String, String> params) {
		String gets = getParams(params, params_authorize);
		String url = api_authorize + "?" + gets;
		return url;
	}
	
	public static JSONObject access_token(Map<String, String> params) {
		String posts = getParams(params, params_access_token);
		String resultstr = HttpsVisiter.post(api_access_token, posts);
		JSONObject result = (JSONObject)JSONValue.parse(resultstr);
		return result;
	}
	
	public static JSONObject revokeauthorize(Map<String, String> params) {
		String posts = getParams(params, params_revokeauthorize);
		String resultstr = HttpsVisiter.post(api_revokeauthorize, posts);
		JSONObject result = (JSONObject)JSONValue.parse(resultstr);
		return result;
	}
	
	public static String getRedirectUri() {
		return redirect_uri;
	}
}
