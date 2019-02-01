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
import java.io.Serializable;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_ErrorAndExceptionHandler;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census.DW_Deprivation_DataHandler;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Handler;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.core.SHBE_Environment;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Handler;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Set;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
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

    // For convenience
    public final DW_Strings strings;
    public final DW_Files files;
    public final transient Generic_Environment ge;
    public final transient SHBE_Environment SHBE_Env;
    public final transient Grids_Environment Grids_Env;
    public final transient Vector_Environment Vector_Env;
    public transient DW_Geotools Geotools;
    public transient ONSPD_Handler ONSPD_Handler;
    public DW_UO_Handler UO_Handler;
    public DW_UO_Data UO_Data;
    public SHBE_TenancyType_Handler SHBE_TenancyType_Handler;
    public DW_Maps Maps;
    public DW_Deprivation_DataHandler Deprivation_DataHandler;
    public SHBE_Handler SHBE_Handler;

    public DW_Environment(Generic_Environment ge) {
        this.ge = ge;
        this.strings = new DW_Strings();
        this.files = new DW_Files(strings);
        this.files.setDataDirectory(ge.getFiles().getDataDir());
        SHBE_Env = new SHBE_Environment(ge);
        Grids_Env = new Grids_Environment(files.getGeneratedGridsDir());
        Vector_Env = new Vector_Environment();
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
//                if (Level < LOGGING_LEVEL_NORMAL) {
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
//                        if (Level < LOGGING_LEVEL_NORMAL) {
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
//                        if (Level < LOGGING_LEVEL_NORMAL) {
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
        return getSHBE_Handler().clearAll();
    }

    /**
     * Swaps to file a collection.
     *
     * @return True iff a collection is swapped.
     */
    public boolean clearSomeSHBECache() {
        return getSHBE_Handler().clearSome();
    }

    /**
     * Swaps to file a collection.
     *
     * @param YM3
     * @return True iff a collection is swapped.
     */
    public boolean clearSomeSHBECacheExcept(ONSPD_YM3 YM3) {
        return getSHBE_Handler().clearSomeExcept(YM3);
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
     * For returning an instance of UO_Data for convenience.
     *
     * @return
     */
    public DW_UO_Data getUO_Data() {
        if (UO_Data == null) {
            try {
                UO_Data = getUO_Data(false);
            } catch (IOException | ClassNotFoundException e) {
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
            f = new File(files.getGeneratedUnderOccupiedDir(),
                    strings.sDW_UO_Data + strings.sBinaryFileExtension);
            if (loadFromSource) {
                UO_Data = UO_Handler.loadUnderOccupiedReportData(loadFromSource);
                Generic_IO.writeObject(UO_Data, f);
            } else if (f.exists()) {
                UO_Data = (DW_UO_Data) Generic_IO.readObject(f);
                // For debugging/testing load
                TreeMap<ONSPD_YM3, DW_UO_Set> CouncilUOSets;
                CouncilUOSets = UO_Data.getCouncilUOSets();
                TreeMap<ONSPD_YM3, DW_UO_Set> RSLUOSets;
                RSLUOSets = UO_Data.getRSLUOSets();
                int n;
                n = CouncilUOSets.size() + RSLUOSets.size();
                //logO("Number of UnderOccupancy data sets loaded " + n);
                //logO("Number of Input files " + numberOfInputFiles);
                if (n != UO_Handler.getNumberOfInputFiles()) {
                    ge.log("Warning, there are some UnderOccupancy Data that "
                            + "have not been loaded.", true);
                }
            } else {
                UO_Data = UO_Handler.loadUnderOccupiedReportData(true);
                Generic_IO.writeObject(UO_Data, f);
            }
        }
        return UO_Data;
    }

    /**
     * For returning an instance of ONSPD_Handler for convenience.
     *
     * @return
     */
    public ONSPD_Handler getONSPD_Handler() {
        if (ONSPD_Handler == null) {
            ONSPD_Handler = SHBE_Env.oe.getHandler();
        }
        return ONSPD_Handler;
    }

    /**
     * For returning an instance of SHBE_Handler for convenience.
     *
     * @return
     */
    public SHBE_Handler getSHBE_Handler() {
        if (SHBE_Env.handler == null) {
            SHBE_Env.handler = new SHBE_Handler(SHBE_Env);
        }
        return SHBE_Env.handler;
    }

    /**
     * {@code this.handler = handler;}
     *
     * @param SHBE_Handler The handler to set.
     */
    public void setSHBE_Handler(SHBE_Handler SHBE_Handler) {
        SHBE_Env.handler = SHBE_Handler;
    }

    /**
     * For returning an instance of SHBE_TenancyType_Handler for convenience.
     *
     * @return
     */
    public SHBE_TenancyType_Handler getSHBE_TenancyType_Handler() {
        if (SHBE_TenancyType_Handler == null) {
            SHBE_TenancyType_Handler = new SHBE_TenancyType_Handler(SHBE_Env);
        }
        return SHBE_TenancyType_Handler;
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
