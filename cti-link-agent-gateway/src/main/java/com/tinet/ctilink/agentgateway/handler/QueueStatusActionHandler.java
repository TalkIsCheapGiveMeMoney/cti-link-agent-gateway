package com.tinet.ctilink.agentgateway.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tinet.ctilink.bigqueue.entity.ActionResponse;
import com.tinet.ctilink.bigqueue.service.AgentService;
import com.tinet.ctilink.agentgateway.inc.Action;
import com.tinet.ctilink.agentgateway.inc.SocketConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class QueueStatusActionHandler extends AbstractActionHandler {

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
                event = Action.createSuccessResponse(content);
                event.put("queueStatus", response.getValues());
            } else {
                event = Action.createFailResponse(content, response.getCode(), response.getMsg());
            }

        } catch (Exception e) {
            event = Action.createFailResponse(content, -1, "exception at agent-gateway");
        }

        messagingTemplate.convertAndSendToUser(cid, SocketConst.SEND_TO_USER_AGENT, event);

        return null;
    }
}
