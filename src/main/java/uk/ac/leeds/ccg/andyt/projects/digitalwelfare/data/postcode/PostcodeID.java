/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode;

import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Point;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_longID;

/**
 * @author geoagdt
 */
public class PostcodeID {

    protected final DW_longID longID;
    protected final AGDT_Point p;

    public PostcodeID(
            DW_longID longID,
            AGDT_Point p) {
        this.longID = longID;
        this.p = p;
    }

    /**
     * @return the point
     */
    public AGDT_Point getPoint() {
        return p;
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
        if (obj instanceof PostcodeID) {
            PostcodeID o;
            o = (PostcodeID) obj;
            if (longID.equals(o.longID)) {
                if (this.hashCode() == o.hashCode()) {
                    if (this.p == o.p) {
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
        hash = 31 * hash + (this.p != null ? this.p.hashCode() : 0);
        return hash;
    }

}
