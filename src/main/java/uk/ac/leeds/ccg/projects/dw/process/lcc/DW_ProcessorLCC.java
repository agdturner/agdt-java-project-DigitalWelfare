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
package uk.ac.leeds.ccg.projects.dw.process.lcc;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import uk.ac.leeds.ccg.data.core.Data_Environment;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.data.ukp.data.UKP_Data;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;
import uk.ac.leeds.ccg.data.shbe.data.id.SHBE_ClaimID;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Handler;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Records;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Data;
import uk.ac.leeds.ccg.projects.dw.process.DW_Processor;
import uk.ac.leeds.ccg.projects.dw.reporting.DW_Report;
//import uk.ac.leeds.ccg.projects.dw.visualisation.charts.DW_BarChart;
import uk.ac.leeds.ccg.projects.dw.visualisation.charts.DW_LineGraph;

/**
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_ProcessorLCC extends DW_Processor {

    // For convenience
    protected transient SHBE_Handler shbeHandler;
    protected transient DW_UO_Data UO_Data;
    protected transient String[] shbeFilenames;
    protected transient Map<SHBE_ClaimID, String> cid2c;

    public DW_ProcessorLCC(DW_Environment e) throws IOException, Exception {
        super(e);
        shbeHandler = e.getSHBE_Handler();
        UO_Data = e.getUO_Data();
        shbeFilenames = shbeHandler.getFilenames();
        cid2c = shbeHandler.getCid2c();
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
                Path dataDir = Paths.get(args[1]);
                DW_Environment env = new DW_Environment(new Data_Environment(
                        new Generic_Environment(new Generic_Defaults(dataDir), 
                                Level.FINE)));
                DW_ProcessorLCC p = new DW_ProcessorLCC(env);
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
//                doLoadAllSHBEFromSource = true;
////                doLoadNewSHBEFromSource = true;
//                doLoadSHBE = true;
////                doPostcodeCheckLatest = true;
////                doPostcodeCheck = true;
//                doLoadUnderOccupancyFromSource = true;
//                doLoadUnderOccupancy = true;
//                doLCCSummary = true;
                doRentArrears = true;
//                doRentArrearsNewData = true;
//                doLCCTenancyChangesUO = true; //Under-Occupancy Group Tables
//                doLCCHBGeneralAggregateStatistics = true;
//                doLCCTTAndPT = true;
//                if (doLCCTTAndPT) {
//                    doLCCTTAndPTAll = true; // Runtime 13:23:08.458s
                /**
                 * If doLCCTTAndPTAll = false then choose bits otherwise run for
                 * all combinations.
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
//                }
//                doChoroplethMapping = true;
//                doLineMaps = true;
//                doReports = true;
//                doLineGraphs = true;
//                if (doLineGraphs) {
//                    doLineGraphTenancyTypeTransitions = true;
//                    //LineGraphAggregateData = true;
//                }
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
            shbeHandler = new SHBE_Handler(env.SHBE_Env, logID2);
            env.setSHBE_Handler(shbeHandler);
            shbeHandler.run(logID2);
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
            shbeHandler = new SHBE_Handler(env.SHBE_Env, logID2);
            env.setSHBE_Handler(shbeHandler);
            shbeHandler.runNew();
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
            shbeHandler = new SHBE_Handler(env.SHBE_Env, logID2);
            env.setSHBE_Handler(shbeHandler);
            shbeHandler.runPostcodeCheck();
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
            shbeHandler = new SHBE_Handler(env.SHBE_Env);
            env.setSHBE_Handler(shbeHandler);
            shbeHandler.runPostcodeCheckLatest();
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
            Map<UKP_YM3, SHBE_Records> data = env.SHBE_Handler.getData();
            shbeHandler = new SHBE_Handler(env.SHBE_Env);
            env.setSHBE_Handler(shbeHandler);
            shbeFilenames = shbeHandler.getFilenames();
            Path dir = env.files.getGeneratedSHBEDir();
            UKP_YM3 YM3;
            for (String SHBEFilename : shbeFilenames) {
                YM3 = shbeHandler.getYM3(SHBEFilename);
                try {
                    SHBE_Records shbeRecords = new SHBE_Records(env.SHBE_Env, YM3);
                    env.checkAndMaybeFreeMemory();
                    data.put(YM3, shbeRecords);
                    env.ge.log("shbeRecords.getClaimIDsWithStatusOfHBAtExtractDateInPayment().size() "
                            + shbeRecords.getClaimIDsWithStatusOfHBAtExtractDateInPayment(env.HOOME).size(), true);
                    env.ge.log("shbeRecords.getClaimIDsWithStatusOfHBAtExtractDateSuspended().size() "
                            + shbeRecords.getClaimIDsWithStatusOfHBAtExtractDateSuspended(env.HOOME).size(), true);
                    env.ge.log("shbeRecords.getClaimIDsWithStatusOfHBAtExtractDateOther().size() "
                            + shbeRecords.getClaimIDsWithStatusOfHBAtExtractDateOther(env.HOOME).size(), true);
                    env.ge.log("shbeRecords.getClaimIDsWithStatusOfHBAtExtractDateInPayment().size() "
                            + shbeRecords.getClaimIDsWithStatusOfCTBAtExtractDateInPayment(env.HOOME).size(), true);
                    env.ge.log("shbeRecords.getClaimIDsWithStatusOfCTBAtExtractDateSuspended().size() "
                            + shbeRecords.getClaimIDsWithStatusOfCTBAtExtractDateSuspended(env.HOOME).size(), true);
                    env.ge.log("shbeRecords.getClaimIDsWithStatusOfCTBAtExtractDateOther().size() "
                            + shbeRecords.getClaimIDsWithStatusOfCTBAtExtractDateOther(env.HOOME).size(), true);
                    shbeRecords.clearData();
                } catch (OutOfMemoryError e) {
                    env.clearMemoryReserve(env.ge);
                    env.clearSomeDataExcept(YM3);
                    env.initMemoryReserve(env.ge);
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
            DW_ProcessorLCCSummary p = new DW_ProcessorLCCSummary(env);
            p.run();
            // Close logs
            env.ge.closeLog(logID2);
        }

        if (doRentArrears) {
            // Set up logging
            processName = "RentArrears";
            int logID2 = env.ge.initLog(processName);
            // Process
            DW_ProcessorLCCRentArrears p = new DW_ProcessorLCCRentArrears(env);
            p.run(doRentArrearsNewData);
            // Close logs
            env.ge.closeLog(logID2);
        }

        if (doLCCHBGeneralAggregateStatistics) {
            // Set up logging
            processName = "LCCHBGeneralAggregateStatistics";
            int logID2 = env.ge.initLog(processName);
            // Process
            DW_ProcessorLCCHBGeneralAggregateStatistics p
                    = new DW_ProcessorLCCHBGeneralAggregateStatistics(env);
            p.run();
            // Close logs
            env.ge.closeLog(logID2);
        }

        if (doLCCTenancyChangesUO) {
            // Set up logging
            processName = "LCCTenancyChangesUO";
            int logID2 = env.ge.initLog(processName);
            // Process
            DW_ProcessorLCCTenancyChangesUO p
                    = new DW_ProcessorLCCTenancyChangesUO(env);
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
            DW_Report p = new DW_Report(env);
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
            DW_LineDensityMaps_LCC p = new DW_LineDensityMaps_LCC(env);
            p.run();
            DW_LineDensityDifferenceMaps_LCC p2
                    = new DW_LineDensityDifferenceMaps_LCC(env);
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
     * @throws java.io.IOException
     */
    public TreeMap<Integer, TreeMap<String, String>> getClaimPostcodeF_To_LevelCode_Maps(
            ArrayList<Integer> levels, UKP_YM3 YM3, int CensusYear) throws IOException {
        TreeMap<Integer, TreeMap<String, String>> r = new TreeMap<>();
        Iterator<Integer> ite = levels.iterator();
        while (ite.hasNext()) {
            int level = ite.next();
            TreeMap<String, String> m = getClaimPostcodeF_To_LevelCode_Map(
                    level, CensusYear, YM3);
            r.put(level, m);
        }
        return r;
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
    public String getAreaCode(Integer level, String postcode,
            TreeMap<String, String> LookupFromPostcodeToCensusCode) {
        String r = "";
        // Special Case
        if (postcode.trim().isEmpty()) {
            return r;
        }
        if (level == UKP_Data.TYPE_OA
                || level == UKP_Data.TYPE_LSOA
                || level == UKP_Data.TYPE_MSOA
                || level == UKP_Data.TYPE_StatisticalWard
                || level == UKP_Data.TYPE_ParliamentaryConstituency) {
            String fp = Postcode_Handler.formatPostcode(postcode);
            r = LookupFromPostcodeToCensusCode.get(fp);
            if (r == null) {
                r = "";
            }
        } else if (level == UKP_Data.TYPE_UNIT) {
            r = postcode;
        } else if (level == UKP_Data.TYPE_SECTOR) {
            r = Postcode_Handler.getPostcodeSector(postcode);
        } else if (level == UKP_Data.TYPE_DISTRICT) {
            r = Postcode_Handler.getPostcodeDistrict(postcode);
        } else {
            // Unrecognised level
            env.ge.log("Unrecognised level in " + this.getClass().getName()
                    + ".getAreaCode(String,String,TreeMap<String, String>)");
        }
        return r;
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
    public String getAreaCode(Integer level,
            TreeMap<String, String> LookupFromPostcodeToCensusCode,
            String ClaimPostcodeF1) {
        String r = "";
        // Special Case
        if (ClaimPostcodeF1.trim().isEmpty()) {
            return r;
        }
        if (level == UKP_Data.TYPE_OA || level == UKP_Data.TYPE_LSOA
                || level == UKP_Data.TYPE_MSOA) {
            r = LookupFromPostcodeToCensusCode.get(ClaimPostcodeF1);
            if (r == null) {
                r = "";
            }
        } else if (level == UKP_Data.TYPE_UNIT) {
            r = ClaimPostcodeF1;
        } else if (level == UKP_Data.TYPE_SECTOR) {
            r = Postcode_Handler.getPostcodeSector(ClaimPostcodeF1);
        } else if (level == UKP_Data.TYPE_DISTRICT) {
            r = Postcode_Handler.getPostcodeDistrict(ClaimPostcodeF1);
        } else {
            // Unrecognised level
            int debug = 1;
        }
        return r;
    }
}
