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

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.reporting.DW_Report;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.charts.DW_BarChart;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.charts.DW_LineGraph;

/**
 *
 * @author geoagdt
 */
public class DW_MainProcessor extends DW_AbstractProcessor {

    public DW_MainProcessor(DW_Environment env) {
        this.env = env;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                System.err.println(
                        "Expected an argument which is the location "
                        + "of the directory containing the input data. "
                        + "Aborting.");
                System.exit(0);
            } else {
                DW_Environment env;
                env = new DW_Environment(args[0]);
                new DW_MainProcessor(env).run();
            }
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

    /**
     * This is the main run method for the Digital welfare project.
     *
     * @throws Exception
     */
    public void run() throws Exception {
        /**
         * Switch for running Postcode Data preprocessing code.
         */
        boolean runPostcode;
        /**
         * Switches for running SHBE Data preprocessing code.
         */
        boolean runSHBE;
        boolean runSHBE_FormatAll;
        boolean runSHBE_CountUnique;
        boolean runSHBE_FormatNew;
        /**
         * Switch for running Under Occupancy Data preprocessing code.
         */
        boolean runUO;
        /**
         * Switch for running LCC processing code.
         */
        boolean runDataProcessor_LCC;
        /**
         * Set switches.
         */
        runPostcode = true;
        runPostcode = false;
        runSHBE = true;
//        runSHBE = false;
        runSHBE_FormatAll = true;
//        runSHBE_FormatAll = false;
        runSHBE_CountUnique = true;
        runSHBE_CountUnique = false;
        runSHBE_FormatNew = true;
        runSHBE_FormatNew = false;
        runUO = true;
        runUO = false;
        runDataProcessor_LCC = true;
        runDataProcessor_LCC = false;

        /**
         * Postcode preprocessing.
         */
        if (runPostcode) {
            System.out.println("<Postcode prerocessing>");
            DW_Postcode_Handler = new DW_Postcode_Handler(env);
            DW_Postcode_Handler.run();
            System.out.println("</Postcode prerocessing>");
        }

        /**
         * SHBE preprocessing.
         */
        if (runSHBE) {
            System.out.println("<SHBE preprocessing>");
            DW_SHBE_Handler DW_SHBE_Handler;
            DW_SHBE_Handler = env.getDW_SHBE_Handler();
            /**
             * Reformat all SHBE data from source.
             */
            if (runSHBE_FormatAll) {
                DW_SHBE_Handler.run();
            }
            /**
             * Count and report unique National Insurance Numbers and unique
             * person IDs so far encountered.
             */
            if (runSHBE_CountUnique) {
                DW_SHBE_Handler.runCount();
            }
            /**
             * Format data not already formatted. This assigns IDs with the
             * assumption being that the new data is for a subsequent period
             * than the existing data. If the data is not subsequent, then it is
             * probably best to reformat all SHBE data from source.
             */
            if (runSHBE_FormatNew) {
                 DW_SHBE_Handler.runNew();
            }
            System.out.println("</SHBE preprocessing>");
        }

        /**
         * Format UnderOccupancy data.
         */
        if (runUO) {
            System.out.println("<runUnderOccupancy_Handler>");
            boolean reload;
            reload = false;
            DW_UO_Data DW_UO_Data;
            DW_UO_Data = env.getDW_UO_Data();
            System.out.println("</runUnderOccupancy_Handler>");
        }

        if (runDataProcessor_LCC) {
            System.out.println("<runDataProcessor_LCC>");
            DW_DataProcessor_LCC DW_DataProcessor_LCC;
            DW_DataProcessor_LCC = new DW_DataProcessor_LCC(env);
            DW_DataProcessor_LCC.run();
            System.out.println("</runDataProcessor_LCC>");
        }

//        DW_ChoroplethMaps_LCC aDW_ChoroplethMaps_LCC;
//        aDW_ChoroplethMaps_LCC = new DW_ChoroplethMaps_LCC();
//        aDW_ChoroplethMaps_LCC.run();
//
//        DW_LineMaps_LCC aDW_LineMaps_LCC;
//        aDW_LineMaps_LCC = new DW_LineMaps_LCC();
//        aDW_LineMaps_LCC.run();
////        DW_Report aDW_Report;
////        aDW_Report = new DW_Report();
////        aDW_Report.run();
//
//        DW_LineGraph aDW_LineGraph;
//        aDW_LineGraph = new DW_LineGraph();
//        aDW_LineGraph.run(args);
//        DW_DensityMaps_LCC aDW_DensityMaps_LCC;
//        aDW_DensityMaps_LCC = new DW_DensityMaps_LCC(env);
//        aDW_DensityMaps_LCC.run();
//        DW_LineDensityMaps_LCC aDW_LineDensityMaps_LCC;
//        aDW_LineDensityMaps_LCC = new DW_LineDensityMaps_LCC();
//        aDW_LineDensityMaps_LCC.run();
//        DW_LineDensityDifferenceMaps_LCC aDW_LineDensityDifferenceMaps_LCC;
//        aDW_LineDensityDifferenceMaps_LCC = new DW_LineDensityDifferenceMaps_LCC();
//        aDW_LineDensityDifferenceMaps_LCC.run();
//        boolean underOccupancy;
//        underOccupancy = true;
//        DW_LineMaps_LCC aDW_LineMaps_LCC;
//        aDW_LineMaps_LCC = new DW_LineMaps_LCC();
//        aDW_LineMaps_LCC.run2(underOccupancy, true);
//        aDW_LineMaps_LCC.run2(underOccupancy, false);
//        aDW_LineMaps_LCC.run2(false, false);
//        aDW_LineMaps_LCC.run();
    }
}
