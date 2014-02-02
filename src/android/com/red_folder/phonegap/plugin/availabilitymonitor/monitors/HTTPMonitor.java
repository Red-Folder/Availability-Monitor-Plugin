package com.red_folder.phonegap.plugin.availabilitymonitor.monitors;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;

import android.util.Log;

import com.red_folder.phonegap.plugin.availabilitymonitor.enums.MonitorType;
import com.red_folder.phonegap.plugin.availabilitymonitor.interfaces.IMonitor;
import com.red_folder.phonegap.plugin.availabilitymonitor.interfaces.IMonitorModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.interfaces.IResultModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.models.HTTPMonitorModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.models.HTTPResultModel;

public class HTTPMonitor implements IMonitor {

	private final static String TAG = HTTPMonitor.class.getSimpleName();
	private final static int CONNECTION_TIMEOUT = 30 * 1000;		// 30 Seconds

	@Override
	public IResultModel run(IMonitorModel monitor) {
		HTTPResultModel result = null;
		
		if (monitor != null) {
			
			if (monitor.getType() == MonitorType.HTTP) {
		
				HTTPMonitorModel httpMonitor = (HTTPMonitorModel)monitor;
				
				result = new HTTPResultModel();
				result.setMonitorId(httpMonitor.getId());
				result.setType(httpMonitor.getType());
				
				try {
					URL url = new URL(httpMonitor.getURL());

					URLConnection connection = url.openConnection();
					if (httpMonitor.getURL().toLowerCase().startsWith("https:"))
					{
						// HTTPS specific code
						// TODO
					}

					// Set a timeout
					connection.setConnectTimeout(CONNECTION_TIMEOUT);

					connection.connect();

					int responseCode = ((HttpURLConnection)connection).getResponseCode();

					if (responseCode == HttpStatus.SC_OK) {
						Log.d(TAG, "Successfully checked");
					} else {
						Log.d(TAG, "Failed check " + responseCode );
					}
					
					result.setResponseCode(responseCode);

					((HttpURLConnection)connection).disconnect();
					connection = null;
				} catch (SocketTimeoutException e) {
					Log.d(TAG, "Connection timed out");
					result.setResponseCode(-100);
				} catch (ClientProtocolException e) {
					Log.d(TAG, "Failed execute HTTP request - " + e.getMessage());
					result.setResponseCode(-200);
				} catch (IOException e) {
					Log.d(TAG, "Failed execute HTTP request - " + e.getMessage());
					result.setResponseCode(-300);
				}
				
				result.setTimestamp(new Date());
			}
		}

		return result;
	}

}
