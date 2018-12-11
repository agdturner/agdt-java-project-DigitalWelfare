/*
 * Copyright (C) 2017 geoagdt.
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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.util;

import java.io.Serializable;
import uk.ac.leeds.ccg.andyt.generic.util.Generic_Time;

/**
 *
 * @author geoagdt
 */
public class DW_YM3 implements Comparable, Serializable {

    private final int Year;
    private final int Month;

    public DW_YM3(String YM3) {
        String[] split;
        split = YM3.split("_");
        Year = Integer.valueOf(split[0]);
        Month = Generic_Time.getMonthInt(split[1]);
    }

    public DW_YM3(int Year, int Month) {
        this.Year = Year;
        this.Month = Month;
    }

    public DW_YM3(DW_YM3 YM3) {
        this.Year = YM3.getYear();
        this.Month = YM3.getMonth();
    }

    @Override
    public String toString() {
        return "" + Integer.toString(getYear()) + "_" + Generic_Time.getMonth3Letters(getMonth());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DW_YM3) {
            DW_YM3 o2;
            o2 = (DW_YM3) o;
            if (this.hashCode() == o2.hashCode()) {
                if (this.getMonth() == o2.getMonth()) {
                    if (this.getYear() == o2.getYear()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.getYear();
        hash = 67 * hash + this.getMonth();
        return hash;
    }

    public int compareTo(Object o) {
        if (o instanceof DW_YM3) {
            DW_YM3 o2;
            o2 = (DW_YM3) o;
            if (this.getYear() > o2.getYear()) {
                return 1;
            } else if (this.getYear() < o2.getYear()) {
                return -1;
            } else {
                if (this.getMonth() > o2.getMonth()) {
                    return 1;
                } else if (this.getMonth() < o2.getMonth()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
        return -1;
    }

    /**
     * @return the Year
     */
    public int getYear() {
        return Year;
    }

    /**
     * @return the Month
     */
    public int getMonth() {
        return Month;
    }

}
