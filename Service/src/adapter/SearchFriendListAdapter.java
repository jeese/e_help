package adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import client.ui.R;
import communicate.PushConfig;
import communicate.PushSender;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//�½�һ���࣬������дBaseExpandableListAdapter�Լ��ṩ������Դ
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
                  
                //�������Ϊ��vlist��ȡview  ֮���view���ظ�ListView   
                  
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
            //��Button��ӵ����¼�  ���Button֮��ListView��ʧȥ����  ��Ҫ��ֱ�Ӱ�Button�Ľ���ȥ��   
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
	
	
	//��ȡ���������   
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
		   .setTitle("��Ӻ���").setView(layout)
		   .setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
		
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
			    	//��ȡ��֤��Ϣ
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
		   .setNegativeButton("ȡ��", null).show();
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
        		Toast.makeText(con,"����û������", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
        	if(result.equals("error")){
        		Toast.makeText(con,"���ӷ�����ʧ��", Toast.LENGTH_SHORT).show();
            	//pro.setVisibility(View.INVISIBLE);
        	}
            try {
            	switch (new JSONObject(result).getInt("state")) {
            	case 0:
            		Toast.makeText(con, "�Ѿ������ĺ���", Toast.LENGTH_SHORT).show();
            		break;
            	case 1:
            		Toast.makeText(con, "��֤��Ϣ���ͳɹ�", Toast.LENGTH_SHORT).show();
	            	break;
            	default:
            		Toast.makeText(con, "����������", Toast.LENGTH_SHORT).show();
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
