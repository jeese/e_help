package client.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

public class Video extends Activity {

	private File myRecAudioFile;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private Button btn_video_start;
	private Button btn_video_stop;
	private File dir;
	private MediaRecorder recorder;
	private String SD_CARD_TEMP_DIR = getSDPath() + File.separator + "myvideo" + File.separator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.video);
		mSurfaceView = (SurfaceView) findViewById(R.id.videoView);
		Log.v("holder", String.valueOf(mSurfaceView == null));
		mSurfaceHolder = mSurfaceView.getHolder();
		Log.v("holder", String.valueOf(mSurfaceHolder == null));
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		btn_video_start = (Button) findViewById(R.id.video_start);
		btn_video_stop = (Button) findViewById(R.id.video_stop);
		dir = new File(SD_CARD_TEMP_DIR);
		if (!dir.exists()) {
			dir.mkdir();
		}
		recorder = new MediaRecorder();
		btn_video_stop.setVisibility(View.GONE);
		btn_video_start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				recorder();
				btn_video_start.setVisibility(View.GONE);
				btn_video_stop.setVisibility(View.VISIBLE);
			}
		});

		btn_video_stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
				recorder.stop();
				recorder.reset();
				recorder.release();
				recorder = null;
				
				Intent data=new Intent();  
	            data.putExtra("video_file", myRecAudioFile.getAbsolutePath());
	            //�����������Լ����ã���������audio=20  
	            setResult(21, data);  
	            //�رյ����Activity  
	            finish();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	public void recorder() {
		try {
			myRecAudioFile = File.createTempFile("video", ".3gp", dir);// ������ʱ�ļ�
			recorder.setPreviewDisplay(mSurfaceHolder.getSurface());// Ԥ��
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // ¼��ԴΪ��˷�
			recorder.setMaxDuration(10000);// �������
			recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); // ��������ɼ���Ƶ
			recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
			int width = mSurfaceView.getMeasuredWidth();
			int height = mSurfaceView.getMeasuredHeight();
			recorder.setVideoSize(width, height);
			recorder.setOrientationHint(0);
			recorder.setVideoFrameRate(3); // ÿ��3֡
			recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT); // ������Ƶ���뷽ʽ
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile(myRecAudioFile.getAbsolutePath());// ����·��
			recorder.prepare();
			recorder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// ��ȡ��Ŀ¼
		} else {
			return null;
		}
		System.out.println("sd·���ǣ�" + sdDir.toString());
		return sdDir.toString();

	}

}