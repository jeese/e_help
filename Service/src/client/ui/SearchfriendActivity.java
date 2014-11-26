package client.ui;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import base.FindedUserList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import communicate.PushSender;

public class SearchfriendActivity extends Activity {

	Spinner sexSpinner;
	Spinner ageSpinner;
	Spinner typeSpinner;
	TextView exactsearch,keywordSearch;
	LinearLayout nearbySearch;
	EditText idnum;
	EditText address;

	ArrayAdapter<String> adapter;
	
	private static final String[] sexs={"不限","男","女"};
	private static final String[] ages={"不限","15-22岁","23-30岁","31-45岁","45岁以上"};
	private static final String[] types={"普通用户","志愿者","小区保安","安全机构","医疗机构","其它机构"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.searchfriend);
		
		
		sexSpinner=(Spinner)findViewById(R.id.sex);
		ageSpinner=(Spinner)findViewById(R.id.age);
		typeSpinner=(Spinner)findViewById(R.id.userType);
		
		idnum=(EditText)findViewById(R.id.idNumber);
		address=(EditText)findViewById(R.id.address);
		
		nearbySearch=(LinearLayout)findViewById(R.id.layout_nearby);
		exactsearch=(TextView)findViewById(R.id.textView_num);
		keywordSearch=(TextView)findViewById(R.id.textView_keyword);
		
		//精确查找
		exactsearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//startActivity(new Intent(SearchfriendActivity.this,Findfriendresult.class));
				ExactSearch search = new ExactSearch();
				search.execute();
				
			}
		});
		exactsearch.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_UP)
					exactsearch.setTextColor(Color.BLACK);
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					exactsearch.setTextColor(Color.BLUE);
				return false;
			}
		});
		
		//关键词查找
		keywordSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				KeywordSearch search = new KeywordSearch();
				search.execute();
				
			}
		});
		
		//查找附近的人
		nearbySearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NearbySearch search = new NearbySearch();
				search.execute();
			}
		});	
		keywordSearch.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_UP)
					keywordSearch.setTextColor(Color.BLACK);
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					keywordSearch.setTextColor(Color.BLUE);
				return false;
			}
		});
		
		
		//性别下拉列表
		//将可选内容与arrayadapter连接
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sexs);
		//设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//将adapter添加到spinner sex中
		sexSpinner.setAdapter(adapter);
		//添加spinner时间监听
		sexSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
			
			@Override
			public void onItemSelected(AdapterView<?> arg0,View arg1,int arg2,long arg3)
			{
				//设置显示当前选择的项
				arg0.setVisibility(View.VISIBLE);
			}
			public void onNothingSelected(AdapterView<?> arg0)
			{
				//TODO Auto-generated method stub
			}
		});
		
		//年龄下拉列表
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ages);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ageSpinner.setAdapter(adapter);
		ageSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
			
			@Override
			public void onItemSelected(AdapterView<?> arg0,View arg1,int arg2,long arg3)
			{
				//text.setText("性别是"+ages[arg2]);
				//设置显示当前选择的项
				arg0.setVisibility(View.VISIBLE);
			}
			public void onNothingSelected(AdapterView<?> arg0)
			{
				//TODO Auto-generated method stub
			}
		});
		
		//用户类型下拉列表
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,types);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeSpinner.setAdapter(adapter);
		typeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
			
			@Override
			public void onItemSelected(AdapterView<?> arg0,View arg1,int arg2,long arg3)
			{
				//text.setText("性别是"+ages[arg2]);
				//设置显示当前选择的项
				arg0.setVisibility(View.VISIBLE);
			}
			public void onNothingSelected(AdapterView<?> arg0)
			{
				//TODO Auto-generated method stub
			}
		});
	}
	
	
	//精确查找
	private class ExactSearch extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			Map<String, Object> data = new HashMap<String, Object>();
			
			data.put("searchtype", "exactSearch");
			data.put("username", idnum.getText().toString());
			
			String result = PushSender.sendMessage("search", data);
			
			
			return result;
		}
		
		@Override
	    protected void onPostExecute(String result) {
			//Toast.makeText(SearchfriendActivity.this, result, Toast.LENGTH_LONG).show();
			//Log.e("res", result);
        	if(result.equals("network error")){
        		Toast.makeText(SearchfriendActivity.this,"您还没有联网", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	if(result.equals("error")){
        		Toast.makeText(SearchfriendActivity.this,"连接服务器失败", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
            try {        	
            	switch (new JSONObject(result).getInt("state")) {
            	case 0:
            		Toast.makeText(SearchfriendActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
            		break;
            	case 1:
            		
            		//startActivity(new Intent(SearchfriendActivity.this,Findfriendresult.class));
            		Toast.makeText(SearchfriendActivity.this, "找到用户", Toast.LENGTH_SHORT).show();
            		//数据处理
         		
            		FindedUserList userList = new FindedUserList(); 
            		try {   		
            			//Toast.makeText(SearchfriendActivity.this,"进入try", Toast.LENGTH_SHORT).show();
            			JSONObject obj = new JSONObject(result);
            			//Toast.makeText(SearchfriendActivity.this,"解析1了", Toast.LENGTH_SHORT).show();
            			JSONArray users = obj.getJSONArray("users");
	            		//Toast.makeText(SearchfriendActivity.this,"解析2了", Toast.LENGTH_SHORT).show();
	            		for(int i = 0; i < users.length(); i++)
	            		{
	            			//Toast.makeText(SearchfriendActivity.this,"进入循环", Toast.LENGTH_SHORT).show();
	            			Map<String, Object> map = new HashMap<String, Object> ();
	            			map.put("img", R.drawable.img21);
	            			map.put("name", users.getJSONObject(i).getString("username"));
	            			map.put("detail", "没实现");
	            			userList.userList.add(map);
	            		}	   
     		
            		} catch (JSONException e) {
        				// TODO Auto-generated catch block
            			//Toast.makeText(SearchfriendActivity.this,"解析错误了", Toast.LENGTH_SHORT).show();
        				e.printStackTrace();
        			}
            		
            		Intent intent = new Intent();
            		Bundle bundle = new Bundle();
            		bundle.putSerializable("users", userList);
            		intent.setClass(SearchfriendActivity.this, Findfriendresult.class);
            		intent.putExtras(bundle);
            		startActivity(intent);
            		 		
    				//startActivity(new Intent(SearchfriendActivity.this,Findfriendresult.class));
            		break;
            	default:
            		Toast.makeText(SearchfriendActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
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
	
	//关键词查找
	private class KeywordSearch extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			Map<String, Object> data = new HashMap<String, Object>();
			
			data.put("searchtype", "keywordSearch");
			
			//性别
			String sexStr = sexSpinner.getSelectedItem().toString();
			if(!sexStr.equals(sexs[0])){
				if (sexStr.equals(sexs[1]))
					data.put("sex", "1");
				else
					data.put("sex", "2");
			}
				
			
			//年龄
			String ageStr = ageSpinner.getSelectedItem().toString();
			if(!ageStr.equals(ages[0]))
			{
				if(ageStr.equals(ages[1]))
				{
					data.put("fromage", "15");
					data.put("endage", "22");
				}
				else if(ageStr.equals(ages[2]))
				{
					data.put("fromage", "23");
					data.put("endage", "30");
				}
				else if(ageStr.equals(ages[3]))
				{
					data.put("fromage", "31");
					data.put("endage", "45");
				}
				else if(ageStr.equals(ages[4]))
				{
					data.put("fromage", "45");
					data.put("endage", "100");
				}
				
			}
			
			//用户类型
			String typeStr = typeSpinner.getSelectedItem().toString();
			if(typeStr.equals(types[0]))
				data.put("kind", "1");
			else if(typeStr.equals(types[1]))
				data.put("kind", "2");
			else if(typeStr.equals(types[2]))
				data.put("kind", "3");
			else if(typeStr.equals(types[3]))
				data.put("kind", "4");
			else if(typeStr.equals(types[4]))
				data.put("kind", "5");
			else
				data.put("kind", "6");
			return PushSender.sendMessage("search", data);
		}
		
		@Override
	    protected void onPostExecute(String result) { 
			Toast.makeText(SearchfriendActivity.this, result, Toast.LENGTH_SHORT).show();
			
        	if(result.equals("network error")){
        		Toast.makeText(SearchfriendActivity.this,"您还没有联网", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	if(result.equals("error")){
        		Toast.makeText(SearchfriendActivity.this,"连接服务器失败", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
            try {        	
            	switch (new JSONObject(result).getInt("state")) {
            	case 0:
            		Toast.makeText(SearchfriendActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
            		break;
            	case 1:
            		
            		//startActivity(new Intent(SearchfriendActivity.this,Findfriendresult.class));
            		Toast.makeText(SearchfriendActivity.this, "找到用户", Toast.LENGTH_SHORT).show();
            		//数据处理
         		
            		FindedUserList userList = new FindedUserList(); 
            		try {   		
            			//Toast.makeText(SearchfriendActivity.this,"进入try", Toast.LENGTH_SHORT).show();
            			JSONObject obj = new JSONObject(result);
            			//Toast.makeText(SearchfriendActivity.this,"解析1了", Toast.LENGTH_SHORT).show();
            			JSONArray users = obj.getJSONArray("users");
	            		//Toast.makeText(SearchfriendActivity.this,"解析2了", Toast.LENGTH_SHORT).show();
	            		for(int i = 0; i < users.length(); i++)
	            		{
	            			//Toast.makeText(SearchfriendActivity.this,"进入循环", Toast.LENGTH_SHORT).show();
	            			Map<String, Object> map = new HashMap<String, Object> ();
	            			map.put("img", R.drawable.img21);
	            			map.put("name", users.getJSONObject(i).getString("username"));
	            			map.put("detail", "没实现");
	            			userList.userList.add(map);
	            		}	   
     		
            		} catch (JSONException e) {
        				// TODO Auto-generated catch block
            			//Toast.makeText(SearchfriendActivity.this,"解析错误了", Toast.LENGTH_SHORT).show();
        				e.printStackTrace();
        			}
            		
            		Intent intent = new Intent();
            		Bundle bundle = new Bundle();
            		bundle.putSerializable("users", userList);
            		intent.setClass(SearchfriendActivity.this, Findfriendresult.class);
            		intent.putExtras(bundle);
            		startActivity(intent);
            		 		
    				//startActivity(new Intent(SearchfriendActivity.this,Findfriendresult.class));
            		break;
            	default:
            		Toast.makeText(SearchfriendActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
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
	
	//附近查找
	private class NearbySearch extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			Map<String, Object> data = new HashMap<String, Object>();
			
			data.put("searchtype", "nearbySearch");
			
			data.put("longitude", String.valueOf(LoginActivity.lo.Getlongtitude()));
			data.put("latitude", String.valueOf(LoginActivity.lo.GetLatitude()));

			return PushSender.sendMessage("search", data);
		}
		
		@Override
	    protected void onPostExecute(String result) { 
			
			//Toast.makeText(SearchfriendActivity.this, result, Toast.LENGTH_SHORT).show();
			
        	if(result.equals("network error")){
        		Toast.makeText(SearchfriendActivity.this,"您还没有联网", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	if(result.equals("error")){
        		Toast.makeText(SearchfriendActivity.this,"连接服务器失败", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
            try {        	
            	switch (new JSONObject(result).getInt("state")) {
            	case 0:
            		Toast.makeText(SearchfriendActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
            		break;
            	case 1:
            		
            		//startActivity(new Intent(SearchfriendActivity.this,Findfriendresult.class));
            		Toast.makeText(SearchfriendActivity.this, "找到用户", Toast.LENGTH_SHORT).show();
            		//数据处理
         		
            		FindedUserList userList = new FindedUserList(); 
            		try {   		
            			//Toast.makeText(SearchfriendActivity.this,"进入try", Toast.LENGTH_SHORT).show();
            			JSONObject obj = new JSONObject(result);
            			//Toast.makeText(SearchfriendActivity.this,"解析1了", Toast.LENGTH_SHORT).show();
            			JSONArray users = obj.getJSONArray("users");
	            		//Toast.makeText(SearchfriendActivity.this,"解析2了", Toast.LENGTH_SHORT).show();
	            		for(int i = 0; i < users.length(); i++)
	            		{
	            			//Toast.makeText(SearchfriendActivity.this,"进入循环", Toast.LENGTH_SHORT).show();
	            			Map<String, Object> map = new HashMap<String, Object> ();
	            			map.put("img", R.drawable.img21);
	            			map.put("name", users.getJSONObject(i).getString("username"));
	            			map.put("detail", "没实现");
	            			userList.userList.add(map);
	            		}	   
     		
            		} catch (JSONException e) {
        				// TODO Auto-generated catch block
            			//Toast.makeText(SearchfriendActivity.this,"解析错误了", Toast.LENGTH_SHORT).show();
        				e.printStackTrace();
        			}
            		
            		Intent intent = new Intent();
            		Bundle bundle = new Bundle();
            		bundle.putSerializable("users", userList);
            		intent.setClass(SearchfriendActivity.this, Findfriendresult.class);
            		intent.putExtras(bundle);
            		startActivity(intent);
            		 		
    				//startActivity(new Intent(SearchfriendActivity.this,Findfriendresult.class));
            		break;
            	default:
            		Toast.makeText(SearchfriendActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
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
