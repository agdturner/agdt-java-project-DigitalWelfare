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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe;

import java.io.Serializable;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;

/**
 *
 * @author geoagdt
 */
public class ID_PostcodeID implements Serializable {

    private DW_ID ID;
    private DW_ID PostcodeID; // Rather than using a detailed PostcodeID use a simple DW_ID.

    public ID_PostcodeID() {
    }

    public ID_PostcodeID(
            DW_ID ID,
            DW_ID PostcodeID
    ) {
        this.ID = ID;
        this.PostcodeID = PostcodeID;
    }

    /**
     * @return the Postcode
     */
    public DW_ID getID() {
        return ID;
    }

    /**
     * @return the Postcode
     */
    public DW_ID getPostcodeID() {
        return PostcodeID;
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof ID_PostcodeID) {
            ID_PostcodeID o;
            o = (ID_PostcodeID) obj;
            if (this.ID.equals(o.ID)) {
                if (PostcodeID.equals(o.PostcodeID)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.ID != null ? this.ID.hashCode() : 0);
        hash = 67 * hash + (this.PostcodeID != null ? this.PostcodeID.hashCode() : 0);
        return hash;
    }
   
}
