package com.freebird.controller;

import com.freebird.service.WechatRequestService;
import com.freebird.wx.common.util.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyaping on 17/8/6.
 */

@RestController
@RequestMapping(value = "/dev")
public class WechatRequestController {

    private static final Logger logger = LoggerFactory.getLogger(WechatRequestController.class);

    @Autowired
    private WechatRequestService wechatRequestService;


    @RequestMapping(value = "/validateWxDev", method = RequestMethod.GET)
    public
    @ResponseBody
    String validateWxDev(HttpServletRequest request) {
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            return echostr;
        } else {
            return "";
        }
    }

    @RequestMapping(value = "validateWxDev", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String processWxRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 调用核心业务类接收消息、处理消息
        String respMessage = wechatRequestService.processRequest(request);
        // 响应消息
        return respMessage;
    }

    @RequestMapping("/getUserInfoByWebAuth")
    public Map getUserInfoByWebAuth(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> baseUserInfo = wechatRequestService.getUserInfoByWebAuth(request);
        logger.info("getUserInfoByWebAuth userinfo:" + baseUserInfo);
        if (baseUserInfo == null) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("err_code", "000");
            map.put("err_msg", "网页未授权或已超时");
//            return ResponseUtil.outputJsonResponse(request, response, map);
            return map;
        }
        return baseUserInfo;
    }

    @RequestMapping("/getOpenidByWebAuth")
    public Map getOpenidByWebAuth(HttpServletRequest request, HttpServletResponse response) {
        String openid = wechatRequestService.getOpenidByWebAuth(request);
        logger.info("getUserInfoByWebAuth openid:" + openid);
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isEmpty(openid)) {
            map.put("err_code", "000");
            map.put("err_msg", "网页未授权或已超时");
//            return ResponseUtil.outputJsonResponse(request, response, map);
            return map;
        }
        map.put("openid", openid);
//        return ResponseUtil.outputJsonResponse(request, response, map);
        return map;
    }

    @RequestMapping("/getJSSDKSignature")
    public Map<String, String> getJSSDKSignature(HttpServletRequest req, HttpServletResponse resp) {
        String url = req.getParameter("currentURL");
        Map<String, String> returnMap = wechatRequestService.getJSSDKSignature(url);
        return returnMap;
    }

}
