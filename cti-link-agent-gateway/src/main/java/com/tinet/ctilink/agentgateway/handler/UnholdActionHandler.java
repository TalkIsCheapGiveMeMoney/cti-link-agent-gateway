package com.tinet.ctilink.agentgateway.handler;

import com.tinet.ctilink.agentgateway.inc.Action;
import org.springframework.stereotype.Component;

/**
 * 保持挂断
 *
 * @author zoubo
 */
@Component
public class UnholdActionHandler extends AbstractActionHandler {

    @Override
    public String getAction() {
        return Action.UNHOLD;
    }

}
