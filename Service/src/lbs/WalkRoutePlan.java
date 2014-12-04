package lbs;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import client.ui.R;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;

public class WalkRoutePlan extends Activity implements OnClickListener,
		AMapNaviListener {

	// 步行导航按钮
	private Button WalkNaviButton;

	// 地图和导航资源
	private MapView mMapView;
	private AMap mAMap;
	private AMapNavi mAMapNavi;
	private UiSettings mUiSettings;

	// 起点终点坐标
	private NaviLatLng mNaviStart = new NaviLatLng(LocationService.getGeoLat(),
			LocationService.getGeoLng());
	private NaviLatLng mNaviEnd;
	private LatLng mEnd;
	// 起点终点列表
	private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
	private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();

	// 规划线路
	private RouteOverLay mRouteOverLay;

	// 传入目的地信息
	private Bundle bundle;

	// 判断路径是否计算成功
	private boolean mIsCalculateRouteSuccess = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.walknav);
		// 目的地位置经纬度
		bundle = this.getIntent().getExtras();
		mNaviEnd = new NaviLatLng(bundle.getDouble("latitude"),
				bundle.getDouble("longitude"));
		mEnd = new LatLng(bundle.getDouble("latitude"),
				bundle.getDouble("longitude"));
		initView(savedInstanceState);

		calculateFootRoute();
	}

	// 初始化View
	private void initView(Bundle savedInstanceState) {
		mAMapNavi = AMapNavi.getInstance(this);
		mAMapNavi.setAMapNaviListener(this);
		mStartPoints.add(mNaviStart);
		mEndPoints.add(mNaviEnd);

		WalkNaviButton = (Button) findViewById(R.id.walknav_walk);
		WalkNaviButton.setOnClickListener(this);

		mMapView = (MapView) findViewById(R.id.walknav_mapview);
		mMapView.onCreate(savedInstanceState);
		mAMap = mMapView.getMap();
		mRouteOverLay = new RouteOverLay(mAMap, null);

		mUiSettings = mAMap.getUiSettings();
		// 设置默认放大缩小按钮是否显示
		mUiSettings.setZoomControlsEnabled(false);
		// 设置指南针是否显示
		mUiSettings.setCompassEnabled(true);

		// 将标记地点显示在屏幕中
		mAMap.moveCamera(CameraUpdateFactory
				.newCameraPosition(new CameraPosition(mEnd, 16, 30, 0)));
	}

	// 计算步行路线
	private void calculateFootRoute() {
		boolean isSuccess = mAMapNavi.calculateWalkRoute(mNaviStart, mNaviEnd);
		if (!isSuccess) {
			Toast.makeText(this, "路线计算失败,检查参数情况", Toast.LENGTH_SHORT).show();
		}
	}

	// 开始导航
	private void startGPSNavi() {
		if (mIsCalculateRouteSuccess) {
			Intent gpsIntent = new Intent(WalkRoutePlan.this, mNaviPage.class);
			startActivity(gpsIntent);
		} else {
			Toast.makeText(this, "路径规划出错", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onArriveDestination() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onArrivedWayPoint(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
		Toast.makeText(this, "路径规划出错", Toast.LENGTH_SHORT).show();
		mIsCalculateRouteSuccess = false;
	}

	@Override
	public void onCalculateRouteSuccess() {
		AMapNaviPath naviPath = mAMapNavi.getNaviPath();
		if (naviPath == null) {
			return;
		}
		// 获取路径规划线路，显示到地图上
		mRouteOverLay.setRouteInfo(naviPath);
		mRouteOverLay.addToMap();
		if (mRouteOverLay != null) {
			mRouteOverLay.zoomToSpan();
		}

		mIsCalculateRouteSuccess = true;
	}

	@Override
	public void onEndEmulatorNavi() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviFailure() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForYaw() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartNavi(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTrafficStatusUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.walknav_walk:
			startGPSNavi();
			this.finish();
			break;

		}
	}

	// ------------------生命周期重写函数---------------------------

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		// 删除监听
		AMapNavi.getInstance(this).removeAMapNaviListener(this);

	}

}
