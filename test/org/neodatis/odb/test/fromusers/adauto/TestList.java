/**
 * 
 */
package org.neodatis.odb.test.fromusers.adauto;

import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.core.query.QueryFactory;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;

/**
 * @author olivier
 *
 */
public class TestList extends ODBTest {
	public void test1(){
		String baseName=getBaseName();

		Dao<ListWrapper> dao = new Dao<ListWrapper>(baseName);
		ListWrapper lw = new ListWrapper("Genero");
		lw.getList().add("item1");
		lw.getList().add("item2");
		dao.create(lw);
		dao.close();
		
		dao = new Dao<ListWrapper>(baseName);
		Query query = QueryFactory.query(ListWrapper.class,W.equal("type","Genero")); 
		lw = dao.read(query).first(); 
		lw.getList().add("item B1");  
		lw.getList().add("item B2");  
		dao.update(lw); 
		dao.close();
		
		dao = new Dao<ListWrapper>(baseName);
		Objects<ListWrapper> lws = dao.read(query);
		dao.close();
		assertEquals(1, lws.size());
		lw = lws.first();

		assertEquals(4, lw.getList().size());
	}
}
