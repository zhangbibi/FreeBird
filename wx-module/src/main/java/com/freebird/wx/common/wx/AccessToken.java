package com.freebird.wx.common.wx;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 微信接口调用凭证access_token
 * 非网页授权access_token
 *
 * @author zhangyaping001
 */
public class AccessToken implements Serializable {

    private static final long serialVersionUID = 1L;
    private String access_token;
    private Long expires_in;
    private Long requestTime;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public Long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }

    /**
     * 判断token是否需要刷新
     *
     * @return
     */
    public boolean needRefresh() {
        return StringUtils.isEmpty(access_token) || timeout();
    }

    /**
     * 接口返回的超时时间前5分钟即视为超时
     *
     * @return
     */
    private boolean timeout() {
        if (requestTime == null || requestTime == 0) {
            return true;
        }

        long now = System.currentTimeMillis();
        return (now - requestTime) / 1000 >= expires_in - 5 * 60;
    }

    @Override
    public String toString() {
        return "Token [access_token=" + access_token + ", expires_in="
                + expires_in + ", requestTime=" + requestTime + "]";
    }

}
