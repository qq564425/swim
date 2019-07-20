package com.hdnav.ip2region;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             工具类        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-8 
*/
public class Util 
{
	/**
	 * 写入指定长度的字节到byte数组
	 * 
	 * @param	b, offset, v, bytes
	*/
	public static void write( byte[] b, int offset, long v, int bytes)
	{
		for ( int i = 0; i < bytes; i++ )
		{
			b[offset++] = (byte)((v >>> (8 * i)) & 0xFF);
		}
	}
	
	/**
	 * 写入一个byte数组
	 * 
	 * @param b, offet, v
	*/
	public static void writeIntLong( byte[] b, int offset, long v )
	{
		b[offset++] = (byte)((v >>  0) & 0xFF);
		b[offset++] = (byte)((v >>  8) & 0xFF);
		b[offset++] = (byte)((v >> 16) & 0xFF);
		b[offset  ] = (byte)((v >> 24) & 0xFF);
	}
	
	/**
	 * 从byte数组中取出指定long类型数据
	 * 
	 * @param  b, offset
	 * @return long
	*/
	public static long getIntLong( byte[] b, int offset )
	{
		return (
			((b[offset++] & 0x000000FFL)) |
			((b[offset++] <<  8) & 0x0000FF00L) |
			((b[offset++] << 16) & 0x00FF0000L) | 
			((b[offset  ] << 24) & 0xFF000000L)
		);
	}
	

	/**
	 * 从byte数组中取出指定int类型数据
	 * 
	 * @param  b, offset
	 * @return int
	*/
	public static int getInt3( byte[] b, int offset )
	{ 
		return (
			(b[offset++] & 0x000000FF) |
			(b[offset++] & 0x0000FF00) |
			(b[offset  ] & 0x00FF0000)
		);
	}
	
	/**
	 * 从byte数组中取出指定int类型数据
	 * 
	 * @param  b, offset
	 * @return int
	*/
	public static int getInt2( byte[] b, int offset )
	{
		return (
			(b[offset++] & 0x000000FF) |
			(b[offset  ] & 0x0000FF00)
		);
	}
	
	/**
	 * 从byte数组中取出指定int类型数据
	 * 
	 * @param  b, offset
	 * @return int
	*/
	public static int getInt1( byte[] b, int offset )
	{
		return (
			(b[offset] & 0x000000FF)
		);
	}
	
	/**
	 * 将ip地址从String类型转换为long类型
	 * 
	 * @param	ip
	 * @return	long
	*/
	public static long ip2long( String ip )
	{
		String[] p = ip.split("\\.");
		if ( p.length != 4 ) return 0;
		
		int p1 = ((Integer.valueOf(p[0]) << 24) & 0xFF000000);
		int p2 = ((Integer.valueOf(p[1]) << 16) & 0x00FF0000);
		int p3 = ((Integer.valueOf(p[2]) <<  8) & 0x0000FF00);
		int p4 = ((Integer.valueOf(p[3]) <<  0) & 0x000000FF);
		
		return ((p1 | p2 | p3 | p4) & 0xFFFFFFFFL);
	}
	
	/**
	 * 将ip地址从int类型转换为String类型
	 * 
	 * @param	ip
	 * @return	String
	*/
	public static String long2ip( long ip )
	{
		StringBuilder sb = new StringBuilder();
		
		sb
		.append((ip >> 24) & 0xFF).append('.')
		.append((ip >> 16) & 0xFF).append('.')
		.append((ip >>  8) & 0xFF).append('.')
		.append((ip >>  0) & 0xFF);
		
		return sb.toString();
	}
	
	/**
	 * check the validate of the specifeld ip address
	 * 
	 * @param	ip
	 * @return	boolean
	*/
	public static boolean isIpAddress( String ip )
	{
		String[] p = ip.split("\\.");
		if ( p.length != 4 ) return false;
		
		for ( String pp : p )
		{
			if ( pp.length() > 3 ) return false;
			int val = Integer.valueOf(pp);
			if ( val > 255 ) return false;
		}
		
		return true;
	}
}
