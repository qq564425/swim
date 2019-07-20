package com.hdnav.entity.vo;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             返回结果实体类        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-5 
*/ 
public class ResultVO {

    public ResultVO(boolean ok) {
        this.ok = ok;
    }

    public ResultVO() {
    }

    private boolean ok;

    private String msg;

    private String url;

    private Object data;
    
    private String serverIp;
    
    private int serverPort;
    
    private int websocketPort;
    
    private String serverUrl;
    
    private int flag;

    public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public int getWebsocketPort() {
		return websocketPort;
	}

	public void setWebsocketPort(int websocketPort) {
		this.websocketPort = websocketPort;
	}
	
    
}

