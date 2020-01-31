/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.projects.dw.process.lcc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.data.DW_TenancyChangesUO;

/**
 * This class produces output relating to Tenancy Type Changes for all those
 * SHBE Claims that have at some stage since April 2013 being Under Occupying.
 * There are several types of output generated.
 */
public class DW_ProcessorLCCTenancyChangesUO extends DW_ProcessorLCC {

    public DW_ProcessorLCCTenancyChangesUO(DW_Environment env) throws IOException {
        super(env);
    }

    @Override
    public void run() throws Exception, Error {
        boolean handleOutOfMemoryError = false;

        // Declaration
        ArrayList<Boolean> bArray;
        Iterator<Boolean> iteB;

        // Initialisation
        bArray = getArrayListBoolean();

        /**
         * Create data about the Tenancy Type changes of UnderOccupied Claims
         */
        Object[] table;
        ArrayList<Integer> include;
        include = SHBE_Handler.getIncludeMonthlyUO();
        DW_TenancyChangesUO UOTTC = new DW_TenancyChangesUO(
                env,
                handleOutOfMemoryError);
        int index;
        String startMonth;
        String startYear;
        String endMonth;
        String endYear;
        index = include.get(0);
        startMonth = SHBE_Handler.getMonth(
                SHBEFilenames[index]);
        startYear = SHBE_Handler.getYear(
                SHBEFilenames[index]);
        index = include.get(include.size() - 1);
        endMonth = SHBE_Handler.getMonth(
                SHBEFilenames[index]);
        endYear = SHBE_Handler.getYear(
                SHBEFilenames[index]);
        iteB = bArray.iterator();
        while (iteB.hasNext()) {
            boolean includePreUnderOccupancyValues;
            includePreUnderOccupancyValues = iteB.next();
            env.ge.log("<includePreUnderOccupancyValues " 
                    + includePreUnderOccupancyValues + ">", true);
            table = UOTTC.getTable(UO_Data, SHBEFilenames, include,
                    includePreUnderOccupancyValues);
            UOTTC.writeTenancyChangeTables(table,
                    includePreUnderOccupancyValues, startMonth, startYear, 
                    endMonth, endYear);
            env.ge.log("</includePreUnderOccupancyValues " 
                    + includePreUnderOccupancyValues + ">", true);
        }
    }
}
