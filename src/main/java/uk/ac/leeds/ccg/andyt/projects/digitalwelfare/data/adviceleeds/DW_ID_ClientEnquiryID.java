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

    protected String enquiry_ref;

    public DW_ID_ClientEnquiryID() {
    }

    public DW_ID_ClientEnquiryID(
            String client_ref,
            String enquiry_ref) {
        this.client_ref = client_ref;
        this.enquiry_ref = enquiry_ref;
    }

    @Override
    public String toString() {
        return "DW_ID_ClientEnquiryID("
                + super.toString()
                + ", enquiry_ref " + enquiry_ref + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DW_ID_ClientEnquiryID) {
            DW_ID_ClientEnquiryID oC;
            oC = (DW_ID_ClientEnquiryID) o;
            if (hashCode() == oC.hashCode()) {
                if (client_ref.equalsIgnoreCase(oC.client_ref)
                        && enquiry_ref.equalsIgnoreCase(oC.enquiry_ref)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.enquiry_ref != null ? this.enquiry_ref.hashCode() : 0);
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
            int comp0 = client_ref.compareTo(oC.client_ref);
                if (comp0 == 0) {
                    return enquiry_ref.compareTo(oC.enquiry_ref);
                } else {
                    return comp0;
                }
        }
        return -1;
    }

}
