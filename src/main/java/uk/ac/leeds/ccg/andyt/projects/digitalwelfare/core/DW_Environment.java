/*
 * Copyright (C) 2016 geoagdt.
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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_ErrorAndExceptionHandler;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census.DW_Deprivation_DataHandler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Set;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.util.DW_YM3;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Geotools;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Maps;
import uk.ac.leeds.ccg.andyt.vector.core.Vector_Environment;

/**
 * This class is for a shared object, common to many objects in a Digital
 * Welfare program. It contains holders for commonly referred to objects that
 * might otherwise be constructed multiple times. It is also for handling memory
 * although for the time being, there has not been a need for convoluted
 * swapping of data from memory to disk.
 */
public class DW_Environment extends DW_OutOfMemoryErrorHandler
        implements Serializable {

    public String Directory;
    //public String Directory = "/scratch02/DigitalWelfare";
    //public String Directory = "C:/Users/geoagdt/projects/DigitalWelfare";

    /**
     * Logging levels.
     */
    public int DEBUG_Level;
    public final int DEBUG_Level_FINEST = 0;
    public final int DEBUG_Level_FINE = 1;
    public final int DEBUG_Level_NORMAL = 2;

    /**
     * For storing an instance of DW_Strings for accessing Strings.
     */
    private DW_Strings Strings;

    /**
     * For storing an instance of DW_Files for accessing filenames and Files
     * therein.
     */
    private DW_Files Files;

    /**
     * For storing an instance of Grids_Environment
     */
    protected transient Grids_Environment Grids_Env;

    /**
     * For storing an instance of Vector_Environment
     */
    protected transient Vector_Environment Vector_Env;

    /**
     * For storing an instance of DW_Geotools
     */
    protected transient DW_Geotools Geotools;

//    /**
//     * For storing an instance of HashMap<String, DW_SHBE_CollectionHandler>.
//     */
//    private HashMap<String, DW_SHBE_CollectionHandler> DW_SHBE_CollectionHandlers;
    /**
     * For storing an instance of UO_Handler for convenience.
     */
    protected DW_UO_Handler UO_Handler;

    /**
     * For storing an instance of UO_Data.
     */
    protected DW_UO_Data UO_Data;

    /**
     * For storing an instance of DW_Postcode_Handler for convenience.
     */
    private DW_Postcode_Handler Postcode_Handler;

    /**
     * For storing an instance of SHBE_Handler for convenience.
     */
    private DW_SHBE_Handler SHBE_Handler;

    /**
     * For storing an instance of SHBE_TenancyType_Handler for convenience.
     */
    private DW_SHBE_TenancyType_Handler SHBE_TenancyType_Handler;

    /**
     * For storing an instance of DW_Maps for convenience.
     */
    private DW_Maps Maps;

    /**
     * For storing an instance of Deprivation_DataHandler for convenience.
     */
    DW_Deprivation_DataHandler Deprivation_DataHandler;

    /**
     * For storing an instance of DW_SHBE_DataAll.
     */
    private DW_SHBE_Data SHBE_Data;

    public DW_Environment(int DEBUG_Level) {
        this.DEBUG_Level = DEBUG_Level;
        this.Files = new DW_Files(this);
        this.Strings = new DW_Strings();
    }

    public DW_Environment(int DEBUG_Level, String directory) {
        this.Directory = directory;
        this.DEBUG_Level = DEBUG_Level;
        this.Files = new DW_Files(this);
        this.Strings = new DW_Strings();
    }

    public int getDefaultMaximumNumberOfObjectsPerDirectory() {
        return 100;
    }

//    /**
//     * A method to ensure there is enough memory to continue.
//     *
//     * @param handleOutOfMemoryError
//     * @return
//     */
//    @Override
//    public boolean checkAndMaybeFreeMemory(
//            boolean handleOutOfMemoryError) {
//        try {
//            if (checkAndMaybeFreeMemory()) {
//                return true;
//            } else {
//                if (DEBUG_Level < DEBUG_Level_NORMAL) {
//                    String message
//                            = "Warning! No data to swap or clear in "
//                            + this.getClass().getName()
//                            + ".tryToEnsureThereIsEnoughMemoryToContinue(boolean)";
//                    System.out.println(message);
//                }
//                // Set to exit method with OutOfMemoryError
//                handleOutOfMemoryError = false;
//                throw new OutOfMemoryError();
//            }
//        } catch (OutOfMemoryError e) {
//            if (handleOutOfMemoryError) {
//                clearMemoryReserve();
//                boolean createdRoom = false;
//                while (!createdRoom) {
//                    if (!swapDataAny()) {
//                        if (DEBUG_Level < DEBUG_Level_NORMAL) {
//                            String message = "Warning! No data to swap or clear in "
//                                    + this.getClass().getName()
//                                    + ".tryToEnsureThereIsEnoughMemoryToContinue(boolean)";
//                            System.out.println(message);
//                        }
//                        throw e;
//                    }
//                    try {
//                        initMemoryReserve();
//                        createdRoom = true;
//                    } catch (OutOfMemoryError e2) {
//                        if (DEBUG_Level < DEBUG_Level_NORMAL) {
//                            String message
//                                    = "Struggling to ensure there is enough memory in "
//                                    + this.getClass().getName()
//                                    + ".tryToEnsureThereIsEnoughMemoryToContinue(boolean)";
//                            System.out.println(message);
//                        }
//                    }
//                }
//                return checkAndMaybeFreeMemory(
//                        handleOutOfMemoryError);
//            } else {
//                throw e;
//            }
//        }
//    }

    /**
     * A method to try to ensure there is enough memory to continue.
     *
     * @return
     */
    @Override
    public boolean checkAndMaybeFreeMemory() {
        while (getTotalFreeMemory() < Memory_Threshold) {
            if (!swapDataAny()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean swapDataAny(boolean handleOutOfMemoryError) {
        try {
            boolean result = swapDataAny();
            checkAndMaybeFreeMemory();
            return result;
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                clearMemoryReserve();
                boolean result = swapDataAny(HOOMEF);
                initMemoryReserve();
                return result;
            } else {
                throw e;
            }
        }
    }

    /**
     * Currently this just tries to swap a SHBE collection.
     *
     * @return
     */
    @Override
    public boolean swapDataAny() {
        boolean clearedSomeSHBECache;
        clearedSomeSHBECache = clearSomeSHBECache();
        if (clearedSomeSHBECache) {
            return clearedSomeSHBECache;
        } else {
            System.out.println("No SHBE data to clear. Do some coding to try "
                    + "to arrange to clear something else if needs be!!!");
            return clearedSomeSHBECache;
        }
    }

    /**
     * Clears the cache of all SHBE data.
     *
     * @return The number of sets of records cleared.
     */
    public int clearAllSHBECache() {
        return SHBE_Data.clearAllCache();
    }

    /**
     * Swaps to file a collection.
     *
     * @return True iff a collection is swapped.
     */
    public boolean clearSomeSHBECache() {
        return getSHBE_Data().clearSomeCache();
    }

    /**
     * Swaps to file a collection.
     *
     * @param YM3
     * @return True iff a collection is swapped.
     */
    public boolean clearSomeSHBECacheExcept(DW_YM3 YM3) {
        return getSHBE_Data().clearSomeCacheExcept(YM3);
    }

    /**
     * For writing output messages to.
     */
    private PrintWriter PrintWriterOut;

    /**
     * For writing error messages to.
     */
    private PrintWriter PrintWriterErr;

    public PrintWriter getPrintWriterOut() {
        return PrintWriterOut;
    }

    public void setPrintWriterOut(PrintWriter PrintWriterOut) {
        this.PrintWriterOut = PrintWriterOut;
    }

    public PrintWriter getPrintWriterErr() {
        return PrintWriterErr;
    }

    public void setPrintWriterErr(PrintWriter PrintWriterErr) {
        this.PrintWriterErr = PrintWriterErr;
    }

    /**
     * Writes s to a new line of the output log and error log and also prints it
     * to std.out.
     *
     * @param s
     */
    public void log(String s) {
        PrintWriterOut.println(s);
        PrintWriterErr.println(s);
        System.out.println(s);
    }

//    private static void log(
//            String message) {
//        log(DW_Log.DW_DefaultLogLevel, message);
//    }
//
//    private static void log(
//            Level level,
//            String message) {
//        Logger.getLogger(DW_Log.DW_DefaultLoggerName).log(level, message);
//    }
    /**
     * Writes s to a new line of the output log and also prints it to std.out.
     *
     * @param s
     * @param println
     */
    public void logO(String s, boolean println) {
        if (PrintWriterOut != null) {
            PrintWriterOut.println(s);
        }
        if (println) {
            System.out.println(s);
        }
    }

    /**
     * Writes s to a new line of the output log and also prints it to std.out if
     * {@code this.DEBUG_Level <= DEBUG_Level}.
     *
     * @param DEBUG_Level
     * @param s
     */
    public void logO(int DEBUG_Level, String s) {
        if (this.DEBUG_Level <= DEBUG_Level) {
            PrintWriterOut.println(s);
            System.out.println(s);
        }
    }

    /**
     * Writes s to a new line of the error log and also prints it to std.err.
     *
     * @param s
     */
    public void logE(String s) {
        if (PrintWriterErr != null) {
            PrintWriterErr.println(s);
        }
        System.err.println(s);
    }

    /**
     * Writes {@code e.getStackTrace()} to the error log and also prints it to
     * std.err.
     *
     * @param e
     */
    public void logE(Exception e) {
        StackTraceElement[] st;
        st = e.getStackTrace();
        for (StackTraceElement st1 : st) {
            logE(st1.toString());
        }
    }

    /**
     * Writes e StackTrace to the error log and also prints it to std.err.
     *
     * @param e
     */
    public void logE(Error e) {
        StackTraceElement[] st;
        st = e.getStackTrace();
        for (StackTraceElement st1 : st) {
            logE(st1.toString());
        }
    }

    /**
     * For returning an instance of DW_Strings for convenience.
     *
     * @return
     */
    public DW_Strings getStrings() {
        if (Strings == null) {
            Strings = new DW_Strings();
        }
        return Strings;
    }

    /**
     * For returning an instance of DW_Files for convenience.
     *
     * @return
     */
    public DW_Files getFiles() {
        if (Files == null) {
            Files = new DW_Files(this);
        }
        return Files;
    }

    /**
     * For returning an instance of Grids_Environment for convenience.
     *
     * @return
     */
    public Grids_Environment getGrids_Environment() {
        if (Grids_Env == null) {
            Grids_Env = new Grids_Environment(Files.getGeneratedGridsDir());
        }
        return Grids_Env;
    }

    public Vector_Environment getVector_Environment() {
        if (Vector_Env == null) {
            Vector_Env = new Vector_Environment();
            //ve = new Vector_Environment(df.getGeneratedVectorDir());
        }
        return Vector_Env;
    }

    public DW_Geotools getGeotools() {
        if (Geotools == null) {
            Geotools = new DW_Geotools(this);
            //Geotools = new DW_Geotools(df.getGeneratedGeotoolsDir());
        }
        return Geotools;
    }

    /**
     * For returning an instance of UO_Handler for convenience.
     *
     * @return
     */
    public DW_UO_Handler getUO_Handler() {
        if (UO_Handler == null) {
            UO_Handler = new DW_UO_Handler(this);
        }
        return UO_Handler;
    }

    /**
     * {@code this.DW_Postcode_Handler = DW_Postcode_Handler;}
     *
     * @param Postcode_Handler The DW_Postcode_Handler to set.
     */
    public void setPostcode_Handler(DW_Postcode_Handler Postcode_Handler) {
        this.Postcode_Handler = Postcode_Handler;
    }

    /**
     * For returning an instance of UO_Data for convenience.
     *
     * @return
     */
    public DW_UO_Data getUO_Data() {
        if (UO_Data == null) {
            try {
                UO_Data = getUO_Data(false);
            } catch (IOException e) {
                try {
                    UO_Data = getUO_Data(true);
                } catch (IOException e1) {
                    System.err.print(e1.getMessage());
                    e.printStackTrace(System.err);
                    System.exit(Generic_ErrorAndExceptionHandler.IOException);
                } catch (ClassNotFoundException e1) {
                    System.err.print(e1.getMessage());
                    e.printStackTrace(System.err);
                    System.exit(Generic_ErrorAndExceptionHandler.ClassNotFoundException);
                }
            } catch (ClassNotFoundException e) {
                try {
                    UO_Data = getUO_Data(true);
                } catch (IOException e1) {
                    System.err.print(e1.getMessage());
                    e.printStackTrace(System.err);
                    System.exit(Generic_ErrorAndExceptionHandler.IOException);
                } catch (ClassNotFoundException e1) {
                    System.err.print(e1.getMessage());
                    e.printStackTrace(System.err);
                    System.exit(Generic_ErrorAndExceptionHandler.ClassNotFoundException);
                }
            }
        }
        return UO_Data;
        // return getUO_Data(false);
    }

    /**
     * For returning an instance of UO_Data for convenience. If loadFromSource
     * is true then the data is reloaded from source. Otherwise if the formatted
     * version exists this is loaded. If the formatted version does not exist,
     * the data is loaded from source.
     *
     * @param loadFromSource
     * @return
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public DW_UO_Data getUO_Data(boolean loadFromSource) throws IOException, ClassNotFoundException {
        if (UO_Data == null || loadFromSource) {
            UO_Handler = getUO_Handler();
            File f;
            f = new File(
                    Files.getGeneratedUnderOccupiedDir(),
                    Strings.sDW_UO_Data + Strings.sBinaryFileExtension);
            if (loadFromSource) {
                UO_Data = UO_Handler.loadUnderOccupiedReportData(loadFromSource);
                Generic_StaticIO.writeObject(UO_Data, f);
            } else if (f.exists()) {
                UO_Data = (DW_UO_Data) Generic_StaticIO.readObject(f, true);
                // For debugging/testing load
                TreeMap<DW_YM3, DW_UO_Set> CouncilUOSets;
                CouncilUOSets = UO_Data.getCouncilUOSets();
                TreeMap<DW_YM3, DW_UO_Set> RSLUOSets;
                RSLUOSets = UO_Data.getRSLUOSets();
                int n;
                n = CouncilUOSets.size() + RSLUOSets.size();
                //logO("Number of UnderOccupancy data sets loaded " + n);
                int numberOfInputFiles;
                numberOfInputFiles = UO_Handler.getNumberOfInputFiles();
                //logO("Number of Input Files " + numberOfInputFiles);
                if (n != numberOfInputFiles) {
                    logE("Warning, there are some UnderOccupancy Data that have not been loaded.");
                }
            } else {
                UO_Data = UO_Handler.loadUnderOccupiedReportData(true);
                Generic_StaticIO.writeObject(UO_Data, f);
            }
        }
        return UO_Data;
    }

    /**
     * For returning an instance of DW_Postcode_Handler for convenience.
     *
     * @return
     */
    public DW_Postcode_Handler getPostcode_Handler() {
        if (Postcode_Handler == null) {
            Postcode_Handler = new DW_Postcode_Handler(this);
        }
        return Postcode_Handler;
    }

    /**
     * For returning an instance of SHBE_Handler for convenience.
     *
     * @return
     */
    public DW_SHBE_Handler getSHBE_Handler() {
        if (SHBE_Handler == null) {
            SHBE_Handler = new DW_SHBE_Handler(this);
        }
        return SHBE_Handler;
    }

    /**
     * {@code this.SHBE_Handler = SHBE_Handler;}
     *
     * @param SHBE_Handler The SHBE_Handler to set.
     */
    public void setSHBE_Handler(DW_SHBE_Handler SHBE_Handler) {
        this.SHBE_Handler = SHBE_Handler;
    }

    /**
     * For returning an instance of SHBE_TenancyType_Handler for convenience.
     *
     * @return
     */
    public DW_SHBE_TenancyType_Handler getSHBE_TenancyType_Handler() {
        if (SHBE_TenancyType_Handler == null) {
            SHBE_TenancyType_Handler = new DW_SHBE_TenancyType_Handler(this);
        }
        return SHBE_TenancyType_Handler;
    }

    /**
     * For returning an instance of SHBE_Data for convenience.
     *
     * @return
     */
    public DW_SHBE_Data getSHBE_Data() {
        if (SHBE_Data == null) {
            SHBE_Data = new DW_SHBE_Data(this);
        }
        return SHBE_Data;
    }

    /**
     * For returning an instance of DW_Maps for convenience.
     *
     * @return
     */
    public DW_Maps getMaps() {
        if (Maps == null) {
            Maps = new DW_Maps(this);
        }
        return Maps;
    }

    /**
     * For returning an instance of Deprivation_DataHandler for convenience.
     *
     * @return
     */
    public DW_Deprivation_DataHandler getDeprivation_DataHandler() {
        if (Deprivation_DataHandler == null) {
            Deprivation_DataHandler = new DW_Deprivation_DataHandler(this);
        }
        return Deprivation_DataHandler;
    }
}
