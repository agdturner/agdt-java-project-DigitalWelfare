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
import java.io.StreamTokenizer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.math.Generic_BigDecimal;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Time;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Set;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class DW_SHBE_Handler {

    private HashSet<String> RecordTypes;

    public static final String sHB = "HB";
    public static final String sCTB = "CTB";
    
    private static HashMap<String, DW_ID> NINOToIDLookup;
    private static HashMap<DW_ID, String> IDToNINOLookup;
    private static HashMap<String, DW_ID> PostcodeToPostcodeIDLookup;
    private static HashMap<DW_ID, String> PostcodeIDToPostcodeLookup;

    public DW_SHBE_Handler() {
        initRecordTypes();
    }

    public static void main(String[] args) {
        new DW_SHBE_Handler().run();
        //new DW_SHBE_Handler().runNew();
    }

    public void run() {
        String[] SHBEFilenames;
        SHBEFilenames = getSHBEFilenamesAll();
        File dir;
        dir = DW_Files.getInputSHBEDir();
        boolean loadFromSource;
        loadFromSource = true;
        // @TODO Pass these in
        File NINOToIDLookupFile;
        File IDToNINOLookupFile;
        File PostcodeToPostcodeIDLookupFile;
        File PostcodeIDToPostcodeLookupFile;

        PostcodeToPostcodeIDLookupFile = getPostcodeToPostcodeIDLookupFile();
        PostcodeIDToPostcodeLookupFile = getPostcodeIDToPostcodeLookupFile();
//        HashMap<String, DW_ID> NINOToIDLookup;
//        HashMap<DW_ID, String> IDToNINOLookup;
//        HashMap<String, DW_ID> PostcodeToPostcodeIDLookup;
//        HashMap<DW_ID, String> PostcodeIDToPostcodeLookup;
//        NINOToIDLookup = getNINOToIDLookup(NINOToIDLookupFile);
//        IDToNINOLookup = getIDToNINOLookup(IDToNINOLookupFile);
        NINOToIDLookup = new HashMap<String, DW_ID>();
        IDToNINOLookup = new HashMap<DW_ID, String>();

//        PostcodeToPostcodeIDLookup = getPostcodeToPostcodeIDLookup(
//                PostcodeToPostcodeIDLookupFile);
//        PostcodeIDToPostcodeLookup = getPostcodeIDToPostcodeLookup(
//                PostcodeIDToPostcodeLookupFile);
        PostcodeToPostcodeIDLookup = new HashMap<String, DW_ID>();
        PostcodeIDToPostcodeLookup = new HashMap<DW_ID, String>();

        ArrayList<String> inPaymentTypes;
        inPaymentTypes = getPaymentTypes();
        Iterator<String> ite = inPaymentTypes.iterator();
        while (ite.hasNext()) {
            String inPaymentType;
            inPaymentType = ite.next();
            NINOToIDLookupFile = getNINOToIDLookupFile(inPaymentType);
            IDToNINOLookupFile = getIDToNINOLookupFile(inPaymentType);
            for (int i = 0; i < SHBEFilenames.length; i++) {
                loadInputData(
                        dir,
                        SHBEFilenames[i],
                        inPaymentType,
                        loadFromSource);
//                    NINOToIDLookupFile,
//                    IDToNINOLookupFile,
//                    PostcodeToPostcodeIDLookupFile,
//                    PostcodeIDToPostcodeLookupFile,
//                    NINOToIDLookup,
//                    IDToNINOLookup,
//                    PostcodeToPostcodeIDLookup,
//                    PostcodeIDToPostcodeLookup);
            }
            // writeOutLookups
            Generic_StaticIO.writeObject(NINOToIDLookup, NINOToIDLookupFile);
            Generic_StaticIO.writeObject(IDToNINOLookup, IDToNINOLookupFile);
            Generic_StaticIO.writeObject(PostcodeToPostcodeIDLookup, PostcodeToPostcodeIDLookupFile);
            Generic_StaticIO.writeObject(PostcodeIDToPostcodeLookup, PostcodeIDToPostcodeLookupFile);
        }
        //loadInputData(dir, SHBEFilenames[47], loadFromSource);
    }

    public static final String sAllPT = "AllPT";
    public static final String sInPayment = "InPayment";
    public static final String sSuspended = "Suspended";
    public static final String sOtherPT = "OtherPT";
    
    public static ArrayList<String> getPaymentTypes() {
        ArrayList<String> result;
        result = new ArrayList<String>();
        result.add(sAllPT);
        result.add(sInPayment);
        result.add(sSuspended);
        result.add(sOtherPT);
        //result.add("NotInPaymentNotSuspended");
        return result;
    }

    public void runNew() {
        String SHBEFilename;
        SHBEFilename = "hb9991_SHBE_754889k October 2015.csv";
        File dir;
        dir = DW_Files.getInputSHBEDir();
        boolean loadFromSource;
        loadFromSource = true;
        // @TODO Pass these in
        File NINOToIDLookupFile;
        File IDToNINOLookupFile;
        File PostcodeToPostcodeIDLookupFile;
        File PostcodeIDToPostcodeLookupFile;
        ArrayList<String> inPaymentTypes;
        inPaymentTypes = getPaymentTypes();
        Iterator<String> ite = inPaymentTypes.iterator();
        while (ite.hasNext()) {
            String inPaymentType;
            inPaymentType = ite.next();
            NINOToIDLookupFile = getNINOToIDLookupFile(inPaymentType);
            IDToNINOLookupFile = getIDToNINOLookupFile(inPaymentType);
            PostcodeToPostcodeIDLookupFile = getPostcodeToPostcodeIDLookupFile();
            PostcodeIDToPostcodeLookupFile = getPostcodeIDToPostcodeLookupFile();
//        HashMap<String, DW_ID> NINOToIDLookup;
//        HashMap<DW_ID, String> IDToNINOLookup;
//        HashMap<String, DW_ID> PostcodeToPostcodeIDLookup;
//        HashMap<DW_ID, String> PostcodeIDToPostcodeLookup;
            NINOToIDLookup = getNINOToIDLookup(NINOToIDLookupFile);
            IDToNINOLookup = getIDToNINOLookup(IDToNINOLookupFile);
            PostcodeToPostcodeIDLookup = getPostcodeToPostcodeIDLookup(
                    PostcodeToPostcodeIDLookupFile);
            PostcodeIDToPostcodeLookup = getPostcodeIDToPostcodeLookup(
                    PostcodeIDToPostcodeLookupFile);
            loadInputDataNew(
                    dir,
                    SHBEFilename,
                    inPaymentType,
                    loadFromSource);
        }
    }

//    public static String getClaimantType(DW_SHBE_D_Record D_Record) {
//        String HBClaimRefNo;
//        HBClaimRefNo = D_Record.getHousingBenefitClaimReferenceNumber();
//        return getClaimantType(HBClaimRefNo);
//    }
    
    
//    public static String getClaimantType(DW_SHBE_D_Record D_Record) {
//        boolean isCurrentHBClaimInPayment;
//        isCurrentHBClaimInPayment = isCurrentHBClaimInPayment(D_Record);
//        boolean isCurrentCTBOnlyClaimInPayment;
//        isCurrentCTBOnlyClaimInPayment = isCurrentCTBOnlyClaimInPayment(D_Record);
//        if (isCurrentHBClaimInPayment) {
//            if (isCurrentCTBOnlyClaimInPayment) {
//                return "HBAndCTB";
//            } else {
//                return "HBOnly";
//            }
//        } else {
//            if (isCurrentCTBOnlyClaimInPayment) {
//                return "CTBOnly";
//            } else {
//                return "NotInPayment";
//            }
//        }
//    }

    public static String getClaimantType(DW_SHBE_D_Record D_Record) {
        if (isCurrentHBClaim(D_Record)) {
            return sHB;
        }
        //if (isCurrentCTBOnlyClaim(D_Record)) {
            return sCTB;
        //}
    }

    public static ArrayList<String> getClaimantTypes() {
        ArrayList<String> result;
        result = new ArrayList<String>();
        result.add(sHB);
        result.add(sCTB);
        return result;
    }
        
    @Deprecated
    public static String getClaimantType(String HBClaimRefNo) {
        String result;
        if (HBClaimRefNo == null) {
            result = sCTB;
        } else {
            if (HBClaimRefNo.isEmpty()) {
                result = sCTB;
            } else {
                result = sHB;
            }
        }
        return result;
    }

    public static boolean isCurrentCTBOnlyClaimOtherPT(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfCTBClaimAtExtractDate() == 0) {
            return isCurrentCTBOnlyClaim(D_Record);
        }
        return false;
    }

    public static boolean isCurrentCTBOnlyClaimSuspended(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfCTBClaimAtExtractDate() == 2) {
            return isCurrentCTBOnlyClaim(D_Record);
        }
        return false;
    }

    public static boolean isCurrentCTBOnlyClaimInPayment(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfCTBClaimAtExtractDate() == 1) {
            return isCurrentCTBOnlyClaim(D_Record);
        }
        return false;
    }

    public static boolean isCurrentCTBOnlyClaim(DW_SHBE_D_Record D_Record) {
        int TT;
        TT = D_Record.getTenancyType();
//        if (TT == 5 || TT == 7) {
//            return true;
//        }
//        return false;
        return TT == 5 || TT == 7;
    }

    public static boolean isCurrentHBClaimOtherPT(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfHBClaimAtExtractDate() == 0) {
            return isCurrentHBClaim(D_Record);
        }
        return false;
    }

    public static boolean isCurrentHBClaimSuspended(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfHBClaimAtExtractDate() == 2) {
            return isCurrentHBClaim(D_Record);
        }
        return false;
    }

    public static boolean isCurrentHBClaimInPayment(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfHBClaimAtExtractDate() == 1) {
            return isCurrentHBClaim(D_Record);
        }
        return false;
    }

    public static boolean isCurrentHBClaim(DW_SHBE_D_Record D_Record) {
        int TT;
        TT = D_Record.getTenancyType();
//        if (TT > 0 && TT < 10 && (TT != 5 || TT != 7)) {
//            return true;
//        }
//        return false;
        return TT > 0 && TT < 10 && (TT != 5 || TT != 7);
    }

    public HashSet<String> getRecordTypes() {
        return RecordTypes;
    }

    /**
     * Initialises RecordTypes
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
     * Attempts to load all SHBE collections.
     *
     * @return ArrayList<Object[]> result where:--------------------------------
     *
     */
    public ArrayList<Object[]> loadSHBEData() {

        DW_Environment env;
        env = new DW_Environment();
        env._Directory = DW_Files.getGeneratedDir();

        ArrayList<Object[]> result;
        result = new ArrayList();
        File dir;
        dir = DW_Files.getInputSHBEDir();
        String[] filenames = getSHBEFilenamesAll();
        for (String filename : filenames) {
            System.out.println("Loading SHBE data from " + filename + " ...");
            File collectionDir = new File(
                    DW_Files.getSwapSHBEDir(),
                    filename);
            DW_SHBE_CollectionHandler handler;
            handler = new DW_SHBE_CollectionHandler(
                    env,
                    collectionDir);
            Object[] SHBEData;
            SHBEData = loadInputData(dir, filename, handler);
            //SHBEData = loadInputData(dir, filename);
            result.add(SHBEData);
            System.out.println("... loaded SHBE data from " + filename + ".");
        }
        return result;
    }

    /**
     * @return Object[16] result {@code
     * result[0] is a TreeMap<String, DW_SHBE_Record> CTBRef, DRecords;
     * result[1] is a TreeMap<String, DW_SHBE_Record> CTBRef, SRecords without DRecords;
     * result[2] is a HashSet<DW_ID> tClaimantIDs;
     * result[3] is a HashSet<DW_ID> tPartnerIDs;
     * result[4] is a HashSet<DW_ID> tDependentsIDs;
     * result[5] is a HashSet<DW_ID> tNonDependentsIDs;
     * result[6] is a HashSet<DW_ID> allHouseholdIDs;
     * result[7] is a HashMap<DW_ID, Long> tClaimantIDToRecordIDLookup;
     * result[8] is a HashMap<DW_ID, String> tClaimantIDToPostcodeLookup;
     * result[9] is a HashMap<DW_ID, Integer> tClaimantIDToTenancyTypeLookup;
     * result[10] is a HashMap<String, DW_ID> tCTBRefToClaimantIDLookup;
     * result[11] is a HashMap<DW_ID, String> tClaimantIDToCTBRefLookup;
     * result[12] is a HashMap<String, Integer> tLoadSummary;
     * result[13] is a HashSet<ID_TenancyType> tClaimantIDAndPostcode.
     * result[14] is a HashSet<ID_TenancyType> tClaimantIDAndTenancyType.
     * result[15] is a HashSet<ID_TenancyType> tClaimantIDAndPostcodeAndTenancyType.
     * }
     *
     * @param directory
     * @param filename
     * @param inPaymentType
     * @param loadFromSource
     */
    public Object[] loadInputData(
            File directory,
            String filename,
            String inPaymentType,
            boolean loadFromSource) {

        Object[] result = new Object[16];
        File inputFile = new File(
                directory,
                filename);
        File tDRecordsFile = getDRecordsFile(inPaymentType, filename);
        File tSRecordsWithoutDRecordsFile = getSRecordsWithoutDRecordsFile(inPaymentType, filename);
        File tClaimantIDsFile = getClaimantIDsFile(inPaymentType, filename);
        File tPartnerIDsFile = getPartnerIDsFile(inPaymentType, filename);
        File tDependentsIDsFile = getDependentsIDsFile(inPaymentType, filename);
        File tNonDependentsIDsFile = getNonDependentsIDsFile(inPaymentType, filename);
        File tAllHouseholdIDsFile = getAllHouseholdIDsFile(inPaymentType, filename);
        File tClaimantIDToRecordIDLookupFile = getClaimantIDToRecordIDLookupFile(inPaymentType, filename);
        File tClaimantIDToPostcodeLookupFile = getClaimantIDToPostcodeLookupFile(inPaymentType, filename);
        File tClaimantIDToTenancyTypeLookupFile = getClaimantIDToTenancyTypeLookupFile(inPaymentType, filename);
        File tCTBRefToClaimantIDLookupFile = getCTBRefToClaimantIDLookupFile(inPaymentType, filename);
        File tClaimantIDToCTBRefLookupFile = getClaimantIDToCTBRefLookupFile(inPaymentType, filename);
        File tLoadSummaryFile = getLoadSummaryFile(inPaymentType, filename);
        File tClaimantIDAndPostcodeFile = getClaimantIDPostcodeSetFile(inPaymentType, filename);
        File tClaimantIDAndTenancyTypeFile = getClaimantIDTenancyTypeSetFile(inPaymentType, filename);
        File tClaimantIDAndPostcodeAndTenancyTypeFile = getClaimantIDPostcodeTenancyTypeSetFile(inPaymentType, filename);
        if (loadFromSource) {
            // Init NINOToIDLookup, IDToNINOLookup, PostcodeToPostcodeIDLookup, 
            // PostcodeIDToPostcodeLookup;
            File NINOToIDLookupFile;
            File IDToNINOLookupFile;
            File PostcodeToPostcodeIDLookupFile;
            File PostcodeIDToPostcodeLookupFile;
            NINOToIDLookupFile = getNINOToIDLookupFile(inPaymentType);
            IDToNINOLookupFile = getIDToNINOLookupFile(inPaymentType);
            PostcodeToPostcodeIDLookupFile = getPostcodeToPostcodeIDLookupFile();
            PostcodeIDToPostcodeLookupFile = getPostcodeIDToPostcodeLookupFile();
//            HashMap<String, DW_ID> NINOToIDLookup;
//            HashMap<DW_ID, String> IDToNINOLookup;
//            HashMap<String, DW_ID> PostcodeToPostcodeIDLookup;
//            HashMap<DW_ID, String> PostcodeIDToPostcodeLookup;
            NINOToIDLookup = getNINOToIDLookup(NINOToIDLookupFile);
            IDToNINOLookup = getIDToNINOLookup(IDToNINOLookupFile);
            PostcodeToPostcodeIDLookup = getPostcodeToPostcodeIDLookup(
                    PostcodeToPostcodeIDLookupFile);
            PostcodeIDToPostcodeLookup = getPostcodeIDToPostcodeLookup(
                    PostcodeIDToPostcodeLookupFile);

            TreeMap<String, DW_SHBE_Record> tDRecords;
            TreeMap<String, DW_SHBE_S_Record> tSRecordsWithoutDRecords;
            int totalCouncilTaxBenefitClaims = 0;
            int totalCouncilTaxAndHousingBenefitClaims = 0;
            int totalHousingBenefitClaims = 0;
            int countSRecords = 0;
//        int countDRecords = 0;
//        int countOfSRecordsWithoutDRecord = 0;
            long totalIncome = 0;
            int totalIncomeGreaterThanZeroCount = 0;
            long totalWeeklyEligibleRentAmount = 0;
            int totalWeeklyEligibleRentAmountGreaterThanZeroCount = 0;
            TreeSet<Long> tRecordIDsNotLoaded = new TreeSet<Long>();
            HashSet<DW_ID> tClaimantIDs;
            HashSet<DW_ID> tPartnerIDs;
            HashSet<DW_ID> tDependentsIDs;
            HashSet<DW_ID> tNonDependentsIDs;
            HashSet<DW_ID> tAllHouseholdIDs;
            HashMap<DW_ID, Long> tClaimantIDToRecordIDLookup;
            HashMap<DW_ID, String> tClaimantIDToPostcodeLookup;
            HashMap<DW_ID, Integer> tClaimantIDToTenancyTypeLookup;
            HashMap<String, DW_ID> tCTBRefToClaimantIDLookup;
            HashMap<DW_ID, String> tClaimantIDToCTBRefLookup;
            HashMap<String, Integer> tLoadSummary;
            HashSet<ID_PostcodeID> tClaimantIDAndPostcodeSet;
            HashSet<ID_TenancyType> tClaimantIDAndTenancyTypeSet;
            HashSet<ID_TenancyType_PostcodeID> tClaimantIDAndPostcodeAndTenancyTypeSet;
            try {
                BufferedReader br;
                br = Generic_StaticIO.getBufferedReader(inputFile);
                StreamTokenizer st
                        = new StreamTokenizer(br);
                Generic_StaticIO.setStreamTokenizerSyntax5(st);
                st.wordChars('`', '`');
                st.wordChars('*', '*');
                String line = "";
                // Initialise result
                tDRecords = new TreeMap<String, DW_SHBE_Record>();
                result[0] = tDRecords;
                tSRecordsWithoutDRecords = new TreeMap<String, DW_SHBE_S_Record>();
                result[1] = tSRecordsWithoutDRecords;
                tClaimantIDs = new HashSet<DW_ID>();
                result[2] = tClaimantIDs;
                tPartnerIDs = new HashSet<DW_ID>();
                result[3] = tPartnerIDs;
                tDependentsIDs = new HashSet<DW_ID>();
                result[4] = tDependentsIDs;
                tNonDependentsIDs = new HashSet<DW_ID>();
                result[5] = tNonDependentsIDs;
                tAllHouseholdIDs = new HashSet<DW_ID>();
                result[6] = tAllHouseholdIDs;
                tClaimantIDToRecordIDLookup = new HashMap<DW_ID, Long>();
                result[7] = tClaimantIDToRecordIDLookup;
                tClaimantIDToPostcodeLookup = new HashMap<DW_ID, String>();
                result[8] = tClaimantIDToPostcodeLookup;
                tClaimantIDToTenancyTypeLookup = new HashMap<DW_ID, Integer>();
                result[9] = tClaimantIDToTenancyTypeLookup;
                tCTBRefToClaimantIDLookup = new HashMap<String, DW_ID>();
                result[10] = tCTBRefToClaimantIDLookup;
                tClaimantIDToCTBRefLookup = new HashMap<DW_ID, String>();
                result[11] = tClaimantIDToCTBRefLookup;
                tLoadSummary = new HashMap<String, Integer>();
                result[12] = tLoadSummary;
                tClaimantIDAndPostcodeSet = new HashSet<ID_PostcodeID>();
                result[13] = tClaimantIDAndPostcodeSet;
                tClaimantIDAndTenancyTypeSet = new HashSet<ID_TenancyType>();
                result[14] = tClaimantIDAndTenancyTypeSet;
                tClaimantIDAndPostcodeAndTenancyTypeSet = new HashSet<ID_TenancyType_PostcodeID>();
                result[15] = tClaimantIDAndPostcodeAndTenancyTypeSet;
                long RecordID = 0;
                int lineCount = 0;
                // Read firstline and check format
                int type = readAndCheckFirstLine(directory, filename);
                // Skip the first line
                Generic_StaticIO.skipline(st);
                // Read collections
                int tokenType;
                tokenType = st.nextToken();
                while (tokenType != StreamTokenizer.TT_EOF) {
                    switch (tokenType) {
                        case StreamTokenizer.TT_EOL:
                            //System.out.println(line);
                            break;
                        case StreamTokenizer.TT_WORD:
                            line = st.sval;
                            if (line.startsWith("S")) {
                                try {
                                    DW_SHBE_S_Record SRecord;
                                    //SRecord = new DW_SHBE_S_Record(RecordID, type, line, this);
                                    SRecord = new DW_SHBE_S_Record(RecordID, line, this);
                                    String CTBRef;
                                    CTBRef = SRecord.getCouncilTaxBenefitClaimReferenceNumber();
                                    if (CTBRef != null) {
                                        if (tDRecords.containsKey(CTBRef)) {
                                            DW_SHBE_Record rec = tDRecords.get(CTBRef);
                                            if (!rec.getSRecords().add(SRecord)) {
                                                throw new Exception("Duplicate SRecord " + SRecord);
                                            }
//                                        } else {
//                                            //throw new Exception("There is no existing DRecord for SRecord " + SRecord);
//                                            countOfSRecordsWithoutDRecord++;
                                        }
                                        String subRecordChildReferenceNumberOrNINO;
                                        subRecordChildReferenceNumberOrNINO = SRecord.getSubRecordChildReferenceNumberOrNINO();
                                        if (subRecordChildReferenceNumberOrNINO.length() > 0) {
                                            DW_ID ID;
                                            ID = getIDAddIfNeeded(
                                                    subRecordChildReferenceNumberOrNINO,
                                                    NINOToIDLookup,
                                                    IDToNINOLookup);
                                            if (SRecord.getSubRecordType() == 2) {
                                                tNonDependentsIDs.add(ID);
                                                tAllHouseholdIDs.add(ID);
                                            } else {
                                                tDependentsIDs.add(ID);
                                                tAllHouseholdIDs.add(ID);
                                            }
                                        }
                                    } else {
                                        tRecordIDsNotLoaded.add(RecordID);
                                    }
                                } catch (Exception e) {
                                    System.err.println(line);
                                    System.err.println("RecordID " + RecordID);
                                    System.err.println(e.getLocalizedMessage());
                                    tRecordIDsNotLoaded.add(RecordID);
                                }
                                countSRecords++;
                            } else {
                                if (line.startsWith("D")) {
                                    try {
                                        DW_SHBE_Record rec;
                                        rec = new DW_SHBE_Record(RecordID);
                                        DW_SHBE_D_Record aDRecord;
                                        aDRecord = new DW_SHBE_D_Record(RecordID, line, this);
                                        rec.DRecord = aDRecord;
                                        String CTBRef;
                                        CTBRef = aDRecord.getCouncilTaxBenefitClaimReferenceNumber();
                                        if (CTBRef != null) {
                                            boolean doLoop = false;
                                            if (inPaymentType.equalsIgnoreCase("All")) {
                                                doLoop = true;
                                                if (!CTBRef.trim().isEmpty()) {
                                                    totalCouncilTaxBenefitClaims++;
                                                    if (!aDRecord.getHousingBenefitClaimReferenceNumber().trim().isEmpty()) {
                                                        totalCouncilTaxAndHousingBenefitClaims++;
                                                        totalHousingBenefitClaims++;
                                                    }
                                                } else {
                                                    if (!aDRecord.getHousingBenefitClaimReferenceNumber().trim().isEmpty()) {
                                                        totalHousingBenefitClaims++;
                                                    }
                                                }
                                            } else {
                                                int StatusOfCTBClaimAtExtractDate;
                                                StatusOfCTBClaimAtExtractDate = aDRecord.getStatusOfCTBClaimAtExtractDate();
                                                int StatusOfHBClaimAtExtractDate;
                                                StatusOfHBClaimAtExtractDate = aDRecord.getStatusOfHBClaimAtExtractDate();
                                                if (inPaymentType.equalsIgnoreCase("InPayment")
                                                        && (StatusOfHBClaimAtExtractDate == 1
                                                        || StatusOfCTBClaimAtExtractDate == 1)) {
                                                    doLoop = true;
                                                    boolean isCurrentCTBClaimInPayment;
                                                    isCurrentCTBClaimInPayment = DW_SHBE_Handler.isCurrentCTBOnlyClaimInPayment(aDRecord);
                                                    if (DW_SHBE_Handler.isCurrentHBClaimInPayment(aDRecord)) {
                                                        totalHousingBenefitClaims++;
                                                        if (isCurrentCTBClaimInPayment) {
                                                            totalCouncilTaxAndHousingBenefitClaims++;
                                                        }
                                                    }
                                                    if (isCurrentCTBClaimInPayment) {
                                                        totalCouncilTaxBenefitClaims++;
                                                    }
                                                }
                                                if (inPaymentType.equalsIgnoreCase("Suspended")
                                                        && (StatusOfHBClaimAtExtractDate == 2
                                                        || StatusOfCTBClaimAtExtractDate == 2)) {
                                                    doLoop = true;
                                                    boolean isCurrentCTBClaimSuspended;
                                                    isCurrentCTBClaimSuspended = DW_SHBE_Handler.isCurrentCTBOnlyClaimSuspended(aDRecord);
                                                    if (DW_SHBE_Handler.isCurrentHBClaimSuspended(aDRecord)) {
                                                        totalHousingBenefitClaims++;
                                                        if (isCurrentCTBClaimSuspended) {
                                                            totalCouncilTaxAndHousingBenefitClaims++;
                                                        }
                                                    }
                                                    if (isCurrentCTBClaimSuspended) {
                                                        totalCouncilTaxBenefitClaims++;
                                                    }
                                                }
                                                if (inPaymentType.equalsIgnoreCase("NotInPaymentNotSuspended")
                                                        && (StatusOfHBClaimAtExtractDate == 0
                                                        || StatusOfCTBClaimAtExtractDate == 0)) {
                                                    doLoop = true;
                                                    boolean isCurrentCTBClaimNotInPaymentNotSuspended;
                                                    isCurrentCTBClaimNotInPaymentNotSuspended = DW_SHBE_Handler.isCurrentCTBOnlyClaimOtherPT(aDRecord);
                                                    if (DW_SHBE_Handler.isCurrentHBClaimOtherPT(aDRecord)) {
                                                        totalHousingBenefitClaims++;
                                                        if (isCurrentCTBClaimNotInPaymentNotSuspended) {
                                                            totalCouncilTaxAndHousingBenefitClaims++;
                                                        }
                                                    }
                                                    if (isCurrentCTBClaimNotInPaymentNotSuspended) {
                                                        totalCouncilTaxBenefitClaims++;
                                                    }
                                                }
                                            }
                                            if (doLoop) {
                                                totalIncome = getClaimantsAndPartnersIncomeTotal(aDRecord);
                                                if (totalIncome > 0) {
                                                    totalIncomeGreaterThanZeroCount++;
                                                }
                                                totalWeeklyEligibleRentAmount = aDRecord.getWeeklyEligibleRentAmount();
                                                if (totalWeeklyEligibleRentAmount > 0) {
                                                    totalWeeklyEligibleRentAmountGreaterThanZeroCount++;
                                                }
                                                Object o = tDRecords.put(CTBRef, rec);
                                                if (o != null) {
                                                    DW_SHBE_Record existingSHBE_DataRecord = (DW_SHBE_Record) o;
                                                    System.out.println("existingSHBE_DataRecord" + existingSHBE_DataRecord);
                                                    System.out.println("replacementSHBE_DataRecord" + aDRecord);
                                                }
                                                // aSHBE_DataRecord.getNonDependantStatus 11
                                                // aSHBE_DataRecord.getSubRecordType() 284
                                                String claimantNINO = aDRecord.getClaimantsNationalInsuranceNumber();
                                                DW_ID claimantID;
                                                if (claimantNINO.length() > 0) {
                                                    claimantID = getIDAddIfNeeded(
                                                            claimantNINO,
                                                            NINOToIDLookup,
                                                            IDToNINOLookup);
                                                    tClaimantIDs.add(claimantID);
                                                    tCTBRefToClaimantIDLookup.put(
                                                            CTBRef,
                                                            claimantID);
                                                    tClaimantIDToCTBRefLookup.put(
                                                            claimantID,
                                                            CTBRef);
                                                    tClaimantIDToRecordIDLookup.put(
                                                            claimantID,
                                                            RecordID);
                                                    String postcode;
                                                    postcode = DW_Postcode_Handler.formatPostcode(
                                                            aDRecord.getClaimantsPostcode());
                                                    tClaimantIDToPostcodeLookup.put(
                                                            claimantID,
                                                            postcode);

                                                    DW_ID postcodeID;
                                                    postcodeID = getIDAddIfNeeded(
                                                            postcode,
                                                            PostcodeToPostcodeIDLookup,
                                                            PostcodeIDToPostcodeLookup);

                                                    tClaimantIDAndPostcodeSet.add(
                                                            new ID_PostcodeID(
                                                                    claimantID,
                                                                    postcodeID));
                                                    int TenancyType;
                                                    TenancyType = aDRecord.getTenancyType();
                                                    ID_TenancyType ID_TenancyType;
                                                    ID_TenancyType = new ID_TenancyType(
                                                            claimantID,
                                                            TenancyType);
                                                    tClaimantIDAndTenancyTypeSet.add(
                                                            ID_TenancyType);
                                                    tClaimantIDAndPostcodeAndTenancyTypeSet.add(
                                                            new ID_TenancyType_PostcodeID(
                                                                    ID_TenancyType,
                                                                    postcodeID));
                                                    tClaimantIDToTenancyTypeLookup.put(
                                                            claimantID,
                                                            TenancyType);
                                                    tAllHouseholdIDs.add(claimantID);
                                                    // aSHBE_DataRecord.getPartnerFlag() 118
                                                    if (aDRecord.getPartnerFlag() > 0) {
                                                        String partnersNationalInsuranceNumber;
                                                        partnersNationalInsuranceNumber = aDRecord.getPartnersNationalInsuranceNumber();
                                                        DW_ID ID;
                                                        ID = getIDAddIfNeeded(
                                                                partnersNationalInsuranceNumber,
                                                                NINOToIDLookup,
                                                                IDToNINOLookup);
                                                        tPartnerIDs.add(ID);
                                                        tAllHouseholdIDs.add(ID);
                                                    }
                                                }
                                            } else {
                                                tRecordIDsNotLoaded.add(RecordID);
                                            }
                                        }
                                    } catch (Exception e) {
                                        System.err.println(line);
                                        System.err.println("RecordID " + RecordID);
                                        //System.err.println(e.getMessage());
                                        System.err.println(e.getLocalizedMessage());
                                        e.printStackTrace();
                                        tRecordIDsNotLoaded.add(RecordID);
                                    }
//                                    countDRecords++;
                                }
                            }
                            lineCount++;
                            RecordID++;
                            break;
                    }
                    tokenType = st.nextToken();
                }
                br.close();
                // Add all SRecords from recordsWithoutDRecords that actually do have 
                // DRecords, it just so happened that the DRecord was read after the 
                // first SRecord
                int countDRecordsInUnexpectedOrder = 0;
                Set<String> s = tDRecords.keySet();
                Iterator<String> ite = tSRecordsWithoutDRecords.keySet().iterator();
                HashSet<String> rem = new HashSet<String>();
                while (ite.hasNext()) {
                    String councilTaxBenefitClaimReferenceNumber = ite.next();
                    if (s.contains(councilTaxBenefitClaimReferenceNumber)) {
                        DW_SHBE_Record DRecord = tDRecords.get(councilTaxBenefitClaimReferenceNumber);
                        DRecord.SRecords.addAll(tDRecords.get(councilTaxBenefitClaimReferenceNumber).getSRecords());
                        rem.add(councilTaxBenefitClaimReferenceNumber);
                        countDRecordsInUnexpectedOrder++;
                    }
                }
                //System.out.println("SRecords that came before DRecords count " + countDRecordsInUnexpectedOrder);
                ite = rem.iterator();
                while (ite.hasNext()) {
                    tSRecordsWithoutDRecords.remove(ite.next());
                }
                // Summary report of load
                System.out.println("totalCouncilTaxBenefitClaims " + totalCouncilTaxBenefitClaims);
                tLoadSummary.put("totalCouncilTaxBenefitClaims", totalCouncilTaxBenefitClaims);
                System.out.println("totalCouncilTaxAndHousingBenefitClaims " + totalCouncilTaxAndHousingBenefitClaims);
                tLoadSummary.put("totalCouncilTaxAndHousingBenefitClaims", totalCouncilTaxAndHousingBenefitClaims);
                System.out.println("totalHousingBenefitClaims " + totalHousingBenefitClaims);
                tLoadSummary.put("totalHousingBenefitClaims", totalHousingBenefitClaims);
                System.out.println("countDRecords " + tDRecords.size());
                tLoadSummary.put("countDRecords", tDRecords.size());
                System.out.println("countSRecords " + countSRecords);
                tLoadSummary.put("countSRecords", countSRecords);
                System.out.println("countOfSRecordsWithoutDRecord " + tSRecordsWithoutDRecords.size());
                tLoadSummary.put("countOfSRecordsWithoutDRecord", tSRecordsWithoutDRecords.size());
                System.out.println("countDRecordsInUnexpectedOrder " + countDRecordsInUnexpectedOrder);
                tLoadSummary.put("countDRecordsInUnexpectedOrder", countDRecordsInUnexpectedOrder);
                System.out.println("tRecordIDsNotLoaded.size() " + tRecordIDsNotLoaded.size());
                tLoadSummary.put("tRecordIDsNotLoaded.size()", tRecordIDsNotLoaded.size());
                System.out.println("uniqueClaimantNationalInsuranceNumberCount " + tClaimantIDs.size());
                tLoadSummary.put("uniqueClaimantNationalInsuranceNumberCount", tClaimantIDs.size());
                System.out.println("uniquePartnerNationalInsuranceNumbersCount " + tPartnerIDs.size());
                tLoadSummary.put("uniquePartnerNationalInsuranceNumbersCount", tPartnerIDs.size());
                System.out.println("uniqueDependentsNationalInsuranceNumbersCount " + tDependentsIDs.size());
                tLoadSummary.put("uniqueDependentsNationalInsuranceNumbersCount", tDependentsIDs.size());
                System.out.println("uniqueNon-DependentsNationalInsuranceNumbersCount " + tNonDependentsIDs.size());
                tLoadSummary.put("uniqueNon-DependentsNationalInsuranceNumbersCount", tNonDependentsIDs.size());
                System.out.println("uniqueAllHouseholdNationalInsuranceNumbersCount " + tAllHouseholdIDs.size());
                tLoadSummary.put("uniqueAllHouseholdNationalInsuranceNumbersCount", tAllHouseholdIDs.size());
                System.out.println("lineCount " + lineCount);
                tLoadSummary.put("lineCount", lineCount);
                System.out.println("totalIncome " + totalIncome);
                //tLoadSummary.put("totalIncome", totalIncome);
                System.out.println("totalIncomeGreaterThanZeroCount " + totalIncomeGreaterThanZeroCount);
                //tLoadSummary.put("totalWeeklyEligibleRentAmount", totalWeeklyEligibleRentAmount);
                System.out.println("totalWeeklyEligibleRentAmountGreaterThanZeroCount " + totalWeeklyEligibleRentAmountGreaterThanZeroCount);
                // Store tDRecords on File
                Generic_StaticIO.writeObject(
                        tDRecords,
                        tDRecordsFile);
                // Store tSRecordsWithoutDRecords on File
                Generic_StaticIO.writeObject(
                        tSRecordsWithoutDRecords,
                        tSRecordsWithoutDRecordsFile);
                // Store tClaimantIDs on File
                Generic_StaticIO.writeObject(
                        tClaimantIDs,
                        tClaimantIDsFile);
                // Store tPartnerIDs on File
                Generic_StaticIO.writeObject(
                        tPartnerIDs,
                        tPartnerIDsFile);
                // Store tDependentsIDs on File
                Generic_StaticIO.writeObject(
                        tDependentsIDs,
                        tDependentsIDsFile);
                // Store tNonDependentsIDs on File
                Generic_StaticIO.writeObject(
                        tNonDependentsIDs,
                        tNonDependentsIDsFile);
                // Store tAllHouseholdIDs on File
                Generic_StaticIO.writeObject(
                        tAllHouseholdIDs,
                        tAllHouseholdIDsFile);
                // Store tClaimantIDToRecordIDLookup on File
                Generic_StaticIO.writeObject(
                        tClaimantIDToRecordIDLookup,
                        tClaimantIDToRecordIDLookupFile);
                // Store tClaimantIDToPostcodeLookup on File
                Generic_StaticIO.writeObject(
                        tClaimantIDToPostcodeLookup,
                        tClaimantIDToPostcodeLookupFile);
                // Store tClaimantIDToTenancyTypeLookup on File
                Generic_StaticIO.writeObject(
                        tClaimantIDToTenancyTypeLookup,
                        tClaimantIDToTenancyTypeLookupFile);
                // Store tCTBClaimRefToClaimantIDLookup
                Generic_StaticIO.writeObject(
                        tCTBRefToClaimantIDLookup,
                        tCTBRefToClaimantIDLookupFile);
                // Store tClaimantIDToCTBRefLookup
                Generic_StaticIO.writeObject(
                        tClaimantIDToCTBRefLookup,
                        tClaimantIDToCTBRefLookupFile);
                // Store tLoadSummary
                Generic_StaticIO.writeObject(
                        tLoadSummary,
                        tLoadSummaryFile);
                // Store tClaimantNationalInsuranceNumberIDAndPostcode
                Generic_StaticIO.writeObject(
                        tClaimantIDAndPostcodeSet,
                        tClaimantIDAndPostcodeFile);
                // Store tClaimantNationalInsuranceNumberIDAndPostcode
                Generic_StaticIO.writeObject(
                        tClaimantIDAndTenancyTypeSet,
                        tClaimantIDAndTenancyTypeFile);
                // Store tClaimantNationalInsuranceNumberIDAndPostcode
                Generic_StaticIO.writeObject(
                        tClaimantIDAndPostcodeAndTenancyTypeSet,
                        tClaimantIDAndPostcodeAndTenancyTypeFile);
            } catch (IOException ex) {
                Logger.getLogger(DW_SHBE_Handler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            result[0] = Generic_StaticIO.readObject(tDRecordsFile);
            result[1] = Generic_StaticIO.readObject(tSRecordsWithoutDRecordsFile);
            result[2] = Generic_StaticIO.readObject(tClaimantIDsFile);
            result[3] = Generic_StaticIO.readObject(tPartnerIDsFile);
            result[4] = Generic_StaticIO.readObject(tDependentsIDsFile);
            result[5] = Generic_StaticIO.readObject(tNonDependentsIDsFile);
            result[6] = Generic_StaticIO.readObject(tAllHouseholdIDsFile);
            result[7] = Generic_StaticIO.readObject(tClaimantIDToRecordIDLookupFile);
            result[8] = Generic_StaticIO.readObject(tClaimantIDToPostcodeLookupFile);
            result[9] = Generic_StaticIO.readObject(tClaimantIDToTenancyTypeLookupFile);
            result[10] = Generic_StaticIO.readObject(tCTBRefToClaimantIDLookupFile);
            result[11] = Generic_StaticIO.readObject(tClaimantIDToCTBRefLookupFile);
            result[12] = Generic_StaticIO.readObject(tLoadSummaryFile);
            result[13] = Generic_StaticIO.readObject(tClaimantIDAndPostcodeFile);
            result[14] = Generic_StaticIO.readObject(tClaimantIDAndTenancyTypeFile);
            result[15] = Generic_StaticIO.readObject(tClaimantIDAndPostcodeAndTenancyTypeFile);
        }
        return result;
    }

    /**
     * @return Object[16] {@code
     * result[0] is a TreeMap<String, DW_SHBE_Record> CTBRef, DRecords;
     * result[1] is a TreeMap<String, DW_SHBE_Record> CTBRef, SRecords without DRecords;
     * result[2] is a HashSet<DW_ID> tClaimantNationalInsuranceNumberIDs;
     * result[3] is a HashSet<DW_ID> tPartnerNationalInsuranceNumberIDs;
     * result[4] is a HashSet<DW_ID> tDependentsNationalInsuranceNumberIDs;
     * result[5] is a HashSet<DW_ID> tNonDependentsNationalInsuranceNumberIDs;
     * result[6] is a HashSet<DW_ID> allHouseholdNationalInsuranceNumberIDs;
     * result[7] is a HashMap<DW_ID, Long> tClaimantNationalInsuranceNumberIDToRecordIDLookup;
     * result[8] is a HashMap<DW_ID, String> tNationalInsuranceNumberIDToPostcodeLookup;
     * result[9] is a HashMap<DW_ID, DW_ID> tNationalInsuranceNumberIDToTenancyTypeLookup;
     * result[10] is a HashMap<String, DW_ID> tCTBRefToNationalInsuranceNumberIDLookup;
     * result[11] is a HashMap<DW_ID, String> tClaimantNationalInsuranceNumberIDToCTBRefLookup;
     * result[12] is a HashMap<String, Integer> tLoadSummary;
     * result[13] is a HashSet<ID_TenancyType> tClaimantNationalInsuranceNumberIDAndPostcode.
     * result[14] is a HashSet<ID_TenancyType> tClaimantNationalInsuranceNumberIDAndTenancyType.
     * result[15] is a HashSet<ID_TenancyType> tClaimantNationalInsuranceNumberIDAndPostcodeAndTenancyType.
     * }
     * Loads SHBE collections from a file in directory.
     *
     * @param directory
     * @param filename
     * @param inPaymentType
     * @param loadFromSource
     */
    public Object[] loadInputDataNew(
            File directory,
            String filename,
            String inPaymentType,
            boolean loadFromSource
    ) {
        Object[] result = loadInputData(
                directory,
                filename,
                inPaymentType,
                loadFromSource);
//        Object[] result = new Object[16];
//        File inputFile = new File(
//                directory,
//                filename);
//        File tDRecordsFile = getDRecordsFile(filename);
//        File tSRecordsWithoutDRecordsFile = getSRecordsWithoutDRecordsFile(filename);
//        File tClaimantIDsFile = getClaimantIDsFile(filename);
//        File tPartnerIDsFile = getPartnerIDsFile(filename);
//        File tDependentsIDsFile = getDependentsIDsFile(filename);
//        File tNonDependentsIDsFile = getNonDependentsIDsFile(filename);
//        File tAllHouseholdIDsFile = getAllHouseholdIDsFile(filename);
//        File tClaimantIDToRecordIDLookupFile = getClaimantIDToRecordIDLookupFile(filename);
//        File tClaimantIDToPostcodeLookupFile = getClaimantIDToPostcodeLookupFile(filename);
//        File tClaimantIDToTenancyTypeLookupFile = getClaimantIDToTenancyTypeLookupFile(filename);
//        File tCTBRefToClaimantIDLookupFile = getCTBRefToClaimantIDLookupFile(filename);
//        File tClaimantIDToCTBRefLookupFile = getClaimantIDToCTBRefLookupFile(filename);
//        File tLoadSummaryFile = getLoadSummaryFile(filename);
//        File tClaimantIDAndPostcodeFile = getClaimantIDPostcodeSetFile(filename);
//        File tClaimantIDAndTenancyTypeFile = getClaimantIDTenancyTypeSetFile(filename);
//        File tClaimantIDAndPostcodeAndTenancyTypeFile = getClaimantIDPostcodeTenancyTypeSetFile(filename);
//        if (loadFromSource) {
//            // Init NINOToIDLookup, IDToNINOLookup, PostcodeToPostcodeIDLookup, 
//            // PostcodeIDToPostcodeLookup;
////            NINOToIDLookup = getNINOToIDLookup(NINOToIDLookupFile);
////            IDToNINOLookup = getIDToNINOLookup(IDToNINOLookupFile);
////            PostcodeToPostcodeIDLookup = getPostcodeToPostcodeIDLookup(
////                    PostcodeToPostcodeIDLookupFile);
////            PostcodeIDToPostcodeLookup = getPostcodeIDToPostcodeLookup(
////                    PostcodeIDToPostcodeLookupFile);
//
//            TreeMap<String, DW_SHBE_Record> tDRecords;
//            TreeMap<String, DW_SHBE_S_Record> tSRecordsWithoutDRecords;
//            int totalCouncilTaxBenefitClaims = 0;
//            int totalCouncilTaxAndHousingBenefitClaims = 0;
//            int totalHousingBenefitClaims = 0;
//            int countSRecords = 0;
////        int countDRecords = 0;
////        int countOfSRecordsWithoutDRecord = 0;
//            long totalIncome = 0;
//            int totalIncomeGreaterThanZeroCount = 0;
//            long totalWeeklyEligibleRentAmount = 0;
//            int totalWeeklyEligibleRentAmountGreaterThanZeroCount = 0;
//            TreeSet<Long> tRecordIDsNotLoaded = new TreeSet<Long>();
//            HashSet<DW_ID> tClaimantIDs;
//            HashSet<DW_ID> tPartnerIDs;
//            HashSet<DW_ID> tDependentsIDs;
//            HashSet<DW_ID> tNonDependentsIDs;
//            HashSet<DW_ID> tAllHouseholdIDs;
//            HashMap<DW_ID, Long> tClaimantIDToRecordIDLookup;
//            HashMap<DW_ID, String> tClaimantIDToPostcodeLookup;
//            HashMap<DW_ID, Integer> tClaimantIDToTenancyTypeLookup;
//            HashMap<String, DW_ID> tCTBRefToClaimantIDLookup;
//            HashMap<DW_ID, String> tClaimantIDToCTBRefLookup;
//            HashMap<String, Integer> tLoadSummary;
//            HashSet<ID_PostcodeID> tClaimantIDAndPostcodeSet;
//            HashSet<ID_TenancyType> tClaimantIDAndTenancyTypeSet;
//            HashSet<ID_TenancyType_PostcodeID> tClaimantIDAndPostcodeAndTenancyTypeSet;
//            try {
//                BufferedReader br;
//                br = Generic_StaticIO.getBufferedReader(inputFile);
//                StreamTokenizer st
//                        = new StreamTokenizer(br);
//                Generic_StaticIO.setStreamTokenizerSyntax5(st);
//                st.wordChars('`', '`');
//                st.wordChars('*', '*');
//                String line = "";
//                // Initialise result
//                tDRecords = new TreeMap<String, DW_SHBE_Record>();
//                result[0] = tDRecords;
//                tSRecordsWithoutDRecords = new TreeMap<String, DW_SHBE_S_Record>();
//                result[1] = tSRecordsWithoutDRecords;
//                tClaimantIDs = new HashSet<DW_ID>();
//                result[2] = tClaimantIDs;
//                tPartnerIDs = new HashSet<DW_ID>();
//                result[3] = tPartnerIDs;
//                tDependentsIDs = new HashSet<DW_ID>();
//                result[4] = tDependentsIDs;
//                tNonDependentsIDs = new HashSet<DW_ID>();
//                result[5] = tNonDependentsIDs;
//                tAllHouseholdIDs = new HashSet<DW_ID>();
//                result[6] = tAllHouseholdIDs;
//                tClaimantIDToRecordIDLookup = new HashMap<DW_ID, Long>();
//                result[7] = tClaimantIDToRecordIDLookup;
//                tClaimantIDToPostcodeLookup = new HashMap<DW_ID, String>();
//                result[8] = tClaimantIDToPostcodeLookup;
//                tClaimantIDToTenancyTypeLookup = new HashMap<DW_ID, Integer>();
//                result[9] = tClaimantIDToTenancyTypeLookup;
//                tCTBRefToClaimantIDLookup = new HashMap<String, DW_ID>();
//                result[10] = tCTBRefToClaimantIDLookup;
//                tClaimantIDToCTBRefLookup = new HashMap<DW_ID, String>();
//                result[11] = tClaimantIDToCTBRefLookup;
//                tLoadSummary = new HashMap<String, Integer>();
//                result[12] = tLoadSummary;
//                tClaimantIDAndPostcodeSet = new HashSet<ID_PostcodeID>();
//                result[13] = tClaimantIDAndPostcodeSet;
//                tClaimantIDAndTenancyTypeSet = new HashSet<ID_TenancyType>();
//                result[14] = tClaimantIDAndTenancyTypeSet;
//                tClaimantIDAndPostcodeAndTenancyTypeSet = new HashSet<ID_TenancyType_PostcodeID>();
//                result[15] = tClaimantIDAndPostcodeAndTenancyTypeSet;
//                long RecordID = 0;
//                int lineCount = 0;
//                // Read firstline and check format
//                int type = readAndCheckFirstLine(directory, filename);
//                // Skip the first line
//                Generic_StaticIO.skipline(st);
//                // Read collections
//                int tokenType;
//                tokenType = st.nextToken();
//                while (tokenType != StreamTokenizer.TT_EOF) {
//                    switch (tokenType) {
//                        case StreamTokenizer.TT_EOL:
//                            //System.out.println(line);
//                            break;
//                        case StreamTokenizer.TT_WORD:
//                            line = st.sval;
//                            if (line.startsWith("S")) {
//                                try {
//                                    DW_SHBE_S_Record SRecord;
//                                    //SRecord = new DW_SHBE_S_Record(RecordID, type, line, this);
//                                    SRecord = new DW_SHBE_S_Record(RecordID, line, this);
//                                    String CTBRef;
//                                    CTBRef = SRecord.getCouncilTaxBenefitClaimReferenceNumber();
//                                    if (CTBRef != null) {
//                                        if (tDRecords.containsKey(CTBRef)) {
//                                            DW_SHBE_Record rec = tDRecords.get(CTBRef);
//                                            if (!rec.getSRecords().add(SRecord)) {
//                                                throw new Exception("Duplicate SRecord " + SRecord);
//                                            }
////                                        } else {
////                                            //throw new Exception("There is no existing DRecord for SRecord " + SRecord);
////                                            countOfSRecordsWithoutDRecord++;
//                                        }
//                                        String subRecordChildReferenceNumberOrNINO;
//                                        subRecordChildReferenceNumberOrNINO = SRecord.getSubRecordChildReferenceNumberOrNINO();
//                                        if (subRecordChildReferenceNumberOrNINO.length() > 0) {
//                                            DW_ID ID;
//                                            ID = getIDAddIfNeeded(
//                                                    subRecordChildReferenceNumberOrNINO,
//                                                    NINOToIDLookup,
//                                                    IDToNINOLookup);
//                                            if (SRecord.getSubRecordType() == 2) {
//                                                tNonDependentsIDs.add(ID);
//                                                tAllHouseholdIDs.add(ID);
//                                            } else {
//                                                tDependentsIDs.add(ID);
//                                                tAllHouseholdIDs.add(ID);
//                                            }
//                                        }
//                                    } else {
//                                        tRecordIDsNotLoaded.add(RecordID);
//                                    }
//                                } catch (Exception e) {
//                                    System.err.println(line);
//                                    System.err.println("RecordID " + RecordID);
//                                    System.err.println(e.getLocalizedMessage());
//                                    tRecordIDsNotLoaded.add(RecordID);
//                                }
//                                countSRecords++;
//                            } else {
//                                if (line.startsWith("D")) {
//                                    try {
//                                        DW_SHBE_Record rec;
//                                        rec = new DW_SHBE_Record(RecordID);
//                                        DW_SHBE_D_Record aDRecord;
//                                        aDRecord = new DW_SHBE_D_Record(RecordID, line, this);
//                                        rec.DRecord = aDRecord;
//                                        totalIncome = getClaimantsAndPartnersIncomeTotal(aDRecord);
//                                        if (totalIncome > 0) {
//                                            totalIncomeGreaterThanZeroCount++;
//                                        }
//                                        totalWeeklyEligibleRentAmount = aDRecord.getWeeklyEligibleRentAmount();
//                                        if (totalWeeklyEligibleRentAmount > 0) {
//                                            totalWeeklyEligibleRentAmountGreaterThanZeroCount++;
//                                        }
//                                        String CTBRef;
//                                        CTBRef = aDRecord.getCouncilTaxBenefitClaimReferenceNumber();
//                                        if (CTBRef != null) {
//                                            Object o = tDRecords.put(CTBRef, rec);
//                                            if (o != null) {
//                                                DW_SHBE_Record existingSHBE_DataRecord = (DW_SHBE_Record) o;
//                                                System.out.println("existingSHBE_DataRecord" + existingSHBE_DataRecord);
//                                                System.out.println("replacementSHBE_DataRecord" + aDRecord);
//                                            }
//                                            // Count Council Tax and Housing Benefits and combined claims
//                                            if (!aDRecord.getCouncilTaxBenefitClaimReferenceNumber().trim().isEmpty()) {
//                                                totalCouncilTaxBenefitClaims++;
//                                                if (!aDRecord.getHousingBenefitClaimReferenceNumber().trim().isEmpty()) {
//                                                    totalCouncilTaxAndHousingBenefitClaims++;
//                                                    totalHousingBenefitClaims++;
//                                                }
//                                            } else {
//                                                if (!aDRecord.getHousingBenefitClaimReferenceNumber().trim().isEmpty()) {
//                                                    totalHousingBenefitClaims++;
//                                                }
//                                            }
//                                            // aSHBE_DataRecord.getNonDependantStatus 11
//                                            // aSHBE_DataRecord.getSubRecordType() 284
//                                            String claimantNINO = aDRecord.getClaimantsNationalInsuranceNumber();
//                                            DW_ID claimantID;
//                                            if (claimantNINO.length() > 0) {
//                                                claimantID = getIDAddIfNeeded(
//                                                        claimantNINO,
//                                                        NINOToIDLookup,
//                                                        IDToNINOLookup);
//                                                tClaimantIDs.add(claimantID);
//                                                tCTBRefToClaimantIDLookup.put(
//                                                        CTBRef,
//                                                        claimantID);
//                                                tClaimantIDToCTBRefLookup.put(
//                                                        claimantID,
//                                                        CTBRef);
//                                                tClaimantIDToRecordIDLookup.put(
//                                                        claimantID,
//                                                        RecordID);
//                                                String postcode;
//                                                postcode = DW_Postcode_Handler.formatPostcode(
//                                                        aDRecord.getClaimantsPostcode());
//                                                tClaimantIDToPostcodeLookup.put(
//                                                        claimantID,
//                                                        postcode);
//
//                                                DW_ID postcodeID;
//                                                postcodeID = getIDAddIfNeeded(
//                                                        claimantNINO,
//                                                        PostcodeToPostcodeIDLookup,
//                                                        PostcodeIDToPostcodeLookup);
//
//                                                tClaimantIDAndPostcodeSet.add(
//                                                        new ID_PostcodeID(
//                                                                claimantID,
//                                                                postcodeID));
//                                                int TenancyType;
//                                                TenancyType = aDRecord.getTenancyType();
//                                                ID_TenancyType ID_TenancyType;
//                                                ID_TenancyType = new ID_TenancyType(
//                                                        claimantID,
//                                                        TenancyType);
//                                                tClaimantIDAndTenancyTypeSet.add(
//                                                        ID_TenancyType);
//                                                tClaimantIDAndPostcodeAndTenancyTypeSet.add(
//                                                        new ID_TenancyType_PostcodeID(
//                                                                ID_TenancyType,
//                                                                postcodeID));
//                                                tClaimantIDToTenancyTypeLookup.put(
//                                                        claimantID,
//                                                        TenancyType);
//                                                tAllHouseholdIDs.add(claimantID);
//                                                // aSHBE_DataRecord.getPartnerFlag() 118
//                                                if (aDRecord.getPartnerFlag() > 0) {
//                                                    String partnersNationalInsuranceNumber;
//                                                    partnersNationalInsuranceNumber = aDRecord.getPartnersNationalInsuranceNumber();
//                                                    DW_ID ID;
//                                                    ID = getIDAddIfNeeded(
//                                                            partnersNationalInsuranceNumber,
//                                                            NINOToIDLookup,
//                                                            IDToNINOLookup);
//                                                    tPartnerIDs.add(ID);
//                                                    tAllHouseholdIDs.add(ID);
//                                                }
//                                            }
//                                        } else {
//                                            tRecordIDsNotLoaded.add(RecordID);
//                                        }
//                                    } catch (Exception e) {
//                                        System.err.println(line);
//                                        System.err.println("RecordID " + RecordID);
//                                        //System.err.println(e.getMessage());
//                                        System.err.println(e.getLocalizedMessage());
//                                        e.printStackTrace();
//                                        tRecordIDsNotLoaded.add(RecordID);
//                                    }
////                                    countDRecords++;
//                                }
//                            }
//                            lineCount++;
//                            RecordID++;
//                            break;
//                    }
//                    tokenType = st.nextToken();
//                }
//                br.close();
//                // Add all SRecords from recordsWithoutDRecords that actually do have 
//                // DRecords, it just so happened that the DRecord was read after the 
//                // first SRecord
//                int countDRecordsInUnexpectedOrder = 0;
//                Set<String> s = tDRecords.keySet();
//                Iterator<String> ite = tSRecordsWithoutDRecords.keySet().iterator();
//                HashSet<String> rem = new HashSet<String>();
//                while (ite.hasNext()) {
//                    String councilTaxBenefitClaimReferenceNumber = ite.next();
//                    if (s.contains(councilTaxBenefitClaimReferenceNumber)) {
//                        DW_SHBE_Record DRecord = tDRecords.get(councilTaxBenefitClaimReferenceNumber);
//                        DRecord.SRecords.addAll(tDRecords.get(councilTaxBenefitClaimReferenceNumber).getSRecords());
//                        rem.add(councilTaxBenefitClaimReferenceNumber);
//                        countDRecordsInUnexpectedOrder++;
//                    }
//                }
//                //System.out.println("SRecords that came before DRecords count " + countDRecordsInUnexpectedOrder);
//                ite = rem.iterator();
//                while (ite.hasNext()) {
//                    tSRecordsWithoutDRecords.remove(ite.next());
//                }
//                // Summary report of load
//                System.out.println("totalCouncilTaxBenefitClaims " + totalCouncilTaxBenefitClaims);
//                tLoadSummary.put("totalCouncilTaxBenefitClaims", totalCouncilTaxBenefitClaims);
//                System.out.println("totalCouncilTaxAndHousingBenefitClaims " + totalCouncilTaxAndHousingBenefitClaims);
//                tLoadSummary.put("totalCouncilTaxAndHousingBenefitClaims", totalCouncilTaxAndHousingBenefitClaims);
//                System.out.println("totalHousingBenefitClaims " + totalHousingBenefitClaims);
//                tLoadSummary.put("totalHousingBenefitClaims", totalHousingBenefitClaims);
//                System.out.println("countDRecords " + tDRecords.size());
//                tLoadSummary.put("countDRecords", tDRecords.size());
//                System.out.println("countSRecords " + countSRecords);
//                tLoadSummary.put("countSRecords", countSRecords);
//                System.out.println("countOfSRecordsWithoutDRecord " + tSRecordsWithoutDRecords.size());
//                tLoadSummary.put("countOfSRecordsWithoutDRecord", tSRecordsWithoutDRecords.size());
//                System.out.println("countDRecordsInUnexpectedOrder " + countDRecordsInUnexpectedOrder);
//                tLoadSummary.put("countDRecordsInUnexpectedOrder", countDRecordsInUnexpectedOrder);
//                System.out.println("tRecordIDsNotLoaded.size() " + tRecordIDsNotLoaded.size());
//                tLoadSummary.put("tRecordIDsNotLoaded.size()", tRecordIDsNotLoaded.size());
//                System.out.println("uniqueClaimantNationalInsuranceNumberCount " + tClaimantIDs.size());
//                tLoadSummary.put("uniqueClaimantNationalInsuranceNumberCount", tClaimantIDs.size());
//                System.out.println("uniquePartnerNationalInsuranceNumbersCount " + tPartnerIDs.size());
//                tLoadSummary.put("uniquePartnerNationalInsuranceNumbersCount", tPartnerIDs.size());
//                System.out.println("uniqueDependentsNationalInsuranceNumbersCount " + tDependentsIDs.size());
//                tLoadSummary.put("uniqueDependentsNationalInsuranceNumbersCount", tDependentsIDs.size());
//                System.out.println("uniqueNon-DependentsNationalInsuranceNumbersCount " + tNonDependentsIDs.size());
//                tLoadSummary.put("uniqueNon-DependentsNationalInsuranceNumbersCount", tNonDependentsIDs.size());
//                System.out.println("uniqueAllHouseholdNationalInsuranceNumbersCount " + tAllHouseholdIDs.size());
//                tLoadSummary.put("uniqueAllHouseholdNationalInsuranceNumbersCount", tAllHouseholdIDs.size());
//                System.out.println("lineCount " + lineCount);
//                tLoadSummary.put("lineCount", lineCount);
//                System.out.println("totalIncome " + totalIncome);
//                //tLoadSummary.put("totalIncome", totalIncome);
//                System.out.println("totalIncomeGreaterThanZeroCount " + totalIncomeGreaterThanZeroCount);
//                //tLoadSummary.put("totalWeeklyEligibleRentAmount", totalWeeklyEligibleRentAmount);
//                System.out.println("totalWeeklyEligibleRentAmountGreaterThanZeroCount " + totalWeeklyEligibleRentAmountGreaterThanZeroCount);
//                // Store tDRecords on File
//                Generic_StaticIO.writeObject(
//                        tDRecords,
//                        tDRecordsFile);
//                // Store tSRecordsWithoutDRecords on File
//                Generic_StaticIO.writeObject(
//                        tSRecordsWithoutDRecords,
//                        tSRecordsWithoutDRecordsFile);
//                // Store tClaimantIDs on File
//                Generic_StaticIO.writeObject(
//                        tClaimantIDs,
//                        tClaimantIDsFile);
//                // Store tPartnerIDs on File
//                Generic_StaticIO.writeObject(
//                        tPartnerIDs,
//                        tPartnerIDsFile);
//                // Store tDependentsIDs on File
//                Generic_StaticIO.writeObject(
//                        tDependentsIDs,
//                        tDependentsIDsFile);
//                // Store tNonDependentsIDs on File
//                Generic_StaticIO.writeObject(
//                        tNonDependentsIDs,
//                        tNonDependentsIDsFile);
//                // Store tAllHouseholdIDs on File
//                Generic_StaticIO.writeObject(
//                        tAllHouseholdIDs,
//                        tAllHouseholdIDsFile);
//                // Store tClaimantIDToRecordIDLookup on File
//                Generic_StaticIO.writeObject(
//                        tClaimantIDToRecordIDLookup,
//                        tClaimantIDToRecordIDLookupFile);
//                // Store tClaimantIDToPostcodeLookup on File
//                Generic_StaticIO.writeObject(
//                        tClaimantIDToPostcodeLookup,
//                        tClaimantIDToPostcodeLookupFile);
//                // Store tClaimantIDToTenancyTypeLookup on File
//                Generic_StaticIO.writeObject(
//                        tClaimantIDToTenancyTypeLookup,
//                        tClaimantIDToTenancyTypeLookupFile);
//                // Store tCTBClaimRefToClaimantIDLookup
//                Generic_StaticIO.writeObject(
//                        tCTBRefToClaimantIDLookup,
//                        tCTBRefToClaimantIDLookupFile);
//                // Store tClaimantIDToCTBRefLookup
//                Generic_StaticIO.writeObject(
//                        tClaimantIDToCTBRefLookup,
//                        tClaimantIDToCTBRefLookupFile);
//                // Store tLoadSummary
//                Generic_StaticIO.writeObject(
//                        tLoadSummary,
//                        tLoadSummaryFile);
//                // Store tClaimantNationalInsuranceNumberIDAndPostcode
//                Generic_StaticIO.writeObject(
//                        tClaimantIDAndPostcodeSet,
//                        tClaimantIDAndPostcodeFile);
//                // Store tClaimantNationalInsuranceNumberIDAndPostcode
//                Generic_StaticIO.writeObject(
//                        tClaimantIDAndTenancyTypeSet,
//                        tClaimantIDAndTenancyTypeFile);
//                // Store tClaimantNationalInsuranceNumberIDAndPostcode
//                Generic_StaticIO.writeObject(
//                        tClaimantIDAndPostcodeAndTenancyTypeSet,
//                        tClaimantIDAndPostcodeAndTenancyTypeFile);
//            } catch (IOException ex) {
//                Logger.getLogger(DW_SHBE_Handler.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else {
//            result[0] = Generic_StaticIO.readObject(tDRecordsFile);
//            result[1] = Generic_StaticIO.readObject(tSRecordsWithoutDRecordsFile);
//            result[2] = Generic_StaticIO.readObject(tClaimantIDsFile);
//            result[3] = Generic_StaticIO.readObject(tPartnerIDsFile);
//            result[4] = Generic_StaticIO.readObject(tDependentsIDsFile);
//            result[5] = Generic_StaticIO.readObject(tNonDependentsIDsFile);
//            result[6] = Generic_StaticIO.readObject(tAllHouseholdIDsFile);
//            result[7] = Generic_StaticIO.readObject(tClaimantIDToRecordIDLookupFile);
//            result[8] = Generic_StaticIO.readObject(tClaimantIDToPostcodeLookupFile);
//            result[9] = Generic_StaticIO.readObject(tClaimantIDToTenancyTypeLookupFile);
//            result[10] = Generic_StaticIO.readObject(tCTBRefToClaimantIDLookupFile);
//            result[11] = Generic_StaticIO.readObject(tClaimantIDToCTBRefLookupFile);
//            result[12] = Generic_StaticIO.readObject(tLoadSummaryFile);
//            result[13] = Generic_StaticIO.readObject(tClaimantIDAndPostcodeFile);
//            result[14] = Generic_StaticIO.readObject(tClaimantIDAndTenancyTypeFile);
//            result[15] = Generic_StaticIO.readObject(tClaimantIDAndPostcodeAndTenancyTypeFile);
//        }
        // Overwrite Lookups
        Generic_StaticIO.writeObject(NINOToIDLookup, getNINOToIDLookupFile(inPaymentType));
        Generic_StaticIO.writeObject(IDToNINOLookup, getIDToNINOLookupFile(inPaymentType));
        Generic_StaticIO.writeObject(PostcodeToPostcodeIDLookup, getPostcodeToPostcodeIDLookupFile());
        Generic_StaticIO.writeObject(PostcodeIDToPostcodeLookup, getPostcodeIDToPostcodeLookupFile());
        return result;
    }

    /**
     * @param S
     * @param SToIDLookup
     * @param IDToSLookup
     * @return
     */
    public DW_ID getIDAddIfNeeded(
            String S,
            HashMap<String, DW_ID> SToIDLookup,
            HashMap<DW_ID, String> IDToSLookup) {
        DW_ID result;
        if (SToIDLookup.containsKey(S)) {
            result = SToIDLookup.get(S);
        } else {
            result = new DW_ID(IDToSLookup.size());
            IDToSLookup.put(result, S);
            SToIDLookup.put(S, result);
        }
        return result;
    }

    /**
     *
     * @param SHBE_Data
     * @param inPaymentType
     * @param filename
     * @param councilUnderOccupiedSet
     * @param doUnderOccupancy
     * @param forceNew
     * @return
     */
    public static HashMap<String, BigDecimal> getIncomeAndRentSummary(
            Object[] SHBE_Data,
            String inPaymentType,
            String filename,
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet,
            boolean doUnderOccupancy,
            boolean forceNew) {
        HashMap<String, BigDecimal> result;
        File tIncomeAndRentSummaryFile = getIncomeAndRentSummaryFile(
                filename,
                inPaymentType,
                doUnderOccupancy);
        if (tIncomeAndRentSummaryFile.exists()) {
            if (!forceNew) {
                result = (HashMap<String, BigDecimal>) Generic_StaticIO.readObject(
                        tIncomeAndRentSummaryFile);
                return result;
            }
        }
        result = new HashMap<String, BigDecimal>();
        TreeMap<String, DW_SHBE_Record> recs;
        recs = (TreeMap<String, DW_SHBE_Record>) SHBE_Data[0];
        //long totalIncome = 0;
        BigDecimal totalIncome = BigDecimal.ZERO;
        long totalIncomeGreaterThanZeroCount = 0;
        BigDecimal totalWeeklyEligibleRentAmount = BigDecimal.ZERO;
        //long totalWeeklyEligibleRentAmount = 0;
        long totalWeeklyEligibleRentAmountGreaterThanZeroCount = 0;
        int nTT = getNumberOfTenancyTypes();
        //long[] totalIncomeByTT;
        BigDecimal[] totalIncomeByTT;
        totalIncomeByTT = new BigDecimal[nTT];
        //long[] totalIncomeByTTGreaterThanZeroCount;
        long[] totalIncomeByTTGreaterThanZeroCount;
        totalIncomeByTTGreaterThanZeroCount = new long[nTT];
        BigDecimal[] totalByTTWeeklyEligibleRentAmount;
        totalByTTWeeklyEligibleRentAmount = new BigDecimal[nTT];
        int[] totalByTTWeeklyEligibleRentAmountGreaterThanZeroCount;
        totalByTTWeeklyEligibleRentAmountGreaterThanZeroCount = new int[nTT];
        for (int i = 0; i < nTT; i++) {
            totalIncomeByTT[i] = BigDecimal.ZERO;
            totalIncomeByTTGreaterThanZeroCount[i] = 0;
            totalByTTWeeklyEligibleRentAmount[i] = BigDecimal.ZERO;
            totalByTTWeeklyEligibleRentAmountGreaterThanZeroCount[i] = 0;
        }
        Iterator<String> ite;
        if (doUnderOccupancy) {
            TreeMap<String, DW_UnderOccupiedReport_Record> map;
            map = councilUnderOccupiedSet.getMap();
            ite = map.keySet().iterator();
            while (ite.hasNext()) {
                String CTBRef;
                CTBRef = ite.next();
                DW_SHBE_Record rec;
                rec = recs.get(CTBRef);
                if (rec != null) {
                    DW_SHBE_D_Record aDRecord;
                    aDRecord = rec.getDRecord();
                    int TT;
                    TT = aDRecord.getTenancyType();
                    BigDecimal income;
                    income = BigDecimal.valueOf(getClaimantsAndPartnersIncomeTotal(aDRecord));
                    //totalIncome += income;
                    totalIncome = totalIncome.add(income);
                    totalIncomeByTT[TT] = totalIncomeByTT[TT].add(income);
                    if (income.compareTo(BigDecimal.ZERO) == 1) {
                        totalIncomeGreaterThanZeroCount++;
                        totalIncomeByTTGreaterThanZeroCount[TT]++;
                    }
                    BigDecimal weeklyEligibleRentAmount;
                    weeklyEligibleRentAmount = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                    totalWeeklyEligibleRentAmount = totalWeeklyEligibleRentAmount.add(weeklyEligibleRentAmount);
                    totalByTTWeeklyEligibleRentAmount[TT] = totalByTTWeeklyEligibleRentAmount[TT].add(weeklyEligibleRentAmount);
                    if (weeklyEligibleRentAmount.compareTo(BigDecimal.ZERO) == 1) {
                        totalWeeklyEligibleRentAmountGreaterThanZeroCount++;
                        totalByTTWeeklyEligibleRentAmountGreaterThanZeroCount[TT]++;
                    }
                }
            }
        } else {
            ite = recs.keySet().iterator();
            while (ite.hasNext()) {
                String CTBRef;
                CTBRef = ite.next();
                DW_SHBE_Record rec;
                rec = recs.get(CTBRef);
                DW_SHBE_D_Record aDRecord;
                aDRecord = rec.getDRecord();
                int TT;
                TT = aDRecord.getTenancyType();
                BigDecimal income;
                income = BigDecimal.valueOf(getClaimantsAndPartnersIncomeTotal(aDRecord));
                //totalIncome += income;
                totalIncome = totalIncome.add(income);
                totalIncomeByTT[TT] = totalIncomeByTT[TT].add(income);
                if (income.compareTo(BigDecimal.ZERO) == 1) {
                    totalIncomeGreaterThanZeroCount++;
                    totalIncomeByTTGreaterThanZeroCount[TT]++;
                }
                BigDecimal weeklyEligibleRentAmount;
                weeklyEligibleRentAmount = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                totalWeeklyEligibleRentAmount = totalWeeklyEligibleRentAmount.add(weeklyEligibleRentAmount);
                totalByTTWeeklyEligibleRentAmount[TT] = totalByTTWeeklyEligibleRentAmount[TT].add(weeklyEligibleRentAmount);
                if (weeklyEligibleRentAmount.compareTo(BigDecimal.ZERO) == 1) {
                    totalWeeklyEligibleRentAmountGreaterThanZeroCount++;
                    totalByTTWeeklyEligibleRentAmountGreaterThanZeroCount[TT]++;
                }
            }
        }
//        //BigDecimal totalIncomeBD;
//        totalIncomeBD = BigDecimal.valueOf(totalIncome);
        BigDecimal totalIncomeGreaterThanZeroCountBD;
        totalIncomeGreaterThanZeroCountBD = BigDecimal.valueOf(totalIncomeGreaterThanZeroCount);
//        BigDecimal totalWeeklyEligibleRentAmountBD;
//        totalWeeklyEligibleRentAmountBD = BigDecimal.valueOf(totalWeeklyEligibleRentAmount);
        BigDecimal totalWeeklyEligibleRentAmountGreaterThanZeroCountBD;
        totalWeeklyEligibleRentAmountGreaterThanZeroCountBD = BigDecimal.valueOf(
                totalWeeklyEligibleRentAmountGreaterThanZeroCount);
        result.put("TotalIncome", totalIncome);
        result.put("TotalIncomeGreaterThanZeroCount",
                totalIncomeGreaterThanZeroCountBD);
        if (totalIncomeGreaterThanZeroCountBD.compareTo(BigDecimal.ZERO) == 1) {
            result.put(
                    "AverageIncome",
                    Generic_BigDecimal.divideRoundIfNecessary(
                            totalIncome, totalIncomeGreaterThanZeroCountBD,
                            2, RoundingMode.HALF_UP));
        } else {
            result.put(
                    "AverageIncome",
                    BigDecimal.ZERO);
        }
        result.put("TotalWeeklyEligibleRentAmount",
                totalWeeklyEligibleRentAmount);
        result.put("TotalWeeklyEligibleRentAmountGreaterThanZeroCount",
                totalWeeklyEligibleRentAmountGreaterThanZeroCountBD);
        if (totalWeeklyEligibleRentAmountGreaterThanZeroCountBD.compareTo(BigDecimal.ZERO) == 1) {
            result.put(
                    "AverageWeeklyEligibleRentAmount",
                    Generic_BigDecimal.divideRoundIfNecessary(
                            totalWeeklyEligibleRentAmount,
                            totalWeeklyEligibleRentAmountGreaterThanZeroCountBD,
                            2, RoundingMode.HALF_UP));
        } else {
            result.put(
                    "AverageWeeklyEligibleRentAmount",
                    BigDecimal.ZERO);
        }
        for (int i = 0; i < nTT; i++) {
            String TTS;
            TTS = "" + i;
            // Income
            result.put("TotalIncomeTenancyType" + TTS,
                    totalIncomeByTT[i]);
            BigDecimal totalIncomeByTTGreaterThanZeroCountBD;
            totalIncomeByTTGreaterThanZeroCountBD = BigDecimal.valueOf(
                    totalIncomeByTTGreaterThanZeroCount[i]);
            result.put("TotalIncomeGreaterThanZeroCountTenancyType" + TTS,
                    totalIncomeByTTGreaterThanZeroCountBD);
            if (totalIncomeByTTGreaterThanZeroCountBD.compareTo(BigDecimal.ZERO) == 1) {
                result.put("AverageIncomeTenancyType" + TTS,
                        Generic_BigDecimal.divideRoundIfNecessary(
                                totalIncomeByTT[i],
                                totalIncomeByTTGreaterThanZeroCountBD,
                                2, RoundingMode.HALF_UP));
            } else {
                result.put("AverageIncomeTenancyType" + TTS,
                        BigDecimal.ZERO);
            }
            // Rent
            result.put("TotalWeeklyEligibleRentAmountTenancyType" + TTS,
                    totalByTTWeeklyEligibleRentAmount[i]);
            BigDecimal totalByTTWeeklyEligibleRentAmountGreaterThanZeroCountBD;
            totalByTTWeeklyEligibleRentAmountGreaterThanZeroCountBD = BigDecimal.valueOf(
                    totalByTTWeeklyEligibleRentAmountGreaterThanZeroCount[i]);
            result.put("TotalWeeklyEligibleRentAmountGreaterThanZeroCountTenancyType" + TTS,
                    totalByTTWeeklyEligibleRentAmountGreaterThanZeroCountBD);
            if (totalByTTWeeklyEligibleRentAmountGreaterThanZeroCountBD.compareTo(BigDecimal.ZERO) == 1) {
                result.put("AverageWeeklyEligibleRentAmountTenancyType" + TTS,
                        Generic_BigDecimal.divideRoundIfNecessary(
                                totalByTTWeeklyEligibleRentAmount[i],
                                totalByTTWeeklyEligibleRentAmountGreaterThanZeroCountBD,
                                2, RoundingMode.HALF_UP));
            } else {
                result.put("AverageWeeklyEligibleRentAmountTenancyType" + TTS,
                        BigDecimal.ZERO);
            }
        }
        Generic_StaticIO.writeObject(result, tIncomeAndRentSummaryFile);
        return result;
    }

    /**
     *
     * @param directory
     * @param filename
     * @param handler
     * @return
     */
    public Object[] loadInputData(
            File directory,
            String filename,
            DW_SHBE_CollectionHandler handler) {
        Object[] result = new Object[7];
        File inputFile = new File(
                directory,
                filename);
        try {
            BufferedReader br;
            br = Generic_StaticIO.getBufferedReader(inputFile);
            StreamTokenizer st;
            st = new StreamTokenizer(br);
            Generic_StaticIO.setStreamTokenizerSyntax5(st);
            st.wordChars('`', '`');
            st.wordChars('*', '*');
            String line = "";
            int totalCouncilTaxBenefitClaims = 0;
            int totalCouncilTaxAndHousingBenefitClaims = 0;
            int totalHousingBenefitClaims = 0;

//        TreeMap<String, DW_SHBE_Record> DRecords;
//        DRecords = new TreeMap<String, DW_SHBE_Record>();
//        result[0] = DRecords;
            result[0] = handler;
            long recordID = 0L;
            long collectionID = 0L;

            DW_SHBE_Collection collection;
            collection = new DW_SHBE_Collection(collectionID, handler);
            handler.add(collection);

            TreeMap<String, DW_SHBE_S_Record> tSRecordsWithoutDRecords;
            tSRecordsWithoutDRecords = new TreeMap<String, DW_SHBE_S_Record>();
            result[1] = tSRecordsWithoutDRecords;
            HashSet<String> ClaimantIDs = new HashSet<String>();
            result[2] = ClaimantIDs;
            HashSet<String> PartnerIDs = new HashSet<String>();
            result[3] = PartnerIDs;
            HashSet<String> DependentsIDs = new HashSet<String>();
            result[4] = DependentsIDs;
            HashSet<String> NonDependentsIDs = new HashSet<String>();
            result[5] = NonDependentsIDs;
            HashSet<String> AllHouseholdIDs = new HashSet<String>();
            result[6] = AllHouseholdIDs;

            TreeSet<Long> recordIDsNotLoaded = new TreeSet<Long>();

            long RecordID = 0;

            long DRecordID = 0;

            int countSRecords = 0;
            int countDRecords = 0;
            int countOfSRecordsWithoutDRecord = 0;

            // Read firstline and check format
            int type = readAndCheckFirstLine(directory, filename);
            // Skip the first line
            Generic_StaticIO.skipline(st);
            // Read collections
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
                                DW_SHBE_S_Record aSRecord;
                                aSRecord = new DW_SHBE_S_Record(
                                        RecordID, line, this);
                                String aCTBCRN;
                                aCTBCRN = aSRecord.getCouncilTaxBenefitClaimReferenceNumber();
                                if (handler.lookup.containsKey(aCTBCRN)) {
                                    //                               if (DRecords.containsKey(councilTaxBenefitClaimReferenceNumber)) {
                                    //get Drecord and add S record
//                                    DW_SHBE_Record DRecord = DRecords.get(councilTaxBenefitClaimReferenceNumber);
                                    long lookupID = handler.lookup.get(aCTBCRN);
                                    DW_SHBE_Record DRecord = collection.data.get(lookupID);
                                    if (!DRecord.getSRecords().add(aSRecord)) {
                                        throw new Exception("Duplicate SRecord " + aSRecord);
                                    }
                                } else {
                                    //throw new Exception("There is no existing DRecord for SRecord " + SRecord);
                                    countOfSRecordsWithoutDRecord++;
//                                    DW_SHBE_Record DRecord;
//                                    if (tSRecordsWithoutDRecords.containsKey(aCTBCRN)) {
//                                        DRecord = tSRecordsWithoutDRecords.get(aCTBCRN);
//                                    } else {
//                                        // Create DRecord
//                                        DRecord = new DW_SHBE_Record(RecordID);
//                                        DRecord.SRecords = new HashSet<DW_SHBE_Record>();
//                                        DRecord.setCouncilTaxBenefitClaimReferenceNumber(aCTBCRN);
//                                    }
//                                    DRecord.SRecords.add(aSRecord);
                                }
                                String subRecordChildReferenceNumberOrNINO = aSRecord.getSubRecordChildReferenceNumberOrNINO();
                                if (subRecordChildReferenceNumberOrNINO.length() > 0) {
                                    if (aSRecord.getSubRecordType() == 2) {
                                        NonDependentsIDs.add(subRecordChildReferenceNumberOrNINO);
                                        AllHouseholdIDs.add(subRecordChildReferenceNumberOrNINO);
                                    } else {
                                        DependentsIDs.add(subRecordChildReferenceNumberOrNINO);
                                        AllHouseholdIDs.add(subRecordChildReferenceNumberOrNINO);
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
                                handler.nextID = DRecordID + 1L;

                                if (handler.nextID % handler._MaximumNumberPerCollection == 0) {
                                    collectionID++;
                                    handler.swapToFile_Collection(collection);
                                    collection = new DW_SHBE_Collection(collectionID, handler);
                                }

                                DW_SHBE_D_Record aDRecord;
                                aDRecord = new DW_SHBE_D_Record(
                                        RecordID, line, this);
                                DW_SHBE_Record rec;
                                rec = new DW_SHBE_Record(RecordID, aDRecord);

                                collection.addRecord(rec);

//                                Object o;
//                                o = DRecords.put(
//                                        aSHBE_DataRecord.getCouncilTaxBenefitClaimReferenceNumber(),
//                                        aSHBE_DataRecord);
//                                if (o != null) {
//                                    DW_SHBE_Record existingSHBE_DataRecord = (DW_SHBE_Record) o;
//                                    System.out.println("existingSHBE_DataRecord" + existingSHBE_DataRecord);
//                                    System.out.println("replacementSHBE_DataRecord" + aSHBE_DataRecord);
//                                }
                                // Count Council Tax and Housing Benefits and combined claims       
//                                if (!aDRecord.getCouncilTaxBenefitClaimReferenceNumber().trim().isEmpty()) {
//                                    totalCouncilTaxBenefitClaims++;
//                                    if (!aDRecord.getHousingBenefitClaimReferenceNumber().trim().isEmpty()) {
//                                        totalCouncilTaxAndHousingBenefitClaims++;
//                                        totalHousingBenefitClaims++;
//                                    }
//                                } else {
//                                    if (!aDRecord.getHousingBenefitClaimReferenceNumber().trim().isEmpty()) {
//                                        totalHousingBenefitClaims++;
//                                    }
//                                }
                                boolean isCurrentHBClaimInPayment;
                                isCurrentHBClaimInPayment = DW_SHBE_Handler.isCurrentHBClaimInPayment(aDRecord);
                                if (DW_SHBE_Handler.isCurrentHBClaimInPayment(aDRecord)) {
                                    totalCouncilTaxBenefitClaims++;
                                    if (isCurrentHBClaimInPayment) {
                                        totalCouncilTaxAndHousingBenefitClaims++;
                                    }
                                }
                                if (isCurrentHBClaimInPayment) {
                                    totalHousingBenefitClaims++;
                                }
                                // aSHBE_DataRecord.getNonDependantStatus 11
                                // aSHBE_DataRecord.getSubRecordType() 284
                                String claimantID = aDRecord.getClaimantsNationalInsuranceNumber();
                                if (claimantID.length() > 0) {
                                    ClaimantIDs.add(claimantID);
                                    AllHouseholdIDs.add(claimantID);
//                                // aSHBE_DataRecord.getPartnerFlag() 118
                                    if (aDRecord.getPartnerFlag() > 0) {
                                        String partnersNationalInsuranceNumber = aDRecord.getPartnersNationalInsuranceNumber();
                                        PartnerIDs.add(partnersNationalInsuranceNumber);
                                        AllHouseholdIDs.add(partnersNationalInsuranceNumber);
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println(line);
                                System.err.println("RecordID " + RecordID);
                                System.err.println(e.getLocalizedMessage());
                                recordIDsNotLoaded.add(RecordID);
                            }
                            countDRecords++;
                            DRecordID++;
                        }
                        RecordID++;
                        break;
                }
                tokenType = st.nextToken();
            }
            br.close();
            // Add all SRecords from recordsWithoutDRecords that actually do have 
            // DRecords, it just so happened that the DRecord was read after the 
            // first SRecord
            int countDRecordsInUnexpectedOrder = 0;

            Iterator<Long> collectionsIDIterator;
            collectionsIDIterator = handler.collections.keySet().iterator();
            while (collectionsIDIterator.hasNext()) {
                collectionID = collectionsIDIterator.next();
                collection = handler.getCollection(collectionID);

            }

            //Set<String> s = DRecords.keySet();
            Iterator<String> ite = tSRecordsWithoutDRecords.keySet().iterator();
            HashSet<String> rem = new HashSet<String>();
            while (ite.hasNext()) {
                String councilTaxBenefitClaimReferenceNumber = ite.next();
                if (handler.lookup.keySet().contains(councilTaxBenefitClaimReferenceNumber)) {
                    //if (s.contains(councilTaxBenefitClaimReferenceNumber)) {
                    //    DW_SHBE_Record DRecord = DRecords.get(councilTaxBenefitClaimReferenceNumber);
                    DRecordID = handler.lookup.get(councilTaxBenefitClaimReferenceNumber);
                    collectionID = handler.getCollection_ID(DRecordID);
                    collection = handler.getCollection(collectionID);
                    DW_SHBE_Record DRecord = collection.getRecord(DRecordID);
                    //DRecord.SRecords.addAll(DRecords.get(councilTaxBenefitClaimReferenceNumber).getSRecords());
                    DRecord.SRecords.add(tSRecordsWithoutDRecords.get(councilTaxBenefitClaimReferenceNumber));
                    rem.add(councilTaxBenefitClaimReferenceNumber);
                    countDRecordsInUnexpectedOrder++;
                }
            }
            //System.out.println("SRecords that came before DRecords count " + countDRecordsInUnexpectedOrder);
            ite = rem.iterator();
            while (ite.hasNext()) {
                tSRecordsWithoutDRecords.remove(ite.next());
            }
            // Summary report of load
            System.out.println("totalCouncilTaxBenefitClaims " + totalCouncilTaxBenefitClaims);
            System.out.println("totalCouncilTaxAndHousingBenefitClaims " + totalCouncilTaxAndHousingBenefitClaims);
            System.out.println("totalHousingBenefitClaims " + totalHousingBenefitClaims);
            System.out.println("countDRecords " + handler.getNextID(true));
            System.out.println("countSRecords " + countSRecords);
            System.out.println("countOfSRecordsWithoutDRecord " + tSRecordsWithoutDRecords.size());
            System.out.println("countDRecordsInUnexpectedOrder " + countDRecordsInUnexpectedOrder);
            System.out.println("recordIDsNotLoaded.size() " + recordIDsNotLoaded.size());
            System.out.println("Count of Unique ClaimantIDs " + ClaimantIDs.size());
            System.out.println("Count of Unique PartnerIDs " + PartnerIDs.size());
            System.out.println("Count of Unique DependentsIDs " + DependentsIDs.size());
            System.out.println("Count of Unique NonDependentsIDs " + NonDependentsIDs.size());
            System.out.println("Count of Unique AllHouseholdIDs " + AllHouseholdIDs.size());
        } catch (IOException ex) {
            Logger.getLogger(DW_SHBE_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    // Month_10_2010_11_381112_D_records.csv
    // 1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,308,309,310,311,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341
    // hb9803_SHBE_206728k\ April\ 2008.csv
    // 1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,307,308,309,310,311,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341
    // 1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,308,309,310,311,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341
    // 307, 315
    public static int readAndCheckFirstLine(
            File directory,
            String filename) {
        int type = 0;
        String type0Header = "1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,"
                + "23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,"
                + "43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,"
                + "63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,"
                + "83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,"
                + "102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,"
                + "117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,"
                + "135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,"
                + "150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,"
                + "165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,"
                + "180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,"
                + "195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,"
                + "210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,"
                + "226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,"
                + "241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,"
                + "256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,"
                + "271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,"
                + "293,294,295,296,297,298,299,308,309,310,311,316,317,318,319,"
                + "320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,"
                + "335,336,337,338,339,340,341";
        String type1Header = "1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,"
                + "23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,"
                + "43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,"
                + "63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,"
                + "83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,"
                + "102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,"
                + "117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,"
                + "135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,"
                + "150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,"
                + "165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,"
                + "180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,"
                + "195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,"
                + "210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,"
                + "226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,"
                + "241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,"
                + "256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,"
                + "271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,"
                + "293,294,295,296,297,298,299,307,308,309,310,311,315,316,317,"
                + "318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,"
                + "333,334,335,336,337,338,339,340,341";
        File inputFile = new File(
                directory,
                filename);
        try {
            BufferedReader br = Generic_StaticIO.getBufferedReader(inputFile);
            StreamTokenizer st
                    = new StreamTokenizer(br);
            Generic_StaticIO.setStreamTokenizerSyntax5(st);
            st.wordChars('`', '`');
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
            if (line.startsWith(type0Header)) {
                return 0;
            }
            if (line.startsWith(type1Header)) {
                return 1;
            } else {
                String[] lineSplit = line.split(",");
                System.err.println("Unrecognised header");
                System.out.println("Number of fields in header " + lineSplit.length);
                System.out.println("header:");
                System.out.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(DW_SHBE_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 2;
    }

    public static DW_SHBE_RecordAggregate aggregate(HashSet<DW_SHBE_Record> records) {
        DW_SHBE_RecordAggregate result = new DW_SHBE_RecordAggregate();
        Iterator<DW_SHBE_Record> ite = records.iterator();
        while (ite.hasNext()) {
            DW_SHBE_Record aSHBE_DataRecord = ite.next();
            aggregate(aSHBE_DataRecord, result);
        }
        return result;
    }

    public static void aggregate(
            DW_SHBE_Record record,
            DW_SHBE_RecordAggregate a_Aggregate_SHBE_DataRecord) {
        DW_SHBE_D_Record aDRecord;
        aDRecord = record.DRecord;
        a_Aggregate_SHBE_DataRecord.setTotalClaimCount(a_Aggregate_SHBE_DataRecord.getTotalClaimCount() + 1);
        if (aDRecord.getHousingBenefitClaimReferenceNumber().length() > 2) {
            a_Aggregate_SHBE_DataRecord.setTotalHBClaimCount(a_Aggregate_SHBE_DataRecord.getTotalHBClaimCount() + 1);
        } else {
            a_Aggregate_SHBE_DataRecord.setTotalCTBClaimCount(a_Aggregate_SHBE_DataRecord.getTotalCTBClaimCount() + 1);
        }
        if (aDRecord.getTenancyType() == 1) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType1Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType1Count() + 1);
        }
        if (aDRecord.getTenancyType() == 2) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType2Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType2Count() + 1);
        }
        if (aDRecord.getTenancyType() == 3) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType3Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType3Count() + 1);
        }
        if (aDRecord.getTenancyType() == 4) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType4Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType4Count() + 1);
        }
        if (aDRecord.getTenancyType() == 5) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType5Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType5Count() + 1);
        }
        if (aDRecord.getTenancyType() == 6) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType6Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType6Count() + 1);
        }
        if (aDRecord.getTenancyType() == 7) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType7Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType7Count() + 1);
        }
        if (aDRecord.getTenancyType() == 8) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType8Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType8Count() + 1);
        }
        if (aDRecord.getTenancyType() == 9) {
            a_Aggregate_SHBE_DataRecord.setTotalTenancyType9Count(a_Aggregate_SHBE_DataRecord.getTotalTenancyType9Count() + 1);
        }
        a_Aggregate_SHBE_DataRecord.setTotalNumberOfChildDependents(
                a_Aggregate_SHBE_DataRecord.getTotalNumberOfChildDependents()
                + aDRecord.getNumberOfChildDependents());
        a_Aggregate_SHBE_DataRecord.setTotalNumberOfNonDependents(
                a_Aggregate_SHBE_DataRecord.getTotalNumberOfNonDependents()
                + aDRecord.getNumberOfNonDependents());
        HashSet<DW_SHBE_S_Record> tSRecords;
        tSRecords = record.getSRecords();
        Iterator<DW_SHBE_S_Record> ite;
        ite = tSRecords.iterator();
        while (ite.hasNext()) {
            DW_SHBE_S_Record aSRecord = ite.next();
            if (aSRecord.getNonDependentStatus() == 0) {
                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus0(
                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus0() + 1);
            }
            if (aSRecord.getNonDependentStatus() == 1) {
                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus1(
                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus1() + 1);
            }
            if (aSRecord.getNonDependentStatus() == 2) {
                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus2(
                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus2() + 1);
            }
            if (aSRecord.getNonDependentStatus() == 3) {
                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus3(
                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus3() + 1);
            }
            if (aSRecord.getNonDependentStatus() == 4) {
                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus4(
                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus4() + 1);
            }
            if (aSRecord.getNonDependentStatus() == 5) {
                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus5(
                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus5() + 1);
            }
            if (aSRecord.getNonDependentStatus() == 6) {
                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus6(
                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus6() + 1);
            }
            if (aSRecord.getNonDependentStatus() == 7) {
                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus7(
                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus7() + 1);
            }
            if (aSRecord.getNonDependentStatus() == 8) {
                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus8(
                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus8() + 1);
            }
            a_Aggregate_SHBE_DataRecord.setTotalNonDependantGrossWeeklyIncomeFromRemunerativeWork(
                    a_Aggregate_SHBE_DataRecord.getTotalNonDependantGrossWeeklyIncomeFromRemunerativeWork()
                    + aSRecord.getNonDependantGrossWeeklyIncomeFromRemunerativeWork());
        }
        if (aDRecord.getStatusOfHBClaimAtExtractDate() == 0) {
            a_Aggregate_SHBE_DataRecord.setTotalStatusOfHBClaimAtExtractDate0(a_Aggregate_SHBE_DataRecord.getTotalStatusOfHBClaimAtExtractDate0() + 1);
        }
        if (aDRecord.getStatusOfHBClaimAtExtractDate() == 1) {
            a_Aggregate_SHBE_DataRecord.setTotalStatusOfHBClaimAtExtractDate1(a_Aggregate_SHBE_DataRecord.getTotalStatusOfHBClaimAtExtractDate1() + 1);
        }
        if (aDRecord.getStatusOfHBClaimAtExtractDate() == 2) {
            a_Aggregate_SHBE_DataRecord.setTotalStatusOfHBClaimAtExtractDate2(a_Aggregate_SHBE_DataRecord.getTotalStatusOfHBClaimAtExtractDate2() + 1);
        }
        if (aDRecord.getStatusOfCTBClaimAtExtractDate() == 0) {
            a_Aggregate_SHBE_DataRecord.setTotalStatusOfCTBClaimAtExtractDate0(a_Aggregate_SHBE_DataRecord.getTotalStatusOfCTBClaimAtExtractDate0() + 1);
        }
        if (aDRecord.getStatusOfCTBClaimAtExtractDate() == 1) {
            a_Aggregate_SHBE_DataRecord.setTotalStatusOfCTBClaimAtExtractDate1(a_Aggregate_SHBE_DataRecord.getTotalStatusOfCTBClaimAtExtractDate1() + 1);
        }
        if (aDRecord.getStatusOfCTBClaimAtExtractDate() == 2) {
            a_Aggregate_SHBE_DataRecord.setTotalStatusOfCTBClaimAtExtractDate2(a_Aggregate_SHBE_DataRecord.getTotalStatusOfCTBClaimAtExtractDate2() + 1);
        }
//        if (aDRecord.getOutcomeOfFirstDecisionOnMostRecentHBClaim() == 1) {
//            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentHBClaim1(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentHBClaim1() + 1);
//        }
//        if (aDRecord.getOutcomeOfFirstDecisionOnMostRecentHBClaim() == 2) {
//            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentHBClaim2(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentHBClaim2() + 1);
//        }
//        if (aDRecord.getOutcomeOfFirstDecisionOnMostRecentHBClaim() == 3) {
//            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentHBClaim3(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentHBClaim3() + 1);
//        }
//        if (aDRecord.getOutcomeOfFirstDecisionOnMostRecentHBClaim() == 4) {
//            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentHBClaim4(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentHBClaim4() + 1);
//        }
//        if (aDRecord.getOutcomeOfFirstDecisionOnMostRecentHBClaim() == 5) {
//            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentHBClaim5(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentHBClaim5() + 1);
//        }
//        if (aDRecord.getOutcomeOfFirstDecisionOnMostRecentHBClaim() == 6) {
//            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentHBClaim6(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentHBClaim6() + 1);
//        }
//        if (aDRecord.getOutcomeOfFirstDecisionOnMostRecentCTBClaim() == 1) {
//            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim1(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim1() + 1);
//        }
//        if (aDRecord.getOutcomeOfFirstDecisionOnMostRecentCTBClaim() == 2) {
//            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim2(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim2() + 1);
//        }
//        if (aDRecord.getOutcomeOfFirstDecisionOnMostRecentCTBClaim() == 3) {
//            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim3(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim3() + 1);
//        }
//        if (aDRecord.getOutcomeOfFirstDecisionOnMostRecentCTBClaim() == 4) {
//            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim4(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim4() + 1);
//        }
//        if (aDRecord.getOutcomeOfFirstDecisionOnMostRecentCTBClaim() == 5) {
//            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim5(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim5() + 1);
//        }
//        if (aDRecord.getOutcomeOfFirstDecisionOnMostRecentCTBClaim() == 6) {
//            a_Aggregate_SHBE_DataRecord.setTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim6(a_Aggregate_SHBE_DataRecord.getTotalOutcomeOfFirstDecisionOnMostRecentCTBClaim6() + 1);
//        }
        a_Aggregate_SHBE_DataRecord.setTotalWeeklyHousingBenefitEntitlement(
                a_Aggregate_SHBE_DataRecord.getTotalWeeklyHousingBenefitEntitlement()
                + aDRecord.getWeeklyHousingBenefitEntitlement());
        a_Aggregate_SHBE_DataRecord.setTotalWeeklyCouncilTaxBenefitEntitlement(
                a_Aggregate_SHBE_DataRecord.getTotalWeeklyCouncilTaxBenefitEntitlement()
                + aDRecord.getWeeklyCouncilTaxBenefitEntitlement());
        if (aDRecord.getLHARegulationsApplied().equalsIgnoreCase("NO")) { // A guess at the values: check!
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
                + aDRecord.getWeeklyMaximumRent());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsAssessedIncomeFigure(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsAssessedIncomeFigure()
                + aDRecord.getClaimantsAssessedIncomeFigure());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsAdjustedAssessedIncomeFigure(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsAdjustedAssessedIncomeFigure()
                + aDRecord.getClaimantsAdjustedAssessedIncomeFigure());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsTotalCapital(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsTotalCapital()
                + aDRecord.getClaimantsTotalCapital());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsGrossWeeklyIncomeFromEmployment(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsGrossWeeklyIncomeFromEmployment()
                + aDRecord.getClaimantsGrossWeeklyIncomeFromEmployment());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsNetWeeklyIncomeFromEmployment(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsNetWeeklyIncomeFromEmployment()
                + aDRecord.getClaimantsNetWeeklyIncomeFromEmployment());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsGrossWeeklyIncomeFromSelfEmployment(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsGrossWeeklyIncomeFromSelfEmployment()
                + aDRecord.getClaimantsGrossWeeklyIncomeFromSelfEmployment());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsNetWeeklyIncomeFromSelfEmployment(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsNetWeeklyIncomeFromSelfEmployment()
                + aDRecord.getClaimantsNetWeeklyIncomeFromSelfEmployment());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsTotalAmountOfEarningsDisregarded(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsTotalAmountOfEarningsDisregarded()
                + aDRecord.getClaimantsTotalAmountOfEarningsDisregarded());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded()
                + aDRecord.getClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromAttendanceAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromAttendanceAllowance()
                + aDRecord.getClaimantsIncomeFromAttendanceAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromAttendanceAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromAttendanceAllowance()
                + aDRecord.getClaimantsIncomeFromAttendanceAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromBusinessStartUpAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromBusinessStartUpAllowance()
                + aDRecord.getClaimantsIncomeFromBusinessStartUpAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromChildBenefit(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromChildBenefit()
                + aDRecord.getClaimantsIncomeFromChildBenefit());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent()
                + aDRecord.getClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromPersonalPension(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromPersonalPension()
                + aDRecord.getClaimantsIncomeFromPersonalPension());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromSevereDisabilityAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromSevereDisabilityAllowance()
                + aDRecord.getClaimantsIncomeFromSevereDisabilityAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromMaternityAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromMaternityAllowance()
                + aDRecord.getClaimantsIncomeFromMaternityAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromContributionBasedJobSeekersAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromContributionBasedJobSeekersAllowance()
                + aDRecord.getClaimantsIncomeFromContributionBasedJobSeekersAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromStudentGrantLoan(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromStudentGrantLoan()
                + aDRecord.getClaimantsIncomeFromStudentGrantLoan());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromStudentGrantLoan(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromStudentGrantLoan()
                + aDRecord.getClaimantsIncomeFromStudentGrantLoan());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromSubTenants(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromSubTenants()
                + aDRecord.getClaimantsIncomeFromSubTenants());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromBoarders(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromBoarders()
                + aDRecord.getClaimantsIncomeFromBoarders());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromTrainingForWorkCommunityAction(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromTrainingForWorkCommunityAction()
                + aDRecord.getClaimantsIncomeFromTrainingForWorkCommunityAction());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromIncapacityBenefitShortTermLower(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromIncapacityBenefitShortTermLower()
                + aDRecord.getClaimantsIncomeFromIncapacityBenefitShortTermLower());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromIncapacityBenefitShortTermHigher(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromIncapacityBenefitShortTermHigher()
                + aDRecord.getClaimantsIncomeFromIncapacityBenefitShortTermHigher());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromIncapacityBenefitLongTerm(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromIncapacityBenefitLongTerm()
                + aDRecord.getClaimantsIncomeFromIncapacityBenefitLongTerm());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromNewDeal50PlusEmploymentCredit(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromNewDeal50PlusEmploymentCredit()
                + aDRecord.getClaimantsIncomeFromNewDeal50PlusEmploymentCredit());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromNewTaxCredits(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromNewTaxCredits()
                + aDRecord.getClaimantsIncomeFromNewTaxCredits());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromDisabilityLivingAllowanceCareComponent(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromDisabilityLivingAllowanceCareComponent()
                + aDRecord.getClaimantsIncomeFromDisabilityLivingAllowanceCareComponent());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent()
                + aDRecord.getClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromGovernemntTraining(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromGovernemntTraining()
                + aDRecord.getClaimantsIncomeFromGovernmentTraining());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromIndustrialInjuriesDisablementBenefit(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromIndustrialInjuriesDisablementBenefit()
                + aDRecord.getClaimantsIncomeFromIndustrialInjuriesDisablementBenefit());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromCarersAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromCarersAllowance()
                + aDRecord.getClaimantsIncomeFromCarersAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromStatutoryMaternityPaternityPay(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromStatutoryMaternityPaternityPay()
                + aDRecord.getClaimantsIncomeFromStatutoryMaternityPaternityPay());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc()
                + aDRecord.getClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromWarDisablementPensionArmedForcesGIP(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromWarDisablementPensionArmedForcesGIP()
                + aDRecord.getClaimantsIncomeFromWarDisablementPensionArmedForcesGIP());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromWarMobilitySupplement(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromWarMobilitySupplement()
                + aDRecord.getClaimantsIncomeFromWarMobilitySupplement());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromWidowsWidowersPension(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromWidowsWidowersPension()
                + aDRecord.getClaimantsIncomeFromWarWidowsWidowersPension());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromBereavementAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromBereavementAllowance()
                + aDRecord.getClaimantsIncomeFromBereavementAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromWidowedParentsAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromWidowedParentsAllowance()
                + aDRecord.getClaimantsIncomeFromWidowedParentsAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromYouthTrainingScheme(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromYouthTrainingScheme()
                + aDRecord.getClaimantsIncomeFromYouthTrainingScheme());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromStatuatorySickPay(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromStatuatorySickPay()
                + aDRecord.getClaimantsIncomeFromStatutorySickPay());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsOtherIncome(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsOtherIncome()
                + aDRecord.getClaimantsOtherIncome());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsTotalAmountOfIncomeDisregarded(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsTotalAmountOfIncomeDisregarded()
                + aDRecord.getClaimantsTotalAmountOfIncomeDisregarded());
        a_Aggregate_SHBE_DataRecord.setTotalFamilyPremiumAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalFamilyPremiumAwarded()
                + aDRecord.getFamilyPremiumAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalFamilyLoneParentPremiumAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalFamilyLoneParentPremiumAwarded()
                + aDRecord.getFamilyLoneParentPremiumAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalDisabilityPremiumAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalDisabilityPremiumAwarded()
                + aDRecord.getDisabilityPremiumAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalSevereDisabilityPremiumAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalSevereDisabilityPremiumAwarded()
                + aDRecord.getSevereDisabilityPremiumAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalDisabledChildPremiumAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalDisabledChildPremiumAwarded()
                + aDRecord.getDisabledChildPremiumAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalCarePremiumAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalCarePremiumAwarded()
                + aDRecord.getCarePremiumAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalEnhancedDisabilityPremiumAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalEnhancedDisabilityPremiumAwarded()
                + aDRecord.getEnhancedDisabilityPremiumAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalBereavementPremiumAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalBereavementPremiumAwarded()
                + aDRecord.getBereavementPremiumAwarded());
        if (aDRecord.getPartnersStudentIndicator().equalsIgnoreCase("Y")) {
            a_Aggregate_SHBE_DataRecord.setTotalPartnersStudentIndicator(
                    a_Aggregate_SHBE_DataRecord.getTotalPartnersStudentIndicator() + 1);
        }
        a_Aggregate_SHBE_DataRecord.setTotalPartnersAssessedIncomeFigure(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersAssessedIncomeFigure()
                + aDRecord.getPartnersAssessedIncomeFigure());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersAdjustedAssessedIncomeFigure(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersAdjustedAssessedIncomeFigure()
                + aDRecord.getPartnersAdjustedAssessedIncomeFigure());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersGrossWeeklyIncomeFromEmployment(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersGrossWeeklyIncomeFromEmployment()
                + aDRecord.getPartnersGrossWeeklyIncomeFromEmployment());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersNetWeeklyIncomeFromEmployment(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersNetWeeklyIncomeFromEmployment()
                + aDRecord.getPartnersNetWeeklyIncomeFromEmployment());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersGrossWeeklyIncomeFromSelfEmployment(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersGrossWeeklyIncomeFromSelfEmployment()
                + aDRecord.getPartnersGrossWeeklyIncomeFromSelfEmployment());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersNetWeeklyIncomeFromSelfEmployment(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersNetWeeklyIncomeFromSelfEmployment()
                + aDRecord.getPartnersNetWeeklyIncomeFromSelfEmployment());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersTotalAmountOfEarningsDisregarded(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersTotalAmountOfEarningsDisregarded()
                + aDRecord.getPartnersTotalAmountOfEarningsDisregarded());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded()
                + aDRecord.getPartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromAttendanceAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromAttendanceAllowance()
                + aDRecord.getPartnersIncomeFromAttendanceAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromBusinessStartUpAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromBusinessStartUpAllowance()
                + aDRecord.getPartnersIncomeFromBusinessStartUpAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromChildBenefit(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromChildBenefit()
                + aDRecord.getPartnersIncomeFromChildBenefit());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromPersonalPension(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromPersonalPension()
                + aDRecord.getPartnersIncomeFromPersonalPension());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromSevereDisabilityAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromSevereDisabilityAllowance()
                + aDRecord.getPartnersIncomeFromSevereDisabilityAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromMaternityAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromMaternityAllowance()
                + aDRecord.getPartnersIncomeFromMaternityAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromContributionBasedJobSeekersAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromContributionBasedJobSeekersAllowance()
                + aDRecord.getPartnersIncomeFromContributionBasedJobSeekersAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromStudentGrantLoan(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromStudentGrantLoan()
                + aDRecord.getPartnersIncomeFromStudentGrantLoan());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromSubTenants(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromSubTenants()
                + aDRecord.getPartnersIncomeFromSubTenants());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromBoarders(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromBoarders()
                + aDRecord.getPartnersIncomeFromBoarders());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromTrainingForWorkCommunityAction(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromTrainingForWorkCommunityAction()
                + aDRecord.getPartnersIncomeFromTrainingForWorkCommunityAction());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromIncapacityBenefitShortTermLower(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromIncapacityBenefitShortTermLower()
                + aDRecord.getPartnersIncomeFromIncapacityBenefitShortTermLower());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromIncapacityBenefitShortTermHigher(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromIncapacityBenefitShortTermHigher()
                + aDRecord.getPartnersIncomeFromIncapacityBenefitShortTermHigher());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromIncapacityBenefitLongTerm(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromIncapacityBenefitLongTerm()
                + aDRecord.getPartnersIncomeFromIncapacityBenefitLongTerm());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromNewDeal50PlusEmploymentCredit(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromNewDeal50PlusEmploymentCredit()
                + aDRecord.getPartnersIncomeFromNewDeal50PlusEmploymentCredit());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromNewTaxCredits(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromNewTaxCredits()
                + aDRecord.getPartnersIncomeFromNewTaxCredits());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromDisabilityLivingAllowanceCareComponent(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromDisabilityLivingAllowanceCareComponent()
                + aDRecord.getPartnersIncomeFromDisabilityLivingAllowanceCareComponent());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromDisabilityLivingAllowanceMobilityComponent(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromDisabilityLivingAllowanceMobilityComponent()
                + aDRecord.getPartnersIncomeFromDisabilityLivingAllowanceMobilityComponent());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromGovernemntTraining(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromGovernemntTraining()
                + aDRecord.getPartnersIncomeFromGovernmentTraining());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromIndustrialInjuriesDisablementBenefit(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromIndustrialInjuriesDisablementBenefit()
                + aDRecord.getPartnersIncomeFromIndustrialInjuriesDisablementBenefit());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromCarersAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromCarersAllowance()
                + aDRecord.getPartnersIncomeFromCarersAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromStatuatorySickPay(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromStatuatorySickPay()
                + aDRecord.getPartnersIncomeFromStatutorySickPay());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromStatutoryMaternityPaternityPay(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromStatutoryMaternityPaternityPay()
                + aDRecord.getPartnersIncomeFromStatutoryMaternityPaternityPay());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc()
                + aDRecord.getPartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromWarDisablementPensionArmedForcesGIP(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromWarDisablementPensionArmedForcesGIP()
                + aDRecord.getPartnersIncomeFromWarDisablementPensionArmedForcesGIP());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromWarMobilitySupplement(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromWarMobilitySupplement()
                + aDRecord.getPartnersIncomeFromWarMobilitySupplement());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromWidowsWidowersPension(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromWidowsWidowersPension()
                + aDRecord.getPartnersIncomeFromWarWidowsWidowersPension());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromBereavementAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromBereavementAllowance()
                + aDRecord.getPartnersIncomeFromBereavementAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromWidowedParentsAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromWidowedParentsAllowance()
                + aDRecord.getPartnersIncomeFromWidowedParentsAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromYouthTrainingScheme(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromYouthTrainingScheme()
                + aDRecord.getPartnersIncomeFromYouthTrainingScheme());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersOtherIncome(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersOtherIncome()
                + aDRecord.getPartnersOtherIncome());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersTotalAmountOfIncomeDisregarded(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersTotalAmountOfIncomeDisregarded()
                + aDRecord.getPartnersTotalAmountOfIncomeDisregarded());
        if (aDRecord.getClaimantsGender().equalsIgnoreCase("F")) {
            a_Aggregate_SHBE_DataRecord.setTotalClaimantsGenderFemale(
                    a_Aggregate_SHBE_DataRecord.getTotalClaimantsGenderFemale() + 1);
        }
        if (aDRecord.getClaimantsGender().equalsIgnoreCase("M")) {
            a_Aggregate_SHBE_DataRecord.setTotalClaimantsGenderMale(
                    a_Aggregate_SHBE_DataRecord.getTotalClaimantsGenderMale() + 1);
        }
        a_Aggregate_SHBE_DataRecord.setTotalContractualRentAmount(
                a_Aggregate_SHBE_DataRecord.getTotalContractualRentAmount()
                + aDRecord.getContractualRentAmount());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromPensionCreditSavingsCredit(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromPensionCreditSavingsCredit()
                + aDRecord.getClaimantsIncomeFromPensionCreditSavingsCredit());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromPensionCreditSavingsCredit(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromPensionCreditSavingsCredit()
                + aDRecord.getPartnersIncomeFromPensionCreditSavingsCredit());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromMaintenancePayments(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromMaintenancePayments()
                + aDRecord.getClaimantsIncomeFromMaintenancePayments());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromMaintenancePayments(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromMaintenancePayments()
                + aDRecord.getPartnersIncomeFromMaintenancePayments());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromOccupationalPension(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromOccupationalPension()
                + aDRecord.getClaimantsIncomeFromOccupationalPension());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromOccupationalPension(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromOccupationalPension()
                + aDRecord.getPartnersIncomeFromOccupationalPension());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsIncomeFromWidowsBenefit(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsIncomeFromWidowsBenefit()
                + aDRecord.getClaimantsIncomeFromWidowsBenefit());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromWidowsBenefit(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromWidowsBenefit()
                + aDRecord.getPartnersIncomeFromWidowsBenefit());
        a_Aggregate_SHBE_DataRecord.setTotalTotalNumberOfRooms(
                a_Aggregate_SHBE_DataRecord.getTotalTotalNumberOfRooms()
                + aDRecord.getTotalNumberOfRooms());
        a_Aggregate_SHBE_DataRecord.setTotalValueOfLHA(
                a_Aggregate_SHBE_DataRecord.getTotalValueOfLHA()
                + aDRecord.getValueOfLHA());
        if (aDRecord.getPartnersGender().equalsIgnoreCase("F")) {
            a_Aggregate_SHBE_DataRecord.setTotalPartnersGenderFemale(
                    a_Aggregate_SHBE_DataRecord.getTotalPartnersGenderFemale() + 1);
        }
        if (aDRecord.getPartnersGender().equalsIgnoreCase("M")) {
            a_Aggregate_SHBE_DataRecord.setTotalPartnersGenderMale(
                    a_Aggregate_SHBE_DataRecord.getTotalPartnersGenderMale() + 1);
        }
        a_Aggregate_SHBE_DataRecord.setTotalTotalAmountOfBackdatedHBAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalTotalAmountOfBackdatedHBAwarded()
                + aDRecord.getTotalAmountOfBackdatedHBAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalTotalAmountOfBackdatedCTBAwarded(
                a_Aggregate_SHBE_DataRecord.getTotalTotalAmountOfBackdatedCTBAwarded()
                + aDRecord.getTotalAmountOfBackdatedCTBAwarded());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersTotalCapital(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersTotalCapital()
                + aDRecord.getPartnersTotalCapital());
        a_Aggregate_SHBE_DataRecord.setTotalWeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure(
                a_Aggregate_SHBE_DataRecord.getTotalWeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure()
                + aDRecord.getWeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure());
        a_Aggregate_SHBE_DataRecord.setTotalClaimantsTotalHoursOfRemunerativeWorkPerWeek(
                a_Aggregate_SHBE_DataRecord.getTotalClaimantsTotalHoursOfRemunerativeWorkPerWeek()
                + aDRecord.getClaimantsTotalHoursOfRemunerativeWorkPerWeek());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersTotalHoursOfRemunerativeWorkPerWeek(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersTotalHoursOfRemunerativeWorkPerWeek()
                + aDRecord.getPartnersTotalHoursOfRemunerativeWorkPerWeek());
    }

    public static long getHouseholdSize(DW_SHBE_Record rec) {
        long result;
        result = 1;
        DW_SHBE_D_Record D_Record;
        D_Record = rec.DRecord;
        result += D_Record.getPartnerFlag();
        result += D_Record.getNumberOfChildDependents();
        long NumberOfNonDependents;
        NumberOfNonDependents = D_Record.getNumberOfNonDependents();
        result += NumberOfNonDependents;
        HashSet<DW_SHBE_S_Record> S_Records;
        S_Records = rec.SRecords;
        long NumberOfS_Records;
        NumberOfS_Records = S_Records.size();
        if (NumberOfS_Records != NumberOfNonDependents) {
            System.out.println("NumberOfS_Records != NumberOfNonDependents for rec " + rec.toString());
            Iterator<DW_SHBE_S_Record> ite;
            ite = S_Records.iterator();
            while (ite.hasNext()) {
                DW_SHBE_S_Record S_Record;
                S_Record = ite.next();
            }
        }
        return result;
    }

    public static long getHouseholdSize(DW_SHBE_D_Record D_Record) {
        long result;
        result = 1;
        result += D_Record.getPartnerFlag();
        result += D_Record.getNumberOfChildDependents();
        long NumberOfNonDependents;
        NumberOfNonDependents = D_Record.getNumberOfNonDependents();
        result += NumberOfNonDependents;
        return result;
    }

    public static long getClaimantsIncomeFromBenefitsAndAllowances(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getClaimantsIncomeFromAttendanceAllowance();
        result += aDRecord.getClaimantsIncomeFromBereavementAllowance();
        result += aDRecord.getClaimantsIncomeFromBusinessStartUpAllowance();
        result += aDRecord.getClaimantsIncomeFromCarersAllowance();
        result += aDRecord.getClaimantsIncomeFromChildBenefit();
        result += aDRecord.getClaimantsIncomeFromContributionBasedJobSeekersAllowance();
        result += aDRecord.getClaimantsIncomeFromDisabilityLivingAllowanceCareComponent();
        result += aDRecord.getClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent();
        result += aDRecord.getClaimantsIncomeFromIncapacityBenefitLongTerm();
        result += aDRecord.getClaimantsIncomeFromIncapacityBenefitShortTermHigher();
        result += aDRecord.getClaimantsIncomeFromIncapacityBenefitShortTermLower();
        result += aDRecord.getClaimantsIncomeFromIndustrialInjuriesDisablementBenefit();
        result += aDRecord.getClaimantsIncomeFromMaternityAllowance();
        result += aDRecord.getClaimantsIncomeFromNewDeal50PlusEmploymentCredit();
        result += aDRecord.getClaimantsIncomeFromNewTaxCredits();
        result += aDRecord.getClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent();
        result += aDRecord.getClaimantsIncomeFromPensionCreditSavingsCredit();
        result += aDRecord.getClaimantsIncomeFromSevereDisabilityAllowance();
        result += aDRecord.getClaimantsIncomeFromStatutoryMaternityPaternityPay();
        result += aDRecord.getClaimantsIncomeFromStatutorySickPay();
        result += aDRecord.getClaimantsIncomeFromWarMobilitySupplement();
        result += aDRecord.getClaimantsIncomeFromWidowedParentsAllowance();
        result += aDRecord.getClaimantsIncomeFromWidowsBenefit();
        return result;
    }

    public static long getClaimantsIncomeFromEmployment(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getClaimantsGrossWeeklyIncomeFromEmployment();
        result += aDRecord.getClaimantsGrossWeeklyIncomeFromSelfEmployment();
        return result;
    }

    public static long getClaimantsIncomeFromGovernmentTraining(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getClaimantsIncomeFromGovernmentTraining();
        result += aDRecord.getClaimantsIncomeFromTrainingForWorkCommunityAction();
        result += aDRecord.getClaimantsIncomeFromYouthTrainingScheme();
        return result;
    }

    public static long getClaimantsIncomeFromPensionPrivate(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getClaimantsIncomeFromOccupationalPension();
        result += aDRecord.getClaimantsIncomeFromPersonalPension();
        return result;
    }

    public static long getClaimantsIncomeFromPensionState(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc();
        result += aDRecord.getClaimantsIncomeFromWarDisablementPensionArmedForcesGIP();
        result += aDRecord.getClaimantsIncomeFromWarWidowsWidowersPension();
        return result;
    }

    public static long getClaimantsIncomeFromBoardersAndSubTenants(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getClaimantsIncomeFromSubTenants();
        result += aDRecord.getClaimantsIncomeFromBoarders();
        return result;
    }

    public static long getClaimantsIncomeFromOther(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getClaimantsIncomeFromMaintenancePayments();
        result += aDRecord.getClaimantsIncomeFromStudentGrantLoan();
        result += aDRecord.getClaimantsOtherIncome();
        return result;
    }

    public static long getClaimantsIncomeTotal(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += getClaimantsIncomeFromBenefitsAndAllowances(aDRecord);
        result += getClaimantsIncomeFromEmployment(aDRecord);
        result += getClaimantsIncomeFromGovernmentTraining(aDRecord);
        result += getClaimantsIncomeFromPensionPrivate(aDRecord);
        result += getClaimantsIncomeFromPensionState(aDRecord);
        result += getClaimantsIncomeFromBoardersAndSubTenants(aDRecord);
        result += getClaimantsIncomeFromOther(aDRecord);
        return result;
    }

    public static long getPartnersIncomeFromBenefitsAndAllowances(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getPartnersIncomeFromAttendanceAllowance();
        result += aDRecord.getPartnersIncomeFromBereavementAllowance();
        result += aDRecord.getPartnersIncomeFromBusinessStartUpAllowance();
        result += aDRecord.getPartnersIncomeFromCarersAllowance();
        result += aDRecord.getPartnersIncomeFromChildBenefit();
        result += aDRecord.getPartnersIncomeFromContributionBasedJobSeekersAllowance();
        result += aDRecord.getPartnersIncomeFromDisabilityLivingAllowanceCareComponent();
        result += aDRecord.getPartnersIncomeFromDisabilityLivingAllowanceMobilityComponent();
        result += aDRecord.getPartnersIncomeFromIncapacityBenefitLongTerm();
        result += aDRecord.getPartnersIncomeFromIncapacityBenefitShortTermHigher();
        result += aDRecord.getPartnersIncomeFromIncapacityBenefitShortTermLower();
        result += aDRecord.getPartnersIncomeFromIndustrialInjuriesDisablementBenefit();
        result += aDRecord.getPartnersIncomeFromMaternityAllowance();
        result += aDRecord.getPartnersIncomeFromNewDeal50PlusEmploymentCredit();
        result += aDRecord.getPartnersIncomeFromNewTaxCredits();
        result += aDRecord.getPartnersIncomeFromPensionCreditSavingsCredit();
        result += aDRecord.getPartnersIncomeFromSevereDisabilityAllowance();
        result += aDRecord.getPartnersIncomeFromStatutoryMaternityPaternityPay();
        result += aDRecord.getPartnersIncomeFromStatutorySickPay();
        result += aDRecord.getPartnersIncomeFromWarMobilitySupplement();
        result += aDRecord.getPartnersIncomeFromWidowedParentsAllowance();
        result += aDRecord.getPartnersIncomeFromWidowsBenefit();
        return result;
    }

    public static long getPartnersIncomeFromEmployment(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getPartnersGrossWeeklyIncomeFromEmployment();
        result += aDRecord.getPartnersGrossWeeklyIncomeFromSelfEmployment();
        return result;
    }

    public static long getPartnersIncomeFromGovernmentTraining(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getPartnersIncomeFromGovernmentTraining();
        result += aDRecord.getPartnersIncomeFromTrainingForWorkCommunityAction();
        result += aDRecord.getPartnersIncomeFromYouthTrainingScheme();
        return result;
    }

    public static long getPartnersIncomeFromPensionPrivate(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getPartnersIncomeFromOccupationalPension();
        result += aDRecord.getPartnersIncomeFromPersonalPension();
        return result;
    }

    public static long getPartnersIncomeFromPensionState(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getPartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc();
        result += aDRecord.getPartnersIncomeFromWarDisablementPensionArmedForcesGIP();
        result += aDRecord.getPartnersIncomeFromWarWidowsWidowersPension();
        return result;
    }

    public static long getPartnersIncomeFromBoardersAndSubTenants(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getPartnersIncomeFromSubTenants();
        result += aDRecord.getPartnersIncomeFromBoarders();
        return result;
    }

    public static long getPartnersIncomeFromOther(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getPartnersIncomeFromMaintenancePayments();
        result += aDRecord.getPartnersIncomeFromStudentGrantLoan();
        result += aDRecord.getPartnersOtherIncome();
        return result;
    }

    public static long getPartnersIncomeTotal(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += getPartnersIncomeFromBenefitsAndAllowances(aDRecord);
        result += getPartnersIncomeFromEmployment(aDRecord);
        result += getPartnersIncomeFromGovernmentTraining(aDRecord);
        result += getPartnersIncomeFromPensionPrivate(aDRecord);
        result += getPartnersIncomeFromPensionState(aDRecord);
        result += getPartnersIncomeFromBoardersAndSubTenants(aDRecord);
        result += getPartnersIncomeFromOther(aDRecord);
        return result;
    }

    public static long getClaimantsAndPartnersIncomeTotal(
            DW_SHBE_D_Record aDRecord) {
        long result = getClaimantsIncomeTotal(aDRecord) + getPartnersIncomeTotal(aDRecord);
        return result;
    }

    public static boolean getUnderOccupancy(
            DW_SHBE_D_Record aDRecord) {
        int numberOfBedroomsForLHARolloutCasesOnly = aDRecord.getNumberOfBedroomsForLHARolloutCasesOnly();
        if (numberOfBedroomsForLHARolloutCasesOnly > 0) {
            if (numberOfBedroomsForLHARolloutCasesOnly
                    > aDRecord.getNumberOfChildDependents()
                    + aDRecord.getNumberOfNonDependents()) {
                return true;
            }
        }
        return false;
    }

    public static int getUnderOccupancyAmount(
            DW_SHBE_D_Record aDRecord) {
        int result = 0;
        int numberOfBedroomsForLHARolloutCasesOnly = aDRecord.getNumberOfBedroomsForLHARolloutCasesOnly();
        if (numberOfBedroomsForLHARolloutCasesOnly > 0) {
            result = numberOfBedroomsForLHARolloutCasesOnly
                    - aDRecord.getNumberOfChildDependents()
                    - aDRecord.getNumberOfNonDependents();
        }
        return result;
    }

    /**
     * Method for getting SHBE collections filenames in an array
     *
     * @return String[] result of SHBE collections filenames where--------------
     * result[0] = "hb9803_SHBE_206728k April 2008.csv";------------------------
     * result[1] = "hb9803_SHBE_234696k October 2008.csv";----------------------
     * result[2] = "hb9803_SHBE_265149k April 2009.csv";------------------------
     * result[3] = "hb9803_SHBE_295723k October 2009.csv";----------------------
     * result[4] = "hb9803_SHBE_329509k April 2010.csv";------------------------
     * result[5] = "hb9803_SHBE_363186k October 2010.csv";----------------------
     * result[6] = "hb9803_SHBE_391746k March 2011.csv";------------------------
     * result[7] = "hb9803_SHBE_397524k April 2011.csv";------------------------
     * result[8] = "hb9803_SHBE_415181k July 2011.csv";-------------------------
     * result[9] = "hb9803_SHBE_433970k October 2011.csv";----------------------
     * result[11] = "hb9803_SHBE_470742k April 2012.csv";-----------------------
     * result[12] = "hb9803_SHBE_490903k July 2012.csv";------------------------
     * result[13] = "hb9803_SHBE_511038k October 2012.csv";---------------------
     * result[14] = "hb9803_SHBE_530243k January 2013.csv";---------------------
     * result[15] = "hb9803_SHBE_536123k February 2013.csv";--------------------
     * result[16] = "hb9991_SHBE_543169k March 2013.csv";-----------------------
     * result[17] = "hb9991_SHBE_549416k April 2013.csv";-----------------------
     * result[18] = "hb9991_SHBE_555086k May 2013.csv";-------------------------
     * result[19] = "hb9991_SHBE_562036k June 2013.csv";------------------------
     * result[20] = "hb9991_SHBE_568694k July 2013.csv";------------------------
     * result[21] = "hb9991_SHBE_576432k August 2013.csv";----------------------
     * result[22] = "hb9991_SHBE_582832k September 2013.csv";-------------------
     * result[23] = "hb9991_SHBE_589664k Oct 2013.csv";-------------------------
     * result[24] = "hb9991_SHBE_596500k Nov 2013.csv";-------------------------
     * result[25] = "hb9991_SHBE_603335k Dec 2013.csv";-------------------------
     * result[26] = "hb9991_SHBE_609791k Jan 2014.csv";-------------------------
     * result[27] = "hb9991_SHBE_615103k Feb 2014.csv";-------------------------
     * result[28] = "hb9991_SHBE_621666k Mar 2014.csv";-------------------------
     * result[29] = "hb9991_SHBE_629066k Apr 2014.csv";-------------------------
     * result[30] = "hb9991_SHBE_635115k May 2014.csv";-------------------------
     * result[31] = "hb9991_SHBE_641800k June 2014.csv";------------------------
     * result[32] = "hb9991_SHBE_648859k July 2014.csv";------------------------
     * result[33] = "hb9991_SHBE_656520k August 2014.csv";----------------------
     * result[34] = "hb9991_SHBE_663169k September 2014.csv";-------------------
     * result[35] = "hb9991_SHBE_670535k October 2014.csv";---------------------
     * result[36] = "hb9991_SHBE_677543k November 2014.csv";--------------------
     * result[37] = "hb9991_SHBE_684519k December 2014.csv";--------------------
     * result[38] = "hb9991_SHBE_691401k January 2015.csv";---------------------
     * result[39] = "hb9991_SHBE_697933k February 2015.csv";--------------------
     * result[40] = "hb9991_SHBE_705679k March 2015.csv";-----------------------
     * result[41] = "hb9991_SHBE_712197k April 2015.csv";-----------------------
     * result[42] = "hb9991_SHBE_718782k May 2015.csv";-------------------------
     * result[43] = "hb9991_SHBE_725465k June 2015.csv";------------------------
     * result[44] = "hb9991_SHBE_733325k July 2015.csv";------------------------
     * result[45] = "hb9991_SHBE_740520k August 2015.csv";----------------------
     * result[46] = "hb9991_SHBE_747387k September 2015.csv";
     */
    public static String[] getSHBEFilenamesAll() {
//        String[] result = new String[1];
//        result[0] = "hb9991_SHBE_670535k October 2014 v2.csv";
        String[] result = new String[48];
        result[0] = "hb9803_SHBE_206728k April 2008.csv";
        result[1] = "hb9803_SHBE_234696k October 2008.csv";
        result[2] = "hb9803_SHBE_265149k April 2009.csv";
        result[3] = "hb9803_SHBE_295723k October 2009.csv";
        result[4] = "hb9803_SHBE_329509k April 2010.csv";
        result[5] = "hb9803_SHBE_363186k October 2010.csv";
        result[6] = "hb9803_SHBE_391746k March 2011.csv"; // For some reason we have March when we probably should have January!
        result[7] = "hb9803_SHBE_397524k April 2011.csv";
        result[8] = "hb9803_SHBE_415181k July 2011.csv";
        result[9] = "hb9803_SHBE_433970k October 2011.csv";
        result[10] = "hb9803_SHBE_451836k January 2012.csv";
        result[11] = "hb9803_SHBE_470742k April 2012.csv";
        result[12] = "hb9803_SHBE_490903k July 2012.csv";
        result[13] = "hb9803_SHBE_511038k October 2012.csv";
        result[14] = "hb9803_SHBE_530243k January 2013.csv";
        result[15] = "hb9803_SHBE_536123k February 2013.csv";
        result[16] = "hb9991_SHBE_543169k March 2013.csv";
        result[17] = "hb9991_SHBE_549416k April 2013.csv";
        result[18] = "hb9991_SHBE_555086k May 2013.csv";
        result[19] = "hb9991_SHBE_562036k June 2013.csv";
        result[20] = "hb9991_SHBE_568694k July 2013.csv";
        result[21] = "hb9991_SHBE_576432k August 2013.csv";
        result[22] = "hb9991_SHBE_582832k September 2013.csv";
        result[23] = "hb9991_SHBE_589664k Oct 2013.csv";
        result[24] = "hb9991_SHBE_596500k Nov 2013.csv";
        result[25] = "hb9991_SHBE_603335k Dec 2013.csv";
        result[26] = "hb9991_SHBE_609791k Jan 2014.csv";
        result[27] = "hb9991_SHBE_615103k Feb 2014.csv";
        result[28] = "hb9991_SHBE_621666k Mar 2014.csv";
        result[29] = "hb9991_SHBE_629066k Apr 2014.csv";
        result[30] = "hb9991_SHBE_635115k May 2014.csv";
        result[31] = "hb9991_SHBE_641800k June 2014.csv";
        result[32] = "hb9991_SHBE_648859k July 2014.csv";
        result[33] = "hb9991_SHBE_656520k August 2014.csv";
        result[34] = "hb9991_SHBE_663169k September 2014.csv"; // Original file sent was corrupt!
        result[35] = "hb9991_SHBE_670535k October 2014.csv";
        result[36] = "hb9991_SHBE_677543k November 2014.csv";
        result[37] = "hb9991_SHBE_684519k December 2014.csv";
        result[38] = "hb9991_SHBE_691401k January 2015.csv";
        result[39] = "hb9991_SHBE_697933k February 2015.csv";
        result[40] = "hb9991_SHBE_705679k March 2015.csv";
        result[41] = "hb9991_SHBE_712197k April 2015.csv";
        result[42] = "hb9991_SHBE_718782k May 2015.csv";
        result[43] = "hb9991_SHBE_725465k June 2015.csv";
        result[44] = "hb9991_SHBE_733325k July 2015.csv";
        result[45] = "hb9991_SHBE_740520k August 2015.csv";
        result[46] = "hb9991_SHBE_747387k September 2015.csv";
        result[47] = "hb9991_SHBE_754889k October 2015.csv";
        return result;
    }

    public static ArrayList<Integer> getSHBEFilenameIndexes() {
        ArrayList<Integer> result;
        result = new ArrayList<Integer>();
        String[] SHBEFilenames;
        SHBEFilenames = DW_SHBE_Handler.getSHBEFilenamesAll();
        for (int i = 0; i < SHBEFilenames.length; i++) {
            result.add(i);
        }
        return result;
    }

    public static ArrayList<Integer> getSHBEFilenameIndexesExcept34() {
        ArrayList<Integer> result;
        result = getSHBEFilenameIndexes();
        result.remove(34);
        return result;
    }

    /**
     *
     * @param tSHBEFilenames
     * @param include
     * @return * {@code
     * Object[] result;
     * result = new Object[2];
     * TreeMap<BigDecimal, String> valueLabel;
     * valueLabel = new TreeMap<BigDecimal, String>();
     * TreeMap<String, BigDecimal> fileLabelValue;
     * fileLabelValue = new TreeMap<String, BigDecimal>();
     * result[0] = valueLabel;
     * result[1] = fileLabelValue;
     * }
     */
    public static Object[] getTreeMapDateLabelSHBEFilenames(
            String[] tSHBEFilenames,
            ArrayList<Integer> include) {
        Object[] result;
        result = new Object[2];
        TreeMap<BigDecimal, String> valueLabel;
        valueLabel = new TreeMap<BigDecimal, String>();
        TreeMap<String, BigDecimal> fileLabelValue;
        fileLabelValue = new TreeMap<String, BigDecimal>();
        result[0] = valueLabel;
        result[1] = fileLabelValue;

        ArrayList<String> month3Letters;
        month3Letters = Generic_Time.getMonths3Letters();

        int startMonth = 0;
        int startYear = 0;
        int yearInt0 = 0;
        int month0Int = 0;
        String month0 = "";
        String m30 = "";
        String yM30 = "";

        boolean first = true;
        Iterator<Integer> ite;
        ite = include.iterator();
        while (ite.hasNext()) {
            int i = ite.next();
            if (first) {
                yM30 = getYM3(tSHBEFilenames[i]);
                yearInt0 = Integer.valueOf(getYear(tSHBEFilenames[i]));
                month0 = getMonth(tSHBEFilenames[i]);
                m30 = month0.substring(0, 3);
                month0Int = month3Letters.indexOf(m30) + 1;
                startMonth = month0Int;
                startYear = yearInt0;
                first = false;
            } else {
                String yM31;
                yM31 = getYM3(tSHBEFilenames[i]);
                int yearInt;
                String month;
                int monthInt;
                String m3;
                month = getMonth(tSHBEFilenames[i]);
                yearInt = Integer.valueOf(getYear(tSHBEFilenames[i]));
                m3 = month.substring(0, 3);
                monthInt = month3Letters.indexOf(m3) + 1;
                BigDecimal timeSinceStart;
                timeSinceStart = BigDecimal.valueOf(
                        Generic_Time.getMonthDiff(
                                startYear, yearInt, startMonth, monthInt));
                //System.out.println(timeSinceStart);
//                valueLabel.put(
//                        timeSinceStart,
//                        yearInt0 + " " + m30 + " - " + yearInt + " " + m3);
                String label;
                label = yM30 + "-" + yM31;
                valueLabel.put(
                        timeSinceStart,
                        label);
//                String fileLabel;
//                fileLabel = yearInt0 + " " + month0 + "_" + yearInt + " " + month;
                fileLabelValue.put(
                        label,
                        timeSinceStart);

                //System.out.println(fileLabel);
                yearInt0 = yearInt;
                month0 = month;
                m30 = m3;
                month0Int = monthInt;
                yM30 = yM31;
            }
        }
        return result;
    }

//    /**
//     *
//     * @param tSHBEFilenames
//     * @param include
//     * @param startIndex
//     * @return * {@code
//     * Object[] result;
//     * result = new Object[2];
//     * TreeMap<BigDecimal, String> valueLabel;
//     * valueLabel = new TreeMap<BigDecimal, String>();
//     * TreeMap<String, BigDecimal> fileLabelValue;
//     * fileLabelValue = new TreeMap<String, BigDecimal>();
//     * result[0] = valueLabel;
//     * result[1] = fileLabelValue;
//     * }
//     */
//    public static TreeMap<BigDecimal, String> getDateValueLabelSHBEFilenames(
//            String[] tSHBEFilenames,
//            ArrayList<Integer> include) {
//        TreeMap<BigDecimal, String> result;
//        result = new TreeMap<BigDecimal, String>();
//        
//        ArrayList<String> month3Letters;
//        month3Letters = Generic_Time.getMonths3Letters();
//
//        int startMonth = 0;
//        int startYear = 0;
//        int yearInt0 = 0;
//        int month0Int = 0;
//        String month0 = "";
//        String m30 = "";
//        String yM30 = "";
//
//        boolean first = true;
//        Iterator<Integer> ite;
//        ite = include.iterator();
//        while (ite.hasNext()) {
//            int i = ite.next();
//            if (first) {
//                yM30 = getYM3(tSHBEFilenames[i]);
//                yearInt0 = Integer.valueOf(getYear(tSHBEFilenames[i]));
//                month0 = getMonth(tSHBEFilenames[i]);
//                m30 = month0.substring(0, 3);
//                month0Int = month3Letters.indexOf(m30) + 1;
//                startMonth = month0Int;
//                startYear = yearInt0;
//                first = false;
//            } else {
//                String yM31 = getYM3(tSHBEFilenames[i]);
//                int yearInt;
//                String month;
//                int monthInt;
//                String m3;
//                month = getMonth(tSHBEFilenames[i]);
//                yearInt = Integer.valueOf(getYear(tSHBEFilenames[i]));
//                m3 = month.substring(0, 3);
//                monthInt = month3Letters.indexOf(m3) + 1;
//                BigDecimal timeSinceStart;
//                timeSinceStart = BigDecimal.valueOf(
//                        Generic_Time.getMonthDiff(
//                                startYear, yearInt, startMonth, monthInt));
//                //System.out.println(timeSinceStart);
//                result.put(
//                        timeSinceStart,
//                        yM30 + " - " + yM31);
//                
//                //System.out.println(fileLabel);
//                yearInt0 = yearInt;
//                month0 = month;
//                m30 = m3;
//                month0Int = monthInt;
//            }
//        }
//        return result;
//    }
    public static String getMonth3(String SHBEFilename) {
        String result;
        result = getMonth(SHBEFilename).substring(0, 3);
        return result;
    }

    public static String getYM3(String SHBEFilename) {
        return getYM3(SHBEFilename, "_");
    }

    public static String getYM3(String SHBEFilename, String separator) {
        String result;
        String year;
        year = DW_SHBE_Handler.getYear(SHBEFilename);
        String m3;
        m3 = DW_SHBE_Handler.getMonth3(SHBEFilename);
        result = year + separator + m3;
        return result;
    }

    public static String getYearMonthNumber(String SHBEFilename) {
        String result;
        String year;
        year = DW_SHBE_Handler.getYear(SHBEFilename);
        String monthNumber;
        monthNumber = DW_SHBE_Handler.getMonthNumber(SHBEFilename);
        result = year + "-" + monthNumber;
        return result;
    }

    /**
     * For example for SHBEFilename "hb9991_SHBE_555086k May 2013.csv", this
     * returns "May"
     *
     * @param SHBEFilename
     * @return
     */
    public static String getMonth(String SHBEFilename) {
        return SHBEFilename.split(" ")[1];
    }

    /**
     * For example for SHBEFilename "hb9991_SHBE_555086k May 2013.csv", this
     * returns "May"
     *
     * @param SHBEFilename
     * @return
     */
    public static String getMonthNumber(String SHBEFilename) {
        String m3;
        m3 = getMonth3(SHBEFilename);
        return Generic_Time.getMonthNumber(m3);
    }

    /**
     * For example for SHBEFilename "hb9991_SHBE_555086k May 2013.csv", this
     * returns "2013"
     *
     * @param SHBEFilename
     * @return
     */
    public static String getYear(String SHBEFilename) {
        return SHBEFilename.split(" ")[2].substring(0, 4);
    }

    /**
     * Method for getting SHBE collections filenames in an array
     *
     * @return String[] SHBE collections filenames
     */
    public static String[] getSHBEFilenamesSome() {
        String[] result = new String[6];
        result[0] = "hb9991_SHBE_549416k April 2013.csv";
        result[1] = "hb9991_SHBE_555086k May 2013.csv";
        result[2] = "hb9991_SHBE_562036k June 2013.csv";
        result[3] = "hb9991_SHBE_568694k July 2013.csv";
        result[4] = "hb9991_SHBE_576432k August 2013.csv";
        result[5] = "hb9991_SHBE_582832k September 2013.csv";
        return result;
    }

    public static HashMap<DW_ID, String> getDW_IDToStringLookup(
            File f) {
        HashMap<DW_ID, String> result;
        if (f.exists()) {
            result = (HashMap<DW_ID, String>) Generic_StaticIO.readObject(f);
        } else {
            result = new HashMap<DW_ID, String>();
        }
        return result;
    }

    public static HashMap<String, DW_ID> getStringToDW_IDLookup(
            File f) {
        HashMap<String, DW_ID> result;
        if (f.exists()) {
            result = (HashMap<String, DW_ID>) Generic_StaticIO.readObject(
                    f);
        } else {
            result = new HashMap<String, DW_ID>();
        }
        return result;
    }

    public static HashMap<String, DW_ID> getPostcodeToPostcodeIDLookup(
            File PostcodeToPostcodeIDLookupFile) {
        if (PostcodeToPostcodeIDLookup == null) {
            PostcodeToPostcodeIDLookup = getStringToDW_IDLookup(
                    PostcodeToPostcodeIDLookupFile);
        }
        return PostcodeToPostcodeIDLookup;
    }

    public static HashMap<String, DW_ID> getPostcodeToPostcodeIDLookup() {
        File f;
        f = getPostcodeToPostcodeIDLookupFile();
        return getPostcodeToPostcodeIDLookup(f);
    }

    public static HashMap<DW_ID, String> getPostcodeIDToPostcodeLookup(
            File PostcodeIDToPostcodeLookupFile) {
        if (PostcodeIDToPostcodeLookup == null) {
            PostcodeIDToPostcodeLookup = getDW_IDToStringLookup(PostcodeIDToPostcodeLookupFile);
        }
        return PostcodeIDToPostcodeLookup;
    }

    public static HashMap<DW_ID, String> getPostcodeIDToPostcodeLookup() {
        File f;
        f = getPostcodeIDToPostcodeLookupFile();
        return getPostcodeIDToPostcodeLookup(f);
    }

    public static HashMap<String, DW_ID> getNINOToIDLookup(
            File NINOToIDLookupFile) {
        if (NINOToIDLookup == null) {
            NINOToIDLookup = getStringToDW_IDLookup(NINOToIDLookupFile);
        }
        return NINOToIDLookup;
    }

    public static HashMap<String, DW_ID> getNINOToIDLookup(String inPaymentType) {
        File f;
        f = getNINOToIDLookupFile(inPaymentType);
        return getNINOToIDLookup(f);
    }

    public static HashMap<DW_ID, String> getIDToNINOLookup(
            File IDToNINOLookupFile) {
        if (IDToNINOLookup == null) {
            IDToNINOLookup = getDW_IDToStringLookup(IDToNINOLookupFile);
        }
        return IDToNINOLookup;
    }

    public static HashMap<DW_ID, String> getIDToNINOLookup(String inPaymentType) {
        File f;
        f = getIDToNINOLookupFile(inPaymentType);
        return getIDToNINOLookup(f);
    }

    public static File getPostcodeToPostcodeIDLookupFile() {
        File result;
        String filename = "PostcodeToPostcodeID_HashMapStringDW_ID.thisFile";
        result = new File(
                DW_Files.getGeneratedSHBEDir(),
                filename);
        return result;
    }

    public static File getPostcodeIDToPostcodeLookupFile() {
        File result;
        String filename = "PostcodeIDToPostcode_HashMapDW_IDString.thisFile";
        result = new File(
                DW_Files.getGeneratedSHBEDir(),
                filename);
        return result;
    }

    public static File getNINOToIDLookupFile(String inPaymentType) {
        File result;
        String filename = "NINOToID_HashMapStringDW_ID.thisFile";
        result = new File(
                DW_Files.getGeneratedSHBEDir(inPaymentType),
                filename);
        return result;
    }

    public static File getIDToNINOLookupFile(String inPaymentType) {
        File result;
        String filename = "IDToNINO_HashMapDW_IDString.thisFile";
        result = new File(
                DW_Files.getGeneratedSHBEDir(inPaymentType),
                filename);
        return result;
    }

    /**
     * [0]
     *
     * @param inPaymentType
     * @param filename
     * @return
     */
    public static File getDRecordsFile(
            String inPaymentType,
            String filename) {
        File result;
        String partFilename = "DRecords_TreeMapStringDW_SHBE_Record.thisFile";
        result = getFile(inPaymentType, filename, partFilename);
        return result;
    }

    /**
     * [1]
     *
     * @param inPaymentType
     * @param filename
     * @return
     */
    public static File getSRecordsWithoutDRecordsFile(
            String inPaymentType,
            String filename) {
        File result;
        String partFilename = "SRecordsWithoutDRecords_TreeMapStringDW_SHBE_S_Record.thisFile";
        result = getFile(inPaymentType, filename, partFilename);
        return result;
    }

    /**
     * [2]
     *
     * @param inPaymentType
     * @param filename
     * @return
     */
    public static File getClaimantIDsFile(
            String inPaymentType,
            String filename) {
        File result;
        String partFilename = "ClaimantIDs_HashSetString.thisFile";
        result = getFile(inPaymentType, filename, partFilename);
        return result;
    }

    /**
     * [3]
     *
     * @param inPaymentType
     * @param filename
     * @return
     */
    public static File getPartnerIDsFile(
            String inPaymentType,
            String filename) {
        File result;
        String partFilename = "PartnerIDs_HashSetString.thisFile";
        result = getFile(inPaymentType, filename, partFilename);
        return result;
    }

    /**
     * [4]
     *
     * @param inPaymentType
     * @param filename
     * @return
     */
    public static File getDependentsIDsFile(
            String inPaymentType,
            String filename) {
        File result;
        String partFilename = "DependentsIDs_HashSetString.thisFile";
        result = getFile(inPaymentType, filename, partFilename);
        return result;
    }

    /**
     * [5]
     *
     * @param inPaymentType
     * @param filename
     * @return
     */
    public static File getNonDependentsIDsFile(
            String inPaymentType,
            String filename) {
        File result;
        String partFilename = "NonDependentsIDs_HashSetString.thisFile";
        result = getFile(inPaymentType, filename, partFilename);
        return result;
    }

    /**
     * [6]
     *
     * @param inPaymentType
     * @param filename
     * @return
     */
    public static File getAllHouseholdIDsFile(
            String inPaymentType,
            String filename) {
        File result;
        String partFilename = "AllHouseholdIDs_HashSetString.thisFile";
        result = getFile(inPaymentType, filename, partFilename);
        return result;
    }

    /**
     * [7]
     *
     * @param inPaymentType
     * @param filename
     * @return
     */
    public static File getClaimantIDToRecordIDLookupFile(
            String inPaymentType,
            String filename) {
        File result;
        String partFilename = "ClaimantIDToRecordIDLookup_HashMapStringLong.thisFile";
        result = getFile(inPaymentType, filename, partFilename);
        return result;
    }

    /**
     * [8]
     *
     * @param inPaymentType
     * @param filename
     * @return
     */
    public static File getClaimantIDToPostcodeLookupFile(
            String inPaymentType,
            String filename) {
        File result;
        String partFilename = "ClaimantIDToPostcodeLookup_HashMapStringString.thisFile";
        result = getFile(inPaymentType, filename, partFilename);
        return result;
    }

    /**
     * [9]
     *
     * @param inPaymentType
     * @param filename
     * @return
     */
    public static File getClaimantIDToTenancyTypeLookupFile(
            String inPaymentType,
            String filename) {
        File result;
        String partFilename = "ClaimantIDToTenancyTypeLookup_HashMapStringInteger.thisFile";
        result = getFile(inPaymentType, filename, partFilename);
        return result;
    }

    /**
     * [10]
     *
     * @param inPaymentType
     * @param filename
     * @return
     */
    public static File getCTBRefToClaimantIDLookupFile(
            String inPaymentType,
            String filename) {
        File result;
        String partFilename = "CTBRefToClaimantIDLookup_HashMapLongString.thisFile";
        result = getFile(inPaymentType, filename, partFilename);
        return result;
    }

    /**
     * [11]
     *
     * @param inPaymentType
     * @param filename
     * @return
     */
    public static File getClaimantIDToCTBRefLookupFile(
            String inPaymentType,
            String filename) {
        File result;
        String partFilename = "ClaimantIDToCTBRefLookup_HashMapLongString.thisFile";
        result = getFile(
                inPaymentType,
                filename,
                partFilename);
        return result;
    }

    /**
     * [12]
     *
     * @param inPaymentType
     * @param filename
     * @return
     */
    public static File getLoadSummaryFile(
            String inPaymentType,
            String filename) {
        File result;
        String partFilename = "LoadSummary_HashMapStringInteger.thisFile";
        result = getFile(inPaymentType, filename, partFilename);
        return result;
    }

    /**
     * [13]
     *
     * @param inPaymentType
     * @param filename
     * @return
     */
    public static File getClaimantIDPostcodeSetFile(
            String inPaymentType,
            String filename) {
        File result;
        String partFilename = "ClaimantIDPostcode_HashSetID_Postcode.thisFile";
        result = getFile(inPaymentType, filename, partFilename);
        return result;
    }

    /**
     * [14]
     *
     * @param inPaymentType
     * @param filename
     * @return
     */
    public static File getClaimantIDTenancyTypeSetFile(
            String inPaymentType,
            String filename) {
        File result;
        String partFilename = "ClaimantIDTenancyType_HashSetID_TenancyType.thisFile";
        result = getFile(inPaymentType, filename, partFilename);
        return result;
    }

    /**
     * [15]
     *
     * @param inPaymentType
     * @param filename
     * @return
     */
    public static File getClaimantIDPostcodeTenancyTypeSetFile(
            String inPaymentType,
            String filename) {
        File result;
        String partFilename = "ClaimantIDPostcodeTenancyType_HashSetID_Postcode_TenancyType.thisFile";
        result = getFile(inPaymentType, filename, partFilename);
        return result;
    }

    /**
     * [16]
     *
     * @param inPaymentType
     * @param filename
     * @param doUnderOccupancy
     * @return
     */
    public static File getIncomeAndRentSummaryFile(
            String inPaymentType,
            String filename,
            boolean doUnderOccupancy) {
        File result;
        String partFilename;
        if (doUnderOccupancy) {
            partFilename = "IncomeAndRentSummaryUO_HashMapStringBigDecimal.thisFile";
        } else {
            partFilename = "IncomeAndRentSummary_HashMapStringBigDecimal.thisFile";
        }
        result = getFile(inPaymentType, filename, partFilename);
        return result;
    }

    /**
     *
     * @param dirName
     * @param filename
     * @param partFilename
     * @return
     */
    public static File getFile(
            String dirName,
            String filename,
            String partFilename) {
        File result;
        String key;
        key = getYearMonthNumber(filename);
        String filenameOut = key + "_" + partFilename;
        File dirOut;
        dirOut = new File(
                DW_Files.getGeneratedSHBEDir(dirName),
                key);
        dirOut.mkdirs();
        result = new File(
                dirOut,
                filenameOut);
        return result;
    }


    public static int getNumberOfTenancyTypes() {
        return 10;
    }

    public static int getNumberOfClaimantsEthnicGroups() {
        return 17;
    }

    public static int getOneOverMaxValueOfPassportStandardIndicator() {
        return 6;
    }

    /**
     * Negation of getOmits()
     *
     * @return
     */
    public static TreeMap<String, ArrayList<Integer>> getIncludes() {
        TreeMap<String, ArrayList<Integer>> result;
        result = new TreeMap<String, ArrayList<Integer>>();
        TreeMap<String, ArrayList<Integer>> omits;
        omits = getOmits();
        Iterator<String> ite;
        ite = omits.keySet().iterator();
        while (ite.hasNext()) {
            String omitKey;
            omitKey = ite.next();
            ArrayList<Integer> omit;
            omit = omits.get(omitKey);
            ArrayList<Integer> include;
            //include = DW_SHBE_Handler.getSHBEFilenameIndexesExcept34();
            include = DW_SHBE_Handler.getSHBEFilenameIndexes();
            include.removeAll(omit);
            result.put(omitKey, include);
        }
        return result;
    }

    /**
     * Negation of getIncludes()
     *
     * @return
     */
    public static TreeMap<String, ArrayList<Integer>> getOmits() {
        TreeMap<String, ArrayList<Integer>> result;
        result = new TreeMap<String, ArrayList<Integer>>();
        String omitKey;
//        ArrayList<Integer> omitAll;
//        omitKey = "All";
//        omitAll = new ArrayList<Integer>();
//        //omitAll.add(34);
//        result.put(omitKey, omitAll);
        omitKey = "Yearly";
        ArrayList<Integer> omitYearly;
        omitYearly = new ArrayList<Integer>();
        omitYearly.add(1);
        omitYearly.add(3);
        omitYearly.add(5);
        omitYearly.add(6);
        omitYearly.add(8);
        omitYearly.add(9);
        omitYearly.add(10);
        omitYearly.add(12);
        omitYearly.add(13);
        omitYearly.add(14);
        omitYearly.add(15);
        omitYearly.add(16);
        omitYearly.add(18);
        omitYearly.add(19);
        omitYearly.add(20);
        omitYearly.add(21);
        omitYearly.add(22);
        omitYearly.add(23);
        omitYearly.add(24);
        omitYearly.add(25);
        omitYearly.add(26);
        omitYearly.add(27);
        omitYearly.add(28);
        omitYearly.add(30);
        omitYearly.add(31);
        omitYearly.add(32);
        omitYearly.add(33);
        omitYearly.add(34);
        omitYearly.add(35);
        omitYearly.add(36);
        omitYearly.add(37);
        omitYearly.add(38);
        omitYearly.add(39);
        omitYearly.add(40);
        omitYearly.add(42);
        omitYearly.add(43);
        omitYearly.add(44);
        omitYearly.add(45);
        omitYearly.add(46);
        result.put(omitKey, omitYearly);
        omitKey = "6Monthly";
        ArrayList<Integer> omit6Monthly;
        omit6Monthly = new ArrayList<Integer>();
        omit6Monthly.add(6);
        omit6Monthly.add(8);
        omit6Monthly.add(10);
        omit6Monthly.add(12);
        omit6Monthly.add(14);
        omit6Monthly.add(15);
        omit6Monthly.add(16);
        omit6Monthly.add(18);
        omit6Monthly.add(19);
        omit6Monthly.add(20);
        omit6Monthly.add(21);
        omit6Monthly.add(22);
        omit6Monthly.add(24);
        omit6Monthly.add(25);
        omit6Monthly.add(26);
        omit6Monthly.add(27);
        omit6Monthly.add(28);
        omit6Monthly.add(30);
        omit6Monthly.add(31);
        omit6Monthly.add(32);
        omit6Monthly.add(33);
        omit6Monthly.add(34);
        omit6Monthly.add(36);
        omit6Monthly.add(37);
        omit6Monthly.add(38);
        omit6Monthly.add(39);
        omit6Monthly.add(40);
        omit6Monthly.add(42);
        omit6Monthly.add(43);
        omit6Monthly.add(44);
        omit6Monthly.add(45);
        omit6Monthly.add(46);
        result.put(omitKey, omit6Monthly);
        omitKey = "3Monthly";
        ArrayList<Integer> omit3Monthly;
        omit3Monthly = new ArrayList<Integer>();
        for (int i = 0; i < 7; i++) {
            omit3Monthly.add(i);
        }
        omit3Monthly.add(15);
        omit3Monthly.add(16);
        omit3Monthly.add(18);
        omit3Monthly.add(19);
        omit3Monthly.add(21);
        omit3Monthly.add(22);
        omit3Monthly.add(24);
        omit3Monthly.add(25);
        omit3Monthly.add(27);
        omit3Monthly.add(28);
        omit3Monthly.add(30);
        omit3Monthly.add(31);
        omit3Monthly.add(33);
        omit3Monthly.add(34);
        omit3Monthly.add(36);
        omit3Monthly.add(37);
        omit3Monthly.add(39);
        omit3Monthly.add(40);
        omit3Monthly.add(42);
        omit3Monthly.add(43);
//        omit3Monthly.add(44); // Only added for final report graphs as we only wanted April 2013 to April 2015
        omit3Monthly.add(45);
        omit3Monthly.add(46);
        result.put(omitKey, omit3Monthly);
//        omitKey = "Monthly";
//        ArrayList<Integer> omitMonthly;
//        omitMonthly = new ArrayList<Integer>();
//        for (int i = 0; i < 14; i++) {
//            omitMonthly.add(i);
//        }
//        result.put(omitKey, omitMonthly);
        omitKey = "MonthlyUO";
        ArrayList<Integer> omitMonthlyUO;
        omitMonthlyUO = new ArrayList<Integer>();
        for (int i = 0; i < 17; i++) {
            omitMonthlyUO.add(i);
        }
        result.put(omitKey, omitMonthlyUO);
        return result;
    }
}
