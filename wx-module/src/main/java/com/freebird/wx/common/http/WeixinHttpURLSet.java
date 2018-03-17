package com.freebird.wx.common.http;


/**
 * 所有微信接口url集合 参数使用大写字母 使用时使用replace成实参
 * @author zhangyaping001
 */
public class WeixinHttpURLSet {
	/**
	 * 获取普通access_token(接口调用凭证)
	 */
	public static String GET_ACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
	/**
	 * 通过code获取网页授权access_token(非普通access_token)
	 */
	public static String GET_AUTHORIZE_ACCESSTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

	/**
	 * 通过refresh_token刷新access_token(非普通access_token)
	 */
	public static String GET_ACCESSTOKEN_BY_REFRESHTOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";

	/**
	 * 通过网页授权access_token获取用户基本信息
	 */
	public static String GET_USERINFO_BY_AUTHTOKEN = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	/**
	 * 通过openid获取用户详细信息
	 */
	public static String GET＿DETAILUSERINFO＿BY＿OPENID = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS＿TOKEN&openid=OPENID&lang=zh_CN";
	/**
	 * 通过access_token获取ticket
	 */
	public static String GET_TICKET_BY_ACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	/**
	 * 网页授权页面地址(snsapi_userinfo方式)
	 */
	public static String WX_AUTH_URL="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
	/**
	 * 微信支付统一下单接口
	 */
	public static String WXPAY_COMMON_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/**
	 * 微信支付-企业付款接口
	 */
	public static String WXPAY_COMPANY_PAYMENT = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

	/**
	 * 微信支付-查询支付订单
	 */
	public static String WXPAY_QUERY_ORDER = "https://api.mch.weixin.qq.com/pay/orderquery";

	/**
	 * 微信支付-关闭支付订单
	 */
	public static String WXPAY_CLOSE_ORDER = "https://api.mch.weixin.qq.com/pay/closeorder";

}
