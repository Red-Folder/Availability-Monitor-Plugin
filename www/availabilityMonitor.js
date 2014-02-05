/*
 * Copyright 2014 Red Folder Consultancy Ltd
 *   
 * Licensed under the Apache License, Version 2.0 (the "License");   
 * you may not use this file except in compliance with the License.   
 * You may obtain a copy of the License at       
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0   
 *
 * Unless required by applicable law or agreed to in writing, software   
 * distributed under the License is distributed on an "AS IS" BASIS,   
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   
 * See the License for the specific language governing permissions and   
 * limitations under the License.
 */



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
			cordova.plugins.AvailabilityMonitor.handleServiceResponse(data);
			
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
			cordova.plugins.AvailabilityMonitor.handleServiceResponse(data);

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
		};
		
		AvailabilityMonitor.prototype.getMonitorsFromDB = function(successCallback, failureCallback) {
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



