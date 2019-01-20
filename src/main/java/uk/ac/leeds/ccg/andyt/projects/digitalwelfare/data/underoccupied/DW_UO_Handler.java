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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.core.SHBE_ID;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Handler;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;

/**
 *
 * @author Andy Turner
 */
public class DW_UO_Handler extends DW_Object {

    /**
     * For convenience.
     */
    private HashMap<SHBE_ID, String> ClaimIDToClaimRefLookup;
    private HashMap<String, SHBE_ID> ClaimRefToClaimIDLookup;

    private HashSet<String> RecordTypes;

    public DW_UO_Handler() {
    }

    public DW_UO_Handler(DW_Environment env) {
        super(env);
        SHBE_Handler h = env.getSHBE_Handler();
        ClaimIDToClaimRefLookup = h.getClaimIDToClaimRefLookup();
        ClaimRefToClaimIDLookup = h.getClaimRefToClaimIDLookup();
    }

    public HashSet<String> getRecordTypes() {
        return RecordTypes;
    }

    /**
     * @param directory
     * @param filename
     * @return
     */
    public HashMap<SHBE_ID, DW_UO_Record> loadInputData(File directory,
            String filename) {
//        String type;
//        if (filename.contains("RSL")) {
//            type = "RSL";
//        } else {
//            type = "Council";
//        }
        HashMap<SHBE_ID, DW_UO_Record> result;
        result = new HashMap<>();
        File inputFile = new File(directory, filename);
        boolean addedNewClaimIDs;
        addedNewClaimIDs = false;
        try {
            try (BufferedReader br = Generic_IO.getBufferedReader(inputFile)) {
                StreamTokenizer st = new StreamTokenizer(br);
                Generic_IO.setStreamTokenizerSyntax5(st);
                st.wordChars('`', '`');
                st.wordChars('*', '*');
                String line;
                //int duplicateEntriesCount = 0;
                //int replacementEntriesCount = 0;
                long RecordID = 0;
                // Read firstline and check format
                int tokenType;
                st.nextToken();
                line = st.sval;
                String[] fieldnames = line.split(",");
//            // Skip the first line
//            Generic_IO.skipline(st);
// Read data
                tokenType = st.nextToken();
                while (tokenType != StreamTokenizer.TT_EOF) {
                    switch (tokenType) {
                        case StreamTokenizer.TT_EOL:
                            //System.out.println(line);
                            break;
                        case StreamTokenizer.TT_WORD:
                            line = st.sval;
                            try {
                                DW_UO_Record DW_UO_Record;
                                DW_UO_Record = new DW_UO_Record(
                                        RecordID, line, fieldnames);
                                //RecordID, line, type);
                                String ClaimRef;
                                ClaimRef = DW_UO_Record.getClaimRef();
                                SHBE_ID ClaimID;
                                ClaimID = ClaimRefToClaimIDLookup.get(ClaimRef);
                                if (ClaimID == null) {
                                    ClaimID = new SHBE_ID(ClaimRefToClaimIDLookup.size());
                                    ClaimRefToClaimIDLookup.put(ClaimRef, ClaimID);
                                    ClaimIDToClaimRefLookup.put(ClaimID, ClaimRef);
                                    addedNewClaimIDs = true;
                                }
                                Object o = result.put(
                                        ClaimID,
                                        DW_UO_Record);
                                if (o != null) {
                                    DW_UO_Record existingUnderOccupiedReport_Record;
                                    existingUnderOccupiedReport_Record = (DW_UO_Record) o;
                                    if (!existingUnderOccupiedReport_Record.equals(DW_UO_Record)) {
                                        System.out.println("existingUnderOccupiedReport_DataRecord " + existingUnderOccupiedReport_Record);
                                        System.out.println("replacementUnderOccupiedReport_DataRecord " + DW_UO_Record);
                                        System.out.println("RecordID " + RecordID);
                                        //replacementEntriesCount++;
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println("DW_UnderOccupiedReport_Handler error processing from file " + inputFile);
                                System.err.println(line);
                                System.err.println("RecordID " + RecordID);
                                System.err.println(e.getLocalizedMessage());
                            }
                            RecordID++;
                            break;
                    }
                    tokenType = st.nextToken();
                }
                //System.out.println("replacementEntriesCount " + replacementEntriesCount);
            }
            if (addedNewClaimIDs) {
                Generic_IO.writeObject(ClaimIDToClaimRefLookup,
                        Env.getSHBE_Handler().getClaimIDToClaimRefLookupFile());
                Generic_IO.writeObject(ClaimRefToClaimIDLookup,
                        Env.getSHBE_Handler().getClaimRefToClaimIDLookupFile());
            }
        } catch (IOException ex) {
            Logger.getLogger(DW_UO_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Loads the Under-Occupied report data for Leeds.
     *
     * @param reload If reload is true then the data is reloaded from source.
     * @return
     */
    public DW_UO_Data loadUnderOccupiedReportData(boolean reload) {
        String methodName;
        methodName = "loadUnderOccupiedReportData()";
        Env.logO("<" + methodName + ">", true);
        DW_UO_Data result;
        TreeMap<ONSPD_YM3, DW_UO_Set> CouncilSets;
        CouncilSets = new TreeMap<>();
        TreeMap<ONSPD_YM3, DW_UO_Set> RSLSets;
        RSLSets = new TreeMap<>();

        /**
         * Look where the generated data should be stored. Look where the input
         * data are. Are there new files to load? If so, load them from source.
         * If not, then load and return the cached object.
         */
        String type;
        Object[] filenames = getInputFilenames();
        TreeMap<ONSPD_YM3, String> CouncilFilenames;
        CouncilFilenames = (TreeMap<ONSPD_YM3, String>) filenames[0];
        TreeMap<ONSPD_YM3, String> RSLFilenames;
        RSLFilenames = (TreeMap<ONSPD_YM3, String>) filenames[1];
        ONSPD_YM3 YM3;
        String filename;
        Iterator<ONSPD_YM3> ite;
        ite = CouncilFilenames.keySet().iterator();
        type = Strings.sCouncil;
        while (ite.hasNext()) {
            YM3 = ite.next();
            filename = CouncilFilenames.get(YM3);
            DW_UO_Set set;
            set = new DW_UO_Set(Env, type, filename, YM3, reload);
            CouncilSets.put(YM3, set);
        }
        ite = RSLFilenames.keySet().iterator();
        type = Strings.sRSL;
        while (ite.hasNext()) {
            YM3 = ite.next();
            filename = RSLFilenames.get(YM3);
            DW_UO_Set set;
            set = new DW_UO_Set(Env, type, filename, YM3, reload);
            RSLSets.put(YM3, set);
        }
        result = new DW_UO_Data(Env, RSLSets, CouncilSets);
        Env.logO("</" + methodName + ">", true);
        return result;
    }

    /**
     * Returns the number of input files of UnderOccupied Data
     *
     * @return
     */
    public int getNumberOfInputFiles() {
        int result;
        File dirIn;
        dirIn = Files.getInputUnderOccupiedDir();
        File[] files;
        files = dirIn.listFiles();
        result = files.length;
        return result;
    }

    /**
     * Returns the number of generated files of UnderOccupied Data
     *
     * @return
     */
    public int getNumberOfGeneratedFiles() {
        int result;
        File dirIn;
        dirIn = Files.getGeneratedUnderOccupiedDir();
        result = dirIn.listFiles().length;
        return result;
    }

    /**
     * Do we really want to store these?
     *
     * @return Object[] result where: null null null null null null null null
     * null null null null null null null null null     {@code 
     * result[0] TreeMap<String, String> councilFilenames (year_Month, filename)
     * result[1] TreeMap<String, String> RSLFilenames (year_Month, filename)
     * }
     */
    private Object[] inputFilenames;

    /**
     * This needs modifying as more datasets are added currently....
     *
     * @return
     */
    public Object[] getInputFilenames() {
        if (inputFilenames == null) {
            inputFilenames = new Object[2];
            TreeMap<ONSPD_YM3, String> CouncilFilenames;
            TreeMap<ONSPD_YM3, String> RSLFilenames;
            CouncilFilenames = new TreeMap<>();
            RSLFilenames = new TreeMap<>();
            inputFilenames[0] = CouncilFilenames;
            inputFilenames[1] = RSLFilenames;
//            String[] list = Files.getInputUnderOccupiedDir().list();
//            String s;
//            String ym;
//            TreeMap<String,String> yms;
//            yms = new TreeMap<String,String>();
//            for (String list1 : list) {
//                s = list1;
//                ym = getYearMonthNumber(s);
//                yms.put(ym, s);
//            }
//            Iterator<String> ite;
//            ite = yms.keySet().iterator();
//            int i = 0;
//            while (ite.hasNext()) {
//                ym = ite.next();
//                SHBEFilenamesAll[i] = yms.get(ym);
//                i ++;
//            }
            CouncilFilenames.put(
                    new ONSPD_YM3(2013, 3),
                    "2013 14 Under Occupied Report For University Year Start Council Tenants.csv");
            RSLFilenames.put(
                    new ONSPD_YM3(2013, 3),
                    "2013 14 Under Occupied Report For University Year Start RSLs.csv");
            String councilEndFilename = " Council Tenants.csv";
            String RSLEndFilename = " RSLs.csv";
            String RSLEndFilename2 = " RSL.csv";
            String underOccupiedReportForUniversityString = " Under Occupied Report For University ";
            String yearAll;
            yearAll = "2013 14";
            //councilFilenames.add(yearAll + underOccupiedReportForUniversityString + "Year Start" + councilEndFilename);
            //registeredSocialLandlordFilenames.add(yearAll + underOccupiedReportForUniversityString + "Year Start" + RSLEndFilename);
            putFilenames(yearAll, underOccupiedReportForUniversityString,
                    councilEndFilename, RSLEndFilename, CouncilFilenames,
                    RSLFilenames, 1, 12);
            yearAll = "2014 15";
            putFilenames(yearAll, underOccupiedReportForUniversityString,
                    councilEndFilename, RSLEndFilename2, CouncilFilenames,
                    RSLFilenames, 1, 12);
            yearAll = "2015 16";
            putFilenames(yearAll, underOccupiedReportForUniversityString,
                    councilEndFilename, RSLEndFilename2, CouncilFilenames,
                    RSLFilenames, 1, 12);
            yearAll = "2016 17";
            putFilenames(yearAll, underOccupiedReportForUniversityString,
                    councilEndFilename, RSLEndFilename2, CouncilFilenames,
                    RSLFilenames, 1, 12);
            yearAll = "2017 18";
            putFilenames(yearAll, underOccupiedReportForUniversityString,
                    councilEndFilename, RSLEndFilename2, CouncilFilenames,
                    RSLFilenames, 1, 9); // The number 6 needs increasing as there are more datasets....
        }
        return inputFilenames;
    }

    protected static void putFilenames(String yearAll,
            String underOccupiedReportForUniversityString,
            String councilEndFilename, String RSLEndFilename2,
            TreeMap<ONSPD_YM3, String> CouncilFilenames,
            TreeMap<ONSPD_YM3, String> RSLFilenames,
            int minMonth, int maxMonth) {
        for (int i = minMonth; i <= maxMonth; i++) {
            String y = getYear(yearAll, i);
            String m = getMonth3(i);
            String ym = y + "_" + m;
            String s;
            s = yearAll + underOccupiedReportForUniversityString + "Month " + i;
//            String filename;
//            filename = s + councilEndFilename;
//            Env.getFiles().getInputUnderOccupiedDir();
            CouncilFilenames.put(new ONSPD_YM3(ym), s + councilEndFilename);
            RSLFilenames.put(new ONSPD_YM3(ym), s + RSLEndFilename2);
        }
    }

    protected static String getYear(String yearAll, int i) {
        String r;
        String[] split;
        split = yearAll.split(" ");
        if (i < 10) {
            r = split[0];
        } else {
            int y;
            y = Integer.valueOf(split[0]);
            y++;
            r = Integer.toString(y);
        }
        return r;
    }

    // The first month of this year sequence is April (financial year)
    protected static String getMonth(int i) {
        switch (i) {
            case 10:
                return "January";
            case 11:
                return "February";
            case 12:
                return "March";
            case 1:
                return "April";
            case 2:
                return "May";
            case 3:
                return "June";
            case 4:
                return "July";
            case 5:
                return "August";
            case 6:
                return "September";
            case 7:
                return "October";
            case 8:
                return "November";
            case 9:
                return "December";
        }
        return "";
    }

    // The first month of this year sequence is April (financial year)
    protected static String getMonth3(int i) {
        return getMonth(i).substring(0, 3);
    }

    /**
     * Returns a {@code Set<SHBE_ID>} of the ClaimIDs of those UnderOccupying at
     * the start of April2013.
     *
     * @param DW_UO_Data
     * @return
     */
    public Set<SHBE_ID> getUOStartApril2013ClaimIDs(
            DW_UO_Data DW_UO_Data) {
        return DW_UO_Data.getClaimIDsInUO().get(DW_UO_Data.getBaselineYM3());
    }

    /**
     * This returns a Set of all ClaimIDs of all Claims that have at some time
     * been classed as Council Under Occupying.
     *
     * @param DW_UO_Data
     * @return
     */
    public Set<SHBE_ID> getAllCouncilUOClaimIDs(
            DW_UO_Data DW_UO_Data) {
        return DW_UO_Data.getClaimIDsInCouncilUO();
    }

    public static int getHouseholdSizeExcludingPartners(DW_UO_Record rec) {
        int result;
        result
                = 1 + rec.getTotalDependentChildren()
                //rec.getChildrenOver16()
                //+ rec.getFemaleChildren10to16() + rec.getFemaleChildrenUnder10()
                //+ rec.getMaleChildren10to16() + rec.getMaleChildrenUnder10()
                + rec.getNonDependents();
        return result;
    }

}
