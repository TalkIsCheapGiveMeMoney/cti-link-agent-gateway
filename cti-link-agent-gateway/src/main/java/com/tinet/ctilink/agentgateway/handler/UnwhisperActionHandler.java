package com.tinet.ctilink.agentgateway.handler;

import com.tinet.ctilink.agentgateway.inc.Action;
import org.springframework.stereotype.Component;

/**
 * 耳语挂断
 *
 * @author zoubo
 */
@Component
public class UnwhisperActionHandler extends AbstractActionHandler {

    @Override
    public String getAction() {
        return Action.UNWHISPER;
    }

}
