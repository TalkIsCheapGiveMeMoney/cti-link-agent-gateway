package com.tinet.ctilink.agentgateway.inc;


/**
 * @author fengwei //
 * @date 16/5/18 16:38
 */
public class ErrorMsg {

    public static final int ERRORCODE_BAD_USERNAME_PASSWORD = 1; // 用户名密码错误
    public static final int ERRORCODE_BAD_CONNECTION = 2; // 连接已中断
    public static final int ERRORCODE_TIMEOUT = 3; // 超时
    public static final int ERRORCODE_NO_QUEUE = 4; // 座席没有在队列中
    public static final int ERRORCODE_NO_CHANNEL = 5; // 咨询失败，座席不在通话中
    public static final int ERRORCODE_BAD_PARAM = 6; // 参数错误
    public static final int ERRORCODE_BAD_BIND_TEL = 7; // 绑定的电话错误
    public static final int ERRORCODE_BAD_PICKUP = 8; // 抢线失败
    public static final int ERRORCODE_BAD_REFUSE = 9; // 拒接失败
    public static final int ERRORCODR_BAD_CANCEL = 10; // 取消外呼失败
    public static final int ERRORCODR_BAD_PAREM = 10; // 咨询或转移或三方通话参数错误
    public static final int ERRORCODR_BAD_DEVICE_STATUS = 11; // 监听耳语强插、外呼等操作时当前状态不为空闲
    public static final int ERRORCODE_NO_SUCH_CNO = 12; // 没有这个坐席号
    public static final int ERRORCODE_BAD_TEL = 13; // 外呼号码格式错误
    public static final int ERRORCODE_BAD_CLID = 14; // 外显号码设置错误
    public static final int ERRORCODE_NOT_ONLINE = 15; // 座席不在线
    public static final int ERRORCODE_NOT_LOGIN = 16; // 座席未登录平台
    public static final int ERRORCODE_ALREADY_ONHOLD = 17; // 座席已经是保持状态
    public static final int ERRORCODE_NOT_ONHOLD = 18; // 座席不在保持状态
    public static final int ERRORCODE_NOT_LOGIN_FRONTEND = 19; // 座席是后台登录
    public static final int ERRORCODE_PERMISSION_DENIED = 20; // 没有权限
    public static final int ERRORCODE_ALREADY_MONITORED = 21; // 座席已经被监控
    public static final int ERRORCODE_CANNOT_MONITORE = 22; // 班长不能监控正在做监控的其他班长座席
    public static final int ERRORCODE_DEFAULT_PAUSE = 23;//座席默认自定义置忙状态配置错误
    public static final int ERRORCODE_PAUSE_OR_BUSY = 24; // 咨询失败，座席置忙或者busy
    public static final int ERRORCODE_BLACK_TEL = 25; // 号码为黑名单
    public static final int ERRORCODE_PERMISSION_OUTCALL = 26; // 座席没有外呼权限
    public static final int ERRORCODE_NO_MONEY = 27; // 余额不足
    public static final int ERRORCODE_NO_ROUTER = 28; // 没有路由
    public static final int ERRORCODE_CLIENT_WEB_LIMIT = 29; // 座席在线数超过最大并发限制
    public static final int ERRORCODE_AGENT_LOGIN = 30; // 此座席工号已经绑定别的电话号码或分机登录
    public static final int ERRORCODE_AGENT_BUDY = 31; //座席busy 不能更改绑定电话
    public static final int ERRORCODE_AGENT_OFFLINE = 34;//错误的断线重连，座席已经下线
    public static final int ERRORCODE_AGENT_NOT_ACTIVE = 32;//座席未激活 不能登录电话
    public static final int ERRORCODE_AGENT_IS_OUTCALLING = 33; // 座席正在使用外呼
    public static final int ERRORCODE_DEAD_NUMBER = 35; // 外呼号码可能是空号
    public static final int ERRORCODE_CALL_ALREADY = 36; // 已经被执行
    public static final int ERRORCODE_CALL_BY_OTHER_NOW = 37; // 正在被别的坐席执行
    public static final int ERRORCODE_BLACK_IP = 38; // IP黑名单
    public static final int ERRORCODE_AGENT_WEB_LOGIN_FORBIDDEN = 39; // 非电脑座席
    public static final int ERRORCODE_EXCEPTION = 999; // 发生异常

    	/*
	 * 后台登陆验证电话号码时候用到的常量
	 */
    /**check in :NUMBER_FORMAT_ERROR 号码格式不正确*/
    public static final String NUMBER_FORMAT_ERROR="1";

    /**check in :NUMBER_IN_USE 号码已经被使用*/
    public static final String NUMBER_IN_USE="2";

    /**check in :NUMBER_NOT_EXIST 分机号码不存在*/
    public static final String NUMBER_NOT_EXIST="3";

    public static final String SUCCESS="0";

    public static final String NUMBER_NOT_ROUTED = "4";
}
