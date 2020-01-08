package org.neodatis.odb.test.delete.withmap;

import org.neodatis.odb.ODB;
import org.neodatis.odb.OID;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;

import java.util.ArrayList;
import java.util.List;


/**
 * Test created by Thiago Santana
 * @author tsantana
 *
 */
public class TestDeleteWithMap extends ODBTest{
	
	private static final int MAXPARENTS=5;
	private static final int MAXCHILDRENPERPARENT=2;

	public void testCreateObjectsWithMapWithoutChildren(){
		String baseName = getBaseName();
		
		ODB odb = open(baseName);
		List<TBugger> listParents = new ArrayList<TBugger>();
		
		for ( int x=0; x < MAXPARENTS; x++){
			TBugger tbugger = new TBugger(x);
			
			if (x%2==0 && x%7==0 ){
				tbugger.setDelete(1);
			}
			listParents.add(tbugger);
		}
		
		// to keep track of parent oids
		List<OID> parentOids = new ArrayList<OID>();
		
		int i=1;
		for (TBugger tbuggerParent : listParents ){
			
			parentOids.add(odb.store(tbuggerParent));
			//odb.commit();
			
			i++;
			
		}
		odb.close();
		
		odb = open(baseName);
		System.out.println("Parent OIDs = " + parentOids);
		Objects<TBugger> tbuggers = odb.query(TBugger.class).objects();
		odb.close();
		System.out.println(tbuggers.size());

		assertEquals(MAXPARENTS, tbuggers.size());
		


	}
	public void testCreateObjectsWithMap(){
		String baseName = getBaseName();
		
		ODB odb = open(baseName);
		List<TBugger> listParents = new ArrayList<TBugger>();
		List<TBugger> listOfChildren = new ArrayList<TBugger>();
		
		for ( int x=0; x < MAXPARENTS; x++){
			TBugger tbugger = new TBugger(x);
			
			if (x%2==0 && x%7==0 ){
				tbugger.setDelete(1);
			}
			listParents.add(tbugger);
		}
		
		for ( int y = 0; y < MAXCHILDRENPERPARENT; y++){
			TBugger tbugger = new TBugger(y+MAXPARENTS);
			
			if ( y%2==0 && y%7==0 ){
				tbugger.setDelete(1);
			}
			listOfChildren.add(tbugger);
		}
		
		// to keep track of parent oids
		List<OID> parentOids = new ArrayList<OID>();
		// to keep track of children oids
		List<OID> childrenOids = new ArrayList<OID>();
		
		int i=1;
		for (TBugger tbuggerParent : listParents ){
			
			
			int j=1;
			for (TBugger tbuggerChild : listOfChildren ){
				
				System.out.println("Adding child " + j + " for parent " + i);
				tbuggerChild.addTBuggerChildren(tbuggerParent, 2);
				
				//childrenOids.add(odb.store(tbuggerChild));
				odb.commit();
				tbuggerParent.addTBuggerChildren(tbuggerChild, j++);
				
			}
			
			parentOids.add(odb.store(tbuggerParent));
			odb.commit();
			
			i++;
			
		}

		System.out.println("Parent OIDs = " + parentOids);
		System.out.println("Children OIDs = " + childrenOids);
		Objects<TBugger> tbuggers = odb.query(TBugger.class).objects();
		odb.close();
		System.out.println(tbuggers.size());

		assertEquals(MAXCHILDRENPERPARENT+MAXPARENTS, tbuggers.size());
		


	}
}
