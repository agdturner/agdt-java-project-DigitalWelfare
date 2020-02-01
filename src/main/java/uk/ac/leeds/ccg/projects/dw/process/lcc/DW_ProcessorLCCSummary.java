/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.projects.dw.process.lcc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.data.shbe.data.id.SHBE_ClaimID;
import uk.ac.leeds.ccg.data.shbe.core.SHBE_Strings;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.core.DW_Strings;
import uk.ac.leeds.ccg.projects.dw.data.DW_Summary;
import uk.ac.leeds.ccg.projects.dw.data.DW_SummaryUO;

/**
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_ProcessorLCCSummary extends DW_ProcessorLCC {

    public DW_ProcessorLCCSummary(DW_Environment env) throws IOException {
        super(env);
    }

    @Override
    public void run() throws Exception, Error {

        HashSet<SHBE_ClaimID> Group;
        Group = new HashSet<>();

        boolean doAll;
        boolean doUO;

        doAll = true;
//        doAll = false;
        doUO = true;
//        doUO = false;
        boolean hoome = false;

        // Declaration
        TreeMap<String, ArrayList<Integer>> includes;
        ArrayList<String> PTs;
        boolean forceNewSummaries;
        int nTT;
        int nEG;
        int nPSI;
        DW_Summary Summary;
        DW_SummaryUO SummaryUO;
        Iterator<String> includesIte;
        String includeKey;
        ArrayList<Integer> include;
        TreeMap<String, HashMap<String, String>> SummaryTableAll;
        TreeMap<String, HashMap<String, String>> SummaryTableUO;
        /**
         * Variable to store a sub-process name.
         */
        String sName;

        // Initialisation
        includes = shbeHandler.getIncludes();
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
        PTs = SHBE_Strings.getPaymentTypes();
//        PTs.remove(DW_Strings.sPaymentTypeAll); // No longer a PT
//        PTs.remove(DW_Strings.sPaymentTypeIn);
//        PTs.remove(DW_Strings.sPaymentTypeSuspended);
//        PTs.remove(DW_Strings.sPaymentTypeOther);

        ArrayList<String> HB_CTB;
        HB_CTB = DW_Strings.getHB_CTB();
        //forceNewSummaries = false;
        forceNewSummaries = true;
        nTT = shbeHandler.getNumberOfTenancyTypes();
        //nEG = shbeHandler.getNumberOfClaimantsEthnicGroups();
        nEG = shbeHandler.getNumberOfClaimantsEthnicGroupsGrouped();
        nPSI = shbeHandler.getOneOverMaxValueOfPassportStandardIndicator();

        // Processing loop
        Summary = new DW_Summary(env, nTT, nEG, nPSI, hoome);
        SummaryUO = new DW_SummaryUO(env, nTT, nEG, nPSI, hoome);
        includesIte = includes.keySet().iterator();
        while (includesIte.hasNext()) {
            includeKey = includesIte.next();
            env.ge.log("<" + includeKey + ">", true);
            include = includes.get(includeKey);
            if (doAll) {
                sName = "Summary";
                env.ge.log("<" + sName + ">", true);
                SummaryTableAll = Summary.getSummaryTable(shbeFilenames,
                        include, forceNewSummaries, HB_CTB, PTs, nTT, nEG, nPSI,
                        hoome);
                Summary.writeSummaryTables(SummaryTableAll, HB_CTB, PTs,
                        includeKey, nTT, nEG, nPSI);
                env.ge.log("</" + sName + ">", true);
            }
            if (doUO) {
                sName = "SummaryUO";
                env.ge.log("<" + sName + ">", true);
                SummaryTableUO = SummaryUO.getSummaryTable(Group, shbeFilenames,
                        include, forceNewSummaries, HB_CTB, PTs, nTT, nEG, nPSI,
                        UO_Data, hoome);
                SummaryUO.writeSummaryTables(SummaryTableUO, PTs, includeKey,
                        nTT, nEG, nPSI);
                env.ge.log("</" + sName + ">", true);
            }
            env.ge.log("</" + includeKey + ">", true);
        }
    }
}
