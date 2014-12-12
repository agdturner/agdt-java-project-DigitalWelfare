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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;

/**
 *
 * @author geoagdt
 */
public class SHBE_DataRecord_Handler {

    private HashSet<String> RecordTypes;

    public SHBE_DataRecord_Handler() {
        initRecordTypes();
    }

    public HashSet<String> getRecordTypes() {
        return RecordTypes;
    }

    /**
     * Initialises HashSet<String> RecordTypes
     */
    public final void initRecordTypes() {
        if (RecordTypes == null) {
            RecordTypes = new HashSet<String>();
            RecordTypes.add("A");
            RecordTypes.add("D");
            RecordTypes.add("C");
            RecordTypes.add("R");
            RecordTypes.add("T");
            RecordTypes.add("P");
            RecordTypes.add("G");
            RecordTypes.add("E");
            RecordTypes.add("S");
        }
    }

    /**
     * Loads SHBE data from a file in directory, filename reporting progress of
     * loading to PrintWriter pw.
     *
     * @param directory
     * @param filename
     * @param pw
     * @return Object[7] result where: ----------------------------------------
     * result[0] is a TreeMap<String,SHBE_DataRecord> representing records with
     * DRecords, --------------------------------------------------------------
     * result[1] is a TreeMap<String, SHBE_DataRecord> representing records
     * without DRecords, ------------------------------------------------------
     * result[2] is a HashSet<String> of ClaimantNationalInsuranceNumberIDs,
     * result[3] is a HashSet<String> of PartnerNationalInsuranceNumberIDs,
     * result[4] is a HashSet<String> of DependentsNationalInsuranceNumberIDs
     * result[5] is a HashSet<String> of NonDependentsNationalInsuranceNumberIDs
     * result[6] is a HashSet<String> of AllHouseholdNationalInsuranceNumberIDs
     */
    public Object[] loadInputData(
            File directory,
            String filename,
            PrintWriter pw) {
        Object[] result = new Object[7];
        File inputFile = new File(
                directory,
                filename);
        BufferedReader br = Generic_StaticIO.getBufferedReader(inputFile);
        StreamTokenizer st
                = new StreamTokenizer(br);
        Generic_StaticIO.setStreamTokenizerSyntax5(st);
        st.wordChars('`', '`');
        st.wordChars('*', '*');
        String line = "";
        int totalCouncilTaxBenefitClaims = 0;
        int totalCouncilTaxAndHousingBenefitClaims = 0;
        int totalHousingBenefitClaims = 0;
        TreeMap<String, SHBE_DataRecord> recordsWithDRecords = new TreeMap<String, SHBE_DataRecord>();
        result[0] = recordsWithDRecords;
        TreeMap<String, SHBE_DataRecord> recordsWithoutDRecords = new TreeMap<String, SHBE_DataRecord>();
        result[1] = recordsWithoutDRecords;
        HashSet<String> ClaimantNationalInsuranceNumberIDs = new HashSet<String>();
        result[2] = ClaimantNationalInsuranceNumberIDs;
        HashSet<String> PartnerNationalInsuranceNumberIDs = new HashSet<String>();
        result[3] = PartnerNationalInsuranceNumberIDs;
        HashSet<String> DependentsNationalInsuranceNumberIDs = new HashSet<String>();
        result[4] = DependentsNationalInsuranceNumberIDs;
        HashSet<String> NonDependentsNationalInsuranceNumberIDs = new HashSet<String>();
        result[5] = NonDependentsNationalInsuranceNumberIDs;
        HashSet<String> AllHouseholdNationalInsuranceNumberIDs = new HashSet<String>();
        result[6] = AllHouseholdNationalInsuranceNumberIDs;

        TreeSet<Long> recordIDsNotLoaded = new TreeSet<Long>();

        long RecordID = 0;
        int countSRecords = 0;
        int countDRecords = 0;
        int countOfSRecordsWithoutDRecord = 0;

        try {
            // Read firstline and check format
            int type = readAndCheckFirstLine(directory, filename);
            // Skip the first line
            Generic_StaticIO.skipline(st);
            // Read data
            int tokenType;
            tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {

                // For debugging
                if (RecordID == 10) {
                    int debug = 1;
                }

                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        //System.out.println(line);
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        if (line.startsWith("S")) {
                            try {
                                SHBE_DataRecord SRecord = new SHBE_DataRecord(RecordID, type, line, this);
                                String councilTaxBenefitClaimReferenceNumber = SRecord.getCouncilTaxBenefitClaimReferenceNumber();
                                if (recordsWithDRecords.containsKey(councilTaxBenefitClaimReferenceNumber)) {
                                    //get Drecord and add S record
                                    SHBE_DataRecord DRecord = recordsWithDRecords.get(councilTaxBenefitClaimReferenceNumber);
                                    if (!DRecord.getSRecords().add(SRecord)) {
                                        throw new Exception("Duplicate SRecord " + SRecord);
                                    }
                                } else {
                                    //throw new Exception("There is no existing DRecord for SRecord " + SRecord);
                                    countOfSRecordsWithoutDRecord++;
                                    SHBE_DataRecord DRecord;
                                    if (recordsWithoutDRecords.containsKey(councilTaxBenefitClaimReferenceNumber)) {
                                        DRecord = recordsWithoutDRecords.get(councilTaxBenefitClaimReferenceNumber);
                                    } else {
                                        // Create DRecord
                                        DRecord = new SHBE_DataRecord(RecordID);
                                        DRecord.SRecords = new HashSet<SHBE_DataRecord>();
                                        DRecord.setCouncilTaxBenefitClaimReferenceNumber(councilTaxBenefitClaimReferenceNumber);
                                    }
                                    DRecord.SRecords.add(SRecord);
                                }
                                String subRecordChildReferenceNumberOrNINO = SRecord.getSubRecordChildReferenceNumberOrNINO();
                                if (subRecordChildReferenceNumberOrNINO.length() > 0) {
                                    if (SRecord.getSubRecordType() == 2) {
                                        NonDependentsNationalInsuranceNumberIDs.add(subRecordChildReferenceNumberOrNINO);
                                        AllHouseholdNationalInsuranceNumberIDs.add(subRecordChildReferenceNumberOrNINO);
                                    } else {
                                        DependentsNationalInsuranceNumberIDs.add(subRecordChildReferenceNumberOrNINO);
                                        AllHouseholdNationalInsuranceNumberIDs.add(subRecordChildReferenceNumberOrNINO);
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println(line);
                                System.err.println("RecordID " + RecordID);
                                System.err.println(e.getLocalizedMessage());
                                recordIDsNotLoaded.add(RecordID);
                            }
                            countSRecords++;
                        } else {
                            // line.startsWith("D")
                            try {
                                SHBE_DataRecord aSHBE_DataRecord = new SHBE_DataRecord(RecordID, type, line, this);
                                Object o = recordsWithDRecords.put(aSHBE_DataRecord.getCouncilTaxBenefitClaimReferenceNumber(), aSHBE_DataRecord);
                                if (o != null) {
                                    SHBE_DataRecord existingSHBE_DataRecord = (SHBE_DataRecord) o;
                                    pw.println("existingSHBE_DataRecord" + existingSHBE_DataRecord);
                                    pw.println("replacementSHBE_DataRecord" + aSHBE_DataRecord);
                                }
                                // Count Council Tax and Housing Benefits and combined claims
                                if (!aSHBE_DataRecord.getCouncilTaxBenefitClaimReferenceNumber().trim().isEmpty()) {
                                    totalCouncilTaxBenefitClaims++;
                                    if (!aSHBE_DataRecord.getHousingBenefitClaimReferenceNumber().trim().isEmpty()) {
                                        totalCouncilTaxAndHousingBenefitClaims++;
                                        totalHousingBenefitClaims++;
                                    }
                                } else {
                                    if (!aSHBE_DataRecord.getHousingBenefitClaimReferenceNumber().trim().isEmpty()) {
                                        totalHousingBenefitClaims++;
                                    }
                                }
                                // aSHBE_DataRecord.getNonDependantStatus 11
                                // aSHBE_DataRecord.getSubRecordType() 284
                                String claimantsNationalInsuranceNumber = aSHBE_DataRecord.getClaimantsNationalInsuranceNumber();
                                if (claimantsNationalInsuranceNumber.length() > 0) {
                                    ClaimantNationalInsuranceNumberIDs.add(claimantsNationalInsuranceNumber);
                                    AllHouseholdNationalInsuranceNumberIDs.add(claimantsNationalInsuranceNumber);
//                                // aSHBE_DataRecord.getPartnerFlag() 118
                                    if (aSHBE_DataRecord.getPartnerFlag() > 0) {
                                        String partnersNationalInsuranceNumber = aSHBE_DataRecord.getPartnersNationalInsuranceNumber();
                                        PartnerNationalInsuranceNumberIDs.add(partnersNationalInsuranceNumber);
                                        AllHouseholdNationalInsuranceNumberIDs.add(partnersNationalInsuranceNumber);
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println(line);
                                System.err.println("RecordID " + RecordID);
                                System.err.println(e.getLocalizedMessage());
                                recordIDsNotLoaded.add(RecordID);
                            }
                            countDRecords++;
                        }
                        RecordID++;
                        break;
                }
                tokenType = st.nextToken();
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(SHBE_DataRecord_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Add all SRecords from recordsWithoutDRecords that actually do have 
        // DRecords, it just so happened that the DRecord was read after the 
        // first SRecord
        int countDRecordsInUnexpectedOrder = 0;
        Set<String> s = recordsWithDRecords.keySet();
        Iterator<String> ite = recordsWithoutDRecords.keySet().iterator();
        HashSet<String> rem = new HashSet<String>();
        while (ite.hasNext()) {
            String councilTaxBenefitClaimReferenceNumber = ite.next();
            if (s.contains(councilTaxBenefitClaimReferenceNumber)) {
                SHBE_DataRecord DRecord = recordsWithDRecords.get(recordsWithDRecords);
                DRecord.SRecords.addAll(recordsWithDRecords.get(councilTaxBenefitClaimReferenceNumber).getSRecords());
                rem.add(councilTaxBenefitClaimReferenceNumber);
                countDRecordsInUnexpectedOrder ++;
            }
        }
        ite = rem.iterator();
        while (ite.hasNext()) {
            recordsWithoutDRecords.remove(ite.next());
        }
        pw.println("totalCouncilTaxBenefitClaims " + totalCouncilTaxBenefitClaims);
        pw.println("totalCouncilTaxAndHousingBenefitClaims " + totalCouncilTaxAndHousingBenefitClaims);
        pw.println("totalHousingBenefitClaims " + totalHousingBenefitClaims);
        pw.println("countDRecords " + countDRecords);
        pw.println("countSRecords " + countSRecords);
        pw.println("countOfSRecordsWithoutDRecord " + countOfSRecordsWithoutDRecord);
        pw.println("countDRecordsInUnexpectedOrder " + countDRecordsInUnexpectedOrder);
        pw.println("recordIDsNotLoaded.size() " + recordIDsNotLoaded.size());
        pw.println("Count of Unique ClaimantNationalInsuranceNumberIDs " + ClaimantNationalInsuranceNumberIDs.size());
        pw.println("Count of Unique PartnerNationalInsuranceNumberIDs " + PartnerNationalInsuranceNumberIDs.size());
        pw.println("Count of Unique DependentsNationalInsuranceNumberIDs " + DependentsNationalInsuranceNumberIDs.size());
        pw.println("Count of Unique NonDependentsNationalInsuranceNumberIDs " + NonDependentsNationalInsuranceNumberIDs.size());
        pw.println("Count of Unique AllHouseholdNationalInsuranceNumberIDs " + AllHouseholdNationalInsuranceNumberIDs.size());
        return result;
    }

    // Month_10_2010_11_381112_D_records.csv
    // 1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,308,309,310,311,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341
    // hb9803_SHBE_206728k\ April\ 2008.csv
    // 1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,307,308,309,310,311,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341
    // 1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,308,309,310,311,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341
    // 307, 315
    public int readAndCheckFirstLine(
            File directory,
            String filename) {
        int type = 0;
        String type0Header = "1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,308,309,310,311,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341";
        String type1Header = "1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,307,308,309,310,311,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341";
        File inputFile = new File(
                directory,
                filename);
        BufferedReader br = Generic_StaticIO.getBufferedReader(inputFile);
        StreamTokenizer st
                = new StreamTokenizer(br);
        Generic_StaticIO.setStreamTokenizerSyntax5(st);
        st.wordChars('`', '`');
        try {
            int tokenType;
            tokenType = st.nextToken();
            String line = "";
            while (tokenType != StreamTokenizer.TT_EOL) {
                switch (tokenType) {
                    case StreamTokenizer.TT_WORD:
                        line += st.sval;
                        break;
                }
                tokenType = st.nextToken();
            }
            br.close();
            if (line.equalsIgnoreCase(type0Header)) {
                return 0;
            }
            if (line.equalsIgnoreCase(type1Header)) {
                return 1;
            } else {
                String[] lineSplit = line.split(",");
                System.err.println("Unrecognised header");
                System.out.println("Number of fields in header " + lineSplit.length);
            }
        } catch (IOException ex) {
            Logger.getLogger(SHBE_DataRecord_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 2;
    }

    public static Aggregate_SHBE_DataRecord aggregate(HashSet<SHBE_DataRecord> records) {
        Aggregate_SHBE_DataRecord result = new Aggregate_SHBE_DataRecord();
        Iterator<SHBE_DataRecord> ite = records.iterator();
        while (ite.hasNext()) {
            SHBE_DataRecord aSHBE_DataRecord = ite.next();
            aggregate(aSHBE_DataRecord, result);
        }
        return result;
    }

    public static void aggregate(
            SHBE_DataRecord aSHBE_DataRecord,
            Aggregate_SHBE_DataRecord a_Aggregate_SHBE_DataRecord) {
        a_Aggregate_SHBE_DataRecord.setTotalClaimCount(a_Aggregate_SHBE_DataRecord.getTotalClaimCount() + 1);
        if (aSHBE_DataRecord.getHousingBenefitClaimReferenceNumber().length() > 2) {
            a_Aggregate_SHBE_DataRecord.setTotalHBClaimCount(a_Aggregate_SHBE_DataRecord.getTotalHBClaimCount() + 1);
        } else {
            a_Aggregate_SHBE_DataRecord.setTotalCTBClaimCount(a_Aggregate_SHBE_DataRecord.getTotalCTBClaimCount() + 1);
        }
        if (aSHBE_DataRecord.getTenancyType() == 1) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType1Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType1Count() + 1);
        }
        if (aSHBE_DataRecord.getTenancyType() == 2) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType2Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType2Count() + 1);
        }
        if (aSHBE_DataRecord.getTenancyType() == 3) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType3Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType3Count() + 1);
        }
        if (aSHBE_DataRecord.getTenancyType() == 4) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType4Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType4Count() + 1);
        }
        if (aSHBE_DataRecord.getTenancyType() == 5) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType5Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType5Count() + 1);
        }
        if (aSHBE_DataRecord.getTenancyType() == 6) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType6Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType6Count() + 1);
        }
        if (aSHBE_DataRecord.getTenancyType() == 7) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType7Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType7Count() + 1);
        }
        if (aSHBE_DataRecord.getTenancyType() == 8) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType8Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType8Count() + 1);
        }
        if (aSHBE_DataRecord.getTenancyType() == 9) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType9Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType9Count() + 1);
        }
        a_Aggregate_SHBE_DataRecord.setTotalNumberOfChildDependents(
                a_Aggregate_SHBE_DataRecord.getTotalNumberOfChildDependents()
                + aSHBE_DataRecord.getNumberOfChildDependents());
        a_Aggregate_SHBE_DataRecord.setTotalNumberOfNonDependents(
                a_Aggregate_SHBE_DataRecord.getTotalNumberOfNonDependents()
                + aSHBE_DataRecord.getNumberOfNonDependents());
        if (aSHBE_DataRecord.getNonDependentStatus() == 0) {
            a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus0(
                    a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus0() + 1);
        }
        if (aSHBE_DataRecord.getNonDependentStatus() == 1) {
            a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus1(
                    a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus1() + 1);
        }
        if (aSHBE_DataRecord.getNonDependentStatus() == 2) {
            a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus2(
                    a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus2() + 1);
        }
        if (aSHBE_DataRecord.getNonDependentStatus() == 3) {
            a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus3(
                    a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus3() + 1);
        }
        if (aSHBE_DataRecord.getNonDependentStatus() == 4) {
            a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus4(
                    a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus4() + 1);
        }
        if (aSHBE_DataRecord.getNonDependentStatus() == 5) {
            a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus5(
                    a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus5() + 1);
        }
        if (aSHBE_DataRecord.getNonDependentStatus() == 6) {
            a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus6(
                    a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus6() + 1);
        }
        if (aSHBE_DataRecord.getNonDependentStatus() == 7) {
            a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus7(
                    a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus7() + 1);
        }
        if (aSHBE_DataRecord.getNonDependentStatus() == 8) {
            a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus8(
                    a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus8() + 1);
        }
        if (aSHBE_DataRecord.getStatusOfHBClaimAtExtractDate() == 0) {
            a_Aggregate_SHBE_DataRecord.setTotalStatusOfHBClaimAtExtractDate0(a_Aggregate_SHBE_DataRecord.getTotalStatusOfHBClaimAtExtractDate0() + 1);
        }
        if (aSHBE_DataRecord.getStatusOfHBClaimAtExtractDate() == 1) {
            a_Aggregate_SHBE_DataRecord.setTotalStatusOfHBClaimAtExtractDate1(a_Aggregate_SHBE_DataRecord.getTotalStatusOfHBClaimAtExtractDate1() + 1);
        }
        if (aSHBE_DataRecord.getStatusOfHBClaimAtExtractDate() == 2) {
            a_Aggregate_SHBE_DataRecord.setTotalStatusOfHBClaimAtExtractDate2(a_Aggregate_SHBE_DataRecord.getTotalStatusOfHBClaimAtExtractDate2() + 1);
        }
        if (aSHBE_DataRecord.getStatusOfCTBClaimAtExtractDate() == 0) {
            a_Aggregate_SHBE_DataRecord.setTotalStatusOfCTBClaimAtExtractDate0(a_Aggregate_SHBE_DataRecord.getTotalStatusOfCTBClaimAtExtractDate0() + 1);
        }
        if (aSHBE_DataRecord.getStatusOfCTBClaimAtExtractDate() == 1) {
            a_Aggregate_SHBE_DataRecord.setTotalStatusOfCTBClaimAtExtractDate1(a_Aggregate_SHBE_DataRecord.getTotalStatusOfCTBClaimAtExtractDate1() + 1);
        }
        if (aSHBE_DataRecord.getStatusOfCTBClaimAtExtractDate() == 2) {
            a_Aggregate_SHBE_DataRecord.setTotalStatusOfCTBClaimAtExtractDate2(a_Aggregate_SHBE_DataRecord.getTotalStatusOfCTBClaimAtExtractDate2() + 1);
        }
        if (aSHBE_DataRecord.getOutcomeOfFirstDecisionOnMostRecentHBClaim() == 1) {
            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentHBClaim1(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentHBClaim1() + 1);
        }
        if (aSHBE_DataRecord.getOutcomeOfFirstDecisionOnMostRecentHBClaim() == 2) {
            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentHBClaim2(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentHBClaim2() + 1);
        }
        if (aSHBE_DataRecord.getOutcomeOfFirstDecisionOnMostRecentHBClaim() == 3) {
            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentHBClaim3(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentHBClaim3() + 1);
        }
        if (aSHBE_DataRecord.getOutcomeOfFirstDecisionOnMostRecentHBClaim() == 4) {
            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentHBClaim4(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentHBClaim4() + 1);
        }
        if (aSHBE_DataRecord.getOutcomeOfFirstDecisionOnMostRecentHBClaim() == 5) {
            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentHBClaim5(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentHBClaim5() + 1);
        }
        if (aSHBE_DataRecord.getOutcomeOfFirstDecisionOnMostRecentHBClaim() == 6) {
            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentHBClaim6(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentHBClaim6() + 1);
        }
        if (aSHBE_DataRecord.getOutcomeOfFirstDecisionOnMostRecentCTBClaim() == 1) {
            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim1(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim1() + 1);
        }
        if (aSHBE_DataRecord.getOutcomeOfFirstDecisionOnMostRecentCTBClaim() == 2) {
            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim2(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim2() + 1);
        }
        if (aSHBE_DataRecord.getOutcomeOfFirstDecisionOnMostRecentCTBClaim() == 3) {
            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim3(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim3() + 1);
        }
        if (aSHBE_DataRecord.getOutcomeOfFirstDecisionOnMostRecentCTBClaim() == 4) {
            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim4(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim4() + 1);
        }
        if (aSHBE_DataRecord.getOutcomeOfFirstDecisionOnMostRecentCTBClaim() == 5) {
            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim5(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim5() + 1);
        }
        if (aSHBE_DataRecord.getOutcomeOfFirstDecisionOnMostRecentCTBClaim() == 6) {
            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim6(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim6() + 1);
        }
        a_Aggregate_SHBE_DataRecord.setTotalWeeklyHousingBenefitEntitlement(
                a_Aggregate_SHBE_DataRecord.getTotalWeeklyHousingBenefitEntitlement()
                + aSHBE_DataRecord.getWeeklyHousingBenefitEntitlement());
        a_Aggregate_SHBE_DataRecord.setTotalWeeklyCouncilTaxBenefitEntitlement(
                a_Aggregate_SHBE_DataRecord.getTotalWeeklyCouncilTaxBenefitEntitlement()
                + aSHBE_DataRecord.getWeeklyCouncilTaxBenefitEntitlement());
        if (aSHBE_DataRecord.getLHARegulationsApplied().equalsIgnoreCase("NO")) { // A guess at the values: check!
            a_Aggregate_SHBE_DataRecord.setTotalLHARegulationsApplied0(
                    a_Aggregate_SHBE_DataRecord.getTotalLHARegulationsApplied0()
                    + 1);
        } else {
            //aSHBE_DataRecord.getLHARegulationsApplied() == 1
            a_Aggregate_SHBE_DataRecord.setTotalLHARegulationsApplied1(
                    a_Aggregate_SHBE_DataRecord.getTotalLHARegulationsApplied1()
                    + 1);
        }
        a_Aggregate_SHBE_DataRecord.setTotalWeeklyMaximumRent(
                a_Aggregate_SHBE_DataRecord.getTotalWeeklyMaximumRent()
                + aSHBE_DataRecord.getWeeklyMaximumRent());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsAssessedIncomeFigure(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsAssessedIncomeFigure()
                + aSHBE_DataRecord.getClaimantsAssessedIncomeFigure());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsAdjustedAssessedIncomeFigure(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsAdjustedAssessedIncomeFigure()
                + aSHBE_DataRecord.getClaimantsAdjustedAssessedIncomeFigure());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsTotalCapital(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsTotalCapital()
                + aSHBE_DataRecord.getClaimantsTotalCapital());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsGrossWeeklyIncomeFromEmployment(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsGrossWeeklyIncomeFromEmployment()
                + aSHBE_DataRecord.getClaimantsGrossWeeklyIncomeFromEmployment());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsNetWeeklyIncomeFromEmployment(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsNetWeeklyIncomeFromEmployment()
                + aSHBE_DataRecord.getClaimantsNetWeeklyIncomeFromEmployment());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsGrossWeeklyIncomeFromSelfEmployment(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsGrossWeeklyIncomeFromSelfEmployment()
                + aSHBE_DataRecord.getClaimantsGrossWeeklyIncomeFromSelfEmployment());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsNetWeeklyIncomeFromSelfEmployment(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsNetWeeklyIncomeFromSelfEmployment()
                + aSHBE_DataRecord.getClaimantsNetWeeklyIncomeFromSelfEmployment());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsTotalAmountOfEarningsDisregarded(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsTotalAmountOfEarningsDisregarded()
                + aSHBE_DataRecord.getClaimantsTotalAmountOfEarningsDisregarded());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded()
                + aSHBE_DataRecord.getClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromAttendanceAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromAttendanceAllowance()
                + aSHBE_DataRecord.getClaimantsIncomeFromAttendanceAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromAttendanceAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromAttendanceAllowance()
                + aSHBE_DataRecord.getClaimantsIncomeFromAttendanceAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromBusinessStartUpAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromBusinessStartUpAllowance()
                + aSHBE_DataRecord.getClaimantsIncomeFromBusinessStartUpAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromChildBenefit(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromChildBenefit()
                + aSHBE_DataRecord.getClaimantsIncomeFromChildBenefit());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent()
                + aSHBE_DataRecord.getClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromPersonalPension(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromPersonalPension()
                + aSHBE_DataRecord.getClaimantsIncomeFromPersonalPension());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromSevereDisabilityAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromSevereDisabilityAllowance()
                + aSHBE_DataRecord.getClaimantsIncomeFromSevereDisabilityAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromMaternityAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromMaternityAllowance()
                + aSHBE_DataRecord.getClaimantsIncomeFromMaternityAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromContributionBasedJobSeekersAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromContributionBasedJobSeekersAllowance()
                + aSHBE_DataRecord.getClaimantsIncomeFromContributionBasedJobSeekersAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromStudentGrantLoan(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromStudentGrantLoan()
                + aSHBE_DataRecord.getClaimantsIncomeFromStudentGrantLoan());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromStudentGrantLoan(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromStudentGrantLoan()
                + aSHBE_DataRecord.getClaimantsIncomeFromStudentGrantLoan());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromSubTenants(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromSubTenants()
                + aSHBE_DataRecord.getClaimantsIncomeFromSubTenants());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromBoarders(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromBoarders()
                + aSHBE_DataRecord.getClaimantsIncomeFromBoarders());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromTrainingForWorkCommunityAction(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromTrainingForWorkCommunityAction()
                + aSHBE_DataRecord.getClaimantsIncomeFromTrainingForWorkCommunityAction());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromIncapacityBenefitShortTermLower(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromIncapacityBenefitShortTermLower()
                + aSHBE_DataRecord.getClaimantsIncomeFromIncapacityBenefitShortTermLower());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromIncapacityBenefitShortTermHigher(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromIncapacityBenefitShortTermHigher()
                + aSHBE_DataRecord.getClaimantsIncomeFromIncapacityBenefitShortTermHigher());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromIncapacityBenefitLongTerm(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromIncapacityBenefitLongTerm()
                + aSHBE_DataRecord.getClaimantsIncomeFromIncapacityBenefitLongTerm());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromNewDeal50PlusEmploymentCredit(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromNewDeal50PlusEmploymentCredit()
                + aSHBE_DataRecord.getClaimantsIncomeFromNewDeal50PlusEmploymentCredit());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromNewTaxCredits(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromNewTaxCredits()
                + aSHBE_DataRecord.getClaimantsIncomeFromNewTaxCredits());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromDisabilityLivingAllowanceCareComponent(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromDisabilityLivingAllowanceCareComponent()
                + aSHBE_DataRecord.getClaimantsIncomeFromDisabilityLivingAllowanceCareComponent());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent()
                + aSHBE_DataRecord.getClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromGovernemntTraining(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromGovernemntTraining()
                + aSHBE_DataRecord.getClaimantsIncomeFromGovernemntTraining());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromIndustrialInjuriesDisablementBenefit(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromIndustrialInjuriesDisablementBenefit()
                + aSHBE_DataRecord.getClaimantsIncomeFromIndustrialInjuriesDisablementBenefit());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromCarersAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromCarersAllowance()
                + aSHBE_DataRecord.getClaimantsIncomeFromCarersAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromStatutoryMaternityPaternityPay(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromStatutoryMaternityPaternityPay()
                + aSHBE_DataRecord.getClaimantsIncomeFromStatutoryMaternityPaternityPay());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc()
                + aSHBE_DataRecord.getClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromWarDisablementPensionArmedForcesGIP(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromWarDisablementPensionArmedForcesGIP()
                + aSHBE_DataRecord.getClaimantsIncomeFromWarDisablementPensionArmedForcesGIP());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromWarMobilitySupplement(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromWarMobilitySupplement()
                + aSHBE_DataRecord.getClaimantsIncomeFromWarMobilitySupplement());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromWidowsWidowersPension(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromWidowsWidowersPension()
                + aSHBE_DataRecord.getClaimantsIncomeFromWidowsWidowersPension());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromBereavementAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromBereavementAllowance()
                + aSHBE_DataRecord.getClaimantsIncomeFromBereavementAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromWidowedParentsAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromWidowedParentsAllowance()
                + aSHBE_DataRecord.getClaimantsIncomeFromWidowedParentsAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromYouthTrainingScheme(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromYouthTrainingScheme()
                + aSHBE_DataRecord.getClaimantsIncomeFromYouthTrainingScheme());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromStatuatorySickPay(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromStatuatorySickPay()
                + aSHBE_DataRecord.getClaimantsIncomeFromStatuatorySickPay());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsOtherIncome(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsOtherIncome()
                + aSHBE_DataRecord.getClaimantsOtherIncome());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsTotalAmountOfIncomeDisregarded(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsTotalAmountOfIncomeDisregarded()
                + aSHBE_DataRecord.getClaimantsTotalAmountOfIncomeDisregarded());
        a_Aggregate_SHBE_DataRecord.setTotalFamilyPremiumAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalFamilyPremiumAwarded()
                + aSHBE_DataRecord.getFamilyPremiumAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalFamilyLoneParentPremiumAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalFamilyLoneParentPremiumAwarded()
                + aSHBE_DataRecord.getFamilyLoneParentPremiumAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalDisabilityPremiumAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalDisabilityPremiumAwarded()
                + aSHBE_DataRecord.getDisabilityPremiumAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalSevereDisabilityPremiumAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalSevereDisabilityPremiumAwarded()
                + aSHBE_DataRecord.getSevereDisabilityPremiumAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalDisabledChildPremiumAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalDisabledChildPremiumAwarded()
                + aSHBE_DataRecord.getDisabledChildPremiumAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalCarePremiumAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalCarePremiumAwarded()
                + aSHBE_DataRecord.getCarePremiumAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalEnhancedDisabilityPremiumAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalEnhancedDisabilityPremiumAwarded()
                + aSHBE_DataRecord.getEnhancedDisabilityPremiumAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalBereavementPremiumAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalBereavementPremiumAwarded()
                + aSHBE_DataRecord.getBereavementPremiumAwarded());
        if (aSHBE_DataRecord.getPartnersStudentIndicator().equalsIgnoreCase("Y")) {
            a_Aggregate_SHBE_DataRecord.setTotalPartnersStudentIndicator(
                    a_Aggregate_SHBE_DataRecord.getTotalPartnersStudentIndicator() + 1);
        }
        a_Aggregate_SHBE_DataRecord.setTotalPartnersAssessedIncomeFigure(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersAssessedIncomeFigure()
                + aSHBE_DataRecord.getPartnersAssessedIncomeFigure());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersAdjustedAssessedIncomeFigure(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersAdjustedAssessedIncomeFigure()
                + aSHBE_DataRecord.getPartnersAdjustedAssessedIncomeFigure());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersGrossWeeklyIncomeFromEmployment(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersGrossWeeklyIncomeFromEmployment()
                + aSHBE_DataRecord.getPartnersGrossWeeklyIncomeFromEmployment());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersNetWeeklyIncomeFromEmployment(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersNetWeeklyIncomeFromEmployment()
                + aSHBE_DataRecord.getPartnersNetWeeklyIncomeFromEmployment());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersGrossWeeklyIncomeFromSelfEmployment(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersGrossWeeklyIncomeFromSelfEmployment()
                + aSHBE_DataRecord.getPartnersGrossWeeklyIncomeFromSelfEmployment());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersNetWeeklyIncomeFromSelfEmployment(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersNetWeeklyIncomeFromSelfEmployment()
                + aSHBE_DataRecord.getPartnersNetWeeklyIncomeFromSelfEmployment());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersTotalAmountOfEarningsDisregarded(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersTotalAmountOfEarningsDisregarded()
                + aSHBE_DataRecord.getPartnersTotalAmountOfEarningsDisregarded());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded()
                + aSHBE_DataRecord.getPartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromAttendanceAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromAttendanceAllowance()
                + aSHBE_DataRecord.getPartnersIncomeFromAttendanceAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromBusinessStartUpAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromBusinessStartUpAllowance()
                + aSHBE_DataRecord.getPartnersIncomeFromBusinessStartUpAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromChildBenefit(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromChildBenefit()
                + aSHBE_DataRecord.getPartnersIncomeFromChildBenefit());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromPersonalPension(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromPersonalPension()
                + aSHBE_DataRecord.getPartnersIncomeFromPersonalPension());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromSevereDisabilityAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromSevereDisabilityAllowance()
                + aSHBE_DataRecord.getPartnersIncomeFromSevereDisabilityAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromMaternityAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromMaternityAllowance()
                + aSHBE_DataRecord.getPartnersIncomeFromMaternityAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromContributionBasedJobSeekersAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromContributionBasedJobSeekersAllowance()
                + aSHBE_DataRecord.getPartnersIncomeFromContributionBasedJobSeekersAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromStudentGrantLoan(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromStudentGrantLoan()
                + aSHBE_DataRecord.getPartnersIncomeFromStudentGrantLoan());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromSubTenants(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromSubTenants()
                + aSHBE_DataRecord.getPartnersIncomeFromSubTenants());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromBoarders(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromBoarders()
                + aSHBE_DataRecord.getPartnersIncomeFromBoarders());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromTrainingForWorkCommunityAction(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromTrainingForWorkCommunityAction()
                + aSHBE_DataRecord.getPartnersIncomeFromTrainingForWorkCommunityAction());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromIncapacityBenefitShortTermLower(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromIncapacityBenefitShortTermLower()
                + aSHBE_DataRecord.getPartnersIncomeFromIncapacityBenefitShortTermLower());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromIncapacityBenefitShortTermHigher(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromIncapacityBenefitShortTermHigher()
                + aSHBE_DataRecord.getPartnersIncomeFromIncapacityBenefitShortTermHigher());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromIncapacityBenefitLongTerm(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromIncapacityBenefitLongTerm()
                + aSHBE_DataRecord.getPartnersIncomeFromIncapacityBenefitLongTerm());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromNewDeal50PlusEmploymentCredit(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromNewDeal50PlusEmploymentCredit()
                + aSHBE_DataRecord.getPartnersIncomeFromNewDeal50PlusEmploymentCredit());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromNewTaxCredits(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromNewTaxCredits()
                + aSHBE_DataRecord.getPartnersIncomeFromNewTaxCredits());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromDisabilityLivingAllowanceCareComponent(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromDisabilityLivingAllowanceCareComponent()
                + aSHBE_DataRecord.getPartnersIncomeFromDisabilityLivingAllowanceCareComponent());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromDisabilityLivingAllowanceMobilityComponent(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromDisabilityLivingAllowanceMobilityComponent()
                + aSHBE_DataRecord.getPartnersIncomeFromDisabilityLivingAllowanceMobilityComponent());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromGovernemntTraining(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromGovernemntTraining()
                + aSHBE_DataRecord.getPartnersIncomeFromGovernemntTraining());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromIndustrialInjuriesDisablementBenefit(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromIndustrialInjuriesDisablementBenefit()
                + aSHBE_DataRecord.getPartnersIncomeFromIndustrialInjuriesDisablementBenefit());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromCarersAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromCarersAllowance()
                + aSHBE_DataRecord.getPartnersIncomeFromCarersAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromStatuatorySickPay(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromStatuatorySickPay()
                + aSHBE_DataRecord.getPartnersIncomeFromStatuatorySickPay());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromStatutoryMaternityPaternityPay(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromStatutoryMaternityPaternityPay()
                + aSHBE_DataRecord.getPartnersIncomeFromStatutoryMaternityPaternityPay());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc()
                + aSHBE_DataRecord.getPartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromWarDisablementPensionArmedForcesGIP(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromWarDisablementPensionArmedForcesGIP()
                + aSHBE_DataRecord.getPartnersIncomeFromWarDisablementPensionArmedForcesGIP());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromWarMobilitySupplement(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromWarMobilitySupplement()
                + aSHBE_DataRecord.getPartnersIncomeFromWarMobilitySupplement());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromWidowsWidowersPension(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromWidowsWidowersPension()
                + aSHBE_DataRecord.getPartnersIncomeFromWidowsWidowersPension());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromBereavementAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromBereavementAllowance()
                + aSHBE_DataRecord.getPartnersIncomeFromBereavementAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromWidowedParentsAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromWidowedParentsAllowance()
                + aSHBE_DataRecord.getPartnersIncomeFromWidowedParentsAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromYouthTrainingScheme(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromYouthTrainingScheme()
                + aSHBE_DataRecord.getPartnersIncomeFromYouthTrainingScheme());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersOtherIncome(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersOtherIncome()
                + aSHBE_DataRecord.getPartnersOtherIncome());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersTotalAmountOfIncomeDisregarded(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersTotalAmountOfIncomeDisregarded()
                + aSHBE_DataRecord.getPartnersTotalAmountOfIncomeDisregarded());
        if (aSHBE_DataRecord.getClaimantsGender().equalsIgnoreCase("F")) {
            a_Aggregate_SHBE_DataRecord.setTotalClaimantsGenderFemale(
                    a_Aggregate_SHBE_DataRecord.getTotalClaimantsGenderFemale() + 1);
        }
        if (aSHBE_DataRecord.getClaimantsGender().equalsIgnoreCase("M")) {
            a_Aggregate_SHBE_DataRecord.setTotalClaimantsGenderMale(
                    a_Aggregate_SHBE_DataRecord.getTotalClaimantsGenderMale() + 1);
        }
        a_Aggregate_SHBE_DataRecord.setTotalContractualRentAmount(
                a_Aggregate_SHBE_DataRecord.getTotalContractualRentAmount()
                + aSHBE_DataRecord.getContractualRentAmount());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromPensionCreditSavingsCredit(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromPensionCreditSavingsCredit()
                + aSHBE_DataRecord.getClaimantsIncomeFromPensionCreditSavingsCredit());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromPensionCreditSavingsCredit(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromPensionCreditSavingsCredit()
                + aSHBE_DataRecord.getPartnersIncomeFromPensionCreditSavingsCredit());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromMaintenancePayments(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromMaintenancePayments()
                + aSHBE_DataRecord.getClaimantsIncomeFromMaintenancePayments());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromMaintenancePayments(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromMaintenancePayments()
                + aSHBE_DataRecord.getPartnersIncomeFromMaintenancePayments());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromOccupationalPension(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromOccupationalPension()
                + aSHBE_DataRecord.getClaimantsIncomeFromOccupationalPension());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromOccupationalPension(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromOccupationalPension()
                + aSHBE_DataRecord.getPartnersIncomeFromOccupationalPension());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromWidowsBenefit(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromWidowsBenefit()
                + aSHBE_DataRecord.getClaimantsIncomeFromWidowsBenefit());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromWidowsBenefit(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromWidowsBenefit()
                + aSHBE_DataRecord.getPartnersIncomeFromWidowsBenefit());
        a_Aggregate_SHBE_DataRecord.setTotalTotalNumberOfRooms(
                a_Aggregate_SHBE_DataRecord.getTotalTotalNumberOfRooms()
                + aSHBE_DataRecord.getTotalNumberOfRooms());
        a_Aggregate_SHBE_DataRecord.setTotalValueOfLHA(
                a_Aggregate_SHBE_DataRecord.getTotalValueOfLHA()
                + aSHBE_DataRecord.getValueOfLHA());
        if (aSHBE_DataRecord.getPartnersGender().equalsIgnoreCase("F")) {
            a_Aggregate_SHBE_DataRecord.setTotalPartnersGenderFemale(
                    a_Aggregate_SHBE_DataRecord.getTotalPartnersGenderFemale() + 1);
        }
        if (aSHBE_DataRecord.getPartnersGender().equalsIgnoreCase("M")) {
            a_Aggregate_SHBE_DataRecord.setTotalPartnersGenderMale(
                    a_Aggregate_SHBE_DataRecord.getTotalPartnersGenderMale() + 1);
        }
        a_Aggregate_SHBE_DataRecord.setTotalNonDependantGrossWeeklyIncomeFromRemunerativeWork(
                a_Aggregate_SHBE_DataRecord.getTotalNonDependantGrossWeeklyIncomeFromRemunerativeWork()
                + aSHBE_DataRecord.getNonDependantGrossWeeklyIncomeFromRemunerativeWork());
        a_Aggregate_SHBE_DataRecord.setTotalTotalAmountOfBackdatedHBAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalTotalAmountOfBackdatedHBAwarded()
                + aSHBE_DataRecord.getTotalAmountOfBackdatedHBAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalTotalAmountOfBackdatedCTBAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalTotalAmountOfBackdatedCTBAwarded()
                + aSHBE_DataRecord.getTotalAmountOfBackdatedCTBAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersTotalCapital(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersTotalCapital()
                + aSHBE_DataRecord.getPartnersTotalCapital());
        a_Aggregate_SHBE_DataRecord.setTotalWeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure(
                a_Aggregate_SHBE_DataRecord.getTotalWeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure()
                + aSHBE_DataRecord.getWeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsTotalHoursOfRemunerativeWorkPerWeek(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsTotalHoursOfRemunerativeWorkPerWeek()
                + aSHBE_DataRecord.getClaimantsTotalHoursOfRemunerativeWorkPerWeek());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersTotalHoursOfRemunerativeWorkPerWeek(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersTotalHoursOfRemunerativeWorkPerWeek()
                + aSHBE_DataRecord.getPartnersTotalHoursOfRemunerativeWorkPerWeek());
    }

    public static long getClaimantsIncomeTotal(
            SHBE_DataRecord DRecord) {
        long result = 0L;
        result += DRecord.getClaimantsIncomeFromAttendanceAllowance();
        result += DRecord.getClaimantsIncomeFromBereavementAllowance();
        result += DRecord.getClaimantsIncomeFromBoarders();
        result += DRecord.getClaimantsIncomeFromBusinessStartUpAllowance();
        result += DRecord.getClaimantsIncomeFromCarersAllowance();
        result += DRecord.getClaimantsIncomeFromChildBenefit();
        result += DRecord.getClaimantsIncomeFromContributionBasedJobSeekersAllowance();
        result += DRecord.getClaimantsIncomeFromDisabilityLivingAllowanceCareComponent();
        result += DRecord.getClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent();
        result += DRecord.getClaimantsIncomeFromGovernemntTraining();
        result += DRecord.getClaimantsIncomeFromIncapacityBenefitLongTerm();
        result += DRecord.getClaimantsIncomeFromIncapacityBenefitShortTermHigher();
        result += DRecord.getClaimantsIncomeFromIncapacityBenefitShortTermLower();
        result += DRecord.getClaimantsIncomeFromIndustrialInjuriesDisablementBenefit();
        result += DRecord.getClaimantsIncomeFromMaintenancePayments();
        result += DRecord.getClaimantsIncomeFromMaternityAllowance();
        result += DRecord.getClaimantsIncomeFromNewDeal50PlusEmploymentCredit();
        result += DRecord.getClaimantsIncomeFromNewTaxCredits();
        result += DRecord.getClaimantsIncomeFromOccupationalPension();
        result += DRecord.getClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent();
        result += DRecord.getClaimantsIncomeFromPensionCreditSavingsCredit();
        result += DRecord.getClaimantsIncomeFromPersonalPension();
        result += DRecord.getClaimantsIncomeFromSevereDisabilityAllowance();
        result += DRecord.getClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc();
        result += DRecord.getClaimantsIncomeFromStatuatorySickPay();
        result += DRecord.getClaimantsIncomeFromStatutoryMaternityPaternityPay();
        result += DRecord.getClaimantsIncomeFromStudentGrantLoan();
        result += DRecord.getClaimantsIncomeFromSubTenants();
        result += DRecord.getClaimantsIncomeFromTrainingForWorkCommunityAction();
        result += DRecord.getClaimantsIncomeFromWarDisablementPensionArmedForcesGIP();
        result += DRecord.getClaimantsIncomeFromWarMobilitySupplement();
        result += DRecord.getClaimantsIncomeFromWidowedParentsAllowance();
        result += DRecord.getClaimantsIncomeFromWidowsBenefit();
        result += DRecord.getClaimantsIncomeFromWidowsWidowersPension();
        result += DRecord.getClaimantsIncomeFromYouthTrainingScheme();
        return result;
    }

    public static long getPartnersIncomeTotal(
            SHBE_DataRecord DRecord) {
        long result = 0L;
        result += DRecord.getPartnersIncomeFromAttendanceAllowance();
        result += DRecord.getPartnersIncomeFromBereavementAllowance();
        result += DRecord.getPartnersIncomeFromBoarders();
        result += DRecord.getPartnersIncomeFromBusinessStartUpAllowance();
        result += DRecord.getPartnersIncomeFromCarersAllowance();
        result += DRecord.getPartnersIncomeFromChildBenefit();
        result += DRecord.getPartnersIncomeFromContributionBasedJobSeekersAllowance();
        result += DRecord.getPartnersIncomeFromDisabilityLivingAllowanceCareComponent();
        result += DRecord.getPartnersIncomeFromDisabilityLivingAllowanceMobilityComponent();
        result += DRecord.getPartnersIncomeFromGovernemntTraining();
        result += DRecord.getPartnersIncomeFromIncapacityBenefitLongTerm();
        result += DRecord.getPartnersIncomeFromIncapacityBenefitShortTermHigher();
        result += DRecord.getPartnersIncomeFromIncapacityBenefitShortTermLower();
        result += DRecord.getPartnersIncomeFromIndustrialInjuriesDisablementBenefit();
        result += DRecord.getPartnersIncomeFromMaintenancePayments();
        result += DRecord.getPartnersIncomeFromMaternityAllowance();
        result += DRecord.getPartnersIncomeFromNewDeal50PlusEmploymentCredit();
        result += DRecord.getPartnersIncomeFromNewTaxCredits();
        result += DRecord.getPartnersIncomeFromOccupationalPension();
        result += DRecord.getPartnersIncomeFromPensionCreditSavingsCredit();
        result += DRecord.getPartnersIncomeFromPersonalPension();
        result += DRecord.getPartnersIncomeFromSevereDisabilityAllowance();
        result += DRecord.getPartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc();
        result += DRecord.getPartnersIncomeFromStatuatorySickPay();
        result += DRecord.getPartnersIncomeFromStatutoryMaternityPaternityPay();
        result += DRecord.getPartnersIncomeFromStudentGrantLoan();
        result += DRecord.getPartnersIncomeFromSubTenants();
        result += DRecord.getPartnersIncomeFromTrainingForWorkCommunityAction();
        result += DRecord.getPartnersIncomeFromWarDisablementPensionArmedForcesGIP();
        result += DRecord.getPartnersIncomeFromWarMobilitySupplement();
        result += DRecord.getPartnersIncomeFromWidowedParentsAllowance();
        result += DRecord.getPartnersIncomeFromWidowsBenefit();
        result += DRecord.getPartnersIncomeFromWidowsWidowersPension();
        result += DRecord.getPartnersIncomeFromYouthTrainingScheme();
        return result;
    }

    public static long getIncomeTotal(
            SHBE_DataRecord DRecord) {
        long result = getClaimantsIncomeTotal(DRecord) + getPartnersIncomeTotal(DRecord);
        return result;
    }

    public static boolean getUnderOccupancy(
            SHBE_DataRecord DRecord) {
        int numberOfBedroomsForLHARolloutCasesOnly = DRecord.getNumberOfBedroomsForLHARolloutCasesOnly();
        if (numberOfBedroomsForLHARolloutCasesOnly > 0) {
            if (numberOfBedroomsForLHARolloutCasesOnly
                    > DRecord.getNumberOfChildDependents()
                    + DRecord.getNumberOfNonDependents()) {
                return true;
            };
        }
        return false;
    }

    public static int getUnderOccupancyAmount(
            SHBE_DataRecord DRecord) {
        int result = 0;
        int numberOfBedroomsForLHARolloutCasesOnly = DRecord.getNumberOfBedroomsForLHARolloutCasesOnly();
        if (numberOfBedroomsForLHARolloutCasesOnly > 0) {
            result = numberOfBedroomsForLHARolloutCasesOnly
                    - DRecord.getNumberOfChildDependents()
                    - DRecord.getNumberOfNonDependents();
        }
        return result;
    }
}
