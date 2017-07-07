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
public class ClaimRefID_PostcodeID implements Serializable {

    private DW_ID ClaimRefID;
    private DW_ID PostcodeID;

    public ClaimRefID_PostcodeID() {
    }

    public ClaimRefID_PostcodeID(
            DW_ID ClaimRefID,
            DW_ID PostcodeID
    ) {
        this.ClaimRefID = ClaimRefID;
        this.PostcodeID = PostcodeID;
    }

    /**
     * @return the Postcode
     */
    public DW_ID getClaimRefID() {
        return ClaimRefID;
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
        if (obj instanceof ClaimRefID_PostcodeID) {
            ClaimRefID_PostcodeID o;
            o = (ClaimRefID_PostcodeID) obj;
            if (this.ClaimRefID.equals(o.ClaimRefID)) {
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
        hash = 67 * hash + (this.ClaimRefID != null ? this.ClaimRefID.hashCode() : 0);
        hash = 67 * hash + (this.PostcodeID != null ? this.PostcodeID.hashCode() : 0);
        return hash;
    }
   
}
