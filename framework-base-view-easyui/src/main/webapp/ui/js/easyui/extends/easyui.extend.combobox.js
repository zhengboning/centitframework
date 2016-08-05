define(['jquery', 'core/core', 'config', 'plugins/mustache.min', 'core/cache', 'core/cache.dictionary', 'easyUI'], 
	function($, Core, Config, Mustache, Cache, DictionaryCache) {
	
	/**
	 * 加载数据函数
	 */
	var loader = function(params, success, error) {
    	var combobox = this;
    	var opts = $(this).combobox("options");
    	
    	var type = opts.target;
    	
    	switch(type) {
    		case 'dictionary':
    			return loader4Dictionary.call(combobox, opts, params, success, error);
    		case 'unit':
    			opts.key = 'Units';
    			return loader4Cache.call(combobox, opts, params, success, error);
			case 'user':
    			opts.key = 'Users';
    			return loader4Cache.call(combobox, opts, params, success, error);
			case 'role':
    			opts.key = 'Roles';
    			return loader4Cache.call(combobox, opts, params, success, error);
			case 'cache':
    			return loader4Cache.call(combobox, opts, params, success, error);
    	}
    	
    	if (!opts.url) return;
    	Core.ajax(opts.url, {
    		data: params
    	}).then(function (data) {
            success(data || []);
        }, function () {
            error.apply(combobox, arguments);
        });
    };
    
    /**
     * 加载数据字典数据
     */
    var loader4Dictionary = function(options, params, success, error) {
    	var key = options.key;
    	
    	var data = DictionaryCache.get(key);
    	
    	// 没有数据字典缓存数据
    	if (!data) {
    		Core.ajax(Mustache.render(Config.URL.Dictionary, {
    			code: key
    		})).then(function (data) {
    			data = data || [];
    			DictionaryCache.save(key, data);
	            success(data);
	        }, function () {
	            error.apply(this, arguments);
	        });
    	}
    	else {
    		success(data);
    	}
    };
    
    /**
     * 缓存
     */
    var loader4Cache = function(options, params, success, error) {
    	var url = options.url, key = options.key;
    	
    	var data = Cache.get(key);
    	var cacheUrl = {Units:'system/cp/allunits/A',
    					Roles : 'system/cp/roleinfo/G',  
    					Users :'system/cp/alluser/A'};
    	// 没有缓存数据
    	if (!data) {
    		Core.ajax(Mustache.render(cacheUrl[key]), {
    			data: params
    		}).then(function (data) {
    			data = data || [];
    			Cache.save(key, data);
	            success(data);
	        }, function () {
	            error.apply(this, arguments);
	        });
    	}
    	else {
    		success(data);
    	}
    };

	$.extend(true, $.fn.combobox.defaults, {
		filter: function(q, row) {
			var opts=$(this).combobox("options");
			return row[opts.textField].has(q) || row[opts.valueField].has(q);
		},
		
		selectOnNavigation: false,
		
		validType: 'combobox',
		
		method: 'get',
		
		onBeforeLoad: function(param) {
			var opts = $.parser.parseOptions(this, ['target', 'key']);
			var fieldOpts = $.parser.parseOptions(this, ['valueField', 'textField']);
			var comboboxOptions = $.data(this, 'combobox').options;
			
			// 根据不同类型有不同的 valueField 和 textField
            if (opts.target) {
            	switch(opts.target) {
            		case 'dictionary':
            			opts.valueField = opts.valueField || 'dataCode';
            			opts.textField = opts.textField || 'dataValue';
            			break;
            		case 'unit':
            			opts.valueField = opts.valueField || 'unitCode';
            			opts.textField = opts.textField || 'unitName';
            			break;
        			case 'user':
    					opts.valueField = opts.valueField || 'userCode';
            			opts.textField = opts.textField || 'userName';
            			break;
        			case 'role':
        				opts.valueField = opts.valueField || 'roleCode';
            			opts.textField = opts.textField || 'roleName';
            			break;
            	}
            }
            
            // 如果节点上有 dataCode 或 dataValue 以指定的为准，否则使用默认值
            $.extend(comboboxOptions, opts, fieldOpts);
		},
		
		loader : loader
	});
	
	
	
});