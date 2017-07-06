package com.sino.scaffold.controller.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sino.scaffold.bean.InstalledRole;
import com.sino.scaffold.controller.base.BaseController;
import com.sino.scaffold.ext.shiro.anno.SINORequiresRoles;
import com.sino.scaffold.service.log.OperationLogService;
import com.sino.scaffold.utils.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author kerbores
 *
 */
@RestController
@RequestMapping("log")
@Api(value = "SystemLog", tags = { "审计日志模块" })
public class SystemLogController extends BaseController {

	@Autowired
	OperationLogService operationLogService;

	/**
	 * 审计日志列表
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("list")
	@SINORequiresRoles(InstalledRole.SU)
	@ApiOperation("审计日志列表")
	public Result list(@RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") int page) {
		return Result.success().addData("pager", operationLogService.searchByPage(_fixPage(page)));
	}

	@GetMapping("search")
	@SINORequiresRoles(InstalledRole.SU)
	@ApiOperation("审计日志检索")
	public Result search(@RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") int page, @RequestParam("key") @ApiParam("关键词") String key) {
		return Result.success().addData("pager",
				operationLogService.searchByKeyAndPage(_fixSearchKey(key), _fixPage(page), "account", "ip", "module", "action", "description").addParam("key", key));
	}
}
