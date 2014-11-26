package otherlogin;

import java.util.Map;

public abstract class Platform {
	abstract String getCode(String url);
	abstract String getAuthorizeUrl(Map<String, String> params);
	abstract void getAccessToken();
	abstract String login();
	abstract String removeAccount();
}
