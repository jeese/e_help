package adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import client.ui.ControlActivity;
import client.ui.DetailMessageActivity;
import client.ui.R;



public class MessageAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private List<Map<String,Object>> data;
	private Context context;	
	private ViewHolder holder;
	public MessageAdapter(Context context){
		this.inflater=LayoutInflater.from(context);
		this.context=context;
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
		return this.data.get(position);
		
	}
	//根据listview的位置得到数据源集中的ID
	@Override
	public long getItemId(int position){
		return position;
	}
	
	//最重要的方法，决定listview界面的样式的
	@Override
	public View getView(int position,View convertView,ViewGroup parent){
		holder=null;
		if(convertView==null)
		{
			holder=new ViewHolder();
			
			convertView=inflater.inflate(R.layout.pull_to_refreshlist_item,null);
			holder.image = (ImageView) convertView.findViewById(R.id.imageItem1);
			holder.name = (TextView) convertView.findViewById(R.id.nameItem1);
			holder.time = (TextView) convertView.findViewById(R.id.timeItem1);
			holder.content = (TextView) convertView.findViewById(R.id.contentItem1);
			holder.assist = (RelativeLayout)convertView.findViewById(R.id.assistBut1);
            //不懂这里setTag什么意思??
            convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.position=position;
		holder.image.setBackgroundDrawable(ControlActivity.base64ToDrawable(data.get(position).get("image").toString()));
        holder.name.setText((String) data.get(position).get("name"));
        holder.time.setText((String) data.get(position).get("time"));
        holder.content.setText((String) data.get(position).get("content"));
        
        
        holder.assist.setOnClickListener(new View.OnClickListener() {
        	@Override
            public void onClick(View v){
        		ViewHolder holder=getViewHolder(v);
        		Intent intent = new Intent();
        		Bundle bundle = new Bundle();
        		bundle.putString("needhelp",data.get(holder.position).get("name").toString());
        		bundle.putString("time",data.get(holder.position).get("time").toString());
        		bundle.putString("content",data.get(holder.position).get("content").toString());
        		bundle.putString("eid",data.get(holder.position).get("eid").toString());
        		bundle.putString("image",data.get(holder.position).get("image").toString());
        		bundle.putString("video",data.get(holder.position).get("video").toString());
        		bundle.putString("audio",data.get(holder.position).get("audio").toString());
        		
        		intent.putExtras(bundle);
        		intent.setClass(context, DetailMessageActivity.class);
        		context.startActivity(intent);
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
        TextView name,time,content;
        ImageView image;
        RelativeLayout assist;
        int position;
    }
	
}
