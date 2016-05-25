package com.tinet.ctilink.agentgateway.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tinet.ctilink.bigqueue.entity.ActionResponse;
import com.tinet.ctilink.bigqueue.service.AgentService;
import com.tinet.ctilink.agentgateway.WebSocketActionHandler;
import com.tinet.ctilink.agentgateway.inc.Action;
import com.tinet.ctilink.agentgateway.inc.ErrorMsg;
import com.tinet.ctilink.agentgateway.inc.SocketConst;
import com.tinet.ctilink.agentgateway.inc.Variable;
import com.tinet.ctilink.agentgateway.inc.ActionErrorUtil;
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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public String getAction() {
        return Action.LOGIN;
    }

    @Override
    public String handle(String cid, Map<String, Object> content) {
        Map<String, Object> event;
        try {
            ActionResponse response = agentService.login(content);

            if (response.getCode() == 0) {  //success
                event = ActionErrorUtil.createSuccessResponse(content);
                event.put(Variable.VARIABLE_SESSION_ID, MapUtils.getString(content, Variable.VARIABLE_SESSION_ID)); // 当前session
                event.put(Variable.VARIABLE_ENTERPRISE_ID, MapUtils.getString(content, Variable.VARIABLE_ENTERPRISE_ID));// 企业id
                event.put(Variable.VARIABLE_CNO, MapUtils.getString(content, Variable.VARIABLE_CNO));// 座席工号
                event.put(Variable.VARIABLE_CNAME, MapUtils.getString(content, Variable.VARIABLE_CNAME));// 座席姓名
                event.put(Variable.VARIABLE_BIND_TEL, MapUtils.getString(content, Variable.VARIABLE_BIND_TEL));// 绑定电话
                event.put(Variable.VARIABLE_BIND_TYPE, MapUtils.getString(content, Variable.VARIABLE_BIND_TYPE));// 绑定电话类型

                //发送status事件
                Map<String, Object> params = new HashMap<>();
                params.put(Variable.VARIABLE_ENTERPRISE_ID, MapUtils.getString(content, Variable.VARIABLE_ENTERPRISE_ID));// 企业id
                params.put(Variable.VARIABLE_CNO, MapUtils.getString(content, Variable.VARIABLE_CNO));// 座席工号
                response = agentService.status(params);

                if (response.getCode() == 0) {
                    Map<String, Object> statusMap = response.getValues();
                    statusMap.put("type", "event");
                    statusMap.put("name", "status");
                    statusMap.put("enterpriseId", MapUtils.getString(content, Variable.VARIABLE_ENTERPRISE_ID));
                    messagingTemplate.convertAndSendToUser(cid, SocketConst.SEND_TO_USER_AGENT, statusMap);
                    //messagingTemplate.convertAndSend("/enterprise/" + enterpriseId, event);
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
