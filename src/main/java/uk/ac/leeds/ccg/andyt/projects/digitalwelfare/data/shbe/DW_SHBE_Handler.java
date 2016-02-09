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

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.math.Generic_BigDecimal;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Time;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.Summary;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UOReport_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Set;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class DW_SHBE_Handler {

    transient final DW_Environment env;

    private HashSet<String> RecordTypes;

    public static final String sHB = "HB";
    public static final String sCTB = "CTB";
    public static final String sDefaultNINO = "XX999999XX";

    public static final String sAll = "All";
    public static final String sMonthlyUO = "MonthlyUO";
    public static final String sMonthly = "Monthly";
    public static final String s6Monthly = "6Monthly";
    public static final String s3Monthly = "3Monthly";
    public static final String sYearly = "Yearly";

    static HashMap<String, DW_ID> NINOToDW_IDLookup;
    static HashMap<DW_ID, String> DW_IDToNINOLookup;
    static HashMap<String, DW_ID> DOBToDW_IDLookup;
    static HashMap<DW_ID, String> DW_IDToDOBLookup;
    static HashMap<DW_PersonID, DW_ID> DW_PersonIDToDW_IDLookup;
    static HashMap<DW_ID, DW_PersonID> DW_IDToDW_PersonIDLookup;
    static HashMap<String, DW_ID> PostcodeToPostcodeIDLookup;
    static HashMap<DW_ID, String> PostcodeIDToPostcodeLookup;

    public DW_SHBE_Handler(DW_Environment env) {
        this.env = env;
//        NINOToDW_IDLookup = new HashMap<String, DW_ID>();
//        DW_IDToNINOLookup = new HashMap<DW_ID, String>();
//        DOBToDW_IDLookup = new HashMap<String, DW_ID>();
//        DW_IDToDOBLookup = new HashMap<DW_ID, String>();
//        DW_PersonIDToDW_IDLookup = new HashMap<DW_PersonID, DW_ID>();
//        DW_IDToDW_PersonIDLookup = new HashMap<DW_ID, DW_PersonID>();
//        PostcodeToPostcodeIDLookup = new HashMap<String, DW_ID>();
//        PostcodeIDToPostcodeLookup = new HashMap<DW_ID, String>();
        initRecordTypes();
    }

    public void run() {
        String[] SHBEFilenames;
        SHBEFilenames = getSHBEFilenamesAll();
        File dir;
        dir = DW_Files.getInputSHBEDir();
        // NINO
        File NINOToDW_IDLookupFile;
        File DW_IDToNINOLookupFile;
        NINOToDW_IDLookupFile = getNINOToDW_IDLookupFile();
        DW_IDToNINOLookupFile = getDW_IDToNINOLookupFile();
//        NINOToDW_IDLookup = getNINOToDW_IDLookup(NINOToDW_IDLookupFile);
        NINOToDW_IDLookup = new HashMap<String, DW_ID>();
//        DW_IDToNINOLookup = getDW_IDToNINOLookup(DW_IDToNINOLookupFile);
        DW_IDToNINOLookup = new HashMap<DW_ID, String>();
        // DOB
        File DOBToDW_IDLookupFile;
        File DW_IDToDOBLookupFile;
        DOBToDW_IDLookupFile = getDOBToDW_IDLookupFile();
        DW_IDToDOBLookupFile = getDW_IDToDOBLookupFile();
//        DOBToDW_IDLookup = getDOBToDW_IDLookup(DOBToDW_IDLookupFile);
        DOBToDW_IDLookup = new HashMap<String, DW_ID>();
//        DW_IDToDOBLookup = getDW_IDToDOBLookup(DW_IDToDOBLookupFile);
        DW_IDToDOBLookup = new HashMap<DW_ID, String>();
        // Postcode
        File PostcodeToPostcodeIDLookupFile;
        File PostcodeIDToPostcodeLookupFile;
        PostcodeToPostcodeIDLookupFile = getPostcodeToPostcodeIDLookupFile();
        PostcodeIDToPostcodeLookupFile = getPostcodeIDToPostcodeLookupFile();
//        PostcodeToPostcodeIDLookup = getPostcodeToPostcodeIDLookup(
//                PostcodeToPostcodeIDLookupFile);
        PostcodeToPostcodeIDLookup = new HashMap<String, DW_ID>();
//        PostcodeIDToPostcodeLookup = getPostcodeIDToPostcodeLookup(
//                PostcodeIDToPostcodeLookupFile);
        PostcodeIDToPostcodeLookup = new HashMap<DW_ID, String>();
        // Person
        File DW_PersonIDToDW_IDLookupFile;
        File DW_IDToDW_PersonIDLookupFile;
        DW_PersonIDToDW_IDLookupFile = getDW_PersonIDToDW_IDLookupFile();
        DW_IDToDW_PersonIDLookupFile = getDW_IDToDW_PersonIDLookupFile();
//        DW_PersonIDToDW_IDLookup = DW_SHBE_Handler.getDW_PersonIDToDW_IDLookup(
//                DW_PersonIDToDW_IDLookupFile);
        DW_PersonIDToDW_IDLookup = new HashMap<DW_PersonID, DW_ID>();
//        DW_IDToDW_PersonIDLookup = getDW_IDToDW_PersonIDLookup(
//                DW_IDToDW_PersonIDLookupFile);
        DW_IDToDW_PersonIDLookup = new HashMap<DW_ID, DW_PersonID>();
        // Loop
        ArrayList<String> paymentTypes;
        paymentTypes = getPaymentTypes();
        Iterator<String> ite = paymentTypes.iterator();
        while (ite.hasNext()) {
            String paymentType;
            paymentType = ite.next();
            System.out.println("----------------------");
            System.out.println("Payment Type " + paymentType);
            System.out.println("----------------------");
            //for (int i = 0; i < SHBEFilenames.length; i++) {
            for (int i = 0; i < SHBEFilenames.length; i++) {
                File collectionDir = new File(
                        DW_Files.getSwapSHBEDir(),
                        paymentType);
                collectionDir = new File(
                        collectionDir,
                        SHBEFilenames[i]);
                DW_SHBE_CollectionHandler handler;
                handler = new DW_SHBE_CollectionHandler(
                        env,
                        collectionDir);
                DW_SHBE_Collection SHBEData;
                SHBEData = new DW_SHBE_Collection(
                        handler.nextID,
                        handler,
                        dir,
                        SHBEFilenames[i],
                        paymentType);
            }
        }
        Generic_StaticIO.writeObject(NINOToDW_IDLookup, NINOToDW_IDLookupFile);
        Generic_StaticIO.writeObject(DW_IDToNINOLookup, DW_IDToNINOLookupFile);
        Generic_StaticIO.writeObject(DOBToDW_IDLookup, DOBToDW_IDLookupFile);
        Generic_StaticIO.writeObject(DW_IDToDOBLookup, DW_IDToDOBLookupFile);
        Generic_StaticIO.writeObject(DW_PersonIDToDW_IDLookup, DW_PersonIDToDW_IDLookupFile);
        Generic_StaticIO.writeObject(DW_IDToDW_PersonIDLookup, DW_IDToDW_PersonIDLookupFile);
        Generic_StaticIO.writeObject(PostcodeToPostcodeIDLookup, PostcodeToPostcodeIDLookupFile);
        Generic_StaticIO.writeObject(PostcodeIDToPostcodeLookup, PostcodeIDToPostcodeLookupFile);
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

//    public void runNew() {
//        String SHBEFilename;
//        SHBEFilename = "hb9803_SHBE_397524k April 2011.csv";
//        //SHBEFilename = "hb9991_SHBE_754889k October 2015.csv";
//        File collectionDir = new File(
//                DW_Files.getSwapSHBEDir(),
//                SHBEFilename);
//        DW_SHBE_CollectionHandler handler;
//        handler = new DW_SHBE_CollectionHandler(env, collectionDir);
//        File dir;
//        dir = DW_Files.getInputSHBEDir();
//        // NINO
//        File NINOToDW_IDLookupFile;
//        File DW_IDToNINOLookupFile;
//        NINOToDW_IDLookupFile = getNINOToDW_IDLookupFile();
//        DW_IDToNINOLookupFile = getDW_IDToNINOLookupFile();
//        NINOToDW_IDLookup = getNINOToDW_IDLookup(NINOToDW_IDLookupFile);
//        DW_IDToNINOLookup = getDW_IDToNINOLookup(DW_IDToNINOLookupFile);
//        // Postcode
//        File PostcodeToPostcodeIDLookupFile;
//        File PostcodeIDToPostcodeLookupFile;
//        PostcodeToPostcodeIDLookupFile = getPostcodeToPostcodeIDLookupFile();
//        PostcodeIDToPostcodeLookupFile = getPostcodeIDToPostcodeLookupFile();
//        PostcodeToPostcodeIDLookup = getPostcodeToPostcodeIDLookup(
//                PostcodeToPostcodeIDLookupFile);
//        PostcodeIDToPostcodeLookup = getPostcodeIDToPostcodeLookup(
//                PostcodeIDToPostcodeLookupFile);
//        // Person
//        File DW_PersonIDToDW_IDLookupFile;
//        File DW_IDToDW_PersonIDLookupFile;
//        DW_PersonIDToDW_IDLookupFile = getDW_PersonIDToDW_IDLookupFile();
//        DW_IDToDW_PersonIDLookupFile = getDW_IDToDW_PersonIDLookupFile();
//        DW_PersonIDToDW_IDLookup = DW_SHBE_Handler.getDW_PersonIDToDW_IDLookup(
//                DW_PersonIDToDW_IDLookupFile);
//        DW_IDToDW_PersonIDLookup = getDW_IDToDW_PersonIDLookup(
//                DW_IDToDW_PersonIDLookupFile);
//        ArrayList<String> paymentTypes;
//        paymentTypes = getPaymentTypes();
//        Iterator<String> ite = paymentTypes.iterator();
//        while (ite.hasNext()) {
//            String paymentType;
//            paymentType = ite.next();
//            DW_SHBE_Collection newCollection = new DW_SHBE_Collection(
//                    handler,
//                    dir,
//                    SHBEFilename,
//                    paymentType);
//        }
//        Generic_StaticIO.writeObject(NINOToDW_IDLookup, NINOToDW_IDLookupFile);
//        Generic_StaticIO.writeObject(DW_IDToNINOLookup, DW_IDToNINOLookupFile);
//        Generic_StaticIO.writeObject(DW_PersonIDToDW_IDLookup, DW_PersonIDToDW_IDLookupFile);
//        Generic_StaticIO.writeObject(DW_IDToDW_PersonIDLookup, DW_IDToDW_PersonIDLookupFile);
//        Generic_StaticIO.writeObject(PostcodeToPostcodeIDLookup, PostcodeToPostcodeIDLookupFile);
//        Generic_StaticIO.writeObject(PostcodeIDToPostcodeLookup, PostcodeIDToPostcodeLookupFile);
//    }
//    public static String getClaimantType(DW_SHBE_D_Record D_Record) {
//        String HBClaimRefNo;
//        HBClaimRefNo = D_Record.getHousingBenefitClaimReferenceNumber();
//        return getClaimantType(HBClaimRefNo);
//    }
//    public static String getClaimantType(DW_SHBE_D_Record D_Record) {
//        boolean isHBClaimInPayment;
//        isHBClaimInPayment = isHBClaimInPayment(D_Record);
//        boolean isCTBOnlyClaimInPayment;
//        isCTBOnlyClaimInPayment = isCTBOnlyClaimInPayment(D_Record);
//        if (isHBClaimInPayment) {
//            if (isCTBOnlyClaimInPayment) {
//                return "HBAndCTB";
//            } else {
//                return "HBOnly";
//            }
//        } else {
//            if (isCTBOnlyClaimInPayment) {
//                return "CTBOnly";
//            } else {
//                return "NotInPayment";
//            }
//        }
//    }
    public static String getClaimantType(DW_SHBE_D_Record D_Record) {
        if (isHBClaim(D_Record)) {
            return sHB;
        }
        //if (isCTBOnlyClaim(D_Record)) {
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

    public static boolean isCTBOnlyClaimOtherPT(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfCTBClaimAtExtractDate() == 0) {
            return isCTBOnlyClaim(D_Record);
        }
        return false;
    }

    public static boolean isCTBOnlyClaimSuspended(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfCTBClaimAtExtractDate() == 2) {
            return isCTBOnlyClaim(D_Record);
        }
        return false;
    }

    public static boolean isCTBOnlyClaimInPayment(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfCTBClaimAtExtractDate() == 1) {
            return isCTBOnlyClaim(D_Record);
        }
        return false;
    }

    public static boolean isCTBOnlyClaim(DW_SHBE_D_Record D_Record) {
        if (D_Record == null) {
            return false;
        }
//        String sHBReferenceNumber;
//        sHBReferenceNumber = D_Record.getHousingBenefitClaimReferenceNumber();
        int TT;
        TT = D_Record.getTenancyType();
        return isCTBOnlyClaim(
                //sHBReferenceNumber,
                TT);
    }

    /**
     * @param TT
     * @return
     */
    public static boolean isCTBOnlyClaim(
            //String sHBReferenceNumber,
            int TT) {
        return TT == 5 || TT == 7;
//        if (sHBReferenceNumber.isEmpty()) {
//            return true;
//            //return TT == 5 || TT == 7;
//        }
//        return TT == 5 || TT == 7;
    }

    public static boolean isHBClaimOtherPT(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfHBClaimAtExtractDate() == 0) {
            return isHBClaim(D_Record);
        }
        return false;
    }

    public static boolean isHBClaimSuspended(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfHBClaimAtExtractDate() == 2) {
            return isHBClaim(D_Record);
        }
        return false;
    }

    public static boolean isHBClaimInPayment(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfHBClaimAtExtractDate() == 1) {
            return isHBClaim(D_Record);
        }
        return false;
    }

    public static boolean isHBClaim(DW_SHBE_D_Record D_Record) {
        if (D_Record == null) {
            return false;
        }
        int TT;
        TT = D_Record.getTenancyType();
        return isHBClaim(TT);
    }

    public static boolean isHBClaim(int TT) {
        if (TT == 5) {
            return false;
        }
        if (TT == 7) {
            return false;
        }
        //return TT > -1 && TT < 10;
        return TT > 0 && TT < 10;
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
     * @return {@code ArrayList<DW_SHBE_Collection>}
     *
     */
    public ArrayList<DW_SHBE_Collection> loadSHBEData() {

        String paymentType = "AllPT";

        ArrayList<DW_SHBE_Collection> result;
        result = new ArrayList();
        File dir;
        dir = DW_Files.getInputSHBEDir();
        String[] filenames = getSHBEFilenamesAll();
        for (String filename : filenames) {
            System.out.println("Load SHBE data from " + filename + " ...");
            DW_SHBE_Collection SHBEData;
            SHBEData = new DW_SHBE_Collection(
                    filename, paymentType);
            result.add(SHBEData);
            System.out.println("... loaded SHBE data from " + filename + ".");
        }
        return result;
    }

    /**
     *
     * @param S
     * @param StringToDW_IDLookup
     * @param DW_IDToStringLookup
     * @return
     */
    public static DW_ID getIDAddIfNeeded(
            String S,
            HashMap<String, DW_ID> StringToDW_IDLookup,
            HashMap<DW_ID, String> DW_IDToStringLookup) {
        DW_ID result;
        if (StringToDW_IDLookup.containsKey(S)) {
            result = StringToDW_IDLookup.get(S);
        } else {
            result = new DW_ID(DW_IDToStringLookup.size());
            DW_IDToStringLookup.put(result, S);
            StringToDW_IDLookup.put(S, result);
        }
        return result;
    }

    /**
     *
     * @param P
     * @param DW_PersonIDToDW_IDLookup
     * @param DW_IDToDW_PersonIDLookup
     * @return
     */
    public static DW_ID getIDAddIfNeeded(
            DW_PersonID P,
            HashMap<DW_PersonID, DW_ID> DW_PersonIDToDW_IDLookup,
            HashMap<DW_ID, DW_PersonID> DW_IDToDW_PersonIDLookup) {
        DW_ID result;
        if (DW_PersonIDToDW_IDLookup.containsKey(P)) {
            result = DW_PersonIDToDW_IDLookup.get(P);
        } else {
            result = new DW_ID(DW_IDToDW_PersonIDLookup.size());
            DW_IDToDW_PersonIDLookup.put(result, P);
            DW_PersonIDToDW_IDLookup.put(P, result);
        }
        return result;
    }

    /**
     * @param S
     * @param SToIDLookup
     * @param IDToSLookup
     * @return
     */
    public static DW_ID getPostcodeIDAddIfNeeded(
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
     * @param SHBE_Collection
     * @param paymentType
     * @param filename
     * @param underOccupiedReportSetCouncil
     * @param underOccupiedReportSetRSL
     * @param doUnderOccupancy
     * @param doCouncil
     * @param doRSL
     * @param forceNew
     * @return
     */
    public static HashMap<String, BigDecimal> getIncomeAndRentSummary(
            DW_SHBE_Collection SHBE_Collection,
            String paymentType,
            String filename,
            DW_UnderOccupiedReport_Set underOccupiedReportSetCouncil,
            DW_UnderOccupiedReport_Set underOccupiedReportSetRSL,
            boolean doUnderOccupancy,
            boolean doCouncil,
            boolean doRSL,
            boolean forceNew) {
        HashMap<String, BigDecimal> result;
        File tIncomeAndRentSummaryFile = getIncomeAndRentSummaryFile(
                paymentType,
                filename,
                doUnderOccupancy,
                doCouncil,
                doRSL);
        if (tIncomeAndRentSummaryFile.exists()) {
            if (!forceNew) {
                result = (HashMap<String, BigDecimal>) Generic_StaticIO.readObject(
                        tIncomeAndRentSummaryFile);
                return result;
            }
        }
        result = new HashMap<String, BigDecimal>();
        TreeMap<String, DW_SHBE_Record> recs;
        recs = SHBE_Collection.getRecords();
        int nTT = getNumberOfTenancyTypes();
        BigDecimal tBD;
        // All
        BigDecimal AllTotalIncome = BigDecimal.ZERO;
        long AllTotalCount_IncomeNonZero = 0;
        long AllTotalCount_IncomeZero = 0;
        BigDecimal AllTotalWeeklyEligibleRentAmount = BigDecimal.ZERO;
        long AllTotalCount_WeeklyEligibleRentAmountNonZero = 0;
        long AllTotalCount_WeeklyEligibleRentAmountZero = 0;
        BigDecimal[] AllTotalIncomeByTT;
        AllTotalIncomeByTT = new BigDecimal[nTT];
        long[] AllTotalCount_IncomeByTTNonZero;
        AllTotalCount_IncomeByTTNonZero = new long[nTT];
        long[] AllTotalCount_IncomeByTTZero;
        AllTotalCount_IncomeByTTZero = new long[nTT];
        BigDecimal[] AllTotalByTTWeeklyEligibleRentAmount;
        AllTotalByTTWeeklyEligibleRentAmount = new BigDecimal[nTT];
        int[] AllTotalCount_ByTTWeeklyEligibleRentAmountNonZero;
        AllTotalCount_ByTTWeeklyEligibleRentAmountNonZero = new int[nTT];
        int[] AllTotalCount_ByTTWeeklyEligibleRentAmountZero;
        AllTotalCount_ByTTWeeklyEligibleRentAmountZero = new int[nTT];
        // HB
        BigDecimal HBTotalIncome = BigDecimal.ZERO;
        long HBTotalCount_IncomeNonZero = 0;
        long HBTotalCount_IncomeZero = 0;
        BigDecimal HBTotalWeeklyEligibleRentAmount = BigDecimal.ZERO;
        long HBTotalCount_WeeklyEligibleRentAmountNonZero = 0;
        long HBTotalCount_WeeklyEligibleRentAmountZero = 0;
        BigDecimal[] HBTotalIncomeByTT;
        HBTotalIncomeByTT = new BigDecimal[nTT];
        long[] HBTotalCount_IncomeByTTNonZero;
        HBTotalCount_IncomeByTTNonZero = new long[nTT];
        long[] HBTotalCount_IncomeByTTZero;
        HBTotalCount_IncomeByTTZero = new long[nTT];
        BigDecimal[] HBTotalByTTWeeklyEligibleRentAmount;
        HBTotalByTTWeeklyEligibleRentAmount = new BigDecimal[nTT];
        int[] HBTotalCount_ByTTWeeklyEligibleRentAmountNonZero;
        HBTotalCount_ByTTWeeklyEligibleRentAmountNonZero = new int[nTT];
        int[] HBTotalCount_ByTTWeeklyEligibleRentAmountZero;
        HBTotalCount_ByTTWeeklyEligibleRentAmountZero = new int[nTT];
        // CTB
        BigDecimal CTBTotalIncome = BigDecimal.ZERO;
        long CTBTotalCount_IncomeNonZero = 0;
        long CTBTotalCount_IncomeZero = 0;
        BigDecimal CTBTotalWeeklyEligibleRentAmount = BigDecimal.ZERO;
        long CTBTotalCount_WeeklyEligibleRentAmountNonZero = 0;
        long CTBTotalCount_WeeklyEligibleRentAmountZero = 0;
        BigDecimal[] CTBTotalIncomeByTT;
        CTBTotalIncomeByTT = new BigDecimal[nTT];
        long[] CTBTotalCount_IncomeByTTNonZero;
        CTBTotalCount_IncomeByTTNonZero = new long[nTT];
        long[] CTBTotalCount_IncomeByTTZero;
        CTBTotalCount_IncomeByTTZero = new long[nTT];
        BigDecimal[] CTBTotalByTTWeeklyEligibleRentAmount;
        CTBTotalByTTWeeklyEligibleRentAmount = new BigDecimal[nTT];
        int[] CTBTotalCount_ByTTWeeklyEligibleRentAmountNonZero;
        CTBTotalCount_ByTTWeeklyEligibleRentAmountNonZero = new int[nTT];
        int[] CTBTotalCount_ByTTWeeklyEligibleRentAmountZero;
        CTBTotalCount_ByTTWeeklyEligibleRentAmountZero = new int[nTT];
        for (int i = 0; i < nTT; i++) {
            // All
            AllTotalIncomeByTT[i] = BigDecimal.ZERO;
            AllTotalCount_IncomeByTTNonZero[i] = 0;
            AllTotalCount_IncomeByTTZero[i] = 0;
            AllTotalByTTWeeklyEligibleRentAmount[i] = BigDecimal.ZERO;
            AllTotalCount_ByTTWeeklyEligibleRentAmountNonZero[i] = 0;
            AllTotalCount_ByTTWeeklyEligibleRentAmountZero[i] = 0;
            // HB
            HBTotalIncomeByTT[i] = BigDecimal.ZERO;
            HBTotalCount_IncomeByTTNonZero[i] = 0;
            HBTotalCount_IncomeByTTZero[i] = 0;
            HBTotalByTTWeeklyEligibleRentAmount[i] = BigDecimal.ZERO;
            HBTotalCount_ByTTWeeklyEligibleRentAmountNonZero[i] = 0;
            HBTotalCount_ByTTWeeklyEligibleRentAmountZero[i] = 0;
            // CTB
            CTBTotalIncomeByTT[i] = BigDecimal.ZERO;
            CTBTotalCount_IncomeByTTNonZero[i] = 0;
            CTBTotalCount_IncomeByTTZero[i] = 0;
            CTBTotalByTTWeeklyEligibleRentAmount[i] = BigDecimal.ZERO;
            CTBTotalCount_ByTTWeeklyEligibleRentAmountNonZero[i] = 0;
            CTBTotalCount_ByTTWeeklyEligibleRentAmountZero[i] = 0;
        }
        Iterator<String> ite;
        if (doUnderOccupancy) {
            if (underOccupiedReportSetCouncil != null) {
                TreeMap<String, DW_UOReport_Record> map;
                map = underOccupiedReportSetCouncil.getMap();
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
                        // All
                        AllTotalIncome = AllTotalIncome.add(income);
                        AllTotalIncomeByTT[TT] = AllTotalIncomeByTT[TT].add(income);
                        if (income.compareTo(BigDecimal.ZERO) == 1) {
                            AllTotalCount_IncomeNonZero++;
                            AllTotalCount_IncomeByTTNonZero[TT]++;
                        } else {
                            AllTotalCount_IncomeZero++;
                            AllTotalCount_IncomeByTTZero[TT]++;
                        }
                        tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                        AllTotalWeeklyEligibleRentAmount = AllTotalWeeklyEligibleRentAmount.add(tBD);
                        AllTotalByTTWeeklyEligibleRentAmount[TT] = AllTotalByTTWeeklyEligibleRentAmount[TT].add(tBD);
                        if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                            AllTotalCount_WeeklyEligibleRentAmountNonZero++;
                            AllTotalCount_ByTTWeeklyEligibleRentAmountNonZero[TT]++;
                        } else {
                            AllTotalCount_WeeklyEligibleRentAmountZero++;
                            AllTotalCount_ByTTWeeklyEligibleRentAmountZero[TT]++;
                        }
                        // HB
                        if (TT == 1 || TT == 2 || TT == 3 || TT == 4 || TT == 6 || TT == 8 || TT == 9) {
                            HBTotalIncome = HBTotalIncome.add(income);
                            HBTotalIncomeByTT[TT] = HBTotalIncomeByTT[TT].add(income);
                            if (income.compareTo(BigDecimal.ZERO) == 1) {
                                HBTotalCount_IncomeNonZero++;
                                HBTotalCount_IncomeByTTNonZero[TT]++;
                            } else {
                                HBTotalCount_IncomeZero++;
                                HBTotalCount_IncomeByTTZero[TT]++;
                            }
                            tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                            HBTotalWeeklyEligibleRentAmount = HBTotalWeeklyEligibleRentAmount.add(tBD);
                            if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                                HBTotalCount_WeeklyEligibleRentAmountNonZero++;
                                HBTotalCount_ByTTWeeklyEligibleRentAmountNonZero[TT]++;
                            } else {
                                HBTotalCount_WeeklyEligibleRentAmountZero++;
                                HBTotalCount_ByTTWeeklyEligibleRentAmountZero[TT]++;
                            }
                        }
                        // CTB
                        if (TT == 5 || TT == 7) {
                            CTBTotalIncome = CTBTotalIncome.add(income);
                            CTBTotalIncomeByTT[TT] = CTBTotalIncomeByTT[TT].add(income);
                            if (income.compareTo(BigDecimal.ZERO) == 1) {
                                CTBTotalCount_IncomeNonZero++;
                                CTBTotalCount_IncomeByTTNonZero[TT]++;
                            } else {
                                CTBTotalCount_IncomeZero++;
                                CTBTotalCount_IncomeByTTZero[TT]++;
                            }
                            tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                            CTBTotalWeeklyEligibleRentAmount = CTBTotalWeeklyEligibleRentAmount.add(tBD);
                            if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                                CTBTotalCount_WeeklyEligibleRentAmountNonZero++;
                                CTBTotalCount_ByTTWeeklyEligibleRentAmountNonZero[TT]++;
                            } else {
                                CTBTotalCount_WeeklyEligibleRentAmountZero++;
                                CTBTotalCount_ByTTWeeklyEligibleRentAmountZero[TT]++;
                            }
                        }
                    }
                }
            }
            if (underOccupiedReportSetRSL != null) {
                TreeMap<String, DW_UOReport_Record> map;
                map = underOccupiedReportSetRSL.getMap();
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
                        // All
                        AllTotalIncome = AllTotalIncome.add(income);
                        AllTotalIncomeByTT[TT] = AllTotalIncomeByTT[TT].add(income);
                        if (income.compareTo(BigDecimal.ZERO) == 1) {
                            AllTotalCount_IncomeNonZero++;
                            AllTotalCount_IncomeByTTNonZero[TT]++;
                        } else {
                            AllTotalCount_IncomeZero++;
                            AllTotalCount_IncomeByTTZero[TT]++;
                        }
                        tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                        AllTotalWeeklyEligibleRentAmount = AllTotalWeeklyEligibleRentAmount.add(tBD);
                        AllTotalByTTWeeklyEligibleRentAmount[TT] = AllTotalByTTWeeklyEligibleRentAmount[TT].add(tBD);
                        if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                            AllTotalCount_WeeklyEligibleRentAmountNonZero++;
                            AllTotalCount_ByTTWeeklyEligibleRentAmountNonZero[TT]++;
                        } else {
                            AllTotalCount_WeeklyEligibleRentAmountZero++;
                            AllTotalCount_ByTTWeeklyEligibleRentAmountZero[TT]++;
                        }
                        // HB
                        if (TT == 1 || TT == 2 || TT == 3 || TT == 4 || TT == 6 || TT == 8 || TT == 9) {
                            HBTotalIncome = HBTotalIncome.add(income);
                            HBTotalIncomeByTT[TT] = HBTotalIncomeByTT[TT].add(income);
                            if (income.compareTo(BigDecimal.ZERO) == 1) {
                                HBTotalCount_IncomeNonZero++;
                                HBTotalCount_IncomeByTTNonZero[TT]++;
                            } else {
                                HBTotalCount_IncomeZero++;
                                HBTotalCount_IncomeByTTZero[TT]++;
                            }
                            tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                            HBTotalWeeklyEligibleRentAmount = HBTotalWeeklyEligibleRentAmount.add(tBD);
                            if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                                HBTotalCount_WeeklyEligibleRentAmountNonZero++;
                                HBTotalCount_ByTTWeeklyEligibleRentAmountNonZero[TT]++;
                            } else {
                                HBTotalCount_WeeklyEligibleRentAmountZero++;
                                HBTotalCount_ByTTWeeklyEligibleRentAmountZero[TT]++;
                            }
                        }
                        // CTB
                        if (TT == 5 || TT == 7) {
                            CTBTotalIncome = CTBTotalIncome.add(income);
                            CTBTotalIncomeByTT[TT] = CTBTotalIncomeByTT[TT].add(income);
                            if (income.compareTo(BigDecimal.ZERO) == 1) {
                                CTBTotalCount_IncomeNonZero++;
                                CTBTotalCount_IncomeByTTNonZero[TT]++;
                            } else {
                                CTBTotalCount_IncomeZero++;
                                CTBTotalCount_IncomeByTTZero[TT]++;
                            }
                            tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                            CTBTotalWeeklyEligibleRentAmount = CTBTotalWeeklyEligibleRentAmount.add(tBD);
                            if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                                CTBTotalCount_WeeklyEligibleRentAmountNonZero++;
                                CTBTotalCount_ByTTWeeklyEligibleRentAmountNonZero[TT]++;
                            } else {
                                CTBTotalCount_WeeklyEligibleRentAmountZero++;
                                CTBTotalCount_ByTTWeeklyEligibleRentAmountZero[TT]++;
                            }
                        }
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
                // All
                AllTotalIncome = AllTotalIncome.add(income);
                AllTotalIncomeByTT[TT] = AllTotalIncomeByTT[TT].add(income);
                if (income.compareTo(BigDecimal.ZERO) == 1) {
                    AllTotalCount_IncomeNonZero++;
                    AllTotalCount_IncomeByTTNonZero[TT]++;
                } else {
                    AllTotalCount_IncomeZero++;
                    AllTotalCount_IncomeByTTZero[TT]++;
                }
                tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                AllTotalWeeklyEligibleRentAmount = AllTotalWeeklyEligibleRentAmount.add(tBD);
                AllTotalByTTWeeklyEligibleRentAmount[TT] = AllTotalByTTWeeklyEligibleRentAmount[TT].add(tBD);
                if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                    AllTotalCount_WeeklyEligibleRentAmountNonZero++;
                    AllTotalCount_ByTTWeeklyEligibleRentAmountNonZero[TT]++;
                } else {
                    AllTotalCount_WeeklyEligibleRentAmountZero++;
                    AllTotalCount_ByTTWeeklyEligibleRentAmountZero[TT]++;
                }
                // HB
                if (TT == 1 || TT == 2 || TT == 3 || TT == 4 || TT == 6 || TT == 8 || TT == 9) {
                    HBTotalIncome = HBTotalIncome.add(income);
                    HBTotalIncomeByTT[TT] = HBTotalIncomeByTT[TT].add(income);
                    if (income.compareTo(BigDecimal.ZERO) == 1) {
                        HBTotalCount_IncomeNonZero++;
                        HBTotalCount_IncomeByTTNonZero[TT]++;
                    } else {
                        HBTotalCount_IncomeZero++;
                        HBTotalCount_IncomeByTTZero[TT]++;
                    }
                    tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                    HBTotalWeeklyEligibleRentAmount = HBTotalWeeklyEligibleRentAmount.add(tBD);
                    if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                        HBTotalCount_WeeklyEligibleRentAmountNonZero++;
                        HBTotalCount_ByTTWeeklyEligibleRentAmountNonZero[TT]++;
                    } else {
                        HBTotalCount_WeeklyEligibleRentAmountZero++;
                        HBTotalCount_ByTTWeeklyEligibleRentAmountZero[TT]++;
                    }
                }
                // CTB
                if (TT == 5 || TT == 7) {
                    CTBTotalIncome = CTBTotalIncome.add(income);
                    CTBTotalIncomeByTT[TT] = CTBTotalIncomeByTT[TT].add(income);
                    if (income.compareTo(BigDecimal.ZERO) == 1) {
                        CTBTotalCount_IncomeNonZero++;
                        CTBTotalCount_IncomeByTTNonZero[TT]++;
                    } else {
                        CTBTotalCount_IncomeZero++;
                        CTBTotalCount_IncomeByTTZero[TT]++;
                    }
                    tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                    CTBTotalWeeklyEligibleRentAmount = CTBTotalWeeklyEligibleRentAmount.add(tBD);
                    if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                        CTBTotalCount_WeeklyEligibleRentAmountNonZero++;
                        CTBTotalCount_ByTTWeeklyEligibleRentAmountNonZero[TT]++;
                    } else {
                        CTBTotalCount_WeeklyEligibleRentAmountZero++;
                        CTBTotalCount_ByTTWeeklyEligibleRentAmountZero[TT]++;
                    }
                }
            }
        }
        // All
        tBD = BigDecimal.valueOf(AllTotalCount_IncomeNonZero);
        BigDecimal zBD;
        zBD = BigDecimal.valueOf(AllTotalCount_IncomeZero);
        result.put(Summary.sAllTotalIncome, AllTotalIncome);
        result.put(Summary.sAllTotalCount_IncomeNonZero, tBD);
        result.put(Summary.sAllTotalCount_IncomeZero, zBD);
        if (tBD.compareTo(BigDecimal.ZERO) == 1) {
            result.put(
                    Summary.sAllAverageIncome,
                    Generic_BigDecimal.divideRoundIfNecessary(
                            AllTotalIncome, tBD,
                            2, RoundingMode.HALF_UP));
        } else {
            result.put(
                    Summary.sAllAverageIncome,
                    BigDecimal.ZERO);
        }
        tBD = BigDecimal.valueOf(
                AllTotalCount_WeeklyEligibleRentAmountNonZero);
        zBD = BigDecimal.valueOf(
                AllTotalCount_WeeklyEligibleRentAmountZero);
        result.put(Summary.sAllTotalWeeklyEligibleRentAmount,
                AllTotalWeeklyEligibleRentAmount);
        result.put(Summary.sAllTotalCount_WeeklyEligibleRentAmountNonZero,
                tBD);
        result.put(Summary.sAllTotalCount_WeeklyEligibleRentAmountZero,
                zBD);
        if (tBD.compareTo(BigDecimal.ZERO) == 1) {
            result.put(
                    Summary.sAllAverageWeeklyEligibleRentAmount,
                    Generic_BigDecimal.divideRoundIfNecessary(
                            AllTotalWeeklyEligibleRentAmount,
                            tBD,
                            2, RoundingMode.HALF_UP));
        }
        for (int i = 0; i < nTT; i++) {
            // Income
            result.put(Summary.sAllTotalIncomeTT[i],
                    AllTotalIncomeByTT[i]);
            tBD = BigDecimal.valueOf(
                    AllTotalCount_IncomeByTTNonZero[i]);
            zBD = BigDecimal.valueOf(
                    AllTotalCount_IncomeByTTZero[i]);
            result.put(Summary.sAllTotalCount_IncomeNonZeroTT[i], tBD);
            result.put(Summary.sAllTotalCount_IncomeZeroTT[i], zBD);
            if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                result.put(Summary.sAllAverageIncomeTT[i],
                        Generic_BigDecimal.divideRoundIfNecessary(
                                AllTotalIncomeByTT[i],
                                tBD,
                                2, RoundingMode.HALF_UP));
            } else {
                result.put(Summary.sAllAverageIncomeTT[i],
                        BigDecimal.ZERO);
            }
            // Rent
            result.put(Summary.sAllTotalWeeklyEligibleRentAmountTT[i],
                    AllTotalByTTWeeklyEligibleRentAmount[i]);
            tBD = BigDecimal.valueOf(
                    AllTotalCount_ByTTWeeklyEligibleRentAmountNonZero[i]);
            zBD = BigDecimal.valueOf(
                    AllTotalCount_ByTTWeeklyEligibleRentAmountZero[i]);
            result.put(Summary.sAllTotalCount_WeeklyEligibleRentAmountNonZeroTT[i],
                    tBD);
            result.put(Summary.sAllTotalCount_WeeklyEligibleRentAmountZeroTT[i],
                    zBD);
            if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                result.put(Summary.sAllAverageWeeklyEligibleRentAmountTT[i],
                        Generic_BigDecimal.divideRoundIfNecessary(
                                AllTotalByTTWeeklyEligibleRentAmount[i],
                                tBD,
                                2, RoundingMode.HALF_UP));
            } else {
                result.put(Summary.sAllAverageWeeklyEligibleRentAmountTT[i],
                        BigDecimal.ZERO);
            }
        }
        // HB
        tBD = BigDecimal.valueOf(HBTotalCount_IncomeNonZero);
        zBD = BigDecimal.valueOf(HBTotalCount_IncomeZero);
        result.put(Summary.sHBTotalIncome, HBTotalIncome);
        result.put(Summary.sHBTotalCount_IncomeNonZero, tBD);
        result.put(Summary.sHBTotalCount_IncomeZero, zBD);
        if (tBD.compareTo(BigDecimal.ZERO) == 1) {
            result.put(
                    Summary.sHBAverageIncome,
                    Generic_BigDecimal.divideRoundIfNecessary(
                            HBTotalIncome, tBD,
                            2, RoundingMode.HALF_UP));
        } else {
            result.put(
                    Summary.sHBAverageIncome,
                    BigDecimal.ZERO);
        }
        tBD = BigDecimal.valueOf(
                HBTotalCount_WeeklyEligibleRentAmountNonZero);
        zBD = BigDecimal.valueOf(
                HBTotalCount_WeeklyEligibleRentAmountZero);
        result.put(Summary.sHBTotalWeeklyEligibleRentAmount,
                HBTotalWeeklyEligibleRentAmount);
        result.put(Summary.sHBTotalCount_WeeklyEligibleRentAmountNonZero,
                tBD);
        result.put(Summary.sHBTotalCount_WeeklyEligibleRentAmountZero,
                zBD);
        if (tBD.compareTo(BigDecimal.ZERO) == 1) {
            result.put(
                    Summary.sHBAverageWeeklyEligibleRentAmount,
                    Generic_BigDecimal.divideRoundIfNecessary(
                            HBTotalWeeklyEligibleRentAmount,
                            tBD,
                            2, RoundingMode.HALF_UP));
        }
        for (int i = 0; i < nTT; i++) {
            // Income
            result.put(Summary.sHBTotalIncomeTT[i],
                    HBTotalIncomeByTT[i]);
            tBD = BigDecimal.valueOf(
                    HBTotalCount_IncomeByTTNonZero[i]);
            zBD = BigDecimal.valueOf(
                    HBTotalCount_IncomeByTTZero[i]);
            result.put(Summary.sHBTotalCount_IncomeNonZeroTT[i], tBD);
            result.put(Summary.sHBTotalCount_IncomeZeroTT[i], zBD);
            if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                result.put(Summary.sHBAverageIncomeTT[i],
                        Generic_BigDecimal.divideRoundIfNecessary(
                                HBTotalIncomeByTT[i],
                                tBD,
                                2, RoundingMode.HALF_UP));
            } else {
                result.put(Summary.sHBAverageIncomeTT[i],
                        BigDecimal.ZERO);
            }
            // Rent
            result.put(Summary.sHBTotalWeeklyEligibleRentAmountTT[i],
                    HBTotalByTTWeeklyEligibleRentAmount[i]);
            tBD = BigDecimal.valueOf(
                    HBTotalCount_ByTTWeeklyEligibleRentAmountNonZero[i]);
            zBD = BigDecimal.valueOf(
                    HBTotalCount_ByTTWeeklyEligibleRentAmountNonZero[i]);
            result.put(Summary.sHBTotalCount_WeeklyEligibleRentAmountNonZeroTT[i],
                    tBD);
            result.put(Summary.sHBTotalCount_WeeklyEligibleRentAmountZeroTT[i],
                    zBD);
            if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                result.put(Summary.sHBAverageWeeklyEligibleRentAmountTT[i],
                        Generic_BigDecimal.divideRoundIfNecessary(
                                HBTotalByTTWeeklyEligibleRentAmount[i],
                                tBD,
                                2, RoundingMode.HALF_UP));
            } else {
                result.put(Summary.sHBAverageWeeklyEligibleRentAmountTT[i],
                        BigDecimal.ZERO);
            }
        }
        // CTB
        tBD = BigDecimal.valueOf(CTBTotalCount_IncomeNonZero);
        zBD = BigDecimal.valueOf(CTBTotalCount_IncomeZero);
        result.put(Summary.sCTBTotalIncome, CTBTotalIncome);
        result.put(Summary.sCTBTotalCount_IncomeNonZero, tBD);
        result.put(Summary.sCTBTotalCount_IncomeZero, zBD);
        if (tBD.compareTo(BigDecimal.ZERO) == 1) {
            result.put(
                    Summary.sCTBAverageIncome,
                    Generic_BigDecimal.divideRoundIfNecessary(
                            CTBTotalIncome, tBD,
                            2, RoundingMode.HALF_UP));
        } else {
            result.put(
                    Summary.sCTBAverageIncome,
                    BigDecimal.ZERO);
        }
        tBD = BigDecimal.valueOf(CTBTotalCount_WeeklyEligibleRentAmountNonZero);
        zBD = BigDecimal.valueOf(CTBTotalCount_WeeklyEligibleRentAmountZero);
        result.put(
                Summary.sCTBTotalWeeklyEligibleRentAmount,
                CTBTotalWeeklyEligibleRentAmount);
        result.put(
                Summary.sCTBTotalCount_WeeklyEligibleRentAmountNonZero,
                tBD);
        result.put(
                Summary.sCTBTotalCount_WeeklyEligibleRentAmountZero,
                zBD);
        if (tBD.compareTo(BigDecimal.ZERO) == 1) {
            result.put(
                    Summary.sCTBAverageWeeklyEligibleRentAmount,
                    Generic_BigDecimal.divideRoundIfNecessary(
                            CTBTotalWeeklyEligibleRentAmount,
                            tBD,
                            2, RoundingMode.HALF_UP));
        }
        for (int i = 0; i < nTT; i++) {
            // Income
            result.put(
                    Summary.sCTBTotalIncomeTT[i],
                    CTBTotalIncomeByTT[i]);
            tBD = BigDecimal.valueOf(CTBTotalCount_IncomeByTTNonZero[i]);
            zBD = BigDecimal.valueOf(CTBTotalCount_IncomeByTTZero[i]);
            result.put(Summary.sCTBTotalCount_IncomeNonZeroTT[i], tBD);
            result.put(Summary.sCTBTotalCount_IncomeZeroTT[i], zBD);
            if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                result.put(
                        Summary.sCTBAverageIncomeTT[i],
                        Generic_BigDecimal.divideRoundIfNecessary(
                                CTBTotalIncomeByTT[i],
                                tBD,
                                2, RoundingMode.HALF_UP));
            } else {
                result.put(
                        Summary.sCTBAverageIncomeTT[i],
                        BigDecimal.ZERO);
            }
            // Rent
            result.put(
                    Summary.sCTBTotalWeeklyEligibleRentAmountTT[i],
                    CTBTotalByTTWeeklyEligibleRentAmount[i]);
            tBD = BigDecimal.valueOf(
                    CTBTotalCount_ByTTWeeklyEligibleRentAmountNonZero[i]);
            zBD = BigDecimal.valueOf(
                    CTBTotalCount_ByTTWeeklyEligibleRentAmountZero[i]);
            result.put(
                    Summary.sCTBTotalCount_WeeklyEligibleRentAmountNonZeroTT[i],
                    tBD);
            result.put(
                    Summary.sCTBTotalCount_WeeklyEligibleRentAmountZeroTT[i],
                    zBD);
            if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                result.put(
                        Summary.sCTBAverageWeeklyEligibleRentAmountTT[i],
                        Generic_BigDecimal.divideRoundIfNecessary(
                                CTBTotalByTTWeeklyEligibleRentAmount[i],
                                tBD,
                                2, RoundingMode.HALF_UP));
            } else {
                result.put(
                        Summary.sCTBAverageWeeklyEligibleRentAmountTT[i],
                        BigDecimal.ZERO);
            }
        }
        Generic_StaticIO.writeObject(result, tIncomeAndRentSummaryFile);
        return result;
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
        //if (aDRecord.getHousingBenefitClaimReferenceNumber().length() > 2) {
        if (DW_SHBE_Handler.isHBClaim(aDRecord)) {
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
        //result += D_Record.getNumberOfChildDependents();
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
            result = (HashMap<String, DW_ID>) Generic_StaticIO.readObject(f);
        } else {
            result = new HashMap<String, DW_ID>();
        }
        return result;
    }

    public static HashMap<String, DW_ID> getNINOToDW_IDLookup(
            File f) {
        if (NINOToDW_IDLookup == null) {
            NINOToDW_IDLookup = getStringToDW_IDLookup(f);
        }
        return NINOToDW_IDLookup;
    }

    public static HashMap<String, DW_ID> getNINOToDW_IDLookup() {
        return getNINOToDW_IDLookup(getNINOToDW_IDLookupFile());
    }

    public static HashMap<String, DW_ID> getDOBToDW_IDLookup(
            File f) {
        if (DOBToDW_IDLookup == null) {
            DOBToDW_IDLookup = getStringToDW_IDLookup(f);
        }
        return DOBToDW_IDLookup;
    }

    public static HashMap<String, DW_ID> getDOBToDW_IDLookup() {
        return getDOBToDW_IDLookup(getDOBToDW_IDLookupFile());
    }

    public static HashMap<DW_ID, String> getDW_IDToNINOLookup(
            File f) {
        if (DW_IDToNINOLookup == null) {
            DW_IDToNINOLookup = getDW_IDToStringLookup(f);
        }
        return DW_IDToNINOLookup;
    }

    public static HashMap<DW_ID, String> getDW_IDToNINOLookup() {
        return getDW_IDToStringLookup(getDW_IDToNINOLookupFile());
    }

    public static HashMap<DW_ID, String> getDW_IDToDOBLookup(
            File f) {
        if (DW_IDToDOBLookup == null) {
            DW_IDToDOBLookup = getDW_IDToStringLookup(f);
        }
        return DW_IDToDOBLookup;
    }

    public static HashMap<DW_ID, String> getDW_IDToDOBLookup() {
        return getDW_IDToStringLookup(getDW_IDToDOBLookupFile());
    }

    public static HashMap<String, DW_ID> getPostcodeToPostcodeIDLookup(
            File f) {
        if (PostcodeToPostcodeIDLookup == null) {
            PostcodeToPostcodeIDLookup = getStringToDW_IDLookup(f);
        }
        return PostcodeToPostcodeIDLookup;
    }

    public static HashMap<String, DW_ID> getPostcodeToPostcodeIDLookup() {
        return getPostcodeToPostcodeIDLookup(getPostcodeToPostcodeIDLookupFile());
    }

    public static HashMap<DW_ID, String> getPostcodeIDToPostcodeLookup(
            File f) {
        if (PostcodeIDToPostcodeLookup == null) {
            PostcodeIDToPostcodeLookup = getDW_IDToStringLookup(f);
        }
        return PostcodeIDToPostcodeLookup;
    }

    public static HashMap<DW_ID, String> getPostcodeIDToPostcodeLookup() {
        File f;
        f = getPostcodeIDToPostcodeLookupFile();
        return getPostcodeIDToPostcodeLookup(f);
    }

    public static HashMap<DW_PersonID, DW_ID> getDW_PersonIDToDW_IDLookup(
            File f) {
        if (DW_PersonIDToDW_IDLookup == null) {
            if (f.exists()) {
                DW_PersonIDToDW_IDLookup = (HashMap<DW_PersonID, DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                DW_PersonIDToDW_IDLookup = new HashMap<DW_PersonID, DW_ID>();
            }
        }
        return DW_PersonIDToDW_IDLookup;
    }

    public static HashMap<DW_PersonID, DW_ID> getDW_PersonIDToDW_IDLookup() {
        return DW_SHBE_Handler.getDW_PersonIDToDW_IDLookup(getDW_PersonIDToDW_IDLookupFile());
    }

    public static HashMap<DW_ID, DW_PersonID> getDW_IDToDW_PersonIDLookup(
            File f) {
        if (DW_IDToDW_PersonIDLookup == null) {
            if (f.exists()) {
                DW_IDToDW_PersonIDLookup = (HashMap<DW_ID, DW_PersonID>) Generic_StaticIO.readObject(f);
            } else {
                DW_IDToDW_PersonIDLookup = new HashMap<DW_ID, DW_PersonID>();
            }
        }
        return DW_IDToDW_PersonIDLookup;
    }

    public static HashMap<DW_ID, DW_PersonID> getDW_IDToDW_PersonIDLookup() {
        return getDW_IDToDW_PersonIDLookup(getDW_IDToDW_PersonIDLookupFile());
    }

    public static File getPostcodeToPostcodeIDLookupFile() {
        File result;
        String filename = "PostcodeToPostcodeID_HashMap_String__DW_ID.thisFile";
        result = new File(
                DW_Files.getGeneratedSHBEDir(),
                filename);
        return result;
    }

    public static File getPostcodeIDToPostcodeLookupFile() {
        File result;
        String filename = "PostcodeIDToPostcode_HashMap_DW_ID__String.thisFile";
        result = new File(
                DW_Files.getGeneratedSHBEDir(),
                filename);
        return result;
    }

    public static File getNINOToDW_IDLookupFile() {
        File result;
        String filename = "NINOToID_HashMap_String__DW_ID.thisFile";
        result = new File(
                DW_Files.getGeneratedSHBEDir(),
                filename);
        return result;
    }

    public static File getDOBToDW_IDLookupFile() {
        File result;
        String filename = "DOBToID_HashMap_String__DW_ID.thisFile";
        result = new File(
                DW_Files.getGeneratedSHBEDir(),
                filename);
        return result;
    }

    public static File getDW_IDToNINOLookupFile() {
        File result;
        String filename = "IDToNINO_HashMap_DW_ID__String.thisFile";
        result = new File(
                DW_Files.getGeneratedSHBEDir(),
                filename);
        return result;
    }

    public static File getDW_IDToDOBLookupFile() {
        File result;
        String filename = "IDToDOB_HashMap_DW_ID__String.thisFile";
        result = new File(
                DW_Files.getGeneratedSHBEDir(),
                filename);
        return result;
    }

    public static File getDW_PersonIDToDW_IDLookupFile() {
        File result;
        String filename = "PersonIDToID_HashMap_DW_PersonID__DW_ID.thisFile";
        result = new File(
                DW_Files.getGeneratedSHBEDir(),
                filename);
        return result;
    }

    /**
     *
     * @return
     */
    public static File getDW_IDToDW_PersonIDLookupFile() {
        File result;
        String filename = "IDToPersonID_HashMap_DW_ID__DW_PersonID.thisFile";
        result = new File(
                DW_Files.getGeneratedSHBEDir(),
                filename);
        return result;
    }

//    /**
//     * 
//     * @param paymentType
//     * @return 
//     */
//    public static File getDW_PersonIDToDW_IDLookupFile(String paymentType) {
//        File result;
//        String filename = "DW_PersonIDToDW_ID_HashMap_DW_PersonID__DW_ID.thisFile";
//        result = new File(
//                DW_Files.getGeneratedSHBEDir(paymentType),
//                filename);
//        return result;
//    }
//    /**
//     * @param paymentType
//     * @return 
//     */
//    public static File getDW_IDToDW_PersonIDLookupFile(String paymentType) {
//        File result;
//        String filename = "DW_IDToDW_PersonID_HashMap_DW_ID__DW_PersonID.thisFile";
//        result = new File(
//                DW_Files.getGeneratedSHBEDir(paymentType),
//                filename);
//        return result;
//    }
    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getDRecordsFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "DRecords_TreeMap_String__DW_SHBE_Record.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getSRecordsWithoutDRecordsFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "SRecordsWithoutDRecords_TreeMap_String__DW_SHBE_S_Record.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getSRecordIDToCTBRefFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "SRecordIDToCTBRef_HashMap_DW_ID__String.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getClaimantIDsFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "ClaimantIDs_HashSet_DW_PersonID.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getPartnerIDsFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "PartnerIDs_HashSet_DW_PersonID.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getDependentIDsFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "DependentIDs_HashSet_DW_PersonID.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getNonDependentIDsFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "NonDependentIDs_HashSet_DW_PersonID.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getAllHouseholdIDsFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "AllHouseholdIDs_HashSet_DW_PersonID.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getPairedClaimantIDsFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "PairedClaimantIDs_HashSet_DW_PersonID.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getClaimantIDToRecordIDLookupFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "ClaimantIDToRecordIDLookup_HashMap_DW_ID__Long.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getClaimantIDToPostcodeLookupFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "ClaimantIDToPostcodeLookup_HashMap_DW_ID__String.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getClaimantIDToTenancyTypeLookupFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "ClaimantIDToTenancyTypeLookup_HashMap_DW_ID__Integer.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getCTBRefToClaimantIDLookupFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "CTBRefToClaimantIDLookup_HashMap_String__DW_ID.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getClaimantIDToCTBRefLookupFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "ClaimantIDToCTBRefLookup_HashMap_DW_ID__String.thisFile";
        result = getFile(
                paymentType,
                filename,
                partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getLoadSummaryFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "LoadSummary_HashMap_String__Integer.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getClaimantIDPostcodeSetFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "ClaimantIDPostcode_HashSet_ID_Postcode.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getClaimantIDTenancyTypeSetFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "ClaimantIDTenancyType_HashSet_ID_TenancyType.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getClaimantIDTenancyPostcodeTypeSetFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "ClaimantIDTenancyTypePostcodeID_HashSet_ID_TenancyType_PostcodeID.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @return
     */
    public static File getRecordIDsNotLoadedFile(
            String paymentType,
            String filename) {
        File result;
        String partFilename = "RecordIDsNotLoaded_TreeSet_Long.thisFile";
        result = getFile(paymentType, filename, partFilename);
        return result;
    }

    /**
     * @param paymentType
     * @param filename
     * @param doUnderOccupancy
     * @param doCouncil
     * @param doRSL
     * @return
     */
    public static File getIncomeAndRentSummaryFile(
            String paymentType,
            String filename,
            boolean doUnderOccupancy,
            boolean doCouncil,
            boolean doRSL
    ) {
        File result;
        String partFilename;
        if (doUnderOccupancy) {
            if (doCouncil) {
                if (doRSL) {
                    partFilename = "IncomeAndRentSummaryUOAll_HashMap_String__BigDecimal.thisFile";
                } else {
                    partFilename = "IncomeAndRentSummaryUOCouncil_HashMap_String__BigDecimal.thisFile";
                }
            } else {
                partFilename = "IncomeAndRentSummaryUORSL_HashMap_String__BigDecimal.thisFile";
            }
        } else {
            partFilename = "IncomeAndRentSummary_HashMap_String__BigDecimal.thisFile";
        }
        result = getFile(paymentType, filename, partFilename);
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

    public static int getNumberOfClaimantsEthnicGroupsGrouped() {
        return 10;
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
        ArrayList<Integer> omitAll;
        omitKey = sAll;
        omitAll = new ArrayList<Integer>();
        result.put(omitKey, omitAll);
        omitKey = sYearly;
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
        omitYearly.add(47);
        result.put(omitKey, omitYearly);
        omitKey = s6Monthly;
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
        omitKey = s3Monthly;
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
        omitKey = sMonthly;
        ArrayList<Integer> omitMonthly;
        omitMonthly = new ArrayList<Integer>();
        for (int i = 0; i < 14; i++) {
            omitMonthly.add(i);
        }
        result.put(omitKey, omitMonthly);
        omitKey = sMonthlyUO;
        ArrayList<Integer> omitMonthlyUO;
        omitMonthlyUO = new ArrayList<Integer>();
        for (int i = 0; i < 17; i++) {
            omitMonthlyUO.add(i);
        }
        result.put(omitKey, omitMonthlyUO);
        return result;
    }

    public static DW_PersonID getClaimantDW_PersonID(DW_SHBE_D_Record D_Record) {
        DW_PersonID result;
        DW_ID NINO_ID;
        NINO_ID = getNINOToDW_IDLookup().get(D_Record.getClaimantsNationalInsuranceNumber());
        DW_ID DOB_ID;
        DOB_ID = getDOBToDW_IDLookup().get(D_Record.getClaimantsDateOfBirth());
        result = new DW_PersonID(NINO_ID, DOB_ID);
        return result;
    }

    /**
     * @return Loads SHBE collections from a file in directory.
     *
     * @param handler
     * @param directory
     * @param filename
     * @param paymentType
     */
    public DW_SHBE_Collection loadInputData(
            DW_SHBE_CollectionHandler handler,
            File directory,
            String filename,
            String paymentType) {
        DW_SHBE_Collection result;
        result = new DW_SHBE_Collection(
                handler.nextID,
                handler,
                directory,
                filename,
                paymentType);
//        Generic_StaticIO.writeObject(
//                DW_PersonIDToDW_IDLookup,
//                getDW_PersonIDToDW_IDLookupFile());
//        Generic_StaticIO.writeObject(
//                DW_IDToDW_PersonIDLookup,
//                getDW_IDToDW_PersonIDLookupFile());
//        Generic_StaticIO.writeObject(
//                PostcodeToPostcodeIDLookup,
//                getPostcodeToPostcodeIDLookupFile());
//        Generic_StaticIO.writeObject(
//                PostcodeIDToPostcodeLookup,
//                getPostcodeIDToPostcodeLookupFile());
        return result;
    }

    /**
     *
     * @param D_Record
     */
    public static String getClaimantsAge(
            String yM3,
            DW_SHBE_D_Record D_Record) {
        String result;
        String[] syM3;
        syM3 = yM3.split("_");
        result = getClaimantsAge(syM3[0], syM3[1], D_Record);
        return result;
    }
    
    /**
     *
     * @param D_Record
     */
    public static String getClaimantsAge(
            String year,
            String month,
            DW_SHBE_D_Record D_Record) {
        String result;
        String DoB = D_Record.getClaimantsDateOfBirth();
        result = getAge(year, month, DoB);
        return result;
    }

    /**
     *
     * @param D_Record
     */
    public static String getPartnersAge(
            String year,
            String month,
            DW_SHBE_D_Record D_Record) {
        String result;
        String DoB = D_Record.getPartnersDateOfBirth();
        result = getAge(year, month, DoB);
        return result;
    }

    public static String getAge(
            String year,
            String month,
            String DoB) {
        if (DoB == null) {
            return "";
        }
        if (DoB.isEmpty()) {
            return DoB;
        }
        String result;
        String[] sDoB = DoB.split("/");
        Generic_Time tDoB;
        tDoB = new Generic_Time(
                Integer.valueOf(sDoB[0]),
                Integer.valueOf(sDoB[1]),
                Integer.valueOf(sDoB[2]));
        Generic_Time tNow;
        tNow = new Generic_Time(
                Integer.valueOf(0),
                Integer.valueOf(month),
                Integer.valueOf(year));
        result = Integer.toString(Generic_Time.getAgeInYears(tNow, tDoB));
        return result;
    }

    /**
     *
     * @param D_Record
     * @return true iff there is any disability awards in the household of
     * D_Record.
     */
    public static boolean getDisability(DW_SHBE_D_Record D_Record) {
        // Disability
        int DisabilityPremiumAwarded = D_Record.getDisabilityPremiumAwarded();
        int SevereDisabilityPremiumAwarded = D_Record.getSevereDisabilityPremiumAwarded();
        int DisabledChildPremiumAwarded = D_Record.getDisabledChildPremiumAwarded();
        int EnhancedDisabilityPremiumAwarded = D_Record.getEnhancedDisabilityPremiumAwarded();
        // General Household Disability Flag
        if (DisabilityPremiumAwarded == 1
                || SevereDisabilityPremiumAwarded == 1
                || DisabledChildPremiumAwarded == 1
                || EnhancedDisabilityPremiumAwarded == 1) {
            return true;
        } else {
            return false;
        }
    }
    
    public static int getEthnicityGroup(DW_SHBE_D_Record D_Record) {
        int claimantsEthnicGroup = D_Record.getClaimantsEthnicGroup();
        switch (claimantsEthnicGroup) {
            case 1:
                return 1;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 3;
            case 6:
                return 4;
            case 7:
                return 5;
            case 8:
                return 6;
            case 9:
                return 6;
            case 10:
                return 6;
            case 11:
                return 6;
            case 12:
                return 7;
            case 13:
                return 7;
            case 14:
                return 7;
            case 15:
                return 8;
            case 16:
                return 9;
        }
        return 0;        
    }
    
    public static String getEthnicityName(DW_SHBE_D_Record D_Record) {
        int claimantsEthnicGroup = D_Record.getClaimantsEthnicGroup();
        switch (claimantsEthnicGroup) {
            case 1:
                return "White: British";
            case 2:
                return "White: Irish";
            case 3:
                return "White: Any Other";
            case 4:
                return "Mixed: White and Black Caribbean";
            case 5:
                return "Mixed: White and Black African";
            case 6:
                return "Mixed: White and Asian";
            case 7:
                return "Mixed: Any Other";
            case 8:
                return "Asian or Asian British: Indian";
            case 9:
                return "Asian or Asian British: Pakistani";
            case 10:
                return "Asian or Asian British: Bangladeshi";
            case 11:
                return "Asian or Asian British: Any Other";
            case 12:
                return "Black or Black British: Caribbean";
            case 13:
                return "Black or Black British: African";
            case 14:
                return "Black or Black British: Any Other";
            case 15:
                return "Chinese";
            case 16:
                return "Any Other";
        }
        return "";        
    }
    
    public static String getEthnicityGroupName(int ethnicityGroup) {
        switch (ethnicityGroup) {
            case 1:
                return "WhiteBritish_Or_WhiteIrish";
            case 2:
                return "WhiteOther";
            case 3:
                return "MixedWhiteAndBlackAfrican_Or_MixedWhiteAndBlackCaribbean";
            case 4:
                return "MixedWhiteAndAsian";
            case 5:
                return "MixedOther";
            case 6:
                return "Asian_Or_AsianBritish";
            case 7:
                return "BlackOrBlackBritishCaribbean_Or_BlackOrBlackBritishAfrican_Or_BlackOrBlackBritishOther";
            case 8:
                return "Chinese";
            case 9:
                return "Other";
        }
        return "";        
    }
}
