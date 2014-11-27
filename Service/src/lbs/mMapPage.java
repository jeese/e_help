package lbs;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;

import client.ui.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class mMapPage extends Activity implements OnClickListener {
	private MapView mapView;
	private AMap aMap;
	private UiSettings mUiSettings;
	
	private Bundle bundle;
	private LatLng des;//目的地坐标

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mappage);
		mapView = (MapView) findViewById(R.id.mappage_mapview);
		mapView.onCreate(savedInstanceState);// 必须要写

		// 目的地位置经纬度
		bundle = this.getIntent().getExtras();
		double desla = bundle.getDouble("latitude");
		double deslo = bundle.getDouble("longitude");
		des = new LatLng(desla,deslo);
		
		init();
	}

	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			mUiSettings = aMap.getUiSettings();
			setUpMap();
		}
		Button mappage_walk = (Button) findViewById(R.id.mappage_walk);
		mappage_walk.setOnClickListener(this);
		Button mappage_drive = (Button) findViewById(R.id.mappage_drive);
		mappage_drive.setOnClickListener(this);
		Button mappage_transit = (Button) findViewById(R.id.mappage_transit);
		mappage_transit.setOnClickListener(this);
	}

	/**
	 * amap添加一些事件监听器
	 */
	private void setUpMap() {
		// 设置地图logo显示在左下方
		mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
		// 设置默认缩放倍数
		aMap.moveCamera(CameraUpdateFactory.zoomTo((float) 16));
		// 设置默认放大缩小按钮是否显示
		mUiSettings.setZoomControlsEnabled(false);
		// 设置指南针是否显示
		mUiSettings.setCompassEnabled(true);

		// 在标记位置做标记
		aMap.addMarker(new MarkerOptions().position(des).icon(
				BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		//将标记地点显示在屏幕中
		aMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(new CameraPosition(des, 16,
						30, 0)));
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.mappage_walk:
			break;

		case R.id.mappage_drive:
			break;

		case R.id.mappage_transit:
			break;
		}
	}
}
