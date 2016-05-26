package com.tinet.ctilink.agentgateway.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tinet.ctilink.bigqueue.service.AgentService;
import com.tinet.ctilink.agentgateway.inc.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * 摘机直呼送客户号码
 *
 * @author zoubo
 */
@Component
public class DirectCallStartActionHandler extends AbstractActionHandler {

    @Override
    public String getAction() {
        return Action.DIRECT_CALL_START;
    }

    @Reference
    private AgentService agentService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public AgentService getAgentService() {
        return agentService;
    }

    @Override
    public SimpMessagingTemplate getMessagingTemplate() {
        return messagingTemplate;
    }
}
