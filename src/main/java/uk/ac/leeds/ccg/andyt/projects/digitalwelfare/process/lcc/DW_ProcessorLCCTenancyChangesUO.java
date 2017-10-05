/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.lcc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.TenancyChangesUO;

/**
 * This class produces output relating to Tenancy Type Changes for all those
 * SHBE Claims that have at some stage since April 2013 being Under Occupying.
 * There are several types of output generated.
 */
public class DW_ProcessorLCCTenancyChangesUO extends DW_ProcessorLCC {

    public DW_ProcessorLCCTenancyChangesUO(DW_Environment env) {
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
        include = DW_SHBE_Handler.getIncludeMonthlyUO();
        TenancyChangesUO UOTTC = new TenancyChangesUO(
                env,
                handleOutOfMemoryError);
        int index;
        String startMonth;
        String startYear;
        String endMonth;
        String endYear;
        index = include.get(0);
        startMonth = DW_SHBE_Handler.getMonth(
                SHBEFilenames[index]);
        startYear = DW_SHBE_Handler.getYear(
                SHBEFilenames[index]);
        index = include.get(include.size() - 1);
        endMonth = DW_SHBE_Handler.getMonth(
                SHBEFilenames[index]);
        endYear = DW_SHBE_Handler.getYear(
                SHBEFilenames[index]);
        iteB = bArray.iterator();
        while (iteB.hasNext()) {
            boolean includePreUnderOccupancyValues;
            includePreUnderOccupancyValues = iteB.next();
            env.logO("<includePreUnderOccupancyValues " + includePreUnderOccupancyValues + ">", true);
            table = UOTTC.getTable(
                    DW_UO_Data,
                    SHBEFilenames,
                    include,
                    includePreUnderOccupancyValues);
            UOTTC.writeTenancyChangeTables(
                    table,
                    includePreUnderOccupancyValues,
                    startMonth,
                    startYear,
                    endMonth,
                    endYear);
            env.logO("</includePreUnderOccupancyValues " + includePreUnderOccupancyValues + ">", true);
        }
    }
}
