package client.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import base.FindedUserList;

import communicate.PushConfig;
import communicate.PushSender;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Binding_number extends Activity {
	Button btn1;
	Button getcaptcha,Checkcaptcha;
	EditText email,phone,captcha;
	TextView emailtext,phonetext,captchatext;
	GetBindState g;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.binding_phone_or_email);
		
		btn1 = (Button)findViewById(R.id.sendemail);
		getcaptcha = (Button)findViewById(R.id.get_captcha);
		Checkcaptcha = (Button)findViewById(R.id.check_captcha);
		phone = (EditText)findViewById(R.id.getphone);
		captcha = (EditText)findViewById(R.id.captcha);
		email = (EditText)findViewById(R.id.email);
		emailtext = (TextView)findViewById(R.id.emailtextview);
		phonetext = (TextView)findViewById(R.id.phonetextview);
		captchatext = (TextView)findViewById(R.id.captchatext);
		
		
		btn1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isEmail(email.getText().toString()))
				{
					AuthEmail search = new AuthEmail() ;
					btn1.setClickable(false);
					btn1.setText("正在发送中");
					search.execute();
				}
				else
				{
					email.setText("");
					email.setHint("邮箱格式错误");
				}
			}			
		});
		
		getcaptcha.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isMobileNum(phone.getText().toString()))
				{
					GetCaptcha search = new GetCaptcha() ;
					btn1.setClickable(false);
					getcaptcha.setText("正在检验中");
					search.execute();
				}
				else{
					phone.setText("");
					phone.setHint("手机号码格式错误");
				}
			}			
		});
		
		Checkcaptcha.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 

				CheckCaptcha search = new CheckCaptcha() ;
				Checkcaptcha.setClickable(false);
				Checkcaptcha.setText("正在发送中");
				search.execute();

				}			
		});
		
		g = new GetBindState();
		g.execute();
	}

	class AuthEmail extends AsyncTask<Void, Void, String>{

	@Override
	protected String doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		Map<String, Object> data = new HashMap<String, Object>();
		
		String str = email.getText().toString();
		data.put("username", PushConfig.username);
		data.put("email", str);
		
		String result = PushSender.sendMessage("requestemailauth", data);
		Log.e("gj", str);
		
		return result;
	}
	
	@Override
    protected void onPostExecute(String result) {
		Log.i("res", result);
    	if(result.equals("network error")){
    		Toast.makeText(Binding_number.this,"您还没有联网", Toast.LENGTH_SHORT).show();
			btn1.setClickable(true);
			btn1.setText("重新发送");
        	//pro.setVisibility(View.INVISIBLE);
    	}
    	if(result.equals("error")){
    		Toast.makeText(Binding_number.this,"连接服务器失败", Toast.LENGTH_SHORT).show();
        	//pro.setVisibility(View.INVISIBLE);
    	}
        try {
        	JSONObject j = new JSONObject(result);
        	if (j.has("result") && j.getString("result").equals("OK")) {
        		// succeed
        		Toast.makeText(Binding_number.this, "邮箱验证中！", Toast.LENGTH_SHORT).show();
        		btn1.setText("绑定中");
        		return;
        	}
        	switch (j.getInt("error_code")) {
        	case 1:
        		Toast.makeText(Binding_number.this, "缺少参数", Toast.LENGTH_SHORT).show();
    			btn1.setClickable(true);
    			btn1.setText("重新发送");
        		break;
        	case 2:
        		
        		//startActivity(new Intent(SearchfriendActivity.this,Findfriendresult.class));
        		Toast.makeText(Binding_number.this, "用户不存在", Toast.LENGTH_SHORT).show();
    			btn1.setClickable(true);
    			btn1.setText("重新发送");
        		break;
        	case 10001:
        		Toast.makeText(Binding_number.this, "您的邮箱已验证，请不要重复验证。", Toast.LENGTH_SHORT).show();
    			btn1.setText("已绑定");
        		break;

        	case 10002:
        		Toast.makeText(Binding_number.this, "您今日请求邮箱验证的次数超过上限，请明天再试。", Toast.LENGTH_SHORT).show();
    			btn1.setText("超过上限");
        		break;
        	case 10003:
        		Toast.makeText(Binding_number.this, "邮件发送失败。可能是邮箱填写错误。", Toast.LENGTH_SHORT).show();
    			btn1.setClickable(true);
    			btn1.setText("重新发送");
        		break;
        	default:
        	}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        super.onPostExecute(result);
	}
}

	class GetCaptcha extends AsyncTask<Void, Void, String>{

	@Override
	protected String doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		Map<String, Object> data = new HashMap<String, Object>();
		
		String str = phone.getText().toString();
		data.put("username", PushConfig.username);
		data.put("phone", str);
		
		String result = PushSender.sendMessage("requestphoneauth", data);
		Log.e("gj", str);
		
		return result;
	}
	
	@Override
    protected void onPostExecute(String result) {
		Log.i("res", result);
    	if(result.equals("network error")){
    		Toast.makeText(Binding_number.this,"您还没有联网", Toast.LENGTH_SHORT).show();
    		getcaptcha.setClickable(true);
    		getcaptcha.setText("重新发送");
        	//pro.setVisibility(View.INVISIBLE);
    	}
    	if(result.equals("error")){
    		Toast.makeText(Binding_number.this,"连接服务器失败", Toast.LENGTH_SHORT).show();
        	//pro.setVisibility(View.INVISIBLE);
    	}
        try {
        	JSONObject j = new JSONObject(result);
        	if (j.getString("result").equals("ok")) {
        		// succeed
        		Toast.makeText(Binding_number.this, "验证码已发送，请在10分钟内使用！", Toast.LENGTH_SHORT).show();
        		getcaptcha.setText("已发送");
        		return;
        	}
        	switch (j.getInt("error_code")) {
        	case 1:
        		Toast.makeText(Binding_number.this, "缺少参数", Toast.LENGTH_SHORT).show();
        		getcaptcha.setClickable(true);
        		getcaptcha.setText("重新发送");
        		break;
        	case 2:
        		
        		//startActivity(new Intent(SearchfriendActivity.this,Findfriendresult.class));
        		Toast.makeText(Binding_number.this, "用户不存在", Toast.LENGTH_SHORT).show();
        		getcaptcha.setClickable(true);
        		getcaptcha.setText("重新发送");
        		break;
        	case 30001:
        		Toast.makeText(Binding_number.this, "您的手机已验证，请不要重复验证。", Toast.LENGTH_SHORT).show();
        		getcaptcha.setText("已绑定");
        		break;

        	case 30002:
        		Toast.makeText(Binding_number.this, "您今日请求手机验证的次数超过上限，请明天再试。", Toast.LENGTH_SHORT).show();
        		getcaptcha.setText("申请超过上限");
        		break;
        	case 30003:
        		Toast.makeText(Binding_number.this, "手机验证码发送失败。可能是手机号码填写错误。", Toast.LENGTH_SHORT).show();
        		getcaptcha.setClickable(true);
        		getcaptcha.setText("重新发送");
        		break;
        	default:
        	}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        super.onPostExecute(result);
	}
}

	class CheckCaptcha extends AsyncTask<Void, Void, String>{

	@Override
	protected String doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		Map<String, Object> data = new HashMap<String, Object>();
		
		String str = captcha.getText().toString();
		data.put("username", PushConfig.username);
		data.put("code", str);
		
		String result = PushSender.sendMessage("authphone", data);
		Log.e("gj", str);
		
		return result;
	}
	
	@Override
    protected void onPostExecute(String result) {
		Log.i("res", result);
    	if(result.equals("network error")){
    		Toast.makeText(Binding_number.this,"您还没有联网", Toast.LENGTH_SHORT).show();
    		Checkcaptcha.setClickable(true);
    		Checkcaptcha.setText("重新发送");
        	//pro.setVisibility(View.INVISIBLE);
    	}
    	if(result.equals("error")){
    		Toast.makeText(Binding_number.this,"连接服务器失败", Toast.LENGTH_SHORT).show();
        	//pro.setVisibility(View.INVISIBLE);
    	}
        try {
        	JSONObject j = new JSONObject(result);
        	if (j.getString("result").equals("ok")) {
        		// succeed
        		Toast.makeText(Binding_number.this, "手机验证成功！", Toast.LENGTH_SHORT).show();
        		Checkcaptcha.setText("已绑定成功");
        		return;
        	}
        	switch (j.getInt("error_code")) {
        	case 1:
        		Toast.makeText(Binding_number.this, "缺少参数", Toast.LENGTH_SHORT).show();
        		Checkcaptcha.setClickable(true);
        		Checkcaptcha.setText("重新发送");
        		break;
        	case 2:
        		
        		//startActivity(new Intent(SearchfriendActivity.this,Findfriendresult.class));
        		Toast.makeText(Binding_number.this, "用户不存在", Toast.LENGTH_SHORT).show();
        		Checkcaptcha.setClickable(true);
        		Checkcaptcha.setText("重新发送");
        		break;
        	case 40001:
        		Toast.makeText(Binding_number.this, "验证失败", Toast.LENGTH_SHORT).show();
        		Checkcaptcha.setClickable(true);
        		Checkcaptcha.setText("重新发送");
        		break;

        	default:
        	}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        super.onPostExecute(result);
	}
}
	private class GetBindState extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) { 
        	Map<String, Object> data = new HashMap<String, Object>();
        	data.put("username",PushConfig.username); 
            return PushSender.sendMessage("authstate",data);
        }
        @Override
        protected void onPreExecute() {   
        	
        }
        @Override
        protected void onPostExecute(String result) {   
        	Log.i("res", result);
        	if(result.equals("network error")){
        		Toast.makeText(Binding_number.this,"您还没有联网", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	if(result.equals("error")){
        		Toast.makeText(Binding_number.this,"连接服务器失败", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
            try {
            	Log.i("res", "11");
				JSONObject info=new JSONObject(result);
				Log.i("res", "121");
				if(info.getString("email_state").equals("authed"))
				{
					Log.i("res", "44");
					btn1.setVisibility(View.INVISIBLE);
					emailtext.setText("已绑定的邮箱");
					email.setText(info.getString("email"));
					email.setEnabled(false);
				}
				else if(info.getString("email_state").equals("authing"))
				{
					Log.i("res", "22");
					emailtext.setText("绑定中的号码");
					email.setText(info.getString("email"));
					btn1.setText("重新发送");
				}
				else
				{
	            	
	            	Toast.makeText(Binding_number.this,"ok", Toast.LENGTH_SHORT).show();
				}
				if(info.getString("phone_state").equals("authed"))
				{
					phonetext.setText("已绑定的手机");
					phone.setText(info.getString("phone"));
					phone.setEnabled(false);
					captchatext.setVisibility(View.INVISIBLE);
					getcaptcha.setVisibility(View.INVISIBLE);
					captcha.setVisibility(View.INVISIBLE);
					Checkcaptcha.setVisibility(View.INVISIBLE);
				}
				else if(info.getString("phone_state").equals("authing"))
				{
					phonetext.setText("绑定中的号码");
					phone.setText(info.getString("phone"));
					getcaptcha.setText("重新发送");
				}
				else
				{
					
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            	
            super.onPostExecute(result);
        }
    }
	
	
	public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();

    }
	
	public boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
		}
	
}
