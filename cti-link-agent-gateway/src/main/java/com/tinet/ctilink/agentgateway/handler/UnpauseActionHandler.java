package com.tinet.ctilink.agentgateway.handler;

import com.tinet.ctilink.agentgateway.inc.Action;
import org.springframework.stereotype.Component;

/**
 * 置闲
 *
 * @author zoubo
 */
@Component
public class UnpauseActionHandler extends AbstractActionHandler {

    @Override
    public String getAction() {
        return Action.UNPAUSE;
    }

}
