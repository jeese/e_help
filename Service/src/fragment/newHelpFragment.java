//package fragment;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import radialdemo.RadialMenuItem;
//import radialdemo.RadialMenuWidget;
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.Toast;
//import client.ui.R;
//import client.ui.SendHelpMsgActivity;
//
//public class newHelpFragment extends Fragment{
//
//	private View view;
//	
//	private Button button1;
//	private Button button2;
//	private Button button3;
//
//	@SuppressWarnings("serial")
//	public newHelpFragment(){
//	}
//	
//	@Override
//    public View onCreateView(LayoutInflater inflater,
//    ViewGroup container, Bundle savedInstanceState) { 
//		
//		ViewGroup p = (ViewGroup) view.getParent(); 
//        if (p != null) { 
//            p.removeAllViewsInLayout(); 
//        } 	      
//		return view;
//   }
//	
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		view=View.inflate(getActivity(),R.layout.radial,null); 
//		button1=(Button) view.findViewById(R.id.button1);
//		button2=(Button) view.findViewById(R.id.button2);
//		button3=(Button) view.findViewById(R.id.button3);
//		
//		button1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent=new Intent();
//				intent.setClass(getActivity(),SendHelpMsgActivity.class);
//				startActivity(intent);
//			}
//		});
//		button2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent=new Intent();
//				intent.setClass(getActivity(),SendHelpMsgActivity.class);
//				startActivity(intent);
//				
//			}
//		});
//		button3.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent=new Intent();
//				intent.setClass(getActivity(),SendHelpMsgActivity.class);
//				startActivity(intent);
//				
//			}
//		});
//
//	}
//}
