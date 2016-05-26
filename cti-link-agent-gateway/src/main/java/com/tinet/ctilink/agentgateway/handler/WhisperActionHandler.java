package com.tinet.ctilink.agentgateway.handler;

import com.tinet.ctilink.agentgateway.inc.Action;
import org.springframework.stereotype.Component;

/**
 * 耳语
 *
 * @author zoubo
 */
@Component
public class WhisperActionHandler extends AbstractActionHandler {

    @Override
    public String getAction() {
        return Action.WHISPER;
    }

}
