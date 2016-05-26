package com.tinet.ctilink.agentgateway.inc;

public class SocketConst {
	/** 本机唯一标识字符串，系统重启后改变 */
	public static final String LOCALHOST_UNIQUE_ID = String.valueOf(System.currentTimeMillis());
	public static final String WEBSOCKET_MESSAGE_CHANNEL = "WebSocketMessages";
	public static final String WEBSOCKET_MESSAGE = "/messages";
	public static final String VAR_ENTERPRISE_ID = "enterpriseId";
	public static final String VAR_QUEUE_LIST = "queueList";
	public static final String VAR_CNO = "cno";
	public static final String VAR_USID = "usid";
	public static final String VAR_QIDS = "qids";
	public static final String VAR_IP = "ip";
	public static final String VAR_LOGIN_KEY = "loginKey";
	public static final String VAR_USER = "user";
	public static final String ERROR_MSG="errorMsg";
	public static final String HTTP_SESSION_ID="HTTP.SESSION.ID";
	public static final String SIMP_SUBSCRIPTION_ID="simpSubscriptionId";
	public static final String SEND_TO_USER_AGENT ="/agent";


	public static final String AGENT_LOGIN = "agent_login";
}
