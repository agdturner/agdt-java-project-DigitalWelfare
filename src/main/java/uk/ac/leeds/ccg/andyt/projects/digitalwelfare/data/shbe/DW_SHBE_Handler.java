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
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.geotools.Geotools_Point;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;
import uk.ac.leeds.ccg.andyt.generic.math.Generic_BigDecimal;
import uk.ac.leeds.ccg.andyt.generic.util.Generic_Time;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Set;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.util.DW_YM3;

/**
 * Class for handling DW_SHBE_Data.
 *
 * @author geoagdt
 */
public class DW_SHBE_Handler extends DW_Object {

    /**
     * For convenience, these are initialised in construction from Env.
     */
    private final transient DW_SHBE_Data SHBE_Data;
    private final transient HashMap<String, DW_ID> NINOToNINOIDLookup;
    private final transient HashMap<String, DW_ID> DOBToDOBIDLookup;
    private final transient DW_Strings Strings;
    private final transient DW_Files Files;
    private final transient DW_Postcode_Handler Postcode_Handler;

    /**
     * For a set of expected RecordTypes. ("A", "D", "C", "R", "T", "P", "G",
     * "E", "S").
     */
    protected HashSet<String> RecordTypes;

//    
//    public DW_SHBE_Handler() {
//    }
    public DW_SHBE_Handler(DW_Environment e) {
        super(e);
        SHBE_Data = e.getSHBE_Data();
        Strings = e.getStrings();
        Files = e.getFiles();
        Postcode_Handler = e.getPostcode_Handler();
        NINOToNINOIDLookup = SHBE_Data.getNINOToNINOIDLookup();
        DOBToDOBIDLookup = SHBE_Data.getDOBToDOBIDLookup();
    }

    /**
     * For loading in all SHBE Data.
     *
     * @param logDir
     */
    public void run(File logDir) {
        String[] SHBEFilenames;
        SHBEFilenames = getSHBEFilenamesAll();
        DW_YM3 LastYM3;
        LastYM3 = getYM3(SHBEFilenames[SHBEFilenames.length - 1]);
        DW_YM3 NearestYM3ForONSPDFormatLookupLastYM3;
        NearestYM3ForONSPDFormatLookupLastYM3 = Postcode_Handler.getNearestYM3ForONSPDLookup(LastYM3);
        File dir;
        dir = Files.getInputSHBEDir();
        for (String SHBEFilename : SHBEFilenames) {
            DW_SHBE_Records DW_SHBE_Records;
            DW_SHBE_Records = new DW_SHBE_Records(
                    Env,
                    dir,
                    SHBEFilename,
                    NearestYM3ForONSPDFormatLookupLastYM3,
                    logDir);
            Generic_IO.writeObject(DW_SHBE_Records, DW_SHBE_Records.getFile());
        }
        writeLookups();
        // Make a backup copy
        File SHBEbackup;
        SHBEbackup = new File(Files.getGeneratedLCCDir(), "SHBEBackup");
        if (SHBEbackup.isDirectory()) {
            SHBEbackup = Generic_IO.addToArchive(SHBEbackup, 100);
        } else {
            SHBEbackup = Generic_IO.initialiseArchive(SHBEbackup, 100);
        }
        Generic_IO.copy(Files.getGeneratedSHBEDir(), SHBEbackup);
    }

    public void writeLookups() {
        Generic_IO.writeObject(
                SHBE_Data.getClaimIDToClaimRefLookup(),
                SHBE_Data.getClaimIDToClaimRefLookupFile());
        Generic_IO.writeObject(
                SHBE_Data.getClaimRefToClaimIDLookup(),
                SHBE_Data.getClaimRefToClaimIDLookupFile());
        Generic_IO.writeObject(
                SHBE_Data.getNINOToNINOIDLookup(),
                SHBE_Data.getNINOToNINOIDLookupFile());
        Generic_IO.writeObject(
                SHBE_Data.getNINOIDToNINOLookup(),
                SHBE_Data.getNINOIDToNINOLookupFile());
        Generic_IO.writeObject(
                SHBE_Data.getDOBToDOBIDLookup(),
                SHBE_Data.getDOBToDOBIDLookupFile());
        Generic_IO.writeObject(
                SHBE_Data.getDOBIDToDOBLookup(),
                SHBE_Data.getDOBIDToDOBLookupFile());
        Generic_IO.writeObject(
                SHBE_Data.getPostcodeToPostcodeIDLookup(),
                SHBE_Data.getPostcodeToPostcodeIDLookupFile());
        Generic_IO.writeObject(
                SHBE_Data.getPostcodeIDToPostcodeLookup(),
                SHBE_Data.getPostcodeIDToPostcodeLookupFile());
        Generic_IO.writeObject(
                SHBE_Data.getPostcodeIDToPointLookups(),
                SHBE_Data.getPostcodeIDToPointLookupsFile());
        Generic_IO.writeObject(
                SHBE_Data.getClaimantPersonIDs(),
                SHBE_Data.getClaimantPersonIDsFile());
        Generic_IO.writeObject(
                SHBE_Data.getPartnerPersonIDs(),
                SHBE_Data.getPartnerPersonIDsFile());
        Generic_IO.writeObject(
                SHBE_Data.getNonDependentPersonIDs(),
                SHBE_Data.getNonDependentPersonIDsFile());
        Generic_IO.writeObject(
                SHBE_Data.getPersonIDToClaimIDLookup(),
                SHBE_Data.getPersonIDToClaimIDLookupFile());
    }

    /**
     * For loading in new SHBE data
     *
     * @param logDir
     */
    public void runNew(File logDir) {
        File dir;
        dir = Env.getFiles().getInputSHBEDir();
        // Ascertain which files are new and need loading
        // Get all filenames
        String[] SHBEFilenames;
        SHBEFilenames = getSHBEFilenamesAll();
        ArrayList<String> newFilesToRead;
        newFilesToRead = new ArrayList<String>();
        File[] FormattedSHBEFiles;
        FormattedSHBEFiles = Files.getGeneratedSHBEDir().listFiles();
        HashSet<DW_YM3> FormattedYM3s;
        FormattedYM3s = new HashSet<DW_YM3>();
        for (File FormattedSHBEFile : FormattedSHBEFiles) {
            if (FormattedSHBEFile.isDirectory()) {
                FormattedYM3s.add(new DW_YM3(FormattedSHBEFile.getName()));
            }
        }
        DW_YM3 YM3;
        for (String SHBEFilename : SHBEFilenames) {
            YM3 = getYM3(SHBEFilename);
            if (!FormattedYM3s.contains(YM3)) {
                newFilesToRead.add(SHBEFilename);
            }
        }
        DW_YM3 LastYM3;
        LastYM3 = getYM3(SHBEFilenames[SHBEFilenames.length - 1]);
        DW_YM3 NearestYM3ForONSPDFormatLookupLastYM3;
        NearestYM3ForONSPDFormatLookupLastYM3 = Postcode_Handler.getNearestYM3ForONSPDLookup(LastYM3);
        if (newFilesToRead.size() > 0) {
            Iterator<String> ite;
            ite = newFilesToRead.iterator();
            while (ite.hasNext()) {
                String SHBEFilename = ite.next();
                DW_SHBE_Records DW_SHBE_Records;
                DW_SHBE_Records = new DW_SHBE_Records(
                        Env,
                        dir,
                        SHBEFilename,
                        NearestYM3ForONSPDFormatLookupLastYM3,
                        logDir);
                Generic_IO.writeObject(DW_SHBE_Records, DW_SHBE_Records.getFile());
            }
            writeLookups();
        }
        // Make a backup copy
        File SHBEbackup;
        SHBEbackup = new File(Files.getGeneratedLCCDir(), "SHBEBackup");
        if (SHBEbackup.isDirectory()) {
            SHBEbackup = Generic_IO.addToArchive(SHBEbackup, 100);
        } else {
            SHBEbackup = Generic_IO.initialiseArchive(SHBEbackup, 100);
        }
        Generic_IO.copy(Files.getGeneratedSHBEDir(), SHBEbackup);
    }

    /**
     * For checking postcodes.
     *
     * @param logDir
     */
    public void runPostcodeCheckLatest(File logDir) {
        boolean handleOutOfMemoryError;
        handleOutOfMemoryError = true;
        File dir;
        dir = Files.getInputSHBEDir();
        HashMap<String, DW_ID> PostcodeToPostcodeIDLookup;
        PostcodeToPostcodeIDLookup = SHBE_Data.getPostcodeToPostcodeIDLookup();
        HashMap<DW_YM3, HashMap<DW_ID, Geotools_Point>> PostcodeIDPointLookups;
        PostcodeIDPointLookups = SHBE_Data.getPostcodeIDToPointLookups();
        HashMap<DW_ID, String> ClaimIDToClaimRefLookup;
        ClaimIDToClaimRefLookup = SHBE_Data.getClaimIDToClaimRefLookup();

        // Prepare for output
        PrintWriter pw = null;
        String YMN;

        // Get latest SHBE.
        String[] SHBEFilenames;
        SHBEFilenames = getSHBEFilenamesAll();
        String SHBEFilename1;
        SHBEFilename1 = SHBEFilenames[SHBEFilenames.length - 1];
        YMN = getYearMonthNumber(SHBEFilename1);
        DW_YM3 YM31;
        YM31 = getYM3(SHBEFilename1);
        System.out.println("YM31 " + YM31);
        DW_YM3 NearestYM3ForONSPDLookupYM31;
        NearestYM3ForONSPDLookupYM31 = Postcode_Handler.getNearestYM3ForONSPDLookup(YM31);
        System.out.println("NearestYM3ForONSPDLookupYM31 " + NearestYM3ForONSPDLookupYM31);
        DW_SHBE_Records DW_SHBE_Records1;
        DW_SHBE_Records1 = new DW_SHBE_Records(
                Env,
                YM31);
        HashMap<DW_ID, DW_SHBE_Record> recs1;
        recs1 = DW_SHBE_Records1.getClaimIDToDW_SHBE_RecordMap(handleOutOfMemoryError);
        DW_SHBE_Record rec1;
        HashMap<DW_ID, Geotools_Point> PostcodeIDToPointLookup1;
        PostcodeIDToPointLookup1 = PostcodeIDPointLookups.get(NearestYM3ForONSPDLookupYM31);

        HashSet<String> UniqueUnmappablePostcodes;
        UniqueUnmappablePostcodes = new HashSet<String>();
        HashMap<DW_ID, String> ClaimantPostcodesUnmappable;
        ClaimantPostcodesUnmappable = DW_SHBE_Records1.getClaimantPostcodesUnmappable();
        DW_ID DW_ID;
        Iterator<DW_ID> ite;
        String ClaimRef;
        ite = ClaimantPostcodesUnmappable.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID = ite.next();
            ClaimRef = ClaimIDToClaimRefLookup.get(DW_ID);
            UniqueUnmappablePostcodes.add(ClaimRef + "," + ClaimantPostcodesUnmappable.get(DW_ID));
        }

        HashSet<String> UniqueModifiedPostcodes;
        UniqueModifiedPostcodes = new HashSet<String>();
        // <writeOutModifiedPostcodes>
        writeOutModifiedPostcodes(
                UniqueModifiedPostcodes,
                logDir, YMN, DW_SHBE_Records1,
                ClaimIDToClaimRefLookup,
                handleOutOfMemoryError);
        // </writeOutModifiedPostcodes>

        /**
         * Set up PrintWriter to write out some basic details of Claims with
         * Claimant Postcodes that are not yet mappable by any means.
         */
        File UnmappablePostcodesFile;
        UnmappablePostcodesFile = new File(
                logDir,
                "UnmappablePostcodes" + YMN + ".csv");
        PrintWriter pw2 = null;
        try {
            pw2 = new PrintWriter(UnmappablePostcodesFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_SHBE_Records.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw2.println("Ref,Year_Month,ClaimRef,Recorded Postcode,Correct Postcode,Input To Academy (Y/N)");
        int ref2 = 1;

        DW_YM3 YM30;
        DW_YM3 NearestYM3ForONSPDLookupYM30;
        HashMap<DW_ID, String> ClaimantPostcodesUnmappable0;
        DW_SHBE_Records DW_SHBE_Records0;
        HashMap<DW_ID, DW_SHBE_Record> recs0;
        DW_SHBE_Record rec0;
        String postcode0;
        String postcode1;
        String postcodef0;
        String unmappablePostcodef0;
        String postcodef1;

        HashMap<DW_ID, Geotools_Point> PostcodeIDToPointLookup0;
        HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup0 = null;
        HashSet<DW_ID> ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture0 = null;
        boolean modifiedAnyRecs = false;

        File FutureModifiedPostcodesFile;

        //for (int i = SHBEFilenames.length - 2; i >= 0; i--) {
        int i = SHBEFilenames.length - 2;
        // Get previous SHBE.
        YM30 = getYM3(SHBEFilenames[i]);
        System.out.println("YM30 " + YM30);
        YMN = getYearMonthNumber(SHBEFilenames[i]);
        // Set up to write FutureModifiedPostcodes
        FutureModifiedPostcodesFile = new File(
                logDir,
                "FutureModifiedPostcodes" + YMN + ".csv");
        try {
            pw = new PrintWriter(FutureModifiedPostcodesFile);
            pw.println("ClaimRef,Original Claimant Postcode,Updated from the Future Claimant Postcode");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_SHBE_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        NearestYM3ForONSPDLookupYM30 = Postcode_Handler.getNearestYM3ForONSPDLookup(YM30);
        System.out.println("NearestYM3ForONSPDLookupYM30 " + NearestYM3ForONSPDLookupYM30);
        DW_SHBE_Records0 = new DW_SHBE_Records(
                Env,
                YM30);
        recs0 = DW_SHBE_Records0.getClaimIDToDW_SHBE_RecordMap(handleOutOfMemoryError);
        // <writeOutModifiedPostcodes>
        writeOutModifiedPostcodes(
                UniqueModifiedPostcodes,
                logDir, YMN, DW_SHBE_Records0,
                ClaimIDToClaimRefLookup,
                handleOutOfMemoryError);
        // </writeOutModifiedPostcodes>
        PostcodeIDToPointLookup0 = PostcodeIDPointLookups.get(NearestYM3ForONSPDLookupYM30);
        // Get previously unmappable postcodes
        ClaimantPostcodesUnmappable0 = DW_SHBE_Records0.getClaimantPostcodesUnmappable(handleOutOfMemoryError);
        boolean modifiedRecs = false;
        ite = ClaimantPostcodesUnmappable0.keySet().iterator();
        HashSet<DW_ID> ClaimantPostcodesUnmappable0Remove = new HashSet<DW_ID>();
        while (ite.hasNext()) {
            DW_ID = ite.next();
            unmappablePostcodef0 = ClaimantPostcodesUnmappable0.get(DW_ID);
            ClaimRef = ClaimIDToClaimRefLookup.get(DW_ID);
            System.out.println(ClaimRef);
            rec1 = recs1.get(DW_ID);
            rec0 = recs0.get(DW_ID);
            postcodef0 = rec0.getClaimPostcodeF();
            postcode0 = rec0.getDRecord().getClaimantsPostcode();
            if (rec1 != null) {
                postcodef1 = rec1.getClaimPostcodeF();

                if (rec1.isClaimPostcodeFMappable()) {
                    System.out.println("Claimants Postcode 0 \"" + postcode0 + "\" unmappablePostcodef0 \"" + unmappablePostcodef0 + "\" postcodef0 \"" + postcodef0 + "\" changed to " + postcodef1 + " which is mappable.");
                    if (!rec0.ClaimPostcodeFValidPostcodeFormat) {
                        rec0.ClaimPostcodeFUpdatedFromTheFuture = true;
                        rec0.ClaimPostcodeF = postcodef1;
                        rec0.ClaimPostcodeFMappable = true;
                        rec0.ClaimPostcodeFValidPostcodeFormat = true;
                        if (ClaimIDToPostcodeIDLookup0 == null) {
                            ClaimIDToPostcodeIDLookup0 = DW_SHBE_Records0.getClaimIDToPostcodeIDLookup();
                        }
                        ClaimIDToPostcodeIDLookup0.put(DW_ID, PostcodeToPostcodeIDLookup.get(postcodef1));
                        if (ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture0 == null) {
                            ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture0 = DW_SHBE_Records0.getClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture();
                        }
                        ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture0.add(DW_ID);
                        Geotools_Point p;
                        p = PostcodeIDToPointLookup1.get(DW_ID);
                        PostcodeIDToPointLookup0.put(DW_ID, p);
                        modifiedRecs = true;
                        modifiedAnyRecs = true;
                        postcode1 = postcodef1.replaceAll(" ", "");
                        postcode1 = postcode1.substring(0, postcode1.length() - 3) + " " + postcode1.substring(postcode1.length() - 3);
                        pw.println(ClaimRef + "," + postcode0 + "," + postcode1);
                        ClaimantPostcodesUnmappable0Remove.add(DW_ID);
                    }
                } else {
                    System.out.println("postcodef1 " + postcodef1 + " is not mappable.");
//                        postcode1 = postcodef1.replaceAll(" ", "");
//                        if (postcode1.length() > 3) {
//                        postcode1 = postcode1.substring(0, postcode1.length() - 3) + " " + postcode1.substring(postcode1.length() - 3);
//                        } else {
//                            postcodef1 = rec1.getClaimPostcodeF();
//                        }
                    postcode1 = rec1.getDRecord().getClaimantsPostcode();
                    UniqueUnmappablePostcodes.add(ClaimRef + "," + postcode1 + ",,");
                    pw2.println("" + ref2 + "," + YM31 + "," + ClaimRef + "," + postcode1 + ",,");
                    ref2++;
//                        System.out.println("postcodef1 " + postcodef1 + " is not mappable.");
//                        UniqueUnmappablePostcodes.add(ClaimRef + "," + postcode0 + ",,");
//                        pw2.println("" + ref2 + "," + YM30 + "," + ClaimRef + "," + postcode0 + ",,");
//                        ref2++;
                }
            }
        }
        ite = ClaimantPostcodesUnmappable0Remove.iterator();
        while (ite.hasNext()) {
            DW_ID = ite.next();
            ClaimantPostcodesUnmappable0.remove(DW_ID);
        }
        if (modifiedRecs == true) {
            // Write out recs0
            Generic_IO.writeObject(ClaimantPostcodesUnmappable0, DW_SHBE_Records0.getClaimantPostcodesUnmappableFile());
            Generic_IO.writeObject(ClaimIDToPostcodeIDLookup0, DW_SHBE_Records0.getClaimIDToPostcodeIDLookupFile());
            Generic_IO.writeObject(recs0, DW_SHBE_Records0.getRecordsFile());
            Generic_IO.writeObject(ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture0, DW_SHBE_Records0.getClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile());
        }

        // Prepare for next iteration
        recs1 = recs0;
        ClaimIDToPostcodeIDLookup0 = null;
        ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture0 = null;
        pw.close();
        //}
        pw2.close();
        // <Write out UniqueUnmappablePostcodes>
        File UniqueUnmappablePostcodesFile = new File(
                logDir,
                "UniqueUnmappablePostcodes.csv");
        try {
            pw = new PrintWriter(UniqueUnmappablePostcodesFile);
            pw.println("ClaimRef,Original Claimant Postcode,Modified Claimant Postcode,Input To Academy (Y/N)");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_SHBE_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        Iterator<String> iteS;
        iteS = UniqueUnmappablePostcodes.iterator();
        while (iteS.hasNext()) {
            pw.println(iteS.next());
        }
        pw.close();
        // </Write out UniqueUnmappablePostcodes>
        // <Write out UniqueModifiedPostcodes>
        File UniqueModifiedPostcodesFile = new File(
                logDir,
                "UniqueModifiedPostcodes.csv");
        try {
            pw = new PrintWriter(UniqueModifiedPostcodesFile);
            pw.println("ClaimRef,Original Claimant Postcode,Modified Claimant Postcode,Input To Academy (Y/N)");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_SHBE_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        iteS = UniqueModifiedPostcodes.iterator();
        while (iteS.hasNext()) {
            pw.println(iteS.next());
        }
        pw.close();
        // </Write out UniqueModifiedPostcodes>
        if (modifiedAnyRecs == true) {
            // Write out PostcodeIDPointLookups
            Generic_IO.writeObject(PostcodeIDPointLookups, SHBE_Data.getPostcodeIDToPointLookupsFile());
        }
    }

    /**
     * For checking postcodes.
     *
     * @param logDir
     */
    public void runPostcodeCheck(File logDir) {
        boolean handleOutOfMemoryError;
        handleOutOfMemoryError = true;
        File dir;
        dir = Files.getInputSHBEDir();
        HashMap<String, DW_ID> PostcodeToPostcodeIDLookup;
        PostcodeToPostcodeIDLookup = SHBE_Data.getPostcodeToPostcodeIDLookup();
        HashMap<DW_YM3, HashMap<DW_ID, Geotools_Point>> PostcodeIDPointLookups;
        PostcodeIDPointLookups = SHBE_Data.getPostcodeIDToPointLookups();
        HashMap<DW_ID, String> ClaimIDToClaimRefLookup;
        ClaimIDToClaimRefLookup = SHBE_Data.getClaimIDToClaimRefLookup();

        // Prepare for output
        PrintWriter pw = null;
        String YMN;

        // Get latest SHBE.
        String[] SHBEFilenames;
        SHBEFilenames = getSHBEFilenamesAll();
        String SHBEFilename1;
        SHBEFilename1 = SHBEFilenames[SHBEFilenames.length - 1];
        YMN = getYearMonthNumber(SHBEFilename1);
        DW_YM3 YM31;
        YM31 = getYM3(SHBEFilename1);
        System.out.println("YM31 " + YM31);
        DW_YM3 NearestYM3ForONSPDLookupYM31;
        NearestYM3ForONSPDLookupYM31 = Postcode_Handler.getNearestYM3ForONSPDLookup(YM31);
        System.out.println("NearestYM3ForONSPDLookupYM31 " + NearestYM3ForONSPDLookupYM31);
        DW_SHBE_Records DW_SHBE_Records1;
        DW_SHBE_Records1 = new DW_SHBE_Records(
                Env,
                YM31);
        HashMap<DW_ID, DW_SHBE_Record> recs1;
        recs1 = DW_SHBE_Records1.getClaimIDToDW_SHBE_RecordMap(handleOutOfMemoryError);
        DW_SHBE_Record rec1;
        HashMap<DW_ID, Geotools_Point> PostcodeIDToPointLookup1;
        PostcodeIDToPointLookup1 = PostcodeIDPointLookups.get(NearestYM3ForONSPDLookupYM31);

        HashSet<String> UniqueUnmappablePostcodes;
        UniqueUnmappablePostcodes = new HashSet<String>();
        HashMap<DW_ID, String> ClaimantPostcodesUnmappable;
        ClaimantPostcodesUnmappable = DW_SHBE_Records1.getClaimantPostcodesUnmappable();
        DW_ID DW_ID;
        Iterator<DW_ID> ite;
        String ClaimRef;
        ite = ClaimantPostcodesUnmappable.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID = ite.next();
            ClaimRef = ClaimIDToClaimRefLookup.get(DW_ID);
            UniqueUnmappablePostcodes.add(ClaimRef + "," + ClaimantPostcodesUnmappable.get(DW_ID));
        }

        HashSet<String> UniqueModifiedPostcodes;
        UniqueModifiedPostcodes = new HashSet<String>();
        // <writeOutModifiedPostcodes>
        writeOutModifiedPostcodes(
                UniqueModifiedPostcodes,
                logDir, YMN, DW_SHBE_Records1,
                ClaimIDToClaimRefLookup,
                handleOutOfMemoryError);
        // </writeOutModifiedPostcodes>

        /**
         * Set up PrintWriter to write out some basic details of Claims with
         * Claimant Postcodes that are not yet mappable by any means.
         */
        File UnmappablePostcodesFile;
        UnmappablePostcodesFile = new File(
                logDir,
                "UnmappablePostcodes" + YMN + ".csv");
        PrintWriter pw2 = null;
        try {
            pw2 = new PrintWriter(UnmappablePostcodesFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_SHBE_Records.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw2.println("Ref,Year_Month,ClaimRef,Recorded Postcode,Correct Postcode,Input To Academy (Y/N)");
        int ref2 = 1;

        DW_YM3 YM30;
        DW_YM3 NearestYM3ForONSPDLookupYM30;
        HashMap<DW_ID, String> ClaimantPostcodesUnmappable0;
        DW_SHBE_Records DW_SHBE_Records0;
        HashMap<DW_ID, DW_SHBE_Record> recs0;
        DW_SHBE_Record rec0;
        String postcode0;
        String postcode1;
        String postcodef0;
        String unmappablePostcodef0;
        String postcodef1;

        HashMap<DW_ID, Geotools_Point> PostcodeIDToPointLookup0;
        HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup0 = null;
        HashSet<DW_ID> ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture0 = null;
        boolean modifiedAnyRecs = false;

        File FutureModifiedPostcodesFile;

        for (int i = SHBEFilenames.length - 2; i >= 0; i--) {
            // Get previous SHBE.
            YM30 = getYM3(SHBEFilenames[i]);
            System.out.println("YM30 " + YM30);
            YMN = getYearMonthNumber(SHBEFilenames[i]);
            // Set up to write FutureModifiedPostcodes
            FutureModifiedPostcodesFile = new File(
                    logDir,
                    "FutureModifiedPostcodes" + YMN + ".csv");
            try {
                pw = new PrintWriter(FutureModifiedPostcodesFile);
                pw.println("ClaimRef,Original Claimant Postcode,Updated from the Future Claimant Postcode");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DW_SHBE_Handler.class.getName()).log(Level.SEVERE, null, ex);
            }
            NearestYM3ForONSPDLookupYM30 = Postcode_Handler.getNearestYM3ForONSPDLookup(YM30);
            System.out.println("NearestYM3ForONSPDLookupYM30 " + NearestYM3ForONSPDLookupYM30);
            DW_SHBE_Records0 = new DW_SHBE_Records(
                    Env,
                    YM30);
            recs0 = DW_SHBE_Records0.getClaimIDToDW_SHBE_RecordMap(handleOutOfMemoryError);
            // <writeOutModifiedPostcodes>
            writeOutModifiedPostcodes(
                    UniqueModifiedPostcodes,
                    logDir, YMN, DW_SHBE_Records0,
                    ClaimIDToClaimRefLookup,
                    handleOutOfMemoryError);
            // </writeOutModifiedPostcodes>
            PostcodeIDToPointLookup0 = PostcodeIDPointLookups.get(NearestYM3ForONSPDLookupYM30);
            // Get previously unmappable postcodes
            ClaimantPostcodesUnmappable0 = DW_SHBE_Records0.getClaimantPostcodesUnmappable(handleOutOfMemoryError);
            boolean modifiedRecs = false;
            ite = ClaimantPostcodesUnmappable0.keySet().iterator();
            HashSet<DW_ID> ClaimantPostcodesUnmappable0Remove = new HashSet<DW_ID>();
            while (ite.hasNext()) {
                DW_ID = ite.next();
                unmappablePostcodef0 = ClaimantPostcodesUnmappable0.get(DW_ID);
                ClaimRef = ClaimIDToClaimRefLookup.get(DW_ID);
                System.out.println(ClaimRef);
                rec1 = recs1.get(DW_ID);
                rec0 = recs0.get(DW_ID);
                postcodef0 = rec0.getClaimPostcodeF();
                postcode0 = rec0.getDRecord().getClaimantsPostcode();
                if (rec1 != null) {
                    postcodef1 = rec1.getClaimPostcodeF();

                    if (rec1.isClaimPostcodeFMappable()) {
                        System.out.println("Claimants Postcode 0 \"" + postcode0 + "\" unmappablePostcodef0 \"" + unmappablePostcodef0 + "\" postcodef0 \"" + postcodef0 + "\" changed to " + postcodef1 + " which is mappable.");
                        if (!rec0.ClaimPostcodeFValidPostcodeFormat) {
                            rec0.ClaimPostcodeFUpdatedFromTheFuture = true;
                            rec0.ClaimPostcodeF = postcodef1;
                            rec0.ClaimPostcodeFMappable = true;
                            rec0.ClaimPostcodeFValidPostcodeFormat = true;
                            if (ClaimIDToPostcodeIDLookup0 == null) {
                                ClaimIDToPostcodeIDLookup0 = DW_SHBE_Records0.getClaimIDToPostcodeIDLookup();
                            }
                            ClaimIDToPostcodeIDLookup0.put(DW_ID, PostcodeToPostcodeIDLookup.get(postcodef1));
                            if (ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture0 == null) {
                                ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture0 = DW_SHBE_Records0.getClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture();
                            }
                            ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture0.add(DW_ID);
                            Geotools_Point p;
                            p = PostcodeIDToPointLookup1.get(DW_ID);
                            PostcodeIDToPointLookup0.put(DW_ID, p);
                            modifiedRecs = true;
                            modifiedAnyRecs = true;
                            postcode1 = postcodef1.replaceAll(" ", "");
                            postcode1 = postcode1.substring(0, postcode1.length() - 3) + " " + postcode1.substring(postcode1.length() - 3);
                            pw.println(ClaimRef + "," + postcode0 + "," + postcode1);
                            ClaimantPostcodesUnmappable0Remove.add(DW_ID);
                        }
                    } else {
                        System.out.println("postcodef1 " + postcodef1 + " is not mappable.");
//                        postcode1 = postcodef1.replaceAll(" ", "");
//                        if (postcode1.length() > 3) {
//                        postcode1 = postcode1.substring(0, postcode1.length() - 3) + " " + postcode1.substring(postcode1.length() - 3);
//                        } else {
//                            postcodef1 = rec1.getClaimPostcodeF();
//                        }
                        postcode1 = rec1.getDRecord().getClaimantsPostcode();
                        UniqueUnmappablePostcodes.add(ClaimRef + "," + postcode1 + ",,");
                        pw2.println("" + ref2 + "," + YM31 + "," + ClaimRef + "," + postcode1 + ",,");
                        ref2++;
//                        System.out.println("postcodef1 " + postcodef1 + " is not mappable.");
//                        UniqueUnmappablePostcodes.add(ClaimRef + "," + postcode0 + ",,");
//                        pw2.println("" + ref2 + "," + YM30 + "," + ClaimRef + "," + postcode0 + ",,");
//                        ref2++;
                    }
                }
            }
            ite = ClaimantPostcodesUnmappable0Remove.iterator();
            while (ite.hasNext()) {
                DW_ID = ite.next();
                ClaimantPostcodesUnmappable0.remove(DW_ID);
            }
            if (modifiedRecs == true) {
                // Write out recs0
                Generic_IO.writeObject(ClaimantPostcodesUnmappable0, DW_SHBE_Records0.getClaimantPostcodesUnmappableFile());
                Generic_IO.writeObject(ClaimIDToPostcodeIDLookup0, DW_SHBE_Records0.getClaimIDToPostcodeIDLookupFile());
                Generic_IO.writeObject(recs0, DW_SHBE_Records0.getRecordsFile());
                Generic_IO.writeObject(ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture0, DW_SHBE_Records0.getClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile());
            }

            // Prepare for next iteration
            recs1 = recs0;
            ClaimIDToPostcodeIDLookup0 = null;
            ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture0 = null;
            pw.close();
        }
        pw2.close();
        // <Write out UniqueUnmappablePostcodes>
        File UniqueUnmappablePostcodesFile = new File(
                logDir,
                "UniqueUnmappablePostcodes.csv");
        try {
            pw = new PrintWriter(UniqueUnmappablePostcodesFile);
            pw.println("ClaimRef,Original Claimant Postcode,Modified Claimant Postcode,Input To Academy (Y/N)");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_SHBE_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        Iterator<String> iteS;
        iteS = UniqueUnmappablePostcodes.iterator();
        while (iteS.hasNext()) {
            pw.println(iteS.next());
        }
        pw.close();
        // </Write out UniqueUnmappablePostcodes>
        // <Write out UniqueModifiedPostcodes>
        File UniqueModifiedPostcodesFile = new File(
                logDir,
                "UniqueModifiedPostcodes.csv");
        try {
            pw = new PrintWriter(UniqueModifiedPostcodesFile);
            pw.println("ClaimRef,Original Claimant Postcode,Modified Claimant Postcode,Input To Academy (Y/N)");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_SHBE_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        iteS = UniqueModifiedPostcodes.iterator();
        while (iteS.hasNext()) {
            pw.println(iteS.next());
        }
        pw.close();
        // </Write out UniqueModifiedPostcodes>
        if (modifiedAnyRecs == true) {
            // Write out PostcodeIDPointLookups
            Generic_IO.writeObject(PostcodeIDPointLookups, SHBE_Data.getPostcodeIDToPointLookupsFile());
        }
    }

    /**
     * Set up a PrintWriter to write out some details of claims with claimant
     * postcodes that are automatically modified in order that they are
     * mappable. The formatting may involve removing any Non A-Z, a-z or 0-9
     * characters. It may also involve changing a "O" to a "0" in the second
     * part of the postcode where a number is expected. And it may also involve
     * removing an additional "0" in the first part of the postcode for example
     * where "LS06" should be "LS6".
     *
     * @param UniqueModifiedPostcodes
     * @param dir
     * @param YMN
     * @param DW_SHBE_Records
     * @param ClaimIDToClaimRefLookup
     * @param handleOutOfMemoryError
     */
    protected void writeOutModifiedPostcodes(
            HashSet<String> UniqueModifiedPostcodes,
            File dir, String YMN, DW_SHBE_Records DW_SHBE_Records,
            HashMap<DW_ID, String> ClaimIDToClaimRefLookup,
            boolean handleOutOfMemoryError) {
        File ModifiedPostcodesFile;
        int ref;
        HashMap<DW_ID, String[]> ClaimantPostcodesModified;
        Iterator<DW_ID> ite;
        DW_ID DW_ID;
        String[] postcodes;
        String ClaimRef;
        ModifiedPostcodesFile = new File(
                dir,
                "ModifiedPostcodes" + YMN + ".csv");
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(ModifiedPostcodesFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_SHBE_Records.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw.println("Ref,ClaimRef,Recorded Postcode,Modified Postcode,Input To Academy (Y/N)");
        ref = 1;
        ClaimantPostcodesModified = DW_SHBE_Records.getClaimantPostcodesModified(handleOutOfMemoryError);
        ite = ClaimantPostcodesModified.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID = ite.next();
            postcodes = ClaimantPostcodesModified.get(DW_ID);
            ClaimRef = ClaimIDToClaimRefLookup.get(DW_ID);
            pw.println("" + ref + "," + ClaimRef + "," + postcodes[0] + "," + postcodes[1] + ",");
            UniqueModifiedPostcodes.add(ClaimRef + "," + postcodes[0] + "," + postcodes[1] + ",");
            ref++;
        }
        pw.close();
    }

    /**
     *
     * @param DW_SHBE_Records
     * @param PT
     * @return
     */
    public HashSet<DW_ID> getClaimIDsWithStatusOfHBAtExtractDate(
            DW_SHBE_Records DW_SHBE_Records,
            String PT) {
        HashSet<DW_ID> result;
        result = null;
        if (PT.equalsIgnoreCase(Strings.sPaymentTypeAll)) {
            result = DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateInPayment();
            result.addAll(DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateSuspended());
            result.addAll(DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateOther());
        } else if (PT.equalsIgnoreCase(Strings.sPaymentTypeIn)) {
            result = DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateInPayment();
        } else if (PT.equalsIgnoreCase(Strings.sPaymentTypeSuspended)) {
            result = DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateSuspended();
        } else if (PT.equalsIgnoreCase(Strings.sPaymentTypeOther)) {
            result = DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateOther();
        }
        return result;
    }

    /**
     *
     * @param DW_SHBE_Records
     * @param PT
     * @return
     */
    public HashSet<DW_ID> getClaimIDsWithStatusOfCTBAtExtractDate(
            DW_SHBE_Records DW_SHBE_Records,
            String PT) {
         HashSet<DW_ID> result;
        result = null;
        if (PT.equalsIgnoreCase(Strings.sPaymentTypeAll)) {
            result = DW_SHBE_Records.getClaimIDsWithStatusOfCTBAtExtractDateInPayment();
            result.addAll(DW_SHBE_Records.getClaimIDsWithStatusOfCTBAtExtractDateSuspended());
            result.addAll(DW_SHBE_Records.getClaimIDsWithStatusOfCTBAtExtractDateOther());
        } else if (PT.equalsIgnoreCase(Strings.sPaymentTypeIn)) {
            result = DW_SHBE_Records.getClaimIDsWithStatusOfCTBAtExtractDateInPayment();
        } else if (PT.equalsIgnoreCase(Strings.sPaymentTypeSuspended)) {
            result = DW_SHBE_Records.getClaimIDsWithStatusOfCTBAtExtractDateSuspended();
        } else if (PT.equalsIgnoreCase(Strings.sPaymentTypeOther)) {
            result = DW_SHBE_Records.getClaimIDsWithStatusOfCTBAtExtractDateOther();
        }
        return result;
    }

    public String getClaimantType(DW_SHBE_D_Record D_Record) {
        if (isHBClaim(D_Record)) {
            return Strings.sHB;
        }
        //if (isCTBOnlyClaim(D_Record)) {
        return Strings.sCTB;
        //}
    }

    public ArrayList<String> getClaimantTypes() {
        ArrayList<String> result;
        result = new ArrayList<String>();
        result.add(Strings.sHB);
        result.add(Strings.sCTB);
        return result;
    }

    public boolean isCTBOnlyClaimOtherPT(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfCTBClaimAtExtractDate() == 0) {
            return isCTBOnlyClaim(D_Record);
        }
        return false;
    }

    public boolean isCTBOnlyClaimSuspended(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfCTBClaimAtExtractDate() == 2) {
            return isCTBOnlyClaim(D_Record);
        }
        return false;
    }

    public boolean isCTBOnlyClaimInPayment(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfCTBClaimAtExtractDate() == 1) {
            return isCTBOnlyClaim(D_Record);
        }
        return false;
    }

    public boolean isCTBOnlyClaim(DW_SHBE_D_Record D_Record) {
        if (D_Record == null) {
            return false;
        }
        int TT;
        TT = D_Record.getTenancyType();
        return isCTBOnlyClaim(
                TT);
    }

    /**
     * @param TT
     * @return
     */
    public boolean isCTBOnlyClaim(
            int TT) {
        return TT == 5 || TT == 7;
    }

    public boolean isHBClaimOtherPT(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfHBClaimAtExtractDate() == 0) {
            return isHBClaim(D_Record);
        }
        return false;
    }

    public boolean isHBClaimSuspended(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfHBClaimAtExtractDate() == 2) {
            return isHBClaim(D_Record);
        }
        return false;
    }

    public boolean isHBClaimInPayment(DW_SHBE_D_Record D_Record) {
        if (D_Record.getStatusOfHBClaimAtExtractDate() == 1) {
            return isHBClaim(D_Record);
        }
        return false;
    }

    public boolean isHBClaim(DW_SHBE_D_Record D_Record) {
        if (D_Record == null) {
            return false;
        }
        int TT;
        TT = D_Record.getTenancyType();
        return isHBClaim(TT);
    }

    public boolean isHBClaim(int TT) {
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

    HashMap<Integer, DW_YM3> indexYM3s;

    /**
     *
     * @return
     */
    public HashMap<Integer, DW_YM3> getIndexYM3s() {
        if (indexYM3s == null) {
            indexYM3s = new HashMap<Integer, DW_YM3>();
            String[] filenames = getSHBEFilenamesAll();
            int i = 0;
            DW_YM3 yM3;
            for (String filename : filenames) {
                yM3 = getYM3(filename);
                indexYM3s.put(i, yM3);
                i++;
            }
        }
        return indexYM3s;
    }

    /**
     *
     * @param S
     * @param StringToDW_IDLookup
     * @param DW_IDToStringLookup
     * @param list List to add result to if a new one is created.
     * @return
     */
    public DW_ID getIDAddIfNeeded(
            String S,
            HashMap<String, DW_ID> StringToDW_IDLookup,
            HashMap<DW_ID, String> DW_IDToStringLookup,
            ArrayList<DW_ID> list
    ) {
        DW_ID result;
        if (StringToDW_IDLookup.containsKey(S)) {
            result = StringToDW_IDLookup.get(S);
        } else {
            result = new DW_ID(DW_IDToStringLookup.size());
            DW_IDToStringLookup.put(result, S);
            StringToDW_IDLookup.put(S, result);
            list.add(result);
        }
        return result;
    }

    /**
     *
     * @param S
     * @param StringToIDLookup
     * @param IDToStringLookup
     * @return
     */
    public DW_ID getIDAddIfNeeded(
            String S,
            HashMap<String, DW_ID> StringToIDLookup,
            HashMap<DW_ID, String> IDToStringLookup
    ) {
        DW_ID result;
        if (StringToIDLookup.containsKey(S)) {
            result = StringToIDLookup.get(S);
        } else {
            result = new DW_ID(IDToStringLookup.size());
            IDToStringLookup.put(result, S);
            StringToIDLookup.put(S, result);
        }
        return result;
    }

    /**
     * Only called when loading SHBE from source.
     *
     * @param ClaimRef
     * @param ClaimRefToClaimIDLookup
     * @param ClaimIDToClaimRefLookup
     * @param ClaimIDs
     * @param ClaimIDsOfNewSHBEClaims
     * @return
     * @throws java.lang.Exception
     */
    public DW_ID getIDAddIfNeeded(
            String ClaimRef,
            HashMap<String, DW_ID> ClaimRefToClaimIDLookup,
            HashMap<DW_ID, String> ClaimIDToClaimRefLookup,
            HashSet<DW_ID> ClaimIDs,
            HashSet<DW_ID> ClaimIDsOfNewSHBEClaims
    ) throws Exception {
        DW_ID result;
        if (ClaimRefToClaimIDLookup.containsKey(ClaimRef)) {
            result = ClaimRefToClaimIDLookup.get(ClaimRef);
        } else {
            result = new DW_ID(ClaimIDToClaimRefLookup.size());
            ClaimIDToClaimRefLookup.put(result, ClaimRef);
            ClaimRefToClaimIDLookup.put(ClaimRef, result);
            if (ClaimIDs.contains(result)) {
                throw new Exception("DRecord already read for ClaimRef " + ClaimRef);
            }
            ClaimIDsOfNewSHBEClaims.add(result);
            ClaimIDs.add(result);
        }
        return result;
    }

    /**
     * Only called when loading SHBE from source.
     *
     * @param PostcodeF
     * @param PostcodeToPostcodeIDLookup
     * @param PostcodeIDToPostcodeLookup
     * @return
     */
    public DW_ID getPostcodeIDAddIfNeeded(
            String PostcodeF,
            HashMap<String, DW_ID> PostcodeToPostcodeIDLookup,
            HashMap<DW_ID, String> PostcodeIDToPostcodeLookup) {
        DW_ID result;
        result = null;
        if (PostcodeToPostcodeIDLookup.containsKey(PostcodeF)) {
            result = PostcodeToPostcodeIDLookup.get(PostcodeF);
        } else {
            result = new DW_ID(PostcodeIDToPostcodeLookup.size());
//            if (IDToSLookup.size() > Integer.MAX_VALUE) {
//                throw new Error("LookupFiles are full!");
//            }
            PostcodeIDToPostcodeLookup.put(result, PostcodeF);
            PostcodeToPostcodeIDLookup.put(PostcodeF, result);

        }
        return result;
    }

    /**
     *
     * @param DW_SHBE_Records
     * @param HB_CTB
     * @param PTs
     * @param YM30
     * @param UOReportSetCouncil
     * @param UOReportSetRSL
     * @param doUnderOccupancy
     * @param doCouncil
     * @param doRSL
     * @param forceNew
     * @return HashMap<String, BigDecimal> result where the keys are Strings for
     * Income and Rent Summary Statistics. An example key is:
     * DW_Strings.sTotal_Income + nameSuffix , where; nameSuffix = AllOrHBOrCTB
     * + PT. (AllOrHBOrCTB is given by HB_CTB, PT is given by PTs)
     */
    public HashMap<String, BigDecimal> getIncomeAndRentSummary(
            DW_SHBE_Records DW_SHBE_Records,
            ArrayList<String> HB_CTB,
            ArrayList<String> PTs,
            DW_YM3 YM30,
            DW_UO_Set UOReportSetCouncil,
            DW_UO_Set UOReportSetRSL,
            boolean doUnderOccupancy,
            boolean doCouncil,
            boolean doRSL,
            boolean forceNew) {
        HashMap<String, BigDecimal> result;
        result = new HashMap<String, BigDecimal>();
        File IncomeAndRentSummaryFile = getIncomeAndRentSummaryFile(
                YM30,
                doUnderOccupancy,
                doCouncil,
                doRSL);
        if (IncomeAndRentSummaryFile.exists()) {
            if (!forceNew) {
                return (HashMap<String, BigDecimal>) Generic_IO.readObject(
                        IncomeAndRentSummaryFile);
            }
        }
        int nTT = getNumberOfTenancyTypes();
        String HBOrCTB;
        String PT;
        String nameSuffix;
        Iterator<String> HB_CTBIte;
        HB_CTBIte = HB_CTB.iterator();
        Iterator<String> PTsIte;

        HashMap<DW_ID, DW_SHBE_Record> recs;
        recs = DW_SHBE_Records.getRecords();

        BigDecimal tBD;
        BigDecimal zBD;

        BigDecimal TotalIncome = null;
        long TotalCount_IncomeNonZero = 0;
        long TotalCount_IncomeZero = 0;
        BigDecimal TotalWeeklyEligibleRentAmount = null;
        long TotalCount_WeeklyEligibleRentAmountNonZero = 0;
        long TotalCount_WeeklyEligibleRentAmountZero = 0;
        BigDecimal[] TotalIncomeTT = null;
        long[] TotalCount_IncomeTTNonZero = null;
        long[] TotalCount_IncomeTTZero = null;
        BigDecimal[] TotalTTWeeklyEligibleRentAmount = null;
        int[] TotalCount_TTWeeklyEligibleRentAmountNonZero = null;
        int[] TotalCount_TTWeeklyEligibleRentAmountZero = null;
        Iterator<DW_ID> ite;
        HashMap<DW_ID, DW_UO_Record> map;
        DW_ID ClaimID;
        int TT;
        BigDecimal income;

        while (HB_CTBIte.hasNext()) {
            HBOrCTB = HB_CTBIte.next();
            HashSet<DW_ID> ClaimIDs;

            // Initialise
            TotalCount_IncomeNonZero = 0;
            TotalIncome = BigDecimal.ZERO;
            TotalCount_IncomeZero = 0;
            TotalWeeklyEligibleRentAmount = BigDecimal.ZERO;
            TotalCount_WeeklyEligibleRentAmountNonZero = 0;
            TotalCount_WeeklyEligibleRentAmountZero = 0;
            TotalIncomeTT = new BigDecimal[nTT];
            TotalCount_IncomeTTNonZero = new long[nTT];
            TotalCount_IncomeTTZero = new long[nTT];
            TotalTTWeeklyEligibleRentAmount = new BigDecimal[nTT];
            TotalCount_TTWeeklyEligibleRentAmountNonZero = new int[nTT];
            TotalCount_TTWeeklyEligibleRentAmountZero = new int[nTT];
            for (int i = 0; i < nTT; i++) {
                TotalIncomeTT[i] = BigDecimal.ZERO;
                TotalCount_IncomeTTNonZero[i] = 0;
                TotalCount_IncomeTTZero[i] = 0;
                TotalTTWeeklyEligibleRentAmount[i] = BigDecimal.ZERO;
                TotalCount_TTWeeklyEligibleRentAmountNonZero[i] = 0;
                TotalCount_TTWeeklyEligibleRentAmountZero[i] = 0;
            }
            PTsIte = PTs.iterator();
            while (PTsIte.hasNext()) {
                PT = PTsIte.next();
                if (HBOrCTB.equalsIgnoreCase(Strings.sHB)) {
                    ClaimIDs = getClaimIDsWithStatusOfHBAtExtractDate(DW_SHBE_Records, PT);
                } else {
                    ClaimIDs = getClaimIDsWithStatusOfCTBAtExtractDate(DW_SHBE_Records, PT);
                }
//                Debugging code
//                if (ClaimIDs == null) {
//                    System.out.println(DW_Strings.sHB);
//                    System.out.println(PT);
//                }
//                
                if (doUnderOccupancy) {
                    if (UOReportSetCouncil != null) {
                        map = UOReportSetCouncil.getMap();
                        ite = map.keySet().iterator();
                        while (ite.hasNext()) {
                            ClaimID = ite.next();
                            if (ClaimIDs.contains(ClaimID)) {
                                DW_SHBE_Record rec;
                                rec = recs.get(ClaimID);
                                if (rec != null) {
                                    DW_SHBE_D_Record aDRecord;
                                    aDRecord = rec.getDRecord();
                                    TT = aDRecord.getTenancyType();
                                    income = BigDecimal.valueOf(getClaimantsAndPartnersIncomeTotal(aDRecord));
                                    if (HBOrCTB.equalsIgnoreCase(Strings.sHB)) {
                                        // HB
                                        if (isHBClaim(TT)) {
                                            TotalIncome = TotalIncome.add(income);
                                            TotalIncomeTT[TT] = TotalIncomeTT[TT].add(income);
                                            if (income.compareTo(BigDecimal.ZERO) == 1) {
                                                TotalCount_IncomeNonZero++;
                                                TotalCount_IncomeTTNonZero[TT]++;
                                            } else {
                                                TotalCount_IncomeZero++;
                                                TotalCount_IncomeTTZero[TT]++;
                                            }
                                            tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                                            TotalWeeklyEligibleRentAmount = TotalWeeklyEligibleRentAmount.add(tBD);
                                            TotalTTWeeklyEligibleRentAmount[TT] = TotalTTWeeklyEligibleRentAmount[TT].add(tBD);
                                            if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                                                TotalCount_WeeklyEligibleRentAmountNonZero++;
                                                TotalCount_TTWeeklyEligibleRentAmountNonZero[TT]++;
                                            } else {
                                                TotalCount_WeeklyEligibleRentAmountZero++;
                                                TotalCount_TTWeeklyEligibleRentAmountZero[TT]++;
                                            }
                                        }
                                    } else // CTB
                                    if (isCTBOnlyClaim(TT)) {
                                        TotalIncome = TotalIncome.add(income);
                                        TotalIncomeTT[TT] = TotalIncomeTT[TT].add(income);
                                        if (income.compareTo(BigDecimal.ZERO) == 1) {
                                            TotalCount_IncomeNonZero++;
                                            TotalCount_IncomeTTNonZero[TT]++;
                                        } else {
                                            TotalCount_IncomeZero++;
                                            TotalCount_IncomeTTZero[TT]++;
                                        }
                                        tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                                        TotalWeeklyEligibleRentAmount = TotalWeeklyEligibleRentAmount.add(tBD);
                                        TotalTTWeeklyEligibleRentAmount[TT] = TotalTTWeeklyEligibleRentAmount[TT].add(tBD);
                                        if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                                            TotalCount_WeeklyEligibleRentAmountNonZero++;
                                            TotalCount_TTWeeklyEligibleRentAmountNonZero[TT]++;
                                        } else {
                                            TotalCount_WeeklyEligibleRentAmountZero++;
                                            TotalCount_TTWeeklyEligibleRentAmountZero[TT]++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (UOReportSetRSL != null) {
                        map = UOReportSetRSL.getMap();
                        ite = map.keySet().iterator();
                        while (ite.hasNext()) {
                            ClaimID = ite.next();
                            if (ClaimIDs.contains(ClaimID)) {
                                DW_SHBE_Record rec;
                                rec = recs.get(ClaimID);
                                if (rec != null) {
                                    DW_SHBE_D_Record aDRecord;
                                    aDRecord = rec.getDRecord();
                                    TT = aDRecord.getTenancyType();
                                    income = BigDecimal.valueOf(getClaimantsAndPartnersIncomeTotal(aDRecord));
                                    if (HBOrCTB.equalsIgnoreCase(Strings.sHB)) {
                                        // HB
                                        if (isHBClaim(TT)) {
                                            TotalIncome = TotalIncome.add(income);
                                            TotalIncomeTT[TT] = TotalIncomeTT[TT].add(income);
                                            if (income.compareTo(BigDecimal.ZERO) == 1) {
                                                TotalCount_IncomeNonZero++;
                                                TotalCount_IncomeTTNonZero[TT]++;
                                            } else {
                                                TotalCount_IncomeZero++;
                                                TotalCount_IncomeTTZero[TT]++;
                                            }
                                            tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                                            TotalWeeklyEligibleRentAmount = TotalWeeklyEligibleRentAmount.add(tBD);
                                            TotalTTWeeklyEligibleRentAmount[TT] = TotalTTWeeklyEligibleRentAmount[TT].add(tBD);
                                            if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                                                TotalCount_WeeklyEligibleRentAmountNonZero++;
                                                TotalCount_TTWeeklyEligibleRentAmountNonZero[TT]++;
                                            } else {
                                                TotalCount_WeeklyEligibleRentAmountZero++;
                                                TotalCount_TTWeeklyEligibleRentAmountZero[TT]++;
                                            }
                                        }
                                    } else // CTB
                                    {
                                        if (isCTBOnlyClaim(TT)) {
                                            TotalIncome = TotalIncome.add(income);
                                            TotalIncomeTT[TT] = TotalIncomeTT[TT].add(income);
                                            if (income.compareTo(BigDecimal.ZERO) == 1) {
                                                TotalCount_IncomeNonZero++;
                                                TotalCount_IncomeTTNonZero[TT]++;
                                            } else {
                                                TotalCount_IncomeZero++;
                                                TotalCount_IncomeTTZero[TT]++;
                                            }
                                            tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                                            TotalWeeklyEligibleRentAmount = TotalWeeklyEligibleRentAmount.add(tBD);
                                            TotalTTWeeklyEligibleRentAmount[TT] = TotalTTWeeklyEligibleRentAmount[TT].add(tBD);
                                            if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                                                TotalCount_WeeklyEligibleRentAmountNonZero++;
                                                TotalCount_TTWeeklyEligibleRentAmountNonZero[TT]++;
                                            } else {
                                                TotalCount_WeeklyEligibleRentAmountZero++;
                                                TotalCount_TTWeeklyEligibleRentAmountZero[TT]++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Iterator<DW_ID> ClaimIDsIte;
                    ClaimIDsIte = ClaimIDs.iterator();
                    while (ClaimIDsIte.hasNext()) {
                        ClaimID = ClaimIDsIte.next();
                        DW_SHBE_Record rec;
                        rec = recs.get(ClaimID);
                        DW_SHBE_D_Record aDRecord;
                        aDRecord = rec.getDRecord();
                        TT = aDRecord.getTenancyType();
                        income = BigDecimal.valueOf(getClaimantsAndPartnersIncomeTotal(aDRecord));
                        if (HBOrCTB.equalsIgnoreCase(Strings.sHB)) {
                            // HB
                            if (isHBClaim(TT)) {
                                TotalIncome = TotalIncome.add(income);
                                TotalIncomeTT[TT] = TotalIncomeTT[TT].add(income);
                                if (income.compareTo(BigDecimal.ZERO) == 1) {
                                    TotalCount_IncomeNonZero++;
                                    TotalCount_IncomeTTNonZero[TT]++;
                                } else {
                                    TotalCount_IncomeZero++;
                                    TotalCount_IncomeTTZero[TT]++;
                                }
                                tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                                TotalWeeklyEligibleRentAmount = TotalWeeklyEligibleRentAmount.add(tBD);
                                TotalTTWeeklyEligibleRentAmount[TT] = TotalTTWeeklyEligibleRentAmount[TT].add(tBD);
                                if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                                    TotalCount_WeeklyEligibleRentAmountNonZero++;
                                    TotalCount_TTWeeklyEligibleRentAmountNonZero[TT]++;
                                } else {
                                    TotalCount_WeeklyEligibleRentAmountZero++;
                                    TotalCount_TTWeeklyEligibleRentAmountZero[TT]++;
                                }
                            }
                        } else // CTB
                        {
                            if (isCTBOnlyClaim(TT)) {
                                TotalIncome = TotalIncome.add(income);
                                TotalIncomeTT[TT] = TotalIncomeTT[TT].add(income);
                                if (income.compareTo(BigDecimal.ZERO) == 1) {
                                    TotalCount_IncomeNonZero++;
                                    TotalCount_IncomeTTNonZero[TT]++;
                                } else {
                                    TotalCount_IncomeZero++;
                                    TotalCount_IncomeTTZero[TT]++;
                                }
                                tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                                TotalWeeklyEligibleRentAmount = TotalWeeklyEligibleRentAmount.add(tBD);
                                TotalTTWeeklyEligibleRentAmount[TT] = TotalTTWeeklyEligibleRentAmount[TT].add(tBD);
                                if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                                    TotalCount_WeeklyEligibleRentAmountNonZero++;
                                    TotalCount_TTWeeklyEligibleRentAmountNonZero[TT]++;
                                } else {
                                    TotalCount_WeeklyEligibleRentAmountZero++;
                                    TotalCount_TTWeeklyEligibleRentAmountZero[TT]++;
                                }
                            }
                        }
                    }
                }
                nameSuffix = HBOrCTB + PT;
                tBD = BigDecimal.valueOf(TotalCount_IncomeNonZero);
                zBD = BigDecimal.valueOf(TotalCount_IncomeZero);
                result.put(Strings.sTotal_Income + nameSuffix, TotalIncome);
                result.put(Strings.sTotalCount_IncomeNonZero + nameSuffix, tBD);
                result.put(Strings.sTotalCount_IncomeZero + nameSuffix, zBD);
                if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                    result.put(Strings.sAverage_NonZero_Income + nameSuffix,
                            Generic_BigDecimal.divideRoundIfNecessary(
                                    TotalIncome, tBD,
                                    2, RoundingMode.HALF_UP));
                } else {
                    result.put(Strings.sAverage_NonZero_Income + nameSuffix,
                            BigDecimal.ZERO);
                }
                tBD = BigDecimal.valueOf(
                        TotalCount_WeeklyEligibleRentAmountNonZero);
                zBD = BigDecimal.valueOf(
                        TotalCount_WeeklyEligibleRentAmountZero);
                result.put(Strings.sTotal_WeeklyEligibleRentAmount + nameSuffix,
                        TotalWeeklyEligibleRentAmount);
                result.put(Strings.sTotalCount_WeeklyEligibleRentAmountNonZero + nameSuffix,
                        tBD);
                result.put(Strings.sTotalCount_WeeklyEligibleRentAmountZero + nameSuffix,
                        zBD);
                if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                    result.put(Strings.sAverage_NonZero_WeeklyEligibleRentAmount + nameSuffix,
                            Generic_BigDecimal.divideRoundIfNecessary(
                                    TotalWeeklyEligibleRentAmount,
                                    tBD,
                                    2, RoundingMode.HALF_UP));
                }
                for (int i = 0; i < nTT; i++) {
                    // Income
                    result.put(Strings.sTotal_IncomeTT[i] + nameSuffix,
                            TotalIncomeTT[i]);
                    tBD = BigDecimal.valueOf(
                            TotalCount_IncomeTTNonZero[i]);
                    zBD = BigDecimal.valueOf(
                            TotalCount_IncomeTTZero[i]);
                    result.put(Strings.sTotalCount_IncomeNonZeroTT[i] + nameSuffix, tBD);
                    result.put(Strings.sTotalCount_IncomeZeroTT[i] + nameSuffix, zBD);
                    if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                        result.put(Strings.sAverage_NonZero_IncomeTT[i] + nameSuffix,
                                Generic_BigDecimal.divideRoundIfNecessary(
                                        TotalIncomeTT[i],
                                        tBD,
                                        2, RoundingMode.HALF_UP));
                    } else {
                        result.put(Strings.sAverage_NonZero_IncomeTT[i] + nameSuffix,
                                BigDecimal.ZERO);
                    }
                    // Rent
                    result.put(Strings.sTotal_WeeklyEligibleRentAmountTT[i] + nameSuffix,
                            TotalTTWeeklyEligibleRentAmount[i]);
                    tBD = BigDecimal.valueOf(
                            TotalCount_TTWeeklyEligibleRentAmountNonZero[i]);
                    zBD = BigDecimal.valueOf(
                            TotalCount_TTWeeklyEligibleRentAmountZero[i]);
                    result.put(Strings.sTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] + nameSuffix,
                            tBD);
                    result.put(Strings.sTotalCount_WeeklyEligibleRentAmountZeroTT[i] + nameSuffix,
                            zBD);
                    if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                        result.put(Strings.sAverage_NonZero_WeeklyEligibleRentAmountTT[i] + nameSuffix,
                                Generic_BigDecimal.divideRoundIfNecessary(
                                        TotalTTWeeklyEligibleRentAmount[i],
                                        tBD,
                                        2, RoundingMode.HALF_UP));
                    } else {
                        result.put(Strings.sAverage_NonZero_WeeklyEligibleRentAmountTT[i] + nameSuffix,
                                BigDecimal.ZERO);
                    }
                }
            }
        }
        Generic_IO.writeObject(result, IncomeAndRentSummaryFile);
        return result;
    }

    public DW_SHBE_RecordAggregate aggregate(HashSet<DW_SHBE_Record> records) {
        DW_SHBE_RecordAggregate result = new DW_SHBE_RecordAggregate();
        Iterator<DW_SHBE_Record> ite = records.iterator();
        while (ite.hasNext()) {
            DW_SHBE_Record rec;
            rec = ite.next();
            aggregate(rec, result);
        }
        return result;
    }

    public void aggregate(
            DW_SHBE_Record record,
            DW_SHBE_RecordAggregate a_Aggregate_SHBE_DataRecord) {
        DW_SHBE_D_Record aDRecord;
        aDRecord = record.DRecord;
        a_Aggregate_SHBE_DataRecord.setTotalClaimCount(a_Aggregate_SHBE_DataRecord.getTotalClaimCount() + 1);
        //if (aDRecord.getHousingBenefitClaimReferenceNumber().length() > 2) {
        if (isHBClaim(aDRecord)) {
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
//        ArrayList<DW_SHBE_S_Record> tSRecords;
//        tSRecords = record.getSRecordsWithoutDRecords();
//        Iterator<DW_SHBE_S_Record> ite;
//        ite = tSRecords.iterator();
//        while (ite.hasNext()) {
//            DW_SHBE_S_Record aSRecord = ite.next();
//            if (aSRecord.getNonDependentStatus() == 0) {
//                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus0(
//                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus0() + 1);
//            }
//            if (aSRecord.getNonDependentStatus() == 1) {
//                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus1(
//                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus1() + 1);
//            }
//            if (aSRecord.getNonDependentStatus() == 2) {
//                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus2(
//                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus2() + 1);
//            }
//            if (aSRecord.getNonDependentStatus() == 3) {
//                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus3(
//                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus3() + 1);
//            }
//            if (aSRecord.getNonDependentStatus() == 4) {
//                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus4(
//                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus4() + 1);
//            }
//            if (aSRecord.getNonDependentStatus() == 5) {
//                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus5(
//                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus5() + 1);
//            }
//            if (aSRecord.getNonDependentStatus() == 6) {
//                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus6(
//                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus6() + 1);
//            }
//            if (aSRecord.getNonDependentStatus() == 7) {
//                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus7(
//                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus7() + 1);
//            }
//            if (aSRecord.getNonDependentStatus() == 8) {
//                a_Aggregate_SHBE_DataRecord.setTotalNonDependentStatus8(
//                        a_Aggregate_SHBE_DataRecord.getTotalNonDependentStatus8() + 1);
//            }
//            a_Aggregate_SHBE_DataRecord.setTotalNonDependantGrossWeeklyIncomeFromRemunerativeWork(
//                    a_Aggregate_SHBE_DataRecord.getTotalNonDependantGrossWeeklyIncomeFromRemunerativeWork()
//                    + aSRecord.getNonDependantGrossWeeklyIncomeFromRemunerativeWork());
//        }
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

    public long getHouseholdSize(DW_SHBE_Record rec) {
        long result;
        result = 1;
        DW_SHBE_D_Record D_Record;
        D_Record = rec.DRecord;
        result += D_Record.getPartnerFlag();
        int NumberOfChildDependents;
        NumberOfChildDependents = D_Record.getNumberOfChildDependents();
        int NumberOfNonDependents;
        NumberOfNonDependents = D_Record.getNumberOfNonDependents();
        int NumberOfDependentsAndNonDependents;
        NumberOfDependentsAndNonDependents = NumberOfChildDependents + NumberOfNonDependents;
        ArrayList<DW_SHBE_S_Record> S_Records;
        S_Records = rec.SRecords;
        if (S_Records != null) {
            result += Math.max(NumberOfDependentsAndNonDependents, S_Records.size());
//            long NumberOfS_Records;
//            NumberOfS_Records = S_Records.size();
//            if (NumberOfS_Records != NumberOfNonDependents ) {
//                rec.init(Env);
//                Iterator<DW_SHBE_S_Record> ite;
//                ite = S_Records.iterator();
//                while (ite.hasNext()) {
//                    DW_SHBE_S_Record S_Record;
//                    S_Record = ite.next();
//                }
//            }
        } else {
            result += NumberOfDependentsAndNonDependents;
        }
        return result;
    }

    public long getHouseholdSizeExcludingPartnerslong(DW_SHBE_D_Record D_Record) {
        long result;
        result = 1;
        result += D_Record.getNumberOfChildDependents();
        long NumberOfNonDependents;
        NumberOfNonDependents = D_Record.getNumberOfNonDependents();
        result += NumberOfNonDependents;
        return result;
    }

    public int getHouseholdSizeExcludingPartnersint(DW_SHBE_D_Record D_Record) {
        int result;
        result = 1;
        result += D_Record.getNumberOfChildDependents();
        long NumberOfNonDependents;
        NumberOfNonDependents = D_Record.getNumberOfNonDependents();
        result += NumberOfNonDependents;
        return result;
    }

    public long getHouseholdSize(DW_SHBE_D_Record D_Record) {
        long result;
        result = getHouseholdSizeint(D_Record);
        return result;
    }

    public int getHouseholdSizeint(DW_SHBE_D_Record D_Record) {
        int result;
        result = getHouseholdSizeExcludingPartnersint(D_Record);
        result += D_Record.getPartnerFlag();
        return result;
    }

    public long getClaimantsIncomeFromBenefitsAndAllowances(
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

    public long getClaimantsIncomeFromEmployment(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getClaimantsGrossWeeklyIncomeFromEmployment();
        result += aDRecord.getClaimantsGrossWeeklyIncomeFromSelfEmployment();
        return result;
    }

    public long getClaimantsIncomeFromGovernmentTraining(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getClaimantsIncomeFromGovernmentTraining();
        result += aDRecord.getClaimantsIncomeFromTrainingForWorkCommunityAction();
        result += aDRecord.getClaimantsIncomeFromYouthTrainingScheme();
        return result;
    }

    public long getClaimantsIncomeFromPensionPrivate(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getClaimantsIncomeFromOccupationalPension();
        result += aDRecord.getClaimantsIncomeFromPersonalPension();
        return result;
    }

    public long getClaimantsIncomeFromPensionState(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc();
        result += aDRecord.getClaimantsIncomeFromWarDisablementPensionArmedForcesGIP();
        result += aDRecord.getClaimantsIncomeFromWarWidowsWidowersPension();
        return result;
    }

    public long getClaimantsIncomeFromBoardersAndSubTenants(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getClaimantsIncomeFromSubTenants();
        result += aDRecord.getClaimantsIncomeFromBoarders();
        return result;
    }

    public long getClaimantsIncomeFromOther(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getClaimantsIncomeFromMaintenancePayments();
        result += aDRecord.getClaimantsIncomeFromStudentGrantLoan();
        result += aDRecord.getClaimantsOtherIncome();
        return result;
    }

    public long getClaimantsIncomeTotal(
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

    public long getHouseholdIncomeTotal(
            DW_SHBE_Record aRecord,
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += getClaimantsIncomeTotal(aDRecord);
        result += getPartnersIncomeTotal(aDRecord);
        ArrayList<DW_SHBE_S_Record> SRecords;
        SRecords = aRecord.getSRecords();
        if (SRecords != null) {
            Iterator<DW_SHBE_S_Record> ite;
            ite = SRecords.iterator();
            DW_SHBE_S_Record DW_SHBE_S_Record;
            while (ite.hasNext()) {
                DW_SHBE_S_Record = ite.next();
                result += DW_SHBE_S_Record.getNonDependantGrossWeeklyIncomeFromRemunerativeWork();
            }
        }
        return result;
    }

    public long getPartnersIncomeFromBenefitsAndAllowances(
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

    public long getPartnersIncomeFromEmployment(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getPartnersGrossWeeklyIncomeFromEmployment();
        result += aDRecord.getPartnersGrossWeeklyIncomeFromSelfEmployment();
        return result;
    }

    public long getPartnersIncomeFromGovernmentTraining(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getPartnersIncomeFromGovernmentTraining();
        result += aDRecord.getPartnersIncomeFromTrainingForWorkCommunityAction();
        result += aDRecord.getPartnersIncomeFromYouthTrainingScheme();
        return result;
    }

    public long getPartnersIncomeFromPensionPrivate(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getPartnersIncomeFromOccupationalPension();
        result += aDRecord.getPartnersIncomeFromPersonalPension();
        return result;
    }

    public long getPartnersIncomeFromPensionState(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getPartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc();
        result += aDRecord.getPartnersIncomeFromWarDisablementPensionArmedForcesGIP();
        result += aDRecord.getPartnersIncomeFromWarWidowsWidowersPension();
        return result;
    }

    public long getPartnersIncomeFromBoardersAndSubTenants(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getPartnersIncomeFromSubTenants();
        result += aDRecord.getPartnersIncomeFromBoarders();
        return result;
    }

    public long getPartnersIncomeFromOther(
            DW_SHBE_D_Record aDRecord) {
        long result = 0L;
        result += aDRecord.getPartnersIncomeFromMaintenancePayments();
        result += aDRecord.getPartnersIncomeFromStudentGrantLoan();
        result += aDRecord.getPartnersOtherIncome();
        return result;
    }

    public long getPartnersIncomeTotal(
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

    public long getClaimantsAndPartnersIncomeTotal(
            DW_SHBE_D_Record aDRecord) {
        long result = getClaimantsIncomeTotal(aDRecord) + getPartnersIncomeTotal(aDRecord);
        return result;
    }

    public boolean getUnderOccupancy(
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

    public int getUnderOccupancyAmount(
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
     * @code {if (SHBEFilenamesAll == null) {
 String[] list = Env.getFiles().getInputSHBEDir().list();
 SHBEFilenamesAll = new String[list.length];
 String s;
 String ym;
 TreeMap<String, String> yms;
 yms = new TreeMap<String, String>();
 for (String list1 : list) {
 s = list1;
 ym = getYearMonthNumber(s);
 yms.put(ym, s);
 }
 Iterator<String> ite; ite = yms.keySet().iterator(); int i = 0; while
 (ite.hasNext()) { ym = ite.next(); SHBEFilenamesAll[i] = yms.get(ym);
 i++; } } return SHBEFilenamesAll;} }
     *
     * @return String[] result of SHBE collections filenames
     */
    private String[] SHBEFilenamesAll;

    public int getSHBEFilenamesAllLength() {
        return getSHBEFilenamesAll().length;
    }

    public String[] getSHBEFilenamesAll() {
        if (SHBEFilenamesAll == null) {
            String[] list = Env.getFiles().getInputSHBEDir().list();
            SHBEFilenamesAll = new String[list.length];
            String s;
            String ym;
            TreeMap<String, String> yms;
            yms = new TreeMap<String, String>();
            for (String list1 : list) {
                s = list1;
                ym = getYearMonthNumber(s);
                yms.put(ym, s);
            }
            Iterator<String> ite;
            ite = yms.keySet().iterator();
            int i = 0;
            while (ite.hasNext()) {
                ym = ite.next();
                SHBEFilenamesAll[i] = yms.get(ym);
                i++;
            }
        }
        return SHBEFilenamesAll;
    }

    private ArrayList<DW_YM3> YM3All;

    public ArrayList<DW_YM3> getYM3All() {
        if (YM3All == null) {
            SHBEFilenamesAll = getSHBEFilenamesAll();
            YM3All = new ArrayList<DW_YM3>();
            SHBEFilenamesAll = getSHBEFilenamesAll();
            for (String SHBEFilename : SHBEFilenamesAll) {
                YM3All.add(getYM3(SHBEFilename));
            }
        }
        return YM3All;
    }

    public ArrayList<Integer> getSHBEFilenameIndexes() {
        ArrayList<Integer> result;
        result = new ArrayList<Integer>();
        SHBEFilenamesAll = getSHBEFilenamesAll();
        for (int i = 0; i < SHBEFilenamesAll.length; i++) {
            result.add(i);
        }
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
    public Object[] getTreeMapDateLabelSHBEFilenames(
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
        DW_YM3 yM30 = null;

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
                DW_YM3 yM31;
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
                label = yM30.toString() + "-" + yM31.toString();
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

    /**
     *
     * @param SHBEFilenames
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
    public Object[] getTreeMapDateLabelSHBEFilenamesSingle(
            String[] SHBEFilenames,
            ArrayList<Integer> include) {
        Object[] result;
        result = new Object[2];
        TreeMap<BigDecimal, DW_YM3> valueLabel;
        valueLabel = new TreeMap<BigDecimal, DW_YM3>();
        TreeMap<DW_YM3, BigDecimal> fileLabelValue;
        fileLabelValue = new TreeMap<DW_YM3, BigDecimal>();
        result[0] = valueLabel;
        result[1] = fileLabelValue;

        ArrayList<String> month3Letters;
        month3Letters = Generic_Time.getMonths3Letters();

        int startMonth = 0;
        int startYear = 0;

        boolean first = true;
        Iterator<Integer> ite;
        ite = include.iterator();
        DW_YM3 YM3;
        int yearInt;
        String month;
        int monthInt;
        String m3;
        while (ite.hasNext()) {
            int i = ite.next();
            if (first) {
                YM3 = getYM3(SHBEFilenames[i]);
                int yearInt0 = Integer.valueOf(getYear(SHBEFilenames[i]));
                String m30 = getMonth3(SHBEFilenames[i]);
                int month0Int = month3Letters.indexOf(m30) + 1;
                startMonth = month0Int;
                startYear = yearInt0;
                first = false;
            } else {
                YM3 = getYM3(SHBEFilenames[i]);
                month = getMonth(SHBEFilenames[i]);
                yearInt = Integer.valueOf(getYear(SHBEFilenames[i]));
                m3 = month.substring(0, 3);
                monthInt = month3Letters.indexOf(m3) + 1;
                BigDecimal timeSinceStart;
                timeSinceStart = BigDecimal.valueOf(
                        Generic_Time.getMonthDiff(
                                startYear, yearInt, startMonth, monthInt));
                valueLabel.put(
                        timeSinceStart,
                        YM3);
                fileLabelValue.put(
                        YM3,
                        timeSinceStart);
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
//    public TreeMap<BigDecimal, String> getDateValueLabelSHBEFilenames(
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
    public String getMonth3(String SHBEFilename) {
        String result;
        result = getMonth(SHBEFilename).substring(0, 3);
        return result;
    }

    public DW_YM3 getYM3(String SHBEFilename) {
        return getYM3(SHBEFilename, "_");
    }

    public DW_YM3 getYM3(String SHBEFilename, String separator) {
        DW_YM3 result;
        String year;
        year = getYear(SHBEFilename);
        String m3;
        m3 = getMonth3(SHBEFilename);
        result = new DW_YM3(year + separator + m3);
        return result;
    }

    public String getYM3FromYearMonthNumber(String YearMonth) {
        String result;
        String[] yM;
        yM = YearMonth.split("-");
        String m3;
        m3 = Generic_Time.getMonth3Letters(yM[1]);
        result = yM[0] + Strings.sUnderscore + m3;
        return result;
    }

    public String getYearMonthNumber(String SHBEFilename) {
        String result;
        String year;
        year = getYear(SHBEFilename);
        String monthNumber;
        monthNumber = getMonthNumber(SHBEFilename);
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
    public String getMonth(String SHBEFilename) {
        return SHBEFilename.split(" ")[1];
    }

    /**
     * For example for SHBEFilename "hb9991_SHBE_555086k May 2013.csv", this
     * returns "May"
     *
     * @param SHBEFilename
     * @return
     */
    public String getMonthNumber(String SHBEFilename) {
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
    public String getYear(String SHBEFilename) {
        return SHBEFilename.split(" ")[2].substring(0, 4);
    }

    /**
     * Method for getting SHBE collections filenames in an array
     *
     * @return String[] SHBE collections filenames
     */
    public String[] getSHBEFilenamesSome() {
        String[] result = new String[6];
        result[0] = "hb9991_SHBE_549416k April 2013.csv";
        result[1] = "hb9991_SHBE_555086k May 2013.csv";
        result[2] = "hb9991_SHBE_562036k June 2013.csv";
        result[3] = "hb9991_SHBE_568694k July 2013.csv";
        result[4] = "hb9991_SHBE_576432k August 2013.csv";
        result[5] = "hb9991_SHBE_582832k September 2013.csv";
        return result;
    }

    public HashMap<DW_ID, String> getIDToStringLookup(
            File f) {
        HashMap<DW_ID, String> result;
        if (f.exists()) {
            result = (HashMap<DW_ID, String>) Generic_IO.readObject(f);
        } else {
            result = new HashMap<DW_ID, String>();
        }
        return result;
    }

    public HashMap<String, DW_ID> getStringToIDLookup(
            File f) {
        HashMap<String, DW_ID> result;
        if (f.exists()) {
            result = (HashMap<String, DW_ID>) Generic_IO.readObject(f);
        } else {
            result = new HashMap<String, DW_ID>();
        }
        return result;
    }

    /**
     * @param YM3
     * @param doUnderOccupancy
     * @param doCouncil
     * @param doRSL
     * @return
     */
    public File getIncomeAndRentSummaryFile(
            //String PT,
            DW_YM3 YM3,
            boolean doUnderOccupancy,
            boolean doCouncil,
            boolean doRSL
    ) {
        File result;
        String partFilename;
        if (doUnderOccupancy) {
            if (doCouncil) {
                if (doRSL) {
                    //partFilename = "IncomeAndRentSummaryUA_HashMap_String__BigDecimal.dat";
                    partFilename = Strings.sIncomeAndRentSummary
                            + Strings.sU + Strings.sA + Strings.sUnderscore
                            + Strings.sHashMap + Strings.sUnderscore
                            + Strings.sString + Strings.sUnderscore + Strings.sUnderscore
                            + Strings.sBigDecimal + Strings.sBinaryFileExtension;
                } else {
                    //partFilename = "IncomeAndRentSummaryUC_HashMap_String__BigDecimal.dat";
                    partFilename = Strings.sIncomeAndRentSummary
                            + Strings.sU + Strings.sCouncil + Strings.sUnderscore
                            + Strings.sHashMap + Strings.sUnderscore
                            + Strings.sString + Strings.sUnderscore + Strings.sUnderscore
                            + Strings.sBigDecimal + Strings.sBinaryFileExtension;
                }
            } else {
                //partFilename = "IncomeAndRentSummaryUR_HashMap_String__BigDecimal.dat";
                partFilename = Strings.sIncomeAndRentSummary
                        + Strings.sU + Strings.sRSL + Strings.sUnderscore
                        + Strings.sHashMap + Strings.sUnderscore
                        + Strings.sString + Strings.sUnderscore + Strings.sUnderscore
                        + Strings.sBigDecimal + Strings.sBinaryFileExtension;
            }
        } else {
            //partFilename = "IncomeAndRentSummary_HashMap_String__BigDecimal.dat";
            partFilename = Strings.sIncomeAndRentSummary
                    + Strings.sHashMap + Strings.sUnderscore
                    + Strings.sString + Strings.sUnderscore + Strings.sUnderscore
                    + Strings.sBigDecimal + Strings.sBinaryFileExtension;
        }
        File dir;
        dir = new File(
                Files.getGeneratedSHBEDir(),
                YM3.toString());
        dir = Files.getUODir(dir, doUnderOccupancy, doCouncil);
        dir.mkdirs();
        result = new File(
                dir,
                partFilename);
        return result;
    }

    public int getNumberOfTenancyTypes() {
        return 10;
    }

    public int getNumberOfPassportedStandardIndicators() {
        return 6;
    }

    public int getNumberOfClaimantsEthnicGroups() {
        return 17;
    }

    public int getNumberOfClaimantsEthnicGroupsGrouped() {
        return 10;
    }

    public int getOneOverMaxValueOfPassportStandardIndicator() {
        return 6;
    }

    /**
     * Negation of getOmits()
     *
     * sIncludeAll
     * sIncludeYearly
     * sInclude6Monthly
     * sInclude3Monthly
     * sIncludeMonthly
     * sIncludeMonthlySinceApril2013
     * sInclude2MonthlySinceApril2013Offset0
     * sInclude2MonthlySinceApril2013Offset1
     * sIncludeStartEndSinceApril2013
     * sIncludeApril2013May2013
     * 
     * @return
     */
    public TreeMap<String, ArrayList<Integer>> getIncludes() {
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
            //include = getSHBEFilenameIndexesExcept34();
            include = getSHBEFilenameIndexes();
            include.removeAll(omit);
            result.put(omitKey, include);
        }
        return result;
    }

    /**
     *
     * @return
     */
    public ArrayList<Integer> getOmitAll() {
        return new ArrayList<Integer>();
    }

    public ArrayList<Integer> getIncludeAll() {
        ArrayList<Integer> r;
        ArrayList<Integer> omit;
        omit = getOmitAll();
        r = getSHBEFilenameIndexes();
        r.removeAll(omit);
        return r;
    }

    /**
     *
     * @param n The number of SHBE Files.
     * @return
     */
    public ArrayList<Integer> getOmitYearly(int n) {
        ArrayList<Integer> r;
        r = new ArrayList<Integer>();
        r.add(1);
        r.add(3);
        r.add(5);
        r.add(6);
        r.add(8);
        r.add(9);
        r.add(10);
        r.add(12);
        r.add(13);
        r.add(14); //Jan 13 NB. Prior to this data not monthly
        r.add(15); //Feb 13
        r.add(16); //Mar 13
        int i0 = 17;
        for (int i = i0; i < n; i++) {
            // Do not add 17,29,41,53...
            if (!((i - i0) % 12 == 0)) {
                r.add(i);
            }
        }
        return r;
    }

    /**
     *
     * @param n The number of SHBE Files.
     * @return
     */
    public ArrayList<Integer> getIncludeYearly(int n) {
        ArrayList<Integer> r;
        ArrayList<Integer> omit;
        omit = getOmitYearly(n);
        r = getSHBEFilenameIndexes();
        r.removeAll(omit);
        return r;
    }

    /**
     *
     * @param n The number of SHBE Files.
     * @return
     */
    public ArrayList<Integer> getOmit6Monthly(int n) {
        ArrayList<Integer> r;
        r = new ArrayList<Integer>();
        r.add(6);
        r.add(8);
        r.add(10);
        r.add(12);
        r.add(14); //Jan 13 NB. Prior to this data not monthly
        r.add(15); //Feb 13
        r.add(16); //Mar 13
        int i0 = 17;
        for (int i = i0; i < n; i++) {
            // Do not add 17,23,29,35,41,47,53...
            if (!((i - i0) % 6 == 0)) {
                r.add(i);
            }
        }
        return r;
    }

    /**
     *
     * @param n The number of SHBE Files.
     * @return
     */
    public ArrayList<Integer> getInclude6Monthly(int n) {
        ArrayList<Integer> r;
        ArrayList<Integer> omit;
        omit = getOmit6Monthly(n);
        r = getSHBEFilenameIndexes();
        r.removeAll(omit);
        return r;
    }

    /**
     *
     * @param n The number of SHBE Files.
     * @return
     */
    public ArrayList<Integer> getOmit3Monthly(int n) {
        ArrayList<Integer> r;
        r = new ArrayList<Integer>();
        for (int i = 0; i < 7; i++) {
            r.add(i);
        }
        r.add(15); //Feb 13 NB. Prior to this data not monthly
        r.add(16); //Mar 13
        int i0 = 17;
        for (int i = i0; i < n; i++) {
            // Do not add 17,20,23,26,29,32,35,38,41,44,47,50,53...
            if (!((i - i0) % 3 == 0)) {
                r.add(i);
            }
        }
        return r;
    }

    /**
     *
     * @return
     */
    public ArrayList<Integer> getInclude3Monthly() {
        int n;
        n = getSHBEFilenamesAllLength();
        return getInclude3Monthly(n);
    }

    /**
     *
     * @param n The number of SHBE Files.
     * @return
     */
    public ArrayList<Integer> getInclude3Monthly(int n) {
        ArrayList<Integer> r;
        ArrayList<Integer> omit;
        omit = getOmit3Monthly(n);
        r = getSHBEFilenameIndexes();
        r.removeAll(omit);
        return r;
    }

    /**
     *
     * @return
     */
    public ArrayList<Integer> getOmitMonthly() {
        ArrayList<Integer> r;
        r = new ArrayList<Integer>();
        for (int i = 0; i < 14; i++) {
            r.add(i);
        }
        return r;
    }

    /**
     *
     * @return
     */
    public ArrayList<Integer> getIncludeMonthly() {
        ArrayList<Integer> r;
        ArrayList<Integer> omit;
        omit = getOmitMonthly();
        r = getSHBEFilenameIndexes();
        r.removeAll(omit);
        return r;
    }

    /**
     * @return a list with the indexes of all SHBE files to omit when
     * considering only those in the period from April 2013.
     */
    public ArrayList<Integer> getOmitMonthlyUO() {
        ArrayList<Integer> r;
        r = new ArrayList<Integer>();
        for (int i = 0; i < 17; i++) {
            r.add(i);
        }
        return r;
    }

    /**
     * @param n The number of SHBE Files.
     * @return a list with the indexes of all SHBE files to omit when
     * considering only those in the period from April 2013 every other month
     * offset by 1 month.
     */
    public ArrayList<Integer> getOmit2MonthlyUO1(int n) {
        ArrayList<Integer> r;
        r = new ArrayList<Integer>();
        for (int i = 0; i < 17; i++) {
            r.add(i);
        }
        for (int i = 17; i < n; i += 2) {
            r.add(i);
        }
        return r;
    }

    /**
     * @param n The number of SHBE Files.
     * @return a list with the indexes of all SHBE files to omit when
     * considering only those in the period from April 2013 every other month
     * offset by 1 month.
     */
    public ArrayList<Integer> getOmit2StartEndSinceApril2013(int n) {
        ArrayList<Integer> r;
        r = new ArrayList<Integer>();
        for (int i = 0; i < 17; i++) {
            r.add(i);
        }
        for (int i = 18; i < n - 2; i++) {
            r.add(i);
        }
        r.add(n - 1);
        return r;
    }

    /**
     * @param n The number of SHBE Files.
     * @return a list with the indexes of all SHBE files to omit when
     * considering only those in the period from April 2013 every other month
     * offset by 1 month.
     */
    public ArrayList<Integer> getOmit2April2013May2013(int n) {
        ArrayList<Integer> r;
        r = new ArrayList<Integer>();
        for (int i = 0; i < 17; i++) {
            r.add(i);
        }
        for (int i = 19; i < n; i++) {
            r.add(i);
        }
        return r;
    }

    /**
     * @param n The number of SHBE Files.
     * @return a list with the indexes of all SHBE files to omit when
     * considering only those in the period from April 2013 every other month
     * offset by 0 months.
     */
    public ArrayList<Integer> getOmit2MonthlyUO0(int n) {
        ArrayList<Integer> r;
        r = new ArrayList<Integer>();
        for (int i = 0; i < 17; i++) {
            r.add(i);
        }
        for (int i = 18; i < n; i += 2) {
            r.add(i);
        }
        return r;
    }

    /**
     *
     * @return
     */
    public ArrayList<Integer> getIncludeMonthlyUO() {
        int n;
        n = getSHBEFilenamesAllLength();
        return getIncludeMonthlyUO(n);
    }

    /**
     *
     * @param n The number of SHBE Files.
     * @return
     */
    public ArrayList<Integer> getIncludeMonthlyUO(int n) {
        ArrayList<Integer> r;
        ArrayList<Integer> omit;
        omit = getOmitMonthlyUO();
        r = getSHBEFilenameIndexes();
        r.removeAll(omit);
        return r;
    }

    /**
     * Negation of getIncludes(). This method will want modifying if data prior
     * to January 2013 is added.
     * 
     * sIncludeAll
     * sIncludeYearly
     * sInclude6Monthly
     * sInclude3Monthly
     * sIncludeMonthly
     * sIncludeMonthlySinceApril2013
     * sInclude2MonthlySinceApril2013Offset0
     * sInclude2MonthlySinceApril2013Offset1
     * sIncludeStartEndSinceApril2013
     * sIncludeApril2013May2013
     *
     * @return
     */
    public TreeMap<String, ArrayList<Integer>> getOmits() {
        TreeMap<String, ArrayList<Integer>> result;
        result = new TreeMap<String, ArrayList<Integer>>();
        String[] tSHBEFilenames;
        tSHBEFilenames = getSHBEFilenamesAll();
        int n;
        n = tSHBEFilenames.length;
        String omitKey;
        ArrayList<Integer> omitAll;
        omitKey = Strings.sIncludeAll;
        omitAll = getOmitAll();
        result.put(omitKey, omitAll);
        omitKey = Strings.sIncludeYearly;
        ArrayList<Integer> omitYearly;
        omitYearly = getOmitYearly(n);
        result.put(omitKey, omitYearly);
        omitKey = Strings.sInclude6Monthly;
        ArrayList<Integer> omit6Monthly;
        omit6Monthly = getOmit6Monthly(n);
        result.put(omitKey, omit6Monthly);
        omitKey = Strings.sInclude3Monthly;
        ArrayList<Integer> omit3Monthly;
        omit3Monthly = getOmit3Monthly(n);
        result.put(omitKey, omit3Monthly);
        omitKey = Strings.sIncludeMonthly;
        ArrayList<Integer> omitMonthly;
        omitMonthly = getOmitMonthly();
        result.put(omitKey, omitMonthly);
        omitKey = Strings.sIncludeMonthlySinceApril2013;
        ArrayList<Integer> omitMonthlyUO;
        omitMonthlyUO = getOmitMonthlyUO();
        result.put(omitKey, omitMonthlyUO);
        omitKey = Strings.sInclude2MonthlySinceApril2013Offset0;
        ArrayList<Integer> omit2MonthlyUO0;
        omit2MonthlyUO0 = getOmit2MonthlyUO0(n);
        result.put(omitKey, omit2MonthlyUO0);
        omitKey = Strings.sInclude2MonthlySinceApril2013Offset1;
        ArrayList<Integer> omit2MonthlyUO1;
        omit2MonthlyUO1 = getOmit2MonthlyUO1(n);
        result.put(omitKey, omit2MonthlyUO1);
        omitKey = Strings.sIncludeStartEndSinceApril2013;
        ArrayList<Integer> omit2StartEndSinceApril2013;
        omit2StartEndSinceApril2013 = getOmit2StartEndSinceApril2013(n);
        result.put(omitKey, omit2StartEndSinceApril2013);
        omitKey = Strings.sIncludeApril2013May2013;
        ArrayList<Integer> omit2April2013May2013;
        omit2April2013May2013 = getOmit2April2013May2013(n);
        result.put(omitKey, omit2April2013May2013);
        return result;
    }

    /**
     *
     * @param yM3
     * @param D_Record
     * @return
     */
    public String getClaimantsAge(
            String yM3,
            DW_SHBE_D_Record D_Record) {
        String result;
        String[] syM3;
        syM3 = yM3.split(Strings.sUnderscore);
        result = getClaimantsAge(syM3[0], syM3[1], D_Record);
        return result;
    }

    /**
     *
     * @param year
     * @param month
     * @param D_Record
     * @return
     */
    public String getClaimantsAge(
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
     * @param year
     * @param month
     * @param D_Record
     * @return
     */
    public String getPartnersAge(
            String year,
            String month,
            DW_SHBE_D_Record D_Record) {
        String result;
        String DoB = D_Record.getPartnersDateOfBirth();
        result = getAge(year, month, DoB);
        return result;
    }

    public String getAge(
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
                0,
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
    public boolean getDisability(DW_SHBE_D_Record D_Record) {
        // Disability
        int DisabilityPremiumAwarded = D_Record.getDisabilityPremiumAwarded();
        int SevereDisabilityPremiumAwarded = D_Record.getSevereDisabilityPremiumAwarded();
        int DisabledChildPremiumAwarded = D_Record.getDisabledChildPremiumAwarded();
        int EnhancedDisabilityPremiumAwarded = D_Record.getEnhancedDisabilityPremiumAwarded();
        // General Household Disability Flag
        return DisabilityPremiumAwarded == 1
                || SevereDisabilityPremiumAwarded == 1
                || DisabledChildPremiumAwarded == 1
                || EnhancedDisabilityPremiumAwarded == 1;
    }

    public int getEthnicityGroup(DW_SHBE_D_Record D_Record) {
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

    public String getEthnicityName(DW_SHBE_D_Record D_Record) {
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

    public String getEthnicityGroupName(int ethnicityGroup) {
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

    /**
     *
     * @param D_Record
     * @return
     */
    public DW_PersonID getClaimantPersonID(DW_SHBE_D_Record D_Record) {
        DW_PersonID result;
        DW_ID NINO_ID;
        NINO_ID = NINOToNINOIDLookup.get(D_Record.getClaimantsNationalInsuranceNumber());
        DW_ID DOB_ID;
        DOB_ID = DOBToDOBIDLookup.get(D_Record.getClaimantsDateOfBirth());
        result = new DW_PersonID(NINO_ID, DOB_ID);
        return result;
    }

    /**
     *
     * @param D_Record
     * @return
     */
    public DW_PersonID getPartnerPersonID(DW_SHBE_D_Record D_Record) {
        DW_PersonID result;
        DW_ID NINO_ID;
        NINO_ID = NINOToNINOIDLookup.get(D_Record.getPartnersNationalInsuranceNumber());
        DW_ID DOB_ID;
        DOB_ID = DOBToDOBIDLookup.get(D_Record.getPartnersDateOfBirth());
        result = new DW_PersonID(NINO_ID, DOB_ID);
        return result;
    }

    /**
     *
     * @param S_Record
     * @return
     */
    public DW_PersonID getNonDependentPersonID(DW_SHBE_S_Record S_Record) {
        DW_PersonID result;
        DW_ID NINO_ID;
        NINO_ID = NINOToNINOIDLookup.get(S_Record.getSubRecordChildReferenceNumberOrNINO());
        DW_ID DOB_ID;
        DOB_ID = DOBToDOBIDLookup.get(S_Record.getSubRecordDateOfBirth());
        result = new DW_PersonID(NINO_ID, DOB_ID);
        return result;
    }

    /**
     *
     * @param S_Record
     * @param index
     * @return
     */
    public DW_PersonID getDependentPersonID(DW_SHBE_S_Record S_Record, int index) {
        DW_PersonID result;
        String NINO;
        String ClaimantsNINO;
        DW_ID NINO_ID;
        DW_ID DOB_ID;
        NINO = S_Record.getSubRecordChildReferenceNumberOrNINO();
        ClaimantsNINO = S_Record.getClaimantsNationalInsuranceNumber();
        if (ClaimantsNINO.trim().isEmpty()) {
            ClaimantsNINO = Strings.sDefaultNINO;
            Env.logE("ClaimantsNINO is empty for "
                    + "ClaimRef " + S_Record.getCouncilTaxBenefitClaimReferenceNumber()
                    + " Setting as default NINO " + ClaimantsNINO);
        }
        if (NINO.isEmpty()) {
            NINO = "" + index;
            NINO += "_" + ClaimantsNINO;
        } else {
            NINO += "_" + ClaimantsNINO;
        }
        NINO_ID = NINOToNINOIDLookup.get(NINO);
        DOB_ID = DOBToDOBIDLookup.get(S_Record.getSubRecordDateOfBirth());
        result = new DW_PersonID(NINO_ID, DOB_ID);
        return result;
    }

    /**
     *
     * @param S_Records
     * @return
     */
    public HashSet<DW_PersonID> getPersonIDs(ArrayList<DW_SHBE_S_Record> S_Records) {
        HashSet<DW_PersonID> result;
        result = new HashSet<DW_PersonID>();
        DW_SHBE_S_Record S_Record;
        DW_ID NINO_ID;
        DW_ID DOB_ID;
        Iterator<DW_SHBE_S_Record> ite;
        ite = S_Records.iterator();
        while (ite.hasNext()) {
            S_Record = ite.next();
            NINO_ID = NINOToNINOIDLookup.get(S_Record.getSubRecordChildReferenceNumberOrNINO());
            DOB_ID = DOBToDOBIDLookup.get(S_Record.getSubRecordDateOfBirth());

            result.add(new DW_PersonID(NINO_ID, DOB_ID));
        }
        return result;
    }

    /**
     *
     * @param S_Record
     * @return
     */
    public DW_PersonID getNonDependentPersonIDs(DW_SHBE_S_Record S_Record) {
        DW_PersonID result;
        DW_ID NINO_ID;
        DW_ID DOB_ID;
        NINO_ID = NINOToNINOIDLookup.get(S_Record.getSubRecordChildReferenceNumberOrNINO());
        DOB_ID = DOBToDOBIDLookup.get(S_Record.getSubRecordDateOfBirth());
        result = new DW_PersonID(NINO_ID, DOB_ID);
        return result;
    }

    /**
     * For getting a DW_PersonID for the NINO and DOB given. If the NINOID
     * and/or the DOBID do not already exist, these are added to
     * NINOToNINOIDLookup and NINOIDToNINOLookup, and/or DOBToDOBIDLookup and
     * DOBIDToDOBLookup respectfully.
     *
     * @param NINO
     * @param DOB
     * @param NINOToNINOIDLookup
     * @param NINOIDToNINOLookup
     * @param DOBToDOBIDLookup
     * @param DOBIDToDOBLookup
     * @return
     */
    DW_PersonID getPersonID(
            String NINO,
            String DOB,
            HashMap<String, DW_ID> NINOToNINOIDLookup,
            HashMap<DW_ID, String> NINOIDToNINOLookup,
            HashMap<String, DW_ID> DOBToDOBIDLookup,
            HashMap<DW_ID, String> DOBIDToDOBLookup) {
        DW_ID NINOID;
        NINOID = getIDAddIfNeeded(NINO, NINOToNINOIDLookup, NINOIDToNINOLookup);
        DW_ID DOBID;
        DOBID = getIDAddIfNeeded(DOB, DOBToDOBIDLookup, DOBIDToDOBLookup);
        return new DW_PersonID(NINOID, DOBID);
    }

    /**
     * For getting a DW_PersonID for the DRecord. If the NINOID and/or the DOBID
     * for the DRecord do not already exist, these are added to
     * NINOToNINOIDLookup and NINOIDToNINOLookup, and/or DOBToDOBIDLookup and
     * DOBIDToDOBLookup respectfully.
     *
     * @param DRecord
     * @param NINOToNINOIDLookup
     * @param NINOIDToNINOLookup
     * @param DOBToDOBIDLookup
     * @param DOBIDToDOBLookup
     * @return
     */
    DW_PersonID getPersonID(
            DW_SHBE_D_Record DRecord,
            HashMap<String, DW_ID> NINOToNINOIDLookup,
            HashMap<DW_ID, String> NINOIDToNINOLookup,
            HashMap<String, DW_ID> DOBToDOBIDLookup,
            HashMap<DW_ID, String> DOBIDToDOBLookup) {
        String NINO;
        NINO = DRecord.getPartnersNationalInsuranceNumber();
        String DOB;
        DOB = DRecord.getPartnersDateOfBirth();
        return DW_SHBE_Handler.this.getPersonID(NINO, DOB, NINOToNINOIDLookup, NINOIDToNINOLookup,
                DOBToDOBIDLookup, DOBIDToDOBLookup);
    }

    public HashSet<DW_PersonID> getUniquePersonIDs(
            HashMap<DW_ID, HashSet<DW_PersonID>> ClaimIDToPersonIDsLookup) {
        HashSet<DW_PersonID> result;
        Collection<HashSet<DW_PersonID>> c;
        Iterator<HashSet<DW_PersonID>> ite2;
        result = new HashSet<DW_PersonID>();
        c = ClaimIDToPersonIDsLookup.values();
        ite2 = c.iterator();
        while (ite2.hasNext()) {
            result.addAll(ite2.next());
        }
        return result;
    }

    public HashSet<DW_PersonID> getUniquePersonIDs0(
            HashMap<DW_ID, DW_PersonID> ClaimIDToPersonIDLookup) {
        HashSet<DW_PersonID> result;
        result = new HashSet<DW_PersonID>();
        result.addAll(ClaimIDToPersonIDLookup.values());
        return result;
    }
}
