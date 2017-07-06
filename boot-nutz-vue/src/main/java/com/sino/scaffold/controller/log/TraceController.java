package com.sino.scaffold.controller.log;

import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sino.scaffold.bean.InstalledRole;
import com.sino.scaffold.controller.base.BaseController;
import com.sino.scaffold.ext.shiro.anno.SINORequiresRoles;
import com.sino.scaffold.service.log.LoginLogService;
import com.sino.scaffold.utils.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author kerbores
 *
 */
@RestController
@RequestMapping("trace")
@Api(value = "Trace", tags = { "登录轨迹模块" })
public class TraceController extends BaseController {

	@Autowired
	LoginLogService loginLogService;

	/**
	 * 登录日志列表
	 * 
	 * @param page
	 *            页码
	 * @return
	 */
	@GetMapping("list")
	@SINORequiresRoles(InstalledRole.SU)
	@ApiOperation("登录日志列表")
	public Result list(@RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") int page) {
		return Result.success().addData("pager", loginLogService.searchByPage(_fixPage(page), Cnd.NEW().desc("id")));
	}

	/**
	 * 登录日志检索
	 * 
	 * @param page
	 *            页码
	 * @param key
	 *            关键词
	 * @return
	 */
	@GetMapping("search")
	@SINORequiresRoles(InstalledRole.SU)
	@ApiOperation("登录日志检索")
	public Result search(@RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") int page, @RequestParam("key") @ApiParam("关键词") String key) {
		return Result.success().addData("pager", loginLogService.searchByKeyAndPage(_fixSearchKey(key), _fixPage(page), "account", "ip").addParam("key", key));
	}
}
