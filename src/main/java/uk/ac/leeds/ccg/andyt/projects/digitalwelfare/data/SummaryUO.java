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

/**
 *
 * @author geoagdt
 */
public class SummaryUO extends Summary {

    // Council
    // Counter Strings
    // HouseholdSize
    protected static final String sCouncilAllTotalHouseholdSize = "CouncilAllTotalHouseholdSize";
    protected static final String sCouncilAllAverageHouseholdSize = "CouncilAllAverageHouseholdSize";
    protected static final String sCouncilHBTotalHouseholdSize = "CouncilHBTotalHouseholdSize";
    protected static final String sCouncilHBAverageHouseholdSize = "CouncilHBAverageHouseholdSize";
    protected static final String sCouncilCTBTotalHouseholdSize = "CouncilCTBTotalHouseholdSize";
    protected static final String sCouncilCTBAverageHouseholdSize = "CouncilCTBAverageHouseholdSize";
    // PSI
    protected static String[] sCouncilAllTotalCount_PSI;
    protected static String[] sCouncilHBTotalCount_PSI;
    protected static String[] sCouncilCTBTotalCount_PSI;
    protected static String[] sCouncilAllPercentageOfAll_PSI;
    protected static String[] sCouncilHBPercentageOfHB_PSI;
    protected static String[] sCouncilCTBPercentageOfCTB_PSI;
    // PSIByTT
    protected static String[][] sCouncilTotalCount_PSIByTT;
    protected static String[][] sCouncilPercentageOfAll_PSIByTT;
    protected static String[][] sCouncilPercentageOfHB_PSIByTT;
    protected static String[][] sCouncilPercentageOfCTB_PSIByTT;
    // DisabilityPremiumAwardByTT
    protected static String[] sCouncilTotalCount_DisabilityPremiumAwardByTT;
    protected static String[] sCouncilPercentageOfAll_DisabilityPremiumAwardByTT;
    protected static String[] sCouncilPercentageOfHB_DisabilityPremiumAwardByTT;
    protected static String[] sCouncilPercentageOfCTB_DisabilityPremiumAwardByTT;
    protected static String[] sCouncilPercentageOfTT_DisabilityPremiumAwardByTT;
    // SevereDisabilityPremiumAwardByTT
    protected static String[] sCouncilTotalCount_SevereDisabilityPremiumAwardByTT;
    protected static String[] sCouncilPercentageOfAll_SevereDisabilityPremiumAwardByTT;
    protected static String[] sCouncilPercentageOfHB_SevereDisabilityPremiumAwardByTT;
    protected static String[] sCouncilPercentageOfCTB_SevereDisabilityPremiumAwardByTT;
    protected static String[] sCouncilPercentageOfTT_SevereDisabilityPremiumAwardByTT;
    // DisabledChildPremiumAwardByTT
    protected static String[] sCouncilTotalCount_DisabledChildPremiumAwardByTT;
    protected static String[] sCouncilPercentageOfAll_DisabledChildPremiumAwardByTT;
    protected static String[] sCouncilPercentageOfHB_DisabledChildPremiumAwardByTT;
    protected static String[] sCouncilPercentageOfCTB_DisabledChildPremiumAwardByTT;
    protected static String[] sCouncilPercentageOfTT_DisabledChildPremiumAwardByTT;
    // EnhancedDisabilityPremiumAwardByTT
    protected static String[] sCouncilTotalCount_EnhancedDisabilityPremiumAwardByTT;
    protected static String[] sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardByTT;
    protected static String[] sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardByTT;
    protected static String[] sCouncilPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT;
    protected static String[] sCouncilPercentageOfTT_EnhancedDisabilityPremiumAwardByTT;
    // DisabilityAwards
    protected static final String sCouncilTotalCount_DisabilityAward = "CouncilTotalCount_DisabilityAward";
    protected static final String sCouncilPercentageOfAll_DisabilityAward = "CouncilPercentageOfAll_DisabilityAward";
    // DisabilityPremiumAwards
    protected static final String sCouncilTotalCount_DisabilityPremiumAward = "CouncilTotalCount_DisabilityPremiumAward";
    protected static final String sCouncilPercentageOfAll_DisabilityPremiumAward = "CouncilPercentageOfAll_DisabilityPremiumAward";
    // SevereDisabilityPremiumAwards
    protected static final String sCouncilTotalCount_SevereDisabilityPremiumAward = "CouncilTotalCount_SevereDisabilityPremiumAward";
    protected static final String sCouncilPercentageOfAll_SevereDisabilityPremiumAward = "CouncilPercentageOfAll_SevereDisabilityPremiumAward";
    // DisabledChildPremiumAwards
    protected static final String sCouncilTotalCount_DisabledChildPremiumAward = "CouncilTotalCount_DisabledChildPremiumAward";
    protected static final String sCouncilPercentageOfAll_DisabledChildPremiumAward = "CouncilPercentageOfAll_DisabledChildPremiumAward";
    // EnhancedDisabilityPremiumAwards
    protected static final String sCouncilTotalCount_EnhancedDisabilityPremiumAward = "CouncilTotalCount_EnhancedDisabilityPremiumAward";
    protected static final String sCouncilPercentageOfAll_EnhancedDisabilityPremiumAward = "CouncilPercentageOfAll_EnhancedDisabilityPremiumAward";
    // DisabilityPremiumAwardHBTTs
    protected static final String sCouncilTotalCount_DisabilityPremiumAwardHBTTs = "CouncilTotalCount_DisabilityPremiumAwardHBTTs";
    protected static final String sCouncilPercentageOfAll_DisabilityPremiumAwardHBTTs = "CouncilPercentageOfAll_DisabilityPremiumAwardHBTTs";
    protected static final String sCouncilPercentageOfHB_DisabilityPremiumAwardHBTTs = "CouncilPercentageOfHB_DisabilityPremiumAwardHBTTs";
    // SevereDisabilityPremiumAwardHBTTs
    protected static final String sCouncilTotalCount_SevereDisabilityPremiumAwardHBTTs = "CouncilTotalCount_SevereDisabilityPremiumAwardHBTTs";
    protected static final String sCouncilPercentageOfAll_SevereDisabilityPremiumAwardHBTTs = "CouncilPercentageOfAll_SevereDisabilityPremiumAwardHBTTs";
    protected static final String sCouncilPercentageOfHB_SevereDisabilityPremiumAwardHBTTs = "CouncilPercentageOfHB_SevereDisabilityPremiumAwardHBTTs";
    // DisabledChildPremiumAwardHBTTs
    protected static final String sCouncilTotalCount_DisabledChildPremiumAwardHBTTs = "CouncilTotalCount_DisabledChildPremiumAwardHBTTs";
    protected static final String sCouncilPercentageOfAll_DisabledChildPremiumAwardHBTTs = "CouncilPercentageOfAll_DisabledChildPremiumAwardHBTTs";
    protected static final String sCouncilPercentageOfHB_DisabledChildPremiumAwardHBTTs = "CouncilPercentageOfHB_DisabledChildPremiumAwardHBTTs";
    // EnhancedDisabilityPremiumAwardHBTTs
    protected static final String sCouncilTotalCount_EnhancedDisabilityPremiumAwardHBTTs = "CouncilTotalCount_EnhancedDisabilityPremiumAwardHBTTs";
    protected static final String sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs = "CouncilPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs";
    protected static final String sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs = "CouncilPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs";
    // DisabilityPremiumAwardCTBTTs
    protected static final String sCouncilTotalCount_DisabilityPremiumAwardCTBTTs = "CouncilTotalCount_DisabilityPremiumAwardCTBTTs";
    protected static final String sCouncilPercentageOfAll_DisabilityPremiumAwardCTBTTs = "CouncilPercentageOfAll_DisabilityPremiumAwardCTBTTs";
    protected static final String sCouncilPercentageOfCTB_DisabilityPremiumAwardCTBTTs = "CouncilPercentageOfCTB_DisabilityPremiumAwardCTBTTs";
    // SevereDisabilityPremiumAwardCTBTTs
    protected static final String sCouncilTotalCount_SevereDisabilityPremiumAwardCTBTTs = "CouncilTotalCount_SevereDisabilityPremiumAwardCTBTTs";
    protected static final String sCouncilPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs = "CouncilPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs";
    protected static final String sCouncilPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs = "CouncilPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs";
    // DisabledChildPremiumAwardCTBTTs
    protected static final String sCouncilTotalCount_DisabledChildPremiumAwardCTBTTs = "CouncilTotalCount_DisabledChildPremiumAwardCTBTTs";
    protected static final String sCouncilPercentageOfAll_DisabledChildPremiumAwardCTBTTs = "CouncilPercentageOfAll_DisabledChildPremiumAwardCTBTTs";
    protected static final String sCouncilPercentageOfCTB_DisabledChildPremiumAwardCTBTTs = "CouncilPercentageOfCTB_DisabledChildPremiumAwardCTBTTs";
    // EnhancedDisabilityPremiumAwardCTBTTs
    protected static final String sCouncilTotalCount_EnhancedDisabilityPremiumAwardCTBTTs = "CouncilTotalCount_EnhancedDisabilityPremiumAwardCTBTTs";
    protected static final String sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs = "CouncilPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs";
    protected static final String sCouncilPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs = "CouncilPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs";
    // DisabilityPremiumAwardSocialTTs
    protected static final String sCouncilTotalCount_DisabilityPremiumAwardSocialTTs = "CouncilTotalCount_DisabilityPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfAll_DisabilityPremiumAwardSocialTTs = "CouncilPercentageOfAll_DisabilityPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfHB_DisabilityPremiumAwardSocialTTs = "CouncilPercentageOfHB_DisabilityPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs = "CouncilPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs";
    // DisabilityPremiumAwardPrivateDeregulatedTTs
    protected static final String sCouncilTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs = "CouncilTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs = "CouncilsPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs = "CouncilsPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs = "CouncilPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs";
    // SevereDisabilityPremiumAwardSocialTTs
    protected static final String sCouncilTotalCount_SevereDisabilityPremiumAwardSocialTTs = "CouncilTotalCount_SevereDisabilityPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs = "CouncilPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs = "CouncilPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs = "CouncilPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs";
    // SevereDisabilityPremiumAwardPrivateDeregulatedTTs
    protected static final String sCouncilTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "CouncilTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "CouncilsPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "CouncilsPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "CouncilPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    // DisabledChildPremiumAwardSocialTTs
    protected static final String sCouncilTotalCount_DisabledChildPremiumAwardSocialTTs = "CouncilTotalCount_DisabledChildPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfAll_DisabledChildPremiumAwardSocialTTs = "CouncilPercentageOfAll_DisabledChildPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfHB_DisabledChildPremiumAwardSocialTTs = "CouncilPercentageOfHB_DisabledChildPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs = "CouncilPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs";
    // DisabledChildPremiumAwardPrivateDeregulatedTTs
    protected static final String sCouncilTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs = "CouncilTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs = "CouncilsPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs = "CouncilsPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs = "CouncilPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    // EnhancedDisabilityPremiumAwardSocialTTs
    protected static final String sCouncilTotalCount_EnhancedDisabilityPremiumAwardSocialTTs = "CouncilTotalCount_EnhancedDisabilityPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs = "CouncilPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs = "CouncilPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs = "CouncilPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs";
    // EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs
    protected static final String sCouncilTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "CouncilTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "CouncilsPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "CouncilsPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "CouncilPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    // DisabilityAwardByTT
    protected static String[] sCouncilTotalCount_DisabilityAwardByTT;
    protected static String[] sCouncilPercentageOfAll_DisabilityAwardByTT;
    protected static String[] sCouncilPercentageOfHB_DisabilityAwardByTT;
    protected static String[] sCouncilPercentageOfCTB_DisabilityAwardByTT;
    protected static String[] sCouncilPercentageOfTT_DisabilityAwardByTT;
    // DisabilityAwardHBTTs
    protected static final String sCouncilTotalCount_DisabilityAwardHBTTs = "CouncilTotalCount_DisabilityAwardHBTTs";
    protected static final String sCouncilPercentageOfAll_DisabilityAwardHBTTs = "CouncilPercentageOfAll_DisabilityAwardHBTTs";
    protected static final String sCouncilPercentageOfHB_DisabilityAwardHBTTs = "CouncilPercentageOfHB_DisabilityAwardHBTTs";
    // DisabilityAwardCTBTTs
    protected static final String sCouncilTotalCount_DisabilityAwardCTBTTs = "CouncilTotalCount_DisabilityAwardCTBTTs";
    protected static final String sCouncilPercentageOfAll_DisabilityAwardCTBTTs = "CouncilPercentageOfAll_DisabilityAwardCTBTTs";
    protected static final String sCouncilPercentageOfCTB_DisabilityAwardCTBTTs = "CouncilPercentageOfCTB_DisabilityAwardCTBTTs";
    // DisabilityAwardSocialTTs
    protected static final String sCouncilTotalCount_DisabilityAwardSocialTTs = "CouncilTotalCount_DisabilityAwardSocialTTs";
    protected static final String sCouncilPercentageOfAll_DisabilityAwardSocialTTs = "CouncilPercentageOfAll_DisabilityAwardSocialTTs";
    protected static final String sCouncilPercentageOfHB_DisabilityAwardSocialTTs = "CouncilPercentageOfHB_DisabilityAwardSocialTTs";
    protected static final String sCouncilPercentageOfSocialTTs_DisabilityAwardSocialTTs = "CouncilsPercentageOfSocialTTs_DisabilityAwardSocialTTs";
    // DisabilityAwardPrivateDeregulatedTTs
    protected static final String sCouncilTotalCount_DisabilityAwardPrivateDeregulatedTTs = "CouncilTotalCount_DisabilityAwardPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs = "CouncilsPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs = "CouncilsPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs = "CouncilPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs";
    // HBEntitlement
    public static final String sCouncilTotalWeeklyHBEntitlement = "CouncilTotalWeeklyHBEntitlement";
    public static final String sCouncilTotalCount_WeeklyHBEntitlementNonZero = "CouncilTotalCount_WeeklyHBEntitlementNonZero";
    public static final String sCouncilTotalCount_WeeklyHBEntitlementZero = "CouncilTotalCount_WeeklyHBEntitlementZero";
    public static final String sCouncilAverageWeeklyHBEntitlement = "CouncilAverageWeeklyHBEntitlement";
    // CTBEntitlement
    public static final String sCouncilTotalWeeklyCTBEntitlement = "CouncilTotalWeeklyCTBEntitlement";
    public static final String sCouncilTotalCount_WeeklyCTBEntitlementNonZero = "CouncilTotalCount_WeeklyCTBEntitlementNonZero";
    public static final String sCouncilTotalCount_WeeklyCTBEntitlementZero = "CouncilTotalCount_WeeklyCTBEntitlementZero";
    public static final String sCouncilAverageWeeklyCTBEntitlement = "CouncilAverageWeeklyCTBEntitlement";
    // WeeklyEligibleRentAmount
    public static final String sCouncilAllTotalWeeklyEligibleRentAmount = "CouncilAllTotalWeeklyEligibleRentAmount";
    public static String[] sCouncilTotalWeeklyEligibleRentAmountTT;
    public static final String sCouncilAllTotalCount_WeeklyEligibleRentAmountNonZero = "CouncilAllTotalCount_WeeklyEligibleRentAmountNonZero";
    public static String[] sCouncilTotalCount_WeeklyEligibleRentAmountNonZeroTT;
    public static final String sCouncilAllTotalCount_WeeklyEligibleRentAmountZero = "CouncilAllTotalCount_WeeklyEligibleRentAmountZero";
    public static String[] sCouncilTotalCount_WeeklyEligibleRentAmountZeroTT;
    public static final String sCouncilAllAverageWeeklyEligibleRentAmount = "CouncilAllAverageWeeklyEligibleRentAmount";
    public static String[] sCouncilAverageWeeklyEligibleRentAmountTT;
    // WeeklyHBEntitlement
    public static final String sCouncilHBTotalWeeklyHBEntitlement = "CouncilHBTotalWeeklyHBEntitlement";
    public static final String sCouncilHBTotalCount_WeeklyHBEntitlementNonZero = "CouncilHBTotalCount_WeeklyHBEntitlementNonZero";
    public static final String sCouncilHBTotalCount_WeeklyHBEntitlementZero = "CouncilHBTotalCount_WeeklyHBEntitlementZero";
    public static final String sCouncilHBAverageWeeklyHBEntitlement = "CouncilHBAverageWeeklyHBEntitlement";
    public static final String sCouncilCTBTotalWeeklyHBEntitlement = "CouncilCTBTotalWeeklyHBEntitlement";
    public static final String sCouncilCTBTotalCount_WeeklyHBEntitlementNonZero = "CouncilCTBTotalCount_WeeklyHBEntitlementNonZero";
    public static final String sCouncilCTBTotalCount_WeeklyHBEntitlementZero = "CouncilCTBTotalCount_WeeklyHBEntitlementZero";
    public static final String sCouncilCTBAverageWeeklyHBEntitlement = "CouncilCTBAverageWeeklyHBEntitlement";
    // WeeklyCTBEntitlement
    public static final String sCouncilHBTotalWeeklyCTBEntitlement = "CouncilHBTotalWeeklyCTBEntitlement";
    public static final String sCouncilHBTotalCount_WeeklyCTBEntitlementNonZero = "CouncilHBTotalCount_WeeklyCTBEntitlementNonZero";
    public static final String sCouncilHBTotalCount_WeeklyCTBEntitlementZero = "CouncilHBTotalCount_WeeklyCTBEntitlementZero";
    public static final String sCouncilHBAverageWeeklyCTBEntitlement = "CouncilHBAverageWeeklyCTBEntitlement";
    public static final String sCouncilCTBTotalWeeklyCTBEntitlement = "CouncilCTBTotalWeeklyCTBEntitlement";
    public static final String sCouncilCTBTotalCount_WeeklyCTBEntitlementNonZero = "CouncilCTBTotalCount_WeeklyCTBEntitlementNonZero";
    public static final String sCouncilCTBTotalCount_WeeklyCTBEntitlementZero = "CouncilCTBTotalCount_WeeklyCTBEntitlementZero";
    public static final String sCouncilCTBAverageWeeklyCTBEntitlement = "CouncilCTBAverageWeeklyCTBEntitlement";
    // WeeklyEligibleRentAmount
    public static final String sCouncilHBTotalWeeklyEligibleRentAmount = "CouncilHBTotalWeeklyEligibleRentAmount";
    public static final String sCouncilHBTotalCount_WeeklyEligibleRentAmountNonZero = "CouncilHBTotalCount_WeeklyEligibleRentAmountNonZero";
    public static final String sCouncilHBTotalCount_WeeklyEligibleRentAmountZero = "CouncilHBTotalCount_WeeklyEligibleRentAmountZero";
    public static final String sCouncilHBAverageWeeklyEligibleRentAmount = "CouncilHBAverageWeeklyEligibleRentAmount";
    public static final String sCouncilCTBTotalWeeklyEligibleRentAmount = "CouncilCTBTotalWeeklyEligibleRentAmount";
    public static final String sCouncilCTBTotalCount_WeeklyEligibleRentAmountNonZero = "CouncilCTBTotalCount_WeeklyEligibleRentAmountNonZero";
    public static final String sCouncilCTBTotalCount_WeeklyEligibleRentAmountZero = "CouncilCTBTotalCount_WeeklyEligibleRentAmountZero";
    public static final String sCouncilCTBAverageWeeklyEligibleRentAmount = "CouncilCTBAverageWeeklyEligibleRentAmount";
    // WeeklyEligibleCouncilTaxAmount
    protected static final String sCouncilAllTotalWeeklyEligibleCouncilTaxAmount = "CouncilAllTotalWeeklyEligibleCouncilTaxAmount";
    protected static final String sCouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = "CouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero";
    protected static final String sCouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountZero = "CouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountZero";
    protected static final String sCouncilAllAverageWeeklyEligibleCouncilTaxAmount = "CouncilAllAverageWeeklyEligibleCouncilTaxAmount";
    protected static final String sCouncilHBTotalWeeklyEligibleCouncilTaxAmount = "CouncilHBTotalCount_WeeklyEligibleCouncilTaxAmount";
    protected static final String sCouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = "CouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero";
    protected static final String sCouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountZero = "CouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountZero";
    protected static final String sCouncilHBAverageWeeklyEligibleCouncilTaxAmount = "CouncilHBAverageWeeklyEligibleCouncilTaxAmount";
    protected static final String sCouncilCTBTotalWeeklyEligibleCouncilTaxAmount = "CouncilCTBTotalWeeklyEligibleCouncilTaxAmount";
    protected static final String sCouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = "CouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero";
    protected static final String sCouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero = "CouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero";
    protected static final String sCouncilCTBAverageWeeklyEligibleCouncilTaxAmount = "CouncilCTBAverageWeeklyEligibleCouncilTaxAmount";
    // ContractualRentAmount
    protected static final String sCouncilAllTotalContractualRentAmount = "CouncilAllTotalContractualRentAmount";
    protected static final String sCouncilAllTotalCountContractualRentAmountNonZeroCount = "CouncilAllTotalCount_ContractualRentAmountNonZero";
    protected static final String sCouncilAllTotalCountContractualRentAmountZeroCount = "CouncilAllTotalCount_ContractualRentAmountZero";
    protected static final String sCouncilAllAverageContractualRentAmount = "CouncilAllAverageContractualRentAmount";
    protected static final String sCouncilHBTotalContractualRentAmount = "CouncilHBTotalContractualRentAmount";
    protected static final String sCouncilHBTotalCountContractualRentAmountNonZeroCount = "CouncilHBTotalCount_ContractualRentAmountNonZero";
    protected static final String sCouncilHBTotalCountContractualRentAmountZeroCount = "CouncilHBTotalCount_ContractualRentAmountZero";
    protected static final String sCouncilHBAverageContractualRentAmount = "CouncilHBAverageContractualRentAmount";
    protected static final String sCouncilCTBTotalContractualRentAmount = "CouncilCTBTotalContractualRentAmount";
    protected static final String sCouncilCTBTotalCountContractualRentAmountNonZeroCount = "CouncilCTBTotalCount_ContractualRentAmountNonZero";
    protected static final String sCouncilCTBTotalCountContractualRentAmountZeroCount = "CouncilCTBTotalCount_ContractualRentAmountZero";
    protected static final String sCouncilCTBAverageContractualRentAmount = "CouncilCTBAverageContractualRentAmount";
    // WeeklyAdditionalDiscretionaryPayment
    protected static final String sCouncilAllTotalWeeklyAdditionalDiscretionaryPayment = "CouncilAllTotalWeeklyAdditionalDiscretionaryPayment";
    protected static final String sCouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero = "CouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero";
    protected static final String sCouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero = "CouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero";
    protected static final String sCouncilAllAverageWeeklyAdditionalDiscretionaryPayment = "CouncilAllAverageWeeklyAdditionalDiscretionaryPayment";
    protected static final String sCouncilHBTotalWeeklyAdditionalDiscretionaryPayment = "CouncilHBTotalWeeklyAdditionalDiscretionaryPayment";
    protected static final String sCouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero = "CouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero";
    protected static final String sCouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero = "CouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero";
    protected static final String sCouncilHBAverageWeeklyAdditionalDiscretionaryPayment = "CouncilHBAverageWeeklyAdditionalDiscretionaryPayment";
    protected static final String sCouncilCTBTotalWeeklyAdditionalDiscretionaryPayment = "CouncilCTBTotalWeeklyAdditionalDiscretionaryPayment";
    protected static final String sCouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero = "CouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero";
    protected static final String sCouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero = "CouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero";
    protected static final String sCouncilCTBAverageWeeklyAdditionalDiscretionaryPayment = "CouncilCTBAverageWeeklyAdditionalDiscretionaryPayment";
    // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
    protected static final String sCouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected static final String sCouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "CouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    protected static final String sCouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "CouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    protected static final String sCouncilAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "CouncilAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected static final String sCouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected static final String sCouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "CouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    protected static final String sCouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "CouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    protected static final String sCouncilHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "CouncilHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected static final String sCouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected static final String sCouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "CouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    protected static final String sCouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "CouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    protected static final String sCouncilCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "CouncilCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    // Employment
    protected static final String sCouncilAllTotalCount_ClaimantsEmployed = "CouncilAllTotalCount_ClaimantsEmployed";
    protected static final String sCouncilAllPercentage_ClaimantsEmployed = "CouncilAllPercentage_ClaimantsEmployed";
    protected static final String sCouncilAllTotalCount_ClaimantsSelfEmployed = "CouncilAllTotalCount_ClaimantsSelfEmployed";
    protected static final String sCouncilAllPercentage_ClaimantsSelfEmployed = "CouncilAllPercentage_ClaimantsSelfEmployed";
    protected static final String sCouncilAllTotalCount_ClaimantsStudents = "CouncilAllTotalCount_ClaimantsStudents";
    protected static final String sCouncilAllPercentage_ClaimantsStudents = "CouncilAllPercentage_ClaimantsStudents";
    protected static final String sCouncilAllTotalCount_LHACases = "CouncilAllTotalCount_LHACases";
    protected static final String sCouncilAllPercentageOfAll_LHACases = "CouncilAllPercentageOfHB_LHACases";
    protected static final String sCouncilHBTotalCount_ClaimantsEmployed = "CouncilHBTotalCount_ClaimantsEmployed";
    protected static final String sCouncilHBPercentageOfHB_ClaimantsEmployed = "CouncilHBPercentageOfHB_ClaimantsEmployed";
    protected static final String sCouncilHBTotalCountClaimantsSelfEmployed = "CouncilHBTotalCount_ClaimantsSelfEmployed";
    protected static final String sCouncilHBPercentageOfHB_ClaimantsSelfEmployed = "CouncilHBPercentageOfHB_ClaimantsSelfEmployed";
    protected static final String sCouncilHBTotalCountClaimantsStudents = "CouncilHBTotalCount_ClaimantsStudents";
    protected static final String sCouncilHBPercentageOfHB_ClaimantsStudents = "CouncilHBPercentageOfHB_ClaimantsStudents";
    protected static final String sCouncilHBTotalCount_LHACases = "CouncilHBTotalCount_LHACases";
    protected static final String sCouncilHBPercentageOfHB_LHACases = "CouncilHBPercentageOfHB_LHACases";
    protected static final String sCouncilCTBTotalCount_ClaimantsEmployed = "CouncilCTBTotalCount_ClaimantsEmployed";
    protected static final String sCouncilCTBPercentageOfCTB_ClaimantsEmployed = "CouncilCTBPercentageOfCTB_ClaimantsEmployed";
    protected static final String sCouncilCTBTotalCountClaimantsSelfEmployed = "CouncilCTBTotalCountClaimantsSelfEmployed";
    protected static final String sCouncilCTBPercentageOfCTB_ClaimantsSelfEmployed = "CouncilCTBPercentageOfCTB_ClaimantsSelfEmployed";
    protected static final String sCouncilCTBTotalCountClaimantsStudents = "CouncilCTBTotalCountClaimantsStudents";
    protected static final String sCouncilCTBPercentageOfCTB_ClaimantsStudents = "CouncilCTBPercentageOfCTB_ClaimantsStudents";
    protected static final String sCouncilCTBTotalCount_LHACases = "CouncilCTBTotalCountLHACases";
    protected static final String sCouncilCTBPercentageOfCTB_LHACases = "CouncilCTBPercentageOfCTB_LHACases";
    // Counts
    protected static final String sCouncilAllCount0 = "CouncilAllCount0";
    protected static final String sCouncilHBCount0 = "CouncilHBCount0";
    protected static final String sCouncilCTBCount0 = "CouncilCTBOnlyCount0";
    protected static final String sCouncilAllCount1 = "CouncilAllCount1";
    protected static final String sCouncilHBCount1 = "CouncilHBCount1";
    protected static final String sCouncilCTBCount1 = "CouncilCTBOnlyCount1";
    protected static final String sCouncilTotalCount_SocialTTsClaimant = "CouncilTotalCount_SocialTTsClaimant";
    protected static final String sCouncilPercentageOfAll_SocialTTsClaimant = "CouncilPercentageOfAll_SocialTTsClaimant";
    protected static final String sCouncilPercentageOfHB_SocialTTsClaimant = "CouncilPercentageOfHB_SocialTTsClaimant";
    protected static final String sCouncilTotalCount_PrivateDeregulatedTTsClaimant = "CouncilTotalCount_PrivateDeregulatedTTsClaimant";
    protected static final String sCouncilPercentageOfAll_PrivateDeregulatedTTsClaimant = "CouncilPercentageOfAll_PrivateDeregulatedTTsClaimant";
    protected static final String sCouncilPercentageOfHB_PrivateDeregulatedTTsClaimant = "CouncilPercentageOfHB_PrivateDeregulatedTTsClaimant";
    protected static String[] sCouncilAllTotalCount_EthnicGroupClaimant;
    protected static String[] sCouncilAllPercentageOfAll_EthnicGroupClaimant;
    protected static String sCouncilAllTotalCount_PostcodeValidFormat;
    protected static String sCouncilAllTotalCount_PostcodeValid;
    // Income
    // All
    public static final String sCouncilAllTotalIncome = "CouncilAllTotalIncome";
    public static String[] sCouncilTotalIncomeTT;
    public static final String sCouncilAllTotalCount_IncomeNonZero = "CouncilAllTotalCount_IncomeNonZero";
    public static final String sCouncilAllTotalCount_IncomeZero = "CouncilAllTotalCount_IncomeZero";
    public static String[] sCouncilTotalCount_IncomeNonZeroTT;
    public static String[] sCouncilTotalCount_IncomeZeroTT;
    public static final String sCouncilAllAverageIncome = "CouncilAllAverageIncome";
    public static String[] sCouncilAverageIncomeTT;
    //HB
    public static final String sCouncilHBTotalIncome = "CouncilHBTotalIncome";
    public static final String sCouncilHBTotalCount_IncomeNonZero = "CouncilHBTotalCount_IncomeNonZero";
    public static final String sCouncilHBTotalCount_IncomeZero = "CouncilHBTotalCount_IncomeZero";
    public static final String sCouncilHBAverageIncome = "CouncilHBAverageIncome";
    // CTB
    public static final String sCouncilCTBTotalIncome = "CouncilCTBTotalIncome";
    public static final String sCouncilCTBTotalCount_IncomeNonZero = "CouncilCTBTotalCount_IncomeNonZero";
    public static final String sCouncilCTBTotalCount_IncomeZero = "CouncilCTBTotalCount_IncomeZero";
    public static final String sCouncilCTBAverageIncome = "CouncilCTBAverageIncome";

    // RSL
    // Counter Strings
    // HouseholdSize
    protected static final String sRSLAllTotalHouseholdSize = "RSLAllTotalHouseholdSize";
    protected static final String sRSLAllAverageHouseholdSize = "RSLAllAverageHouseholdSize";
    protected static final String sRSLHBTotalHouseholdSize = "RSLHBTotalHouseholdSize";
    protected static final String sRSLHBAverageHouseholdSize = "RSLHBAverageHouseholdSize";
    protected static final String sRSLCTBTotalHouseholdSize = "RSLCTBTotalHouseholdSize";
    protected static final String sRSLCTBAverageHouseholdSize = "RSLCTBAverageHouseholdSize";
    // PSI
    protected static String[] sRSLAllTotalCount_PSI;
    protected static String[] sRSLHBTotalCount_PSI;
    protected static String[] sRSLCTBTotalCount_PSI;
    protected static String[] sRSLAllPercentageOfAll_PSI;
    protected static String[] sRSLHBPercentageOfHB_PSI;
    protected static String[] sRSLCTBPercentageOfCTB_PSI;
    // PSIByTT
    protected static String[][] sRSLTotalCount_PSIByTT;
    protected static String[][] sRSLPercentageOfAll_PSIByTT;
    protected static String[][] sRSLPercentageOfHB_PSIByTT;
    protected static String[][] sRSLPercentageOfCTB_PSIByTT;
    // DisabilityPremiumAwardByTT
    protected static String[] sRSLTotalCount_DisabilityPremiumAwardByTT;
    protected static String[] sRSLPercentageOfAll_DisabilityPremiumAwardByTT;
    protected static String[] sRSLPercentageOfHB_DisabilityPremiumAwardByTT;
    protected static String[] sRSLPercentageOfCTB_DisabilityPremiumAwardByTT;
    protected static String[] sRSLPercentageOfTT_DisabilityPremiumAwardByTT;
    // SevereDisabilityPremiumAwardByTT
    protected static String[] sRSLTotalCount_SevereDisabilityPremiumAwardByTT;
    protected static String[] sRSLPercentageOfAll_SevereDisabilityPremiumAwardByTT;
    protected static String[] sRSLPercentageOfHB_SevereDisabilityPremiumAwardByTT;
    protected static String[] sRSLPercentageOfCTB_SevereDisabilityPremiumAwardByTT;
    protected static String[] sRSLPercentageOfTT_SevereDisabilityPremiumAwardByTT;
    // DisabledChildPremiumAwardByTT
    protected static String[] sRSLTotalCount_DisabledChildPremiumAwardByTT;
    protected static String[] sRSLPercentageOfAll_DisabledChildPremiumAwardByTT;
    protected static String[] sRSLPercentageOfHB_DisabledChildPremiumAwardByTT;
    protected static String[] sRSLPercentageOfCTB_DisabledChildPremiumAwardByTT;
    protected static String[] sRSLPercentageOfTT_DisabledChildPremiumAwardByTT;
    // EnhancedDisabilityPremiumAwardByTT
    protected static String[] sRSLTotalCount_EnhancedDisabilityPremiumAwardByTT;
    protected static String[] sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardByTT;
    protected static String[] sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardByTT;
    protected static String[] sRSLPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT;
    protected static String[] sRSLPercentageOfTT_EnhancedDisabilityPremiumAwardByTT;
    // DisabilityAwards
    protected static final String sRSLTotalCount_DisabilityAward = "RSLTotalCount_DisabilityAward";
    protected static final String sRSLPercentageOfAll_DisabilityAward = "RSLPercentageOfAll_DisabilityAward";
    // DisabilityPremiumAwards
    protected static final String sRSLTotalCount_DisabilityPremiumAward = "RSLTotalCount_DisabilityPremiumAward";
    protected static final String sRSLPercentageOfAll_DisabilityPremiumAward = "RSLPercentageOfAll_DisabilityPremiumAward";
    // SevereDisabilityPremiumAwards
    protected static final String sRSLTotalCount_SevereDisabilityPremiumAward = "RSLTotalCount_SevereDisabilityPremiumAward";
    protected static final String sRSLPercentageOfAll_SevereDisabilityPremiumAward = "RSLPercentageOfAll_SevereDisabilityPremiumAward";
    // DisabledChildPremiumAwards
    protected static final String sRSLTotalCount_DisabledChildPremiumAward = "RSLTotalCount_DisabledChildPremiumAward";
    protected static final String sRSLPercentageOfAll_DisabledChildPremiumAward = "RSLPercentageOfAll_DisabledChildPremiumAward";
    // EnhancedDisabilityPremiumAwards
    protected static final String sRSLTotalCount_EnhancedDisabilityPremiumAward = "RSLTotalCount_EnhancedDisabilityPremiumAward";
    protected static final String sRSLPercentageOfAll_EnhancedDisabilityPremiumAward = "RSLPercentageOfAll_EnhancedDisabilityPremiumAward";
    // DisabilityPremiumAwardHBTTs
    protected static final String sRSLTotalCount_DisabilityPremiumAwardHBTTs = "RSLTotalCount_DisabilityPremiumAwardHBTTs";
    protected static final String sRSLPercentageOfAll_DisabilityPremiumAwardHBTTs = "RSLPercentageOfAll_DisabilityPremiumAwardHBTTs";
    protected static final String sRSLPercentageOfHB_DisabilityPremiumAwardHBTTs = "RSLPercentageOfHB_DisabilityPremiumAwardHBTTs";
    // SevereDisabilityPremiumAwardHBTTs
    protected static final String sRSLTotalCount_SevereDisabilityPremiumAwardHBTTs = "RSLTotalCount_SevereDisabilityPremiumAwardHBTTs";
    protected static final String sRSLPercentageOfAll_SevereDisabilityPremiumAwardHBTTs = "RSLPercentageOfAll_SevereDisabilityPremiumAwardHBTTs";
    protected static final String sRSLPercentageOfHB_SevereDisabilityPremiumAwardHBTTs = "RSLPercentageOfHB_SevereDisabilityPremiumAwardHBTTs";
    // DisabledChildPremiumAwardHBTTs
    protected static final String sRSLTotalCount_DisabledChildPremiumAwardHBTTs = "RSLTotalCount_DisabledChildPremiumAwardHBTTs";
    protected static final String sRSLPercentageOfAll_DisabledChildPremiumAwardHBTTs = "RSLPercentageOfAll_DisabledChildPremiumAwardHBTTs";
    protected static final String sRSLPercentageOfHB_DisabledChildPremiumAwardHBTTs = "RSLPercentageOfHB_DisabledChildPremiumAwardHBTTs";
    // EnhancedDisabilityPremiumAwardHBTTs
    protected static final String sRSLTotalCount_EnhancedDisabilityPremiumAwardHBTTs = "RSLTotalCount_EnhancedDisabilityPremiumAwardHBTTs";
    protected static final String sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs = "RSLPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs";
    protected static final String sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs = "RSLPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs";
    // DisabilityPremiumAwardCTBTTs
    protected static final String sRSLTotalCount_DisabilityPremiumAwardCTBTTs = "RSLTotalCount_DisabilityPremiumAwardCTBTTs";
    protected static final String sRSLPercentageOfAll_DisabilityPremiumAwardCTBTTs = "RSLPercentageOfAll_DisabilityPremiumAwardCTBTTs";
    protected static final String sRSLPercentageOfCTB_DisabilityPremiumAwardCTBTTs = "RSLPercentageOfCTB_DisabilityPremiumAwardCTBTTs";
    // SevereDisabilityPremiumAwardCTBTTs
    protected static final String sRSLTotalCount_SevereDisabilityPremiumAwardCTBTTs = "RSLTotalCount_SevereDisabilityPremiumAwardCTBTTs";
    protected static final String sRSLPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs = "RSLPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs";
    protected static final String sRSLPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs = "RSLPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs";
    // DisabledChildPremiumAwardCTBTTs
    protected static final String sRSLTotalCount_DisabledChildPremiumAwardCTBTTs = "RSLTotalCount_DisabledChildPremiumAwardCTBTTs";
    protected static final String sRSLPercentageOfAll_DisabledChildPremiumAwardCTBTTs = "RSLPercentageOfAll_DisabledChildPremiumAwardCTBTTs";
    protected static final String sRSLPercentageOfCTB_DisabledChildPremiumAwardCTBTTs = "RSLPercentageOfCTB_DisabledChildPremiumAwardCTBTTs";
    // EnhancedDisabilityPremiumAwardCTBTTs
    protected static final String sRSLTotalCount_EnhancedDisabilityPremiumAwardCTBTTs = "RSLTotalCount_EnhancedDisabilityPremiumAwardCTBTTs";
    protected static final String sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs = "RSLPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs";
    protected static final String sRSLPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs = "RSLPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs";
    // DisabilityPremiumAwardSocialTTs
    protected static final String sRSLTotalCount_DisabilityPremiumAwardSocialTTs = "RSLTotalCount_DisabilityPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfAll_DisabilityPremiumAwardSocialTTs = "RSLPercentageOfAll_DisabilityPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfHB_DisabilityPremiumAwardSocialTTs = "RSLPercentageOfHB_DisabilityPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs = "RSLPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs";
    // DisabilityPremiumAwardPrivateDeregulatedTTs
    protected static final String sRSLTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs = "RSLTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs = "RSLsPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs = "RSLsPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs = "RSLPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs";
    // SevereDisabilityPremiumAwardSocialTTs
    protected static final String sRSLTotalCount_SevereDisabilityPremiumAwardSocialTTs = "RSLTotalCount_SevereDisabilityPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs = "RSLPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs = "RSLPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs = "RSLPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs";
    // SevereDisabilityPremiumAwardPrivateDeregulatedTTs
    protected static final String sRSLTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "RSLTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "RSLsPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "RSLsPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "RSLPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    // DisabledChildPremiumAwardSocialTTs
    protected static final String sRSLTotalCount_DisabledChildPremiumAwardSocialTTs = "RSLTotalCount_DisabledChildPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfAll_DisabledChildPremiumAwardSocialTTs = "RSLPercentageOfAll_DisabledChildPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfHB_DisabledChildPremiumAwardSocialTTs = "RSLPercentageOfHB_DisabledChildPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs = "RSLPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs";
    // DisabledChildPremiumAwardPrivateDeregulatedTTs
    protected static final String sRSLTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs = "RSLTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs = "RSLsPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs = "RSLsPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs = "RSLPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    // EnhancedDisabilityPremiumAwardSocialTTs
    protected static final String sRSLTotalCount_EnhancedDisabilityPremiumAwardSocialTTs = "RSLTotalCount_EnhancedDisabilityPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs = "RSLPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs = "RSLPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs = "RSLPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs";
    // EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs
    protected static final String sRSLTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "RSLTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "RSLsPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "RSLsPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "RSLPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    // DisabilityAwardByTT
    protected static String[] sRSLTotalCount_DisabilityAwardByTT;
    protected static String[] sRSLPercentageOfAll_DisabilityAwardByTT;
    protected static String[] sRSLPercentageOfHB_DisabilityAwardByTT;
    protected static String[] sRSLPercentageOfCTB_DisabilityAwardByTT;
    protected static String[] sRSLPercentageOfTT_DisabilityAwardByTT;
    // DisabilityAwardHBTTs
    protected static final String sRSLTotalCount_DisabilityAwardHBTTs = "RSLTotalCount_DisabilityAwardHBTTs";
    protected static final String sRSLPercentageOfAll_DisabilityAwardHBTTs = "RSLPercentageOfAll_DisabilityAwardHBTTs";
    protected static final String sRSLPercentageOfHB_DisabilityAwardHBTTs = "RSLPercentageOfHB_DisabilityAwardHBTTs";
    // DisabilityAwardCTBTTs
    protected static final String sRSLTotalCount_DisabilityAwardCTBTTs = "RSLTotalCount_DisabilityAwardCTBTTs";
    protected static final String sRSLPercentageOfAll_DisabilityAwardCTBTTs = "RSLPercentageOfAll_DisabilityAwardCTBTTs";
    protected static final String sRSLPercentageOfCTB_DisabilityAwardCTBTTs = "RSLPercentageOfCTB_DisabilityAwardCTBTTs";
    // DisabilityAwardSocialTTs
    protected static final String sRSLTotalCount_DisabilityAwardSocialTTs = "RSLTotalCount_DisabilityAwardSocialTTs";
    protected static final String sRSLPercentageOfAll_DisabilityAwardSocialTTs = "RSLPercentageOfAll_DisabilityAwardSocialTTs";
    protected static final String sRSLPercentageOfHB_DisabilityAwardSocialTTs = "RSLPercentageOfHB_DisabilityAwardSocialTTs";
    protected static final String sRSLPercentageOfSocialTTs_DisabilityAwardSocialTTs = "RSLsPercentageOfSocialTTs_DisabilityAwardSocialTTs";
    // DisabilityAwardPrivateDeregulatedTTs
    protected static final String sRSLTotalCount_DisabilityAwardPrivateDeregulatedTTs = "RSLTotalCount_DisabilityAwardPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs = "RSLsPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs = "RSLsPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs = "RSLPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs";
    // HBEntitlement
    public static final String sRSLTotalWeeklyHBEntitlement = "RSLTotalWeeklyHBEntitlement";
    public static final String sRSLTotalCount_WeeklyHBEntitlementNonZero = "RSLTotalCount_WeeklyHBEntitlementNonZero";
    public static final String sRSLTotalCount_WeeklyHBEntitlementZero = "RSLTotalCount_WeeklyHBEntitlementZero";
    public static final String sRSLAverageWeeklyHBEntitlement = "RSLAverageWeeklyHBEntitlement";
    // CTBEntitlement
    public static final String sRSLTotalWeeklyCTBEntitlement = "RSLTotalWeeklyCTBEntitlement";
    public static final String sRSLTotalCount_WeeklyCTBEntitlementNonZero = "RSLTotalCount_WeeklyCTBEntitlementNonZero";
    public static final String sRSLTotalCount_WeeklyCTBEntitlementZero = "RSLTotalCount_WeeklyCTBEntitlementZero";
    public static final String sRSLAverageWeeklyCTBEntitlement = "RSLAverageWeeklyCTBEntitlement";
    // WeeklyEligibleRentAmount
    public static final String sRSLAllTotalWeeklyEligibleRentAmount = "RSLAllTotalWeeklyEligibleRentAmount";
    public static String[] sRSLTotalWeeklyEligibleRentAmountTT;
    public static final String sRSLAllTotalCount_WeeklyEligibleRentAmountNonZero = "RSLAllTotalCount_WeeklyEligibleRentAmountNonZero";
    public static String[] sRSLTotalCount_WeeklyEligibleRentAmountNonZeroTT;
    public static final String sRSLAllTotalCount_WeeklyEligibleRentAmountZero = "RSLAllTotalCount_WeeklyEligibleRentAmountZero";
    public static String[] sRSLTotalCount_WeeklyEligibleRentAmountZeroTT;
    public static final String sRSLAllAverageWeeklyEligibleRentAmount = "RSLAllAverageWeeklyEligibleRentAmount";
    public static String[] sRSLAverageWeeklyEligibleRentAmountTT;
    // WeeklyHBEntitlement
    public static final String sRSLHBTotalWeeklyHBEntitlement = "RSLHBTotalWeeklyHBEntitlement";
    public static final String sRSLHBTotalCount_WeeklyHBEntitlementNonZero = "RSLHBTotalCount_WeeklyHBEntitlementNonZero";
    public static final String sRSLHBTotalCount_WeeklyHBEntitlementZero = "RSLHBTotalCount_WeeklyHBEntitlementZero";
    public static final String sRSLHBAverageWeeklyHBEntitlement = "RSLHBAverageWeeklyHBEntitlement";
    public static final String sRSLCTBTotalWeeklyHBEntitlement = "RSLCTBTotalWeeklyHBEntitlement";
    public static final String sRSLCTBTotalCount_WeeklyHBEntitlementNonZero = "RSLCTBTotalCount_WeeklyHBEntitlementNonZero";
    public static final String sRSLCTBTotalCount_WeeklyHBEntitlementZero = "RSLCTBTotalCount_WeeklyHBEntitlementZero";
    public static final String sRSLCTBAverageWeeklyHBEntitlement = "RSLCTBAverageWeeklyHBEntitlement";
    // WeeklyCTBEntitlement
    public static final String sRSLHBTotalWeeklyCTBEntitlement = "RSLHBTotalWeeklyCTBEntitlement";
    public static final String sRSLHBTotalCount_WeeklyCTBEntitlementNonZero = "RSLHBTotalCount_WeeklyCTBEntitlementNonZero";
    public static final String sRSLHBTotalCount_WeeklyCTBEntitlementZero = "RSLHBTotalCount_WeeklyCTBEntitlementZero";
    public static final String sRSLHBAverageWeeklyCTBEntitlement = "RSLHBAverageWeeklyCTBEntitlement";
    public static final String sRSLCTBTotalWeeklyCTBEntitlement = "RSLCTBTotalWeeklyCTBEntitlement";
    public static final String sRSLCTBTotalCount_WeeklyCTBEntitlementNonZero = "RSLCTBTotalCount_WeeklyCTBEntitlementNonZero";
    public static final String sRSLCTBTotalCount_WeeklyCTBEntitlementZero = "RSLCTBTotalCount_WeeklyCTBEntitlementZero";
    public static final String sRSLCTBAverageWeeklyCTBEntitlement = "RSLCTBAverageWeeklyCTBEntitlement";
    // WeeklyEligibleRentAmount
    public static final String sRSLHBTotalWeeklyEligibleRentAmount = "RSLHBTotalWeeklyEligibleRentAmount";
    public static final String sRSLHBTotalCount_WeeklyEligibleRentAmountNonZero = "RSLHBTotalCount_WeeklyEligibleRentAmountNonZero";
    public static final String sRSLHBTotalCount_WeeklyEligibleRentAmountZero = "RSLHBTotalCount_WeeklyEligibleRentAmountZero";
    public static final String sRSLHBAverageWeeklyEligibleRentAmount = "RSLHBAverageWeeklyEligibleRentAmount";
    public static final String sRSLCTBTotalWeeklyEligibleRentAmount = "RSLCTBTotalWeeklyEligibleRentAmount";
    public static final String sRSLCTBTotalCount_WeeklyEligibleRentAmountNonZero = "RSLCTBTotalCount_WeeklyEligibleRentAmountNonZero";
    public static final String sRSLCTBTotalCount_WeeklyEligibleRentAmountZero = "RSLCTBTotalCount_WeeklyEligibleRentAmountZero";
    public static final String sRSLCTBAverageWeeklyEligibleRentAmount = "RSLCTBAverageWeeklyEligibleRentAmount";
    // WeeklyEligibleCouncilTaxAmount
    protected static final String sRSLAllTotalWeeklyEligibleCouncilTaxAmount = "RSLAllTotalWeeklyEligibleCouncilTaxAmount";
    protected static final String sRSLAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = "RSLAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero";
    protected static final String sRSLAllTotalCount_WeeklyEligibleCouncilTaxAmountZero = "RSLAllTotalCount_WeeklyEligibleCouncilTaxAmountZero";
    protected static final String sRSLAllAverageWeeklyEligibleCouncilTaxAmount = "RSLAllAverageWeeklyEligibleCouncilTaxAmount";
    protected static final String sRSLHBTotalWeeklyEligibleCouncilTaxAmount = "RSLHBTotalCount_WeeklyEligibleCouncilTaxAmount";
    protected static final String sRSLHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = "RSLHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero";
    protected static final String sRSLHBTotalCount_WeeklyEligibleCouncilTaxAmountZero = "RSLHBTotalCount_WeeklyEligibleCouncilTaxAmountZero";
    protected static final String sRSLHBAverageWeeklyEligibleCouncilTaxAmount = "RSLHBAverageWeeklyEligibleCouncilTaxAmount";
    protected static final String sRSLCTBTotalWeeklyEligibleCouncilTaxAmount = "RSLCTBTotalWeeklyEligibleCouncilTaxAmount";
    protected static final String sRSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = "RSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero";
    protected static final String sRSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero = "RSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero";
    protected static final String sRSLCTBAverageWeeklyEligibleCouncilTaxAmount = "RSLCTBAverageWeeklyEligibleCouncilTaxAmount";
    // ContractualRentAmount
    protected static final String sRSLAllTotalContractualRentAmount = "RSLAllTotalContractualRentAmount";
    protected static final String sRSLAllTotalCountContractualRentAmountNonZeroCount = "RSLAllTotalCount_ContractualRentAmountNonZero";
    protected static final String sRSLAllTotalCountContractualRentAmountZeroCount = "RSLAllTotalCount_ContractualRentAmountZero";
    protected static final String sRSLAllAverageContractualRentAmount = "RSLAllAverageContractualRentAmount";
    protected static final String sRSLHBTotalContractualRentAmount = "RSLHBTotalContractualRentAmount";
    protected static final String sRSLHBTotalCountContractualRentAmountNonZeroCount = "RSLHBTotalCount_ContractualRentAmountNonZero";
    protected static final String sRSLHBTotalCountContractualRentAmountZeroCount = "RSLHBTotalCount_ContractualRentAmountZero";
    protected static final String sRSLHBAverageContractualRentAmount = "RSLHBAverageContractualRentAmount";
    protected static final String sRSLCTBTotalContractualRentAmount = "RSLCTBTotalContractualRentAmount";
    protected static final String sRSLCTBTotalCountContractualRentAmountNonZeroCount = "RSLCTBTotalCount_ContractualRentAmountNonZero";
    protected static final String sRSLCTBTotalCountContractualRentAmountZeroCount = "RSLCTBTotalCount_ContractualRentAmountZero";
    protected static final String sRSLCTBAverageContractualRentAmount = "RSLCTBAverageContractualRentAmount";
    // WeeklyAdditionalDiscretionaryPayment
    protected static final String sRSLAllTotalWeeklyAdditionalDiscretionaryPayment = "RSLAllTotalWeeklyAdditionalDiscretionaryPayment";
    protected static final String sRSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero = "RSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero";
    protected static final String sRSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero = "RSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero";
    protected static final String sRSLAllAverageWeeklyAdditionalDiscretionaryPayment = "RSLAllAverageWeeklyAdditionalDiscretionaryPayment";
    protected static final String sRSLHBTotalWeeklyAdditionalDiscretionaryPayment = "RSLHBTotalWeeklyAdditionalDiscretionaryPayment";
    protected static final String sRSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero = "RSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero";
    protected static final String sRSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero = "RSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero";
    protected static final String sRSLHBAverageWeeklyAdditionalDiscretionaryPayment = "RSLHBAverageWeeklyAdditionalDiscretionaryPayment";
    protected static final String sRSLCTBTotalWeeklyAdditionalDiscretionaryPayment = "RSLCTBTotalWeeklyAdditionalDiscretionaryPayment";
    protected static final String sRSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero = "RSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero";
    protected static final String sRSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero = "RSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero";
    protected static final String sRSLCTBAverageWeeklyAdditionalDiscretionaryPayment = "RSLCTBAverageWeeklyAdditionalDiscretionaryPayment";
    // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
    protected static final String sRSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "RSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected static final String sRSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "RSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    protected static final String sRSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "RSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    protected static final String sRSLAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "RSLAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected static final String sRSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "RSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected static final String sRSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "RSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    protected static final String sRSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "RSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    protected static final String sRSLHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "RSLHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected static final String sRSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected static final String sRSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "RSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    protected static final String sRSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "RSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    protected static final String sRSLCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "RSLCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    // Employment
    protected static final String sRSLAllTotalCount_ClaimantsEmployed = "RSLAllTotalCount_ClaimantsEmployed";
    protected static final String sRSLAllPercentage_ClaimantsEmployed = "RSLAllPercentage_ClaimantsEmployed";
    protected static final String sRSLAllTotalCount_ClaimantsSelfEmployed = "RSLAllTotalCount_ClaimantsSelfEmployed";
    protected static final String sRSLAllPercentage_ClaimantsSelfEmployed = "RSLAllPercentage_ClaimantsSelfEmployed";
    protected static final String sRSLAllTotalCount_ClaimantsStudents = "RSLAllTotalCount_ClaimantsStudents";
    protected static final String sRSLAllPercentage_ClaimantsStudents = "RSLAllPercentage_ClaimantsStudents";
    protected static final String sRSLAllTotalCount_LHACases = "RSLAllTotalCount_LHACases";
    protected static final String sRSLAllPercentageOfAll_LHACases = "RSLAllPercentageOfHB_LHACases";
    protected static final String sRSLHBTotalCount_ClaimantsEmployed = "RSLHBTotalCount_ClaimantsEmployed";
    protected static final String sRSLHBPercentageOfHB_ClaimantsEmployed = "RSLHBPercentageOfHB_ClaimantsEmployed";
    protected static final String sRSLHBTotalCountClaimantsSelfEmployed = "RSLHBTotalCount_ClaimantsSelfEmployed";
    protected static final String sRSLHBPercentageOfHB_ClaimantsSelfEmployed = "RSLHBPercentageOfHB_ClaimantsSelfEmployed";
    protected static final String sRSLHBTotalCountClaimantsStudents = "RSLHBTotalCount_ClaimantsStudents";
    protected static final String sRSLHBPercentageOfHB_ClaimantsStudents = "RSLHBPercentageOfHB_ClaimantsStudents";
    protected static final String sRSLHBTotalCount_LHACases = "RSLHBTotalCount_LHACases";
    protected static final String sRSLHBPercentageOfHB_LHACases = "RSLHBPercentageOfHB_LHACases";
    protected static final String sRSLCTBTotalCount_ClaimantsEmployed = "RSLCTBTotalCount_ClaimantsEmployed";
    protected static final String sRSLCTBPercentageOfCTB_ClaimantsEmployed = "RSLCTBPercentageOfCTB_ClaimantsEmployed";
    protected static final String sRSLCTBTotalCountClaimantsSelfEmployed = "RSLCTBTotalCountClaimantsSelfEmployed";
    protected static final String sRSLCTBPercentageOfCTB_ClaimantsSelfEmployed = "RSLCTBPercentageOfCTB_ClaimantsSelfEmployed";
    protected static final String sRSLCTBTotalCountClaimantsStudents = "RSLCTBTotalCountClaimantsStudents";
    protected static final String sRSLCTBPercentageOfCTB_ClaimantsStudents = "RSLCTBPercentageOfCTB_ClaimantsStudents";
    protected static final String sRSLCTBTotalCount_LHACases = "RSLCTBTotalCountLHACases";
    protected static final String sRSLCTBPercentageOfCTB_LHACases = "RSLCTBPercentageOfCTB_LHACases";
    // Counts
    protected static final String sRSLAllCount0 = "RSLAllCount0";
    protected static final String sRSLHBCount0 = "RSLHBCount0";
    protected static final String sRSLCTBCount0 = "RSLCTBOnlyCount0";
    protected static final String sRSLAllCount1 = "RSLAllCount1";
    protected static final String sRSLHBCount1 = "RSLHBCount1";
    protected static final String sRSLCTBCount1 = "RSLCTBOnlyCount1";
    protected static final String sRSLTotalCount_SocialTTsClaimant = "RSLTotalCount_SocialTTsClaimant";
    protected static final String sRSLPercentageOfAll_SocialTTsClaimant = "RSLPercentageOfAll_SocialTTsClaimant";
    protected static final String sRSLPercentageOfHB_SocialTTsClaimant = "RSLPercentageOfHB_SocialTTsClaimant";
    protected static final String sRSLTotalCount_PrivateDeregulatedTTsClaimant = "RSLTotalCount_PrivateDeregulatedTTsClaimant";
    protected static final String sRSLPercentageOfAll_PrivateDeregulatedTTsClaimant = "RSLPercentageOfAll_PrivateDeregulatedTTsClaimant";
    protected static final String sRSLPercentageOfHB_PrivateDeregulatedTTsClaimant = "RSLPercentageOfHB_PrivateDeregulatedTTsClaimant";
    protected static String[] sRSLAllTotalCount_EthnicGroupClaimant;
    protected static String[] sRSLAllPercentageOfAll_EthnicGroupClaimant;
    protected static String sRSLAllTotalCount_PostcodeValidFormat;
    protected static String sRSLAllTotalCount_PostcodeValid;
    // Income
    // All
    public static final String sRSLAllTotalIncome = "RSLAllTotalIncome";
    public static String[] sRSLTotalIncomeTT;
    public static final String sRSLAllTotalCount_IncomeNonZero = "RSLAllTotalCount_IncomeNonZero";
    public static final String sRSLAllTotalCount_IncomeZero = "RSLAllTotalCount_IncomeZero";
    public static String[] sRSLTotalCount_IncomeNonZeroTT;
    public static String[] sRSLTotalCount_IncomeZeroTT;
    public static final String sRSLAllAverageIncome = "RSLAllAverageIncome";
    public static String[] sRSLAverageIncomeTT;
    //HB
    public static final String sRSLHBTotalIncome = "RSLHBTotalIncome";
    public static final String sRSLHBTotalCount_IncomeNonZero = "RSLHBTotalCount_IncomeNonZero";
    public static final String sRSLHBTotalCount_IncomeZero = "RSLHBTotalCount_IncomeZero";
    public static final String sRSLHBAverageIncome = "RSLHBAverageIncome";
    // CTB
    public static final String sRSLCTBTotalIncome = "RSLCTBTotalIncome";
    public static final String sRSLCTBTotalCount_IncomeNonZero = "RSLCTBTotalCount_IncomeNonZero";
    public static final String sRSLCTBTotalCount_IncomeZero = "RSLCTBTotalCount_IncomeZero";
    public static final String sRSLCTBAverageIncome = "RSLCTBAverageIncome";

    // Files
    protected static final String sCouncilFilename00 = "CouncilFilename00";
    protected static final String sCouncilFilename0 = "CouncilFilename0";
    protected static final String sCouncilFilename1 = "CouncilFilename1";
    protected static final String sRSLFilename00 = "RSLFilename00";
    protected static final String sRSLFilename0 = "RSLFilename0";
    protected static final String sRSLFilename1 = "RSLFilename1";

    // Strings0
    // All
    protected static final String sAllUOCount00 = "AllUOCount00";
    protected static final String sAllUOCount0 = "AllUOCount0";
    protected static final String sAllUOCount1 = "AllUOCount1";
    protected static final String sAllUOLinkedRecordCount00 = "AllUOLinkedRecordCount00";
    protected static final String sAllUOLinkedRecordCount0 = "AllUOLinkedRecordCount0";
    protected static final String sAllUOLinkedRecordCount1 = "AllUOLinkedRecordCount1";
    // Council
    protected static final String sCouncilCount00 = "CouncilCount00";
    protected static final String sCouncilCount0 = "CouncilCount0";
    protected static final String sCouncilCount1 = "CouncilCount1";
    protected static final String sCouncilLinkedRecordCount00 = "CouncilLinkedRecordCount00";
    protected static final String sCouncilLinkedRecordCount0 = "CouncilLinkedRecordCount0";
    protected static final String sCouncilLinkedRecordCount1 = "CouncilLinkedRecordCount1";
    protected static final String sCouncilTotal_RentArrears = "CouncilTotal_RentArrears";
    protected static final String sCouncilTotalCount_RentArrears = "CouncilTotalCount_RentArrears";
    protected static final String sCouncilTotalCount_RentArrearsZero = "CouncilTotalCount_RentArrearsZero";
    protected static final String sCouncilTotalCount_RentArrearsNonZero = "CouncilTotalCount_RentArrearsNonZero";
    protected static final String sCouncilAverage_RentArrears = "CouncilAverage_RentArrears";
    protected static final String sCouncilAverage_NonZeroRentArrears = "CouncilAverage_NonZeroRentArrears";
    // RSL
    protected static final String sRSLCount00 = "RSLCount00";
    protected static final String sRSLCount0 = "RSLCount0";
    protected static final String sRSLCount1 = "RSLCount1";
    protected static final String sRSLLinkedRecordCount00 = "RSLLinkedRecordCount00";
    protected static final String sRSLLinkedRecordCount0 = "RSLLinkedRecordCount0";
    protected static final String sRSLLinkedRecordCount1 = "RSLLinkedRecordCount1";

    // Demographics
    // Ethnicity
    // Council
    protected static String[] sCouncilHBTotalCount_EthnicGroupClaimant;
    protected static String[] sCouncilHBPercentageOfHB_EthnicGroupClaimant;
    protected static String[] sCouncilCTBTotalCount_EthnicGroupClaimant;
    protected static String[] sCouncilCTBPercentageOfCTB_EthnicGroupClaimant;
    // RSL
    protected static String[] sRSLHBTotalCount_EthnicGroupClaimant;
    protected static String[] sRSLHBPercentageOfHB_EthnicGroupClaimant;
    protected static String[] sRSLCTBTotalCount_EthnicGroupClaimant;
    protected static String[] sRSLCTBPercentageOfCTB_EthnicGroupClaimant;

    // Key Counts
    // Council
    protected static String[] sCouncilTotalCount_ClaimantTT;
    protected static String[] sCouncilPercentageOfAll_ClaimantTT;
    protected static String[] sCouncilPercentageOfHB_ClaimantTT;
    protected static String[] sCouncilPercentageOfCTB_ClaimantTT;
    protected static String[] sCouncilPercentageOfTT_ClaimantTT;
    protected static String[] sCouncilCTBTotalCount_TTClaimant;
    protected static String[] sCouncilCTBPercentageOfCTB_TTClaimant;
    // RSL
    protected static String[] sRSLTotalCount_ClaimantTT;
    protected static String[] sRSLPercentageOfAll_ClaimantTT;
    protected static String[] sRSLPercentageOfHB_ClaimantTT;
    protected static String[] sRSLPercentageOfCTB_ClaimantTT;
    protected static String[] sRSLPercentageOfTT_ClaimantTT;
    protected static String[] sRSLCTBTotalCount_TTClaimant;
    protected static String[] sRSLCTBPercentageOfCTB_TTClaimant;

    // Postcode
    // Council
    protected static String sCouncilHBTotalCount_PostcodeValidFormat;
    protected static String sCouncilHBTotalCount_PostcodeValid;
    protected static String sCouncilCTBTotalCount_PostcodeValidFormat;
    protected static String sCouncilCTBTotalCount_PostcodeValid;
    // RSL
    protected static String sRSLHBTotalCount_PostcodeValidFormat;
    protected static String sRSLHBTotalCount_PostcodeValid;
    protected static String sRSLCTBTotalCount_PostcodeValidFormat;
    protected static String sRSLCTBTotalCount_PostcodeValid;

    // Compare 2 Times
    // Council
    // All TT
    protected static final String sCouncilAllTotalCount_TTChangeClaimant = "CouncilAllTotalCount_TTChangeClaimant";
    protected static final String sCouncilAllPercentageOfAll_TTChangeClaimant = "CouncilAllPercentageOfAll_TTChangeClaimant";
    protected static final String sCouncilTotalCount_HBTTsToCTBTTs = "CouncilTotalCount_HBTTsToCTBTTs";
    protected static final String sCouncilPercentageOfHB_HBTTsToCTBTTs = "CouncilPercentageOfHB_HBTTsToCTBTTs";
    protected static final String sCouncilTotalCount_CTBTTsToHBTTs = "CouncilTotalCount_CTBTTsToHBTTs";
    protected static final String sCouncilPercentageOfCTB_CTBTTsToHBTTs = "CouncilPercentageOfCTB_CTBTTsToHBTTs";
    // HB TT
    protected static final String sCouncilHBTotalCount_TTChangeClaimant = "CouncilHBTotalCount_TTChangeClaimant";
    protected static final String sCouncilHBPercentageOfHB_TTChangeClaimant = "CouncilHBPercentageOfHB_TTChangeClaimant";
    protected static final String sCouncilTotalCount_Minus999TTToSocialTTs = "CouncilTotalCount_Minus999TTToSocialTTs";
    protected static final String sCouncilTotalCount_Minus999TTToPrivateDeregulatedTTs = "CouncilTotalCount_Minus999TTToPrivateDeregulatedTTs";
    protected static final String sCouncilTotalCount_HBTTsToHBTTs = "CouncilTotalCount_HBTTsToHBTTs";
    protected static final String sCouncilPercentageOfHB_HBTTsToHBTTs = "CouncilPercentageOfHB_HBTTsToHBTTs";
    protected static final String sCouncilTotalCount_HBTTsToMinus999TT = "CouncilTotalCount_HBTTsToMinus999TT";
    protected static final String sCouncilPercentageOfHB_HBTTsToMinus999TT = "CouncilPercentageOfHB_HBTTsToMinus999TT";
    protected static final String sCouncilTotalCount_SocialTTsToPrivateDeregulatedTTs = "CouncilTotalCount_SocialTTsToPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs = "CouncilPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs";
    protected static final String sCouncilTotalCount_SocialTTsToMinus999TT = "CouncilTotalCount_SocialTTsToMinus999TT";
    protected static final String sCouncilPercentageOfSocialTTs_SocialTTsToMinus999TT = "CouncilPercentageOfSocialTTs_SocialTTsToMinus999TT";
    protected static final String sCouncilTotalCount_PrivateDeregulatedTTsToSocialTTs = "CouncilTotalCount_PrivateDeregulatedTTsToSocialTTs";
    protected static final String sCouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs = "CouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs";
    protected static final String sCouncilTotalCount_PrivateDeregulatedTTsToMinus999TT = "CouncilTotalCount_PrivateDeregulatedTTsToMinus999TT";
    protected static final String sCouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT = "CouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT";
    protected static final String sCouncilTotalCount_TT1ToPrivateDeregulatedTTs = "CouncilTotalCount_TT1ToPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfTT1_TT1ToPrivateDeregulatedTTs = "CouncilPercentageOfTT1_TT1ToPrivateDeregulatedTTs";
    protected static final String sCouncilTotalCount_TT4ToPrivateDeregulatedTTs = "CouncilTotalCount_TT4ToPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfTT4_TT4ToPrivateDeregulatedTTs = "CouncilPercentageOfTT4_TT4ToPrivateDeregulatedTTs";
    protected static final String sCouncilTotalCount_PrivateDeregulatedTTsToTT1 = "CouncilTotalCount_PrivateDeregulatedTTsToTT1";
    protected static final String sCouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1 = "CouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1";
    protected static final String sCouncilTotalCount_PrivateDeregulatedTTsToTT4 = "CouncilTotalCount_PrivateDeregulatedTTsToTT4";
    protected static final String sCouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4 = "CouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4";
    protected static final String sCouncilTotalCount_TT1ToTT4 = "CouncilTotalCount_TT1ToTT4";
    protected static final String sCouncilPercentageOfTT1_TT1ToTT4 = "CouncilPercentageOfTT1_TT1ToTT4";
    protected static final String sCouncilTotalCount_TT4ToTT1 = "CouncilTotalCount_TT4ToTT1";
    protected static final String sCouncilPercentageOfTT4_TT4ToTT1 = "CouncilPercentageOfTT4_TT4ToTT1";
    protected static final String sCouncilTotalCount_PostcodeChangeWithinSocialTTs = "CouncilTotalCount_PostcodeChangeWithinSocialTTs";
    protected static final String sCouncilPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs = "CouncilPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs";
    protected static final String sCouncilTotalCount_PostcodeChangeWithinTT1 = "CouncilTotalCount_PostcodeChangeWithinTT1";
    protected static final String sCouncilPercentageOfTT1_PostcodeChangeWithinTT1 = "CouncilPercentageOfTT1_PostcodeChangeWithinTT1";
    protected static final String sCouncilTotalCount_PostcodeChangeWithinTT4 = "CouncilTotalCount_PostcodeChangeWithinTT4";
    protected static final String sCouncilPercentageOfTT4_PostcodeChangeWithinTT4 = "CouncilPercentageOfTT4_PostcodeChangeWithinTT4";
    protected static final String sCouncilTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs = "CouncilTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs = "CouncilPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs";
    // CTB TT
    protected static final String sCouncilCTBTotalCount_TTChangeClaimant = "CouncilCTBTotalCount_TTChangeClaimant";
    protected static final String sCouncilCTBPercentageOfCTB_TTChangeClaimant = "CouncilCTBPercentageOfCTB_TTChangeClaimant";
    protected static final String sCouncilTotalCount_Minus999TTToCTBTTs = "CouncilTotalCount_Minus999TTToCTBTTs";
    protected static final String sCouncilTotalCount_SocialTTsToCTBTTs = "CouncilTotalCount_SocialTTsToCTBTTs";
    protected static final String sCouncilPercentageOfSocialTTs_SocialTTsToCTBTTs = "CouncilPercentageOfSocialTTs_SocialTTsToCTBTTs";
    protected static final String sCouncilTotalCount_TT1ToCTBTTs = "CouncilTotalCount_TT1ToCTBTTs";
    protected static final String sCouncilPercentageOfTT1_TT1ToCTBTTs = "CouncilPercentageOfTT1_TT1ToCTBTTs";
    protected static final String sCouncilTotalCount_TT4ToCTBTTs = "CouncilTotalCount_TT4ToCTBTTs";
    protected static final String sCouncilPercentageOfTT4_TT4ToCTBTTs = "CouncilPercentageOfTT4_TT4ToCTBTTs";
    protected static final String sCouncilTotalCount_PrivateDeregulatedTTsToCTBTTs = "CouncilTotalCount_PrivateDeregulatedTTsToCTBTTs";
    protected static final String sCouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs = "CouncilPercentageOfPrivateDeregulated_PrivateDeregulatedTTsToCTBTTs";
    protected static final String sCouncilTotalCount_CTBTTsToSocialTTs = "CouncilTotalCount_CTBTTsToSocialTTs";
    protected static final String sCouncilTotalCount_CTBTTsToMinus999TT = "CouncilTotalCount_CTBTTsToMinus999TT";
    protected static final String sCouncilPercentageOfCTB_CTBTTsToSocialTTs = "CouncilPercentageOfCTB_CTBTTsToSocialTTs";
    protected static final String sCouncilPercentageOfCTB_CTBTTsToMinus999TT = "CouncilPercentageOfCTB_CTBTTsToMinus999TT";
    protected static final String sCouncilTotalCount_CTBTTsToTT1 = "CouncilTotalCount_CTBTTsToTT1";
    protected static final String sCouncilPercentageOfCTB_CTBTTsToTT1 = "CouncilPercentageOfCTB_CTBTTsToTT1";
    protected static final String sCouncilTotalCount_CTBTTsToTT4 = "CouncilTotalCount_CTBTTsToTT4";
    protected static final String sCouncilPercentageOfCTB_CTBTTsToTT4 = "CouncilPercentageOfCTB_CTBTTsToTT4";
    protected static final String sCouncilTotalCount_CTBTTsToPrivateDeregulatedTTs = "CouncilTotalCount_CTBTTsToPrivateDeregulatedTypes";
    protected static final String sCouncilPercentageOfCTB_CTBTTsToPrivateDeregulatedTTs = "CouncilPercentageOfCTB_CTBTTsToPrivateDeregulatedTypes";
    // All Postcode
    protected static final String sCouncilAllTotalCount_Postcode0ValidPostcode1Valid = "CouncilAllTotalCount_Postcode0ValidPostcode1Valid";
    protected static final String sCouncilAllPercentagePostcode0ValidPostcode1Valid = "CouncilAllPercentagePostcode0ValidPostcode1Valid";
    protected static final String sCouncilAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = "CouncilAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sCouncilAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged = "CouncilAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sCouncilAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChange = "CouncilAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChange";
    protected static final String sCouncilAllPercentagePostcode0ValidPostcode1ValidPostcodeChange = "CouncilAllPercentagePostcode0ValidPostcode1ValidPostcodeChange";
    protected static final String sCouncilAllTotalCount_Postcode0ValidPostcode1NotValid = "CouncilAllTotalCount_Postcode0ValidPostcode1NotValid";
    protected static final String sCouncilAllPercentagePostcode0ValidPostcode1NotValid = "CouncilAllPercentagePostcode0ValidPostcode1NotValid";
    protected static final String sCouncilAllTotalCount_Postcode0NotValidPostcode1Valid = "CouncilAllTotalCount_Postcode0NotValidPostcode1Valid";
    protected static final String sCouncilAllPercentagePostcode0NotValidPostcode1Valid = "CouncilAllPercentagePostcode0NotValidPostcode1Valid";
    protected static final String sCouncilAllTotalCount_Postcode0NotValidPostcode1NotValid = "CouncilAllTotalCount_Postcode0NotValidPostcode1NotValid";
    protected static final String sCouncilAllPercentagePostcode0NotValidPostcode1NotValid = "CouncilAllPercentagePostcode0NotValidPostcode1NotValid";
    protected static final String sCouncilAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = "CouncilAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sCouncilAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged = "CouncilAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sCouncilAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = "CouncilAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged";
    protected static final String sCouncilAllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged = "CouncilAllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged";
    // HB Postcode
    protected static final String sCouncilHBTotalCount_Postcode0ValidPostcode1Valid = "CouncilHBTotalCount_Postcode0ValidPostcode1Valid";
    protected static final String sCouncilHBPercentagePostcode0ValidPostcode1Valid = "CouncilHBPercentagePostcode0ValidPostcode1Valid";
    protected static final String sCouncilHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = "CouncilHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sCouncilHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged = "CouncilHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sCouncilHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChange = "CouncilHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChange";
    protected static final String sCouncilHBPercentagePostcode0ValidPostcode1ValidPostcodeChange = "CouncilHBPercentagePostcode0ValidPostcode1ValidPostcodeChange";
    protected static final String sCouncilHBTotalCount_Postcode0ValidPostcode1NotValid = "CouncilHBTotalCount_Postcode0ValidPostcode1NotValid";
    protected static final String sCouncilHBPercentagePostcode0ValidPostcode1NotValid = "CouncilHBPercentagePostcode0ValidPostcode1NotValid";
    protected static final String sCouncilHBTotalCount_Postcode0NotValidPostcode1Valid = "CouncilHBTotalCount_Postcode0NotValidPostcode1Valid";
    protected static final String sCouncilHBPercentagePostcode0NotValidPostcode1Valid = "CouncilHBPercentagePostcode0NotValidPostcode1Valid";
    protected static final String sCouncilHBTotalCount_Postcode0NotValidPostcode1NotValid = "CouncilHBTotalCount_Postcode0NotValidPostcode1NotValid";
    protected static final String sCouncilHBPercentagePostcode0NotValidPostcode1NotValid = "CouncilHBPercentagePostcode0NotValidPostcode1NotValid";
    protected static final String sCouncilHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = "CouncilHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sCouncilHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged = "CouncilHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sCouncilHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = "CouncilHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged";
    protected static final String sCouncilHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged = "CouncilHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged";
    // CTB Postcode
    protected static final String sCouncilCTBTotalCount_Postcode0ValidPostcode1Valid = "CouncilCTBTotalCount_Postcode0ValidPostcode1Valid";
    protected static final String sCouncilCTBPercentageOfCTB_Postcode0ValidPostcode1Valid = "CouncilCTBPercentagePostcode0ValidPostcode1Valid";
    protected static final String sCouncilCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = "CouncilCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sCouncilCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged = "CouncilCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sCouncilCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged = "CouncilCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged";
    protected static final String sCouncilCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged = "CouncilCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged";
    protected static final String sCouncilCTBTotalCount_Postcode0ValidPostcode1NotValid = "CouncilCTBTotalCount_Postcode0ValidPostcode1NotValid";
    protected static final String sCouncilCTBPercentagePostcode0ValidPostcode1NotValid = "CouncilCTBPercentagePostcode0ValidPostcode1NotValid";
    protected static final String sCouncilCTBTotalCount_Postcode0NotValidPostcode1Valid = "CouncilCTBTotalCount_Postcode0NotValidPostcode1Valid";
    protected static final String sCouncilCTBPercentagePostcode0NotValidPostcode1Valid = "CouncilCTBPercentagePostcode0NotValidPostcode1Valid";
    protected static final String sCouncilCTBTotalCount_Postcode0NotValidPostcode1NotValid = "CouncilCTBTotalCount_Postcode0NotValidPostcode1NotValid";
    protected static final String sCouncilCTBPercentagePostcode0NotValidPostcode1NotValid = "CouncilCTBPercentagePostcode0NotValidPostcode1NotValid";
    protected static final String sCouncilCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = "CouncilCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sCouncilCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged = "CouncilCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sCouncilCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = "CouncilCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged";
    protected static final String sCouncilCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged = "CouncilCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged";
    // RSL
    // All TT
    protected static final String sRSLAllTotalCount_TTChangeClaimant = "RSLAllTotalCount_TTChangeClaimant";
    protected static final String sRSLAllPercentageOfAll_TTChangeClaimant = "RSLAllPercentageOfAll_TTChangeClaimant";
    protected static final String sRSLTotalCount_HBTTsToCTBTTs = "RSLTotalCount_HBTTsToCTBTTs";
    protected static final String sRSLPercentageOfHB_HBTTsToCTBTTs = "RSLPercentageOfHB_HBTTsToCTBTTs";
    protected static final String sRSLTotalCount_CTBTTsToHBTTs = "RSLTotalCount_CTBTTsToHBTTs";
    protected static final String sRSLPercentageOfCTB_CTBTTsToHBTTs = "RSLPercentageOfCTB_CTBTTsToHBTTs";
    // HB TT
    protected static final String sRSLHBTotalCount_TTChangeClaimant = "RSLHBTotalCount_TTChangeClaimant";
    protected static final String sRSLHBPercentageOfHB_TTChangeClaimant = "RSLHBPercentageOfHB_TTChangeClaimant";
    protected static final String sRSLTotalCount_Minus999TTToSocialTTs = "RSLTotalCount_Minus999TTToSocialTTs";
    protected static final String sRSLTotalCount_Minus999TTToPrivateDeregulatedTTs = "RSLTotalCount_Minus999TTToPrivateDeregulatedTTs";
    protected static final String sRSLTotalCount_HBTTsToHBTTs = "RSLTotalCount_HBTTsToHBTTs";
    protected static final String sRSLPercentageOfHB_HBTTsToHBTTs = "RSLPercentageOfHB_HBTTsToHBTTs";
    protected static final String sRSLTotalCount_HBTTsToMinus999TT = "RSLTotalCount_HBTTsToMinus999TT";
    protected static final String sRSLPercentageOfHB_HBTTsToMinus999TT = "RSLPercentageOfHB_HBTTsToMinus999TT";
    protected static final String sRSLTotalCount_SocialTTsToPrivateDeregulatedTTs = "RSLTotalCount_SocialTTsToPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs = "RSLPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs";
    protected static final String sRSLTotalCount_SocialTTsToMinus999TT = "RSLTotalCount_SocialTTsToMinus999TT";
    protected static final String sRSLPercentageOfSocialTTs_SocialTTsToMinus999TT = "RSLPercentageOfSocialTTs_SocialTTsToMinus999TT";
    protected static final String sRSLTotalCount_PrivateDeregulatedTTsToSocialTTs = "RSLTotalCount_PrivateDeregulatedTTsToSocialTTs";
    protected static final String sRSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs = "RSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs";
    protected static final String sRSLTotalCount_PrivateDeregulatedTTsToMinus999TT = "RSLTotalCount_PrivateDeregulatedTTsToMinus999TT";
    protected static final String sRSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT = "RSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT";
    protected static final String sRSLTotalCount_TT1ToPrivateDeregulatedTTs = "RSLTotalCount_TT1ToPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfTT1_TT1ToPrivateDeregulatedTTs = "RSLPercentageOfTT1_TT1ToPrivateDeregulatedTTs";
    protected static final String sRSLTotalCount_TT4ToPrivateDeregulatedTTs = "RSLTotalCount_TT4ToPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfTT4_TT4ToPrivateDeregulatedTTs = "RSLPercentageOfTT4_TT4ToPrivateDeregulatedTTs";
    protected static final String sRSLTotalCount_PrivateDeregulatedTTsToTT1 = "RSLTotalCount_PrivateDeregulatedTTsToTT1";
    protected static final String sRSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1 = "RSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1";
    protected static final String sRSLTotalCount_PrivateDeregulatedTTsToTT4 = "RSLTotalCount_PrivateDeregulatedTTsToTT4";
    protected static final String sRSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4 = "RSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4";
    protected static final String sRSLTotalCount_TT1ToTT4 = "RSLTotalCount_TT1ToTT4";
    protected static final String sRSLPercentageOfTT1_TT1ToTT4 = "RSLPercentageOfTT1_TT1ToTT4";
    protected static final String sRSLTotalCount_TT4ToTT1 = "RSLTotalCount_TT4ToTT1";
    protected static final String sRSLPercentageOfTT4_TT4ToTT1 = "RSLPercentageOfTT4_TT4ToTT1";
    protected static final String sRSLTotalCount_PostcodeChangeWithinSocialTTs = "RSLTotalCount_PostcodeChangeWithinSocialTTs";
    protected static final String sRSLPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs = "RSLPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs";
    protected static final String sRSLTotalCount_PostcodeChangeWithinTT1 = "RSLTotalCount_PostcodeChangeWithinTT1";
    protected static final String sRSLPercentageOfTT1_PostcodeChangeWithinTT1 = "RSLPercentageOfTT1_PostcodeChangeWithinTT1";
    protected static final String sRSLTotalCount_PostcodeChangeWithinTT4 = "RSLTotalCount_PostcodeChangeWithinTT4";
    protected static final String sRSLPercentageOfTT4_PostcodeChangeWithinTT4 = "RSLPercentageOfTT4_PostcodeChangeWithinTT4";
    protected static final String sRSLTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs = "RSLTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs = "RSLPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs";
    // CTB TT
    protected static final String sRSLCTBTotalCount_TTChangeClaimant = "RSLCTBTotalCount_TTChangeClaimant";
    protected static final String sRSLCTBPercentageOfCTB_TTChangeClaimant = "RSLCTBPercentageOfCTB_TTChangeClaimant";
    protected static final String sRSLTotalCount_Minus999TTToCTBTTs = "RSLTotalCount_Minus999TTToCTBTTs";
    protected static final String sRSLTotalCount_SocialTTsToCTBTTs = "RSLTotalCount_SocialTTsToCTBTTs";
    protected static final String sRSLPercentageOfSocialTTs_SocialTTsToCTBTTs = "RSLPercentageOfSocialTTs_SocialTTsToCTBTTs";
    protected static final String sRSLTotalCount_TT1ToCTBTTs = "RSLTotalCount_TT1ToCTBTTs";
    protected static final String sRSLPercentageOfTT1_TT1ToCTBTTs = "RSLPercentageOfTT1_TT1ToCTBTTs";
    protected static final String sRSLTotalCount_TT4ToCTBTTs = "RSLTotalCount_TT4ToCTBTTs";
    protected static final String sRSLPercentageOfTT4_TT4ToCTBTTs = "RSLPercentageOfTT4_TT4ToCTBTTs";
    protected static final String sRSLTotalCount_PrivateDeregulatedTTsToCTBTTs = "RSLTotalCount_PrivateDeregulatedTTsToCTBTTs";
    protected static final String sRSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs = "RSLPercentageOfPrivateDeregulated_PrivateDeregulatedTTsToCTBTTs";
    protected static final String sRSLTotalCount_CTBTTsToSocialTTs = "RSLTotalCount_CTBTTsToSocialTTs";
    protected static final String sRSLTotalCount_CTBTTsToMinus999TT = "RSLTotalCount_CTBTTsToMinus999TT";
    protected static final String sRSLPercentageOfCTB_CTBTTsToSocialTTs = "RSLPercentageOfCTB_CTBTTsToSocialTTs";
    protected static final String sRSLPercentageOfCTB_CTBTTsToMinus999TT = "RSLPercentageOfCTB_CTBTTsToMinus999TT";
    protected static final String sRSLTotalCount_CTBTTsToTT1 = "RSLTotalCount_CTBTTsToTT1";
    protected static final String sRSLPercentageOfCTB_CTBTTsToTT1 = "RSLPercentageOfCTB_CTBTTsToTT1";
    protected static final String sRSLTotalCount_CTBTTsToTT4 = "RSLTotalCount_CTBTTsToTT4";
    protected static final String sRSLPercentageOfCTB_CTBTTsToTT4 = "RSLPercentageOfCTB_CTBTTsToTT4";
    protected static final String sRSLTotalCount_CTBTTsToPrivateDeregulatedTTs = "RSLTotalCount_CTBTTsToPrivateDeregulatedTypes";
    protected static final String sRSLPercentageOfCTB_CTBTTsToPrivateDeregulatedTTs = "RSLPercentageOfCTB_CTBTTsToPrivateDeregulatedTypes";
    // All Postcode
    protected static final String sRSLAllTotalCount_Postcode0ValidPostcode1Valid = "RSLAllTotalCount_Postcode0ValidPostcode1Valid";
    protected static final String sRSLAllPercentagePostcode0ValidPostcode1Valid = "RSLAllPercentagePostcode0ValidPostcode1Valid";
    protected static final String sRSLAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = "RSLAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sRSLAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged = "RSLAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sRSLAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChange = "RSLAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChange";
    protected static final String sRSLAllPercentagePostcode0ValidPostcode1ValidPostcodeChange = "RSLAllPercentagePostcode0ValidPostcode1ValidPostcodeChange";
    protected static final String sRSLAllTotalCount_Postcode0ValidPostcode1NotValid = "RSLAllTotalCount_Postcode0ValidPostcode1NotValid";
    protected static final String sRSLAllPercentagePostcode0ValidPostcode1NotValid = "RSLAllPercentagePostcode0ValidPostcode1NotValid";
    protected static final String sRSLAllTotalCount_Postcode0NotValidPostcode1Valid = "RSLAllTotalCount_Postcode0NotValidPostcode1Valid";
    protected static final String sRSLAllPercentagePostcode0NotValidPostcode1Valid = "RSLAllPercentagePostcode0NotValidPostcode1Valid";
    protected static final String sRSLAllTotalCount_Postcode0NotValidPostcode1NotValid = "RSLAllTotalCount_Postcode0NotValidPostcode1NotValid";
    protected static final String sRSLAllPercentagePostcode0NotValidPostcode1NotValid = "RSLAllPercentagePostcode0NotValidPostcode1NotValid";
    protected static final String sRSLAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = "RSLAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sRSLAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged = "RSLAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sRSLAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = "RSLAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged";
    protected static final String sRSLAllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged = "RSLAllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged";
    // HB Postcode
    protected static final String sRSLHBTotalCount_Postcode0ValidPostcode1Valid = "RSLHBTotalCount_Postcode0ValidPostcode1Valid";
    protected static final String sRSLHBPercentagePostcode0ValidPostcode1Valid = "RSLHBPercentagePostcode0ValidPostcode1Valid";
    protected static final String sRSLHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = "RSLHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sRSLHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged = "RSLHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sRSLHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChange = "RSLHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChange";
    protected static final String sRSLHBPercentagePostcode0ValidPostcode1ValidPostcodeChange = "RSLHBPercentagePostcode0ValidPostcode1ValidPostcodeChange";
    protected static final String sRSLHBTotalCount_Postcode0ValidPostcode1NotValid = "RSLHBTotalCount_Postcode0ValidPostcode1NotValid";
    protected static final String sRSLHBPercentagePostcode0ValidPostcode1NotValid = "RSLHBPercentagePostcode0ValidPostcode1NotValid";
    protected static final String sRSLHBTotalCount_Postcode0NotValidPostcode1Valid = "RSLHBTotalCount_Postcode0NotValidPostcode1Valid";
    protected static final String sRSLHBPercentagePostcode0NotValidPostcode1Valid = "RSLHBPercentagePostcode0NotValidPostcode1Valid";
    protected static final String sRSLHBTotalCount_Postcode0NotValidPostcode1NotValid = "RSLHBTotalCount_Postcode0NotValidPostcode1NotValid";
    protected static final String sRSLHBPercentagePostcode0NotValidPostcode1NotValid = "RSLHBPercentagePostcode0NotValidPostcode1NotValid";
    protected static final String sRSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = "RSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sRSLHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged = "RSLHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sRSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = "RSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged";
    protected static final String sRSLHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged = "RSLHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged";
    // CTB Postcode
    protected static final String sRSLCTBTotalCount_Postcode0ValidPostcode1Valid = "RSLCTBTotalCount_Postcode0ValidPostcode1Valid";
    protected static final String sRSLCTBPercentageOfCTB_Postcode0ValidPostcode1Valid = "RSLCTBPercentagePostcode0ValidPostcode1Valid";
    protected static final String sRSLCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = "RSLCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sRSLCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged = "RSLCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sRSLCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged = "RSLCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged";
    protected static final String sRSLCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged = "RSLCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged";
    protected static final String sRSLCTBTotalCount_Postcode0ValidPostcode1NotValid = "RSLCTBTotalCount_Postcode0ValidPostcode1NotValid";
    protected static final String sRSLCTBPercentagePostcode0ValidPostcode1NotValid = "RSLCTBPercentagePostcode0ValidPostcode1NotValid";
    protected static final String sRSLCTBTotalCount_Postcode0NotValidPostcode1Valid = "RSLCTBTotalCount_Postcode0NotValidPostcode1Valid";
    protected static final String sRSLCTBPercentagePostcode0NotValidPostcode1Valid = "RSLCTBPercentagePostcode0NotValidPostcode1Valid";
    protected static final String sRSLCTBTotalCount_Postcode0NotValidPostcode1NotValid = "RSLCTBTotalCount_Postcode0NotValidPostcode1NotValid";
    protected static final String sRSLCTBPercentagePostcode0NotValidPostcode1NotValid = "RSLCTBPercentagePostcode0NotValidPostcode1NotValid";
    protected static final String sRSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = "RSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sRSLCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged = "RSLCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sRSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = "RSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged";
    protected static final String sRSLCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged = "RSLCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged";

    // Compare 3 Times
    // Council
    protected static String sCouncilSamePostcodeIII;
    protected static String sCouncilSamePostcodeIOI;
    protected static String sCouncilSamePostcodeOIO;
    protected static String sCouncilSameTenancyIII;
    protected static String sCouncilSameTenancyIOI;
    protected static String sCouncilSameTenancyOIO;
    protected static String sCouncilSameTenancyAndPostcodeIII;
    protected static String sCouncilSameTenancyAndPostcodeIOI;
    protected static String sCouncilSameTenancyAndPostcodeOIO;
    protected static String[] sCouncilSameTenancyIIITT;
    protected static String[] sCouncilSameTenancyIOITT;
    protected static String[] sCouncilSameTenancyOIOTT;
    protected static String[] sCouncilSameTenancyAndPostcodeIIITT;
    protected static String[] sCouncilSameTenancyAndPostcodeIOITT;
    protected static String[] sCouncilSameTenancyAndPostcodeOIOTT;
    // RSL
    protected static String sRSLSamePostcodeIII;
    protected static String sRSLSamePostcodeIOI;
    protected static String sRSLSamePostcodeOIO;
    protected static String sRSLSameTenancyIII;
    protected static String sRSLSameTenancyIOI;
    protected static String sRSLSameTenancyOIO;
    protected static String sRSLSameTenancyAndPostcodeIII;
    protected static String sRSLSameTenancyAndPostcodeIOI;
    protected static String sRSLSameTenancyAndPostcodeOIO;
    protected static String[] sRSLSameTenancyIIITT;
    protected static String[] sRSLSameTenancyIOITT;
    protected static String[] sRSLSameTenancyOIOTT;
    protected static String[] sRSLSameTenancyAndPostcodeIIITT;
    protected static String[] sRSLSameTenancyAndPostcodeIOITT;
    protected static String[] sRSLSameTenancyAndPostcodeOIOTT;

    // Counters
    // Council
    // Single Time
    // All
    protected static double CouncilAllTotalWeeklyHBEntitlement;
    protected static int CouncilAllTotalWeeklyHBEntitlementNonZeroCount;
    protected static int CouncilAllTotalWeeklyHBEntitlementZeroCount;
    protected static double CouncilAllTotalWeeklyCTBEntitlement;
    protected static int CouncilAllTotalCount_WeeklyCTBEntitlementNonZero;
    protected static int CouncilAllTotalWeeklyCTBEntitlementZeroCount;
    protected static double CouncilAllTotalWeeklyEligibleRentAmount;
    protected static int CouncilAllTotalWeeklyEligibleRentAmountNonZeroCount;
    protected static int CouncilAllTotalWeeklyEligibleRentAmountZeroCount;
    protected static double CouncilAllTotalWeeklyEligibleCouncilTaxAmount;
    protected static int CouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
    protected static int CouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountZero;
    protected static double CouncilAllTotalContractualRentAmount;
    protected static int CouncilAllTotalContractualRentAmountNonZeroCount;
    protected static int CouncilAllTotalContractualRentAmountZeroCount;
    protected static double CouncilAllTotalWeeklyAdditionalDiscretionaryPayment;
    protected static int CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    protected static int CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    protected static double CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    protected static int CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    protected static int CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;
    // HB
    protected static double CouncilHBTotalWeeklyHBEntitlement;
    protected static int CouncilHBTotalCount_WeeklyHBEntitlementNonZero;
    protected static int CouncilHBTotalCount_WeeklyHBEntitlementZero;
    protected static double CouncilHBTotalWeeklyCTBEntitlement;
    protected static int CouncilHBTotalCount_WeeklyCTBEntitlementNonZero;
    protected static int CouncilHBTotalWeeklyCTBEntitlementZeroCount;
    protected static double CouncilHBTotalWeeklyEligibleRentAmount;
    protected static int CouncilHBTotalWeeklyEligibleRentAmountNonZeroCount;
    protected static int CouncilHBTotalWeeklyEligibleRentAmountZeroCount;
    protected static double CouncilHBTotalWeeklyEligibleCouncilTaxAmount;
    protected static int CouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
    protected static int CouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountZero;
    protected static double CouncilHBTotalContractualRentAmount;
    protected static int CouncilHBTotalContractualRentAmountNonZeroCount;
    protected static int CouncilHBTotalContractualRentAmountZeroCount;
    protected static double CouncilHBTotalWeeklyAdditionalDiscretionaryPayment;
    protected static int CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    protected static int CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    protected static double CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    protected static int CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    protected static int CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;
    // CTB
    protected static double CouncilCTBTotalWeeklyHBEntitlement;
    protected static int CouncilCTBTotalCount_WeeklyHBEntitlementNonZero;
    protected static int CouncilCTBTotalWeeklyHBEntitlementZeroCount;
    protected static double CouncilCTBTotalWeeklyCTBEntitlement;
    protected static int CouncilCTBTotalCount_WeeklyCTBEntitlementNonZero;
    protected static int CouncilCTBTotalWeeklyCTBEntitlementZeroCount;
    protected static double CouncilCTBTotalWeeklyEligibleRentAmount;
    protected static int CouncilCTBTotalWeeklyEligibleRentAmountNonZeroCount;
    protected static int CouncilCTBTotalWeeklyEligibleRentAmountZeroCount;
    protected static double CouncilCTBTotalWeeklyEligibleCouncilTaxAmount;
    protected static int CouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
    protected static int CouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero;
    protected static double CouncilCTBTotalContractualRentAmount;
    protected static int CouncilCTBTotalContractualRentAmountNonZeroCount;
    protected static int CouncilCTBTotalContractualRentAmountZeroCount;
    protected static double CouncilCTBTotalWeeklyAdditionalDiscretionaryPayment;
    protected static int CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    protected static int CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    protected static double CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    protected static int CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    protected static int CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;
    // Employment Education Training
    protected static int CouncilHBTotalCount_EmployedClaimants;
    protected static int CouncilHBTotalCount_SelfEmployedClaimants;
    protected static int CouncilHBTotalCount_StudentsClaimants;
    protected static int CouncilCTBTotalCount_EmployedClaimants;
    protected static int CouncilCTBTotalCount_SelfEmployedClaimants;
    protected static int CouncilCTBTotalCount_StudentsClaimants;
    // HLA
    protected static int CouncilHBTotalCount_LHACases;
    protected static int CouncilCTBTotalCount_LHACases;
    protected static int[] CouncilTotalCount_DisabilityPremiumAwardByTT;
    protected static int[] CouncilTotalCount_SevereDisabilityPremiumAwardByTT;
    protected static int[] CouncilTotalCount_DisabledChildPremiumAwardByTT;
    protected static int[] CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT;
    protected static int[] CouncilTotalCount_DisabilityAwardByTT;
    protected static int[] CouncilAllTotalCount_PSI;
    protected static int[] CouncilHBTotalCount_PSI;
    protected static int[] CouncilCTBTotalCount_PSI;
    protected static int[][] CouncilAllTotalCount_PSIByTT;
    protected static int[][] CouncilHBTotalCount_PSIByTT;
    protected static int[][] CouncilCTBTotalCount_PSIByTT;
    protected static int[] CouncilTotalCount_TTClaimant1;
    protected static int[] CouncilTotalCount_TTClaimant0;
    protected static long CouncilAllTotalHouseholdSize;
    protected static long CouncilHBTotalHouseholdSize;
    protected static long CouncilCTBTotalHouseholdSize;
    protected static int CouncilAllCount1;
    protected static Integer CouncilAllCount0;
    // HB
    protected static int CouncilHBCount1;
    protected static Integer CouncilHBCount0;
    protected static int[] CouncilHBEthnicGroupCount;
    protected static int CouncilHBTotalCount_PostcodeValidFormat;
    protected static int CouncilHBTotalCount_PostcodeValid;
    // CTB
    protected static int CouncilCTBCount1;
    protected static Integer CouncilCTBCount0;
    protected static int[] CouncilCTBEthnicGroupCount;
    protected static int CouncilCTBTotalCount_PostcodeValidFormat;
    protected static int CouncilCTBTotalCount_PostcodeValid;
    // Other summary stats
    protected static double CouncilTotal_RentArrears;
    protected static double CouncilTotalCount_RentArrears;
    protected static int CouncilTotalCount_RentArrearsNonZero;
    protected static int CouncilTotalCount_RentArrearsZero;
    // RSL
    // Single Time
    // All
    protected static double RSLAllTotalWeeklyHBEntitlement;
    protected static int RSLAllTotalWeeklyHBEntitlementNonZeroCount;
    protected static int RSLAllTotalWeeklyHBEntitlementZeroCount;
    protected static double RSLAllTotalWeeklyCTBEntitlement;
    protected static int RSLAllTotalCount_WeeklyCTBEntitlementNonZero;
    protected static int RSLAllTotalWeeklyCTBEntitlementZeroCount;
    protected static double RSLAllTotalWeeklyEligibleRentAmount;
    protected static int RSLAllTotalWeeklyEligibleRentAmountNonZeroCount;
    protected static int RSLAllTotalWeeklyEligibleRentAmountZeroCount;
    protected static double RSLAllTotalWeeklyEligibleCouncilTaxAmount;
    protected static int RSLAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
    protected static int RSLAllTotalCount_WeeklyEligibleCouncilTaxAmountZero;
    protected static double RSLAllTotalContractualRentAmount;
    protected static int RSLAllTotalContractualRentAmountNonZeroCount;
    protected static int RSLAllTotalContractualRentAmountZeroCount;
    protected static double RSLAllTotalWeeklyAdditionalDiscretionaryPayment;
    protected static int RSLAllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    protected static int RSLAllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    protected static double RSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    protected static int RSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    protected static int RSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;
    // HB
    protected static double RSLHBTotalWeeklyHBEntitlement;
    protected static int RSLHBTotalCount_WeeklyHBEntitlementNonZero;
    protected static int RSLHBTotalCount_WeeklyHBEntitlementZero;
    protected static double RSLHBTotalWeeklyCTBEntitlement;
    protected static int RSLHBTotalCount_WeeklyCTBEntitlementNonZero;
    protected static int RSLHBTotalWeeklyCTBEntitlementZeroCount;
    protected static double RSLHBTotalWeeklyEligibleRentAmount;
    protected static int RSLHBTotalWeeklyEligibleRentAmountNonZeroCount;
    protected static int RSLHBTotalWeeklyEligibleRentAmountZeroCount;
    protected static double RSLHBTotalWeeklyEligibleCouncilTaxAmount;
    protected static int RSLHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
    protected static int RSLHBTotalCount_WeeklyEligibleCouncilTaxAmountZero;
    protected static double RSLHBTotalContractualRentAmount;
    protected static int RSLHBTotalContractualRentAmountNonZeroCount;
    protected static int RSLHBTotalContractualRentAmountZeroCount;
    protected static double RSLHBTotalWeeklyAdditionalDiscretionaryPayment;
    protected static int RSLHBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    protected static int RSLHBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    protected static double RSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    protected static int RSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    protected static int RSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;
    // CTB
    protected static double RSLCTBTotalWeeklyHBEntitlement;
    protected static int RSLCTBTotalCount_WeeklyHBEntitlementNonZero;
    protected static int RSLCTBTotalWeeklyHBEntitlementZeroCount;
    protected static double RSLCTBTotalWeeklyCTBEntitlement;
    protected static int RSLCTBTotalCount_WeeklyCTBEntitlementNonZero;
    protected static int RSLCTBTotalWeeklyCTBEntitlementZeroCount;
    protected static double RSLCTBTotalWeeklyEligibleRentAmount;
    protected static int RSLCTBTotalWeeklyEligibleRentAmountNonZeroCount;
    protected static int RSLCTBTotalWeeklyEligibleRentAmountZeroCount;
    protected static double RSLCTBTotalWeeklyEligibleCouncilTaxAmount;
    protected static int RSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
    protected static int RSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero;
    protected static double RSLCTBTotalContractualRentAmount;
    protected static int RSLCTBTotalContractualRentAmountNonZeroCount;
    protected static int RSLCTBTotalContractualRentAmountZeroCount;
    protected static double RSLCTBTotalWeeklyAdditionalDiscretionaryPayment;
    protected static int RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    protected static int RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    protected static double RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    protected static int RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    protected static int RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;
    // Employment Education Training
    protected static int RSLHBTotalCount_EmployedClaimants;
    protected static int RSLHBTotalCount_SelfEmployedClaimants;
    protected static int RSLHBTotalCount_StudentsClaimants;
    protected static int RSLCTBTotalCount_EmployedClaimants;
    protected static int RSLCTBTotalCount_SelfEmployedClaimants;
    protected static int RSLCTBTotalCount_StudentsClaimants;
    // HLA
    protected static int RSLHBTotalCount_LHACases;
    protected static int RSLCTBTotalCount_LHACases;
    // Disability
    protected static int[] RSLTotalCount_DisabilityPremiumAwardByTT;
    protected static int[] RSLTotalCount_SevereDisabilityPremiumAwardByTT;
    protected static int[] RSLTotalCount_DisabledChildPremiumAwardByTT;
    protected static int[] RSLTotalCount_EnhancedDisabilityPremiumAwardByTT;
    protected static int[] RSLTotalCount_DisabilityAwardByTT;
    // Key Counts
    protected static int[] RSLAllTotalCount_PSI;
    protected static int[] RSLHBTotalCount_PSI;
    protected static int[] RSLCTBTotalCount_PSI;
    protected static int[][] RSLAllTotalCount_PSIByTT;
    protected static int[][] RSLHBTotalCount_PSIByTT;
    protected static int[][] RSLCTBTotalCount_PSIByTT;
    protected static int[] RSLTotalCount_TTClaimant1;
    protected static int[] RSLTotalCount_TTClaimant0;
    protected static long RSLAllTotalHouseholdSize;
    protected static long RSLHBTotalHouseholdSize;
    protected static long RSLCTBTotalHouseholdSize;
    protected static int RSLAllCount1;
    protected static Integer RSLAllCount0;
    // HB
    protected static int RSLHBCount1;
    protected static Integer RSLHBCount0;
    protected static int[] RSLHBEthnicGroupCount;
    protected static int RSLHBTotalCount_PostcodeValidFormat;
    protected static int RSLHBTotalCount_PostcodeValid;
    // CTB
    protected static int RSLCTBCount1;
    protected static Integer RSLCTBCount0;
    protected static int[] RSLCTBEthnicGroupCount;
    protected static int RSLCTBTotalCount_PostcodeValidFormat;
    protected static int RSLCTBTotalCount_PostcodeValid;

    // Compare 2 Times
    // Council
    // General
    protected static int CouncilTotalCount_HBTTsToCTBTTs;
    protected static int CouncilTotalCount_CTBTTsToHBTTs;
    // General HB related
    protected static int CouncilTotalCount_Minus999TTToSocialTTs;
    protected static int CouncilTotalCount_Minus999TTToPrivateDeregulatedTTs;
    protected static int CouncilTotalCount_HBTTsToHBTTs;
    protected static int CouncilTotalCount_HBTTsToMinus999TT;
    protected static int CouncilTotalCount_SocialTTsToPrivateDeregulatedTTs;
    protected static int CouncilTotalCount_SocialTTsToMinus999TT;
    protected static int CouncilTotalCount_PrivateDeregulatedTTsToSocialTTs;
    protected static int CouncilTotalCount_PrivateDeregulatedTTsToMinus999TT;
    protected static int CouncilTotalCount_TT1ToPrivateDeregulatedTTs;
    protected static int CouncilTotalCount_TT4ToPrivateDeregulatedTTs;
    protected static int CouncilTotalCount_PrivateDeregulatedTTsToTT1;
    protected static int CouncilTotalCount_PrivateDeregulatedTTsToTT4;
    protected static int CouncilTotalCount_PostcodeChangeWithinSocialTTs;
    protected static int CouncilTotalCount_PostcodeChangeWithinTT1;
    protected static int CouncilTotalCount_PostcodeChangeWithinTT4;
    protected static int CouncilTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs;
    // General CTB related
    protected static int CouncilTotalCount_SocialTTsToCTBTTs;
    protected static int CouncilTotalCount_Minus999TTToCTBTTs;
    protected static int CouncilTotalCount_TT1ToCTBTTs;
    protected static int CouncilTotalCount_TT4ToCTBTTs;
    protected static int CouncilTotalCount_PrivateDeregulatedTTsToCTBTTs;
    protected static int CouncilTotalCount_CTBTTsToSocialTTs;
    protected static int CouncilTotalCount_CTBTTsToMinus999TT;
    protected static int CouncilTotalCount_CTBTTsToTT1;
    protected static int CouncilTotalCount_CTBTTsToTT4;
    protected static int CouncilTotalCount_CTBTTsToPrivateDeregulatedTTs;
    // TT1 TT4
    protected static int CouncilTotalCount_TT1ToTT4;
    protected static int CouncilTotalCount_TT4ToTT1;
    // CouncilAll
    protected static int CouncilAllTotalCount_TTChangeClaimant;
    //protected static int CouncilAllTotalCount_TTChangeClaimantIgnoreMinus999;
    protected static int CouncilAllTotalCount_Postcode0ValidPostcode1Valid;
    protected static int CouncilAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged;
    protected static int CouncilAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged;
    protected static int CouncilAllTotalCount_Postcode0ValidPostcode1NotValid;
    protected static int CouncilAllTotalCount_Postcode0NotValidPostcode1Valid;
    protected static int CouncilAllTotalCount_Postcode0NotValidPostcode1NotValid;
    protected static int CouncilAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged;
    protected static int CouncilAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged;
    // HB
    protected static int CouncilHBTotalCount_TTChangeClaimant;
    //protected static int CouncilHBTotalCount_TTChangeClaimantIgnoreMinus999;
    protected static int CouncilHBTotalCount_Postcode0ValidPostcode1Valid;
    protected static int CouncilHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged;
    protected static int CouncilHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged;
    protected static int CouncilHBTotalCount_Postcode0ValidPostcode1NotValid;
    protected static int CouncilHBTotalCount_Postcode0NotValidPostcode1Valid;
    protected static int CouncilHBTotalCount_Postcode0NotValidPostcode1NotValid;
    protected static int CouncilHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged;
    protected static int CouncilHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged;
    // CTB
    protected static int CouncilCTBTotalCount_TTChangeClaimant;
    protected static int CouncilCTBTotalCount_Postcode0ValidPostcode1Valid;
    protected static int CouncilCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged;
    protected static int CouncilCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged;
    protected static int CouncilCTBTotalCount_Postcode0ValidPostcode1NotValid;
    protected static int CouncilCTBTotalCount_Postcode0NotValidPostcode1Valid;
    protected static int CouncilCTBTotalCount_Postcode0NotValidPostcode1NotValid;
    protected static int CouncilCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged;
    protected static int CouncilCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged;
    // RSL
    // General
    protected static int RSLTotalCount_HBTTsToCTBTTs;
    protected static int RSLTotalCount_CTBTTsToHBTTs;
    // General HB related
    protected static int RSLTotalCount_Minus999TTToSocialTTs;
    protected static int RSLTotalCount_Minus999TTToPrivateDeregulatedTTs;
    protected static int RSLTotalCount_HBTTsToHBTTs;
    protected static int RSLTotalCount_HBTTsToMinus999TT;
    protected static int RSLTotalCount_SocialTTsToPrivateDeregulatedTTs;
    protected static int RSLTotalCount_SocialTTsToMinus999TT;
    protected static int RSLTotalCount_PrivateDeregulatedTTsToSocialTTs;
    protected static int RSLTotalCount_PrivateDeregulatedTTsToMinus999TT;
    protected static int RSLTotalCount_TT1ToPrivateDeregulatedTTs;
    protected static int RSLTotalCount_TT4ToPrivateDeregulatedTTs;
    protected static int RSLTotalCount_PrivateDeregulatedTTsToTT1;
    protected static int RSLTotalCount_PrivateDeregulatedTTsToTT4;
    protected static int RSLTotalCount_PostcodeChangeWithinSocialTTs;
    protected static int RSLTotalCount_PostcodeChangeWithinTT1;
    protected static int RSLTotalCount_PostcodeChangeWithinTT4;
    protected static int RSLTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs;
    // General CTB related
    protected static int RSLTotalCount_SocialTTsToCTBTTs;
    protected static int RSLTotalCount_Minus999TTToCTBTTs;
    protected static int RSLTotalCount_TT1ToCTBTTs;
    protected static int RSLTotalCount_TT4ToCTBTTs;
    protected static int RSLTotalCount_PrivateDeregulatedTTsToCTBTTs;
    protected static int RSLTotalCount_CTBTTsToSocialTTs;
    protected static int RSLTotalCount_CTBTTsToMinus999TT;
    protected static int RSLTotalCount_CTBTTsToTT1;
    protected static int RSLTotalCount_CTBTTsToTT4;
    protected static int RSLTotalCount_CTBTTsToPrivateDeregulatedTTs;
    // TT1 TT4
    protected static int RSLTotalCount_TT1ToTT4;
    protected static int RSLTotalCount_TT4ToTT1;
    // RSLAll
    protected static int RSLAllTotalCount_TTChangeClaimant;
    protected static int RSLAllTotalCount_Postcode0ValidPostcode1Valid;
    protected static int RSLAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged;
    protected static int RSLAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged;
    protected static int RSLAllTotalCount_Postcode0ValidPostcode1NotValid;
    protected static int RSLAllTotalCount_Postcode0NotValidPostcode1Valid;
    protected static int RSLAllTotalCount_Postcode0NotValidPostcode1NotValid;
    protected static int RSLAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged;
    protected static int RSLAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged;
    // HB
    protected static int RSLHBTotalCount_TTChangeClaimant;
    protected static int RSLHBTotalCount_Postcode0ValidPostcode1Valid;
    protected static int RSLHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged;
    protected static int RSLHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged;
    protected static int RSLHBTotalCount_Postcode0ValidPostcode1NotValid;
    protected static int RSLHBTotalCount_Postcode0NotValidPostcode1Valid;
    protected static int RSLHBTotalCount_Postcode0NotValidPostcode1NotValid;
    protected static int RSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged;
    protected static int RSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged;
    // CTB
    protected static int RSLCTBTotalCount_TTChangeClaimant;
    protected static int RSLCTBTotalCount_Postcode0ValidPostcode1Valid;
    protected static int RSLCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged;
    protected static int RSLCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged;
    protected static int RSLCTBTotalCount_Postcode0ValidPostcode1NotValid;
    protected static int RSLCTBTotalCount_Postcode0NotValidPostcode1Valid;
    protected static int RSLCTBTotalCount_Postcode0NotValidPostcode1NotValid;
    protected static int RSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged;
    protected static int RSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged;

    protected static int AllUOCount00;
    protected static int AllUOCount0;
    protected static int AllUOCount1;
    protected static int CouncilCount00;
    protected static int CouncilCount0;
    protected static int CouncilCount1;
    protected static int RSLCount00;
    protected static int RSLCount0;
    protected static int RSLCount1;
    protected static int AllLinkedRecordCount00;
    protected static int AllUOLinkedRecordCount0;
    protected static int AllUOLinkedRecordCount1;
    protected static int CouncilLinkedRecordCount00;
    protected static int CouncilLinkedRecordCount0;
    protected static int CouncilLinkedRecordCount1;
    protected static int RSLLinkedRecordCount00;
    protected static int RSLLinkedRecordCount0;
    protected static int RSLLinkedRecordCount1;

    // Compare 3 Times
    // Council
    protected static int CouncilSamePostcodeIII;
    protected static int CouncilSamePostcodeIOI;
    protected static int CouncilSamePostcodeOIO;
    protected static int CouncilSameTenancyIII;
    protected static int CouncilSameTenancyIOI;
    protected static int CouncilSameTenancyOIO;
    protected static int CouncilSameTenancyAndPostcodeIII;
    protected static int CouncilSameTenancyAndPostcodeIOI;
    protected static int CouncilSameTenancyAndPostcodeOIO;
    protected static int[] CouncilSameTenancyIIITT;
    protected static int[] CouncilSameTenancyIOITT;
    protected static int[] CouncilSameTenancyOIOTT;
    protected static int[] CouncilSameTenancyAndPostcodeIIITT;
    protected static int[] CouncilSameTenancyAndPostcodeIOITT;
    protected static int[] CouncilSameTenancyAndPostcodeOIOTT;
    // RSL
    protected static int RSLSamePostcodeIII;
    protected static int RSLSamePostcodeIOI;
    protected static int RSLSamePostcodeOIO;
    protected static int RSLSameTenancyIII;
    protected static int RSLSameTenancyIOI;
    protected static int RSLSameTenancyOIO;
    protected static int RSLSameTenancyAndPostcodeIII;
    protected static int RSLSameTenancyAndPostcodeIOI;
    protected static int RSLSameTenancyAndPostcodeOIO;
    protected static int[] RSLSameTenancyIIITT;
    protected static int[] RSLSameTenancyIOITT;
    protected static int[] RSLSameTenancyOIOTT;
    protected static int[] RSLSameTenancyAndPostcodeIIITT;
    protected static int[] RSLSameTenancyAndPostcodeIOITT;
    protected static int[] RSLSameTenancyAndPostcodeOIOTT;

    public SummaryUO(
            DW_Environment env,
            DW_SHBE_CollectionHandler collectionHandler,
            DW_SHBE_Handler tDW_SHBE_Handler,
            int nTT,
            int nEG,
            int nPSI,
            boolean handleOutOfMemoryError) {
        this.env = env;
        this.collectionHandler = collectionHandler;
        this.tDW_SHBE_Handler = tDW_SHBE_Handler;
        init(nTT, nEG, nPSI);
    }

    @Override
    protected void init(int nTT, int nEG, int nPSI) {
        super.init(nTT, nEG, nPSI);
        initSingleTimeStrings(nTT, nEG, nPSI);
        initCompare3TimesStrings(nTT, nEG);
        // Council
        CouncilTotalCount_DisabilityPremiumAwardByTT = new int[nTT];
        CouncilTotalCount_SevereDisabilityPremiumAwardByTT = new int[nTT];
        CouncilTotalCount_DisabledChildPremiumAwardByTT = new int[nTT];
        CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT = new int[nTT];
        CouncilTotalCount_DisabilityAwardByTT = new int[nTT];
        CouncilAllTotalCount_PSI = new int[nPSI];
        CouncilHBTotalCount_PSI = new int[nPSI];
        CouncilCTBTotalCount_PSI = new int[nPSI];
        CouncilAllTotalCount_PSIByTT = new int[nPSI][nTT];
        CouncilHBTotalCount_PSIByTT = new int[nPSI][nTT];
        CouncilCTBTotalCount_PSIByTT = new int[nPSI][nTT];
        CouncilTotalCount_TTClaimant1 = new int[nTT];
        CouncilTotalCount_TTClaimant0 = new int[nTT];
        CouncilHBEthnicGroupCount = new int[nEG];
        CouncilCTBEthnicGroupCount = new int[nEG];
        // RSL
        RSLTotalCount_DisabilityPremiumAwardByTT = new int[nTT];
        RSLTotalCount_SevereDisabilityPremiumAwardByTT = new int[nTT];
        RSLTotalCount_DisabledChildPremiumAwardByTT = new int[nTT];
        RSLTotalCount_EnhancedDisabilityPremiumAwardByTT = new int[nTT];
        RSLTotalCount_DisabilityAwardByTT = new int[nTT];
        RSLAllTotalCount_PSI = new int[nPSI];
        RSLHBTotalCount_PSI = new int[nPSI];
        RSLCTBTotalCount_PSI = new int[nPSI];
        RSLAllTotalCount_PSIByTT = new int[nPSI][nTT];
        RSLHBTotalCount_PSIByTT = new int[nPSI][nTT];
        RSLCTBTotalCount_PSIByTT = new int[nPSI][nTT];
        RSLTotalCount_TTClaimant1 = new int[nTT];
        RSLTotalCount_TTClaimant0 = new int[nTT];
        RSLHBEthnicGroupCount = new int[nEG];
        RSLCTBEthnicGroupCount = new int[nEG];
    }

    @Override
    protected void initSingleTimeStrings(int nTT, int nEG, int nPSI) {
        super.initSingleTimeStrings(nTT, nEG, nPSI);
        // Council
        sCouncilTotalIncomeTT = new String[nTT];
        sCouncilTotalCount_IncomeNonZeroTT = new String[nTT];
        sCouncilTotalCount_IncomeZeroTT = new String[nTT];
        sCouncilAverageIncomeTT = new String[nTT];
        sCouncilTotalWeeklyEligibleRentAmountTT = new String[nTT];
        sCouncilTotalCount_WeeklyEligibleRentAmountNonZeroTT = new String[nTT];
        sCouncilTotalCount_WeeklyEligibleRentAmountZeroTT = new String[nTT];
        sCouncilAverageWeeklyEligibleRentAmountTT = new String[nTT];
        sCouncilAllTotalCount_PSI = new String[nPSI];
        sCouncilHBTotalCount_PSI = new String[nPSI];
        sCouncilCTBTotalCount_PSI = new String[nPSI];
        sCouncilAllPercentageOfAll_PSI = new String[nPSI];
        sCouncilHBPercentageOfHB_PSI = new String[nPSI];
        sCouncilCTBPercentageOfCTB_PSI = new String[nPSI];
        sCouncilTotalCount_PSIByTT = new String[nPSI][nTT];
        sCouncilPercentageOfAll_PSIByTT = new String[nPSI][nTT];
        sCouncilPercentageOfHB_PSIByTT = new String[nPSI][nTT];
        sCouncilPercentageOfCTB_PSIByTT = new String[nPSI][nTT];
        for (int i = 1; i < nPSI; i++) {
            sCouncilAllTotalCount_PSI[i] = "CouncilAllTotalCount_PSI" + i;
            sCouncilHBTotalCount_PSI[i] = "CouncilHBTotalCount_PSI" + i;
            sCouncilCTBTotalCount_PSI[i] = "CouncilCTBTotalCount_PSI" + i;
            sCouncilAllPercentageOfAll_PSI[i] = "CouncilAllPercentageOfAll_PSI" + i;
            sCouncilHBPercentageOfHB_PSI[i] = "CouncilHBPercentageOfHB_PSI" + i;
            sCouncilCTBPercentageOfCTB_PSI[i] = "CouncilCTBPercentageOfCTB_PSI" + i;
            for (int j = 1; j < nTT; j++) {
                sCouncilTotalCount_PSIByTT[i][j] = "CouncilTotalCount_PSI" + i + "TT" + j;
                sCouncilPercentageOfAll_PSIByTT[i][j] = "CouncilPercentageOfAll_PSI" + i + "TT" + j;
                if (j == 5 || j == 7) {
                    sCouncilPercentageOfCTB_PSIByTT[i][j] = "CouncilPercentageOfCTB_PSI" + i + "TT" + j;
                } else {
                    sCouncilPercentageOfHB_PSIByTT[i][j] = "CouncilPercentageOfHB_PSI" + i + "TT" + j;
                }
            }
        }
        // All
        sCouncilAllTotalCount_EthnicGroupClaimant = new String[nEG];
        sCouncilAllPercentageOfAll_EthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            sCouncilAllTotalCount_EthnicGroupClaimant[i] = "CouncilAllTotalCount_EthnicGroup" + i + "Claimant";
            sCouncilAllPercentageOfAll_EthnicGroupClaimant[i] = "CouncilAllPercentageOfAll_EthnicGroup" + i + "Claimant";
        }
        sCouncilTotalCount_ClaimantTT = new String[nTT];
        sCouncilPercentageOfAll_ClaimantTT = new String[nTT];
        sCouncilPercentageOfHB_ClaimantTT = new String[nTT];
        sCouncilPercentageOfCTB_ClaimantTT = new String[nTT];
        // DisabilityAward
        sCouncilTotalCount_DisabilityAwardByTT = new String[nTT];
        sCouncilPercentageOfAll_DisabilityAwardByTT = new String[nTT];
        sCouncilPercentageOfHB_DisabilityAwardByTT = new String[nTT];
        sCouncilPercentageOfCTB_DisabilityAwardByTT = new String[nTT];
        sCouncilPercentageOfTT_DisabilityAwardByTT = new String[nTT];
        // DisabilityPremiumAward
        sCouncilTotalCount_DisabilityPremiumAwardByTT = new String[nTT];
        sCouncilPercentageOfAll_DisabilityPremiumAwardByTT = new String[nTT];
        sCouncilPercentageOfHB_DisabilityPremiumAwardByTT = new String[nTT];
        sCouncilPercentageOfCTB_DisabilityPremiumAwardByTT = new String[nTT];
        sCouncilPercentageOfTT_DisabilityPremiumAwardByTT = new String[nTT];
        // SevereDisabilityPremiumAward
        sCouncilTotalCount_SevereDisabilityPremiumAwardByTT = new String[nTT];
        sCouncilPercentageOfAll_SevereDisabilityPremiumAwardByTT = new String[nTT];
        sCouncilPercentageOfHB_SevereDisabilityPremiumAwardByTT = new String[nTT];
        sCouncilPercentageOfCTB_SevereDisabilityPremiumAwardByTT = new String[nTT];
        sCouncilPercentageOfTT_SevereDisabilityPremiumAwardByTT = new String[nTT];
        // DisabledChildPremiumAward
        sCouncilTotalCount_DisabledChildPremiumAwardByTT = new String[nTT];
        sCouncilPercentageOfAll_DisabledChildPremiumAwardByTT = new String[nTT];
        sCouncilPercentageOfHB_DisabledChildPremiumAwardByTT = new String[nTT];
        sCouncilPercentageOfCTB_DisabledChildPremiumAwardByTT = new String[nTT];
        sCouncilPercentageOfTT_DisabledChildPremiumAwardByTT = new String[nTT];
        // EnhancedDisabilityPremiumAward
        sCouncilTotalCount_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sCouncilPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sCouncilPercentageOfTT_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            // Claimants
            sCouncilTotalCount_ClaimantTT[i] = "CouncilTotalCount_ClaimantTT" + i;
            sCouncilPercentageOfAll_ClaimantTT[i] = "CouncilPercentageOfAll_ClaimantTT" + i;
            sCouncilPercentageOfHB_ClaimantTT[i] = "CouncilPercentageOfHB_ClaimantTT" + i;
            sCouncilPercentageOfCTB_ClaimantTT[i] = "CouncilPercentageOfCTB_ClaimantTT" + i;
            // Income
            sCouncilTotalIncomeTT[i] = "CouncilTotalIncomeTT" + i;
            sCouncilTotalCount_IncomeNonZeroTT[i] = "CouncilTotalCount_IncomeNonZeroTT" + i;
            sCouncilTotalCount_IncomeZeroTT[i] = "CouncilTotalCount_IncomeZeroTT" + i;
            sCouncilAverageIncomeTT[i] = "CouncilAverageIncomeTT" + i;
            // WeeklyEligibleRentAmountTT
            sCouncilTotalWeeklyEligibleRentAmountTT[i] = "CouncilTotalWeeklyEligibleRentAmountTT" + i;
            sCouncilTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] = "CouncilTotalCount_WeeklyEligibleRentAmountNonZeroTT" + i;
            sCouncilTotalCount_WeeklyEligibleRentAmountZeroTT[i] = "CouncilTotalCount_WeeklyEligibleRentAmountZeroTT" + i;
            sCouncilAverageWeeklyEligibleRentAmountTT[i] = "CouncilAverageWeeklyEligibleRentAmountTT" + i;
            // DisabilityAwardByTT
            sCouncilTotalCount_DisabilityAwardByTT[i] = "CouncilTotalCount_DisabilityAwardByTT" + i;
            sCouncilPercentageOfAll_DisabilityAwardByTT[i] = "CouncilPercentageOfAll_DisabilityAwardByTT" + i;
            sCouncilPercentageOfHB_DisabilityAwardByTT[i] = "CouncilPercentageOfHB_DisabilityAwardByTT" + i;
            sCouncilPercentageOfCTB_DisabilityAwardByTT[i] = "CouncilPercentageOfCTB_DisabilityAwardByTT" + i;
            sCouncilPercentageOfTT_DisabilityAwardByTT[i] = "CouncilPercentageOfTT_DisabilityAwardByTT" + i;
            // DisabilityPremiumAwardByTT
            sCouncilTotalCount_DisabilityPremiumAwardByTT[i] = "CouncilTotalCount_DisabilityPremiumAwardByTT" + i;
            sCouncilPercentageOfAll_DisabilityPremiumAwardByTT[i] = "CouncilPercentageOfAll_DisabilityPremiumAwardByTT" + i;
            sCouncilPercentageOfHB_DisabilityPremiumAwardByTT[i] = "CouncilPercentageOfHB_DisabilityPremiumAwardByTT" + i;
            sCouncilPercentageOfCTB_DisabilityPremiumAwardByTT[i] = "CouncilPercentageOfCTB_DisabilityPremiumAwardByTT" + i;
            sCouncilPercentageOfTT_DisabilityPremiumAwardByTT[i] = "CouncilPercentageOfTT_DisabilityPremiumAwardByTT" + i;
            // SevereDisabilityPremiumAwardByTT
            sCouncilTotalCount_SevereDisabilityPremiumAwardByTT[i] = "CouncilTotalCount_SevereDisabilityPremiumAwardByTT" + i;
            sCouncilPercentageOfAll_SevereDisabilityPremiumAwardByTT[i] = "CouncilPercentageOfAll_SevereDisabilityPremiumAwardByTT" + i;
            sCouncilPercentageOfHB_SevereDisabilityPremiumAwardByTT[i] = "CouncilPercentageOfHB_SevereDisabilityPremiumAwardByTT" + i;
            sCouncilPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i] = "CouncilPercentageOfCTB_SevereDisabilityPremiumAwardByTT" + i;
            sCouncilPercentageOfTT_SevereDisabilityPremiumAwardByTT[i] = "CouncilPercentageOfTT_SevereDisabilityPremiumAwardByTT" + i;
            // DisabledChildPremiumAwardByTT
            sCouncilTotalCount_DisabledChildPremiumAwardByTT[i] = "CouncilTotalCount_DisabledChildPremiumAwardByTT" + i;
            sCouncilPercentageOfAll_DisabledChildPremiumAwardByTT[i] = "CouncilPercentageOfAll_DisabledChildPremiumAwardByTT" + i;
            sCouncilPercentageOfHB_DisabledChildPremiumAwardByTT[i] = "CouncilPercentageOfHB_DisabledChildPremiumAwardByTT" + i;
            sCouncilPercentageOfCTB_DisabledChildPremiumAwardByTT[i] = "CouncilPercentageOfCTB_DisabledChildPremiumAwardByTT" + i;
            sCouncilPercentageOfTT_DisabledChildPremiumAwardByTT[i] = "CouncilPercentageOfTT_DisabledChildPremiumAwardByTT" + i;
            // EnhancedDisabilityPremiumAwardByTT
            sCouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[i] = "CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT" + i;
            sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i] = "CouncilPercentageOfAll_EnhancedDisabilityPremiumAwardByTT" + i;
            sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i] = "CouncilPercentageOfHB_EnhancedDisabilityPremiumAwardByTT" + i;
            sCouncilPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i] = "CouncilPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT" + i;
            sCouncilPercentageOfTT_EnhancedDisabilityPremiumAwardByTT[i] = "CouncilPercentageOfTT_EnhancedDisabilityPremiumAwardByTT" + i;
        }
        sCouncilAllTotalCount_PostcodeValidFormat = "CouncilAllTotalCount_PostcodeValidFormat";
        sCouncilAllTotalCount_PostcodeValid = "CouncilAllTotalCount_PostcodeValid";
        // HB
        sCouncilHBTotalCount_EthnicGroupClaimant = new String[nEG];
        sCouncilHBPercentageOfHB_EthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            sCouncilHBTotalCount_EthnicGroupClaimant[i] = "CouncilHBTotalCount_EthnicGroup" + i + "Claimant";
            sCouncilHBPercentageOfHB_EthnicGroupClaimant[i] = "CouncilHBPercentageOfHB_EthnicGroup" + i + "Claimant";
        }
        sCouncilHBTotalCount_PostcodeValidFormat = "CouncilHBTotalCount_PostcodeValidFormat";
        sCouncilHBTotalCount_PostcodeValid = "CouncilHBTotalCount_PostcodeValid";
        // CTB
        sCouncilCTBTotalCount_EthnicGroupClaimant = new String[nEG];
        sCouncilCTBPercentageOfCTB_EthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            sCouncilCTBTotalCount_EthnicGroupClaimant[i] = "CouncilCTBTotalCount_ClaimantEthnicGroup" + i + "Claimant";
            sCouncilCTBPercentageOfCTB_EthnicGroupClaimant[i] = "CouncilCTBPercentageOfCTB_ClaimantEthnicGroup" + i + "Claimant";
        }
        sCouncilCTBTotalCount_TTClaimant = new String[nTT];
        sCouncilCTBPercentageOfCTB_TTClaimant = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            sCouncilCTBTotalCount_TTClaimant[i] = "CouncilCTBTotalCount_TT" + i + "Claimant";
            sCouncilCTBPercentageOfCTB_TTClaimant[i] = "CouncilCTBPercentageOfCTB_TT" + i + "Claimant";
        }
        sCouncilCTBTotalCount_PostcodeValidFormat = "CouncilCTBTotalCount_PostcodeValidFormat";
        sCouncilCTBTotalCount_PostcodeValid = "CouncilCTBTotalCount_PostcodeValid";
        // RSL
        sRSLTotalIncomeTT = new String[nTT];
        sRSLTotalCount_IncomeNonZeroTT = new String[nTT];
        sRSLTotalCount_IncomeZeroTT = new String[nTT];
        sRSLAverageIncomeTT = new String[nTT];
        sRSLTotalWeeklyEligibleRentAmountTT = new String[nTT];
        sRSLTotalCount_WeeklyEligibleRentAmountNonZeroTT = new String[nTT];
        sRSLTotalCount_WeeklyEligibleRentAmountZeroTT = new String[nTT];
        sRSLAverageWeeklyEligibleRentAmountTT = new String[nTT];
        sRSLAllTotalCount_PSI = new String[nPSI];
        sRSLHBTotalCount_PSI = new String[nPSI];
        sRSLCTBTotalCount_PSI = new String[nPSI];
        sRSLAllPercentageOfAll_PSI = new String[nPSI];
        sRSLHBPercentageOfHB_PSI = new String[nPSI];
        sRSLCTBPercentageOfCTB_PSI = new String[nPSI];
        sRSLTotalCount_PSIByTT = new String[nPSI][nTT];
        sRSLPercentageOfAll_PSIByTT = new String[nPSI][nTT];
        sRSLPercentageOfHB_PSIByTT = new String[nPSI][nTT];
        sRSLPercentageOfCTB_PSIByTT = new String[nPSI][nTT];
        for (int i = 1; i < nPSI; i++) {
            sRSLAllTotalCount_PSI[i] = "RSLAllTotalCount_PSI" + i;
            sRSLHBTotalCount_PSI[i] = "RSLHBTotalCount_PSI" + i;
            sRSLCTBTotalCount_PSI[i] = "RSLCTBTotalCount_PSI" + i;
            sRSLAllPercentageOfAll_PSI[i] = "RSLAllPercentageOfAll_PSI" + i;
            sRSLHBPercentageOfHB_PSI[i] = "RSLHBPercentageOfHB_PSI" + i;
            sRSLCTBPercentageOfCTB_PSI[i] = "RSLCTBPercentageOfCTB_PSI" + i;
            for (int j = 1; j < nTT; j++) {
                sRSLTotalCount_PSIByTT[i][j] = "RSLTotalCount_PSI" + i + "TT" + j;
                sRSLPercentageOfAll_PSIByTT[i][j] = "RSLPercentageOfAll_PSI" + i + "TT" + j;
                if (j == 5 || j == 7) {
                    sRSLPercentageOfCTB_PSIByTT[i][j] = "RSLPercentageOfCTB_PSI" + i + "TT" + j;
                } else {
                    sRSLPercentageOfHB_PSIByTT[i][j] = "RSLPercentageOfHB_PSI" + i + "TT" + j;
                }
            }
        }
        // All
        sRSLAllTotalCount_EthnicGroupClaimant = new String[nEG];
        sRSLAllPercentageOfAll_EthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            sRSLAllTotalCount_EthnicGroupClaimant[i] = "RSLAllTotalCount_EthnicGroup" + i + "Claimant";
            sRSLAllPercentageOfAll_EthnicGroupClaimant[i] = "RSLAllPercentageOfAll_EthnicGroup" + i + "Claimant";
        }
        sRSLTotalCount_ClaimantTT = new String[nTT];
        sRSLPercentageOfAll_ClaimantTT = new String[nTT];
        sRSLPercentageOfHB_ClaimantTT = new String[nTT];
        sRSLPercentageOfCTB_ClaimantTT = new String[nTT];
        // DisabilityAward
        sRSLTotalCount_DisabilityAwardByTT = new String[nTT];
        sRSLPercentageOfAll_DisabilityAwardByTT = new String[nTT];
        sRSLPercentageOfHB_DisabilityAwardByTT = new String[nTT];
        sRSLPercentageOfCTB_DisabilityAwardByTT = new String[nTT];
        sRSLPercentageOfTT_DisabilityAwardByTT = new String[nTT];
        // DisabilityPremiumAward
        sRSLTotalCount_DisabilityPremiumAwardByTT = new String[nTT];
        sRSLPercentageOfAll_DisabilityPremiumAwardByTT = new String[nTT];
        sRSLPercentageOfHB_DisabilityPremiumAwardByTT = new String[nTT];
        sRSLPercentageOfCTB_DisabilityPremiumAwardByTT = new String[nTT];
        sRSLPercentageOfTT_DisabilityPremiumAwardByTT = new String[nTT];
        // SevereDisabilityPremiumAward
        sRSLTotalCount_SevereDisabilityPremiumAwardByTT = new String[nTT];
        sRSLPercentageOfAll_SevereDisabilityPremiumAwardByTT = new String[nTT];
        sRSLPercentageOfHB_SevereDisabilityPremiumAwardByTT = new String[nTT];
        sRSLPercentageOfCTB_SevereDisabilityPremiumAwardByTT = new String[nTT];
        sRSLPercentageOfTT_SevereDisabilityPremiumAwardByTT = new String[nTT];
        // DisabledChildPremiumAward
        sRSLTotalCount_DisabledChildPremiumAwardByTT = new String[nTT];
        sRSLPercentageOfAll_DisabledChildPremiumAwardByTT = new String[nTT];
        sRSLPercentageOfHB_DisabledChildPremiumAwardByTT = new String[nTT];
        sRSLPercentageOfCTB_DisabledChildPremiumAwardByTT = new String[nTT];
        sRSLPercentageOfTT_DisabledChildPremiumAwardByTT = new String[nTT];
        // EnhancedDisabilityPremiumAward
        sRSLTotalCount_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sRSLPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        sRSLPercentageOfTT_EnhancedDisabilityPremiumAwardByTT = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            // Claimants
            sRSLTotalCount_ClaimantTT[i] = "RSLTotalCount_ClaimantTT" + i;
            sRSLPercentageOfAll_ClaimantTT[i] = "RSLPercentageOfAll_ClaimantTT" + i;
            sRSLPercentageOfHB_ClaimantTT[i] = "RSLPercentageOfHB_ClaimantTT" + i;
            sRSLPercentageOfCTB_ClaimantTT[i] = "RSLPercentageOfCTB_ClaimantTT" + i;
            // Income
            sRSLTotalIncomeTT[i] = "RSLTotalIncomeTT" + i;
            sRSLTotalCount_IncomeNonZeroTT[i] = "RSLTotalCount_IncomeNonZeroTT" + i;
            sRSLTotalCount_IncomeZeroTT[i] = "RSLTotalCount_IncomeZeroTT" + i;
            sRSLAverageIncomeTT[i] = "RSLAverageIncomeTT" + i;
            // WeeklyEligibleRentAmountTT
            sRSLTotalWeeklyEligibleRentAmountTT[i] = "RSLTotalWeeklyEligibleRentAmountTT" + i;
            sRSLTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] = "RSLTotalCount_WeeklyEligibleRentAmountNonZeroTT" + i;
            sRSLTotalCount_WeeklyEligibleRentAmountZeroTT[i] = "RSLTotalCount_WeeklyEligibleRentAmountZeroTT" + i;
            sRSLAverageWeeklyEligibleRentAmountTT[i] = "RSLAverageWeeklyEligibleRentAmountTT" + i;
            // DisabilityAwardByTT
            sRSLTotalCount_DisabilityAwardByTT[i] = "RSLTotalCount_DisabilityAwardByTT" + i;
            sRSLPercentageOfAll_DisabilityAwardByTT[i] = "RSLPercentageOfAll_DisabilityAwardByTT" + i;
            sRSLPercentageOfHB_DisabilityAwardByTT[i] = "RSLPercentageOfHB_DisabilityAwardByTT" + i;
            sRSLPercentageOfCTB_DisabilityAwardByTT[i] = "RSLPercentageOfCTB_DisabilityAwardByTT" + i;
            sRSLPercentageOfTT_DisabilityAwardByTT[i] = "RSLPercentageOfTT_DisabilityAwardByTT" + i;
            // DisabilityPremiumAwardByTT
            sRSLTotalCount_DisabilityPremiumAwardByTT[i] = "RSLTotalCount_DisabilityPremiumAwardByTT" + i;
            sRSLPercentageOfAll_DisabilityPremiumAwardByTT[i] = "RSLPercentageOfAll_DisabilityPremiumAwardByTT" + i;
            sRSLPercentageOfHB_DisabilityPremiumAwardByTT[i] = "RSLPercentageOfHB_DisabilityPremiumAwardByTT" + i;
            sRSLPercentageOfCTB_DisabilityPremiumAwardByTT[i] = "RSLPercentageOfCTB_DisabilityPremiumAwardByTT" + i;
            sRSLPercentageOfTT_DisabilityPremiumAwardByTT[i] = "RSLPercentageOfTT_DisabilityPremiumAwardByTT" + i;
            // SevereDisabilityPremiumAwardByTT
            sRSLTotalCount_SevereDisabilityPremiumAwardByTT[i] = "RSLTotalCount_SevereDisabilityPremiumAwardByTT" + i;
            sRSLPercentageOfAll_SevereDisabilityPremiumAwardByTT[i] = "RSLPercentageOfAll_SevereDisabilityPremiumAwardByTT" + i;
            sRSLPercentageOfHB_SevereDisabilityPremiumAwardByTT[i] = "RSLPercentageOfHB_SevereDisabilityPremiumAwardByTT" + i;
            sRSLPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i] = "RSLPercentageOfCTB_SevereDisabilityPremiumAwardByTT" + i;
            sRSLPercentageOfTT_SevereDisabilityPremiumAwardByTT[i] = "RSLPercentageOfTT_SevereDisabilityPremiumAwardByTT" + i;
            // DisabledChildPremiumAwardByTT
            sRSLTotalCount_DisabledChildPremiumAwardByTT[i] = "RSLTotalCount_DisabledChildPremiumAwardByTT" + i;
            sRSLPercentageOfAll_DisabledChildPremiumAwardByTT[i] = "RSLPercentageOfAll_DisabledChildPremiumAwardByTT" + i;
            sRSLPercentageOfHB_DisabledChildPremiumAwardByTT[i] = "RSLPercentageOfHB_DisabledChildPremiumAwardByTT" + i;
            sRSLPercentageOfCTB_DisabledChildPremiumAwardByTT[i] = "RSLPercentageOfCTB_DisabledChildPremiumAwardByTT" + i;
            sRSLPercentageOfTT_DisabledChildPremiumAwardByTT[i] = "RSLPercentageOfTT_DisabledChildPremiumAwardByTT" + i;
            // EnhancedDisabilityPremiumAwardByTT
            sRSLTotalCount_EnhancedDisabilityPremiumAwardByTT[i] = "RSLTotalCount_EnhancedDisabilityPremiumAwardByTT" + i;
            sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i] = "RSLPercentageOfAll_EnhancedDisabilityPremiumAwardByTT" + i;
            sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i] = "RSLPercentageOfHB_EnhancedDisabilityPremiumAwardByTT" + i;
            sRSLPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i] = "RSLPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT" + i;
            sRSLPercentageOfTT_EnhancedDisabilityPremiumAwardByTT[i] = "RSLPercentageOfTT_EnhancedDisabilityPremiumAwardByTT" + i;
        }
        sRSLAllTotalCount_PostcodeValidFormat = "RSLAllTotalCount_PostcodeValidFormat";
        sRSLAllTotalCount_PostcodeValid = "RSLAllTotalCount_PostcodeValid";
        // HB
        sRSLHBTotalCount_EthnicGroupClaimant = new String[nEG];
        sRSLHBPercentageOfHB_EthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            sRSLHBTotalCount_EthnicGroupClaimant[i] = "RSLHBTotalCount_EthnicGroup" + i + "Claimant";
            sRSLHBPercentageOfHB_EthnicGroupClaimant[i] = "RSLHBPercentageOfHB_EthnicGroup" + i + "Claimant";
        }

        sRSLHBTotalCount_PostcodeValidFormat = "RSLHBTotalCount_PostcodeValidFormat";
        sRSLHBTotalCount_PostcodeValid = "RSLHBTotalCount_PostcodeValid";
        // CTB
        sRSLCTBTotalCount_EthnicGroupClaimant = new String[nEG];
        sRSLCTBPercentageOfCTB_EthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            sRSLCTBTotalCount_EthnicGroupClaimant[i] = "RSLCTBTotalCount_EthnicGroup" + i + "Claimant";
            sRSLCTBPercentageOfCTB_EthnicGroupClaimant[i] = "RSLCTBPercentageOfCTB_EthnicGroup" + i + "Claimant";
        }
        sCTBTotalCount_TTClaimant = new String[nTT];
        sRSLCTBPercentageOfCTB_TTClaimant = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            sRSLCTBTotalCount_TTClaimant[i] = "RSLCTBTotalCount_TT" + i + "Claimant";
            sRSLCTBPercentageOfCTB_TTClaimant[i] = "RSLCTBPercentageOfCTB_TT" + i + "Claimant";
        }
        sRSLCTBTotalCount_PostcodeValidFormat = "RSLCTBTotalCount_PostcodeValidFormat";
        sRSLCTBTotalCount_PostcodeValid = "RSLCTBTotalCount_PostcodeValid";
    }

    @Override
    protected void initCounts(int nTT, int nEG, int nPSI) {
        initSingleTimeCounts(nTT, nEG, nPSI);
        initCompare2TimesCounts();
        initCompare3TimesCounts(nTT);
    }

    @Override
    protected void initSingleTimeCounts(int nTT, int nEG, int nPSI) {
        super.initSingleTimeCounts(nTT, nEG, nPSI);
        // Council
        for (int i = 1; i < nPSI; i++) {
            CouncilAllTotalCount_PSI[i] = 0;
            CouncilHBTotalCount_PSI[i] = 0;
            CouncilCTBTotalCount_PSI[i] = 0;
            for (int j = 1; j < nTT; j++) {
                CouncilAllTotalCount_PSIByTT[i][j] = 0;
                CouncilHBTotalCount_PSIByTT[i][j] = 0;
                CouncilCTBTotalCount_PSIByTT[i][j] = 0;
            }
        }
        // All
        CouncilAllTotalWeeklyHBEntitlement = 0.0d;
        CouncilAllTotalWeeklyHBEntitlementNonZeroCount = 0;
        CouncilAllTotalWeeklyHBEntitlementZeroCount = 0;
        CouncilAllTotalWeeklyCTBEntitlement = 0.0d;
        CouncilAllTotalCount_WeeklyCTBEntitlementNonZero = 0;
        CouncilAllTotalWeeklyCTBEntitlementZeroCount = 0;
        CouncilAllTotalWeeklyEligibleRentAmount = 0.0d;
        CouncilAllTotalWeeklyEligibleRentAmountNonZeroCount = 0;
        CouncilAllTotalWeeklyEligibleRentAmountZeroCount = 0;
        CouncilAllTotalWeeklyEligibleCouncilTaxAmount = 0.0d;
        CouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = 0;
        CouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountZero = 0;
        CouncilAllTotalContractualRentAmount = 0.0d;
        CouncilAllTotalContractualRentAmountNonZeroCount = 0;
        CouncilAllTotalContractualRentAmountZeroCount = 0;
        CouncilAllTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;
        // HB
        CouncilHBTotalWeeklyHBEntitlement = 0.0d;
        CouncilHBTotalCount_WeeklyHBEntitlementNonZero = 0;
        CouncilHBTotalCount_WeeklyHBEntitlementZero = 0;
        CouncilHBTotalWeeklyCTBEntitlement = 0.0d;
        CouncilHBTotalCount_WeeklyCTBEntitlementNonZero = 0;
        CouncilHBTotalWeeklyCTBEntitlementZeroCount = 0;
        CouncilHBTotalWeeklyEligibleRentAmount = 0.0d;
        CouncilHBTotalWeeklyEligibleRentAmountNonZeroCount = 0;
        CouncilHBTotalWeeklyEligibleRentAmountZeroCount = 0;
        CouncilHBTotalWeeklyEligibleCouncilTaxAmount = 0.0d;
        CouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = 0;
        CouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountZero = 0;
        CouncilHBTotalContractualRentAmount = 0.0d;
        CouncilHBTotalContractualRentAmountNonZeroCount = 0;
        CouncilHBTotalContractualRentAmountZeroCount = 0;
        CouncilHBTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;
        // CTB
        CouncilCTBTotalWeeklyHBEntitlement = 0.0d;
        CouncilCTBTotalCount_WeeklyHBEntitlementNonZero = 0;
        CouncilCTBTotalWeeklyHBEntitlementZeroCount = 0;
        CouncilCTBTotalWeeklyCTBEntitlement = 0.0d;
        CouncilCTBTotalCount_WeeklyCTBEntitlementNonZero = 0;
        CouncilCTBTotalWeeklyCTBEntitlementZeroCount = 0;
        CouncilCTBTotalWeeklyEligibleRentAmount = 0.0d;
        CouncilCTBTotalWeeklyEligibleRentAmountNonZeroCount = 0;
        CouncilCTBTotalWeeklyEligibleRentAmountZeroCount = 0;
        CouncilCTBTotalWeeklyEligibleCouncilTaxAmount = 0.0d;
        CouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = 0;
        CouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero = 0;
        CouncilCTBTotalContractualRentAmount = 0.0d;
        CouncilCTBTotalContractualRentAmountNonZeroCount = 0;
        CouncilCTBTotalContractualRentAmountZeroCount = 0;
        CouncilCTBTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;
        // Demographic Counts
        CouncilHBTotalCount_EmployedClaimants = 0;
        CouncilHBTotalCount_SelfEmployedClaimants = 0;
        CouncilHBTotalCount_StudentsClaimants = 0;
        CouncilHBTotalCount_LHACases = 0;
        CouncilCTBTotalCount_EmployedClaimants = 0;
        CouncilCTBTotalCount_SelfEmployedClaimants = 0;
        CouncilCTBTotalCount_StudentsClaimants = 0;
        CouncilCTBTotalCount_LHACases = 0;
        for (int i = 1; i < nTT; i++) {
            CouncilTotalCount_TTClaimant1[i] = 0;
            CouncilTotalCount_DisabilityPremiumAwardByTT[i] = 0;
            CouncilTotalCount_SevereDisabilityPremiumAwardByTT[i] = 0;
            CouncilTotalCount_DisabledChildPremiumAwardByTT[i] = 0;
            CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[i] = 0;
            CouncilTotalCount_DisabilityAwardByTT[i] = 0;
        }
        // Ethnicity
        for (int i = 1; i < nEG; i++) {
            CouncilHBEthnicGroupCount[i] = 0;
            CouncilCTBEthnicGroupCount[i] = 0;
        }
        // Key Counts
        CouncilAllCount1 = 0;
        CouncilHBCount1 = 0;
        CouncilCTBCount1 = 0;
        // Postcode Counts
        CouncilHBTotalCount_PostcodeValidFormat = 0;
        CouncilHBTotalCount_PostcodeValid = 0;
        CouncilHBTotalCount_TTChangeClaimant = 0;
        CouncilCTBTotalCount_PostcodeValidFormat = 0;
        CouncilCTBTotalCount_PostcodeValid = 0;
        CouncilCTBTotalCount_TTChangeClaimant = 0;
        // Household Size
        CouncilAllTotalHouseholdSize = 0L;
        CouncilHBTotalHouseholdSize = 0L;
        CouncilCTBTotalHouseholdSize = 0L;

        // RSL
        for (int i = 1; i < nPSI; i++) {
            RSLAllTotalCount_PSI[i] = 0;
            RSLHBTotalCount_PSI[i] = 0;
            RSLCTBTotalCount_PSI[i] = 0;
            for (int j = 1; j < nTT; j++) {
                RSLAllTotalCount_PSIByTT[i][j] = 0;
                RSLHBTotalCount_PSIByTT[i][j] = 0;
                RSLCTBTotalCount_PSIByTT[i][j] = 0;
            }
        }
        // All
        RSLAllTotalWeeklyHBEntitlement = 0.0d;
        RSLAllTotalWeeklyHBEntitlementNonZeroCount = 0;
        RSLAllTotalWeeklyHBEntitlementZeroCount = 0;
        RSLAllTotalWeeklyCTBEntitlement = 0.0d;
        RSLAllTotalCount_WeeklyCTBEntitlementNonZero = 0;
        RSLAllTotalWeeklyCTBEntitlementZeroCount = 0;
        RSLAllTotalWeeklyEligibleRentAmount = 0.0d;
        RSLAllTotalWeeklyEligibleRentAmountNonZeroCount = 0;
        RSLAllTotalWeeklyEligibleRentAmountZeroCount = 0;
        RSLAllTotalWeeklyEligibleCouncilTaxAmount = 0.0d;
        RSLAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = 0;
        RSLAllTotalCount_WeeklyEligibleCouncilTaxAmountZero = 0;
        RSLAllTotalContractualRentAmount = 0.0d;
        RSLAllTotalContractualRentAmountNonZeroCount = 0;
        RSLAllTotalContractualRentAmountZeroCount = 0;
        RSLAllTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        RSLAllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        RSLAllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        RSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        RSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;
        // HB
        RSLHBTotalWeeklyHBEntitlement = 0.0d;
        RSLHBTotalCount_WeeklyHBEntitlementNonZero = 0;
        RSLHBTotalCount_WeeklyHBEntitlementZero = 0;
        RSLHBTotalWeeklyCTBEntitlement = 0.0d;
        RSLHBTotalCount_WeeklyCTBEntitlementNonZero = 0;
        RSLHBTotalWeeklyCTBEntitlementZeroCount = 0;
        RSLHBTotalWeeklyEligibleRentAmount = 0.0d;
        RSLHBTotalWeeklyEligibleRentAmountNonZeroCount = 0;
        RSLHBTotalWeeklyEligibleRentAmountZeroCount = 0;
        RSLHBTotalWeeklyEligibleCouncilTaxAmount = 0.0d;
        RSLHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = 0;
        RSLHBTotalCount_WeeklyEligibleCouncilTaxAmountZero = 0;
        RSLHBTotalContractualRentAmount = 0.0d;
        RSLHBTotalContractualRentAmountNonZeroCount = 0;
        RSLHBTotalContractualRentAmountZeroCount = 0;
        RSLHBTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        RSLHBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        RSLHBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        RSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        RSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;
        // CTB
        RSLCTBTotalWeeklyHBEntitlement = 0.0d;
        RSLCTBTotalCount_WeeklyHBEntitlementNonZero = 0;
        RSLCTBTotalWeeklyHBEntitlementZeroCount = 0;
        RSLCTBTotalWeeklyCTBEntitlement = 0.0d;
        RSLCTBTotalCount_WeeklyCTBEntitlementNonZero = 0;
        RSLCTBTotalWeeklyCTBEntitlementZeroCount = 0;
        RSLCTBTotalWeeklyEligibleRentAmount = 0.0d;
        RSLCTBTotalWeeklyEligibleRentAmountNonZeroCount = 0;
        RSLCTBTotalWeeklyEligibleRentAmountZeroCount = 0;
        RSLCTBTotalWeeklyEligibleCouncilTaxAmount = 0.0d;
        RSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = 0;
        RSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero = 0;
        RSLCTBTotalContractualRentAmount = 0.0d;
        RSLCTBTotalContractualRentAmountNonZeroCount = 0;
        RSLCTBTotalContractualRentAmountZeroCount = 0;
        RSLCTBTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;
        // Demographic Counts
        RSLHBTotalCount_EmployedClaimants = 0;
        RSLHBTotalCount_SelfEmployedClaimants = 0;
        RSLHBTotalCount_StudentsClaimants = 0;
        RSLHBTotalCount_LHACases = 0;
        RSLCTBTotalCount_EmployedClaimants = 0;
        RSLCTBTotalCount_SelfEmployedClaimants = 0;
        RSLCTBTotalCount_StudentsClaimants = 0;
        RSLCTBTotalCount_LHACases = 0;
        for (int i = 1; i < nTT; i++) {
            RSLTotalCount_TTClaimant1[i] = 0;
            RSLTotalCount_DisabilityPremiumAwardByTT[i] = 0;
            RSLTotalCount_SevereDisabilityPremiumAwardByTT[i] = 0;
            RSLTotalCount_DisabledChildPremiumAwardByTT[i] = 0;
            RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[i] = 0;
            RSLTotalCount_DisabilityAwardByTT[i] = 0;
        }
        // Ethnicity
        for (int i = 1; i < nEG; i++) {
            RSLHBEthnicGroupCount[i] = 0;
            RSLCTBEthnicGroupCount[i] = 0;
        }
        // Key Counts
        RSLAllCount1 = 0;
        RSLHBCount1 = 0;
        RSLCTBCount1 = 0;
        // Postcode Counts
        RSLHBTotalCount_PostcodeValidFormat = 0;
        RSLHBTotalCount_PostcodeValid = 0;
        RSLHBTotalCount_TTChangeClaimant = 0;
        RSLCTBTotalCount_PostcodeValidFormat = 0;
        RSLCTBTotalCount_PostcodeValid = 0;
        RSLCTBTotalCount_TTChangeClaimant = 0;
        // Household Size
        RSLAllTotalHouseholdSize = 0L;
        RSLHBTotalHouseholdSize = 0L;
        RSLCTBTotalHouseholdSize = 0L;

        // UO only
        AllUOCount1 = 0;
        CouncilCount1 = 0;
        RSLCount1 = 0;
        AllUOLinkedRecordCount1 = 0;
        CouncilLinkedRecordCount1 = 0;
        RSLLinkedRecordCount1 = 0;
        // RentArrears
        CouncilTotal_RentArrears = 0.0d;
        CouncilTotalCount_RentArrears = 0;
        CouncilTotalCount_RentArrearsZero = 0;
        CouncilTotalCount_RentArrearsNonZero = 0;
    }

    @Override
    protected void initCompare2TimesCounts() {
        super.initCompare2TimesCounts();
        // Council
        // General
        CouncilTotalCount_HBTTsToCTBTTs = 0;
        CouncilTotalCount_CTBTTsToHBTTs = 0;
        // General HB related
        CouncilTotalCount_Minus999TTToSocialTTs = 0;
        CouncilTotalCount_Minus999TTToPrivateDeregulatedTTs = 0;
        CouncilTotalCount_HBTTsToHBTTs = 0;
        CouncilTotalCount_HBTTsToMinus999TT = 0;
        CouncilTotalCount_SocialTTsToMinus999TT = 0;
        CouncilTotalCount_PrivateDeregulatedTTsToMinus999TT = 0;
        CouncilTotalCount_SocialTTsToPrivateDeregulatedTTs = 0;
        CouncilTotalCount_PrivateDeregulatedTTsToSocialTTs = 0;
        CouncilTotalCount_TT1ToPrivateDeregulatedTTs = 0;
        CouncilTotalCount_TT4ToPrivateDeregulatedTTs = 0;
        CouncilTotalCount_PrivateDeregulatedTTsToTT1 = 0;
        CouncilTotalCount_PrivateDeregulatedTTsToTT4 = 0;
        CouncilTotalCount_PostcodeChangeWithinSocialTTs = 0;
        CouncilTotalCount_PostcodeChangeWithinTT1 = 0;
        CouncilTotalCount_PostcodeChangeWithinTT4 = 0;
        CouncilTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs = 0;
        // General CTB related
        CouncilTotalCount_Minus999TTToCTBTTs = 0;
        CouncilTotalCount_SocialTTsToCTBTTs = 0;
        CouncilTotalCount_TT1ToCTBTTs = 0;
        CouncilTotalCount_TT4ToCTBTTs = 0;
        CouncilTotalCount_PrivateDeregulatedTTsToCTBTTs = 0;
        CouncilTotalCount_CTBTTsToSocialTTs = 0;
        CouncilTotalCount_CTBTTsToMinus999TT = 0;
        CouncilTotalCount_CTBTTsToTT1 = 0;
        CouncilTotalCount_CTBTTsToTT4 = 0;
        CouncilTotalCount_CTBTTsToPrivateDeregulatedTTs = 0;
        // TT1 TT4
        CouncilTotalCount_TT1ToTT4 = 0;
        CouncilTotalCount_TT4ToTT1 = 0;
        // All
        CouncilAllTotalCount_TTChangeClaimant = 0;
        CouncilAllTotalCount_Postcode0ValidPostcode1Valid = 0;
        CouncilAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        CouncilAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged = 0;
        CouncilAllTotalCount_Postcode0ValidPostcode1NotValid = 0;
        CouncilAllTotalCount_Postcode0NotValidPostcode1Valid = 0;
        CouncilAllTotalCount_Postcode0NotValidPostcode1NotValid = 0;
        CouncilAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        CouncilAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = 0;
        // HB
        CouncilHBTotalCount_TTChangeClaimant = 0;
        CouncilHBTotalCount_Postcode0ValidPostcode1Valid = 0;
        CouncilHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        CouncilHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged = 0;
        CouncilHBTotalCount_Postcode0ValidPostcode1NotValid = 0;
        CouncilHBTotalCount_Postcode0NotValidPostcode1Valid = 0;
        CouncilHBTotalCount_Postcode0NotValidPostcode1NotValid = 0;
        CouncilHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        CouncilHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = 0;
        // CTB
        CouncilCTBTotalCount_TTChangeClaimant = 0;
        CouncilCTBTotalCount_Postcode0ValidPostcode1Valid = 0;
        CouncilCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        CouncilCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged = 0;
        CouncilCTBTotalCount_Postcode0ValidPostcode1NotValid = 0;
        CouncilCTBTotalCount_Postcode0NotValidPostcode1Valid = 0;
        CouncilCTBTotalCount_Postcode0NotValidPostcode1NotValid = 0;
        CouncilCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        CouncilCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = 0;
        //  RSL
        // General
        RSLTotalCount_HBTTsToCTBTTs = 0;
        RSLTotalCount_CTBTTsToHBTTs = 0;
        // General HB related
        RSLTotalCount_Minus999TTToSocialTTs = 0;
        RSLTotalCount_Minus999TTToPrivateDeregulatedTTs = 0;
        RSLTotalCount_HBTTsToHBTTs = 0;
        RSLTotalCount_HBTTsToMinus999TT = 0;
        RSLTotalCount_SocialTTsToMinus999TT = 0;
        RSLTotalCount_PrivateDeregulatedTTsToMinus999TT = 0;
        RSLTotalCount_SocialTTsToPrivateDeregulatedTTs = 0;
        RSLTotalCount_PrivateDeregulatedTTsToSocialTTs = 0;
        RSLTotalCount_TT1ToPrivateDeregulatedTTs = 0;
        RSLTotalCount_TT4ToPrivateDeregulatedTTs = 0;
        RSLTotalCount_PrivateDeregulatedTTsToTT1 = 0;
        RSLTotalCount_PrivateDeregulatedTTsToTT4 = 0;
        RSLTotalCount_PostcodeChangeWithinSocialTTs = 0;
        RSLTotalCount_PostcodeChangeWithinTT1 = 0;
        RSLTotalCount_PostcodeChangeWithinTT4 = 0;
        RSLTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs = 0;
        // General CTB related
        RSLTotalCount_Minus999TTToCTBTTs = 0;
        RSLTotalCount_SocialTTsToCTBTTs = 0;
        RSLTotalCount_TT1ToCTBTTs = 0;
        RSLTotalCount_TT4ToCTBTTs = 0;
        RSLTotalCount_PrivateDeregulatedTTsToCTBTTs = 0;
        RSLTotalCount_CTBTTsToSocialTTs = 0;
        RSLTotalCount_CTBTTsToMinus999TT = 0;
        RSLTotalCount_CTBTTsToTT1 = 0;
        RSLTotalCount_CTBTTsToTT4 = 0;
        RSLTotalCount_CTBTTsToPrivateDeregulatedTTs = 0;
        // TT1 TT4
        RSLTotalCount_TT1ToTT4 = 0;
        RSLTotalCount_TT4ToTT1 = 0;
        // All
        RSLAllTotalCount_TTChangeClaimant = 0;
        RSLAllTotalCount_Postcode0ValidPostcode1Valid = 0;
        RSLAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        RSLAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged = 0;
        RSLAllTotalCount_Postcode0ValidPostcode1NotValid = 0;
        RSLAllTotalCount_Postcode0NotValidPostcode1Valid = 0;
        RSLAllTotalCount_Postcode0NotValidPostcode1NotValid = 0;
        RSLAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        RSLAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = 0;
        // HB
        RSLHBTotalCount_TTChangeClaimant = 0;
        RSLHBTotalCount_Postcode0ValidPostcode1Valid = 0;
        RSLHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        RSLHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged = 0;
        RSLHBTotalCount_Postcode0ValidPostcode1NotValid = 0;
        RSLHBTotalCount_Postcode0NotValidPostcode1Valid = 0;
        RSLHBTotalCount_Postcode0NotValidPostcode1NotValid = 0;
        RSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        RSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = 0;
        // CTB
        RSLCTBTotalCount_TTChangeClaimant = 0;
        RSLCTBTotalCount_Postcode0ValidPostcode1Valid = 0;
        RSLCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        RSLCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged = 0;
        RSLCTBTotalCount_Postcode0ValidPostcode1NotValid = 0;
        RSLCTBTotalCount_Postcode0NotValidPostcode1Valid = 0;
        RSLCTBTotalCount_Postcode0NotValidPostcode1NotValid = 0;
        RSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        RSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = 0;
    }

    @Override
    protected void initCompare3TimesCounts(int nTT) {
        super.initCompare3TimesCounts(nTT);
        // Council
        CouncilSamePostcodeIII = 0;
        CouncilSamePostcodeIOI = 0;
        CouncilSamePostcodeOIO = 0;
        CouncilSameTenancyIII = 0;
        CouncilSameTenancyIOI = 0;
        CouncilSameTenancyOIO = 0;
        CouncilSameTenancyAndPostcodeIII = 0;
        CouncilSameTenancyAndPostcodeIOI = 0;
        CouncilSameTenancyAndPostcodeOIO = 0;
        CouncilSameTenancyIIITT = new int[nTT];
        CouncilSameTenancyIOITT = new int[nTT];
        CouncilSameTenancyOIOTT = new int[nTT];
        CouncilSameTenancyAndPostcodeIIITT = new int[nTT];
        CouncilSameTenancyAndPostcodeIOITT = new int[nTT];
        CouncilSameTenancyAndPostcodeOIOTT = new int[nTT];
        for (int i = 1; i < nTT; i++) {
            CouncilSameTenancyIIITT[i] = 0;
            CouncilSameTenancyIOITT[i] = 0;
            CouncilSameTenancyOIOTT[i] = 0;
            CouncilSameTenancyAndPostcodeIIITT[i] = 0;
            CouncilSameTenancyAndPostcodeIOITT[i] = 0;
            CouncilSameTenancyAndPostcodeOIOTT[i] = 0;
        }
        // RSL
        RSLSamePostcodeIII = 0;
        RSLSamePostcodeIOI = 0;
        RSLSamePostcodeOIO = 0;
        RSLSameTenancyIII = 0;
        RSLSameTenancyIOI = 0;
        RSLSameTenancyOIO = 0;
        RSLSameTenancyAndPostcodeIII = 0;
        RSLSameTenancyAndPostcodeIOI = 0;
        RSLSameTenancyAndPostcodeOIO = 0;
        RSLSameTenancyIIITT = new int[nTT];
        RSLSameTenancyIOITT = new int[nTT];
        RSLSameTenancyOIOTT = new int[nTT];
        RSLSameTenancyAndPostcodeIIITT = new int[nTT];
        RSLSameTenancyAndPostcodeIOITT = new int[nTT];
        RSLSameTenancyAndPostcodeOIOTT = new int[nTT];
        for (int i = 1; i < nTT; i++) {
            RSLSameTenancyIIITT[i] = 0;
            RSLSameTenancyIOITT[i] = 0;
            RSLSameTenancyOIOTT[i] = 0;
            RSLSameTenancyAndPostcodeIIITT[i] = 0;
            RSLSameTenancyAndPostcodeIOITT[i] = 0;
            RSLSameTenancyAndPostcodeOIOTT[i] = 0;
        }
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
            } else {
                // There are a few cases where a mapping to the SHBE does not exist!
                int debug = 1;//?
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
            } else {
                // There are a few cases where a mapping to the SHBE does not exist!
                int debug = 1;//?
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
            } else {
                // There are a few cases where a mapping to the SHBE does not exist!
                int debug = 1;//?
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
            } else {
                // There are a few cases where a mapping to the SHBE does not exist!
                int debug = 1;//?
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
            } else {
                // There are a few cases where a mapping to the SHBE does not exist!
                int debug = 1;//?
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
            } else {
                // There are a few cases where a mapping to the SHBE does not exist!
                int debug = 1;//?
            }
        }
        return result;
    }

    @Override
    protected void addToSummaryCompare2Times(
            int nTT,
            int nEG,
            int nPSI,
            HashMap<String, String> summary) {
        super.addToSummaryCompare2Times(nTT, nEG, nPSI, summary);
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

    protected void addToSummarySingleTimeRentArrears(
            HashMap<String, String> summary) {
        summary.put(
                sCouncilTotal_RentArrears,
                "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                        BigDecimal.valueOf(CouncilTotal_RentArrears),
                        2, RoundingMode.HALF_UP));
        summary.put(
                sCouncilTotalCount_RentArrears,
                "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                        BigDecimal.valueOf(CouncilTotalCount_RentArrears),
                        2, RoundingMode.HALF_UP));
        summary.put(
                sCouncilTotalCount_RentArrearsNonZero,
                "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                        BigDecimal.valueOf(CouncilTotalCount_RentArrearsNonZero),
                        2, RoundingMode.HALF_UP));
        summary.put(
                sCouncilTotalCount_RentArrearsZero,
                "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                        BigDecimal.valueOf(CouncilTotalCount_RentArrearsZero),
                        2, RoundingMode.HALF_UP));
        if (CouncilTotalCount_RentArrears != 0.0d) {
            summary.put(
                    sCouncilAverage_RentArrears,
                    "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                            BigDecimal.valueOf(CouncilTotal_RentArrears / (double) CouncilTotalCount_RentArrears),
                            2, RoundingMode.HALF_UP));
        }
        if (CouncilTotalCount_RentArrearsNonZero != 0.0d) {
            summary.put(
                    sCouncilAverage_NonZeroRentArrears,
                    "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                            BigDecimal.valueOf(CouncilTotal_RentArrears / CouncilTotalCount_RentArrearsNonZero),
                            2, RoundingMode.HALF_UP));
        }
    }

    fdsfadsfsdag ;

    // Need to work out what to do hwith this adding mallarkery

    @Override
    protected void addToSummarySingleTime(
            int nTT,
            int nEG,
            int nPSI,
            HashMap<String, String> summary) {
        //super.addToSummarySingleTime(nTT, nEG, nPSI, summary);
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
        // Add Counts
        addToSummarySingleTimeCounts0(summary);
        addToSummarySingleTimeDisabilityCounts(nTT, summary);
        addToSummarySingleTimeEthnicityCounts(nEG, summary);
        addToSummarySingleTimeTTCounts(nTT, summary);
        addToSummarySingleTimePSICounts(nTT, nPSI, summary);
        addToSummarySingleTimeCounts1(summary);

        // Add Rates
        addToSummarySingleTimeRates0(summary);
        addToSummarySingleTimeDisabilityRates(nTT, summary);
        addToSummarySingleTimeEthnicityRates(nEG, summary);
        addToSummarySingleTimeTTRates(nTT, summary);
        addToSummarySingleTimePSIRates(nTT, nPSI, summary);
        addToSummarySingleTimeRates1(summary);

    }

    @Override
    protected void addToSummarySingleTimeCounts0(
            HashMap<String, String> summary) {
        //super.addToSummarySingleTimeCounts0(summary);
        // Council
        CouncilAllCount1 = CouncilHBCount1 + CouncilCTBCount1;
        summary.put(sCouncilAllCount1, Integer.toString(CouncilAllCount1));
        summary.put(sCouncilHBCount1, Integer.toString(CouncilHBCount1));
        summary.put(sCouncilCTBCount1, Integer.toString(CouncilCTBCount1));
        summary.put(
                sCouncilAllTotalHouseholdSize,
                Long.toString(CouncilAllTotalHouseholdSize));
        // RSL
        RSLAllCount1 = RSLHBCount1 + RSLCTBCount1;
        summary.put(sRSLAllCount1, Integer.toString(RSLAllCount1));
        summary.put(sRSLHBCount1, Integer.toString(RSLHBCount1));
        summary.put(sRSLCTBCount1, Integer.toString(RSLCTBCount1));
        summary.put(
                sRSLAllTotalHouseholdSize,
                Long.toString(RSLAllTotalHouseholdSize));
    }

    @Override
    protected void addToSummarySingleTimeRates0(
            HashMap<String, String> summary) {
        //super.addToSummarySingleTimeRates0(summary);
        double ave;
        // Council
        // All HouseholdSize
        if (CouncilAllCount1 > 0) {
            ave = CouncilAllTotalHouseholdSize / (double) CouncilAllCount1;
            summary.put(
                    sCouncilAllAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilAllAverageHouseholdSize,
                    s0);
        }
        // HB HouseholdSize
        summary.put(
                sCouncilHBTotalHouseholdSize,
                Long.toString(CouncilHBTotalHouseholdSize));
        if (CouncilHBCount1 > 0) {
            ave = CouncilHBTotalHouseholdSize / (double) CouncilHBCount1;
            summary.put(
                    sCouncilHBAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilHBAverageHouseholdSize,
                    s0);
        }
        // CTB HouseholdSize
        summary.put(
                sCouncilCTBTotalHouseholdSize,
                Long.toString(CouncilCTBTotalHouseholdSize));
        if (CouncilCTBCount1 > 0) {
            ave = CouncilCTBTotalHouseholdSize / (double) CouncilCTBCount1;
            summary.put(
                    sCouncilCTBAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilCTBAverageHouseholdSize,
                    s0);
        }
        // RSL
        // All HouseholdSize
        if (RSLAllCount1 > 0) {
            ave = RSLAllTotalHouseholdSize / (double) RSLAllCount1;
            summary.put(
                    sRSLAllAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLAllAverageHouseholdSize,
                    s0);
        }
        // HB HouseholdSize
        summary.put(
                sRSLHBTotalHouseholdSize,
                Long.toString(RSLHBTotalHouseholdSize));
        if (RSLHBCount1 > 0) {
            ave = RSLHBTotalHouseholdSize / (double) RSLHBCount1;
            summary.put(
                    sRSLHBAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLHBAverageHouseholdSize,
                    s0);
        }
        // CTB HouseholdSize
        summary.put(
                sRSLCTBTotalHouseholdSize,
                Long.toString(RSLCTBTotalHouseholdSize));
        if (RSLCTBCount1 > 0) {
            ave = RSLCTBTotalHouseholdSize / (double) RSLCTBCount1;
            summary.put(
                    sRSLCTBAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLCTBAverageHouseholdSize,
                    s0);
        }
    }

    @Override
    protected void addToSummarySingleTimePSICounts(
            int nTT,
            int nPSI,
            HashMap<String, String> summary) {
        //super.addToSummarySingleTimePSICounts(nTT, nPSI, summary);
        // Council
        for (int i = 1; i < nPSI; i++) {
            summary.put(
                    sCouncilAllTotalCount_PSI[i],
                    Long.toString(CouncilAllTotalCount_PSI[i]));
            summary.put(
                    sCouncilHBTotalCount_PSI[i],
                    Long.toString(CouncilHBTotalCount_PSI[i]));
            summary.put(
                    sCouncilCTBTotalCount_PSI[i],
                    Long.toString(CouncilCTBTotalCount_PSI[i]));
            for (int j = 1; j < nTT; j++) {
                summary.put(
                        sCouncilTotalCount_PSIByTT[i][j],
                        Long.toString(CouncilAllTotalCount_PSIByTT[i][j]));
            }
        }
        // RSL
        for (int i = 1; i < nPSI; i++) {
            summary.put(
                    sRSLAllTotalCount_PSI[i],
                    Long.toString(RSLAllTotalCount_PSI[i]));
            summary.put(
                    sRSLHBTotalCount_PSI[i],
                    Long.toString(RSLHBTotalCount_PSI[i]));
            summary.put(
                    sRSLCTBTotalCount_PSI[i],
                    Long.toString(RSLCTBTotalCount_PSI[i]));
            for (int j = 1; j < nTT; j++) {
                summary.put(
                        sRSLTotalCount_PSIByTT[i][j],
                        Long.toString(RSLAllTotalCount_PSIByTT[i][j]));
            }
        }
    }

    @Override
    protected void addToSummarySingleTimePSIRates(
            int nTT,
            int nPSI,
            HashMap<String, String> summary) {
        //super.addToSummarySingleTimePSIRates(nTT, nPSI, summary);
        double ave;
        double d;
        double all;
        // Council
        for (int i = 1; i < nPSI; i++) {
            all = Integer.valueOf(summary.get(sCouncilAllTotalCount_PSI[i]));
            d = CouncilAllCount1;
            if (d > 0) {
                ave = (all * 100.0d) / d;
                summary.put(
                        sCouncilAllPercentageOfAll_PSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sCouncilHBTotalCount_PSI[i]));
            d = CouncilHBCount1;
            if (d > 0) {
                ave = (all * 100.0d) / d;
                summary.put(
                        sCouncilHBPercentageOfHB_PSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sCouncilCTBTotalCount_PSI[i]));
            d = CouncilCTBCount1;
            if (d > 0) {
                ave = (all * 100.0d) / d;
                summary.put(
                        sCouncilCTBPercentageOfCTB_PSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            for (int j = 1; j < nTT; j++) {
                all = Integer.valueOf(summary.get(sCouncilTotalCount_PSIByTT[i][j]));
                d = CouncilAllCount1;
                if (d > 0) {
                    ave = (all * 100.0d) / d;
                    summary.put(
                            sCouncilPercentageOfAll_PSIByTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
                d = CouncilTotalCount_TTClaimant1[j];
                if (d > 0) {
                    ave = (all * 100.0d) / d;
                    summary.put(
                            sCouncilPercentageOfTT_PSIByTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
                d = CouncilHBCount1;
                if (d > 0) {
                    ave = (all * 100.0d) / d;
                    summary.put(
                            sCouncilPercentageOfHB_PSIByTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
                d = CouncilCTBCount1;
                if (d > 0) {
                    ave = (all * 100.0d) / d;
                    summary.put(
                            sCouncilPercentageOfCTB_PSIByTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
            }
        }
        // RSL
        for (int i = 1; i < nPSI; i++) {
            all = Integer.valueOf(summary.get(sRSLAllTotalCount_PSI[i]));
            d = RSLAllCount1;
            if (d > 0) {
                ave = (all * 100.0d) / d;
                summary.put(
                        sRSLAllPercentageOfAll_PSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sRSLHBTotalCount_PSI[i]));
            d = RSLHBCount1;
            if (d > 0) {
                ave = (all * 100.0d) / d;
                summary.put(
                        sRSLHBPercentageOfHB_PSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sRSLCTBTotalCount_PSI[i]));
            d = RSLCTBCount1;
            if (d > 0) {
                ave = (all * 100.0d) / d;
                summary.put(
                        sRSLCTBPercentageOfCTB_PSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            for (int j = 1; j < nTT; j++) {
                all = Integer.valueOf(summary.get(sRSLTotalCount_PSIByTT[i][j]));
                d = RSLAllCount1;
                if (d > 0) {
                    ave = (all * 100.0d) / d;
                    summary.put(
                            sRSLPercentageOfAll_PSIByTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
                d = RSLTotalCount_TTClaimant1[j];
                if (d > 0) {
                    ave = (all * 100.0d) / d;
                    summary.put(
                            sRSLPercentageOfTT_PSIByTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
                d = RSLHBCount1;
                if (d > 0) {
                    ave = (all * 100.0d) / d;
                    summary.put(
                            sRSLPercentageOfHB_PSIByTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
                d = RSLCTBCount1;
                if (d > 0) {
                    ave = (all * 100.0d) / d;
                    summary.put(
                            sRSLPercentageOfCTB_PSIByTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
            }
        }
    }

    @Override
    protected void addToSummarySingleTimeDisabilityCounts(
            int nTT,
            HashMap<String, String> summary) {
        //super.addToSummarySingleTimeDisabilityCounts(nTT, summary);
        int t;
        // Council
        // DisabilityAward
        t = CouncilTotalCount_DisabilityAwardByTT[1]
                + CouncilTotalCount_DisabilityAwardByTT[2]
                + CouncilTotalCount_DisabilityAwardByTT[3]
                + CouncilTotalCount_DisabilityAwardByTT[4]
                + CouncilTotalCount_DisabilityAwardByTT[5]
                + CouncilTotalCount_DisabilityAwardByTT[6]
                + CouncilTotalCount_DisabilityAwardByTT[7]
                + CouncilTotalCount_DisabilityAwardByTT[8]
                + CouncilTotalCount_DisabilityAwardByTT[9];
        summary.put(
                sCouncilTotalCount_DisabilityAward,
                Integer.toString(t));
        // DisabilityAwardHBTTs
        t = CouncilTotalCount_DisabilityAwardByTT[1]
                + CouncilTotalCount_DisabilityAwardByTT[2]
                + CouncilTotalCount_DisabilityAwardByTT[3]
                + CouncilTotalCount_DisabilityAwardByTT[4]
                + CouncilTotalCount_DisabilityAwardByTT[6]
                + CouncilTotalCount_DisabilityAwardByTT[8]
                + CouncilTotalCount_DisabilityAwardByTT[9];
        summary.put(
                sCouncilTotalCount_DisabilityAwardHBTTs,
                Integer.toString(t));
        // DisabilityAwardCTBTTs
        t = CouncilTotalCount_DisabilityAwardByTT[5]
                + CouncilTotalCount_DisabilityAwardByTT[7];
        summary.put(
                sCouncilTotalCount_DisabilityAwardCTBTTs,
                Integer.toString(t));
        // DisabilityAwardSocialTTs
        t = CouncilTotalCount_DisabilityAwardByTT[1] + CouncilTotalCount_DisabilityAwardByTT[4];
        summary.put(
                sCouncilTotalCount_DisabilityAwardSocialTTs,
                Integer.toString(t));
        // DisabilityAwardPrivateDeregulatedTTs
        t = CouncilTotalCount_DisabilityAwardByTT[3] + CouncilTotalCount_DisabilityAwardByTT[6];
        summary.put(
                sCouncilTotalCount_DisabilityAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        // DisabilityPremiumAward
        t = CouncilTotalCount_DisabilityPremiumAwardByTT[1]
                + CouncilTotalCount_DisabilityPremiumAwardByTT[2]
                + CouncilTotalCount_DisabilityPremiumAwardByTT[3]
                + CouncilTotalCount_DisabilityPremiumAwardByTT[4]
                + CouncilTotalCount_DisabilityPremiumAwardByTT[5]
                + CouncilTotalCount_DisabilityPremiumAwardByTT[6]
                + CouncilTotalCount_DisabilityPremiumAwardByTT[7]
                + CouncilTotalCount_DisabilityPremiumAwardByTT[8]
                + CouncilTotalCount_DisabilityPremiumAwardByTT[9];
        summary.put(
                sCouncilTotalCount_DisabilityPremiumAward,
                Integer.toString(t));
        // DisabilityPremiumAwardHBTTs
        t = CouncilTotalCount_DisabilityPremiumAwardByTT[1]
                + CouncilTotalCount_DisabilityPremiumAwardByTT[2]
                + CouncilTotalCount_DisabilityPremiumAwardByTT[3]
                + CouncilTotalCount_DisabilityPremiumAwardByTT[4]
                + CouncilTotalCount_DisabilityPremiumAwardByTT[6]
                + CouncilTotalCount_DisabilityPremiumAwardByTT[8]
                + CouncilTotalCount_DisabilityPremiumAwardByTT[9];
        summary.put(
                sCouncilTotalCount_DisabilityPremiumAwardHBTTs,
                Integer.toString(t));
        // DisabilityPremiumAwardCTBTTs
        t = CouncilTotalCount_DisabilityPremiumAwardByTT[5]
                + CouncilTotalCount_DisabilityPremiumAwardByTT[7];
        summary.put(
                sCouncilTotalCount_DisabilityPremiumAwardCTBTTs,
                Integer.toString(t));
        // DisabilityPremiumAwardSocialTTs
        t = CouncilTotalCount_DisabilityPremiumAwardByTT[1] + CouncilTotalCount_DisabilityPremiumAwardByTT[4];
        summary.put(
                sCouncilTotalCount_DisabilityPremiumAwardSocialTTs,
                Integer.toString(t));
        // DisabilityPremiumAwardPrivateDeregulatedTTs
        t = CouncilTotalCount_DisabilityPremiumAwardByTT[3] + CouncilTotalCount_DisabilityPremiumAwardByTT[6];
        summary.put(
                sCouncilTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        // SevereDisabilityPremiumAward
        t = CouncilTotalCount_SevereDisabilityPremiumAwardByTT[1]
                + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[2]
                + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[3]
                + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[4]
                + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[5]
                + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[6]
                + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[7]
                + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[8]
                + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[9];
        summary.put(
                sCouncilTotalCount_SevereDisabilityPremiumAward,
                Integer.toString(t));
        // SevereSevereDisabilityPremiumAwardHBTTs
        t = CouncilTotalCount_SevereDisabilityPremiumAwardByTT[1]
                + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[2]
                + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[3]
                + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[4]
                + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[6]
                + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[8]
                + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[9];
        summary.put(
                sCouncilTotalCount_SevereDisabilityPremiumAwardHBTTs,
                Integer.toString(t));
        // SevereDisabilityPremiumAwardCTBTTs
        t = CouncilTotalCount_SevereDisabilityPremiumAwardByTT[5]
                + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[7];
        summary.put(
                sCouncilTotalCount_SevereDisabilityPremiumAwardCTBTTs,
                Integer.toString(t));
        // SevereDisabilityPremiumAwardSocialTTs
        t = CouncilTotalCount_SevereDisabilityPremiumAwardByTT[1] + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[4];
        summary.put(
                sCouncilTotalCount_SevereDisabilityPremiumAwardSocialTTs,
                Integer.toString(t));
        // SevereDisabilityPremiumAwardPrivateDeregulatedTTs
        t = CouncilTotalCount_SevereDisabilityPremiumAwardByTT[3] + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[6];
        summary.put(
                sCouncilTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        // DisabledChildPremiumAward
        t = CouncilTotalCount_DisabledChildPremiumAwardByTT[1]
                + CouncilTotalCount_DisabledChildPremiumAwardByTT[2]
                + CouncilTotalCount_DisabledChildPremiumAwardByTT[3]
                + CouncilTotalCount_DisabledChildPremiumAwardByTT[4]
                + CouncilTotalCount_DisabledChildPremiumAwardByTT[5]
                + CouncilTotalCount_DisabledChildPremiumAwardByTT[6]
                + CouncilTotalCount_DisabledChildPremiumAwardByTT[7]
                + CouncilTotalCount_DisabledChildPremiumAwardByTT[8]
                + CouncilTotalCount_DisabledChildPremiumAwardByTT[9];
        summary.put(
                sCouncilTotalCount_DisabledChildPremiumAward,
                Integer.toString(t));
        // SevereDisabledChildPremiumAwardHBTTs
        t = CouncilTotalCount_DisabledChildPremiumAwardByTT[1]
                + CouncilTotalCount_DisabledChildPremiumAwardByTT[2]
                + CouncilTotalCount_DisabledChildPremiumAwardByTT[3]
                + CouncilTotalCount_DisabledChildPremiumAwardByTT[4]
                + CouncilTotalCount_DisabledChildPremiumAwardByTT[6]
                + CouncilTotalCount_DisabledChildPremiumAwardByTT[8]
                + CouncilTotalCount_DisabledChildPremiumAwardByTT[9];
        summary.put(
                sCouncilTotalCount_DisabledChildPremiumAwardHBTTs,
                Integer.toString(t));
        // DisabledChildPremiumAwardCTBTTs
        t = CouncilTotalCount_DisabledChildPremiumAwardByTT[5]
                + CouncilTotalCount_DisabledChildPremiumAwardByTT[7];
        summary.put(
                sCouncilTotalCount_DisabledChildPremiumAwardCTBTTs,
                Integer.toString(t));
        // DisabledChildPremiumAwardSocialTTs
        t = CouncilTotalCount_DisabledChildPremiumAwardByTT[1] + CouncilTotalCount_DisabledChildPremiumAwardByTT[4];
        summary.put(
                sCouncilTotalCount_DisabledChildPremiumAwardSocialTTs,
                Integer.toString(t));
        // DisabledChildPremiumAwardPrivateDeregulatedTTs
        t = CouncilTotalCount_DisabledChildPremiumAwardByTT[3] + CouncilTotalCount_DisabledChildPremiumAwardByTT[6];
        summary.put(
                sCouncilTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAward
        t = CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[1]
                + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[2]
                + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[3]
                + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[4]
                + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[5]
                + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[6]
                + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[7]
                + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[8]
                + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[9];
        summary.put(
                sCouncilTotalCount_EnhancedDisabilityPremiumAward,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAwardHBTTs
        t = CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[1]
                + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[2]
                + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[3]
                + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[4]
                + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[6]
                + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[8]
                + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[9];
        summary.put(
                sCouncilTotalCount_EnhancedDisabilityPremiumAwardHBTTs,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAwardCTBTTs
        t = CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[5]
                + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[7];
        summary.put(
                sCouncilTotalCount_EnhancedDisabilityPremiumAwardCTBTTs,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAwardSocialTTs
        t = CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[1] + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[4];
        summary.put(
                sCouncilTotalCount_EnhancedDisabilityPremiumAwardSocialTTs,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs
        t = CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[3] + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[6];
        summary.put(
                sCouncilTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        // ByTT
        for (int i = 1; i < nTT; i++) {
            summary.put(
                    sCouncilTotalCount_DisabilityAwardByTT[i],
                    Integer.toString(TotalCount_DisabilityAwardByTT[i]));
            summary.put(
                    sCouncilTotalCount_DisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_DisabilityPremiumAwardByTT[i]));
            summary.put(
                    sCouncilTotalCount_SevereDisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_SevereDisabilityPremiumAwardByTT[i]));
            summary.put(
                    sCouncilTotalCount_DisabledChildPremiumAwardByTT[i],
                    Integer.toString(TotalCount_DisabledChildPremiumAwardByTT[i]));
            summary.put(
                    sCouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
        }
        // RSL
        // DisabilityAward
        t = RSLTotalCount_DisabilityAwardByTT[1]
                + RSLTotalCount_DisabilityAwardByTT[2]
                + RSLTotalCount_DisabilityAwardByTT[3]
                + RSLTotalCount_DisabilityAwardByTT[4]
                + RSLTotalCount_DisabilityAwardByTT[5]
                + RSLTotalCount_DisabilityAwardByTT[6]
                + RSLTotalCount_DisabilityAwardByTT[7]
                + RSLTotalCount_DisabilityAwardByTT[8]
                + RSLTotalCount_DisabilityAwardByTT[9];
        summary.put(
                sRSLTotalCount_DisabilityAward,
                Integer.toString(t));
        // DisabilityAwardHBTTs
        t = RSLTotalCount_DisabilityAwardByTT[1]
                + RSLTotalCount_DisabilityAwardByTT[2]
                + RSLTotalCount_DisabilityAwardByTT[3]
                + RSLTotalCount_DisabilityAwardByTT[4]
                + RSLTotalCount_DisabilityAwardByTT[6]
                + RSLTotalCount_DisabilityAwardByTT[8]
                + RSLTotalCount_DisabilityAwardByTT[9];
        summary.put(
                sRSLTotalCount_DisabilityAwardHBTTs,
                Integer.toString(t));
        // DisabilityAwardCTBTTs
        t = RSLTotalCount_DisabilityAwardByTT[5]
                + RSLTotalCount_DisabilityAwardByTT[7];
        summary.put(
                sRSLTotalCount_DisabilityAwardCTBTTs,
                Integer.toString(t));
        // DisabilityAwardSocialTTs
        t = RSLTotalCount_DisabilityAwardByTT[1] + RSLTotalCount_DisabilityAwardByTT[4];
        summary.put(
                sRSLTotalCount_DisabilityAwardSocialTTs,
                Integer.toString(t));
        // DisabilityAwardPrivateDeregulatedTTs
        t = RSLTotalCount_DisabilityAwardByTT[3] + RSLTotalCount_DisabilityAwardByTT[6];
        summary.put(
                sRSLTotalCount_DisabilityAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        // DisabilityPremiumAward
        t = RSLTotalCount_DisabilityPremiumAwardByTT[1]
                + RSLTotalCount_DisabilityPremiumAwardByTT[2]
                + RSLTotalCount_DisabilityPremiumAwardByTT[3]
                + RSLTotalCount_DisabilityPremiumAwardByTT[4]
                + RSLTotalCount_DisabilityPremiumAwardByTT[5]
                + RSLTotalCount_DisabilityPremiumAwardByTT[6]
                + RSLTotalCount_DisabilityPremiumAwardByTT[7]
                + RSLTotalCount_DisabilityPremiumAwardByTT[8]
                + RSLTotalCount_DisabilityPremiumAwardByTT[9];
        summary.put(
                sRSLTotalCount_DisabilityPremiumAward,
                Integer.toString(t));
        // DisabilityPremiumAwardHBTTs
        t = RSLTotalCount_DisabilityPremiumAwardByTT[1]
                + RSLTotalCount_DisabilityPremiumAwardByTT[2]
                + RSLTotalCount_DisabilityPremiumAwardByTT[3]
                + RSLTotalCount_DisabilityPremiumAwardByTT[4]
                + RSLTotalCount_DisabilityPremiumAwardByTT[6]
                + RSLTotalCount_DisabilityPremiumAwardByTT[8]
                + RSLTotalCount_DisabilityPremiumAwardByTT[9];
        summary.put(
                sRSLTotalCount_DisabilityPremiumAwardHBTTs,
                Integer.toString(t));
        // DisabilityPremiumAwardCTBTTs
        t = RSLTotalCount_DisabilityPremiumAwardByTT[5]
                + RSLTotalCount_DisabilityPremiumAwardByTT[7];
        summary.put(
                sRSLTotalCount_DisabilityPremiumAwardCTBTTs,
                Integer.toString(t));
        // DisabilityPremiumAwardSocialTTs
        t = RSLTotalCount_DisabilityPremiumAwardByTT[1] + RSLTotalCount_DisabilityPremiumAwardByTT[4];
        summary.put(
                sRSLTotalCount_DisabilityPremiumAwardSocialTTs,
                Integer.toString(t));
        // DisabilityPremiumAwardPrivateDeregulatedTTs
        t = RSLTotalCount_DisabilityPremiumAwardByTT[3] + RSLTotalCount_DisabilityPremiumAwardByTT[6];
        summary.put(
                sRSLTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        // SevereDisabilityPremiumAward
        t = RSLTotalCount_SevereDisabilityPremiumAwardByTT[1]
                + RSLTotalCount_SevereDisabilityPremiumAwardByTT[2]
                + RSLTotalCount_SevereDisabilityPremiumAwardByTT[3]
                + RSLTotalCount_SevereDisabilityPremiumAwardByTT[4]
                + RSLTotalCount_SevereDisabilityPremiumAwardByTT[5]
                + RSLTotalCount_SevereDisabilityPremiumAwardByTT[6]
                + RSLTotalCount_SevereDisabilityPremiumAwardByTT[7]
                + RSLTotalCount_SevereDisabilityPremiumAwardByTT[8]
                + RSLTotalCount_SevereDisabilityPremiumAwardByTT[9];
        summary.put(
                sRSLTotalCount_SevereDisabilityPremiumAward,
                Integer.toString(t));
        // SevereSevereDisabilityPremiumAwardHBTTs
        t = RSLTotalCount_SevereDisabilityPremiumAwardByTT[1]
                + RSLTotalCount_SevereDisabilityPremiumAwardByTT[2]
                + RSLTotalCount_SevereDisabilityPremiumAwardByTT[3]
                + RSLTotalCount_SevereDisabilityPremiumAwardByTT[4]
                + RSLTotalCount_SevereDisabilityPremiumAwardByTT[6]
                + RSLTotalCount_SevereDisabilityPremiumAwardByTT[8]
                + RSLTotalCount_SevereDisabilityPremiumAwardByTT[9];
        summary.put(
                sRSLTotalCount_SevereDisabilityPremiumAwardHBTTs,
                Integer.toString(t));
        // SevereDisabilityPremiumAwardCTBTTs
        t = RSLTotalCount_SevereDisabilityPremiumAwardByTT[5]
                + RSLTotalCount_SevereDisabilityPremiumAwardByTT[7];
        summary.put(
                sRSLTotalCount_SevereDisabilityPremiumAwardCTBTTs,
                Integer.toString(t));
        // SevereDisabilityPremiumAwardSocialTTs
        t = RSLTotalCount_SevereDisabilityPremiumAwardByTT[1] + RSLTotalCount_SevereDisabilityPremiumAwardByTT[4];
        summary.put(
                sRSLTotalCount_SevereDisabilityPremiumAwardSocialTTs,
                Integer.toString(t));
        // SevereDisabilityPremiumAwardPrivateDeregulatedTTs
        t = RSLTotalCount_SevereDisabilityPremiumAwardByTT[3] + RSLTotalCount_SevereDisabilityPremiumAwardByTT[6];
        summary.put(
                sRSLTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        // DisabledChildPremiumAward
        t = RSLTotalCount_DisabledChildPremiumAwardByTT[1]
                + RSLTotalCount_DisabledChildPremiumAwardByTT[2]
                + RSLTotalCount_DisabledChildPremiumAwardByTT[3]
                + RSLTotalCount_DisabledChildPremiumAwardByTT[4]
                + RSLTotalCount_DisabledChildPremiumAwardByTT[5]
                + RSLTotalCount_DisabledChildPremiumAwardByTT[6]
                + RSLTotalCount_DisabledChildPremiumAwardByTT[7]
                + RSLTotalCount_DisabledChildPremiumAwardByTT[8]
                + RSLTotalCount_DisabledChildPremiumAwardByTT[9];
        summary.put(
                sRSLTotalCount_DisabledChildPremiumAward,
                Integer.toString(t));
        // SevereDisabledChildPremiumAwardHBTTs
        t = RSLTotalCount_DisabledChildPremiumAwardByTT[1]
                + RSLTotalCount_DisabledChildPremiumAwardByTT[2]
                + RSLTotalCount_DisabledChildPremiumAwardByTT[3]
                + RSLTotalCount_DisabledChildPremiumAwardByTT[4]
                + RSLTotalCount_DisabledChildPremiumAwardByTT[6]
                + RSLTotalCount_DisabledChildPremiumAwardByTT[8]
                + RSLTotalCount_DisabledChildPremiumAwardByTT[9];
        summary.put(
                sRSLTotalCount_DisabledChildPremiumAwardHBTTs,
                Integer.toString(t));
        // DisabledChildPremiumAwardCTBTTs
        t = RSLTotalCount_DisabledChildPremiumAwardByTT[5]
                + RSLTotalCount_DisabledChildPremiumAwardByTT[7];
        summary.put(
                sRSLTotalCount_DisabledChildPremiumAwardCTBTTs,
                Integer.toString(t));
        // DisabledChildPremiumAwardSocialTTs
        t = RSLTotalCount_DisabledChildPremiumAwardByTT[1] + RSLTotalCount_DisabledChildPremiumAwardByTT[4];
        summary.put(
                sRSLTotalCount_DisabledChildPremiumAwardSocialTTs,
                Integer.toString(t));
        // DisabledChildPremiumAwardPrivateDeregulatedTTs
        t = RSLTotalCount_DisabledChildPremiumAwardByTT[3] + RSLTotalCount_DisabledChildPremiumAwardByTT[6];
        summary.put(
                sRSLTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAward
        t = RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[1]
                + RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[2]
                + RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[3]
                + RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[4]
                + RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[5]
                + RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[6]
                + RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[7]
                + RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[8]
                + RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[9];
        summary.put(
                sRSLTotalCount_EnhancedDisabilityPremiumAward,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAwardHBTTs
        t = RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[1]
                + RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[2]
                + RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[3]
                + RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[4]
                + RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[6]
                + RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[8]
                + RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[9];
        summary.put(
                sRSLTotalCount_EnhancedDisabilityPremiumAwardHBTTs,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAwardCTBTTs
        t = RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[5]
                + RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[7];
        summary.put(
                sRSLTotalCount_EnhancedDisabilityPremiumAwardCTBTTs,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAwardSocialTTs
        t = RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[1] + RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[4];
        summary.put(
                sRSLTotalCount_EnhancedDisabilityPremiumAwardSocialTTs,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs
        t = RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[3] + RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[6];
        summary.put(
                sRSLTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        // ByTT
        for (int i = 1; i < nTT; i++) {
            summary.put(
                    sRSLTotalCount_DisabilityAwardByTT[i],
                    Integer.toString(TotalCount_DisabilityAwardByTT[i]));
            summary.put(
                    sRSLTotalCount_DisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_DisabilityPremiumAwardByTT[i]));
            summary.put(
                    sRSLTotalCount_SevereDisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_SevereDisabilityPremiumAwardByTT[i]));
            summary.put(
                    sRSLTotalCount_DisabledChildPremiumAwardByTT[i],
                    Integer.toString(TotalCount_DisabledChildPremiumAwardByTT[i]));
            summary.put(
                    sRSLTotalCount_EnhancedDisabilityPremiumAwardByTT[i],
                    Integer.toString(TotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
        }
    }

    @Override
    protected void addToSummarySingleTimeDisabilityRates(
            int nTT,
            HashMap<String, String> summary) {
        //super.addToSummarySingleTimeDisabilityRates(nTT, summary);
        double percentage;
        double d;
        int t;
        // Council
        // DisabilityAward
        t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityAward));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_DisabilityAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityAwardHBTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityAwardHBTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_DisabilityAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabilityAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityAwardCTBTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityAwardCTBTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_DisabilityAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilCTBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfCTB_DisabilityAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityAwardSocialTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityAwardSocialTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_DisabilityAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabilityAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilTotalCount_TTClaimant0[1] + CouncilTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfSocialTTs_DisabilityAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityAwardPrivateDeregulatedTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityAwardPrivateDeregulatedTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilTotalCount_TTClaimant0[3] + CouncilTotalCount_TTClaimant0[6];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAward
        t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityPremiumAward));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_DisabilityPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAwardHBTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityPremiumAwardHBTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_DisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAwardCTBTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityPremiumAwardCTBTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_DisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilCTBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfCTB_DisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAwardSocialTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityPremiumAwardSocialTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_DisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilTotalCount_TTClaimant0[1] + CouncilTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAwardPrivateDeregulatedTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilTotalCount_TTClaimant0[3] + CouncilTotalCount_TTClaimant0[6];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabilityPremiumAward
        t = Integer.valueOf(summary.get(sCouncilTotalCount_SevereDisabilityPremiumAward));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_SevereDisabilityPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereSevereDisabilityPremiumAwardHBTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardHBTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_SevereDisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_SevereDisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabilityPremiumAwardCTBTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardCTBTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilCTBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabilityPremiumAwardSocialTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardSocialTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilTotalCount_TTClaimant0[1] + CouncilTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabilityPremiumAwardPrivateDeregulatedTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilTotalCount_TTClaimant0[3] + CouncilTotalCount_TTClaimant0[6];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAward
        t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabledChildPremiumAward));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_DisabledChildPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabledChildPremiumAwardHBTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabledChildPremiumAwardHBTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_DisabledChildPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabledChildPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAwardCTBTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabledChildPremiumAwardCTBTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_DisabledChildPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilCTBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfCTB_DisabledChildPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAwardSocialTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabledChildPremiumAwardSocialTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_DisabledChildPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabledChildPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilTotalCount_TTClaimant0[1] + CouncilTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAwardPrivateDeregulatedTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilTotalCount_TTClaimant0[3] + CouncilTotalCount_TTClaimant0[6];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAward
        t = Integer.valueOf(summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAward));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_EnhancedDisabilityPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAwardHBTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardHBTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAwardCTBTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardCTBTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilCTBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAwardSocialTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardSocialTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilTotalCount_TTClaimant0[1] + CouncilTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs
        t = Integer.valueOf(summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs));
        d = CouncilAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilTotalCount_TTClaimant0[1] + CouncilTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // ByTT
        for (int i = 1; i < nTT; i++) {
            // CouncilAllCount1
            d = CouncilAllCount1;
            t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfAll_DisabilityAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfAll_DisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfAll_SevereDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabledChildPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfAll_DisabledChildPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            // CouncilHBCount1;
            d = CouncilHBCount1;
            t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfHB_DisabilityAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfHB_DisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfHB_SevereDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabledChildPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfHB_DisabledChildPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            // CouncilCTBCount1
            d = CouncilCTBCount1;
            t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfCTB_DisabilityAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfCTB_DisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_SevereDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabledChildPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_DisabledChildPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfCTB_DisabledChildPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_EnhancedDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            // TT
            d = CouncilTotalCount_TTClaimant1[i];
            t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfTT_DisabilityAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfTT_DisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_SevereDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfTT_SevereDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sCouncilTotalCount_DisabledChildPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_DisabledChildPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfTT_DisabledChildPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_EnhancedDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfTT_EnhancedDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
        }
        // RSL
        // DisabilityAward
        t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityAward));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_DisabilityAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityAwardHBTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityAwardHBTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_DisabilityAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabilityAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityAwardCTBTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityAwardCTBTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_DisabilityAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLCTBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfCTB_DisabilityAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityAwardSocialTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityAwardSocialTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_DisabilityAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabilityAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLTotalCount_TTClaimant0[1] + RSLTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfSocialTTs_DisabilityAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityAwardPrivateDeregulatedTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityAwardPrivateDeregulatedTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLTotalCount_TTClaimant0[3] + RSLTotalCount_TTClaimant0[6];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAward
        t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityPremiumAward));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_DisabilityPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAwardHBTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityPremiumAwardHBTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_DisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAwardCTBTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityPremiumAwardCTBTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_DisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLCTBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfCTB_DisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAwardSocialTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityPremiumAwardSocialTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_DisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLTotalCount_TTClaimant0[1] + RSLTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAwardPrivateDeregulatedTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLTotalCount_TTClaimant0[3] + RSLTotalCount_TTClaimant0[6];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabilityPremiumAward
        t = Integer.valueOf(summary.get(sRSLTotalCount_SevereDisabilityPremiumAward));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_SevereDisabilityPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereSevereDisabilityPremiumAwardHBTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_SevereDisabilityPremiumAwardHBTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_SevereDisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_SevereDisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabilityPremiumAwardCTBTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_SevereDisabilityPremiumAwardCTBTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLCTBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabilityPremiumAwardSocialTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_SevereDisabilityPremiumAwardSocialTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLTotalCount_TTClaimant0[1] + RSLTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabilityPremiumAwardPrivateDeregulatedTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLTotalCount_TTClaimant0[3] + RSLTotalCount_TTClaimant0[6];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAward
        t = Integer.valueOf(summary.get(sRSLTotalCount_DisabledChildPremiumAward));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_DisabledChildPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabledChildPremiumAwardHBTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_DisabledChildPremiumAwardHBTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_DisabledChildPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabledChildPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAwardCTBTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_DisabledChildPremiumAwardCTBTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_DisabledChildPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLCTBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfCTB_DisabledChildPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAwardSocialTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_DisabledChildPremiumAwardSocialTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_DisabledChildPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabledChildPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLTotalCount_TTClaimant0[1] + RSLTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAwardPrivateDeregulatedTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLTotalCount_TTClaimant0[3] + RSLTotalCount_TTClaimant0[6];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAward
        t = Integer.valueOf(summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAward));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_EnhancedDisabilityPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAwardHBTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardHBTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAwardCTBTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardCTBTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLCTBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAwardSocialTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardSocialTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLTotalCount_TTClaimant0[1] + RSLTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs
        t = Integer.valueOf(summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs));
        d = RSLAllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLHBCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLTotalCount_TTClaimant0[1] + RSLTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // ByTT
        for (int i = 1; i < nTT; i++) {
            // RSLAllCount1
            d = RSLAllCount1;
            t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfAll_DisabilityAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfAll_DisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sRSLTotalCount_SevereDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfAll_SevereDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sRSLTotalCount_DisabledChildPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfAll_DisabledChildPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            // RSLHBCount1;
            d = RSLHBCount1;
            t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfHB_DisabilityAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfHB_DisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sRSLTotalCount_SevereDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfHB_SevereDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sRSLTotalCount_DisabledChildPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfHB_DisabledChildPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            // RSLCTBCount1
            d = RSLCTBCount1;
            t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfCTB_DisabilityAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfCTB_DisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sRSLTotalCount_SevereDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_SevereDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sRSLTotalCount_DisabledChildPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_DisabledChildPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfCTB_DisabledChildPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_EnhancedDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            // TT
            d = RSLTotalCount_TTClaimant1[i];
            t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfTT_DisabilityAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sRSLTotalCount_DisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfTT_DisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sRSLTotalCount_SevereDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_SevereDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfTT_SevereDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sRSLTotalCount_DisabledChildPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_DisabledChildPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfTT_DisabledChildPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
            if (d > 0) {
                percentage = (TotalCount_EnhancedDisabilityPremiumAwardByTT[i] * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfTT_EnhancedDisabilityPremiumAwardByTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
        }
    }

    @Override
    protected void addToSummarySingleTimeCounts1(
            HashMap<String, String> summary) {
        super.addToSummarySingleTimeCounts1(summary);
        int t;
        // Council
        // WeeklyHBEntitlement
        summary.put(
                sCouncilTotalWeeklyHBEntitlement,
                BigDecimal.valueOf(CouncilAllTotalWeeklyHBEntitlement).toPlainString());
        summary.put(
                sCouncilTotalCount_WeeklyHBEntitlementNonZero,
                Integer.toString(CouncilAllTotalWeeklyHBEntitlementNonZeroCount));
        summary.put(
                sCouncilTotalCount_WeeklyHBEntitlementZero,
                Integer.toString(CouncilAllTotalWeeklyHBEntitlementZeroCount));
        summary.put(
                sCouncilHBTotalWeeklyHBEntitlement,
                BigDecimal.valueOf(CouncilHBTotalWeeklyHBEntitlement).toPlainString());
        summary.put(
                sCouncilHBTotalCount_WeeklyHBEntitlementNonZero,
                Integer.toString(CouncilHBTotalCount_WeeklyHBEntitlementNonZero));
        summary.put(
                sCouncilHBTotalCount_WeeklyHBEntitlementZero,
                Integer.toString(CouncilHBTotalCount_WeeklyHBEntitlementZero));
        summary.put(
                sCouncilCTBTotalWeeklyHBEntitlement,
                BigDecimal.valueOf(CouncilCTBTotalWeeklyHBEntitlement).toPlainString());
        summary.put(
                sCouncilCTBTotalCount_WeeklyHBEntitlementNonZero,
                Integer.toString(CouncilCTBTotalCount_WeeklyHBEntitlementNonZero));
        summary.put(
                sCouncilCTBTotalCount_WeeklyHBEntitlementZero,
                Integer.toString(CouncilCTBTotalWeeklyHBEntitlementZeroCount));
        // WeeklyCTBEntitlement
        summary.put(
                sCouncilTotalWeeklyCTBEntitlement,
                BigDecimal.valueOf(CouncilAllTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(
                sCouncilTotalCount_WeeklyCTBEntitlementNonZero,
                Integer.toString(CouncilAllTotalCount_WeeklyCTBEntitlementNonZero));
        summary.put(
                sCouncilTotalCount_WeeklyCTBEntitlementZero,
                Integer.toString(CouncilAllTotalWeeklyCTBEntitlementZeroCount));
        summary.put(
                sCouncilHBTotalWeeklyCTBEntitlement,
                BigDecimal.valueOf(CouncilHBTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(
                sCouncilHBTotalCount_WeeklyCTBEntitlementNonZero,
                Integer.toString(CouncilHBTotalCount_WeeklyCTBEntitlementNonZero));
        summary.put(
                sCouncilHBTotalCount_WeeklyCTBEntitlementZero,
                Integer.toString(CouncilHBTotalWeeklyCTBEntitlementZeroCount));
        summary.put(
                sCouncilCTBTotalWeeklyCTBEntitlement,
                BigDecimal.valueOf(CouncilCTBTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(
                sCouncilCTBTotalCount_WeeklyCTBEntitlementNonZero,
                Integer.toString(CouncilCTBTotalCount_WeeklyCTBEntitlementNonZero));
        summary.put(
                sCouncilCTBTotalCount_WeeklyCTBEntitlementZero,
                Integer.toString(CouncilCTBTotalWeeklyCTBEntitlementZeroCount));
        // WeeklyEligibleRentAmount
        summary.put(
                sCouncilAllTotalWeeklyEligibleRentAmount,
                BigDecimal.valueOf(CouncilAllTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                sCouncilAllTotalCount_WeeklyEligibleRentAmountNonZero,
                Integer.toString(CouncilAllTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(
                sCouncilAllTotalCount_WeeklyEligibleRentAmountZero,
                Integer.toString(CouncilAllTotalWeeklyEligibleRentAmountZeroCount));
        summary.put(
                sCouncilHBTotalWeeklyEligibleRentAmount,
                BigDecimal.valueOf(CouncilHBTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                sCouncilHBTotalCount_WeeklyEligibleRentAmountNonZero,
                Integer.toString(CouncilHBTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(
                sCouncilHBTotalCount_WeeklyEligibleRentAmountZero,
                Integer.toString(CouncilHBTotalWeeklyEligibleRentAmountZeroCount));
        summary.put(
                sCouncilCTBTotalWeeklyEligibleRentAmount,
                BigDecimal.valueOf(CouncilCTBTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                sCouncilCTBTotalCount_WeeklyEligibleRentAmountNonZero,
                Integer.toString(CouncilCTBTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(
                sCouncilCTBTotalCount_WeeklyEligibleRentAmountZero,
                Integer.toString(CouncilCTBTotalWeeklyEligibleRentAmountZeroCount));
        // WeeklyEligibleCouncilTaxAmount
        summary.put(
                sCouncilAllTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(CouncilAllTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(
                sCouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(CouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(
                sCouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(CouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountZero));
        summary.put(
                sCouncilHBTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(CouncilHBTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(
                sCouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(CouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(
                sCouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(CouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountZero));
        summary.put(
                sCouncilCTBTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(CouncilCTBTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(
                sCouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(CouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(
                sCouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(CouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero));
        // ContractualRentAmount
        summary.put(
                sCouncilAllTotalContractualRentAmount,
                BigDecimal.valueOf(CouncilAllTotalContractualRentAmount).toPlainString());
        summary.put(
                sCouncilAllTotalCountContractualRentAmountNonZeroCount,
                Integer.toString(CouncilAllTotalContractualRentAmountNonZeroCount));
        summary.put(
                sCouncilAllTotalCountContractualRentAmountZeroCount,
                Integer.toString(CouncilAllTotalContractualRentAmountZeroCount));
        summary.put(
                sCouncilHBTotalContractualRentAmount,
                BigDecimal.valueOf(CouncilHBTotalContractualRentAmount).toPlainString());
        summary.put(
                sCouncilHBTotalCountContractualRentAmountNonZeroCount,
                Integer.toString(CouncilHBTotalContractualRentAmountNonZeroCount));
        summary.put(
                sCouncilHBTotalCountContractualRentAmountZeroCount,
                Integer.toString(CouncilHBTotalContractualRentAmountZeroCount));
        summary.put(
                sCouncilCTBTotalContractualRentAmount,
                BigDecimal.valueOf(CouncilCTBTotalContractualRentAmount).toPlainString());
        summary.put(
                sCouncilCTBTotalCountContractualRentAmountNonZeroCount,
                Integer.toString(CouncilCTBTotalContractualRentAmountNonZeroCount));
        summary.put(
                sCouncilCTBTotalCountContractualRentAmountZeroCount,
                Integer.toString(CouncilCTBTotalContractualRentAmountZeroCount));
        // WeeklyAdditionalDiscretionaryPayment
        summary.put(
                sCouncilAllTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(CouncilAllTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sCouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sCouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        summary.put(
                sCouncilHBTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(CouncilHBTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sCouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sCouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        summary.put(
                sCouncilCTBTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(CouncilCTBTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sCouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sCouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        summary.put(
                sCouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sCouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sCouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        summary.put(
                sCouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sCouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sCouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        summary.put(
                sCouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sCouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sCouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        // Employed
        t = CouncilHBTotalCount_EmployedClaimants + CouncilCTBTotalCount_EmployedClaimants;
        summary.put(
                sCouncilAllTotalCount_ClaimantsEmployed,
                Integer.toString(t));
        summary.put(
                sCouncilHBTotalCount_ClaimantsEmployed,
                Integer.toString(CouncilHBTotalCount_EmployedClaimants));
        summary.put(
                sCouncilCTBTotalCount_ClaimantsEmployed,
                Integer.toString(CouncilCTBTotalCount_EmployedClaimants));
        // Self Employed
        summary.put(
                sCouncilHBTotalCountClaimantsSelfEmployed,
                Integer.toString(CouncilHBTotalCount_SelfEmployedClaimants));
        summary.put(
                sCouncilCTBTotalCountClaimantsSelfEmployed,
                Integer.toString(CouncilCTBTotalCount_SelfEmployedClaimants));
        // Students
        summary.put(
                sCouncilHBTotalCountClaimantsStudents,
                Integer.toString(CouncilHBTotalCount_StudentsClaimants));
        summary.put(
                sCouncilCTBTotalCountClaimantsStudents,
                Integer.toString(CouncilCTBTotalCount_StudentsClaimants));
        // LHACases
        t = CouncilHBTotalCount_LHACases + CouncilCTBTotalCount_LHACases;
        summary.put(
                sCouncilAllTotalCount_LHACases,
                Integer.toString(t));
        summary.put(
                sCouncilHBTotalCount_LHACases,
                Integer.toString(HBTotalCount_LHACases));
        summary.put(
                sCouncilCTBTotalCount_LHACases,
                Integer.toString(CTBTotalCount_LHACases));
        // RSL
        // WeeklyHBEntitlement
        summary.put(
                sRSLTotalWeeklyHBEntitlement,
                BigDecimal.valueOf(RSLAllTotalWeeklyHBEntitlement).toPlainString());
        summary.put(
                sRSLTotalCount_WeeklyHBEntitlementNonZero,
                Integer.toString(RSLAllTotalWeeklyHBEntitlementNonZeroCount));
        summary.put(
                sRSLTotalCount_WeeklyHBEntitlementZero,
                Integer.toString(RSLAllTotalWeeklyHBEntitlementZeroCount));
        summary.put(
                sRSLHBTotalWeeklyHBEntitlement,
                BigDecimal.valueOf(RSLHBTotalWeeklyHBEntitlement).toPlainString());
        summary.put(
                sRSLHBTotalCount_WeeklyHBEntitlementNonZero,
                Integer.toString(RSLHBTotalCount_WeeklyHBEntitlementNonZero));
        summary.put(
                sRSLHBTotalCount_WeeklyHBEntitlementZero,
                Integer.toString(RSLHBTotalCount_WeeklyHBEntitlementZero));
        summary.put(
                sRSLCTBTotalWeeklyHBEntitlement,
                BigDecimal.valueOf(RSLCTBTotalWeeklyHBEntitlement).toPlainString());
        summary.put(
                sRSLCTBTotalCount_WeeklyHBEntitlementNonZero,
                Integer.toString(RSLCTBTotalCount_WeeklyHBEntitlementNonZero));
        summary.put(
                sRSLCTBTotalCount_WeeklyHBEntitlementZero,
                Integer.toString(RSLCTBTotalWeeklyHBEntitlementZeroCount));
        // WeeklyCTBEntitlement
        summary.put(
                sRSLTotalWeeklyCTBEntitlement,
                BigDecimal.valueOf(RSLAllTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(
                sRSLTotalCount_WeeklyCTBEntitlementNonZero,
                Integer.toString(RSLAllTotalCount_WeeklyCTBEntitlementNonZero));
        summary.put(
                sRSLTotalCount_WeeklyCTBEntitlementZero,
                Integer.toString(RSLAllTotalWeeklyCTBEntitlementZeroCount));
        summary.put(
                sRSLHBTotalWeeklyCTBEntitlement,
                BigDecimal.valueOf(RSLHBTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(
                sRSLHBTotalCount_WeeklyCTBEntitlementNonZero,
                Integer.toString(RSLHBTotalCount_WeeklyCTBEntitlementNonZero));
        summary.put(
                sRSLHBTotalCount_WeeklyCTBEntitlementZero,
                Integer.toString(RSLHBTotalWeeklyCTBEntitlementZeroCount));
        summary.put(
                sRSLCTBTotalWeeklyCTBEntitlement,
                BigDecimal.valueOf(RSLCTBTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(
                sRSLCTBTotalCount_WeeklyCTBEntitlementNonZero,
                Integer.toString(RSLCTBTotalCount_WeeklyCTBEntitlementNonZero));
        summary.put(
                sRSLCTBTotalCount_WeeklyCTBEntitlementZero,
                Integer.toString(RSLCTBTotalWeeklyCTBEntitlementZeroCount));
        // WeeklyEligibleRentAmount
        summary.put(
                sRSLAllTotalWeeklyEligibleRentAmount,
                BigDecimal.valueOf(RSLAllTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                sRSLAllTotalCount_WeeklyEligibleRentAmountNonZero,
                Integer.toString(RSLAllTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(
                sRSLAllTotalCount_WeeklyEligibleRentAmountZero,
                Integer.toString(RSLAllTotalWeeklyEligibleRentAmountZeroCount));
        summary.put(
                sRSLHBTotalWeeklyEligibleRentAmount,
                BigDecimal.valueOf(RSLHBTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                sRSLHBTotalCount_WeeklyEligibleRentAmountNonZero,
                Integer.toString(RSLHBTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(
                sRSLHBTotalCount_WeeklyEligibleRentAmountZero,
                Integer.toString(RSLHBTotalWeeklyEligibleRentAmountZeroCount));
        summary.put(
                sRSLCTBTotalWeeklyEligibleRentAmount,
                BigDecimal.valueOf(RSLCTBTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                sRSLCTBTotalCount_WeeklyEligibleRentAmountNonZero,
                Integer.toString(RSLCTBTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(
                sRSLCTBTotalCount_WeeklyEligibleRentAmountZero,
                Integer.toString(RSLCTBTotalWeeklyEligibleRentAmountZeroCount));
        // WeeklyEligibleCouncilTaxAmount
        summary.put(
                sRSLAllTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(RSLAllTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(
                sRSLAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(RSLAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(
                sRSLAllTotalCount_WeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(RSLAllTotalCount_WeeklyEligibleCouncilTaxAmountZero));
        summary.put(
                sRSLHBTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(RSLHBTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(
                sRSLHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(RSLHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(
                sRSLHBTotalCount_WeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(RSLHBTotalCount_WeeklyEligibleCouncilTaxAmountZero));
        summary.put(
                sRSLCTBTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(RSLCTBTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(
                sRSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(RSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(
                sRSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(RSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero));
        // ContractualRentAmount
        summary.put(
                sRSLAllTotalContractualRentAmount,
                BigDecimal.valueOf(RSLAllTotalContractualRentAmount).toPlainString());
        summary.put(
                sRSLAllTotalCountContractualRentAmountNonZeroCount,
                Integer.toString(RSLAllTotalContractualRentAmountNonZeroCount));
        summary.put(
                sRSLAllTotalCountContractualRentAmountZeroCount,
                Integer.toString(RSLAllTotalContractualRentAmountZeroCount));
        summary.put(
                sRSLHBTotalContractualRentAmount,
                BigDecimal.valueOf(RSLHBTotalContractualRentAmount).toPlainString());
        summary.put(
                sRSLHBTotalCountContractualRentAmountNonZeroCount,
                Integer.toString(RSLHBTotalContractualRentAmountNonZeroCount));
        summary.put(
                sRSLHBTotalCountContractualRentAmountZeroCount,
                Integer.toString(RSLHBTotalContractualRentAmountZeroCount));
        summary.put(
                sRSLCTBTotalContractualRentAmount,
                BigDecimal.valueOf(RSLCTBTotalContractualRentAmount).toPlainString());
        summary.put(
                sRSLCTBTotalCountContractualRentAmountNonZeroCount,
                Integer.toString(RSLCTBTotalContractualRentAmountNonZeroCount));
        summary.put(
                sRSLCTBTotalCountContractualRentAmountZeroCount,
                Integer.toString(RSLCTBTotalContractualRentAmountZeroCount));
        // WeeklyAdditionalDiscretionaryPayment
        summary.put(
                sRSLAllTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(RSLAllTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sRSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(RSLAllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sRSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(RSLAllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        summary.put(
                sRSLHBTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(RSLHBTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sRSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(RSLHBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sRSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(RSLHBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        summary.put(
                sRSLCTBTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(RSLCTBTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sRSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sRSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        summary.put(
                sRSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(RSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sRSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(RSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sRSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(RSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        summary.put(
                sRSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(RSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sRSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(RSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sRSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(RSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        summary.put(
                sRSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sRSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sRSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        // Employed
        t = RSLHBTotalCount_EmployedClaimants + RSLCTBTotalCount_EmployedClaimants;
        summary.put(
                sRSLAllTotalCount_ClaimantsEmployed,
                Integer.toString(t));
        summary.put(
                sRSLHBTotalCount_ClaimantsEmployed,
                Integer.toString(RSLHBTotalCount_EmployedClaimants));
        summary.put(
                sRSLCTBTotalCount_ClaimantsEmployed,
                Integer.toString(RSLCTBTotalCount_EmployedClaimants));
        // Self Employed
        summary.put(
                sRSLHBTotalCountClaimantsSelfEmployed,
                Integer.toString(RSLHBTotalCount_SelfEmployedClaimants));
        summary.put(
                sRSLCTBTotalCountClaimantsSelfEmployed,
                Integer.toString(RSLCTBTotalCount_SelfEmployedClaimants));
        // Students
        summary.put(
                sRSLHBTotalCountClaimantsStudents,
                Integer.toString(RSLHBTotalCount_StudentsClaimants));
        summary.put(
                sRSLCTBTotalCountClaimantsStudents,
                Integer.toString(RSLCTBTotalCount_StudentsClaimants));
        // LHACases
        t = RSLHBTotalCount_LHACases + RSLCTBTotalCount_LHACases;
        summary.put(
                sRSLAllTotalCount_LHACases,
                Integer.toString(t));
        summary.put(
                sRSLHBTotalCount_LHACases,
                Integer.toString(HBTotalCount_LHACases));
        summary.put(
                sRSLCTBTotalCount_LHACases,
                Integer.toString(CTBTotalCount_LHACases));
    }

    @Override
    protected void addToSummarySingleTimeRates1(
            HashMap<String, String> summary) {
        //super.addToSummarySingleTimeRates1(summary);
        double ave;
        double d;
        double t;
        // Council
        // WeeklyHBEntitlement
        t = Double.valueOf(summary.get(sCouncilTotalWeeklyHBEntitlement));
        d = CouncilAllTotalWeeklyHBEntitlementNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilAverageWeeklyHBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilAverageWeeklyHBEntitlement,
                    s0);
        }
        t = Double.valueOf(summary.get(sCouncilHBTotalWeeklyHBEntitlement));
        d = CouncilHBTotalCount_WeeklyHBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilHBAverageWeeklyHBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilHBAverageWeeklyHBEntitlement,
                    s0);
        }
        t = Double.valueOf(summary.get(sCouncilCTBTotalWeeklyHBEntitlement));
        d = CouncilCTBTotalCount_WeeklyHBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilCTBAverageWeeklyHBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilCTBAverageWeeklyHBEntitlement,
                    s0);
        }
        // WeeklyCTBEntitlement
        t = Double.valueOf(summary.get(sCouncilTotalWeeklyCTBEntitlement));
        d = CouncilAllTotalCount_WeeklyCTBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilAverageWeeklyCTBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilAverageWeeklyCTBEntitlement,
                    s0);
        }
        t = Double.valueOf(summary.get(sCouncilHBTotalWeeklyCTBEntitlement));
        d = CouncilHBTotalCount_WeeklyCTBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilHBAverageWeeklyCTBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilHBAverageWeeklyCTBEntitlement,
                    s0);
        }
        t = Double.valueOf(summary.get(sCouncilCTBTotalWeeklyCTBEntitlement));
        d = CouncilCTBTotalCount_WeeklyCTBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilCTBAverageWeeklyCTBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilCTBAverageWeeklyCTBEntitlement,
                    s0);
        }
        // WeeklyEligibleRentAmount
        t = Double.valueOf(summary.get(sCouncilAllTotalWeeklyEligibleRentAmount));
        d = CouncilAllTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilAllAverageWeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilAllAverageWeeklyEligibleRentAmount,
                    s0);
        }
        t = Double.valueOf(summary.get(sCouncilHBTotalWeeklyEligibleRentAmount));
        d = CouncilHBTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilHBAverageWeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilHBAverageWeeklyEligibleRentAmount,
                    s0);
        }
        t = Double.valueOf(summary.get(sCouncilCTBTotalWeeklyEligibleRentAmount));
        d = CouncilCTBTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilCTBAverageWeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilCTBAverageWeeklyEligibleRentAmount,
                    s0);
        }
        // WeeklyEligibleCouncilTaxAmount
        t = Double.valueOf(summary.get(sCouncilAllTotalWeeklyEligibleCouncilTaxAmount));
        d = CouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilAllAverageWeeklyEligibleCouncilTaxAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilAllAverageWeeklyEligibleCouncilTaxAmount,
                    s0);
        }
        t = Double.valueOf(summary.get(sCouncilHBTotalWeeklyEligibleCouncilTaxAmount));
        d = CouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilHBAverageWeeklyEligibleCouncilTaxAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilHBAverageWeeklyEligibleCouncilTaxAmount,
                    s0);
        }
        t = Double.valueOf(summary.get(sCouncilCTBTotalWeeklyEligibleCouncilTaxAmount));
        d = CouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilCTBAverageWeeklyEligibleCouncilTaxAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilCTBAverageWeeklyEligibleCouncilTaxAmount,
                    s0);
        }
        // ContractualRentAmount
        t = Double.valueOf(summary.get(sCouncilAllTotalContractualRentAmount));
        d = CouncilAllTotalContractualRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilAllAverageContractualRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilAllAverageContractualRentAmount,
                    s0);
        }
        t = Double.valueOf(summary.get(sCouncilHBTotalContractualRentAmount));
        d = CouncilHBTotalContractualRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilHBAverageContractualRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilHBAverageContractualRentAmount,
                    s0);
        }
        t = Double.valueOf(summary.get(sCouncilCTBTotalContractualRentAmount));
        d = CouncilCTBTotalContractualRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilCTBAverageContractualRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilCTBAverageContractualRentAmount,
                    s0);
        }
        // WeeklyAdditionalDiscretionaryPayment
        t = Double.valueOf(summary.get(sCouncilAllTotalWeeklyAdditionalDiscretionaryPayment));
        d = CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilAllAverageWeeklyAdditionalDiscretionaryPayment,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilAllAverageWeeklyAdditionalDiscretionaryPayment,
                    s0);
        }
        t = Double.valueOf(summary.get(sCouncilHBTotalWeeklyAdditionalDiscretionaryPayment));
        d = CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilHBAverageWeeklyAdditionalDiscretionaryPayment,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilHBAverageWeeklyAdditionalDiscretionaryPayment,
                    s0);
        }
        t = Double.valueOf(summary.get(sCouncilCTBTotalWeeklyAdditionalDiscretionaryPayment));
        d = CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilCTBAverageWeeklyAdditionalDiscretionaryPayment,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilCTBAverageWeeklyAdditionalDiscretionaryPayment,
                    s0);
        }
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        t = Double.valueOf(summary.get(sCouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability));
        d = CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    s0);
        }
        t = Double.valueOf(summary.get(sCouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability));
        d = CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    s0);
        }
        t = Double.valueOf(summary.get(sCouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability));
        d = CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    s0);
        }
        // Employed
        t = Integer.valueOf(summary.get(sCouncilAllTotalCount_ClaimantsEmployed));
        d = CouncilAllCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sCouncilAllPercentage_ClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilAllPercentage_ClaimantsEmployed,
                    s0);
        }
        t = Integer.valueOf(summary.get(sCouncilHBTotalCount_ClaimantsEmployed));
        d = CouncilHBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sCouncilHBPercentageOfHB_ClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilHBPercentageOfHB_ClaimantsEmployed,
                    s0);
        }
        t = Integer.valueOf(summary.get(sCouncilCTBTotalCount_ClaimantsEmployed));
        d = CouncilCTBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sCouncilCTBPercentageOfCTB_ClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilCTBPercentageOfCTB_ClaimantsEmployed,
                    s0);
        }
        // Self Employed
        t = Integer.valueOf(summary.get(sCouncilHBTotalCountClaimantsSelfEmployed));
        d = CouncilHBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sCouncilHBPercentageOfHB_ClaimantsSelfEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilHBPercentageOfHB_ClaimantsSelfEmployed,
                    s0);
        }
        t = Integer.valueOf(summary.get(sCouncilCTBTotalCountClaimantsSelfEmployed));
        d = CouncilCTBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sCouncilCTBPercentageOfCTB_ClaimantsSelfEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilCTBPercentageOfCTB_ClaimantsSelfEmployed,
                    s0);
        }
        // Students
        t = Integer.valueOf(summary.get(sCouncilHBTotalCountClaimantsStudents));
        d = CouncilHBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sCouncilHBPercentageOfHB_ClaimantsStudents,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilHBPercentageOfHB_ClaimantsStudents,
                    s0);
        }
        t = Integer.valueOf(summary.get(sCouncilCTBTotalCountClaimantsStudents));
        d = CouncilCTBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sCouncilCTBPercentageOfCTB_ClaimantsStudents,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilCTBPercentageOfCTB_ClaimantsStudents,
                    s0);
        }
        // LHACases
        t = Integer.valueOf(summary.get(sCouncilAllTotalCount_LHACases));
        d = CouncilAllCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sCouncilAllPercentageOfAll_LHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilAllPercentageOfAll_LHACases,
                    s0);
        }
        t = Integer.valueOf(summary.get(sCouncilHBTotalCount_LHACases));
        d = CouncilHBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sCouncilHBPercentageOfHB_LHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilHBPercentageOfHB_LHACases,
                    s0);
        }
        t = Integer.valueOf(summary.get(sCouncilCTBTotalCount_LHACases));
        d = CouncilCTBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sCouncilCTBPercentageOfCTB_LHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilCTBPercentageOfCTB_LHACases,
                    s0);
        }
        // RSL
        // WeeklyHBEntitlement
        t = Double.valueOf(summary.get(sRSLTotalWeeklyHBEntitlement));
        d = RSLAllTotalWeeklyHBEntitlementNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLAverageWeeklyHBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLAverageWeeklyHBEntitlement,
                    s0);
        }
        t = Double.valueOf(summary.get(sRSLHBTotalWeeklyHBEntitlement));
        d = RSLHBTotalCount_WeeklyHBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLHBAverageWeeklyHBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLHBAverageWeeklyHBEntitlement,
                    s0);
        }
        t = Double.valueOf(summary.get(sRSLCTBTotalWeeklyHBEntitlement));
        d = RSLCTBTotalCount_WeeklyHBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLCTBAverageWeeklyHBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLCTBAverageWeeklyHBEntitlement,
                    s0);
        }
        // WeeklyCTBEntitlement
        t = Double.valueOf(summary.get(sRSLTotalWeeklyCTBEntitlement));
        d = RSLAllTotalCount_WeeklyCTBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLAverageWeeklyCTBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLAverageWeeklyCTBEntitlement,
                    s0);
        }
        t = Double.valueOf(summary.get(sRSLHBTotalWeeklyCTBEntitlement));
        d = RSLHBTotalCount_WeeklyCTBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLHBAverageWeeklyCTBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLHBAverageWeeklyCTBEntitlement,
                    s0);
        }
        t = Double.valueOf(summary.get(sRSLCTBTotalWeeklyCTBEntitlement));
        d = RSLCTBTotalCount_WeeklyCTBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLCTBAverageWeeklyCTBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLCTBAverageWeeklyCTBEntitlement,
                    s0);
        }
        // WeeklyEligibleRentAmount
        t = Double.valueOf(summary.get(sRSLAllTotalWeeklyEligibleRentAmount));
        d = RSLAllTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLAllAverageWeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLAllAverageWeeklyEligibleRentAmount,
                    s0);
        }
        t = Double.valueOf(summary.get(sRSLHBTotalWeeklyEligibleRentAmount));
        d = RSLHBTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLHBAverageWeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLHBAverageWeeklyEligibleRentAmount,
                    s0);
        }
        t = Double.valueOf(summary.get(sRSLCTBTotalWeeklyEligibleRentAmount));
        d = RSLCTBTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLCTBAverageWeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLCTBAverageWeeklyEligibleRentAmount,
                    s0);
        }
        // WeeklyEligibleCouncilTaxAmount
        t = Double.valueOf(summary.get(sRSLAllTotalWeeklyEligibleCouncilTaxAmount));
        d = RSLAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLAllAverageWeeklyEligibleCouncilTaxAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLAllAverageWeeklyEligibleCouncilTaxAmount,
                    s0);
        }
        t = Double.valueOf(summary.get(sRSLHBTotalWeeklyEligibleCouncilTaxAmount));
        d = RSLHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLHBAverageWeeklyEligibleCouncilTaxAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLHBAverageWeeklyEligibleCouncilTaxAmount,
                    s0);
        }
        t = Double.valueOf(summary.get(sRSLCTBTotalWeeklyEligibleCouncilTaxAmount));
        d = RSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLCTBAverageWeeklyEligibleCouncilTaxAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLCTBAverageWeeklyEligibleCouncilTaxAmount,
                    s0);
        }
        // ContractualRentAmount
        t = Double.valueOf(summary.get(sRSLAllTotalContractualRentAmount));
        d = RSLAllTotalContractualRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLAllAverageContractualRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLAllAverageContractualRentAmount,
                    s0);
        }
        t = Double.valueOf(summary.get(sRSLHBTotalContractualRentAmount));
        d = RSLHBTotalContractualRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLHBAverageContractualRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLHBAverageContractualRentAmount,
                    s0);
        }
        t = Double.valueOf(summary.get(sRSLCTBTotalContractualRentAmount));
        d = RSLCTBTotalContractualRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLCTBAverageContractualRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLCTBAverageContractualRentAmount,
                    s0);
        }
        // WeeklyAdditionalDiscretionaryPayment
        t = Double.valueOf(summary.get(sRSLAllTotalWeeklyAdditionalDiscretionaryPayment));
        d = RSLAllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLAllAverageWeeklyAdditionalDiscretionaryPayment,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLAllAverageWeeklyAdditionalDiscretionaryPayment,
                    s0);
        }
        t = Double.valueOf(summary.get(sRSLHBTotalWeeklyAdditionalDiscretionaryPayment));
        d = RSLHBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLHBAverageWeeklyAdditionalDiscretionaryPayment,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLHBAverageWeeklyAdditionalDiscretionaryPayment,
                    s0);
        }
        t = Double.valueOf(summary.get(sRSLCTBTotalWeeklyAdditionalDiscretionaryPayment));
        d = RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLCTBAverageWeeklyAdditionalDiscretionaryPayment,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLCTBAverageWeeklyAdditionalDiscretionaryPayment,
                    s0);
        }
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        t = Double.valueOf(summary.get(sRSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability));
        d = RSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    s0);
        }
        t = Double.valueOf(summary.get(sRSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability));
        d = RSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    s0);
        }
        t = Double.valueOf(summary.get(sRSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability));
        d = RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    s0);
        }
        // Employed
        t = Integer.valueOf(summary.get(sRSLAllTotalCount_ClaimantsEmployed));
        d = RSLAllCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sRSLAllPercentage_ClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLAllPercentage_ClaimantsEmployed,
                    s0);
        }
        t = Integer.valueOf(summary.get(sRSLHBTotalCount_ClaimantsEmployed));
        d = RSLHBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sRSLHBPercentageOfHB_ClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLHBPercentageOfHB_ClaimantsEmployed,
                    s0);
        }
        t = Integer.valueOf(summary.get(sRSLCTBTotalCount_ClaimantsEmployed));
        d = RSLCTBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sRSLCTBPercentageOfCTB_ClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLCTBPercentageOfCTB_ClaimantsEmployed,
                    s0);
        }
        // Self Employed
        t = Integer.valueOf(summary.get(sRSLHBTotalCountClaimantsSelfEmployed));
        d = RSLHBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sRSLHBPercentageOfHB_ClaimantsSelfEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLHBPercentageOfHB_ClaimantsSelfEmployed,
                    s0);
        }
        t = Integer.valueOf(summary.get(sRSLCTBTotalCountClaimantsSelfEmployed));
        d = RSLCTBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sRSLCTBPercentageOfCTB_ClaimantsSelfEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLCTBPercentageOfCTB_ClaimantsSelfEmployed,
                    s0);
        }
        // Students
        t = Integer.valueOf(summary.get(sRSLHBTotalCountClaimantsStudents));
        d = RSLHBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sRSLHBPercentageOfHB_ClaimantsStudents,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLHBPercentageOfHB_ClaimantsStudents,
                    s0);
        }
        t = Integer.valueOf(summary.get(sRSLCTBTotalCountClaimantsStudents));
        d = RSLCTBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sRSLCTBPercentageOfCTB_ClaimantsStudents,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLCTBPercentageOfCTB_ClaimantsStudents,
                    s0);
        }
        // LHACases
        t = Integer.valueOf(summary.get(sRSLAllTotalCount_LHACases));
        d = RSLAllCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sRSLAllPercentageOfAll_LHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLAllPercentageOfAll_LHACases,
                    s0);
        }
        t = Integer.valueOf(summary.get(sRSLHBTotalCount_LHACases));
        d = RSLHBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sRSLHBPercentageOfHB_LHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLHBPercentageOfHB_LHACases,
                    s0);
        }
        t = Integer.valueOf(summary.get(sRSLCTBTotalCount_LHACases));
        d = RSLCTBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sRSLCTBPercentageOfCTB_LHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLCTBPercentageOfCTB_LHACases,
                    s0);
        }
    }

    @Override
    protected void addToSummarySingleTimeEthnicityCounts(
            int nEG,
            HashMap<String, String> summary) {
        //super.addToSummarySingleTimeEthnicityCounts(nEG, summary);
        // Council
        for (int i = 1; i < nEG; i++) {
            int all;
            all = CouncilHBEthnicGroupCount[i] + CouncilCTBEthnicGroupCount[i];
            summary.put(
                    sCouncilAllTotalCount_EthnicGroupClaimant[i],
                    Integer.toString(all));
            summary.put(
                    sCouncilHBTotalCount_EthnicGroupClaimant[i],
                    Integer.toString(CouncilHBEthnicGroupCount[i]));
            summary.put(
                    sCouncilCTBTotalCount_EthnicGroupClaimant[i],
                    Integer.toString(CouncilCTBEthnicGroupCount[i]));
        }
        // RSL
        for (int i = 1; i < nEG; i++) {
            int all;
            all = RSLHBEthnicGroupCount[i] + RSLCTBEthnicGroupCount[i];
            summary.put(
                    sRSLAllTotalCount_EthnicGroupClaimant[i],
                    Integer.toString(all));
            summary.put(
                    sRSLHBTotalCount_EthnicGroupClaimant[i],
                    Integer.toString(RSLHBEthnicGroupCount[i]));
            summary.put(
                    sRSLCTBTotalCount_EthnicGroupClaimant[i],
                    Integer.toString(RSLCTBEthnicGroupCount[i]));
        }
    }

    @Override
    protected void addToSummarySingleTimeEthnicityRates(
            int nEG,
            HashMap<String, String> summary) {
        //super.addToSummarySingleTimeEthnicityRates(nEG, summary);
        double percentage;
        double all;
        double d;
        // Council
        for (int i = 1; i < nEG; i++) {
            all = Integer.valueOf(summary.get(sCouncilAllTotalCount_EthnicGroupClaimant[i]));
            d = CouncilAllCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(
                        sCouncilAllPercentageOfAll_EthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sCouncilHBTotalCount_EthnicGroupClaimant[i]));
            d = CouncilHBCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(
                        sCouncilHBPercentageOfHB_EthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sCouncilCTBTotalCount_EthnicGroupClaimant[i]));
            d = CouncilCTBCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(
                        sCouncilCTBPercentageOfCTB_EthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
        }
        // RSL
        for (int i = 1; i < nEG; i++) {
            all = Integer.valueOf(summary.get(sRSLAllTotalCount_EthnicGroupClaimant[i]));
            d = RSLAllCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(
                        sRSLAllPercentageOfAll_EthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sRSLHBTotalCount_EthnicGroupClaimant[i]));
            d = RSLHBCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(
                        sRSLHBPercentageOfHB_EthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sRSLCTBTotalCount_EthnicGroupClaimant[i]));
            d = RSLCTBCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(
                        sRSLCTBPercentageOfCTB_EthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
        }
    }

    @Override
    protected void addToSummarySingleTimeTTCounts(
            int nTT,
            HashMap<String, String> summary) {
        int all;
        // Council
        all = CouncilTotalCount_TTClaimant1[1] + CouncilTotalCount_TTClaimant1[4];
        summary.put(
                sCouncilTotalCount_SocialTTsClaimant,
                Integer.toString(all));
        all = CouncilTotalCount_TTClaimant1[3] + CouncilTotalCount_TTClaimant1[6];
        summary.put(
                sCouncilTotalCount_PrivateDeregulatedTTsClaimant,
                Integer.toString(all));
        // TT
        for (int i = 1; i < nTT; i++) {
            all = CouncilTotalCount_TTClaimant1[i];
            summary.put(
                    sCouncilTotalCount_ClaimantTT[i],
                    Integer.toString(all));
        }
        summary.put(sCouncilAllTotalCount_PostcodeValidFormat,
                Integer.toString(CouncilHBTotalCount_PostcodeValidFormat + CouncilCTBTotalCount_PostcodeValidFormat));
        summary.put(sCouncilAllTotalCount_PostcodeValid,
                Integer.toString(CouncilHBTotalCount_PostcodeValid + CouncilCTBTotalCount_PostcodeValid));
        // HB
        summary.put(sCouncilHBCount1, Integer.toString(CouncilHBCount1));
        summary.put(sCouncilHBTotalCount_PostcodeValidFormat,
                Integer.toString(CouncilHBTotalCount_PostcodeValidFormat));
        summary.put(sCouncilHBTotalCount_PostcodeValid,
                Integer.toString(CouncilHBTotalCount_PostcodeValid));
        // CTB
        summary.put(sCouncilCTBCount1, Integer.toString(CouncilCTBCount1));
        summary.put(sCouncilCTBTotalCount_PostcodeValidFormat,
                Integer.toString(CouncilCTBTotalCount_PostcodeValidFormat));
        summary.put(sCouncilCTBTotalCount_PostcodeValid,
                Integer.toString(CouncilCTBTotalCount_PostcodeValid));
        // RSL
        all = RSLTotalCount_TTClaimant1[1] + RSLTotalCount_TTClaimant1[4];
        summary.put(
                sRSLTotalCount_SocialTTsClaimant,
                Integer.toString(all));
        all = RSLTotalCount_TTClaimant1[3] + RSLTotalCount_TTClaimant1[6];
        summary.put(
                sRSLTotalCount_PrivateDeregulatedTTsClaimant,
                Integer.toString(all));
        // TT
        for (int i = 1; i < nTT; i++) {
            all = RSLTotalCount_TTClaimant1[i];
            summary.put(
                    sRSLTotalCount_ClaimantTT[i],
                    Integer.toString(all));
        }
        summary.put(sRSLAllTotalCount_PostcodeValidFormat,
                Integer.toString(RSLHBTotalCount_PostcodeValidFormat + RSLCTBTotalCount_PostcodeValidFormat));
        summary.put(sRSLAllTotalCount_PostcodeValid,
                Integer.toString(RSLHBTotalCount_PostcodeValid + RSLCTBTotalCount_PostcodeValid));
        // HB
        summary.put(sRSLHBCount1, Integer.toString(RSLHBCount1));
        summary.put(sRSLHBTotalCount_PostcodeValidFormat,
                Integer.toString(RSLHBTotalCount_PostcodeValidFormat));
        summary.put(sRSLHBTotalCount_PostcodeValid,
                Integer.toString(RSLHBTotalCount_PostcodeValid));
        // CTB
        summary.put(sRSLCTBCount1, Integer.toString(RSLCTBCount1));
        summary.put(sRSLCTBTotalCount_PostcodeValidFormat,
                Integer.toString(RSLCTBTotalCount_PostcodeValidFormat));
        summary.put(sRSLCTBTotalCount_PostcodeValid,
                Integer.toString(RSLCTBTotalCount_PostcodeValid));
    }

    @Override
    protected void addToSummarySingleTimeTTRates(
            int nTT,
            HashMap<String, String> summary) {
        //super.addToSummarySingleTimeTTRates(nTT, summary);
        double percentage;
        double d;
        int all;
        // Council
        all = Integer.valueOf(summary.get(sCouncilTotalCount_SocialTTsClaimant));
        d = CouncilAllCount1;
        if (CouncilAllCount1 > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_SocialTTsClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilHBCount1;
        if (CouncilHBCount1 > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_SocialTTsClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        all = Integer.valueOf(summary.get(sCouncilTotalCount_PrivateDeregulatedTTsClaimant));
        d = CouncilAllCount1;
        if (CouncilAllCount1 > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfAll_PrivateDeregulatedTTsClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilHBCount1;
        if (CouncilHBCount1 > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_PrivateDeregulatedTTsClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // TT
        for (int i = 1; i < nTT; i++) {
            all = Integer.valueOf(summary.get(sCouncilTotalCount_ClaimantTT[i]));
            d = CouncilAllCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfAll_ClaimantTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            if (i == 5 || i == 7) {
                d = CouncilCTBCount1;
                if (d > 0) {
                    percentage = (all * 100.0d) / d;
                    summary.put(
                            sCouncilPercentageOfCTB_ClaimantTT[i],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(percentage),
                                    decimalPlacePrecisionForPercentage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
            } else {
                d = CouncilHBCount1;
                if (d > 0) {
                    percentage = (all * 100.0d) / d;
                    summary.put(
                            sCouncilPercentageOfHB_ClaimantTT[i],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(percentage),
                                    decimalPlacePrecisionForPercentage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
            }
        }
        // RSL
        all = Integer.valueOf(summary.get(sRSLTotalCount_SocialTTsClaimant));
        d = RSLAllCount1;
        if (RSLAllCount1 > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_SocialTTsClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLHBCount1;
        if (RSLHBCount1 > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_SocialTTsClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        all = Integer.valueOf(summary.get(sRSLTotalCount_PrivateDeregulatedTTsClaimant));
        d = RSLAllCount1;
        if (RSLAllCount1 > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfAll_PrivateDeregulatedTTsClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLHBCount1;
        if (RSLHBCount1 > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_PrivateDeregulatedTTsClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // TT
        for (int i = 1; i < nTT; i++) {
            all = Integer.valueOf(summary.get(sRSLTotalCount_ClaimantTT[i]));
            d = RSLAllCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfAll_ClaimantTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            if (i == 5 || i == 7) {
                d = RSLCTBCount1;
                if (d > 0) {
                    percentage = (all * 100.0d) / d;
                    summary.put(
                            sRSLPercentageOfCTB_ClaimantTT[i],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(percentage),
                                    decimalPlacePrecisionForPercentage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
            } else {
                d = RSLHBCount1;
                if (d > 0) {
                    percentage = (all * 100.0d) / d;
                    summary.put(
                            sRSLPercentageOfHB_ClaimantTT[i],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(percentage),
                                    decimalPlacePrecisionForPercentage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
            }
        }
    }


    @Override
    protected void doCompare2TimesCounts(
            DW_SHBE_D_Record D_Record0,
            DW_SHBE_D_Record D_Record1,
            String yM30v,
            String yM31v) {
        //super.doCompare2TimesCounts(D_Record0, D_Record1, yM30v, yM31v);
        boolean isHBClaim;
        isHBClaim = false;
        boolean isCTBOnlyClaim;
        isCTBOnlyClaim = false;
        if (D_Record1 != null) {
            doSingleTimeCount(
                    D_Record1,
                    yM30v);
            isHBClaim = DW_SHBE_Handler.isHBClaim(D_Record1);
            isCTBOnlyClaim = DW_SHBE_Handler.isCTBOnlyClaim(D_Record1);
        }
        String postcode0;
        postcode0 = null;
        int TT0;
        TT0 = DW_SHBE_TenancyType_Handler.iMinus999;
        boolean isValidPostcode0;
        isValidPostcode0 = false;
        if (D_Record0 != null) {
            postcode0 = D_Record0.getClaimantsPostcode();
            if (postcode0 != null) {
                isValidPostcode0 = DW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
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
                isValidPostcode1 = DW_Postcode_Handler.isValidPostcode(yM31v, postcode1);
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
        // RSL
        RSLAllTotalCount_TTChangeClaimant = RSLHBTotalCount_TTChangeClaimant + RSLCTBTotalCount_TTChangeClaimant;
        RSLAllTotalCount_Postcode0ValidPostcode1Valid = RSLHBTotalCount_Postcode0ValidPostcode1Valid + RSLCTBTotalCount_Postcode0ValidPostcode1Valid;
        RSLAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = RSLHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged + RSLCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged;
        RSLAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged = RSLHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged + RSLCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged;
        RSLAllTotalCount_Postcode0ValidPostcode1NotValid = RSLHBTotalCount_Postcode0ValidPostcode1NotValid + RSLCTBTotalCount_Postcode0ValidPostcode1NotValid;
        RSLAllTotalCount_Postcode0NotValidPostcode1Valid = RSLHBTotalCount_Postcode0NotValidPostcode1Valid + RSLCTBTotalCount_Postcode0NotValidPostcode1Valid;
        RSLAllTotalCount_Postcode0NotValidPostcode1NotValid = RSLHBTotalCount_Postcode0NotValidPostcode1NotValid + RSLCTBTotalCount_Postcode0NotValidPostcode1NotValid;
        RSLAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = RSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged + RSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged;
        RSLAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = RSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged + RSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged;
    }

    @Override
    protected void doSingleTimeCount(
            DW_SHBE_D_Record D_Record,
            String yM30v) {
        //super.doSingleTimeCount(D_Record, yM30v);
        // Council
        int ClaimantsEthnicGroup0;
        ClaimantsEthnicGroup0 = D_Record.getClaimantsEthnicGroup();
        int TT;
        String postcode;
        // All unfiltered counts
        TT = D_Record.getTenancyType();
        postcode = D_Record.getClaimantsPostcode();
        CouncilTotalCount_TTClaimant1[TT]++;
        // Disability
        int DisabilityPremiumAwarded;
        DisabilityPremiumAwarded = D_Record.getDisabilityPremiumAwarded();
        if (DisabilityPremiumAwarded == 1) {
            CouncilTotalCount_DisabilityPremiumAwardByTT[TT]++;
        }
        int SevereDisabilityPremiumAwarded;
        SevereDisabilityPremiumAwarded = D_Record.getSevereDisabilityPremiumAwarded();
        if (SevereDisabilityPremiumAwarded == 1) {
            CouncilTotalCount_SevereDisabilityPremiumAwardByTT[TT]++;
        }
        int DisabledChildPremiumAwarded;
        DisabledChildPremiumAwarded = D_Record.getDisabledChildPremiumAwarded();
        if (DisabledChildPremiumAwarded == 1) {
            CouncilTotalCount_DisabledChildPremiumAwardByTT[TT]++;
        }
        int EnhancedDisabilityPremiumAwarded;
        EnhancedDisabilityPremiumAwarded = D_Record.getEnhancedDisabilityPremiumAwarded();
        if (EnhancedDisabilityPremiumAwarded == 1) {
            CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[TT]++;
        }
        // General Household Disability Flag
        if (DisabilityPremiumAwarded == 1
                || SevereDisabilityPremiumAwarded == 1
                || DisabledChildPremiumAwarded == 1
                || EnhancedDisabilityPremiumAwarded == 1) {
            CouncilTotalCount_DisabilityAwardByTT[TT]++;
        }
        // Passported Standard Indicator
        int PSI;
        PSI = D_Record.getPassportedStandardIndicator();
        CouncilAllTotalCount_PSI[PSI]++;
        CouncilAllTotalCount_PSIByTT[PSI][TT]++;
        // Household size
        long HouseholdSize;
        HouseholdSize = DW_SHBE_Handler.getHouseholdSize(D_Record);
        CouncilAllTotalHouseholdSize += HouseholdSize;
        // Entitlements
        int WeeklyHousingBenefitEntitlement;
        WeeklyHousingBenefitEntitlement = D_Record.getWeeklyHousingBenefitEntitlement();
        if (WeeklyHousingBenefitEntitlement > 0) {
            CouncilAllTotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
            CouncilAllTotalWeeklyHBEntitlementNonZeroCount++;
        } else {
            CouncilAllTotalWeeklyHBEntitlementZeroCount++;
        }
        int WeeklyCouncilTaxBenefitBenefitEntitlement;
        WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
        if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
            CouncilAllTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
            CouncilAllTotalCount_WeeklyCTBEntitlementNonZero++;
        } else {
            CouncilAllTotalWeeklyCTBEntitlementZeroCount++;
        }
        // Eligible Amounts
        int WeeklyEligibleRentAmount;
        WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
        if (WeeklyEligibleRentAmount > 0) {
            CouncilAllTotalWeeklyEligibleRentAmount += WeeklyEligibleRentAmount;
            CouncilAllTotalWeeklyEligibleRentAmountNonZeroCount++;
        } else {
            CouncilAllTotalWeeklyEligibleRentAmountZeroCount++;
        }
        int WeeklyEligibleCouncilTaxAmount;
        WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
        if (WeeklyEligibleCouncilTaxAmount > 0) {
            CouncilAllTotalWeeklyEligibleCouncilTaxAmount += WeeklyEligibleCouncilTaxAmount;
            CouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero++;
        } else {
            CouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountZero++;
        }
        int ContractualRentAmount;
        ContractualRentAmount = D_Record.getContractualRentAmount();
        if (ContractualRentAmount > 0) {
            CouncilAllTotalContractualRentAmount += ContractualRentAmount;
            CouncilAllTotalContractualRentAmountNonZeroCount++;
        } else {
            CouncilAllTotalContractualRentAmountZeroCount++;
        }
        // Additional Payments
        int WeeklyAdditionalDiscretionaryPayment;
        WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
        if (WeeklyAdditionalDiscretionaryPayment > 0) {
            CouncilAllTotalWeeklyAdditionalDiscretionaryPayment += WeeklyAdditionalDiscretionaryPayment;
            CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount++;
        } else {
            CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount++;
        }
        int WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
        WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
        if (WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability > 0) {
            CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability += WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
            CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount++;
        } else {
            CouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount++;
        }
        // HBClaim only counts
        if (DW_SHBE_Handler.isHBClaim(D_Record)) {
            CouncilHBTotalCount_PSI[PSI]++;
            CouncilHBTotalCount_PSIByTT[PSI][TT]++;
            //if (HBRef.equalsIgnoreCase(CTBRef)) {
            CouncilHBTotalHouseholdSize += HouseholdSize;
            int ClaimantsNetWeeklyIncomeFromEmployment;
            ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
            if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
                CouncilHBTotalCount_EmployedClaimants++;
            }
            int ClaimantsNetWeeklyIncomeFromSelfEmployment;
            ClaimantsNetWeeklyIncomeFromSelfEmployment = D_Record.getClaimantsNetWeeklyIncomeFromSelfEmployment();
            if (ClaimantsNetWeeklyIncomeFromSelfEmployment > 0) {
                CouncilHBTotalCount_SelfEmployedClaimants++;
            }
            String ClaimantsStudentIndicator;
            ClaimantsStudentIndicator = D_Record.getClaimantsStudentIndicator();
            if (ClaimantsStudentIndicator != null) {
                if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                    CouncilHBTotalCount_StudentsClaimants++;
                }
            }
            String LHARegulationsApplied;
            LHARegulationsApplied = D_Record.getLHARegulationsApplied();
            if (LHARegulationsApplied != null) {
                if (LHARegulationsApplied.equalsIgnoreCase("1")
                        || LHARegulationsApplied.equalsIgnoreCase("Yes")) {
                    CouncilHBTotalCount_LHACases++;
                }
            }
            if (WeeklyHousingBenefitEntitlement > 0) {
                CouncilHBTotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
                CouncilHBTotalCount_WeeklyHBEntitlementNonZero++;
            } else {
                CouncilHBTotalCount_WeeklyHBEntitlementZero++;
            }
            WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
            if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
                CouncilHBTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
                CouncilHBTotalCount_WeeklyCTBEntitlementNonZero++;
            } else {
                CouncilHBTotalWeeklyCTBEntitlementZeroCount++;
            }
            WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
            if (WeeklyEligibleRentAmount > 0) {
                CouncilHBTotalWeeklyEligibleRentAmount += WeeklyEligibleRentAmount;
                CouncilHBTotalWeeklyEligibleRentAmountNonZeroCount++;
            } else {
                CouncilHBTotalWeeklyEligibleRentAmountZeroCount++;
            }
            WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
            if (WeeklyEligibleCouncilTaxAmount > 0) {
                CouncilHBTotalWeeklyEligibleCouncilTaxAmount += WeeklyEligibleCouncilTaxAmount;
                CouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero++;
            } else {
                CouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountZero++;
            }
            ContractualRentAmount = D_Record.getContractualRentAmount();
            if (ContractualRentAmount > 0) {
                CouncilHBTotalContractualRentAmount += ContractualRentAmount;
                CouncilHBTotalContractualRentAmountNonZeroCount++;
            } else {
                CouncilHBTotalContractualRentAmountZeroCount++;
            }
            WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
            if (WeeklyAdditionalDiscretionaryPayment > 0) {
                CouncilHBTotalWeeklyAdditionalDiscretionaryPayment += WeeklyAdditionalDiscretionaryPayment;
                CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount++;
            } else {
                CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount++;
            }
            WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
            if (WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability > 0) {
                CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability += WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
                CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount++;
            } else {
                CouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount++;
            }
            doSingleTimeHBCount(
                    ClaimantsEthnicGroup0,
                    postcode,
                    yM30v);
        }
        // CTB Claim only counts
        if (DW_SHBE_Handler.isCTBOnlyClaim(D_Record)) {
            CouncilCTBTotalCount_PSI[PSI]++;
            CouncilCTBTotalCount_PSIByTT[PSI][TT]++;
            CouncilCTBTotalHouseholdSize += HouseholdSize;
            int ClaimantsNetWeeklyIncomeFromEmployment;
            ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
            if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
                CouncilCTBTotalCount_EmployedClaimants++;
            }
            int ClaimantsNetWeeklyIncomeFromSelfEmployment;
            ClaimantsNetWeeklyIncomeFromSelfEmployment = D_Record.getClaimantsNetWeeklyIncomeFromSelfEmployment();
            if (ClaimantsNetWeeklyIncomeFromSelfEmployment > 0) {
                CouncilCTBTotalCount_SelfEmployedClaimants++;
            }
            String ClaimantsStudentIndicator;
            ClaimantsStudentIndicator = D_Record.getClaimantsStudentIndicator();
            if (ClaimantsStudentIndicator != null) {
                if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                    CouncilCTBTotalCount_StudentsClaimants++;
                }
            }
            String LHARegulationsApplied;
            LHARegulationsApplied = D_Record.getLHARegulationsApplied();
            if (LHARegulationsApplied != null) {
                if (LHARegulationsApplied.equalsIgnoreCase("1")) {
                    CouncilCTBTotalCount_LHACases++;
                }
            }
            if (WeeklyHousingBenefitEntitlement > 0) {
                CouncilCTBTotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
                CouncilCTBTotalCount_WeeklyHBEntitlementNonZero++;
            } else {
                CouncilCTBTotalWeeklyHBEntitlementZeroCount++;
            }
            WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
            if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
                CouncilCTBTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
                CouncilCTBTotalCount_WeeklyCTBEntitlementNonZero++;
            } else {
                CouncilCTBTotalWeeklyCTBEntitlementZeroCount++;
            }
            WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
            if (WeeklyEligibleRentAmount > 0) {
                CouncilCTBTotalWeeklyEligibleRentAmount += WeeklyEligibleRentAmount;
                CouncilCTBTotalWeeklyEligibleRentAmountNonZeroCount++;
            } else {
                CouncilCTBTotalWeeklyEligibleRentAmountZeroCount++;
            }
            WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
            if (WeeklyEligibleCouncilTaxAmount > 0) {
                CouncilCTBTotalWeeklyEligibleCouncilTaxAmount += WeeklyEligibleCouncilTaxAmount;
                CouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero++;
            } else {
                CouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero++;
            }
            ContractualRentAmount = D_Record.getContractualRentAmount();
            if (ContractualRentAmount > 0) {
                CouncilCTBTotalContractualRentAmount += ContractualRentAmount;
                CouncilCTBTotalContractualRentAmountNonZeroCount++;
            } else {
                CouncilCTBTotalContractualRentAmountZeroCount++;
            }
            WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
            if (WeeklyAdditionalDiscretionaryPayment > 0) {
                CouncilCTBTotalWeeklyAdditionalDiscretionaryPayment += WeeklyAdditionalDiscretionaryPayment;
                CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount++;
            } else {
                CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount++;
            }
            WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
            if (WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability > 0) {
                CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability += WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
                CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount++;
            } else {
                CouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount++;
            }
            doSingleTimeCTBCount(
                    ClaimantsEthnicGroup0,
                    postcode,
                    yM30v);
        }
        CouncilAllCount1 = CouncilHBCount1 + CouncilCTBCount1;
        // RSL
        ClaimantsEthnicGroup0 = D_Record.getClaimantsEthnicGroup();
        // All unfiltered counts
        TT = D_Record.getTenancyType();
        postcode = D_Record.getClaimantsPostcode();
        RSLTotalCount_TTClaimant1[TT]++;
        // Disability
        DisabilityPremiumAwarded = D_Record.getDisabilityPremiumAwarded();
        if (DisabilityPremiumAwarded == 1) {
            RSLTotalCount_DisabilityPremiumAwardByTT[TT]++;
        }
        SevereDisabilityPremiumAwarded = D_Record.getSevereDisabilityPremiumAwarded();
        if (SevereDisabilityPremiumAwarded == 1) {
            RSLTotalCount_SevereDisabilityPremiumAwardByTT[TT]++;
        }
        DisabledChildPremiumAwarded = D_Record.getDisabledChildPremiumAwarded();
        if (DisabledChildPremiumAwarded == 1) {
            RSLTotalCount_DisabledChildPremiumAwardByTT[TT]++;
        }
        EnhancedDisabilityPremiumAwarded = D_Record.getEnhancedDisabilityPremiumAwarded();
        if (EnhancedDisabilityPremiumAwarded == 1) {
            RSLTotalCount_EnhancedDisabilityPremiumAwardByTT[TT]++;
        }
        // General Household Disability Flag
        if (DisabilityPremiumAwarded == 1
                || SevereDisabilityPremiumAwarded == 1
                || DisabledChildPremiumAwarded == 1
                || EnhancedDisabilityPremiumAwarded == 1) {
            RSLTotalCount_DisabilityAwardByTT[TT]++;
        }
        // Passported Standard Indicator
        PSI = D_Record.getPassportedStandardIndicator();
        RSLAllTotalCount_PSI[PSI]++;
        RSLAllTotalCount_PSIByTT[PSI][TT]++;
        // Household size
        HouseholdSize = DW_SHBE_Handler.getHouseholdSize(D_Record);
        RSLAllTotalHouseholdSize += HouseholdSize;
        // Entitlements
        WeeklyHousingBenefitEntitlement = D_Record.getWeeklyHousingBenefitEntitlement();
        if (WeeklyHousingBenefitEntitlement > 0) {
            RSLAllTotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
            RSLAllTotalWeeklyHBEntitlementNonZeroCount++;
        } else {
            RSLAllTotalWeeklyHBEntitlementZeroCount++;
        }
        WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
        if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
            RSLAllTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
            RSLAllTotalCount_WeeklyCTBEntitlementNonZero++;
        } else {
            RSLAllTotalWeeklyCTBEntitlementZeroCount++;
        }
        // Eligible Amounts
        WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
        if (WeeklyEligibleRentAmount > 0) {
            RSLAllTotalWeeklyEligibleRentAmount += WeeklyEligibleRentAmount;
            RSLAllTotalWeeklyEligibleRentAmountNonZeroCount++;
        } else {
            RSLAllTotalWeeklyEligibleRentAmountZeroCount++;
        }
        WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
        if (WeeklyEligibleCouncilTaxAmount > 0) {
            RSLAllTotalWeeklyEligibleCouncilTaxAmount += WeeklyEligibleCouncilTaxAmount;
            RSLAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero++;
        } else {
            RSLAllTotalCount_WeeklyEligibleCouncilTaxAmountZero++;
        }
        ContractualRentAmount = D_Record.getContractualRentAmount();
        if (ContractualRentAmount > 0) {
            RSLAllTotalContractualRentAmount += ContractualRentAmount;
            RSLAllTotalContractualRentAmountNonZeroCount++;
        } else {
            RSLAllTotalContractualRentAmountZeroCount++;
        }
        // Additional Payments
         WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
        if (WeeklyAdditionalDiscretionaryPayment > 0) {
            RSLAllTotalWeeklyAdditionalDiscretionaryPayment += WeeklyAdditionalDiscretionaryPayment;
            RSLAllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount++;
        } else {
            RSLAllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount++;
        }
        WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
        if (WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability > 0) {
            RSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability += WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
            RSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount++;
        } else {
            RSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount++;
        }
        // HBClaim only counts
        if (DW_SHBE_Handler.isHBClaim(D_Record)) {
            RSLHBTotalCount_PSI[PSI]++;
            RSLHBTotalCount_PSIByTT[PSI][TT]++;
            //if (HBRef.equalsIgnoreCase(CTBRef)) {
            RSLHBTotalHouseholdSize += HouseholdSize;
            int ClaimantsNetWeeklyIncomeFromEmployment;
            ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
            if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
                RSLHBTotalCount_EmployedClaimants++;
            }
            int ClaimantsNetWeeklyIncomeFromSelfEmployment;
            ClaimantsNetWeeklyIncomeFromSelfEmployment = D_Record.getClaimantsNetWeeklyIncomeFromSelfEmployment();
            if (ClaimantsNetWeeklyIncomeFromSelfEmployment > 0) {
                RSLHBTotalCount_SelfEmployedClaimants++;
            }
            String ClaimantsStudentIndicator;
            ClaimantsStudentIndicator = D_Record.getClaimantsStudentIndicator();
            if (ClaimantsStudentIndicator != null) {
                if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                    RSLHBTotalCount_StudentsClaimants++;
                }
            }
            String LHARegulationsApplied;
            LHARegulationsApplied = D_Record.getLHARegulationsApplied();
            if (LHARegulationsApplied != null) {
                if (LHARegulationsApplied.equalsIgnoreCase("1")
                        || LHARegulationsApplied.equalsIgnoreCase("Yes")) {
                    RSLHBTotalCount_LHACases++;
                }
            }
            if (WeeklyHousingBenefitEntitlement > 0) {
                RSLHBTotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
                RSLHBTotalCount_WeeklyHBEntitlementNonZero++;
            } else {
                RSLHBTotalCount_WeeklyHBEntitlementZero++;
            }
            WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
            if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
                RSLHBTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
                RSLHBTotalCount_WeeklyCTBEntitlementNonZero++;
            } else {
                RSLHBTotalWeeklyCTBEntitlementZeroCount++;
            }
            WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
            if (WeeklyEligibleRentAmount > 0) {
                RSLHBTotalWeeklyEligibleRentAmount += WeeklyEligibleRentAmount;
                RSLHBTotalWeeklyEligibleRentAmountNonZeroCount++;
            } else {
                RSLHBTotalWeeklyEligibleRentAmountZeroCount++;
            }
            WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
            if (WeeklyEligibleCouncilTaxAmount > 0) {
                RSLHBTotalWeeklyEligibleCouncilTaxAmount += WeeklyEligibleCouncilTaxAmount;
                RSLHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero++;
            } else {
                RSLHBTotalCount_WeeklyEligibleCouncilTaxAmountZero++;
            }
            ContractualRentAmount = D_Record.getContractualRentAmount();
            if (ContractualRentAmount > 0) {
                RSLHBTotalContractualRentAmount += ContractualRentAmount;
                RSLHBTotalContractualRentAmountNonZeroCount++;
            } else {
                RSLHBTotalContractualRentAmountZeroCount++;
            }
            WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
            if (WeeklyAdditionalDiscretionaryPayment > 0) {
                RSLHBTotalWeeklyAdditionalDiscretionaryPayment += WeeklyAdditionalDiscretionaryPayment;
                RSLHBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount++;
            } else {
                RSLHBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount++;
            }
            WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
            if (WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability > 0) {
                RSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability += WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
                RSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount++;
            } else {
                RSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount++;
            }
            doSingleTimeHBCount(
                    ClaimantsEthnicGroup0,
                    postcode,
                    yM30v);
        }
        // CTB Claim only counts
        if (DW_SHBE_Handler.isCTBOnlyClaim(D_Record)) {
            RSLCTBTotalCount_PSI[PSI]++;
            RSLCTBTotalCount_PSIByTT[PSI][TT]++;
            RSLCTBTotalHouseholdSize += HouseholdSize;
            int ClaimantsNetWeeklyIncomeFromEmployment;
            ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
            if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
                RSLCTBTotalCount_EmployedClaimants++;
            }
            int ClaimantsNetWeeklyIncomeFromSelfEmployment;
            ClaimantsNetWeeklyIncomeFromSelfEmployment = D_Record.getClaimantsNetWeeklyIncomeFromSelfEmployment();
            if (ClaimantsNetWeeklyIncomeFromSelfEmployment > 0) {
                RSLCTBTotalCount_SelfEmployedClaimants++;
            }
            String ClaimantsStudentIndicator;
            ClaimantsStudentIndicator = D_Record.getClaimantsStudentIndicator();
            if (ClaimantsStudentIndicator != null) {
                if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                    RSLCTBTotalCount_StudentsClaimants++;
                }
            }
            String LHARegulationsApplied;
            LHARegulationsApplied = D_Record.getLHARegulationsApplied();
            if (LHARegulationsApplied != null) {
                if (LHARegulationsApplied.equalsIgnoreCase("1")) {
                    RSLCTBTotalCount_LHACases++;
                }
            }
            if (WeeklyHousingBenefitEntitlement > 0) {
                RSLCTBTotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
                RSLCTBTotalCount_WeeklyHBEntitlementNonZero++;
            } else {
                RSLCTBTotalWeeklyHBEntitlementZeroCount++;
            }
            WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
            if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
                RSLCTBTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
                RSLCTBTotalCount_WeeklyCTBEntitlementNonZero++;
            } else {
                RSLCTBTotalWeeklyCTBEntitlementZeroCount++;
            }
            WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
            if (WeeklyEligibleRentAmount > 0) {
                RSLCTBTotalWeeklyEligibleRentAmount += WeeklyEligibleRentAmount;
                RSLCTBTotalWeeklyEligibleRentAmountNonZeroCount++;
            } else {
                RSLCTBTotalWeeklyEligibleRentAmountZeroCount++;
            }
            WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
            if (WeeklyEligibleCouncilTaxAmount > 0) {
                RSLCTBTotalWeeklyEligibleCouncilTaxAmount += WeeklyEligibleCouncilTaxAmount;
                RSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero++;
            } else {
                RSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero++;
            }
            ContractualRentAmount = D_Record.getContractualRentAmount();
            if (ContractualRentAmount > 0) {
                RSLCTBTotalContractualRentAmount += ContractualRentAmount;
                RSLCTBTotalContractualRentAmountNonZeroCount++;
            } else {
                RSLCTBTotalContractualRentAmountZeroCount++;
            }
            WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
            if (WeeklyAdditionalDiscretionaryPayment > 0) {
                RSLCTBTotalWeeklyAdditionalDiscretionaryPayment += WeeklyAdditionalDiscretionaryPayment;
                RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount++;
            } else {
                RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount++;
            }
            WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
            if (WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability > 0) {
                RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability += WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
                RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount++;
            } else {
                RSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount++;
            }
            doSingleTimeCTBCount(
                    ClaimantsEthnicGroup0,
                    postcode,
                    yM30v);
        }
        RSLAllCount1 = RSLHBCount1 + RSLCTBCount1;
    }

    protected void doSingleTimeRentArrearsCount(DW_UOReport_Record UORec) {
        Double totalRA;
        totalRA = UORec.getTotalRentArrears();
        if (totalRA != null) {
            CouncilTotal_RentArrears += totalRA;
            CouncilTotalCount_RentArrears += 1.0d;
            if (totalRA > 0.0d) {
                CouncilTotalCount_RentArrearsNonZero++;
            } else {
                CouncilTotalCount_RentArrearsZero++;
            }
        }
    }

    @Override
    protected void doCompare2TimesHBCount(
            Integer tenancyType0,
            String postcode0,
            boolean isValidPostcode0,
            Integer tenancyType1,
            String postcode1,
            boolean isValidPostcode1) {
        super.doCompare2TimesHBCount(tenancyType0, postcode0, isValidPostcode0, tenancyType1, postcode1, isValidPostcode1);
        // Council
        if (isValidPostcode0) {
            if (isValidPostcode1) {
                CouncilHBTotalCount_Postcode0ValidPostcode1Valid++;
                if (postcode0.equalsIgnoreCase(postcode1)) {
                    CouncilHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged++;
                } else {
                    CouncilHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged++;
                }
            } else {
                CouncilHBTotalCount_Postcode0ValidPostcode1NotValid++;
            }
        } else {
            if (isValidPostcode1) {
                CouncilHBTotalCount_Postcode0NotValidPostcode1Valid++;
            } else {
                CouncilHBTotalCount_Postcode0NotValidPostcode1NotValid++;
                if (postcode0 == null) {
                    CouncilHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                } else {
                    if (postcode1 == null) {
                        CouncilHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                    } else {
                        if (postcode0.equalsIgnoreCase(postcode1)) {
                            CouncilHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                        } else {
                            CouncilHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged++;
                        }
                    }
                }
            }
        }
        if (tenancyType0.compareTo(tenancyType1) != 0) {
            if (tenancyType0 == DW_SHBE_TenancyType_Handler.iMinus999
                    || tenancyType1 == DW_SHBE_TenancyType_Handler.iMinus999) {
                CouncilHBTotalCount_TTChangeClaimant++;
            }
            if (tenancyType0 == DW_SHBE_TenancyType_Handler.iMinus999) {
                if (tenancyType1 == 1 || tenancyType1 == 4) {
                    CouncilTotalCount_Minus999TTToSocialTTs++;
                }
                if (tenancyType1 == 3 || tenancyType1 == 6) {
                    CouncilTotalCount_Minus999TTToPrivateDeregulatedTTs++;
                }
                if (tenancyType1 == 1
                        || tenancyType1 == 2
                        || tenancyType1 == 3
                        || tenancyType1 == 4
                        || tenancyType1 == 6
                        || tenancyType1 == 8
                        || tenancyType1 == 9) {
                    CouncilTotalCount_HBTTsToMinus999TT++;
                }
            }
            if (tenancyType1 == DW_SHBE_TenancyType_Handler.iMinus999) {
                if (tenancyType0 == 1 || tenancyType0 == 4) {
                    CouncilTotalCount_SocialTTsToMinus999TT++;
                }
                if (tenancyType0 == 3 || tenancyType0 == 6) {
                    CouncilTotalCount_PrivateDeregulatedTTsToMinus999TT++;
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
                CouncilTotalCount_CTBTTsToHBTTs++;
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
                CouncilTotalCount_HBTTsToHBTTs++;
            }
            if (tenancyType0 == 1 || tenancyType0 == 4) {
                if (tenancyType1 == 3 || tenancyType0 == 6) {
                    CouncilTotalCount_SocialTTsToPrivateDeregulatedTTs++;
                    if (tenancyType0 == 1) {
                        CouncilTotalCount_TT1ToPrivateDeregulatedTTs++;
                    }
                    if (tenancyType0 == 4) {
                        CouncilTotalCount_TT4ToPrivateDeregulatedTTs++;
                    }
                }
            }
            if (tenancyType0 == 3 || tenancyType0 == 6) {
                if (tenancyType1 == 1 || tenancyType1 == 4) {
                    CouncilTotalCount_PrivateDeregulatedTTsToSocialTTs++;
                    if (tenancyType1 == 1) {
                        CouncilTotalCount_PrivateDeregulatedTTsToTT1++;
                    }
                    if (tenancyType1 == 4) {
                        CouncilTotalCount_PrivateDeregulatedTTsToTT4++;
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
                    CouncilTotalCount_PostcodeChangeWithinSocialTTs++;
                    if (tenancyType0 == 1 && tenancyType1 == 1) {
                        CouncilTotalCount_PostcodeChangeWithinTT1++;
                    }
                    if (tenancyType0 == 4 && tenancyType1 == 4) {
                        CouncilTotalCount_PostcodeChangeWithinTT4++;
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
                    CouncilTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs++;
                }
            }
        }
        if ((tenancyType0 == 5 || tenancyType0 == 7)
                && (tenancyType1 == 3 || tenancyType1 == 6)) {
            CouncilTotalCount_CTBTTsToPrivateDeregulatedTTs++;
        }
        if (tenancyType0 == 1 && tenancyType1 == 4) {
            CouncilTotalCount_TT1ToTT4++;
        }
        if (tenancyType0 == 4 && tenancyType1 == 1) {
            CouncilTotalCount_TT4ToTT1++;
        }
        if (tenancyType0 == 5 || tenancyType0 == 7) {
            if (tenancyType1 == 1 || tenancyType1 == 4) {
                CouncilTotalCount_CTBTTsToSocialTTs++;
                if (tenancyType1 == 1) {
                    CouncilTotalCount_CTBTTsToTT1++;
                }
                if (tenancyType1 == 4) {
                    CouncilTotalCount_CTBTTsToTT4++;
                }
            }
        }
        // RSL
        if (isValidPostcode0) {
            if (isValidPostcode1) {
                RSLHBTotalCount_Postcode0ValidPostcode1Valid++;
                if (postcode0.equalsIgnoreCase(postcode1)) {
                    RSLHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged++;
                } else {
                    RSLHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged++;
                }
            } else {
                RSLHBTotalCount_Postcode0ValidPostcode1NotValid++;
            }
        } else {
            if (isValidPostcode1) {
                RSLHBTotalCount_Postcode0NotValidPostcode1Valid++;
            } else {
                RSLHBTotalCount_Postcode0NotValidPostcode1NotValid++;
                if (postcode0 == null) {
                    RSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                } else {
                    if (postcode1 == null) {
                        RSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                    } else {
                        if (postcode0.equalsIgnoreCase(postcode1)) {
                            RSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                        } else {
                            RSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged++;
                        }
                    }
                }
            }
        }
        if (tenancyType0.compareTo(tenancyType1) != 0) {
            if (tenancyType0 == DW_SHBE_TenancyType_Handler.iMinus999
                    || tenancyType1 == DW_SHBE_TenancyType_Handler.iMinus999) {
                RSLHBTotalCount_TTChangeClaimant++;
            }
            if (tenancyType0 == DW_SHBE_TenancyType_Handler.iMinus999) {
                if (tenancyType1 == 1 || tenancyType1 == 4) {
                    RSLTotalCount_Minus999TTToSocialTTs++;
                }
                if (tenancyType1 == 3 || tenancyType1 == 6) {
                    RSLTotalCount_Minus999TTToPrivateDeregulatedTTs++;
                }
                if (tenancyType1 == 1
                        || tenancyType1 == 2
                        || tenancyType1 == 3
                        || tenancyType1 == 4
                        || tenancyType1 == 6
                        || tenancyType1 == 8
                        || tenancyType1 == 9) {
                    RSLTotalCount_HBTTsToMinus999TT++;
                }
            }
            if (tenancyType1 == DW_SHBE_TenancyType_Handler.iMinus999) {
                if (tenancyType0 == 1 || tenancyType0 == 4) {
                    RSLTotalCount_SocialTTsToMinus999TT++;
                }
                if (tenancyType0 == 3 || tenancyType0 == 6) {
                    RSLTotalCount_PrivateDeregulatedTTsToMinus999TT++;
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
                RSLTotalCount_CTBTTsToHBTTs++;
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
                RSLTotalCount_HBTTsToHBTTs++;
            }
            if (tenancyType0 == 1 || tenancyType0 == 4) {
                if (tenancyType1 == 3 || tenancyType0 == 6) {
                    RSLTotalCount_SocialTTsToPrivateDeregulatedTTs++;
                    if (tenancyType0 == 1) {
                        RSLTotalCount_TT1ToPrivateDeregulatedTTs++;
                    }
                    if (tenancyType0 == 4) {
                        RSLTotalCount_TT4ToPrivateDeregulatedTTs++;
                    }
                }
            }
            if (tenancyType0 == 3 || tenancyType0 == 6) {
                if (tenancyType1 == 1 || tenancyType1 == 4) {
                    RSLTotalCount_PrivateDeregulatedTTsToSocialTTs++;
                    if (tenancyType1 == 1) {
                        RSLTotalCount_PrivateDeregulatedTTsToTT1++;
                    }
                    if (tenancyType1 == 4) {
                        RSLTotalCount_PrivateDeregulatedTTsToTT4++;
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
                    RSLTotalCount_PostcodeChangeWithinSocialTTs++;
                    if (tenancyType0 == 1 && tenancyType1 == 1) {
                        RSLTotalCount_PostcodeChangeWithinTT1++;
                    }
                    if (tenancyType0 == 4 && tenancyType1 == 4) {
                        RSLTotalCount_PostcodeChangeWithinTT4++;
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
                    RSLTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs++;
                }
            }
        }
        if ((tenancyType0 == 5 || tenancyType0 == 7)
                && (tenancyType1 == 3 || tenancyType1 == 6)) {
            RSLTotalCount_CTBTTsToPrivateDeregulatedTTs++;
        }
        if (tenancyType0 == 1 && tenancyType1 == 4) {
            RSLTotalCount_TT1ToTT4++;
        }
        if (tenancyType0 == 4 && tenancyType1 == 1) {
            RSLTotalCount_TT4ToTT1++;
        }
        if (tenancyType0 == 5 || tenancyType0 == 7) {
            if (tenancyType1 == 1 || tenancyType1 == 4) {
                RSLTotalCount_CTBTTsToSocialTTs++;
                if (tenancyType1 == 1) {
                    RSLTotalCount_CTBTTsToTT1++;
                }
                if (tenancyType1 == 4) {
                    RSLTotalCount_CTBTTsToTT4++;
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
    @Override
    protected void doSingleTimeHBCount(
            int tEG,
            String tP,
            String yM3v) {
        //super.doSingleTimeHBCount(tEG, tP, yM3v);
        // RSL
        RSLHBCount1++;
        RSLHBEthnicGroupCount[tEG]++;
        if (tP != null) {
            boolean isValidPostcodeFormat;
            isValidPostcodeFormat = Generic_UKPostcode_Handler.isValidPostcodeForm(tP);
            boolean isValidPostcode;
            isValidPostcode = DW_Postcode_Handler.isValidPostcode(yM3v, tP);
            if (isValidPostcodeFormat) {
                RSLHBTotalCount_PostcodeValidFormat++;
            }
            if (isValidPostcode) {
                RSLHBTotalCount_PostcodeValid++;
            }
        }
        // Council
        CouncilHBCount1++;
        CouncilHBEthnicGroupCount[tEG]++;
        if (tP != null) {
            boolean isValidPostcodeFormat;
            isValidPostcodeFormat = Generic_UKPostcode_Handler.isValidPostcodeForm(tP);
            boolean isValidPostcode;
            isValidPostcode = DW_Postcode_Handler.isValidPostcode(yM3v, tP);
            if (isValidPostcodeFormat) {
                CouncilHBTotalCount_PostcodeValidFormat++;
            }
            if (isValidPostcode) {
                CouncilHBTotalCount_PostcodeValid++;
            }
        }
    }

    @Override
    protected void doCompare2TimesCTBCount(
            Integer tenancyType0,
            String postcode0,
            boolean isValidPostcode0,
            Integer tenancyType1,
            String postcode1,
            boolean isValidPostcode1) {
        //super.doCompare2TimesCTBCount(tenancyType0, postcode0, isValidPostcode0, tenancyType1, postcode1, isValidPostcode1);
        // Council
        if (isValidPostcode0) {
            if (isValidPostcode1) {
                CouncilCTBTotalCount_Postcode0ValidPostcode1Valid++;
                if (postcode0.equalsIgnoreCase(postcode1)) {
                    CouncilCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged++;
                } else {
                    CouncilCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged++;
                }
            } else {
                CouncilCTBTotalCount_Postcode0ValidPostcode1NotValid++;
            }
        } else {
            if (isValidPostcode1) {
                CouncilCTBTotalCount_Postcode0NotValidPostcode1Valid++;
            } else {
                CouncilCTBTotalCount_Postcode0NotValidPostcode1NotValid++;
                if (postcode0 == null) {
                    CouncilCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                } else {
                    if (postcode1 == null) {
                        CouncilCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                    } else {
                        if (postcode0.equalsIgnoreCase(postcode1)) {
                            CouncilCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                        } else {
                            CouncilCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged++;
                        }
                    }
                }
            }
        }
        if (tenancyType0.compareTo(tenancyType1) != 0) {
            if (tenancyType0 == DW_SHBE_TenancyType_Handler.iMinus999
                    || tenancyType1 == DW_SHBE_TenancyType_Handler.iMinus999) {
                CouncilCTBTotalCount_TTChangeClaimant++;
            }
            if (tenancyType1 == DW_SHBE_TenancyType_Handler.iMinus999) {
                if (tenancyType0 == 5 || tenancyType0 == 7) {
                    CouncilTotalCount_CTBTTsToMinus999TT++;
                }
            }
            if (tenancyType0 == DW_SHBE_TenancyType_Handler.iMinus999) {
                CouncilTotalCount_Minus999TTToCTBTTs++;
            }
            if ((tenancyType0 == 1
                    || tenancyType0 == 2
                    || tenancyType0 == 3
                    || tenancyType0 == 4
                    || tenancyType0 == 6
                    || tenancyType0 == 8
                    || tenancyType0 == 9)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                CouncilTotalCount_HBTTsToCTBTTs++;
            }
            if ((tenancyType0 == 1 || tenancyType0 == 4)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                CouncilTotalCount_SocialTTsToCTBTTs++;
            }
            if ((tenancyType0 == 1)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                CouncilTotalCount_TT1ToCTBTTs++;
            }
            if ((tenancyType0 == 4)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                CouncilTotalCount_TT4ToCTBTTs++;
            }
            if ((tenancyType0 == 3 || tenancyType0 == 6)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                CouncilTotalCount_PrivateDeregulatedTTsToCTBTTs++;
            }
        }
        // RSL
        if (isValidPostcode0) {
            if (isValidPostcode1) {
                RSLCTBTotalCount_Postcode0ValidPostcode1Valid++;
                if (postcode0.equalsIgnoreCase(postcode1)) {
                    RSLCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged++;
                } else {
                    RSLCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged++;
                }
            } else {
                RSLCTBTotalCount_Postcode0ValidPostcode1NotValid++;
            }
        } else {
            if (isValidPostcode1) {
                RSLCTBTotalCount_Postcode0NotValidPostcode1Valid++;
            } else {
                RSLCTBTotalCount_Postcode0NotValidPostcode1NotValid++;
                if (postcode0 == null) {
                    RSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                } else {
                    if (postcode1 == null) {
                        RSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                    } else {
                        if (postcode0.equalsIgnoreCase(postcode1)) {
                            RSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                        } else {
                            RSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged++;
                        }
                    }
                }
            }
        }
        if (tenancyType0.compareTo(tenancyType1) != 0) {
            if (tenancyType0 == DW_SHBE_TenancyType_Handler.iMinus999
                    || tenancyType1 == DW_SHBE_TenancyType_Handler.iMinus999) {
                RSLCTBTotalCount_TTChangeClaimant++;
            }
            if (tenancyType1 == DW_SHBE_TenancyType_Handler.iMinus999) {
                if (tenancyType0 == 5 || tenancyType0 == 7) {
                    RSLTotalCount_CTBTTsToMinus999TT++;
                }
            }
            if (tenancyType0 == DW_SHBE_TenancyType_Handler.iMinus999) {
                RSLTotalCount_Minus999TTToCTBTTs++;
            }
            if ((tenancyType0 == 1
                    || tenancyType0 == 2
                    || tenancyType0 == 3
                    || tenancyType0 == 4
                    || tenancyType0 == 6
                    || tenancyType0 == 8
                    || tenancyType0 == 9)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                RSLTotalCount_HBTTsToCTBTTs++;
            }
            if ((tenancyType0 == 1 || tenancyType0 == 4)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                RSLTotalCount_SocialTTsToCTBTTs++;
            }
            if ((tenancyType0 == 1)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                RSLTotalCount_TT1ToCTBTTs++;
            }
            if ((tenancyType0 == 4)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                RSLTotalCount_TT4ToCTBTTs++;
            }
            if ((tenancyType0 == 3 || tenancyType0 == 6)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                RSLTotalCount_PrivateDeregulatedTTsToCTBTTs++;
            }
        }
    }

    /**
     *
     * @param tEG The Ethnic Group
     * @param tP The Postcode
     * @param yM3v They yM3 for postcode lookup validity
     */
    @Override
    protected void doSingleTimeCTBCount(
            int tEG,
            String tP,
            String yM3v) {
        // Council
        CouncilCTBCount1++;
        CouncilCTBEthnicGroupCount[tEG]++;
        if (tP != null) {
            boolean isValidPostcodeFormat;
            isValidPostcodeFormat = Generic_UKPostcode_Handler.isValidPostcodeForm(tP);
            boolean isValidPostcode;
            isValidPostcode = DW_Postcode_Handler.isValidPostcode(yM3v, tP);
            if (isValidPostcodeFormat) {
                CouncilCTBTotalCount_PostcodeValidFormat++;
            }
            if (isValidPostcode) {
                CouncilCTBTotalCount_PostcodeValid++;
            }
        }
        // RSL
        RSLCTBCount1++;
        RSLCTBEthnicGroupCount[tEG]++;
        if (tP != null) {
            boolean isValidPostcodeFormat;
            isValidPostcodeFormat = Generic_UKPostcode_Handler.isValidPostcodeForm(tP);
            boolean isValidPostcode;
            isValidPostcode = DW_Postcode_Handler.isValidPostcode(yM3v, tP);
            if (isValidPostcodeFormat) {
                RSLCTBTotalCount_PostcodeValidFormat++;
            }
            if (isValidPostcode) {
                RSLCTBTotalCount_PostcodeValid++;
            }
        }
    }

    not sure what to do about this...;

    @Override
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
        //super.doCompare3TimesCounts(summary, nTT, SHBEFilenames, include, yM31, tClaimantIDPostcodes, tClaimantIDPostcodes1, tClaimantIDTTs, tClaimantIDTTs1, tClaimantIDPostcodeTTs, tClaimantIDPostcodeTTs1);
        Integer[] counts;
        counts = getCountsIDPostcode(
                SHBEFilenames,
                include,
                yM31,
                tClaimantIDPostcodes,
                tClaimantIDPostcodes1);
        summary.put(sRSLSamePostcodeIII,
                "" + counts[0]);
        summary.put(sRSLSamePostcodeIOI,
                "" + counts[1]);
        summary.put(sRSLSamePostcodeOIO,
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
                summary.put(
                        sSameTenancyIIITT[TT],
                        null);
                summary.put(
                        sSameTenancyIOITT[TT],
                        null);
                summary.put(
                        sSameTenancyOIOTT[TT],
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
                summary.put(
                        sSameTenancyIIITT[TT],
                        "" + IIITTCounts[TT]);
                summary.put(
                        sSameTenancyIOITT[TT],
                        "" + IOITTCounts[TT]);
                summary.put(
                        sSameTenancyOIOTT[TT],
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
                summary.put(
                        sSameTenancyAndPostcodeIIITT[TT],
                        null);
                summary.put(
                        sSameTenancyAndPostcodeIOITT[TT],
                        null);
                summary.put(
                        sSameTenancyAndPostcodeOIOTT[TT],
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
                summary.put(
                        sSameTenancyAndPostcodeIIITT[TT],
                        "" + IIITTCounts[TT]);
                summary.put(
                        sSameTenancyAndPostcodeIOITT[TT],
                        "" + IOITTCounts[TT]);
                summary.put(
                        sSameTenancyAndPostcodeOIOTT[TT],
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
            summary.put(sSameTenancyIII,
                    null);
            summary.put(sSameTenancyIOI,
                    null);
            summary.put(sSameTenancyOIO,
                    null);
            for (int TT = 1; TT < nTT; TT++) {
                summary.put(
                        sSameTenancyIIITT[TT],
                        null);
                summary.put(
                        sSameTenancyIOITT[TT],
                        null);
                summary.put(
                        sSameTenancyOIOTT[TT],
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
                summary.put(
                        sSameTenancyIIITT[TT],
                        "" + IIITTCounts[TT]);
                summary.put(
                        sSameTenancyIOITT[TT],
                        "" + IOITTCounts[TT]);
                summary.put(
                        sSameTenancyOIOTT[TT],
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
                summary.put(
                        sSameTenancyAndPostcodeIIITT[TT],
                        null);
                summary.put(
                        sSameTenancyAndPostcodeIOITT[TT],
                        null);
                summary.put(
                        sSameTenancyAndPostcodeOIOTT[TT],
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
                summary.put(
                        sSameTenancyAndPostcodeIIITT[TT],
                        "" + IIITTCounts[TT]);
                summary.put(
                        sSameTenancyAndPostcodeIOITT[TT],
                        "" + IOITTCounts[TT]);
                summary.put(
                        sSameTenancyAndPostcodeOIOTT[TT],
                        "" + OIOTTCounts[TT]);
            }
        }
    }

    /**
     *
     * @param summaryTableAll
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
        // Initialise result part 1
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
        // Initialise result part 2
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
        // The following could be returned/passed in  to save time recreating 
        // them for other includes.
        TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes;
        tClaimantIDPostcodes = new TreeMap<String, HashSet<ID_PostcodeID>>();
        TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTTs;
        tClaimantIDTTs = new TreeMap<String, HashSet<ID_TenancyType>>();
        TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTTs;
        tClaimantIDPostcodeTTs = new TreeMap<String, HashSet<ID_TenancyType_PostcodeID>>();

        yM31v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);

        // Summarise first data
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

        // Declare vars for referring to previous data needed for comparisons
        String filename0;
        String yM30;
        String yM30v;
        DW_SHBE_Collection tSHBEData0;
        DW_UnderOccupiedReport_Set councilUnderOccupiedSet0;
        DW_UnderOccupiedReport_Set RSLUnderOccupiedSet0;

        if (includeIte.hasNext()) {
            i = includeIte.next();

            filename0 = filename1;
            yM30 = yM31;
            yM30v = yM31v;
            tSHBEData0 = tSHBEData1;
            councilUnderOccupiedSet0 = councilUnderOccupiedSet1;
            RSLUnderOccupiedSet0 = RSLUnderOccupiedSet1;

            filename1 = SHBEFilenames[i];
            yM31 = DW_SHBE_Handler.getYM3(filename1);
            yM31v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
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

            String filename00;
            String yM300;

            while (includeIte.hasNext()) {
                i = includeIte.next();

                filename00 = filename0;
                yM300 = yM30;

                filename0 = filename1;
                yM30 = yM31;
                yM30v = yM31v;
                tSHBEData0 = tSHBEData1;
                councilUnderOccupiedSet0 = councilUnderOccupiedSet1;
                RSLUnderOccupiedSet0 = RSLUnderOccupiedSet1;

                filename1 = SHBEFilenames[i];
                yM31 = DW_SHBE_Handler.getYM3(filename1);
                yM31v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
                councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(yM31);
                RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(yM31);
                System.out.println("Load " + filename1);
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
                        yM300,
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
                        result,
                        tClaimantIDPostcodes,
                        tClaimantIDTTs,
                        tClaimantIDPostcodeTTs,
                        tDW_PersonIDToIDLookup,
                        tPostcodeToPostcodeIDLookup);

                AllCount0 = null;
                HBCount0 = null;
                CTBCount0 = null;
                // Not used at present. incomeAndRentSummary0 = incomeAndRentSummary1;

            }
        }
        return result;
    }

    protected void doPartSummaryCompare3Times(
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
            String yM300,
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
     * @param paymentType
     * @param nTT
     * @param nEG
     * @param nPSI
     * @param councilFilenames
     * @param RSLFilenames
     * @param councilUnderOccupiedSet1
     * @param RSLUnderOccupiedSet1
     * @param councilUnderOccupiedSet0
     * @param RSLUnderOccupiedSet0
     * @param summaries
     * @param tClaimantIDPostcodes
     * @param tClaimantIDTTs
     * @param tClaimantIDPostcodeTTs
     * @param DW_PersonIDToIDLookup
     * @param PostcodeToPostcodeIDLookup
     */
    protected void doPartSummaryCompare2Times(
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
        TreeMap<String, DW_SHBE_Record> tDRecords0;
        tDRecords0 = tSHBEData0.getRecords();

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
                yM30v,
                yM31v);
        // Loop over RSL
        doCompare2TimesLoopOverSet(
                RSLUnderOccupiedSet0Map,
                RSLUnderOccupiedSet1Map,
                tDRecords0,
                tDRecords1,
                yM30v,
                yM31v);

        String key;
        key = DW_SHBE_Handler.getYearMonthNumber(filename1);
        HashMap<String, String> summary;
        summary = summaries.get(key);

        addToSummaryCompare2Times(nTT, nEG, nPSI, summary);

        // All
        summary.put(sSHBEFilename0, filename0);
        summary.put(sSHBEFilename1, filename1);
        summary.put(sCouncilFilename0, councilFilenames.get(yM30));
        summary.put(sRSLFilename0, RSLFilenames.get(yM30));
        summary.put(sCouncilCount0, Integer.toString(CouncilCount0));
        summary.put(sCouncilLinkedRecordCount0, Integer.toString(CouncilLinkedRecordCount0));
        summary.put(sRSLCount0, Integer.toString(RSLCount0));
        summary.put(sRSLLinkedRecordCount0, Integer.toString(RSLLinkedRecordCount0));
        summary.put(sAllUOCount0, Integer.toString(AllUOCount0));
        summary.put(sAllUOLinkedRecordCount0, Integer.toString(CouncilLinkedRecordCount0 + RSLLinkedRecordCount0));
        summary.put(sCouncilFilename1, councilFilenames.get(yM31));
        summary.put(sRSLFilename1, RSLFilenames.get(yM31));
        summary.put(sCouncilCount1, Integer.toString(CouncilCount1));
        summary.put(sCouncilLinkedRecordCount1, Integer.toString(CouncilLinkedRecordCount1));
        summary.put(sRSLCount1, Integer.toString(RSLCount1));
        summary.put(sRSLLinkedRecordCount1, Integer.toString(RSLLinkedRecordCount1));
        summary.put(sAllUOCount1, Integer.toString(AllUOCount1));
        summary.put(sAllUOLinkedRecordCount1, Integer.toString(CouncilLinkedRecordCount1 + RSLLinkedRecordCount1));
    }

    protected void addToSetsForComparisons(
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

    protected void doPartSummarySingleTime(
            DW_SHBE_Collection tSHBEData,
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
//        // Set the last results
//        AllCount0 = AllCount1;
//        HBCount0 = HBCount1;
//        CTBCount0 = CTBCount1;
//        AllUOCount0 = AllUOCount1;
//        CouncilCount0 = CouncilCount1;
//        RSLCount0 = RSLCount1;
//        CouncilLinkedRecordCount0 = CouncilLinkedRecordCount1;
//        RSLLinkedRecordCount0 = RSLLinkedRecordCount1;
//        AllCount0 = AllCount1;
//        AllUOLinkedRecordCount0 = AllUOLinkedRecordCount1;
////        for (int TT = 0; TT < nTT; TT++) {
////            AllTotalCountTTClaimant0[TT] = AllTotalCountTTClaimant1[TT];
////        }
//        System.arraycopy(TotalCount_TTClaimant1, 0, TotalCount_TTClaimant0, 0, nTT);

        TreeMap<String, DW_SHBE_Record> tDRecords;
        tDRecords = tSHBEData.getRecords();
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
                tDRecords,
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
                councilUnderOccupiedSetMap, tDRecords, yM3v);
        // To calculate just council percentages and rates we should do this here.
        // Loop over RSL
        RSLLinkedRecordCount1 = doSingleTimeLoopOverSet(
                RSLUnderOccupiedSetMap, tDRecords, yM3v);
        // The above adds to the councilo calculate just council percentages and rates we should do this here.
        // Prepare vars
        CouncilCount1 = councilUnderOccupiedSetMap.size();
        RSLCount1 = RSLUnderOccupiedSetMap.size();
        AllUOCount1 = CouncilCount1 + RSLCount1;
        AllUOLinkedRecordCount1 = CouncilLinkedRecordCount1 + RSLLinkedRecordCount1;
        // Add to counts
        HashMap<String, BigDecimal> incomeAndRentSummary;
        incomeAndRentSummary = DW_SHBE_Handler.getIncomeAndRentSummary(
                tSHBEData,
                filename,
                paymentType,
                councilUnderOccupiedSet,
                true,
                forceNewSummaries);
        addToSummarySingleTimeIncomeAndRent(
                summary,
                incomeAndRentSummary);
        addToSummarySingleTime(nTT, nEG, nPSI, summary);
        addToSummarySingleTimeRentArrears(summary);
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
            String yM30v,
            String yM31v) {
        Iterator<String> ite;
        // Go through all those in D_Records0 and do counts for all those 
        // that are in map1
        ite = D_Records0.keySet().iterator();
        while (ite.hasNext()) {
            String CTBRef;
            CTBRef = ite.next();
            DW_SHBE_Record Record0;
            Record0 = D_Records0.get(CTBRef);
            if (map1.containsKey(CTBRef)) {
                DW_SHBE_Record Record1;
                Record1 = D_Records1.get(CTBRef);
                DW_SHBE_D_Record D_Record0;
                D_Record0 = null;
                if (Record0 != null) {
                    D_Record0 = Record0.getDRecord();
                }
                DW_SHBE_D_Record D_Record1;
                D_Record1 = null;
                if (D_Record1 != null) {
                    D_Record1 = Record1.getDRecord();
                }
                doCompare2TimesCounts(
                        D_Record0,
                        D_Record1,
                        yM30v,
                        yM31v);
            }
        }
        // Go through all those in current UO data        
        ite = map1.keySet().iterator();
        while (ite.hasNext()) {
            String CTBRef;
            CTBRef = ite.next();
            DW_UOReport_Record UORec;
            UORec = map1.get(CTBRef);
            // Rent Arrears Summary
            doSingleTimeRentArrearsCount(UORec);
//            String CTBRef;
//            CTBRef = UORec.getClaimReferenceNumber();
            DW_SHBE_Record Record0;
            Record0 = D_Records0.get(CTBRef);
            DW_SHBE_Record Record1;
            Record1 = D_Records1.get(CTBRef);
            DW_SHBE_D_Record D_Record0;
            D_Record0 = null;
            if (Record0 != null) {
                D_Record0 = Record0.getDRecord();
            }
            DW_SHBE_D_Record D_Record1;
            D_Record1 = null;
            if (Record1 != null) {
                D_Record1 = Record1.getDRecord();
            }
            doCompare2TimesCounts(
                    D_Record0,
                    D_Record1,
                    yM30v,
                    yM31v);
        }
        // Go through all those that were in the UO data, but have come out.
        HashSet<String> claimsOutOfUO;
        claimsOutOfUO = new HashSet<String>();
        claimsOutOfUO.addAll(map0.keySet());
        claimsOutOfUO.removeAll(map1.keySet());
        ite = claimsOutOfUO.iterator();
        while (ite.hasNext()) {
            String CTBRef;
            CTBRef = ite.next();
            DW_UOReport_Record UORec;
            UORec = map0.get(CTBRef);
            // Rent Arrears Summary
            doSingleTimeRentArrearsCount(UORec);
//            String CTBRef;
//            CTBRef = UORec.getClaimReferenceNumber();
            DW_SHBE_Record Record0;
            Record0 = D_Records0.get(CTBRef);
            DW_SHBE_Record Record1;
            Record1 = D_Records1.get(CTBRef);
            DW_SHBE_D_Record D_Record0;
            D_Record0 = null;
            if (Record0 != null) {
                D_Record0 = Record0.getDRecord();
            }
            DW_SHBE_D_Record D_Record1;
            D_Record1 = null;
            if (Record1 != null) {
                D_Record1 = Record1.getDRecord();
            }
            doCompare2TimesCounts(
                    D_Record0,
                    D_Record1,
                    yM30v,
                    yM31v);
        }
    }

    /**
     *
     * @param map
     * @param D_Records
     * @param yM30v
     * @return
     */
    public int doSingleTimeLoopOverSet(
            TreeMap<String, DW_UOReport_Record> map,
            TreeMap<String, DW_SHBE_Record> D_Records,
            String yM30v) {
        int linkedRecords;
        linkedRecords = 0;
        Iterator<String> ite;
        ite = map.keySet().iterator();
        while (ite.hasNext()) {
            String CTBRef;
            CTBRef = ite.next();
            DW_UOReport_Record UORec;
            UORec = map.get(CTBRef);
            // Rent Arrears Summary
            doSingleTimeRentArrearsCount(UORec);
            DW_SHBE_Record Record;
            Record = D_Records.get(CTBRef);
            if (Record == null) {
                //tDRecordsCTBRefDW_SHBE_RecordNullCount++;
                int count = 1;
            } else {
                DW_SHBE_D_Record D_Record;
                D_Record = Record.getDRecord();
                doSingleTimeCount(
                        D_Record,
                        yM30v);
                linkedRecords++;
            }
        }
        return linkedRecords;
    }

    @Override
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
    @Override
    public void writeSummaryTables(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean doUnderOccupancy,
            int nTT,
            int nEG,
            int nPSI
    ) {
//        writeSummaryTableCompare3Times(
//                summaryTable,
//                paymentType,
//                includeKey,
//                doUnderOccupancy,
//                nTT, nEG);
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

    @Override
    protected PrintWriter getPrintWriter(
            String name,
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy) {
        PrintWriter result;
        File dirOut;
        dirOut = getSummaryTableDir(paymentType, includeKey, underOccupancy);
        String outFilename;
        outFilename = paymentType + "_" + includeKey + "_";
        if (underOccupancy) {
            outFilename += "UO_";
        }
        outFilename += summaryTable.firstKey() + "_To_" + summaryTable.lastKey() + "_" + name + ".csv";
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
    @Override
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
        // All UO
            header += sSamePostcodeIII + ", ";
        header += sSamePostcodeIOI + ", ";
        header += sSamePostcodeOIO + ", ";
        header += sSameTenancyIII + ", ";
        header += sSameTenancyIOI + ", ";
        header += sSameTenancyOIO + ", ";
        for (int i = 1; i < nTT; i++) {
            header += sSameTenancyIIITT[i] + ", ";
            header += sSameTenancyIOITT[i] + ", ";
            header += sSameTenancyOIOTT[i] + ", ";
        }
        header += sSameTenancyAndPostcodeIII + ", ";
        header += sSameTenancyAndPostcodeIOI + ", ";
        header += sSameTenancyAndPostcodeOIO + ", ";
        for (int i = 1; i < nTT; i++) {
            header += sSameTenancyAndPostcodeIIITT[i] + ", ";
            header += sSameTenancyAndPostcodeIOITT[i] + ", ";
            header += sSameTenancyAndPostcodeOIOTT[i] + ", ";
        }
        // Council
        header += sCouncilSamePostcodeIII + ", ";
        header += sCouncilSamePostcodeIOI + ", ";
        header += sCouncilSamePostcodeOIO + ", ";
        header += sCouncilSameTenancyIII + ", ";
        header += sCouncilSameTenancyIOI + ", ";
        header += sCouncilSameTenancyOIO + ", ";
        for (int i = 1; i < nTT; i++) {
            header += sCouncilSameTenancyIIITT[i] + ", ";
            header += sCouncilSameTenancyIOITT[i] + ", ";
            header += sCouncilSameTenancyOIOTT[i] + ", ";
        }
        header += sCouncilSameTenancyAndPostcodeIII + ", ";
        header += sCouncilSameTenancyAndPostcodeIOI + ", ";
        header += sCouncilSameTenancyAndPostcodeOIO + ", ";
        for (int i = 1; i < nTT; i++) {
            header += sCouncilSameTenancyAndPostcodeIIITT[i] + ", ";
            header += sCouncilSameTenancyAndPostcodeIOITT[i] + ", ";
            header += sCouncilSameTenancyAndPostcodeOIOTT[i] + ", ";
        }
        // RSL
        header += sRSLSamePostcodeIII + ", ";
        header += sRSLSamePostcodeIOI + ", ";
        header += sRSLSamePostcodeOIO + ", ";
        header += sRSLSameTenancyIII + ", ";
        header += sRSLSameTenancyIOI + ", ";
        header += sRSLSameTenancyOIO + ", ";
        for (int i = 1; i < nTT; i++) {
            header += sRSLSameTenancyIIITT[i] + ", ";
            header += sRSLSameTenancyIOITT[i] + ", ";
            header += sRSLSameTenancyOIOTT[i] + ", ";
        }
        header += sRSLSameTenancyAndPostcodeIII + ", ";
        header += sRSLSameTenancyAndPostcodeIOI + ", ";
        header += sRSLSameTenancyAndPostcodeOIO + ", ";
        for (int i = 1; i < nTT; i++) {
            header += sRSLSameTenancyAndPostcodeIIITT[i] + ", ";
            header += sRSLSameTenancyAndPostcodeIOITT[i] + ", ";
            header += sRSLSameTenancyAndPostcodeOIOTT[i] + ", ";
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
            if (filename1 != null) {
                PostCodeLookupDate1 = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                        DW_SHBE_Handler.getYM3(filename1));
                PostCodeLookupFile1Name = ONSPDFiles.get(PostCodeLookupDate1).getName();
            }
            line += PostCodeLookupDate1 + ", " + PostCodeLookupFile1Name + ", ";
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
            // All UO
                line += summary.get(sSamePostcodeIII) + ", ";
            line += summary.get(sSamePostcodeIOI) + ", ";
            line += summary.get(sSamePostcodeOIO) + ", ";
            line += summary.get(sSameTenancyIII) + ", ";
            line += summary.get(sSameTenancyIOI) + ", ";
            line += summary.get(sSameTenancyOIO) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sSameTenancyIIITT[i]) + ", ";
                line += summary.get(sSameTenancyIOITT[i]) + ", ";
                line += summary.get(sSameTenancyOIOTT[i]) + ", ";
            }
            line += summary.get(sSameTenancyAndPostcodeIII) + ", ";
            line += summary.get(sSameTenancyAndPostcodeIOI) + ", ";
            line += summary.get(sSameTenancyAndPostcodeOIO) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sSameTenancyAndPostcodeIIITT[i]) + ", ";
                line += summary.get(sSameTenancyAndPostcodeIOITT[i]) + ", ";
                line += summary.get(sSameTenancyAndPostcodeOIOTT[i]) + ", ";
            }
            // Council
                line += summary.get(sCouncilSamePostcodeIII) + ", ";
            line += summary.get(sCouncilSamePostcodeIOI) + ", ";
            line += summary.get(sCouncilSamePostcodeOIO) + ", ";
            line += summary.get(sCouncilSameTenancyIII) + ", ";
            line += summary.get(sCouncilSameTenancyIOI) + ", ";
            line += summary.get(sCouncilSameTenancyOIO) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sCouncilSameTenancyIIITT[i]) + ", ";
                line += summary.get(sCouncilSameTenancyIOITT[i]) + ", ";
                line += summary.get(sCouncilSameTenancyOIOTT[i]) + ", ";
            }
            line += summary.get(sCouncilSameTenancyAndPostcodeIII) + ", ";
            line += summary.get(sCouncilSameTenancyAndPostcodeIOI) + ", ";
            line += summary.get(sCouncilSameTenancyAndPostcodeOIO) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sCouncilSameTenancyAndPostcodeIIITT[i]) + ", ";
                line += summary.get(sCouncilSameTenancyAndPostcodeIOITT[i]) + ", ";
                line += summary.get(sCouncilSameTenancyAndPostcodeOIOTT[i]) + ", ";
            }
            // RSL
                line += summary.get(sRSLSamePostcodeIII) + ", ";
            line += summary.get(sRSLSamePostcodeIOI) + ", ";
            line += summary.get(sRSLSamePostcodeOIO) + ", ";
            line += summary.get(sRSLSameTenancyIII) + ", ";
            line += summary.get(sRSLSameTenancyIOI) + ", ";
            line += summary.get(sRSLSameTenancyOIO) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sRSLSameTenancyIIITT[i]) + ", ";
                line += summary.get(sRSLSameTenancyIOITT[i]) + ", ";
                line += summary.get(sRSLSameTenancyOIOTT[i]) + ", ";
            }
            line += summary.get(sRSLSameTenancyAndPostcodeIII) + ", ";
            line += summary.get(sRSLSameTenancyAndPostcodeIOI) + ", ";
            line += summary.get(sRSLSameTenancyAndPostcodeOIO) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sRSLSameTenancyAndPostcodeIIITT[i]) + ", ";
                line += summary.get(sRSLSameTenancyAndPostcodeIOITT[i]) + ", ";
                line += summary.get(sRSLSameTenancyAndPostcodeOIOTT[i]) + ", ";
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
    @Override
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
        header = getHeaderCompare2TimesGeneric();
        header += getHeaderCompare2TimesTTChange();
        header += getHeaderCompare2TimesTTChangeCouncil();
        header += getHeaderCompare2TimesTTChangeRSL();
        header += getHeaderCompare2TimesPostcodeChange();
        header += getHeaderCompare2TimesPostcodeChangeCouncil();
        header += getHeaderCompare2TimesPostcodeChangeRSL();
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
            line += getLineCompare2TimesTTChangeCouncil(summary);
            line += getLineCompare2TimesTTChangeRSL(summary);
            line += getLineCompare2TimesPostcodeChange(summary);
            line += getLineCompare2TimesPostcodeChangeCouncil(summary);
            line += getLineCompare2TimesPostcodeChangeRSL(summary);
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    protected String getHeaderCompare2TimesPostcodeChangeCouncil() {
        String header = "";
        // All Postcode Related
        header += sCouncilAllTotalCount_Postcode0ValidPostcode1Valid + ", ";
        header += sCouncilAllPercentagePostcode0ValidPostcode1Valid + ", ";
        header += sCouncilAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sCouncilAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sCouncilAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sCouncilAllPercentagePostcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sCouncilAllTotalCount_Postcode0ValidPostcode1NotValid + ", ";
        header += sCouncilAllPercentagePostcode0ValidPostcode1NotValid + ", ";
        header += sCouncilAllTotalCount_Postcode0NotValidPostcode1Valid + ", ";
        header += sCouncilAllPercentagePostcode0NotValidPostcode1Valid + ", ";
        header += sCouncilAllTotalCount_Postcode0NotValidPostcode1NotValid + ", ";
        header += sCouncilAllPercentagePostcode0NotValidPostcode1NotValid + ", ";
        header += sCouncilAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sCouncilAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sCouncilAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sCouncilAllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        // HB Postcode Related
        header += sCouncilHBTotalCount_Postcode0ValidPostcode1Valid + ", ";
        header += sCouncilHBPercentagePostcode0ValidPostcode1Valid + ", ";
        header += sCouncilHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sCouncilHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sCouncilHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sCouncilHBPercentagePostcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sCouncilHBTotalCount_Postcode0ValidPostcode1NotValid + ", ";
        header += sCouncilHBPercentagePostcode0ValidPostcode1NotValid + ", ";
        header += sCouncilHBTotalCount_Postcode0NotValidPostcode1Valid + ", ";
        header += sCouncilHBPercentagePostcode0NotValidPostcode1Valid + ", ";
        header += sCouncilHBTotalCount_Postcode0NotValidPostcode1NotValid + ", ";
        header += sCouncilHBPercentagePostcode0NotValidPostcode1NotValid + ", ";
        header += sCouncilHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sCouncilHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sCouncilHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sCouncilHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        // CTB Postcode Related
        header += sCouncilCTBTotalCount_Postcode0ValidPostcode1Valid + ", ";
        header += sCouncilCTBPercentageOfCTB_Postcode0ValidPostcode1Valid + ", ";
        header += sCouncilCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sCouncilCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sCouncilCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged + ", ";
        header += sCouncilCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged + ", ";
        header += sCouncilCTBTotalCount_Postcode0ValidPostcode1NotValid + ", ";
        header += sCouncilCTBPercentagePostcode0ValidPostcode1NotValid + ", ";
        header += sCouncilCTBTotalCount_Postcode0NotValidPostcode1Valid + ", ";
        header += sCouncilCTBPercentagePostcode0NotValidPostcode1Valid + ", ";
        header += sCouncilCTBTotalCount_Postcode0NotValidPostcode1NotValid + ", ";
        header += sCouncilCTBPercentagePostcode0NotValidPostcode1NotValid + ", ";
        header += sCouncilCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sCouncilCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sCouncilCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sCouncilCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        return header;
    }

    protected String getHeaderCompare2TimesPostcodeChangeRSL() {
        String header = "";
        // All Postcode Related
        header += sRSLAllTotalCount_Postcode0ValidPostcode1Valid + ", ";
        header += sRSLAllPercentagePostcode0ValidPostcode1Valid + ", ";
        header += sRSLAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sRSLAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sRSLAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sRSLAllPercentagePostcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sRSLAllTotalCount_Postcode0ValidPostcode1NotValid + ", ";
        header += sRSLAllPercentagePostcode0ValidPostcode1NotValid + ", ";
        header += sRSLAllTotalCount_Postcode0NotValidPostcode1Valid + ", ";
        header += sRSLAllPercentagePostcode0NotValidPostcode1Valid + ", ";
        header += sRSLAllTotalCount_Postcode0NotValidPostcode1NotValid + ", ";
        header += sRSLAllPercentagePostcode0NotValidPostcode1NotValid + ", ";
        header += sRSLAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sRSLAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sRSLAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sRSLAllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        // HB Postcode Related
        header += sRSLHBTotalCount_Postcode0ValidPostcode1Valid + ", ";
        header += sRSLHBPercentagePostcode0ValidPostcode1Valid + ", ";
        header += sRSLHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sRSLHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sRSLHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sRSLHBPercentagePostcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sRSLHBTotalCount_Postcode0ValidPostcode1NotValid + ", ";
        header += sRSLHBPercentagePostcode0ValidPostcode1NotValid + ", ";
        header += sRSLHBTotalCount_Postcode0NotValidPostcode1Valid + ", ";
        header += sRSLHBPercentagePostcode0NotValidPostcode1Valid + ", ";
        header += sRSLHBTotalCount_Postcode0NotValidPostcode1NotValid + ", ";
        header += sRSLHBPercentagePostcode0NotValidPostcode1NotValid + ", ";
        header += sRSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sRSLHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sRSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sRSLHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        // CTB Postcode Related
        header += sRSLCTBTotalCount_Postcode0ValidPostcode1Valid + ", ";
        header += sRSLCTBPercentageOfCTB_Postcode0ValidPostcode1Valid + ", ";
        header += sRSLCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sRSLCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sRSLCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged + ", ";
        header += sRSLCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged + ", ";
        header += sRSLCTBTotalCount_Postcode0ValidPostcode1NotValid + ", ";
        header += sRSLCTBPercentagePostcode0ValidPostcode1NotValid + ", ";
        header += sRSLCTBTotalCount_Postcode0NotValidPostcode1Valid + ", ";
        header += sRSLCTBPercentagePostcode0NotValidPostcode1Valid + ", ";
        header += sRSLCTBTotalCount_Postcode0NotValidPostcode1NotValid + ", ";
        header += sRSLCTBPercentagePostcode0NotValidPostcode1NotValid + ", ";
        header += sRSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sRSLCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sRSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sRSLCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        return header;
    }
    
    protected String getLineCompare2TimesPostcodeChangeCouncil(HashMap<String, String> summary) {
        String line = "";
        // All
        line += summary.get(sCouncilAllTotalCount_Postcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sCouncilAllPercentagePostcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sCouncilAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sCouncilAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sCouncilAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sCouncilAllPercentagePostcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sCouncilAllTotalCount_Postcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sCouncilAllPercentagePostcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sCouncilAllTotalCount_Postcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sCouncilAllPercentagePostcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sCouncilAllTotalCount_Postcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sCouncilAllPercentagePostcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sCouncilAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sCouncilAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sCouncilAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        line += summary.get(sCouncilAllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        // HB
        line += summary.get(sCouncilHBTotalCount_Postcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sCouncilHBPercentagePostcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sCouncilHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sCouncilHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sCouncilHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sCouncilHBPercentagePostcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sCouncilHBTotalCount_Postcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sCouncilHBPercentagePostcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sCouncilHBTotalCount_Postcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sCouncilHBPercentagePostcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sCouncilHBTotalCount_Postcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sCouncilHBPercentagePostcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sCouncilHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sCouncilHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sCouncilHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        line += summary.get(sCouncilHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        // CTB
        line += summary.get(sCouncilCTBTotalCount_Postcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sCouncilCTBPercentageOfCTB_Postcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sCouncilCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sCouncilCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sCouncilCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged) + ", ";
        line += summary.get(sCouncilCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged) + ", ";
        line += summary.get(sCouncilCTBTotalCount_Postcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sCouncilCTBPercentagePostcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sCouncilCTBTotalCount_Postcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sCouncilCTBPercentagePostcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sCouncilCTBTotalCount_Postcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sCouncilCTBPercentagePostcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sCouncilCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sCouncilCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sCouncilCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        line += summary.get(sCouncilCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        return line;
    }

    protected String getLineCompare2TimesPostcodeChangeRSL(HashMap<String, String> summary) {
        String line = "";
        // All
        line += summary.get(sRSLAllTotalCount_Postcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sRSLAllPercentagePostcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sRSLAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sRSLAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sRSLAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sRSLAllPercentagePostcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sRSLAllTotalCount_Postcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sRSLAllPercentagePostcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sRSLAllTotalCount_Postcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sRSLAllPercentagePostcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sRSLAllTotalCount_Postcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sRSLAllPercentagePostcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sRSLAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sRSLAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sRSLAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        line += summary.get(sRSLAllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        // HB
        line += summary.get(sRSLHBTotalCount_Postcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sRSLHBPercentagePostcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sRSLHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sRSLHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sRSLHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sRSLHBPercentagePostcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sRSLHBTotalCount_Postcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sRSLHBPercentagePostcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sRSLHBTotalCount_Postcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sRSLHBPercentagePostcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sRSLHBTotalCount_Postcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sRSLHBPercentagePostcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sRSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sRSLHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sRSLHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        line += summary.get(sRSLHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        // CTB
        line += summary.get(sRSLCTBTotalCount_Postcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sRSLCTBPercentageOfCTB_Postcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sRSLCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sRSLCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sRSLCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged) + ", ";
        line += summary.get(sRSLCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged) + ", ";
        line += summary.get(sRSLCTBTotalCount_Postcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sRSLCTBPercentagePostcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sRSLCTBTotalCount_Postcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sRSLCTBPercentagePostcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sRSLCTBTotalCount_Postcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sRSLCTBPercentagePostcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sRSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sRSLCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sRSLCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        line += summary.get(sRSLCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        return line;
    }
    
    public String getHeaderCompare2TimesTTChangeCouncil() {
        String header = "";
        // General
        // All
        header += sCouncilAllTotalCount_TTChangeClaimant + ", ";
        header += sCouncilAllPercentageOfAll_TTChangeClaimant + ", ";
        // General HB related
        header += sCouncilHBTotalCount_TTChangeClaimant + ", ";
        header += sCouncilHBPercentageOfHB_TTChangeClaimant + ", ";
        header += sCouncilTotalCount_Minus999TTToSocialTTs + ", ";
        header += sCouncilTotalCount_Minus999TTToPrivateDeregulatedTTs + ", ";
        header += sCouncilTotalCount_HBTTsToMinus999TT + ", ";
        header += sCouncilPercentageOfHB_HBTTsToMinus999TT + ", ";
        header += sCouncilTotalCount_SocialTTsToMinus999TT + ", ";
        header += sCouncilPercentageOfSocialTTs_SocialTTsToMinus999TT + ", ";
        header += sCouncilTotalCount_PrivateDeregulatedTTsToMinus999TT + ", ";
        header += sCouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT + ", ";
        header += sCouncilTotalCount_HBTTsToHBTTs + ", ";
        header += sCouncilPercentageOfHB_HBTTsToHBTTs + ", ";
        header += sCouncilTotalCount_SocialTTsToPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs + ", ";
        header += sCouncilTotalCount_PrivateDeregulatedTTsToSocialTTs + ", ";
        header += sCouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs + ", ";
        header += sCouncilTotalCount_TT1ToPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfTT1_TT1ToPrivateDeregulatedTTs + ", ";
        header += sCouncilTotalCount_TT4ToPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfTT4_TT4ToPrivateDeregulatedTTs + ", ";
        header += sCouncilTotalCount_PrivateDeregulatedTTsToTT1 + ", ";
        header += sCouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1 + ", ";
        header += sCouncilTotalCount_PrivateDeregulatedTTsToTT4 + ", ";
        header += sCouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4 + ", ";
        header += sCouncilTotalCount_TT1ToTT4 + ", ";
        header += sCouncilPercentageOfTT1_TT1ToTT4 + ", ";
        header += sCouncilTotalCount_TT4ToTT1 + ", ";
        header += sCouncilPercentageOfTT4_TT4ToTT1 + ", ";
        header += sCouncilTotalCount_PostcodeChangeWithinSocialTTs + ", ";
        header += sCouncilPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs + ", ";
        header += sCouncilTotalCount_PostcodeChangeWithinTT1 + ", ";
        header += sCouncilPercentageOfTT1_PostcodeChangeWithinTT1 + ", ";
        header += sCouncilTotalCount_PostcodeChangeWithinTT4 + ", ";
        header += sCouncilPercentageOfTT4_PostcodeChangeWithinTT4 + ", ";
        header += sCouncilTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs + ", ";
        header += sCouncilTotalCount_HBTTsToCTBTTs + ", ";
        header += sCouncilPercentageOfHB_HBTTsToCTBTTs + ", ";
        // General CTB related
        header += sCouncilCTBTotalCount_TTChangeClaimant + ", ";
        header += sCouncilCTBPercentageOfCTB_TTChangeClaimant + ", ";
        header += sCouncilTotalCount_Minus999TTToCTBTTs + ", ";
        header += sCouncilTotalCount_CTBTTsToMinus999TT + ", ";
        header += sCouncilPercentageOfCTB_CTBTTsToMinus999TT + ", ";
        header += sCouncilTotalCount_SocialTTsToCTBTTs + ", ";
        header += sCouncilPercentageOfSocialTTs_SocialTTsToCTBTTs + ", ";
        header += sCouncilTotalCount_TT1ToCTBTTs + ", ";
        header += sCouncilPercentageOfTT1_TT1ToCTBTTs + ", ";
        header += sCouncilTotalCount_TT4ToCTBTTs + ", ";
        header += sCouncilPercentageOfTT4_TT4ToCTBTTs + ", ";
        header += sCouncilTotalCount_PrivateDeregulatedTTsToCTBTTs + ", ";
        header += sCouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs + ", ";
        header += sCouncilTotalCount_CTBTTsToSocialTTs + ", ";
        header += sCouncilPercentageOfCTB_CTBTTsToSocialTTs + ", ";
        header += sCouncilTotalCount_CTBTTsToTT1 + ", ";
        header += sCouncilPercentageOfCTB_CTBTTsToTT1 + ", ";
        header += sCouncilTotalCount_CTBTTsToTT4 + ", ";
        header += sCouncilPercentageOfCTB_CTBTTsToTT4 + ", ";
        header += sCouncilTotalCount_CTBTTsToPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfCTB_CTBTTsToPrivateDeregulatedTTs + ", ";
        header += sCouncilTotalCount_CTBTTsToHBTTs + ", ";
        header += sCouncilPercentageOfCTB_CTBTTsToHBTTs + ", ";
        return header;
    }

    public String getHeaderCompare2TimesTTChangeRSL() {
        String header = "";
        // General
        // All
        header += sRSLAllTotalCount_TTChangeClaimant + ", ";
        header += sRSLAllPercentageOfAll_TTChangeClaimant + ", ";
        // General HB related
        header += sRSLHBTotalCount_TTChangeClaimant + ", ";
        header += sRSLHBPercentageOfHB_TTChangeClaimant + ", ";
        header += sRSLTotalCount_Minus999TTToSocialTTs + ", ";
        header += sRSLTotalCount_Minus999TTToPrivateDeregulatedTTs + ", ";
        header += sRSLTotalCount_HBTTsToMinus999TT + ", ";
        header += sRSLPercentageOfHB_HBTTsToMinus999TT + ", ";
        header += sRSLTotalCount_SocialTTsToMinus999TT + ", ";
        header += sRSLPercentageOfSocialTTs_SocialTTsToMinus999TT + ", ";
        header += sRSLTotalCount_PrivateDeregulatedTTsToMinus999TT + ", ";
        header += sRSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT + ", ";
        header += sRSLTotalCount_HBTTsToHBTTs + ", ";
        header += sRSLPercentageOfHB_HBTTsToHBTTs + ", ";
        header += sRSLTotalCount_SocialTTsToPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs + ", ";
        header += sRSLTotalCount_PrivateDeregulatedTTsToSocialTTs + ", ";
        header += sRSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs + ", ";
        header += sRSLTotalCount_TT1ToPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfTT1_TT1ToPrivateDeregulatedTTs + ", ";
        header += sRSLTotalCount_TT4ToPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfTT4_TT4ToPrivateDeregulatedTTs + ", ";
        header += sRSLTotalCount_PrivateDeregulatedTTsToTT1 + ", ";
        header += sRSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1 + ", ";
        header += sRSLTotalCount_PrivateDeregulatedTTsToTT4 + ", ";
        header += sRSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4 + ", ";
        header += sRSLTotalCount_TT1ToTT4 + ", ";
        header += sRSLPercentageOfTT1_TT1ToTT4 + ", ";
        header += sRSLTotalCount_TT4ToTT1 + ", ";
        header += sRSLPercentageOfTT4_TT4ToTT1 + ", ";
        header += sRSLTotalCount_PostcodeChangeWithinSocialTTs + ", ";
        header += sRSLPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs + ", ";
        header += sRSLTotalCount_PostcodeChangeWithinTT1 + ", ";
        header += sRSLPercentageOfTT1_PostcodeChangeWithinTT1 + ", ";
        header += sRSLTotalCount_PostcodeChangeWithinTT4 + ", ";
        header += sRSLPercentageOfTT4_PostcodeChangeWithinTT4 + ", ";
        header += sRSLTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs + ", ";
        header += sRSLTotalCount_HBTTsToCTBTTs + ", ";
        header += sRSLPercentageOfHB_HBTTsToCTBTTs + ", ";
        // General CTB related
        header += sRSLCTBTotalCount_TTChangeClaimant + ", ";
        header += sRSLCTBPercentageOfCTB_TTChangeClaimant + ", ";
        header += sRSLTotalCount_Minus999TTToCTBTTs + ", ";
        header += sRSLTotalCount_CTBTTsToMinus999TT + ", ";
        header += sRSLPercentageOfCTB_CTBTTsToMinus999TT + ", ";
        header += sRSLTotalCount_SocialTTsToCTBTTs + ", ";
        header += sRSLPercentageOfSocialTTs_SocialTTsToCTBTTs + ", ";
        header += sRSLTotalCount_TT1ToCTBTTs + ", ";
        header += sRSLPercentageOfTT1_TT1ToCTBTTs + ", ";
        header += sRSLTotalCount_TT4ToCTBTTs + ", ";
        header += sRSLPercentageOfTT4_TT4ToCTBTTs + ", ";
        header += sRSLTotalCount_PrivateDeregulatedTTsToCTBTTs + ", ";
        header += sRSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs + ", ";
        header += sRSLTotalCount_CTBTTsToSocialTTs + ", ";
        header += sRSLPercentageOfCTB_CTBTTsToSocialTTs + ", ";
        header += sRSLTotalCount_CTBTTsToTT1 + ", ";
        header += sRSLPercentageOfCTB_CTBTTsToTT1 + ", ";
        header += sRSLTotalCount_CTBTTsToTT4 + ", ";
        header += sRSLPercentageOfCTB_CTBTTsToTT4 + ", ";
        header += sRSLTotalCount_CTBTTsToPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfCTB_CTBTTsToPrivateDeregulatedTTs + ", ";
        header += sRSLTotalCount_CTBTTsToHBTTs + ", ";
        header += sRSLPercentageOfCTB_CTBTTsToHBTTs + ", ";
        return header;
    }
    
    protected String getLineCompare2TimesTTChangeCouncil(HashMap<String, String> summary) {
        String line = "";
        // All
        line += summary.get(sCouncilAllTotalCount_TTChangeClaimant) + ", ";
        line += summary.get(sCouncilAllPercentageOfAll_TTChangeClaimant) + ", ";
        // General HB related
        line += summary.get(sCouncilHBTotalCount_TTChangeClaimant) + ", ";
        line += summary.get(sCouncilHBPercentageOfHB_TTChangeClaimant) + ", ";
        line += summary.get(sCouncilTotalCount_Minus999TTToSocialTTs) + ", ";
        line += summary.get(sCouncilTotalCount_Minus999TTToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sCouncilTotalCount_HBTTsToMinus999TT) + ", ";
        line += summary.get(sCouncilPercentageOfHB_HBTTsToMinus999TT) + ", ";
        line += summary.get(sCouncilTotalCount_SocialTTsToMinus999TT) + ", ";
        line += summary.get(sCouncilPercentageOfSocialTTs_SocialTTsToMinus999TT) + ", ";
        line += summary.get(sCouncilTotalCount_PrivateDeregulatedTTsToMinus999TT) + ", ";
        line += summary.get(sCouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT) + ", ";
        line += summary.get(sCouncilTotalCount_HBTTsToHBTTs) + ", ";
        line += summary.get(sCouncilPercentageOfHB_HBTTsToHBTTs) + ", ";
        line += summary.get(sCouncilTotalCount_SocialTTsToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sCouncilPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sCouncilTotalCount_PrivateDeregulatedTTsToSocialTTs) + ", ";
        line += summary.get(sCouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs) + ", ";
        line += summary.get(sCouncilTotalCount_TT1ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sCouncilPercentageOfTT1_TT1ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sCouncilTotalCount_TT4ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sCouncilPercentageOfTT4_TT4ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sCouncilTotalCount_PrivateDeregulatedTTsToTT1) + ", ";
        line += summary.get(sCouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1) + ", ";
        line += summary.get(sCouncilTotalCount_PrivateDeregulatedTTsToTT4) + ", ";
        line += summary.get(sCouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4) + ", ";
        line += summary.get(sCouncilTotalCount_TT1ToTT4) + ", ";
        line += summary.get(sCouncilPercentageOfTT1_TT1ToTT4) + ", ";
        line += summary.get(sCouncilTotalCount_TT4ToTT1) + ", ";
        line += summary.get(sCouncilPercentageOfTT4_TT4ToTT1) + ", ";
        line += summary.get(sCouncilTotalCount_PostcodeChangeWithinSocialTTs) + ", ";
        line += summary.get(sCouncilPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs) + ", ";
        line += summary.get(sCouncilTotalCount_PostcodeChangeWithinTT1) + ", ";
        line += summary.get(sCouncilPercentageOfTT1_PostcodeChangeWithinTT1) + ", ";
        line += summary.get(sCouncilTotalCount_PostcodeChangeWithinTT4) + ", ";
        line += summary.get(sCouncilPercentageOfTT4_PostcodeChangeWithinTT4) + ", ";
        line += summary.get(sCouncilTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs) + ", ";
        line += summary.get(sCouncilPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs) + ", ";
        line += summary.get(sCouncilTotalCount_HBTTsToCTBTTs) + ", ";
        line += summary.get(sCouncilPercentageOfHB_HBTTsToCTBTTs) + ", ";
        // General CTB related
        line += summary.get(sCouncilCTBTotalCount_TTChangeClaimant) + ", ";
        line += summary.get(sCouncilCTBPercentageOfCTB_TTChangeClaimant) + ", ";
        line += summary.get(sCouncilTotalCount_Minus999TTToCTBTTs) + ", ";
        line += summary.get(sCouncilTotalCount_CTBTTsToMinus999TT) + ", ";
        line += summary.get(sCouncilPercentageOfCTB_CTBTTsToMinus999TT) + ", ";
        line += summary.get(sCouncilTotalCount_SocialTTsToCTBTTs) + ", ";
        line += summary.get(sCouncilPercentageOfSocialTTs_SocialTTsToCTBTTs) + ", ";
        line += summary.get(sCouncilTotalCount_TT1ToCTBTTs) + ", ";
        line += summary.get(sCouncilPercentageOfTT1_TT1ToCTBTTs) + ", ";
        line += summary.get(sCouncilTotalCount_TT4ToCTBTTs) + ", ";
        line += summary.get(sCouncilPercentageOfTT4_TT4ToCTBTTs) + ", ";
        line += summary.get(sCouncilTotalCount_PrivateDeregulatedTTsToCTBTTs) + ", ";
        line += summary.get(sCouncilPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs) + ", ";
        line += summary.get(sCouncilTotalCount_CTBTTsToSocialTTs) + ", ";
        line += summary.get(sCouncilPercentageOfCTB_CTBTTsToSocialTTs) + ", ";
        line += summary.get(sCouncilTotalCount_CTBTTsToTT1) + ", ";
        line += summary.get(sCouncilPercentageOfCTB_CTBTTsToTT1) + ", ";
        line += summary.get(sCouncilTotalCount_CTBTTsToTT4) + ", ";
        line += summary.get(sCouncilPercentageOfCTB_CTBTTsToTT4) + ", ";
        line += summary.get(sCouncilTotalCount_CTBTTsToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sCouncilPercentageOfCTB_CTBTTsToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sCouncilTotalCount_CTBTTsToHBTTs) + ", ";
        line += summary.get(sCouncilPercentageOfCTB_CTBTTsToHBTTs) + ", ";
        return line;
    }
    
    protected String getLineCompare2TimesTTChangeRSL(HashMap<String, String> summary) {
        String line = "";
        // All
        line += summary.get(sRSLAllTotalCount_TTChangeClaimant) + ", ";
        line += summary.get(sRSLAllPercentageOfAll_TTChangeClaimant) + ", ";
        // General HB related
        line += summary.get(sRSLHBTotalCount_TTChangeClaimant) + ", ";
        line += summary.get(sRSLHBPercentageOfHB_TTChangeClaimant) + ", ";
        line += summary.get(sRSLTotalCount_Minus999TTToSocialTTs) + ", ";
        line += summary.get(sRSLTotalCount_Minus999TTToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sRSLTotalCount_HBTTsToMinus999TT) + ", ";
        line += summary.get(sRSLPercentageOfHB_HBTTsToMinus999TT) + ", ";
        line += summary.get(sRSLTotalCount_SocialTTsToMinus999TT) + ", ";
        line += summary.get(sRSLPercentageOfSocialTTs_SocialTTsToMinus999TT) + ", ";
        line += summary.get(sRSLTotalCount_PrivateDeregulatedTTsToMinus999TT) + ", ";
        line += summary.get(sRSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT) + ", ";
        line += summary.get(sRSLTotalCount_HBTTsToHBTTs) + ", ";
        line += summary.get(sRSLPercentageOfHB_HBTTsToHBTTs) + ", ";
        line += summary.get(sRSLTotalCount_SocialTTsToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sRSLPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sRSLTotalCount_PrivateDeregulatedTTsToSocialTTs) + ", ";
        line += summary.get(sRSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs) + ", ";
        line += summary.get(sRSLTotalCount_TT1ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sRSLPercentageOfTT1_TT1ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sRSLTotalCount_TT4ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sRSLPercentageOfTT4_TT4ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sRSLTotalCount_PrivateDeregulatedTTsToTT1) + ", ";
        line += summary.get(sRSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1) + ", ";
        line += summary.get(sRSLTotalCount_PrivateDeregulatedTTsToTT4) + ", ";
        line += summary.get(sRSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4) + ", ";
        line += summary.get(sRSLTotalCount_TT1ToTT4) + ", ";
        line += summary.get(sRSLPercentageOfTT1_TT1ToTT4) + ", ";
        line += summary.get(sRSLTotalCount_TT4ToTT1) + ", ";
        line += summary.get(sRSLPercentageOfTT4_TT4ToTT1) + ", ";
        line += summary.get(sRSLTotalCount_PostcodeChangeWithinSocialTTs) + ", ";
        line += summary.get(sRSLPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs) + ", ";
        line += summary.get(sRSLTotalCount_PostcodeChangeWithinTT1) + ", ";
        line += summary.get(sRSLPercentageOfTT1_PostcodeChangeWithinTT1) + ", ";
        line += summary.get(sRSLTotalCount_PostcodeChangeWithinTT4) + ", ";
        line += summary.get(sRSLPercentageOfTT4_PostcodeChangeWithinTT4) + ", ";
        line += summary.get(sRSLTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs) + ", ";
        line += summary.get(sRSLPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs) + ", ";
        line += summary.get(sRSLTotalCount_HBTTsToCTBTTs) + ", ";
        line += summary.get(sRSLPercentageOfHB_HBTTsToCTBTTs) + ", ";
        // General CTB related
        line += summary.get(sRSLCTBTotalCount_TTChangeClaimant) + ", ";
        line += summary.get(sRSLCTBPercentageOfCTB_TTChangeClaimant) + ", ";
        line += summary.get(sRSLTotalCount_Minus999TTToCTBTTs) + ", ";
        line += summary.get(sRSLTotalCount_CTBTTsToMinus999TT) + ", ";
        line += summary.get(sRSLPercentageOfCTB_CTBTTsToMinus999TT) + ", ";
        line += summary.get(sRSLTotalCount_SocialTTsToCTBTTs) + ", ";
        line += summary.get(sRSLPercentageOfSocialTTs_SocialTTsToCTBTTs) + ", ";
        line += summary.get(sRSLTotalCount_TT1ToCTBTTs) + ", ";
        line += summary.get(sRSLPercentageOfTT1_TT1ToCTBTTs) + ", ";
        line += summary.get(sRSLTotalCount_TT4ToCTBTTs) + ", ";
        line += summary.get(sRSLPercentageOfTT4_TT4ToCTBTTs) + ", ";
        line += summary.get(sRSLTotalCount_PrivateDeregulatedTTsToCTBTTs) + ", ";
        line += summary.get(sRSLPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs) + ", ";
        line += summary.get(sRSLTotalCount_CTBTTsToSocialTTs) + ", ";
        line += summary.get(sRSLPercentageOfCTB_CTBTTsToSocialTTs) + ", ";
        line += summary.get(sRSLTotalCount_CTBTTsToTT1) + ", ";
        line += summary.get(sRSLPercentageOfCTB_CTBTTsToTT1) + ", ";
        line += summary.get(sRSLTotalCount_CTBTTsToTT4) + ", ";
        line += summary.get(sRSLPercentageOfCTB_CTBTTsToTT4) + ", ";
        line += summary.get(sRSLTotalCount_CTBTTsToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sRSLPercentageOfCTB_CTBTTsToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sRSLTotalCount_CTBTTsToHBTTs) + ", ";
        line += summary.get(sRSLPercentageOfCTB_CTBTTsToHBTTs) + ", ";
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
    @Override
    public void writeSummaryTableCompare2TimesTT(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        //super.writeSummaryTableCompare2TimesTT(summaryTable, paymentType, includeKey, underOccupancy, nTT, nEG);
        TreeMap<String, File> ONSPDFiles;
        ONSPDFiles = DW_Postcode_Handler.getONSPDFiles();
        String name;
        name = "Compare2TimesTT";
        PrintWriter pw;
        pw = getPrintWriter(name, summaryTable, paymentType, includeKey, underOccupancy);
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

    @Override
    public String getHeaderCompare2TimesGeneric() {
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
        header += sAllCount0 + ", ";
        header += sHBCount0 + ", ";
        header += sCTBCount0 + ", ";
        header += sAllCount1 + ", ";
        header += sHBCount1 + ", ";
        header += sCTBCount1 + ", ";
        header += "Month0 Year0 to Month1 Year1, ";
        return header;
    }

    @Override
    public String getLineCompare2TimesGeneric(
            HashMap<String, String> summary,
            TreeMap<String, File> ONSPDFiles) {
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
        String month1;
        String year1;
        if (filename1 != null) {
            line += DW_SHBE_Handler.getYearMonthNumber(filename1) + ", ";
            month1 = DW_SHBE_Handler.getMonth3(filename1);
            year1 = DW_SHBE_Handler.getYear(filename1);
            line += month1 + " " + year1 + ", ";
        } else {
            month1 = "null";
            year1 = "null";
            line += "null, ";
            line += "null, ";
        }
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
        if (filename1 != null) {
            PostCodeLookupDate1 = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                    DW_SHBE_Handler.getYM3(filename1));
            PostCodeLookupFile1Name = ONSPDFiles.get(PostCodeLookupDate1).getName();
        }
        line += PostCodeLookupDate1 + ", " + PostCodeLookupFile1Name + ", ";
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
        line += summary.get(sAllCount0) + ", ";
        line += summary.get(sHBCount0) + ", ";
        line += summary.get(sCTBCount0) + ", ";
        line += summary.get(sAllCount1) + ", ";
        line += summary.get(sHBCount1) + ", ";
        line += summary.get(sCTBCount1) + ", ";
        line += month0 + " " + year0 + " to " + month1 + " " + year1 + ", ";
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
    @Override
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
        header = getHeaderCompare2TimesGeneric();
        header += getHeaderCompare2TimesPostcodeChange();
        header += getHeaderCompare2TimesPostcodeChangeCouncil();
        header += getHeaderCompare2TimesPostcodeChangeRSL();
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
            line += getLineCompare2TimesPostcodeChangeCouncil(summary);
            line += getLineCompare2TimesPostcodeChangeRSL(summary);
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    @Override
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
        header += getHeaderSingleTimeGeneric();
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
        header += sAllTotalHouseholdSize + ", ";
        header += sAllAverageHouseholdSize + ", ";
        header += sHBTotalHouseholdSize + ", ";
        header += sHBAverageHouseholdSize + ", ";
        header += sCTBTotalHouseholdSize + ", ";
        header += sCTBAverageHouseholdSize + ", ";
        header += sAllTotalCount_PostcodeValidFormat + ", ";
        header += sAllTotalCount_PostcodeValid + ", ";
        header += sHBTotalCount_PostcodeValidFormat + ", ";
        header += sHBTotalCount_PostcodeValid + ", ";
        header += sCTBTotalCount_PostcodeValidFormat + ", ";
        header += sCTBTotalCount_PostcodeValid + ", ";
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
            line += getLineSingleTimeGeneric(key, summary);
                line += getPostcodeLookupDateAndFilenameLinePart(filename1, ONSPDFiles);
                line += summary.get(DW_SHBE_Collection.sLineCount) + ", ";
                line += summary.get(DW_SHBE_Collection.sCountDRecords) + ", ";
                line += summary.get(DW_SHBE_Collection.sCountSRecords) + ", ";
                line += summary.get(DW_SHBE_Collection.sCountUniqueClaimants) + ", ";
                line += summary.get(DW_SHBE_Collection.sCountUniquePartners) + ", ";
                line += summary.get(DW_SHBE_Collection.sCountUniqueDependents) + ", ";
                line += summary.get(DW_SHBE_Collection.sCountUniqueNonDependents) + ", ";
                line += summary.get(DW_SHBE_Collection.sCountUniqueIndividuals) + ", ";
            line += summary.get(sAllTotalHouseholdSize) + ", ";
            line += summary.get(sAllAverageHouseholdSize) + ", ";
            line += summary.get(sHBTotalHouseholdSize) + ", ";
            line += summary.get(sHBAverageHouseholdSize) + ", ";
            line += summary.get(sCTBTotalHouseholdSize) + ", ";
            line += summary.get(sCTBAverageHouseholdSize) + ", ";
            line += summary.get(sAllTotalCount_PostcodeValidFormat) + ", ";
            line += summary.get(sAllTotalCount_PostcodeValid) + ", ";
            line += summary.get(sHBTotalCount_PostcodeValidFormat) + ", ";
            line += summary.get(sHBTotalCount_PostcodeValid) + ", ";
            line += summary.get(sCTBTotalCount_PostcodeValidFormat) + ", ";
            line += summary.get(sCTBTotalCount_PostcodeValid) + ", ";
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    @Override
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
        header += getHeaderSingleTimeGeneric();
        // All UO
        header += sTotalWeeklyHBEntitlement + ", ";
        header += sTotalCount_WeeklyHBEntitlementNonZero + ", ";
        header += sTotalCount_WeeklyHBEntitlementZero + ", ";
        header += sAverageWeeklyHBEntitlement + ", ";
        // WeeklyEligibleRentAmount
        header += sAllTotalWeeklyEligibleRentAmount + ", ";
        header += sAllTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sAllTotalCount_WeeklyEligibleRentAmountZero + ", ";
        header += sAllAverageWeeklyEligibleRentAmount + ", ";
        header += sHBTotalWeeklyEligibleRentAmount + ", ";
        header += sHBTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sHBTotalCount_WeeklyEligibleRentAmountZero + ", ";
        header += sHBAverageWeeklyEligibleRentAmount + ", ";
        header += sCTBTotalWeeklyEligibleRentAmount + ", ";
        header += sCTBTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sCTBTotalCount_WeeklyEligibleRentAmountZero + ", ";
        header += sCTBAverageWeeklyEligibleRentAmount + ", ";
        // WeeklyEligibleCouncilTaxAmount
        header += sAllTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sAllTotalCount_WeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sAllAverageWeeklyEligibleCouncilTaxAmount + ", ";
        header += sHBTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sHBTotalCount_WeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sHBAverageWeeklyEligibleCouncilTaxAmount + ", ";
        header += sCTBTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero + ", ";
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
        header += sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sAllAverageWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sHBTotalWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sHBAverageWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sCTBTotalWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sCTBAverageWeeklyAdditionalDiscretionaryPayment + ", ";
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        header += sAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        // Council
        header += sCouncilTotalWeeklyHBEntitlement + ", ";
        header += sCouncilTotalCount_WeeklyHBEntitlementNonZero + ", ";
        header += sCouncilTotalCount_WeeklyHBEntitlementZero + ", ";
        header += sCouncilAverageWeeklyHBEntitlement + ", ";
        // WeeklyEligibleRentAmount
        header += sCouncilAllTotalWeeklyEligibleRentAmount + ", ";
        header += sCouncilAllTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sCouncilAllTotalCount_WeeklyEligibleRentAmountZero + ", ";
        header += sCouncilAllAverageWeeklyEligibleRentAmount + ", ";
        header += sCouncilHBTotalWeeklyEligibleRentAmount + ", ";
        header += sCouncilHBTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sCouncilHBTotalCount_WeeklyEligibleRentAmountZero + ", ";
        header += sCouncilHBAverageWeeklyEligibleRentAmount + ", ";
        header += sCouncilCTBTotalWeeklyEligibleRentAmount + ", ";
        header += sCouncilCTBTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sCouncilCTBTotalCount_WeeklyEligibleRentAmountZero + ", ";
        header += sCouncilCTBAverageWeeklyEligibleRentAmount + ", ";
        // WeeklyEligibleCouncilTaxAmount
        header += sCouncilAllTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sCouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sCouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sCouncilAllAverageWeeklyEligibleCouncilTaxAmount + ", ";
        header += sCouncilHBTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sCouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sCouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sCouncilHBAverageWeeklyEligibleCouncilTaxAmount + ", ";
        header += sCouncilCTBTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sCouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sCouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sCouncilCTBAverageWeeklyEligibleCouncilTaxAmount + ", ";
        // ContractualRentAmount
        header += sCouncilAllTotalContractualRentAmount + ", ";
        header += sCouncilAllTotalCountContractualRentAmountNonZeroCount + ", ";
        header += sCouncilAllTotalCountContractualRentAmountZeroCount + ", ";
        header += sCouncilAllAverageContractualRentAmount + ", ";
        header += sCouncilHBTotalContractualRentAmount + ", ";
        header += sCouncilHBTotalCountContractualRentAmountNonZeroCount + ", ";
        header += sCouncilHBTotalCountContractualRentAmountZeroCount + ", ";
        header += sCouncilHBAverageContractualRentAmount + ", ";
        header += sCouncilCTBTotalContractualRentAmount + ", ";
        header += sCouncilCTBTotalCountContractualRentAmountNonZeroCount + ", ";
        header += sCouncilCTBTotalCountContractualRentAmountZeroCount + ", ";
        header += sCouncilCTBAverageContractualRentAmount + ", ";
        // WeeklyAdditionalDiscretionaryPayment
        header += sCouncilAllTotalWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sCouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sCouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sCouncilAllAverageWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sCouncilHBTotalWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sCouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sCouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sCouncilHBAverageWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sCouncilCTBTotalWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sCouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sCouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sCouncilCTBAverageWeeklyAdditionalDiscretionaryPayment + ", ";
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        header += sCouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sCouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sCouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sCouncilAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sCouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sCouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sCouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sCouncilHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sCouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sCouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sCouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sCouncilCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        // RSL
        header += sRSLTotalWeeklyHBEntitlement + ", ";
        header += sRSLTotalCount_WeeklyHBEntitlementNonZero + ", ";
        header += sRSLTotalCount_WeeklyHBEntitlementZero + ", ";
        header += sRSLAverageWeeklyHBEntitlement + ", ";
        // WeeklyEligibleRentAmount
        header += sRSLAllTotalWeeklyEligibleRentAmount + ", ";
        header += sRSLAllTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sRSLAllTotalCount_WeeklyEligibleRentAmountZero + ", ";
        header += sRSLAllAverageWeeklyEligibleRentAmount + ", ";
        header += sRSLHBTotalWeeklyEligibleRentAmount + ", ";
        header += sRSLHBTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sRSLHBTotalCount_WeeklyEligibleRentAmountZero + ", ";
        header += sRSLHBAverageWeeklyEligibleRentAmount + ", ";
        header += sRSLCTBTotalWeeklyEligibleRentAmount + ", ";
        header += sRSLCTBTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sRSLCTBTotalCount_WeeklyEligibleRentAmountZero + ", ";
        header += sRSLCTBAverageWeeklyEligibleRentAmount + ", ";
        // WeeklyEligibleCouncilTaxAmount
        header += sRSLAllTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sRSLAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sRSLAllTotalCount_WeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sRSLAllAverageWeeklyEligibleCouncilTaxAmount + ", ";
        header += sRSLHBTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sRSLHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sRSLHBTotalCount_WeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sRSLHBAverageWeeklyEligibleCouncilTaxAmount + ", ";
        header += sRSLCTBTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sRSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sRSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sRSLCTBAverageWeeklyEligibleCouncilTaxAmount + ", ";
        // ContractualRentAmount
        header += sRSLAllTotalContractualRentAmount + ", ";
        header += sRSLAllTotalCountContractualRentAmountNonZeroCount + ", ";
        header += sRSLAllTotalCountContractualRentAmountZeroCount + ", ";
        header += sRSLAllAverageContractualRentAmount + ", ";
        header += sRSLHBTotalContractualRentAmount + ", ";
        header += sRSLHBTotalCountContractualRentAmountNonZeroCount + ", ";
        header += sRSLHBTotalCountContractualRentAmountZeroCount + ", ";
        header += sRSLHBAverageContractualRentAmount + ", ";
        header += sRSLCTBTotalContractualRentAmount + ", ";
        header += sRSLCTBTotalCountContractualRentAmountNonZeroCount + ", ";
        header += sRSLCTBTotalCountContractualRentAmountZeroCount + ", ";
        header += sRSLCTBAverageContractualRentAmount + ", ";
        // WeeklyAdditionalDiscretionaryPayment
        header += sRSLAllTotalWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sRSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sRSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sRSLAllAverageWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sRSLHBTotalWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sRSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sRSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sRSLHBAverageWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sRSLCTBTotalWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sRSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sRSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sRSLCTBAverageWeeklyAdditionalDiscretionaryPayment + ", ";
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        header += sRSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sRSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sRSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sRSLAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sRSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sRSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sRSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sRSLHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sRSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sRSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sRSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sRSLCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
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
            line += getLineSingleTimeGeneric(key, summary);
            // All UO
            line += summary.get(sTotalWeeklyHBEntitlement) + ", ";
            line += summary.get(sTotalCount_WeeklyHBEntitlementNonZero) + ", ";
            line += summary.get(sTotalCount_WeeklyHBEntitlementZero) + ", ";
            line += summary.get(sAverageWeeklyHBEntitlement) + ", ";
            // WeeklyEligibleRentAmount
            line += summary.get(sAllTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sAllTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sAllTotalCount_WeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sAllAverageWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sHBTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sHBTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sHBTotalCount_WeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sHBAverageWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sCTBTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sCTBTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sCTBTotalCount_WeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sCTBAverageWeeklyEligibleRentAmount) + ", ";
            // WeeklyEligibleCouncilTaxAmount
            line += summary.get(sAllTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sAllTotalCount_WeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sAllAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sHBTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sHBTotalCount_WeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sHBAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sCTBTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero) + ", ";
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
            line += summary.get(sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sAllAverageWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sHBTotalWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sHBAverageWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sCTBTotalWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sCTBAverageWeeklyAdditionalDiscretionaryPayment) + ", ";
            // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
            line += summary.get(sAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            // Council
            line += summary.get(sCouncilTotalWeeklyHBEntitlement) + ", ";
            line += summary.get(sCouncilTotalCount_WeeklyHBEntitlementNonZero) + ", ";
            line += summary.get(sCouncilTotalCount_WeeklyHBEntitlementZero) + ", ";
            line += summary.get(sCouncilAverageWeeklyHBEntitlement) + ", ";
            // WeeklyEligibleRentAmount
            line += summary.get(sCouncilAllTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sCouncilAllTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sCouncilAllTotalCount_WeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sCouncilAllAverageWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sCouncilHBTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sCouncilHBTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sCouncilHBTotalCount_WeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sCouncilHBAverageWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sCouncilCTBTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sCouncilCTBTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sCouncilCTBTotalCount_WeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sCouncilCTBAverageWeeklyEligibleRentAmount) + ", ";
            // WeeklyEligibleCouncilTaxAmount
            line += summary.get(sCouncilAllTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sCouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sCouncilAllTotalCount_WeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sCouncilAllAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sCouncilHBTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sCouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sCouncilHBTotalCount_WeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sCouncilHBAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sCouncilCTBTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sCouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sCouncilCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sCouncilCTBAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            // ContractualRentAmount
            line += summary.get(sCouncilAllTotalContractualRentAmount) + ", ";
            line += summary.get(sCouncilAllTotalCountContractualRentAmountNonZeroCount) + ", ";
            line += summary.get(sCouncilAllTotalCountContractualRentAmountZeroCount) + ", ";
            line += summary.get(sCouncilAllAverageContractualRentAmount) + ", ";
            line += summary.get(sCouncilHBTotalContractualRentAmount) + ", ";
            line += summary.get(sCouncilHBTotalCountContractualRentAmountNonZeroCount) + ", ";
            line += summary.get(sCouncilHBTotalCountContractualRentAmountZeroCount) + ", ";
            line += summary.get(sCouncilHBAverageContractualRentAmount) + ", ";
            line += summary.get(sCouncilCTBTotalContractualRentAmount) + ", ";
            line += summary.get(sCouncilCTBTotalCountContractualRentAmountNonZeroCount) + ", ";
            line += summary.get(sCouncilCTBTotalCountContractualRentAmountZeroCount) + ", ";
            line += summary.get(sCouncilCTBAverageContractualRentAmount) + ", ";
            // WeeklyAdditionalDiscretionaryPayment
            line += summary.get(sCouncilAllTotalWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sCouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sCouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sCouncilAllAverageWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sCouncilHBTotalWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sCouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sCouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sCouncilHBAverageWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sCouncilCTBTotalWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sCouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sCouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sCouncilCTBAverageWeeklyAdditionalDiscretionaryPayment) + ", ";
            // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
            line += summary.get(sCouncilAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sCouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sCouncilAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sCouncilAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sCouncilHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sCouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sCouncilHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sCouncilHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sCouncilCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sCouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sCouncilCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sCouncilCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            // RSL
            line += summary.get(sRSLTotalWeeklyHBEntitlement) + ", ";
            line += summary.get(sRSLTotalCount_WeeklyHBEntitlementNonZero) + ", ";
            line += summary.get(sRSLTotalCount_WeeklyHBEntitlementZero) + ", ";
            line += summary.get(sRSLAverageWeeklyHBEntitlement) + ", ";
            // WeeklyEligibleRentAmount
            line += summary.get(sRSLAllTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sRSLAllTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sRSLAllTotalCount_WeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sRSLAllAverageWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sRSLHBTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sRSLHBTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sRSLHBTotalCount_WeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sRSLHBAverageWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sRSLCTBTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sRSLCTBTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sRSLCTBTotalCount_WeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sRSLCTBAverageWeeklyEligibleRentAmount) + ", ";
            // WeeklyEligibleCouncilTaxAmount
            line += summary.get(sRSLAllTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sRSLAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sRSLAllTotalCount_WeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sRSLAllAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sRSLHBTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sRSLHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sRSLHBTotalCount_WeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sRSLHBAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sRSLCTBTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sRSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sRSLCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sRSLCTBAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            // ContractualRentAmount
            line += summary.get(sRSLAllTotalContractualRentAmount) + ", ";
            line += summary.get(sRSLAllTotalCountContractualRentAmountNonZeroCount) + ", ";
            line += summary.get(sRSLAllTotalCountContractualRentAmountZeroCount) + ", ";
            line += summary.get(sRSLAllAverageContractualRentAmount) + ", ";
            line += summary.get(sRSLHBTotalContractualRentAmount) + ", ";
            line += summary.get(sRSLHBTotalCountContractualRentAmountNonZeroCount) + ", ";
            line += summary.get(sRSLHBTotalCountContractualRentAmountZeroCount) + ", ";
            line += summary.get(sRSLHBAverageContractualRentAmount) + ", ";
            line += summary.get(sRSLCTBTotalContractualRentAmount) + ", ";
            line += summary.get(sRSLCTBTotalCountContractualRentAmountNonZeroCount) + ", ";
            line += summary.get(sRSLCTBTotalCountContractualRentAmountZeroCount) + ", ";
            line += summary.get(sRSLCTBAverageContractualRentAmount) + ", ";
            // WeeklyAdditionalDiscretionaryPayment
            line += summary.get(sRSLAllTotalWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sRSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sRSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sRSLAllAverageWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sRSLHBTotalWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sRSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sRSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sRSLHBAverageWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sRSLCTBTotalWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sRSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sRSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sRSLCTBAverageWeeklyAdditionalDiscretionaryPayment) + ", ";
            // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
            line += summary.get(sRSLAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sRSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sRSLAllTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sRSLAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sRSLHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sRSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sRSLHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sRSLHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sRSLCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sRSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sRSLCTBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sRSLCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    @Override
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
        header += getHeaderSingleTimeGeneric();
        // All UO
        header += sAllTotalCount_ClaimantsEmployed + ", ";
        header += sAllPercentage_ClaimantsEmployed + ", ";
        header += sAllTotalCount_ClaimantsSelfEmployed + ", ";
        header += sAllPercentage_ClaimantsSelfEmployed + ", ";
        header += sAllTotalCount_ClaimantsStudents + ", ";
        header += sAllPercentage_ClaimantsStudents + ", ";
        header += sHBTotalCount_ClaimantsEmployed + ", ";
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
        // Council
        header += sCouncilAllTotalCount_ClaimantsEmployed + ", ";
        header += sCouncilAllPercentage_ClaimantsEmployed + ", ";
        header += sCouncilAllTotalCount_ClaimantsSelfEmployed + ", ";
        header += sCouncilAllPercentage_ClaimantsSelfEmployed + ", ";
        header += sCouncilAllTotalCount_ClaimantsStudents + ", ";
        header += sCouncilAllPercentage_ClaimantsStudents + ", ";
        header += sCouncilHBTotalCount_ClaimantsEmployed + ", ";
        header += sCouncilHBPercentageOfHB_ClaimantsEmployed + ", ";
        header += sCouncilHBTotalCountClaimantsSelfEmployed + ", ";
        header += sCouncilHBPercentageOfHB_ClaimantsSelfEmployed + ", ";
        header += sCouncilHBTotalCountClaimantsStudents + ", ";
        header += sCouncilHBPercentageOfHB_ClaimantsStudents + ", ";
        header += sCouncilCTBTotalCount_ClaimantsEmployed + ", ";
        header += sCouncilCTBPercentageOfCTB_ClaimantsEmployed + ", ";
        header += sCouncilCTBTotalCountClaimantsSelfEmployed + ", ";
        header += sCouncilCTBPercentageOfCTB_ClaimantsSelfEmployed + ", ";
        header += sCouncilCTBTotalCountClaimantsStudents + ", ";
        header += sCouncilCTBPercentageOfCTB_ClaimantsStudents + ", ";
        // RSL
        header += sRSLAllTotalCount_ClaimantsEmployed + ", ";
        header += sRSLAllPercentage_ClaimantsEmployed + ", ";
        header += sRSLAllTotalCount_ClaimantsSelfEmployed + ", ";
        header += sRSLAllPercentage_ClaimantsSelfEmployed + ", ";
        header += sRSLAllTotalCount_ClaimantsStudents + ", ";
        header += sRSLAllPercentage_ClaimantsStudents + ", ";
        header += sRSLHBTotalCount_ClaimantsEmployed + ", ";
        header += sRSLHBPercentageOfHB_ClaimantsEmployed + ", ";
        header += sRSLHBTotalCountClaimantsSelfEmployed + ", ";
        header += sRSLHBPercentageOfHB_ClaimantsSelfEmployed + ", ";
        header += sRSLHBTotalCountClaimantsStudents + ", ";
        header += sRSLHBPercentageOfHB_ClaimantsStudents + ", ";
        header += sRSLCTBTotalCount_ClaimantsEmployed + ", ";
        header += sRSLCTBPercentageOfCTB_ClaimantsEmployed + ", ";
        header += sRSLCTBTotalCountClaimantsSelfEmployed + ", ";
        header += sRSLCTBPercentageOfCTB_ClaimantsSelfEmployed + ", ";
        header += sRSLCTBTotalCountClaimantsStudents + ", ";
        header += sRSLCTBPercentageOfCTB_ClaimantsStudents + ", ";
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
            line += getLineSingleTimeGeneric(key, summary);
            // All UO
            line += summary.get(sAllTotalCount_ClaimantsEmployed) + ", ";
            line += summary.get(sAllPercentage_ClaimantsEmployed) + ", ";
            line += summary.get(sAllTotalCount_ClaimantsSelfEmployed) + ", ";
            line += summary.get(sAllPercentage_ClaimantsSelfEmployed) + ", ";
            line += summary.get(sAllTotalCount_ClaimantsStudents) + ", ";
            line += summary.get(sAllPercentage_ClaimantsStudents) + ", ";
            line += summary.get(sHBTotalCount_ClaimantsEmployed) + ", ";
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
            // Council
            line += summary.get(sCouncilAllTotalCount_ClaimantsEmployed) + ", ";
            line += summary.get(sCouncilAllPercentage_ClaimantsEmployed) + ", ";
            line += summary.get(sCouncilAllTotalCount_ClaimantsSelfEmployed) + ", ";
            line += summary.get(sCouncilAllPercentage_ClaimantsSelfEmployed) + ", ";
            line += summary.get(sCouncilAllTotalCount_ClaimantsStudents) + ", ";
            line += summary.get(sCouncilAllPercentage_ClaimantsStudents) + ", ";
            line += summary.get(sCouncilHBTotalCount_ClaimantsEmployed) + ", ";
            line += summary.get(sCouncilHBPercentageOfHB_ClaimantsEmployed) + ", ";
            line += summary.get(sCouncilHBTotalCountClaimantsSelfEmployed) + ", ";
            line += summary.get(sCouncilHBPercentageOfHB_ClaimantsSelfEmployed) + ", ";
            line += summary.get(sCouncilHBTotalCountClaimantsStudents) + ", ";
            line += summary.get(sCouncilHBPercentageOfHB_ClaimantsStudents) + ", ";
            line += summary.get(sCouncilCTBTotalCount_ClaimantsEmployed) + ", ";
            line += summary.get(sCouncilCTBPercentageOfCTB_ClaimantsEmployed) + ", ";
            line += summary.get(sCouncilCTBTotalCountClaimantsSelfEmployed) + ", ";
            line += summary.get(sCouncilCTBPercentageOfCTB_ClaimantsSelfEmployed) + ", ";
            line += summary.get(sCouncilCTBTotalCountClaimantsStudents) + ", ";
            line += summary.get(sCouncilCTBPercentageOfCTB_ClaimantsStudents) + ", ";
            line += summary.get(sCouncilCTBTotalCount_LHACases) + ", ";
            line += summary.get(sCouncilCTBPercentageOfCTB_LHACases) + ", ";
            // RSL
            line += summary.get(sRSLAllTotalCount_ClaimantsEmployed) + ", ";
            line += summary.get(sRSLAllPercentage_ClaimantsEmployed) + ", ";
            line += summary.get(sRSLAllTotalCount_ClaimantsSelfEmployed) + ", ";
            line += summary.get(sRSLAllPercentage_ClaimantsSelfEmployed) + ", ";
            line += summary.get(sRSLAllTotalCount_ClaimantsStudents) + ", ";
            line += summary.get(sRSLAllPercentage_ClaimantsStudents) + ", ";
            line += summary.get(sRSLHBTotalCount_ClaimantsEmployed) + ", ";
            line += summary.get(sRSLHBPercentageOfHB_ClaimantsEmployed) + ", ";
            line += summary.get(sRSLHBTotalCountClaimantsSelfEmployed) + ", ";
            line += summary.get(sRSLHBPercentageOfHB_ClaimantsSelfEmployed) + ", ";
            line += summary.get(sRSLHBTotalCountClaimantsStudents) + ", ";
            line += summary.get(sRSLHBPercentageOfHB_ClaimantsStudents) + ", ";
            line += summary.get(sRSLCTBTotalCount_ClaimantsEmployed) + ", ";
            line += summary.get(sRSLCTBPercentageOfCTB_ClaimantsEmployed) + ", ";
            line += summary.get(sRSLCTBTotalCountClaimantsSelfEmployed) + ", ";
            line += summary.get(sRSLCTBPercentageOfCTB_ClaimantsSelfEmployed) + ", ";
            line += summary.get(sRSLCTBTotalCountClaimantsStudents) + ", ";
            line += summary.get(sRSLCTBPercentageOfCTB_ClaimantsStudents) + ", ";
            line += summary.get(sRSLCTBTotalCount_LHACases) + ", ";
            line += summary.get(sRSLCTBPercentageOfCTB_LHACases) + ", ";
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    @Override
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
        header += getHeaderSingleTimeGeneric();
        // All UO
        // All
            header += sAllTotalIncome + ", ";
        header += sAllTotalCount_IncomeNonZero + ", ";
        header += sAllTotalCount_IncomeZero + ", ";
        header += sAllAverageIncome + ", ";
        header += sAllTotalWeeklyEligibleRentAmount + ", ";
        header += sAllTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sAllAverageWeeklyEligibleRentAmount + ", ";
        // HB
        header += sHBTotalIncome + ", ";
        header += sHBTotalCount_IncomeNonZero + ", ";
        header += sHBTotalCount_IncomeZero + ", ";
        header += sHBAverageIncome + ", ";
        header += sHBTotalWeeklyEligibleRentAmount + ", ";
        header += sHBTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sHBAverageWeeklyEligibleRentAmount + ", ";
        // CTB
        header += sCTBTotalIncome + ", ";
        header += sCTBTotalCount_IncomeNonZero + ", ";
        header += sCTBTotalCount_IncomeZero + ", ";
        header += sCTBAverageIncome + ", ";
        header += sCTBTotalWeeklyEligibleRentAmount + ", ";
        header += sCTBTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sCTBAverageWeeklyEligibleRentAmount + ", ";
        for (int i = 1; i < nTT; i++) {
            header += sTotalIncomeTT[i] + ", ";
            header += sTotalCount_IncomeNonZeroTT[i] + ", ";
            header += sTotalCount_IncomeZeroTT[i] + ", ";
            header += sAverageIncomeTT[i] + ", ";
            header += sTotalWeeklyEligibleRentAmountTT[i] + ", ";
            header += sTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] + ", ";
            header += sAverageWeeklyEligibleRentAmountTT[i] + ", ";
        }
        // Council
        // All
            header += sCouncilAllTotalIncome + ", ";
        header += sCouncilAllTotalCount_IncomeNonZero + ", ";
        header += sCouncilAllTotalCount_IncomeZero + ", ";
        header += sCouncilAllAverageIncome + ", ";
        header += sCouncilAllTotalWeeklyEligibleRentAmount + ", ";
        header += sCouncilAllTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sCouncilAllAverageWeeklyEligibleRentAmount + ", ";
        // HB
        header += sCouncilHBTotalIncome + ", ";
        header += sCouncilHBTotalCount_IncomeNonZero + ", ";
        header += sCouncilHBTotalCount_IncomeZero + ", ";
        header += sCouncilHBAverageIncome + ", ";
        header += sCouncilHBTotalWeeklyEligibleRentAmount + ", ";
        header += sCouncilHBTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sCouncilHBAverageWeeklyEligibleRentAmount + ", ";
        // CTB
        header += sCouncilCTBTotalIncome + ", ";
        header += sCouncilCTBTotalCount_IncomeNonZero + ", ";
        header += sCouncilCTBTotalCount_IncomeZero + ", ";
        header += sCouncilCTBAverageIncome + ", ";
        header += sCouncilCTBTotalWeeklyEligibleRentAmount + ", ";
        header += sCouncilCTBTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sCouncilCTBAverageWeeklyEligibleRentAmount + ", ";
        for (int i = 1; i < nTT; i++) {
            header += sCouncilTotalIncomeTT[i] + ", ";
            header += sCouncilTotalCount_IncomeNonZeroTT[i] + ", ";
            header += sCouncilTotalCount_IncomeZeroTT[i] + ", ";
            header += sCouncilAverageIncomeTT[i] + ", ";
            header += sCouncilTotalWeeklyEligibleRentAmountTT[i] + ", ";
            header += sCouncilTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] + ", ";
            header += sCouncilAverageWeeklyEligibleRentAmountTT[i] + ", ";
        }
        // RSL
        // All
            header += sRSLAllTotalIncome + ", ";
        header += sRSLAllTotalCount_IncomeNonZero + ", ";
        header += sRSLAllTotalCount_IncomeZero + ", ";
        header += sRSLAllAverageIncome + ", ";
        header += sRSLAllTotalWeeklyEligibleRentAmount + ", ";
        header += sRSLAllTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sRSLAllAverageWeeklyEligibleRentAmount + ", ";
        // HB
        header += sRSLHBTotalIncome + ", ";
        header += sRSLHBTotalCount_IncomeNonZero + ", ";
        header += sRSLHBTotalCount_IncomeZero + ", ";
        header += sRSLHBAverageIncome + ", ";
        header += sRSLHBTotalWeeklyEligibleRentAmount + ", ";
        header += sRSLHBTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sRSLHBAverageWeeklyEligibleRentAmount + ", ";
        // CTB
        header += sRSLCTBTotalIncome + ", ";
        header += sRSLCTBTotalCount_IncomeNonZero + ", ";
        header += sRSLCTBTotalCount_IncomeZero + ", ";
        header += sRSLCTBAverageIncome + ", ";
        header += sRSLCTBTotalWeeklyEligibleRentAmount + ", ";
        header += sRSLCTBTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sRSLCTBAverageWeeklyEligibleRentAmount + ", ";
        for (int i = 1; i < nTT; i++) {
            header += sRSLTotalIncomeTT[i] + ", ";
            header += sRSLTotalCount_IncomeNonZeroTT[i] + ", ";
            header += sRSLTotalCount_IncomeZeroTT[i] + ", ";
            header += sRSLAverageIncomeTT[i] + ", ";
            header += sRSLTotalWeeklyEligibleRentAmountTT[i] + ", ";
            header += sRSLTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] + ", ";
            header += sRSLAverageWeeklyEligibleRentAmountTT[i] + ", ";
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
            line += getLineSingleTimeGeneric(key, summary);
                        // All UO
            // All
            line += summary.get(sAllTotalIncome) + ", ";
            line += summary.get(sAllTotalCount_IncomeNonZero) + ", ";
            line += summary.get(sAllTotalCount_IncomeZero) + ", ";
            line += summary.get(sAllAverageIncome) + ", ";
            line += summary.get(sAllTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sAllTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sAllAverageWeeklyEligibleRentAmount) + ", ";
            // HB
            line += summary.get(sHBTotalIncome) + ", ";
            line += summary.get(sHBTotalCount_IncomeNonZero) + ", ";
            line += summary.get(sHBTotalCount_IncomeZero) + ", ";
            line += summary.get(sHBAverageIncome) + ", ";
            line += summary.get(sHBTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sHBTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sHBAverageWeeklyEligibleRentAmount) + ", ";
            // CTB
            line += summary.get(sCTBTotalIncome) + ", ";
            line += summary.get(sCTBTotalCount_IncomeNonZero) + ", ";
            line += summary.get(sCTBTotalCount_IncomeZero) + ", ";
            line += summary.get(sCTBAverageIncome) + ", ";
            line += summary.get(sCTBTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sCTBTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sCTBAverageWeeklyEligibleRentAmount) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sTotalIncomeTT[i]) + ", ";
                line += summary.get(sTotalCount_IncomeNonZeroTT[i]) + ", ";
                line += summary.get(sTotalCount_IncomeZeroTT[i]) + ", ";
                line += summary.get(sAverageIncomeTT[i]) + ", ";
                line += summary.get(sTotalWeeklyEligibleRentAmountTT[i]) + ", ";
                line += summary.get(sTotalCount_WeeklyEligibleRentAmountNonZeroTT[i]) + ", ";
                line += summary.get(sAverageWeeklyEligibleRentAmountTT[i]) + ", ";
            }
            // Council
            // All
            line += summary.get(sCouncilAllTotalIncome) + ", ";
            line += summary.get(sCouncilAllTotalCount_IncomeNonZero) + ", ";
            line += summary.get(sCouncilAllTotalCount_IncomeZero) + ", ";
            line += summary.get(sCouncilAllAverageIncome) + ", ";
            line += summary.get(sCouncilAllTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sCouncilAllTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sCouncilAllAverageWeeklyEligibleRentAmount) + ", ";
            // HB
            line += summary.get(sCouncilHBTotalIncome) + ", ";
            line += summary.get(sCouncilHBTotalCount_IncomeNonZero) + ", ";
            line += summary.get(sCouncilHBTotalCount_IncomeZero) + ", ";
            line += summary.get(sCouncilHBAverageIncome) + ", ";
            line += summary.get(sCouncilHBTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sCouncilHBTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sCouncilHBAverageWeeklyEligibleRentAmount) + ", ";
            // CTB
            line += summary.get(sCouncilCTBTotalIncome) + ", ";
            line += summary.get(sCouncilCTBTotalCount_IncomeNonZero) + ", ";
            line += summary.get(sCouncilCTBTotalCount_IncomeZero) + ", ";
            line += summary.get(sCouncilCTBAverageIncome) + ", ";
            line += summary.get(sCouncilCTBTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sCouncilCTBTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sCouncilCTBAverageWeeklyEligibleRentAmount) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sCouncilTotalIncomeTT[i]) + ", ";
                line += summary.get(sCouncilTotalCount_IncomeNonZeroTT[i]) + ", ";
                line += summary.get(sCouncilTotalCount_IncomeZeroTT[i]) + ", ";
                line += summary.get(sCouncilAverageIncomeTT[i]) + ", ";
                line += summary.get(sCouncilTotalWeeklyEligibleRentAmountTT[i]) + ", ";
                line += summary.get(sCouncilTotalCount_WeeklyEligibleRentAmountNonZeroTT[i]) + ", ";
                line += summary.get(sCouncilAverageWeeklyEligibleRentAmountTT[i]) + ", ";
            }
            // RSL
            // All
            line += summary.get(sRSLAllTotalIncome) + ", ";
            line += summary.get(sRSLAllTotalCount_IncomeNonZero) + ", ";
            line += summary.get(sRSLAllTotalCount_IncomeZero) + ", ";
            line += summary.get(sRSLAllAverageIncome) + ", ";
            line += summary.get(sRSLAllTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sRSLAllTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sRSLAllAverageWeeklyEligibleRentAmount) + ", ";
            // HB
            line += summary.get(sRSLHBTotalIncome) + ", ";
            line += summary.get(sRSLHBTotalCount_IncomeNonZero) + ", ";
            line += summary.get(sRSLHBTotalCount_IncomeZero) + ", ";
            line += summary.get(sRSLHBAverageIncome) + ", ";
            line += summary.get(sRSLHBTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sRSLHBTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sRSLHBAverageWeeklyEligibleRentAmount) + ", ";
            // CTB
            line += summary.get(sRSLCTBTotalIncome) + ", ";
            line += summary.get(sRSLCTBTotalCount_IncomeNonZero) + ", ";
            line += summary.get(sRSLCTBTotalCount_IncomeZero) + ", ";
            line += summary.get(sRSLCTBAverageIncome) + ", ";
            line += summary.get(sRSLCTBTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sRSLCTBTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sRSLCTBAverageWeeklyEligibleRentAmount) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sRSLTotalIncomeTT[i]) + ", ";
                line += summary.get(sRSLTotalCount_IncomeNonZeroTT[i]) + ", ";
                line += summary.get(sRSLTotalCount_IncomeZeroTT[i]) + ", ";
                line += summary.get(sRSLAverageIncomeTT[i]) + ", ";
                line += summary.get(sRSLTotalWeeklyEligibleRentAmountTT[i]) + ", ";
                line += summary.get(sRSLTotalCount_WeeklyEligibleRentAmountNonZeroTT[i]) + ", ";
                line += summary.get(sRSLAverageWeeklyEligibleRentAmountTT[i]) + ", ";
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
        header += getHeaderSingleTimeGeneric();
        header += sCouncilTotal_RentArrears + ", ";
        header += sCouncilTotalCount_RentArrearsNonZero + ", ";
        header += sCouncilTotalCount_RentArrearsZero + ", ";
        header += sCouncilAverage_RentArrears + ", ";
        header += sCouncilAverage_NonZeroRentArrears + ", ";
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
            line += getLineSingleTimeGeneric(key, summary);
            line += summary.get(sCouncilTotal_RentArrears) + ", ";
            line += summary.get(sCouncilTotalCount_RentArrearsNonZero) + ", ";
            line += summary.get(sCouncilTotalCount_RentArrearsZero) + ", ";
            line += summary.get(sCouncilAverage_RentArrears) + ", ";
            line += summary.get(sCouncilAverage_NonZeroRentArrears) + ", ";
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    @Override
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
        header += getHeaderSingleTimeGeneric();
        // All UO
        for (int i = 1; i < nEG; i++) {
            header += sAllTotalCount_EthnicGroupClaimant[i] + ", ";
            header += sAllPercentageOfAll_EthnicGroupClaimant[i] + ", ";
        }
        for (int i = 1; i < nEG; i++) {
            header += sHBTotalCount_EthnicGroupClaimant[i] + ", ";
            header += sHBPercentageOfHB_EthnicGroupClaimant[i] + ", ";
        }
        for (int i = 1; i < nEG; i++) {
            header += sCTBTotalCount_EthnicGroupClaimant[i] + ", ";
            header += sCTBPercentageOfCTB_EthnicGroupClaimant[i] + ", ";
        }
        // Council
        for (int i = 1; i < nEG; i++) {
            header += sCouncilAllTotalCount_EthnicGroupClaimant[i] + ", ";
            header += sCouncilAllPercentageOfAll_EthnicGroupClaimant[i] + ", ";
        }
        for (int i = 1; i < nEG; i++) {
            header += sCouncilHBTotalCount_EthnicGroupClaimant[i] + ", ";
            header += sCouncilHBPercentageOfHB_EthnicGroupClaimant[i] + ", ";
        }
        for (int i = 1; i < nEG; i++) {
            header += sCouncilCTBTotalCount_EthnicGroupClaimant[i] + ", ";
            header += sCouncilCTBPercentageOfCTB_EthnicGroupClaimant[i] + ", ";
        }
        // RSL
        for (int i = 1; i < nEG; i++) {
            header += sRSLAllTotalCount_EthnicGroupClaimant[i] + ", ";
            header += sRSLAllPercentageOfAll_EthnicGroupClaimant[i] + ", ";
        }
        for (int i = 1; i < nEG; i++) {
            header += sRSLHBTotalCount_EthnicGroupClaimant[i] + ", ";
            header += sRSLHBPercentageOfHB_EthnicGroupClaimant[i] + ", ";
        }
        for (int i = 1; i < nEG; i++) {
            header += sRSLCTBTotalCount_EthnicGroupClaimant[i] + ", ";
            header += sRSLCTBPercentageOfCTB_EthnicGroupClaimant[i] + ", ";
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
            line += getLineSingleTimeGeneric(key, summary);
            // All UO
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sAllTotalCount_EthnicGroupClaimant[i]) + ", ";
                line += summary.get(sAllPercentageOfAll_EthnicGroupClaimant[i]) + ", ";
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sHBTotalCount_EthnicGroupClaimant[i]) + ", ";
                line += summary.get(sHBPercentageOfHB_EthnicGroupClaimant[i]) + ", ";
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sCTBTotalCount_EthnicGroupClaimant[i]) + ", ";
                line += summary.get(sCTBPercentageOfCTB_EthnicGroupClaimant[i]) + ", ";
            }
            // Council
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sCouncilAllTotalCount_EthnicGroupClaimant[i]) + ", ";
                line += summary.get(sCouncilAllPercentageOfAll_EthnicGroupClaimant[i]) + ", ";
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sCouncilHBTotalCount_EthnicGroupClaimant[i]) + ", ";
                line += summary.get(sCouncilHBPercentageOfHB_EthnicGroupClaimant[i]) + ", ";
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sCouncilCTBTotalCount_EthnicGroupClaimant[i]) + ", ";
                line += summary.get(sCouncilCTBPercentageOfCTB_EthnicGroupClaimant[i]) + ", ";
            }
            // RSL
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sRSLAllTotalCount_EthnicGroupClaimant[i]) + ", ";
                line += summary.get(sRSLAllPercentageOfAll_EthnicGroupClaimant[i]) + ", ";
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sRSLHBTotalCount_EthnicGroupClaimant[i]) + ", ";
                line += summary.get(sRSLHBPercentageOfHB_EthnicGroupClaimant[i]) + ", ";
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sRSLCTBTotalCount_EthnicGroupClaimant[i]) + ", ";
                line += summary.get(sRSLCTBPercentageOfCTB_EthnicGroupClaimant[i]) + ", ";
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    @Override
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
        header += getHeaderSingleTimeGeneric();
        // All UO
        header += sTotalCount_SocialTTsClaimant + ", ";
        header += sPercentageOfAll_SocialTTsClaimant + ", ";
        header += sPercentageOfHB_SocialTTsClaimant + ", ";
        header += sTotalCount_PrivateDeregulatedTTsClaimant + ", ";
        header += sPercentageOfAll_PrivateDeregulatedTTsClaimant + ", ";
        header += sPercentageOfHB_PrivateDeregulatedTTsClaimant + ", ";
        for (int i = 1; i < nTT; i++) {
            header += sTotalCount_ClaimantTT[i] + ", ";
            header += sPercentageOfAll_ClaimantTT[i] + ", ";
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
        // Council
        header += sCouncilTotalCount_SocialTTsClaimant + ", ";
        header += sCouncilPercentageOfAll_SocialTTsClaimant + ", ";
        header += sCouncilPercentageOfHB_SocialTTsClaimant + ", ";
        header += sCouncilTotalCount_PrivateDeregulatedTTsClaimant + ", ";
        header += sCouncilPercentageOfAll_PrivateDeregulatedTTsClaimant + ", ";
        header += sCouncilPercentageOfHB_PrivateDeregulatedTTsClaimant + ", ";
        for (int i = 1; i < nTT; i++) {
            header += sCouncilTotalCount_ClaimantTT[i] + ", ";
            header += sCouncilPercentageOfAll_ClaimantTT[i] + ", ";
            if ((i == 5 || i == 7)) {
                header += sCouncilPercentageOfCTB_ClaimantTT[i] + ", ";
            } else {
                header += sCouncilPercentageOfHB_ClaimantTT[i] + ", ";
            }
        }
        header += sCouncilAllTotalCount_LHACases + ", ";
        header += sCouncilAllPercentageOfAll_LHACases + ", ";
        header += sCouncilHBTotalCount_LHACases + ", ";
        header += sCouncilHBPercentageOfHB_LHACases + ", ";
        header += sCouncilCTBTotalCount_LHACases + ", ";
        header += sCouncilCTBPercentageOfCTB_LHACases + ", ";
        // RSL
        header += sRSLTotalCount_SocialTTsClaimant + ", ";
        header += sRSLPercentageOfAll_SocialTTsClaimant + ", ";
        header += sRSLPercentageOfHB_SocialTTsClaimant + ", ";
        header += sRSLTotalCount_PrivateDeregulatedTTsClaimant + ", ";
        header += sRSLPercentageOfAll_PrivateDeregulatedTTsClaimant + ", ";
        header += sRSLPercentageOfHB_PrivateDeregulatedTTsClaimant + ", ";
        for (int i = 1; i < nTT; i++) {
            header += sRSLTotalCount_ClaimantTT[i] + ", ";
            header += sRSLPercentageOfAll_ClaimantTT[i] + ", ";
            if ((i == 5 || i == 7)) {
                header += sRSLPercentageOfCTB_ClaimantTT[i] + ", ";
            } else {
                header += sRSLPercentageOfHB_ClaimantTT[i] + ", ";
            }
        }
        header += sRSLAllTotalCount_LHACases + ", ";
        header += sRSLAllPercentageOfAll_LHACases + ", ";
        header += sRSLHBTotalCount_LHACases + ", ";
        header += sRSLHBPercentageOfHB_LHACases + ", ";
        header += sRSLCTBTotalCount_LHACases + ", ";
        header += sRSLCTBPercentageOfCTB_LHACases + ", ";
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
            line += getLineSingleTimeGeneric(key, summary);
            // All UO
            line += summary.get(sTotalCount_SocialTTsClaimant) + ", ";
            line += summary.get(sPercentageOfAll_SocialTTsClaimant) + ", ";
            line += summary.get(sPercentageOfHB_SocialTTsClaimant) + ", ";
            line += summary.get(sTotalCount_PrivateDeregulatedTTsClaimant) + ", ";
            line += summary.get(sPercentageOfAll_PrivateDeregulatedTTsClaimant) + ", ";
            line += summary.get(sPercentageOfHB_PrivateDeregulatedTTsClaimant) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sTotalCount_ClaimantTT[i]) + ", ";
                line += summary.get(sPercentageOfAll_ClaimantTT[i]) + ", ";
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
            // Council
            line += summary.get(sCouncilTotalCount_SocialTTsClaimant) + ", ";
            line += summary.get(sCouncilPercentageOfAll_SocialTTsClaimant) + ", ";
            line += summary.get(sCouncilPercentageOfHB_SocialTTsClaimant) + ", ";
            line += summary.get(sCouncilTotalCount_PrivateDeregulatedTTsClaimant) + ", ";
            line += summary.get(sCouncilPercentageOfAll_PrivateDeregulatedTTsClaimant) + ", ";
            line += summary.get(sCouncilPercentageOfHB_PrivateDeregulatedTTsClaimant) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sCouncilTotalCount_ClaimantTT[i]) + ", ";
                line += summary.get(sCouncilPercentageOfAll_ClaimantTT[i]) + ", ";
                if ((i == 5 || i == 7)) {
                    line += summary.get(sCouncilPercentageOfCTB_ClaimantTT[i]) + ", ";
                } else {
                    line += summary.get(sCouncilPercentageOfHB_ClaimantTT[i]) + ", ";
                }
            }
            line += summary.get(sCouncilAllTotalCount_LHACases) + ", ";
            line += summary.get(sCouncilAllPercentageOfAll_LHACases) + ", ";
            line += summary.get(sCouncilHBTotalCount_LHACases) + ", ";
            line += summary.get(sCouncilHBPercentageOfHB_LHACases) + ", ";
            line += summary.get(sCouncilCTBTotalCount_LHACases) + ", ";
            line += summary.get(sCouncilCTBPercentageOfCTB_LHACases) + ", ";
            // RSL
            line += summary.get(sRSLTotalCount_SocialTTsClaimant) + ", ";
            line += summary.get(sRSLPercentageOfAll_SocialTTsClaimant) + ", ";
            line += summary.get(sRSLPercentageOfHB_SocialTTsClaimant) + ", ";
            line += summary.get(sRSLTotalCount_PrivateDeregulatedTTsClaimant) + ", ";
            line += summary.get(sRSLPercentageOfAll_PrivateDeregulatedTTsClaimant) + ", ";
            line += summary.get(sRSLPercentageOfHB_PrivateDeregulatedTTsClaimant) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sRSLTotalCount_ClaimantTT[i]) + ", ";
                line += summary.get(sRSLPercentageOfAll_ClaimantTT[i]) + ", ";
                if ((i == 5 || i == 7)) {
                    line += summary.get(sRSLPercentageOfCTB_ClaimantTT[i]) + ", ";
                } else {
                    line += summary.get(sRSLPercentageOfHB_ClaimantTT[i]) + ", ";
                }
            }
            line += summary.get(sRSLAllTotalCount_LHACases) + ", ";
            line += summary.get(sRSLAllPercentageOfAll_LHACases) + ", ";
            line += summary.get(sRSLHBTotalCount_LHACases) + ", ";
            line += summary.get(sRSLHBPercentageOfHB_LHACases) + ", ";
            line += summary.get(sRSLCTBTotalCount_LHACases) + ", ";
            line += summary.get(sRSLCTBPercentageOfCTB_LHACases) + ", ";
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    @Override
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
        header += getHeaderSingleTimeGeneric();
        // All UO
        for (int i = 1; i < nPSI; i++) {
            header += sAllTotalCount_PSI[i] + ", ";
            header += sAllPercentageOfAll_PSI[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            header += sHBTotalCount_PSI[i] + ", ";
            header += sHBPercentageOfHB_PSI[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            header += sCTBTotalCount_PSI[i] + ", ";
            header += sCTBPercentageOfCTB_PSI[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            for (int j = 1; j < nTT; j++) {
                header += sTotalCount_PSIByTT[i][j] + ", ";
                header += sPercentageOfAll_PSIByTT[i][j] + ", ";
            }
        }
        for (int i = 1; i < nPSI; i++) {
            for (int j = 1; j < nTT; j++) {
                header += sTotalCount_PSIByTT[i][j] + ", ";
                if (j == 5 || j == 7) {
                    header += sPercentageOfCTB_PSIByTT[i][j] + ", ";
                } else {
                    header += sPercentageOfHB_PSIByTT[i][j] + ", ";
                }
            }
        }
        // Council
        for (int i = 1; i < nPSI; i++) {
            header += sCouncilAllTotalCount_PSI[i] + ", ";
            header += sCouncilAllPercentageOfAll_PSI[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            header += sCouncilHBTotalCount_PSI[i] + ", ";
            header += sCouncilHBPercentageOfHB_PSI[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            header += sCouncilCTBTotalCount_PSI[i] + ", ";
            header += sCouncilCTBPercentageOfCTB_PSI[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            for (int j = 1; j < nTT; j++) {
                header += sCouncilTotalCount_PSIByTT[i][j] + ", ";
                header += sCouncilPercentageOfAll_PSIByTT[i][j] + ", ";
            }
        }
        for (int i = 1; i < nPSI; i++) {
            for (int j = 1; j < nTT; j++) {
                header += sCouncilTotalCount_PSIByTT[i][j] + ", ";
                if (j == 5 || j == 7) {
                    header += sCouncilPercentageOfCTB_PSIByTT[i][j] + ", ";
                } else {
                    header += sCouncilPercentageOfHB_PSIByTT[i][j] + ", ";
                }
            }
        }
        // RSL
        for (int i = 1; i < nPSI; i++) {
            header += sRSLAllTotalCount_PSI[i] + ", ";
            header += sRSLAllPercentageOfAll_PSI[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            header += sRSLHBTotalCount_PSI[i] + ", ";
            header += sRSLHBPercentageOfHB_PSI[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            header += sRSLCTBTotalCount_PSI[i] + ", ";
            header += sRSLCTBPercentageOfCTB_PSI[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            for (int j = 1; j < nTT; j++) {
                header += sRSLTotalCount_PSIByTT[i][j] + ", ";
                header += sRSLPercentageOfAll_PSIByTT[i][j] + ", ";
            }
        }
        for (int i = 1; i < nPSI; i++) {
            for (int j = 1; j < nTT; j++) {
                header += sRSLTotalCount_PSIByTT[i][j] + ", ";
                if (j == 5 || j == 7) {
                    header += sRSLPercentageOfCTB_PSIByTT[i][j] + ", ";
                } else {
                    header += sRSLPercentageOfHB_PSIByTT[i][j] + ", ";
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
            line += getLineSingleTimeGeneric(key, summary);
            // All UO
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sAllTotalCount_PSI[i]) + ", ";
                line += summary.get(sAllPercentageOfAll_PSI[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sHBTotalCount_PSI[i]) + ", ";
                line += summary.get(sHBPercentageOfHB_PSI[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sCTBTotalCount_PSI[i]) + ", ";
                line += summary.get(sCTBPercentageOfCTB_PSI[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                for (int j = 1; j < nTT; j++) {
                    line += summary.get(sTotalCount_PSIByTT[i][j]) + ", ";
                    line += summary.get(sPercentageOfAll_PSIByTT[i][j]) + ", ";
                }
            }
            for (int i = 1; i < nPSI; i++) {
                for (int j = 1; j < nTT; j++) {
                    line += summary.get(sTotalCount_PSIByTT[i][j]) + ", ";
                    if (j == 5 || j == 7) {
                        line += summary.get(sPercentageOfCTB_PSIByTT[i][j]) + ", ";
                    } else {
                        line += summary.get(sPercentageOfHB_PSIByTT[i][j]) + ", ";
                    }
                }
            }
            // Council
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sCouncilAllTotalCount_PSI[i]) + ", ";
                line += summary.get(sCouncilAllPercentageOfAll_PSI[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sCouncilHBTotalCount_PSI[i]) + ", ";
                line += summary.get(sCouncilHBPercentageOfHB_PSI[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sCouncilCTBTotalCount_PSI[i]) + ", ";
                line += summary.get(sCouncilCTBPercentageOfCTB_PSI[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                for (int j = 1; j < nTT; j++) {
                    line += summary.get(sCouncilTotalCount_PSIByTT[i][j]) + ", ";
                    line += summary.get(sCouncilPercentageOfAll_PSIByTT[i][j]) + ", ";
                }
            }
            for (int i = 1; i < nPSI; i++) {
                for (int j = 1; j < nTT; j++) {
                    line += summary.get(sCouncilTotalCount_PSIByTT[i][j]) + ", ";
                    if (j == 5 || j == 7) {
                        line += summary.get(sCouncilPercentageOfCTB_PSIByTT[i][j]) + ", ";
                    } else {
                        line += summary.get(sCouncilPercentageOfHB_PSIByTT[i][j]) + ", ";
                    }
                }
            }
            // RSL
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sRSLAllTotalCount_PSI[i]) + ", ";
                line += summary.get(sRSLAllPercentageOfAll_PSI[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sRSLHBTotalCount_PSI[i]) + ", ";
                line += summary.get(sRSLHBPercentageOfHB_PSI[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sRSLCTBTotalCount_PSI[i]) + ", ";
                line += summary.get(sRSLCTBPercentageOfCTB_PSI[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                for (int j = 1; j < nTT; j++) {
                    line += summary.get(sRSLTotalCount_PSIByTT[i][j]) + ", ";
                    line += summary.get(sRSLPercentageOfAll_PSIByTT[i][j]) + ", ";
                }
            }
            for (int i = 1; i < nPSI; i++) {
                for (int j = 1; j < nTT; j++) {
                    line += summary.get(sRSLTotalCount_PSIByTT[i][j]) + ", ";
                    if (j == 5 || j == 7) {
                        line += summary.get(sRSLPercentageOfCTB_PSIByTT[i][j]) + ", ";
                    } else {
                        line += summary.get(sRSLPercentageOfHB_PSIByTT[i][j]) + ", ";
                    }
                }
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    @Override
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
        header += getHeaderSingleTimeGeneric();
        // All UO
        // General
        // DisabilityAward
        header += sTotalCount_DisabilityAward + ", ";
        header += sPercentageOfAll_DisabilityAward + ", ";
        header += sTotalCount_DisabilityAwardHBTTs + ", ";
        header += sPercentageOfAll_DisabilityAwardHBTTs + ", ";
        header += sPercentageOfHB_DisabilityAwardHBTTs + ", ";
        header += sTotalCount_DisabilityAwardCTBTTs + ", ";
        header += sPercentageOfAll_DisabilityAwardCTBTTs + ", ";
        header += sPercentageOfCTB_DisabilityAwardCTBTTs + ", ";
        // DisabilityPremiumAward
        header += sTotalCount_DisabilityPremiumAward + ", ";
        header += sPercentageOfAll_DisabilityPremiumAward + ", ";
        header += sTotalCount_DisabilityPremiumAwardHBTTs + ", ";
        header += sPercentageOfAll_DisabilityPremiumAwardHBTTs + ", ";
        header += sPercentageOfHB_DisabilityPremiumAwardHBTTs + ", ";
        header += sTotalCount_DisabilityPremiumAwardCTBTTs + ", ";
        header += sPercentageOfAll_DisabilityPremiumAwardCTBTTs + ", ";
        header += sPercentageOfCTB_DisabilityPremiumAwardCTBTTs + ", ";
        // SevereDisabilityPremiumAward
        header += sTotalCount_SevereDisabilityPremiumAward + ", ";
        header += sPercentageOfAll_SevereDisabilityPremiumAward + ", ";
        header += sTotalCount_SevereDisabilityPremiumAwardHBTTs + ", ";
        header += sPercentageOfAll_SevereDisabilityPremiumAwardHBTTs + ", ";
        header += sPercentageOfHB_SevereDisabilityPremiumAwardHBTTs + ", ";
        header += sTotalCount_SevereDisabilityPremiumAwardCTBTTs + ", ";
        header += sPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs + ", ";
        header += sPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs + ", ";
        // DisabledChildPremiumAward
        header += sTotalCount_DisabledChildPremiumAward + ", ";
        header += sPercentageOfAll_DisabledChildPremiumAward + ", ";
        header += sTotalCount_DisabledChildPremiumAwardHBTTs + ", ";
        header += sPercentageOfAll_DisabledChildPremiumAwardHBTTs + ", ";
        header += sPercentageOfHB_DisabledChildPremiumAwardHBTTs + ", ";
        header += sTotalCount_DisabledChildPremiumAwardCTBTTs + ", ";
        header += sPercentageOfAll_DisabledChildPremiumAwardCTBTTs + ", ";
        header += sPercentageOfCTB_DisabledChildPremiumAwardCTBTTs + ", ";
        // EnhancedDisabilityPremiumAward
        header += sTotalCount_EnhancedDisabilityPremiumAward + ", ";
        header += sPercentageOfAll_EnhancedDisabilityPremiumAward + ", ";
        header += sTotalCount_EnhancedDisabilityPremiumAwardHBTTs + ", ";
        header += sPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs + ", ";
        header += sPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs + ", ";
        header += sTotalCount_EnhancedDisabilityPremiumAwardCTBTTs + ", ";
        header += sPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs + ", ";
        header += sPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs + ", ";
        // SocialTTs
        // DisabilityAward
        header += sTotalCount_DisabilityAwardSocialTTs + ", ";
        header += sPercentageOfAll_DisabilityAwardSocialTTs + ", ";
        header += sPercentageOfHB_DisabilityAwardSocialTTs + ", ";
        header += sPercentageOfSocialTTs_DisabilityAwardSocialTTs + ", ";
        // DisabilityPremiumAward
        header += sTotalCount_DisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfAll_DisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfHB_DisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs + ", ";
        // SevereDisabilityPremiumAward
        header += sTotalCount_SevereDisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs + ", ";
        // DisabledChildPremiumAward
        header += sTotalCount_DisabledChildPremiumAwardSocialTTs + ", ";
        header += sPercentageOfAll_DisabledChildPremiumAwardSocialTTs + ", ";
        header += sPercentageOfHB_DisabledChildPremiumAwardSocialTTs + ", ";
        header += sPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs + ", ";
        // EnhancedDisabilityPremiumAward
        header += sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs + ", ";
        // PrivateDeregulatedTTs
        // DisabilityAward
        header += sTotalCount_DisabilityAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs + ", ";
        // DisabilityPremiumAward
        header += sTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        // SevereDisabilityPremiumAward
        header += sTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        // DisabledChildPremiumAward
        header += sTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs + ", ";
        // EnhancedDisabilityPremiumAward
        header += sTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        for (int i = 1; i < nTT; i++) {
            // DisabilityAward
            header += sTotalCount_DisabilityAwardByTT[i] + ", ";
            header += sPercentageOfAll_DisabilityAwardByTT[i] + ", ";
            if (i == 5 || i == 7) {
                header += sPercentageOfCTB_DisabilityAwardByTT[i] + ", ";
            } else {
                header += sPercentageOfHB_DisabilityAwardByTT[i] + ", ";
            }
            header += sPercentageOfTT_DisabilityAwardByTT[i] + ", ";
            // DisabilityPremiumAward
            header += sTotalCount_DisabilityPremiumAwardByTT[i] + ", ";
            header += sPercentageOfAll_DisabilityPremiumAwardByTT[i] + ", ";
            if (i == 5 || i == 7) {
                header += sPercentageOfCTB_DisabilityPremiumAwardByTT[i] + ", ";
            } else {
                header += sPercentageOfHB_DisabilityPremiumAwardByTT[i] + ", ";
            }
            header += sPercentageOfTT_DisabilityPremiumAwardByTT[i] + ", ";
            // SevereDisabilityPremiumAward
            header += sTotalCount_SevereDisabilityPremiumAwardByTT[i] + ", ";
            header += sPercentageOfAll_SevereDisabilityPremiumAwardByTT[i] + ", ";
            if (i == 5 || i == 7) {
                header += sPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i] + ", ";
            } else {
                header += sPercentageOfHB_SevereDisabilityPremiumAwardByTT[i] + ", ";
            }
            header += sPercentageOfTT_SevereDisabilityPremiumAwardByTT[i] + ", ";
            // DisabledChildPremiumAward
            header += sTotalCount_DisabledChildPremiumAwardByTT[i] + ", ";
            header += sPercentageOfAll_DisabledChildPremiumAwardByTT[i] + ", ";
            if (i == 5 || i == 7) {
                header += sPercentageOfCTB_DisabledChildPremiumAwardByTT[i] + ", ";
            } else {
                header += sPercentageOfHB_DisabledChildPremiumAwardByTT[i] + ", ";
            }
            header += sPercentageOfTT_DisabledChildPremiumAwardByTT[i] + ", ";
            // EnhancedDisabilityPremiumAward
            header += sTotalCount_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
            header += sPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
            if (i == 5 || i == 7) {
                header += sPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
            } else {
                header += sPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
            }
            header += sPercentageOfTT_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
        }
        // Council
        // General
        // DisabilityAward
        header += sCouncilTotalCount_DisabilityAward + ", ";
        header += sCouncilPercentageOfAll_DisabilityAward + ", ";
        header += sCouncilTotalCount_DisabilityAwardHBTTs + ", ";
        header += sCouncilPercentageOfAll_DisabilityAwardHBTTs + ", ";
        header += sCouncilPercentageOfHB_DisabilityAwardHBTTs + ", ";
        header += sCouncilTotalCount_DisabilityAwardCTBTTs + ", ";
        header += sCouncilPercentageOfAll_DisabilityAwardCTBTTs + ", ";
        header += sCouncilPercentageOfCTB_DisabilityAwardCTBTTs + ", ";
        // DisabilityPremiumAward
        header += sCouncilTotalCount_DisabilityPremiumAward + ", ";
        header += sCouncilPercentageOfAll_DisabilityPremiumAward + ", ";
        header += sCouncilTotalCount_DisabilityPremiumAwardHBTTs + ", ";
        header += sCouncilPercentageOfAll_DisabilityPremiumAwardHBTTs + ", ";
        header += sCouncilPercentageOfHB_DisabilityPremiumAwardHBTTs + ", ";
        header += sCouncilTotalCount_DisabilityPremiumAwardCTBTTs + ", ";
        header += sCouncilPercentageOfAll_DisabilityPremiumAwardCTBTTs + ", ";
        header += sCouncilPercentageOfCTB_DisabilityPremiumAwardCTBTTs + ", ";
        // SevereDisabilityPremiumAward
        header += sCouncilTotalCount_SevereDisabilityPremiumAward + ", ";
        header += sCouncilPercentageOfAll_SevereDisabilityPremiumAward + ", ";
        header += sCouncilTotalCount_SevereDisabilityPremiumAwardHBTTs + ", ";
        header += sCouncilPercentageOfAll_SevereDisabilityPremiumAwardHBTTs + ", ";
        header += sCouncilPercentageOfHB_SevereDisabilityPremiumAwardHBTTs + ", ";
        header += sCouncilTotalCount_SevereDisabilityPremiumAwardCTBTTs + ", ";
        header += sCouncilPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs + ", ";
        header += sCouncilPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs + ", ";
        // DisabledChildPremiumAward
        header += sCouncilTotalCount_DisabledChildPremiumAward + ", ";
        header += sCouncilPercentageOfAll_DisabledChildPremiumAward + ", ";
        header += sCouncilTotalCount_DisabledChildPremiumAwardHBTTs + ", ";
        header += sCouncilPercentageOfAll_DisabledChildPremiumAwardHBTTs + ", ";
        header += sCouncilPercentageOfHB_DisabledChildPremiumAwardHBTTs + ", ";
        header += sCouncilTotalCount_DisabledChildPremiumAwardCTBTTs + ", ";
        header += sCouncilPercentageOfAll_DisabledChildPremiumAwardCTBTTs + ", ";
        header += sCouncilPercentageOfCTB_DisabledChildPremiumAwardCTBTTs + ", ";
        // EnhancedDisabilityPremiumAward
        header += sCouncilTotalCount_EnhancedDisabilityPremiumAward + ", ";
        header += sCouncilPercentageOfAll_EnhancedDisabilityPremiumAward + ", ";
        header += sCouncilTotalCount_EnhancedDisabilityPremiumAwardHBTTs + ", ";
        header += sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs + ", ";
        header += sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs + ", ";
        header += sCouncilTotalCount_EnhancedDisabilityPremiumAwardCTBTTs + ", ";
        header += sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs + ", ";
        header += sCouncilPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs + ", ";
        // SocialTTs
        // DisabilityAward
        header += sCouncilTotalCount_DisabilityAwardSocialTTs + ", ";
        header += sCouncilPercentageOfAll_DisabilityAwardSocialTTs + ", ";
        header += sCouncilPercentageOfHB_DisabilityAwardSocialTTs + ", ";
        header += sCouncilPercentageOfSocialTTs_DisabilityAwardSocialTTs + ", ";
        // DisabilityPremiumAward
        header += sCouncilTotalCount_DisabilityPremiumAwardSocialTTs + ", ";
        header += sCouncilPercentageOfAll_DisabilityPremiumAwardSocialTTs + ", ";
        header += sCouncilPercentageOfHB_DisabilityPremiumAwardSocialTTs + ", ";
        header += sCouncilPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs + ", ";
        // SevereDisabilityPremiumAward
        header += sCouncilTotalCount_SevereDisabilityPremiumAwardSocialTTs + ", ";
        header += sCouncilPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs + ", ";
        header += sCouncilPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs + ", ";
        header += sCouncilPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs + ", ";
        // DisabledChildPremiumAward
        header += sCouncilTotalCount_DisabledChildPremiumAwardSocialTTs + ", ";
        header += sCouncilPercentageOfAll_DisabledChildPremiumAwardSocialTTs + ", ";
        header += sCouncilPercentageOfHB_DisabledChildPremiumAwardSocialTTs + ", ";
        header += sCouncilPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs + ", ";
        // EnhancedDisabilityPremiumAward
        header += sCouncilTotalCount_EnhancedDisabilityPremiumAwardSocialTTs + ", ";
        header += sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs + ", ";
        header += sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs + ", ";
        header += sCouncilPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs + ", ";
        // PrivateDeregulatedTTs
        // DisabilityAward
        header += sCouncilTotalCount_DisabilityAwardPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs + ", ";
        // DisabilityPremiumAward
        header += sCouncilTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        // SevereDisabilityPremiumAward
        header += sCouncilTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        // DisabledChildPremiumAward
        header += sCouncilTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs + ", ";
        // EnhancedDisabilityPremiumAward
        header += sCouncilTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        for (int i = 1; i < nTT; i++) {
            // DisabilityAward
            header += sCouncilTotalCount_DisabilityAwardByTT[i] + ", ";
            header += sCouncilPercentageOfAll_DisabilityAwardByTT[i] + ", ";
            if (i == 5 || i == 7) {
                header += sCouncilPercentageOfCTB_DisabilityAwardByTT[i] + ", ";
            } else {
                header += sCouncilPercentageOfHB_DisabilityAwardByTT[i] + ", ";
            }
            header += sCouncilPercentageOfTT_DisabilityAwardByTT[i] + ", ";
            // DisabilityPremiumAward
            header += sCouncilTotalCount_DisabilityPremiumAwardByTT[i] + ", ";
            header += sCouncilPercentageOfAll_DisabilityPremiumAwardByTT[i] + ", ";
            if (i == 5 || i == 7) {
                header += sCouncilPercentageOfCTB_DisabilityPremiumAwardByTT[i] + ", ";
            } else {
                header += sCouncilPercentageOfHB_DisabilityPremiumAwardByTT[i] + ", ";
            }
            header += sCouncilPercentageOfTT_DisabilityPremiumAwardByTT[i] + ", ";
            // SevereDisabilityPremiumAward
            header += sCouncilTotalCount_SevereDisabilityPremiumAwardByTT[i] + ", ";
            header += sCouncilPercentageOfAll_SevereDisabilityPremiumAwardByTT[i] + ", ";
            if (i == 5 || i == 7) {
                header += sCouncilPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i] + ", ";
            } else {
                header += sCouncilPercentageOfHB_SevereDisabilityPremiumAwardByTT[i] + ", ";
            }
            header += sCouncilPercentageOfTT_SevereDisabilityPremiumAwardByTT[i] + ", ";
            // DisabledChildPremiumAward
            header += sCouncilTotalCount_DisabledChildPremiumAwardByTT[i] + ", ";
            header += sCouncilPercentageOfAll_DisabledChildPremiumAwardByTT[i] + ", ";
            if (i == 5 || i == 7) {
                header += sCouncilPercentageOfCTB_DisabledChildPremiumAwardByTT[i] + ", ";
            } else {
                header += sCouncilPercentageOfHB_DisabledChildPremiumAwardByTT[i] + ", ";
            }
            header += sCouncilPercentageOfTT_DisabledChildPremiumAwardByTT[i] + ", ";
            // EnhancedDisabilityPremiumAward
            header += sCouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
            header += sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
            if (i == 5 || i == 7) {
                header += sCouncilPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
            } else {
                header += sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
            }
            header += sCouncilPercentageOfTT_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
        }
        // RSL
        // General
        // DisabilityAward
        header += sRSLTotalCount_DisabilityAward + ", ";
        header += sRSLPercentageOfAll_DisabilityAward + ", ";
        header += sRSLTotalCount_DisabilityAwardHBTTs + ", ";
        header += sRSLPercentageOfAll_DisabilityAwardHBTTs + ", ";
        header += sRSLPercentageOfHB_DisabilityAwardHBTTs + ", ";
        header += sRSLTotalCount_DisabilityAwardCTBTTs + ", ";
        header += sRSLPercentageOfAll_DisabilityAwardCTBTTs + ", ";
        header += sRSLPercentageOfCTB_DisabilityAwardCTBTTs + ", ";
        // DisabilityPremiumAward
        header += sRSLTotalCount_DisabilityPremiumAward + ", ";
        header += sRSLPercentageOfAll_DisabilityPremiumAward + ", ";
        header += sRSLTotalCount_DisabilityPremiumAwardHBTTs + ", ";
        header += sRSLPercentageOfAll_DisabilityPremiumAwardHBTTs + ", ";
        header += sRSLPercentageOfHB_DisabilityPremiumAwardHBTTs + ", ";
        header += sRSLTotalCount_DisabilityPremiumAwardCTBTTs + ", ";
        header += sRSLPercentageOfAll_DisabilityPremiumAwardCTBTTs + ", ";
        header += sRSLPercentageOfCTB_DisabilityPremiumAwardCTBTTs + ", ";
        // SevereDisabilityPremiumAward
        header += sRSLTotalCount_SevereDisabilityPremiumAward + ", ";
        header += sRSLPercentageOfAll_SevereDisabilityPremiumAward + ", ";
        header += sRSLTotalCount_SevereDisabilityPremiumAwardHBTTs + ", ";
        header += sRSLPercentageOfAll_SevereDisabilityPremiumAwardHBTTs + ", ";
        header += sRSLPercentageOfHB_SevereDisabilityPremiumAwardHBTTs + ", ";
        header += sRSLTotalCount_SevereDisabilityPremiumAwardCTBTTs + ", ";
        header += sRSLPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs + ", ";
        header += sRSLPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs + ", ";
        // DisabledChildPremiumAward
        header += sRSLTotalCount_DisabledChildPremiumAward + ", ";
        header += sRSLPercentageOfAll_DisabledChildPremiumAward + ", ";
        header += sRSLTotalCount_DisabledChildPremiumAwardHBTTs + ", ";
        header += sRSLPercentageOfAll_DisabledChildPremiumAwardHBTTs + ", ";
        header += sRSLPercentageOfHB_DisabledChildPremiumAwardHBTTs + ", ";
        header += sRSLTotalCount_DisabledChildPremiumAwardCTBTTs + ", ";
        header += sRSLPercentageOfAll_DisabledChildPremiumAwardCTBTTs + ", ";
        header += sRSLPercentageOfCTB_DisabledChildPremiumAwardCTBTTs + ", ";
        // EnhancedDisabilityPremiumAward
        header += sRSLTotalCount_EnhancedDisabilityPremiumAward + ", ";
        header += sRSLPercentageOfAll_EnhancedDisabilityPremiumAward + ", ";
        header += sRSLTotalCount_EnhancedDisabilityPremiumAwardHBTTs + ", ";
        header += sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs + ", ";
        header += sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs + ", ";
        header += sRSLTotalCount_EnhancedDisabilityPremiumAwardCTBTTs + ", ";
        header += sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs + ", ";
        header += sRSLPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs + ", ";
        // SocialTTs
        // DisabilityAward
        header += sRSLTotalCount_DisabilityAwardSocialTTs + ", ";
        header += sRSLPercentageOfAll_DisabilityAwardSocialTTs + ", ";
        header += sRSLPercentageOfHB_DisabilityAwardSocialTTs + ", ";
        header += sRSLPercentageOfSocialTTs_DisabilityAwardSocialTTs + ", ";
        // DisabilityPremiumAward
        header += sRSLTotalCount_DisabilityPremiumAwardSocialTTs + ", ";
        header += sRSLPercentageOfAll_DisabilityPremiumAwardSocialTTs + ", ";
        header += sRSLPercentageOfHB_DisabilityPremiumAwardSocialTTs + ", ";
        header += sRSLPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs + ", ";
        // SevereDisabilityPremiumAward
        header += sRSLTotalCount_SevereDisabilityPremiumAwardSocialTTs + ", ";
        header += sRSLPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs + ", ";
        header += sRSLPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs + ", ";
        header += sRSLPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs + ", ";
        // DisabledChildPremiumAward
        header += sRSLTotalCount_DisabledChildPremiumAwardSocialTTs + ", ";
        header += sRSLPercentageOfAll_DisabledChildPremiumAwardSocialTTs + ", ";
        header += sRSLPercentageOfHB_DisabledChildPremiumAwardSocialTTs + ", ";
        header += sRSLPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs + ", ";
        // EnhancedDisabilityPremiumAward
        header += sRSLTotalCount_EnhancedDisabilityPremiumAwardSocialTTs + ", ";
        header += sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs + ", ";
        header += sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs + ", ";
        header += sRSLPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs + ", ";
        // PrivateDeregulatedTTs
        // DisabilityAward
        header += sRSLTotalCount_DisabilityAwardPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs + ", ";
        // DisabilityPremiumAward
        header += sRSLTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        // SevereDisabilityPremiumAward
        header += sRSLTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        // DisabledChildPremiumAward
        header += sRSLTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs + ", ";
        // EnhancedDisabilityPremiumAward
        header += sRSLTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + ", ";
        for (int i = 1; i < nTT; i++) {
            // DisabilityAward
            header += sRSLTotalCount_DisabilityAwardByTT[i] + ", ";
            header += sRSLPercentageOfAll_DisabilityAwardByTT[i] + ", ";
            if (i == 5 || i == 7) {
                header += sRSLPercentageOfCTB_DisabilityAwardByTT[i] + ", ";
            } else {
                header += sRSLPercentageOfHB_DisabilityAwardByTT[i] + ", ";
            }
            header += sRSLPercentageOfTT_DisabilityAwardByTT[i] + ", ";
            // DisabilityPremiumAward
            header += sRSLTotalCount_DisabilityPremiumAwardByTT[i] + ", ";
            header += sRSLPercentageOfAll_DisabilityPremiumAwardByTT[i] + ", ";
            if (i == 5 || i == 7) {
                header += sRSLPercentageOfCTB_DisabilityPremiumAwardByTT[i] + ", ";
            } else {
                header += sRSLPercentageOfHB_DisabilityPremiumAwardByTT[i] + ", ";
            }
            header += sRSLPercentageOfTT_DisabilityPremiumAwardByTT[i] + ", ";
            // SevereDisabilityPremiumAward
            header += sRSLTotalCount_SevereDisabilityPremiumAwardByTT[i] + ", ";
            header += sRSLPercentageOfAll_SevereDisabilityPremiumAwardByTT[i] + ", ";
            if (i == 5 || i == 7) {
                header += sRSLPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i] + ", ";
            } else {
                header += sRSLPercentageOfHB_SevereDisabilityPremiumAwardByTT[i] + ", ";
            }
            header += sRSLPercentageOfTT_SevereDisabilityPremiumAwardByTT[i] + ", ";
            // DisabledChildPremiumAward
            header += sRSLTotalCount_DisabledChildPremiumAwardByTT[i] + ", ";
            header += sRSLPercentageOfAll_DisabledChildPremiumAwardByTT[i] + ", ";
            if (i == 5 || i == 7) {
                header += sRSLPercentageOfCTB_DisabledChildPremiumAwardByTT[i] + ", ";
            } else {
                header += sRSLPercentageOfHB_DisabledChildPremiumAwardByTT[i] + ", ";
            }
            header += sRSLPercentageOfTT_DisabledChildPremiumAwardByTT[i] + ", ";
            // EnhancedDisabilityPremiumAward
            header += sRSLTotalCount_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
            header += sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
            if (i == 5 || i == 7) {
                header += sRSLPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
            } else {
                header += sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
            }
            header += sRSLPercentageOfTT_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
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
            line += getLineSingleTimeGeneric(key, summary);
            // All UO
            // General
            // DisabilityAward
            line += summary.get(sTotalCount_DisabilityAward) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityAward) + ", ";
            line += summary.get(sTotalCount_DisabilityAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabilityAwardHBTTs) + ", ";
            line += summary.get(sTotalCount_DisabilityAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfCTB_DisabilityAwardCTBTTs) + ", ";
            // DisabilityPremiumAward
            line += summary.get(sTotalCount_DisabilityPremiumAward) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityPremiumAward) + ", ";
            line += summary.get(sTotalCount_DisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sTotalCount_DisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfCTB_DisabilityPremiumAwardCTBTTs) + ", ";
            // SevereDisabilityPremiumAward
            line += summary.get(sTotalCount_SevereDisabilityPremiumAward) + ", ";
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAward) + ", ";
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfHB_SevereDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs) + ", ";
            // DisabledChildPremiumAward
            line += summary.get(sTotalCount_DisabledChildPremiumAward) + ", ";
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAward) + ", ";
            line += summary.get(sTotalCount_DisabledChildPremiumAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabledChildPremiumAwardHBTTs) + ", ";
            line += summary.get(sTotalCount_DisabledChildPremiumAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfCTB_DisabledChildPremiumAwardCTBTTs) + ", ";
            // EnhancedDisabilityPremiumAward
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAward) + ", ";
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAward) + ", ";
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs) + ", ";
            // SocialTTs
            // DisabilityAward
            line += summary.get(sTotalCount_DisabilityAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabilityAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfSocialTTs_DisabilityAwardSocialTTs) + ", ";
            // DisabilityPremiumAward
            line += summary.get(sTotalCount_DisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs) + ", ";
            // SevereDisabilityPremiumAward
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs) + ", ";
            // DisabledChildPremiumAward
            line += summary.get(sTotalCount_DisabledChildPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabledChildPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs) + ", ";
            // EnhancedDisabilityPremiumAward
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs) + ", ";
            // PrivateDeregulatedTTs
            // DisabilityAward
            line += summary.get(sTotalCount_DisabilityAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs) + ", ";
            // DisabilityPremiumAward
            line += summary.get(sTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            // SevereDisabilityPremiumAward
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            // DisabledChildPremiumAward
            line += summary.get(sTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs) + ", ";
            // EnhancedDisabilityPremiumAward
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            for (int i = 1; i < nTT; i++) {
                // DisabilityAward
                line += summary.get(sTotalCount_DisabilityAwardByTT[i]) + ", ";
                line += summary.get(sPercentageOfAll_DisabilityAwardByTT[i]) + ", ";
                if (i == 5 || i == 7) {
                    line += summary.get(sPercentageOfCTB_DisabilityAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sPercentageOfHB_DisabilityAwardByTT[i]) + ", ";
                }
                line += summary.get(sPercentageOfTT_DisabilityAwardByTT[i]) + ", ";
                // DisabilityPremiumAward
                line += summary.get(sTotalCount_DisabilityPremiumAwardByTT[i]) + ", ";
                line += summary.get(sPercentageOfAll_DisabilityPremiumAwardByTT[i]) + ", ";
                if (i == 5 || i == 7) {
                    line += summary.get(sPercentageOfCTB_DisabilityPremiumAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sPercentageOfHB_DisabilityPremiumAwardByTT[i]) + ", ";
                }
                line += summary.get(sPercentageOfTT_DisabilityPremiumAwardByTT[i]) + ", ";
                // SevereDisabilityPremiumAward
                line += summary.get(sTotalCount_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                if (i == 5 || i == 7) {
                    line += summary.get(sPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sPercentageOfHB_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                }
                line += summary.get(sPercentageOfTT_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                // DisabledChildPremiumAward
                line += summary.get(sTotalCount_DisabledChildPremiumAwardByTT[i]) + ", ";
                line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardByTT[i]) + ", ";
                if (i == 5 || i == 7) {
                    line += summary.get(sPercentageOfCTB_DisabledChildPremiumAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sPercentageOfHB_DisabledChildPremiumAwardByTT[i]) + ", ";
                }
                line += summary.get(sPercentageOfTT_DisabledChildPremiumAwardByTT[i]) + ", ";
                // EnhancedDisabilityPremiumAward
                line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                if (i == 5 || i == 7) {
                    line += summary.get(sPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                }
                line += summary.get(sPercentageOfTT_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
            }
            // Council
            // General
            // DisabilityAward
            line += summary.get(sCouncilTotalCount_DisabilityAward) + ", ";
            line += summary.get(sCouncilPercentageOfAll_DisabilityAward) + ", ";
            line += summary.get(sCouncilTotalCount_DisabilityAwardHBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_DisabilityAwardHBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfHB_DisabilityAwardHBTTs) + ", ";
            line += summary.get(sCouncilTotalCount_DisabilityAwardCTBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_DisabilityAwardCTBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfCTB_DisabilityAwardCTBTTs) + ", ";
            // DisabilityPremiumAward
            line += summary.get(sCouncilTotalCount_DisabilityPremiumAward) + ", ";
            line += summary.get(sCouncilPercentageOfAll_DisabilityPremiumAward) + ", ";
            line += summary.get(sCouncilTotalCount_DisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_DisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfHB_DisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sCouncilTotalCount_DisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_DisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfCTB_DisabilityPremiumAwardCTBTTs) + ", ";
            // SevereDisabilityPremiumAward
            line += summary.get(sCouncilTotalCount_SevereDisabilityPremiumAward) + ", ";
            line += summary.get(sCouncilPercentageOfAll_SevereDisabilityPremiumAward) + ", ";
            line += summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_SevereDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfHB_SevereDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs) + ", ";
            // DisabledChildPremiumAward
            line += summary.get(sCouncilTotalCount_DisabledChildPremiumAward) + ", ";
            line += summary.get(sCouncilPercentageOfAll_DisabledChildPremiumAward) + ", ";
            line += summary.get(sCouncilTotalCount_DisabledChildPremiumAwardHBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_DisabledChildPremiumAwardHBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfHB_DisabledChildPremiumAwardHBTTs) + ", ";
            line += summary.get(sCouncilTotalCount_DisabledChildPremiumAwardCTBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_DisabledChildPremiumAwardCTBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfCTB_DisabledChildPremiumAwardCTBTTs) + ", ";
            // EnhancedDisabilityPremiumAward
            line += summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAward) + ", ";
            line += summary.get(sCouncilPercentageOfAll_EnhancedDisabilityPremiumAward) + ", ";
            line += summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sCouncilPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs) + ", ";
            // SocialTTs
            // DisabilityAward
            line += summary.get(sCouncilTotalCount_DisabilityAwardSocialTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_DisabilityAwardSocialTTs) + ", ";
            line += summary.get(sCouncilPercentageOfHB_DisabilityAwardSocialTTs) + ", ";
            line += summary.get(sCouncilPercentageOfSocialTTs_DisabilityAwardSocialTTs) + ", ";
            // DisabilityPremiumAward
            line += summary.get(sCouncilTotalCount_DisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_DisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sCouncilPercentageOfHB_DisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sCouncilPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs) + ", ";
            // SevereDisabilityPremiumAward
            line += summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sCouncilPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sCouncilPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs) + ", ";
            // DisabledChildPremiumAward
            line += summary.get(sCouncilTotalCount_DisabledChildPremiumAwardSocialTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_DisabledChildPremiumAwardSocialTTs) + ", ";
            line += summary.get(sCouncilPercentageOfHB_DisabledChildPremiumAwardSocialTTs) + ", ";
            line += summary.get(sCouncilPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs) + ", ";
            // EnhancedDisabilityPremiumAward
            line += summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sCouncilPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs) + ", ";
            // PrivateDeregulatedTTs
            // DisabilityAward
            line += summary.get(sCouncilTotalCount_DisabilityAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sCouncilPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sCouncilPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs) + ", ";
            // DisabilityPremiumAward
            line += summary.get(sCouncilTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sCouncilPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sCouncilPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            // SevereDisabilityPremiumAward
            line += summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sCouncilPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sCouncilPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            // DisabledChildPremiumAward
            line += summary.get(sCouncilTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sCouncilPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sCouncilPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs) + ", ";
            // EnhancedDisabilityPremiumAward
            line += summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sCouncilPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            for (int i = 1; i < nTT; i++) {
                // DisabilityAward
                line += summary.get(sCouncilTotalCount_DisabilityAwardByTT[i]) + ", ";
                line += summary.get(sCouncilPercentageOfAll_DisabilityAwardByTT[i]) + ", ";
                if (i == 5 || i == 7) {
                    line += summary.get(sCouncilPercentageOfCTB_DisabilityAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sCouncilPercentageOfHB_DisabilityAwardByTT[i]) + ", ";
                }
                line += summary.get(sCouncilPercentageOfTT_DisabilityAwardByTT[i]) + ", ";
                // DisabilityPremiumAward
                line += summary.get(sCouncilTotalCount_DisabilityPremiumAwardByTT[i]) + ", ";
                line += summary.get(sCouncilPercentageOfAll_DisabilityPremiumAwardByTT[i]) + ", ";
                if (i == 5 || i == 7) {
                    line += summary.get(sCouncilPercentageOfCTB_DisabilityPremiumAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sCouncilPercentageOfHB_DisabilityPremiumAwardByTT[i]) + ", ";
                }
                line += summary.get(sCouncilPercentageOfTT_DisabilityPremiumAwardByTT[i]) + ", ";
                // SevereDisabilityPremiumAward
                line += summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                line += summary.get(sCouncilPercentageOfAll_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                if (i == 5 || i == 7) {
                    line += summary.get(sCouncilPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sCouncilPercentageOfHB_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                }
                line += summary.get(sCouncilPercentageOfTT_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                // DisabledChildPremiumAward
                line += summary.get(sCouncilTotalCount_DisabledChildPremiumAwardByTT[i]) + ", ";
                line += summary.get(sCouncilPercentageOfAll_DisabledChildPremiumAwardByTT[i]) + ", ";
                if (i == 5 || i == 7) {
                    line += summary.get(sCouncilPercentageOfCTB_DisabledChildPremiumAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sCouncilPercentageOfHB_DisabledChildPremiumAwardByTT[i]) + ", ";
                }
                line += summary.get(sCouncilPercentageOfTT_DisabledChildPremiumAwardByTT[i]) + ", ";
                // EnhancedDisabilityPremiumAward
                line += summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                line += summary.get(sCouncilPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                if (i == 5 || i == 7) {
                    line += summary.get(sCouncilPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                }
                line += summary.get(sCouncilPercentageOfTT_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
            }
            // RSL
            // General
            // DisabilityAward
            line += summary.get(sRSLTotalCount_DisabilityAward) + ", ";
            line += summary.get(sRSLPercentageOfAll_DisabilityAward) + ", ";
            line += summary.get(sRSLTotalCount_DisabilityAwardHBTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_DisabilityAwardHBTTs) + ", ";
            line += summary.get(sRSLPercentageOfHB_DisabilityAwardHBTTs) + ", ";
            line += summary.get(sRSLTotalCount_DisabilityAwardCTBTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_DisabilityAwardCTBTTs) + ", ";
            line += summary.get(sRSLPercentageOfCTB_DisabilityAwardCTBTTs) + ", ";
            // DisabilityPremiumAward
            line += summary.get(sRSLTotalCount_DisabilityPremiumAward) + ", ";
            line += summary.get(sRSLPercentageOfAll_DisabilityPremiumAward) + ", ";
            line += summary.get(sRSLTotalCount_DisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_DisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sRSLPercentageOfHB_DisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sRSLTotalCount_DisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_DisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sRSLPercentageOfCTB_DisabilityPremiumAwardCTBTTs) + ", ";
            // SevereDisabilityPremiumAward
            line += summary.get(sRSLTotalCount_SevereDisabilityPremiumAward) + ", ";
            line += summary.get(sRSLPercentageOfAll_SevereDisabilityPremiumAward) + ", ";
            line += summary.get(sRSLTotalCount_SevereDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_SevereDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sRSLPercentageOfHB_SevereDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sRSLTotalCount_SevereDisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sRSLPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs) + ", ";
            // DisabledChildPremiumAward
            line += summary.get(sRSLTotalCount_DisabledChildPremiumAward) + ", ";
            line += summary.get(sRSLPercentageOfAll_DisabledChildPremiumAward) + ", ";
            line += summary.get(sRSLTotalCount_DisabledChildPremiumAwardHBTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_DisabledChildPremiumAwardHBTTs) + ", ";
            line += summary.get(sRSLPercentageOfHB_DisabledChildPremiumAwardHBTTs) + ", ";
            line += summary.get(sRSLTotalCount_DisabledChildPremiumAwardCTBTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_DisabledChildPremiumAwardCTBTTs) + ", ";
            line += summary.get(sRSLPercentageOfCTB_DisabledChildPremiumAwardCTBTTs) + ", ";
            // EnhancedDisabilityPremiumAward
            line += summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAward) + ", ";
            line += summary.get(sRSLPercentageOfAll_EnhancedDisabilityPremiumAward) + ", ";
            line += summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs) + ", ";
            line += summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs) + ", ";
            line += summary.get(sRSLPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs) + ", ";
            // SocialTTs
            // DisabilityAward
            line += summary.get(sRSLTotalCount_DisabilityAwardSocialTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_DisabilityAwardSocialTTs) + ", ";
            line += summary.get(sRSLPercentageOfHB_DisabilityAwardSocialTTs) + ", ";
            line += summary.get(sRSLPercentageOfSocialTTs_DisabilityAwardSocialTTs) + ", ";
            // DisabilityPremiumAward
            line += summary.get(sRSLTotalCount_DisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_DisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sRSLPercentageOfHB_DisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sRSLPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs) + ", ";
            // SevereDisabilityPremiumAward
            line += summary.get(sRSLTotalCount_SevereDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sRSLPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sRSLPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs) + ", ";
            // DisabledChildPremiumAward
            line += summary.get(sRSLTotalCount_DisabledChildPremiumAwardSocialTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_DisabledChildPremiumAwardSocialTTs) + ", ";
            line += summary.get(sRSLPercentageOfHB_DisabledChildPremiumAwardSocialTTs) + ", ";
            line += summary.get(sRSLPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs) + ", ";
            // EnhancedDisabilityPremiumAward
            line += summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sRSLPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs) + ", ";
            // PrivateDeregulatedTTs
            // DisabilityAward
            line += summary.get(sRSLTotalCount_DisabilityAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sRSLPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sRSLPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs) + ", ";
            // DisabilityPremiumAward
            line += summary.get(sRSLTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sRSLPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sRSLPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            // SevereDisabilityPremiumAward
            line += summary.get(sRSLTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sRSLPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sRSLPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            // DisabledChildPremiumAward
            line += summary.get(sRSLTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sRSLPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sRSLPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs) + ", ";
            // EnhancedDisabilityPremiumAward
            line += summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            line += summary.get(sRSLPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + ", ";
            for (int i = 1; i < nTT; i++) {
                // DisabilityAward
                line += summary.get(sRSLTotalCount_DisabilityAwardByTT[i]) + ", ";
                line += summary.get(sRSLPercentageOfAll_DisabilityAwardByTT[i]) + ", ";
                if (i == 5 || i == 7) {
                    line += summary.get(sRSLPercentageOfCTB_DisabilityAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sRSLPercentageOfHB_DisabilityAwardByTT[i]) + ", ";
                }
                line += summary.get(sRSLPercentageOfTT_DisabilityAwardByTT[i]) + ", ";
                // DisabilityPremiumAward
                line += summary.get(sRSLTotalCount_DisabilityPremiumAwardByTT[i]) + ", ";
                line += summary.get(sRSLPercentageOfAll_DisabilityPremiumAwardByTT[i]) + ", ";
                if (i == 5 || i == 7) {
                    line += summary.get(sRSLPercentageOfCTB_DisabilityPremiumAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sRSLPercentageOfHB_DisabilityPremiumAwardByTT[i]) + ", ";
                }
                line += summary.get(sRSLPercentageOfTT_DisabilityPremiumAwardByTT[i]) + ", ";
                // SevereDisabilityPremiumAward
                line += summary.get(sRSLTotalCount_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                line += summary.get(sRSLPercentageOfAll_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                if (i == 5 || i == 7) {
                    line += summary.get(sRSLPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sRSLPercentageOfHB_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                }
                line += summary.get(sRSLPercentageOfTT_SevereDisabilityPremiumAwardByTT[i]) + ", ";
                // DisabledChildPremiumAward
                line += summary.get(sRSLTotalCount_DisabledChildPremiumAwardByTT[i]) + ", ";
                line += summary.get(sRSLPercentageOfAll_DisabledChildPremiumAwardByTT[i]) + ", ";
                if (i == 5 || i == 7) {
                    line += summary.get(sRSLPercentageOfCTB_DisabledChildPremiumAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sRSLPercentageOfHB_DisabledChildPremiumAwardByTT[i]) + ", ";
                }
                line += summary.get(sRSLPercentageOfTT_DisabledChildPremiumAwardByTT[i]) + ", ";
                // EnhancedDisabilityPremiumAward
                line += summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                line += summary.get(sRSLPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                if (i == 5 || i == 7) {
                    line += summary.get(sRSLPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                } else {
                    line += summary.get(sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
                }
                line += summary.get(sRSLPercentageOfTT_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    @Override
    protected String getHeaderSingleTimeGeneric() {
        String result;
        result = "year-month, ";
        result += sAllCount1 + ", ";
            result += sHBCount1 + ", ";
            result += sCTBCount1 + ", ";
        result += "Month Year, ";
        return result;
    }

    @Override
    protected String getLineSingleTimeGeneric(
            String key,
            HashMap<String, String> summary) {
        String result;
        result = key + ", ";
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
        String[] split;
        split = key.split("-");
        result += Generic_Time.getMonth3Letters(split[1]);
        result += " " + split[0] + ", ";
        return result;
    }
}
