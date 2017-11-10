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
public class DW_ID_ClientOutletEnquiryID extends DW_ID_ClientOutletID {

    protected String EnquiryRef;

    public DW_ID_ClientOutletEnquiryID() {
    }

    public DW_ID_ClientOutletEnquiryID(
            String client_ref,
            String outlet,
            String enquiry_ref) {
        this.ClientRef = client_ref;
        this.Outlet = outlet;
        this.EnquiryRef = enquiry_ref;
    }

    @Override
    public String toString() {
        return "DW_ID_ClientEnquiryOutletID("
                + super.toString()
                + ", enquiry_ref " + EnquiryRef + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DW_ID_ClientOutletEnquiryID) {
            DW_ID_ClientOutletEnquiryID oC;
            oC = (DW_ID_ClientOutletEnquiryID) o;
            if (hashCode() == oC.hashCode()) {
                if (ClientRef.equalsIgnoreCase(oC.ClientRef)
                        && Outlet.equalsIgnoreCase(oC.Outlet)
                        && EnquiryRef.equalsIgnoreCase(oC.EnquiryRef)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (this.EnquiryRef != null ? this.EnquiryRef.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof DW_ID_ClientOutletEnquiryID) {
            if (this.equals(o)) {
                return 0;
            }
            DW_ID_ClientOutletEnquiryID oC;
            oC = (DW_ID_ClientOutletEnquiryID) o;
            int comp0 = EnquiryRef.compareTo(oC.EnquiryRef);
            if (comp0 == 0) {
                int comp1 = ClientRef.compareTo(oC.ClientRef);
                if (comp1 == 0) {
                    return Outlet.compareTo(oC.Outlet);
                } else {
                    return comp1;
                }
            } else {
                return comp0;
            }
        }
        return -1;
    }

}
