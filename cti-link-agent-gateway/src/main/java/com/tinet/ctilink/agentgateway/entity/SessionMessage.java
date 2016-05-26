package com.tinet.ctilink.agentgateway.entity;

import java.io.Serializable;

/**
 * 座席用户登录对象，保存至Session，以验证用户登录状态
 *
 * @author Jiangsl
 */
public class SessionMessage implements Serializable {

    private static final long serialVersionUID = -6488250855794385842L;

    private static final ThreadLocal<SessionMessage> threadLocal = new ThreadLocal<SessionMessage>();

    private Integer enterpriseId;
    private String cno;
    private Integer clientId;
    private Integer power;

    /**
     * @return the enterpriseId
     */
    public Integer getEnterpriseId() {
        return enterpriseId;
    }

    /**
     * @param reEnterpriseId the enterpriseId to set
     */
    public void setEnterpriseId(Integer reEnterpriseId) {
        this.enterpriseId = reEnterpriseId;
    }

    /**
     * @return the cno
     */
    public String getCno() {
        return cno;
    }

    /**
     * @param cno the cno to set
     */
    public void setCno(String cno) {
        this.cno = cno;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the power
     */
    public Integer getPower() {
        return power;
    }

    /**
     * @param power the power to set
     */
    public void setPower(Integer power) {
        this.power = power;
    }

    /**
     * 获取当前线程绑定的座席用户登录对象
     *
     * @return
     */
    public static SessionMessage current() {
        return threadLocal.get();
    }

    /**
     * 将座席用户登录对象绑定到当前线程
     *
     * @param sessionMessage
     */
    public static void save(SessionMessage sessionMessage) {
        threadLocal.set(sessionMessage);
    }
}
