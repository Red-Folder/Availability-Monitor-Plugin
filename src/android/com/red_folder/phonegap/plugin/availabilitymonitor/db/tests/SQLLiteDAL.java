package com.red_folder.phonegap.plugin.availabilitymonitor.db.tests;

import java.util.Date;

import com.red_folder.phonegap.plugin.availabilitymonitor.enums.MonitorType;
import com.red_folder.phonegap.plugin.availabilitymonitor.interfaces.IMonitorModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.interfaces.IResultModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.models.HTTPMonitorModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.models.HTTPResultModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.utils.Conversion;

import android.content.Context;
import android.test.InstrumentationTestCase;


public class SQLLiteDAL extends InstrumentationTestCase {

	private Context mContext = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		this.mContext = this.getInstrumentation().getContext();

		TestHelper.emptyDB(this.mContext);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
		TestHelper.emptyDB(this.mContext);
	}
	
	
	public void testAddMonitor() {
		HTTPMonitorModel original = new HTTPMonitorModel();
		
		original.setName("Test Monitor");
		original.setFrequency(1);
		original.setType(MonitorType.HTTP);
		original.setNotifyWhenDown(false);
		original.setNotifyWhenUp(false);
		original.setUnavailableThreshold(5);
		original.setUnavailableCount(0);
		original.setCreated(new Date());
		original.setAmended(new Date());
		original.setURL("http://www.google.com");
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL dal = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL(this.mContext);
		
		long id = dal.addMonitor(original);

		assertTrue("If inserted, then we should get back an ID", id > 0);
	}

	
	public void testDeleteMonitor() {
		HTTPMonitorModel original = new HTTPMonitorModel();
		
		original.setName("Test Monitor");
		original.setFrequency(1);
		original.setType(MonitorType.HTTP);
		original.setNotifyWhenDown(false);
		original.setNotifyWhenUp(false);
		original.setUnavailableThreshold(5);
		original.setUnavailableCount(0);
		original.setCreated(new Date());
		original.setAmended(new Date());
		original.setURL("http://www.google.com");
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL dal = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL(this.mContext);
		
		long id = dal.addMonitor(original);

		HTTPMonitorModel stored = (HTTPMonitorModel)dal.getMonitor(id);
		assertHTTPMonitorEquals(original, stored);
		
		boolean result = dal.deleteMonitor(stored);
		assertTrue("If deleted, we should have true returned", result);
		
		stored = (HTTPMonitorModel)dal.getMonitor(id);
		assertNull("If the model has been deleted, we should get null",stored);
	}

	public void testDeleteMonitorAndResults() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL dal = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL(this.mContext);
		
		HTTPMonitorModel monitor = new HTTPMonitorModel();
		monitor.setName("Test");
		monitor.setFrequency(1);
		monitor.setType(MonitorType.HTTP);
		monitor.setNotifyWhenDown(false);
		monitor.setNotifyWhenUp(false);
		monitor.setCreated(new Date());
		monitor.setAmended(new Date());
		monitor.setUnavailableThreshold(10);
		monitor.setUnavailableCount(5);
		monitor.setURL("http://www.google.com");
		
		long monitorId = dal.addMonitor(monitor); 
		long monitorId2 = dal.addMonitor(monitor); 

		HTTPResultModel original = new HTTPResultModel();
		
		original.setMonitorId(monitorId);
		original.setType(MonitorType.HTTP);
		original.setTimestamp(new Date());
		original.setResponseCode(200);
		
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		
		original.setMonitorId(monitorId2);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);

		IMonitorModel[] monitors;
		
		monitors = dal.getMonitors();
		assertEquals("Should have 2 records in monitors", monitors.length, 2);

		IResultModel[] results;
		
		results = dal.getResults();
		assertEquals("Should have 10 records in results", results.length, 10);
		
		results = dal.getResults(monitorId2);
		assertEquals("Should have 5 records in results", results.length, 5);
		
		monitor.setId(monitorId2);
		boolean deleteResult = dal.deleteMonitor(monitor, true);
		assertTrue("Delete should return true", deleteResult);
		
		results = dal.getResults(monitorId2);
		assertTrue("Results for monitor2 should be null", results == null);

		results = dal.getResults();
		assertEquals("Should have 10 records in results", results.length, 5);

		monitors = dal.getMonitors();
		assertEquals("Should have 1 record in monitors", monitors.length, 1);

	}


	public void testUpdateMonitor() {
		HTTPMonitorModel original = new HTTPMonitorModel();
		
		original.setName("Test Monitor");
		original.setFrequency(1);
		original.setType(MonitorType.HTTP);
		original.setNotifyWhenDown(false);
		original.setNotifyWhenUp(false);
		original.setUnavailableThreshold(5);
		original.setUnavailableCount(0);
		original.setCreated(new Date());
		original.setAmended(new Date());
		original.setURL("http://www.google.com");
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL dal = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL(this.mContext);
		
		long id;

		HTTPMonitorModel stored;
		HTTPMonitorModel updated;

		// Don't test the Type at this stage as there is only 1 version
		String[] fields = { "Name", "Frequency", /*"Type",*/ "NotifyWhenDown", "NotifyWhenUp", "Created", "Amended", "UnavailableThreshold", "UnavailableCount", "Url"}; 
		for (int i = 0; i < fields.length ; i++) {
			original.setId(0);
			id = dal.addMonitor(original);

			stored = (HTTPMonitorModel)dal.getMonitor(id);
			assertHTTPMonitorEquals(original, stored);

			updated = stored;
			
			switch (i)
			{
				case 0:
					updated.setName("Updated");
					break;
				case 1:
					updated.setFrequency(99);
					break;
				case 9999: 
					updated.setType(MonitorType.HTTP);
					break;
				case 2: 
					updated.setNotifyWhenDown(true);
					break;
				case 3:
					updated.setNotifyWhenUp(true);
					break;
				case 4: 
					updated.setCreated(new Date());
					break;
				case 5: 
					updated.setAmended(new Date());
					break;
				case 6: 
					updated.setUnavailableThreshold(999);
					break;
				case 7:
					updated.setUnavailableCount(999);
					break;
				case 8:
					updated.setURL("http://www.yahoo.co.uk");
					break;
			}
			
			boolean result = dal.updateMonitor(updated);
			assertTrue("If " + fields[i] + " updated, we should have true returned", result);

			stored = (HTTPMonitorModel)dal.getMonitor(id);
			assertHTTPMonitorEquals(updated, stored);
		}
	}

	public void testGetMonitorById() {
		HTTPMonitorModel original = new HTTPMonitorModel();
		
		original.setName("Test Monitor");
		original.setFrequency(1);
		original.setType(MonitorType.HTTP);
		original.setNotifyWhenDown(false);
		original.setNotifyWhenUp(false);
		original.setUnavailableThreshold(5);
		original.setUnavailableCount(0);
		original.setCreated(new Date());
		original.setAmended(new Date());
		original.setURL("http://www.google.com");
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL dal = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL(this.mContext);
		
		long id = dal.addMonitor(original);
		
		assertTrue("If inserted, then we should get back an ID", id > 0);

		HTTPMonitorModel stored = (HTTPMonitorModel)dal.getMonitor(id);
		
		assertHTTPMonitorEquals(original, stored);
	}

	public void testGetMonitorsByName() {
		HTTPMonitorModel original = new HTTPMonitorModel();
		
		original.setName("Test Monitor");
		original.setFrequency(1);
		original.setType(MonitorType.HTTP);
		original.setNotifyWhenDown(false);
		original.setNotifyWhenUp(false);
		original.setUnavailableThreshold(5);
		original.setUnavailableCount(0);
		original.setCreated(new Date());
		original.setAmended(new Date());
		original.setURL("http://www.google.com");
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL dal = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL(this.mContext);
		
		long id = dal.addMonitor(original);
		
		assertTrue("If inserted, then we should get back an ID", id > 0);

		IMonitorModel[] stored = dal.getMonitors(original.getName());
		assertEquals("Only 1 record should have been returned", stored.length, 1);
		assertHTTPMonitorEquals(original, (HTTPMonitorModel)stored[0]);
	}

	public void testGetMonitors() {
		HTTPMonitorModel original = new HTTPMonitorModel();
		
		original.setName("Test Monitor");
		original.setFrequency(1);
		original.setType(MonitorType.HTTP);
		original.setNotifyWhenDown(false);
		original.setNotifyWhenUp(false);
		original.setUnavailableThreshold(5);
		original.setUnavailableCount(0);
		original.setCreated(new Date());
		original.setAmended(new Date());
		original.setURL("http://www.google.com");
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL dal = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL(this.mContext);
		
		long id = dal.addMonitor(original);
		
		assertTrue("If inserted, then we should get back an ID", id > 0);

		IMonitorModel[] stored = dal.getMonitors();
		assertEquals("Only 1 record should have been returned", stored.length, 1);
		assertHTTPMonitorEquals(original, (HTTPMonitorModel)stored[0]);
	}

	
	public void testAddResult() {
		HTTPResultModel original = new HTTPResultModel();
		
		original.setMonitorId(1);
		original.setType(MonitorType.HTTP);
		original.setTimestamp(new Date());
		original.setResponseCode(200);
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL dal = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL(this.mContext);
		
		long id = dal.addResult(original);

		assertTrue("If inserted, then we should get back an ID", id > 0);
	}

	public void testDeleteResult() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL dal = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL(this.mContext);
		
		HTTPMonitorModel monitor = new HTTPMonitorModel();
		monitor.setName("Test");
		monitor.setFrequency(1);
		monitor.setType(MonitorType.HTTP);
		monitor.setNotifyWhenDown(false);
		monitor.setNotifyWhenUp(false);
		monitor.setCreated(new Date());
		monitor.setAmended(new Date());
		monitor.setUnavailableThreshold(10);
		monitor.setUnavailableCount(5);
		monitor.setURL("http://www.google.com");
		
		long monitorId = dal.addMonitor(monitor); 
		
		HTTPResultModel original = new HTTPResultModel();
		
		original.setMonitorId(monitorId);
		original.setType(MonitorType.HTTP);
		original.setTimestamp(new Date());
		original.setResponseCode(200);
		
		long id = dal.addResult(original);

		HTTPResultModel stored = (HTTPResultModel)dal.getResult(id);
		assertHTTPResultEquals(original, stored);
		
		boolean result = dal.deleteResult(stored);
		assertTrue("If deleted, we should have true returned", result);
		
		stored = (HTTPResultModel)dal.getResult(id);
		assertNull("If the model has been deleted, we should get null",stored);
	}
	
	public void testDeleteResultByMonitorId() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL dal = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL(this.mContext);
		
		HTTPMonitorModel monitor = new HTTPMonitorModel();
		monitor.setName("Test");
		monitor.setFrequency(1);
		monitor.setType(MonitorType.HTTP);
		monitor.setNotifyWhenDown(false);
		monitor.setNotifyWhenUp(false);
		monitor.setCreated(new Date());
		monitor.setAmended(new Date());
		monitor.setUnavailableThreshold(10);
		monitor.setUnavailableCount(5);
		monitor.setURL("http://www.google.com");
		
		long monitorId = dal.addMonitor(monitor); 
		long monitorId2 = dal.addMonitor(monitor); 

		HTTPResultModel original = new HTTPResultModel();
		
		original.setMonitorId(monitorId);
		original.setType(MonitorType.HTTP);
		original.setTimestamp(new Date());
		original.setResponseCode(200);
		
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		
		original.setMonitorId(monitorId2);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);

		IResultModel[] results;
		
		results = dal.getResults();
		assertEquals("Should have 10 records in results", results.length, 10);
		
		results = dal.getResults(monitorId2);
		assertEquals("Should have 5 records in results", results.length, 5);
		
		boolean deleteResult = dal.deleteResultsByMonitorId(monitorId2);
		assertTrue("Delete should return true", deleteResult);
		
		results = dal.getResults(monitorId2);
		assertTrue("Results for monitor2 should be null", results == null);

		results = dal.getResults();
		assertEquals("Should have 10 records in results", results.length, 5);

	}

	public void testDeleteResultByMonitorIdAndOlderThan() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL dal = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL(this.mContext);
		
		HTTPMonitorModel monitor = new HTTPMonitorModel();
		monitor.setName("Test");
		monitor.setFrequency(1);
		monitor.setType(MonitorType.HTTP);
		monitor.setNotifyWhenDown(false);
		monitor.setNotifyWhenUp(false);
		monitor.setCreated(new Date());
		monitor.setAmended(new Date());
		monitor.setUnavailableThreshold(10);
		monitor.setUnavailableCount(5);
		monitor.setURL("http://www.google.com");
		
		long monitorId = dal.addMonitor(monitor); 
		long monitorId2 = dal.addMonitor(monitor); 

		HTTPResultModel original = new HTTPResultModel();
		
		original.setMonitorId(monitorId);
		original.setType(MonitorType.HTTP);
		original.setTimestamp(new Date());
		original.setResponseCode(200);
		
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		
		original.setMonitorId(monitorId2);
		original.setTimestamp(Conversion.parseString("2001-01-01T12:08:56.235-0000"));
		dal.addResult(original);
		original.setTimestamp(Conversion.parseString("2002-01-01T12:08:56.235-0000"));
		dal.addResult(original);
		original.setTimestamp(Conversion.parseString("2003-01-01T12:08:56.235-0000"));
		dal.addResult(original);
		original.setTimestamp(Conversion.parseString("2004-01-01T12:08:56.235-0000"));
		dal.addResult(original);
		original.setTimestamp(Conversion.parseString("2005-01-01T12:08:56.235-0000"));
		dal.addResult(original);

		IResultModel[] results;
		
		results = dal.getResults();
		assertEquals("Should have 10 records in results", results.length, 10);
		
		results = dal.getResults(monitorId2);
		assertEquals("Should have 5 records in results", results.length, 5);
		
		boolean deleteResult = dal.deleteResultsByMonitorId(monitorId2, Conversion.parseString("2003-01-01T12:08:56.235-0000"));
		assertTrue("Delete should return true", deleteResult);
		
		results = dal.getResults(monitorId2);
		assertEquals("Should have 2 records in results", results.length, 2);
		original.setTimestamp(Conversion.parseString("2005-01-01T12:08:56.235-0000"));
		assertHTTPResultEquals(original, (HTTPResultModel)results[0]);
		original.setTimestamp(Conversion.parseString("2004-01-01T12:08:56.235-0000"));
		assertHTTPResultEquals(original, (HTTPResultModel)results[1]);

		results = dal.getResults();
		assertEquals("Should have 7 records in results", results.length, 7);

	}

	public void testUpdateResult() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL dal = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL(this.mContext);
		
		HTTPMonitorModel monitor = new HTTPMonitorModel();
		monitor.setName("Test");
		monitor.setFrequency(1);
		monitor.setType(MonitorType.HTTP);
		monitor.setNotifyWhenDown(false);
		monitor.setNotifyWhenUp(false);
		monitor.setCreated(new Date());
		monitor.setAmended(new Date());
		monitor.setUnavailableThreshold(10);
		monitor.setUnavailableCount(5);
		monitor.setURL("http://www.google.com");
		
		long monitorId = dal.addMonitor(monitor); 
		long monitorId2 = dal.addMonitor(monitor); 

		HTTPResultModel original = new HTTPResultModel();
		
		original.setMonitorId(monitorId);
		original.setType(MonitorType.HTTP);
		original.setTimestamp(new Date());
		original.setResponseCode(200);
		
		long id;

		HTTPResultModel stored;
		HTTPResultModel updated;

		// Don't test the Type at this stage as there is only 1 version
		String[] fields = { "MonitorId", /*"Type", */ "Timestamp", "ResponseCode" }; 
		for (int i = 0; i < fields.length ; i++) {
			original.setId(0);
			id = dal.addResult(original);

			stored = (HTTPResultModel)dal.getResult(id);
			assertHTTPResultEquals(original, stored);

			updated = stored;
			
			switch (i)
			{
				case 0:
					updated.setMonitorId(monitorId2);
					break;
				case 9999: // TODO when we have a new monitor type
					updated.setType(MonitorType.HTTP);
					break;
				case 1:
					updated.setTimestamp(new Date());
					break;
				case 2:
					updated.setResponseCode(403);
					break;
			}
			
			boolean result = dal.updateResult(updated);
			assertTrue("If " + fields[i] + " updated, we should have true returned", result);

			stored = (HTTPResultModel)dal.getResult(id);
			assertHTTPResultEquals(updated, stored);
		}
	}

	public void testGetResultsByMonitorId() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL dal = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL(this.mContext);
		
		HTTPMonitorModel monitor = new HTTPMonitorModel();
		monitor.setName("Test");
		monitor.setFrequency(1);
		monitor.setType(MonitorType.HTTP);
		monitor.setNotifyWhenDown(false);
		monitor.setNotifyWhenUp(false);
		monitor.setCreated(new Date());
		monitor.setAmended(new Date());
		monitor.setUnavailableThreshold(10);
		monitor.setUnavailableCount(5);
		monitor.setURL("http://www.google.com");
		
		long monitorId = dal.addMonitor(monitor); 
		long monitorId2 = dal.addMonitor(monitor); 

		HTTPResultModel original = new HTTPResultModel();
		
		original.setMonitorId(monitorId);
		original.setType(MonitorType.HTTP);
		original.setTimestamp(new Date());
		original.setResponseCode(200);
		
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		
		original.setMonitorId(monitorId2);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);

		IResultModel[] results;
		
		results = dal.getResults();
		
		assertEquals("Should have 10 records in results", results.length, 10);
		
		results = dal.getResults(monitorId2);
		assertEquals("Should have 5 records in results", results.length, 5);
		
		/* Check that data is correct */
		assertHTTPResultEquals(original, (HTTPResultModel)results[0]);
		assertHTTPResultEquals(original, (HTTPResultModel)results[1]);
		assertHTTPResultEquals(original, (HTTPResultModel)results[2]);
		assertHTTPResultEquals(original, (HTTPResultModel)results[3]);
		assertHTTPResultEquals(original, (HTTPResultModel)results[4]);
	}
	
	public void testGetResultsByMonitorIdAndSkipAndTake() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL dal = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL(this.mContext);
		
		HTTPMonitorModel monitor = new HTTPMonitorModel();
		monitor.setName("Test");
		monitor.setFrequency(1);
		monitor.setType(MonitorType.HTTP);
		monitor.setNotifyWhenDown(false);
		monitor.setNotifyWhenUp(false);
		monitor.setCreated(new Date());
		monitor.setAmended(new Date());
		monitor.setUnavailableThreshold(10);
		monitor.setUnavailableCount(5);
		monitor.setURL("http://www.google.com");
		
		long monitorId1 = dal.addMonitor(monitor); 
		long monitorId2 = dal.addMonitor(monitor); 

		HTTPResultModel original = new HTTPResultModel();
		
		original.setMonitorId(monitorId1);
		original.setType(MonitorType.HTTP);
		original.setTimestamp(new Date());
		original.setResponseCode(200);
		
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		
		original.setMonitorId(monitorId2);
		long id1 = dal.addResult(original);
		long id2 = dal.addResult(original);
		long id3 = dal.addResult(original);
		long id4 = dal.addResult(original);
		long id5 = dal.addResult(original);

		IResultModel[] results;
		
		results = dal.getResults();
		
		assertEquals("Should have 10 records in results", results.length, 10);
		
		results = dal.getResults(monitorId2, 1, 3);
		assertEquals("Should have 3 records in results", results.length, 3);
		
		/* Check that data is correct */
		assertEquals("First record should be the 2nd for the result", id2, results[0].getId());
		assertEquals("First record should be the 3rd for the result", id3, results[1].getId());
		assertEquals("First record should be the 4th for the result", id4, results[2].getId());
		
		assertHTTPResultEquals(original, (HTTPResultModel)results[0]);
		assertHTTPResultEquals(original, (HTTPResultModel)results[1]);
		assertHTTPResultEquals(original, (HTTPResultModel)results[2]);
		
	}
	
	public void testGetResultsByMonitorIdAndFromAndTo() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL dal = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL(this.mContext);
		
		HTTPMonitorModel monitor = new HTTPMonitorModel();
		monitor.setName("Test");
		monitor.setFrequency(1);
		monitor.setType(MonitorType.HTTP);
		monitor.setNotifyWhenDown(false);
		monitor.setNotifyWhenUp(false);
		monitor.setCreated(new Date());
		monitor.setAmended(new Date());
		monitor.setUnavailableThreshold(10);
		monitor.setUnavailableCount(5);
		monitor.setURL("http://www.google.com");
		
		long monitorId1 = dal.addMonitor(monitor); 
		long monitorId2 = dal.addMonitor(monitor); 

		HTTPResultModel original = new HTTPResultModel();
		
		original.setMonitorId(monitorId1);
		original.setType(MonitorType.HTTP);
		original.setTimestamp(new Date());
		original.setResponseCode(200);
		
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		
		original.setMonitorId(monitorId2);
		original.setTimestamp(Conversion.parseString("2001-01-01T12:08:56.235-0000"));
		long id1 = dal.addResult(original);
		original.setTimestamp(Conversion.parseString("2002-01-01T12:08:56.235-0000"));
		long id2 = dal.addResult(original);
		original.setTimestamp(Conversion.parseString("2003-01-01T12:08:56.235-0000"));
		long id3 = dal.addResult(original);
		original.setTimestamp(Conversion.parseString("2004-01-01T12:08:56.235-0000"));
		long id4 = dal.addResult(original);
		original.setTimestamp(Conversion.parseString("2005-01-01T12:08:56.235-0000"));
		long id5 = dal.addResult(original);

		IResultModel[] results;
		
		results = dal.getResults();
		
		assertEquals("Should have 10 records in results", results.length, 10);
		
		results = dal.getResults(monitorId2, Conversion.parseString("2002-01-01T12:08:56.235-0000"), Conversion.parseString("2004-01-01T12:08:56.235-0000"));
		assertEquals("Should have 3 records in results", results.length, 3);
		
		/* Check that data is correct */
		assertEquals("First record should be the 4th for the result", id4, results[0].getId());
		assertEquals("Second record should be the 3rd for the result", id3, results[1].getId());
		assertEquals("Third record should be the 2nd for the result", id2, results[2].getId());
		
		original.setTimestamp(Conversion.parseString("2004-01-01T12:08:56.235-0000"));
		assertHTTPResultEquals(original, (HTTPResultModel)results[0]);
		original.setTimestamp(Conversion.parseString("2003-01-01T12:08:56.235-0000"));
		assertHTTPResultEquals(original, (HTTPResultModel)results[1]);
		original.setTimestamp(Conversion.parseString("2002-01-01T12:08:56.235-0000"));
		assertHTTPResultEquals(original, (HTTPResultModel)results[2]);
		
	}
	
	
	public void testGetResultsByMonitorIdAndFromAndToAndSkipAndTake() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL dal = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL(this.mContext);
		
		HTTPMonitorModel monitor = new HTTPMonitorModel();
		monitor.setName("Test");
		monitor.setFrequency(1);
		monitor.setType(MonitorType.HTTP);
		monitor.setNotifyWhenDown(false);
		monitor.setNotifyWhenUp(false);
		monitor.setCreated(new Date());
		monitor.setAmended(new Date());
		monitor.setUnavailableThreshold(10);
		monitor.setUnavailableCount(5);
		monitor.setURL("http://www.google.com");
		
		long monitorId1 = dal.addMonitor(monitor); 
		long monitorId2 = dal.addMonitor(monitor); 

		HTTPResultModel original = new HTTPResultModel();
		
		original.setMonitorId(monitorId1);
		original.setType(MonitorType.HTTP);
		original.setTimestamp(new Date());
		original.setResponseCode(200);
		
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		dal.addResult(original);
		
		original.setMonitorId(monitorId2);
		original.setTimestamp(Conversion.parseString("2001-01-01T12:08:56.235-0000"));
		long id1 = dal.addResult(original);
		original.setTimestamp(Conversion.parseString("2002-01-01T12:08:56.235-0000"));
		long id2 = dal.addResult(original);
		original.setTimestamp(Conversion.parseString("2003-01-01T12:08:56.235-0000"));
		long id3 = dal.addResult(original);
		original.setTimestamp(Conversion.parseString("2004-01-01T12:08:56.235-0000"));
		long id4 = dal.addResult(original);
		original.setTimestamp(Conversion.parseString("2005-01-01T12:08:56.235-0000"));
		long id5 = dal.addResult(original);

		IResultModel[] results;
		
		results = dal.getResults();
		
		assertEquals("Should have 10 records in results", results.length, 10);
		
		results = dal.getResults(monitorId2, Conversion.parseString("2002-01-01T12:08:56.235-0000"), Conversion.parseString("2004-01-01T12:08:56.235-0000"), 1, 1);
		assertEquals("Should have 1 record in results", results.length, 1);
		
		/* Check that data is correct */
		assertEquals("First record should be the 3rd for the result", id3, results[0].getId());
		
		original.setTimestamp(Conversion.parseString("2003-01-01T12:08:56.235-0000"));
		assertHTTPResultEquals(original, (HTTPResultModel)results[0]);
		
	}
	
	
	
	

	private void assertHTTPMonitorEquals(HTTPMonitorModel first, HTTPMonitorModel second) {
		assertHTTPMonitorEquals(first, second, false);
	}

	private void assertHTTPMonitorEquals(HTTPMonitorModel first, HTTPMonitorModel second, boolean includeID) {
		assertTrue("First model should not be null", first != null);
		assertTrue("Second model should not be null", second != null);
		
		if (includeID)
			assertEquals("Id should be equal", first.getId(), second.getId());

		assertEquals("Name should be equal", first.getName(), second.getName());
		assertEquals("Frequency should be equal", first.getFrequency(), second.getFrequency());
		assertEquals("Type should be equal", first.getType(), second.getType());
		assertEquals("NotifyWhenDown should be equal", first.getNotifyWhenDown(), second.getNotifyWhenDown());
		assertEquals("NotifyWhenUp should be equal", first.getNotifyWhenUp(), second.getNotifyWhenUp());
		assertEquals("Created should be equal", first.getCreated(), second.getCreated());
		assertEquals("Amended should be equal", first.getAmended(), second.getAmended());
		assertEquals("UnavailableThreshold should be equal", first.getUnavailableThreshold(), second.getUnavailableThreshold());
		assertEquals("UnavailableCount should be equal", first.getUnavailableCount(), second.getUnavailableCount());
		assertEquals("URL should be equal", first.getURL(), second.getURL());
	}

	private void assertHTTPResultEquals(HTTPResultModel first, HTTPResultModel second) {
		assertHTTPResultEquals(first, second, false);
	}

	private void assertHTTPResultEquals(HTTPResultModel first, HTTPResultModel second, boolean includeID) {
		assertTrue("First model should not be null", first != null);
		assertTrue("Second model should not be null", second != null);
		
		if (includeID)
			assertEquals("Id should be equal", first.getId(), second.getId());

		assertEquals("MonitorId should be equal", first.getMonitorId(), second.getMonitorId());
		assertEquals("Type should be equal", first.getType(), second.getType());
		assertEquals("Timestamp should be equal", first.getTimestamp(), second.getTimestamp());
		assertEquals("ResponseCode should be equal", first.getResponseCode(), second.getResponseCode());
	}

}
