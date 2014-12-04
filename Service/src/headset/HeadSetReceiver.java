package headset;

import headset.HeadSetHelper.OnHeadSetListener;

import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

public class HeadSetReceiver extends BroadcastReceiver{

	Timer timer = null;
	OnHeadSetListener headSetListener = null;
	private static boolean isFiveClick = false;
	private static MyTimer myTimer = null;
	private static int clicknum = 0;
	//重写构造方法，将接口绑定。因为此类的初始化的特殊性。
	public HeadSetReceiver(){
		timer = new Timer(true);
		this.headSetListener = HeadSetHelper.getInstance().getOnHeadSetListener();
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		 String intentAction = intent.getAction() ;
	        if(Intent.ACTION_MEDIA_BUTTON.equals(intentAction)){
	        	//获得KeyEvent对象  
	        	KeyEvent keyEvent = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT); 
	        	if(headSetListener != null){
	        		try {
	        			if(keyEvent.getAction() == KeyEvent.ACTION_UP){
	        				if(isFiveClick){
	        					myTimer.cancel();
	        					isFiveClick = false;
	        					clicknum = 0;
	        					headSetListener.onFiveClick();
	        				}else{
	        					myTimer = new MyTimer();
	        					clicknum++;
	        					//第一次按下触发计时
	        					if(clicknum == 1){
	        						timer.schedule(myTimer,2000);
	        					}	        					        				
	        					//当第五次按下时才会触发单击五次的效果
	        					if(clicknum == 4){
	        						isFiveClick = true;
	        					}
	        				}
	        			}
					} catch (Exception e) {
			
					}
	        	}	
	        }
	        //终止广播(不让别的程序收到此广播，免受干扰)  
	        abortBroadcast();
	}
	/*
	 * 定时器，用于延迟1秒，内若无操作则为单击
	 */
	class MyTimer extends TimerTask{
			
			@Override
			public void run() {
				try {
					myHandle.sendEmptyMessage(0);
				} catch (Exception e) {
					
				}
			}
	};
	/*
	 * 此handle的目的主要是为了将接口在主线程中触发
	 * ，为了安全起见把接口放到主线程触发
	 */
	Handler myHandle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isFiveClick = false;
			clicknum = 0;
		}
		
	};
		
}
