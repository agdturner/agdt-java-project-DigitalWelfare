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

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.lcc.*;
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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_ProcessorAbstract;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.reporting.DW_Report;
//import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.charts.DW_BarChart;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.charts.DW_LineGraph;

/**
 *
 * @author geoagdt
 */
public class DW_Processor extends DW_ProcessorAbstract {

    protected DW_Processor() {
    }

    public DW_Processor(DW_Environment env) {
        super(env);
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
                DW_Processor DW_ProcessorLCC;
                DW_ProcessorLCC = new DW_Processor();
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
        /**
         * Run Advice Leeds processing
         */
        
        /**
         * Run LCC SHBE data processing
         */
        DW_ProcessorLCC DW_ProcessorLCC;
        DW_ProcessorLCC = new DW_ProcessorLCC(env);
        DW_ProcessorLCC.run();
        
    }

}
