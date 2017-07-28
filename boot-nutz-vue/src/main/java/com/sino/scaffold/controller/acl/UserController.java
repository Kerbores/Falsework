package com.sino.scaffold.controller.acl;

import org.apache.shiro.SecurityUtils;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sino.scaffold.BootNutzVueApplication;
import com.sino.scaffold.bean.InstallPermission;
import com.sino.scaffold.bean.acl.User;
import com.sino.scaffold.controller.base.BaseController;
import com.sino.scaffold.dto.GrantDTO;
import com.sino.scaffold.dto.UserLoginDto;
import com.sino.scaffold.ext.shiro.anno.SINORequiresPermissions;
import com.sino.scaffold.ext.shiro.matcher.SINOCredentialsMatcher;
import com.sino.scaffold.rest.ApiRequest;
import com.sino.scaffold.service.acl.ShiroUserService;
import com.sino.scaffold.service.acl.UserService;
import com.sino.scaffold.utils.DES;
import club.zhcs.common.Result;

import club.zhcs.apm.APM;
import club.zhcs.captcha.CaptchaView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author kerbores
 *
 */
@RestController
@RequestMapping("user")
@Api(value = "User", tags = { "用户模块" }, consumes = "信息是二位")
public class UserController extends BaseController {

	@Autowired
	ShiroUserService shiroUserService;

	@Autowired
	UserService userService;

	/**
	 * 用户列表
	 * 
	 * @param page
	 *            页码
	 * @return
	 */
	@GetMapping("list")
	@SINORequiresPermissions(InstallPermission.USER_LIST)
	@ApiOperation(value = "用户分页列表")
	public Result list(@RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") int page) {
		return Result.success().addData("pager", userService.searchByPage(_fixPage(page), Cnd.NEW().desc("id")));
	}

	/**
	 * 
	 * @param key
	 *            检索条件
	 * @param page
	 *            页码
	 * @return
	 */
	@GetMapping("search")
	@SINORequiresPermissions(InstallPermission.USER_LIST)
	@ApiOperation(value = "用户分页检索")
	public Result search(@RequestParam("key") @ApiParam("搜索关键字") String key, @RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") int page) {
		return Result.success()
				.addData("pager", userService.searchByKeyAndPage(
						_fixSearchKey(key),
						_fixPage(page),
						// Cnd.NEW().desc("id"),
						"name", "nickName", "realName")
						.addParam("key", key));
	}

	/**
	 * 添加用户
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping(value = "add")
	@SINORequiresPermissions(InstallPermission.USER_ADD)
	@ApiOperation(value = "新增用户")
	public Result save(@RequestBody User user) {
		user.setPassword(SINOCredentialsMatcher.password(user.getName(), user.getPassword()));// 密码密文转换
		return userService.save(user) == null ? Result.fail("保存用户失败!") : Result.success().addData("user", user);
	}

	/**
	 * 编辑用户
	 * 
	 * @param user
	 *            待更新用户
	 * @return
	 */
	@PostMapping(value = "edit")
	@SINORequiresPermissions(InstallPermission.USER_EDIT)
	@ApiOperation(value = "修改用户基本信息", notes = "仅修改姓名,电话,邮箱和状态信息")
	public Result edit(@RequestBody User user) {
		return userService.update(user, "realName", "phone", "email", "status") ? Result.success() : Result.fail("更新用户失败!");
	}

	/**
	 * 重置密码
	 * 
	 * @param user
	 *            用户信息
	 * @return
	 */
	@PostMapping(value = "resetPassword")
	@SINORequiresPermissions(InstallPermission.USER_EDIT)
	@ApiOperation(value = "重置用户密码")
	public Result resetPassword(@RequestBody User user) {
		user.setPassword(SINOCredentialsMatcher.password(user.getName(), user.getPassword()));// 密码密文转换
		return userService.update(user, "password") != 1 ? Result.fail("保存用户失败!") : Result.success().addData("user", user);
	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 *            用户id
	 * @return
	 */
	@GetMapping("delete/{id}")
	@SINORequiresPermissions(InstallPermission.USER_DELETE)
	@ApiOperation(value = "删除用户")
	public Result delete(@PathVariable("id") @ApiParam("待删除用户id") long id) {
		return userService.delete(id) == 1 ? Result.success() : Result.fail("删除用户失败!");
	}

	/**
	 * 用户详情
	 * 
	 * @param id
	 *            用户id
	 * @return
	 */
	@GetMapping("{id}")
	@SINORequiresPermissions(InstallPermission.USER_DETAIL)
	@ApiOperation(value = "用户详情")
	public Result detail(@PathVariable("id") @ApiParam("用户id") long id) {
		return Result.success().addData("user", userService.fetch(id));
	}

	/**
	 * 获取用户的角色信息
	 * 
	 * @param id
	 *            用户id
	 * @return
	 */
	@GetMapping("role/{id}")
	@SINORequiresPermissions(InstallPermission.USER_ROLE)
	@ApiOperation(value = "用户角色授权信息")
	public Result roleInfo(@PathVariable("id") @ApiParam("用户id") int id) {
		return Result.success().addData("infos", userService.findRolesWithUserPowerdInfoByUserId(id));
	}

	/**
	 * 获取用户的权限信息
	 * 
	 * @param id
	 *            用户id
	 * @return
	 */
	@GetMapping("permission/{id}")
	@SINORequiresPermissions(InstallPermission.USER_GRANT)
	@ApiOperation(value = "用户权限信息")
	public Result permissionInfo(@PathVariable("id") @ApiParam("用户id") int id) {
		return Result.success().addData("infos", userService.findPermissionsWithUserPowerdInfoByUserId(id));
	}

	/**
	 * 为用户设置角色
	 * 
	 * @param dto
	 * @return
	 */
	@PostMapping("/grant/role")
	@SINORequiresPermissions(InstallPermission.USER_ROLE)
	@ApiOperation("为用户设置角色")
	public Result grantRole(@RequestBody GrantDTO dto) {
		return userService.setRole(dto.getGrantIds(), dto.getId());
	}

	/**
	 * 为用户设置权限
	 * 
	 * @param dto
	 * @return
	 */
	@PostMapping("/grant/permission")
	@SINORequiresPermissions(InstallPermission.USER_GRANT)
	@ApiOperation("为用户设置权限")
	public Result grantPermission(@RequestBody GrantDTO dto) {
		return userService.setPermission(dto.getGrantIds(), dto.getId());
	}

	/**
	 * 登录
	 * 
	 * @param request
	 *            请求
	 * @param session
	 *            会话
	 * @return 登录结果
	 */
	@PostMapping("login")
	@ApiOperation(value = "用户登录")
	@APM("用户登录")
	public Result login(@RequestBody ApiRequest<UserLoginDto> request) {
		if (Strings.equalsIgnoreCase(request.getData().getCaptcha(), _getSessionObj(CaptchaView.CAPTCHA_SESSION_KEY, String.class))) {
			Result result = shiroUserService.login(request.getData().getUserName(), request.getData().getPassword(), Lang.getIP(request()));
			if (result.isSuccess()) {
				// 登录成功处理
				_putSession(BootNutzVueApplication.USER_KEY, result.getData().get("loginUser"));
				if (request.getData().isRememberMe()) {
					NutMap data = NutMap.NEW();
					data.put("user", request.getData().getUserName());
					data.put("password", request.getData().getPassword());
					data.put("rememberMe", request.getData().getPassword());
					_addCookie("kerbores", DES.encrypt(Json.toJson(data)), 24 * 60 * 60 * 365);
				}
				return result
						.addData("roles", shiroUserService.roleInfos(request.getData().getUserName()))
						.addData("permissions", shiroUserService.permissionInfos(request.getData().getUserName()));
			}
			return result;
		} else {
			return Result.fail("验证码输入错误");
		}
	}

	@GetMapping("logout")
	@ApiOperation(value = "退出登录")
	public Result logout() {
		SecurityUtils.getSubject().logout();
		return Result.success();
	}

}
