define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	var Cache = require('core/cache');
	var DeptRoleAdd = require('../ctrl/deptrole.add');
	var DeptRoleEdit = require('../ctrl/deptrole.edit');
	var DeptRoleOperate = require('../ctrl/deptrole.operate');
    
	// 角色信息列表
	var DeptRole = Page.extend(function() {
		this.injecte([
	        new DeptRoleAdd('deptrole_add'), 
	        new DeptRoleEdit('deptrole_edit'), 
	        new DeptRoleOperate('deptrole_operate')
	    ]);
		
		// @override
		this.load = function(panel) {
			var loginuser=Cache.get('loginuser');
			var primaryUnit=loginuser.primaryUnit;
			panel.find('table').cdatagrid({
				// 必须要加此项!!
				controller: this,
				url:Config.ContextPath+"system/roleinfo/unit/"+primaryUnit,
				queryParams: {
					s_isValid: 'T'
				},
				
				rowStyler: function(index, row) {
					if (row.isValid == 'F') {
						return {'class': 'ban'};
					}
				}
			});
		};
	});
	
	return DeptRole;
});