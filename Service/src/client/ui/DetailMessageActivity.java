package client.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.lbsapi.auth.LBSAuthManagerListener;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;

import adapter.AssistListViewAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import communicate.PushConfig;
import communicate.PushImport;
import communicate.PushSender;

public class DetailMessageActivity extends Activity {
	
	//求助视图
	public final class FirstItemView{
		public ImageView image;
		public TextView name;
		public TextView time;
		public TextView content;
		public Button concern;
		public Button assist;
		public Button btn_audio_play,btn_video_play;
	}
	
	private FirstItemView firstItemView=new FirstItemView(); ;
	
	private Map<String,Object> data=new HashMap<String, Object>();
	private Addassist addassist;
	private ListView listView;  
	private AssistListViewAdapter assistListViewAdapter;
	private List<Map<String, Object>> datalist=new ArrayList<Map<String, Object>>(); 
	private Bundle bundle;
	private GetAssist getassist;
	private static String canend="0",ishelper="0",eid;
	private static double longitude=0,latitude=0;
	private String isfollows="0",follows="0",helpers="0";
	private PushImport pushimport;
	
	//新加入的变量
	MediaPlayer audio_player = new MediaPlayer();
	MediaPlayer video_player = new MediaPlayer();
	private VideoView video;
	boolean isLongClick=false;
	private File			dir;
	private MediaRecorder	mMediaRecorder;
	private String          local_audio_file, local_video_file;
	private boolean         audioIsDownload = false;
	private boolean         videoIsDownload = false;
	private String          url_video = "0", url_audio = "0";
	
	private boolean mIsEngineInitSuccess = false;
	
	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
		public void engineInitSuccess() {
			mIsEngineInitSuccess = true;
		}

		public void engineInitStart() {
		}

		public void engineInitFail() {
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messagedetail);
		
		//初始化导航引擎
//		BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(),
//		        mNaviEngineInitListener, ACCESS_KEY, null);
        BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(),
                mNaviEngineInitListener, new LBSAuthManagerListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {
                    }
                });

		View firstView = View.inflate(DetailMessageActivity.this, R.layout.help_item, null);
		bundle=this.getIntent().getExtras();
		eid=bundle.getString("eid");
		url_video=bundle.getString("video");
		url_audio=bundle.getString("audio");
		
		
		
		firstItemView.image = (ImageView)firstView.findViewById(R.id.imageItem1);
		firstItemView.name = (TextView)firstView.findViewById(R.id.nameItem1);   
		firstItemView.time = (TextView)firstView.findViewById(R.id.timeItem1);   
		firstItemView.content= (TextView)firstView.findViewById(R.id.contentItem1);   
		firstItemView.concern = (Button)firstView.findViewById(R.id.concernBut1);
		firstItemView.assist = (Button)firstView.findViewById(R.id.assistBut1);
		
		firstItemView.image.setBackgroundDrawable(ControlActivity.base64ToDrawable(bundle.getString("image").toString()));
		firstItemView.name.setText(bundle.getString("needhelp").toString());
		firstItemView.time.setText(bundle.getString("time").toString());
		firstItemView.content.setText(bundle.getString("content").toString());
		firstItemView.concern.setText("关注(0)");
		firstItemView.assist.setText("帮助 (0)");
		
		
		//新加入的listener
		firstItemView.btn_audio_play = (Button) firstView.findViewById(R.id.audio_play11);
		firstItemView.btn_video_play = (Button) firstView.findViewById(R.id.video_play11);
		
		if(!url_video.equals("0"))
			firstItemView.btn_video_play.setText("视频(1)");
		
		if(!url_audio.equals("0"))
			firstItemView.btn_audio_play.setText("音频(1)");
		
		firstItemView.btn_audio_play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{

					Toast.makeText(DetailMessageActivity.this, "Download start", Toast.LENGTH_SHORT).show();
					new Thread(new Runnable() {

						@Override
						public void run() {
							if(!audioIsDownload){
								// TODO Auto-generated method stub
								
								//准备拼接新的文件名（保存在存储卡后的文件名）
								String newFilename = url_audio.substring(url_audio.lastIndexOf("/")+1);
								
								MyDownload download = new MyDownload(url_audio);
								System.out.println(url_audio);
								System.out.println(newFilename);
								//在本地新建文件:
								download.getAsBinary(newFilename);
								handler.sendEmptyMessage(INFO_DOWNLOAD_OK);
								local_audio_file = download.getFilePath();
								audioIsDownload = true;
							}
							
							//直接播放语音
							try{
								audio_player.reset();
								audio_player.setDataSource(local_audio_file);
							    audio_player.prepare();
							    audio_player.start();
								
							}catch(Exception e){   
					            e.printStackTrace();           
					        }  
							
						}
						
					}).start();       
		        }
		        catch(Exception e){   
		            e.printStackTrace();           
		        }  
			}
		});
		
		firstItemView.btn_video_play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(DetailMessageActivity.this, "Download start", Toast.LENGTH_SHORT).show();
				new Thread(new Runnable() {

					@Override
					public void run() {
						if(url_video.equals("0")){
							
						}else{
							if(!videoIsDownload){
								// TODO Auto-generated method stub
								
								//准备拼接新的文件名（保存在存储卡后的文件名）
								String newFilename = url_video.substring(url_video.lastIndexOf("/")+1);
								MyDownload download = new MyDownload(url_video);
								System.out.println(url_video);
								System.out.println(newFilename);
								//在本地新建文件:
								download.getAsBinary(newFilename);
								handler.sendEmptyMessage(INFO_DOWNLOAD_OK);
								local_video_file = download.getFilePath();
								videoIsDownload = true;
							}
							try{
								//直接播放
								Uri uri = Uri.parse(local_video_file);    
								//调用系统自带的播放器   
								Intent intent = new Intent(Intent.ACTION_VIEW);   
								Log.v("URI:::::::::", uri.toString());   
								intent.setDataAndType(uri, "video/3gp");   
								startActivity(intent);
							}catch(Exception e){   
					            e.printStackTrace();           
					        }
						}
						
						
					}
					
				}).start();
			}
		});
		
		
		firstItemView.concern.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isfollows.equals("0")){
					int numConcern = Integer.parseInt(getNumStr(firstItemView.concern.getText().toString()));
					numConcern ++;
					firstItemView.concern.setText("关注(" + Integer.toString(numConcern) + ")");
					isfollows="1";
					AddConcern addconcern=new AddConcern();
					addconcern.execute();
				}
				else
					Toast.makeText(DetailMessageActivity.this,"您已经关注", Toast.LENGTH_SHORT).show();
					
				
			}	
		});	
		
		firstItemView.assist.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ishelper.equals("0")){
					int numConcern = Integer.parseInt(getNumStr(firstItemView.assist.getText().toString()));
					numConcern ++;
					firstItemView.assist.setText("帮助(" + Integer.toString(numConcern) + ")");
					ishelper="1";
					addassist=new Addassist();
					addassist.execute();
				}
				else
					Toast.makeText(DetailMessageActivity.this,"您已经参与帮助", Toast.LENGTH_SHORT).show();
			}	
		});	
		
		listView = (ListView)findViewById(R.id.assist_list);
		assistListViewAdapter = new AssistListViewAdapter(this, datalist);
		
		
		listView.addHeaderView(firstView);
		listView.setAdapter(assistListViewAdapter);
		getassist=new GetAssist();
		getassist.execute();
		
		pushimport = new PushImport(this);
		pushimport.detailOnCreate(datalist, assistListViewAdapter);
		
	}
	
	@Override
	protected void onPause() {
		pushimport.detailOnPause();
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		pushimport.detailOnDestroy();
		super.onDestroy();
	}
	
	private String getNumStr(String string) {
		// TODO Auto-generated method stub
		
		StringBuffer str = new StringBuffer();
		boolean flag = false;
		for (int i=0; i<string.length(); i++)
		{
			if (string.charAt(i) == '('){
				flag = true;
				continue;
			}
			
			if (flag && string.charAt(i) != ')'){
				str.append(string.charAt(i));
				System.out.println(string.charAt(i));
			}
		}
		
		return str.toString();
	}
	public void onResume()
	{
		getassist=new GetAssist();
		getassist.execute();
		pushimport.detailOnResume(bundle);
		super.onResume();
	}
	
	private class GetAssist extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) { 
        	data.clear();
        	data.put("eventid",bundle.get("eid").toString()); 	
        	data.put("username",PushConfig.username);
        	datalist.clear();
            String result=PushSender.sendMessage("event",data);
            try {
				JSONArray support = new JSONObject(result).getJSONArray("support");
				for (int i=0; i<support.length(); i++) {
					Map<String, Object> map = new HashMap<String, Object> ();
					map.put("image",support.getJSONObject(i).getString("avatar"));
					map.put("name", support.getJSONObject(i).getString("username"));
					map.put("time", support.getJSONObject(i).getString("time"));
					map.put("content", support.getJSONObject(i).getString("content"));
					datalist.add(map);
				}
				longitude=new JSONObject(result).getJSONObject("event").getDouble("longitude");
				latitude=new JSONObject(result).getJSONObject("event").getDouble("latitude");
				follows=new JSONObject(result).getJSONObject("event").getString("follows");
				helpers=new JSONObject(result).getJSONObject("event").getString("helpers");
				ishelper=new JSONObject(result).getString("ishelper");
				isfollows=new JSONObject(result).getString("isfollow");
				canend=new JSONObject(result).getString("canend");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.i("test",e.toString());
				e.printStackTrace();
			}
            return "true";
        }
        @Override
        protected void onPreExecute() {   
        	
        }
        @Override
        protected void onPostExecute(String result) {   	
        	assistListViewAdapter.notifyDataSetChanged();
        	firstItemView.concern.setText("关注("+follows+")");
        	firstItemView.assist.setText("帮助 ("+helpers+")");
            super.onPostExecute(result);
        }
    }
	public static String GetEid(){
		return eid;
	}
	public static Double Getlongitude(){
		return longitude;
	}
	public static Double Getlatitude(){
		return latitude;
	}
	public static String GetIshelper(){
		return ishelper;
	}
	public static String GetCanEnd(){
		return canend;
	}
	
	private class Addassist extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) { 
        	data.clear();
        	data.put("username",PushConfig.username); 
            data.put("eventid",bundle.getString("eid"));
            return PushSender.sendMessage("addaid",data);
        }
        @Override
        protected void onPreExecute() {   
        	
        }
        @Override
        protected void onPostExecute(String result) {   	
        	if(result.equals("network error")){
        		Toast.makeText(DetailMessageActivity.this,"您还没有联网", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	if(result.equals("error")){
        		Toast.makeText(DetailMessageActivity.this,"连接服务器失败", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
            try {
            	switch (new JSONObject(result).getInt("state")) {
            	case 1:
            		Toast.makeText(DetailMessageActivity.this, "成功加入帮助", Toast.LENGTH_SHORT).show();
            		break;
            	case 2:
            		Toast.makeText(DetailMessageActivity.this, "已经加入", Toast.LENGTH_SHORT).show();
            		break;
            	case 3:
            		Toast.makeText(DetailMessageActivity.this, "事件已经结束", Toast.LENGTH_SHORT).show();
            		break;
            	default:
            		Toast.makeText(DetailMessageActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
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
		
	private class AddConcern extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) { 
        	data.clear();
        	data.put("username", PushConfig.username);
        	data.put("eid",bundle.getString("eid"));
            return PushSender.sendMessage("startfollow",data);
        }
        @Override
        protected void onPreExecute() {   
        	
        }
        @Override
        protected void onPostExecute(String result) {  
        	if(result.equals("network error")){
        		Toast.makeText(DetailMessageActivity.this,"您还没有联网", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	if(result.equals("error")){
        		Toast.makeText(DetailMessageActivity.this,"连接服务器失败", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
            try {
            	JSONObject json = new JSONObject(result);
            	switch (json.getInt("state")) {
            	
            	case 1:
            		Toast.makeText(DetailMessageActivity.this,"成功关注", Toast.LENGTH_SHORT).show();
            		break;
            	case 2:
            		Toast.makeText(DetailMessageActivity.this,"用户不存在", Toast.LENGTH_SHORT).show();
            		
            	case 3:
            		Toast.makeText(DetailMessageActivity.this,"已经关注", Toast.LENGTH_SHORT).show();
            	default:
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
	
    
	private static final int INFO_DOWNLOAD_OK = 1;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what){
				case INFO_DOWNLOAD_OK:
					Log.v("file", "download ok");
					Toast.makeText(DetailMessageActivity.this, "Download OK", Toast.LENGTH_SHORT).show();
					break;
				default:
			}
		}
	};
	
}
