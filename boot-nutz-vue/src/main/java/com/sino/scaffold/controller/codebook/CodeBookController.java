package com.sino.scaffold.controller.codebook;

import org.apache.shiro.authz.annotation.Logical;
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
import com.sino.scaffold.bean.config.CodeBook;
import com.sino.scaffold.controller.base.BaseController;
import com.sino.scaffold.ext.shiro.anno.SINORequiresPermissions;
import com.sino.scaffold.service.codebook.CodeBookService;
import com.sino.scaffold.utils.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author kerbores
 *
 */
@RestController
@RequestMapping("codebook")
@Api(value = "CodeBook", tags = { "码本模块" })
public class CodeBookController extends BaseController {
	@Autowired
	CodeBookService codebookService;

	/**
	 * 码本列表
	 * 
	 * @param page
	 *            页码
	 * @return
	 */
	@GetMapping("list")
	@SINORequiresPermissions(InstallPermission.CODEBOOK_LIST)
	@ApiOperation("码本列表")
	public Result list(@RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") int page) {
		return Result.success().addData("pager", codebookService.searchByPage(_fixPage(page), Cnd.where("parentId", "=", 0)));
	}

	/**
	 * 码本检索
	 * 
	 * @param key
	 *            关键词
	 * @param groupId
	 *            分组id
	 * @param page
	 *            页码
	 * @return
	 */
	@GetMapping("search")
	@SINORequiresPermissions(InstallPermission.CODEBOOK_LIST)
	@ApiOperation("码本检索")
	public Result search(
			@RequestParam("key") @ApiParam("关键词") String key,
			@RequestParam(value = "group", defaultValue = "0") @ApiParam("分组id") int groupId,
			@RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") int page) {
		return Result.success()
				.addData("pager",
						codebookService.searchByKeyAndPage(
								_fixSearchKey(key),
								_fixPage(page),
								groupId == 0 ? Cnd.where("parentId", "=", 0) : Cnd.where("parentId", "=", 0).and("groupId", "=", groupId), "name", "value")
								.addParam("key", key));
	}

	/**
	 * 添加数据
	 * 
	 * @param codebook
	 *            待添加码本数据
	 * @return
	 */
	@PostMapping("add")
	@SINORequiresPermissions(InstallPermission.CODEBOOK_ADD)
	@ApiOperation("添加码本数据")
	public Result save(@RequestBody CodeBook codebook) {
		return codebookService.save(codebook) == null ? Result.fail("保存数据失败!") : Result.success().addData("codebook", codebook);
	}

	/**
	 * 数据详情
	 * 
	 * @param id
	 *            数据id
	 * @return
	 */
	@GetMapping("{id}")
	@SINORequiresPermissions(InstallPermission.CODEBOOK_EDIT)
	@ApiOperation("数据详情")
	public Result detail(@PathVariable("id") @ApiParam("数据id") long id) {
		return Result.success().addData("codebook", codebookService.fetch(id));
	}

	/**
	 * 删除数据
	 * 
	 * @param id
	 *            数据id
	 * @return
	 */
	@GetMapping("delete/{id}")
	@SINORequiresPermissions(InstallPermission.CODEBOOK_DELETE)
	@ApiOperation("删除数据")
	public Result delete(@PathVariable("id") @ApiParam("数据id") long id) {
		return codebookService.delete(id) == 1 ? Result.success() : Result.fail("删除数据失败!");
	}

	/**
	 * 编辑码本数据
	 * 
	 * @param codebook
	 *            待编辑数据
	 * @return
	 */
	@PostMapping("edit")
	@SINORequiresPermissions(InstallPermission.CODEBOOK_EDIT)
	@ApiOperation("编辑数据")
	public Result update(@RequestBody CodeBook codebook) {
		return codebookService.updateIgnoreNull(codebook) != 1 ? Result.fail("更新数据失败!") : Result.success().addData("codebook", codebook);
	}

	/**
	 * 分组下的顶级码本列表
	 * 
	 * @param id
	 *            分组id
	 * @return
	 */
	@GetMapping("top/{id}")
	@SINORequiresPermissions(InstallPermission.CODEBOOK_EDIT)
	@ApiOperation("获取指定分组下的顶级码本")
	public Result top(@PathVariable("id") @ApiParam("分组id") long id) {
		return Result.success().addData("codes", codebookService.query(Cnd.where("groupId", "=", id).and("parentId", "=", 0)));
	}

	/**
	 * 获取指定码本的子级数据
	 * 
	 * @param id
	 *            码本id
	 * @return
	 */
	@GetMapping("sub/{id}")
	@SINORequiresPermissions(value = { InstallPermission.CODEBOOK_EDIT, InstallPermission.CODEBOOK_LIST }, logical = Logical.OR)
	@ApiOperation("获取指定码本的子级数据")
	public Result sub(@PathVariable("id") @ApiParam("码本数据id") long id) {
		return Result.success().addData("codes", codebookService.query(Cnd.where("parentId", "=", id)));
	}
}
