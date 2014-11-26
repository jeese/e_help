package adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import client.ui.R;
import client.ui.ValidationActivity;

import communicate.PushConfig;
import communicate.PushSender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ValidationAdapter extends BaseAdapter {
	
	private String userName; //请求者的名字
	private String kind;  //添加的类型
	private LayoutInflater inflater;
	private List<Map<String,Object>> data;
	private Context context;
	private ViewHolder holder;
	public ValidationAdapter(Context ctx, List<Map<String, Object>> lst){
		context = ctx;
		inflater = LayoutInflater.from(context);
		data = lst;
	}
	
	public void setData(List<Map<String,Object>> item)
	{
		data=item;
	}
	//程序加载显示到UI时先要读取的，决定了listview显示多少行
	@Override
	public int getCount(){
		return data.size();
	}
		
	//根绝listview的位置返回view
	@Override
	public Object getItem(int position){
		//return this.data.get(position);
		return null;
		
	}
	//根据listview的位置得到数据源集中的ID
	@Override
	public long getItemId(int position){
		return position;
	}
	
	//最重要的方法，决定listview界面的样式的
	@Override
	public View getView(final int position,View convertView,ViewGroup parent){
		
		holder=null;
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.validation_item,null);
			holder.image = (ImageView) convertView.findViewById(R.id.val_imageItem);
			holder.name = (TextView) convertView.findViewById(R.id.val_name);
			holder.kind	= (TextView) convertView.findViewById(R.id.kind);
			holder.request = (TextView) convertView.findViewById(R.id.val_request);
			holder.agree = (Button) convertView.findViewById(R.id.val_agree);
			holder.reject = (Button) convertView.findViewById(R.id.val_reject);
            convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.position=position;
		holder.image.setBackgroundResource((Integer) data.get(position).get("image"));
        holder.name.setText((String) data.get(position).get("name"));
        holder.request.setText((String) data.get(position).get("info")); //= (TextView) convertView.findViewById(R.id.val_request);;
        if(data.get(position).get("kind").toString().equals("1")){
        	holder.kind.setText("亲友");
        }
        else{
        	holder.kind.setText("好友");
        }
        
       //添加事件agree事件监听 
        holder.agree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userName = (String) data.get(position).get("name");
				kind = (String)data.get(position).get("kind");
				Agree agree = new Agree();
				agree.execute("1"); //agreeOrNot
			}
		});
        
        holder.reject.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userName = (String) data.get(position).get("name");
				kind = (String)data.get(position).get("kind");
				Agree agree = new Agree();
				agree.execute("0");
			}
		});
        
        return convertView;
	}
	public ViewHolder getViewHolder(View v){
		  if (v.getTag() == null){
		    return getViewHolder((View) v.getParent());
		  }
		  return (ViewHolder) v.getTag();
	}
	
	static class ViewHolder {
        Button agree, reject;
        TextView name, kind, request;
        ImageView image;
        int position;
    }
	
	private class Agree extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) { 
        	Map<String,Object> data = new HashMap<String,Object>();
        	data.put("u_name", userName);
        	data.put("c_name", PushConfig.username);
        	data.put("agree", params[0]);
        	data.put("kind", kind);
        	return PushSender.sendMessage("agreerelatives",data);
        }
        
        @Override
	    protected void onPostExecute(String result) { 
        	if(result.equals("network error")){
        		Toast.makeText(context, "您还没有联网", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	if(result.equals("error")){
        		Toast.makeText(context, "连接服务器失败", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	try {
            	switch (new JSONObject(result).getInt("state")) {
            	case 1:
            		Toast.makeText(context, "处理消息发送成功", Toast.LENGTH_SHORT).show();
	            	break;
            	default:
            		Toast.makeText(context, "服务器错误", Toast.LENGTH_SHORT).show();
            	}
            } catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	super.onPostExecute(result);
        	
        	Intent in = new Intent(context, ValidationActivity.class);
        	Activity act = (Activity)context;
        	act.startActivity(in);
        	act.finish();
        }
    }
}