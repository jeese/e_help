package fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.MessageAdapter;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import client.ui.R;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import communicate.PushConfig;
import communicate.PushSender;

public class MessageFragment extends Fragment{
	private PullToRefreshListView mListView;
	private MessageAdapter myadapter;
	private View view;
	private final Handler handler = new Handler();
	private GetMessager getmessager;
	private static List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
	private Map<String, Object> data = new HashMap<String, Object>();
  
	
	public MessageFragment(){
		
	}
	@Override
    public View onCreateView(LayoutInflater inflater,
    ViewGroup container, Bundle savedInstanceState) { 
		
		ViewGroup p = (ViewGroup) view.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
        } 	      
		return view;
   }
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		 
		super.onCreate(savedInstanceState);
		view=View.inflate(getActivity(),R.layout.pull_to_refreshlist,null); 
	    mListView=(PullToRefreshListView)view.findViewById(R.id.pull_to_refresh_list);   
	    myadapter=new MessageAdapter(getActivity());    
	    myadapter.setData(datalist);
	    mListView.setAdapter(myadapter);
		// 设置PullToRefresh	    
		mListView.setMode(Mode.BOTH);
		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>(){
		 
		    // 下拉Pulling Down
		    @Override
		    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		    	// 下拉的时候数据重置
		    	getmessager=new GetMessager();
		    	getmessager.execute();
		    }
		            
		    // 上拉Pulling Up
		    @Override
		    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		    	handler.postDelayed(new Runnable() {	
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mListView.onRefreshComplete();
					}
				}, 1000);
		    }
		
		});	
	}
	@Override
	public void onResume()
	{
		getmessager=new GetMessager();
    	getmessager.execute();		
		super.onResume();
	}
	@Override
	public void onDestroy()
	{
		if (getmessager != null && getmessager.getStatus() != AsyncTask.Status.FINISHED)
			getmessager.cancel(true);

		super.onPause();
	}
	@Override
	public void onPause()
	{
		if (getmessager != null && getmessager.getStatus() != AsyncTask.Status.FINISHED)
			getmessager.cancel(true);
		super.onPause();
	}
	
	private class GetMessager extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) { 
        	datalist.clear();
        	data.put("username", PushConfig.username);
            return PushSender.sendMessage("getAround",data);
        }
        @Override
        protected void onPreExecute() {   
        	
        }
        @Override
        protected void onPostExecute(String result) {  
        	Log.i("test",result);
        	if(result.equals("network error")){
        		Toast.makeText(getActivity(),"您还没有联网", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	if(result.equals("error")){
        		Toast.makeText(getActivity(),"连接服务器失败", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
            try {
            	JSONObject json = new JSONObject(result);
            	Log.i("test",json.toString());
            	switch (json.getInt("state")) {
            	
            	case 1:
            		JSONArray array = json.getJSONArray("aids");
            		Log.i("test",array.toString());
            		for (int i = 0; i < array.length(); i++) {
            			Map<String, Object> map = new HashMap<String, Object>();    
            	        map.put("image", array.getJSONObject(i).getString("avatar"));                 
            	        map.put("name", array.getJSONObject(i).getString("name"));              
            	        map.put("time",array.getJSONObject(i).getString("starttime"));             
            	        map.put("content", array.getJSONObject(i).getString("content"));
            	        map.put("eid", array.getJSONObject(i).getString("id"));
            	        map.put("video", array.getJSONObject(i).getString("video"));
            	        map.put("audio", array.getJSONObject(i).getString("audio"));
            	        datalist.add(map);    
            		}
            		myadapter.notifyDataSetChanged();
            		Toast.makeText(getActivity(), "消息列表刷新成功", Toast.LENGTH_SHORT).show();
            	default:
            	}
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            mListView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }
	
	public void addItem(String name, String time, String content, String eventid, String video, String audio, String image) {
		Log.i("PushReceiver", "eventid=" + eventid);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("image", image);
		map.put("name", name);
		map.put("time", time);
		map.put("content", content);
		map.put("eid", eventid);
		map.put("video", video);
        map.put("audio", audio);
		datalist.add(0, map);
		myadapter.notifyDataSetChanged();
	}
	
	public static Map<String, Object> getEventById(String eventid) {
		Map<String, Object> map;
		for (int i = 0; i < datalist.size(); i++) {
			map = datalist.get(i);
			if (map.get("eid").equals(eventid))
				return map;
		}
		return null;
	}


}