package client.ui;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class Video_play extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.video_play);

		
		VideoView videoView = (VideoView) findViewById(R.id.videoView);
	
		MediaController mediaController = new MediaController(this);
	
		mediaController.setAnchorView(videoView);
	
		videoView.setMediaController(mediaController);
		
		videoView.setVideoURI(Uri.parse("/mnt/sdcard/myvideo/video-1617243045.3gp"));
		
		videoView.start();
	}
}