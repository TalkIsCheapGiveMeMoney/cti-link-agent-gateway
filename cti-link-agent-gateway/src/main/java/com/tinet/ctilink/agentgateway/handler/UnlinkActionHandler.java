package com.tinet.ctilink.agentgateway.handler;

import com.tinet.ctilink.agentgateway.inc.Action;
import org.springframework.stereotype.Component;

/**
 * 挂断
 *
 * @author zoubo
 */
@Component
public class UnlinkActionHandler extends AbstractActionHandler {

    @Override
    public String getAction() {
        return Action.UNLINK;
    }

}
