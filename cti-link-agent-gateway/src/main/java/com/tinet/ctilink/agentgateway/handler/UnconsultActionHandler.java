package com.tinet.ctilink.agentgateway.handler;

import com.tinet.ctilink.agentgateway.inc.Action;
import org.springframework.stereotype.Component;

/**
 * 咨询接回，挂断被咨询方
 *
 * @author zoubo
 */
@Component
public class UnconsultActionHandler extends AbstractActionHandler {

    @Override
    public String getAction() {
        return Action.UNCONSULT;
    }

}
