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

/**
 *
 * @author geoagdt
 */
public class DW_UnderOccupiedReport_Record {

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
     * @param BedroomRequirement
     */
    private int BedroomRequirement;
    /**
     * @param BedroomsInProperty
     */
    private int BedroomsInProperty;
    /**
     * @param MaleChildrenUnder10
     */
    private int MaleChildrenUnder10;
    /**
     * @param FemaleChildrenUnder10
     */
    private int FemaleChildrenUnder10;
    /**
     * @param MaleChildren10to16
     */
    private int MaleChildren10to16;
    /**
     * @param FemaleChildren10to16
     */
    private int FemaleChildren10to16;
    /**
     * @param ChildrenOver16
     */
    private int ChildrenOver16;
    /**
     * @param TotalDependentChildren
     */
    private int TotalDependentChildren;
    /**
     * @param NonDependents
     */
    private int NonDependents;
    /**
     * @param TotalRentArrears
     */
    private double TotalRentArrears;

    public DW_UnderOccupiedReport_Record(
            long RecordID,
            String line,
            DW_UnderOccupiedReport_Handler aUnderOccupiedReport_DataHandler) throws Exception {
        this.RecordID = RecordID;
        String[] fields = line.split(",");
        int n = 0;
        this.ClaimReferenceNumber = fields[n];
        if (ClaimReferenceNumber.endsWith("X")) {
            this.RecordType = "X";
        } else {
            this.RecordType = "";
        }
        n++;
        this.BedroomRequirement = new Integer(fields[n]);
        n++;
        this.BedroomsInProperty = new Integer(fields[n]);
        n++;
        this.MaleChildrenUnder10 = new Integer(fields[n]);
        n++;
        this.FemaleChildrenUnder10 = new Integer(fields[n]);
        n++;
        this.MaleChildren10to16 = new Integer(fields[n]);
        n++;
        this.FemaleChildren10to16 = new Integer(fields[n]);
        n++;
        this.ChildrenOver16 = new Integer(fields[n]);
        n++;
        this.TotalDependentChildren = new Integer(fields[n]);
        n++;
        this.NonDependents = new Integer(fields[n]);
        n++;
        if (fields.length == 11) {
            if (fields[n].isEmpty() || fields[n].trim().equalsIgnoreCase("")) {
                this.TotalRentArrears = 0;
            } else {
                this.TotalRentArrears = new Double(fields[n]);
            }
        }
    }

    @Override
    public String toString() {
        return "RecordID " + getRecordID()
                + ",RecordType " + getRecordType()
                + ",ClaimReferenceNumber " + getClaimReferenceNumber()
                + ",BedroomRequirement " + getBedroomRequirement()
                + ",BedroomsInProperty " + getBedroomsInProperty()
                + ",MaleChildrenUnder10 " + getMaleChildrenUnder10()
                + ",FemaleChildrenUnder10 " + getFemaleChildrenUnder10()
                + ",MaleChildren10to16 " + getMaleChildren10to16()
                + ",FemaleChildren10to16 " + getFemaleChildren10to16()
                + ",ChildrenOver16 " + getChildrenOver16()
                + ",TotalDependentChildren " + getTotalDependentChildren()
                + ",NonDependents " + getNonDependents()
                + ",TotalRentArrears " + getTotalRentArrears();
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
     * @param ClaimReferenceNumber the ClaimReferenceNumber to set
     */
    public void setClaimReferenceNumber(String ClaimReferenceNumber) {
        this.ClaimReferenceNumber = ClaimReferenceNumber;
    }

    /**
     * @return the BedroomRequirement
     */
    public int getBedroomRequirement() {
        return BedroomRequirement;
    }

    /**
     * @param BedroomRequirement the BedroomRequirement to set
     */
    public void setBedroomRequirement(int BedroomRequirement) {
        this.BedroomRequirement = BedroomRequirement;
    }

    /**
     * @return the BedroomsInProperty
     */
    public int getBedroomsInProperty() {
        return BedroomsInProperty;
    }

    /**
     * @param BedroomsInProperty the BedroomsInProperty to set
     */
    public void setBedroomsInProperty(int BedroomsInProperty) {
        this.BedroomsInProperty = BedroomsInProperty;
    }

    /**
     * @return the MaleChildrenUnder10
     */
    public int getMaleChildrenUnder10() {
        return MaleChildrenUnder10;
    }

    /**
     * @param MaleChildrenUnder10 the MaleChildrenUnder10 to set
     */
    public void setMaleChildrenUnder10(int MaleChildrenUnder10) {
        this.MaleChildrenUnder10 = MaleChildrenUnder10;
    }

    /**
     * @return the FemaleChildrenUnder10
     */
    public int getFemaleChildrenUnder10() {
        return FemaleChildrenUnder10;
    }

    /**
     * @param FemaleChildrenUnder10 the FemaleChildrenUnder10 to set
     */
    public void setFemaleChildrenUnder10(int FemaleChildrenUnder10) {
        this.FemaleChildrenUnder10 = FemaleChildrenUnder10;
    }

    /**
     * @return the MaleChildren10to16
     */
    public int getMaleChildren10to16() {
        return MaleChildren10to16;
    }

    /**
     * @param MaleChildren10to16 the MaleChildren10to16 to set
     */
    public void setMaleChildren10to16(int MaleChildren10to16) {
        this.MaleChildren10to16 = MaleChildren10to16;
    }

    /**
     * @return the FemaleChildren10to16
     */
    public int getFemaleChildren10to16() {
        return FemaleChildren10to16;
    }

    /**
     * @param FemaleChildren10to16 the FemaleChildren10to16 to set
     */
    public void setFemaleChildren10to16(int FemaleChildren10to16) {
        this.FemaleChildren10to16 = FemaleChildren10to16;
    }

    /**
     * @return the ChildrenOver16
     */
    public int getChildrenOver16() {
        return ChildrenOver16;
    }

    /**
     * @param ChildrenOver16 the ChildrenOver16 to set
     */
    public void setChildrenOver16(int ChildrenOver16) {
        this.ChildrenOver16 = ChildrenOver16;
    }

    /**
     * @return the TotalDependentChildren
     */
    public int getTotalDependentChildren() {
        return TotalDependentChildren;
    }

    /**
     * @param TotalDependentChildren the TotalDependentChildren to set
     */
    public void setTotalDependentChildren(int TotalDependentChildren) {
        this.TotalDependentChildren = TotalDependentChildren;
    }

    /**
     * @return the NonDependents
     */
    public int getNonDependents() {
        return NonDependents;
    }

    /**
     * @param NonDependents the NonDependents to set
     */
    public void setNonDependents(int NonDependents) {
        this.NonDependents = NonDependents;
    }

    /**
     * @return the TotalRentArrears
     */
    public double getTotalRentArrears() {
        return TotalRentArrears;
    }

    /**
     * @param TotalRentArrears the TotalRentArrears to set
     */
    public void setTotalRentArrears(double TotalRentArrears) {
        this.TotalRentArrears = TotalRentArrears;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (this.ClaimReferenceNumber != null ? this.ClaimReferenceNumber.hashCode() : 0);
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
        final DW_UnderOccupiedReport_Record other = (DW_UnderOccupiedReport_Record) obj;
        if ((this.RecordType == null) ? (other.RecordType != null) : !this.RecordType.equals(other.RecordType)) {
            return false;
        }
        if ((this.ClaimReferenceNumber == null) ? (other.ClaimReferenceNumber != null) : !this.ClaimReferenceNumber.equals(other.ClaimReferenceNumber)) {
            return false;
        }
        if (this.BedroomRequirement != other.BedroomRequirement) {
            return false;
        }
        if (this.BedroomsInProperty != other.BedroomsInProperty) {
            return false;
        }
        if (this.MaleChildrenUnder10 != other.MaleChildrenUnder10) {
            return false;
        }
        if (this.FemaleChildrenUnder10 != other.FemaleChildrenUnder10) {
            return false;
        }
        if (this.MaleChildren10to16 != other.MaleChildren10to16) {
            return false;
        }
        if (this.FemaleChildren10to16 != other.FemaleChildren10to16) {
            return false;
        }
        if (this.ChildrenOver16 != other.ChildrenOver16) {
            return false;
        }
        if (this.TotalDependentChildren != other.TotalDependentChildren) {
            return false;
        }
        if (this.NonDependents != other.NonDependents) {
            return false;
        }
        if (Double.doubleToLongBits(this.TotalRentArrears) != Double.doubleToLongBits(other.TotalRentArrears)) {
            return false;
        }
        return true;
    }
}
