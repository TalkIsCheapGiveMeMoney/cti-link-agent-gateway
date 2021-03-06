package com.tinet.ctilink.agentgateway.handler;

import java.util.Map;

import com.tinet.ctilink.agentgateway.ActionHandler;
import com.tinet.ctilink.agentgateway.inc.Action;
import com.tinet.ctilink.agentgateway.inc.SocketConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * ping
 * @author zoubo
 *
 */
@Component
public class PingActionHandler implements ActionHandler {
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Override
	public String getAction() {
		return Action.PING;
	}

	@Override
	public String handle(String cid, Map<String,Object> content){
		Map<String,Object> response = Action.createSuccessResponse(content);
		messagingTemplate.convertAndSendToUser(cid, SocketConst.SEND_TO_USER_AGENT, response);
		return null; 
	}
}
