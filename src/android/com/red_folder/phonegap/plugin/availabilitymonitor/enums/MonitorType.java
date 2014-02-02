package com.red_folder.phonegap.plugin.availabilitymonitor.enums;

public enum MonitorType {
	HTTP;
	
	public int toInt() {
		return 1;
	}
	
	public static MonitorType fromInt(int ordinal) {
		return MonitorType.HTTP;
	}
	
	public static MonitorType parse(String type) {
		return MonitorType.HTTP;
	}
}

