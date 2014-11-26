package client.ui;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

public class Audio extends Activity {
	private Button btn_audio_start, btn_audio_stop;
	private MediaRecorder	mMediaRecorder;
	private String			strTempFile	= "audio_";
	private String SD_CARD_TEMP_DIR = getSDPath() + File.separator + "myaudio" + File.separator;
	/* 录制的音频文件 */
	private File			mRecAudioFile;
	private File			mRecAudioPath;
	private File			dir;
	private Chronometer timer = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio);
		//修改
		//button id
		btn_audio_start = (Button) findViewById(R.id.audio_start);
		btn_audio_stop = (Button) findViewById(R.id.audio_stop);
		dir = new File(SD_CARD_TEMP_DIR);
		if (!dir.exists()) {
			dir.mkdir();
		}
		/*找到计时器*/
		timer = (Chronometer) findViewById(R.id.chronometer);
		timer.setFormat("计时：%s"); 
			
		btn_audio_start.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				recorder();		
			}
		} );
		
		btn_audio_stop.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				try{
				if(mRecAudioFile != null){
					/* 停止录音 */
					mMediaRecorder.stop();
					/* 释放MediaRecorder */
					mMediaRecorder.release();
					mMediaRecorder = null;
					btn_audio_start.setText("语音录制");
					/*停止计时*/
					timer.stop();
				}
				
				Intent data=new Intent();  
	            data.putExtra("audio_file", mRecAudioFile.getAbsolutePath());
	            //请求代码可以自己设置，这里设置audio=20  
	            setResult(20, data);  
	            //关闭掉这个Activity  
	            finish();  
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
	}

	protected void recorder(){
		try{
			/* 检测是否存在SD卡 */
			if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			{
				/* 得到SD卡得路径 */
				mRecAudioPath = Environment.getExternalStorageDirectory();
				//Toast.makeText(getApplicationContext(), "SD卡存在", Toast.LENGTH_LONG).show();
				
			}
			else
			{
				Toast.makeText(getApplicationContext(), "没有SD卡", Toast.LENGTH_LONG).show();
			}
			
			Toast.makeText(getApplicationContext(), "SDcard:" + mRecAudioPath, Toast.LENGTH_SHORT).show();
			/*createTempFile(String prefix, String suffix, File directory)；
			在指定目录中创建一个新的空文件，使用给定的前缀和后缀字符串生成其名称。*/
			mRecAudioFile = File.createTempFile(strTempFile, ".amr", dir);
			//Toast.makeText(getApplicationContext(), "开始录音......", Toast.LENGTH_SHORT).show();
			/*实例化*/
			mMediaRecorder = new MediaRecorder();
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
			mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			mMediaRecorder.setOutputFile(mRecAudioFile.getAbsolutePath());
			mMediaRecorder.prepare();
			mMediaRecorder.start();
			btn_audio_start.setText("正在录音");
			/*计时器先复位，再启动*/
			timer.setBase(SystemClock.elapsedRealtime());
			timer.start();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		} else {
			return null;
		}
		//System.out.println("sd路径是：" + sdDir.toString());
		return sdDir.toString();

	}

}
