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
package uk.ac.leeds.ccg.projects.dw.data.adviceleeds;

import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.io.DW_IO;

/**
 *
 * @author geoagdt
 */
public class DW_Data_CAB0_Record1 extends DW_Data_CAB0_Record {

    private String Last_Updated_By;
    private String Last_Updated;

    public DW_Data_CAB0_Record1(DW_Environment env) {
        super(env);
    }
    
    /**
     * @param env
     * @param RecordID
     * @param line
     * @param handler
     * @throws java.lang.Exception
     */
    public DW_Data_CAB0_Record1(
            DW_Environment env,
            long RecordID,
            String line,
            DW_Data_CAB0_Handler handler) throws Exception {
        super(env);
        setRecordID(RecordID);
        String[] fields;
        fields = DW_IO.splitWithQuotesThenCommas(line);
        int fieldCount = fields.length;
        int n = initMostFields(fields, fieldCount);
        n++;
        if (n < fieldCount) {
            Last_Updated_By = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Last_Updated = fields[n];
        }
        n++;
        if (n < fieldCount) {
            DOB = fields[n];
        }
    }

    @Override
    public String toString() {
        return super.toStringPart1()
                + ",Last_Updated_By " + Last_Updated_By
                + ",Last_Updated " + Last_Updated
                + ",DOB " + DOB;
    }

    public String getLast_Updated_By() {
        return Last_Updated_By;
    }

    public void setLast_Updated_By(String Last_Updated_By) {
        this.Last_Updated_By = Last_Updated_By;
    }

    public String getLast_Updated() {
        return Last_Updated;
    }

    public void setLast_Updated(String Last_Updated) {
        this.Last_Updated = Last_Updated;
    }

}
