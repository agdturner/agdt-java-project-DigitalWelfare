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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.lcc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.core.SHBE_ID;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Handler;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Records;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_ProcessorAbstract;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.reporting.DW_Report;
//import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.charts.DW_BarChart;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.charts.DW_LineGraph;

/**
 *
 * @author geoagdt
 */
public class DW_ProcessorLCC extends DW_ProcessorAbstract {

    // For convenience
    protected transient SHBE_Handler SHBE_Handler;
    protected transient DW_UO_Data UO_Data;
    protected transient String[] SHBEFilenames;
    protected transient HashMap<SHBE_ID, String> ClaimIDToClaimRefLookup;

    public DW_ProcessorLCC(DW_Environment e) {
        super(e);
        SHBE_Handler = e.getSHBE_Handler();
        UO_Data = e.getUO_Data();
        SHBEFilenames = SHBE_Handler.getSHBEFilenamesAll();
        ClaimIDToClaimRefLookup = SHBE_Handler.getClaimIDToClaimRefLookup();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.err.println("Expected an argument which is the location "
                        + "of the directory containing the input data. "
                        + "Aborting.");
                System.exit(0);
            } else {
                File dataDir = new File(args[1]);
                Generic_Environment ge;
                ge = new Generic_Environment(dataDir, Level.FINE);
                DW_Environment env = new DW_Environment(ge);
                DW_ProcessorLCC p;
                p = new DW_ProcessorLCC(env);
                p.run();
                /**
                 * Not done this way as this would first load UnderOccupancy
                 * data. This is to be avoided as we want to load first the SHBE
                 * and then load the UO data.
                 */
                // new DW_ProcessorLCC(env).run();

            }
        } catch (Exception | Error e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace(System.err);
//            StackTraceElement[] stes = e.getStackTrace();
//            for (StackTraceElement ste : stes) {
//                System.err.println(ste.toString());
//            }
        }
    }

    /**
     * This is the main run method for the Digital welfare project.
     *
     * @throws Exception
     */
    public void run() throws Exception, Error {
        /*
         * Set which parts of the code to run. There is a logical order to these
         * in that some parts have to have been run before others will produce 
         * results as expected/desired.
         */
//        RunAllFromScratch = true;
        RunAllFromScratch = false;
        RunAllFromUpdate = false;
        if (!RunAllFromScratch) {
            if (!RunAllFromUpdate) {
//                // Choose which bits to run
//                doLoadAllONSPDFromSource = true;
////                doLoadNewONSPDFromSource = true;
                doLoadAllSHBEFromSource = true;
////                doLoadNewSHBEFromSource = true;
//                doLoadSHBE = true;
////                doPostcodeCheckLatest = true;
////                doPostcodeCheck = true;
//                doLoadUnderOccupancyFromSource = true;
//                doLoadUnderOccupancy = true;
                doLCCSummary = true;
                doRentArrears = true;
//                doRentArrearsNewData = true;
                doLCCTenancyChangesUO = true; //Under-Occupancy Group Tables
                doLCCHBGeneralAggregateStatistics = true;
                doLCCTTAndPT = true;
                if (doLCCTTAndPT) {
                    doLCCTTAndPTAll = true; // Runtime 13:23:08.458s
                    /**
                     * If doLCCTTAndPTAll = false then choose bits otherwise run
                     * for all combinations.
                     */
//                    doLCCTTAndPTAll = false;
//                    LCCTTAndPT_DoGrouped = true;
//                    LCCTTAndPT_DoPostcodeChanges = true;
////                    LCCTTAndPT_DoPostcodeChanges = false;
//                    LCCTTAndPT_DoAnyTenancyChanges = true;
////                    LCCTTAndPT_DoAnyTenancyChanges = false;
//                    LCCTTAndPT_DoTenancyChanges = true;
////                    LCCTTAndPT_DoTenancyChanges = false;
//                    LCCTTAndPT_DoTenancyAndPostcodeChanges = true;
////                    LCCTTAndPT_DoTenancyAndPostcodeChanges = false;
                }
                doChoroplethMapping = true;
                doLineMaps = true;
                doReports = true;
                doLineGraphs = true;
                if (doLineGraphs) {
                    doLineGraphTenancyTypeTransitions = true;
                    //LineGraphAggregateData = true;
                }
//                doDensityMaps = true;
//                doLineDensityMaps = true;
            } else {
                //loadNewONSPDFromSource = true;
                doLoadNewSHBEFromSource = true;
                doLoadUnderOccupancyFromSource = true;
                doLCCSummary = true;
                doRentArrears = true;
                doRentArrearsNewData = true;
                doLCCTenancyChangesUO = true; //Under-Occupancy Group Tables
                doLCCHBGeneralAggregateStatistics = true; // Could do with a new method that only runs the aggregated data that has not already been produced.
                doLCCTTAndPT = true;
                doChoroplethMapping = true;
                doLineMaps = true;
                doReports = true;
                doLineGraphs = true;
                doDensityMaps = true;
                doLineDensityMaps = true;
            }
        } else {
            doLoadAllONSPDFromSource = true;
            doLoadAllSHBEFromSource = true;
            doLoadUnderOccupancyFromSource = true;
            doLCCSummary = true;
            doRentArrears = true;
            doRentArrearsNewData = true;
            doLCCTenancyChangesUO = true; //Under-Occupancy Group Tables
            doLCCHBGeneralAggregateStatistics = true;
            doLCCTTAndPT = true;
            doChoroplethMapping = true;
            doLineMaps = true;
            doReports = true;
            doLineGraphs = true;
            doDensityMaps = true;
            doLineDensityMaps = true;
        }
        // range is the number of directories or files in any archives.
        int range;
        range = 100;
        process(range);
    }

    /**
     *
     * @param range The number of directories or files in any archives.
     * @throws Exception
     */
    private void process(int range) throws Exception {
        String processName;
        /**
         * Format Postcode_Handling data. The ONSPD Data is updated
         * approximately quarterly. It is used in this program to check and
         * format unit postcodes from the SHBE and keep a mapping from these to
         * the respective Eastings and Northings as AGDT_Points. The ONSPD Data
         * is needed when loading SHBE Data, but should not be needed once this
         * has been loaded as the maps generated when it is loaded should be
         * sufficient.
         */
        if (doLoadAllONSPDFromSource || doLoadNewONSPDFromSource) {
            // Set up logging
            if (doLoadAllONSPDFromSource) {
                processName = "LoadAllONSPDFromSource";
            } else {
                processName = "LoadNewONSPDFromSource";
            }
            int logID2 = env.ge.initLog(processName);
            // Process
            Postcode_Handler = env.SHBE_Env.oe.getHandler();
            Postcode_Handler.run(logID2);
            // Close logs
            env.ge.closeLog(logID2);
        }

        /**
         * Format All SHBE data. This checks and formats postcodes.
         */
        if (doLoadAllSHBEFromSource) {
            // Set up logging
            processName = "LoadAllSHBEFromSource";
            int logID2 = env.ge.initLog(processName);
            // Process
            SHBE_Handler = new SHBE_Handler(env.SHBE_Env, logID2);
            env.setSHBE_Handler(SHBE_Handler);
            SHBE_Handler.run(logID2);
            // Close logs
            env.ge.closeLog(logID2);
        }

        /**
         * Format New SHBE data. This checks and formats postcodes.
         */
        if (doLoadNewSHBEFromSource) {
            // Set up logging
            processName = "LoadNewSHBEFromSource";
            int logID2 = env.ge.initLog(processName);
            // Process
            SHBE_Handler = new SHBE_Handler(env.SHBE_Env, logID2);
            env.setSHBE_Handler(SHBE_Handler);
            SHBE_Handler.runNew();
            // Close logs
            env.ge.closeLog(logID2);
        }

        /**
         * Check SHBE postcodes.
         */
        if (doPostcodeCheck) {
            // Set up logging
            processName = "PostcodeCheck";
            int logID2 = env.ge.initLog(processName);
            // Process
            SHBE_Handler = new SHBE_Handler(env.SHBE_Env, logID2);
            env.setSHBE_Handler(SHBE_Handler);
            SHBE_Handler.runPostcodeCheck();
            // Close logs
            env.ge.closeLog(logID2);
        }

        /**
         * Check SHBE postcodes.
         */
        if (doPostcodeCheckLatest) {
            // Set up logging
            processName = "PostcodeCheckLatest";
            int logID2 = env.ge.initLog(processName);
            // Process
            SHBE_Handler = new SHBE_Handler(env.SHBE_Env);
            env.setSHBE_Handler(SHBE_Handler);
            SHBE_Handler.runPostcodeCheckLatest();
            // Close logs
            env.ge.closeLog(logID2);
        }

        /**
         * Load SHBE data. This loads already formatted SHBE data. This process
         * is for checking the data loads. It could be used as a basis for
         * testing the speed of loading and for memory requirements.
         */
        if (doLoadSHBE) {
            // Set up logging
            processName = "LoadSHBE";
            int logID2 = env.ge.initLog(processName);
            // Process
            HashMap<ONSPD_YM3, SHBE_Records> Data;
            Data = env.SHBE_Handler.getData();
            SHBE_Handler = new SHBE_Handler(env.SHBE_Env);
            env.setSHBE_Handler(SHBE_Handler);
            SHBEFilenames = SHBE_Handler.getSHBEFilenamesAll();
            File dir;
            dir = env.files.getGeneratedSHBEDir();
            ONSPD_YM3 YM3;
            for (String SHBEFilename : SHBEFilenames) {
                YM3 = SHBE_Handler.getYM3(SHBEFilename);
                try {
                    SHBE_Records DW_SHBE_Records;
                    DW_SHBE_Records = new SHBE_Records(env.SHBE_Env, YM3);
                    env.checkAndMaybeFreeMemory();
                    Data.put(YM3, DW_SHBE_Records);
                    env.ge.log("DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateInPayment().size() "
                            + DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateInPayment(env.HOOME).size(), true);
                    env.ge.log("DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateSuspended().size() "
                            + DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateSuspended(env.HOOME).size(), true);
                    env.ge.log("DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateOther().size() "
                            + DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateOther(env.HOOME).size(), true);
                    env.ge.log("DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateInPayment().size() "
                            + DW_SHBE_Records.getClaimIDsWithStatusOfCTBAtExtractDateInPayment(env.HOOME).size(), true);
                    env.ge.log("DW_SHBE_Records.getClaimIDsWithStatusOfCTBAtExtractDateSuspended().size() "
                            + DW_SHBE_Records.getClaimIDsWithStatusOfCTBAtExtractDateSuspended(env.HOOME).size(), true);
                    env.ge.log("DW_SHBE_Records.getClaimIDsWithStatusOfCTBAtExtractDateOther().size() "
                            + DW_SHBE_Records.getClaimIDsWithStatusOfCTBAtExtractDateOther(env.HOOME).size(), true);
                    DW_SHBE_Records.clearData();
                } catch (OutOfMemoryError e) {
                    env.clearMemoryReserve();
                    env.clearSomeSHBECacheExcept(YM3);
                    env.initMemoryReserve();
                    env.checkAndMaybeFreeMemory();
                }
            }
            // Close logs
            env.ge.closeLog(logID2);
        }

        /**
         * Load UnderOccupancy data from source.
         */
        if (doLoadUnderOccupancyFromSource) {
            // Set up logging
            processName = "LoadUnderOccupancyFromSource";
            int logID2 = env.ge.initLog(processName);
            // Process
            boolean loadFromSource;
            loadFromSource = true;
            UO_Data = env.getUO_Data(loadFromSource);
            // Close logs
            env.ge.closeLog(logID2);
        }

        if (doLoadUnderOccupancy) {
            // Set up logging
            processName = "LoadUnderOccupancy";
            int logID2 = env.ge.initLog(processName);
            // Process
            boolean loadFromSource;
            loadFromSource = false;
            UO_Data = env.getUO_Data(loadFromSource);
            // Close logs
            env.ge.closeLog(logID2);
        }

        if (doLCCSummary) {
            // Set up logging
            processName = "LCCSummary";
            int logID2 = env.ge.initLog(processName);
            // Process
            DW_ProcessorLCCSummary p;
            p = new DW_ProcessorLCCSummary(env);
            p.run();
            // Close logs
            env.ge.closeLog(logID2);
        }

        if (doRentArrears) {
            // Set up logging
            processName = "RentArrears";
            int logID2 = env.ge.initLog(processName);
            // Process
            DW_ProcessorLCCRentArrears p;
            p = new DW_ProcessorLCCRentArrears(env);
            p.run(doRentArrearsNewData);
            // Close logs
            env.ge.closeLog(logID2);
        }

        if (doLCCHBGeneralAggregateStatistics) {
            // Set up logging
            processName = "LCCHBGeneralAggregateStatistics";
            int logID2 = env.ge.initLog(processName);
            // Process
            DW_ProcessorLCCHBGeneralAggregateStatistics p;
            p = new DW_ProcessorLCCHBGeneralAggregateStatistics(env);
            p.run();
            // Close logs
            env.ge.closeLog(logID2);
        }

        if (doLCCTenancyChangesUO) {
            // Set up logging
            processName = "LCCTenancyChangesUO";
           int logID2 = env.ge.initLog(processName);
            // Process
            DW_ProcessorLCCTenancyChangesUO p;
            p = new DW_ProcessorLCCTenancyChangesUO(env);
            p.run();
            // Close logs
            env.ge.closeLog(logID2);
        }

        if (doLCCTTAndPT) {
            // Set up logging
            processName = "LCCTTAndPT";
            int logID2 = env.ge.initLog(processName);
            // Process
            DW_ProcessorLCCTTAndPT p;
            p = new DW_ProcessorLCCTTAndPT(env);
            if (doLCCTTAndPTAll) {
                LCCTTAndPT_DoGrouped = true;
                LCCTTAndPT_DoPostcodeChanges = true;
                LCCTTAndPT_DoAnyTenancyChanges = true;
                LCCTTAndPT_DoTenancyChanges = true;
                LCCTTAndPT_DoTenancyAndPostcodeChanges = true;
                p.run(LCCTTAndPT_DoGrouped,
                        LCCTTAndPT_DoPostcodeChanges,
                        LCCTTAndPT_DoAnyTenancyChanges,
                        LCCTTAndPT_DoTenancyChanges,
                        LCCTTAndPT_DoTenancyAndPostcodeChanges);
            } else {
                // For debugging
                p.run(LCCTTAndPT_DoGrouped,
                        LCCTTAndPT_DoPostcodeChanges,
                        LCCTTAndPT_DoAnyTenancyChanges,
                        LCCTTAndPT_DoTenancyChanges,
                        LCCTTAndPT_DoTenancyAndPostcodeChanges);
            }
            // Close logs
            env.ge.closeLog(logID2);
        }

        if (doChoroplethMapping) {
            // Set up logging
            processName = "ChoroplethMapping";
            int logID2 = env.ge.initLog(processName);
            // Process
            DW_ChoroplethMapsLCC p;
            p = new DW_ChoroplethMapsLCC(env);
            p.run();
            // Close logs
            env.ge.closeLog(logID2);
        }

        if (doLineMaps) {
            // Set up logging
            processName = "LineMaps";
            int logID2 = env.ge.initLog(processName);
            // Process
            DW_LineMapsLCC p;
            p = new DW_LineMapsLCC(env);
            p.run();
            boolean underOccupancy;
            underOccupancy = true;
            p.run2(underOccupancy, true);
            p.run2(underOccupancy, false);
            p.run2(false, false);
            p.run();
            // Close logs
            env.ge.closeLog(logID2);
        }

        if (doReports) {
            // Set up logging
            processName = "Reports";
            int logID2 = env.ge.initLog(processName);
            // Process
            DW_Report p;
            p = new DW_Report(env);
            p.run();
            // Close logs
            env.ge.closeLog(logID2);
        }

        if (doLineGraphs) {
            // Set up logging
            processName = "LineGraphs";
            int logID2 = env.ge.initLog(processName);
            // Process
            DW_LineGraph p;
            p = new DW_LineGraph(env);
            //aDW_LineGraph.start();
            p.run(doLineGraphTenancyTypeTransitions,
                    doLineGraphAggregateData);
            // Close logs
            env.ge.closeLog(logID2);
        }

        if (doDensityMaps) {
            // Set up logging
            processName = "DensityMaps";
            int logID2 = env.ge.initLog(processName);
            // Process
            DW_DensityMapsLCC p;
            p = new DW_DensityMapsLCC(env);
            p.run();
            // Close logs
            env.ge.closeLog(logID2);
        }

        if (doLineDensityMaps) {
            // Set up logging
            processName = "LineDensityMaps";
            int logID2 = env.ge.initLog(processName);
            // Process
            DW_LineDensityMaps_LCC p;
            p = new DW_LineDensityMaps_LCC(env);
            p.run();
            DW_LineDensityDifferenceMaps_LCC p2;
            p2 = new DW_LineDensityDifferenceMaps_LCC(env);
            p2.run();
            // Close logs
            env.ge.closeLog(logID2);
        }
    }

    /**
     * Switch for loading All ONSPD Data From Source.
     */
    boolean doLoadAllONSPDFromSource = false;

    /**
     * Switch for loading New ONSPD Data From Source.
     */
    boolean doLoadNewONSPDFromSource = false;

    /**
     * Switch for loading SHBE Data From Source.
     */
    boolean doLoadAllSHBEFromSource = false;

    /**
     * Switch for reformatting all the SHBE data.
     */
    boolean doReformatAll = false;

    /**
     * Switch for printing stats of reformatted SHBE data.
     */
    boolean doStat = false;

    /**
     * Switch for reformatting new SHBE data that might have been added to the
     * input collection.
     */
    boolean doLoadNewSHBEFromSource = false;

    /**
     * Switch for loading SHBE Data.
     */
    boolean doLoadSHBE = false;

    /**
     * Switch for SHBE postcode check run.
     */
    boolean doPostcodeCheck = false;
    boolean doPostcodeCheckLatest = false;
    /**
     * Switch for loading Under Occupancy Data From Source.
     */
    boolean doLoadUnderOccupancyFromSource = false;

    /**
     * Switch for loading Under Occupancy Data From Source.
     */
    boolean doLoadUnderOccupancy = false;

    /**
     * Switch for running LCC Summary processing algorithms.
     */
    boolean doLCCSummary = false;

    /**
     * Switch for running Rent Arrears processing algorithms.
     */
    boolean doRentArrears = false;
    boolean doRentArrearsNewData = false;

    /**
     * Switch for running doLCCTenancyChangesUO.
     */
    boolean doLCCTenancyChangesUO = false;

    /**
     * Switch for running doLCCHBGeneralAggregateStatistics.
     */
    boolean doLCCHBGeneralAggregateStatistics = false;

    /**
     * Switch for running LCC Data processing algorithms.
     */
    boolean doLCCTTAndPT = false;
    boolean doLCCTTAndPTAll = false;
    boolean LCCTTAndPT_DoGrouped;
    boolean LCCTTAndPT_DoPostcodeChanges;
    boolean LCCTTAndPT_DoAnyTenancyChanges;
    boolean LCCTTAndPT_DoTenancyChanges;
    boolean LCCTTAndPT_DoTenancyAndPostcodeChanges;

    /**
     * Switch for running Choropleth Mapping code.
     */
    boolean doChoroplethMapping = false;

    /**
     * Switch for running Line Mapping code.
     */
    boolean doLineMaps = false;

    /**
     * Switch for running report generation code.
     */
    boolean doReports = false;

    /**
     * Switch for generating Line Graphs.
     */
    boolean doLineGraphs = false;

    /**
     * Switch for generating Tenancy Type Transition Line Graphs.
     */
    boolean doLineGraphTenancyTypeTransitions = true;

    /**
     * Switch for generating Tenancy Type Transition Line Graphs.
     */
    boolean doLineGraphAggregateData = true;

    /**
     * Switch for generating Density Maps.
     */
    boolean doDensityMaps = false;

    /**
     * Switch for generating Line Density Maps.
     */
    boolean doLineDensityMaps = false;

    /**
     * Switch for generating everything from scratch.
     */
    boolean RunAllFromScratch = false;

    /**
     * Switch for generating new output given more data.
     */
    boolean RunAllFromUpdate = false;

    /**
     * @param levels A set of levels expected values include OA, LSOA, MSOA,
     * PostcodeUnit, PostcodeSector, PostcodeDistrict.
     * @param YM3
     * @param CensusYear This has to be 2001 or 2011 or the levels might not
     * include census levels.
     * @return A set of look ups from postcodes to each level input in levels.
     */
    public TreeMap<String, TreeMap<String, String>> getClaimPostcodeF_To_LevelCode_Maps(
            ArrayList<String> levels, ONSPD_YM3 YM3, int CensusYear) {
        TreeMap<String, TreeMap<String, String>> result;
        result = new TreeMap<>();
        Iterator<String> ite = levels.iterator();
        while (ite.hasNext()) {
            String level = ite.next();
            //            Iterate over YM3
            TreeMap<String, String> ClaimPostcodeF_To_LevelCode_Map;
            ClaimPostcodeF_To_LevelCode_Map = getClaimPostcodeF_To_LevelCode_Map(
                    level, CensusYear, YM3);
            result.put(level, ClaimPostcodeF_To_LevelCode_Map);
        }
        return result;
    }

    /**
     * For the postcode input, this returns the area code for the level input
     * using tLookupFromPostcodeToCensusCode.
     *
     * @param level
     * @param postcode
     * @param LookupFromPostcodeToCensusCode
     * @return The area code for level from tLookupFromPostcodeToCensusCode for
     * postcode. This may return "" or null.
     */
    public String getAreaCode(String level, String postcode, TreeMap<String, String> LookupFromPostcodeToCensusCode) {
        String result = "";
        // Special Case
        if (postcode.trim().isEmpty()) {
            return result;
        }
        if (level.equalsIgnoreCase(strings.sOA) || level.equalsIgnoreCase(strings.sLSOA) || level.equalsIgnoreCase(strings.sMSOA) || level.equalsIgnoreCase(strings.sStatisticalWard) || level.equalsIgnoreCase(strings.sParliamentaryConstituency)) {
            String formattedPostcode = Postcode_Handler.formatPostcode(postcode);
            result = LookupFromPostcodeToCensusCode.get(formattedPostcode);
            if (result == null) {
                result = "";
            }
        } else if (level.equalsIgnoreCase(strings.sPostcodeUnit) || level.equalsIgnoreCase(strings.sPostcodeSector) || level.equalsIgnoreCase(strings.sPostcodeDistrict)) {
            if (level.equalsIgnoreCase(strings.sPostcodeUnit)) {
                result = postcode;
            }
            if (level.equalsIgnoreCase(strings.sPostcodeSector)) {
                result = Postcode_Handler.getPostcodeSector(postcode);
            }
            if (level.equalsIgnoreCase(strings.sPostcodeDistrict)) {
                result = Postcode_Handler.getPostcodeDistrict(postcode);
            }
        } else {
            // Unrecognised level
            env.ge.log("Unrecognised level in " + this.getClass().getName() 
                    + ".getAreaCode(String,String,TreeMap<String, String>)");
        }
        return result;
    }

    /**
     * For the postcode input, this returns the area code for the level input
     * using tLookupFromPostcodeToCensusCode.
     *
     * @param level
     * @param ClaimPostcodeF1
     * @param LookupFromPostcodeToCensusCode
     * @return The area code for level from tLookupFromPostcodeToCensusCode for
     * postcode. This may return "" or null.
     */
    public String getAreaCode(String level, TreeMap<String, String> LookupFromPostcodeToCensusCode, String ClaimPostcodeF1) {
        String result = "";
        // Special Case
        if (ClaimPostcodeF1.trim().isEmpty()) {
            return result;
        }
        if (level.equalsIgnoreCase("OA") || level.equalsIgnoreCase("LSOA") || level.equalsIgnoreCase("MSOA")) {
            result = LookupFromPostcodeToCensusCode.get(ClaimPostcodeF1);
            if (result == null) {
                result = "";
            }
        } else if (level.equalsIgnoreCase("PostcodeUnit") || level.equalsIgnoreCase("PostcodeSector") || level.equalsIgnoreCase("PostcodeDistrict")) {
            if (level.equalsIgnoreCase("PostcodeUnit")) {
                result = ClaimPostcodeF1;
            }
            if (level.equalsIgnoreCase("PostcodeSector")) {
                result = Postcode_Handler.getPostcodeSector(ClaimPostcodeF1);
            }
            if (level.equalsIgnoreCase("PostcodeDistrict")) {
                result = Postcode_Handler.getPostcodeDistrict(ClaimPostcodeF1);
            }
        } else {
            // Unrecognised level
            int debug = 1;
        }
        return result;
    }
}
