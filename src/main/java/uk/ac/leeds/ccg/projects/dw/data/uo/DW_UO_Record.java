
package uk.ac.leeds.ccg.projects.dw.data.uo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_UO_Record implements Serializable {

    /**
     * 0 RecordID
     */
    private long RecordID;
    /**
     * ClaimReferenceNumber
     */
    private String ClaimRef;
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
    private Double TotalRentArrears;

    /**
     * For RSL type we expect rectangular data with 9 or 10 columns. If there
     * are 9 columns we assume that TotalRentArrears = 0. There is only rent
     * arrears information for council tenants.
     *
     * @param RecordID
     * @param line
     * @param fieldNames
     * @throws Exception
     */
    public DW_UO_Record(long RecordID, String line, String[] fieldNames) 
            throws Exception {
        this.RecordID = RecordID;
        String[] fields = line.split(",");
//        if (fields.length != fieldNames.length) {
//            System.err.println(this.getClass().getName() + ".DW_UnderOccupiedReport_Record(long,String,String[])");
//            System.err.println("RecordID " + RecordID);
//            System.err.println("fields.length != fieldNames.length");
//            System.err.println("" + fields.length + " != " + fieldNames.length);
//            for (int i = 0; i < fieldNames.length - 1; i++) {
//                System.err.print(fieldNames[i] + ", ");
//            }
//            System.err.println(fieldNames.length - 1);
//            System.err.println(line);
//        }
        boolean doneMaleChildrenUnder10 = false;
        boolean doneFemaleChildrenUnder10 = false;
        for (int i = 0; i < fieldNames.length; i++) {
            if (i < fields.length) { // This is because the data records are sometimes incomplete
                if (fieldNames[i].equalsIgnoreCase("claim_ref")) {
                    this.ClaimRef = fields[i];
                } else if (fieldNames[i].equalsIgnoreCase("room_requirem")
                        || fieldNames[i].equalsIgnoreCase("bedroom_requirement")) {
                    this.BedroomRequirement = Integer.valueOf(fields[i]);
                } else if (fieldNames[i].equalsIgnoreCase("bedrooms_in_p")
                        || fieldNames[i].equalsIgnoreCase("bedrooms_in_property")) {
                    this.BedroomsInProperty = Integer.valueOf(fields[i]);
                } else if (fieldNames[i].equalsIgnoreCase("male_children under 10")) {
                    this.MaleChildrenUnder10 = Integer.valueOf(fields[i]);
                } else if (fieldNames[i].equalsIgnoreCase("male_children 10 to 16")) {
                    this.MaleChildren10to16 = Integer.valueOf(fields[i]);
                } else if (fieldNames[i].equalsIgnoreCase("male_children")) {
                    // This is because two fields are identically named in many of the files!
                    if (doneMaleChildrenUnder10) {
                        this.MaleChildren10to16 = Integer.valueOf(fields[i]);
                    } else {
                        this.MaleChildrenUnder10 = Integer.valueOf(fields[i]);
                        doneMaleChildrenUnder10 = true;
                    }
                } else if (fieldNames[i].equalsIgnoreCase("female_children under 10")) {
                    this.FemaleChildrenUnder10 = Integer.valueOf(fields[i]);
                } else if (fieldNames[i].equalsIgnoreCase("female_children 10 to 16")) {
                    this.FemaleChildren10to16 = Integer.valueOf(fields[i]);
                } else if (fieldNames[i].equalsIgnoreCase("female_childr")) {
                    if (doneFemaleChildrenUnder10) {
                        this.FemaleChildren10to16 = Integer.valueOf(fields[i]);
                    } else {
                        this.FemaleChildrenUnder10 = Integer.valueOf(fields[i]);
                        doneFemaleChildrenUnder10 = true;
                    }
                } else if (fieldNames[i].equalsIgnoreCase("children_over")
                        || fieldNames[i].equalsIgnoreCase("children_over 16")) {
                    this.ChildrenOver16 = Integer.valueOf(fields[i]);
                } else if (fieldNames[i].equalsIgnoreCase("TOTAL Dep Children")) {
                    this.TotalDependentChildren = Integer.valueOf(fields[i]);
                } else if (fieldNames[i].equalsIgnoreCase("nondependants")) {
                    this.NonDependents = Integer.valueOf(fields[i]);
                } else if (fieldNames[i].equalsIgnoreCase("total_rent")
                        || fieldNames[i].equalsIgnoreCase("total_rent arrears")) {
                    if (fields[i].trim().isEmpty()) {
                        this.TotalRentArrears = null;
                    } else {
                        this.TotalRentArrears = Double.valueOf(fields[i]);
                    }
                } else {
                    System.err.println("Unrecognised field: " + fieldNames[i] + ". Debug needed of: " + this.getClass().getName());
                    int debug = 1;
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
                + ",ClaimReferenceNumber " + getClaimRef()
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
     * @return the ClaimRef
     */
    public String getClaimRef() {
        return ClaimRef;
    }

    /**
     * @param ClaimRef the ClaimRef to set
     */
    public void setClaimRef(String ClaimRef) {
        this.ClaimRef = ClaimRef;
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
        hash = 83 * hash + (this.ClaimRef != null ? this.ClaimRef.hashCode() : 0);
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
        if (this.ClaimRef.equalsIgnoreCase(other.ClaimRef)) {
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
        if (!Objects.equals(this.TotalRentArrears, other.TotalRentArrears)) {
            return false;
        }
        return true;
    }
}
