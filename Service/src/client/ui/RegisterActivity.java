package client.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import communicate.PushConfig;
import communicate.PushSender;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity implements OnClickListener{

	private EditText userId,password,password1;
	private Button cancel,register;
	private Map<String,Object> data=new HashMap<String,Object>();
	private Regis regis;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		init();
		

	}
	
	//��ʼ���ؼ�
	private void init() {
		userId=(EditText)findViewById(R.id.user);
		password=(EditText)findViewById(R.id.password);
		password1=(EditText)findViewById(R.id.password2);
		cancel=(Button)findViewById(R.id.cancel);
		register=(Button)findViewById(R.id.register);
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				regis=new Regis();
				regis.execute();
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	private class Regis extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) { 
        	data.put("username",userId.getText().toString()); 
            data.put("password",password.getText().toString());
            data.put("password1",password1.getText().toString());
            return PushSender.sendMessage("register",data);
        }
        @Override
        protected void onPreExecute() {   
        	
        }
        @Override
        protected void onPostExecute(String result) {   	
        	if(result.equals("network error")){
        		Toast.makeText(RegisterActivity.this,"����û������", Toast.LENGTH_SHORT).show();
        	}
        	if(result.equals("error")){
        		Toast.makeText(RegisterActivity.this,"���ӷ�����ʧ��", Toast.LENGTH_SHORT).show();
        	}
            super.onPostExecute(result);
            try {
            	switch (new JSONObject(result).getInt("state")) {
            	case 1:
            		Toast.makeText(RegisterActivity.this, "�û����Ѿ�����", Toast.LENGTH_SHORT).show();
            		break;
            	case 2:
            		Toast.makeText(RegisterActivity.this, "�û�����ʽ����", Toast.LENGTH_SHORT).show();
            		break;
            	case 3:
            		Toast.makeText(RegisterActivity.this, "�����������벻һ��", Toast.LENGTH_SHORT).show();
            		break;
            	case 4:
            		Toast.makeText(RegisterActivity.this, "���볤�Ȳ�����Ҫ��", Toast.LENGTH_SHORT).show();
            		break;
            	default:
            		Toast.makeText(RegisterActivity.this, "ע��ɹ�", Toast.LENGTH_SHORT).show();
            		PushConfig.username = userId.getText().toString();
					PushSender.sendClientId();
					Intent intent = new Intent(RegisterActivity.this,PerfectInfomationActivity.class);
            		startActivity(intent);
            		finish();
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}

