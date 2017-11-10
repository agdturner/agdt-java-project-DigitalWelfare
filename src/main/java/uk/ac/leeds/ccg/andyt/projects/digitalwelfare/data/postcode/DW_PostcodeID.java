/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode;

import uk.ac.leeds.ccg.andyt.geotools.Geotools_Point;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;

/**
 * @author geoagdt
 */
public class DW_PostcodeID {

    protected final DW_ID ID;
    protected final Geotools_Point Point;

    public DW_PostcodeID(
            DW_ID ID,
            Geotools_Point p) {
        this.ID = ID;
        this.Point = p;
    }

    /**
     * @return the point
     */
    public Geotools_Point getPoint() {
        return Point;
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
        if (obj instanceof DW_PostcodeID) {
            DW_PostcodeID o;
            o = (DW_PostcodeID) obj;
            if (ID.equals(o.ID)) {
                if (this.Point == o.Point) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.ID != null ? this.ID.hashCode() : 0);
        hash = 23 * hash + (this.Point != null ? this.Point.hashCode() : 0);
        return hash;
    }

    
}
