package fragment;

import java.util.HashMap;
import java.util.Map;

import lbs.mMapPage;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import client.ui.CloseActivity;
import client.ui.DetailMessageActivity;
import client.ui.R;
import client.ui.SendAssistMsgActivity;

import communicate.PushConfig;
import communicate.PushSender;

public class BottomButtonFragment extends Fragment {

	private View messageLayout;
	private TextView navigation; // 导航按钮
	private TextView assist; // 援助按钮
	private TextView conclude; // 结束按钮
	private Map<String, Object> data = new HashMap<String, Object>();
	private Close close;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		messageLayout = inflater.inflate(R.layout.messagedetail_bottom,
				container, false);

		navigation = (TextView) messageLayout
				.findViewById(R.id.button_navigate);
		assist = (TextView) messageLayout.findViewById(R.id.button_assist);
		conclude = (TextView) messageLayout.findViewById(R.id.button_conclude);

		// 监听事件
		navigation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putDouble("longitude",
						DetailMessageActivity.Getlongitude());
				bundle.putDouble("latitude",
						DetailMessageActivity.Getlatitude());
				intent.putExtras(bundle);
				intent.setClass(getActivity(), mMapPage.class);
				startActivity(intent);
			}
		});

		assist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (DetailMessageActivity.GetIshelper().equals("1"))
					startActivity(new Intent(getActivity(),
							SendAssistMsgActivity.class));
				else
					Toast.makeText(getActivity(), "您没有参与帮助", Toast.LENGTH_SHORT)
							.show();
			}
		});

		conclude.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (DetailMessageActivity.GetCanEnd().equals("1")) {
					close = new Close();
					close.execute();
				} else
					Toast.makeText(getActivity(), "您没有权限", Toast.LENGTH_SHORT)
							.show();
			}
		});

		return messageLayout;
	}

	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private class Close extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			data.put("username", PushConfig.username);
			data.put("eventid", DetailMessageActivity.GetEid());
			return PushSender.sendMessage("finish", data);
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onPostExecute(String result) {
			if (result.equals("network error")) {
				Toast.makeText(getActivity(), "您还没有联网", Toast.LENGTH_SHORT)
						.show();
				// pro.setVisibility(View.INVISIBLE);
			}
			if (result.equals("error")) {
				Toast.makeText(getActivity(), "连接服务器失败", Toast.LENGTH_SHORT)
						.show();
				// pro.setVisibility(View.INVISIBLE);
			}
			Intent intent = new Intent();
			intent.putExtra("result", result);
			intent.setClass(getActivity(), CloseActivity.class);
			startActivity(intent);
			getActivity().finish();
			super.onPostExecute(result);
		}
	}

}
