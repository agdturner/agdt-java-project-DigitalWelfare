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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process;

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.reporting.DW_Report;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.charts.DW_BarChart;

/**
 *
 * @author geoagdt
 */
public class DW_MainProcessor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new DW_DataProcessor_LCC().run();
        //new DW_ChoroplethMaps_SHBE().run();
        //new DW_BarChart().run(args);
        new DW_Report().run();
    }
    
}
