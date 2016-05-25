package com.tinet.ctilink.agentgateway.handler;

import com.tinet.ctilink.agentgateway.inc.Action;
import org.springframework.stereotype.Component;

/**
 * 三方挂断
 *
 * @author zoubo
 */
@Component
public class UnthreewayActionHandler extends AbstractActionHandler {

    @Override
    public String getAction() {
        return Action.UNTHREEWAY;
    }

}
