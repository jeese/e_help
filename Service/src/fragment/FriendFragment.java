package fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.FriendAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import base.friend;
import client.ui.FriendInfoActivity;
import client.ui.R;

import communicate.PushConfig;
import communicate.PushSender;

public class FriendFragment extends Fragment {
	
	private ExpandableListView mListView = null; 
    private FriendAdapter mAdapter = null; 
    public static List<List<friend>> mData; //表示用户信息的列表
    private EditText mSearchEditText = null;
    private ImageButton deleteBtn;
    private View view;
    private MenuItem item=null;
    
    private CheckRelatives checkRelatives;
    private int _groupPosition, _childPosition;
    
    //表示定义的四个分组
    public static String[] mGroupArrays = new String[] {  
            "亲人", 
            "好友"
    }; 

	public FriendFragment(){
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {       
  
		ViewGroup p = (ViewGroup) view.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
        } 

		return view;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		view=View.inflate(getActivity(),R.layout.friend,null); 
		
		if(mData == null)
			mData = new ArrayList<List<friend>>();
		else 
			mData.clear();
		initData(); 
		
		mListView = (ExpandableListView)view.findViewById(R.id.friendlist);
        //mListView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT)); 
        mAdapter = new FriendAdapter(getActivity(), mData, mGroupArrays);
        mListView.setAdapter(mAdapter); 

        
        mListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
					int childPosition, long arg4) {
				
				friend f = mAdapter.getChild(groupPosition, childPosition); 
				final String name = f.getName();
				
				_groupPosition = groupPosition;
				_childPosition = childPosition;
		
				final String[] friend_manage = new String[]{"查看亲友","删除亲友"};
				new AlertDialog.Builder(getActivity())
						.setItems(friend_manage, new DialogInterface.OnClickListener() {		
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								if(which == 0) //查看亲友信息
								{
									Bundle bundle = new Bundle();
									int x = _groupPosition;
									int y = _childPosition;
									bundle.putInt("x", x);
									bundle.putInt("y", y);
									
									//Toast.makeText(getActivity(), String.valueOf(x) + "," + String.valueOf(y), Toast.LENGTH_SHORT).show();
					
									Intent intent = new Intent(getActivity(),FriendInfoActivity.class);
									intent.putExtras(bundle);
									startActivity(intent);
								}
								else if(which == 1) //删除亲友
								{	
									DeleteRelatives deleteRelatives = new DeleteRelatives();
								    deleteRelatives.execute(name);
								}
							}
						}).show();
				
				return false;
			}
		});
        
        
		
		mSearchEditText = (EditText)view.findViewById(R.id.search);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
        	//当输入框的文字改变时，执行以下方法。
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                            int count) {
            	//当输入文字的时候才显示删除按钮
            	if(s.toString().isEmpty())
            	{
            		deleteBtn.setVisibility(View.GONE);
            	}
            	else 
            	{
            		deleteBtn.setVisibility(View.VISIBLE);
            	}
            	
                FriendAdapter adapter = null;
                List<List<friend>> data = new ArrayList<List<friend>>();
                for(int i = 0; i < mData.size(); i++)
                {
                	List<friend> list = new ArrayList<friend>(); 
                	for(int j = 0; j < mData.get(i).size(); j++)
                	{
                		//查询满足条件的联系人
                		String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(mData.get(i).get(j).getName().charAt(0));
                		if(mData.get(i).get(j).getName().toLowerCase().indexOf(s.toString()) != -1 ||
                				mData.get(i).get(j).getName().toUpperCase().indexOf(s.toString()) != -1 ||
                				(pinyin != null && pinyin[0].charAt(0) == s.toString().toLowerCase().charAt(0))
                				)
                		{
                			list.add(mData.get(i).get(j));
                		}
                	}
                	
                	data.add(list);
                }
                
                adapter = new FriendAdapter(getActivity(), data, mGroupArrays); 
                mListView.setAdapter(adapter);
                
                if(s.toString().isEmpty())
                	mListView.setAdapter(mAdapter);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {
                    // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

            }
		});
        
        //删除按钮，删除查询内容
        deleteBtn = (ImageButton)view.findViewById(R.id.delete_button);
        deleteBtn.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{				
					((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.delete_icon_down));
				}
				else if(event.getAction() == MotionEvent.ACTION_UP)
				{
					((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.delete_icon));
					mSearchEditText.setText(null);
				}
				return false;
			}
		});
        
	}
	
	//页面恢复时自动刷新
	public void Refresh(MenuItem item){
		this.item=item;
		CheckRelatives checkRelatives = new CheckRelatives();
		checkRelatives.execute();
	}
	@Override
	public void onResume()
	{
		//自动获取好友列表
        checkRelatives = new CheckRelatives();
		checkRelatives.execute();
		
		super.onResume();
	}
	@Override
	public void onDestroy()
	{
		if (checkRelatives != null && checkRelatives.getStatus() != AsyncTask.Status.FINISHED)
			checkRelatives.cancel(true);
		super.onPause();
	}
	@Override
	public void onPause()
	{
		if (checkRelatives != null && checkRelatives.getStatus() != AsyncTask.Status.FINISHED)
			checkRelatives.cancel(true);
		super.onPause();
	}
	
	//初始化mData的内容
	private void initData() {     
        for(int i = 0; i < mGroupArrays.length; i++)
        {
        	List<friend> list = new ArrayList<friend>(); 
        	mData.add(list);
        }
    } 
	
	//删除好友
	private class DeleteRelatives extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... arg0) {
			Map<String, Object> data = new HashMap<String, Object>();
			
			data.put("username1", PushConfig.username);
			data.put("username2", arg0[0]);
			
			String result = PushSender.sendMessage("deleterelatives", data);
							
			return result;
		}
		
		@Override
	    protected void onPostExecute(String result) {
			Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
			Log.d("res", result);
        	if(result.equals("network error")){
        		Toast.makeText(getActivity(),"您还没有联网", Toast.LENGTH_SHORT).show();
        	}
        	if(result.equals("error")){
        		Toast.makeText(getActivity(),"连接服务器失败", Toast.LENGTH_SHORT).show();
        	}
            try {        	
            	switch (new JSONObject(result).getInt("state")) {
            	case 1:
            		Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
            		
            		mData.get(_groupPosition).remove(_childPosition);
            		         	
            		mAdapter = new FriendAdapter(getActivity(), mData, mGroupArrays);
            		//mListView = findViewById(R.id.friendlist);
            		mListView.setAdapter(mAdapter);
            		
            		break;
            	case 0:
            		Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
            		
            		break;
            	default:
            		Toast.makeText(getActivity(), "服务器错误", Toast.LENGTH_SHORT).show();
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

	public ExpandableListView findViewById(int friendlist) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//请求好友列表
	private class CheckRelatives extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... arg0) {
			Map<String, Object> data = new HashMap<String, Object>();
			
			data.put("username", PushConfig.username);
			
			String result = PushSender.sendMessage("checkrelatives", data);
							
			return result;
		}
		
		@Override
	    protected void onPostExecute(String result) {
			//Toast.makeText(ControlActivity.this, result, Toast.LENGTH_LONG).show();
			//Log.d("res", result);
        	if(result.equals("network error")){
        		Toast.makeText(getActivity(),"您还没有联网", Toast.LENGTH_SHORT).show();
        	}
        	if(result.equals("error")){
        		Toast.makeText(getActivity(),"连接服务器失败", Toast.LENGTH_SHORT).show();
        	}
            try {        	
            	switch (new JSONObject(result).getInt("state")) {
            	case 1:
            		Toast.makeText(getActivity(), "好友列表刷新成功 ", Toast.LENGTH_SHORT).show();
    
            		//把mData清空
            		FriendFragment.mData.get(0).clear();
            		FriendFragment.mData.get(1).clear();
            		
            		//数据处理         		
            		try {   		
            			JSONObject obj = new JSONObject(result);
            			JSONArray users = obj.getJSONArray("relatives");
	            		for(int i = 0; i < users.length(); i++)
	            		{  			
	            			friend f = new friend(users.getJSONObject(i).getString("avatar"), users.getJSONObject(i).getString("name"), 
	            					users.getJSONObject(i).getString("phone"), users.getJSONObject(i).getString("illness"), 
	            								 users.getJSONObject(i).getString("realname"), 
	            								 users.getJSONObject(i).getString("age"), 
	            								 users.getJSONObject(i).getString("sex"), 
	            								 users.getJSONObject(i).getString("credit"), 
	            								 users.getJSONObject(i).getString("score"), 
	            								 users.getJSONObject(i).getString("address"));
	            			
	            			if(users.getJSONObject(i).getString("kind").equals("1"))
	            				FriendFragment.mData.get(0).add(f);
	            			else if(users.getJSONObject(i).getString("kind").equals("2"))
	            				FriendFragment.mData.get(1).add(f);
	            		}	   
     		
            		} catch (JSONException e) {
        				e.printStackTrace();
        			}
            		mAdapter = new FriendAdapter(getActivity(),
            				mData, mGroupArrays);  	
            		mListView.setAdapter(mAdapter);
            		
            		break;
            	default:
            		Toast.makeText(getActivity(), "服务器错误", Toast.LENGTH_SHORT).show();
            	}
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if(item!=null)
            	item.setActionView(null);
            super.onPostExecute(result);			
		}
	}

}