package org.neodatis.odb.core.layers.layer4.plugin.jdbmv3;

import java.io.IOException;
import java.util.SortedMap;

/**
 * Created with IntelliJ IDEA.
 * User: olivier
 * Date: 8/13/12
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestJdbm3 {
    public static void main(String[] args) throws IOException {
        JDBM3Wrapper util = new JDBM3Wrapper("data/test1");
        //open an collection, TreeMap has better performance then HashMap

        SortedMap<String, byte[]> map1 = (SortedMap<String, byte[]>) util.getMap("User");

        int size = 1000000;
        for(int i=0;i<size;i++){
            map1.put("oid-oid-oid-"+i, ("onefdklskfkdjvklfdjkljvkldfjvfgl-"+i).getBytes());
            if(i%1000==0){
                System.out.println(i);
            }
        }
        //map.keySet() is now [1,2] even before commit

        util.commitAndClose();  //persist changes into disk


        util = new JDBM3Wrapper("data/test1");
        //open an collection, TreeMap has better performance then HashMap

        map1 = (SortedMap<String, byte[]>) util.getMap("User");

        System.out.println("Size of map " + map1.size());
    }


}
