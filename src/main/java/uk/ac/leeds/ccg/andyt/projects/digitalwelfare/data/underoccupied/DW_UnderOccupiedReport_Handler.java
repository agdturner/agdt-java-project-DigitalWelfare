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
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

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
     * HashSet<SHBE_DataRecord>
     *
     * @param directory
     * @param filename
     * @param pw
     * @return
     */
    public TreeMap<String, DW_UnderOccupiedReport_Record> loadInputData(
            File directory,
            String filename) {
        TreeMap<String, DW_UnderOccupiedReport_Record> result;
        result = new TreeMap<String, DW_UnderOccupiedReport_Record>();
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
            //int type = readAndCheckFirstLine(directory, filename);
            // Skip the first line
            Generic_StaticIO.skipline(st);
            // Read data
            int tokenType;
            tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        //System.out.println(line);
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        try {
                            DW_UnderOccupiedReport_Record aUnderOccupiedReport_Record;
                            aUnderOccupiedReport_Record = new DW_UnderOccupiedReport_Record(
                                    RecordID, line, this);
                            Object o = result.put(
                                    aUnderOccupiedReport_Record.getClaimReferenceNumber(),
                                    aUnderOccupiedReport_Record);
                            if (o != null) {
                                DW_UnderOccupiedReport_Record existingUnderOccupiedReport_Record;
                                existingUnderOccupiedReport_Record = (DW_UnderOccupiedReport_Record) o;
                                if (!existingUnderOccupiedReport_Record.equals(aUnderOccupiedReport_Record)) {
                                    System.out.println("existingUnderOccupiedReport_DataRecord " + existingUnderOccupiedReport_Record);
                                    System.out.println("replacementUnderOccupiedReport_DataRecord " + aUnderOccupiedReport_Record);
                                    System.out.println("RecordID " + RecordID);
                                    replacementEntriesCount++;
                                }
                            }
                        } catch (Exception e) {
                            System.err.println(line);
                            System.err.println("RecordID " + RecordID);
                            System.err.println(e.getLocalizedMessage());
                        }
                        RecordID++;
                        break;
                }
                tokenType = st.nextToken();
            }
            System.out.println("replacementEntriesCount " + replacementEntriesCount);
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(DW_UnderOccupiedReport_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Loads the Under-Occupied report data for Leeds.
     *
     * @return ArrayList<DW_UnderOccupiedReport_Set>[] result where:------------
     * result[0] = councilSets;-------------------------------------------------
     * result[1] = RSLSets;-----------------------------------------------------
     */
    public ArrayList<DW_UnderOccupiedReport_Set>[] loadUnderOccupiedReportData() {
        ArrayList<DW_UnderOccupiedReport_Set>[] result;
        result = new ArrayList[2];
        ArrayList<DW_UnderOccupiedReport_Set> councilSets;
        councilSets = new ArrayList<DW_UnderOccupiedReport_Set>();
        ArrayList<DW_UnderOccupiedReport_Set> RSLSets;
        RSLSets = new ArrayList<DW_UnderOccupiedReport_Set>();
        result[0] = councilSets;
        result[1] = RSLSets;
        ArrayList<String>[] filenames = getFilenames();
        ArrayList<String> councilFilenames = filenames[0];
        ArrayList<String> RSLFilenames = filenames[1];
        Iterator<String> ite;
        ite = councilFilenames.iterator();
        while (ite.hasNext()) {
            String filename = ite.next();
            DW_UnderOccupiedReport_Set set = new DW_UnderOccupiedReport_Set(filename, this);
            councilSets.add(set);
        }
        ite = RSLFilenames.iterator();
        while (ite.hasNext()) {
            String filename = ite.next();
            DW_UnderOccupiedReport_Set set = new DW_UnderOccupiedReport_Set(filename, this);
            RSLSets.add(set);
        }
        return result;
    }
//    /**
//     * This will return a collation of all ClaimReferenceNumbers in the dataset 
//     * as a HashMap
//     */
//    public HashMap getRecordIDHashMap(TreeMap<String, DW_UnderOccupiedReport_Record> data) {
//        HashMap result;
//        Iterator ite = data.keySet();
//        
//        
//        return result;
//    }

    /**
     *
     * @return ArrayList<String>[] result where:--------------------------------
     * result[0] councilFilenames-----------------------------------------------
     * result[1] registeredSocialLandlordFilenames------------------------------
     */
    public static ArrayList<String>[] getFilenames() {
        ArrayList<String>[] result;
        result = new ArrayList[2];
        ArrayList<String> councilFilenames;
        ArrayList<String> registeredSocialLandlordFilenames;
        councilFilenames = new ArrayList<String>();
        registeredSocialLandlordFilenames = new ArrayList<String>();
        result[0] = councilFilenames;
        result[1] = registeredSocialLandlordFilenames;
        String councilEndFilename = " Council Tenants.csv";
        String RSLEndFilename = " RSLs.csv";
        String RSLEndFilename2 = " RSL.csv";
        String underOccupiedReportForUniversityString = " Under Occupied Report For University ";
        String year;
        year = "2013 14";
        councilFilenames.add(year + underOccupiedReportForUniversityString + "Year Start" + councilEndFilename);
        registeredSocialLandlordFilenames.add(year + underOccupiedReportForUniversityString + "Year Start" + RSLEndFilename);
        for (int i = 1; i < 12; i++) {
            councilFilenames.add(year + underOccupiedReportForUniversityString + "Month " + i + councilEndFilename);
            registeredSocialLandlordFilenames.add(year + underOccupiedReportForUniversityString + "Month " + i + RSLEndFilename);
        }
        year = "2014 15";
        for (int i = 1; i < 12; i++) {
            councilFilenames.add(year + underOccupiedReportForUniversityString + "Month " + i + councilEndFilename);
            registeredSocialLandlordFilenames.add(year + underOccupiedReportForUniversityString + "Month " + i + RSLEndFilename2);
        }
        year = "2015 16";
        for (int i = 1; i < 2; i++) {
            councilFilenames.add(year + underOccupiedReportForUniversityString + "Month " + i + councilEndFilename);
            registeredSocialLandlordFilenames.add(year + underOccupiedReportForUniversityString + "Month " + i + RSLEndFilename2);
        }
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
         */
    }
}
