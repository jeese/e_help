package otherlogin;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import client.ui.R;

import communicate.PushConfig;


import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class AuthorizeActivity extends Activity {
	private WebView loginPage;
	private Platform platform;
	private static final int INFO_OTHER_ERROR = 1;
	private static final int INFO_NET_ERROR = 2;
	private static final int INFO_PARAM_NULL = 3; // 
	private static final int INFO_SERVER_CONN_ERROR = 4;
	private static final int INFO_LACK_PARAMS = 5;
	private static final int INFO_NO_SUCH_USER = 6;
	private static final int INFO_AUTH_SERVER_ERROR = 7;
	private static final int INFO_UNSUPPORTED_PLATFORM = 8;
	private static final int INFO_AUTH_OUTDATE = 9;
	private static final int INFO_LOGIN_OK = 11;
	private static final int INFO_CANCEL_OK = 12;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case INFO_OTHER_ERROR:
					Toast.makeText(AuthorizeActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
					break;
				case INFO_NET_ERROR:
					Toast.makeText(AuthorizeActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
					break;
				case INFO_PARAM_NULL:
					Toast.makeText(AuthorizeActivity.this, "参数错误", Toast.LENGTH_SHORT).show();
					break;
				case INFO_SERVER_CONN_ERROR:
					Toast.makeText(AuthorizeActivity.this, "访问服务器失败", Toast.LENGTH_SHORT).show();
					break;
				case INFO_LACK_PARAMS:
					Toast.makeText(AuthorizeActivity.this, "缺少参数", Toast.LENGTH_SHORT).show();
					break;
				case INFO_NO_SUCH_USER:
					Toast.makeText(AuthorizeActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
					break;
				case INFO_AUTH_SERVER_ERROR:
					Toast.makeText(AuthorizeActivity.this, "第三方授权服务器访问出错", Toast.LENGTH_SHORT).show();
					break;
				case INFO_UNSUPPORTED_PLATFORM:
					Toast.makeText(AuthorizeActivity.this, "不支持的第三方平台", Toast.LENGTH_SHORT).show();
					break;
				case INFO_AUTH_OUTDATE:
					Toast.makeText(AuthorizeActivity.this, "授权过期", Toast.LENGTH_SHORT).show();
					break;
				case INFO_LOGIN_OK:
					Toast.makeText(AuthorizeActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
					break;
				case INFO_CANCEL_OK:
					Toast.makeText(AuthorizeActivity.this, "成功移除账号", Toast.LENGTH_SHORT).show();
					break;
				default:
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.otherlogin_authorize);
		
		switch (ThirdPartyManager.platform) {
		case ThirdPartyManager.PLATFORM_SINAWEIBO:
			platform = new SinaWeibo();
			break;
		case ThirdPartyManager.PLATFORM_TECENTQQ:
			break;
		default:
		}
		loginPage = (WebView)findViewById(R.id.loginPage);
		loginPage.setWebViewClient(new WebViewClient() {
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				Log.v("mark", "receive ssl error");
				handler.proceed();
			}
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url != null) {
					String code = platform.getCode(url);
					if (code != null) {
						new Thread(new Runnable() {
							public void run() {
								platform.getAccessToken();
								String resultstr = "other error";
								switch (ThirdPartyManager.task) {
									case ThirdPartyManager.TASK_LOGIN:
										resultstr = platform.login();
										break;
									case ThirdPartyManager.TASK_REMOVEACCOUNT:
										resultstr = platform.removeAccount();
										break;
									default:
								}
								if (resultstr.equals("other error")) {
									handler.sendEmptyMessage(INFO_OTHER_ERROR);
								} else if (resultstr.equals("network error")) {
									handler.sendEmptyMessage(INFO_NET_ERROR);
								} else if (resultstr.equals("param is null")) {
									handler.sendEmptyMessage(INFO_PARAM_NULL);
								} else if (resultstr.equals("error")) {
									handler.sendEmptyMessage(INFO_SERVER_CONN_ERROR);
								} else {
									JSONObject result = (JSONObject) JSONValue.parse(resultstr);
									if (result.get("result").equals("error")) {
										switch (Integer.valueOf(result.get("error_code").toString())) {
											case 1:
												handler.sendEmptyMessage(INFO_LACK_PARAMS);
												break;
											case 2:
												handler.sendEmptyMessage(INFO_NO_SUCH_USER);
												break;
											case 50001:
												handler.sendEmptyMessage(INFO_AUTH_SERVER_ERROR);
												break;
											case 50002:
												handler.sendEmptyMessage(INFO_UNSUPPORTED_PLATFORM);
												break;
											case 50003:
												handler.sendEmptyMessage(INFO_AUTH_OUTDATE);
												break;
											default:
										}
									} else if (result.get("result").equals("ok")) {
										if (ThirdPartyManager.task == ThirdPartyManager.TASK_LOGIN) {
											ThirdPartyManager.state = ThirdPartyManager.STATE_LOGIN;
											PushConfig.username = ThirdPartyManager.username;
											handler.sendEmptyMessage(INFO_LOGIN_OK);
											ThirdPartyManager.onLoginSucceed.onSucceed();
											AuthorizeActivity.this.finish();
										} else if (ThirdPartyManager.task == ThirdPartyManager.TASK_REMOVEACCOUNT) {
											ThirdPartyManager.state = ThirdPartyManager.STATE_NOLOGIN;
											ThirdPartyManager.username = null;
											handler.sendEmptyMessage(INFO_CANCEL_OK);
											ThirdPartyManager.onRemoveAccountSucceed.onSucceed();
											AuthorizeActivity.this.finish();
										}
									}
								}
								ThirdPartyManager.task = ThirdPartyManager.TASK_NOTHING;
							}
						}).start();
						
					} else {
						view.loadUrl(url);
					}
				}
				return true;
			}
		});
		loginPage.getSettings().setJavaScriptEnabled(true);
		loginPage.loadUrl(platform.getAuthorizeUrl(null));
	}
}
