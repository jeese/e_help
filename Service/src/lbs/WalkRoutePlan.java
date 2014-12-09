package lbs;

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

import java.util.ArrayList;

public class WalkRoutePlan extends Activity implements OnClickListener,
		AMapNaviListener {

	// ���е�����ť
	private Button WalkNaviButton;

	// ��ͼ�͵�����Դ
	private MapView mMapView;
	private AMap mAMap;
	private AMapNavi mAMapNavi;
	private UiSettings mUiSettings;

	// ����յ�����
	private NaviLatLng mNaviStart = new NaviLatLng(LocationService.getGeoLat(),
			LocationService.getGeoLng());
	private NaviLatLng mNaviEnd;
	private LatLng mEnd;
	// ����յ��б�
	private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
	private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();

	// �滮��·
	private RouteOverLay mRouteOverLay;

	// ����Ŀ�ĵ���Ϣ
	private Bundle bundle;

	// �ж�·���Ƿ����ɹ�
	private boolean mIsCalculateRouteSuccess = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.walknav);
		// Ŀ�ĵ�λ�þ�γ��
		bundle = this.getIntent().getExtras();
		mNaviEnd = new NaviLatLng(bundle.getDouble("latitude"),
				bundle.getDouble("longitude"));
		mEnd = new LatLng(bundle.getDouble("latitude"),
				bundle.getDouble("longitude"));
		initView(savedInstanceState);

		calculateFootRoute();
	}

	// ��ʼ��View
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
		// ����Ĭ�ϷŴ���С��ť�Ƿ���ʾ
		mUiSettings.setZoomControlsEnabled(false);
		// ����ָ�����Ƿ���ʾ
		mUiSettings.setCompassEnabled(true);

		// ����ǵص���ʾ����Ļ��
		mAMap.moveCamera(CameraUpdateFactory
				.newCameraPosition(new CameraPosition(mEnd, 16, 30, 0)));
	}

	// ���㲽��·��
	private void calculateFootRoute() {
		boolean isSuccess = mAMapNavi.calculateWalkRoute(mNaviStart, mNaviEnd);
		if (!isSuccess) {
			Toast.makeText(this, "·�߼���ʧ��,���������", Toast.LENGTH_SHORT).show();
		}
	}

	// ��ʼ����
	private void startGPSNavi() {
		if (mIsCalculateRouteSuccess) {
			Intent gpsIntent = new Intent(WalkRoutePlan.this, mNaviPage.class);
			startActivity(gpsIntent);
		} else {
			Toast.makeText(this, "·���滮����", Toast.LENGTH_SHORT).show();
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
		Toast.makeText(this, "·���滮����", Toast.LENGTH_SHORT).show();
		mIsCalculateRouteSuccess = false;
	}

	@Override
	public void onCalculateRouteSuccess() {
		AMapNaviPath naviPath = mAMapNavi.getNaviPath();
		if (naviPath == null) {
			return;
		}
		// ��ȡ·���滮��·����ʾ����ͼ��
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

	// ------------------����������д����---------------------------

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
		// ɾ������
		AMapNavi.getInstance(this).removeAMapNaviListener(this);

	}

}
