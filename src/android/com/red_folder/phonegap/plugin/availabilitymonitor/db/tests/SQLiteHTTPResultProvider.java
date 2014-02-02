package com.red_folder.phonegap.plugin.availabilitymonitor.db.tests;

import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.HTTPResultModel;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class SQLiteHTTPResultProvider extends InstrumentationTestCase {

	private Context mContext = null;

	protected void setUp() throws Exception {
		super.setUp();
		
		this.mContext = this.getInstrumentation().getContext();

		TestHelper.emptyHTTPResults(this.mContext);
	}

	protected void tearDown() throws Exception {
		super.tearDown();

		TestHelper.emptyHTTPResults(this.mContext);
	}

	public void testGetByResultId() {
		HTTPResultModel original = new HTTPResultModel();
		
		original.setResultId(999);
		original.setResponseCode(200);
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPResultProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPResultProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		HTTPResultModel[] stored = provider.getByResultId(original.getResultId());
		
		assertTrue("Stored model shoud not be null", stored != null);
		assertEquals("Array should only have 1", stored.length, 1);
		
		assertModelEquals(original, stored[0]);
	}

	public void testDeleteByResultId() {
		HTTPResultModel original = new HTTPResultModel();
		
		original.setResultId(999);
		original.setResponseCode(200);
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPResultProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPResultProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		HTTPResultModel stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(original, stored);
		
		boolean result = provider.deleteByResultId(original.getResultId());
		
		assertTrue("Delete should have returned true", result);
		
		stored = provider.get(id);
		
		assertTrue("Stored model shoud be null", stored == null);
	}
	
	public void testAdd() {
		HTTPResultModel original = new HTTPResultModel();
		
		original.setResultId(999);
		original.setResponseCode(200);
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPResultProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPResultProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		HTTPResultModel stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(original, stored);
	}

	public void testDelete() {
		HTTPResultModel original = new HTTPResultModel();
		
		original.setResultId(999);
		original.setResponseCode(200);
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPResultProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPResultProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		HTTPResultModel stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(original, stored);
		
		boolean result = provider.delete(id);
		
		assertTrue("Delete should have returned true", result);
		
		stored = provider.get(id);
		
		assertTrue("Stored model shoud be null", stored == null);
	}

	public void testDeleteAll() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPResultProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPResultProvider(this.mContext);

		HTTPResultModel original[] = new HTTPResultModel[5];
		long[] id = new long[5];

		for (int i = 0; i < original.length; i++) {
			original[i] = new HTTPResultModel();
			original[i].setResultId(i);
			original[i].setResponseCode(i);
			
			id[i] = provider.add(original[i]);
			assertTrue("After add, id should be greater than zero", id[i] > 0);
		}

		HTTPResultModel[] stored = provider.getAll();
		
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
		HTTPResultModel original = new HTTPResultModel();
		
		original.setResultId(999);
		original.setResponseCode(200);
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPResultProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPResultProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		HTTPResultModel stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(original, stored);

		HTTPResultModel updated = stored;
		
		updated.setResultId(99);
		boolean result = provider.update(updated);
		
		assertTrue("Update should have returned true", result);
		
		stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(updated, stored);
		
		updated.setResponseCode(403);
		result = provider.update(updated);
		
		assertTrue("Update should have returned true", result);
		
		stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(updated, stored);
	}

	public void testGetAll() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPResultProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPResultProvider(this.mContext);

		HTTPResultModel original[] = new HTTPResultModel[5];
		long[] id = new long[5];

		for (int i = 0; i < original.length; i++) {
			original[i] = new HTTPResultModel();
			original[i].setResultId(i);
			original[i].setResponseCode(i);
			
			id[i] = provider.add(original[i]);
			assertTrue("After add, id should be greater than zero", id[i] > 0);
		}

		HTTPResultModel[] stored = provider.getAll();
		
		assertTrue("Stored model array shoud not be null", stored != null);
		assertTrue("Stroed model array should be 5 long", stored.length == 5);
		
		for (int i = 0; i < stored.length; i++) {
			assertModelEquals(original[i], stored[i]);
		}
	}


	private void assertModelEquals(HTTPResultModel first, HTTPResultModel second) {
		assertModelEquals(first, second, false);
	}

	private void assertModelEquals(HTTPResultModel first, HTTPResultModel second, boolean includeID) {
		assertTrue("First model should not be null", first != null);
		assertTrue("Second model should not be null", second != null);
		
		if (includeID)
			assertEquals("Id should be equal", first.getId(), second.getId());

		assertEquals("ResultId should be equal", first.getResultId(), second.getResultId());
		assertEquals("ResponseCode should be equal", first.getResponseCode(), second.getResponseCode());
	}

}
