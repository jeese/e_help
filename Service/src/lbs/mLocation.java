package lbs;

/**
 * Location数据类
 * 单例模式
 * 用于获取用户的位置信息
 * @author Jeese
 */
public class mLocation {
	private double geoLat = (Double) null;// 纬度
	private double geoLng = (Double) null;// 精度
	private String province = null; // 省名称
	private String city = null; // 城市名称
	private String city_code = null; // 城市编码
	private String district = null;// 区县名称
	private String ad_code = null;// 区域编码
	private String street = null;// 街道和门牌信息
	private String address = null;// 详细地址
	
	private boolean success = false;//是否定位成功

	/*
	 * 单例模式
	 */
	private static mLocation location = null;
	public static mLocation getInstance() {
		if (location == null) {
			location = new mLocation();
		}
		return location;
	}
	
	/*******     get     *************/
	public boolean getSuccess(){
		return success;
	}
	public double getGeoLat(){
		return geoLat;
	}
	public double getGeoLng(){
		return geoLng;
	}
	public String getProvince(){
		return province;
	}
	public String getCity(){
		return city;
	}
	public String getCityCode(){
		return city_code;		
	}
	public String getDistrict(){
		return district;		
	}
	public String getAdCode(){
		return ad_code;	
	}
	public String getStreet(){
		return street;		
	}
	public String getAddress(){
		return address;		
	}
	/*******     get     *************/
	
	/*******     set     *************/
	public void setSuccess(boolean set){
		success = set;
	}
	public void setGeoLat(double set){
		geoLat = set;
	}
	public void setGeoLng(double set){
		geoLng = set;
	}
	public void setProvince(String set){
		province = set;
	}
	public void setCity(String set){
		city = set;
	}
	public void setCityCode(String set){
		city_code = set;		
	}
	public void setDistrict(String set){
		district = set;		
	}
	public void setAdCode(String set){
		ad_code = set;
	}
	public void setStreet(String set){
		street = set;	
	}
	public void setAddress(String set){
		address = set;		
	}
	/*******     set     *************/
}
