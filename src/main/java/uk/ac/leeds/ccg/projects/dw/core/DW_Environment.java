package uk.ac.leeds.ccg.projects.dw.core;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;
import uk.ac.leeds.ccg.data.core.Data_Environment;
import uk.ac.leeds.ccg.data.census.core.Census_Environment;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.data.ukp.data.UKP_Data;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;
import uk.ac.leeds.ccg.data.shbe.core.SHBE_Environment;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Handler;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.generic.io.Generic_Path;
import uk.ac.leeds.ccg.generic.memory.Generic_MemoryManager;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Data;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Handler;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Set;
import uk.ac.leeds.ccg.projects.dw.io.DW_Files;
import uk.ac.leeds.ccg.projects.dw.visualisation.mapping.DW_Geotools;
import uk.ac.leeds.ccg.projects.dw.visualisation.mapping.DW_Maps;
import uk.ac.leeds.ccg.vector.core.Vector_Environment;

/**
 * This class is for a shared object, common to many objects in a Digital
 * Welfare program. It contains holders for commonly referred to objects that
 * might otherwise be constructed multiple times. It is also for handling memory
 * although for the time being, there has not been a need for convoluted
 * cacheping of data from memory to disk.
 */
public class DW_Environment extends Generic_MemoryManager
        implements Serializable {

    public String Directory;
    //public String Directory = "/scratch02/DigitalWelfare";
    //public String Directory = "C:/Users/geoagdt/projects/DigitalWelfare";

    // For convenience
    public final DW_Files files;
    public final transient Data_Environment de;
    public final transient Generic_Environment ge;
    public final transient SHBE_Environment SHBE_Env;
    public final transient Grids_Environment Grids_Env;
    public final transient Vector_Environment Vector_Env;
    public final transient Census_Environment censusEnv;
    public transient DW_Geotools Geotools;
    public transient UKP_Data ONSPD_Handler;
    public DW_UO_Handler UO_Handler;
    public DW_UO_Data UO_Data;
    public SHBE_TenancyType_Handler SHBE_TenancyType_Handler;
    public DW_Maps Maps;
    public SHBE_Handler SHBE_Handler;

    public DW_Environment(Data_Environment de) throws IOException, Exception {
        this.de = de;
        this.ge = de.env;
        this.files = new DW_Files(ge.files.getDir());
        SHBE_Env = new SHBE_Environment(de);
        Grids_Env = new Grids_Environment(ge, new Generic_Path(
                files.getGeneratedGridsDir()));
        Vector_Env = new Vector_Environment(de.env);
        Path censusDir = null;
        censusEnv = new Census_Environment(de, censusDir);
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
//                            = "Warning! No data to cache or clear in "
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
//                    if (!swapSomeData()) {
//                        if (Level < LOGGING_LEVEL_NORMAL) {
//                            String message = "Warning! No data to cache or clear in "
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
     * @throws java.io.IOException
     */
    @Override
    public boolean checkAndMaybeFreeMemory() throws IOException, Exception {
        while (getTotalFreeMemory() < Memory_Threshold) {
            if (!swapSomeData()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean swapSomeData(boolean hoome)
            throws IOException, Exception {
        try {
            boolean r = swapSomeData();
            checkAndMaybeFreeMemory();
            return r;
        } catch (OutOfMemoryError e) {
            if (hoome) {
                clearMemoryReserve(ge);
                boolean r = swapSomeData(HOOMEF);
                initMemoryReserve(ge);
                return r;
            } else {
                throw e;
            }
        }
    }

    /**
     * Currently this just tries to cache a SHBE collection.
     *
     * @return {@code true} if some data is cleared.
     * @throws java.io.IOException If encountered
     */
    @Override
    public boolean swapSomeData() throws IOException, Exception {
        boolean c = clearSomeData();
        if (!c) {
            ge.log("No SHBE data to clear. Do some coding to try "
                    + "to arrange to clear something else if needs be!!!");
        }
        return c;
    }

    /**
     * Clears the cache of all SHBE data.
     *
     * @return The number of sets of records cleared.
     */
    public int clearAllSHBECache() throws IOException, Exception {
        return getSHBE_Handler().clearAll();
    }

    /**
     * Swaps to file a collection.
     *
     * @return True iff a collection is cacheped.
     */
    public boolean clearSomeData() throws IOException, Exception {
        return getSHBE_Handler().clearSome();
    }

    /**
     * Swaps to file a collection.
     *
     * @param ym3 ym3
     * @return {@code true} if a collection is cleared.
     */
    public boolean clearSomeDataExcept(UKP_YM3 ym3) throws IOException, Exception {
        return getSHBE_Handler().clearSomeExcept(ym3);
    }

    public DW_Geotools getGeotools() throws IOException {
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
    public DW_UO_Handler getUO_Handler() throws IOException, Exception {
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
    public DW_UO_Data getUO_Data() throws Exception {
        if (UO_Data == null) {
            try {
                UO_Data = getUO_Data(false);
            } catch (IOException | ClassNotFoundException e) {
                try {
                    UO_Data = getUO_Data(true);
                } catch (IOException | ClassNotFoundException e1) {
                    System.err.print(e1.getMessage());
                    e.printStackTrace(System.err);
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
    public DW_UO_Data getUO_Data(boolean loadFromSource) throws IOException, 
            ClassNotFoundException, Exception {
        if (UO_Data == null || loadFromSource) {
            UO_Handler = getUO_Handler();
            Path f = Paths.get(files.getGeneratedUnderOccupiedDir().toString(),
                    DW_Strings.sDW_UO_Data + DW_Strings.sBinaryFileExtension);
            if (loadFromSource) {
                UO_Data = UO_Handler.loadUnderOccupiedReportData(loadFromSource);
                Generic_IO.writeObject(UO_Data, f);
            } else if (Files.exists(f)) {
                UO_Data = (DW_UO_Data) Generic_IO.readObject(f);
                // For debugging/testing load
                TreeMap<UKP_YM3, DW_UO_Set> CouncilUOSets = UO_Data.getCouncilUOSets();
                TreeMap<UKP_YM3, DW_UO_Set> RSLUOSets = UO_Data.getRSLUOSets();
                int n = CouncilUOSets.size() + RSLUOSets.size();
                //logO("Number of UnderOccupancy data sets loaded " + n);
                //logO("Number of Input files " + numberOfInputFiles);
                if (n != UO_Handler.getNumberOfInputFiles()) {
                    ge.log("Warning, there are some UnderOccupancy Data that "
                            + "have not been loaded.", true);
                }
            } else {
                UO_Data = UO_Handler.loadUnderOccupiedReportData(true);
                ge.io.writeObject(UO_Data, f);
            }
        }
        return UO_Data;
    }

    /**
     * For returning an instance of UKP_Data for convenience.
     *
     * @return
     */
    public UKP_Data getONSPD_Handler() {
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
    public SHBE_Handler getSHBE_Handler() throws Exception {
        if (SHBE_Env.handler == null) {
            try {
                SHBE_Env.handler = new SHBE_Handler(SHBE_Env);
            } catch (IOException ex) {
                ex.printStackTrace();
                ge.log(ex.getMessage());
            }
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
    public DW_Maps getMaps() throws IOException {
        if (Maps == null) {
            Maps = new DW_Maps(this);
        }
        return Maps;
    }
}
