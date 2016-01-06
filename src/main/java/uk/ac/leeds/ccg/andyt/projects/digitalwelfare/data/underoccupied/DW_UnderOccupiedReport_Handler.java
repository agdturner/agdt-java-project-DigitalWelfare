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
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;

/**
 *
 * @author geoagdt
 */
public class DW_UnderOccupiedReport_Handler {

    private HashSet<String> RecordTypes;

    public DW_UnderOccupiedReport_Handler() {
    }

    public HashSet<String> getRecordTypes() {
        return RecordTypes;
    }

    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {
     new    DW_UnderOccupiedReport_Handler().run();
    }
    
    public void run() {
        Object[] underOccupiedReportData;
        underOccupiedReportData = loadUnderOccupiedReportData();
        /*
         * 0 2013 14 Under Occupied Report For University Year Start Council Tenants.csv
         * 1 2013 14 Under Occupied Report For University Month 1 Council Tenants.csv
         * 2 2013 14 Under Occupied Report For University Month 2 Council Tenants.csv
         * 3 2013 14 Under Occupied Report For University Month 3 Council Tenants.csv
         * 4 2013 14 Under Occupied Report For University Month 4 Council Tenants.csv
         * 5 2013 14 Under Occupied Report For University Month 5 Council Tenants.csv
         * 6 2013 14 Under Occupied Report For University Month 6 Council Tenants.csv
         * 7 2013 14 Under Occupied Report For University Month 7 Council Tenants.csv
         * 8 2013 14 Under Occupied Report For University Month 8 Council Tenants.csv
         * 9 2013 14 Under Occupied Report For University Month 9 Council Tenants.csv
         * 10 2013 14 Under Occupied Report For University Month 10 Council Tenants.csv
         * 11 2013 14 Under Occupied Report For University Month 11 Council Tenants.csv
         * 12 2013 14 Under Occupied Report For University Month 12 Council Tenants.csv
         * 13 2014 15 Under Occupied Report For University Month 1 Council Tenants.csv
         * 14 2014 15 Under Occupied Report For University Month 2 Council Tenants.csv
         * 15 2014 15 Under Occupied Report For University Month 3 Council Tenants.csv
         * 16 2014 15 Under Occupied Report For University Month 4 Council Tenants.csv
         * 17 2014 15 Under Occupied Report For University Month 5 Council Tenants.csv
         * 18 2014 15 Under Occupied Report For University Month 6 Council Tenants.csv
         * 19 2014 15 Under Occupied Report For University Month 7 Council Tenants.csv
         * 20 2014 15 Under Occupied Report For University Month 8 Council Tenants.csv
         * 21 2014 15 Under Occupied Report For University Month 9 Council Tenants.csv
         * 22 2015 16 Under Occupied Report For University Month 1 Council Tenants.csv
         * 23 2015 16 Under Occupied Report For University Month 2 Council Tenants.csv
         * 24 2015 16 Under Occupied Report For University Month 3 Council Tenants.csv
         * 25 2015 16 Under Occupied Report For University Month 4 Council Tenants.csv
         * 26 2015 16 Under Occupied Report For University Month 5 Council Tenants.csv
         * 27 2015 16 Under Occupied Report For University Month 6 Council Tenants.csv
         * 28 2015 16 Under Occupied Report For University Month 7 Council Tenants.csv
         *
         * 0 2013 14 Under Occupied Report For University Year Start RSLs.csv
         * 1 2013 14 Under Occupied Report For University Month 1 RSLs.csv
         * 2 2013 14 Under Occupied Report For University Month 2 RSLs.csv
         * 3 2013 14 Under Occupied Report For University Month 3 RSLs.csv
         * 4 2013 14 Under Occupied Report For University Month 4 RSLs.csv
         * 5 2013 14 Under Occupied Report For University Month 5 RSLs.csv
         * 6 2013 14 Under Occupied Report For University Month 6 RSLs.csv
         * 7 2013 14 Under Occupied Report For University Month 7 RSLs.csv
         * 8 2013 14 Under Occupied Report For University Month 8 RSLs.csv
         * 9 2013 14 Under Occupied Report For University Month 9 RSLs.csv
         * 10 2013 14 Under Occupied Report For University Month 10 RSLs.csv
         * 11 2013 14 Under Occupied Report For University Month 11 RSLs.csv
         * 12 2013 14 Under Occupied Report For University Month 12 RSLs.csv
         * 13 2014 15 Under Occupied Report For University Month 1 RSL.csv
         * 14 2014 15 Under Occupied Report For University Month 2 RSL.csv
         * 15 2014 15 Under Occupied Report For University Month 3 RSL.csv
         * 16 2014 15 Under Occupied Report For University Month 4 RSL.csv
         * 17 2014 15 Under Occupied Report For University Month 5 RSL.csv
         * 18 2014 15 Under Occupied Report For University Month 6 RSL.csv
         * 19 2014 15 Under Occupied Report For University Month 7 RSL.csv
         * 20 2014 15 Under Occupied Report For University Month 8 RSL.csv
         * 21 2014 15 Under Occupied Report For University Month 9 RSL.csv
         * 22 2015 16 Under Occupied Report For University Month 1 RSL.csv
         * 23 2015 16 Under Occupied Report For University Month 2 RSL.csv
         * 24 2015 16 Under Occupied Report For University Month 3 RSL.csv
         * 25 2015 16 Under Occupied Report For University Month 4 RSL.csv*
         * 26 2015 16 Under Occupied Report For University Month 5 RSL.csv*
         * 27 2015 16 Under Occupied Report For University Month 6 RSL.csv*
         * 28 2015 16 Under Occupied Report For University Month 7 RSL.csv*
         */
        String filename;
        filename = "2014 15 Under Occupied Report For University Month 7 Council Tenants.csv";
            DW_UnderOccupiedReport_Set set = new DW_UnderOccupiedReport_Set(
                    filename);
            
    }
    
    
    /**
     * @param directory
     * @param filename
     * @return
     */
    public static TreeMap<String, DW_UOReport_Record> loadInputData(
            File directory,
            String filename) {
//        String type;
//        if (filename.contains("RSL")) {
//            type = "RSL";
//        } else {
//            type = "Council";
//        }
        TreeMap<String, DW_UOReport_Record> result;
        result = new TreeMap<String, DW_UOReport_Record>();
        File inputFile = new File(
                directory,
                filename);
        try {
            BufferedReader br = Generic_StaticIO.getBufferedReader(inputFile);
            StreamTokenizer st
                    = new StreamTokenizer(br);
            Generic_StaticIO.setStreamTokenizerSyntax5(st);
            st.wordChars('`', '`');
            st.wordChars('*', '*');
            String line = "";
            //int duplicateEntriesCount = 0;
            int replacementEntriesCount = 0;
            long RecordID = 0;
            // Read firstline and check format
            int tokenType;
            tokenType = st.nextToken();
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
                            DW_UOReport_Record aUnderOccupiedReport_Record;
                            aUnderOccupiedReport_Record = new DW_UOReport_Record(
                                    RecordID, line, fieldnames);
                                    //RecordID, line, type);
                            Object o = result.put(
                                    aUnderOccupiedReport_Record.getClaimReferenceNumber(),
                                    aUnderOccupiedReport_Record);
                            if (o != null) {
                                DW_UOReport_Record existingUnderOccupiedReport_Record;
                                existingUnderOccupiedReport_Record = (DW_UOReport_Record) o;
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
            Logger.getLogger(DW_UnderOccupiedReport_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Loads the Under-Occupied report data for Leeds.
     *
     * @return Object[] result where: result[0] = councilSets
     * {@code TreeMap<String, DW_UnderOccupiedReport_Set>}; result[1] = RSLSets
     * {@code TreeMap<String, DW_UnderOccupiedReport_Set>}
     */
    public static Object[] loadUnderOccupiedReportData() {
        Object[] result;
        result = new Object[2];
        TreeMap<String, DW_UnderOccupiedReport_Set> councilSets;
        councilSets = new TreeMap<String, DW_UnderOccupiedReport_Set>();
        TreeMap<String, DW_UnderOccupiedReport_Set> RSLSets;
        RSLSets = new TreeMap<String, DW_UnderOccupiedReport_Set>();
        result[0] = councilSets;
        result[1] = RSLSets;
        Object[] filenames = getFilenames();
        TreeMap<String, String> councilFilenames;
        councilFilenames = (TreeMap<String, String>) filenames[0];
        TreeMap<String, String> RSLFilenames;
        RSLFilenames = (TreeMap<String, String>) filenames[1];
        Iterator<String> ite;
        ite = councilFilenames.keySet().iterator();
        while (ite.hasNext()) {
            String year_Month = ite.next();
            String filename = councilFilenames.get(year_Month);
            DW_UnderOccupiedReport_Set set = new DW_UnderOccupiedReport_Set(
                    filename);
            councilSets.put(year_Month, set);
        }
        ite = RSLFilenames.keySet().iterator();
        while (ite.hasNext()) {
            String year_Month = ite.next();
            String filename = RSLFilenames.get(year_Month);
            DW_UnderOccupiedReport_Set set = new DW_UnderOccupiedReport_Set(
                    filename);
            RSLSets.put(year_Month, set);
        }
        return result;
    }

    /**
     *
     * @return Object[] result where:-------------------------------------------
     * result[0] councilFilenames-----------------------------------------------
     * result[1] registeredSocialLandlordFilenames------------------------------
     */
    public static Object[] getFilenames() {
        Object[] result;
        result = new Object[2];
        TreeMap<String, String> councilFilenames;
        TreeMap<String, String> registeredSocialLandlordFilenames;
        councilFilenames = new TreeMap<String, String>();
        registeredSocialLandlordFilenames = new TreeMap<String, String>();
        result[0] = councilFilenames;
        result[1] = registeredSocialLandlordFilenames;
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
                councilFilenames,
                registeredSocialLandlordFilenames,
                12);
        yearAll = "2014 15";
        putFilenames(
                yearAll,
                underOccupiedReportForUniversityString,
                councilEndFilename,
                RSLEndFilename2,
                councilFilenames,
                registeredSocialLandlordFilenames,
                12);
        yearAll = "2015 16";
        putFilenames(
                yearAll,
                underOccupiedReportForUniversityString,
                councilEndFilename,
                RSLEndFilename2,
                councilFilenames,
                registeredSocialLandlordFilenames,
                7); // This number needs increasing as there are more datasets....
        return result;
        /*
         * 0 2013 14 Under Occupied Report For University Year Start Council Tenants.csv
         * 1 2013 14 Under Occupied Report For University Month 1 Council Tenants.csv
         * 2 2013 14 Under Occupied Report For University Month 2 Council Tenants.csv
         * 3 2013 14 Under Occupied Report For University Month 3 Council Tenants.csv
         * 4 2013 14 Under Occupied Report For University Month 4 Council Tenants.csv
         * 5 2013 14 Under Occupied Report For University Month 5 Council Tenants.csv
         * 6 2013 14 Under Occupied Report For University Month 6 Council Tenants.csv
         * 7 2013 14 Under Occupied Report For University Month 7 Council Tenants.csv
         * 8 2013 14 Under Occupied Report For University Month 8 Council Tenants.csv
         * 9 2013 14 Under Occupied Report For University Month 9 Council Tenants.csv
         * 10 2013 14 Under Occupied Report For University Month 10 Council Tenants.csv
         * 11 2013 14 Under Occupied Report For University Month 11 Council Tenants.csv
         * 12 2013 14 Under Occupied Report For University Month 12 Council Tenants.csv
         * 13 2014 15 Under Occupied Report For University Month 1 Council Tenants.csv
         * 14 2014 15 Under Occupied Report For University Month 2 Council Tenants.csv
         * 15 2014 15 Under Occupied Report For University Month 3 Council Tenants.csv
         * 16 2014 15 Under Occupied Report For University Month 4 Council Tenants.csv
         * 17 2014 15 Under Occupied Report For University Month 5 Council Tenants.csv
         * 18 2014 15 Under Occupied Report For University Month 6 Council Tenants.csv
         * 19 2014 15 Under Occupied Report For University Month 7 Council Tenants.csv
         * 20 2014 15 Under Occupied Report For University Month 8 Council Tenants.csv
         * 21 2014 15 Under Occupied Report For University Month 9 Council Tenants.csv
         * 22 2015 16 Under Occupied Report For University Month 1 Council Tenants.csv
         * 23 2015 16 Under Occupied Report For University Month 2 Council Tenants.csv
         * 24 2015 16 Under Occupied Report For University Month 3 Council Tenants.csv
         * 25 2015 16 Under Occupied Report For University Month 4 Council Tenants.csv
         * 26 2015 16 Under Occupied Report For University Month 5 Council Tenants.csv
         * 27 2015 16 Under Occupied Report For University Month 6 Council Tenants.csv
         * 28 2015 16 Under Occupied Report For University Month 7 Council Tenants.csv
         *
         * 0 2013 14 Under Occupied Report For University Year Start RSLs.csv
         * 1 2013 14 Under Occupied Report For University Month 1 RSLs.csv
         * 2 2013 14 Under Occupied Report For University Month 2 RSLs.csv
         * 3 2013 14 Under Occupied Report For University Month 3 RSLs.csv
         * 4 2013 14 Under Occupied Report For University Month 4 RSLs.csv
         * 5 2013 14 Under Occupied Report For University Month 5 RSLs.csv
         * 6 2013 14 Under Occupied Report For University Month 6 RSLs.csv
         * 7 2013 14 Under Occupied Report For University Month 7 RSLs.csv
         * 8 2013 14 Under Occupied Report For University Month 8 RSLs.csv
         * 9 2013 14 Under Occupied Report For University Month 9 RSLs.csv
         * 10 2013 14 Under Occupied Report For University Month 10 RSLs.csv
         * 11 2013 14 Under Occupied Report For University Month 11 RSLs.csv
         * 12 2013 14 Under Occupied Report For University Month 12 RSLs.csv
         * 13 2014 15 Under Occupied Report For University Month 1 RSL.csv
         * 14 2014 15 Under Occupied Report For University Month 2 RSL.csv
         * 15 2014 15 Under Occupied Report For University Month 3 RSL.csv
         * 16 2014 15 Under Occupied Report For University Month 4 RSL.csv
         * 17 2014 15 Under Occupied Report For University Month 5 RSL.csv
         * 18 2014 15 Under Occupied Report For University Month 6 RSL.csv
         * 19 2014 15 Under Occupied Report For University Month 7 RSL.csv
         * 20 2014 15 Under Occupied Report For University Month 8 RSL.csv
         * 21 2014 15 Under Occupied Report For University Month 9 RSL.csv
         * 22 2015 16 Under Occupied Report For University Month 1 RSL.csv
         * 23 2015 16 Under Occupied Report For University Month 2 RSL.csv
         * 24 2015 16 Under Occupied Report For University Month 3 RSL.csv
         * 25 2015 16 Under Occupied Report For University Month 4 RSL.csv*
         * 26 2015 16 Under Occupied Report For University Month 5 RSL.csv*
         * 27 2015 16 Under Occupied Report For University Month 6 RSL.csv*
         * 28 2015 16 Under Occupied Report For University Month 7 RSL.csv*
         */
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

}
