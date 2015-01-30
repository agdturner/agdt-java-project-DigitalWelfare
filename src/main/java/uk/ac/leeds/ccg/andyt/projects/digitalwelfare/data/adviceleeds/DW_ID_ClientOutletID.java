/*
 * Copyright (C) 2014 geoagdt.
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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds;

/**
 *
 * @author geoagdt
 */
public class DW_ID_ClientOutletID extends DW_ID_ClientID implements Comparable {

    protected String outlet;

    public DW_ID_ClientOutletID() {
    }

    public DW_ID_ClientOutletID(
            String client_ref,
            String outlet) {
        this.client_ref = client_ref;
        this.outlet = outlet;
    }

    @Override
    public String toString() {
        return "DW_ID_ClientOutletID("
                + super.toString()
                + ", outlet " + outlet + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DW_ID_ClientOutletID) {
            DW_ID_ClientOutletID oC;
            oC = (DW_ID_ClientOutletID) o;
            if (hashCode() == oC.hashCode()) {
                if (client_ref.equalsIgnoreCase(oC.client_ref)
                        && outlet.equalsIgnoreCase(oC.outlet)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.outlet != null ? this.outlet.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof DW_ID_ClientOutletID) {
            if (this.equals(o)) {
                return 0;
            }
            DW_ID_ClientOutletID oC;
            oC = (DW_ID_ClientOutletID) o;
            int comp0 = client_ref.compareTo(oC.client_ref);
            if (comp0 == 0) {
                return outlet.compareTo(oC.outlet);
            } else {
                return comp0;
            }
        }
        return -1;
    }

}
