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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied;

import java.io.Serializable;
import java.util.HashMap;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;

/**
 *
 * @author geoagdt
 */
public class DW_UO_Record extends DW_Object implements Serializable {

    /**
     * 0 RecordID
     */
    private long RecordID;
    /**
     * If ClaimReferenceNumber ends in X this is X otherwise this is blank
     */
    private String RecordType;
    /**
     * @param ClaimReferenceNumber
     */
    private String ClaimReferenceNumber;
    /**
     * @param ClaimID
     */
    private DW_ID ClaimID;
    /**
     * @param BedroomRequirement
     */
    private Integer BedroomRequirement;
    /**
     * @param BedroomsInProperty
     */
    private Integer BedroomsInProperty;
    /**
     * @param MaleChildrenUnder10
     */
    private Integer MaleChildrenUnder10;
    /**
     * @param FemaleChildrenUnder10
     */
    private Integer FemaleChildrenUnder10;
    /**
     * @param MaleChildren10to16
     */
    private Integer MaleChildren10to16;
    /**
     * @param FemaleChildren10to16
     */
    private Integer FemaleChildren10to16;
    /**
     * @param ChildrenOver16
     */
    private Integer ChildrenOver16;
    /**
     * @param TotalDependentChildren
     */
    private Integer TotalDependentChildren;
    /**
     * @param NonDependents
     */
    private Integer NonDependents;
    /**
     * @param TotalRentArrears
     */
    private Double TotalRentArrears;

    /**
     * For RSL type we expect rectangular data with 9 or 10 columns. If there
     * are 9 columns we assume that
     *
     * @param env
     * @param RecordID
     * @param line
     * @param fieldNames
     * @param type
     * @throws Exception
     */
    public DW_UO_Record(
            DW_Environment env,
            long RecordID,
            String line,
            //String type
            String[] fieldNames) throws Exception {
        this.env = env;

        // Get CTBRef to ClaimRef and ClaimRef to CTBRef lookups.
        DW_SHBE_Handler DW_SHBE_Handler;
        DW_SHBE_Handler = env.getDW_SHBE_Handler();
        HashMap<DW_ID, String> ClaimIDToCTBRefLookup;
        ClaimIDToCTBRefLookup = DW_SHBE_Handler.getClaimIDToCTBRefLookup();
        HashMap<String, DW_ID> CTBRefToClaimIDLookup;
        CTBRefToClaimIDLookup = DW_SHBE_Handler.getCTBRefToClaimIDLookup();
        this.RecordID = RecordID;
        String[] fields = line.split(",");
        if (fields.length != fieldNames.length) {
            System.err.println(this.getClass().getName() + ".DW_UnderOccupiedReport_Record(long,String,String[])");
            System.err.println("RecordID " + RecordID);
            System.err.println("fields.length != fieldNames.length");
            System.err.println("" + fields.length + " != " + fieldNames.length);
            for (int i = 0; i < fieldNames.length - 1; i++) {
                System.err.print(fieldNames[i] + ", ");
            }
            System.err.println(fieldNames.length - 1);
            System.err.println(line);
        }
        boolean doneMaleChildrenUnder10 = false;
        boolean doneFemaleChildrenUnder10 = false;
        for (int i = 0; i < fieldNames.length; i++) {
            if (i < fields.length) { // This is because the data records are sometimes incomplete
                if (fieldNames[i].equalsIgnoreCase("claim_ref")) {
                    this.ClaimReferenceNumber = fields[i];
                    if (ClaimReferenceNumber.endsWith("X")) {
                        this.RecordType = "X";
                    } else {
                        this.RecordType = "";
                    }
                    ClaimID = DW_SHBE_Handler.getIDAddIfNeeded(
                            ClaimReferenceNumber,
                            CTBRefToClaimIDLookup,
                            ClaimIDToCTBRefLookup);
                } else {
                    if (fieldNames[i].equalsIgnoreCase("room_requirem")
                            || fieldNames[i].equalsIgnoreCase("bedroom_requirement")) {
                        this.BedroomRequirement = new Integer(fields[i]);
                    } else {
                        if (fieldNames[i].equalsIgnoreCase("bedrooms_in_p")
                                || fieldNames[i].equalsIgnoreCase("bedrooms_in_property")) {
                            this.BedroomsInProperty = new Integer(fields[i]);
                        } else {
                            if (fieldNames[i].equalsIgnoreCase("male_children under 10")) {
                                this.MaleChildrenUnder10 = new Integer(fields[i]);
                            } else {
                                if (fieldNames[i].equalsIgnoreCase("male_children 10 to 16")) {
                                    this.MaleChildren10to16 = new Integer(fields[i]);
                                } else {
                                    // This is because two fields are identically named in many of the files!
                                    if (fieldNames[i].equalsIgnoreCase("male_children")) {
                                        if (doneMaleChildrenUnder10) {
                                            this.MaleChildren10to16 = new Integer(fields[i]);
                                        } else {
                                            this.MaleChildrenUnder10 = new Integer(fields[i]);
                                            doneMaleChildrenUnder10 = true;
                                        }
                                    } else {
                                        if (fieldNames[i].equalsIgnoreCase("female_children under 10")) {
                                            this.FemaleChildrenUnder10 = new Integer(fields[i]);
                                        } else {
                                            if (fieldNames[i].equalsIgnoreCase("female_children 10 to 16")) {
                                                this.FemaleChildren10to16 = new Integer(fields[i]);
                                            } else {
                                                if (fieldNames[i].equalsIgnoreCase("female_childr")) {
                                                    if (doneFemaleChildrenUnder10) {
                                                        this.FemaleChildren10to16 = new Integer(fields[i]);
                                                    } else {
                                                        this.FemaleChildrenUnder10 = new Integer(fields[i]);
                                                        doneFemaleChildrenUnder10 = true;
                                                    }
                                                } else {
                                                    if (fieldNames[i].equalsIgnoreCase("children_over")
                                                            || fieldNames[i].equalsIgnoreCase("children_over 16")) {
                                                        this.ChildrenOver16 = new Integer(fields[i]);
                                                    } else {
                                                        if (fieldNames[i].equalsIgnoreCase("TOTAL Dep Children")) {
                                                            this.TotalDependentChildren = new Integer(fields[i]);
                                                        } else {
                                                            if (fieldNames[i].equalsIgnoreCase("nondependants")) {
                                                                this.NonDependents = new Integer(fields[i]);
                                                            } else {
                                                                if (fieldNames[i].equalsIgnoreCase("total_rent")
                                                                        || fieldNames[i].equalsIgnoreCase("total_rent arrears")) {
                                                                    this.TotalRentArrears = new Double(fields[i]);
                                                                } else {
                                                                    System.err.println("Unrecognised field: " + fieldNames[i] + ". Debug needed of: " + this.getClass().getName());
                                                                    int debug = 1;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
//        n++;
//        this.MaleChildren10to16 = new Integer(fields[n]);
//        n++;
//        this.FemaleChildren10to16 = new Integer(fields[n]);
//        n++;
//        this.ChildrenOver16 = new Integer(fields[n]);
//        if (type.equalsIgnoreCase("RSL")) {
//            if (fields.length == 10) {
//                n++;
//                this.TotalDependentChildren = new Integer(fields[n]);
//            }
//            n++;
//            this.NonDependents = new Integer(fields[n]);
//        } else {
//            if (fields.length == 11) {
//                n++;
//                this.TotalDependentChildren = new Integer(fields[n]); // This field is not in all the data! In particular it is not in 2015 5
//            }
//            n++;
//            this.NonDependents = new Integer(fields[n]);
//            n++;
//            if (fields[n].isEmpty() || fields[n].trim().equalsIgnoreCase("")) {
//                this.TotalRentArrears = 0.0d;
//            } else {
//                this.TotalRentArrears = new Double(fields[n]);
//            }
//        }
    }

    @Override
    public String toString() {
        return "RecordID " + getRecordID()
                + ", ClaimID " + getClaimID()
                + ", RecordType " + getRecordType()
                + ", ClaimReferenceNumber " + getClaimReferenceNumber()
                + ", BedroomRequirement " + getBedroomRequirement()
                + ", BedroomsInProperty " + getBedroomsInProperty()
                + ", MaleChildrenUnder10 " + getMaleChildrenUnder10()
                + ", FemaleChildrenUnder10 " + getFemaleChildrenUnder10()
                + ", MaleChildren10to16 " + getMaleChildren10to16()
                + ", FemaleChildren10to16 " + getFemaleChildren10to16()
                + ", ChildrenOver16 " + getChildrenOver16()
                + ", TotalDependentChildren " + getTotalDependentChildren()
                + ", NonDependents " + getNonDependents()
                + ", TotalRentArrears " + getTotalRentArrears();
    }

    /**
     * @return the RecordID
     */
    public long getRecordID() {
        return RecordID;
    }

    /**
     * @param RecordID the RecordID to set
     */
    public void setRecordID(long RecordID) {
        this.RecordID = RecordID;
    }

    /**
     * @return the RecordType
     */
    public String getRecordType() {
        return RecordType;
    }

    /**
     * @param RecordType the RecordType to set
     */
    public void setRecordType(String RecordType) {
        this.RecordType = RecordType;
    }

    /**
     * @return the ClaimReferenceNumber
     */
    public String getClaimReferenceNumber() {
        return ClaimReferenceNumber;
    }

    /**
     * @return the ClaimID
     */
    public DW_ID getClaimID() {
        return ClaimID;
    }

    /**
     * @return the BedroomRequirement
     */
    public Integer getBedroomRequirement() {
        return BedroomRequirement;
    }

    /**
     * @param BedroomRequirement the BedroomRequirement to set
     */
    public void setBedroomRequirement(Integer BedroomRequirement) {
        this.BedroomRequirement = BedroomRequirement;
    }

    /**
     * @return the BedroomsInProperty
     */
    public Integer getBedroomsInProperty() {
        return BedroomsInProperty;
    }

    /**
     * @param BedroomsInProperty the BedroomsInProperty to set
     */
    public void setBedroomsInProperty(Integer BedroomsInProperty) {
        this.BedroomsInProperty = BedroomsInProperty;
    }

    /**
     * @return the MaleChildrenUnder10
     */
    public Integer getMaleChildrenUnder10() {
        return MaleChildrenUnder10;
    }

    /**
     * @param MaleChildrenUnder10 the MaleChildrenUnder10 to set
     */
    public void setMaleChildrenUnder10(Integer MaleChildrenUnder10) {
        this.MaleChildrenUnder10 = MaleChildrenUnder10;
    }

    /**
     * @return the FemaleChildrenUnder10
     */
    public Integer getFemaleChildrenUnder10() {
        return FemaleChildrenUnder10;
    }

    /**
     * @param FemaleChildrenUnder10 the FemaleChildrenUnder10 to set
     */
    public void setFemaleChildrenUnder10(Integer FemaleChildrenUnder10) {
        this.FemaleChildrenUnder10 = FemaleChildrenUnder10;
    }

    /**
     * @return the MaleChildren10to16
     */
    public Integer getMaleChildren10to16() {
        return MaleChildren10to16;
    }

    /**
     * @param MaleChildren10to16 the MaleChildren10to16 to set
     */
    public void setMaleChildren10to16(Integer MaleChildren10to16) {
        this.MaleChildren10to16 = MaleChildren10to16;
    }

    /**
     * @return the FemaleChildren10to16
     */
    public Integer getFemaleChildren10to16() {
        return FemaleChildren10to16;
    }

    /**
     * @param FemaleChildren10to16 the FemaleChildren10to16 to set
     */
    public void setFemaleChildren10to16(Integer FemaleChildren10to16) {
        this.FemaleChildren10to16 = FemaleChildren10to16;
    }

    /**
     * @return the ChildrenOver16
     */
    public Integer getChildrenOver16() {
        return ChildrenOver16;
    }

    /**
     * @param ChildrenOver16 the ChildrenOver16 to set
     */
    public void setChildrenOver16(Integer ChildrenOver16) {
        this.ChildrenOver16 = ChildrenOver16;
    }

    /**
     * @return the TotalDependentChildren
     */
    public Integer getTotalDependentChildren() {
        return TotalDependentChildren;
    }

    /**
     * @param TotalDependentChildren the TotalDependentChildren to set
     */
    public void setTotalDependentChildren(Integer TotalDependentChildren) {
        this.TotalDependentChildren = TotalDependentChildren;
    }

    /**
     * @return the NonDependents
     */
    public Integer getNonDependents() {
        return NonDependents;
    }

    /**
     * @param NonDependents the NonDependents to set
     */
    public void setNonDependents(Integer NonDependents) {
        this.NonDependents = NonDependents;
    }

    /**
     * @return the TotalRentArrears
     */
    public Double getTotalRentArrears() {
        return TotalRentArrears;
    }

    /**
     * @param TotalRentArrears the TotalRentArrears to set
     */
    public void setTotalRentArrears(Double TotalRentArrears) {
        this.TotalRentArrears = TotalRentArrears;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (this.ClaimID != null ? this.ClaimID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DW_UO_Record other = (DW_UO_Record) obj;
        if ((this.RecordType == null) ? (other.RecordType != null) : !this.RecordType.equals(other.RecordType)) {
            return false;
        }
        if ((this.ClaimID == null) ? (other.ClaimID != null) : !this.ClaimID.equals(other.ClaimID)) {
            return false;
        }
        if (this.BedroomRequirement.compareTo(other.BedroomRequirement) != 0) {
            return false;
        }
        if (this.BedroomsInProperty.compareTo(other.BedroomsInProperty) != 0) {
            return false;
        }
        if (this.MaleChildrenUnder10.compareTo(other.MaleChildrenUnder10) != 0) {
            return false;
        }
        if (this.FemaleChildrenUnder10.compareTo(other.FemaleChildrenUnder10) != 0) {
            return false;
        }
        if (this.MaleChildren10to16.compareTo(other.MaleChildren10to16) != 0) {
            return false;
        }
        if (this.FemaleChildren10to16.compareTo(other.FemaleChildren10to16) != 0) {
            return false;
        }
        if (this.ChildrenOver16.compareTo(other.ChildrenOver16) != 0) {
            return false;
        }
        if (this.TotalDependentChildren == null) {
            if (other.TotalDependentChildren != null) {
                return false;
            }
        } else {
            if (other.TotalDependentChildren == null) {
                return false;
            }
            if (this.TotalDependentChildren.compareTo(other.TotalDependentChildren) != 0) {
                return false;
            }
        }
        if (this.NonDependents.compareTo(other.NonDependents) != 0) {
            return false;
        }
        if (this.TotalRentArrears == null) {
            if (other.TotalRentArrears != null) {
                return false;
            }
        } else {
            if (other.TotalRentArrears == null) {
                return false;
            }
            if (this.TotalRentArrears.compareTo(other.TotalRentArrears) != 0) {
                return false;
            }
        }
        return true;
    }
}
