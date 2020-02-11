package uk.ac.leeds.ccg.projects.dw.core;

import java.io.IOException;
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
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Data;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_TenancyType;
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
 * DW_Environment
 * 
 * @author Andy Turner
 * @version 1.0.1
 */
public class DW_Environment extends Generic_MemoryManager {

    private static final long serialVersionUID = 1L;

    public String directory;
    //public String directory = "/scratch02/DigitalWelfare";
    //public String directory = "C:/Users/geoagdt/projects/DigitalWelfare";

    // For convenience
    public final DW_Files files;
    public final transient Data_Environment de;
    public final transient Generic_Environment ge;
    public final transient SHBE_Environment shbeEnv;
    public final transient Grids_Environment gridsEnv;
    public final transient Vector_Environment vectorEnv;
    public final transient Census_Environment censusEnv;
    public transient DW_Geotools geotools;
    public transient UKP_Data ukpData;
    public DW_UO_Handler uoHandler;
    public DW_UO_Data uoData;
    public SHBE_TenancyType shbeTenancyType;
    public DW_Maps maps;
    public SHBE_Data shbeData;

    public DW_Environment(Data_Environment de) throws IOException, Exception {
        this.de = de;
        this.ge = de.env;
        this.files = new DW_Files(ge.files.getDir());
        shbeEnv = new SHBE_Environment(de);
        gridsEnv = new Grids_Environment(ge, new Generic_Path(
                files.getGeneratedGridsDir()));
        vectorEnv = new Vector_Environment(gridsEnv);
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
        return getShbeData().clearAll();
    }

    /**
     * Swaps to file a collection.
     *
     * @return True iff a collection is cacheped.
     */
    public boolean clearSomeData() throws IOException, Exception {
        return getShbeData().clearSome();
    }

    /**
     * Swaps to file a collection.
     *
     * @param ym3 ym3
     * @return {@code true} if a collection is cleared.
     */
    public boolean clearSomeDataExcept(UKP_YM3 ym3) throws IOException, Exception {
        return getShbeData().clearSomeExcept(ym3);
    }

    public DW_Geotools getGeotools() throws IOException {
        if (geotools == null) {
            geotools = new DW_Geotools(this);
            //Geotools = new DW_Geotools(df.getGeneratedGeotoolsDir());
        }
        return geotools;
    }

    /**
     * For returning an instance of uoHandler for convenience.
     *
     * @return
     */
    public DW_UO_Handler getUoHandler() throws IOException, Exception {
        if (uoHandler == null) {
            uoHandler = new DW_UO_Handler(this);
        }
        return uoHandler;
    }

    /**
     * For returning an instance of uoData for convenience.
     *
     * @return
     */
    public DW_UO_Data getUoData() throws Exception {
        if (uoData == null) {
            try {
                uoData = getUoData(false);
            } catch (IOException | ClassNotFoundException e) {
                try {
                    uoData = getUoData(true);
                } catch (IOException | ClassNotFoundException e1) {
                    System.err.print(e1.getMessage());
                    e.printStackTrace(System.err);
                }
            }
        }
        return uoData;
        // return getUoData(false);
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
    public DW_UO_Data getUoData(boolean loadFromSource) throws IOException, 
            ClassNotFoundException, Exception {
        if (uoData == null || loadFromSource) {
            uoHandler = getUoHandler();
            Path f = Paths.get(files.getGeneratedUnderOccupiedDir().toString(),
                    DW_Strings.sDW_UO_Data + DW_Strings.sBinaryFileExtension);
            if (loadFromSource) {
                uoData = uoHandler.loadUnderOccupiedReportData(loadFromSource);
                Generic_IO.writeObject(uoData, f);
            } else if (Files.exists(f)) {
                uoData = (DW_UO_Data) Generic_IO.readObject(f);
                // For debugging/testing load
                TreeMap<UKP_YM3, DW_UO_Set> CouncilUOSets = uoData.getCouncilUOSets();
                TreeMap<UKP_YM3, DW_UO_Set> RSLUOSets = uoData.getRSLUOSets();
                int n = CouncilUOSets.size() + RSLUOSets.size();
                //logO("Number of UnderOccupancy data sets loaded " + n);
                //logO("Number of Input files " + numberOfInputFiles);
                if (n != uoHandler.getNumberOfInputFiles()) {
                    ge.log("Warning, there are some UnderOccupancy Data that "
                            + "have not been loaded.", true);
                }
            } else {
                uoData = uoHandler.loadUnderOccupiedReportData(true);
                ge.io.writeObject(uoData, f);
            }
        }
        return uoData;
    }

    /**
     * For returning an instance of UKP_Data for convenience.
     *
     * @return
     */
    public UKP_Data getUkpData() {
        if (ukpData == null) {
            ukpData = shbeEnv.oe.getHandler();
        }
        return ukpData;
    }

    /**
     * For returning an instance of shbeData for convenience.
     *
     * @return
     */
    public SHBE_Data getShbeData() throws Exception {
        if (shbeEnv.data == null) {
            try {
                shbeEnv.data = new SHBE_Data(shbeEnv);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
        return shbeEnv.data;
    }

    /**
     * {@code this.handler = handler;}
     *
     * @param data The handler to set.
     */
    public void setShbeData(SHBE_Data data) {
        shbeEnv.data = shbeData;
    }

    /**
     * For returning an instance of shbeTenancyType for convenience.
     *
     * @return
     */
    public SHBE_TenancyType getShbeTenancyType() {
        if (shbeTenancyType == null) {
            shbeTenancyType = new SHBE_TenancyType(shbeEnv);
        }
        return shbeTenancyType;
    }

    /**
     * For returning an instance of DW_Maps for convenience.
     *
     * @return
     */
    public DW_Maps getMaps() throws IOException {
        if (maps == null) {
            maps = new DW_Maps(this);
        }
        return maps;
    }
}
