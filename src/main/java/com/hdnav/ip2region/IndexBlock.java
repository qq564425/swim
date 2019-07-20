package com.hdnav.ip2region;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             数据块       
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-8 
*/
public class IndexBlock 
{
	private static int LENGTH = 12;
	
	/**
	 * 起始ip地址
	*/
	private long startIp;
	
	/**
	 * 结束ip地址 
	*/
	private long endIp;
	
	/**
	 * 数据指针
	*/
	private int dataPtr;
	
	/**
	 * 数据长度
	*/
	private int dataLen;
	
	public IndexBlock(long startIp, long endIp, int dataPtr, int dataLen)
	{
		this.startIp = startIp;
		this.endIp = endIp;
		this.dataPtr = dataPtr;
		this.dataLen = dataLen;
	}

	public long getStartIp() {
		return startIp;
	}

	public IndexBlock setStartIp(long startIp) {
		this.startIp = startIp;
		return this;
	}

	public long getEndIp() {
		return endIp;
	}

	public IndexBlock setEndIp(long endIp) {
		this.endIp = endIp;
		return this;
	}

	public int getDataPtr() {
		return dataPtr;
	}

	public IndexBlock setDataPtr(int dataPtr) {
		this.dataPtr = dataPtr;
		return this;
	}

	public int getDataLen() {
		return dataLen;
	}

	public IndexBlock setDataLen(int dataLen) {
		this.dataLen = dataLen;
		return this;
	}
	
	public static int getIndexBlockLength() {
		return LENGTH;
	}
	
	/**
	 * 获取要存储的字节
	 * 
	 * @return	byte[]
	*/
	public byte[] getBytes()
	{
		/*
		 * +------------+-----------+-----------+
		 * | 4bytes		| 4bytes	| 4bytes	|
		 * +------------+-----------+-----------+
		 *  start ip      end ip      data ptr + len 
		*/
		byte[] b = new byte[12];
		
		Util.writeIntLong(b, 0, startIp);	//start ip
		Util.writeIntLong(b, 4, endIp);		//end ip
		
		//write the data ptr and the length
		long mix = dataPtr | ((dataLen << 24) & 0xFF000000L);
		Util.writeIntLong(b, 8, mix);
		
		return b;
	}
}
