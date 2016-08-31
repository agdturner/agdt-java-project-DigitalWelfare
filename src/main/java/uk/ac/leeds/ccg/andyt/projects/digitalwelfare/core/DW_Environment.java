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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core;

import java.io.File;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.log.DW_Log;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_CollectionHandler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_Processor;

/**
 *
 * @author geoagdt
 */
public class DW_Environment extends DW_OutOfMemoryErrorHandler
        implements Serializable {

    public static String sDigitalWelfareDir = "/scratch02/DigitalWelfare";
    //public static String sDigitalWelfareDir = "C:/Users/geoagdt/projects/DigitalWelfare";

    /**
     * For storing an instance of DW_Files for accessing filenames and Files
     * therein.
     */
    private DW_Files tDW_Files;

    /**
     * For returning an instance of DW_Files for convenience.
     */
    public DW_Files getDW_Files() {
        if (tDW_Files == null) {
            tDW_Files = new DW_Files();
        }
        return tDW_Files;
    }

    /**
     * For storing an instance of Grids_Environment
     */
    private transient Grids_Environment tGrids_Environment;

    /**
     * For returning an instance of Grids_Environment for convenience.
     */
    public Grids_Environment getGrids_Environment() {
        if (tGrids_Environment == null) {
            tGrids_Environment = new Grids_Environment();
        }
        return tGrids_Environment;
    }
    public transient DW_SHBE_CollectionHandler _DW_SHBE_CollectionHandler;

    /**
     * For storing an instance of DW_Postcode_Handler for convenience.
     */
    private static DW_Postcode_Handler tDW_Postcode_Handler;

    /**
     * For returning an instance of DW_Postcode_Handler for convenience.
     */
    public DW_Postcode_Handler getDW_Postcode_Handler() {
        if (tDW_Postcode_Handler == null) {
            tDW_Postcode_Handler = new DW_Postcode_Handler(this);
        }
        return tDW_Postcode_Handler;
    }

    /**
     * For storing an instance of DW_SHBE_Handler for convenience.
     */
    private static DW_SHBE_Handler tDW_SHBE_Handler;

    /**
     * For returning an instance of DW_SHBE_Handler for convenience.
     */
    public DW_SHBE_Handler getDW_SHBE_Handler() {
        if (tDW_SHBE_Handler == null) {
            tDW_SHBE_Handler = new DW_SHBE_Handler(this);
        }
        return tDW_SHBE_Handler;
    }

    public DW_Environment(String sDigitalWelfareDir) {
        init_DW_Environment(sDigitalWelfareDir);
    }

    private void init_DW_Environment(String sDigitalWelfareDir) {
        this.sDigitalWelfareDir = sDigitalWelfareDir;
        this.tDW_Files = new DW_Files();
        _DW_SHBE_CollectionHandler = new DW_SHBE_CollectionHandler(
                this,
                tDW_Files.getSwapSHBEDir());
    }

    @Override
    public void init_MemoryReserve(
            boolean handleOutOfMemoryError) {
        try {
            init_MemoryReserve();
            tryToEnsureThereIsEnoughMemoryToContinue(handleOutOfMemoryError);
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
                String message
                        = "Warning! Not enough data to swap in "
                        + this.getClass().getName()
                        + ".tryToEnsureThereIsEnoughMemoryToContinue(boolean)";
                log(message);
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
                        String message = "Warning! Not enough data to swap in "
                                + this.getClass().getName()
                                + ".tryToEnsureThereIsEnoughMemoryToContinue(boolean)";
                        log(message);
                        throw a_OutOfMemoryError;
                    }
                    try {
                        init_MemoryReserve(
                                HandleOutOfMemoryErrorFalse);
                        createdRoom = true;
                    } catch (OutOfMemoryError b_OutOfMemoryError) {
                        log(
                                "Struggling to ensure there is enough memory in "
                                + this.getClass().getName()
                                + ".tryToEnsureThereIsEnoughMemoryToContinue(boolean)");
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

    private static void log(
            String message) {
        log(DW_Log.DW_DefaultLogLevel, message);
    }

    private static void log(
            Level level,
            String message) {
        Logger.getLogger(DW_Log.DW_DefaultLoggerName).log(level, message);
    }
}
