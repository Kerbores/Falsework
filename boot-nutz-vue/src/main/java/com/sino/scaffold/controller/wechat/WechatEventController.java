package com.sino.scaffold.controller.wechat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.lang.Lang;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.Param;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.impl.AbstractWxHandler;
import org.nutz.weixin.impl.WxApi2Impl;
import org.nutz.weixin.repo.com.qq.weixin.mp.aes.AesException;
import org.nutz.weixin.repo.com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.nutz.weixin.spi.WxHandler;
import org.nutz.weixin.spi.WxResp;
import org.nutz.weixin.util.Wxs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.View;

import com.sino.scaffold.BootNutzVueApplication;
import com.sino.scaffold.bean.qa.Nutzer;
import com.sino.scaffold.config.wechat.NutzViewWrapper;
import com.sino.scaffold.controller.base.BaseController;
import com.sino.scaffold.service.qa.NutzerService;
import com.sino.scaffold.utils.Result;

import io.swagger.annotations.ApiOperation;

/**
 * @author kerbores
 *
 */
@Controller
public class WechatEventController extends BaseController {

	@Autowired
	WxApi2Impl api;

	@Autowired
	NutzerService nutzerService;

	{
		Wxs.enableDevMode();
	}

	protected WxHandler wxHandler = new AbstractWxHandler() {

		@Override
		public WXBizMsgCrypt getMsgCrypt() {
			try {
				return new WXBizMsgCrypt(api.getToken(), api.getEncodingAesKey(), api.getAppid());
			} catch (AesException e) {
				logger.debug(e);
				throw new RuntimeException(e);
			}
		}

		@Override
		public boolean check(String signature, String timestamp, String nonce, String key) {
			return Wxs.check(api.getToken(), signature, timestamp, nonce);
		}

		@Override
		public WxOutMsg eventSubscribe(WxInMsg msg) {
			WxResp resp = api.user_info(msg.getFromUserName(), "zh_CN");
			Nutzer nutzer = nutzerService.fetch(Cnd.where("openid", "=", msg.getFromUserName()));
			if (nutzer == null) {
				nutzer = new Nutzer();
				nutzer.setOpenid(msg.getFromUserName());
				String nickName = resp.getString("nickname").replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "");
				nutzer.setCity(resp.getString("city"));
				nutzer.setCountry(resp.getString("country"));
				nutzer.setProvince(resp.getString("province"));
				nutzer.setNickName(nickName);
				nutzer.setHeadImgUrl(resp.getString("headimgurl"));
				nutzerService.save(nutzer);
				return Wxs.respText(null, "欢迎关注!");
			} else {
				return Wxs.respText(null, "欢迎回来!");
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.nutz.weixin.impl.AbstractWxHandler#defaultMsg(org.nutz.weixin.bean.
		 * WxInMsg)
		 */
		@Override
		public WxOutMsg defaultMsg(WxInMsg msg) {
			return Wxs.respText("hello!");
		}
	};

	@RequestMapping(value = { "wechat", "wechat/?" })
	public View msgIn(String key, HttpServletRequest req) throws IOException {
		return new NutzViewWrapper(Wxs.handle(wxHandler, req, key));
	}

	@GetMapping("bind")
	@ApiOperation("绑定用户")
	public @ResponseBody Result bind(@Param("token") String token, @SessionAttribute(BootNutzVueApplication.NUTZ_USER_KEY) Nutzer nutzer) {
		Response response = Http.post2("https://nutz.cn/yvr/api/v1/accesstoken", NutMap.NEW().addv("accesstoken", token), 5000);
		if (response.isOK()) {
			NutMap data = Lang.map(response.getContent());
			if (data.getBoolean("success")) {
				// 更新信息到NUTZER
				Response r1 = Http.get("https://nutz.cn/yvr/api/v1/user/" + data.getString("loginname"));
				NutMap userInfo = Lang.map(r1.getContent());
				nutzer.setAccessToken(token);
				nutzer.setLoginName(userInfo.getAs("data", NutMap.class).getString("loginname"));
				nutzer.setAvatarUrl(userInfo.getAs("data", NutMap.class).getString("avatar_url"));
				nutzerService.update(nutzer);
			}
			return Result.success().addData(data);
		}
		return Result.fail("token不正确!");
	}
}
