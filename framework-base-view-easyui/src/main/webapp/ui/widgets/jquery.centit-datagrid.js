define(function (require) {
	require('plugins/es5-array');
	require('plugins/printThis');

	var $ = require('jquery');
    var Core = require('core/core');
    var Dialog = require('centit/centit.dialog');
    
    // 特殊需要实例化
    var FilterCache = require('core/filter/filter.cache');
    FilterCache = new FilterCache();
    
    var FilterDate = require('core/filter/filter.date');
    var FilterDictionay = require('core/filter/filter.dictionary');
    var FilterColumn = require('core/filter/filter.column');
    var FilterUserInfo = require('core/filter/filter.userinfo');
    var FilterRoleInfo = require('core/filter/filter.roleinfo');
    var FilterUnitInfo = require('core/filter/filter.unitinfo');
    
    var Mustache = require('plugins/mustache.min');
    var Events = require('core/events');
    
    function getColumns(_5c4) {
	    var _5c7 = [];
	    var _5c8 = [];
	    $(_5c4).children("thead").each(function() {
	        var opt = $.parser.parseOptions(this, [{frozen: "boolean"}]);
	        $(this).find("tr").each(function() {
	            var cols = [];
	            $(this).find("th").each(function() {
	                var th = $(this);
	                var col = $.extend({}, $.parser.parseOptions(this, ["field", "align", "halign", "order", "width", "format", {sortable: "boolean",checkbox: "boolean",resizable: "boolean",fixed: "boolean"}, {rowspan: "number",colspan: "number"}]), {title: (th.html() || undefined),hidden: (th.attr("hidden") ? true : undefined),formatter: (th.attr("formatter") ? eval(th.attr("formatter")) : undefined),styler: (th.attr("styler") ? eval(th.attr("styler")) : undefined),sorter: (th.attr("sorter") ? eval(th.attr("sorter")) : undefined)});
	                if (col.width && String(col.width).indexOf("%") == -1) {
	                    col.width = parseInt(col.width);
	                }
	                if (th.attr("editor")) {
	                    var s = $.trim(th.attr("editor"));
	                    if (s.substr(0, 1) == "{") {
	                        col.editor = eval("(" + s + ")");
	                    } else {
	                        col.editor = s;
	                    }
	                }
	                cols.push(col);
	            });
	            opt.frozen ? _5c7.push(cols) : _5c8.push(cols);
	        });
	    });
	    
	    // 新增自定义格式过滤器
	    [].concat(_5c7).concat(_5c8).reduce(function(array, obj){
	    	return array.concat(obj)
	    }, []).forEach(function(col) {
	    	var format = col.format;
	    	
	    	if (format) {
	    		col.formatter = getFormatter(format);
	    	}
	    });

	    return [_5c7, _5c8];
	}
    
	// 格式过滤器
	function getFormatter(format) {
		// 类似于这样的format：
		// Dictionary:CatalogStyle
		// Date:yyyy-mm-dd
		// Column:anotherField
		// User:userName?
		// Unit:unitName?
		// Role:roleName?
		// Cache:cacheName,textField?
		var matcher = format.match(/(.*?):(.*)/);
		var name, args = [], filterHandle;
		
		if (matcher) {
			name = matcher[1];
			args = matcher[2].split(',');
		}
		else {
			name = format;
		}
		
		switch(name) {
			case 'Dictionary':
				filterHandle = FilterDictionay.convert;
				break;
			case 'Column':
				filterHandle = FilterColumn.convert;
				break;
			case 'User':
				filterHandle = FilterUserInfo.convert;
				
				// 该过滤方法需要2个参数
				if (!args.length) {
					args.length = 2;
				}
				else if (args.length == 1) {
					args.unshift(undefined);
				}

				break;
			case 'Unit':
				filterHandle = FilterUnitInfo.convert;
				
				// 该过滤方法需要2个参数
				if (!args.length) {
					args.length = 2;
				}
				else if (args.length == 1) {
					args.unshift(undefined);
				}
				
				break;
			case 'Role':
				filterHandle = FilterRoleInfo.convert;
				
				// 该过滤方法需要2个参数
				if (!args.length) {
					args.length = 2;
				}
				else if (args.length == 1) {
					args.unshift(undefined);
				}
				
				break;
			case 'Cache': 
				filterHandle = FilterCache.convert;
				
				if (args.length == 1) {
					args.push(undefined);
				}
				
				break;
			case 'Date':
				filterHandle = FilterDate.convert;
			default:
				break;
		}
		
		return function(value, row, index) {
			if (filterHandle) {
				return filterHandle.apply(this, args.concat([value, row, index]));
			}
			else {
				return value;
			}
		}
	}

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
                var _rows = table.datagrid('getChecked');
                if (_rows && _rows.length > 0) {
                	rows = _rows;
                }

                if (rows.length < 1) {
                    return false;
                }

                return rows;
            }

            return false;
        };

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

                    if (subCtrl.beforeLoad && subCtrl.beforeLoad.call(this, result) === false) {
                        return;
                    }

                    var title = opts.title, url = opts.href, id = Mustache.render('dialog_{{id}}', {
                        id: subCtrl.id
                    });
                    if (opts.trigger == 'single') {
                        title = Mustache.render(opts.title, result);
                        url = Mustache.render(url, result);
                    }
                    
                    var dialogOpts = {
                        id: id,
	                    title: title,
	
	                    href: url,
	                    width: opts.width,
	                    height: opts.height,
	                    
	                    // 页面加载事件
	                    onLoad: subCtrl.init,
	
	                    // 关闭事件
	                    onClose: function (data) {
	                        subCtrl.onClose.call(subCtrl, table, data);
	                        window.SUBMIT_BTN = null;
	                    }
	                };
                    
	                // 添加对话框按钮
                    addDialogButton(dialogOpts, opts, subCtrl, function () {
                        Dialog.close(id);
                    	window.SUBMIT_BTN = null;
                    });
                    
                    addDialogTool(dialogOpts, opts, subCtrl);

                    Dialog.open(dialogOpts, result);
                }
            };

            return btn;
        };
        
        function addDialogTool(dialogOpts, opts, subCtrl) {
        	var tools = dialogOpts.tools = dialogOpts.tools || [];
        
        	if (opts.print) {
        		tools.push({
        			iconCls: 'icon-print',
	    			handler: printHandler
        		});
    		}
        	
        	function printHandler() {
        		var panel = $(this).closest('.panel.window').find('>.panel-body');
        		
        		panel.find('form.print').printThis({
        			title: 'lalalalalaal',
        			formValues: false
        		});
        	}
        }
        
        var addDialogButton = function(dialogOpts, opts, subCtrl, closeCallback) {
        	var buttons = opts.buttons;
        	
        	// 等于false对话框只有一个取消按钮
        	if (buttons === 'false') {
        		return;
        	}
        	// 没有buttons值，默认添加提交按钮
        	else if (!buttons) {
        		dialogOpts = $.extend(dialogOpts, {
        			okValue: opts.btnValue,
	                ok: function() {
        				window.SUBMIT_BTN = this;
	        			return subCtrl.submit.call(subCtrl, subCtrl.panel, subCtrl.data, closeCallback);
        			}
        		});
        	}
        	else {
        		try {
        			buttons = eval(buttons);
        			
        			// 添加按钮
        			buttons.forEach(function(button) {
        				var method = subCtrl[button.method];
        				
        				if (!method) {
        					console.warn(Mustache.render('按钮【{{text}}】对应控制器【{{id}}】缺少方法：【{{method}}】', {
        						text: button.text,
	    						id: subCtrl.id,
	    						method: button.method
	    					}));
        					return true;
        				}
        				
        				// 按钮点击事件
        				button.handler = function() {
        					window.SUBMIT_BTN = this;
        				
        					if (method.call(subCtrl, subCtrl.panel, subCtrl.data, closeCallback) == false) {
        	                    return;
        	                }
        	
        	                closeCallback();
        				}
        			});
        			
        			dialogOpts.buttons = buttons;
        		}
        		catch(e) {
        			console.error('解析对话框按钮失败：' + buttons);
        		}
        	}
        }

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

                    $.messager.confirm(opts.text, title, function (r) {
                        if (r) {
                            subCtrl.submit.call(subCtrl, table, result);
                        }
                    });
                }
            };

            return btn;
        };

        // 解析自定义按钮
        var parseCustomButton = function (opts, mainCtrl, subCtrl) {
            var table = $(this);

            var btn = {
                text: opts.text,
                iconCls: opts.iconCls,
                handler: function () {
                    var result = _checkRows.call(table, opts.trigger, opts);

                    if (result == false) {
                        $.messager.alert('提示信息', opts.warn, 'info');
                        return;
                    }

                    // this传递按钮对象
                    subCtrl.submit.call(subCtrl, table, result, this);
                }
            };

            return btn;
        };

        // 解析菜单tab按钮
        var parseTabButton = function(opts, mainCtrl, subCtrl) {
            var table = $(this);

            var btn = {
                text: opts.text,
                iconCls: opts.iconCls,
                handler: function () {
            		var btn = $(this);
            		
            		// 清空tab面板缓存的值
            		if (btn.data('panel')) {
            			btn.data('panel').data('result', null);
            		}
            		
                    var result = _checkRows.call(table, opts.trigger, opts);

                    if (result == false) {
                        $.messager.alert('提示信息', opts.warn, 'info');
                        return;
                    }

                    if (subCtrl.beforeLoad && subCtrl.beforeLoad.call(this, result) === false) {
                        return;
                    }

                    var title = opts.title, url = opts.href, id = subCtrl.id;
                    
                    if (opts.trigger == 'single') {
                        title = Mustache.render(opts.title, result);
                        url = Mustache.render(url, result);
                    }
                    		
                    Events.trigger('open.MenuTab', {
                    	id: id,

						text: title,
						
						url: url,
						
						onLoad: function() {
                    		var panel = $(this);
                    		
                    		// 缓存值，当tab刷新时不用再从表格取值
							var result = panel.data('result') || _checkRows.call(table, opts.trigger, opts);
							panel.data('result', result);
							
							btn.data('panel', panel);
							
							subCtrl.init.call(this, $(this), result);
						}
                    });
                }
            };

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
            else if (opts.target == 'tab') {
                btn = parseTabButton.call(table, opts, mainCtrl, subCtrl);
            }
            else if (opts.target == 'custom') {
                btn = parseCustomButton.call(table, opts, mainCtrl, subCtrl);
            }
            
            btn = $.extend({}, opts, btn, {
            	id: opts.id ? opts.id : opts.rel,
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
                'rel', 'trigger', 'target', 'title', 'warn', 'href', 'btnValue', 'buttons',
                {width: 'number', height: 'number', print: 'boolean'}
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
                
                search.keydown(function(e) {
                	if (13 == event.keyCode) {
                		searchFn();
                	}
                })
                
            	// 帮顶查询事件
                searchBtn.on('click', function() {
                	searchFn();
                });
            }
            
            var searchFn = function () {
            	var isValid = form.form('validate');
                if (isValid) {
                	table.datagrid('load', form.form('value'));
                }
            };
        };
        
        // 初始化编辑
        var enableEdit = function (opts) {
            var table = $(this);
            
            var trigger = opts.editTrigger;
            
            if (['onDblClickCell', 'onClickCell'].indexOf(trigger) > -1) {
            	var oldFn = opts[trigger];
            	
            	opts[trigger] = function(index, field, value) {
            		// 如果cell没有editor不响应点击编辑
            		var colOpt = table.datagrid('getColumnOption', field);
            		if (!colOpt || !colOpt.editor) return;
            		
            		table.cdatagrid('beginEdit', index, field, value);
            		
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
        	$(document).off('click.'+ctrl.id).on('click.'+ctrl.id, ctrl.panel, function(e) {
        		
        		var datagrid = $.data(table[0], 'datagrid');
        		
        		// 如果表格已经被销毁，取消事件
        		if (!datagrid) {
        			$(document).off('click.'+ctrl.id);
        			return;
        		}
        		
        		var tablePanel = table.datagrid('getPanel').find('.datagrid-view');
        		if (!tablePanel.is(':visible')) return;
        	
        		// 点击表格外， 且在panel内任意地方触发取消编辑事件
        		if ($(e.target).closest(tablePanel).length == 0 && $(e.target).closest(ctrl.panel.closest('.panel')).length) {
	        		table.cdatagrid('endEdit');
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
        		table.datagrid('selectRow', index);
        	}
        }
        
        // 绑定双击表格默认的提交事件
        function bindDefaultAction(table, opts) {
        	var action = opts.action;
        	if (!action) return;

        	table = $(table);
        	var panel = table.datagrid('getPanel');
        	
        	
        	table.data('datagrid').options.onDblClickRow = function() {
        		panel.find('.datagrid-toolbar').find('#'+action).click();
        	}
        }

        // 构建表格
        var buildGrid = function (target) {
            var opts = $.data(target, 'cdatagrid').options;

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
            
            // 预先提取columns信息
            if (!opts.columns) {
            	var columns = getColumns(target);
                opts.frozenColumns = columns[0];
                opts.columns = columns[1];
            }
             
            renderContextMenu(target, opts);

            $(target).datagrid(opts).data('cdatagrid', {
                options: opts,
                editIndex: -1
            });
            
            renderButton(target);
            
            bindDefaultAction(target, opts);
        };

        $.fn.cdatagrid = function (options, param) {
            if (typeof options == 'string') {
                var method = $.fn.cdatagrid.methods[options];
                if (method) {
                    return method.apply(this[0], Array.prototype.slice.call(arguments, 1));
                } else {
                    return this.datagrid(options, param);
                }
            }

            options = options || {};
            return this.each(function () {
                var state = $.data(this, 'cdatagrid');
                if (state) {
                    $.extend(state.options, options);
                } else {
                    $.data(this, 'cdatagrid', {
                        options: $.extend({}, $.fn.cdatagrid.defaults, $.fn.cdatagrid.parseOptions(this), options)
                    });
                }

                buildGrid(this);
            });
        };

        $.fn.cdatagrid.parseOptions = function (target) {
            var t = $(t);
            return $.extend({}, $.fn.datagrid.parseOptions(target), $.parser.parseOptions(target, [
                'toolbar', 'search', 'layoutH', 'editTrigger', 'action',
                {editable: 'boolean'}
            ]));
        };

        $.fn.cdatagrid.defaults = {
        	width: '100%',	
            method: 'get',
            pagination: true,
            rownumbers: true,
            singleSelect: true,
            checkOnSelect: false, selectOnCheck: false,
            editable: false, editTrigger: 'onDblClickCell',
            loadFilter: function (data) {
                if (data.objList) {
                    var rows = data.objList || [];
                    var page = data.pageDesc;

                    return {
                        rows: rows,
                        total: page.totalRows
                    };
                }
                else if ($.isArray(data)) {
                    return {
                        rows: data,
                        total: data.length
                    };
                }

                return data;
            }
        };

        $.fn.cdatagrid.methods = {
            // 开始编辑
            beginEdit: function (index, field, value) {
                var jq = $(this);
                var target = jq.data('cdatagrid'), opts = target.options, editIndex = target.editIndex;
                var row = jq.datagrid('getRowData', index);

                // 开始编辑前校验，增加支持点击cell时的校验
                var onBeforeEdit = opts.onBeforeEdit;
                if (onBeforeEdit) {
                    if (false === onBeforeEdit.call(jq, index, row, field, value)) {
                        return false;
                    }
                }

                // 没有正在编辑的对象
                if (editIndex < 0) {
                    jq.datagrid('beginEdit', index);
                }
                // 正在编辑的对象校验通过
                else if (jq.cdatagrid('endEdit', editIndex)) {
                    jq.datagrid('beginEdit', index);
                }
                // 正在编辑的对象校验没通过
                else {
                    return false;
                }

                jq.cdatagrid('focusEditor', index, field);

                // 重新设定编辑对象的index
                target.editIndex = index;
            },
            
            // 聚焦编辑对象
            focusEditor: function (index, field) {
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

                var ed = jq.datagrid('getEditor', {index: index, field: field});
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
            endEdit: function (index) {
            	var jq = $(this);

                if (index == undefined) {
                    index = jq.data('cdatagrid').editIndex;
                }

                // 有正在编辑的行
                if (index > -1) {
                    if (jq.cdatagrid('validateRow', index)) {
                        jq.datagrid('endEdit', index);
                        return true;
                    }
                    else {
                        return false;
                    }
                }

                return true;
            },

            // 校验正在编辑的行
            validateRow: function (index) {
            	var jq = $(this);

                if (!jq.datagrid('validateRow', index)) {
                    jq.datagrid('scrollTo', index)
                        .datagrid('selectRow', index);

                    	jq.cdatagrid('focusInvalidEditor', index);
                    return false;
                }

                return true;
            }
        };
    })(jQuery);

});
