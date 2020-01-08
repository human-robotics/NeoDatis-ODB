package org.neodatis.odb.core.layers.layer4.plugin.jdbmv3;

import org.neodatis.odb.ClassOid;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.core.layers.layer4.ClassOidIterator;
import org.neodatis.odb.core.layers.layer4.ObjectOidIterator;
import org.neodatis.odb.core.layers.layer4.OidGenerator;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;

public class Jdbm3ObjectOidIterator implements ObjectOidIterator {
    protected OidGenerator oidGenerator;
    protected SortedMap<String, byte[]> btree;
    protected Set<String> keys;
    protected int currentPosition;
    protected Iterator<String> internalIterator;

    public Jdbm3ObjectOidIterator(SortedMap<String, byte[]> btree, OidGenerator oidGenerator){
        this.btree = btree;
        this.keys = btree.keySet();
        this.internalIterator = keys.iterator();
        this.oidGenerator = oidGenerator;
    }

    public void startAtTheEnd() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void startAtTheBeginning() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean hasNext() {
        return internalIterator.hasNext();
    }

    public ObjectOid next() {
        return oidGenerator.objectOidFromString(internalIterator.next());
    }

    public void reset() {
        currentPosition = 0;
    }

    public Iterator<ObjectOid> iterator() {
        return (Iterator<ObjectOid>)this;
    }
}
