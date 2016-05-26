package com.tinet.ctilink.agentgateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.inc.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author fengwei //
 * @date 16/5/26 14:27
 */
@Controller
@RequestMapping("/session")
public class SessionController {
    private static final String AGENT_GATEWAY_SESSION_ID = "agent_gateway_session_%s";

    @Autowired
    private RedisService redisService;

    private ObjectMapper mapper = new ObjectMapper();


    @RequestMapping("/{cid}")
    @ResponseBody
    public String getSession(@PathVariable("cid") Long cid, HttpServletRequest request) throws Exception {
//        Session session = SessionFactory.createSession(cid);
//        String sessionId = session.getKey();
//        String key = String.format(AGENT_GATEWAY_SESSION_ID, sessionId);
//
//        //存入redis
//        redisService.set(Const.REDIS_DB_SESSION_INDEX, key, cid);
//        redisService.expire(Const.REDIS_DB_SESSION_INDEX, key, 30, TimeUnit.SECONDS);
//
//        Map<String,String> r = new HashMap<String, String>();
//        r.put("sessionId", sessionId);
//        return request.getParameter("callback") +"('"+mapper.writeValueAsString(r)+"')";
        return null;
    }
}
