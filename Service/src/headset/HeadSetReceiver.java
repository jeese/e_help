package headset;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import headset.HeadSetHelper.OnHeadSetListener;

import java.util.Timer;
import java.util.TimerTask;

public class HeadSetReceiver extends BroadcastReceiver{

	Timer timer = null;
	OnHeadSetListener headSetListener = null;
	private static boolean isFiveClick = false;
	private static MyTimer myTimer = null;
	private static int clicknum = 0;
	//��д���췽�������ӿڰ󶨡���Ϊ����ĳ�ʼ���������ԡ�
	public HeadSetReceiver(){
		timer = new Timer(true);
		this.headSetListener = HeadSetHelper.getInstance().getOnHeadSetListener();
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		 String intentAction = intent.getAction() ;
	        if(Intent.ACTION_MEDIA_BUTTON.equals(intentAction)){
	        	//���KeyEvent����  
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
	        					//��һ�ΰ��´�����ʱ
	        					if(clicknum == 1){
	        						timer.schedule(myTimer,2000);
	        					}	        					        				
	        					//������ΰ���ʱ�Żᴥ��������ε�Ч��
	        					if(clicknum == 4){
	        						isFiveClick = true;
	        					}
	        				}
	        			}
					} catch (Exception e) {
			
					}
	        	}	
	        }
	        //��ֹ�㲥(���ñ�ĳ����յ��˹㲥�����ܸ���)  
	        abortBroadcast();
	}
	/*
	 * ��ʱ���������ӳ�1�룬�����޲�����Ϊ����
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
	 * ��handle��Ŀ����Ҫ��Ϊ�˽��ӿ������߳��д���
	 * ��Ϊ�˰�ȫ����ѽӿڷŵ����̴߳���
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
