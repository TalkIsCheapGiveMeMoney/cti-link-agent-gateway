package com.tinet.ctilink.agentgateway.handler;

import com.tinet.ctilink.agentgateway.inc.Action;
import org.springframework.stereotype.Component;

/**
 * 三方邀请
 *
 * @author zoubo
 */
@Component
public class ThreewayActionHandler extends AbstractActionHandler {

    @Override
    public String getAction() {
        return Action.THREEWAY;
    }

}
