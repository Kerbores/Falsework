package com.sino.scaffold.controller.wechat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.impl.AbstractWxHandler;
import org.nutz.weixin.impl.WxApi2Impl;
import org.nutz.weixin.repo.com.qq.weixin.mp.aes.AesException;
import org.nutz.weixin.repo.com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.nutz.weixin.spi.WxHandler;
import org.nutz.weixin.util.Wxs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;

import com.sino.scaffold.config.wechat.NutzViewWrapper;

/**
 * @author kerbores
 *
 */
@Controller
public class WechatEventController {

	@Autowired
	WxApi2Impl api;

	{
		Wxs.enableDevMode();
	}

	protected WxHandler wxHandler = new AbstractWxHandler() {

		@Override
		public WXBizMsgCrypt getMsgCrypt() {
			try {
				return new WXBizMsgCrypt(api.getToken(), api.getEncodingAesKey(), api.getAppid());
			} catch (AesException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public boolean check(String signature, String timestamp, String nonce, String key) {
			return Wxs.check(api.getToken(), signature, timestamp, nonce);
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
}
