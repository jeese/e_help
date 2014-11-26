package client.ui;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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

public class LogOffActivity extends Activity {
	private Button makesure,cancel;
	private EditText userET,passwordET;
	private Map<String,Object> data = new HashMap<String,Object>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.log_off);
        
        makesure=(Button)findViewById(R.id.log_off_ack);
        cancel=(Button)findViewById(R.id.log_off_cancel);
        userET=(EditText)findViewById(R.id.log_off_user);
        passwordET=(EditText)findViewById(R.id.log_off_password);
        userET.setText(PushConfig.username);
        cancel.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				setResult(1,getIntent());
				finish();
			}
		});
        
        makesure.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				Cancel can=new Cancel();
				can.execute();
				}
		});
    }
    
    private class Cancel extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) { 
        	data.clear();
        	data.put("username", userET.getText().toString());
        	data.put("password", passwordET.getText().toString());
            return PushSender.sendMessage("cancel",data);
        }
        @Override
        protected void onPreExecute() {   
        	
        }
        @Override
        protected void onPostExecute(String result) {  
        	if(result.equals("network error")){
        		Toast.makeText(LogOffActivity.this,"您还没有联网", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	if(result.equals("error")){
        		Toast.makeText(LogOffActivity.this,"连接服务器失败", Toast.LENGTH_SHORT).show();  
            	//pro.setVisibility(View.INVISIBLE);
        	}
            try {
            	JSONObject json = new JSONObject(result);
            	switch (json.getInt("state")) {   	
            	case 3:
            		Toast.makeText(LogOffActivity.this,"注销成功", Toast.LENGTH_SHORT).show();
            		setResult(0,getIntent());
            		finish();
            		break;
            	case 2:
            		Toast.makeText(LogOffActivity.this,"密码错误", Toast.LENGTH_SHORT).show();
            	default:
            		Toast.makeText(LogOffActivity.this,"用户名错误", Toast.LENGTH_SHORT).show();
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
