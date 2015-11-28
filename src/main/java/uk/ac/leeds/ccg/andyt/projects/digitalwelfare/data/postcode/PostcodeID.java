/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode;

import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Point;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;

/**
 *
 * @author geoagdt
 */
public class PostcodeID {

    private final DW_ID ID;
    private final AGDT_Point point;

    public PostcodeID(
            DW_ID ID,
            AGDT_Point point) {
        this.ID = ID;
        this.point = point;
    }

    /**
     * @return the DW_ID
     */
    public DW_ID getID() {
        return ID;
    }

    /**
     * @return the point
     */
    public AGDT_Point getPoint() {
        return point;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof PostcodeID) {
            PostcodeID o;
            o = (PostcodeID) obj;
            if (this.hashCode() == o.hashCode()) {
                if (this.ID == o.ID) {
                    if (this.point == o.point) {
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
        hash = 37 * hash + (this.ID != null ? this.ID.hashCode() : 0);
        hash = 37 * hash + (this.point != null ? this.point.hashCode() : 0);
        return hash;
    }

    

}
