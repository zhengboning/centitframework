define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	
	var Page = require('core/page');
	
	// 删除用户角色
	var UserInfoRoleRemove = Page.extend(function() {
		
		// @override
		this.submit = function(table, data) {
			
		
			
			Core.ajax(Config.ContextPath+'system/userrole/ban/'+data.roleCode+'/'+data.userCode, {
				method: 'post',
				data: {
					_method: 'PUT',
					obtainDate:data.obtainDate
				}
			}).then(function() {
				table.datagrid('reload');
			});
		};
		
		
		this.renderButton = function (btn, data) {
			var secede=data.secedeDate;
		    var strArray = secede.split(" ");
		    var strDate = strArray[0].split("-");
		    var strTime = strArray[1].split(":");
		    var secedeDate = new Date(strDate[0], (strDate[1]-parseInt(1)), strDate[2], strTime[0], strTime[1], strTime[2]);
			var now=new Date();
		    return secedeDate>=now;
	    };
		
	});
	
	return UserInfoRoleRemove;
});