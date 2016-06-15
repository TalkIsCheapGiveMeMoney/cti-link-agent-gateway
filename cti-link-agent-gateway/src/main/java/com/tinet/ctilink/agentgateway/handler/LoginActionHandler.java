package com.tinet.ctilink.agentgateway.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tinet.ctilink.bigqueue.entity.ActionResponse;
import com.tinet.ctilink.bigqueue.inc.BigQueueCacheKey;
import com.tinet.ctilink.bigqueue.service.AgentService;
import com.tinet.ctilink.agentgateway.inc.Action;
import com.tinet.ctilink.agentgateway.inc.SocketConst;
import com.tinet.ctilink.agentgateway.inc.Variable;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.conf.service.v1.CtiLinkAgentService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LoginActionHandler extends AbstractActionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Reference
    private AgentService agentService;

    @Reference
    private CtiLinkAgentService confAgentService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RedisService redisService;

    @Override
    public String getAction() {
        return Action.LOGIN;
    }

    @Override
    public String handle(String cid, Map<String, Object> content) {
        Integer enterpriseId = MapUtils.getInteger(content, Variable.VARIABLE_ENTERPRISE_ID);
        String cno = MapUtils.getString(content, Variable.VARIABLE_CNO);
        String bindTel = MapUtils.getString(content, Variable.VARIABLE_BIND_TEL);
        Integer bindType = MapUtils.getInteger(content, Variable.VARIABLE_BIND_TYPE);
        Integer loginStatus = MapUtils.getInteger(content, Variable.VARIABLE_LOGIN_STATUS);

        Map<String, Object> event;

        //断线重连, 直接获取状态返回就可以
        if (loginStatus == -1) {
            //发送status事件
            Map<String, Object> params = new HashMap<>();
            params.put(Variable.VARIABLE_ENTERPRISE_ID, enterpriseId);// 企业id
            params.put(Variable.VARIABLE_CNO, cno);// 座席工号
            ActionResponse statusResponse = agentService.status(params);
            if (statusResponse.getCode() == 0) {
                Map<String, Object> statusEvent = statusResponse.getValues();
                statusEvent.put("event", "status");
                statusEvent.put("enterpriseId", enterpriseId);
                statusEvent.put("cno", cno);
                redisService.convertAndSend(BigQueueCacheKey.AGENT_GATEWAY_EVENT_TOPIC, statusEvent);
            }

            return null;
        }

        //updateClientOnline
        String result = confAgentService.updateAgentOnline(enterpriseId, cno, bindTel, bindType);
        if (!"success".equals(result)) {
            event = Action.createFailResponse(content, -1, result);
            messagingTemplate.convertAndSendToUser(cid, SocketConst.SEND_TO_USER_AGENT, event);
            return null;
        }
        try {
            ActionResponse response = agentService.login(content);

            if (response.getCode() == 0) {  //success
                event = Action.createSuccessResponse(content);
                event.put(Variable.VARIABLE_SESSION_ID, MapUtils.getString(content, Variable.VARIABLE_SESSION_ID)); // 当前session
                event.put(Variable.VARIABLE_ENTERPRISE_ID, enterpriseId);// 企业id
                event.put(Variable.VARIABLE_CNO, cno);// 座席工号
                event.put(Variable.VARIABLE_BIND_TEL, bindTel);// 绑定电话
                event.put(Variable.VARIABLE_BIND_TYPE, bindType);// 绑定电话类型

                //发送status事件
                Map<String, Object> params = new HashMap<>();
                params.put(Variable.VARIABLE_ENTERPRISE_ID, enterpriseId);// 企业id
                params.put(Variable.VARIABLE_CNO, cno);// 座席工号
                ActionResponse statusResponse = agentService.status(params);
                if (statusResponse.getCode() == 0) {
                    Map<String, Object> statusEvent = statusResponse.getValues();
                    statusEvent.put("event", "status");
                    statusEvent.put("enterpriseId", enterpriseId);
                    statusEvent.put("cno", cno);
                    redisService.convertAndSend(BigQueueCacheKey.AGENT_GATEWAY_EVENT_TOPIC, statusEvent);
                }
            } else {
                event = Action.createFailResponse(content, response.getCode(), response.getMsg());
            }

        } catch (Exception e) {
            event = Action.createFailResponse(content, -1, "exception at agent-gateway");
            logger.error("LoginActionHandler error: ", e);
        }

        messagingTemplate.convertAndSendToUser(cid, SocketConst.SEND_TO_USER_AGENT, event);

        return null;
    }
}
