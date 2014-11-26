package adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import client.ui.ControlActivity;
import client.ui.R;

public class AssistHistoryAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Map<String,Object>> data;
	private Context context;	
	private ViewHolder holder;
	public AssistHistoryAdapter(Context ctx, List<Map<String, Object>> lst){
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
			convertView=inflater.inflate(R.layout.assisthistoryitem,null);
			holder.image = (ImageView) convertView.findViewById(R.id.imageassisthistory);
			holder.name = (TextView) convertView.findViewById(R.id.nameassisthistory);
			holder.time = (TextView) convertView.findViewById(R.id.timeassisthistory);
			holder.content = (TextView) convertView.findViewById(R.id.contentassisthistory);
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
        int position;
    }
}
