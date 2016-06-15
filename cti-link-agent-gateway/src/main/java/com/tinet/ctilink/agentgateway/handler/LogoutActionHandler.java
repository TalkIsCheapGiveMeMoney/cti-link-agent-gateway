package com.tinet.ctilink.agentgateway.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tinet.ctilink.agentgateway.inc.*;
import com.tinet.ctilink.bigqueue.entity.ActionResponse;
import com.tinet.ctilink.bigqueue.service.AgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 座席退出
 *
 * @author zoubo
 */
@Component
public class LogoutActionHandler extends AbstractActionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Reference
    private AgentService agentService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public String getAction() {
        return Action.LOGOUT;
    }

    @Override
    public String handle(String cid, Map<String, Object> content) {

        Map<String, Object> event;
        try {
            ActionResponse response = agentService.logout(content);

            if (response.getCode() == 0) {  //success
                event = Action.createSuccessResponse(content);
            } else {
                event = Action.createFailResponse(content, response.getCode(), response.getMsg());
            }

        } catch (Exception e) {
            event = Action.createFailResponse(content, -1, "bad param");
            logger.error("LogoutActionHandler error: ", e);
        }

        messagingTemplate.convertAndSendToUser(cid, SocketConst.SEND_TO_USER_AGENT, event);

        return null;
    }

}
