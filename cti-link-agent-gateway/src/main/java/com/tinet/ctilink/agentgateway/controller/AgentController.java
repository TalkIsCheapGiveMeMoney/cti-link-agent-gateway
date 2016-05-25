package com.tinet.ctilink.agentgateway.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tinet.ctilink.agentgateway.WebSocketActionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/agent")
public class AgentController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private List<WebSocketActionHandler> handlerList;

    private Map<String, WebSocketActionHandler> handlerMap;


    /**
     * 处理座席WebSocket动作
     *
     * @param action
     * @param principal
     * @param content
     */
    @MessageMapping("/handle/{action}")
    public void handle(@DestinationVariable String action, Principal principal, Map<String, Object> content) {
        String result = this.getHandler(action).handle(principal.getName(), content);
        if (result != null)
            logger.error(result);
    }

    /**
     * 根据Action获取对应的处理器
     *
     * @param action
     * @return
     */
    private WebSocketActionHandler getHandler(String action) {
        if (handlerMap == null) {
            handlerMap = new HashMap<String, WebSocketActionHandler>();
            for (WebSocketActionHandler handler : handlerList) {
                handlerMap.put(handler.getAction(), handler);
            }
        }

        WebSocketActionHandler handler = handlerMap.get(action);

        if (handler == null) {
            logger.error("AgentActionHandler for AmiAction: " + action + " not found.");
            throw new UnsupportedOperationException("AgentActionHandler for AmiAction: " + action + " not found.");
        }

        return handler;
    }
}