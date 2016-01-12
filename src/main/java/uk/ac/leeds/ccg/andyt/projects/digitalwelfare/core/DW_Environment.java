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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_CollectionHandler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class DW_Environment extends DW_OutOfMemoryErrorHandler
        implements Serializable {

    public static String sDigitalWelfareDir = "/scratch02/DigitalWelfare";
    
    /**
     * Used for Logging
     */
    private static final String sourceClass = DW_Environment.class.getName();
    private static final String sourcePackage = DW_Environment.class.getPackage().getName();
    //= Logger.getLogger(sourcePackage);
    public File _Directory;
    /**
     * A store and reference for all AbstractGrid2DSquareCell
     */
    public transient Grids_Environment _Grids_Environment;

    public transient DW_SHBE_CollectionHandler _DW_SHBE_CollectionHandler;

    public DW_Environment(String sDigitalWelfareDir) {
        init_DW_Environment(sDigitalWelfareDir);
    }

    private void init_DW_Environment(String sDigitalWelfareDir) {
        this.sDigitalWelfareDir = sDigitalWelfareDir;
        _Grids_Environment = new Grids_Environment();
        _DW_SHBE_CollectionHandler = new DW_SHBE_CollectionHandler(
                this,
                DW_Files.getSwapSHBEDir());
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

    public static int getDefaultMaximumNumberOfObjectsPerDirectory() {
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

    /**
     * @param handleOutOfMemoryError
     * @return _Directory
     */
    public File get_Directory(
            boolean handleOutOfMemoryError) {
        try {
            File result = get_Directory();
            tryToEnsureThereIsEnoughMemoryToContinue(
                    handleOutOfMemoryError);
            return result;
        } catch (OutOfMemoryError a_OutOfMemoryError) {
            if (handleOutOfMemoryError) {
                clear_MemoryReserve();
                swapToFile_DataAny();
                init_MemoryReserve();
                return get_Directory(
                        handleOutOfMemoryError);
            } else {
                throw a_OutOfMemoryError;
            }
        }
    }

    /**
     * @return _Directory
     */
    protected File get_Directory() {
        return _Directory;
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
