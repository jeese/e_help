package client.ui;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import otherlogin.AuthorizeActivity;
import otherlogin.ThirdPartyManager;
import routeplan.Location;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import communicate.PushConfig;
import communicate.PushSender;

public class LoginActivity extends Activity implements OnClickListener{
	private ImageView loginLogo,login_more;
	private EditText loginaccount,loginpassword;
	private ToggleButton isShowPassword;
	private boolean isDisplayflag=false;
	private String getpassword;
	private Button loginBtn,register;
	private Intent mIntent;
	private Login login;
	private String username;
	private String password;
	private TextView otherlogin;
	public static Location lo;
	private Map<String,Object> data=new HashMap<String,Object>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
		loginLogo=(ImageView)this.findViewById(R.id.logo);
		login_more=(ImageView)this.findViewById(R.id.login_more);
		loginaccount=(EditText)this.findViewById(R.id.loginaccount);
		loginpassword=(EditText)this.findViewById(R.id.loginpassword);
		otherlogin=(TextView)findViewById(R.id.login_otherpassword);
		lo=new Location(LoginActivity.this);
		
		ThirdPartyManager.setOnLoginSucceed(new ThirdPartyManager.OnLoginSucceed() {

			@Override
			public void onSucceed() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, ControlActivity.class);
				LoginActivity.this.startActivity(intent);
			}
		});
		otherlogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ThirdPartyManager.state == ThirdPartyManager.STATE_LOGIN) {
					Toast.makeText(LoginActivity.this, "您已经登录，不要重复登录", Toast.LENGTH_LONG).show();
					return;
				}
				ThirdPartyManager.setLocation(lo.GetLatitude(), lo.Getlongtitude());
				ThirdPartyManager.platform = ThirdPartyManager.PLATFORM_SINAWEIBO;
				ThirdPartyManager.task = ThirdPartyManager.TASK_LOGIN;
				Intent intent = new Intent(LoginActivity.this, AuthorizeActivity.class);
				LoginActivity.this.startActivity(intent);
				finish();
			}
		});
		
		isShowPassword=(ToggleButton)this.findViewById(R.id.isShowPassword);
		loginBtn=(Button)this.findViewById(R.id.login);
		register=(Button)this.findViewById(R.id.register);		
		PushConfig.init(this);
		getpassword=loginpassword.getText().toString();
		init();
	}
	
	protected void onDestroy(){
		super.onDestroy();
	}
	
	protected void init() {
		isShowPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					loginpassword.setInputType(0x90);    
					//loginpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}else{
					loginpassword.setInputType(0x81); 
					//loginpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
	
		register.setOnClickListener(this);
		loginBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login=new Login();
				login.execute();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.register:
					mIntent=new Intent(LoginActivity.this, RegisterActivity.class);
					startActivity(mIntent);
					break;
		
			case R.id.login:
					
					break;
				
			default:
					break;
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	private class Login extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) { 
//        	data.put("username",loginaccount.getText().toString().trim()); 
//            data.put("password",loginpassword.getText().toString().trim());
            
            //免登录简易版
        	data.put("username","qwe".trim()); 
            data.put("password","111111".trim());
            
            data.put("longitude", lo.Getlongtitude());
            data.put("latitude", lo.GetLatitude());
            return PushSender.sendMessage("login",data);
        }
        @Override
        protected void onPreExecute() {   
        	
        }
        @Override
        protected void onPostExecute(String result) {   	
        	if(result.equals("network error")){
        		Toast.makeText(LoginActivity.this,"您还没有联网", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	if(result.equals("error")){
        		Toast.makeText(LoginActivity.this,"连接服务器失败", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
            try {
            	switch (new JSONObject(result).getInt("state")) {
            	case 1:
            		Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
            		break;
            	case 2:
            		Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
            		break;
            	case 3:
            		Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//            		PushConfig.username = loginaccount.getText().toString().trim();
            		
            		//免登录简易版
            		PushConfig.username = "qwe".trim();
            		
            		PushSender.sendClientId();
    				startActivity(new Intent(LoginActivity.this,ControlActivity.class));
    				finish();
            		break;
            	default:
            		Toast.makeText(LoginActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
            	}
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            super.onPostExecute(result);
        }
    }
	
	
}
