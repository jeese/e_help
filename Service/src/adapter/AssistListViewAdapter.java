package adapter;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle.Control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import client.ui.ControlActivity;
import client.ui.R;

public class AssistListViewAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, Object>> listItems;
	private LayoutInflater listContainer;
	
	//item类别
	private final int TYPE_COUNT = 2;
	private final int FIRST_TYPE = 0;
	private final int SECOND_TYPE = 1;
	

	//援助视图
	public final class ListItemView{
		public ImageView image;
		public TextView name;
		public TextView time;
		public TextView content;
	}
	
	public AssistListViewAdapter(Context ctx, List<Map<String, Object>> lst){
		context = ctx;
		listContainer = LayoutInflater.from(context);
		listItems = lst;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int getViewTypeCount(){
		return TYPE_COUNT;
	}
	
	public int getItemViewType(int position){
		if(position == 0)
		{
			return FIRST_TYPE;
		}
		else{
			return SECOND_TYPE;
		}
	}

	//FirstItemView firstItemView;
	ListItemView listItemView;
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		listItemView = null;  //援助信息视图
		
		if (convertView == null){
			listItemView = new ListItemView();
			convertView = listContainer.inflate(R.layout.assist_item, null);
			
			listItemView.image = (ImageView)convertView.findViewById(R.id.imageItem);
			listItemView.name = (TextView)convertView.findViewById(R.id.nameItem);   
            listItemView.time = (TextView)convertView.findViewById(R.id.timeItem);   
            listItemView.content= (TextView)convertView.findViewById(R.id.contentItem);   
             
            
            convertView.setTag(listItemView);
		}
		
		else{
			listItemView = (ListItemView)convertView.getTag();
		}
		
		listItemView.image.setBackgroundDrawable(ControlActivity.base64ToDrawable(listItems.get(position).get("image").toString()));   
        listItemView.name.setText((String) listItems.get(position).get("name"));   
        listItemView.time.setText((String) listItems.get(position).get("time"));   
        listItemView.content.setText((String) listItems.get(position).get("content"));   	
	
		convertView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "hello"+String.valueOf(position), Toast.LENGTH_SHORT).show();
			}
			
		});

		return convertView;
	}


}
