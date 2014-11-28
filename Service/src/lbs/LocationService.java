package lbs;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.model.LatLng;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * 定位服务，打开app时开启，关闭app时关闭 请在app启动时bindservice 在app关闭时unbindservice
 * 通过静态方法可以获取定位信息 调用前先判断是否定位成功及service是否已经开启
 * service开启一次定位一次，需要更新位置时startservice则可
 * @author Jeese
 * 
 */
public class LocationService extends Service implements AMapLocationListener {

	private LocationManagerProxy mLocationManagerProxy = null;

	private static double geoLat;// 纬度
	private static double geoLng;// 精度
	private static String province; // 省名称
	private static String city; // 城市名称
	private static String city_code; // 城市编码
	private static String district;// 区县名称
	private static String ad_code;// 区域编码
	private static String street;// 街道和门牌信息
	private static String address;// 详细地址
	
	private static LatLng latlng;

	private static boolean success = false;// 是否定位成功

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		init();
	}

	@Override
	public void onDestroy() {
		if (mLocationManagerProxy != null) {
			mLocationManagerProxy.removeUpdates(this);
			mLocationManagerProxy.destroy();
		}
		mLocationManagerProxy = null;
		super.onDestroy();
	}

	/**
	 * 初始化定位
	 */
	private void init() {

		mLocationManagerProxy = LocationManagerProxy
				.getInstance(LocationService.this);

		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, -1, 15, this);

		mLocationManagerProxy.setGpsEnable(false);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null
				&& amapLocation.getAMapException().getErrorCode() == 0) {
			// 设置位置详细信息
			geoLat = amapLocation.getLatitude();
			geoLng = amapLocation.getLongitude();
			province = amapLocation.getProvince();
			city = amapLocation.getCity();
			city_code = amapLocation.getCityCode();
			district = amapLocation.getDistrict();
			address = amapLocation.getAddress();
			street = amapLocation.getStreet();
			ad_code = amapLocation.getAdCode();

			// 设置定位成功
			success = true;
			
			System.out.println("定位一次" + amapLocation.getStreet());
		}
	}

	// 绑定服务，方便app关闭时关闭定位服务
	private MyBinder myBinder = new MyBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return myBinder;
	}

	public class MyBinder extends Binder {

		public LocationService getService() {
			return LocationService.this;
		}
	}

	/******* get *************/
	public static boolean getSuccess() {
		return success;
	}

	public static double getGeoLat() {
		return geoLat;
	}

	public static double getGeoLng() {
		return geoLng;
	}

	public static String getProvince() {
		return province;
	}

	public static String getCity() {
		return city;
	}

	public static String getCityCode() {
		return city_code;
	}

	public static String getDistrict() {
		return district;
	}

	public static String getAdCode() {
		return ad_code;
	}

	public static String getStreet() {
		return street;
	}

	public static String getAddress() {
		return address;
	}
	
	public static LatLng getLatLng() {
		return new LatLng(geoLat,geoLng);
	}

}
