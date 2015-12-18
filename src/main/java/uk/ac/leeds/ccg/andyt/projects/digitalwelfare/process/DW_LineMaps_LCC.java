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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process;

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Maps;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.TreeSetFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
//import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Geotools;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Geotools;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Point;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Shapefile;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_AreaCodesAndShapefiles;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Geotools;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Shapefile;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Style;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.data.generated.DW_Table;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.charts.DW_LineGraph;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_StyleParameters;

/**
 *
 * @author geoagdt
 */
public class DW_LineMaps_LCC extends DW_Maps {

    private static final String targetPropertyNameLSOA = "LSOA11CD";
//    private DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile;
//    private DW_AreaCodesAndShapefiles tMSOACodesAndLeedsMSOAShapefile;
    protected ArrayList<AGDT_Shapefile> midgrounds;
    protected ArrayList<AGDT_Shapefile> foregrounds;

    DW_StyleParameters styleParameters;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //new DW_LineMaps_LCC().run2();
            new DW_LineMaps_LCC().run();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
//            StackTraceElement[] stes = e.getStackTrace();
//            for (StackTraceElement ste : stes) {
//                System.err.println(ste.toString());
//            }
        } catch (Error e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
//            StackTraceElement[] stes = e.getStackTrace();
//            for (StackTraceElement ste : stes) {
//                System.err.println(ste.toString());
//            }
        }
    }

    protected static HashMap<Boolean, ArrayList<ArrayList<String>>> getAllTenancyTypeChanges() {
        HashMap<Boolean, ArrayList<ArrayList<String>>> result;
        result = new HashMap<Boolean, ArrayList<ArrayList<String>>>();
        boolean doUnderOccupancy;
        ArrayList<ArrayList<String>> attc;
        doUnderOccupancy = true;
        attc = getAllTenancyTypeChanges(doUnderOccupancy);
        result.put(doUnderOccupancy, attc);
        doUnderOccupancy = false;
        attc = getAllTenancyTypeChanges(doUnderOccupancy);
        result.put(doUnderOccupancy, attc);
        return result;
    }

    protected static ArrayList<ArrayList<String>> getAllTenancyTypeChanges(
            boolean doUnderOccupancy) {
        ArrayList<ArrayList<String>> result;
        result = new ArrayList<ArrayList<String>>();
        ArrayList<String> tenancyTypeChanges;
        tenancyTypeChanges = new ArrayList<String>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("1 - 1UO");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<String>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("1UO - 1");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<String>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("4 - 4UO");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<String>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("4UO - 4");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<String>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("1 - 4UO");
            tenancyTypeChanges.add("1UO - 4");
            tenancyTypeChanges.add("1UO - 4UO");
        } else {
            tenancyTypeChanges.add("1 - 4");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<String>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("4 - 1UO");
            tenancyTypeChanges.add("4UO - 1");
            tenancyTypeChanges.add("4UO - 1UO");
        } else {
            tenancyTypeChanges.add("4 - 1");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<String>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("1UO - 3");
            tenancyTypeChanges.add("1 - 3UO");
            tenancyTypeChanges.add("1UO - 3UO");
        } else {
            tenancyTypeChanges.add("1 - 3");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<String>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("4UO - 3");
            tenancyTypeChanges.add("4 - 3UO");
            tenancyTypeChanges.add("4UO - 3UO");
        } else {
            tenancyTypeChanges.add("4 - 3");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<String>();
        if (doUnderOccupancy) {
            //tenancyTypeChanges.add("1UO - 1UO");
            tenancyTypeChanges.add("1UO - 3");
            tenancyTypeChanges.add("1 - 3UO");
            tenancyTypeChanges.add("1UO - 3UO");
            tenancyTypeChanges.add("1UO - 3");
            tenancyTypeChanges.add("1 - 3UO");
            tenancyTypeChanges.add("1UO - 3UO");
            tenancyTypeChanges.add("2UO - 3");
            tenancyTypeChanges.add("2 - 3UO");
            tenancyTypeChanges.add("2UO - 3UO");
            tenancyTypeChanges.add("2UO - 3");
            tenancyTypeChanges.add("2 - 3UO");
            tenancyTypeChanges.add("2UO - 3UO");
            tenancyTypeChanges.add("4UO - 3");
            tenancyTypeChanges.add("4 - 3UO");
            tenancyTypeChanges.add("4UO - 3UO");
            tenancyTypeChanges.add("4UO - 3");
            tenancyTypeChanges.add("4 - 3UO");
            tenancyTypeChanges.add("4UO - 3UO");
        } else {
            tenancyTypeChanges.add("1 - 3");
            tenancyTypeChanges.add("2 - 3");
            tenancyTypeChanges.add("4 - 3");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<String>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("3UO - 1");
            tenancyTypeChanges.add("3 - 1UO");
            tenancyTypeChanges.add("3UO - 1UO");
        } else {
            tenancyTypeChanges.add("3 - 1");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<String>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("3UO - 4");
            tenancyTypeChanges.add("3 - 4UO");
            tenancyTypeChanges.add("3UO - 4UO");
        } else {
            tenancyTypeChanges.add("3 - 4");
        }
        result.add(tenancyTypeChanges);
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<String>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("3UO - 1");
            tenancyTypeChanges.add("3 - 1UO");
            tenancyTypeChanges.add("3UO - 1UO");
            tenancyTypeChanges.add("3UO - 2");
            tenancyTypeChanges.add("3 - 2UO");
            tenancyTypeChanges.add("3UO - 2UO");
            tenancyTypeChanges.add("3UO - 4");
            tenancyTypeChanges.add("3 - 4UO");
            tenancyTypeChanges.add("3UO - 4UO");
        } else {
            tenancyTypeChanges.add("3 - 1");
            tenancyTypeChanges.add("3 - 2");
            tenancyTypeChanges.add("3 - 4");
        }
        result.add(tenancyTypeChanges);
        return result;
    }

    protected static HashMap<Boolean, ArrayList<ArrayList<String>>> getAllTenancyTypeChangesGrouped() {
        HashMap<Boolean, ArrayList<ArrayList<String>>> result;
        result = new HashMap<Boolean, ArrayList<ArrayList<String>>>();
        boolean doUnderOccupancy;
        ArrayList<ArrayList<String>> attc;
        doUnderOccupancy = true;
        attc = getAllTenancyTypeChangesGrouped(doUnderOccupancy);
        result.put(doUnderOccupancy, attc);
        doUnderOccupancy = false;
        attc = getAllTenancyTypeChangesGrouped(doUnderOccupancy);
        result.put(doUnderOccupancy, attc);
        return result;
    }

    protected static ArrayList<ArrayList<String>> getAllTenancyTypeChangesGrouped(
            boolean doUnderOccupancy) {
        ArrayList<ArrayList<String>> result;
        result = new ArrayList<ArrayList<String>>();
        ArrayList<String> tenancyTypeChanges;
        tenancyTypeChanges = new ArrayList<String>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("Regulated - RegulatedUO");
            tenancyTypeChanges.add("RegulatedUO - Regulated");
        } else {
            tenancyTypeChanges.add("Regulated - Regulated");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<String>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("UnregulatedUO - Unregulated");
            tenancyTypeChanges.add("Unregulated - UnregulatedUO");
        } else {
            tenancyTypeChanges.add("Unregulated - Unregulated");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<String>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("RegulatedUO - Unregulated");
            tenancyTypeChanges.add("Regulated - UnregulatedUO");
        } else {
            tenancyTypeChanges.add("Regulated - Unregulated");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<String>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("UnregulatedUO - Regulated");
            tenancyTypeChanges.add("Unregulated - RegulatedUO");
        } else {
            tenancyTypeChanges.add("Unregulated - Regulated");
        }
        result.add(tenancyTypeChanges);
        return result;
    }

    protected static TreeMap<String, ArrayList<String>> getYM3s(
            ArrayList<Integer> includes) {
        TreeMap<String, ArrayList<String>> result;
        result = new TreeMap<String, ArrayList<String>>();
        String[] tSHBEFilenamesAll;
        tSHBEFilenamesAll = DW_SHBE_Handler.getSHBEFilenamesAll();
        ArrayList<String> yMs;
        // All
        Iterator<Integer> includesIte;
        includesIte = includes.iterator();
        yMs = new ArrayList<String>();
        String yM3 = "";
        boolean first = true;
        String name = "";
        int i;
        while (includesIte.hasNext()) {
            i = includesIte.next();
            yM3 = DW_SHBE_Handler.getYM3(tSHBEFilenamesAll[i]);
            if (first) {
                name = yM3;
                first = false;
            }
            yMs.add(yM3);
        }
        name += "_TO_" + yM3;
        //result.put("All", yearMonths1);
        result.put(name, yMs);
        // Individual
        includesIte = includes.iterator();
        i = includesIte.next();
        String yM30 = DW_SHBE_Handler.getYM3(tSHBEFilenamesAll[i]);
        while (includesIte.hasNext()) {
            i = includesIte.next();
            yMs = new ArrayList<String>();
            String yM31;
            yM31 = DW_SHBE_Handler.getYM3(tSHBEFilenamesAll[i]);
            yMs.add(yM30);
            yMs.add(yM31);
//            result.put(yM, yearMonths1);
            result.put(yM30 + "_TO_" + yM31, yMs);
            yM30 = yM31;
        }
        return result;
    }

    public void run2(
            boolean doUnderOccupancy,
            boolean doCouncil) {
        String filename;
        //filename = "PostcodeChanges_Start_2008April_End_2008October.csv";
        File dirIn = new File(
                DW_Files.getOutputSHBETablesDir(),
                "Tenancy");
        dirIn = new File(
                dirIn,
                "All");
        dirIn = new File(
                dirIn,
                DW_Files.sTenancyTypeTransition);
        dirIn = new File(
                dirIn,
                DW_Files.sCheckedPreviousTenure);
        dirIn = new File(
                dirIn,
                DW_Files.sTenancyAndPostcodeChanges);
        if (doUnderOccupancy) {
            dirIn = new File(
                    dirIn,
                    "UnderOccupied");
            if (doCouncil) {
                dirIn = new File(
                        dirIn,
                        DW_Files.sCouncil);
            } else {
                dirIn = new File(
                        dirIn,
                        DW_Files.sRSL);
            }
        } else {
            dirIn = new File(
                    dirIn,
                    "All");
        }
        dirIn = new File(
                dirIn,
                DW_Files.sPostcodeChanged);
        dirIn = new File(
                dirIn,
                "All");
        dirIn = new File(
                dirIn,
                DW_Files.sUngrouped);
        dirIn = new File(
                dirIn,
                DW_Files.sPostcodeChanges);
        dirIn = new File(
                dirIn,
                DW_Files.sCheckedPreviousPostcode);

        ArrayList<Integer> includes;
        //includes = DW_SHBE_Handler.getSHBEFilenameIndexesExcept34();
        includes = DW_SHBE_Handler.getSHBEFilenameIndexes();

        TreeMap<String, ArrayList<String>> yM3s;
        yM3s = getYM3s(includes);

        ArrayList<ArrayList<String>> allTenancyTypeChanges;
        allTenancyTypeChanges = getAllTenancyTypeChanges(doUnderOccupancy);

        Iterator<String> yM3sIte;
        yM3sIte = yM3s.keySet().iterator();
        while (yM3sIte.hasNext()) {
            String name;
            name = yM3sIte.next();
            System.out.println(name);
            ArrayList<String> yMs;
            yMs = yM3s.get(name);
            ArrayList<String> lines;
            lines = new ArrayList<String>();
            Iterator<String> yMsIte;
            yMsIte = yMs.iterator();
            String yM0 = yMsIte.next();
            while (yMsIte.hasNext()) {
                String yM1;
                yM1 = yMsIte.next();
                filename = "PostcodeChanges_Start_" + yM0 + "_End_" + yM1 + ".csv";
                File f;
                f = new File(
                        dirIn,
                        filename);
                //System.out.println("Load " + f);
                if (f.exists()) {
                    ArrayList<String> linesPart;
                    linesPart = DW_Table.readCSV(f);
                    lines.addAll(linesPart);
                    yM0 = yM1;
                } else {
                    System.out.println("File " + f + " does not exist.");
                }
            }
            int size = lines.size();
            System.out.println("Total moves " + Integer.toString(size));

            // Totals for each tenancy type
            Iterator<ArrayList<String>> allTenancyTypeChangesIte;
            allTenancyTypeChangesIte = allTenancyTypeChanges.iterator();
            while (allTenancyTypeChangesIte.hasNext()) {
                ArrayList<String> tenancyTypeChanges;
                tenancyTypeChanges = allTenancyTypeChangesIte.next();
                int moves = 0;
                String tenancyTypeChangesName;
                tenancyTypeChangesName = getName(tenancyTypeChanges);
                Iterator<String> linesIte;
                linesIte = lines.iterator();
                while (linesIte.hasNext()) {
                    String line = linesIte.next();
                    boolean contains;
                    contains = false;
                    Iterator<String> tenancyTypeChangesIte;
                    tenancyTypeChangesIte = tenancyTypeChanges.iterator();
                    while (tenancyTypeChangesIte.hasNext()) {
                        String tenancyTypeChange;
                        tenancyTypeChange = tenancyTypeChangesIte.next();
                        if (line.contains(tenancyTypeChange)) {
                            contains = true;
                        }
                    }
                    if (contains) {
                        moves++;
                    }
                }
                System.out.println("Total " + tenancyTypeChangesName + " moves " + moves);
            }
        }
    }

    public void run() throws Exception {
        // If showMapsInJMapPane == true then resulting maps are displayed on 
        // screen in a JMapPane otherwise maps are only written to file.
//        showMapsInJMapPane = true;
        showMapsInJMapPane = false;
        imageWidth = 1000;
        init();

        ArrayList<String> paymentTypes;
        paymentTypes = DW_SHBE_Handler.getPaymentTypes();
        paymentTypes.remove(DW_SHBE_Handler.sAllPT);
//        paymentTypes.remove(DW_SHBE_Handler.sInPayment);
        paymentTypes.remove(DW_SHBE_Handler.sSuspended);
        paymentTypes.remove(DW_SHBE_Handler.sOtherPT);

        Iterator<String> paymentTypesIte;

        TreeMap<String, ArrayList<Integer>> includes;
        includes = DW_SHBE_Handler.getIncludes();

        HashMap<Boolean, ArrayList<ArrayList<String>>> allTenancyTypeChanges;
        allTenancyTypeChanges = getAllTenancyTypeChanges();

        HashMap<Boolean, ArrayList<ArrayList<String>>> allTenancyTypeChangesGrouped;
        allTenancyTypeChangesGrouped = getAllTenancyTypeChangesGrouped();

        paymentTypesIte = paymentTypes.iterator();
        while (paymentTypesIte.hasNext()) {
            String paymentType;
            paymentType = paymentTypesIte.next();
            Iterator<String> includesIte;
            includesIte = includes.keySet().iterator();
            while (includesIte.hasNext()) {
                String includeName;
                includeName = includesIte.next();
                ArrayList<Integer> include;
                include = includes.get(includeName);

                TreeMap<String, ArrayList<String>> yM3s;
                yM3s = getYM3s(include);

                ArrayList<Boolean> b;
                b = new ArrayList<Boolean>();
                b.add(true);
                b.add(false);
                boolean doTenancyTypeAndPostcodeChange;
                Iterator<Boolean> iteb0;
                iteb0 = b.iterator();
                while (iteb0.hasNext()) {
                    doTenancyTypeAndPostcodeChange = iteb0.next();
                    Iterator<Boolean> iteb;
                    iteb = b.iterator();
                    while (iteb.hasNext()) {
                        boolean doUnderOccupancyData;
                        doUnderOccupancyData = iteb.next();
                        if (doUnderOccupancyData) {
                            boolean doCouncil;
                            doCouncil = true;
                            boolean doRSL;
                            doRSL = true;
                            Iterator<Boolean> iteb1;
                            iteb1 = b.iterator();
                            while (iteb1.hasNext()) {
                                boolean doCheckedPreviousTenure;
                                doCheckedPreviousTenure = iteb1.next();
                                Iterator<Boolean> iteb2;
                                iteb2 = b.iterator();
                                while (iteb2.hasNext()) {
                                    boolean doCheckedPreviousPostcode;
                                    doCheckedPreviousPostcode = iteb2.next();
                                    Iterator<Boolean> iteb3;
                                    iteb3 = b.iterator();
                                    while (iteb3.hasNext()) {
                                        boolean grouped;
                                        grouped = iteb3.next();
                                        if (grouped) {
                                            postcodeMaps(
                                                    paymentType,
                                                    yM3s,
                                                    includeName,
                                                    include,
                                                    allTenancyTypeChangesGrouped.get(doUnderOccupancyData),
                                                    doTenancyTypeAndPostcodeChange,
                                                    doUnderOccupancyData,
                                                    doCouncil,
                                                    doRSL,
                                                    doCheckedPreviousTenure,
                                                    doCheckedPreviousPostcode,
                                                    grouped);
                                        } else {
                                            postcodeMaps(
                                                    paymentType,
                                                    yM3s,
                                                    includeName,
                                                    include,
                                                    allTenancyTypeChanges.get(doUnderOccupancyData),
                                                    doTenancyTypeAndPostcodeChange,
                                                    doUnderOccupancyData,
                                                    doCouncil,
                                                    doRSL,
                                                    doCheckedPreviousTenure,
                                                    doCheckedPreviousPostcode,
                                                    grouped);
                                        }
                                    }
                                }
                            }
                            iteb1 = b.iterator();
                            while (iteb1.hasNext()) {
                                doCouncil = iteb1.next();
                                Iterator<Boolean> iteb2;
                                iteb2 = b.iterator();
                                while (iteb2.hasNext()) {
                                    boolean doCheckedPreviousTenure;
                                    doCheckedPreviousTenure = iteb2.next();
                                    Iterator<Boolean> iteb3;
                                    iteb3 = b.iterator();
                                    while (iteb3.hasNext()) {
                                        boolean doCheckedPreviousPostcode;
                                        doCheckedPreviousPostcode = iteb3.next();
                                        Iterator<Boolean> iteb4;
                                        iteb4 = b.iterator();
                                        while (iteb4.hasNext()) {
                                            boolean doGrouped;
                                            doGrouped = iteb4.next();
                                            if (doGrouped) {
                                                postcodeMaps(
                                                        paymentType,
                                                        yM3s,
                                                        includeName,
                                                        include,
                                                        allTenancyTypeChangesGrouped.get(doUnderOccupancyData),
                                                        doTenancyTypeAndPostcodeChange,
                                                        doUnderOccupancyData,
                                                        doCouncil,
                                                        false,
                                                        doCheckedPreviousTenure,
                                                        doCheckedPreviousPostcode,
                                                        doGrouped);
                                            } else {
                                                postcodeMaps(
                                                        paymentType,
                                                        yM3s,
                                                        includeName,
                                                        include,
                                                        allTenancyTypeChanges.get(doUnderOccupancyData),
                                                        doTenancyTypeAndPostcodeChange,
                                                        doUnderOccupancyData,
                                                        doCouncil,
                                                        false,
                                                        doCheckedPreviousTenure,
                                                        doCheckedPreviousPostcode,
                                                        doGrouped);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Iterator<Boolean> iteb1;
                            iteb1 = b.iterator();
                            while (iteb1.hasNext()) {
                                boolean doCheckedPreviousTenure;
                                doCheckedPreviousTenure = iteb1.next();
                                Iterator<Boolean> iteb2;
                                iteb2 = b.iterator();
                                while (iteb2.hasNext()) {
                                    boolean doCheckedPreviousPostcode;
                                    doCheckedPreviousPostcode = iteb2.next();
                                    Iterator<Boolean> iteb3;
                                    iteb3 = b.iterator();
                                    while (iteb3.hasNext()) {
                                        boolean doGrouped;
                                        doGrouped = iteb3.next();
                                        if (doGrouped) {
                                            postcodeMaps(
                                                    paymentType,
                                                    yM3s,
                                                    includeName,
                                                    include,
                                                    allTenancyTypeChangesGrouped.get(doUnderOccupancyData),
                                                    doTenancyTypeAndPostcodeChange,
                                                    doUnderOccupancyData,
                                                    false,
                                                    false,
                                                    doCheckedPreviousTenure,
                                                    doCheckedPreviousPostcode,
                                                    doGrouped);
                                        } else {
                                            postcodeMaps(
                                                    paymentType,
                                                    yM3s,
                                                    includeName,
                                                    include,
                                                    allTenancyTypeChanges.get(doUnderOccupancyData),
                                                    doTenancyTypeAndPostcodeChange,
                                                    doUnderOccupancyData,
                                                    false,
                                                    false,
                                                    doCheckedPreviousTenure,
                                                    doCheckedPreviousPostcode,
                                                    doGrouped);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // Tidy up
        if (!showMapsInJMapPane) {
//            Iterator<AGDT_Shapefile> ite1;
//            ite1 = midgrounds.iterator();
//            while (ite1.hasNext()) {
//                AGDT_Shapefile s = ite1.next();
//                s.dispose();
//            }
            Iterator<AGDT_Shapefile> ite;
            ite = foregrounds.iterator();
            while (ite.hasNext()) {
                ite.next().dispose();
//                tLSOACodesAndLeedsLSOAShapefile.dispose();
//            tMSOACodesAndLeedsMSOAShapefile.dispose();
            }
        }
    }

    private void init() {
        initStyleParameters();
        mapDirectory = DW_Files.getOutputSHBELineMapsDir();
        foregrounds = new ArrayList<AGDT_Shapefile>();
        //midgrounds = new ArrayList<AGDT_Shapefile>();
//        backgrounds = new ArrayList<AGDT_Shapefile>();
        //initLSOACodesAndLeedsLSOAShapefile(targetPropertyNameLSOA);
        DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile = new DW_AreaCodesAndShapefiles(
                "LSOA", targetPropertyNameLSOA, getShapefileDataStoreFactory());
        foregrounds.add(tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile());
//        foregroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
    }

    private void initStyleParameters() {
        styleParameters = new DW_StyleParameters();
        styleParameters.setForegroundStyle(
                DW_Style.createDefaultPolygonStyle(
                        Color.GREEN,
                        Color.WHITE), 0);
        styleParameters.setMidgroundStyle(
                DW_Style.createDefaultLineStyle(Color.LIGHT_GRAY), 0);
    }

    private void setStyleParameters(
            int type,
            Color origin,
            Color destination) {
        if (type == 0) {
            styleParameters.setMidgroundStyle(
                    DW_Style.createDefaultLineStyle(origin), 1);
            styleParameters.setMidgroundStyle(
                    DW_Style.createDefaultLineStyle(destination), 2);
        } else {
            styleParameters.setMidgroundStyle(
                    DW_Style.createDefaultLineStyle(origin), 2);
            styleParameters.setMidgroundStyle(
                    DW_Style.createDefaultLineStyle(destination), 1);

        }
    }

//    private void initLSOACodesAndLeedsLSOAShapefile(
//            String targetPropertyNameLSOA) {
//        tLSOACodesAndLeedsLSOAShapefile = new DW_AreaCodesAndShapefiles(
//                "LSOA", targetPropertyNameLSOA, getShapefileDataStoreFactory());
//    }
    public void postcodeMaps(
            TreeMap<String, ArrayList<String>> yearMonths,
            String includeName,
            ArrayList<Integer> include,
            ArrayList<ArrayList<String>> allTenancyTypeChanges,
            ArrayList<ArrayList<String>> allTenancyTypeChangesGrouped,
            boolean doTenancyTypeAndPostcodeChange,
            boolean doUnderOccupancyData,
            boolean doCouncil,
            boolean doCheckedPreviousTenure,
            boolean doCheckedPreviousPostcode,
            boolean grouped) throws Exception {
        File dirIn;
        File dirOut;
        if (doTenancyTypeAndPostcodeChange) {
            dirIn = new File(
                    DW_Files.getOutputSHBETablesDir(),
                    "Tenancy");
            dirOut = new File(
                    mapDirectory,
                    DW_Files.sTenancyAndPostcodeChanges);
            dirIn = DW_Files.getUOFile(dirIn, doUnderOccupancyData, doCouncil);
            dirOut = DW_Files.getUOFile(dirOut, doUnderOccupancyData, doCouncil);
            dirIn = new File(
                    dirIn,
                    DW_Files.sTenancyTypeTransition);
            if (doCheckedPreviousTenure) {
                dirIn = new File(
                        dirIn,
                        DW_Files.sCheckedPreviousTenure);
                dirOut = new File(
                        dirOut,
                        DW_Files.sCheckedPreviousTenure);
            } else {
                dirIn = new File(
                        dirIn,
                        DW_Files.sNotCheckedPreviousTenure);
                dirOut = new File(
                        dirOut,
                        DW_Files.sNotCheckedPreviousTenure);
            }
            dirIn = new File(
                    dirIn,
                    DW_Files.sTenancyAndPostcodeChanges);
        } else {
            dirIn = new File(
                    mapDirectory,
                    DW_Files.sPostcodeChanges);
            dirOut = new File(
                    mapDirectory,
                    DW_Files.sPostcodeChanges);
            dirIn = DW_Files.getUOFile(dirIn, doUnderOccupancyData, doCouncil);
            dirOut = DW_Files.getUOFile(dirOut, doUnderOccupancyData, doCouncil);
            dirIn = new File(
                    dirIn,
                    DW_Files.sPostcodeChanged);
            dirIn = new File(
                    dirIn,
                    includeName);
            if (grouped) {
                dirIn = new File(
                        dirIn,
                        DW_Files.sGrouped);
                dirOut = new File(
                        dirOut,
                        DW_Files.sGrouped);
            } else {
                dirIn = new File(
                        dirIn,
                        DW_Files.sUngrouped);
                dirOut = new File(
                        dirOut,
                        DW_Files.sUngrouped);
            }
            dirIn = new File(
                    dirIn,
                    DW_Files.sPostcodeChanges);
            if (doCheckedPreviousPostcode) {
                dirIn = new File(
                        dirIn,
                        DW_Files.sCheckedPreviousPostcode);
                dirOut = new File(
                        dirOut,
                        DW_Files.sCheckedPreviousPostcode);
            } else {
                dirIn = new File(
                        dirIn,
                        DW_Files.sNotCheckedPreviousPostcode);
                dirOut = new File(
                        dirOut,
                        DW_Files.sNotCheckedPreviousPostcode);
            }
        }
        //dirOut.mkdirs(); // done later
        Iterator<String> yearMonthsIte;
        yearMonthsIte = yearMonths.keySet().iterator();
        while (yearMonthsIte.hasNext()) {
            String name;
            name = yearMonthsIte.next();

            if (name.equalsIgnoreCase("2013_April_TO_2015_September")) {

                System.out.println(name);
                ArrayList<String> yMs;
                yMs = yearMonths.get(name);
                ArrayList<String> lines;
                lines = new ArrayList<String>();
                Iterator<String> yMsIte;
                yMsIte = yMs.iterator();
                String yM30 = yMsIte.next();
                String yM30Start = yM30;
                String yM30End;
                while (yMsIte.hasNext()) {
                    String yM31;
                    yM31 = yMsIte.next();
                    String filename;
                    filename = "PostcodeChanges_Start_" + yM30 + "_End_" + yM31 + ".csv";
                    File f;
                    f = new File(
                            dirIn,
                            filename);
                    if (f.exists()) {
                        //System.out.println("Load " + f);
                        ArrayList<String> linesPart;
                        linesPart = DW_Table.readCSV(f);
                        lines.addAll(linesPart);
                    }
                    yM30 = yM31;
                }
                yM30End = yM30;
                //System.out.println(filename + " lines " + lines.size());
                //int count = 0;
                if (!lines.isEmpty()) {
                    //name += "_TO_" + split1[0] + "_" + split1[1];
                    File dirOut2;
                    dirOut2 = new File(
                            dirOut,
                            name);
                    // Step 3.
                    SimpleFeatureType aLineSFT = DataUtilities.createType(
                            "LINE",
                            "the_geom:LineString," + "name:String," + "number:Integer");

                    // Totals for each tenancy type
                    Iterator<ArrayList<String>> allTenancyTypeChangesIte;
                    allTenancyTypeChangesIte = allTenancyTypeChanges.iterator();
                    while (allTenancyTypeChangesIte.hasNext()) {
                        ArrayList<String> tenancyTypeChanges;
                        tenancyTypeChanges = allTenancyTypeChangesIte.next();

                        // Initialise a FeatureCollections and SimpleFeatureBuilders
                        HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcSs;
                        tsfcSs = new HashMap<ArrayList<String>, TreeSetFeatureCollection>();
                        HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcMs;
                        tsfcMs = new HashMap<ArrayList<String>, TreeSetFeatureCollection>();
                        HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcEs;
                        tsfcEs = new HashMap<ArrayList<String>, TreeSetFeatureCollection>();

                        HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbSs;
                        sfbSs = new HashMap<ArrayList<String>, SimpleFeatureBuilder>();
                        HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbMs;
                        sfbMs = new HashMap<ArrayList<String>, SimpleFeatureBuilder>();
                        HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbEs;
                        sfbEs = new HashMap<ArrayList<String>, SimpleFeatureBuilder>();

                        // Create SimpleFeatureBuilder
                        //FeatureFactory ff = FactoryFinder.getGeometryFactories();
                        GeometryFactory geometryFactory = new GeometryFactory();

                        long count = 0;
                        long notPCCount = 0;

                        Iterator<String> linesIte;
                        linesIte = lines.iterator();
                        while (linesIte.hasNext()) {
                            String line;
                            line = linesIte.next();
                            String[] lineSplit;
                            lineSplit = line.split(",");
                            //String yM30;
                            yM30 = lineSplit[1].trim();
                            String yM31;
                            yM31 = lineSplit[2].trim();
                            String tenancyTypeChange;
                            String yM30v;
                            yM30v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM30);
                            String yM31v;
                            yM31v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
                            tenancyTypeChange = lineSplit[3].trim();
                            if (tenancyTypeChanges.contains(tenancyTypeChange)) {
                                if (yMs.contains(yM31)) {
                                    String postcode0 = lineSplit[4].trim();
                                    String postcode1 = lineSplit[5].trim();
                                    AGDT_Point origin;
                                    origin = DW_Postcode_Handler.getPointFromPostcode(
                                            yM30v,
                                            DW_Postcode_Handler.TYPE_UNIT,
                                            postcode0);
                                    AGDT_Point destination;
                                    destination = DW_Postcode_Handler.getPointFromPostcode(
                                            yM31v,
                                            DW_Postcode_Handler.TYPE_UNIT,
                                            postcode1);
                                    TreeSetFeatureCollection tsfcS;
                                    tsfcS = tsfcSs.get(tenancyTypeChanges);
                                    if (tsfcS == null) {
                                        tsfcS = new TreeSetFeatureCollection();
                                        tsfcSs.put(tenancyTypeChanges, tsfcS);
                                    }
                                    SimpleFeatureBuilder sfbS;
                                    sfbS = sfbSs.get(tenancyTypeChanges);
                                    if (sfbS == null) {
                                        sfbS = new SimpleFeatureBuilder(aLineSFT);
                                    }
                                    TreeSetFeatureCollection tsfcM;
                                    tsfcM = tsfcMs.get(tenancyTypeChanges);
                                    if (tsfcM == null) {
                                        tsfcM = new TreeSetFeatureCollection();
                                        tsfcMs.put(tenancyTypeChanges, tsfcM);
                                    }
                                    SimpleFeatureBuilder sfbM;
                                    sfbM = sfbMs.get(tenancyTypeChanges);
                                    if (sfbM == null) {
                                        sfbM = new SimpleFeatureBuilder(aLineSFT);
                                    }
                                    TreeSetFeatureCollection tsfcE;
                                    tsfcE = tsfcEs.get(tenancyTypeChanges);
                                    if (tsfcE == null) {
                                        tsfcE = new TreeSetFeatureCollection();
                                        tsfcEs.put(tenancyTypeChanges, tsfcE);
                                    }
                                    SimpleFeatureBuilder sfbE;
                                    sfbE = sfbEs.get(tenancyTypeChanges);
                                    if (sfbE == null) {
                                        sfbE = new SimpleFeatureBuilder(aLineSFT);
                                    }
                                    if (origin != null && destination != null) {

                                        //count ++;
                                        double originx;
                                        double originy;
                                        originx = origin.getX();
                                        originy = origin.getY();
                                        double destinationx;
                                        double destinationy;
                                        destinationx = destination.getX();
                                        destinationy = destination.getY();

                                        double break1x;
                                        double break1y;
                                        break1x = (originx + (originx + ((originx + destinationx) / 2.0d)) / 2.0d) / 2.0d;
                                        break1y = (originy + (originy + ((originy + destinationy) / 2.0d)) / 2.0d) / 2.0d;
//                        break1x = (originx + destinationx) / 2.0d;
//                        break1y = (originy + destinationy) / 2.0d;

                                        double break2x;
                                        double break2y;
                                        break2x = (destinationx + (destinationx + ((originx + destinationx) / 2.0d)) / 2.0d) / 2.0d;
                                        break2y = (destinationy + (destinationy + ((originy + destinationy) / 2.0d)) / 2.0d) / 2.0d;
//                        break2x = (originx + destinationx) / 2.0d;
//                        break2y = (originy + destinationy) / 2.0d;

//                        double midpointx;
//                        double midpointy;
//                        midpointx = (originx + destinationx) / 2.0d;
//                        midpointy = (originy + destinationy) / 2.0d;
                                        Coordinate[] coords;
                                        LineString lineString;
                                        SimpleFeature feature;

//                        coords = new Coordinate[2];
//                        coords[0] = new Coordinate(originx, originy);
//                        coords[1] = new Coordinate(destinationx, destinationy);
//                        lineString = geometryFactory.createLineString(coords);
//                        sfbOM.add(lineString);
////                        // Add attributes to line
////          sfb.add(outlet);
//                        feature = sfbOM.buildFeature(null);
//                        tsfcOM.add(feature);
                                        coords = new Coordinate[2];
                                        coords[0] = new Coordinate(originx, originy);
                                        coords[1] = new Coordinate(break1x, break1y);
                                        lineString = geometryFactory.createLineString(coords);
                                        sfbS.add(lineString);
                                        feature = sfbS.buildFeature(null);
                                        tsfcS.add(feature);

                                        coords = new Coordinate[2];
                                        coords[0] = new Coordinate(break1x, break1y);
                                        coords[1] = new Coordinate(break2x, break2y);
                                        lineString = geometryFactory.createLineString(coords);
                                        sfbM.add(lineString);
                                        // Add attributes to line
//          sfb.add(outlet);
                                        feature = sfbM.buildFeature(null);
                                        tsfcM.add(feature);

                                        coords = new Coordinate[2];
                                        coords[0] = new Coordinate(break2x, break2y);
                                        coords[1] = new Coordinate(destinationx, destinationy);
                                        lineString = geometryFactory.createLineString(coords);
                                        sfbE.add(lineString);
                                        // Add attributes to line
//          sfb.add(outlet);
                                        feature = sfbE.buildFeature(null);
                                        tsfcE.add(feature);
                                        count++;

                                    } else {
                                        notPCCount++;
                                    }
                                }
                            }
                        }
                        String dates;
                        dates = getDatesString(yM30Start, yM30End);
                        String legendMessage;
                        legendMessage = dates + " Count " + count;
                        System.out.println("line count = " + count);
                        System.out.println("Postcode not valid count = " + notPCCount);
                        TreeSetFeatureCollection tsfc;
                        name = getName(tenancyTypeChanges);
                        File dirOut3;
                        dirOut3 = new File(
                                dirOut2,
                                name);
                        String nameM;
                        nameM = name + "M";
                        tsfc = tsfcMs.get(tenancyTypeChanges);
                        if (tsfc != null) {
                            File f0 = AGDT_Geotools.getOutputShapefile(
                                    dirOut3,
                                    nameM);
                            DW_Shapefile.transact(
                                    f0,
                                    aLineSFT,
                                    tsfc,
                                    getShapefileDataStoreFactory());
                            String nameS;
                            nameS = name + "S";
                            tsfc = tsfcSs.get(tenancyTypeChanges);
                            File f1 = AGDT_Geotools.getOutputShapefile(
                                    dirOut3,
                                    nameS);
                            DW_Shapefile.transact(
                                    f1,
                                    aLineSFT,
                                    tsfc,
                                    getShapefileDataStoreFactory());
                            String nameE;
                            nameE = name + "E";
                            tsfc = tsfcEs.get(tenancyTypeChanges);
                            File f2 = AGDT_Geotools.getOutputShapefile(
                                    dirOut3,
                                    nameE);
                            DW_Shapefile.transact(
                                    f2,
                                    aLineSFT,
                                    tsfc,
                                    getShapefileDataStoreFactory());

//            // Had Hoped to getting away with doing this once
//            FeatureLayer backgroundFeatureLayer = DW_Shapefile.getFeatureLayer(
//                backgroundShapefile,
//                backgroundStyle);
                            for (int i = 0; i < 2; i++) {
                                String name2;
                                if (i == 0) {
                                    name2 = "DestinationOnTop";
                                    midgrounds = new ArrayList<AGDT_Shapefile>();
                                    midgrounds.add(new DW_Shapefile(f0));
                                    midgrounds.add(new DW_Shapefile(f1));
                                    midgrounds.add(new DW_Shapefile(f2));
                                } else {
                                    name2 = "OriginOnTop";
                                    midgrounds = new ArrayList<AGDT_Shapefile>();
                                    midgrounds.add(new DW_Shapefile(f0));
                                    midgrounds.add(new DW_Shapefile(f2));
                                    midgrounds.add(new DW_Shapefile(f1));
                                }
                                System.out.println(name2);
                                File dirOut4;
                                dirOut4 = new File(
                                        dirOut3,
                                        name2);
                                dirOut4.mkdirs();
                                setStyleParameters(
                                        i,
                                        Color.RED,
                                        Color.BLUE);
                                DW_Geotools.outputToImage(
                                        legendMessage,
                                        name,
                                        midgrounds,
                                        foregrounds,
                                        dirOut4,
                                        png_String,
                                        imageWidth,
                                        styleParameters,
                                        0,
                                        Double.POSITIVE_INFINITY,
                                        showMapsInJMapPane);
                            }
                        } else {
                            System.out.println("No lines to visualise1");
                        }
                    }
                }
                //System.out.println("count " + count);

            } else {
                System.out.println("No lines to visualise2");
            }
        }
    }

    private String getDatesString(
            String yM30Start,
            String yM30End) {
        String result;
        String[] split;
        split = yM30Start.split("_");
        result = split[1] + " " + split[0] + " to ";
        split = yM30End.split("_");
        result += split[1] + " " + split[0];
        return result;
    }
                        
    public void postcodeMaps(
            String paymentType,
            TreeMap<String, ArrayList<String>> yearMonths,
            String includeName,
            ArrayList<Integer> include,
            ArrayList<ArrayList<String>> allTenancyTypeChanges,
            boolean doTenancyTypeAndPostcodeChange,
            boolean doUnderOccupancyData,
            boolean doCouncil,
            boolean doRSL,
            boolean doCheckedPreviousTenure,
            boolean doCheckedPreviousPostcode,
            boolean grouped) throws Exception {
        File dirIn;
        File dirInCouncil;
        File dirInRSL;
        File dirOut;
        if (doTenancyTypeAndPostcodeChange) {
            dirIn = new File(
                    DW_Files.getOutputSHBETablesDir(),
                    "Tenancy");
            dirIn = new File(
                    dirIn,
                    paymentType);
            dirIn = new File(
                    dirIn,
                    "All");
            dirOut = new File(
                    mapDirectory,
                    paymentType);
            dirOut = new File(
                    dirOut,
                    DW_Files.sTenancyAndPostcodeChanges);
            dirOut = DW_Files.getUOFile(dirOut, doUnderOccupancyData, doCouncil, doRSL);
            dirIn = new File(
                    dirIn,
                    DW_Files.sTenancyTypeTransition);
            if (doCheckedPreviousTenure) {
                dirIn = new File(
                        dirIn,
                        DW_Files.sCheckedPreviousTenure);
                dirOut = new File(
                        dirOut,
                        DW_Files.sCheckedPreviousTenure);
            } else {
                dirIn = new File(
                        dirIn,
                        DW_Files.sNotCheckedPreviousTenure);
                dirOut = new File(
                        dirOut,
                        DW_Files.sNotCheckedPreviousTenure);
            }
            dirIn = new File(
                    dirIn,
                    DW_Files.sTenancyAndPostcodeChanges);
            dirInCouncil = DW_Files.getUOFile(dirIn, doUnderOccupancyData, true);
            dirInRSL = DW_Files.getUOFile(dirIn, doUnderOccupancyData, false);
            dirInCouncil = new File(
                    dirInCouncil,
                    DW_Files.sPostcodeChanged);
            dirInRSL = new File(
                    dirInRSL,
                    DW_Files.sPostcodeChanged);
            dirInCouncil = new File(
                    dirInCouncil,
                    includeName);
            dirInRSL = new File(
                    dirInRSL,
                    includeName);
        } else {
            dirIn = new File(
                    DW_Files.getOutputSHBETablesDir(),
                    DW_Files.sPostcodeChanges);
            dirOut = new File(
                    mapDirectory,
                    DW_Files.sPostcodeChanges);
            dirOut = DW_Files.getUOFile(dirOut, doUnderOccupancyData, doCouncil, doRSL);
            dirInCouncil = DW_Files.getUOFile(dirIn, doUnderOccupancyData, true);
            dirInRSL = DW_Files.getUOFile(dirIn, doUnderOccupancyData, false);
            dirInCouncil = new File(
                    dirInCouncil,
                    DW_Files.sPostcodeChanged);
            dirInRSL = new File(
                    dirInRSL,
                    DW_Files.sPostcodeChanged);
            dirInCouncil = new File(
                    dirInCouncil,
                    includeName);
            dirInRSL = new File(
                    dirInRSL,
                    includeName);
        }
        if (grouped) {
            dirInCouncil = new File(
                    dirInCouncil,
                    DW_Files.sGrouped);
            dirInRSL = new File(
                    dirInRSL,
                    DW_Files.sGrouped);
            dirOut = new File(
                    dirOut,
                    DW_Files.sGrouped);
        } else {
            dirInCouncil = new File(
                    dirInCouncil,
                    DW_Files.sUngrouped);
            dirInRSL = new File(
                    dirInRSL,
                    DW_Files.sUngrouped);
            dirOut = new File(
                    dirOut,
                    DW_Files.sUngrouped);
        }
        dirInCouncil = new File(
                dirInCouncil,
                DW_Files.sPostcodeChanges);
        dirInRSL = new File(
                dirInRSL,
                DW_Files.sPostcodeChanges);
        if (doCheckedPreviousPostcode) {
            dirInCouncil = new File(
                    dirInCouncil,
                    DW_Files.sCheckedPreviousPostcode);
            dirInRSL = new File(
                    dirInRSL,
                    DW_Files.sCheckedPreviousPostcode);
            dirOut = new File(
                    dirOut,
                    DW_Files.sCheckedPreviousPostcode);
        } else {
            dirInCouncil = new File(
                    dirInCouncil,
                    DW_Files.sNotCheckedPreviousPostcode);
            dirInRSL = new File(
                    dirInRSL,
                    DW_Files.sNotCheckedPreviousPostcode);
            dirOut = new File(
                    dirOut,
                    DW_Files.sNotCheckedPreviousPostcode);
        }
        Iterator<String> yearMonthsIte;
        yearMonthsIte = yearMonths.keySet().iterator();
        while (yearMonthsIte.hasNext()) {
            String name;
            name = yearMonthsIte.next();
            System.out.println(name);
            ArrayList<String> yM3s;
            yM3s = yearMonths.get(name);
            ArrayList<String> lines;
            lines = new ArrayList<String>();
            Iterator<String> yMsIte;
            yMsIte = yM3s.iterator();
            String yM30 = yMsIte.next();
            String yM30Start = yM30;
            String yM30End;
            while (yMsIte.hasNext()) {
                String yM31;
                yM31 = yMsIte.next();
                String filename = "PostcodeChanges_Start_" + yM30 + "_End_" + yM31 + ".csv";
                File f;
                f = new File(
                        dirInCouncil,
                        filename);
                if (f.exists()) {
                    //System.out.println("Load " + f);
                    ArrayList<String> linesPart;
                    linesPart = DW_Table.readCSV(f);
                    lines.addAll(linesPart);
                }
                f = new File(
                        dirInRSL,
                        filename);
                if (f.exists()) {
                    //System.out.println("Load " + f);
                    ArrayList<String> linesPart;
                    linesPart = DW_Table.readCSV(f);
                    lines.addAll(linesPart);
                }
                yM30 = yM31;
            }
            yM30End = yM30;
            if (!lines.isEmpty()) {
                //name += "_TO_" + split1[0] + "_" + split1[1];
                File dirOut2;
                dirOut2 = new File(
                        dirOut,
                        name);
                // Step 3.
                SimpleFeatureType aLineSFT = DataUtilities.createType(
                        "LINE",
                        "the_geom:LineString," + "name:String," + "number:Integer");

                // Totals for each tenancy type
                Iterator<ArrayList<String>> allTenancyTypeChangesIte;
                allTenancyTypeChangesIte = allTenancyTypeChanges.iterator();
                while (allTenancyTypeChangesIte.hasNext()) {
                    ArrayList<String> tenancyTypeChanges;
                    tenancyTypeChanges = allTenancyTypeChangesIte.next();

                    // Initialise a FeatureCollections and SimpleFeatureBuilders
                    HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcSs;
                    tsfcSs = new HashMap<ArrayList<String>, TreeSetFeatureCollection>();
                    HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcMs;
                    tsfcMs = new HashMap<ArrayList<String>, TreeSetFeatureCollection>();
                    HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcEs;
                    tsfcEs = new HashMap<ArrayList<String>, TreeSetFeatureCollection>();

                    HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbSs;
                    sfbSs = new HashMap<ArrayList<String>, SimpleFeatureBuilder>();
                    HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbMs;
                    sfbMs = new HashMap<ArrayList<String>, SimpleFeatureBuilder>();
                    HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbEs;
                    sfbEs = new HashMap<ArrayList<String>, SimpleFeatureBuilder>();

                    // Create SimpleFeatureBuilder
                    //FeatureFactory ff = FactoryFinder.getGeometryFactories();
                    GeometryFactory geometryFactory = new GeometryFactory();

                    long count = 0;
                    long notPCCount = 0;

                    Iterator<String> linesIte;
                    linesIte = lines.iterator();
                    while (linesIte.hasNext()) {
                        String line;
                        line = linesIte.next();
                        String[] lineSplit;
                        lineSplit = line.split(",");
                        yM30 = lineSplit[1].trim();
                        String yM31;
                        yM31 = lineSplit[2].trim();
                        String yM30v;
                        yM30v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM30);
                        String yM31v;
                        yM31v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
                        String tenancyTypeChange;
                        tenancyTypeChange = lineSplit[3].trim();
                        if (tenancyTypeChanges.contains(tenancyTypeChange)) {
                            if (yM3s.contains(yM31)) {
                                String postcode0 = lineSplit[4].trim();
                                String postcode1 = lineSplit[5].trim();
                                AGDT_Point origin;
                                origin = DW_Postcode_Handler.getPointFromPostcode(
                                        yM30v,
                                        DW_Postcode_Handler.TYPE_UNIT,
                                        postcode0);
                                AGDT_Point destination;
                                destination = DW_Postcode_Handler.getPointFromPostcode(
                                        yM31v,
                                        DW_Postcode_Handler.TYPE_UNIT,
                                        postcode1);
                                TreeSetFeatureCollection tsfcS;
                                tsfcS = tsfcSs.get(tenancyTypeChanges);
                                if (tsfcS == null) {
                                    tsfcS = new TreeSetFeatureCollection();
                                    tsfcSs.put(tenancyTypeChanges, tsfcS);
                                }
                                SimpleFeatureBuilder sfbS;
                                sfbS = sfbSs.get(tenancyTypeChanges);
                                if (sfbS == null) {
                                    sfbS = new SimpleFeatureBuilder(aLineSFT);
                                }
                                TreeSetFeatureCollection tsfcM;
                                tsfcM = tsfcMs.get(tenancyTypeChanges);
                                if (tsfcM == null) {
                                    tsfcM = new TreeSetFeatureCollection();
                                    tsfcMs.put(tenancyTypeChanges, tsfcM);
                                }
                                SimpleFeatureBuilder sfbM;
                                sfbM = sfbMs.get(tenancyTypeChanges);
                                if (sfbM == null) {
                                    sfbM = new SimpleFeatureBuilder(aLineSFT);
                                }
                                TreeSetFeatureCollection tsfcE;
                                tsfcE = tsfcEs.get(tenancyTypeChanges);
                                if (tsfcE == null) {
                                    tsfcE = new TreeSetFeatureCollection();
                                    tsfcEs.put(tenancyTypeChanges, tsfcE);
                                }
                                SimpleFeatureBuilder sfbE;
                                sfbE = sfbEs.get(tenancyTypeChanges);
                                if (sfbE == null) {
                                    sfbE = new SimpleFeatureBuilder(aLineSFT);
                                }
                                if (origin != null && destination != null) {

                                    double originx;
                                    double originy;
                                    originx = origin.getX();
                                    originy = origin.getY();
                                    double destinationx;
                                    double destinationy;
                                    destinationx = destination.getX();
                                    destinationy = destination.getY();

                                    double break1x;
                                    double break1y;
                                    break1x = (originx + (originx + ((originx + destinationx) / 2.0d)) / 2.0d) / 2.0d;
                                    break1y = (originy + (originy + ((originy + destinationy) / 2.0d)) / 2.0d) / 2.0d;
//                        break1x = (originx + destinationx) / 2.0d;
//                        break1y = (originy + destinationy) / 2.0d;

                                    double break2x;
                                    double break2y;
                                    break2x = (destinationx + (destinationx + ((originx + destinationx) / 2.0d)) / 2.0d) / 2.0d;
                                    break2y = (destinationy + (destinationy + ((originy + destinationy) / 2.0d)) / 2.0d) / 2.0d;
//                        break2x = (originx + destinationx) / 2.0d;
//                        break2y = (originy + destinationy) / 2.0d;

//                        double midpointx;
//                        double midpointy;
//                        midpointx = (originx + destinationx) / 2.0d;
//                        midpointy = (originy + destinationy) / 2.0d;
                                    Coordinate[] coords;
                                    LineString lineString;
                                    SimpleFeature feature;

//                        coords = new Coordinate[2];
//                        coords[0] = new Coordinate(originx, originy);
//                        coords[1] = new Coordinate(destinationx, destinationy);
//                        lineString = geometryFactory.createLineString(coords);
//                        sfbOM.add(lineString);
////                        // Add attributes to line
////          sfb.add(outlet);
//                        feature = sfbOM.buildFeature(null);
//                        tsfcOM.add(feature);
                                    coords = new Coordinate[2];
                                    coords[0] = new Coordinate(originx, originy);
                                    coords[1] = new Coordinate(break1x, break1y);
                                    lineString = geometryFactory.createLineString(coords);
                                    sfbS.add(lineString);
                                    feature = sfbS.buildFeature(null);
                                    tsfcS.add(feature);

                                    coords = new Coordinate[2];
                                    coords[0] = new Coordinate(break1x, break1y);
                                    coords[1] = new Coordinate(break2x, break2y);
                                    lineString = geometryFactory.createLineString(coords);
                                    sfbM.add(lineString);
                                    // Add attributes to line
//          sfb.add(outlet);
                                    feature = sfbM.buildFeature(null);
                                    tsfcM.add(feature);

                                    coords = new Coordinate[2];
                                    coords[0] = new Coordinate(break2x, break2y);
                                    coords[1] = new Coordinate(destinationx, destinationy);
                                    lineString = geometryFactory.createLineString(coords);
                                    sfbE.add(lineString);
                                    // Add attributes to line
//          sfb.add(outlet);
                                    feature = sfbE.buildFeature(null);
                                    tsfcE.add(feature);

                                    count++;

                                } else {
                                    notPCCount++;
                                }
                            }
                        }
                    }
                    String dates;
                    dates = getDatesString(yM30Start, yM30End);
                    String legendMessage;
                    legendMessage = dates + " Count " + count;
                    System.out.println("line count = " + count);
                    System.out.println("Postcode not valid count = " + notPCCount);
                    TreeSetFeatureCollection tsfc;
                    name = getName(tenancyTypeChanges);
                    File dirOut3;
                    dirOut3 = new File(
                            dirOut2,
                            name);
                    String nameM;
                    nameM = name + "M";
                    tsfc = tsfcMs.get(tenancyTypeChanges);
                    if (tsfc != null) {
                        File f0 = AGDT_Geotools.getOutputShapefile(
                                dirOut3,
                                nameM);
                        DW_Shapefile.transact(
                                f0,
                                aLineSFT,
                                tsfc,
                                getShapefileDataStoreFactory());
                        String nameS;
                        nameS = name + "S";
                        tsfc = tsfcSs.get(tenancyTypeChanges);
                        File f1 = AGDT_Geotools.getOutputShapefile(
                                dirOut3,
                                nameS);
                        DW_Shapefile.transact(
                                f1,
                                aLineSFT,
                                tsfc,
                                getShapefileDataStoreFactory());
                        String nameE;
                        nameE = name + "E";
                        tsfc = tsfcEs.get(tenancyTypeChanges);
                        File f2 = AGDT_Geotools.getOutputShapefile(
                                dirOut3,
                                nameE);
                        DW_Shapefile.transact(
                                f2,
                                aLineSFT,
                                tsfc,
                                getShapefileDataStoreFactory());

//            // Had Hoped to getting away with doing this once
//            FeatureLayer backgroundFeatureLayer = DW_Shapefile.getFeatureLayer(
//                backgroundShapefile,
//                backgroundStyle);
                        for (int i = 0; i < 2; i++) {
                            String name2;
                            if (i == 0) {
                                name2 = "DestinationOnTop";
                                midgrounds = new ArrayList<AGDT_Shapefile>();
                                midgrounds.add(new DW_Shapefile(f0));
                                midgrounds.add(new DW_Shapefile(f1));
                                midgrounds.add(new DW_Shapefile(f2));
                            } else {
                                name2 = "OriginOnTop";
                                midgrounds = new ArrayList<AGDT_Shapefile>();
                                midgrounds.add(new DW_Shapefile(f0));
                                midgrounds.add(new DW_Shapefile(f2));
                                midgrounds.add(new DW_Shapefile(f1));
                            }
                            System.out.println(name2);
                            File dirOut4;
                            dirOut4 = new File(
                                    dirOut3,
                                    name2);
                            dirOut4.mkdirs();
                            setStyleParameters(
                                    i,
                                    Color.RED,
                                    Color.BLUE);
                            DW_Geotools.outputToImage(
                                    legendMessage,
                                    name,
                                    midgrounds,
                                    foregrounds,
                                    dirOut4,
                                    png_String,
                                    imageWidth,
                                    styleParameters,
                                    0,
                                    Double.POSITIVE_INFINITY,
                                    showMapsInJMapPane);
                        }
                    } else {
                        System.out.println("No lines to visualise1");
                    }
                }
            } else {
                System.out.println("No lines to visualise2");
            }
        }
    }

    public static String getName(ArrayList<String> tenancyTypeChanges) {
        String result = "";
        Iterator<String> ite;
        ite = tenancyTypeChanges.iterator();
        boolean doneFirst = false;
        while (ite.hasNext()) {
            String s;
            s = ite.next().replaceAll(" ", "");
            if (!doneFirst) {
                result += s;
                doneFirst = true;
            } else {
                result += "And" + s;
            }
        }
        return result;
    }
}
