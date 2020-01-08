/**
 * 
 */
package org.neodatis.odb.test.performance.comparison;

/**
 * @author olivier
 *
 */
public class PerformanceComparison {
	/*
	protected VoldemortStoreServer voldemortServer;
	
	
	public static int TEST_SIZE = 20000;
	private SimpleObject getSimpleObjectInstance(int i) {
		SimpleObject so = new SimpleObject();
		so.setDate(new Date());
		so.setDuration(i);
		so.setName("Bonjour, comment allez vous?" + i);
		return so;
	}
	
	
	public void executeBerkeleyDbLocal(){
		IOUtil.deleteDirectory("test-berkeleydb.neodatis");
		NeoDatisConfig properties = NeoDatis.getConfig();
		properties.setStorageEngineClass(NeoDatisBerkeleyDBPlugin.class);
		execute(properties, "test-berkeleydb.neodatis", "BerkeleyDB local");
	}
	

	private void startVoldemortServer() {
		IOUtil.deleteDirectory("test-voldemort.neodatis");
		if(voldemortServer!=null){
			voldemortServer.stop();
		}
		voldemortServer = new VoldemortStoreServer("test-voldemort.neodatis",NeoDatis.getConfig().setHostAndPort("localhost", 10002));
		voldemortServer.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void executeVoldemortLocal(){
		
		NeoDatisConfig properties = NeoDatis.getConfig().setHostAndPort("localhost", 10002);
		properties.setStorageEngineClass(NeoDatisVoldemortPlugin.class);
		execute(properties, "test-voldemort.neodatis", "Voldemort");
	}
	
	protected void execute(NeoDatisConfig properties, String baseName, String name){
		boolean doUpdate = true;
		boolean doDelete = true;
		boolean doSelect = true;

		boolean inMemory = true;

		long t1 = 0, t2 = 0, t3 = 0, t4 = 0, t5 = 0, t6 = 0, t7 = 0, t77 = 0, t8 = 0;
		ODB odb = null;
		Objects l = null;
		SimpleObject so = null;

		// Insert TEST_SIZE objects
		println("Inserting " + TEST_SIZE + " objects");

		t1 = OdbTime.getCurrentTimeInMs();
		odb = NeoDatis.open(baseName,properties);
		int i = 0;

		for (i = 0; i < TEST_SIZE; i++) {
			Object o = getSimpleObjectInstance(i);
			ObjectOid oid = odb.store(o);
			if (i % 1000 == 0) {
				println("i="+i + " - oid="+oid);
			}
		}
		t2 = OdbTime.getCurrentTimeInMs();
		// Closes the database
		odb.close();
		// if(true)return;
		t3 = OdbTime.getCurrentTimeInMs();

		println("Retrieving " + TEST_SIZE + " objects");
		// Reopen the database
		odb = NeoDatis.open(baseName,properties);

		if (doSelect) {
			// Gets the TEST_SIZE objects
			Query q = odb.query(SimpleObject.class);
			q.getQueryParameters().setInMemory(inMemory);
			l = q.objects();
			t4 = OdbTime.getCurrentTimeInMs();

			i = 0;
			while (l.hasNext()) {
				Object o = l.next();
				if (i % 10000 == 0) {
					// Monitor.displayCurrentMemory("select "+i,true);
				}
				i++;
			}
		}
		t5 = OdbTime.getCurrentTimeInMs();

		if (doUpdate) {
			println("Updating " + TEST_SIZE + " objects");
			i = 0;
			so = null;
			l.reset();
			while (l.hasNext()) {
				so = (SimpleObject) l.next();
				so.setName(so.getName() + " updated");
				odb.store(so);
				if (i % 10000 == 0) {
					// Monitor.displayCurrentMemory(""+i);
				}
				i++;
			}
		}

		t6 = OdbTime.getCurrentTimeInMs();

		odb.close();
		t7 = OdbTime.getCurrentTimeInMs();

		if (doDelete) {

			println("Deleting " + TEST_SIZE + " objects");
			odb = NeoDatis.open(baseName,properties);

			Query q = odb.query(SimpleObject.class);
			q.getQueryParameters().setInMemory(inMemory);
			l = q.objects();
			t77 = OdbTime.getCurrentTimeInMs();
			// println("After getting objects - before delete");

			i = 0;

			while (l.hasNext()) {
				so = (SimpleObject) l.next();
				if (!so.getName().endsWith("updated")) {
					throw new RuntimeException("Update  not ok for " + so.getName());
				}
				odb.delete(so);
				//if (i % 10000 == 0) {
					
				//}
				i++;
			}
			odb.close();

		}
		t8 = OdbTime.getCurrentTimeInMs();

		displayResult(name, " ODB " + TEST_SIZE + " SimpleObject objects ", t1, t2, t3, t4, t5, t6, t7, t77, t8);
		
	}
	
	private void println(String string) {
		//System.out.println(string);
		
	}


	private void displayResult(String name, String string, long t1, long t2, long t3, long t4, long t5, long t6, long t7, long t77, long t8) {

		String s1 = " total=" + (t8 - t1);
		String s2 = " total insert=" + (t3 - t1) + " -- " + "insert=" + (t2 - t1) + " commit=" + (t3 - t2) + " o/s=" + (float) TEST_SIZE
				/ (float) ((t3 - t1)) * 1000;
		String s3 = " total select=" + (t5 - t3) + " -- " + "select=" + (t4 - t3) + " get=" + (t5 - t4) + " o/s=" + (float) TEST_SIZE
				/ (float) ((t5 - t3)) * 1000;
		String s4 = " total update=" + (t7 - t5) + " -- " + "update=" + (t6 - t5) + " commit=" + (t7 - t6) + " o/s=" + (float) TEST_SIZE
				/ (float) ((t7 - t5)) * 1000;
		String s5 = " total delete=" + (t8 - t7) + " -- " + "select=" + (t77 - t7) + " - delete=" + (t8 - t77) + " o/s="
				+ (float) TEST_SIZE / (float) ((t8 - t7)) * 1000;

		System.out.println(name + " => " + string + s1 + " | " + s2 + " | " + s3 + " | " + s4 + " | " + s5);

		long tinsert = t3 - t1;
		long tselect = t5 - t3;
		long tupdate = t7 - t5;
		long tdelete = t8 - t7;


	}

	
	public static void testBerkeleyDb(int n){
		PerformanceComparison c = new PerformanceComparison();
		
		for(int i=0;i<n;i++){
			c.executeBerkeleyDbLocal();
		}
		
	}
	public static void  testVoldemort(){
		PerformanceComparison c = new PerformanceComparison();
		
		for(int i=0;i<10;i++){
			c.startVoldemortServer();
			c.executeVoldemortLocal();
		}
		
	}
	
	public static void main(String[] args) {
		testBerkeleyDb(1);
	}
	*/

}
