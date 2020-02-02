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
package uk.ac.leeds.ccg.projects.dw.data.adviceleeds;

import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.core.DW_Object;

/**
 *
 * @author geoagdt
 */
public abstract class DW_Data_AbstractRecord extends DW_Object {

    /**
     * RecordID
     */
    private long RecordID;

    public DW_Data_AbstractRecord(DW_Environment env) {
        super(env);
    }

    /**
     * @return the RecordID
     */
    public final long getRecordID() {
        return RecordID;
    }

    /**
     * @param RecordID the RecordID to set
     */
    public final void setRecordID(long RecordID) {
        this.RecordID = RecordID;
    }

    @Override
    public String toString() {
        return "RecordID " + RecordID;
    }

}
