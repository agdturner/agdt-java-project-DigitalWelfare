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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Records;
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
    protected transient DW_SHBE_Data DW_SHBE_Data;
    protected transient DW_SHBE_Handler DW_SHBE_Handler;
    protected transient DW_UO_Data DW_UO_Data;
    protected transient String[] SHBEFilenames;
    protected transient HashMap<DW_ID, String> ClaimIDToClaimRefLookup;

    protected DW_ProcessorLCC() {
    }

    public DW_ProcessorLCC(DW_Environment env) {
        super(env);
        DW_SHBE_Data = env.getDW_SHBE_Data();
        DW_SHBE_Handler = env.getDW_SHBE_Handler();
        DW_UO_Data = env.getDW_UO_Data();
        SHBEFilenames = DW_SHBE_Handler.getSHBEFilenamesAll();
        ClaimIDToClaimRefLookup = DW_SHBE_Data.getClaimIDToClaimRefLookup();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.err.println(
                        "Expected an argument which is the location "
                        + "of the directory containing the input data. "
                        + "Aborting.");
                System.exit(0);
            } else {
                DW_Environment env = new DW_Environment(
                        new Integer(args[0]),
                        args[1]);
                DW_ProcessorLCC DW_ProcessorLCC;
                DW_ProcessorLCC = new DW_ProcessorLCC();
                DW_ProcessorLCC.env = env;
                DW_ProcessorLCC.DW_Files = env.getDW_Files();
                DW_ProcessorLCC.DW_Strings = env.getDW_Strings();
                DW_ProcessorLCC.run();
                /**
                 * Not done this way as this would first load UnderOccupancy
                 * data. This is to be avoided as we want to load first the SHBE
                 * and then load the UO data.
                 */
                // new DW_ProcessorLCC(env).run();

            }
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace(System.err);
//            StackTraceElement[] stes = e.getStackTrace();
//            for (StackTraceElement ste : stes) {
//                System.err.println(ste.toString());
//            }
        } catch (Error e) {
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
    public void run() throws Exception {
        /*
         * Set which parts of the code to run. There is a logical order to these
         * in that some parts have to have been run before others will produce 
         * results as expected.
         */
        RunAllFromScratch = false;
        RunAllFromUpdate = false;
        if (!RunAllFromScratch) {
            if (!RunAllFromUpdate) {
                // Choose which bits to run
//                LoadAllONSPDFromSource = true;
//                LoadNewONSPDFromSource = true;
//                LoadAllSHBEFromSource = true;
//                LoadNewSHBEFromSource = true;
//                LoadSHBE = true;
//                PostcodeCheckLatest = true;
//                PostcodeCheck = true;
//                LoadUnderOccupancyFromSource = true;
//                LoadUnderOccupancy = true;
//                LCCSummary = true;
//                RentArrears = true;
//                RentArrearsNewData = true;
//                LCCTenancyChangesUO = true; //Under-Occupancy Group Tables
                LCCHBGeneralAggregateStatistics = true;
//                LCCTTAndPT = true;
//                if (LCCTTAndPT) {
//                    LCCTTAndPT_DoGrouped = true;
//                    LCCTTAndPT_DoPostcodeChanges = true;
//                    LCCTTAndPT_DoAnyTenancyChanges = true;
//                    LCCTTAndPT_DoTenancyChanges = true;
//                    LCCTTAndPT_DoTenancyAndPostcodeChanges = true;
//                }
//                ChoroplethMapping = true;
//                LineMaps = true;
//                Reports = true;
                LineGraphs = true;
                if (LineGraphs) {
                    LineGraphTenancyTypeTransitions = true;
                    //LineGraphAggregateData = true;
                }
//                DensityMaps = true;
//                LineDensityMaps = true;
            } else {
                //loadNewONSPDFromSource = true;
                LoadNewSHBEFromSource = true;
                LoadUnderOccupancyFromSource = true;
                LCCSummary = true;
                RentArrears = true;
                RentArrearsNewData = true;
                LCCTenancyChangesUO = true; //Under-Occupancy Group Tables
                LCCHBGeneralAggregateStatistics = true; // Could do with a new method that only runs the aggregated data that has not already been produced.
                LCCTTAndPT = true;
                ChoroplethMapping = true;
                LineMaps = true;
                Reports = true;
                LineGraphs = true;
                DensityMaps = true;
                LineDensityMaps = true;
            }
        } else {
            LoadAllONSPDFromSource = true;
            LoadAllSHBEFromSource = true;
            LoadUnderOccupancyFromSource = true;
            LCCSummary = true;
            RentArrears = true;
            RentArrearsNewData = true;
            LCCTenancyChangesUO = true; //Under-Occupancy Group Tables
            LCCHBGeneralAggregateStatistics = true;
            LCCTTAndPT = true;
            ChoroplethMapping = true;
            LineMaps = true;
            Reports = true;
            LineGraphs = true;
            DensityMaps = true;
            LineDensityMaps = true;
        }
        // range is the number of directories or files in any archives.
        int range;
        range = 100;
        process(range);
    }

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
        if (LoadAllONSPDFromSource || LoadNewONSPDFromSource) {
            // Set up logging
            if (LoadAllONSPDFromSource) {
                processName = "LoadAllONSPDFromSource";
            } else {
                processName = "LoadNewONSPDFromSource";
            }
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_Postcode_Handler = new DW_Postcode_Handler(env);
            env.setDW_Postcode_Handler(DW_Postcode_Handler);
            DW_Postcode_Handler.run(logDir);
            // Close logs
            closeLogs(processName);
        }

        /**
         * Format All SHBE data. This checks and formats postcodes.
         */
        if (LoadAllSHBEFromSource) {
            // Set up logging
            processName = "LoadAllSHBEFromSource";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_SHBE_Handler = new DW_SHBE_Handler(env);
            env.setDW_SHBE_Handler(DW_SHBE_Handler);
            DW_SHBE_Handler.run(logDir);
            // Close logs
            closeLogs(processName);
        }

        /**
         * Format New SHBE data. This checks and formats postcodes.
         */
        if (LoadNewSHBEFromSource) {
            // Set up logging
            processName = "LoadNewSHBEFromSource";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_SHBE_Handler = new DW_SHBE_Handler(env);
            env.setDW_SHBE_Handler(DW_SHBE_Handler);
            DW_SHBE_Handler.runNew(logDir);
            // Close logs
            closeLogs(processName);
        }

        /**
         * Check SHBE postcodes.
         */
        if (PostcodeCheck) {
            // Set up logging
            processName = "PostcodeCheck";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_SHBE_Handler = new DW_SHBE_Handler(env);
            env.setDW_SHBE_Handler(DW_SHBE_Handler);
            DW_SHBE_Handler.runPostcodeCheck(logDir);
            // Close logs
            closeLogs(processName);
        }

        /**
         * Check SHBE postcodes.
         */
        if (PostcodeCheckLatest) {
            // Set up logging
            processName = "PostcodeCheckLatest";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_SHBE_Handler = new DW_SHBE_Handler(env);
            env.setDW_SHBE_Handler(DW_SHBE_Handler);
            DW_SHBE_Handler.runPostcodeCheckLatest(logDir);
            // Close logs
            closeLogs(processName);
        }

        /**
         * Load SHBE data. This loads already formatted SHBE data. This process
         * is for checking the data loads. It could be used as a basis for
         * testing the speed of loading and for memory requirements.
         */
        if (LoadSHBE) {
            // Set up logging
            processName = "LoadSHBE";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_SHBE_Data = env.getDW_SHBE_Data();
            HashMap<String, DW_SHBE_Records> Data;
            Data = DW_SHBE_Data.getData();
            DW_SHBE_Handler = new DW_SHBE_Handler(env);
            env.setDW_SHBE_Handler(DW_SHBE_Handler);
            SHBEFilenames = DW_SHBE_Handler.getSHBEFilenamesAll();
            File dir;
            dir = env.getDW_Files().getGeneratedSHBEDir();
            String YM3;
            for (String SHBEFilename : SHBEFilenames) {
                YM3 = DW_SHBE_Handler.getYM3(SHBEFilename);
                try {
                    DW_SHBE_Records DW_SHBE_Records;
                    DW_SHBE_Records = new DW_SHBE_Records(
                            env,
                            YM3);
                    env.tryToEnsureThereIsEnoughMemoryToContinue(env._HandleOutOfMemoryError_boolean);
                    Data.put(YM3, DW_SHBE_Records);
                    env.logO("DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateInPayment().size() "
                            + DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateInPayment(env._HandleOutOfMemoryError_boolean).size(), true);
                    env.logO("DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateSuspended().size() "
                            + DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateSuspended(env._HandleOutOfMemoryError_boolean).size(), true);
                    env.logO("DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateOther().size() "
                            + DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateOther(env._HandleOutOfMemoryError_boolean).size(), true);
                    env.logO("DW_SHBE_Records.getClaimIDsWithStatusOfHBAtExtractDateInPayment().size() "
                            + DW_SHBE_Records.getClaimIDsWithStatusOfCTBAtExtractDateInPayment(env._HandleOutOfMemoryError_boolean).size(), true);
                    env.logO("DW_SHBE_Records.getClaimIDsWithStatusOfCTBAtExtractDateSuspended().size() "
                            + DW_SHBE_Records.getClaimIDsWithStatusOfCTBAtExtractDateSuspended(env._HandleOutOfMemoryError_boolean).size(), true);
                    env.logO("DW_SHBE_Records.getClaimIDsWithStatusOfCTBAtExtractDateOther().size() "
                            + DW_SHBE_Records.getClaimIDsWithStatusOfCTBAtExtractDateOther(env._HandleOutOfMemoryError_boolean).size(), true);
                    DW_SHBE_Records.clearData();
                } catch (OutOfMemoryError e) {
                    env.clear_MemoryReserve();
                    env.clearSomeSHBECacheExcept(YM3);
                    env.init_MemoryReserve(env._HandleOutOfMemoryError_boolean);
                    env.tryToEnsureThereIsEnoughMemoryToContinue(env._HandleOutOfMemoryError_boolean);
                }
            }
            // Close logs
            closeLogs(processName);
        }

        /**
         * Load UnderOccupancy data from source.
         */
        if (LoadUnderOccupancyFromSource) {
            // Set up logging
            processName = "LoadUnderOccupancyFromSource";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            boolean loadFromSource;
            loadFromSource = true;
            DW_UO_Data = env.getDW_UO_Data(loadFromSource);
            // Close logs
            closeLogs(processName);
        }

        if (LoadUnderOccupancy) {
            // Set up logging
            processName = "LoadUnderOccupancy";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            boolean loadFromSource;
            loadFromSource = false;
            DW_UO_Data = env.getDW_UO_Data(loadFromSource);
            // Close logs
            closeLogs(processName);
        }

        if (LCCSummary) {
            // Set up logging
            processName = "LCCSummary";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_ProcessorLCCSummary p;
            p = new DW_ProcessorLCCSummary(env);
            p.run();
            // Close logs
            closeLogs(processName);
        }

        if (RentArrears) {
            // Set up logging
            processName = "RentArrears";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_ProcessorLCCRentArrears p;
            p = new DW_ProcessorLCCRentArrears(env);
            p.run(RentArrearsNewData);
            // Close logs
            closeLogs(processName);
        }

        if (LCCHBGeneralAggregateStatistics) {
            // Set up logging
            processName = "LCCHBGeneralAggregateStatistics";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_ProcessorLCCHBGeneralAggregateStatistics p;
            p = new DW_ProcessorLCCHBGeneralAggregateStatistics(env);
            p.run();
            // Close logs
            closeLogs(processName);
        }

        if (LCCTenancyChangesUO) {
            // Set up logging
            processName = "LCCTenancyChangesUO";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_ProcessorLCCTenancyChangesUO p;
            p = new DW_ProcessorLCCTenancyChangesUO(env);
            p.run();
            // Close logs
            closeLogs(processName);
        }

        if (LCCTTAndPT) {
            // Set up logging
            processName = "LCCTTAndPT";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_ProcessorLCCTTAndPT p;
            p = new DW_ProcessorLCCTTAndPT(env);
////            
////            // <DEBUG skip to a particular output creation>
//            p.run(
//                    true,
//                    false,
//                    true,
//                    false,
//                    true);
//            System.exit(0);
////            // </DEBUG skip to a particular output creation>
////
            ArrayList<Boolean> b;
            b = getArrayListBoolean();
            Iterator<Boolean> iteb0;
            Iterator<Boolean> iteb1;
            Iterator<Boolean> iteb2;
            Iterator<Boolean> iteb3;
            Iterator<Boolean> iteb4;
            LCCTTAndPT_DoGrouped = true;
            //LCCTTAndPT_DoGrouped = false;
            iteb1 = b.iterator();
            while (iteb1.hasNext()) {
                LCCTTAndPT_DoPostcodeChanges = iteb1.next();
                iteb2 = b.iterator();
                while (iteb2.hasNext()) {
                    LCCTTAndPT_DoAnyTenancyChanges = iteb2.next();
                    iteb3 = b.iterator();
                    while (iteb3.hasNext()) {
                        LCCTTAndPT_DoTenancyChanges = iteb3.next();
                        iteb4 = b.iterator();
                        while (iteb4.hasNext()) {
                            LCCTTAndPT_DoTenancyAndPostcodeChanges = iteb4.next();
                            p.run(LCCTTAndPT_DoGrouped,
                                    LCCTTAndPT_DoPostcodeChanges,
                                    LCCTTAndPT_DoAnyTenancyChanges,
                                    LCCTTAndPT_DoTenancyChanges,
                                    LCCTTAndPT_DoTenancyAndPostcodeChanges);
                        }
                    }
                }
            }
            // Close logs
            closeLogs(processName);
        }

        if (ChoroplethMapping) {
            // Set up logging
            processName = "ChoroplethMapping";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_ChoroplethMapsLCC p;
            p = new DW_ChoroplethMapsLCC(env);
            p.run();
            // Close logs
            closeLogs(processName);
        }

        if (LineMaps) {
            // Set up logging
            processName = "LineMaps";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
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
            closeLogs(processName);
        }

        if (Reports) {
            // Set up logging
            processName = "Reports";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_Report p;
            p = new DW_Report(env);
            p.run();
            // Close logs
            closeLogs(processName);
        }

        if (LineGraphs) {
            // Set up logging
            processName = "LineGraphs";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_LineGraph p;
            p = new DW_LineGraph(env);
            //aDW_LineGraph.start();
            p.run(LineGraphTenancyTypeTransitions,
                    LineGraphAggregateData);
            // Close logs
            closeLogs(processName);
        }

        if (DensityMaps) {
            // Set up logging
            processName = "DensityMaps";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_DensityMapsLCC p;
            p = new DW_DensityMapsLCC(env);
            p.run();
            // Close logs
            closeLogs(processName);
        }

        if (LineDensityMaps) {
            // Set up logging
            processName = "LineDensityMaps";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_LineDensityMaps_LCC p;
            p = new DW_LineDensityMaps_LCC(env);
            p.run();
            DW_LineDensityDifferenceMaps_LCC p2;
            p2 = new DW_LineDensityDifferenceMaps_LCC(env);
            p2.run();
            // Close logs
            closeLogs(processName);
        }
    }

    /**
     * Switch for loading All ONSPD Data From Source.
     */
    boolean LoadAllONSPDFromSource = false;

    /**
     * Switch for loading New ONSPD Data From Source.
     */
    boolean LoadNewONSPDFromSource = false;

    /**
     * Switch for loading SHBE Data From Source.
     */
    boolean LoadAllSHBEFromSource = false;

    /**
     * Switch for reformatting all the SHBE data.
     */
    boolean ReformatAll = false;

    /**
     * Switch for printing stats of reformatted SHBE data.
     */
    boolean Stat = false;

    /**
     * Switch for reformatting new SHBE data that might have been added to the
     * input collection.
     */
    boolean LoadNewSHBEFromSource = false;

    /**
     * Switch for loading SHBE Data.
     */
    boolean LoadSHBE = false;

    /**
     * Switch for SHBE postcode check run.
     */
    boolean PostcodeCheck = false;
    boolean PostcodeCheckLatest = false;
    /**
     * Switch for loading Under Occupancy Data From Source.
     */
    boolean LoadUnderOccupancyFromSource = false;

    /**
     * Switch for loading Under Occupancy Data From Source.
     */
    boolean LoadUnderOccupancy = false;

    /**
     * Switch for running LCC Summary processing algorithms.
     */
    boolean LCCSummary = false;

    /**
     * Switch for running Rent Arrears processing algorithms.
     */
    boolean RentArrears = false;
    boolean RentArrearsNewData = false;

    /**
     * Switch for running LCCTenancyChangesUO.
     */
    boolean LCCTenancyChangesUO = false;

    /**
     * Switch for running LCCHBGeneralAggregateStatistics.
     */
    boolean LCCHBGeneralAggregateStatistics = false;

    /**
     * Switch for running LCC Data processing algorithms.
     */
    boolean LCCTTAndPT = false;

    boolean LCCTTAndPT_DoGrouped;
    boolean LCCTTAndPT_DoPostcodeChanges;
    boolean LCCTTAndPT_DoAnyTenancyChanges;
    boolean LCCTTAndPT_DoTenancyChanges;
    boolean LCCTTAndPT_DoTenancyAndPostcodeChanges;

    /**
     * Switch for running Choropleth Mapping code.
     */
    boolean ChoroplethMapping = false;

    /**
     * Switch for running Line Mapping code.
     */
    boolean LineMaps = false;

    /**
     * Switch for running report generation code.
     */
    boolean Reports = false;

    /**
     * Switch for generating Line Graphs.
     */
    boolean LineGraphs = false;

    /**
     * Switch for generating Tenancy Type Transition Line Graphs.
     */
    boolean LineGraphTenancyTypeTransitions = true;

    /**
     * Switch for generating Tenancy Type Transition Line Graphs.
     */
    boolean LineGraphAggregateData = true;

    /**
     * Switch for generating Density Maps.
     */
    boolean DensityMaps = false;

    /**
     * Switch for generating Line Density Maps.
     */
    boolean LineDensityMaps = false;

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
            ArrayList<String> levels,
            String YM3,
            int CensusYear) {
        TreeMap<String, TreeMap<String, String>> result;
        result = new TreeMap<String, TreeMap<String, String>>();
        Iterator<String> ite = levels.iterator();
        while (ite.hasNext()) {
            String level = ite.next();
            //            Iterate over YM3
            TreeMap<String, String> ClaimPostcodeF_To_LevelCode_Map;
            ClaimPostcodeF_To_LevelCode_Map = getClaimPostcodeF_To_LevelCode_Map(env, level, CensusYear, YM3);
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
        if (level.equalsIgnoreCase(DW_Strings.sOA) || level.equalsIgnoreCase(DW_Strings.sLSOA) || level.equalsIgnoreCase(DW_Strings.sMSOA) || level.equalsIgnoreCase(DW_Strings.sStatisticalWard) || level.equalsIgnoreCase(DW_Strings.sParliamentaryConstituency)) {
            String formattedPostcode = DW_Postcode_Handler.formatPostcode(postcode);
            result = LookupFromPostcodeToCensusCode.get(formattedPostcode);
            if (result == null) {
                result = "";
            }
        } else if (level.equalsIgnoreCase(DW_Strings.sPostcodeUnit) || level.equalsIgnoreCase(DW_Strings.sPostcodeSector) || level.equalsIgnoreCase(DW_Strings.sPostcodeDistrict)) {
            if (level.equalsIgnoreCase(DW_Strings.sPostcodeUnit)) {
                result = postcode;
            }
            if (level.equalsIgnoreCase(DW_Strings.sPostcodeSector)) {
                result = DW_Postcode_Handler.getPostcodeSector(postcode);
            }
            if (level.equalsIgnoreCase(DW_Strings.sPostcodeDistrict)) {
                result = DW_Postcode_Handler.getPostcodeDistrict(postcode);
            }
        } else {
            // Unrecognised level
            env.log("Unrecognised level in " + this.getClass().getName() + ".getAreaCode(String,String,TreeMap<String, String>)");
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
                result = DW_Postcode_Handler.getPostcodeSector(ClaimPostcodeF1);
            }
            if (level.equalsIgnoreCase("PostcodeDistrict")) {
                result = DW_Postcode_Handler.getPostcodeDistrict(ClaimPostcodeF1);
            }
        } else {
            // Unrecognised level
            int debug = 1;
        }
        return result;
    }
}
