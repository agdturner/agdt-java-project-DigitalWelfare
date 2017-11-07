/*
 * Copyright (C) 2015 geoagdt.
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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core;

import java.io.Serializable;

/**
 * A simple class for identifiers that uses a single long. There can only be as 
 * many unique identifiers as there are long numbers.
 *
 * @author geoagdt
 */
public class DW_ID implements Serializable, Comparable<DW_ID> {

    protected final long l;

    public DW_ID(
            DW_ID ID) {
        l = ID.l;
    }

    public DW_ID(
            long ID) {
        l = ID;
    }

    /**
     * @return the DW_ID
     */
    public long getID() {
        return l;
    }

    @Override
    public String toString() {
        return "" + l;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof DW_ID) {
            DW_ID o;
            o = (DW_ID) obj;
            if (hashCode() == o.hashCode()) {
                return o.l == l;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (int) (l ^ (l >>> 32));
        return hash;
    }

    public int compareTo(DW_ID t) {
        if (l > t.l) {
            return 1;
        } else if (l < t.l) {
            return -1;
        }
        return 0;
    }

}
