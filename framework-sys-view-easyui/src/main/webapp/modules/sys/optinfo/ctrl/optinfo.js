define(function(require) {
    var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');
    
    var Mustache = require('plugins/mustache.min');
    
    var OptInfoAdd = require('../ctrl/optinfo.add');
    var OptInfoAdd1 = require('../ctrl/optinfo.add');
    var OptInfoEdit = require('../ctrl/optinfo.edit');
    var OptInfoAll = require('../ctrl/optinfo.all');
    var OptInfoRemove = require('../ctrl/optinfo.remove');

    // 业务信息
    var OptInfo = Page.extend(function() {
    	
    	this.injecte([
          new OptInfoAdd('optinfo_add'),
          new OptInfoAdd1('optinfo_add1'),
          OptInfoAll,
          new OptInfoEdit('optinfo_edit'),
          new OptInfoAll('optinfo_all'),
          new OptInfoRemove('optinfo_remove')
    	]);
    	
    	// @override
    	this.load = function(panel) {
    		var table = this.table = panel.find('table');
    		
    		table.ctreegrid({
    			controller: this,
    			
    			rowStyler: function(row) {
		    		// 不在菜单上显示
		    		if (row && row.isInToolbar == 'N') {
		    			return {
		    				'class': 'ban'
		    			};
		    		}
		    	},
		    	
		    	/*onClickRow: function(row) {
					var layout = $('#optinfo_panel').layout('panel', 'east');
					layout.data('panel').options.onLoad = function() {
						OptInfoAll.init(layout, row);
					}
					//layout.panel('setTitle', row.userName + ' 操作定义')
					layout.panel('refresh', Config.ViewContextPath + 'modules/sys/optinfo/optinfo-all.html');
				},*/
		    	// 加载完数据回调函数
		    	onLoadSuccess: function(data) {
		    		var panel = $(this).treegrid('getPanel');
		    		btns = panel.find('.easyui-linkbutton').linkbutton();
		    	}
		    	
		    	
    		});
    	};
    	
    	this.Format = {
            // ...不用显示
            Url: function(url) {
                if (!url) return "";
                url = url.replace(/^\s\t*/, '').replace(/\s\t*$/, '');

                if (url === '...') {
                    return "";
                }

                return url;
            },

            // 转换为图标
            Icon: function(cls) {
                if (cls) {
                    return Mustache.render('<a class="easyui-linkbutton icon" plain="true" iconCls="{{cls}}" style="height: 18px;"></a>', {
                        cls: cls
                    });
                }
                
                return "";
            }
        }
    });

    return OptInfo;
});