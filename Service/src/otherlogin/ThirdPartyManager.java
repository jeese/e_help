package otherlogin;

public class ThirdPartyManager {
	public static final int PLATFORM_SINAWEIBO = 1;
	public static final int PLATFORM_TECENTQQ = 2;
	
	public static final int TASK_NOTHING = 1;
	public static final int TASK_LOGIN = 2;
	public static final int TASK_REMOVEACCOUNT = 3;
	
	public static final int STATE_NOLOGIN = 1;
	public static final int STATE_LOGIN = 2;

	public interface OnLoginSucceed {
		public void onSucceed();
	}
	
	public interface OnRemoveAccountSucceed {
		public void onSucceed();
	}

	public static int platform;
	public static int task = TASK_NOTHING;
	public static int state = STATE_NOLOGIN;
	public static String username = null;
	public static OnLoginSucceed onLoginSucceed;
	public static OnRemoveAccountSucceed onRemoveAccountSucceed;
	

	public static void setOnLoginSucceed(OnLoginSucceed _onLoginSucceed) {
		// TODO Auto-generated method stub
		onLoginSucceed = _onLoginSucceed;
	}
	
	public static void setOnRemoveAccountSucceed(OnRemoveAccountSucceed _onRemoveAccountSucceed) {
		// TODO Auto-generated method stub
		onRemoveAccountSucceed = _onRemoveAccountSucceed;
	}
	
	public static double latitude;
	public static double longitude;
	
	public static void setLocation(double _latitude, double _longitude) {
		latitude = _latitude;
		longitude = _longitude;
	}
}
