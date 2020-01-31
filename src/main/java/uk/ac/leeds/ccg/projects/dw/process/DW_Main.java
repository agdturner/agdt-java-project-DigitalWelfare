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
package uk.ac.leeds.ccg.projects.dw.process;

import java.io.File;
import uk.ac.leeds.ccg.andyt.data.core.Data_Environment;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.process.lcc.DW_ProcessorLCC;

/**
 * This is the main processing class for the project.
 *
 * @author Andy Turner
 */
public class DW_Main extends DW_Processor {

    public DW_Main(DW_Environment env) {
        super(env);
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
                Generic_Environment ge = new Generic_Environment(dataDir);
                Data_Environment de = new Data_Environment(ge);
                DW_Environment env = new DW_Environment(de);
                DW_Main p;
                p = new DW_Main(env);
//                p.env.SHBE_Env = new SHBE_Environment(p.files.getDataDir(), 
//                SHBE_Environment.DEBUG_Level_NORMAL);
//                p.env.ONSPD_Env = new ONSPD_Environment(p.files.getDataDir());
                p.run();
                env.ge.closeLog(0);
            }
        } catch (Exception | Error e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace(System.err);
            StackTraceElement[] stes = e.getStackTrace();
            for (StackTraceElement ste : stes) {
                System.err.println(ste.toString());
            }
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
