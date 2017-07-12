package com.sino.scaffold.controller.wechat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxMenu;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;

import com.google.common.collect.Lists;
import com.sino.scaffold.bean.qa.Nutzer;
import com.sino.scaffold.config.wechat.NutzViewWrapper;
import com.sino.scaffold.config.wechat.WechatJsSDKConfiger;
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

	@Autowired
	WechatJsSDKConfiger wechatJsSDKConfiger;

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

	/**
	 * 微信继而验证和消息回调
	 * 
	 * @param key
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = { "wechat", "wechat/?" })
	public View msgIn(String key, HttpServletRequest req) throws IOException {
		return new NutzViewWrapper(Wxs.handle(wxHandler, req, key));
	}

	/**
	 * 创建菜单
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping("menu")
	public @ResponseBody Result menu() throws UnsupportedEncodingException {
		List<WxMenu> menus = Lists.newArrayList();
		WxMenu list = new WxMenu();
		list.setType("view");
		list.setName("话题列表");
		list.setUrl(String.format(
				"https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect",
				api.getAppid(),
				URLEncoder.encode("http://kerbores.ngrok.wendal.cn/", "UTF8")));
		list.setSubButtons(Lists.newArrayList());

		WxMenu add = new WxMenu();
		add.setType("view");
		add.setName("我要提问");
		add.setUrl(String.format(
				"https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect",
				api.getAppid(),
				URLEncoder.encode("http://kerbores.ngrok.wendal.cn/#/add", "UTF8")));
		add.setSubButtons(Lists.newArrayList());

		WxMenu me = new WxMenu();
		me.setType("view");
		me.setName("我的信息");
		me.setUrl(String.format(
				"https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect",
				api.getAppid(),
				URLEncoder.encode("http://kerbores.ngrok.wendal.cn/#/me", "UTF8")));
		me.setSubButtons(Lists.newArrayList());
		menus.add(list);
		menus.add(add);
		menus.add(me);
		api.menu_create(menus);
		return Result.success();
	}

	/**
	 * 获取 jssdk 配置
	 * 
	 * @param url
	 * @return
	 * @throws ExecutionException
	 */
	@PostMapping("config")
	@ApiOperation("获取 JSSDK 配置")
	public @ResponseBody Result config(@RequestParam("url") String url) throws ExecutionException {
		logger.debug(url);
		return Result.success().addData("config", wechatJsSDKConfiger.getConfig(url));
	}

}
