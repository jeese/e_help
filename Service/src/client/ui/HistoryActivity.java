package client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.AssistHistoryAdapter;
import adapter.HelpHistoryAdapter;
import android.app.TabActivity;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import communicate.PushConfig;
import communicate.PushSender;

public class HistoryActivity extends TabActivity {

	private ListView listView1, listView2;
	private HelpHistoryAdapter helpHistoryAdapter;
	private AssistHistoryAdapter assistHistoryAdapter;
	private List<Map<String, Object>> helpdatalist=new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> assistdatalist=new ArrayList<Map<String, Object>>();
	private Map<String,Object>data=new HashMap<String,Object>();
	private String photo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		TabHost tabhost = getTabHost();
		LayoutInflater.from(this).inflate(R.layout.history_main, tabhost.getTabContentView(), true);
		
		// 添加第一个标签页
		tabhost.addTab(tabhost.newTabSpec("tab1").setIndicator("求助历史").setContent(R.id.tab1));
		listView1 = (ListView)findViewById(R.id.askhelp_history);
		helpHistoryAdapter = new HelpHistoryAdapter(this, helpdatalist);
		listView1.setAdapter(helpHistoryAdapter);
		
		// 添加第二个标签页
		tabhost.addTab(tabhost.newTabSpec("tab2").setIndicator("援助历史").setContent(R.id.tab2));
		listView2 = (ListView)findViewById(R.id.assist_history);
		assistHistoryAdapter = new AssistHistoryAdapter(this, assistdatalist);
		listView2.setAdapter(assistHistoryAdapter);
		GetHistory gethistory=new GetHistory();
		gethistory.execute();
	}
	
	private class GetHistory extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) { 
        	helpdatalist.clear();
        	assistdatalist.clear();
        	data.clear();
        	data.put("username", PushConfig.username);
        	String r=PushSender.sendMessage("getavatar",data);
        	try {
				photo=new JSONObject(r).getString("avatar");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return PushSender.sendMessage("history",data);
        }
        @Override
        protected void onPreExecute() {   
        	
        }
        @Override
        protected void onPostExecute(String result) {  
        	Log.i("hh",result);
        	if(result.equals("network error")){
        		Toast.makeText(HistoryActivity.this,"您还没有联网", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	if(result.equals("error")){
        		Toast.makeText(HistoryActivity.this,"连接服务器失败", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
            try {
            	JSONObject json = new JSONObject(result);
            	Log.i("test",json.toString());
            	switch (json.getInt("state")) {
            	
            	case 1:
            		JSONArray array = json.getJSONArray("events");
            		for (int i = 0; i < array.length(); i++) {
            			Map<String, Object> map = new HashMap<String, Object>();    
            	        map.put("image",photo);                 
            	        map.put("name",PushConfig.username);              
            	        map.put("time",array.getJSONObject(i).getString("starttime"));             
            	        map.put("content", array.getJSONObject(i).getString("content"));
            	        map.put("eid", array.getJSONObject(i).getString("id"));
            	        helpdatalist.add(map);    
            		}
            		array = json.getJSONArray("supports");
            		for (int i = 0; i < array.length(); i++) {
            			Map<String, Object> map = new HashMap<String, Object>();    
            	        map.put("image",photo);                 
            	        map.put("name",PushConfig.username);              
            	        map.put("time",array.getJSONObject(i).getString("time"));             
            	        map.put("content", array.getJSONObject(i).getString("content"));
            	        map.put("eid", array.getJSONObject(i).getString("eid"));
            	        assistdatalist.add(map);    
            		}
            		helpHistoryAdapter.notifyDataSetChanged();
            		assistHistoryAdapter.notifyDataSetChanged();
            		Toast.makeText(HistoryActivity.this, "历史列表刷新成功", Toast.LENGTH_SHORT).show();
            		break;
            	default:
            		Toast.makeText(HistoryActivity.this, "历史列表刷新失败", Toast.LENGTH_SHORT).show();
            	}
			} catch (NotFoundException e) {
				Log.i("hhhhhh",e.toString());
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.i("hhhhhh",e.toString());
				e.printStackTrace();
			}
            super.onPostExecute(result);
        }
    }
	

}
