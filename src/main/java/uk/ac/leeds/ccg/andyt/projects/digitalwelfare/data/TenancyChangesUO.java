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
import java.math.BigDecimal;
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
    String sUnderOccupancy = "UnderOccupancy";
    String sTT = "TT";
    String sP = "Postcode";
    String sWHBE = "WeeklyHousingBenefitEntitlement";
    String sWERA = "WeeklyEligibleRentAmount";
    String sPSI = "PassportedStandardIndicator";
    String sSHBC = "StatusOfHBClaim";
    String sRTHBCC = "ReasonThatHBClaimClosed";
    String sCEG = "ClaimantEthnicGroup";
    String sHS = "HouseholdSize";
    String sND = "NonDependents";
    String sCD = "ChildDependents";
    String sCDoB = "ClaimantsDoB";
    String sCA = "ClaimantsAge";
    String sPDoB = "PartnersDoB";
    String sPA = "PartnersAge";
    String sCG = "ClaimantsGender";
    String sPG = "PartnersGender";
    String sD = "D";
    String sDisability = "Disability";
    String sDP = "DP";
    String sDisabilityPremium = "DisabilityPremium";
    String sDS = "DS";
    String sDisabilitySevere = "DisabilitySevere";
    String sDE = "DE";
    String sDisabilityEnhanced = "DisabilityEnhanced";
    String sDC = "DC";
    String sDisabledChild = "DisabledChild";
    String sPDeath = "PartnerDeath";
    String sHBDP = "HBDiscretionaryPayment";
    //String sCTBDP = "CTBDiscretionaryPayment";
    String sA = "Arrears";
    String s = "";
    String s0 = "0"; // zero
    String s0Dot0 = "0.0"; // zeros
    String s0Dot00 = "0.00"; // zeros

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

    String sUO_To_LeftSHBE = "UO_To_LeftSHBE";
    String sUOTT1_To_LeftSHBE = "UOTT1_To_LeftSHBE";
    String sUOTT1_To_LeftSHBEPermanently = "UOTT1_To_LeftSHBEPermanently";
    String sUOTT1_To_LeftSHBEReturnedAsTT1orTT4 = "UOTT1_To_LeftSHBEReturnedAsTT1orTT4";
    String sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6 = "UOTT1_To_LeftSHBEReturnedAsTT3OrTT6";
    String sUOTT4_To_LeftSHBE = "UOTT4_To_LeftSHBE";
    String sUOTT4_To_LeftSHBEPermanently = "UOTT4_To_LeftSHBEPermanently";
    String sUOTT4_To_LeftSHBEReturnedAsTT1orTT4 = "UOTT4_To_LeftSHBEReturnedAsTT1orTT4";
    String sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6 = "UOTT4_To_LeftSHBEReturnedAsTT3OrTT6";
    String sUOTT1_To_TT3OrTT6 = "UOTT1_To_TT3OrTT6";
    String sUOTT4_To_TT3OrTT6 = "UOTT4_To_TT3OrTT6";
    String sTT3OrTT6_To_UOTT1 = "TT3OrTT6_To_UOTT1";
    String sTT3OrTT6_To_UOTT4 = "TT3OrTT6_To_UOTT4";
    String sTT1_To_UOTT1_PostcodeUnchanged = "TT1_To_UOTT1_PostcodeUnchanged";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months";
    String sTT4_To_UOTT4_PostcodeUnchanged = "TT4_To_UOTT4_PostcodeUnchanged";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months";
    String sUOTT1_To_TT1_PostcodeUnchanged = "UOTT1_To_TT1_PostcodeUnchanged";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months";
    String sUOTT4_To_TT4_PostcodeUnchanged = "UOTT4_To_TT4_PostcodeUnchanged";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months";
    String sUOTT1_To_TT1_PostcodeChanged = "UOTT1_To_TT1_PostcodeChanged";
    String sUOTT4_To_TT4_PostcodeChanged = "UOTT4_To_TT4_PostcodeChanged";
    String sTT1_To_UOTT1_PostcodeChanged = "TT1_To_UOTT1_PostcodeChanged";
    String sTT4_To_UOTT4_PostcodeChanged = "TT4_To_UOTT4_PostcodeChanged";

    // For Aggregate Statistics
    String sTotal_DHP = "Total_DHP";
    String sTotalCount_DHP = "TotalCount_DHP";
    String sTotal_HBLossDueToUO = "Total_HBLossDueToUO";
    String sTotalCount_HBLossDueToUO = "TotalCount_HBLossDueToUO";
    String sMax_Arrears = "Max_Arrears";
    String sTotalCount_Arrears = "TotalCount_Arrears";
    String sTotalCount_UnderOccupancy = "TotalCount_UnderOccupancy";
    // For General Statistics
    String sTotalCount_AlwaysUOFromWhenStarted = "TotalCount_AlwaysUOFromWhenStarted";
    String sTotalCount_AlwaysUOFromStart = "TotalCount_AlwaysUOFromStart";
//    String sTotalCount_ExistingSHBEClaimsThatBecameUOInApril2013 = "TotalCount_ExistingSHBEClaimsThatBecameUOInApril2013";
//    String sTotalCount_ExistingSHBEClaimsThatBecameUOAfterApril2013 = "TotalCount_ExistingSHBEClaimsThatBecameUOAfterApril2013";
//    String sTotalCount_ExistingSHBEClaimsThatBecameUOAfterChangePostcodeAndOrTT = "TotalCount_ExistingSHBEClaimsThatBecameUOAfterChangePostcodeAndOrTT";
//    String sTotalCount_ChangePostcodeAndOrTTToAvoidUO = "TotalCount_ChangePostcodeAndOrTToAvoidUO";
//    String sTotalCount_StayPutAndAvoidedUO = "TotalCount_StayPutAndAvoidedUO";
    String sTotalCount_ClaimsEffectedByUnderOccupancy = "TotalCount_ClaimsEffectedByUnderOccupancy";
    String sTotalCount_UniqueIndividualsEffectedByUnderOccupancy = "TotalCount_UniqueIndividualsEffectedByUnderOccupancy";
    // Council
    String sTotalCount_CouncilClaimsEffectedByUnderOccupancy = "TotalCount_CouncilClaimsEffectedByUnderOccupancy";
    String sTotalCount_CouncilUniqueIndividualsEffectedByUnderOccupancy = "TotalCount_CouncilUniqueIndividualsEffectedByUnderOccupancy";
    String sTotalCount_CouncilUniqueClaimantsEffectedByUnderOccupancy = "TotalCount_CouncilUniqueClaimantsEffectedByUnderOccupancy";
    String sTotalCount_CouncilUniquePartnersEffectedByUnderOccupancy = "TotalCount_CouncilUniquePartnersEffectedByUnderOccupancy";
    String sTotalCount_CouncilDependentsEffectedByUnderOccupancy = "TotalCount_CouncilDependentsEffectedByUnderOccupancy";
    String sTotalCount_CouncilUniqueNonDependentsEffectedByUnderOccupancy = "TotalCount_CouncilUniqueNonDependentsEffectedByUnderOccupancy";
    // RSL
    String sTotalCount_RSLClaimsEffectedByUnderOccupancy = "TotalCount_RSLClaimsEffectedByUnderOccupancy";
    String sTotalCount_RSLUniqueIndividualsEffectedByUnderOccupancy = "TotalCount_RSLUniqueIndividualsEffectedByUnderOccupancy";
    String sTotalCount_RSLUniqueClaimantsEffectedByUnderOccupancy = "TotalCount_RSLUniqueClaimantsEffectedByUnderOccupancy";
    String sTotalCount_RSLUniquePartnersEffectedByUnderOccupancy = "TotalCount_RSLUniquePartnersEffectedByUnderOccupancy";
    String sTotalCount_RSLDependentsEffectedByUnderOccupancy = "TotalCount_RSLDependentsEffectedByUnderOccupancy";
    String sTotalCount_RSLUniqueNonDependentsEffectedByUnderOccupancy = "TotalCount_RSLUniqueNonDependentsEffectedByUnderOccupancy";

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
            result.put(aCTBRef + sUnderscore + sUnderOccupancy, s);
            result.put(aCTBRef + sUnderscore + sP, s);
            result.put(aCTBRef + sUnderscore + sWHBE, s);
            result.put(aCTBRef + sUnderscore + sWERA, s);
            result.put(aCTBRef + sUnderscore + sPSI, s);
            result.put(aCTBRef + sUnderscore + sSHBC, s);
            result.put(aCTBRef + sUnderscore + sRTHBCC, s);
            result.put(aCTBRef + sUnderscore + sCEG, s);
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
            result.put(aCTBRef + sUnderscore + sCDoB, s);
            result.put(aCTBRef + sUnderscore + sCA, s);
            result.put(aCTBRef + sUnderscore + sPDoB, s);
            result.put(aCTBRef + sUnderscore + sPA, s);
            result.put(aCTBRef + sUnderscore + sCG, s);
            result.put(aCTBRef + sUnderscore + sPG, s);
            result.put(aCTBRef + sUnderscore + sDisability, s);
            result.put(aCTBRef + sUnderscore + sDisabilityPremium, s);
            result.put(aCTBRef + sUnderscore + sDisabilitySevere, s);
            result.put(aCTBRef + sUnderscore + sDisabilityEnhanced, s);
            result.put(aCTBRef + sUnderscore + sDisabledChild, s);
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
        String year;
        String month;
        DW_SHBE_Record record;
        DW_SHBE_D_Record dRecord;
        tNotMonthlyUOIte = NotMonthlyUO.iterator();
        while (tNotMonthlyUOIte.hasNext()) {
            i = tNotMonthlyUOIte.next();
            tSHBEData = new DW_SHBE_Collection(SHBEFilenames[i], paymentType);
            year = DW_SHBE_Handler.getYear(SHBEFilenames[i]);
            month = DW_SHBE_Handler.getMonthNumber(SHBEFilenames[i]);
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
                    aS += sCommaSpace + sTT_ + j;
                    result.put(key, aS);
                    // Under Occupancy
                    key = aCTBRef + sUnderscore + sUnderOccupancy;
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
                    // PassportedStandardIndicator
                    key = aCTBRef + sUnderscore + sPSI;
                    aS = result.get(key);
                    j = dRecord.getPassportedStandardIndicator();
                    aS += sCommaSpace + j;
                    result.put(key, aS);
                    // StatusOfHBClaim
                    key = aCTBRef + sUnderscore + sSHBC;
                    aS = result.get(key);
                    j = dRecord.getStatusOfHBClaimAtExtractDate();
                    aS += sCommaSpace + j;
                    result.put(key, aS);
                    // ReasonThatHBClaimClosed
                    key = aCTBRef + sUnderscore + sRTHBCC;
                    aS = result.get(key);
                    j = dRecord.getReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective();
                    if (j == 0) {
                        aS += sCommaSpace;
                    } else {
                        aS += sCommaSpace + j;
                    }
                    result.put(key, aS);
                    // ClaimantEthnicGroup
                    key = aCTBRef + sUnderscore + sCEG;
                    aS = result.get(key);
                    j = dRecord.getClaimantsEthnicGroup();
                    aS += sCommaSpace + j;
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
                    if (j == 0) {
                        aS += sCommaSpace;
                    } else {
                        aS += sCommaSpace + j;
                    }
                    result.put(key, aS);
                    // Child Dependents
                    key = aCTBRef + sUnderscore + sCD;
                    aS = result.get(key);
                    j = dRecord.getNumberOfChildDependents();
                    if (j == 0) {
                        aS += sCommaSpace;
                    } else {
                        aS += sCommaSpace + j;
                    }
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
                    // Claimants Date Of Birth
                    key = aCTBRef + sUnderscore + sCDoB;
                    aS = result.get(key);
                    bS = dRecord.getClaimantsDateOfBirth();
                    aS += sCommaSpace + bS;
                    result.put(key, aS);
                    // ClaimantsAge
                    key = aCTBRef + sUnderscore + sCA;
                    aS = result.get(key);
                    bS = DW_SHBE_Handler.getClaimantsAge(year, month, dRecord);
                    aS += sCommaSpace + bS;
                    result.put(key, aS);
                    // Partners Date Of Birth
                    key = aCTBRef + sUnderscore + sPDoB;
                    aS = result.get(key);
                    bS = dRecord.getPartnersDateOfBirth();
                    aS += sCommaSpace + bS;
                    result.put(key, aS);
                    // PartnersAge
                    key = aCTBRef + sUnderscore + sPA;
                    aS = result.get(key);
                    bS = DW_SHBE_Handler.getPartnersAge(year, month, dRecord);
                    aS += sCommaSpace + bS;
                    result.put(key, aS);
                    // ClaimantsGender
                    key = aCTBRef + sUnderscore + sCG;
                    aS = result.get(key);
                    bS = dRecord.getClaimantsGender();
                    aS += sCommaSpace + bS;
                    result.put(key, aS);
                    // PartnersGender
                    key = aCTBRef + sUnderscore + sPG;
                    aS = result.get(key);
                    bS = dRecord.getPartnersGender();
                    aS += sCommaSpace + bS;
                    result.put(key, aS);
                    // Disability
                    key = aCTBRef + sUnderscore + sDisability;
                    aS = result.get(key);
                    b = DW_SHBE_Handler.getDisability(dRecord);
                    if (b == true) {
                        aS += sCommaSpace + sDisability;
                    } else {
                        aS += sCommaSpace;
                    }
                    result.put(key, aS);
                    // Disability Premium
                    key = aCTBRef + sUnderscore + sDisabilityPremium;
                    aS = result.get(key);
                    j = dRecord.getDisabilityPremiumAwarded();
                    if (j == 1) {
                        aS += sCommaSpace + sDP;
                    } else {
                        aS += sCommaSpace;
                    }
                    result.put(key, aS);
                    // Disability Severe
                    key = aCTBRef + sUnderscore + sDisabilitySevere;
                    aS = result.get(key);
                    j = dRecord.getSevereDisabilityPremiumAwarded();
                    if (j == 1) {
                        aS += sCommaSpace + sDS;
                    } else {
                        aS += sCommaSpace;
                    }
                    result.put(key, aS);
                    // Disability Enhanced
                    key = aCTBRef + sUnderscore + sDisabilityEnhanced;
                    aS = result.get(key);
                    j = dRecord.getEnhancedDisabilityPremiumAwarded();
                    if (j == 1) {
                        aS += sCommaSpace + sDE;
                    } else {
                        aS += sCommaSpace;
                    }
                    result.put(key, aS);
                    // Child Disability
                    key = aCTBRef + sUnderscore + sDisabledChild;
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
                    if (bS == null) {
                        aS += sCommaSpace;
                    } else {
                        if (bS.isEmpty()) {
                            aS += sCommaSpace;
                        } else {
                            aS += sCommaSpace + sPDeath + sUnderscore + bS;
                        }
                    }
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
                    key = aCTBRef + sUnderscore + sUnderOccupancy;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
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
                    // PassportedStandardIndicator
                    key = aCTBRef + sUnderscore + sPSI;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // StatusOfHBClaim
                    key = aCTBRef + sUnderscore + sSHBC;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // ReasonThatHBClaimClosed
                    key = aCTBRef + sUnderscore + sRTHBCC;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // ClaimantEthnicGroup
                    key = aCTBRef + sUnderscore + sCEG;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Household Size
                    key = aCTBRef + sUnderscore + sHS;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
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
                    // Claimants Date Of Birth
                    key = aCTBRef + sUnderscore + sCDoB;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // ClaimantsAge
                    key = aCTBRef + sUnderscore + sCA;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Partners Date Of Birth
                    key = aCTBRef + sUnderscore + sPDoB;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Partners Age
                    key = aCTBRef + sUnderscore + sPA;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // ClaimantsGender
                    key = aCTBRef + sUnderscore + sCG;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Partners Gender
                    key = aCTBRef + sUnderscore + sPG;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Disability
                    key = aCTBRef + sUnderscore + sDisability;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Disability Premium
                    key = aCTBRef + sUnderscore + sDisabilityPremium;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Disability Severe
                    key = aCTBRef + sUnderscore + sDisabilitySevere;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Disability Enhanced
                    key = aCTBRef + sUnderscore + sDisabilityEnhanced;
                    aS = result.get(key);
                    aS += sCommaSpace;
                    result.put(key, aS);
                    // Child Disability
                    key = aCTBRef + sUnderscore + sDisabledChild;
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
        String t;
        t = s.trim();
        if (t.equalsIgnoreCase(s0)) {
            return s0;
        }
        int l;
        l = t.length();
        if (l == 1) {
            return s0Dot0 + t;
        }
        if (l == 2) {
            return "0." + t;
        }
        if (l == 3) {
            return t.substring(0, l - 2) + "." + t.substring(l - 2, l);
        }
        //if (l > 2) {
        return t.substring(0, l - 2) + "." + t.substring(l - 2, l);
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
        result = new Object[7];
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

        TreeSet<String> tCTBRefSetUO_To_LeftSHBE;
        tCTBRefSetUO_To_LeftSHBE = new TreeSet<String>();
        groups.put(sUO_To_LeftSHBE, tCTBRefSetUO_To_LeftSHBE);

        TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE;
        tCTBRefSetUOTT1_To_LeftSHBE = new TreeSet<String>();
        groups.put(sUOTT1_To_LeftSHBE, tCTBRefSetUOTT1_To_LeftSHBE);

        TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBEPermanently;
        tCTBRefSetUOTT1_To_LeftSHBEPermanently = new TreeSet<String>();
        groups.put(sUOTT1_To_LeftSHBEPermanently, tCTBRefSetUOTT1_To_LeftSHBEPermanently);

        TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT1orTT4;
        tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT1orTT4 = new TreeSet<String>();
        groups.put(sUOTT1_To_LeftSHBEReturnedAsTT1orTT4, tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT1orTT4);

        TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT3OrTT6;
        tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT3OrTT6 = new TreeSet<String>();
        groups.put(sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6, tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT3OrTT6);

        TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE;
        tCTBRefSetUOTT4_To_LeftSHBE = new TreeSet<String>();
        groups.put(sUOTT4_To_LeftSHBE, tCTBRefSetUOTT4_To_LeftSHBE);

        TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBEPermanently;
        tCTBRefSetUOTT4_To_LeftSHBEPermanently = new TreeSet<String>();
        groups.put(sUOTT4_To_LeftSHBEPermanently, tCTBRefSetUOTT4_To_LeftSHBEPermanently);

        TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT1orTT4;
        tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT1orTT4 = new TreeSet<String>();
        groups.put(sUOTT4_To_LeftSHBEReturnedAsTT1orTT4, tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT1orTT4);

        TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT3OrTT6;
        tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT3OrTT6 = new TreeSet<String>();
        groups.put(sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6, tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT3OrTT6);

        TreeSet<String> tCTBRefSetUOTT1_To_TT3OrTT6;
        tCTBRefSetUOTT1_To_TT3OrTT6 = new TreeSet<String>();
        groups.put(sUOTT1_To_TT3OrTT6, tCTBRefSetUOTT1_To_TT3OrTT6);

        TreeSet<String> tCTBRefSetUOTT4_To_TT3OrTT6;
        tCTBRefSetUOTT4_To_TT3OrTT6 = new TreeSet<String>();
        groups.put(sUOTT4_To_TT3OrTT6, tCTBRefSetUOTT4_To_TT3OrTT6);

        TreeSet<String> tCTBRefSetTT3OrTT6_To_UOTT1;
        tCTBRefSetTT3OrTT6_To_UOTT1 = new TreeSet<String>();
        groups.put(sTT3OrTT6_To_UOTT1, tCTBRefSetTT3OrTT6_To_UOTT1);

        TreeSet<String> tCTBRefSetTT3OrTT6_To_UOTT4;
        tCTBRefSetTT3OrTT6_To_UOTT4 = new TreeSet<String>();
        groups.put(sTT3OrTT6_To_UOTT4, tCTBRefSetTT3OrTT6_To_UOTT4);

        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged = new TreeSet<String>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchanged, tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged);
        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month = new TreeSet<String>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month, tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month);
        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months = new TreeSet<String>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months, tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months);

        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged = new TreeSet<String>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchanged, tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged);
        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month = new TreeSet<String>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month, tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month);
        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months = new TreeSet<String>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months, tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months);

        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged = new TreeSet<String>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchanged, tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged);
        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month = new TreeSet<String>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month, tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month);
        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months = new TreeSet<String>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months, tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months);

        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged = new TreeSet<String>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchanged, tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged);
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month = new TreeSet<String>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month, tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month);
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months = new TreeSet<String>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months, tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months);

        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious = new TreeSet<String>();
        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious = new TreeSet<String>();
        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious = new TreeSet<String>();
        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious = new TreeSet<String>();
        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious = new TreeSet<String>();
        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious = new TreeSet<String>();
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious = new TreeSet<String>();
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious = new TreeSet<String>();

        TreeSet<String> tCTBRefSetUOTT1_ToTT1_PostcodeChanged;
        tCTBRefSetUOTT1_ToTT1_PostcodeChanged = new TreeSet<String>();
        groups.put(sUOTT1_To_TT1_PostcodeChanged, tCTBRefSetUOTT1_ToTT1_PostcodeChanged);

        TreeSet<String> tCTBRefSetUOTT4_ToTT4_PostcodeChanged;
        tCTBRefSetUOTT4_ToTT4_PostcodeChanged = new TreeSet<String>();
        groups.put(sUOTT4_To_TT4_PostcodeChanged, tCTBRefSetUOTT4_ToTT4_PostcodeChanged);

        TreeSet<String> tCTBRefSetTT1_ToUOTT1_PostcodeChanged;
        tCTBRefSetTT1_ToUOTT1_PostcodeChanged = new TreeSet<String>();
        groups.put(sTT1_To_UOTT1_PostcodeChanged, tCTBRefSetTT1_ToUOTT1_PostcodeChanged);

        TreeSet<String> tCTBRefSetTT4_ToUOTT4_PostcodeChanged;
        tCTBRefSetTT4_ToUOTT4_PostcodeChanged = new TreeSet<String>();
        groups.put(sTT4_To_UOTT4_PostcodeChanged, tCTBRefSetTT4_ToUOTT4_PostcodeChanged);

        String aCTBRef;
        Iterator<String> tCTBRefsIte;

        // Initialise aggregateStatistics and generalStatistics
        TreeMap<String, BigDecimal> aggregateStatistics;
        aggregateStatistics = new TreeMap<String, BigDecimal>();
        tCTBRefsIte = tCTBRefs.iterator();
        while (tCTBRefsIte.hasNext()) {
            aCTBRef = tCTBRefsIte.next();
            aggregateStatistics.put(aCTBRef + sUnderscore + sTotal_DHP, BigDecimal.ZERO);
            aggregateStatistics.put(aCTBRef + sUnderscore + sTotalCount_DHP, BigDecimal.ZERO);
            aggregateStatistics.put(aCTBRef + sUnderscore + sTotal_HBLossDueToUO, BigDecimal.ZERO);
            aggregateStatistics.put(aCTBRef + sUnderscore + sTotalCount_HBLossDueToUO, BigDecimal.ZERO);
            aggregateStatistics.put(aCTBRef + sUnderscore + sMax_Arrears, BigDecimal.ZERO);
            aggregateStatistics.put(aCTBRef + sUnderscore + sTotalCount_Arrears, BigDecimal.ZERO);
            aggregateStatistics.put(aCTBRef + sUnderscore + sTotalCount_UnderOccupancy, BigDecimal.ZERO);
        }
        TreeMap<String, BigDecimal> generalStatistics;
        generalStatistics = new TreeMap<String, BigDecimal>();
        // Use sets?
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOInApril2013, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOAfterApril2013, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOAfterChangePostcodeAndOrTT, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ChangePostcodeAndOrTTToAvoidUO, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_StayPutAndAvoidedUO, BigDecimal.ZERO);
//        generalStatistics.put(sCostOfUOToTaxPayer, BigDecimal.ZERO);

        HashMap<String, Integer> DHP_Totals;
        DHP_Totals = new HashMap<String, Integer>();
        // Initialise tableValues (part 2) and DHP_Totals
        tCTBRefsIte = tCTBRefs.iterator();
        while (tCTBRefsIte.hasNext()) {
            aCTBRef = tCTBRefsIte.next();
            tableValues.put(aCTBRef + sUnderscore + sTT, s);
            tableValues.put(aCTBRef + sUnderscore + sUnderOccupancy, s);
            tableValues.put(aCTBRef + sUnderscore + sP, s);
            tableValues.put(aCTBRef + sUnderscore + sWHBE, s);
            tableValues.put(aCTBRef + sUnderscore + sWERA, s);
            tableValues.put(aCTBRef + sUnderscore + sPSI, s);
            tableValues.put(aCTBRef + sUnderscore + sSHBC, s);
            tableValues.put(aCTBRef + sUnderscore + sRTHBCC, s);
            tableValues.put(aCTBRef + sUnderscore + sCEG, s);
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
            tableValues.put(aCTBRef + sUnderscore + sCDoB, s);
            tableValues.put(aCTBRef + sUnderscore + sCA, s);
            tableValues.put(aCTBRef + sUnderscore + sPDoB, s);
            tableValues.put(aCTBRef + sUnderscore + sPA, s);
            tableValues.put(aCTBRef + sUnderscore + sCG, s);
            tableValues.put(aCTBRef + sUnderscore + sPG, s);
            tableValues.put(aCTBRef + sUnderscore + sDisability, s);
            tableValues.put(aCTBRef + sUnderscore + sDisabilityPremium, s);
            tableValues.put(aCTBRef + sUnderscore + sDisabilitySevere, s);
            tableValues.put(aCTBRef + sUnderscore + sDisabilityEnhanced, s);
            tableValues.put(aCTBRef + sUnderscore + sDisabledChild, s);
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
        String year = s;
        String month = s;
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
            year = DW_SHBE_Handler.getYear(aSHBEFilename);
            month = DW_SHBE_Handler.getMonthNumber(aSHBEFilename);
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

        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth = new TreeSet<String>();
        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth = new TreeSet<String>();
        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth = new TreeSet<String>();
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth = new TreeSet<String>();
        
        // Add TT of all CTBRefs to result
        tCTBRefsIte = tCTBRefs.iterator();
        while (tCTBRefsIte.hasNext()) {
            aCTBRef = tCTBRefsIte.next();
            aDW_SHBE_Record = aRecords.get(aCTBRef);
            process(
                    aggregateStatistics,
                    generalStatistics,
                    aCTBRef,
                    year,
                    month,
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
                    tCTBRefSetMoved,
                    tCTBRefSetChangedTT,
                    tCTBRefSetAlwaysUOFromStart,
                    tCTBRefSetAlwaysUOFromWhenStarted,
                    tCTBRefSetIntermitantUO,
                    tCTBRefSetUO_To_LeftSHBE,
                    tCTBRefSetUOTT1_To_LeftSHBE,
                    tCTBRefSetUOTT1_To_LeftSHBEPermanently,
                    tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT1orTT4,
                    tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
                    tCTBRefSetUOTT4_To_LeftSHBE,
                    tCTBRefSetUOTT4_To_LeftSHBEPermanently,
                    tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT1orTT4,
                    tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
                    tCTBRefSetUOTT1_To_TT3OrTT6,
                    tCTBRefSetUOTT4_To_TT3OrTT6,
                    tCTBRefSetTT3OrTT6_To_UOTT1,
                    tCTBRefSetTT3OrTT6_To_UOTT4,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious,
                    tCTBRefSetUOTT1_ToTT1_PostcodeChanged,
                    tCTBRefSetUOTT4_ToTT4_PostcodeChanged,
                    tCTBRefSetTT1_ToUOTT1_PostcodeChanged,
                    tCTBRefSetTT4_ToUOTT4_PostcodeChanged);
        }

        tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious = tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious = tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth = new TreeSet<String>();
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth = new TreeSet<String>();
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious = tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious = tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth = new TreeSet<String>();
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth = new TreeSet<String>();

        // Iterate over the rest of the data
        while (includeIte.hasNext()) {
            i = includeIte.next();
            bRecords = aRecords;
            aSHBEFilename = SHBEFilenames[i];
            aYM3 = DW_SHBE_Handler.getYM3(aSHBEFilename);
            year = DW_SHBE_Handler.getYear(aSHBEFilename);
            month = DW_SHBE_Handler.getMonthNumber(aSHBEFilename);
            aSHBEData = new DW_SHBE_Collection(aSHBEFilename, paymentType);
            aRecords = aSHBEData.getRecords();
            councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(aYM3);
            RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(aYM3);
            header += sCommaSpace + aYM3;
            tCTBRefsIte = tCTBRefs.iterator();
            while (tCTBRefsIte.hasNext()) {
                aCTBRef = tCTBRefsIte.next();
                aDW_SHBE_Record = aRecords.get(aCTBRef);
                process(
                        aggregateStatistics,
                        generalStatistics,
                        aCTBRef,
                        year,
                        month,
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
                        tCTBRefSetMoved,
                        tCTBRefSetChangedTT,
                        tCTBRefSetAlwaysUOFromStart,
                        tCTBRefSetAlwaysUOFromWhenStarted,
                        tCTBRefSetIntermitantUO,
                        tCTBRefSetUO_To_LeftSHBE,
                        tCTBRefSetUOTT1_To_LeftSHBE,
                        tCTBRefSetUOTT1_To_LeftSHBEPermanently,
                        tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT1orTT4,
                        tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
                        tCTBRefSetUOTT4_To_LeftSHBE,
                        tCTBRefSetUOTT4_To_LeftSHBEPermanently,
                        tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT1orTT4,
                        tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
                        tCTBRefSetUOTT1_To_TT3OrTT6,
                        tCTBRefSetUOTT4_To_TT3OrTT6,
                        tCTBRefSetTT3OrTT6_To_UOTT1,
                        tCTBRefSetTT3OrTT6_To_UOTT4,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious,
                        tCTBRefSetUOTT1_ToTT1_PostcodeChanged,
                        tCTBRefSetUOTT4_ToTT4_PostcodeChanged,
                        tCTBRefSetTT1_ToUOTT1_PostcodeChanged,
                        tCTBRefSetTT4_ToUOTT4_PostcodeChanged);
            }
            tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious = tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious;
            tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious = tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious;
            tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious = tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth;
            tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious = tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth;
            tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth = new TreeSet<String>();
            tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth = new TreeSet<String>();
            tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious = tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious;
            tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious = tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious;
            tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious = tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth;
            tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious = tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth;
            tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth = new TreeSet<String>();
            tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth = new TreeSet<String>();
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
            key = aCTBRef + sUnderscore + sUnderOccupancy;
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
                generalStatistics,
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

        generalStatistics.put(sTotalCount_AlwaysUOFromStart,
                BigDecimal.valueOf(tCTBRefSetAlwaysUOFromStart.size()));
        generalStatistics.put(sTotalCount_AlwaysUOFromStart,
                BigDecimal.valueOf(tCTBRefSetAlwaysUOFromStart.size()));
        generalStatistics.put(sTotalCount_AlwaysUOFromWhenStarted,
                BigDecimal.valueOf(tCTBRefSetAlwaysUOFromWhenStarted.size()));

// Use sets?
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOInApril2013, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOAfterApril2013, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOAfterChangePostcodeAndOrTT, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ChangePostcodeAndOrTTToAvoidUO, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_StayPutAndAvoidedUO, BigDecimal.ZERO);
//        generalStatistics.put(sCostOfUOToTaxPayer, BigDecimal.ZERO);
        result[0] = header;
        result[1] = tableValues;
        result[2] = tCTBRefs;
        result[3] = groups;
        result[4] = preUnderOccupancyValues;
        result[5] = aggregateStatistics;
        result[6] = generalStatistics;
        return result;
    }

    protected void checkSets(
            TreeMap<String, BigDecimal> generalStatistics,
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
        generalStatistics.put(
                sUO_To_LeftSHBE,
                BigDecimal.valueOf(groups.get(sUO_To_LeftSHBE).size()));
        generalStatistics.put(
                sUOTT1_To_LeftSHBE,
                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBE).size()));
        generalStatistics.put(
                sUOTT1_To_LeftSHBEPermanently,
                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBEPermanently).size()));
        generalStatistics.put(
                sUOTT1_To_LeftSHBEReturnedAsTT1orTT4,
                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBEReturnedAsTT1orTT4).size()));
        generalStatistics.put(
                sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6).size()));
        generalStatistics.put(
                sUOTT4_To_LeftSHBE,
                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBE).size()));
        generalStatistics.put(
                sUOTT4_To_LeftSHBEPermanently,
                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBEPermanently).size()));
        generalStatistics.put(
                sUOTT4_To_LeftSHBEReturnedAsTT1orTT4,
                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBEReturnedAsTT1orTT4).size()));
        generalStatistics.put(
                sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6).size()));
        generalStatistics.put(
                sUOTT1_To_TT3OrTT6,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT3OrTT6).size()));
        generalStatistics.put(
                sUOTT4_To_TT3OrTT6,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT3OrTT6).size()));
        generalStatistics.put(
                sTT3OrTT6_To_UOTT1,
                BigDecimal.valueOf(groups.get(sTT3OrTT6_To_UOTT1).size()));
        generalStatistics.put(
                sTT3OrTT6_To_UOTT4,
                BigDecimal.valueOf(groups.get(sTT3OrTT6_To_UOTT4).size()));

        generalStatistics.put(sTT1_To_UOTT1_PostcodeUnchanged,
                BigDecimal.valueOf(groups.get(sTT1_To_UOTT1_PostcodeUnchanged).size()));
        generalStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
                BigDecimal.valueOf(groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month).size()));
        generalStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
                BigDecimal.valueOf(groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months).size()));

        generalStatistics.put(sTT4_To_UOTT4_PostcodeUnchanged,
                BigDecimal.valueOf(groups.get(sTT4_To_UOTT4_PostcodeUnchanged).size()));
        generalStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
                BigDecimal.valueOf(groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month).size()));
        generalStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
                BigDecimal.valueOf(groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months).size()));

        generalStatistics.put(sUOTT1_To_TT1_PostcodeUnchanged,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeUnchanged).size()));
        generalStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month).size()));
        generalStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months).size()));

        generalStatistics.put(sUOTT4_To_TT4_PostcodeUnchanged,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeUnchanged).size()));
        generalStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month).size()));
        generalStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months).size()));

        generalStatistics.put(sUOTT1_To_TT1_PostcodeChanged,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeChanged).size()));
        generalStatistics.put(sUOTT4_To_TT4_PostcodeChanged,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeChanged).size()));
        generalStatistics.put(sTT1_To_UOTT1_PostcodeChanged,
                BigDecimal.valueOf(groups.get(sTT1_To_UOTT1_PostcodeChanged).size()));
        generalStatistics.put(sTT4_To_UOTT4_PostcodeChanged,
                BigDecimal.valueOf(groups.get(sTT4_To_UOTT4_PostcodeChanged).size()));
        generalStatistics.put(
                sTotalCount_ClaimsEffectedByUnderOccupancy,
                BigDecimal.valueOf(tCTBRefs.size()));
        generalStatistics.put(
                sTotalCount_CouncilClaimsEffectedByUnderOccupancy,
                BigDecimal.valueOf(tCouncilCTBRefs.size()));
        generalStatistics.put(
                sTotalCount_RSLClaimsEffectedByUnderOccupancy,
                BigDecimal.valueOf(tRSLCTBRefs.size()));
        generalStatistics.put(
                sTotalCount_UniqueIndividualsEffectedByUnderOccupancy,
                BigDecimal.valueOf((tIDSetUniqueIndividualsEffected.size()
                        + totalCouncilDependentsEffected + totalRSLDependentsEffected)));
        // Council
        generalStatistics.put(
                sTotalCount_CouncilUniqueIndividualsEffectedByUnderOccupancy,
                BigDecimal.valueOf(tIDSetCouncilUniqueIndividualsEffected.size()
                        + totalCouncilDependentsEffected));
        generalStatistics.put(
                sTotalCount_CouncilUniqueClaimantsEffectedByUnderOccupancy,
                BigDecimal.valueOf(tIDSetCouncilUniqueClaimantsEffected.size()));
        generalStatistics.put(
                sTotalCount_CouncilUniquePartnersEffectedByUnderOccupancy,
                BigDecimal.valueOf(tIDSetCouncilUniquePartnersEffected.size()));
        generalStatistics.put(
                sTotalCount_CouncilDependentsEffectedByUnderOccupancy,
                BigDecimal.valueOf(totalCouncilDependentsEffected));
        generalStatistics.put(
                sTotalCount_CouncilUniqueNonDependentsEffectedByUnderOccupancy,
                BigDecimal.valueOf(tIDSetCouncilUniqueNonDependentsEffected.size()));
        // RSL
        generalStatistics.put(
                sTotalCount_RSLUniqueIndividualsEffectedByUnderOccupancy,
                BigDecimal.valueOf(tIDSetRSLUniqueIndividualsEffected.size()
                        + totalRSLDependentsEffected));
        generalStatistics.put(
                sTotalCount_RSLUniqueClaimantsEffectedByUnderOccupancy,
                BigDecimal.valueOf(tIDSetRSLUniqueClaimantsEffected.size()));
        generalStatistics.put(
                sTotalCount_RSLUniquePartnersEffectedByUnderOccupancy,
                BigDecimal.valueOf(tIDSetRSLUniquePartnersEffected.size()));
        generalStatistics.put(
                sTotalCount_RSLDependentsEffectedByUnderOccupancy,
                BigDecimal.valueOf(totalRSLDependentsEffected));
        generalStatistics.put(
                sTotalCount_RSLUniqueNonDependentsEffectedByUnderOccupancy,
                BigDecimal.valueOf(tIDSetRSLUniqueNonDependentsEffected.size()));
    }

    public void process(
            TreeMap<String, BigDecimal> aggregateStatistics,
            TreeMap<String, BigDecimal> generalStatistics,
            String aCTBRef,
            String year,
            String month,
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
            TreeSet<String> tCTBRefSetMoved,
            TreeSet<String> tCTBRefSetChangedTT,
            TreeSet<String> tCTBRefSetAlwaysUOFromStart,
            TreeSet<String> tCTBRefSetAlwaysUOFromWhenStarted,
            TreeSet<String> tCTBRefSetIntermitantUO,
            TreeSet<String> tCTBRefSetUO_To_LeftSHBE,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBEPermanently,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT1orTT4,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
            TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE,
            TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBEPermanently,
            TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT1orTT4,
            TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
            TreeSet<String> tCTBRefSetUOTT1_To_TT3OrTT6,
            TreeSet<String> tCTBRefSetUOTT4_To_TT3OrTT6,
            TreeSet<String> tCTBRefSetTT3OrTT6_To_UOTT1,
            TreeSet<String> tCTBRefSetTT3OrTT6_To_UOTT4,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious,
            TreeSet<String> tCTBRefSetUOTT1_ToTT1_PostcodeChanged,
            TreeSet<String> tCTBRefSetUOTT4_ToTT4_PostcodeChanged,
            TreeSet<String> tCTBRefSetTT1_ToUOTT1_PostcodeChanged,
            TreeSet<String> tCTBRefSetTT4_ToUOTT4_PostcodeChanged
    ) {
        String aS;
        String key;
        DW_SHBE_D_Record aDW_SHBE_D_Record = null;
        boolean isPairedRecord;
        int aTT;
        String aPC;
        int aWHBE;
        int aWERA;
        int aPSI;
        int aSHBC;
        int aRTHBCC;
        int aCEG;
        long aHS;
        long aND;
        long aCD;
        String aPDD;
        String aCDoB;
        String aPDoB;
        String aCA;
        String aPA;
        String aCG;
        String aPG;
        String aD;
        String aDP;
        String aDS;
        String aDC;
        String aDE;
        int aHBDP = 0;
        Double aArrears;
        DW_SHBE_D_Record bDW_SHBE_D_Record;
        int bTT;
        String bPC;
        int bWHBE;
        int bWERA = 0;
        int bPSI;
        int bSHBC;
        int bRTHBCC;
        int bCEG;
        long bHS = 0;
        long bND = 0;
        long bCD = 0;
        String bPDD;
        String bCDoB;
        String bPDoB;
        String bCA;
        String bPA;
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
            aPSI = 0;
            aSHBC = 0;
            aRTHBCC = 0;
            aCEG = 0;
            aHS = 0;
            aND = 0;
            aCD = 0;
            aPDD = s;
            aCDoB = s;
            aPDoB = s;
            aCA = s;
            aPA = s;
            aCG = s;
            aPG = s;
            aD = s;
            aDP = s;
            aDS = s;
            aDC = s;
            aDE = s;
            aHBDP = 0;
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
            aPSI = aDW_SHBE_D_Record.getPassportedStandardIndicator();
            aSHBC = aDW_SHBE_D_Record.getStatusOfHBClaimAtExtractDate();
            aRTHBCC = aDW_SHBE_D_Record.getReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective();
            aCEG = aDW_SHBE_D_Record.getClaimantsEthnicGroup();
            aHS = DW_SHBE_Handler.getHouseholdSize(aDW_SHBE_D_Record);
            aND = aDW_SHBE_D_Record.getNumberOfNonDependents();
            aCD = aDW_SHBE_D_Record.getNumberOfChildDependents();
            aCDoB = aDW_SHBE_D_Record.getClaimantsDateOfBirth();
            aPDoB = aDW_SHBE_D_Record.getPartnersDateOfBirth();
            if (DW_SHBE_Handler.getDisability(aDW_SHBE_D_Record)) {
                aD = sDisability;
            } else {
                aD = s;
            }
            // Disability Premium
            if (aDW_SHBE_D_Record.getDisabilityPremiumAwarded() == 1) {
                aDP = sDP;
            } else {
                aDP = s;
            }
            // Disability Severe
            if (aDW_SHBE_D_Record.getSevereDisabilityPremiumAwarded() == 1) {
                aDS = sDS;
            } else {
                aDS = s;
            }
            // Disability Enhanced
            if (aDW_SHBE_D_Record.getEnhancedDisabilityPremiumAwarded() == 1) {
                aDE = sDE;
            } else {
                aDE = s;
            }
            // Disabiled Child
            if (aDW_SHBE_D_Record.getDisabledChildPremiumAwarded() == 1) {
                aDC = sDC;
            } else {
                aDC = s;
            }
            aCA = DW_SHBE_Handler.getClaimantsAge(year, month, aDW_SHBE_D_Record);
            aPA = DW_SHBE_Handler.getPartnersAge(year, month, aDW_SHBE_D_Record);
            aCG = aDW_SHBE_D_Record.getClaimantsGender();
            aPG = aDW_SHBE_D_Record.getPartnersGender();
            aPDD = aDW_SHBE_D_Record.getPartnersDateOfDeath();
            aHBDP = aDW_SHBE_D_Record.getWeeklyAdditionalDiscretionaryPayment();
        }
        if (bDW_SHBE_Record == null) {
            bTT = DW_SHBE_TenancyType_Handler.iMinus999;
            bPC = defaultPostcode;
            bWHBE = 0;
            bWERA = 0;
            bPSI = 0;
            bSHBC = 0;
            bRTHBCC = 0;
            bCEG = 0;
            bHS = 0;
            bND = 0;
            bCD = 0;
            bPDD = s;
            bCDoB = s;
            bPDoB = s;
            bCA = s;
            bPA = s;
        } else {
            bDW_SHBE_D_Record = bDW_SHBE_Record.getDRecord();
            bTT = bDW_SHBE_D_Record.getTenancyType();
            bPC = bDW_SHBE_D_Record.getClaimantsPostcode();
            if (bPC.isEmpty()) {
                bPC = defaultPostcode;
            }
            bWHBE = bDW_SHBE_D_Record.getWeeklyHousingBenefitEntitlement();
            bWERA = bDW_SHBE_D_Record.getWeeklyEligibleRentAmount();
            bPSI = bDW_SHBE_D_Record.getPassportedStandardIndicator();
            bSHBC = bDW_SHBE_D_Record.getStatusOfHBClaimAtExtractDate();
            bRTHBCC = bDW_SHBE_D_Record.getReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective();
            bCEG = bDW_SHBE_D_Record.getClaimantsEthnicGroup();
            bHS = DW_SHBE_Handler.getHouseholdSize(bDW_SHBE_D_Record);
            bND = bDW_SHBE_D_Record.getNumberOfNonDependents();
            bCD = bDW_SHBE_D_Record.getNumberOfChildDependents();
            bPDD = bDW_SHBE_D_Record.getPartnersDateOfDeath();
            bCDoB = bDW_SHBE_D_Record.getClaimantsDateOfBirth();
            bPDoB = bDW_SHBE_D_Record.getPartnersDateOfBirth();
            bCA = DW_SHBE_Handler.getClaimantsAge(year, month, bDW_SHBE_D_Record);
            bPA = DW_SHBE_Handler.getPartnersAge(year, month, bDW_SHBE_D_Record);
        }

        boolean wasUO;
        key = aCTBRef + sUnderscore + sUnderOccupancy;
        aS = tableValues.get(key);
        wasUO = aS.endsWith(sU);

        boolean isUO;
        isUO = (councilUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)
                || RSLUnderOccupiedSet1.getMap().keySet().contains(aCTBRef));

        if (tCTBRefSetUOTT1_To_LeftSHBEPermanently.contains(aCTBRef)) {
            tCTBRefSetUOTT1_To_LeftSHBEPermanently.remove(aCTBRef);
            if (aTT == 1 || aTT == 4) {
                tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT1orTT4.add(aCTBRef);
            } else {
                if (aTT == 3 || aTT == 6) {
                    tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT3OrTT6.add(aCTBRef);
                }
            }
        }

        if (tCTBRefSetUOTT4_To_LeftSHBEPermanently.contains(aCTBRef)) {
            tCTBRefSetUOTT4_To_LeftSHBEPermanently.remove(aCTBRef);
            if (aTT == 1 || aTT == 4) {
                tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT1orTT4.add(aCTBRef);
            } else {
                if (aTT == 3 || aTT == 6) {
                    tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT3OrTT6.add(aCTBRef);
                }
            }
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
                if (aTT == DW_SHBE_TenancyType_Handler.iMinus999) {
                    // If previously UO then add to from UO to left the SHBE
                    if (wasUO) {
                        tCTBRefSetUO_To_LeftSHBE.add(aCTBRef);
                        tCTBRefSetUOTT1_To_LeftSHBEPermanently.add(aCTBRef);
                        if (bTT == 1) {
                            tCTBRefSetUOTT1_To_LeftSHBE.add(aCTBRef);
                        } else {
                            if (bTT == 4) {
                                tCTBRefSetUOTT4_To_LeftSHBE.add(aCTBRef);
                            }
                        }
                    }
                }
            } else {
                tCTBRefSetChangedTT.add(aCTBRef);
                if (aTT == 3 || aTT == 6) {
                    if (bTT == 1) {
                        tCTBRefSetTT1_To_TT3OrTT6.add(aCTBRef);
                        // If previously UO then add to set of those that move from UO TT1 to TT3 or TT6
                        if (wasUO) {
                            tCTBRefSetUOTT1_To_TT3OrTT6.add(aCTBRef);
                        }
                    } else {
                        if (bTT == 4) {
                            tCTBRefSetTT4_To_TT3OrTT6.add(aCTBRef);
                            // If previously UO then add to set of those that move from UO TT4 to TT3 or TT6
                            if (wasUO) {
                                tCTBRefSetUOTT4_To_TT3OrTT6.add(aCTBRef);
                            }
                        }
                    }
                } else {
                    if (bTT == 3 || aTT == 6) {
                        if (aTT == 1) {
                            tCTBRefSetTT3OrTT6_To_TT1.add(aCTBRef);
                            // If UO add to set that move from the PRS to Council UO.
                            if (councilUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)
                                    || RSLUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)) {
                                tCTBRefSetTT3OrTT6_To_UOTT1.add(aCTBRef);
                            }
                        } else {
                            if (aTT == 4) {
                                tCTBRefSetTT3OrTT6_To_TT4.add(aCTBRef);
                                // If UO add to set that move from the PRS to RSL UO.
                                if (councilUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)
                                        || RSLUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)) {
                                    tCTBRefSetTT3OrTT6_To_UOTT4.add(aCTBRef);
                                }
                            }
                        }
                    }
                }
            }
        }
        aS += sCommaSpace + sTT_ + aTT;
        tableValues.put(key, aS);
        // UnderOccupancy
        key = aCTBRef + sUnderscore + sUnderOccupancy;
        aS = tableValues.get(key);
        if (isUO) {
            tCTBRefSetAlwaysUOFromWhenStarted.add(aCTBRef);
            aS += sCommaSpace + sU;
            BigDecimal bd;
            String key2 = aCTBRef + sUnderscore + sTotalCount_UnderOccupancy;
            bd = aggregateStatistics.get(key2);
            bd = bd.add(BigDecimal.ONE);
            aggregateStatistics.put(key2, bd);
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
            if (!wasUO) {
                // UO OnFlow
                if (aTT == bTT) {
                    // Became UO staying in the same TT and postcode.
                    // Here we only count room requirement changes at the same 
                    // postcode (postcode changes are dealt with below).
                    if (aPC.equalsIgnoreCase(bPC)) {
                        if (aTT == 1) {
                            tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged.add(aCTBRef);
                            tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth.add(aCTBRef);
                        } else {
                            if (aTT == 4) {
                                tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged.add(aCTBRef);
                                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth.add(aCTBRef);
                            }
                        }
                    }
                }
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
            if (wasUO) {
                if (aTT == bTT) {
                    // SolvedUO problem staying in same TT
                    // Cases involving a postcode change are dealt with below.
                    if (aPC.equalsIgnoreCase(bPC)) {
                        // Resolved UO without moving
                        // Room requirement changed or number of rooms reduced or both?
                        if (aTT == 1) {
                            tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged.add(aCTBRef);
                            tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth.add(aCTBRef);
                        } else {
                            if (aTT == 4) {
                                tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged.add(aCTBRef);
                                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth.add(aCTBRef);
                            }
                        }
                    }
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
                    if (wasUO && !isUO) {
                        if (aTT == bTT) {
                            if (aTT == 1) {
                                tCTBRefSetUOTT1_ToTT1_PostcodeChanged.add(aCTBRef);
                            } else {
                                if (aTT == 4) {
                                    tCTBRefSetUOTT4_ToTT4_PostcodeChanged.add(aCTBRef);
                                }
                            }
                        }
                    } else {
                        if (!wasUO && isUO) {
                            if (aTT == bTT) {
                                if (aTT == 1) {
                                    tCTBRefSetTT1_ToUOTT1_PostcodeChanged.add(aCTBRef);
                                } else {
                                    if (aTT == 4) {
                                        tCTBRefSetTT4_ToUOTT4_PostcodeChanged.add(aCTBRef);
                                    }
                                }
                            }
                        }
                    }
                    if (tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious.contains(aCTBRef)) {
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month.add(aCTBRef);
                    }
                    if (tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious.contains(aCTBRef)) {
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month.add(aCTBRef);
                    }
                    if (tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious.contains(aCTBRef)) {
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months.add(aCTBRef);
                    }
                    if (tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious.contains(aCTBRef)) {
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months.add(aCTBRef);
                    }
                    if (tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious.contains(aCTBRef)) {
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month.add(aCTBRef);
                    }
                    if (tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious.contains(aCTBRef)) {
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month.add(aCTBRef);
                    }
                    if (tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious.contains(aCTBRef)) {
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months.add(aCTBRef);
                    }
                    if (tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious.contains(aCTBRef)) {
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months.add(aCTBRef);
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
            aS += sCommaSpace + decimalise(aWHBE);
        }
        tableValues.put(key, aS);
        // ERA
        key = aCTBRef + sUnderscore + sWERA;
        aS = tableValues.get(key);
        if (aWERA == bWERA) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + decimalise(aWERA);
        }
        tableValues.put(key, aS);
        // PassportedStandardIndicator
        key = aCTBRef + sUnderscore + sPSI;
        aS = tableValues.get(key);
        if (aPSI == bPSI) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aPSI;
        }
        tableValues.put(key, aS);
        // StatusOfHBClaim
        key = aCTBRef + sUnderscore + sSHBC;
        aS = tableValues.get(key);
        if (aSHBC == bSHBC) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aSHBC;
        }
        tableValues.put(key, aS);
        // ReasonThatHBClaimClosed
        key = aCTBRef + sUnderscore + sRTHBCC;
        aS = tableValues.get(key);
        if (aRTHBCC == bRTHBCC) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aRTHBCC;
        }
        tableValues.put(key, aS);
        // ClaimantEthnicGroup
        key = aCTBRef + sUnderscore + sCEG;
        aS = tableValues.get(key);
        if (aCEG == bCEG) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aCEG;
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
            key = aCTBRef + sUnderscore + sNDUO;
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

            int loss;
            loss = aWERA - aWHBE;
            key = aCTBRef + sUnderscore + sTotal_HBLossDueToUO;
            BigDecimal bd;
            bd = aggregateStatistics.get(key);
            bd = bd.add(BigDecimal.valueOf(loss));
            aggregateStatistics.put(key, bd);
            if (loss > 0) {
                key = aCTBRef + sUnderscore + sTotalCount_HBLossDueToUO;
                bd = aggregateStatistics.get(key);
                bd = bd.add(BigDecimal.ONE);
                aggregateStatistics.put(key, bd);
            }
        } else {
            // NonDependents
            key = aCTBRef + sUnderscore + sNDUO;
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
        // Claimants DoB
        key = aCTBRef + sUnderscore + sCDoB;
        aS = tableValues.get(key);
        if (aCDoB.equalsIgnoreCase(bCDoB)) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aCDoB;
        }
        tableValues.put(key, aS);
        // Claimants Age
        key = aCTBRef + sUnderscore + sCA;
        aS = tableValues.get(key);
        if (aCA.equalsIgnoreCase(bCA)) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aCA;
        }
        tableValues.put(key, aS);
        // Partners DoB
        key = aCTBRef + sUnderscore + sPDoB;
        aS = tableValues.get(key);
        if (aPDoB.equalsIgnoreCase(bPDoB)) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aPDoB;
        }
        tableValues.put(key, aS);
        // Partners Age
        key = aCTBRef + sUnderscore + sPA;
        aS = tableValues.get(key);
        if (aPA.equalsIgnoreCase(bPA)) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aPA;
        }
        tableValues.put(key, aS);
        // ClaimantsGender
        key = aCTBRef + sUnderscore + sCG;
        aS = tableValues.get(key);
        if (aCA.equalsIgnoreCase(bCA)) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aCG;
        }
        tableValues.put(key, aS);
        // PartnersGender
        key = aCTBRef + sUnderscore + sPG;
        aS = tableValues.get(key);
        if (aPA.equalsIgnoreCase(bPA)) {
            aS += sCommaSpace;
        } else {
            aS += sCommaSpace + aPG;
        }
        tableValues.put(key, aS);
        // Disability
        key = aCTBRef + sUnderscore + sDisability;
        aS = tableValues.get(key);
        aS += sCommaSpace + aD;
        tableValues.put(key, aS);
        // Disability Premium
        key = aCTBRef + sUnderscore + sDisabilityPremium;
        aS = tableValues.get(key);
        aS += sCommaSpace + aDP;
        tableValues.put(key, aS);
        // Disability Severe
        key = aCTBRef + sUnderscore + sDisabilitySevere;
        aS = tableValues.get(key);
        aS += sCommaSpace + aDS;
        tableValues.put(key, aS);
        // Disability Enhanced
        key = aCTBRef + sUnderscore + sDisabilityEnhanced;
        aS = tableValues.get(key);
        aS += sCommaSpace + aDE;
        tableValues.put(key, aS);
        // Child Disability
        key = aCTBRef + sUnderscore + sDisabledChild;
        aS = tableValues.get(key);
        aS += sCommaSpace + aDC;
        tableValues.put(key, aS);
        // Partner Death
        key = aCTBRef + sUnderscore + sPDeath;
        aS = tableValues.get(key);
        if (aPDD.equalsIgnoreCase(bPDD)) {
            aS += sCommaSpace;
        } else {
            if (aPDD == null) {
                aS += sCommaSpace;
            } else {
                if (aPDD.isEmpty()) {
                    aS += sCommaSpace;
                } else {
                    aS += sCommaSpace + sPDeath + sUnderscore + aPDD;
                }
            }
        }
        tableValues.put(key, aS);

        // HBDP
        BigDecimal bd;
        key = aCTBRef + sUnderscore + sTotal_DHP;
        bd = aggregateStatistics.get(key);
        bd = bd.add(BigDecimal.valueOf(aHBDP));
        aggregateStatistics.put(key, bd);
        if (aHBDP > 0) {
            key = aCTBRef + sUnderscore + sTotalCount_DHP;
            bd = aggregateStatistics.get(key);
            bd = bd.add(BigDecimal.ONE);
            aggregateStatistics.put(key, bd);
        }

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
                    key = aCTBRef + sUnderscore + sMax_Arrears;
                    bd = aggregateStatistics.get(key);
                    bd = bd.max(BigDecimal.valueOf(aArrears));
                    aggregateStatistics.put(key, bd);
                    if (aArrears > 0) {
                        key = aCTBRef + sUnderscore + sTotalCount_Arrears;
                        bd = aggregateStatistics.get(key);
                        bd = bd.add(BigDecimal.ONE);
                        aggregateStatistics.put(key, bd);
                    }
                }
            }
        } else {
            aS += sCommaSpace;
        }
        tableValues.put(key, aS);
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

    public ArrayList<String> getKeys(String aCTBRef) {
        ArrayList<String> result;
        result = new ArrayList<String>();
        // TenancyType
        result.add(aCTBRef + sUnderscore + sTT);
        // UnderOccupancy
        result.add(aCTBRef + sUnderscore + sUnderOccupancy);
        // Postcode
        result.add(aCTBRef + sUnderscore + sP);
        // WeeklyHousingBenefitEntitlement
        result.add(aCTBRef + sUnderscore + sWHBE);
        // WeeklyHousingBenefitEntitlement
        result.add(aCTBRef + sUnderscore + sWERA);
        // PassportedStandardIndicator
        result.add(aCTBRef + sUnderscore + sPSI);
        // StatusOfHBClaim
        result.add(aCTBRef + sUnderscore + sSHBC);
        // ReasonThatHBClaimClosed
        result.add(aCTBRef + sUnderscore + sRTHBCC);
        // ClaimantEthnicGroup
        result.add(aCTBRef + sUnderscore + sCEG);
        // Arrears
        result.add(aCTBRef + sUnderscore + sA);
        // HB DiscretionaryPayment
        result.add(aCTBRef + sUnderscore + sHBDP);
        // Disability
        result.add(aCTBRef + sUnderscore + sDisability);
        // Disability Premium
        result.add(aCTBRef + sUnderscore + sDisabilityPremium);
        // Disability Severe
        result.add(aCTBRef + sUnderscore + sDisabilitySevere);
        // Disability Enhanced
        result.add(aCTBRef + sUnderscore + sDisabilityEnhanced);
        // Child Disability
        result.add(aCTBRef + sUnderscore + sDisabledChild);
        // Partner Death
        result.add(aCTBRef + sUnderscore + sPDeath);
        // Household Size
        result.add(aCTBRef + sUnderscore + sHS);
        // NonDependents
        result.add(aCTBRef + sUnderscore + sND);
        // ChildDependents
        result.add(aCTBRef + sUnderscore + sCD);
        // NonDependents (UO)
        result.add(aCTBRef + sUnderscore + sNDUO);
        // Children 16 +
        result.add(aCTBRef + sUnderscore + sCO16);
        // FemaleChildrenUnder10
        result.add(aCTBRef + sUnderscore + sFCU10);
        // MaleChildrenUnder10
        result.add(aCTBRef + sUnderscore + sMCU10);
        // FemaleChildren10to16
        result.add(aCTBRef + sUnderscore + sFC10To16);
        // MaleChildren10to16
        result.add(aCTBRef + sUnderscore + sMC10To16);
        // Number of Bedrooms
        result.add(aCTBRef + sUnderscore + sNB);
        // Bedroom Requirement
        result.add(aCTBRef + sUnderscore + sBR);
        // Claimants DoB
        result.add(aCTBRef + sUnderscore + sCDoB);
        // Claimants Age
        result.add(aCTBRef + sUnderscore + sCA);
        // Claimants Gender
        result.add(aCTBRef + sUnderscore + sCG);
        // Partners DoB
        result.add(aCTBRef + sUnderscore + sPDoB);
        // Partners Age
        result.add(aCTBRef + sUnderscore + sPA);
        // Partners Gender
        result.add(aCTBRef + sUnderscore + sPG);
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
        TreeMap<String, BigDecimal> aggregateStatistics;
        aggregateStatistics = (TreeMap<String, BigDecimal>) table[5];
        TreeMap<String, BigDecimal> generalStatistics;
        generalStatistics = (TreeMap<String, BigDecimal>) table[6];
        TreeMap<String, String> groupNameDescriptions;
        groupNameDescriptions = getGroupNameDescriptions(groups.keySet());

        String aggregateStatisticsHeader;
        //aggregateStatisticsHeader = "CTBRef, DHP_Total, Housing Benefit Loss as a Result of UnderOccupancy, Max_Arrears, NumberOfUnderOccupancyMonths";
        aggregateStatisticsHeader = "CTBRef " + sCommaSpace
                + sTotal_DHP + sCommaSpace
                + sTotalCount_DHP + sCommaSpace
                + sTotal_HBLossDueToUO + sCommaSpace
                + sTotalCount_HBLossDueToUO + sCommaSpace
                + sMax_Arrears + sCommaSpace
                + sTotalCount_Arrears + sCommaSpace
                + sTotalCount_UnderOccupancy;

        String generalStatisticsHeader;
        generalStatisticsHeader = "GeneralStatistic, Value";
        PrintWriter pw5;
        pw5 = getPrintWriter("GeneralStatistics", paymentType, includeKey, underOccupancy);
        pw5.println(generalStatisticsHeader);
        String line;
        line = sTotalCount_AlwaysUOFromWhenStarted + sCommaSpace
                + generalStatistics.get(sTotalCount_AlwaysUOFromWhenStarted);
        pw5.println(line);
        line = sTotalCount_ClaimsEffectedByUnderOccupancy + sCommaSpace
                + generalStatistics.get(sTotalCount_ClaimsEffectedByUnderOccupancy);
        pw5.println(line);
        line = sTotalCount_UniqueIndividualsEffectedByUnderOccupancy + sCommaSpace
                + generalStatistics.get(sTotalCount_UniqueIndividualsEffectedByUnderOccupancy);
        pw5.println(line);
        line = sTotalCount_CouncilClaimsEffectedByUnderOccupancy + sCommaSpace
                + generalStatistics.get(sTotalCount_CouncilClaimsEffectedByUnderOccupancy);
        pw5.println(line);
        line = sTotalCount_CouncilUniqueIndividualsEffectedByUnderOccupancy + sCommaSpace
                + generalStatistics.get(sTotalCount_CouncilUniqueIndividualsEffectedByUnderOccupancy);
        pw5.println(line);
        line = sTotalCount_CouncilUniqueClaimantsEffectedByUnderOccupancy + sCommaSpace
                + generalStatistics.get(sTotalCount_CouncilUniqueClaimantsEffectedByUnderOccupancy);
        pw5.println(line);
        line = sTotalCount_CouncilUniquePartnersEffectedByUnderOccupancy + sCommaSpace
                + generalStatistics.get(sTotalCount_CouncilUniquePartnersEffectedByUnderOccupancy);
        pw5.println(line);
        line = sTotalCount_CouncilDependentsEffectedByUnderOccupancy + sCommaSpace
                + generalStatistics.get(sTotalCount_CouncilDependentsEffectedByUnderOccupancy);
        pw5.println(line);
        line = sTotalCount_CouncilUniqueNonDependentsEffectedByUnderOccupancy + sCommaSpace
                + generalStatistics.get(sTotalCount_CouncilUniqueNonDependentsEffectedByUnderOccupancy);
        pw5.println(line);
        line = sTotalCount_RSLClaimsEffectedByUnderOccupancy + sCommaSpace
                + generalStatistics.get(sTotalCount_RSLClaimsEffectedByUnderOccupancy);
        pw5.println(line);
        line = sTotalCount_RSLUniqueIndividualsEffectedByUnderOccupancy + sCommaSpace
                + generalStatistics.get(sTotalCount_RSLUniqueIndividualsEffectedByUnderOccupancy);
        pw5.println(line);
        line = sTotalCount_RSLUniqueClaimantsEffectedByUnderOccupancy + sCommaSpace
                + generalStatistics.get(sTotalCount_RSLUniqueClaimantsEffectedByUnderOccupancy);
        pw5.println(line);
        line = sTotalCount_RSLUniquePartnersEffectedByUnderOccupancy + sCommaSpace
                + generalStatistics.get(sTotalCount_RSLUniquePartnersEffectedByUnderOccupancy);
        pw5.println(line);
        line = sTotalCount_RSLDependentsEffectedByUnderOccupancy + sCommaSpace
                + generalStatistics.get(sTotalCount_RSLDependentsEffectedByUnderOccupancy);
        pw5.println(line);
        line = sTotalCount_RSLUniqueNonDependentsEffectedByUnderOccupancy + sCommaSpace
                + generalStatistics.get(sTotalCount_RSLUniqueNonDependentsEffectedByUnderOccupancy);
        pw5.println(line);
        line = sUO_To_LeftSHBE + sCommaSpace + generalStatistics.get(sUO_To_LeftSHBE);

        pw5.println(line);
        line = sUOTT1_To_LeftSHBE + sCommaSpace + generalStatistics.get(sUOTT1_To_LeftSHBE);
        pw5.println(line);
        line = sUOTT1_To_LeftSHBEPermanently + sCommaSpace + generalStatistics.get(sUOTT1_To_LeftSHBEPermanently);
        pw5.println(line);
        line = sUOTT1_To_LeftSHBEReturnedAsTT1orTT4 + sCommaSpace + generalStatistics.get(sUOTT1_To_LeftSHBEReturnedAsTT1orTT4);
        pw5.println(line);
        line = sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6 + sCommaSpace + generalStatistics.get(sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6);
        pw5.println(line);
        line = sUOTT4_To_LeftSHBE + sCommaSpace + generalStatistics.get(sUOTT4_To_LeftSHBE);
        pw5.println(line);
        line = sUOTT4_To_LeftSHBEPermanently + sCommaSpace + generalStatistics.get(sUOTT4_To_LeftSHBEPermanently);
        pw5.println(line);
        line = sUOTT4_To_LeftSHBEReturnedAsTT1orTT4 + sCommaSpace + generalStatistics.get(sUOTT4_To_LeftSHBEReturnedAsTT1orTT4);
        pw5.println(line);
        line = sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6 + sCommaSpace + generalStatistics.get(sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6);
        pw5.println(line);

        line = sUOTT1_To_TT3OrTT6 + sCommaSpace + generalStatistics.get(sUOTT1_To_TT3OrTT6);
        pw5.println(line);
        line = sUOTT4_To_TT3OrTT6 + sCommaSpace + generalStatistics.get(sUOTT4_To_TT3OrTT6);
        pw5.println(line);
        line = sTT3OrTT6_To_UOTT1 + sCommaSpace + generalStatistics.get(sTT3OrTT6_To_UOTT1);
        pw5.println(line);
        line = sTT3OrTT6_To_UOTT4 + sCommaSpace + generalStatistics.get(sTT3OrTT6_To_UOTT4);
        pw5.println(line);

        line = sTT1_To_UOTT1_PostcodeUnchanged + sCommaSpace + generalStatistics.get(sTT1_To_UOTT1_PostcodeUnchanged);
        pw5.println(line);
        line = sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month + sCommaSpace + generalStatistics.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month);
        pw5.println(line);
        line = sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months + sCommaSpace + generalStatistics.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months);
        pw5.println(line);

        line = sTT4_To_UOTT4_PostcodeUnchanged + sCommaSpace + generalStatistics.get(sTT4_To_UOTT4_PostcodeUnchanged);
        pw5.println(line);
        line = sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month + sCommaSpace + generalStatistics.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month);
        pw5.println(line);
        line = sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months + sCommaSpace + generalStatistics.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months);
        pw5.println(line);

        line = sUOTT1_To_TT1_PostcodeUnchanged + sCommaSpace + generalStatistics.get(sUOTT1_To_TT1_PostcodeUnchanged);
        pw5.println(line);
        line = sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month + sCommaSpace + generalStatistics.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month);
        pw5.println(line);
        line = sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months + sCommaSpace + generalStatistics.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months);
        pw5.println(line);

        line = sUOTT4_To_TT4_PostcodeUnchanged + sCommaSpace + generalStatistics.get(sUOTT4_To_TT4_PostcodeUnchanged);
        pw5.println(line);
        line = sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month + sCommaSpace + generalStatistics.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month);
        pw5.println(line);
        line = sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months + sCommaSpace + generalStatistics.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months);
        pw5.println(line);

        line = sUOTT1_To_TT1_PostcodeChanged + sCommaSpace + generalStatistics.get(sUOTT1_To_TT1_PostcodeChanged);
        pw5.println(line);
        line = sUOTT4_To_TT4_PostcodeChanged + sCommaSpace + generalStatistics.get(sUOTT4_To_TT4_PostcodeChanged);
        pw5.println(line);
        line = sTT1_To_UOTT1_PostcodeChanged + sCommaSpace + generalStatistics.get(sTT1_To_UOTT1_PostcodeChanged);
        pw5.println(line);
        line = sTT4_To_UOTT4_PostcodeChanged + sCommaSpace + generalStatistics.get(sTT4_To_UOTT4_PostcodeChanged);
        pw5.println(line);
        line = sTotalCount_RSLUniqueNonDependentsEffectedByUnderOccupancy + sCommaSpace
                + generalStatistics.get(sTotalCount_RSLUniqueNonDependentsEffectedByUnderOccupancy);
        pw5.println(line);

        pw5.close();

        HashSet<String> tCTBRefsCheck;
        tCTBRefsCheck = new HashSet<String>();
        boolean check = true;

        String groupName;
        PrintWriter pw;
        PrintWriter pw2;
        PrintWriter pwAggregateStatistics;
        PrintWriter pwAggregateStatistics2;
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
            pwAggregateStatistics = getPrintWriter(name2 + "AggregateStatistics", paymentType, includeKey, underOccupancy);
            name2 += sUnderscore + "WithDuplicates";
            pw2 = getPrintWriter(name2, paymentType, includeKey, underOccupancy);
            pwAggregateStatistics2 = getPrintWriter(name2 + "AggregateStatistics", paymentType, includeKey, underOccupancy);
            // Write header
            groupNameDescription = groupNameDescriptions.get(groupName);
            pw.println(groupNameDescription);
            pw.println(header);
            pwAggregateStatistics.println(groupNameDescription);
            pwAggregateStatistics.println(aggregateStatisticsHeader);
            pw2.println(groupNameDescription);
            pw2.println(header);
            pwAggregateStatistics2.println(groupNameDescription);
            pwAggregateStatistics2.println(aggregateStatisticsHeader);
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
                                    includePreUnderOccupancyValues,
                                    preUnderOccupancyValues,
                                    aCTBRef,
                                    pw2);
                            writeAggregateRecords(
                                    aggregateStatistics,
                                    aCTBRef,
                                    pwAggregateStatistics2);
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
                                        includePreUnderOccupancyValues,
                                        preUnderOccupancyValues,
                                        aCTBRef,
                                        pw2);
                                writeAggregateRecords(
                                        aggregateStatistics,
                                        aCTBRef,
                                        pwAggregateStatistics2);
                            } else {
                                otherGroupName = sTTNot1Or4AndUnderOccupying;
                                otherGroup = groups.get(otherGroupName);
                                if (otherGroup.contains(aCTBRef)) {
                                    writeRecordCollection(
                                            tableValues,
                                            includePreUnderOccupancyValues,
                                            preUnderOccupancyValues,
                                            aCTBRef,
                                            pw2);
                                    writeAggregateRecords(
                                            aggregateStatistics,
                                            aCTBRef,
                                            pwAggregateStatistics2);
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
                                            includePreUnderOccupancyValues,
                                            preUnderOccupancyValues,
                                            aCTBRef,
                                            pw2);
                                    writeAggregateRecords(
                                            aggregateStatistics,
                                            aCTBRef,
                                            pwAggregateStatistics2);
                                } else {
                                    otherGroupName = sTTNot1Or4AndUnderOccupying;
                                    otherGroup = groups.get(otherGroupName);
                                    if (otherGroup.contains(aCTBRef)) {
                                        writeRecordCollection(
                                                tableValues,
                                                includePreUnderOccupancyValues,
                                                preUnderOccupancyValues,
                                                aCTBRef,
                                                pw2);
                                        writeAggregateRecords(
                                                aggregateStatistics,
                                                aCTBRef,
                                                pwAggregateStatistics2);
                                    } else {
                                        otherGroupName = sTT1_To_TT3OrTT6;
                                        otherGroup = groups.get(otherGroupName);
                                        if (otherGroup.contains(aCTBRef)) {
                                            writeRecordCollection(
                                                    tableValues,
                                                    includePreUnderOccupancyValues,
                                                    preUnderOccupancyValues,
                                                    aCTBRef,
                                                    pw2);
                                            writeAggregateRecords(
                                                    aggregateStatistics,
                                                    aCTBRef,
                                                    pwAggregateStatistics2);
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
                                                includePreUnderOccupancyValues,
                                                preUnderOccupancyValues,
                                                aCTBRef,
                                                pw2);
                                        writeAggregateRecords(
                                                aggregateStatistics,
                                                aCTBRef,
                                                pwAggregateStatistics2);
                                    } else {
                                        otherGroupName = sTTNot1Or4AndUnderOccupying;
                                        otherGroup = groups.get(otherGroupName);
                                        if (otherGroup.contains(aCTBRef)) {
                                            writeRecordCollection(
                                                    tableValues,
                                                    includePreUnderOccupancyValues,
                                                    preUnderOccupancyValues,
                                                    aCTBRef,
                                                    pw2);
                                            writeAggregateRecords(
                                                    aggregateStatistics,
                                                    aCTBRef,
                                                    pwAggregateStatistics2);
                                        } else {
                                            otherGroupName = sTT1_To_TT3OrTT6;
                                            otherGroup = groups.get(otherGroupName);
                                            if (otherGroup.contains(aCTBRef)) {
                                                writeRecordCollection(
                                                        tableValues,
                                                        includePreUnderOccupancyValues,
                                                        preUnderOccupancyValues,
                                                        aCTBRef,
                                                        pw2);
                                                writeAggregateRecords(
                                                        aggregateStatistics,
                                                        aCTBRef,
                                                        pwAggregateStatistics2);
                                            } else {
                                                otherGroupName = sTT4_To_TT3OrTT6;
                                                otherGroup = groups.get(otherGroupName);
                                                if (otherGroup.contains(aCTBRef)) {
                                                    writeRecordCollection(
                                                            tableValues,
                                                            includePreUnderOccupancyValues,
                                                            preUnderOccupancyValues,
                                                            aCTBRef,
                                                            pw2);
                                                    writeAggregateRecords(
                                                            aggregateStatistics,
                                                            aCTBRef,
                                                            pwAggregateStatistics2);
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
                                                    includePreUnderOccupancyValues,
                                                    preUnderOccupancyValues,
                                                    aCTBRef,
                                                    pw2);
                                            writeAggregateRecords(
                                                    aggregateStatistics,
                                                    aCTBRef,
                                                    pwAggregateStatistics2);
                                        } else {
                                            otherGroupName = sTTNot1Or4AndUnderOccupying;
                                            otherGroup = groups.get(otherGroupName);
                                            if (otherGroup.contains(aCTBRef)) {
                                                writeRecordCollection(
                                                        tableValues,
                                                        includePreUnderOccupancyValues,
                                                        preUnderOccupancyValues,
                                                        aCTBRef,
                                                        pw2);
                                                writeAggregateRecords(
                                                        aggregateStatistics,
                                                        aCTBRef,
                                                        pwAggregateStatistics2);
                                            } else {
                                                otherGroupName = sTT1_To_TT3OrTT6;
                                                otherGroup = groups.get(otherGroupName);
                                                if (otherGroup.contains(aCTBRef)) {
                                                    writeRecordCollection(
                                                            tableValues,
                                                            includePreUnderOccupancyValues,
                                                            preUnderOccupancyValues,
                                                            aCTBRef,
                                                            pw2);
                                                    writeAggregateRecords(
                                                            aggregateStatistics,
                                                            aCTBRef,
                                                            pwAggregateStatistics2);
                                                } else {
                                                    otherGroupName = sTT4_To_TT3OrTT6;
                                                    otherGroup = groups.get(otherGroupName);
                                                    if (otherGroup.contains(aCTBRef)) {
                                                        writeRecordCollection(
                                                                tableValues,
                                                                includePreUnderOccupancyValues,
                                                                preUnderOccupancyValues,
                                                                aCTBRef,
                                                                pw2);
                                                        writeAggregateRecords(
                                                                aggregateStatistics,
                                                                aCTBRef,
                                                                pwAggregateStatistics2);
                                                    } else {
                                                        otherGroupName = sTT3OrTT6_To_TT1;
                                                        otherGroup = groups.get(otherGroupName);
                                                        if (otherGroup.contains(aCTBRef)) {
                                                            writeRecordCollection(
                                                                    tableValues,
                                                                    includePreUnderOccupancyValues,
                                                                    preUnderOccupancyValues,
                                                                    aCTBRef,
                                                                    pw2);
                                                            writeAggregateRecords(
                                                                    aggregateStatistics,
                                                                    aCTBRef,
                                                                    pwAggregateStatistics2);
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
                                                        includePreUnderOccupancyValues,
                                                        preUnderOccupancyValues,
                                                        aCTBRef,
                                                        pw2);
                                                writeAggregateRecords(
                                                        aggregateStatistics,
                                                        aCTBRef,
                                                        pwAggregateStatistics2);
                                            } else {
                                                otherGroupName = sTTNot1Or4AndUnderOccupying;
                                                otherGroup = groups.get(otherGroupName);
                                                if (otherGroup.contains(aCTBRef)) {
                                                    writeRecordCollection(
                                                            tableValues,
                                                            includePreUnderOccupancyValues,
                                                            preUnderOccupancyValues,
                                                            aCTBRef,
                                                            pw2);
                                                    writeAggregateRecords(
                                                            aggregateStatistics,
                                                            aCTBRef,
                                                            pwAggregateStatistics2);
                                                } else {
                                                    otherGroupName = sTT1_To_TT3OrTT6;
                                                    otherGroup = groups.get(otherGroupName);
                                                    if (otherGroup.contains(aCTBRef)) {
                                                        writeRecordCollection(
                                                                tableValues,
                                                                includePreUnderOccupancyValues,
                                                                preUnderOccupancyValues,
                                                                aCTBRef,
                                                                pw2);
                                                        writeAggregateRecords(
                                                                aggregateStatistics,
                                                                aCTBRef,
                                                                pwAggregateStatistics2);
                                                    } else {
                                                        otherGroupName = sTT4_To_TT3OrTT6;
                                                        otherGroup = groups.get(otherGroupName);
                                                        if (otherGroup.contains(aCTBRef)) {
                                                            writeRecordCollection(
                                                                    tableValues,
                                                                    includePreUnderOccupancyValues,
                                                                    preUnderOccupancyValues,
                                                                    aCTBRef,
                                                                    pw2);
                                                            writeAggregateRecords(
                                                                    aggregateStatistics,
                                                                    aCTBRef,
                                                                    pwAggregateStatistics2);
                                                        } else {
                                                            otherGroupName = sTT3OrTT6_To_TT1;
                                                            otherGroup = groups.get(otherGroupName);
                                                            if (otherGroup.contains(aCTBRef)) {
                                                                writeRecordCollection(
                                                                        tableValues,
                                                                        includePreUnderOccupancyValues,
                                                                        preUnderOccupancyValues,
                                                                        aCTBRef,
                                                                        pw2);
                                                                writeAggregateRecords(
                                                                        aggregateStatistics,
                                                                        aCTBRef,
                                                                        pwAggregateStatistics2);
                                                            } else {
                                                                otherGroupName = sTT3OrTT6_To_TT4;
                                                                otherGroup = groups.get(otherGroupName);
                                                                if (otherGroup.contains(aCTBRef)) {
                                                                    writeRecordCollection(
                                                                            tableValues,
                                                                            includePreUnderOccupancyValues,
                                                                            preUnderOccupancyValues,
                                                                            aCTBRef,
                                                                            pw2);
                                                                    writeAggregateRecords(
                                                                            aggregateStatistics,
                                                                            aCTBRef,
                                                                            pwAggregateStatistics2);
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
                                                            includePreUnderOccupancyValues,
                                                            preUnderOccupancyValues,
                                                            aCTBRef,
                                                            pw2);
                                                    writeAggregateRecords(
                                                            aggregateStatistics,
                                                            aCTBRef,
                                                            pwAggregateStatistics2);
                                                } else {
                                                    otherGroupName = sTTNot1Or4AndUnderOccupying;
                                                    otherGroup = groups.get(otherGroupName);
                                                    if (otherGroup.contains(aCTBRef)) {
                                                        writeRecordCollection(
                                                                tableValues,
                                                                includePreUnderOccupancyValues,
                                                                preUnderOccupancyValues,
                                                                aCTBRef,
                                                                pw2);
                                                        writeAggregateRecords(
                                                                aggregateStatistics,
                                                                aCTBRef,
                                                                pwAggregateStatistics2);
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
                                                                includePreUnderOccupancyValues,
                                                                preUnderOccupancyValues,
                                                                aCTBRef,
                                                                pw2);
                                                        writeAggregateRecords(
                                                                aggregateStatistics,
                                                                aCTBRef,
                                                                pwAggregateStatistics2);
                                                    } else {
                                                        otherGroupName = sTTNot1Or4AndUnderOccupying;
                                                        otherGroup = groups.get(otherGroupName);
                                                        if (otherGroup.contains(aCTBRef)) {
                                                            writeRecordCollection(
                                                                    tableValues,
                                                                    includePreUnderOccupancyValues,
                                                                    preUnderOccupancyValues,
                                                                    aCTBRef,
                                                                    pw2);
                                                            writeAggregateRecords(
                                                                    aggregateStatistics,
                                                                    aCTBRef,
                                                                    pwAggregateStatistics2);
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
                                                                    includePreUnderOccupancyValues,
                                                                    preUnderOccupancyValues,
                                                                    aCTBRef,
                                                                    pw2);
                                                            writeAggregateRecords(
                                                                    aggregateStatistics,
                                                                    aCTBRef,
                                                                    pwAggregateStatistics2);
                                                        } else {
                                                            otherGroupName = sTTNot1Or4AndUnderOccupying;
                                                            otherGroup = groups.get(otherGroupName);
                                                            if (otherGroup.contains(aCTBRef)) {
                                                                writeRecordCollection(
                                                                        tableValues,
                                                                        includePreUnderOccupancyValues,
                                                                        preUnderOccupancyValues,
                                                                        aCTBRef,
                                                                        pw2);
                                                                writeAggregateRecords(
                                                                        aggregateStatistics,
                                                                        aCTBRef,
                                                                        pwAggregateStatistics2);
                                                            } else {
                                                                otherGroupName = sTT1_To_TT3OrTT6;
                                                                otherGroup = groups.get(otherGroupName);
                                                                if (otherGroup.contains(aCTBRef)) {
                                                                    writeRecordCollection(
                                                                            tableValues,
                                                                            includePreUnderOccupancyValues,
                                                                            preUnderOccupancyValues,
                                                                            aCTBRef,
                                                                            pw2);
                                                                    writeAggregateRecords(
                                                                            aggregateStatistics,
                                                                            aCTBRef,
                                                                            pwAggregateStatistics2);
                                                                } else {
                                                                    otherGroupName = sTT4_To_TT3OrTT6;
                                                                    otherGroup = groups.get(otherGroupName);
                                                                    if (otherGroup.contains(aCTBRef)) {
                                                                        writeRecordCollection(
                                                                                tableValues,
                                                                                includePreUnderOccupancyValues,
                                                                                preUnderOccupancyValues,
                                                                                aCTBRef,
                                                                                pw2);
                                                                        writeAggregateRecords(
                                                                                aggregateStatistics,
                                                                                aCTBRef,
                                                                                pwAggregateStatistics2);
                                                                    } else {
                                                                        otherGroupName = sTT3OrTT6_To_TT1;
                                                                        otherGroup = groups.get(otherGroupName);
                                                                        if (otherGroup.contains(aCTBRef)) {
                                                                            writeRecordCollection(
                                                                                    tableValues,
                                                                                    includePreUnderOccupancyValues,
                                                                                    preUnderOccupancyValues,
                                                                                    aCTBRef,
                                                                                    pw2);
                                                                            writeAggregateRecords(
                                                                                    aggregateStatistics,
                                                                                    aCTBRef,
                                                                                    pwAggregateStatistics2);
                                                                        } else {
                                                                            otherGroupName = sTT3OrTT6_To_TT4;
                                                                            otherGroup = groups.get(otherGroupName);
                                                                            if (otherGroup.contains(aCTBRef)) {
                                                                                writeRecordCollection(
                                                                                        tableValues,
                                                                                        includePreUnderOccupancyValues,
                                                                                        preUnderOccupancyValues,
                                                                                        aCTBRef,
                                                                                        pw2);
                                                                                writeAggregateRecords(
                                                                                        aggregateStatistics,
                                                                                        aCTBRef,
                                                                                        pwAggregateStatistics2);
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
                                                                        includePreUnderOccupancyValues,
                                                                        preUnderOccupancyValues,
                                                                        aCTBRef,
                                                                        pw2);
                                                                writeAggregateRecords(
                                                                        aggregateStatistics,
                                                                        aCTBRef,
                                                                        pwAggregateStatistics2);
                                                            } else {
                                                                otherGroupName = sTTNot1Or4AndUnderOccupying;
                                                                otherGroup = groups.get(otherGroupName);
                                                                if (otherGroup.contains(aCTBRef)) {
                                                                    writeRecordCollection(
                                                                            tableValues,
                                                                            includePreUnderOccupancyValues,
                                                                            preUnderOccupancyValues,
                                                                            aCTBRef,
                                                                            pw2);
                                                                    writeAggregateRecords(
                                                                            aggregateStatistics,
                                                                            aCTBRef,
                                                                            pwAggregateStatistics2);
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
                            includePreUnderOccupancyValues,
                            preUnderOccupancyValues,
                            aCTBRef,
                            pw);
                    writeRecordCollection(
                            tableValues,
                            includePreUnderOccupancyValues,
                            preUnderOccupancyValues,
                            aCTBRef,
                            pw2);
                    writeAggregateRecords(
                            aggregateStatistics,
                            aCTBRef,
                            pwAggregateStatistics);
                    writeAggregateRecords(
                            aggregateStatistics,
                            aCTBRef,
                            pwAggregateStatistics2);
                }
            }
            System.out.println(group.size() + ", " + counter + ", " + groupNameDescription);
            pw.close();
            pwAggregateStatistics.close();
            pw2.close();
            pwAggregateStatistics2.close();
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
                    includePreUnderOccupancyValues,
                    preUnderOccupancyValues,
                    aCTBRef,
                    pw);
        }
        pw.close();
    }

    protected void writeAggregateRecords(
            TreeMap<String, BigDecimal> aggregateStatistics,
            String aCTBRef,
            PrintWriter pw) {
        String line;
        line = aCTBRef;
        line += sCommaSpace + decimalise(
                aggregateStatistics.get(aCTBRef + sUnderscore + sTotal_DHP).intValue());
        line += sCommaSpace + aggregateStatistics.get(aCTBRef + sUnderscore + sTotalCount_DHP);
        line += sCommaSpace + decimalise(aggregateStatistics.get(aCTBRef + sUnderscore + sTotal_HBLossDueToUO).intValue());
        line += sCommaSpace + aggregateStatistics.get(aCTBRef + sUnderscore + sTotalCount_HBLossDueToUO);
        line += sCommaSpace + aggregateStatistics.get(aCTBRef + sUnderscore + sMax_Arrears);
        line += sCommaSpace + aggregateStatistics.get(aCTBRef + sUnderscore + sTotalCount_Arrears);
        line += sCommaSpace + aggregateStatistics.get(aCTBRef + sUnderscore + sTotalCount_UnderOccupancy);
        pw.println(line);
    }

    protected void writeRecordCollection(
            TreeMap<String, String> tableValues,
            boolean includePreUnderOccupancyValues,
            TreeMap<String, String> preUnderOccupancyValues,
            String aCTBRef,
            PrintWriter pw) {
        ArrayList<String> keys;
        keys = getKeys(aCTBRef);
        String key;
        String line;
        Iterator<String> ite;
        ite = keys.iterator();
        while (ite.hasNext()) {
            key = ite.next();
            line = key;
            if (includePreUnderOccupancyValues) {
                line += preUnderOccupancyValues.get(key);
            }
            line += tableValues.get(key);
            pw.println(line);
        }
        pw.println();
    }

    private void writeRecordCollectionToStdOut(
            TreeMap<String, String> tableValues,
            boolean includePreUnderOccupancyValues,
            TreeMap<String, String> preUnderOccupancyValues,
            String aCTBRef) {
        ArrayList<String> keys;
        keys = getKeys(aCTBRef);
        String key;
        String line;
        Iterator<String> ite;
        ite = keys.iterator();
        while (ite.hasNext()) {
            key = ite.next();
            line = key;
            if (includePreUnderOccupancyValues) {
                line += preUnderOccupancyValues.get(key);
            }
            line += tableValues.get(key);
            System.out.println(line);
        }
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

//    protected TreeMap<String, String> profile(
//            TreeMap<String, String> tCTBRefYM3,
//            ) {
//        TreeMap<String, String> result;
//        result = new TreeMap<String, String>();
//        
//        return result;
//    }
}
