define(function(require) {
	var Config = require("config");
	var ThemeConfig = {
		id : "jmht",
		name : "jmht",
		css : [
		       "ui/themes/easyui/default/easyui.css",
		       "ui/css/icon.css","ui/css/icon-base.css","ui/css/jm-icons/jm-icon.css",
		       "ui/themes/jmht/less/style.css",
		       "ui/themes/jmht/less/header.css",
		       "ui/themes/jmht/less/layout.css",
		       "ui/themes/jmht/less/menu.css",
		       "ui/themes/jmht/less/tab.css",
		       "ui/css/custom.css" /*总是放到最后一个，用户自定义样式覆盖默认主题*/
        ],
        template : "ui/themes/jmht/template.html",
        colors : [{
        	id : "blue",
        	name : "天空蓝",
        	color : "#2FB0E2",
        	css : "ui/themes/new/blue/color.css"
        },{
        	id : "yellow",
        	name : "玛瑙黄",
        	color : "#D1D433",
        	css : "ui/themes/xjoa/yellow/color.css"
        },{
        	id : "red",
        	name : "深沉红",
        	color : "#B40000",
        	css : "ui/themes/xjoa/red/color.css"
        },{
        	id : "green",
        	name : "橄榄绿",
        	color : "#367500",
        	css : "ui/themes/xjoa/green/color.css"
        }],
        
        menuIcons: {
			DASHBOARD: 'icon-base icon-base-home',
			Components: 'icon-base icon-base-www',
			Tables: 'icon-base icon-base-option',
			Form: 'icon-base icon-base-report',
			MultiLevel: 'icon-base icon-base-copy'
		},
		
		menuLargeIcons: {
			DASHBOARD: 'icon-jm icon-jm-dashboard',
			Components: 'icon-jm icon-jm-components',
			Tables: 'icon-jm icon-jm-tables',
			Form: 'icon-jm icon-jm-form',
			MultiLevel: 'icon-jm icon-jm-multilevel'
		},
		
        init : function() {
        
        	// 菜单
        	var Menu = require("centit/centit.menu").init({
        		border : true,
        		width : 194,
        		layout : "mix",
        		hasSearch : false,
        		collapsible : false,
        		setSize : function(container) {
        			return container.closest("div.layout-body").height() - container.prev("div.easyui-panel").outerHeight() - 42;
        		},
        		loader : Config.menuLoader || function(data) {
        			data = [{
        				id : "DASHBOARD",
        				text : "我的首页",
        				url : Config.Url.DashboardUrl,
        				isDashboard : true
        			}].concat(data);
        			
        			return data;
        		},
        		
        		selected: 1,
        		
        		icons: Config.menuIcons || ThemeConfig.menuIcons,
        		
        		largeIcons: Config.menuLargeIcons || ThemeConfig.menuLargeIcons
        	});
        	// 菜单TAB
        	var MenuTab = require("centit/centit.tab").init("menu_tab", {
        		tabHeight : 40
        	}).open({
        		id : "DASHBOARD",
        		text : "我的首页",
        		url : Config.Url.DashboardUrl,
        		closable : false
        	}, true);
        	
        	// 工具栏
        	var Toolbar = require("centit/toolbar/centit.toolbar").init("header", {
        		height : 28
        	})
        	
        	// 全屏
//        	.add(require("centit/toolbar/centit.toolbar.fullscreen"))
        	// 修改密码对话框
//        	.add(require("centit/toolbar/centit.toolbar.modifypassword"))
        	// 皮肤颜色选择
//        	.add(require("centit/toolbar/centit.toolbar.skincolor"), ThemeConfig.colors)
        	// 帮助
        	.add(require("centit/toolbar/centit.toolbar.help"))
        	// 注销
        	.add(require("centit/toolbar/centit.toolbar.logout"));
        	// 滚动条
//        	$("#main_menu .panel-body").niceScroll({cursorcolor:"#BCBCBC"});
        	$("#side").before("<div id='sideTop'><i></i></div>");
        }
	};
	return ThemeConfig;
});
