/*
 * Copyright (C) 2015 geoagdt.
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
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_intID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;

/**
 *
 * @author geoagdt
 */
public class DW_SHBE_Collection extends DW_Object implements Serializable {

    private transient DW_SHBE_CollectionHandler handler;
    
    /**
     * For storing DW_SHBE_Record indexed by DRecordID
     */
    private HashMap<Long, DW_SHBE_Record> data;

    /**
     * For storing the ID of this.
     */
    private Long ID;

    private String PaymentType;
    private File InputFile;
    private TreeMap<String, DW_SHBE_Record> Records;
    private TreeMap<String, DW_SHBE_S_Record> SRecordsWithoutDRecords;
    private HashMap<DW_intID, String> SRecordIDToCTBRef;
    private HashSet<DW_PersonID> ClaimantIDs;
    private HashSet<DW_PersonID> PartnerIDs;
    private HashSet<DW_PersonID> DependentIDs;
    private HashSet<DW_PersonID> NonDependentIDs;
    private HashSet<DW_PersonID> AllIDs;
    private HashSet<DW_PersonID> PairedClaimantIDs;
    private HashMap<DW_intID, Long> ClaimantIDToRecordIDLookup;
    private HashMap<DW_intID, String> ClaimantIDToPostcodeLookup;
    private HashMap<DW_intID, Integer> ClaimantIDToTenancyTypeLookup;
    private HashMap<String, DW_intID> CTBRefToClaimantIDLookup;
    private HashMap<DW_intID, String> ClaimantIDToCTBRefLookup;
    private HashMap<String, Integer> LoadSummary;
    private HashSet<ID_PostcodeID> ClaimantIDAndPostcodeSet;
    private HashSet<ID_TenancyType> ClaimantIDAndTenancyTypeSet;
    private HashSet<ID_TenancyType_PostcodeID> ClaimantIDAndPostcodeAndTenancyTypeSet;
    private TreeSet<Long> RecordIDsNotLoaded = new TreeSet<Long>();

    public static final String sCountCTBClaims = "TotalCTBClaims";
    public static final String sCountCTBAndHBClaims = "TotalCTBAndHBClaims";
    public static final String sCountHBClaims = "CountHBClaims";
    public static final String sCountDRecords = "CountDRecords";
    public static final String sCountSRecords = "CountSRecords";
    public static final String sCountOfSRecordsWithoutDRecord = "CountOfSRecordsWithoutDRecord";
    public static final String sCountSRecordNotLoaded = "CountSRecordNotLoaded";
    public static final String sCountOfIncompleteDRecords = "CountOfIncompleteDRecords";
    public static final String sCountDRecordsInUnexpectedOrder = "CountDRecordsInUnexpectedOrder";
    //public static final String sCountRecordIDsNotLoaded = "RecordIDsNotLoaded.size()";
    public static final String sCountRecordIDsNotLoaded = "CountRecordIDsNotLoaded";
    public static final String sCountUniqueClaimants = "CountUniqueClaimants";
    public static final String sCountUniquePartners = "CountUniquePartners";
    public static final String sCountUniqueDependents = "CountUniqueDependents";
    public static final String sCountUniqueNonDependents = "CountUniqueNonDependents";
    public static final String sCountUniqueIndividuals = "CountUniqueIndividuals";
    public static final String sCountPairedClaimants = "CountPairedClaimants";
    public static final String sLineCount = "LineCount";
    public static final String sTotalIncome = "TotalIncome";
    public static final String sTotalIncomeGreaterThanZeroCount = "TotalIncomeGreaterThanZeroCount";
    public static final String sAverageIncomeGreaterThanZeroCount = "AverageIncomeGreaterThanZeroCount";
    public static final String sTotalWeeklyEligibleRentAmount = "TotalWeeklyEligibleRentAmount";
    public static final String sTotalWeeklyEligibleRentAmountGreaterThanZeroCount = "TotalWeeklyEligibleRentAmountGreaterThanZeroCount";
    public static final String sAverageWeeklyEligibleRentAmountGreaterThanZeroCount = "AverageWeeklyEligibleRentAmountGreaterThanZeroCount";

    public DW_SHBE_Collection(
            Long ID,
            DW_SHBE_CollectionHandler handler) {
        this.env = handler.env;
        this.handler = handler;
        data = new HashMap();
        this.ID = ID;
    }

    public HashMap<Long, DW_SHBE_Record> getData() {
        return data;
    }

//    public void setData(HashMap<Long, DW_SHBE_Record> data) {
//        this.data = data;
//    }
    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public DW_SHBE_Record getRecord(long aRecordID) {
        DW_SHBE_Record result;
        result = getData().get(aRecordID);
        return result;
    }

    public void addRecord(DW_SHBE_Record rec) {
        getData().put(
                rec.RecordID,
                rec);
        getEnv()._DW_SHBE_CollectionHandler.lookup.put(
                rec.getCouncilTaxBenefitClaimReferenceNumber(),
                rec.RecordID);
    }

    public void write() {
        File f = getFile();
        File parent_File = f.getParentFile();
        if (!parent_File.exists()) {
            parent_File.mkdirs();
        }
        Generic_StaticIO.writeObject(
                this,
                f);
    }

    protected File getDir() {
        File result;
        result = Generic_StaticIO.getObjectDirectory(getHandler().getDirectory(), getID(), getID(),
                getEnv()._DW_SHBE_CollectionHandler._MaximumNumberOfObjectsPerDirectory);
        return result;
    }

    protected File getFile() {
        File result;
        File dir;
        dir = getDir();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        result = new File(
                dir,
                this.getClass().getName() + getID() + ".thisFile");
        return result;
    }

    private void init() {
        Records = new TreeMap<String, DW_SHBE_Record>();
        SRecordsWithoutDRecords = new TreeMap<String, DW_SHBE_S_Record>();
        SRecordIDToCTBRef = new HashMap<DW_intID, String>();
        ClaimantIDs = new HashSet<DW_PersonID>();
        PartnerIDs = new HashSet<DW_PersonID>();
        DependentIDs = new HashSet<DW_PersonID>();
        NonDependentIDs = new HashSet<DW_PersonID>();
        AllIDs = new HashSet<DW_PersonID>();
        PairedClaimantIDs = new HashSet<DW_PersonID>();
        ClaimantIDToRecordIDLookup = new HashMap<DW_intID, Long>();
        ClaimantIDToPostcodeLookup = new HashMap<DW_intID, String>();
        ClaimantIDToTenancyTypeLookup = new HashMap<DW_intID, Integer>();
        CTBRefToClaimantIDLookup = new HashMap<String, DW_intID>();
        ClaimantIDToCTBRefLookup = new HashMap<DW_intID, String>();
        LoadSummary = new HashMap<String, Integer>();
        ClaimantIDAndPostcodeSet = new HashSet<ID_PostcodeID>();
        ClaimantIDAndTenancyTypeSet = new HashSet<ID_TenancyType>();
        ClaimantIDAndPostcodeAndTenancyTypeSet = new HashSet<ID_TenancyType_PostcodeID>();
    }

//    HashMap<String, DW_ID> NINOToDW_IDLookup,
//      HashMap<DW_ID, String> DW_IDToNINOLookup,
//      HashMap<String, DW_ID> DOBToDW_IDLookup,
//      HashMap<DW_ID, String> DW_IDToDOBLookup,
//      HashMap<DW_PersonID, DW_ID> DW_PersonIDToDW_IDLookup,
//      HashMap<DW_ID, DW_PersonID> DW_IDToDW_PersonIDLookup,
//      HashMap<String, DW_ID> PostcodeToPostcodeIDLookup,
//      HashMap<DW_ID, String> PostcodeIDToPostcodeLookup,
//              this.NINOToDW_IDLookup = NINOToDW_IDLookup;
//      this.DW_IDToNINOLookup = DW_IDToNINOLookup;
//      this.DOBToDW_IDLookup = DOBToDW_IDLookup;
//      this.DW_IDToDOBLookup = DW_IDToDOBLookup;
//      this.DW_PersonIDToDW_IDLookup = DW_PersonIDToDW_IDLookup;
//      this.DW_IDToDW_PersonIDLookup = DW_IDToDW_PersonIDLookup;
//      this.PostcodeToPostcodeIDLookup = PostcodeToPostcodeIDLookup;
//      this.PostcodeIDToPostcodeLookup = PostcodeIDToPostcodeLookup;
    /**
     * For loading an existing collection.
     *
     * @param inputFilename
     * @param paymentType
     */
    public DW_SHBE_Collection(
            DW_Environment env,
            String inputFilename,
            String paymentType
    ) {
        this.env = env;
        DW_SHBE_Handler tDW_SHBE_Handler;
        tDW_SHBE_Handler = env.getDW_SHBE_Handler();
        File DRecordsFile = tDW_SHBE_Handler.getDRecordsFile(paymentType, inputFilename);
        File SRecordsWithoutDRecordsFile = tDW_SHBE_Handler.getSRecordsWithoutDRecordsFile(paymentType, inputFilename);
        File SRecordIDToCTBRefFile = tDW_SHBE_Handler.getSRecordIDToCTBRefFile(paymentType, inputFilename);
        File ClaimantIDsFile = tDW_SHBE_Handler.getClaimantIDsFile(paymentType, inputFilename);
        File PartnerIDsFile = tDW_SHBE_Handler.getPartnerIDsFile(paymentType, inputFilename);
        File DependentsIDsFile = tDW_SHBE_Handler.getDependentIDsFile(paymentType, inputFilename);
        File NonDependentsIDsFile = tDW_SHBE_Handler.getNonDependentIDsFile(paymentType, inputFilename);
        File AllHouseholdIDsFile = tDW_SHBE_Handler.getAllHouseholdIDsFile(paymentType, inputFilename);
        File PairedClaimantIDsFile = tDW_SHBE_Handler.getPairedClaimantIDsFile(paymentType, inputFilename);
        File ClaimantIDToRecordIDLookupFile = tDW_SHBE_Handler.getClaimantIDToRecordIDLookupFile(paymentType, inputFilename);
        File ClaimantIDToPostcodeLookupFile = tDW_SHBE_Handler.getClaimantIDToPostcodeLookupFile(paymentType, inputFilename);
        File ClaimantIDToTenancyTypeLookupFile = tDW_SHBE_Handler.getClaimantIDToTenancyTypeLookupFile(paymentType, inputFilename);
        File CTBRefToClaimantIDLookupFile = tDW_SHBE_Handler.getCTBRefToClaimantIDLookupFile(paymentType, inputFilename);
        File ClaimantIDToCTBRefLookupFile = tDW_SHBE_Handler.getClaimantIDToCTBRefLookupFile(paymentType, inputFilename);
        File LoadSummaryFile = tDW_SHBE_Handler.getLoadSummaryFile(paymentType, inputFilename);
        File ClaimantIDAndPostcodeFile = tDW_SHBE_Handler.getClaimantIDPostcodeSetFile(paymentType, inputFilename);
        File ClaimantIDAndTenancyTypeFile = tDW_SHBE_Handler.getClaimantIDTenancyTypeSetFile(paymentType, inputFilename);
        File ClaimantIDAndPostcodeAndTenancyTypeFile = tDW_SHBE_Handler.getClaimantIDTenancyPostcodeTypeSetFile(paymentType, inputFilename);
        File RecordIDsNotLoadedFile = tDW_SHBE_Handler.getRecordIDsNotLoadedFile(paymentType, inputFilename);
        Records = (TreeMap<String, DW_SHBE_Record>) Generic_StaticIO.readObject(DRecordsFile);
        SRecordsWithoutDRecords = (TreeMap<String, DW_SHBE_S_Record>) Generic_StaticIO.readObject(SRecordsWithoutDRecordsFile);
        SRecordIDToCTBRef = (HashMap<DW_intID, String>) Generic_StaticIO.readObject(SRecordIDToCTBRefFile);
        ClaimantIDs = (HashSet<DW_PersonID>) Generic_StaticIO.readObject(ClaimantIDsFile);
        PartnerIDs = (HashSet<DW_PersonID>) Generic_StaticIO.readObject(PartnerIDsFile);
        DependentIDs = (HashSet<DW_PersonID>) Generic_StaticIO.readObject(DependentsIDsFile);
        NonDependentIDs = (HashSet<DW_PersonID>) Generic_StaticIO.readObject(NonDependentsIDsFile);
        AllIDs = (HashSet<DW_PersonID>) Generic_StaticIO.readObject(AllHouseholdIDsFile);
        PairedClaimantIDs = (HashSet<DW_PersonID>) Generic_StaticIO.readObject(PairedClaimantIDsFile);
        ClaimantIDToRecordIDLookup = (HashMap<DW_intID, Long>) Generic_StaticIO.readObject(ClaimantIDToRecordIDLookupFile);
        ClaimantIDToPostcodeLookup = (HashMap<DW_intID, String>) Generic_StaticIO.readObject(ClaimantIDToPostcodeLookupFile);
        ClaimantIDToTenancyTypeLookup = (HashMap<DW_intID, Integer>) Generic_StaticIO.readObject(ClaimantIDToTenancyTypeLookupFile);
        CTBRefToClaimantIDLookup = (HashMap<String, DW_intID>) Generic_StaticIO.readObject(CTBRefToClaimantIDLookupFile);
        ClaimantIDToCTBRefLookup = (HashMap<DW_intID, String>) Generic_StaticIO.readObject(ClaimantIDToCTBRefLookupFile);
        LoadSummary = (HashMap<String, Integer>) Generic_StaticIO.readObject(LoadSummaryFile);
        ClaimantIDAndPostcodeSet = (HashSet<ID_PostcodeID>) Generic_StaticIO.readObject(ClaimantIDAndPostcodeFile);
        ClaimantIDAndTenancyTypeSet = (HashSet<ID_TenancyType>) Generic_StaticIO.readObject(ClaimantIDAndTenancyTypeFile);
        ClaimantIDAndPostcodeAndTenancyTypeSet = (HashSet<ID_TenancyType_PostcodeID>) Generic_StaticIO.readObject(ClaimantIDAndPostcodeAndTenancyTypeFile);
        RecordIDsNotLoaded = (TreeSet<Long>) Generic_StaticIO.readObject(RecordIDsNotLoadedFile);
    }

    /**
     * Loads data from source.
     *
     * @param tDW_SHBE_Handler
     * @param ID
     * @param handler
     * @param inputDirectory
     * @param inputFilename
     * @param paymentType
     */
    public DW_SHBE_Collection(
            DW_SHBE_Handler tDW_SHBE_Handler,
            Long ID,
            DW_SHBE_CollectionHandler handler,
            File inputDirectory,
            String inputFilename,
            String paymentType
    ) {
        DW_Strings tDW_Strings;
        tDW_Strings = env.getDW_Strings();
        System.err.println("----------------------");
        System.err.println("Load " + inputFilename);
        System.err.println("----------------------");
        this.ID = ID;
        this.handler = handler;
        this.env = handler.env;
        this.InputFile = new File(inputDirectory, inputFilename);
        this.PaymentType = paymentType;
        // NINO
        HashMap<String, DW_intID> tNINOToDW_IDLookup;
        HashMap<DW_intID, String> tDW_IDToNINOLookup;
        HashMap<String, DW_intID> tDOBToDW_IDLookup;
        HashMap<DW_intID, String> tDW_IDToDOBLookup;
        HashMap<DW_PersonID, DW_intID> tDW_PersonIDToDW_IDLookup;
        HashMap<DW_intID, DW_PersonID> tDW_IDToDW_PersonIDLookup;
        HashMap<String, DW_intID> tPostcodeToPostcodeIDLookup;
        HashMap<DW_intID, String> tPostcodeIDToPostcodeLookup;
        tNINOToDW_IDLookup = tDW_SHBE_Handler.getNINOToDW_IDLookup();
        tDW_IDToNINOLookup = tDW_SHBE_Handler.getDW_IDToNINOLookup();
        tDOBToDW_IDLookup = tDW_SHBE_Handler.getDOBToDW_IDLookup();
        tDW_IDToDOBLookup = tDW_SHBE_Handler.getDW_IDToDOBLookup();
        tPostcodeToPostcodeIDLookup = tDW_SHBE_Handler.getPostcodeToPostcodeIDLookup();
        tPostcodeIDToPostcodeLookup = tDW_SHBE_Handler.getPostcodeIDToPostcodeLookup();
        tDW_PersonIDToDW_IDLookup = tDW_SHBE_Handler.getDW_PersonIDToDW_IDLookup();
        tDW_IDToDW_PersonIDLookup = tDW_SHBE_Handler.getDW_IDToDW_PersonIDLookup();
        File tDRecordsFile;
        tDRecordsFile = tDW_SHBE_Handler.getDRecordsFile(
                paymentType, inputFilename);
        File tSRecordsWithoutDRecordsFile;
        tSRecordsWithoutDRecordsFile = tDW_SHBE_Handler.getSRecordsWithoutDRecordsFile(
                paymentType, inputFilename);
        File tSRecordIDToCTBRefFile;
        tSRecordIDToCTBRefFile = tDW_SHBE_Handler.getSRecordIDToCTBRefFile(
                paymentType, inputFilename);
        File tClaimantIDsFile;
        tClaimantIDsFile = tDW_SHBE_Handler.getClaimantIDsFile(
                paymentType, inputFilename);
        File tPartnerIDsFile;
        tPartnerIDsFile = tDW_SHBE_Handler.getPartnerIDsFile(
                paymentType, inputFilename);
        File tDependentsIDsFile;
        tDependentsIDsFile = tDW_SHBE_Handler.getDependentIDsFile(
                paymentType, inputFilename);
        File tNonDependentsIDsFile;
        tNonDependentsIDsFile = tDW_SHBE_Handler.getNonDependentIDsFile(
                paymentType, inputFilename);
        File tAllHouseholdIDsFile = tDW_SHBE_Handler.getAllHouseholdIDsFile(
                paymentType, inputFilename);
        File tPairedClaimantIDsFile = tDW_SHBE_Handler.getPairedClaimantIDsFile(
                paymentType, inputFilename);
        File tClaimantIDToRecordIDLookupFile = tDW_SHBE_Handler.getClaimantIDToRecordIDLookupFile(
                paymentType, inputFilename);
        File tClaimantIDToPostcodeLookupFile = tDW_SHBE_Handler.getClaimantIDToPostcodeLookupFile(
                paymentType, inputFilename);
        File tClaimantIDToTenancyTypeLookupFile = tDW_SHBE_Handler.getClaimantIDToTenancyTypeLookupFile(
                paymentType, inputFilename);
        File tCTBRefToClaimantIDLookupFile = tDW_SHBE_Handler.getCTBRefToClaimantIDLookupFile(
                paymentType, inputFilename);
        File tClaimantIDToCTBRefLookupFile = tDW_SHBE_Handler.getClaimantIDToCTBRefLookupFile(
                paymentType, inputFilename);
        File tLoadSummaryFile = tDW_SHBE_Handler.getLoadSummaryFile(
                paymentType, inputFilename);
        File tClaimantIDAndPostcodeFile = tDW_SHBE_Handler.getClaimantIDPostcodeSetFile(
                paymentType, inputFilename);
        File tClaimantIDAndTenancyTypeFile = tDW_SHBE_Handler.getClaimantIDTenancyTypeSetFile(
                paymentType, inputFilename);
        File tClaimantIDAndPostcodeAndTenancyTypeFile = tDW_SHBE_Handler.getClaimantIDTenancyPostcodeTypeSetFile(
                paymentType, inputFilename);
        File tRecordIDsNotLoadedFile = tDW_SHBE_Handler.getRecordIDsNotLoadedFile(
                paymentType, inputFilename);
        long collectionID = 0L;
        DW_SHBE_Collection collection;
        collection = new DW_SHBE_Collection(collectionID, handler);
        handler.add(collection);
        long DRecordID = 0;
        // Loop
        int totalCouncilTaxBenefitClaims = 0;
        int totalCouncilTaxAndHousingBenefitClaims = 0;
        int totalHousingBenefitClaims = 0;
        int countSRecords = 0;
        int SRecordNotLoadedCount = 0;
        int NumberOfIncompleteDRecords = 0;
        //        int countDRecords = 0;
        //        int countOfSRecordsWithoutDRecord = 0;
        long totalIncome = 0;
        long grandTotalIncome = 0;
        int totalIncomeGreaterThanZeroCount = 0;
        long totalWeeklyEligibleRentAmount = 0;
        long grandTotalWeeklyEligibleRentAmount = 0;
        int totalWeeklyEligibleRentAmountGreaterThanZeroCount = 0;
        try {
            BufferedReader br;
            br = Generic_StaticIO.getBufferedReader(InputFile);
            StreamTokenizer st = new StreamTokenizer(br);
            Generic_StaticIO.setStreamTokenizerSyntax5(st);
            st.wordChars('`', '`');
            st.wordChars('*', '*');
            String line = "";
            init();
            long RecordID = 0;
            int lineCount = 0;
            int type = readAndCheckFirstLine(inputDirectory, inputFilename);
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
                                SRecord = new DW_SHBE_S_Record(RecordID, type, line);
                                String CTBRef;
                                CTBRef = SRecord.getCouncilTaxBenefitClaimReferenceNumber();
                                if (CTBRef == null) {
                                    RecordIDsNotLoaded.add(RecordID);
                                    SRecordNotLoadedCount++;
                                } else {
                                    DW_SHBE_Record record;
                                    record = Records.get(CTBRef);
                                    if (record == null) {
                                        /* SRecordsWithoutDRecords are gone 
                                         * through at the end in case and 
                                         * DRecrods are in the data in later 
                                         * lines for some reason. At this 
                                         * stage, we can create new IDs as 
                                         * without the DRecord we have no 
                                         * idea of the Claimant NINO which 
                                         * is generally used for assigning 
                                         * DW_IDs for SRecords where there is 
                                         * not one set.
                                         */
                                        SRecordsWithoutDRecords.put(CTBRef, SRecord);
                                    } else {
                                        if (!record.getSRecords().add(SRecord)) {
                                            SRecordNotLoadedCount++;
                                            throw new Exception("Duplicate SRecord " + SRecord);
                                        }
                                        boolean doLoop = false;
                                        if (paymentType.equalsIgnoreCase(tDW_Strings.sPaymentTypeAll)) {
                                            doLoop = true;
                                        } else {
                                            DW_SHBE_D_Record DRecord;
                                            DRecord = record.DRecord;
                                            int StatusOfHBClaimAtExtractDate;
                                            StatusOfHBClaimAtExtractDate = DRecord.getStatusOfHBClaimAtExtractDate();
                                            int StatusOfCTBClaimAtExtractDate;
                                            StatusOfCTBClaimAtExtractDate = DRecord.getStatusOfCTBClaimAtExtractDate();
                                            if (paymentType.equalsIgnoreCase(tDW_Strings.sPaymentTypeIn)
                                                    && (StatusOfHBClaimAtExtractDate == 1
                                                    || StatusOfCTBClaimAtExtractDate == 1)) {
                                                doLoop = true;
                                            }
                                            if (paymentType.equalsIgnoreCase(tDW_Strings.sPaymentTypeSuspended)
                                                    && (StatusOfHBClaimAtExtractDate == 2
                                                    || StatusOfCTBClaimAtExtractDate == 2)) {
                                                doLoop = true;
                                            }
                                            if (paymentType.equalsIgnoreCase(tDW_Strings.sPaymentTypeOther)
                                                    && (StatusOfHBClaimAtExtractDate == 0
                                                    || StatusOfCTBClaimAtExtractDate == 0)) {
                                                doLoop = true;
                                            }
                                        }
                                        if (doLoop) {
                                            doSRecordLoop(
                                                    tDW_SHBE_Handler,
                                                    tNINOToDW_IDLookup,
                                                    tDW_IDToNINOLookup,
                                                    tDOBToDW_IDLookup,
                                                    tDW_IDToDOBLookup,
                                                    tDW_PersonIDToDW_IDLookup,
                                                    tDW_IDToDW_PersonIDLookup,
                                                    SRecord,
                                                    CTBRef);
                                        } else {
                                            RecordIDsNotLoaded.add(RecordID);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println(line);
                                System.err.println("RecordID " + RecordID);
                                System.err.println(e.getLocalizedMessage());
                                e.printStackTrace();
                                RecordIDsNotLoaded.add(RecordID);
                            }
                            countSRecords++;
                        } else {
                            if (line.startsWith("D")) {
                                try {
                                    handler.nextID = DRecordID + 1L;
                                    if (handler.nextID % handler._MaximumNumberPerCollection == 0) {
                                        collectionID++;
                                        handler.swapToFile_Collection(collection);
                                        collection = new DW_SHBE_Collection(collectionID, handler);
                                    }
                                    DW_SHBE_D_Record aDRecord;
                                    aDRecord = new DW_SHBE_D_Record(RecordID, type, line);
                                    /*
                                     * For the time being, if for some reason 
                                     * the record does not load correctly such 
                                     * that the TenancyType is not set, then
                                     * do not load this record. Ideally those 
                                     * do not load will be investigated and a 
                                     * solution for loading them found.
                                     */
                                    int TenancyType;
                                    TenancyType = aDRecord.getTenancyType();
                                    if (TenancyType == 0) {
                                        int debug = 1;
                                        aDRecord = new DW_SHBE_D_Record(RecordID, type, line);
                                        //System.err.println("Incomplete record " + RecordID);
                                        NumberOfIncompleteDRecords++;
                                        RecordIDsNotLoaded.add(RecordID);
                                        lineCount++;
                                        RecordID++;
                                        break;
                                    }

                                    DW_SHBE_Record rec;
                                    rec = new DW_SHBE_Record(RecordID, aDRecord);
                                    collection.addRecord(rec);

                                    String CTBRef;
                                    CTBRef = aDRecord.getCouncilTaxBenefitClaimReferenceNumber();
                                    if (CTBRef != null) {
                                        boolean doLoop = false;
                                        if (paymentType.equalsIgnoreCase(tDW_Strings.sPaymentTypeAll)) {
                                            doLoop = true;
                                            if (!CTBRef.trim().isEmpty()) {
                                                totalCouncilTaxBenefitClaims++;
                                                if (tDW_SHBE_Handler.isHBClaim(aDRecord)) {
                                                    totalCouncilTaxAndHousingBenefitClaims++;
                                                    totalHousingBenefitClaims++;
                                                }
                                            } else {
                                                int Debug = 1; // Did think that all records had a CTBRef
                                                if (tDW_SHBE_Handler.isHBClaim(aDRecord)) {
                                                    totalHousingBenefitClaims++;
                                                }
                                            }
                                        } else {
                                            int StatusOfCTBClaimAtExtractDate;
                                            StatusOfCTBClaimAtExtractDate = aDRecord.getStatusOfCTBClaimAtExtractDate();
                                            int StatusOfHBClaimAtExtractDate;
                                            StatusOfHBClaimAtExtractDate = aDRecord.getStatusOfHBClaimAtExtractDate();
                                            if (paymentType.equalsIgnoreCase(tDW_Strings.sPaymentTypeIn) && (StatusOfHBClaimAtExtractDate == 1 || StatusOfCTBClaimAtExtractDate == 1)) {
                                                doLoop = true;
                                                boolean isCTBClaimInPayment;
                                                isCTBClaimInPayment = tDW_SHBE_Handler.isCTBOnlyClaimInPayment(aDRecord);
                                                if (tDW_SHBE_Handler.isHBClaimInPayment(aDRecord)) {
                                                    totalHousingBenefitClaims++;
                                                    if (isCTBClaimInPayment) {
                                                        totalCouncilTaxAndHousingBenefitClaims++;
                                                    }
                                                }
                                                if (isCTBClaimInPayment) {
                                                    totalCouncilTaxBenefitClaims++;
                                                }
                                            }
                                            if (paymentType.equalsIgnoreCase(tDW_Strings.sPaymentTypeSuspended) && (StatusOfHBClaimAtExtractDate == 2 || StatusOfCTBClaimAtExtractDate == 2)) {
                                                doLoop = true;
                                                boolean isCTBClaimSuspended;
                                                isCTBClaimSuspended = tDW_SHBE_Handler.isCTBOnlyClaimSuspended(aDRecord);
                                                if (tDW_SHBE_Handler.isHBClaimSuspended(aDRecord)) {
                                                    totalHousingBenefitClaims++;
                                                    if (isCTBClaimSuspended) {
                                                        totalCouncilTaxAndHousingBenefitClaims++;
                                                    }
                                                }
                                                if (isCTBClaimSuspended) {
                                                    totalCouncilTaxBenefitClaims++;
                                                }
                                            }
                                            if (paymentType.equalsIgnoreCase(tDW_Strings.sPaymentTypeOther) && (StatusOfHBClaimAtExtractDate == 0 || StatusOfCTBClaimAtExtractDate == 0)) {
                                                doLoop = true;
                                                boolean isCTBClaimOtherPT;
                                                isCTBClaimOtherPT = tDW_SHBE_Handler.isCTBOnlyClaimOtherPT(aDRecord);
                                                if (tDW_SHBE_Handler.isHBClaimOtherPT(aDRecord)) {
                                                    totalHousingBenefitClaims++;
                                                    if (isCTBClaimOtherPT) {
                                                        totalCouncilTaxAndHousingBenefitClaims++;
                                                    }
                                                }
                                                if (isCTBClaimOtherPT) {
                                                    totalCouncilTaxBenefitClaims++;
                                                }
                                            }
                                        }
                                        if (doLoop) {
                                            totalIncome = tDW_SHBE_Handler.getClaimantsAndPartnersIncomeTotal(aDRecord);
                                            grandTotalIncome += totalIncome;
                                            if (totalIncome > 0) {
                                                totalIncomeGreaterThanZeroCount++;
                                            }
                                            totalWeeklyEligibleRentAmount = aDRecord.getWeeklyEligibleRentAmount();
                                            grandTotalWeeklyEligibleRentAmount += totalWeeklyEligibleRentAmount;
                                            if (totalWeeklyEligibleRentAmount > 0) {
                                                totalWeeklyEligibleRentAmountGreaterThanZeroCount++;
                                            }
                                            Object o = Records.put(CTBRef, rec);
                                            if (o != null) {
                                                DW_SHBE_Record existingSHBE_DataRecord = (DW_SHBE_Record) o;
                                                System.err.println("Warning existing SHBE Record overwritten:");
                                                System.err.println("Existing SHBE_DataRecord: " + existingSHBE_DataRecord);
                                                System.err.println("Replacement SHBE_DataRecord: " + aDRecord);
                                            }
                                            String claimantNINO;
                                            claimantNINO = aDRecord.getClaimantsNationalInsuranceNumber();
                                            DW_intID claimantNINODW_ID;
                                            claimantNINODW_ID = tDW_SHBE_Handler.getIDAddIfNeeded(
                                                    claimantNINO,
                                                    tNINOToDW_IDLookup,
                                                    tDW_IDToNINOLookup);
                                            String claimantDOB;
                                            claimantDOB = aDRecord.getClaimantsDateOfBirth();
                                            DW_intID claimantDOBDW_ID;
                                            claimantDOBDW_ID = tDW_SHBE_Handler.getIDAddIfNeeded(
                                                    claimantDOB,
                                                    tDOBToDW_IDLookup,
                                                    tDW_IDToDOBLookup);
                                            DW_PersonID claimantDW_PersonID;
                                            claimantDW_PersonID = new DW_PersonID(claimantNINODW_ID, claimantDOBDW_ID);
                                            if (ClaimantIDs.contains(claimantDW_PersonID)) {
                                                // This claimant has two claims in the month.
                                                // This can happen and most commonly happens
                                                // for travellers. Some claimants have their
                                                // NINO set to XX999999XX and it is possible
                                                // that two have this set and have the same
                                                // date of birth!
//                                                    System.out.println("Claimant may have mulitple claims!");
                                                DW_intID DW_ID;
                                                DW_ID = tDW_PersonIDToDW_IDLookup.get(claimantDW_PersonID);
                                                String previousCTBRef;
                                                previousCTBRef = ClaimantIDToCTBRefLookup.get(DW_ID);
                                                DW_SHBE_Record previousRecord;
                                                previousRecord = Records.get(previousCTBRef);
                                                //System.out.println("Previous D Record");
//                                                    System.out.println(previousRecord.DRecord.toString());
                                                //System.out.println("This D Record");
//                                                    System.out.println(aDRecord.toString());
                                                if (previousRecord.isPairedRecord()) {
                                                    System.out.println("Claimant already has a paired record!");
                                                    System.out.println("Claimant appears to have multiple claims!");
                                                    System.out.println("This might happen if a claimant moves in a "
                                                            + "month and sets up different claims. This could in "
                                                            + "theory happen a number of times!");
                                                    int debug = 1;
                                                } else {
                                                    PairedClaimantIDs.add(
                                                            tDW_IDToDW_PersonIDLookup.get(
                                                                    CTBRefToClaimantIDLookup.get(previousCTBRef)));
                                                }
                                                DW_intID claimantID = CTBRefToClaimantIDLookup.get(previousCTBRef);
                                                String postcode;
                                                postcode = DW_Postcode_Handler.formatPostcode(aDRecord.getClaimantsPostcode());
                                                ClaimantIDToPostcodeLookup.put(claimantID, postcode);
                                                DW_intID postcodeID;
                                                postcodeID = tDW_SHBE_Handler.getPostcodeIDAddIfNeeded(
                                                        postcode,
                                                        tPostcodeToPostcodeIDLookup,
                                                        tPostcodeIDToPostcodeLookup);
                                                ClaimantIDAndPostcodeSet.add(new ID_PostcodeID(claimantID, postcodeID));
                                                ID_TenancyType ID_TenancyType;
                                                ID_TenancyType = new ID_TenancyType(claimantID, TenancyType);
                                                ClaimantIDAndTenancyTypeSet.add(ID_TenancyType);
                                                ClaimantIDAndPostcodeAndTenancyTypeSet.add(
                                                        new ID_TenancyType_PostcodeID(ID_TenancyType, postcodeID));
                                                ClaimantIDToTenancyTypeLookup.put(claimantID, TenancyType);
                                                AllIDs.add(claimantDW_PersonID);
                                            } else {
                                                ClaimantIDs.add(claimantDW_PersonID);
                                                DW_intID claimantID = tDW_SHBE_Handler.getIDAddIfNeeded(
                                                        claimantDW_PersonID,
                                                        tDW_PersonIDToDW_IDLookup,
                                                        tDW_IDToDW_PersonIDLookup);
                                                CTBRefToClaimantIDLookup.put(CTBRef, claimantID);
                                                ClaimantIDToCTBRefLookup.put(claimantID, CTBRef);
                                                ClaimantIDToRecordIDLookup.put(claimantID, RecordID);
                                                String postcode;
                                                postcode = DW_Postcode_Handler.formatPostcode(aDRecord.getClaimantsPostcode());
                                                ClaimantIDToPostcodeLookup.put(claimantID, postcode);
                                                DW_intID postcodeID;
                                                postcodeID = tDW_SHBE_Handler.getPostcodeIDAddIfNeeded(
                                                        postcode,
                                                        tPostcodeToPostcodeIDLookup,
                                                        tPostcodeIDToPostcodeLookup);
                                                ClaimantIDAndPostcodeSet.add(new ID_PostcodeID(claimantID, postcodeID));
                                                ID_TenancyType ID_TenancyType;
                                                ID_TenancyType = new ID_TenancyType(claimantID, TenancyType);
                                                ClaimantIDAndTenancyTypeSet.add(ID_TenancyType);
                                                ClaimantIDAndPostcodeAndTenancyTypeSet.add(
                                                        new ID_TenancyType_PostcodeID(ID_TenancyType, postcodeID));
                                                ClaimantIDToTenancyTypeLookup.put(claimantID, TenancyType);
                                                AllIDs.add(claimantDW_PersonID);
                                                if (aDRecord.getPartnerFlag() > 0) {
                                                    String partnerNINO;
                                                    partnerNINO = aDRecord.getPartnersNationalInsuranceNumber();
                                                    DW_intID partnerNINODW_ID;
                                                    partnerNINODW_ID = tDW_SHBE_Handler.getIDAddIfNeeded(
                                                            partnerNINO,
                                                            tNINOToDW_IDLookup,
                                                            tDW_IDToNINOLookup);
                                                    String partnerDOB;
                                                    partnerDOB = aDRecord.getPartnersDateOfBirth();
                                                    DW_intID partnerDOBDW_ID;
                                                    partnerDOBDW_ID = tDW_SHBE_Handler.getIDAddIfNeeded(
                                                            partnerDOB,
                                                            tDOBToDW_IDLookup,
                                                            tDW_IDToDOBLookup);
                                                    DW_PersonID partnerDW_PersonID;
                                                    partnerDW_PersonID = new DW_PersonID(partnerNINODW_ID, partnerDOBDW_ID);
                                                    if (PartnerIDs.contains(partnerDW_PersonID)) {
                                                        // This partner is in two claims in the month.
                                                        // Some claimants have their NINO set to
                                                        // XX999999XX and it is possible that two
                                                        // have this set and have the same
                                                        // date of birth!
                                                        int debug = 1;
                                                    }
                                                    PartnerIDs.add(partnerDW_PersonID);
                                                    DW_intID partnerID = tDW_SHBE_Handler.getIDAddIfNeeded(
                                                            partnerDW_PersonID,
                                                            tDW_PersonIDToDW_IDLookup,
                                                            tDW_IDToDW_PersonIDLookup);
                                                    AllIDs.add(partnerDW_PersonID);
                                                }
                                            }
                                        } else {
                                            RecordIDsNotLoaded.add(RecordID);
                                        }
                                    }
                                } catch (Exception e) {
                                    System.err.println(line);
                                    System.err.println("RecordID " + RecordID);
                                    System.err.println(e.getLocalizedMessage());
                                    e.printStackTrace();
                                    RecordIDsNotLoaded.add(RecordID);
                                }
                            }
                        }
                        lineCount++;
                        RecordID++;
                        DRecordID++;
                        break;
                }
                tokenType = st.nextToken();
            }
            br.close();
            // Add all SRecords from recordsWithoutDRecords that actually do have
            // Records, it just so happened that the DRecord was read after the
            // first SRecord
            int countDRecordsInUnexpectedOrder = 0;
            Set<String> s = Records.keySet();
            Iterator<String> ite = SRecordsWithoutDRecords.keySet().iterator();
            HashSet<String> rem = new HashSet<String>();
            while (ite.hasNext()) {
                String councilTaxBenefitClaimReferenceNumber = ite.next();
                if (s.contains(councilTaxBenefitClaimReferenceNumber)) {
                    DW_SHBE_Record DRecord = Records.get(councilTaxBenefitClaimReferenceNumber);
                    DRecord.SRecords.addAll(Records.get(councilTaxBenefitClaimReferenceNumber).getSRecords());
                    rem.add(councilTaxBenefitClaimReferenceNumber);
                    countDRecordsInUnexpectedOrder++;
                }
            }
            ite = rem.iterator();
            while (ite.hasNext()) {
                SRecordsWithoutDRecords.remove(ite.next());
            }
            // Get IDs for remaining SRecords
            ite = SRecordsWithoutDRecords.keySet().iterator();
            while (ite.hasNext()) {
                String CTBRef;
                CTBRef = ite.next();
                DW_SHBE_S_Record SRecord;
                SRecord = SRecordsWithoutDRecords.get(CTBRef);
                doSRecordLoop(
                        tDW_SHBE_Handler,
                        tNINOToDW_IDLookup,
                        tDW_IDToNINOLookup,
                        tDOBToDW_IDLookup,
                        tDW_IDToDOBLookup,
                        tDW_PersonIDToDW_IDLookup,
                        tDW_IDToDW_PersonIDLookup,
                        SRecord,
                        CTBRef);
            }
            System.out.println(sCountCTBClaims + " " + totalCouncilTaxBenefitClaims);
            LoadSummary.put(sCountCTBClaims, totalCouncilTaxBenefitClaims);
            System.out.println(sCountCTBAndHBClaims + " " + totalCouncilTaxAndHousingBenefitClaims);
            LoadSummary.put(sCountCTBAndHBClaims, totalCouncilTaxAndHousingBenefitClaims);
            System.out.println(sCountHBClaims + " " + totalHousingBenefitClaims);
            LoadSummary.put(sCountHBClaims, totalHousingBenefitClaims);
            System.out.println(sCountDRecords + " " + Records.size());
            LoadSummary.put(sCountDRecords, Records.size());
            System.out.println(sCountSRecords + " " + countSRecords);
            LoadSummary.put(sCountSRecords, countSRecords);
            System.out.println(sCountOfSRecordsWithoutDRecord + " " + SRecordsWithoutDRecords.size());
            LoadSummary.put(sCountOfSRecordsWithoutDRecord, SRecordsWithoutDRecords.size());
            System.out.println(sCountSRecordNotLoaded + " " + SRecordNotLoadedCount);
            LoadSummary.put(sCountSRecordNotLoaded, SRecordNotLoadedCount);
            System.out.println(sCountOfIncompleteDRecords + " " + NumberOfIncompleteDRecords);
            LoadSummary.put(sCountOfIncompleteDRecords, NumberOfIncompleteDRecords);
            System.out.println(sCountDRecordsInUnexpectedOrder + " " + countDRecordsInUnexpectedOrder);
            LoadSummary.put(sCountDRecordsInUnexpectedOrder, countDRecordsInUnexpectedOrder);
            System.out.println(sCountRecordIDsNotLoaded + " " + RecordIDsNotLoaded.size());
            LoadSummary.put(sCountRecordIDsNotLoaded, RecordIDsNotLoaded.size());
            System.out.println(sCountUniqueClaimants + " " + ClaimantIDs.size());
            LoadSummary.put(sCountUniqueClaimants, ClaimantIDs.size());
            System.out.println(sCountUniquePartners + " " + PartnerIDs.size());
            LoadSummary.put(sCountUniquePartners, PartnerIDs.size());
            System.out.println(sCountUniqueDependents + " " + DependentIDs.size());
            LoadSummary.put(sCountUniqueDependents, DependentIDs.size());
            System.out.println(sCountUniqueNonDependents + " " + NonDependentIDs.size());
            LoadSummary.put(sCountUniqueNonDependents, NonDependentIDs.size());
            System.out.println(sCountUniqueIndividuals + " " + AllIDs.size());
            LoadSummary.put(sCountUniqueIndividuals, AllIDs.size());
            System.out.println(sCountPairedClaimants + " " + PairedClaimantIDs.size());
            LoadSummary.put(sCountPairedClaimants, PairedClaimantIDs.size());
            System.out.println(sLineCount + " " + lineCount);
            LoadSummary.put(sLineCount, lineCount);
            System.out.println(sTotalIncome + " " + grandTotalIncome);
            System.out.println(sTotalIncomeGreaterThanZeroCount + " " + totalIncomeGreaterThanZeroCount);
            System.out.println(sAverageIncomeGreaterThanZeroCount + " " + grandTotalIncome / (double) totalIncomeGreaterThanZeroCount);
            System.out.println(sTotalWeeklyEligibleRentAmount + " " + grandTotalWeeklyEligibleRentAmount);
            System.out.println(sTotalWeeklyEligibleRentAmountGreaterThanZeroCount + " " + totalWeeklyEligibleRentAmountGreaterThanZeroCount);
            System.out.println(sAverageWeeklyEligibleRentAmountGreaterThanZeroCount + " " + grandTotalWeeklyEligibleRentAmount / (double) totalWeeklyEligibleRentAmountGreaterThanZeroCount);
            Generic_StaticIO.writeObject(Records, tDRecordsFile);
            Generic_StaticIO.writeObject(SRecordsWithoutDRecords, tSRecordsWithoutDRecordsFile);
            Generic_StaticIO.writeObject(SRecordIDToCTBRef, tSRecordIDToCTBRefFile);
            Generic_StaticIO.writeObject(ClaimantIDs, tClaimantIDsFile);
            Generic_StaticIO.writeObject(PartnerIDs, tPartnerIDsFile);
            Generic_StaticIO.writeObject(DependentIDs, tDependentsIDsFile);
            Generic_StaticIO.writeObject(NonDependentIDs, tNonDependentsIDsFile);
            Generic_StaticIO.writeObject(AllIDs, tAllHouseholdIDsFile);
            Generic_StaticIO.writeObject(PairedClaimantIDs, tPairedClaimantIDsFile);
            Generic_StaticIO.writeObject(ClaimantIDToRecordIDLookup, tClaimantIDToRecordIDLookupFile);
            Generic_StaticIO.writeObject(ClaimantIDToPostcodeLookup, tClaimantIDToPostcodeLookupFile);
            Generic_StaticIO.writeObject(ClaimantIDToTenancyTypeLookup, tClaimantIDToTenancyTypeLookupFile);
            Generic_StaticIO.writeObject(CTBRefToClaimantIDLookup, tCTBRefToClaimantIDLookupFile);
            Generic_StaticIO.writeObject(ClaimantIDToCTBRefLookup, tClaimantIDToCTBRefLookupFile);
            Generic_StaticIO.writeObject(LoadSummary, tLoadSummaryFile);
            Generic_StaticIO.writeObject(ClaimantIDAndPostcodeSet, tClaimantIDAndPostcodeFile);
            Generic_StaticIO.writeObject(ClaimantIDAndTenancyTypeSet, tClaimantIDAndTenancyTypeFile);
            Generic_StaticIO.writeObject(ClaimantIDAndPostcodeAndTenancyTypeSet, tClaimantIDAndPostcodeAndTenancyTypeFile);
            Generic_StaticIO.writeObject(RecordIDsNotLoaded, tRecordIDsNotLoadedFile);
        } catch (IOException ex) {
            Logger.getLogger(DW_SHBE_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void doSRecordLoop(
            DW_SHBE_Handler tDW_SHBE_Handler,
            HashMap<String, DW_intID> tNINOToDW_IDLookup,
            HashMap<DW_intID, String> tDW_IDToNINOLookup,
            HashMap<String, DW_intID> tDOBToDW_IDLookup,
            HashMap<DW_intID, String> tDW_IDToDOBLookup,
            HashMap<DW_PersonID, DW_intID> tDW_PersonIDToDW_IDLookup,
            HashMap<DW_intID, DW_PersonID> tDW_IDToDW_PersonIDLookup,
            DW_SHBE_S_Record tSRecord,
            String tCTBRef) {
        String sNINO;
        sNINO = tSRecord.getSubRecordChildReferenceNumberOrNINO();
        if (sNINO.isEmpty()) {
            sNINO = "0";
        }
        /*
         * If we get a simple number here rather 
         * than a NINO, we append the claimant 
         * NINO which in theory should result in 
         * a unique ID for a person. This may not 
         * work for different SHBE extracts as 
         * the number of Dependents may change 
         * over time and the number codes may 
         * alter...
         */
        if (sNINO.length() < 3) {
            sNINO += "_" + tSRecord.getClaimantsNationalInsuranceNumber();
        }
        DW_intID sNINODW_ID;
        sNINODW_ID = tDW_SHBE_Handler.getIDAddIfNeeded(
                sNINO, 
                tNINOToDW_IDLookup,
                tDW_IDToNINOLookup);
        String sDOB;
        sDOB = tSRecord.getSubRecordDateOfBirth();
        DW_intID sDOBDW_ID;
        sDOBDW_ID = tDW_SHBE_Handler.getIDAddIfNeeded(
                sDOB, 
                tDOBToDW_IDLookup,
                tDW_IDToDOBLookup);
        DW_PersonID sDW_PersonID;
        sDW_PersonID = new DW_PersonID(sNINODW_ID, sDOBDW_ID);
        DW_intID sDW_ID = tDW_SHBE_Handler.getIDAddIfNeeded(
                sDW_PersonID,
                tDW_PersonIDToDW_IDLookup,
                tDW_IDToDW_PersonIDLookup);
        if (tSRecord.getSubRecordType() == 2) {
            if (NonDependentIDs.contains(sDW_PersonID)) {
                // This subrecord has the same ID as another.
                // Some subrecords have their
                // NINO set to XX999999XX and it is possible
                // that two have this set and have the same
                // date of birth!
                DW_intID DW_ID;
                DW_ID = tDW_PersonIDToDW_IDLookup.get(sDW_PersonID);
                String previousCTBRef;
                previousCTBRef = SRecordIDToCTBRef.get(DW_ID);
                DW_SHBE_Record previousRecord;
                previousRecord = Records.get(previousCTBRef);
                if (previousRecord == null) {
                    //System.out.println("SRecord does not have a DRecord and SRecord ID not unique");
                    Object[] SRecordIDUpdate;
                    SRecordIDUpdate = getSRecordIDUpdate(
                            tDW_SHBE_Handler,
                            tNINOToDW_IDLookup, 
                            tDW_IDToNINOLookup,
                            tDOBToDW_IDLookup, 
                            tDW_IDToDOBLookup,
                            tDW_PersonIDToDW_IDLookup,
                            tDW_IDToDW_PersonIDLookup, 
                            tSRecord);
                    sDW_ID = (DW_intID) SRecordIDUpdate[0];
                    sDW_PersonID = (DW_PersonID) SRecordIDUpdate[1];
                } else {
                    if (!previousRecord.isPairedRecord()) {
                        //System.out.println("Not Paired DRecord and NonDependent SRecord ID not unique");
                        Object[] SRecordIDUpdate;
                        SRecordIDUpdate = getSRecordIDUpdate(
                                tDW_SHBE_Handler,
                                tNINOToDW_IDLookup, 
                                tDW_IDToNINOLookup,
                                tDOBToDW_IDLookup, 
                                tDW_IDToDOBLookup,
                                tDW_PersonIDToDW_IDLookup,
                                tDW_IDToDW_PersonIDLookup, 
                                tSRecord);
                        sDW_ID = (DW_intID) SRecordIDUpdate[0];
                        sDW_PersonID = (DW_PersonID) SRecordIDUpdate[1];
                    }
                }
            }
            NonDependentIDs.add(sDW_PersonID);
        } else {
            if (DependentIDs.contains(sDW_PersonID)) {
                // This subrecord has the same ID as another.
                // Some subrecords have their
                // NINO set to XX999999XX and it is possible
                // that two have this set and have the same
                // date of birth! Indeed, it is more likley for
                // subrecords than for anything else as there
                // could be twins or tripplets etc...
                DW_intID DW_ID;
                DW_ID = tDW_PersonIDToDW_IDLookup.get(sDW_PersonID);
                String previousCTBRef;
                previousCTBRef = SRecordIDToCTBRef.get(DW_ID);
                DW_SHBE_Record previousRecord;
                previousRecord = Records.get(previousCTBRef);
                if (previousRecord == null) {
                    //System.out.println("SRecord does not have a DRecord and SRecord ID not unique");
                    Object[] SRecordIDUpdate;
                    SRecordIDUpdate = getSRecordIDUpdate(
                            tDW_SHBE_Handler,
                            tNINOToDW_IDLookup,
                            tDW_IDToNINOLookup,
                            tDOBToDW_IDLookup,
                            tDW_IDToDOBLookup,
                            tDW_PersonIDToDW_IDLookup,
                            tDW_IDToDW_PersonIDLookup,
                            tSRecord);
                    sDW_ID = (DW_intID) SRecordIDUpdate[0];
                    sDW_PersonID = (DW_PersonID) SRecordIDUpdate[1];
                } else {
                    if (!previousRecord.isPairedRecord()) {
                        //System.out.println("Not Paired DRecord and Dependent SRecord ID not unique");
                        Object[] SRecordIDUpdate;
                        SRecordIDUpdate = getSRecordIDUpdate(
                                tDW_SHBE_Handler,
                                tNINOToDW_IDLookup,
                                tDW_IDToNINOLookup,
                                tDOBToDW_IDLookup,
                                tDW_IDToDOBLookup,
                                tDW_PersonIDToDW_IDLookup,
                                tDW_IDToDW_PersonIDLookup,
                                tSRecord);
                        sDW_ID = (DW_intID) SRecordIDUpdate[0];
                        sDW_PersonID = (DW_PersonID) SRecordIDUpdate[1];
                    }
                }
            }
            DependentIDs.add(sDW_PersonID);
        }
        SRecordIDToCTBRef.put(sDW_ID, tCTBRef);
        AllIDs.add(sDW_PersonID);
    }

    /**
     *
     * @param SRecord
     * @return
     */
    private Object[] getSRecordIDUpdate(
            DW_SHBE_Handler tDW_SHBE_Handler,
            HashMap<String, DW_intID> NINOToDW_IDLookup,
            HashMap<DW_intID, String> DW_IDToNINOLookup,
            HashMap<String, DW_intID> DOBToDW_IDLookup,
            HashMap<DW_intID, String> DW_IDToDOBLookup,
            HashMap<DW_PersonID, DW_intID> DW_PersonIDToDW_IDLookup,
            HashMap<DW_intID, DW_PersonID> DW_IDToDW_PersonIDLookup,
            DW_SHBE_S_Record SRecord) {
        Object[] result;
        result = new Object[2];
        DW_PersonID sDW_PersonID;
        sDW_PersonID = null;
        DW_intID DW_ID;
        DW_ID = null;
        int i = 1;
        boolean set = false;
        while (!set) {
            String sNINO;
            sNINO = "" + i + "_" + SRecord.getClaimantsNationalInsuranceNumber();
            DW_intID sNINODW_ID;
            sNINODW_ID = tDW_SHBE_Handler.getIDAddIfNeeded(
                    sNINO, NINOToDW_IDLookup,
                    DW_IDToNINOLookup);
            String sDOB;
            sDOB = SRecord.getSubRecordDateOfBirth();
            DW_intID sDOBDW_ID;
            sDOBDW_ID = tDW_SHBE_Handler.getIDAddIfNeeded(
                    sDOB, DOBToDW_IDLookup,
                    DW_IDToDOBLookup);
            sDW_PersonID = new DW_PersonID(sNINODW_ID, sDOBDW_ID);
            if (DW_PersonIDToDW_IDLookup.containsKey(sDW_PersonID)) {
                i++;
            } else {
                DW_intID sDW_ID = tDW_SHBE_Handler.getIDAddIfNeeded(
                        sDW_PersonID,
                        DW_PersonIDToDW_IDLookup,
                        DW_IDToDW_PersonIDLookup);
                DW_ID = sDW_ID;
                set = true;
            }
        }
        result[0] = DW_ID;
        result[1] = sDW_PersonID;
        return result;
    }
//    /**
//     *
//     * @param directory
//     * @param filename
//     * @param handler
//     * @return
//     */
//    public DW_SHBE_Collection(
//            File directory,
//            String filename,
//            DW_SHBE_CollectionHandler handler) {
//        this.handler = handler;
//        InputFile = new File(directory, filename);
//        try {
//            BufferedReader br;
//            br = Generic_StaticIO.getBufferedReader(InputFile);
//            StreamTokenizer st;
//            st = new StreamTokenizer(br);
//            Generic_StaticIO.setStreamTokenizerSyntax5(st);
//            st.wordChars('`', '`');
//            st.wordChars('*', '*');
//            String line = "";
//            int totalCouncilTaxBenefitClaims = 0;
//            int totalCouncilTaxAndHousingBenefitClaims = 0;
//            int totalHousingBenefitClaims = 0;
//            long recordID = 0L;
//            long collectionID = 0L;
//            DW_SHBE_Collection collection;
//            collection = new DW_SHBE_Collection(collectionID, handler);
//            handler.add(collection);
//            TreeMap<String, DW_SHBE_S_Record> SRecordsWithoutDRecords;
//            SRecordsWithoutDRecords = new TreeMap<String, DW_SHBE_S_Record>();
//            init();
//            TreeSet<Long> recordIDsNotLoaded = new TreeSet<Long>();
//            long RecordID = 0;
//            long DRecordID = 0;
//            int countSRecords = 0;
//            int countDRecords = 0;
//            int countOfSRecordsWithoutDRecord = 0;
//            int type = readAndCheckFirstLine(directory, filename);
//            Generic_StaticIO.skipline(st);
//            // Read collections
//            int tokenType;
//            tokenType = st.nextToken();
//            while (tokenType != StreamTokenizer.TT_EOF) {
//                if (RecordID == 10) {
//                    int debug = 1;
//                }
//                switch (tokenType) {
//                    case StreamTokenizer.TT_EOL:
//                        //System.out.println(line);
//                        break;
//                    case StreamTokenizer.TT_WORD:
//                        line = st.sval;
//                        if (line.startsWith("S")) {
//                            try {
//                                DW_SHBE_S_Record aSRecord;
//                                aSRecord = new DW_SHBE_S_Record(RecordID, line);
//                                String aCTBCRN;
//                                aCTBCRN = aSRecord.getCouncilTaxBenefitClaimReferenceNumber();
//                                if (handler.lookup.containsKey(aCTBCRN)) {
//                                    long lookupID = handler.lookup.get(aCTBCRN);
//                                    DW_SHBE_Record DRecord = collection.data.get(lookupID);
//                                    if (!DRecord.getSRecords().add(aSRecord)) {
//                                        throw new Exception("Duplicate SRecord " + aSRecord);
//                                    }
//                                } else {
//                                    countOfSRecordsWithoutDRecord++;
//                                }
//                                String subRecordChildReferenceNumberOrNINO = aSRecord.getSubRecordChildReferenceNumberOrNINO();
//                                if (subRecordChildReferenceNumberOrNINO.length() > 0) {
//                                    if (aSRecord.getSubRecordType() == 2) {
//                                        NonDependentIDs.add(subRecordChildReferenceNumberOrNINO);
//                                        AllIDs.add(subRecordChildReferenceNumberOrNINO);
//                                    } else {
//                                        NonDependentIDs.add(subRecordChildReferenceNumberOrNINO);
//                                        AllIDs.add(subRecordChildReferenceNumberOrNINO);
//                                    }
//                                }
//                            } catch (Exception e) {
//                                System.err.println(line);
//                                System.err.println("RecordID " + RecordID);
//                                System.err.println(e.getLocalizedMessage());
//                                recordIDsNotLoaded.add(RecordID);
//                            }
//                            countSRecords++;
//                        } else {
//                            try {
//                                handler.nextID = DRecordID + 1L;
//                                if (handler.nextID % handler._MaximumNumberPerCollection == 0) {
//                                    collectionID++;
//                                    handler.swapToFile_Collection(collection);
//                                    collection = new DW_SHBE_Collection(collectionID, handler);
//                                }
//                                DW_SHBE_D_Record aDRecord;
//                                aDRecord = new DW_SHBE_D_Record(RecordID, line);
//                                DW_SHBE_Record rec;
//                                rec = new DW_SHBE_Record(RecordID, aDRecord);
//                                collection.addRecord(rec);
//                                //                                Object o;
//                                //                                o = Records.put(
//                                //                                        aSHBE_DataRecord.getCouncilTaxBenefitClaimReferenceNumber(),
//                                //                                        aSHBE_DataRecord);
//                                //                                if (o != null) {
//                                //                                    DW_SHBE_Record existingSHBE_DataRecord = (DW_SHBE_Record) o;
//                                //                                    System.out.println("existingSHBE_DataRecord" + existingSHBE_DataRecord);
//                                //                                    System.out.println("replacementSHBE_DataRecord" + aSHBE_DataRecord);
//                                //                                }
//                                // Count Council Tax and Housing Benefits and combined claims
//                                //                                if (!aDRecord.getCouncilTaxBenefitClaimReferenceNumber().trim().isEmpty()) {
//                                //                                    totalCouncilTaxBenefitClaims++;
//                                //                                    if (!aDRecord.getHousingBenefitClaimReferenceNumber().trim().isEmpty()) {
//                                //                                        totalCouncilTaxAndHousingBenefitClaims++;
//                                //                                        totalHousingBenefitClaims++;
//                                //                                    }
//                                //                                } else {
//                                //                                    if (!aDRecord.getHousingBenefitClaimReferenceNumber().trim().isEmpty()) {
//                                //                                        totalHousingBenefitClaims++;
//                                //                                    }
//                                //                                }
//                                boolean isCurrentHBClaimInPayment;
//                                isCurrentHBClaimInPayment = DW_SHBE_Handler.isHBClaimInPayment(aDRecord);
//                                if (DW_SHBE_Handler.isHBClaimInPayment(aDRecord)) {
//                                    totalCouncilTaxBenefitClaims++;
//                                    if (isCurrentHBClaimInPayment) {
//                                        totalCouncilTaxAndHousingBenefitClaims++;
//                                    }
//                                }
//                                if (isCurrentHBClaimInPayment) {
//                                    totalHousingBenefitClaims++;
//                                }
//                                String claimantID = aDRecord.getClaimantsNationalInsuranceNumber();
//                                if (claimantID.length() > 0) {
//                                    ClaimantIDs.add(claimantID);
//                                    AllIDs.add(claimantID);
//                                    if (aDRecord.getPartnerFlag() > 0) {
//                                        String partnersNationalInsuranceNumber = aDRecord.getPartnersNationalInsuranceNumber();
//                                        PartnerIDs.add(partnersNationalInsuranceNumber);
//                                        AllIDs.add(partnersNationalInsuranceNumber);
//                                    }
//                                }
//                            } catch (Exception e) {
//                                System.err.println(line);
//                                System.err.println("RecordID " + RecordID);
//                                System.err.println(e.getLocalizedMessage());
//                                recordIDsNotLoaded.add(RecordID);
//                            }
//                            countDRecords++;
//                            DRecordID++;
//                        }
//                        RecordID++;
//                        break;
//                }
//                tokenType = st.nextToken();
//            }
//            br.close();
//            // Add all SRecords from recordsWithoutDRecords that actually do have
//            // Records, it just so happened that the DRecord was read after the
//            // first SRecord
//            int countDRecordsInUnexpectedOrder = 0;
//            Iterator<Long> collectionsIDIterator;
//            collectionsIDIterator = handler.collections.keySet().iterator();
//            while (collectionsIDIterator.hasNext()) {
//                collectionID = collectionsIDIterator.next();
//                collection = handler.getCollection(collectionID);
//            }
//            Iterator<String> ite = SRecordsWithoutDRecords.keySet().iterator();
//            HashSet<String> rem = new HashSet<String>();
//            while (ite.hasNext()) {
//                String councilTaxBenefitClaimReferenceNumber = ite.next();
//                if (handler.lookup.keySet().contains(councilTaxBenefitClaimReferenceNumber)) {
//                    DRecordID = handler.lookup.get(councilTaxBenefitClaimReferenceNumber);
//                    collectionID = handler.getCollection_ID(DRecordID);
//                    collection = handler.getCollection(collectionID);
//                    DW_SHBE_Record DRecord = collection.getRecord(DRecordID);
//                    DRecord.SRecords.add(SRecordsWithoutDRecords.get(councilTaxBenefitClaimReferenceNumber));
//                    rem.add(councilTaxBenefitClaimReferenceNumber);
//                    countDRecordsInUnexpectedOrder++;
//                }
//            }
//            ite = rem.iterator();
//            while (ite.hasNext()) {
//                SRecordsWithoutDRecords.remove(ite.next());
//            }
//            System.out.println("totalCouncilTaxBenefitClaims " + totalCouncilTaxBenefitClaims);
//            System.out.println("totalCouncilTaxAndHousingBenefitClaims " + totalCouncilTaxAndHousingBenefitClaims);
//            System.out.println("totalHousingBenefitClaims " + totalHousingBenefitClaims);
//            System.out.println("countDRecords " + handler.getNextID(true));
//            System.out.println("countSRecords " + countSRecords);
//            System.out.println("countOfSRecordsWithoutDRecord " + SRecordsWithoutDRecords.size());
//            System.out.println("countDRecordsInUnexpectedOrder " + countDRecordsInUnexpectedOrder);
//            System.out.println("recordIDsNotLoaded.size() " + recordIDsNotLoaded.size());
//            System.out.println("Count of Unique ClaimantIDs " + ClaimantIDs.size());
//            System.out.println("Count of Unique PartnerIDs " + PartnerIDs.size());
//            System.out.println("Count of Unique DependentsIDs " + DependentIDs.size());
//            System.out.println("Count of Unique NonDependentsIDs " + NonDependentIDs.size());
//            System.out.println("Count of Unique AllHouseholdIDs " + AllIDs.size());
//        } catch (IOException ex) {
//            Logger.getLogger(DW_SHBE_Handler.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    /**
     * Month_10_2010_11_381112_D_records.csv
     * 1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,308,309,310,311,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341
     * hb9803_SHBE_206728k\ April\ 2008.csv
     * 1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,307,308,309,310,311,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341
     * 1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,308,309,310,311,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341
     * 307, 315
     *
     * @param directory
     * @param filename
     * @return
     */
    public static int readAndCheckFirstLine(File directory, String filename) {
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
        File inputFile = new File(directory, filename);
        try {
            BufferedReader br = Generic_StaticIO.getBufferedReader(inputFile);
            StreamTokenizer st = new StreamTokenizer(br);
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

    /**
     * @return the env
     */
    public DW_Environment getEnv() {
        return env;
    }

    /**
     * @return the handler
     */
    public DW_SHBE_CollectionHandler getHandler() {
        return handler;
    }

    /**
     * @return the PaymentType
     */
    public String getPaymentType() {
        return PaymentType;
    }

    /**
     * @return the InputFile
     */
    public File getInputFile() {
        return InputFile;
    }

    /**
     * @return the Records
     */
    public TreeMap<String, DW_SHBE_Record> getRecords() {
        return Records;
    }

    /**
     * @return the SRecordsWithoutDRecords
     */
    public TreeMap<String, DW_SHBE_S_Record> getSRecordsWithoutDRecords() {
        return SRecordsWithoutDRecords;
    }

    /**
     * @return the ClaimantIDs
     */
    public HashSet<DW_PersonID> getClaimantIDs() {
        return ClaimantIDs;
    }

    /**
     * @return the PartnerIDs
     */
    public HashSet<DW_PersonID> getPartnerIDs() {
        return PartnerIDs;
    }

    /**
     * @return the DependentIDs
     */
    public HashSet<DW_PersonID> getDependentIDs() {
        return DependentIDs;
    }

    /**
     * @return the NonDependentIDs
     */
    public HashSet<DW_PersonID> getNonDependentIDs() {
        return NonDependentIDs;
    }

    /**
     * @return the AllIDs
     */
    public HashSet<DW_PersonID> getAllIDs() {
        return AllIDs;
    }

    /**
     * @return the ClaimantIDToRecordIDLookup
     */
    public HashMap<DW_intID, Long> getClaimantIDToRecordIDLookup() {
        return ClaimantIDToRecordIDLookup;
    }

    /**
     * @return the ClaimantIDToPostcodeLookup
     */
    public HashMap<DW_intID, String> getClaimantIDToPostcodeLookup() {
        return ClaimantIDToPostcodeLookup;
    }

    /**
     * @return the ClaimantIDToTenancyTypeLookup
     */
    public HashMap<DW_intID, Integer> getClaimantIDToTenancyTypeLookup() {
        return ClaimantIDToTenancyTypeLookup;
    }

    /**
     * @return the CTBRefToClaimantIDLookup
     */
    public HashMap<String, DW_intID> getCTBRefToClaimantIDLookup() {
        return CTBRefToClaimantIDLookup;
    }

    /**
     * @return the ClaimantIDToCTBRefLookup
     */
    public HashMap<DW_intID, String> getClaimantIDToCTBRefLookup() {
        return ClaimantIDToCTBRefLookup;
    }

    /**
     * @return the LoadSummary
     */
    public HashMap<String, Integer> getLoadSummary() {
        return LoadSummary;
    }

    /**
     * @return the ClaimantIDAndPostcodeSet
     */
    public HashSet<ID_PostcodeID> getClaimantIDAndPostcodeSet() {
        return ClaimantIDAndPostcodeSet;
    }

    /**
     * @return the ClaimantIDAndTenancyTypeSet
     */
    public HashSet<ID_TenancyType> getClaimantIDAndTenancyTypeSet() {
        return ClaimantIDAndTenancyTypeSet;
    }

    /**
     * @return the ClaimantIDAndPostcodeAndTenancyTypeSet
     */
    public HashSet<ID_TenancyType_PostcodeID> getClaimantIDAndPostcodeAndTenancyTypeSet() {
        return ClaimantIDAndPostcodeAndTenancyTypeSet;
    }

    /**
     * @return the RecordIDsNotLoaded
     */
    public TreeSet<Long> getRecordIDsNotLoaded() {
        return RecordIDsNotLoaded;
    }

}
