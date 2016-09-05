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

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_intID;
import java.io.Serializable;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.PostcodeID;

/**
 *
 * @author geoagdt
 */
public class DW_PersonID implements Serializable {

    private DW_intID NINO_ID;
    private DW_intID DOB_ID;
    
    public DW_PersonID() {
    }

    public DW_PersonID(
            DW_intID NINO_ID,
            DW_intID DOB_ID
    ) {
        this.NINO_ID = NINO_ID;
        this.DOB_ID = DOB_ID;
    }

    /**
     * @return the NINO_ID
     */
    public DW_intID getNINO_ID() {
        return NINO_ID;
    }
    
    /**
     * @return the Postcode
     */
    public DW_intID getDOB_ID() {
        return DOB_ID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof DW_PersonID) {
            DW_PersonID o;
            o = (DW_PersonID) obj;
            if (this.hashCode() == o.hashCode()) {
                if (NINO_ID.equals(o.NINO_ID)) {
                    if (DOB_ID.equals(o.DOB_ID)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.NINO_ID != null ? this.NINO_ID.hashCode() : 0);
        hash = 59 * hash + (this.DOB_ID != null ? this.DOB_ID.hashCode() : 0);
        return hash;
    }

}
