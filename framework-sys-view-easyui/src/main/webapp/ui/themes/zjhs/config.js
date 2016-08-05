define(function(require) {
    var Config = require('config');

    var ThemeConfig = {
        id : "zjhs",

        name : "镇江海事",

        css : [
            "ui/themes/easyui/default/easyui.css",
            "ui/css/icon.css",
            "ui/themes/zjhs/less/style.css",
            "ui/themes/zjhs/less/header.css",
            "ui/themes/zjhs/less/layout.css",
            "ui/themes/zjhs/less/menu.css",
            "ui/themes/zjhs/less/tab.css",
		    "ui/css/custom.css" /*总是放到最后一个，用户自定义样式覆盖默认主题*/
        ],

        template : "ui/themes/zjhs/template.html",

        colors : [{
            id : "blue",
            name : "天空蓝",
            color: '#2FB0E2',
            css : "ui/themes/new/blue/color.css"
        }],
        
        menuIcons: {
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
                layout: 'left',
                hasSearch: false,
            	icons: Config.menuIcons || ThemeConfig.menuIcons,
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
                height: 24,
                width: 303
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
