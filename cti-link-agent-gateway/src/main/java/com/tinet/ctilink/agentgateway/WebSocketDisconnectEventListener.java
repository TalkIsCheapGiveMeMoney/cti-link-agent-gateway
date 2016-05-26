package com.tinet.ctilink.agentgateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


/**
 * WebSocket连接中断的事件监听器
 * 
 * @author Jiangsl
 *
 */
@Component
public class WebSocketDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {
	private static Logger logger = LoggerFactory.getLogger(WebSocketDisconnectEventListener.class);

	@Override
	public void onApplicationEvent(final SessionDisconnectEvent event) {
//		if (event.getUser() == null) {
//			return;
//		}
//
//		// 如果是不受控（例如网络故障）的连接中断，则触发退出逻辑
//		String cid = event.getUser().getName();
//		CtiAgent ctiAgent = ctiAgentService.get(cid);
//		if (ctiAgent == null) {
//			return;
//		}
//
//		Map<String, Object> content = new HashMap<String, Object>();
//		logOutActionHandler.handle(cid, content);

	}

}