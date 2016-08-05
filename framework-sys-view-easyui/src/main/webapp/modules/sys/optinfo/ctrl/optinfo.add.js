define(function(require) {
	var Config = require('config');
	var Page = require('core/page');
	
	var OptInfoAdd = Page.extend(function() {
		
		// @override
		this.object = {
			isInToolbar: 'Y', 	// 显示
			pageType: 'D',		// DIV方式打开
			optType: 'N'		// 普通业务             
		};
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			
			// 
			form.form('load', $.extend({}, this.object, {
				// 0 为顶级节点
				preOptId: data ? data.id : " "
			}))
			.form('addValidation', {
				optId: {
					required: true,
				    validType: {
				    	remote: [Config.ContextPath+'system/optinfo/notexists/{{optId}}', 'optId']
				    }
				}
			})
			.form('disableValidation')
			.form('focus');
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			// 开启校验
			form.form('enableValidation');
			var isValid = form.form('validate');
			
			if (isValid) {
				this.newObject = form.form('value');
			
				form.form('ajax', {
					url: Config.ContextPath + 'system/optinfo',
					method: 'post'
				}).then(closeCallback);
			}
			
			return false;
		};
		
		// @override 
		this.onClose = function(table, data) {
			var newObject = this.newObject;
			
			if (!newObject) return;
			
			newObject = $.extend(newObject, {
				id: newObject.optId,
				text: newObject.optName,
				url: newObject.optRoute
			})
		
			if (data) {
                // 展开节点
	    		table.treegrid('expand', data.id);
	    		// 添加新菜单到树
	        	table.treegrid('append', {
	        		parent: newObject.preOptId,
	        		data: [newObject]
	        	});
            }
			else{
				table.treegrid('reload');
			}
			
            
           
		};
	});
	
	return OptInfoAdd;
});