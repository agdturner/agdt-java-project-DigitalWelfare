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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census;

/**
 *
 * @author geoagdt
 */
public class Age_EcoAct_LSOA_DataRecord {

    protected long RecordID;
    
    private String ID;
    private String LSOACode;
    private String GEO_LABEL;
    private String GEO_TYPE;
    private String PopAge16to74;
    private String PopAge16to74Unemployed;
    private String PopAge16to74Retired;
    private String PopAge16to74LookingAfterHomeOrFamily;
    private String PopAge16to74LongTermSickOrDisabled;

    /**
     * Creates a null record in case this is needed
     *
     * @param RecordID
     */
    public Age_EcoAct_LSOA_DataRecord(
            long RecordID) {
        this.RecordID = RecordID;
    }

    /**
     * @param RecordID
     * @param line
     * @param handler
     * @throws java.lang.Exception
     */
    public Age_EcoAct_LSOA_DataRecord(
            long RecordID,
            String line,
            Age_EcoAct_LSOA_DataRecord_Handler handler) throws Exception {
        this.RecordID = RecordID;
        String[] fields = line.split(",");
        if (fields.length != 10) {
            System.out.println("fields.length " + fields.length);
            System.out.println("RecordID " + RecordID);
            System.out.println(line);
        }
        int n = 0;
        ID = fields[n];
        n++;
        LSOACode = fields[n];
        n++;
        GEO_LABEL = fields[n];
        n++;
        GEO_TYPE = fields[n];
        n++;
        PopAge16to74 = fields[n];
        n++;
        PopAge16to74Unemployed = fields[n];
        n++;
        PopAge16to74Retired = fields[n];
        n++;
        PopAge16to74LookingAfterHomeOrFamily = fields[n];
        n++;
        PopAge16to74LongTermSickOrDisabled = fields[n];
    }

    @Override
    public String toString() {
        return "RecordID " + RecordID
                + ",ID " + ID
                + ",LSOACode " + LSOACode
                + ",GEO_LABEL " + GEO_LABEL
                + ",GEO_TYPE " + GEO_TYPE
                + ",PopAge16to74 " + PopAge16to74
                + ",PopAge16to74Unemployed " + PopAge16to74Unemployed
                + ",PopAge16to74Retired " + PopAge16to74Retired
                + ",PopAge16to74LookingAfterHomeOrFamily " + PopAge16to74LookingAfterHomeOrFamily
                + ",PopAge16to74LongTermSickOrDisabled " + PopAge16to74LongTermSickOrDisabled;
    }

    public long getRecordID() {
        return RecordID;
    }

    public void setRecordID(long RecordID) {
        this.RecordID = RecordID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLSOACode() {
        return LSOACode;
    }

    public void setLSOACode(String LSOACode) {
        this.LSOACode = LSOACode;
    }

    public String getGEO_LABEL() {
        return GEO_LABEL;
    }

    public void setGEO_LABEL(String GEO_LABEL) {
        this.GEO_LABEL = GEO_LABEL;
    }

    public String getGEO_TYPE() {
        return GEO_TYPE;
    }

    public void setGEO_TYPE(String GEO_TYPE) {
        this.GEO_TYPE = GEO_TYPE;
    }

    public String getPopAge16to74() {
        return PopAge16to74;
    }

    public void setPopAge16to74(String PopAge16to74) {
        this.PopAge16to74 = PopAge16to74;
    }

    public String getPopAge16to74Unemployed() {
        return PopAge16to74Unemployed;
    }

    public void setPopAge16to74Unemployed(String PopAge16to74Unemployed) {
        this.PopAge16to74Unemployed = PopAge16to74Unemployed;
    }

    public String getPopAge16to74Retired() {
        return PopAge16to74Retired;
    }

    public void setPopAge16to74Retired(String PopAge16to74Retired) {
        this.PopAge16to74Retired = PopAge16to74Retired;
    }

    public String getPopAge16to74LookingAfterHomeOrFamily() {
        return PopAge16to74LookingAfterHomeOrFamily;
    }

    public void setPopAge16to74LookingAfterHomeOrFamily(String PopAge16to74LookingAfterHomeOrFamily) {
        this.PopAge16to74LookingAfterHomeOrFamily = PopAge16to74LookingAfterHomeOrFamily;
    }

    public String getPopAge16to74LongTermSickOrDisabled() {
        return PopAge16to74LongTermSickOrDisabled;
    }

    public void setPopAge16to74LongTermSickOrDisabled(String PopAge16to74LongTermSickOrDisabled) {
        this.PopAge16to74LongTermSickOrDisabled = PopAge16to74LongTermSickOrDisabled;
    }

    

}
