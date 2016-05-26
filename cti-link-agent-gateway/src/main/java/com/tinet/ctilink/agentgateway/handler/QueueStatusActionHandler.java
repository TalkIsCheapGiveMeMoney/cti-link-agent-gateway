package com.tinet.ctilink.agentgateway.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tinet.ctilink.bigqueue.entity.ActionResponse;
import com.tinet.ctilink.bigqueue.service.AgentService;
import com.tinet.ctilink.agentgateway.WebSocketActionHandler;
import com.tinet.ctilink.agentgateway.inc.Action;
import com.tinet.ctilink.agentgateway.inc.ErrorMsg;
import com.tinet.ctilink.agentgateway.inc.SocketConst;
import com.tinet.ctilink.agentgateway.inc.ActionErrorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class QueueStatusActionHandler implements WebSocketActionHandler {

    @Override
    public String getAction() {
        return Action.QUEUE_STATUS;
    }

    @Reference
    private AgentService agentService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @Override
    public String handle(String cid, Map<String, Object> content) {
        Map<String, Object> event;
        try {
            ActionResponse response = agentService.queueStatus(content);
            if (response.getCode() == 0) {  //success
                event = ActionErrorUtil.createSuccessResponse(content);
                event.put("queueStatus", response.getValues());
            } else {
                event = ActionErrorUtil.createFailResponse(content, response.getCode(), response.getMsg());
            }

        } catch (Exception e) {
            event = ActionErrorUtil.createFailResponse(content, ErrorMsg.ERRORCODE_BAD_PARAM, "bad param");
        }

        messagingTemplate.convertAndSendToUser(cid, SocketConst.SEND_TO_USER_AGENT, event);

        return null;
    }
}
