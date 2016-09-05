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
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.generic.data.Generic_UKPostcode_Handler;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.math.Generic_BigDecimal;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Time;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_intID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_PersonID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Collection;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_CollectionHandler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_D_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.ID_PostcodeID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.ID_TenancyType;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.ID_TenancyType_PostcodeID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 * A class for summarising SHBE data.
 * @author geoagdt
 */
public class Summary extends DW_Object {

    // Generic vars
    public DW_SHBE_CollectionHandler collectionHandler;
    protected DW_SHBE_Handler tDW_SHBE_Handler;
    protected DW_Strings tDW_Strings;
    protected DW_Postcode_Handler tDW_Postcode_Handler;
    protected static final int decimalPlacePrecisionForAverage = 3;
    protected static final int decimalPlacePrecisionForPercentage = 3;
    // Special vars
    protected final String postcodeLS277NS = "LS27 7NS";
    protected final String s0 = "0";
    protected final String sSummaryTables = "SummaryTables";
    protected final String sCommaSpace = ", ";
    protected final String sSpace = " ";
    
    protected final String sSingleTimePSI = "SingleTimePSI";
    // Main vars
    // Counter Strings
    // HouseholdSize
    protected static final String sAllTotalHouseholdSize = "AllTotalHouseholdSize";
    protected static final String sAllAverageHouseholdSize = "AllAverageHouseholdSize";
    protected static final String sHBTotalHouseholdSize = "HBTotalHouseholdSize";
    protected static final String sHBAverageHouseholdSize = "HBAverageHouseholdSize";
    protected static final String sCTBTotalHouseholdSize = "CTBTotalHouseholdSize";
    protected static final String sCTBAverageHouseholdSize = "CTBAverageHouseholdSize";
    protected static String[] sAllTotalHouseholdSizeByTT;
    protected static String[] sAllAverageHouseholdSizeByTT;
    // PSI
    protected static String[] sAllTotalCount_PSI;
    protected static String[] sHBTotalCount_PSI;
    protected static String[] sCTBTotalCount_PSI;
    protected static String[] sAllPercentageOfAll_PSI;
    protected static String[] sHBPercentageOfHB_PSI;
    protected static String[] sCTBPercentageOfCTB_PSI;
    // PSIByTT
    protected static String[][] sTotalCount_PSIByTT;
    protected static String[][] sPercentageOfAll_PSIByTT;
    protected static String[][] sPercentageOfTT_PSIByTT;
    protected static String[][] sPercentageOfHB_PSIByTT;
    protected static String[][] sPercentageOfCTB_PSIByTT;
    // DisabilityPremiumAwardByTT
    protected static String[] sTotalCount_DisabilityPremiumAwardByTT;
    protected static String[] sPercentageOfAll_DisabilityPremiumAwardByTT;
    protected static String[] sPercentageOfHB_DisabilityPremiumAwardByTT;
    protected static String[] sPercentageOfCTB_DisabilityPremiumAwardByTT;
    protected static String[] sPercentageOfTT_DisabilityPremiumAwardByTT;
    // SevereDisabilityPremiumAwardByTT
    protected static String[] sTotalCount_SevereDisabilityPremiumAwardByTT;
    protected static String[] sPercentageOfAll_SevereDisabilityPremiumAwardByTT;
    protected static String[] sPercentageOfHB_SevereDisabilityPremiumAwardByTT;
    protected static String[] sPercentageOfCTB_SevereDisabilityPremiumAwardByTT;
    protected static String[] sPercentageOfTT_SevereDisabilityPremiumAwardByTT;
    // DisabledChildPremiumAwardByTT
    protected static String[] sTotalCount_DisabledChildPremiumAwardByTT;
    protected static String[] sPercentageOfAll_DisabledChildPremiumAwardByTT;
    protected static String[] sPercentageOfHB_DisabledChildPremiumAwardByTT;
    protected static String[] sPercentageOfCTB_DisabledChildPremiumAwardByTT;
    protected static String[] sPercentageOfTT_DisabledChildPremiumAwardByTT;
    // EnhancedDisabilityPremiumAwardByTT
    protected static String[] sTotalCount_EnhancedDisabilityPremiumAwardByTT;
    protected static String[] sPercentageOfAll_EnhancedDisabilityPremiumAwardByTT;
    protected static String[] sPercentageOfHB_EnhancedDisabilityPremiumAwardByTT;
    protected static String[] sPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT;
    protected static String[] sPercentageOfTT_EnhancedDisabilityPremiumAwardByTT;
    // DisabilityAwards
    protected static final String sTotalCount_DisabilityAward = "TotalCount_DisabilityAward";
    protected static final String sPercentageOfAll_DisabilityAward = "PercentageOfAll_DisabilityAward";
    // DisabilityPremiumAwards
    protected static final String sTotalCount_DisabilityPremiumAward = "TotalCount_DisabilityPremiumAward";
    protected static final String sPercentageOfAll_DisabilityPremiumAward = "PercentageOfAll_DisabilityPremiumAward";
    // SevereDisabilityPremiumAwards
    protected static final String sTotalCount_SevereDisabilityPremiumAward = "TotalCount_SevereDisabilityPremiumAward";
    protected static final String sPercentageOfAll_SevereDisabilityPremiumAward = "PercentageOfAll_SevereDisabilityPremiumAward";
    // DisabledChildPremiumAwards
    protected static final String sTotalCount_DisabledChildPremiumAward = "TotalCount_DisabledChildPremiumAward";
    protected static final String sPercentageOfAll_DisabledChildPremiumAward = "PercentageOfAll_DisabledChildPremiumAward";
    // EnhancedDisabilityPremiumAwards
    protected static final String sTotalCount_EnhancedDisabilityPremiumAward = "TotalCount_EnhancedDisabilityPremiumAward";
    protected static final String sPercentageOfAll_EnhancedDisabilityPremiumAward = "PercentageOfAll_EnhancedDisabilityPremiumAward";
    // DisabilityPremiumAwardHBTTs
    protected static final String sTotalCount_DisabilityPremiumAwardHBTTs = "TotalCount_DisabilityPremiumAwardHBTTs";
    protected static final String sPercentageOfAll_DisabilityPremiumAwardHBTTs = "PercentageOfAll_DisabilityPremiumAwardHBTTs";
    protected static final String sPercentageOfHB_DisabilityPremiumAwardHBTTs = "PercentageOfHB_DisabilityPremiumAwardHBTTs";
    // SevereDisabilityPremiumAwardHBTTs
    protected static final String sTotalCount_SevereDisabilityPremiumAwardHBTTs = "TotalCount_SevereDisabilityPremiumAwardHBTTs";
    protected static final String sPercentageOfAll_SevereDisabilityPremiumAwardHBTTs = "PercentageOfAll_SevereDisabilityPremiumAwardHBTTs";
    protected static final String sPercentageOfHB_SevereDisabilityPremiumAwardHBTTs = "PercentageOfHB_SevereDisabilityPremiumAwardHBTTs";
    // DisabledChildPremiumAwardHBTTs
    protected static final String sTotalCount_DisabledChildPremiumAwardHBTTs = "TotalCount_DisabledChildPremiumAwardHBTTs";
    protected static final String sPercentageOfAll_DisabledChildPremiumAwardHBTTs = "PercentageOfAll_DisabledChildPremiumAwardHBTTs";
    protected static final String sPercentageOfHB_DisabledChildPremiumAwardHBTTs = "PercentageOfHB_DisabledChildPremiumAwardHBTTs";
    // EnhancedDisabilityPremiumAwardHBTTs
    protected static final String sTotalCount_EnhancedDisabilityPremiumAwardHBTTs = "TotalCount_EnhancedDisabilityPremiumAwardHBTTs";
    protected static final String sPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs = "PercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs";
    protected static final String sPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs = "PercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs";
    // DisabilityPremiumAwardCTBTTs
    protected static final String sTotalCount_DisabilityPremiumAwardCTBTTs = "TotalCount_DisabilityPremiumAwardCTBTTs";
    protected static final String sPercentageOfAll_DisabilityPremiumAwardCTBTTs = "PercentageOfAll_DisabilityPremiumAwardCTBTTs";
    protected static final String sPercentageOfCTB_DisabilityPremiumAwardCTBTTs = "PercentageOfCTB_DisabilityPremiumAwardCTBTTs";
    // SevereDisabilityPremiumAwardCTBTTs
    protected static final String sTotalCount_SevereDisabilityPremiumAwardCTBTTs = "TotalCount_SevereDisabilityPremiumAwardCTBTTs";
    protected static final String sPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs = "PercentageOfAll_SevereDisabilityPremiumAwardCTBTTs";
    protected static final String sPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs = "PercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs";
    // DisabledChildPremiumAwardCTBTTs
    protected static final String sTotalCount_DisabledChildPremiumAwardCTBTTs = "TotalCount_DisabledChildPremiumAwardCTBTTs";
    protected static final String sPercentageOfAll_DisabledChildPremiumAwardCTBTTs = "PercentageOfAll_DisabledChildPremiumAwardCTBTTs";
    protected static final String sPercentageOfCTB_DisabledChildPremiumAwardCTBTTs = "PercentageOfCTB_DisabledChildPremiumAwardCTBTTs";
    // EnhancedDisabilityPremiumAwardCTBTTs
    protected static final String sTotalCount_EnhancedDisabilityPremiumAwardCTBTTs = "TotalCount_EnhancedDisabilityPremiumAwardCTBTTs";
    protected static final String sPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs = "PercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs";
    protected static final String sPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs = "PercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs";
    // DisabilityPremiumAwardSocialTTs
    protected static final String sTotalCount_DisabilityPremiumAwardSocialTTs = "TotalCount_DisabilityPremiumAwardSocialTTs";
    protected static final String sPercentageOfAll_DisabilityPremiumAwardSocialTTs = "PercentageOfAll_DisabilityPremiumAwardSocialTTs";
    protected static final String sPercentageOfHB_DisabilityPremiumAwardSocialTTs = "PercentageOfHB_DisabilityPremiumAwardSocialTTs";
    protected static final String sPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs = "PercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs";
    // DisabilityPremiumAwardPrivateDeregulatedTTs
    protected static final String sTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs = "TotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs";
    // SevereDisabilityPremiumAwardSocialTTs
    protected static final String sTotalCount_SevereDisabilityPremiumAwardSocialTTs = "TotalCount_SevereDisabilityPremiumAwardSocialTTs";
    protected static final String sPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs = "PercentageOfAll_SevereDisabilityPremiumAwardSocialTTs";
    protected static final String sPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs = "PercentageOfHB_SevereDisabilityPremiumAwardSocialTTs";
    protected static final String sPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs = "PercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs";
    // SevereDisabilityPremiumAwardPrivateDeregulatedTTs
    protected static final String sTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "TotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    // DisabledChildPremiumAwardSocialTTs
    protected static final String sTotalCount_DisabledChildPremiumAwardSocialTTs = "TotalCount_DisabledChildPremiumAwardSocialTTs";
    protected static final String sPercentageOfAll_DisabledChildPremiumAwardSocialTTs = "PercentageOfAll_DisabledChildPremiumAwardSocialTTs";
    protected static final String sPercentageOfHB_DisabledChildPremiumAwardSocialTTs = "PercentageOfHB_DisabledChildPremiumAwardSocialTTs";
    protected static final String sPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs = "PercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs";
    // DisabledChildPremiumAwardPrivateDeregulatedTTs
    protected static final String sTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs = "TotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    protected static final String sPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs = "PercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    protected static final String sPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs = "PercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    protected static final String sPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs = "PercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    // EnhancedDisabilityPremiumAwardSocialTTs
    protected static final String sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs = "TotalCount_EnhancedDisabilityPremiumAwardSocialTTs";
    protected static final String sPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs = "PercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs";
    protected static final String sPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs = "PercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs";
    protected static final String sPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs = "PercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs";
    // EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs
    protected static final String sTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "TotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    // DisabilityAwardByTT
    protected static String[] sTotalCount_DisabilityAwardByTT;
    protected static String[] sPercentageOfAll_DisabilityAwardByTT;
    protected static String[] sPercentageOfHB_DisabilityAwardByTT;
    protected static String[] sPercentageOfCTB_DisabilityAwardByTT;
    protected static String[] sPercentageOfTT_DisabilityAwardByTT;
    // DisabilityAwardHBTTs
    protected static final String sTotalCount_DisabilityAwardHBTTs = "TotalCount_DisabilityAwardHBTTs";
    protected static final String sPercentageOfAll_DisabilityAwardHBTTs = "PercentageOfAll_DisabilityAwardHBTTs";
    protected static final String sPercentageOfHB_DisabilityAwardHBTTs = "PercentageOfHB_DisabilityAwardHBTTs";
    // DisabilityAwardCTBTTs
    protected static final String sTotalCount_DisabilityAwardCTBTTs = "TotalCount_DisabilityAwardCTBTTs";
    protected static final String sPercentageOfAll_DisabilityAwardCTBTTs = "PercentageOfAll_DisabilityAwardCTBTTs";
    protected static final String sPercentageOfCTB_DisabilityAwardCTBTTs = "PercentageOfCTB_DisabilityAwardCTBTTs";
    // DisabilityAwardSocialTTs
    protected static final String sTotalCount_DisabilityAwardSocialTTs = "TotalCount_DisabilityAwardSocialTTs";
    protected static final String sPercentageOfAll_DisabilityAwardSocialTTs = "PercentageOfAll_DisabilityAwardSocialTTs";
    protected static final String sPercentageOfHB_DisabilityAwardSocialTTs = "PercentageOfHB_DisabilityAwardSocialTTs";
    protected static final String sPercentageOfSocialTTs_DisabilityAwardSocialTTs = "PercentageOfSocialTTs_DisabilityAwardSocialTTs";
    // DisabilityAwardPrivateDeregulatedTTs
    protected static final String sTotalCount_DisabilityAwardPrivateDeregulatedTTs = "TotalCount_DisabilityAwardPrivateDeregulatedTTs";
    protected static final String sPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs = "PercentageOfAll_DisabilityAwardPrivateDeregulatedTTs";
    protected static final String sPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs = "PercentageOfHB_DisabilityAwardPrivateDeregulatedTTs";
    protected static final String sPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs = "PercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs";
    // HBEntitlement
    public static final String sTotalWeeklyHBEntitlement = "TotalWeeklyHBEntitlement";
    public static final String sTotalCount_WeeklyHBEntitlementNonZero = "TotalCount_WeeklyHBEntitlementNonZero";
    public static final String sTotalCount_WeeklyHBEntitlementZero = "TotalCount_WeeklyHBEntitlementZero";
    public static final String sAverageWeeklyHBEntitlement = "AverageWeeklyHBEntitlement";
    // CTBEntitlement
    public static final String sTotalWeeklyCTBEntitlement = "TotalWeeklyCTBEntitlement";
    public static final String sTotalCount_WeeklyCTBEntitlementNonZero = "TotalCount_WeeklyCTBEntitlementNonZero";
    public static final String sTotalCount_WeeklyCTBEntitlementZero = "TotalCount_WeeklyCTBEntitlementZero";
    public static final String sAverageWeeklyCTBEntitlement = "AverageWeeklyCTBEntitlement";
    // WeeklyEligibleRentAmount
    public static final String sAllTotalWeeklyEligibleRentAmount = "AllTotalWeeklyEligibleRentAmount";
    public static final String sAllTotalCount_WeeklyEligibleRentAmountNonZero = "AllTotalCount_WeeklyEligibleRentAmountNonZero";
    public static final String sAllTotalCount_WeeklyEligibleRentAmountZero = "AllTotalCount_WeeklyEligibleRentAmountZero";
    public static final String sAllAverageWeeklyEligibleRentAmount = "AllAverageWeeklyEligibleRentAmount";
    public static String[] sAllTotalWeeklyEligibleRentAmountTT;
    public static String[] sAllTotalCount_WeeklyEligibleRentAmountNonZeroTT;
    public static String[] sAllTotalCount_WeeklyEligibleRentAmountZeroTT;
    public static String[] sAllAverageWeeklyEligibleRentAmountTT;
    public static String[] sHBTotalWeeklyEligibleRentAmountTT;
    public static String[] sHBTotalCount_WeeklyEligibleRentAmountNonZeroTT;
    public static String[] sHBTotalCount_WeeklyEligibleRentAmountZeroTT;
    public static String[] sHBAverageWeeklyEligibleRentAmountTT;
    public static String[] sCTBTotalWeeklyEligibleRentAmountTT;
    public static String[] sCTBTotalCount_WeeklyEligibleRentAmountNonZeroTT;
    public static String[] sCTBTotalCount_WeeklyEligibleRentAmountZeroTT;
    public static String[] sCTBAverageWeeklyEligibleRentAmountTT;
    // WeeklyHBEntitlement
    public static final String sHBTotalWeeklyHBEntitlement = "HBTotalWeeklyHBEntitlement";
    public static final String sHBTotalCount_WeeklyHBEntitlementNonZero = "HBTotalCount_WeeklyHBEntitlementNonZero";
    public static final String sHBTotalCount_WeeklyHBEntitlementZero = "HBTotalCount_WeeklyHBEntitlementZero";
    public static final String sHBAverageWeeklyHBEntitlement = "HBAverageWeeklyHBEntitlement";
    public static final String sCTBTotalWeeklyHBEntitlement = "CTBTotalWeeklyHBEntitlement";
    public static final String sCTBTotalCount_WeeklyHBEntitlementNonZero = "CTBTotalCount_WeeklyHBEntitlementNonZero";
    public static final String sCTBTotalCount_WeeklyHBEntitlementZero = "CTBTotalCount_WeeklyHBEntitlementZero";
    public static final String sCTBAverageWeeklyHBEntitlement = "CTBAverageWeeklyHBEntitlement";
    // WeeklyCTBEntitlement
    public static final String sHBTotalWeeklyCTBEntitlement = "HBTotalWeeklyCTBEntitlement";
    public static final String sHBTotalCount_WeeklyCTBEntitlementNonZero = "HBTotalCount_WeeklyCTBEntitlementNonZero";
    public static final String sHBTotalCount_WeeklyCTBEntitlementZero = "HBTotalCount_WeeklyCTBEntitlementZero";
    public static final String sHBAverageWeeklyCTBEntitlement = "HBAverageWeeklyCTBEntitlement";
    public static final String sCTBTotalWeeklyCTBEntitlement = "CTBTotalWeeklyCTBEntitlement";
    public static final String sCTBTotalCount_WeeklyCTBEntitlementNonZero = "CTBTotalCount_WeeklyCTBEntitlementNonZero";
    public static final String sCTBTotalCount_WeeklyCTBEntitlementZero = "CTBTotalCount_WeeklyCTBEntitlementZero";
    public static final String sCTBAverageWeeklyCTBEntitlement = "CTBAverageWeeklyCTBEntitlement";
    // WeeklyEligibleRentAmount
    public static final String sHBTotalWeeklyEligibleRentAmount = "HBTotalWeeklyEligibleRentAmount";
    public static final String sHBTotalCount_WeeklyEligibleRentAmountNonZero = "HBTotalCount_WeeklyEligibleRentAmountNonZero";
    public static final String sHBTotalCount_WeeklyEligibleRentAmountZero = "HBTotalCount_WeeklyEligibleRentAmountZero";
    public static final String sHBAverageWeeklyEligibleRentAmount = "HBAverageWeeklyEligibleRentAmount";
    public static final String sCTBTotalWeeklyEligibleRentAmount = "CTBTotalWeeklyEligibleRentAmount";
    public static final String sCTBTotalCount_WeeklyEligibleRentAmountNonZero = "CTBTotalCount_WeeklyEligibleRentAmountNonZero";
    public static final String sCTBTotalCount_WeeklyEligibleRentAmountZero = "CTBTotalCount_WeeklyEligibleRentAmountZero";
    public static final String sCTBAverageWeeklyEligibleRentAmount = "CTBAverageWeeklyEligibleRentAmount";
    // WeeklyEligibleCouncilTaxAmount
    protected static final String sAllTotalWeeklyEligibleCouncilTaxAmount = "AllTotalWeeklyEligibleCouncilTaxAmount";
    protected static final String sAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = "AllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero";
    protected static final String sAllTotalCount_WeeklyEligibleCouncilTaxAmountZero = "AllTotalCount_WeeklyEligibleCouncilTaxAmountZero";
    protected static final String sAllAverageWeeklyEligibleCouncilTaxAmount = "AllAverageWeeklyEligibleCouncilTaxAmount";
    protected static final String sHBTotalWeeklyEligibleCouncilTaxAmount = "HBTotalCount_WeeklyEligibleCouncilTaxAmount";
    protected static final String sHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = "HBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero";
    protected static final String sHBTotalCount_WeeklyEligibleCouncilTaxAmountZero = "HBTotalCount_WeeklyEligibleCouncilTaxAmountZero";
    protected static final String sHBAverageWeeklyEligibleCouncilTaxAmount = "HBAverageWeeklyEligibleCouncilTaxAmount";
    protected static final String sCTBTotalWeeklyEligibleCouncilTaxAmount = "CTBTotalWeeklyEligibleCouncilTaxAmount";
    protected static final String sCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = "CTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero";
    protected static final String sCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero = "CTBTotalCount_WeeklyEligibleCouncilTaxAmountZero";
    protected static final String sCTBAverageWeeklyEligibleCouncilTaxAmount = "CTBAverageWeeklyEligibleCouncilTaxAmount";
    // ContractualRentAmount
    protected static final String sAllTotalContractualRentAmount = "AllTotalContractualRentAmount";
    protected static final String sAllTotalCountContractualRentAmountNonZeroCount = "AllTotalCount_ContractualRentAmountNonZero";
    protected static final String sAllTotalCountContractualRentAmountZeroCount = "AllTotalCount_ContractualRentAmountZero";
    protected static final String sAllAverageContractualRentAmount = "AllAverageContractualRentAmount";
    protected static final String sHBTotalContractualRentAmount = "HBTotalContractualRentAmount";
    protected static final String sHBTotalCountContractualRentAmountNonZeroCount = "HBTotalCount_ContractualRentAmountNonZero";
    protected static final String sHBTotalCountContractualRentAmountZeroCount = "HBTotalCount_ContractualRentAmountZero";
    protected static final String sHBAverageContractualRentAmount = "HBAverageContractualRentAmount";
    protected static final String sCTBTotalContractualRentAmount = "CTBTotalContractualRentAmount";
    protected static final String sCTBTotalCountContractualRentAmountNonZeroCount = "CTBTotalCount_ContractualRentAmountNonZero";
    protected static final String sCTBTotalCountContractualRentAmountZeroCount = "CTBTotalCount_ContractualRentAmountZero";
    protected static final String sCTBAverageContractualRentAmount = "CTBAverageContractualRentAmount";
    // WeeklyAdditionalDiscretionaryPayment
    protected static final String sAllTotalWeeklyAdditionalDiscretionaryPayment = "AllTotalWeeklyAdditionalDiscretionaryPayment";
    protected static final String sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero = "AllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero";
    protected static final String sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero = "AllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero";
    protected static final String sAllAverageWeeklyAdditionalDiscretionaryPayment = "AllAverageWeeklyAdditionalDiscretionaryPayment";
    protected static final String sHBTotalWeeklyAdditionalDiscretionaryPayment = "HBTotalWeeklyAdditionalDiscretionaryPayment";
    protected static final String sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero = "HBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero";
    protected static final String sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero = "HBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero";
    protected static final String sHBAverageWeeklyAdditionalDiscretionaryPayment = "HBAverageWeeklyAdditionalDiscretionaryPayment";
    protected static final String sCTBTotalWeeklyAdditionalDiscretionaryPayment = "CTBTotalWeeklyAdditionalDiscretionaryPayment";
    protected static final String sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero = "CTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero";
    protected static final String sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero = "CTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero";
    protected static final String sCTBAverageWeeklyAdditionalDiscretionaryPayment = "CTBAverageWeeklyAdditionalDiscretionaryPayment";
    // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
    protected static final String sAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected static final String sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "AllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    protected static final String sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "AllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    protected static final String sAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "AllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected static final String sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected static final String sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "HBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    protected static final String sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "HBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    protected static final String sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "HBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected static final String sCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected static final String sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "CTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    protected static final String sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "CTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    protected static final String sCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "CTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    // Employment
    protected static final String sAllTotalCount_ClaimantsEmployed = "AllTotalCount_ClaimantsEmployed";
    protected static final String sAllPercentage_ClaimantsEmployed = "AllPercentage_ClaimantsEmployed";
    protected static final String sAllTotalCount_ClaimantsSelfEmployed = "AllTotalCount_ClaimantsSelfEmployed";
    protected static final String sAllPercentage_ClaimantsSelfEmployed = "AllPercentage_ClaimantsSelfEmployed";
    protected static final String sAllTotalCount_ClaimantsStudents = "AllTotalCount_ClaimantsStudents";
    protected static final String sAllPercentage_ClaimantsStudents = "AllPercentage_ClaimantsStudents";
    protected static final String sAllTotalCount_LHACases = "AllTotalCount_LHACases";
    protected static final String sAllPercentageOfAll_LHACases = "AllPercentageOfHB_LHACases";
    protected static final String sHBTotalCount_ClaimantsEmployed = "HBTotalCount_ClaimantsEmployed";
    protected static final String sHBPercentageOfHB_ClaimantsEmployed = "HBPercentageOfHB_ClaimantsEmployed";
    protected static final String sHBTotalCountClaimantsSelfEmployed = "HBTotalCount_ClaimantsSelfEmployed";
    protected static final String sHBPercentageOfHB_ClaimantsSelfEmployed = "HBPercentageOfHB_ClaimantsSelfEmployed";
    protected static final String sHBTotalCountClaimantsStudents = "HBTotalCount_ClaimantsStudents";
    protected static final String sHBPercentageOfHB_ClaimantsStudents = "HBPercentageOfHB_ClaimantsStudents";
    protected static final String sHBTotalCount_LHACases = "HBTotalCount_LHACases";
    protected static final String sHBPercentageOfHB_LHACases = "HBPercentageOfHB_LHACases";
    protected static final String sCTBTotalCount_ClaimantsEmployed = "CTBTotalCount_ClaimantsEmployed";
    protected static final String sCTBPercentageOfCTB_ClaimantsEmployed = "CTBPercentageOfCTB_ClaimantsEmployed";
    protected static final String sCTBTotalCountClaimantsSelfEmployed = "CTBTotalCountClaimantsSelfEmployed";
    protected static final String sCTBPercentageOfCTB_ClaimantsSelfEmployed = "CTBPercentageOfCTB_ClaimantsSelfEmployed";
    protected static final String sCTBTotalCountClaimantsStudents = "CTBTotalCountClaimantsStudents";
    protected static final String sCTBPercentageOfCTB_ClaimantsStudents = "CTBPercentageOfCTB_ClaimantsStudents";
    protected static final String sCTBTotalCount_LHACases = "CTBTotalCountLHACases";
    protected static final String sCTBPercentageOfCTB_LHACases = "CTBPercentageOfCTB_LHACases";
    // Counts
    protected static final String sAllCount00 = "AllCount00";
    protected static final String sAllCount0 = "AllCount0";
    protected static final String sHBCount0 = "HBCount0";
    protected static final String sCTBCount0 = "CTBOnlyCount0";
    protected static final String sAllCount1 = "AllCount1";
    protected static final String sHBCount1 = "HBCount1";
    protected static final String sCTBCount1 = "CTBOnlyCount1";
    protected static final String sTotalCount_SocialTTsClaimant = "TotalCount_SocialTTsClaimant";
    protected static final String sPercentageOfAll_SocialTTsClaimant = "PercentageOfAll_SocialTTsClaimant";
    protected static final String sPercentageOfHB_SocialTTsClaimant = "PercentageOfHB_SocialTTsClaimant";
    protected static final String sTotalCount_PrivateDeregulatedTTsClaimant = "TotalCount_PrivateDeregulatedTTsClaimant";
    protected static final String sPercentageOfAll_PrivateDeregulatedTTsClaimant = "PercentageOfAll_PrivateDeregulatedTTsClaimant";
    protected static final String sPercentageOfHB_PrivateDeregulatedTTsClaimant = "PercentageOfHB_PrivateDeregulatedTTsClaimant";
    protected static String[] sAllTotalCount_EthnicGroupClaimant;
    protected static String[] sAllTotalCount_EthnicGroupSocialTTClaimant;
    protected static String[] sAllTotalCount_EthnicGroupPrivateDeregulatedTTClaimant;
    protected static String[] sAllPercentageOfAll_EthnicGroupClaimant;
    protected static String[] sAllPercentageOfSocialTT_EthnicGroupSocialTTClaimant;
    protected static String[] sAllPercentageOfPrivateDeregulatedTT_EthnicGroupPrivateDeregulatedTTClaimant;
    protected static String[][] sAllTotalCount_EthnicGroupClaimantByTT;
    protected static String[][] sAllPercentageOfEthnicGroup_EthnicGroupClaimantByTT;
    protected static String[][] sAllPercentageOfTT_EthnicGroupClaimantByTT;

    protected static String sAllTotalCount_PostcodeValidFormat;
    protected static String sAllTotalCount_PostcodeValid;
    // Income
    // All
    public static final String sAllTotalIncome = "AllTotalIncome";
    public static final String sAllTotalCount_IncomeNonZero = "AllTotalCount_IncomeNonZero";
    public static final String sAllTotalCount_IncomeZero = "AllTotalCount_IncomeZero";
    public static final String sAllAverageIncome = "AllAverageIncome";
    public static String[] sAllTotalIncomeTT;
    public static String[] sAllTotalCount_IncomeNonZeroTT;
    public static String[] sAllTotalCount_IncomeZeroTT;
    public static String[] sAllAverageIncomeTT;
    //HB
    public static final String sHBTotalIncome = "HBTotalIncome";
    public static final String sHBTotalCount_IncomeNonZero = "HBTotalCount_IncomeNonZero";
    public static final String sHBTotalCount_IncomeZero = "HBTotalCount_IncomeZero";
    public static final String sHBAverageIncome = "HBAverageIncome";
    public static String[] sHBTotalIncomeTT;
    public static String[] sHBTotalCount_IncomeNonZeroTT;
    public static String[] sHBTotalCount_IncomeZeroTT;
    public static String[] sHBAverageIncomeTT;
    // CTB
    public static final String sCTBTotalIncome = "CTBTotalIncome";
    public static final String sCTBTotalCount_IncomeNonZero = "CTBTotalCount_IncomeNonZero";
    public static final String sCTBTotalCount_IncomeZero = "CTBTotalCount_IncomeZero";
    public static final String sCTBAverageIncome = "CTBAverageIncome";
    public static String[] sCTBTotalIncomeTT;
    public static String[] sCTBTotalCount_IncomeNonZeroTT;
    public static String[] sCTBTotalCount_IncomeZeroTT;
    public static String[] sCTBAverageIncomeTT;
    // Demographics
    // HB
    protected static String[] sHBTotalCount_EthnicGroupClaimant;
    protected static String[] sHBPercentageOfHB_EthnicGroupClaimant;
    // CTB
    protected static String[] sCTBTotalCount_EthnicGroupClaimant;
    protected static String[] sCTBPercentageOfCTB_EthnicGroupClaimant;
    // Files
    protected static final String sSHBEFilename00 = "SHBEFilename00";
    protected static final String sSHBEFilename0 = "SHBEFilename0";
    protected static final String sSHBEFilename1 = "SHBEFilename1";
    // Key Counts
    protected static String[] sTotalCount_ClaimantTT;
    protected static String[] sPercentageOfAll_ClaimantTT;
    protected static String[] sPercentageOfHB_ClaimantTT;
    protected static String[] sPercentageOfCTB_ClaimantTT;
    // Postcode
    protected static String sHBTotalCount_PostcodeValidFormat;
    protected static String sHBTotalCount_PostcodeValid;
    protected static String sCTBTotalCount_PostcodeValidFormat;
    protected static String sCTBTotalCount_PostcodeValid;

    // Compare 2 Times
    // All TT
    protected static final String sAllTotalCount_TTChangeClaimant = "AllTotalCount_TTChangeClaimant";
    protected static final String sAllPercentageOfAll_TTChangeClaimant = "AllPercentageOfAll_TTChangeClaimant";
    protected static final String sTotalCount_HBTTsToCTBTTs = "TotalCount_HBTTsToCTBTTs";
    protected static final String sPercentageOfHB_HBTTsToCTBTTs = "PercentageOfHB_HBTTsToCTBTTs";
    protected static final String sTotalCount_CTBTTsToHBTTs = "TotalCount_CTBTTsToHBTTs";
    protected static final String sPercentageOfCTB_CTBTTsToHBTTs = "PercentageOfCTB_CTBTTsToHBTTs";
    // HB TT
    protected static final String sHBTotalCount_TTChangeClaimant = "HBTotalCount_TTChangeClaimant";
    protected static final String sHBPercentageOfHB_TTChangeClaimant = "HBPercentageOfHB_TTChangeClaimant";
    protected static final String sTotalCount_Minus999TTToSocialTTs = "TotalCount_Minus999TTToSocialTTs";
    protected static final String sTotalCount_Minus999TTToPrivateDeregulatedTTs = "TotalCount_Minus999TTToPrivateDeregulatedTTs";
    protected static final String sTotalCount_HBTTsToHBTTs = "TotalCount_HBTTsToHBTTs";
    protected static final String sPercentageOfHB_HBTTsToHBTTs = "PercentageOfHB_HBTTsToHBTTs";
    protected static final String sTotalCount_HBTTsToMinus999TT = "TotalCount_HBTTsToMinus999TT";
    protected static final String sPercentageOfHB_HBTTsToMinus999TT = "PercentageOfHB_HBTTsToMinus999TT";
    protected static final String sTotalCount_SocialTTsToPrivateDeregulatedTTs = "TotalCount_SocialTTsToPrivateDeregulatedTTs";
    protected static final String sPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs = "PercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs";
    protected static final String sTotalCount_SocialTTsToMinus999TT = "TotalCount_SocialTTsToMinus999TT";
    protected static final String sPercentageOfSocialTTs_SocialTTsToMinus999TT = "PercentageOfSocialTTs_SocialTTsToMinus999TT";
    protected static final String sTotalCount_PrivateDeregulatedTTsToSocialTTs = "TotalCount_PrivateDeregulatedTTsToSocialTTs";
    protected static final String sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs = "PercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs";
    protected static final String sTotalCount_PrivateDeregulatedTTsToMinus999TT = "TotalCount_PrivateDeregulatedTTsToMinus999TT";
    protected static final String sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT = "PercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT";
    protected static final String sTotalCount_TT1ToPrivateDeregulatedTTs = "TotalCount_TT1ToPrivateDeregulatedTTs";
    protected static final String sPercentageOfTT1_TT1ToPrivateDeregulatedTTs = "PercentageOfTT1_TT1ToPrivateDeregulatedTTs";
    protected static final String sTotalCount_TT4ToPrivateDeregulatedTTs = "TotalCount_TT4ToPrivateDeregulatedTTs";
    protected static final String sPercentageOfTT4_TT4ToPrivateDeregulatedTTs = "PercentageOfTT4_TT4ToPrivateDeregulatedTTs";
    protected static final String sTotalCount_PrivateDeregulatedTTsToTT1 = "TotalCount_PrivateDeregulatedTTsToTT1";
    protected static final String sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1 = "PercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1";
    protected static final String sTotalCount_PrivateDeregulatedTTsToTT4 = "TotalCount_PrivateDeregulatedTTsToTT4";
    protected static final String sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4 = "PercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4";
    protected static final String sTotalCount_TT1ToTT4 = "TotalCount_TT1ToTT4";
    protected static final String sPercentageOfTT1_TT1ToTT4 = "PercentageOfTT1_TT1ToTT4";
    protected static final String sTotalCount_TT4ToTT1 = "TotalCount_TT4ToTT1";
    protected static final String sPercentageOfTT4_TT4ToTT1 = "PercentageOfTT4_TT4ToTT1";
    protected static final String sTotalCount_PostcodeChangeWithinSocialTTs = "TotalCount_PostcodeChangeWithinSocialTTs";
    protected static final String sPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs = "PercentageOfSocialTTs_PostcodeChangeWithinSocialTTs";
    protected static final String sTotalCount_PostcodeChangeWithinTT1 = "TotalCount_PostcodeChangeWithinTT1";
    protected static final String sPercentageOfTT1_PostcodeChangeWithinTT1 = "PercentageOfTT1_PostcodeChangeWithinTT1";
    protected static final String sTotalCount_PostcodeChangeWithinTT4 = "TotalCount_PostcodeChangeWithinTT4";
    protected static final String sPercentageOfTT4_PostcodeChangeWithinTT4 = "PercentageOfTT4_PostcodeChangeWithinTT4";
    protected static final String sTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs = "TotalCount_PostcodeChangeWithinPrivateDeregulatedTTs";
    protected static final String sPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs = "PercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs";
    // CTB TT
    protected static final String sCTBTotalCount_TTChangeClaimant = "CTBTotalCount_TTChangeClaimant";
    protected static final String sCTBPercentageOfCTB_TTChangeClaimant = "CTBPercentageOfCTB_TTChangeClaimant";
    protected static final String sTotalCount_Minus999TTToCTBTTs = "TotalCount_Minus999TTToCTBTTs";
    protected static final String sTotalCount_SocialTTsToCTBTTs = "TotalCount_SocialTTsToCTBTTs";
    protected static final String sPercentageOfSocialTTs_SocialTTsToCTBTTs = "PercentageOfSocialTTs_SocialTTsToCTBTTs";
    protected static final String sTotalCount_TT1ToCTBTTs = "TotalCount_TT1ToCTBTTs";
    protected static final String sPercentageOfTT1_TT1ToCTBTTs = "PercentageOfTT1_TT1ToCTBTTs";
    protected static final String sTotalCount_TT4ToCTBTTs = "TotalCount_TT4ToCTBTTs";
    protected static final String sPercentageOfTT4_TT4ToCTBTTs = "PercentageOfTT4_TT4ToCTBTTs";
    protected static final String sTotalCount_PrivateDeregulatedTTsToCTBTTs = "TotalCount_PrivateDeregulatedTTsToCTBTTs";
    protected static final String sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs = "PercentageOfPrivateDeregulated_PrivateDeregulatedTTsToCTBTTs";
    protected static final String sTotalCount_CTBTTsToSocialTTs = "TotalCount_CTBTTsToSocialTTs";
    protected static final String sTotalCount_CTBTTsToMinus999TT = "TotalCount_CTBTTsToMinus999TT";
    protected static final String sPercentageOfCTB_CTBTTsToSocialTTs = "PercentageOfCTB_CTBTTsToSocialTTs";
    protected static final String sPercentageOfCTB_CTBTTsToMinus999TT = "PercentageOfCTB_CTBTTsToMinus999TT";
    protected static final String sTotalCount_CTBTTsToTT1 = "TotalCount_CTBTTsToTT1";
    protected static final String sPercentageOfCTB_CTBTTsToTT1 = "PercentageOfCTB_CTBTTsToTT1";
    protected static final String sTotalCount_CTBTTsToTT4 = "TotalCount_CTBTTsToTT4";
    protected static final String sPercentageOfCTB_CTBTTsToTT4 = "PercentageOfCTB_CTBTTsToTT4";
    protected static final String sTotalCount_CTBTTsToPrivateDeregulatedTTs = "TotalCount_CTBTTsToPrivateDeregulatedTypes";
    protected static final String sPercentageOfCTB_CTBTTsToPrivateDeregulatedTTs = "PercentageOfCTB_CTBTTsToPrivateDeregulatedTypes";
    // All Postcode
    protected static final String sAllTotalCount_Postcode0ValidPostcode1Valid = "AllTotalCount_Postcode0ValidPostcode1Valid";
    protected static final String sAllPercentagePostcode0ValidPostcode1Valid = "AllPercentagePostcode0ValidPostcode1Valid";
    protected static final String sAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = "AllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged = "AllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChange = "AllTotalCount_Postcode0ValidPostcode1ValidPostcodeChange";
    protected static final String sAllPercentagePostcode0ValidPostcode1ValidPostcodeChange = "AllPercentagePostcode0ValidPostcode1ValidPostcodeChange";
    protected static final String sAllTotalCount_Postcode0ValidPostcode1NotValid = "AllTotalCount_Postcode0ValidPostcode1NotValid";
    protected static final String sAllPercentagePostcode0ValidPostcode1NotValid = "AllPercentagePostcode0ValidPostcode1NotValid";
    protected static final String sAllTotalCount_Postcode0NotValidPostcode1Valid = "AllTotalCount_Postcode0NotValidPostcode1Valid";
    protected static final String sAllPercentagePostcode0NotValidPostcode1Valid = "AllPercentagePostcode0NotValidPostcode1Valid";
    protected static final String sAllTotalCount_Postcode0NotValidPostcode1NotValid = "AllTotalCount_Postcode0NotValidPostcode1NotValid";
    protected static final String sAllPercentagePostcode0NotValidPostcode1NotValid = "AllPercentagePostcode0NotValidPostcode1NotValid";
    protected static final String sAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = "AllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged = "AllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = "AllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged";
    protected static final String sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged = "AllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged";
    // HB Postcode
    protected static final String sHBTotalCount_Postcode0ValidPostcode1Valid = "HBTotalCount_Postcode0ValidPostcode1Valid";
    protected static final String sHBPercentagePostcode0ValidPostcode1Valid = "HBPercentagePostcode0ValidPostcode1Valid";
    protected static final String sHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = "HBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged = "HBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChange = "HBTotalCount_Postcode0ValidPostcode1ValidPostcodeChange";
    protected static final String sHBPercentagePostcode0ValidPostcode1ValidPostcodeChange = "HBPercentagePostcode0ValidPostcode1ValidPostcodeChange";
    protected static final String sHBTotalCount_Postcode0ValidPostcode1NotValid = "HBTotalCount_Postcode0ValidPostcode1NotValid";
    protected static final String sHBPercentagePostcode0ValidPostcode1NotValid = "HBPercentagePostcode0ValidPostcode1NotValid";
    protected static final String sHBTotalCount_Postcode0NotValidPostcode1Valid = "HBTotalCount_Postcode0NotValidPostcode1Valid";
    protected static final String sHBPercentagePostcode0NotValidPostcode1Valid = "HBPercentagePostcode0NotValidPostcode1Valid";
    protected static final String sHBTotalCount_Postcode0NotValidPostcode1NotValid = "HBTotalCount_Postcode0NotValidPostcode1NotValid";
    protected static final String sHBPercentagePostcode0NotValidPostcode1NotValid = "HBPercentagePostcode0NotValidPostcode1NotValid";
    protected static final String sHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = "HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged = "HBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = "HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged";
    protected static final String sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged = "HBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged";
    // CTB Postcode
    protected static final String sCTBTotalCount_Postcode0ValidPostcode1Valid = "CTBTotalCount_Postcode0ValidPostcode1Valid";
    protected static final String sCTBPercentageOfCTB_Postcode0ValidPostcode1Valid = "CTBPercentagePostcode0ValidPostcode1Valid";
    protected static final String sCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = "CTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged = "CTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged = "CTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged";
    protected static final String sCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged = "CTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged";
    protected static final String sCTBTotalCount_Postcode0ValidPostcode1NotValid = "CTBTotalCount_Postcode0ValidPostcode1NotValid";
    protected static final String sCTBPercentagePostcode0ValidPostcode1NotValid = "CTBPercentagePostcode0ValidPostcode1NotValid";
    protected static final String sCTBTotalCount_Postcode0NotValidPostcode1Valid = "CTBTotalCount_Postcode0NotValidPostcode1Valid";
    protected static final String sCTBPercentagePostcode0NotValidPostcode1Valid = "CTBPercentagePostcode0NotValidPostcode1Valid";
    protected static final String sCTBTotalCount_Postcode0NotValidPostcode1NotValid = "CTBTotalCount_Postcode0NotValidPostcode1NotValid";
    protected static final String sCTBPercentagePostcode0NotValidPostcode1NotValid = "CTBPercentagePostcode0NotValidPostcode1NotValid";
    protected static final String sCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = "CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged = "CTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = "CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged";
    protected static final String sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged = "CTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged";

    // Compare 3 Times
    // All
    protected static String sSamePostcodeIII;
    protected static String sSamePostcodeIOI;
    protected static String sSamePostcodeOIO;
    protected static String sSameTenancyIII;
    protected static String sSameTenancyIOI;
    protected static String sSameTenancyOIO;
    protected static String sSameTenancyAndPostcodeIII;
    protected static String sSameTenancyAndPostcodeIOI;
    protected static String sSameTenancyAndPostcodeOIO;
    protected static String[] sSameTenancyIIITT;
    protected static String[] sSameTenancyIOITT;
    protected static String[] sSameTenancyOIOTT;
    protected static String[] sSameTenancyAndPostcodeIIITT;
    protected static String[] sSameTenancyAndPostcodeIOITT;
    protected static String[] sSameTenancyAndPostcodeOIOTT;

    // Counters
    // Single Time
    // All
    protected static double AllTotalWeeklyHBEntitlement;
    protected static int AllTotalWeeklyHBEntitlementNonZeroCount;
    protected static int AllTotalWeeklyHBEntitlementZeroCount;
    protected static double AllTotalWeeklyCTBEntitlement;
    protected static int AllTotalCount_WeeklyCTBEntitlementNonZero;
    protected static int AllTotalWeeklyCTBEntitlementZeroCount;
    protected static double AllTotalWeeklyEligibleRentAmount;
    protected static int AllTotalWeeklyEligibleRentAmountNonZeroCount;
    protected static int AllTotalWeeklyEligibleRentAmountZeroCount;
    protected static double AllTotalWeeklyEligibleCouncilTaxAmount;
    protected static int AllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
    protected static int AllTotalCount_WeeklyEligibleCouncilTaxAmountZero;
    protected static double AllTotalContractualRentAmount;
    protected static int AllTotalContractualRentAmountNonZeroCount;
    protected static int AllTotalContractualRentAmountZeroCount;
    protected static double AllTotalWeeklyAdditionalDiscretionaryPayment;
    protected static int AllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    protected static int AllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    protected static double AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    protected static int AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    protected static int AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;
    // HB
    protected static double HBTotalWeeklyHBEntitlement;
    protected static int HBTotalCount_WeeklyHBEntitlementNonZero;
    protected static int HBTotalCount_WeeklyHBEntitlementZero;
    protected static double HBTotalWeeklyCTBEntitlement;
    protected static int HBTotalCount_WeeklyCTBEntitlementNonZero;
    protected static int HBTotalWeeklyCTBEntitlementZeroCount;
    protected static double HBTotalWeeklyEligibleRentAmount;
    protected static int HBTotalWeeklyEligibleRentAmountNonZeroCount;
    protected static int HBTotalWeeklyEligibleRentAmountZeroCount;
    protected static double HBTotalWeeklyEligibleCouncilTaxAmount;
    protected static int HBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
    protected static int HBTotalCount_WeeklyEligibleCouncilTaxAmountZero;
    protected static double HBTotalContractualRentAmount;
    protected static int HBTotalContractualRentAmountNonZeroCount;
    protected static int HBTotalContractualRentAmountZeroCount;
    protected static double HBTotalWeeklyAdditionalDiscretionaryPayment;
    protected static int HBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    protected static int HBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    protected static double HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    protected static int HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    protected static int HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;
    // CTB
    protected static double CTBTotalWeeklyHBEntitlement;
    protected static int CTBTotalCount_WeeklyHBEntitlementNonZero;
    protected static int CTBTotalWeeklyHBEntitlementZeroCount;
    protected static double CTBTotalWeeklyCTBEntitlement;
    protected static int CTBTotalCount_WeeklyCTBEntitlementNonZero;
    protected static int CTBTotalWeeklyCTBEntitlementZeroCount;
    protected static double CTBTotalWeeklyEligibleRentAmount;
    protected static int CTBTotalWeeklyEligibleRentAmountNonZeroCount;
    protected static int CTBTotalWeeklyEligibleRentAmountZeroCount;
    protected static double CTBTotalWeeklyEligibleCouncilTaxAmount;
    protected static int CTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
    protected static int CTBTotalCount_WeeklyEligibleCouncilTaxAmountZero;
    protected static double CTBTotalContractualRentAmount;
    protected static int CTBTotalContractualRentAmountNonZeroCount;
    protected static int CTBTotalContractualRentAmountZeroCount;
    protected static double CTBTotalWeeklyAdditionalDiscretionaryPayment;
    protected static int CTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    protected static int CTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    protected static double CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    protected static int CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    protected static int CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;
    // Employment Education Training
    protected static int HBTotalCount_EmployedClaimants;
    protected static int HBTotalCount_SelfEmployedClaimants;
    protected static int HBTotalCount_StudentsClaimants;
    protected static int CTBTotalCount_EmployedClaimants;
    protected static int CTBTotalCount_SelfEmployedClaimants;
    protected static int CTBTotalCount_StudentsClaimants;
    // HLA
    protected static int HBTotalCount_LHACases;
    protected static int CTBTotalCount_LHACases;
    // Disability
    protected static int[] TotalCount_DisabilityPremiumAwardByTT;
    protected static int[] TotalCount_SevereDisabilityPremiumAwardByTT;
    protected static int[] TotalCount_DisabledChildPremiumAwardByTT;
    protected static int[] TotalCount_EnhancedDisabilityPremiumAwardByTT;
    protected static int[] TotalCount_DisabilityAwardByTT;
    // PSI
    protected static int[] AllTotalCount_PSI;
    protected static int[] HBTotalCount_PSI;
    protected static int[] CTBTotalCount_PSI;
    protected static int[][] AllTotalCount_PSIByTT;
    protected static int[][] HBTotalCount_PSIByTT;
    protected static int[][] CTBTotalCount_PSIByTT;
    // Total Count Claimant
    protected static int[] TotalCount_TTClaimant1;
    protected static int[] TotalCount_TTClaimant0;
    // Household Size
    protected static long AllTotalHouseholdSize;
    protected static long HBTotalHouseholdSize;
    protected static long CTBTotalHouseholdSize;
    protected static long[] AllTotalHouseholdSizeByTT;
    // All
    protected static int AllCount1;
    protected static Integer AllCount0;
    protected static int[][] AllTotalCount_EthnicGroupClaimantByTT;
    // HB
    protected static int HBCount1;
    protected static Integer HBCount0;
    protected static int[] HBEthnicGroupCount;
    protected static int HBTotalCount_PostcodeValidFormat;
    protected static int HBTotalCount_PostcodeValid;
    // CTB
    protected static int CTBCount1;
    protected static Integer CTBCount0;
    protected static int[] CTBEthnicGroupCount;
    protected static int CTBTotalCount_PostcodeValidFormat;
    protected static int CTBTotalCount_PostcodeValid;
    // Compare 2 Times
    // General
    protected static int TotalCount_HBTTsToCTBTTs;
    protected static int TotalCount_CTBTTsToHBTTs;
    // General HB related
    protected static int TotalCount_Minus999TTToSocialTTs;
    protected static int TotalCount_Minus999TTToPrivateDeregulatedTTs;
    protected static int TotalCount_HBTTsToHBTTs;
    protected static int TotalCount_HBTTsToMinus999TT;
    protected static int TotalCount_SocialTTsToPrivateDeregulatedTTs;
    protected static int TotalCount_SocialTTsToMinus999TT;
    protected static int TotalCount_PrivateDeregulatedTTsToSocialTTs;
    protected static int TotalCount_PrivateDeregulatedTTsToMinus999TT;
    protected static int TotalCount_TT1ToPrivateDeregulatedTTs;
    protected static int TotalCount_TT4ToPrivateDeregulatedTTs;
    protected static int TotalCount_PrivateDeregulatedTTsToTT1;
    protected static int TotalCount_PrivateDeregulatedTTsToTT4;
    protected static int TotalCount_PostcodeChangeWithinSocialTTs;
    protected static int TotalCount_PostcodeChangeWithinTT1;
    protected static int TotalCount_PostcodeChangeWithinTT4;
    protected static int TotalCount_PostcodeChangeWithinPrivateDeregulatedTTs;
    // General CTB related
    protected static int TotalCount_SocialTTsToCTBTTs;
    protected static int TotalCount_Minus999TTToCTBTTs;
    protected static int TotalCount_TT1ToCTBTTs;
    protected static int TotalCount_TT4ToCTBTTs;
    protected static int TotalCount_PrivateDeregulatedTTsToCTBTTs;
    protected static int TotalCount_CTBTTsToSocialTTs;
    protected static int TotalCount_CTBTTsToMinus999TT;
    protected static int TotalCount_CTBTTsToTT1;
    protected static int TotalCount_CTBTTsToTT4;
    protected static int TotalCount_CTBTTsToPrivateDeregulatedTTs;
    // TT1 TT4
    protected static int TotalCount_TT1ToTT4;
    protected static int TotalCount_TT4ToTT1;
    // All
    protected static int AllTotalCount_TTChangeClaimant;
    //protected static int AllTotalCount_TTChangeClaimantIgnoreMinus999;
    protected static int AllTotalCount_Postcode0ValidPostcode1Valid;
    protected static int AllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged;
    protected static int AllTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged;
    protected static int AllTotalCount_Postcode0ValidPostcode1NotValid;
    protected static int AllTotalCount_Postcode0NotValidPostcode1Valid;
    protected static int AllTotalCount_Postcode0NotValidPostcode1NotValid;
    protected static int AllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged;
    protected static int AllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged;
    // HB
    protected static int HBTotalCount_TTChangeClaimant;
    //protected static int HBTotalCount_TTChangeClaimantIgnoreMinus999;
    protected static int HBTotalCount_Postcode0ValidPostcode1Valid;
    protected static int HBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged;
    protected static int HBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged;
    protected static int HBTotalCount_Postcode0ValidPostcode1NotValid;
    protected static int HBTotalCount_Postcode0NotValidPostcode1Valid;
    protected static int HBTotalCount_Postcode0NotValidPostcode1NotValid;
    protected static int HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged;
    protected static int HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged;
    // CTB
    protected static int CTBTotalCount_TTChangeClaimant;
    //protected static int CTBTotalCount_TTChangeClaimantIgnoreMinus999;
    protected static int CTBTotalCount_Postcode0ValidPostcode1Valid;
    protected static int CTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged;
    protected static int CTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged;
    protected static int CTBTotalCount_Postcode0ValidPostcode1NotValid;
    protected static int CTBTotalCount_Postcode0NotValidPostcode1Valid;
    protected static int CTBTotalCount_Postcode0NotValidPostcode1NotValid;
    protected static int CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged;
    protected static int CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged;
    // Compare 3 Times
    protected static int SamePostcodeIII;
    protected static int SamePostcodeIOI;
    protected static int SamePostcodeOIO;
    protected static int SameTenancyIII;
    protected static int SameTenancyIOI;
    protected static int SameTenancyOIO;
    protected static int SameTenancyAndPostcodeIII;
    protected static int SameTenancyAndPostcodeIOI;
    protected static int SameTenancyAndPostcodeOIO;
    protected static int[] SameTenancyIIITT;
    protected static int[] SameTenancyIOITT;
    protected static int[] SameTenancyOIOTT;
    protected static int[] SameTenancyAndPostcodeIIITT;
    protected static int[] SameTenancyAndPostcodeIOITT;
    protected static int[] SameTenancyAndPostcodeOIOTT;

    public Summary() {}
    
    public Summary(DW_Environment env) {
        this.env = env;
        this.collectionHandler = null;
        this.tDW_SHBE_Handler = env.getDW_SHBE_Handler();
        this.tDW_Strings = env.getDW_Strings();
        this.tDW_Postcode_Handler = env.getDW_Postcode_Handler();
    }

    public Summary(
            DW_Environment env,
            DW_SHBE_CollectionHandler collectionHandler,
            int nTT,
            int nEG,
            int nPSI,
            boolean handleOutOfMemoryError) {
        this(env);
        init(nTT, nEG, nPSI);
    }

    protected void init(int nTT, int nEG, int nPSI) {
        initSingleTimeStrings(nTT, nEG, nPSI);
        initCompare3TimesStrings(nTT, nEG);
        TotalCount_DisabilityPremiumAwardByTT = new int[nTT];
        TotalCount_SevereDisabilityPremiumAwardByTT = new int[nTT];
        TotalCount_DisabledChildPremiumAwardByTT = new int[nTT];
        TotalCount_EnhancedDisabilityPremiumAwardByTT = new int[nTT];
        TotalCount_DisabilityAwardByTT = new int[nTT];
        AllTotalHouseholdSizeByTT = new long[nTT];
        AllTotalCount_PSI = new int[nPSI];
        HBTotalCount_PSI = new int[nPSI];
        CTBTotalCount_PSI = new int[nPSI];
        AllTotalCount_PSIByTT = new int[nPSI][nTT];
        HBTotalCount_PSIByTT = new int[nPSI][nTT];
        CTBTotalCount_PSIByTT = new int[nPSI][nTT];
        TotalCount_TTClaimant1 = new int[nTT];
        TotalCount_TTClaimant0 = new int[nTT];
        AllTotalCount_EthnicGroupClaimantByTT = new int[nEG][nTT];
        HBEthnicGroupCount = new int[nEG];
        CTBEthnicGroupCount = new int[nEG];
    }

    protected void initSingleTimeStrings(int nTT, int nEG, int nPSI) {
        sAllTotalIncomeTT = new String[nTT];
        sAllTotalCount_IncomeNonZeroTT = new String[nTT];
        sAllTotalCount_IncomeZeroTT = new String[nTT];
        sAllAverageIncomeTT = new String[nTT];
        sHBTotalIncomeTT = new String[nTT];
        sHBTotalCount_IncomeNonZeroTT = new String[nTT];
        sHBTotalCount_IncomeZeroTT = new String[nTT];
        sHBAverageIncomeTT = new String[nTT];
        sCTBTotalIncomeTT = new String[nTT];
        sCTBTotalCount_IncomeNonZeroTT = new String[nTT];
        sCTBTotalCount_IncomeZeroTT = new String[nTT];
        sCTBAverageIncomeTT = new String[nTT];
        sAllTotalWeeklyEligibleRentAmountTT = new String[nTT];
        sAllTotalCount_WeeklyEligibleRentAmountNonZeroTT = new String[nTT];
        sAllTotalCount_WeeklyEligibleRentAmountZeroTT = new String[nTT];
        sAllAverageWeeklyEligibleRentAmountTT = new String[nTT];
        sHBTotalWeeklyEligibleRentAmountTT = new String[nTT];
        sHBTotalCount_WeeklyEligibleRentAmountNonZeroTT = new String[nTT];
        sHBTotalCount_WeeklyEligibleRentAmountZeroTT = new String[nTT];
        sHBAverageWeeklyEligibleRentAmountTT = new String[nTT];
        sCTBTotalWeeklyEligibleRentAmountTT = new String[nTT];
        sCTBTotalCount_WeeklyEligibleRentAmountNonZeroTT = new String[nTT];
        sCTBTotalCount_WeeklyEligibleRentAmountZeroTT = new String[nTT];
        sCTBAverageWeeklyEligibleRentAmountTT = new String[nTT];
        sAllTotalHouseholdSizeByTT = new String[nTT];
        sAllAverageHouseholdSizeByTT = new String[nTT];
        sAllTotalCount_PSI = new String[nPSI];
        sHBTotalCount_PSI = new String[nPSI];
        sCTBTotalCount_PSI = new String[nPSI];
        sAllPercentageOfAll_PSI = new String[nPSI];
        sHBPercentageOfHB_PSI = new String[nPSI];
        sCTBPercentageOfCTB_PSI = new String[nPSI];
        sTotalCount_PSIByTT = new String[nPSI][nTT];
        sPercentageOfAll_PSIByTT = new String[nPSI][nTT];
        sPercentageOfTT_PSIByTT = new String[nPSI][nTT];
        sPercentageOfHB_PSIByTT = new String[nPSI][nTT];
        sPercentageOfCTB_PSIByTT = new String[nPSI][nTT];
        for (int i = 1; i < nPSI; i++) {
            sAllTotalCount_PSI[i] = "AllTotalCount_PSI" + i;
            sHBTotalCount_PSI[i] = "HBTotalCount_PSI" + i;
            sCTBTotalCount_PSI[i] = "CTBTotalCount_PSI" + i;
            sAllPercentageOfAll_PSI[i] = "AllPercentageOfAll_PSI" + i;
            sHBPercentageOfHB_PSI[i] = "HBPercentageOfHB_PSI" + i;
            sCTBPercentageOfCTB_PSI[i] = "CTBPercentageOfCTB_PSI" + i;
            for (int j = 1; j < nTT; j++) {
                sTotalCount_PSIByTT[i][j] = "TotalCount_PSI" + i + "TT" + j;
                sPercentageOfAll_PSIByTT[i][j] = "PercentageOfAll_PSI" + i + "TT" + j;
                if (j == 5 || j == 7) {
                    sPercentageOfCTB_PSIByTT[i][j] = "PercentageOfCTB_PSI" + i + "TT" + j;
                } else {
                    sPercentageOfHB_PSIByTT[i][j] = "PercentageOfHB_PSI" + i + "TT" + j;
                }
                sPercentageOfTT_PSIByTT[i][j] = "PercentageOfTT_PSI" + i + "TT" + j;
            }
        }
        // All
        sTotalCount_ClaimantTT = new String[nTT];
        sPercentageOfAll_ClaimantTT = new String[nTT];
        sPercentageOfHB_ClaimantTT = new String[nTT];
        sPercentageOfCTB_ClaimantTT = new String[nTT];
        // DisabilityAward
        sTotalCount_DisabilityAwardByTT = new String[nTT];
        sPercentageOfAll_DisabilityAwardByTT = new String[nTT];
        sPercentageOfHB_DisabilityAwardByTT = new String[nTT];
        sPercentageOfCTB_DisabilityAwardByTT = new String[nTT];
        sPercentageOfTT_DisabilityAwardByTT = new String[nTT];
        // DisabilityPremiumAward
        sTotalCount_DisabilityPremiumAwardByTT = new String[nTT];
        sPercentageOfAll_DisabilityPremiumAwardByTT = new String[nTT];
        sPercentageOfHB_DisabilityPremiumAwardByTT = new String[nTT];
        sPercentageOfCTB_DisabilityPremiumAwardByTT = new String[nTT];
        sPercentageOfTT_DisabilityPremiumAwardByTT = new String[nTT];
        // SevereDisabilityPremiumAward
        sTotalCount_SevereDisabilityPremiumAwardByTT = new String[nTT];
        sPercentageOfAll_SevereDisabilityPremiumAwardByTT = new String[nTT];
        sPercentageOfHB_SevereDisabilityPremiumAwardByTT = new String[nTT];
        sPercentageOfCTB_SevereDisabilityPremiumAwardByTT = new String[nTT];
        sPercentageOfTT_SevereDisabilityPremiumAwardByTT = new String[nTT];
        // DisabledChildPremiumAward
        sTotalCount_DisabledChildPremiumAwardByTT = new String[nTT];
        sPercentageOfAll_DisabledChildPremiumAwardByTT = new String[nTT];
        sPercentageOfHB_DisabledChildPremiumAwardByTT = new String[nTT];
        sPercentageOfCTB_DisabledChildPremiumAwardByTT = new String[nTT];
        sPercentageOfTT_DisabledChildPremiumAwardByTT = new String[nTT];
        // EnhancedDisabilityPremiumAward
        sTotalCount_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sPercentageOfAll_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sPercentageOfHB_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sPercentageOfTT_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            // HouseholdSize
            sAllTotalHouseholdSizeByTT[i] = "AllTotalHouseholdSizeByTT" + i;
            sAllAverageHouseholdSizeByTT[i] = "AllAverageHouseholdSizeByTT" + i;
            // Claimants
            sTotalCount_ClaimantTT[i] = "TotalCount_ClaimantTT" + i;
            sPercentageOfAll_ClaimantTT[i] = "PercentageOfAll_ClaimantTT" + i;
            sPercentageOfHB_ClaimantTT[i] = "PercentageOfHB_ClaimantTT" + i;
            sPercentageOfCTB_ClaimantTT[i] = "PercentageOfCTB_ClaimantTT" + i;
            // Income
            sAllTotalIncomeTT[i] = "AllTotalIncomeTT" + i;
            sAllTotalCount_IncomeNonZeroTT[i] = "AllTotalCount_IncomeNonZeroTT" + i;
            sAllTotalCount_IncomeZeroTT[i] = "AllTotalCount_IncomeZeroTT" + i;
            sAllAverageIncomeTT[i] = "AllAverageIncomeTT" + i;
            sHBTotalIncomeTT[i] = "HBTotalIncomeTT" + i;
            sHBTotalCount_IncomeNonZeroTT[i] = "HBTotalCount_IncomeNonZeroTT" + i;
            sHBTotalCount_IncomeZeroTT[i] = "HBTotalCount_IncomeZeroTT" + i;
            sHBAverageIncomeTT[i] = "HBAverageIncomeTT" + i;
            sCTBTotalIncomeTT[i] = "CTBTotalIncomeTT" + i;
            sCTBTotalCount_IncomeNonZeroTT[i] = "CTBTotalCount_IncomeNonZeroTT" + i;
            sCTBTotalCount_IncomeZeroTT[i] = "CTBTotalCount_IncomeZeroTT" + i;
            sCTBAverageIncomeTT[i] = "CTBAverageIncomeTT" + i;
            // WeeklyEligibleRentAmountTT
            sAllTotalWeeklyEligibleRentAmountTT[i] = "AllTotalWeeklyEligibleRentAmountTT" + i;
            sAllTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] = "AllTotalCount_WeeklyEligibleRentAmountNonZeroTT" + i;
            sAllTotalCount_WeeklyEligibleRentAmountZeroTT[i] = "AllTotalCount_WeeklyEligibleRentAmountZeroTT" + i;
            sAllAverageWeeklyEligibleRentAmountTT[i] = "AllAverageWeeklyEligibleRentAmountTT" + i;
            sHBTotalWeeklyEligibleRentAmountTT[i] = "HBTotalWeeklyEligibleRentAmountTT" + i;
            sHBTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] = "HBTotalCount_WeeklyEligibleRentAmountNonZeroTT" + i;
            sHBTotalCount_WeeklyEligibleRentAmountZeroTT[i] = "HBTotalCount_WeeklyEligibleRentAmountZeroTT" + i;
            sHBAverageWeeklyEligibleRentAmountTT[i] = "HBAverageWeeklyEligibleRentAmountTT" + i;
            sCTBTotalWeeklyEligibleRentAmountTT[i] = "CTBTotalWeeklyEligibleRentAmountTT" + i;
            sCTBTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] = "CTBTotalCount_WeeklyEligibleRentAmountNonZeroTT" + i;
            sCTBTotalCount_WeeklyEligibleRentAmountZeroTT[i] = "CTBTotalCount_WeeklyEligibleRentAmountZeroTT" + i;
            sCTBAverageWeeklyEligibleRentAmountTT[i] = "CTBAverageWeeklyEligibleRentAmountTT" + i;
            // DisabilityAwardByTT
            sTotalCount_DisabilityAwardByTT[i] = "TotalCount_DisabilityAwardByTT" + i;
            sPercentageOfAll_DisabilityAwardByTT[i] = "PercentageOfAll_DisabilityAwardByTT" + i;
            sPercentageOfHB_DisabilityAwardByTT[i] = "PercentageOfHB_DisabilityAwardByTT" + i;
            sPercentageOfCTB_DisabilityAwardByTT[i] = "PercentageOfCTB_DisabilityAwardByTT" + i;
            sPercentageOfTT_DisabilityAwardByTT[i] = "PercentageOfTT_DisabilityAwardByTT" + i;
            // DisabilityPremiumAwardByTT
            sTotalCount_DisabilityPremiumAwardByTT[i] = "TotalCount_DisabilityPremiumAwardByTT" + i;
            sPercentageOfAll_DisabilityPremiumAwardByTT[i] = "PercentageOfAll_DisabilityPremiumAwardByTT" + i;
            sPercentageOfHB_DisabilityPremiumAwardByTT[i] = "PercentageOfHB_DisabilityPremiumAwardByTT" + i;
            sPercentageOfCTB_DisabilityPremiumAwardByTT[i] = "PercentageOfCTB_DisabilityPremiumAwardByTT" + i;
            sPercentageOfTT_DisabilityPremiumAwardByTT[i] = "PercentageOfTT_DisabilityPremiumAwardByTT" + i;
            // SevereDisabilityPremiumAwardByTT
            sTotalCount_SevereDisabilityPremiumAwardByTT[i] = "TotalCount_SevereDisabilityPremiumAwardByTT" + i;
            sPercentageOfAll_SevereDisabilityPremiumAwardByTT[i] = "PercentageOfAll_SevereDisabilityPremiumAwardByTT" + i;
            sPercentageOfHB_SevereDisabilityPremiumAwardByTT[i] = "PercentageOfHB_SevereDisabilityPremiumAwardByTT" + i;
            sPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i] = "PercentageOfCTB_SevereDisabilityPremiumAwardByTT" + i;
            sPercentageOfTT_SevereDisabilityPremiumAwardByTT[i] = "PercentageOfTT_SevereDisabilityPremiumAwardByTT" + i;
            // DisabledChildPremiumAwardByTT
            sTotalCount_DisabledChildPremiumAwardByTT[i] = "TotalCount_DisabledChildPremiumAwardByTT" + i;
            sPercentageOfAll_DisabledChildPremiumAwardByTT[i] = "PercentageOfAll_DisabledChildPremiumAwardByTT" + i;
            sPercentageOfHB_DisabledChildPremiumAwardByTT[i] = "PercentageOfHB_DisabledChildPremiumAwardByTT" + i;
            sPercentageOfCTB_DisabledChildPremiumAwardByTT[i] = "PercentageOfCTB_DisabledChildPremiumAwardByTT" + i;
            sPercentageOfTT_DisabledChildPremiumAwardByTT[i] = "PercentageOfTT_DisabledChildPremiumAwardByTT" + i;
            // EnhancedDisabilityPremiumAwardByTT
            sTotalCount_EnhancedDisabilityPremiumAwardByTT[i] = "TotalCount_EnhancedDisabilityPremiumAwardByTT" + i;
            sPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i] = "PercentageOfAll_EnhancedDisabilityPremiumAwardByTT" + i;
            sPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i] = "PercentageOfHB_EnhancedDisabilityPremiumAwardByTT" + i;
            sPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i] = "PercentageOfCTB_EnhancedDisabilityPremiumAwardByTT" + i;
            sPercentageOfTT_EnhancedDisabilityPremiumAwardByTT[i] = "PercentageOfTT_EnhancedDisabilityPremiumAwardByTT" + i;
        }
        // Postcode
        sAllTotalCount_PostcodeValidFormat = "AllTotalCount_PostcodeValidFormat";
        sAllTotalCount_PostcodeValid = "AllTotalCount_PostcodeValid";
        sHBTotalCount_PostcodeValidFormat = "HBTotalCount_PostcodeValidFormat";
        sHBTotalCount_PostcodeValid = "HBTotalCount_PostcodeValid";
        sCTBTotalCount_PostcodeValidFormat = "CTBTotalCount_PostcodeValidFormat";
        sCTBTotalCount_PostcodeValid = "CTBTotalCount_PostcodeValid";
        // EthnicGroup
        sAllTotalCount_EthnicGroupClaimant = new String[nEG];
        sAllTotalCount_EthnicGroupSocialTTClaimant = new String[nEG];
        sAllTotalCount_EthnicGroupPrivateDeregulatedTTClaimant = new String[nEG];
        sAllPercentageOfAll_EthnicGroupClaimant = new String[nEG];
        sAllPercentageOfSocialTT_EthnicGroupSocialTTClaimant = new String[nEG];
        sAllPercentageOfPrivateDeregulatedTT_EthnicGroupPrivateDeregulatedTTClaimant = new String[nEG];
        sAllPercentageOfAll_EthnicGroupClaimant = new String[nEG];
        sAllTotalCount_EthnicGroupClaimantByTT = new String[nEG][nTT];
        sAllPercentageOfEthnicGroup_EthnicGroupClaimantByTT = new String[nEG][nTT];
        sAllPercentageOfTT_EthnicGroupClaimantByTT = new String[nEG][nTT];
        sHBTotalCount_EthnicGroupClaimant = new String[nEG];
        sHBPercentageOfHB_EthnicGroupClaimant = new String[nEG];
        sCTBTotalCount_EthnicGroupClaimant = new String[nEG];
        sCTBPercentageOfCTB_EthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            String EGN = tDW_SHBE_Handler.getEthnicityGroupName(i);
            for (int j = 1; j < nTT; j++) {
                sAllTotalCount_EthnicGroupClaimantByTT[i][j] = "AllTotalCount__ClaimantEthnicGroup_" + EGN + "__ClaimantTT" + j;
                sAllPercentageOfEthnicGroup_EthnicGroupClaimantByTT[i][j] = "AllPercentageOfEthnicGroup_" + EGN + "___ClaimantEthnicGroup_" + EGN + "__ClaimantTT" + j;
                sAllPercentageOfTT_EthnicGroupClaimantByTT[i][j] = "AllPercentageOfTT" + j + "__EthnicGroup _" + EGN + "__ClaimantTT" + j;
            }
            sAllTotalCount_EthnicGroupClaimant[i] = "AllTotalCount_EthnicGroup_" + EGN + "_Claimant";
            sAllTotalCount_EthnicGroupSocialTTClaimant[i] = "AllTotalCount_EthnicGroup_" + EGN + "_SocialTTClaimant";
            sAllTotalCount_EthnicGroupPrivateDeregulatedTTClaimant[i] = "AllTotalCount_EthnicGroup_" + EGN + "_PrivateDeregulatedTTClaimant";
            sAllPercentageOfAll_EthnicGroupClaimant[i] = "AllPercentageOfAll_EthnicGroup_" + EGN + "_Claimant";
            sAllPercentageOfSocialTT_EthnicGroupSocialTTClaimant[i] = "AllPercentageOfSocialTT_EthnicGroup_" + EGN + "_SocialTTClaimant";
            sAllPercentageOfPrivateDeregulatedTT_EthnicGroupPrivateDeregulatedTTClaimant[i] = "AllPercentageOfPrivateDeregulatedTT_EthnicGroup_" + EGN + "_PrivateDeregulatedTTClaimant";
            sHBTotalCount_EthnicGroupClaimant[i] = "HBTotalCount_EthnicGroup_" + EGN + "_Claimant";
            sHBPercentageOfHB_EthnicGroupClaimant[i] = "HBPercentageOfHB_EthnicGroup_" + EGN + "_Claimant";
            sCTBTotalCount_EthnicGroupClaimant[i] = "CTBTotalCount_ClaimantEthnicGroup_" + EGN + "_Claimant";
            sCTBPercentageOfCTB_EthnicGroupClaimant[i] = "CTBPercentageOfCTB_ClaimantEthnicGroup_" + EGN + "_Claimant";
        }
    }

    protected void initCompare3TimesStrings(int nTT, int nEG) {
        sSamePostcodeIII = "SamePostcodeIII";
        sSamePostcodeIOI = "SamePostcodeIOI";
        sSamePostcodeOIO = "SamePostcodeOIO";
        sSameTenancyIII = "SameTenancyIII";
        sSameTenancyIOI = "SameTenancyIOI";
        sSameTenancyOIO = "SameTenancyOIO";
        sSameTenancyAndPostcodeIII = "SameTenancyAndPostcodeIII";
        sSameTenancyAndPostcodeIOI = "SameTenancyAndPostcodeIOI";
        sSameTenancyAndPostcodeOIO = "SameTenancyAndPostcodeOIO";
        sSameTenancyIIITT = new String[nTT];
        sSameTenancyIOITT = new String[nTT];
        sSameTenancyOIOTT = new String[nTT];
        sSameTenancyAndPostcodeIIITT = new String[nTT];
        sSameTenancyAndPostcodeIOITT = new String[nTT];
        sSameTenancyAndPostcodeOIOTT = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            sSameTenancyIIITT[i] = "SameTenancyIIITT" + i;
            sSameTenancyIOITT[i] = "SameTenancyIOITT" + i;
            sSameTenancyOIOTT[i] = "SameTenancyOIOTT" + i;
            sSameTenancyAndPostcodeIIITT[i] = "SameTenancyAndPostcodeIIITT" + i;
            sSameTenancyAndPostcodeIOITT[i] = "SameTenancyAndPostcodeIOITT" + i;
            sSameTenancyAndPostcodeOIOTT[i] = "SameTenancyAndPostcodeOIOTT" + i;
        }
    }

    protected void initCounts(int nTT, int nEG, int nPSI) {
        initSingleTimeCounts(nTT, nEG, nPSI);
        initCompare2TimesCounts();
        initCompare3TimesCounts(nTT);
    }

    protected void initSingleTimeCounts(int nTT, int nEG, int nPSI) {
        // PSI
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
        AllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = 0;
        AllTotalCount_WeeklyEligibleCouncilTaxAmountZero = 0;
        AllTotalContractualRentAmount = 0.0d;
        AllTotalContractualRentAmountNonZeroCount = 0;
        AllTotalContractualRentAmountZeroCount = 0;
        AllTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        AllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        AllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;
        // HB
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
        HBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = 0;
        HBTotalCount_WeeklyEligibleCouncilTaxAmountZero = 0;
        HBTotalContractualRentAmount = 0.0d;
        HBTotalContractualRentAmountNonZeroCount = 0;
        HBTotalContractualRentAmountZeroCount = 0;
        HBTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        HBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        HBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;
        // CTB
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
        CTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = 0;
        CTBTotalCount_WeeklyEligibleCouncilTaxAmountZero = 0;
        CTBTotalContractualRentAmount = 0.0d;
        CTBTotalContractualRentAmountNonZeroCount = 0;
        CTBTotalContractualRentAmountZeroCount = 0;
        CTBTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        CTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        CTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;
        // Employment
        HBTotalCount_EmployedClaimants = 0;
        CTBTotalCount_EmployedClaimants = 0;
        HBTotalCount_SelfEmployedClaimants = 0;
        CTBTotalCount_SelfEmployedClaimants = 0;
        HBTotalCount_StudentsClaimants = 0;
        CTBTotalCount_StudentsClaimants = 0;
        // LHA
        HBTotalCount_LHACases = 0;
        CTBTotalCount_LHACases = 0;
        // Disability
        for (int i = 1; i < nTT; i++) {
            TotalCount_TTClaimant1[i] = 0;
            TotalCount_DisabilityPremiumAwardByTT[i] = 0;
            TotalCount_SevereDisabilityPremiumAwardByTT[i] = 0;
            TotalCount_DisabledChildPremiumAwardByTT[i] = 0;
            TotalCount_EnhancedDisabilityPremiumAwardByTT[i] = 0;
            TotalCount_DisabilityAwardByTT[i] = 0;
            AllTotalHouseholdSizeByTT[i] = 0;
        }
        //AllCount0 = AllCount1;
        AllCount1 = 0;
        //HBCount0 = HBCount1;
        HBCount1 = 0;
        //CTBCount0 = CTBCount1;
        CTBCount1 = 0;
        // Ethnicity
        for (int i = 1; i < nEG; i++) {
            for (int j = 1; j < nTT; j++) {
                AllTotalCount_EthnicGroupClaimantByTT[i][j] = 0;
            }
            HBEthnicGroupCount[i] = 0;
            CTBEthnicGroupCount[i] = 0;
        }
        HBTotalCount_PostcodeValidFormat = 0;
        HBTotalCount_PostcodeValid = 0;
        HBTotalCount_TTChangeClaimant = 0;
        //HBTotalCount_TTChangeClaimantIgnoreMinus999 = 0;
        CTBTotalCount_PostcodeValidFormat = 0;
        CTBTotalCount_PostcodeValid = 0;
        CTBTotalCount_TTChangeClaimant = 0;
        //CTBTotalCount_TTChangeClaimantIgnoreMinus999 = 0;
        // Household Size
        AllTotalHouseholdSize = 0L;
        HBTotalHouseholdSize = 0L;
        CTBTotalHouseholdSize = 0L;
    }

    protected void initCompare2TimesCounts() {
        // Compare 2 Times
        // General
        TotalCount_HBTTsToCTBTTs = 0;
        TotalCount_CTBTTsToHBTTs = 0;
        // General HB related
        TotalCount_Minus999TTToSocialTTs = 0;
        TotalCount_Minus999TTToPrivateDeregulatedTTs = 0;
        TotalCount_HBTTsToHBTTs = 0;
        TotalCount_HBTTsToMinus999TT = 0;
        TotalCount_SocialTTsToMinus999TT = 0;
        TotalCount_PrivateDeregulatedTTsToMinus999TT = 0;
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
        TotalCount_Minus999TTToCTBTTs = 0;
        TotalCount_SocialTTsToCTBTTs = 0;
        TotalCount_TT1ToCTBTTs = 0;
        TotalCount_TT4ToCTBTTs = 0;
        TotalCount_PrivateDeregulatedTTsToCTBTTs = 0;
        TotalCount_CTBTTsToSocialTTs = 0;
        TotalCount_CTBTTsToMinus999TT = 0;
        TotalCount_CTBTTsToTT1 = 0;
        TotalCount_CTBTTsToTT4 = 0;
        TotalCount_CTBTTsToPrivateDeregulatedTTs = 0;
        //
        TotalCount_TT1ToTT4 = 0;
        TotalCount_TT4ToTT1 = 0;
        // All
        AllTotalCount_TTChangeClaimant = 0;
        AllTotalCount_Postcode0ValidPostcode1Valid = 0;
        AllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        AllTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged = 0;
        AllTotalCount_Postcode0ValidPostcode1NotValid = 0;
        AllTotalCount_Postcode0NotValidPostcode1Valid = 0;
        AllTotalCount_Postcode0NotValidPostcode1NotValid = 0;
        AllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        AllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = 0;
        // HB
        HBTotalCount_TTChangeClaimant = 0;
        //HBTotalCount_TTChangeClaimantIgnoreMinus999 = 0;
        HBTotalCount_Postcode0ValidPostcode1Valid = 0;
        HBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        HBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged = 0;
        HBTotalCount_Postcode0ValidPostcode1NotValid = 0;
        HBTotalCount_Postcode0NotValidPostcode1Valid = 0;
        HBTotalCount_Postcode0NotValidPostcode1NotValid = 0;
        HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = 0;
        // CTB
        CTBTotalCount_TTChangeClaimant = 0;
        CTBTotalCount_Postcode0ValidPostcode1Valid = 0;
        CTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        CTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged = 0;
        CTBTotalCount_Postcode0ValidPostcode1NotValid = 0;
        CTBTotalCount_Postcode0NotValidPostcode1Valid = 0;
        CTBTotalCount_Postcode0NotValidPostcode1NotValid = 0;
        CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = 0;
    }

    protected void initCompare3TimesCounts(int nTT) {
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
        for (int i = 1; i < nTT; i++) {
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
        // Union
        HashSet union;
        union = new HashSet();
        if (set1 != null) {
            union.addAll(set1);
        }
        if (set0 != null) {
            union.retainAll(set0);
        }
        if (set00 != null) {
            union.retainAll(set00);
        }
        // set0Only
        HashSet set0Only;
        set0Only = new HashSet();
        if (set0 != null) {
            set0Only.addAll(set0);
        }
        if (set1 != null) {
            set0Only.removeAll(set1);
        }
        if (set00 != null) {
            set0Only.removeAll(set00);
        }
        // set00AndSet1
        HashSet set00AndSet1;
        set00AndSet1 = new HashSet();
        if (set00 != null) {
            set00AndSet1.addAll(set00);
        }
        if (set0 != null) {
            set00AndSet1.removeAll(set0);
        }
        if (set1 != null) {
            set00AndSet1.retainAll(set1);
        }
        result[0] = union;
        result[1] = set0Only;
        result[2] = set00AndSet1;
        return result;
    }

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
    public Object[] getCountsIDPostcodeTT(
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
        for (int i = 1; i < nTT; i++) {
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
    public Object[] getCountsIDTT(
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
        for (int i = 1; i < nTT; i++) {
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
    public Integer[] getCountsIDPostcode(
            String[] SHBEFilenames, ArrayList<Integer> include,
            String yM31,
            TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodeTypes,
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

    protected Object[] getPreviousYM3s(String[] SHBEFilenames, ArrayList<Integer> include, String yM31) {
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
            yM3 = tDW_SHBE_Handler.getYM3(SHBEFilenames[i]);
            if (yM3.equalsIgnoreCase(yM31)) {
                break;
            }
            if (yM300set && yM30set) {
                yM300 = yM30;
                yM30 = yM3;
            } else if (yM300set) {
                yM30 = yM3;
                yM30set = true;
            } else {
                yM300 = yM3;
                yM300set = true;
            }
        }
        result[0] = yM300set && yM30set;
        result[1] = yM300;
        result[2] = yM30;
        return result;
    }

    protected void addToSummaryCompare2Times(
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
//        summary.put(
//                sAllTotalCount_TTChangeClaimantIgnoreMinus999,
//                Integer.toString(AllTotalCount_TTChangeClaimantIgnoreMinus999));
        d = AllCount0;
        if (d > 0) {
            percentage = (AllTotalCount_TTChangeClaimant * 100.0d) / d;
            summary.put(
                    sAllPercentageOfAll_TTChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
//            percentage = (AllTotalCount_TTChangeClaimantIgnoreMinus999 * 100.0d) / d;
//            summary.put(
//                    sAllPercentageOfAll_TTChangeClaimantIgnoreMinus999,
//                    Generic_BigDecimal.roundIfNecessary(
//                            BigDecimal.valueOf(percentage),
//                            decimalPlacePrecisionForPercentage,
//                            RoundingMode.HALF_UP).toPlainString());
        }
        // Postcode
        summary.put(
                sAllTotalCount_Postcode0ValidPostcode1Valid,
                Integer.toString(HBTotalCount_Postcode0ValidPostcode1Valid + CTBTotalCount_Postcode0ValidPostcode1Valid));
        summary.put(
                sAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged,
                Integer.toString(AllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged));
        summary.put(
                sAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChange,
                Integer.toString(AllTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged));
        summary.put(
                sAllTotalCount_Postcode0ValidPostcode1NotValid,
                Integer.toString(AllTotalCount_Postcode0ValidPostcode1NotValid));
        summary.put(
                sAllTotalCount_Postcode0NotValidPostcode1Valid,
                Integer.toString(AllTotalCount_Postcode0NotValidPostcode1Valid));
        summary.put(
                sAllTotalCount_Postcode0NotValidPostcode1NotValid,
                Integer.toString(AllTotalCount_Postcode0NotValidPostcode1NotValid));
        summary.put(
                sAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged,
                Integer.toString(AllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged));
        summary.put(
                sAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged,
                Integer.toString(AllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged));
        if (d > 0) {
            percentage = (AllTotalCount_Postcode0ValidPostcode1Valid * 100.0d) / d;
            summary.put(
                    sAllPercentagePostcode0ValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (AllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged * 100.0d) / d;
            summary.put(
                    sAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (AllTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged * 100.0d) / d;
            summary.put(
                    sAllPercentagePostcode0ValidPostcode1ValidPostcodeChange,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (AllTotalCount_Postcode0ValidPostcode1NotValid * 100.0d) / d;
            summary.put(
                    sAllPercentagePostcode0ValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (AllTotalCount_Postcode0NotValidPostcode1Valid * 100.0d) / d;
            summary.put(
                    sAllPercentagePostcode0NotValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (AllTotalCount_Postcode0NotValidPostcode1NotValid * 100.0d) / d;
            summary.put(
                    sAllPercentagePostcode0NotValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (AllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged * 100.0d) / d;
            summary.put(
                    sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (AllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged * 100.0d) / d;
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
//        summary.put(
//                sHBTotalCount_TTChangeClaimantIgnoreMinus999,
//                Integer.toString(HBTotalCount_TTChangeClaimantIgnoreMinus999));

        summary.put(
                sTotalCount_Minus999TTToSocialTTs,
                Integer.toString(TotalCount_Minus999TTToSocialTTs));
        summary.put(
                sTotalCount_Minus999TTToPrivateDeregulatedTTs,
                Integer.toString(TotalCount_Minus999TTToPrivateDeregulatedTTs));

        summary.put(
                sTotalCount_HBTTsToCTBTTs,
                Integer.toString(TotalCount_HBTTsToCTBTTs));
        summary.put(
                sTotalCount_HBTTsToHBTTs,
                Integer.toString(TotalCount_HBTTsToHBTTs));
        summary.put(
                sTotalCount_HBTTsToMinus999TT,
                Integer.toString(TotalCount_HBTTsToMinus999TT));
        if (d > 0) {
            percentage = (HBTotalCount_TTChangeClaimant * 100.0d) / d;
            summary.put(
                    sHBPercentageOfHB_TTChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
//            percentage = (HBTotalCount_TTChangeClaimantIgnoreMinus999 * 100.0d) / d;
//            summary.put(
//                    sHBPercentageOfHB_TTChangeClaimantIgnoreMinus999,
//                    Generic_BigDecimal.roundIfNecessary(
//                            BigDecimal.valueOf(percentage),
//                            decimalPlacePrecisionForPercentage,
//                            RoundingMode.HALF_UP).toPlainString());
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
            percentage = (TotalCount_HBTTsToMinus999TT * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_HBTTsToMinus999TT,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // Postcode
        summary.put(
                sHBTotalCount_Postcode0ValidPostcode1Valid,
                Integer.toString(HBTotalCount_Postcode0ValidPostcode1Valid));
        summary.put(
                sHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged,
                Integer.toString(HBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged));
        summary.put(
                sHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChange,
                Integer.toString(HBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged));
        summary.put(
                sHBTotalCount_Postcode0ValidPostcode1NotValid,
                Integer.toString(HBTotalCount_Postcode0ValidPostcode1NotValid));
        summary.put(
                sHBTotalCount_Postcode0NotValidPostcode1Valid,
                Integer.toString(HBTotalCount_Postcode0NotValidPostcode1Valid));
        summary.put(
                sHBTotalCount_Postcode0NotValidPostcode1NotValid,
                Integer.toString(HBTotalCount_Postcode0NotValidPostcode1NotValid));
        summary.put(
                sHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged,
                Integer.toString(HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged));
        summary.put(
                sHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged,
                Integer.toString(HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged));
        if (d > 0) {
            percentage = (HBTotalCount_Postcode0ValidPostcode1Valid * 100.0d) / d;
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged * 100.0d) / d;
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged * 100.0d) / d;
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1ValidPostcodeChange,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCount_Postcode0ValidPostcode1NotValid * 100.0d) / d;
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCount_Postcode0NotValidPostcode1Valid * 100.0d) / d;
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCount_Postcode0NotValidPostcode1NotValid * 100.0d) / d;
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged * 100.0d) / d;
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged * 100.0d) / d;
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
        summary.put(
                sTotalCount_PrivateDeregulatedTTsToMinus999TT,
                Integer.toString(TotalCount_PrivateDeregulatedTTsToMinus999TT));
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
            percentage = (TotalCount_PrivateDeregulatedTTsToMinus999TT * 100.0d) / d;
            summary.put(
                    sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT,
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
                Integer.toString(TotalCount_SocialTTsToCTBTTs));
        summary.put(
                sTotalCount_Minus999TTToCTBTTs,
                Integer.toString(TotalCount_Minus999TTToCTBTTs));
        summary.put(
                sTotalCount_SocialTTsToMinus999TT,
                Integer.toString(TotalCount_SocialTTsToMinus999TT));
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
            percentage = (TotalCount_SocialTTsToCTBTTs * 100.0d) / d;
            summary.put(
                    sPercentageOfSocialTTs_SocialTTsToCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_SocialTTsToMinus999TT * 100.0d) / d;
            summary.put(
                    sPercentageOfSocialTTs_SocialTTsToMinus999TT,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // TT1
        d = TotalCount_TTClaimant0[1];
        summary.put(
                sTotalCount_TT1ToTT4,
                Integer.toString(TotalCount_TT1ToTT4));
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
            percentage = (TotalCount_TT1ToTT4 * 100.0d) / d;
            summary.put(
                    sPercentageOfTT1_TT1ToTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // TT4
        d = TotalCount_TTClaimant0[4];
        summary.put(
                sTotalCount_TT4ToTT1,
                Integer.toString(TotalCount_TT4ToTT1));
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
            percentage = (TotalCount_TT4ToTT1 * 100.0d) / d;
            summary.put(
                    sPercentageOfTT4_TT4ToTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // CTB
        summary.put(
                sCTBTotalCount_Postcode0ValidPostcode1Valid,
                Integer.toString(CTBTotalCount_Postcode0ValidPostcode1Valid));
        summary.put(
                sCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged,
                Integer.toString(CTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged));
        summary.put(
                sCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged,
                Integer.toString(CTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged));
        summary.put(
                sCTBTotalCount_Postcode0ValidPostcode1NotValid,
                Integer.toString(CTBTotalCount_Postcode0ValidPostcode1NotValid));
        summary.put(
                sCTBTotalCount_Postcode0NotValidPostcode1Valid,
                Integer.toString(CTBTotalCount_Postcode0NotValidPostcode1Valid));
        summary.put(
                sCTBTotalCount_Postcode0NotValidPostcode1NotValid,
                Integer.toString(CTBTotalCount_Postcode0NotValidPostcode1NotValid));
        summary.put(
                sCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged,
                Integer.toString(CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged));
        summary.put(
                sCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged,
                Integer.toString(CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged));

        summary.put(
                sCTBTotalCount_TTChangeClaimant,
                Integer.toString(CTBTotalCount_TTChangeClaimant));
//        summary.put(
//                sCTBTotalCount_TTChangeClaimantIgnoreMinus999,
//                Integer.toString(CTBTotalCount_TTChangeClaimantIgnoreMinus999));

        summary.put(
                sTotalCount_CTBTTsToSocialTTs,
                Integer.toString(TotalCount_CTBTTsToSocialTTs));
        summary.put(
                sTotalCount_CTBTTsToMinus999TT,
                Integer.toString(TotalCount_CTBTTsToMinus999TT));
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
            percentage = (CTBTotalCount_Postcode0ValidPostcode1Valid * 100.0d) / d;
            summary.put(
                    sCTBPercentageOfCTB_Postcode0ValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged * 100.0d) / d;
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged * 100.0d) / d;
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCount_Postcode0ValidPostcode1NotValid * 100.0d) / d;
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCount_Postcode0NotValidPostcode1Valid * 100.0d) / d;
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCount_Postcode0NotValidPostcode1NotValid * 100.0d) / d;
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged * 100.0d) / d;
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged * 100.0d) / d;
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
//            percentage = (CTBTotalCount_TTChangeClaimantIgnoreMinus999 * 100.0d) / d;
//            summary.put(
//                    sCTBPercentageOfCTB_TTChangeClaimantIgnoreMinus999,
//                    Generic_BigDecimal.roundIfNecessary(
//                            BigDecimal.valueOf(percentage),
//                            decimalPlacePrecisionForPercentage,
//                            RoundingMode.HALF_UP).toPlainString());

            percentage = (TotalCount_CTBTTsToSocialTTs * 100.0d) / d;
            summary.put(
                    sPercentageOfCTB_CTBTTsToSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());

            percentage = (TotalCount_CTBTTsToMinus999TT * 100.0d) / d;
            summary.put(
                    sPercentageOfCTB_CTBTTsToMinus999TT,
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

    /**
     *
     * @param nTT
     * @param nEG
     * @param nPSI
     * @param summary
     */
    protected void addToSummarySingleTime(
            int nTT,
            int nEG,
            int nPSI,
            HashMap<String, String> summary) {
        // Set the last results
        AllCount0 = AllCount1;
        //AllCount0 = HBCount1 + CTBCount1;
        HBCount0 = HBCount1;
        CTBCount0 = CTBCount1;
//        for (int TT = 0; TT < nTT; TT++) {
//            TotalCount_TTClaimant0[TT] = TotalCount_TTClaimant1[TT];
//        }
        System.arraycopy(TotalCount_TTClaimant1, 0, TotalCount_TTClaimant0, 0, nTT);
        // Add Counts
        addToSummarySingleTimeCounts0(summary);
        addToSummarySingleTimeDisabilityCounts(nTT, summary);
        addToSummarySingleTimeEthnicityCounts(nEG, nTT, summary);
        addToSummarySingleTimeTTCounts(nTT, summary);
        addToSummarySingleTimePSICounts(nTT, nPSI, summary);
        addToSummarySingleTimeCounts1(summary);

        // Add Rates
        addToSummarySingleTimeRates0(nTT, summary);
        addToSummarySingleTimeDisabilityRates(nTT, summary);
        addToSummarySingleTimeEthnicityRates(nEG, nTT, summary);
        addToSummarySingleTimeTTRates(nTT, summary);
        addToSummarySingleTimePSIRates(nTT, nPSI, summary);
        addToSummarySingleTimeRates1(summary);

    }

    protected void addToSummarySingleTimeCounts0(
            HashMap<String, String> summary) {
        // All
        AllCount1 = HBCount1 + CTBCount1;
        summary.put(sAllCount1, Integer.toString(AllCount1));
        summary.put(sHBCount1, Integer.toString(HBCount1));
        summary.put(sCTBCount1, Integer.toString(CTBCount1));
        summary.put(
                sAllTotalHouseholdSize,
                Long.toString(AllTotalHouseholdSize));
    }

    protected void addToSummarySingleTimeRates0(
            int nTT,
            HashMap<String, String> summary) {
        double ave;
        double d;
        double n;
        // All HouseholdSize
        d = AllCount1;
        n = AllTotalHouseholdSize;
        if (d > 0) {
            ave = n / d;
            summary.put(
                    sAllAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAllAverageHouseholdSize,
                    s0);
        }
        // HB HouseholdSize
        summary.put(
                sHBTotalHouseholdSize,
                Long.toString(HBTotalHouseholdSize));
        d = HBCount1;
        n = HBTotalHouseholdSize;
        if (d > 0) {
            ave = n / d;
            summary.put(
                    sHBAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBAverageHouseholdSize,
                    s0);
        }
        // CTB HouseholdSize
        summary.put(
                sCTBTotalHouseholdSize,
                Long.toString(CTBTotalHouseholdSize));
        d = CTBCount1;
        n = CTBTotalHouseholdSize;
        if (d > 0) {
            ave = n / d;
            summary.put(
                    sCTBAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBAverageHouseholdSize,
                    s0);
        }
        // TotalHouseholdSizeByTT
        for (int i = 1; i < nTT; i++) {
            summary.put(
                    sAllTotalHouseholdSizeByTT[i],
                    Long.toString(AllTotalHouseholdSizeByTT[i]));
            d = TotalCount_TTClaimant1[i];
            n = AllTotalHouseholdSizeByTT[i];
            if (d > 0) {
                ave = n / d;
                summary.put(
                        sAllAverageHouseholdSizeByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sAllAverageHouseholdSizeByTT[i],
                        s0);
            }
        }
    }

    protected void addToSummarySingleTimePSICounts(
            int nTT,
            int nPSI,
            HashMap<String, String> summary) {
        // PassportStandardIndicator
        for (int i = 1; i < nPSI; i++) {
            summary.put(
                    sAllTotalCount_PSI[i],
                    Long.toString(AllTotalCount_PSI[i]));
            summary.put(
                    sHBTotalCount_PSI[i],
                    Long.toString(HBTotalCount_PSI[i]));
            summary.put(
                    sCTBTotalCount_PSI[i],
                    Long.toString(CTBTotalCount_PSI[i]));
            for (int j = 1; j < nTT; j++) {
                summary.put(
                        sTotalCount_PSIByTT[i][j],
                        Long.toString(AllTotalCount_PSIByTT[i][j]));
            }
        }
    }

    protected void addToSummarySingleTimePSIRates(
            int nTT,
            int nPSI,
            HashMap<String, String> summary) {
        double ave;
        double d;
        double all;
        // PassportStandardIndicator
        for (int i = 1; i < nPSI; i++) {
            all = Integer.valueOf(summary.get(sAllTotalCount_PSI[i]));
            d = AllCount1;
            if (d > 0) {
                ave = (all * 100.0d) / d;
                summary.put(
                        sAllPercentageOfAll_PSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sHBTotalCount_PSI[i]));
            d = HBCount1;
            if (d > 0) {
                ave = (all * 100.0d) / d;
                summary.put(
                        sHBPercentageOfHB_PSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sCTBTotalCount_PSI[i]));
            d = CTBCount1;
            if (d > 0) {
                ave = (all * 100.0d) / d;
                summary.put(
                        sCTBPercentageOfCTB_PSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            for (int j = 1; j < nTT; j++) {
                all = Integer.valueOf(summary.get(sTotalCount_PSIByTT[i][j]));
                d = AllCount1;
                if (d > 0) {
                    ave = (all * 100.0d) / d;
                    summary.put(
                            sPercentageOfAll_PSIByTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
                d = TotalCount_TTClaimant1[j];
                if (d > 0) {
                    ave = (all * 100.0d) / d;
                    summary.put(
                            sPercentageOfTT_PSIByTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
                d = HBCount1;
                if (d > 0) {
                    ave = (all * 100.0d) / d;
                    summary.put(
                            sPercentageOfHB_PSIByTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
                d = CTBCount1;
                if (d > 0) {
                    ave = (all * 100.0d) / d;
                    summary.put(
                            sPercentageOfCTB_PSIByTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
            }
        }
    }

    protected void addToSummarySingleTimeDisabilityCounts(
            int nTT,
            HashMap<String, String> summary) {
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
        // DisabilityAwardCTBTTs
        t = TotalCount_DisabilityAwardByTT[5]
                + TotalCount_DisabilityAwardByTT[7];
        summary.put(
                sTotalCount_DisabilityAwardCTBTTs,
                Integer.toString(t));
        // DisabilityAwardSocialTTs
        t = TotalCount_DisabilityAwardByTT[1] + TotalCount_DisabilityAwardByTT[4];
        summary.put(
                sTotalCount_DisabilityAwardSocialTTs,
                Integer.toString(t));
        // DisabilityAwardPrivateDeregulatedTTs
        t = TotalCount_DisabilityAwardByTT[3] + TotalCount_DisabilityAwardByTT[6];
        summary.put(
                sTotalCount_DisabilityAwardPrivateDeregulatedTTs,
                Integer.toString(t));
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
        // DisabilityPremiumAwardCTBTTs
        t = TotalCount_DisabilityPremiumAwardByTT[5]
                + TotalCount_DisabilityPremiumAwardByTT[7];
        summary.put(
                sTotalCount_DisabilityPremiumAwardCTBTTs,
                Integer.toString(t));
        // DisabilityPremiumAwardSocialTTs
        t = TotalCount_DisabilityPremiumAwardByTT[1] + TotalCount_DisabilityPremiumAwardByTT[4];
        summary.put(
                sTotalCount_DisabilityPremiumAwardSocialTTs,
                Integer.toString(t));
        // DisabilityPremiumAwardPrivateDeregulatedTTs
        t = TotalCount_DisabilityPremiumAwardByTT[3] + TotalCount_DisabilityPremiumAwardByTT[6];
        summary.put(
                sTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
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
        // SevereDisabilityPremiumAwardCTBTTs
        t = TotalCount_SevereDisabilityPremiumAwardByTT[5]
                + TotalCount_SevereDisabilityPremiumAwardByTT[7];
        summary.put(
                sTotalCount_SevereDisabilityPremiumAwardCTBTTs,
                Integer.toString(t));
        // SevereDisabilityPremiumAwardSocialTTs
        t = TotalCount_SevereDisabilityPremiumAwardByTT[1] + TotalCount_SevereDisabilityPremiumAwardByTT[4];
        summary.put(
                sTotalCount_SevereDisabilityPremiumAwardSocialTTs,
                Integer.toString(t));
        // SevereDisabilityPremiumAwardPrivateDeregulatedTTs
        t = TotalCount_SevereDisabilityPremiumAwardByTT[3] + TotalCount_SevereDisabilityPremiumAwardByTT[6];
        summary.put(
                sTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
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
        // DisabledChildPremiumAwardCTBTTs
        t = TotalCount_DisabledChildPremiumAwardByTT[5]
                + TotalCount_DisabledChildPremiumAwardByTT[7];
        summary.put(
                sTotalCount_DisabledChildPremiumAwardCTBTTs,
                Integer.toString(t));
        // DisabledChildPremiumAwardSocialTTs
        t = TotalCount_DisabledChildPremiumAwardByTT[1] + TotalCount_DisabledChildPremiumAwardByTT[4];
        summary.put(
                sTotalCount_DisabledChildPremiumAwardSocialTTs,
                Integer.toString(t));
        // DisabledChildPremiumAwardPrivateDeregulatedTTs
        t = TotalCount_DisabledChildPremiumAwardByTT[3] + TotalCount_DisabledChildPremiumAwardByTT[6];
        summary.put(
                sTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
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
        // EnhancedDisabilityPremiumAwardCTBTTs
        t = TotalCount_EnhancedDisabilityPremiumAwardByTT[5]
                + TotalCount_EnhancedDisabilityPremiumAwardByTT[7];
        summary.put(
                sTotalCount_EnhancedDisabilityPremiumAwardCTBTTs,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAwardSocialTTs
        t = TotalCount_EnhancedDisabilityPremiumAwardByTT[1] + TotalCount_EnhancedDisabilityPremiumAwardByTT[4];
        summary.put(
                sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs
        t = TotalCount_EnhancedDisabilityPremiumAwardByTT[3] + TotalCount_EnhancedDisabilityPremiumAwardByTT[6];
        summary.put(
                sTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        // ByTT
        for (int i = 1; i < nTT; i++) {
            summary.put(
                    sTotalCount_DisabilityAwardByTT[i],
                    Integer.toString(TotalCount_DisabilityAwardByTT[i]));
            summary.put(
                    sTotalCount_DisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_DisabilityPremiumAwardByTT[i]));
            summary.put(
                    sTotalCount_SevereDisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_SevereDisabilityPremiumAwardByTT[i]));
            summary.put(
                    sTotalCount_DisabledChildPremiumAwardByTT[i],
                    Integer.toString(TotalCount_DisabledChildPremiumAwardByTT[i]));
            summary.put(
                    sTotalCount_EnhancedDisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
        }
    }

    protected void addToSummarySingleTimeDisabilityRates(
            int nTT,
            HashMap<String, String> summary) {
        double percentage;
        double d;
        int t;
        // DisabilityAward
        t = Integer.valueOf(summary.get(sTotalCount_DisabilityAward));
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
        t = Integer.valueOf(summary.get(sTotalCount_DisabilityAwardHBTTs));
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
        // DisabilityAwardCTBTTs
        t = Integer.valueOf(summary.get(sTotalCount_DisabilityAwardCTBTTs));
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
        t = Integer.valueOf(summary.get(sTotalCount_DisabilityAwardSocialTTs));
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
        d = TotalCount_TTClaimant0[1] + TotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfSocialTTs_DisabilityAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityAwardPrivateDeregulatedTTs
        t = Integer.valueOf(summary.get(sTotalCount_DisabilityAwardPrivateDeregulatedTTs));
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
        d = TotalCount_TTClaimant0[3] + TotalCount_TTClaimant0[6];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAward
        t = Integer.valueOf(summary.get(sTotalCount_DisabilityPremiumAward));
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
        t = Integer.valueOf(summary.get(sTotalCount_DisabilityPremiumAwardHBTTs));
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
        t = Integer.valueOf(summary.get(sTotalCount_DisabilityPremiumAwardCTBTTs));
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
        t = Integer.valueOf(summary.get(sTotalCount_DisabilityPremiumAwardSocialTTs));
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
        d = TotalCount_TTClaimant0[1] + TotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAwardPrivateDeregulatedTTs
        t = Integer.valueOf(summary.get(sTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs));
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
        d = TotalCount_TTClaimant0[3] + TotalCount_TTClaimant0[6];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabilityPremiumAward
        t = Integer.valueOf(summary.get(sTotalCount_SevereDisabilityPremiumAward));
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
        t = Integer.valueOf(summary.get(sTotalCount_SevereDisabilityPremiumAwardHBTTs));
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
        t = Integer.valueOf(summary.get(sTotalCount_SevereDisabilityPremiumAwardCTBTTs));
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
        t = Integer.valueOf(summary.get(sTotalCount_SevereDisabilityPremiumAwardSocialTTs));
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
        d = TotalCount_TTClaimant0[1] + TotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabilityPremiumAwardPrivateDeregulatedTTs
        t = Integer.valueOf(summary.get(sTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs));
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
        d = TotalCount_TTClaimant0[3] + TotalCount_TTClaimant0[6];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAward
        t = Integer.valueOf(summary.get(sTotalCount_DisabledChildPremiumAward));
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
        t = Integer.valueOf(summary.get(sTotalCount_DisabledChildPremiumAwardHBTTs));
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
        t = Integer.valueOf(summary.get(sTotalCount_DisabledChildPremiumAwardCTBTTs));
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
        t = Integer.valueOf(summary.get(sTotalCount_DisabledChildPremiumAwardSocialTTs));
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
        d = TotalCount_TTClaimant0[1] + TotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAwardPrivateDeregulatedTTs
        t = Integer.valueOf(summary.get(sTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs));
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
        d = TotalCount_TTClaimant0[3] + TotalCount_TTClaimant0[6];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAward
        t = Integer.valueOf(summary.get(sTotalCount_EnhancedDisabilityPremiumAward));
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
        t = Integer.valueOf(summary.get(sTotalCount_EnhancedDisabilityPremiumAwardHBTTs));
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
        t = Integer.valueOf(summary.get(sTotalCount_EnhancedDisabilityPremiumAwardCTBTTs));
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
        t = Integer.valueOf(summary.get(sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs));
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
        d = TotalCount_TTClaimant0[1] + TotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs
        t = Integer.valueOf(summary.get(sTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs));
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
        d = TotalCount_TTClaimant0[1] + TotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // ByTT
        for (int i = 1; i < nTT; i++) {
            // AllCount1
            d = AllCount1;
            t = Integer.valueOf(summary.get(sTotalCount_DisabilityAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfAll_DisabilityAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_DisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfAll_DisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_SevereDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfAll_SevereDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_DisabledChildPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfAll_DisabledChildPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            // HBCount1;
            d = HBCount1;
            t = Integer.valueOf(summary.get(sTotalCount_DisabilityAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfHB_DisabilityAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_DisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfHB_DisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_SevereDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfHB_SevereDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_DisabledChildPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfHB_DisabledChildPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            // CTBCount1
            d = CTBCount1;
            t = Integer.valueOf(summary.get(sTotalCount_DisabilityAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfCTB_DisabilityAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_DisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfCTB_DisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_SevereDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_SevereDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_DisabledChildPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_DisabledChildPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sPercentageOfCTB_DisabledChildPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_EnhancedDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            // TT
            d = TotalCount_TTClaimant1[i];
            t = Integer.valueOf(summary.get(sTotalCount_DisabilityAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfTT_DisabilityAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_DisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfTT_DisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_SevereDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_SevereDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sPercentageOfTT_SevereDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_DisabledChildPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_DisabledChildPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sPercentageOfTT_DisabledChildPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_EnhancedDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sPercentageOfTT_EnhancedDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
        }
    }

    protected void addToSummarySingleTimeCounts1(
            HashMap<String, String> summary) {
        // WeeklyHBEntitlement
        summary.put(
                sTotalWeeklyHBEntitlement,
                BigDecimal.valueOf(AllTotalWeeklyHBEntitlement).toPlainString());
        summary.put(
                sTotalCount_WeeklyHBEntitlementNonZero,
                Integer.toString(AllTotalWeeklyHBEntitlementNonZeroCount));
        summary.put(
                sTotalCount_WeeklyHBEntitlementZero,
                Integer.toString(AllTotalWeeklyHBEntitlementZeroCount));
        summary.put(
                sHBTotalWeeklyHBEntitlement,
                BigDecimal.valueOf(HBTotalWeeklyHBEntitlement).toPlainString());
        summary.put(sHBTotalCount_WeeklyHBEntitlementNonZero,
                Integer.toString(HBTotalCount_WeeklyHBEntitlementNonZero));
        summary.put(sHBTotalCount_WeeklyHBEntitlementZero,
                Integer.toString(HBTotalCount_WeeklyHBEntitlementZero));
        summary.put(
                sCTBTotalWeeklyHBEntitlement,
                BigDecimal.valueOf(CTBTotalWeeklyHBEntitlement).toPlainString());
        summary.put(sCTBTotalCount_WeeklyHBEntitlementNonZero,
                Integer.toString(CTBTotalCount_WeeklyHBEntitlementNonZero));
        summary.put(
                sCTBTotalCount_WeeklyHBEntitlementZero,
                Integer.toString(CTBTotalWeeklyHBEntitlementZeroCount));
        // WeeklyCTBEntitlement
        summary.put(
                sTotalWeeklyCTBEntitlement,
                BigDecimal.valueOf(AllTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(
                sTotalCount_WeeklyCTBEntitlementNonZero,
                Integer.toString(AllTotalCount_WeeklyCTBEntitlementNonZero));
        summary.put(
                sTotalCount_WeeklyCTBEntitlementZero,
                Integer.toString(AllTotalWeeklyCTBEntitlementZeroCount));
        summary.put(
                sHBTotalWeeklyCTBEntitlement,
                BigDecimal.valueOf(HBTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(sHBTotalCount_WeeklyCTBEntitlementNonZero,
                Integer.toString(HBTotalCount_WeeklyCTBEntitlementNonZero));
        summary.put(sHBTotalCount_WeeklyCTBEntitlementZero,
                Integer.toString(HBTotalWeeklyCTBEntitlementZeroCount));
        summary.put(
                sCTBTotalWeeklyCTBEntitlement,
                BigDecimal.valueOf(CTBTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(sCTBTotalCount_WeeklyCTBEntitlementNonZero,
                Integer.toString(CTBTotalCount_WeeklyCTBEntitlementNonZero));
        summary.put(
                sCTBTotalCount_WeeklyCTBEntitlementZero,
                Integer.toString(CTBTotalWeeklyCTBEntitlementZeroCount));
        // WeeklyEligibleRentAmount
        summary.put(
                sAllTotalWeeklyEligibleRentAmount,
                BigDecimal.valueOf(AllTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                sAllTotalCount_WeeklyEligibleRentAmountNonZero,
                Integer.toString(AllTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(
                sAllTotalCount_WeeklyEligibleRentAmountZero,
                Integer.toString(AllTotalWeeklyEligibleRentAmountZeroCount));
        summary.put(
                sHBTotalWeeklyEligibleRentAmount,
                BigDecimal.valueOf(HBTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                sHBTotalCount_WeeklyEligibleRentAmountNonZero,
                Integer.toString(HBTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(
                sHBTotalCount_WeeklyEligibleRentAmountZero,
                Integer.toString(HBTotalWeeklyEligibleRentAmountZeroCount));
        summary.put(
                sCTBTotalWeeklyEligibleRentAmount,
                BigDecimal.valueOf(CTBTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                sCTBTotalCount_WeeklyEligibleRentAmountNonZero,
                Integer.toString(CTBTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(
                sCTBTotalCount_WeeklyEligibleRentAmountZero,
                Integer.toString(CTBTotalWeeklyEligibleRentAmountZeroCount));
        // WeeklyEligibleCouncilTaxAmount
        summary.put(
                sAllTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(AllTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(sAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(AllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(sAllTotalCount_WeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(AllTotalCount_WeeklyEligibleCouncilTaxAmountZero));
        summary.put(
                sHBTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(HBTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(sHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(HBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(sHBTotalCount_WeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(HBTotalCount_WeeklyEligibleCouncilTaxAmountZero));
        summary.put(
                sCTBTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(CTBTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(sCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(CTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(sCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(CTBTotalCount_WeeklyEligibleCouncilTaxAmountZero));
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
        summary.put(
                sHBTotalContractualRentAmount,
                BigDecimal.valueOf(HBTotalContractualRentAmount).toPlainString());
        summary.put(
                sHBTotalCountContractualRentAmountNonZeroCount,
                Integer.toString(HBTotalContractualRentAmountNonZeroCount));
        summary.put(
                sHBTotalCountContractualRentAmountZeroCount,
                Integer.toString(HBTotalContractualRentAmountZeroCount));
        summary.put(
                sCTBTotalContractualRentAmount,
                BigDecimal.valueOf(CTBTotalContractualRentAmount).toPlainString());
        summary.put(
                sCTBTotalCountContractualRentAmountNonZeroCount,
                Integer.toString(CTBTotalContractualRentAmountNonZeroCount));
        summary.put(
                sCTBTotalCountContractualRentAmountZeroCount,
                Integer.toString(CTBTotalContractualRentAmountZeroCount));
        // WeeklyAdditionalDiscretionaryPayment
        summary.put(
                sAllTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(AllTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(AllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(AllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        summary.put(
                sHBTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(HBTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(HBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(HBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        summary.put(
                sCTBTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(CTBTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(CTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(CTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        summary.put(
                sAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        summary.put(
                sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        summary.put(
                sCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        // Employed
        int t;
        t = HBTotalCount_EmployedClaimants + CTBTotalCount_EmployedClaimants;
        summary.put(
                sAllTotalCount_ClaimantsEmployed,
                Integer.toString(t));
        summary.put(
                sHBTotalCount_ClaimantsEmployed,
                Integer.toString(HBTotalCount_EmployedClaimants));
        summary.put(sCTBTotalCount_ClaimantsEmployed,
                Integer.toString(CTBTotalCount_EmployedClaimants));
        // Self Employed
        summary.put(sHBTotalCountClaimantsSelfEmployed,
                Integer.toString(HBTotalCount_SelfEmployedClaimants));
        summary.put(sCTBTotalCountClaimantsSelfEmployed,
                Integer.toString(CTBTotalCount_SelfEmployedClaimants));
        // Students
        summary.put(sHBTotalCountClaimantsStudents,
                Integer.toString(HBTotalCount_StudentsClaimants));
        summary.put(sCTBTotalCountClaimantsStudents,
                Integer.toString(CTBTotalCount_StudentsClaimants));
        // LHACases
        t = HBTotalCount_LHACases + CTBTotalCount_LHACases;
        summary.put(
                sAllTotalCount_LHACases,
                Integer.toString(t));
        summary.put(sHBTotalCount_LHACases,
                Integer.toString(HBTotalCount_LHACases));
        summary.put(
                sCTBTotalCount_LHACases,
                Integer.toString(CTBTotalCount_LHACases));
    }

    protected void addToSummarySingleTimeRates1(
            HashMap<String, String> summary) {
        double ave;
        double d;
        double t;
        // WeeklyHBEntitlement
        t = Double.valueOf(summary.get(sTotalWeeklyHBEntitlement));
        d = AllTotalWeeklyHBEntitlementNonZeroCount;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sHBTotalWeeklyHBEntitlement));
        d = HBTotalCount_WeeklyHBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sCTBTotalWeeklyHBEntitlement));
        d = CTBTotalCount_WeeklyHBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sTotalWeeklyCTBEntitlement));
        d = AllTotalCount_WeeklyCTBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sHBTotalWeeklyCTBEntitlement));
        d = HBTotalCount_WeeklyCTBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sCTBTotalWeeklyCTBEntitlement));
        d = CTBTotalCount_WeeklyCTBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sAllTotalWeeklyEligibleRentAmount));
        d = AllTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sHBTotalWeeklyEligibleRentAmount));
        d = HBTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sCTBTotalWeeklyEligibleRentAmount));
        d = CTBTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sAllTotalWeeklyEligibleCouncilTaxAmount));
        d = AllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sHBTotalWeeklyEligibleCouncilTaxAmount));
        d = HBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sCTBTotalWeeklyEligibleCouncilTaxAmount));
        d = CTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sAllTotalContractualRentAmount));
        d = AllTotalContractualRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sHBTotalContractualRentAmount));
        d = HBTotalContractualRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sCTBTotalContractualRentAmount));
        d = CTBTotalContractualRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sAllTotalWeeklyAdditionalDiscretionaryPayment));
        d = AllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sHBTotalWeeklyAdditionalDiscretionaryPayment));
        d = HBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sCTBTotalWeeklyAdditionalDiscretionaryPayment));
        d = CTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability));
        d = AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability));
        d = HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
        if (d > 0) {
            ave = t / d;
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
        t = Double.valueOf(summary.get(sCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability));
        d = CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
        if (d > 0) {
            ave = t / d;
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
        t = Integer.valueOf(summary.get(sAllTotalCount_ClaimantsEmployed));
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
        t = Integer.valueOf(summary.get(sHBTotalCount_ClaimantsEmployed));
        d = HBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
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
        t = Integer.valueOf(summary.get(sCTBTotalCount_ClaimantsEmployed));
        d = CTBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
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
        t = Integer.valueOf(summary.get(sHBTotalCountClaimantsSelfEmployed));
        d = HBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
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
        t = Integer.valueOf(summary.get(sCTBTotalCountClaimantsSelfEmployed));
        d = CTBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
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
        t = Integer.valueOf(summary.get(sHBTotalCountClaimantsStudents));
        d = HBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
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
        t = Integer.valueOf(summary.get(sCTBTotalCountClaimantsStudents));
        d = CTBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
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
        t = Integer.valueOf(summary.get(sAllTotalCount_LHACases));
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
        t = Integer.valueOf(summary.get(sHBTotalCount_LHACases));
        d = HBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
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
        t = Integer.valueOf(summary.get(sCTBTotalCount_LHACases));
        d = CTBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
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

    protected void addToSummarySingleTimeEthnicityCounts(
            int nEG,
            int nTT,
            HashMap<String, String> summary) {
        for (int i = 1; i < nEG; i++) {
            int all;
            all = HBEthnicGroupCount[i] + CTBEthnicGroupCount[i];
            summary.put(
                    sAllTotalCount_EthnicGroupClaimant[i],
                    Integer.toString(all));
            summary.put(
                    sHBTotalCount_EthnicGroupClaimant[i],
                    Integer.toString(HBEthnicGroupCount[i]));
            summary.put(
                    sCTBTotalCount_EthnicGroupClaimant[i],
                    Integer.toString(CTBEthnicGroupCount[i]));
            for (int j = 0; j < nTT; j++) {
                summary.put(
                        sAllTotalCount_EthnicGroupClaimantByTT[i][j],
                        Integer.toString(AllTotalCount_EthnicGroupClaimantByTT[i][j]));
            }
            summary.put(
                    sAllTotalCount_EthnicGroupSocialTTClaimant[i],
                    Integer.toString(AllTotalCount_EthnicGroupClaimantByTT[i][1] + AllTotalCount_EthnicGroupClaimantByTT[i][4]));
            summary.put(
                    sAllTotalCount_EthnicGroupPrivateDeregulatedTTClaimant[i],
                    Integer.toString(AllTotalCount_EthnicGroupClaimantByTT[i][3] + AllTotalCount_EthnicGroupClaimantByTT[i][6]));
        }
    }

    protected void addToSummarySingleTimeEthnicityRates(
            int nEG,
            int nTT,
            HashMap<String, String> summary) {
        // Ethnicity
        double percentage;
        double all;
        double d;
        for (int i = 1; i < nEG; i++) {
            all = Integer.valueOf(summary.get(sAllTotalCount_EthnicGroupClaimant[i]));
            d = AllCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(sAllPercentageOfAll_EthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sHBTotalCount_EthnicGroupClaimant[i]));
            d = HBCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(sHBPercentageOfHB_EthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sCTBTotalCount_EthnicGroupClaimant[i]));
            d = CTBCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(sCTBPercentageOfCTB_EthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            for (int j = 1; j < nTT; j++) {
                all = Integer.valueOf(summary.get(sAllTotalCount_EthnicGroupClaimantByTT[i][j]));
                d = TotalCount_TTClaimant1[j];
                if (d > 0) {
                    percentage = (all * 100.0d) / d;
                    summary.put(
                            sAllPercentageOfTT_EthnicGroupClaimantByTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(percentage),
                                    decimalPlacePrecisionForPercentage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
                d = Integer.valueOf(summary.get(sAllTotalCount_EthnicGroupClaimant[i]));
                if (d > 0) {
                    percentage = (all * 100.0d) / d;
                    summary.put(
                            sAllPercentageOfEthnicGroup_EthnicGroupClaimantByTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(percentage),
                                    decimalPlacePrecisionForPercentage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
            }
            all = Integer.valueOf(summary.get(sAllTotalCount_EthnicGroupSocialTTClaimant[i]));
            d = TotalCount_TTClaimant1[1] + TotalCount_TTClaimant1[4];
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(
                        sAllPercentageOfSocialTT_EthnicGroupSocialTTClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sAllTotalCount_EthnicGroupPrivateDeregulatedTTClaimant[i]));
            d = TotalCount_TTClaimant1[3] + TotalCount_TTClaimant1[6];
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(
                        sAllPercentageOfPrivateDeregulatedTT_EthnicGroupPrivateDeregulatedTTClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
        }
    }

    protected void addToSummarySingleTimeTTCounts(
            int nTT,
            HashMap<String, String> summary) {
        int all;
        all = TotalCount_TTClaimant1[1] + TotalCount_TTClaimant1[4];
        summary.put(
                sTotalCount_SocialTTsClaimant,
                Integer.toString(all));
        all = TotalCount_TTClaimant1[3] + TotalCount_TTClaimant1[6];
        summary.put(
                sTotalCount_PrivateDeregulatedTTsClaimant,
                Integer.toString(all));
        // TT
        for (int i = 1; i < nTT; i++) {
            all = TotalCount_TTClaimant1[i];
            summary.put(
                    sTotalCount_ClaimantTT[i],
                    Integer.toString(all));
        }
        summary.put(sAllTotalCount_PostcodeValidFormat,
                Integer.toString(HBTotalCount_PostcodeValidFormat + CTBTotalCount_PostcodeValidFormat));
        summary.put(sAllTotalCount_PostcodeValid,
                Integer.toString(HBTotalCount_PostcodeValid + CTBTotalCount_PostcodeValid));
        // HB
        summary.put(sHBCount1, Integer.toString(HBCount1));
        summary.put(sHBTotalCount_PostcodeValidFormat,
                Integer.toString(HBTotalCount_PostcodeValidFormat));
        summary.put(sHBTotalCount_PostcodeValid,
                Integer.toString(HBTotalCount_PostcodeValid));
        // CTB
        summary.put(sCTBCount1, Integer.toString(CTBCount1));
        summary.put(sCTBTotalCount_PostcodeValidFormat,
                Integer.toString(CTBTotalCount_PostcodeValidFormat));
        summary.put(sCTBTotalCount_PostcodeValid,
                Integer.toString(CTBTotalCount_PostcodeValid));
    }

    protected void addToSummarySingleTimeTTRates(
            int nTT,
            HashMap<String, String> summary) {
        // Ethnicity
        double percentage;
        double d;
        int all;
        all = Integer.valueOf(summary.get(sTotalCount_SocialTTsClaimant));
        d = AllCount1;
        if (d > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_SocialTTsClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_SocialTTsClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        all = Integer.valueOf(summary.get(sTotalCount_PrivateDeregulatedTTsClaimant));
        d = AllCount1;
        if (d > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_PrivateDeregulatedTTsClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
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
            all = Integer.valueOf(summary.get(sTotalCount_ClaimantTT[i]));
            d = AllCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(
                        sPercentageOfAll_ClaimantTT[i],
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
    }

    protected void doCompare2TimesCounts(
            DW_SHBE_D_Record D_Record0,
            DW_SHBE_D_Record D_Record1,
            String yM30v,
            String yM31v) {
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();
        boolean isHBClaim;
        isHBClaim = false;
        boolean isCTBOnlyClaim;
        isCTBOnlyClaim = false;
        if (D_Record1 != null) {
            doSingleTimeCount(
                    D_Record1,
                    yM30v);
            isHBClaim = tDW_SHBE_Handler.isHBClaim(D_Record1);
            isCTBOnlyClaim = tDW_SHBE_Handler.isCTBOnlyClaim(D_Record1);
        }
        AllCount1 = HBCount1 + CTBCount1;
        String postcode0;
        postcode0 = null;
        int TT0;
        TT0 = DW_SHBE_TenancyType_Handler.iMinus999;
        boolean isValidPostcode0;
        isValidPostcode0 = false;
        if (D_Record0 != null) {
            postcode0 = D_Record0.getClaimantsPostcode();
            if (postcode0 != null) {
                isValidPostcode0 = tDW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
            }
            TT0 = D_Record0.getTenancyType();
        }
        String postcode1;
        postcode1 = null;
        int TT1;
        TT1 = DW_SHBE_TenancyType_Handler.iMinus999;
        boolean isValidPostcode1;
        isValidPostcode1 = false;
        if (D_Record1 != null) {
            postcode1 = D_Record1.getClaimantsPostcode();
            if (postcode1 != null) {
                isValidPostcode1 = tDW_Postcode_Handler.isValidPostcode(yM31v, postcode1);
            }
            TT1 = D_Record1.getTenancyType();
        }
        if (isHBClaim || D_Record1 == null) {
            doCompare2TimesHBCount(
                    TT0,
                    postcode0,
                    isValidPostcode0,
                    TT1,
                    postcode1,
                    isValidPostcode1);
        }
        if (isCTBOnlyClaim || D_Record1 == null) {
            doCompare2TimesCTBCount(
                    TT0,
                    postcode0,
                    isValidPostcode0,
                    TT1,
                    postcode1,
                    isValidPostcode1);
        }
        AllTotalCount_TTChangeClaimant = HBTotalCount_TTChangeClaimant + CTBTotalCount_TTChangeClaimant;
        //AllTotalCount_TTChangeClaimantIgnoreMinus999 = HBTotalCount_TTChangeClaimantIgnoreMinus999 + CTBTotalCount_TTChangeClaimantIgnoreMinus999;
        AllTotalCount_Postcode0ValidPostcode1Valid = HBTotalCount_Postcode0ValidPostcode1Valid + CTBTotalCount_Postcode0ValidPostcode1Valid;
        AllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = HBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged + CTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged;
        AllTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged = HBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged + CTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged;
        AllTotalCount_Postcode0ValidPostcode1NotValid = HBTotalCount_Postcode0ValidPostcode1NotValid + CTBTotalCount_Postcode0ValidPostcode1NotValid;
        AllTotalCount_Postcode0NotValidPostcode1Valid = HBTotalCount_Postcode0NotValidPostcode1Valid + CTBTotalCount_Postcode0NotValidPostcode1Valid;
        AllTotalCount_Postcode0NotValidPostcode1NotValid = HBTotalCount_Postcode0NotValidPostcode1NotValid + CTBTotalCount_Postcode0NotValidPostcode1NotValid;
        AllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged + CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged;
        AllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged + CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged;
    }

    protected void doSingleTimeCount(
            DW_SHBE_D_Record D_Record,
            String yM30v) {
        int ClaimantsEthnicGroup0;
        //ClaimantsEthnicGroup0 = D_Record.getClaimantsEthnicGroup();
        ClaimantsEthnicGroup0 = tDW_SHBE_Handler.getEthnicityGroup(D_Record);
        int TT;
        String postcode;
        // All unfiltered counts
        TT = D_Record.getTenancyType();
        postcode = D_Record.getClaimantsPostcode();
        TotalCount_TTClaimant1[TT]++;
        // Disability
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
        // Passported Standard Indicator
        int PSI;
        PSI = D_Record.getPassportedStandardIndicator();
        AllTotalCount_PSI[PSI]++;
        AllTotalCount_PSIByTT[PSI][TT]++;
        // Household size
        long HouseholdSize;
        HouseholdSize = tDW_SHBE_Handler.getHouseholdSize(D_Record);
        AllTotalHouseholdSize += HouseholdSize;
        // Entitlements
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
        // Eligible Amounts
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
            AllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero++;
        } else {
            AllTotalCount_WeeklyEligibleCouncilTaxAmountZero++;
        }
        int ContractualRentAmount;
        ContractualRentAmount = D_Record.getContractualRentAmount();
        if (ContractualRentAmount > 0) {
            AllTotalContractualRentAmount += ContractualRentAmount;
            AllTotalContractualRentAmountNonZeroCount++;
        } else {
            AllTotalContractualRentAmountZeroCount++;
        }
        // Additional Payments
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
        // HBClaim only counts
        if (tDW_SHBE_Handler.isHBClaim(D_Record)) {
            HBTotalCount_PSI[PSI]++;
            HBTotalCount_PSIByTT[PSI][TT]++;
            //if (HBRef.equalsIgnoreCase(CTBRef)) {
            HBTotalHouseholdSize += HouseholdSize;
            AllTotalHouseholdSizeByTT[TT] += HouseholdSize;
            int ClaimantsNetWeeklyIncomeFromEmployment;
            ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
            if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
                HBTotalCount_EmployedClaimants++;
            }
            int ClaimantsNetWeeklyIncomeFromSelfEmployment;
            ClaimantsNetWeeklyIncomeFromSelfEmployment = D_Record.getClaimantsNetWeeklyIncomeFromSelfEmployment();
            if (ClaimantsNetWeeklyIncomeFromSelfEmployment > 0) {
                HBTotalCount_SelfEmployedClaimants++;
            }
            String ClaimantsStudentIndicator;
            ClaimantsStudentIndicator = D_Record.getClaimantsStudentIndicator();
            if (ClaimantsStudentIndicator != null) {
                if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                    HBTotalCount_StudentsClaimants++;
                }
            }
            String LHARegulationsApplied;
            LHARegulationsApplied = D_Record.getLHARegulationsApplied();
            if (LHARegulationsApplied != null) {
                if (LHARegulationsApplied.equalsIgnoreCase("1")
                        || LHARegulationsApplied.equalsIgnoreCase("Yes")) {
                    HBTotalCount_LHACases++;
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
                HBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero++;
            } else {
                HBTotalCount_WeeklyEligibleCouncilTaxAmountZero++;
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
                    TT,
                    postcode,
                    yM30v);
        }
        // CTB Claim only counts
        if (tDW_SHBE_Handler.isCTBOnlyClaim(D_Record)) {
            CTBTotalCount_PSI[PSI]++;
            CTBTotalCount_PSIByTT[PSI][TT]++;
            CTBTotalHouseholdSize += HouseholdSize;
            AllTotalHouseholdSizeByTT[TT] += HouseholdSize;
            int ClaimantsNetWeeklyIncomeFromEmployment;
            ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
            if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
                CTBTotalCount_EmployedClaimants++;
            }
            int ClaimantsNetWeeklyIncomeFromSelfEmployment;
            ClaimantsNetWeeklyIncomeFromSelfEmployment = D_Record.getClaimantsNetWeeklyIncomeFromSelfEmployment();
            if (ClaimantsNetWeeklyIncomeFromSelfEmployment > 0) {
                CTBTotalCount_SelfEmployedClaimants++;
            }
            String ClaimantsStudentIndicator;
            ClaimantsStudentIndicator = D_Record.getClaimantsStudentIndicator();
            if (ClaimantsStudentIndicator != null) {
                if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                    CTBTotalCount_StudentsClaimants++;
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
                CTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero++;
            } else {
                CTBTotalCount_WeeklyEligibleCouncilTaxAmountZero++;
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
                    TT,
                    postcode,
                    yM30v);
        }
//        AllCount1 = HBCount1 + CTBCount1;
    }

    protected void doCompare2TimesHBCount(
            Integer tenancyType0,
            String postcode0,
            boolean isValidPostcode0,
            Integer tenancyType1,
            String postcode1,
            boolean isValidPostcode1) {
        if (isValidPostcode0) {
            if (isValidPostcode1) {
                HBTotalCount_Postcode0ValidPostcode1Valid++;
                if (postcode0.equalsIgnoreCase(postcode1)) {
                    HBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged++;
                } else {
                    HBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged++;
                }
            } else {
                HBTotalCount_Postcode0ValidPostcode1NotValid++;
            }
        } else if (isValidPostcode1) {
            HBTotalCount_Postcode0NotValidPostcode1Valid++;
        } else {
            HBTotalCount_Postcode0NotValidPostcode1NotValid++;
            if (postcode0 == null) {
                HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
            } else if (postcode1 == null) {
                HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
            } else if (postcode0.equalsIgnoreCase(postcode1)) {
                HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
            } else {
                HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged++;
            }
        }
        if (tenancyType0.compareTo(tenancyType1) != 0) {
            if (!(tenancyType0 == DW_SHBE_TenancyType_Handler.iMinus999
                    || tenancyType1 == DW_SHBE_TenancyType_Handler.iMinus999)) {
                HBTotalCount_TTChangeClaimant++;
                //HBTotalCount_TTChangeClaimantIgnoreMinus999++;
            }
            if (tenancyType0 == DW_SHBE_TenancyType_Handler.iMinus999) {
                if (tenancyType1 == 1 || tenancyType1 == 4) {
                    TotalCount_Minus999TTToSocialTTs++;
                }
                if (tenancyType1 == 3 || tenancyType1 == 6) {
                    TotalCount_Minus999TTToPrivateDeregulatedTTs++;
                }
                if (tenancyType1 == 1
                        || tenancyType1 == 2
                        || tenancyType1 == 3
                        || tenancyType1 == 4
                        || tenancyType1 == 6
                        || tenancyType1 == 8
                        || tenancyType1 == 9) {
                    TotalCount_HBTTsToMinus999TT++;
                }
            }
            if (tenancyType1 == DW_SHBE_TenancyType_Handler.iMinus999) {
                if (tenancyType0 == 1 || tenancyType0 == 4) {
                    TotalCount_SocialTTsToMinus999TT++;
                }
                if (tenancyType0 == 3 || tenancyType0 == 6) {
                    TotalCount_PrivateDeregulatedTTsToMinus999TT++;
                }
            }
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
        if ((tenancyType0 == 3 && tenancyType1 == 3)
                || (tenancyType0 == 3 && tenancyType1 == 6)
                || (tenancyType0 == 6 && tenancyType1 == 3)
                || (tenancyType0 == 6 && tenancyType1 == 6)) {
            if (isValidPostcode0 && isValidPostcode1) {
                if (!postcode0.equalsIgnoreCase(postcode1)) {
                    TotalCount_PostcodeChangeWithinPrivateDeregulatedTTs++;
                }
            }
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
    }

    /**
     *
     * @param tEG The Ethnic Group
     * @param tP The Postcode
     * @param yM3v They yM3 for postcode lookup validity
     */
    protected void doSingleTimeHBCount(
            int tEG,
            int tTT,
            String tP,
            String yM3v) {
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();
        HBCount1++;
        HBEthnicGroupCount[tEG]++;
        AllTotalCount_EthnicGroupClaimantByTT[tEG][tTT]++;
        if (tP != null) {
            boolean isValidPostcodeFormat;
            isValidPostcodeFormat = Generic_UKPostcode_Handler.isValidPostcodeForm(tP);
            boolean isValidPostcode;
            isValidPostcode = tDW_Postcode_Handler.isValidPostcode(yM3v, tP);
            if (isValidPostcodeFormat) {
                HBTotalCount_PostcodeValidFormat++;
            }
            if (isValidPostcode) {
                HBTotalCount_PostcodeValid++;
            }
        }
    }

    protected void doCompare2TimesCTBCount(
            Integer tenancyType0,
            String postcode0,
            boolean isValidPostcode0,
            Integer tenancyType1,
            String postcode1,
            boolean isValidPostcode1) {
        if (isValidPostcode0) {
            if (isValidPostcode1) {
                CTBTotalCount_Postcode0ValidPostcode1Valid++;
                if (postcode0.equalsIgnoreCase(postcode1)) {
                    CTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged++;
                } else {
                    CTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged++;
                }
            } else {
                CTBTotalCount_Postcode0ValidPostcode1NotValid++;
            }
        } else if (isValidPostcode1) {
            CTBTotalCount_Postcode0NotValidPostcode1Valid++;
        } else {
            CTBTotalCount_Postcode0NotValidPostcode1NotValid++;
            if (postcode0 == null) {
                CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
            } else if (postcode1 == null) {
                CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
            } else if (postcode0.equalsIgnoreCase(postcode1)) {
                CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
            } else {
                CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged++;
            }
        }
        if (tenancyType0.compareTo(tenancyType1) != 0) {
            if (!(tenancyType0 == DW_SHBE_TenancyType_Handler.iMinus999
                    || tenancyType1 == DW_SHBE_TenancyType_Handler.iMinus999)) {
                CTBTotalCount_TTChangeClaimant++;
                //CTBTotalCount_TTChangeClaimantIgnoreMinus999++;
            }
            if (tenancyType1 == DW_SHBE_TenancyType_Handler.iMinus999) {
                if (tenancyType0 == 5 || tenancyType0 == 7) {
                    TotalCount_CTBTTsToMinus999TT++;
                }
            }
            if (tenancyType0 == DW_SHBE_TenancyType_Handler.iMinus999) {
                TotalCount_Minus999TTToCTBTTs++;
            }
            if ((tenancyType0 == 1
                    || tenancyType0 == 2
                    || tenancyType0 == 3
                    || tenancyType0 == 4
                    || tenancyType0 == 6
                    || tenancyType0 == 8
                    || tenancyType0 == 9)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                TotalCount_HBTTsToCTBTTs++;
            }
            if ((tenancyType0 == 1 || tenancyType0 == 4)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                TotalCount_SocialTTsToCTBTTs++;
            }
            if ((tenancyType0 == 1)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                TotalCount_TT1ToCTBTTs++;
            }
            if ((tenancyType0 == 4)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                TotalCount_TT4ToCTBTTs++;
            }
            if ((tenancyType0 == 3 || tenancyType0 == 6)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                TotalCount_PrivateDeregulatedTTsToCTBTTs++;
            }
        }
    }

    /**
     *
     * @param tEG The Ethnic Group
     * @param tTT
     * @param tP The Postcode
     * @param yM3v They yM3 for postcode lookup validity
     */
    protected void doSingleTimeCTBCount(
            int tEG,
            int tTT,
            String tP,
            String yM3v) {
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();
        CTBCount1++;
        CTBEthnicGroupCount[tEG]++;
        AllTotalCount_EthnicGroupClaimantByTT[tEG][tTT]++;
        if (tP != null) {
            boolean isValidPostcodeFormat;
            isValidPostcodeFormat = Generic_UKPostcode_Handler.isValidPostcodeForm(tP);
            boolean isValidPostcode;
            isValidPostcode = tDW_Postcode_Handler.isValidPostcode(yM3v, tP);
            if (isValidPostcodeFormat) {
                CTBTotalCount_PostcodeValidFormat++;
            }
            if (isValidPostcode) {
                CTBTotalCount_PostcodeValid++;
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
            key = tDW_SHBE_Handler.getYearMonthNumber(SHBEFilenames[i]);
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
        yM30 = tDW_SHBE_Handler.getYM3(filename0);
        System.out.println("Load " + filename0);
        DW_SHBE_Collection tSHBEData0;
        tSHBEData0 = new DW_SHBE_Collection(env, filename0, paymentType);
        // These could be returned to save time recreating them for other includes.
        // This would involve feeding them in to the method too per se.
        String yM30v;
        yM30v = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM30);
        String key;
        key = tDW_SHBE_Handler.getYearMonthNumber(SHBEFilenames[i]);
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
        HashMap<DW_intID, String> tIDByPostcode0;
        tIDByPostcode0 = tSHBEData0.getClaimantIDToPostcodeLookup();
        //tIDByPostcode0 = loadIDByPostcode(loadData, filename, i);
        HashMap<DW_intID, Integer> tIDByTT0;
        tIDByTT0 = tSHBEData0.getClaimantIDToTenancyTypeLookup();
        //tIDByTT0 = loadIDByTT(loadData, filename, i);
        HashMap<String, DW_intID> CTBRefToIDLookup0;
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
                    yM30v);
        }
//        AllCount1 = HBCount1 + CTBCount1;
        summary.put(sSHBEFilename1, filename0); // This looks odd but is right!
        HashMap<String, BigDecimal> incomeAndRentSummary;
        incomeAndRentSummary = tDW_SHBE_Handler.getIncomeAndRentSummary(
                tSHBEData0,
                paymentType,
                filename0,
                null,
                null,
                false,
                false,
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
            yM31 = tDW_SHBE_Handler.getYM3(filename1);
            String yM31v;
            yM31v = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
            // Load next data
            System.out.println("Load " + filename1);
            DW_SHBE_Collection tSHBEData1;
            tSHBEData1 = new DW_SHBE_Collection(env,
                    filename1, paymentType);
            TreeMap<String, DW_SHBE_Record> tDRecords1;
            tDRecords1 = tSHBEData1.getRecords();
            HashSet<DW_PersonID> tClaimantIDs1;
            tClaimantIDs1 = tSHBEData1.getClaimantIDs();
            tClaimantIDs.put(yM31, tClaimantIDs1);

            HashSet<DW_PersonID> AllIDs1;
            AllIDs1 = tSHBEData1.getAllIDs();
            AllIDs.put(yM31, AllIDs1);
            HashMap<DW_intID, String> tIDByPostcode1;
            tIDByPostcode1 = tSHBEData1.getClaimantIDToPostcodeLookup();
            HashMap<DW_intID, Integer> tIDByTT1;
            tIDByTT1 = tSHBEData1.getClaimantIDToTenancyTypeLookup();
            HashMap<String, DW_intID> CTBRefToIDLookup1;
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

            key = tDW_SHBE_Handler.getYearMonthNumber(filename1);
            summary = result.get(key);
            // Counters
            tLoadSummary = (HashMap<String, Integer>) tSHBEData1.getLoadSummary();
            addToSummary(summary, tLoadSummary);
            HashMap<String, BigDecimal> incomeAndRentSummary1;
            incomeAndRentSummary1 = tDW_SHBE_Handler.getIncomeAndRentSummary(
                    tSHBEData1,
                    paymentType,
                    filename1,
                    null,
                    null,
                    false,
                    false,
                    false,
                    forceNewSummaries);
            addToSummarySingleTimeIncomeAndRent(
                    summary,
                    incomeAndRentSummary1);
            initCounts(nTT, nEG, nPSI);

            // Loop over CTBRefToIDLookup0
            ite = CTBRefToIDLookup0.keySet().iterator();
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
                DW_SHBE_Record Record1;
                Record1 = tDRecords1.get(CTBRef);
                if (Record1 == null) {
                    doCompare2TimesCounts(
                            D_Record0,
                            null,
                            yM30v,
                            yM31v);
                }
            }

            // Loop over CTBRefToIDLookup1
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
        summary.put(sSamePostcodeIII,
                "" + counts[0]);
        summary.put(sSamePostcodeIOI,
                "" + counts[1]);
        summary.put(sSamePostcodeOIO,
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
            summary.put(sSameTenancyIII,
                    null);
            summary.put(sSameTenancyIOI,
                    null);
            summary.put(sSameTenancyOIO,
                    null);
            for (int TT = 1; TT < nTT; TT++) {
                summary.put(sSameTenancyIIITT[TT],
                        null);
                summary.put(sSameTenancyIOITT[TT],
                        null);
                summary.put(sSameTenancyOIOTT[TT],
                        null);
            }
        } else {
            summary.put(sSameTenancyIII,
                    "" + mainCounts[0]);
            summary.put(sSameTenancyIOI,
                    "" + mainCounts[1]);
            summary.put(sSameTenancyOIO,
                    "" + mainCounts[2]);
            for (int TT = 1; TT < nTT; TT++) {
                summary.put(sSameTenancyIIITT[TT],
                        "" + IIITTCounts[TT]);
                summary.put(sSameTenancyIOITT[TT],
                        "" + IOITTCounts[TT]);
                summary.put(sSameTenancyOIOTT[TT],
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
            summary.put(sSameTenancyAndPostcodeIII,
                    null);
            summary.put(sSameTenancyAndPostcodeIOI,
                    null);
            summary.put(sSameTenancyAndPostcodeOIO,
                    null);
            for (int TT = 1; TT < nTT; TT++) {
                summary.put(sSameTenancyAndPostcodeIIITT[TT],
                        null);
                summary.put(sSameTenancyAndPostcodeIOITT[TT],
                        null);
                summary.put(sSameTenancyAndPostcodeOIOTT[TT],
                        null);
            }
        } else {
            summary.put(sSameTenancyAndPostcodeIII,
                    "" + mainCounts[0]);
            summary.put(sSameTenancyAndPostcodeIOI,
                    "" + mainCounts[1]);
            summary.put(sSameTenancyAndPostcodeOIO,
                    "" + mainCounts[2]);
            for (int TT = 1; TT < nTT; TT++) {
                summary.put(sSameTenancyAndPostcodeIIITT[TT],
                        "" + IIITTCounts[TT]);
                summary.put(sSameTenancyAndPostcodeIOITT[TT],
                        "" + IOITTCounts[TT]);
                summary.put(sSameTenancyAndPostcodeOIOTT[TT],
                        "" + OIOTTCounts[TT]);
            }
        }
    }

    protected void addToSummarySingleTimeIncomeAndRent(
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

    /**
     *
     * @param name
     * @param name2
     * @param summaryTable
     * @param paymentType
     * @param includeKey
     * @param underOccupancy
     * @return
     */
    protected PrintWriter getPrintWriter(
            String name,
            String name2,
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy) {
        PrintWriter result;
        File dirOut;
        dirOut = env.getDW_Files().getTableDir(
                name2,
                paymentType,
                includeKey,
                underOccupancy);
        String outFilename;
        outFilename = paymentType + tDW_Strings.sUnderscore + includeKey + tDW_Strings.sUnderscore;
        if (underOccupancy) {
            outFilename += tDW_Strings.sU + tDW_Strings.sUnderscore;
        }
        outFilename += summaryTable.firstKey() + "_To_" + summaryTable.lastKey() + tDW_Strings.sUnderscore + name + ".csv";
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
        ONSPDFiles = env.getDW_Postcode_Handler().getONSPDFiles();
        String name;
        name = "Compare3Times";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename00 + sCommaSpace;
        header += sSHBEFilename0 + sCommaSpace;
        header += sSHBEFilename1 + sCommaSpace;
        header += "PostCodeLookupDate00, PostCodeLookupFile00, "
                + "PostCodeLookupDate0, PostCodeLookupFile0, "
                + "PostCodeLookupDate1, PostCodeLookupFile1, ";
        header += sSamePostcodeIII + sCommaSpace;
        header += sSamePostcodeIOI + sCommaSpace;
        header += sSamePostcodeOIO + sCommaSpace;
        header += sSameTenancyIII + sCommaSpace;
        header += sSameTenancyIOI + sCommaSpace;
        header += sSameTenancyOIO + sCommaSpace;
        for (int i = 1; i < nTT; i++) {
            header += sSameTenancyIIITT[i] + sCommaSpace;
            header += sSameTenancyIOITT[i] + sCommaSpace;
            header += sSameTenancyOIOTT[i] + sCommaSpace;
        }
        header += sSameTenancyAndPostcodeIII + sCommaSpace;
        header += sSameTenancyAndPostcodeIOI + sCommaSpace;
        header += sSameTenancyAndPostcodeOIO + sCommaSpace;
        for (int i = 1; i < nTT; i++) {
            header += sSameTenancyAndPostcodeIIITT[i] + sCommaSpace;
            header += sSameTenancyAndPostcodeIOITT[i] + sCommaSpace;
            header += sSameTenancyAndPostcodeOIOTT[i] + sCommaSpace;
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
            //if (filename1 != null) {
            line += filename00 + "," + filename0 + sCommaSpace + filename1 + sCommaSpace;
            String PostCodeLookupDate00 = null;
            String PostCodeLookupFile00Name = null;
            if (filename00 != null) {
                PostCodeLookupDate00 = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                        tDW_SHBE_Handler.getYM3(filename00));
                PostCodeLookupFile00Name = ONSPDFiles.get(PostCodeLookupDate00).getName();
            }
            line += PostCodeLookupDate00 + sCommaSpace + PostCodeLookupFile00Name + sCommaSpace;
            String PostCodeLookupDate0 = null;
            String PostCodeLookupFile0Name = null;
            if (filename0 != null) {
                PostCodeLookupDate0 = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                        tDW_SHBE_Handler.getYM3(filename0));
                PostCodeLookupFile0Name = ONSPDFiles.get(PostCodeLookupDate0).getName();
            }
            line += PostCodeLookupDate0 + sCommaSpace + PostCodeLookupFile0Name + sCommaSpace;
            String PostCodeLookupDate1 = null;
            String PostCodeLookupFile1Name = null;
            if (filename1 != null) {
                PostCodeLookupDate1 = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                        tDW_SHBE_Handler.getYM3(filename1));
                PostCodeLookupFile1Name = ONSPDFiles.get(PostCodeLookupDate1).getName();
            }
            line += PostCodeLookupDate1 + sCommaSpace + PostCodeLookupFile1Name + sCommaSpace;
            line += summary.get(sSamePostcodeIII) + sCommaSpace;
            line += summary.get(sSamePostcodeIOI) + sCommaSpace;
            line += summary.get(sSamePostcodeOIO) + sCommaSpace;
            line += summary.get(sSameTenancyIII) + sCommaSpace;
            line += summary.get(sSameTenancyIOI) + sCommaSpace;
            line += summary.get(sSameTenancyOIO) + sCommaSpace;
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sSameTenancyIIITT[i]) + sCommaSpace;
                line += summary.get(sSameTenancyIOITT[i]) + sCommaSpace;
                line += summary.get(sSameTenancyOIOTT[i]) + sCommaSpace;
            }
            line += summary.get(sSameTenancyAndPostcodeIII) + sCommaSpace;
            line += summary.get(sSameTenancyAndPostcodeIOI) + sCommaSpace;
            line += summary.get(sSameTenancyAndPostcodeOIO) + sCommaSpace;
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sSameTenancyAndPostcodeIIITT[i]) + sCommaSpace;
                line += summary.get(sSameTenancyAndPostcodeIOITT[i]) + sCommaSpace;
                line += summary.get(sSameTenancyAndPostcodeOIOTT[i]) + sCommaSpace;
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
            //}
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
        ONSPDFiles = env.getDW_Postcode_Handler().getONSPDFiles();
        String name;
        name = "Compare2Times";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = getHeaderCompare2TimesGeneric();
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
                    ONSPDFiles);
            line += getLineCompare2TimesTTChange(summary);
            line += getLineCompare2TimesPostcodeChange(summary);
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    protected String getHeaderCompare2TimesPostcodeChange() {
        String header = "";
        // All Postcode Related
        header += sAllTotalCount_Postcode0ValidPostcode1Valid + sCommaSpace;
        header += sAllPercentagePostcode0ValidPostcode1Valid + sCommaSpace;
        header += sAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged + sCommaSpace;
        header += sAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + sCommaSpace;
        header += sAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChange + sCommaSpace;
        header += sAllPercentagePostcode0ValidPostcode1ValidPostcodeChange + sCommaSpace;
        header += sAllTotalCount_Postcode0ValidPostcode1NotValid + sCommaSpace;
        header += sAllPercentagePostcode0ValidPostcode1NotValid + sCommaSpace;
        header += sAllTotalCount_Postcode0NotValidPostcode1Valid + sCommaSpace;
        header += sAllPercentagePostcode0NotValidPostcode1Valid + sCommaSpace;
        header += sAllTotalCount_Postcode0NotValidPostcode1NotValid + sCommaSpace;
        header += sAllPercentagePostcode0NotValidPostcode1NotValid + sCommaSpace;
        header += sAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged + sCommaSpace;
        header += sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + sCommaSpace;
        header += sAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged + sCommaSpace;
        header += sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + sCommaSpace;
        // HB Postcode Related
        header += sHBTotalCount_Postcode0ValidPostcode1Valid + sCommaSpace;
        header += sHBPercentagePostcode0ValidPostcode1Valid + sCommaSpace;
        header += sHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged + sCommaSpace;
        header += sHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + sCommaSpace;
        header += sHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChange + sCommaSpace;
        header += sHBPercentagePostcode0ValidPostcode1ValidPostcodeChange + sCommaSpace;
        header += sHBTotalCount_Postcode0ValidPostcode1NotValid + sCommaSpace;
        header += sHBPercentagePostcode0ValidPostcode1NotValid + sCommaSpace;
        header += sHBTotalCount_Postcode0NotValidPostcode1Valid + sCommaSpace;
        header += sHBPercentagePostcode0NotValidPostcode1Valid + sCommaSpace;
        header += sHBTotalCount_Postcode0NotValidPostcode1NotValid + sCommaSpace;
        header += sHBPercentagePostcode0NotValidPostcode1NotValid + sCommaSpace;
        header += sHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged + sCommaSpace;
        header += sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + sCommaSpace;
        header += sHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged + sCommaSpace;
        header += sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + sCommaSpace;
        // CTB Postcode Related
        header += sCTBTotalCount_Postcode0ValidPostcode1Valid + sCommaSpace;
        header += sCTBPercentageOfCTB_Postcode0ValidPostcode1Valid + sCommaSpace;
        header += sCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged + sCommaSpace;
        header += sCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + sCommaSpace;
        header += sCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged + sCommaSpace;
        header += sCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged + sCommaSpace;
        header += sCTBTotalCount_Postcode0ValidPostcode1NotValid + sCommaSpace;
        header += sCTBPercentagePostcode0ValidPostcode1NotValid + sCommaSpace;
        header += sCTBTotalCount_Postcode0NotValidPostcode1Valid + sCommaSpace;
        header += sCTBPercentagePostcode0NotValidPostcode1Valid + sCommaSpace;
        header += sCTBTotalCount_Postcode0NotValidPostcode1NotValid + sCommaSpace;
        header += sCTBPercentagePostcode0NotValidPostcode1NotValid + sCommaSpace;
        header += sCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged + sCommaSpace;
        header += sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + sCommaSpace;
        header += sCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged + sCommaSpace;
        header += sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + sCommaSpace;
        return header;
    }

    protected String getLineCompare2TimesPostcodeChange(HashMap<String, String> summary) {
        String line = "";
        // All
        line += summary.get(sAllTotalCount_Postcode0ValidPostcode1Valid) + sCommaSpace;
        line += summary.get(sAllPercentagePostcode0ValidPostcode1Valid) + sCommaSpace;
        line += summary.get(sAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged) + sCommaSpace;
        line += summary.get(sAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + sCommaSpace;
        line += summary.get(sAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChange) + sCommaSpace;
        line += summary.get(sAllPercentagePostcode0ValidPostcode1ValidPostcodeChange) + sCommaSpace;
        line += summary.get(sAllTotalCount_Postcode0ValidPostcode1NotValid) + sCommaSpace;
        line += summary.get(sAllPercentagePostcode0ValidPostcode1NotValid) + sCommaSpace;
        line += summary.get(sAllTotalCount_Postcode0NotValidPostcode1Valid) + sCommaSpace;
        line += summary.get(sAllPercentagePostcode0NotValidPostcode1Valid) + sCommaSpace;
        line += summary.get(sAllTotalCount_Postcode0NotValidPostcode1NotValid) + sCommaSpace;
        line += summary.get(sAllPercentagePostcode0NotValidPostcode1NotValid) + sCommaSpace;
        line += summary.get(sAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged) + sCommaSpace;
        line += summary.get(sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + sCommaSpace;
        line += summary.get(sAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged) + sCommaSpace;
        line += summary.get(sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + sCommaSpace;
        // HB
        line += summary.get(sHBTotalCount_Postcode0ValidPostcode1Valid) + sCommaSpace;
        line += summary.get(sHBPercentagePostcode0ValidPostcode1Valid) + sCommaSpace;
        line += summary.get(sHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged) + sCommaSpace;
        line += summary.get(sHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + sCommaSpace;
        line += summary.get(sHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChange) + sCommaSpace;
        line += summary.get(sHBPercentagePostcode0ValidPostcode1ValidPostcodeChange) + sCommaSpace;
        line += summary.get(sHBTotalCount_Postcode0ValidPostcode1NotValid) + sCommaSpace;
        line += summary.get(sHBPercentagePostcode0ValidPostcode1NotValid) + sCommaSpace;
        line += summary.get(sHBTotalCount_Postcode0NotValidPostcode1Valid) + sCommaSpace;
        line += summary.get(sHBPercentagePostcode0NotValidPostcode1Valid) + sCommaSpace;
        line += summary.get(sHBTotalCount_Postcode0NotValidPostcode1NotValid) + sCommaSpace;
        line += summary.get(sHBPercentagePostcode0NotValidPostcode1NotValid) + sCommaSpace;
        line += summary.get(sHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged) + sCommaSpace;
        line += summary.get(sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + sCommaSpace;
        line += summary.get(sHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged) + sCommaSpace;
        line += summary.get(sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + sCommaSpace;
        // CTB
        line += summary.get(sCTBTotalCount_Postcode0ValidPostcode1Valid) + sCommaSpace;
        line += summary.get(sCTBPercentageOfCTB_Postcode0ValidPostcode1Valid) + sCommaSpace;
        line += summary.get(sCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged) + sCommaSpace;
        line += summary.get(sCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + sCommaSpace;
        line += summary.get(sCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged) + sCommaSpace;
        line += summary.get(sCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged) + sCommaSpace;
        line += summary.get(sCTBTotalCount_Postcode0ValidPostcode1NotValid) + sCommaSpace;
        line += summary.get(sCTBPercentagePostcode0ValidPostcode1NotValid) + sCommaSpace;
        line += summary.get(sCTBTotalCount_Postcode0NotValidPostcode1Valid) + sCommaSpace;
        line += summary.get(sCTBPercentagePostcode0NotValidPostcode1Valid) + sCommaSpace;
        line += summary.get(sCTBTotalCount_Postcode0NotValidPostcode1NotValid) + sCommaSpace;
        line += summary.get(sCTBPercentagePostcode0NotValidPostcode1NotValid) + sCommaSpace;
        line += summary.get(sCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged) + sCommaSpace;
        line += summary.get(sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + sCommaSpace;
        line += summary.get(sCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged) + sCommaSpace;
        line += summary.get(sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + sCommaSpace;
        return line;
    }

    public String getHeaderCompare2TimesTTChange() {
        String header = "";
        // General
        // All
        header += sAllTotalCount_TTChangeClaimant + sCommaSpace;
        header += sAllPercentageOfAll_TTChangeClaimant + sCommaSpace;
//        header += sAllTotalCount_TTChangeClaimantIgnoreMinus999 + sCommaSpace;
//        header += sAllPercentageOfAll_TTChangeClaimantIgnoreMinus999 + sCommaSpace;
        // General HB related
        header += sHBTotalCount_TTChangeClaimant + sCommaSpace;
        header += sHBPercentageOfHB_TTChangeClaimant + sCommaSpace;
//        header += sHBTotalCount_TTChangeClaimantIgnoreMinus999 + sCommaSpace;
//        header += sHBPercentageOfHB_TTChangeClaimantIgnoreMinus999 + sCommaSpace;

        header += sTotalCount_Minus999TTToSocialTTs + sCommaSpace;
        header += sTotalCount_Minus999TTToPrivateDeregulatedTTs + sCommaSpace;
        header += sTotalCount_HBTTsToMinus999TT + sCommaSpace;
        header += sPercentageOfHB_HBTTsToMinus999TT + sCommaSpace;
        header += sTotalCount_SocialTTsToMinus999TT + sCommaSpace;
        header += sPercentageOfSocialTTs_SocialTTsToMinus999TT + sCommaSpace;
        header += sTotalCount_PrivateDeregulatedTTsToMinus999TT + sCommaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT + sCommaSpace;

        header += sTotalCount_HBTTsToHBTTs + sCommaSpace;
        header += sPercentageOfHB_HBTTsToHBTTs + sCommaSpace;
        header += sTotalCount_SocialTTsToPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs + sCommaSpace;
        header += sTotalCount_PrivateDeregulatedTTsToSocialTTs + sCommaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs + sCommaSpace;
        header += sTotalCount_TT1ToPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfTT1_TT1ToPrivateDeregulatedTTs + sCommaSpace;
        header += sTotalCount_TT4ToPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfTT4_TT4ToPrivateDeregulatedTTs + sCommaSpace;
        header += sTotalCount_PrivateDeregulatedTTsToTT1 + sCommaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1 + sCommaSpace;
        header += sTotalCount_PrivateDeregulatedTTsToTT4 + sCommaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4 + sCommaSpace;
        header += sTotalCount_TT1ToTT4 + sCommaSpace;
        header += sPercentageOfTT1_TT1ToTT4 + sCommaSpace;
        header += sTotalCount_TT4ToTT1 + sCommaSpace;
        header += sPercentageOfTT4_TT4ToTT1 + sCommaSpace;
        header += sTotalCount_PostcodeChangeWithinSocialTTs + sCommaSpace;
        header += sPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs + sCommaSpace;
        header += sTotalCount_PostcodeChangeWithinTT1 + sCommaSpace;
        header += sPercentageOfTT1_PostcodeChangeWithinTT1 + sCommaSpace;
        header += sTotalCount_PostcodeChangeWithinTT4 + sCommaSpace;
        header += sPercentageOfTT4_PostcodeChangeWithinTT4 + sCommaSpace;
        header += sTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs + sCommaSpace;

        header += sTotalCount_HBTTsToCTBTTs + sCommaSpace;
        header += sPercentageOfHB_HBTTsToCTBTTs + sCommaSpace;
        // General CTB related
        header += sCTBTotalCount_TTChangeClaimant + sCommaSpace;
        header += sCTBPercentageOfCTB_TTChangeClaimant + sCommaSpace;
//        header += sCTBTotalCount_TTChangeClaimantIgnoreMinus999 + sCommaSpace;
//        header += sCTBPercentageOfCTB_TTChangeClaimantIgnoreMinus999 + sCommaSpace;

        header += sTotalCount_Minus999TTToCTBTTs + sCommaSpace;
        header += sTotalCount_CTBTTsToMinus999TT + sCommaSpace;
        header += sPercentageOfCTB_CTBTTsToMinus999TT + sCommaSpace;

        header += sTotalCount_SocialTTsToCTBTTs + sCommaSpace;
        header += sPercentageOfSocialTTs_SocialTTsToCTBTTs + sCommaSpace;
        header += sTotalCount_TT1ToCTBTTs + sCommaSpace;
        header += sPercentageOfTT1_TT1ToCTBTTs + sCommaSpace;
        header += sTotalCount_TT4ToCTBTTs + sCommaSpace;
        header += sPercentageOfTT4_TT4ToCTBTTs + sCommaSpace;
        header += sTotalCount_PrivateDeregulatedTTsToCTBTTs + sCommaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs + sCommaSpace;
        header += sTotalCount_CTBTTsToSocialTTs + sCommaSpace;
        header += sPercentageOfCTB_CTBTTsToSocialTTs + sCommaSpace;
        header += sTotalCount_CTBTTsToTT1 + sCommaSpace;
        header += sPercentageOfCTB_CTBTTsToTT1 + sCommaSpace;
        header += sTotalCount_CTBTTsToTT4 + sCommaSpace;
        header += sPercentageOfCTB_CTBTTsToTT4 + sCommaSpace;
        header += sTotalCount_CTBTTsToPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfCTB_CTBTTsToPrivateDeregulatedTTs + sCommaSpace;
        header += sTotalCount_CTBTTsToHBTTs + sCommaSpace;
        header += sPercentageOfCTB_CTBTTsToHBTTs + sCommaSpace;
        return header;
    }

    protected String getLineCompare2TimesTTChange(HashMap<String, String> summary) {
        String line = "";
        // All
        line += summary.get(sAllTotalCount_TTChangeClaimant) + sCommaSpace;
        line += summary.get(sAllPercentageOfAll_TTChangeClaimant) + sCommaSpace;
//        line += summary.get(sAllTotalCount_TTChangeClaimantIgnoreMinus999) + sCommaSpace;
//        line += summary.get(sAllPercentageOfAll_TTChangeClaimantIgnoreMinus999) + sCommaSpace;
        // General HB related
        line += summary.get(sHBTotalCount_TTChangeClaimant) + sCommaSpace;
        line += summary.get(sHBPercentageOfHB_TTChangeClaimant) + sCommaSpace;
//        line += summary.get(sHBTotalCount_TTChangeClaimantIgnoreMinus999) + sCommaSpace;
//        line += summary.get(sHBPercentageOfHB_TTChangeClaimantIgnoreMinus999) + sCommaSpace;

        line += summary.get(sTotalCount_Minus999TTToSocialTTs) + sCommaSpace;
        line += summary.get(sTotalCount_Minus999TTToPrivateDeregulatedTTs) + sCommaSpace;
        line += summary.get(sTotalCount_HBTTsToMinus999TT) + sCommaSpace;
        line += summary.get(sPercentageOfHB_HBTTsToMinus999TT) + sCommaSpace;
        line += summary.get(sTotalCount_SocialTTsToMinus999TT) + sCommaSpace;
        line += summary.get(sPercentageOfSocialTTs_SocialTTsToMinus999TT) + sCommaSpace;
        line += summary.get(sTotalCount_PrivateDeregulatedTTsToMinus999TT) + sCommaSpace;
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT) + sCommaSpace;

        line += summary.get(sTotalCount_HBTTsToHBTTs) + sCommaSpace;
        line += summary.get(sPercentageOfHB_HBTTsToHBTTs) + sCommaSpace;
        line += summary.get(sTotalCount_SocialTTsToPrivateDeregulatedTTs) + sCommaSpace;
        line += summary.get(sPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs) + sCommaSpace;
        line += summary.get(sTotalCount_PrivateDeregulatedTTsToSocialTTs) + sCommaSpace;
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs) + sCommaSpace;
        line += summary.get(sTotalCount_TT1ToPrivateDeregulatedTTs) + sCommaSpace;
        line += summary.get(sPercentageOfTT1_TT1ToPrivateDeregulatedTTs) + sCommaSpace;
        line += summary.get(sTotalCount_TT4ToPrivateDeregulatedTTs) + sCommaSpace;
        line += summary.get(sPercentageOfTT4_TT4ToPrivateDeregulatedTTs) + sCommaSpace;
        line += summary.get(sTotalCount_PrivateDeregulatedTTsToTT1) + sCommaSpace;
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1) + sCommaSpace;
        line += summary.get(sTotalCount_PrivateDeregulatedTTsToTT4) + sCommaSpace;
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4) + sCommaSpace;
        line += summary.get(sTotalCount_TT1ToTT4) + sCommaSpace;
        line += summary.get(sPercentageOfTT1_TT1ToTT4) + sCommaSpace;
        line += summary.get(sTotalCount_TT4ToTT1) + sCommaSpace;
        line += summary.get(sPercentageOfTT4_TT4ToTT1) + sCommaSpace;
        line += summary.get(sTotalCount_PostcodeChangeWithinSocialTTs) + sCommaSpace;
        line += summary.get(sPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs) + sCommaSpace;
        line += summary.get(sTotalCount_PostcodeChangeWithinTT1) + sCommaSpace;
        line += summary.get(sPercentageOfTT1_PostcodeChangeWithinTT1) + sCommaSpace;
        line += summary.get(sTotalCount_PostcodeChangeWithinTT4) + sCommaSpace;
        line += summary.get(sPercentageOfTT4_PostcodeChangeWithinTT4) + sCommaSpace;
        line += summary.get(sTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs) + sCommaSpace;
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs) + sCommaSpace;

        line += summary.get(sTotalCount_HBTTsToCTBTTs) + sCommaSpace;
        line += summary.get(sPercentageOfHB_HBTTsToCTBTTs) + sCommaSpace;
        // General CTB related
        line += summary.get(sCTBTotalCount_TTChangeClaimant) + sCommaSpace;
        line += summary.get(sCTBPercentageOfCTB_TTChangeClaimant) + sCommaSpace;
//        line += summary.get(sCTBTotalCount_TTChangeClaimantIgnoreMinus999) + sCommaSpace;
//        line += summary.get(sCTBPercentageOfCTB_TTChangeClaimantIgnoreMinus999) + sCommaSpace;

        line += summary.get(sTotalCount_Minus999TTToCTBTTs) + sCommaSpace;
        line += summary.get(sTotalCount_CTBTTsToMinus999TT) + sCommaSpace;
        line += summary.get(sPercentageOfCTB_CTBTTsToMinus999TT) + sCommaSpace;

        line += summary.get(sTotalCount_SocialTTsToCTBTTs) + sCommaSpace;
        line += summary.get(sPercentageOfSocialTTs_SocialTTsToCTBTTs) + sCommaSpace;
        line += summary.get(sTotalCount_TT1ToCTBTTs) + sCommaSpace;
        line += summary.get(sPercentageOfTT1_TT1ToCTBTTs) + sCommaSpace;
        line += summary.get(sTotalCount_TT4ToCTBTTs) + sCommaSpace;
        line += summary.get(sPercentageOfTT4_TT4ToCTBTTs) + sCommaSpace;
        line += summary.get(sTotalCount_PrivateDeregulatedTTsToCTBTTs) + sCommaSpace;
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs) + sCommaSpace;
        line += summary.get(sTotalCount_CTBTTsToSocialTTs) + sCommaSpace;
        line += summary.get(sPercentageOfCTB_CTBTTsToSocialTTs) + sCommaSpace;
        line += summary.get(sTotalCount_CTBTTsToTT1) + sCommaSpace;
        line += summary.get(sPercentageOfCTB_CTBTTsToTT1) + sCommaSpace;
        line += summary.get(sTotalCount_CTBTTsToTT4) + sCommaSpace;
        line += summary.get(sPercentageOfCTB_CTBTTsToTT4) + sCommaSpace;
        line += summary.get(sTotalCount_CTBTTsToPrivateDeregulatedTTs) + sCommaSpace;
        line += summary.get(sPercentageOfCTB_CTBTTsToPrivateDeregulatedTTs) + sCommaSpace;
        line += summary.get(sTotalCount_CTBTTsToHBTTs) + sCommaSpace;
        line += summary.get(sPercentageOfCTB_CTBTTsToHBTTs) + sCommaSpace;
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
        ONSPDFiles = env.getDW_Postcode_Handler().getONSPDFiles();
        String name;
        name = "Compare2TimesTT";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = getHeaderCompare2TimesGeneric();
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
                    ONSPDFiles);
            // General
            // All
            line += getLineCompare2TimesTTChange(summary);
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public String getHeaderCompare2TimesGeneric() {
        String header;
        header = "";
        header += sSHBEFilename0 + sCommaSpace;
        header += "Year0-Month0, ";
        header += "Month0 Year0, ";
        header += sSHBEFilename1 + sCommaSpace;
        header += "Year1-Month1, ";
        header += "Month1 Year1, ";
        header += "PostCodeLookupDate0, PostCodeLookupFile0, "
                + "PostCodeLookupDate1, PostCodeLookupFile1, ";
        header += sAllCount0 + sCommaSpace;
        header += sHBCount0 + sCommaSpace;
        header += sCTBCount0 + sCommaSpace;
        header += sAllCount1 + sCommaSpace;
        header += sHBCount1 + sCommaSpace;
        header += sCTBCount1 + sCommaSpace;
        header += "Month0 Year0 to Month1 Year1, ";
        return header;
    }

    public String getLineCompare2TimesGeneric(
            HashMap<String, String> summary,
            TreeMap<String, File> ONSPDFiles) {
        String line = "";
        String filename0;
        filename0 = summary.get(sSHBEFilename0);
        line += filename0 + sCommaSpace;
        String month0;
        String year0;
        if (filename0 != null) {
            line += tDW_SHBE_Handler.getYearMonthNumber(filename0) + sCommaSpace;
            month0 = tDW_SHBE_Handler.getMonth3(filename0);
            year0 = tDW_SHBE_Handler.getYear(filename0);
            line += month0 + " " + year0 + sCommaSpace;
        } else {
            month0 = "null";
            year0 = "null";
            line += "null, ";
            line += "null, ";
        }
        String filename1;
        filename1 = summary.get(sSHBEFilename1);
        line += filename1 + sCommaSpace;
        String month1;
        String year1;
        if (filename1 != null) {
            line += tDW_SHBE_Handler.getYearMonthNumber(filename1) + sCommaSpace;
            month1 = tDW_SHBE_Handler.getMonth3(filename1);
            year1 = tDW_SHBE_Handler.getYear(filename1);
            line += month1 + " " + year1 + sCommaSpace;
        } else {
            month1 = "null";
            year1 = "null";
            line += "null, ";
            line += "null, ";
        }
        String PostCodeLookupDate0 = null;
        String PostCodeLookupFile0Name = null;
        if (filename0 != null) {
            PostCodeLookupDate0 = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                    tDW_SHBE_Handler.getYM3(filename0));
            PostCodeLookupFile0Name = ONSPDFiles.get(PostCodeLookupDate0).getName();
        }
        line += PostCodeLookupDate0 + sCommaSpace + PostCodeLookupFile0Name + sCommaSpace;
        String PostCodeLookupDate1 = null;
        String PostCodeLookupFile1Name = null;
        if (filename1 != null) {
            PostCodeLookupDate1 = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                    tDW_SHBE_Handler.getYM3(filename1));
            PostCodeLookupFile1Name = ONSPDFiles.get(PostCodeLookupDate1).getName();
        }
        line += PostCodeLookupDate1 + sCommaSpace + PostCodeLookupFile1Name + sCommaSpace;
        line += summary.get(sAllCount0) + sCommaSpace;
        line += summary.get(sHBCount0) + sCommaSpace;
        line += summary.get(sCTBCount0) + sCommaSpace;
        line += summary.get(sAllCount1) + sCommaSpace;
        line += summary.get(sHBCount1) + sCommaSpace;
        line += summary.get(sCTBCount1) + sCommaSpace;
        line += month0 + " " + year0 + " to " + month1 + " " + year1 + sCommaSpace;
        //}
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
        ONSPDFiles = env.getDW_Postcode_Handler().getONSPDFiles();
        String name;
        name = "Compare2TimesPostcode";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = getHeaderCompare2TimesGeneric();
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
                    ONSPDFiles);
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
        ONSPDFiles = env.getDW_Postcode_Handler().getONSPDFiles();
        String name;
        name = "SingleTimeGenericCounts";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + sCommaSpace;
        header += getHeaderSingleTimeGeneric();
        if (!underOccupancy) {
            header += "PostCodeLookupDate, ";
            header += "PostCodeLookupFile, ";
            header += DW_SHBE_Collection.sLineCount + sCommaSpace;
            header += DW_SHBE_Collection.sCountDRecords + sCommaSpace;
            header += DW_SHBE_Collection.sCountSRecords + sCommaSpace;
            header += DW_SHBE_Collection.sCountUniqueClaimants + sCommaSpace;
            header += DW_SHBE_Collection.sCountUniquePartners + sCommaSpace;
            header += DW_SHBE_Collection.sCountUniqueDependents + sCommaSpace;
            header += DW_SHBE_Collection.sCountUniqueNonDependents + sCommaSpace;
            header += DW_SHBE_Collection.sCountUniqueIndividuals + sCommaSpace;
        }
        header += sAllTotalHouseholdSize + sCommaSpace;
        header += sAllAverageHouseholdSize + sCommaSpace;
        header += sHBTotalHouseholdSize + sCommaSpace;
        header += sHBAverageHouseholdSize + sCommaSpace;
        header += sCTBTotalHouseholdSize + sCommaSpace;
        header += sCTBAverageHouseholdSize + sCommaSpace;
        for (int i = 1; i < nTT; i++) {
            header += sAllTotalHouseholdSizeByTT[i] + sCommaSpace;
            header += sAllAverageHouseholdSizeByTT[i] + sCommaSpace;
        }
        header += sAllTotalCount_PostcodeValidFormat + sCommaSpace;
        header += sAllTotalCount_PostcodeValid + sCommaSpace;
        header += sHBTotalCount_PostcodeValidFormat + sCommaSpace;
        header += sHBTotalCount_PostcodeValid + sCommaSpace;
        header += sCTBTotalCount_PostcodeValidFormat + sCommaSpace;
        header += sCTBTotalCount_PostcodeValid + sCommaSpace;
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
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + sCommaSpace;
            line += getLineSingleTimeGeneric(key, summary);
            if (!underOccupancy) {
                line += getPostcodeLookupDateAndFilenameLinePart(filename1, ONSPDFiles);
                line += summary.get(DW_SHBE_Collection.sLineCount) + sCommaSpace;
                line += summary.get(DW_SHBE_Collection.sCountDRecords) + sCommaSpace;
                line += summary.get(DW_SHBE_Collection.sCountSRecords) + sCommaSpace;
                line += summary.get(DW_SHBE_Collection.sCountUniqueClaimants) + sCommaSpace;
                line += summary.get(DW_SHBE_Collection.sCountUniquePartners) + sCommaSpace;
                line += summary.get(DW_SHBE_Collection.sCountUniqueDependents) + sCommaSpace;
                line += summary.get(DW_SHBE_Collection.sCountUniqueNonDependents) + sCommaSpace;
                line += summary.get(DW_SHBE_Collection.sCountUniqueIndividuals) + sCommaSpace;
            }
            line += summary.get(sAllTotalHouseholdSize) + sCommaSpace;
            line += summary.get(sAllAverageHouseholdSize) + sCommaSpace;
            line += summary.get(sHBTotalHouseholdSize) + sCommaSpace;
            line += summary.get(sHBAverageHouseholdSize) + sCommaSpace;
            line += summary.get(sCTBTotalHouseholdSize) + sCommaSpace;
            line += summary.get(sCTBAverageHouseholdSize) + sCommaSpace;
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sAllTotalHouseholdSizeByTT[i]) + sCommaSpace;
                line += summary.get(sAllAverageHouseholdSizeByTT[i]) + sCommaSpace;
            }
            line += summary.get(sAllTotalCount_PostcodeValidFormat) + sCommaSpace;
            line += summary.get(sAllTotalCount_PostcodeValid) + sCommaSpace;
            line += summary.get(sHBTotalCount_PostcodeValidFormat) + sCommaSpace;
            line += summary.get(sHBTotalCount_PostcodeValid) + sCommaSpace;
            line += summary.get(sCTBTotalCount_PostcodeValidFormat) + sCommaSpace;
            line += summary.get(sCTBTotalCount_PostcodeValid) + sCommaSpace;
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
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + sCommaSpace;
        header += getHeaderSingleTimeGeneric();
        header += sTotalWeeklyHBEntitlement + sCommaSpace;
        header += sTotalCount_WeeklyHBEntitlementNonZero + sCommaSpace;
        header += sTotalCount_WeeklyHBEntitlementZero + sCommaSpace;
        header += sAverageWeeklyHBEntitlement + sCommaSpace;
        // WeeklyEligibleRentAmount
        header += sAllTotalWeeklyEligibleRentAmount + sCommaSpace;
        header += sAllTotalCount_WeeklyEligibleRentAmountNonZero + sCommaSpace;
        header += sAllTotalCount_WeeklyEligibleRentAmountZero + sCommaSpace;
        header += sAllAverageWeeklyEligibleRentAmount + sCommaSpace;
        header += sHBTotalWeeklyEligibleRentAmount + sCommaSpace;
        header += sHBTotalCount_WeeklyEligibleRentAmountNonZero + sCommaSpace;
        header += sHBTotalCount_WeeklyEligibleRentAmountZero + sCommaSpace;
        header += sHBAverageWeeklyEligibleRentAmount + sCommaSpace;
        header += sCTBTotalWeeklyEligibleRentAmount + sCommaSpace;
        header += sCTBTotalCount_WeeklyEligibleRentAmountNonZero + sCommaSpace;
        header += sCTBTotalCount_WeeklyEligibleRentAmountZero + sCommaSpace;
        header += sCTBAverageWeeklyEligibleRentAmount + sCommaSpace;
        // WeeklyEligibleCouncilTaxAmount
        header += sAllTotalWeeklyEligibleCouncilTaxAmount + sCommaSpace;
        header += sAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero + sCommaSpace;
        header += sAllTotalCount_WeeklyEligibleCouncilTaxAmountZero + sCommaSpace;
        header += sAllAverageWeeklyEligibleCouncilTaxAmount + sCommaSpace;
        header += sHBTotalWeeklyEligibleCouncilTaxAmount + sCommaSpace;
        header += sHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero + sCommaSpace;
        header += sHBTotalCount_WeeklyEligibleCouncilTaxAmountZero + sCommaSpace;
        header += sHBAverageWeeklyEligibleCouncilTaxAmount + sCommaSpace;
        header += sCTBTotalWeeklyEligibleCouncilTaxAmount + sCommaSpace;
        header += sCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero + sCommaSpace;
        header += sCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero + sCommaSpace;
        header += sCTBAverageWeeklyEligibleCouncilTaxAmount + sCommaSpace;
        // ContractualRentAmount
        header += sAllTotalContractualRentAmount + sCommaSpace;
        header += sAllTotalCountContractualRentAmountNonZeroCount + sCommaSpace;
        header += sAllTotalCountContractualRentAmountZeroCount + sCommaSpace;
        header += sAllAverageContractualRentAmount + sCommaSpace;
        header += sHBTotalContractualRentAmount + sCommaSpace;
        header += sHBTotalCountContractualRentAmountNonZeroCount + sCommaSpace;
        header += sHBTotalCountContractualRentAmountZeroCount + sCommaSpace;
        header += sHBAverageContractualRentAmount + sCommaSpace;
        header += sCTBTotalContractualRentAmount + sCommaSpace;
        header += sCTBTotalCountContractualRentAmountNonZeroCount + sCommaSpace;
        header += sCTBTotalCountContractualRentAmountZeroCount + sCommaSpace;
        header += sCTBAverageContractualRentAmount + sCommaSpace;
        // WeeklyAdditionalDiscretionaryPayment
        header += sAllTotalWeeklyAdditionalDiscretionaryPayment + sCommaSpace;
        header += sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero + sCommaSpace;
        header += sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero + sCommaSpace;
        header += sAllAverageWeeklyAdditionalDiscretionaryPayment + sCommaSpace;
        header += sHBTotalWeeklyAdditionalDiscretionaryPayment + sCommaSpace;
        header += sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero + sCommaSpace;
        header += sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero + sCommaSpace;
        header += sHBAverageWeeklyAdditionalDiscretionaryPayment + sCommaSpace;
        header += sCTBTotalWeeklyAdditionalDiscretionaryPayment + sCommaSpace;
        header += sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero + sCommaSpace;
        header += sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero + sCommaSpace;
        header += sCTBAverageWeeklyAdditionalDiscretionaryPayment + sCommaSpace;
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        header += sAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + sCommaSpace;
        header += sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + sCommaSpace;
        header += sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + sCommaSpace;
        header += sAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + sCommaSpace;
        header += sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + sCommaSpace;
        header += sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + sCommaSpace;
        header += sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + sCommaSpace;
        header += sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + sCommaSpace;
        header += sCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + sCommaSpace;
        header += sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + sCommaSpace;
        header += sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + sCommaSpace;
        header += sCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + sCommaSpace;
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
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + sCommaSpace;
            line += getLineSingleTimeGeneric(key, summary);
            line += summary.get(sTotalWeeklyHBEntitlement) + sCommaSpace;
            line += summary.get(sTotalCount_WeeklyHBEntitlementNonZero) + sCommaSpace;
            line += summary.get(sTotalCount_WeeklyHBEntitlementZero) + sCommaSpace;
            line += summary.get(sAverageWeeklyHBEntitlement) + sCommaSpace;
            // WeeklyEligibleRentAmount
            line += summary.get(sAllTotalWeeklyEligibleRentAmount) + sCommaSpace;
            line += summary.get(sAllTotalCount_WeeklyEligibleRentAmountNonZero) + sCommaSpace;
            line += summary.get(sAllTotalCount_WeeklyEligibleRentAmountZero) + sCommaSpace;
            line += summary.get(sAllAverageWeeklyEligibleRentAmount) + sCommaSpace;
            line += summary.get(sHBTotalWeeklyEligibleRentAmount) + sCommaSpace;
            line += summary.get(sHBTotalCount_WeeklyEligibleRentAmountNonZero) + sCommaSpace;
            line += summary.get(sHBTotalCount_WeeklyEligibleRentAmountZero) + sCommaSpace;
            line += summary.get(sHBAverageWeeklyEligibleRentAmount) + sCommaSpace;
            line += summary.get(sCTBTotalWeeklyEligibleRentAmount) + sCommaSpace;
            line += summary.get(sCTBTotalCount_WeeklyEligibleRentAmountNonZero) + sCommaSpace;
            line += summary.get(sCTBTotalCount_WeeklyEligibleRentAmountZero) + sCommaSpace;
            line += summary.get(sCTBAverageWeeklyEligibleRentAmount) + sCommaSpace;
            // WeeklyEligibleCouncilTaxAmount
            line += summary.get(sAllTotalWeeklyEligibleCouncilTaxAmount) + sCommaSpace;
            line += summary.get(sAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero) + sCommaSpace;
            line += summary.get(sAllTotalCount_WeeklyEligibleCouncilTaxAmountZero) + sCommaSpace;
            line += summary.get(sAllAverageWeeklyEligibleCouncilTaxAmount) + sCommaSpace;
            line += summary.get(sHBTotalWeeklyEligibleCouncilTaxAmount) + sCommaSpace;
            line += summary.get(sHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero) + sCommaSpace;
            line += summary.get(sHBTotalCount_WeeklyEligibleCouncilTaxAmountZero) + sCommaSpace;
            line += summary.get(sHBAverageWeeklyEligibleCouncilTaxAmount) + sCommaSpace;
            line += summary.get(sCTBTotalWeeklyEligibleCouncilTaxAmount) + sCommaSpace;
            line += summary.get(sCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero) + sCommaSpace;
            line += summary.get(sCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero) + sCommaSpace;
            line += summary.get(sCTBAverageWeeklyEligibleCouncilTaxAmount) + sCommaSpace;
            // ContractualRentAmount
            line += summary.get(sAllTotalContractualRentAmount) + sCommaSpace;
            line += summary.get(sAllTotalCountContractualRentAmountNonZeroCount) + sCommaSpace;
            line += summary.get(sAllTotalCountContractualRentAmountZeroCount) + sCommaSpace;
            line += summary.get(sAllAverageContractualRentAmount) + sCommaSpace;
            line += summary.get(sHBTotalContractualRentAmount) + sCommaSpace;
            line += summary.get(sHBTotalCountContractualRentAmountNonZeroCount) + sCommaSpace;
            line += summary.get(sHBTotalCountContractualRentAmountZeroCount) + sCommaSpace;
            line += summary.get(sHBAverageContractualRentAmount) + sCommaSpace;
            line += summary.get(sCTBTotalContractualRentAmount) + sCommaSpace;
            line += summary.get(sCTBTotalCountContractualRentAmountNonZeroCount) + sCommaSpace;
            line += summary.get(sCTBTotalCountContractualRentAmountZeroCount) + sCommaSpace;
            line += summary.get(sCTBAverageContractualRentAmount) + sCommaSpace;
            // WeeklyAdditionalDiscretionaryPayment
            line += summary.get(sAllTotalWeeklyAdditionalDiscretionaryPayment) + sCommaSpace;
            line += summary.get(sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero) + sCommaSpace;
            line += summary.get(sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero) + sCommaSpace;
            line += summary.get(sAllAverageWeeklyAdditionalDiscretionaryPayment) + sCommaSpace;
            line += summary.get(sHBTotalWeeklyAdditionalDiscretionaryPayment) + sCommaSpace;
            line += summary.get(sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero) + sCommaSpace;
            line += summary.get(sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero) + sCommaSpace;
            line += summary.get(sHBAverageWeeklyAdditionalDiscretionaryPayment) + sCommaSpace;
            line += summary.get(sCTBTotalWeeklyAdditionalDiscretionaryPayment) + sCommaSpace;
            line += summary.get(sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero) + sCommaSpace;
            line += summary.get(sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero) + sCommaSpace;
            line += summary.get(sCTBAverageWeeklyAdditionalDiscretionaryPayment) + sCommaSpace;
            // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
            line += summary.get(sAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + sCommaSpace;
            line += summary.get(sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + sCommaSpace;
            line += summary.get(sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + sCommaSpace;
            line += summary.get(sAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + sCommaSpace;
            line += summary.get(sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + sCommaSpace;
            line += summary.get(sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + sCommaSpace;
            line += summary.get(sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + sCommaSpace;
            line += summary.get(sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + sCommaSpace;
            line += summary.get(sCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + sCommaSpace;
            line += summary.get(sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + sCommaSpace;
            line += summary.get(sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + sCommaSpace;
            line += summary.get(sCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + sCommaSpace;
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
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + sCommaSpace;
        header += getHeaderSingleTimeGeneric();
        header += sAllTotalCount_ClaimantsEmployed + sCommaSpace;
        header += sAllPercentage_ClaimantsEmployed + sCommaSpace;
        header += sAllTotalCount_ClaimantsSelfEmployed + sCommaSpace;
        header += sAllPercentage_ClaimantsSelfEmployed + sCommaSpace;
        header += sAllTotalCount_ClaimantsStudents + sCommaSpace;
        header += sAllPercentage_ClaimantsStudents + sCommaSpace;
        header += sHBTotalCount_ClaimantsEmployed + sCommaSpace;
        header += sHBPercentageOfHB_ClaimantsEmployed + sCommaSpace;
        header += sHBTotalCountClaimantsSelfEmployed + sCommaSpace;
        header += sHBPercentageOfHB_ClaimantsSelfEmployed + sCommaSpace;
        header += sHBTotalCountClaimantsStudents + sCommaSpace;
        header += sHBPercentageOfHB_ClaimantsStudents + sCommaSpace;
        header += sCTBTotalCount_ClaimantsEmployed + sCommaSpace;
        header += sCTBPercentageOfCTB_ClaimantsEmployed + sCommaSpace;
        header += sCTBTotalCountClaimantsSelfEmployed + sCommaSpace;
        header += sCTBPercentageOfCTB_ClaimantsSelfEmployed + sCommaSpace;
        header += sCTBTotalCountClaimantsStudents + sCommaSpace;
        header += sCTBPercentageOfCTB_ClaimantsStudents + sCommaSpace;
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
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + sCommaSpace;
            line += getLineSingleTimeGeneric(key, summary);
            line += summary.get(sAllTotalCount_ClaimantsEmployed) + sCommaSpace;
            line += summary.get(sAllPercentage_ClaimantsEmployed) + sCommaSpace;
            line += summary.get(sAllTotalCount_ClaimantsSelfEmployed) + sCommaSpace;
            line += summary.get(sAllPercentage_ClaimantsSelfEmployed) + sCommaSpace;
            line += summary.get(sAllTotalCount_ClaimantsStudents) + sCommaSpace;
            line += summary.get(sAllPercentage_ClaimantsStudents) + sCommaSpace;
            line += summary.get(sHBTotalCount_ClaimantsEmployed) + sCommaSpace;
            line += summary.get(sHBPercentageOfHB_ClaimantsEmployed) + sCommaSpace;
            line += summary.get(sHBTotalCountClaimantsSelfEmployed) + sCommaSpace;
            line += summary.get(sHBPercentageOfHB_ClaimantsSelfEmployed) + sCommaSpace;
            line += summary.get(sHBTotalCountClaimantsStudents) + sCommaSpace;
            line += summary.get(sHBPercentageOfHB_ClaimantsStudents) + sCommaSpace;
            line += summary.get(sCTBTotalCount_ClaimantsEmployed) + sCommaSpace;
            line += summary.get(sCTBPercentageOfCTB_ClaimantsEmployed) + sCommaSpace;
            line += summary.get(sCTBTotalCountClaimantsSelfEmployed) + sCommaSpace;
            line += summary.get(sCTBPercentageOfCTB_ClaimantsSelfEmployed) + sCommaSpace;
            line += summary.get(sCTBTotalCountClaimantsStudents) + sCommaSpace;
            line += summary.get(sCTBPercentageOfCTB_ClaimantsStudents) + sCommaSpace;
            line += summary.get(sCTBTotalCount_LHACases) + sCommaSpace;
            line += summary.get(sCTBPercentageOfCTB_LHACases) + sCommaSpace;
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    protected String getPostcodeLookupDateAndFilenameLinePart(
            String filename,
            TreeMap<String, File> ONSPDFiles) {
        String result;
        String PostCodeLookupDate0 = null;
        String PostCodeLookupFile0Name = null;
        if (filename != null) {
            PostCodeLookupDate0 = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                    tDW_SHBE_Handler.getYM3(filename));
            PostCodeLookupFile0Name = ONSPDFiles.get(PostCodeLookupDate0).getName();
        }
        result = PostCodeLookupDate0 + sCommaSpace + PostCodeLookupFile0Name + sCommaSpace;
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
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + sCommaSpace;
        header += getHeaderSingleTimeGeneric();
        header += sAllTotalIncome + sCommaSpace;
        header += sAllTotalCount_IncomeNonZero + sCommaSpace;
        header += sAllTotalCount_IncomeZero + sCommaSpace;
        header += sAllAverageIncome + sCommaSpace;
        header += sAllTotalWeeklyEligibleRentAmount + sCommaSpace;
        header += sAllTotalCount_WeeklyEligibleRentAmountNonZero + sCommaSpace;
        header += sAllAverageWeeklyEligibleRentAmount + sCommaSpace;
        for (int i = 1; i < nTT; i++) {
            header += sAllTotalIncomeTT[i] + sCommaSpace;
            header += sAllTotalCount_IncomeNonZeroTT[i] + sCommaSpace;
            header += sAllTotalCount_IncomeZeroTT[i] + sCommaSpace;
            header += sAllAverageIncomeTT[i] + sCommaSpace;
            header += sAllTotalWeeklyEligibleRentAmountTT[i] + sCommaSpace;
            header += sAllTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] + sCommaSpace;
            header += sAllAverageWeeklyEligibleRentAmountTT[i] + sCommaSpace;
        }
        // HB
        header += sHBTotalIncome + sCommaSpace;
        header += sHBTotalCount_IncomeNonZero + sCommaSpace;
        header += sHBTotalCount_IncomeZero + sCommaSpace;
        header += sHBAverageIncome + sCommaSpace;
        header += sHBTotalWeeklyEligibleRentAmount + sCommaSpace;
        header += sHBTotalCount_WeeklyEligibleRentAmountNonZero + sCommaSpace;
        header += sHBAverageWeeklyEligibleRentAmount + sCommaSpace;
        for (int i = 1; i < nTT; i++) {
            header += sHBTotalIncomeTT[i] + sCommaSpace;
            header += sHBTotalCount_IncomeNonZeroTT[i] + sCommaSpace;
            header += sHBTotalCount_IncomeZeroTT[i] + sCommaSpace;
            header += sHBAverageIncomeTT[i] + sCommaSpace;
            header += sHBTotalWeeklyEligibleRentAmountTT[i] + sCommaSpace;
            header += sHBTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] + sCommaSpace;
            header += sHBAverageWeeklyEligibleRentAmountTT[i] + sCommaSpace;
        }
        // CTB
        header += sCTBTotalIncome + sCommaSpace;
        header += sCTBTotalCount_IncomeNonZero + sCommaSpace;
        header += sCTBTotalCount_IncomeZero + sCommaSpace;
        header += sCTBAverageIncome + sCommaSpace;
        header += sCTBTotalWeeklyEligibleRentAmount + sCommaSpace;
        header += sCTBTotalCount_WeeklyEligibleRentAmountNonZero + sCommaSpace;
        header += sCTBAverageWeeklyEligibleRentAmount + sCommaSpace;
        for (int i = 1; i < nTT; i++) {
            header += sCTBTotalIncomeTT[i] + sCommaSpace;
            header += sCTBTotalCount_IncomeNonZeroTT[i] + sCommaSpace;
            header += sCTBTotalCount_IncomeZeroTT[i] + sCommaSpace;
            header += sCTBAverageIncomeTT[i] + sCommaSpace;
            header += sCTBTotalWeeklyEligibleRentAmountTT[i] + sCommaSpace;
            header += sCTBTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] + sCommaSpace;
            header += sCTBAverageWeeklyEligibleRentAmountTT[i] + sCommaSpace;
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
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + sCommaSpace;
            line += getLineSingleTimeGeneric(key, summary);
            // All
            line += summary.get(sAllTotalIncome) + sCommaSpace;
            line += summary.get(sAllTotalCount_IncomeNonZero) + sCommaSpace;
            line += summary.get(sAllTotalCount_IncomeZero) + sCommaSpace;
            line += summary.get(sAllAverageIncome) + sCommaSpace;
            line += summary.get(sAllTotalWeeklyEligibleRentAmount) + sCommaSpace;
            line += summary.get(sAllTotalCount_WeeklyEligibleRentAmountNonZero) + sCommaSpace;
            line += summary.get(sAllAverageWeeklyEligibleRentAmount) + sCommaSpace;
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sAllTotalIncomeTT[i]) + sCommaSpace;
                line += summary.get(sAllTotalCount_IncomeNonZeroTT[i]) + sCommaSpace;
                line += summary.get(sAllTotalCount_IncomeZeroTT[i]) + sCommaSpace;
                line += summary.get(sAllAverageIncomeTT[i]) + sCommaSpace;
                line += summary.get(sAllTotalWeeklyEligibleRentAmountTT[i]) + sCommaSpace;
                line += summary.get(sAllTotalCount_WeeklyEligibleRentAmountNonZeroTT[i]) + sCommaSpace;
                line += summary.get(sAllAverageWeeklyEligibleRentAmountTT[i]) + sCommaSpace;
            }
            // HB
            line += summary.get(sHBTotalIncome) + sCommaSpace;
            line += summary.get(sHBTotalCount_IncomeNonZero) + sCommaSpace;
            line += summary.get(sHBTotalCount_IncomeZero) + sCommaSpace;
            line += summary.get(sHBAverageIncome) + sCommaSpace;
            line += summary.get(sHBTotalWeeklyEligibleRentAmount) + sCommaSpace;
            line += summary.get(sHBTotalCount_WeeklyEligibleRentAmountNonZero) + sCommaSpace;
            line += summary.get(sHBAverageWeeklyEligibleRentAmount) + sCommaSpace;
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sHBTotalIncomeTT[i]) + sCommaSpace;
                line += summary.get(sHBTotalCount_IncomeNonZeroTT[i]) + sCommaSpace;
                line += summary.get(sHBTotalCount_IncomeZeroTT[i]) + sCommaSpace;
                line += summary.get(sHBAverageIncomeTT[i]) + sCommaSpace;
                line += summary.get(sHBTotalWeeklyEligibleRentAmountTT[i]) + sCommaSpace;
                line += summary.get(sHBTotalCount_WeeklyEligibleRentAmountNonZeroTT[i]) + sCommaSpace;
                line += summary.get(sHBAverageWeeklyEligibleRentAmountTT[i]) + sCommaSpace;
            }
            // CTB
            line += summary.get(sCTBTotalIncome) + sCommaSpace;
            line += summary.get(sCTBTotalCount_IncomeNonZero) + sCommaSpace;
            line += summary.get(sCTBTotalCount_IncomeZero) + sCommaSpace;
            line += summary.get(sCTBAverageIncome) + sCommaSpace;
            line += summary.get(sCTBTotalWeeklyEligibleRentAmount) + sCommaSpace;
            line += summary.get(sCTBTotalCount_WeeklyEligibleRentAmountNonZero) + sCommaSpace;
            line += summary.get(sCTBAverageWeeklyEligibleRentAmount) + sCommaSpace;
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sCTBTotalIncomeTT[i]) + sCommaSpace;
                line += summary.get(sCTBTotalCount_IncomeNonZeroTT[i]) + sCommaSpace;
                line += summary.get(sCTBTotalCount_IncomeZeroTT[i]) + sCommaSpace;
                line += summary.get(sCTBAverageIncomeTT[i]) + sCommaSpace;
                line += summary.get(sCTBTotalWeeklyEligibleRentAmountTT[i]) + sCommaSpace;
                line += summary.get(sCTBTotalCount_WeeklyEligibleRentAmountNonZeroTT[i]) + sCommaSpace;
                line += summary.get(sCTBAverageWeeklyEligibleRentAmountTT[i]) + sCommaSpace;
            }
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
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + sCommaSpace;
        header += getHeaderSingleTimeGeneric();
        for (int i = 1; i < nEG; i++) {
            header += sAllTotalCount_EthnicGroupClaimant[i] + sCommaSpace;
            header += sAllPercentageOfAll_EthnicGroupClaimant[i] + sCommaSpace;
            header += sAllTotalCount_EthnicGroupSocialTTClaimant[i] + sCommaSpace;
            header += sAllPercentageOfSocialTT_EthnicGroupSocialTTClaimant[i] + sCommaSpace;
            header += sAllTotalCount_EthnicGroupPrivateDeregulatedTTClaimant[i] + sCommaSpace;
            header += sAllPercentageOfPrivateDeregulatedTT_EthnicGroupPrivateDeregulatedTTClaimant[i] + sCommaSpace;
        }
        for (int i = 1; i < nEG; i++) {
            header += sHBTotalCount_EthnicGroupClaimant[i] + sCommaSpace;
            header += sHBPercentageOfHB_EthnicGroupClaimant[i] + sCommaSpace;
        }
        for (int i = 1; i < nEG; i++) {
            header += sCTBTotalCount_EthnicGroupClaimant[i] + sCommaSpace;
            header += sCTBPercentageOfCTB_EthnicGroupClaimant[i] + sCommaSpace;
        }
        for (int i = 1; i < nEG; i++) {
            for (int j = 1; j < nTT; j++) {
                header += sAllTotalCount_EthnicGroupClaimantByTT[i][j] + sCommaSpace;
                header += sAllPercentageOfTT_EthnicGroupClaimantByTT[i][j] + sCommaSpace;
                header += sAllPercentageOfEthnicGroup_EthnicGroupClaimantByTT[i][j] + sCommaSpace;
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
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + sCommaSpace;
            line += getLineSingleTimeGeneric(key, summary);
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sAllTotalCount_EthnicGroupClaimant[i]) + sCommaSpace;
                line += summary.get(sAllPercentageOfAll_EthnicGroupClaimant[i]) + sCommaSpace;
                line += summary.get(sAllTotalCount_EthnicGroupSocialTTClaimant[i]) + sCommaSpace;
                line += summary.get(sAllPercentageOfSocialTT_EthnicGroupSocialTTClaimant[i]) + sCommaSpace;
                line += summary.get(sAllTotalCount_EthnicGroupPrivateDeregulatedTTClaimant[i]) + sCommaSpace;
                line += summary.get(sAllPercentageOfPrivateDeregulatedTT_EthnicGroupPrivateDeregulatedTTClaimant[i]) + sCommaSpace;
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sHBTotalCount_EthnicGroupClaimant[i]) + sCommaSpace;
                line += summary.get(sHBPercentageOfHB_EthnicGroupClaimant[i]) + sCommaSpace;
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sCTBTotalCount_EthnicGroupClaimant[i]) + sCommaSpace;
                line += summary.get(sCTBPercentageOfCTB_EthnicGroupClaimant[i]) + sCommaSpace;
            }
            for (int i = 1; i < nEG; i++) {
                for (int j = 1; j < nTT; j++) {
                    line += summary.get(sAllTotalCount_EthnicGroupClaimantByTT[i][j]) + sCommaSpace;
                    line += summary.get(sAllPercentageOfTT_EthnicGroupClaimantByTT[i][j]) + sCommaSpace;
                    line += summary.get(sAllPercentageOfEthnicGroup_EthnicGroupClaimantByTT[i][j]) + sCommaSpace;
                }
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
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + sCommaSpace;
        header += getHeaderSingleTimeGeneric();
        header += sTotalCount_SocialTTsClaimant + sCommaSpace;
        header += sPercentageOfAll_SocialTTsClaimant + sCommaSpace;
        header += sPercentageOfHB_SocialTTsClaimant + sCommaSpace;
        header += sTotalCount_PrivateDeregulatedTTsClaimant + sCommaSpace;
        header += sPercentageOfAll_PrivateDeregulatedTTsClaimant + sCommaSpace;
        header += sPercentageOfHB_PrivateDeregulatedTTsClaimant + sCommaSpace;
        for (int i = 1; i < nTT; i++) {
            header += sTotalCount_ClaimantTT[i] + sCommaSpace;
            header += sPercentageOfAll_ClaimantTT[i] + sCommaSpace;
            if ((i == 5 || i == 7)) {
                header += sPercentageOfCTB_ClaimantTT[i] + sCommaSpace;
            } else {
                header += sPercentageOfHB_ClaimantTT[i] + sCommaSpace;
            }
        }
        header += sAllTotalCount_LHACases + sCommaSpace;
        header += sAllPercentageOfAll_LHACases + sCommaSpace;
        header += sHBTotalCount_LHACases + sCommaSpace;
        header += sHBPercentageOfHB_LHACases + sCommaSpace;
        header += sCTBTotalCount_LHACases + sCommaSpace;
        header += sCTBPercentageOfCTB_LHACases + sCommaSpace;
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
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + sCommaSpace;
            line += getLineSingleTimeGeneric(key, summary);
            line += summary.get(sTotalCount_SocialTTsClaimant) + sCommaSpace;
            line += summary.get(sPercentageOfAll_SocialTTsClaimant) + sCommaSpace;
            line += summary.get(sPercentageOfHB_SocialTTsClaimant) + sCommaSpace;
            line += summary.get(sTotalCount_PrivateDeregulatedTTsClaimant) + sCommaSpace;
            line += summary.get(sPercentageOfAll_PrivateDeregulatedTTsClaimant) + sCommaSpace;
            line += summary.get(sPercentageOfHB_PrivateDeregulatedTTsClaimant) + sCommaSpace;
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sTotalCount_ClaimantTT[i]) + sCommaSpace;
                line += summary.get(sPercentageOfAll_ClaimantTT[i]) + sCommaSpace;
                if ((i == 5 || i == 7)) {
                    line += summary.get(sPercentageOfCTB_ClaimantTT[i]) + sCommaSpace;
                } else {
                    line += summary.get(sPercentageOfHB_ClaimantTT[i]) + sCommaSpace;
                }
            }
            line += summary.get(sAllTotalCount_LHACases) + sCommaSpace;
            line += summary.get(sAllPercentageOfAll_LHACases) + sCommaSpace;
            line += summary.get(sHBTotalCount_LHACases) + sCommaSpace;
            line += summary.get(sHBPercentageOfHB_LHACases) + sCommaSpace;
            line += summary.get(sCTBTotalCount_LHACases) + sCommaSpace;
            line += summary.get(sCTBPercentageOfCTB_LHACases) + sCommaSpace;
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
        name = sSingleTimePSI;
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + sCommaSpace;
        header += getHeaderSingleTimeGeneric();
        for (int i = 1; i < nPSI; i++) {
            header += sAllTotalCount_PSI[i] + sCommaSpace;
            header += sAllPercentageOfAll_PSI[i] + sCommaSpace;
        }
        for (int i = 1; i < nPSI; i++) {
            header += sHBTotalCount_PSI[i] + sCommaSpace;
            header += sHBPercentageOfHB_PSI[i] + sCommaSpace;
        }
        for (int i = 1; i < nPSI; i++) {
            header += sCTBTotalCount_PSI[i] + sCommaSpace;
            header += sCTBPercentageOfCTB_PSI[i] + sCommaSpace;
        }
        for (int i = 1; i < nPSI; i++) {
            for (int j = 1; j < nTT; j++) {
                header += sTotalCount_PSIByTT[i][j] + sCommaSpace;
                header += sPercentageOfAll_PSIByTT[i][j] + sCommaSpace;
                if (j == 5 || j == 7) {
                    header += sPercentageOfCTB_PSIByTT[i][j] + sCommaSpace;
                } else {
                    header += sPercentageOfHB_PSIByTT[i][j] + sCommaSpace;
                }
                header += sPercentageOfTT_PSIByTT[i][j] + sCommaSpace;
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
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + sCommaSpace;
            line += getLineSingleTimeGeneric(key, summary);
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sAllTotalCount_PSI[i]) + sCommaSpace;
                line += summary.get(sAllPercentageOfAll_PSI[i]) + sCommaSpace;
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sHBTotalCount_PSI[i]) + sCommaSpace;
                line += summary.get(sHBPercentageOfHB_PSI[i]) + sCommaSpace;
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sCTBTotalCount_PSI[i]) + sCommaSpace;
                line += summary.get(sCTBPercentageOfCTB_PSI[i]) + sCommaSpace;
            }
            for (int i = 1; i < nPSI; i++) {
                for (int j = 1; j < nTT; j++) {
                    line += summary.get(sTotalCount_PSIByTT[i][j]) + sCommaSpace;
                    line += summary.get(sPercentageOfAll_PSIByTT[i][j]) + sCommaSpace;
                    if (j == 5 || j == 7) {
                        line += summary.get(sPercentageOfCTB_PSIByTT[i][j]) + sCommaSpace;
                    } else {
                        line += summary.get(sPercentageOfHB_PSIByTT[i][j]) + sCommaSpace;
                    }
                    line += summary.get(sPercentageOfTT_PSIByTT[i][j]) + sCommaSpace;
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
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + sCommaSpace;
        header += getHeaderSingleTimeGeneric();
        // General
        // DisabilityAward
        header += sTotalCount_DisabilityAward + sCommaSpace;
        header += sPercentageOfAll_DisabilityAward + sCommaSpace;
        header += sTotalCount_DisabilityAwardHBTTs + sCommaSpace;
        header += sPercentageOfAll_DisabilityAwardHBTTs + sCommaSpace;
        header += sPercentageOfHB_DisabilityAwardHBTTs + sCommaSpace;
        header += sTotalCount_DisabilityAwardCTBTTs + sCommaSpace;
        header += sPercentageOfAll_DisabilityAwardCTBTTs + sCommaSpace;
        header += sPercentageOfCTB_DisabilityAwardCTBTTs + sCommaSpace;
        // DisabilityPremiumAward
        header += sTotalCount_DisabilityPremiumAward + sCommaSpace;
        header += sPercentageOfAll_DisabilityPremiumAward + sCommaSpace;
        header += sTotalCount_DisabilityPremiumAwardHBTTs + sCommaSpace;
        header += sPercentageOfAll_DisabilityPremiumAwardHBTTs + sCommaSpace;
        header += sPercentageOfHB_DisabilityPremiumAwardHBTTs + sCommaSpace;
        header += sTotalCount_DisabilityPremiumAwardCTBTTs + sCommaSpace;
        header += sPercentageOfAll_DisabilityPremiumAwardCTBTTs + sCommaSpace;
        header += sPercentageOfCTB_DisabilityPremiumAwardCTBTTs + sCommaSpace;
        // SevereDisabilityPremiumAward
        header += sTotalCount_SevereDisabilityPremiumAward + sCommaSpace;
        header += sPercentageOfAll_SevereDisabilityPremiumAward + sCommaSpace;
        header += sTotalCount_SevereDisabilityPremiumAwardHBTTs + sCommaSpace;
        header += sPercentageOfAll_SevereDisabilityPremiumAwardHBTTs + sCommaSpace;
        header += sPercentageOfHB_SevereDisabilityPremiumAwardHBTTs + sCommaSpace;
        header += sTotalCount_SevereDisabilityPremiumAwardCTBTTs + sCommaSpace;
        header += sPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs + sCommaSpace;
        header += sPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs + sCommaSpace;
        // DisabledChildPremiumAward
        header += sTotalCount_DisabledChildPremiumAward + sCommaSpace;
        header += sPercentageOfAll_DisabledChildPremiumAward + sCommaSpace;
        header += sTotalCount_DisabledChildPremiumAwardHBTTs + sCommaSpace;
        header += sPercentageOfAll_DisabledChildPremiumAwardHBTTs + sCommaSpace;
        header += sPercentageOfHB_DisabledChildPremiumAwardHBTTs + sCommaSpace;
        header += sTotalCount_DisabledChildPremiumAwardCTBTTs + sCommaSpace;
        header += sPercentageOfAll_DisabledChildPremiumAwardCTBTTs + sCommaSpace;
        header += sPercentageOfCTB_DisabledChildPremiumAwardCTBTTs + sCommaSpace;
        // EnhancedDisabilityPremiumAward
        header += sTotalCount_EnhancedDisabilityPremiumAward + sCommaSpace;
        header += sPercentageOfAll_EnhancedDisabilityPremiumAward + sCommaSpace;
        header += sTotalCount_EnhancedDisabilityPremiumAwardHBTTs + sCommaSpace;
        header += sPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs + sCommaSpace;
        header += sPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs + sCommaSpace;
        header += sTotalCount_EnhancedDisabilityPremiumAwardCTBTTs + sCommaSpace;
        header += sPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs + sCommaSpace;
        header += sPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs + sCommaSpace;
        // SocialTTs
        // DisabilityAward
        header += sTotalCount_DisabilityAwardSocialTTs + sCommaSpace;
        header += sPercentageOfAll_DisabilityAwardSocialTTs + sCommaSpace;
        header += sPercentageOfHB_DisabilityAwardSocialTTs + sCommaSpace;
        header += sPercentageOfSocialTTs_DisabilityAwardSocialTTs + sCommaSpace;
        // DisabilityPremiumAward
        header += sTotalCount_DisabilityPremiumAwardSocialTTs + sCommaSpace;
        header += sPercentageOfAll_DisabilityPremiumAwardSocialTTs + sCommaSpace;
        header += sPercentageOfHB_DisabilityPremiumAwardSocialTTs + sCommaSpace;
        header += sPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs + sCommaSpace;
        // SevereDisabilityPremiumAward
        header += sTotalCount_SevereDisabilityPremiumAwardSocialTTs + sCommaSpace;
        header += sPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs + sCommaSpace;
        header += sPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs + sCommaSpace;
        header += sPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs + sCommaSpace;
        // DisabledChildPremiumAward
        header += sTotalCount_DisabledChildPremiumAwardSocialTTs + sCommaSpace;
        header += sPercentageOfAll_DisabledChildPremiumAwardSocialTTs + sCommaSpace;
        header += sPercentageOfHB_DisabledChildPremiumAwardSocialTTs + sCommaSpace;
        header += sPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs + sCommaSpace;
        // EnhancedDisabilityPremiumAward
        header += sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs + sCommaSpace;
        header += sPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs + sCommaSpace;
        header += sPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs + sCommaSpace;
        header += sPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs + sCommaSpace;
        // PrivateDeregulatedTTs
        // DisabilityAward
        header += sTotalCount_DisabilityAwardPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs + sCommaSpace;
        // DisabilityPremiumAward
        header += sTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs + sCommaSpace;
        // SevereDisabilityPremiumAward
        header += sTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + sCommaSpace;
        // DisabledChildPremiumAward
        header += sTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs + sCommaSpace;
        // EnhancedDisabilityPremiumAward
        header += sTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + sCommaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + sCommaSpace;
        for (int i = 1; i < nTT; i++) {
            // DisabilityAward
            header += sTotalCount_DisabilityAwardByTT[i] + sCommaSpace;
            header += sPercentageOfAll_DisabilityAwardByTT[i] + sCommaSpace;
            if (i == 5 || i == 7) {
                header += sPercentageOfCTB_DisabilityAwardByTT[i] + sCommaSpace;
            } else {
                header += sPercentageOfHB_DisabilityAwardByTT[i] + sCommaSpace;
            }
            header += sPercentageOfTT_DisabilityAwardByTT[i] + sCommaSpace;
            // DisabilityPremiumAward
            header += sTotalCount_DisabilityPremiumAwardByTT[i] + sCommaSpace;
            header += sPercentageOfAll_DisabilityPremiumAwardByTT[i] + sCommaSpace;
            if (i == 5 || i == 7) {
                header += sPercentageOfCTB_DisabilityPremiumAwardByTT[i] + sCommaSpace;
            } else {
                header += sPercentageOfHB_DisabilityPremiumAwardByTT[i] + sCommaSpace;
            }
            header += sPercentageOfTT_DisabilityPremiumAwardByTT[i] + sCommaSpace;
            // SevereDisabilityPremiumAward
            header += sTotalCount_SevereDisabilityPremiumAwardByTT[i] + sCommaSpace;
            header += sPercentageOfAll_SevereDisabilityPremiumAwardByTT[i] + sCommaSpace;
            if (i == 5 || i == 7) {
                header += sPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i] + sCommaSpace;
            } else {
                header += sPercentageOfHB_SevereDisabilityPremiumAwardByTT[i] + sCommaSpace;
            }
            header += sPercentageOfTT_SevereDisabilityPremiumAwardByTT[i] + sCommaSpace;
            // DisabledChildPremiumAward
            header += sTotalCount_DisabledChildPremiumAwardByTT[i] + sCommaSpace;
            header += sPercentageOfAll_DisabledChildPremiumAwardByTT[i] + sCommaSpace;
            if (i == 5 || i == 7) {
                header += sPercentageOfCTB_DisabledChildPremiumAwardByTT[i] + sCommaSpace;
            } else {
                header += sPercentageOfHB_DisabledChildPremiumAwardByTT[i] + sCommaSpace;
            }
            header += sPercentageOfTT_DisabledChildPremiumAwardByTT[i] + sCommaSpace;
            // EnhancedDisabilityPremiumAward
            header += sTotalCount_EnhancedDisabilityPremiumAwardByTT[i] + sCommaSpace;
            header += sPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i] + sCommaSpace;
            if (i == 5 || i == 7) {
                header += sPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i] + sCommaSpace;
            } else {
                header += sPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i] + sCommaSpace;
            }
            header += sPercentageOfTT_EnhancedDisabilityPremiumAwardByTT[i] + sCommaSpace;
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
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + sCommaSpace;
            line += getLineSingleTimeGeneric(key,
                    summary);
            // General
            // DisabilityAward
            line += summary.get(sTotalCount_DisabilityAward) + sCommaSpace;
            line += summary.get(sPercentageOfAll_DisabilityAward) + sCommaSpace;
            line += summary.get(sTotalCount_DisabilityAwardHBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_DisabilityAwardHBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfHB_DisabilityAwardHBTTs) + sCommaSpace;
            line += summary.get(sTotalCount_DisabilityAwardCTBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_DisabilityAwardCTBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfCTB_DisabilityAwardCTBTTs) + sCommaSpace;
            // DisabilityPremiumAward
            line += summary.get(sTotalCount_DisabilityPremiumAward) + sCommaSpace;
            line += summary.get(sPercentageOfAll_DisabilityPremiumAward) + sCommaSpace;
            line += summary.get(sTotalCount_DisabilityPremiumAwardHBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_DisabilityPremiumAwardHBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfHB_DisabilityPremiumAwardHBTTs) + sCommaSpace;
            line += summary.get(sTotalCount_DisabilityPremiumAwardCTBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_DisabilityPremiumAwardCTBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfCTB_DisabilityPremiumAwardCTBTTs) + sCommaSpace;
            // SevereDisabilityPremiumAward
            line += summary.get(sTotalCount_SevereDisabilityPremiumAward) + sCommaSpace;
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAward) + sCommaSpace;
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardHBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardHBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfHB_SevereDisabilityPremiumAwardHBTTs) + sCommaSpace;
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardCTBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs) + sCommaSpace;
            // DisabledChildPremiumAward
            line += summary.get(sTotalCount_DisabledChildPremiumAward) + sCommaSpace;
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAward) + sCommaSpace;
            line += summary.get(sTotalCount_DisabledChildPremiumAwardHBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardHBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfHB_DisabledChildPremiumAwardHBTTs) + sCommaSpace;
            line += summary.get(sTotalCount_DisabledChildPremiumAwardCTBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardCTBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfCTB_DisabledChildPremiumAwardCTBTTs) + sCommaSpace;
            // EnhancedDisabilityPremiumAward
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAward) + sCommaSpace;
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAward) + sCommaSpace;
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardHBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs) + sCommaSpace;
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardCTBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs) + sCommaSpace;
            line += summary.get(sPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs) + sCommaSpace;
            // SocialTTs
            // DisabilityAward
            line += summary.get(sTotalCount_DisabilityAwardSocialTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_DisabilityAwardSocialTTs) + sCommaSpace;
            line += summary.get(sPercentageOfHB_DisabilityAwardSocialTTs) + sCommaSpace;
            line += summary.get(sPercentageOfSocialTTs_DisabilityAwardSocialTTs) + sCommaSpace;
            // DisabilityPremiumAward
            line += summary.get(sTotalCount_DisabilityPremiumAwardSocialTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_DisabilityPremiumAwardSocialTTs) + sCommaSpace;
            line += summary.get(sPercentageOfHB_DisabilityPremiumAwardSocialTTs) + sCommaSpace;
            line += summary.get(sPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs) + sCommaSpace;
            // SevereDisabilityPremiumAward
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardSocialTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs) + sCommaSpace;
            line += summary.get(sPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs) + sCommaSpace;
            line += summary.get(sPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs) + sCommaSpace;
            // DisabledChildPremiumAward
            line += summary.get(sTotalCount_DisabledChildPremiumAwardSocialTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardSocialTTs) + sCommaSpace;
            line += summary.get(sPercentageOfHB_DisabledChildPremiumAwardSocialTTs) + sCommaSpace;
            line += summary.get(sPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs) + sCommaSpace;
            // EnhancedDisabilityPremiumAward
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs) + sCommaSpace;
            line += summary.get(sPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs) + sCommaSpace;
            line += summary.get(sPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs) + sCommaSpace;
            // PrivateDeregulatedTTs
            // DisabilityAward
            line += summary.get(sTotalCount_DisabilityAwardPrivateDeregulatedTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs) + sCommaSpace;
            line += summary.get(sPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs) + sCommaSpace;
            line += summary.get(sPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs) + sCommaSpace;
            // DisabilityPremiumAward
            line += summary.get(sTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs) + sCommaSpace;
            line += summary.get(sPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs) + sCommaSpace;
            line += summary.get(sPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs) + sCommaSpace;
            // SevereDisabilityPremiumAward
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + sCommaSpace;
            line += summary.get(sPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + sCommaSpace;
            line += summary.get(sPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + sCommaSpace;
            // DisabledChildPremiumAward
            line += summary.get(sTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs) + sCommaSpace;
            line += summary.get(sPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs) + sCommaSpace;
            line += summary.get(sPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs) + sCommaSpace;
            // EnhancedDisabilityPremiumAward
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + sCommaSpace;
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + sCommaSpace;
            line += summary.get(sPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + sCommaSpace;
            line += summary.get(sPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + sCommaSpace;
            for (int i = 1; i < nTT; i++) {
                // DisabilityAward
                line += summary.get(sTotalCount_DisabilityAwardByTT[i]) + sCommaSpace;
                line += summary.get(sPercentageOfAll_DisabilityAwardByTT[i]) + sCommaSpace;
                if (i == 5 || i == 7) {
                    line += summary.get(sPercentageOfCTB_DisabilityAwardByTT[i]) + sCommaSpace;
                } else {
                    line += summary.get(sPercentageOfHB_DisabilityAwardByTT[i]) + sCommaSpace;
                }
                line += summary.get(sPercentageOfTT_DisabilityAwardByTT[i]) + sCommaSpace;
                // DisabilityPremiumAward
                line += summary.get(sTotalCount_DisabilityPremiumAwardByTT[i]) + sCommaSpace;
                line += summary.get(sPercentageOfAll_DisabilityPremiumAwardByTT[i]) + sCommaSpace;
                if (i == 5 || i == 7) {
                    line += summary.get(sPercentageOfCTB_DisabilityPremiumAwardByTT[i]) + sCommaSpace;
                } else {
                    line += summary.get(sPercentageOfHB_DisabilityPremiumAwardByTT[i]) + sCommaSpace;
                }
                line += summary.get(sPercentageOfTT_DisabilityPremiumAwardByTT[i]) + sCommaSpace;
                // SevereDisabilityPremiumAward
                line += summary.get(sTotalCount_SevereDisabilityPremiumAwardByTT[i]) + sCommaSpace;
                line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardByTT[i]) + sCommaSpace;
                if (i == 5 || i == 7) {
                    line += summary.get(sPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i]) + sCommaSpace;
                } else {
                    line += summary.get(sPercentageOfHB_SevereDisabilityPremiumAwardByTT[i]) + sCommaSpace;
                }
                line += summary.get(sPercentageOfTT_SevereDisabilityPremiumAwardByTT[i]) + sCommaSpace;
                // DisabledChildPremiumAward
                line += summary.get(sTotalCount_DisabledChildPremiumAwardByTT[i]) + sCommaSpace;
                line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardByTT[i]) + sCommaSpace;
                if (i == 5 || i == 7) {
                    line += summary.get(sPercentageOfCTB_DisabledChildPremiumAwardByTT[i]) + sCommaSpace;
                } else {
                    line += summary.get(sPercentageOfHB_DisabledChildPremiumAwardByTT[i]) + sCommaSpace;
                }
                line += summary.get(sPercentageOfTT_DisabledChildPremiumAwardByTT[i]) + sCommaSpace;
                // EnhancedDisabilityPremiumAward
                line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardByTT[i]) + sCommaSpace;
                line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i]) + sCommaSpace;
                if (i == 5 || i == 7) {
                    line += summary.get(sPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i]) + sCommaSpace;
                } else {
                    line += summary.get(sPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i]) + sCommaSpace;
                }
                line += summary.get(sPercentageOfTT_EnhancedDisabilityPremiumAwardByTT[i]) + sCommaSpace;
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    protected String getHeaderSingleTimeGeneric() {
        String result;
        result = "year-month" + sCommaSpace;
        result += sAllCount1 + sCommaSpace;
        result += sHBCount1 + sCommaSpace;
        result += sCTBCount1 + sCommaSpace;
        result += "Month Year" + sCommaSpace;
        return result;
    }

    protected String getLineSingleTimeGeneric(
            String key,
            HashMap<String, String> summary) {
        String result;
        result = key + sCommaSpace;
        result += summary.get(sAllCount1) + sCommaSpace;
        result += summary.get(sHBCount1) + sCommaSpace;
        result += summary.get(sCTBCount1) + sCommaSpace;
        String[] split;
        split = key.split("-");
        result += Generic_Time.getMonth3Letters(split[1]);
        result += sSpace + split[0] + sCommaSpace;
        return result;
    }
}
