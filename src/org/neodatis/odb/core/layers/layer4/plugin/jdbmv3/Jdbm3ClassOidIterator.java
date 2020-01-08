package org.neodatis.odb.core.layers.layer4.plugin.jdbmv3;

import org.neodatis.odb.ClassOid;
import org.neodatis.odb.core.layers.layer4.ClassOidIterator;
import org.neodatis.odb.core.layers.layer4.OidGenerator;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;

public class Jdbm3ClassOidIterator implements ClassOidIterator {
    protected OidGenerator oidGenerator;
    protected SortedMap<String, byte[]> btree;
    protected Set<String> keys;
    protected int currentPosition;
    protected Iterator<String> internalIterator;

    public Jdbm3ClassOidIterator(SortedMap<String, byte[]> btree, OidGenerator oidGenerator){
        this.btree = btree;
        this.keys = btree.keySet();
        this.internalIterator = keys.iterator();
        this.oidGenerator = oidGenerator;
    }
    public boolean hasNext() {
        return internalIterator.hasNext();
    }

    public ClassOid next() {
        return oidGenerator.classOidFromString(internalIterator.next());
    }

    public void reset() {
        currentPosition = 0;
    }

    public Iterator<ClassOid> iterator() {
        return (Iterator<ClassOid>)this;
    }
}
