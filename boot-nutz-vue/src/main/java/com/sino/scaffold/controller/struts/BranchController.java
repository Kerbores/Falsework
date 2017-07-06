package com.sino.scaffold.controller.struts;

import org.apache.shiro.authz.annotation.Logical;
import org.nutz.dao.Cnd;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sino.scaffold.bean.InstallPermission;
import com.sino.scaffold.bean.struts.Branch;
import com.sino.scaffold.controller.base.BaseController;
import com.sino.scaffold.ext.shiro.anno.SINORequiresPermissions;
import com.sino.scaffold.service.struts.BranchService;
import com.sino.scaffold.utils.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author kerbores
 *
 */
@RestController
@RequestMapping("branch")
@Api(value = "Branch", tags = { "分支机构模块" })
public class BranchController extends BaseController {

	@Autowired
	BranchService branchService;

	/**
	 * 机构列表
	 * 
	 * @param page
	 *            页码
	 * @return
	 */
	@GetMapping("list")
	@SINORequiresPermissions(InstallPermission.BRANCH_LIST)
	@ApiOperation("机构列表")
	public Result list(@RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") int page) {
		return Result.success().addData("pager", branchService.searchByPage(_fixPage(page), Cnd.where("parentId", "=", 0)));
	}

	/**
	 * 机构检索
	 * 
	 * @param key
	 *            关键词
	 * @param page
	 *            页码
	 * @return
	 */
	@GetMapping("search")
	@SINORequiresPermissions(InstallPermission.BRANCH_LIST)
	@ApiOperation("机构检索")
	public Result search(
			@RequestParam("key") @ApiParam("关键词") String key,
			@RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") int page) {
		return Result.success().addData("pager",
				branchService.searchByKeyAndPage(_fixSearchKey(key), _fixPage(page), Cnd.where("parentId", "=", 0), "name", "value").addParam("key", key));
	}

	@At
	@POST
	@AdaptBy(type = JsonAdaptor.class)
	@SINORequiresPermissions(InstallPermission.BRANCH_ADD)
	public Result save(Branch branch) {
		return branchService.save(branch) == null ? Result.fail("保存数据失败!") : Result.success().addData("branch", branch);
	}

	@At("/?")
	@GET
	@SINORequiresPermissions(InstallPermission.BRANCH_EDIT)
	public Result detail(long id) {
		return Result.success().addData("branch", branchService.fetch(id));
	}

	@At("/delete/?")
	@GET
	@SINORequiresPermissions(InstallPermission.BRANCH_DELETE)
	public Result delete(long id) {
		return branchService.delete(id) == 1 ? Result.success() : Result.fail("删除数据失败!");
	}

	@At
	@POST
	@AdaptBy(type = JsonAdaptor.class)
	@SINORequiresPermissions(InstallPermission.BRANCH_EDIT)
	public Result update(Branch branch) {
		return branchService.updateIgnoreNull(branch) != 1 ? Result.fail("更新数据失败!") : Result.success().addData("branch", branch);
	}

	@At("/top/?")
	@GET
	@SINORequiresPermissions(InstallPermission.BRANCH_EDIT)
	public Result top() {
		return Result.success().addData("tops", branchService.query(Cnd.where("parentId", "=", 0)));
	}

	@At("/sub/?")
	@GET
	@SINORequiresPermissions(value = { InstallPermission.BRANCH_EDIT, InstallPermission.BRANCH_LIST }, logical = Logical.OR)
	public Result sub(long id) {
		return Result.success().addData("codes", branchService.query(Cnd.where("parentId", "=", id)));
	}
}
