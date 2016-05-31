package com.tinet.ctilink.agentgateway.handler;

import com.tinet.ctilink.agentgateway.inc.Action;
import com.tinet.ctilink.bigqueue.entity.ActionResponse;
import com.tinet.ctilink.bigqueue.service.AgentService;
import com.tinet.ctilink.agentgateway.ActionHandler;
import com.tinet.ctilink.agentgateway.inc.SocketConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author fengwei //
 * @date 16/5/24 09:32
 */
public abstract class AbstractActionHandler implements ActionHandler {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String handle(String cid, Map<String, Object> content) {
        Map<String, Object> event;
        try {
            //获取方法
            Method method = getAgentService().getClass().getMethod(getAction(), Map.class);
            //执行
            Object object = method.invoke(getAgentService(), content);
            ActionResponse response = (ActionResponse) object;

            if (response.getCode() == 0) {  //success
                event = Action.createSuccessResponse(content);
            } else {
                event = Action.createFailResponse(content, response.getCode(), response.getMsg());
            }

        } catch (Exception e) {
            event = Action.createFailResponse(content, -1, "bad param");
            logger.error("AbstractActionHandler error: ", e);
        }

        getMessagingTemplate().convertAndSendToUser(cid, SocketConst.SEND_TO_USER_AGENT, event);

        return null;
    }

    public abstract String getAction();


//    public String getAction () {
//        String className = this.getClass().getName();
//        String suffix = className.substring(className.lastIndexOf('.') + 1);
//        String action = suffix.substring(0, suffix.length() - 13);
//        return action.substring(0, 1).toLowerCase() + action.substring(1, action.length());
//    }

    public AgentService getAgentService() {
        return null;
    }

    public SimpMessagingTemplate getMessagingTemplate() {
        return null;
    }

}
