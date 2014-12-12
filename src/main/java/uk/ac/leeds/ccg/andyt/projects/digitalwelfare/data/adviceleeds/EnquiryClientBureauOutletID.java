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
public class EnquiryClientBureauOutletID implements Comparable {

    private String enquiry_ref;
    private String client_ref;
    private String bureau;
    private String outlet;

    public EnquiryClientBureauOutletID() {
    }

    public EnquiryClientBureauOutletID(
            String enquiry_ref,
            String client_ref,
            String bureau,
            String outlet) {
        this.enquiry_ref = enquiry_ref;
        this.client_ref = client_ref;
        this.bureau = bureau;
        this.outlet = outlet;
    }

    @Override
    public String toString() {
        return "EnquiryClientBureauOutletID("
                + "enquiry_ref " + enquiry_ref + ", "
                + "client_ref " + client_ref + ", "
                + "bureau " + bureau + ", "
                + "outlet " + outlet + ")";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.enquiry_ref != null ? this.enquiry_ref.hashCode() : 0);
        hash = 37 * hash + (this.client_ref != null ? this.client_ref.hashCode() : 0);
        hash = 37 * hash + (this.bureau != null ? this.bureau.hashCode() : 0);
        hash = 37 * hash + (this.outlet != null ? this.outlet.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EnquiryClientBureauOutletID) {
            EnquiryClientBureauOutletID oC;
            oC = (EnquiryClientBureauOutletID) o;
            if (hashCode() == oC.hashCode()) {
                if (enquiry_ref.equalsIgnoreCase(oC.enquiry_ref)
                        && client_ref.equalsIgnoreCase(oC.client_ref)
                        && bureau.equalsIgnoreCase(oC.bureau)
                        && outlet.equalsIgnoreCase(oC.outlet)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof EnquiryClientBureauOutletID) {
            if (this.equals(o)) {
                return 0;
            }
            EnquiryClientBureauOutletID oC;
            oC = (EnquiryClientBureauOutletID) o;
            int comp = enquiry_ref.compareTo(oC.enquiry_ref);
            if (comp == 0) {
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
            } else {
                return comp;
            }
        }
        return -1;
    }

}
