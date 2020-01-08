package org.neodatis.odb.test.uid;

import junit.framework.Assert;
import org.junit.Test;
import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;

import java.util.UUID;

public class TestStoreUUID extends ODBTest {

    @Test
    public void testStoreAndRetrieve() throws Exception {
        // from http://stackoverflow.com/questions/11332943/uuid-field-not-stored-by-neodatis
        TestObj o1 = new TestObj();

        ODB odb = null;
        try {
            NeoDatisConfig config = NeoDatis.getConfig().setDebugLayers(false);
            config.setEnableEmptyConstructorCreation(true);
            odb = open(getBaseName(), config);

            odb.store(o1);
            odb.close();

            odb = open(getBaseName(), config);
            Objects<TestObj> l = odb.query(TestObj.class).objects();
            assertEquals(1, l.size());

            TestObj o2 = l.first();

            UUID id1 = o1.getId();
            UUID id2 = o2.getId();
            UUID id3 = UUID.fromString(o2.getId().toString());
            Assert.assertEquals(o2.getId().toString(), o1.getId().toString());
            boolean b = id1.equals(id2);
            int v1 = id1.variant();
            int v2 = id2.variant();
            System.out.println(v1);
            System.out.println(v2);
            Assert.assertEquals(id1,id3);
            Assert.assertEquals(id1,id2);   /*this one fail!*/


        } catch (Exception e) {
            if (odb != null) {
                odb.rollback();
                odb = null;
            }
            throw e;
        } finally {
            if (odb != null) {
                odb.close();
            }
        }
    }
}


class TestObj {
    public UUID getId() {
        return id;
    }

    private final UUID id = UUID.randomUUID();
}