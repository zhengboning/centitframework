define(function(require) {
	var Add = require('./unitinfo.add');
	
	var UnitInfoAdd = Add.extend(function() {
		
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			
			form.form('load', $.extend({}, this.object, {
				parentUnit: " "
			}))
				.form('disableValidation')
				.form('focus');
				
			if (!data.unitCode) {
				form.form('readonly', 'parentUnit');
			}
		};
		
		
	});
	
	return UnitInfoAdd;
});