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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.lcc.DW_ProcessorLCC;

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
                DW_ProcessorLCC.df = env.getDW_Files();
                DW_ProcessorLCC.ds = env.getDW_Strings();
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
    public void run() throws Exception, Error {
        /**
         * Run Advice Leeds processing
         */
        
        /**
         * Run LCC SHBE data processing
         */
        DW_ProcessorLCC p;
        p = new DW_ProcessorLCC(env);
        p.run();
        
    }

}
