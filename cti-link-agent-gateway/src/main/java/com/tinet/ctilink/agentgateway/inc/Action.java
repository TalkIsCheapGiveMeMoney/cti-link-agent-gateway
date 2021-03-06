package com.tinet.ctilink.agentgateway.inc;

import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fengwei //
 * @date 16/5/18 18:02
 */
public class Action {

    public static final String BARGE = "barge";

    public static final String CHANGE_BIND_TEL = "changeBindTel";

    public static final String CONSULT = "consult";

    public static final String CONSULT_CANCEL = "consultCancel";

    public static final String CONSULT_THREEWAY = "consultThreeway";

    public static final String CONSULT_TRANSFER = "consultTransfer";

    public static final String DIRECT_CALL_START = "directCallStart";

    public static final String DISCONNECT = "disconnect";

    public static final String HOLD = "hold";

    public static final String INTERACT = "interact";

    public static final String INVESTIGATION = "investigation";

    public static final String LOGIN = "login";

    public static final String LOGOUT = "logout";

    public static final String MUTE = "mute";

    public static final String PAUSE = "pause";

    public static final String PICKUP = "pickup";

    public static final String PING = "ping";

    public static final String PREVIEW_OUTCALL = "previewOutcall";

    public static final String PREVIEW_OUTCALL_CANCEL = "previewOutcallCancel";

    public static final String QUEUE_STATUS = "queueStatus";

    public static final String REFUSE = "refuse";

    public static final String SET_PAUSE = "setPause";

    public static final String SET_UNPAUSE = "setUnpause";

    public static final String SPY = "spy";

    public static final String THREEWAY = "threeway";

    public static final String TRANSFER = "transfer";

    public static final String UNCONSULT = "unconsult";

    public static final String UNHOLD = "unhold";

    public static final String UNLINK = "unlink";

    public static final String UNPAUSE = "unpause";

    public static final String UNSPY = "unspy";

    public static final String UNTHREEWAY = "unthreeway";

    public static final String UNWHISPER = "unwhisper";

    public static final String WHISPER = "whisper";



    static public Map<String,Object> createFailResponse(Map<String, Object> msg , int code , String resMsg){
        Map<String,Object> response = new HashMap<>();
        response.put(Variable.VARIABLE_TYPE, "response");
        String resType = MapUtils.getString(msg, Variable.VARIABLE_TYPE);
        String utid = MapUtils.getString(msg, Variable.VARIABLE_UTID);
        response.put(Variable.VARIABLE_CODE, "" + code);
        response.put(Variable.VARIABLE_MSG, resMsg);
        if (resType != null) {
            response.put(Variable.VARIABLE_RES_TYPE, resType);
        }
        if(utid != null){
            response.put(Variable.VARIABLE_UTID, utid);
        }
        return response;
    }

    static public Map<String,Object> createSuccessResponse(Map<String, Object> msg){
        Map<String,Object> response = new HashMap<>();
        response.put(Variable.VARIABLE_TYPE, "response");
        String resType = MapUtils.getString(msg, Variable.VARIABLE_TYPE);
        String utid = MapUtils.getString(msg, Variable.VARIABLE_UTID);
        response.put(Variable.VARIABLE_CODE, "0");
        response.put(Variable.VARIABLE_MSG, "ok");
        if (resType != null) {
            response.put(Variable.VARIABLE_RES_TYPE, resType);
        }
        if(utid != null){
            response.put(Variable.VARIABLE_UTID, utid);
        }
        return response;
    }
}
