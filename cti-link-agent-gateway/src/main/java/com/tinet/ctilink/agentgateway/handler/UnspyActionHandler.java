package com.tinet.ctilink.agentgateway.handler;

import com.tinet.ctilink.agentgateway.inc.Action;
import org.springframework.stereotype.Component;

/**
 * 监听挂断
 *
 * @author zoubo
 */
@Component
public class UnspyActionHandler extends AbstractActionHandler {

    @Override
    public String getAction() {
        return Action.UNSPY;
    }

}
