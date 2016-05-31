package com.tinet.ctilink.agentgateway.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tinet.ctilink.agentgateway.inc.*;
import com.tinet.ctilink.bigqueue.entity.ActionResponse;
import com.tinet.ctilink.bigqueue.inc.BigQueueCacheKey;
import com.tinet.ctilink.bigqueue.service.AgentService;
import com.tinet.ctilink.cache.RedisService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 置闲
 *
 * @author zoubo
 */
@Component
public class UnpauseActionHandler extends AbstractActionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Reference
    private AgentService agentService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RedisService redisService;

    @Override
    public String getAction() {
        return Action.UNPAUSE;
    }

    @Override
    public String handle(String cid, Map<String, Object> content) {
        Integer enterpriseId = MapUtils.getInteger(content, Variable.VARIABLE_ENTERPRISE_ID);
        String cno = MapUtils.getString(content, Variable.VARIABLE_CNO);

        Map<String, Object> event;
        try {
            ActionResponse response = agentService.unpause(content);

            if (response.getCode() == 0) {  //success
                event = Action.createSuccessResponse(content);

                //发送status事件
                Map<String, Object> params = new HashMap<>();
                params.put(Variable.VARIABLE_ENTERPRISE_ID, MapUtils.getString(content, Variable.VARIABLE_ENTERPRISE_ID));// 企业id
                params.put(Variable.VARIABLE_CNO, MapUtils.getString(content, Variable.VARIABLE_CNO));// 座席工号
                ActionResponse statusResponse = agentService.status(params);
                if (statusResponse.getCode() == 0) {
                    Map<String, Object> statusEvent = statusResponse.getValues();
                    statusEvent.put("event", "status");
                    statusEvent.put("enterpriseId", MapUtils.getString(content, Variable.VARIABLE_ENTERPRISE_ID));
                    statusEvent.put("cno", MapUtils.getString(content, Variable.VARIABLE_CNO));
                    redisService.convertAndSend(BigQueueCacheKey.AGENT_GATEWAY_EVENT_TOPIC, statusEvent);
                }
            } else {
                event = Action.createFailResponse(content, response.getCode(), response.getMsg());
            }

        } catch (Exception e) {
            event = Action.createFailResponse(content, -1, "bad param");
            logger.error("AbstractActionHandler error: ", e);
        }

        messagingTemplate.convertAndSendToUser(cid, SocketConst.SEND_TO_USER_AGENT, event);

        return null;
    }

}
