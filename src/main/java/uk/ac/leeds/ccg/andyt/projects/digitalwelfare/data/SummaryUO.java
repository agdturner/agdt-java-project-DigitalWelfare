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
    protected static final String sCouncilAllPercentageOfAll_LHACases = "CouncilAllPercentageOfHB__LHACases";
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
    protected static String[] sCouncilAllPercentage_EthnicGroupClaimant;
    protected static String sCouncilAllPostcodeValidFormatCount;
    protected static String sCouncilAllPostcodeValidCount;
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
    protected static final String sRSLAllPercentageOfAll_LHACases = "RSLAllPercentageOfHB__LHACases";
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
    protected static String[] sRSLAllPercentage_EthnicGroupClaimant;
    protected static String sRSLAllPostcodeValidFormatCount;
    protected static String sRSLAllPostcodeValidCount;
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
    protected static String[] sRSLHBTotalCount_EthnicGroupClaimant;
    protected static String[] sRSLHBPercentage_EthnicGroupClaimant;
    protected static String[] sCouncilHBTotalCount_EthnicGroupClaimant;
    protected static String[] sCouncilHBPercentage_EthnicGroupClaimant;

    // Strings1
    // Council
    protected static String[] sCouncilTotalCount_ClaimantTT;
    protected static String[] sCouncilPercentageOfAll_ClaimantTT;
    protected static String[] sCouncilPercentageOfHB_ClaimantTT;
    protected static String[] sCouncilPercentageOfCTB_ClaimantTT;
    protected static String sCouncilHBPostcodeValidFormatCount;
    protected static String sCouncilHBPostcodeValidCount;
    // CTB
    protected static String[] sCouncilTBTotalCount_EthnicGroupClaimant;
    protected static String[] sCouncilCTBPercentageEthnicGroupClaimant;
    protected static String[] sCouncilCTBTotalCountTTClaimant;
    protected static String[] sCouncilCTBPercentageOfAllTTClaimant;
    protected static String sCouncilCTBPostcodeValidFormatCount;
    protected static String sCouncilCTBPostcodeValidCount;
    // RSL
    protected static String[] sRSLTotalCount_ClaimantTT;
    protected static String[] sRSLPercentageOfAll_ClaimantTT;
    protected static String[] sRSLPercentageOfHB_ClaimantTT;
    protected static String[] sRSLPercentageOfCTB_ClaimantTT;
    protected static String sRSLHBPostcodeValidFormatCount;
    protected static String sRSLHBPostcodeValidCount;
    // CTB
    protected static String[] sRSLCTBTotalCount_EthnicGroupClaimant;
    protected static String[] sRSLCTBPercentageEthnicGroupClaimant;
    protected static String[] sRSLCTBTotalCountTTClaimant;
    protected static String[] sRSLCTBPercentageOfAllTTClaimant;
    protected static String sRSLCTBPostcodeValidFormatCount;
    protected static String sRSLCTBPostcodeValidCount;

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
    protected static int CouncilHBTotalCountLHACases;
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
    protected static int CouncilHBPostcodeValidFormatCount;
    protected static int CouncilHBPostcodeValidCount;
    // CTB
    protected static int CouncilCTBCount1;
    protected static Integer CouncilCTBCount0;
    protected static int[] CouncilCTBEthnicGroupCount;
    protected static int CouncilCTBPostcodeValidFormatCount;
    protected static int CouncilCTBPostcodeValidCount;
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
    protected static int RSLHBTotalCountLHACases;
    protected static int RSLCTBTotalCount_LHACases;
    protected static int[] RSLTotalCount_DisabilityPremiumAwardByTT;
    protected static int[] RSLTotalCount_SevereDisabilityPremiumAwardByTT;
    protected static int[] RSLTotalCount_DisabledChildPremiumAwardByTT;
    protected static int[] RSLTotalCount_EnhancedDisabilityPremiumAwardByTT;
    protected static int[] RSLTotalCount_DisabilityAwardByTT;
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
    protected static int RSLHBPostcodeValidFormatCount;
    protected static int RSLHBPostcodeValidCount;
    // CTB
    protected static int RSLCTBCount1;
    protected static Integer RSLCTBCount0;
    protected static int[] RSLCTBEthnicGroupCount;
    protected static int RSLCTBPostcodeValidFormatCount;
    protected static int RSLCTBPostcodeValidCount;

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
    //
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
    //protected static int CouncilCTBTotalCount_TTChangeClaimantIgnoreMinus999;
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
    //
    protected static int RSLTotalCount_TT1ToTT4;
    protected static int RSLTotalCount_TT4ToTT1;
    // RSLAll
    protected static int RSLAllTotalCount_TTChangeClaimant;
    //protected static int RSLAllTotalCount_TTChangeClaimantIgnoreMinus999;
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
    //protected static int RSLHBTotalCount_TTChangeClaimantIgnoreMinus999;
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
    //protected static int RSLCTBTotalCount_TTChangeClaimantIgnoreMinus999;
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

    
    
    
    
    // Go through all methods and approriate for UO
    
    
    
    
    
    @Override
    protected void init(int nTT, int nEG, int nPSI) {
        super.init(nTT, nEG, nPSI);
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

    @Override
    protected void initSingleTimeStrings(int nTT, int nEG, int nPSI) {
        super.initSingleTimeStrings(nTT, nEG, nPSI);
        sTotalIncomeTT = new String[nTT];
        sTotalCount_IncomeNonZeroTT = new String[nTT];
        sTotalCount_IncomeZeroTT = new String[nTT];
        sAverageIncomeTT = new String[nTT];
        sTotalWeeklyEligibleRentAmountTT = new String[nTT];
        sTotalCount_WeeklyEligibleRentAmountNonZeroTT = new String[nTT];
        sTotalCount_WeeklyEligibleRentAmountZeroTT = new String[nTT];
        sAverageWeeklyEligibleRentAmountTT = new String[nTT];

        sAllTotalCount_PSI = new String[nPSI];
        sHBTotalCount_PSI = new String[nPSI];
        sCTBTotalCount_PSI = new String[nPSI];
        sAllPercentageOfAll_PSI = new String[nPSI];
        sHBPercentageOfHB_PSI = new String[nPSI];
        sCTBPercentageOfCTB_PSI = new String[nPSI];
        sTotalCount_PSIByTT = new String[nPSI][nTT];
        sPercentageOfAll_PSIByTT = new String[nPSI][nTT];
        sPercentageOfHB_PSIByTT = new String[nPSI][nTT];
        sPercentageOfCTB_PSIByTT = new String[nPSI][nTT];
        for (int i = 1; i < nPSI; i++) {
            sAllTotalCount_PSI[i] = "RSLAllTotalCount_PSI" + i;
            sHBTotalCount_PSI[i] = "RSLHBTotalCount_PSI" + i;
            sCTBTotalCount_PSI[i] = "RSLCTBTotalCount_PSI" + i;
            sAllPercentageOfAll_PSI[i] = "RSLAllPercentageOfAll_PSI" + i;
            sHBPercentageOfHB_PSI[i] = "RSLHBPercentageOfHB_PSI" + i;
            sCTBPercentageOfCTB_PSI[i] = "RSLCTBPercentageOfCTB_PSI" + i;
            for (int j = 1; j < nTT; j++) {
                sTotalCount_PSIByTT[i][j] = "RSLTotalCount_PSI" + i + "TT" + j;
                sPercentageOfAll_PSIByTT[i][j] = "RSLPercentageOfAll_PSI" + i + "TT" + j;
                if (j == 5 || j == 7) {
                    sPercentageOfCTB_PSIByTT[i][j] = "RSLPercentageOfCTB_PSI" + i + "TT" + j;
                } else {
                    sPercentageOfHB_PSIByTT[i][j] = "RSLPercentageOfHB_PSI" + i + "TT" + j;
                }
            }
        }

        // All
        sAllTotalCount_EthnicGroupClaimant = new String[nEG];
        sAllPercentage_EthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            sAllTotalCount_EthnicGroupClaimant[i] = "RSLAllTotalCount_EthnicGroup" + i + "Claimant";
            sAllPercentage_EthnicGroupClaimant[i] = "RSLAllPercentageEthnicGroup" + i + "Claimant";
        }
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
            // Claimants
            sTotalCount_ClaimantTT[i] = "RSLTotalCount_ClaimantTT" + i;
            sPercentageOfAll_ClaimantTT[i] = "RSLPercentageOfAll_ClaimantTT" + i;
            sPercentageOfHB_ClaimantTT[i] = "RSLPercentageOfHB_ClaimantTT" + i;
            sPercentageOfCTB_ClaimantTT[i] = "RSLPercentageOfCTB_ClaimantTT" + i;
            // Income
            sTotalIncomeTT[i] = "RSLTotalIncomeTT" + i;
            sTotalCount_IncomeNonZeroTT[i] = "RSLTotalCount_IncomeNonZeroTT" + i;
            sTotalCount_IncomeZeroTT[i] = "RSLTotalCount_IncomeZeroTT" + i;
            sAverageIncomeTT[i] = "RSLAverageIncomeTT" + i;
            // WeeklyEligibleRentAmountTT
            sTotalWeeklyEligibleRentAmountTT[i] = "RSLTotalWeeklyEligibleRentAmountTT" + i;
            sTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] = "RSLTotalCount_WeeklyEligibleRentAmountNonZeroTT" + i;
            sTotalCount_WeeklyEligibleRentAmountZeroTT[i] = "RSLTotalCount_WeeklyEligibleRentAmountZeroTT" + i;
            sAverageWeeklyEligibleRentAmountTT[i] = "RSLAverageWeeklyEligibleRentAmountTT" + i;
            // DisabilityAwardByTT
            sTotalCount_DisabilityAwardByTT[i] = "RSLTotalCount_DisabilityAwardByTT" + i;
            sPercentageOfAll_DisabilityAwardByTT[i] = "RSLPercentageOfAll_DisabilityAwardByTT" + i;
            sPercentageOfHB_DisabilityAwardByTT[i] = "RSLPercentageOfHB_DisabilityAwardByTT" + i;
            sPercentageOfCTB_DisabilityAwardByTT[i] = "RSLPercentageOfCTB_DisabilityAwardByTT" + i;
            sPercentageOfTT_DisabilityAwardByTT[i] = "RSLPercentageOfTT_DisabilityAwardByTT" + i;
            // DisabilityPremiumAwardByTT
            sTotalCount_DisabilityPremiumAwardByTT[i] = "RSLTotalCount_DisabilityPremiumAwardByTT" + i;
            sPercentageOfAll_DisabilityPremiumAwardByTT[i] = "RSLPercentageOfAll_DisabilityPremiumAwardByTT" + i;
            sPercentageOfHB_DisabilityPremiumAwardByTT[i] = "RSLPercentageOfHB_DisabilityPremiumAwardByTT" + i;
            sPercentageOfCTB_DisabilityPremiumAwardByTT[i] = "RSLPercentageOfCTB_DisabilityPremiumAwardByTT" + i;
            sPercentageOfTT_DisabilityPremiumAwardByTT[i] = "RSLPercentageOfTT_DisabilityPremiumAwardByTT" + i;
            // SevereDisabilityPremiumAwardByTT
            sTotalCount_SevereDisabilityPremiumAwardByTT[i] = "RSLTotalCount_SevereDisabilityPremiumAwardByTT" + i;
            sPercentageOfAll_SevereDisabilityPremiumAwardByTT[i] = "RSLPercentageOfAll_SevereDisabilityPremiumAwardByTT" + i;
            sPercentageOfHB_SevereDisabilityPremiumAwardByTT[i] = "RSLPercentageOfHB_SevereDisabilityPremiumAwardByTT" + i;
            sPercentageOfCTB_SevereDisabilityPremiumAwardByTT[i] = "RSLPercentageOfCTB_SevereDisabilityPremiumAwardByTT" + i;
            sPercentageOfTT_SevereDisabilityPremiumAwardByTT[i] = "RSLPercentageOfTT_SevereDisabilityPremiumAwardByTT" + i;
            // DisabledChildPremiumAwardByTT
            sTotalCount_DisabledChildPremiumAwardByTT[i] = "RSLTotalCount_DisabledChildPremiumAwardByTT" + i;
            sPercentageOfAll_DisabledChildPremiumAwardByTT[i] = "RSLPercentageOfAll_DisabledChildPremiumAwardByTT" + i;
            sPercentageOfHB_DisabledChildPremiumAwardByTT[i] = "RSLPercentageOfHB_DisabledChildPremiumAwardByTT" + i;
            sPercentageOfCTB_DisabledChildPremiumAwardByTT[i] = "RSLPercentageOfCTB_DisabledChildPremiumAwardByTT" + i;
            sPercentageOfTT_DisabledChildPremiumAwardByTT[i] = "RSLPercentageOfTT_DisabledChildPremiumAwardByTT" + i;
            // EnhancedDisabilityPremiumAwardByTT
            sTotalCount_EnhancedDisabilityPremiumAwardByTT[i] = "RSLTotalCount_EnhancedDisabilityPremiumAwardByTT" + i;
            sPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i] = "RSLPercentageOfAll_EnhancedDisabilityPremiumAwardByTT" + i;
            sPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i] = "RSLPercentageOfHB_EnhancedDisabilityPremiumAwardByTT" + i;
            sPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT[i] = "RSLPercentageOfCTB_EnhancedDisabilityPremiumAwardByTT" + i;
            sPercentageOfTT_EnhancedDisabilityPremiumAwardByTT[i] = "RSLPercentageOfTT_EnhancedDisabilityPremiumAwardByTT" + i;
        }
        sAllPostcodeValidFormatCount = "RSLAllPostcodeValidFormatCount";
        sAllPostcodeValidCount = "RSLAllPostcodeValidCount";

        // HB
        sHBTotalCount_EthnicGroupClaimant = new String[nEG];
        sHBPercentage_EthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            sHBTotalCount_EthnicGroupClaimant[i] = "RSLHBTotalCount_EthnicGroup" + i + "Claimant";
            sHBPercentage_EthnicGroupClaimant[i] = "RSLHBPercentageEthnicGroup" + i + "Claimant";
        }

        sHBPostcodeValidFormatCount = "RSLHBPostcodeValidFormatCount";
        sHBPostcodeValidCount = "RSLHBPostcodeValidCount";
//        HBFemaleClaimantCountString = "RSLHBFemaleClaimantCount";
//        HBDisabledClaimantCountString = "RSLHBDisabledClaimantCount";
//        HBFemaleDisabledClaimantCountString = "RSLHBDisabledFemaleClaimantCount";
//        HBMaleDisabledClaimantCountString = "RSLHBDisabledMaleClaimantCount";

        // CTB
        sCTBTotalCount_EthnicGroupClaimant = new String[nEG];
        sCTBPercentageEthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            sCTBTotalCount_EthnicGroupClaimant[i] = "RSLCTBTotalCountClaimantEthnicGroup" + i + "Claimant";
            sCTBPercentageEthnicGroupClaimant[i] = "RSLCTBPercentageClaimantEthnicGroup" + i + "Claimant";
        }
        sCTBTotalCount_TTClaimant = new String[nTT];
        sCTBPercentageOfAll_TTClaimant = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            sCTBTotalCount_TTClaimant[i] = "RSLCTBTotalCountTT" + i + "Claimant";
            sCTBPercentageOfAll_TTClaimant[i] = "RSLCTBPercentageTT" + i + "Claimant";
        }
        sCTBPostcodeValidFormatCount = "RSLCTBPostcodeValidFormatCount";
        sCTBPostcodeValidCount = "RSLCTBPostcodeValidCount";
//        CTBFemaleClaimantCountString = "RSLCTBFemaleClaimantCount";
//        CTBDisabledClaimantCountString = "RSLCTBDisabledClaimantCount";
//        CTBFemaleDisabledClaimantCountString = "RSLCTBDisabledFemaleClaimantCount";
//        CTBMaleDisabledClaimantCountString = "RSLCTBDisabledMaleClaimantCount";

    }

    protected void initCounts(int nTT, int nEG, int nPSI) {
        initSingleTimeCounts(nTT, nEG, nPSI);
        initCompare2TimesCounts();
        initCompare3TimesCounts(nTT);
    }

    protected void initSingleTimeCounts(int nTT, int nEG, int nPSI) {

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

        HBTotalCount_EmployedClaimants = 0;
        CTBTotalCount_EmployedClaimants = 0;
        HBTotalCount_SelfEmployedClaimants = 0;
        CTBTotalCount_SelfEmployedClaimants = 0;
        HBTotalCount_StudentsClaimants = 0;
        CTBTotalCount_StudentsClaimants = 0;
        HBTotalCount_LHACases = 0;
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
        HBTotalCount_TTChangeClaimant = 0;
        //HBTotalCount_TTChangeClaimantIgnoreMinus999 = 0;

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
        CTBTotalCount_TTChangeClaimant = 0;
        //CTBTotalCount_TTChangeClaimantIgnoreMinus999 = 0;

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
            memberships = SummaryUO.getMemberships(
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
            memberships = SummaryUO.getMemberships(
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
            memberships = SummaryUO.getMemberships(
                    tClaimantIDTypes00,
                    tClaimantIDTypes0,
                    tClaimantIDPostcodeTypes1);
            result[0] = ((HashSet<ID_PostcodeID>) memberships[0]).size();
            result[1] = ((HashSet<ID_PostcodeID>) memberships[1]).size();
            result[2] = ((HashSet<ID_PostcodeID>) memberships[2]).size();
        }
        return result;
    }

    protected static Object[] getPreviousYM3s(String[] SHBEFilenames, ArrayList<Integer> include, String yM31) {
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

    protected void addToSummarySingleTime(
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
            HashMap<String, String> summary) {
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
        summary.put(
                sAllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(AllTotalCount_WeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(
                sAllTotalCount_WeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(AllTotalCount_WeeklyEligibleCouncilTaxAmountZero));
        summary.put(
                sHBTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(HBTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(
                sHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(HBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(
                sHBTotalCount_WeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(HBTotalCount_WeeklyEligibleCouncilTaxAmountZero));
        summary.put(
                sCTBTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(CTBTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(
                sCTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(CTBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(
                sCTBTotalCount_WeeklyEligibleCouncilTaxAmountZero,
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
        summary.put(
                sCTBTotalCount_ClaimantsEmployed,
                Integer.toString(CTBTotalCount_EmployedClaimants));
        // Self Employed
        summary.put(
                sHBTotalCountClaimantsSelfEmployed,
                Integer.toString(HBTotalCount_SelfEmployedClaimants));
        summary.put(
                sCTBTotalCountClaimantsSelfEmployed,
                Integer.toString(CTBTotalCount_SelfEmployedClaimants));
        // Students
        summary.put(
                sHBTotalCountClaimantsStudents,
                Integer.toString(HBTotalCount_StudentsClaimants));
        summary.put(
                sCTBTotalCountClaimantsStudents,
                Integer.toString(CTBTotalCount_StudentsClaimants));
        // LHACases
        t = HBTotalCount_LHACases + CTBTotalCount_LHACases;
        summary.put(
                sAllTotalCount_LHACases,
                Integer.toString(t));
        summary.put(
                sHBTotalCount_LHACases,
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
        }
    }

    protected void addToSummarySingleTimeEthnicityRates(
            int nEG,
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
                summary.put(
                        sAllPercentage_EthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sHBTotalCount_EthnicGroupClaimant[i]));
            d = HBCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(
                        sHBPercentage_EthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sCTBTotalCount_EthnicGroupClaimant[i]));
            d = CTBCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(
                        sCTBPercentageEthnicGroupClaimant[i],
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

    protected void addToSummarySingleTimeTTRates(
            int nTT,
            HashMap<String, String> summary) {
        // Ethnicity
        double percentage;
        double d;
        int all;
        all = Integer.valueOf(summary.get(sTotalCount_SocialTTsClaimant));
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
        all = Integer.valueOf(summary.get(sTotalCount_PrivateDeregulatedTTsClaimant));
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
        ClaimantsEthnicGroup0 = D_Record.getClaimantsEthnicGroup();
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
        HouseholdSize = DW_SHBE_Handler.getHouseholdSize(D_Record);
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
                    postcode,
                    yM30v);
        }
        // CTB Claim only counts
        if (DW_SHBE_Handler.isCTBOnlyClaim(D_Record)) {
            CTBTotalCount_PSI[PSI]++;
            CTBTotalCount_PSIByTT[PSI][TT]++;
            CTBTotalHouseholdSize += HouseholdSize;
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
                    postcode,
                    yM30v);
        }
        AllCount1 = HBCount1 + CTBCount1;
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
        } else {
            if (isValidPostcode1) {
                HBTotalCount_Postcode0NotValidPostcode1Valid++;
            } else {
                HBTotalCount_Postcode0NotValidPostcode1NotValid++;
                if (postcode0 == null) {
                    HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                } else {
                    if (postcode1 == null) {
                        HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                    } else {
                        if (postcode0.equalsIgnoreCase(postcode1)) {
                            HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                        } else {
                            HBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged++;
                        }
                    }
                }
            }
        }
        if (tenancyType0.compareTo(tenancyType1) != 0) {
            if (tenancyType0 == DW_SHBE_TenancyType_Handler.iMinus999
                    || tenancyType1 == DW_SHBE_TenancyType_Handler.iMinus999) {
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
     * @param tTT The Tenancy Type
     * @param tP The Postcode
     * @param yM3v They yM3 for postcode lookup validity
     */
    protected void doSingleTimeHBCount(
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
        } else {
            if (isValidPostcode1) {
                CTBTotalCount_Postcode0NotValidPostcode1Valid++;
            } else {
                CTBTotalCount_Postcode0NotValidPostcode1NotValid++;
                if (postcode0 == null) {
                    CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                } else {
                    if (postcode1 == null) {
                        CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                    } else {
                        if (postcode0.equalsIgnoreCase(postcode1)) {
                            CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
                        } else {
                            CTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged++;
                        }
                    }
                }
            }
        }
        if (tenancyType0.compareTo(tenancyType1) != 0) {
            if (tenancyType0 == DW_SHBE_TenancyType_Handler.iMinus999
                    || tenancyType1 == DW_SHBE_TenancyType_Handler.iMinus999) {
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
     * @param tTT The Tenancy Type
     * @param tP The Postcode
     * @param yM3v They yM3 for postcode lookup validity
     */
    protected void doSingleTimeCTBCount(
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
        header += sAllTotalCount_Postcode0ValidPostcode1Valid + ", ";
        header += sAllPercentagePostcode0ValidPostcode1Valid + ", ";
        header += sAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sAllPercentagePostcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sAllTotalCount_Postcode0ValidPostcode1NotValid + ", ";
        header += sAllPercentagePostcode0ValidPostcode1NotValid + ", ";
        header += sAllTotalCount_Postcode0NotValidPostcode1Valid + ", ";
        header += sAllPercentagePostcode0NotValidPostcode1Valid + ", ";
        header += sAllTotalCount_Postcode0NotValidPostcode1NotValid + ", ";
        header += sAllPercentagePostcode0NotValidPostcode1NotValid + ", ";
        header += sAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        // HB Postcode Related
        header += sHBTotalCount_Postcode0ValidPostcode1Valid + ", ";
        header += sHBPercentagePostcode0ValidPostcode1Valid + ", ";
        header += sHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sHBPercentagePostcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sHBTotalCount_Postcode0ValidPostcode1NotValid + ", ";
        header += sHBPercentagePostcode0ValidPostcode1NotValid + ", ";
        header += sHBTotalCount_Postcode0NotValidPostcode1Valid + ", ";
        header += sHBPercentagePostcode0NotValidPostcode1Valid + ", ";
        header += sHBTotalCount_Postcode0NotValidPostcode1NotValid + ", ";
        header += sHBPercentagePostcode0NotValidPostcode1NotValid + ", ";
        header += sHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        // CTB Postcode Related
        header += sCTBTotalCount_Postcode0ValidPostcode1Valid + ", ";
        header += sCTBPercentageOfCTB_Postcode0ValidPostcode1Valid + ", ";
        header += sCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged + ", ";
        header += sCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged + ", ";
        header += sCTBTotalCount_Postcode0ValidPostcode1NotValid + ", ";
        header += sCTBPercentagePostcode0ValidPostcode1NotValid + ", ";
        header += sCTBTotalCount_Postcode0NotValidPostcode1Valid + ", ";
        header += sCTBPercentagePostcode0NotValidPostcode1Valid + ", ";
        header += sCTBTotalCount_Postcode0NotValidPostcode1NotValid + ", ";
        header += sCTBPercentagePostcode0NotValidPostcode1NotValid + ", ";
        header += sCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        return header;
    }

    protected String getLineCompare2TimesPostcodeChange(HashMap<String, String> summary) {
        String line = "";
        // All
        line += summary.get(sAllTotalCount_Postcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sAllPercentagePostcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sAllTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sAllPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sAllTotalCount_Postcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sAllPercentagePostcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sAllTotalCount_Postcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sAllPercentagePostcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sAllTotalCount_Postcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sAllPercentagePostcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sAllTotalCount_Postcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sAllPercentagePostcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sAllTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        line += summary.get(sAllPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        // HB
        line += summary.get(sHBTotalCount_Postcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sHBPercentagePostcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sHBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sHBTotalCount_Postcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sHBPercentagePostcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sHBTotalCount_Postcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sHBPercentagePostcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sHBTotalCount_Postcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sHBPercentagePostcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sHBTotalCount_Postcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sHBPercentagePostcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sHBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        line += summary.get(sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        // CTB
        line += summary.get(sCTBTotalCount_Postcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sCTBPercentageOfCTB_Postcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sCTBTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged) + ", ";
        line += summary.get(sCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged) + ", ";
        line += summary.get(sCTBTotalCount_Postcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sCTBPercentagePostcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sCTBTotalCount_Postcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sCTBPercentagePostcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sCTBTotalCount_Postcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sCTBPercentagePostcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sCTBTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        line += summary.get(sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        return line;
    }

    public String getHeaderCompare2TimesTTChange() {
        String header = "";
        // General
        // All
        header += sAllTotalCount_TTChangeClaimant + ", ";
        header += sAllPercentageOfAll_TTChangeClaimant + ", ";
//        header += sAllTotalCount_TTChangeClaimantIgnoreMinus999 + ", ";
//        header += sAllPercentageOfAll_TTChangeClaimantIgnoreMinus999 + ", ";
        // General HB related
        header += sHBTotalCount_TTChangeClaimant + ", ";
        header += sHBPercentageOfHB_TTChangeClaimant + ", ";
//        header += sHBTotalCount_TTChangeClaimantIgnoreMinus999 + ", ";
//        header += sHBPercentageOfHB_TTChangeClaimantIgnoreMinus999 + ", ";

        header += sTotalCount_Minus999TTToSocialTTs + ", ";
        header += sTotalCount_Minus999TTToPrivateDeregulatedTTs + ", ";
        header += sTotalCount_HBTTsToMinus999TT + ", ";
        header += sPercentageOfHB_HBTTsToMinus999TT + ", ";
        header += sTotalCount_SocialTTsToMinus999TT + ", ";
        header += sPercentageOfSocialTTs_SocialTTsToMinus999TT + ", ";
        header += sTotalCount_PrivateDeregulatedTTsToMinus999TT + ", ";
        header += sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT + ", ";

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
//        header += sCTBTotalCount_TTChangeClaimantIgnoreMinus999 + ", ";
//        header += sCTBPercentageOfCTB_TTChangeClaimantIgnoreMinus999 + ", ";

        header += sTotalCount_Minus999TTToCTBTTs + ", ";
        header += sTotalCount_CTBTTsToMinus999TT + ", ";
        header += sPercentageOfCTB_CTBTTsToMinus999TT + ", ";

        header += sTotalCount_SocialTTsToCTBTTs + ", ";
        header += sPercentageOfSocialTTs_SocialTTsToCTBTTs + ", ";
        header += sTotalCount_TT1ToCTBTTs + ", ";
        header += sPercentageOfTT1_TT1ToCTBTTs + ", ";
        header += sTotalCount_TT4ToCTBTTs + ", ";
        header += sPercentageOfTT4_TT4ToCTBTTs + ", ";
        header += sTotalCount_PrivateDeregulatedTTsToCTBTTs + ", ";
        header += sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs + ", ";
        header += sTotalCount_CTBTTsToSocialTTs + ", ";
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

    protected String getLineCompare2TimesTTChange(HashMap<String, String> summary) {
        String line = "";
        // All
        line += summary.get(sAllTotalCount_TTChangeClaimant) + ", ";
        line += summary.get(sAllPercentageOfAll_TTChangeClaimant) + ", ";
//        line += summary.get(sAllTotalCount_TTChangeClaimantIgnoreMinus999) + ", ";
//        line += summary.get(sAllPercentageOfAll_TTChangeClaimantIgnoreMinus999) + ", ";
        // General HB related
        line += summary.get(sHBTotalCount_TTChangeClaimant) + ", ";
        line += summary.get(sHBPercentageOfHB_TTChangeClaimant) + ", ";
//        line += summary.get(sHBTotalCount_TTChangeClaimantIgnoreMinus999) + ", ";
//        line += summary.get(sHBPercentageOfHB_TTChangeClaimantIgnoreMinus999) + ", ";

        line += summary.get(sTotalCount_Minus999TTToSocialTTs) + ", ";
        line += summary.get(sTotalCount_Minus999TTToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sTotalCount_HBTTsToMinus999TT) + ", ";
        line += summary.get(sPercentageOfHB_HBTTsToMinus999TT) + ", ";
        line += summary.get(sTotalCount_SocialTTsToMinus999TT) + ", ";
        line += summary.get(sPercentageOfSocialTTs_SocialTTsToMinus999TT) + ", ";
        line += summary.get(sTotalCount_PrivateDeregulatedTTsToMinus999TT) + ", ";
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT) + ", ";

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
//        line += summary.get(sCTBTotalCount_TTChangeClaimantIgnoreMinus999) + ", ";
//        line += summary.get(sCTBPercentageOfCTB_TTChangeClaimantIgnoreMinus999) + ", ";

        line += summary.get(sTotalCount_Minus999TTToCTBTTs) + ", ";
        line += summary.get(sTotalCount_CTBTTsToMinus999TT) + ", ";
        line += summary.get(sPercentageOfCTB_CTBTTsToMinus999TT) + ", ";

        line += summary.get(sTotalCount_SocialTTsToCTBTTs) + ", ";
        line += summary.get(sPercentageOfSocialTTs_SocialTTsToCTBTTs) + ", ";
        line += summary.get(sTotalCount_TT1ToCTBTTs) + ", ";
        line += summary.get(sPercentageOfTT1_TT1ToCTBTTs) + ", ";
        line += summary.get(sTotalCount_TT4ToCTBTTs) + ", ";
        line += summary.get(sPercentageOfTT4_TT4ToCTBTTs) + ", ";
        line += summary.get(sTotalCount_PrivateDeregulatedTTsToCTBTTs) + ", ";
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs) + ", ";
        line += summary.get(sTotalCount_CTBTTsToSocialTTs) + ", ";
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
            line += getLineSingleTimeGeneric(key,
                    summary, underOccupancy);
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
            header += sAllTotalCount_EthnicGroupClaimant[i] + ", ";
            header += sAllPercentage_EthnicGroupClaimant[i] + ", ";
        }
        for (int i = 1; i < nEG; i++) {
            header += sHBTotalCount_EthnicGroupClaimant[i] + ", ";
            header += sHBPercentage_EthnicGroupClaimant[i] + ", ";
        }
        for (int i = 1; i < nEG; i++) {
            header += sCTBTotalCount_EthnicGroupClaimant[i] + ", ";
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
                line += summary.get(sAllTotalCount_EthnicGroupClaimant[i]) + ", ";
                line += summary.get(sAllPercentage_EthnicGroupClaimant[i]) + ", ";
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sHBTotalCount_EthnicGroupClaimant[i]) + ", ";
                line += summary.get(sHBPercentage_EthnicGroupClaimant[i]) + ", ";
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sCTBTotalCount_EthnicGroupClaimant[i]) + ", ";
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
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    protected String getHeaderSingleTimeGeneric(boolean underoccupancy) {
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

    protected String getLineSingleTimeGeneric(
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
