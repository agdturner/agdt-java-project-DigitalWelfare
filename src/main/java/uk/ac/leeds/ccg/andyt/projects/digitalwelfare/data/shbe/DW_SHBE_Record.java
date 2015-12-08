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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author geoagdt
 */
public class DW_SHBE_Record implements Serializable {

    /**
     * 0 RecordID
     */
    protected long RecordID;
    
    private String CouncilTaxBenefitClaimReferenceNumber;
    
    /**
     * DRecord
     */
    protected DW_SHBE_D_Record DRecord;
    
    /**
     * SRecords associated with a DRecord
     */
    protected HashSet<DW_SHBE_S_Record> SRecords;
    
    /**
     * Creates a null record in case this is needed
     *
     * @param RecordID
     */
    public DW_SHBE_Record(
            long RecordID) {
        this.RecordID = RecordID;
    }

    /**
     * Creates a null record in case this is needed
     *
     * @param RecordID
     * @param aDRecord
     */
    public DW_SHBE_Record(
            long RecordID,
            DW_SHBE_D_Record aDRecord) {
        this.RecordID = RecordID;
        this.DRecord = aDRecord;
    }
    
    /**
     * @param RecordID
     * @param type ------------------------------------------------------------
     * The type is worked out by reading the first line of the data. type1 has:
     * LandlordPostcode and SubRecordDateOfBirth for S Record; and,
     * LandlordPostcode for D Record.
     * type0-------------------------------------------------------------------
     * 1,2,3,4,8,9, 11,12,13,14,15,16,17,18,19, 20,21,22,23,24,25,26,27,28,29,
     * 30,31,32,33,34,35,36,37,38,39, 40,41,42,43,44,45,46,47,48,49,
     * 50,51,52,53,54,55,56,57,58,59, 60,61,62,63,64,65,66,67,68,69,
     * 70,71,72,73,74,75,76,77,78,79, 80,81,82,83,84,85,86,87,88,89,
     * 90,91,92,93,94,95,96,97,98,99, 100,101,102,103,104,105,106,107,108,109,
     * 110,111,112,113,114,115,116,117,118,119, 120,121,122,123,124,125,126,
     * 130,131,132,133,134,135,136,137,138,139,
     * 140,141,142,143,144,145,146,147,148,149,
     * 150,151,152,153,154,155,156,157,158,159,
     * 160,161,162,163,164,165,166,167,168,169,
     * 170,171,172,173,174,175,176,177,178,179,
     * 180,181,182,183,184,185,186,187,188,189,
     * 190,191,192,193,194,195,196,197,198,199,
     * 200,201,202,203,204,205,206,207,208,209,
     * 210,211,213,214,215,216,217,218,219,
     * 220,221,222,223,224,225,226,227,228,229,
     * 230,231,232,233,234,235,236,237,238,239,
     * 240,241,242,243,244,245,246,247,248,249,
     * 250,251,252,253,254,255,256,257,258,259,
     * 260,261,262,263,264,265,266,267,268,269,
     * 270,271,272,273,274,275,276,277,278, 284,285,286,287,
     * 290,291,292,293,294,295,296,297,298,299, 308,309,
     * 310,311,316,317,318,319, 320,321,322,323,324,325,326,327,328,329,
     * 330,331,332,333,334,335,336,337,338,339, 340,341
     * type1-------------------------------------------------------------------
     * 1,2,3,4,8,9, 11,12,13,14,15,16,17,18,19, 20,21,22,23,24,25,26,27,28,29,
     * 30,31,32,33,34,35,36,37,38,39, 40,41,42,43,44,45,46,47,48,49,
     * 50,51,52,53,54,55,56,57,58,59, 60,61,62,63,64,65,66,67,68,69,
     * 70,71,72,73,74,75,76,77,78,79, 80,81,82,83,84,85,86,87,88,89,
     * 90,91,92,93,94,95,96,97,98,99, 100,101,102,103,104,105,106,107,108,109,
     * 110,111,112,113,114,115,116,117,118,119, 120,121,122,123,124,125,126,
     * 130,131,132,133,134,135,136,137,138,139,
     * 140,141,142,143,144,145,146,147,148,149,
     * 150,151,152,153,154,155,156,157,158,159,
     * 160,161,162,163,164,165,166,167,168,169,
     * 170,171,172,173,174,175,176,177,178,179,
     * 180,181,182,183,184,185,186,187,188,189,
     * 190,191,192,193,194,195,196,197,198,199,
     * 200,201,202,203,204,205,206,207,208,209,
     * 210,211,213,214,215,216,217,218,219,
     * 220,221,222,223,224,225,226,227,228,229,
     * 230,231,232,233,234,235,236,237,238,239,
     * 240,241,242,243,244,245,246,247,248,249,
     * 250,251,252,253,254,255,256,257,258,259,
     * 260,261,262,263,264,265,266,267,268,269,
     * 270,271,272,273,274,275,276,277,278, 284,285,286,287,
     * 290,291,292,293,294,295,296,297,298,299, 307,308,309,
     * 310,311,315,316,317,318,319, 320,321,322,323,324,325,326,327,328,329,
     * 330,331,332,333,334,335,336,337,338,339, 340,341
     * type1-------------------------------------------------------------------
     * 1,2,3,4,8,9,11,12,13,14,15,16,17,18,19, 20,21,22,23,24,25,26,27,28,29,
     * 30,31,32,33,34,35,36,37,38,39, 40,41,42,43,44,45,46,47,48,49,
     * 50,51,52,53,54,55,56,57,58,59, 60,61,62,63,64,65,66,67,68,69,
     * 70,71,72,73,74,75,76,77,78,79, 80,81,82,83,84,85,86,87,88,89,
     * 90,91,92,93,94,95,96,97,98,99, 100,101,102,103,104,105,106,107,108,109,
     * 110,111,112,113,114,115,116,117,118,119, 120,121,122,123,124,125,126,
     * 130,131,132,133,134,135,136,137,138,139,
     * 140,141,142,143,144,145,146,147,148,149,
     * 150,151,152,153,154,155,156,157,158,159,
     * 160,161,162,163,164,165,166,167,168,169,
     * 170,171,172,173,174,175,176,177,178,179,
     * 180,181,182,183,184,185,186,187,188,189,
     * 190,191,192,193,194,195,196,197,198,199,
     * 200,201,202,203,204,205,206,207,208,209,
     * 210,211,213,214,215,216,217,218,219,
     * 220,221,222,223,224,225,226,227,228,229,
     * 230,231,232,233,234,235,236,237,238,239,
     * 240,241,242,243,244,245,246,247,248,249,
     * 250,251,252,253,254,255,256,257,258,259,
     * 260,261,262,263,264,265,266,267,268,269,
     * 270,271,272,273,274,275,276,277,278, 284,285,286,287,
     * 290,291,292,293,294,295,296,297,298,299, 307,308,309,
     * 310,311,315,316,317,318,319, 320,321,322,323,324,325,326,327,328,329,
     * 330,331,332,333,334,335,336,337,338,339, 340,341
     * @param line
     * @param handler
     * @throws java.lang.Exception
     */
    public DW_SHBE_Record(
            long RecordID,
            //int type,
            String line,
            DW_SHBE_Handler handler) throws Exception {
        this.RecordID = RecordID;
        if (line.startsWith("S")) {
            //System.out.println("S Record");
            getSRecords().add(generateSRecord(
                    //type,
                    RecordID,
                    line,
                    handler));
        }
        if (line.startsWith("D"))  {
            //System.out.println("D Record");
            setDRecord(generateDRecord(
                    //type,
                    RecordID,
                    line,
                    handler));
        }
    }
    
    @Override
    public String toString() {
        String result;
        result = "D_Record: " + DRecord.toString();
        long NumberOfS_Records;
        NumberOfS_Records = SRecords.size();
        result += " Number of SRecords = " + NumberOfS_Records;
        if (NumberOfS_Records > 0) {
         result += ": ";   
        }
        Iterator<DW_SHBE_S_Record> ite;
        ite = SRecords.iterator();
        while (ite.hasNext()) {
            DW_SHBE_S_Record S_Record;
            S_Record = ite.next();
            result += " S_Record: " + S_Record.toString();
        }
        return result;
    }

    private DW_SHBE_S_Record generateSRecord(
            //int type,
            long RecordID,
            String line,
            DW_SHBE_Handler handler) throws Exception {
        return new DW_SHBE_S_Record(RecordID,line,handler);
    }
    
    private DW_SHBE_D_Record generateDRecord(
            //int type,
            long RecordID,
            String line,
            DW_SHBE_Handler handler) throws Exception {
        return new DW_SHBE_D_Record(RecordID, line, handler);
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
     * 
     * @return 
     */
    public DW_SHBE_D_Record getDRecord() {
        return DRecord;
    }

    /**
     * 
     * @param DRecord 
     */
    protected void setDRecord(DW_SHBE_D_Record DRecord) {
        this.DRecord = DRecord;
    }

    /**
     * @return the CouncilTaxBenefitClaimReferenceNumber
     */
    public String getCouncilTaxBenefitClaimReferenceNumber() {
        return CouncilTaxBenefitClaimReferenceNumber;
    }

    /**
     * @param CouncilTaxBenefitClaimReferenceNumber the
     * CouncilTaxBenefitClaimReferenceNumber to set
     */
    public void setCouncilTaxBenefitClaimReferenceNumber(String CouncilTaxBenefitClaimReferenceNumber) {
        this.CouncilTaxBenefitClaimReferenceNumber = CouncilTaxBenefitClaimReferenceNumber;
    }
    
    /**
     * @return the SRecords
     */
    public HashSet<DW_SHBE_S_Record> getSRecords() {
        if (SRecords == null) {
            SRecords = new HashSet<DW_SHBE_S_Record>();
        }
        return SRecords;
    }

    /**
     * @param SRecords the SRecords to set
     */
    public void setSRecords(HashSet<DW_SHBE_S_Record> SRecords) {
        this.SRecords = SRecords;
    }

}
