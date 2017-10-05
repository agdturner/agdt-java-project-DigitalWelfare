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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Maps;

/**
 * This class is for an instance of the environment for the Digital Welfare
 * program. It contains holders for commonly referred to objects that might
 * otherwise be constructed multiple times. It is also for handling memory
 * although for the time being, there has not been a need for convoluted
 * swapping of data from memory to disk.
 *
 * @author agdturner
 */
public class DW_Environment extends DW_OutOfMemoryErrorHandler
        implements Serializable {

    public String sDigitalWelfareDir = "/scratch02/DigitalWelfare";
    //public String sDigitalWelfareDir = "C:/Users/geoagdt/projects/DigitalWelfare";

    /**
     * For storing an instance of DW_Strings for accessing Strings.
     */
    private DW_Strings DW_Strings;

    /**
     * Logging levels.
     */
    public int DEBUG_Level;
    public final int DEBUG_Level_FINEST = 0;
    public final int DEBUG_Level_FINE = 1;
    public final int DEBUG_Level_NORMAL = 2;
    
    /**
     * For storing an instance of DW_Files for accessing filenames and Files
     * therein.
     */
    private DW_Files DW_Files;
    
    /**
     * For storing an instance of Grids_Environment
     */
    private transient Grids_Environment Grids_Environment;
    
//    /**
//     * For storing an instance of HashMap<String, DW_SHBE_CollectionHandler>.
//     */
//    private HashMap<String, DW_SHBE_CollectionHandler> DW_SHBE_CollectionHandlers;

    /**
     * For storing an instance of DW_UO_Handler for convenience.
     */
    protected DW_UO_Handler DW_UO_Handler;

    /**
     * For storing an instance of DW_UO_Data.
     */
    protected DW_UO_Data DW_UO_Data;

    /**
     * For storing an instance of DW_Postcode_Handler for convenience.
     */
    private DW_Postcode_Handler DW_Postcode_Handler;

    /**
     * For storing an instance of DW_SHBE_Handler for convenience.
     */
    private DW_SHBE_Handler DW_SHBE_Handler;
    
    /**
     * For storing an instance of DW_SHBE_TenancyType_Handler for convenience.
     */
    private DW_SHBE_TenancyType_Handler DW_SHBE_TenancyType_Handler;
    
    /**
     * For storing an instance of DW_Maps for convenience.
     */
    private DW_Maps DW_Maps;

    /**
     * For storing an instance of DW_Deprivation_DataHandler for convenience.
     */
    DW_Deprivation_DataHandler DW_Deprivation_DataHandler;

    /**
     * For storing an instance of DW_SHBE_DataAll.
     */
    private DW_SHBE_Data DW_SHBE_Data;
    
    public DW_Environment(int DEBUG_Level) {
        init(DEBUG_Level);
    }
    
    public DW_Environment(int DEBUG_Level, String sDigitalWelfareDir) {
        init(DEBUG_Level, sDigitalWelfareDir);
    }

    private void init(int DEBUG_Level) {
        this.DEBUG_Level = DEBUG_Level;
            this.DW_Files = new DW_Files(this);
        this.DW_Strings = new DW_Strings();
    }
    
    private void init(int DEBUG_Level, String sDigitalWelfareDir) {
        this.sDigitalWelfareDir = sDigitalWelfareDir;
        init(DEBUG_Level);
    }

    @Override
    public void init_MemoryReserve(
            boolean handleOutOfMemoryError) {
        try {
            init_MemoryReserve();
        } catch (OutOfMemoryError a_OutOfMemoryError) {
            if (handleOutOfMemoryError) {
                clear_MemoryReserve();
                if (!swapToFile_DataAny()) {
                    throw a_OutOfMemoryError;
                }
                init_MemoryReserve(handleOutOfMemoryError);
            } else {
                throw a_OutOfMemoryError;
            }
        }
    }

    public int getDefaultMaximumNumberOfObjectsPerDirectory() {
        return 100;
    }

    /**
     * A method to ensure there is enough memory to continue.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    @Override
    public boolean tryToEnsureThereIsEnoughMemoryToContinue(
            boolean handleOutOfMemoryError) {
        try {
            if (tryToEnsureThereIsEnoughMemoryToContinue()) {
                return true;
            } else {
                if (DEBUG_Level < DEBUG_Level_NORMAL) {
                    String message
                            = "Warning! No data to swap or clear in "
                            + this.getClass().getName()
                            + ".tryToEnsureThereIsEnoughMemoryToContinue(boolean)";
                    System.out.println(message);
                }
                // Set to exit method with OutOfMemoryError
                handleOutOfMemoryError = false;
                throw new OutOfMemoryError();
            }
        } catch (OutOfMemoryError a_OutOfMemoryError) {
            if (handleOutOfMemoryError) {
                clear_MemoryReserve();
                boolean createdRoom = false;
                while (!createdRoom) {
                    if (!swapToFile_DataAny()) {
                        if (DEBUG_Level < DEBUG_Level_NORMAL) {
                            String message = "Warning! No data to swap or clear in "
                                    + this.getClass().getName()
                                    + ".tryToEnsureThereIsEnoughMemoryToContinue(boolean)";
                            System.out.println(message);
                        }
                        throw a_OutOfMemoryError;
                    }
                    try {
                        init_MemoryReserve(
                                HandleOutOfMemoryErrorFalse);
                        createdRoom = true;
                    } catch (OutOfMemoryError b_OutOfMemoryError) {
                        if (DEBUG_Level < DEBUG_Level_NORMAL) {
                            String message
                                    = "Struggling to ensure there is enough memory in "
                                    + this.getClass().getName()
                                    + ".tryToEnsureThereIsEnoughMemoryToContinue(boolean)";
                            System.out.println(message);
                        }
                    }
                }
                return tryToEnsureThereIsEnoughMemoryToContinue(
                        handleOutOfMemoryError);
            } else {
                throw a_OutOfMemoryError;
            }
        }
    }

    /**
     * A method to try to ensure there is enough memory to continue.
     *
     * @return
     */
    @Override
    protected boolean tryToEnsureThereIsEnoughMemoryToContinue() {
        while (getTotalFreeMemory() < Memory_Threshold) {
            if (!swapToFile_DataAny()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean swapToFile_DataAny(boolean handleOutOfMemoryError) {
        try {
            boolean result = swapToFile_DataAny();
            tryToEnsureThereIsEnoughMemoryToContinue(
                    HandleOutOfMemoryErrorFalse);
            return result;
        } catch (OutOfMemoryError a_OutOfMemoryError) {
            if (handleOutOfMemoryError) {
                clear_MemoryReserve();
                boolean result = swapToFile_DataAny(
                        HandleOutOfMemoryErrorFalse);
                init_MemoryReserve(HandleOutOfMemoryErrorFalse);
                return result;
            } else {
                throw a_OutOfMemoryError;
            }
        }
    }

    /**
     * Currently this just tries to swap a SHBE collection.
     *
     * @return
     */
    @Override
    public boolean swapToFile_DataAny() {
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
        return DW_SHBE_Data.clearAllCache();
    }

    /**
     * Swaps to file a collection.
     *
     * @return True iff a collection is swapped.
     */
    public boolean clearSomeSHBECache() {
        return getDW_SHBE_Data().clearSomeCache();
    }

    /**
     * Swaps to file a collection.
     *
     * @param YM3
     * @return True iff a collection is swapped.
     */
    public boolean clearSomeSHBECacheExcept(DW_YM3 YM3) {
        return getDW_SHBE_Data().clearSomeCacheExcept(YM3);
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
    public DW_Strings getDW_Strings() {
        if (DW_Strings == null) {
            DW_Strings = new DW_Strings();
        }
        return DW_Strings;
    }

    /**
     * For returning an instance of DW_Files for convenience.
     *
     * @return
     */
    public DW_Files getDW_Files() {
        if (DW_Files == null) {
            DW_Files = new DW_Files(this);
        }
        return DW_Files;
    }

    /**
     * For returning an instance of Grids_Environment for convenience.
     *
     * @return
     */
    public Grids_Environment getGrids_Environment() {
        if (Grids_Environment == null) {
            Grids_Environment = new Grids_Environment();
        }
        return Grids_Environment;
    }

//    /**
//     * For returning an instance of DW_SHBE_CollectionHandler for convenience.
//     *
//     * @return
//     */
//    public HashMap<String, DW_SHBE_CollectionHandler> getDW_SHBE_CollectionHandlers() {
//        if (DW_SHBE_CollectionHandlers == null) {
//            DW_SHBE_CollectionHandlers = new HashMap<String, DW_SHBE_CollectionHandler>();
//        }
//        return DW_SHBE_CollectionHandlers;
//    }
//
//    /**
//     * For returning an instance of DW_SHBE_CollectionHandler for convenience.
//     *
//     * @param PT
//     * @return
//     */
//    public DW_SHBE_CollectionHandler getDW_SHBE_CollectionHandler(String PT) {
//        DW_SHBE_CollectionHandler result;
//        DW_SHBE_CollectionHandlers = getDW_SHBE_CollectionHandlers();
//        if (DW_SHBE_CollectionHandlers.containsKey(PT)) {
//            result = DW_SHBE_CollectionHandlers.get(PT);
//        } else {
//            File dir;
//            dir = new File(
//                    DW_Files.getSwapSHBEDir(),
//                    PT);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//            result = new DW_SHBE_CollectionHandler(
//                    this,
//                    PT);
//            result.nextID = 0L;
//            DW_SHBE_CollectionHandlers.put(PT, result);
//        }
//        return result;
//    }

    /**
     * For returning an instance of DW_UO_Handler for convenience.
     *
     * @return
     */
    public DW_UO_Handler getDW_UO_Handler() {
        if (DW_UO_Handler == null) {
            DW_UO_Handler = new DW_UO_Handler(this);
        }
        return DW_UO_Handler;
    }

    /**
     * {@code this.DW_Postcode_Handler = DW_Postcode_Handler;}
     *
     * @param DW_Postcode_Handler The DW_Postcode_Handler to set.
     */
    public void setDW_Postcode_Handler(DW_Postcode_Handler DW_Postcode_Handler) {
        this.DW_Postcode_Handler = DW_Postcode_Handler;
    }

    /**
     * For returning an instance of DW_UO_Data for convenience.
     *
     * @return
     */
    public DW_UO_Data getDW_UO_Data() {
        if (DW_UO_Data == null) {
            try {
                DW_UO_Data = getDW_UO_Data(false);
            } catch (IOException e) {
                try {
                    DW_UO_Data = getDW_UO_Data(true);
                } catch (IOException e1) {
                    System.err.print(e.getMessage());
                    e.printStackTrace(System.err);
                    System.exit(Generic_ErrorAndExceptionHandler.IOException);
                } catch (ClassNotFoundException e1) {
                    System.err.print(e.getMessage());
                    e.printStackTrace(System.err);
                    System.exit(Generic_ErrorAndExceptionHandler.ClassNotFoundException);
                }
            } catch (ClassNotFoundException e) {
                try {
                    DW_UO_Data = getDW_UO_Data(true);
                } catch (IOException e1) {
                    System.err.print(e.getMessage());
                    e.printStackTrace(System.err);
                    System.exit(Generic_ErrorAndExceptionHandler.IOException);
                } catch (ClassNotFoundException e1) {
                    System.err.print(e.getMessage());
                    e.printStackTrace(System.err);
                    System.exit(Generic_ErrorAndExceptionHandler.ClassNotFoundException);
                }
            }
        }
        return DW_UO_Data;
        // return getDW_UO_Data(false);
    }

    /**
     * For returning an instance of DW_UO_Data for convenience. If
     * loadFromSource is true then the data is reloaded from source. Otherwise
     * if the formatted version exists this is loaded. If the formatted version
     * does not exist, the data is loaded from source.
     *
     * @param loadFromSource
     * @return
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public DW_UO_Data getDW_UO_Data(boolean loadFromSource) throws IOException, ClassNotFoundException {
        if (DW_UO_Data == null || loadFromSource) {
            DW_UO_Handler = getDW_UO_Handler();
            File f;
            f = new File(
                    DW_Files.getGeneratedUnderOccupiedDir(),
                    DW_Strings.sDW_UO_Data + DW_Strings.sBinaryFileExtension);
            if (loadFromSource) {
                DW_UO_Data = DW_UO_Handler.loadUnderOccupiedReportData(loadFromSource);
                Generic_StaticIO.writeObject(DW_UO_Data, f);
            } else if (f.exists()) {
                DW_UO_Data = (DW_UO_Data) Generic_StaticIO.readObject(f, true);
                // For debugging/testing load
                TreeMap<DW_YM3, DW_UO_Set> CouncilUOSets;
                CouncilUOSets = DW_UO_Data.getCouncilUOSets();
                TreeMap<DW_YM3, DW_UO_Set> RSLUOSets;
                RSLUOSets = DW_UO_Data.getRSLUOSets();
                int n;
                n = CouncilUOSets.size() + RSLUOSets.size();
                //logO("Number of UnderOccupancy data sets loaded " + n);
                int numberOfInputFiles;
                numberOfInputFiles = DW_UO_Handler.getNumberOfInputFiles();
                //logO("Number of Input Files " + numberOfInputFiles);
                if (n != numberOfInputFiles) {
                    logE("Warning, there are some UnderOccupancy Data that have not been loaded.");
                }
            } else {
                DW_UO_Data = DW_UO_Handler.loadUnderOccupiedReportData(true);
                Generic_StaticIO.writeObject(DW_UO_Data, f);
            }
        }
        return DW_UO_Data;
    }

    /**
     * For returning an instance of DW_Postcode_Handler for convenience.
     *
     * @return
     */
    public DW_Postcode_Handler getDW_Postcode_Handler() {
        if (DW_Postcode_Handler == null) {
            DW_Postcode_Handler = new DW_Postcode_Handler(this);
        }
        return DW_Postcode_Handler;
    }

    /**
     * For returning an instance of DW_SHBE_Handler for convenience.
     *
     * @return
     */
    public DW_SHBE_Handler getDW_SHBE_Handler() {
        if (DW_SHBE_Handler == null) {
            DW_SHBE_Handler = new DW_SHBE_Handler(this);
        }
        return DW_SHBE_Handler;
    }

    /**
     * {@code this.DW_SHBE_Handler = DW_SHBE_Handler;}
     *
     * @param DW_SHBE_Handler The DW_SHBE_Handler to set.
     */
    public void setDW_SHBE_Handler(DW_SHBE_Handler DW_SHBE_Handler) {
        this.DW_SHBE_Handler = DW_SHBE_Handler;
    }

    /**
     * For returning an instance of DW_SHBE_TenancyType_Handler for convenience.
     *
     * @return
     */
    public DW_SHBE_TenancyType_Handler getDW_SHBE_TenancyType_Handler() {
        if (DW_SHBE_TenancyType_Handler == null) {
            DW_SHBE_TenancyType_Handler = new DW_SHBE_TenancyType_Handler(this);
        }
        return DW_SHBE_TenancyType_Handler;
    }

    /**
     * For returning an instance of DW_SHBE_Data for convenience.
     *
     * @return
     */
    public DW_SHBE_Data getDW_SHBE_Data() {
        if (DW_SHBE_Data == null) {
            DW_SHBE_Data = new DW_SHBE_Data(this);
        }
        return DW_SHBE_Data;
    }
    
    /**
     * For returning an instance of DW_Maps for convenience.
     *
     * @return
     */
    public DW_Maps getDW_Maps() {
        if (DW_Maps == null) {
            DW_Maps = new DW_Maps(this);
        }
        return DW_Maps;
    }

    /**
     * For returning an instance of DW_Deprivation_DataHandler for convenience.
     *
     * @return
     */
    public DW_Deprivation_DataHandler getDW_Deprivation_DataHandler() {
        if (DW_Deprivation_DataHandler == null) {
            DW_Deprivation_DataHandler = new DW_Deprivation_DataHandler(this);
        }
        return DW_Deprivation_DataHandler;
    }
}
