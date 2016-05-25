package com.tinet.ctilink.agentgateway.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tinet.ctilink.bigqueue.entity.ActionResponse;
import com.tinet.ctilink.bigqueue.inc.BigQueueCacheKey;
import com.tinet.ctilink.bigqueue.service.AgentService;
import com.tinet.ctilink.agentgateway.WebSocketActionHandler;
import com.tinet.ctilink.agentgateway.inc.Action;
import com.tinet.ctilink.agentgateway.inc.ErrorMsg;
import com.tinet.ctilink.agentgateway.inc.SocketConst;
import com.tinet.ctilink.agentgateway.inc.Variable;
import com.tinet.ctilink.agentgateway.inc.ActionErrorUtil;
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
public class LoginActionHandler implements WebSocketActionHandler {
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

        //TODO 如果不是断线重连
        //updateClientOnline
        String result = confAgentService.updateAgentOnline(enterpriseId, cno, bindTel, bindType);
        if (!"success".equals(result)) {
            event = ActionErrorUtil.createFailResponse(content, -1, result);
            messagingTemplate.convertAndSendToUser(cid, SocketConst.SEND_TO_USER_AGENT, event);
            return null;
        }
        try {
            ActionResponse response = agentService.login(content);

            if (response.getCode() == 0) {  //success
                event = ActionErrorUtil.createSuccessResponse(content);
                event.put(Variable.VARIABLE_SESSION_ID, MapUtils.getString(content, Variable.VARIABLE_SESSION_ID)); // 当前session
                event.put(Variable.VARIABLE_ENTERPRISE_ID, enterpriseId);// 企业id
                event.put(Variable.VARIABLE_CNO, cno);// 座席工号
                event.put(Variable.VARIABLE_BIND_TEL, bindTel);// 绑定电话
                event.put(Variable.VARIABLE_BIND_TYPE, bindType);// 绑定电话类型

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
                event = ActionErrorUtil.createFailResponse(content, response.getCode(), response.getMsg());
            }

        } catch (Exception e) {
            event = ActionErrorUtil.createFailResponse(content, ErrorMsg.ERRORCODE_BAD_PARAM, "bad param");
            logger.error("AbstractActionHandler error: ", e);
        }

        messagingTemplate.convertAndSendToUser(cid, SocketConst.SEND_TO_USER_AGENT, event);

        return null;
    }
}
