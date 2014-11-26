package client.ui;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.SearchFriendListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import base.FindedUserList;



public class Findfriendresult extends Activity {
	
	private ListView list;
	Button add_btn;
	private ArrayList<Map<String,Object>> listItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_results);
		
		list = (ListView)findViewById(R.id.search_results);
		add_btn = (Button)findViewById(R.id.addfriend);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		
		FindedUserList userList = (FindedUserList) bundle.getSerializable("users");
		SearchFriendListAdapter newListAdapter = new SearchFriendListAdapter(this, userList.userList);
		list.setAdapter(newListAdapter);
		
	}
	
}
