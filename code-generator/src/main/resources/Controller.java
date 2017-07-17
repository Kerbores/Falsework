package ${project.group}.${project.name}.controller;

import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ${project.group}.${project.name}.bean.${table.javaClassName!};
import ${project.group}.${project.name}.controller.base.BaseController;
import ${project.group}.${project.name}.service.${table.javaClassName!}Service;
import ${project.group}.${project.name}.utils.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author nutz-matic
 *
 */
@RestController
@RequestMapping("${table.lowerJavaClassName!}")
@Api(value = "${table.javaClassName!}", tags = { "${has(table.remarks) ? table.remarks : table.javaClassName }模块" })
public class ${table.javaClassName!}Controller extends BaseController{@Autowired ${table.javaClassName!}Service ${table.lowerJavaClassName!}Service;

	/**
	 * ${table.remarks!}列表
	 * 
	 * @param page 页码
	 * @return
	 */
	@GetMapping("list")
	@ApiOperation("${table.remarks!}列表")
	public Result list(@RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") int page) {
		return Result.success().addData("pager", ${table.lowerJavaClassName!}Service.searchByPage(_fixPage(page), Cnd.NEW().desc("id")));
	}

	/**
	 * ${table.remarks!}检索
	 * 
	 * @param key
	 *            关键词
	 * @param page
	 *            页码
	 * @return
	 */
	@GetMapping("search")
	@ApiOperation("${table.remarks!}检索")
	public Result search(@RequestParam("key") @ApiParam("关键词") String key, @RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") int page) {
		return Result.success().addData("pager", ${table.lowerJavaClassName!}Service.searchByKeyAndPage(_fixSearchKey(key), _fixPage(page), "name").addParam("key", key));
	}

	/**
	 * 添加${table.remarks!}
	 * 
	 * @param ${table.javaClassName!}
	 *            待添加${table.remarks!}
	 * @return
	 */
	@PostMapping("add")
	@ApiOperation("添加${table.remarks!}")
	public Result save(@RequestBody ${table.javaClassName!} ${table.lowerJavaClassName!})
	{
		return ${table.lowerJavaClassName!}Service.save(${table.lowerJavaClassName!}) == null ? Result.fail("保存${table.remarks!}失败!") : Result.success().addData("${table.lowerJavaClassName!}", ${table.lowerJavaClassName!});
	}

	/**
	 * 获取${table.remarks!}详情
	 * 
	 * @param id
	 *            ${table.remarks!}id
	 * @return
	 */
	@GetMapping("{id}")
	@ApiOperation("${table.remarks!}详情")
	public Result detail(@PathVariable("id") @ApiParam("${table.remarks!}id") long id) {
		return Result.success().addData("${table.lowerJavaClassName!}", ${table.lowerJavaClassName!}Service.fetch(id));
	}

	/**
	 * 删除${table.remarks!}
	 * 
	 * @param id
	 *            ${table.remarks!}id
	 * @return
	 */
	@GetMapping("delete/{id}")
	@ApiOperation("删除${table.remarks!}")
	public Result delete(@PathVariable("id") @ApiParam("${table.remarks!}id") long id) {
		return ${table.lowerJavaClassName!}Service.delete(id) == 1 ? Result.success() : Result.fail("删除失败!");
	}

	/**
	 * 更新${table.remarks!}
	 * 
	 * @param ${table.javaClassName!}
	 * @return
	 */
	@PostMapping("edit")
	@ApiOperation("更新${table.remarks!}")
	public Result update(@RequestBody ${table.javaClassName!} ${table.lowerJavaClassName!})
	{
		return ${table.lowerJavaClassName!}Service.updateIgnoreNull(${table.lowerJavaClassName!}) != 1 ? Result.fail("更新${table.remarks!}失败!") : Result.success().addData("${table.lowerJavaClassName!}", ${table.lowerJavaClassName!});
	}
}
