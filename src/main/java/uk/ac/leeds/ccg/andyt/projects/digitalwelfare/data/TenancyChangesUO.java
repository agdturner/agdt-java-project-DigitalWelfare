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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.math.Generic_BigDecimal;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Collection;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_CollectionHandler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_D_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_S_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Set;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class TenancyChangesUO extends DW_Object {

    // Go through all the UO data and get a list of all UO Claim refs
    boolean handleOutOfMemoryError = false;
    
    /**
     * A reference to DW_Environment
     * For convenience;
     */
    DW_SHBE_CollectionHandler DW_SHBE_CollectionHandler;
    DW_Strings DW_Strings;
    DW_SHBE_Handler DW_SHBE_Handler;
    DW_SHBE_TenancyType_Handler DW_SHBE_TenancyType_Handler;
    
    HashMap<String, DW_ID> tPostcodeToPostcodeIDLookup;
    HashSet<String> validPostcodes;
    //HashMap<String, HashSet<String>> validPostcodes;

    String sUnderOccupancyGroupTables = "UnderOccupancyGroupTables";
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

    String sTT_;
    String sBR = "BedroomRequirement";
    String sNB = "NumberOfBedrooms";
    String sNDUO = "NonDependents(UO)";
    String sCO16 = "ChildrenOver16";
    String sFCU10 = "FemaleChildrenUnder10";
    String sMCU10 = "MaleChildrenUnder10";
    String sFC10To16 = "FemaleChildren10to16";
    String sMC10To16 = "MaleChildren10to16";

    String sSubsequentlyEffectedUOStillUOInOctober2015 = "SubsequentlyEffectedUOStillUOInOctober2015";
    String sPercentageReceivingDHPInOctober2015 = "PercentageReceivingDHPInOctober2015";
    String sPercentageInArrearsOctober2015 = "PercentageInArrearsOctober2015";
    String sPercentageInArrearsAndReceivingDHPInOctober2015 = "PercentageInArrearsAndReceivingDHPInOctober2015";
    String sPercentageReceivingDHPInOctober2015ThatWereUOInApril2013 = "PercentageReceivingDHPInOctober2015ThatWereUOInApril2013";
    String sPercentageInArrearsOctober2015ThatWereUOInApril2013 = "PercentageInArrearsOctober2015ThatWereUOInApril2013";
    String sPercentageInArrearsAndReceivingDHPInOctober2015ThatWereUOInApril2013 = "PercentageInArrearsAndReceivingDHPInOctober2015ThatWereUOInApril2013";

    String sUniqueIndividualsEffected = "UniqueIndividualsEffected";

    String sNoValidPostcodeChange = "NoValidPostcodeChange";
    String sChangedTT = "ChangedTT";
    String sUOAtSomePoint = "sUOAtSomePoint";
    String sUOTT1AtSomePoint = "sUOTT1AtSomePoint";
    String sUOTT4AtSomePoint = "sUOTT4AtSomePoint";
    String sAlwaysUOTT1FromStart = "AlwaysUOTT1FromStart";
    String sAlwaysUOTT1FromStartExceptWhenSuspended = "AlwaysUOTT1FromStartExceptWhenSuspended";
    String sAlwaysUOTT1FromWhenStarted = "AlwaysUOTT1FromWhenStarted";
    String sAlwaysUOTT4FromStart = "AlwaysUOTT4FromStart";
    String sAlwaysUOTT4FromStartExceptWhenSuspended = "AlwaysUOTT4FromStartExceptWhenSuspended";
    String sAlwaysUOTT4FromWhenStarted = "AlwaysUOTT4FromWhenStarted";
    String sIntermitantUO = "IntermitantUO";
    String sPermanantlyLeftUOButRemainedInSHBE = "PermanantlyLeftUOButRemainedInSHBE";
    String sPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged = "PermanantlyLeftUOButRemainedInSHBE_PostcodeChanged";
    String sPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased = "PermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased";
    String sTravellers;
    String sTTNot1Or4AndUnderOccupying;
    String sTT3OrTT6_To_TT1;
    String sTT1_To_TT3OrTT6;
    String sTT3OrTT6_To_TT4;
    String sTT4_To_TT3OrTT6;
    String sAlwaysUOFromStart__NoValidPostcodeChange_NotChangedTT;
    String sAlwaysUOFromStart__ChangedTT;
    String sAlwaysUOFromStart__ValidPostcodeChange_NotChangedTT;
    String sAlwaysUOFromWhenStarted__NoValidPostcodeChange_NotChangedTT;
    String sAlwaysUOFromWhenStarted__ChangedTT;
    String sAlwaysUOFromWhenStarted__ValidPostcodeChange_NotChangedTT;
    String sIntermitantUO__ChangedTT;
    String sIntermitantUO__ValidPostcodeChange_NotChangedTT;
    String sIntermitantUO__NoValidPostcodeChange_NotChangedTT;

    String sUO_NotUO_UO = "UO_NotUO_UO";
    String sUO_NotUO_UO_NotUO = "UO_NotUO_UO_NotUO";
    String sUO_NotUO_UO_NotUO_UO = "UO_NotUO_UO_NotUO_UO";
    String sUO_NotUO_UO_NotUO_UO_NotUO = "UO_NotUO_UO_NotUO_UO_NotUO";
    String sUO_NotUO_UO_NotUO_UO_NotUO_UO = "UO_NotUO_UO_NotUO_UO_NotUO_UO";
    String sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO = "UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO";
    String sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO = "UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO";

    String sUOTT1_To_NotUO_InSHBE_PostcodeChanged = "UOTT1_To_NotUO_InSHBE_PostcodeChanged";
    String sUOTT1_To_UOTT1_PostcodeChanged = "UOTT1_To_UOTT1_PostcodeChanged";
    String sUOTT1_To_TT1_PostcodeChanged = "UOTT1_To_TT1_PostcodeChanged";
    String sUOTT1_To_UOTT4_PostcodeChanged = "UOTT1_To_UOTT4_PostcodeChanged";
    String sUOTT1_To_TT4_PostcodeChanged = "UOTT1_To_TT4_PostcodeChanged";
    String sUOTT4_To_NotUO_InSHBE_PostcodeChanged = "UOTT4_To_NotUO_InSHBE_PostcodeChanged";
    String sUOTT4_To_UOTT1_PostcodeChanged = "UOTT4_To_UOTT1_PostcodeChanged";
    String sUOTT4_To_TT1_PostcodeChanged = "UOTT4_To_TT1_PostcodeChanged";
    String sUOTT4_To_UOTT4_PostcodeChanged = "UOTT4_To_UOTT4_PostcodeChanged";
    String sUOTT4_To_TT4_PostcodeChanged = "UOTT4_To_TT4_PostcodeChanged";

    String sUO_To_LeftSHBEAtSomePoint = "UO_To_LeftSHBEAtSomePoint";
    String sUOTT1_To_LeftSHBE = "UOTT1_To_LeftSHBE";
    String sUOTT4_To_LeftSHBE = "UOTT4_To_LeftSHBE";
    String sUOTT3OrTT6_To_LeftSHBE = "UOTT3OrTT6_To_LeftSHBE";
    String sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE = "UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE";
    String sUOTT1_To_TT3OrTT6 = "UOTT1_To_TT3OrTT6";
    String sUOTT1_To_TT3OrTT6AtSomePoint = "UOTT1_To_TT3OrTT6AtSomePoint";
    String sUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999 = "UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999";
    String sUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint = "UOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint";
    String sUOTT4_To_TT3OrTT6 = "UOTT4_To_TT3OrTT6";
    String sUOTT4_To_TT3OrTT6AtSomePoint = "UOTT4_To_TT3OrTT6AtSomePoint";
    String sUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999 = "UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999";
    String sUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint = "UOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint";
    String sTT3OrTT6_To_UOTT1 = "TT3OrTT6_To_UOTT1";
    String sTT3OrTT6_To_UOTT4 = "TT3OrTT6_To_UOTT4";

    //String sUOTT1OrTT1_To_UOTT4 = "UOTT1OrTT1_To_UOTT4";
    String sUOTT1_To_UOTT4 = "UOTT1_To_UOTT4";
    String sTT1_To_UOTT4 = "TT1_To_UOTT4";
    String sTT1_To_UOTT4GettingDHP = "TT1_To_UOTT4GettingDHP";
    //String sUOTT4OrTT4_To_UOTT1 = "UOTT4OrTT4_To_UOTT1";
    //String sUOTT4OrTT4_To_UOTT1InArrears = "UOTT4OrTT4_To_UOTT1InArrears";
    //String sUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP = "UOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP";
    String sUOTT4_To_UOTT1 = "UOTT4_To_UOTT1";
    String sUOTT4_To_UOTT1InArrears = "UOTT4_To_UOTT1InArrears";
    String sUOTT4_To_UOTT1GettingDHP = "UOTT4_To_UOTT1GettingDHP";
    String sUOTT4_To_UOTT1InArrearsAndGettingDHP = "UOTT4_To_UOTT1InArrearsAndGettingDHP";
    String sTT4_To_UOTT1 = "TT4_To_UOTT1";
    String sTT4_To_UOTT1InArrears = "TT4_To_UOTT1InArrears";
    String sTT4_To_UOTT1GettingDHP = "TT4_To_UOTT1GettingDHP";
    String sTT4_To_UOTT1InArrearsAndGettingDHP = "TT4_To_UOTT1InArrearsAndGettingDHP";
    String sInArrearsAtSomePoint = "InArrearsAtSomePoint";
    String sDHPAtSomePoint = "DHPAtSomePoint";
    String sInArrearsAtSomePoint_And_DHPAtSomePoint = "InArrearsAtSomePoint_And_DHPAtSomePoint";

    String sTT1_To_UOTT1_PostcodeUnchanged = "TT1_To_UOTT1_PostcodeUnchanged";
    //String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1 = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1 = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4 = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4 = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8 = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9 = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months";
    String sTT4_To_UOTT4_PostcodeUnchanged = "TT4_To_UOTT4_PostcodeUnchanged";
    //String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1 = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1 = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4 = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4 = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OtTT7";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8 = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9 = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months";
    String sUOTT1_To_TT1_PostcodeUnchanged = "UOTT1_To_TT1_PostcodeUnchanged";
    //String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months";
    String sUOTT4_To_TT4_PostcodeUnchanged = "UOTT4_To_TT4_PostcodeUnchanged";
    //String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1 = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1 = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4 = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4 = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8 = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9 = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months";
    //String sUOTT1_To_TT1_PostcodeChanged = "UOTT1_To_TT1_PostcodeChanged";
    //String sUOTT1_To_UOTT1_PostcodeChanged = "UOTT1_To_UOTT1_PostcodeChanged";
    //String sUOTT4_To_TT4_PostcodeChanged = "UOTT4_To_TT4_PostcodeChanged";
    //String sUOTT4_To_UOTT4_PostcodeChanged = "UOTT4_To_UOTT4_PostcodeChanged";
    String sTT1_To_UOTT1_PostcodeChanged = "TT1_To_UOTT1_PostcodeChanged";
    String sTT4_To_UOTT4_PostcodeChanged = "TT4_To_UOTT4_PostcodeChanged";

    String sUO_To_LeftSHBE = "UO_To_LeftSHBE";
    String sUO_To_LeftSHBEBetweenOneAndTwoMonths = "UO_To_LeftSHBEBetweenOneAndTwoMonths";
    String sUO_To_LeftSHBEBetweenTwoAndThreeMonths = "UO_To_LeftSHBEBetweenTwoAndThreeMonths";
    String sUO_To_LeftSHBE_NotReturned = "UO_To_LeftSHBE_NotReturned";
    String sUOTT1_To_LeftSHBE_NotReturned = "UOTT1_To_LeftSHBE_NotReturned";
    String sUOTT4_To_LeftSHBE_NotReturned = "UOTT4_To_LeftSHBE_NotReturned";
    String sUOTT3OrTT6_To_LeftSHBE_NotReturned = "UOTT3OrTT6_To_LeftSHBE_NotReturned";
    String sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned = "UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned";
    String sUOTT1_To_LeftSHBE_ReturnedAsUOTT1 = "UOTT1_To_LeftSHBE_ReturnedAsUOTT1";
    String sUOTT1_To_LeftSHBE_ReturnedAsTT1 = "UOTT1_To_LeftSHBE_ReturnedAsTT1";
    String sUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6 = "UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6";
    String sUOTT1_To_LeftSHBE_ReturnedAsUOTT4 = "UOTT1_To_LeftSHBE_ReturnedAsUOTT4";
    String sUOTT1_To_LeftSHBE_ReturnedAsTT4 = "UOTT1_To_LeftSHBE_ReturnedAsTT4";
    String sUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7 = "UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7";
    String sUOTT1_To_LeftSHBE_ReturnedAsTT8 = "UOTT1_To_LeftSHBE_ReturnedAsTT8";
    String sUOTT1_To_LeftSHBE_ReturnedAsTT9 = "UOTT1_To_LeftSHBE_ReturnedAsTT9";
    String sUOTT4_To_LeftSHBE_ReturnedAsUOTT1 = "UOTT4_To_LeftSHBE_ReturnedAsUOTT1";
    String sUOTT4_To_LeftSHBE_ReturnedAsTT1 = "UOTT4_To_LeftSHBE_ReturnedAsTT1";
    String sUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6 = "UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6";
    String sUOTT4_To_LeftSHBE_ReturnedAsUOTT4 = "UOTT4_To_LeftSHBE_ReturnedAsUOTT4";
    String sUOTT4_To_LeftSHBE_ReturnedAsTT4 = "UOTT4_To_LeftSHBE_ReturnedAsTT4";
    String sUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7 = "UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7";
    String sUOTT4_To_LeftSHBE_ReturnedAsTT8 = "UOTT4_To_LeftSHBE_ReturnedAsTT8";
    String sUOTT4_To_LeftSHBE_ReturnedAsTT9 = "UOTT4_To_LeftSHBE_ReturnedAsTT9";
    String sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1 = "UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1";
    String sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1 = "UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1";
    String sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6 = "UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6";
    String sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4 = "UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4";
    String sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4 = "UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4";
    String sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7 = "UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7";
    String sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8 = "UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8";
    String sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9 = "UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9";
    String sUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint = "UOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint";
    String sUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint = "UOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint";
    String sUOTT1_To_TT3OrTT6BetweenOneAndTwoMonths = "UOTT1_To_TT3OrTT6BetweenOneAndTwoMonths";
    String sUOTT1_To_TT3OrTT6BetweenTwoAndThreeMonths = "UOTT1_To_TT3OrTT6BetweenTwoAndThreeMonths";
    String sUOTT4_To_TT3OrTT6BetweenOneAndTwoMonths = "UOTT4_To_TT3OrTT6BetweenOneAndTwoMonths";
    String sUOTT4_To_TT3OrTT6BetweenTwoAndThreeMonths = "UOTT4_To_TT3OrTT6BetweenTwoAndThreeMonths";
    String sTT3OrTT6_To_UOTT1AsNextTTIncludingBreaks = "TT3OrTT6_To_UOTT1AsNextTTIncludingBreaks";
    String sTT3OrTT6_To_UOTT4AsNextTTIncludingBreaks = "TT3OrTT6_To_UOTT4AsNextTTIncludingBreaks";

    String sTT1_To_UOTT1 = "TT1_To_UOTT1";
    String sTT1_To_UOTT1IncludingBreaks = "TT1_To_UOTT1IncludingBreaks";
    String sTT1_To_UOTT1_PostcodeChangedAfter1MonthButStillUOTT1 = "TT1_To_UOTT1_PostcodeChangedAfter1MonthButStillUOTT1";
    String sTT1_To_UOTT1_PostcodeChangedAfter2MonthsButStillUOTT1 = "TT1_To_UOTT1_PostcodeChangedAfter2MonthsButStillUOTT1";
    String sTT1_To_UOTT1_PostcodeChangedAfter3MonthsButStillUOTT1 = "TT1_To_UOTT1_PostcodeChangedAfter3MonthsButStillUOTT1";

    String sTT4_To_UOTT4 = "TT4_To_UOTT4";
    String sTT4_To_UOTT4IncludingBreaks = "TT4_To_UOTT4IncludingBreaks";
    String sTT4_To_UOTT4_PostcodeChangedAfter1MonthButStillUOTT4 = "TT4_To_UOTT4_PostcodeChangedAfter1MonthButStillUOTT4";
    String sTT4_To_UOTT4_PostcodeChangedAfter2MonthsButStillUOTT4 = "TT4_To_UOTT4_PostcodeChangedAfter2MonthsButStillUOTT4";
    String sTT4_To_UOTT4_PostcodeChangedAfter3MonthsButStillUOTT4 = "TT4_To_UOTT4_PostcodeChangedAfter3MonthsButStillUOTT4";

    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthButStillTT1 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthButStillTT1";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2MonthsButStillTT1 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2MonthsButStillTT1";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3MonthsButStillTT1 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3MonthsButStillTT1";

    // For Aggregate Statistics
    String sTotal_DHP = "Total_DHP";
    String sTotalCount_DHP = "TotalCount_DHP";
    String sTotal_HBLossDueToUO = "Total_HBLossDueToUO";
    String sTotalCount_HBLossDueToUO = "TotalCount_HBLossDueToUO";
    String sMax_Arrears = "Max_Arrears";
    String sTotalCount_Arrears = "TotalCount_Arrears";
    String sTotalCount_UnderOccupancy = "TotalCount_UnderOccupancy";

    // For General Statistics
    String sUOClaimsRecievingDHP = "UOClaimsRecievingDHP";
    String sUOTT1ClaimsInRentArrearsAtSomePoint = "UOTT1ClaimsInRentArrearsAtSomePoint";
    String sUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint = "UOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint";

    String sTotalCount_AlwaysUOTT1FromWhenStarted = "TotalCount_AlwaysUOTT1FromWhenStarted";
    String sTotalCount_AlwaysUOTT1FromStart = "TotalCount_AlwaysUOTT1FromStart";
    String sTotalCount_AlwaysUOTT1FromStartExceptWhenSuspended = "TotalCount_AlwaysUOTT1FromStartExceptWhenSuspended";

    String sTotalCount_AlwaysUOTT4FromWhenStarted = "TotalCount_AlwaysUOTT4FromWhenStarted";
    String sTotalCount_AlwaysUOTT4FromStart = "TotalCount_AlwaysUOTT4FromStart";
    String sTotalCount_AlwaysUOTT4FromStartExceptWhenSuspended = "TotalCount_AlwaysUOTT4FromStartExceptWhenSuspended";
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
    String sTotalCount_UniqueChildrenAgeLessThan10EffectedByUnderOccupancy = "TotalCount_UniqueChildrenAgeLessThan10EffectedByUnderOccupancy";

    String sAverageHouseholdSizeOfThoseUOTT1AlwaysFromStart = "AverageHouseholdSizeOfThoseUOTT1AlwaysFromStart";
    String sAverageHouseholdSizeOfThoseUOTT4AlwaysFromStart = "AverageHouseholdSizeOfThoseUOTT4AlwaysFromStart";

    HashMap<String, String> generalStatisticDescriptions;

    // TimeStatistics
    String sTotalCount_cumulativeUniqueClaims = "TotalCount_cumulativeUniqueClaims";
    String sTotalCount_UOClaims = "TotalCounts_UOClaims";
    String sTotalCount_UOCouncilClaims = "TotalCounts_UOCouncilClaims";
    String sTotalCount_UORSLClaims = "TotalCounts_UORSLClaims";
    String sTotalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE = "TotalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE";
    String sTotalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO = "TotalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO";
    String sTotalCount_UOClaimsCouncil = "TotalCount_UOClaimsCouncil";
    String sTotalCount_UOClaimsRSL = "TotalCount_UOClaimsRSL";

    String sTotalHouseholdSizeExcludingPartnersCouncilSHBE = "sTotalHouseholdSizeExcludingPartnersCouncilSHBE";
    String sTotalHouseholdSizeExcludingPartnersCouncilUO = "sTotalHouseholdSizeExcludingPartnersCouncilUO";
    String sTotalHouseholdSizeExcludingPartnersRSLSHBE = "sTotalHouseholdSizeExcludingPartnersRSLSHBE";
    String sTotalHouseholdSizeExcludingPartnersRSLUO = "sTotalHouseholdSizeExcludingPartnersRSLUO";
//    String sTotalAggregateHouseholdSize_UOClaims = "TotalAggregateHouseholdSize_UOClaims";
//    String sAverageHouseholdSize_UOClaims = "TotalAverageHouseholdSize_UOClaims";
//    String sTotalAggregateHouseholdSize_UOClaimsCouncil = "TotalAggregateHouseholdSize_UOClaimsCouncil";
//    String sTotalAverageHouseholdSize_UOClaimsCouncil = "TotalAverageHouseholdSize_UOClaimsCouncil";
//    String sTotalAggregateHouseholdSize_UOClaimsRSL = "TotalAggregateHouseholdSize_UOClaimsRSL";
//    String sTotalAverageHouseholdSize_UOClaimsRSL = "TotalAverageHouseholdSize_UOClaimsRSL";

    protected HashMap<String, String> getGeneralStatisticDescriptions(
            String startMonth,
            String startYear,
            String endMonth,
            String endYear) {
        String dates;
        dates = startMonth + " " + startYear + " to " + endMonth + " " + endYear;
        generalStatisticDescriptions = new HashMap<String, String>();
        generalStatisticDescriptions.put(
                sTotalCount_AlwaysUOTT1FromStart,
                "Total count of claims that were always TT1 and in the "
                + "under-occupancy from " + dates
                + " (includes claims that were not in under-occupancy "
                + "for months when their Housing Benefit Claim Status was In "
                + "Suspension).");
        generalStatisticDescriptions.put(
                sTotalCount_AlwaysUOTT1FromWhenStarted,
                "Total count of claims that were always TT1 and in the "
                + "under-occupancy data from " + dates + " but which only became "
                + "claims after " + startMonth + " " + startYear
                + " (includes claims that were not in under-occupancy "
                + "for months when their Housing Benefit Claim Status was In "
                + "Suspension).");
        generalStatisticDescriptions.put(
                sTotalCount_AlwaysUOTT4FromStart,
                "Total count of claims that were always TT4 and in the "
                + "under-occupancy from " + dates
                + " (includes claims that were not in under-occupancy "
                + "for months when their Housing Benefit Claim Status was In "
                + "Suspension).");
        generalStatisticDescriptions.put(
                sTotalCount_AlwaysUOTT4FromWhenStarted,
                "Total count of claims that were always TT4 and in the "
                + "under-occupancy data from " + dates + " but which only became "
                + "claims after " + startMonth + " " + startYear
                + " (includes claims that were not in under-occupancy "
                + "for months when their Housing Benefit Claim Status was In "
                + "Suspension).");
        generalStatisticDescriptions.put(
                sAverageHouseholdSizeOfThoseUOTT1AlwaysFromStart,
                "Average Household Size of claims that were always TT1 in under-"
                + "occupancy from " + dates + " in " + endMonth + " "
                + endYear + ".");
        generalStatisticDescriptions.put(
                sAverageHouseholdSizeOfThoseUOTT4AlwaysFromStart,
                "Average Household Size of claims that were always TT4 in under-"
                + "occupancy from " + dates + " in " + endMonth + " "
                + endYear + ".");
        generalStatisticDescriptions.put(
                sTotalCount_ClaimsEffectedByUnderOccupancy,
                "Total count of claims that were effected by under-occupancy "
                + "(claims can have different Tenancy Types at different times).");
        generalStatisticDescriptions.put(
                sTotalCount_UniqueIndividualsEffectedByUnderOccupancy,
                "Total count of unique individuals effected by under-occupancy "
                + "(total count unique claimants + total count unique partners + "
                + "total count of unique dependents + total count of unique non-dependents) : "
                + "(uniqueness is based on date of birth and NINo) this should distinguish twins, "
                + "but it does not deal with claims with multiple partners.");
        // Council
        generalStatisticDescriptions.put(
                sTotalCount_CouncilClaimsEffectedByUnderOccupancy,
                "Total count of claims effected by under-occupancy at some time "
                + "between " + dates + " and in Council tenancies.");
        generalStatisticDescriptions.put(
                sTotalCount_CouncilUniqueIndividualsEffectedByUnderOccupancy,
                "Total count of unique individuals effected by under-occupancy "
                + "and in Council tenancies.");
        generalStatisticDescriptions.put(
                sTotalCount_CouncilUniqueClaimantsEffectedByUnderOccupancy,
                "Total count of unique claimants effected by under-occupancy "
                + "and in Council tenancies.");
        generalStatisticDescriptions.put(
                sTotalCount_CouncilUniquePartnersEffectedByUnderOccupancy,
                "Total count of unique partners effected by under-occupancy "
                + "and in Council tenancies (this only deals with main "
                + "partners, not for all partners in claims where there are "
                + "multiple partners).");
        generalStatisticDescriptions.put(
                sTotalCount_CouncilDependentsEffectedByUnderOccupancy,
                "Total count of unique Dependents effected by under-occupancy "
                + "in Council tenancies.");
        generalStatisticDescriptions.put(
                sTotalCount_CouncilUniqueNonDependentsEffectedByUnderOccupancy,
                "Total count of unique NonDependents effected by under-"
                + "occupancy in Council tenancies.");
        // RSL
        generalStatisticDescriptions.put(
                sTotalCount_RSLClaimsEffectedByUnderOccupancy,
                "Total count of claims effected by under-occupancy at  "
                + "some time between " + dates + " and in Registered Social "
                + "Landlord tenancies.");
        generalStatisticDescriptions.put(
                sTotalCount_RSLUniqueIndividualsEffectedByUnderOccupancy,
                "Total count of unique individuals effected by under-occupancy "
                + "and in Registered Social Landlord tenancies.");
        generalStatisticDescriptions.put(
                sTotalCount_RSLUniqueClaimantsEffectedByUnderOccupancy,
                "Total count of unique claimants effected by under-occupancy "
                + "and in Registered Social Landlord tenancies.");
        generalStatisticDescriptions.put(
                sTotalCount_RSLUniquePartnersEffectedByUnderOccupancy,
                "Total count of unique partners effected by under-occupancy "
                + "and in Registered Social Landlord tenancies (this only "
                + "deals with main partners, not for all partners in claims "
                + "where there are multiple partners).");
        generalStatisticDescriptions.put(
                sTotalCount_RSLDependentsEffectedByUnderOccupancy,
                "Total count of unique Dependents effected by under-occupancy "
                + "in Registered Social Landlord tenancies.");
        generalStatisticDescriptions.put(
                sTotalCount_RSLUniqueNonDependentsEffectedByUnderOccupancy,
                "Total count of unique NonDependents effected by under-"
                + "occupancy in Registered Social Landlord tenancies.");
        generalStatisticDescriptions.put(
                sTotalCount_UniqueChildrenAgeLessThan10EffectedByUnderOccupancy,
                "Total count of unique children under the Age of 10 effected "
                + "by under-occupancy (the number may be higher as a result "
                + "of twins or if dates of birth not being recorded in "
                + "multiple cases).");

        generalStatisticDescriptions.put(
                sSubsequentlyEffectedUOStillUOInOctober2015,
                "Total count of under-occupancy claims that were not "
                + "under-occupancy claims in April 2013, but were "
                + "still under-occupancy claims in October 2015.");

        generalStatisticDescriptions.put(
                sPercentageReceivingDHPInOctober2015,
                "Percentage of under-occupancy claims receiving a weekly "
                + "discretionary Payment for Housing Benefit in October 2015.");
        generalStatisticDescriptions.put(
                sPercentageInArrearsOctober2015,
                "Percentage of Council under-occupancy claims in arrears in "
                + "October 2015.");
        generalStatisticDescriptions.put(
                sPercentageInArrearsAndReceivingDHPInOctober2015,
                "Percentage of Council under-occupancy claims in arrears and "
                + "receiving a weekly discretionary Payment for Housing "
                + "Benefit in October 2015.");

        generalStatisticDescriptions.put(sPercentageReceivingDHPInOctober2015ThatWereUOInApril2013,
                "Percentage of under-occupancy claims (that were under-"
                + "occupancy claims in April 2008) receiving a weekly "
                + "discretionary Payment for Housing Benefit in October 2015.");
        generalStatisticDescriptions.put(sPercentageInArrearsOctober2015ThatWereUOInApril2013,
                "Percentage of Council under-occupancy claims (that were under-"
                + "occupancy claims in April 2008) in arrears in October 2015.");
        generalStatisticDescriptions.put(sPercentageInArrearsAndReceivingDHPInOctober2015ThatWereUOInApril2013,
                "Percentage of Council under-occupancy claims (that were under-"
                + "occupancy claims in April 2008) in arrears and receiving a "
                + "weekly discretionary Payment for Housing Benefit in October "
                + "2015.");

        // LeftSHBE
        generalStatisticDescriptions.put(
                sUO_To_LeftSHBEAtSomePoint,
                "Total count of under-occupancy claims that have left SHBE at "
                + "some time between " + dates + " after becoming under-"
                + "occupied.");
        generalStatisticDescriptions.put(
                sUO_To_LeftSHBE,
                "Total count of UO_To_LeftSHBE claims.");
        generalStatisticDescriptions.put(
                sUO_To_LeftSHBEBetweenOneAndTwoMonths,
                "Total count of UO_To_LeftSHBE (not the next month, but the "
                + "month after) claims.");
        generalStatisticDescriptions.put(
                sUO_To_LeftSHBEBetweenTwoAndThreeMonths,
                "Total count of UO_To_LeftSHBE (not the next month, or the "
                + "month after, but the month after that) claims.");
        generalStatisticDescriptions.put(
                sUO_To_LeftSHBE_NotReturned,
                "Total count of UO_To_LeftSHBE and not returned claims "
                + "(this may be bias and much of the count might be for "
                + "those leaving in the last months in the collection "
                + "and that may in fact return at a later date).");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_NotReturned,
                "Total count of UOTT1_To_LeftSHBE and not returned claims "
                + "(again this may be bias and much of the count might "
                + "be for those leaving in the last months in the "
                + "collection and that may in fact return at a later date).");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_NotReturned,
                "Total count of UOTT4_To_LeftSHBE and not returned claims "
                + "(again this may be bias and much of the count might "
                + "be for those leaving in the last months in the "
                + "collection and that may in fact return at a later date).");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_NotReturned,
                "Total count of UOTT3OrTT6_To_LeftSHBE and not returned claims "
                + "(again this may be bias and much of the count might "
                + "be for those leaving in the last months in the "
                + "collection and that may in fact return at a later date).");
        generalStatisticDescriptions.put(
                sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned,
                "Total count of UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE and not "
                + "returned claims "
                + "(again this may be bias and much of the count might "
                + "be for those leaving in the last months in the "
                + "collection and that may in fact return at a later date).");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAsUOTT1,
                "Total count of UOTT1_To_LeftSHBE claims that next returned "
                + "to the SHBE as UOTT1 "
                + "(unique claims: if this happens more than once for "
                + "the same claim, it is just counted once).");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT1,
                "Total count of UOTT1_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT1 "
                + "(unique claims: if this happens more than once for "
                + "the same claim, it is just counted once).");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
                "Total count of UOTT1_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT3OrTT6.");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAsUOTT4,
                "Total count of UOTT1_To_LeftSHBE claims that next returned "
                + "to the SHBE as UOTT4.");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT4,
                "Total count of UOTT1_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT4.");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
                "Total count of UOTT1_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT5OrTT7.");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT8,
                "Total count of UOTT1_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT8.");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT9,
                "Total count of UOTT1_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT9.");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAsUOTT1,
                "Total count of UOTT4_To_LeftSHBE claims that next returned "
                + "to the SHBE as UOTT1.");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT1,
                "Total count of UOTT4_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT1.");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6,
                "Total count of UOTT4_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT3OrTT6.");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAsUOTT4,
                "Total count of UOTT4_To_LeftSHBE claims that next returned "
                + "to the SHBE as UOTT4.");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT4,
                "Total count of UOTT4_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT4.");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7,
                "Total count of UOTT4_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT5OrTT7.");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT8,
                "Total count of UOTT4_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT8.");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT9,
                "Total count of UOTT4_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT9.");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1,
                "Total count of UOTT3OrTT6_To_LeftSHBE claims that next "
                + "returned to the SHBE as UOTT1.");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1,
                "Total count of UOTT3OrTT6_To_LeftSHBE claims that next "
                + "returned to the SHBE as TT1.");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6,
                "Total count of UOTT3OrTT6_To_LeftSHBE claims that next "
                + "returned to the SHBE as TT3OrTT6.");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4,
                "Total count of UOTT3OrTT6_To_LeftSHBE claims that next "
                + "returned to the SHBE as UOTT4.");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4,
                "Total count of UOTT3OrTT6_To_LeftSHBE claims that next "
                + "returned to the SHBE as TT4.");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7,
                "Total count of UOTT3OrTT6_To_LeftSHBE claims that next "
                + "returned to the SHBE as TT5OrTT7.");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8,
                "Total count of UOTT3OrTT6_To_LeftSHBE claims that next "
                + "returned to the SHBE as TT8.");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9,
                "Total count of UOTT3OrTT6_To_LeftSHBE claims that next "
                + "returned to the SHBE as TT9.");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                "Total count of UOTT1_To_LeftSHBE claims that returned and "
                + "became under-occupancy again at some time between "
                + dates + ".");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                "Total count of UOTT4_To_LeftSHBE claims that returned and "
                + "became under-occupancy again at  some time between "
                + dates + ".");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE,
                "Total count of UOTT1_To_LeftSHBE claims.");
//        generalStatisticDescriptions.put(
//                sUOTT1_To_LeftSHBEReturnedAsTT1orTT4,
//                "UOTT1_To_LeftSHBEReturnedAsTT1orTT4");
//        generalStatisticDescriptions.put(
//                sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
//                "UOTT1_To_LeftSHBEReturnedAsTT3OrTT6");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE,
                "Total count of UOTT4_To_LeftSHBE claims.");
//        generalStatisticDescriptions.put(
//                sUOTT4_To_LeftSHBEReturnedAsTT1orTT4,
//                "UOTT4_To_LeftSHBEReturnedAsTT1orTT4");
//        generalStatisticDescriptions.put(
//                sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
//                "UOTT4_To_LeftSHBEReturnedAsTT3OrTT6");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE,
                "Total count of UOTT3OrTT6_To_LeftSHBE claims.");
        generalStatisticDescriptions.put(
                sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE,
                "Total count of NotTT1OrTT3OrTT4OrTT6_To_LeftSHBE claims.");

        generalStatisticDescriptions.put(
                sUO_NotUO_UO,
                "Total count of sUO_NotUO_UO claims.");
        generalStatisticDescriptions.put(
                sUO_NotUO_UO_NotUO_UO,
                "Total count of sUO_NotUO_UO_NotUO_UO claims.");
        generalStatisticDescriptions.put(
                sUO_NotUO_UO_NotUO_UO_NotUO_UO,
                "Total count of sUO_NotUO_UO_NotUO_UO_NotUO_UO claims.");
        generalStatisticDescriptions.put(
                sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO,
                "Total count of sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO claims "
                + "(including claims with even more _NotUO_UO occurrences).");

        generalStatisticDescriptions.put(
                sUOTT1_To_NotUO_InSHBE_PostcodeChanged,
                "Total count of UOTT1_To_NotUO_InSHBE_PostcodeChanged claims "
                + "(only postcode changes where both the origin and "
                + "destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeChanged,
                "Total count of UOTT1_To_TT1_PostcodeChanged claims "
                + "(only postcode changes where both the origin and "
                + "destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT4_PostcodeChanged,
                "Total count of UOTT1_To_TT4_PostcodeChanged claims "
                + "(only postcode changes where both the origin and "
                + "destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sUOTT4_To_NotUO_InSHBE_PostcodeChanged,
                "Total count of UOTT4_To_NotUO_InSHBE_PostcodeChanged claims "
                + "(only postcode changes where both the origin and "
                + "destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT1_PostcodeChanged,
                "Total count of UOTT4_To_TT1_PostcodeChanged claims "
                + "(only postcode changes where both the origin and "
                + "destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeChanged,
                "Total count of UOTT4_To_TT4_PostcodeChanged claims "
                + "(only postcode changes where both the origin and "
                + "destination postcodes validate are included).");

        // UOTT1_To_TT3OrTT6
        generalStatisticDescriptions.put(
                sUOTT1_To_TT3OrTT6,
                "Total count of UOTT1_To_TT3OrTT6 claims.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT3OrTT6BetweenOneAndTwoMonths,
                "Total count of UOTT1_To_TT3OrTT6BetweenOneAndTwoMonths claims.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT3OrTT6BetweenTwoAndThreeMonths,
                "Total count of UOTT1_To_TT3OrTT6BetweenTwoAndThreeMonths claims.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT3OrTT6AtSomePoint,
                "Total count of under-occupied TT1 claims that became TT3OrTT6 at  "
                + "some time between " + dates + ".");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                "Total count of under-occupied TT1 claims that became TT3OrTT6 "
                + "in the next TT change ignoring those times they "
                + "came out of the SHBE if indeed they have.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint,
                "Total count of under-occupied TT1 claims that became TT3OrTT6 "
                + "in the next TT change ignoring those times they "
                + "came out of the SHBE if indeed they have and which returned "
                + "at some later point to be either TT1 or UOTT1.");

        // UOTT4_To_TT3OrTT6
        generalStatisticDescriptions.put(
                sUOTT4_To_TT3OrTT6,
                "Total count of UOTT4_To_TT3OrTT6 claims.");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT3OrTT6BetweenOneAndTwoMonths,
                "Total count of UOTT4_To_TT3OrTT6BetweenOneAndTwoMonths claims.");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT3OrTT6BetweenTwoAndThreeMonths,
                "Total count of UOTT4_To_TT3OrTT6BetweenTwoAndThreeMonths claims.");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT3OrTT6AtSomePoint,
                "Total count of under-occupied TT4 claims that became TT3OrTT6 at "
                + "some time between " + dates + ".");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                "Total count of under-occupied TT4 claims that became TT3OrTT6 "
                + "in the next TT change ignoring those times they "
                + "came out of the SHBE if indeed they have.");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint,
                "Total count of under-occupied TT4 claims that became TT3OrTT6 "
                + "in the next TT change ignoring those times they "
                + "came out of the SHBE if indeed they have and which returned "
                + "at some later point to be either TT4 or UOTT4.");

        // TT3OrTT6_To_UOTT1
        generalStatisticDescriptions.put(
                sTT3OrTT6_To_UOTT1,
                "Total count of TT3OrTT6_To_UOTT1 claims.");
        generalStatisticDescriptions.put(
                sTT3OrTT6_To_UOTT1AsNextTTIncludingBreaks,
                "Total count of TT3OrTT6_To_UOTT1AsNextTTIncludingBreaks claims.");
        // TT3OrTT6_To_UOTT4        
        generalStatisticDescriptions.put(
                sTT3OrTT6_To_UOTT4,
                "Total count of TT3OrTT6_To_UOTT4 claims.");
        generalStatisticDescriptions.put(
                sTT3OrTT6_To_UOTT4AsNextTTIncludingBreaks,
                "Total count of TT3OrTT6_To_UOTT4AsNextTTIncludingBreaks claims.");
        // UOTT1_To_TT1
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchanged,
                "Total count of UOTT1_To_TT1_PostcodeUnchanged claims.");
//        generalStatisticDescriptions.put(
//                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
//                "Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month claims.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                "Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1 claims.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                "Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1 claims.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                "Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 claims.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                "Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4 claims.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                "Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4 claims.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                "Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 claims.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                "Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8 claims.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                "Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9 claims.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
                "Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months claims.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months,
                "Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months claims.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthButStillTT1,
                "Total count of UOTT1_To_TT1_PostcodeChangedAfter1MonthButStillTT1 separate from -999 claims.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2MonthsButStillTT1,
                "Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2MonthsButStillTT1 separate from -999 claims.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3MonthsButStillTT1,
                "Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3MonthsButStillTT1 separate from -999 claims.");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeChanged,
                "Total count of UOTT1_To_TT1_PostcodeChanged claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        // UOTT4_To_TT4
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchanged,
                "Total count of UOTT4_To_TT4_PostcodeUnchanged claims.");
//        generalStatisticDescriptions.put(
//                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
//                "Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month claims.");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                "Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1 claims.");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                "Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1 claims.");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                "Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 claims.");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                "Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4 claims.");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                "Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4 claims.");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                "Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 claims.");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                "Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8 claims.");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                "Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9 claims.");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
                "Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months claims.");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months,
                "Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months claims.");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeChanged,
                "Total count of UOTT4_To_TT4_PostcodeChanged claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        // TT1_To_UOTT1        
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1,
                "Total count of TT1_To_UOTT1 claims.");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1IncludingBreaks,
                "Total count of TT1_To_UOTT1 including breaks claims.");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchanged,
                "Total count of TT1_To_UOTT1_PostcodeUnchanged claims.");
//        generalStatisticDescriptions.put(
//                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
//                "Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month claims "
//                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                "Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                "Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                "Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                "Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                "Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                "Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                "Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                "Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
                "Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months,
                "Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(sTT1_To_UOTT1_PostcodeChangedAfter1MonthButStillUOTT1,
                "Total count of TT1_To_UOTT1_PostcodeChangedAfter1MonthButStillTT1UO claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(sTT1_To_UOTT1_PostcodeChangedAfter2MonthsButStillUOTT1,
                "Total count of TT1_To_UOTT1_PostcodeChangedAfter2MonthsButStillTT1UO claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(sTT1_To_UOTT1_PostcodeChangedAfter3MonthsButStillUOTT1,
                "Total count of TT1_To_UOTT1_PostcodeChangedAfter3MonthsButStillTT1UO claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeChanged,
                "Total count of TT1_To_UOTT1_PostcodeChanged claims "
                + "(includes corrections of postcodes) "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");

        generalStatisticDescriptions.put(
                sUOTT1_To_UOTT1_PostcodeChanged,
                "Total count of UOTT1_To_UOTT1_PostcodeChanged claims "
                + "(includes corrections of postcodes) "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");

        // TT1_To_UOTT4
        //generalStatisticDescriptions.put(sUOTT1OrTT1_To_UOTT4,
        //        "Total count of UOTT1OrTT1_To_UOTT4 claims.");
        generalStatisticDescriptions.put(sUOTT1_To_UOTT4,
                "Total count of UOTT1_To_UOTT4 claims.");
        generalStatisticDescriptions.put(sTT1_To_UOTT4,
                "Total count of TT1_To_UOTT4 claims.");
        generalStatisticDescriptions.put(sTT1_To_UOTT4GettingDHP,
                "Total count of TT1_To_UOTT4 claims receiving "
                + "Housing Benefit Discretionary Payment "
                + "some time between " + dates + ".");

        // TT4_To_UOTT4
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4,
                "Total count of TT4_To_UOTT4 claims.");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4IncludingBreaks,
                "Total count of TT4_To_UOTT4 including breaks claims.");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchanged,
                "Total count of TT4_To_UOTT4_PostcodeUnchanged claims.");
//        generalStatisticDescriptions.put(
//                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
//                "Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month claims "
//                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                "Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                "Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                "Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                "Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                "Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                "Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
                "Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months,
                "Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeChangedAfter1MonthButStillUOTT4,
                "Total count of TT4_To_UOTT4_PostcodeChangedAfter1MonthButStillUOTT4 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeChangedAfter2MonthsButStillUOTT4,
                "Total count of TT4_To_UOTT4_PostcodeChangedAfter2MonthsButStillUOTT4 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeChangedAfter3MonthsButStillUOTT4,
                "Total count of TT4_To_UOTT4_PostcodeChangedAfter3MonthsButStillUOTT4 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeChanged,
                "Total count of TT4_To_UOTT4_PostcodeChanged claims "
                + "(includes corrections of postcodes) "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");

        generalStatisticDescriptions.put(
                sUOTT4_To_UOTT4_PostcodeChanged,
                "Total count of UOTT4_To_UOTT4_PostcodeChanged claims "
                + "(includes corrections of postcodes) "
                + "(only postcode changes where both the origin and destination postcodes validate are included).");

        // TT4_To_UOTT1
        //generalStatisticDescriptions.put(sUOTT4OrTT4_To_UOTT1,
        //        "Total count of UOTT4OrTT4_To_UOTT1 claims.");
        //generalStatisticDescriptions.put(sUOTT4OrTT4_To_UOTT1InArrears,
        //        "Total count of UOTT4OrTT4_To_UOTT1 claims in arrears at  "
        //        + "some time between " + dates + ".");
        //generalStatisticDescriptions.put(sUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
        //        "Total count of UOTT4OrTT4_To_UOTT1 claims simultaneously in "
        //        + "arrears and receiving Housing Benefit Discretionary "
        //        + "Payment at  "
        //        + "some time between " + dates + ".");
        generalStatisticDescriptions.put(sUOTT4_To_UOTT1,
                "Total count of UOTT4_To_UOTT1 claims.");
        generalStatisticDescriptions.put(sUOTT4_To_UOTT1InArrears,
                "Total count of UOTT4_To_UOTT1 claims in arrears "
                + "some time between " + dates + ".");
        generalStatisticDescriptions.put(sUOTT4_To_UOTT1GettingDHP,
                "Total count of UOTT4_To_UOTT1 claims receiving "
                + "Housing Benefit Discretionary Payment "
                + "some time between " + dates + ".");
        generalStatisticDescriptions.put(sUOTT4_To_UOTT1InArrearsAndGettingDHP,
                "Total count of UOTT4_To_UOTT1 claims simultaneously in "
                + "arrears and receiving Housing Benefit Discretionary "
                + "Payment "
                + "some time between " + dates + ".");
        generalStatisticDescriptions.put(sTT4_To_UOTT1,
                "Total count of TT4_To_UOTT1 claims.");
        generalStatisticDescriptions.put(sTT4_To_UOTT1InArrears,
                "Total count of UOTT4OrTT4_To_UOTT1 claims in arrears  "
                + "some time between " + dates + ".");
        generalStatisticDescriptions.put(sTT4_To_UOTT1GettingDHP,
                "Total count of TT4_To_UOTT1 claims receiving "
                + "Housing Benefit Discretionary Payment "
                + "some time between " + dates + ".");
        generalStatisticDescriptions.put(sTT4_To_UOTT1InArrearsAndGettingDHP,
                "Total count of UOTT4OrTT4_To_UOTT1 claims simultaneously in "
                + "arrears and receiving Housing Benefit Discretionary "
                + "Payment "
                + "some time between " + dates + ".");
        generalStatisticDescriptions.put(sInArrearsAtSomePoint_And_DHPAtSomePoint,
                "Claims in arrears at some time between " + dates + " and also "
                + "receiving a Housing Benefit Discretionary Payment "
                + "some time between " + dates + ".");
        generalStatisticDescriptions.put(
                sUOClaimsRecievingDHP,
                "Total count of claims receiving Housing Benefit Discretionary "
                + "Payment some time between " + dates + ".");
        generalStatisticDescriptions.put(
                sUOTT1ClaimsInRentArrearsAtSomePoint,
                "Total count of under-occupied TT1 claims in rent arrears at "
                + "some time between " + dates + ".");
        generalStatisticDescriptions.put(
                sUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint,
                "Total count of under-occupied TT1 claims in rent arrears and "
                + "receiving DHP simultaneously some time between " + dates + ".");
        String sPermanantlyLeftUOButRemainedInSHBE = "PermanantlyLeftUOButRemainedInSHBE";
        String sPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged = "PermanantlyLeftUOButRemainedInSHBE_PostcodeChanged";
        String sPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased = "PermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased";
        generalStatisticDescriptions.put(
                sPermanantlyLeftUOButRemainedInSHBE,
                "Total count of under-occupied claims that stopped being "
                + "under-occupied and "
                + " which have not returned to under-occupancy.");
        generalStatisticDescriptions.put(
                sPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged,
                "Total count of under-occupied claims that stopped being "
                + "under-occupied and changed postcode and "
                + " which have not returned to under-occupancy.");
        generalStatisticDescriptions.put(
                sPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased,
                "Total count of under-occupied claims that stopped being "
                + "under-occupied because of an increase in household size and"
                + " which have not returned to under-occupancy.");
        return generalStatisticDescriptions;
    }

    private void initString() {
        sTT_ = sTT + DW_Strings.sUnderscore;
    
        sTravellers = "a_Travellers"; // Letter_ added for ordering purposes.
        sTTNot1Or4AndUnderOccupying = "b_TTNot1Or4AndUnderOccupying";

        sTT1_To_TT3OrTT6 = "e_TT1_To_TT3OrTT6";
        sTT4_To_TT3OrTT6 = "f_TT4_To_TT3OrTT6";
        sTT3OrTT6_To_TT1 = "g_TT3OrTT6_To_TT1";
        sTT3OrTT6_To_TT4 = "h_TT3OrTT6_To_TT4";

        sAlwaysUOFromStart__NoValidPostcodeChange_NotChangedTT = "k_AlwaysUOFromStart__NoValidPostcodeChange_NotChangedTT";
        sAlwaysUOFromStart__ChangedTT = "l_AlwaysUOFromStart__ChangedTT";
        sAlwaysUOFromStart__ValidPostcodeChange_NotChangedTT = "m_AlwaysUOFromStart__ValidPostcodeChange_NotChangedTT";

        sAlwaysUOFromWhenStarted__NoValidPostcodeChange_NotChangedTT = "p_AlwaysUOFromWhenStarted__NoValidPostcodeChange_NotChangedTT";
        sAlwaysUOFromWhenStarted__ChangedTT = "q_AlwaysUOFromWhenStarted__ChangedTT";
        sAlwaysUOFromWhenStarted__ValidPostcodeChange_NotChangedTT = "r_AlwaysUOFromWhenStarted__ValidPostcodeChange_NotChangedTT";

        sIntermitantUO__NoValidPostcodeChange_NotChangedTT = "u_" + "IntermitantUO__NoValidPostcodeChange_NotChangedTT";
        sIntermitantUO__ChangedTT = "v_" + "IntermitantUO__ChangedTT";
        sIntermitantUO__ValidPostcodeChange_NotChangedTT = "w_" + "IntermitantUO__ValidPostcodeChange_NotChangedTT";
    }

    public TenancyChangesUO(
            DW_Environment env,
            String aPT,
            HashMap<String, DW_ID> tPostcodeToPostcodeIDLookup,
            boolean handleOutOfMemoryError) {
        this.env = env;
        this.DW_Strings = env.getDW_Strings();
        this.DW_SHBE_CollectionHandler = env.getDW_SHBE_CollectionHandler(aPT);
        this.DW_SHBE_Handler = env.getDW_SHBE_Handler();
        this.DW_SHBE_TenancyType_Handler = env.getDW_SHBE_TenancyType_Handler();
        this.handleOutOfMemoryError = handleOutOfMemoryError;
        this.tPostcodeToPostcodeIDLookup = tPostcodeToPostcodeIDLookup;
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
            result.put(aCTBRef + DW_Strings.sUnderscore + sTT, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sUnderOccupancy, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sP, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sWHBE, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sWERA, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sPSI, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sSHBC, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sRTHBCC, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sCEG, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sHS, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sND, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sCD, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sNDUO, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sCO16, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sFCU10, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sMCU10, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sFC10To16, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sMC10To16, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sBR, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sNB, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sCDoB, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sCA, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sPDoB, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sPA, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sCG, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sPG, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sDisability, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sDisabilityPremium, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sDisabilitySevere, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sDisabilityEnhanced, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sDisabledChild, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sPDeath, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sHBDP, s);
            result.put(aCTBRef + DW_Strings.sUnderscore + sA, s);
        }
        Iterator<Integer> tNotMonthlyUOIte;
        DW_SHBE_Collection tSHBEData;
        String paymentType;
        paymentType = env.getDW_Strings().sPaymentTypeAll;
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
            tSHBEData = new DW_SHBE_Collection(env, SHBEFilenames[i], paymentType);
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
                    key = aCTBRef + DW_Strings.sUnderscore + sTT;
                    aS = result.get(key);
                    j = dRecord.getTenancyType();
                    aS += DW_Strings.sCommaSpace + sTT_ + j;
                    result.put(key, aS);
                    // Under Occupancy
                    key = aCTBRef + DW_Strings.sUnderscore + sUnderOccupancy;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Postcode
                    key = aCTBRef + DW_Strings.sUnderscore + sP;
                    aS = result.get(key);
                    bS = dRecord.getClaimantsPostcode();
                    aS += DW_Strings.sCommaSpace + bS;
                    result.put(key, aS);
                    // Weekly Housing Benefit Entitlement
                    key = aCTBRef + DW_Strings.sUnderscore + sWHBE;
                    aS = result.get(key);
                    j = dRecord.getWeeklyHousingBenefitEntitlement();
                    aS += DW_Strings.sCommaSpace + decimalise(j);
                    result.put(key, aS);
                    // Weekly Eligible Rent Amount
                    key = aCTBRef + DW_Strings.sUnderscore + sWERA;
                    aS = result.get(key);
                    j = dRecord.getWeeklyEligibleRentAmount();
                    aS += DW_Strings.sCommaSpace + decimalise(j);
                    result.put(key, aS);
                    // PassportedStandardIndicator
                    key = aCTBRef + DW_Strings.sUnderscore + sPSI;
                    aS = result.get(key);
                    j = dRecord.getPassportedStandardIndicator();
                    aS += DW_Strings.sCommaSpace + j;
                    result.put(key, aS);
                    // StatusOfHBClaim
                    key = aCTBRef + DW_Strings.sUnderscore + sSHBC;
                    aS = result.get(key);
                    j = dRecord.getStatusOfHBClaimAtExtractDate();
                    aS += DW_Strings.sCommaSpace + j;
                    result.put(key, aS);
                    // ReasonThatHBClaimClosed
                    key = aCTBRef + DW_Strings.sUnderscore + sRTHBCC;
                    aS = result.get(key);
                    j = dRecord.getReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective();
                    if (j == 0) {
                        aS += DW_Strings.sCommaSpace;
                    } else {
                        aS += DW_Strings.sCommaSpace + j;
                    }
                    result.put(key, aS);
                    // ClaimantEthnicGroup
                    key = aCTBRef + DW_Strings.sUnderscore + sCEG;
                    aS = result.get(key);
                    //j = dRecord.getClaimantsEthnicGroup();
                    j = DW_SHBE_Handler.getEthnicityGroup(dRecord);
                    aS += DW_Strings.sCommaSpace + j;
                    result.put(key, aS);
                    // Household Size
                    key = aCTBRef + DW_Strings.sUnderscore + sHS;
                    aS = result.get(key);
                    j = (int) DW_SHBE_Handler.getHouseholdSize(dRecord);
                    aS += DW_Strings.sCommaSpace + j;
                    result.put(key, aS);
                    // NonDependents
                    key = aCTBRef + DW_Strings.sUnderscore + sND;
                    aS = result.get(key);
                    j = dRecord.getNumberOfNonDependents();
                    if (j == 0) {
                        aS += DW_Strings.sCommaSpace;
                    } else {
                        aS += DW_Strings.sCommaSpace + j;
                    }
                    result.put(key, aS);
                    // Child Dependents
                    key = aCTBRef + DW_Strings.sUnderscore + sCD;
                    aS = result.get(key);
                    j = dRecord.getNumberOfChildDependents();
                    if (j == 0) {
                        aS += DW_Strings.sCommaSpace;
                    } else {
                        aS += DW_Strings.sCommaSpace + j;
                    }
                    result.put(key, aS);
                    // NonDependents (UO)
                    key = aCTBRef + DW_Strings.sUnderscore + sNDUO;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // ChildrenOver16
                    key = aCTBRef + DW_Strings.sUnderscore + sCO16;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // FemaleChildrenUnder10";
                    key = aCTBRef + DW_Strings.sUnderscore + sFCU10;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // MaleChildrenUnder10";
                    key = aCTBRef + DW_Strings.sUnderscore + sMCU10;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // FemaleChildren10to16";
                    key = aCTBRef + DW_Strings.sUnderscore + sFC10To16;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // MaleChildren10to16
                    key = aCTBRef + DW_Strings.sUnderscore + sMC10To16;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Number of Bedrooms
                    key = aCTBRef + DW_Strings.sUnderscore + sNB;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Bedroom Requirement
                    key = aCTBRef + DW_Strings.sUnderscore + sBR;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Claimants Date Of Birth
                    key = aCTBRef + DW_Strings.sUnderscore + sCDoB;
                    aS = result.get(key);
                    bS = dRecord.getClaimantsDateOfBirth();
                    aS += DW_Strings.sCommaSpace + bS;
                    result.put(key, aS);
                    // ClaimantsAge
                    key = aCTBRef + DW_Strings.sUnderscore + sCA;
                    aS = result.get(key);
                    bS = DW_SHBE_Handler.getClaimantsAge(year, month, dRecord);
                    aS += DW_Strings.sCommaSpace + bS;
                    result.put(key, aS);
                    // Partners Date Of Birth
                    key = aCTBRef + DW_Strings.sUnderscore + sPDoB;
                    aS = result.get(key);
                    bS = dRecord.getPartnersDateOfBirth();
                    aS += DW_Strings.sCommaSpace + bS;
                    result.put(key, aS);
                    // PartnersAge
                    key = aCTBRef + DW_Strings.sUnderscore + sPA;
                    aS = result.get(key);
                    bS = DW_SHBE_Handler.getPartnersAge(year, month, dRecord);
                    aS += DW_Strings.sCommaSpace + bS;
                    result.put(key, aS);
                    // ClaimantsGender
                    key = aCTBRef + DW_Strings.sUnderscore + sCG;
                    aS = result.get(key);
                    bS = dRecord.getClaimantsGender();
                    aS += DW_Strings.sCommaSpace + bS;
                    result.put(key, aS);
                    // PartnersGender
                    key = aCTBRef + DW_Strings.sUnderscore + sPG;
                    aS = result.get(key);
                    bS = dRecord.getPartnersGender();
                    aS += DW_Strings.sCommaSpace + bS;
                    result.put(key, aS);
                    // Disability
                    key = aCTBRef + DW_Strings.sUnderscore + sDisability;
                    aS = result.get(key);
                    b = DW_SHBE_Handler.getDisability(dRecord);
                    if (b == true) {
                        aS += DW_Strings.sCommaSpace + sDisability;
                    } else {
                        aS += DW_Strings.sCommaSpace;
                    }
                    result.put(key, aS);
                    // Disability Premium
                    key = aCTBRef + DW_Strings.sUnderscore + sDisabilityPremium;
                    aS = result.get(key);
                    j = dRecord.getDisabilityPremiumAwarded();
                    if (j == 1) {
                        aS += DW_Strings.sCommaSpace + sDP;
                    } else {
                        aS += DW_Strings.sCommaSpace;
                    }
                    result.put(key, aS);
                    // Disability Severe
                    key = aCTBRef + DW_Strings.sUnderscore + sDisabilitySevere;
                    aS = result.get(key);
                    j = dRecord.getSevereDisabilityPremiumAwarded();
                    if (j == 1) {
                        aS += DW_Strings.sCommaSpace + sDS;
                    } else {
                        aS += DW_Strings.sCommaSpace;
                    }
                    result.put(key, aS);
                    // Disability Enhanced
                    key = aCTBRef + DW_Strings.sUnderscore + sDisabilityEnhanced;
                    aS = result.get(key);
                    j = dRecord.getEnhancedDisabilityPremiumAwarded();
                    if (j == 1) {
                        aS += DW_Strings.sCommaSpace + sDE;
                    } else {
                        aS += DW_Strings.sCommaSpace;
                    }
                    result.put(key, aS);
                    // Child Disability
                    key = aCTBRef + DW_Strings.sUnderscore + sDisabledChild;
                    aS = result.get(key);
                    j = dRecord.getDisabledChildPremiumAwarded();
                    if (j == 1) {
                        aS += DW_Strings.sCommaSpace + sDC;
                    } else {
                        aS += DW_Strings.sCommaSpace;
                    }
                    result.put(key, aS);
                    // Partner Death
                    key = aCTBRef + DW_Strings.sUnderscore + sPDeath;
                    aS = result.get(key);
                    bS = dRecord.getPartnersDateOfDeath();
                    if (bS == null) {
                        aS += DW_Strings.sCommaSpace;
                    } else if (bS.isEmpty()) {
                        aS += DW_Strings.sCommaSpace;
                    } else {
                        aS += DW_Strings.sCommaSpace + sPDeath + DW_Strings.sUnderscore + bS;
                    }
                    result.put(key, aS);
                    // HB Discretionary Payment
                    key = aCTBRef + DW_Strings.sUnderscore + sHBDP;
                    aS = result.get(key);
                    j = dRecord.getWeeklyAdditionalDiscretionaryPayment();
                    aS += DW_Strings.sCommaSpace + decimalise(j);
                    result.put(key, aS);
                    // Arrears
                    key = aCTBRef + DW_Strings.sUnderscore + sA;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                } else {
                    // Tenancy Type
                    key = aCTBRef + DW_Strings.sUnderscore + sTT;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Under Occupancy
                    key = aCTBRef + DW_Strings.sUnderscore + sUnderOccupancy;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Postcode
                    key = aCTBRef + DW_Strings.sUnderscore + sP;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Weekly Housing Benefit Entitlement
                    key = aCTBRef + DW_Strings.sUnderscore + sWHBE;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Weekly Eligible Rent Amount
                    key = aCTBRef + DW_Strings.sUnderscore + sWERA;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // PassportedStandardIndicator
                    key = aCTBRef + DW_Strings.sUnderscore + sPSI;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // StatusOfHBClaim
                    key = aCTBRef + DW_Strings.sUnderscore + sSHBC;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // ReasonThatHBClaimClosed
                    key = aCTBRef + DW_Strings.sUnderscore + sRTHBCC;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // ClaimantEthnicGroup
                    key = aCTBRef + DW_Strings.sUnderscore + sCEG;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Household Size
                    key = aCTBRef + DW_Strings.sUnderscore + sHS;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // NonDependents
                    key = aCTBRef + DW_Strings.sUnderscore + sND;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // ChildDependents
                    key = aCTBRef + DW_Strings.sUnderscore + sCD;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // NonDependents (UO)
                    key = aCTBRef + DW_Strings.sUnderscore + sNDUO;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // ChildrenOver16
                    key = aCTBRef + DW_Strings.sUnderscore + sCO16;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // FemaleChildrenUnder10";
                    key = aCTBRef + DW_Strings.sUnderscore + sFCU10;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // MaleChildrenUnder10";
                    key = aCTBRef + DW_Strings.sUnderscore + sMCU10;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // FemaleChildren10to16";
                    key = aCTBRef + DW_Strings.sUnderscore + sFC10To16;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // MaleChildren10to16
                    key = aCTBRef + DW_Strings.sUnderscore + sMC10To16;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Number of Bedrooms
                    key = aCTBRef + DW_Strings.sUnderscore + sNB;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Bedroom Requirement
                    key = aCTBRef + DW_Strings.sUnderscore + sBR;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Claimants Date Of Birth
                    key = aCTBRef + DW_Strings.sUnderscore + sCDoB;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // ClaimantsAge
                    key = aCTBRef + DW_Strings.sUnderscore + sCA;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Partners Date Of Birth
                    key = aCTBRef + DW_Strings.sUnderscore + sPDoB;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Partners Age
                    key = aCTBRef + DW_Strings.sUnderscore + sPA;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // ClaimantsGender
                    key = aCTBRef + DW_Strings.sUnderscore + sCG;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Partners Gender
                    key = aCTBRef + DW_Strings.sUnderscore + sPG;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Disability
                    key = aCTBRef + DW_Strings.sUnderscore + sDisability;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Disability Premium
                    key = aCTBRef + DW_Strings.sUnderscore + sDisabilityPremium;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Disability Severe
                    key = aCTBRef + DW_Strings.sUnderscore + sDisabilitySevere;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Disability Enhanced
                    key = aCTBRef + DW_Strings.sUnderscore + sDisabilityEnhanced;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Child Disability
                    key = aCTBRef + DW_Strings.sUnderscore + sDisabledChild;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Partner Death
                    key = aCTBRef + DW_Strings.sUnderscore + sPDeath;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // HB Discretionary Payment
                    key = aCTBRef + DW_Strings.sUnderscore + sHBDP;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Arrears
                    key = aCTBRef + DW_Strings.sUnderscore + sA;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
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

    /**
     * 
     * @param DW_UO_Data
     * @param SHBEFilenames
     * @param include
     * @param aPT A Payment Type.
     * @param includePreUnderOccupancyValues
     * @return 
     */
    public Object[] getTable(
            DW_UO_Data DW_UO_Data,
            String[] SHBEFilenames,
            ArrayList<Integer> include,
            String aPT,
            boolean includePreUnderOccupancyValues
    ) {
        Object[] result;
        result = new Object[8];
        //validPostcodes = new HashMap<String, HashSet<String>>();
        validPostcodes = new HashSet<String>();

        // Initialise result part 1
        TreeMap<String, String> tableValues;
        tableValues = new TreeMap<String, String>();

        TreeMap<String, DW_UO_Set> councilUnderOccupiedSets = null;
        TreeMap<String, DW_UO_Set> RSLUnderOccupiedSets = null;
        councilUnderOccupiedSets = DW_UO_Data.getCouncilSets();
        RSLUnderOccupiedSets = DW_UO_Data.getRSLSets();

        TreeSet<String> tUOClaims;
        tUOClaims = new TreeSet<String>();

        // Init Time Statistics
        TreeMap<String, TreeMap<String, ?>> timeStatistics;
        timeStatistics = new TreeMap<String, TreeMap<String, ?>>();
        TreeMap<String, Integer> totalCounts_cumulativeUniqueClaims;
        totalCounts_cumulativeUniqueClaims = new TreeMap<String, Integer>();
        timeStatistics.put(
                sTotalCount_cumulativeUniqueClaims,
                totalCounts_cumulativeUniqueClaims);
        int totalCount_UOClaims;
        TreeMap<String, Integer> totalCounts_UOClaims;
        totalCounts_UOClaims = new TreeMap<String, Integer>();
        timeStatistics.put(
                sTotalCount_UOClaims,
                totalCounts_UOClaims);
        int totalCount_UOCouncilClaims;
        TreeMap<String, Integer> totalCounts_UOCouncilClaims;
        totalCounts_UOCouncilClaims = new TreeMap<String, Integer>();
        timeStatistics.put(
                sTotalCount_UOCouncilClaims,
                totalCounts_UOCouncilClaims);
        int totalCount_UORSLClaims;
        TreeMap<String, Integer> totalCounts_UORSLClaims;
        totalCounts_UORSLClaims = new TreeMap<String, Integer>();
        timeStatistics.put(
                sTotalCount_UORSLClaims,
                totalCounts_UORSLClaims);
        int totalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE;
        TreeMap<String, Integer> totalCounts_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE;
        totalCounts_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE = new TreeMap<String, Integer>();
        timeStatistics.put(
                sTotalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE,
                totalCounts_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE);
        int totalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO;
        TreeMap<String, Integer> totalCounts_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO;
        totalCounts_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO = new TreeMap<String, Integer>();
        timeStatistics.put(sTotalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO,
                totalCounts_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO);

        int totalHouseholdSizeExcludingPartnersCouncilSHBE;
        TreeMap<String, Integer> totalHouseholdSizeExcludingPartnersCouncilSHBEs;
        totalHouseholdSizeExcludingPartnersCouncilSHBEs = new TreeMap<String, Integer>();
        timeStatistics.put(sTotalHouseholdSizeExcludingPartnersCouncilSHBE,
                totalHouseholdSizeExcludingPartnersCouncilSHBEs);

        int totalHouseholdSizeExcludingPartnersCouncilUO;
        TreeMap<String, Integer> totalHouseholdSizeExcludingPartnersCouncilUOs;
        totalHouseholdSizeExcludingPartnersCouncilUOs = new TreeMap<String, Integer>();
        timeStatistics.put(sTotalHouseholdSizeExcludingPartnersCouncilUO,
                totalHouseholdSizeExcludingPartnersCouncilUOs);

        int totalHouseholdSizeExcludingPartnersRSLSHBE;
        TreeMap<String, Integer> totalHouseholdSizeExcludingPartnersRSLSHBEs;
        totalHouseholdSizeExcludingPartnersRSLSHBEs = new TreeMap<String, Integer>();
        timeStatistics.put(sTotalHouseholdSizeExcludingPartnersRSLSHBE,
                totalHouseholdSizeExcludingPartnersRSLSHBEs);

        int totalHouseholdSizeExcludingPartnersRSLUO;
        TreeMap<String, Integer> totalHouseholdSizeExcludingPartnersRSLUOs;
        totalHouseholdSizeExcludingPartnersRSLUOs = new TreeMap<String, Integer>();
        timeStatistics.put(sTotalHouseholdSizeExcludingPartnersRSLUO,
                totalHouseholdSizeExcludingPartnersRSLUOs);

//        int totalAggregateHouseholdSize_UOClaims;
//        TreeMap<String, Integer> totalAggregateHouseholdSize_UOClaimss;
//        totalAggregateHouseholdSize_UOClaimss = new TreeMap<String, Integer>();
//        timeStatistics.put(
//                sTotalAggregateHouseholdSize_UOClaims,
//                totalAggregateHouseholdSize_UOClaimss);
//        double averageHouseholdSize_UOClaims;
//        TreeMap<String, Double> averageHouseholdSize_UOClaimss;
//        averageHouseholdSize_UOClaimss = new TreeMap<String, Double>();
//        timeStatistics.put(sAverageHouseholdSize_UOClaims,
//                totalCounts_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedrooms);
//         String sTotalAggregateHouseholdSize_UOClaimsCouncil = "TotalAggregateHouseholdSize_UOClaimsCouncil";
//    String sTotalAverageHouseholdSize_UOClaimsCouncil = "TotalAverageHouseholdSize_UOClaimsCouncil";
//    String sTotalAggregateHouseholdSize_UOClaimsRSL = "TotalAggregateHouseholdSize_UOClaimsRSL";
//    String sTotalAverageHouseholdSize_UOClaimsRSL = "TotalAverageHouseholdSize_UOClaimsRSL";
        HashSet<String>[] tAllCTBRefs;
        tAllCTBRefs = getUOCTBRefs(
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

        HashSet<String>[] tStartUOCTBRefsX;
        tStartUOCTBRefsX = getStartUOCTBRefs(
                councilUnderOccupiedSets,
                RSLUnderOccupiedSets,
                SHBEFilenames,
                include);
        HashSet<String> tStartUOCTBRefs;
        tStartUOCTBRefs = new HashSet<String>();
        tStartUOCTBRefs.addAll(tStartUOCTBRefsX[0]);
        tStartUOCTBRefs.addAll(tStartUOCTBRefsX[1]);

        HashSet<String>[] tEndUOCTBRefsX;
        tEndUOCTBRefsX = getEndUOCTBRefs(
                councilUnderOccupiedSets,
                RSLUnderOccupiedSets,
                SHBEFilenames,
                include);
        HashSet<String> tEndUOCTBRefs;
        tEndUOCTBRefs = new HashSet<String>();
        tEndUOCTBRefs.addAll(tEndUOCTBRefsX[0]);
        tEndUOCTBRefs.addAll(tEndUOCTBRefsX[1]);

        HashSet<String> subsequentlyEffectedUOStillUOInOctober2015;
        subsequentlyEffectedUOStillUOInOctober2015 = new HashSet<String>();
        subsequentlyEffectedUOStillUOInOctober2015.addAll(tCTBRefs);
        subsequentlyEffectedUOStillUOInOctober2015.removeAll(tStartUOCTBRefs);
        subsequentlyEffectedUOStillUOInOctober2015.retainAll(tEndUOCTBRefs);

        TreeMap<String, BigDecimal> generalStatistics;
        generalStatistics = new TreeMap<String, BigDecimal>();
        generalStatistics.put(sSubsequentlyEffectedUOStillUOInOctober2015,
                BigDecimal.valueOf(subsequentlyEffectedUOStillUOInOctober2015.size()));
        HashSet<String> tEndUOThatWereAlsoStartUOCTBRefs;
        tEndUOThatWereAlsoStartUOCTBRefs = new HashSet<String>();
        tEndUOThatWereAlsoStartUOCTBRefs.addAll(tEndUOCTBRefs);
        tEndUOThatWereAlsoStartUOCTBRefs.retainAll(tStartUOCTBRefs);

        TreeMap<String, ArrayList<Integer>> includes;
        includes = DW_SHBE_Handler.getIncludes();
        ArrayList<Integer> MonthlyUO;
        MonthlyUO = includes.get(DW_Strings.sIncludeMonthlySinceApril2013);
        ArrayList<Integer> All;
        All = includes.get(DW_Strings.sIncludeAll);
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

        DW_SHBE_Collection aSHBEData = null;
        String aYM3 = s;
        String year = s;
        String month = s;
        DW_UO_Set councilUnderOccupiedSet1 = null;
        DW_UO_Set RSLUnderOccupiedSet1 = null;
        String aSHBEFilename = null;

        // % in arrears
        // % getting DHP
        // % in arrears and getting DHP
        // All October 2015
        // All October 2015 that were also there in April 2013
        Iterator<Integer> iteX;
        iteX = include.iterator();
        int j = 0;
        while (iteX.hasNext()) {
            j = iteX.next();
        }
        aSHBEFilename = SHBEFilenames[j];
        aYM3 = DW_SHBE_Handler.getYM3(aSHBEFilename);
        year = DW_SHBE_Handler.getYear(aSHBEFilename);
        month = DW_SHBE_Handler.getMonthNumber(aSHBEFilename);
        councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(aYM3);
        if (councilUnderOccupiedSet1 != null) {
            RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(aYM3);
            aSHBEData = new DW_SHBE_Collection(env, aSHBEFilename, aPT);
        }

        TreeMap<String, DW_SHBE_Record> aRecords;
        aRecords = aSHBEData.getRecords();
        DW_SHBE_Record aDW_SHBE_Record;
        DW_UO_Set endCouncilUnderOccupiedSet;
        endCouncilUnderOccupiedSet = councilUnderOccupiedSets.get(aYM3);
        TreeMap<String, DW_UO_Record> endCouncilUnderOccupiedSetMap;
        endCouncilUnderOccupiedSetMap = endCouncilUnderOccupiedSet.getMap();
        int inArrearsCount = 0;
        int receivingDHPCount = 0;
        int inArrearsAndReceivingDHPCount = 0;
        String aCTBRef;
        DW_SHBE_D_Record aDRecord;
        /*
        * Iterate over records in tEndUOCTBRefs
        */
        Iterator<String> tCTBRefsIte;
        tCTBRefsIte = tEndUOCTBRefs.iterator();
        while (tCTBRefsIte.hasNext()) {
            aCTBRef = tCTBRefsIte.next();
            aDW_SHBE_Record = aRecords.get(aCTBRef);
            if (aDW_SHBE_Record == null) {
                System.out.println(aCTBRef + " not in " + aYM3);
            } else {
                aDRecord = aDW_SHBE_Record.getDRecord();
                int DHP;
                DHP = aDRecord.getWeeklyAdditionalDiscretionaryPayment();
                if (DHP > 0) {
                    receivingDHPCount++;
                }
                if (endCouncilUnderOccupiedSetMap.containsKey(aCTBRef)) {
                    DW_UO_Record UORec;
                    UORec = endCouncilUnderOccupiedSetMap.get(aCTBRef);
                    double arrears;
                    arrears = UORec.getTotalRentArrears();
                    if (arrears > 0) {
                        inArrearsCount++;
                        if (DHP > 0) {
                            inArrearsAndReceivingDHPCount++;
                        }
                    }
                }
            }
        }
        double p;
        p = ((double) receivingDHPCount) * 100.0d / (double) tEndUOCTBRefs.size();
        generalStatistics.put(sPercentageReceivingDHPInOctober2015,
                Generic_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        p = ((double) inArrearsCount) * 100.0d / (double) endCouncilUnderOccupiedSetMap.size();
        generalStatistics.put(sPercentageInArrearsOctober2015,
                Generic_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        p = ((double) inArrearsAndReceivingDHPCount) * 100.0d / (double) tEndUOCTBRefs.size();
        generalStatistics.put(sPercentageInArrearsAndReceivingDHPInOctober2015,
                Generic_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));

        tCTBRefsIte = tEndUOThatWereAlsoStartUOCTBRefs.iterator();
        while (tCTBRefsIte.hasNext()) {
            aCTBRef = tCTBRefsIte.next();
            aDW_SHBE_Record = aRecords.get(aCTBRef);
            if (aDW_SHBE_Record == null) {
                System.out.println(aCTBRef + " not in " + aYM3);
            } else {
                aDRecord = aDW_SHBE_Record.getDRecord();
                int DHP;
                DHP = aDRecord.getWeeklyAdditionalDiscretionaryPayment();
                if (DHP > 0) {
                    receivingDHPCount++;
                }
                if (endCouncilUnderOccupiedSetMap.containsKey(aCTBRef)) {
                    DW_UO_Record UORec;
                    UORec = endCouncilUnderOccupiedSetMap.get(aCTBRef);
                    double arrears;
                    arrears = UORec.getTotalRentArrears();
                    if (arrears > 0) {
                        inArrearsCount++;
                        if (DHP > 0) {
                            inArrearsAndReceivingDHPCount++;
                        }
                    }
                }
            }
        }
        p = ((double) receivingDHPCount) * 100.0d / (double) tEndUOCTBRefs.size();
        generalStatistics.put(sPercentageReceivingDHPInOctober2015ThatWereUOInApril2013,
                Generic_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        p = ((double) inArrearsCount) * 100.0d / (double) endCouncilUnderOccupiedSetMap.size();
        generalStatistics.put(sPercentageInArrearsOctober2015ThatWereUOInApril2013,
                Generic_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        p = ((double) inArrearsAndReceivingDHPCount) * 100.0d / (double) tEndUOCTBRefs.size();
        generalStatistics.put(sPercentageInArrearsAndReceivingDHPInOctober2015ThatWereUOInApril2013,
                Generic_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));

//        TreeMap<String, ArrayList<Integer>> includes;
//        includes = DW_SHBE_Handler.getIncludes();
//        ArrayList<Integer> MonthlyUO;
//        MonthlyUO = includes.get(DW_SHBE_Handler.sIncludeMonthlySinceApril2013);
//        ArrayList<Integer> All;
//        All = includes.get(DW_SHBE_Handler.sIncludeAll);
//        ArrayList<Integer> NotMonthlyUO;
//        NotMonthlyUO = new ArrayList<Integer>();
//        NotMonthlyUO.addAll(All);
//        NotMonthlyUO.removeAll(MonthlyUO);
//
//        TreeMap<String, String> preUnderOccupancyValues;
//        preUnderOccupancyValues = null;
//        if (includePreUnderOccupancyValues) {
//            preUnderOccupancyValues = getPreUnderOccupancyValues(tCTBRefs,
//                    SHBEFilenames,
//                    NotMonthlyUO);
//            result[4] = preUnderOccupancyValues;
//        }
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

        HashSet<ID> tIDSetUniqueDependentChildrenUnderAge10Effected;
        tIDSetUniqueDependentChildrenUnderAge10Effected = new HashSet<ID>();

        // groups is for ordering the table output. Keys are the group type and 
        // values are ordered sets of keys for writing rows.
        HashMap<String, TreeSet<String>> groups;
        groups = new HashMap<String, TreeSet<String>>();

        TreeSet<String> tCTBRefSetPermanantlyLeftUOButRemainedInSHBE;
        tCTBRefSetPermanantlyLeftUOButRemainedInSHBE = new TreeSet<String>();
        groups.put(sPermanantlyLeftUOButRemainedInSHBE, tCTBRefSetPermanantlyLeftUOButRemainedInSHBE);

        TreeSet<String> tCTBRefSetPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged;
        tCTBRefSetPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged = new TreeSet<String>();
        groups.put(sPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged, tCTBRefSetPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged);

        TreeSet<String> tCTBRefSetPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased;
        tCTBRefSetPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased = new TreeSet<String>();
        groups.put(sPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased, tCTBRefSetPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased);

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

//        TreeSet<String> tCTBRefSetNoValidPostcodeChange;
//        tCTBRefSetNoValidPostcodeChange = new TreeSet<String>();
//        tCTBRefSetNoValidPostcodeChange.addAll(tCTBRefs);
//        groups.put(sNoValidPostcodeChange, tCTBRefSetNoValidPostcodeChange);
        TreeSet<String> tCTBRefSetValidPostcodeChange;
        tCTBRefSetValidPostcodeChange = new TreeSet<String>();
        groups.put(sNoValidPostcodeChange, tCTBRefSetValidPostcodeChange);

        TreeSet<String> tCTBRefSetChangedTT;
        tCTBRefSetChangedTT = new TreeSet<String>();
        groups.put(sChangedTT, tCTBRefSetChangedTT);

        TreeSet<String> tCTBRefSetUOAtSomePoint;
        tCTBRefSetUOAtSomePoint = new TreeSet<String>();
        groups.put(sUOAtSomePoint, tCTBRefSetUOAtSomePoint);

        TreeSet<String> tCTBRefSetUOTT1AtSomePoint;
        tCTBRefSetUOTT1AtSomePoint = new TreeSet<String>();
        groups.put(sUOTT1AtSomePoint, tCTBRefSetUOTT1AtSomePoint);

        TreeSet<String> tCTBRefSetUOTT4AtSomePoint;
        tCTBRefSetUOTT4AtSomePoint = new TreeSet<String>();
        groups.put(sUOTT4AtSomePoint, tCTBRefSetUOTT4AtSomePoint);

        TreeSet<String> tCTBRefSetAlwaysUOTT1FromStart;
        tCTBRefSetAlwaysUOTT1FromStart = new TreeSet<String>();
        tCTBRefSetAlwaysUOTT1FromStart.addAll(tCouncilCTBRefs);
        groups.put(sAlwaysUOTT1FromStart, tCTBRefSetAlwaysUOTT1FromStart);

        TreeSet<String> tCTBRefSetAlwaysUOTT1FromStartExceptWhenSuspended;
        tCTBRefSetAlwaysUOTT1FromStartExceptWhenSuspended = new TreeSet<String>();
        tCTBRefSetAlwaysUOTT1FromStartExceptWhenSuspended.addAll(tCouncilCTBRefs);
        groups.put(sAlwaysUOTT1FromStartExceptWhenSuspended, tCTBRefSetAlwaysUOTT1FromStartExceptWhenSuspended);

        TreeSet<String> tCTBRefSetAlwaysUOTT1FromWhenStarted;
        tCTBRefSetAlwaysUOTT1FromWhenStarted = new TreeSet<String>();
        //tCTBRefSetAlwaysUOFromWhenStarted.addAll(tCTBRefs);
        groups.put(sAlwaysUOTT1FromWhenStarted, tCTBRefSetAlwaysUOTT1FromWhenStarted);

        TreeSet<String> tCTBRefSetAlwaysUOTT4FromStart;
        tCTBRefSetAlwaysUOTT4FromStart = new TreeSet<String>();
        tCTBRefSetAlwaysUOTT4FromStart.addAll(tRSLCTBRefs);
        groups.put(sAlwaysUOTT4FromStart, tCTBRefSetAlwaysUOTT4FromStart);

        TreeSet<String> tCTBRefSetAlwaysUOTT4FromStartExceptWhenSuspended;
        tCTBRefSetAlwaysUOTT4FromStartExceptWhenSuspended = new TreeSet<String>();
        tCTBRefSetAlwaysUOTT4FromStartExceptWhenSuspended.addAll(tRSLCTBRefs);
        groups.put(sAlwaysUOTT4FromStartExceptWhenSuspended, tCTBRefSetAlwaysUOTT4FromStartExceptWhenSuspended);

        TreeSet<String> tCTBRefSetAlwaysUOTT4FromWhenStarted;
        tCTBRefSetAlwaysUOTT4FromWhenStarted = new TreeSet<String>();
        //tCTBRefSetAlwaysUOFromWhenStarted.addAll(tCTBRefs);
        groups.put(sAlwaysUOTT4FromWhenStarted, tCTBRefSetAlwaysUOTT4FromWhenStarted);

        TreeSet<String> tCTBRefSetIntermitantUO;
        tCTBRefSetIntermitantUO = new TreeSet<String>();
        groups.put(sIntermitantUO, tCTBRefSetIntermitantUO);

        TreeSet<String> tCTBRefSetUO_To_LeftSHBEAtSomePoint;
        tCTBRefSetUO_To_LeftSHBEAtSomePoint = new TreeSet<String>();
        groups.put(sUO_To_LeftSHBEAtSomePoint, tCTBRefSetUO_To_LeftSHBEAtSomePoint);

        TreeSet<String> tCTBRefSetUO_To_LeftSHBETheVeryNextMonth;
        tCTBRefSetUO_To_LeftSHBETheVeryNextMonth = new TreeSet<String>();
        groups.put(sUO_To_LeftSHBE, tCTBRefSetUO_To_LeftSHBETheVeryNextMonth);

        TreeSet<String> tCTBRefSetUO_To_LeftSHBEBetweenOneAndTwoMonths;
        tCTBRefSetUO_To_LeftSHBEBetweenOneAndTwoMonths = new TreeSet<String>();
        groups.put(sUO_To_LeftSHBEBetweenOneAndTwoMonths, tCTBRefSetUO_To_LeftSHBEBetweenOneAndTwoMonths);

        TreeSet<String> tCTBRefSetUO_To_LeftSHBEBetweenTwoAndThreeMonths;
        tCTBRefSetUO_To_LeftSHBEBetweenTwoAndThreeMonths = new TreeSet<String>();
        groups.put(sUO_To_LeftSHBEBetweenTwoAndThreeMonths, tCTBRefSetUO_To_LeftSHBEBetweenTwoAndThreeMonths);

        TreeSet<String> tCTBRefSetUO_To_LeftSHBEAndNotReturned;
        tCTBRefSetUO_To_LeftSHBEAndNotReturned = new TreeSet<String>();
        groups.put(sUO_To_LeftSHBE_NotReturned, tCTBRefSetUO_To_LeftSHBEAndNotReturned);

        TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBEAndNotReturned;
        tCTBRefSetUOTT1_To_LeftSHBEAndNotReturned = new TreeSet<String>();
        groups.put(sUOTT1_To_LeftSHBE_NotReturned, tCTBRefSetUOTT1_To_LeftSHBEAndNotReturned);

        TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBEAndNotReturned;
        tCTBRefSetUOTT4_To_LeftSHBEAndNotReturned = new TreeSet<String>();
        groups.put(sUOTT4_To_LeftSHBE_NotReturned, tCTBRefSetUOTT4_To_LeftSHBEAndNotReturned);

        TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBEAndNotReturned;
        tCTBRefSetUOTT3OrTT6_To_LeftSHBEAndNotReturned = new TreeSet<String>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_NotReturned, tCTBRefSetUOTT3OrTT6_To_LeftSHBEAndNotReturned);

        TreeSet<String> tCTBRefSetUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEAndNotReturned;
        tCTBRefSetUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEAndNotReturned = new TreeSet<String>();
        groups.put(sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned, tCTBRefSetUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEAndNotReturned);

        TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT1;
        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT1 = new TreeSet<String>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAsUOTT1, tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT1);

        TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT1;
        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT1 = new TreeSet<String>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT1, tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT1);

        TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6;
        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6 = new TreeSet<String>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6, tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6);

        TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT4;
        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT4 = new TreeSet<String>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAsUOTT4, tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT4);

        TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT4;
        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT4 = new TreeSet<String>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT4, tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT4);

        TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7;
        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7 = new TreeSet<String>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7, tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7);

        TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT8;
        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT8 = new TreeSet<String>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT8, tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT8);

        TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT9;
        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT9 = new TreeSet<String>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT9, tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT9);

        TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsUOTT1;
        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsUOTT1 = new TreeSet<String>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAsUOTT1, tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsUOTT1);

        TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT1;
        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT1 = new TreeSet<String>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT1, tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT1);

        TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6;
        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6 = new TreeSet<String>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6, tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6);

        TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsUOTT4;
        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsUOTT4 = new TreeSet<String>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAsUOTT4, tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsUOTT4);

        TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT4;
        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT4 = new TreeSet<String>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT4, tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT4);

        TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7;
        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7 = new TreeSet<String>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7, tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7);

        TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT8;
        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT8 = new TreeSet<String>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT8, tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT8);

        TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT9;
        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT9 = new TreeSet<String>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT9, tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT9);

        TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1;
        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1 = new TreeSet<String>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1, tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1);

        TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1;
        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1 = new TreeSet<String>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1, tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1);

        TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6;
        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6 = new TreeSet<String>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6, tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6);

        TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4;
        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4 = new TreeSet<String>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4, tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4);

        TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4;
        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4 = new TreeSet<String>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4, tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4);

        TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7;
        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7 = new TreeSet<String>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7, tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7);

        TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8;
        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8 = new TreeSet<String>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8, tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8);

        TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9;
        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9 = new TreeSet<String>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9, tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9);

        TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint;
        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint = new TreeSet<String>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint);

        TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint;
        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint = new TreeSet<String>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint);

        TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE;
        tCTBRefSetUOTT1_To_LeftSHBE = new TreeSet<String>();
        groups.put(sUOTT1_To_LeftSHBE, tCTBRefSetUOTT1_To_LeftSHBE);

//        TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT1orTT4;
//        tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT1orTT4 = new TreeSet<String>();
//        groups.put(sUOTT1_To_LeftSHBEReturnedAsTT1orTT4, tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT1orTT4);
//
//        TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT3OrTT6;
//        tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT3OrTT6 = new TreeSet<String>();
//        groups.put(sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6, tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT3OrTT6);
        TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE;
        tCTBRefSetUOTT4_To_LeftSHBE = new TreeSet<String>();
        groups.put(sUOTT4_To_LeftSHBE, tCTBRefSetUOTT4_To_LeftSHBE);

//        TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT1orTT4;
//        tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT1orTT4 = new TreeSet<String>();
//        groups.put(sUOTT4_To_LeftSHBEReturnedAsTT1orTT4, tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT1orTT4);
//
//        TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT3OrTT6;
//        tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT3OrTT6 = new TreeSet<String>();
//        groups.put(sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6, tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT3OrTT6);
        TreeSet<String> tCTBRefSetUO_NotUO;
        tCTBRefSetUO_NotUO = new TreeSet<String>();

        TreeSet<String> tCTBRefSetUO_NotUO_UO_NotUO;
        tCTBRefSetUO_NotUO_UO_NotUO = new TreeSet<String>();
        groups.put(sUO_NotUO_UO_NotUO, tCTBRefSetUO_NotUO_UO_NotUO);

        TreeSet<String> tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO;
        tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO = new TreeSet<String>();
        groups.put(sUO_NotUO_UO_NotUO_UO_NotUO, tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO);

        TreeSet<String> tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO;
        tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO = new TreeSet<String>();
        groups.put(sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO, tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO);

        TreeSet<String> tCTBRefSetUO_NotUO_UO;
        tCTBRefSetUO_NotUO_UO = new TreeSet<String>();
        groups.put(sUO_NotUO_UO, tCTBRefSetUO_NotUO_UO);

        TreeSet<String> tCTBRefSetUO_NotUO_UO_NotUO_UO;
        tCTBRefSetUO_NotUO_UO_NotUO_UO = new TreeSet<String>();
        groups.put(sUO_NotUO_UO_NotUO_UO, tCTBRefSetUO_NotUO_UO_NotUO_UO);

        TreeSet<String> tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO;
        tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO = new TreeSet<String>();
        groups.put(sUO_NotUO_UO_NotUO_UO_NotUO_UO, tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO);

        TreeSet<String> tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO;
        tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO = new TreeSet<String>();
        groups.put(sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO, tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO);

        TreeSet<String> tCTBRefSetUOTT1_To_NotUO_InSHBE_PostcodeChanged;
        tCTBRefSetUOTT1_To_NotUO_InSHBE_PostcodeChanged = new TreeSet<String>();
        groups.put(sUOTT1_To_NotUO_InSHBE_PostcodeChanged, tCTBRefSetUOTT1_To_NotUO_InSHBE_PostcodeChanged);

        TreeSet<String> tCTBRefSetUOTT1_To_UOTT1_PostcodeChanged;
        tCTBRefSetUOTT1_To_UOTT1_PostcodeChanged = new TreeSet<String>();
        groups.put(sUOTT1_To_UOTT1_PostcodeChanged, tCTBRefSetUOTT1_To_UOTT1_PostcodeChanged);

        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeChanged;
        tCTBRefSetUOTT1_To_TT1_PostcodeChanged = new TreeSet<String>();
        groups.put(sUOTT1_To_TT1_PostcodeChanged, tCTBRefSetUOTT1_To_TT1_PostcodeChanged);

        TreeSet<String> tCTBRefSetUOTT1_To_UOTT4_PostcodeChanged;
        tCTBRefSetUOTT1_To_UOTT4_PostcodeChanged = new TreeSet<String>();
        groups.put(sUOTT1_To_UOTT4_PostcodeChanged, tCTBRefSetUOTT1_To_UOTT4_PostcodeChanged);

        TreeSet<String> tCTBRefSetUOTT1_To_TT4_PostcodeChanged;
        tCTBRefSetUOTT1_To_TT4_PostcodeChanged = new TreeSet<String>();
        groups.put(sUOTT1_To_TT4_PostcodeChanged, tCTBRefSetUOTT1_To_TT4_PostcodeChanged);

        TreeSet<String> tCTBRefSetUOTT4_To_NotUO_InSHBE_PostcodeChanged;
        tCTBRefSetUOTT4_To_NotUO_InSHBE_PostcodeChanged = new TreeSet<String>();
        groups.put(sUOTT4_To_NotUO_InSHBE_PostcodeChanged, tCTBRefSetUOTT4_To_NotUO_InSHBE_PostcodeChanged);

        TreeSet<String> tCTBRefSetUOTT4_To_UOTT1_PostcodeChanged;
        tCTBRefSetUOTT4_To_UOTT1_PostcodeChanged = new TreeSet<String>();
        groups.put(sUOTT4_To_UOTT1_PostcodeChanged, tCTBRefSetUOTT4_To_UOTT1_PostcodeChanged);

        TreeSet<String> tCTBRefSetUOTT4_To_TT1_PostcodeChanged;
        tCTBRefSetUOTT4_To_TT1_PostcodeChanged = new TreeSet<String>();
        groups.put(sUOTT4_To_TT1_PostcodeChanged, tCTBRefSetUOTT4_To_TT1_PostcodeChanged);

        TreeSet<String> tCTBRefSetUOTT4_To_UOTT4_PostcodeChanged;
        tCTBRefSetUOTT4_To_UOTT4_PostcodeChanged = new TreeSet<String>();
        groups.put(sUOTT4_To_UOTT4_PostcodeChanged, tCTBRefSetUOTT4_To_UOTT4_PostcodeChanged);

        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeChanged;
        tCTBRefSetUOTT4_To_TT4_PostcodeChanged = new TreeSet<String>();
        groups.put(sUOTT4_To_TT4_PostcodeChanged, tCTBRefSetUOTT4_To_TT4_PostcodeChanged);

        TreeSet<String> tCTBRefSetUOTT1_To_TT3OrTT6;
        tCTBRefSetUOTT1_To_TT3OrTT6 = new TreeSet<String>();
        groups.put(sUOTT1_To_TT3OrTT6, tCTBRefSetUOTT1_To_TT3OrTT6);

        TreeSet<String> tCTBRefSetUOTT1_To_TT3OrTT6AtSomePoint;
        tCTBRefSetUOTT1_To_TT3OrTT6AtSomePoint = new TreeSet<String>();
        groups.put(sUOTT1_To_TT3OrTT6AtSomePoint, tCTBRefSetUOTT1_To_TT3OrTT6AtSomePoint);

        TreeSet<String> tCTBRefSetUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999;
        tCTBRefSetUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999 = new TreeSet<String>();
        groups.put(sUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999, tCTBRefSetUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999);

        TreeSet<String> tCTBRefSetUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint;
        tCTBRefSetUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint = new TreeSet<String>();
        groups.put(sUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint, tCTBRefSetUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint);

        TreeSet<String> tCTBRefSetUOTT1_To_TT3OrTT6NotDoneNextChange;
        tCTBRefSetUOTT1_To_TT3OrTT6NotDoneNextChange = new TreeSet<String>();

        TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE;
        tCTBRefSetUOTT3OrTT6_To_LeftSHBE = new TreeSet<String>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE, tCTBRefSetUOTT3OrTT6_To_LeftSHBE);

        TreeSet<String> tCTBRefSetUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE;
        tCTBRefSetUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE = new TreeSet<String>();
        groups.put(sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE, tCTBRefSetUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE);

        TreeSet<String> tCTBRefSetUOTT4_To_TT3OrTT6;
        tCTBRefSetUOTT4_To_TT3OrTT6 = new TreeSet<String>();
        groups.put(sUOTT4_To_TT3OrTT6, tCTBRefSetUOTT4_To_TT3OrTT6);

        TreeSet<String> tCTBRefSetUOTT4_To_TT3OrTT6AtSomePoint;
        tCTBRefSetUOTT4_To_TT3OrTT6AtSomePoint = new TreeSet<String>();
        groups.put(sUOTT4_To_TT3OrTT6AtSomePoint, tCTBRefSetUOTT4_To_TT3OrTT6AtSomePoint);

        TreeSet<String> tCTBRefSetUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999;
        tCTBRefSetUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999 = new TreeSet<String>();
        groups.put(sUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999, tCTBRefSetUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999);

        TreeSet<String> tCTBRefSetUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint;
        tCTBRefSetUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint = new TreeSet<String>();
        groups.put(sUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint, tCTBRefSetUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint);

        TreeSet<String> tCTBRefSetUOTT4_To_TT3OrTT6NotDoneNextChange;
        tCTBRefSetUOTT4_To_TT3OrTT6NotDoneNextChange = new TreeSet<String>();

        TreeSet<String> tCTBRefSetTT3OrTT6_To_UOTT1;
        tCTBRefSetTT3OrTT6_To_UOTT1 = new TreeSet<String>();
        groups.put(sTT3OrTT6_To_UOTT1, tCTBRefSetTT3OrTT6_To_UOTT1);

        TreeSet<String> tCTBRefSetTT3OrTT6_To_UOTT4;
        tCTBRefSetTT3OrTT6_To_UOTT4 = new TreeSet<String>();
        groups.put(sTT3OrTT6_To_UOTT4, tCTBRefSetTT3OrTT6_To_UOTT4);

        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged = new TreeSet<String>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchanged, tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged);
//        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month;
//        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month = new TreeSet<String>();
//        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
//                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month);
        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1 = new TreeSet<String>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1);
        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1 = new TreeSet<String>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1);
        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 = new TreeSet<String>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6);
        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4 = new TreeSet<String>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4);
        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4 = new TreeSet<String>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4);
        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 = new TreeSet<String>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7);
        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8 = new TreeSet<String>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8);
        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9 = new TreeSet<String>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9);

        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months = new TreeSet<String>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months);
        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months = new TreeSet<String>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months,
                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months);

        //TreeSet<String> tCTBRefSetUOTT1OrTT1_To_UOTT4;
        //tCTBRefSetUOTT1OrTT1_To_UOTT4 = new TreeSet<String>();
        //groups.put(sUOTT1OrTT1_To_UOTT4, tCTBRefSetUOTT1OrTT1_To_UOTT4);
        TreeSet<String> tCTBRefSetUOTT1_To_UOTT4;
        tCTBRefSetUOTT1_To_UOTT4 = new TreeSet<String>();
        groups.put(sUOTT1_To_UOTT4, tCTBRefSetUOTT1_To_UOTT4);
        TreeSet<String> tCTBRefSetTT1_To_UOTT4;
        tCTBRefSetTT1_To_UOTT4 = new TreeSet<String>();
        groups.put(sTT1_To_UOTT4, tCTBRefSetTT1_To_UOTT4);
        TreeSet<String> tCTBRefSetTT1_To_UOTT4GettingDHP;
        tCTBRefSetTT1_To_UOTT4GettingDHP = new TreeSet<String>();
        groups.put(sTT1_To_UOTT4GettingDHP, tCTBRefSetTT1_To_UOTT4GettingDHP);

        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged = new TreeSet<String>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchanged, tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged);
//        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month;
//        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month = new TreeSet<String>();
//        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
//                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month);        
        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1 = new TreeSet<String>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1);
        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1 = new TreeSet<String>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1);
        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 = new TreeSet<String>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6);
        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4 = new TreeSet<String>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4);
        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4 = new TreeSet<String>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4);
        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 = new TreeSet<String>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7);
        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8 = new TreeSet<String>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8);
        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9 = new TreeSet<String>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9);

        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months = new TreeSet<String>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months);
        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months = new TreeSet<String>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months,
                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months);

        //TreeSet<String> tCTBRefSetUOTT4OrTT4_To_UOTT1;
        //tCTBRefSetUOTT4OrTT4_To_UOTT1 = new TreeSet<String>();
        //groups.put(sUOTT4OrTT4_To_UOTT1, tCTBRefSetUOTT4OrTT4_To_UOTT1);
        //TreeSet<String> tCTBRefSetUOTT4OrTT4_To_UOTT1InArrears;
        //tCTBRefSetUOTT4OrTT4_To_UOTT1InArrears = new TreeSet<String>();
        //groups.put(sUOTT4OrTT4_To_UOTT1InArrears, tCTBRefSetUOTT4OrTT4_To_UOTT1InArrears);
        //TreeSet<String> tCTBRefSetUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP;
        //tCTBRefSetUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP = new TreeSet<String>();
        //groups.put(sUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
        //        tCTBRefSetUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP);
        TreeSet<String> tCTBRefSetUOTT4_To_UOTT1;
        tCTBRefSetUOTT4_To_UOTT1 = new TreeSet<String>();
        groups.put(sUOTT4_To_UOTT1, tCTBRefSetUOTT4_To_UOTT1);
        TreeSet<String> tCTBRefSetUOTT4_To_UOTT1InArrears;
        tCTBRefSetUOTT4_To_UOTT1InArrears = new TreeSet<String>();
        groups.put(sUOTT4_To_UOTT1InArrears, tCTBRefSetUOTT4_To_UOTT1InArrears);
        TreeSet<String> tCTBRefSetUOTT4_To_UOTT1GettingDHP;
        tCTBRefSetUOTT4_To_UOTT1GettingDHP = new TreeSet<String>();
        groups.put(sUOTT4_To_UOTT1GettingDHP,
                tCTBRefSetUOTT4_To_UOTT1GettingDHP);
        TreeSet<String> tCTBRefSetUOTT4_To_UOTT1InArrearsAndGettingDHP;
        tCTBRefSetUOTT4_To_UOTT1InArrearsAndGettingDHP = new TreeSet<String>();
        groups.put(sUOTT4_To_UOTT1InArrearsAndGettingDHP,
                tCTBRefSetUOTT4_To_UOTT1InArrearsAndGettingDHP);
        TreeSet<String> tCTBRefSetTT4_To_UOTT1;
        tCTBRefSetTT4_To_UOTT1 = new TreeSet<String>();
        groups.put(sTT4_To_UOTT1, tCTBRefSetTT4_To_UOTT1);
        TreeSet<String> tCTBRefSetTT4_To_UOTT1InArrears;
        tCTBRefSetTT4_To_UOTT1InArrears = new TreeSet<String>();
        groups.put(sTT4_To_UOTT1InArrears, tCTBRefSetTT4_To_UOTT1InArrears);
        TreeSet<String> tCTBRefSetTT4_To_UOTT1GettingDHP;
        tCTBRefSetTT4_To_UOTT1GettingDHP = new TreeSet<String>();
        groups.put(sTT4_To_UOTT1GettingDHP,
                tCTBRefSetTT4_To_UOTT1GettingDHP);
        TreeSet<String> tCTBRefSetTT4_To_UOTT1InArrearsAndGettingDHP;
        tCTBRefSetTT4_To_UOTT1InArrearsAndGettingDHP = new TreeSet<String>();
        groups.put(sTT4_To_UOTT1InArrearsAndGettingDHP,
                tCTBRefSetTT4_To_UOTT1InArrearsAndGettingDHP);

        TreeSet<String> tCTBRefSetInArrearsAtSomePoint;
        tCTBRefSetInArrearsAtSomePoint = new TreeSet<String>();
        TreeSet<String> tCTBRefSetDHPAtSomePoint;
        tCTBRefSetDHPAtSomePoint = new TreeSet<String>();
        TreeSet<String> tCTBRefSetInArrearsAtSomePoint_And_DHPAtSomePoint;
        tCTBRefSetInArrearsAtSomePoint_And_DHPAtSomePoint = new TreeSet<String>();
        groups.put(sInArrearsAtSomePoint_And_DHPAtSomePoint,
                tCTBRefSetInArrearsAtSomePoint_And_DHPAtSomePoint);

        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged = new TreeSet<String>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchanged, tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged);
//        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month;
//        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month = new TreeSet<String>();
//        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
//                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month);
        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1 = new TreeSet<String>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1);
        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1 = new TreeSet<String>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1);
        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 = new TreeSet<String>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6);
        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4 = new TreeSet<String>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4);
        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4 = new TreeSet<String>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4);
        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 = new TreeSet<String>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7);
        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8 = new TreeSet<String>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8);
        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9 = new TreeSet<String>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9);

        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months = new TreeSet<String>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months);
        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months = new TreeSet<String>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months,
                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months);

        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged = new TreeSet<String>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchanged, tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged);
//        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month;
//        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month = new TreeSet<String>();
//        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
//                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month);
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1 = new TreeSet<String>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1);
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1 = new TreeSet<String>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1);
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 = new TreeSet<String>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6);
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4 = new TreeSet<String>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4);
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4 = new TreeSet<String>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4);
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 = new TreeSet<String>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7);
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8 = new TreeSet<String>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8);
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9 = new TreeSet<String>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9);
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months = new TreeSet<String>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months);
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months = new TreeSet<String>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months,
                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months);

        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious = new TreeSet<String>();
        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious = new TreeSet<String>();
        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged3MonthsPrevious;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged3MonthsPrevious = new TreeSet<String>();

        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious = new TreeSet<String>();
        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious = new TreeSet<String>();
        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged3MonthsPrevious;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged3MonthsPrevious = new TreeSet<String>();

        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious = new TreeSet<String>();
        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious = new TreeSet<String>();
        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged3MonthsPrevious;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged3MonthsPrevious = new TreeSet<String>();

        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious = new TreeSet<String>();
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious = new TreeSet<String>();
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged3MonthsPrevious;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged3MonthsPrevious = new TreeSet<String>();

        TreeSet<String> tCTBRefSetUOTT1_ToTT1_PostcodeChanged;
        tCTBRefSetUOTT1_ToTT1_PostcodeChanged = new TreeSet<String>();
        groups.put(sUOTT1_To_TT1_PostcodeChanged, tCTBRefSetUOTT1_ToTT1_PostcodeChanged);

        TreeSet<String> tCTBRefSetUOTT1_ToUOTT1_PostcodeChanged;
        tCTBRefSetUOTT1_ToUOTT1_PostcodeChanged = new TreeSet<String>();
        groups.put(sUOTT1_To_UOTT1_PostcodeChanged, tCTBRefSetUOTT1_ToUOTT1_PostcodeChanged);

        TreeSet<String> tCTBRefSetUOTT4_ToTT4_PostcodeChanged;
        tCTBRefSetUOTT4_ToTT4_PostcodeChanged = new TreeSet<String>();
        groups.put(sUOTT4_To_TT4_PostcodeChanged, tCTBRefSetUOTT4_ToTT4_PostcodeChanged);

        TreeSet<String> tCTBRefSetUOTT4_ToUOTT4_PostcodeChanged;
        tCTBRefSetUOTT4_ToUOTT4_PostcodeChanged = new TreeSet<String>();
        groups.put(sUOTT4_To_UOTT4_PostcodeChanged, tCTBRefSetUOTT4_ToUOTT4_PostcodeChanged);

        TreeSet<String> tCTBRefSetTT1_ToUOTT1_PostcodeChanged;
        tCTBRefSetTT1_ToUOTT1_PostcodeChanged = new TreeSet<String>();
        groups.put(sTT1_To_UOTT1_PostcodeChanged, tCTBRefSetTT1_ToUOTT1_PostcodeChanged);

        TreeSet<String> tCTBRefSetTT4_ToUOTT4_PostcodeChanged;
        tCTBRefSetTT4_ToUOTT4_PostcodeChanged = new TreeSet<String>();
        groups.put(sTT4_To_UOTT4_PostcodeChanged, tCTBRefSetTT4_ToUOTT4_PostcodeChanged);

        TreeSet<String> tCTBRefSetUOClaimsRecievingDHP;
        tCTBRefSetUOClaimsRecievingDHP = new TreeSet<String>();
        groups.put(sUOClaimsRecievingDHP, tCTBRefSetUOClaimsRecievingDHP);

        TreeSet<String> tCTBRefSetUOTT1ClaimsInRentArrearsAtSomePoint;
        tCTBRefSetUOTT1ClaimsInRentArrearsAtSomePoint = new TreeSet<String>();
        groups.put(sUOTT1ClaimsInRentArrearsAtSomePoint, tCTBRefSetUOTT1ClaimsInRentArrearsAtSomePoint);

        TreeSet<String> tCTBRefSetUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint;
        tCTBRefSetUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint = new TreeSet<String>();
        groups.put(sUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint, tCTBRefSetUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint);

//        String aCTBRef;
//        Iterator<String> tCTBRefsIte;
        // Initialise aggregateStatistics and generalStatistics
        TreeMap<String, BigDecimal> aggregateStatistics;
        aggregateStatistics = new TreeMap<String, BigDecimal>();
        tCTBRefsIte = tCTBRefs.iterator();
        while (tCTBRefsIte.hasNext()) {
            aCTBRef = tCTBRefsIte.next();
            aggregateStatistics.put(aCTBRef + DW_Strings.sUnderscore + sTotal_DHP, BigDecimal.ZERO);
            aggregateStatistics.put(aCTBRef + DW_Strings.sUnderscore + sTotalCount_DHP, BigDecimal.ZERO);
            aggregateStatistics.put(aCTBRef + DW_Strings.sUnderscore + sTotal_HBLossDueToUO, BigDecimal.ZERO);
            aggregateStatistics.put(aCTBRef + DW_Strings.sUnderscore + sTotalCount_HBLossDueToUO, BigDecimal.ZERO);
            aggregateStatistics.put(aCTBRef + DW_Strings.sUnderscore + sMax_Arrears, BigDecimal.ZERO);
            aggregateStatistics.put(aCTBRef + DW_Strings.sUnderscore + sTotalCount_Arrears, BigDecimal.ZERO);
            aggregateStatistics.put(aCTBRef + DW_Strings.sUnderscore + sTotalCount_UnderOccupancy, BigDecimal.ZERO);
        }

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
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sTT, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sUnderOccupancy, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sP, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sWHBE, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sWERA, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sPSI, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sSHBC, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sRTHBCC, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sCEG, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sHS, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sND, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sCD, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sNDUO, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sCO16, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sFCU10, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sMCU10, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sFC10To16, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sMC10To16, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sNB, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sBR, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sCDoB, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sCA, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sPDoB, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sPA, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sCG, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sPG, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sDisability, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sDisabilityPremium, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sDisabilitySevere, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sDisabilityEnhanced, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sDisabledChild, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sPDeath, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sHBDP, s);
            tableValues.put(aCTBRef + DW_Strings.sUnderscore + sA, s);
            DHP_Totals.put(aCTBRef, 0);
        }

        // Load dataset 1
        Iterator<Integer> includeIte;
        includeIte = include.iterator();
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
                header += yM3 + DW_Strings.sCommaSpace;
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
                aSHBEData = new DW_SHBE_Collection(env, aSHBEFilename, aPT);
                initFirst = true;
            }
            header += aYM3;
        }
        //TreeMap<String, DW_SHBE_Record> aRecords;
        aRecords = aSHBEData.getRecords();
        TreeMap<String, DW_SHBE_Record> bRecords;
        bRecords = null;
        TreeMap<String, DW_SHBE_Record> cRecords;
        cRecords = null;

        //DW_SHBE_Record aDW_SHBE_Record;
        TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth = new TreeSet<String>();
        TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth = new TreeSet<String>();
        TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth = new TreeSet<String>();
        TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth = new TreeSet<String>();

        // Add TT of all CTBRefs to result
        Object[] processResult;
        totalCount_UOClaims = 0;
        totalCount_UOCouncilClaims = 0;
        totalCount_UORSLClaims = 0;
        totalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE = 0;
        totalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO = 0;
        totalHouseholdSizeExcludingPartnersCouncilSHBE = 0;
        totalHouseholdSizeExcludingPartnersCouncilUO = 0;
        totalHouseholdSizeExcludingPartnersRSLSHBE = 0;
        totalHouseholdSizeExcludingPartnersRSLUO = 0;
        tCTBRefsIte = tCTBRefs.iterator();
        while (tCTBRefsIte.hasNext()) {
            aCTBRef = tCTBRefsIte.next();
            aDW_SHBE_Record = aRecords.get(aCTBRef);
            processResult = process(
                    tUOClaims,
                    aggregateStatistics,
                    generalStatistics,
                    aCTBRef,
                    year,
                    month,
                    aYM3,
                    aDW_SHBE_Record,
                    bRecords,
                    cRecords,
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
                    tIDSetUniqueDependentChildrenUnderAge10Effected,
                    tCTBRefSetPermanantlyLeftUOButRemainedInSHBE,
                    tCTBRefSetPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged,
                    tCTBRefSetPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased,
                    tCTBRefSetTravellers,
                    tCTBRefSetTTNot1Or4AndUnderOccupying,
                    tCTBRefSetTT1_To_TT3,
                    tCTBRefSetTT4_To_TT3,
                    tCTBRefSetTT3_To_TT1,
                    tCTBRefSetTT3_To_TT4,
                    tCTBRefSetValidPostcodeChange,
                    tCTBRefSetChangedTT,
                    tCTBRefSetUOAtSomePoint,
                    tCTBRefSetUOTT1AtSomePoint,
                    tCTBRefSetUOTT4AtSomePoint,
                    tCTBRefSetAlwaysUOTT1FromStart,
                    tCTBRefSetAlwaysUOTT1FromStartExceptWhenSuspended,
                    tCTBRefSetAlwaysUOTT1FromWhenStarted,
                    tCTBRefSetAlwaysUOTT4FromStart,
                    tCTBRefSetAlwaysUOTT4FromStartExceptWhenSuspended,
                    tCTBRefSetAlwaysUOTT4FromWhenStarted,
                    tCTBRefSetIntermitantUO,
                    tCTBRefSetUO_To_LeftSHBEAtSomePoint,
                    tCTBRefSetUOTT1_To_LeftSHBE,
                    tCTBRefSetUOTT4_To_LeftSHBE,
                    tCTBRefSetUOTT3OrTT6_To_LeftSHBE,
                    tCTBRefSetUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE,
                    //tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT1orTT4,
                    //tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
                    //tCTBRefSetUOTT4_To_LeftSHBEAndHaveNotReturned,
                    //tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT1orTT4,
                    //tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
                    //tCTBRefSetUO_To_LeftSHBETheVeryNextMonth,
                    tCTBRefSetUO_To_LeftSHBETheVeryNextMonth,
                    tCTBRefSetUO_To_LeftSHBEBetweenOneAndTwoMonths,
                    tCTBRefSetUO_To_LeftSHBEBetweenTwoAndThreeMonths,
                    tCTBRefSetUO_To_LeftSHBEAndNotReturned,
                    tCTBRefSetUOTT1_To_LeftSHBEAndNotReturned,
                    tCTBRefSetUOTT4_To_LeftSHBEAndNotReturned,
                    tCTBRefSetUOTT3OrTT6_To_LeftSHBEAndNotReturned,
                    tCTBRefSetUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEAndNotReturned,
                    tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT1,
                    tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT1,
                    tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
                    tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT4,
                    tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT4,
                    tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
                    tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT8,
                    tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT9,
                    tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsUOTT1,
                    tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT1,
                    tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6,
                    tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsUOTT4,
                    tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT4,
                    tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7,
                    tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT8,
                    tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT9,
                    tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1,
                    tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1,
                    tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6,
                    tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4,
                    tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4,
                    tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7,
                    tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8,
                    tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9,
                    tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                    tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                    tCTBRefSetUO_NotUO,
                    tCTBRefSetUO_NotUO_UO,
                    tCTBRefSetUO_NotUO_UO_NotUO,
                    tCTBRefSetUO_NotUO_UO_NotUO_UO,
                    tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO,
                    tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO,
                    tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO,
                    tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO,
                    tCTBRefSetUOTT1_To_NotUO_InSHBE_PostcodeChanged,
                    tCTBRefSetUOTT1_To_UOTT1_PostcodeChanged,
                    tCTBRefSetUOTT1_To_TT1_PostcodeChanged,
                    tCTBRefSetUOTT1_To_UOTT4_PostcodeChanged,
                    tCTBRefSetUOTT1_To_TT4_PostcodeChanged,
                    tCTBRefSetUOTT4_To_NotUO_InSHBE_PostcodeChanged,
                    tCTBRefSetUOTT4_To_UOTT1_PostcodeChanged,
                    tCTBRefSetUOTT4_To_TT1_PostcodeChanged,
                    tCTBRefSetUOTT4_To_UOTT4_PostcodeChanged,
                    tCTBRefSetUOTT4_To_TT4_PostcodeChanged,
                    tCTBRefSetUOTT1_To_TT3OrTT6,
                    tCTBRefSetUOTT1_To_TT3OrTT6AtSomePoint,
                    tCTBRefSetUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                    tCTBRefSetUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint,
                    tCTBRefSetUOTT1_To_TT3OrTT6NotDoneNextChange,
                    tCTBRefSetUOTT4_To_TT3OrTT6,
                    tCTBRefSetUOTT4_To_TT3OrTT6AtSomePoint,
                    tCTBRefSetUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                    tCTBRefSetUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint,
                    tCTBRefSetUOTT4_To_TT3OrTT6NotDoneNextChange,
                    tCTBRefSetTT3OrTT6_To_UOTT1,
                    tCTBRefSetTT3OrTT6_To_UOTT4,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth,
                    //tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious,
                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged3MonthsPrevious,
                    //tCTBRefSetUOTT1OrTT1_To_UOTT4,
                    tCTBRefSetUOTT1_To_UOTT4,
                    tCTBRefSetTT1_To_UOTT4,
                    tCTBRefSetTT1_To_UOTT4GettingDHP,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth,
                    //tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious,
                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged3MonthsPrevious,
                    //tCTBRefSetUOTT4OrTT4_To_UOTT1,
                    //tCTBRefSetUOTT4OrTT4_To_UOTT1InArrears,
                    //tCTBRefSetUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
                    tCTBRefSetUOTT4_To_UOTT1,
                    tCTBRefSetUOTT4_To_UOTT1InArrears,
                    tCTBRefSetUOTT4_To_UOTT1GettingDHP,
                    tCTBRefSetUOTT4_To_UOTT1InArrearsAndGettingDHP,
                    tCTBRefSetTT4_To_UOTT1,
                    tCTBRefSetTT4_To_UOTT1InArrears,
                    tCTBRefSetTT4_To_UOTT1GettingDHP,
                    tCTBRefSetTT4_To_UOTT1InArrearsAndGettingDHP,
                    tCTBRefSetInArrearsAtSomePoint,
                    tCTBRefSetDHPAtSomePoint,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth,
                    //tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious,
                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged3MonthsPrevious,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth,
                    //tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious,
                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged3MonthsPrevious,
                    tCTBRefSetUOTT1_ToTT1_PostcodeChanged,
                    tCTBRefSetUOTT1_ToUOTT1_PostcodeChanged,
                    tCTBRefSetUOTT4_ToTT4_PostcodeChanged,
                    tCTBRefSetUOTT4_ToUOTT4_PostcodeChanged,
                    tCTBRefSetTT1_ToUOTT1_PostcodeChanged,
                    tCTBRefSetTT4_ToUOTT4_PostcodeChanged,
                    tCTBRefSetUOClaimsRecievingDHP,
                    tCTBRefSetUOTT1ClaimsInRentArrearsAtSomePoint,
                    tCTBRefSetUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint);
            if ((Boolean) processResult[0]) {
                totalCount_UOClaims++;
            }
            if ((Boolean) processResult[1]) {
                totalCount_UOCouncilClaims++;
            }
            if ((Boolean) processResult[2]) {
                totalCount_UORSLClaims++;
            }
            if ((Boolean) processResult[3]) {
                totalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE++;
            }
            if ((Boolean) processResult[4]) {
                totalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO++;
            }
            totalHouseholdSizeExcludingPartnersCouncilSHBE += (Integer) processResult[5];
            totalHouseholdSizeExcludingPartnersCouncilUO += (Integer) processResult[6];
            totalHouseholdSizeExcludingPartnersRSLSHBE += (Integer) processResult[7];
            totalHouseholdSizeExcludingPartnersRSLUO += (Integer) processResult[8];
        }

        tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious = tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth;
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious = tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth;
        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth = new TreeSet<String>();
        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth = new TreeSet<String>();
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious = tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth;
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious = tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth;
        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth = new TreeSet<String>();
        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth = new TreeSet<String>();

        String yearMonth;
        yearMonth = year + "-" + month;
        totalCounts_cumulativeUniqueClaims.put(
                yearMonth,
                tUOClaims.size());
        totalCounts_UOClaims.put(
                yearMonth,
                totalCount_UOClaims);
        totalCounts_UOCouncilClaims.put(
                yearMonth,
                totalCount_UOCouncilClaims);
        totalCounts_UORSLClaims.put(
                yearMonth,
                totalCount_UORSLClaims);
        totalCounts_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE.put(
                yearMonth,
                totalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE);
        totalCounts_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO.put(
                yearMonth,
                totalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO);
        totalHouseholdSizeExcludingPartnersCouncilSHBEs.put(
                yearMonth,
                totalHouseholdSizeExcludingPartnersCouncilSHBE);
        totalHouseholdSizeExcludingPartnersCouncilUOs.put(
                yearMonth,
                totalHouseholdSizeExcludingPartnersCouncilUO);
        totalHouseholdSizeExcludingPartnersRSLSHBEs.put(
                yearMonth,
                totalHouseholdSizeExcludingPartnersRSLSHBE);
        totalHouseholdSizeExcludingPartnersRSLUOs.put(
                yearMonth,
                totalHouseholdSizeExcludingPartnersRSLUO);

        // Iterate over the rest of the data
        while (includeIte.hasNext()) {
            totalCount_UOClaims = 0;
            totalCount_UOCouncilClaims = 0;
            totalCount_UORSLClaims = 0;
            totalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE = 0;
            totalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO = 0;
            totalHouseholdSizeExcludingPartnersCouncilSHBE = 0;
            totalHouseholdSizeExcludingPartnersCouncilUO = 0;
            totalHouseholdSizeExcludingPartnersRSLSHBE = 0;
            totalHouseholdSizeExcludingPartnersRSLUO = 0;
            i = includeIte.next();
            cRecords = bRecords;
            bRecords = aRecords;
            aSHBEFilename = SHBEFilenames[i];
            aYM3 = DW_SHBE_Handler.getYM3(aSHBEFilename);
            year = DW_SHBE_Handler.getYear(aSHBEFilename);
            month = DW_SHBE_Handler.getMonthNumber(aSHBEFilename);
            aSHBEData = new DW_SHBE_Collection(env, aSHBEFilename, aPT);
            aRecords = aSHBEData.getRecords();
            councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(aYM3);
            RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(aYM3);
            header += DW_Strings.sCommaSpace + aYM3;
            tCTBRefsIte = tCTBRefs.iterator();
            while (tCTBRefsIte.hasNext()) {
                aCTBRef = tCTBRefsIte.next();
                aDW_SHBE_Record = aRecords.get(aCTBRef);
                processResult = process(
                        tUOClaims,
                        aggregateStatistics,
                        generalStatistics,
                        aCTBRef,
                        year,
                        month,
                        aYM3,
                        aDW_SHBE_Record,
                        bRecords,
                        cRecords,
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
                        tIDSetUniqueDependentChildrenUnderAge10Effected,
                        tCTBRefSetPermanantlyLeftUOButRemainedInSHBE,
                        tCTBRefSetPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged,
                        tCTBRefSetPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased,
                        tCTBRefSetTravellers,
                        tCTBRefSetTTNot1Or4AndUnderOccupying,
                        tCTBRefSetTT1_To_TT3,
                        tCTBRefSetTT4_To_TT3,
                        tCTBRefSetTT3_To_TT1,
                        tCTBRefSetTT3_To_TT4,
                        tCTBRefSetValidPostcodeChange,
                        tCTBRefSetChangedTT,
                        tCTBRefSetUOAtSomePoint,
                        tCTBRefSetUOTT1AtSomePoint,
                        tCTBRefSetUOTT4AtSomePoint,
                        tCTBRefSetAlwaysUOTT1FromStart,
                        tCTBRefSetAlwaysUOTT1FromStartExceptWhenSuspended,
                        tCTBRefSetAlwaysUOTT1FromWhenStarted,
                        tCTBRefSetAlwaysUOTT4FromStart,
                        tCTBRefSetAlwaysUOTT4FromStartExceptWhenSuspended,
                        tCTBRefSetAlwaysUOTT4FromWhenStarted,
                        tCTBRefSetIntermitantUO,
                        tCTBRefSetUO_To_LeftSHBEAtSomePoint,
                        tCTBRefSetUOTT1_To_LeftSHBE,
                        tCTBRefSetUOTT4_To_LeftSHBE,
                        tCTBRefSetUOTT3OrTT6_To_LeftSHBE,
                        tCTBRefSetUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE,
                        //tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT1orTT4,
                        //tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
                        //tCTBRefSetUOTT4_To_LeftSHBEAndHaveNotReturned,
                        //tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT1orTT4,
                        //tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
                        //tCTBRefSetUO_To_LeftSHBETheVeryNextMonth,
                        tCTBRefSetUO_To_LeftSHBETheVeryNextMonth,
                        tCTBRefSetUO_To_LeftSHBEBetweenOneAndTwoMonths,
                        tCTBRefSetUO_To_LeftSHBEBetweenTwoAndThreeMonths,
                        tCTBRefSetUO_To_LeftSHBEAndNotReturned,
                        tCTBRefSetUOTT1_To_LeftSHBEAndNotReturned,
                        tCTBRefSetUOTT4_To_LeftSHBEAndNotReturned,
                        tCTBRefSetUOTT3OrTT6_To_LeftSHBEAndNotReturned,
                        tCTBRefSetUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEAndNotReturned,
                        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT1,
                        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT1,
                        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
                        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT4,
                        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT4,
                        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
                        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT8,
                        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT9,
                        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsUOTT1,
                        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT1,
                        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6,
                        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsUOTT4,
                        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT4,
                        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7,
                        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT8,
                        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT9,
                        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1,
                        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1,
                        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6,
                        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4,
                        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4,
                        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7,
                        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8,
                        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9,
                        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                        tCTBRefSetUO_NotUO,
                        tCTBRefSetUO_NotUO_UO,
                        tCTBRefSetUO_NotUO_UO_NotUO,
                        tCTBRefSetUO_NotUO_UO_NotUO_UO,
                        tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO,
                        tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO,
                        tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO,
                        tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO,
                        tCTBRefSetUOTT1_To_NotUO_InSHBE_PostcodeChanged,
                        tCTBRefSetUOTT1_To_UOTT1_PostcodeChanged,
                        tCTBRefSetUOTT1_To_TT1_PostcodeChanged,
                        tCTBRefSetUOTT1_To_UOTT4_PostcodeChanged,
                        tCTBRefSetUOTT1_To_TT4_PostcodeChanged,
                        tCTBRefSetUOTT4_To_NotUO_InSHBE_PostcodeChanged,
                        tCTBRefSetUOTT4_To_UOTT1_PostcodeChanged,
                        tCTBRefSetUOTT4_To_TT1_PostcodeChanged,
                        tCTBRefSetUOTT4_To_UOTT4_PostcodeChanged,
                        tCTBRefSetUOTT4_To_TT4_PostcodeChanged,
                        tCTBRefSetUOTT1_To_TT3OrTT6,
                        tCTBRefSetUOTT1_To_TT3OrTT6AtSomePoint,
                        tCTBRefSetUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                        tCTBRefSetUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint,
                        tCTBRefSetUOTT1_To_TT3OrTT6NotDoneNextChange,
                        tCTBRefSetUOTT4_To_TT3OrTT6,
                        tCTBRefSetUOTT4_To_TT3OrTT6AtSomePoint,
                        tCTBRefSetUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                        tCTBRefSetUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint,
                        tCTBRefSetUOTT4_To_TT3OrTT6NotDoneNextChange,
                        tCTBRefSetTT3OrTT6_To_UOTT1,
                        tCTBRefSetTT3OrTT6_To_UOTT4,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth,
                        //tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious,
                        tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged3MonthsPrevious,
                        //tCTBRefSetUOTT1OrTT1_To_UOTT4,
                        tCTBRefSetUOTT1_To_UOTT4,
                        tCTBRefSetTT1_To_UOTT4,
                        tCTBRefSetTT1_To_UOTT4GettingDHP,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth,
                        //tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious,
                        tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged3MonthsPrevious,
                        //tCTBRefSetUOTT4OrTT4_To_UOTT1,
                        //tCTBRefSetUOTT4OrTT4_To_UOTT1InArrears,
                        //tCTBRefSetUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
                        tCTBRefSetUOTT4_To_UOTT1,
                        tCTBRefSetUOTT4_To_UOTT1InArrears,
                        tCTBRefSetUOTT4_To_UOTT1GettingDHP,
                        tCTBRefSetUOTT4_To_UOTT1InArrearsAndGettingDHP,
                        tCTBRefSetTT4_To_UOTT1,
                        tCTBRefSetTT4_To_UOTT1InArrears,
                        tCTBRefSetTT4_To_UOTT1GettingDHP,
                        tCTBRefSetTT4_To_UOTT1InArrearsAndGettingDHP,
                        tCTBRefSetInArrearsAtSomePoint,
                        tCTBRefSetDHPAtSomePoint,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth,
                        //tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious,
                        tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged3MonthsPrevious,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth,
                        //tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious,
                        tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged3MonthsPrevious,
                        tCTBRefSetUOTT1_ToTT1_PostcodeChanged,
                        tCTBRefSetUOTT1_ToUOTT1_PostcodeChanged,
                        tCTBRefSetUOTT4_ToTT4_PostcodeChanged,
                        tCTBRefSetUOTT4_ToUOTT4_PostcodeChanged,
                        tCTBRefSetTT1_ToUOTT1_PostcodeChanged,
                        tCTBRefSetTT4_ToUOTT4_PostcodeChanged,
                        tCTBRefSetUOClaimsRecievingDHP,
                        tCTBRefSetUOTT1ClaimsInRentArrearsAtSomePoint,
                        tCTBRefSetUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint);
                if ((Boolean) processResult[0]) {
                    totalCount_UOClaims++;
                }
                if ((Boolean) processResult[1]) {
                    totalCount_UOCouncilClaims++;
                }
                if ((Boolean) processResult[2]) {
                    totalCount_UORSLClaims++;
                }
                if ((Boolean) processResult[3]) {
                    totalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE++;
                }
                if ((Boolean) processResult[4]) {
                    totalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO++;
                }
                totalHouseholdSizeExcludingPartnersCouncilSHBE += (Integer) processResult[5];
                totalHouseholdSizeExcludingPartnersCouncilUO += (Integer) processResult[6];
                totalHouseholdSizeExcludingPartnersRSLSHBE += (Integer) processResult[7];
                totalHouseholdSizeExcludingPartnersRSLUO += (Integer) processResult[8];

            }
            tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged3MonthsPrevious = tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious;
            tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged3MonthsPrevious = tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious;
            tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious = tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious;
            tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious = tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious;
            tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious = tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth;
            tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious = tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth;
            tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth = new TreeSet<String>();
            tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth = new TreeSet<String>();

            tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged3MonthsPrevious = tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious;
            tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged3MonthsPrevious = tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious;
            tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious = tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious;
            tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious = tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious;
            tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious = tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth;
            tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious = tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth;
            tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth = new TreeSet<String>();
            tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth = new TreeSet<String>();

            yearMonth = year + "-" + month;
            totalCounts_cumulativeUniqueClaims.put(
                    yearMonth,
                    tUOClaims.size());
            totalCounts_UOClaims.put(
                    yearMonth,
                    totalCount_UOClaims);
            totalCounts_UOCouncilClaims.put(
                    yearMonth,
                    totalCount_UOCouncilClaims);
            totalCounts_UORSLClaims.put(
                    yearMonth,
                    totalCount_UORSLClaims);
            totalCounts_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE.put(
                    yearMonth,
                    totalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE);
            totalCounts_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO.put(
                    yearMonth,
                    totalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO);
            totalHouseholdSizeExcludingPartnersCouncilSHBEs.put(
                    yearMonth,
                    totalHouseholdSizeExcludingPartnersCouncilSHBE);
            totalHouseholdSizeExcludingPartnersCouncilUOs.put(
                    yearMonth,
                    totalHouseholdSizeExcludingPartnersCouncilUO);
            totalHouseholdSizeExcludingPartnersRSLSHBEs.put(
                    yearMonth,
                    totalHouseholdSizeExcludingPartnersRSLSHBE);
            totalHouseholdSizeExcludingPartnersRSLUOs.put(
                    yearMonth,
                    totalHouseholdSizeExcludingPartnersRSLUO);
        }

        tCTBRefSetInArrearsAtSomePoint_And_DHPAtSomePoint.addAll(tCTBRefSetInArrearsAtSomePoint);
        tCTBRefSetInArrearsAtSomePoint_And_DHPAtSomePoint.retainAll(tCTBRefSetDHPAtSomePoint);

//        tCTBRefSetUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.removeAll(tCTBRefSetUOTT1_To_LeftSHBEAndNotReturned);
//        tCTBRefSetUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.removeAll(tCTBRefSetUOTT4_To_LeftSHBEAndNotReturned);
        tCTBRefSetUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.retainAll(tCTBRefSetUOTT1_To_TT3OrTT6AtSomePoint);
        tCTBRefSetUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.retainAll(tCTBRefSetUOTT4_To_TT3OrTT6AtSomePoint);

        header += DW_Strings.sCommaSpace + "HBDPTotal";

//        TreeSet<String> tCTBRefSetValidPostcodeChange; // Calculate by removing all from tCTBRefSetNoValidPostcodeChange.
//        tCTBRefSetValidPostcodeChange = new TreeSet<String>();
//        tCTBRefSetValidPostcodeChange.addAll(tCTBRefs);
//        tCTBRefSetValidPostcodeChange.removeAll(tCTBRefSetNoValidPostcodeChange);
//        groups.put("ValidPostcodeChange", tCTBRefSetValidPostcodeChange);
        TreeSet<String> tCTBRefSetNoValidPostcodeChange; // Calculate by removing all from tCTBRefSetNoValidPostcodeChange.
        tCTBRefSetNoValidPostcodeChange = new TreeSet<String>();
        tCTBRefSetNoValidPostcodeChange.addAll(tCTBRefs);
        tCTBRefSetNoValidPostcodeChange.removeAll(tCTBRefSetValidPostcodeChange);
        groups.put(sNoValidPostcodeChange, tCTBRefSetValidPostcodeChange);

        TreeSet<String> tCTBRefSetNotChangedTT; // Calculate by removing all from tCTBRefSetChangedTT.
        tCTBRefSetNotChangedTT = new TreeSet<String>();
        tCTBRefSetNotChangedTT.addAll(tCTBRefs);
        tCTBRefSetNotChangedTT.removeAll(tCTBRefSetChangedTT);
        groups.put("NotChangedTT", tCTBRefSetNotChangedTT);

        TreeSet<String> tCTBRefSetAlwaysUOFromStartNoValidPostcodeChangeNotChangedTT; // Calculate by intersect of sets.
        tCTBRefSetAlwaysUOFromStartNoValidPostcodeChangeNotChangedTT = new TreeSet<String>();
        tCTBRefSetAlwaysUOFromStartNoValidPostcodeChangeNotChangedTT.addAll(tCTBRefSetAlwaysUOTT1FromStart);
        tCTBRefSetAlwaysUOFromStartNoValidPostcodeChangeNotChangedTT.addAll(tCTBRefSetAlwaysUOTT4FromStart);
        tCTBRefSetAlwaysUOFromStartNoValidPostcodeChangeNotChangedTT.retainAll(tCTBRefSetNoValidPostcodeChange);
        tCTBRefSetAlwaysUOFromStartNoValidPostcodeChangeNotChangedTT.retainAll(tCTBRefSetNotChangedTT);
        groups.put(sAlwaysUOFromStart__NoValidPostcodeChange_NotChangedTT, tCTBRefSetAlwaysUOFromStartNoValidPostcodeChangeNotChangedTT);

        TreeSet<String> tCTBRefSetAlwaysUOFromStartChangedTT; // Calculate by intersect of sets.
        tCTBRefSetAlwaysUOFromStartChangedTT = new TreeSet<String>();
        tCTBRefSetAlwaysUOFromStartChangedTT.addAll(tCTBRefSetChangedTT);
        tCTBRefSetAlwaysUOFromStartChangedTT.retainAll(tCTBRefSetAlwaysUOTT1FromStart);
        tCTBRefSetAlwaysUOFromStartChangedTT.retainAll(tCTBRefSetAlwaysUOTT4FromStart);
        groups.put(sAlwaysUOFromStart__ChangedTT, tCTBRefSetAlwaysUOFromStartChangedTT);

        TreeSet<String> tCTBRefSetAlwaysUOFromStartValidPostcodeChangeNotChangedTT; // Calculate by intersect of sets.
        tCTBRefSetAlwaysUOFromStartValidPostcodeChangeNotChangedTT = new TreeSet<String>();
        tCTBRefSetAlwaysUOFromStartValidPostcodeChangeNotChangedTT.addAll(tCTBRefSetAlwaysUOTT1FromStart);
        tCTBRefSetAlwaysUOFromStartValidPostcodeChangeNotChangedTT.addAll(tCTBRefSetAlwaysUOTT4FromStart);
        tCTBRefSetAlwaysUOFromStartValidPostcodeChangeNotChangedTT.removeAll(tCTBRefSetAlwaysUOFromStartChangedTT);
        tCTBRefSetAlwaysUOFromStartValidPostcodeChangeNotChangedTT.retainAll(tCTBRefSetValidPostcodeChange);
        groups.put(sAlwaysUOFromStart__ValidPostcodeChange_NotChangedTT, tCTBRefSetAlwaysUOFromStartValidPostcodeChangeNotChangedTT);

        String aS;
        String key;
        Iterator<String> ite;
        ite = tCTBRefs.iterator();
        while (ite.hasNext()) {
            aCTBRef = ite.next();
            key = aCTBRef + DW_Strings.sUnderscore + sUnderOccupancy;
            aS = tableValues.get(key);
            if (aS.endsWith(DW_Strings.sCommaSpace)) {
                tCTBRefSetIntermitantUO.add(aCTBRef);
            }
        }

        tCTBRefSetAlwaysUOTT1FromWhenStarted.removeAll(tCTBRefSetAlwaysUOTT1FromStart);
        tCTBRefSetAlwaysUOTT1FromWhenStarted.removeAll(tCTBRefSetIntermitantUO);
        tCTBRefSetAlwaysUOTT4FromWhenStarted.removeAll(tCTBRefSetAlwaysUOTT4FromStart);
        tCTBRefSetAlwaysUOTT4FromWhenStarted.removeAll(tCTBRefSetIntermitantUO);

        TreeSet<String> tCTBRefSetAlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT; // Calculate by intersect of sets.
        tCTBRefSetAlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT = new TreeSet<String>();
        tCTBRefSetAlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT.addAll(tCTBRefSetAlwaysUOTT1FromWhenStarted);
        tCTBRefSetAlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT.addAll(tCTBRefSetAlwaysUOTT4FromWhenStarted);
        tCTBRefSetAlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT.removeAll(tCTBRefSetChangedTT);
        tCTBRefSetAlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT.retainAll(tCTBRefSetNoValidPostcodeChange);
        groups.put(sAlwaysUOFromWhenStarted__NoValidPostcodeChange_NotChangedTT, tCTBRefSetAlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT);

        TreeSet<String> tCTBRefSetAlwaysUOFromWhenStartedChangedTT; // Calculate by intersect of sets.
        tCTBRefSetAlwaysUOFromWhenStartedChangedTT = new TreeSet<String>();
        tCTBRefSetAlwaysUOFromWhenStartedChangedTT.addAll(tCTBRefSetAlwaysUOTT1FromWhenStarted);
        tCTBRefSetAlwaysUOFromWhenStartedChangedTT.addAll(tCTBRefSetAlwaysUOTT4FromWhenStarted);
        tCTBRefSetAlwaysUOFromWhenStartedChangedTT.retainAll(tCTBRefSetChangedTT);
        groups.put(sAlwaysUOFromWhenStarted__ChangedTT, tCTBRefSetAlwaysUOFromWhenStartedChangedTT);

        TreeSet<String> tCTBRefSetAlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT; // Calculate by intersect of sets.
        tCTBRefSetAlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT = new TreeSet<String>();
        tCTBRefSetAlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT.addAll(tCTBRefSetAlwaysUOTT1FromWhenStarted);
        tCTBRefSetAlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT.addAll(tCTBRefSetAlwaysUOTT4FromWhenStarted);
        tCTBRefSetAlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT.removeAll(tCTBRefSetNoValidPostcodeChange);
        tCTBRefSetAlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT.removeAll(tCTBRefSetChangedTT);
        groups.put(sAlwaysUOFromWhenStarted__ValidPostcodeChange_NotChangedTT, tCTBRefSetAlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT);

        TreeSet<String> tCTBRefSetIntermitantUONoValidPostcodeChangeNotChangedTT;
        tCTBRefSetIntermitantUONoValidPostcodeChangeNotChangedTT = new TreeSet<String>();
        tCTBRefSetIntermitantUONoValidPostcodeChangeNotChangedTT.addAll(tCTBRefSetIntermitantUO);
        tCTBRefSetIntermitantUONoValidPostcodeChangeNotChangedTT.retainAll(tCTBRefSetNoValidPostcodeChange);
        tCTBRefSetIntermitantUONoValidPostcodeChangeNotChangedTT.retainAll(tCTBRefSetNotChangedTT);
        groups.put(sIntermitantUO__NoValidPostcodeChange_NotChangedTT, tCTBRefSetIntermitantUONoValidPostcodeChangeNotChangedTT);

        TreeSet<String> tCTBRefSetIntermitantUOChangedTT;
        tCTBRefSetIntermitantUOChangedTT = new TreeSet<String>();
        tCTBRefSetIntermitantUOChangedTT.addAll(tCTBRefSetIntermitantUO);
        tCTBRefSetIntermitantUOChangedTT.retainAll(tCTBRefSetChangedTT);
        groups.put(sIntermitantUO__ChangedTT, tCTBRefSetIntermitantUOChangedTT);

        TreeSet<String> tCTBRefSetIntermitantUOValidPostcodeChangeNotChangedTT;
        tCTBRefSetIntermitantUOValidPostcodeChangeNotChangedTT = new TreeSet<String>();
        tCTBRefSetIntermitantUOValidPostcodeChangeNotChangedTT.addAll(tCTBRefSetIntermitantUO);
        tCTBRefSetIntermitantUOValidPostcodeChangeNotChangedTT.removeAll(tCTBRefSetNoValidPostcodeChange);
        tCTBRefSetIntermitantUOValidPostcodeChangeNotChangedTT.removeAll(tCTBRefSetChangedTT);
        groups.put(sIntermitantUO__ValidPostcodeChange_NotChangedTT, tCTBRefSetIntermitantUOValidPostcodeChangeNotChangedTT);

        checkSetsAndAddToGeneralStatistics(
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
                tIDSetUniqueDependentChildrenUnderAge10Effected,
                groups);

        long totalHouseholdSize;
        double averageHouseholdSizeOfThoseUOAlwaysFromStart;
        double d;
        Iterator<String> iteS;
        // TT1
        totalHouseholdSize = 0;
        d = 0.0d;
        iteS = tCTBRefSetAlwaysUOTT1FromStart.iterator();
        while (iteS.hasNext()) {
            aCTBRef = iteS.next();
            DW_SHBE_Record rec = aRecords.get(aCTBRef);
            if (rec != null) {
                totalHouseholdSize += DW_SHBE_Handler.getHouseholdSize(rec);
                d += 1.0d;
            }
        }
        if (d > 0) {
            //averageHouseholdSizeOfThoseUOAlwaysFromStart = (double) totalHouseholdSize / (double) tCTBRefSetAlwaysUOTT1FromStart.size();
            averageHouseholdSizeOfThoseUOAlwaysFromStart = (double) totalHouseholdSize / d;
        } else {
            averageHouseholdSizeOfThoseUOAlwaysFromStart = 0.0d;
        }
        generalStatistics.put(sAverageHouseholdSizeOfThoseUOTT1AlwaysFromStart,
                BigDecimal.valueOf(averageHouseholdSizeOfThoseUOAlwaysFromStart));
        // TT4
        totalHouseholdSize = 0;
        d = 0.0d;
        iteS = tCTBRefSetAlwaysUOTT4FromStart.iterator();
        while (iteS.hasNext()) {
            aCTBRef = iteS.next();
            DW_SHBE_Record rec = aRecords.get(aCTBRef);
            if (rec != null) {
                totalHouseholdSize += DW_SHBE_Handler.getHouseholdSize(rec);
                d += 1.0d;
            }
        }
        if (d > 0) {
            //averageHouseholdSizeOfThoseUOAlwaysFromStart = (double) totalHouseholdSize / (double) tCTBRefSetAlwaysUOTT4FromStart.size();
            averageHouseholdSizeOfThoseUOAlwaysFromStart = (double) totalHouseholdSize / d;
        } else {
            averageHouseholdSizeOfThoseUOAlwaysFromStart = 0.0d;
        }
        generalStatistics.put(sAverageHouseholdSizeOfThoseUOTT4AlwaysFromStart,
                BigDecimal.valueOf(averageHouseholdSizeOfThoseUOAlwaysFromStart));

        generalStatistics.put(sTotalCount_AlwaysUOTT1FromStart,
                BigDecimal.valueOf(tCTBRefSetAlwaysUOTT1FromStart.size()));
        generalStatistics.put(sTotalCount_AlwaysUOTT1FromStartExceptWhenSuspended,
                BigDecimal.valueOf(tCTBRefSetAlwaysUOTT1FromStartExceptWhenSuspended.size()));
        generalStatistics.put(sTotalCount_AlwaysUOTT1FromWhenStarted,
                BigDecimal.valueOf(tCTBRefSetAlwaysUOTT1FromWhenStarted.size()));
        generalStatistics.put(sTotalCount_AlwaysUOTT4FromStart,
                BigDecimal.valueOf(tCTBRefSetAlwaysUOTT4FromStart.size()));
        generalStatistics.put(sTotalCount_AlwaysUOTT4FromStartExceptWhenSuspended,
                BigDecimal.valueOf(tCTBRefSetAlwaysUOTT4FromStartExceptWhenSuspended.size()));
        generalStatistics.put(sTotalCount_AlwaysUOTT4FromWhenStarted,
                BigDecimal.valueOf(tCTBRefSetAlwaysUOTT4FromWhenStarted.size()));

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
        result[7] = timeStatistics;
        return result;
    }

    protected void checkSetsAndAddToGeneralStatistics(
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
            HashSet<ID> tIDSetUniqueDependentChildrenUnderAge10Effected,
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
        generalStatistics.put(sUO_To_LeftSHBEAtSomePoint,
                BigDecimal.valueOf(groups.get(sUO_To_LeftSHBEAtSomePoint).size()));
        generalStatistics.put(
                sUOTT1_To_LeftSHBE,
                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBE).size()));
        generalStatistics.put(
                sUOTT4_To_LeftSHBE,
                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBE).size()));
        generalStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE,
                BigDecimal.valueOf(groups.get(sUOTT3OrTT6_To_LeftSHBE).size()));
        generalStatistics.put(
                sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE,
                BigDecimal.valueOf(groups.get(sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE).size()));
        generalStatistics.put(sUO_To_LeftSHBE,
                BigDecimal.valueOf(groups.get(sUO_To_LeftSHBE).size()));
        generalStatistics.put(
                sUO_To_LeftSHBEBetweenOneAndTwoMonths,
                BigDecimal.valueOf(groups.get(sUO_To_LeftSHBEBetweenOneAndTwoMonths).size()));
        generalStatistics.put(
                sUO_To_LeftSHBEBetweenTwoAndThreeMonths,
                BigDecimal.valueOf(groups.get(sUO_To_LeftSHBEBetweenTwoAndThreeMonths).size()));
        generalStatistics.put(sUO_To_LeftSHBE_NotReturned,
                BigDecimal.valueOf(groups.get(sUO_To_LeftSHBE_NotReturned).size()));
        generalStatistics.put(sUOTT1_To_LeftSHBE_NotReturned,
                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBE_NotReturned).size()));
        generalStatistics.put(sUOTT4_To_LeftSHBE_NotReturned,
                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBE_NotReturned).size()));
        generalStatistics.put(sUOTT3OrTT6_To_LeftSHBE_NotReturned,
                BigDecimal.valueOf(groups.get(sUOTT3OrTT6_To_LeftSHBE_NotReturned).size()));
        generalStatistics.put(sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned,
                BigDecimal.valueOf(groups.get(sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned).size()));
        generalStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAsUOTT1,
                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBE_ReturnedAsUOTT1).size()));
        generalStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT1,
                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBE_ReturnedAsTT1).size()));
        generalStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6).size()));
        generalStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAsUOTT4,
                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBE_ReturnedAsUOTT4).size()));
        generalStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT4,
                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBE_ReturnedAsTT4).size()));
        generalStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7).size()));
        generalStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT8,
                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBE_ReturnedAsTT8).size()));
        generalStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT9,
                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBE_ReturnedAsTT9).size()));
        generalStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAsUOTT1,
                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBE_ReturnedAsUOTT1).size()));
        generalStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT1,
                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBE_ReturnedAsTT1).size()));
        generalStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6,
                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6).size()));
        generalStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAsUOTT4,
                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBE_ReturnedAsUOTT4).size()));
        generalStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT4,
                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBE_ReturnedAsTT4).size()));
        generalStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7,
                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7).size()));
        generalStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT8,
                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBE_ReturnedAsTT8).size()));
        generalStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT9,
                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBE_ReturnedAsTT9).size()));
        generalStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1,
                BigDecimal.valueOf(groups.get(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1).size()));
        generalStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1,
                BigDecimal.valueOf(groups.get(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1).size()));
        generalStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6,
                BigDecimal.valueOf(groups.get(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6).size()));
        generalStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4,
                BigDecimal.valueOf(groups.get(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4).size()));
        generalStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4,
                BigDecimal.valueOf(groups.get(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4).size()));
        generalStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7,
                BigDecimal.valueOf(groups.get(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7).size()));
        generalStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8,
                BigDecimal.valueOf(groups.get(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8).size()));
        generalStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9,
                BigDecimal.valueOf(groups.get(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9).size()));
        generalStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint).size()));
        generalStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint).size()));
//                sUOTT1_To_LeftSHBE,
//                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBE).size()));
//        generalStatistics.put(
//                sUOTT1_To_LeftSHBE,
//                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBE).size()));
//        generalStatistics.put(
//                sUOTT1_To_LeftSHBEAndHaveNotReturned,
//                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBEAndHaveNotReturned).size()));
//        generalStatistics.put(
//                sUOTT1_To_LeftSHBEReturnedAsTT1orTT4,
//                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBEReturnedAsTT1orTT4).size()));
//        generalStatistics.put(
//                sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
//                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6).size()));
//        generalStatistics.put(
//                sUOTT4_To_LeftSHBE,
//                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBE).size()));
//        generalStatistics.put(
//                sUOTT4_To_LeftSHBEAndHaveNotReturned,
//                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBEAndHaveNotReturned).size()));
//        generalStatistics.put(
//                sUOTT4_To_LeftSHBEReturnedAsTT1orTT4,
//                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBEReturnedAsTT1orTT4).size()));
//        generalStatistics.put(
//                sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
//                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6).size()));

        generalStatistics.put(
                sUO_NotUO_UO,
                BigDecimal.valueOf(
                        groups.get(sUO_NotUO_UO).size()
                        + groups.get(sUO_NotUO_UO_NotUO).size()));
        generalStatistics.put(
                sUO_NotUO_UO_NotUO_UO,
                BigDecimal.valueOf(
                        groups.get(sUO_NotUO_UO_NotUO_UO).size()
                        + groups.get(sUO_NotUO_UO_NotUO_UO_NotUO).size()));
        generalStatistics.put(
                sUO_NotUO_UO_NotUO_UO_NotUO_UO,
                BigDecimal.valueOf(groups.get(sUO_NotUO_UO_NotUO_UO_NotUO_UO).size()
                        + groups.get(sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO).size()));
        generalStatistics.put(
                sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO,
                BigDecimal.valueOf(groups.get(sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO).size()));

        generalStatistics.put(
                sUOTT1_To_NotUO_InSHBE_PostcodeChanged,
                BigDecimal.valueOf(groups.get(sUOTT1_To_NotUO_InSHBE_PostcodeChanged).size()));
        generalStatistics.put(
                sUOTT1_To_TT1_PostcodeChanged,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeChanged).size()));
        generalStatistics.put(
                sUOTT1_To_TT4_PostcodeChanged,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT4_PostcodeChanged).size()));
        generalStatistics.put(
                sUOTT4_To_NotUO_InSHBE_PostcodeChanged,
                BigDecimal.valueOf(groups.get(sUOTT4_To_NotUO_InSHBE_PostcodeChanged).size()));
        generalStatistics.put(
                sUOTT4_To_TT1_PostcodeChanged,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT1_PostcodeChanged).size()));
        generalStatistics.put(
                sUOTT4_To_TT4_PostcodeChanged,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeChanged).size()));

        generalStatistics.put(
                sUOTT1_To_TT3OrTT6,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT3OrTT6).size()));
        generalStatistics.put(
                sUOTT1_To_TT3OrTT6AtSomePoint,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT3OrTT6AtSomePoint).size()));
        generalStatistics.put(
                sUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999).size()));
        generalStatistics.put(
                sUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint).size()));

        generalStatistics.put(
                sUOTT4_To_TT3OrTT6,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT3OrTT6).size()));
        generalStatistics.put(
                sUOTT4_To_TT3OrTT6AtSomePoint,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT3OrTT6AtSomePoint).size()));
        generalStatistics.put(
                sUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999).size()));
        generalStatistics.put(
                sUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint).size()));

        generalStatistics.put(
                sTT3OrTT6_To_UOTT1,
                BigDecimal.valueOf(groups.get(sTT3OrTT6_To_UOTT1).size()));
        generalStatistics.put(
                sTT3OrTT6_To_UOTT4,
                BigDecimal.valueOf(groups.get(sTT3OrTT6_To_UOTT4).size()));

        generalStatistics.put(sTT1_To_UOTT1_PostcodeUnchanged,
                BigDecimal.valueOf(groups.get(sTT1_To_UOTT1_PostcodeUnchanged).size()));
//        generalStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
//                BigDecimal.valueOf(groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month).size()));
        generalStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                BigDecimal.valueOf(groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1).size()));
        generalStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                BigDecimal.valueOf(groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1).size()));
        generalStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                BigDecimal.valueOf(groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6).size()));
        generalStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                BigDecimal.valueOf(groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4).size()));
        generalStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                BigDecimal.valueOf(groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4).size()));
        generalStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                BigDecimal.valueOf(groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7).size()));
        generalStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                BigDecimal.valueOf(groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8).size()));
        generalStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                BigDecimal.valueOf(groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9).size()));
        generalStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
                BigDecimal.valueOf(groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months).size()));
        generalStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months,
                BigDecimal.valueOf(groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months).size()));

        //generalStatistics.put(sUOTT1OrTT1_To_UOTT4,
        //        BigDecimal.valueOf(groups.get(sUOTT1OrTT1_To_UOTT4).size()));
        generalStatistics.put(sUOTT1_To_UOTT4,
                BigDecimal.valueOf(groups.get(sUOTT1_To_UOTT4).size()));
        generalStatistics.put(sTT1_To_UOTT4,
                BigDecimal.valueOf(groups.get(sTT1_To_UOTT4).size()));
        generalStatistics.put(sTT1_To_UOTT4GettingDHP,
                BigDecimal.valueOf(groups.get(sTT1_To_UOTT4GettingDHP).size()));
//        generalStatistics.put(sTT1_To_UOTT4InArrears,
//                BigDecimal.valueOf(groups.get(sTT1_To_UOTT4InArrears).size()));
//        generalStatistics.put(sTT1_To_UOTT4InArrearsAndGettingDHP,
//                BigDecimal.valueOf(groups.get(sTT1_To_UOTT4InArrearsAndGettingDHP).size()));

        generalStatistics.put(sTT4_To_UOTT4_PostcodeUnchanged,
                BigDecimal.valueOf(groups.get(sTT4_To_UOTT4_PostcodeUnchanged).size()));
//        generalStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
//                BigDecimal.valueOf(groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month).size()));
        generalStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                BigDecimal.valueOf(groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1).size()));
        generalStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                BigDecimal.valueOf(groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6).size()));
        generalStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                BigDecimal.valueOf(groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4).size()));
        generalStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                BigDecimal.valueOf(groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7).size()));
        generalStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                BigDecimal.valueOf(groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8).size()));
        generalStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                BigDecimal.valueOf(groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9).size()));
        generalStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
                BigDecimal.valueOf(groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months).size()));
        generalStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months,
                BigDecimal.valueOf(groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months).size()));

        //generalStatistics.put(sUOTT4OrTT4_To_UOTT1,
        //        BigDecimal.valueOf(groups.get(sUOTT4OrTT4_To_UOTT1).size()));
        //generalStatistics.put(sUOTT4OrTT4_To_UOTT1InArrears,
        //        BigDecimal.valueOf(groups.get(sUOTT4OrTT4_To_UOTT1InArrears).size()));
        //generalStatistics.put(sUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
        //        BigDecimal.valueOf(groups.get(sUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP).size()));
        generalStatistics.put(sUOTT4_To_UOTT1,
                BigDecimal.valueOf(groups.get(sUOTT4_To_UOTT1).size()));
        generalStatistics.put(sUOTT4_To_UOTT1InArrears,
                BigDecimal.valueOf(groups.get(sUOTT4_To_UOTT1InArrears).size()));
        generalStatistics.put(sUOTT4_To_UOTT1GettingDHP,
                BigDecimal.valueOf(groups.get(sUOTT4_To_UOTT1GettingDHP).size()));
        generalStatistics.put(sUOTT4_To_UOTT1InArrearsAndGettingDHP,
                BigDecimal.valueOf(groups.get(sUOTT4_To_UOTT1InArrearsAndGettingDHP).size()));
        generalStatistics.put(sTT4_To_UOTT1,
                BigDecimal.valueOf(groups.get(sTT4_To_UOTT1).size()));
        generalStatistics.put(sTT4_To_UOTT1InArrears,
                BigDecimal.valueOf(groups.get(sTT4_To_UOTT1InArrears).size()));
        generalStatistics.put(sTT4_To_UOTT1GettingDHP,
                BigDecimal.valueOf(groups.get(sTT4_To_UOTT1GettingDHP).size()));
        generalStatistics.put(sTT4_To_UOTT1InArrearsAndGettingDHP,
                BigDecimal.valueOf(groups.get(sTT4_To_UOTT1InArrearsAndGettingDHP).size()));

        generalStatistics.put(sUOTT1_To_TT1_PostcodeUnchanged,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeUnchanged).size()));
//        generalStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
//                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month).size()));
        generalStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1).size()));
        generalStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1).size()));
        generalStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6).size()));
        generalStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4).size()));
        generalStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4).size()));
        generalStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7).size()));
        generalStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8).size()));
        generalStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9).size()));
        generalStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months).size()));
        generalStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months).size()));

        generalStatistics.put(sUOTT4_To_TT4_PostcodeUnchanged,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeUnchanged).size()));
//        generalStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
//                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month).size()));
        generalStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1).size()));
        generalStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1).size()));
        generalStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6).size()));
        generalStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4).size()));
        generalStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4).size()));
        generalStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7).size()));
        generalStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8).size()));
        generalStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9).size()));
        generalStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months).size()));
        generalStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months).size()));

        generalStatistics.put(sUOTT1_To_TT1_PostcodeChanged,
                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeChanged).size()));
        generalStatistics.put(sUOTT1_To_UOTT1_PostcodeChanged,
                BigDecimal.valueOf(groups.get(sUOTT1_To_UOTT1_PostcodeChanged).size()));
        generalStatistics.put(sUOTT4_To_TT4_PostcodeChanged,
                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeChanged).size()));
        generalStatistics.put(sUOTT4_To_UOTT4_PostcodeChanged,
                BigDecimal.valueOf(groups.get(sUOTT4_To_UOTT4_PostcodeChanged).size()));
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
        generalStatistics.put(
                sTotalCount_UniqueChildrenAgeLessThan10EffectedByUnderOccupancy,
                BigDecimal.valueOf(tIDSetUniqueDependentChildrenUnderAge10Effected.size()));

        generalStatistics.put(
                sUOClaimsRecievingDHP,
                BigDecimal.valueOf(groups.get(sUOClaimsRecievingDHP).size()));
        generalStatistics.put(
                sUOTT1ClaimsInRentArrearsAtSomePoint,
                BigDecimal.valueOf(groups.get(sUOTT1ClaimsInRentArrearsAtSomePoint).size()));

        generalStatistics.put(
                sUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint,
                BigDecimal.valueOf(groups.get(sUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint).size()));
        generalStatistics.put(
                sInArrearsAtSomePoint_And_DHPAtSomePoint,
                BigDecimal.valueOf(groups.get(sInArrearsAtSomePoint_And_DHPAtSomePoint).size()));

        generalStatistics.put(
                sPermanantlyLeftUOButRemainedInSHBE,
                BigDecimal.valueOf(groups.get(sPermanantlyLeftUOButRemainedInSHBE).size()));
        generalStatistics.put(
                sPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged,
                BigDecimal.valueOf(groups.get(sPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged).size()));
        generalStatistics.put(
                sPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased,
                BigDecimal.valueOf(groups.get(sPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased).size()));
    }

    public Object[] process(
            TreeSet<String> tUOClaims,
            TreeMap<String, BigDecimal> aggregateStatistics,
            TreeMap<String, BigDecimal> generalStatistics,
            String aCTBRef,
            String year,
            String month,
            String aYM3,
            DW_SHBE_Record aDW_SHBE_Record,
            TreeMap<String, DW_SHBE_Record> bRecords,
            TreeMap<String, DW_SHBE_Record> cRecords,
            TreeMap<String, String> tableValues,
            DW_UO_Set councilUnderOccupiedSet1,
            DW_UO_Set RSLUnderOccupiedSet1,
            HashSet<ID> tIDSetCouncilUniqueClaimantsEffected,
            HashSet<ID> tIDSetCouncilUniquePartnersEffected,
            HashMap<String, Integer> tCouncilMaxNumberOfDependentsInClaimWhenUO,
            HashSet<ID> tIDSetCouncilUniqueNonDependentsEffected,
            HashSet<ID> tIDSetRSLUniqueClaimantsEffected,
            HashSet<ID> tIDSetRSLUniquePartnersEffected,
            HashMap<String, Integer> tRSLMaxNumberOfDependentsInClaimWhenUO,
            HashSet<ID> tIDSetRSLUniqueNonDependentsEffected,
            HashSet<ID> tIDSetUniqueDependentChildrenUnderAge10Effected,
            TreeSet<String> tCTBRefSetPermanantlyLeftUOButRemainedInSHBE,
            TreeSet<String> tCTBRefSetPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged,
            TreeSet<String> tCTBRefSetPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased,
            TreeSet<String> tCTBRefSetTraveller,
            TreeSet<String> tCTBRefSetTTNot1Or4AndUnderOccupying,
            TreeSet<String> tCTBRefSetTT1_To_TT3OrTT6,
            TreeSet<String> tCTBRefSetTT4_To_TT3OrTT6,
            TreeSet<String> tCTBRefSetTT3OrTT6_To_TT1,
            TreeSet<String> tCTBRefSetTT3OrTT6_To_TT4,
            TreeSet<String> tCTBRefSetValidPostcodeChange,
            TreeSet<String> tCTBRefSetChangedTT,
            TreeSet<String> tCTBRefSetUOAtSomePoint,
            TreeSet<String> tCTBRefSetUOTT1AtSomePoint,
            TreeSet<String> tCTBRefSetUOTT4AtSomePoint,
            TreeSet<String> tCTBRefSetAlwaysUOTT1FromStart,
            TreeSet<String> tCTBRefSetAlwaysUOTT1FromStartExceptWhenSuspended,
            TreeSet<String> tCTBRefSetAlwaysUOTT1FromWhenStarted,
            TreeSet<String> tCTBRefSetAlwaysUOTT4FromStart,
            TreeSet<String> tCTBRefSetAlwaysUOTT4FromStartExceptWhenSuspended,
            TreeSet<String> tCTBRefSetAlwaysUOTT4FromWhenStarted,
            TreeSet<String> tCTBRefSetIntermitantUO,
            TreeSet<String> tCTBRefSetUO_To_LeftSHBEAtSomePoint,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE,
            TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE,
            TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE,
            TreeSet<String> tCTBRefSetUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE,
            TreeSet<String> tCTBRefSetUO_To_LeftSHBETheVeryNextMonth,
            TreeSet<String> tCTBRefSetUO_To_LeftSHBEBetweenOneAndTwoMonths,
            TreeSet<String> tCTBRefSetUO_To_LeftSHBEBetweenTwoAndThreeMonths,
            TreeSet<String> tCTBRefSetUO_To_LeftSHBE_NotReturned,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_NotReturned,
            TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_NotReturned,
            TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE_NotReturned,
            TreeSet<String> tCTBRefSetUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT1,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT1,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT4,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT4,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT8,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT9,
            TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsUOTT1,
            TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT1,
            TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6,
            TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsUOTT4,
            TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT4,
            TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7,
            TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT8,
            TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT9,
            TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1,
            TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1,
            TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6,
            TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4,
            TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4,
            TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7,
            TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8,
            TreeSet<String> tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
            TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
            //TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT1orTT4,
            //TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
            //TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT1orTT4,
            //TreeSet<String> tCTBRefSetUOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
            TreeSet<String> tCTBRefSetUO_NotUO,
            TreeSet<String> tCTBRefSetUO_NotUO_UO,
            TreeSet<String> tCTBRefSetUO_NotUO_UO_NotUO,
            TreeSet<String> tCTBRefSetUO_NotUO_UO_NotUO_UO,
            TreeSet<String> tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO,
            TreeSet<String> tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO,
            TreeSet<String> tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO,
            TreeSet<String> tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO,
            TreeSet<String> tCTBRefSetUOTT1_To_NotUO_InSHBE_PostcodeChanged,
            TreeSet<String> tCTBRefSetUOTT1_To_UOTT1_PostcodeChanged,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeChanged,
            TreeSet<String> tCTBRefSetUOTT1_To_UOTT4_PostcodeChanged,
            TreeSet<String> tCTBRefSetUOTT1_To_TT4_PostcodeChanged,
            TreeSet<String> tCTBRefSetUOTT4_To_NotUO_InSHBE_PostcodeChanged,
            TreeSet<String> tCTBRefSetUOTT4_To_UOTT1_PostcodeChanged,
            TreeSet<String> tCTBRefSetUOTT4_To_TT1_PostcodeChanged,
            TreeSet<String> tCTBRefSetUOTT4_To_UOTT4_PostcodeChanged,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeChanged,
            TreeSet<String> tCTBRefSetUOTT1_To_TT3OrTT6,
            TreeSet<String> tCTBRefSetUOTT1_To_TT3OrTT6AtSomePoint,
            TreeSet<String> tCTBRefSetUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
            TreeSet<String> tCTBRefSetUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint,
            TreeSet<String> tCTBRefSetUOTT1_To_TT3OrTT6NotDoneNextChange,
            TreeSet<String> tCTBRefSetUOTT4_To_TT3OrTT6,
            TreeSet<String> tCTBRefSetUOTT4_To_TT3OrTT6AtSomePoint,
            TreeSet<String> tCTBRefSetUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
            TreeSet<String> tCTBRefSetUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint,
            TreeSet<String> tCTBRefSetUOTT4_To_TT3OrTT6NotDoneNextChange,
            TreeSet<String> tCTBRefSetTT3OrTT6_To_UOTT1,
            TreeSet<String> tCTBRefSetTT3OrTT6_To_UOTT4,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth,
            //TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious,
            TreeSet<String> tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged3MonthsPrevious,
            //TreeSet<String> tCTBRefSetUOTT1OrTT1_To_UOTT4,
            TreeSet<String> tCTBRefSetUOTT1_To_UOTT4,
            TreeSet<String> tCTBRefSetTT1_To_UOTT4,
            TreeSet<String> tCTBRefSetTT1_To_UOTT4GettingDHP,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth,
            //TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious,
            TreeSet<String> tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged3MonthsPrevious,
            //TreeSet<String> tCTBRefSetUOTT4OrTT4_To_UOTT1,
            //TreeSet<String> tCTBRefSetUOTT4OrTT4_To_UOTT1InArrears,
            //TreeSet<String> tCTBRefSetUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
            TreeSet<String> tCTBRefSetUOTT4_To_UOTT1,
            TreeSet<String> tCTBRefSetUOTT4_To_UOTT1InArrears,
            TreeSet<String> tCTBRefSetUOTT4_To_UOTT1GettingDHP,
            TreeSet<String> tCTBRefSetUOTT4_To_UOTT1InArrearsAndGettingDHP,
            TreeSet<String> tCTBRefSetTT4_To_UOTT1,
            TreeSet<String> tCTBRefSetTT4_To_UOTT1InArrears,
            TreeSet<String> tCTBRefSetTT4_To_UOTT1GettingDHP,
            TreeSet<String> tCTBRefSetTT4_To_UOTT1InArrearsAndGettingDHP,
            TreeSet<String> tCTBRefSetInArrearsAtSomePoint,
            TreeSet<String> tCTBRefSetDHPAtSomePoint,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth,
            //TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious,
            TreeSet<String> tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged3MonthsPrevious,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedThisMonth,
            //TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious,
            TreeSet<String> tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged3MonthsPrevious,
            TreeSet<String> tCTBRefSetUOTT1_ToTT1_PostcodeChanged,
            TreeSet<String> tCTBRefSetUOTT1_ToUOTT1_PostcodeChanged,
            TreeSet<String> tCTBRefSetUOTT4_ToTT4_PostcodeChanged,
            TreeSet<String> tCTBRefSetUOTT4_ToUOTT4_PostcodeChanged,
            TreeSet<String> tCTBRefSetTT1_ToUOTT1_PostcodeChanged,
            TreeSet<String> tCTBRefSetTT4_ToUOTT4_PostcodeChanged,
            TreeSet<String> tCTBRefSetUOClaimsRecievingDHP,
            TreeSet<String> tCTBRefSetUOTT1ClaimsInRentArrearsAtSomePoint,
            TreeSet<String> tCTBRefSetUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint
    ) {
        Object[] result = new Object[9];
        result[0] = false; // UnderOcuupied
        result[1] = false; // UnderOcuupied
        result[2] = false; // UnderOcuupied
        result[3] = false; // 
        result[4] = false; // 
        result[5] = 0; // 
        result[6] = 0; // 
        result[7] = 0; // 
        result[8] = 0; //
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();
        int cumulativeClaims;
        String aS;
        String key;
        // Declare
        DW_SHBE_D_Record aDW_SHBE_D_Record = null;
        boolean isPairedRecord;
        int aTT;
        String aPC;
        //HashSet<String> validPostcodesYM3;
        //if (validPostcodes.containsKey(aYM3)) {
        //    validPostcodesYM3 = validPostcodes.get(aYM3);
        //} else {
        //    validPostcodesYM3 = new HashSet<String>();
        //    validPostcodes.put(aYM3, validPostcodesYM3);
        //}
        int aStatus;
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
        int bStatus;
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
        DW_SHBE_D_Record cDW_SHBE_D_Record;
        int cTT;
        String cPC;
        int cStatus;
        DW_SHBE_Record cDW_SHBE_Record;
        if (cRecords == null) {
            cDW_SHBE_Record = null;
        } else {
            cDW_SHBE_Record = cRecords.get(aCTBRef);
        }
        // Init
        if (aDW_SHBE_Record == null) {
            isPairedRecord = false;
            aTT = DW_SHBE_TenancyType_Handler.iMinus999;
            aPC = defaultPostcode;
            aStatus = 0;
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
            // Add postcode to validPostcodes if it is valid
            if (aPC.isEmpty()) {
                aPC = defaultPostcode;
            } else //if (!validPostcodesYM3.contains(aPC)) {
            //    if(DW_Postcode_Handler.isValidPostcode(aYM3, aPC)) {
            //        validPostcodesYM3.add(aPC);
            //    }
            //}
            if (!validPostcodes.contains(aPC)) {
                if (tDW_Postcode_Handler.isValidPostcode(
                        tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(aYM3), aPC)) {
                    validPostcodes.add(aPC);
                }
            }
            aStatus = aDW_SHBE_D_Record.getStatusOfHBClaimAtExtractDate();
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
            bStatus = 0;
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
            bStatus = bDW_SHBE_D_Record.getStatusOfHBClaimAtExtractDate();
            bWHBE = bDW_SHBE_D_Record.getWeeklyHousingBenefitEntitlement();
            bWERA = bDW_SHBE_D_Record.getWeeklyEligibleRentAmount();
            bPSI = bDW_SHBE_D_Record.getPassportedStandardIndicator();
            bSHBC = bDW_SHBE_D_Record.getStatusOfHBClaimAtExtractDate();
            bRTHBCC = bDW_SHBE_D_Record.getReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective();
            //bCEG = bDW_SHBE_D_Record.getClaimantsEthnicGroup();
            bCEG = DW_SHBE_Handler.getEthnicityGroup(bDW_SHBE_D_Record);
            bHS = DW_SHBE_Handler.getHouseholdSize(bDW_SHBE_D_Record);
            bND = bDW_SHBE_D_Record.getNumberOfNonDependents();
            bCD = bDW_SHBE_D_Record.getNumberOfChildDependents();
            bPDD = bDW_SHBE_D_Record.getPartnersDateOfDeath();
            bCDoB = bDW_SHBE_D_Record.getClaimantsDateOfBirth();
            bPDoB = bDW_SHBE_D_Record.getPartnersDateOfBirth();
            bCA = DW_SHBE_Handler.getClaimantsAge(year, month, bDW_SHBE_D_Record);
            bPA = DW_SHBE_Handler.getPartnersAge(year, month, bDW_SHBE_D_Record);
        }
        if (cDW_SHBE_Record == null) {
            cTT = DW_SHBE_TenancyType_Handler.iMinus999;
            cPC = defaultPostcode;
            cStatus = 0;
        } else {
            cDW_SHBE_D_Record = cDW_SHBE_Record.getDRecord();
            cTT = cDW_SHBE_D_Record.getTenancyType();
            cPC = cDW_SHBE_D_Record.getClaimantsPostcode();
            if (cPC.isEmpty()) {
                cPC = defaultPostcode;
            }
            cStatus = cDW_SHBE_D_Record.getStatusOfHBClaimAtExtractDate();
        }
        key = aCTBRef + DW_Strings.sUnderscore + sUnderOccupancy;
        aS = tableValues.get(key);

        boolean wasUOBefore;
        if (aS.endsWith(sU + DW_Strings.sCommaSpace + sU)
                || aS.endsWith(sU + DW_Strings.sCommaSpace)) {
            wasUOBefore = true;
        } else {
            wasUOBefore = false;
        }
        boolean wasUO;
        wasUO = aS.endsWith(sU);

        boolean isUO;
        isUO = (councilUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)
                || RSLUnderOccupiedSet1.getMap().keySet().contains(aCTBRef));
        if (isUO) {
            tCTBRefSetUOAtSomePoint.add(aCTBRef);
            if (aTT == 1) {
                tCTBRefSetUOTT1AtSomePoint.add(aCTBRef);
            } else if (aTT == 4) {
                tCTBRefSetUOTT4AtSomePoint.add(aCTBRef);
            }
        }

        if (aHBDP > 0) {
            tCTBRefSetUOClaimsRecievingDHP.add(aCTBRef);
        }

        // TenancyType
        key = aCTBRef + DW_Strings.sUnderscore + sTT;
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
                    if (tCTBRefSetUOTT1_To_LeftSHBE.contains(aCTBRef)) {
                        if (aTT != 3 || aTT != 6) {
                            if (tCTBRefSetUOTT1_To_TT3OrTT6NotDoneNextChange.contains(aCTBRef)) {
                                tCTBRefSetUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(aCTBRef);
                            }
                        }
                    }
                    if (tCTBRefSetUOTT4_To_LeftSHBE.contains(aCTBRef)) {
                        if (aTT != 3 || aTT != 6) {
                            if (tCTBRefSetUOTT4_To_TT3OrTT6NotDoneNextChange.contains(aCTBRef)) {
                                tCTBRefSetUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(aCTBRef);
                            }
                        }
                    }
                    doX(aCTBRef, aHBDP, aTT, bTT, isUO, wasUO, wasUOBefore,
                            bStatus,
                            tCTBRefSetUOTT1_To_LeftSHBE_NotReturned,
                            tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT1,
                            tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT1,
                            tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
                            tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT4,
                            tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT4,
                            tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
                            tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT8,
                            tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT9,
                            tCTBRefSetUOTT1_To_UOTT4,
                            tCTBRefSetTT1_To_UOTT4,
                            tCTBRefSetTT1_To_UOTT4GettingDHP);
                }
                if (aTT == DW_SHBE_TenancyType_Handler.iMinus999) {
                    if (tCTBRefSetUOAtSomePoint.contains(aCTBRef)) {
                        tCTBRefSetUO_To_LeftSHBEAtSomePoint.add(aCTBRef);
                    }
                    /*
                     * If previously UO (or previously not UO but status 2 and 
                     * prior to previously UO) then add to from UO to left the 
                     * SHBE.
                     */
                    boolean doA;
                    doA = false;
                    if (wasUO) {
                        doA = true;
                    } else if (bStatus == 2) {
                        if (wasUOBefore) {
                            doA = true;
                        }
                    }
                    if (doA) {
                        tCTBRefSetUO_To_LeftSHBE_NotReturned.add(aCTBRef);
                        if (bTT == 1) {
                            tCTBRefSetUOTT1_To_LeftSHBE.add(aCTBRef);
                            tCTBRefSetUOTT1_To_LeftSHBE_NotReturned.add(aCTBRef);
                            tCTBRefSetUOTT1_To_TT3OrTT6NotDoneNextChange.add(aCTBRef);
                            tCTBRefSetUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(aCTBRef);
                        } else if (bTT == 4) {
                            tCTBRefSetUOTT4_To_LeftSHBE.add(aCTBRef);
                            tCTBRefSetUOTT4_To_LeftSHBE_NotReturned.add(aCTBRef);
                            tCTBRefSetUOTT4_To_TT3OrTT6NotDoneNextChange.add(aCTBRef);
                            tCTBRefSetUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(aCTBRef);
                        } else if (bTT == 3 || bTT == 6) {
                            tCTBRefSetUOTT3OrTT6_To_LeftSHBE.add(aCTBRef);
                            tCTBRefSetUOTT3OrTT6_To_LeftSHBE_NotReturned.add(aCTBRef);
                        } else {
                            tCTBRefSetUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE.add(aCTBRef);
                            tCTBRefSetUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned.add(aCTBRef);
                        }
                    }
                }
            } else {
                tCTBRefSetChangedTT.add(aCTBRef);
                if (isUO) {
                    if (aTT == 4 && bTT == 1) {
                        //tCTBRefSetUOTT1OrTT1_To_UOTT4.add(aCTBRef);
                        if (wasUO) {
                            tCTBRefSetUOTT1_To_UOTT4.add(aCTBRef); // Looking forward, we may see that this claim actually comes out of being UO. To filter for this we should look back in the next iteration and perhaps move claim refs to other sets based upon some logic...
                        } else if (bStatus == 2 && wasUOBefore) {
                            tCTBRefSetUOTT1_To_UOTT4.add(aCTBRef); // Looking forward, we may see that this claim actually comes out of being UO. To filter for this we should look back in the next iteration and perhaps move claim refs to other sets based upon some logic...
                        } else {
                            tCTBRefSetTT1_To_UOTT4.add(aCTBRef);
                            if (aHBDP > 0) {
                                tCTBRefSetTT1_To_UOTT4GettingDHP.add(aCTBRef);
                            }
                        }
                    }
                }
                doX(aCTBRef, aHBDP, aTT, bTT, isUO, wasUO, wasUOBefore,
                        bStatus,
                        tCTBRefSetUOTT1_To_LeftSHBE_NotReturned,
                        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT1,
                        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT1,
                        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
                        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT4,
                        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT4,
                        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
                        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT8,
                        tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT9,
                        tCTBRefSetUOTT1_To_UOTT4,
                        tCTBRefSetTT1_To_UOTT4,
                        tCTBRefSetTT1_To_UOTT4GettingDHP);
                if (tCTBRefSetUOTT4_To_LeftSHBE_NotReturned.contains(aCTBRef)) {
                    tCTBRefSetUOTT4_To_LeftSHBE_NotReturned.remove(aCTBRef);
                    if (aTT == 1) {
                        if (isUO) {
                            tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsUOTT1.add(aCTBRef);
                        } else {
                            tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT1.add(aCTBRef);
                        }
                    } else if (aTT == 4) {
                        if (isUO) {
                            tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsUOTT4.add(aCTBRef);
                        } else {
                            tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT4.add(aCTBRef);
                        }
                    } else if (aTT == 3 || aTT == 6) {
                        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6.add(aCTBRef);
                    } else if (aTT == 5 || aTT == 7) {
                        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7.add(aCTBRef);
                    } else if (aTT == 8) {
                        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT8.add(aCTBRef);
                    } else if (aTT == 9) {
                        tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAsTT9.add(aCTBRef);
                    }
                }
                if (tCTBRefSetUOTT3OrTT6_To_LeftSHBE_NotReturned.contains(aCTBRef)) {
                    tCTBRefSetUOTT3OrTT6_To_LeftSHBE_NotReturned.remove(aCTBRef);
                    if (aTT == 1) {
                        if (isUO) {
                            tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1.add(aCTBRef);
                        } else {
                            tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1.add(aCTBRef);
                        }
                    } else if (aTT == 4) {
                        if (isUO) {
                            tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4.add(aCTBRef);
                        } else {
                            tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4.add(aCTBRef);
                        }
                    } else if (aTT == 3 || aTT == 6) {
                        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6.add(aCTBRef);
                    } else if (aTT == 5 || aTT == 7) {
                        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7.add(aCTBRef);
                    } else if (aTT == 8) {
                        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8.add(aCTBRef);
                    } else if (aTT == 9) {
                        tCTBRefSetUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9.add(aCTBRef);
                    }
                }
                if (tCTBRefSetUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned.contains(aCTBRef)) {
                    tCTBRefSetUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned.remove(aCTBRef);
                }
                if (tCTBRefSetUO_To_LeftSHBE_NotReturned.contains(aCTBRef)) {
                    tCTBRefSetUO_To_LeftSHBE_NotReturned.remove(aCTBRef);
                }
                if (aTT == 3 || aTT == 6) {
                    if (tCTBRefSetUOTT1_To_LeftSHBE.contains(aCTBRef)) {
                        if (tCTBRefSetUOTT1_To_TT3OrTT6NotDoneNextChange.contains(aCTBRef)) {
                            tCTBRefSetUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(aCTBRef);
                        }
                    }
                    if (tCTBRefSetUOTT4_To_LeftSHBE.contains(aCTBRef)) {
                        if (tCTBRefSetUOTT4_To_TT3OrTT6NotDoneNextChange.contains(aCTBRef)) {
                            tCTBRefSetUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(aCTBRef);
                        }
                    }
                    if (tCTBRefSetUOTT1AtSomePoint.contains(aCTBRef)) {
                        tCTBRefSetUOTT1_To_TT3OrTT6AtSomePoint.add(aCTBRef);
                    }
                    if (tCTBRefSetUOTT4AtSomePoint.contains(aCTBRef)) {
                        tCTBRefSetUOTT4_To_TT3OrTT6AtSomePoint.add(aCTBRef);
                    }
                    if (bTT == 1) {
                        tCTBRefSetTT1_To_TT3OrTT6.add(aCTBRef);
                        // If previously UO then add to set of those that move from UO TT1 to TT3 or TT6
                        if (wasUO) {
                            tCTBRefSetUOTT1_To_TT3OrTT6.add(aCTBRef);
                            tCTBRefSetUOTT1_To_TT3OrTT6NotDoneNextChange.add(aCTBRef);
                            tCTBRefSetUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(aCTBRef);
                        } else if (bStatus == 2) {
                            if (wasUOBefore) {
                                tCTBRefSetUOTT1_To_TT3OrTT6.add(aCTBRef);
                                tCTBRefSetUOTT1_To_TT3OrTT6NotDoneNextChange.add(aCTBRef);
                                tCTBRefSetUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(aCTBRef);
                            }
                        }
                    } else if (bTT == 4) {
                        tCTBRefSetTT4_To_TT3OrTT6.add(aCTBRef);
                        // If previously UO then add to set of those that move from UO TT4 to TT3 or TT6
                        if (wasUO) {
                            tCTBRefSetUOTT4_To_TT3OrTT6.add(aCTBRef);
                            tCTBRefSetUOTT4_To_TT3OrTT6NotDoneNextChange.add(aCTBRef);
                            tCTBRefSetUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(aCTBRef);
                        } else if (bStatus == 2) {
                            if (wasUOBefore) {
                                tCTBRefSetUOTT4_To_TT3OrTT6.add(aCTBRef);
                                tCTBRefSetUOTT4_To_TT3OrTT6NotDoneNextChange.add(aCTBRef);
                                tCTBRefSetUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(aCTBRef);
                            }
                        }
                    }
                } else {
                    if (tCTBRefSetUOTT1_To_TT3OrTT6NotDoneNextChange.contains(aCTBRef)) {
                        if (tCTBRefSetUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.contains(aCTBRef)) {
                            tCTBRefSetUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(aCTBRef);
                        }
                    }
                    if (tCTBRefSetUOTT4_To_TT3OrTT6NotDoneNextChange.contains(aCTBRef)) {
                        if (tCTBRefSetUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.contains(aCTBRef)) {
                            tCTBRefSetUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.remove(aCTBRef);
                        }
                    }
                    if (bTT == 3 || bTT == 6) {
                        if (aTT == 1) {
                            // If UO add to set that move from the PRS to Council UO.
                            if (isUO) {
                                tCTBRefSetTT3OrTT6_To_UOTT1.add(aCTBRef);  // Looking forward, we may see that this claim actually comes out of being UO almost immediately. This is kind of different to those claims that get stuck in UO for a significant period.
                            } else {
                                tCTBRefSetTT3OrTT6_To_TT1.add(aCTBRef);
                            }
                        } else if (aTT == 4) {
                            // If UO add to set that move from the PRS to RSL UO.
                            if (isUO) {
                                tCTBRefSetTT3OrTT6_To_UOTT4.add(aCTBRef);  // Looking forward, we may see that this claim actually comes out of being UO almost immediately. This is kind of different to those claims that get stuck in UO for a significant period.
                            } else {
                                tCTBRefSetTT3OrTT6_To_TT4.add(aCTBRef);
                            }
                        }
                    }
                }
            }
        }
        aS += DW_Strings.sCommaSpace + sTT_ + aTT;

        tableValues.put(key, aS);

        // UnderOccupancy
        key = aCTBRef + DW_Strings.sUnderscore + sUnderOccupancy;
        aS = tableValues.get(key);
        if (isUO) {
            tCTBRefSetPermanantlyLeftUOButRemainedInSHBE.remove(aCTBRef);
            tCTBRefSetPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged.remove(aCTBRef);
            tCTBRefSetPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased.remove(aCTBRef);
            result[0] = true;
            tUOClaims.add(aCTBRef);
            if (aTT == 1) {
                tCTBRefSetAlwaysUOTT1FromWhenStarted.add(aCTBRef);
            } else if (aTT == 4) {
                tCTBRefSetAlwaysUOTT4FromWhenStarted.add(aCTBRef);
            }
            aS += DW_Strings.sCommaSpace + sU;
            BigDecimal bd;
            String key2 = aCTBRef + DW_Strings.sUnderscore + sTotalCount_UnderOccupancy;
            bd = aggregateStatistics.get(key2);
            bd = bd.add(BigDecimal.ONE);
            aggregateStatistics.put(key2, bd);
            if (aDW_SHBE_D_Record != null) {
                if (councilUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)) {
                    result[1] = true;
                    addToSets(
                            aCTBRef,
                            tIDSetCouncilUniqueClaimantsEffected,
                            tIDSetCouncilUniquePartnersEffected,
                            tCouncilMaxNumberOfDependentsInClaimWhenUO,
                            tIDSetCouncilUniqueNonDependentsEffected,
                            tIDSetUniqueDependentChildrenUnderAge10Effected,
                            year,
                            month,
                            aDW_SHBE_Record,
                            aDW_SHBE_D_Record);
                    DW_UO_Record rec = councilUnderOccupiedSet1.getMap().get(aCTBRef);
                    int bedrooms = rec.getBedroomsInProperty();
                    int householdSizeSHBE;
                    householdSizeSHBE = DW_SHBE_Handler.getHouseholdSizeint(aDW_SHBE_D_Record);
                    result[5] = householdSizeSHBE;
                    if (householdSizeSHBE >= bedrooms) {
                        result[3] = true;
                    }
                    int householdSizeUO;
                    householdSizeUO = DW_UO_Handler.getHouseholdSizeExcludingPartners(rec);
                    if (householdSizeUO >= bedrooms) {
                        result[4] = true;
                    }
                    result[6] = householdSizeUO;
                }
                if (RSLUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)) {
                    result[2] = true;
                    addToSets(
                            aCTBRef,
                            tIDSetRSLUniqueClaimantsEffected,
                            tIDSetRSLUniquePartnersEffected,
                            tRSLMaxNumberOfDependentsInClaimWhenUO,
                            tIDSetRSLUniqueNonDependentsEffected,
                            tIDSetUniqueDependentChildrenUnderAge10Effected,
                            year,
                            month,
                            aDW_SHBE_Record,
                            aDW_SHBE_D_Record);
                    DW_UO_Record rec = RSLUnderOccupiedSet1.getMap().get(aCTBRef);
                    int bedrooms = rec.getBedroomsInProperty();
                    //long householdSize = tDW_SHBE_Handler.getHouseholdSize(aDW_SHBE_D_Record);
                    int householdSizeSHBE;
                    householdSizeSHBE = DW_SHBE_Handler.getHouseholdSizeint(aDW_SHBE_D_Record);
                    result[7] = householdSizeSHBE;
                    if (householdSizeSHBE >= bedrooms) {
                        result[3] = true;
                    }
                    int householdSizeUO;
                    householdSizeUO = DW_UO_Handler.getHouseholdSizeExcludingPartners(rec);
                    if (householdSizeUO >= bedrooms) {
                        result[4] = true;
                    }
                    result[8] = householdSizeUO;
                }
            }
            if (!(aTT == 1 || aTT == 4 || aTT == DW_SHBE_TenancyType_Handler.iMinus999)) {
                tCTBRefSetTTNot1Or4AndUnderOccupying.add(aCTBRef);
            }
            if (!wasUO) {
                if (!(bStatus == 2 && wasUOBefore)) {
                    // UO OnFlow
                    if (aTT == bTT) {
                        // Became UO staying in the same TT and postcode.
                        // Here we only count room requirement changes at the same 
                        // postcode (postcode changes are dealt with below).
                        if (aPC.equalsIgnoreCase(bPC)) {
                            if (aTT == 1) {
                                tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged.add(aCTBRef);
                                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedThisMonth.add(aCTBRef);
                            } else if (aTT == 4) {
                                tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged.add(aCTBRef);
                                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedThisMonth.add(aCTBRef);
                            }
                        }
                    }
                    if (tCTBRefSetUO_NotUO.contains(aCTBRef)) {
                        tCTBRefSetUO_NotUO_UO.add(aCTBRef);
                        tCTBRefSetUO_NotUO.remove(aCTBRef);
                    } else if (tCTBRefSetUO_NotUO_UO_NotUO.contains(aCTBRef)) {
                        tCTBRefSetUO_NotUO_UO_NotUO_UO.add(aCTBRef);
                        tCTBRefSetUO_NotUO_UO_NotUO.remove(aCTBRef);
                    } else if (tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO.contains(aCTBRef)) {
                        tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO.add(aCTBRef);
                        tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO.remove(aCTBRef);
                    } else if (tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO.contains(aCTBRef)) {
                        tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO.add(aCTBRef);
                        tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO.remove(aCTBRef);
                    }
                }
            }
            if (tCTBRefSetUOTT1_To_LeftSHBE.contains(aCTBRef)) {
                tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint.add(aCTBRef);
            }
            if (tCTBRefSetUOTT4_To_LeftSHBE.contains(aCTBRef)) {
                tCTBRefSetUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint.add(aCTBRef);
            }
        } else {
            aS += DW_Strings.sCommaSpace;
            tCTBRefSetAlwaysUOTT1FromStart.remove(aCTBRef);
            tCTBRefSetAlwaysUOTT4FromStart.remove(aCTBRef);
            if (aStatus == 2) {
                // Filter added as suspended claims that were UO are probably still UO
            } else {
                tCTBRefSetAlwaysUOTT1FromStartExceptWhenSuspended.remove(aCTBRef);
                tCTBRefSetAlwaysUOTT4FromStartExceptWhenSuspended.remove(aCTBRef);
                if (aS.contains(sU)) {
                    tCTBRefSetAlwaysUOTT1FromWhenStarted.remove(aCTBRef);
                    tCTBRefSetAlwaysUOTT4FromWhenStarted.remove(aCTBRef);
                    if (aS.contains(sU + DW_Strings.sCommaSpace + DW_Strings.sCommaSpace)) {
                        // ..., U, ,
                        tCTBRefSetIntermitantUO.add(aCTBRef);
                    }
                }
                if (wasUO) {
                    if (tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO.contains(aCTBRef)) {
                        tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO.add(aCTBRef);
                        tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO_UO.remove(aCTBRef);
                    } else if (tCTBRefSetUO_NotUO_UO_NotUO_UO.contains(aCTBRef)) {
                        tCTBRefSetUO_NotUO_UO_NotUO_UO_NotUO.add(aCTBRef);
                        tCTBRefSetUO_NotUO_UO_NotUO_UO.remove(aCTBRef);
                    } else if (tCTBRefSetUO_NotUO_UO.contains(aCTBRef)) {
                        tCTBRefSetUO_NotUO_UO_NotUO.add(aCTBRef);
                        tCTBRefSetUO_NotUO_UO.remove(aCTBRef);
                    } else {
                        tCTBRefSetUO_NotUO.add(aCTBRef);
                    }
                    if (aTT == DW_SHBE_TenancyType_Handler.iMinus999) {
                        tCTBRefSetUO_To_LeftSHBETheVeryNextMonth.add(aCTBRef);
                    } else {
                        tCTBRefSetPermanantlyLeftUOButRemainedInSHBE.add(aCTBRef);
                        if (aHS > bHS) {
                            tCTBRefSetPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased.add(aCTBRef);
                        }
                        if (!aPC.equalsIgnoreCase(bPC)) {
                            if (validPostcodes.contains(aPC) && validPostcodes.contains(bPC)) {
                                tCTBRefSetPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged.add(aCTBRef);
                                if (bTT == 1) {
                                    tCTBRefSetUOTT1_To_NotUO_InSHBE_PostcodeChanged.add(aCTBRef);
                                }
                                if (aTT == 1) {
                                    if (bTT == 1) {
                                        tCTBRefSetUOTT1_To_TT1_PostcodeChanged.add(aCTBRef);
                                    } else if (bTT == 4) {
                                        tCTBRefSetUOTT4_To_TT1_PostcodeChanged.add(aCTBRef);
                                    }
                                } else if (aTT == 4) {
                                    tCTBRefSetUOTT4_To_NotUO_InSHBE_PostcodeChanged.add(aCTBRef);
                                    if (bTT == 1) {
                                        tCTBRefSetUOTT1_To_TT4_PostcodeChanged.add(aCTBRef);
                                    } else if (bTT == 4) {
                                        tCTBRefSetUOTT4_To_TT4_PostcodeChanged.add(aCTBRef);
                                    }
                                }
                            }
                        }
                    }
                    if (aTT == bTT) {
                        // SolvedUO problem staying in same TT
                        // Cases involving a postcode change are dealt with below.
                        if (aPC.equalsIgnoreCase(bPC)) {
                            // Resolved UO without moving
                            // Room requirement changed or number of rooms reduced or both?
                            if (aTT == 1) {
                                tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged.add(aCTBRef);
                                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedThisMonth.add(aCTBRef);
                            } else if (aTT == 4) {
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
        key = aCTBRef + DW_Strings.sUnderscore + sP;
        aS = tableValues.get(key);

        if (aPC.equalsIgnoreCase(bPC)) {
            aS += DW_Strings.sCommaSpace;
        } else {
            boolean aSContainsaPC = aS.contains(aPC);
            aS += DW_Strings.sCommaSpace + aPC;
            if (!aPC.equalsIgnoreCase(defaultPostcode)) {
                boolean containsAnotherPostcode;
                if (bPC.equalsIgnoreCase(defaultPostcode)) {
                    containsAnotherPostcode = getContainsAnotherPostcode(aS, aPC);
                    if (aSContainsaPC) {
                        if (containsAnotherPostcode) {
                            boolean likelyTraveller;
                            likelyTraveller = getLikelyTraveller(aS, aPC);
                            if (likelyTraveller) {
                                tCTBRefSetTraveller.add(aCTBRef);
                            }
                            if (validPostcodes.contains(aPC) && validPostcodes.contains(bPC)) {
                                tCTBRefSetValidPostcodeChange.add(aCTBRef);
                            }
                        }
                    } else if (containsAnotherPostcode) {
                        if (validPostcodes.contains(aPC) && validPostcodes.contains(bPC)) {
                            tCTBRefSetValidPostcodeChange.add(aCTBRef);
                        }
                    }
                } else {
                    if (validPostcodes.contains(aPC) && validPostcodes.contains(bPC)) {
                        tCTBRefSetValidPostcodeChange.add(aCTBRef);
                    }
                    if (aSContainsaPC) {
//                        containsAnotherPostcode = getContainsAnotherPostcode(aS, aPC);
//                        if (containsAnotherPostcode) {
                        boolean likelyTraveller;
                        likelyTraveller = getLikelyTraveller(aS, aPC);
                        if (likelyTraveller) {
                            tCTBRefSetTraveller.add(aCTBRef);
                        }
//                        }
//                        tCTBRefSetNoValidPostcodeChange.remove(aCTBRef);
//                    } else {
//                        containsAnotherPostcode = getContainsAnotherPostcode(aS, aPC);
//                        if (containsAnotherPostcode) {
//                            tCTBRefSetNoValidPostcodeChange.remove(aCTBRef);
//                        }
                    }
                    if (wasUO && !isUO) {
                        if (aTT == bTT) {
                            if (aTT == 1) {
                                if (validPostcodes.contains(aPC)) {
                                    tCTBRefSetUOTT1_ToTT1_PostcodeChanged.add(aCTBRef);
                                }
                            } else if (aTT == 4) {
                                if (validPostcodes.contains(aPC)) {
                                    tCTBRefSetUOTT4_ToTT4_PostcodeChanged.add(aCTBRef);
                                }
                            }
                        }
                    } else if (!wasUO && isUO) {
                        if (aTT == bTT) {
                            if (aTT == 1) {
                                if (validPostcodes.contains(aPC)) {
                                    tCTBRefSetTT1_ToUOTT1_PostcodeChanged.add(aCTBRef);
                                }
                            } else if (aTT == 4) {
                                if (validPostcodes.contains(aPC)) {
                                    tCTBRefSetTT4_ToUOTT4_PostcodeChanged.add(aCTBRef);
                                }
                            }
                        }
                    } else if (wasUO && isUO) {
                        if (aTT == bTT) {
                            if (aTT == 1) {
                                if (validPostcodes.contains(aPC)) {
                                    tCTBRefSetUOTT1_ToUOTT1_PostcodeChanged.add(aCTBRef);
                                }
                            } else if (aTT == 4) {
                                if (validPostcodes.contains(aPC)) {
                                    tCTBRefSetUOTT4_ToUOTT4_PostcodeChanged.add(aCTBRef);
                                }
                            }
                        }
                    }
                    if (validPostcodes.contains(aPC) && validPostcodes.contains(bPC)) {
                        if (tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged1MonthPrevious.contains(aCTBRef)) {
                            if (aTT == 1) {
                                //tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month.add(aCTBRef);
                                if (isUO) {
                                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1.add(aCTBRef);
                                } else {
                                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1.add(aCTBRef);
                                }
                            } else if (aTT == 3 || aTT == 6) {
                                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6.add(aCTBRef);
                            } else if (aTT == 4) {
                                if (isUO) {
                                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4.add(aCTBRef);
                                } else {
                                    tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4.add(aCTBRef);
                                }
                            } else if (aTT == 5 || aTT == 7) {
                                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7.add(aCTBRef);
                            } else if (aTT == 8) {
                                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8.add(aCTBRef);
                            } else if (aTT == 9) {
                                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9.add(aCTBRef);
                            }
                        }
                        if (tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged1MonthPrevious.contains(aCTBRef)) {
                            if (aTT == 1) {
                                //tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month.add(aCTBRef);
                                if (isUO) {
                                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1.add(aCTBRef);
                                } else {
                                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1.add(aCTBRef);
                                }
                            } else if (aTT == 3 || aTT == 6) {
                                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6.add(aCTBRef);
                            } else if (aTT == 4) {
                                if (isUO) {
                                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1.add(aCTBRef);
                                } else {
                                    tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4.add(aCTBRef);
                                }
                            } else if (aTT == 5 || aTT == 7) {
                                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7.add(aCTBRef);
                            } else if (aTT == 8) {
                                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8.add(aCTBRef);
                            } else if (aTT == 9) {
                                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9.add(aCTBRef);
                            }
                        }

                        if (tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious.contains(aCTBRef)) {
                            if (aTT == 4) {
                                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months.add(aCTBRef);
                            }
                        }
                        if (tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious.contains(aCTBRef)) {
                            if (aTT == 1) { // Additional filter added as we only want those that are in the same TT.
                                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months.add(aCTBRef);
                            }
                        }
                        if (tCTBRefSetTT4_To_UOTT4_PostcodeUnchanged3MonthsPrevious.contains(aCTBRef)) {
                            if (aTT == 4) { // Additional filter added as we only want those that are in the same TT.
                                tCTBRefSetTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months.add(aCTBRef);
                            }
                        }
                        if (tCTBRefSetTT1_To_UOTT1_PostcodeUnchanged3MonthsPrevious.contains(aCTBRef)) {
                            if (aTT == 1) {
                                tCTBRefSetTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months.add(aCTBRef);
                            }
                        }
                        if (tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged1MonthPrevious.contains(aCTBRef)) {
                            if (aTT == 1) {
                                if (isUO) {
                                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1.add(aCTBRef);
                                } else {
                                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1.add(aCTBRef);
                                }
                            } else if (aTT == 4) {
                                if (isUO) {
                                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4.add(aCTBRef);
                                } else {
                                    tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4.add(aCTBRef);
                                }
                            } else if (aTT == 3 || aTT == 6) {
                                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6.add(aCTBRef);
                            } else if (aTT == 5 || aTT == 7) {
                                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7.add(aCTBRef);
                            } else if (aTT == 8) {
                                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8.add(aCTBRef);
                            } else if (aTT == 9) {
                                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9.add(aCTBRef);
                            }
                        }
                        if (tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged1MonthPrevious.contains(aCTBRef)) {
                            if (aTT == 1) {
                                if (isUO) {
                                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1.add(aCTBRef);
                                } else {
                                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1.add(aCTBRef);
                                }
                            } else if (aTT == 4) {
                                if (isUO) {
                                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4.add(aCTBRef);
                                } else {
                                    tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4.add(aCTBRef);
                                }
                            } else if (aTT == 3 || aTT == 6) {
                                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6.add(aCTBRef);
                            } else if (aTT == 5 || aTT == 7) {
                                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7.add(aCTBRef);
                            } else if (aTT == 8) {
                                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8.add(aCTBRef);
                            } else if (aTT == 9) {
                                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9.add(aCTBRef);
                            }
                        }
                        if (tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious.contains(aCTBRef)) {
                            if (aTT == 4) { // Additional filter added as we only want those that are in the same TT.
                                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months.add(aCTBRef);
                            }
                        }
                        if (tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious.contains(aCTBRef)) {
                            if (aTT == 1) { // Additional filter added as we only want those that are in the same TT.
                                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months.add(aCTBRef);
                            }
                        }
                        if (tCTBRefSetUOTT4_To_TT4_PostcodeUnchanged3MonthsPrevious.contains(aCTBRef)) {
                            if (aTT == 4) { // Additional filter added as we only want those that are in the same TT.
                                tCTBRefSetUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months.add(aCTBRef);
                            }
                        }
                        if (tCTBRefSetUOTT1_To_TT1_PostcodeUnchanged3MonthsPrevious.contains(aCTBRef)) {
                            if (aTT == 1) { // Additional filter added as we only want those that are in the same TT.
                                tCTBRefSetUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months.add(aCTBRef);
                            }
                        }
                    }
                }
            }
        }

        tableValues.put(key, aS);

        // HB Entitlement
        key = aCTBRef + DW_Strings.sUnderscore + sWHBE;
        aS = tableValues.get(key);
        if (aWHBE == bWHBE) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + decimalise(aWHBE);
        }

        tableValues.put(key, aS);

        // ERA
        key = aCTBRef + DW_Strings.sUnderscore + sWERA;
        aS = tableValues.get(key);
        if (aWERA == bWERA) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + decimalise(aWERA);
        }

        tableValues.put(key, aS);

        // PassportedStandardIndicator
        key = aCTBRef + DW_Strings.sUnderscore + sPSI;
        aS = tableValues.get(key);
        if (aPSI == bPSI) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aPSI;
        }

        tableValues.put(key, aS);

        // StatusOfHBClaim
        key = aCTBRef + DW_Strings.sUnderscore + sSHBC;
        aS = tableValues.get(key);
        if (aSHBC == bSHBC) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aSHBC;
        }

        tableValues.put(key, aS);

        // ReasonThatHBClaimClosed
        key = aCTBRef + DW_Strings.sUnderscore + sRTHBCC;
        aS = tableValues.get(key);
        if (aRTHBCC == bRTHBCC) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aRTHBCC;
        }

        tableValues.put(key, aS);

        // ClaimantEthnicGroup
        key = aCTBRef + DW_Strings.sUnderscore + sCEG;
        aS = tableValues.get(key);
        if (aCEG == bCEG) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aCEG;
        }

        tableValues.put(key, aS);

        // HS
        key = aCTBRef + DW_Strings.sUnderscore + sHS;
        aS = tableValues.get(key);
        if (aHS == bHS) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aHS;
        }

        tableValues.put(key, aS);

        // NonDependents
        key = aCTBRef + DW_Strings.sUnderscore + sND;
        aS = tableValues.get(key);
        if (aND == bND) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aND;
        }

        tableValues.put(key, aS);

        // ChildDependents
        key = aCTBRef + DW_Strings.sUnderscore + sCD;
        aS = tableValues.get(key);
        if (aCD == bCD) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aCD;
        }

        tableValues.put(key, aS);

        // UO
        DW_UO_Record aDW_UOReport_Record;

        if (councilUnderOccupiedSet1.getMap()
                .keySet().contains(aCTBRef)
                || RSLUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)) {
            if (councilUnderOccupiedSet1.getMap().keySet().contains(aCTBRef)) {
                aDW_UOReport_Record = councilUnderOccupiedSet1.getMap().get(aCTBRef);
            } else {
                aDW_UOReport_Record = RSLUnderOccupiedSet1.getMap().get(aCTBRef);
            }
            // NonDependents
            key = aCTBRef + DW_Strings.sUnderscore + sNDUO;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace + aDW_UOReport_Record.getNonDependents();
            tableValues.put(key, aS);
            // Children 16 +
            key = aCTBRef + DW_Strings.sUnderscore + sCO16;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace + aDW_UOReport_Record.getChildrenOver16();
            tableValues.put(key, aS);
            // FemaleChildrenUnder10
            key = aCTBRef + DW_Strings.sUnderscore + sFCU10;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace + aDW_UOReport_Record.getFemaleChildrenUnder10();
            tableValues.put(key, aS);
            // MaleChildrenUnder10
            key = aCTBRef + DW_Strings.sUnderscore + sMCU10;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace + aDW_UOReport_Record.getMaleChildrenUnder10();
            tableValues.put(key, aS);
            // FemaleChildren10to16
            key = aCTBRef + DW_Strings.sUnderscore + sFC10To16;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace + aDW_UOReport_Record.getFemaleChildren10to16();
            tableValues.put(key, aS);
            // MaleChildren10to16
            key = aCTBRef + DW_Strings.sUnderscore + sMC10To16;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace + aDW_UOReport_Record.getMaleChildren10to16();
            tableValues.put(key, aS);
            // Number of Bedrooms
            key = aCTBRef + DW_Strings.sUnderscore + sNB;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace + aDW_UOReport_Record.getBedroomsInProperty();
            tableValues.put(key, aS);
            // Bedroom Requirement
            key = aCTBRef + DW_Strings.sUnderscore + sBR;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace + aDW_UOReport_Record.getBedroomRequirement();
            tableValues.put(key, aS);
            int loss;
            loss = aWERA - aWHBE;
            key = aCTBRef + DW_Strings.sUnderscore + sTotal_HBLossDueToUO;
            BigDecimal bd;
            bd = aggregateStatistics.get(key);
            bd = bd.add(BigDecimal.valueOf(loss));
            aggregateStatistics.put(key, bd);
            if (loss > 0) {
                key = aCTBRef + DW_Strings.sUnderscore + sTotalCount_HBLossDueToUO;
                bd = aggregateStatistics.get(key);
                bd = bd.add(BigDecimal.ONE);
                aggregateStatistics.put(key, bd);
            }
        } else {
            // NonDependents
            key = aCTBRef + DW_Strings.sUnderscore + sNDUO;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace;
            tableValues.put(key, aS);
            // Children 16 +
            key = aCTBRef + DW_Strings.sUnderscore + sCO16;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace;
            tableValues.put(key, aS);
            // FemaleChildrenUnder10
            key = aCTBRef + DW_Strings.sUnderscore + sFCU10;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace;
            tableValues.put(key, aS);
            // MaleChildrenUnder10
            key = aCTBRef + DW_Strings.sUnderscore + sMCU10;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace;
            tableValues.put(key, aS);
            // FemaleChildren10to16
            key = aCTBRef + DW_Strings.sUnderscore + sFC10To16;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace;
            tableValues.put(key, aS);
            // MaleChildren10to16
            key = aCTBRef + DW_Strings.sUnderscore + sMC10To16;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace;
            tableValues.put(key, aS);
            // Number of Bedrooms
            key = aCTBRef + DW_Strings.sUnderscore + sNB;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace;
            tableValues.put(key, aS);
            // Bedroom Requirement
            key = aCTBRef + DW_Strings.sUnderscore + sBR;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace;
            tableValues.put(key, aS);
        }
        // Claimants DoB
        key = aCTBRef + DW_Strings.sUnderscore + sCDoB;
        aS = tableValues.get(key);

        if (aCDoB.equalsIgnoreCase(bCDoB)) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aCDoB;
        }

        tableValues.put(key, aS);
        // Claimants Age
        key = aCTBRef + DW_Strings.sUnderscore + sCA;
        aS = tableValues.get(key);

        if (aCA.equalsIgnoreCase(bCA)) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aCA;
        }

        tableValues.put(key, aS);
        // Partners DoB
        key = aCTBRef + DW_Strings.sUnderscore + sPDoB;
        aS = tableValues.get(key);

        if (aPDoB.equalsIgnoreCase(bPDoB)) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aPDoB;
        }

        tableValues.put(key, aS);
        // Partners Age
        key = aCTBRef + DW_Strings.sUnderscore + sPA;
        aS = tableValues.get(key);

        if (aPA.equalsIgnoreCase(bPA)) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aPA;
        }

        tableValues.put(key, aS);
        // ClaimantsGender
        key = aCTBRef + DW_Strings.sUnderscore + sCG;
        aS = tableValues.get(key);

        if (aCA.equalsIgnoreCase(bCA)) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aCG;
        }

        tableValues.put(key, aS);
        // PartnersGender
        key = aCTBRef + DW_Strings.sUnderscore + sPG;
        aS = tableValues.get(key);

        if (aPA.equalsIgnoreCase(bPA)) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aPG;
        }

        tableValues.put(key, aS);
        // Disability
        key = aCTBRef + DW_Strings.sUnderscore + sDisability;
        aS = tableValues.get(key);
        aS += DW_Strings.sCommaSpace + aD;

        tableValues.put(key, aS);
        // Disability Premium
        key = aCTBRef + DW_Strings.sUnderscore + sDisabilityPremium;
        aS = tableValues.get(key);
        aS += DW_Strings.sCommaSpace + aDP;

        tableValues.put(key, aS);
        // Disability Severe
        key = aCTBRef + DW_Strings.sUnderscore + sDisabilitySevere;
        aS = tableValues.get(key);
        aS += DW_Strings.sCommaSpace + aDS;

        tableValues.put(key, aS);
        // Disability Enhanced
        key = aCTBRef + DW_Strings.sUnderscore + sDisabilityEnhanced;
        aS = tableValues.get(key);
        aS += DW_Strings.sCommaSpace + aDE;

        tableValues.put(key, aS);
        // Child Disability
        key = aCTBRef + DW_Strings.sUnderscore + sDisabledChild;
        aS = tableValues.get(key);
        aS += DW_Strings.sCommaSpace + aDC;

        tableValues.put(key, aS);
        // Partner Death
        key = aCTBRef + DW_Strings.sUnderscore + sPDeath;
        aS = tableValues.get(key);

        if (aPDD.equalsIgnoreCase(bPDD)) {
            aS += DW_Strings.sCommaSpace;
        } else if (aPDD == null) {
            aS += DW_Strings.sCommaSpace;
        } else if (aPDD.isEmpty()) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + sPDeath + DW_Strings.sUnderscore + aPDD;
        }

        tableValues.put(key, aS);

        // HBDP
        BigDecimal bd;
        key = aCTBRef + DW_Strings.sUnderscore + sTotal_DHP;
        bd = aggregateStatistics.get(key);
        bd = bd.add(BigDecimal.valueOf(aHBDP));

        aggregateStatistics.put(key, bd);
        if (aHBDP
                > 0) {
            tCTBRefSetDHPAtSomePoint.add(aCTBRef);
            key = aCTBRef + DW_Strings.sUnderscore + sTotalCount_DHP;
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
        key = aCTBRef + DW_Strings.sUnderscore + sA;
        aS = tableValues.get(key);

        if (councilUnderOccupiedSet1.getMap()
                .keySet().contains(aCTBRef)) {
            DW_UO_Record UORec;
            UORec = councilUnderOccupiedSet1.getMap().get(aCTBRef);
            if (UORec == null) {
                aS += DW_Strings.sCommaSpace;
            } else {
                aArrears = UORec.getTotalRentArrears();
                if (aArrears == null) {
                    aS += DW_Strings.sCommaSpace;
                } else {
                    aS += DW_Strings.sCommaSpace + aArrears;
                    key = aCTBRef + DW_Strings.sUnderscore + sMax_Arrears;
                    bd = aggregateStatistics.get(key);
                    bd = bd.max(BigDecimal.valueOf(aArrears));
                    aggregateStatistics.put(key, bd);
                    if (aArrears > 0) {
                        tCTBRefSetInArrearsAtSomePoint.add(aCTBRef);
                        key = aCTBRef + DW_Strings.sUnderscore + sTotalCount_Arrears;
                        bd = aggregateStatistics.get(key);
                        bd = bd.add(BigDecimal.ONE);
                        aggregateStatistics.put(key, bd);
                    }
                    if (aHBDP > 0) {
                        if (aArrears > 0) {
                            tCTBRefSetUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint.add(aCTBRef);
                        }
                    }
                    if (aArrears > 0) {
                        tCTBRefSetUOTT1ClaimsInRentArrearsAtSomePoint.add(aCTBRef);
                    }
                    if (isUO) {
                        if (aTT == 1 && bTT == 4) {
                            //tCTBRefSetUOTT4OrTT4_To_UOTT1.add(aCTBRef);
                            //if (aArrears > 0) {
                            //    tCTBRefSetUOTT4OrTT4_To_UOTT1InArrears.add(aCTBRef);
                            //    if (aHBDP > 0) {
                            //        tCTBRefSetUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP.add(aCTBRef);
                            //    }
                            //}
                            if (wasUO) {
                                tCTBRefSetUOTT4_To_UOTT1.add(aCTBRef);
                                if (aHBDP > 0) {
                                    tCTBRefSetUOTT4_To_UOTT1GettingDHP.add(aCTBRef);
                                }
                                if (aArrears > 0) {
                                    tCTBRefSetUOTT4_To_UOTT1InArrears.add(aCTBRef);
                                    if (aHBDP > 0) {
                                        tCTBRefSetUOTT4_To_UOTT1InArrearsAndGettingDHP.add(aCTBRef);
                                    }
                                }
                            } else {
                                tCTBRefSetTT4_To_UOTT1.add(aCTBRef);
                                if (aHBDP > 0) {
                                    tCTBRefSetTT4_To_UOTT1GettingDHP.add(aCTBRef);
                                }
                                if (aArrears > 0) {
                                    tCTBRefSetTT4_To_UOTT1InArrears.add(aCTBRef);
                                    if (aHBDP > 0) {
                                        tCTBRefSetTT4_To_UOTT1InArrearsAndGettingDHP.add(aCTBRef);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            aS += DW_Strings.sCommaSpace;
        }

        if (tCTBRefSetUOTT1_To_TT1_PostcodeChanged.contains(aCTBRef)) {
            if (wasUOBefore && cTT == 1
                    && (!wasUO) && bTT == 1
                    && !(cPC.equalsIgnoreCase(bPC))
                    && (validPostcodes.contains(cPC) && validPostcodes.contains(bPC))) {
                if (isUO) {
                    tCTBRefSetUOTT1_To_TT1_PostcodeChanged.remove(aCTBRef);
                    if (aTT == 1) {
                        tCTBRefSetUOTT1_To_UOTT1_PostcodeChanged.add(aCTBRef);
                    } else if (aTT == 4) {
                        tCTBRefSetUOTT1_To_UOTT4_PostcodeChanged.add(aCTBRef);
                    }
                }
            }
        }

        if (tCTBRefSetUOTT4_To_TT4_PostcodeChanged.contains(aCTBRef)) {
            if (wasUOBefore && cTT == 1
                    && (!wasUO) && bTT == 1
                    && !(cPC.equalsIgnoreCase(bPC))
                    && (validPostcodes.contains(cPC) && validPostcodes.contains(bPC))) {
                if (isUO) {
                    tCTBRefSetUOTT4_To_TT4_PostcodeChanged.remove(aCTBRef);
                    if (aTT == 1) {
                        tCTBRefSetUOTT4_To_UOTT1_PostcodeChanged.add(aCTBRef);
                    } else if (aTT == 4) {
                        tCTBRefSetUOTT4_To_UOTT4_PostcodeChanged.add(aCTBRef);
                    }
                }
            }
        }

        if (tCTBRefSetUOTT1_To_TT3OrTT6.contains(aCTBRef)) {
            if (aTT == 1) {
                tCTBRefSetUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint.add(aCTBRef);
            }
        }

        if (tCTBRefSetUOTT4_To_TT3OrTT6.contains(aCTBRef)) {
            if (aTT == 4) {
                tCTBRefSetUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint.add(aCTBRef);
            }
        }

        tableValues.put(key, aS);
        return result;
    }

    private void doX(
            String aCTBRef,
            int aHBDP,
            int aTT,
            int bTT,
            boolean isUO,
            boolean wasUO,
            boolean wasUOBefore,
            int bStatus,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_NotReturned,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT1,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT1,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT4,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT4,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT8,
            TreeSet<String> tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT9,
            TreeSet<String> tCTBRefSetUOTT1_To_UOTT4,
            TreeSet<String> tCTBRefSetTT1_To_UOTT4,
            TreeSet<String> tCTBRefSetTT1_To_UOTT4GettingDHP
    ) {
        if (tCTBRefSetUOTT1_To_LeftSHBE_NotReturned.contains(aCTBRef)) {
            tCTBRefSetUOTT1_To_LeftSHBE_NotReturned.remove(aCTBRef);
            if (aTT == 1) {
                if (isUO) {
                    tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT1.add(aCTBRef);
                } else {
                    tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT1.add(aCTBRef);
                }
            } else if (aTT == 3 || aTT == 6) {
                tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6.add(aCTBRef);
            } else if (aTT == 4) {
                if (isUO) {
                    tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsUOTT4.add(aCTBRef);
                    if (bTT == 1) {
                        //tCTBRefSetUOTT1OrTT1_To_UOTT4.add(aCTBRef);
                        if (wasUO) {
                            tCTBRefSetUOTT1_To_UOTT4.add(aCTBRef); // Looking forward, we may see that this claim actually comes out of being UO. To filter for this we should look back in the next iteration and perhaps move claim refs to other sets based upon some logic...
                        } else if (bStatus == 2 && wasUOBefore) {
                            tCTBRefSetUOTT1_To_UOTT4.add(aCTBRef); // Looking forward, we may see that this claim actually comes out of being UO. To filter for this we should look back in the next iteration and perhaps move claim refs to other sets based upon some logic...
                        } else {
                            tCTBRefSetTT1_To_UOTT4.add(aCTBRef);
                            if (aHBDP > 0) {
                                tCTBRefSetTT1_To_UOTT4GettingDHP.add(aCTBRef);
                            }
                        }
                    }
                } else {
                    tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT4.add(aCTBRef);
                }
            } else if (aTT == 5 || aTT == 7) {
                tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7.add(aCTBRef);
            } else if (aTT == 8) {
                tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT8.add(aCTBRef);
            } else if (aTT == 9) {
                tCTBRefSetUOTT1_To_LeftSHBE_ReturnedAsTT9.add(aCTBRef);
            }
        }
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
        split = aS.split(DW_Strings.sComma);
        boolean firstIsTheSame = false;
        String s1;
        for (int i = split.length - 2; i > -1; i--) { // We don't go from the very end as we already added aPC to the end!
            s1 = split[i].trim();
            if (!s1.isEmpty()) {
                if (!(s1.equalsIgnoreCase(defaultPostcode))) {
                    if (s1.equalsIgnoreCase(aPC)) {
                        firstIsTheSame = true;
                    } else if (!firstIsTheSame) {
                        return true;
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
        split = aS.split(DW_Strings.sComma);
        String s1;
        for (int i = 0; i < split.length; i++) {
            //for (String split1 : split) {
            //    s1 = split1.trim();
            s1 = split[i];
            if (!s1.isEmpty()) {
                if (!(s1.equalsIgnoreCase(defaultPostcode))) {
                    if (!(s1.equalsIgnoreCase(aPC))) {
                        if (validPostcodes.contains(s1)) {
                            return true;
                        }
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

    /**
     * This returns the CTBRefs of the UnderOccupying claims in the first 
     * include period.
     * 
     * @TODO For overall computational efficiency, this should probably only be 
     * calculated once for each period and stored in a file then re-read from 
     * file when needed.
     * @param councilUnderOccupiedSets
     * @param RSLUnderOccupiedSets
     * @param SHBEFilenames
     * @param include
     * @return 
     */
    public HashSet<String>[] getStartUOCTBRefs(
            TreeMap<String, DW_UO_Set> councilUnderOccupiedSets,
            TreeMap<String, DW_UO_Set> RSLUnderOccupiedSets,
            String[] SHBEFilenames,
            ArrayList<Integer> include
    ) {
        HashSet<String>[] result;
        result = new HashSet[2];
        result[0] = new HashSet<String>();
        result[1] = new HashSet<String>();
        String yM31 = s;
        DW_UO_Set councilUnderOccupiedSet1 = null;
        DW_UO_Set RSLUnderOccupiedSet1 = null;
        String filename1 = null;
        Iterator<Integer> includeIte;
        includeIte = include.iterator();
        int i;
        i = includeIte.next();
        filename1 = SHBEFilenames[i];
        yM31 = DW_SHBE_Handler.getYM3(filename1);
        councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(yM31);
        if (councilUnderOccupiedSet1 != null) {
            RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(yM31);
            // Add to result
            result[0].addAll(councilUnderOccupiedSet1.getMap().keySet());
            result[1].addAll(RSLUnderOccupiedSet1.getMap().keySet());
        }
        return result;
    }

    /**
     * This returns the CTBRefs of the UnderOccupying claims in the last 
     * include period.
     * 
     * @TODO For overall computational efficiency, this should probably only be 
     * calculated once for each period and stored in a file then re-read from 
     * file when needed.
     * @param councilUnderOccupiedSets
     * @param RSLUnderOccupiedSets
     * @param SHBEFilenames
     * @param include
     * @return 
     */
    public HashSet<String>[] getEndUOCTBRefs(
            TreeMap<String, DW_UO_Set> councilUnderOccupiedSets,
            TreeMap<String, DW_UO_Set> RSLUnderOccupiedSets,
            String[] SHBEFilenames,
            ArrayList<Integer> include
    ) {
        HashSet<String>[] result;
        result = new HashSet[2];
        result[0] = new HashSet<String>();
        result[1] = new HashSet<String>();
        String yM31 = s;
        DW_UO_Set councilUnderOccupiedSet1 = null;
        DW_UO_Set RSLUnderOccupiedSet1 = null;
        String filename1 = null;
        Iterator<Integer> includeIte;
        includeIte = include.iterator();
        int i;
        i = includeIte.next();
        while (includeIte.hasNext()) {
            i = includeIte.next();
        }
        filename1 = SHBEFilenames[i];
        yM31 = DW_SHBE_Handler.getYM3(filename1);
        councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(yM31);
        if (councilUnderOccupiedSet1 != null) {
            RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(yM31);
            // Add to result
            result[0].addAll(councilUnderOccupiedSet1.getMap().keySet());
            result[1].addAll(RSLUnderOccupiedSet1.getMap().keySet());
        }
        return result;
    }

    public HashSet<String>[] getUOCTBRefs(
            TreeMap<String, DW_UO_Set> councilUnderOccupiedSets,
            TreeMap<String, DW_UO_Set> RSLUnderOccupiedSets,
            String[] SHBEFilenames,
            ArrayList<Integer> include
    ) {
        HashSet<String>[] result;
        result = new HashSet[2];
        result[0] = new HashSet<String>();
        result[1] = new HashSet<String>();
        String yM31 = s;
        DW_UO_Set councilUnderOccupiedSet1 = null;
        DW_UO_Set RSLUnderOccupiedSet1 = null;
        String filename1 = null;
        Iterator<Integer> includeIte;
        includeIte = include.iterator();
        int i;
        while (includeIte.hasNext()) {
            i = includeIte.next();
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
        result.add(aCTBRef + DW_Strings.sUnderscore + sTT);
        // UnderOccupancy
        result.add(aCTBRef + DW_Strings.sUnderscore + sUnderOccupancy);
        // Postcode
        result.add(aCTBRef + DW_Strings.sUnderscore + sP);
        // WeeklyHousingBenefitEntitlement
        result.add(aCTBRef + DW_Strings.sUnderscore + sWHBE);
        // WeeklyHousingBenefitEntitlement
        result.add(aCTBRef + DW_Strings.sUnderscore + sWERA);
        // PassportedStandardIndicator
        result.add(aCTBRef + DW_Strings.sUnderscore + sPSI);
        // StatusOfHBClaim
        result.add(aCTBRef + DW_Strings.sUnderscore + sSHBC);
        // ReasonThatHBClaimClosed
        result.add(aCTBRef + DW_Strings.sUnderscore + sRTHBCC);
        // ClaimantEthnicGroup
        result.add(aCTBRef + DW_Strings.sUnderscore + sCEG);
        // Arrears
        result.add(aCTBRef + DW_Strings.sUnderscore + sA);
        // HB DiscretionaryPayment
        result.add(aCTBRef + DW_Strings.sUnderscore + sHBDP);
        // Disability
        result.add(aCTBRef + DW_Strings.sUnderscore + sDisability);
        // Disability Premium
        result.add(aCTBRef + DW_Strings.sUnderscore + sDisabilityPremium);
        // Disability Severe
        result.add(aCTBRef + DW_Strings.sUnderscore + sDisabilitySevere);
        // Disability Enhanced
        result.add(aCTBRef + DW_Strings.sUnderscore + sDisabilityEnhanced);
        // Child Disability
        result.add(aCTBRef + DW_Strings.sUnderscore + sDisabledChild);
        // Partner Death
        result.add(aCTBRef + DW_Strings.sUnderscore + sPDeath);
        // Household Size
        result.add(aCTBRef + DW_Strings.sUnderscore + sHS);
        // NonDependents
        result.add(aCTBRef + DW_Strings.sUnderscore + sND);
        // ChildDependents
        result.add(aCTBRef + DW_Strings.sUnderscore + sCD);
        // NonDependents (UO)
        result.add(aCTBRef + DW_Strings.sUnderscore + sNDUO);
        // Children 16 +
        result.add(aCTBRef + DW_Strings.sUnderscore + sCO16);
        // FemaleChildrenUnder10
        result.add(aCTBRef + DW_Strings.sUnderscore + sFCU10);
        // MaleChildrenUnder10
        result.add(aCTBRef + DW_Strings.sUnderscore + sMCU10);
        // FemaleChildren10to16
        result.add(aCTBRef + DW_Strings.sUnderscore + sFC10To16);
        // MaleChildren10to16
        result.add(aCTBRef + DW_Strings.sUnderscore + sMC10To16);
        // Number of Bedrooms
        result.add(aCTBRef + DW_Strings.sUnderscore + sNB);
        // Bedroom Requirement
        result.add(aCTBRef + DW_Strings.sUnderscore + sBR);
        // Claimants DoB
        result.add(aCTBRef + DW_Strings.sUnderscore + sCDoB);
        // Claimants Age
        result.add(aCTBRef + DW_Strings.sUnderscore + sCA);
        // Claimants Gender
        result.add(aCTBRef + DW_Strings.sUnderscore + sCG);
        // Partners DoB
        result.add(aCTBRef + DW_Strings.sUnderscore + sPDoB);
        // Partners Age
        result.add(aCTBRef + DW_Strings.sUnderscore + sPA);
        // Partners Gender
        result.add(aCTBRef + DW_Strings.sUnderscore + sPG);
        return result;
    }

    private void writeLine(
            String generalStatistic,
            TreeMap<String, BigDecimal> generalStatistics,
            HashMap<String, String> generalStatisticsDescriptions,
            PrintWriter pw) {
        String line;
        line = generalStatistic + DW_Strings.sCommaSpace
                + generalStatistics.get(generalStatistic) + DW_Strings.sCommaSpace
                + generalStatisticsDescriptions.get(generalStatistic);
        pw.println(line);
    }

    public void writeTenancyChangeTables(
            Object[] table,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            boolean includePreUnderOccupancyValues,
            String name,
            String startMonth,
            String startYear,
            String endMonth,
            String endYear
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
        TreeMap<String, TreeMap<String, Integer>> timeStatistics;
        timeStatistics = (TreeMap<String, TreeMap<String, Integer>>) table[7];

        String dirName;
        dirName = startMonth + startYear + "_To_" + endMonth + endYear;

        PrintWriter pw5;
        pw5 = getPrintWriter(
                "TimeStatistics", dirName, paymentType, includeKey, underOccupancy);
        pw5.println("Date, " + sTotalCount_cumulativeUniqueClaims
                + ", " + sTotalCount_UOClaims
                + ", " + sTotalCount_UOCouncilClaims
                + ", " + sTotalCount_UORSLClaims
                + ", " + sTotalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE
                + ", " + sTotalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO
                + ", " + sTotalHouseholdSizeExcludingPartnersCouncilSHBE
                + ", " + sTotalHouseholdSizeExcludingPartnersCouncilUO
                + ", " + sTotalHouseholdSizeExcludingPartnersRSLSHBE
                + ", " + sTotalHouseholdSizeExcludingPartnersRSLUO);
        String date;
        int cumulativeCount;
        TreeMap<String, Integer> totalCounts_cumulativeUniqueClaims;
        totalCounts_cumulativeUniqueClaims = timeStatistics.get(sTotalCount_cumulativeUniqueClaims);
        int UOCount;
        int UOCouncilCount;
        int UORSLCount;
        TreeMap<String, Integer> totalCounts_UOClaims;
        totalCounts_UOClaims = timeStatistics.get(
                sTotalCount_UOClaims);
        TreeMap<String, Integer> totalCounts_UOCouncilClaims;
        totalCounts_UOCouncilClaims = timeStatistics.get(
                sTotalCount_UOCouncilClaims);
        TreeMap<String, Integer> totalCounts_UORSLClaims;
        totalCounts_UORSLClaims = timeStatistics.get(
                sTotalCount_UORSLClaims);
        int UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE;
        int UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO;

        int totalHouseholdSizeExcludingPartnersCouncilSHBE;
        int totalHouseholdSizeExcludingPartnersCouncilUO;
        int totalHouseholdSizeExcludingPartnersRSLSHBE;
        int totalHouseholdSizeExcludingPartnersRSLUO;
        TreeMap<String, Integer> totalCounts_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE;
        totalCounts_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE = timeStatistics.get(sTotalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE);
        TreeMap<String, Integer> totalCounts_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO;
        totalCounts_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO = timeStatistics.get(sTotalCount_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO);

        TreeMap<String, Integer> sTotalHouseholdSizeExcludingPartnersCouncilSHBEs;
        sTotalHouseholdSizeExcludingPartnersCouncilSHBEs = timeStatistics.get(sTotalHouseholdSizeExcludingPartnersCouncilSHBE);
        TreeMap<String, Integer> sTotalHouseholdSizeExcludingPartnersCouncilUOs;
        sTotalHouseholdSizeExcludingPartnersCouncilUOs = timeStatistics.get(sTotalHouseholdSizeExcludingPartnersCouncilUO);
        TreeMap<String, Integer> sTotalHouseholdSizeExcludingPartnersRSLSHBEs;
        sTotalHouseholdSizeExcludingPartnersRSLSHBEs = timeStatistics.get(sTotalHouseholdSizeExcludingPartnersRSLSHBE);
        TreeMap<String, Integer> sTotalHouseholdSizeExcludingPartnersRSLUOs;
        sTotalHouseholdSizeExcludingPartnersRSLUOs = timeStatistics.get(sTotalHouseholdSizeExcludingPartnersRSLUO);

        Iterator<String> ite;
        ite = totalCounts_cumulativeUniqueClaims.keySet().iterator();
        while (ite.hasNext()) {
            date = ite.next();
            cumulativeCount = totalCounts_cumulativeUniqueClaims.get(date);
            UOCount = totalCounts_UOClaims.get(date);
            UOCouncilCount = totalCounts_UOCouncilClaims.get(date);
            UORSLCount = totalCounts_UORSLClaims.get(date);
            UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE = totalCounts_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE.get(date);
            UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO = totalCounts_UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO.get(date);
            totalHouseholdSizeExcludingPartnersCouncilSHBE = sTotalHouseholdSizeExcludingPartnersCouncilSHBEs.get(date);
            totalHouseholdSizeExcludingPartnersCouncilUO = sTotalHouseholdSizeExcludingPartnersCouncilUOs.get(date);
            totalHouseholdSizeExcludingPartnersRSLSHBE = sTotalHouseholdSizeExcludingPartnersRSLSHBEs.get(date);
            totalHouseholdSizeExcludingPartnersRSLUO = sTotalHouseholdSizeExcludingPartnersRSLUOs.get(date);
            pw5.println(date + ", " + Integer.toString(cumulativeCount)
                    + ", " + Integer.toString(UOCount)
                    + ", " + Integer.toString(UOCouncilCount)
                    + ", " + Integer.toString(UORSLCount)
                    + ", " + Integer.toString(UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsSHBE)
                    + ", " + Integer.toString(UOClaimsInHouseholdsWithHouseholdSizeGreaterThanOrEqualToNumberOfBedroomsUO)
                    + ", " + Integer.toString(totalHouseholdSizeExcludingPartnersCouncilSHBE)
                    + ", " + Integer.toString(totalHouseholdSizeExcludingPartnersCouncilUO)
                    + ", " + Integer.toString(totalHouseholdSizeExcludingPartnersRSLSHBE)
                    + ", " + Integer.toString(totalHouseholdSizeExcludingPartnersRSLUO));
        }
        pw5.close();

        TreeMap<String, String> groupNameDescriptions;
        groupNameDescriptions = getGroupNameDescriptions(groups.keySet());

        String aggregateStatisticsHeader;
        //aggregateStatisticsHeader = "CTBRef, DHP_Total, Housing Benefit Loss as a Result of UnderOccupancy, Max_Arrears, NumberOfUnderOccupancyMonths";
        aggregateStatisticsHeader = "CTBRef " + DW_Strings.sCommaSpace
                + sTotal_DHP + DW_Strings.sCommaSpace
                + sTotalCount_DHP + DW_Strings.sCommaSpace
                + sTotal_HBLossDueToUO + DW_Strings.sCommaSpace
                + sTotalCount_HBLossDueToUO + DW_Strings.sCommaSpace
                + sMax_Arrears + DW_Strings.sCommaSpace
                + sTotalCount_Arrears + DW_Strings.sCommaSpace
                + sTotalCount_UnderOccupancy;

        String generalStatisticsHeader;
        generalStatisticsHeader = "GeneralStatistic, Value, GeneralStatisticDescription";
        pw5 = getPrintWriter(
                "GeneralStatistics", dirName, paymentType, includeKey, underOccupancy);
        pw5.println(generalStatisticsHeader);
        HashMap<String, String> generalStatisticsDescriptions;
        generalStatisticsDescriptions = getGeneralStatisticDescriptions(
                startMonth, startYear, endMonth, endYear);
        writeLine(sTotalCount_AlwaysUOTT1FromStart,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_AlwaysUOTT1FromWhenStarted,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_AlwaysUOTT4FromStart,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_AlwaysUOTT4FromWhenStarted,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sAverageHouseholdSizeOfThoseUOTT1AlwaysFromStart,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sAverageHouseholdSizeOfThoseUOTT4AlwaysFromStart,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_ClaimsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_UniqueIndividualsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_CouncilClaimsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_CouncilUniqueIndividualsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_CouncilUniqueClaimantsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_CouncilUniquePartnersEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_CouncilDependentsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_CouncilUniqueNonDependentsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_RSLClaimsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_RSLUniqueIndividualsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_RSLUniqueClaimantsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_RSLUniquePartnersEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_RSLDependentsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_RSLUniqueNonDependentsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_UniqueChildrenAgeLessThan10EffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sSubsequentlyEffectedUOStillUOInOctober2015,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageReceivingDHPInOctober2015,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageInArrearsOctober2015,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageInArrearsAndReceivingDHPInOctober2015,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageReceivingDHPInOctober2015ThatWereUOInApril2013,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageInArrearsOctober2015ThatWereUOInApril2013,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageInArrearsAndReceivingDHPInOctober2015ThatWereUOInApril2013,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sUO_To_LeftSHBEAtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE,
                generalStatistics, generalStatisticsDescriptions, pw5);
//        line = getLine(sUOTT1_To_LeftSHBEReturnedAsTT1orTT4 + sCommaSpace + generalStatistics.get(sUOTT1_To_LeftSHBEReturnedAsTT1orTT4);
//        pw5.println(line);
//        line = getLine(sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6 + sCommaSpace + generalStatistics.get(sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6);
//        pw5.println(line);
        writeLine(sUOTT4_To_LeftSHBE,
                generalStatistics, generalStatisticsDescriptions, pw5);
//        line = getLine(sUOTT4_To_LeftSHBEReturnedAsTT1orTT4 + sCommaSpace + generalStatistics.get(sUOTT4_To_LeftSHBEReturnedAsTT1orTT4);
//        pw5.println(line);
//        line = getLine(sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6 + sCommaSpace + generalStatistics.get(sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6);
//        pw5.println(line);
        writeLine(sUOTT3OrTT6_To_LeftSHBE,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUO_To_LeftSHBE,
                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sUO_To_LeftSHBEBetweenOneAndTwoMonths,
//                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sUO_To_LeftSHBEBetweenTwoAndThreeMonths,
//                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUO_To_LeftSHBE_NotReturned,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_NotReturned,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_NotReturned,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_NotReturned,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAsUOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAsTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAsUOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAsTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAsTT8,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAsTT9,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAsUOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAsTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAsUOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAsTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAsTT8,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAsTT9,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sUO_NotUO_UO,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUO_NotUO_UO_NotUO_UO,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUO_NotUO_UO_NotUO_UO_NotUO_UO,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sUOTT1_To_NotUO_InSHBE_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT4_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_NotUO_InSHBE_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT1_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sUOTT1_To_TT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT3OrTT6AtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sUOTT4_To_TT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT3OrTT6AtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sTT3OrTT6_To_UOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT3OrTT6_To_UOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sTT1_To_UOTT1_PostcodeUnchanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
//                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months,
                generalStatistics, generalStatisticsDescriptions, pw5);

        //writeLine(sUOTT1OrTT1_To_UOTT4,
        //        generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_UOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT4GettingDHP,
                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sTT1_To_UOTT4InArrears,
//                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sTT1_To_UOTT4InArrearsAndGettingDHP,
//                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sTT4_To_UOTT4_PostcodeUnchanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
//                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months,
                generalStatistics, generalStatisticsDescriptions, pw5);

        //writeLine(sUOTT4OrTT4_To_UOTT1,
        //        generalStatistics, generalStatisticsDescriptions, pw5);
        //writeLine(sUOTT4OrTT4_To_UOTT1InArrears,
        //        generalStatistics, generalStatisticsDescriptions, pw5);
        //writeLine(sUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
        //        generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_UOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_UOTT1InArrears,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_UOTT1GettingDHP,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_UOTT1InArrearsAndGettingDHP,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT1InArrears,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT1GettingDHP,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT1InArrearsAndGettingDHP,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sUOTT1_To_TT1_PostcodeUnchanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
//                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sUOTT4_To_TT4_PostcodeUnchanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
//                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sUOTT1_To_TT1_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_UOTT1_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_UOTT4_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sUOClaimsRecievingDHP,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1ClaimsInRentArrearsAtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sInArrearsAtSomePoint_And_DHPAtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPermanantlyLeftUOButRemainedInSHBE,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased,
                generalStatistics, generalStatisticsDescriptions, pw5);

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
        //Iterator<String> ite;
        int counter;

        System.out.println("Group Size, Not previously exported, GroupName");

        iteG = groupNameDescriptions.keySet().iterator();
        while (iteG.hasNext()) {
            groupName = iteG.next();

            if (groupName.endsWith("Minus999")) {
                int debug = 1;
            }

            name2 = name + DW_Strings.sUnderscore + groupName;
            if (includePreUnderOccupancyValues) {
                name2 += DW_Strings.sUnderscore + "IncludesPreUnderOccupancyValues";
            }
            pw = getPrintWriter(name2, dirName, paymentType, includeKey, underOccupancy);
            pwAggregateStatistics = getPrintWriter(name2 + "AggregateStatistics", dirName, paymentType, includeKey, underOccupancy);
            name2 += DW_Strings.sUnderscore + "WithDuplicates";
            pw2 = getPrintWriter(name2, dirName, paymentType, includeKey, underOccupancy);
            pwAggregateStatistics2 = getPrintWriter(name2 + "AggregateStatistics", dirName, paymentType, includeKey, underOccupancy);
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
                    } else if (groupName.equalsIgnoreCase(sTT1_To_TT3OrTT6)) {
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
                    } else if (groupName.equalsIgnoreCase(sTT4_To_TT3OrTT6)) {
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
                    } else if (groupName.equalsIgnoreCase(sTT3OrTT6_To_TT1)) {
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
                    } else if (groupName.equalsIgnoreCase(sTT3OrTT6_To_TT4)) {
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
                    } else if (groupName.equalsIgnoreCase(sAlwaysUOFromWhenStarted__ChangedTT)) {
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
                    } else if (groupName.equalsIgnoreCase(sAlwaysUOFromWhenStarted__ValidPostcodeChange_NotChangedTT)) {
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
                    } else if (groupName.equalsIgnoreCase(sAlwaysUOFromWhenStarted__NoValidPostcodeChange_NotChangedTT)) {
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
                    } else if (groupName.equalsIgnoreCase(sIntermitantUO__ChangedTT)) {
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
                    } else if (groupName.equalsIgnoreCase(sIntermitantUO__ValidPostcodeChange_NotChangedTT)) {
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
//                                                            System.out.println("CTBRef " + aCTBRef + " already added to"
//                                                                    + " another group and in " + groupNameDescription);
//                                                            writeRecordCollectionToStdOut(
//                                                                    tableValues,
//                                                                    includePreUnderOccupancyValues,
//                                                                    preUnderOccupancyValues,
//                                                                    aCTBRef);
                        writeRecordCollection(
                                tableValues,
                                includePreUnderOccupancyValues,
                                preUnderOccupancyValues,
                                aCTBRef,
                                pw);
                        writeAggregateRecords(
                                aggregateStatistics,
                                aCTBRef,
                                pwAggregateStatistics);
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
                    } //                                        sAlwaysUOFromStart__NoValidPostcodeChange_NotChangedTT
                    //                                        sIntermitantUO__NoValidPostcodeChange_NotChangedTT                                                    
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
        name2 = name + DW_Strings.sUnderscore + groupName;
        pw = getPrintWriter(name2, dirName, paymentType, includeKey, underOccupancy);
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
        line += DW_Strings.sCommaSpace + decimalise(aggregateStatistics.get(aCTBRef + DW_Strings.sUnderscore + sTotal_DHP).intValue());
        line += DW_Strings.sCommaSpace + aggregateStatistics.get(aCTBRef + DW_Strings.sUnderscore + sTotalCount_DHP);
        line += DW_Strings.sCommaSpace + decimalise(aggregateStatistics.get(aCTBRef + DW_Strings.sUnderscore + sTotal_HBLossDueToUO).intValue());
        line += DW_Strings.sCommaSpace + aggregateStatistics.get(aCTBRef + DW_Strings.sUnderscore + sTotalCount_HBLossDueToUO);
        line += DW_Strings.sCommaSpace + aggregateStatistics.get(aCTBRef + DW_Strings.sUnderscore + sMax_Arrears);
        line += DW_Strings.sCommaSpace + aggregateStatistics.get(aCTBRef + DW_Strings.sUnderscore + sTotalCount_Arrears);
        line += DW_Strings.sCommaSpace + aggregateStatistics.get(aCTBRef + DW_Strings.sUnderscore + sTotalCount_UnderOccupancy);
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

    protected PrintWriter getPrintWriter(
            String name,
            String dirName,
            String paymentType,
            String includeKey,
            boolean underOccupancy) {
        PrintWriter result;
        File dirOut;
        dirOut = env.getDW_Files().getTableDir(
                sUnderOccupancyGroupTables,
                paymentType, 
                includeKey, 
                underOccupancy);
        dirOut = new File(
                dirOut,
                dirName);
        dirOut.mkdirs();
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
            result.put(groupName, "Type: " + groupName);
        }
//            gn = sTravellers;
//            if (groupName.equalsIgnoreCase(gn)) {
//                result.put(groupName, "Type: " + gn);
//            } else {
//                gn = sTTNot1Or4AndUnderOccupying;
//                if (groupName.equalsIgnoreCase(gn)) {
//                    result.put(groupName, "Type: " + gn);
//                } else {
//                    gn = sTT1_To_TT3OrTT6;
//                    if (groupName.equalsIgnoreCase(gn)) {
//                        result.put(groupName, "Type: " + gn);
//                    } else {
//                        gn = sTT4_To_TT3OrTT6;
//                        if (groupName.equalsIgnoreCase(gn)) {
//                            result.put(groupName, "Type: " + gn);
//                        } else {
//                            gn = sTT3OrTT6_To_TT1;
//                            if (groupName.equalsIgnoreCase(gn)) {
//                                result.put(groupName, "Type: " + gn);
//                            } else {
//                                gn = sTT3OrTT6_To_TT4;
//                                if (groupName.equalsIgnoreCase(gn)) {
//                                    result.put(groupName, "Type: " + gn);
//                                } else {
//                                    gn = sAlwaysUOFromStart__NoValidPostcodeChange_NotChangedTT;
//                                    if (groupName.equalsIgnoreCase(gn)) {
//                                        result.put(groupName, "Type: " + gn);
//                                    } else {
//                                        gn = sAlwaysUOFromStart__ChangedTT;
//                                        if (groupName.equalsIgnoreCase(gn)) {
//                                            result.put(groupName, "Type: " + gn);
//                                        } else {
//                                            gn = sAlwaysUOFromStart__ValidPostcodeChange_NotChangedTT;
//                                            if (groupName.equalsIgnoreCase(gn)) {
//                                                result.put(groupName, "Type: " + gn);
//                                            } else {
//                                                gn = sAlwaysUOFromWhenStarted__NoValidPostcodeChange_NotChangedTT;
//                                                if (groupName.equalsIgnoreCase(gn)) {
//                                                    result.put(groupName, "Type: " + gn);
//                                                } else {
//                                                    gn = sAlwaysUOFromWhenStarted__ChangedTT;
//                                                    if (groupName.equalsIgnoreCase(gn)) {
//                                                        result.put(groupName, "Type: " + gn);
//                                                    } else {
//                                                        gn = sAlwaysUOFromWhenStarted__ValidPostcodeChange_NotChangedTT;
//                                                        if (groupName.equalsIgnoreCase(gn)) {
//                                                            result.put(groupName, "Type: " + gn);
//                                                        } else {
//                                                            gn = sIntermitantUO__NoValidPostcodeChange_NotChangedTT;
//                                                            if (groupName.equalsIgnoreCase(gn)) {
//                                                                result.put(groupName, "Type: " + gn);
//                                                            } else {
//                                                                gn = sIntermitantUO__ChangedTT;
//                                                                if (groupName.equalsIgnoreCase(gn)) {
//                                                                    result.put(groupName, "Type: " + gn);
//                                                                } else {
//                                                                    gn = sIntermitantUO__ValidPostcodeChange_NotChangedTT;
//                                                                    if (groupName.equalsIgnoreCase(gn)) {
//                                                                        result.put(groupName, "Type: " + gn);
//                                                                    } else {
//                                                                        gn = sUOTT1_To_TT3OrTT6;
//                                                                        if (groupName.equalsIgnoreCase(gn)) {
//                                                                            result.put(groupName, "Type: " + gn);
//                                                                        } else {
//                                                                            gn = sUOTT1_To_TT3OrTT6AtSomePoint;
//                                                                            if (groupName.equalsIgnoreCase(gn)) {
//                                                                                result.put(groupName, "Type: " + gn);
//                                                                            } else {
//                                                                                gn = sUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999;
//                                                                                if (groupName.equalsIgnoreCase(gn)) {
//                                                                                    result.put(groupName, "Type: " + gn);
//                                                                                } else {
//                                                                                    gn = sUOTT4_To_TT3OrTT6;
//                                                                                    if (groupName.equalsIgnoreCase(gn)) {
//                                                                                        result.put(groupName, "Type: " + gn);
//                                                                                    } else {
//                                                                                        gn = sUOTT4_To_TT3OrTT6AtSomePoint;
//                                                                                        if (groupName.equalsIgnoreCase(gn)) {
//                                                                                            result.put(groupName, "Type: " + gn);
//                                                                                        } else {
//                                                                                            gn = sUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999;
//                                                                                            if (groupName.equalsIgnoreCase(gn)) {
//                                                                                                result.put(groupName, "Type: " + gn);
//                                                                                            } else {
//                                                                                                gn = sTT3OrTT6_To_UOTT1;
//                                                                                                if (groupName.equalsIgnoreCase(gn)) {
//                                                                                                    result.put(groupName, "Type: " + gn);
//                                                                                                } else {
//                                                                                                    gn = sUOTT1OrTT1_To_UOTT4;
//                                                                                                    if (groupName.equalsIgnoreCase(gn)) {
//                                                                                                        result.put(groupName, "Type: " + gn);
//                                                                                                    } else {
//                                                                                                        gn = sTT1_To_UOTT4InArrears;
//                                                                                                        if (groupName.equalsIgnoreCase(gn)) {
//                                                                                                            result.put(groupName, "Type: " + gn);
//                                                                                                        } else {
//                                                                                                            gn = sTT1_To_UOTT4InArrearsAndGettingDHP;
//                                                                                                            if (groupName.equalsIgnoreCase(gn)) {
//                                                                                                                result.put(groupName, "Type: " + gn);
//                                                                                                            } else {
//                                                                                                                gn = sUOTT4OrTT4_To_UOTT1;
//                                                                                                                if (groupName.equalsIgnoreCase(gn)) {
//                                                                                                                    result.put(groupName, "Type: " + gn);
//                                                                                                                } else {
//                                                                                                                    gn = sUOTT4OrTT4_To_UOTT1InArrears;
//                                                                                                                    if (groupName.equalsIgnoreCase(gn)) {
//                                                                                                                        result.put(groupName, "Type: " + gn);
//                                                                                                                    } else {
//                                                                                                                        gn = sUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP;
//                                                                                                                        if (groupName.equalsIgnoreCase(gn)) {
//                                                                                                                            result.put(groupName, "Type: " + gn);
//                                                                                                                        } else {
//                                                                                                                            gn = sUOClaimsRecievingDHP;
//                                                                                                                            if (groupName.equalsIgnoreCase(gn)) {
//                                                                                                                                result.put(groupName, "Type: " + gn);
//                                                                                                                            } else {
//                                                                                                                                gn = sUOTT1ClaimsInRentArrearsAtSomePoint;
//                                                                                                                                if (groupName.equalsIgnoreCase(gn)) {
//                                                                                                                                    result.put(groupName, "Type: " + gn);
//                                                                                                                                } else {
//                                                                                                                                    gn = sUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint;
//                                                                                                                                    if (groupName.equalsIgnoreCase(gn)) {
//                                                                                                                                        result.put(groupName, "Type: " + gn);
//                                                                                                                                    } else {
//                                                                                                                                        gn = sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month;
//                                                                                                                                        if (groupName.equalsIgnoreCase(gn)) {
//                                                                                                                                            result.put(groupName, "Type: " + gn);
//                                                                                                                                        } else {
//                                                                                                                                            gn = sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month;
//                                                                                                                                            if (groupName.equalsIgnoreCase(gn)) {
//                                                                                                                                                result.put(groupName, "Type: " + gn);
//                                                                                                                                            } else {
//                                                                                                                                                gn = sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month;
//                                                                                                                                                if (groupName.equalsIgnoreCase(gn)) {
//                                                                                                                                                    result.put(groupName, "Type: " + gn);
//                                                                                                                                                } else {
//                                                                                                                                                    gn = sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month;
//                                                                                                                                                    if (groupName.equalsIgnoreCase(gn)) {
//                                                                                                                                                        result.put(groupName, "Type: " + gn);
//                                                                                                                                                    }
//                                                                                                                                                }
//                                                                                                                                            }
//                                                                                                                                        }
//                                                                                                                                    }
//                                                                                                                                }
//                                                                                                                            }
//                                                                                                                        }
//                                                                                                                    }
//                                                                                                                }
//                                                                                                            }
//                                                                                                        }
//                                                                                                    }
//                                                                                                }
//                                                                                            }
//                                                                                        }
//                                                                                    }
//                                                                                }
//                                                                            }
//                                                                        }
//                                                                    }
//                                                                }
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
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
            HashSet<ID> tIDSetUniqueDependentChildrenUnderAge10Effected,
            String year,
            String month,
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
                String DoB = SRecord.getSubRecordDateOfBirth();
                int age = Integer.valueOf(DW_SHBE_Handler.getAge(year, month, DoB));
                if (age < 10) {
                    aID = getSID(
                            SRecord,
                            subRecordType,
                            claimantNINO,
                            0,
                            nonDependentsSet);
                    tIDSetUniqueDependentChildrenUnderAge10Effected.add(aID);
                }
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
