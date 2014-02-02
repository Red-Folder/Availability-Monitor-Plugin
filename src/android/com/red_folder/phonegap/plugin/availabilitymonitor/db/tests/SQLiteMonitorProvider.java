package com.red_folder.phonegap.plugin.availabilitymonitor.db.tests;

import java.util.Date;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.MonitorModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.enums.MonitorType;

public class SQLiteMonitorProvider extends InstrumentationTestCase {

	private Context mContext = null;

	protected void setUp() throws Exception {
		super.setUp();
		
		this.mContext = this.getInstrumentation().getContext();

		TestHelper.emptyMonitors(this.mContext);
	}

	protected void tearDown() throws Exception {
		super.tearDown();

		TestHelper.emptyMonitors(this.mContext);
	}

	public void testAdd() {
		MonitorModel original = new MonitorModel();
		
		original.setName("Test");
		original.setType(MonitorType.HTTP);
		original.setFrequency(1);
		original.setNotifyWhenDown(false);
		original.setNotifyWhenUp(false);
		original.setUnavailableThreshold(10);
		original.setUnavailableCount(5);
		original.setCreated(new Date());
		original.setAmended(new Date());
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteMonitorProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteMonitorProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		MonitorModel stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(original, stored);
	}

	public void testDelete() {
		MonitorModel original = new MonitorModel();
		
		original.setName("Test");
		original.setType(MonitorType.HTTP);
		original.setFrequency(1);
		original.setNotifyWhenDown(false);
		original.setNotifyWhenUp(false);
		original.setUnavailableThreshold(10);
		original.setUnavailableCount(5);
		original.setCreated(new Date());
		original.setAmended(new Date());
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteMonitorProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteMonitorProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		MonitorModel stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(original, stored);
		
		boolean result = provider.delete(id);
		
		assertTrue("Delete should have returned true", result);
		
		stored = provider.get(id);
		
		assertTrue("Stored model shoud be null", stored == null);
	}

	public void testDeleteAll() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteMonitorProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteMonitorProvider(this.mContext);

		MonitorModel original[] = new MonitorModel[5];
		long[] id = new long[5];

		for (int i = 0; i < original.length; i++) {
			original[i] = new MonitorModel();
			original[i].setName("Test " + i);
			original[i].setType(MonitorType.HTTP);
			original[i].setFrequency(i);
			original[i].setNotifyWhenDown(false);
			original[i].setNotifyWhenUp(false);
			original[i].setUnavailableThreshold(i);
			original[i].setUnavailableCount(i);
			original[i].setCreated(new Date());
			original[i].setAmended(new Date());

			
			id[i] = provider.add(original[i]);
			assertTrue("After add, id should be greater than zero", id[i] > 0);
		}

		MonitorModel[] stored = provider.getAll();
		
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
		MonitorModel original = new MonitorModel();
		
		original.setName("Test");
		original.setType(MonitorType.HTTP);
		original.setFrequency(1);
		original.setNotifyWhenDown(false);
		original.setNotifyWhenUp(false);
		original.setUnavailableThreshold(10);
		original.setUnavailableCount(5);
		original.setCreated(new Date());
		original.setAmended(new Date());
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteMonitorProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteMonitorProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		MonitorModel stored = provider.get(id);
		
		assertTrue("Stored model shoud not be null", stored != null);
		
		assertModelEquals(original, stored);

		MonitorModel updated = stored;
		
		updated.setName("Changed");
		boolean result = provider.update(updated);
		assertTrue("Update should have returned true", result);
		stored = provider.get(id);
		assertTrue("Stored model shoud not be null", stored != null);
		assertModelEquals(updated, stored);
		
		/*
		updated.setType(TODO);
		result = provider.update(updated);
		assertTrue("Update should have returned true", result);
		stored = provider.get(id);
		assertTrue("Stored model shoud not be null", stored != null);
		assertModelEquals(updated, stored);
		*/

		updated.setFrequency(999);
		result = provider.update(updated);
		assertTrue("Update should have returned true", result);
		stored = provider.get(id);
		assertTrue("Stored model shoud not be null", stored != null);
		assertModelEquals(updated, stored);

		updated.setNotifyWhenDown(true);
		result = provider.update(updated);
		assertTrue("Update should have returned true", result);
		stored = provider.get(id);
		assertTrue("Stored model shoud not be null", stored != null);
		assertModelEquals(updated, stored);

		updated.setNotifyWhenUp(true);
		result = provider.update(updated);
		assertTrue("Update should have returned true", result);
		stored = provider.get(id);
		assertTrue("Stored model shoud not be null", stored != null);
		assertModelEquals(updated, stored);

		updated.setCreated(new Date());
		result = provider.update(updated);
		assertTrue("Update should have returned true", result);
		stored = provider.get(id);
		assertTrue("Stored model shoud not be null", stored != null);
		assertModelEquals(updated, stored);

		updated.setAmended(new Date());
		result = provider.update(updated);
		assertTrue("Update should have returned true", result);
		stored = provider.get(id);
		assertTrue("Stored model shoud not be null", stored != null);
		assertModelEquals(updated, stored);

		updated.setUnavailableThreshold(9999);
		result = provider.update(updated);
		assertTrue("Update should have returned true", result);
		stored = provider.get(id);
		assertTrue("Stored model shoud not be null", stored != null);
		assertModelEquals(updated, stored);

		updated.setUnavailableCount(9999);
		result = provider.update(updated);
		assertTrue("Update should have returned true", result);
		stored = provider.get(id);
		assertTrue("Stored model shoud not be null", stored != null);
		assertModelEquals(updated, stored);

	}

	public void testGetAll() {
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteMonitorProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteMonitorProvider(this.mContext);

		MonitorModel original[] = new MonitorModel[5];
		long[] id = new long[5];

		for (int i = 0; i < original.length; i++) {
			original[i] = new MonitorModel();
			original[i].setName("Test " + i);
			original[i].setType(MonitorType.HTTP);
			original[i].setFrequency(i);
			original[i].setNotifyWhenDown(false);
			original[i].setNotifyWhenUp(false);
			original[i].setUnavailableThreshold(i);
			original[i].setUnavailableCount(i);
			original[i].setCreated(new Date());
			original[i].setAmended(new Date());
			
			id[i] = provider.add(original[i]);
			assertTrue("After add, id should be greater than zero", id[i] > 0);
		}

		MonitorModel[] stored = provider.getAll();
		
		assertTrue("Stored model array shoud not be null", stored != null);
		assertTrue("Stroed model array should be 5 long", stored.length == 5);
		
		for (int i = 0; i < stored.length; i++) {
			assertModelEquals(original[i], stored[i]);
		}
	}

	public void testGetByName() {
		MonitorModel original = new MonitorModel();
		
		original.setName("Test");
		original.setType(MonitorType.HTTP);
		original.setFrequency(1);
		original.setNotifyWhenDown(false);
		original.setNotifyWhenUp(false);
		original.setUnavailableThreshold(10);
		original.setUnavailableCount(5);
		original.setCreated(new Date());
		original.setAmended(new Date());
		
		com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteMonitorProvider provider = new com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteMonitorProvider(this.mContext);
		
		long id = provider.add(original);
		
		assertTrue("After add, id should be greater than zero", id > 0);
		
		MonitorModel[] stored = provider.getByName(original.getName());
		
		assertTrue("Stored model shoud not be null", stored != null);
		assertEquals("Array should only have 1 record", stored.length, 1);
		
		assertModelEquals(original, stored[0]);
	}



	private void assertModelEquals(MonitorModel first, MonitorModel second) {
		assertModelEquals(first, second, false);
	}

	private void assertModelEquals(MonitorModel first, MonitorModel second, boolean includeID) {
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
		
	}

}
