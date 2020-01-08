package jdbm;

import junit.framework.TestCase;
import org.neodatis.odb.ClassOid;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.core.layers.layer4.plugin.jdbmv3.JDBM3Wrapper;
import org.neodatis.odb.core.oid.uuid.ClassOidImpl;
import org.neodatis.odb.core.oid.uuid.ObjectOidImpl;

import java.io.IOException;
import java.util.UUID;

public class TestJdbm3Wrapper extends TestCase {

    public void test1() throws IOException {
        JDBM3Wrapper w = new JDBM3Wrapper("unit-test/test1");

        ClassOid coid = new ClassOidImpl(UUID.randomUUID());
        ObjectOid ooid = new ObjectOidImpl(UUID.randomUUID(), coid);

        int size = 1000000;
        for (int i = 0; i < size; i++) {
            w.put(ooid, "Test to store bytes".getBytes());
            if(i%10000==0){
                System.out.println(i);
            }
        }

        w.commitAndClose();

        w = new JDBM3Wrapper("unit-test/test1");
        byte[] bytes = (byte[]) w.get(ooid);
        System.out.println(new String(bytes));
        w.close();
    }
}
