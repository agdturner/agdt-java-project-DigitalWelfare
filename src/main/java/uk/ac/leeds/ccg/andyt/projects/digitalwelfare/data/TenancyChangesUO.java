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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_PersonID;
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
     * A reference to DW_Environment For convenience;
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
//        String sPermanantlyLeftUOButRemainedInSHBE = "PermanantlyLeftUOButRemainedInSHBE";
//        String sPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged = "PermanantlyLeftUOButRemainedInSHBE_PostcodeChanged";
//        String sPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased = "PermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased";
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
            HashMap<DW_ID, String> ClaimIDToCTBRefLookup,
            HashSet<DW_ID> ClaimIDs,
            String[] SHBEFilenames,
            ArrayList<Integer> NotMonthlyUO
    ) {
        TreeMap<String, String> result;
        result = new TreeMap<String, String>();
        // Init result
        DW_ID ClaimID;
        String CTBRef;
        Iterator<DW_ID> ClaimIDsIte;
        ClaimIDsIte = ClaimIDs.iterator();
        while (ClaimIDsIte.hasNext()) {
            ClaimID = ClaimIDsIte.next();
            CTBRef = ClaimIDToCTBRefLookup.get(ClaimID);
            result.put(CTBRef + DW_Strings.sUnderscore + sTT, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sUnderOccupancy, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sP, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sWHBE, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sWERA, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sPSI, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sSHBC, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sRTHBCC, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sCEG, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sHS, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sND, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sCD, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sNDUO, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sCO16, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sFCU10, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sMCU10, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sFC10To16, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sMC10To16, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sBR, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sNB, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sCDoB, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sCA, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sPDoB, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sPA, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sCG, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sPG, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sDisability, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sDisabilityPremium, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sDisabilitySevere, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sDisabilityEnhanced, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sDisabledChild, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sPDeath, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sHBDP, s);
            result.put(CTBRef + DW_Strings.sUnderscore + sA, s);
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
        TreeMap<DW_ID, DW_SHBE_Record> records;
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
            ClaimIDsIte = ClaimIDs.iterator();
            while (ClaimIDsIte.hasNext()) {
                ClaimID = ClaimIDsIte.next();
                record = records.get(ClaimID);
                if (record != null) {
                    dRecord = record.getDRecord();
                    // Tenancy Type
                    key = ClaimID + DW_Strings.sUnderscore + sTT;
                    aS = result.get(key);
                    j = dRecord.getTenancyType();
                    aS += DW_Strings.sCommaSpace + sTT_ + j;
                    result.put(key, aS);
                    // Under Occupancy
                    key = ClaimID + DW_Strings.sUnderscore + sUnderOccupancy;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Postcode
                    key = ClaimID + DW_Strings.sUnderscore + sP;
                    aS = result.get(key);
                    bS = dRecord.getClaimantsPostcode();
                    aS += DW_Strings.sCommaSpace + bS;
                    result.put(key, aS);
                    // Weekly Housing Benefit Entitlement
                    key = ClaimID + DW_Strings.sUnderscore + sWHBE;
                    aS = result.get(key);
                    j = dRecord.getWeeklyHousingBenefitEntitlement();
                    aS += DW_Strings.sCommaSpace + decimalise(j);
                    result.put(key, aS);
                    // Weekly Eligible Rent Amount
                    key = ClaimID + DW_Strings.sUnderscore + sWERA;
                    aS = result.get(key);
                    j = dRecord.getWeeklyEligibleRentAmount();
                    aS += DW_Strings.sCommaSpace + decimalise(j);
                    result.put(key, aS);
                    // PassportedStandardIndicator
                    key = ClaimID + DW_Strings.sUnderscore + sPSI;
                    aS = result.get(key);
                    j = dRecord.getPassportedStandardIndicator();
                    aS += DW_Strings.sCommaSpace + j;
                    result.put(key, aS);
                    // StatusOfHBClaim
                    key = ClaimID + DW_Strings.sUnderscore + sSHBC;
                    aS = result.get(key);
                    j = dRecord.getStatusOfHBClaimAtExtractDate();
                    aS += DW_Strings.sCommaSpace + j;
                    result.put(key, aS);
                    // ReasonThatHBClaimClosed
                    key = ClaimID + DW_Strings.sUnderscore + sRTHBCC;
                    aS = result.get(key);
                    j = dRecord.getReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective();
                    if (j == 0) {
                        aS += DW_Strings.sCommaSpace;
                    } else {
                        aS += DW_Strings.sCommaSpace + j;
                    }
                    result.put(key, aS);
                    // ClaimantEthnicGroup
                    key = ClaimID + DW_Strings.sUnderscore + sCEG;
                    aS = result.get(key);
                    //j = dRecord.getClaimantsEthnicGroup();
                    j = DW_SHBE_Handler.getEthnicityGroup(dRecord);
                    aS += DW_Strings.sCommaSpace + j;
                    result.put(key, aS);
                    // Household Size
                    key = ClaimID + DW_Strings.sUnderscore + sHS;
                    aS = result.get(key);
                    j = (int) DW_SHBE_Handler.getHouseholdSize(dRecord);
                    aS += DW_Strings.sCommaSpace + j;
                    result.put(key, aS);
                    // NonDependents
                    key = ClaimID + DW_Strings.sUnderscore + sND;
                    aS = result.get(key);
                    j = dRecord.getNumberOfNonDependents();
                    if (j == 0) {
                        aS += DW_Strings.sCommaSpace;
                    } else {
                        aS += DW_Strings.sCommaSpace + j;
                    }
                    result.put(key, aS);
                    // Child Dependents
                    key = ClaimID + DW_Strings.sUnderscore + sCD;
                    aS = result.get(key);
                    j = dRecord.getNumberOfChildDependents();
                    if (j == 0) {
                        aS += DW_Strings.sCommaSpace;
                    } else {
                        aS += DW_Strings.sCommaSpace + j;
                    }
                    result.put(key, aS);
                    // NonDependents (UO)
                    key = ClaimID + DW_Strings.sUnderscore + sNDUO;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // ChildrenOver16
                    key = ClaimID + DW_Strings.sUnderscore + sCO16;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // FemaleChildrenUnder10";
                    key = ClaimID + DW_Strings.sUnderscore + sFCU10;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // MaleChildrenUnder10";
                    key = ClaimID + DW_Strings.sUnderscore + sMCU10;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // FemaleChildren10to16";
                    key = ClaimID + DW_Strings.sUnderscore + sFC10To16;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // MaleChildren10to16
                    key = ClaimID + DW_Strings.sUnderscore + sMC10To16;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Number of Bedrooms
                    key = ClaimID + DW_Strings.sUnderscore + sNB;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Bedroom Requirement
                    key = ClaimID + DW_Strings.sUnderscore + sBR;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Claimants Date Of Birth
                    key = ClaimID + DW_Strings.sUnderscore + sCDoB;
                    aS = result.get(key);
                    bS = dRecord.getClaimantsDateOfBirth();
                    aS += DW_Strings.sCommaSpace + bS;
                    result.put(key, aS);
                    // ClaimantsAge
                    key = ClaimID + DW_Strings.sUnderscore + sCA;
                    aS = result.get(key);
                    bS = DW_SHBE_Handler.getClaimantsAge(year, month, dRecord);
                    aS += DW_Strings.sCommaSpace + bS;
                    result.put(key, aS);
                    // Partners Date Of Birth
                    key = ClaimID + DW_Strings.sUnderscore + sPDoB;
                    aS = result.get(key);
                    bS = dRecord.getPartnersDateOfBirth();
                    aS += DW_Strings.sCommaSpace + bS;
                    result.put(key, aS);
                    // PartnersAge
                    key = ClaimID + DW_Strings.sUnderscore + sPA;
                    aS = result.get(key);
                    bS = DW_SHBE_Handler.getPartnersAge(year, month, dRecord);
                    aS += DW_Strings.sCommaSpace + bS;
                    result.put(key, aS);
                    // ClaimantsGender
                    key = ClaimID + DW_Strings.sUnderscore + sCG;
                    aS = result.get(key);
                    bS = dRecord.getClaimantsGender();
                    aS += DW_Strings.sCommaSpace + bS;
                    result.put(key, aS);
                    // PartnersGender
                    key = ClaimID + DW_Strings.sUnderscore + sPG;
                    aS = result.get(key);
                    bS = dRecord.getPartnersGender();
                    aS += DW_Strings.sCommaSpace + bS;
                    result.put(key, aS);
                    // Disability
                    key = ClaimID + DW_Strings.sUnderscore + sDisability;
                    aS = result.get(key);
                    b = DW_SHBE_Handler.getDisability(dRecord);
                    if (b == true) {
                        aS += DW_Strings.sCommaSpace + sDisability;
                    } else {
                        aS += DW_Strings.sCommaSpace;
                    }
                    result.put(key, aS);
                    // Disability Premium
                    key = ClaimID + DW_Strings.sUnderscore + sDisabilityPremium;
                    aS = result.get(key);
                    j = dRecord.getDisabilityPremiumAwarded();
                    if (j == 1) {
                        aS += DW_Strings.sCommaSpace + sDP;
                    } else {
                        aS += DW_Strings.sCommaSpace;
                    }
                    result.put(key, aS);
                    // Disability Severe
                    key = ClaimID + DW_Strings.sUnderscore + sDisabilitySevere;
                    aS = result.get(key);
                    j = dRecord.getSevereDisabilityPremiumAwarded();
                    if (j == 1) {
                        aS += DW_Strings.sCommaSpace + sDS;
                    } else {
                        aS += DW_Strings.sCommaSpace;
                    }
                    result.put(key, aS);
                    // Disability Enhanced
                    key = ClaimID + DW_Strings.sUnderscore + sDisabilityEnhanced;
                    aS = result.get(key);
                    j = dRecord.getEnhancedDisabilityPremiumAwarded();
                    if (j == 1) {
                        aS += DW_Strings.sCommaSpace + sDE;
                    } else {
                        aS += DW_Strings.sCommaSpace;
                    }
                    result.put(key, aS);
                    // Child Disability
                    key = ClaimID + DW_Strings.sUnderscore + sDisabledChild;
                    aS = result.get(key);
                    j = dRecord.getDisabledChildPremiumAwarded();
                    if (j == 1) {
                        aS += DW_Strings.sCommaSpace + sDC;
                    } else {
                        aS += DW_Strings.sCommaSpace;
                    }
                    result.put(key, aS);
                    // Partner Death
                    key = ClaimID + DW_Strings.sUnderscore + sPDeath;
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
                    key = ClaimID + DW_Strings.sUnderscore + sHBDP;
                    aS = result.get(key);
                    j = dRecord.getWeeklyAdditionalDiscretionaryPayment();
                    aS += DW_Strings.sCommaSpace + decimalise(j);
                    result.put(key, aS);
                    // Arrears
                    key = ClaimID + DW_Strings.sUnderscore + sA;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                } else {
                    // Tenancy Type
                    key = ClaimID + DW_Strings.sUnderscore + sTT;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Under Occupancy
                    key = ClaimID + DW_Strings.sUnderscore + sUnderOccupancy;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Postcode
                    key = ClaimID + DW_Strings.sUnderscore + sP;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Weekly Housing Benefit Entitlement
                    key = ClaimID + DW_Strings.sUnderscore + sWHBE;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Weekly Eligible Rent Amount
                    key = ClaimID + DW_Strings.sUnderscore + sWERA;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // PassportedStandardIndicator
                    key = ClaimID + DW_Strings.sUnderscore + sPSI;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // StatusOfHBClaim
                    key = ClaimID + DW_Strings.sUnderscore + sSHBC;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // ReasonThatHBClaimClosed
                    key = ClaimID + DW_Strings.sUnderscore + sRTHBCC;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // ClaimantEthnicGroup
                    key = ClaimID + DW_Strings.sUnderscore + sCEG;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Household Size
                    key = ClaimID + DW_Strings.sUnderscore + sHS;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // NonDependents
                    key = ClaimID + DW_Strings.sUnderscore + sND;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // ChildDependents
                    key = ClaimID + DW_Strings.sUnderscore + sCD;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // NonDependents (UO)
                    key = ClaimID + DW_Strings.sUnderscore + sNDUO;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // ChildrenOver16
                    key = ClaimID + DW_Strings.sUnderscore + sCO16;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // FemaleChildrenUnder10";
                    key = ClaimID + DW_Strings.sUnderscore + sFCU10;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // MaleChildrenUnder10";
                    key = ClaimID + DW_Strings.sUnderscore + sMCU10;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // FemaleChildren10to16";
                    key = ClaimID + DW_Strings.sUnderscore + sFC10To16;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // MaleChildren10to16
                    key = ClaimID + DW_Strings.sUnderscore + sMC10To16;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Number of Bedrooms
                    key = ClaimID + DW_Strings.sUnderscore + sNB;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Bedroom Requirement
                    key = ClaimID + DW_Strings.sUnderscore + sBR;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Claimants Date Of Birth
                    key = ClaimID + DW_Strings.sUnderscore + sCDoB;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // ClaimantsAge
                    key = ClaimID + DW_Strings.sUnderscore + sCA;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Partners Date Of Birth
                    key = ClaimID + DW_Strings.sUnderscore + sPDoB;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Partners Age
                    key = ClaimID + DW_Strings.sUnderscore + sPA;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // ClaimantsGender
                    key = ClaimID + DW_Strings.sUnderscore + sCG;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Partners Gender
                    key = ClaimID + DW_Strings.sUnderscore + sPG;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Disability
                    key = ClaimID + DW_Strings.sUnderscore + sDisability;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Disability Premium
                    key = ClaimID + DW_Strings.sUnderscore + sDisabilityPremium;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Disability Severe
                    key = ClaimID + DW_Strings.sUnderscore + sDisabilitySevere;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Disability Enhanced
                    key = ClaimID + DW_Strings.sUnderscore + sDisabilityEnhanced;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Child Disability
                    key = ClaimID + DW_Strings.sUnderscore + sDisabledChild;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Partner Death
                    key = ClaimID + DW_Strings.sUnderscore + sPDeath;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // HB Discretionary Payment
                    key = ClaimID + DW_Strings.sUnderscore + sHBDP;
                    aS = result.get(key);
                    aS += DW_Strings.sCommaSpace;
                    result.put(key, aS);
                    // Arrears
                    key = ClaimID + DW_Strings.sUnderscore + sA;
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

        // Lookups
        HashMap<DW_ID, String> ClaimIDToCTBRefLookup;
        ClaimIDToCTBRefLookup = DW_SHBE_Handler.getClaimIDToCTBRefLookup();
        HashMap<String, DW_ID> NINOToNINOIDLookup;
        NINOToNINOIDLookup = DW_SHBE_Handler.getNINOToNINOIDLookup();
        HashMap<String, DW_ID> DOBToDOBIDLookup;
        DOBToDOBIDLookup = DW_SHBE_Handler.getDOBToDOBIDLookup();
        
        // Initialise result part 1
        TreeMap<String, String> tableValues;
        tableValues = new TreeMap<String, String>();

        TreeMap<String, DW_UO_Set> CouncilUOSets;
        TreeMap<String, DW_UO_Set> RSLUOSets;
        CouncilUOSets = DW_UO_Data.getCouncilSets();
        RSLUOSets = DW_UO_Data.getRSLSets();

        TreeSet<DW_ID> UOClaims;
        UOClaims = new TreeSet<DW_ID>();

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
        HashSet<DW_ID>[] AllClaimIDs;
        AllClaimIDs = getUOClaimIDs(
                CouncilUOSets,
                RSLUOSets,
                SHBEFilenames,
                include);
        HashSet<DW_ID> CouncilClaimIDs;
        CouncilClaimIDs = AllClaimIDs[0];
        HashSet<DW_ID> RSLClaimIDs;
        RSLClaimIDs = AllClaimIDs[1];
        HashSet<DW_ID> ClaimIDs;
        ClaimIDs = new HashSet<DW_ID>();
        ClaimIDs.addAll(CouncilClaimIDs);
        ClaimIDs.addAll(RSLClaimIDs);

        HashSet<DW_ID>[] StartUOClaimIDsX;
        StartUOClaimIDsX = getStartUOCTBRefs(
                CouncilUOSets,
                RSLUOSets,
                SHBEFilenames,
                include);
        HashSet<DW_ID> StartUOClaimIDs;
        StartUOClaimIDs = new HashSet<DW_ID>();
        StartUOClaimIDs.addAll(StartUOClaimIDsX[0]);
        StartUOClaimIDs.addAll(StartUOClaimIDsX[1]);

        HashSet<DW_ID>[] EndUOClaimIDsX;
        EndUOClaimIDsX = getEndUOClaimIDs(
                CouncilUOSets,
                RSLUOSets,
                SHBEFilenames,
                include);
        HashSet<DW_ID> EndUOClaimIDs;
        EndUOClaimIDs = new HashSet<DW_ID>();
        EndUOClaimIDs.addAll(EndUOClaimIDsX[0]);
        EndUOClaimIDs.addAll(EndUOClaimIDsX[1]);

        HashSet<DW_ID> SubsequentlyEffectedUOStillUOInOctober2015ClaimIDs;
        SubsequentlyEffectedUOStillUOInOctober2015ClaimIDs = new HashSet<DW_ID>();
        SubsequentlyEffectedUOStillUOInOctober2015ClaimIDs.addAll(ClaimIDs);
        SubsequentlyEffectedUOStillUOInOctober2015ClaimIDs.removeAll(StartUOClaimIDs);
        SubsequentlyEffectedUOStillUOInOctober2015ClaimIDs.retainAll(EndUOClaimIDs);

        TreeMap<String, BigDecimal> GeneralStatistics;
        GeneralStatistics = new TreeMap<String, BigDecimal>();
        GeneralStatistics.put(sSubsequentlyEffectedUOStillUOInOctober2015,
                BigDecimal.valueOf(SubsequentlyEffectedUOStillUOInOctober2015ClaimIDs.size()));
        HashSet<DW_ID> EndUOThatWereAlsoStartUO;
        EndUOThatWereAlsoStartUO = new HashSet<DW_ID>();
        EndUOThatWereAlsoStartUO.addAll(EndUOClaimIDs);
        EndUOThatWereAlsoStartUO.retainAll(StartUOClaimIDs);

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

        TreeMap<String, String> PreUnderOccupancyValues;
        PreUnderOccupancyValues = null;
        if (includePreUnderOccupancyValues) {
            PreUnderOccupancyValues = getPreUnderOccupancyValues(
                    ClaimIDToCTBRefLookup,
                    ClaimIDs,
                    SHBEFilenames,
                    NotMonthlyUO);
            result[4] = PreUnderOccupancyValues;
        }

        DW_SHBE_Collection aSHBEData = null;
        String aYM3;
        String year;
        String month;
        DW_UO_Set CouncilUOSet1;
        DW_UO_Set RSLUOSet1 = null;
        String aSHBEFilename;

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
        CouncilUOSet1 = CouncilUOSets.get(aYM3);
        if (CouncilUOSet1 != null) {
            RSLUOSet1 = RSLUOSets.get(aYM3);
            aSHBEData = new DW_SHBE_Collection(env, aSHBEFilename, aPT);
        }

        TreeMap<DW_ID, DW_SHBE_Record> aRecords;
        aRecords = aSHBEData.getRecords();
        DW_SHBE_Record DW_SHBE_Record;
        DW_UO_Set EndCouncilUOSet;
        EndCouncilUOSet = CouncilUOSets.get(aYM3);
        TreeMap<DW_ID, DW_UO_Record> EndCouncilUOSetMap;
        EndCouncilUOSetMap = EndCouncilUOSet.getMap();
        int inArrearsCount = 0;
        int receivingDHPCount = 0;
        int inArrearsAndReceivingDHPCount = 0;
        DW_ID ClaimID;
        DW_SHBE_D_Record aDRecord;
        /*
        * Iterate over records in tEndUOCTBRefs
         */
        Iterator<DW_ID> ClaimIDsIte;
        ClaimIDsIte = EndUOClaimIDs.iterator();
        while (ClaimIDsIte.hasNext()) {
            ClaimID = ClaimIDsIte.next();
            DW_SHBE_Record = aRecords.get(ClaimID);
            if (DW_SHBE_Record == null) {
                System.out.println(ClaimID + " not in " + aYM3);
            } else {
                aDRecord = DW_SHBE_Record.getDRecord();
                int DHP;
                DHP = aDRecord.getWeeklyAdditionalDiscretionaryPayment();
                if (DHP > 0) {
                    receivingDHPCount++;
                }
                if (EndCouncilUOSetMap.containsKey(ClaimID)) {
                    DW_UO_Record UORec;
                    UORec = EndCouncilUOSetMap.get(ClaimID);
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
        p = ((double) receivingDHPCount) * 100.0d / (double) EndUOClaimIDs.size();
        GeneralStatistics.put(sPercentageReceivingDHPInOctober2015,
                Generic_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        p = ((double) inArrearsCount) * 100.0d / (double) EndCouncilUOSetMap.size();
        GeneralStatistics.put(sPercentageInArrearsOctober2015,
                Generic_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        p = ((double) inArrearsAndReceivingDHPCount) * 100.0d / (double) EndUOClaimIDs.size();
        GeneralStatistics.put(sPercentageInArrearsAndReceivingDHPInOctober2015,
                Generic_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));

        ClaimIDsIte = EndUOThatWereAlsoStartUO.iterator();
        while (ClaimIDsIte.hasNext()) {
            ClaimID = ClaimIDsIte.next();
            DW_SHBE_Record = aRecords.get(ClaimID);
            if (DW_SHBE_Record == null) {
                System.out.println(ClaimID + " not in " + aYM3);
            } else {
                aDRecord = DW_SHBE_Record.getDRecord();
                int DHP;
                DHP = aDRecord.getWeeklyAdditionalDiscretionaryPayment();
                if (DHP > 0) {
                    receivingDHPCount++;
                }
                if (EndCouncilUOSetMap.containsKey(ClaimID)) {
                    DW_UO_Record UORec;
                    UORec = EndCouncilUOSetMap.get(ClaimID);
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
        p = ((double) receivingDHPCount) * 100.0d / (double) EndUOClaimIDs.size();
        GeneralStatistics.put(sPercentageReceivingDHPInOctober2015ThatWereUOInApril2013,
                Generic_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        p = ((double) inArrearsCount) * 100.0d / (double) EndCouncilUOSetMap.size();
        GeneralStatistics.put(sPercentageInArrearsOctober2015ThatWereUOInApril2013,
                Generic_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        p = ((double) inArrearsAndReceivingDHPCount) * 100.0d / (double) EndUOClaimIDs.size();
        GeneralStatistics.put(sPercentageInArrearsAndReceivingDHPInOctober2015ThatWereUOInApril2013,
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
//        HashSet<DW_PersonID> CouncilUniqueIndividualsEffected;
//        CouncilUniqueIndividualsEffected = new HashSet<DW_PersonID>();
        HashSet<DW_PersonID> CouncilUniqueClaimantsEffected;
        CouncilUniqueClaimantsEffected = new HashSet<DW_PersonID>();
        HashSet<DW_PersonID> CouncilUniquePartnersEffected;
        CouncilUniquePartnersEffected = new HashSet<DW_PersonID>();

        HashMap<DW_ID, Integer> tCouncilMaxNumberOfDependentsInClaimWhenUO;
        tCouncilMaxNumberOfDependentsInClaimWhenUO = new HashMap<DW_ID, Integer>();

        HashSet<DW_PersonID> CouncilUniqueNonDependentsEffected;
        CouncilUniqueNonDependentsEffected = new HashSet<DW_PersonID>();

//        HashSet<DW_PersonID> RSLUniqueIndividualsEffected;
//        RSLUniqueIndividualsEffected = new HashSet<DW_PersonID>();
        HashSet<DW_PersonID> RSLUniqueClaimantsEffected;
        RSLUniqueClaimantsEffected = new HashSet<DW_PersonID>();
        HashSet<DW_PersonID> RSLUniquePartnersEffected;
        RSLUniquePartnersEffected = new HashSet<DW_PersonID>();
//        HashSet<DW_PersonID> RSLUniqueDependentsEffected;
//        RSLUniqueDependentsEffected = new HashSet<DW_PersonID>();

        HashMap<DW_ID, Integer> RSLMaxNumberOfDependentsInClaimWhenUO;
        RSLMaxNumberOfDependentsInClaimWhenUO = new HashMap<DW_ID, Integer>();

        HashSet<DW_PersonID> RSLUniqueNonDependentsEffected;
        RSLUniqueNonDependentsEffected = new HashSet<DW_PersonID>();

        HashSet<DW_PersonID> UniqueDependentChildrenUnderAge10Effected;
        UniqueDependentChildrenUnderAge10Effected = new HashSet<DW_PersonID>();

        // groups is for ordering the table output. Keys are the group type and 
        // values are ordered sets of keys for writing rows.
        HashMap<String, TreeSet<DW_ID>> groups;
        groups = new HashMap<String, TreeSet<DW_ID>>();

        TreeSet<DW_ID> PermanantlyLeftUOButRemainedInSHBEClaimIDs;
        PermanantlyLeftUOButRemainedInSHBEClaimIDs = new TreeSet<DW_ID>();
        groups.put(sPermanantlyLeftUOButRemainedInSHBE, PermanantlyLeftUOButRemainedInSHBEClaimIDs);

        TreeSet<DW_ID> ClaimIDs_PermanantlyLeftUOButRemainedInSHBE_PostcodeChanged;
        ClaimIDs_PermanantlyLeftUOButRemainedInSHBE_PostcodeChanged = new TreeSet<DW_ID>();
        groups.put(sPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged, ClaimIDs_PermanantlyLeftUOButRemainedInSHBE_PostcodeChanged);

        TreeSet<DW_ID> ClaimIDs_PermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased;
        ClaimIDs_PermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased = new TreeSet<DW_ID>();
        groups.put(sPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased, ClaimIDs_PermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased);

        TreeSet<DW_ID> ClaimIDs_Travellers;
        ClaimIDs_Travellers = new TreeSet<DW_ID>();
        groups.put(sTravellers, ClaimIDs_Travellers);

        TreeSet<DW_ID> ClaimIDs_TTNot1Or4AndUnderOccupying;
        ClaimIDs_TTNot1Or4AndUnderOccupying = new TreeSet<DW_ID>();
        groups.put(sTTNot1Or4AndUnderOccupying, ClaimIDs_TTNot1Or4AndUnderOccupying);

        TreeSet<DW_ID> ClaimIDs_TT1_To_TT3;
        ClaimIDs_TT1_To_TT3 = new TreeSet<DW_ID>();
        groups.put(sTT1_To_TT3OrTT6, ClaimIDs_TT1_To_TT3);

        TreeSet<DW_ID> ClaimIDs_TT4_To_TT3;
        ClaimIDs_TT4_To_TT3 = new TreeSet<DW_ID>();
        groups.put(sTT4_To_TT3OrTT6, ClaimIDs_TT4_To_TT3);

        TreeSet<DW_ID> ClaimIDs_TT3_To_TT1;
        ClaimIDs_TT3_To_TT1 = new TreeSet<DW_ID>();
        groups.put(sTT3OrTT6_To_TT1, ClaimIDs_TT3_To_TT1);

        TreeSet<DW_ID> ClaimIDs_TT3_To_TT4;
        ClaimIDs_TT3_To_TT4 = new TreeSet<DW_ID>();
        groups.put(sTT3OrTT6_To_TT4, ClaimIDs_TT3_To_TT4);

//        TreeSet<DW_ID> ClaimIDs_NoValidPostcodeChange;
//        ClaimIDs_NoValidPostcodeChange = new TreeSet<DW_ID>();
//        ClaimIDs_NoValidPostcodeChange.addAll(tCTBRefs);
//        groups.put(sNoValidPostcodeChange, ClaimIDs_NoValidPostcodeChange);
        TreeSet<DW_ID> ClaimIDs_ValidPostcodeChange;
        ClaimIDs_ValidPostcodeChange = new TreeSet<DW_ID>();
        groups.put(sNoValidPostcodeChange, ClaimIDs_ValidPostcodeChange);

        TreeSet<DW_ID> ClaimIDs_ChangedTT;
        ClaimIDs_ChangedTT = new TreeSet<DW_ID>();
        groups.put(sChangedTT, ClaimIDs_ChangedTT);

        TreeSet<DW_ID> ClaimIDs_UOAtSomePoint;
        ClaimIDs_UOAtSomePoint = new TreeSet<DW_ID>();
        groups.put(sUOAtSomePoint, ClaimIDs_UOAtSomePoint);

        TreeSet<DW_ID> ClaimIDs_UOTT1AtSomePoint;
        ClaimIDs_UOTT1AtSomePoint = new TreeSet<DW_ID>();
        groups.put(sUOTT1AtSomePoint, ClaimIDs_UOTT1AtSomePoint);

        TreeSet<DW_ID> ClaimIDs_UOTT4AtSomePoint;
        ClaimIDs_UOTT4AtSomePoint = new TreeSet<DW_ID>();
        groups.put(sUOTT4AtSomePoint, ClaimIDs_UOTT4AtSomePoint);

        TreeSet<DW_ID> AlwaysUOTT1FromStartClaimIDs;
        AlwaysUOTT1FromStartClaimIDs = new TreeSet<DW_ID>();
        AlwaysUOTT1FromStartClaimIDs.addAll(CouncilClaimIDs);
        groups.put(sAlwaysUOTT1FromStart, AlwaysUOTT1FromStartClaimIDs);

        TreeSet<DW_ID> ClaimIDs_AlwaysUOTT1FromStartExceptWhenSuspended;
        ClaimIDs_AlwaysUOTT1FromStartExceptWhenSuspended = new TreeSet<DW_ID>();
        ClaimIDs_AlwaysUOTT1FromStartExceptWhenSuspended.addAll(CouncilClaimIDs);
        groups.put(sAlwaysUOTT1FromStartExceptWhenSuspended, ClaimIDs_AlwaysUOTT1FromStartExceptWhenSuspended);

        TreeSet<DW_ID> AlwaysUOTT1FromWhenStartedClaimIDs;
        AlwaysUOTT1FromWhenStartedClaimIDs = new TreeSet<DW_ID>();
        //ClaimIDs_AlwaysUOFromWhenStarted.addAll(tCTBRefs);
        groups.put(sAlwaysUOTT1FromWhenStarted, AlwaysUOTT1FromWhenStartedClaimIDs);

        TreeSet<DW_ID> ClaimIDs_AlwaysUOTT4FromStart;
        ClaimIDs_AlwaysUOTT4FromStart = new TreeSet<DW_ID>();
        ClaimIDs_AlwaysUOTT4FromStart.addAll(RSLClaimIDs);
        groups.put(sAlwaysUOTT4FromStart, ClaimIDs_AlwaysUOTT4FromStart);

        TreeSet<DW_ID> ClaimIDs_AlwaysUOTT4FromStartExceptWhenSuspended;
        ClaimIDs_AlwaysUOTT4FromStartExceptWhenSuspended = new TreeSet<DW_ID>();
        ClaimIDs_AlwaysUOTT4FromStartExceptWhenSuspended.addAll(RSLClaimIDs);
        groups.put(sAlwaysUOTT4FromStartExceptWhenSuspended, ClaimIDs_AlwaysUOTT4FromStartExceptWhenSuspended);

        TreeSet<DW_ID> AlwaysUOTT4FromWhenStartedClaimIDs;
        AlwaysUOTT4FromWhenStartedClaimIDs = new TreeSet<DW_ID>();
        //ClaimIDs_AlwaysUOFromWhenStarted.addAll(tCTBRefs);
        groups.put(sAlwaysUOTT4FromWhenStarted, AlwaysUOTT4FromWhenStartedClaimIDs);

        TreeSet<DW_ID> IntermitantUOClaimIDs;
        IntermitantUOClaimIDs = new TreeSet<DW_ID>();
        groups.put(sIntermitantUO, IntermitantUOClaimIDs);

        TreeSet<DW_ID> ClaimIDs_UO_To_LeftSHBEAtSomePoint;
        ClaimIDs_UO_To_LeftSHBEAtSomePoint = new TreeSet<DW_ID>();
        groups.put(sUO_To_LeftSHBEAtSomePoint, ClaimIDs_UO_To_LeftSHBEAtSomePoint);

        TreeSet<DW_ID> ClaimIDs_UO_To_LeftSHBETheVeryNextMonth;
        ClaimIDs_UO_To_LeftSHBETheVeryNextMonth = new TreeSet<DW_ID>();
        groups.put(sUO_To_LeftSHBE, ClaimIDs_UO_To_LeftSHBETheVeryNextMonth);

        TreeSet<DW_ID> ClaimIDs_UO_To_LeftSHBEBetweenOneAndTwoMonths;
        ClaimIDs_UO_To_LeftSHBEBetweenOneAndTwoMonths = new TreeSet<DW_ID>();
        groups.put(sUO_To_LeftSHBEBetweenOneAndTwoMonths, ClaimIDs_UO_To_LeftSHBEBetweenOneAndTwoMonths);

        TreeSet<DW_ID> ClaimIDs_UO_To_LeftSHBEBetweenTwoAndThreeMonths;
        ClaimIDs_UO_To_LeftSHBEBetweenTwoAndThreeMonths = new TreeSet<DW_ID>();
        groups.put(sUO_To_LeftSHBEBetweenTwoAndThreeMonths, ClaimIDs_UO_To_LeftSHBEBetweenTwoAndThreeMonths);

        TreeSet<DW_ID> ClaimIDs_UO_To_LeftSHBEAndNotReturned;
        ClaimIDs_UO_To_LeftSHBEAndNotReturned = new TreeSet<DW_ID>();
        groups.put(sUO_To_LeftSHBE_NotReturned, ClaimIDs_UO_To_LeftSHBEAndNotReturned);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBEAndNotReturned;
        ClaimIDs_UOTT1_To_LeftSHBEAndNotReturned = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_LeftSHBE_NotReturned, ClaimIDs_UOTT1_To_LeftSHBEAndNotReturned);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBEAndNotReturned;
        ClaimIDs_UOTT4_To_LeftSHBEAndNotReturned = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_LeftSHBE_NotReturned, ClaimIDs_UOTT4_To_LeftSHBEAndNotReturned);

        TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBEAndNotReturned;
        ClaimIDs_UOTT3OrTT6_To_LeftSHBEAndNotReturned = new TreeSet<DW_ID>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_NotReturned, ClaimIDs_UOTT3OrTT6_To_LeftSHBEAndNotReturned);

        TreeSet<DW_ID> ClaimIDs_UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEAndNotReturned;
        ClaimIDs_UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEAndNotReturned = new TreeSet<DW_ID>();
        groups.put(sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned, ClaimIDs_UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEAndNotReturned);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT1;
        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT1 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAsUOTT1, ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT1);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT1;
        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT1 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT1, ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT1);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6;
        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6, ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT4;
        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT4 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAsUOTT4, ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT4);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT4;
        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT4 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT4, ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT4);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7;
        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7, ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT8;
        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT8 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT8, ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT8);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT9;
        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT9 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT9, ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT9);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsUOTT1;
        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsUOTT1 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAsUOTT1, ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsUOTT1);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT1;
        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT1 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT1, ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT1);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6;
        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6, ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsUOTT4;
        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsUOTT4 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAsUOTT4, ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsUOTT4);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT4;
        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT4 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT4, ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT4);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7;
        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7, ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT8;
        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT8 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT8, ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT8);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT9;
        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT9 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT9, ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT9);

        TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1;
        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1 = new TreeSet<DW_ID>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1, ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1);

        TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1;
        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1 = new TreeSet<DW_ID>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1, ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1);

        TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6;
        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6 = new TreeSet<DW_ID>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6, ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6);

        TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4;
        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4 = new TreeSet<DW_ID>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4, ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4);

        TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4;
        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4 = new TreeSet<DW_ID>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4, ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4);

        TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7;
        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7 = new TreeSet<DW_ID>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7, ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7);

        TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8;
        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8 = new TreeSet<DW_ID>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8, ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8);

        TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9;
        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9 = new TreeSet<DW_ID>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9, ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint;
        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint;
        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE;
        ClaimIDs_UOTT1_To_LeftSHBE = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_LeftSHBE, ClaimIDs_UOTT1_To_LeftSHBE);

//        TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBEReturnedAsTT1orTT4;
//        ClaimIDs_UOTT1_To_LeftSHBEReturnedAsTT1orTT4 = new TreeSet<DW_ID>();
//        groups.put(sUOTT1_To_LeftSHBEReturnedAsTT1orTT4, ClaimIDs_UOTT1_To_LeftSHBEReturnedAsTT1orTT4);
//
//        TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBEReturnedAsTT3OrTT6;
//        ClaimIDs_UOTT1_To_LeftSHBEReturnedAsTT3OrTT6 = new TreeSet<DW_ID>();
//        groups.put(sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6, ClaimIDs_UOTT1_To_LeftSHBEReturnedAsTT3OrTT6);
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE;
        ClaimIDs_UOTT4_To_LeftSHBE = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_LeftSHBE, ClaimIDs_UOTT4_To_LeftSHBE);

//        TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBEReturnedAsTT1orTT4;
//        ClaimIDs_UOTT4_To_LeftSHBEReturnedAsTT1orTT4 = new TreeSet<DW_ID>();
//        groups.put(sUOTT4_To_LeftSHBEReturnedAsTT1orTT4, ClaimIDs_UOTT4_To_LeftSHBEReturnedAsTT1orTT4);
//
//        TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBEReturnedAsTT3OrTT6;
//        ClaimIDs_UOTT4_To_LeftSHBEReturnedAsTT3OrTT6 = new TreeSet<DW_ID>();
//        groups.put(sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6, ClaimIDs_UOTT4_To_LeftSHBEReturnedAsTT3OrTT6);
        TreeSet<DW_ID> ClaimIDs_UO_NotUO;
        ClaimIDs_UO_NotUO = new TreeSet<DW_ID>();

        TreeSet<DW_ID> ClaimIDs_UO_NotUO_UO_NotUO;
        ClaimIDs_UO_NotUO_UO_NotUO = new TreeSet<DW_ID>();
        groups.put(sUO_NotUO_UO_NotUO, ClaimIDs_UO_NotUO_UO_NotUO);

        TreeSet<DW_ID> ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO;
        ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO = new TreeSet<DW_ID>();
        groups.put(sUO_NotUO_UO_NotUO_UO_NotUO, ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO);

        TreeSet<DW_ID> ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO;
        ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO = new TreeSet<DW_ID>();
        groups.put(sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO, ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO);

        TreeSet<DW_ID> ClaimIDs_UO_NotUO_UO;
        ClaimIDs_UO_NotUO_UO = new TreeSet<DW_ID>();
        groups.put(sUO_NotUO_UO, ClaimIDs_UO_NotUO_UO);

        TreeSet<DW_ID> ClaimIDs_UO_NotUO_UO_NotUO_UO;
        ClaimIDs_UO_NotUO_UO_NotUO_UO = new TreeSet<DW_ID>();
        groups.put(sUO_NotUO_UO_NotUO_UO, ClaimIDs_UO_NotUO_UO_NotUO_UO);

        TreeSet<DW_ID> ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO;
        ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO = new TreeSet<DW_ID>();
        groups.put(sUO_NotUO_UO_NotUO_UO_NotUO_UO, ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO);

        TreeSet<DW_ID> ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO;
        ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO = new TreeSet<DW_ID>();
        groups.put(sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO, ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_NotUO_InSHBE_PostcodeChanged;
        ClaimIDs_UOTT1_To_NotUO_InSHBE_PostcodeChanged = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_NotUO_InSHBE_PostcodeChanged, ClaimIDs_UOTT1_To_NotUO_InSHBE_PostcodeChanged);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_UOTT1_PostcodeChanged;
        ClaimIDs_UOTT1_To_UOTT1_PostcodeChanged = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_UOTT1_PostcodeChanged, ClaimIDs_UOTT1_To_UOTT1_PostcodeChanged);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeChanged;
        ClaimIDs_UOTT1_To_TT1_PostcodeChanged = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT1_PostcodeChanged, ClaimIDs_UOTT1_To_TT1_PostcodeChanged);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_UOTT4_PostcodeChanged;
        ClaimIDs_UOTT1_To_UOTT4_PostcodeChanged = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_UOTT4_PostcodeChanged, ClaimIDs_UOTT1_To_UOTT4_PostcodeChanged);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT4_PostcodeChanged;
        ClaimIDs_UOTT1_To_TT4_PostcodeChanged = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT4_PostcodeChanged, ClaimIDs_UOTT1_To_TT4_PostcodeChanged);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_NotUO_InSHBE_PostcodeChanged;
        ClaimIDs_UOTT4_To_NotUO_InSHBE_PostcodeChanged = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_NotUO_InSHBE_PostcodeChanged, ClaimIDs_UOTT4_To_NotUO_InSHBE_PostcodeChanged);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_UOTT1_PostcodeChanged;
        ClaimIDs_UOTT4_To_UOTT1_PostcodeChanged = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_UOTT1_PostcodeChanged, ClaimIDs_UOTT4_To_UOTT1_PostcodeChanged);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT1_PostcodeChanged;
        ClaimIDs_UOTT4_To_TT1_PostcodeChanged = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT1_PostcodeChanged, ClaimIDs_UOTT4_To_TT1_PostcodeChanged);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_UOTT4_PostcodeChanged;
        ClaimIDs_UOTT4_To_UOTT4_PostcodeChanged = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_UOTT4_PostcodeChanged, ClaimIDs_UOTT4_To_UOTT4_PostcodeChanged);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeChanged;
        ClaimIDs_UOTT4_To_TT4_PostcodeChanged = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT4_PostcodeChanged, ClaimIDs_UOTT4_To_TT4_PostcodeChanged);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT3OrTT6;
        ClaimIDs_UOTT1_To_TT3OrTT6 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT3OrTT6, ClaimIDs_UOTT1_To_TT3OrTT6);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT3OrTT6AtSomePoint;
        ClaimIDs_UOTT1_To_TT3OrTT6AtSomePoint = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT3OrTT6AtSomePoint, ClaimIDs_UOTT1_To_TT3OrTT6AtSomePoint);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999;
        ClaimIDs_UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999, ClaimIDs_UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint;
        ClaimIDs_UOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint, ClaimIDs_UOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT3OrTT6NotDoneNextChange;
        ClaimIDs_UOTT1_To_TT3OrTT6NotDoneNextChange = new TreeSet<DW_ID>();

        TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE;
        ClaimIDs_UOTT3OrTT6_To_LeftSHBE = new TreeSet<DW_ID>();
        groups.put(sUOTT3OrTT6_To_LeftSHBE, ClaimIDs_UOTT3OrTT6_To_LeftSHBE);

        TreeSet<DW_ID> ClaimIDs_UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE;
        ClaimIDs_UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE = new TreeSet<DW_ID>();
        groups.put(sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE, ClaimIDs_UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT3OrTT6;
        ClaimIDs_UOTT4_To_TT3OrTT6 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT3OrTT6, ClaimIDs_UOTT4_To_TT3OrTT6);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT3OrTT6AtSomePoint;
        ClaimIDs_UOTT4_To_TT3OrTT6AtSomePoint = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT3OrTT6AtSomePoint, ClaimIDs_UOTT4_To_TT3OrTT6AtSomePoint);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999;
        ClaimIDs_UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999, ClaimIDs_UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint;
        ClaimIDs_UOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint, ClaimIDs_UOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT3OrTT6NotDoneNextChange;
        ClaimIDs_UOTT4_To_TT3OrTT6NotDoneNextChange = new TreeSet<DW_ID>();

        TreeSet<DW_ID> ClaimIDs_TT3OrTT6_To_UOTT1;
        ClaimIDs_TT3OrTT6_To_UOTT1 = new TreeSet<DW_ID>();
        groups.put(sTT3OrTT6_To_UOTT1, ClaimIDs_TT3OrTT6_To_UOTT1);

        TreeSet<DW_ID> ClaimIDs_TT3OrTT6_To_UOTT4;
        ClaimIDs_TT3OrTT6_To_UOTT4 = new TreeSet<DW_ID>();
        groups.put(sTT3OrTT6_To_UOTT4, ClaimIDs_TT3OrTT6_To_UOTT4);

        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged;
        ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged = new TreeSet<DW_ID>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchanged, ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged);
//        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month;
//        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month = new TreeSet<DW_ID>();
//        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
//                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month);
        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1;
        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1 = new TreeSet<DW_ID>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1);
        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1;
        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1 = new TreeSet<DW_ID>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1);
        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6;
        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 = new TreeSet<DW_ID>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6);
        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4;
        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4 = new TreeSet<DW_ID>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4);
        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4;
        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4 = new TreeSet<DW_ID>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4);
        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7;
        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 = new TreeSet<DW_ID>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7);
        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8;
        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8 = new TreeSet<DW_ID>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8);
        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9;
        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9 = new TreeSet<DW_ID>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9);

        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months;
        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months = new TreeSet<DW_ID>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months);
        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months;
        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months = new TreeSet<DW_ID>();
        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months,
                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months);

        //TreeSet<DW_ID> ClaimIDs_UOTT1OrTT1_To_UOTT4;
        //ClaimIDs_UOTT1OrTT1_To_UOTT4 = new TreeSet<DW_ID>();
        //groups.put(sUOTT1OrTT1_To_UOTT4, ClaimIDs_UOTT1OrTT1_To_UOTT4);
        TreeSet<DW_ID> ClaimIDs_UOTT1_To_UOTT4;
        ClaimIDs_UOTT1_To_UOTT4 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_UOTT4, ClaimIDs_UOTT1_To_UOTT4);
        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT4;
        ClaimIDs_TT1_To_UOTT4 = new TreeSet<DW_ID>();
        groups.put(sTT1_To_UOTT4, ClaimIDs_TT1_To_UOTT4);
        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT4GettingDHP;
        ClaimIDs_TT1_To_UOTT4GettingDHP = new TreeSet<DW_ID>();
        groups.put(sTT1_To_UOTT4GettingDHP, ClaimIDs_TT1_To_UOTT4GettingDHP);

        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged;
        ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged = new TreeSet<DW_ID>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchanged, ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged);
//        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month;
//        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month = new TreeSet<DW_ID>();
//        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
//                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month);        
        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1;
        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1 = new TreeSet<DW_ID>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1);
        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1;
        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1 = new TreeSet<DW_ID>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1);
        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6;
        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 = new TreeSet<DW_ID>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6);
        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4;
        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4 = new TreeSet<DW_ID>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4);
        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4;
        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4 = new TreeSet<DW_ID>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4);
        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7;
        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 = new TreeSet<DW_ID>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7);
        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8;
        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8 = new TreeSet<DW_ID>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8);
        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9;
        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9 = new TreeSet<DW_ID>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9);

        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months;
        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months = new TreeSet<DW_ID>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months);
        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months;
        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months = new TreeSet<DW_ID>();
        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months,
                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months);

        //TreeSet<DW_ID> ClaimIDs_UOTT4OrTT4_To_UOTT1;
        //ClaimIDs_UOTT4OrTT4_To_UOTT1 = new TreeSet<DW_ID>();
        //groups.put(sUOTT4OrTT4_To_UOTT1, ClaimIDs_UOTT4OrTT4_To_UOTT1);
        //TreeSet<DW_ID> ClaimIDs_UOTT4OrTT4_To_UOTT1InArrears;
        //ClaimIDs_UOTT4OrTT4_To_UOTT1InArrears = new TreeSet<DW_ID>();
        //groups.put(sUOTT4OrTT4_To_UOTT1InArrears, ClaimIDs_UOTT4OrTT4_To_UOTT1InArrears);
        //TreeSet<DW_ID> ClaimIDs_UOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP;
        //ClaimIDs_UOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP = new TreeSet<DW_ID>();
        //groups.put(sUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
        //        ClaimIDs_UOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP);
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_UOTT1;
        ClaimIDs_UOTT4_To_UOTT1 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_UOTT1, ClaimIDs_UOTT4_To_UOTT1);
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_UOTT1InArrears;
        ClaimIDs_UOTT4_To_UOTT1InArrears = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_UOTT1InArrears, ClaimIDs_UOTT4_To_UOTT1InArrears);
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_UOTT1GettingDHP;
        ClaimIDs_UOTT4_To_UOTT1GettingDHP = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_UOTT1GettingDHP,
                ClaimIDs_UOTT4_To_UOTT1GettingDHP);
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_UOTT1InArrearsAndGettingDHP;
        ClaimIDs_UOTT4_To_UOTT1InArrearsAndGettingDHP = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_UOTT1InArrearsAndGettingDHP,
                ClaimIDs_UOTT4_To_UOTT1InArrearsAndGettingDHP);
        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT1;
        ClaimIDs_TT4_To_UOTT1 = new TreeSet<DW_ID>();
        groups.put(sTT4_To_UOTT1, ClaimIDs_TT4_To_UOTT1);
        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT1InArrears;
        ClaimIDs_TT4_To_UOTT1InArrears = new TreeSet<DW_ID>();
        groups.put(sTT4_To_UOTT1InArrears, ClaimIDs_TT4_To_UOTT1InArrears);
        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT1GettingDHP;
        ClaimIDs_TT4_To_UOTT1GettingDHP = new TreeSet<DW_ID>();
        groups.put(sTT4_To_UOTT1GettingDHP,
                ClaimIDs_TT4_To_UOTT1GettingDHP);
        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT1InArrearsAndGettingDHP;
        ClaimIDs_TT4_To_UOTT1InArrearsAndGettingDHP = new TreeSet<DW_ID>();
        groups.put(sTT4_To_UOTT1InArrearsAndGettingDHP,
                ClaimIDs_TT4_To_UOTT1InArrearsAndGettingDHP);

        TreeSet<DW_ID> ClaimIDs_InArrearsAtSomePoint;
        ClaimIDs_InArrearsAtSomePoint = new TreeSet<DW_ID>();
        TreeSet<DW_ID> ClaimIDs_DHPAtSomePoint;
        ClaimIDs_DHPAtSomePoint = new TreeSet<DW_ID>();
        TreeSet<DW_ID> ClaimIDs_InArrearsAtSomePoint_And_DHPAtSomePoint;
        ClaimIDs_InArrearsAtSomePoint_And_DHPAtSomePoint = new TreeSet<DW_ID>();
        groups.put(sInArrearsAtSomePoint_And_DHPAtSomePoint,
                ClaimIDs_InArrearsAtSomePoint_And_DHPAtSomePoint);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged;
        ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchanged, ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged);
//        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month;
//        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month = new TreeSet<DW_ID>();
//        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
//                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month);
        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1;
        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1);
        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1;
        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1);
        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6;
        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6);
        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4;
        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4);
        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4;
        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4);
        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7;
        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7);
        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8;
        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8);
        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9;
        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9 = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9);

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months;
        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months);
        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months;
        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months,
                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months);

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged;
        ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchanged, ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged);
//        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month;
//        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month = new TreeSet<DW_ID>();
//        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
//                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month);
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1;
        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1);
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1;
        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1);
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6;
        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6);
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4;
        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4);
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4;
        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4);
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7;
        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7);
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8;
        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8);
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9;
        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9 = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9);
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months;
        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months);
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months;
        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months,
                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months);

        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged1MonthPrevious;
        ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged1MonthPrevious = new TreeSet<DW_ID>();
        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious;
        ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious = new TreeSet<DW_ID>();
        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged3MonthsPrevious;
        ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged3MonthsPrevious = new TreeSet<DW_ID>();

        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged1MonthPrevious;
        ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged1MonthPrevious = new TreeSet<DW_ID>();
        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious;
        ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious = new TreeSet<DW_ID>();
        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged3MonthsPrevious;
        ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged3MonthsPrevious = new TreeSet<DW_ID>();

        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged1MonthPrevious;
        ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged1MonthPrevious = new TreeSet<DW_ID>();
        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious;
        ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious = new TreeSet<DW_ID>();
        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged3MonthsPrevious;
        ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged3MonthsPrevious = new TreeSet<DW_ID>();

        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged1MonthPrevious;
        ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged1MonthPrevious = new TreeSet<DW_ID>();
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious;
        ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious = new TreeSet<DW_ID>();
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged3MonthsPrevious;
        ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged3MonthsPrevious = new TreeSet<DW_ID>();

        TreeSet<DW_ID> ClaimIDs_UOTT1_ToTT1_PostcodeChanged;
        ClaimIDs_UOTT1_ToTT1_PostcodeChanged = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_TT1_PostcodeChanged, ClaimIDs_UOTT1_ToTT1_PostcodeChanged);

        TreeSet<DW_ID> ClaimIDs_UOTT1_ToUOTT1_PostcodeChanged;
        ClaimIDs_UOTT1_ToUOTT1_PostcodeChanged = new TreeSet<DW_ID>();
        groups.put(sUOTT1_To_UOTT1_PostcodeChanged, ClaimIDs_UOTT1_ToUOTT1_PostcodeChanged);

        TreeSet<DW_ID> ClaimIDs_UOTT4_ToTT4_PostcodeChanged;
        ClaimIDs_UOTT4_ToTT4_PostcodeChanged = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_TT4_PostcodeChanged, ClaimIDs_UOTT4_ToTT4_PostcodeChanged);

        TreeSet<DW_ID> ClaimIDs_UOTT4_ToUOTT4_PostcodeChanged;
        ClaimIDs_UOTT4_ToUOTT4_PostcodeChanged = new TreeSet<DW_ID>();
        groups.put(sUOTT4_To_UOTT4_PostcodeChanged, ClaimIDs_UOTT4_ToUOTT4_PostcodeChanged);

        TreeSet<DW_ID> ClaimIDs_TT1_ToUOTT1_PostcodeChanged;
        ClaimIDs_TT1_ToUOTT1_PostcodeChanged = new TreeSet<DW_ID>();
        groups.put(sTT1_To_UOTT1_PostcodeChanged, ClaimIDs_TT1_ToUOTT1_PostcodeChanged);

        TreeSet<DW_ID> ClaimIDs_TT4_ToUOTT4_PostcodeChanged;
        ClaimIDs_TT4_ToUOTT4_PostcodeChanged = new TreeSet<DW_ID>();
        groups.put(sTT4_To_UOTT4_PostcodeChanged, ClaimIDs_TT4_ToUOTT4_PostcodeChanged);

        TreeSet<DW_ID> ClaimIDs_UOClaimsRecievingDHP;
        ClaimIDs_UOClaimsRecievingDHP = new TreeSet<DW_ID>();
        groups.put(sUOClaimsRecievingDHP, ClaimIDs_UOClaimsRecievingDHP);

        TreeSet<DW_ID> ClaimIDs_UOTT1ClaimsInRentArrearsAtSomePoint;
        ClaimIDs_UOTT1ClaimsInRentArrearsAtSomePoint = new TreeSet<DW_ID>();
        groups.put(sUOTT1ClaimsInRentArrearsAtSomePoint, ClaimIDs_UOTT1ClaimsInRentArrearsAtSomePoint);

        TreeSet<DW_ID> ClaimIDs_UOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint;
        ClaimIDs_UOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint = new TreeSet<DW_ID>();
        groups.put(sUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint, ClaimIDs_UOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint);

//        String aCTBRef;
//        Iterator<String> tCTBRefsIte;
        // Initialise aggregateStatistics and generalStatistics
        TreeMap<String, BigDecimal> aggregateStatistics;
        aggregateStatistics = new TreeMap<String, BigDecimal>();
        ClaimIDsIte = ClaimIDs.iterator();
        while (ClaimIDsIte.hasNext()) {
            ClaimID = ClaimIDsIte.next();
            aggregateStatistics.put(ClaimID + DW_Strings.sUnderscore + sTotal_DHP, BigDecimal.ZERO);
            aggregateStatistics.put(ClaimID + DW_Strings.sUnderscore + sTotalCount_DHP, BigDecimal.ZERO);
            aggregateStatistics.put(ClaimID + DW_Strings.sUnderscore + sTotal_HBLossDueToUO, BigDecimal.ZERO);
            aggregateStatistics.put(ClaimID + DW_Strings.sUnderscore + sTotalCount_HBLossDueToUO, BigDecimal.ZERO);
            aggregateStatistics.put(ClaimID + DW_Strings.sUnderscore + sMax_Arrears, BigDecimal.ZERO);
            aggregateStatistics.put(ClaimID + DW_Strings.sUnderscore + sTotalCount_Arrears, BigDecimal.ZERO);
            aggregateStatistics.put(ClaimID + DW_Strings.sUnderscore + sTotalCount_UnderOccupancy, BigDecimal.ZERO);
        }

        // Use sets?
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOInApril2013, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOAfterApril2013, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOAfterChangePostcodeAndOrTT, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ChangePostcodeAndOrTTToAvoidUO, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_StayPutAndAvoidedUO, BigDecimal.ZERO);
//        generalStatistics.put(sCostOfUOToTaxPayer, BigDecimal.ZERO);
        HashMap<DW_ID, Integer> DHP_Totals;
        DHP_Totals = new HashMap<DW_ID, Integer>();
        // Initialise tableValues (part 2) and DHP_Totals
        String CTBRef;
        ClaimIDsIte = ClaimIDs.iterator();
        while (ClaimIDsIte.hasNext()) {
            ClaimID = ClaimIDsIte.next();
            CTBRef = ClaimIDToCTBRefLookup.get(ClaimID);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sTT, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sUnderOccupancy, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sP, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sWHBE, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sWERA, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sPSI, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sSHBC, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sRTHBCC, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sCEG, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sHS, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sND, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sCD, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sNDUO, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sCO16, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sFCU10, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sMCU10, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sFC10To16, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sMC10To16, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sNB, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sBR, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sCDoB, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sCA, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sPDoB, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sPA, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sCG, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sPG, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sDisability, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sDisabilityPremium, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sDisabilitySevere, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sDisabilityEnhanced, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sDisabledChild, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sPDeath, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sHBDP, s);
            tableValues.put(CTBRef + DW_Strings.sUnderscore + sA, s);
            DHP_Totals.put(ClaimID, 0);
        }

        // Load dataset 1
        Iterator<Integer> includeIte;
        includeIte = include.iterator();
        int i;

        String header;
        header = "ClaimRef, ";
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
            CouncilUOSet1 = CouncilUOSets.get(aYM3);
            if (CouncilUOSet1 != null) {
                RSLUOSet1 = RSLUOSets.get(aYM3);
                aSHBEData = new DW_SHBE_Collection(env, aSHBEFilename, aPT);
                initFirst = true;
            }
            header += aYM3;
        }
        //TreeMap<String, DW_SHBE_Record> aRecords;
        aRecords = aSHBEData.getRecords();
        TreeMap<DW_ID, DW_SHBE_Record> bRecords;
        bRecords = null;
        TreeMap<DW_ID, DW_SHBE_Record> cRecords;
        cRecords = null;

        //DW_SHBE_Record aDW_SHBE_Record;
        TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedThisMonth;
        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedThisMonth = new TreeSet<DW_ID>();
        TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedThisMonth;
        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedThisMonth = new TreeSet<DW_ID>();
        TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedThisMonth;
        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedThisMonth = new TreeSet<DW_ID>();
        TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedThisMonth;
        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedThisMonth = new TreeSet<DW_ID>();

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
        ClaimIDsIte = ClaimIDs.iterator();
        while (ClaimIDsIte.hasNext()) {
            ClaimID = ClaimIDsIte.next();
            DW_SHBE_Record = aRecords.get(ClaimID);
            processResult = process(
                    NINOToNINOIDLookup,
                    DOBToDOBIDLookup,
                    ClaimIDToCTBRefLookup,
                    UOClaims,
                    aggregateStatistics,
                    GeneralStatistics,
                    ClaimID,
                    year,
                    month,
                    aYM3,
                    DW_SHBE_Record,
                    bRecords,
                    cRecords,
                    tableValues,
                    CouncilUOSet1,
                    RSLUOSet1,
                    CouncilUniqueClaimantsEffected,
                    CouncilUniquePartnersEffected,
                    tCouncilMaxNumberOfDependentsInClaimWhenUO,
                    CouncilUniqueNonDependentsEffected,
                    RSLUniqueClaimantsEffected,
                    RSLUniquePartnersEffected,
                    RSLMaxNumberOfDependentsInClaimWhenUO,
                    RSLUniqueNonDependentsEffected,
                    UniqueDependentChildrenUnderAge10Effected,
                    PermanantlyLeftUOButRemainedInSHBEClaimIDs,
                    ClaimIDs_PermanantlyLeftUOButRemainedInSHBE_PostcodeChanged,
                    ClaimIDs_PermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased,
                    ClaimIDs_Travellers,
                    ClaimIDs_TTNot1Or4AndUnderOccupying,
                    ClaimIDs_TT1_To_TT3,
                    ClaimIDs_TT4_To_TT3,
                    ClaimIDs_TT3_To_TT1,
                    ClaimIDs_TT3_To_TT4,
                    ClaimIDs_ValidPostcodeChange,
                    ClaimIDs_ChangedTT,
                    ClaimIDs_UOAtSomePoint,
                    ClaimIDs_UOTT1AtSomePoint,
                    ClaimIDs_UOTT4AtSomePoint,
                    AlwaysUOTT1FromStartClaimIDs,
                    ClaimIDs_AlwaysUOTT1FromStartExceptWhenSuspended,
                    AlwaysUOTT1FromWhenStartedClaimIDs,
                    ClaimIDs_AlwaysUOTT4FromStart,
                    ClaimIDs_AlwaysUOTT4FromStartExceptWhenSuspended,
                    AlwaysUOTT4FromWhenStartedClaimIDs,
                    IntermitantUOClaimIDs,
                    ClaimIDs_UO_To_LeftSHBEAtSomePoint,
                    ClaimIDs_UOTT1_To_LeftSHBE,
                    ClaimIDs_UOTT4_To_LeftSHBE,
                    ClaimIDs_UOTT3OrTT6_To_LeftSHBE,
                    ClaimIDs_UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE,
                    //ClaimIDs_UOTT1_To_LeftSHBEReturnedAsTT1orTT4,
                    //ClaimIDs_UOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
                    //ClaimIDs_UOTT4_To_LeftSHBEAndHaveNotReturned,
                    //ClaimIDs_UOTT4_To_LeftSHBEReturnedAsTT1orTT4,
                    //ClaimIDs_UOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
                    //ClaimIDs_UO_To_LeftSHBETheVeryNextMonth,
                    ClaimIDs_UO_To_LeftSHBETheVeryNextMonth,
                    ClaimIDs_UO_To_LeftSHBEBetweenOneAndTwoMonths,
                    ClaimIDs_UO_To_LeftSHBEBetweenTwoAndThreeMonths,
                    ClaimIDs_UO_To_LeftSHBEAndNotReturned,
                    ClaimIDs_UOTT1_To_LeftSHBEAndNotReturned,
                    ClaimIDs_UOTT4_To_LeftSHBEAndNotReturned,
                    ClaimIDs_UOTT3OrTT6_To_LeftSHBEAndNotReturned,
                    ClaimIDs_UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEAndNotReturned,
                    ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT1,
                    ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT1,
                    ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
                    ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT4,
                    ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT4,
                    ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
                    ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT8,
                    ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT9,
                    ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsUOTT1,
                    ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT1,
                    ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6,
                    ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsUOTT4,
                    ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT4,
                    ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7,
                    ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT8,
                    ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT9,
                    ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1,
                    ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1,
                    ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6,
                    ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4,
                    ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4,
                    ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7,
                    ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8,
                    ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9,
                    ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                    ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                    ClaimIDs_UO_NotUO,
                    ClaimIDs_UO_NotUO_UO,
                    ClaimIDs_UO_NotUO_UO_NotUO,
                    ClaimIDs_UO_NotUO_UO_NotUO_UO,
                    ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO,
                    ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO,
                    ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO,
                    ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO,
                    ClaimIDs_UOTT1_To_NotUO_InSHBE_PostcodeChanged,
                    ClaimIDs_UOTT1_To_UOTT1_PostcodeChanged,
                    ClaimIDs_UOTT1_To_TT1_PostcodeChanged,
                    ClaimIDs_UOTT1_To_UOTT4_PostcodeChanged,
                    ClaimIDs_UOTT1_To_TT4_PostcodeChanged,
                    ClaimIDs_UOTT4_To_NotUO_InSHBE_PostcodeChanged,
                    ClaimIDs_UOTT4_To_UOTT1_PostcodeChanged,
                    ClaimIDs_UOTT4_To_TT1_PostcodeChanged,
                    ClaimIDs_UOTT4_To_UOTT4_PostcodeChanged,
                    ClaimIDs_UOTT4_To_TT4_PostcodeChanged,
                    ClaimIDs_UOTT1_To_TT3OrTT6,
                    ClaimIDs_UOTT1_To_TT3OrTT6AtSomePoint,
                    ClaimIDs_UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                    ClaimIDs_UOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint,
                    ClaimIDs_UOTT1_To_TT3OrTT6NotDoneNextChange,
                    ClaimIDs_UOTT4_To_TT3OrTT6,
                    ClaimIDs_UOTT4_To_TT3OrTT6AtSomePoint,
                    ClaimIDs_UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                    ClaimIDs_UOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint,
                    ClaimIDs_UOTT4_To_TT3OrTT6NotDoneNextChange,
                    ClaimIDs_TT3OrTT6_To_UOTT1,
                    ClaimIDs_TT3OrTT6_To_UOTT4,
                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged,
                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedThisMonth,
                    //ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months,
                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged1MonthPrevious,
                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious,
                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged3MonthsPrevious,
                    //ClaimIDs_UOTT1OrTT1_To_UOTT4,
                    ClaimIDs_UOTT1_To_UOTT4,
                    ClaimIDs_TT1_To_UOTT4,
                    ClaimIDs_TT1_To_UOTT4GettingDHP,
                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged,
                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedThisMonth,
                    //ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months,
                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged1MonthPrevious,
                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious,
                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged3MonthsPrevious,
                    //ClaimIDs_UOTT4OrTT4_To_UOTT1,
                    //ClaimIDs_UOTT4OrTT4_To_UOTT1InArrears,
                    //ClaimIDs_UOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
                    ClaimIDs_UOTT4_To_UOTT1,
                    ClaimIDs_UOTT4_To_UOTT1InArrears,
                    ClaimIDs_UOTT4_To_UOTT1GettingDHP,
                    ClaimIDs_UOTT4_To_UOTT1InArrearsAndGettingDHP,
                    ClaimIDs_TT4_To_UOTT1,
                    ClaimIDs_TT4_To_UOTT1InArrears,
                    ClaimIDs_TT4_To_UOTT1GettingDHP,
                    ClaimIDs_TT4_To_UOTT1InArrearsAndGettingDHP,
                    ClaimIDs_InArrearsAtSomePoint,
                    ClaimIDs_DHPAtSomePoint,
                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged,
                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedThisMonth,
                    //ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months,
                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged1MonthPrevious,
                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious,
                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged3MonthsPrevious,
                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged,
                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedThisMonth,
                    //ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months,
                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged1MonthPrevious,
                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious,
                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged3MonthsPrevious,
                    ClaimIDs_UOTT1_ToTT1_PostcodeChanged,
                    ClaimIDs_UOTT1_ToUOTT1_PostcodeChanged,
                    ClaimIDs_UOTT4_ToTT4_PostcodeChanged,
                    ClaimIDs_UOTT4_ToUOTT4_PostcodeChanged,
                    ClaimIDs_TT1_ToUOTT1_PostcodeChanged,
                    ClaimIDs_TT4_ToUOTT4_PostcodeChanged,
                    ClaimIDs_UOClaimsRecievingDHP,
                    ClaimIDs_UOTT1ClaimsInRentArrearsAtSomePoint,
                    ClaimIDs_UOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint);
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

        ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged1MonthPrevious = ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedThisMonth;
        ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged1MonthPrevious = ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedThisMonth;
        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedThisMonth = new TreeSet<DW_ID>();
        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedThisMonth = new TreeSet<DW_ID>();
        ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged1MonthPrevious = ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedThisMonth;
        ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged1MonthPrevious = ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedThisMonth;
        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedThisMonth = new TreeSet<DW_ID>();
        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedThisMonth = new TreeSet<DW_ID>();

        String yearMonth;
        yearMonth = year + "-" + month;
        totalCounts_cumulativeUniqueClaims.put(
                yearMonth,
                UOClaims.size());
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
            CouncilUOSet1 = CouncilUOSets.get(aYM3);
            RSLUOSet1 = RSLUOSets.get(aYM3);
            header += DW_Strings.sCommaSpace + aYM3;
            ClaimIDsIte = ClaimIDs.iterator();
            while (ClaimIDsIte.hasNext()) {
                ClaimID = ClaimIDsIte.next();
                DW_SHBE_Record = aRecords.get(ClaimID);
                processResult = process(
                        NINOToNINOIDLookup,
                        DOBToDOBIDLookup,
                        ClaimIDToCTBRefLookup,
                        UOClaims,
                        aggregateStatistics,
                        GeneralStatistics,
                        ClaimID,
                        year,
                        month,
                        aYM3,
                        DW_SHBE_Record,
                        bRecords,
                        cRecords,
                        tableValues,
                        CouncilUOSet1,
                        RSLUOSet1,
                        CouncilUniqueClaimantsEffected,
                        CouncilUniquePartnersEffected,
                        tCouncilMaxNumberOfDependentsInClaimWhenUO,
                        CouncilUniqueNonDependentsEffected,
                        RSLUniqueClaimantsEffected,
                        RSLUniquePartnersEffected,
                        RSLMaxNumberOfDependentsInClaimWhenUO,
                        RSLUniqueNonDependentsEffected,
                        UniqueDependentChildrenUnderAge10Effected,
                        PermanantlyLeftUOButRemainedInSHBEClaimIDs,
                        ClaimIDs_PermanantlyLeftUOButRemainedInSHBE_PostcodeChanged,
                        ClaimIDs_PermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased,
                        ClaimIDs_Travellers,
                        ClaimIDs_TTNot1Or4AndUnderOccupying,
                        ClaimIDs_TT1_To_TT3,
                        ClaimIDs_TT4_To_TT3,
                        ClaimIDs_TT3_To_TT1,
                        ClaimIDs_TT3_To_TT4,
                        ClaimIDs_ValidPostcodeChange,
                        ClaimIDs_ChangedTT,
                        ClaimIDs_UOAtSomePoint,
                        ClaimIDs_UOTT1AtSomePoint,
                        ClaimIDs_UOTT4AtSomePoint,
                        AlwaysUOTT1FromStartClaimIDs,
                        ClaimIDs_AlwaysUOTT1FromStartExceptWhenSuspended,
                        AlwaysUOTT1FromWhenStartedClaimIDs,
                        ClaimIDs_AlwaysUOTT4FromStart,
                        ClaimIDs_AlwaysUOTT4FromStartExceptWhenSuspended,
                        AlwaysUOTT4FromWhenStartedClaimIDs,
                        IntermitantUOClaimIDs,
                        ClaimIDs_UO_To_LeftSHBEAtSomePoint,
                        ClaimIDs_UOTT1_To_LeftSHBE,
                        ClaimIDs_UOTT4_To_LeftSHBE,
                        ClaimIDs_UOTT3OrTT6_To_LeftSHBE,
                        ClaimIDs_UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE,
                        //ClaimIDs_UOTT1_To_LeftSHBEReturnedAsTT1orTT4,
                        //ClaimIDs_UOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
                        //ClaimIDs_UOTT4_To_LeftSHBEAndHaveNotReturned,
                        //ClaimIDs_UOTT4_To_LeftSHBEReturnedAsTT1orTT4,
                        //ClaimIDs_UOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
                        //ClaimIDs_UO_To_LeftSHBETheVeryNextMonth,
                        ClaimIDs_UO_To_LeftSHBETheVeryNextMonth,
                        ClaimIDs_UO_To_LeftSHBEBetweenOneAndTwoMonths,
                        ClaimIDs_UO_To_LeftSHBEBetweenTwoAndThreeMonths,
                        ClaimIDs_UO_To_LeftSHBEAndNotReturned,
                        ClaimIDs_UOTT1_To_LeftSHBEAndNotReturned,
                        ClaimIDs_UOTT4_To_LeftSHBEAndNotReturned,
                        ClaimIDs_UOTT3OrTT6_To_LeftSHBEAndNotReturned,
                        ClaimIDs_UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEAndNotReturned,
                        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT1,
                        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT1,
                        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
                        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT4,
                        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT4,
                        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
                        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT8,
                        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT9,
                        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsUOTT1,
                        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT1,
                        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6,
                        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsUOTT4,
                        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT4,
                        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7,
                        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT8,
                        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT9,
                        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1,
                        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1,
                        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6,
                        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4,
                        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4,
                        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7,
                        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8,
                        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9,
                        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                        ClaimIDs_UO_NotUO,
                        ClaimIDs_UO_NotUO_UO,
                        ClaimIDs_UO_NotUO_UO_NotUO,
                        ClaimIDs_UO_NotUO_UO_NotUO_UO,
                        ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO,
                        ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO,
                        ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO,
                        ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO,
                        ClaimIDs_UOTT1_To_NotUO_InSHBE_PostcodeChanged,
                        ClaimIDs_UOTT1_To_UOTT1_PostcodeChanged,
                        ClaimIDs_UOTT1_To_TT1_PostcodeChanged,
                        ClaimIDs_UOTT1_To_UOTT4_PostcodeChanged,
                        ClaimIDs_UOTT1_To_TT4_PostcodeChanged,
                        ClaimIDs_UOTT4_To_NotUO_InSHBE_PostcodeChanged,
                        ClaimIDs_UOTT4_To_UOTT1_PostcodeChanged,
                        ClaimIDs_UOTT4_To_TT1_PostcodeChanged,
                        ClaimIDs_UOTT4_To_UOTT4_PostcodeChanged,
                        ClaimIDs_UOTT4_To_TT4_PostcodeChanged,
                        ClaimIDs_UOTT1_To_TT3OrTT6,
                        ClaimIDs_UOTT1_To_TT3OrTT6AtSomePoint,
                        ClaimIDs_UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                        ClaimIDs_UOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint,
                        ClaimIDs_UOTT1_To_TT3OrTT6NotDoneNextChange,
                        ClaimIDs_UOTT4_To_TT3OrTT6,
                        ClaimIDs_UOTT4_To_TT3OrTT6AtSomePoint,
                        ClaimIDs_UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                        ClaimIDs_UOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint,
                        ClaimIDs_UOTT4_To_TT3OrTT6NotDoneNextChange,
                        ClaimIDs_TT3OrTT6_To_UOTT1,
                        ClaimIDs_TT3OrTT6_To_UOTT4,
                        ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged,
                        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedThisMonth,
                        //ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
                        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
                        ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months,
                        ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged1MonthPrevious,
                        ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious,
                        ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged3MonthsPrevious,
                        //ClaimIDs_UOTT1OrTT1_To_UOTT4,
                        ClaimIDs_UOTT1_To_UOTT4,
                        ClaimIDs_TT1_To_UOTT4,
                        ClaimIDs_TT1_To_UOTT4GettingDHP,
                        ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged,
                        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedThisMonth,
                        //ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
                        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
                        ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months,
                        ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged1MonthPrevious,
                        ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious,
                        ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged3MonthsPrevious,
                        //ClaimIDs_UOTT4OrTT4_To_UOTT1,
                        //ClaimIDs_UOTT4OrTT4_To_UOTT1InArrears,
                        //ClaimIDs_UOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
                        ClaimIDs_UOTT4_To_UOTT1,
                        ClaimIDs_UOTT4_To_UOTT1InArrears,
                        ClaimIDs_UOTT4_To_UOTT1GettingDHP,
                        ClaimIDs_UOTT4_To_UOTT1InArrearsAndGettingDHP,
                        ClaimIDs_TT4_To_UOTT1,
                        ClaimIDs_TT4_To_UOTT1InArrears,
                        ClaimIDs_TT4_To_UOTT1GettingDHP,
                        ClaimIDs_TT4_To_UOTT1InArrearsAndGettingDHP,
                        ClaimIDs_InArrearsAtSomePoint,
                        ClaimIDs_DHPAtSomePoint,
                        ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged,
                        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedThisMonth,
                        //ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
                        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
                        ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months,
                        ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged1MonthPrevious,
                        ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious,
                        ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged3MonthsPrevious,
                        ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged,
                        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedThisMonth,
                        //ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
                        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
                        ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months,
                        ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged1MonthPrevious,
                        ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious,
                        ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged3MonthsPrevious,
                        ClaimIDs_UOTT1_ToTT1_PostcodeChanged,
                        ClaimIDs_UOTT1_ToUOTT1_PostcodeChanged,
                        ClaimIDs_UOTT4_ToTT4_PostcodeChanged,
                        ClaimIDs_UOTT4_ToUOTT4_PostcodeChanged,
                        ClaimIDs_TT1_ToUOTT1_PostcodeChanged,
                        ClaimIDs_TT4_ToUOTT4_PostcodeChanged,
                        ClaimIDs_UOClaimsRecievingDHP,
                        ClaimIDs_UOTT1ClaimsInRentArrearsAtSomePoint,
                        ClaimIDs_UOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint);
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
            ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged3MonthsPrevious = ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious;
            ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged3MonthsPrevious = ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious;
            ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious = ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged1MonthPrevious;
            ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious = ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged1MonthPrevious;
            ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged1MonthPrevious = ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedThisMonth;
            ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged1MonthPrevious = ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedThisMonth;
            ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedThisMonth = new TreeSet<DW_ID>();
            ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedThisMonth = new TreeSet<DW_ID>();

            ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged3MonthsPrevious = ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious;
            ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged3MonthsPrevious = ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious;
            ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious = ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged1MonthPrevious;
            ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious = ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged1MonthPrevious;
            ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged1MonthPrevious = ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedThisMonth;
            ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged1MonthPrevious = ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedThisMonth;
            ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedThisMonth = new TreeSet<DW_ID>();
            ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedThisMonth = new TreeSet<DW_ID>();

            yearMonth = year + "-" + month;
            totalCounts_cumulativeUniqueClaims.put(
                    yearMonth,
                    UOClaims.size());
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

        ClaimIDs_InArrearsAtSomePoint_And_DHPAtSomePoint.addAll(ClaimIDs_InArrearsAtSomePoint);
        ClaimIDs_InArrearsAtSomePoint_And_DHPAtSomePoint.retainAll(ClaimIDs_DHPAtSomePoint);

//        ClaimIDs_UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.removeAll(ClaimIDs_UOTT1_To_LeftSHBEAndNotReturned);
//        ClaimIDs_UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.removeAll(ClaimIDs_UOTT4_To_LeftSHBEAndNotReturned);
        ClaimIDs_UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.retainAll(ClaimIDs_UOTT1_To_TT3OrTT6AtSomePoint);
        ClaimIDs_UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.retainAll(ClaimIDs_UOTT4_To_TT3OrTT6AtSomePoint);

        header += DW_Strings.sCommaSpace + "HBDPTotal";

//        TreeSet<String> ClaimIDs_ValidPostcodeChange; // Calculate by removing all from ClaimIDs_NoValidPostcodeChange.
//        ClaimIDs_ValidPostcodeChange = new TreeSet<String>();
//        ClaimIDs_ValidPostcodeChange.addAll(tCTBRefs);
//        ClaimIDs_ValidPostcodeChange.removeAll(ClaimIDs_NoValidPostcodeChange);
//        groups.put("ValidPostcodeChange", ClaimIDs_ValidPostcodeChange);
        TreeSet<DW_ID> ClaimIDs_NoValidPostcodeChange; // Calculate by removing all from ClaimIDs_NoValidPostcodeChange.
        ClaimIDs_NoValidPostcodeChange = new TreeSet<DW_ID>();
        ClaimIDs_NoValidPostcodeChange.addAll(ClaimIDs);
        ClaimIDs_NoValidPostcodeChange.removeAll(ClaimIDs_ValidPostcodeChange);
        groups.put(sNoValidPostcodeChange, ClaimIDs_ValidPostcodeChange);

        TreeSet<DW_ID> ClaimIDs_NotChangedTT; // Calculate by removing all from ClaimIDs_ChangedTT.
        ClaimIDs_NotChangedTT = new TreeSet<DW_ID>();
        ClaimIDs_NotChangedTT.addAll(ClaimIDs);
        ClaimIDs_NotChangedTT.removeAll(ClaimIDs_ChangedTT);
        groups.put("NotChangedTT", ClaimIDs_NotChangedTT);

        TreeSet<DW_ID> ClaimIDs_AlwaysUOFromStartNoValidPostcodeChangeNotChangedTT; // Calculate by intersect of sets.
        ClaimIDs_AlwaysUOFromStartNoValidPostcodeChangeNotChangedTT = new TreeSet<DW_ID>();
        ClaimIDs_AlwaysUOFromStartNoValidPostcodeChangeNotChangedTT.addAll(AlwaysUOTT1FromStartClaimIDs);
        ClaimIDs_AlwaysUOFromStartNoValidPostcodeChangeNotChangedTT.addAll(ClaimIDs_AlwaysUOTT4FromStart);
        ClaimIDs_AlwaysUOFromStartNoValidPostcodeChangeNotChangedTT.retainAll(ClaimIDs_NoValidPostcodeChange);
        ClaimIDs_AlwaysUOFromStartNoValidPostcodeChangeNotChangedTT.retainAll(ClaimIDs_NotChangedTT);
        groups.put(sAlwaysUOFromStart__NoValidPostcodeChange_NotChangedTT, ClaimIDs_AlwaysUOFromStartNoValidPostcodeChangeNotChangedTT);

        TreeSet<DW_ID> ClaimIDs_AlwaysUOFromStartChangedTT; // Calculate by intersect of sets.
        ClaimIDs_AlwaysUOFromStartChangedTT = new TreeSet<DW_ID>();
        ClaimIDs_AlwaysUOFromStartChangedTT.addAll(ClaimIDs_ChangedTT);
        ClaimIDs_AlwaysUOFromStartChangedTT.retainAll(AlwaysUOTT1FromStartClaimIDs);
        ClaimIDs_AlwaysUOFromStartChangedTT.retainAll(ClaimIDs_AlwaysUOTT4FromStart);
        groups.put(sAlwaysUOFromStart__ChangedTT, ClaimIDs_AlwaysUOFromStartChangedTT);

        TreeSet<DW_ID> ClaimIDs_AlwaysUOFromStartValidPostcodeChangeNotChangedTT; // Calculate by intersect of sets.
        ClaimIDs_AlwaysUOFromStartValidPostcodeChangeNotChangedTT = new TreeSet<DW_ID>();
        ClaimIDs_AlwaysUOFromStartValidPostcodeChangeNotChangedTT.addAll(AlwaysUOTT1FromStartClaimIDs);
        ClaimIDs_AlwaysUOFromStartValidPostcodeChangeNotChangedTT.addAll(ClaimIDs_AlwaysUOTT4FromStart);
        ClaimIDs_AlwaysUOFromStartValidPostcodeChangeNotChangedTT.removeAll(ClaimIDs_AlwaysUOFromStartChangedTT);
        ClaimIDs_AlwaysUOFromStartValidPostcodeChangeNotChangedTT.retainAll(ClaimIDs_ValidPostcodeChange);
        groups.put(sAlwaysUOFromStart__ValidPostcodeChange_NotChangedTT, ClaimIDs_AlwaysUOFromStartValidPostcodeChangeNotChangedTT);

        String aS;
        String key;
        Iterator<DW_ID> ite;
        ite = ClaimIDs.iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            CTBRef = ClaimIDToCTBRefLookup.get(ClaimID);
            key = CTBRef + DW_Strings.sUnderscore + sUnderOccupancy;
            aS = tableValues.get(key);
            if (aS.endsWith(DW_Strings.sCommaSpace)) {
                IntermitantUOClaimIDs.add(ClaimID);
            }
        }

        AlwaysUOTT1FromWhenStartedClaimIDs.removeAll(AlwaysUOTT1FromStartClaimIDs);
        AlwaysUOTT1FromWhenStartedClaimIDs.removeAll(IntermitantUOClaimIDs);
        AlwaysUOTT4FromWhenStartedClaimIDs.removeAll(ClaimIDs_AlwaysUOTT4FromStart);
        AlwaysUOTT4FromWhenStartedClaimIDs.removeAll(IntermitantUOClaimIDs);

        TreeSet<DW_ID> ClaimIDs_AlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT; // Calculate by intersect of sets.
        ClaimIDs_AlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT = new TreeSet<DW_ID>();
        ClaimIDs_AlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT.addAll(AlwaysUOTT1FromWhenStartedClaimIDs);
        ClaimIDs_AlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT.addAll(AlwaysUOTT4FromWhenStartedClaimIDs);
        ClaimIDs_AlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT.removeAll(ClaimIDs_ChangedTT);
        ClaimIDs_AlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT.retainAll(ClaimIDs_NoValidPostcodeChange);
        groups.put(sAlwaysUOFromWhenStarted__NoValidPostcodeChange_NotChangedTT, ClaimIDs_AlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT);

        TreeSet<DW_ID> ClaimIDs_AlwaysUOFromWhenStartedChangedTT; // Calculate by intersect of sets.
        ClaimIDs_AlwaysUOFromWhenStartedChangedTT = new TreeSet<DW_ID>();
        ClaimIDs_AlwaysUOFromWhenStartedChangedTT.addAll(AlwaysUOTT1FromWhenStartedClaimIDs);
        ClaimIDs_AlwaysUOFromWhenStartedChangedTT.addAll(AlwaysUOTT4FromWhenStartedClaimIDs);
        ClaimIDs_AlwaysUOFromWhenStartedChangedTT.retainAll(ClaimIDs_ChangedTT);
        groups.put(sAlwaysUOFromWhenStarted__ChangedTT, ClaimIDs_AlwaysUOFromWhenStartedChangedTT);

        TreeSet<DW_ID> ClaimIDs_AlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT; // Calculate by intersect of sets.
        ClaimIDs_AlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT = new TreeSet<DW_ID>();
        ClaimIDs_AlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT.addAll(AlwaysUOTT1FromWhenStartedClaimIDs);
        ClaimIDs_AlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT.addAll(AlwaysUOTT4FromWhenStartedClaimIDs);
        ClaimIDs_AlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT.removeAll(ClaimIDs_NoValidPostcodeChange);
        ClaimIDs_AlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT.removeAll(ClaimIDs_ChangedTT);
        groups.put(sAlwaysUOFromWhenStarted__ValidPostcodeChange_NotChangedTT, ClaimIDs_AlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT);

        TreeSet<DW_ID> ClaimIDs_IntermitantUONoValidPostcodeChangeNotChangedTT;
        ClaimIDs_IntermitantUONoValidPostcodeChangeNotChangedTT = new TreeSet<DW_ID>();
        ClaimIDs_IntermitantUONoValidPostcodeChangeNotChangedTT.addAll(IntermitantUOClaimIDs);
        ClaimIDs_IntermitantUONoValidPostcodeChangeNotChangedTT.retainAll(ClaimIDs_NoValidPostcodeChange);
        ClaimIDs_IntermitantUONoValidPostcodeChangeNotChangedTT.retainAll(ClaimIDs_NotChangedTT);
        groups.put(sIntermitantUO__NoValidPostcodeChange_NotChangedTT, ClaimIDs_IntermitantUONoValidPostcodeChangeNotChangedTT);

        TreeSet<DW_ID> ClaimIDs_IntermitantUOChangedTT;
        ClaimIDs_IntermitantUOChangedTT = new TreeSet<DW_ID>();
        ClaimIDs_IntermitantUOChangedTT.addAll(IntermitantUOClaimIDs);
        ClaimIDs_IntermitantUOChangedTT.retainAll(ClaimIDs_ChangedTT);
        groups.put(sIntermitantUO__ChangedTT, ClaimIDs_IntermitantUOChangedTT);

        TreeSet<DW_ID> ClaimIDs_IntermitantUOValidPostcodeChangeNotChangedTT;
        ClaimIDs_IntermitantUOValidPostcodeChangeNotChangedTT = new TreeSet<DW_ID>();
        ClaimIDs_IntermitantUOValidPostcodeChangeNotChangedTT.addAll(IntermitantUOClaimIDs);
        ClaimIDs_IntermitantUOValidPostcodeChangeNotChangedTT.removeAll(ClaimIDs_NoValidPostcodeChange);
        ClaimIDs_IntermitantUOValidPostcodeChangeNotChangedTT.removeAll(ClaimIDs_ChangedTT);
        groups.put(sIntermitantUO__ValidPostcodeChange_NotChangedTT, ClaimIDs_IntermitantUOValidPostcodeChangeNotChangedTT);

        checkSetsAndAddToGeneralStatistics(
                GeneralStatistics,
                ClaimIDs,
                CouncilClaimIDs,
                RSLClaimIDs,
                CouncilUniqueClaimantsEffected,
                CouncilUniquePartnersEffected,
                tCouncilMaxNumberOfDependentsInClaimWhenUO,
                CouncilUniqueNonDependentsEffected,
                RSLUniqueClaimantsEffected,
                RSLUniquePartnersEffected,
                RSLMaxNumberOfDependentsInClaimWhenUO,
                RSLUniqueNonDependentsEffected,
                UniqueDependentChildrenUnderAge10Effected,
                groups);

        long totalHouseholdSize;
        double averageHouseholdSizeOfThoseUOAlwaysFromStart;
        double d;
        Iterator<String> iteS;
        // TT1
        totalHouseholdSize = 0;
        d = 0.0d;
        ite = AlwaysUOTT1FromStartClaimIDs.iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            DW_SHBE_Record rec = aRecords.get(ClaimID);
            if (rec != null) {
                totalHouseholdSize += DW_SHBE_Handler.getHouseholdSize(rec);
                d += 1.0d;
            }
        }
        if (d > 0) {
            //averageHouseholdSizeOfThoseUOAlwaysFromStart = (double) totalHouseholdSize / (double) ClaimIDs_AlwaysUOTT1FromStart.size();
            averageHouseholdSizeOfThoseUOAlwaysFromStart = (double) totalHouseholdSize / d;
        } else {
            averageHouseholdSizeOfThoseUOAlwaysFromStart = 0.0d;
        }
        GeneralStatistics.put(sAverageHouseholdSizeOfThoseUOTT1AlwaysFromStart,
                BigDecimal.valueOf(averageHouseholdSizeOfThoseUOAlwaysFromStart));
        // TT4
        totalHouseholdSize = 0;
        d = 0.0d;
        ite = ClaimIDs_AlwaysUOTT4FromStart.iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            DW_SHBE_Record rec = aRecords.get(ClaimID);
            if (rec != null) {
                totalHouseholdSize += DW_SHBE_Handler.getHouseholdSize(rec);
                d += 1.0d;
            }
        }
        if (d > 0) {
            //averageHouseholdSizeOfThoseUOAlwaysFromStart = (double) totalHouseholdSize / (double) ClaimIDs_AlwaysUOTT4FromStart.size();
            averageHouseholdSizeOfThoseUOAlwaysFromStart = (double) totalHouseholdSize / d;
        } else {
            averageHouseholdSizeOfThoseUOAlwaysFromStart = 0.0d;
        }
        GeneralStatistics.put(sAverageHouseholdSizeOfThoseUOTT4AlwaysFromStart,
                BigDecimal.valueOf(averageHouseholdSizeOfThoseUOAlwaysFromStart));

        GeneralStatistics.put(sTotalCount_AlwaysUOTT1FromStart,
                BigDecimal.valueOf(AlwaysUOTT1FromStartClaimIDs.size()));
        GeneralStatistics.put(sTotalCount_AlwaysUOTT1FromStartExceptWhenSuspended,
                BigDecimal.valueOf(ClaimIDs_AlwaysUOTT1FromStartExceptWhenSuspended.size()));
        GeneralStatistics.put(sTotalCount_AlwaysUOTT1FromWhenStarted,
                BigDecimal.valueOf(AlwaysUOTT1FromWhenStartedClaimIDs.size()));
        GeneralStatistics.put(sTotalCount_AlwaysUOTT4FromStart,
                BigDecimal.valueOf(ClaimIDs_AlwaysUOTT4FromStart.size()));
        GeneralStatistics.put(sTotalCount_AlwaysUOTT4FromStartExceptWhenSuspended,
                BigDecimal.valueOf(ClaimIDs_AlwaysUOTT4FromStartExceptWhenSuspended.size()));
        GeneralStatistics.put(sTotalCount_AlwaysUOTT4FromWhenStarted,
                BigDecimal.valueOf(AlwaysUOTT4FromWhenStartedClaimIDs.size()));

// Use sets?
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOInApril2013, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOAfterApril2013, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOAfterChangePostcodeAndOrTT, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ChangePostcodeAndOrTTToAvoidUO, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_StayPutAndAvoidedUO, BigDecimal.ZERO);
//        generalStatistics.put(sCostOfUOToTaxPayer, BigDecimal.ZERO);
        result[0] = header;
        result[1] = tableValues;
        result[2] = ClaimIDs;
        result[3] = groups;
        result[4] = PreUnderOccupancyValues;
        result[5] = aggregateStatistics;
        result[6] = GeneralStatistics;
        result[7] = timeStatistics;
        return result;
    }

    protected void checkSetsAndAddToGeneralStatistics(
            TreeMap<String, BigDecimal> generalStatistics,
            HashSet<DW_ID> ClaimIDs,
            HashSet<DW_ID> CouncilClaimIDs,
            HashSet<DW_ID> RSLClaimIDs,
            HashSet<DW_PersonID> CouncilUniqueClaimantsEffected,
            HashSet<DW_PersonID> CouncilUniquePartnersEffected,
            HashMap<DW_ID, Integer> tCouncilMaxNumberOfDependentsInClaimWhenUO,
            HashSet<DW_PersonID> CouncilUniqueNonDependentsEffected,
            HashSet<DW_PersonID> RSLUniqueClaimantsEffected,
            HashSet<DW_PersonID> RSLUniquePartnersEffected,
            HashMap<DW_ID, Integer> tRSLMaxNumberOfDependentsInClaimWhenUO,
            HashSet<DW_PersonID> RSLUniqueNonDependentsEffected,
            HashSet<DW_PersonID> UniqueDependentChildrenUnderAge10Effected,
            HashMap<String, TreeSet<DW_ID>> groups) {
        HashSet<DW_PersonID> CouncilUniqueIndividualsEffected;
        CouncilUniqueIndividualsEffected = new HashSet<DW_PersonID>();
        CouncilUniqueIndividualsEffected.addAll(CouncilUniqueClaimantsEffected);
        CouncilUniqueIndividualsEffected.addAll(CouncilUniquePartnersEffected);
        CouncilUniqueIndividualsEffected.addAll(CouncilUniqueNonDependentsEffected);

        HashSet<DW_PersonID> RSLUniqueIndividualsEffected;
        RSLUniqueIndividualsEffected = new HashSet<DW_PersonID>();
        RSLUniqueIndividualsEffected.addAll(RSLUniqueClaimantsEffected);
        RSLUniqueIndividualsEffected.addAll(RSLUniquePartnersEffected);
        RSLUniqueIndividualsEffected.addAll(RSLUniqueNonDependentsEffected);

        HashSet<DW_PersonID> UniqueIndividualsEffected;
        UniqueIndividualsEffected = new HashSet<DW_PersonID>();
        UniqueIndividualsEffected.addAll(CouncilUniqueIndividualsEffected);
        UniqueIndividualsEffected.addAll(RSLUniqueIndividualsEffected);

        DW_ID ClaimID;
        int m;
        Iterator<DW_ID> ite;

        long totalCouncilDependentsEffected;
        totalCouncilDependentsEffected = 0;
        ite = tCouncilMaxNumberOfDependentsInClaimWhenUO.keySet().iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            m = tCouncilMaxNumberOfDependentsInClaimWhenUO.get(ClaimID);
            totalCouncilDependentsEffected += m;
        }

        long totalRSLDependentsEffected;
        totalRSLDependentsEffected = 0;
        ite = tRSLMaxNumberOfDependentsInClaimWhenUO.keySet().iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            m = tRSLMaxNumberOfDependentsInClaimWhenUO.get(ClaimID);
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
                BigDecimal.valueOf(ClaimIDs.size()));
        generalStatistics.put(
                sTotalCount_CouncilClaimsEffectedByUnderOccupancy,
                BigDecimal.valueOf(CouncilClaimIDs.size()));
        generalStatistics.put(
                sTotalCount_RSLClaimsEffectedByUnderOccupancy,
                BigDecimal.valueOf(RSLClaimIDs.size()));
        generalStatistics.put(
                sTotalCount_UniqueIndividualsEffectedByUnderOccupancy,
                BigDecimal.valueOf((UniqueIndividualsEffected.size()
                        + totalCouncilDependentsEffected + totalRSLDependentsEffected)));
        // Council
        generalStatistics.put(
                sTotalCount_CouncilUniqueIndividualsEffectedByUnderOccupancy,
                BigDecimal.valueOf(CouncilUniqueIndividualsEffected.size()
                        + totalCouncilDependentsEffected));
        generalStatistics.put(
                sTotalCount_CouncilUniqueClaimantsEffectedByUnderOccupancy,
                BigDecimal.valueOf(CouncilUniqueClaimantsEffected.size()));
        generalStatistics.put(
                sTotalCount_CouncilUniquePartnersEffectedByUnderOccupancy,
                BigDecimal.valueOf(CouncilUniquePartnersEffected.size()));
        generalStatistics.put(
                sTotalCount_CouncilDependentsEffectedByUnderOccupancy,
                BigDecimal.valueOf(totalCouncilDependentsEffected));
        generalStatistics.put(
                sTotalCount_CouncilUniqueNonDependentsEffectedByUnderOccupancy,
                BigDecimal.valueOf(CouncilUniqueNonDependentsEffected.size()));
        // RSL
        generalStatistics.put(
                sTotalCount_RSLUniqueIndividualsEffectedByUnderOccupancy,
                BigDecimal.valueOf(RSLUniqueIndividualsEffected.size()
                        + totalRSLDependentsEffected));
        generalStatistics.put(
                sTotalCount_RSLUniqueClaimantsEffectedByUnderOccupancy,
                BigDecimal.valueOf(RSLUniqueClaimantsEffected.size()));
        generalStatistics.put(
                sTotalCount_RSLUniquePartnersEffectedByUnderOccupancy,
                BigDecimal.valueOf(RSLUniquePartnersEffected.size()));
        generalStatistics.put(
                sTotalCount_RSLDependentsEffectedByUnderOccupancy,
                BigDecimal.valueOf(totalRSLDependentsEffected));
        generalStatistics.put(
                sTotalCount_RSLUniqueNonDependentsEffectedByUnderOccupancy,
                BigDecimal.valueOf(RSLUniqueNonDependentsEffected.size()));
        generalStatistics.put(
                sTotalCount_UniqueChildrenAgeLessThan10EffectedByUnderOccupancy,
                BigDecimal.valueOf(UniqueDependentChildrenUnderAge10Effected.size()));

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
            HashMap<String, DW_ID> NINOToNINOIDLookup,
            HashMap<String, DW_ID> DOBToDOBIDLookup,
            HashMap<DW_ID, String> ClaimIDToCTBRefLookup,
            TreeSet<DW_ID> UOClaims,
            TreeMap<String, BigDecimal> AggregateStatistics,
            TreeMap<String, BigDecimal> GeneralStatistics,
            DW_ID ClaimID,
            //String CTBRef,
            //String aCTBRef,
            String year,
            String month,
            String aYM3,
            DW_SHBE_Record DW_SHBE_Record,
            TreeMap<DW_ID, DW_SHBE_Record> bRecords,
            TreeMap<DW_ID, DW_SHBE_Record> cRecords,
            TreeMap<String, String> tableValues,
            DW_UO_Set CouncilUnderOccupiedSet1,
            DW_UO_Set RSLUnderOccupiedSet1,
            HashSet<DW_PersonID> CouncilUniqueClaimantsEffected,
            HashSet<DW_PersonID> CouncilUniquePartnersEffected,
            HashMap<DW_ID, Integer> tCouncilMaxNumberOfDependentsInClaimWhenUO,
            HashSet<DW_PersonID> CouncilUniqueNonDependentsEffected,
            HashSet<DW_PersonID> RSLUniqueClaimantsEffected,
            HashSet<DW_PersonID> RSLUniquePartnersEffected,
            HashMap<DW_ID, Integer> tRSLMaxNumberOfDependentsInClaimWhenUO,
            HashSet<DW_PersonID> RSLUniqueNonDependentsEffected,
            HashSet<DW_PersonID> UniqueDependentChildrenUnderAge10Effected,
            TreeSet<DW_ID> ClaimIDs_PermanantlyLeftUOButRemainedInSHBE,
            TreeSet<DW_ID> ClaimIDs_PermanantlyLeftUOButRemainedInSHBE_PostcodeChanged,
            TreeSet<DW_ID> ClaimIDs_PermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased,
            TreeSet<DW_ID> ClaimIDs_Traveller,
            TreeSet<DW_ID> ClaimIDs_TTNot1Or4AndUnderOccupying,
            TreeSet<DW_ID> ClaimIDs_TT1_To_TT3OrTT6,
            TreeSet<DW_ID> ClaimIDs_TT4_To_TT3OrTT6,
            TreeSet<DW_ID> ClaimIDs_TT3OrTT6_To_TT1,
            TreeSet<DW_ID> ClaimIDs_TT3OrTT6_To_TT4,
            TreeSet<DW_ID> ClaimIDs_ValidPostcodeChange,
            TreeSet<DW_ID> ClaimIDs_ChangedTT,
            TreeSet<DW_ID> ClaimIDs_UOAtSomePoint,
            TreeSet<DW_ID> ClaimIDs_UOTT1AtSomePoint,
            TreeSet<DW_ID> ClaimIDs_UOTT4AtSomePoint,
            TreeSet<DW_ID> ClaimIDs_AlwaysUOTT1FromStart,
            TreeSet<DW_ID> ClaimIDs_AlwaysUOTT1FromStartExceptWhenSuspended,
            TreeSet<DW_ID> ClaimIDs_AlwaysUOTT1FromWhenStarted,
            TreeSet<DW_ID> ClaimIDs_AlwaysUOTT4FromStart,
            TreeSet<DW_ID> ClaimIDs_AlwaysUOTT4FromStartExceptWhenSuspended,
            TreeSet<DW_ID> ClaimIDs_AlwaysUOTT4FromWhenStarted,
            TreeSet<DW_ID> ClaimIDs_IntermitantUO,
            TreeSet<DW_ID> ClaimIDs_UO_To_LeftSHBEAtSomePoint,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE,
            TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE,
            TreeSet<DW_ID> ClaimIDs_UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE,
            TreeSet<DW_ID> ClaimIDs_UO_To_LeftSHBETheVeryNextMonth,
            TreeSet<DW_ID> ClaimIDs_UO_To_LeftSHBEBetweenOneAndTwoMonths,
            TreeSet<DW_ID> ClaimIDs_UO_To_LeftSHBEBetweenTwoAndThreeMonths,
            TreeSet<DW_ID> ClaimIDs_UO_To_LeftSHBE_NotReturned,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_NotReturned,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_NotReturned,
            TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE_NotReturned,
            TreeSet<DW_ID> ClaimIDs_UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT1,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT1,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT4,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT4,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT8,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT9,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsUOTT1,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT1,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsUOTT4,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT4,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT8,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT9,
            TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1,
            TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1,
            TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6,
            TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4,
            TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4,
            TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7,
            TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8,
            TreeSet<DW_ID> ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
            //TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBEReturnedAsTT1orTT4,
            //TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
            //TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBEReturnedAsTT1orTT4,
            //TreeSet<DW_ID> ClaimIDs_UOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
            TreeSet<DW_ID> ClaimIDs_UO_NotUO,
            TreeSet<DW_ID> ClaimIDs_UO_NotUO_UO,
            TreeSet<DW_ID> ClaimIDs_UO_NotUO_UO_NotUO,
            TreeSet<DW_ID> ClaimIDs_UO_NotUO_UO_NotUO_UO,
            TreeSet<DW_ID> ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO,
            TreeSet<DW_ID> ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO,
            TreeSet<DW_ID> ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO,
            TreeSet<DW_ID> ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_NotUO_InSHBE_PostcodeChanged,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_UOTT1_PostcodeChanged,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeChanged,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_UOTT4_PostcodeChanged,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT4_PostcodeChanged,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_NotUO_InSHBE_PostcodeChanged,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_UOTT1_PostcodeChanged,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT1_PostcodeChanged,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_UOTT4_PostcodeChanged,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeChanged,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT3OrTT6,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT3OrTT6AtSomePoint,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT3OrTT6NotDoneNextChange,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT3OrTT6,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT3OrTT6AtSomePoint,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT3OrTT6NotDoneNextChange,
            TreeSet<DW_ID> ClaimIDs_TT3OrTT6_To_UOTT1,
            TreeSet<DW_ID> ClaimIDs_TT3OrTT6_To_UOTT4,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedThisMonth,
            //TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged1MonthPrevious,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged3MonthsPrevious,
            //TreeSet<DW_ID> ClaimIDs_UOTT1OrTT1_To_UOTT4,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_UOTT4,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT4,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT4GettingDHP,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedThisMonth,
            //TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged1MonthPrevious,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged3MonthsPrevious,
            //TreeSet<DW_ID> ClaimIDs_UOTT4OrTT4_To_UOTT1,
            //TreeSet<DW_ID> ClaimIDs_UOTT4OrTT4_To_UOTT1InArrears,
            //TreeSet<DW_ID> ClaimIDs_UOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_UOTT1,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_UOTT1InArrears,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_UOTT1GettingDHP,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_UOTT1InArrearsAndGettingDHP,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT1,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT1InArrears,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT1GettingDHP,
            TreeSet<DW_ID> ClaimIDs_TT4_To_UOTT1InArrearsAndGettingDHP,
            TreeSet<DW_ID> ClaimIDs_InArrearsAtSomePoint,
            TreeSet<DW_ID> ClaimIDs_DHPAtSomePoint,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedThisMonth,
            //TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged1MonthPrevious,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged3MonthsPrevious,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedThisMonth,
            //TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged1MonthPrevious,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious,
            TreeSet<DW_ID> ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged3MonthsPrevious,
            TreeSet<DW_ID> ClaimIDs_UOTT1_ToTT1_PostcodeChanged,
            TreeSet<DW_ID> ClaimIDs_UOTT1_ToUOTT1_PostcodeChanged,
            TreeSet<DW_ID> ClaimIDs_UOTT4_ToTT4_PostcodeChanged,
            TreeSet<DW_ID> ClaimIDs_UOTT4_ToUOTT4_PostcodeChanged,
            TreeSet<DW_ID> ClaimIDs_TT1_ToUOTT1_PostcodeChanged,
            TreeSet<DW_ID> ClaimIDs_TT4_ToUOTT4_PostcodeChanged,
            TreeSet<DW_ID> ClaimIDs_UOClaimsRecievingDHP,
            TreeSet<DW_ID> ClaimIDs_UOTT1ClaimsInRentArrearsAtSomePoint,
            TreeSet<DW_ID> ClaimIDs_UOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint
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
        DW_Postcode_Handler DW_Postcode_Handler;
        DW_Postcode_Handler = env.getDW_Postcode_Handler();
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
        int aHBDP;
        Double aArrears;
        DW_SHBE_D_Record bDW_SHBE_D_Record;
        int bTT;
        String bPC;
        int bStatus;
        int bWHBE;
        int bWERA;
        int bPSI;
        int bSHBC;
        int bRTHBCC;
        int bCEG;
        long bHS;
        long bND;
        long bCD;
        String bPDD;
        String bCDoB;
        String bPDoB;
        String bCA;
        String bPA;
        DW_SHBE_Record bDW_SHBE_Record;
        if (bRecords == null) {
            bDW_SHBE_Record = null;
        } else {
            bDW_SHBE_Record = bRecords.get(ClaimID);
        }
        DW_SHBE_D_Record cDW_SHBE_D_Record;
        int cTT;
        String cPC;
        int cStatus;
        DW_SHBE_Record cDW_SHBE_Record;
        if (cRecords == null) {
            cDW_SHBE_Record = null;
        } else {
            cDW_SHBE_Record = cRecords.get(ClaimID);
        }
        // Init
        if (DW_SHBE_Record == null) {
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
            aDW_SHBE_D_Record = DW_SHBE_Record.getDRecord();
            aTT = aDW_SHBE_D_Record.getTenancyType();
            isPairedRecord = DW_SHBE_Record.isPairedRecord();
            if (isPairedRecord) {
                ClaimIDs_Traveller.add(ClaimID);
            }
//            if (aTT == 3) {
//                ClaimIDs_TT3.add(aClaimID);
//            }
            aPC = aDW_SHBE_D_Record.getClaimantsPostcode();
            // Add postcode to validPostcodes if it is valid
            if (aPC.isEmpty()) {
                aPC = defaultPostcode;
            } else if (!validPostcodes.contains(aPC)) {
                String NearestYM3ForONSPDLookup;
                NearestYM3ForONSPDLookup = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(aYM3);
                boolean ValidPostcode;
                ValidPostcode = DW_Postcode_Handler.isValidPostcode(NearestYM3ForONSPDLookup, aPC);
                if (ValidPostcode) {
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
        String CTBRef;
        CTBRef = ClaimIDToCTBRefLookup.get(ClaimID);
        key = CTBRef + DW_Strings.sUnderscore + sUnderOccupancy;
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
        isUO = (CouncilUnderOccupiedSet1.getMap().keySet().contains(ClaimID)
                || RSLUnderOccupiedSet1.getMap().keySet().contains(ClaimID));
        if (isUO) {
            ClaimIDs_UOAtSomePoint.add(ClaimID);
            if (aTT == 1) {
                ClaimIDs_UOTT1AtSomePoint.add(ClaimID);
            } else if (aTT == 4) {
                ClaimIDs_UOTT4AtSomePoint.add(ClaimID);
            }
        }

        if (aHBDP > 0) {
            ClaimIDs_UOClaimsRecievingDHP.add(ClaimID);
        }

        // TenancyType
        key = CTBRef + DW_Strings.sUnderscore + sTT;
        aS = tableValues.get(key);
        if (aTT != bTT) {
            if (aTT == DW_SHBE_TenancyType_Handler.iMinus999
                    || bTT == DW_SHBE_TenancyType_Handler.iMinus999) {
                if (bTT == DW_SHBE_TenancyType_Handler.iMinus999) {
                    // Check if there is another TT in aS
                    boolean isAnotherTT;
                    isAnotherTT = isAnotherTT(aTT, aS);
                    if (isAnotherTT) {
                        ClaimIDs_ChangedTT.add(ClaimID);
                    }
                    if (ClaimIDs_UOTT1_To_LeftSHBE.contains(ClaimID)) {
                        if (aTT != 3 || aTT != 6) {
                            if (ClaimIDs_UOTT1_To_TT3OrTT6NotDoneNextChange.contains(ClaimID)) {
                                ClaimIDs_UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(ClaimID);
                            }
                        }
                    }
                    if (ClaimIDs_UOTT4_To_LeftSHBE.contains(ClaimID)) {
                        if (aTT != 3 || aTT != 6) {
                            if (ClaimIDs_UOTT4_To_TT3OrTT6NotDoneNextChange.contains(ClaimID)) {
                                ClaimIDs_UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(ClaimID);
                            }
                        }
                    }
                    doX(ClaimID, aHBDP, aTT, bTT, isUO, wasUO, wasUOBefore,
                            bStatus,
                            ClaimIDs_UOTT1_To_LeftSHBE_NotReturned,
                            ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT1,
                            ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT1,
                            ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
                            ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT4,
                            ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT4,
                            ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
                            ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT8,
                            ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT9,
                            ClaimIDs_UOTT1_To_UOTT4,
                            ClaimIDs_TT1_To_UOTT4,
                            ClaimIDs_TT1_To_UOTT4GettingDHP);
                }
                if (aTT == DW_SHBE_TenancyType_Handler.iMinus999) {
                    if (ClaimIDs_UOAtSomePoint.contains(ClaimID)) {
                        ClaimIDs_UO_To_LeftSHBEAtSomePoint.add(ClaimID);
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
                        ClaimIDs_UO_To_LeftSHBE_NotReturned.add(ClaimID);
                        if (bTT == 1) {
                            ClaimIDs_UOTT1_To_LeftSHBE.add(ClaimID);
                            ClaimIDs_UOTT1_To_LeftSHBE_NotReturned.add(ClaimID);
                            ClaimIDs_UOTT1_To_TT3OrTT6NotDoneNextChange.add(ClaimID);
                            ClaimIDs_UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(ClaimID);
                        } else if (bTT == 4) {
                            ClaimIDs_UOTT4_To_LeftSHBE.add(ClaimID);
                            ClaimIDs_UOTT4_To_LeftSHBE_NotReturned.add(ClaimID);
                            ClaimIDs_UOTT4_To_TT3OrTT6NotDoneNextChange.add(ClaimID);
                            ClaimIDs_UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(ClaimID);
                        } else if (bTT == 3 || bTT == 6) {
                            ClaimIDs_UOTT3OrTT6_To_LeftSHBE.add(ClaimID);
                            ClaimIDs_UOTT3OrTT6_To_LeftSHBE_NotReturned.add(ClaimID);
                        } else {
                            ClaimIDs_UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE.add(ClaimID);
                            ClaimIDs_UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned.add(ClaimID);
                        }
                    }
                }
            } else {
                ClaimIDs_ChangedTT.add(ClaimID);
                if (isUO) {
                    if (aTT == 4 && bTT == 1) {
                        //ClaimIDs_UOTT1OrTT1_To_UOTT4.add(ClaimID);
                        if (wasUO) {
                            ClaimIDs_UOTT1_To_UOTT4.add(ClaimID); // Looking forward, we may see that this claim actually comes out of being UO. To filter for this we should look back in the next iteration and perhaps move claim refs to other sets based upon some logic...
                        } else if (bStatus == 2 && wasUOBefore) {
                            ClaimIDs_UOTT1_To_UOTT4.add(ClaimID); // Looking forward, we may see that this claim actually comes out of being UO. To filter for this we should look back in the next iteration and perhaps move claim refs to other sets based upon some logic...
                        } else {
                            ClaimIDs_TT1_To_UOTT4.add(ClaimID);
                            if (aHBDP > 0) {
                                ClaimIDs_TT1_To_UOTT4GettingDHP.add(ClaimID);
                            }
                        }
                    }
                }
                doX(ClaimID, aHBDP, aTT, bTT, isUO, wasUO, wasUOBefore,
                        bStatus,
                        ClaimIDs_UOTT1_To_LeftSHBE_NotReturned,
                        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT1,
                        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT1,
                        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
                        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT4,
                        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT4,
                        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
                        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT8,
                        ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT9,
                        ClaimIDs_UOTT1_To_UOTT4,
                        ClaimIDs_TT1_To_UOTT4,
                        ClaimIDs_TT1_To_UOTT4GettingDHP);
                if (ClaimIDs_UOTT4_To_LeftSHBE_NotReturned.contains(ClaimID)) {
                    ClaimIDs_UOTT4_To_LeftSHBE_NotReturned.remove(ClaimID);
                    if (aTT == 1) {
                        if (isUO) {
                            ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsUOTT1.add(ClaimID);
                        } else {
                            ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT1.add(ClaimID);
                        }
                    } else if (aTT == 4) {
                        if (isUO) {
                            ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsUOTT4.add(ClaimID);
                        } else {
                            ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT4.add(ClaimID);
                        }
                    } else if (aTT == 3 || aTT == 6) {
                        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6.add(ClaimID);
                    } else if (aTT == 5 || aTT == 7) {
                        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7.add(ClaimID);
                    } else if (aTT == 8) {
                        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT8.add(ClaimID);
                    } else if (aTT == 9) {
                        ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAsTT9.add(ClaimID);
                    }
                }
                if (ClaimIDs_UOTT3OrTT6_To_LeftSHBE_NotReturned.contains(ClaimID)) {
                    ClaimIDs_UOTT3OrTT6_To_LeftSHBE_NotReturned.remove(ClaimID);
                    if (aTT == 1) {
                        if (isUO) {
                            ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1.add(ClaimID);
                        } else {
                            ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1.add(ClaimID);
                        }
                    } else if (aTT == 4) {
                        if (isUO) {
                            ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4.add(ClaimID);
                        } else {
                            ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4.add(ClaimID);
                        }
                    } else if (aTT == 3 || aTT == 6) {
                        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6.add(ClaimID);
                    } else if (aTT == 5 || aTT == 7) {
                        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7.add(ClaimID);
                    } else if (aTT == 8) {
                        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8.add(ClaimID);
                    } else if (aTT == 9) {
                        ClaimIDs_UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9.add(ClaimID);
                    }
                }
                if (ClaimIDs_UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned.contains(ClaimID)) {
                    ClaimIDs_UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned.remove(ClaimID);
                }
                if (ClaimIDs_UO_To_LeftSHBE_NotReturned.contains(ClaimID)) {
                    ClaimIDs_UO_To_LeftSHBE_NotReturned.remove(ClaimID);
                }
                if (aTT == 3 || aTT == 6) {
                    if (ClaimIDs_UOTT1_To_LeftSHBE.contains(ClaimID)) {
                        if (ClaimIDs_UOTT1_To_TT3OrTT6NotDoneNextChange.contains(ClaimID)) {
                            ClaimIDs_UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(ClaimID);
                        }
                    }
                    if (ClaimIDs_UOTT4_To_LeftSHBE.contains(ClaimID)) {
                        if (ClaimIDs_UOTT4_To_TT3OrTT6NotDoneNextChange.contains(ClaimID)) {
                            ClaimIDs_UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(ClaimID);
                        }
                    }
                    if (ClaimIDs_UOTT1AtSomePoint.contains(ClaimID)) {
                        ClaimIDs_UOTT1_To_TT3OrTT6AtSomePoint.add(ClaimID);
                    }
                    if (ClaimIDs_UOTT4AtSomePoint.contains(ClaimID)) {
                        ClaimIDs_UOTT4_To_TT3OrTT6AtSomePoint.add(ClaimID);
                    }
                    if (bTT == 1) {
                        ClaimIDs_TT1_To_TT3OrTT6.add(ClaimID);
                        // If previously UO then add to set of those that move from UO TT1 to TT3 or TT6
                        if (wasUO) {
                            ClaimIDs_UOTT1_To_TT3OrTT6.add(ClaimID);
                            ClaimIDs_UOTT1_To_TT3OrTT6NotDoneNextChange.add(ClaimID);
                            ClaimIDs_UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(ClaimID);
                        } else if (bStatus == 2) {
                            if (wasUOBefore) {
                                ClaimIDs_UOTT1_To_TT3OrTT6.add(ClaimID);
                                ClaimIDs_UOTT1_To_TT3OrTT6NotDoneNextChange.add(ClaimID);
                                ClaimIDs_UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(ClaimID);
                            }
                        }
                    } else if (bTT == 4) {
                        ClaimIDs_TT4_To_TT3OrTT6.add(ClaimID);
                        // If previously UO then add to set of those that move from UO TT4 to TT3 or TT6
                        if (wasUO) {
                            ClaimIDs_UOTT4_To_TT3OrTT6.add(ClaimID);
                            ClaimIDs_UOTT4_To_TT3OrTT6NotDoneNextChange.add(ClaimID);
                            ClaimIDs_UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(ClaimID);
                        } else if (bStatus == 2) {
                            if (wasUOBefore) {
                                ClaimIDs_UOTT4_To_TT3OrTT6.add(ClaimID);
                                ClaimIDs_UOTT4_To_TT3OrTT6NotDoneNextChange.add(ClaimID);
                                ClaimIDs_UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(ClaimID);
                            }
                        }
                    }
                } else {
                    if (ClaimIDs_UOTT1_To_TT3OrTT6NotDoneNextChange.contains(ClaimID)) {
                        if (ClaimIDs_UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.contains(ClaimID)) {
                            ClaimIDs_UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.add(ClaimID);
                        }
                    }
                    if (ClaimIDs_UOTT4_To_TT3OrTT6NotDoneNextChange.contains(ClaimID)) {
                        if (ClaimIDs_UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.contains(ClaimID)) {
                            ClaimIDs_UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.remove(ClaimID);
                        }
                    }
                    if (bTT == 3 || bTT == 6) {
                        if (aTT == 1) {
                            // If UO add to set that move from the PRS to Council UO.
                            if (isUO) {
                                ClaimIDs_TT3OrTT6_To_UOTT1.add(ClaimID);  // Looking forward, we may see that this claim actually comes out of being UO almost immediately. This is kind of different to those claims that get stuck in UO for a significant period.
                            } else {
                                ClaimIDs_TT3OrTT6_To_TT1.add(ClaimID);
                            }
                        } else if (aTT == 4) {
                            // If UO add to set that move from the PRS to RSL UO.
                            if (isUO) {
                                ClaimIDs_TT3OrTT6_To_UOTT4.add(ClaimID);  // Looking forward, we may see that this claim actually comes out of being UO almost immediately. This is kind of different to those claims that get stuck in UO for a significant period.
                            } else {
                                ClaimIDs_TT3OrTT6_To_TT4.add(ClaimID);
                            }
                        }
                    }
                }
            }
        }
        aS += DW_Strings.sCommaSpace + sTT_ + aTT;

        tableValues.put(key, aS);

        // UnderOccupancy
        key = CTBRef + DW_Strings.sUnderscore + sUnderOccupancy;
        aS = tableValues.get(key);
        if (isUO) {
            ClaimIDs_PermanantlyLeftUOButRemainedInSHBE.remove(ClaimID);
            ClaimIDs_PermanantlyLeftUOButRemainedInSHBE_PostcodeChanged.remove(ClaimID);
            ClaimIDs_PermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased.remove(ClaimID);
            result[0] = true;
            UOClaims.add(ClaimID);
            if (aTT == 1) {
                ClaimIDs_AlwaysUOTT1FromWhenStarted.add(ClaimID);
            } else if (aTT == 4) {
                ClaimIDs_AlwaysUOTT4FromWhenStarted.add(ClaimID);
            }
            aS += DW_Strings.sCommaSpace + sU;
            BigDecimal bd;
            String key2 = CTBRef + DW_Strings.sUnderscore + sTotalCount_UnderOccupancy;
            bd = AggregateStatistics.get(key2);
            bd = bd.add(BigDecimal.ONE);
            AggregateStatistics.put(key2, bd);
            if (aDW_SHBE_D_Record != null) {
                if (CouncilUnderOccupiedSet1.getMap().keySet().contains(ClaimID)) {
                    result[1] = true;
                    addToSets(
                            NINOToNINOIDLookup,
                            DOBToDOBIDLookup,
                            ClaimID,
                            CouncilUniqueClaimantsEffected,
                            CouncilUniquePartnersEffected,
                            tCouncilMaxNumberOfDependentsInClaimWhenUO,
                            CouncilUniqueNonDependentsEffected,
                            UniqueDependentChildrenUnderAge10Effected,
                            year,
                            month,
                            DW_SHBE_Record,
                            aDW_SHBE_D_Record);
                    DW_UO_Record rec = CouncilUnderOccupiedSet1.getMap().get(ClaimID);
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
                if (RSLUnderOccupiedSet1.getMap().keySet().contains(ClaimID)) {
                    result[2] = true;
                    addToSets(
                            NINOToNINOIDLookup,
                            DOBToDOBIDLookup,
                            ClaimID,
                            RSLUniqueClaimantsEffected,
                            RSLUniquePartnersEffected,
                            tRSLMaxNumberOfDependentsInClaimWhenUO,
                            RSLUniqueNonDependentsEffected,
                            UniqueDependentChildrenUnderAge10Effected,
                            year,
                            month,
                            DW_SHBE_Record,
                            aDW_SHBE_D_Record);
                    DW_UO_Record rec = RSLUnderOccupiedSet1.getMap().get(ClaimID);
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
                ClaimIDs_TTNot1Or4AndUnderOccupying.add(ClaimID);
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
                                ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged.add(ClaimID);
                                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedThisMonth.add(ClaimID);
                            } else if (aTT == 4) {
                                ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged.add(ClaimID);
                                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedThisMonth.add(ClaimID);
                            }
                        }
                    }
                    if (ClaimIDs_UO_NotUO.contains(ClaimID)) {
                        ClaimIDs_UO_NotUO_UO.add(ClaimID);
                        ClaimIDs_UO_NotUO.remove(ClaimID);
                    } else if (ClaimIDs_UO_NotUO_UO_NotUO.contains(ClaimID)) {
                        ClaimIDs_UO_NotUO_UO_NotUO_UO.add(ClaimID);
                        ClaimIDs_UO_NotUO_UO_NotUO.remove(ClaimID);
                    } else if (ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO.contains(ClaimID)) {
                        ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO.add(ClaimID);
                        ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO.remove(ClaimID);
                    } else if (ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO.contains(ClaimID)) {
                        ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO.add(ClaimID);
                        ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO.remove(ClaimID);
                    }
                }
            }
            if (ClaimIDs_UOTT1_To_LeftSHBE.contains(ClaimID)) {
                ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint.add(ClaimID);
            }
            if (ClaimIDs_UOTT4_To_LeftSHBE.contains(ClaimID)) {
                ClaimIDs_UOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint.add(ClaimID);
            }
        } else {
            aS += DW_Strings.sCommaSpace;
            ClaimIDs_AlwaysUOTT1FromStart.remove(ClaimID);
            ClaimIDs_AlwaysUOTT4FromStart.remove(ClaimID);
            if (aStatus == 2) {
                // Filter added as suspended claims that were UO are probably still UO
            } else {
                ClaimIDs_AlwaysUOTT1FromStartExceptWhenSuspended.remove(ClaimID);
                ClaimIDs_AlwaysUOTT4FromStartExceptWhenSuspended.remove(ClaimID);
                if (aS.contains(sU)) {
                    ClaimIDs_AlwaysUOTT1FromWhenStarted.remove(ClaimID);
                    ClaimIDs_AlwaysUOTT4FromWhenStarted.remove(ClaimID);
                    if (aS.contains(sU + DW_Strings.sCommaSpace + DW_Strings.sCommaSpace)) {
                        // ..., U, ,
                        ClaimIDs_IntermitantUO.add(ClaimID);
                    }
                }
                if (wasUO) {
                    if (ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO.contains(ClaimID)) {
                        ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO.add(ClaimID);
                        ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO_UO.remove(ClaimID);
                    } else if (ClaimIDs_UO_NotUO_UO_NotUO_UO.contains(ClaimID)) {
                        ClaimIDs_UO_NotUO_UO_NotUO_UO_NotUO.add(ClaimID);
                        ClaimIDs_UO_NotUO_UO_NotUO_UO.remove(ClaimID);
                    } else if (ClaimIDs_UO_NotUO_UO.contains(ClaimID)) {
                        ClaimIDs_UO_NotUO_UO_NotUO.add(ClaimID);
                        ClaimIDs_UO_NotUO_UO.remove(ClaimID);
                    } else {
                        ClaimIDs_UO_NotUO.add(ClaimID);
                    }
                    if (aTT == DW_SHBE_TenancyType_Handler.iMinus999) {
                        ClaimIDs_UO_To_LeftSHBETheVeryNextMonth.add(ClaimID);
                    } else {
                        ClaimIDs_PermanantlyLeftUOButRemainedInSHBE.add(ClaimID);
                        if (aHS > bHS) {
                            ClaimIDs_PermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased.add(ClaimID);
                        }
                        if (!aPC.equalsIgnoreCase(bPC)) {
                            if (validPostcodes.contains(aPC) && validPostcodes.contains(bPC)) {
                                ClaimIDs_PermanantlyLeftUOButRemainedInSHBE_PostcodeChanged.add(ClaimID);
                                if (bTT == 1) {
                                    ClaimIDs_UOTT1_To_NotUO_InSHBE_PostcodeChanged.add(ClaimID);
                                }
                                if (aTT == 1) {
                                    if (bTT == 1) {
                                        ClaimIDs_UOTT1_To_TT1_PostcodeChanged.add(ClaimID);
                                    } else if (bTT == 4) {
                                        ClaimIDs_UOTT4_To_TT1_PostcodeChanged.add(ClaimID);
                                    }
                                } else if (aTT == 4) {
                                    ClaimIDs_UOTT4_To_NotUO_InSHBE_PostcodeChanged.add(ClaimID);
                                    if (bTT == 1) {
                                        ClaimIDs_UOTT1_To_TT4_PostcodeChanged.add(ClaimID);
                                    } else if (bTT == 4) {
                                        ClaimIDs_UOTT4_To_TT4_PostcodeChanged.add(ClaimID);
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
                                ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged.add(ClaimID);
                                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedThisMonth.add(ClaimID);
                            } else if (aTT == 4) {
                                ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged.add(ClaimID);
                                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedThisMonth.add(ClaimID);
                            }
                        }
                    }
                }
            }
        }

        tableValues.put(key, aS);

        // Postcode
        key = CTBRef + DW_Strings.sUnderscore + sP;
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
                                ClaimIDs_Traveller.add(ClaimID);
                            }
                            if (validPostcodes.contains(aPC) && validPostcodes.contains(bPC)) {
                                ClaimIDs_ValidPostcodeChange.add(ClaimID);
                            }
                        }
                    } else if (containsAnotherPostcode) {
                        if (validPostcodes.contains(aPC) && validPostcodes.contains(bPC)) {
                            ClaimIDs_ValidPostcodeChange.add(ClaimID);
                        }
                    }
                } else {
                    if (validPostcodes.contains(aPC) && validPostcodes.contains(bPC)) {
                        ClaimIDs_ValidPostcodeChange.add(ClaimID);
                    }
                    if (aSContainsaPC) {
//                        containsAnotherPostcode = getContainsAnotherPostcode(aS, aPC);
//                        if (containsAnotherPostcode) {
                        boolean likelyTraveller;
                        likelyTraveller = getLikelyTraveller(aS, aPC);
                        if (likelyTraveller) {
                            ClaimIDs_Traveller.add(ClaimID);
                        }
//                        }
//                        ClaimIDs_NoValidPostcodeChange.remove(ClaimID);
//                    } else {
//                        containsAnotherPostcode = getContainsAnotherPostcode(aS, aPC);
//                        if (containsAnotherPostcode) {
//                            ClaimIDs_NoValidPostcodeChange.remove(ClaimID);
//                        }
                    }
                    if (wasUO && !isUO) {
                        if (aTT == bTT) {
                            if (aTT == 1) {
                                if (validPostcodes.contains(aPC)) {
                                    ClaimIDs_UOTT1_ToTT1_PostcodeChanged.add(ClaimID);
                                }
                            } else if (aTT == 4) {
                                if (validPostcodes.contains(aPC)) {
                                    ClaimIDs_UOTT4_ToTT4_PostcodeChanged.add(ClaimID);
                                }
                            }
                        }
                    } else if (!wasUO && isUO) {
                        if (aTT == bTT) {
                            if (aTT == 1) {
                                if (validPostcodes.contains(aPC)) {
                                    ClaimIDs_TT1_ToUOTT1_PostcodeChanged.add(ClaimID);
                                }
                            } else if (aTT == 4) {
                                if (validPostcodes.contains(aPC)) {
                                    ClaimIDs_TT4_ToUOTT4_PostcodeChanged.add(ClaimID);
                                }
                            }
                        }
                    } else if (wasUO && isUO) {
                        if (aTT == bTT) {
                            if (aTT == 1) {
                                if (validPostcodes.contains(aPC)) {
                                    ClaimIDs_UOTT1_ToUOTT1_PostcodeChanged.add(ClaimID);
                                }
                            } else if (aTT == 4) {
                                if (validPostcodes.contains(aPC)) {
                                    ClaimIDs_UOTT4_ToUOTT4_PostcodeChanged.add(ClaimID);
                                }
                            }
                        }
                    }
                    if (validPostcodes.contains(aPC) && validPostcodes.contains(bPC)) {
                        if (ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged1MonthPrevious.contains(ClaimID)) {
                            if (aTT == 1) {
                                //ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month.add(ClaimID);
                                if (isUO) {
                                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1.add(ClaimID);
                                } else {
                                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1.add(ClaimID);
                                }
                            } else if (aTT == 3 || aTT == 6) {
                                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6.add(ClaimID);
                            } else if (aTT == 4) {
                                if (isUO) {
                                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4.add(ClaimID);
                                } else {
                                    ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4.add(ClaimID);
                                }
                            } else if (aTT == 5 || aTT == 7) {
                                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7.add(ClaimID);
                            } else if (aTT == 8) {
                                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8.add(ClaimID);
                            } else if (aTT == 9) {
                                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9.add(ClaimID);
                            }
                        }
                        if (ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged1MonthPrevious.contains(ClaimID)) {
                            if (aTT == 1) {
                                //ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month.add(aClaimID);
                                if (isUO) {
                                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1.add(ClaimID);
                                } else {
                                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1.add(ClaimID);
                                }
                            } else if (aTT == 3 || aTT == 6) {
                                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6.add(ClaimID);
                            } else if (aTT == 4) {
                                if (isUO) {
                                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1.add(ClaimID);
                                } else {
                                    ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4.add(ClaimID);
                                }
                            } else if (aTT == 5 || aTT == 7) {
                                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7.add(ClaimID);
                            } else if (aTT == 8) {
                                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8.add(ClaimID);
                            } else if (aTT == 9) {
                                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9.add(ClaimID);
                            }
                        }

                        if (ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged2MonthsPrevious.contains(ClaimID)) {
                            if (aTT == 4) {
                                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months.add(ClaimID);
                            }
                        }
                        if (ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged2MonthsPrevious.contains(ClaimID)) {
                            if (aTT == 1) { // Additional filter added as we only want those that are in the same TT.
                                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months.add(ClaimID);
                            }
                        }
                        if (ClaimIDs_TT4_To_UOTT4_PostcodeUnchanged3MonthsPrevious.contains(ClaimID)) {
                            if (aTT == 4) { // Additional filter added as we only want those that are in the same TT.
                                ClaimIDs_TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months.add(ClaimID);
                            }
                        }
                        if (ClaimIDs_TT1_To_UOTT1_PostcodeUnchanged3MonthsPrevious.contains(ClaimID)) {
                            if (aTT == 1) {
                                ClaimIDs_TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months.add(ClaimID);
                            }
                        }
                        if (ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged1MonthPrevious.contains(ClaimID)) {
                            if (aTT == 1) {
                                if (isUO) {
                                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1.add(ClaimID);
                                } else {
                                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1.add(ClaimID);
                                }
                            } else if (aTT == 4) {
                                if (isUO) {
                                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4.add(ClaimID);
                                } else {
                                    ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4.add(ClaimID);
                                }
                            } else if (aTT == 3 || aTT == 6) {
                                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6.add(ClaimID);
                            } else if (aTT == 5 || aTT == 7) {
                                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7.add(ClaimID);
                            } else if (aTT == 8) {
                                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8.add(ClaimID);
                            } else if (aTT == 9) {
                                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9.add(ClaimID);
                            }
                        }
                        if (ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged1MonthPrevious.contains(ClaimID)) {
                            if (aTT == 1) {
                                if (isUO) {
                                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1.add(ClaimID);
                                } else {
                                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1.add(ClaimID);
                                }
                            } else if (aTT == 4) {
                                if (isUO) {
                                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4.add(ClaimID);
                                } else {
                                    ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4.add(ClaimID);
                                }
                            } else if (aTT == 3 || aTT == 6) {
                                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6.add(ClaimID);
                            } else if (aTT == 5 || aTT == 7) {
                                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7.add(ClaimID);
                            } else if (aTT == 8) {
                                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8.add(ClaimID);
                            } else if (aTT == 9) {
                                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9.add(ClaimID);
                            }
                        }
                        if (ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged2MonthsPrevious.contains(ClaimID)) {
                            if (aTT == 4) { // Additional filter added as we only want those that are in the same TT.
                                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months.add(ClaimID);
                            }
                        }
                        if (ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged2MonthsPrevious.contains(ClaimID)) {
                            if (aTT == 1) { // Additional filter added as we only want those that are in the same TT.
                                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months.add(ClaimID);
                            }
                        }
                        if (ClaimIDs_UOTT4_To_TT4_PostcodeUnchanged3MonthsPrevious.contains(ClaimID)) {
                            if (aTT == 4) { // Additional filter added as we only want those that are in the same TT.
                                ClaimIDs_UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months.add(ClaimID);
                            }
                        }
                        if (ClaimIDs_UOTT1_To_TT1_PostcodeUnchanged3MonthsPrevious.contains(ClaimID)) {
                            if (aTT == 1) { // Additional filter added as we only want those that are in the same TT.
                                ClaimIDs_UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months.add(ClaimID);
                            }
                        }
                    }
                }
            }
        }

        tableValues.put(key, aS);

        // HB Entitlement
        key = CTBRef + DW_Strings.sUnderscore + sWHBE;
        aS = tableValues.get(key);
        if (aWHBE == bWHBE) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + decimalise(aWHBE);
        }

        tableValues.put(key, aS);

        // ERA
        key = CTBRef + DW_Strings.sUnderscore + sWERA;
        aS = tableValues.get(key);
        if (aWERA == bWERA) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + decimalise(aWERA);
        }

        tableValues.put(key, aS);

        // PassportedStandardIndicator
        key = CTBRef + DW_Strings.sUnderscore + sPSI;
        aS = tableValues.get(key);
        if (aPSI == bPSI) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aPSI;
        }

        tableValues.put(key, aS);

        // StatusOfHBClaim
        key = CTBRef + DW_Strings.sUnderscore + sSHBC;
        aS = tableValues.get(key);
        if (aSHBC == bSHBC) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aSHBC;
        }

        tableValues.put(key, aS);

        // ReasonThatHBClaimClosed
        key = CTBRef + DW_Strings.sUnderscore + sRTHBCC;
        aS = tableValues.get(key);
        if (aRTHBCC == bRTHBCC) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aRTHBCC;
        }

        tableValues.put(key, aS);

        // ClaimantEthnicGroup
        key = CTBRef + DW_Strings.sUnderscore + sCEG;
        aS = tableValues.get(key);
        if (aCEG == bCEG) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aCEG;
        }

        tableValues.put(key, aS);

        // HS
        key = CTBRef + DW_Strings.sUnderscore + sHS;
        aS = tableValues.get(key);
        if (aHS == bHS) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aHS;
        }

        tableValues.put(key, aS);

        // NonDependents
        key = CTBRef + DW_Strings.sUnderscore + sND;
        aS = tableValues.get(key);
        if (aND == bND) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aND;
        }

        tableValues.put(key, aS);

        // ChildDependents
        key = CTBRef + DW_Strings.sUnderscore + sCD;
        aS = tableValues.get(key);
        if (aCD == bCD) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aCD;
        }

        tableValues.put(key, aS);

        // UO
        DW_UO_Record aDW_UOReport_Record;

        if (CouncilUnderOccupiedSet1.getMap().keySet().contains(ClaimID)
                || RSLUnderOccupiedSet1.getMap().keySet().contains(ClaimID)) {
            if (CouncilUnderOccupiedSet1.getMap().keySet().contains(ClaimID)) {
                aDW_UOReport_Record = CouncilUnderOccupiedSet1.getMap().get(ClaimID);
            } else {
                aDW_UOReport_Record = RSLUnderOccupiedSet1.getMap().get(ClaimID);
            }
            // NonDependents
            key = CTBRef + DW_Strings.sUnderscore + sNDUO;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace + aDW_UOReport_Record.getNonDependents();
            tableValues.put(key, aS);
            // Children 16 +
            key = CTBRef + DW_Strings.sUnderscore + sCO16;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace + aDW_UOReport_Record.getChildrenOver16();
            tableValues.put(key, aS);
            // FemaleChildrenUnder10
            key = CTBRef + DW_Strings.sUnderscore + sFCU10;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace + aDW_UOReport_Record.getFemaleChildrenUnder10();
            tableValues.put(key, aS);
            // MaleChildrenUnder10
            key = CTBRef + DW_Strings.sUnderscore + sMCU10;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace + aDW_UOReport_Record.getMaleChildrenUnder10();
            tableValues.put(key, aS);
            // FemaleChildren10to16
            key = CTBRef + DW_Strings.sUnderscore + sFC10To16;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace + aDW_UOReport_Record.getFemaleChildren10to16();
            tableValues.put(key, aS);
            // MaleChildren10to16
            key = CTBRef + DW_Strings.sUnderscore + sMC10To16;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace + aDW_UOReport_Record.getMaleChildren10to16();
            tableValues.put(key, aS);
            // Number of Bedrooms
            key = CTBRef + DW_Strings.sUnderscore + sNB;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace + aDW_UOReport_Record.getBedroomsInProperty();
            tableValues.put(key, aS);
            // Bedroom Requirement
            key = CTBRef + DW_Strings.sUnderscore + sBR;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace + aDW_UOReport_Record.getBedroomRequirement();
            tableValues.put(key, aS);
            int loss;
            loss = aWERA - aWHBE;
            key = CTBRef + DW_Strings.sUnderscore + sTotal_HBLossDueToUO;
            BigDecimal bd;
            bd = AggregateStatistics.get(key);
            bd = bd.add(BigDecimal.valueOf(loss));
            AggregateStatistics.put(key, bd);
            if (loss > 0) {
                key = CTBRef + DW_Strings.sUnderscore + sTotalCount_HBLossDueToUO;
                bd = AggregateStatistics.get(key);
                bd = bd.add(BigDecimal.ONE);
                AggregateStatistics.put(key, bd);
            }
        } else {
            // NonDependents
            key = CTBRef + DW_Strings.sUnderscore + sNDUO;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace;
            tableValues.put(key, aS);
            // Children 16 +
            key = CTBRef + DW_Strings.sUnderscore + sCO16;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace;
            tableValues.put(key, aS);
            // FemaleChildrenUnder10
            key = CTBRef + DW_Strings.sUnderscore + sFCU10;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace;
            tableValues.put(key, aS);
            // MaleChildrenUnder10
            key = CTBRef + DW_Strings.sUnderscore + sMCU10;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace;
            tableValues.put(key, aS);
            // FemaleChildren10to16
            key = CTBRef + DW_Strings.sUnderscore + sFC10To16;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace;
            tableValues.put(key, aS);
            // MaleChildren10to16
            key = CTBRef + DW_Strings.sUnderscore + sMC10To16;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace;
            tableValues.put(key, aS);
            // Number of Bedrooms
            key = CTBRef + DW_Strings.sUnderscore + sNB;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace;
            tableValues.put(key, aS);
            // Bedroom Requirement
            key = CTBRef + DW_Strings.sUnderscore + sBR;
            aS = tableValues.get(key);
            aS += DW_Strings.sCommaSpace;
            tableValues.put(key, aS);
        }
        // Claimants DoB
        key = CTBRef + DW_Strings.sUnderscore + sCDoB;
        aS = tableValues.get(key);

        if (aCDoB.equalsIgnoreCase(bCDoB)) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aCDoB;
        }

        tableValues.put(key, aS);
        // Claimants Age
        key = CTBRef + DW_Strings.sUnderscore + sCA;
        aS = tableValues.get(key);

        if (aCA.equalsIgnoreCase(bCA)) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aCA;
        }

        tableValues.put(key, aS);
        // Partners DoB
        key = CTBRef + DW_Strings.sUnderscore + sPDoB;
        aS = tableValues.get(key);

        if (aPDoB.equalsIgnoreCase(bPDoB)) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aPDoB;
        }

        tableValues.put(key, aS);
        // Partners Age
        key = CTBRef + DW_Strings.sUnderscore + sPA;
        aS = tableValues.get(key);

        if (aPA.equalsIgnoreCase(bPA)) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aPA;
        }

        tableValues.put(key, aS);
        // ClaimantsGender
        key = CTBRef + DW_Strings.sUnderscore + sCG;
        aS = tableValues.get(key);

        if (aCA.equalsIgnoreCase(bCA)) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aCG;
        }

        tableValues.put(key, aS);
        // PartnersGender
        key = CTBRef + DW_Strings.sUnderscore + sPG;
        aS = tableValues.get(key);

        if (aPA.equalsIgnoreCase(bPA)) {
            aS += DW_Strings.sCommaSpace;
        } else {
            aS += DW_Strings.sCommaSpace + aPG;
        }

        tableValues.put(key, aS);
        // Disability
        key = CTBRef + DW_Strings.sUnderscore + sDisability;
        aS = tableValues.get(key);
        aS += DW_Strings.sCommaSpace + aD;

        tableValues.put(key, aS);
        // Disability Premium
        key = CTBRef + DW_Strings.sUnderscore + sDisabilityPremium;
        aS = tableValues.get(key);
        aS += DW_Strings.sCommaSpace + aDP;

        tableValues.put(key, aS);
        // Disability Severe
        key = CTBRef + DW_Strings.sUnderscore + sDisabilitySevere;
        aS = tableValues.get(key);
        aS += DW_Strings.sCommaSpace + aDS;

        tableValues.put(key, aS);
        // Disability Enhanced
        key = CTBRef + DW_Strings.sUnderscore + sDisabilityEnhanced;
        aS = tableValues.get(key);
        aS += DW_Strings.sCommaSpace + aDE;

        tableValues.put(key, aS);
        // Child Disability
        key = CTBRef + DW_Strings.sUnderscore + sDisabledChild;
        aS = tableValues.get(key);
        aS += DW_Strings.sCommaSpace + aDC;

        tableValues.put(key, aS);
        // Partner Death
        key = CTBRef + DW_Strings.sUnderscore + sPDeath;
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
        key = CTBRef + DW_Strings.sUnderscore + sTotal_DHP;
        bd = AggregateStatistics.get(key);
        bd = bd.add(BigDecimal.valueOf(aHBDP));

        AggregateStatistics.put(key, bd);
        if (aHBDP
                > 0) {
            ClaimIDs_DHPAtSomePoint.add(ClaimID);
            key = CTBRef + DW_Strings.sUnderscore + sTotalCount_DHP;
            bd = AggregateStatistics.get(key);
            bd = bd.add(BigDecimal.ONE);
            AggregateStatistics.put(key, bd);
        }

//        // CTBDP
//        key = CTBRef + sUnderscore + sCTBDP;
//        aS = tableValues.get(key);
//        if (aCTBDP > 0) {
//            aS += sCommaSpace + sCTBDP + sUnderscore + aCTBDP;
//        } else {
//            aS += sCommaSpace;
//        }
//        tableValues.put(key, aS);
        // Arrears
        key = CTBRef + DW_Strings.sUnderscore + sA;
        aS = tableValues.get(key);

        if (CouncilUnderOccupiedSet1.getMap().keySet().contains(ClaimID)) {
            DW_UO_Record UORec;
            UORec = CouncilUnderOccupiedSet1.getMap().get(ClaimID);
            if (UORec == null) {
                aS += DW_Strings.sCommaSpace;
            } else {
                aArrears = UORec.getTotalRentArrears();
                if (aArrears == null) {
                    aS += DW_Strings.sCommaSpace;
                } else {
                    aS += DW_Strings.sCommaSpace + aArrears;
                    key = CTBRef + DW_Strings.sUnderscore + sMax_Arrears;
                    bd = AggregateStatistics.get(key);
                    bd = bd.max(BigDecimal.valueOf(aArrears));
                    AggregateStatistics.put(key, bd);
                    if (aArrears > 0) {
                        ClaimIDs_InArrearsAtSomePoint.add(ClaimID);
                        key = CTBRef + DW_Strings.sUnderscore + sTotalCount_Arrears;
                        bd = AggregateStatistics.get(key);
                        bd = bd.add(BigDecimal.ONE);
                        AggregateStatistics.put(key, bd);
                    }
                    if (aHBDP > 0) {
                        if (aArrears > 0) {
                            ClaimIDs_UOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint.add(ClaimID);
                        }
                    }
                    if (aArrears > 0) {
                        ClaimIDs_UOTT1ClaimsInRentArrearsAtSomePoint.add(ClaimID);
                    }
                    if (isUO) {
                        if (aTT == 1 && bTT == 4) {
                            //ClaimIDs_UOTT4OrTT4_To_UOTT1.add(ClaimID);
                            //if (aArrears > 0) {
                            //    ClaimIDs_UOTT4OrTT4_To_UOTT1InArrears.add(ClaimID);
                            //    if (aHBDP > 0) {
                            //        ClaimIDs_UOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP.add(ClaimID);
                            //    }
                            //}
                            if (wasUO) {
                                ClaimIDs_UOTT4_To_UOTT1.add(ClaimID);
                                if (aHBDP > 0) {
                                    ClaimIDs_UOTT4_To_UOTT1GettingDHP.add(ClaimID);
                                }
                                if (aArrears > 0) {
                                    ClaimIDs_UOTT4_To_UOTT1InArrears.add(ClaimID);
                                    if (aHBDP > 0) {
                                        ClaimIDs_UOTT4_To_UOTT1InArrearsAndGettingDHP.add(ClaimID);
                                    }
                                }
                            } else {
                                ClaimIDs_TT4_To_UOTT1.add(ClaimID);
                                if (aHBDP > 0) {
                                    ClaimIDs_TT4_To_UOTT1GettingDHP.add(ClaimID);
                                }
                                if (aArrears > 0) {
                                    ClaimIDs_TT4_To_UOTT1InArrears.add(ClaimID);
                                    if (aHBDP > 0) {
                                        ClaimIDs_TT4_To_UOTT1InArrearsAndGettingDHP.add(ClaimID);
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

        if (ClaimIDs_UOTT1_To_TT1_PostcodeChanged.contains(ClaimID)) {
            if (wasUOBefore && cTT == 1
                    && (!wasUO) && bTT == 1
                    && !(cPC.equalsIgnoreCase(bPC))
                    && (validPostcodes.contains(cPC) && validPostcodes.contains(bPC))) {
                if (isUO) {
                    ClaimIDs_UOTT1_To_TT1_PostcodeChanged.remove(ClaimID);
                    if (aTT == 1) {
                        ClaimIDs_UOTT1_To_UOTT1_PostcodeChanged.add(ClaimID);
                    } else if (aTT == 4) {
                        ClaimIDs_UOTT1_To_UOTT4_PostcodeChanged.add(ClaimID);
                    }
                }
            }
        }

        if (ClaimIDs_UOTT4_To_TT4_PostcodeChanged.contains(ClaimID)) {
            if (wasUOBefore && cTT == 1
                    && (!wasUO) && bTT == 1
                    && !(cPC.equalsIgnoreCase(bPC))
                    && (validPostcodes.contains(cPC) && validPostcodes.contains(bPC))) {
                if (isUO) {
                    ClaimIDs_UOTT4_To_TT4_PostcodeChanged.remove(ClaimID);
                    if (aTT == 1) {
                        ClaimIDs_UOTT4_To_UOTT1_PostcodeChanged.add(ClaimID);
                    } else if (aTT == 4) {
                        ClaimIDs_UOTT4_To_UOTT4_PostcodeChanged.add(ClaimID);
                    }
                }
            }
        }

        if (ClaimIDs_UOTT1_To_TT3OrTT6.contains(ClaimID)) {
            if (aTT == 1) {
                ClaimIDs_UOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint.add(ClaimID);
            }
        }

        if (ClaimIDs_UOTT4_To_TT3OrTT6.contains(ClaimID)) {
            if (aTT == 4) {
                ClaimIDs_UOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint.add(ClaimID);
            }
        }

        tableValues.put(key, aS);
        return result;
    }

    private void doX(
            DW_ID ClaimID,
            int aHBDP,
            int aTT,
            int bTT,
            boolean isUO,
            boolean wasUO,
            boolean wasUOBefore,
            int bStatus,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_NotReturned,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT1,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT1,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT4,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT4,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT8,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT9,
            TreeSet<DW_ID> ClaimIDs_UOTT1_To_UOTT4,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT4,
            TreeSet<DW_ID> ClaimIDs_TT1_To_UOTT4GettingDHP
    ) {
        if (ClaimIDs_UOTT1_To_LeftSHBE_NotReturned.contains(ClaimID)) {
            ClaimIDs_UOTT1_To_LeftSHBE_NotReturned.remove(ClaimID);
            if (aTT == 1) {
                if (isUO) {
                    ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT1.add(ClaimID);
                } else {
                    ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT1.add(ClaimID);
                }
            } else if (aTT == 3 || aTT == 6) {
                ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6.add(ClaimID);
            } else if (aTT == 4) {
                if (isUO) {
                    ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsUOTT4.add(ClaimID);
                    if (bTT == 1) {
                        //ClaimIDs_UOTT1OrTT1_To_UOTT4.add(aCTBRef);
                        if (wasUO) {
                            ClaimIDs_UOTT1_To_UOTT4.add(ClaimID); // Looking forward, we may see that this claim actually comes out of being UO. To filter for this we should look back in the next iteration and perhaps move claim refs to other sets based upon some logic...
                        } else if (bStatus == 2 && wasUOBefore) {
                            ClaimIDs_UOTT1_To_UOTT4.add(ClaimID); // Looking forward, we may see that this claim actually comes out of being UO. To filter for this we should look back in the next iteration and perhaps move claim refs to other sets based upon some logic...
                        } else {
                            ClaimIDs_TT1_To_UOTT4.add(ClaimID);
                            if (aHBDP > 0) {
                                ClaimIDs_TT1_To_UOTT4GettingDHP.add(ClaimID);
                            }
                        }
                    }
                } else {
                    ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT4.add(ClaimID);
                }
            } else if (aTT == 5 || aTT == 7) {
                ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7.add(ClaimID);
            } else if (aTT == 8) {
                ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT8.add(ClaimID);
            } else if (aTT == 9) {
                ClaimIDs_UOTT1_To_LeftSHBE_ReturnedAsTT9.add(ClaimID);
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
     * @param CouncilUnderOccupiedSets
     * @param RSLUnderOccupiedSets
     * @param SHBEFilenames
     * @param include
     * @return
     */
    public HashSet<DW_ID>[] getStartUOCTBRefs(
            TreeMap<String, DW_UO_Set> CouncilUnderOccupiedSets,
            TreeMap<String, DW_UO_Set> RSLUnderOccupiedSets,
            String[] SHBEFilenames,
            ArrayList<Integer> include
    ) {
        HashSet<DW_ID>[] result;
        result = new HashSet[2];
        result[0] = new HashSet<DW_ID>();
        result[1] = new HashSet<DW_ID>();
        String yM31 = s;
        DW_UO_Set CouncilUO = null;
        DW_UO_Set RSLUO = null;
        String filename1 = null;
        Iterator<Integer> includeIte;
        includeIte = include.iterator();
        int i;
        i = includeIte.next();
        filename1 = SHBEFilenames[i];
        yM31 = DW_SHBE_Handler.getYM3(filename1);
        CouncilUO = CouncilUnderOccupiedSets.get(yM31);
        if (CouncilUO != null) {
            RSLUO = RSLUnderOccupiedSets.get(yM31);
            // Add to result
            result[0].addAll(CouncilUO.getMap().keySet());
            result[1].addAll(RSLUO.getMap().keySet());
        }
        return result;
    }

    /**
     * This returns the CTBRefs of the UnderOccupying claims in the last include
     * period.
     *
     * @TODO For overall computational efficiency, this should probably only be
     * calculated once for each period and stored in a file then re-read from
     * file when needed.
     * @param CouncilUnderOccupiedSets
     * @param RSLUnderOccupiedSets
     * @param SHBEFilenames
     * @param include
     * @return
     */
    public HashSet<DW_ID>[] getEndUOClaimIDs(
            TreeMap<String, DW_UO_Set> CouncilUnderOccupiedSets,
            TreeMap<String, DW_UO_Set> RSLUnderOccupiedSets,
            String[] SHBEFilenames,
            ArrayList<Integer> include
    ) {
        HashSet<DW_ID>[] result;
        result = new HashSet[2];
        result[0] = new HashSet<DW_ID>();
        result[1] = new HashSet<DW_ID>();
        String yM31 = s;
        DW_UO_Set CouncilUnderOccupiedSet1 = null;
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
        CouncilUnderOccupiedSet1 = CouncilUnderOccupiedSets.get(yM31);
        if (CouncilUnderOccupiedSet1 != null) {
            RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(yM31);
            // Add to result
            result[0].addAll(CouncilUnderOccupiedSet1.getMap().keySet());
            result[1].addAll(RSLUnderOccupiedSet1.getMap().keySet());
        }
        return result;
    }

    public HashSet<DW_ID>[] getUOClaimIDs(
            TreeMap<String, DW_UO_Set> councilUnderOccupiedSets,
            TreeMap<String, DW_UO_Set> RSLUnderOccupiedSets,
            String[] SHBEFilenames,
            ArrayList<Integer> include
    ) {
        HashSet<DW_ID>[] result;
        result = new HashSet[2];
        result[0] = new HashSet<DW_ID>();
        result[1] = new HashSet<DW_ID>();
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

    protected void addToSets(
            HashMap<String, DW_ID> NINOToNINOIDLookup,
            HashMap<String, DW_ID> DOBToDOBIDLookup,
            DW_ID ClaimID,
            HashSet<DW_PersonID> claimantsSet,
            HashSet<DW_PersonID> partnersSet,
            HashMap<DW_ID, Integer> maxNumberOfDependentsInClaimWhenUO,
            HashSet<DW_PersonID> nonDependentsSet,
            HashSet<DW_PersonID> UniqueDependentChildrenUnderAge10Effected,
            String year,
            String month,
            DW_SHBE_Record aDW_SHBE_Record,
            DW_SHBE_D_Record aDW_SHBE_D_Record) {
        String ClaimantNINO;
        ClaimantNINO = aDW_SHBE_D_Record.getClaimantsNationalInsuranceNumber();
        DW_ID ClaimantNINOID;
        ClaimantNINOID = NINOToNINOIDLookup.get(ClaimantNINO);
        String aDOB;
        aDOB = aDW_SHBE_D_Record.getClaimantsDateOfBirth();
        DW_ID aDOBID;
        aDOBID = DOBToDOBIDLookup.get(aDOB);
        DW_PersonID aID;
        aID = new DW_PersonID(ClaimantNINOID, aDOBID);
        String aNINO;
        DW_ID aNINOID;
        claimantsSet.add(aID);
        if (aDW_SHBE_D_Record.getPartnerFlag() != 0) {
            aNINO = aDW_SHBE_D_Record.getPartnersNationalInsuranceNumber();
            aNINOID = NINOToNINOIDLookup.get(aNINO);
            aDOB = aDW_SHBE_D_Record.getPartnersDateOfBirth();
            aDOBID = DOBToDOBIDLookup.get(aDOB);
            aID = new DW_PersonID(aNINOID, aDOBID);
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
                m = maxNumberOfDependentsInClaimWhenUO.get(ClaimID);
                if (m == null) {
                    m = 0;
                }
                int numberOfChildDependents;
                numberOfChildDependents = aDW_SHBE_D_Record.getNumberOfChildDependents();
                int max;
                max = Math.max(m, numberOfChildDependents);
                maxNumberOfDependentsInClaimWhenUO.put(ClaimID, max);
                String DoB = SRecord.getSubRecordDateOfBirth();
                int age = Integer.valueOf(DW_SHBE_Handler.getAge(year, month, DoB));
                if (age < 10) {
                    aID = getSID(
                            NINOToNINOIDLookup,
                            DOBToDOBIDLookup,
                            SRecord,
                            subRecordType,
                            ClaimantNINO,
                            0,
                            nonDependentsSet);
                    UniqueDependentChildrenUnderAge10Effected.add(aID);
                }
            } else {
                aID = getSID(
                        NINOToNINOIDLookup,
                        DOBToDOBIDLookup,
                        SRecord,
                        subRecordType,
                        ClaimantNINO,
                        0,
                        nonDependentsSet);
                nonDependentsSet.add(aID);
            }
        }
    }

    protected DW_PersonID getSID(
            HashMap<String, DW_ID> NINOToNINOIDLookup,
            HashMap<String, DW_ID> DOBToDOBIDLookup,
            DW_SHBE_S_Record SRecord,
            int subRecordType,
            String claimantNINO,
            int i,
            HashSet<DW_PersonID> set) {
        DW_PersonID aID = null;
        String aNINO = SRecord.getSubRecordChildReferenceNumberOrNINO();
        DW_ID aNINOID;
        aNINOID = NINOToNINOIDLookup.get(aNINO);
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
        DW_ID aDOBID;
        aDOBID = DOBToDOBIDLookup.get(aDOB);
        aID = new DW_PersonID(aNINOID, aDOBID);
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
