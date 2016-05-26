package com.tinet.ctilink.agentgateway.handler;

import com.tinet.ctilink.agentgateway.inc.Action;
import org.springframework.stereotype.Component;

/**
 * 转移
 *
 * @author zoubo
 */
@Component
public class TransferActionHandler extends AbstractActionHandler {

    @Override
    public String getAction() {
        return Action.TRANSFER;
    }

}
