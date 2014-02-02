package com.red_folder.phonegap.plugin.availabilitymonitor.db.tests;


import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.HTTPMonitorModel;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class SQLiteHTTPMonitorProvider extends InstrumentationTestCase {

	private Context mContext = null;

	protected void setUp() throws Exception {
		super.setUp();
		
		this.mContext = this.getInstrumentation().getContext();

		TestHelper.emptyHTTPMonitors(this.mContext);
	}

	protected void tearDown() throws Exception {
		super.tearDown();

		TestHelper.emptyHTTPMonitors(this.mContext);
	}

	public void testGetByMonitorId() {
		HTTPMonitorModel original = new HTTPMonitorModel();
		
		original.setMonitorId(999);
		original.setURL("http://www.google.com");
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPMonitorProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPMonitorProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		HTTPMonitorModel[] stored = provider.getByMonitorId(original.getMonitorId());
		
		assertTrue("Stored model shoud not be null", stored != null);
		assertEquals("Array should only have 1", stored.length, 1);
		
		assertModelEquals(original, stored[0]);
	}

	public void testDeleteByMonitorId() {
		HTTPMonitorModel original = new HTTPMonitorModel();
		
		original.setMonitorId(2345);
		original.setURL("http://www.google.com");
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPMonitorProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPMonitorProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		HTTPMonitorModel stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(original, stored);
		
		boolean result = provider.deleteByMonitorId(original.getMonitorId());
		
		assertTrue("Delete should have returned true", result);
		
		stored = provider.get(id);
		
		assertTrue("Stored model shoud be null", stored == null);
	}
	
	public void testAdd() {
		HTTPMonitorModel original = new HTTPMonitorModel();
		
		original.setMonitorId(1);
		original.setURL("http://www.google.com");
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPMonitorProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPMonitorProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		HTTPMonitorModel stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(original, stored);
	}

	public void testDelete() {
		HTTPMonitorModel original = new HTTPMonitorModel();
		
		original.setMonitorId(1);
		original.setURL("http://www.google.com");
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPMonitorProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPMonitorProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		HTTPMonitorModel stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(original, stored);
		
		boolean result = provider.delete(id);
		
		assertTrue("Delete should have returned true", result);
		
		stored = provider.get(id);
		
		assertTrue("Stored model shoud be null", stored == null);
	}

	public void testDeleteAll() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPMonitorProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPMonitorProvider(this.mContext);

		HTTPMonitorModel original[] = new HTTPMonitorModel[5];
		long[] id = new long[5];

		for (int i = 0; i < original.length; i++) {
			original[i] = new HTTPMonitorModel();
			original[i].setMonitorId(i);
			original[i].setURL("http://www.google.com/" + i);
			
			id[i] = provider.add(original[i]);
			assertTrue("After add, id should be greater than zero", id[i] > 0);
		}

		HTTPMonitorModel[] stored = provider.getAll();
		
		assertTrue("Stored model array shoud not be null", stored != null);
		assertTrue("Stroed model array should be 5 long", stored.length == 5);
		
		for (int i = 0; i < stored.length; i++) {
			assertModelEquals(original[i], stored[i]);
		}
		
		for (int i = 0; i < original.length; i++) {
			boolean result = provider.delete(id[i]);
			
			assertTrue("Delete should have returned true", result);
		}
		
		stored = provider.getAll();
		
		assertTrue("Stored model array shoud not be null", stored != null);
		assertTrue("Stroed model array should be 0 long", stored.length == 0);
	}

	public void testUpdate() {
		HTTPMonitorModel original = new HTTPMonitorModel();
		
		original.setMonitorId(1);
		original.setURL("http://www.google.com");
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPMonitorProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPMonitorProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		HTTPMonitorModel stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(original, stored);

		HTTPMonitorModel updated = stored;
		
		updated.setMonitorId(99);
		boolean result = provider.update(updated);
		
		assertTrue("Update should have returned true", result);
		
		stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(updated, stored);
		
		updated.setURL("http://www.yahoo.co.uk");
		result = provider.update(updated);
		
		assertTrue("Update should have returned true", result);
		
		stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(updated, stored);
	}

	public void testGetAll() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPMonitorProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPMonitorProvider(this.mContext);

		HTTPMonitorModel original[] = new HTTPMonitorModel[5];
		long[] id = new long[5];

		for (int i = 0; i < original.length; i++) {
			original[i] = new HTTPMonitorModel();
			original[i].setMonitorId(i);
			original[i].setURL("http://www.google.com/" + i);
			
			id[i] = provider.add(original[i]);
			assertTrue("After add, id should be greater than zero", id[i] > 0);
		}

		HTTPMonitorModel[] stored = provider.getAll();
		
		assertTrue("Stored model array shoud not be null", stored != null);
		assertTrue("Stroed model array should be 5 long", stored.length == 5);
		
		for (int i = 0; i < stored.length; i++) {
			assertModelEquals(original[i], stored[i]);
		}
	}


	private void assertModelEquals(HTTPMonitorModel first, HTTPMonitorModel second) {
		assertModelEquals(first, second, false);
	}

	private void assertModelEquals(HTTPMonitorModel first, HTTPMonitorModel second, boolean includeID) {
		assertTrue("First model should not be null", first != null);
		assertTrue("Second model should not be null", second != null);
		
		if (includeID)
			assertEquals("Id should be equal", first.getId(), second.getId());

		assertEquals("MonitorId should be equal", first.getMonitorId(), second.getMonitorId());
		assertEquals("URL should be equal", first.getURL(), second.getURL());
	}
}
