package com.freebird.wx.common.util;

import com.freebird.wx.common.http.BusinessResponse;
import com.freebird.wx.common.http.NetUtil;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

public class WeixinTemplateNotice {

	private static final String tempNoticeURL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

	private static final String TOPCOLOR = "#FF0000";

	private static final String WORDCOLOR = "#000000";

	private static String payment_notice_tempid = "-mYaMWSE1MJTWYqYfR-D9_lZsjawuE7bHOk80iGNP_I";

	private static String comment_notice_tempid = "Hf_4bs1ja0z3Vluf6kvx7r9Gl9A_ALcwlMvp3EKn5v4";

	private static String user_pay_notice_tempid = "spqreXMUPm5v7gF7piuxBqzQ53EUAwI9HdMcTNegPIA";

	public enum TemplateIDENUM {

		// 评论提示
		COMMENT_NOTICE_TIMPID(comment_notice_tempid),
		// 到款提示
		PAYMENT_NOTICE_TEMPID(payment_notice_tempid),
		// 支付提示
		USER_PAY_NOTICE_TEMPID(user_pay_notice_tempid);
		private String tempID;

		private TemplateIDENUM(String tempID) {
			this.tempID = tempID;
		}

		@Override
		public String toString() {
			return this.tempID;
		}
	}

	public class CommonNoticeBean {
		private String first;
		private String keyword1;
		private String keyword2;
		private String keyword3;
		private String remark;

		private String touser;
		private String template_id;
		private String url;

		public String getTouser() {
			return touser;
		}

		public void setTouser(String touser) {
			this.touser = touser;
		}

		public String getTemplate_id() {
			return template_id;
		}

		public void setTemplate_id(String template_id) {
			this.template_id = template_id;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getFirst() {
			return first;
		}

		public void setFirst(String first) {
			this.first = first;
		}

		public String getKeyword1() {
			return keyword1;
		}

		public void setKeyword1(String keyword1) {
			this.keyword1 = keyword1;
		}

		public String getKeyword2() {
			return keyword2;
		}

		public void setKeyword2(String keyword2) {
			this.keyword2 = keyword2;
		}

		public String getKeyword3() {
			return keyword3;
		}

		public void setKeyword3(String keyword3) {
			this.keyword3 = keyword3;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}
	}

	public static String sendCommonNotice(CommonNoticeBean bean,
			String access_token) {
		String url = tempNoticeURL.replace("ACCESS_TOKEN", access_token);
//		JSONObject json = HttpRequestUtil.httpsRequest(url, "POST",
//				packagingCommonNoticeParams(bean));
		BusinessResponse<Object> response = NetUtil.getInstance().post(url, packagingCommonNoticeParams(bean), MediaType.APPLICATION_JSON);
		String dataStr = new Gson().toJson(response.getData());
		return dataStr;
	}

	private static String packagingCommonNoticeParams(CommonNoticeBean bean) {
		Map<String, String> first = new HashMap<String, String>();
		first.put("value", bean.getFirst());
		first.put("color", WORDCOLOR);

		Map<String, String> keyword1 = new HashMap<String, String>();
		keyword1.put("value", bean.getKeyword1());
		keyword1.put("color", WORDCOLOR);

		Map<String, String> keyword2 = new HashMap<String, String>();
		keyword2.put("value", bean.getKeyword2());
		keyword2.put("color", WORDCOLOR);

		Map<String, String> keyword3 = new HashMap<String, String>();
		keyword3.put("value", bean.getKeyword3());
		keyword3.put("color", WORDCOLOR);

		Map<String, String> remark = new HashMap<String, String>();
		remark.put("value", bean.getRemark());
		remark.put("color", WORDCOLOR);

		Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
		data.put("first", first);
		data.put("keyword1", keyword1);
		data.put("keyword2", keyword2);
		data.put("keyword3", keyword3);
		data.put("remark", remark);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("touser", bean.getTouser());
		paramMap.put("template_id", bean.getTemplate_id());
		paramMap.put("topcolor", TOPCOLOR);
		if(StringUtils.isNotEmpty(bean.getUrl())){
			paramMap.put("url", bean.getUrl());
		}
		paramMap.put("data", data);
		return new Gson().toJson(paramMap);
	}

	public static void main(String[] args) {
		CommonNoticeBean bean = new WeixinTemplateNotice().new CommonNoticeBean();
		bean.setFirst("first");
		bean.setKeyword1("keyword1");
		bean.setKeyword2("keyword2");
		bean.setKeyword3("keyword3");
		bean.setRemark("remark");
		bean.setTouser("osTWmw4TstzvR1osvYmqOb_1EVAc");
		bean.setTemplate_id(TemplateIDENUM.USER_PAY_NOTICE_TEMPID.toString());
		bean.setUrl("www.baidu.com");
		String access_token = "jghu4CG1YvS3znwSUtPx0gfGJ-AMs_9l9N5GwygR14kcor04UkA77IQH_YbMUIiIVExb8ChOAw5k_Gpm4Z5pIBaoEzJ4jepct0qNWYZXu9tey9daitedX_Mse_OX2fKoWWXhADAAAB";
		System.out.println(WeixinTemplateNotice.sendCommonNotice(bean,
				access_token));
	}
}
