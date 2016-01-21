/*
 * Copyright (C) 2016 geoagdt.
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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Collection;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_CollectionHandler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_D_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_S_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UOReport_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Set;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class TenancyChangesUO {

    // Go through all the UO data and get a list of all UO Claim refs
    boolean handleOutOfMemoryError = false;
    DW_Environment env;
    DW_SHBE_CollectionHandler collectionHandler;
    DW_SHBE_Handler tDW_SHBE_Handler;

    static String sUnderOccupancyGroupTables = "UnderOccupancyGroupTables";
    String defaultPostcode = "AAN NAA";
    String sU = "U";
    String sTT = "TT";
    String sP = "Postcode";
    String sWHBE = "WeeklyHousingBenefitEntitlement";
    String sWERA = "WeeklyEligibleRentAmount";
    String sHS = "HouseholdSize";
    String sND = "NonDependents";
    String sCD = "ChildDependents";
    String sDoB = "ClaimantDoB";
    String sD = "Disability";
    String sDP = "DisabilityPremium";
    String sDS = "DisabilitySevere";
    String sDE = "DisabilityEnhanced";
    String sDC = "DisabledChild";
    String sPDeath = "PartnerDeath";
    String sHBDP = "HBDiscretionaryPayment";
    //String sCTBDP = "CTBDiscretionaryPayment";
    String sA = "Arrears";
    String s = "";
    String sUnderscore = "_";
    String sComma = ",";
    String sCommaSpace = ", ";
    String sTT_ = sTT + sUnderscore;

    String sBR = "BedroomRequirement";
    String sNB = "NumberOfBedrooms";
    String sNDUO = "NonDependents(UO)";
    String sCO16 = "ChildrenOver16";
    String sFCU10 = "FemaleChildrenUnder10";
    String sMCU10 = "MaleChildrenUnder10";
    String sFC10To16 = "FemaleChildren10to16";
    String sMC10To16 = "MaleChildren10to16";

    String sUniqueIndividualsEffected = "UniqueIndividualsEffected";

    String sNotMoved = "NotMoved";
    String sChangedTT = "ChangedTT";
    String sAlwaysUOFromStart = "AlwaysUOFromStart";
    String sAlwaysUOFromWhenStarted = "AlwaysUOFromWhenStarted";
    String sIntermitantUO = "IntermitantUO";
    String sTravellers;
    String sTTNot1Or4AndUnderOccupying;
    String sTT3OrTT6_To_TT1;
    String sTT1_To_TT3OrTT6;
    String sTT3OrTT6_To_TT4;
    String sTT4_To_TT3OrTT6;
    String sAlwaysUOFromStart__NotMoved_NotChangedTT;
    String sAlwaysUOFromStart__ChangedTT;
    String sAlwaysUOFromStart__Moved_NotChangedTT;
    String sAlwaysUOFromWhenStarted__NotMoved_NotChangedTT;
    String sAlwaysUOFromWhenStarted__ChangedTT;
    String sAlwaysUOFromWhenStarted__Moved_NotChangedTT;
    String sIntermitantUO__ChangedTT;
    String sIntermitantUO__Moved_NotChangedTT;
    String sIntermitantUO__NotMoved_NotChangedTT;

    private void initString() {
        sTravellers = "a_Travellers"; // Letter_ added for ordering purposes.
        sTTNot1Or4AndUnderOccupying = "b_TTNot1Or4AndUnderOccupying";

        sTT1_To_TT3OrTT6 = "e_TT1_To_TT3OrTT6";
        sTT4_To_TT3OrTT6 = "f_TT4_To_TT3OrTT6";
        sTT3OrTT6_To_TT1 = "g_TT3OrTT6_To_TT1";
        sTT3OrTT6_To_TT4 = "h_TT3OrTT6_To_TT4";

        sAlwaysUOFromStart__NotMoved_NotChangedTT = "k_AlwaysUOFromStart__NotMoved_NotChangedTT";
        sAlwaysUOFromStart__ChangedTT = "l_AlwaysUOFromStart__ChangedTT";
        sAlwaysUOFromStart__Moved_NotChangedTT = "m_AlwaysUOFromStart__Moved_NotChangedTT";

        sAlwaysUOFromWhenStarted__NotMoved_NotChangedTT = "p_AlwaysUOFromWhenStarted__NotMoved_NotChangedTT";
        sAlwaysUOFromWhenStarted__ChangedTT = "q_AlwaysUOFromWhenStarted__ChangedTT";
        sAlwaysUOFromWhenStarted__Moved_NotChangedTT = "r_AlwaysUOFromWhenStarted__Moved_NotChangedTT";

        sIntermitantUO__NotMoved_NotChangedTT = "u_" + "IntermitantUO__NotMoved_NotChangedTT";
        sIntermitantUO__ChangedTT = "v_" + "IntermitantUO__ChangedTT";
        sIntermitantUO__Moved_NotChangedTT = "w_" + "IntermitantUO__Moved_NotChangedTT";
    }

    public TenancyChangesUO(
            DW_Environment env,
            DW_SHBE_CollectionHandler collectionHandler,
            DW_SHBE_Handler tDW_SHBE_Handler,
            boolean handleOutOfMemoryError) {
        this.env = env;
        this.collectionHandler = collectionHandler;
        this.tDW_SHBE_Handler = tDW_SHBE_Handler;
        this.handleOutOfMemoryError = handleOutOfMemoryError;
        initString();
    }

    protected TreeMap<String, String> getPreUnderOccupancyValues(
            HashSet<String> tCTBRefs,
            String[] SHBEFilenames,
            ArrayList<Integer> NotMonthlyUO
    ) {
        TreeMap<String, String> result;
        result = new TreeMap<String, String>();
        // Init result
        String aCTBRef;
        Iterator<String> tCTBRefsIte;
        tCTBRefsIte = tCTBRefs.iterator();
        while (tCTBRefsIte.hasNext()) {
            aCTBRef = tCTBRefsIte.next();
            result.put(aCTBRef + sUnderscore + sTT, s);
            result.put(aCTBRef + sUnderscore + sU, s);
            result.put(aCTBRef + sUnderscore + sP, s);
            result.put(aCTBRef + sUnderscore + sWHBE, s);
            result.put(aCTBRef + sUnderscore + sWERA, s);
            result.put(aCTBRef + sUnderscore + sHS, s);
            result.put(aCTBRef + sUnderscore + sND, s);
            result.put(aCTBRef + sUnderscore + sCD, s);
            result.put(aCTBRef + sUnderscore + sNDUO, s);
            result.put(aCTBRef + sUnderscore + sCO16, s);
            result.put(aCTBRef + sUnderscore + sFCU10, s);
            result.put(aCTBRef + sUnderscore + sMCU10, s);
            result.put(aCTBRef + sUnderscore + sFC10To16, s);
            result.put(aCTBRef + sUnderscore + sMC10To16, s);
            result.put(aCTBRef + sUnderscore + sBR, s);
            result.put(aCTBRef + sUnderscore + sNB, s);
            result.put(aCTBRef + sUnderscore + sDoB, s);
            result.put(aCTBRef + sUnderscore + sDP, s);
            result.put(aCTBRef + sUnderscore + sDS, s);
            result.put(aCTBRef + sUnderscore + sDE, s);
            result.put(aCTBRef + sUnderscore + sDC, s);
            result.put(aCTBRef + sUnderscore + sPDeath, s);
            result.put(aCTBRef + sUnderscore + sHBDP, s);
            result.put(aCTBRef + sUnderscore + sA, s);
        }
        Iterator<Integer> tNotMonthlyUOIte;
        DW_SHBE_Collection tSHBEData;
        String paymentType;
        paymentType = DW_SHBE_Handler.sAllPT;
        int i;
        String key;
        String aS;
        int j;
        String bS;
        boolean b;
        TreeMap<String, DW_SHBE_Record> records;
        DW_SHBE_Record record;
        DW_SHBE_D_Record dRecord;
        tNotMonthlyUOIte = NotMonthlyUO.iterator();
        while (tNotMonthlyUOIte.hasNext()) {
            i = tNotMonthlyUOIte.next();
            tSHBEData = new DW_SHBE_Collection(SHBEFilenames[i], paymentType);
            records = tSHBEData.getRecords();
            tCTBRefsIte = tCTBRefs.iterator();
            while (tCTBRefsIte.hasNext()) {
                aCTBRef = tCTBRefsIte.next();
                record = records.get(aCTBRef);
                if (record != null) {
                    dRecord = record.getDRecord();
                    // Tenancy Type
                    key = aCTBRef + sUnderscore + sTT;
                    aS = result.get(key);
                    j = dRecord.getTenancyType();
                    aS += sCommaSpace + j;
                    result.put(key, aS);
                    // Under Occupancy
                    key = aCTBRef + sUnderscore + sU;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Postcode
                    key = aCTBRef + sUnderscore + sP;
                    aS = result.get(key);
                    bS = dRecord.getClaimantsPostcode();
                    aS += sCommaSpace + bS;
                    result.put(key, aS);
                    // Weekly Housing Benefit Entitlement
                    key = aCTBRef + sUnderscore + sWHBE;
                    aS = result.get(key);
                    j = dRecord.getWeeklyHousingBenefitEntitlement();
                    aS += sCommaSpace + decimalise(j);
                    result.put(key, aS);
                    // Weekly Eligible Rent Amount
                    key = aCTBRef + sUnderscore + sWERA;
                    aS = result.get(key);
                    j = dRecord.getWeeklyEligibleRentAmount();
                    aS += sCommaSpace + decimalise(j);
                    result.put(key, aS);
                    // Household Size
                    key = aCTBRef + sUnderscore + sHS;
                    aS = result.get(key);
                    j = (int) DW_SHBE_Handler.getHouseholdSize(dRecord);
                    aS += sCommaSpace + j;
                    result.put(key, aS);
                    // NonDependents
                    key = aCTBRef + sUnderscore + sND;
                    aS = result.get(key);
                    j = dRecord.getNumberOfNonDependents();
                    aS += sCommaSpace + j;
                    result.put(key, aS);
                    // Child Dependents
                    key = aCTBRef + sUnderscore + sCD;
                    aS = result.get(key);
                    j = dRecord.getNumberOfChildDependents();
                    aS += sCommaSpace + j;
                    result.put(key, aS);
                    // NonDependents (UO)
                    key = aCTBRef + sUnderscore + sNDUO;
                    aS = result.get(key);
                    j = dRecord.getNumberOfNonDependents();
                    aS += sCommaSpace + j;
                    result.put(key, aS);
                    // ChildrenOver16
                    key = aCTBRef + sUnderscore + sCO16;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // FemaleChildrenUnder10";
                    key = aCTBRef + sUnderscore + sFCU10;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // MaleChildrenUnder10";
                    key = aCTBRef + sUnderscore + sMCU10;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // FemaleChildren10to16";
                    key = aCTBRef + sUnderscore + sFC10To16;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // MaleChildren10to16
                    key = aCTBRef + sUnderscore + sMC10To16;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Number of Bedrooms
                    key = aCTBRef + sUnderscore + sNB;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Bedroom Requirement
                    key = aCTBRef + sUnderscore + sBR;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Date Of Birth
                    key = aCTBRef + sUnderscore + sDoB;
                    aS = result.get(key);
                    bS = dRecord.getClaimantsDateOfBirth();
                    aS += sCommaSpace + bS;
                    result.put(key, aS);
                    // Disability
                    key = aCTBRef + sUnderscore + sD;
                    aS = result.get(key);
                    b = DW_SHBE_Handler.getDisability(dRecord);
                    if (b == true) {
                        aS += sCommaSpace + sD;
                    } else {
                        aS += sCommaSpace;
                    }
                    result.put(key, aS);
                    // Disability Premium
                    key = aCTBRef + sUnderscore + sDP;
                    aS = result.get(key);
                    j = dRecord.getDisabledChildPremiumAwarded();
                    if (j == 1) {
                        aS += sCommaSpace + sDP;
                    } else {
                        aS += sCommaSpace;
                    }
                    result.put(key, aS);
                    // Disability Severe
                    key = aCTBRef + sUnderscore + sDS;
                    aS = result.get(key);
                    j = dRecord.getDisabledChildPremiumAwarded();
                    if (j == 1) {
                        aS += sCommaSpace + sDS;
                    } else {
                        aS += sCommaSpace;
                    }
                    result.put(key, aS);
                    // Disability Enhanced
                    key = aCTBRef + sUnderscore + sDE;
                    aS = result.get(key);
                    j = dRecord.getDisabledChildPremiumAwarded();
                    if (j == 1) {
                        aS += sCommaSpace + sDE;
                    } else {
                        aS += sCommaSpace;
                    }
                    result.put(key, aS);
                    // Child Disability
                    key = aCTBRef + sUnderscore + sDC;
                    aS = result.get(key);
                    j = dRecord.getDisabledChildPremiumAwarded();
                    if (j == 1) {
                        aS += sCommaSpace + sDC;
                    } else {
                        aS += sCommaSpace;
                    }
                    result.put(key, aS);
                    // Partner Death
                    key = aCTBRef + sUnderscore + sPDeath;
                    aS = result.get(key);
                    bS = dRecord.getPartnersDateOfDeath();
                    aS += sCommaSpace + bS;
                    result.put(key, aS);
                    // HB Discretionary Payment
                    key = aCTBRef + sUnderscore + sHBDP;
                    aS = result.get(key);
                    j = dRecord.getWeeklyAdditionalDiscretionaryPayment();
                    aS += sCommaSpace + decimalise(j);
                    result.put(key, aS);
                    // Arrears
                    key = aCTBRef + sUnderscore + sA;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                } else {
                    // Tenancy Type
                    key = aCTBRef + sUnderscore + sTT;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Under Occupancy
                    key = aCTBRef + sUnderscore + sU;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(aCTBRef + sUnderscore + s, aS);
                    // Postcode
                    key = aCTBRef + sUnderscore + sP;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Weekly Housing Benefit Entitlement
                    key = aCTBRef + sUnderscore + sWHBE;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Weekly Eligible Rent Amount
                    key = aCTBRef + sUnderscore + sWERA;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Household Size
                    key = aCTBRef + sUnderscore + sHS;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    // NonDependents
                    key = aCTBRef + sUnderscore + sND;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // ChildDependents
                    key = aCTBRef + sUnderscore + sCD;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // NonDependents (UO)
                    key = aCTBRef + sUnderscore + sNDUO;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // ChildrenOver16
                    key = aCTBRef + sUnderscore + sCO16;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // FemaleChildrenUnder10";
                    key = aCTBRef + sUnderscore + sFCU10;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // MaleChildrenUnder10";
                    key = aCTBRef + sUnderscore + sMCU10;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // FemaleChildren10to16";
                    key = aCTBRef + sUnderscore + sFC10To16;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // MaleChildren10to16
                    key = aCTBRef + sUnderscore + sMC10To16;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Number of Bedrooms
                    key = aCTBRef + sUnderscore + sNB;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Bedroom Requirement
                    key = aCTBRef + sUnderscore + sBR;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Date Of Birth
                    key = aCTBRef + sUnderscore + sDoB;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Disability
                    key = aCTBRef + sUnderscore + sD;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Disability Premium
                    key = aCTBRef + sUnderscore + sDP;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Disability Severe
                    key = aCTBRef + sUnderscore + sDS;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Disability Enhanced
                    key = aCTBRef + sUnderscore + sDE;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Child Disability
                    key = aCTBRef + sUnderscore + sDC;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Partner Death
                    key = aCTBRef + sUnderscore + sPDeath;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // HB Discretionary Payment
                    key = aCTBRef + sUnderscore + sHBDP;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Arrears
                    key = aCTBRef + sUnderscore + sA;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                }
            }
        }
        return result;
    }

    public String decimalise(String s) {
        if (s.isEmpty()) {
            return s;
        }
        int l;
        l = s.length();
        if (l == 1) {
            return "0.0" + s;
        }
        if (l == 2) {
            return "0." + s;
        }
        //if (l > 2) {
        return s.substring(0, l - 2) + "." + s.substring(l - 2, l);
        //}
    }

    public String decimalise(int i) {
        return decimalise(Integer.toString(i));
    }

    public Object[] getTable(
            Object[] underOccupiedData,
            String[] SHBEFilenames,
            ArrayList<Integer> include,
            String paymentType,
            boolean includePreUnderOccupancyValues
    ) {
        Object[] result;
        result = new Object[6];
        // Initialise result part 1
        TreeMap<String, String> tableValues;
        tableValues = new TreeMap<String, String>();

        TreeMap<String, DW_UnderOccupiedReport_Set> councilUnderOccupiedSets = null;
        TreeMap<String, DW_UnderOccupiedReport_Set> RSLUnderOccupiedSets = null;
        councilUnderOccupiedSets = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[0];
        RSLUnderOccupiedSets = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[1];

        HashSet<String>[] tAllCTBRefs;
        tAllCTBRefs = getCTBRefs(
                councilUnderOccupiedSets,
                RSLUnderOccupiedSets,
                SHBEFilenames,
                include);
        HashSet<String> tCouncilCTBRefs;
        tCouncilCTBRefs = tAllCTBRefs[0];
        HashSet<String> tRSLCTBRefs;
        tRSLCTBRefs = tAllCTBRefs[1];
        HashSet<String> tCTBRefs;
        tCTBRefs = new HashSet<String>();
        tCTBRefs.addAll(tCouncilCTBRefs);
        tCTBRefs.addAll(tRSLCTBRefs);

        TreeMap<String, ArrayList<Integer>> includes;
        includes = DW_SHBE_Handler.getIncludes();
        ArrayList<Integer> MonthlyUO;
        MonthlyUO = includes.get(DW_SHBE_Handler.sMonthlyUO);
        ArrayList<Integer> All;
        All = includes.get(DW_SHBE_Handler.sAll);
        ArrayList<Integer> NotMonthlyUO;
        NotMonthlyUO = new ArrayList<Integer>();
        NotMonthlyUO.addAll(All);
        NotMonthlyUO.removeAll(MonthlyUO);

        TreeMap<String, String> preUnderOccupancyValues;
        preUnderOccupancyValues = null;
        if (includePreUnderOccupancyValues) {
            preUnderOccupancyValues = getPreUnderOccupancyValues(tCTBRefs,
                    SHBEFilenames,
                    NotMonthlyUO);
            result[4] = preUnderOccupancyValues;
        }

//        HashSet<ID> tIDSetCouncilUniqueIndividualsEffected;
//        tIDSetCouncilUniqueIndividualsEffected = new HashSet<ID>();
        HashSet<ID> tIDSetCouncilUniqueClaimantsEffected;
        tIDSetCouncilUniqueClaimantsEffected = new HashSet<ID>();
        HashSet<ID> tIDSetCouncilUniquePartnersEffected;
        tIDSetCouncilUniquePartnersEffected = new HashSet<ID>();

        HashMap<String, Integer> tCouncilMaxNumberOfDependentsInClaimWhenUO;
        tCouncilMaxNumberOfDependentsInClaimWhenUO = new HashMap<String, Integer>();

        HashSet<ID> tIDSetCouncilUniqueNonDependentsEffected;
        tIDSetCouncilUniqueNonDependentsEffected = new HashSet<ID>();

//        HashSet<ID> tIDSetRSLUniqueIndividualsEffected;
//        tIDSetRSLUniqueIndividualsEffected = new HashSet<ID>();
        HashSet<ID> tIDSetRSLUniqueClaimantsEffected;
        tIDSetRSLUniqueClaimantsEffected = new HashSet<ID>();
        HashSet<ID> tIDSetRSLUniquePartnersEffected;
        tIDSetRSLUniquePartnersEffected = new HashSet<ID>();
//        HashSet<ID> tIDSetRSLUniqueDependentsEffected;
//        tIDSetRSLUniqueDependentsEffected = new HashSet<ID>();

        HashMap<String, Integer> tRSLMaxNumberOfDependentsInClaimWhenUO;
        tRSLMaxNumberOfDependentsInClaimWhenUO = new HashMap<String, Integer>();

        HashSet<ID> tIDSetRSLUniqueNonDependentsEffected;
        tIDSetRSLUniqueNonDependentsEffected = new HashSet<ID>();

        // groups is for ordering the table output. Keys are the group type and 
        // values are ordered sets of keys for writing rows.
        HashMap<String, TreeSet<String>> groups;
        groups = new HashMap<String, TreeSet<String>>();

        TreeSet<String> tCTBRefSetTravellers;
        tCTBRefSetTravellers = new TreeSet<String>();
        groups.put(sTravellers, tCTBRefSetTravellers);

        TreeSet<String> tCTBRefSetTTNot1Or4AndUnderOccupying;
        tCTBRefSetTTNot1Or4AndUnderOccupying = new TreeSet<String>();
        groups.put(sTTNot1Or4AndUnderOccupying, tCTBRefSetTTNot1Or4AndUnderOccupying);

        TreeSet<String> tCTBRefSetTT1_To_TT3;
        tCTBRefSetTT1_To_TT3 = new TreeSet<String>();
        groups.put(sTT1_To_TT3OrTT6, tCTBRefSetTT1_To_TT3);

        TreeSet<String> tCTBRefSetTT4_To_TT3;
        tCTBRefSetTT4_To_TT3 = new TreeSet<String>();
        groups.put(sTT4_To_TT3OrTT6, tCTBRefSetTT4_To_TT3);

        TreeSet<String> tCTBRefSetTT3_To_TT1;
        tCTBRefSetTT3_To_TT1 = new TreeSet<String>();
        groups.put(sTT3OrTT6_To_TT1, tCTBRefSetTT3_To_TT1);

        TreeSet<String> tCTBRefSetTT3_To_TT4;
        tCTBRefSetTT3_To_TT4 = new TreeSet<String>();
        groups.put(sTT3OrTT6_To_TT4, tCTBRefSetTT3_To_TT4);

//        TreeSet<String> tCTBRefSetNotMoved;
//        tCTBRefSetNotMoved = new TreeSet<String>();
//        tCTBRefSetNotMoved.addAll(tCTBRefs);
//        groups.put(sNotMoved, tCTBRefSetNotMoved);
        TreeSet<String> tCTBRefSetMoved;
        tCTBRefSetMoved = new TreeSet<String>();
        groups.put(sNotMoved, tCTBRefSetMoved);

        TreeSet<String> tCTBRefSetChangedTT;
        tCTBRefSetChangedTT = new TreeSet<String>();
        groups.put(sChangedTT, tCTBRefSetChangedTT);

        TreeSet<String> tCTBRefSetAlwaysUOFromStart;
        tCTBRefSetAlwaysUOFromStart = new TreeSet<String>();
        tCTBRefSetAlwaysUOFromStart.addAll(tCTBRefs);
        groups.put(sAlwaysUOFromStart, tCTBRefSetAlwaysUOFromStart);

        TreeSet<String> tCTBRefSetAlwaysUOFromWhenStarted;
        tCTBRefSetAlwaysUOFromWhenStarted = new TreeSet<String>();
        //tCTBRefSetAlwaysUOFromWhenStarted.addAll(tCTBRefs);
        groups.put(sAlwaysUOFromWhenStarted, tCTBRefSetAlwaysUOFromWhenStarted);

        TreeSet<String> tCTBRefSetIntermitantUO;
        tCTBRefSetIntermitantUO = new TreeSet<String>();
        groups.put(sIntermitantUO, tCTBRefSetIntermitantUO);

        String aCTBRef;
        HashMap<String, Integer> DHP_Totals;
        DHP_Totals = new HashMap<String, Integer>();
        // Initialise tableValues (part 2) and DHP_Totals
        Iterator<String> tCTBRefsIte;
        tCTBRefsIte = tCTBRefs.iterator();
        while (tCTBRefsIte.hasNext()) {
            aCTBRef = tCTBRefsIte.next();
            tableValues.put(aCTBRef + sUnderscore + sTT, s);
            tableValues.put(aCTBRef + sUnderscore + sU, s);
            tableValues.put(aCTBRef + sUnderscore + sP, s);
            tableValues.put(aCTBRef + sUnderscore + sWHBE, s);
            tableValues.put(aCTBRef + sUnderscore + sWERA, s);
            tableValues.put(aCTBRef + sUnderscore + sHS, s);
            tableValues.put(aCTBRef + sUnderscore + sND, s);
            tableValues.put(aCTBRef + sUnderscore + sCD, s);
            tableValues.put(aCTBRef + sUnderscore + sNDUO, s);
            tableValues.put(aCTBRef + sUnderscore + sCO16, s);
            tableValues.put(aCTBRef + sUnderscore + sFCU10, s);
            tableValues.put(aCTBRef + sUnderscore + sMCU10, s);
            tableValues.put(aCTBRef + sUnderscore + sFC10To16, s);
            tableValues.put(aCTBRef + sUnderscore + sMC10To16, s);
            tableValues.put(aCTBRef + sUnderscore + sNB, s);
            tableValues.put(aCTBRef + sUnderscore + sBR, s);
            tableValues.put(aCTBRef + sUnderscore + sDoB, s);
            tableValues.put(aCTBRef + sUnderscore + sD, s);
            tableValues.put(aCTBRef + sUnderscore + sDP, s);
            tableValues.put(aCTBRef + sUnderscore + sDS, s);
            tableValues.put(aCTBRef + sUnderscore + sDE, s);
            tableValues.put(aCTBRef + sUnderscore + sDC, s);
            tableValues.put(aCTBRef + sUnderscore + sPDeath, s);
            tableValues.put(aCTBRef + sUnderscore + sHBDP, s);
            tableValues.put(aCTBRef + sUnderscore + sA, s);
            DHP_Totals.put(aCTBRef, 0);
        }

        // Load dataset 1
        Iterator<Integer> includeIte;
        DW_SHBE_Collection aSHBEData = null;
        includeIte = include.iterator();
        String aYM3 = s;
        DW_UnderOccupiedReport_Set councilUnderOccupiedSet1 = null;
        DW_UnderOccupiedReport_Set RSLUnderOccupiedSet1 = null;
        String aSHBEFilename = null;
        int i;

        String header;
        header = "CTBRef, ";
        if (includePreUnderOccupancyValues) {
            Iterator<Integer> tNotMonthlyUOIte;
            String yM3;
            tNotMonthlyUOIte = NotMonthlyUO.iterator();
            while (tNotMonthlyUOIte.hasNext()) {
                i = tNotMonthlyUOIte.next();
                yM3 = DW_SHBE_Handler.getYM3(SHBEFilenames[i]);
                header += yM3 + sCommaSpace;
            }
        }

        boolean initFirst;
        initFirst = false;
        // Load first data
        while (!initFirst) {
            i = includeIte.next();
            aSHBEFilename = SHBEFilenames[i];
            aYM3 = DW_SHBE_Handler.getYM3(aSHBEFilename);
            councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(aYM3);
            if (councilUnderOccupiedSet1 != null) {
                RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(aYM3);
                aSHBEData = new DW_SHBE_Collection(aSHBEFilename, paymentType);
                initFirst = true;
            }
            header += aYM3;
        }
        TreeMap<String, DW_SHBE_Record> aRecords;
        aRecords = aSHBEData.getRecords();
        TreeMap<String, DW_SHBE_Record> bRecords;
        bRecords = null;

        DW_SHBE_Record aDW_SHBE_Record;

        int aDHPTotal;
        int aDHP;
        // Add TT of all CTBRefs to result
        tCTBRefsIte = tCTBRefs.iterator();
        while (tCTBRefsIte.hasNext()) {
            aCTBRef = tCTBRefsIte.next();
            aDW_SHBE_Record = aRecords.get(aCTBRef);
            aDHP = process(
                    aCTBRef,
                    aDW_SHBE_Record,
                    bRecords,
                    tableValues,
                    councilUnderOccupiedSet1,
                    RSLUnderOccupiedSet1,
                    tIDSetCouncilUniqueClaimantsEffected,
                    tIDSetCouncilUniquePartnersEffected,
                    tCouncilMaxNumberOfDependentsInClaimWhenUO,
                    tIDSetCouncilUniqueNonDependentsEffected,
                    tIDSetRSLUniqueClaimantsEffected,
                    tIDSetRSLUniquePartnersEffected,
                    tRSLMaxNumberOfDependentsInClaimWhenUO,
                    tIDSetRSLUniqueNonDependentsEffected,
                    tCTBRefSetTravellers,
                    tCTBRefSetTTNot1Or4AndUnderOccupying,
                    tCTBRefSetTT1_To_TT3,
                    tCTBRefSetTT4_To_TT3,
                    tCTBRefSetTT3_To_TT1,
                    tCTBRefSetTT3_To_TT4,
                    //tCTBRefSetNotMoved,
                    tCTBRefSetMoved,
                    tCTBRefSetChangedTT,
                    tCTBRefSetAlwaysUOFromStart,
                    tCTBRefSetAlwaysUOFromWhenStarted,
                    tCTBRefSetIntermitantUO);
            aDHPTotal = DHP_Totals.get(aCTBRef);
            aDHPTotal += aDHP;
            DHP_Totals.put(aCTBRef, aDHPTotal);
        }

        // Iterate over the rest of the data
        while (includeIte.hasNext()) {
            i = includeIte.next();
            bRecords = aRecords;
            aSHBEFilename = SHBEFilenames[i];
            aYM3 = DW_SHBE_Handler.getYM3(aSHBEFilename);
            aSHBEData = new DW_SHBE_Collection(aSHBEFilename, paymentType);
            aRecords = aSHBEData.getRecords();
            councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(aYM3);
            RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(aYM3);
            header += sCommaSpace + aYM3;
            tCTBRefsIte = tCTBRefs.iterator();
            while (tCTBRefsIte.hasNext()) {
                aCTBRef = tCTBRefsIte.next();
                aDW_SHBE_Record = aRecords.get(aCTBRef);
                aDHP = process(
                        aCTBRef,
                        aDW_SHBE_Record,
                        bRecords,
                        tableValues,
                        councilUnderOccupiedSet1,
                        RSLUnderOccupiedSet1,
                        tIDSetCouncilUniqueClaimantsEffected,
                        tIDSetCouncilUniquePartnersEffected,
                        tCouncilMaxNumberOfDependentsInClaimWhenUO,
                        tIDSetCouncilUniqueNonDependentsEffected,
                        tIDSetRSLUniqueClaimantsEffected,
                        tIDSetRSLUniquePartnersEffected,
                        tRSLMaxNumberOfDependentsInClaimWhenUO,
                        tIDSetRSLUniqueNonDependentsEffected,
                        tCTBRefSetTravellers,
                        tCTBRefSetTTNot1Or4AndUnderOccupying,
                        tCTBRefSetTT1_To_TT3,
                        tCTBRefSetTT4_To_TT3,
                        tCTBRefSetTT3_To_TT1,
                        tCTBRefSetTT3_To_TT4,
                        //tCTBRefSetNotMoved,
                        tCTBRefSetMoved,
                        tCTBRefSetChangedTT,
                        tCTBRefSetAlwaysUOFromStart,
                        tCTBRefSetAlwaysUOFromWhenStarted,
                        tCTBRefSetIntermitantUO);
                aDHPTotal = DHP_Totals.get(aCTBRef);
                aDHPTotal += aDHP;
                DHP_Totals.put(aCTBRef, aDHPTotal);
            }
        }

        header += sCommaSpace + "HBDPTotal";

//        TreeSet<String> tCTBRefSetMoved; // Calculate by removing all from tCTBRefSetNotMoved.
//        tCTBRefSetMoved = new TreeSet<String>();
//        tCTBRefSetMoved.addAll(tCTBRefs);
//        tCTBRefSetMoved.removeAll(tCTBRefSetNotMoved);
//        groups.put("Moved", tCTBRefSetMoved);
        TreeSet<String> tCTBRefSetNotMoved; // Calculate by removing all from tCTBRefSetNotMoved.
        tCTBRefSetNotMoved = new TreeSet<String>();
        tCTBRefSetNotMoved.addAll(tCTBRefs);
        tCTBRefSetNotMoved.removeAll(tCTBRefSetMoved);
        groups.put(sNotMoved, tCTBRefSetMoved);

        TreeSet<String> tCTBRefSetNotChangedTT; // Calculate by removing all from tCTBRefSetChangedTT.
        tCTBRefSetNotChangedTT = new TreeSet<String>();
        tCTBRefSetNotChangedTT.addAll(tCTBRefs);
        tCTBRefSetNotChangedTT.removeAll(tCTBRefSetChangedTT);
        groups.put("NotChangedTT", tCTBRefSetNotChangedTT);

        TreeSet<String> tCTBRefSetAlwaysUOFromStartNotMovedNotChangedTT; // Calculate by intersect of sets.
        tCTBRefSetAlwaysUOFromStartNotMovedNotChangedTT = new TreeSet<String>();
        tCTBRefSetAlwaysUOFromStartNotMovedNotChangedTT.addAll(tCTBRefSetAlwaysUOFromStart);
        tCTBRefSetAlwaysUOFromStartNotMovedNotChangedTT.retainAll(tCTBRefSetNotMoved);
        tCTBRefSetAlwaysUOFromStartNotMovedNotChangedTT.retainAll(tCTBRefSetNotChangedTT);
        groups.put(sAlwaysUOFromStart__NotMoved_NotChangedTT, tCTBRefSetAlwaysUOFromStartNotMovedNotChangedTT);

        TreeSet<String> tCTBRefSetAlwaysUOFromStartChangedTT; // Calculate by intersect of sets.
        tCTBRefSetAlwaysUOFromStartChangedTT = new TreeSet<String>();
        tCTBRefSetAlwaysUOFromStartChangedTT.addAll(tCTBRefSetChangedTT);
        tCTBRefSetAlwaysUOFromStartChangedTT.retainAll(tCTBRefSetAlwaysUOFromStart);
        groups.put(sAlwaysUOFromStart__ChangedTT, tCTBRefSetAlwaysUOFromStartChangedTT);

        TreeSet<String> tCTBRefSetAlwaysUOFromStartMovedNotChangedTT; // Calculate by intersect of sets.
        tCTBRefSetAlwaysUOFromStartMovedNotChangedTT = new TreeSet<String>();
        tCTBRefSetAlwaysUOFromStartMovedNotChangedTT.addAll(tCTBRefSetAlwaysUOFromStart);
        tCTBRefSetAlwaysUOFromStartMovedNotChangedTT.removeAll(tCTBRefSetAlwaysUOFromStartChangedTT);
        tCTBRefSetAlwaysUOFromStartMovedNotChangedTT.retainAll(tCTBRefSetMoved);
        groups.put(sAlwaysUOFromStart__Moved_NotChangedTT, tCTBRefSetAlwaysUOFromStartMovedNotChangedTT);

        String aS;
        String key;
        Iterator<String> ite;
        ite = tCTBRefs.iterator();
        while (ite.hasNext()) {
            aCTBRef = ite.next();
            key = aCTBRef + sUnderscore + sU;
            aS = tableValues.get(key);
            if (aS.endsWith(sCommaSpace)) {
                tCTBRefSetIntermitantUO.add(aCTBRef);
            }
        }

        tCTBRefSetAlwaysUOFromWhenStarted.removeAll(tCTBRefSetAlwaysUOFromStart);
        tCTBRefSetAlwaysUOFromWhenStarted.removeAll(tCTBRefSetIntermitantUO);

        TreeSet<String> tCTBRefSetAlwaysUOFromWhenStartedNotMovedNotChangedTT; // Calculate by intersect of sets.
        tCTBRefSetAlwaysUOFromWhenStartedNotMovedNotChangedTT = new TreeSet<String>();
        tCTBRefSetAlwaysUOFromWhenStartedNotMovedNotChangedTT.addAll(tCTBRefSetAlwaysUOFromWhenStarted);
        tCTBRefSetAlwaysUOFromWhenStartedNotMovedNotChangedTT.removeAll(tCTBRefSetChangedTT);
        tCTBRefSetAlwaysUOFromWhenStartedNotMovedNotChangedTT.retainAll(tCTBRefSetNotMoved);
        groups.put(sAlwaysUOFromWhenStarted__NotMoved_NotChangedTT, tCTBRefSetAlwaysUOFromWhenStartedNotMovedNotChangedTT);

        TreeSet<String> tCTBRefSetAlwaysUOFromWhenStartedChangedTT; // Calculate by intersect of sets.
        tCTBRefSetAlwaysUOFromWhenStartedChangedTT = new TreeSet<String>();
        tCTBRefSetAlwaysUOFromWhenStartedChangedTT.addAll(tCTBRefSetAlwaysUOFromWhenStarted);
        tCTBRefSetAlwaysUOFromWhenStartedChangedTT.retainAll(tCTBRefSetChangedTT);
        groups.put(sAlwaysUOFromWhenStarted__ChangedTT, tCTBRefSetAlwaysUOFromWhenStartedChangedTT);

        TreeSet<String> tCTBRefSetAlwaysUOFromWhenStartedMovedNotChangedTT; // Calculate by intersect of sets.
        tCTBRefSetAlwaysUOFromWhenStartedMovedNotChangedTT = new TreeSet<String>();
        tCTBRefSetAlwaysUOFromWhenStartedMovedNotChangedTT.addAll(tCTBRefSetAlwaysUOFromWhenStarted);
        tCTBRefSetAlwaysUOFromWhenStartedMovedNotChangedTT.removeAll(tCTBRefSetNotMoved);
        tCTBRefSetAlwaysUOFromWhenStartedMovedNotChangedTT.removeAll(tCTBRefSetChangedTT);
        groups.put(sAlwaysUOFromWhenStarted__Moved_NotChangedTT, tCTBRefSetAlwaysUOFromWhenStartedMovedNotChangedTT);

        TreeSet<String> tCTBRefSetIntermitantUONotMovedNotChangedTT;
        tCTBRefSetIntermitantUONotMovedNotChangedTT = new TreeSet<String>();
        tCTBRefSetIntermitantUONotMovedNotChangedTT.addAll(tCTBRefSetIntermitantUO);
        tCTBRefSetIntermitantUONotMovedNotChangedTT.retainAll(tCTBRefSetNotMoved);
        tCTBRefSetIntermitantUONotMovedNotChangedTT.retainAll(tCTBRefSetNotChangedTT);
        groups.put(sIntermitantUO__NotMoved_NotChangedTT, tCTBRefSetIntermitantUONotMovedNotChangedTT);

        TreeSet<String> tCTBRefSetIntermitantUOChangedTT;
        tCTBRefSetIntermitantUOChangedTT = new TreeSet<String>();
        tCTBRefSetIntermitantUOChangedTT.addAll(tCTBRefSetIntermitantUO);
        tCTBRefSetIntermitantUOChangedTT.retainAll(tCTBRefSetChangedTT);
        groups.put(sIntermitantUO__ChangedTT, tCTBRefSetIntermitantUOChangedTT);

        TreeSet<String> tCTBRefSetIntermitantUOMovedNotChangedTT;
        tCTBRefSetIntermitantUOMovedNotChangedTT = new TreeSet<String>();
        tCTBRefSetIntermitantUOMovedNotChangedTT.addAll(tCTBRefSetIntermitantUO);
        tCTBRefSetIntermitantUOMovedNotChangedTT.removeAll(tCTBRefSetNotMoved);
        tCTBRefSetIntermitantUOMovedNotChangedTT.removeAll(tCTBRefSetChangedTT);
        groups.put(sIntermitantUO__Moved_NotChangedTT, tCTBRefSetIntermitantUOMovedNotChangedTT);

        checkSets(
                tCTBRefs,
                tCouncilCTBRefs,
                tRSLCTBRefs,
                tIDSetCouncilUniqueClaimantsEffected,
                tIDSetCouncilUniquePartnersEffected,
                tCouncilMaxNumberOfDependentsInClaimWhenUO,
                tIDSetCouncilUniqueNonDependentsEffected,
                tIDSetRSLUniqueClaimantsEffected,
                tIDSetRSLUniquePartnersEffected,
                tRSLMaxNumberOfDependentsInClaimWhenUO,
                tIDSetRSLUniqueNonDependentsEffected,
                groups);

        result[0] = header;
        result[1] = tableValues;
        result[2] = tCTBRefs;
        result[3] = groups;
        result[4] = preUnderOccupancyValues;
        result[5] = DHP_Totals;
        return result;
    }

    protected void checkSets(
            HashSet<String> tCTBRefs,
            HashSet<String> tCouncilCTBRefs,
            HashSet<String> tRSLCTBRefs,
            HashSet<ID> tIDSetCouncilUniqueClaimantsEffected,
            HashSet<ID> tIDSetCouncilUniquePartnersEffected,
            HashMap<String, Integer> tCouncilMaxNumberOfDependentsInClaimWhenUO,
            HashSet<ID> tIDSetCouncilUniqueNonDependentsEffected,
            HashSet<ID> tIDSetRSLUniqueClaimantsEffected,
            HashSet<ID> tIDSetRSLUniquePartnersEffected,
            HashMap<String, Integer> tRSLMaxNumberOfDependentsInClaimWhenUO,
            HashSet<ID> tIDSetRSLUniqueNonDependentsEffected,
            HashMap<String, TreeSet<String>> groups) {
        HashSet<ID> tIDSetCouncilUniqueIndividualsEffected;
        tIDSetCouncilUniqueIndividualsEffected = new HashSet<ID>();
        tIDSetCouncilUniqueIndividualsEffected.addAll(tIDSetCouncilUniqueClaimantsEffected);
        tIDSetCouncilUniqueIndividualsEffected.addAll(tIDSetCouncilUniquePartnersEffected);
        tIDSetCouncilUniqueIndividualsEffected.addAll(tIDSetCouncilUniqueNonDependentsEffected);

        HashSet<ID> tIDSetRSLUniqueIndividualsEffected;
        tIDSetRSLUniqueIndividualsEffected = new HashSet<ID>();
        tIDSetRSLUniqueIndividualsEffected.addAll(tIDSetRSLUniqueClaimantsEffected);
        tIDSetRSLUniqueIndividualsEffected.addAll(tIDSetRSLUniquePartnersEffected);
        tIDSetRSLUniqueIndividualsEffected.addAll(tIDSetRSLUniqueNonDependentsEffected);

        HashSet<ID> tIDSetUniqueIndividualsEffected;
        tIDSetUniqueIndividualsEffected = new HashSet<ID>();
        tIDSetUniqueIndividualsEffected.addAll(tIDSetCouncilUniqueIndividualsEffected);
        tIDSetUniqueIndividualsEffected.addAll(tIDSetRSLUniqueIndividualsEffected);

        String aCTBRef;
        int m;
        Iterator<String> ite;

        long totalCouncilDependentsEffected;
        totalCouncilDependentsEffected = 0;
        ite = tCouncilMaxNumberOfDependentsInClaimWhenUO.keySet().iterator();
        while (ite.hasNext()) {
            aCTBRef = ite.next();
            m = tCouncilMaxNumberOfDependentsInClaimWhenUO.get(aCTBRef);
            totalCouncilDependentsEffected += m;
        }

        long totalRSLDependentsEffected;
        totalRSLDependentsEffected = 0;
        ite = tRSLMaxNumberOfDependentsInClaimWhenUO.keySet().iterator();
        while (ite.hasNext()) {
            aCTBRef = ite.next();
            m = tRSLMaxNumberOfDependentsInClaimWhenUO.get(aCTBRef);
            totalRSLDependentsEffected += m;
        }

        System.out.println("From April 2013 to October 2015 there were the "
                + "following counts of claims and individuals effected by "
                + "UnderOccupancy:");
        System.out.println("" + tCTBRefs.size()
                + " claims.");
        System.out.println("" + tCouncilCTBRefs.size()
                + " Council claims");
        System.out.println("" + tRSLCTBRefs.size()
                + " RSL claims");
        System.out.println("" + (tIDSetUniqueIndividualsEffected.size()
                + totalCouncilDependentsEffected + totalRSLDependentsEffected)
                + " unique individuals.");
        // Council
        System.out.println("" + (tIDSetCouncilUniqueIndividualsEffected.size()
                + totalCouncilDependentsEffected)
                + " Council unique individuals.");
        System.out.println("" + tIDSetCouncilUniqueClaimantsEffected.size()
                + " Total Council unique claimants.");
        System.out.println("" + tIDSetCouncilUniquePartnersEffected.size()
                + " Council unique partners.");
        System.out.println("" + totalCouncilDependentsEffected
                + " Council dependents.");
        System.out.println("" + tIDSetCouncilUniqueNonDependentsEffected.size()
                + " Council unique non-dependents.");
        // RSL
        System.out.println("" + (tIDSetRSLUniqueIndividualsEffected.size()
                + totalRSLDependentsEffected)
                + " RSL unique individuals.");
        System.out.println("" + tIDSetRSLUniqueClaimantsEffected.size()
                + " RSL unique claimants.");
        System.out.println("" + tIDSetRSLUniquePartnersEffected.size()
                + " RSL unique partners.");
        System.out.println("" + totalRSLDependentsEffected
                + " RSL dependents.");
        System.out.println("" + tIDSetRSLUniqueNonDependentsEffected.size()
                + " RSL unique non-dependents.");
    }

    public int process(
            String aCTBRef,
            DW_SHBE_Record aDW_SHBE_Record,
            TreeMap<String, DW_SHBE_Record> bRecords,
            TreeMap<String, String> tableValues,
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet1,
            DW_UnderOccupiedReport_Set RSLUnderOccupiedSet1,
            HashSet<ID> tIDSetCouncilUniqueClaimantsEffected,
            HashSet<ID> tIDSetCouncilUniquePartnersEffected,
            HashMap<String, Integer> tCouncilMaxNumberOfDependentsInClaimWhenUO,
            HashSet<ID> tIDSetCouncilUniqueNonDependentsEffected,
            HashSet<ID> tIDSetRSLUniqueClaimantsEffected,
            HashSet<ID> tIDSetRSLUniquePartnersEffected,
            HashMap<String, Integer> tRSLMaxNumberOfDependentsInClaimWhenUO,
            HashSet<ID> tIDSetRSLUniqueNonDependentsEffected,
            TreeSet<String> tCTBRefSetTraveller,
            TreeSet<String> tCTBRefSetTTNot1Or4AndUnderOccupying,
            TreeSet<String> tCTBRefSetTT1_To_TT3OrTT6,
            TreeSet<String> tCTBRefSetTT4_To_TT3OrTT6,
            TreeSet<String> tCTBRefSetTT3OrTT6_To_TT1,
            TreeSet<String> tCTBRefSetTT3OrTT6_To_TT4,
            //TreeSet<String> tCTBRefSetNotMoved,
            TreeSet<String> tCTBRefSetMoved,
            TreeSet<String> tCTBRefSetChangedTT,
            TreeSet<String> tCTBRefSetAlwaysUOFromStart,
            TreeSet<String> tCTBRefSetAlwaysUOFromWhenStarted,
            TreeSet<String> tCTBRefSetIntermitantUO
    ) {
        int result; // HBDP (DHP)
        //tClaimantID = tCTBRefToClaimantIDLookup.get(tCTBRef);
        String aS;
        String key;
        DW_SHBE_D_Record aDW_SHBE_D_Record = null;
        boolean isPairedRecord;
        int aTT;
        String aPC;
        int aWHBE;
        int aWERA;
        long aHS;
        long aND;
        long aCD;
        String aPDD;
        String aDoB;
        String aD;
        String aDC;
        int aHBDP = 0;
//        int aCTBDP = 0;
        Double aArrears;
        DW_SHBE_D_Record bDW_SHBE_D_Record;
        int bTT;
        String bPC;
        int bWHBE;
        int bWERA = 0;
        long bHS = 0;
        long bND = 0;
        long bCD = 0;
        String bPDD;
        String bDoB;
//        String bD = "";
//        int bHBDP = 0;
//        int bCTBDP = 0;
        DW_SHBE_Record bDW_SHBE_Record;
        if (bRecords == null) {
            bDW_SHBE_Record = null;
        } else {
            bDW_SHBE_Record = bRecords.get(aCTBRef);
        }
        if (aDW_SHBE_Record == null) {
            isPairedRecord = false;
            aTT = DW_SHBE_TenancyType_Handler.iMinus999;
            aPC = defaultPostcode;
            aWHBE = 0;
            aWERA = 0;
            aHS = 0;
            aND = 0;
            aCD = 0;
            aPDD = s;
            aDoB = s;
            aD = s;
            aDC = s;
            aHBDP = 0;
//            aCTBDP = 0;
        } else {
            aDW_SHBE_D_Record = aDW_SHBE_Record.getDRecord();
            aTT = aDW_SHBE_D_Record.getTenancyType();
            isPairedRecord = aDW_SHBE_Record.isPairedRecord();
            if (isPairedRecord) {
                tCTBRefSetTraveller.add(aCTBRef);
            }
//            if (aTT == 3) {
//                tCTBRefSetTT3.add(aCTBRef);
//            }
            aPC = aDW_SHBE_D_Record.getClaimantsPostcode();
            if (aPC.isEmpty()) {
                aPC = defaultPostcode;
            }
            aWHBE = aDW_SHBE_D_Record.getWeeklyHousingBenefitEntitlement();
            aWERA = aDW_SHBE_D_Record.getWeeklyEligibleRentAmount();
            aHS = DW_SHBE_Handler.getHouseholdSize(aDW_SHBE_D_Record);
            aND = aDW_SHBE_D_Record.getNumberOfNonDependents();
            aCD = aDW_SHBE_D_Record.getNumberOfChildDependents();
            aDoB = aDW_SHBE_D_Record.getClaimantsDateOfBirth();
            if (DW_SHBE_Handler.getDisability(aDW_SHBE_D_Record)) {
                aD = sD;
            } else {
                aD = s;
            }
            // Disability Premium
            if (aDW_SHBE_D_Record.getDisabilityPremiumAwarded() == 1) {
                aDC = sDC;
            } else {
                aDC = s;
            }
            // Disability Severe
            if (aDW_SHBE_D_Record.getSevereDisabilityPremiumAwarded() == 1) {
                aDC = sDC;
            } else {
                aDC = s;
            }
            // Disability Enhanced
            if (aDW_SHBE_D_Record.getEnhancedDisabilityPremiumAwarded() == 1) {
                aDC = sDC;
            } else {
                aDC = s;
            }
            // Disabiled Child
            if (aDW_SHBE_D_Record.getDisabledChildPremiumAwarded() == 1) {
                aDC = sDC;
            } else {
                aDC = s;
            }
            aPDD = sPDeath + sUnderscore + aDW_SHBE_D_Record.getPartnersDateOfDeath();
            aHBDP = aDW_SHBE_D_Record.getWeeklyAdditionalDiscretionaryPayment();
//            aCTBDP = aDW_SHBE_D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
        }
        if (bDW_SHBE_Record == null) {
            bTT = DW_SHBE_TenancyType_Handler.iMinus999;
            bPC = defaultPostcode;
            bWHBE = 0;
            bWERA = 0;
            bHS = 0;
            bND = 0;
            bCD = 0;
            bPDD = s;
            bDoB = s;
//            bD = "";
//            bHBDP = 0;
//            bCTBDP = 0;
        } else {
            bDW_SHBE_D_Record = bDW_SHBE_Record.getDRecord();
            bTT = bDW_SHBE_D_Record.getTenancyType();
            bPC = bDW_SHBE_D_Record.getClaimantsPostcode();
            if (bPC.isEmpty()) {
                bPC = defaultPostcode;
            }
            bWHBE = bDW_SHBE_D_Record.getWeeklyHousingBenefitEntitlement();
            bWERA = bDW_SHBE_D_Record.getWeeklyEligibleRentAmount();
            bHS = DW_SHBE_Handler.getHouseholdSize(bDW_SHBE_D_Record);
            bND = bDW_SHBE_D_Record.getNumberOfNonDependents();
            bCD = bDW_SHBE_D_Record.getNumberOfChildDependents();
            bPDD = sPDeath + sUnderscore + bDW_SHBE_D_Record.getPartnersDateOfDeath();
            bDoB = bDW_SHBE_D_Record.getClaimantsDateOfBirth();
//            if (DW_SHBE_Handler.getDisability(bDW_SHBE_D_Record)) {
//                bD = "D";
//            }
//            bHBDP = bDW_SHBE_D_Record.getWeeklyAdditionalDiscretionaryPayment();
//            bCTBDP = bDW_SHBE_D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
        }

        // TenancyType
        key = aCTBRef + sUnderscore + sTT;
        aS = tableValues.get(key);
        if (aTT != bTT) {
            if (aTT == DW_SHBE_TenancyType_Handler.iMinus999
                    || bTT == DW_SHBE_TenancyType_Handler.iMinus999) {
                if (bTT == DW_SHBE_TenancyType_Handler.iMinus999) {
                    // Check if there is another TT in aS
                    boolean isAnotherTT;
                    isAnotherTT = isAnotherTT(aTT, aS);
                    if (isAnotherTT) {
                        tCTBRefSetChangedTT.add(aCTBRef);
                    }
                }
            } else {
                tCTBRefSetChangedTT.add(aCTBRef);
                if (aTT == 3 || aTT == 6) {
                    if (bTT == 1) {
                        tCTBRefSetTT1_To_TT3OrTT6.add(aCTBRef);
                    } else {
                        if (bTT == 4) {
                            tCTBRefSetTT4_To_TT3OrTT6.add(aCTBRef);
                        }
                    }
                } else {
                    if (bTT == 3 || aTT == 6) {
                        if (aTT == 1) {
                            tCTBRefSetTT3OrTT6_To_TT1.add(aCTBRef);
                        } else {
                            if (aTT == 4) {
                                tCTBRefSetTT3OrTT6_To_TT4.add(aCTBRef);
                            }
                        }
                    }
                }
            }
        }
        aS += sCommaSpace + sTT_ + aTT;
        tableValues.put(key, aS);

        // UnderOccupancy
        key = aCTBRef + sUnderscore + sU;
        aS = tableValues.get(key);
        if (councilUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)
                || RSLUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)) {
            tCTBRefSetAlwaysUOFromWhenStarted.add(aCTBRef);
            aS += sCommaSpace + sU;
            if (aDW_SHBE_D_Record != null) {
                if (councilUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)) {
                    addToSets(
                            aCTBRef,
                            tIDSetCouncilUniqueClaimantsEffected,
                            tIDSetCouncilUniquePartnersEffected,
                            tCouncilMaxNumberOfDependentsInClaimWhenUO,
                            tIDSetCouncilUniqueNonDependentsEffected,
                            aDW_SHBE_Record,
                            aDW_SHBE_D_Record);
                }
                if (RSLUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)) {
                    addToSets(
                            aCTBRef,
                            tIDSetRSLUniqueClaimantsEffected,
                            tIDSetRSLUniquePartnersEffected,
                            tRSLMaxNumberOfDependentsInClaimWhenUO,
                            tIDSetRSLUniqueNonDependentsEffected,
                            aDW_SHBE_Record,
                            aDW_SHBE_D_Record);
                }
            }
            if (!(aTT == 1 || aTT == 4 || aTT == DW_SHBE_TenancyType_Handler.iMinus999)) {
                tCTBRefSetTTNot1Or4AndUnderOccupying.add(aCTBRef);
            }
        } else {
            aS += sCommaSpace;
            tCTBRefSetAlwaysUOFromStart.remove(aCTBRef);
            if (aS.contains(sU)) {
                tCTBRefSetAlwaysUOFromWhenStarted.remove(aCTBRef);
                if (aS.contains(sU + sCommaSpace + sCommaSpace)) {
                    // ..., U, ,
                    tCTBRefSetIntermitantUO.add(aCTBRef);
                }
            }
        }
        tableValues.put(key, aS);

        // Postcode
        key = aCTBRef + sUnderscore + sP;
        aS = tableValues.get(key);
        if (aPC.equalsIgnoreCase(bPC)) {
            aS += sCommaSpace;
        } else {
            boolean aSContainsaPC = aS.contains(aPC);
            aS += sCommaSpace + aPC;
            if (!aPC.equalsIgnoreCase(defaultPostcode)) {
                boolean containsAnotherPostcode;
                if (bPC.equalsIgnoreCase(defaultPostcode)) {
                    if (aSContainsaPC) {
                        containsAnotherPostcode = getContainsAnotherPostcode(aS, aPC);
                        if (containsAnotherPostcode) {
                            boolean likelyTraveller;
                            likelyTraveller = getLikelyTraveller(aS, aPC);
                            if (likelyTraveller) {
                                tCTBRefSetTraveller.add(aCTBRef);
                            }
                            //tCTBRefSetNotMoved.remove(aCTBRef);
                            tCTBRefSetMoved.add(aCTBRef);
                        }
                    } else {
                        containsAnotherPostcode = getContainsAnotherPostcode(aS, aPC);
                        if (containsAnotherPostcode) {
                            //tCTBRefSetNotMoved.remove(aCTBRef);
                            tCTBRefSetMoved.add(aCTBRef);
                        }
                    }
                } else {
                    //tCTBRefSetNotMoved.remove(aCTBRef);
                    tCTBRefSetMoved.add(aCTBRef);
                    if (aSContainsaPC) {
//                        containsAnotherPostcode = getContainsAnotherPostcode(aS, aPC);
//                        if (containsAnotherPostcode) {
                        boolean likelyTraveller;
                        likelyTraveller = getLikelyTraveller(aS, aPC);
                        if (likelyTraveller) {
                            tCTBRefSetTraveller.add(aCTBRef);
                        }
//                        }
//                        tCTBRefSetNotMoved.remove(aCTBRef);
//                    } else {
//                        containsAnotherPostcode = getContainsAnotherPostcode(aS, aPC);
//                        if (containsAnotherPostcode) {
//                            tCTBRefSetNotMoved.remove(aCTBRef);
//                        }
                    }
                }
            }
        }
        tableValues.put(key, aS);

        // HB Entitlement
        key = aCTBRef + sUnderscore + sWHBE;
        aS = tableValues.get(key);
        if (aWHBE == bWHBE) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aWHBE;
        }
        tableValues.put(key, aS);

        // ERA
        key = aCTBRef + sUnderscore + sWERA;
        aS = tableValues.get(key);
        if (aWERA == bWERA) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aWERA;
        }
        tableValues.put(key, aS);

        // HS
        key = aCTBRef + sUnderscore + sHS;
        aS = tableValues.get(key);
        if (aHS == bHS) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aHS;
        }
        tableValues.put(key, aS);

        // NonDependents
        key = aCTBRef + sUnderscore + sND;
        aS = tableValues.get(key);
        if (aND == bND) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aND;
        }
        tableValues.put(key, aS);

        // ChildDependents
        key = aCTBRef + sUnderscore + sCD;
        aS = tableValues.get(key);
        if (aCD == bCD) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aCD;
        }
        tableValues.put(key, aS);

        // UO
        DW_UOReport_Record aDW_UOReport_Record;
        if (councilUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)
                || RSLUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)) {
            if (councilUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)) {
                aDW_UOReport_Record = councilUnderOccupiedSet1.getMap().get(aCTBRef);
            } else {
                aDW_UOReport_Record = RSLUnderOccupiedSet1.getMap().get(aCTBRef);
            }
            // NonDependents
            key = aCTBRef + sUnderscore + sND;
            aS = tableValues.get(key);
            aS += sCommaSpace + aDW_UOReport_Record.getNonDependents();
            tableValues.put(key, aS);
            // Children 16 +
            key = aCTBRef + sUnderscore + sCO16;
            aS = tableValues.get(key);
            aS += sCommaSpace + aDW_UOReport_Record.getChildrenOver16();
            tableValues.put(key, aS);
            // FemaleChildrenUnder10
            key = aCTBRef + sUnderscore + sFCU10;
            aS = tableValues.get(key);
            aS += sCommaSpace + aDW_UOReport_Record.getFemaleChildrenUnder10();
            tableValues.put(key, aS);
            // MaleChildrenUnder10
            key = aCTBRef + sUnderscore + sMCU10;
            aS = tableValues.get(key);
            aS += sCommaSpace + aDW_UOReport_Record.getMaleChildrenUnder10();
            tableValues.put(key, aS);
            // FemaleChildren10to16
            key = aCTBRef + sUnderscore + sFC10To16;
            aS = tableValues.get(key);
            aS += sCommaSpace + aDW_UOReport_Record.getFemaleChildren10to16();
            tableValues.put(key, aS);
            // MaleChildren10to16
            key = aCTBRef + sUnderscore + sMC10To16;
            aS = tableValues.get(key);
            aS += sCommaSpace + aDW_UOReport_Record.getMaleChildren10to16();
            tableValues.put(key, aS);
            // Number of Bedrooms
            key = aCTBRef + sUnderscore + sNB;
            aS = tableValues.get(key);
            aS += sCommaSpace + aDW_UOReport_Record.getBedroomsInProperty();
            tableValues.put(key, aS);
            // Bedroom Requirement
            key = aCTBRef + sUnderscore + sBR;
            aS = tableValues.get(key);
            aS += sCommaSpace + aDW_UOReport_Record.getBedroomRequirement();
            tableValues.put(key, aS);
        } else {
            // NonDependents
            key = aCTBRef + sUnderscore + sND;
            aS = tableValues.get(key);
            aS += sCommaSpace;
            tableValues.put(key, aS);
            // Children 16 +
            key = aCTBRef + sUnderscore + sCO16;
            aS = tableValues.get(key);
            aS += sCommaSpace;
            tableValues.put(key, aS);
            // FemaleChildrenUnder10
            key = aCTBRef + sUnderscore + sFCU10;
            aS = tableValues.get(key);
            aS += sCommaSpace;
            tableValues.put(key, aS);
            // MaleChildrenUnder10
            key = aCTBRef + sUnderscore + sMCU10;
            aS = tableValues.get(key);
            aS += sCommaSpace;
            tableValues.put(key, aS);
            // FemaleChildren10to16
            key = aCTBRef + sUnderscore + sFC10To16;
            aS = tableValues.get(key);
            aS += sCommaSpace;
            tableValues.put(key, aS);
            // MaleChildren10to16
            key = aCTBRef + sUnderscore + sMC10To16;
            aS = tableValues.get(key);
            aS += sCommaSpace;
            tableValues.put(key, aS);
            // Number of Bedrooms
            key = aCTBRef + sUnderscore + sNB;
            aS = tableValues.get(key);
            aS += sCommaSpace;
            tableValues.put(key, aS);
            // Bedroom Requirement
            key = aCTBRef + sUnderscore + sBR;
            aS = tableValues.get(key);
            aS += sCommaSpace;
            tableValues.put(key, aS);
        }

        // Claimant DoB
        key = aCTBRef + sUnderscore + sDoB;
        aS = tableValues.get(key);
        if (aDoB.equalsIgnoreCase(bDoB)) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aDoB;
        }
        tableValues.put(key, aS);
        // Disability
        key = aCTBRef + sUnderscore + sD;
        aS = tableValues.get(key);
        aS += sCommaSpace + aD;
        tableValues.put(key, aS);
// Disability Premium
        key = aCTBRef + sUnderscore + sDP;
        aS = tableValues.get(key);
        aS += sCommaSpace + aD;
        tableValues.put(key, aS);
        // Disability Severe
        key = aCTBRef + sUnderscore + sDS;
        aS = tableValues.get(key);
        aS += sCommaSpace + aD;
        tableValues.put(key, aS);
        // Disability Enhanced
        key = aCTBRef + sUnderscore + sDE;
        aS = tableValues.get(key);
        aS += sCommaSpace + aD;
        tableValues.put(key, aS);
        // Child Disability
        key = aCTBRef + sUnderscore + sDC;
        aS = tableValues.get(key);
        aS += sCommaSpace + aDC;
        tableValues.put(key, aS);
        // Claimant DoB
        key = aCTBRef + sUnderscore + sDoB;
        aS = tableValues.get(key);
        if (aDoB.equalsIgnoreCase(bDoB)) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aDoB;
        }
        tableValues.put(key, aS);

        // Partner Death
        key = aCTBRef + sUnderscore + sPDeath;
        aS = tableValues.get(key);
        if (aPDD.equalsIgnoreCase(bPDD)) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aPDD;
        }
        tableValues.put(key, aS);

        // HBDP
        key = aCTBRef + sUnderscore + sHBDP;
        aS = tableValues.get(key);
        if (aHBDP > 0) {
            //aS += sCommaSpace + sHBDP + sUnderscore + aHBDP;
            aS += sCommaSpace + aHBDP;
        } else {
            aS += sCommaSpace;
        }
        tableValues.put(key, aS);

//        // CTBDP
//        key = aCTBRef + sUnderscore + sCTBDP;
//        aS = tableValues.get(key);
//        if (aCTBDP > 0) {
//            aS += sCommaSpace + sCTBDP + sUnderscore + aCTBDP;
//        } else {
//            aS += sCommaSpace;
//        }
//        tableValues.put(key, aS);
        // Arrears
        key = aCTBRef + sUnderscore + sA;
        aS = tableValues.get(key);
        if (councilUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)) {
            DW_UOReport_Record UORec;
            UORec = councilUnderOccupiedSet1.getMap().get(aCTBRef);
            if (UORec == null) {
                aS += sCommaSpace;
            } else {
                aArrears = UORec.getTotalRentArrears();
                if (aArrears == null) {
                    aS += sCommaSpace;
                } else {
                    aS += sCommaSpace + aArrears;
                }
            }
        } else {
            aS += sCommaSpace;
        }
        tableValues.put(key, aS);
        result = aHBDP;
        return result;
    }

    /**
     * Already in the context from where this is called, it is known that aS
     * prior to adding aPC at the end contained another instance of aPC.
     *
     * @param aS
     * @param aPC
     * @return
     */
    protected boolean getLikelyTraveller(
            String aS,
            String aPC) {
        boolean result;
        result = false;
        String[] split;
        split = aS.split(sComma);
        boolean firstIsTheSame = false;
        String s1;
        for (int i = split.length - 2; i > -1; i--) { // We don't go from the very end as we already added aPC to the end!
            s1 = split[i].trim();
            if (!s1.isEmpty()) {
                if (!(s1.equalsIgnoreCase(defaultPostcode))) {
                    if (s1.equalsIgnoreCase(aPC)) {
                        firstIsTheSame = true;
                    } else {
                        if (!firstIsTheSame) {
                            return true;
                        }
                    }
                }
            }
        }
        return result;
    }

    public boolean getContainsAnotherPostcode(
            String aS,
            String aPC) {
        boolean result;
        result = false;
        String[] split;
        split = aS.split(sComma);
        String s1;
        for (String split1 : split) {
            s1 = split1.trim();
            if (!s1.isEmpty()) {
                if (!(s1.equalsIgnoreCase(defaultPostcode))) {
                    if (!(s1.equalsIgnoreCase(aPC))) {
                        return true;
                    }
                }
            }
        }
        return result;
    }

    public boolean isAnotherTT(int aTT, String aS) {
        boolean result = false;
        switch (aTT) {
            case 1:
                if (aS.contains("TT_2")
                        || aS.contains("TT_3")
                        || aS.contains("TT_4")
                        || aS.contains("TT_5")
                        || aS.contains("TT_6")
                        || aS.contains("TT_7")
                        || aS.contains("TT_8")
                        || aS.contains("TT_9")) {
                    return true;
                }
                break;
            case 2:
                if (aS.contains("TT_1")
                        || aS.contains("TT_3")
                        || aS.contains("TT_4")
                        || aS.contains("TT_5")
                        || aS.contains("TT_6")
                        || aS.contains("TT_7")
                        || aS.contains("TT_8")
                        || aS.contains("TT_9")) {
                    return true;
                }
                break;
            case 3:
                if (aS.contains("TT_1")
                        || aS.contains("TT_2")
                        || aS.contains("TT_4")
                        || aS.contains("TT_5")
                        || aS.contains("TT_6")
                        || aS.contains("TT_7")
                        || aS.contains("TT_8")
                        || aS.contains("TT_9")) {
                    return true;
                }
                break;
            case 4:
                if (aS.contains("TT_1")
                        || aS.contains("TT_2")
                        || aS.contains("TT_3")
                        || aS.contains("TT_5")
                        || aS.contains("TT_6")
                        || aS.contains("TT_7")
                        || aS.contains("TT_8")
                        || aS.contains("TT_9")) {
                    return true;
                }
                break;
            case 5:
                if (aS.contains("TT_1")
                        || aS.contains("TT_2")
                        || aS.contains("TT_3")
                        || aS.contains("TT_4")
                        || aS.contains("TT_6")
                        || aS.contains("TT_7")
                        || aS.contains("TT_8")
                        || aS.contains("TT_9")) {
                    return true;
                }
                break;
            case 6:
                if (aS.contains("TT_1")
                        || aS.contains("TT_2")
                        || aS.contains("TT_3")
                        || aS.contains("TT_4")
                        || aS.contains("TT_5")
                        || aS.contains("TT_7")
                        || aS.contains("TT_8")
                        || aS.contains("TT_9")) {
                    return true;
                }
                break;
            case 7:
                if (aS.contains("TT_1")
                        || aS.contains("TT_2")
                        || aS.contains("TT_3")
                        || aS.contains("TT_4")
                        || aS.contains("TT_5")
                        || aS.contains("TT_6")
                        || aS.contains("TT_8")
                        || aS.contains("TT_9")) {
                    return true;
                }
                break;
            case 8:
                if (aS.contains("TT_1")
                        || aS.contains("TT_2")
                        || aS.contains("TT_3")
                        || aS.contains("TT_4")
                        || aS.contains("TT_5")
                        || aS.contains("TT_6")
                        || aS.contains("TT_7")
                        || aS.contains("TT_9")) {
                    return true;
                }
                break;
            case 9:
                if (aS.contains("TT_1")
                        || aS.contains("TT_2")
                        || aS.contains("TT_3")
                        || aS.contains("TT_4")
                        || aS.contains("TT_5")
                        || aS.contains("TT_6")
                        || aS.contains("TT_7")
                        || aS.contains("TT_8")) {
                    return true;
                }
                break;
        }
        return result;
    }

    public HashSet<String>[] getCTBRefs(
            TreeMap<String, DW_UnderOccupiedReport_Set> councilUnderOccupiedSets,
            TreeMap<String, DW_UnderOccupiedReport_Set> RSLUnderOccupiedSets,
            String[] SHBEFilenames,
            ArrayList<Integer> include
    ) {

        HashSet<String>[] result;
        result = new HashSet[2];
        result[0] = new HashSet<String>();
        result[1] = new HashSet<String>();
        String yM31 = s;
        DW_UnderOccupiedReport_Set councilUnderOccupiedSet1 = null;
        DW_UnderOccupiedReport_Set RSLUnderOccupiedSet1 = null;
        String filename1 = null;
        Iterator<Integer> includeIte;
        includeIte = include.iterator();
        int i;
        while (includeIte.hasNext()) {
            i = includeIte.next();
            // Load first data
            filename1 = SHBEFilenames[i];
            yM31 = DW_SHBE_Handler.getYM3(filename1);
            councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(yM31);
            if (councilUnderOccupiedSet1 != null) {
                RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(yM31);
                // Add to result
                result[0].addAll(councilUnderOccupiedSet1.getMap().keySet());
                result[1].addAll(RSLUnderOccupiedSet1.getMap().keySet());
            }
        }
        return result;
    }

    public void writeTenancyChangeTables(
            Object[] table,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            boolean includePreUnderOccupancyValues,
            String name
    ) {
        String header;
        header = (String) table[0];
        TreeMap<String, String> tableValues;
        tableValues = (TreeMap<String, String>) table[1];
        HashSet<String> tCTBRefs;
        tCTBRefs = (HashSet<String>) table[2];
        HashMap<String, TreeSet<String>> groups;
        groups = (HashMap<String, TreeSet<String>>) table[3];
        TreeMap<String, String> preUnderOccupancyValues;
        preUnderOccupancyValues = (TreeMap<String, String>) table[4];
        HashMap<String, Integer> tHBDPTotals;
        tHBDPTotals = (HashMap<String, Integer>) table[5];
        TreeMap<String, String> groupNameDescriptions;
        groupNameDescriptions = getGroupNameDescriptions(groups.keySet());

        HashSet<String> tCTBRefsCheck;
        tCTBRefsCheck = new HashSet<String>();
        boolean check = true;

        String groupName;
        PrintWriter pw;
        PrintWriter pw2;
        String name2;
        String groupNameDescription;
        TreeSet<String> group;
        String aCTBRef;
        Iterator<String> iteG;
        Iterator<String> ite;
        int counter;

        System.out.println("Group Size, Not previously exported, GroupName");

        iteG = groupNameDescriptions.keySet().iterator();
        while (iteG.hasNext()) {
            groupName = iteG.next();
            name2 = name + sUnderscore + groupName;
            if (includePreUnderOccupancyValues) {
                name2 += sUnderscore + "IncludesPreUnderOccupancyValues";
            }
            pw = getPrintWriter(name2, paymentType, includeKey, underOccupancy);
            name2 += sUnderscore + "WithDuplicates";
            pw2 = getPrintWriter(name2, paymentType, includeKey, underOccupancy);
            // Write header
            groupNameDescription = groupNameDescriptions.get(groupName);
            pw.println(groupNameDescription);
            pw.println(header);
            pw2.println(groupNameDescription);
            pw2.println(header);
            group = groups.get(groupName);
            counter = 0;
            ite = group.iterator();
            while (ite.hasNext()) {
                aCTBRef = ite.next();
                check = tCTBRefsCheck.add(aCTBRef);
                if (check == false) {
                    String otherGroupName;
                    TreeSet<String> otherGroup;
                    if (groupName.equalsIgnoreCase(sTTNot1Or4AndUnderOccupying)) {
                        otherGroupName = sTravellers;
                        otherGroup = groups.get(otherGroupName);
                        if (otherGroup.contains(aCTBRef)) {
                            writeRecordCollection(
                                    tableValues,
                                    tHBDPTotals,
                                    includePreUnderOccupancyValues,
                                    preUnderOccupancyValues,
                                    aCTBRef,
                                    pw2);
                        } else {
                            System.out.println(
                                    "CTBRef " + aCTBRef
                                    + " is in group " + groupName
                                    + " and is in one of the not "
                                    + "expected other groups "
                                    + "previously written out.");
                        }
                    } else {
                        if (groupName.equalsIgnoreCase(sTT1_To_TT3OrTT6)) {
                            otherGroupName = sTravellers;
                            otherGroup = groups.get(otherGroupName);
                            if (otherGroup.contains(aCTBRef)) {
                                writeRecordCollection(
                                        tableValues,
                                        tHBDPTotals,
                                        includePreUnderOccupancyValues,
                                        preUnderOccupancyValues,
                                        aCTBRef,
                                        pw2);
                            } else {
                                otherGroupName = sTTNot1Or4AndUnderOccupying;
                                otherGroup = groups.get(otherGroupName);
                                if (otherGroup.contains(aCTBRef)) {
                                    writeRecordCollection(
                                            tableValues,
                                            tHBDPTotals,
                                            includePreUnderOccupancyValues,
                                            preUnderOccupancyValues,
                                            aCTBRef,
                                            pw2);
                                } else {
                                    System.out.println(
                                            "CTBRef " + aCTBRef
                                            + " is in group " + groupName
                                            + " and is in one of the not "
                                            + "expected other groups "
                                            + "previously written out.");
                                }
                            }
                        } else {
                            if (groupName.equalsIgnoreCase(sTT4_To_TT3OrTT6)) {
                                otherGroupName = sTravellers;
                                otherGroup = groups.get(otherGroupName);
                                if (otherGroup.contains(aCTBRef)) {
                                    writeRecordCollection(
                                            tableValues,
                                            tHBDPTotals,
                                            includePreUnderOccupancyValues,
                                            preUnderOccupancyValues,
                                            aCTBRef,
                                            pw2);
                                } else {
                                    otherGroupName = sTTNot1Or4AndUnderOccupying;
                                    otherGroup = groups.get(otherGroupName);
                                    if (otherGroup.contains(aCTBRef)) {
                                        writeRecordCollection(
                                                tableValues,
                                                tHBDPTotals,
                                                includePreUnderOccupancyValues,
                                                preUnderOccupancyValues,
                                                aCTBRef,
                                                pw2);
                                    } else {
                                        otherGroupName = sTT1_To_TT3OrTT6;
                                        otherGroup = groups.get(otherGroupName);
                                        if (otherGroup.contains(aCTBRef)) {
                                            writeRecordCollection(
                                                    tableValues,
                                                    tHBDPTotals,
                                                    includePreUnderOccupancyValues,
                                                    preUnderOccupancyValues,
                                                    aCTBRef,
                                                    pw2);
                                        } else {
                                            System.out.println(
                                                    "CTBRef " + aCTBRef
                                                    + " is in group " + groupName
                                                    + " and is in one of the not "
                                                    + "expected other groups "
                                                    + "previously written out.");
                                        }
                                    }
                                }
                            } else {
                                if (groupName.equalsIgnoreCase(sTT3OrTT6_To_TT1)) {
                                    otherGroupName = sTravellers;
                                    otherGroup = groups.get(otherGroupName);
                                    if (otherGroup.contains(aCTBRef)) {
                                        writeRecordCollection(
                                                tableValues,
                                                tHBDPTotals,
                                                includePreUnderOccupancyValues,
                                                preUnderOccupancyValues,
                                                aCTBRef,
                                                pw2);
                                    } else {
                                        otherGroupName = sTTNot1Or4AndUnderOccupying;
                                        otherGroup = groups.get(otherGroupName);
                                        if (otherGroup.contains(aCTBRef)) {
                                            writeRecordCollection(
                                                    tableValues,
                                                    tHBDPTotals,
                                                    includePreUnderOccupancyValues,
                                                    preUnderOccupancyValues,
                                                    aCTBRef,
                                                    pw2);
                                        } else {
                                            otherGroupName = sTT1_To_TT3OrTT6;
                                            otherGroup = groups.get(otherGroupName);
                                            if (otherGroup.contains(aCTBRef)) {
                                                writeRecordCollection(
                                                        tableValues,
                                                        tHBDPTotals,
                                                        includePreUnderOccupancyValues,
                                                        preUnderOccupancyValues,
                                                        aCTBRef,
                                                        pw2);
                                            } else {
                                                otherGroupName = sTT4_To_TT3OrTT6;
                                                otherGroup = groups.get(otherGroupName);
                                                if (otherGroup.contains(aCTBRef)) {
                                                    writeRecordCollection(
                                                            tableValues,
                                                            tHBDPTotals,
                                                            includePreUnderOccupancyValues,
                                                            preUnderOccupancyValues,
                                                            aCTBRef,
                                                            pw2);
                                                } else {
                                                    System.out.println(
                                                            "CTBRef " + aCTBRef
                                                            + " is in group " + groupName
                                                            + " and is in one of the not "
                                                            + "expected other groups "
                                                            + "previously written out.");
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (groupName.equalsIgnoreCase(sTT3OrTT6_To_TT4)) {
                                        otherGroupName = sTravellers;
                                        otherGroup = groups.get(otherGroupName);
                                        if (otherGroup.contains(aCTBRef)) {
                                            writeRecordCollection(
                                                    tableValues,
                                                    tHBDPTotals,
                                                    includePreUnderOccupancyValues,
                                                    preUnderOccupancyValues,
                                                    aCTBRef,
                                                    pw2);
                                        } else {
                                            otherGroupName = sTTNot1Or4AndUnderOccupying;
                                            otherGroup = groups.get(otherGroupName);
                                            if (otherGroup.contains(aCTBRef)) {
                                                writeRecordCollection(
                                                        tableValues,
                                                        tHBDPTotals,
                                                        includePreUnderOccupancyValues,
                                                        preUnderOccupancyValues,
                                                        aCTBRef,
                                                        pw2);
                                            } else {
                                                otherGroupName = sTT1_To_TT3OrTT6;
                                                otherGroup = groups.get(otherGroupName);
                                                if (otherGroup.contains(aCTBRef)) {
                                                    writeRecordCollection(
                                                            tableValues,
                                                            tHBDPTotals,
                                                            includePreUnderOccupancyValues,
                                                            preUnderOccupancyValues,
                                                            aCTBRef,
                                                            pw2);
                                                } else {
                                                    otherGroupName = sTT4_To_TT3OrTT6;
                                                    otherGroup = groups.get(otherGroupName);
                                                    if (otherGroup.contains(aCTBRef)) {
                                                        writeRecordCollection(
                                                                tableValues,
                                                                tHBDPTotals,
                                                                includePreUnderOccupancyValues,
                                                                preUnderOccupancyValues,
                                                                aCTBRef,
                                                                pw2);
                                                    } else {
                                                        otherGroupName = sTT3OrTT6_To_TT1;
                                                        otherGroup = groups.get(otherGroupName);
                                                        if (otherGroup.contains(aCTBRef)) {
                                                            writeRecordCollection(
                                                                    tableValues,
                                                                    tHBDPTotals,
                                                                    includePreUnderOccupancyValues,
                                                                    preUnderOccupancyValues,
                                                                    aCTBRef,
                                                                    pw2);
                                                        } else {
                                                            System.out.println(
                                                                    "CTBRef " + aCTBRef
                                                                    + " is in group " + groupName
                                                                    + " and is in one of the not "
                                                                    + "expected other groups "
                                                                    + "previously written out.");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        if (groupName.equalsIgnoreCase(sAlwaysUOFromWhenStarted__ChangedTT)) {
                                            otherGroupName = sTravellers;
                                            otherGroup = groups.get(otherGroupName);
                                            if (otherGroup.contains(aCTBRef)) {
                                                writeRecordCollection(
                                                        tableValues,
                                                        tHBDPTotals,
                                                        includePreUnderOccupancyValues,
                                                        preUnderOccupancyValues,
                                                        aCTBRef,
                                                        pw2);
                                            } else {
                                                otherGroupName = sTTNot1Or4AndUnderOccupying;
                                                otherGroup = groups.get(otherGroupName);
                                                if (otherGroup.contains(aCTBRef)) {
                                                    writeRecordCollection(
                                                            tableValues,
                                                            tHBDPTotals,
                                                            includePreUnderOccupancyValues,
                                                            preUnderOccupancyValues,
                                                            aCTBRef,
                                                            pw2);
                                                } else {
                                                    otherGroupName = sTT1_To_TT3OrTT6;
                                                    otherGroup = groups.get(otherGroupName);
                                                    if (otherGroup.contains(aCTBRef)) {
                                                        writeRecordCollection(
                                                                tableValues,
                                                                tHBDPTotals,
                                                                includePreUnderOccupancyValues,
                                                                preUnderOccupancyValues,
                                                                aCTBRef,
                                                                pw2);
                                                    } else {
                                                        otherGroupName = sTT4_To_TT3OrTT6;
                                                        otherGroup = groups.get(otherGroupName);
                                                        if (otherGroup.contains(aCTBRef)) {
                                                            writeRecordCollection(
                                                                    tableValues,
                                                                    tHBDPTotals,
                                                                    includePreUnderOccupancyValues,
                                                                    preUnderOccupancyValues,
                                                                    aCTBRef,
                                                                    pw2);
                                                        } else {
                                                            otherGroupName = sTT3OrTT6_To_TT1;
                                                            otherGroup = groups.get(otherGroupName);
                                                            if (otherGroup.contains(aCTBRef)) {
                                                                writeRecordCollection(
                                                                        tableValues,
                                                                        tHBDPTotals,
                                                                        includePreUnderOccupancyValues,
                                                                        preUnderOccupancyValues,
                                                                        aCTBRef,
                                                                        pw2);
                                                            } else {
                                                                otherGroupName = sTT3OrTT6_To_TT4;
                                                                otherGroup = groups.get(otherGroupName);
                                                                if (otherGroup.contains(aCTBRef)) {
                                                                    writeRecordCollection(
                                                                            tableValues,
                                                                            tHBDPTotals,
                                                                            includePreUnderOccupancyValues,
                                                                            preUnderOccupancyValues,
                                                                            aCTBRef,
                                                                            pw2);
                                                                } else {
                                                                    System.out.println(
                                                                            "CTBRef " + aCTBRef
                                                                            + " is in group " + groupName
                                                                            + " and is in one of the not "
                                                                            + "expected other groups "
                                                                            + "previously written out.");
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            if (groupName.equalsIgnoreCase(sAlwaysUOFromWhenStarted__Moved_NotChangedTT)) {
                                                otherGroupName = sTravellers;
                                                otherGroup = groups.get(otherGroupName);
                                                if (otherGroup.contains(aCTBRef)) {
                                                    writeRecordCollection(
                                                            tableValues,
                                                            tHBDPTotals,
                                                            includePreUnderOccupancyValues,
                                                            preUnderOccupancyValues,
                                                            aCTBRef,
                                                            pw2);
                                                } else {
                                                    otherGroupName = sTTNot1Or4AndUnderOccupying;
                                                    otherGroup = groups.get(otherGroupName);
                                                    if (otherGroup.contains(aCTBRef)) {
                                                        writeRecordCollection(
                                                                tableValues,
                                                                tHBDPTotals,
                                                                includePreUnderOccupancyValues,
                                                                preUnderOccupancyValues,
                                                                aCTBRef,
                                                                pw2);
                                                    } else {
                                                        System.out.println(
                                                                "CTBRef " + aCTBRef
                                                                + " is in group " + groupName
                                                                + " and is in one of the not "
                                                                + "expected other groups "
                                                                + "previously written out.");
                                                    }
                                                }
                                            } else {
                                                if (groupName.equalsIgnoreCase(sAlwaysUOFromWhenStarted__NotMoved_NotChangedTT)) {
                                                    otherGroupName = sTravellers;
                                                    otherGroup = groups.get(otherGroupName);
                                                    if (otherGroup.contains(aCTBRef)) {
                                                        writeRecordCollection(
                                                                tableValues,
                                                                tHBDPTotals,
                                                                includePreUnderOccupancyValues,
                                                                preUnderOccupancyValues,
                                                                aCTBRef,
                                                                pw2);
                                                    } else {
                                                        otherGroupName = sTTNot1Or4AndUnderOccupying;
                                                        otherGroup = groups.get(otherGroupName);
                                                        if (otherGroup.contains(aCTBRef)) {
                                                            writeRecordCollection(
                                                                    tableValues,
                                                                    tHBDPTotals,
                                                                    includePreUnderOccupancyValues,
                                                                    preUnderOccupancyValues,
                                                                    aCTBRef,
                                                                    pw2);
                                                        } else {
                                                            System.out.println(
                                                                    "CTBRef " + aCTBRef
                                                                    + " is in group " + groupName
                                                                    + " and is in one of the not "
                                                                    + "expected other groups "
                                                                    + "previously written out.");
                                                        }
                                                    }
                                                } else {
                                                    if (groupName.equalsIgnoreCase(sIntermitantUO__ChangedTT)) {
                                                        otherGroupName = sTravellers;
                                                        otherGroup = groups.get(otherGroupName);
                                                        if (otherGroup.contains(aCTBRef)) {
                                                            writeRecordCollection(
                                                                    tableValues,
                                                                    tHBDPTotals,
                                                                    includePreUnderOccupancyValues,
                                                                    preUnderOccupancyValues,
                                                                    aCTBRef,
                                                                    pw2);
                                                        } else {
                                                            otherGroupName = sTTNot1Or4AndUnderOccupying;
                                                            otherGroup = groups.get(otherGroupName);
                                                            if (otherGroup.contains(aCTBRef)) {
                                                                writeRecordCollection(
                                                                        tableValues,
                                                                        tHBDPTotals,
                                                                        includePreUnderOccupancyValues,
                                                                        preUnderOccupancyValues,
                                                                        aCTBRef,
                                                                        pw2);
                                                            } else {
                                                                otherGroupName = sTT1_To_TT3OrTT6;
                                                                otherGroup = groups.get(otherGroupName);
                                                                if (otherGroup.contains(aCTBRef)) {
                                                                    writeRecordCollection(
                                                                            tableValues,
                                                                            tHBDPTotals,
                                                                            includePreUnderOccupancyValues,
                                                                            preUnderOccupancyValues,
                                                                            aCTBRef,
                                                                            pw2);
                                                                } else {
                                                                    otherGroupName = sTT4_To_TT3OrTT6;
                                                                    otherGroup = groups.get(otherGroupName);
                                                                    if (otherGroup.contains(aCTBRef)) {
                                                                        writeRecordCollection(
                                                                                tableValues,
                                                                                tHBDPTotals,
                                                                                includePreUnderOccupancyValues,
                                                                                preUnderOccupancyValues,
                                                                                aCTBRef,
                                                                                pw2);
                                                                    } else {
                                                                        otherGroupName = sTT3OrTT6_To_TT1;
                                                                        otherGroup = groups.get(otherGroupName);
                                                                        if (otherGroup.contains(aCTBRef)) {
                                                                            writeRecordCollection(
                                                                                    tableValues,
                                                                                    tHBDPTotals,
                                                                                    includePreUnderOccupancyValues,
                                                                                    preUnderOccupancyValues,
                                                                                    aCTBRef,
                                                                                    pw2);
                                                                        } else {
                                                                            otherGroupName = sTT3OrTT6_To_TT4;
                                                                            otherGroup = groups.get(otherGroupName);
                                                                            if (otherGroup.contains(aCTBRef)) {
                                                                                writeRecordCollection(
                                                                                        tableValues,
                                                                                        tHBDPTotals,
                                                                                        includePreUnderOccupancyValues,
                                                                                        preUnderOccupancyValues,
                                                                                        aCTBRef,
                                                                                        pw2);
                                                                            } else {
                                                                                System.out.println(
                                                                                        "CTBRef " + aCTBRef
                                                                                        + " is in group " + groupName
                                                                                        + " and is in one of the not "
                                                                                        + "expected other groups "
                                                                                        + "previously written out.");
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if (groupName.equalsIgnoreCase(sIntermitantUO__Moved_NotChangedTT)) {
                                                            otherGroupName = sTravellers;
                                                            otherGroup = groups.get(otherGroupName);
                                                            if (otherGroup.contains(aCTBRef)) {
                                                                writeRecordCollection(
                                                                        tableValues,
                                                                        tHBDPTotals,
                                                                        includePreUnderOccupancyValues,
                                                                        preUnderOccupancyValues,
                                                                        aCTBRef,
                                                                        pw2);
                                                            } else {
                                                                otherGroupName = sTTNot1Or4AndUnderOccupying;
                                                                otherGroup = groups.get(otherGroupName);
                                                                if (otherGroup.contains(aCTBRef)) {
                                                                    writeRecordCollection(
                                                                            tableValues,
                                                                            tHBDPTotals,
                                                                            includePreUnderOccupancyValues,
                                                                            preUnderOccupancyValues,
                                                                            aCTBRef,
                                                                            pw2);
                                                                } else {
                                                                    System.out.println(
                                                                            "CTBRef " + aCTBRef
                                                                            + " is in group " + groupName
                                                                            + " and is in one of the not "
                                                                            + "expected other groups "
                                                                            + "previously written out.");
                                                                }
                                                            }
                                                        } else {
                                                            System.out.println("CTBRef " + aCTBRef + " already added to"
                                                                    + " another group and in " + groupNameDescription);
                                                            writeRecordCollectionToStdOut(
                                                                    tableValues,
                                                                    includePreUnderOccupancyValues,
                                                                    preUnderOccupancyValues,
                                                                    aCTBRef);
                                                        }
                                                    }
//                                        sAlwaysUOFromStart__NotMoved_NotChangedTT
//                                        sIntermitantUO__NotMoved_NotChangedTT                                                    
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    counter++;
                    writeRecordCollection(
                            tableValues,
                            tHBDPTotals,
                            includePreUnderOccupancyValues,
                            preUnderOccupancyValues,
                            aCTBRef,
                            pw);
                    writeRecordCollection(
                            tableValues,
                            tHBDPTotals,
                            includePreUnderOccupancyValues,
                            preUnderOccupancyValues,
                            aCTBRef,
                            pw2);
                }
            }
            System.out.println(group.size() + ", " + counter + ", " + groupNameDescription);
            pw.close();
            pw2.close();
        }

        // Check size of tCTBRefsCheck
        if (tCTBRefsCheck.size() != tCTBRefs.size()) {
            System.out.println("tCTBRefsCheck.size() != tCTBRefs.size()");
            System.out.println("" + tCTBRefsCheck.size() + " tCTBRefsCheck.size()");
            System.out.println("" + tCTBRefs.size() + " tCTBRefs.size()");
        }

        HashSet<String> remainder;
        remainder = new HashSet<String>();
        remainder.addAll(tCTBRefs);
        remainder.removeAll(tCTBRefsCheck);

        groupName = "remainder";
        name2 = name + sUnderscore + groupName;
        pw = getPrintWriter(name2, paymentType, includeKey, underOccupancy);
        pw.println(header);
        ite = remainder.iterator();
        while (ite.hasNext()) {
            aCTBRef = ite.next();
            writeRecordCollection(
                    tableValues,
                    tHBDPTotals,
                    includePreUnderOccupancyValues,
                    preUnderOccupancyValues,
                    aCTBRef,
                    pw);
        }
        pw.close();
    }

    private void writeRecordCollection(
            TreeMap<String, String> tableValues,
            HashMap<String, Integer> tHBDPTotals,
            boolean includePreUnderOccupancyValues,
            TreeMap<String, String> preUnderOccupancyValues,
            String aCTBRef,
            PrintWriter pw) {
        String key;
        String line;
        // TenancyType
        key = aCTBRef + sUnderscore + sTT;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // UnderOccupancy
        key = aCTBRef + sUnderscore + sU;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // Postcode
        key = aCTBRef + sUnderscore + sP;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // WeeklyHousingBenefitEntitlement
        key = aCTBRef + sUnderscore + sWHBE;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // WeeklyHousingBenefitEntitlement
        key = aCTBRef + sUnderscore + sWERA;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // Arrears
        key = aCTBRef + sUnderscore + sA;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // HB DiscretionaryPayment
        key = aCTBRef + sUnderscore + sHBDP;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace + decimalise(tHBDPTotals.get(aCTBRef));
        pw.println(line);
        // Disability
        key = aCTBRef + sUnderscore + sD;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // Disability Premium
        key = aCTBRef + sUnderscore + sDP;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // Disability Severe
        key = aCTBRef + sUnderscore + sDS;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // Disability Enhanced
        key = aCTBRef + sUnderscore + sDE;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // Child Disability
        key = aCTBRef + sUnderscore + sDC;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // Partner Death
        key = aCTBRef + sUnderscore + sPDeath;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // Household Size
        key = aCTBRef + sUnderscore + sHS;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // NonDependents
        key = aCTBRef + sUnderscore + sND;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // ChildDependents
        key = aCTBRef + sUnderscore + sCD;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // NonDependents (UO)
        key = aCTBRef + sUnderscore + sNDUO;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // Children 16 +
        key = aCTBRef + sUnderscore + sCO16;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // FemaleChildrenUnder10
        key = aCTBRef + sUnderscore + sFCU10;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // MaleChildrenUnder10
        key = aCTBRef + sUnderscore + sMCU10;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // FemaleChildren10to16
        key = aCTBRef + sUnderscore + sFC10To16;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // MaleChildren10to16
        key = aCTBRef + sUnderscore + sMC10To16;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // Number of Bedrooms
        key = aCTBRef + sUnderscore + sNB;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // Bedroom Requirement
        key = aCTBRef + sUnderscore + sBR;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
        // Claimant DoB
        key = aCTBRef + sUnderscore + sDoB;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        line += sCommaSpace;
        pw.println(line);
//        // CTB DiscretionaryPayment
//        key = aCTBRef + sUnderscore + sCTBDP;
//        line = key;
//        line += tableValues.get(key);
//        pw.println(line);
        pw.println();
    }

    private void writeRecordCollectionToStdOut(
            TreeMap<String, String> tableValues,
            boolean includePreUnderOccupancyValues,
            TreeMap<String, String> preUnderOccupancyValues,
            String aCTBRef) {
        String key;
        String line;
        // TenancyType
        key = aCTBRef + sUnderscore + sTT;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        System.out.println(line);
        // UnderOccupancy
        key = aCTBRef + sUnderscore + sU;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        System.out.println(line);
        // Postcode
        key = aCTBRef + sUnderscore + sP;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        System.out.println(line);
        // WeeklyHousingBenefitEntitlement
        key = aCTBRef + sUnderscore + sWHBE;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        System.out.println(line);
        // WeeklyHousingBenefitEntitlement
        key = aCTBRef + sUnderscore + sWERA;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        System.out.println(line);
        // Arrears
        key = aCTBRef + sUnderscore + sA;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        System.out.println(line);
        // HB DiscretionaryPayment
        key = aCTBRef + sUnderscore + sHBDP;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        System.out.println(line);
        // Disability
        key = aCTBRef + sUnderscore + sD;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        System.out.println(line);
        // Disability Premium
        // Disability Severe
        // Disability Enhanced

        // Household Size
        key = aCTBRef + sUnderscore + sHS;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        System.out.println(line);
        // Claimant DoB
        key = aCTBRef + sUnderscore + sDoB;
        line = key;
        if (includePreUnderOccupancyValues) {
            line += preUnderOccupancyValues.get(key);
        }
        line += tableValues.get(key);
        System.out.println(line);
//        // CTB DiscretionaryPayment
//        key = aCTBRef + sUnderscore + sCTBDP;
//        line = key;
//        line += tableValues.get(key);
//        System.out.println(line);
        System.out.println();
    }

    protected static PrintWriter getPrintWriter(
            String name,
            String paymentType,
            String includeKey,
            boolean underOccupancy) {
        PrintWriter result;
        File dirOut;
        dirOut = DW_Files.getTableDir(sUnderOccupancyGroupTables, paymentType, includeKey, underOccupancy);
        String outFilename;
        outFilename = paymentType + "_" + includeKey + "_";
        if (underOccupancy) {
            outFilename += "UO_";
        }
        outFilename += name + ".csv";
        File outFile;
        outFile = new File(dirOut, outFilename);
        result = Generic_StaticIO.getPrintWriter(outFile, false);
        return result;
    }

    public TreeMap<String, String> getGroupNameDescriptions(Set<String> groupNames) {
        TreeMap<String, String> result;
        result = new TreeMap<String, String>();
        String groupName;
        String gn;
        Iterator<String> ite;
        ite = groupNames.iterator();
        while (ite.hasNext()) {
            groupName = ite.next();
            gn = sTravellers;
            if (groupName.equalsIgnoreCase(gn)) {
                result.put(groupName, "Type: " + gn);
            } else {
                gn = sTTNot1Or4AndUnderOccupying;
                if (groupName.equalsIgnoreCase(gn)) {
                    result.put(groupName, "Type: " + gn);
                } else {
                    gn = sTT1_To_TT3OrTT6;
                    if (groupName.equalsIgnoreCase(gn)) {
                        result.put(groupName, "Type: " + gn);
                    } else {
                        gn = sTT4_To_TT3OrTT6;
                        if (groupName.equalsIgnoreCase(gn)) {
                            result.put(groupName, "Type: " + gn);
                        } else {
                            gn = sTT3OrTT6_To_TT1;
                            if (groupName.equalsIgnoreCase(gn)) {
                                result.put(groupName, "Type: " + gn);
                            } else {
                                gn = sTT3OrTT6_To_TT4;
                                if (groupName.equalsIgnoreCase(gn)) {
                                    result.put(groupName, "Type: " + gn);
                                } else {
                                    gn = sAlwaysUOFromStart__NotMoved_NotChangedTT;
                                    if (groupName.equalsIgnoreCase(gn)) {
                                        result.put(groupName, "Type: " + gn);
                                    } else {
                                        gn = sAlwaysUOFromStart__ChangedTT;
                                        if (groupName.equalsIgnoreCase(gn)) {
                                            result.put(groupName, "Type: " + gn);
                                        } else {
                                            gn = sAlwaysUOFromStart__Moved_NotChangedTT;
                                            if (groupName.equalsIgnoreCase(gn)) {
                                                result.put(groupName, "Type: " + gn);
                                            } else {
                                                gn = sAlwaysUOFromWhenStarted__NotMoved_NotChangedTT;
                                                if (groupName.equalsIgnoreCase(gn)) {
                                                    result.put(groupName, "Type: " + gn);
                                                } else {
                                                    gn = sAlwaysUOFromWhenStarted__ChangedTT;
                                                    if (groupName.equalsIgnoreCase(gn)) {
                                                        result.put(groupName, "Type: " + gn);
                                                    } else {
                                                        gn = sAlwaysUOFromWhenStarted__Moved_NotChangedTT;
                                                        if (groupName.equalsIgnoreCase(gn)) {
                                                            result.put(groupName, "Type: " + gn);
                                                        } else {
                                                            gn = sIntermitantUO__NotMoved_NotChangedTT;
                                                            if (groupName.equalsIgnoreCase(gn)) {
                                                                result.put(groupName, "Type: " + gn);
                                                            } else {
                                                                gn = sIntermitantUO__ChangedTT;
                                                                if (groupName.equalsIgnoreCase(gn)) {
                                                                    result.put(groupName, "Type: " + gn);
                                                                } else {
                                                                    gn = sIntermitantUO__Moved_NotChangedTT;
                                                                    if (groupName.equalsIgnoreCase(gn)) {
                                                                        result.put(groupName, "Type: " + gn);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    protected class ID {

        String aNINO;
        String aDOB;

        protected ID(
                String aNINO,
                String aDOB) {
            this.aNINO = aNINO;
            this.aDOB = aDOB;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ID) {
                ID o = (ID) obj;
                if (aNINO.equalsIgnoreCase(o.aNINO)) {
                    if (aDOB.equalsIgnoreCase(o.aDOB)) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 97 * hash + (this.aNINO != null ? this.aNINO.hashCode() : 0);
            hash = 97 * hash + (this.aDOB != null ? this.aDOB.hashCode() : 0);
            return hash;
        }
    }

    protected void addToSets(
            String aCTBRef,
            HashSet<ID> claimantsSet,
            HashSet<ID> partnersSet,
            HashMap<String, Integer> maxNumberOfDependentsInClaimWhenUO,
            HashSet<ID> nonDependentsSet,
            DW_SHBE_Record aDW_SHBE_Record,
            DW_SHBE_D_Record aDW_SHBE_D_Record) {
        String claimantNINO;
        String aNINO;
        String aDOB;
        ID aID;
        claimantNINO = aDW_SHBE_D_Record.getClaimantsNationalInsuranceNumber();
        aDOB = aDW_SHBE_D_Record.getClaimantsDateOfBirth();
        aID = new ID(claimantNINO, aDOB);
        claimantsSet.add(aID);
        if (aDW_SHBE_D_Record.getPartnerFlag() != 0) {
            aNINO = aDW_SHBE_D_Record.getPartnersNationalInsuranceNumber();
            aDOB = aDW_SHBE_D_Record.getPartnersDateOfBirth();
            aID = new ID(aNINO, aDOB);
            partnersSet.add(aID);
        }
        HashSet<DW_SHBE_S_Record> SRecords;
        SRecords = aDW_SHBE_Record.getSRecords();
        DW_SHBE_S_Record SRecord;
        Iterator<DW_SHBE_S_Record> sIte;
        sIte = SRecords.iterator();
        while (sIte.hasNext()) {
            SRecord = sIte.next();
            int subRecordType;
            subRecordType = SRecord.getSubRecordType();
//            int nonDependentStatus;
//            nonDependentStatus = SRecord.getNonDependentStatus();
//            if (nonDependentStatus > 0 && nonDependentStatus < 9) {
            if (subRecordType == 1) {
                Integer m;
                m = maxNumberOfDependentsInClaimWhenUO.get(aCTBRef);
                if (m == null) {
                    m = 0;
                }
                int numberOfChildDependents;
                numberOfChildDependents = aDW_SHBE_D_Record.getNumberOfChildDependents();
                int max;
                max = Math.max(m, numberOfChildDependents);
                maxNumberOfDependentsInClaimWhenUO.put(aCTBRef, max);
            } else {
                aID = getSID(
                        SRecord,
                        subRecordType,
                        claimantNINO,
                        0,
                        nonDependentsSet);
                nonDependentsSet.add(aID);
            }
        }
    }

    protected ID getSID(
            DW_SHBE_S_Record SRecord,
            int subRecordType,
            String claimantNINO,
            int i,
            HashSet<ID> set) {
        ID aID = null;
        String aNINO = SRecord.getSubRecordChildReferenceNumberOrNINO();
//        if (aNINO.isEmpty()) {
//            aNINO = claimantNINO + i;
//        } else {
//            if (aNINO.equalsIgnoreCase(DW_SHBE_Handler.sDefaultNINO)) {
//                aNINO += claimantNINO + i;
//            } else {
//                if (aNINO.equalsIgnoreCase("AA999999A")) {
//                    aNINO += claimantNINO + i;
//                }
//                if (i > 100) {
//                    aNINO += i;
//                }
//            }
//        }
        String aDOB = SRecord.getSubRecordDateOfBirth();
        aID = new ID(aNINO, aDOB);
//        if (set.contains(aID)) {
//            if (subRecordType == 1) {
//            i++;
//            return getSID(SRecord, subRecordType, claimantNINO, i, set);
//            }
//        }
        return aID;
    }
}
