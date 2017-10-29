(function(that){
	that.config={
		server: ''
	}

	var cdn="http://nobibi.site/scripts/";
	require.config({
		baseUrl : cdn,
		//baseUrl : "http://nobibi.site/scripts/",
	    paths : {
	        "jquery" : ["jquery/jquery-3.2.1.min"],
	        "bootstrap": ["bootstrap/js/bootstrap",cdn+"bootstrap/js/bootstrap"],
	        "layer": ["layer/layer",cdn+"layer/layer"],
	        "c":["common/common",cdn+"/common/common"],
	        "moment":["moment/moment"],
	        "daterangepciker":["bootstrap/js/daterangepicker"],
	        "holiday":["dateapi/holiday"]
	    },
	    preload:['jquery','bootstrap','layer','c'],
	    shim:{
	    	bootstrap:{
	    		deps:['jquery']
	    	},
	    	c:{
	    		deps:['jquery','layer']
	    	}
	    	// daterangepciker:['jquery','moment','bootstrap']
	    }
	});
	console.log('require config');
	require(['bootstrap']);
})(window)