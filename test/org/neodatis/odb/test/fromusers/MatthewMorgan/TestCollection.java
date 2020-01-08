/**
 * 
 */
package org.neodatis.odb.test.fromusers.MatthewMorgan;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Query;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.fromusers.MatthewMorgan.Receipt.ItemLine;

import java.util.Date;

/**
 * @author olivier
 * 
 */
public class TestCollection extends ODBTest {
	public void test1() {
		String baseName = getBaseName();

		ODB odb = null;

		try {
			odb = open(baseName);

			Receipt r = new Receipt(1, new Date(), 2);

			for (int i = 0; i < 10; i++) {
				r.addItem(i, "desc" + i);
			}
			odb.store(r);

			odb.commit();
			ItemLine il = (ItemLine) odb.query(Receipt.ItemLine.class, W.equal("itemNum", 8)).objects().first();

			assertEquals(8, il.getItemNum());
			assertEquals("desc8", il.getItemDescription());

			Query q = odb.query(Receipt.class, W.contain("itemLines", il));
			Receipt r2 = (Receipt) q.objects().first();

			assertEquals(r.getCustomerId(), r2.getCustomerId());
			assertEquals(r.getDocId(), r2.getDocId());
			assertEquals(r.getTimestamp(), r2.getTimestamp());
			assertEquals(r.getItemLines().size(), r2.getItemLines().size());

		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}

}
