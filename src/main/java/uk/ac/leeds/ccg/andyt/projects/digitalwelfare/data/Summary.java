/*
 * Copyright (C) 2015 geoagdt.
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
import uk.ac.leeds.ccg.andyt.generic.data.Generic_UKPostcode_Handler;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.math.Generic_BigDecimal;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Time;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_PersonID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Collection;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_CollectionHandler;
//import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.PostcodeID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_D_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.ID_PostcodeID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.ID_TenancyType;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.ID_TenancyType_PostcodeID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UOReport_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Set;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class Summary {

    private transient final DW_Environment env;

    public final DW_SHBE_CollectionHandler collectionHandler;

    private final DW_SHBE_Handler tDW_SHBE_Handler;

    private static final int decimalPlacePrecisionForAverage = 3;
    private static final int decimalPlacePrecisionForPercentage = 3;

    private static final String postcodeLS277NS = "LS27 7NS";
    private static final String s0 = "0";
    // Counter Strings
    // HouseholdSize
    private static final String sAllTotalHouseholdSize = "AllTotalHouseholdSize";
    private static final String sAllAverageHouseholdSize = "AllAverageHouseholdSize";
    private static final String sHBTotalHouseholdSize = "HBTotalHouseholdSize";
    private static final String sHBAverageHouseholdSize = "HBAverageHouseholdSize";
    private static final String sCTBTotalHouseholdSize = "CTBTotalHouseholdSize";
    private static final String sCTBAverageHouseholdSize = "CTBAverageHouseholdSize";
    // PSI
    private static String[] sAllTotalCount_PSI;
    private static String[] sHBTotalCount_PSI;
    private static String[] sCTBTotalCountPSI;
    private static String[] sAllPercentageOfAll_PSI;
    private static String[] sHBPercentageOfHB_PSI;
    private static String[] sCTBPercentageOfCTB_PSI;
    // PSIByTT
    private static String[][] sAllTotalCount_PSIByTT;
    private static String[][] sHBTotalCount_PSIByTT;
    private static String[][] sCTBTotalCount_PSIByTT;
    private static String[][] sAllPercentageOfAll_PSIByTT;
    private static String[][] sHBPercentageOfHB_PSIByTT;
    private static String[][] sCTBPercentageOfCTB_PSIByTT;
    // DisabilityPremiumAwardByTT
    private static String[] sAllTotalCount_DisabilityPremiumAwardByTT;
    private static String[] sHBTotalCount_DisabilityPremiumAwardByTT;
    private static String[] sCTBTotalCount_DisabilityPremiumAwardByTT;
    private static String[] sAllPercentageOfAll_DisabilityPremiumAwardByTT;
    private static String[] sHBPercentageOfHB_DisabilityPremiumAwardByTT;
    private static String[] sCTBPercentageOfCTB_DisabilityPremiumAwardByTT;
    // SevereDisabilityPremiumAwardByTT
    private static String[] sAllTotalCountSevereDisabilityPremiumAwardByTT;
    private static String[] sHBTotalCount_SevereDisabilityPremiumAwardByTT;
    private static String[] sCTBTotalCount_SevereDisabilityPremiumAwardByTT;
    private static String[] sAllPercentageOfAll_SevereDisabilityPremiumAwardByTT;
    private static String[] sHBPercentageOfHB_SevereDisabilityPremiumAwardByTT;
    private static String[] sCTBPercentageOfCTB_SevereDisabilityPremiumAwardByTT;
    // DisabledChildPremiumAwardByTT
    private static String[] sAllTotalCountDisabledChildPremiumAwardByTT;
    private static String[] sHBTotalCount_DisabledChildPremiumAwardByTT;
    private static String[] sCTBTotalCount_DisabledChildPremiumAwardByTT;
    private static String[] sAllPercentageOfAll_DisabledChildPremiumAwardByTT;
    private static String[] sHBPercentageOfHB_DisabledChildPremiumAwardByTT;
    private static String[] sCTBPercentageOfCTB_DisabledChildPremiumAwardByTT;
    // EnhancedDisabilityPremiumAwardByTT
    private static String[] sAllTotalCount_EnhancedDisabilityPremiumAwardByTT;
    private static String[] sHBTotalCount_EnhancedDisabilityPremiumAwardByTT;
    private static String[] sCTBTotalCount_EnhancedDisabilityPremiumAwardByTT;
    private static String[] sAllPercentageOfAll_EnhancedDisabilityPremiumAwardByTT;
    private static String[] sHBPercentageOfHB_EnhancedDisabilityPremiumAwardByTT;
    private static String[] sCTBPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT;
    // DisabilityAwards
    private static final String sTotalCount_DisabilityAward = "TotalCount_DisabilityAward";
    private static final String sPercentageOfAll_DisabilityAward = "PercentageOfAll_DisabilityAward";
    // DisabilityPremiumAwards
    private static final String sTotalCount_DisabilityPremiumAward = "TotalCount_DisabilityPremiumAward";
    private static final String sPercentageOfAll_DisabilityPremiumAward = "PercentageOfAll_DisabilityPremiumAward";
    // SevereDisabilityPremiumAwards
    private static final String sTotalCount_SevereDisabilityPremiumAward = "TotalCount_SevereDisabilityPremiumAward";
    private static final String sPercentageOfAll_SevereDisabilityPremiumAward = "PercentageOfAll_SevereDisabilityPremiumAward";
    // DisabledChildPremiumAwards
    private static final String sTotalCount_DisabledChildPremiumAward = "TotalCount_DisabledChildPremiumAward";
    private static final String sPercentageOfAll_DisabledChildPremiumAward = "PercentageOfAll_DisabledChildPremiumAward";
    // EnhancedDisabilityPremiumAwards
    private static final String sTotalCount_EnhancedDisabilityPremiumAward = "TotalCount_EnhancedDisabilityPremiumAward";
    private static final String sPercentageOfAll_EnhancedDisabilityPremiumAward = "PercentageOfAll_EnhancedDisabilityPremiumAward";
    // DisabilityPremiumAwardHBTTs
    private static final String sTotalCount_DisabilityPremiumAwardHBTTs = "TotalCount_DisabilityPremiumAwardHBTTs";
    private static final String sPercentageOfAll_DisabilityPremiumAwardHBTTs = "PercentageOfAll_DisabilityPremiumAwardHBTTs";
    private static final String sPercentageOfHB_DisabilityPremiumAwardHBTTs = "PercentageOfHB_DisabilityPremiumAwardHBTTs";
    // SevereDisabilityPremiumAwardHBTTs
    private static final String sTotalCount_SevereDisabilityPremiumAwardHBTTs = "TotalCount_SevereDisabilityPremiumAwardHBTTs";
    private static final String sPercentageOfAll_SevereDisabilityPremiumAwardHBTTs = "PercentageOfAll_SevereDisabilityPremiumAwardHBTTs";
    private static final String sPercentageOfHB_SevereDisabilityPremiumAwardHBTTs = "PercentageOfHB_SevereDisabilityPremiumAwardHBTTs";
    // DisabledChildPremiumAwardHBTTs
    private static final String sTotalCount_DisabledChildPremiumAwardHBTTs = "TotalCount_DisabledChildPremiumAwardHBTTs";
    private static final String sPercentageOfAll_DisabledChildPremiumAwardHBTTs = "PercentageOfAll_DisabledChildPremiumAwardHBTTs";
    private static final String sPercentageOfHB_DisabledChildPremiumAwardHBTTs = "PercentageOfHB_DisabledChildPremiumAwardHBTTs";
    // EnhancedDisabilityPremiumAwardHBTTs
    private static final String sTotalCount_EnhancedDisabilityPremiumAwardHBTTs = "TotalCount_EnhancedDisabilityPremiumAwardHBTTs";
    private static final String sPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs = "PercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs";
    private static final String sPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs = "PercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs";
    // DisabilityPremiumAwardCTBTTs
    private static final String sTotalCount_DisabilityPremiumAwardCTBTTs = "TotalCount_DisabilityPremiumAwardCTBTTs";
    private static final String sPercentageOfAll_DisabilityPremiumAwardCTBTTs = "PercentageOfAll_DisabilityPremiumAwardCTBTTs";
    private static final String sPercentageOfCTB_DisabilityPremiumAwardCTBTTs = "PercentageOfCTB_DisabilityPremiumAwardCTBTTs";
    // SevereDisabilityPremiumAwardCTBTTs
    private static final String sTotalCount_SevereDisabilityPremiumAwardCTBTTs = "TotalCount_SevereDisabilityPremiumAwardCTBTTs";
    private static final String sPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs = "PercentageOfAll_SevereDisabilityPremiumAwardCTBTTs";
    private static final String sPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs = "PercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs";
    // DisabledChildPremiumAwardCTBTTs
    private static final String sTotalCount_DisabledChildPremiumAwardCTBTTs = "TotalCount_DisabledChildPremiumAwardCTBTTs";
    private static final String sPercentageOfAll_DisabledChildPremiumAwardCTBTTs = "PercentageOfAll_DisabledChildPremiumAwardCTBTTs";
    private static final String sPercentageOfCTB_DisabledChildPremiumAwardCTBTTs = "PercentageOfCTB_DisabledChildPremiumAwardCTBTTs";
    // EnhancedDisabilityPremiumAwardCTBTTs
    private static final String sTotalCount_EnhancedDisabilityPremiumAwardCTBTTs = "TotalCount_EnhancedDisabilityPremiumAwardCTBTTs";
    private static final String sPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs = "PercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs";
    private static final String sPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs = "PercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs";
    // DisabilityPremiumAwardSocialTTs
    private static final String sTotalCount_DisabilityPremiumAwardSocialTTs = "TotalCount_DisabilityPremiumAwardSocialTTs";
    private static final String sPercentageOfAll_DisabilityPremiumAwardSocialTTs = "PrecentageOfAll_DisabilityPremiumAwardSocialTTs";
    private static final String sPercentageOfHB_DisabilityPremiumAwardSocialTTs = "PrecentageOfHB_DisabilityPremiumAwardSocialTTs";
    // DisabilityPremiumAwardPrivateDeregulatedTTs
    private static final String sTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs = "TotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs";
    private static final String sPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs = "sPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs";
    private static final String sPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs = "sPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs";
    // SevereDisabilityPremiumAwardSocialTTs
    private static final String sTotalCount_SevereDisabilityPremiumAwardSocialTTs = "TotalCount_SevereDisabilityPremiumAwardSocialTTs";
    private static final String sPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs = "PrecentageOfAll_SevereDisabilityPremiumAwardSocialTTs";
    private static final String sPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs = "PrecentageOfHB_SevereDisabilityPremiumAwardSocialTTs";
    // SevereDisabilityPremiumAwardPrivateDeregulatedTTs
    private static final String sTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "TotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    private static final String sPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "sPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    private static final String sPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "sPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    // DisabledChildPremiumAwardSocialTTs
    private static final String sTotalCount_DisabledChildPremiumAwardSocialTTs = "TotalCount_DisabledChildPremiumAwardSocialTTs";
    private static final String sPercentageOfAll_DisabledChildPremiumAwardSocialTTs = "PrecentageOfAll_DisabledChildPremiumAwardSocialTTs";
    private static final String sPercentageOfHB_DisabledChildPremiumAwardSocialTTs = "PrecentageOfHB_DisabledChildPremiumAwardSocialTTs";
    // DisabledChildPremiumAwardPrivateDeregulatedTTs
    private static final String sTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs = "TotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    private static final String sPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs = "sPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    private static final String sPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs = "sPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    // EnhancedDisabilityPremiumAwardSocialTTs
    private static final String sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs = "TotalCount_EnhancedDisabilityPremiumAwardSocialTTs";
    private static final String sPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs = "PrecentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs";
    private static final String sPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs = "PrecentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs";
    // EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs
    private static final String sTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "TotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    private static final String sPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "sPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    private static final String sPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "sPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";

    // DisabilityAwardByTT
    private static String[] sAllTotalCount_DisabilityAwardByTT;
    private static String[] sAllPercentageOfAll_DisabilityAwardByTT;
    private static String[] sHBTotalCount_DisabilityAwardByTT;
    private static String[] sHBPercentageOfHB_DisabilityAwardByTT;
    private static String[] sCTBTotalCount_DisabilityAwardByTT;
    private static String[] sCTBPercentageOfCTB_DisabilityAwardByTT;
    // DisabilityAwardHBTTs
    private static final String sTotalCount_DisabilityAwardHBTTs = "TotalCount_DisabilityAwardHBTTs";
    private static final String sPercentageOfAll_DisabilityAwardHBTTs = "PercentageOfAll_DisabilityAwardHBTTs";
    private static final String sPercentageOfHB_DisabilityAwardHBTTs = "PercentageOfHB_DisabilityAwardHBTTs";
    // DisabilityAwardCTBTTs
    private static final String sTotalCount_DisabilityAwardCTBTTs = "TotalCount_DisabilityAwardCTBTTs";
    private static final String sPercentageOfAll_DisabilityAwardCTBTTs = "PercentageOfAll_DisabilityAwardCTBTTs";
    private static final String sPercentageOfCTB_DisabilityAwardCTBTTs = "PercentageOfCTB_DisabilityAwardCTBTTs";
    // DisabilityAwardSocialTTs
    private static final String sTotalCount_DisabilityAwardSocialTTs = "TotalCount_DisabilityAwardSocialTTs";
    private static final String sPercentageOfAll_DisabilityAwardSocialTTs = "PrecentageOfAll_DisabilityAwardSocialTTs";
    private static final String sPercentageOfHB_DisabilityAwardSocialTTs = "PrecentageOfHB_DisabilityAwardSocialTTs";
    // DisabilityAwardPrivateDeregulatedTTs
    private static final String sTotalCount_DisabilityAwardPrivateDeregulatedTTs = "TotalCount_DisabilityAwardPrivateDeregulatedTTs";
    private static final String sPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs = "sPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs";
    private static final String sPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs = "sPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs";

    // HBEntitlement
    public static final String sTotalWeeklyHBEntitlement = "TotalWeeklyHBEntitlement";
    public static final String sTotalCountWeeklyHBEntitlementNonZero = "TotalCount_WeeklyHBEntitlementNonZero";
    public static final String sTotalCountWeeklyHBEntitlementZero = "TotalCount_WeeklyHBEntitlementZero";
    public static final String sAverageWeeklyHBEntitlement = "AverageWeeklyHBEntitlement";
    // CTBEntitlement
    public static final String sTotalWeeklyCTBEntitlement = "TotalWeeklyCTBEntitlement";
    public static final String sTotalCountWeeklyCTBEntitlementNonZero = "TotalCount_WeeklyCTBEntitlementNonZero";
    public static final String sTotalCountWeeklyCTBEntitlementZero = "TotalCount_WeeklyCTBEntitlementZero";
    public static final String sAverageWeeklyCTBEntitlement = "AverageWeeklyCTBEntitlement";
    // WeeklyEligibleRentAmount
    public static final String sAllTotalWeeklyEligibleRentAmount = "AllTotalWeeklyEligibleRentAmount";
    public static String[] sAllTotalWeeklyEligibleRentAmountTT;
    public static final String sAllTotalCountWeeklyEligibleRentAmountNonZero = "AllTotalCount_WeeklyEligibleRentAmountNonZero";
    public static String[] sAllTotalCountWeeklyEligibleRentAmountNonZeroTT;
    public static final String sAllTotalCountWeeklyEligibleRentAmountZero = "AllTotalCount_WeeklyEligibleRentAmountZero";
    public static String[] sAllTotalCountWeeklyEligibleRentAmountZeroTT;
    public static final String sAllAverageWeeklyEligibleRentAmount = "AllAverageWeeklyEligibleRentAmount";
    public static String[] sAllAverageWeeklyEligibleRentAmountTT;

    // WeeklyHBEntitlement
    public static final String sHBTotalWeeklyHBEntitlement = "HBTotalWeeklyHBEntitlement";
    public static final String sHBTotalCount_WeeklyHBEntitlementNonZero = "HBTotalCount_WeeklyHBEntitlementNonZero";
    public static final String sHBTotalCount_WeeklyHBEntitlementZero = "HBTotalCount_WeeklyHBEntitlementZero";
    public static final String sHBAverageWeeklyHBEntitlement = "HBAverageWeeklyHBEntitlement";
    public static final String sCTBTotalWeeklyHBEntitlement = "CTBTotalWeeklyHBEntitlement";
    public static final String sCTBTotalCount_WeeklyHBEntitlementNonZero = "CTBTotalCount_WeeklyHBEntitlementNonZero";
    public static final String sCTBTotalCountWeeklyHBEntitlementZero = "CTBTotalCount_WeeklyHBEntitlementZero";
    public static final String sCTBAverageWeeklyHBEntitlement = "CTBAverageWeeklyHBEntitlement";
    // WeeklyCTBEntitlement
    public static final String sHBTotalWeeklyCTBEntitlement = "HBTotalWeeklyCTBEntitlement";
    public static final String sHBTotalCount_WeeklyCTBEntitlementNonZero = "HBTotalCount_WeeklyCTBEntitlementNonZero";
    public static final String sHBTotalCount_WeeklyCTBEntitlementZero = "HBTotalCount_WeeklyCTBEntitlementZero";
    public static final String sHBAverageWeeklyCTBEntitlement = "HBAverageWeeklyCTBEntitlement";
    public static final String sCTBTotalWeeklyCTBEntitlement = "CTBTotalWeeklyCTBEntitlement";
    public static final String sCTBTotalCount_WeeklyCTBEntitlementNonZero = "CTBTotalCount_WeeklyCTBEntitlementNonZero";
    public static final String sCTBTotalCountWeeklyCTBEntitlementZero = "CTBTotalCount_WeeklyCTBEntitlementZero";
    public static final String sCTBAverageWeeklyCTBEntitlement = "CTBAverageWeeklyCTBEntitlement";
    // WeeklyEligibleRentAmount
    public static final String sHBTotalWeeklyEligibleRentAmount = "HBTotalWeeklyEligibleRentAmount";
    public static final String sHBTotalCountWeeklyEligibleRentAmountNonZero = "HBTotalCount_WeeklyEligibleRentAmountNonZero";
    public static final String sHBTotalCountWeeklyEligibleRentAmountZero = "HBTotalCount_WeeklyEligibleRentAmountZero";
    public static final String sHBAverageWeeklyEligibleRentAmount = "HBAverageWeeklyEligibleRentAmount";
    public static final String sCTBTotalWeeklyEligibleRentAmount = "CTBTotalWeeklyEligibleRentAmount";
    public static final String sCTBTotalCountWeeklyEligibleRentAmountNonZero = "CTBTotalCount_WeeklyEligibleRentAmountNonZero";
    public static final String sCTBTotalCountWeeklyEligibleRentAmountZero = "CTBTotalCount_WeeklyEligibleRentAmountZero";
    public static final String sCTBAverageWeeklyEligibleRentAmount = "CTBAverageWeeklyEligibleRentAmount";
    // WeeklyEligibleCouncilTaxAmount
    private static final String sAllTotalWeeklyEligibleCouncilTaxAmount = "AllTotalWeeklyEligibleCouncilTaxAmount";
    private static final String sAllTotalCountWeeklyEligibleCouncilTaxAmountNonZero = "AllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero";
    private static final String sAllTotalCountWeeklyEligibleCouncilTaxAmountZero = "AllTotalCount_WeeklyEligibleCouncilTaxAmountZero";
    private static final String sAllAverageWeeklyEligibleCouncilTaxAmount = "AllAverageWeeklyEligibleCouncilTaxAmount";
    private static final String sHBTotalWeeklyEligibleCouncilTaxAmount = "HBTotalCount_WeeklyEligibleCouncilTaxAmount";
    private static final String sHBTotalCountWeeklyEligibleCouncilTaxAmountNonZero = "HBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero";
    private static final String sHBTotalCountWeeklyEligibleCouncilTaxAmountZero = "HBTotalCount_WeeklyEligibleCouncilTaxAmountZero";
    private static final String sHBAverageWeeklyEligibleCouncilTaxAmount = "HBAverageWeeklyEligibleCouncilTaxAmount";
    private static final String sCTBTotalWeeklyEligibleCouncilTaxAmount = "CTBTotalWeeklyEligibleCouncilTaxAmount";
    private static final String sCTBTotalCountWeeklyEligibleCouncilTaxAmountNonZero = "CTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero";
    private static final String sCTBTotalCountWeeklyEligibleCouncilTaxAmountZero = "CTBTotalCount_WeeklyEligibleCouncilTaxAmountZero";
    private static final String sCTBAverageWeeklyEligibleCouncilTaxAmount = "CTBAverageWeeklyEligibleCouncilTaxAmount";
    // ContractualRentAmount
    private static final String sAllTotalContractualRentAmount = "AllTotalContractualRentAmount";
    private static final String sAllTotalCountContractualRentAmountNonZeroCount = "AllTotalCount_ContractualRentAmountNonZero";
    private static final String sAllTotalCountContractualRentAmountZeroCount = "AllTotalCount_ContractualRentAmountZero";
    private static final String sAllAverageContractualRentAmount = "AllAverageContractualRentAmount";
    private static final String sHBTotalContractualRentAmount = "HBTotalContractualRentAmount";
    private static final String sHBTotalCountContractualRentAmountNonZeroCount = "HBTotalCount_ContractualRentAmountNonZero";
    private static final String sHBTotalCountContractualRentAmountZeroCount = "HBTotalCount_ContractualRentAmountZero";
    private static final String sHBAverageContractualRentAmount = "HBAverageContractualRentAmount";
    private static final String sCTBTotalContractualRentAmount = "CTBTotalContractualRentAmount";
    private static final String sCTBTotalCountContractualRentAmountNonZeroCount = "CTBTotalCount_ContractualRentAmountNonZero";
    private static final String sCTBTotalCountContractualRentAmountZeroCount = "CTBTotalCount_ContractualRentAmountZero";
    private static final String sCTBAverageContractualRentAmount = "CTBAverageContractualRentAmount";
    // WeeklyAdditionalDiscretionaryPayment
    private static final String sAllTotalWeeklyAdditionalDiscretionaryPayment = "AllTotalWeeklyAdditionalDiscretionaryPayment";
    private static final String sAllTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero = "AllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero";
    private static final String sAllTotalCountWeeklyAdditionalDiscretionaryPaymentZero = "AllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero";
    private static final String sAllAverageWeeklyAdditionalDiscretionaryPayment = "AllAverageWeeklyAdditionalDiscretionaryPayment";
    private static final String sHBTotalWeeklyAdditionalDiscretionaryPayment = "HBTotalWeeklyAdditionalDiscretionaryPayment";
    private static final String sHBTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero = "HBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero";
    private static final String sHBTotalCountWeeklyAdditionalDiscretionaryPaymentZero = "HBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero";
    private static final String sHBAverageWeeklyAdditionalDiscretionaryPayment = "HBAverageWeeklyAdditionalDiscretionaryPayment";
    private static final String sCTBTotalWeeklyAdditionalDiscretionaryPayment = "CTBTotalWeeklyAdditionalDiscretionaryPayment";
    private static final String sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero = "CTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero";
    private static final String sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentZero = "CTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero";
    private static final String sCTBAverageWeeklyAdditionalDiscretionaryPayment = "CTBAverageWeeklyAdditionalDiscretionaryPayment";
    // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
    private static final String sAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    private static final String sAllTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "AllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    private static final String sAllTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "AllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    private static final String sAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "AllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    private static final String sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    private static final String sHBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "HBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    private static final String sHBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "HBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    private static final String sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "HBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    private static final String sCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    private static final String sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "CTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    private static final String sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "CTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    private static final String sCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "CTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    // Employment
    private static final String sAllTotalCount_ClaimantsEmployed = "AllTotalCount_ClaimantsEmployed";
    private static final String sAllPercentage_ClaimantsEmployed = "AllPercentage_ClaimantsEmployed";
    private static final String sAllTotalCount_ClaimantsSelfEmployed = "AllTotalCount_ClaimantsSelfEmployed";
    private static final String sAllPercentage_ClaimantsSelfEmployed = "AllPercentage_ClaimantsSelfEmployed";
    private static final String sAllTotalCount_ClaimantsStudents = "AllTotalCount_ClaimantsStudents";
    private static final String sAllPercentage_ClaimantsStudents = "AllPercentage_ClaimantsStudents";
    private static final String sAllTotalCount_LHACases = "AllTotalCount_LHACases";
    private static final String sAllPercentageOfAll_LHACases = "AllPercentageOfHB__LHACases";
    private static final String sHBTotalCountClaimantsEmployed = "HBTotalCount_ClaimantsEmployed";
    private static final String sHBPercentageOfHB_ClaimantsEmployed = "HBPercentageOfHB_ClaimantsEmployed";
    private static final String sHBTotalCountClaimantsSelfEmployed = "HBTotalCount_ClaimantsSelfEmployed";
    private static final String sHBPercentageOfHB_ClaimantsSelfEmployed = "HBPercentageOfHB_ClaimantsSelfEmployed";
    private static final String sHBTotalCountClaimantsStudents = "HBTotalCount_ClaimantsStudents";
    private static final String sHBPercentageOfHB_ClaimantsStudents = "HBPercentageOfHB_ClaimantsStudents";
    private static final String sHBTotalCount_LHACases = "HBTotalCount_LHACases";
    private static final String sHBPercentageOfHB_LHACases = "HBPercentageOfHB_LHACases";
    private static final String sCTBTotalCount_ClaimantsEmployed = "CTBTotalCount_ClaimantsEmployed";
    private static final String sCTBPercentageOfCTB_ClaimantsEmployed = "CTBPercentageOfCTB_ClaimantsEmployed";
    private static final String sCTBTotalCountClaimantsSelfEmployed = "CTBTotalCountClaimantsSelfEmployed";
    private static final String sCTBPercentageOfCTB_ClaimantsSelfEmployed = "CTBPercentageOfCTB_ClaimantsSelfEmployed";
    private static final String sCTBTotalCountClaimantsStudents = "CTBTotalCountClaimantsStudents";
    private static final String sCTBPercentageOfCTB_ClaimantsStudents = "CTBPercentageOfCTB_ClaimantsStudents";
    private static final String sCTBTotalCount_LHACases = "CTBTotalCountLHACases";
    private static final String sCTBPercentageOfCTB_LHACases = "CTBPercentageOfCTB_LHACases";

    private static final String sAllCount0 = "AllCount0";
    private static final String sHBCount0 = "HBCount0";
    private static final String sCTBCount0 = "CTBOnlyCount0";
    private static final String sAllCount1 = "AllCount1";
    private static final String sHBCount1 = "HBCount1";
    private static final String sCTBCount1 = "CTBOnlyCount1";

    private static final String sTotalCount_SocialTTsClaimant = "TotalCount_SocialTTsClaimant";
    private static final String sPercentageOfAll_SocialTTsClaimant = "PercentageOfAll_SocialTTsClaimant";
    private static final String sPercentageOfHB_SocialTTsClaimant = "PercentageOfHB_SocialTTsClaimant";
    private static final String sTotalCount_PrivateDeregulatedTTsClaimant = "TotalCount_PrivateDeregulatedTTsClaimant";
    private static final String sPercentageOfAll_PrivateDeregulatedTTsClaimant = "PercentageOfAll_PrivateDeregulatedTTsClaimant";
    private static final String sPercentageOfHB_PrivateDeregulatedTTsClaimant = "PercentageOfHB_PrivateDeregulatedTTsClaimant";

    private static String[] sAllTotalCountEthnicGroupClaimant;
    private static String[] sAllPercentageEthnicGroupClaimant;
    private static String[] sTotalCount_ClaimantsTT;
    private static String[] sPercentageOfAll_ClaimantsTT;
    private static String sAllPostcodeValidFormatCount;
    private static String sAllPostcodeValidCount;

    // Income
    // All
    public static final String sAllTotalIncome = "AllTotalIncome";
    public static String[] sAllTotalIncomeTT;
    public static final String sAllTotalCount_IncomeNonZero = "AllTotalCount_IncomeNonZero";
    public static final String sAllTotalCount_IncomeZero = "AllTotalCount_IncomeZero";
    public static String[] sAllTotalCountIncomeNonZeroTT;
    public static final String sAllTotalCountIncomeZero = "AllTotalCount_IncomeZero";
    public static String[] sAllTotalCountIncomeZeroTT;
    public static final String sAllAverageIncome = "AllAverageIncome";
    public static String[] sAllAverageIncomeTT;
    //HB
    public static final String sHBTotalIncome = "HBTotalIncome";
    public static final String sHBTotalCount_IncomeNonZero = "HBTotalCount_IncomeNonZero";
    public static final String sHBTotalCount_IncomeZero = "HBTotalCount_IncomeZero";
    public static final String sHBAverageIncome = "HBAverageIncome";
    // CTB
    public static final String sCTBTotalIncome = "CTBTotalIncome";
    public static final String sCTBTotalCount_IncomeNonZero = "CTBTotalCount_IncomeNonZero";
    public static final String sCTBTotalCount_IncomeZero = "CTBTotalCount_IncomeZero";
    public static final String sCTBAverageIncome = "CTBAverageIncome";

    private static final String sSHBEFilename00 = "SHBEFilename00";
    private static final String sSHBEFilename0 = "SHBEFilename0";
    private static final String sSHBEFilename1 = "SHBEFilename1";
    private static final String sCouncilFilename00 = "CouncilFilename00";
    private static final String sCouncilFilename0 = "CouncilFilename0";
    private static final String sCouncilFilename1 = "CouncilFilename1";
    private static final String sRSLFilename00 = "RSLFilename00";
    private static final String sRSLFilename0 = "RSLFilename0";
    private static final String sRSLFilename1 = "RSLFilename1";

    private static final String sAllUOCount00 = "AllUOCount00";
    private static final String sAllUOCount0 = "AllUOCount0";
    private static final String sAllUOCount1 = "AllUOCount1";
    private static final String sCouncilCount00 = "CouncilCount00";
    private static final String sCouncilCount0 = "CouncilCount0";
    private static final String sCouncilCount1 = "CouncilCount1";
    private static final String sRSLCount00 = "RSLCount00";
    private static final String sRSLCount0 = "RSLCount0";
    private static final String sRSLCount1 = "RSLCount1";
    private static final String sAllUOLinkedRecordCount00 = "AllLinkedRecordCount00";
    private static final String sAllUOLinkedRecordCount0 = "AllLinkedRecordCount0";
    private static final String sAllUOLinkedRecordCount1 = "AllLinkedRecordCount1";
    private static final String sCouncilLinkedRecordCount00 = "CouncilLinkedRecordCount00";
    private static final String sCouncilLinkedRecordCount0 = "CouncilLinkedRecordCount0";
    private static final String sCouncilLinkedRecordCount1 = "CouncilLinkedRecordCount1";
    private static final String sRSLLinkedRecordCount00 = "RSLLinkedRecordCount00";
    private static final String sRSLLinkedRecordCount0 = "RSLLinkedRecordCount0";
    private static final String sRSLLinkedRecordCount1 = "RSLLinkedRecordCount1";
    // Under Occupancy Only
//    private static final String councilCountString = "CouncilCount";
//    private static final String RSLCountString = "RSLCount";
    private static final String sTotal_RentArrears = "Total_RentArrears";
    private static final String sTotalCount_RentArrears = "TotalCount_RentArrears";
    private static final String sTotalCount_RentArrearsZero = "TotalCount_RentArrearsZero";
    private static final String sTotalCount_RentArrearsNonZero = "TotalCount_RentArrearsNonZero";
    private static final String sAverage_RentArrears = "Average_RentArrears";
    private static final String sAverage_NonZeroRentArrears = "AverageNonZero_RentArrears";
    // Demographics
    // Disability
    // HB
//    private static String HBFemaleClaimantCountString;
//    private static String HBMaleClaimantCountString;
//    private static String HBDisabledClaimantCountString;
//    private static String HBFemaleDisabledClaimantCountString;
//    private static String HBMaleDisabledClaimantCountString;
    // Ethnicity
    private static String[] sHBTotalCount_EthnicGroupClaimant;
    private static String[] sHBPercentage_EthnicGroupClaimant;

    private static String[] sTotalCountClaimantTT;
    private static String[] sPercentageOfAll_TTClaimant;
    private static String[] sPercentageOfHB_ClaimantTT;
    private static String[] sPercentageOfCTB_ClaimantTT;
    private static String sHBPostcodeValidFormatCount;
    private static String sHBPostcodeValidCount;
    // CTB
//    private static String CTBFemaleClaimantCountString;
//    private static String CTBMaleClaimantCountString;
//    private static String CTBDisabledClaimantCountString;
//    private static String CTBFemaleDisabledClaimantCountString;
//    private static String CTBMaleDisabledClaimantCountString;
    private static String[] sCTBTotalCountEthnicGroupClaimant;
    private static String[] sCTBPercentageEthnicGroupClaimant;
    private static String[] sCTBTotalCountTTClaimant;
    private static String[] sCTBPercentageOfAllTTClaimant;
    private static String sCTBPostcodeValidFormatCount;
    private static String sCTBPostcodeValidCount;

    // Compare 2 Times
    // All TT
    private static final String sAllTotalCount_TTChangeClaimant = "AllTotalCount_TTChangeClaimant";
    private static final String sAllPercentageOfAll_TTChangeClaimant = "AllPercentageOfAll_TTChangeClaimant";
    private static final String sTotalCount_HBTTsToCTBTTs = "TotalCount_HBTTsToCTBTTs";
    private static final String sPercentageOfHB_HBTTsToCTBTTs = "PercentageOfHB_HBTTsToCTBTTs";
    private static final String sTotalCount_CTBTTsToHBTTs = "TotalCount_CTBTTsToHBTTs";
    private static final String sPercentageOfCTB_CTBTTsToHBTTs = "PercentageOfCTB_CTBTTsToHBTTs";
    // HB TT
    private static final String sHBTotalCount_TTChangeClaimant = "HBTotalCount_TTChangeClaimant";
    private static final String sHBPercentageOfHB_TTChangeClaimant = "HBPercentageOfHB_TTChangeClaimant";
    private static final String sTotalCount_HBTTsToHBTTs = "TotalCount_HBTTsToHBTTs";
    private static final String sPercentageOfHB_HBTTsToHBTTs = "PercentageOfHB_HBTTsToHBTTs";
    private static final String sTotalCount_SocialTTsToPrivateDeregulatedTTs = "TotalCount_SocialTTsToPrivateDeregulatedTTs";
    private static final String sPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs = "PercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs";
    private static final String sTotalCount_PrivateDeregulatedTTsToSocialTTs = "TotalCount_PrivateDeregulatedTTsToSocialTTs";
    private static final String sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs = "PercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs";
    private static final String sTotalCount_TT1ToPrivateDeregulatedTTs = "TotalCount_TT1ToPrivateDeregulatedTTs";
    private static final String sPercentageOfTT1_TT1ToPrivateDeregulatedTTs = "PercentageOfTT1_TT1ToPrivateDeregulatedTTs";
    private static final String sTotalCount_TT4ToPrivateDeregulatedTTs = "TotalCount_TT4ToPrivateDeregulatedTTs";
    private static final String sPercentageOfTT4_TT4ToPrivateDeregulatedTTs = "PercentageOfTT4_TT4ToPrivateDeregulatedTTs";
    private static final String sTotalCount_PrivateDeregulatedTTsToTT1 = "TotalCount_PrivateDeregulatedTTsToTT1";
    private static final String sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1 = "PercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1";
    private static final String sTotalCount_PrivateDeregulatedTTsToTT4 = "TotalCount_PrivateDeregulatedTTsToTT4";
    private static final String sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4 = "PercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4";

    private static final String sTotalCount_TT1ToTT4 = "TotalCount_TT1ToTT4";
    private static final String sPercentageOfTT1_TT1ToTT4 = "PercentageOfTT1_TT1ToTT4";
    private static final String sTotalCount_TT4ToTT1 = "TotalCount_TT4ToTT1";
    private static final String sPercentageOfTT4_TT4ToTT1 = "PercentageOfTT4_TT4ToTT1";

    private static final String sTotalCount_PostcodeChangeWithinSocialTTs = "TotalCount_PostcodeChangeWithinSocialTTs";
    private static final String sPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs = "PercentageOfSocialTTs_PostcodeChangeWithinSocialTTs";
    private static final String sTotalCount_PostcodeChangeWithinTT1 = "TotalCount_PostcodeChangeWithinTT1";
    private static final String sPercentageOfTT1_PostcodeChangeWithinTT1 = "PercentageOfTT1_PostcodeChangeWithinTT1";
    private static final String sTotalCount_PostcodeChangeWithinTT4 = "TotalCountPostcodeChangeWithinTT4";
    private static final String sPercentageOfTT4_PostcodeChangeWithinTT4 = "PercentageOfTT4_PostcodeChangeWithinTT4";
    private static final String sTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs = "TotalCount_PostcodeChangeWithinPrivateDeregulatedTTs";
    private static final String sPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs = "PercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs";
    // CTB TT
    private static final String sCTBTotalCount_TTChangeClaimant = "CTBTotalCount_TTChangeClaimant";
    private static final String sCTBPercentageOfCTB_TTChangeClaimant = "CTBPercentageOfCTB_TTChangeClaimant";
    private static final String sTotalCount_SocialTTsToCTBTTs = "TotalCount_SocialTTsToCTBTTs";
    private static final String sPercentageOfSocialTTs_SocialTTsToCTBTTs = "PercentageOfSocialTTs_SocialTTsToCTBTTs";
    private static final String sTotalCount_TT1ToCTBTTs = "TotalCount_TT1ToCTBTTs";
    private static final String sPercentageOfTT1_TT1ToCTBTTs = "PercentageOfTT1_TT1ToCTBTTs";
    private static final String sTotalCount_TT4ToCTBTTs = "TotalCount_TT4ToCTBTTs";
    private static final String sPercentageOfTT4_TT4ToCTBTTs = "PercentageOfTT4_TT4ToCTBTTs";
    private static final String sTotalCount_PrivateDeregulatedTTsToCTBTTs = "TotalCount_PrivateDeregulatedTTsToCTBTTs";
    private static final String sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs = "PercentageOfPrivateDeregulated_PrivateDeregulatedTTsToCTBTTs";
    private static final String sTotalCountCTBTTsToSocialTTs = "TotalCount_CTBTTsToSocialTTs";
    private static final String sPercentageOfCTB_CTBTTsToSocialTTs = "PercentageOfCTB_CTBTTsToSocialTTs";
    private static final String sTotalCount_CTBTTsToTT1 = "TotalCount_CTBTTsToTT1";
    private static final String sPercentageOfCTB_CTBTTsToTT1 = "PercentageOfCTB_CTBTTsToTT1";
    private static final String sTotalCount_CTBTTsToTT4 = "TotalCount_CTBTTsToTT4";
    private static final String sPercentageOfCTB_CTBTTsToTT4 = "PercentageOfCTB_CTBTTsToTT4";
    private static final String sTotalCount_CTBTTsToPrivateDeregulatedTTs = "TotalCount_CTBTTsToPrivateDeregulatedTypes";
    private static final String sPercentageOfCTB_CTBTTsToPrivateDeregulatedTTs = "PercentageOfCTB_CTBTTsToPrivateDeregulatedTypes";
    // All Postcode
    private static final String sAllTotalCountPostcode0ValidPostcode1Valid = "AllTotalCountPostcode0ValidPostcode1Valid";
    private static final String sAllPercentagePostcode0ValidPostcode1Valid = "AllPercentagePostcode0ValidPostcode1Valid";
    private static final String sAllTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged = "AllTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged";
    private static final String sAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged = "AllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged";
    private static final String sAllTotalCountPostcode0ValidPostcode1ValidPostcodeChange = "AllTotalCountPostcode0ValidPostcode1ValidPostcodeChange";
    private static final String sAllPercentagePostcode0ValidPostcode1ValidPostcodeChange = "AllPercentagePostcode0ValidPostcode1ValidPostcodeChange";
    private static final String sAllTotalCountPostcode0ValidPostcode1NotValid = "AllTotalCountPostcode0ValidPostcode1NotValid";
    private static final String sAllPercentagePostcode0ValidPostcode1NotValid = "AllPercentagePostcode0ValidPostcode1NotValid";
    private static final String sAllTotalCountPostcode0NotValidPostcode1Valid = "AllTotalCountPostcode0NotValidPostcode1Valid";
    private static final String sAllPercentagePostcode0NotValidPostcode1Valid = "AllPercentagePostcode0NotValidPostcode1Valid";
    private static final String sAllTotalCountPostcode0NotValidPostcode1NotValid = "AllTotalCountPostcode0NotValidPostcode1NotValid";
    private static final String sAllPercentagePostcode0NotValidPostcode1NotValid = "AllPercentagePostcode0NotValidPostcode1NotValid";
    private static final String sAllTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged = "AllTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    private static final String sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged = "AllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    private static final String sAllTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged = "AllTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged";
    private static final String sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged = "AllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged";
    // HB Postcode
    private static final String sHBTotalCountPostcode0ValidPostcode1Valid = "HBTotalCountPostcode0ValidPostcode1Valid";
    private static final String sHBPercentagePostcode0ValidPostcode1Valid = "HBPercentagePostcode0ValidPostcode1Valid";
    private static final String sHBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged = "HBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged";
    private static final String sHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged = "HBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged";
    private static final String sHBTotalCountPostcode0ValidPostcode1ValidPostcodeChange = "HBTotalCountPostcode0ValidPostcode1ValidPostcodeChange";
    private static final String sHBPercentagePostcode0ValidPostcode1ValidPostcodeChange = "HBPercentagePostcode0ValidPostcode1ValidPostcodeChange";
    private static final String sHBTotalCountPostcode0ValidPostcode1NotValid = "HBTotalCountPostcode0ValidPostcode1NotValid";
    private static final String sHBPercentagePostcode0ValidPostcode1NotValid = "HBPercentagePostcode0ValidPostcode1NotValid";
    private static final String sHBTotalCountPostcode0NotValidPostcode1Valid = "HBTotalCountPostcode0NotValidPostcode1Valid";
    private static final String sHBPercentagePostcode0NotValidPostcode1Valid = "HBPercentagePostcode0NotValidPostcode1Valid";
    private static final String sHBTotalCountPostcode0NotValidPostcode1NotValid = "HBTotalCountPostcode0NotValidPostcode1NotValid";
    private static final String sHBPercentagePostcode0NotValidPostcode1NotValid = "HBPercentagePostcode0NotValidPostcode1NotValid";
    private static final String sHBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged = "HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    private static final String sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged = "HBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    private static final String sHBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged = "HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged";
    private static final String sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged = "HBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged";
    // CTB Postcode
    private static final String sCTBTotalCountPostcode0ValidPostcode1Valid = "CTBTotalCountPostcode0ValidPostcode1Valid";
    private static final String sCTBPercentageOfCTB_Postcode0ValidPostcode1Valid = "CTBPercentagePostcode0ValidPostcode1Valid";
    private static final String sCTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged = "CTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged";
    private static final String sCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged = "CTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged";
    private static final String sCTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged = "CTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged";
    private static final String sCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged = "CTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged";
    private static final String sCTBTotalCountPostcode0ValidPostcode1NotValid = "CTBTotalCountPostcode0ValidPostcode1NotValid";
    private static final String sCTBPercentagePostcode0ValidPostcode1NotValid = "CTBPercentagePostcode0ValidPostcode1NotValid";
    private static final String sCTBTotalCountPostcode0NotValidPostcode1Valid = "CTBTotalCountPostcode0NotValidPostcode1Valid";
    private static final String sCTBPercentagePostcode0NotValidPostcode1Valid = "CTBPercentagePostcode0NotValidPostcode1Valid";
    private static final String sCTBTotalCountPostcode0NotValidPostcode1NotValid = "CTBTotalCountPostcode0NotValidPostcode1NotValid";
    private static final String sCTBPercentagePostcode0NotValidPostcode1NotValid = "CTBPercentagePostcode0NotValidPostcode1NotValid";
    private static final String sCTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged = "CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    private static final String sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged = "CTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    private static final String sCTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged = "CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged";
    private static final String sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged = "CTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged";

    // Compare 3 Times
    // All
    private static String SamePostcodeIIIString;
    private static String SamePostcodeIOIString;
    private static String SamePostcodeOIOString;
    private static String SameTenancyIIIString;
    private static String SameTenancyIOIString;
    private static String SameTenancyOIOString;
    private static String SameTenancyAndPostcodeIIIString;
    private static String SameTenancyAndPostcodeIOIString;
    private static String SameTenancyAndPostcodeOIOString;
    private static String[] SameTenancyIIITTString;
    private static String[] SameTenancyIOITTString;
    private static String[] SameTenancyOIOTTString;
    private static String[] SameTenancyAndPostcodeIIITTString;
    private static String[] SameTenancyAndPostcodeIOITTString;
    private static String[] SameTenancyAndPostcodeOIOTTString;

    // Counters
    // Single Time
    // All
    private static double AllTotalWeeklyHBEntitlement;
    private static int AllTotalWeeklyHBEntitlementNonZeroCount;
    private static int AllTotalWeeklyHBEntitlementZeroCount;
    private static double AllTotalWeeklyCTBEntitlement;
    private static int AllTotalCount_WeeklyCTBEntitlementNonZero;
    private static int AllTotalWeeklyCTBEntitlementZeroCount;
    private static double AllTotalWeeklyEligibleRentAmount;
    private static int AllTotalWeeklyEligibleRentAmountNonZeroCount;
    private static int AllTotalWeeklyEligibleRentAmountZeroCount;
    private static double AllTotalWeeklyEligibleCouncilTaxAmount;
    private static int AllTotalCountWeeklyEligibleCouncilTaxAmountNonZero;
    private static int AllTotalCountWeeklyEligibleCouncilTaxAmountZero;
    private static double AllTotalContractualRentAmount;
    private static int AllTotalContractualRentAmountNonZeroCount;
    private static int AllTotalContractualRentAmountZeroCount;
    private static double AllTotalWeeklyAdditionalDiscretionaryPayment;
    private static int AllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    private static int AllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    private static double AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    private static int AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    private static int AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;
    // HB
    private static double HBTotalWeeklyHBEntitlement;
    private static int HBTotalCount_WeeklyHBEntitlementNonZero;
    private static int HBTotalCount_WeeklyHBEntitlementZero;
    private static double HBTotalWeeklyCTBEntitlement;
    private static int HBTotalCount_WeeklyCTBEntitlementNonZero;
    private static int HBTotalWeeklyCTBEntitlementZeroCount;
    private static double HBTotalWeeklyEligibleRentAmount;
    private static int HBTotalWeeklyEligibleRentAmountNonZeroCount;
    private static int HBTotalWeeklyEligibleRentAmountZeroCount;
    private static double HBTotalWeeklyEligibleCouncilTaxAmount;
    private static int HBTotalCountWeeklyEligibleCouncilTaxAmountNonZero;
    private static int HBTotalCountWeeklyEligibleCouncilTaxAmountZero;
    private static double HBTotalContractualRentAmount;
    private static int HBTotalContractualRentAmountNonZeroCount;
    private static int HBTotalContractualRentAmountZeroCount;
    private static double HBTotalWeeklyAdditionalDiscretionaryPayment;
    private static int HBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    private static int HBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    private static double HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    private static int HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    private static int HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;
    // CTB
    private static double CTBTotalWeeklyHBEntitlement;
    private static int CTBTotalCount_WeeklyHBEntitlementNonZero;
    private static int CTBTotalWeeklyHBEntitlementZeroCount;
    private static double CTBTotalWeeklyCTBEntitlement;
    private static int CTBTotalCount_WeeklyCTBEntitlementNonZero;
    private static int CTBTotalWeeklyCTBEntitlementZeroCount;
    private static double CTBTotalWeeklyEligibleRentAmount;
    private static int CTBTotalWeeklyEligibleRentAmountNonZeroCount;
    private static int CTBTotalWeeklyEligibleRentAmountZeroCount;
    private static double CTBTotalWeeklyEligibleCouncilTaxAmount;
    private static int CTBTotalCountWeeklyEligibleCouncilTaxAmountNonZero;
    private static int CTBTotalCountWeeklyEligibleCouncilTaxAmountZero;
    private static double CTBTotalContractualRentAmount;
    private static int CTBTotalContractualRentAmountNonZeroCount;
    private static int CTBTotalContractualRentAmountZeroCount;
    private static double CTBTotalWeeklyAdditionalDiscretionaryPayment;
    private static int CTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    private static int CTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    private static double CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    private static int CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    private static int CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;

    // Employment Education Training
    private static int HBTotalCount_EmployedClaimants;
    private static int HBTotalCountSelfEmployedClaimants;
    private static int HBTotalCountStudentsClaimants;
    private static int CTBTotalCountEmployedClaimants;
    private static int CTBTotalCountSelfEmployedClaimants;
    private static int CTBTotalCountStudentsClaimants;
    // HLA
    private static int HBTotalCountLHACases;
    private static int CTBTotalCount_LHACases;

    private static int[] TotalCount_DisabilityPremiumAwardByTT;
    private static int[] TotalCount_SevereDisabilityPremiumAwardByTT;
    private static int[] TotalCount_DisabledChildPremiumAwardByTT;
    private static int[] TotalCount_EnhancedDisabilityPremiumAwardByTT;
    private static int[] TotalCount_DisabilityAwardByTT;
    private static int[] AllTotalCount_PSI;
    private static int[] HBTotalCount_PSI;
    private static int[] CTBTotalCount_PSI;
    private static int[][] AllTotalCount_PSIByTT;
    private static int[][] HBTotalCount_PSIByTT;
    private static int[][] CTBTotalCount_PSIByTT;

    private static int[] TotalCount_TTClaimant1;
    private static int[] TotalCount_TTClaimant0;
    private static long AllTotalHouseholdSize;
    private static long HBTotalHouseholdSize;
    private static long CTBTotalHouseholdSize;
    private static int AllCount1;
    private static Integer AllCount0;
    // HB
    private static int HBCount1;
    private static Integer HBCount0;
    private static int[] HBEthnicGroupCount;
    private static int HBPostcodeValidFormatCount;
    private static int HBPostcodeValidCount;
    // CTB
    private static int CTBCount1;
    private static Integer CTBCount0;
    private static int[] CTBEthnicGroupCount;
    private static int CTBPostcodeValidFormatCount;
    private static int CTBPostcodeValidCount;
    // Other summary stats
    private static double Total_RentArrears;
    private static double TotalCount_RentArrears;
    private static int TotalCount_RentArrearsNonZero;
    private static int TotalCount_RentArrearsZero;

    // Compare 2 Times
    // General
    private static int TotalCount_HBTTsToCTBTTs;
    private static int TotalCount_CTBTTsToHBTTs;
    // General HB related
    private static int TotalCount_HBTTsToHBTTs;
    private static int TotalCount_SocialTTsToPrivateDeregulatedTTs;
    private static int TotalCount_PrivateDeregulatedTTsToSocialTTs;
    private static int TotalCount_TT1ToPrivateDeregulatedTTs;
    private static int TotalCount_TT4ToPrivateDeregulatedTTs;
    private static int TotalCount_PrivateDeregulatedTTsToTT1;
    private static int TotalCount_PrivateDeregulatedTTsToTT4;
    private static int TotalCount_PostcodeChangeWithinSocialTTs;
    private static int TotalCount_PostcodeChangeWithinTT1;
    private static int TotalCount_PostcodeChangeWithinTT4;
    private static int TotalCount_PostcodeChangeWithinPrivateDeregulatedTTs;
    // General CTB related
    private static int TotalCountSocialTTsToCTBTTs;
    private static int TotalCount_TT1ToCTBTTs;
    private static int TotalCount_TT4ToCTBTTs;
    private static int TotalCount_PrivateDeregulatedTTsToCTBTTs;
    private static int TotalCount_CTBTTsToSocialTTs;
    private static int TotalCount_CTBTTsToTT1;
    private static int TotalCount_CTBTTsToTT4;
    private static int TotalCount_CTBTTsToPrivateDeregulatedTTs;
    //
    private static int TotalCount_TT1ToTT4;
    private static int TotalCount_TT4ToTT1;
    // All
    private static int AllTotalCount_TTChangeClaimant;
    private static int AllTotalCountPostcode0ValidPostcode1Valid;
    private static int AllTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged;
    private static int AllTotalCountPostcode0ValidPostcode1ValidPostcodeChanged;
    private static int AllTotalCountPostcode0ValidPostcode1NotValid;
    private static int AllTotalCountPostcode0NotValidPostcode1Valid;
    private static int AllTotalCountPostcode0NotValidPostcode1NotValid;
    private static int AllTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged;
    private static int AllTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged;
    // HB
    private static int HBTotalCount_TTChangeClaimant;
    private static int HBTotalCountPostcode0ValidPostcode1Valid;
    private static int HBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged;
    private static int HBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged;
    private static int HBTotalCountPostcode0ValidPostcode1NotValid;
    private static int HBTotalCountPostcode0NotValidPostcode1Valid;
    private static int HBTotalCountPostcode0NotValidPostcode1NotValid;
    private static int HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged;
    private static int HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged;
    // CTB
    private static int CTBTotalCount_TTChangeClaimant;
    private static int CTBTotalCountPostcode0ValidPostcode1Valid;
    private static int CTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged;
    private static int CTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged;
    private static int CTBTotalCountPostcode0ValidPostcode1NotValid;
    private static int CTBTotalCountPostcode0NotValidPostcode1Valid;
    private static int CTBTotalCountPostcode0NotValidPostcode1NotValid;
    private static int CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged;
    private static int CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged;

    private static int AllUOCount00;
    private static int AllUOCount0;
    private static int AllUOCount1;
    private static int CouncilCount00;
    private static int CouncilCount0;
    private static int CouncilCount1;
    private static int RSLCount00;
    private static int RSLCount0;
    private static int RSLCount1;
    private static int AllLinkedRecordCount00;
    private static int AllUOLinkedRecordCount0;
    private static int AllUOLinkedRecordCount1;
    private static int CouncilLinkedRecordCount00;
    private static int CouncilLinkedRecordCount0;
    private static int CouncilLinkedRecordCount1;
    private static int RSLLinkedRecordCount00;
    private static int RSLLinkedRecordCount0;
    private static int RSLLinkedRecordCount1;

    // Compare 3 Times
    private static int SamePostcodeIII;
    private static int SamePostcodeIOI;
    private static int SamePostcodeOIO;
    private static int SameTenancyIII;
    private static int SameTenancyIOI;
    private static int SameTenancyOIO;
    private static int SameTenancyAndPostcodeIII;
    private static int SameTenancyAndPostcodeIOI;
    private static int SameTenancyAndPostcodeOIO;
    private static int[] SameTenancyIIITT;
    private static int[] SameTenancyIOITT;
    private static int[] SameTenancyOIOTT;
    private static int[] SameTenancyAndPostcodeIIITT;
    private static int[] SameTenancyAndPostcodeIOITT;
    private static int[] SameTenancyAndPostcodeOIOTT;

    public Summary(
            DW_Environment env,
            DW_SHBE_CollectionHandler collectionHandler,
            DW_SHBE_Handler tDW_SHBE_Handler,
            int nTT, int nEG, int nPSI,
            boolean handleOutOfMemoryError) {
        this.env = env;
        this.collectionHandler = collectionHandler;
        this.tDW_SHBE_Handler = tDW_SHBE_Handler;
        init(nTT, nEG, nPSI);
    }

    protected final void init(int nTT, int nEG, int nPSI) {
        initSingleTimeStrings(nTT, nEG, nPSI);
        initCompare3TimesStrings(nTT, nEG);
        TotalCount_DisabilityPremiumAwardByTT = new int[nTT];
        TotalCount_SevereDisabilityPremiumAwardByTT = new int[nTT];
        TotalCount_DisabledChildPremiumAwardByTT = new int[nTT];
        TotalCount_EnhancedDisabilityPremiumAwardByTT = new int[nTT];
        TotalCount_DisabilityAwardByTT = new int[nTT];
        AllTotalCount_PSI = new int[nPSI];
        HBTotalCount_PSI = new int[nPSI];
        CTBTotalCount_PSI = new int[nPSI];
        AllTotalCount_PSIByTT = new int[nPSI][nTT];
        HBTotalCount_PSIByTT = new int[nPSI][nTT];
        CTBTotalCount_PSIByTT = new int[nPSI][nTT];
        TotalCount_TTClaimant1 = new int[nTT];
        TotalCount_TTClaimant0 = new int[nTT];
        HBEthnicGroupCount = new int[nEG];
        CTBEthnicGroupCount = new int[nEG];
    }

    protected final void initSingleTimeStrings(int nTT, int nEG, int nPSI) {
        sAllTotalIncomeTT = new String[nTT];
        sAllTotalCountIncomeNonZeroTT = new String[nTT];
        sAllTotalCountIncomeZeroTT = new String[nTT];
        sAllAverageIncomeTT = new String[nTT];
        sAllTotalWeeklyEligibleRentAmountTT = new String[nTT];
        sAllTotalCountWeeklyEligibleRentAmountNonZeroTT = new String[nTT];
        sAllTotalCountWeeklyEligibleRentAmountZeroTT = new String[nTT];
        sAllAverageWeeklyEligibleRentAmountTT = new String[nTT];

        sAllTotalCount_PSI = new String[nPSI];
        sHBTotalCount_PSI = new String[nPSI];
        sCTBTotalCountPSI = new String[nPSI];
        sAllPercentageOfAll_PSI = new String[nPSI];
        sHBPercentageOfHB_PSI = new String[nPSI];
        sCTBPercentageOfCTB_PSI = new String[nPSI];
        sAllTotalCount_PSIByTT = new String[nPSI][nTT];
        sHBTotalCount_PSIByTT = new String[nPSI][nTT];
        sCTBTotalCount_PSIByTT = new String[nPSI][nTT];
        sAllPercentageOfAll_PSIByTT = new String[nPSI][nTT];
        sHBPercentageOfHB_PSIByTT = new String[nPSI][nTT];
        sCTBPercentageOfCTB_PSIByTT = new String[nPSI][nTT];
        for (int i = 1; i < nPSI; i++) {
            sAllTotalCount_PSI[i] = "TotalAllPSI" + i + "Count";
            sHBTotalCount_PSI[i] = "TotalHBPSI" + i + "Count";
            sCTBTotalCountPSI[i] = "TotalCTBPSI" + i + "Count";
            sAllPercentageOfAll_PSI[i] = "PercentageAllPSI" + i + "Count";
            sHBPercentageOfHB_PSI[i] = "PercentageHBPSI" + i + "Count";
            sCTBPercentageOfCTB_PSI[i] = "PercentageCTBPSI" + i + "Count";
            for (int j = 1; j < nTT; j++) {
                sAllTotalCount_PSIByTT[i][j] = "TotalCount_AllPSI" + i + "TT" + j;
                sHBTotalCount_PSIByTT[i][j] = "TotalCount_HBPSI" + i + "TT" + j;
                sCTBTotalCount_PSIByTT[i][j] = "TotalCount_CTBPSI" + i + "TT" + j;
                sAllPercentageOfAll_PSIByTT[i][j] = "PercentageOfAll_PSI" + i + "TT" + j;
                if (!(j == 5 || j == 7)) {
                    sHBPercentageOfHB_PSIByTT[i][j] = "PercentageOfHB_PSI" + i + "TT" + j;
                } else {
                    sCTBPercentageOfCTB_PSIByTT[i][j] = "PercentageOfCTB_PSI" + i + "TT" + j;
                }
            }
        }

        // All
        sAllTotalCountEthnicGroupClaimant = new String[nEG];
        sAllPercentageEthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            sAllTotalCountEthnicGroupClaimant[i] = "AllTotalCountEthnicGroup" + i + "Claimant";
            sAllPercentageEthnicGroupClaimant[i] = "AllPercentageEthnicGroup" + i + "Claimant";
        }
        sTotalCountClaimantTT = new String[nTT];
        sPercentageOfHB_ClaimantTT = new String[nTT];
        sPercentageOfCTB_ClaimantTT = new String[nTT];
        // DisabilityAward
        sAllTotalCount_DisabilityAwardByTT = new String[nTT];
        sHBTotalCount_DisabilityAwardByTT = new String[nTT];
        sCTBTotalCount_DisabilityAwardByTT = new String[nTT];
        sAllPercentageOfAll_DisabilityAwardByTT = new String[nTT];
        sHBPercentageOfHB_DisabilityAwardByTT = new String[nTT];
        sCTBPercentageOfCTB_DisabilityAwardByTT = new String[nTT];
        // DisabilityPremiumAward
        sAllTotalCount_DisabilityPremiumAwardByTT = new String[nTT];
        sHBTotalCount_DisabilityPremiumAwardByTT = new String[nTT];
        sCTBTotalCount_DisabilityPremiumAwardByTT = new String[nTT];
        sAllPercentageOfAll_DisabilityPremiumAwardByTT = new String[nTT];
        sHBPercentageOfHB_DisabilityPremiumAwardByTT = new String[nTT];
        sCTBPercentageOfCTB_DisabilityPremiumAwardByTT = new String[nTT];
        // SevereDisabilityPremiumAward
        sAllTotalCountSevereDisabilityPremiumAwardByTT = new String[nTT];
        sHBTotalCount_SevereDisabilityPremiumAwardByTT = new String[nTT];
        sCTBTotalCount_SevereDisabilityPremiumAwardByTT = new String[nTT];
        sAllPercentageOfAll_SevereDisabilityPremiumAwardByTT = new String[nTT];
        sHBPercentageOfHB_SevereDisabilityPremiumAwardByTT = new String[nTT];
        sCTBPercentageOfCTB_SevereDisabilityPremiumAwardByTT = new String[nTT];
        // DisabledChildPremiumAward
        sAllTotalCountDisabledChildPremiumAwardByTT = new String[nTT];
        sHBTotalCount_DisabledChildPremiumAwardByTT = new String[nTT];
        sCTBTotalCount_DisabledChildPremiumAwardByTT = new String[nTT];
        sAllPercentageOfAll_DisabledChildPremiumAwardByTT = new String[nTT];
        sHBPercentageOfHB_DisabledChildPremiumAwardByTT = new String[nTT];
        sCTBPercentageOfCTB_DisabledChildPremiumAwardByTT = new String[nTT];
        // EnhancedDisabilityPremiumAward
        sAllTotalCount_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sHBTotalCount_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sCTBTotalCount_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sAllPercentageOfAll_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sHBPercentageOfHB_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sCTBPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sTotalCount_ClaimantsTT = new String[nTT];
        sPercentageOfAll_ClaimantsTT = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            // Claimants
            sTotalCount_ClaimantsTT[i] = "TotalCount_ClaimantsTT" + i;
            sPercentageOfAll_ClaimantsTT[i] = "PercentageOfAll_ClaimantsTT" + i;
            sPercentageOfHB_ClaimantTT[i] = "PercentageOfHB_ClaimantTT" + i;
            sPercentageOfCTB_ClaimantTT[i] = "PercentageOfCTB_ClaimantTT" + i;
            // Income
            sAllTotalIncomeTT[i] = sAllTotalIncome + "TT" + i;
            sAllTotalCountIncomeNonZeroTT[i] = sAllTotalCount_IncomeNonZero + "TT" + i;
            sAllTotalCountIncomeZeroTT[i] = sAllTotalCountIncomeZero + "TT" + i;
            sAllAverageIncomeTT[i] = sAllAverageIncome + "TT" + i;
            // WeeklyEligibleRentAmountTT
            sAllTotalWeeklyEligibleRentAmountTT[i] = sAllTotalWeeklyEligibleRentAmount + "TT" + i;
            sAllTotalCountWeeklyEligibleRentAmountNonZeroTT[i] = sAllTotalCountWeeklyEligibleRentAmountNonZero + "TT" + i;
            sAllTotalCountWeeklyEligibleRentAmountZeroTT[i] = sAllTotalCountWeeklyEligibleRentAmountZero + "TT" + i;
            sAllAverageWeeklyEligibleRentAmountTT[i] = sAllAverageWeeklyEligibleRentAmount + "TT" + i;
            // DisabilityAwardByTT
            sAllTotalCount_DisabilityAwardByTT[i] = "AllTotalCount_DisabilityAwardByTT" + i;
            sHBTotalCount_DisabilityAwardByTT[i] = "HBTotalCount_DisabilityAwardByTT" + i;
            sCTBTotalCount_DisabilityAwardByTT[i] = "CTBTotalCount_DisabilityAwardByTT" + i;
            sAllPercentageOfAll_DisabilityAwardByTT[i] = "AllPercentageOfAll_DisabilityAwardByTT" + i;
            sHBPercentageOfHB_DisabilityAwardByTT[i] = "HBPercentageOfHB_DisabilityAwardByTT" + i;
            sCTBPercentageOfCTB_DisabilityAwardByTT[i] = "CTBPercentageOfCTB_DisabilityAwardByTT" + i;
            // DisabilityPremiumAwardByTT
            sAllTotalCount_DisabilityPremiumAwardByTT[i] = "AllTotalCount_DisabilityPremiumAwardByTT" + i;
            sHBTotalCount_DisabilityPremiumAwardByTT[i] = "HBTotalCount_DisabilityPremiumAwardByTT" + i;
            sCTBTotalCount_DisabilityPremiumAwardByTT[i] = "CTBTotalCount_DisabilityPremiumAwardByTT" + i;
            sAllPercentageOfAll_DisabilityPremiumAwardByTT[i] = "AllPercentageOfAll_DisabilityPremiumAwardByTT" + i;
            sHBPercentageOfHB_DisabilityPremiumAwardByTT[i] = "HBPercentageOfHB_DisabilityPremiumAwardByTT" + i;
            sCTBPercentageOfCTB_DisabilityPremiumAwardByTT[i] = "CTBPercentageOfCTB_DisabilityPremiumAwardByTT" + i;
            // SevereDisabilityPremiumAwardByTT
            sAllTotalCountSevereDisabilityPremiumAwardByTT[i] = "AllTotalCount_SevereDisabilityPremiumAwardByTT" + i;
            sHBTotalCount_SevereDisabilityPremiumAwardByTT[i] = "HBTotalCount_SevereDisabilityPremiumAwardByTT" + i;
            sCTBTotalCount_SevereDisabilityPremiumAwardByTT[i] = "CTBTotalCount_SevereDisabilityPremiumAwardByTT" + i;
            sAllPercentageOfAll_SevereDisabilityPremiumAwardByTT[i] = "AllPercentageOfAll_SevereDisabilityPremiumAwardByTT" + i;
            sHBPercentageOfHB_SevereDisabilityPremiumAwardByTT[i] = "HBPercentageOfHB_SevereDisabilityPremiumAwardByTT" + i;
            sCTBPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i] = "CTBPercentageOfCTB_SevereDisabilityPremiumAwardByTT" + i;
            // DisabledChildPremiumAwardByTT
            sAllTotalCountDisabledChildPremiumAwardByTT[i] = "AllTotalCount_DisabledChildPremiumAwardByTT" + i;
            sHBTotalCount_DisabledChildPremiumAwardByTT[i] = "HBTotalCount_DisabledChildPremiumAwardByTT" + i;
            sCTBTotalCount_DisabledChildPremiumAwardByTT[i] = "CTBTotalCount_DisabledChildPremiumAwardByTT" + i;
            sAllPercentageOfAll_DisabledChildPremiumAwardByTT[i] = "AllPercentageOfAll_DisabledChildPremiumAwardByTT" + i;
            sHBPercentageOfHB_DisabledChildPremiumAwardByTT[i] = "HBPercentageOfHB_DisabledChildPremiumAwardByTT" + i;
            sCTBPercentageOfCTB_DisabledChildPremiumAwardByTT[i] = "CTBPercentageOfCTB_DisabledChildPremiumAwardByTT" + i;
            // EnhancedDisabilityPremiumAwardByTT
            sAllTotalCount_EnhancedDisabilityPremiumAwardByTT[i] = "AllTotalCount_EnhancedDisabilityPremiumAwardByTT" + i;
            sHBTotalCount_EnhancedDisabilityPremiumAwardByTT[i] = "HBTotalCount_EnhancedDisabilityPremiumAwardByTT" + i;
            sCTBTotalCount_EnhancedDisabilityPremiumAwardByTT[i] = "CTBTotalCount_EnhancedDisabilityPremiumAwardByTT" + i;
            sAllPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i] = "AllPercentageOfAll_EnhancedDisabilityPremiumAwardByTT" + i;
            sHBPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i] = "HBPercentageOfHB_EnhancedDisabilityPremiumAwardByTT" + i;
            sCTBPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i] = "CTBPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT" + i;
        }
        sAllPostcodeValidFormatCount = "AllPostcodeValidFormatCount";
        sAllPostcodeValidCount = "AllPostcodeValidCount";

        // HB
        sHBTotalCount_EthnicGroupClaimant = new String[nEG];
        sHBPercentage_EthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            sHBTotalCount_EthnicGroupClaimant[i] = "HBTotalCountEthnicGroup" + i + "Claimant";
            sHBPercentage_EthnicGroupClaimant[i] = "HBPercentageEthnicGroup" + i + "Claimant";
        }

        sHBPostcodeValidFormatCount = "HBPostcodeValidFormatCount";
        sHBPostcodeValidCount = "HBPostcodeValidCount";
//        HBFemaleClaimantCountString = "HBFemaleClaimantCount";
//        HBDisabledClaimantCountString = "HBDisabledClaimantCount";
//        HBFemaleDisabledClaimantCountString = "HBDisabledFemaleClaimantCount";
//        HBMaleDisabledClaimantCountString = "HBDisabledMaleClaimantCount";

        // CTB
        sCTBTotalCountEthnicGroupClaimant = new String[nEG];
        sCTBPercentageEthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            sCTBTotalCountEthnicGroupClaimant[i] = "CTBTotalCountClaimantEthnicGroup" + i + "Claimant";
            sCTBPercentageEthnicGroupClaimant[i] = "CTBPercentageClaimantEthnicGroup" + i + "Claimant";
        }
        sCTBTotalCountTTClaimant = new String[nTT];
        sCTBPercentageOfAllTTClaimant = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            sCTBTotalCountTTClaimant[i] = "CTBTotalCountTT" + i + "Claimant";
            sCTBPercentageOfAllTTClaimant[i] = "CTBPercentageTT" + i + "Claimant";
        }
        sCTBPostcodeValidFormatCount = "CTBPostcodeValidFormatCount";
        sCTBPostcodeValidCount = "CTBPostcodeValidCount";
//        CTBFemaleClaimantCountString = "CTBFemaleClaimantCount";
//        CTBDisabledClaimantCountString = "CTBDisabledClaimantCount";
//        CTBFemaleDisabledClaimantCountString = "CTBDisabledFemaleClaimantCount";
//        CTBMaleDisabledClaimantCountString = "CTBDisabledMaleClaimantCount";

    }

    protected final void initCompare3TimesStrings(int nTT, int nEG) {
        SamePostcodeIIIString = "SamePostcodeIII";
        SamePostcodeIOIString = "SamePostcodeIOI";
        SamePostcodeOIOString = "SamePostcodeOIO";
        SameTenancyIIIString = "SameTenancyIII";
        SameTenancyIOIString = "SameTenancyIOI";
        SameTenancyOIOString = "SameTenancyOIO";
        SameTenancyAndPostcodeIIIString = "SameTenancyAndPostcodeIII";
        SameTenancyAndPostcodeIOIString = "SameTenancyAndPostcodeIOI";
        SameTenancyAndPostcodeOIOString = "SameTenancyAndPostcodeOIO";
        SameTenancyIIITTString = new String[nTT];
        SameTenancyIOITTString = new String[nTT];
        SameTenancyOIOTTString = new String[nTT];
        SameTenancyAndPostcodeIIITTString = new String[nTT];
        SameTenancyAndPostcodeIOITTString = new String[nTT];
        SameTenancyAndPostcodeOIOTTString = new String[nTT];
        for (int i = 0; i < nTT; i++) {
            SameTenancyIIITTString[i] = "SameTenancyIIITT" + i;
            SameTenancyIOITTString[i] = "SameTenancyIOITT" + i;
            SameTenancyOIOTTString[i] = "SameTenancyOIOTT" + i;
            SameTenancyAndPostcodeIIITTString[i] = "SameTenancyAndPostcodeIIITT" + i;
            SameTenancyAndPostcodeIOITTString[i] = "SameTenancyAndPostcodeIOITT" + i;
            SameTenancyAndPostcodeOIOTTString[i] = "SameTenancyAndPostcodeOIOTT" + i;
        }
    }

    private void initCounts(int nTT, int nEG, int nPSI) {
        initSingleTimeCounts(nTT, nEG, nPSI);
        initCompare2TimesCounts();
        initCompare3TimesCounts(nTT);
    }

    private void initSingleTimeCounts(int nTT, int nEG, int nPSI) {

        for (int i = 1; i < nPSI; i++) {
            AllTotalCount_PSI[i] = 0;
            HBTotalCount_PSI[i] = 0;
            CTBTotalCount_PSI[i] = 0;
            for (int j = 1; j < nTT; j++) {
                AllTotalCount_PSIByTT[i][j] = 0;
                HBTotalCount_PSIByTT[i][j] = 0;
                CTBTotalCount_PSIByTT[i][j] = 0;
            }
        }

        // All
        AllTotalWeeklyHBEntitlement = 0.0d;
        AllTotalWeeklyHBEntitlementNonZeroCount = 0;
        AllTotalWeeklyHBEntitlementZeroCount = 0;
        AllTotalWeeklyCTBEntitlement = 0.0d;
        AllTotalCount_WeeklyCTBEntitlementNonZero = 0;
        AllTotalWeeklyCTBEntitlementZeroCount = 0;
        AllTotalWeeklyEligibleRentAmount = 0.0d;
        AllTotalWeeklyEligibleRentAmountNonZeroCount = 0;
        AllTotalWeeklyEligibleRentAmountZeroCount = 0;
        AllTotalWeeklyEligibleCouncilTaxAmount = 0.0d;
        AllTotalCountWeeklyEligibleCouncilTaxAmountNonZero = 0;
        AllTotalCountWeeklyEligibleCouncilTaxAmountZero = 0;
        AllTotalContractualRentAmount = 0.0d;
        AllTotalContractualRentAmountNonZeroCount = 0;
        AllTotalContractualRentAmountZeroCount = 0;
        AllTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        AllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        AllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;

        HBTotalWeeklyHBEntitlement = 0.0d;
        HBTotalCount_WeeklyHBEntitlementNonZero = 0;
        HBTotalCount_WeeklyHBEntitlementZero = 0;
        HBTotalWeeklyCTBEntitlement = 0.0d;
        HBTotalCount_WeeklyCTBEntitlementNonZero = 0;
        HBTotalWeeklyCTBEntitlementZeroCount = 0;
        HBTotalWeeklyEligibleRentAmount = 0.0d;
        HBTotalWeeklyEligibleRentAmountNonZeroCount = 0;
        HBTotalWeeklyEligibleRentAmountZeroCount = 0;
        HBTotalWeeklyEligibleCouncilTaxAmount = 0.0d;
        HBTotalCountWeeklyEligibleCouncilTaxAmountNonZero = 0;
        HBTotalCountWeeklyEligibleCouncilTaxAmountZero = 0;
        HBTotalContractualRentAmount = 0.0d;
        HBTotalContractualRentAmountNonZeroCount = 0;
        HBTotalContractualRentAmountZeroCount = 0;
        HBTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        HBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        HBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;

        CTBTotalWeeklyHBEntitlement = 0.0d;
        CTBTotalCount_WeeklyHBEntitlementNonZero = 0;
        CTBTotalWeeklyHBEntitlementZeroCount = 0;
        CTBTotalWeeklyCTBEntitlement = 0.0d;
        CTBTotalCount_WeeklyCTBEntitlementNonZero = 0;
        CTBTotalWeeklyCTBEntitlementZeroCount = 0;
        CTBTotalWeeklyEligibleRentAmount = 0.0d;
        CTBTotalWeeklyEligibleRentAmountNonZeroCount = 0;
        CTBTotalWeeklyEligibleRentAmountZeroCount = 0;
        CTBTotalWeeklyEligibleCouncilTaxAmount = 0.0d;
        CTBTotalCountWeeklyEligibleCouncilTaxAmountNonZero = 0;
        CTBTotalCountWeeklyEligibleCouncilTaxAmountZero = 0;
        CTBTotalContractualRentAmount = 0.0d;
        CTBTotalContractualRentAmountNonZeroCount = 0;
        CTBTotalContractualRentAmountZeroCount = 0;
        CTBTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        CTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        CTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;

        HBTotalCount_EmployedClaimants = 0;
        CTBTotalCountEmployedClaimants = 0;
        HBTotalCountSelfEmployedClaimants = 0;
        CTBTotalCountSelfEmployedClaimants = 0;
        HBTotalCountStudentsClaimants = 0;
        CTBTotalCountStudentsClaimants = 0;
        HBTotalCountLHACases = 0;
        CTBTotalCount_LHACases = 0;
        for (int i = 1; i < nTT; i++) {
            TotalCount_TTClaimant1[i] = 0;
            TotalCount_DisabilityPremiumAwardByTT[i] = 0;
            TotalCount_SevereDisabilityPremiumAwardByTT[i] = 0;
            TotalCount_DisabledChildPremiumAwardByTT[i] = 0;
            TotalCount_EnhancedDisabilityPremiumAwardByTT[i] = 0;
            TotalCount_DisabilityAwardByTT[i] = 0;
        }
        // All
        //AllCount0 = AllCount1;
        AllCount1 = 0;

        // HB
        for (int i = 1; i < nEG; i++) {
            HBEthnicGroupCount[i] = 0;
        }
        //HBCount0 = HBCount1;
        HBCount1 = 0;
        HBPostcodeValidFormatCount = 0;
        HBPostcodeValidCount = 0;

        AllTotalHouseholdSize = 0L;
        HBTotalHouseholdSize = 0L;
        CTBTotalHouseholdSize = 0L;

        // CTB
        for (int i = 1; i < nEG; i++) {
            CTBEthnicGroupCount[i] = 0;
        }
        //CTBCount0 = CTBCount1;
        CTBCount1 = 0;
        CTBPostcodeValidFormatCount = 0;
        CTBPostcodeValidCount = 0;

        AllUOCount1 = 0;
        CouncilCount1 = 0;
        RSLCount1 = 0;
        AllUOLinkedRecordCount1 = 0;
        CouncilLinkedRecordCount1 = 0;
        RSLLinkedRecordCount1 = 0;
        // RentArrears
        Total_RentArrears = 0.0d;
        TotalCount_RentArrears = 0;
        TotalCount_RentArrearsZero = 0;
        TotalCount_RentArrearsNonZero = 0;
    }

    private void initCompare2TimesCounts() {
        // Compare 2 Times
        // General
        TotalCount_HBTTsToCTBTTs = 0;
        TotalCount_CTBTTsToHBTTs = 0;
        // General HB related
        TotalCount_HBTTsToHBTTs = 0;
        TotalCount_SocialTTsToPrivateDeregulatedTTs = 0;
        TotalCount_PrivateDeregulatedTTsToSocialTTs = 0;
        TotalCount_TT1ToPrivateDeregulatedTTs = 0;
        TotalCount_TT4ToPrivateDeregulatedTTs = 0;
        TotalCount_PrivateDeregulatedTTsToTT1 = 0;
        TotalCount_PrivateDeregulatedTTsToTT4 = 0;
        TotalCount_PostcodeChangeWithinSocialTTs = 0;
        TotalCount_PostcodeChangeWithinTT1 = 0;
        TotalCount_PostcodeChangeWithinTT4 = 0;
        TotalCount_PostcodeChangeWithinPrivateDeregulatedTTs = 0;
        // General CTB related
        TotalCountSocialTTsToCTBTTs = 0;
        TotalCount_TT1ToCTBTTs = 0;
        TotalCount_TT4ToCTBTTs = 0;
        TotalCount_PrivateDeregulatedTTsToCTBTTs = 0;
        TotalCount_CTBTTsToSocialTTs = 0;
        TotalCount_CTBTTsToTT1 = 0;
        TotalCount_CTBTTsToTT4 = 0;
        TotalCount_CTBTTsToPrivateDeregulatedTTs = 0;
        //
        TotalCount_TT1ToTT4 = 0;
        TotalCount_TT4ToTT1 = 0;
        // All
        AllTotalCount_TTChangeClaimant = 0;
        AllTotalCountPostcode0ValidPostcode1Valid = 0;
        AllTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        AllTotalCountPostcode0ValidPostcode1ValidPostcodeChanged = 0;
        AllTotalCountPostcode0ValidPostcode1NotValid = 0;
        AllTotalCountPostcode0NotValidPostcode1Valid = 0;
        AllTotalCountPostcode0NotValidPostcode1NotValid = 0;
        AllTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        AllTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged = 0;
        // HB
        HBTotalCount_TTChangeClaimant = 0;
        HBTotalCountPostcode0ValidPostcode1Valid = 0;
        HBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        HBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged = 0;
        HBTotalCountPostcode0ValidPostcode1NotValid = 0;
        HBTotalCountPostcode0NotValidPostcode1Valid = 0;
        HBTotalCountPostcode0NotValidPostcode1NotValid = 0;
        HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged = 0;
        // CTB
        CTBTotalCount_TTChangeClaimant = 0;
        CTBTotalCountPostcode0ValidPostcode1Valid = 0;
        CTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        CTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged = 0;
        CTBTotalCountPostcode0ValidPostcode1NotValid = 0;
        CTBTotalCountPostcode0NotValidPostcode1Valid = 0;
        CTBTotalCountPostcode0NotValidPostcode1NotValid = 0;
        CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged = 0;
    }

    private void initCompare3TimesCounts(int nTT) {
        SamePostcodeIII = 0;
        SamePostcodeIOI = 0;
        SamePostcodeOIO = 0;
        SameTenancyIII = 0;
        SameTenancyIOI = 0;
        SameTenancyOIO = 0;
        SameTenancyAndPostcodeIII = 0;
        SameTenancyAndPostcodeIOI = 0;
        SameTenancyAndPostcodeOIO = 0;
        SameTenancyIIITT = new int[nTT];
        SameTenancyIOITT = new int[nTT];
        SameTenancyOIOTT = new int[nTT];
        SameTenancyAndPostcodeIIITT = new int[nTT];
        SameTenancyAndPostcodeIOITT = new int[nTT];
        SameTenancyAndPostcodeOIOTT = new int[nTT];
        for (int i = 0; i < nTT; i++) {
            SameTenancyIIITT[i] = 0;
            SameTenancyIOITT[i] = 0;
            SameTenancyOIOTT[i] = 0;
            SameTenancyAndPostcodeIIITT[i] = 0;
            SameTenancyAndPostcodeIOITT[i] = 0;
            SameTenancyAndPostcodeOIOTT[i] = 0;
        }
    }

    public static Object[] getMemberships(
            HashSet set00,
            HashSet set0,
            HashSet set1) {
        Object[] result;
        result = new Object[3];
        HashSet union;
        union = new HashSet();
        union.addAll(set1);
        union.retainAll(set0);
        union.retainAll(set00);
        HashSet set0Only;
        set0Only = new HashSet();
        set0Only.addAll(set0);
        set0Only.removeAll(set1);
        set0Only.removeAll(set00);
        HashSet set00AndSet1;
        set00AndSet1 = new HashSet();
        set00AndSet1.addAll(set00);
        set00AndSet1.removeAll(set0);
        set00AndSet1.retainAll(set1);
        result[0] = union;
        result[1] = set0Only;
        result[2] = set00AndSet1;
        return result;
    }

    public static HashSet<ID_TenancyType> getID_TenancyTypeSet(
            TreeMap<String, DW_SHBE_Record> D_Records,
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet,
            DW_UnderOccupiedReport_Set RSLUnderOccupiedSet,
            HashMap<DW_PersonID, DW_ID> DW_PersonIDToIDLookup) {
        HashSet<ID_TenancyType> result;
        result = new HashSet<ID_TenancyType>();
        TreeMap<String, DW_UOReport_Record> RSLMap;
        RSLMap = RSLUnderOccupiedSet.getMap();
        TreeMap<String, DW_UOReport_Record> councilMap;
        councilMap = councilUnderOccupiedSet.getMap();
        Iterator<String> ite;
        ite = councilMap.keySet().iterator();
        while (ite.hasNext()) {
            String CTBRef;
            CTBRef = ite.next();
            if (D_Records.containsKey(CTBRef)) {
                DW_SHBE_D_Record D_Record;
                D_Record = D_Records.get(CTBRef).getDRecord();
                DW_PersonID DW_PersonID;
                DW_PersonID = DW_SHBE_Handler.getClaimantDW_PersonID(D_Record);
                DW_ID ID;
                ID = DW_PersonIDToIDLookup.get(DW_PersonID);
                int TT;
                TT = D_Record.getTenancyType();
                ID_TenancyType ID_TenancyType;
                ID_TenancyType = new ID_TenancyType(ID, TT);
                result.add(ID_TenancyType);
//            } else {
//                // There are a few cases where a mapping to the SHBE does not exist!
//                int debug = 1;//?
            }
        }
        ite = RSLMap.keySet().iterator();
        while (ite.hasNext()) {
            String CTBRef;
            CTBRef = ite.next();
            if (D_Records.containsKey(CTBRef)) {
                DW_SHBE_D_Record D_Record;
                D_Record = D_Records.get(CTBRef).getDRecord();
                DW_PersonID DW_PersonID;
                DW_PersonID = DW_SHBE_Handler.getClaimantDW_PersonID(D_Record);
                DW_ID ID;
                ID = DW_PersonIDToIDLookup.get(DW_PersonID);
                int TT;
                TT = D_Record.getTenancyType();
                ID_TenancyType ID_TenancyType;
                ID_TenancyType = new ID_TenancyType(ID, TT);
                result.add(ID_TenancyType);
//            } else {
//                // There are a few cases where a mapping to the SHBE does not exist!
//                int debug = 1;//?
            }
        }
        return result;
    }

    public static HashSet<ID_PostcodeID> getID_PostcodeIDSet(
            TreeMap<String, DW_SHBE_Record> D_Records,
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet,
            DW_UnderOccupiedReport_Set RSLUnderOccupiedSet,
            HashMap<DW_PersonID, DW_ID> DW_PersonIDToIDLookup,
            HashMap<String, DW_ID> PostcodeToPostcodeIDLookup) {
        HashSet<ID_PostcodeID> result;
        result = new HashSet<ID_PostcodeID>();
        TreeMap<String, DW_UOReport_Record> RSLMap;
        RSLMap = RSLUnderOccupiedSet.getMap();
        TreeMap<String, DW_UOReport_Record> councilMap;
        councilMap = councilUnderOccupiedSet.getMap();
        Iterator<String> ite;
        ite = councilMap.keySet().iterator();
        while (ite.hasNext()) {
            String CTBRef;
            CTBRef = ite.next();
            DW_SHBE_D_Record D_Record;
            if (D_Records.containsKey(CTBRef)) {
                D_Record = D_Records.get(CTBRef).getDRecord();
                DW_PersonID DW_PersonID;
                DW_PersonID = DW_SHBE_Handler.getClaimantDW_PersonID(D_Record);
                DW_ID ID;
                ID = DW_PersonIDToIDLookup.get(DW_PersonID);
                DW_ID PostcodeID;
                PostcodeID = PostcodeToPostcodeIDLookup.get(D_Record.getClaimantsPostcode());
                ID_PostcodeID ID_PostcodeID;
                ID_PostcodeID = new ID_PostcodeID(ID, PostcodeID);
                result.add(ID_PostcodeID);
//            } else {
//                // There are a few cases where a mapping to the SHBE does not exist!
//                int debug = 1;//?
            }
        }
        ite = RSLMap.keySet().iterator();
        while (ite.hasNext()) {
            String CTBRef;
            CTBRef = ite.next();
            DW_SHBE_D_Record D_Record;
            if (D_Records.containsKey(CTBRef)) {
                D_Record = D_Records.get(CTBRef).getDRecord();
                DW_PersonID DW_PersonID;
                DW_PersonID = DW_SHBE_Handler.getClaimantDW_PersonID(D_Record);
                DW_ID ID;
                ID = DW_PersonIDToIDLookup.get(DW_PersonID);
                DW_ID PostcodeID;
                PostcodeID = PostcodeToPostcodeIDLookup.get(D_Record.getClaimantsPostcode());
                ID_PostcodeID ID_PostcodeID;
                ID_PostcodeID = new ID_PostcodeID(ID, PostcodeID);
                result.add(ID_PostcodeID);
//            } else {
//                // There are a few cases where a mapping to the SHBE does not exist!
//                int debug = 1;//?
            }
        }
        return result;
    }

    public static HashSet<ID_TenancyType_PostcodeID> getID_TenancyType_PostcodeIDSet(
            TreeMap<String, DW_SHBE_Record> D_Records,
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet,
            DW_UnderOccupiedReport_Set RSLUnderOccupiedSet,
            HashMap<DW_PersonID, DW_ID> DW_PersonIDToIDLookup,
            HashMap<String, DW_ID> PostcodeToPostcodeIDLookup) {
        HashSet<ID_TenancyType_PostcodeID> result;
        result = new HashSet<ID_TenancyType_PostcodeID>();
        TreeMap<String, DW_UOReport_Record> RSLMap;
        RSLMap = RSLUnderOccupiedSet.getMap();
        TreeMap<String, DW_UOReport_Record> councilMap;
        councilMap = councilUnderOccupiedSet.getMap();
        Iterator<String> ite;
        ite = councilMap.keySet().iterator();
        while (ite.hasNext()) {
            String CTBRef;
            CTBRef = ite.next();
            if (D_Records.containsKey(CTBRef)) {
                DW_SHBE_D_Record D_Record;
                D_Record = D_Records.get(CTBRef).getDRecord();
                DW_PersonID DW_PersonID;
                DW_PersonID = DW_SHBE_Handler.getClaimantDW_PersonID(D_Record);
                DW_ID ID;
                ID = DW_PersonIDToIDLookup.get(DW_PersonID);
                int TT;
                TT = D_Record.getTenancyType();
                ID_TenancyType ID_TenancyType;
                ID_TenancyType = new ID_TenancyType(ID, TT);
                DW_ID PostcodeID;
                PostcodeID = PostcodeToPostcodeIDLookup.get(D_Record.getClaimantsPostcode());
                ID_TenancyType_PostcodeID ID_TenancyType_PostcodeID;
                ID_TenancyType_PostcodeID = new ID_TenancyType_PostcodeID(ID_TenancyType, PostcodeID);
                result.add(ID_TenancyType_PostcodeID);
//            } else {
//                // There are a few cases where a mapping to the SHBE does not exist!
//                int debug = 1;//?
            }
        }
        ite = RSLMap.keySet().iterator();
        while (ite.hasNext()) {
            String CTBRef;
            CTBRef = ite.next();
            if (D_Records.containsKey(CTBRef)) {
                DW_SHBE_D_Record D_Record;
                D_Record = D_Records.get(CTBRef).getDRecord();
                DW_PersonID DW_PersonID;
                DW_PersonID = DW_SHBE_Handler.getClaimantDW_PersonID(D_Record);
                DW_ID ID;
                ID = DW_PersonIDToIDLookup.get(DW_PersonID);
                int TT;
                TT = D_Record.getTenancyType();
                ID_TenancyType ID_TenancyType;
                ID_TenancyType = new ID_TenancyType(ID, TT);
                DW_ID PostcodeID;
                PostcodeID = PostcodeToPostcodeIDLookup.get(D_Record.getClaimantsPostcode());
                ID_TenancyType_PostcodeID ID_TenancyType_PostcodeID;
                ID_TenancyType_PostcodeID = new ID_TenancyType_PostcodeID(ID_TenancyType, PostcodeID);
                result.add(ID_TenancyType_PostcodeID);
//            } else {
//                // There are a few cases where a mapping to the SHBE does not exist!
//                int debug = 1;//?
            }
        }
        return result;
    }

//    public static Object[] getMembershipsID_TenancyType(HashSet<ID_TenancyType> set00, HashSet<ID_TenancyType> set0, HashSet<ID_TenancyType> set1) {
//        Object[] result;
//        result = new Object[3];
//        HashSet<ID_TenancyType> union;
//        union = new HashSet<ID_TenancyType>();
//        union.addAll(set1);
//        union.retainAll(set0);
//        union.retainAll(set00);
//        HashSet<ID_TenancyType> set0Only;
//        set0Only = new HashSet<ID_TenancyType>();
//        set0Only.addAll(set0);
//        set0Only.removeAll(set1);
//        set0Only.removeAll(set00);
//        HashSet<ID_TenancyType> set00AndSet1;
//        set00AndSet1 = new HashSet<ID_TenancyType>();
//        set00AndSet1.addAll(set00);
//        set00AndSet1.removeAll(set0);
//        set00AndSet1.retainAll(set1);
//        result[0] = union;
//        result[1] = set0Only;
//        result[2] = set00AndSet1;
//        return result;
//    }
//
//    public static Object[] getMembershipsID_TenancyType_PostcodeID(HashSet<ID_TenancyType_PostcodeID> set00, HashSet<ID_TenancyType_PostcodeID> set0, HashSet<ID_TenancyType_PostcodeID> set1) {
//        Object[] result;
//        result = new Object[3];
//        HashSet<ID_TenancyType_PostcodeID> union;
//        union = new HashSet<ID_TenancyType_PostcodeID>();
//        union.addAll(set1);
//        union.retainAll(set0);
//        union.retainAll(set00);
//        HashSet<ID_TenancyType_PostcodeID> set0Only;
//        set0Only = new HashSet<ID_TenancyType_PostcodeID>();
//        set0Only.addAll(set0);
//        set0Only.removeAll(set1);
//        set0Only.removeAll(set00);
//        HashSet<ID_TenancyType_PostcodeID> set00AndSet1;
//        set00AndSet1 = new HashSet<ID_TenancyType_PostcodeID>();
//        set00AndSet1.addAll(set00);
//        set00AndSet1.removeAll(set0);
//        set00AndSet1.retainAll(set1);
//        result[0] = union;
//        result[1] = set0Only;
//        result[2] = set00AndSet1;
//        return result;
//    }
//
//    public static Object[] getMembershipsID_PostcodeID(HashSet<ID_PostcodeID> set00, HashSet<ID_PostcodeID> set0, HashSet<ID_PostcodeID> set1) {
//        Object[] result;
//        result = new Object[3];
//        HashSet<ID_PostcodeID> union;
//        union = new HashSet<ID_PostcodeID>();
//        union.addAll(set1);
//        union.retainAll(set0);
//        union.retainAll(set00);
//        HashSet<ID_PostcodeID> set0Only;
//        set0Only = new HashSet<ID_PostcodeID>();
//        set0Only.addAll(set0);
//        set0Only.removeAll(set1);
//        set0Only.removeAll(set00);
//        HashSet<ID_PostcodeID> set00AndSet1;
//        set00AndSet1 = new HashSet<ID_PostcodeID>();
//        set00AndSet1.addAll(set00);
//        set00AndSet1.removeAll(set0);
//        set00AndSet1.retainAll(set1);
//        result[0] = union;
//        result[1] = set0Only;
//        result[2] = set00AndSet1;
//        return result;
//    }
    /**
     *
     * @param nTT
     * @param SHBEFilenames
     * @param include
     * @param yM31
     * @param tClaimantIDPostcodeTTs
     * @param tClaimantIDPostcodeTTs1
     * @return
     */
    public static Object[] getCountsIDPostcodeTT(
            int nTT,
            String[] SHBEFilenames,
            ArrayList<Integer> include,
            String yM31,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTTs,
            HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTTs1) {
        Object[] result;
        result = new Object[4];
        Integer[] mainCounts;
        mainCounts = new Integer[3];
        mainCounts[0] = null;
        mainCounts[1] = null;
        mainCounts[2] = null;
        Integer[] IIITTCounts;
        Integer[] IOITTCounts;
        Integer[] OIOTTCounts;
        IIITTCounts = new Integer[nTT];
        IOITTCounts = new Integer[nTT];
        OIOTTCounts = new Integer[nTT];
        for (int i = 0; i < nTT; i++) {
            IIITTCounts[i] = 0;
            IOITTCounts[i] = 0;
            OIOTTCounts[i] = 0;
        }
        Object[] previousYM3s = getPreviousYM3s(SHBEFilenames, include, yM31);
        if ((Boolean) previousYM3s[0]) {
            HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTTs00;
            tClaimantIDPostcodeTTs00 = tClaimantIDPostcodeTTs.get((String) previousYM3s[1]);
            HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTTs0;
            tClaimantIDPostcodeTTs0 = tClaimantIDPostcodeTTs.get((String) previousYM3s[2]);
            Object[] memberships;
            //memberships = Summary.getMembershipsID_TenancyType_PostcodeID(tClaimantIDPostcodeTTs00, tClaimantIDPostcodeTTs0, tClaimantIDPostcodeTTs1);
            memberships = Summary.getMemberships(
                    tClaimantIDPostcodeTTs00,
                    tClaimantIDPostcodeTTs0,
                    tClaimantIDPostcodeTTs1);
            HashSet<ID_TenancyType_PostcodeID> III;
            III = (HashSet<ID_TenancyType_PostcodeID>) memberships[0];
            HashSet<ID_TenancyType_PostcodeID> IOI;
            IOI = (HashSet<ID_TenancyType_PostcodeID>) memberships[1];
            HashSet<ID_TenancyType_PostcodeID> OIO;
            OIO = (HashSet<ID_TenancyType_PostcodeID>) memberships[2];
            mainCounts[0] = III.size();
            mainCounts[1] = IOI.size();
            mainCounts[2] = OIO.size();
            result[0] = mainCounts;
            Iterator<ID_TenancyType_PostcodeID> ite2;
            ite2 = III.iterator();
            while (ite2.hasNext()) {
                ID_TenancyType_PostcodeID aID_Postcode_TenancyType;
                aID_Postcode_TenancyType = ite2.next();
                int TT = aID_Postcode_TenancyType.getTenancyType();
                IIITTCounts[TT]++;
            }
            result[1] = IIITTCounts;
            ite2 = IOI.iterator();
            while (ite2.hasNext()) {
                ID_TenancyType_PostcodeID aID_Postcode_TenancyType;
                aID_Postcode_TenancyType = ite2.next();
                int TT = aID_Postcode_TenancyType.getTenancyType();
                IOITTCounts[TT]++;
            }
            result[2] = IOITTCounts;
            ite2 = OIO.iterator();
            while (ite2.hasNext()) {
                ID_TenancyType_PostcodeID aID_Postcode_TenancyType;
                aID_Postcode_TenancyType = ite2.next();
                int TT = aID_Postcode_TenancyType.getTenancyType();
                OIOTTCounts[TT]++;
            }
            result[3] = OIOTTCounts;
        }
        return result;
    }

    /**
     *
     * @param nTT
     * @param SHBEFilenames
     * @param include
     * @param yM31
     * @param tClaimantIDTTs
     * @param tClaimantIDTTs1
     * @return
     */
    public static Object[] getCountsIDTT(
            int nTT, String[] SHBEFilenames,
            ArrayList<Integer> include,
            String yM31,
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTTs,
            HashSet<ID_TenancyType> tClaimantIDTTs1) {
        Object[] result;
        result = new Object[4];
        Integer[] mainCounts;
        mainCounts = new Integer[3];
        mainCounts[0] = null;
        mainCounts[1] = null;
        mainCounts[2] = null;
        Integer[] IIITTCounts;
        Integer[] IOITTCounts;
        Integer[] OIOTTCounts;
        IIITTCounts = new Integer[nTT];
        IOITTCounts = new Integer[nTT];
        OIOTTCounts = new Integer[nTT];
        for (int i = 0; i < nTT; i++) {
            IIITTCounts[i] = 0;
            IOITTCounts[i] = 0;
            OIOTTCounts[i] = 0;
        }
        Object[] previousYM3s = getPreviousYM3s(SHBEFilenames, include, yM31);
        if ((Boolean) previousYM3s[0]) {
            HashSet<ID_TenancyType> tClaimantIDTTs00;
            tClaimantIDTTs00 = tClaimantIDTTs.get((String) previousYM3s[1]);
            HashSet<ID_TenancyType> tClaimantIDTTs0;
            tClaimantIDTTs0 = tClaimantIDTTs.get((String) previousYM3s[2]);
            Object[] memberships;
            //memberships = Summary.getMembershipsID_TenancyType(tClaimantIDTTs00, tClaimantIDTTs0, tClaimantIDTTs1);
            memberships = Summary.getMemberships(
                    tClaimantIDTTs00,
                    tClaimantIDTTs0,
                    tClaimantIDTTs1);
            HashSet<ID_TenancyType> III;
            III = (HashSet<ID_TenancyType>) memberships[0];
            HashSet<ID_TenancyType> IOI;
            IOI = (HashSet<ID_TenancyType>) memberships[1];
            HashSet<ID_TenancyType> OIO;
            OIO = (HashSet<ID_TenancyType>) memberships[2];
            mainCounts[0] = III.size();
            mainCounts[1] = IOI.size();
            mainCounts[2] = OIO.size();
            result[0] = mainCounts;
            Iterator<ID_TenancyType> ite2;
            ite2 = III.iterator();
            while (ite2.hasNext()) {
                ID_TenancyType aID_TenancyType;
                aID_TenancyType = ite2.next();
                int TT = aID_TenancyType.getTenancyType();
                IIITTCounts[TT]++;
            }
            result[1] = IIITTCounts;
            ite2 = IOI.iterator();
            while (ite2.hasNext()) {
                ID_TenancyType aID_TenancyType;
                aID_TenancyType = ite2.next();
                int TT = aID_TenancyType.getTenancyType();
                IOITTCounts[TT]++;
            }
            result[2] = IOITTCounts;
            ite2 = OIO.iterator();
            while (ite2.hasNext()) {
                ID_TenancyType aID_TenancyType;
                aID_TenancyType = ite2.next();
                int TT = aID_TenancyType.getTenancyType();
                OIOTTCounts[TT]++;
            }
            result[3] = OIOTTCounts;
        }
        return result;
    }

    /**
     *
     * @param SHBEFilenames
     * @param include
     * @param yM31
     * @param tClaimantIDPostcodeTypes
     * @param tClaimantIDPostcodeTypes1
     * @return
     */
    public static Integer[] getCountsIDPostcode(
            String[] SHBEFilenames, ArrayList<Integer> include,
            String yM31, TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodeTypes,
            HashSet<ID_PostcodeID> tClaimantIDPostcodeTypes1) {
        Integer[] result;
        result = new Integer[3];
        result[0] = null;
        result[1] = null;
        result[2] = null;
        Object[] previousYM3s = getPreviousYM3s(SHBEFilenames, include, yM31);
        if ((Boolean) previousYM3s[0]) {
            HashSet<ID_PostcodeID> tClaimantIDTypes00;
            tClaimantIDTypes00 = tClaimantIDPostcodeTypes.get((String) previousYM3s[1]);
            HashSet tClaimantIDTypes0;
            tClaimantIDTypes0 = tClaimantIDPostcodeTypes.get((String) previousYM3s[2]);
            Object[] memberships;
            //memberships = Summary.getMembershipsID_PostcodeID(tClaimantIDTypes00, tClaimantIDTypes0, tClaimantIDPostcodeTypes1);
            memberships = Summary.getMemberships(
                    tClaimantIDTypes00,
                    tClaimantIDTypes0,
                    tClaimantIDPostcodeTypes1);
            result[0] = ((HashSet<ID_PostcodeID>) memberships[0]).size();
            result[1] = ((HashSet<ID_PostcodeID>) memberships[1]).size();
            result[2] = ((HashSet<ID_PostcodeID>) memberships[2]).size();
        }
        return result;
    }

    private static Object[] getPreviousYM3s(String[] SHBEFilenames, ArrayList<Integer> include, String yM31) {
        Object[] result;
        result = new Object[3];
        String yM300 = null;
        boolean yM300set;
        yM300set = false;
        String yM30 = null;
        boolean yM30set;
        yM30set = false;
        Iterator<Integer> ite = include.iterator();
        while (ite.hasNext()) {
            int i = ite.next();
            String yM3;
            yM3 = DW_SHBE_Handler.getYM3(SHBEFilenames[i]);
            if (yM3.equalsIgnoreCase(yM31)) {
                break;
            }
            if (yM300set && yM30set) {
                yM300 = yM30;
                yM30 = yM3;
            } else {
                if (yM300set) {
                    yM30 = yM3;
                    yM30set = true;
                } else {
                    yM300 = yM3;
                    yM300set = true;
                }
            }
        }
        result[0] = yM300set && yM30set;
        result[1] = yM300;
        result[2] = yM30;
        return result;
    }

    private void addToSummaryCompare2Times(
            int nTT,
            int nEG,
            int nPSI,
            HashMap<String, String> summary) {
        addToSummarySingleTime(
                nTT,
                nEG,
                nPSI,
                summary);
        double percentage;
        double d;
        // All
        summary.put(
                sAllTotalCount_TTChangeClaimant,
                Integer.toString(AllTotalCount_TTChangeClaimant));
        d = AllCount0;
        if (d > 0) {
            percentage = (AllTotalCount_TTChangeClaimant * 100.0d) / d;
            summary.put(
                    sAllPercentageOfAll_TTChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // Postcode
        summary.put(
                sAllTotalCountPostcode0ValidPostcode1Valid,
                Integer.toString(HBTotalCountPostcode0ValidPostcode1Valid + CTBTotalCountPostcode0ValidPostcode1Valid));
        summary.put(
                sAllTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged,
                Integer.toString(AllTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged));
        summary.put(
                sAllTotalCountPostcode0ValidPostcode1ValidPostcodeChange,
                Integer.toString(AllTotalCountPostcode0ValidPostcode1ValidPostcodeChanged));
        summary.put(
                sAllTotalCountPostcode0ValidPostcode1NotValid,
                Integer.toString(AllTotalCountPostcode0ValidPostcode1NotValid));
        summary.put(
                sAllTotalCountPostcode0NotValidPostcode1Valid,
                Integer.toString(AllTotalCountPostcode0NotValidPostcode1Valid));
        summary.put(
                sAllTotalCountPostcode0NotValidPostcode1NotValid,
                Integer.toString(AllTotalCountPostcode0NotValidPostcode1NotValid));
        summary.put(
                sAllTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                Integer.toString(AllTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged));
        summary.put(
                sAllTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged,
                Integer.toString(AllTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged));
        if (d > 0) {
            percentage = (AllTotalCountPostcode0ValidPostcode1Valid * 100.0d) / d;
            summary.put(
                    sAllPercentagePostcode0ValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (AllTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged * 100.0d) / d;
            summary.put(
                    sAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (AllTotalCountPostcode0ValidPostcode1ValidPostcodeChanged * 100.0d) / d;
            summary.put(
                    sAllPercentagePostcode0ValidPostcode1ValidPostcodeChange,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (AllTotalCountPostcode0ValidPostcode1NotValid * 100.0d) / d;
            summary.put(
                    sAllPercentagePostcode0ValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (AllTotalCountPostcode0NotValidPostcode1Valid * 100.0d) / d;
            summary.put(
                    sAllPercentagePostcode0NotValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (AllTotalCountPostcode0NotValidPostcode1NotValid * 100.0d) / d;
            summary.put(
                    sAllPercentagePostcode0NotValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (AllTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged * 100.0d) / d;
            summary.put(
                    sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (AllTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged * 100.0d) / d;
            summary.put(
                    sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // HB
        d = HBCount0;
        // Tenancy Type
        summary.put(
                sHBTotalCount_TTChangeClaimant,
                Integer.toString(HBTotalCount_TTChangeClaimant));
        summary.put(
                sTotalCount_HBTTsToCTBTTs,
                Integer.toString(TotalCount_HBTTsToCTBTTs));
        summary.put(
                sTotalCount_HBTTsToHBTTs,
                Integer.toString(TotalCount_HBTTsToHBTTs));
        if (d > 0) {
            percentage = (HBTotalCount_TTChangeClaimant * 100.0d) / d;
            summary.put(
                    sHBPercentageOfHB_TTChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_HBTTsToCTBTTs * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_HBTTsToCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_HBTTsToHBTTs * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_HBTTsToHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // Postcode
        summary.put(
                sHBTotalCountPostcode0ValidPostcode1Valid,
                Integer.toString(HBTotalCountPostcode0ValidPostcode1Valid));
        summary.put(
                sHBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged,
                Integer.toString(HBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged));
        summary.put(
                sHBTotalCountPostcode0ValidPostcode1ValidPostcodeChange,
                Integer.toString(HBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged));
        summary.put(
                sHBTotalCountPostcode0ValidPostcode1NotValid,
                Integer.toString(HBTotalCountPostcode0ValidPostcode1NotValid));
        summary.put(
                sHBTotalCountPostcode0NotValidPostcode1Valid,
                Integer.toString(HBTotalCountPostcode0NotValidPostcode1Valid));
        summary.put(
                sHBTotalCountPostcode0NotValidPostcode1NotValid,
                Integer.toString(HBTotalCountPostcode0NotValidPostcode1NotValid));
        summary.put(
                sHBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                Integer.toString(HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged));
        summary.put(
                sHBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged,
                Integer.toString(HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged));
        if (d > 0) {
            percentage = (HBTotalCountPostcode0ValidPostcode1Valid * 100.0d) / d;
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged * 100.0d) / d;
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged * 100.0d) / d;
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1ValidPostcodeChange,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCountPostcode0ValidPostcode1NotValid * 100.0d) / d;
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCountPostcode0NotValidPostcode1Valid * 100.0d) / d;
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCountPostcode0NotValidPostcode1NotValid * 100.0d) / d;
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged * 100.0d) / d;
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged * 100.0d) / d;
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // Private Deregulated
        summary.put(
                sTotalCount_PrivateDeregulatedTTsToCTBTTs,
                Integer.toString(TotalCount_PrivateDeregulatedTTsToCTBTTs));
        summary.put(
                sTotalCount_PrivateDeregulatedTTsToSocialTTs,
                Integer.toString(TotalCount_PrivateDeregulatedTTsToSocialTTs));
        summary.put(
                sTotalCount_PrivateDeregulatedTTsToTT1,
                Integer.toString(TotalCount_PrivateDeregulatedTTsToTT1));
        summary.put(
                sTotalCount_PrivateDeregulatedTTsToTT4,
                Integer.toString(TotalCount_PrivateDeregulatedTTsToTT4));
        summary.put(
                sTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs,
                Integer.toString(TotalCount_PostcodeChangeWithinPrivateDeregulatedTTs));
        d = TotalCount_TTClaimant0[3] + TotalCount_TTClaimant0[6];
        if (d > 0) {
            percentage = (TotalCount_PrivateDeregulatedTTsToCTBTTs * 100.0d) / d;
            summary.put(
                    sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_PrivateDeregulatedTTsToSocialTTs * 100.0d) / d;
            summary.put(
                    sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_PrivateDeregulatedTTsToTT1 * 100.0d) / d;
            summary.put(
                    sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_PrivateDeregulatedTTsToTT4 * 100.0d) / d;
            summary.put(
                    sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_PostcodeChangeWithinPrivateDeregulatedTTs * 100.0d) / d;
            summary.put(
                    sPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // TT1
        summary.put(
                sTotalCount_TT1ToTT4,
                Integer.toString(TotalCount_TT1ToTT4));
        d = TotalCount_TTClaimant0[1];
        if (d > 0) {
            percentage = (TotalCount_TT1ToTT4 * 100.0d) / d;
            summary.put(
                    sPercentageOfTT1_TT1ToTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // TT4
        summary.put(
                sTotalCount_TT4ToTT1,
                Integer.toString(TotalCount_TT4ToTT1));
        d = TotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (TotalCount_TT4ToTT1 * 100.0d) / d;
            summary.put(
                    sPercentageOfTT4_TT4ToTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // Social
        d = TotalCount_TTClaimant0[1] + TotalCount_TTClaimant0[4];
        summary.put(
                sTotalCount_SocialTTsToPrivateDeregulatedTTs,
                Integer.toString(TotalCount_SocialTTsToPrivateDeregulatedTTs));
        summary.put(
                sTotalCount_PostcodeChangeWithinSocialTTs,
                Integer.toString(TotalCount_PostcodeChangeWithinSocialTTs));
        summary.put(
                sTotalCount_SocialTTsToCTBTTs,
                Integer.toString(TotalCountSocialTTsToCTBTTs));
        if (d > 0) {
            percentage = (TotalCount_SocialTTsToPrivateDeregulatedTTs * 100.0d) / d;
            summary.put(
                    sPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_PostcodeChangeWithinSocialTTs * 100.0d) / d;
            summary.put(
                    sPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCountSocialTTsToCTBTTs * 100.0d) / d;
            summary.put(
                    sPercentageOfSocialTTs_SocialTTsToCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // TT1
        d = TotalCount_TTClaimant0[1];
        summary.put(
                sTotalCount_TT1ToCTBTTs,
                Integer.toString(TotalCount_TT1ToCTBTTs));
        summary.put(
                sTotalCount_TT1ToPrivateDeregulatedTTs,
                Integer.toString(TotalCount_TT1ToPrivateDeregulatedTTs));
        summary.put(
                sTotalCount_PostcodeChangeWithinTT1,
                Integer.toString(TotalCount_PostcodeChangeWithinTT1));
        if (d > 0) {
            percentage = (TotalCount_TT1ToCTBTTs * 100.0d) / d;
            summary.put(
                    sPercentageOfTT1_TT1ToCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_TT1ToPrivateDeregulatedTTs * 100.0d) / d;
            summary.put(
                    sPercentageOfTT1_TT1ToPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_PostcodeChangeWithinTT1 * 100.0d) / d;
            summary.put(
                    sPercentageOfTT1_PostcodeChangeWithinTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // TT4
        d = TotalCount_TTClaimant0[4];
        summary.put(
                sTotalCount_TT4ToCTBTTs,
                Integer.toString(TotalCount_TT4ToCTBTTs));
        summary.put(
                sTotalCount_TT4ToPrivateDeregulatedTTs,
                Integer.toString(TotalCount_TT4ToPrivateDeregulatedTTs));
        summary.put(
                sTotalCount_PostcodeChangeWithinTT1,
                Integer.toString(TotalCount_PostcodeChangeWithinTT1));
        summary.put(
                sTotalCount_PostcodeChangeWithinTT4,
                Integer.toString(TotalCount_PostcodeChangeWithinTT4));
        if (d > 0) {
            percentage = (TotalCount_TT4ToCTBTTs * 100.0d) / d;
            summary.put(
                    sPercentageOfTT4_TT4ToCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_TT4ToPrivateDeregulatedTTs * 100.0d) / d;
            summary.put(
                    sPercentageOfTT4_TT4ToPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_PostcodeChangeWithinTT1 * 100.0d) / d;
            summary.put(
                    sPercentageOfTT1_PostcodeChangeWithinTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_PostcodeChangeWithinTT4 * 100.0d) / d;
            summary.put(
                    sPercentageOfTT4_PostcodeChangeWithinTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // CTB
        summary.put(
                sCTBTotalCountPostcode0ValidPostcode1Valid,
                Integer.toString(CTBTotalCountPostcode0ValidPostcode1Valid));
        summary.put(
                sCTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged,
                Integer.toString(CTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged));
        summary.put(
                sCTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged,
                Integer.toString(CTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged));
        summary.put(
                sCTBTotalCountPostcode0ValidPostcode1NotValid,
                Integer.toString(CTBTotalCountPostcode0ValidPostcode1NotValid));
        summary.put(
                sCTBTotalCountPostcode0NotValidPostcode1Valid,
                Integer.toString(CTBTotalCountPostcode0NotValidPostcode1Valid));
        summary.put(
                sCTBTotalCountPostcode0NotValidPostcode1NotValid,
                Integer.toString(CTBTotalCountPostcode0NotValidPostcode1NotValid));
        summary.put(
                sCTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                Integer.toString(CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged));
        summary.put(
                sCTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged,
                Integer.toString(CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged));
        summary.put(
                sCTBTotalCount_TTChangeClaimant,
                Integer.toString(CTBTotalCount_TTChangeClaimant));
        summary.put(
                sCTBTotalCount_TTChangeClaimant,
                Integer.toString(CTBTotalCount_TTChangeClaimant));
        summary.put(
                sTotalCountCTBTTsToSocialTTs,
                Integer.toString(TotalCount_CTBTTsToSocialTTs));
        summary.put(
                sTotalCount_CTBTTsToTT1,
                Integer.toString(TotalCount_CTBTTsToTT1));
        summary.put(
                sTotalCount_CTBTTsToTT4,
                Integer.toString(TotalCount_CTBTTsToTT4));
        summary.put(
                sTotalCount_CTBTTsToPrivateDeregulatedTTs,
                Integer.toString(TotalCount_CTBTTsToPrivateDeregulatedTTs));
        summary.put(
                sTotalCount_CTBTTsToHBTTs,
                Integer.toString(TotalCount_CTBTTsToHBTTs));
        d = TotalCount_TTClaimant0[5] + TotalCount_TTClaimant0[7];
        if (d > 0) {
            percentage = (CTBTotalCountPostcode0ValidPostcode1Valid * 100.0d) / d;
            summary.put(
                    sCTBPercentageOfCTB_Postcode0ValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged * 100.0d) / d;
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged * 100.0d) / d;
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCountPostcode0ValidPostcode1NotValid * 100.0d) / d;
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCountPostcode0NotValidPostcode1Valid * 100.0d) / d;
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCountPostcode0NotValidPostcode1NotValid * 100.0d) / d;
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged * 100.0d) / d;
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged * 100.0d) / d;
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCount_TTChangeClaimant * 100.0d) / d;
            summary.put(
                    sCTBPercentageOfCTB_TTChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCount_TTChangeClaimant * 100.0d) / d;
            summary.put(
                    sCTBPercentageOfCTB_TTChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBTTsToSocialTTs * 100.0d) / d;
            summary.put(
                    sPercentageOfCTB_CTBTTsToSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBTTsToTT1 * 100.0d) / d;
            summary.put(
                    sPercentageOfCTB_CTBTTsToTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBTTsToTT4 * 100.0d) / d;
            summary.put(
                    sPercentageOfCTB_CTBTTsToTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBTTsToPrivateDeregulatedTTs * 100.0d) / d;
            summary.put(
                    sPercentageOfCTB_CTBTTsToPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBTTsToHBTTs * 100.0d) / d;
            summary.put(
                    sPercentageOfCTB_CTBTTsToHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
    }

    private void addToSummarySingleTimeRentArrears(
            HashMap<String, String> summary) {
        summary.put(
                sTotal_RentArrears,
                "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                        BigDecimal.valueOf(Total_RentArrears),
                        2, RoundingMode.HALF_UP));
        summary.put(
                sTotalCount_RentArrears,
                "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                        BigDecimal.valueOf(TotalCount_RentArrears),
                        2, RoundingMode.HALF_UP));
        summary.put(
                sTotalCount_RentArrearsNonZero,
                "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                        BigDecimal.valueOf(TotalCount_RentArrearsNonZero),
                        2, RoundingMode.HALF_UP));
        summary.put(
                sTotalCount_RentArrearsZero,
                "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                        BigDecimal.valueOf(TotalCount_RentArrearsZero),
                        2, RoundingMode.HALF_UP));
        if (TotalCount_RentArrears != 0.0d) {
            summary.put(
                    sAverage_RentArrears,
                    "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                            BigDecimal.valueOf(Total_RentArrears / (double) TotalCount_RentArrears),
                            2, RoundingMode.HALF_UP));
        }
        if (TotalCount_RentArrearsNonZero != 0.0d) {
            summary.put(
                    sAverage_NonZeroRentArrears,
                    "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                            BigDecimal.valueOf(Total_RentArrears / TotalCount_RentArrearsNonZero),
                            2, RoundingMode.HALF_UP));
        }
    }

    private void addToSummarySingleTime(
            int nTT,
            int nEG,
            int nPSI,
            HashMap<String, String> summary) {
        // Set the last results
        AllCount0 = AllCount1;
        HBCount0 = HBCount1;
        CTBCount0 = CTBCount1;
        AllUOCount0 = AllUOCount1;
        CouncilCount0 = CouncilCount1;
        RSLCount0 = RSLCount1;
        CouncilLinkedRecordCount0 = CouncilLinkedRecordCount1;
        RSLLinkedRecordCount0 = RSLLinkedRecordCount1;
        AllUOLinkedRecordCount0 = AllUOLinkedRecordCount1;
//        for (int TT = 0; TT < nTT; TT++) {
//            AllTotalCountTTClaimant0[TT] = AllTotalCountTTClaimant1[TT];
//        }
        System.arraycopy(TotalCount_TTClaimant1, 0, TotalCount_TTClaimant0, 0, nTT);

        addToSummarySingleTime0(summary);
        addToSummarySingleTimeDisability(nTT, summary);
        addToSummarySingleTimeEthnicity(nEG, summary);
        addToSummarySingleTimeTT(nTT, summary);
        addToSummarySingleTimePSI(nTT, nPSI, summary);
        addToSummarySingleTime1(summary);
//        AllCount0 = AllCount1;
//        HBCount0 = HBCount1;
//        CTBCount0 = CTBCount1;
    }

    private void addToSummarySingleTime0(
            HashMap<String, String> summary) {
        // All
        AllCount1 = HBCount1 + CTBCount1;
        summary.put(sAllCount1, Integer.toString(AllCount1));
        summary.put(sHBCount1, Integer.toString(HBCount1));
        summary.put(sCTBCount1, Integer.toString(CTBCount1));
        summary.put(
                sAllTotalHouseholdSize,
                Long.toString(AllTotalHouseholdSize));
        double ave;
        // All HouseholdSize
        if (AllCount1 > 0) {
            ave = AllTotalHouseholdSize / (double) AllCount1;
            summary.put(
                    sAllAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), decimalPlacePrecisionForAverage, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAllAverageHouseholdSize,
                    s0);
        }
        // HB HouseholdSize
        summary.put(
                sHBTotalHouseholdSize,
                Long.toString(HBTotalHouseholdSize));
        if (HBCount1 > 0) {
            ave = HBTotalHouseholdSize / (double) HBCount1;
            summary.put(
                    sHBAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), decimalPlacePrecisionForAverage, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBAverageHouseholdSize,
                    s0);
        }
        // CTB HouseholdSize
        summary.put(
                sCTBTotalHouseholdSize,
                Long.toString(CTBTotalHouseholdSize));
        if (CTBCount1 > 0) {
            ave = CTBTotalHouseholdSize / (double) CTBCount1;
            summary.put(
                    sCTBAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), decimalPlacePrecisionForAverage, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBAverageHouseholdSize,
                    s0);
        }
    }

    private void addToSummarySingleTimePSI(
            int nTT,
            int nPSI,
            HashMap<String, String> summary) {
        double ave;
        double d;
        // PassportStandardIndicator
        for (int i = 0; i < nPSI; i++) {
            summary.put(
                    sAllTotalCount_PSI[i],
                    Long.toString(AllTotalCount_PSI[i]));
            d = AllCount1;
            if (d > 0) {
                ave = (AllTotalCount_PSI[i] * 100.0d) / d;
                summary.put(
                        sAllPercentageOfAll_PSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            summary.put(
                    sHBTotalCount_PSI[i],
                    Long.toString(HBTotalCount_PSI[i]));
            d = HBCount1;
            if (d > 0) {
                ave = (HBTotalCount_PSI[i] * 100.0d) / d;
                summary.put(
                        sHBPercentageOfHB_PSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            summary.put(
                    sCTBTotalCountPSI[i],
                    Long.toString(CTBTotalCount_PSI[i]));
            d = CTBCount1;
            if (d > 0) {
                ave = (CTBTotalCount_PSI[i] * 100.0d) / d;
                summary.put(
                        sCTBPercentageOfCTB_PSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            for (int j = 1; j < nTT; j++) {
                summary.put(
                        sAllTotalCount_PSIByTT[i][j],
                        Long.toString(AllTotalCount_PSIByTT[i][j]));
                d = AllCount1;
                if (d > 0) {
                    ave = (AllTotalCount_PSIByTT[i][j] * 100.0d) / d;
                    summary.put(
                            sAllPercentageOfAll_PSIByTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
                summary.put(
                        sHBTotalCount_PSIByTT[i][j],
                        Long.toString(HBTotalCount_PSIByTT[i][j]));
                d = HBCount1;
                if (d > 0) {
                    ave = (HBTotalCount_PSIByTT[i][j] * 100.0d) / d;
                    summary.put(
                            sHBPercentageOfHB_PSIByTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
                summary.put(
                        sCTBTotalCount_PSIByTT[i][j],
                        Long.toString(CTBTotalCount_PSIByTT[i][j]));
                d = CTBCount1;
                if (d > 0) {
                    ave = (CTBTotalCount_PSIByTT[i][j] * 100.0d) / d;
                    summary.put(
                            sCTBPercentageOfCTB_PSIByTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
            }
        }
    }

    private void addToSummarySingleTimeDisability(
            int nTT,
            HashMap<String, String> summary) {
        double percentage;
        double d;
        int t;
        // DisabilityAward
        t = TotalCount_DisabilityAwardByTT[1]
                + TotalCount_DisabilityAwardByTT[2]
                + TotalCount_DisabilityAwardByTT[3]
                + TotalCount_DisabilityAwardByTT[4]
                + TotalCount_DisabilityAwardByTT[5]
                + TotalCount_DisabilityAwardByTT[6]
                + TotalCount_DisabilityAwardByTT[7]
                + TotalCount_DisabilityAwardByTT[8]
                + TotalCount_DisabilityAwardByTT[9];
        summary.put(
                sTotalCount_DisabilityAward,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_DisabilityAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityAwardHBTTs
        t = TotalCount_DisabilityAwardByTT[1]
                + TotalCount_DisabilityAwardByTT[2]
                + TotalCount_DisabilityAwardByTT[3]
                + TotalCount_DisabilityAwardByTT[4]
                + TotalCount_DisabilityAwardByTT[6]
                + TotalCount_DisabilityAwardByTT[8]
                + TotalCount_DisabilityAwardByTT[9];
        summary.put(
                sTotalCount_DisabilityAwardHBTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_DisabilityAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_DisabilityAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityAwardByTT
        t = TotalCount_DisabilityAwardByTT[5]
                + TotalCount_DisabilityAwardByTT[7];
        summary.put(
                sTotalCount_DisabilityAwardCTBTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_DisabilityAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CTBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfCTB_DisabilityAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityAwardSocialTTs
        t = TotalCount_DisabilityAwardByTT[1] + TotalCount_DisabilityAwardByTT[4];
        summary.put(
                sTotalCount_DisabilityAwardSocialTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_DisabilityAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_DisabilityAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityAwardPrivateDeregulatedTTs
        t = TotalCount_DisabilityAwardByTT[3] + TotalCount_DisabilityAwardByTT[6];
        summary.put(
                sTotalCount_DisabilityAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAward
        t = TotalCount_DisabilityPremiumAwardByTT[1]
                + TotalCount_DisabilityPremiumAwardByTT[2]
                + TotalCount_DisabilityPremiumAwardByTT[3]
                + TotalCount_DisabilityPremiumAwardByTT[4]
                + TotalCount_DisabilityPremiumAwardByTT[5]
                + TotalCount_DisabilityPremiumAwardByTT[6]
                + TotalCount_DisabilityPremiumAwardByTT[7]
                + TotalCount_DisabilityPremiumAwardByTT[8]
                + TotalCount_DisabilityPremiumAwardByTT[9];
        summary.put(
                sTotalCount_DisabilityPremiumAward,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_DisabilityPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAwardHBTTs
        t = TotalCount_DisabilityPremiumAwardByTT[1]
                + TotalCount_DisabilityPremiumAwardByTT[2]
                + TotalCount_DisabilityPremiumAwardByTT[3]
                + TotalCount_DisabilityPremiumAwardByTT[4]
                + TotalCount_DisabilityPremiumAwardByTT[6]
                + TotalCount_DisabilityPremiumAwardByTT[8]
                + TotalCount_DisabilityPremiumAwardByTT[9];
        summary.put(
                sTotalCount_DisabilityPremiumAwardHBTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_DisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_DisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAwardCTBTTs
        t = TotalCount_DisabilityPremiumAwardByTT[5]
                + TotalCount_DisabilityPremiumAwardByTT[7];
        summary.put(
                sTotalCount_DisabilityPremiumAwardCTBTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_DisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CTBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfCTB_DisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAwardSocialTTs
        t = TotalCount_DisabilityPremiumAwardByTT[1] + TotalCount_DisabilityPremiumAwardByTT[4];
        summary.put(
                sTotalCount_DisabilityPremiumAwardSocialTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_DisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_DisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAwardByPrivateDeregulatedTTs
        t = TotalCount_DisabilityPremiumAwardByTT[3] + TotalCount_DisabilityPremiumAwardByTT[6];
        summary.put(
                sTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabilityPremiumAward
        t = TotalCount_SevereDisabilityPremiumAwardByTT[1]
                + TotalCount_SevereDisabilityPremiumAwardByTT[2]
                + TotalCount_SevereDisabilityPremiumAwardByTT[3]
                + TotalCount_SevereDisabilityPremiumAwardByTT[4]
                + TotalCount_SevereDisabilityPremiumAwardByTT[5]
                + TotalCount_SevereDisabilityPremiumAwardByTT[6]
                + TotalCount_SevereDisabilityPremiumAwardByTT[7]
                + TotalCount_SevereDisabilityPremiumAwardByTT[8]
                + TotalCount_SevereDisabilityPremiumAwardByTT[9];
        summary.put(
                sTotalCount_SevereDisabilityPremiumAward,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_SevereDisabilityPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereSevereDisabilityPremiumAwardHBTTs
        t = TotalCount_SevereDisabilityPremiumAwardByTT[1]
                + TotalCount_SevereDisabilityPremiumAwardByTT[2]
                + TotalCount_SevereDisabilityPremiumAwardByTT[3]
                + TotalCount_SevereDisabilityPremiumAwardByTT[4]
                + TotalCount_SevereDisabilityPremiumAwardByTT[6]
                + TotalCount_SevereDisabilityPremiumAwardByTT[8]
                + TotalCount_SevereDisabilityPremiumAwardByTT[9];
        summary.put(
                sTotalCount_SevereDisabilityPremiumAwardHBTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_SevereDisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_SevereDisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabilityPremiumAwardCTBTTs
        t = TotalCount_SevereDisabilityPremiumAwardByTT[5]
                + TotalCount_SevereDisabilityPremiumAwardByTT[7];
        summary.put(
                sTotalCount_SevereDisabilityPremiumAwardCTBTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CTBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabilityPremiumAwardSocialTTs
        t = TotalCount_SevereDisabilityPremiumAwardByTT[1] + TotalCount_SevereDisabilityPremiumAwardByTT[4];
        summary.put(
                sTotalCount_SevereDisabilityPremiumAwardSocialTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabilityPremiumAwardPrivateDeregulatedTTs
        t = TotalCount_SevereDisabilityPremiumAwardByTT[3] + TotalCount_SevereDisabilityPremiumAwardByTT[6];
        summary.put(
                sTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAward
        t = TotalCount_DisabledChildPremiumAwardByTT[1]
                + TotalCount_DisabledChildPremiumAwardByTT[2]
                + TotalCount_DisabledChildPremiumAwardByTT[3]
                + TotalCount_DisabledChildPremiumAwardByTT[4]
                + TotalCount_DisabledChildPremiumAwardByTT[5]
                + TotalCount_DisabledChildPremiumAwardByTT[6]
                + TotalCount_DisabledChildPremiumAwardByTT[7]
                + TotalCount_DisabledChildPremiumAwardByTT[8]
                + TotalCount_DisabledChildPremiumAwardByTT[9];
        summary.put(
                sTotalCount_DisabledChildPremiumAward,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_DisabledChildPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabledChildPremiumAwardHBTTs
        t = TotalCount_DisabledChildPremiumAwardByTT[1]
                + TotalCount_DisabledChildPremiumAwardByTT[2]
                + TotalCount_DisabledChildPremiumAwardByTT[3]
                + TotalCount_DisabledChildPremiumAwardByTT[4]
                + TotalCount_DisabledChildPremiumAwardByTT[6]
                + TotalCount_DisabledChildPremiumAwardByTT[8]
                + TotalCount_DisabledChildPremiumAwardByTT[9];
        summary.put(
                sTotalCount_DisabledChildPremiumAwardHBTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_DisabledChildPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_DisabledChildPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAwardCTBTTs
        t = TotalCount_DisabledChildPremiumAwardByTT[5]
                + TotalCount_DisabledChildPremiumAwardByTT[7];
        summary.put(
                sTotalCount_DisabledChildPremiumAwardCTBTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_DisabledChildPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CTBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfCTB_DisabledChildPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAwardSocialTTs
        t = TotalCount_DisabledChildPremiumAwardByTT[1] + TotalCount_DisabledChildPremiumAwardByTT[4];
        summary.put(
                sTotalCount_DisabledChildPremiumAwardSocialTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_DisabledChildPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_DisabledChildPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAwardPrivateDeregulatedTTs
        t = TotalCount_DisabledChildPremiumAwardByTT[3] + TotalCount_DisabledChildPremiumAwardByTT[6];
        summary.put(
                sTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAward
        t = TotalCount_EnhancedDisabilityPremiumAwardByTT[1]
                + TotalCount_EnhancedDisabilityPremiumAwardByTT[2]
                + TotalCount_EnhancedDisabilityPremiumAwardByTT[3]
                + TotalCount_EnhancedDisabilityPremiumAwardByTT[4]
                + TotalCount_EnhancedDisabilityPremiumAwardByTT[5]
                + TotalCount_EnhancedDisabilityPremiumAwardByTT[6]
                + TotalCount_EnhancedDisabilityPremiumAwardByTT[7]
                + TotalCount_EnhancedDisabilityPremiumAwardByTT[8]
                + TotalCount_EnhancedDisabilityPremiumAwardByTT[9];
        summary.put(
                sTotalCount_EnhancedDisabilityPremiumAward,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_EnhancedDisabilityPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAwardHBTTs
        t = TotalCount_EnhancedDisabilityPremiumAwardByTT[1]
                + TotalCount_EnhancedDisabilityPremiumAwardByTT[2]
                + TotalCount_EnhancedDisabilityPremiumAwardByTT[3]
                + TotalCount_EnhancedDisabilityPremiumAwardByTT[4]
                + TotalCount_EnhancedDisabilityPremiumAwardByTT[6]
                + TotalCount_EnhancedDisabilityPremiumAwardByTT[8]
                + TotalCount_EnhancedDisabilityPremiumAwardByTT[9];
        summary.put(
                sTotalCount_EnhancedDisabilityPremiumAwardHBTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAwardCTBTTs
        t = TotalCount_EnhancedDisabilityPremiumAwardByTT[5]
                + TotalCount_EnhancedDisabilityPremiumAwardByTT[7];
        summary.put(
                sTotalCount_EnhancedDisabilityPremiumAwardCTBTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CTBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAwardSocialTTs
        t = TotalCount_EnhancedDisabilityPremiumAwardByTT[1] + TotalCount_EnhancedDisabilityPremiumAwardByTT[4];
        summary.put(
                sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs
        t = TotalCount_EnhancedDisabilityPremiumAwardByTT[3] + TotalCount_EnhancedDisabilityPremiumAwardByTT[6];
        summary.put(
                sTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // ByTT
        for (int i = 0; i < nTT; i++) {
            summary.put(
                    sAllTotalCount_DisabilityAwardByTT[i],
                    Integer.toString(TotalCount_DisabilityAwardByTT[i]));
            summary.put(
                    sAllTotalCount_DisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_DisabilityPremiumAwardByTT[i]));
            summary.put(
                    sAllTotalCountSevereDisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_SevereDisabilityPremiumAwardByTT[i]));
            summary.put(
                    sAllTotalCountDisabledChildPremiumAwardByTT[i],
                    Integer.toString(TotalCount_DisabledChildPremiumAwardByTT[i]));
            summary.put(
                    sAllTotalCount_EnhancedDisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
            d = AllCount1;
            if (d > 0) {
                percentage = (TotalCount_DisabilityAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sAllPercentageOfAll_DisabilityAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
                percentage = (TotalCount_DisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sAllPercentageOfAll_DisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
                percentage = (TotalCount_SevereDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sAllPercentageOfAll_SevereDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
                percentage = (TotalCount_DisabledChildPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sAllPercentageOfAll_DisabledChildPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
                percentage = (TotalCount_EnhancedDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sAllPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            summary.put(
                    sHBTotalCount_DisabilityAwardByTT[i],
                    Integer.toString(TotalCount_DisabilityAwardByTT[i]));
            summary.put(
                    sHBTotalCount_DisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_DisabilityPremiumAwardByTT[i]));
            summary.put(
                    sHBTotalCount_SevereDisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_SevereDisabilityPremiumAwardByTT[i]));
            summary.put(
                    sHBTotalCount_DisabledChildPremiumAwardByTT[i],
                    Integer.toString(TotalCount_DisabledChildPremiumAwardByTT[i]));
            summary.put(
                    sHBTotalCount_EnhancedDisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
            d = HBCount1;
            if (d > 0) {
                percentage = (TotalCount_DisabilityAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sHBPercentageOfHB_DisabilityAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
                percentage = (TotalCount_DisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sHBPercentageOfHB_DisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
                percentage = (TotalCount_SevereDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sHBPercentageOfHB_SevereDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
                percentage = (TotalCount_DisabledChildPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sHBPercentageOfHB_DisabledChildPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
                percentage = (TotalCount_EnhancedDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sHBPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            summary.put(
                    sCTBTotalCount_DisabilityAwardByTT[i],
                    Integer.toString(TotalCount_DisabilityAwardByTT[i]));
            summary.put(
                    sCTBTotalCount_DisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_DisabilityPremiumAwardByTT[i]));
            summary.put(
                    sCTBTotalCount_SevereDisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_SevereDisabilityPremiumAwardByTT[i]));
            summary.put(
                    sCTBTotalCount_DisabledChildPremiumAwardByTT[i],
                    Integer.toString(TotalCount_DisabledChildPremiumAwardByTT[i]));
            summary.put(
                    sCTBTotalCount_EnhancedDisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
            d = CTBCount1;
            if (d > 0) {
                percentage = (TotalCount_DisabilityAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sCTBPercentageOfCTB_DisabilityAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
                percentage = (TotalCount_DisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sCTBPercentageOfCTB_DisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
                percentage = (TotalCount_SevereDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sCTBPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
                percentage = (TotalCount_DisabledChildPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sCTBPercentageOfCTB_DisabledChildPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
                percentage = (TotalCount_EnhancedDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sCTBPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
            }
        }
    }

    private void addToSummarySingleTime1(
            HashMap<String, String> summary) {
        double ave;
        double d;
        // WeeklyHBEntitlement
        summary.put(
                sTotalWeeklyHBEntitlement,
                BigDecimal.valueOf(AllTotalWeeklyHBEntitlement).toPlainString());
        summary.put(
                sTotalCountWeeklyHBEntitlementNonZero,
                Integer.toString(AllTotalWeeklyHBEntitlementNonZeroCount));
        summary.put(
                sTotalCountWeeklyHBEntitlementZero,
                Integer.toString(AllTotalWeeklyHBEntitlementZeroCount));
        d = AllTotalWeeklyHBEntitlementNonZeroCount;
        if (d > 0) {
            ave = AllTotalWeeklyHBEntitlement / d;
            summary.put(
                    sAverageWeeklyHBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAverageWeeklyHBEntitlement,
                    s0);
        }
        summary.put(
                sHBTotalWeeklyHBEntitlement,
                BigDecimal.valueOf(HBTotalWeeklyHBEntitlement).toPlainString());
        summary.put(sHBTotalCount_WeeklyHBEntitlementNonZero,
                Integer.toString(HBTotalCount_WeeklyHBEntitlementNonZero));
        summary.put(sHBTotalCount_WeeklyHBEntitlementZero,
                Integer.toString(HBTotalCount_WeeklyHBEntitlementZero));
        d = HBTotalCount_WeeklyHBEntitlementNonZero;
        if (d > 0) {
            ave = HBTotalWeeklyHBEntitlement / d;
            summary.put(
                    sHBAverageWeeklyHBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBAverageWeeklyHBEntitlement,
                    s0);
        }
        summary.put(
                sCTBTotalWeeklyHBEntitlement,
                BigDecimal.valueOf(CTBTotalWeeklyHBEntitlement).toPlainString());
        summary.put(sCTBTotalCount_WeeklyHBEntitlementNonZero,
                Integer.toString(CTBTotalCount_WeeklyHBEntitlementNonZero));
        summary.put(
                sCTBTotalCountWeeklyHBEntitlementZero,
                Integer.toString(CTBTotalWeeklyHBEntitlementZeroCount));
        d = CTBTotalCount_WeeklyHBEntitlementNonZero;
        if (d > 0) {
            ave = CTBTotalWeeklyHBEntitlement / d;
            summary.put(
                    sCTBAverageWeeklyHBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBAverageWeeklyHBEntitlement,
                    s0);
        }
        // WeeklyCTBEntitlement
        summary.put(
                sTotalWeeklyCTBEntitlement,
                BigDecimal.valueOf(AllTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(
                sTotalCountWeeklyCTBEntitlementNonZero,
                Integer.toString(AllTotalCount_WeeklyCTBEntitlementNonZero));
        summary.put(
                sTotalCountWeeklyCTBEntitlementZero,
                Integer.toString(AllTotalWeeklyCTBEntitlementZeroCount));
        d = AllTotalCount_WeeklyCTBEntitlementNonZero;
        if (d > 0) {
            ave = AllTotalWeeklyCTBEntitlement / d;
            summary.put(
                    sAverageWeeklyCTBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAverageWeeklyCTBEntitlement,
                    s0);
        }
        summary.put(
                sHBTotalWeeklyCTBEntitlement,
                BigDecimal.valueOf(HBTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(sHBTotalCount_WeeklyCTBEntitlementNonZero,
                Integer.toString(HBTotalCount_WeeklyCTBEntitlementNonZero));
        summary.put(sHBTotalCount_WeeklyCTBEntitlementZero,
                Integer.toString(HBTotalWeeklyCTBEntitlementZeroCount));
        d = HBTotalCount_WeeklyCTBEntitlementNonZero;
        if (d > 0) {
            ave = HBTotalWeeklyCTBEntitlement / d;
            summary.put(
                    sHBAverageWeeklyCTBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBAverageWeeklyCTBEntitlement,
                    s0);
        }
        summary.put(
                sCTBTotalWeeklyCTBEntitlement,
                BigDecimal.valueOf(CTBTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(sCTBTotalCount_WeeklyCTBEntitlementNonZero,
                Integer.toString(CTBTotalCount_WeeklyCTBEntitlementNonZero));
        summary.put(
                sCTBTotalCountWeeklyCTBEntitlementZero,
                Integer.toString(CTBTotalWeeklyCTBEntitlementZeroCount));
        d = CTBTotalCount_WeeklyCTBEntitlementNonZero;
        if (d > 0) {
            ave = CTBTotalWeeklyCTBEntitlement / d;
            summary.put(
                    sCTBAverageWeeklyCTBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBAverageWeeklyCTBEntitlement,
                    s0);
        }
        // WeeklyEligibleRentAmount
        summary.put(
                sAllTotalWeeklyEligibleRentAmount,
                BigDecimal.valueOf(AllTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                sAllTotalCountWeeklyEligibleRentAmountNonZero,
                Integer.toString(AllTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(
                sAllTotalCountWeeklyEligibleRentAmountZero,
                Integer.toString(AllTotalWeeklyEligibleRentAmountZeroCount));
        d = AllTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = AllTotalWeeklyEligibleRentAmount / d;
            summary.put(
                    sAllAverageWeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAllAverageWeeklyEligibleRentAmount,
                    s0);
        }
        summary.put(
                sHBTotalWeeklyEligibleRentAmount,
                BigDecimal.valueOf(HBTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                sHBTotalCountWeeklyEligibleRentAmountNonZero,
                Integer.toString(HBTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(
                sHBTotalCountWeeklyEligibleRentAmountZero,
                Integer.toString(HBTotalWeeklyEligibleRentAmountZeroCount));
        d = HBTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = HBTotalWeeklyEligibleRentAmount / d;
            summary.put(
                    sHBAverageWeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBAverageWeeklyEligibleRentAmount,
                    s0);
        }
        summary.put(
                sCTBTotalWeeklyEligibleRentAmount,
                BigDecimal.valueOf(CTBTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                sCTBTotalCountWeeklyEligibleRentAmountNonZero,
                Integer.toString(CTBTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(
                sCTBTotalCountWeeklyEligibleRentAmountZero,
                Integer.toString(CTBTotalWeeklyEligibleRentAmountZeroCount));
        d = CTBTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = CTBTotalWeeklyEligibleRentAmount / d;
            summary.put(
                    sCTBAverageWeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBAverageWeeklyEligibleRentAmount,
                    s0);
        }
        // WeeklyEligibleCouncilTaxAmount
        summary.put(
                sAllTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(AllTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(
                sAllTotalCountWeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(AllTotalCountWeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(
                sAllTotalCountWeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(AllTotalCountWeeklyEligibleCouncilTaxAmountZero));
        d = AllTotalCountWeeklyEligibleCouncilTaxAmountNonZero;
        if (d > 0) {
            ave = AllTotalWeeklyEligibleCouncilTaxAmount / d;
            summary.put(
                    sAllAverageWeeklyEligibleCouncilTaxAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAllAverageWeeklyEligibleCouncilTaxAmount,
                    s0);
        }
        summary.put(
                sHBTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(HBTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(
                sHBTotalCountWeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(HBTotalCountWeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(
                sHBTotalCountWeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(HBTotalCountWeeklyEligibleCouncilTaxAmountZero));
        d = HBTotalCountWeeklyEligibleCouncilTaxAmountNonZero;
        if (d > 0) {
            ave = HBTotalWeeklyEligibleCouncilTaxAmount / d;
            summary.put(
                    sHBAverageWeeklyEligibleCouncilTaxAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBAverageWeeklyEligibleCouncilTaxAmount,
                    s0);
        }
        summary.put(
                sCTBTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(CTBTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(
                sCTBTotalCountWeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(CTBTotalCountWeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(
                sCTBTotalCountWeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(CTBTotalCountWeeklyEligibleCouncilTaxAmountZero));
        d = CTBTotalCountWeeklyEligibleCouncilTaxAmountNonZero;
        if (d > 0) {
            ave = CTBTotalWeeklyEligibleCouncilTaxAmount / d;
            summary.put(
                    sCTBAverageWeeklyEligibleCouncilTaxAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBAverageWeeklyEligibleCouncilTaxAmount,
                    s0);
        }
        // ContractualRentAmount
        summary.put(
                sAllTotalContractualRentAmount,
                BigDecimal.valueOf(AllTotalContractualRentAmount).toPlainString());
        summary.put(
                sAllTotalCountContractualRentAmountNonZeroCount,
                Integer.toString(AllTotalContractualRentAmountNonZeroCount));
        summary.put(
                sAllTotalCountContractualRentAmountZeroCount,
                Integer.toString(AllTotalContractualRentAmountZeroCount));
        d = AllTotalContractualRentAmountNonZeroCount;
        if (d > 0) {
            ave = AllTotalContractualRentAmount / d;
            summary.put(
                    sAllAverageContractualRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAllAverageContractualRentAmount,
                    s0);
        }
        summary.put(
                sHBTotalContractualRentAmount,
                BigDecimal.valueOf(HBTotalContractualRentAmount).toPlainString());
        summary.put(
                sHBTotalCountContractualRentAmountNonZeroCount,
                Integer.toString(HBTotalContractualRentAmountNonZeroCount));
        summary.put(
                sHBTotalCountContractualRentAmountZeroCount,
                Integer.toString(HBTotalContractualRentAmountZeroCount));
        d = HBTotalContractualRentAmountNonZeroCount;
        if (d > 0) {
            ave = HBTotalContractualRentAmount / d;
            summary.put(
                    sHBAverageContractualRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBAverageContractualRentAmount,
                    s0);
        }
        summary.put(
                sCTBTotalContractualRentAmount,
                BigDecimal.valueOf(CTBTotalContractualRentAmount).toPlainString());
        summary.put(
                sCTBTotalCountContractualRentAmountNonZeroCount,
                Integer.toString(CTBTotalContractualRentAmountNonZeroCount));
        summary.put(
                sCTBTotalCountContractualRentAmountZeroCount,
                Integer.toString(CTBTotalContractualRentAmountZeroCount));
        d = CTBTotalContractualRentAmountNonZeroCount;
        if (d > 0) {
            ave = CTBTotalContractualRentAmount / d;
            summary.put(
                    sCTBAverageContractualRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBAverageContractualRentAmount,
                    s0);
        }
        // WeeklyAdditionalDiscretionaryPayment
        summary.put(
                sAllTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(AllTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sAllTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(AllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sAllTotalCountWeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(AllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        d = AllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
        if (d > 0) {
            ave = AllTotalWeeklyAdditionalDiscretionaryPayment / d;
            summary.put(
                    sAllAverageWeeklyAdditionalDiscretionaryPayment,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAllAverageWeeklyAdditionalDiscretionaryPayment,
                    s0);
        }
        summary.put(
                sHBTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(HBTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sHBTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(HBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sHBTotalCountWeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(HBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        d = HBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
        if (d > 0) {
            ave = HBTotalWeeklyAdditionalDiscretionaryPayment / d;
            summary.put(
                    sHBAverageWeeklyAdditionalDiscretionaryPayment,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBAverageWeeklyAdditionalDiscretionaryPayment,
                    s0);
        }
        summary.put(
                sCTBTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(CTBTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(CTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(CTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        d = CTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
        if (d > 0) {
            ave = CTBTotalWeeklyAdditionalDiscretionaryPayment / d;
            summary.put(
                    sCTBAverageWeeklyAdditionalDiscretionaryPayment,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBAverageWeeklyAdditionalDiscretionaryPayment,
                    s0);
        }
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        summary.put(
                sAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sAllTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sAllTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        d = AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
        if (d > 0) {
            ave = AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability / d;
            summary.put(
                    sAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    s0);
        }
        summary.put(
                sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sHBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sHBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        d = HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
        if (d > 0) {
            ave = HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability / d;
            summary.put(
                    sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    s0);
        }
        summary.put(
                sCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        d = CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
        if (d > 0) {
            ave = CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability / d;
            summary.put(
                    sCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    s0);
        }
        // Employed
        int t;
        t = HBTotalCount_EmployedClaimants + CTBTotalCountEmployedClaimants;
        summary.put(
                sAllTotalCount_ClaimantsEmployed,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sAllPercentage_ClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAllPercentage_ClaimantsEmployed,
                    s0);
        }
        summary.put(
                sHBTotalCountClaimantsEmployed,
                Integer.toString(HBTotalCount_EmployedClaimants));
        d = HBCount1;
        if (d > 0) {
            ave = (HBTotalCount_EmployedClaimants * 100.0d) / d;
            summary.put(
                    sHBPercentageOfHB_ClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentageOfHB_ClaimantsEmployed,
                    s0);
        }
        summary.put(
                sCTBTotalCount_ClaimantsEmployed,
                Integer.toString(CTBTotalCountEmployedClaimants));
        d = CTBCount1;
        if (d > 0) {
            ave = (CTBTotalCountEmployedClaimants * 100.0d) / d;
            summary.put(
                    sCTBPercentageOfCTB_ClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentageOfCTB_ClaimantsEmployed,
                    s0);
        }
        // Self Employed
        summary.put(
                sHBTotalCountClaimantsSelfEmployed,
                Integer.toString(HBTotalCountSelfEmployedClaimants));
        d = HBCount1;
        if (d > 0) {
            ave = (HBTotalCountSelfEmployedClaimants * 100.0d) / d;
            summary.put(
                    sHBPercentageOfHB_ClaimantsSelfEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentageOfHB_ClaimantsSelfEmployed,
                    s0);
        }
        summary.put(
                sCTBTotalCountClaimantsSelfEmployed,
                Integer.toString(CTBTotalCountSelfEmployedClaimants));
        d = CTBCount1;
        if (d > 0) {
            ave = (CTBTotalCountSelfEmployedClaimants * 100.0d) / d;
            summary.put(
                    sCTBPercentageOfCTB_ClaimantsSelfEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentageOfCTB_ClaimantsSelfEmployed,
                    s0);
        }
        // Students
        summary.put(
                sHBTotalCountClaimantsStudents,
                Integer.toString(HBTotalCountStudentsClaimants));
        d = HBCount1;
        if (d > 0) {
            ave = (HBTotalCountStudentsClaimants * 100.0d) / d;
            summary.put(
                    sHBPercentageOfHB_ClaimantsStudents,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentageOfHB_ClaimantsStudents,
                    s0);
        }
        summary.put(
                sCTBTotalCountClaimantsStudents,
                Integer.toString(CTBTotalCountStudentsClaimants));
        d = CTBCount1;
        if (d > 0) {
            ave = (CTBTotalCountStudentsClaimants * 100.0d) / d;
            summary.put(
                    sCTBPercentageOfCTB_ClaimantsStudents,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentageOfCTB_ClaimantsStudents,
                    s0);
        }
        // LHACases
        t = HBTotalCountLHACases + CTBTotalCount_LHACases;
        summary.put(
                sAllTotalCount_LHACases,
                Integer.toString(t));
        d = AllCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sAllPercentageOfAll_LHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAllPercentageOfAll_LHACases,
                    s0);
        }
        summary.put(
                sHBTotalCount_LHACases,
                Integer.toString(HBTotalCountLHACases));
        d = HBCount1;
        if (d > 0) {
            ave = (HBTotalCountLHACases * 100.0d) / d;
            summary.put(
                    sHBPercentageOfHB_LHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentageOfHB_LHACases,
                    s0);
        }
        summary.put(
                sCTBTotalCount_LHACases,
                Integer.toString(CTBTotalCount_LHACases));
        d = CTBCount1;
        if (d > 0) {
            ave = (CTBTotalCount_LHACases * 100.0d) / d;
            summary.put(
                    sCTBPercentageOfCTB_LHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentageOfCTB_LHACases,
                    s0);
        }
    }

    private void addToSummarySingleTimeEthnicity(
            int nEG,
            HashMap<String, String> summary) {
        // Ethnicity
        double percentage;
        for (int i = 1; i < nEG; i++) {
            int all;
            all = HBEthnicGroupCount[i] + CTBEthnicGroupCount[i];
            summary.put(
                    sAllTotalCountEthnicGroupClaimant[i],
                    Integer.toString(all));

            if (AllCount1 > 0) {
                percentage = (all * 100.0d) / (double) AllCount1;
                summary.put(
                        sAllPercentageEthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sAllPercentageEthnicGroupClaimant[i],
                        s0);
            }
            summary.put(
                    sHBTotalCount_EthnicGroupClaimant[i],
                    Integer.toString(HBEthnicGroupCount[i]));
            if (HBCount1 > 0) {
                percentage = (HBEthnicGroupCount[i] * 100.0d) / (double) HBCount1;
                summary.put(
                        sHBPercentage_EthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sHBPercentage_EthnicGroupClaimant[i],
                        s0);
            }
            summary.put(
                    sCTBTotalCountEthnicGroupClaimant[i],
                    Integer.toString(CTBEthnicGroupCount[i]));
            if (CTBCount1 > 0) {
                percentage = (CTBEthnicGroupCount[i] * 100.0d) / (double) CTBCount1;
                summary.put(
                        sCTBPercentageEthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sCTBPercentageEthnicGroupClaimant[i],
                        s0);
            }
        }
    }

    private void addToSummarySingleTimeTT(
            int nTT,
            HashMap<String, String> summary) {
        // Ethnicity
        double percentage;
        double d;
        int all;
        all = TotalCount_TTClaimant1[1] + TotalCount_TTClaimant1[4];
        summary.put(
                sTotalCount_SocialTTsClaimant,
                Integer.toString(all));
        d = AllCount1;
        if (AllCount1 > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_SocialTTsClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (HBCount1 > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_SocialTTsClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        all = TotalCount_TTClaimant1[3] + TotalCount_TTClaimant1[6];
        summary.put(
                sTotalCount_PrivateDeregulatedTTsClaimant,
                Integer.toString(all));
        d = AllCount1;
        if (AllCount1 > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_PrivateDeregulatedTTsClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (HBCount1 > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_PrivateDeregulatedTTsClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // TT
        for (int i = 1; i < nTT; i++) {
            all = TotalCount_TTClaimant1[i];
            summary.put(
                    sTotalCount_ClaimantsTT[i],
                    Integer.toString(all));
            d = AllCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(
                        sPercentageOfAll_ClaimantsTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            if (i == 5 || i == 7) {
                d = CTBCount1;
                if (d > 0) {
                    percentage = (all * 100.0d) / d;
                    summary.put(
                            sPercentageOfCTB_ClaimantTT[i],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(percentage),
                                    decimalPlacePrecisionForPercentage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
            } else {
                d = HBCount1;
                if (d > 0) {
                    percentage = (all * 100.0d) / d;
                    summary.put(
                            sPercentageOfHB_ClaimantTT[i],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(percentage),
                                    decimalPlacePrecisionForPercentage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
            }
        }
        summary.put(
                sAllPostcodeValidFormatCount,
                Integer.toString(HBPostcodeValidFormatCount + CTBPostcodeValidFormatCount));
        summary.put(
                sAllPostcodeValidCount,
                Integer.toString(HBPostcodeValidCount + CTBPostcodeValidCount));
        // HB
        summary.put(sHBCount1, Integer.toString(HBCount1));
        summary.put(
                sHBPostcodeValidFormatCount,
                Integer.toString(HBPostcodeValidFormatCount));
        summary.put(
                sHBPostcodeValidCount,
                Integer.toString(HBPostcodeValidCount));
        // CTB
        summary.put(sCTBCount1, Integer.toString(CTBCount1));
        summary.put(
                sCTBPostcodeValidFormatCount,
                Integer.toString(CTBPostcodeValidFormatCount));
        summary.put(
                sCTBPostcodeValidCount,
                Integer.toString(CTBPostcodeValidCount));
    }

    private void doCompare2TimesCounts(
            DW_SHBE_D_Record D_Record0,
            DW_SHBE_D_Record D_Record1,
            //            String CTBRef,
            //            HashMap<String, DW_ID> CTBRefID0,
            //            HashMap<String, DW_ID> CTBRefID1,
            //            HashMap<DW_ID, Integer> tIDByTT0,
            //            HashMap<DW_ID, Integer> tIDByTT1,
            //            HashMap<DW_ID, String> tIDByPostcode0,
            //            HashMap<DW_ID, String> tIDByPostcode1,
            String yM30v,
            String yM31v) {
        boolean isHBClaim;
        isHBClaim = DW_SHBE_Handler.isHBClaim(D_Record0);
        boolean isCTBOnlyClaim;
        isCTBOnlyClaim = DW_SHBE_Handler.isCTBOnlyClaim(D_Record0);
        //if (isHBClaim || isCTBOnlyClaim) {
        doSingleTimeCount(
                D_Record1,
                //                    CTBRef,
                //                    CTBRefID0,
                //                    tIDByTT0,
                //                    tIDByPostcode0,
                yM30v);
        String postcode0;
        postcode0 = null;
        int TT0;
        TT0 = DW_SHBE_TenancyType_Handler.iMinus999;
        boolean isValidPostcode0;
        isValidPostcode0 = false;
//            DW_ID tID0;
//            tID0 = CTBRefID0.get(CTBRef);
//            postcode0 = null;
//            TT0 = 0;
//            if (tID0 != null) {
//                postcode0 = tIDByPostcode0.get(tID0);
//                if (postcode0 != null) {
//                    isValidPostcode0 = DW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
//                }
//                TT0 = tIDByTT0.get(tID0);
//                if (TT0 == null) {
//                    TT0 = 0;
//                }
//            }
        if (D_Record0 != null) {
            postcode0 = D_Record0.getClaimantsPostcode();
            if (postcode0 != null) {
                isValidPostcode0 = DW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
            }
            TT0 = D_Record0.getTenancyType();
        }
        String postcode1;
        int TT1;
        boolean isValidPostcode1;
        isValidPostcode1 = false;
//        DW_ID tID1;
//        tID1 = CTBRefID1.get(CTBRef);
//        postcode1 = null;
//        TT1 = 0;
//        if (tID1 != null) {
//            postcode1 = tIDByPostcode1.get(tID1);
//            if (postcode1 != null) {
//                isValidPostcode1 = DW_Postcode_Handler.isValidPostcode(yM31v, postcode1);
//            }
//            TT1 = tIDByTT1.get(tID1);
//            if (TT1 == null) {
//                TT1 = 0;
//            }
//        }
        postcode1 = D_Record1.getClaimantsPostcode();
        if (postcode1 != null) {
            isValidPostcode1 = DW_Postcode_Handler.isValidPostcode(yM31v, postcode1);
        }
        TT1 = D_Record1.getTenancyType();
        if (isHBClaim) {
            doCompare2TimesHBCount(
                    TT0,
                    postcode0,
                    isValidPostcode1,
                    TT1,
                    postcode1,
                    isValidPostcode0);
        }
        if (isCTBOnlyClaim) {
            doCompare2TimesCTBCount(
                    TT0,
                    postcode0,
                    isValidPostcode1,
                    TT1,
                    postcode1,
                    isValidPostcode0);
        }
        AllTotalCount_TTChangeClaimant = HBTotalCount_TTChangeClaimant + CTBTotalCount_TTChangeClaimant;
        AllTotalCountPostcode0ValidPostcode1Valid = HBTotalCountPostcode0ValidPostcode1Valid + CTBTotalCountPostcode0ValidPostcode1Valid;
        AllTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged = HBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged + CTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged;
        AllTotalCountPostcode0ValidPostcode1ValidPostcodeChanged = HBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged + CTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged;
        AllTotalCountPostcode0ValidPostcode1NotValid = HBTotalCountPostcode0ValidPostcode1NotValid + CTBTotalCountPostcode0ValidPostcode1NotValid;
        AllTotalCountPostcode0NotValidPostcode1Valid = HBTotalCountPostcode0NotValidPostcode1Valid + CTBTotalCountPostcode0NotValidPostcode1Valid;
        AllTotalCountPostcode0NotValidPostcode1NotValid = HBTotalCountPostcode0NotValidPostcode1NotValid + CTBTotalCountPostcode0NotValidPostcode1NotValid;
        AllTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged = HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged + CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged;
        AllTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged = HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged + CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged;
    }

    private void doSingleTimeCount(
            DW_SHBE_D_Record D_Record,
            //            String CTBRef,
            //            HashMap<String, DW_ID> CTBRefID0,
            //            HashMap<DW_ID, Integer> tIDByTT0,
            //            HashMap<DW_ID, String> tIDByPostcode0,
            String yM30v) {
        int ClaimantsEthnicGroup0;
        ClaimantsEthnicGroup0 = D_Record.getClaimantsEthnicGroup();
        int TT;
        String postcode;
//        DW_ID tID0;
//        tID0 = CTBRefID0.get(CTBRef);
//        postcode = null;
//        TT = 0
//        if (tID0 != null) {
//            TT = tIDByTT0.get(tID0);
//            if (TT == null) {
//                TT = 0;
//            }
//            postcode = tIDByPostcode0.get(tID0);
//        }
        // All unfiltered counts
        TT = D_Record.getTenancyType();
        postcode = D_Record.getClaimantsPostcode();
        TotalCount_TTClaimant1[TT]++;
        int DisabilityPremiumAwarded;
        DisabilityPremiumAwarded = D_Record.getDisabilityPremiumAwarded();
        if (DisabilityPremiumAwarded == 1) {
            TotalCount_DisabilityPremiumAwardByTT[TT]++;
        }
        int SevereDisabilityPremiumAwarded;
        SevereDisabilityPremiumAwarded = D_Record.getSevereDisabilityPremiumAwarded();
        if (SevereDisabilityPremiumAwarded == 1) {
            TotalCount_SevereDisabilityPremiumAwardByTT[TT]++;
        }
        int DisabledChildPremiumAwarded;
        DisabledChildPremiumAwarded = D_Record.getDisabledChildPremiumAwarded();
        if (DisabledChildPremiumAwarded == 1) {
            TotalCount_DisabledChildPremiumAwardByTT[TT]++;
        }
        int EnhancedDisabilityPremiumAwarded;
        EnhancedDisabilityPremiumAwarded = D_Record.getEnhancedDisabilityPremiumAwarded();
        if (EnhancedDisabilityPremiumAwarded == 1) {
            TotalCount_EnhancedDisabilityPremiumAwardByTT[TT]++;
        }
        // General Household Disability Flag
        if (DisabilityPremiumAwarded == 1
                || SevereDisabilityPremiumAwarded == 1
                || DisabledChildPremiumAwarded == 1
                || EnhancedDisabilityPremiumAwarded == 1) {
            TotalCount_DisabilityAwardByTT[TT]++;
        }
        int PSI;
        PSI = D_Record.getPassportedStandardIndicator();
        AllTotalCount_PSI[PSI]++;
        AllTotalCount_PSIByTT[PSI][TT]++;
        long HouseholdSize;
        HouseholdSize = DW_SHBE_Handler.getHouseholdSize(D_Record);
        AllTotalHouseholdSize += HouseholdSize;
        int WeeklyHousingBenefitEntitlement;
        WeeklyHousingBenefitEntitlement = D_Record.getWeeklyHousingBenefitEntitlement();
        if (WeeklyHousingBenefitEntitlement > 0) {
            AllTotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
            AllTotalWeeklyHBEntitlementNonZeroCount++;
        } else {
            AllTotalWeeklyHBEntitlementZeroCount++;
        }
        int WeeklyCouncilTaxBenefitBenefitEntitlement;
        WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
        if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
            AllTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
            AllTotalCount_WeeklyCTBEntitlementNonZero++;
        } else {
            AllTotalWeeklyCTBEntitlementZeroCount++;
        }
        int WeeklyEligibleRentAmount;
        WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
        if (WeeklyEligibleRentAmount > 0) {
            AllTotalWeeklyEligibleRentAmount += WeeklyEligibleRentAmount;
            AllTotalWeeklyEligibleRentAmountNonZeroCount++;
        } else {
            AllTotalWeeklyEligibleRentAmountZeroCount++;
        }
        int WeeklyEligibleCouncilTaxAmount;
        WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
        if (WeeklyEligibleCouncilTaxAmount > 0) {
            AllTotalWeeklyEligibleCouncilTaxAmount += WeeklyEligibleCouncilTaxAmount;
            AllTotalCountWeeklyEligibleCouncilTaxAmountNonZero++;
        } else {
            AllTotalCountWeeklyEligibleCouncilTaxAmountZero++;
        }
        int ContractualRentAmount;
        ContractualRentAmount = D_Record.getContractualRentAmount();
        if (ContractualRentAmount > 0) {
            AllTotalContractualRentAmount += ContractualRentAmount;
            AllTotalContractualRentAmountNonZeroCount++;
        } else {
            AllTotalContractualRentAmountZeroCount++;
        }
        int WeeklyAdditionalDiscretionaryPayment;
        WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
        if (WeeklyAdditionalDiscretionaryPayment > 0) {
            AllTotalWeeklyAdditionalDiscretionaryPayment += WeeklyAdditionalDiscretionaryPayment;
            AllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount++;
        } else {
            AllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount++;
        }
        int WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
        WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
        if (WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability > 0) {
            AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability += WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
            AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount++;
        } else {
            AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount++;
        }
        if (DW_SHBE_Handler.isHBClaim(D_Record)) {
            HBTotalCount_PSI[PSI]++;
            HBTotalCount_PSIByTT[PSI][TT]++;
            //if (HBRef.equalsIgnoreCase(CTBRef)) {
            HBTotalHouseholdSize += HouseholdSize;
            int ClaimantsNetWeeklyIncomeFromEmployment;
            ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
            if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
                HBTotalCount_EmployedClaimants++;
            }
            int ClaimantsNetWeeklyIncomeFromSelfEmployment;
            ClaimantsNetWeeklyIncomeFromSelfEmployment = D_Record.getClaimantsNetWeeklyIncomeFromSelfEmployment();
            if (ClaimantsNetWeeklyIncomeFromSelfEmployment > 0) {
                HBTotalCountSelfEmployedClaimants++;
            }
            String ClaimantsStudentIndicator;
            ClaimantsStudentIndicator = D_Record.getClaimantsStudentIndicator();
            if (ClaimantsStudentIndicator != null) {
                if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                    HBTotalCountStudentsClaimants++;
                }
            }
            String LHARegulationsApplied;
            LHARegulationsApplied = D_Record.getLHARegulationsApplied();
            if (LHARegulationsApplied != null) {
                if (LHARegulationsApplied.equalsIgnoreCase("1")
                        || LHARegulationsApplied.equalsIgnoreCase("Yes")) {
                    HBTotalCountLHACases++;
                }
            }
            if (WeeklyHousingBenefitEntitlement > 0) {
                HBTotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
                HBTotalCount_WeeklyHBEntitlementNonZero++;
            } else {
                HBTotalCount_WeeklyHBEntitlementZero++;
            }
            WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
            if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
                HBTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
                HBTotalCount_WeeklyCTBEntitlementNonZero++;
            } else {
                HBTotalWeeklyCTBEntitlementZeroCount++;
            }
            WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
            if (WeeklyEligibleRentAmount > 0) {
                HBTotalWeeklyEligibleRentAmount += WeeklyEligibleRentAmount;
                HBTotalWeeklyEligibleRentAmountNonZeroCount++;
            } else {
                HBTotalWeeklyEligibleRentAmountZeroCount++;
            }
            WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
            if (WeeklyEligibleCouncilTaxAmount > 0) {
                HBTotalWeeklyEligibleCouncilTaxAmount += WeeklyEligibleCouncilTaxAmount;
                HBTotalCountWeeklyEligibleCouncilTaxAmountNonZero++;
            } else {
                HBTotalCountWeeklyEligibleCouncilTaxAmountZero++;
            }
            ContractualRentAmount = D_Record.getContractualRentAmount();
            if (ContractualRentAmount > 0) {
                HBTotalContractualRentAmount += ContractualRentAmount;
                HBTotalContractualRentAmountNonZeroCount++;
            } else {
                HBTotalContractualRentAmountZeroCount++;
            }
            WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
            if (WeeklyAdditionalDiscretionaryPayment > 0) {
                HBTotalWeeklyAdditionalDiscretionaryPayment += WeeklyAdditionalDiscretionaryPayment;
                HBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount++;
            } else {
                HBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount++;
            }
            WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
            if (WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability > 0) {
                HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability += WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
                HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount++;
            } else {
                HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount++;
            }
            doSingleTimeHBCount(
                    ClaimantsEthnicGroup0,
                    postcode,
                    yM30v);
        }
        if (DW_SHBE_Handler.isCTBOnlyClaim(D_Record)) {
            CTBTotalCount_PSI[PSI]++;
            CTBTotalCount_PSIByTT[PSI][TT]++;
            CTBTotalHouseholdSize += HouseholdSize;
            int ClaimantsNetWeeklyIncomeFromEmployment;
            ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
            if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
                CTBTotalCountEmployedClaimants++;
            }
            int ClaimantsNetWeeklyIncomeFromSelfEmployment;
            ClaimantsNetWeeklyIncomeFromSelfEmployment = D_Record.getClaimantsNetWeeklyIncomeFromSelfEmployment();
            if (ClaimantsNetWeeklyIncomeFromSelfEmployment > 0) {
                CTBTotalCountSelfEmployedClaimants++;
            }
            String ClaimantsStudentIndicator;
            ClaimantsStudentIndicator = D_Record.getClaimantsStudentIndicator();
            if (ClaimantsStudentIndicator != null) {
                if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                    CTBTotalCountStudentsClaimants++;
                }
            }
            String LHARegulationsApplied;
            LHARegulationsApplied = D_Record.getLHARegulationsApplied();
            if (LHARegulationsApplied != null) {
                if (LHARegulationsApplied.equalsIgnoreCase("1")) {
                    CTBTotalCount_LHACases++;
                }
            }
            if (WeeklyHousingBenefitEntitlement > 0) {
                CTBTotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
                CTBTotalCount_WeeklyHBEntitlementNonZero++;
            } else {
                CTBTotalWeeklyHBEntitlementZeroCount++;
            }
            WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
            if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
                CTBTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
                CTBTotalCount_WeeklyCTBEntitlementNonZero++;
            } else {
                CTBTotalWeeklyCTBEntitlementZeroCount++;
            }
            WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
            if (WeeklyEligibleRentAmount > 0) {
                CTBTotalWeeklyEligibleRentAmount += WeeklyEligibleRentAmount;
                CTBTotalWeeklyEligibleRentAmountNonZeroCount++;
            } else {
                CTBTotalWeeklyEligibleRentAmountZeroCount++;
            }
            WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
            if (WeeklyEligibleCouncilTaxAmount > 0) {
                CTBTotalWeeklyEligibleCouncilTaxAmount += WeeklyEligibleCouncilTaxAmount;
                CTBTotalCountWeeklyEligibleCouncilTaxAmountNonZero++;
            } else {
                CTBTotalCountWeeklyEligibleCouncilTaxAmountZero++;
            }
            ContractualRentAmount = D_Record.getContractualRentAmount();
            if (ContractualRentAmount > 0) {
                CTBTotalContractualRentAmount += ContractualRentAmount;
                CTBTotalContractualRentAmountNonZeroCount++;
            } else {
                CTBTotalContractualRentAmountZeroCount++;
            }
            WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
            if (WeeklyAdditionalDiscretionaryPayment > 0) {
                CTBTotalWeeklyAdditionalDiscretionaryPayment += WeeklyAdditionalDiscretionaryPayment;
                CTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount++;
            } else {
                CTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount++;
            }
            WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
            if (WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability > 0) {
                CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability += WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
                CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount++;
            } else {
                CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount++;
            }
            doSingleTimeCTBCount(
                    ClaimantsEthnicGroup0,
                    postcode,
                    yM30v);
        }
        AllCount1 = HBCount1 + CTBCount1;
    }

    private void doSingleTimeRentArrearsCount(DW_UOReport_Record UORec) {
        Double totalRA;
        totalRA = UORec.getTotalRentArrears();
        if (totalRA != null) {
            Total_RentArrears += totalRA;
            TotalCount_RentArrears += 1.0d;
            if (totalRA > 0.0d) {
                TotalCount_RentArrearsNonZero++;
            } else {
                TotalCount_RentArrearsZero++;
            }
        }
    }

    private void doCompare2TimesHBCount(
            Integer tenancyType0,
            String postcode0,
            boolean isValidPostcode1,
            Integer tenancyType1,
            String postcode1,
            boolean isValidPostcode0) {
        if (isValidPostcode0) {
            if (isValidPostcode1) {
                HBTotalCountPostcode0ValidPostcode1Valid++;
                if (postcode0.equalsIgnoreCase(postcode1)) {
                    HBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged++;
                } else {
                    HBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged++;
                }
            } else {
                HBTotalCountPostcode0ValidPostcode1NotValid++;
            }
        } else {
            if (isValidPostcode1) {
                HBTotalCountPostcode0NotValidPostcode1Valid++;
            } else {
                HBTotalCountPostcode0NotValidPostcode1NotValid++;
                if (postcode0 == null) {
                    HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                } else {
                    if (postcode1 == null) {
                        HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                    } else {
                        if (postcode0.equalsIgnoreCase(postcode1)) {
                            HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                        } else {
                            HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged++;
                        }
                    }
                }
            }
        }
        if (tenancyType0.compareTo(tenancyType1) != 0) {
            HBTotalCount_TTChangeClaimant++;
            if ((tenancyType0 == 5
                    || tenancyType0 == 7)
                    && (tenancyType1 == 1
                    || tenancyType1 == 2
                    || tenancyType1 == 3
                    || tenancyType1 == 4
                    || tenancyType1 == 6
                    || tenancyType1 == 8
                    || tenancyType1 == 9)) {
                TotalCount_CTBTTsToHBTTs++;
            }
            if ((tenancyType0 == 1
                    || tenancyType0 == 2
                    || tenancyType0 == 3
                    || tenancyType0 == 4
                    || tenancyType0 == 6
                    || tenancyType0 == 8
                    || tenancyType0 == 9)
                    && (tenancyType1 == 1
                    || tenancyType1 == 2
                    || tenancyType1 == 3
                    || tenancyType1 == 4
                    || tenancyType1 == 6
                    || tenancyType1 == 8
                    || tenancyType1 == 9)) {
                TotalCount_HBTTsToHBTTs++;
            }
            if (tenancyType0 == 1 || tenancyType0 == 4) {
                if (tenancyType1 == 3 || tenancyType0 == 6) {
                    TotalCount_SocialTTsToPrivateDeregulatedTTs++;
                    if (tenancyType0 == 1) {
                        TotalCount_TT1ToPrivateDeregulatedTTs++;
                    }
                    if (tenancyType0 == 4) {
                        TotalCount_TT4ToPrivateDeregulatedTTs++;
                    }
                }
            }
            if (tenancyType0 == 3 || tenancyType0 == 6) {
                if (tenancyType1 == 1 || tenancyType1 == 4) {
                    TotalCount_PrivateDeregulatedTTsToSocialTTs++;
                    if (tenancyType1 == 1) {
                        TotalCount_PrivateDeregulatedTTsToTT1++;
                    }
                    if (tenancyType1 == 4) {
                        TotalCount_PrivateDeregulatedTTsToTT4++;
                    }
                }
            }
        }
        if ((tenancyType0 == 1 && tenancyType1 == 1)
                || (tenancyType0 == 1 && tenancyType1 == 4)
                || (tenancyType0 == 4 && tenancyType1 == 1)
                || (tenancyType0 == 4 && tenancyType1 == 4)) {
            if (isValidPostcode0 && isValidPostcode1) {
                if (!postcode0.equalsIgnoreCase(postcode1)) {
                    TotalCount_PostcodeChangeWithinSocialTTs++;
                    if (tenancyType0 == 1 && tenancyType1 == 1) {
                        TotalCount_PostcodeChangeWithinTT1++;
                    }
                    if (tenancyType0 == 4 && tenancyType1 == 4) {
                        TotalCount_PostcodeChangeWithinTT4++;
                    }
                }
            }
        }
        if ((tenancyType0 == 3 || tenancyType0 == 6) && (tenancyType1 == 3 || tenancyType1 == 6)) {
            if (isValidPostcode0 && isValidPostcode1) {
                if (!postcode0.equalsIgnoreCase(postcode1)) {
                    TotalCount_PostcodeChangeWithinPrivateDeregulatedTTs++;
                }
            }
        }
    }

    /**
     *
     * @param tEG The Ethnic Group
     * @param tTT The Tenancy Type
     * @param tP The Postcode
     * @param yM3v They yM3 for postcode lookup validity
     */
    private void doSingleTimeHBCount(
            int tEG,
            String tP,
            String yM3v) {
        HBCount1++;
        HBEthnicGroupCount[tEG]++;
        if (tP != null) {
            boolean isValidPostcodeFormat;
            isValidPostcodeFormat = Generic_UKPostcode_Handler.isValidPostcodeForm(tP);
            boolean isValidPostcode;
            isValidPostcode = DW_Postcode_Handler.isValidPostcode(yM3v, tP);
            if (isValidPostcodeFormat) {
                HBPostcodeValidFormatCount++;
            }
            if (isValidPostcode) {
                HBPostcodeValidCount++;
            }
        }
    }

    private void doCompare2TimesCTBCount(
            Integer tenancyType0,
            String postcode0,
            boolean isValidPostcode1,
            Integer tenancyType1,
            String postcode1,
            boolean isValidPostcode0) {
        if (isValidPostcode0) {
            if (isValidPostcode1) {
                CTBTotalCountPostcode0ValidPostcode1Valid++;
                if (postcode0.equalsIgnoreCase(postcode1)) {
                    CTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged++;
                } else {
                    CTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged++;
                }
            } else {
                CTBTotalCountPostcode0ValidPostcode1NotValid++;
            }
        } else {
            if (isValidPostcode1) {
                CTBTotalCountPostcode0NotValidPostcode1Valid++;
            } else {
                CTBTotalCountPostcode0NotValidPostcode1NotValid++;
                if (postcode0 == null) {
                    CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                } else {
                    if (postcode1 == null) {
                        CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                    } else {
                        if (postcode0.equalsIgnoreCase(postcode1)) {
                            CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                        } else {
                            CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged++;
                        }
                    }
                }
            }
        }
        if (tenancyType0.compareTo(tenancyType1) != 0) {
            CTBTotalCount_TTChangeClaimant++;
            if ((tenancyType0 == 1
                    || tenancyType0 == 2
                    || tenancyType0 == 3
                    || tenancyType0 == 4
                    || tenancyType0 == 6
                    || tenancyType0 == 8
                    || tenancyType0 == 9)
                    && (tenancyType1 == 5
                    || tenancyType1 == 7)) {
                TotalCount_HBTTsToCTBTTs++;
            }
            if ((tenancyType0 == 1 || tenancyType0 == 4)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                TotalCountSocialTTsToCTBTTs++;
            }
            if (tenancyType0 == 5 || tenancyType0 == 7) {
                if (tenancyType1 == 1 || tenancyType1 == 4) {
                    TotalCount_CTBTTsToSocialTTs++;
                    if (tenancyType1 == 1) {
                        TotalCount_CTBTTsToTT1++;
                    }
                    if (tenancyType1 == 4) {
                        TotalCount_CTBTTsToTT4++;
                    }
                }
            }
            if ((tenancyType0 == 3 || tenancyType0 == 6)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                TotalCount_PrivateDeregulatedTTsToCTBTTs++;
            }
            if ((tenancyType0 == 5 || tenancyType0 == 7)
                    && (tenancyType1 == 3 || tenancyType1 == 6)) {
                TotalCount_CTBTTsToPrivateDeregulatedTTs++;
            }
            if (tenancyType0 == 1 && tenancyType1 == 4) {
                TotalCount_TT1ToTT4++;
            }
            if (tenancyType0 == 4 && tenancyType1 == 1) {
                TotalCount_TT4ToTT1++;
            }
        }
    }

    /**
     *
     * @param tEG The Ethnic Group
     * @param tTT The Tenancy Type
     * @param tP The Postcode
     * @param yM3v They yM3 for postcode lookup validity
     */
    private void doSingleTimeCTBCount(
            int tEG,
            String tP,
            String yM3v) {
        CTBCount1++;
        CTBEthnicGroupCount[tEG]++;
        if (tP != null) {
            boolean isValidPostcodeFormat;
            isValidPostcodeFormat = Generic_UKPostcode_Handler.isValidPostcodeForm(tP);
            boolean isValidPostcode;
            isValidPostcode = DW_Postcode_Handler.isValidPostcode(yM3v, tP);
            if (isValidPostcodeFormat) {
                CTBPostcodeValidFormatCount++;
            }
            if (isValidPostcode) {
                CTBPostcodeValidCount++;
            }
        }
    }

    /**
     *
     * @param SHBEFilenames
     * @param paymentType
     * @param include
     * @param forceNewSummaries
     * @param nTT
     * @param nEG
     * @param nPSI
     * @param handleOutOfMemoryError
     * @return
     */
    public TreeMap<String, HashMap<String, String>> getSummaryTable(
            String[] SHBEFilenames,
            ArrayList<Integer> include,
            boolean forceNewSummaries,
            String paymentType,
            //underOccupiedData,
            int nTT,
            int nEG,
            int nPSI,
            boolean handleOutOfMemoryError
    ) {
        AllCount0 = null;
        HBCount0 = null;
        CTBCount0 = null;
        initCounts(nTT, nEG, nPSI);
        // Initialise summaryTable
        TreeMap<String, HashMap<String, String>> result;
        result = new TreeMap<String, HashMap<String, String>>();
        // All
        Iterator<Integer> includeIte;
        includeIte = include.iterator();
        while (includeIte.hasNext()) {
            int i;
            i = includeIte.next();
            HashMap<String, String> summary;
            summary = new HashMap<String, String>();
            String key;
            key = DW_SHBE_Handler.getYearMonthNumber(SHBEFilenames[i]);
            result.put(key, summary);
        }
        includeIte = include.iterator();
        int i;
        i = includeIte.next();
        // Load first data
        String filename00;
        filename00 = null;
        String filename0;
        filename0 = SHBEFilenames[i];
        String yM30;
        yM30 = DW_SHBE_Handler.getYM3(filename0);
        System.out.println("Load " + filename0);
        DW_SHBE_Collection tSHBEData0;
        tSHBEData0 = new DW_SHBE_Collection(filename0, paymentType);
        // These could be returned to save time recreating them for other includes.
        // This would involve feeding them in to the method too per se.
        String yM30v;
        yM30v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM30);
        String key;
        key = DW_SHBE_Handler.getYearMonthNumber(SHBEFilenames[i]);
        HashMap<String, String> summary;
        summary = result.get(key);
        TreeMap<String, HashSet<DW_PersonID>> tClaimantIDs;
        tClaimantIDs = new TreeMap<String, HashSet<DW_PersonID>>();
        TreeMap<String, HashSet<DW_PersonID>> AllIDs;
        AllIDs = new TreeMap<String, HashSet<DW_PersonID>>();
        TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes;
        tClaimantIDPostcodes = new TreeMap<String, HashSet<ID_PostcodeID>>();
        TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTTs;
        tClaimantIDTTs = new TreeMap<String, HashSet<ID_TenancyType>>();
        TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTTs;
        tClaimantIDPostcodeTTs = new TreeMap<String, HashSet<ID_TenancyType_PostcodeID>>();
        TreeMap<String, DW_SHBE_Record> tDRecords0;
        tDRecords0 = tSHBEData0.getRecords();
        HashSet<DW_PersonID> tClaimantIDs0;
        tClaimantIDs0 = tSHBEData0.getClaimantIDs();
        tClaimantIDs.put(yM30, tClaimantIDs0);
        HashSet<DW_PersonID> AllIDs0;
        AllIDs0 = tSHBEData0.getAllIDs();
        AllIDs.put(yM30, AllIDs0);
        HashMap<DW_ID, String> tIDByPostcode0;
        tIDByPostcode0 = tSHBEData0.getClaimantIDToPostcodeLookup();
        //tIDByPostcode0 = loadIDByPostcode(loadData, filename, i);
        HashMap<DW_ID, Integer> tIDByTT0;
        tIDByTT0 = tSHBEData0.getClaimantIDToTenancyTypeLookup();
        //tIDByTT0 = loadIDByTT(loadData, filename, i);
        HashMap<String, DW_ID> CTBRefToIDLookup0;
        CTBRefToIDLookup0 = tSHBEData0.getCTBRefToClaimantIDLookup();
        //tCTBRefID0 = loadCTBRefByID(loadData, filename, i);        
        HashMap<String, Integer> tLoadSummary;
        tLoadSummary = tSHBEData0.getLoadSummary();
        addToSummary(summary, tLoadSummary);
        HashSet<ID_PostcodeID> tClaimantIDPostcodes0;
        tClaimantIDPostcodes0 = tSHBEData0.getClaimantIDAndPostcodeSet();
        tClaimantIDPostcodes.put(yM30, tClaimantIDPostcodes0);
        HashSet<ID_TenancyType> tClaimantIDTT0;
        tClaimantIDTT0 = tSHBEData0.getClaimantIDAndTenancyTypeSet();
        tClaimantIDTTs.put(yM30, tClaimantIDTT0);
        HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTTs0;
        tClaimantIDPostcodeTTs0 = tSHBEData0.getClaimantIDAndPostcodeAndTenancyTypeSet();
        tClaimantIDPostcodeTTs.put(yM30, tClaimantIDPostcodeTTs0);
        // All
        initCounts(nTT, nEG, nPSI);
        Iterator<String> ite;
        ite = CTBRefToIDLookup0.keySet().iterator();
        while (ite.hasNext()) {
            String CTBRef;
            CTBRef = ite.next();
            DW_SHBE_D_Record D_Record;
            D_Record = tDRecords0.get(CTBRef).getDRecord();
            doSingleTimeCount(
                    D_Record,
                    //                    CTBRef,
                    //                    CTBRefToIDLookup0,
                    //                    tIDByTT0,
                    //                    tIDByPostcode0,
                    yM30v);
        }
        summary.put(sSHBEFilename1, filename0); // This looks odd but is right!
        HashMap<String, BigDecimal> incomeAndRentSummary;
        incomeAndRentSummary = DW_SHBE_Handler.getIncomeAndRentSummary(
                tSHBEData0,
                filename0,
                paymentType,
                null,
                false,
                forceNewSummaries);
        addToSummarySingleTimeIncomeAndRent(
                summary,
                incomeAndRentSummary);
        addToSummarySingleTime(nTT, nEG, nPSI, summary);
        while (includeIte.hasNext()) {
            i = includeIte.next();
            String filename1;
            filename1 = SHBEFilenames[i];
            String yM31;
            yM31 = DW_SHBE_Handler.getYM3(filename1);
            String yM31v;
            yM31v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
            // Load next data
            System.out.println("Load " + filename1);
            DW_SHBE_Collection tSHBEData1;
            tSHBEData1 = new DW_SHBE_Collection(filename1, paymentType);
            TreeMap<String, DW_SHBE_Record> tDRecords1;
            tDRecords1 = tSHBEData1.getRecords();
            HashSet<DW_PersonID> tClaimantIDs1;
            tClaimantIDs1 = tSHBEData1.getClaimantIDs();
            tClaimantIDs.put(yM31, tClaimantIDs1);
            HashSet<DW_PersonID> AllIDs1;
            AllIDs1 = tSHBEData1.getAllIDs();
            AllIDs.put(yM31, AllIDs1);
            HashMap<DW_ID, String> tIDByPostcode1;
            tIDByPostcode1 = tSHBEData1.getClaimantIDToPostcodeLookup();
            HashMap<DW_ID, Integer> tIDByTT1;
            tIDByTT1 = tSHBEData1.getClaimantIDToTenancyTypeLookup();
            HashMap<String, DW_ID> CTBRefToIDLookup1;
            CTBRefToIDLookup1 = tSHBEData1.getCTBRefToClaimantIDLookup();
            HashSet<ID_PostcodeID> tClaimantIDPostcodes1;
            tClaimantIDPostcodes1 = tSHBEData1.getClaimantIDAndPostcodeSet();
            tClaimantIDPostcodes.put(yM31, tClaimantIDPostcodes1);
            HashSet<ID_TenancyType> tClaimantIDTT1;
            tClaimantIDTT1 = tSHBEData1.getClaimantIDAndTenancyTypeSet();
            tClaimantIDTTs.put(yM31, tClaimantIDTT1);
            HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTTs1;
            tClaimantIDPostcodeTTs1 = tSHBEData1.getClaimantIDAndPostcodeAndTenancyTypeSet();
            tClaimantIDPostcodeTTs.put(yM31, tClaimantIDPostcodeTTs1);

            key = DW_SHBE_Handler.getYearMonthNumber(filename1);
            summary = result.get(key);
            // Counters
            tLoadSummary = (HashMap<String, Integer>) tSHBEData1.getLoadSummary();
            addToSummary(summary, tLoadSummary);
            HashMap<String, BigDecimal> incomeAndRentSummary1;
            incomeAndRentSummary1 = DW_SHBE_Handler.getIncomeAndRentSummary(
                    tSHBEData1,
                    filename1,
                    paymentType,
                    null,
                    false,
                    forceNewSummaries);
            addToSummarySingleTimeIncomeAndRent(
                    summary,
                    incomeAndRentSummary1);
            initCounts(nTT, nEG, nPSI);
            // Loop over CTBRefToNationalInsuranceNumberIDLookup1
            ite = CTBRefToIDLookup1.keySet().iterator();
            while (ite.hasNext()) {
                String CTBRef;
                CTBRef = ite.next();
                DW_SHBE_Record Record0;
                Record0 = tDRecords0.get(CTBRef);
                DW_SHBE_D_Record D_Record0;
                D_Record0 = null;
                if (Record0 != null) {
                    D_Record0 = Record0.getDRecord();
                }
                DW_SHBE_D_Record D_Record1;
                D_Record1 = tDRecords1.get(CTBRef).getDRecord();
                doCompare2TimesCounts(
                        D_Record0,
                        D_Record1,
                        //                        CTBRef,
                        //                        CTBRefToIDLookup0,
                        //                        CTBRefToIDLookup1,
                        //                        tIDByTT0,
                        //                        tIDByTT1,
                        //                        tIDByPostcode0,
                        //                        tIDByPostcode1,
                        yM30v,
                        yM31v);
            }
            summary.put(sSHBEFilename00, filename00);
            summary.put(sSHBEFilename0, filename0);
            summary.put(sSHBEFilename1, filename1);

            // All
            addToSummaryCompare2Times(nTT, nEG, nPSI, summary);
            doCompare3TimesCounts(
                    summary,
                    nTT,
                    SHBEFilenames,
                    include,
                    yM31,
                    tClaimantIDPostcodes,
                    tClaimantIDPostcodes1,
                    tClaimantIDTTs,
                    tClaimantIDTT1,
                    tClaimantIDPostcodeTTs,
                    tClaimantIDPostcodeTTs1);
            filename00 = filename0;
            filename0 = filename1;
            tSHBEData0 = tSHBEData1;
            tDRecords0 = tDRecords1;
            tIDByPostcode0 = tIDByPostcode1;
            tIDByTT0 = tIDByTT1;
            CTBRefToIDLookup0 = CTBRefToIDLookup1;
            yM30 = yM31;
            yM30v = yM31v;
            AllCount0 = null;
            HBCount0 = null;
            CTBCount0 = null;
            // Not used at present. incomeAndRentSummary0 = incomeAndRentSummary1;
        }
        return result;
    }

    public void addToSummary(
            HashMap<String, String> summary,
            HashMap<String, Integer> tLoadSummary) {
        summary.put(
                DW_SHBE_Collection.sLineCount,
                Integer.toString(tLoadSummary.get(DW_SHBE_Collection.sLineCount)));
//        summary.put(
//                "LineNotLoadedCount",
//                Integer.toString(tLoadSummary.get("RecordIDsNotLoaded.size()")));
        summary.put(
                "LineNotLoadedCount",
                Integer.toString(tLoadSummary.get(DW_SHBE_Collection.sCountRecordIDsNotLoaded)));
        summary.put(
                DW_SHBE_Collection.sCountDRecords,
                Integer.toString(tLoadSummary.get(DW_SHBE_Collection.sCountDRecords)));
        summary.put(
                DW_SHBE_Collection.sCountSRecords,
                Integer.toString(tLoadSummary.get(DW_SHBE_Collection.sCountSRecords)));
        summary.put(
                DW_SHBE_Collection.sCountOfSRecordsWithoutDRecord,
                Integer.toString(tLoadSummary.get(DW_SHBE_Collection.sCountOfSRecordsWithoutDRecord)));
        summary.put(
                DW_SHBE_Collection.sCountUniqueClaimants,
                Integer.toString(tLoadSummary.get(DW_SHBE_Collection.sCountUniqueClaimants)));
        summary.put(
                DW_SHBE_Collection.sCountUniquePartners,
                Integer.toString(tLoadSummary.get(DW_SHBE_Collection.sCountUniquePartners)));
        summary.put(
                DW_SHBE_Collection.sCountUniqueDependents,
                Integer.toString(tLoadSummary.get(DW_SHBE_Collection.sCountUniqueDependents)));
        summary.put(
                DW_SHBE_Collection.sCountUniqueNonDependents,
                Integer.toString(tLoadSummary.get(DW_SHBE_Collection.sCountUniqueNonDependents)));
        summary.put(
                DW_SHBE_Collection.sCountUniqueIndividuals,
                Integer.toString(tLoadSummary.get(DW_SHBE_Collection.sCountUniqueIndividuals)));
        summary.put(sAllCount0, Integer.toString(AllCount1));
        summary.put(sHBCount0, Integer.toString(HBCount1));
        summary.put(sCTBCount0, Integer.toString(CTBCount1));
    }

    public void doCompare3TimesCounts(
            HashMap<String, String> summary,
            int nTT,
            String[] SHBEFilenames,
            ArrayList<Integer> include,
            String yM31,
            TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes,
            HashSet<ID_PostcodeID> tClaimantIDPostcodes1,
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTTs,
            HashSet<ID_TenancyType> tClaimantIDTTs1,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTTs,
            HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTTs1
    ) {
        Integer[] counts;
        counts = getCountsIDPostcode(
                SHBEFilenames,
                include,
                yM31,
                tClaimantIDPostcodes,
                tClaimantIDPostcodes1);
        summary.put(
                SamePostcodeIIIString,
                "" + counts[0]);
        summary.put(
                SamePostcodeIOIString,
                "" + counts[1]);
        summary.put(
                SamePostcodeOIOString,
                "" + counts[2]);
        Object[] countsIDTT = getCountsIDTT(
                nTT,
                SHBEFilenames,
                include,
                yM31,
                tClaimantIDTTs,
                tClaimantIDTTs1);
        Integer[] mainCounts;
        mainCounts = (Integer[]) countsIDTT[0];
        Integer[] IIITTCounts;
        Integer[] IOITTCounts;
        Integer[] OIOTTCounts;
        IIITTCounts = (Integer[]) countsIDTT[1];
        IOITTCounts = (Integer[]) countsIDTT[2];
        OIOTTCounts = (Integer[]) countsIDTT[3];
        if (mainCounts == null) { // If mainCounts is null, then so are other counts
            summary.put(
                    SameTenancyIIIString,
                    null);
            summary.put(
                    SameTenancyIOIString,
                    null);
            summary.put(
                    SameTenancyOIOString,
                    null);
            for (int TT = 1; TT < nTT; TT++) {
                summary.put(
                        SameTenancyIIITTString[TT],
                        null);
                summary.put(
                        SameTenancyIOITTString[TT],
                        null);
                summary.put(
                        SameTenancyOIOTTString[TT],
                        null);
            }
        } else {
            summary.put(
                    SameTenancyIIIString,
                    "" + mainCounts[0]);
            summary.put(
                    SameTenancyIOIString,
                    "" + mainCounts[1]);
            summary.put(
                    SameTenancyOIOString,
                    "" + mainCounts[2]);
            for (int TT = 1; TT < nTT; TT++) {
                summary.put(
                        SameTenancyIIITTString[TT],
                        "" + IIITTCounts[TT]);
                summary.put(
                        SameTenancyIOITTString[TT],
                        "" + IOITTCounts[TT]);
                summary.put(
                        SameTenancyOIOTTString[TT],
                        "" + OIOTTCounts[TT]);
            }
        }
        Object[] countsIDPostcodeTT = getCountsIDPostcodeTT(
                nTT,
                SHBEFilenames,
                include,
                yM31,
                tClaimantIDPostcodeTTs,
                tClaimantIDPostcodeTTs1);
        mainCounts = (Integer[]) countsIDPostcodeTT[0];
        IIITTCounts = (Integer[]) countsIDPostcodeTT[1];
        IOITTCounts = (Integer[]) countsIDPostcodeTT[2];
        OIOTTCounts = (Integer[]) countsIDPostcodeTT[3];
        if (mainCounts == null) { // If mainCounts is null, then so are other counts
            summary.put(
                    SameTenancyAndPostcodeIIIString,
                    null);
            summary.put(
                    SameTenancyAndPostcodeIOIString,
                    null);
            summary.put(
                    SameTenancyAndPostcodeOIOString,
                    null);
            for (int TT = 1; TT < nTT; TT++) {
                summary.put(
                        SameTenancyAndPostcodeIIITTString[TT],
                        null);
                summary.put(
                        SameTenancyAndPostcodeIOITTString[TT],
                        null);
                summary.put(
                        SameTenancyAndPostcodeOIOTTString[TT],
                        null);
            }
        } else {
            summary.put(
                    SameTenancyAndPostcodeIIIString,
                    "" + mainCounts[0]);
            summary.put(
                    SameTenancyAndPostcodeIOIString,
                    "" + mainCounts[1]);
            summary.put(
                    SameTenancyAndPostcodeOIOString,
                    "" + mainCounts[2]);
            for (int TT = 1; TT < nTT; TT++) {
                summary.put(
                        SameTenancyAndPostcodeIIITTString[TT],
                        "" + IIITTCounts[TT]);
                summary.put(
                        SameTenancyAndPostcodeIOITTString[TT],
                        "" + IOITTCounts[TT]);
                summary.put(
                        SameTenancyAndPostcodeOIOTTString[TT],
                        "" + OIOTTCounts[TT]);
            }
        }
    }

    public void doCompare3TimesCounts(
            HashMap<String, String> summary,
            int nTT,
            String[] SHBEFilenames,
            ArrayList<Integer> include,
            String yM31,
            TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes,
            HashSet<ID_PostcodeID> tClaimantIDPostcodes1,
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTTs,
            HashSet<ID_TenancyType> tClaimantIDTT1,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTTs,
            HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTTs1,
            HashMap<String, DW_SHBE_D_Record> D_Records,
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet,
            DW_UnderOccupiedReport_Set RSLUnderOccupiedSet
    ) {
        Integer[] counts;
        counts = getCountsIDPostcode(
                SHBEFilenames,
                include,
                yM31,
                tClaimantIDPostcodes,
                tClaimantIDPostcodes1);
        summary.put(
                SamePostcodeIIIString,
                "" + counts[0]);
        summary.put(
                SamePostcodeIOIString,
                "" + counts[1]);
        summary.put(
                SamePostcodeOIOString,
                "" + counts[2]);
        Object[] countsIDTT = getCountsIDTT(
                nTT,
                SHBEFilenames,
                include,
                yM31,
                tClaimantIDTTs,
                tClaimantIDTT1);
        Integer[] mainCounts;
        mainCounts = (Integer[]) countsIDTT[0];
        Integer[] IIITTCounts;
        Integer[] IOITTCounts;
        Integer[] OIOTTCounts;
        IIITTCounts = (Integer[]) countsIDTT[1];
        IOITTCounts = (Integer[]) countsIDTT[2];
        OIOTTCounts = (Integer[]) countsIDTT[3];
        if (mainCounts == null) { // If mainCounts is null, then so are other counts
            summary.put(
                    SameTenancyIIIString,
                    null);
            summary.put(
                    SameTenancyIOIString,
                    null);
            summary.put(
                    SameTenancyOIOString,
                    null);
            for (int TT = 1; TT < nTT; TT++) {
                summary.put(
                        SameTenancyIIITTString[TT],
                        null);
                summary.put(
                        SameTenancyIOITTString[TT],
                        null);
                summary.put(
                        SameTenancyOIOTTString[TT],
                        null);
            }
        } else {
            summary.put(
                    SameTenancyIIIString,
                    "" + mainCounts[0]);
            summary.put(
                    SameTenancyIOIString,
                    "" + mainCounts[1]);
            summary.put(
                    SameTenancyOIOString,
                    "" + mainCounts[2]);
            for (int TT = 1; TT < nTT; TT++) {
                summary.put(
                        SameTenancyIIITTString[TT],
                        "" + IIITTCounts[TT]);
                summary.put(
                        SameTenancyIOITTString[TT],
                        "" + IOITTCounts[TT]);
                summary.put(
                        SameTenancyOIOTTString[TT],
                        "" + OIOTTCounts[TT]);
            }
        }
        Object[] countsIDPostcodeTT = getCountsIDPostcodeTT(
                nTT,
                SHBEFilenames,
                include,
                yM31,
                tClaimantIDPostcodeTTs,
                tClaimantIDPostcodeTTs1);
        mainCounts = (Integer[]) countsIDPostcodeTT[0];
        IIITTCounts = (Integer[]) countsIDPostcodeTT[1];
        IOITTCounts = (Integer[]) countsIDPostcodeTT[2];
        OIOTTCounts = (Integer[]) countsIDPostcodeTT[3];
        if (mainCounts == null) { // If mainCounts is null, then so are other counts
            summary.put(
                    SameTenancyAndPostcodeIIIString,
                    null);
            summary.put(
                    SameTenancyAndPostcodeIOIString,
                    null);
            summary.put(
                    SameTenancyAndPostcodeOIOString,
                    null);
            for (int TT = 1; TT < nTT; TT++) {
                summary.put(
                        SameTenancyAndPostcodeIIITTString[TT],
                        null);
                summary.put(
                        SameTenancyAndPostcodeIOITTString[TT],
                        null);
                summary.put(
                        SameTenancyAndPostcodeOIOTTString[TT],
                        null);
            }
        } else {
            summary.put(
                    SameTenancyAndPostcodeIIIString,
                    "" + mainCounts[0]);
            summary.put(
                    SameTenancyAndPostcodeIOIString,
                    "" + mainCounts[1]);
            summary.put(
                    SameTenancyAndPostcodeOIOString,
                    "" + mainCounts[2]);
            for (int TT = 1; TT < nTT; TT++) {
                summary.put(
                        SameTenancyAndPostcodeIIITTString[TT],
                        "" + IIITTCounts[TT]);
                summary.put(
                        SameTenancyAndPostcodeIOITTString[TT],
                        "" + IOITTCounts[TT]);
                summary.put(
                        SameTenancyAndPostcodeOIOTTString[TT],
                        "" + OIOTTCounts[TT]);
            }
        }
    }

    /**
     *
     * @param SHBEFilenames
     * @param include
     * @param forceNewSummaries
     * @param paymentType
     * @param nTT
     * @param nEG
     * @param nPSI
     * @param underOccupiedData
     * @param tDW_PersonIDToIDLookup
     * @param tPostcodeToPostcodeIDLookup
     * @param handleOutOfMemoryError
     * @return
     */
    public TreeMap<String, HashMap<String, String>> getSummaryTable(
            TreeMap<String, HashMap<String, String>> summaryTableAll,
            String[] SHBEFilenames,
            ArrayList<Integer> include,
            boolean forceNewSummaries,
            String paymentType,
            int nTT,
            int nEG,
            int nPSI,
            Object[] underOccupiedData,
            HashMap<DW_PersonID, DW_ID> tDW_PersonIDToIDLookup,
            HashMap<String, DW_ID> tPostcodeToPostcodeIDLookup,
            boolean handleOutOfMemoryError
    ) {
        AllCount0 = null;
        HBCount0 = null;
        CTBCount0 = null;
        initCounts(nTT, nEG, nPSI);
        // Initialise result
        TreeMap<String, HashMap<String, String>> result;
        result = new TreeMap<String, HashMap<String, String>>();
        // Initialise UO
        TreeMap<String, DW_UnderOccupiedReport_Set> councilUnderOccupiedSets = null;
        TreeMap<String, DW_UnderOccupiedReport_Set> RSLUnderOccupiedSets = null;
        councilUnderOccupiedSets = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[0];
        RSLUnderOccupiedSets = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[1];
        // Initialise first data
        Iterator<Integer> includeIte;
        DW_SHBE_Collection tSHBEData1 = null;
        includeIte = include.iterator();
        boolean initFirst = false;
        String yM31 = "";
        String yM31v = "";
        DW_UnderOccupiedReport_Set councilUnderOccupiedSet1 = null;
        DW_UnderOccupiedReport_Set RSLUnderOccupiedSet1 = null;
        String filename1 = null;
        int i;
        while (!initFirst) {
            i = includeIte.next();
            // Load first data
            filename1 = SHBEFilenames[i];
            yM31 = DW_SHBE_Handler.getYM3(filename1);
            councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(yM31);
            if (councilUnderOccupiedSet1 != null) {
                RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(yM31);
                System.out.println("Load " + filename1);
                tSHBEData1 = new DW_SHBE_Collection(filename1, paymentType);
                initFirst = true;
            }
        }
        if (tSHBEData1 == null) {
            return result;
        }
        Object[] filenames;
        filenames = DW_UnderOccupiedReport_Handler.getFilenames();
        TreeMap<String, String> councilFilenames;
        TreeMap<String, String> RSLFilenames;
        councilFilenames = (TreeMap<String, String>) filenames[0];
        RSLFilenames = (TreeMap<String, String>) filenames[1];
        // Init result
        Iterator<Integer> includeIte2;
        includeIte2 = include.iterator();
        while (includeIte2.hasNext()) {
            int j;
            j = includeIte2.next();
            String yM3;
            yM3 = DW_SHBE_Handler.getYM3(SHBEFilenames[j]);
            DW_UnderOccupiedReport_Set aCouncilUnderOccupiedSet;
            aCouncilUnderOccupiedSet = councilUnderOccupiedSets.get(yM3);
            if (aCouncilUnderOccupiedSet != null) {
                HashMap<String, String> summary;
                summary = new HashMap<String, String>();
                String key;
                key = DW_SHBE_Handler.getYearMonthNumber(SHBEFilenames[j]);
                result.put(key, summary);
            }
        }
        // These could be returned to save time recreating them for other includes.
        // This would involve feeding them in to the method too per se.
        TreeMap<String, HashSet<DW_PersonID>> tClaimantIDs;
        tClaimantIDs = new TreeMap<String, HashSet<DW_PersonID>>();
        TreeMap<String, HashSet<DW_PersonID>> AllIDs;
        AllIDs = new TreeMap<String, HashSet<DW_PersonID>>();
        TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes;
        tClaimantIDPostcodes = new TreeMap<String, HashSet<ID_PostcodeID>>();
        TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTTs;
        tClaimantIDTTs = new TreeMap<String, HashSet<ID_TenancyType>>();
        TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTTs;
        tClaimantIDPostcodeTTs = new TreeMap<String, HashSet<ID_TenancyType_PostcodeID>>();

        // Specify Data
//        TreeMap<String, DW_SHBE_Record> tDRecords0;
//        tDRecords0 = (TreeMap<String, DW_SHBE_Record>) tSHBEData0[0];
        HashSet<DW_PersonID> tClaimantIDs1;
        tClaimantIDs1 = tSHBEData1.getClaimantIDs();
        tClaimantIDs.put(yM31, tClaimantIDs1);
        HashSet<DW_PersonID> AllIDs1;
        AllIDs1 = tSHBEData1.getAllIDs();
        AllIDs.put(yM31, AllIDs1);
//        HashMap<DW_ID, String> tIDByPostcode0;
//        tIDByPostcode0 = (HashMap<DW_ID, String>) tSHBEData0[8];
        //tIDByPostcode0 = loadIDByPostcode(loadData, filename, i);
//        HashMap<DW_ID, Integer> tIDByTT0;
//        tIDByTT0 = (HashMap<DW_ID, Integer>) tSHBEData0[9];
        //tIDByTT0 = loadIDByTT(loadData, filename, i);
//        HashMap<String, DW_ID> CTBRefToIDLookup0;
//        CTBRefToIDLookup0 = (HashMap<String, DW_ID>) tSHBEData0[10];
//        HashSet<ID_PostcodeID> tClaimantIDPostcodes0;
//        tClaimantIDPostcodes0 = (HashSet<ID_PostcodeID>) tSHBEData0[13];
//        tClaimantIDPostcodes.put(yM30, tClaimantIDPostcodes0);
//        HashSet<ID_TenancyType> tClaimantIDTT0;
//        tClaimantIDTT0 = (HashSet<ID_TenancyType>) tSHBEData0[14];
//        tClaimantIDTTs.put(yM30, tClaimantIDTT0);
//        HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTTs0;
//        tClaimantIDPostcodeTTs0 = (HashSet<ID_TenancyType_PostcodeID>) tSHBEData0[15];
//        tClaimantIDPostcodeTTs.put(yM30, tClaimantIDPostcodeTTs0);

        yM31v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
        doPartSummarySingleTime(
                tSHBEData1,
                yM31,
                yM31v,
                filename1,
                forceNewSummaries,
                paymentType,
                nTT,
                nEG,
                nPSI,
                councilFilenames,
                RSLFilenames,
                councilUnderOccupiedSet1,
                RSLUnderOccupiedSet1,
                result,
                tClaimantIDPostcodes,
                tClaimantIDTTs,
                tClaimantIDPostcodeTTs,
                tDW_PersonIDToIDLookup,
                tPostcodeToPostcodeIDLookup);
        if (!includeIte.hasNext()) {
            return result;
        }
        i = includeIte.next();
        // Second data
        // Init vars
        String filename0 = filename1;
        String yM30 = yM31;
        String yM30v = yM31v;
        DW_SHBE_Collection tSHBEData0 = tSHBEData1;
        DW_UnderOccupiedReport_Set councilUnderOccupiedSet0;
        councilUnderOccupiedSet0 = councilUnderOccupiedSet1;
        DW_UnderOccupiedReport_Set RSLUnderOccupiedSet0;
        RSLUnderOccupiedSet0 = RSLUnderOccupiedSet1;

        filename1 = SHBEFilenames[i];
        yM31 = DW_SHBE_Handler.getYM3(filename1);
        councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(yM31);
        RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(yM31);
        System.out.println("Load " + filename1);
        tSHBEData1 = new DW_SHBE_Collection(filename1, paymentType);

        //DO SOME SUMMARY
        doPartSummaryCompare2Times(
                tSHBEData1,
                yM31,
                yM31v,
                filename1,
                tSHBEData0,
                yM30,
                yM30v,
                filename0,
                forceNewSummaries,
                paymentType,
                nTT,
                nEG,
                nPSI,
                councilFilenames,
                RSLFilenames,
                councilUnderOccupiedSet1,
                RSLUnderOccupiedSet1,
                councilUnderOccupiedSet0,
                RSLUnderOccupiedSet0,
                result,
                tClaimantIDPostcodes,
                tClaimantIDTTs,
                tClaimantIDPostcodeTTs,
                tDW_PersonIDToIDLookup,
                tPostcodeToPostcodeIDLookup);
        String filename00 = filename0;
        String yM300 = yM30;
        String yM300v = yM30v;
        DW_SHBE_Collection tSHBEData00 = tSHBEData0;
        DW_UnderOccupiedReport_Set councilUnderOccupiedSet00 = councilUnderOccupiedSet0;
        DW_UnderOccupiedReport_Set RSLUnderOccupiedSet00 = RSLUnderOccupiedSet0;
        filename0 = filename1;
        yM30 = yM31;
        yM30v = yM31v;
        tSHBEData0 = tSHBEData1;
        councilUnderOccupiedSet0 = councilUnderOccupiedSet1;
        RSLUnderOccupiedSet0 = RSLUnderOccupiedSet1;

        while (includeIte.hasNext()) {
            i = includeIte.next();

            filename1 = SHBEFilenames[i];
            yM31 = DW_SHBE_Handler.getYM3(filename1);
            yM31v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
            councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(yM31);
            RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(yM31);
            tSHBEData1 = new DW_SHBE_Collection(filename1, paymentType);

            //DO SOME SUMMARY
            doPartSummaryCompare3Times(
                    SHBEFilenames,
                    include,
                    tSHBEData1,
                    yM31,
                    yM31v,
                    filename1,
                    tSHBEData0,
                    yM30,
                    yM30v,
                    filename0,
//                    tSHBEData00,
                    yM300,
//                    yM300v,
                    filename00,
                    forceNewSummaries,
                    paymentType,
                    nTT,
                    nEG,
                    nPSI,
                    councilFilenames,
                    RSLFilenames,
                    councilUnderOccupiedSet1,
                    RSLUnderOccupiedSet1,
                    councilUnderOccupiedSet0,
                    RSLUnderOccupiedSet0,
//                    councilUnderOccupiedSet00,
//                    RSLUnderOccupiedSet00,
                    result,
                    tClaimantIDPostcodes,
                    tClaimantIDTTs,
                    tClaimantIDPostcodeTTs,
                    tDW_PersonIDToIDLookup,
                    tPostcodeToPostcodeIDLookup);
            councilUnderOccupiedSet00 = councilUnderOccupiedSet0;
            councilUnderOccupiedSet0 = councilUnderOccupiedSet1;
            RSLUnderOccupiedSet00 = RSLUnderOccupiedSet0;
            RSLUnderOccupiedSet0 = RSLUnderOccupiedSet1;
            filename00 = filename0;
            filename0 = filename1;
            tSHBEData00 = tSHBEData0;
            tSHBEData0 = tSHBEData1;
            yM300 = yM30;
            yM30 = yM31;
            yM300v = yM30v;
            yM30v = yM31v;
            AllCount0 = null;
            HBCount0 = null;
            CTBCount0 = null;
            // Not used at present. incomeAndRentSummary0 = incomeAndRentSummary1;
        }
        return result;
    }

    private void doPartSummaryCompare3Times(
            String[] SHBEFilenames,
            ArrayList<Integer> include,
            DW_SHBE_Collection tSHBEData1,
            String yM31,
            String yM31v,
            String filename1,
            DW_SHBE_Collection tSHBEData0,
            String yM30,
            String yM30v,
            String filename0,
//            DW_SHBE_Collection tSHBEData00,
            String yM300,
//            String yM300v,
            String filename00,
            boolean forceNewSummaries,
            String paymentType,
            int nTT,
            int nEG,
            int nPSI,
            TreeMap<String, String> tCouncilFilenames,
            TreeMap<String, String> tRSLFilenames,
            DW_UnderOccupiedReport_Set tCouncilUnderOccupiedSet1,
            DW_UnderOccupiedReport_Set tRSLUnderOccupiedSet1,
            DW_UnderOccupiedReport_Set tCouncilUnderOccupiedSet0,
            DW_UnderOccupiedReport_Set tRSLUnderOccupiedSet0,
//            DW_UnderOccupiedReport_Set tCouncilUnderOccupiedSet00,
//            DW_UnderOccupiedReport_Set tRSLUnderOccupiedSet00,
            TreeMap<String, HashMap<String, String>> result,
            TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes,
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTTs,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTTs,
            HashMap<DW_PersonID, DW_ID> tDW_PersonIDToIDLookup,
            HashMap<String, DW_ID> tPostcodeToPostcodeIDLookup) {
        // Set counters
        CouncilLinkedRecordCount00 = CouncilLinkedRecordCount0;
        RSLLinkedRecordCount00 = RSLLinkedRecordCount0;
        AllLinkedRecordCount00 = AllUOLinkedRecordCount0;
        AllUOCount00 = AllUOCount0;
        CouncilCount00 = CouncilCount0;
        RSLCount00 = RSLCount0;
        doPartSummaryCompare2Times(
                tSHBEData1,
                yM31,
                yM31v,
                filename1,
                tSHBEData0,
                yM30,
                yM30v,
                filename0,
                forceNewSummaries,
                paymentType,
                nTT,
                nEG,
                nPSI,
                tCouncilFilenames,
                tRSLFilenames,
                tCouncilUnderOccupiedSet1,
                tRSLUnderOccupiedSet1,
                tCouncilUnderOccupiedSet0,
                tRSLUnderOccupiedSet0,
                result,
                tClaimantIDPostcodes,
                tClaimantIDTTs,
                tClaimantIDPostcodeTTs,
                tDW_PersonIDToIDLookup,
                tPostcodeToPostcodeIDLookup);

        String key;
        key = DW_SHBE_Handler.getYearMonthNumber(filename1);
        HashMap<String, String> summary;
        summary = result.get(key);
//        TreeMap<String, DW_SHBE_Record> tDRecords1;
//        tDRecords1 = tSHBEData1.getRecords();
        HashSet<ID_PostcodeID> tClaimantIDPostcodes1;
        tClaimantIDPostcodes1 = tClaimantIDPostcodes.get(yM31);
        HashSet<ID_TenancyType> tClaimantIDTTs1;
        tClaimantIDTTs1 = tClaimantIDTTs.get(yM31);
        HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTTs1;
        tClaimantIDPostcodeTTs1 = tClaimantIDPostcodeTTs.get(yM31);

        doCompare3TimesCounts(
                summary,
                nTT,
                SHBEFilenames,
                include,
                yM31,
                tClaimantIDPostcodes,
                tClaimantIDPostcodes1,
                tClaimantIDTTs,
                tClaimantIDTTs1,
                tClaimantIDPostcodeTTs,
                tClaimantIDPostcodeTTs1);
        summary.put(sSHBEFilename00, filename00);
        summary.put(sSHBEFilename0, filename0);
        summary.put(sSHBEFilename1, filename1);
        summary.put(sCouncilFilename00, tCouncilFilenames.get(yM300));
        summary.put(sRSLFilename00, tRSLFilenames.get(yM300));
        summary.put(sCouncilCount00, Integer.toString(CouncilCount00));
        summary.put(sCouncilLinkedRecordCount00, Integer.toString(CouncilLinkedRecordCount00));
        summary.put(sRSLCount00, Integer.toString(RSLCount00));
        summary.put(sRSLLinkedRecordCount00, Integer.toString(RSLLinkedRecordCount00));
        summary.put(sAllUOLinkedRecordCount00, Integer.toString(CouncilLinkedRecordCount00 + RSLLinkedRecordCount00));
        summary.put(sCouncilFilename0, tCouncilFilenames.get(yM30));
        summary.put(sRSLFilename0, tRSLFilenames.get(yM30));
        summary.put(sCouncilCount0, Integer.toString(CouncilCount0));
        summary.put(sCouncilLinkedRecordCount0, Integer.toString(CouncilLinkedRecordCount0));
        summary.put(sRSLCount0, Integer.toString(RSLCount0));
        summary.put(sRSLLinkedRecordCount0, Integer.toString(RSLLinkedRecordCount0));
        summary.put(sAllUOCount0, Integer.toString(AllUOCount0));
        summary.put(sAllUOLinkedRecordCount0, Integer.toString(CouncilLinkedRecordCount0 + RSLLinkedRecordCount0));
        summary.put(sCouncilFilename1, tCouncilFilenames.get(yM31));
        summary.put(sRSLFilename1, tRSLFilenames.get(yM31));
        summary.put(sCouncilCount1, Integer.toString(CouncilCount1));
        summary.put(sCouncilLinkedRecordCount1, Integer.toString(CouncilLinkedRecordCount1));
        summary.put(sRSLCount1, Integer.toString(RSLCount1));
        summary.put(sRSLLinkedRecordCount1, Integer.toString(RSLLinkedRecordCount1));
        summary.put(sAllUOLinkedRecordCount1, Integer.toString(CouncilLinkedRecordCount1 + RSLLinkedRecordCount1));
    }

    /**
     * Bit odd but filename0 has the data for now. filename00 has the data for
     * the previous time.
     *
     * @param tSHBEData1
     * @param yM31
     * @param yM31v
     * @param filename1
     * @param tSHBEData0
     * @param yM30
     * @param yM30v
     * @param filename0
     * @param forceNewSummaries
     * @param nTT
     * @param nEG
     * @param councilFilenames
     * @param RSLFilenames
     * @param councilUnderOccupiedSet1
     * @param RSLUnderOccupiedSet1
     * @param councilUnderOccupiedSet00
     * @param RSLUnderOccupiedSet00
     * @param summaries
     */
    private void doPartSummaryCompare2Times(
            DW_SHBE_Collection tSHBEData1,
            String yM31,
            String yM31v,
            String filename1,
            DW_SHBE_Collection tSHBEData0,
            String yM30,
            String yM30v,
            String filename0,
            boolean forceNewSummaries,
            String paymentType,
            int nTT,
            int nEG,
            int nPSI,
            TreeMap<String, String> councilFilenames,
            TreeMap<String, String> RSLFilenames,
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet1,
            DW_UnderOccupiedReport_Set RSLUnderOccupiedSet1,
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet0,
            DW_UnderOccupiedReport_Set RSLUnderOccupiedSet0,
            TreeMap<String, HashMap<String, String>> summaries,
            TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes,
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTTs,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTTs,
            HashMap<DW_PersonID, DW_ID> DW_PersonIDToIDLookup,
            HashMap<String, DW_ID> PostcodeToPostcodeIDLookup) {
        doPartSummarySingleTime(
                tSHBEData1,
                yM31,
                yM31v,
                filename1,
                forceNewSummaries,
                paymentType,
                nTT,
                nEG,
                nPSI,
                councilFilenames,
                RSLFilenames,
                councilUnderOccupiedSet1,
                RSLUnderOccupiedSet1,
                summaries,
                tClaimantIDPostcodes,
                tClaimantIDTTs,
                tClaimantIDPostcodeTTs,
                DW_PersonIDToIDLookup,
                PostcodeToPostcodeIDLookup);

        TreeMap<String, DW_SHBE_Record> tDRecords1;
        tDRecords1 = tSHBEData1.getRecords();
//        HashMap<DW_ID, String> tIDByPostcode0;
//        tIDByPostcode0 = (HashMap<DW_ID, String>) tSHBEData0[8];
//        HashMap<DW_ID, Integer> tIDByTT0;
//        tIDByTT0 = (HashMap<DW_ID, Integer>) tSHBEData0[9];
//        HashMap<String, DW_ID> CTBRefToIDLookup0;
//        CTBRefToIDLookup0 = (HashMap<String, DW_ID>) tSHBEData0[10];

        TreeMap<String, DW_SHBE_Record> tDRecords0;
        tDRecords0 = tSHBEData0.getRecords();
//        HashMap<DW_ID, String> tIDByPostcode00;
//        tIDByPostcode00 = (HashMap<DW_ID, String>) tSHBEData00[8];
//        HashMap<DW_ID, Integer> tIDByTT00;
//        tIDByTT00 = (HashMap<DW_ID, Integer>) tSHBEData00[9];
//        HashMap<String, DW_ID> CTBRefToIDLookup00;
//        CTBRefToIDLookup00 = (HashMap<String, DW_ID>) tSHBEData00[10];

        TreeMap<String, DW_UOReport_Record> councilUnderOccupiedSet0Map;
        councilUnderOccupiedSet0Map = councilUnderOccupiedSet0.getMap();
        TreeMap<String, DW_UOReport_Record> RSLUnderOccupiedSet0Map;
        RSLUnderOccupiedSet0Map = RSLUnderOccupiedSet0.getMap();
        TreeMap<String, DW_UOReport_Record> councilUnderOccupiedSet1Map;
        councilUnderOccupiedSet1Map = councilUnderOccupiedSet1.getMap();
        TreeMap<String, DW_UOReport_Record> RSLUnderOccupiedSet1Map;
        RSLUnderOccupiedSet1Map = RSLUnderOccupiedSet1.getMap();
        // Loop over underoccupancy data
        // Loop over Council
        doCompare2TimesLoopOverSet(
                councilUnderOccupiedSet0Map,
                councilUnderOccupiedSet1Map,
                tDRecords0,
                tDRecords1,
                //                CTBRefToIDLookup00,
                //                CTBRefToIDLookup0,
                //                tIDByTT00,
                //                tIDByTT0,
                //                tIDByPostcode00,
                //                tIDByPostcode0,
                yM30v,
                yM31v);
        // Loop over RSL
        doCompare2TimesLoopOverSet(
                RSLUnderOccupiedSet0Map,
                RSLUnderOccupiedSet1Map,
                tDRecords0,
                tDRecords1,
                //                CTBRefToIDLookup00,
                //                CTBRefToIDLookup0,
                //                tIDByTT00,
                //                tIDByTT0,
                //                tIDByPostcode00,
                //                tIDByPostcode0,
                yM30v,
                yM31v);

        String key;
        key = DW_SHBE_Handler.getYearMonthNumber(filename1);
        HashMap<String, String> summary;
        summary = summaries.get(key);

        addToSummaryCompare2Times(nTT, nEG, nPSI, summary);

        // All
        summary.put(sSHBEFilename0, filename0); // This looks wierd but is right!
        summary.put(sSHBEFilename1, filename1); // This looks wierd but is right!
        summary.put(sCouncilFilename0, councilFilenames.get(yM30)); // This looks wierd but is right!
        summary.put(sRSLFilename0, RSLFilenames.get(yM30)); // This looks wierd but is right!
        summary.put(sCouncilCount0, Integer.toString(CouncilCount0));
        summary.put(sCouncilLinkedRecordCount0, Integer.toString(CouncilLinkedRecordCount0));
        summary.put(sRSLCount0, Integer.toString(RSLCount0));
        summary.put(sRSLLinkedRecordCount0, Integer.toString(RSLLinkedRecordCount0));
        summary.put(sAllUOCount0, Integer.toString(AllUOCount0));
        summary.put(sAllUOLinkedRecordCount0, Integer.toString(CouncilLinkedRecordCount0 + RSLLinkedRecordCount0));
        summary.put(sCouncilFilename1, councilFilenames.get(yM31)); // This looks wierd but is right!
        summary.put(sRSLFilename1, RSLFilenames.get(yM31)); // This looks wierd but is right!
        summary.put(sCouncilCount1, Integer.toString(CouncilCount1));
        summary.put(sCouncilLinkedRecordCount1, Integer.toString(CouncilLinkedRecordCount1));
        summary.put(sRSLCount1, Integer.toString(RSLCount1));
        summary.put(sRSLLinkedRecordCount1, Integer.toString(RSLLinkedRecordCount1));
        summary.put(sAllUOCount1, Integer.toString(AllUOCount1));
        summary.put(sAllUOLinkedRecordCount1, Integer.toString(CouncilLinkedRecordCount1 + RSLLinkedRecordCount1));
    }

    private void addToSetsForComparisons(
            String yM3,
            TreeMap<String, DW_SHBE_Record> D_Records,
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet,
            DW_UnderOccupiedReport_Set RSLUnderOccupiedSet,
            TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes,
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTTs,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTTs,
            HashMap<DW_PersonID, DW_ID> DW_PersonIDToIDLookup,
            HashMap<String, DW_ID> PostcodeToPostcodeIDLookup
    ) {
        HashSet<ID_PostcodeID> tClaimantIDPostcodes0;
        HashSet<ID_TenancyType> tClaimantIDTTs0;
        HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTTs0;
        if (tClaimantIDPostcodes.containsKey(yM3)) {
            tClaimantIDPostcodes0 = tClaimantIDPostcodes.get(yM3);
        } else {
            tClaimantIDPostcodes0 = getID_PostcodeIDSet(
                    D_Records, councilUnderOccupiedSet, RSLUnderOccupiedSet,
                    DW_PersonIDToIDLookup, PostcodeToPostcodeIDLookup);
            tClaimantIDPostcodes.put(yM3, tClaimantIDPostcodes0);
        }
        if (tClaimantIDTTs.containsKey(yM3)) {
            tClaimantIDTTs0 = tClaimantIDTTs.get(yM3);
        } else {
            tClaimantIDTTs0 = getID_TenancyTypeSet(
                    D_Records, councilUnderOccupiedSet, RSLUnderOccupiedSet,
                    DW_PersonIDToIDLookup);
            tClaimantIDTTs.put(yM3, tClaimantIDTTs0);
        }
        if (tClaimantIDPostcodeTTs.containsKey(yM3)) {
            tClaimantIDPostcodeTTs0 = tClaimantIDPostcodeTTs.get(yM3);
        } else {
            tClaimantIDPostcodeTTs0 = getID_TenancyType_PostcodeIDSet(
                    D_Records, councilUnderOccupiedSet, RSLUnderOccupiedSet,
                    DW_PersonIDToIDLookup, PostcodeToPostcodeIDLookup);
            tClaimantIDPostcodeTTs.put(yM3, tClaimantIDPostcodeTTs0);
        }
    }

    private void doPartSummarySingleTime(
            //            HashMap<String, String> summary,
            DW_SHBE_Collection tSHBEData,
            //            TreeMap<String, DW_SHBE_Record> tDRecords0,
            //            HashMap<DW_ID, String> tIDByPostcode0,
            //            HashMap<DW_ID, Integer> tIDByTT0,
            //            HashMap<String, DW_ID> CTBRefToIDLookup0,
            //            HashMap<String, Integer> tLoadSummary,
            String yM3,
            String yM3v,
            String filename,
            boolean forceNewSummaries,
            String paymentType,
            int nTT,
            int nEG,
            int nPSI,
            TreeMap<String, String> councilFilenames,
            TreeMap<String, String> RSLFilenames,
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet,
            DW_UnderOccupiedReport_Set RSLUnderOccupiedSet,
            TreeMap<String, HashMap<String, String>> summaries,
            TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes,
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTTs,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTTs,
            HashMap<DW_PersonID, DW_ID> tDW_PersonIDToIDLookup,
            HashMap<String, DW_ID> tPostcodeToPostcodeIDLookup) {
        // Set the last results
        AllCount0 = AllCount1;
        HBCount0 = HBCount1;
        CTBCount0 = CTBCount1;
        AllUOCount0 = AllUOCount1;
        CouncilCount0 = CouncilCount1;
        RSLCount0 = RSLCount1;
        CouncilLinkedRecordCount0 = CouncilLinkedRecordCount1;
        RSLLinkedRecordCount0 = RSLLinkedRecordCount1;
        AllCount0 = AllCount1;
        AllUOLinkedRecordCount0 = AllUOLinkedRecordCount1;
//        for (int TT = 0; TT < nTT; TT++) {
//            AllTotalCountTTClaimant0[TT] = AllTotalCountTTClaimant1[TT];
//        }
        System.arraycopy(TotalCount_TTClaimant1, 0, TotalCount_TTClaimant0, 0, nTT);

        TreeMap<String, DW_SHBE_Record> tDRecords0;
        tDRecords0 = tSHBEData.getRecords();
        HashMap<DW_ID, String> tIDByPostcode0;
        tIDByPostcode0 = tSHBEData.getClaimantIDToPostcodeLookup();
        //tIDByPostcode0 = loadIDByPostcode(loadData, filename, i);
        HashMap<DW_ID, Integer> tIDByTT0;
        tIDByTT0 = tSHBEData.getClaimantIDToTenancyTypeLookup();
        //tIDByTT0 = loadIDByTT(loadData, filename, i);
        HashMap<String, DW_ID> CTBRefToIDLookup0;
        CTBRefToIDLookup0 = tSHBEData.getCTBRefToClaimantIDLookup();
        //tCTBRefID0 = loadCTBRefByID(loadData, filename, i);
        String key;
        key = DW_SHBE_Handler.getYearMonthNumber(filename);
        HashMap<String, String> summary;
        summary = summaries.get(key);
        HashMap<String, Integer> tLoadSummary;
        tLoadSummary = tSHBEData.getLoadSummary();
        addToSummary(summary, tLoadSummary);
        initCounts(nTT, nEG, nPSI);

        // Add to tClaimantIDPostcodes, tClaimantIDTTs, tClaimantIDPostcodeTTs for later comparisons
        addToSetsForComparisons(
                yM3,
                tDRecords0,
                councilUnderOccupiedSet,
                RSLUnderOccupiedSet,
                tClaimantIDPostcodes,
                tClaimantIDTTs,
                tClaimantIDPostcodeTTs,
                tDW_PersonIDToIDLookup,
                tPostcodeToPostcodeIDLookup);

        TreeMap<String, DW_UOReport_Record> councilUnderOccupiedSetMap;
        councilUnderOccupiedSetMap = councilUnderOccupiedSet.getMap();
        TreeMap<String, DW_UOReport_Record> RSLUnderOccupiedSetMap;
        RSLUnderOccupiedSetMap = RSLUnderOccupiedSet.getMap();
        // Loop over underoccupancy data
        // Loop over Council
        CouncilLinkedRecordCount1 = doSingleTimeLoopOverSet(
                councilUnderOccupiedSetMap, tDRecords0, CTBRefToIDLookup0,
                tIDByTT0, tIDByPostcode0, yM3v);
        // Loop over RSL
        RSLLinkedRecordCount1 = doSingleTimeLoopOverSet(
                RSLUnderOccupiedSetMap, tDRecords0, CTBRefToIDLookup0,
                tIDByTT0, tIDByPostcode0, yM3v);
        HashMap<String, BigDecimal> incomeAndRentSummary0;
        incomeAndRentSummary0 = DW_SHBE_Handler.getIncomeAndRentSummary(
                tSHBEData,
                filename,
                paymentType,
                councilUnderOccupiedSet,
                true,
                forceNewSummaries);
        addToSummarySingleTimeIncomeAndRent(
                summary,
                incomeAndRentSummary0);
        addToSummarySingleTime(nTT, nEG, nPSI, summary);
        addToSummarySingleTimeRentArrears(summary);
        // Prepare vars
        CouncilCount1 = councilUnderOccupiedSetMap.size();
        RSLCount1 = RSLUnderOccupiedSetMap.size();
        AllUOCount1 = CouncilCount1 + RSLCount1;
        AllUOLinkedRecordCount1 = CouncilLinkedRecordCount1 + RSLLinkedRecordCount1;
        summary.put(sAllUOCount1, Integer.toString(AllUOCount1));
        summary.put(sSHBEFilename1, filename);
        summary.put(sCouncilFilename1, councilFilenames.get(yM3));
        summary.put(sRSLFilename1, RSLFilenames.get(yM3));
        summary.put(sCouncilCount1, Integer.toString(CouncilCount1));
        summary.put(sCouncilLinkedRecordCount1, Integer.toString(CouncilLinkedRecordCount1));
        summary.put(sRSLCount1, Integer.toString(RSLCount1));
        summary.put(sRSLLinkedRecordCount1, Integer.toString(RSLLinkedRecordCount1));
        summary.put(sAllUOCount1, Integer.toString(AllUOCount1));
        summary.put(sAllUOLinkedRecordCount1, Integer.toString(AllUOLinkedRecordCount1));
    }

    public void doCompare2TimesLoopOverSet(
            TreeMap<String, DW_UOReport_Record> map0,
            TreeMap<String, DW_UOReport_Record> map1,
            TreeMap<String, DW_SHBE_Record> D_Records0,
            TreeMap<String, DW_SHBE_Record> D_Records1,
            //            HashMap<String, DW_ID> CTBRefID0,
            //            HashMap<String, DW_ID> CTBRefID1,
            //            HashMap<DW_ID, Integer> tIDByTT0,
            //            HashMap<DW_ID, Integer> tIDByTT1,
            //            HashMap<DW_ID, String> tIDByPostcode0,
            //            HashMap<DW_ID, String> tIDByPostcode1,
            String yM30v,
            String yM31v) {
        Iterator<String> ite;
        ite = map1.keySet().iterator();
        while (ite.hasNext()) {
            String tID;
            tID = ite.next();
            DW_UOReport_Record UORec;
            UORec = map1.get(tID);
            // Rent Arrears Summary
            doSingleTimeRentArrearsCount(UORec);
            String CTBRef;
            CTBRef = UORec.getClaimReferenceNumber();
            DW_SHBE_Record Record0;
            Record0 = D_Records0.get(CTBRef);
            DW_SHBE_Record Record1;
            Record1 = D_Records1.get(CTBRef);
            if (Record1 == null) {
                // Count this!
                //tDRecordsCTBRefDW_SHBE_RecordNullCount++;
            } else {
                DW_SHBE_D_Record D_Record0;
                D_Record0 = null;
                if (Record0 != null) {
                    D_Record0 = Record0.getDRecord();
                }
                DW_SHBE_D_Record D_Record1;
                D_Record1 = Record1.getDRecord();
                doCompare2TimesCounts(
                        D_Record0,
                        D_Record1,
                        //                        CTBRef,
                        //                        CTBRefID0,
                        //                        CTBRefID1,
                        //                        tIDByTT0,
                        //                        tIDByTT1,
                        //                        tIDByPostcode0,
                        //                        tIDByPostcode1,
                        yM30v,
                        yM31v);
            }
        }
        // Go through all those that were in the UO data, but have come out.
        HashSet<String> claimsOutOfUO;
        claimsOutOfUO = new HashSet<String>();
        claimsOutOfUO.addAll(map0.keySet());
        claimsOutOfUO.removeAll(map1.keySet());
        ite = claimsOutOfUO.iterator();
        while (ite.hasNext()) {
            String tID;
            tID = ite.next();
            DW_UOReport_Record UORec;
            UORec = map0.get(tID);
            // Rent Arrears Summary
            doSingleTimeRentArrearsCount(UORec);
            String CTBRef;
            CTBRef = UORec.getClaimReferenceNumber();
            DW_SHBE_Record Record0;
            Record0 = D_Records0.get(CTBRef);
            DW_SHBE_Record Record1;
            Record1 = D_Records1.get(CTBRef);
            if (Record1 == null) {
                // Count this!
                //tDRecordsCTBRefDW_SHBE_RecordNullCount++;
            } else {
                DW_SHBE_D_Record D_Record0;
                D_Record0 = null;
                if (Record0 != null) {
                    D_Record0 = Record0.getDRecord();
                }
                DW_SHBE_D_Record D_Record1;
                D_Record1 = Record1.getDRecord();
                doCompare2TimesCounts(
                        D_Record0,
                        D_Record1,
                        //                        CTBRef,
                        //                        CTBRefID0,
                        //                        CTBRefID1,
                        //                        tIDByTT0,
                        //                        tIDByTT1,
                        //                        tIDByPostcode0,
                        //                        tIDByPostcode1,
                        yM30v,
                        yM31v);
            }
        }

    }

    /**
     *
     * @param map
     * @param D_Records
     * @param CTBRefToIDLookup0
     * @param tIDByTT0
     * @param tIDByPostcode0
     * @param yM30v
     * @return
     */
    public int doSingleTimeLoopOverSet(
            TreeMap<String, DW_UOReport_Record> map,
            TreeMap<String, DW_SHBE_Record> D_Records,
            HashMap<String, DW_ID> CTBRefToIDLookup0,
            HashMap<DW_ID, Integer> tIDByTT0,
            HashMap<DW_ID, String> tIDByPostcode0,
            String yM30v) {
        int linkedRecords;
        linkedRecords = 0;
        Iterator<String> ite;
        ite = map.keySet().iterator();
        while (ite.hasNext()) {
            String tID;
            tID = ite.next();
            DW_UOReport_Record UORec;
            UORec = map.get(tID);
            // Rent Arrears Summary
            doSingleTimeRentArrearsCount(UORec);
            String CTBRef;
            CTBRef = UORec.getClaimReferenceNumber();
            DW_SHBE_Record Record;
            Record = D_Records.get(CTBRef);
            if (Record == null) {
                //tDRecordsCTBRefDW_SHBE_RecordNullCount++;
            } else {
                DW_SHBE_D_Record D_Record;
                D_Record = Record.getDRecord();
                doSingleTimeCount(
                        D_Record,
                        //                        CTBRef,
                        //                        CTBRefToIDLookup0,
                        //                        tIDByTT0,
                        //                        tIDByPostcode0,
                        yM30v);
                linkedRecords++;
            }
        }
        return linkedRecords;
    }

    private void addToSummarySingleTimeIncomeAndRent(
            HashMap<String, String> summary,
            HashMap<String, BigDecimal> incomeAndRentSummary) {
        Iterator<String> incomeAndRentSummaryKeySetIte;
        incomeAndRentSummaryKeySetIte = incomeAndRentSummary.keySet().iterator();
        while (incomeAndRentSummaryKeySetIte.hasNext()) {
            String name;
            name = incomeAndRentSummaryKeySetIte.next();
            String value;
            value = Generic_BigDecimal.roundIfNecessary(
                    incomeAndRentSummary.get(name), 2, RoundingMode.HALF_UP).toPlainString();
            summary.put(
                    name,
                    value);
        }

    }

    private File getSummaryTableDir(
            String paymentType,
            String includeKey,
            boolean doUnderOccupancy) {
        File result;
        result = new File(
                DW_Files.getOutputSHBETablesDir(),
                "SummaryTables");
        result = new File(
                result,
                paymentType);
        result = new File(
                result,
                includeKey);
        if (doUnderOccupancy) {
            result = new File(
                    result,
                    "UO");
        } else {
            result = new File(
                    result,
                    "All");
        }
        result.mkdirs();
        return result;
    }

    /**
     * Provides comparisons with the previous period
     *
     * @param summaryTable
     * @param paymentType
     * @param includeKey
     * @param doUnderOccupancy
     * @param nTT
     * @param nEG
     * @param nPSI
     */
    public void writeSummaryTables(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean doUnderOccupancy,
            int nTT,
            int nEG,
            int nPSI
    ) {
        writeSummaryTableCompare3Times(
                summaryTable,
                paymentType,
                includeKey,
                doUnderOccupancy,
                nTT, nEG);
        writeSummaryTableCompare2Times(
                summaryTable,
                paymentType,
                includeKey,
                doUnderOccupancy,
                nTT, nEG);
        writeSummaryTableCompare2TimesTT(
                summaryTable,
                paymentType,
                includeKey,
                doUnderOccupancy,
                nTT, nEG);
        writeSummaryTableCompare2TimesPostcode(
                summaryTable,
                paymentType,
                includeKey,
                doUnderOccupancy,
                nTT, nEG);
        writeSummaryTableSingleTimeGenericCounts(
                summaryTable,
                paymentType,
                includeKey,
                doUnderOccupancy,
                nTT, nEG, nPSI);
        writeSummaryTableSingleTimeEntitlementEligibleAmountContractualAmount(
                summaryTable,
                paymentType,
                includeKey,
                doUnderOccupancy,
                nTT, nEG);
        writeSummaryTableSingleTimePSI(
                summaryTable,
                paymentType,
                includeKey,
                doUnderOccupancy,
                nTT, nEG, nPSI);
        writeSummaryTableSingleTimeRentAndIncome(
                summaryTable,
                paymentType,
                includeKey,
                doUnderOccupancy,
                nTT, nEG);
        writeSummaryTableSingleTimeTT(
                summaryTable,
                paymentType,
                includeKey,
                doUnderOccupancy,
                nTT, nEG);
        if (doUnderOccupancy) {
            writeSummaryTableSingleTimeRentArrears(
                    summaryTable,
                    paymentType,
                    includeKey, doUnderOccupancy, nTT, nEG);
        }
        writeSummaryTableSingleTimeEthnicity(
                summaryTable,
                paymentType,
                includeKey, doUnderOccupancy, nTT, nEG);
        writeSummaryTableSingleTimeDisability(
                summaryTable,
                paymentType,
                includeKey,
                doUnderOccupancy,
                nTT, nEG);
    }

    private PrintWriter getPrintWriter(
            String name,
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy) {
        PrintWriter result;
        File dirOut;
        dirOut = getSummaryTableDir(paymentType, includeKey, underOccupancy);
        String outFilename;
        outFilename = paymentType + "_";
        if (underOccupancy) {
            outFilename += "UO_";
        }
        outFilename += summaryTable.firstKey() + "To" + summaryTable.lastKey() + name + ".csv";
        File outFile;
        outFile = new File(dirOut, outFilename);
        result = Generic_StaticIO.getPrintWriter(outFile, false);
        return result;
    }

    /**
     * Provides comparisons with the previous period
     *
     * @param summaryTable
     * @param paymentType
     * @param includeKey
     * @param underOccupancy
     * @param nTT
     * @param nEG
     */
    public void writeSummaryTableCompare3Times(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        TreeMap<String, File> ONSPDFiles;
        ONSPDFiles = DW_Postcode_Handler.getONSPDFiles();
        String name;
        name = "Compare3Times";
        PrintWriter pw;
        pw = getPrintWriter(name, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename00 + ", ";
        header += sSHBEFilename0 + ", ";
        header += sSHBEFilename1 + ", ";
        header += "PostCodeLookupDate00, PostCodeLookupFile00, "
                + "PostCodeLookupDate0, PostCodeLookupFile0, "
                + "PostCodeLookupDate1, PostCodeLookupFile1, ";
        if (underOccupancy) {
            header += sCouncilFilename00 + ", ";
            header += sRSLFilename00 + ", ";
            header += sCouncilCount00 + ", ";
            header += sRSLCount00 + ", ";
            header += sAllUOCount00 + ", ";
            header += sAllUOLinkedRecordCount00 + ", ";
            header += sCouncilFilename0 + ", ";
            header += sRSLFilename0 + ", ";
            header += sCouncilCount0 + ", ";
            header += sRSLCount0 + ", ";
            header += sAllUOCount0 + ", ";
            header += sAllUOLinkedRecordCount1 + ", ";
            header += sCouncilFilename1 + ", ";
            header += sRSLFilename1 + ", ";
            header += sCouncilCount1 + ", ";
            header += sRSLCount1 + ", ";
            header += sAllUOCount1 + ", ";
            header += sAllUOLinkedRecordCount1 + ", ";
        }
        header += SamePostcodeIIIString + ", ";
        header += SamePostcodeIOIString + ", ";
        header += SamePostcodeOIOString + ", ";
        header += SameTenancyIIIString + ", ";
        header += SameTenancyIOIString + ", ";
        header += SameTenancyOIOString + ", ";
        for (int i = 1; i < nTT; i++) {
            header += SameTenancyIIITTString[i] + ", ";
            header += SameTenancyIOITTString[i] + ", ";
            header += SameTenancyOIOTTString[i] + ", ";
        }
        header += SameTenancyAndPostcodeIIIString + ", ";
        header += SameTenancyAndPostcodeIOIString + ", ";
        header += SameTenancyAndPostcodeOIOString + ", ";
        for (int i = 1; i < nTT; i++) {
            header += SameTenancyAndPostcodeIIITTString[i] + ", ";
            header += SameTenancyAndPostcodeIOITTString[i] + ", ";
            header += SameTenancyAndPostcodeOIOTTString[i] + ", ";
        }
        header = header.substring(0, header.length() - 2);
        pw.println(header);
        Iterator<String> ite;
        ite = summaryTable.keySet().iterator();
        while (ite.hasNext()) {
            String key;
            key = ite.next();
            String line;
            line = "";
            HashMap<String, String> summary;
            summary = summaryTable.get(key);
            String filename00;
            filename00 = summary.get(sSHBEFilename00);
            String filename0;
            filename0 = summary.get(sSHBEFilename0);
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename00 + "," + filename0 + ", " + filename1 + ", ";
            String PostCodeLookupDate00 = null;
            String PostCodeLookupFile00Name = null;
            if (filename00 != null) {
                PostCodeLookupDate00 = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                        DW_SHBE_Handler.getYM3(filename00));
                PostCodeLookupFile00Name = ONSPDFiles.get(PostCodeLookupDate00).getName();
            }
            line += PostCodeLookupDate00 + ", " + PostCodeLookupFile00Name + ", ";
            String PostCodeLookupDate0 = null;
            String PostCodeLookupFile0Name = null;
            if (filename0 != null) {
                PostCodeLookupDate0 = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                        DW_SHBE_Handler.getYM3(filename0));
                PostCodeLookupFile0Name = ONSPDFiles.get(PostCodeLookupDate0).getName();
            }
            line += PostCodeLookupDate0 + ", " + PostCodeLookupFile0Name + ", ";
            String PostCodeLookupDate1 = null;
            String PostCodeLookupFile1Name = null;
            PostCodeLookupDate1 = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                    DW_SHBE_Handler.getYM3(filename1));
            PostCodeLookupFile1Name = ONSPDFiles.get(PostCodeLookupDate1).getName();
            line += PostCodeLookupDate1 + ", " + PostCodeLookupFile1Name + ", ";
            if (underOccupancy) {
                line += summary.get(sCouncilFilename00) + ", ";
                line += summary.get(sRSLFilename00) + ", ";
                line += summary.get(sCouncilCount00) + ", ";
                line += summary.get(sRSLCount00) + ", ";
                line += summary.get(sAllUOCount00) + ", ";
                line += summary.get(sAllUOLinkedRecordCount00) + ", ";
                line += summary.get(sCouncilFilename0) + ", ";
                line += summary.get(sRSLFilename0) + ", ";
                line += summary.get(sCouncilCount0) + ", ";
                line += summary.get(sRSLCount0) + ", ";
                line += summary.get(sAllUOCount0) + ", ";
                line += summary.get(sAllUOLinkedRecordCount0) + ", ";
                line += summary.get(sCouncilFilename1) + ", ";
                line += summary.get(sRSLFilename1) + ", ";
                line += summary.get(sCouncilCount1) + ", ";
                line += summary.get(sRSLCount1) + ", ";
                line += summary.get(sAllUOCount1) + ", ";
                line += summary.get(sAllUOLinkedRecordCount1) + ", ";
            }
            line += summary.get(SamePostcodeIIIString) + ", ";
            line += summary.get(SamePostcodeIOIString) + ", ";
            line += summary.get(SamePostcodeOIOString) + ", ";
            line += summary.get(SameTenancyIIIString) + ", ";
            line += summary.get(SameTenancyIOIString) + ", ";
            line += summary.get(SameTenancyOIOString) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(SameTenancyIIITTString[i]) + ", ";
                line += summary.get(SameTenancyIOITTString[i]) + ", ";
                line += summary.get(SameTenancyOIOTTString[i]) + ", ";
            }
            line += summary.get(SameTenancyAndPostcodeIIIString) + ", ";
            line += summary.get(SameTenancyAndPostcodeIOIString) + ", ";
            line += summary.get(SameTenancyAndPostcodeOIOString) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(SameTenancyAndPostcodeIIITTString[i]) + ", ";
                line += summary.get(SameTenancyAndPostcodeIOITTString[i]) + ", ";
                line += summary.get(SameTenancyAndPostcodeOIOTTString[i]) + ", ";
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    /**
     * Provides comparisons with the previous period
     *
     * @param summaryTable
     * @param paymentType
     * @param includeKey
     * @param underOccupancy
     * @param nTT
     * @param nEG
     */
    public void writeSummaryTableCompare2Times(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        TreeMap<String, File> ONSPDFiles;
        ONSPDFiles = DW_Postcode_Handler.getONSPDFiles();
        String name;
        name = "Compare2Times";
        PrintWriter pw;
        pw = getPrintWriter(name, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = getHeaderCompare2TimesGeneric(underOccupancy);
        header += getHeaderCompare2TimesTTChange();
        header += getHeaderCompare2TimesPostcodeChange();
        header = header.substring(0, header.length() - 2);
        pw.println(header);
        Iterator<String> ite;
        ite = summaryTable.keySet().iterator();
        while (ite.hasNext()) {
            String key;
            key = ite.next();
            HashMap<String, String> summary;
            summary = summaryTable.get(key);
            String line;
            line = getLineCompare2TimesGeneric(
                    summary,
                    ONSPDFiles,
                    underOccupancy);
            // General
            line += getLineCompare2TimesTTChange(summary);
            line += getLineCompare2TimesPostcodeChange(summary);
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    private String getHeaderCompare2TimesPostcodeChange() {
        String header = "";
        // All Postcode Related
        header += sAllTotalCountPostcode0ValidPostcode1Valid + ", ";
        header += sAllPercentagePostcode0ValidPostcode1Valid + ", ";
        header += sAllTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sAllTotalCountPostcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sAllPercentagePostcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sAllTotalCountPostcode0ValidPostcode1NotValid + ", ";
        header += sAllPercentagePostcode0ValidPostcode1NotValid + ", ";
        header += sAllTotalCountPostcode0NotValidPostcode1Valid + ", ";
        header += sAllPercentagePostcode0NotValidPostcode1Valid + ", ";
        header += sAllTotalCountPostcode0NotValidPostcode1NotValid + ", ";
        header += sAllPercentagePostcode0NotValidPostcode1NotValid + ", ";
        header += sAllTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sAllTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        // HB Postcode Related
        header += sHBTotalCountPostcode0ValidPostcode1Valid + ", ";
        header += sHBPercentagePostcode0ValidPostcode1Valid + ", ";
        header += sHBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sHBTotalCountPostcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sHBPercentagePostcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sHBTotalCountPostcode0ValidPostcode1NotValid + ", ";
        header += sHBPercentagePostcode0ValidPostcode1NotValid + ", ";
        header += sHBTotalCountPostcode0NotValidPostcode1Valid + ", ";
        header += sHBPercentagePostcode0NotValidPostcode1Valid + ", ";
        header += sHBTotalCountPostcode0NotValidPostcode1NotValid + ", ";
        header += sHBPercentagePostcode0NotValidPostcode1NotValid + ", ";
        header += sHBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sHBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        // CTB Postcode Related
        header += sCTBTotalCountPostcode0ValidPostcode1Valid + ", ";
        header += sCTBPercentageOfCTB_Postcode0ValidPostcode1Valid + ", ";
        header += sCTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sCTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged + ", ";
        header += sCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged + ", ";
        header += sCTBTotalCountPostcode0ValidPostcode1NotValid + ", ";
        header += sCTBPercentagePostcode0ValidPostcode1NotValid + ", ";
        header += sCTBTotalCountPostcode0NotValidPostcode1Valid + ", ";
        header += sCTBPercentagePostcode0NotValidPostcode1Valid + ", ";
        header += sCTBTotalCountPostcode0NotValidPostcode1NotValid + ", ";
        header += sCTBPercentagePostcode0NotValidPostcode1NotValid + ", ";
        header += sCTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sCTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        return header;
    }

    private String getLineCompare2TimesPostcodeChange(HashMap<String, String> summary) {
        String line = "";
        // All
        line += summary.get(sAllTotalCountPostcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sAllPercentagePostcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sAllTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sAllTotalCountPostcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sAllPercentagePostcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sAllTotalCountPostcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sAllPercentagePostcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sAllTotalCountPostcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sAllPercentagePostcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sAllTotalCountPostcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sAllPercentagePostcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sAllTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sAllTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        line += summary.get(sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        // HB
        line += summary.get(sHBTotalCountPostcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sHBPercentagePostcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sHBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sHBTotalCountPostcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sHBPercentagePostcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sHBTotalCountPostcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sHBPercentagePostcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sHBTotalCountPostcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sHBPercentagePostcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sHBTotalCountPostcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sHBPercentagePostcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sHBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sHBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        line += summary.get(sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        // CTB
        line += summary.get(sCTBTotalCountPostcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sCTBPercentageOfCTB_Postcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sCTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sCTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged) + ", ";
        line += summary.get(sCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged) + ", ";
        line += summary.get(sCTBTotalCountPostcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sCTBPercentagePostcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sCTBTotalCountPostcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sCTBPercentagePostcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sCTBTotalCountPostcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sCTBPercentagePostcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sCTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sCTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        line += summary.get(sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        return line;
    }

    public String getHeaderCompare2TimesTTChange() {
        String header = "";
        // General
        // All
        header += sAllTotalCount_TTChangeClaimant + ", ";
        header += sAllPercentageOfAll_TTChangeClaimant + ", ";
        // General HB related
        header += sHBTotalCount_TTChangeClaimant + ", ";
        header += sHBPercentageOfHB_TTChangeClaimant + ", ";
        header += sTotalCount_HBTTsToHBTTs + ", ";
        header += sPercentageOfHB_HBTTsToHBTTs + ", ";
        header += sTotalCount_SocialTTsToPrivateDeregulatedTTs + ", ";
        header += sPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs + ", ";
        header += sTotalCount_PrivateDeregulatedTTsToSocialTTs + ", ";
        header += sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs + ", ";
        header += sTotalCount_TT1ToPrivateDeregulatedTTs + ", ";
        header += sPercentageOfTT1_TT1ToPrivateDeregulatedTTs + ", ";
        header += sTotalCount_TT4ToPrivateDeregulatedTTs + ", ";
        header += sPercentageOfTT4_TT4ToPrivateDeregulatedTTs + ", ";
        header += sTotalCount_PrivateDeregulatedTTsToTT1 + ", ";
        header += sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1 + ", ";
        header += sTotalCount_PrivateDeregulatedTTsToTT4 + ", ";
        header += sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4 + ", ";

        header += sTotalCount_TT1ToTT4 + ", ";
        header += sPercentageOfTT1_TT1ToTT4 + ", ";
        header += sTotalCount_TT4ToTT1 + ", ";
        header += sPercentageOfTT4_TT4ToTT1 + ", ";

        header += sTotalCount_PostcodeChangeWithinSocialTTs + ", ";
        header += sPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs + ", ";
        header += sTotalCount_PostcodeChangeWithinTT1 + ", ";
        header += sPercentageOfTT1_PostcodeChangeWithinTT1 + ", ";
        header += sTotalCount_PostcodeChangeWithinTT4 + ", ";
        header += sPercentageOfTT4_PostcodeChangeWithinTT4 + ", ";
        header += sTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs + ", ";
        header += sPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs + ", ";
        header += sTotalCount_HBTTsToCTBTTs + ", ";
        header += sPercentageOfHB_HBTTsToCTBTTs + ", ";
        // General CTB related
        header += sCTBTotalCount_TTChangeClaimant + ", ";
        header += sCTBPercentageOfCTB_TTChangeClaimant + ", ";
        header += sTotalCount_SocialTTsToCTBTTs + ", ";
        header += sPercentageOfSocialTTs_SocialTTsToCTBTTs + ", ";
        header += sTotalCount_TT1ToCTBTTs + ", ";
        header += sPercentageOfTT1_TT1ToCTBTTs + ", ";
        header += sTotalCount_TT4ToCTBTTs + ", ";
        header += sPercentageOfTT4_TT4ToCTBTTs + ", ";
        header += sTotalCount_PrivateDeregulatedTTsToCTBTTs + ", ";
        header += sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs + ", ";
        header += sTotalCountCTBTTsToSocialTTs + ", ";
        header += sPercentageOfCTB_CTBTTsToSocialTTs + ", ";
        header += sTotalCount_CTBTTsToTT1 + ", ";
        header += sPercentageOfCTB_CTBTTsToTT1 + ", ";
        header += sTotalCount_CTBTTsToTT4 + ", ";
        header += sPercentageOfCTB_CTBTTsToTT4 + ", ";
        header += sTotalCount_CTBTTsToPrivateDeregulatedTTs + ", ";
        header += sPercentageOfCTB_CTBTTsToPrivateDeregulatedTTs + ", ";
        header += sTotalCount_CTBTTsToHBTTs + ", ";
        header += sPercentageOfCTB_CTBTTsToHBTTs + ", ";
        return header;
    }

    private String getLineCompare2TimesTTChange(HashMap<String, String> summary) {
        String line = "";
        // All
        line += summary.get(sAllTotalCount_TTChangeClaimant) + ", ";
        line += summary.get(sAllPercentageOfAll_TTChangeClaimant) + ", ";
        // General HB related
        line += summary.get(sHBTotalCount_TTChangeClaimant) + ", ";
        line += summary.get(sHBPercentageOfHB_TTChangeClaimant) + ", ";
        line += summary.get(sTotalCount_HBTTsToHBTTs) + ", ";
        line += summary.get(sPercentageOfHB_HBTTsToHBTTs) + ", ";
        line += summary.get(sTotalCount_SocialTTsToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sTotalCount_PrivateDeregulatedTTsToSocialTTs) + ", ";
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs) + ", ";
        line += summary.get(sTotalCount_TT1ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sPercentageOfTT1_TT1ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sTotalCount_TT4ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sPercentageOfTT4_TT4ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sTotalCount_PrivateDeregulatedTTsToTT1) + ", ";
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1) + ", ";
        line += summary.get(sTotalCount_PrivateDeregulatedTTsToTT4) + ", ";
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4) + ", ";

        line += summary.get(sTotalCount_TT1ToTT4) + ", ";
        line += summary.get(sPercentageOfTT1_TT1ToTT4) + ", ";
        line += summary.get(sTotalCount_TT4ToTT1) + ", ";
        line += summary.get(sPercentageOfTT4_TT4ToTT1) + ", ";

        line += summary.get(sTotalCount_PostcodeChangeWithinSocialTTs) + ", ";
        line += summary.get(sPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs) + ", ";
        line += summary.get(sTotalCount_PostcodeChangeWithinTT1) + ", ";
        line += summary.get(sPercentageOfTT1_PostcodeChangeWithinTT1) + ", ";
        line += summary.get(sTotalCount_PostcodeChangeWithinTT4) + ", ";
        line += summary.get(sPercentageOfTT4_PostcodeChangeWithinTT4) + ", ";
        line += summary.get(sTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs) + ", ";
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs) + ", ";
        line += summary.get(sTotalCount_HBTTsToCTBTTs) + ", ";
        line += summary.get(sPercentageOfHB_HBTTsToCTBTTs) + ", ";
        // General CTB related
        line += summary.get(sCTBTotalCount_TTChangeClaimant) + ", ";
        line += summary.get(sCTBPercentageOfCTB_TTChangeClaimant) + ", ";
        line += summary.get(sTotalCount_SocialTTsToCTBTTs) + ", ";
        line += summary.get(sPercentageOfSocialTTs_SocialTTsToCTBTTs) + ", ";
        line += summary.get(sTotalCount_TT1ToCTBTTs) + ", ";
        line += summary.get(sPercentageOfTT1_TT1ToCTBTTs) + ", ";
        line += summary.get(sTotalCount_TT4ToCTBTTs) + ", ";
        line += summary.get(sPercentageOfTT4_TT4ToCTBTTs) + ", ";
        line += summary.get(sTotalCount_PrivateDeregulatedTTsToCTBTTs) + ", ";
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs) + ", ";
        line += summary.get(sTotalCountCTBTTsToSocialTTs) + ", ";
        line += summary.get(sPercentageOfCTB_CTBTTsToSocialTTs) + ", ";
        line += summary.get(sTotalCount_CTBTTsToTT1) + ", ";
        line += summary.get(sPercentageOfCTB_CTBTTsToTT1) + ", ";
        line += summary.get(sTotalCount_CTBTTsToTT4) + ", ";
        line += summary.get(sPercentageOfCTB_CTBTTsToTT4) + ", ";
        line += summary.get(sTotalCount_CTBTTsToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sPercentageOfCTB_CTBTTsToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sTotalCount_CTBTTsToHBTTs) + ", ";
        line += summary.get(sPercentageOfCTB_CTBTTsToHBTTs) + ", ";
        return line;
    }

    /**
     * Provides comparisons with the previous period
     *
     * @param summaryTable
     * @param paymentType
     * @param includeKey
     * @param underOccupancy
     * @param nTT
     * @param nEG
     */
    public void writeSummaryTableCompare2TimesTT(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        TreeMap<String, File> ONSPDFiles;
        ONSPDFiles = DW_Postcode_Handler.getONSPDFiles();
        String name;
        name = "Compare2TimesTT";
        PrintWriter pw;
        pw = getPrintWriter(name, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = getHeaderCompare2TimesGeneric(underOccupancy);
        header += getHeaderCompare2TimesTTChange();
        header = header.substring(0, header.length() - 2);
        pw.println(header);
        Iterator<String> ite;
        ite = summaryTable.keySet().iterator();
        while (ite.hasNext()) {
            String key;
            key = ite.next();
            HashMap<String, String> summary;
            summary = summaryTable.get(key);
            String line;
            line = getLineCompare2TimesGeneric(
                    summary,
                    ONSPDFiles,
                    underOccupancy);
            // General
            // All
            line += getLineCompare2TimesTTChange(summary);
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public String getHeaderCompare2TimesGeneric(boolean underOccupancy) {
        String header;
        header = "";
        header += sSHBEFilename0 + ", ";
        header += "Year0-Month0, ";
        header += "Month0 Year0, ";
        header += sSHBEFilename1 + ", ";
        header += "Year1-Month1, ";
        header += "Month1 Year1, ";
        header += "PostCodeLookupDate0, PostCodeLookupFile0, "
                + "PostCodeLookupDate1, PostCodeLookupFile1, ";
        if (underOccupancy) {
            header += sCouncilFilename0 + ", ";
            header += sRSLFilename0 + ", ";
            header += sCouncilCount0 + ", ";
            header += sCouncilLinkedRecordCount0 + ", ";
            header += sRSLCount0 + ", ";
            header += sRSLLinkedRecordCount0 + ", ";
            header += sAllUOCount0 + ", ";
            header += sAllUOLinkedRecordCount0 + ", ";
            header += sCouncilFilename1 + ", ";
            header += sRSLFilename1 + ", ";
            header += sCouncilCount1 + ", ";
            header += sCouncilLinkedRecordCount1 + ", ";
            header += sRSLCount1 + ", ";
            header += sRSLLinkedRecordCount1 + ", ";
            header += sAllUOCount1 + ", ";
            header += sAllUOLinkedRecordCount1 + ", ";
        }
        header += sAllCount0 + ", ";
        header += sHBCount0 + ", ";
        header += sCTBCount0 + ", ";
        header += sAllCount1 + ", ";
        header += sHBCount1 + ", ";
        header += sCTBCount1 + ", ";
        header += "Month0 Year0 to Month1 Year1, ";
        return header;
    }

    public String getLineCompare2TimesGeneric(
            HashMap<String, String> summary,
            TreeMap<String, File> ONSPDFiles,
            boolean underOccupancy) {
        String line = "";
        String filename0;
        filename0 = summary.get(sSHBEFilename0);
        line += filename0 + ", ";
        String month0;
        String year0;
        if (filename0 != null) {
            line += DW_SHBE_Handler.getYearMonthNumber(filename0) + ", ";
            month0 = DW_SHBE_Handler.getMonth3(filename0);
            year0 = DW_SHBE_Handler.getYear(filename0);
            line += month0 + " " + year0 + ", ";
        } else {
            month0 = "null";
            year0 = "null";
            line += "null, ";
            line += "null, ";
        }
        String filename1;
        filename1 = summary.get(sSHBEFilename1);
        line += filename1 + ", ";
        line += DW_SHBE_Handler.getYearMonthNumber(filename1) + ", ";
        String month1;
        String year1;
        month1 = DW_SHBE_Handler.getMonth3(filename1);
        year1 = DW_SHBE_Handler.getYear(filename1);
        line += month1 + " " + year1 + ", ";
        String PostCodeLookupDate0 = null;
        String PostCodeLookupFile0Name = null;
        if (filename0 != null) {
            PostCodeLookupDate0 = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                    DW_SHBE_Handler.getYM3(filename0));
            PostCodeLookupFile0Name = ONSPDFiles.get(PostCodeLookupDate0).getName();
        }
        line += PostCodeLookupDate0 + ", " + PostCodeLookupFile0Name + ", ";
        String PostCodeLookupDate1 = null;
        String PostCodeLookupFile1Name = null;
        PostCodeLookupDate1 = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                DW_SHBE_Handler.getYM3(filename1));
        PostCodeLookupFile1Name = ONSPDFiles.get(PostCodeLookupDate1).getName();
        line += PostCodeLookupDate1 + ", " + PostCodeLookupFile1Name + ", ";
        if (underOccupancy) {
            line += summary.get(sCouncilFilename0) + ", ";
            line += summary.get(sRSLFilename0) + ", ";
            line += summary.get(sCouncilCount0) + ", ";
            line += summary.get(sCouncilLinkedRecordCount0) + ", ";
            line += summary.get(sRSLCount0) + ", ";
            line += summary.get(sRSLLinkedRecordCount0) + ", ";
            line += summary.get(sAllUOCount0) + ", ";
            line += summary.get(sAllUOLinkedRecordCount0) + ", ";
            line += summary.get(sCouncilFilename1) + ", ";
            line += summary.get(sRSLFilename1) + ", ";
            line += summary.get(sCouncilCount1) + ", ";
            line += summary.get(sCouncilLinkedRecordCount1) + ", ";
            line += summary.get(sRSLCount1) + ", ";
            line += summary.get(sRSLLinkedRecordCount1) + ", ";
            line += summary.get(sAllUOCount1) + ", ";
            line += summary.get(sAllUOLinkedRecordCount1) + ", ";
        }
        line += summary.get(sAllCount0) + ", ";
        line += summary.get(sHBCount0) + ", ";
        line += summary.get(sCTBCount0) + ", ";
        line += summary.get(sAllCount1) + ", ";
        line += summary.get(sHBCount1) + ", ";
        line += summary.get(sCTBCount1) + ", ";
        line += month0 + " " + year0 + " to " + month1 + " " + year1 + ", ";
        return line;
    }

    /**
     * Provides comparisons with the previous period
     *
     * @param summaryTable
     * @param paymentType
     * @param includeKey
     * @param underOccupancy
     * @param nTT
     * @param nEG
     */
    public void writeSummaryTableCompare2TimesPostcode(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        TreeMap<String, File> ONSPDFiles;
        ONSPDFiles = DW_Postcode_Handler.getONSPDFiles();
        String name;
        name = "Compare2TimesPostcode";
        PrintWriter pw;
        pw = getPrintWriter(name, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = getHeaderCompare2TimesGeneric(underOccupancy);
        header += getHeaderCompare2TimesPostcodeChange();
        header = header.substring(0, header.length() - 2);
        pw.println(header);
        Iterator<String> ite;
        ite = summaryTable.keySet().iterator();
        while (ite.hasNext()) {
            String key;
            key = ite.next();
            HashMap<String, String> summary;
            summary = summaryTable.get(key);
            String line;
            line = getLineCompare2TimesGeneric(
                    summary,
                    ONSPDFiles,
                    underOccupancy);
            line += getLineCompare2TimesPostcodeChange(summary);
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimeGenericCounts(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG,
            int nPSI
    ) {
        TreeMap<String, File> ONSPDFiles;
        ONSPDFiles = DW_Postcode_Handler.getONSPDFiles();
        String name;
        name = "SingleTimeGenericCounts";
        PrintWriter pw;
        pw = getPrintWriter(name, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric(underOccupancy);
        if (!underOccupancy) {
            header += "PostCodeLookupDate, ";
            header += "PostCodeLookupFile, ";
            header += DW_SHBE_Collection.sLineCount + ", ";
            header += DW_SHBE_Collection.sCountDRecords + ", ";
            header += DW_SHBE_Collection.sCountSRecords + ", ";
            header += DW_SHBE_Collection.sCountUniqueClaimants + ", ";
            header += DW_SHBE_Collection.sCountUniquePartners + ", ";
            header += DW_SHBE_Collection.sCountUniqueDependents + ", ";
            header += DW_SHBE_Collection.sCountUniqueNonDependents + ", ";
            header += DW_SHBE_Collection.sCountUniqueIndividuals + ", ";
        }
        header += sAllTotalHouseholdSize + ", ";
        header += sAllAverageHouseholdSize + ", ";
        header += sHBTotalHouseholdSize + ", ";
        header += sHBAverageHouseholdSize + ", ";
        header += sCTBTotalHouseholdSize + ", ";
        header += sCTBAverageHouseholdSize + ", ";
        header += sAllPostcodeValidFormatCount + ", ";
        header += sAllPostcodeValidCount + ", ";
        header += sHBPostcodeValidFormatCount + ", ";
        header += sHBPostcodeValidCount + ", ";
        header += sCTBPostcodeValidFormatCount + ", ";
        header += sCTBPostcodeValidCount + ", ";
        header = header.substring(0, header.length() - 2);
        pw.println(header);
        Iterator<String> ite;
        ite = summaryTable.keySet().iterator();
        while (ite.hasNext()) {
            String key;
            key = ite.next();
            String line;
            line = "";
            HashMap<String, String> summary;
            summary = summaryTable.get(key);
            header = "";
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + ", ";
            line += getLineSingleTimeGeneric(key, summary, underOccupancy);
            if (!underOccupancy) {
                line += getPostcodeLookupDateAndFilenameLinePart(filename1, ONSPDFiles);
                line += summary.get(DW_SHBE_Collection.sLineCount) + ", ";
                line += summary.get(DW_SHBE_Collection.sCountDRecords) + ", ";
                line += summary.get(DW_SHBE_Collection.sCountSRecords) + ", ";
                line += summary.get(DW_SHBE_Collection.sCountUniqueClaimants) + ", ";
                line += summary.get(DW_SHBE_Collection.sCountUniquePartners) + ", ";
                line += summary.get(DW_SHBE_Collection.sCountUniqueDependents) + ", ";
                line += summary.get(DW_SHBE_Collection.sCountUniqueNonDependents) + ", ";
                line += summary.get(DW_SHBE_Collection.sCountUniqueIndividuals) + ", ";
            }
            line += summary.get(sAllTotalHouseholdSize) + ", ";
            line += summary.get(sAllAverageHouseholdSize) + ", ";
            line += summary.get(sHBTotalHouseholdSize) + ", ";
            line += summary.get(sHBAverageHouseholdSize) + ", ";
            line += summary.get(sCTBTotalHouseholdSize) + ", ";
            line += summary.get(sCTBAverageHouseholdSize) + ", ";
            line += summary.get(sAllPostcodeValidFormatCount) + ", ";
            line += summary.get(sAllPostcodeValidCount) + ", ";
            line += summary.get(sHBPostcodeValidFormatCount) + ", ";
            line += summary.get(sHBPostcodeValidCount) + ", ";
            line += summary.get(sCTBPostcodeValidFormatCount) + ", ";
            line += summary.get(sCTBPostcodeValidCount) + ", ";
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimeEntitlementEligibleAmountContractualAmount(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeEntitlementEligibleAmountContractualAmount";
        PrintWriter pw;
        pw = getPrintWriter(name, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric(underOccupancy);
        header += sTotalWeeklyHBEntitlement + ", ";
        header += sTotalCountWeeklyHBEntitlementNonZero + ", ";
        header += sTotalCountWeeklyHBEntitlementZero + ", ";
        header += sAverageWeeklyHBEntitlement + ", ";
        // WeeklyEligibleRentAmount
        header += sAllTotalWeeklyEligibleRentAmount + ", ";
        header += sAllTotalCountWeeklyEligibleRentAmountNonZero + ", ";
        header += sAllTotalCountWeeklyEligibleRentAmountZero + ", ";
        header += sAllAverageWeeklyEligibleRentAmount + ", ";
        header += sHBTotalWeeklyEligibleRentAmount + ", ";
        header += sHBTotalCountWeeklyEligibleRentAmountNonZero + ", ";
        header += sHBTotalCountWeeklyEligibleRentAmountZero + ", ";
        header += sHBAverageWeeklyEligibleRentAmount + ", ";
        header += sCTBTotalWeeklyEligibleRentAmount + ", ";
        header += sCTBTotalCountWeeklyEligibleRentAmountNonZero + ", ";
        header += sCTBTotalCountWeeklyEligibleRentAmountZero + ", ";
        header += sCTBAverageWeeklyEligibleRentAmount + ", ";
        // WeeklyEligibleCouncilTaxAmount
        header += sAllTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sAllTotalCountWeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sAllTotalCountWeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sAllAverageWeeklyEligibleCouncilTaxAmount + ", ";
        header += sHBTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sHBTotalCountWeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sHBTotalCountWeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sHBAverageWeeklyEligibleCouncilTaxAmount + ", ";
        header += sCTBTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sCTBTotalCountWeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sCTBTotalCountWeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sCTBAverageWeeklyEligibleCouncilTaxAmount + ", ";
        // ContractualRentAmount
        header += sAllTotalContractualRentAmount + ", ";
        header += sAllTotalCountContractualRentAmountNonZeroCount + ", ";
        header += sAllTotalCountContractualRentAmountZeroCount + ", ";
        header += sAllAverageContractualRentAmount + ", ";
        header += sHBTotalContractualRentAmount + ", ";
        header += sHBTotalCountContractualRentAmountNonZeroCount + ", ";
        header += sHBTotalCountContractualRentAmountZeroCount + ", ";
        header += sHBAverageContractualRentAmount + ", ";
        header += sCTBTotalContractualRentAmount + ", ";
        header += sCTBTotalCountContractualRentAmountNonZeroCount + ", ";
        header += sCTBTotalCountContractualRentAmountZeroCount + ", ";
        header += sCTBAverageContractualRentAmount + ", ";
        // WeeklyAdditionalDiscretionaryPayment
        header += sAllTotalWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sAllTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sAllTotalCountWeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sAllAverageWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sHBTotalWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sHBTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sHBTotalCountWeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sHBAverageWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sCTBTotalWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sCTBAverageWeeklyAdditionalDiscretionaryPayment + ", ";
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        header += sAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sAllTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sAllTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sHBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sHBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header = header.substring(0, header.length() - 2);
        pw.println(header);
        Iterator<String> ite;
        ite = summaryTable.keySet().iterator();
        while (ite.hasNext()) {
            String key;
            key = ite.next();
            String line;
            line = "";
            HashMap<String, String> summary;
            summary = summaryTable.get(key);
            header = "";
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + ", ";
            line += getLineSingleTimeGeneric(key, summary, underOccupancy);
            line += summary.get(sTotalWeeklyHBEntitlement) + ", ";
            line += summary.get(sTotalCountWeeklyHBEntitlementNonZero) + ", ";
            line += summary.get(sTotalCountWeeklyHBEntitlementZero) + ", ";
            line += summary.get(sAverageWeeklyHBEntitlement) + ", ";
            // WeeklyEligibleRentAmount
            line += summary.get(sAllTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sAllTotalCountWeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sAllTotalCountWeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sAllAverageWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sHBTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sHBTotalCountWeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sHBTotalCountWeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sHBAverageWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sCTBTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sCTBTotalCountWeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sCTBTotalCountWeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sCTBAverageWeeklyEligibleRentAmount) + ", ";
            // WeeklyEligibleCouncilTaxAmount
            line += summary.get(sAllTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sAllTotalCountWeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sAllTotalCountWeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sAllAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sHBTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sHBTotalCountWeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sHBTotalCountWeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sHBAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sCTBTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sCTBTotalCountWeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sCTBTotalCountWeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sCTBAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            // ContractualRentAmount
            line += summary.get(sAllTotalContractualRentAmount) + ", ";
            line += summary.get(sAllTotalCountContractualRentAmountNonZeroCount) + ", ";
            line += summary.get(sAllTotalCountContractualRentAmountZeroCount) + ", ";
            line += summary.get(sAllAverageContractualRentAmount) + ", ";
            line += summary.get(sHBTotalContractualRentAmount) + ", ";
            line += summary.get(sHBTotalCountContractualRentAmountNonZeroCount) + ", ";
            line += summary.get(sHBTotalCountContractualRentAmountZeroCount) + ", ";
            line += summary.get(sHBAverageContractualRentAmount) + ", ";
            line += summary.get(sCTBTotalContractualRentAmount) + ", ";
            line += summary.get(sCTBTotalCountContractualRentAmountNonZeroCount) + ", ";
            line += summary.get(sCTBTotalCountContractualRentAmountZeroCount) + ", ";
            line += summary.get(sCTBAverageContractualRentAmount) + ", ";
            // WeeklyAdditionalDiscretionaryPayment
            line += summary.get(sAllTotalWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sAllTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sAllTotalCountWeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sAllAverageWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sHBTotalWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sHBTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sHBTotalCountWeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sHBAverageWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sCTBTotalWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sCTBAverageWeeklyAdditionalDiscretionaryPayment) + ", ";
            // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
            line += summary.get(sAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sAllTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sAllTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sHBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sHBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimeEmploymentEducationTraining(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeEmploymentEducationTraining";
        PrintWriter pw;
        pw = getPrintWriter(name, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric(underOccupancy);
        header += sAllTotalCount_ClaimantsEmployed + ", ";
        header += sAllPercentage_ClaimantsEmployed + ", ";
        header += sAllTotalCount_ClaimantsSelfEmployed + ", ";
        header += sAllPercentage_ClaimantsSelfEmployed + ", ";
        header += sAllTotalCount_ClaimantsStudents + ", ";
        header += sAllPercentage_ClaimantsStudents + ", ";
        header += sHBTotalCountClaimantsEmployed + ", ";
        header += sHBPercentageOfHB_ClaimantsEmployed + ", ";
        header += sHBTotalCountClaimantsSelfEmployed + ", ";
        header += sHBPercentageOfHB_ClaimantsSelfEmployed + ", ";
        header += sHBTotalCountClaimantsStudents + ", ";
        header += sHBPercentageOfHB_ClaimantsStudents + ", ";
        header += sCTBTotalCount_ClaimantsEmployed + ", ";
        header += sCTBPercentageOfCTB_ClaimantsEmployed + ", ";
        header += sCTBTotalCountClaimantsSelfEmployed + ", ";
        header += sCTBPercentageOfCTB_ClaimantsSelfEmployed + ", ";
        header += sCTBTotalCountClaimantsStudents + ", ";
        header += sCTBPercentageOfCTB_ClaimantsStudents + ", ";
        header = header.substring(0, header.length() - 2);
        pw.println(header);
        Iterator<String> ite;
        ite = summaryTable.keySet().iterator();
        while (ite.hasNext()) {
            String key;
            key = ite.next();
            String line;
            line = "";
            HashMap<String, String> summary;
            summary = summaryTable.get(key);
            header = "";
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + ", ";
            line += getLineSingleTimeGeneric(key, summary, underOccupancy);
            line += summary.get(sAllTotalCount_ClaimantsEmployed) + ", ";
            line += summary.get(sAllPercentage_ClaimantsEmployed) + ", ";
            line += summary.get(sAllTotalCount_ClaimantsSelfEmployed) + ", ";
            line += summary.get(sAllPercentage_ClaimantsSelfEmployed) + ", ";
            line += summary.get(sAllTotalCount_ClaimantsStudents) + ", ";
            line += summary.get(sAllPercentage_ClaimantsStudents) + ", ";
            line += summary.get(sHBTotalCountClaimantsEmployed) + ", ";
            line += summary.get(sHBPercentageOfHB_ClaimantsEmployed) + ", ";
            line += summary.get(sHBTotalCountClaimantsSelfEmployed) + ", ";
            line += summary.get(sHBPercentageOfHB_ClaimantsSelfEmployed) + ", ";
            line += summary.get(sHBTotalCountClaimantsStudents) + ", ";
            line += summary.get(sHBPercentageOfHB_ClaimantsStudents) + ", ";
            line += summary.get(sCTBTotalCount_ClaimantsEmployed) + ", ";
            line += summary.get(sCTBPercentageOfCTB_ClaimantsEmployed) + ", ";
            line += summary.get(sCTBTotalCountClaimantsSelfEmployed) + ", ";
            line += summary.get(sCTBPercentageOfCTB_ClaimantsSelfEmployed) + ", ";
            line += summary.get(sCTBTotalCountClaimantsStudents) + ", ";
            line += summary.get(sCTBPercentageOfCTB_ClaimantsStudents) + ", ";
            line += summary.get(sCTBTotalCount_LHACases) + ", ";
            line += summary.get(sCTBPercentageOfCTB_LHACases) + ", ";
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    private String getPostcodeLookupDateAndFilenameLinePart(
            String filename,
            TreeMap<String, File> ONSPDFiles) {
        String result;
        String PostCodeLookupDate0 = null;
        String PostCodeLookupFile0Name = null;
        if (filename != null) {
            PostCodeLookupDate0 = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                    DW_SHBE_Handler.getYM3(filename));
            PostCodeLookupFile0Name = ONSPDFiles.get(PostCodeLookupDate0).getName();
        }
        result = PostCodeLookupDate0 + ", " + PostCodeLookupFile0Name + ", ";
        return result;
    }

    public void writeSummaryTableSingleTimeRentAndIncome(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeRentAndIncome";
        PrintWriter pw;
        pw = getPrintWriter(name, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric(underOccupancy);
        header += sAllTotalIncome + ", ";
        header += sAllTotalCount_IncomeNonZero + ", ";
        header += sAllTotalCountIncomeZero + ", ";
        header += sAllAverageIncome + ", ";
        header += sAllTotalWeeklyEligibleRentAmount + ", ";
        header += sAllTotalCountWeeklyEligibleRentAmountNonZero + ", ";
        header += sAllAverageWeeklyEligibleRentAmount + ", ";
        // HB
        header += sHBTotalIncome + ", ";
        header += sHBTotalCount_IncomeNonZero + ", ";
        header += sHBTotalCount_IncomeZero + ", ";
        header += sHBAverageIncome + ", ";
        header += sHBTotalWeeklyEligibleRentAmount + ", ";
        header += sHBTotalCountWeeklyEligibleRentAmountNonZero + ", ";
        header += sHBAverageWeeklyEligibleRentAmount + ", ";
        // CTB
        header += sCTBTotalIncome + ", ";
        header += sCTBTotalCount_IncomeNonZero + ", ";
        header += sCTBTotalCount_IncomeZero + ", ";
        header += sCTBAverageIncome + ", ";
        header += sCTBTotalWeeklyEligibleRentAmount + ", ";
        header += sCTBTotalCountWeeklyEligibleRentAmountNonZero + ", ";
        header += sCTBAverageWeeklyEligibleRentAmount + ", ";
        for (int i = 1; i < nTT; i++) {
            header += sAllTotalIncome + "TT" + i + ", ";
            header += sAllTotalCount_IncomeNonZero + "TT" + i + ", ";
            header += sAllTotalCountIncomeZero + "TT" + i + ", ";
            header += sAllAverageIncome + "TT" + i + ", ";
            header += sAllTotalWeeklyEligibleRentAmount + "TT" + i + ", ";
            header += sAllTotalCountWeeklyEligibleRentAmountNonZero + "TT" + i + ", ";
            header += sAllAverageWeeklyEligibleRentAmount + "TT" + i + ", ";
        }
        header = header.substring(0, header.length() - 2);
        pw.println(header);
        Iterator<String> ite;
        ite = summaryTable.keySet().iterator();
        while (ite.hasNext()) {
            String key;
            key = ite.next();
            String line;
            line = "";
            HashMap<String, String> summary;
            summary = summaryTable.get(key);
            header = "";
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + ", ";
            line += getLineSingleTimeGeneric(key, summary, underOccupancy);
            // All
            line += summary.get(sAllTotalIncome) + ", ";
            line += summary.get(sAllTotalCount_IncomeNonZero) + ", ";
            line += summary.get(sAllTotalCountIncomeZero) + ", ";
            line += summary.get(sAllAverageIncome) + ", ";
            line += summary.get(sAllTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sAllTotalCountWeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sAllAverageWeeklyEligibleRentAmount) + ", ";
            // HB
            line += summary.get(sHBTotalIncome) + ", ";
            line += summary.get(sHBTotalCount_IncomeNonZero) + ", ";
            line += summary.get(sHBTotalCount_IncomeZero) + ", ";
            line += summary.get(sHBAverageIncome) + ", ";
            line += summary.get(sHBTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sHBTotalCountWeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sHBAverageWeeklyEligibleRentAmount) + ", ";
            // CTB
            line += summary.get(sCTBTotalIncome) + ", ";
            line += summary.get(sCTBTotalCount_IncomeNonZero) + ", ";
            line += summary.get(sCTBTotalCount_IncomeZero) + ", ";
            line += summary.get(sCTBAverageIncome) + ", ";
            line += summary.get(sCTBTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sCTBTotalCountWeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sCTBAverageWeeklyEligibleRentAmount) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sAllTotalIncome + "TT" + i) + ", ";
                line += summary.get(sAllTotalCount_IncomeNonZero + "TT" + i) + ", ";
                line += summary.get(sAllTotalCountIncomeZero + "TT" + i) + ", ";
                line += summary.get(sAllAverageIncome + "TT" + i) + ", ";
                line += summary.get(sAllTotalWeeklyEligibleRentAmount + "TT" + i) + ", ";
                line += summary.get(sAllTotalCountWeeklyEligibleRentAmountNonZero + "TT" + i) + ", ";
                line += summary.get(sAllAverageWeeklyEligibleRentAmount + "TT" + i) + ", ";
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimeRentArrears(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeCouncilRentArrears";
        PrintWriter pw;
        pw = getPrintWriter(name, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric(underOccupancy);
        header += sTotal_RentArrears + ", ";
        header += sTotalCount_RentArrearsNonZero + ", ";
        header += sTotalCount_RentArrearsZero + ", ";
        header += sAverage_RentArrears + ", ";
        header += sAverage_NonZeroRentArrears + ", ";
        header = header.substring(0, header.length() - 2);
        pw.println(header);
        Iterator<String> ite;
        ite = summaryTable.keySet().iterator();
        while (ite.hasNext()) {
            String key;
            key = ite.next();
            String line;
            line = "";
            HashMap<String, String> summary;
            summary = summaryTable.get(key);
            header = "";
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + ", ";
            line += getLineSingleTimeGeneric(key,
                    summary, underOccupancy);
            line += summary.get(sTotal_RentArrears) + ", ";
            line += summary.get(sTotalCount_RentArrearsNonZero) + ", ";
            line += summary.get(sTotalCount_RentArrearsZero) + ", ";
            line += summary.get(sAverage_RentArrears) + ", ";
            line += summary.get(sAverage_NonZeroRentArrears) + ", ";
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimeEthnicity(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeEthnicity";
        PrintWriter pw;
        pw = getPrintWriter(name, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric(underOccupancy);
        for (int i = 1; i < nEG; i++) {
            header += sAllTotalCountEthnicGroupClaimant[i] + ", ";
            header += sAllPercentageEthnicGroupClaimant[i] + ", ";
        }
        for (int i = 1; i < nEG; i++) {
            header += sHBTotalCount_EthnicGroupClaimant[i] + ", ";
            header += sHBPercentage_EthnicGroupClaimant[i] + ", ";
        }
        for (int i = 1; i < nEG; i++) {
            header += sCTBTotalCountEthnicGroupClaimant[i] + ", ";
            header += sCTBPercentageEthnicGroupClaimant[i] + ", ";
        }
        header = header.substring(0, header.length() - 2);
        pw.println(header);
        Iterator<String> ite;
        ite = summaryTable.keySet().iterator();
        while (ite.hasNext()) {
            String key;
            key = ite.next();
            String line;
            line = "";
            HashMap<String, String> summary;
            summary = summaryTable.get(key);
            header = "";
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + ", ";
            line += getLineSingleTimeGeneric(key,
                    summary, underOccupancy);
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sAllTotalCountEthnicGroupClaimant[i]) + ", ";
                line += summary.get(sAllPercentageEthnicGroupClaimant[i]) + ", ";
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sHBTotalCount_EthnicGroupClaimant[i]) + ", ";
                line += summary.get(sHBPercentage_EthnicGroupClaimant[i]) + ", ";
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sCTBTotalCountEthnicGroupClaimant[i]) + ", ";
                line += summary.get(sCTBPercentageEthnicGroupClaimant[i]) + ", ";
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimeTT(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeTT";
        PrintWriter pw;
        pw = getPrintWriter(name, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric(underOccupancy);
        header += sTotalCount_SocialTTsClaimant + ", ";
        header += sPercentageOfAll_SocialTTsClaimant + ", ";
        header += sPercentageOfHB_SocialTTsClaimant + ", ";
        header += sTotalCount_PrivateDeregulatedTTsClaimant + ", ";
        header += sPercentageOfAll_PrivateDeregulatedTTsClaimant + ", ";
        header += sPercentageOfHB_PrivateDeregulatedTTsClaimant + ", ";
        for (int i = 1; i < nTT; i++) {
            header += sTotalCount_ClaimantsTT[i] + ", ";
            header += sPercentageOfAll_ClaimantsTT[i] + ", ";
            if ((i == 5 || i == 7)) {
                header += sPercentageOfCTB_ClaimantTT[i] + ", ";
            } else {
                header += sPercentageOfHB_ClaimantTT[i] + ", ";
            }
        }
        header += sAllTotalCount_LHACases + ", ";
        header += sAllPercentageOfAll_LHACases + ", ";
        header += sHBTotalCount_LHACases + ", ";
        header += sHBPercentageOfHB_LHACases + ", ";
        header += sCTBTotalCount_LHACases + ", ";
        header += sCTBPercentageOfCTB_LHACases + ", ";
        header = header.substring(0, header.length() - 2);
        pw.println(header);
        Iterator<String> ite;
        ite = summaryTable.keySet().iterator();
        while (ite.hasNext()) {
            String key;
            key = ite.next();
            String line;
            line = "";
            HashMap<String, String> summary;
            summary = summaryTable.get(key);
            header = "";
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + ", ";
            line += getLineSingleTimeGeneric(key,
                    summary, underOccupancy);
            line += summary.get(sTotalCount_SocialTTsClaimant) + ", ";
            line += summary.get(sPercentageOfAll_SocialTTsClaimant) + ", ";
            line += summary.get(sPercentageOfHB_SocialTTsClaimant) + ", ";
            line += summary.get(sTotalCount_PrivateDeregulatedTTsClaimant) + ", ";
            line += summary.get(sPercentageOfAll_PrivateDeregulatedTTsClaimant) + ", ";
            line += summary.get(sPercentageOfHB_PrivateDeregulatedTTsClaimant) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sTotalCount_ClaimantsTT[i]) + ", ";
                line += summary.get(sPercentageOfAll_ClaimantsTT[i]) + ", ";
                if ((i == 5 || i == 7)) {
                    line += summary.get(sPercentageOfCTB_ClaimantTT[i]) + ", ";
                } else {
                    line += summary.get(sPercentageOfHB_ClaimantTT[i]) + ", ";
                }
            }
            line += summary.get(sAllTotalCount_LHACases) + ", ";
            line += summary.get(sAllPercentageOfAll_LHACases) + ", ";
            line += summary.get(sHBTotalCount_LHACases) + ", ";
            line += summary.get(sHBPercentageOfHB_LHACases) + ", ";
            line += summary.get(sCTBTotalCount_LHACases) + ", ";
            line += summary.get(sCTBPercentageOfCTB_LHACases) + ", ";
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimePSI(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG,
            int nPSI
    ) {
        String name;
        name = "SingleTimePSI";
        PrintWriter pw;
        pw = getPrintWriter(name, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric(underOccupancy);
        for (int i = 1; i < nPSI; i++) {
            header += sAllTotalCount_PSI[i] + ", ";
            header += sAllPercentageOfAll_PSI[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            header += sHBTotalCount_PSI[i] + ", ";
            header += sHBPercentageOfHB_PSI[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            header += sCTBTotalCountPSI[i] + ", ";
            header += sCTBPercentageOfCTB_PSI[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            for (int j = 1; j < nTT; j++) {
                header += sAllTotalCount_PSIByTT[i][j] + ", ";
                header += sAllPercentageOfAll_PSIByTT[i][j] + ", ";
            }
        }
        for (int i = 1; i < nPSI; i++) {
            for (int j = 1; j < nTT; j++) {
                if (!(j == 5 || j == 7)) {
                    header += sHBTotalCount_PSIByTT[i][j] + ", ";
                    header += sHBPercentageOfHB_PSIByTT[i][j] + ", ";
                } else {
                    header += sCTBTotalCount_PSIByTT[i][j] + ", ";
                    header += sCTBPercentageOfCTB_PSIByTT[i][j] + ", ";
                }
            }
        }
        header = header.substring(0, header.length() - 2);
        pw.println(header);
        Iterator<String> ite;
        ite = summaryTable.keySet().iterator();
        while (ite.hasNext()) {
            String key;
            key = ite.next();
            String line;
            line = "";
            HashMap<String, String> summary;
            summary = summaryTable.get(key);
            header = "";
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + ", ";
            line += getLineSingleTimeGeneric(key,
                    summary, underOccupancy);
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sAllTotalCount_PSI[i]) + ", ";
                line += summary.get(sAllPercentageOfAll_PSI[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sHBTotalCount_PSI[i]) + ", ";
                line += summary.get(sHBPercentageOfHB_PSI[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sCTBTotalCountPSI[i]) + ", ";
                line += summary.get(sCTBPercentageOfCTB_PSI[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                for (int j = 1; j < nTT; j++) {
                    line += summary.get(sAllTotalCount_PSIByTT[i][j]) + ", ";
                    line += summary.get(sAllPercentageOfAll_PSIByTT[i][j]) + ", ";
                }
            }
            for (int i = 1; i < nPSI; i++) {
                for (int j = 1; j < nTT; j++) {
                    if (!(j == 5 || j == 7)) {
                        line += summary.get(sHBTotalCount_PSIByTT[i][j]) + ", ";
                        line += summary.get(sHBPercentageOfHB_PSIByTT[i][j]) + ", ";
                    } else {
                        line += summary.get(sCTBTotalCount_PSIByTT[i][j]) + ", ";
                        line += summary.get(sCTBPercentageOfCTB_PSIByTT[i][j]) + ", ";
                    }
                }
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimeDisability(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeDisability";
        PrintWriter pw;
        pw = getPrintWriter(name, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric(underOccupancy);
        // General DisabilityAward
        header += sTotalCount_DisabilityAward + ", ";
        header += sPercentageOfAll_DisabilityAward + ", ";
        header += sTotalCount_DisabilityAwardHBTTs + ", ";
        header += sPercentageOfAll_DisabilityAwardHBTTs + ", ";
        header += sPercentageOfHB_DisabilityAwardHBTTs + ", ";
        header += sTotalCount_DisabilityAwardCTBTTs + ", ";
        header += sPercentageOfAll_DisabilityAwardCTBTTs + ", ";
        header += sPercentageOfCTB_DisabilityAwardCTBTTs + ", ";
        // General DisabilityPremiumAward
        header += sTotalCount_DisabilityPremiumAward + ", ";
        header += sPercentageOfAll_DisabilityPremiumAward + ", ";
        header += sTotalCount_DisabilityPremiumAwardHBTTs + ", ";
        header += sPercentageOfAll_DisabilityPremiumAwardHBTTs + ", ";
        header += sPercentageOfHB_DisabilityPremiumAwardHBTTs + ", ";
        header += sTotalCount_DisabilityPremiumAwardCTBTTs + ", ";
        header += sPercentageOfAll_DisabilityPremiumAwardCTBTTs + ", ";
        header += sPercentageOfCTB_DisabilityPremiumAwardCTBTTs + ", ";
        // General SevereDisabilityPremiumAward
        header += sTotalCount_SevereDisabilityPremiumAward + ", ";
        header += sPercentageOfAll_SevereDisabilityPremiumAward + ", ";
        header += sTotalCount_SevereDisabilityPremiumAwardHBTTs + ", ";
        header += sPercentageOfAll_SevereDisabilityPremiumAwardHBTTs + ", ";
        header += sPercentageOfHB_SevereDisabilityPremiumAwardHBTTs + ", ";
        header += sTotalCount_SevereDisabilityPremiumAwardCTBTTs + ", ";
        header += sPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs + ", ";
        header += sPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs + ", ";
        // General DisabledChildPremiumAward
        header += sTotalCount_DisabledChildPremiumAward + ", ";
        header += sPercentageOfAll_DisabledChildPremiumAward + ", ";
        header += sTotalCount_DisabledChildPremiumAwardHBTTs + ", ";
        header += sPercentageOfAll_DisabledChildPremiumAwardHBTTs + ", ";
        header += sPercentageOfHB_DisabledChildPremiumAwardHBTTs + ", ";
        header += sTotalCount_DisabledChildPremiumAwardCTBTTs + ", ";
        header += sPercentageOfAll_DisabledChildPremiumAwardCTBTTs + ", ";
        header += sPercentageOfCTB_DisabledChildPremiumAwardCTBTTs + ", ";
        // General EnhancedDisabilityPremiumAward
        header += sTotalCount_EnhancedDisabilityPremiumAward + ", ";
        header += sPercentageOfAll_EnhancedDisabilityPremiumAward + ", ";
        header += sTotalCount_EnhancedDisabilityPremiumAwardHBTTs + ", ";
        header += sPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs + ", ";
        header += sPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs + ", ";
        header += sTotalCount_EnhancedDisabilityPremiumAwardCTBTTs + ", ";
        header += sPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs + ", ";
        header += sPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs + ", ";
        // DisabilityPremiumAwardSocialTTs
        header += sTotalCount_DisabilityAwardSocialTTs + ", ";
        header += sPercentageOfAll_DisabilityAwardSocialTTs + ", ";
        header += sPercentageOfHB_DisabilityAwardSocialTTs + ", ";
        header += sTotalCount_DisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfAll_DisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfHB_DisabilityPremiumAwardSocialTTs + ", ";
        header += sTotalCount_SevereDisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs + ", ";
        header += sTotalCount_DisabledChildPremiumAwardSocialTTs + ", ";
        header += sPercentageOfAll_DisabledChildPremiumAwardSocialTTs + ", ";
        header += sPercentageOfHB_DisabledChildPremiumAwardSocialTTs + ", ";
        header += sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs + ", ";
        // DisabilityPremiumAwardPrivateDeregulatedTTs
        header += sTotalCount_DisabilityAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs + ", ";
        header += sTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        for (int i = 1; i < nTT; i++) {
            // DisabilityAward
            //header += sAllTotalCount_DisabilityAwardByTT[i] + ", ";
            //header += sAllPercentageOfAll_DisabilityAwardByTT[i] + ", ";
            if ((i == 5 || i == 7)) {
                header += sCTBTotalCount_DisabilityAwardByTT[i] + ", ";
                header += sAllPercentageOfAll_DisabilityAwardByTT[i] + ", ";
                header += sCTBPercentageOfCTB_DisabilityAwardByTT[i] + ", ";
            } else {
                header += sHBTotalCount_DisabilityAwardByTT[i] + ", ";
                header += sAllPercentageOfAll_DisabilityAwardByTT[i] + ", ";
                header += sHBPercentageOfHB_DisabilityAwardByTT[i] + ", ";
            }
            // DisabilityPremiumAward
            //header += sAllTotalCount_DisabilityPremiumAwardByTT[i] + ", ";
            //header += sAllPercentageOfAll_DisabilityPremiumAwardByTT[i] + ", ";
            if ((i == 5 || i == 7)) {
                header += sCTBTotalCount_DisabilityPremiumAwardByTT[i] + ", ";
                header += sAllPercentageOfAll_DisabilityPremiumAwardByTT[i] + ", ";
                header += sCTBPercentageOfCTB_DisabilityPremiumAwardByTT[i] + ", ";
            } else {
                header += sHBTotalCount_DisabilityPremiumAwardByTT[i] + ", ";
                header += sAllPercentageOfAll_DisabilityPremiumAwardByTT[i] + ", ";
                header += sHBPercentageOfHB_DisabilityPremiumAwardByTT[i] + ", ";
            }
            // SevereDisabilityPremiumAward
            //header += sAllTotalCountSevereDisabilityPremiumAwardByTT[i] + ", ";
            //header += sAllPercentageOfAll_SevereDisabilityPremiumAwardByTT[i] + ", ";
            if ((i == 5 || i == 7)) {
                header += sCTBTotalCount_SevereDisabilityPremiumAwardByTT[i] + ", ";
                header += sAllPercentageOfAll_SevereDisabilityPremiumAwardByTT[i] + ", ";
                header += sCTBPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i] + ", ";
            } else {
                header += sHBTotalCount_SevereDisabilityPremiumAwardByTT[i] + ", ";
                header += sAllPercentageOfAll_SevereDisabilityPremiumAwardByTT[i] + ", ";
                header += sHBPercentageOfHB_SevereDisabilityPremiumAwardByTT[i] + ", ";
            }
            // DisabledChildPremiumAward
            //header += sAllTotalCountDisabledChildPremiumAwardByTT[i] + ", ";
            //header += sAllPercentageOfAll_DisabledChildPremiumAwardByTT[i] + ", ";
            if ((i == 5 || i == 7)) {
                header += sCTBTotalCount_DisabledChildPremiumAwardByTT[i] + ", ";
                header += sAllPercentageOfAll_DisabledChildPremiumAwardByTT[i] + ", ";
                header += sCTBPercentageOfCTB_DisabledChildPremiumAwardByTT[i] + ", ";
            } else {
                header += sHBTotalCount_DisabledChildPremiumAwardByTT[i] + ", ";
                header += sAllPercentageOfAll_DisabledChildPremiumAwardByTT[i] + ", ";
                header += sHBPercentageOfHB_DisabledChildPremiumAwardByTT[i] + ", ";
            }
            // EnhancedDisabilityPremiumAward
            //header += sAllTotalCount_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
            //header += sAllPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
            if ((i == 5 || i == 7)) {
                header += sCTBTotalCount_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
                header += sAllPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
                header += sCTBPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
            } else {
                header += sHBTotalCount_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
                header += sAllPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
                header += sHBPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
            }
        }
        header = header.substring(0, header.length() - 2);
        pw.println(header);
        Iterator<String> ite;
        ite = summaryTable.keySet().iterator();
        while (ite.hasNext()) {
            String key;
            key = ite.next();
            String line;
            line = "";
            HashMap<String, String> summary;
            summary = summaryTable.get(key);
            header = "";
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + ", ";
            line += getLineSingleTimeGeneric(key,
                    summary, underOccupancy);
            // General DisabilityAward
            line += summary.get(sTotalCount_DisabilityAward) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityAward) + ", ";
            line += summary.get(sTotalCount_DisabilityAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabilityAwardHBTTs) + ", ";
            line += summary.get(sTotalCount_DisabilityAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfCTB_DisabilityAwardCTBTTs) + ", ";
            // General DisabilityPremiumAward
            line += summary.get(sTotalCount_DisabilityPremiumAward) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityPremiumAward) + ", ";
            line += summary.get(sTotalCount_DisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sTotalCount_DisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfCTB_DisabilityPremiumAwardCTBTTs) + ", ";
            // General SevereDisabilityPremiumAward
            line += summary.get(sTotalCount_SevereDisabilityPremiumAward) + ", ";
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAward) + ", ";
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfHB_SevereDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs) + ", ";
            // General DisabledChildPremiumAward
            line += summary.get(sTotalCount_DisabledChildPremiumAward) + ", ";
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAward) + ", ";
            line += summary.get(sTotalCount_DisabledChildPremiumAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabledChildPremiumAwardHBTTs) + ", ";
            line += summary.get(sTotalCount_DisabledChildPremiumAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfCTB_DisabledChildPremiumAwardCTBTTs) + ", ";
            // General EnhancedDisabilityPremiumAward
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAward) + ", ";
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAward) + ", ";
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs) + ", ";
            // DisabilityPremiumAwardSocialTTs
            line += summary.get(sTotalCount_DisabilityAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabilityAwardSocialTTs) + ", ";
            line += summary.get(sTotalCount_DisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sTotalCount_DisabledChildPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabledChildPremiumAwardSocialTTs) + ", ";
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs) + ", ";
            // DisabilityPremiumAwardPrivateDeregulatedTTs
            line += summary.get(sTotalCount_DisabilityAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            for (int i = 1; i < nTT; i++) {
                // DisabilityAward
                //line += summary.get(sAllTotalCount_DisabilityAwardByTT[i]) + ", ";
                //line += summary.get(sAllPercentageOfAll_DisabilityAwardByTT[i]) + ", ";
                if ((i == 5 || i == 7)) {
                    line += summary.get(sCTBTotalCount_DisabilityAwardByTT[i]) + ", ";
                    line += summary.get(sAllPercentageOfAll_DisabilityAwardByTT[i]) + ", ";
                    line += summary.get(sCTBPercentageOfCTB_DisabilityAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sHBTotalCount_DisabilityAwardByTT[i]) + ", ";
                    line += summary.get(sAllPercentageOfAll_DisabilityAwardByTT[i]) + ", ";
                    line += summary.get(sHBPercentageOfHB_DisabilityAwardByTT[i]) + ", ";
                }
                // DisabilityPremiumAward
                //line += summary.get(sAllTotalCount_DisabilityPremiumAwardByTT[i]) + ", ";
                //line += summary.get(sAllPercentageOfAll_DisabilityPremiumAwardByTT[i]) + ", ";
                if ((i == 5 || i == 7)) {
                    line += summary.get(sCTBTotalCount_DisabilityPremiumAwardByTT[i]) + ", ";
                    line += summary.get(sAllPercentageOfAll_DisabilityPremiumAwardByTT[i]) + ", ";
                    line += summary.get(sCTBPercentageOfCTB_DisabilityPremiumAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sHBTotalCount_DisabilityPremiumAwardByTT[i]) + ", ";
                    line += summary.get(sAllPercentageOfAll_DisabilityPremiumAwardByTT[i]) + ", ";
                    line += summary.get(sHBPercentageOfHB_DisabilityPremiumAwardByTT[i]) + ", ";
                }
                // SevereDisabilityPremiumAward
                //line += summary.get(sAllTotalCountSevereDisabilityPremiumAwardByTT[i]) + ", ";
                //line += summary.get(sAllPercentageOfAll_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                if ((i == 5 || i == 7)) {
                    line += summary.get(sCTBTotalCount_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                    line += summary.get(sAllPercentageOfAll_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                    line += summary.get(sCTBPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sHBTotalCount_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                    line += summary.get(sAllPercentageOfAll_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                    line += summary.get(sHBPercentageOfHB_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                }
                // DisabledChildPremiumAward
                //line += summary.get(sAllTotalCountDisabledChildPremiumAwardByTT[i]) + ", ";
                //line += summary.get(sAllPercentageOfAll_DisabledChildPremiumAwardByTT[i]) + ", ";
                if ((i == 5 || i == 7)) {
                    line += summary.get(sCTBTotalCount_DisabledChildPremiumAwardByTT[i]) + ", ";
                    line += summary.get(sAllPercentageOfAll_DisabledChildPremiumAwardByTT[i]) + ", ";
                    line += summary.get(sCTBPercentageOfCTB_DisabledChildPremiumAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sHBTotalCount_DisabledChildPremiumAwardByTT[i]) + ", ";
                    line += summary.get(sAllPercentageOfAll_DisabledChildPremiumAwardByTT[i]) + ", ";
                    line += summary.get(sHBPercentageOfHB_DisabledChildPremiumAwardByTT[i]) + ", ";
                }
                // EnhancedDisabilityPremiumAward
                //line += summary.get(sAllTotalCount_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                //line += summary.get(sAllPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                if ((i == 5 || i == 7)) {
                    line += summary.get(sCTBTotalCount_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                    line += summary.get(sAllPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                    line += summary.get(sCTBPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sHBTotalCount_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                    line += summary.get(sAllPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                    line += summary.get(sHBPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                }
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    private String getHeaderSingleTimeGeneric(boolean underoccupancy) {
        String result;
        result = "year-month, ";
        if (underoccupancy) {
            result += "CouncilFilename, "
                    + "CouncilCount, "
                    + "CouncilLinkedRecordsCount,"
                    + "RSLFilename, "
                    //+ sRSLCount + ", "
                    + "RSLCount, "
                    + "RSLLinkedRecordsCount, "
                    + "AllCount, "
                    + "AllLinkedRecordsCount, ";
        } else {
            result += sAllCount1 + ", ";
            result += sHBCount1 + ", ";
            result += sCTBCount1 + ", ";
        }
        result += "Month Year, ";
        return result;
    }

    private String getLineSingleTimeGeneric(
            String key,
            HashMap<String, String> summary,
            boolean doUnderOccupancy) {
        String result;
        result = key + ", ";
        if (doUnderOccupancy) {
            result += summary.get(sCouncilFilename1) + ", ";
            result += summary.get(sCouncilCount1) + ", ";
            result += summary.get(sCouncilLinkedRecordCount1) + ", ";
            result += summary.get(sRSLFilename1) + ", ";
            result += summary.get(sRSLCount1) + ", ";
            result += summary.get(sRSLLinkedRecordCount1) + ", ";
            result += summary.get(sAllUOCount1) + ", ";
//                    (Integer.valueOf(summary.get("CouncilCount"))
//                    + Integer.valueOf(summary.get("RSLCount")))) + ", ";
            result += summary.get(sAllUOLinkedRecordCount1) + ", ";
        } else {
            result += summary.get(sAllCount1) + ", ";
            result += summary.get(sHBCount1) + ", ";
            result += summary.get(sCTBCount1) + ", ";
        }
        String[] split;
        split = key.split("-");
        result += Generic_Time.getMonth3Letters(split[1]);
        result += " " + split[0] + ", ";
        return result;
    }
}
