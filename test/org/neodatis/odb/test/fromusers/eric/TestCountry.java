/**
 * 
 */
package org.neodatis.odb.test.fromusers.eric;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;

/**
 * @author olivier
 *
 */
public class TestCountry extends ODBTest{
	public void test1(){
		String baseName = getBaseName();
        ODB odb = open(baseName);
        
        odb.store(new Country("Bananaland"));
        
        Place ilike = new Place();
        ilike.setLike(new Country("Brazil"));
        
        Place p = new Place();
        p.setLike(new Country("Waterworld"));
        p.setDislike(new Country("McDonaldland"));
        odb.store(ilike);
        odb.store(p);
        odb.commit();
        
        Objects<Country> objs = odb.query(Country.class).objects();
        //Objects<Country> objs = odb.query(Country.class).objects();
        for (Country c: objs) {
                System.out.println(c);                
        }
        odb.close();
        assertEquals(4, objs.size());
	}

}
