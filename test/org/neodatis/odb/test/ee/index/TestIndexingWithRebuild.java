package org.neodatis.odb.test.ee.index;

import org.neodatis.odb.ODB;
import org.neodatis.odb.test.ODBTest;

import java.util.Date;

/**
 * Junit to test indexing an object when the index field is an object and not a
 * native attribute
 */
public class TestIndexingWithRebuild extends ODBTest {

	public void test1() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		String [] fields = {"uuid", "createdAt", "parent", "active"};
		if (!odb.getClassRepresentation(Soul.class).existIndex("Csoul-index")) {
			odb.getClassRepresentation(Soul.class).addIndexOn("Csoul-index", fields, true);
		} else {
			odb.getClassRepresentation(Soul.class).rebuildIndex ("Csoul-index", true);
		}
		if (!odb.getClassRepresentation(Body.class).existIndex("Cbody-index")) {
			odb.getClassRepresentation(Body.class).addIndexOn("Cbody-index", fields, true);
		} else {
			odb.getClassRepresentation(Body.class).rebuildIndex ("Cbody-index", true);
		}
		if (!odb.getClassRepresentation(CObject.class).existIndex("Cobject-index")) {
			odb.getClassRepresentation(CObject.class).addIndexOn("Cobject-index", fields, true);
		} else {
			odb.getClassRepresentation(CObject.class).rebuildIndex ("Cobject-index", true);
		}
		if (!odb.getClassRepresentation(Item.class).existIndex("Citem-index")) {
			odb.getClassRepresentation(Item.class).addIndexOn("Citem-index", fields, true);
		} else {
			odb.getClassRepresentation(Item.class).rebuildIndex ("Citem-index", true);
		}
		odb.close();
		
		
		odb = open(baseName);
		int size = 10;
		for(int i=0;i<size;i++){
			odb.store(new Soul("soul"+i, "parent-soul-"+i, new Date(), "objectType-soul-"+i, Boolean.TRUE));
			odb.store(new Body("body"+i, "parent-body-"+i, new Date(), "objectType-body-"+i, Boolean.TRUE));
			odb.store(new Item("item"+i, "parent-item"+i, new Date(), "objectType-parent-"+i, Boolean.TRUE));
			odb.store(new Soul("cobject"+i, "parent-cobject-"+i, new Date(), "objectType-cobject-"+i, Boolean.TRUE));
		}
			
		odb.close();
	}	
}
