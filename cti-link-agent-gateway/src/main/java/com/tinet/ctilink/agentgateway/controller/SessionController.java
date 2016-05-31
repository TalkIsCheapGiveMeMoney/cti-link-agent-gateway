package com.tinet.ctilink.agentgateway.controller;

import com.tinet.ctilink.agentgateway.inc.SocketConst;
import com.tinet.ctilink.agentgateway.util.SessionFactory;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.inc.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

/**
 * @author fengwei //
 * @date 16/5/26 14:27
 */
@Controller
@RequestMapping("/session")
public class SessionController {

    @Autowired
    private RedisService redisService;

    @RequestMapping("/{cid}")
    @ResponseBody
    public String getSession(@PathVariable("cid") String cid) throws Exception {
        String sessionId = SessionFactory.getSessionId(cid);
        System.out.println(sessionId);
        String key = String.format(SocketConst.AGENT_GATEWAY_SESSION_ID, sessionId);

        // 如果sessionId已存在?

        // 存入redis
        redisService.set(Const.REDIS_DB_SESSION_INDEX, key, cid);
        redisService.expire(Const.REDIS_DB_SESSION_INDEX, key, 10, TimeUnit.SECONDS);

        return sessionId;
    }
}
