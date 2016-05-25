package com.tinet.ctilink.agentgateway;

import java.util.Map;

/**
 * 处理WebSocket动作的处理器接口
 * 
 * @author Jiangsl
 *
 */
public interface WebSocketActionHandler {
	String getAction();

	String handle(String cid, Map<String, Object> content);

}
