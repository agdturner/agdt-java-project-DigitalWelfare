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

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import java.io.Serializable;

/**
 *
 * @author geoagdt
 */
public class DW_ClaimID_TenancyType implements Serializable {

    private final DW_ID ClaimID;
    private final int TenancyType;

    public DW_ClaimID_TenancyType(
            DW_ID ClaimID,
            int TenancyType
    ) {
        this.ClaimID = ClaimID;
        this.TenancyType = TenancyType;
    }

    /**
     * @return the TenancyType
     */
    public int getTenancyType() {
        return TenancyType;
    }
    
    /**
     * @return the ClaimID
     */
    public DW_ID getClaimID() {
        return ClaimID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof DW_ClaimID_TenancyType) {
            DW_ClaimID_TenancyType o;
            o = (DW_ClaimID_TenancyType) obj;
            if (hashCode() == o.hashCode()) {
                if (TenancyType == o.TenancyType) {
                    if (ClaimID.equals(o.ClaimID)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (this.ClaimID != null ? this.ClaimID.hashCode() : 0);
        hash = 73 * hash + this.TenancyType;
        return hash;
    }

}
