package com.red_folder.phonegap.plugin.availabilitymonitor.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class Conversion {
	// Example 2001-07-04T12:08:56.235-0700
	public static Date parseString(String dateString) {
		Date result = null;
		
		try {
		   result = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH).parse(dateString);
		} catch (Exception ex) {
			Log.d("Conversion.parseString", "Failed to convert string to date", ex);
		}
		
		return result;
	}
}
