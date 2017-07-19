/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.lcc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.Summary;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.SummaryUO;

/**
 * This is the main class for the Digital Welfare Project. For more details of
 * the project visit:
 * http://www.geog.leeds.ac.uk/people/a.turner/projects/DigitalWelfare/
 *
 * @author geoagdt
 */
public class DW_ProcessorLCCSummary extends DW_ProcessorLCC {

    public DW_ProcessorLCCSummary(DW_Environment env) {
        super(env);
    }

    @Override
    public void run() {
        
        HashSet<DW_ID> Group;
        Group = new HashSet<DW_ID>();
                
        boolean doAll;
        boolean doUO;

        doAll = true;
//        doAll = false;
        doUO = true;
//        doUO = false;
        boolean handleOutOfMemoryError = false;

        // Declaration
        TreeMap<String, ArrayList<Integer>> includes;
        ArrayList<String> PTs;
        boolean forceNewSummaries;
        int nTT;
        int nEG;
        int nPSI;
        Summary Summary;
        SummaryUO SummaryUO;
        Iterator<String> includesIte;
        String includeKey;
        ArrayList<Integer> include;
        TreeMap<String, HashMap<String, String>> SummaryTableAll;
        TreeMap<String, HashMap<String, String>> SummaryTableUO;
        String subProcessName;

        // Initialisation
        includes = DW_SHBE_Handler.getIncludes();
//        includes.remove(DW_Strings.sIncludeAll);
//        includes.remove(DW_Strings.sIncludeYearly);
//        includes.remove(DW_Strings.sInclude6Monthly);
//        includes.remove(DW_Strings.sInclude3Monthly);
//        includes.remove(DW_Strings.sIncludeMonthlySinceApril2013);
//        includes.remove(DW_Strings.sInclude2MonthlySinceApril2013Offset0);
//        includes.remove(DW_Strings.sInclude2MonthlySinceApril2013Offset1);
//        includes.remove(DW_Strings.sIncludeStartEndSinceApril2013);
//        includes.remove(DW_Strings.sIncludeMonthly);
        //includes.remove(DW_Strings.sIncludeApril2013May2013);
        PTs = DW_Strings.getPaymentTypes();
//        PTs.remove(DW_Strings.sPaymentTypeAll); // No longer a PT
//        PTs.remove(DW_Strings.sPaymentTypeIn);
//        PTs.remove(DW_Strings.sPaymentTypeSuspended);
//        PTs.remove(DW_Strings.sPaymentTypeOther);

        ArrayList<String> HB_CTB;
        HB_CTB = DW_Strings.getHB_CTB();
        //forceNewSummaries = false;
        forceNewSummaries = true;
        nTT = DW_SHBE_Handler.getNumberOfTenancyTypes();
        //nEG = DW_SHBE_Handler.getNumberOfClaimantsEthnicGroups();
        nEG = DW_SHBE_Handler.getNumberOfClaimantsEthnicGroupsGrouped();
        nPSI = DW_SHBE_Handler.getOneOverMaxValueOfPassportStandardIndicator();

        // Processing loop
//        while (PTsIte.hasNext()) {
//            PT = PTsIte.next();
//            env.logO("<" + PT + ">");
        Summary = new Summary(
                env,
                nTT,
                nEG,
                nPSI,
                handleOutOfMemoryError);
        SummaryUO = new SummaryUO(
                env,
                nTT,
                nEG,
                nPSI,
                handleOutOfMemoryError);
        includesIte = includes.keySet().iterator();
        while (includesIte.hasNext()) {
            includeKey = includesIte.next();
            env.logO("<" + includeKey + ">", true);
            include = includes.get(includeKey);
            if (doAll) {
                subProcessName = "Summary";
                env.logO("<" + subProcessName + ">", true);
                SummaryTableAll = Summary.getSummaryTable(
                        SHBEFilenames,
                        include,
                        forceNewSummaries,
                        HB_CTB,
                        PTs,
                        nTT,
                        nEG,
                        nPSI,
                        handleOutOfMemoryError);
                Summary.writeSummaryTables(
                        SummaryTableAll,
                        HB_CTB,
                        PTs,
                        includeKey,
                        nTT, nEG, nPSI);
                env.logO("</" + subProcessName + ">", true);
            }
            if (doUO) {
                subProcessName = "SummaryUO";
                env.logO("<" + subProcessName + ">", true);
                SummaryTableUO = SummaryUO.getSummaryTable(
                        
                        Group,
                        
                        SHBEFilenames,
                        include,
                        forceNewSummaries,
                        HB_CTB,
                        PTs,
                        nTT,
                        nEG,
                        nPSI,
                        DW_UO_Data,
                        handleOutOfMemoryError);
                SummaryUO.writeSummaryTables(
                        SummaryTableUO,
                        PTs,
                        includeKey,
                        nTT, nEG, nPSI);
                env.logO("</" + subProcessName + ">", true);
            }
            env.logO("</" + includeKey + ">", true);
        }
//            env.logO("</" + PT + ">");
//        }
    }
}
