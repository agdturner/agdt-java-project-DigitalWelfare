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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class DW_SHBE_Collection implements Serializable {

    protected transient DW_Environment env;
    
    protected transient DW_SHBE_CollectionHandler handler;

    /**
     * For storing DW_SHBE_Record indexed by DRecordID
     */
    protected HashMap<Long, DW_SHBE_Record> data;

    /**
     * For storing the ID of this.
     */
    protected Long ID;

    public DW_SHBE_Collection(
            Long ID,
            DW_SHBE_CollectionHandler handler) {
        this.env = handler.env;
        this.handler = handler;
        data = new HashMap();
        this.ID = ID;
    }

    public HashMap<Long, DW_SHBE_Record> getData() {
        return data;
    }

//    public void setData(HashMap<Long, DW_SHBE_Record> data) {
//        this.data = data;
//    }
    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public DW_SHBE_Record getRecord(long aRecordID) {
        DW_SHBE_Record result;
        result = data.get(aRecordID);
        return result;
    }

    public void addRecord(DW_SHBE_Record rec) {
        data.put(
                rec.RecordID,
                rec);
        env._DW_SHBE_CollectionHandler.lookup.put(
                rec.getCouncilTaxBenefitClaimReferenceNumber(),
                rec.RecordID);
    }

    public void write() {
        File f = getFile();
        File parent_File = f.getParentFile();
        if (!parent_File.exists()) {
            parent_File.mkdirs();
        }
        Generic_StaticIO.writeObject(
                this,
                f);
    }

    protected File getDir() {
        File result;
        result = Generic_StaticIO.getObjectDirectory(
                handler.getDirectory(),
                ID,
                ID,
                env._DW_SHBE_CollectionHandler._MaximumNumberOfObjectsPerDirectory);
        return result;
    }

    protected File getFile() {
        File result;
        File dir;
        dir = getDir();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        result = new File(
                dir,
                this.getClass().getName() + ID + ".thisFile");
        return result;
    }
}
