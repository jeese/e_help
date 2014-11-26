package client.ui;



import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import communicate.PushConfig;
import communicate.PushSender;


public class SetupActivity extends Activity {
	
	RelativeLayout info;
	RelativeLayout about;
	RelativeLayout feedback,changepass,exit,bind,logoff;
    private Map<String,Object>data=new HashMap<String,Object>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setup);
		
		info = (RelativeLayout)findViewById(R.id.info);
		about = (RelativeLayout)findViewById(R.id.aboutus);
		feedback = (RelativeLayout)findViewById(R.id.feedback);
		changepass = (RelativeLayout)findViewById(R.id.change);
		exit=(RelativeLayout)findViewById(R.id.exit);
		bind = (RelativeLayout)findViewById(R.id.binding);
		logoff=(RelativeLayout)findViewById(R.id.logoff);
		logoff.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                Intent intent = new Intent(); 
                intent.setClass(SetupActivity.this, LogOffActivity.class);
                startActivityForResult(intent,0); 
			}
			
		});
		info.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                Intent intent = new Intent(); 
                intent.setClass(SetupActivity.this, PersonalInfoActivity.class);
                startActivity(intent); 
			}
			
		});
		about.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                Intent intent = new Intent(); 
                intent.setClass(SetupActivity.this, aboutYiZhu.class);
                startActivity(intent); 
			}
			
		});
		feedback.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                Intent intent = new Intent(); 
                intent.setClass(SetupActivity.this, Feekback.class);
                startActivity(intent); 
			}
			
		});
		changepass.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                Intent intent = new Intent(); 
                intent.setClass(SetupActivity.this, ChgPwdActivity.class);
                startActivity(intent); 
			}
			
		});
		
		exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Exit e=new Exit();
				e.execute();

			}
		});
		
		bind.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                Intent intent = new Intent(); 
                intent.setClass(SetupActivity.this, Binding_number.class);
                startActivity(intent); 
			}
			
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
        if (keyCode == KeyEvent.KEYCODE_BACK
                 && event.getRepeatCount() == 0) {
        	setResult(1,getIntent());
        	finish();
            return true;
         }
         return super.onKeyDown(keyCode, event);
     }
	
	private class Exit extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) { 
        	data.clear();
        	data.put("username", PushConfig.username);
            return PushSender.sendMessage("logout",data);
        }
        @Override
        protected void onPreExecute() {   
        	
        }
        @Override
        protected void onPostExecute(String result) {  
        	if(result.equals("network error")){
        		Toast.makeText(SetupActivity.this,"您还没有联网", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	if(result.equals("error")){
        		Toast.makeText(SetupActivity.this,"连接服务器失败", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
            try {
            	JSONObject json = new JSONObject(result);
            	switch (json.getInt("state")) {
            	
            	case 1:
            		Toast.makeText(SetupActivity.this,"退出成功", Toast.LENGTH_SHORT).show();
            		setResult(0,getIntent());
            		finish();
            		break;
            	default:
            		Toast.makeText(SetupActivity.this,"退出失败", Toast.LENGTH_SHORT).show();
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
		   case 0:
			   setResult(0,getIntent());
			   finish();
			   break;
		   default:
			   break;
		    }
		}
}
