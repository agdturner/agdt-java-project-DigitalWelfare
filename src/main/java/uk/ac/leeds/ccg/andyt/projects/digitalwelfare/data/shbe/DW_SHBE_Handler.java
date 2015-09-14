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
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Time;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class DW_SHBE_Handler {

    private HashSet<String> RecordTypes;

    public DW_SHBE_Handler() {
        initRecordTypes();
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
     * Loads SHBE collections from a file in directory.
     *
     * @param directory
     * @param filename
     * @return Object[9] result where: null null null     {@code
     * result[0] = TreeMap<String,DW_SHBE_Record> representing records with
     * DRecords;
     * result[1] is a TreeMap<String, DW_SHBE_Record> representing records
     * without DRecords;
     * result[2] is a HashSet<String> of claimantNationalInsuranceNumberIDs;
     * result[3] is a HashSet<String> of partnerNationalInsuranceNumberIDs;
     * result[4] is a HashSet<String> of dependentsNationalInsuranceNumberIDs;
     * result[5] is a HashSet<String> of nonDependentsNationalInsuranceNumberIDs;
     * result[6] is a HashSet<String> of allHouseholdNationalInsuranceNumberIDs;
     * result[7] is a HashMap<String, Long> of claimantNationalInsuranceNumberIDToRecordIDLookup;
     * result[8] is a HashMap<String, String> of NationalInsuranceNumberIDsToPostcode;
     * result[9] is a HashMap<String, Integer> of NationalInsuranceNumberIDsToTenure.
     * }
     */
    public Object[] loadInputData(
            File directory,
            String filename) {
        Object[] result = new Object[10];
        File inputFile = new File(
                directory,
                filename);
        TreeMap<String, DW_SHBE_Record> recs;
        TreeMap<String, DW_SHBE_S_Record> SRecordsWithoutDRecords;
        int totalCouncilTaxBenefitClaims = 0;
        int totalCouncilTaxAndHousingBenefitClaims = 0;
        int totalHousingBenefitClaims = 0;
        int countSRecords = 0;
        int countDRecords = 0;
        int countOfSRecordsWithoutDRecord = 0;
        TreeSet<Long> recordIDsNotLoaded = new TreeSet<Long>();
        HashSet<String> claimantNationalInsuranceNumberIDs;
        HashSet<String> partnerNationalInsuranceNumberIDs;
        HashSet<String> dependentsNationalInsuranceNumberIDs;
        HashSet<String> nonDependentsNationalInsuranceNumberIDs;
        HashSet<String> allHouseholdNationalInsuranceNumberIDs;
        HashMap<String, Long> claimantNationalInsuranceNumberIDToRecordIDLookup;
        HashMap<String, String> claimantNationalInsuranceNumberIDToPostcodeLookup;
        HashMap<String, Integer> claimantNationalInsuranceNumberIDToTenureLookup;
        File claimantNationalInsuranceNumberIDsFile;
        File claimantNationalInsuranceNumberIDToRecordIDLookupFile;
        File claimantNationalInsuranceNumberIDToPostcodeLookupFile;
        File claimantNationalInsuranceNumberIDToTenureLookupFile;

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
            recs = new TreeMap<String, DW_SHBE_Record>();
            result[0] = recs;
            SRecordsWithoutDRecords = new TreeMap<String, DW_SHBE_S_Record>();
            result[1] = SRecordsWithoutDRecords;
            claimantNationalInsuranceNumberIDs = new HashSet<String>();
            result[2] = claimantNationalInsuranceNumberIDs;
            partnerNationalInsuranceNumberIDs = new HashSet<String>();
            result[3] = partnerNationalInsuranceNumberIDs;
            dependentsNationalInsuranceNumberIDs = new HashSet<String>();
            result[4] = dependentsNationalInsuranceNumberIDs;
            nonDependentsNationalInsuranceNumberIDs = new HashSet<String>();
            result[5] = nonDependentsNationalInsuranceNumberIDs;
            allHouseholdNationalInsuranceNumberIDs = new HashSet<String>();
            result[6] = allHouseholdNationalInsuranceNumberIDs;

            // Initialise map and files for writing out 
            String fn;
            fn = filename.substring(0, filename.length() - 4);
            fn += "ClaimantNationalInsuranceNumberIDs_HashSetString.thisFile";
            claimantNationalInsuranceNumberIDsFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    fn);
            fn = filename.substring(0, filename.length() - 4);
            fn += "ClaimantNationalInsuranceNumberIDToRecordIDLookup_HashMapStringLong.thisFile";
            claimantNationalInsuranceNumberIDToRecordIDLookupFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    fn);
            boolean doClaimantNationalInsuranceNumberIDToRecordIDLookup = true;
            if (claimantNationalInsuranceNumberIDToRecordIDLookupFile.exists()) {
                claimantNationalInsuranceNumberIDToRecordIDLookup = (HashMap<String, Long>) Generic_StaticIO.readObject(claimantNationalInsuranceNumberIDToRecordIDLookupFile);
                doClaimantNationalInsuranceNumberIDToRecordIDLookup = false;
            } else {
                claimantNationalInsuranceNumberIDToRecordIDLookup = new HashMap<String, Long>();
            }
            result[7] = claimantNationalInsuranceNumberIDToRecordIDLookup;
            fn = filename.substring(0, filename.length() - 4);
            fn += "ClaimantNationalInsuranceNumberIDToPostcodeLookup_HashMapStringString.thisFile";
            claimantNationalInsuranceNumberIDToPostcodeLookupFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    fn);
            boolean doClaimantNationalInsuranceNumberIDToPostcodeLookup = true;
            if (!claimantNationalInsuranceNumberIDToPostcodeLookupFile.exists()) {
                claimantNationalInsuranceNumberIDToPostcodeLookup = new HashMap<String, String>();
            } else {
                claimantNationalInsuranceNumberIDToPostcodeLookup = (HashMap<String, String>) Generic_StaticIO.readObject(claimantNationalInsuranceNumberIDToPostcodeLookupFile);
                doClaimantNationalInsuranceNumberIDToPostcodeLookup = false;
            }
            result[8] = claimantNationalInsuranceNumberIDToPostcodeLookup;
            fn = filename.substring(0, filename.length() - 4);
            fn += "ClaimantNationalInsuranceNumberIDToTenureLookup_HashMapStringInteger.thisFile";
            claimantNationalInsuranceNumberIDToTenureLookupFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    fn);
            boolean doClaimantNationalInsuranceNumberIDToTenureLookup = true;
            if (!claimantNationalInsuranceNumberIDToTenureLookupFile.exists()) {
                claimantNationalInsuranceNumberIDToTenureLookup = new HashMap<String, Integer>();
            } else {
                claimantNationalInsuranceNumberIDToTenureLookup = (HashMap<String, Integer>) Generic_StaticIO.readObject(claimantNationalInsuranceNumberIDToTenureLookupFile);
                doClaimantNationalInsuranceNumberIDToTenureLookup = false;
            }
            result[9] = claimantNationalInsuranceNumberIDToTenureLookup;

            long RecordID = 0;

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
                                    if (recs.containsKey(CTBRef)) {
                                        DW_SHBE_Record rec = recs.get(CTBRef);
                                        if (!rec.getSRecords().add(SRecord)) {
                                            throw new Exception("Duplicate SRecord " + SRecord);
                                        }
                                    } else {
                                        //throw new Exception("There is no existing DRecord for SRecord " + SRecord);
                                        countOfSRecordsWithoutDRecord++;
                                    }
                                    String subRecordChildReferenceNumberOrNINO = SRecord.getSubRecordChildReferenceNumberOrNINO();
                                    if (subRecordChildReferenceNumberOrNINO.length() > 0) {
                                        if (SRecord.getSubRecordType() == 2) {
                                            nonDependentsNationalInsuranceNumberIDs.add(subRecordChildReferenceNumberOrNINO);
                                            allHouseholdNationalInsuranceNumberIDs.add(subRecordChildReferenceNumberOrNINO);
                                        } else {
                                            dependentsNationalInsuranceNumberIDs.add(subRecordChildReferenceNumberOrNINO);
                                            allHouseholdNationalInsuranceNumberIDs.add(subRecordChildReferenceNumberOrNINO);
                                        }
                                    }
                                } else {
                                    recordIDsNotLoaded.add(RecordID);
                                }
                            } catch (Exception e) {
                                System.err.println(line);
                                System.err.println("RecordID " + RecordID);
                                System.err.println(e.getLocalizedMessage());
                                recordIDsNotLoaded.add(RecordID);
                            }
                            countSRecords++;
                        } else {
                            if (line.startsWith("D")) {
                                try {

                                    // Special Case 1338
                                    if (line.equalsIgnoreCase("D,100024459,100024459, NY804724D,, 02-04-1930, 110 BURLINGTON ROAD, LS11 7DP,2,0,0, , , ,0, ,0, , , , , , , , ,0, , , ,1,1, , , , , , , 07-04-2003, ,7507,1286,3,3, , , , , , , ,1,0, A,7507,1286, N,2, , ,4, , , , ,7507, , , , , , , ,0,0,0,0,0,0,0,0,0, ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, ,0, , , , , , ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, , , , , , , F, , ,2,7507,1, , , , , , , , , , , , , , , , , , , 01-04-2003, , , , , , , , , , , , , , , , , , , ,0, ,0,0, , , , , ,0,0, , , ,0, , , , , , , , ,24399,0,9, , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , ,2,2, , 06-10-2014, , , , , , , , , LEEDS, WEST YORKSHIRE, LS 3 1DE, , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , ,,,")) {
                                        int debug = 1;
                                    }
                                    /*
                                     8*D,100024459,100024459, NY804724D,, 02-04-1930, 110 BURLINGTON ROAD, LS11 7DP,
                                     23*2,0,0, , , ,0, ,0, , , , , , , , ,0, , , ,1,1,
                                     8* , , , , , , 07-04-2003, ,
                                     25*7507,1286,3,3, , , , , , , ,1,0, A,7507,1286, N,2, , ,4, , , , ,7507,
                                     *, , , , , , ,0,0,0,0,0,0,0,0,0, ,
                                     *0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                                     *0,0,0,0, ,0, , , , , , ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                                     *0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, , , , , , , F,
                                     *, ,2,7507,1, , , , , , , , , , , , , , , , , , , 01-04-2003,
                                     *, , , , , , , , , , , , , , , , , , ,0, ,0,0,
                                     *, , , , ,0,0, , , ,0, , , , , , , , ,24399,0,9,
                                     *, , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , ,2,2, , 06-10-2014,
                                     *, , , , , , , , LEEDS, WEST YORKSHIRE, LS 3 1DE,
                                     *, , , , , , , , , , , , , , , , , , , , , , , , , , , , , , ,,,
                                     /*
                                
                                     */
                                    // Special Case 1488
                                    if (line.equalsIgnoreCase("D,100028100,100028100, YR363178B, 23-11-1952,2, FLAT 3, LS4 2LB,5,0,0, , , ,0, ,0, , , , , , , , ,0, , , ,1,1, , , , , , , 07-04-2003, ,8400,952,3,3, , , , , , , ,1,0, A,8400,1286, N,2, , ,4, , , , ,8400, , , , , , , ,0,0,0,0,0,0,0,0,0, ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, ,0, , , , , , ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, , , , , , , F, , ,2,8400,1, , , , , , , , , , , , , , , , , , , 01-04-2003, , , , , , , , , , , , , , , , , , , ,0, ,0,0, , , , , ,0,0, , , ,0, , , , , , , , ,33600,0,1, , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , ,2,2, , 06-10-2014, , , , , , , ,1, LEEDS, WEST YORKSHIRE, LS 4 2LB, , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , ,,,")) {
                                        int debug = 1;
                                    }
                                    /*
                                     8*D,100028100,100028100, YR363178B, 23-11-1952,2, FLAT 3, LS4 2LB,
                                     23*5,0,0, , , ,0, ,0, , , , , , , , ,0, , , ,1,1,
                                     8*, , , , , , 07-04-2003, ,8400,
                                     25*952,3,3, , , , , , , ,1,0, A,8400,1286, N,2, , ,4, , , , ,8400,
                                     *, , , , , , ,0,0,0,0,0,0,0,0,0, ,
                                     *0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                                     *0,0,0,0, ,0, , , , , , ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                                     *0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, , , , , , , F,
                                     *, ,2,8400,1, , , , , , , , , , , , , , , , , , , 01-04-2003,
                                     *, , , , , , , , , , , , , , , , , , ,0, ,0,0,
                                     *, , , , ,0,0, , , ,0, , , , , , , , ,33600,0,1,
                                     *, , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , ,2,2, , 06-10-2014,
                                     *, , , , , , ,1, LEEDS, WEST YORKSHIRE, LS 4 2LB,
                                     *, , , , , , , , , , , , , , , , , , , , , , , , , , , , , , ,,,
                                     */

                                    /*
                                     7*D,100000013,100000013, YY895089D, 28-02-1957,1, LS14 5LP,
                                     23*4,1,0, , , ,1, 04-10-2012,0,1112, , , , , , , ,0, , , ,1,0,
                                     8*18-09-2012, 18-09-2012, 04-10-2012, 04-10-2012,1,3, 24-09-2012, ,
                                     13*2627, ,1, , , , , , , , , ,0, A,7945,1286, N,2, , ,4, , , , ,7945,
                                     *, , , , , ,1,13416,13080,0,0,0,0,0,0,2050, ,
                                     *0,0,0,0,0,0,0,0,0,0,0,0,13218,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,
                                     *0,0,0,0, ,0, , , , , , ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, , , , , , , F,
                                     *, , ,7945,1, , ,164, , , , , , , , , , , , , , , , , , , , ,2, , , , , , , , , , , , , 18-09-2012,1,0, ,0,0,
                                     *, , , 18-09-2012,1,0,0, , , ,0, , , ,0,17, , , ,10508,0,1,
                                     *, , , , , , , , , , , , , , , , , , , , , 18-09-2012,
                                     *, , 18-09-2012, , , , , ,3, ,2,2,0, 06-10-2014, , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , ,,,,,
                                     */
                                    /*
                                     *D,100024468,100024468, AY926220C, 22-07-1923,1, LS27 8SE,
                                     *2,0,0, , , ,0, ,0, , , , , , , , ,0, , , ,1,1,
                                     *12-12-2007, , 18-12-2007, ,1, , 17-12-2007, ,
                                     *7760,1305,1,3, , , , , , , , ,0, A,7760,1305, N,2, , ,4, , , , ,7760,
                                     *, , , , , , ,0,0,0,0,0,0,0,0,0, ,
                                     *0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                                     *0,0, ,0, , , , , , ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, , , , , , , F,
                                     *, , ,7760,1, , , , , , , , , , , , , , , , , , , 01-04-2003, , , , , , , , , , , , , , , , , , 12-12-2007,1,0, ,0,0,
                                     *, , , ,1,0,0, , , ,0, , , , , , , , ,31040,0,99,
                                     *, , , , , , , , , , , , , , , , , , , , , 12-12-2007,
                                     *, , , , , , , , , ,2,2,0, 06-10-2014, , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , ,,,,,
                                     */
                                    DW_SHBE_Record rec;
                                    rec = new DW_SHBE_Record(RecordID);
                                    DW_SHBE_D_Record aDRecord;
                                    aDRecord = new DW_SHBE_D_Record(RecordID, line, this);
                                    rec.DRecord = aDRecord;
                                    String CTBRef;
                                    CTBRef = aDRecord.getCouncilTaxBenefitClaimReferenceNumber();
                                    if (CTBRef != null) {
                                        Object o = recs.put(CTBRef, rec);
                                        if (o != null) {
                                            DW_SHBE_Record existingSHBE_DataRecord = (DW_SHBE_Record) o;
                                            System.out.println("existingSHBE_DataRecord" + existingSHBE_DataRecord);
                                            System.out.println("replacementSHBE_DataRecord" + aDRecord);
                                        }
                                        // Count Council Tax and Housing Benefits and combined claims
                                        if (!aDRecord.getCouncilTaxBenefitClaimReferenceNumber().trim().isEmpty()) {
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
                                        // aSHBE_DataRecord.getNonDependantStatus 11
                                        // aSHBE_DataRecord.getSubRecordType() 284
                                        String claimantsNationalInsuranceNumber = aDRecord.getClaimantsNationalInsuranceNumber();
                                        if (claimantsNationalInsuranceNumber.length() > 0) {
                                            claimantNationalInsuranceNumberIDs.add(claimantsNationalInsuranceNumber);
                                            if (doClaimantNationalInsuranceNumberIDToRecordIDLookup) {
                                                claimantNationalInsuranceNumberIDToRecordIDLookup.put(
                                                        claimantsNationalInsuranceNumber,
                                                        RecordID);
                                            }
                                            if (doClaimantNationalInsuranceNumberIDToPostcodeLookup) {
                                                claimantNationalInsuranceNumberIDToPostcodeLookup.put(
                                                        claimantsNationalInsuranceNumber,
                                                        aDRecord.getClaimantsPostcode());
                                            }
                                            if (doClaimantNationalInsuranceNumberIDToTenureLookup) {
                                                claimantNationalInsuranceNumberIDToTenureLookup.put(
                                                        claimantsNationalInsuranceNumber,
                                                        aDRecord.getTenancyType());
                                            }
                                            allHouseholdNationalInsuranceNumberIDs.add(claimantsNationalInsuranceNumber);
//                                // aSHBE_DataRecord.getPartnerFlag() 118
                                            if (aDRecord.getPartnerFlag() > 0) {
                                                String partnersNationalInsuranceNumber = aDRecord.getPartnersNationalInsuranceNumber();
                                                partnerNationalInsuranceNumberIDs.add(partnersNationalInsuranceNumber);
                                                allHouseholdNationalInsuranceNumberIDs.add(partnersNationalInsuranceNumber);
                                            }
                                        }
                                    } else {
                                        recordIDsNotLoaded.add(RecordID);
                                    }
                                } catch (Exception e) {
                                    System.err.println(line);
                                    System.err.println("RecordID " + RecordID);
                                    //System.err.println(e.getMessage());
                                    System.err.println(e.getLocalizedMessage());
                                    e.printStackTrace();
                                    recordIDsNotLoaded.add(RecordID);
                                }
                                countDRecords++;
                            }
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
            Set<String> s = recs.keySet();
            Iterator<String> ite = SRecordsWithoutDRecords.keySet().iterator();
            HashSet<String> rem = new HashSet<String>();
            while (ite.hasNext()) {
                String councilTaxBenefitClaimReferenceNumber = ite.next();
                if (s.contains(councilTaxBenefitClaimReferenceNumber)) {
                    DW_SHBE_Record DRecord = recs.get(councilTaxBenefitClaimReferenceNumber);
                    DRecord.SRecords.addAll(recs.get(councilTaxBenefitClaimReferenceNumber).getSRecords());
                    rem.add(councilTaxBenefitClaimReferenceNumber);
                    countDRecordsInUnexpectedOrder++;
                }
            }
            //System.out.println("SRecords that came before DRecords count " + countDRecordsInUnexpectedOrder);
            ite = rem.iterator();
            while (ite.hasNext()) {
                SRecordsWithoutDRecords.remove(ite.next());
            }
            // Summary report of load
            System.out.println("totalCouncilTaxBenefitClaims " + totalCouncilTaxBenefitClaims);
            System.out.println("totalCouncilTaxAndHousingBenefitClaims " + totalCouncilTaxAndHousingBenefitClaims);
            System.out.println("totalHousingBenefitClaims " + totalHousingBenefitClaims);
            System.out.println("countDRecords " + recs.size());
            System.out.println("countSRecords " + countSRecords);
            System.out.println("countOfSRecordsWithoutDRecord " + SRecordsWithoutDRecords.size());
            System.out.println("countDRecordsInUnexpectedOrder " + countDRecordsInUnexpectedOrder);
            System.out.println("recordIDsNotLoaded.size() " + recordIDsNotLoaded.size());
            System.out.println("Count of Unique Claimant NationalInsuranceNumbers " + claimantNationalInsuranceNumberIDs.size());
            System.out.println("Count of Unique Partner NationalInsuranceNumbers " + partnerNationalInsuranceNumberIDs.size());
            System.out.println("Count of Unique Dependents NationalInsuranceNumbers " + dependentsNationalInsuranceNumberIDs.size());
            System.out.println("Count of Unique Non-Dependents NationalInsuranceNumbers " + nonDependentsNationalInsuranceNumberIDs.size());
            System.out.println("Count of Unique All Household NationalInsuranceNumbers " + allHouseholdNationalInsuranceNumberIDs.size());

            // Store claimantNationalInsuranceNumberIDs on File
            Generic_StaticIO.writeObject(
                    claimantNationalInsuranceNumberIDs,
                    claimantNationalInsuranceNumberIDsFile);
            // Store claimantNationalInsuranceNumberIDToRecordIDLookup on File
            if (doClaimantNationalInsuranceNumberIDToRecordIDLookup) {
                Generic_StaticIO.writeObject(
                        claimantNationalInsuranceNumberIDToRecordIDLookup,
                        claimantNationalInsuranceNumberIDToRecordIDLookupFile);
            }
            // Store claimantNationalInsuranceNumberIDToPostcodeLookup on File
            if (doClaimantNationalInsuranceNumberIDToPostcodeLookup) {
                Generic_StaticIO.writeObject(
                        claimantNationalInsuranceNumberIDToPostcodeLookup,
                        claimantNationalInsuranceNumberIDToPostcodeLookupFile);
            }
            // Store claimantNationalInsuranceNumberIDToTenureLookupFile on File
            if (doClaimantNationalInsuranceNumberIDToTenureLookup) {
                Generic_StaticIO.writeObject(
                        claimantNationalInsuranceNumberIDToTenureLookup,
                        claimantNationalInsuranceNumberIDToTenureLookupFile);
            }
        } catch (IOException ex) {
            Logger.getLogger(DW_SHBE_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Loads SHBE collections from a file in directory, filename reporting
     * progress of loading to PrintWriter pw.
     *
     * @param directory
     * @param filename
     * @param handler
     * @return Object[7] result where: -----------------------------------------
     * result[0] is handler-----------------------------------------------------
     * result[1] is a TreeMap<String, DW_SHBE_Record> representing records
     * without DRecords, -------------------------------------------------------
     * result[2] is a HashSet\<String\> of ClaimantNationalInsuranceNumberIDs,--
     * result[3] is a HashSet\<String\> of PartnerNationalInsuranceNumberIDs,---
     * result[4] is a HashSet\<String\> of DependentsNationalInsuranceNumberIDs,
     * result[5] is a HashSet\<String\> of
     * NonDependentsNationalInsuranceNumberIDs,---------------------------------
     * result[6] is a HashSet\<String\> of
     * AllHouseholdNationalInsuranceNumberIDs.
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
                                if (!aDRecord.getCouncilTaxBenefitClaimReferenceNumber().trim().isEmpty()) {
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
                                // aSHBE_DataRecord.getNonDependantStatus 11
                                // aSHBE_DataRecord.getSubRecordType() 284
                                String claimantsNationalInsuranceNumber = aDRecord.getClaimantsNationalInsuranceNumber();
                                if (claimantsNationalInsuranceNumber.length() > 0) {
                                    ClaimantNationalInsuranceNumberIDs.add(claimantsNationalInsuranceNumber);
                                    AllHouseholdNationalInsuranceNumberIDs.add(claimantsNationalInsuranceNumber);
//                                // aSHBE_DataRecord.getPartnerFlag() 118
                                    if (aDRecord.getPartnerFlag() > 0) {
                                        String partnersNationalInsuranceNumber = aDRecord.getPartnersNationalInsuranceNumber();
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
            System.out.println("Count of Unique ClaimantNationalInsuranceNumberIDs " + ClaimantNationalInsuranceNumberIDs.size());
            System.out.println("Count of Unique PartnerNationalInsuranceNumberIDs " + PartnerNationalInsuranceNumberIDs.size());
            System.out.println("Count of Unique DependentsNationalInsuranceNumberIDs " + DependentsNationalInsuranceNumberIDs.size());
            System.out.println("Count of Unique NonDependentsNationalInsuranceNumberIDs " + NonDependentsNationalInsuranceNumberIDs.size());
            System.out.println("Count of Unique AllHouseholdNationalInsuranceNumberIDs " + AllHouseholdNationalInsuranceNumberIDs.size());
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
    public int readAndCheckFirstLine(
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
                + aDRecord.getClaimantsIncomeFromGovernemntTraining());
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
                + aDRecord.getClaimantsIncomeFromWidowsWidowersPension());
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
                + aDRecord.getClaimantsIncomeFromStatuatorySickPay());
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
                + aDRecord.getPartnersIncomeFromGovernemntTraining());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromIndustrialInjuriesDisablementBenefit(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromIndustrialInjuriesDisablementBenefit()
                + aDRecord.getPartnersIncomeFromIndustrialInjuriesDisablementBenefit());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromCarersAllowance(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromCarersAllowance()
                + aDRecord.getPartnersIncomeFromCarersAllowance());
        a_Aggregate_SHBE_DataRecord.setTotalPartnersIncomeFromStatuatorySickPay(
                a_Aggregate_SHBE_DataRecord.getTotalPartnersIncomeFromStatuatorySickPay()
                + aDRecord.getPartnersIncomeFromStatuatorySickPay());
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
                + aDRecord.getPartnersIncomeFromWidowsWidowersPension());
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

    public static long getClaimantsIncomeTotal(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getClaimantsIncomeFromAttendanceAllowance();
        result += aDRecord.getClaimantsIncomeFromBereavementAllowance();
        result += aDRecord.getClaimantsIncomeFromBoarders();
        result += aDRecord.getClaimantsIncomeFromBusinessStartUpAllowance();
        result += aDRecord.getClaimantsIncomeFromCarersAllowance();
        result += aDRecord.getClaimantsIncomeFromChildBenefit();
        result += aDRecord.getClaimantsIncomeFromContributionBasedJobSeekersAllowance();
        result += aDRecord.getClaimantsIncomeFromDisabilityLivingAllowanceCareComponent();
        result += aDRecord.getClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent();
        result += aDRecord.getClaimantsIncomeFromGovernemntTraining();
        result += aDRecord.getClaimantsIncomeFromIncapacityBenefitLongTerm();
        result += aDRecord.getClaimantsIncomeFromIncapacityBenefitShortTermHigher();
        result += aDRecord.getClaimantsIncomeFromIncapacityBenefitShortTermLower();
        result += aDRecord.getClaimantsIncomeFromIndustrialInjuriesDisablementBenefit();
        result += aDRecord.getClaimantsIncomeFromMaintenancePayments();
        result += aDRecord.getClaimantsIncomeFromMaternityAllowance();
        result += aDRecord.getClaimantsIncomeFromNewDeal50PlusEmploymentCredit();
        result += aDRecord.getClaimantsIncomeFromNewTaxCredits();
        result += aDRecord.getClaimantsIncomeFromOccupationalPension();
        result += aDRecord.getClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent();
        result += aDRecord.getClaimantsIncomeFromPensionCreditSavingsCredit();
        result += aDRecord.getClaimantsIncomeFromPersonalPension();
        result += aDRecord.getClaimantsIncomeFromSevereDisabilityAllowance();
        result += aDRecord.getClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc();
        result += aDRecord.getClaimantsIncomeFromStatuatorySickPay();
        result += aDRecord.getClaimantsIncomeFromStatutoryMaternityPaternityPay();
        result += aDRecord.getClaimantsIncomeFromStudentGrantLoan();
        result += aDRecord.getClaimantsIncomeFromSubTenants();
        result += aDRecord.getClaimantsIncomeFromTrainingForWorkCommunityAction();
        result += aDRecord.getClaimantsIncomeFromWarDisablementPensionArmedForcesGIP();
        result += aDRecord.getClaimantsIncomeFromWarMobilitySupplement();
        result += aDRecord.getClaimantsIncomeFromWidowedParentsAllowance();
        result += aDRecord.getClaimantsIncomeFromWidowsBenefit();
        result += aDRecord.getClaimantsIncomeFromWidowsWidowersPension();
        result += aDRecord.getClaimantsIncomeFromYouthTrainingScheme();
        return result;
    }

    public static long getPartnersIncomeTotal(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getPartnersIncomeFromAttendanceAllowance();
        result += aDRecord.getPartnersIncomeFromBereavementAllowance();
        result += aDRecord.getPartnersIncomeFromBoarders();
        result += aDRecord.getPartnersIncomeFromBusinessStartUpAllowance();
        result += aDRecord.getPartnersIncomeFromCarersAllowance();
        result += aDRecord.getPartnersIncomeFromChildBenefit();
        result += aDRecord.getPartnersIncomeFromContributionBasedJobSeekersAllowance();
        result += aDRecord.getPartnersIncomeFromDisabilityLivingAllowanceCareComponent();
        result += aDRecord.getPartnersIncomeFromDisabilityLivingAllowanceMobilityComponent();
        result += aDRecord.getPartnersIncomeFromGovernemntTraining();
        result += aDRecord.getPartnersIncomeFromIncapacityBenefitLongTerm();
        result += aDRecord.getPartnersIncomeFromIncapacityBenefitShortTermHigher();
        result += aDRecord.getPartnersIncomeFromIncapacityBenefitShortTermLower();
        result += aDRecord.getPartnersIncomeFromIndustrialInjuriesDisablementBenefit();
        result += aDRecord.getPartnersIncomeFromMaintenancePayments();
        result += aDRecord.getPartnersIncomeFromMaternityAllowance();
        result += aDRecord.getPartnersIncomeFromNewDeal50PlusEmploymentCredit();
        result += aDRecord.getPartnersIncomeFromNewTaxCredits();
        result += aDRecord.getPartnersIncomeFromOccupationalPension();
        result += aDRecord.getPartnersIncomeFromPensionCreditSavingsCredit();
        result += aDRecord.getPartnersIncomeFromPersonalPension();
        result += aDRecord.getPartnersIncomeFromSevereDisabilityAllowance();
        result += aDRecord.getPartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc();
        result += aDRecord.getPartnersIncomeFromStatuatorySickPay();
        result += aDRecord.getPartnersIncomeFromStatutoryMaternityPaternityPay();
        result += aDRecord.getPartnersIncomeFromStudentGrantLoan();
        result += aDRecord.getPartnersIncomeFromSubTenants();
        result += aDRecord.getPartnersIncomeFromTrainingForWorkCommunityAction();
        result += aDRecord.getPartnersIncomeFromWarDisablementPensionArmedForcesGIP();
        result += aDRecord.getPartnersIncomeFromWarMobilitySupplement();
        result += aDRecord.getPartnersIncomeFromWidowedParentsAllowance();
        result += aDRecord.getPartnersIncomeFromWidowsBenefit();
        result += aDRecord.getPartnersIncomeFromWidowsWidowersPension();
        result += aDRecord.getPartnersIncomeFromYouthTrainingScheme();
        return result;
    }

    public static long getIncomeTotal(
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
     */
    public static String[] getSHBEFilenamesAll() {
//        String[] result = new String[1];
//        result[0] = "hb9991_SHBE_670535k October 2014 v2.csv";
        String[] result = new String[44];
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
        result[34] = "hb9991_SHBE_663169k September 2014.csv";
        //result[35] = "hb9991_SHBE_670535k October 2014.csv";
        result[35] = "hb9991_SHBE_670535k October 2014 v2.csv";
        result[36] = "hb9991_SHBE_677543k November 2014.csv";
        result[37] = "hb9991_SHBE_684519k December 2014.csv";
        result[38] = "hb9991_SHBE_691401k January 2015.csv";
        result[39] = "hb9991_SHBE_697933k February 2015.csv";
        result[40] = "hb9991_SHBE_705679k March 2015.csv";
        result[41] = "hb9991_SHBE_712197k April 2015.csv";
        result[42] = "hb9991_SHBE_718782k May 2015.csv";
        result[43] = "hb9991_SHBE_725465k June 2015.csv";
        return result;
    }

    /**
     * 
     * @return 
     * {@code
        Object[] result;
        result = new Object[2];
        TreeMap<BigDecimal, String> valueLabel;
        valueLabel = new TreeMap<BigDecimal, String>();
        TreeMap<String, BigDecimal> fileLabelValue;
        fileLabelValue = new TreeMap<String, BigDecimal>();
        result[0] = valueLabel;
        result[1] = fileLabelValue;     
       }
     */
    public static Object[] getTreeMapDateLabelSHBEFilenames() {
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
        BigDecimal startTime;
        startTime = BigDecimal.ZERO;
        int startYear = 0;
        int startMonth = 0;
        boolean first = true;
        String[] tSHBEFilenamesAll;
        tSHBEFilenamesAll = getSHBEFilenamesAll();
        for (int i = 0; i < tSHBEFilenamesAll.length; i++) {
            String year;
            int yearInt;
            String month;
            int monthInt;
            year = getYear(tSHBEFilenamesAll[i]);
            month = getMonth(tSHBEFilenamesAll[i]);
            yearInt = Integer.valueOf(year);
            String m3;
            m3 = month.substring(0, 3);
            monthInt = month3Letters.indexOf(m3) + 1;
            if (first) {
                startYear = yearInt;
                startMonth = monthInt;
                first = false;
            } else {
                BigDecimal timeSinceStart;
                timeSinceStart = BigDecimal.valueOf(Generic_Time.getMonthDiff(
                        startYear, yearInt, startMonth, monthInt));
                valueLabel.put(
                        timeSinceStart,
                        year + " " + m3);
                fileLabelValue.put(
                        year + "_" + month,
                        timeSinceStart);
            }
        }
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

    public static ArrayList<Integer> getTenureTypeAll() {
        ArrayList<Integer> result;
        result = new ArrayList<Integer>();
        result.add(0);
        result.add(1);
        result.add(2);
        result.add(3);
        result.add(4);
        result.add(5);
        result.add(6);
        result.add(7);
        result.add(8);
        result.add(9);
        return result;
    }

    public static ArrayList<Integer> getTenureTypeRegulated() {
        ArrayList<Integer> result;
        result = new ArrayList<Integer>();
        result.add(1);
        result.add(4);
        result.add(2);
        return result;
    }

    public static ArrayList<Integer> getTenureTypeUnregulated() {
        ArrayList<Integer> result;
        result = new ArrayList<Integer>();
        result.add(3);
        return result;
    }
}
