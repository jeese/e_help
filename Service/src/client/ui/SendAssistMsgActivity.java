package client.ui;

import android.app.Activity;
import android.content.res.Resources.NotFoundException;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import communicate.PushConfig;
import communicate.PushSender;
import org.json.JSONException;
import org.json.JSONObject;
import routeplan.Location;

import java.util.HashMap;
import java.util.Map;


public class SendAssistMsgActivity extends Activity{

	private static final String[] sen={"�������", "���ż�,���ϵ�","����·����","��������","��������"};
	private EditText edt;
	private Spinner spinner;
	private Button sendmessage,close;
	private ArrayAdapter<String> adapter;
    private Send send;
    private ProgressBar pro;
    private String audio_file="", video_file="";
    private Location lo;
    private MediaPlayer audio_player = new MediaPlayer();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sendassistmsg);
		close=(Button)findViewById(R.id.close);
		pro=(ProgressBar)findViewById(R.id.progressBar1);
		edt = (EditText) findViewById(R.id.send_msg);
		spinner = (Spinner) findViewById(R.id.common_sentence);
		sendmessage=(Button)findViewById(R.id.sendmessage);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, sen);
		lo=new Location(SendAssistMsgActivity.this);
		
		//���������б�ķ��
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	    //��adapter ��ӵ�spinner��
	    spinner.setAdapter(adapter);
	    
	    //����¼�Spinner�¼����� 
	    spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
	    
	    sendmessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pro.setVisibility(View.VISIBLE);
			    send=new Send();
			    send.execute();
				
			}
		});
	    
	    close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}
	
	//ʹ��������ʽ����

	class SpinnerSelectedListener implements OnItemSelectedListener{

	    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	    	
	    	if (arg2 != 0)
	    		edt.setText(edt.getText().toString() + sen[arg2] + "...");
	    	
	    }

	    public void onNothingSelected(AdapterView<?> arg0) {

	    }
	}
	
	private class Send extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) { 
        	Map<String,Object> a=new HashMap<String, Object>();
			a.put("content", edt.getText().toString());
			Map<String,Object> b= new HashMap<String, Object>();
			b.put("username",PushConfig.username);
			b.put("eid",DetailMessageActivity.GetEid());
			b.put("message", a);
			
            return PushSender.sendMessage("sendsupport", b);
        }
        @Override
        protected void onPreExecute() {   
        	
        }
        @Override
        protected void onPostExecute(String result) {   	
        	
        	if(result.equals("network error")){
        		Toast.makeText(SendAssistMsgActivity.this,"����û������", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	if(result.equals("error")){
        		Toast.makeText(SendAssistMsgActivity.this,"���ӷ�����ʧ��", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
            try {
            	switch (new JSONObject(result).getInt("errorCode")) {
            	case 200:
            		Toast.makeText(SendAssistMsgActivity.this, "���ͳɹ�", Toast.LENGTH_SHORT).show();
            		ControlActivity.SetPager(1);
            		finish();
            		break;
            	default:
            		Toast.makeText(SendAssistMsgActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
            	}
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            pro.setVisibility(View.INVISIBLE);
            super.onPostExecute(result);
        }
    }

}
