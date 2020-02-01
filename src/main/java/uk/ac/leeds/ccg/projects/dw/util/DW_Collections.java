package uk.ac.leeds.ccg.projects.dw.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import uk.ac.leeds.ccg.generic.util.Generic_Collections;
import uk.ac.leeds.ccg.projects.dw.core.DW_ID;
import uk.ac.leeds.ccg.data.shbe.data.id.SHBE_PersonID;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;

/**
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_Collections extends Generic_Collections {

    protected final DW_Environment env;

    public DW_Collections(DW_Environment e) {
        env = e;
    }

    /**
     * Returns the count of all values in the map (the sum of all the number of
     * things in the Sets), null values are not allowed.
     *
     * @param m
     * @return
     */
    public static int getCountHashMap_DW_PersonID__HashSet_DW_ID(
            HashMap<SHBE_PersonID, HashSet<DW_ID>> m) {
        int r = 0;
        Collection<HashSet<DW_ID>> c = m.values();
        Iterator<HashSet<DW_ID>> ite = c.iterator();
        while (ite.hasNext()) {
            r += ite.next().size();
        }
        return r;
    }

    /**
     * Returns the count of all values in the map (the sum of all the number of
     * things in the Sets), null values are not allowed.
     *
     * @param m
     * @return
     */
    public static int getCountHashMap_DW_ID__HashSet_DW_PersonID(
            HashMap<DW_ID, HashSet<SHBE_PersonID>> m) {
        int r = 0;
        Collection<HashSet<SHBE_PersonID>> c = m.values();
        Iterator<HashSet<SHBE_PersonID>> ite = c.iterator();
        while (ite.hasNext()) {
            r += ite.next().size();
        }
        return r;
    }

    public static DW_ID getKeyOfSetValue(
            HashMap<DW_ID, HashSet<SHBE_PersonID>> map,
            SHBE_PersonID pid) {
        Set<Entry<DW_ID, HashSet<SHBE_PersonID>>> s = map.entrySet();
        Iterator<Entry<DW_ID, HashSet<SHBE_PersonID>>> ite = s.iterator();
        while (ite.hasNext()) {
            Entry<DW_ID, HashSet<SHBE_PersonID>> entry = ite.next();
            DW_ID r = entry.getKey();
            if (entry.getValue().contains(pid)) {
                return r;
            }
        }
        return null;
    }

    public HashMap<DW_ID, String> getHashMap_DW_ID__String(Path f)
            throws IOException, ClassNotFoundException {
        HashMap<DW_ID, String> r;
        if (Files.exists(f)) {
            r = (HashMap<DW_ID, String>) Generic_IO.readObject(f);
        } else {
            r = new HashMap<>();
        }
        return r;
    }

    public HashSet<DW_ID> getHashSet_DW_ID(Path f) throws IOException,
            ClassNotFoundException {
        HashSet<DW_ID> r;
        if (Files.exists(f)) {
            r = (HashSet<DW_ID>) Generic_IO.readObject(f);
        } else {
            r = new HashSet<>();
        }
        return r;
    }

    public HashSet<SHBE_PersonID> getHashSet_DW_PersonID(Path f)
            throws IOException, ClassNotFoundException {
        HashSet<SHBE_PersonID> r;
        if (Files.exists(f)) {
            r = (HashSet<SHBE_PersonID>) Generic_IO.readObject(f);
        } else {
            r = new HashSet<>();
        }
        return r;
    }

    public HashMap<SHBE_PersonID, HashSet<DW_ID>> getHashMap_DW_PersonID__HashSet_DW_ID(
            Path f) throws IOException, ClassNotFoundException {
        HashMap<SHBE_PersonID, HashSet<DW_ID>> r;
        if (Files.exists(f)) {
            r = (HashMap<SHBE_PersonID, HashSet<DW_ID>>) Generic_IO.readObject(f);
        } else {
            r = new HashMap<>();
        }
        return r;
    }
}
