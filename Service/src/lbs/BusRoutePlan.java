package lbs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import client.ui.R;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.overlay.BusRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;

public class BusRoutePlan extends Activity implements OnRouteSearchListener, OnMarkerClickListener,
OnMapClickListener, OnInfoWindowClickListener, InfoWindowAdapter {
	private AMap aMap;
	private MapView mapView;
	private int busMode = RouteSearch.BusDefault;// 公交默认模式
	private BusRouteResult busRouteResult;// 公交模式查询结果
	private LatLonPoint startPoint = null;
	private LatLonPoint endPoint = null;
	private String city = null;
	private RouteSearch routeSearch;
	public ArrayAdapter<String> aAdapter;
	// 传入目的地信息
	private Bundle bundle;
	private LatLng des;
	private UiSettings mUiSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.busnav);

		mapView = (MapView) findViewById(R.id.busnav_mapview);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		
		// 目的地位置经纬度
		bundle = this.getIntent().getExtras();
		startPoint = new LatLonPoint(LocationService.getGeoLat(),LocationService.getGeoLng());
		endPoint = new LatLonPoint(bundle.getDouble("latitude"),
				bundle.getDouble("longitude"));
		des = new LatLng(bundle.getDouble("latitude"),
				bundle.getDouble("longitude"));
		
//		endPoint = new LatLonPoint(23.114155, 113.318977);
//		des = new LatLng(23.114155, 113.318977);
		
		city = LocationService.getCityCode();
		init();
		//开始搜索公交路线
		searchRouteResult(startPoint,endPoint);
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			mUiSettings = aMap.getUiSettings();
		}
		routeSearch = new RouteSearch(this);
		routeSearch.setRouteSearchListener(this);
		
		aMap.setOnMapClickListener(BusRoutePlan.this);
		aMap.setOnMarkerClickListener(BusRoutePlan.this);
		aMap.setOnInfoWindowClickListener(BusRoutePlan.this);
		aMap.setInfoWindowAdapter(BusRoutePlan.this);
		
		// 设置默认放大缩小按钮是否显示
		mUiSettings.setZoomControlsEnabled(false);
		// 设置指南针是否显示
		mUiSettings.setCompassEnabled(true);
		//将标记地点显示在屏幕中
		aMap.moveCamera(CameraUpdateFactory
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

	/**
	 * 选择公交模式
	 */
	private void busRoute() {
		busMode = RouteSearch.BusDefault;
	}

	/**
	 * 开始搜索路径规划方案
	 */
	public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
		//选择公交模式
		busRoute();
		
		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
				startPoint, endPoint);
		BusRouteQuery query = new BusRouteQuery(fromAndTo, busMode, city, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
		routeSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询

	}

	/**
	 * 公交路线查询回调
	 */
	@Override
	public void onBusRouteSearched(BusRouteResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				busRouteResult = result;
				BusPath busPath = busRouteResult.getPaths().get(0);
				aMap.clear();// 清理地图上的所有覆盖物
				BusRouteOverlay routeOverlay = new BusRouteOverlay(this, aMap,
						busPath, busRouteResult.getStartPos(),
						busRouteResult.getTargetPos());
				routeOverlay.removeFromMap();
				routeOverlay.addToMap();
				routeOverlay.zoomToSpan();
			} else {
				//ToastUtil.show(RouteActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			//ToastUtil.show(RouteActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			//ToastUtil.show(RouteActivity.this, R.string.error_key);
		} else {
//			ToastUtil.show(RouteActivity.this, getString(R.string.error_other)
//					+ rCode);
		}
	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
