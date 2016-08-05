define(['jquery', 'easyUI'], function($) {
	$.extend($.fn.calendar.defaults, {
		weeks : [ '��', 'һ', '��', '��', '��', '��', '��' ],
		months : [ 'һ��', '����', '����', '����', '����', '����', '����', '����', '����', 'ʮ��', 'ʮһ��', 'ʮ����' ],
	});
	
	$.extend($.fn.datetimebox.defaults, {
		currentText: '����',
		closeText: '�ر�',
		okText: 'ȷ��'
	});
	
	$.extend($.fn.datebox.defaults, {
		currentText: '����',
		closeText: '�ر�',
		okText: 'ȷ��',
		formatter: function(date){
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
		},
		parser : function(s) {
			if (!s)
				return new Date();
			var ss = s.split('-');
			var y = parseInt(ss[0], 10);
			var m = parseInt(ss[1], 10);
			var d = parseInt(ss[2], 10);
			return !isNaN(y) && !isNaN(m) && !isNaN(d) ? new Date(y, m - 1, d) : new Date();
		},
		validType : 'date'
	});
	
	$.extend($.fn.datetimebox.defaults, {
		validType : 'datetime'
	});
});