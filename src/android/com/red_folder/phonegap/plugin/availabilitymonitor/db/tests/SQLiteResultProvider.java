package com.red_folder.phonegap.plugin.availabilitymonitor.db.tests;

import java.util.Date;

import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.ResultModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.enums.MonitorType;
import com.red_folder.phonegap.plugin.availabilitymonitor.utils.Conversion;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class SQLiteResultProvider extends InstrumentationTestCase {

	private Context mContext = null;

	protected void setUp() throws Exception {
		super.setUp();
		
		this.mContext = this.getInstrumentation().getContext();

		TestHelper.emptyResults(this.mContext);
	}

	protected void tearDown() throws Exception {
		super.tearDown();

		TestHelper.emptyResults(this.mContext);
	}

	public void testAdd() {
		ResultModel original = new ResultModel();
		
		original.setMonitorId(1);
		original.setType(MonitorType.HTTP);
		original.setTimestamp(new Date());
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		ResultModel stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(original, stored);
	}

	public void testDelete() {
		ResultModel original = new ResultModel();
		
		original.setMonitorId(1);
		original.setType(MonitorType.HTTP);
		original.setTimestamp(new Date());
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		ResultModel stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(original, stored);
		
		boolean result = provider.delete(id);
		
		assertTrue("Delete should have returned true", result);
		
		stored = provider.get(id);
		
		assertTrue("Stored model shoud be null", stored == null);
	}


	public void testDeleteAll() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider(this.mContext);

		ResultModel original[] = new ResultModel[5];
		long[] id = new long[5];

		for (int i = 0; i < original.length; i++) {
			original[i] = new ResultModel();
			original[i].setMonitorId(1);
			original[i].setType(MonitorType.HTTP);
			original[i].setTimestamp(new Date());

			
			id[i] = provider.add(original[i]);
			assertTrue("After add, id should be greater than zero", id[i] > 0);
		}

		ResultModel[] stored = provider.getAll();
		
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
		ResultModel original = new ResultModel();
		
		original.setMonitorId(1);
		original.setType(MonitorType.HTTP);
		original.setTimestamp(new Date());
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		ResultModel stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(original, stored);

		ResultModel updated = stored;
		
		updated.setMonitorId(999);
		boolean result = provider.update(updated);
		assertTrue("Update should have returned true", result);
		stored = provider.get(id);
		assertTrue("Stored model shoud not be null", stored != null);
		assertModelEquals(updated, stored);

		/* TODO - Update when we have another monitor type */
		/*
		updated.setMonitorType(TODO);
		boolean result = provider.update(updated);
		assertTrue("Update should have returned true", result);
		stored = provider.get(id);
		assertTrue("Stored model shoud not be null", stored != null);
		assertModelEquals(updated, stored);
		*/
		
		updated.setTimestamp(new Date());
		result = provider.update(updated);
		assertTrue("Update should have returned true", result);
		stored = provider.get(id);
		assertTrue("Stored model shoud not be null", stored != null);
		assertModelEquals(updated, stored);

	}

	public void testGetAll() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider(this.mContext);

		ResultModel original[] = new ResultModel[5];
		long[] id = new long[5];

		for (int i = 0; i < original.length; i++) {
			original[i] = new ResultModel();
			original[i].setMonitorId(1);
			original[i].setType(MonitorType.HTTP);
			original[i].setTimestamp(new Date());
			
			id[i] = provider.add(original[i]);
			assertTrue("After add, id should be greater than zero", id[i] > 0);
		}

		ResultModel[] stored = provider.getAll();
		
		assertTrue("Stored model array shoud not be null", stored != null);
		assertTrue("Stroed model array should be 5 long", stored.length == 5);
		
		for (int i = 0; i < stored.length; i++) {
			assertModelEquals(original[i], stored[i]);
		}
	}

	public void testGetByMonitorId() {
		ResultModel original = new ResultModel();
		
		original.setMonitorId(1);
		original.setType(MonitorType.HTTP);
		original.setTimestamp(new Date());
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		ResultModel[] stored = provider.getByMonitorId(original.getMonitorId());
		
		assertTrue("Stored model shoud not be null", stored != null);
		assertEquals("Array should only have 1 record", stored.length, 1);
		
		assertModelEquals(original, stored[0]);
	}

	public void testGetByMonitorIdAndSkipAndTake() {
		ResultModel original = new ResultModel();
		
		original.setMonitorId(1);
		original.setType(MonitorType.HTTP);
		original.setTimestamp(new Date());
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider(this.mContext);
		
		provider.add(original);
		provider.add(original);
		provider.add(original);
		provider.add(original);
		provider.add(original);
		provider.add(original);
		
		ResultModel[] stored = provider.getByMonitorId(original.getMonitorId(), 1, 3);
		
		assertTrue("Stored model shoud not be null", stored != null);
		assertEquals("Array should only have 3 record", stored.length, 3);
		
		assertModelEquals(original, stored[0]);
		assertModelEquals(original, stored[1]);
		assertModelEquals(original, stored[2]);
	}

	public void getByMonitorIdAndFromAndTo() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider(this.mContext);

		ResultModel original = new ResultModel();
		
		original.setMonitorId(1);
		original.setType(MonitorType.HTTP);

		original.setTimestamp(Conversion.parseString("2001-01-01T12:08:56.235-0000"));
		provider.add(original);
		original.setTimestamp(Conversion.parseString("2002-01-01T12:08:56.235-0000"));
		provider.add(original);
		original.setTimestamp(Conversion.parseString("2003-01-01T12:08:56.235-0000"));
		provider.add(original);
		original.setTimestamp(Conversion.parseString("2004-01-01T12:08:56.235-0000"));
		provider.add(original);
		original.setTimestamp(Conversion.parseString("2005-01-01T12:08:56.235-0000"));
		provider.add(original);
		original.setTimestamp(Conversion.parseString("2006-01-01T12:08:56.235-0000"));
		provider.add(original);
		
		
		ResultModel[] stored = provider.getByMonitorId(	original.getMonitorId(), 
														Conversion.parseString("2002-01-01T12:08:56.235-0000"), 
														Conversion.parseString("2004-01-01T12:08:56.235-0000"));
		
		assertTrue("Stored model shoud not be null", stored != null);
		assertEquals("Array should only have 3 record", stored.length, 3);
		
		original.setTimestamp(Conversion.parseString("2002-01-01T12:08:56.235-0000"));
		assertModelEquals(original, stored[0]);
		original.setTimestamp(Conversion.parseString("2003-01-01T12:08:56.235-0000"));
		assertModelEquals(original, stored[1]);
		original.setTimestamp(Conversion.parseString("2004-01-01T12:08:56.235-0000"));
		assertModelEquals(original, stored[2]);
	}
	
	public void getByMonitorIdAndFromAndToAndSkipAndTake() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider(this.mContext);

		ResultModel original = new ResultModel();
		
		original.setMonitorId(1);
		original.setType(MonitorType.HTTP);

		original.setTimestamp(Conversion.parseString("2001-01-01T12:08:56.235-0000"));
		provider.add(original);
		original.setTimestamp(Conversion.parseString("2002-01-01T12:08:56.235-0000"));
		provider.add(original);
		original.setTimestamp(Conversion.parseString("2003-01-01T12:08:56.235-0000"));
		provider.add(original);
		original.setTimestamp(Conversion.parseString("2004-01-01T12:08:56.235-0000"));
		provider.add(original);
		original.setTimestamp(Conversion.parseString("2005-01-01T12:08:56.235-0000"));
		provider.add(original);
		original.setTimestamp(Conversion.parseString("2006-01-01T12:08:56.235-0000"));
		provider.add(original);
		
		
		ResultModel[] stored = provider.getByMonitorId(	original.getMonitorId(), 
														Conversion.parseString("2002-01-01T12:08:56.235-0000"), 
														Conversion.parseString("2004-01-01T12:08:56.235-0000"),
														1,
														1);
		
		assertTrue("Stored model shoud not be null", stored != null);
		assertEquals("Array should only have 1 record", stored.length, 1);
		
		original.setTimestamp(Conversion.parseString("2003-01-01T12:08:56.235-0000"));
		assertModelEquals(original, stored[0]);
	}
	
	private void assertModelEquals(ResultModel first, ResultModel second) {
		assertModelEquals(first, second, false);
	}

	private void assertModelEquals(ResultModel first, ResultModel second, boolean includeID) {
		assertTrue("First model should not be null", first != null);
		assertTrue("Second model should not be null", second != null);
		
		if (includeID)
			assertEquals("Id should be equal", first.getId(), second.getId());

		assertEquals("MonitorId should be equal", first.getMonitorId(), second.getMonitorId());
		assertEquals("Type should be equal", first.getType(), second.getType());
		assertEquals("Timestamp should be equal", first.getTimestamp(), second.getTimestamp());
		
	}
}