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
package uk.ac.leeds.ccg.projects.dw.data.adviceleeds;

/**
 *
 * @author geoagdt
 */
public class DW_ID_ClientID implements Comparable {

    protected String ClientRef;

    public DW_ID_ClientID() {
    }

    public DW_ID_ClientID(
            String client_ref) {
        this.ClientRef = client_ref;
    }

    @Override
    public String toString() {
        return "DW_ID_ClientID("
                + "client_ref " + ClientRef + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DW_ID_ClientID) {
            DW_ID_ClientID oC;
            oC = (DW_ID_ClientID) o;
            if (hashCode() == oC.hashCode()) {
                if (ClientRef.equalsIgnoreCase(oC.ClientRef)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.ClientRef != null ? this.ClientRef.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof DW_ID_ClientID) {
            if (this.equals(o)) {
                return 0;
            }
            DW_ID_ClientID oC;
            oC = (DW_ID_ClientID) o;
            return ClientRef.compareTo(oC.ClientRef);
        }
        return -1;
    }

}
