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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;

/**
 *
 * @author geoagdt
 */
public class ID_TenancyType_PostcodeID implements Serializable {

    private final ID_TenancyType ID_TenancyType;
    private final DW_ID PostcodeID; // Rather than using a detailed PostcodeID use a simple DW_ID.

    public ID_TenancyType_PostcodeID(
            ID_TenancyType ID_TenancyType,
            DW_ID PostcodeID
    ) {
        this.ID_TenancyType = ID_TenancyType;
        this.PostcodeID = PostcodeID;
    }

    /**
     * @return the TenancyType
     */
    public int getTenancyType() {
        return ID_TenancyType.getTenancyType(); // For convenience
    }

    /**
     * @return the TenancyType.ID
     */
    public DW_ID getID() {
        return ID_TenancyType.getID(); // For convenience
    }

    /**
     * @return the PostcodeID
     */
    public DW_ID getPostcodeID() {
        return PostcodeID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof ID_TenancyType_PostcodeID) {
            ID_TenancyType_PostcodeID o;
            o = (ID_TenancyType_PostcodeID) obj;
            //if (this.hashCode() == o.hashCode()) {
                if (PostcodeID.equals(o.PostcodeID)) {
                    if (ID_TenancyType.equals(o.ID_TenancyType)) {
                        return true;
                    }
                }
            //}
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.ID_TenancyType != null ? this.ID_TenancyType.hashCode() : 0);
        hash = 97 * hash + (this.PostcodeID != null ? this.PostcodeID.hashCode() : 0);
        return hash;
    }

}
