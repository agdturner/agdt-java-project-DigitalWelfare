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
import uk.ac.leeds.ccg.andyt.generic.data.shbe.core.SHBE_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.DW_Summary;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.DW_SummaryUO;

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
    public void run() throws Exception, Error {

        HashSet<SHBE_ID> Group;
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
        includes = SHBE_Handler.getIncludes();
//        includes.remove(Strings.sIncludeAll);
//        includes.remove(Strings.sIncludeYearly);
//        includes.remove(Strings.sInclude6Monthly);
//        includes.remove(Strings.sInclude3Monthly);
//        includes.remove(Strings.sIncludeMonthlySinceApril2013);
//        includes.remove(Strings.sInclude2MonthlySinceApril2013Offset0);
//        includes.remove(Strings.sInclude2MonthlySinceApril2013Offset1);
//        includes.remove(Strings.sIncludeStartEndSinceApril2013);
//        includes.remove(Strings.sIncludeMonthly);
        //includes.remove(Strings.sIncludeApril2013May2013);
        PTs = Env.SHBE_Env.Strings.getPaymentTypes();
//        PTs.remove(Strings.sPaymentTypeAll); // No longer a PT
//        PTs.remove(Strings.sPaymentTypeIn);
//        PTs.remove(Strings.sPaymentTypeSuspended);
//        PTs.remove(Strings.sPaymentTypeOther);

        ArrayList<String> HB_CTB;
        HB_CTB = Strings.getHB_CTB();
        //forceNewSummaries = false;
        forceNewSummaries = true;
        nTT = SHBE_Handler.getNumberOfTenancyTypes();
        //nEG = SHBE_Handler.getNumberOfClaimantsEthnicGroups();
        nEG = SHBE_Handler.getNumberOfClaimantsEthnicGroupsGrouped();
        nPSI = SHBE_Handler.getOneOverMaxValueOfPassportStandardIndicator();

        // Processing loop
        Summary = new DW_Summary(Env, nTT, nEG, nPSI, hoome);
        SummaryUO = new DW_SummaryUO(Env, nTT, nEG, nPSI, hoome);
        includesIte = includes.keySet().iterator();
        while (includesIte.hasNext()) {
            includeKey = includesIte.next();
            Env.logO("<" + includeKey + ">", true);
            include = includes.get(includeKey);
            if (doAll) {
                sName = "Summary";
                Env.logO("<" + sName + ">", true);
                SummaryTableAll = Summary.getSummaryTable(SHBEFilenames,
                        include, forceNewSummaries, HB_CTB, PTs, nTT, nEG, nPSI,
                        hoome);
                Summary.writeSummaryTables(SummaryTableAll, HB_CTB, PTs,
                        includeKey, nTT, nEG, nPSI);
                Env.logO("</" + sName + ">", true);
            }
            if (doUO) {
                sName = "SummaryUO";
                Env.logO("<" + sName + ">", true);
                SummaryTableUO = SummaryUO.getSummaryTable(Group, SHBEFilenames,
                        include, forceNewSummaries, HB_CTB, PTs, nTT, nEG, nPSI,
                        UO_Data, hoome);
                SummaryUO.writeSummaryTables(SummaryTableUO, PTs, includeKey,
                        nTT, nEG, nPSI);
                Env.logO("</" + sName + ">", true);
            }
            Env.logO("</" + includeKey + ">", true);
        }
    }
}
