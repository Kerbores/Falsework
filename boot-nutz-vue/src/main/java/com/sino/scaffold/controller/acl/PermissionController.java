package com.sino.scaffold.controller.acl;

import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sino.scaffold.bean.InstallPermission;
import com.sino.scaffold.bean.acl.Permission;
import com.sino.scaffold.controller.base.BaseController;
import com.sino.scaffold.ext.shiro.anno.SINORequiresPermissions;
import com.sino.scaffold.service.acl.PermissionService;
import com.sino.scaffold.utils.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author kerbores
 *
 */
@RestController
@RequestMapping("permission")
@Api(value = "Permission", tags = { "权限模块" })
public class PermissionController extends BaseController {

	@Autowired
	PermissionService permissionService;

	/**
	 * 权限列表
	 * 
	 * @RequestParam page
	 * @return
	 */
	@GetMapping("list")
	@SINORequiresPermissions(InstallPermission.PERMISSION_LIST)
	@ApiOperation("权限列表")
	public Result list(@RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") int page) {
		return Result.success().addData("pager", permissionService.searchByPage(_fixPage(page), Cnd.NEW().desc("id")));
	}

	/**
	 * 权限搜索
	 * 
	 * @param key
	 *            关键词
	 * @param page
	 *            页码
	 * @return
	 */
	@GetMapping("search")
	@SINORequiresPermissions(InstallPermission.PERMISSION_LIST)
	@ApiOperation("权限分页搜索")
	public Result search(@RequestParam("key") @ApiParam("搜索关键词") String key, @RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") int page) {
		return Result.success().addData("pager", permissionService.searchByKeyAndPage(_fixSearchKey(key), _fixPage(page), "name", "description").addParam("key", key));
	}

	/**
	 * 添加权限
	 * 
	 * @param Permission
	 * @return
	 */
	@PostMapping("add")
	@SINORequiresPermissions(InstallPermission.PERMISSION_ADD)
	@ApiOperation("添加权限")
	public Result save(@RequestBody Permission permission) {
		return permissionService.save(permission) == null ? Result.fail("保存权限失败!") : Result.success().addData("permission", permission);
	}

	/**
	 * 权限详情
	 * 
	 * @param id
	 *            权限id
	 * @return
	 */
	@GetMapping("{id}")
	@SINORequiresPermissions(InstallPermission.PERMISSION_EDIT)
	@ApiOperation("权限详情")
	public Result detail(@PathVariable("id") @ApiParam("权限id") long id) {
		return Result.success().addData("permission", permissionService.fetch(id));
	}

	/**
	 * 删除全新啊
	 * 
	 * @param id
	 *            权限id
	 * @return
	 */
	@GetMapping("delete/{id}")
	@SINORequiresPermissions(InstallPermission.PERMISSION_DELETE)
	@ApiOperation("删除权限")
	public Result delete(@PathVariable("id") @ApiParam("权限id") long id) {
		return permissionService.delete(id) == 1 ? Result.success() : Result.fail("删除权限失败!");
	}

	/**
	 * 更新权限
	 * 
	 * @param Permission
	 * @return
	 */
	@PostMapping("edit")
	@SINORequiresPermissions(InstallPermission.PERMISSION_EDIT)
	@ApiOperation("更新权限")
	public Result update(@RequestBody Permission permission) {
		return permissionService.updateIgnoreNull(permission) != 1 ? Result.fail("更新权限失败!") : Result.success().addData("permission", permission);
	}

}
