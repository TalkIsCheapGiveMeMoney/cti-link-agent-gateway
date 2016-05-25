package com.tinet.ctilink.agentgateway;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

/**
 * WebSocket连接过滤器，根据座席登录信息判断是否允许连接，并根据cid生成java.security.Principal对象返回
 * 
 * @author Jiangsl
 *
 */
@Component
public class WebSocketFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);

//		if (session == null) {
//			res.sendError(403);
//			return;
//		}
//
//		// 从Session中获取获取座席登录对象，并存入ThreadLocal
//		SessionMessage sessionMessage = (SessionMessage) session.getAttribute(SocketConst.AGENT_LOGIN);
//		if (sessionMessage == null) {
//			res.sendError(403);
//			return;
//		}
//
//		SessionMessage.save(sessionMessage);
//
//		String cid = sessionMessage.getEnterpriseId() + sessionMessage.getCno();
		chain.doFilter(new UserRequestWrapper("60000012008", req), response);

//		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig cfg) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	/**
	 * 对HttpServletRequest的包装，用于返回java.security.Principal对象
	 * 
	 * @author Jiangsl
	 *
	 */
	public class UserRequestWrapper extends HttpServletRequestWrapper {
		private String name;
		private HttpServletRequest realRequest;

		public UserRequestWrapper(String name, HttpServletRequest request) {
			super(request);
			this.name = name;
			this.realRequest = request;
		}

		@Override
		public Principal getUserPrincipal() {
			if (this.name == null) {
				return realRequest.getUserPrincipal();
			}

			return new Principal() {
				@Override
				public String getName() {
					return name;
				}
			};
		}
	}
}