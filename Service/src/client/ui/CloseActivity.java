package client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.CloseAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import communicate.PushConfig;
import communicate.PushSender;

public class CloseActivity extends ListActivity {

	private ListView mlist;
	private Button makecomment;
	private Bundle bundle;
	private List<Map<String,Object>> datalist=new ArrayList<Map<String,Object>>();
	private Map<String,Object> data=new HashMap<String,Object>();
	private CloseAdapter closeadapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.close);
		closeadapter=new CloseAdapter(CloseActivity.this);
		mlist = (ListView)findViewById(android.R.id.list);
		makecomment=(Button) findViewById(R.id.button1);
		closeadapter.setData(datalist);
		mlist.setAdapter(closeadapter);
		bundle=this.getIntent().getExtras();
//		ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//			
//			@Override
//			public void onRatingChanged(RatingBar ratingBar, float rating,
//					boolean fromUser) {
//				// TODO Auto-generated method stub
//				
//				
//			}
//		});'
		setData();
		
		makecomment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Evaluate evaluate=new Evaluate();
				evaluate.execute();
				
			}
		});
	}
	
	private void setData(){
		String result=bundle.getString("result");
		datalist.clear();
		if(result.equals("network error")){
    		Toast.makeText(CloseActivity.this,"您还没有联网", Toast.LENGTH_SHORT).show();
        	//pro.setVisibility(View.INVISIBLE);
    	}
    	if(result.equals("error")){
    		Toast.makeText(CloseActivity.this,"连接服务器失败", Toast.LENGTH_SHORT).show();
        	//pro.setVisibility(View.INVISIBLE);
    	}
		try {
			JSONObject json=new JSONObject(result);
			switch(json.getInt("state")){
			case 1:
				Toast.makeText(CloseActivity.this,"不存在此事件", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(CloseActivity.this,"用户没有权限", Toast.LENGTH_SHORT).show();
				break;
			default:
				JSONArray array=json.getJSONArray("helpers");
				for(int i=0;i<array.length();i++){
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("title", array.getJSONObject(i).getString("username"));
					map.put("img", R.drawable.img01);
					map.put("evalue","0");
					datalist.add(map);
				}
				closeadapter.notifyDataSetChanged();
				break;
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class Evaluate extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) { 
        	List<Map<String,Object>> credits=new ArrayList<Map<String,Object>>();
        	for(int i=0;i<datalist.size();i++){
        		Map<String,Object> map=new HashMap<String,Object>();
        		map.put("username",datalist.get(i).get("title").toString());
        		map.put("cridit",datalist.get(i).get("evalue"));
        		credits.add(map);
        	}
        	data.clear();
        	data.put("eventid",DetailMessageActivity.GetEid());
        	data.put("credits",credits);
 
            return PushSender.sendMessage("givecredit",data);
        }
        @Override
        protected void onPreExecute() {   
        	
        }
        @Override
        protected void onPostExecute(String result) {   	
        	if(result.equals("network error")){
        		Toast.makeText(CloseActivity.this,"您还没有联网", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	if(result.equals("error")){
        		Toast.makeText(CloseActivity.this,"连接服务器失败", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	Toast.makeText(CloseActivity.this,"评价成功", Toast.LENGTH_SHORT).show();
        	finish();
            super.onPostExecute(result);
        }
    }
	
	
}
