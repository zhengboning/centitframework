define(function(require) {
	var Config = require('config');

	var ThemeConfig = {
		id : "qui",
		
		name : "qui",
		
		css : [
            "ui/themes/easyui/default/easyui.css",
            "ui/css/icon.css","ui/css/icon-base.css",
            "ui/themes/qui/less/style.css",
            "ui/themes/qui/less/header.css",
            "ui/themes/qui/less/menu.css",
            "ui/themes/qui/less/layout.css",
            "ui/themes/qui/less/tab.css",
		    "ui/css/custom.css" /*总是放到最后一个，用户自定义样式覆盖默认主题*/
        ],
		
		template : "ui/themes/qui/template.html",
		
		colors : [{
			id : "sky_blue",
			name : "天空蓝",
			color: '#518CF0',
			css : "ui/themes/qui/blue/color.css"
		},{
			id : "jewelry_blue",
			name : "宝石蓝",
			color: '#0062BD',
			css : "ui/themes/qui/jewelry-blue/color.css"
		},{
			id : "dark_blue",
			name : "深蓝",
			color: '#2D7BB4',
			css : "ui/themes/qui/dark-blue/color.css"
		},{
			id : "light_blue",
			name : "淡蓝",
			color: '#E1EDFA',
			css : "ui/themes/qui/light-blue/color.css"
		},{
			id : "jade_green",
			name : "翠绿",
			color: '#47BC6E',
			css : "ui/themes/qui/jade-green/color.css"
		},{
			id : "blue_green",
			name : "蓝绿",
			color: '#0FF3F8',
			css : "ui/themes/qui/blue-green/color.css"
		},{
			id : "orange",
			name : "橘色",
			color: '#FF870F',
			css : "ui/themes/qui/orange/color.css"
		},{
			id : "red",
			name : "艳红",
			color: '#E03E2C',
			css : "ui/themes/qui/red/color.css"
		}],
		
		menuIcons: {
			DASHBOARD: 'icon-base icon-base-home',
			Components: 'icon-base icon-base-www',
			Tables: 'icon-base icon-base-option',
			Form: 'icon-base icon-base-report',
			MultiLevel: 'icon-base icon-base-copy'
		},
		
		init: function() {
			
			// 菜单
			var Menu = require('centit/centit.menu').init({
                layout: 'left',
                
				loader: Config.menuLoader || function(data) {
					data = [{
						id: 'DASHBOARD',
						text: '我的首页',
						url: Config.Url.DashboardUrl,
						isDashboard: true,
						collapsible: false
					}].concat(data);
					
					return data;
				},
				
				selected: 1,
				
				icons: Config.menuIcons || ThemeConfig.menuIcons
			});
			
			// 菜单TAB
			var MenuTab = require('centit/centit.tab').init('menu_tab')
			.open({
				id: 'DASHBOARD',
				text: '我的首页',
				url: Config.Url.DashboardUrl,
				closable: false
			}, true);
			
			// 工具栏
			var Toolbar = require('centit/toolbar/centit.toolbar').init('header', {
				height: 28,
				width: 330
			})
			// 全屏
			.add(require('centit/toolbar/centit.toolbar.fullscreen'))
			// 修改密码对话框
			.add(require('centit/toolbar/centit.toolbar.modifypassword'))
			// 皮肤颜色选择
			.add(require('centit/toolbar/centit.toolbar.skincolor'), ThemeConfig.colors)
			// 帮助
			.add(require('centit/toolbar/centit.toolbar.help'))
			// 注销
			.add(require('centit/toolbar/centit.toolbar.logout'));
			
			// 滚动条
//			$('#main_menu .panel-body').niceScroll({cursorcolor:"#BCBCBC"});
			
		}
	};
	
	return ThemeConfig;
});
