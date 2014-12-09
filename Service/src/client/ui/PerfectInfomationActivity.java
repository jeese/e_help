package client.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.*;
import android.widget.RadioGroup.OnCheckedChangeListener;
import communicate.PushConfig;
import communicate.PushSender;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class PerfectInfomationActivity extends Activity implements OnClickListener{
	
	private ImageView img_view; //ͷ����ͼ
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// ����
	private static final int PHOTO_REQUEST_GALLERY = 2;// �������ѡ��
	private static final int PHOTO_REQUEST_CUT = 3;// ���
	// ����һ���Ե�ǰʱ��Ϊ���Ƶ��ļ�
	private File tempFile = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());
	private EditText name,phone,idcard,sickness,age,email;
	private Spinner career,usertype;
	private Button cancel,register;
	private RadioGroup gender;
	private Map<String,Object> data=new HashMap<String,Object>();
	private Perfect perfect;
	private String sex="��";
	private Bitmap photo=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.perfect_info);
		init();
		

	}
	
	//��ʼ���ؼ�
	private void init() {
		img_view = (ImageView) findViewById(R.id.portrait);
//		userId=(EditText)findViewById(R.id.user);
		name=(EditText)findViewById(R.id.name);
		phone=(EditText)findViewById(R.id.idNumber);
		career=(Spinner)findViewById(R.id.spinner1);
		idcard=(EditText)findViewById(R.id.teleNumber);
		sickness=(EditText)findViewById(R.id.sickness);
		email = (EditText)findViewById(R.id.email);
		age=(EditText)findViewById(R.id.age);
//		password=(EditText)findViewById(R.id.password);
//		password1=(EditText)findViewById(R.id.password2);
		cancel=(Button)findViewById(R.id.cancel1);
		register=(Button)findViewById(R.id.perfect_info);
		gender=(RadioGroup)findViewById(R.id.gender);
		usertype=(Spinner)findViewById(R.id.usertype);
		
		String[] mitems={"ҽ�������Ա","���졢���������������Ա","����"};
		ArrayAdapter<String> _Adapters=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mitems);
		career.setAdapter(_Adapters); 
		
		String[] mitem={"��ͨ�û�","־Ը��","С������","��ȫ����","ҽ�ƻ���","��������"};
		ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mitem);
		usertype.setAdapter(_Adapter); 
		
		//ΪImageView��Ӽ����¼�
		img_view.setOnClickListener(this);
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				perfect=new Perfect();
				perfect.execute();
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				data.put("username",PushConfig.username);
				data.put("phone",phone.getText().toString());
				data.put("cardid", idcard.getText().toString());
				data.put("realname",name.getText().toString());
				data.put("email",email.getText().toString());
				data.put("kind","1");
				finish();
			}
		});
		
		
		gender.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton r=(RadioButton)findViewById(checkedId);
				sex=r.getText().toString();
			}
		});
		
	}
	
	//����¼�
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
	
		case R.id.portrait:			
			showDialog();
			break;
		}

	}

	//��ʾ�Ի��򷽷�
	private void showDialog() {
		
		new AlertDialog.Builder(this)
		.setTitle("ͷ������")
		.setPositiveButton("����", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				// ����ϵͳ�����չ���
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// ָ������������պ���Ƭ�Ĵ���·��
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
				startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
			}
		})
		
		.setNegativeButton("���", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
			}
		}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub

		switch (requestCode) {
		case PHOTO_REQUEST_TAKEPHOTO://��ѡ������ʱ����
			startPhotoZoom(Uri.fromFile(tempFile), 150);
			break;
	
		case PHOTO_REQUEST_GALLERY://��ѡ��ӱ��ػ�ȡͼƬʱ
			//���ǿ��жϣ������Ǿ��ò����������¼��õ�ʱ��㲻�ᱨ�쳣����ͬ
			if (data != null)
			startPhotoZoom(data.getData(), 150);
			break;
	
		case PHOTO_REQUEST_CUT://���صĽ��
			if (data != null) 
			setPicToView(data);
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	private void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// cropΪtrue�������ڿ�����intent��������ʾ��view���Լ���
		intent.putExtra("crop", "true");
	
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
	
		// outputX,outputY �Ǽ���ͼƬ�Ŀ��
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);
	
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	//�����м��ú��ͼƬ��ʾ��UI������
	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			photo = bundle.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			img_view.setBackgroundDrawable(drawable);
		}
	}

	// ʹ��ϵͳ��ǰ���ڼ��Ե�����Ϊ��Ƭ������
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	
	
	private class Perfect extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
        	data.put("username",PushConfig.username);
            data.put("phone",phone.getText().toString());
            data.put("cardid", idcard.getText().toString());
            data.put("realname",name.getText().toString());
			data.put("email",email.getText().toString());
            if(career.getSelectedItem().toString().equals("ҽ�������Ա"))
            	data.put("vocation","1");
            else if(career.getSelectedItem().toString().equals("���졢���������������Ա"))
            	data.put("vocation","2");
            else
            	data.put("vocation","3");
            
            if(usertype.getSelectedItem().toString().equals("��ͨ�û�"))
            	data.put("kind","1");
            else if(career.getSelectedItem().toString().equals("־Ը��"))
            	data.put("kind","2");
            else if(career.getSelectedItem().toString().equals("С������"))
            	data.put("kind","3");
            else if(career.getSelectedItem().toString().equals("��ȫ����"))
            	data.put("kind","4");
            else if(career.getSelectedItem().toString().equals("ҽ�ƻ���"))
            	data.put("kind","5");
            else
            	data.put("kind","6");    
//            
            
            if(sex.equals("��"))
            		data.put("sex","1");
            else
            	data.put("sex","2");
            data.put("age",age.getText().toString());
            data.put("illness",sickness.getText().toString());
            if(photo!=null)
            	data.put("file",Bitmap.createScaledBitmap(photo,70,70, true));
            return PushSender.sendMessage("perfect",data);
        }
        @Override
        protected void onPreExecute() {   
        	
        }
        @Override
        protected void onPostExecute(String result) {   	
        	if(result.equals("network error")){
        		Toast.makeText(PerfectInfomationActivity.this,"����û������", Toast.LENGTH_SHORT).show();
        	}
        	if(result.equals("error")){
        		Toast.makeText(PerfectInfomationActivity.this,"���ӷ�����ʧ��", Toast.LENGTH_SHORT).show();
        	}
            super.onPostExecute(result);
            try {
            	switch (new JSONObject(result).getInt("state")) {
            	case 1:
            		Toast.makeText(PerfectInfomationActivity.this, "���֤��ʽ����", Toast.LENGTH_SHORT).show();
            		break;
            	case 2:
            		Toast.makeText(PerfectInfomationActivity.this, "���֤�Ѵ���", Toast.LENGTH_SHORT).show();
            		break;
            	case 3:
            		Toast.makeText(PerfectInfomationActivity.this, "�����ַ��ʽ����", Toast.LENGTH_SHORT).show();
            		break;
            	case 4:
            		Toast.makeText(PerfectInfomationActivity.this, "������Ϣ����", Toast.LENGTH_SHORT).show();
            		break;
            	case 5:
            		Toast.makeText(PerfectInfomationActivity.this, "�绰��Ϣ����", Toast.LENGTH_SHORT).show();
            		break;
            	case 6:
            		Toast.makeText(PerfectInfomationActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
            		break;
            	default:
            		Toast.makeText(PerfectInfomationActivity.this, "���Ƴɹ�", Toast.LENGTH_SHORT).show();
            		finish();
            	}
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            super.onPostExecute(result);
        }
    }

}

