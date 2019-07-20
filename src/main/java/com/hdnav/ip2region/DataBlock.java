package com.hdnav.ip2region;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             数据类        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-8 
*/
public class DataBlock 
{
	/**
	 * city id 
	*/
	private int city_id;
	
	/**
	 * region address
	*/
	private String region;
	
	public DataBlock( int city_id, String region )
	{
		this.city_id = city_id;
		this.region = region;
	}

	public int getCityId() {
		return city_id;
	}

	public DataBlock setCityId(int city_id) {
		this.city_id = city_id;
		return this;
	}

	public String getRegion() {
		return region;
	}

	public DataBlock setRegion(String region) {
		this.region = region;
		return this;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(city_id).append('|').append(region);
		return sb.toString();
	}
}
