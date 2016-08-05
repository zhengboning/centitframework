define(function(require) {
    var Config = require('config');

    var ThemeConfig = {
        id : "xkj",

        name : "新框架",

        css : [
            "ui/themes/easyui/default/easyui.css",
            "ui/css/icon.css",
            "ui/themes/xkj/less/style.css",
            "ui/themes/xkj/less/header.css",
            "ui/themes/xkj/less/layout.css",
            "ui/themes/xkj/less/menu.css",
            "ui/themes/xkj/less/tab.css",
	        "ui/css/custom.css" /*总是放到最后一个，用户自定义样式覆盖默认主题*/
        ],

        template : "ui/themes/xkj/template.html",

        colors : [{
            id : "blue",
            name : "天空蓝",
            color: '#2FB0E2',
            css : "ui/themes/new/blue/color.css"
        },{
            id : "yellow",
            name : "玛瑙黄",
            color: '#D1D433',
            css : "ui/themes/xjoa/yellow/color.css"
        },{
            id : "red",
            name : "深沉红",
            color: '#B40000',
            css : "ui/themes/xjoa/red/color.css"
        },{
            id : "green",
            name : "橄榄绿",
            color: '#367500',
            css : "ui/themes/xjoa/green/color.css"
        }],
        
        menuIcons: {
			DASHBOARD: 'xf xf-dep',
			Components: 'xf xf-org',
			Tables: 'xf xf-sys',
			Form: 'xf xf-fag',
			MultiLevel: 'xf xf-qua'
		},

        init: function() {

            // 菜单
            var Menu = require('centit/centit.menu').init({
                border: true,
                width: 229,
                layout: 'left',
                hasSearch: false,
                setSize: function(container) {
                    return container.closest('div.layout-body').height() - container.prev('div.easyui-panel').outerHeight() - 2;
                },
                loader: Config.menuLoader || function(data) {
                    data = [{
						id: 'DASHBOARD',
						text: '我的首页',
//						icon: 'xf xf-home',
						url: Config.Url.DashboardUrl,
						isDashboard: true
					}].concat(data);
                    
					return data;
                },
                
                selected: 1,
                
                icons: Config.menuIcons || ThemeConfig.menuIcons
            });

            // 菜单TAB
            var MenuTab = require('centit/centit.tab').init('menu_tab', {
				tabHeight: 30
			}).open({
                id: 'DASHBOARD',
                text: '我的首页',
                url: 'modules/frame/metro/metro.html'
            }, true);

            // 工具栏
            var Toolbar = require('centit/toolbar/centit.toolbar').init('header', {
                height: 28,
                width: 350
            })
            // 全屏
//                .add(require('centit/toolbar/centit.toolbar.fullscreen'))
            // 修改密码对话框
            .add(require('centit/toolbar/centit.toolbar.modifypassword'))
            // 皮肤颜色选择
//                .add(require('centit/toolbar/centit.toolbar.skincolor'), ThemeConfig.colors)
            // 帮助
            .add(require('centit/toolbar/centit.toolbar.help'))
            // 注销
            .add(require('centit/toolbar/centit.toolbar.logout'));
            // 滚动条
//			$('#main_menu .panel-body').niceScroll({cursorcolor:"#BCBCBC"});
            if (!$("#sideTop")[0])
            	$("#side").before("<div id='sideTop'>MENU</div>");
        }
    };

    return ThemeConfig;
});
