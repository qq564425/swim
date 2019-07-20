package com.hdnav.ip2region;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             数据块异常       
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-8 
*/
public class DbMakerConfigException extends Exception
{
	private static final long serialVersionUID = 4495714680349884838L;
	
	public DbMakerConfigException( String info ) {
		super(info);
	}
	
	public DbMakerConfigException( Throwable res ) {
		super(res);
	}
	
	public DbMakerConfigException( String info, Throwable res ) {
		super(info, res);
	}
}