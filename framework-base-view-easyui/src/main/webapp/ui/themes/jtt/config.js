define(function(require) {
    var Config = require('config');

    var ThemeConfig = {
        id : "test",

        name : "test",

        css : ["ui/themes/easyui/default/easyui.css", "ui/css/icon.css", "ui/themes/jtt/style.css"],

        template : "ui/themes/jtt/template.html",

        colors : [{
            id : "sky_blue",
            name : "天空蓝",
            color: '#518CF0',
            css : "ui/themes/jtt/default/color.css"
        }],

        init: function() {

            // 菜单
            var Menu = require('centit/centit.menu').init({
                collapsible: false,
                loader: function(data) {
                    /*return [{
                     id: 'DASHBOARD',
                     text: '我的首页',
                     url: Config.Url.DashboardUrl,
                     isDashboard: true,
                     collapsible: false
                     }].concat(data[0].children);*/

                    return data[0].children;
                }
            });

            // 菜单TAB
            var MenuTab = require('centit/centit.tab').init('menu_tab')
                .open({
                    id: 'DASHBOARD',
                    text: '我的首页',
                    url: Config.Url.DashboardUrl,
                    closable: false
                });

            // 工具栏
            var Toolbar = require('centit/toolbar/centit.toolbar').init('main_header', {
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
