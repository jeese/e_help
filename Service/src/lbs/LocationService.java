package lbs;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

/**
 * 定位服务，打开app时开启，关闭app时关闭 请在app启动时bindservice 在app关闭时unbindservice
 * 
 * @author Jeese
 * 
 */
public class LocationService extends Service implements AMapLocationListener {

	private LocationManagerProxy mLocationManagerProxy = null;

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	@Override
	public void onDestroy() {
		if (mLocationManagerProxy != null) {
			mLocationManagerProxy.removeUpdates(this);
			mLocationManagerProxy.destroy();
		}
		mLocationManagerProxy = null;
		// 重置定位数据
		mLocation.getInstance().setSuccess(false);
		super.onDestroy();
	}

	/**
	 * 初始化定位
	 */
	private void init() {

		mLocationManagerProxy = LocationManagerProxy.getInstance(this);

		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);

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
			mLocation.getInstance().setGeoLat(amapLocation.getLatitude());
			mLocation.getInstance().setGeoLng(amapLocation.getLongitude());
			mLocation.getInstance().setProvince(amapLocation.getProvince());
			mLocation.getInstance().setCity(amapLocation.getCity());
			mLocation.getInstance().setCityCode(amapLocation.getCityCode());
			mLocation.getInstance().setDistrict(amapLocation.getDistrict());
			mLocation.getInstance().setAddress(amapLocation.getAddress());
			mLocation.getInstance().setStreet(amapLocation.getStreet());
			mLocation.getInstance().setAdCode(amapLocation.getAdCode());

			// 设置定位成功
			mLocation.getInstance().setSuccess(true);

			Toast.makeText(
					getApplicationContext(),"ubbhubhuhbu", Toast.LENGTH_LONG)
					.show();
		}
	}

	// 绑定服务，方便app关闭时关闭定位服务
	private MyBinder myBinder = new MyBinder();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return myBinder;
	}

	public class MyBinder extends Binder {

		public LocationService getService() {
			return LocationService.this;
		}
	}

}
