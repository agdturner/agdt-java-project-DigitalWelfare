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
 * A simple class for representing identifiers using an long.
 *
 * @author geoagdt
 */
public class DW_ID implements Serializable {

    protected final int i;

    public DW_ID(
            DW_ID ID
    ) {
        this.i = ID.i;
    }
    
    public DW_ID(
            int ID
    ) {
        this.i = ID;
    }

    /**
     * @return the DW_ID
     */
    public int getID() {
        return i;
    }

    @Override
    public String toString() {
        return "" + i;
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
            if (this.hashCode() == o.hashCode()) {
                return o.i == i;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.i;
        return hash;
    }

}
