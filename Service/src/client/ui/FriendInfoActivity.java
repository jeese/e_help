package client.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import fragment.FriendFragment;

public class FriendInfoActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.friend_info);
		
		ImageView image=(ImageView)findViewById(R.id.friend_info_image);
		TextView userTV = (TextView)findViewById(R.id.friend_info_user);
		TextView nameTV = (TextView)findViewById(R.id.friend_info_name);
		TextView ageTV = (TextView)findViewById(R.id.friend_info_age);
		TextView sexTV = (TextView)findViewById(R.id.friend_info_sex);
		TextView phoneTV = (TextView)findViewById(R.id.friend_info_phone);
		TextView addressTV = (TextView)findViewById(R.id.friend_info_address);
		TextView diseaseTV = (TextView)findViewById(R.id.friend_info_disease);
		RatingBar creditRB = (RatingBar)findViewById(R.id.friend_info_ratingBar);
		TextView pointTV = (TextView)findViewById(R.id.friend_info_point);
		Button back = (Button)findViewById(R.id.friend_info_back);
		
		//获取内容
		Bundle bundle = this.getIntent().getExtras();
		
		int x = bundle.getInt("x");
		int y = bundle.getInt("y");
		//Toast.makeText(FriendInfoActivity.this, Integer.toString(x) + ",,," + Integer.toString(y), Toast.LENGTH_LONG).show();
		
		//int x = 1, y = 1;
		
		String user = FriendFragment.mData.get(x)
				.get(y).getName(); 
		String name = FriendFragment.mData.get(x)
				.get(y).getRealname();
		String age = FriendFragment.mData.get(x)
				.get(y).getage();
		String sex = FriendFragment.mData.get(x)
				.get(y).getSex();
		String phone = FriendFragment.mData.get(x)
				.get(y).getPhone();
		String address = FriendFragment.mData.get(x)
				.get(y).getAddress();
		String disease = FriendFragment.mData.get(x)
				.get(y).getIllness();
		String rating = FriendFragment.mData.get(x)
				.get(y).getCredit();
		String point = FriendFragment.mData.get(x)
				.get(y).getScore();
		
		image.setBackgroundDrawable(ControlActivity.base64ToDrawable(FriendFragment.mData.get(x).get(y).getImageId()));
		userTV.setText("昵称  "+user);
		nameTV.setText("真实姓名  "+name);
		ageTV.setText(age);
		sexTV.setText(sex);
		phoneTV.setText(phone);
		addressTV.setText(address);
		diseaseTV.setText(disease);
		creditRB.setRating(Float.parseFloat(rating));
		pointTV.setText(point);
		
		back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
