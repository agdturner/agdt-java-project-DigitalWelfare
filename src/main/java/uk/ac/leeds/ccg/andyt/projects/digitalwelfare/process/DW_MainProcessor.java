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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.reporting.DW_Report;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.charts.DW_BarChart;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.charts.DW_LineGraph;

/**
 *
 * @author geoagdt
 */
public class DW_MainProcessor {

    private transient final DW_Environment env;

    public DW_MainProcessor(DW_Environment env) {
        this.env = env;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            DW_Environment env = new DW_Environment();
            new DW_MainProcessor(env).run();
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

    public void run() {
        //            DW_Postcode_Handler aDW_Postcode_Handler;
//            aDW_Postcode_Handler = new DW_Postcode_Handler();
//            aDW_Postcode_Handler.run();

//        DW_SHBE_Handler aDW_SHBE_Handler;
//        aDW_SHBE_Handler = new DW_SHBE_Handler(env);
//        aDW_SHBE_Handler.run();
//            aDW_SHBE_Handler.runNew();
//
            DW_DataProcessor_LCC aDW_DataProcessor_LCC;
            aDW_DataProcessor_LCC = new DW_DataProcessor_LCC(env);
            aDW_DataProcessor_LCC.run();
//////            
////////            DW_ChoroplethMaps_LCC aDW_ChoroplethMaps_LCC;
////////            aDW_ChoroplethMaps_LCC = new DW_ChoroplethMaps_LCC();
////////            aDW_ChoroplethMaps_LCC.run();
////
////            DW_LineMaps_LCC aDW_LineMaps_LCC;
////            aDW_LineMaps_LCC = new DW_LineMaps_LCC();
////            aDW_LineMaps_LCC.run();
////            
////////            DW_Report aDW_Report;
////////            aDW_Report = new DW_Report();
////////            aDW_Report.run();
////            
//            DW_LineGraph aDW_LineGraph;
//            aDW_LineGraph = new DW_LineGraph();
//            aDW_LineGraph.run(args);
//
//            DW_DensityMaps_LCC aDW_DensityMaps_LCC;
//            aDW_DensityMaps_LCC = new DW_DensityMaps_LCC();
//            aDW_DensityMaps_LCC.run();

//            DW_LineDensityMaps_LCC aDW_LineDensityMaps_LCC;
//            aDW_LineDensityMaps_LCC = new DW_LineDensityMaps_LCC();
//            aDW_LineDensityMaps_LCC.run();
//            DW_LineDensityDifferenceMaps_LCC aDW_LineDensityDifferenceMaps_LCC;
//            aDW_LineDensityDifferenceMaps_LCC = new DW_LineDensityDifferenceMaps_LCC();
//            aDW_LineDensityDifferenceMaps_LCC.run();
//            boolean underOccupancy;
//            underOccupancy = true;
//            DW_LineMaps_LCC aDW_LineMaps_LCC;
//            aDW_LineMaps_LCC = new DW_LineMaps_LCC();
//            aDW_LineMaps_LCC.run2(underOccupancy,true);
//            aDW_LineMaps_LCC.run2(underOccupancy,false);
//            aDW_LineMaps_LCC.run2(false,false);            
//            aDW_LineMaps_LCC.run(args);
    }
}
