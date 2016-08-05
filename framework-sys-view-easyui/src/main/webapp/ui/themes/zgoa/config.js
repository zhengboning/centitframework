define(function(require) {
    var Config = require('config');

    var ThemeConfig = {
        id : "zjhs",

        name : "镇江海事",

        css : [
            "ui/themes/easyui/default/easyui.css",
            "ui/css/icon.css",
            "ui/themes/zgoa/less/style.css",
            "ui/themes/zgoa/less/header.css",
            "ui/themes/zgoa/less/layout.css",
            "ui/themes/zgoa/less/menu.css",
            "ui/themes/zgoa/less/tab.css",
		    "ui/css/custom.css" /*总是放到最后一个，用户自定义样式覆盖默认主题*/
        ],

        template : "ui/themes/zgoa/template.html",

        colors : [{
            id : "blue",
            name : "天空蓝",
            color: '#2FB0E2',
            css : "ui/themes/zgoa/blue/color.css"
        }],
        
        menuIcons: {
			DASHBOARD: 'fa fa-home',
			Components: 'fa fa-tachometer',
			Tables: 'fa fa-table',
			Form: 'fa fa-pencil',
			MultiLevel: 'fa fa-gears'
		},
		
		menuLargeIcons: {
			DASHBOARD: 'fa fa-home',
			Components: 'fa fa-tachometer',
			Tables: 'fa fa-table',
			Form: 'fa fa-pencil',
			MultiLevel: 'fa fa-gears'
		},

        init: function() {

            // 菜单
            var Menu = require('centit/centit.menu').init({
            	collapsible: false,
                border: true,
                width: 190,
                layout: 'mix',
                hasSearch: false,
            	
            	icons: Config.menuIcons || ThemeConfig.menuIcons,

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
                    url: 'modules/frame/metro/metro.html',
                    closable: false
                }, true);

            // 工具栏
            var Toolbar = require('centit/toolbar/centit.toolbar').init('header', {
                height: 80,
                width: 118
            })
            // 全屏
            .add(require('centit/toolbar/centit.toolbar.fullscreen'))
            // 修改密码对话框
            .add(require('centit/toolbar/centit.toolbar.modifypassword'))
            // 注销
            .add(require('centit/toolbar/centit.toolbar.logout'));

            // 滚动条
//			$('#main_menu .panel-body').niceScroll({cursorcolor:"#BCBCBC"});

        }
    };

    return ThemeConfig;
});
