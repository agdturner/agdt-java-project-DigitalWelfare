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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class DW_UnderOccupiedReport_Set {
    
    TreeMap<String, DW_UnderOccupiedReport_Record> set;
    
    public DW_UnderOccupiedReport_Set() {}
    
    /**
     * @param filename
     * @param handler 
     */
    public DW_UnderOccupiedReport_Set(
            String filename,
            DW_UnderOccupiedReport_Handler handler) {
        File dir;
        dir = DW_Files.getInputUnderOccupiedDir();        
        set = handler.loadInputData(
                    dir,
                    filename);
    }
       
//        councilRecords = new TreeMap[numberOfUnderOccupiedReportFiles];
//        result[0] = councilRecords;
//        RSLRecords = new TreeMap[numberOfUnderOccupiedReportFiles];

    public TreeMap<String, DW_UnderOccupiedReport_Record> getSet() {
        return set;
    }

    public void setSet(
            TreeMap<String, DW_UnderOccupiedReport_Record> set) {
        this.set = set;
    }
    
}