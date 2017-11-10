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
public class DW_ID_ClientEnquiryID extends DW_ID_ClientID implements Comparable {

    protected String EnquiryRef;

    public DW_ID_ClientEnquiryID() {
    }

    public DW_ID_ClientEnquiryID(
            String client_ref,
            String enquiry_ref) {
        this.ClientRef = client_ref;
        this.EnquiryRef = enquiry_ref;
    }

    @Override
    public String toString() {
        return "DW_ID_ClientEnquiryID("
                + super.toString()
                + ", EnquiryRef " + EnquiryRef + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DW_ID_ClientEnquiryID) {
            DW_ID_ClientEnquiryID oC;
            oC = (DW_ID_ClientEnquiryID) o;
            if (hashCode() == oC.hashCode()) {
                if (ClientRef.equalsIgnoreCase(oC.ClientRef)
                        && EnquiryRef.equalsIgnoreCase(oC.EnquiryRef)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.EnquiryRef != null ? this.EnquiryRef.hashCode() : 0);
        return hash;
    }
    
    

    @Override
    public int compareTo(Object o) {
        if (o instanceof DW_ID_ClientEnquiryID) {
            if (this.equals(o)) {
                return 0;
            }
            DW_ID_ClientEnquiryID oC;
            oC = (DW_ID_ClientEnquiryID) o;
            int comp0 = ClientRef.compareTo(oC.ClientRef);
                if (comp0 == 0) {
                    return EnquiryRef.compareTo(oC.EnquiryRef);
                } else {
                    return comp0;
                }
        }
        return -1;
    }

}
