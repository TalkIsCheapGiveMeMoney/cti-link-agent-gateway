package com.tinet.ctilink.agentgateway;

import java.util.List;
import java.util.Map;

import com.tinet.ctilink.json.JSONObject;
import com.tinet.ctilink.agentgateway.inc.Event;
import com.tinet.ctilink.agentgateway.inc.Variable;
import com.tinet.ctilink.agentgateway.inc.SocketConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;

/**
 * AMI事件监听器，通过Redis的pub/sub监听AMI事件，并通过WebSocket推送至座席端的浏览器
 * 
 * @author Jiangsl
 *
 */
public class BigQueueEventListener {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private SimpUserRegistry simpUserRegistry;

	@Autowired
	private WebSocketCache webSocketCache;


	public void handleMessage(String json, String channel) {
		System.out.println(json);
		Map event = JSONObject.getBean(json, Map.class);

		// 在建立新连接之前，先踢掉同一个用户的其它连接，根据sessionId区分是否是新连接
		String name = event.get(Variable.VARIABLE_EVENT).toString();

		// 这两事件先不处理
		switch (name) {
		case Event.INCOMING:
		case Event.ANSWER:
			return;
		}

		if (Event.KICKOUT.equals(name)) {
			String cid = event.get(Variable.VARIABLE_CID).toString();
			if (simpUserRegistry.getUser(cid) != null) {
				messagingTemplate.convertAndSendToUser(cid, SocketConst.SEND_TO_USER_AGENT, event);
			}
			return;
		}

		// 发送到座席
		sendMessage(event);
	}


	/**
	 * 执行往座席端发送消息
	 * 
	 * @param event
	 */
	private void sendMessage(Map event) {
		//增加type类型
		event.put(Variable.VARIABLE_TYPE, Variable.VARIABLE_EVENT);
		String name = event.get(Variable.VARIABLE_EVENT).toString();
		//暂时兼容老得工具条
		event.put(Variable.VARIABLE_NAME, name);
		event.remove(Variable.VARIABLE_EVENT);
		String enterpriseId = event.get(Variable.VARIABLE_ENTERPRISE_ID).toString();
		switch (name) {
		case Event.STATUS:
			String cno = event.get(Variable.VARIABLE_CNO).toString();
			List<String> qList = webSocketCache.getQidsbyCid(Integer.parseInt(enterpriseId), cno);
			if(qList == null || qList.size() == 0){
				return ;
			}
			// 广播发送给队列中的所有座席
			messagingTemplate.convertAndSend("/queue/" + String.join(",", qList), event);

			break;
			
		case Event.JOIN_QUEUE:
		case Event.LEAVE_QUEUE:
		case Event.QUEUE_CALL:
		case Event.ORDER_CALL_BACK:
			Object queueName = event.get(Variable.VARIABLE_QID);
			
			if (queueName != null && !queueName.toString().isEmpty()) {
				// 广播发送给队列中的所有座席
				messagingTemplate.convertAndSend("/queue/" + queueName.toString(), event);
			}
			
			break;

		case Event.INCOMING:
		case Event.ANSWER:
		case Event.UNANSWER:
			// 广播发送给企业的所有座席
			messagingTemplate.convertAndSend("/enterprise/" + enterpriseId, event);

			break;

		default:
			cno = event.get(Variable.VARIABLE_CNO).toString();
			String cid = enterpriseId + cno;
			// 发送给指定的座席
			if (simpUserRegistry.getUser(cid) != null) {
				messagingTemplate.convertAndSendToUser(cid, SocketConst.SEND_TO_USER_AGENT, event);
			}

			break;
		}

	}

}
