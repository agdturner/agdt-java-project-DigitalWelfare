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
public class DW_PersonID implements Serializable {

    private DW_ID NINO_ID;
    private DW_ID DOB_ID;

    public DW_PersonID() {
    }

    public DW_PersonID(
            DW_ID tNINO_ID,
            DW_ID tDOB_ID
    ) {
        this.NINO_ID = tNINO_ID;
        this.DOB_ID = tDOB_ID;
    }

    /**
     * @return the tNINO_ID
     */
    public DW_ID getNINO_ID() {
        return NINO_ID;
    }

    /**
     * @return the tDOB_ID
     */
    public DW_ID getDOB_ID() {
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
                if (NINO_ID.equals(o.getNINO_ID())) {
                    if (DOB_ID.equals(o.getDOB_ID())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.NINO_ID != null ? this.NINO_ID.hashCode() : 0);
        hash = 67 * hash + (this.DOB_ID != null ? this.DOB_ID.hashCode() : 0);
        return hash;
    }

}
