package com.hdnav.socket;

import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

/**
 * @author 王维伟
 */
@ServerEndpoint(value = "/websocket/{loginName}")
@Component
public class WebSocketServer {

}
