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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Records;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Data;
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
                 * data. This is to be avaoided as we want to load first the
                 * SHBE and then load the UO data.
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
//        loadAllONSPDFromSource = true;
//        loadNewONSPDFromSource = true;
        loadAllSHBEFromSource = true;
//        loadNewSHBEFromSource = true;
//        loadSHBE = true;
//        runPostcodeCheckLatest = true;
//        runPostcodeCheck = true;
        loadUnderOccupancyFromSource = true;
//        loadUnderOccupancy = true;
        runLCCSummary = true;
        runRentArrears = true; newRentArrearsData = true;
        runLCCTenancyChangesUO = true; //Under-Occupancy Group Tables
//         runLCCHBGeneralAggregateStatistics = true;
//        runLCCTTAndPT = true;
//        doChoroplethMapping = true;
//        doLineMaps = true;
//        doReports = true;
//        doLineGraphs = true;
//        doDensityMaps = true;
//        doLineDensityMaps = true;
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
        if (loadAllONSPDFromSource || loadNewONSPDFromSource) {
            // Set up logging
            if (loadAllONSPDFromSource) {
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
        if (loadAllSHBEFromSource) {
            // Set up logging
            processName = "loadAllSHBEFromSource";
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
        if (loadNewSHBEFromSource) {
            // Set up logging
            processName = "loadNewSHBEFromSource";
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
        if (runPostcodeCheck) {
            // Set up logging
            processName = "runPostcodeCheck";
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
        if (runPostcodeCheckLatest) {
            // Set up logging
            processName = "runPostcodeCheckLatest";
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
        if (loadSHBE) {
            // Set up logging
            processName = "loadSHBE";
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
        if (loadUnderOccupancyFromSource) {
            // Set up logging
            processName = "loadUnderOccupancyFromSource";
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

        if (loadUnderOccupancy) {
            // Set up logging
            processName = "loadUnderOccupancy";
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

        if (runLCCSummary) {
            // Set up logging
            processName = "runLCCSummary";
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

        if (runRentArrears) {
            // Set up logging
            processName = "runRentArrears";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_ProcessorLCCRentArrears p;
            p = new DW_ProcessorLCCRentArrears(env);
            p.run(newRentArrearsData);
            // Close logs
            closeLogs(processName);
        }

        if (runLCCHBGeneralAggregateStatistics) {
            // Set up logging
            processName = "runLCCHBGeneralAggregateStatistics";
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

        if (runLCCTenancyChangesUO) {
            // Set up loggingprocess = "doTenancyChangesUO";
            processName = "runLCCTenancyChangesUO";
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

        if (runLCCTTAndPT) {
            // Set up logging
            processName = "runLCCTTAndPT";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_ProcessorLCCTTAndPT p;
            p = new DW_ProcessorLCCTTAndPT(env);
            p.run();
            // Close logs
            closeLogs(processName);
        }

        if (doChoroplethMapping) {
            // Set up logging
            processName = "doChoroplethMapping";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_ChoroplethMapsLCC aDW_ChoroplethMaps_LCC;
            aDW_ChoroplethMaps_LCC = new DW_ChoroplethMapsLCC(env);
            aDW_ChoroplethMaps_LCC.run();
            // Close logs
            closeLogs(processName);
        }

        if (doLineMaps) {
            // Set up logging
            processName = "doLineMaps";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_LineMapsLCC aDW_LineMaps_LCC;
            aDW_LineMaps_LCC = new DW_LineMapsLCC(env);
            aDW_LineMaps_LCC.run();
            boolean underOccupancy;
            underOccupancy = true;
            aDW_LineMaps_LCC.run2(underOccupancy, true);
            aDW_LineMaps_LCC.run2(underOccupancy, false);
            aDW_LineMaps_LCC.run2(false, false);
            aDW_LineMaps_LCC.run();
            // Close logs
            closeLogs(processName);
        }

        if (doReports) {
            // Set up logging
            processName = "doReports";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_Report aDW_Report;
            aDW_Report = new DW_Report(env);
            aDW_Report.run();
            // Close logs
            closeLogs(processName);
        }

        if (doLineGraphs) {
            // Set up logging
            processName = "doLineGraphs";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_LineGraph aDW_LineGraph;
            aDW_LineGraph = new DW_LineGraph(env);
            //aDW_LineGraph.start();
            aDW_LineGraph.run();
            // Close logs
            closeLogs(processName);
        }

        if (doDensityMaps) {
            // Set up logging
            processName = "doDensityMaps";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_DensityMapsLCC aDW_DensityMaps_LCC;
            aDW_DensityMaps_LCC = new DW_DensityMapsLCC(env);
            aDW_DensityMaps_LCC.run();
            // Close logs
            closeLogs(processName);
        }

        if (doLineDensityMaps) {
            // Set up logging
            processName = "doLineDensityMaps";
            File logDir = initLogs(
                    env.DEBUG_Level_FINE,
                    processName,
                    range);
            // Process
            DW_LineDensityMaps_LCC aDW_LineDensityMaps_LCC;
            aDW_LineDensityMaps_LCC = new DW_LineDensityMaps_LCC(env);
            aDW_LineDensityMaps_LCC.run();
            DW_LineDensityDifferenceMaps_LCC aDW_LineDensityDifferenceMaps_LCC;
            aDW_LineDensityDifferenceMaps_LCC = new DW_LineDensityDifferenceMaps_LCC(env);
            aDW_LineDensityDifferenceMaps_LCC.run();
            // Close logs
            closeLogs(processName);
        }
    }

    /**
     * Initialises env logging PrintWriters and returns the directory in which
     * logs are written. The directory is in an archive structure where the
     * number of directories or files in the archive (which is a growing
     * structure) is range.
     *
     * @param DEBUG_Level The debugging level - used to control how much is
     * written to the logs about the process. The following DEBUG_Levels are
     * defined: DEBUG_Level_FINEST = 0, DEBUG_Level_FINE = 1, DEBUG_Level_NORMAL
     * = 2.
     * @param processName The name of the process used for the directory inside
     * DW_Files.getOutputSHBELogsDir().
     * @param range The number of directories or files in the archive where the
     * logs are stored.
     * @return
     */
    protected File initLogs(
            int DEBUG_Level,
            String processName,
            int range) {
        env.DEBUG_Level = DEBUG_Level;
        File dir;
        dir = new File(
                DW_Files.getOutputSHBELogsDir(),
                processName);
        if (dir.isDirectory()) {
            dir = Generic_StaticIO.addToArchive(dir, 100);
        } else {
            dir = Generic_StaticIO.initialiseArchive(dir, 100);
        }
        dir.mkdirs();
        File fO;
        fO = new File(dir,
                "Out.txt");
        PrintWriter PrintWriterO;
        PrintWriterO = null;
        try {
            PrintWriterO = new PrintWriter(fO);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_ProcessorLCC.class.getName()).log(Level.SEVERE, null, ex);
        }
        env.setPrintWriterOut(PrintWriterO);
        File fE;
        fE = new File(dir,
                "Err.txt");
        PrintWriter PrintWriterE;
        PrintWriterE = null;
        try {
            PrintWriterE = new PrintWriter(fE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_ProcessorLCC.class.getName()).log(Level.SEVERE, null, ex);
        }
        env.setPrintWriterErr(PrintWriterE);

        env.log("DEBUG_Level = " + env.DEBUG_Level);
        env.log("env.DEBUG_Level_FINEST = " + env.DEBUG_Level_FINEST);
        env.log("env.DEBUG_Level_FINE = " + env.DEBUG_Level_FINE);
        env.log("env.DEBUG_Level_NORMAL = " + env.DEBUG_Level_NORMAL);
        env.log("<" + processName + ">");
        return dir;
    }

    /**
     * Closes env logging PrintWriters.
     *
     * @param processName
     */
    protected void closeLogs(String processName) {
        env.log("</" + processName + ">");
        env.getPrintWriterOut().close();
        env.getPrintWriterErr().close();
    }

    /**
     * Switch for loading All ONSPD Data From Source.
     */
    boolean loadAllONSPDFromSource = false;

    /**
     * Switch for loading New ONSPD Data From Source.
     */
    boolean loadNewONSPDFromSource = false;

    /**
     * Switch for loading SHBE Data From Source.
     */
    boolean loadAllSHBEFromSource = false;

    /**
     * Switch for reformatting all the SHBE data.
     */
    boolean reformatAll = false;

    /**
     * Switch for printing stats of reformatted SHBE data.
     */
    boolean stat = false;

    /**
     * Switch for reformatting new SHBE data that might have been added to the
     * input collection.
     */
    boolean loadNewSHBEFromSource = false;

    /**
     * Switch for loading SHBE Data.
     */
    boolean loadSHBE = false;

    /**
     * Switch for SHBE postcode check run.
     */
    boolean runPostcodeCheck = false;
    boolean runPostcodeCheckLatest = false;
    /**
     * Switch for loading Under Occupancy Data From Source.
     */
    boolean loadUnderOccupancyFromSource = false;

    /**
     * Switch for loading Under Occupancy Data From Source.
     */
    boolean loadUnderOccupancy = false;

    /**
     * Switch for running LCC Summary processing algorithms.
     */
    boolean runLCCSummary = false;
    boolean runRentArrears = false;
    boolean newRentArrearsData = false;
    boolean runLCCTenancyChangesUO = false;
    boolean runLCCHBGeneralAggregateStatistics = false;
    /**
     * Switch for running LCC Data processing algorithms.
     */
    boolean runLCCTTAndPT = false;

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
     * Switch for generating Density Maps.
     */
    boolean doDensityMaps = false;

    /**
     * Switch for generating Line Density Maps.
     */
    boolean doLineDensityMaps = false;

    /**
     * @param levels A set of levels expected values include OA, LSOA, MSOA,
     * PostcodeUnit, PostcodeSector, PostcodeDistrict.
     * @param YM3
     * @return A set of look ups from postcodes to each level input in levels.
     */
    public TreeMap<String, TreeMap<String, String>> getLookupsFromPostcodeToLevelCode(
            ArrayList<String> levels,
            String YM3) {
        TreeMap<String, TreeMap<String, String>> result;
        result = new TreeMap<String, TreeMap<String, String>>();
        Iterator<String> ite = levels.iterator();
        while (ite.hasNext()) {
            String level = ite.next();
            //            Iterate over YM3
            TreeMap<String, String> LookupFromPostcodeToLevelCode;
            LookupFromPostcodeToLevelCode = getLookupFromPostcodeToLevelCode(env, level, YM3);
            result.put(level, LookupFromPostcodeToLevelCode);
        }
        return result;
    }
}
