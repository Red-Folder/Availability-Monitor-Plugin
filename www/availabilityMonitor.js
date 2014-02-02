cordova.define("com.red_folder.phonegap.plugin.availabilitymonitor.service.MonitorService", function(require, exports, module) {



	function AvailabilityMonitorFactory() { };

	AvailabilityMonitorFactory.prototype.create = function () {
		var exec = cordova.require("cordova/exec");
			
		var AvailabilityMonitor = function () {
			var serviceName = 'com.red_folder.phonegap.plugin.availabilitymonitor.service.MonitorService';
			var factory = cordova.require('com.red_folder.phonegap.plugin.backgroundservice.BackgroundService')
			
			var service = factory.create(serviceName);
			this.getService = function() {
				return service;
			}
			
			var monitors = {};
			this.getMonitors = function() {
				return monitors;
			}		
			this.setMonitors = function(data) {
				monitors = data;
			}
			
			var serviceRunning = false;
			this.getServiceRunning = function() {
				return serviceRunning;
			}
			this.setServiceRunning = function(data) {
				serviceRunning = data;
			}

			var timerEnabled = false;
			this.getTimerEnabled = function() {
				return timerEnabled;
			}
			this.setTimerEnabled = function(data) {
				timerEnabled = data;
			}
		};
			
		AvailabilityMonitor.prototype.start = function(successCallback, failureCallback) {
			if (cordova.plugins.AvailabilityMonitor.hasMonitors())
				return cordova.plugins.AvailabilityMonitor.getService().startService(	function(data) {
																							cordova.plugins.AvailabilityMonitor.startServiceCallback(data, successCallback, failureCallback);
																						},
																						failureCallback);
			else
				failureCallback({ data: { ErrorMessage : "No monitor setup"} });
		};

		AvailabilityMonitor.prototype.startServiceCallback = function(data, successCallback, failureCallback) {
			//alert("startService: " + JSON.stringify(data));
			//successCallback(data);
			
			cordova.plugins.AvailabilityMonitor.handleServiceResponse(data);
			
			//if (data.ServiceRunning)
			if (cordova.plugins.AvailabilityMonitor.getServiceRunning())
				return cordova.plugins.AvailabilityMonitor.getService().enableTimer(	60 * 1000,
																						function(data) {
																							cordova.plugins.AvailabilityMonitor.enableTimerCallback(data, successCallback, failureCallback);
																						},
																						failureCallback);
			else
				failureCallback({ data: { ErrorMessage : "Service not started"} });
		};

		AvailabilityMonitor.prototype.enableTimerCallback = function(data, successCallback, failureCallback) {
			//alert("enableTimerCallback: " + JSON.stringify(data));

			cordova.plugins.AvailabilityMonitor.handleServiceResponse(data);

			//if (data.TimerEnabled)
			if (cordova.plugins.AvailabilityMonitor.getTimerEnabled())
				successCallback(data);
			else 
				failureCallback({ data: { ErrorMessage : "Timer not started"} });
		};

		AvailabilityMonitor.prototype.stop = function(successCallback, failureCallback) {
			return cordova.plugins.AvailabilityMonitor.getService().disableTimer(	function(data) {
																						cordova.plugins.AvailabilityMonitor.disableTimerCallback(data, successCallback, failureCallback);
																					},
																					failureCallback);
		};

		AvailabilityMonitor.prototype.disableTimerCallback = function(data, successCallback, failureCallback) {
			cordova.plugins.AvailabilityMonitor.handleServiceResponse(data);
			
			//if (!data.TimerEnabled)
			if (!cordova.plugins.AvailabilityMonitor.getTimerEnabled())
				return cordova.plugins.AvailabilityMonitor.getService().stopService(	function(data) {
																							cordova.plugins.AvailabilityMonitor.stopServiceCallback(data, successCallback, failureCallback);
																						},
																						failureCallback);
			else
				failureCallback({ data: { ErrorMessage : "Timer hasn't stopped"} });
		};

		AvailabilityMonitor.prototype.stopServiceCallback = function(data, successCallback, failureCallback) {
			cordova.plugins.AvailabilityMonitor.handleServiceResponse(data);

			//if (!data.ServiceRunning)
			if (!cordova.plugins.AvailabilityMonitor.getServiceRunning())
				successCallback(data);
			else 
				failureCallback({ data: { ErrorMessage : "Service hasn't stopped"} });
		};

				

		AvailabilityMonitor.prototype.handleServiceResponse = function(data) {
			if (data != null) {
				if (data.ServiceRunning != null)
					cordova.plugins.AvailabilityMonitor.setServiceRunning(data.ServiceRunning);
				
				if (data.TimerEnabled != null)
					cordova.plugins.AvailabilityMonitor.setTimerEnabled(data.TimerEnabled);
						
			}
		};
		
		AvailabilityMonitor.prototype.init = function(successCallback, failureCallback) {
			return this.getService().getStatus( function(data) {
													cordova.plugins.AvailabilityMonitor.getStatusCallback(data, successCallback, failureCallback);
												}, 
												failureCallback);
		};
		
		AvailabilityMonitor.prototype.getStatusCallback = function(data, successCallback, failureCallback) {
			cordova.plugins.AvailabilityMonitor.handleServiceResponse(data);
			
			// Now load the monitors
			cordova.plugins.AvailabilityMonitor.getMonitorsFromDB(successCallback, failureCallback);
			//successCallback();
		};
	
		AvailabilityMonitor.prototype.hasMonitors = function() {
			return (cordova.plugins.AvailabilityMonitor.getMonitors() != null && cordova.plugins.AvailabilityMonitor.getMonitors().length > 0);
		};
				
		AvailabilityMonitor.prototype.getMonitor = function(id) {
			if (cordova.plugins.AvailabilityMonitor.getMonitors() != null && cordova.plugins.AvailabilityMonitor.getMonitors().length < id)
				return cordova.plugins.AvailabilityMonitor.getMonitors()[id];
			else
				return null;
			/*
			return { 
						Monitors: [
							{
								Name: "Test",
								Frequency: 1,
								Type: "HTTP",
								NotifyWhenDown: false,
								NotifyWhenUp: false,
								UnavailableThreshold: 10,
								UnavailableCount: 0,
								ULR: "http://www.google.com"
							}
						]
					};
			*/	
		};
		
		AvailabilityMonitor.prototype.getMonitorsFromDB = function(successCallback, failureCallback) {
			//alert("Calling getMonuitors"); 
			return exec(	function(data) {
								cordova.plugins.AvailabilityMonitor.getMonitorsFromDBCallback(data, successCallback, failureCallback);
							},      
							failureCallback,      
							'AvailabilityMonitorPlugin', 
							'getMonitors',      
							[]);
		};
		
		AvailabilityMonitor.prototype.getMonitorsFromDBCallback = function(data, successCallback, failureCallback) {
			cordova.plugins.AvailabilityMonitor.setMonitors(data);
			successCallback();
		};

		AvailabilityMonitor.prototype.saveMonitor = function(monitor, successCallback, failureCallback) {
			return exec(	function(data) {
								cordova.plugins.AvailabilityMonitor.getMonitorsFromDB(successCallback, failureCallback);
							},      
							failureCallback,      
							'AvailabilityMonitorPlugin', 
							'saveMonitor',      
							[monitor]);
		};
		
		
		var monitor = new AvailabilityMonitor();
		return monitor;
		
	};
   
   	if (!cordova.plugins) cordova.plugins = {};
   	if (!cordova.plugins.AvailabilityMonitor) cordova.plugins.AvailabilityMonitor = new AvailabilityMonitorFactory().create();
   	
   	module.exports = cordova.plugins.AvailabilityMonitor; 

});

/*
		AvailabilityMonitor.Service.prototype.start = function(successCallback, failureCallback) {
			if (cordova.plugins.AvailabilityMonitor.startService.hasMonitor())
			
			return this.getService().getStatus( function(data) {
													cordova.plugins.AvailabilityMonitor.startService(data, successCallback, failureCallback);
												}, 
												failureCallback);
		};

		AvailabilityMonitor.prototype.startService = function(data, successCallback, failureCallback) {
			//alert("startService: " + JSON.stringify(data));
			successCallback(data);
		};

*/
/*
			return this.getService().getStatus( new	function(data , successCallback, failureCallback) {
													successCallback(data);
													alert("Hello");
											if (hasMonitor(data.Configuration) {
												this.startService(	function(data, successCallback, failureCallback) {
																		if (data.ServiceRunning) {
																			successCallback("Worked");
																		} else {
																			failureCallback("Failed");
																		}
																	},
																	failureCallback);
												if (data.ServiceRunning) {
												}
											} else {
												faulreCallback( { ErrorMessage : "Cannot start, no monitor defined" } );	
											},
									},
									failureCallback);
*/
/*
		AvailabilityMonitor.prototype.hasMonitor = function(configuration) {
			if (configuration != null)
				if (configuration.Monitors != null)
					if (configuration.Monitors[0] != null)
						return true;
			
			return false;
		};
		
		AvailabilityMonitor.prototype.getMonitor = function(configuration) {
			if (configuration != null)
				if (configuration.Monitors != null)
					if (configuration.Monitors[0] != null)
						return configuration.Monitors[0];
			
			return null;
		};
*/		
