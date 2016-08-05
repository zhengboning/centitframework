define(function(require) {
	require('plugins/extend');

	var Page = require('core/page');
	var Core = require('core/core');
	
	var LogInfoRemove = Page.extend(function() {
		
		// TODO 日志删除
		// @override
		this.submit = function(table, data) {
//			Core.ajax(Config.ContextPath+'system/optinfo/'+data.id, {
//            	type: 'json',
//                method: 'post',
//                data: {
//                    _method: 'delete'
//                }
//			}).then(function() {
//            	table.treegrid('remove', data.id);
//            });
		}
	});
	
	return LogInfoRemove;
});