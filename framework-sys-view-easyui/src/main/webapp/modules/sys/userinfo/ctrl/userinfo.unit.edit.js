define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	
	var Page = require('core/page');
	
	// 编辑机构用户
	var UserInfoUnitEdit = Page.extend(function() {
		var _self = this;
		
		// @override
		this.load = function(panel, data) {
			
			// 保存原始信息
			this.oldData = data;
			
			var form = panel.find('form');
			Core.ajax(Config.ContextPath+'system/userunit/'+data.userUnitId, {
				method: 'get'
			}).then(function(data) {
				_self.data = data;
				
				form.form('load', data)
					.form('disableValidation')
					.form('readonly', 'unitCode')
					.form('focus');
			});
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			form.form('enableValidation');
			var isValid = form.form('validate');
			
			if (isValid) {
				// 原始职位和岗位
				data.oldUserStation = this.oldData.userStation;
				data.oldUserRank = this.oldData.userRank;
				form.form('ajax', {
					url: Config.ContextPath+'system/userunit/'+data.userUnitId,
					method: 'put',
					data: data
				}).then(function(){
					return require('loaders/cache/loader.system').loadAll()
				}).then(function(data){
					var table_userinfo=_self.parent.parent.panel.find("#userInfoTable");
					//var row=table_userinfo.datagrid("getSelected");
					//table_userinfo.datagrid("updateRow",{index:0,row:data});
					table_userinfo.datagrid("reload");
					closeCallback();
				});
			}
			
			return false;
		};
		
		// @override
		this.onClose = function(table) {
			table.datagrid('reload');
		};
	});
	
	return UserInfoUnitEdit;
});