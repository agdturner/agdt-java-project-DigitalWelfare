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
 *
 * @author geoagdt
 */
public class DW_ID implements Serializable {

    private final int ID;

    public DW_ID(int ID
    ) {
        this.ID = ID;
    }

    /**
     * @return the DW_ID
     */
    public int getID() {
        return ID;
    }
    
    @Override
    public String toString() {
        return "" + ID;
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
                return o.ID == ID;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.ID;
        return hash;
    }

}
