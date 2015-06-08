/*
 * Copyright (C) 2014 geoagdt.
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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds;

import java.io.File;
import java.util.TreeMap;

/**
 * For handling data from CASE.
 */
public abstract class DW_Data_Abstract_Handler {

    public DW_Data_Abstract_Handler() {
    }

    /**
     * For loading data from a file.
     *
     * @param dir
     * @param filename
     * @param IDType
     * @return TreeMap
     */
    public abstract TreeMap loadInputData(
            File dir,
            String filename,
            Object IDType);

}
