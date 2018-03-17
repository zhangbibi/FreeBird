package com.freebird.common.wx;

import com.threeguys.common.util.PropertiesUtils;

/**
 * 获取微信接口调用工具类的工厂方法
 * 进行本地调试返回mock()
 * @author zhangyaping001
 */
public class WeixinUtilFactory {

	private static WeixinUtil utilInstance = new WeixinUtilImpl(PropertiesUtils.getPropertyValues("wx.appId"), PropertiesUtils.getPropertyValues("wx.appsecret"));

	public static WeixinUtil getInstance() {
		return utilInstance;
	}

}
