package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import client.ui.R;

import java.util.List;
import java.util.Map;

public class CloseAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private List<Map<String,Object>> data;
	private Context context;	
	private ViewHolder holder;
	public CloseAdapter(Context context){
		this.inflater=LayoutInflater.from(context);
		this.context=context;
	}
	
	public void setData(List<Map<String,Object>> item)
	{
		data=item;
	}
	//���������ʾ��UIʱ��Ҫ��ȡ�ģ�������listview��ʾ������
	@Override
	public int getCount(){
		return data.size();
	}
		
	//����listview��λ�÷���view
	@Override
	public Object getItem(int position){
		return this.data.get(position);
		
	}
	//����listview��λ�õõ�����Դ���е�ID
	@Override
	public long getItemId(int position){
		return position;
	}
	
	//����Ҫ�ķ���������listview�������ʽ��
	@Override
	public View getView(int position,View convertView,ViewGroup parent){
		holder=null;
		if(convertView==null)
		{
			holder=new ViewHolder();
			
			convertView=inflater.inflate(R.layout.closeitem,null);
			holder.image = (ImageView) convertView.findViewById(R.id.img);
			holder.name = (TextView) convertView.findViewById(R.id.title);
			holder.evaluate = (RatingBar) convertView.findViewById(R.id.ratingBar1);
            //��������setTagʲô��˼??
            convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.position=position;
		holder.image.setBackgroundResource((Integer)data.get(position).get("img"));
        holder.name.setText((String) data.get(position).get("title"));
        holder.evaluate.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				data.get(holder.position).put("evalue",Double.toString(rating));
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
        TextView name;
        ImageView image;
        RatingBar evaluate;
        int position;
    }
	
}