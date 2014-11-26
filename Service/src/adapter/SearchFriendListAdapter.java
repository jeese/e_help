package adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import communicate.PushConfig;
import communicate.PushSender;

import client.ui.Findfriendresult;
import client.ui.R;
import client.ui.SearchfriendActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

//新建一个类，用来重写BaseExpandableListAdapter以及提供的数据源
public class SearchFriendListAdapter extends BaseAdapter {
    String verification;
    String addkind;
    View layout;
    String userName;
	
	private LayoutInflater mInflater;  
	private ArrayList<Map<String,Object>> listItem;
	Context con;
	private Map<String, Object> data = new HashMap<String, Object>();

	public SearchFriendListAdapter(Context context, ArrayList<Map<String,Object>> list) {  

         this.mInflater = LayoutInflater.from(context);  
         listItem = list;
         con = context;
    }  

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItem.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	ViewHolder holder; 
	 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
       holder = null;  
            if (convertView == null) {  
                  
                holder=new ViewHolder();    
                  
                //可以理解为从vlist获取view  之后把view返回给ListView   
                  
                convertView = mInflater.inflate(R.layout.search_result_item, null);
                holder.img = (ImageView)convertView.findViewById(R.id.add_image);
                holder.name = (TextView)convertView.findViewById(R.id.item_name);  
                holder.info = (TextView)convertView.findViewById(R.id.item_detail);  
                holder.add_Btn = (ImageButton)convertView.findViewById(R.id.addfriend);  
                convertView.setTag(holder);               
            }else {               
                holder = (ViewHolder)convertView.getTag();  
            }         
            
            holder.img.setTag(position);
            holder.name.setText((String)listItem.get(position).get("name"));  
            holder.info.setText((String)listItem.get(position).get("detail"));  
            holder.add_Btn.setTag(position);  
            //给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉   
            holder.add_Btn.setOnClickListener(new View.OnClickListener() {  
                  
                @Override  
                public void onClick(View v) {  
                	userName = (String)listItem.get(position).get("name"); //holder.name.getText().toString();
                	
                	showInfo(position);                   
                }  
            }); 
            
            holder.img.setOnClickListener(new View.OnClickListener() {  
                  
                @Override  
                public void onClick(View v) {  
                                   
                }  
            }); 
             
            //holder.viewBtn.setOnClickListener(MyListener(position));   
                      
            return convertView; 
	}
	
	
	//提取出来方便点   
	    public final class ViewHolder {  
	    	public ImageView img;
	        public TextView name;  
	        public TextView info;  
	        public ImageButton add_Btn;  
	    }  
	    

	    
	    public void showInfo(int position){  
	    	          
	    	LayoutInflater inflater = mInflater;
	    	layout = inflater.inflate(R.layout.addfriendcheck, null);

	    	
	    	AlertDialog a = new AlertDialog.Builder(con)
		   .setTitle("添加好友").setView(layout)
		   .setPositiveButton("确定", new DialogInterface.OnClickListener(){
		
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
			    	//获取验证信息
			    	verification = ((EditText)layout.findViewById(R.id.editText1))
			    							.getText().toString();
			    	
			    	if ( ((RadioButton)layout.findViewById(R.id.radio_relative)).isChecked() )
			    		addkind = "1";
			    	else 
			    		addkind = "2";
			    	
					AddFriend addFriend = new AddFriend();
					addFriend.execute(verification, addkind);
					//Toast.makeText(con, PushConfig.username, Toast.LENGTH_SHORT).show();
				}
			   
		   })
		   .setNegativeButton("取消", null).show();
	   } 
	
	  
	private class AddFriend extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			data.put("u_name", PushConfig.username);
			
			data.put("r_name", userName);
			data.put("info", arg0[0]);
			data.put("kind", arg0[1]);
			
			//Log.d("e", arg0[0]+arg0[1]);
			return PushSender.sendMessage("addrelatives", data);
			//return null;
		}
		
		@Override
	    protected void onPostExecute(String result) { 
        	if(result.equals("network error")){
        		Toast.makeText(con,"您还没有联网", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	if(result.equals("error")){
        		Toast.makeText(con,"连接服务器失败", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
            try {
            	switch (new JSONObject(result).getInt("state")) {
            	case 0:
            		Toast.makeText(con, "已经是您的好友", Toast.LENGTH_SHORT).show();
            		break;
            	case 1:
            		Toast.makeText(con, "验证信息发送成功", Toast.LENGTH_SHORT).show();
	            	break;
            	default:
            		Toast.makeText(con, "服务器错误", Toast.LENGTH_SHORT).show();
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
