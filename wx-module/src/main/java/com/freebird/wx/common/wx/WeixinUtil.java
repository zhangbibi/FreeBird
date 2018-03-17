package com.freebird.wx.common.wx;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * 调用微信接口工具类 调用新接口时在此接口中新增方法
 * WeixinUtilImpl中实现,如果需要本地调试需要实现WeixinUtilImplMock
 * @author zhangyaping001
 */
public interface WeixinUtil {

	/**
	 * 获取Appid
	 * @return
	 */
	String getAppid();
	/**
	 * 获取secret
	 * @return
	 */
	String getSecret();
	/**
	 * 网页授权方式通过code获取openId
	 * @param code
	 * @return
	 */
	WebAuthAccessToken getWebAccessTokenByCode(String code);
	/**
	 * 获取微信接口调用凭证access_token
	 * @return
	 */
	public String getAccessToken() ;
	/**
	 * 获取网页授权jsapi_ticket
	 * @return
	 */
	public String getTicket();
	/**
	 * 请求经过了网页授权后openid会保存在session中缓存起来
	 */
	public String getOpenidFromCache(HttpServletRequest httpRequest);
	/**
	 * 请求经过了网页授权后access_token会保存在redis中
	 */
	public String getWebAuthAccessTokenFromCache(String openid);
	/**
	 * 通过网页授权access_token和openid请求用户基本信息
	 * @return
	 */
	public Map<String, Object> getBaseUserInfoByAuth(String auth_access_token, String openid);


}
