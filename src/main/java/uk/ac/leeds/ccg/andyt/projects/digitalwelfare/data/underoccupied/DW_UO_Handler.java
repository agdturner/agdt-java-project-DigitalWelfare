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
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class DW_UO_Handler extends DW_Object {

    private HashSet<String> RecordTypes;

    /**
     * For convenience.
     */
    protected DW_Files DW_Files;
    protected DW_Strings DW_Strings;

    public DW_UO_Handler(DW_Environment env) {
        this.env = env;
        this.DW_Files = env.getDW_Files();
        this.DW_Strings = env.getDW_Strings();
    }

    public HashSet<String> getRecordTypes() {
        return RecordTypes;
    }

    /**
     * @param directory
     * @param filename
     * @return
     */
    public HashMap<DW_ID, DW_UO_Record> loadInputData(
            File directory,
            String filename) {
//        String type;
//        if (filename.contains("RSL")) {
//            type = "RSL";
//        } else {
//            type = "Council";
//        }
        HashMap<DW_ID, DW_UO_Record> result;
        result = new HashMap<DW_ID, DW_UO_Record>();
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
            String line;
            //int duplicateEntriesCount = 0;
            int replacementEntriesCount = 0;
            long RecordID = 0;
            // Read firstline and check format
            int tokenType;
            st.nextToken();
            line = st.sval;
            String[] fieldnames = line.split(",");
//            // Skip the first line
//            Generic_StaticIO.skipline(st);

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
                            DW_UO_Record aUnderOccupiedReport_Record;
                            aUnderOccupiedReport_Record = new DW_UO_Record(
                                    env, RecordID, line, fieldnames);
                            //RecordID, line, type);
                            Object o = result.put(
                                    aUnderOccupiedReport_Record.getClaimID(),
                                    aUnderOccupiedReport_Record);
                            if (o != null) {
                                DW_UO_Record existingUnderOccupiedReport_Record;
                                existingUnderOccupiedReport_Record = (DW_UO_Record) o;
                                if (!existingUnderOccupiedReport_Record.equals(aUnderOccupiedReport_Record)) {
                                    System.out.println("existingUnderOccupiedReport_DataRecord " + existingUnderOccupiedReport_Record);
                                    System.out.println("replacementUnderOccupiedReport_DataRecord " + aUnderOccupiedReport_Record);
                                    System.out.println("RecordID " + RecordID);
                                    replacementEntriesCount++;
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
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(DW_UO_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Loads the Under-Occupied report data for Leeds.
     *
     * @param reload
     * @return Object[] result where: result[0] = councilSets
     * {@code TreeMap<String, DW_UO_Set>}; result[1] = RSLSets
     * {@code TreeMap<String, DW_UO_Set>}
     */
    public DW_UO_Data loadUnderOccupiedReportData(boolean reload) {
        DW_UO_Data result;
        TreeMap<String, DW_UO_Set> CouncilSets;
        CouncilSets = new TreeMap<String, DW_UO_Set>();
        TreeMap<String, DW_UO_Set> RSLSets;
        RSLSets = new TreeMap<String, DW_UO_Set>();
        // @ToDo
        // If reload:
        //   Reload all files.
        // Else:
        //   Look where the generated data should be stored.
        //   Look where the input data are.
        //   Are there new files to load? If so, load them 
        //   from source. If not, then load and return the 
        //   cached object.
        String type;
        Object[] filenames = getInputFilenames();
        TreeMap<String, String> CouncilFilenames;
        CouncilFilenames = (TreeMap<String, String>) filenames[0];
        TreeMap<String, String> RSLFilenames;
        RSLFilenames = (TreeMap<String, String>) filenames[1];
        Iterator<String> ite;
        ite = CouncilFilenames.keySet().iterator();
        while (ite.hasNext()) {
            type = DW_Strings.sRSL;
            String year_Month = ite.next();
            String filename = CouncilFilenames.get(year_Month);
            System.out.println("<Load " + filename + ">");
            DW_UO_Set set;
            set = new DW_UO_Set(
                    DW_Files,
                    this,
                    type,
                    filename,
                    year_Month,
                    reload);
            CouncilSets.put(year_Month, set);
            System.out.println("</Load " + filename + ">");
        }
        ite = RSLFilenames.keySet().iterator();
        while (ite.hasNext()) {
            type = DW_Strings.sCouncil;
            String year_Month = ite.next();
            String filename = RSLFilenames.get(year_Month);
            System.out.println("<Load " + filename + ">");
            DW_UO_Set set;
            set = new DW_UO_Set(
                    DW_Files,
                    this,
                    type,
                    filename,
                    year_Month,
                    reload);
            RSLSets.put(year_Month, set);
            System.out.println("</Load " + filename + ">");
        }
        result = new DW_UO_Data(env, RSLSets, CouncilSets);
        File f;
        f = new File(DW_Files.getGeneratedUnderOccupiedDir(),
                "DW_UO_Data" + DW_Files.getsDotdat());
        Generic_StaticIO.writeObject(result, f);
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
        dirIn = DW_Files.getInputUnderOccupiedDir();
        File[] files;
        files = dirIn.listFiles();
        TreeSet<String> set;
        set = new TreeSet<String>();
        for (File file : files) {
            //System.out.println(file);
            set.add(file.toString());
        }
        Iterator<String> ite;
        ite = set.iterator();
        while (ite.hasNext()) {
            System.out.println(ite.next());
        }
        System.out.println("set.size() " + set.size());
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
        DW_Files tDW_Files;
        tDW_Files = env.getDW_Files();
        File dirIn;
        dirIn = tDW_Files.getGeneratedUnderOccupiedDir();
        result = dirIn.listFiles().length;
        return result;
    }

    /**
     * Do we really want to store these?
     *
     * @return Object[] result where: null null     {@code 
     * result[0] TreeMap<String, String> councilFilenames (year_Month, filename)
     * result[1] TreeMap<String, String> RSLFilenames (year_Month, filename)
     * }
     */
    private Object[] inputFilenames;

    /**
     *
     * @return
     */
    public Object[] getInputFilenames() {
        if (inputFilenames == null) {
            inputFilenames = new Object[2];
            TreeMap<String, String> CouncilFilenames;
            TreeMap<String, String> RSLFilenames;
            CouncilFilenames = new TreeMap<String, String>();
            RSLFilenames = new TreeMap<String, String>();
            inputFilenames[0] = CouncilFilenames;
            inputFilenames[1] = RSLFilenames;
//            String[] list = DW_Files.getInputUnderOccupiedDir().list();
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
                    "2013_Mar",
                    "2013 14 Under Occupied Report For University Year Start Council Tenants.csv");
            RSLFilenames.put(
                    "2013_Mar",
                    "2013 14 Under Occupied Report For University Year Start RSLs.csv");
            String councilEndFilename = " Council Tenants.csv";
            String RSLEndFilename = " RSLs.csv";
            String RSLEndFilename2 = " RSL.csv";
            String underOccupiedReportForUniversityString = " Under Occupied Report For University ";
            String yearAll;
            yearAll = "2013 14";
            //councilFilenames.add(yearAll + underOccupiedReportForUniversityString + "Year Start" + councilEndFilename);
            //registeredSocialLandlordFilenames.add(yearAll + underOccupiedReportForUniversityString + "Year Start" + RSLEndFilename);
            putFilenames(
                    yearAll,
                    underOccupiedReportForUniversityString,
                    councilEndFilename,
                    RSLEndFilename,
                    CouncilFilenames,
                    RSLFilenames,
                    12);
            yearAll = "2014 15";
            putFilenames(
                    yearAll,
                    underOccupiedReportForUniversityString,
                    councilEndFilename,
                    RSLEndFilename2,
                    CouncilFilenames,
                    RSLFilenames,
                    12);
            yearAll = "2015 16";
            putFilenames(
                    yearAll,
                    underOccupiedReportForUniversityString,
                    councilEndFilename,
                    RSLEndFilename2,
                    CouncilFilenames,
                    RSLFilenames,
                    12);
            yearAll = "2016 17";
            putFilenames(
                    yearAll,
                    underOccupiedReportForUniversityString,
                    councilEndFilename,
                    RSLEndFilename2,
                    CouncilFilenames,
                    RSLFilenames,
                    4); // This number needs increasing as there are more datasets....
        }
        return inputFilenames;
    }

    protected static void putFilenames(
            String yearAll,
            String underOccupiedReportForUniversityString,
            String councilEndFilename,
            String RSLEndFilename2,
            TreeMap<String, String> councilFilenames,
            TreeMap<String, String> registeredSocialLandlordFilenames,
            int maxMonth) {
        for (int i = 1; i <= maxMonth; i++) {
            String year = getYear(yearAll, i);
            String month = getMonth3(i);
            String year_Month = year + "_" + month;
            String s;
            s = yearAll + underOccupiedReportForUniversityString + "Month " + i;
//            String filename;
//            filename = s + councilEndFilename;
//            env.getDW_Files().getInputUnderOccupiedDir();
            councilFilenames.put(
                    year_Month,
                    s + councilEndFilename);
            registeredSocialLandlordFilenames.put(
                    year_Month,
                    s + RSLEndFilename2);
        }
    }

    protected static String getYear(String yearAll, int i) {
        String result;
        String[] split;
        split = yearAll.split(" ");
        if (i < 10) {
            result = split[0];
        } else {
            int year;
            year = Integer.valueOf(split[0]);
            year++;
            result = Integer.toString(year);
        }
        return result;
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
     * Returns ClaimIDs of those Claims deemed to be under occupying at the
     * start of April2013.
     *
     * @param DW_UO_Data
     * @return
     */
    public HashSet<DW_ID> getUOStartApril2013ClaimIDs(
            DW_UO_Data DW_UO_Data) {
        return DW_UO_Data.getClaimIDs().get("2013_Mar");
//        return DW_UO_Data.getClaimIDs().get("2013_Apr");
    }

    public static int getHouseholdSizeExcludingPartners(DW_UO_Record rec) {
        int result;
        result
                = //rec.getTotalDependentChildren()
                rec.getChildrenOver16()
                + rec.getFemaleChildren10to16() + rec.getFemaleChildrenUnder10()
                + rec.getMaleChildren10to16() + rec.getMaleChildrenUnder10()
                + rec.getNonDependents();
        return result;
    }
}
