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

import org.apache.commons.lang3.StringUtils;
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

		System.out.println(req.getRequestURI());
		if (req.getRequestURI().equals("/agent/info")
				|| req.getRequestURI().startsWith("/session/")) {
			chain.doFilter(request, response);
			return;
		}

//		String sessionId = req.getParameter("sessionId");
//		if (StringUtils.isEmpty(sessionId)) {
//			res.sendError(403);
//			return;
//		}
//
//		Session session = SessionFactory.build(sessionId);
//		if (session == null || System.currentTimeMillis() - session.getTick() > 10000) {
//			res.sendError(403);
//			return;
//		}
		String cid = req.getParameter("cid");
		if (StringUtils.isEmpty(cid)) {
			res.sendError(403);
			return;
		}
		chain.doFilter(new UserRequestWrapper(cid, req), response);
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