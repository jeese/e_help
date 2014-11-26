package client.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class MyProgressBar extends ProgressBar {  
    private String text_progress;  
    private Paint mPaint;//ª≠±   
  
    public MyProgressBar(Context context) {  
        super(context);  
        initPaint();  
    }  
    public MyProgressBar(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        initPaint();  
    }  
    public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        initPaint();  
    }  
      
    public synchronized void setProgress(int progressMin,int progressMax) {  
        super.setProgress(progressMin*100/progressMax);  
        setTextProgress(progressMin,progressMax);  
    }  
    @Override  
    protected synchronized void onDraw(Canvas canvas) {  
        // TODO Auto-generated method stub  
        super.onDraw(canvas);  
        Rect rect=new Rect();  
        this.mPaint.getTextBounds(this.text_progress, 0, this.text_progress.length(), rect);  
        int x = (getWidth() / 2) - rect.centerX();  
        int y = (getHeight() / 2) - rect.centerY();  
        canvas.drawText(this.text_progress, x, y, this.mPaint);  
    }  
    /** 
     *  
     *description: ≥ı ºªØª≠±  
     */  
    private void initPaint(){  
        this.mPaint=new Paint();  
        this.mPaint.setAntiAlias(true);  
        this.mPaint.setColor(Color.WHITE);  
        this.mPaint.setTextSize(20);
    }  
    private void setTextProgress(int progressMin,int progressMax){     
        this.text_progress = String.valueOf(progressMin) + "/" + String.valueOf(progressMax);  
    }  
  
  
  
}  

