package com.hdnav.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class Token {
	
	public static String generateToken(){
		String token = "{from:authority-management}";
		try {
			return new String(Base64.encodeBase64(token.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
}
