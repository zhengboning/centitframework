define(function (require) {
    var Core = require('core/core');
    var Dialog = require('centit/centit.dialog');
    var Mustache = require('plugins/mustache.min');

    (function ($) {
        var _checkRows = function (type, opts) {
            var table = $(this);
            var rows = table.datagrid('getSelections');

            // 无需选中任何行
            if ('none' == type) {
//                table.datagrid('unselectAll');

                return;
            }
            // 必须且只能选中一行
            else if ('single' == type) {
                opts.warn = opts.warn || ('请选择一条记录再进行' + opts.text);

                if (rows.length < 1) {
                    return false;
                }
                else if (rows.length > 1) {
                    var index = table.datagrid('getRowIndex', rows[0]);
                    table.datagrid('unselectAll').datagrid('selectRow', index);
                }

                return rows[0];
            }
            // 必须选中一行或多行
            else if ('multiple' == type) {
                opts.warn = opts.warn || ('请至少选择一条记录再进行' + opts.text);

                if (rows.length < 1) {
                    return false;
                }

                return rows;
            }

            return false;
        }

        // 解析弹出框按钮
        var parseDialogButton = function (opts, mainCtrl, subCtrl) {
            var table = $(this), trigger = opts.trigger;

            var btn = {
                text: opts.text,
                iconCls: opts.iconCls,
                handler: function () {
                    var result = _checkRows.call(table, opts.trigger, opts);

                    if (result == false) {
                        $.messager.alert('提示信息', opts.warn, 'info');
                        return;
                    }

                    var title = opts.title, id = Mustache.render('dialog_{{id}}', {
                        id: subCtrl.id
                    });
                    if (opts.trigger == 'single') {
                        title = Mustache.render(opts.title, result);
                    }

                    Dialog.open({
                        id: id,
                        title: title,

                        href: opts.href,
                        width: opts.width,
                        height: opts.height,

                        // 提交按钮
                        okValue: opts.btnValue,
                        ok: function () {
                            return subCtrl.submit.call(subCtrl, subCtrl.panel, subCtrl.data, function () {
                                Dialog.close(id);
                            });
                        },

                        // 页面加载事件
                        onLoad: subCtrl.init,

                        // 关闭事件
                        onClose: function (data) {
                            subCtrl.onClose.call(subCtrl, table, data);
                        }
                    }, result);
                }
            }

            return btn;
        };

        // 解析确认按钮
        var parseConfirmButton = function (opts, mainCtrl, subCtrl) {
            var table = $(this), trigger = opts.trigger;

            var btn = {
                text: opts.text,
                iconCls: opts.iconCls,
                handler: function () {
                    var result = _checkRows.call(table, opts.trigger, opts);

                    if (result == false) {
                        $.messager.alert('提示信息', opts.warn, 'info');
                        return;
                    }

                    var title = opts.title;
                    // 多条记录，可以显示提示例如：确定删除王二等108条记录吗？
                    // 记录条数使用特别属性名 _length
                    if (opts.trigger == 'multiple') {
                        title = Mustache.render(title, $.extend({}, result[0], {
                            _length: result.length
                        }));
                    }
                    // 单条记录，可以显示提示例如：确定删除王二吗？
                    else if (opts.trigger == 'single') {
                        title = Mustache.render(title, $.extend({}, result));
                    }

                    $.messager.confirm('删除', title, function (r) {
                        if (r) {
                            subCtrl.submit.call(subCtrl, table, result);
                        }
                    });
                }
            }

            return btn;
        };

        // 解析自定义按钮
        var parseCustomButton = function (opts, mainCtrl, subCtrl) {
            var table = $(this), trigger = opts.trigger;

            var btn = {
                text: opts.text,
                iconCls: opts.iconCls,
                handler: function () {
                    var result = _checkRows.call(table, opts.trigger, opts);

                    if (result == false) {
                        $.messager.alert('提示信息', opts.warn, 'info');
                        return;
                    }

                    subCtrl.submit.call(subCtrl, table, result);
                }
            }

            return btn;
        };


        // 解析按钮
        var parseButton = function (table, mainCtrl) {
            var opts = _parseButtonOptions(this), subCtrl = mainCtrl.controllers[opts.rel];
            var btn;

            if (opts.target == 'dialog') {
                btn = parseDialogButton.call(table, opts, mainCtrl, subCtrl);
            }
            else if (opts.target == 'confirm') {
                btn = parseConfirmButton.call(table, opts, mainCtrl, subCtrl);
            }
            else if (opts.target == 'custom') {
                btn = parseCustomButton.call(table, opts, mainCtrl, subCtrl);
            }
            
            btn = $.extend({}, opts, btn, {
            	id: 'datagrid_toolbar_'+opts.rel,
            	controller: subCtrl
            });
            
            // 弹出框width和height属性和按钮本身的属性名字重复，在工具栏内按钮的高度和宽度基本固定，所以在解析完弹出框后删除这2个属性
            delete btn.width;
            delete btn.height;

            return btn;
        };

        var _parseButtonOptions = function (target) {
            var t = $(t);
            return $.extend({}, $.fn.linkbutton.parseOptions(target), $.parser.parseOptions(target, [
                'rel', 'trigger', 'target', 'title', 'warn', 'href', 'btnValue',
                {width: 'number', height: 'number'}
            ]));
        };

        // 构建工具栏
        var buildToolbar = function (toolbar, controller) {
            var table = $(this), panel = $(controller.panel);
            var buttons = [], btnMap = {};

            toolbar = panel.find(toolbar);
            toolbar.children().each(function () {
                var el = $(this);

                if (el.is('a')) {
                	var btn = parseButton.call(this, table, controller);
                	btnMap[btn.id] = btn;
                    buttons.push(btn);
                }
                else if (el.is('hr') || el.is('br') || el.is(':text')) {
                    buttons.push('-');
                }
            });

            // 在初始化的时候无法得到btn的dom对象，先暂存在map中，等渲染完后根据id重新获得，添加根据选中表格数据情况改变按钮不同状态
            table.data('buttons', btnMap);
            
            toolbar.remove();

            return buttons;
        };

        // 构建查询栏
        var buildSearch = function (search, controller) {
            var table = $(this), panel = $(controller.panel);

            search = panel.find(search);

            if (search.length) {
                var form = search.find('form'), searchBtn = search.find('.btn-search');

                // 帮顶查询事件
                searchBtn.on('click', function () {
                    table.datagrid('load', form.form('value'));
                });
            }
        };
        
        // 初始化编辑
        var enableEdit = function (opts) {
            var table = $(this);
            
            var trigger = opts.editTrigger;
            
            if (['onClickCell', 'onDblClickCell'].indexOf(trigger) > -1) {
            	var oldFn = opts[trigger];
            	
            	opts[trigger] = function(field, row) {
            		table.ctreegrid('beginEdit', field, row);
            		
            		if (oldFn) {
            			oldFn.apply(table, arguments);
            		}
            	}
            }
            
            bindEndEditEvent.call(table, opts.controller);
        };
        
        function bindEndEditEvent(ctrl) {
        	var table = this;
        
        	// 点击文档其他位置关闭编辑
        	$(document).off('click.'+ctrl.id).on('click.'+ctrl.id, function(e) {
        		var panel = table.datagrid('getPanel').find('.datagrid-view');
        		if ($(e.target).closest(panel).length == 0) {
	        		table.ctreegrid('endEdit');
        		}
        	});
        	
        	// 当panel关闭时取消事件
        	if (ctrl.panel) {
    			var panel = ctrl.panel.data('panel'), opts = panel.options;
    			var disattachEndEditEvent = panel.disattachEndEditEvent;
    			var oldOnBeforeClose = opts.onBeforeClose;
    			
    			// 防止重复绑定
    			if (!disattachEndEditEvent) {
    				if (oldOnBeforeClose) {
        				opts.onBeforeClose = function() {
        					$(document).off('click.'+ctrl.id);
        					oldOnBeforeClose.apply(this, arguments);
        				}
        			}
        			else {
        				opts.onBeforeClose = function() {
        					$(document).off('click.'+ctrl.id);
        				}
        			}
    				
        			panel.disattachEndEditEvent = true;
    			}
    		}
        }
        
        /**
         * 处理工具栏按钮禁用、启用
         */
        function renderButton(table) {
        	table = $(table);
        	var panel = table.datagrid('getPanel');
        	var buttonOpts = table.data('buttons');
        	
        	var buttons = panel.find('.datagrid-toolbar a').each(function() {
        		var btn = $(this);
        		
        		var opts = buttonOpts[btn.attr('id')];
        		if (opts) {
        			btn.data('trigger', opts.trigger);
        			btn.data('controller', opts.controller);
        		}
        	});
        	
        	table.data('datagrid').options.onSelect = renderButtonEvent;
        	table.data('datagrid').options.onSelectAll = renderButtonEvent;
        	table.data('datagrid').options.onUnselect = renderButtonEvent;
        	table.data('datagrid').options.onUnselectAll = renderButtonEvent;
        	table.data('datagrid').options.onLoadSuccess = renderButtonEvent;
        	
        	renderButtonEvent.call(table);
        }
        
        function renderButtonEvent() {
        	var table = $(this), panel = table.datagrid('getPanel');
        	var rows = table.datagrid('getSelections');
        	
        	panel.find('.datagrid-toolbar a').each(function() {
        		var btn = $(this), trigger = btn.data('trigger'), renderButton = btn.data('controller').renderButton;
        		var result;
        		
        		btn.linkbutton('enable');
        		
        		// 什么也没有选择
        		if (trigger == 'none') {
        			renderButton && (result = renderButton(btn));
        		}
        		// 选择了一条记录
        		else if (rows.length == 1 && (trigger == 'single' || trigger == 'multiple')) {
        			renderButton && (result = renderButton(btn, rows[0]));
        		}
        		// 选择了多条记录
        		else if (rows.length > 1 && trigger == 'multiple') {
        			renderButton && (result == renderButton(btn, rows));
        		}
        		else {
        			btn.linkbutton('disable');
        		}
        		
        		if (result === true) {
        			btn.linkbutton('enable');
        		}
        		else if (result === false) {
        			btn.linkbutton('disable');
        		}
        	});
        }
        
        function renderContextMenu(table, opts) {
        	table = $(table);
        
        	opts.onRowContextMenu = function(e, index, row) {
        		e.preventDefault();
        		table.treegrid('select', row[opts.idField]);
        	}
        }

        // 构建表格
        var buildGrid = function (target) {
            var opts = $.data(target, 'ctreegrid').options;

            if (opts.toolbar && typeof opts.toolbar == 'string') {
                opts.toolbar = buildToolbar.call(target, opts.toolbar, opts.controller);
            }

            if (opts.search) {
                buildSearch.call(target, opts.search, opts.controller);
            }

            if (opts.layoutH) {
            	if (parseInt(opts.layoutH)) {
            		opts.layoutH = parseInt(opts.layoutH);
            	}
            	
                opts.height = Core.height(opts.layoutH);
            }

            if (opts.editable) {
                enableEdit.call(target, opts);
            }
            
            renderContextMenu(target, opts);

            $(target).treegrid(opts).data('ctreegrid', {
                options: opts,
                editIndex: -1
            });
            
            renderButton(target);
        };

        $.fn.ctreegrid = function (options, param) {
            if (typeof options == 'string') {
                var method = $.fn.ctreegrid.methods[options];
                if (method) {
                    return method.apply(this[0], Array.prototype.slice.call(arguments, 1));
                } else {
                    return this.treegrid(options, param);
                }
            }

            options = options || {};
            return this.each(function () {
                var state = $.data(this, 'ctreegrid');
                if (state) {
                    $.extend(state.options, options);
                } else {
                    $.data(this, 'ctreegrid', {
                        options: $.extend({}, $.fn.ctreegrid.defaults, $.fn.ctreegrid.parseOptions(this), options)
                    });
                }

                buildGrid(this);
            });
        };

        $.fn.ctreegrid.parseOptions = function (target) {
            var t = $(t);
            return $.extend({}, $.fn.datagrid.parseOptions(target), $.parser.parseOptions(target, [
                'toolbar', 'search', 'layoutH', 'editTrigger',
                {editable: 'boolean'}
            ]));
        };

        $.fn.ctreegrid.defaults = {
        	width: '100%',	
            method: 'get',
            autoRowHeight: false,
            pagination: false,
            rownumbers: true,
            singleSelect: true,
            editable: false, editTrigger: 'onDblClickCell',
            loadFilter: function (data) {
                if (data.data) {
                    return data.data;
                }

                return data;
            }
        };

        $.fn.ctreegrid.methods = {
            // 开始编辑
            beginEdit: function (field, row) {
                var jq = $(this);
                var target = jq.data('ctreegrid'), opts = target.options, editIndex = target.editIndex;
                
                var id = row[opts.idField];

                // 开始编辑前校验，增加支持点击cell时的校验
                var onBeforeEdit = opts.onBeforeEdit;
                if (onBeforeEdit) {
                    if (false === onBeforeEdit.call(jq, row)) {
                        return false;
                    }
                }

                // 没有正在编辑的对象
                if (!editIndex) {
                    jq.treegrid('beginEdit', id);
                }
                // 正在编辑的对象校验通过
                else if (jq.ctreegrid('endEdit', editIndex)) {
                    jq.treegrid('beginEdit', id);
                }
                // 正在编辑的对象校验没通过
                else {
                    return false;
                }

                jq.ctreegrid('focusEditor', id, field);

                // 重新设定编辑对象的index
                target.editIndex = id;
            },
            
            // 聚焦编辑对象
            focusEditor: function (id, field) {
            	var jq = $(this);

                // 若没有传递field值，则选中所在行第一个能编辑的cell
                if (!field) {
                    var fields = jq.datagrid('getColumnFields', true).concat(jq.datagrid('getColumnFields'));
                    for (var i = 0; i < fields.length; i++) {
                        var col = jq.datagrid('getColumnOption', fields[i]);
                        if (col.editor) {
                            field = fields[i];
                            break;
                        }
                    }
                }

                var ed = jq.treegrid('getEditor', {id: id, field: field});
                if (ed) {
                    ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                }
            },
            
            // 聚焦错误对象
            focusInvalidEditor: function(index) {
            	var tr = $.data(this, "datagrid").options.finder.getTr(this, index);
		        tr.find(".validatebox-invalid:first").focus();
            },

            // 结束编辑
            endEdit: function (id) {
            	var jq = $(this);

                if (id == undefined) {
                    id = jq.data('ctreegrid').editIndex;
                }

                // 有正在编辑的行
                if (id) {
                    if (jq.ctreegrid('validateRow', id)) {
                        jq.treegrid('endEdit', id);
                        return true;
                    }
                    else {
                        return false;
                    }
                }

                return true;
            },

            // 校验正在编辑的行
            validateRow: function (id) {
            	var jq = $(this);

                if (!jq.treegrid('validateRow', id)) {
                    jq.treegrid('scrollTo', id)
                        .treegrid('select', id);

                    	jq.ctreegrid('focusInvalidEditor', id);
                    return false;
                }

                return true;
            }
        };
    })(jQuery);

});
