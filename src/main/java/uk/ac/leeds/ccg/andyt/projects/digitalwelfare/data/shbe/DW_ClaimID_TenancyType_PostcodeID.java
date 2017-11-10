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
public class DW_ClaimID_TenancyType_PostcodeID implements Serializable {

    private final DW_ClaimID_TenancyType ClaimID_TenancyType;
    private final DW_ID PostcodeID;

    public DW_ClaimID_TenancyType_PostcodeID(
            DW_ClaimID_TenancyType ID_TenancyType,
            DW_ID PostcodeID
    ) {
        this.ClaimID_TenancyType = ID_TenancyType;
        this.PostcodeID = PostcodeID;
    }

    /**
     * @return the TenancyType
     */
    public int getTenancyType() {
        return ClaimID_TenancyType.getTenancyType(); // For convenience
    }

    /**
     * @return the TenancyType.ID
     */
    public DW_ID getID() {
        return ClaimID_TenancyType.getClaimID(); // For convenience
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
        if (obj instanceof DW_ClaimID_TenancyType_PostcodeID) {
            DW_ClaimID_TenancyType_PostcodeID o;
            o = (DW_ClaimID_TenancyType_PostcodeID) obj;
            //if (this.hashCode() == o.hashCode()) {
                if (PostcodeID.equals(o.PostcodeID)) {
                    if (ClaimID_TenancyType.equals(o.ClaimID_TenancyType)) {
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
        hash = 97 * hash + (this.ClaimID_TenancyType != null ? this.ClaimID_TenancyType.hashCode() : 0);
        hash = 97 * hash + (this.PostcodeID != null ? this.PostcodeID.hashCode() : 0);
        return hash;
    }

}
