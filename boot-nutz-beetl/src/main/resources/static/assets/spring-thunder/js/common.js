/**
 * Created by kerbores on 2016/10/8.
 */
var CSRF = {
	init : function() {
		var token = $("#CSRFToken").val();
		if (token) {
			var _headers = {
				"RequestVerificationToken" : token
			};
			var option = {
				"headers" : _headers
			};
			$.ajaxSetup(option);
		}
	}
};
var Common = {
	rePwd : function() {
		layer.open({
			type : 2,
			title : '修改密码',
			fix : false,
			closeBtn : false,
			move : false,
			area : [ '500px', '375px' ],
			content : Common.getRootPath() + '/system/changePassword',
			btn : [ '确认', '取消' ],
			yes : function(index, layero) {
				if (layer.getChildFrame('#changePwdForm', index).validation({
						validationcallBack : function(status, dom, errorMsg, defaultValue) {
							if(!status){
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.showMsg(errorMsg);
							}
						}
					})) {
					var oldPwd =layer.getChildFrame('#old', index).val();
					var newPwd =layer.getChildFrame('#new', index).val();
					var confirmPwd =layer.getChildFrame('#confirm', index).val();
					if(newPwd != confirmPwd){
						var iframeWin = window[layero.find('iframe')[0]['name']];
						iframeWin.showMsg("两次输入的新密码不一致");
					}else{
						$.post(Common.getRootPath() + '/system/changePassword',{
							"old":oldPwd,
							"new":newPwd
						},function(result){
							if(result.operationState=="SUCCESS"){
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.showMsg("密码修改成功",function(){
									layer.close(index);
								});
							}else{
								var iframeWin = window[layero.find('iframe')[0]['name']];
								iframeWin.showMsg(result.data.reason);
							}
						},'json');
					}
				}
			},
			cancel : function(index) {
				layer.close(index);
			}
		});
	},
	baiduMap : function(callback) {
		layer.open({
			type : 2,
			title : '地点选择器',
			fix : false,
			closeBtn : false,
			move : false,
			area : [ '800px', '600px' ],
			content : Common.getRootPath() + '/map/show',
			btn : [ '确认', '取消' ],
			yes : function(index, layero) {

				if (!layer.getChildFrame('#data', index).val()) {
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.showMsg();
				} else {
					var result = $.parseJSON(layer.getChildFrame('#data', index).val());
					callback(result);
					layer.close(index);
				}
			},
			cancel : function(index) {
				layer.close(index);
			}
		});
	},
	branchSelector : function(callback) {
		layer.open({
			type : 2,
			title : '组织机构选择器',
			fix : false,
			closeBtn : false,
			move : false,
			area : [ '800px', '600px' ],
			content : Common.getRootPath() + '/branch/selector',
			btn : [ '确认', '取消' ],
			yes : function(index, layero) {
				if (!layer.getChildFrame('#branchId', index).val()) {
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.showMsg();
				} else {
					var result = $.parseJSON(layer.getChildFrame('#branchId', index).val());
					callback(result);
					layer.close(index);
				}
			},
			cancel : function(index) {
				layer.close(index);
			}
		});
	},
	init : function() {
		$('.nav-item').each(function(i,item){
			if( !$(item).find('.sub-menu').children().length && $(item).children('a').attr('href') == 'javascript:;'){
				$(item).hide();
			}
		})
	},
	logout : function() {
		location.href = Common.getRootPath() + '/user/logout'
	},
	getRootPath : function() {
		return contextPath;
	},
	validationFail : function(msg, dom) {
		layer.tips(msg, dom);
	},
	openUrl : function(url, title, width, height, closeBtn) {
		layer.open({
			type : 2,
			shadeClose : true,
			title : title,
			closeBtn : closeBtn,
			area : [ width + 'px', height + 'px' ], // 宽高
			content : contextPath + url
		});
	},
	closePopWindow : function() {
		var index = parent.layer.getFrameIndex(window.name);
		parent.layer.close(index);
	},
	showMessage : function(msg, title) {
		if (layer) {
			layer.msg(msg, {
				icon : 1,
				title : title || false
			});
		} else {
			alert(msg);
		}
	}
}

$(function() {
	CSRF.init();
	Common.init();
	$('.search-btn').on('click', function() {
		if ($('.search-form').find('input').validation(function(status, dom, errorMsg, defaultValue) { // 此处覆盖一下默认的回调,让tips框框到后面的搜索按钮上面去
				status ? $.noop() : function() {
					Common.validationFail(errorMsg, $(dom).next()[0]);
				}.call();
			})) {
			$('.search-form').submit();
		}
	});
	// 添加弹窗按钮的功能通用实现
	$('.btn-pop').on('click', function() {
		layer.open({
			type : 2,
			title : $(this).data('title'),
			shadeClose : false,
			closeBtn : false,
			shade : 0.8,
			area : [ $(this).data('width') + 'px', $(this).data('height') + 'px' ],
			content : Common.getRootPath() + $(this).data('url')
		});
	});
	// 返回按钮
	$('.btn-back').on('click', function() {
		history.go(-1);
	});
	// 删除按钮功能
	$('.btn-delete').on('click', function() {
		var url = $(this).data('url');
		var id = $(this).data('id');
		layer.confirm('确认删除这条数据 ?', {
			icon : 3,
			title : '删除提示'
		}, function(index) {
			$.post(Common.getRootPath() + url, {
				id : id
			}, function(result) {
				layer.close(index); // 关闭弹窗
				if (result.operationState == 'SUCCESS') {
					location.reload(); // 刷新页面
				} else {
					Common.showMessage(result.data.reason);
				}
			}, 'json');

		});
	});
	// 弹窗返回按钮
	$('.btn-dialog-undo').on('click', function() {
		Common.closePopWindow();
	});
	// 禁用分页条的disabled节点
	$('.pagination .disabled a').on('click', function() {
		return false;
	});
	$('.ellipsis-col').attr('style', "max-width: " + $('.ellipsis-col').data('width') + "px").on('mouseenter', function() {
		layer.msg($(this).text());
	});
})

Date.prototype.format = function(format) {
	var date = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"h+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S+" : this.getMilliseconds()
	};
	if (/(y+)/i.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
	}
	for (var k in date) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
		}
	}
	return format;
}