package com.freebird.wx.common.wxpay;

import com.freebird.wx.common.http.BusinessResponse;
import com.freebird.wx.common.http.NetUtil;
import com.freebird.wx.common.http.WechatPayHttpClientUtil;
import com.freebird.wx.common.http.WeixinHttpURLSet;
import com.freebird.wx.common.util.MessageUtil;
import com.freebird.wx.common.util.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * 微信支付工具类
 *
 * @author zhangyaping
 */
public class WxpayUtil {

    private static final Logger logger = LoggerFactory.getLogger(WxpayUtil.class);

    /**
     * 微信支付-统一下单接口
     *
     * @param params
     * @return
     */
    public static Map<String, String> commonOrder(CommonOrderParams params) {
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", params.getAppid());
        parameters.put("mch_id", params.getMch_id());
        parameters.put("nonce_str", params.getNonce_str());
        parameters.put("body", params.getBody());
        parameters.put("out_trade_no", params.getOut_trade_no());
        parameters.put("total_fee", params.getTotal_fee());
        parameters.put("spbill_create_ip", params.getSpbill_create_ip());
        parameters.put("notify_url", params.getNotify_url());
        parameters.put("trade_type", params.getTrade_type());
        parameters.put("openid", params.getOpenid());
        parameters.put("time_start", params.getTime_start());
        parameters.put("time_expire", params.getTime_expire());
        String sign = SignUtil.createSign("UTF-8", parameters);
        parameters.put("sign", sign);
        String requestXML = MessageUtil.commonOrderParamsToXml(parameters);
        logger.info("WxPay commonOrder requestXML: " + requestXML);
        Map<String, String> resultMap = new HashMap<String, String>();
        BusinessResponse<Object> response = NetUtil.getInstance().post(WeixinHttpURLSet.WXPAY_COMMON_ORDER, requestXML, MediaType.APPLICATION_FORM_URLENCODED);
        try {
            if (response.getRt_code() == BusinessResponse.RESPONSE_OK) {
                InputStream in = new ByteArrayInputStream(((String) (response.getData())).getBytes("UTF-8"));
                resultMap = MessageUtil.parseXmlFromStream(in);
                logger.info("commonOrder resultMap: " + resultMap);
            }
        } catch (Exception e) {
            logger.error("WxPay commonOrder error. " + e);
        }
        return resultMap;
    }

    /**
     * 微信支付-企业向用户付款接口
     *
     * @param params
     * @return
     */
    public static Map<String, String> companyPay(CompanyPayParams params) {
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("mch_appid", params.getMch_appid());
        parameters.put("mchid", params.getMchid());
        parameters.put("nonce_str", params.getNonce_str());
        parameters.put("partner_trade_no", params.getPartner_trade_no());
        parameters.put("openid", params.getOpenid());
        parameters.put("check_name", params.getCheck_name());
//		parameters.put("re_user_name", params.getRe_user_name());
        parameters.put("amount", params.getAmount());
        parameters.put("desc", params.getDesc());
        parameters.put("spbill_create_ip", params.getSpbill_create_ip());
        String sign = SignUtil.createSign("UTF-8", parameters);
        parameters.put("sign", sign);
        String requestXML = MessageUtil.commonOrderParamsToXml(parameters);
        logger.info("companyPay requestXML: " + requestXML);
//		String result = HttpRequestUtil.httpsXMLRequest(WeixinHttpURLSet.WXPAY_COMPANY_PAYMENT, "POST", requestXML);
//		String result = HttpRequestUtil.httpsP12Post(WeixinHttpURLSet.WXPAY_COMPANY_PAYMENT, requestXML);

        String result = WechatPayHttpClientUtil.getInstance().httpsP12Post(WeixinHttpURLSet.WXPAY_COMPANY_PAYMENT, requestXML);

        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            InputStream in = new ByteArrayInputStream(result.getBytes("UTF-8"));
            resultMap = MessageUtil.parseXmlFromStream(in);
            logger.info("companyPay resultMap: " + resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }


    /**
     * 微信支付-查询订单接口
     *
     * @param params
     * @return
     */
    public static Map<String, String> queryOrder(QueryCloseOrderParams params) {
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", params.getAppid());
        parameters.put("mch_id", params.getMch_id());
        parameters.put("nonce_str", params.getNonce_str());
        parameters.put("out_trade_no", params.getOut_trade_no());
        String sign = SignUtil.createSign("UTF-8", parameters);
        parameters.put("sign", sign);
        String requestXML = MessageUtil.commonOrderParamsToXml(parameters);
        logger.info("WxPay queryOrder requestXML: " + requestXML);
        Map<String, String> resultMap = new HashMap<String, String>();
        BusinessResponse<Object> response = NetUtil.getInstance().post(WeixinHttpURLSet.WXPAY_QUERY_ORDER, requestXML, MediaType.APPLICATION_FORM_URLENCODED);
        try {
            if (response.getRt_code() == BusinessResponse.RESPONSE_OK) {
                InputStream in = new ByteArrayInputStream(((String) (response.getData())).getBytes("UTF-8"));
                resultMap = MessageUtil.parseXmlFromStream(in);
                logger.info("queryOrder resultMap: " + resultMap);
            }
        } catch (Exception e) {
            logger.error("WxPay queryOrder error. " + e);
        }
        return resultMap;
    }

    public static Map<String, String> closeOrder(QueryCloseOrderParams queryOrderParams) {
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", queryOrderParams.getAppid());
        parameters.put("mch_id", queryOrderParams.getMch_id());
        parameters.put("nonce_str", queryOrderParams.getNonce_str());
        parameters.put("out_trade_no", queryOrderParams.getOut_trade_no());
        String sign = SignUtil.createSign("UTF-8", parameters);
        parameters.put("sign", sign);
        String requestXML = MessageUtil.commonOrderParamsToXml(parameters);
        logger.info("WxPay closeOrder requestXML: " + requestXML);
        Map<String, String> resultMap = new HashMap<String, String>();
        BusinessResponse<Object> response = NetUtil.getInstance().post(WeixinHttpURLSet.WXPAY_CLOSE_ORDER, requestXML, MediaType.APPLICATION_FORM_URLENCODED);
        try {
            if (response.getRt_code() == BusinessResponse.RESPONSE_OK) {
                InputStream in = new ByteArrayInputStream(((String) (response.getData())).getBytes("UTF-8"));
                resultMap = MessageUtil.parseXmlFromStream(in);
                logger.info("closeOrder resultMap: " + resultMap);
            }
        } catch (Exception e) {
            logger.error("WxPay closeOrder error. " + e);
        }
        return resultMap;
    }
}
