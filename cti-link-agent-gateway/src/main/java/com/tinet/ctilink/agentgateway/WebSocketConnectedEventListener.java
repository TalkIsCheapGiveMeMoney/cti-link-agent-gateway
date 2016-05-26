package com.tinet.ctilink.agentgateway;

import com.tinet.ctilink.agentgateway.inc.Event;
import com.tinet.ctilink.agentgateway.inc.Variable;
import com.tinet.ctilink.bigqueue.inc.BigQueueCacheKey;
import com.tinet.ctilink.cache.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket连接建立的事件监听器
 * 
 * @author Jiangsl
 *
 */
@Component
public class WebSocketConnectedEventListener implements ApplicationListener<SessionConnectedEvent> {
	private static Logger logger = LoggerFactory.getLogger(WebSocketConnectedEventListener.class);

	@Autowired
	private RedisService redisService;

	@Override
	public void onApplicationEvent(final SessionConnectedEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = sha.getSessionId();
		String cid = event.getUser().getName();

		// 在建立新连接之前，先踢掉同一个用户的其它连接（通过Redis广播告诉整个集群），根据sessionId区分是否是新连接
		Map<String, String> userEvent = new HashMap<>();
		userEvent.put(Variable.VARIABLE_EVENT, Event.KICKOUT);
		userEvent.put(Variable.VARIABLE_CID, cid);
		userEvent.put(Variable.VARIABLE_SESSION_ID, sessionId);
		userEvent.put(Variable.VARIABLE_CODE, "0");
		userEvent.put(Variable.VARIABLE_MSG, "ok");
		redisService.convertAndSend(BigQueueCacheKey.AGENT_GATEWAY_EVENT_TOPIC, userEvent);
	}

}