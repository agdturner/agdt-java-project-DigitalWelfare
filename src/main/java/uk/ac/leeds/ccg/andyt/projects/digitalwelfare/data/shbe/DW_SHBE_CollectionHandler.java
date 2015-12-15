/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.log.DW_Log;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 * Each Collection is assigned an CollectionManager which manages File IO. Each
 * Agent has a reference to at least one Collection and hence at least one
 * CollectionManager. This class contains methods for creating and deleting file
 * stores/caches.
 */
public class DW_SHBE_CollectionHandler
        //        extends GENESIS_OutOfMemoryErrorHandler
        implements Serializable {

    final transient DW_Environment env;

    protected HashMap<Long, DW_SHBE_Collection> collections;

    /**
     * For storing DRecordID indexed by CouncilTaxReferenceNumber
     */
    public HashMap<String,Long> lookup;
    
    /**
     * Directory.
     */
    protected File _Directory;
//    /**
//     * Directory where all DW_SHBE)Collections are written.
//     */
//    protected File _SHBECollectionsDirectory;
    /**
     * This is to be a sensible number of DW_SHBE_Records to hold in a single
     * collection. If this number is too small, IO will be slow. Too big and the
     * RAM memory demand could be too high. Perhaps 1000 is good minimum to work
     * on. Any factor of 10 increase on this is likely to be good. However,
     * loading a large collection just to get at a few records is likely to be
     * expensive if that collection then has to be cached again writing/storing
     * changes to to the file system...
     */
    public int _MaximumNumberPerCollection;
    /**
     * This is to be a sensible number of files to store in a single file. If
     * this number is too small, the directory depth will be high with possible
     * repercussions for IO. If too big then the file system might get upset and
     * it might be hard to find and order things. A value of 100 seems a good
     * compromise.
     */
    public int _MaximumNumberOfObjectsPerDirectory;

    public long nextID;

    public DW_SHBE_CollectionHandler(
            DW_Environment env) {
        this.env = env;
        _Directory = new File(
                DW_Files.getSwapSHBEDir(),
                "Collection" + System.currentTimeMillis());
        init();
    }
    
    public DW_SHBE_CollectionHandler(
            DW_Environment env,
            File dir) {
        this.env = env;
        _Directory = dir;
        init();
    }

    private void init() {
        env.init_MemoryReserve(false);
        nextID = 0L;
        _MaximumNumberOfObjectsPerDirectory = 100;
        _MaximumNumberPerCollection = 1000;
        collections = new HashMap<Long, DW_SHBE_Collection>();
        lookup = new HashMap<String,Long>();
    }

    public void add(DW_SHBE_Collection col){
        collections.put(col.getID(), col);
    }
    
    /**
     * <code>
     * return new File(_Directory.toString());
     * </code>
     *
     * @return a copy of _Directory
     */
    public File getDirectory() {
        return new File(_Directory.toString());
    }

//    /**
//     * <code>
//     * this._Directory = f;
//     * </code>
//     *
//     * @param f
//     */
//    public void setDirectory(File f) {
//        this._Directory = f;
//        f.mkdirs();
//    }

//    /**
//     * <code>
//     * return new File(_SHBECollectionsDirectory.toString());
//     * </code>
//     *
//     * @return a copy of _SHBECollectionsDirectory
//     */
//    public File getSHBECollectionsDirectory() {
//        return new File(_SHBECollectionsDirectory.toString());
//    }
//
//    /**
//     * <code>
//     * this._SHBECollectionsDirectory = f
//     * </code>
//     *
//     * @param f
//     */
//    public void setSHBECollectionsDirectory(File f) {
//        this._SHBECollectionsDirectory = f;
//        f.mkdirs();
//    }

    /**
     * @return a number representing the ID of the Collection with the highest
     * ID
     */
    public Long getMaxCollectionID() {
        return Generic_StaticIO.getArchiveHighestLeaf(
                DW_Files.getSwapSHBEDir(),
                "_");
    }

    protected File getCollectionDirectory(
            Long a_Agent_ID) {
        File result;
        long maxCollectionID = getMaxCollectionID();
        result = new File(
                Generic_StaticIO.getObjectDirectory(
                        DW_Files.getSwapSHBEDir(),
                        a_Agent_ID,
                        maxCollectionID,
                        _MaximumNumberOfObjectsPerDirectory),
                "" + a_Agent_ID);
        return result;
    }

    /**
     * @param handleOutOfMemoryError
     * @return
     * @see get_NextID()
     */
    public Long getNextID(
            boolean handleOutOfMemoryError) {
        try {
            Long result = nextID;
            env.tryToEnsureThereIsEnoughMemoryToContinue(handleOutOfMemoryError);
            return result;
        } catch (OutOfMemoryError a_OutOfMemoryError) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                swapToFile_Collection(false);
                env.init_MemoryReserve(
                        handleOutOfMemoryError);
                return getNextID(
                        handleOutOfMemoryError);
            } else {
                throw a_OutOfMemoryError;
            }
        }
    }

    public Long getCollection_ID(Long rec_ID) {
        long l = _MaximumNumberPerCollection;
        long result = 0L;
        while (rec_ID >= l) {
            l += _MaximumNumberPerCollection;
            result++;
        }
        return result;
    }

    protected DW_SHBE_Collection getCollection(
            Long ID) {
        DW_SHBE_Collection result;
        result = collections.get(ID);
        if (result == null) {
            File dir;
            dir = Generic_StaticIO.getObjectDirectory(
                    DW_Files.getSwapSHBEDir(), 
                    ID,
                    ID,
                    _MaximumNumberPerCollection);
            File f;
            f = new File(
                    dir,
                    "DW_SHBE_Collection.thisFile");
            Object o;
            o = Generic_StaticIO.readObject(f);
            result = (DW_SHBE_Collection) o;
            collections.put(ID, result);
        }
        return result;
    }

    public DW_SHBE_Collection getCollection(
            Long ID,
            boolean handleOutOfMemoryError) {
        try {
            DW_SHBE_Collection result = getCollection(
                    ID);
            env.tryToEnsureThereIsEnoughMemoryToContinue(
                    handleOutOfMemoryError);
            return result;
        } catch (OutOfMemoryError a_OutOfMemoryError) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                swapToFile_Collection();
                env.init_MemoryReserve(
                        DW_Environment.HandleOutOfMemoryErrorFalse);
                return getCollection(
                        ID,
                        handleOutOfMemoryError);
            } else {
                throw a_OutOfMemoryError;
            }
        }
    }

    public boolean swapToFile_Collection() {
        Iterator<Long> ite;
        ite = collections.keySet().iterator();
        while (ite.hasNext()) {
            Long aID = ite.next();
            DW_SHBE_Collection c;
            c = collections.get(aID);
            if (c != null) {
                c.write();
                c = null;
                return true;
            }
        }
        return false;
    }

    /**
     * Swaps to file any Collection.
     *
     * @param handleOutOfMemoryError If true then OutOfMemoryErrors are caught,
     * swap operations are initiated, then the method is re-called. If false
     * then OutOfMemoryErrors are caught and thrown.
     * @return
     */
    public boolean swapToFile_Collection(
            boolean handleOutOfMemoryError) {
        try {
            boolean result = swapToFile_Collection();
            try {
                if (!result) {
                    env.tryToEnsureThereIsEnoughMemoryToContinue(
                            false);
                } else {
                    env.tryToEnsureThereIsEnoughMemoryToContinue(
                            handleOutOfMemoryError);
                }
            } catch (OutOfMemoryError e) {
                throw e;
            }
            return result;
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                boolean createdRoom = false;
                while (!createdRoom) {
                    if (!swapToFile_Collection()) {
                        if (!swapToFile_Collection()) {
                            throw e;
                        }
                    }
                    env.init_MemoryReserve(false);
                    createdRoom = env.tryToEnsureThereIsEnoughMemoryToContinue(
                            handleOutOfMemoryError);
                }
                return true;
            } else {
                throw e;
            }
        }
    }

    /**
     * Attempts to swap to file a_DW_SHBE_Collection, but may swap other
     * GENESIS_Collections in the process which is likely if
     * a_DW_SHBE_Collection is very small and total available memory is almost
     * low and handleOutOfMemoryError is true
     *
     * @param c The DW_SHBE_Collection to attempt to swap to
     * file
     * @param handleOutOfMemoryError
     */
    public void swapToFile_Collection(
            DW_SHBE_Collection c,
            boolean handleOutOfMemoryError) {
        try {
            boolean success = swapToFile_Collection(c);
            try {
                if (!success) {
                    env.tryToEnsureThereIsEnoughMemoryToContinue(
                            false);
                } else {
                    env.tryToEnsureThereIsEnoughMemoryToContinue(
                            handleOutOfMemoryError);
                }
            } catch (OutOfMemoryError e) {
                throw e;
            }
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                boolean createdRoom = false;
                while (!createdRoom) {
                    if (!swapToFile_Collection(c)) {
                        if (!swapToFile_Collection()) {
                            throw e;
                        }
                    }
                    env.init_MemoryReserve(false);
                    createdRoom = env.tryToEnsureThereIsEnoughMemoryToContinue(
                            handleOutOfMemoryError);
                }
            } else {
                throw e;
            }
        }
    }

    /**
     * @param c The Collection to be swapped to disk.
     * @return true iff c is not null and is successfully swapped to disk.
     */
    protected boolean swapToFile_Collection(
            DW_SHBE_Collection c) {
        if (c == null) {
            return false;
        }
        c.write();
        collections.remove(c.getID());
        c = null;
        return true;
    }

    public DW_SHBE_Record getRecord(String councilTaxClaimNumber){
        DW_SHBE_Record result;
        Long DRecordID;
        DRecordID = lookup.get(councilTaxClaimNumber);
        if (DRecordID == null) {
            int debug = 1;
            return null;
        }
        Long collectionID;
        collectionID = getCollection_ID(DRecordID);
        DW_SHBE_Collection collection;
        collection = collections.get(collectionID);
        result = collection.getRecord(DRecordID);
        return result;
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
