package com.freebird.service;

import com.freebird.controller.WechatRequestController;
import com.freebird.wx.common.message.resp.TextMessage;
import com.freebird.wx.common.util.MessageUtil;
import com.freebird.wx.common.util.SignUtil;
import com.freebird.wx.common.wx.WeixinUtil;
import com.freebird.wx.common.wx.WeixinUtilFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Service
public class WechatRequestService {

    private static final Logger logger = LoggerFactory.getLogger(WechatRequestController.class);

    /**
     * 处理微信发来的请求
     */
    public String processRequest(HttpServletRequest request) {
        String respMessage = null;
        try {
            String respContent = "请求处理异常，请稍候尝试！";
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            String fromUserName = requestMap.get("FromUserName");// 发送方open_id
            String toUserName = requestMap.get("ToUserName");// 公众帐号
            String msgType = requestMap.get("MsgType");
            // 回复文本消息
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {// 文本消息
                if (MessageUtil.isQqFace((requestMap.get("Content")))) {// 接收到的是QQ表情
                    respContent = requestMap.get("Content");
                } else {
                    respContent = "嗯";
                }
            } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {// 图片消息
                respContent = "";
            } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {// 地理位置消息
                respContent = "";
            } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {// 链接消息
                respContent = "";
            } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {// 音频消息
                respContent = "";
            } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                String eventType = requestMap.get("Event");
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {// 订阅
                    respContent = "谢谢关注!\ue032 \n这是一个用来写收费内容的小工具\ue417 \n将你写的分享出去获得更多赞赏\ue106";
                } else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {// 取消订阅

                } else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {// 自定义菜单点击事件

                }
            }
            textMessage.setContent(respContent);
            respMessage = MessageUtil.textMessageToXml(textMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMessage;
    }

    public Map<String, Object> getUserInfoByWebAuth(HttpServletRequest request) {
        WeixinUtil util = WeixinUtilFactory.getInstance();
        String openid = null;
        String access_token = null;
        openid = ;
        if (StringUtils.isEmpty(openid)) {
            logger.info("WechatRequestServiceImpl.getUserInfoByWebAuth openid is null");
            return null;
        }
        access_token = util.getWebAuthAccessTokenFromCache(openid);
        if (StringUtils.isEmpty(access_token)) {
            logger.info("WechatRequestServiceImpl.getUserInfoByWebAuth redis auth_access_token is null");
            return null;
        }
        logger.info("WechatRequestServiceImpl.getUserInfoByWebAuth: openid = " + openid + ". auth_access_token = " + access_token);
        Map<String, Object> baseUserInfo = util.getBaseUserInfoByAuth(access_token, openid);
        if (baseUserInfo == null) {
            access_token = util.getWebAuthAccessTokenFromCache(openid);
            baseUserInfo = util.getBaseUserInfoByAuth(access_token, openid);
        }
        return baseUserInfo;
    }

    public String getOpenidByWebAuth(HttpServletRequest request) {
        WeixinUtil util = WeixinUtilFactory.getInstance();
        String openid = util.getOpenidFromCache(request);
        return openid;
    }

    public Map<String, String> getJSSDKSignature(String url) {
        WeixinUtil util = WeixinUtilFactory.getInstance();
        String token = util.getAccessToken();
        String ticket = util.getTicket();
        return SignUtil.signForTicket(ticket, token, url);
    }
}
