/*
 * Copyright (C) 2016 geoagdt.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.util;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;
import uk.ac.leeds.ccg.andyt.generic.util.Generic_Collections;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.DW_PersonID;

/**
 *
 * @author geoagdt
 */
public class DW_Collections extends Generic_Collections {
    
    /**
     * Returns the count of all values in the map (the sum of all the number of
     * things in the Sets), null values are not allowed.
     *
     * @param m
     * @return
     */
    public static int getCountHashMap_DW_PersonID__HashSet_DW_ID(HashMap<DW_PersonID, HashSet<DW_ID>> m) {
        int result = 0;
        Collection<HashSet<DW_ID>> c;
        c = m.values();
        Iterator<HashSet<DW_ID>> ite;
        ite = c.iterator();
        while (ite.hasNext()) {
            result += ite.next().size();
        }
        return result;
    }

    /**
     * Returns the count of all values in the map (the sum of all the number of
     * things in the Sets), null values are not allowed.
     *
     * @param m
     * @return
     */
    public static int getCountHashMap_DW_ID__HashSet_DW_PersonID(HashMap<DW_ID, HashSet<DW_PersonID>> m) {
        int result = 0;
        Collection<HashSet<DW_PersonID>> c;
        c = m.values();
        Iterator<HashSet<DW_PersonID>> ite;
        ite = c.iterator();
        while (ite.hasNext()) {
            result += ite.next().size();
        }
        return result;
    }
    
    public static DW_ID getKeyOfSetValue(
            HashMap<DW_ID, HashSet<DW_PersonID>> map,
            DW_PersonID DW_PersonID) {
        DW_ID result;
        Set<Entry<DW_ID, HashSet<DW_PersonID>>> mapEntrySet;
        mapEntrySet = map.entrySet();
        Iterator<Entry<DW_ID, HashSet<DW_PersonID>>> ite;
        ite = mapEntrySet.iterator();
        Entry<DW_ID, HashSet<DW_PersonID>> entry;
        HashSet<DW_PersonID> set;
        while (ite.hasNext()) {
            entry = ite.next();
            result = entry.getKey();
            if (entry.getValue().contains(DW_PersonID)) {
                return result;
            }
        }
        return null;
    }

    public static HashMap<DW_ID, String> getHashMap_DW_ID__String(File f) {
        HashMap<DW_ID, String> r;
        if (f.exists()) {
            r = (HashMap<DW_ID, String>) Generic_IO.readObject(f);
        } else {
            r = new HashMap<>();
        }
        return r;
    }

    public static HashSet<DW_ID> getHashSet_DW_ID(File f) {
        HashSet<DW_ID> result;
        if (f.exists()) {
            result = (HashSet<DW_ID>) Generic_IO.readObject(f);
        } else {
            result = new HashSet<>();
        }
        return result;
    }
    
    public static HashSet<DW_PersonID> getHashSet_DW_PersonID(File f) {
        HashSet<DW_PersonID> result;
        if (f.exists()) {
            result = (HashSet<DW_PersonID>) Generic_IO.readObject(f);
        } else {
            result = new HashSet<>();
        }
        return result;
    }
    
    public static HashMap<DW_PersonID, HashSet<DW_ID>> getHashMap_DW_PersonID__HashSet_DW_ID(
            File f) {
        HashMap<DW_PersonID, HashSet<DW_ID>> result;
        if (f.exists()) {
            result = (HashMap<DW_PersonID, HashSet<DW_ID>>) Generic_IO.readObject(f);
        } else {
            result = new HashMap<>();
        }
        return result;
    }
}
