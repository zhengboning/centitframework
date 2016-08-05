define(function(require) {
    var Config = require('config');

    var ThemeConfig = {
        id : "sdda",

        name : "山东档案",

        css : [
            "ui/themes/easyui/default/easyui.css",
            "ui/css/icon.css",
            "ui/themes/sdda/less/style.css",
            "ui/themes/sdda/less/header.css",
            "ui/themes/sdda/less/layout.css",
            "ui/themes/sdda/less/menu.css",
            "ui/themes/sdda/less/tab.css",
	        "ui/css/custom.css" /*总是放到最后一个，用户自定义样式覆盖默认主题*/
        ],

        template : "ui/themes/sdda/template.html",

        colors : [{
            id : "blue",
            name : "天空蓝",
            color: '#2FB0E2',
            css : "ui/themes/sdda/blue/color.css"
        }],
        
        menuLargeIcons: {
			DASHBOARD: 'easyui-large-icon icon-large-picture',
			Components: 'easyui-large-icon icon-large-chart',
			Tables: 'easyui-large-icon icon-large-shapes',
			Form: 'easyui-large-icon icon-large-clipart',
			MultiLevel: 'easyui-large-icon icon-large-smartart'
		},
		
        init: function() {

            // 菜单
            var Menu = require('centit/centit.menu').init({
                layout: 'top',
                hasSearch: false,
                height: 71,
                active: 'hover',
                loader: function(data) {
            		data = data.reverse();
            		return data;
            	},
            	
            	largeIcons: Config.menuLargeIcons || ThemeConfig.menuLargeIcons
            });

            // 菜单TAB
            var MenuTab = require('centit/centit.tab').init('menu_tab', {
            	tabHeight: 35,
            	pill: true
            })
            .open({
                id: 'DASHBOARD',
                text: '我的首页',
                url: Config.Url.DashboardUrl,
                closable: false
            }, true);

            // 工具栏
            var Toolbar = require('centit/toolbar/centit.toolbar').init('header', {
                height: 80,
                width: 36
            })
            // 修改密码对话框
            .add(require('centit/toolbar/centit.toolbar.modifypassword'))
            // 注销
            .add(require('centit/toolbar/centit.toolbar.logout'))
            // 皮肤颜色选择
			.add(require('centit/toolbar/centit.toolbar.skincolor'), ThemeConfig.colors);

            // 滚动条
//			$('#main_menu .panel-body').niceScroll({cursorcolor:"#BCBCBC"});

        }
    };

    return ThemeConfig;
});
