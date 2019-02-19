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
import uk.ac.leeds.ccg.andyt.generic.util.Generic_Time;
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
    private final HashMap<SHBE_ID, String> ClaimIDToClaimRefLookup;
    private final HashMap<String, SHBE_ID> ClaimRefToClaimIDLookup;

    private HashSet<String> RecordTypes;

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
     * Loads and returns the data from the specified input file.
     *
     * @param dir Directory data are loaded from.
     * @param fn Name of file from which data are loaded.
     * @return The loaded data.
     */
    public HashMap<SHBE_ID, DW_UO_Record> loadInputData(File dir, String fn) {
        String methodName;
        methodName = "loadInputData(File,String)";
        env.ge.log("<" + methodName + ">", true);
        HashMap<SHBE_ID, DW_UO_Record> r = new HashMap<>();
        File inputFile = new File(dir, fn);
        boolean addedNewClaimIDs;
        addedNewClaimIDs = false;
        String uorr = " Under-Occupied Report Record ";
        try (BufferedReader br = Generic_IO.getBufferedReader(inputFile)) {
            StreamTokenizer st = new StreamTokenizer(br);
            Generic_IO.setStreamTokenizerSyntax5(st);
            st.wordChars('`', '`');
            st.wordChars('*', '*');
            String line;
            //int duplicateEntriesCount = 0;
            int replacementEntriesCount = 0;
            long recID = 0;
            // Read firstline and check format
            int tt; // Token type.
            st.nextToken();
            line = st.sval;
            String[] fieldnames = line.split(",");
            // Read data
            tt = st.nextToken();
            while (tt != StreamTokenizer.TT_EOF) {
                switch (tt) {
                    case StreamTokenizer.TT_EOL:
                        //System.out.println(line);
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        try {
                            DW_UO_Record rec;
                            rec = new DW_UO_Record(recID, line, fieldnames);
                            String claimRef;
                            claimRef = rec.getClaimRef();
                            SHBE_ID claimID;
                            claimID = ClaimRefToClaimIDLookup.get(claimRef);
                            if (claimID == null) {
                                claimID = new SHBE_ID(
                                        ClaimRefToClaimIDLookup.size());
                                ClaimRefToClaimIDLookup.put(claimRef, claimID);
                                ClaimIDToClaimRefLookup.put(claimID, claimRef);
                                addedNewClaimIDs = true;
                            }
                            Object o = r.put(claimID, rec);
                            if (o != null) {
                                // Existing Under-Occupied report record.
                                DW_UO_Record euorr;
                                euorr = (DW_UO_Record) o;
                                if (!euorr.equals(rec)) {
                                    env.ge.log("Existing" + uorr + euorr);
                                    env.ge.log("Replacement" + uorr + rec);
                                    env.ge.log("Record ID " + recID);
                                    replacementEntriesCount++;
                                }
                            }
                        } catch (Exception e) {
                            env.ge.log("Error processing from file "
                                    + inputFile);
                            env.ge.log(line);
                            env.ge.log("RecordID " + recID);
                            env.ge.log(e.getLocalizedMessage());
                        }
                        recID++;
                        break;
                }
                tt = st.nextToken();
            }
            env.ge.log("Replacement Entries " + replacementEntriesCount);
        } catch (IOException ex) {
            Logger.getLogger(DW_UO_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (addedNewClaimIDs) {
            Generic_IO.writeObject(ClaimIDToClaimRefLookup,
                    env.getSHBE_Handler().getClaimIDToClaimRefLookupFile());
            Generic_IO.writeObject(ClaimRefToClaimIDLookup,
                    env.getSHBE_Handler().getClaimRefToClaimIDLookupFile());
        }
        env.ge.log("</" + methodName + ">", true);
        return r;
    }

    /**
     * Loads the Under-Occupied report data for Leeds.
     *
     * @param reload Iff true then the data is reloaded from source.
     * @return The loaded data.
     */
    public DW_UO_Data loadUnderOccupiedReportData(boolean reload) {
        String methodName;
        methodName = "loadUnderOccupiedReportData(boolean)";
        env.ge.log("<" + methodName + ">");
        DW_UO_Data r;
        TreeMap<ONSPD_YM3, DW_UO_Set> sc = new TreeMap<>();
        TreeMap<ONSPD_YM3, DW_UO_Set> sr = new TreeMap<>();

        /**
         * Look where the generated data should be stored. Look where the input
         * data are. Are there new files to load? If so, load them from source.
         * If not, then load and return the cached object.
         */
        String type;
        Object[] filenames = getInputFilenames();
        // Filenames for Council data.
        TreeMap<ONSPD_YM3, String> fc;
        fc = (TreeMap<ONSPD_YM3, String>) filenames[0];
        // Filenames for RSL data.
        TreeMap<ONSPD_YM3, String> fr;
        fr = (TreeMap<ONSPD_YM3, String>) filenames[1];
        ONSPD_YM3 ym3;
        DW_UO_Set set;
        String filename;
        Iterator<ONSPD_YM3> ite;
        ite = fc.keySet().iterator();
        type = strings.sCouncil;
        while (ite.hasNext()) {
            ym3 = ite.next();
            filename = fc.get(ym3);
            set = new DW_UO_Set(env, type, filename, ym3, reload);
            sc.put(ym3, set);
        }
        ite = fr.keySet().iterator();
        type = strings.sRSL;
        while (ite.hasNext()) {
            ym3 = ite.next();
            filename = fr.get(ym3);
            set = new DW_UO_Set(env, type, filename, ym3, reload);
            sr.put(ym3, set);
        }
        r = new DW_UO_Data(env, sr, sc);
        env.ge.log("</" + methodName + ">");
        return r;
    }

    /**
     * @return The number of input files of Under-Occupied Data
     */
    public int getNumberOfInputFiles() {
        return files.getInputUnderOccupiedDir().listFiles().length;
    }

    /**
     * @return The number of generated files of Under-Occupied Data
     */
    public int getNumberOfGeneratedFiles() {
        return files.getGeneratedUnderOccupiedDir().listFiles().length;
    }

    /**
     * This needs modifying as more data are added.
     *
     * @return Object[] r where:
     * <ul>
     * <li>r[0] = {@code TreeMap<String, String>} of filenames of Council data;
     * where the keys are {@link ONSPD_YM3} and the values are the
     * filenames.</li>
     * <li>r[1] = {@code TreeMap<String, String>} of filenames of RSL data;
     * where the keys are {@link ONSPD_YM3} and the values are the
     * filenames.</li>
     * }
     * </ul>
     */
    public Object[] getInputFilenames() {
        Object[] r;
        r = new Object[2];
        // Collection of filenames of Council data.
        TreeMap<ONSPD_YM3, String> fc;
        // Collection of filenames of Council data.
        TreeMap<ONSPD_YM3, String> fr;
        fc = new TreeMap<>();
        fr = new TreeMap<>();
        r[0] = fc;
        r[1] = fr;
        String yearAll = "2013 14";
        String uorfu = " Under Occupied Report For University ";
        String efc = " Council Tenants.csv";
        String efr = " RSLs.csv";
        String efr2 = " RSL.csv";
        ONSPD_YM3 ym3 = new ONSPD_YM3(2013, 3);
        fc.put(ym3, yearAll + uorfu + "Year Start" + efc);
        fr.put(ym3, yearAll + uorfu + "Year Start" + efr);
        putFilenames(yearAll, uorfu, efc, efr, fc, fr, 1, 12);
        yearAll = "2014 15";
        putFilenames(yearAll, uorfu, efc, efr2, fc, fr, 1, 12);
        yearAll = "2015 16";
        putFilenames(yearAll, uorfu, efc, efr2, fc, fr, 1, 12);
        yearAll = "2016 17";
        putFilenames(yearAll, uorfu, efc, efr2, fc, fr, 1, 12);
        yearAll = "2017 18";
        putFilenames(yearAll, uorfu, efc, efr2, fc, fr, 1, 9); // The number 9 needs increasing more data are added....
        return r;
    }

    /**
     * @param yearAll The financial year (e.g. "2014 15"). This starts in March.
     * @param uorfu Expecting " Under Occupied Report For University ".
     * @param efc End of filename for Council data.
     * @param efr End of filename for RSL data.
     * @param fc Collection of filenames of Council data that is added to.
     * @param fr Collection of filenames of RSL data that is added to.
     * @param minMonth The first month to be added.
     * @param maxMonth The last month to be added.
     */
    private static void putFilenames(String yearAll, String uorfu, String efc,
            String efr, TreeMap<ONSPD_YM3, String> fc,
            TreeMap<ONSPD_YM3, String> fr, int minMonth, int maxMonth) {
        for (int i = minMonth; i <= maxMonth; i++) {
            String s = yearAll + uorfu + "Month " + i;
            ONSPD_YM3 ym3;
            ym3 = new ONSPD_YM3(Integer.valueOf(getYear(yearAll, i)),
                    Generic_Time.getMonthInt(getMonth3(i)));
            fc.put(ym3, s + efc);
            fr.put(ym3, s + efr);
        }
    }

    /**
     * @param yearAll The financial year (e.g. "2014 15"). This starts in March.
     * @param i The month of the financial year. (1 is April, 2 is May,..., 12
     * is March).
     * @return The calendar year.
     */
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

    /**
     * The first month of this year sequence is April (financial year).
     *
     * @param i The month of the financial year. (1 is April, 2 is May,..., 12
     * is March).
     *
     * @return The full name of the month (e.g. "April").
     */
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

    /**
     * The first month of this year sequence is April (financial year).
     *
     * @param i The month of the financial year. (1 is April, 2 is May,..., 12
     * is March).
     *
     * @return The 3 letter name of the month (e.g. "Apr").
     */
    protected static String getMonth3(int i) {
        return getMonth(i).substring(0, 3);
    }

    /**
     * @return A {@code Set<SHBE_ID>} of the ClaimIDs of those UnderOccupying at
     * the start of April2013.
     *
     * @param d The Under-Occupied Data.
     *
     */
    public Set<SHBE_ID> getUOStartApril2013ClaimIDs(DW_UO_Data d) {
        return d.getClaimIDsInUO().get(d.getBaselineYM3());
    }

    /**
     * @return A {@code Set<SHBE_ID>} of the ClaimIDs of all Claims that have at
     * some time been classed as Council Under Occupying.
     *
     * @param d The Under-Occupied Data.
     */
    public Set<SHBE_ID> getAllCouncilUOClaimIDs(DW_UO_Data d) {
        return d.getClaimIDsInCouncilUO();
    }

    /**
     * @param rec The Under-Occupied record.
     * @return The Household size excluding partners
     * {@code 1 + rec.getTotalDependentChildren() + rec.getNonDependents()}
     */
    public static int getHouseholdSizeExcludingPartners(DW_UO_Record rec) {
        int r;
        r = 1 + rec.getTotalDependentChildren()
                //rec.getChildrenOver16()
                //+ rec.getFemaleChildren10to16() + rec.getFemaleChildrenUnder10()
                //+ rec.getMaleChildren10to16() + rec.getMaleChildrenUnder10()
                + rec.getNonDependents();
        return r;
    }

}
