package otherlogin;

import java.util.HashMap;
import java.util.Map;

import communicate.PushSender;

public class ThirdPartyAPI {
	public static final String loginapi = "thirdpartylogin";
	public static final String removeaccountapi = "thirdpartyremoveaccount";

	public static String login(String platformname, String access_token) {
		Map<String, Object> params_login = new HashMap<String, Object>();
		params_login.put("access_token", access_token);
		params_login.put("platformname", platformname);
		params_login.put("latitude", ThirdPartyManager.latitude);
		params_login.put("longitude", ThirdPartyManager.longitude);
		return PushSender.sendMessage(loginapi, params_login);
	}
	
	public static String removeAccount(String platformname, String access_token) {
		Map<String, Object> params_login = new HashMap<String, Object>();
		params_login.put("access_token", access_token);
		params_login.put("platformname", platformname);
		return PushSender.sendMessage(removeaccountapi, params_login);
	}
}

