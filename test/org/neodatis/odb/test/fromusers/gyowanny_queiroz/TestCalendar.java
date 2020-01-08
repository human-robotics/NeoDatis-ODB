/**
 * 
 */
package org.neodatis.odb.test.fromusers.gyowanny_queiroz;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.core.query.criteria.Criterion;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

/**
 * @author olivier
 * 
 */
public class TestCalendar extends ODBTest {

	public void test1() {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		ClassWithCalendar cwc = new ClassWithCalendar("c1", Calendar.getInstance());
		odb.store(cwc);
		odb.close();
		
		odb = open(baseName);
		Calendar from = Calendar.getInstance();
		from.add(Calendar.DAY_OF_MONTH, -1);
		Calendar to = Calendar.getInstance();
		to.add(Calendar.DAY_OF_MONTH, +1);

		System.out.println(System.identityHashCode(from) + " | " + System.identityHashCode(to));
		System.out.println(from.getTime()+ " | " + to.getTime() + " | comp=");
		Criterion criteria = W.equal("name", "c1").and(W.ge("calendar", from)).and(W.le("calendar", to));

		System.out.println(criteria);
		Query q = odb.query(ClassWithCalendar.class, criteria);
		q.setOptimizeObjectComparison(false);
		Objects<ClassWithCalendar> ww = q.objects();
		odb.close();
		assertEquals(1, ww.size());

	}
	
	public void testCountWithCalendar() {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		ClassWithCalendar cwc = new ClassWithCalendar("c1", Calendar.getInstance());
		odb.store(cwc);
		odb.close();

		odb = open(baseName);
		Calendar from = Calendar.getInstance();
		from.add(Calendar.DAY_OF_MONTH, -1);
		Calendar to = Calendar.getInstance();
		to.add(Calendar.DAY_OF_MONTH, +1);

		Criterion criteria = W.equal("name", "c1").and(W.ge("calendar", from)).and(W.le("calendar", to));
		Query q = odb.query(ClassWithCalendar.class, criteria);
		//q.setOptimizeObjectComparison(false);

		BigInteger count = odb.count((CriteriaQuery) q);
		odb.close();
		assertEquals(1, count.intValue());

	}

	public void test2() {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		ClassWithDate cwc = new ClassWithDate("c1", new Date());
		odb.store(cwc);
		odb.close();

		odb = open(baseName);
		Calendar from = Calendar.getInstance();
		from.add(Calendar.DAY_OF_MONTH, -1);
		Calendar to = Calendar.getInstance();
		to.add(Calendar.DAY_OF_MONTH, +1);

		Criterion criteria = W.and().add(W.equal("name", "c1")).add(W.ge("calendar", from.getTime())).add(
				W.le("calendar", to.getTime()));
		println(criteria);

		Objects<ClassWithDate> ww = odb.query(ClassWithDate.class, criteria).objects();
		odb.close();
		assertEquals(1, ww.size());

	}

}
