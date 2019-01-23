/*
 * Copyright (C) 2017 Andy Turner, CCG, University of Leeds.
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

import java.io.Serializable;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

public abstract class DW_Object implements Serializable {

    /**
     * A reference to DW_Environment
     */
    protected transient final DW_Environment env;

    /**
     * For logging.
     */
    protected transient final int logID;
    
    /**
     * {@link DW_Environment#strings} of {@link #env} for convenience.
     */
    protected transient DW_Strings strings;

    /**
     * {@link DW_Environment#files} of {@link #env} for convenience.
     */
    protected transient DW_Files files;

    /**
     * Creates a new DW_Object. {@link #logID} is set to 0.
     *
     * @param e What {@link #env} is set to and from what {@link #strings} and
     * {@link #files} is set from.
     */
    public DW_Object(DW_Environment e) {
        this(e, 0);
    }

    /**
     * Creates a new DW_Object.
     *
     * @param e What {@link #env} is set to and from what {@link #strings} and
     * {@link #files} is set from.
     * @param i What {@link #logID} is set to.
     */
    public DW_Object(DW_Environment e, int i) {
        env = e;
        logID = i;
        strings = e.strings;
        files = e.files;
    }
}
