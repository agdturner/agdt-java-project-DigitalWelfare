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
public class ClientBureauOutletID implements Comparable {

    protected String client_ref;
    protected String bureau;
    protected String outlet;

    public ClientBureauOutletID() {
    }

    public ClientBureauOutletID(
            String client_ref,
            String bureau,
            String outlet) {
        this.client_ref = client_ref;
        this.bureau = bureau;
        this.outlet = outlet;
    }

    @Override
    public String toString() {
        return "EnquiryClientBureauOutletID("
                + "client_ref " + client_ref + ", "
                + "bureau " + bureau + ", "
                + "outlet " + outlet + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ClientBureauOutletID) {
            ClientBureauOutletID oC;
            oC = (ClientBureauOutletID) o;
            if (hashCode() == oC.hashCode()) {
                if (client_ref.equalsIgnoreCase(oC.client_ref)
                        && bureau.equalsIgnoreCase(oC.bureau)
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
        hash = 79 * hash + (this.client_ref != null ? this.client_ref.hashCode() : 0);
        hash = 79 * hash + (this.bureau != null ? this.bureau.hashCode() : 0);
        hash = 79 * hash + (this.outlet != null ? this.outlet.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof ClientBureauOutletID) {
            if (this.equals(o)) {
                return 0;
            }
            ClientBureauOutletID oC;
            oC = (ClientBureauOutletID) o;
            int comp2 = client_ref.compareTo(oC.client_ref);
                if (comp2 == 0) {
                    int comp3 = bureau.compareTo(oC.bureau);
                    if (comp3 == 0) {
                        return outlet.compareTo(oC.outlet);
                    } else {
                        return comp3;
                    }
                } else {
                    return comp2;
                }
        }
        return -1;
    }

}
