package otherlogin;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;

import android.util.Log;

public class SinaWeibo extends Platform {
	private static final String platformname = "sinaweibo";
	private static String code;
	private static String access_token;
	private static String uid;
	
	@Override
	public String getCode(String url) {
		// TODO Auto-generated method stub
		String redirect_uri = SinaWeiboAPI.getRedirectUri();
		String regEx = "^(http://)?" + redirect_uri + "(/)?\\?code=(\\w+)";
		Log.v("reg", regEx);
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(url);
		if (mat.find()) {
			code = mat.group(3);
		} else {
			code = null;
		}
		return code;
	}
	@Override
	public String getAuthorizeUrl(Map<String, String> params) {
		// TODO Auto-generated method stub
		return SinaWeiboAPI.getAuthorizeUrl(params);
	}
	@Override
	public void getAccessToken() {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		JSONObject result = SinaWeiboAPI.access_token(params);
		access_token = result.get("access_token").toString();
		uid = result.get("uid").toString();
	}
	@Override
	public String login() {
		// TODO Auto-generated method stub
		ThirdPartyManager.username = "*" + platformname + uid;
		return ThirdPartyAPI.login(platformname, access_token);
	}
	@Override
	public String removeAccount() {
		// TODO Auto-generated method stub
		return ThirdPartyAPI.removeAccount(platformname, access_token);
	}
}
