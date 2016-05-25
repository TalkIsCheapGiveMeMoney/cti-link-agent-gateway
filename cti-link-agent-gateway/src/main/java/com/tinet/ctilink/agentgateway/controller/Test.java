package com.tinet.ctilink.agentgateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * @author fengwei //
 * @date 16/5/24 14:38
 */
@Controller
@RequestMapping("agent")
public class Test {

    @RequestMapping("info")
    public String xxx(HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "http://cti-link.com:9090");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return "";
    }
}
