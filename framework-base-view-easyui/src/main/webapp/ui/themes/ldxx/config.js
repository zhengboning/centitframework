define(function(require) {
	var Config = require('config');

	var ThemeConfig = {
		id : "ldxx",
		
		name : "ldxx",
		
		css : [
            "ui/themes/easyui/default/easyui.css",
            "ui/css/icon.css","ui/css/icon-base.css",
            "ui/themes/ldxx/less/style.css",
            "ui/themes/ldxx/less/header.css",
            "ui/themes/ldxx/less/menu.css",
            "ui/themes/ldxx/less/layout.css",
            "ui/themes/ldxx/less/tab.css",
		    "ui/css/custom.css" /*总是放到最后一个，用户自定义样式覆盖默认主题*/
        ],
		
		template : "ui/themes/ldxx/template.html",
		
		colors : [{
			id : "sky_blue",
			name : "天空蓝",
			color: '#518CF0',
			css : "ui/themes/qui/blue/color.css"
		}],
		
//		menuIcons: {
//			DASHBOARD: 'icon-base icon-base-home',
//			Components: 'icon-base icon-base-www',
//			Tables: 'icon-base icon-base-option',
//			Form: 'icon-base icon-base-report',
//			MultiLevel: 'icon-base icon-base-copy'
//		},
		
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
