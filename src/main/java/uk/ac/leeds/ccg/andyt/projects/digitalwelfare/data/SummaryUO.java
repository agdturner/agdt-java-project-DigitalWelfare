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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Set;

/**
 *
 * @author geoagdt
 */
public class SummaryUO extends Summary {

    protected static final String sAllUO = "AllUO";
    protected static final String sCouncil = "Council";
    protected static final String sRSL = "RSL";
    protected static final String sAllUOAllCount1 = sAllUO + "AllCount1";
    // Council
    // Counter Strings
    // HouseholdSize
    protected static final String sCouncilTotalHouseholdSize = "CouncilTotalHouseholdSize";
    protected static final String sCouncilAverageHouseholdSize = "CouncilAverageHouseholdSize";
    // PSI
    protected static String[] sCouncilTotalCount_PSI;
    protected static String[] sCouncilPercentageOfHB_PSI;
    // PSIByTT
    protected static String[] sCouncilTotalCount_PSIByTT1;
    protected static String[] sCouncilPercentageOfHB_PSIByTT1;
    protected static String[] sCouncilPercentageOfTT_PSIByTT1;
    // DisabilityPremiumAwardByTT1
    protected static final String sCouncilTotalCount_DisabilityPremiumAwardByTT1 = "CouncilTotalCount_DisabilityPremiumAwardByTT1";
    protected static final String sCouncilPercentageOfHB_DisabilityPremiumAwardByTT1 = "CouncilPercentageOfHB_DisabilityPremiumAwardByTT1";
    protected static final String sCouncilPercentageOfTT_DisabilityPremiumAwardByTT1 = "CouncilPercentageOfTT_DisabilityPremiumAwardByTT1";
    // SevereDisabilityPremiumAwardByTT1
    protected static final String sCouncilTotalCount_SevereDisabilityPremiumAwardByTT1 = "CouncilTotalCount_SevereDisabilityPremiumAwardByTT1";
    protected static final String sCouncilPercentageOfHB_SevereDisabilityPremiumAwardByTT1 = "CouncilPercentageOfHB_SevereDisabilityPremiumAwardByTT1";
    protected static final String sCouncilPercentageOfTT_SevereDisabilityPremiumAwardByTT1 = "CouncilPercentageOfTT_SevereDisabilityPremiumAwardByTT1";
    // DisabledChildPremiumAwardByTT1
    protected static final String sCouncilTotalCount_DisabledChildPremiumAwardByTT1 = "CouncilTotalCount_DisabledChildPremiumAwardByTT1";
    protected static final String sCouncilPercentageOfHB_DisabledChildPremiumAwardByTT1 = "CouncilPercentageOfHB_DisabledChildPremiumAwardByTT1";
    protected static final String sCouncilPercentageOfTT_DisabledChildPremiumAwardByTT1 = "CouncilPercentageOfTT_DisabledChildPremiumAwardByTT1";
    // EnhancedDisabilityPremiumAwardByTT1
    protected static final String sCouncilTotalCount_EnhancedDisabilityPremiumAwardByTT1 = "CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT1";
    protected static final String sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardByTT1 = "CouncilPercentageOfHB_EnhancedDisabilityPremiumAwardByTT1";
    protected static final String sCouncilPercentageOfTT_EnhancedDisabilityPremiumAwardByTT1 = "CouncilPercentageOfTT_EnhancedDisabilityPremiumAwardByTT1";
    // DisabilityAwards
    protected static final String sCouncilTotalCount_DisabilityAward = "CouncilTotalCount_DisabilityAward";
    protected static final String sCouncilPercentageOfHB_DisabilityAward = "CouncilPercentageOfHB_DisabilityAward";
    // DisabilityPremiumAwards
    protected static final String sCouncilTotalCount_DisabilityPremiumAward = "CouncilTotalCount_DisabilityPremiumAward";
    protected static final String sCouncilPercentageOfHB_DisabilityPremiumAward = "CouncilPercentageOfHB_DisabilityPremiumAward";
    // SevereDisabilityPremiumAwards
    protected static final String sCouncilTotalCount_SevereDisabilityPremiumAward = "CouncilTotalCount_SevereDisabilityPremiumAward";
    protected static final String sCouncilPercentageOfHB_SevereDisabilityPremiumAward = "CouncilPercentageOfHB_SevereDisabilityPremiumAward";
    // DisabledChildPremiumAwards
    protected static final String sCouncilTotalCount_DisabledChildPremiumAward = "CouncilTotalCount_DisabledChildPremiumAward";
    protected static final String sCouncilPercentageOfHB_DisabledChildPremiumAward = "CouncilPercentageOfHB_DisabledChildPremiumAward";
    // EnhancedDisabilityPremiumAwards
    protected static final String sCouncilTotalCount_EnhancedDisabilityPremiumAward = "CouncilTotalCount_EnhancedDisabilityPremiumAward";
    protected static final String sCouncilPercentageOfHB_EnhancedDisabilityPremiumAward = "CouncilPercentageOfHB_EnhancedDisabilityPremiumAward";
    // DisabilityPremiumAwardHBTTs
    protected static final String sCouncilTotalCount_DisabilityPremiumAwardHBTTs = "CouncilTotalCount_DisabilityPremiumAwardHBTTs";
    protected static final String sCouncilPercentageOfHB_DisabilityPremiumAwardHBTTs = "CouncilPercentageOfHB_DisabilityPremiumAwardHBTTs";
    // SevereDisabilityPremiumAwardHBTTs
    protected static final String sCouncilTotalCount_SevereDisabilityPremiumAwardHBTTs = "CouncilTotalCount_SevereDisabilityPremiumAwardHBTTs";
    protected static final String sCouncilPercentageOfHB_SevereDisabilityPremiumAwardHBTTs = "CouncilPercentageOfHB_SevereDisabilityPremiumAwardHBTTs";
    // DisabledChildPremiumAwardHBTTs
    protected static final String sCouncilTotalCount_DisabledChildPremiumAwardHBTTs = "CouncilTotalCount_DisabledChildPremiumAwardHBTTs";
    protected static final String sCouncilPercentageOfHB_DisabledChildPremiumAwardHBTTs = "CouncilPercentageOfHB_DisabledChildPremiumAwardHBTTs";
    // EnhancedDisabilityPremiumAwardHBTTs
    protected static final String sCouncilTotalCount_EnhancedDisabilityPremiumAwardHBTTs = "CouncilTotalCount_EnhancedDisabilityPremiumAwardHBTTs";
    protected static final String sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs = "CouncilPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs";
    // DisabilityPremiumAwardSocialTTs
    protected static final String sCouncilTotalCount_DisabilityPremiumAwardSocialTTs = "CouncilTotalCount_DisabilityPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfHB_DisabilityPremiumAwardSocialTTs = "CouncilPercentageOfHB_DisabilityPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs = "CouncilPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs";
    // SevereDisabilityPremiumAwardSocialTTs
    protected static final String sCouncilTotalCount_SevereDisabilityPremiumAwardSocialTTs = "CouncilTotalCount_SevereDisabilityPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs = "CouncilPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs = "CouncilPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs";
    // DisabledChildPremiumAwardSocialTTs
    protected static final String sCouncilTotalCount_DisabledChildPremiumAwardSocialTTs = "CouncilTotalCount_DisabledChildPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfHB_DisabledChildPremiumAwardSocialTTs = "CouncilPercentageOfHB_DisabledChildPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs = "CouncilPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs";
    // EnhancedDisabilityPremiumAwardSocialTTs
    protected static final String sCouncilTotalCount_EnhancedDisabilityPremiumAwardSocialTTs = "CouncilTotalCount_EnhancedDisabilityPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs = "CouncilPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs";
    protected static final String sCouncilPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs = "CouncilPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs";
    // DisabilityAwardByTT
    protected static final String sCouncilTotalCount_DisabilityAwardByTT1 = "CouncilTotalCount_DisabilityAwardByTT1";
    protected static final String sCouncilPercentageOfHB_DisabilityAwardByTT1 = "CouncilPercentageOfHB_DisabilityAwardByTT1";
    protected static final String sCouncilPercentageOfTT_DisabilityAwardByTT1 = "CouncilPercentageOfTT_DisabilityAwardByTT1";
    // DisabilityAwardHBTTs
    protected static final String sCouncilTotalCount_DisabilityAwardHBTTs = "CouncilTotalCount_DisabilityAwardHBTTs";
    protected static final String sCouncilPercentageOfHB_DisabilityAwardHBTTs = "CouncilPercentageOfHB_DisabilityAwardHBTTs";
    // DisabilityAwardCTBTTs
    protected static final String sCouncilTotalCount_DisabilityAwardCTBTTs = "CouncilTotalCount_DisabilityAwardCTBTTs";
    protected static final String sCouncilPercentageOfCTB_DisabilityAwardCTBTTs = "CouncilPercentageOfCTB_DisabilityAwardCTBTTs";
    // DisabilityAwardSocialTTs
    protected static final String sCouncilTotalCount_DisabilityAwardSocialTTs = "CouncilTotalCount_DisabilityAwardSocialTTs";
    protected static final String sCouncilPercentageOfHB_DisabilityAwardSocialTTs = "CouncilPercentageOfHB_DisabilityAwardSocialTTs";
    protected static final String sCouncilPercentageOfSocialTTs_DisabilityAwardSocialTTs = "CouncilsPercentageOfSocialTTs_DisabilityAwardSocialTTs";
    // WeeklyHBEntitlement
    public static final String sCouncilTotalWeeklyHBEntitlement = "CouncilTotalWeeklyHBEntitlement";
    public static final String sCouncilTotalCount_WeeklyHBEntitlementNonZero = "CouncilTotalCount_WeeklyHBEntitlementNonZero";
    public static final String sCouncilTotalCount_WeeklyHBEntitlementZero = "CouncilTotalCount_WeeklyHBEntitlementZero";
    public static final String sCouncilAverageWeeklyHBEntitlement = "CouncilAverageWeeklyHBEntitlement";
    // WeeklyCTBEntitlement
    public static final String sCouncilTotalWeeklyCTBEntitlement = "CouncilTotalWeeklyCTBEntitlement";
    public static final String sCouncilTotalCount_WeeklyCTBEntitlementNonZero = "CouncilTotalCount_WeeklyCTBEntitlementNonZero";
    public static final String sCouncilTotalCount_WeeklyCTBEntitlementZero = "CouncilTotalCount_WeeklyCTBEntitlementZero";
    public static final String sCouncilAverageWeeklyCTBEntitlement = "CouncilAverageWeeklyCTBEntitlement";
    // WeeklyEligibleRentAmount
    public static final String sCouncilTotalWeeklyEligibleRentAmount = "CouncilTotalWeeklyEligibleRentAmount";
    public static final String sCouncilTotalCount_WeeklyEligibleRentAmountNonZero = "CouncilTotalCount_WeeklyEligibleRentAmountNonZero";
    public static final String sCouncilTotalCount_WeeklyEligibleRentAmountZero = "CouncilTotalCount_WeeklyEligibleRentAmountZero";
    public static final String sCouncilAverageWeeklyEligibleRentAmount = "CouncilAverageWeeklyEligibleRentAmount";
    public static final String sCouncilTotalWeeklyEligibleRentAmountTT1 = "CouncilHBTotalWeeklyEligibleRentAmount";
    public static final String sCouncilTotalCount_WeeklyEligibleRentAmountNonZeroTT1 = "CouncilHBTotalCount_WeeklyEligibleRentAmountNonZeroTT1";
    public static final String sCouncilTotalCount_WeeklyEligibleRentAmountZeroTT1 = "CouncilHBTotalCount_WeeklyEligibleRentAmountZeroTT1";
    public static final String sCouncilAverageWeeklyEligibleRentAmountTT1 = "CouncilHBAverageWeeklyEligibleRentAmountTT1";
    // WeeklyEligibleCouncilTaxAmount
    protected static final String sCouncilTotalWeeklyEligibleCouncilTaxAmount = "CouncilTotalCount_WeeklyEligibleCouncilTaxAmount";
    protected static final String sCouncilTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = "CouncilTotalCount_WeeklyEligibleCouncilTaxAmountNonZero";
    protected static final String sCouncilTotalCount_WeeklyEligibleCouncilTaxAmountZero = "CouncilTotalCount_WeeklyEligibleCouncilTaxAmountZero";
    protected static final String sCouncilAverageWeeklyEligibleCouncilTaxAmount = "CouncilAverageWeeklyEligibleCouncilTaxAmount";
    // ContractualRentAmount
    protected static final String sCouncilTotalContractualRentAmount = "CouncilTotalContractualRentAmount";
    protected static final String sCouncilTotalCountContractualRentAmountNonZeroCount = "CouncilTotalCount_ContractualRentAmountNonZero";
    protected static final String sCouncilTotalCountContractualRentAmountZeroCount = "CouncilTotalCount_ContractualRentAmountZero";
    protected static final String sCouncilAverageContractualRentAmount = "CouncilAverageContractualRentAmount";
    // WeeklyAdditionalDiscretionaryPayment
    protected static final String sCouncilTotalWeeklyAdditionalDiscretionaryPayment = "CouncilTotalWeeklyAdditionalDiscretionaryPayment";
    protected static final String sCouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero = "CouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero";
    protected static final String sCouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentZero = "CouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentZero";
    protected static final String sCouncilAverageWeeklyAdditionalDiscretionaryPayment = "CouncilAverageWeeklyAdditionalDiscretionaryPayment";
    // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
    protected static final String sCouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected static final String sCouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "CouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    protected static final String sCouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "CouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    protected static final String sCouncilAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "CouncilAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    // Employment
    protected static final String sCouncilTotalCount_ClaimantsEmployed = "CouncilTotalCount_ClaimantsEmployed";
    protected static final String sCouncilPercentageOfHB_ClaimantsEmployed = "CouncilPercentageOfHB_ClaimantsEmployed";
    protected static final String sCouncilTotalCountClaimantsSelfEmployed = "CouncilTotalCount_ClaimantsSelfEmployed";
    protected static final String sCouncilPercentageOfHB_ClaimantsSelfEmployed = "CouncilPercentageOfHB_ClaimantsSelfEmployed";
    protected static final String sCouncilTotalCountClaimantsStudents = "CouncilTotalCount_ClaimantsStudents";
    protected static final String sCouncilPercentageOfHB_ClaimantsStudents = "CouncilPercentageOfHB_ClaimantsStudents";
    protected static final String sCouncilTotalCount_LHACases = "CouncilTotalCount_LHACases";
    protected static final String sCouncilPercentageOfHB_LHACases = "CouncilPercentageOfHB_LHACases";
    // Counts
    protected static final String sCouncilCount00 = "CouncilCount00";
    protected static final String sCouncilCount0 = "CouncilCount0";
    protected static final String sCouncilCount1 = "CouncilCount1";
    // Income
    public static final String sCouncilHBTotalIncomeTT1 = "CouncilHBTotalIncomeTT1";
    public static final String sCouncilHBTotalCount_IncomeNonZeroTT1 = "CouncilHBTotalCount_IncomeNonZeroTT1";
    public static final String sCouncilHBTotalCount_IncomeZeroTT1 = "CouncilHBTotalCount_IncomeZeroTT1";
    public static final String sCouncilHBAverageIncomeTT1 = "CouncilHBAverageIncomeTT1";

    // RSL
    // Counter Strings
    // HouseholdSize
    protected static final String sRSLTotalHouseholdSize = "RSLTotalHouseholdSize";
    protected static final String sRSLAverageHouseholdSize = "RSLAverageHouseholdSize";
    // PSI
    protected static String[] sRSLTotalCount_PSI;
    protected static String[] sRSLPercentageOfHB_PSI;
    // PSIByTT4
    protected static String[] sRSLTotalCount_PSIByTT4;
    protected static String[] sRSLPercentageOfHB_PSIByTT4;
    protected static String[] sRSLPercentageOfTT_PSIByTT4;
    // DisabilityPremiumAwardByTT4
    protected static final String sRSLTotalCount_DisabilityPremiumAwardByTT4 = "RSLTotalCount_DisabilityPremiumAwardByTT4";
    protected static final String sRSLPercentageOfHB_DisabilityPremiumAwardByTT4 = "RSLPercentageOfHB_DisabilityPremiumAwardByTT4";
    protected static final String sRSLPercentageOfTT_DisabilityPremiumAwardByTT4 = "RSLPercentageOfTT_DisabilityPremiumAwardByTT4";
    // SevereDisabilityPremiumAwardByTT4
    protected static final String sRSLTotalCount_SevereDisabilityPremiumAwardByTT4 = "RSLTotalCount_SevereDisabilityPremiumAwardByTT4";
    protected static final String sRSLPercentageOfHB_SevereDisabilityPremiumAwardByTT4 = "RSLPercentageOfHB_SevereDisabilityPremiumAwardByTT4";
    protected static final String sRSLPercentageOfTT_SevereDisabilityPremiumAwardByTT4 = "RSLPercentageOfTT_SevereDisabilityPremiumAwardByTT4";
    // DisabledChildPremiumAwardByTT4
    protected static final String sRSLTotalCount_DisabledChildPremiumAwardByTT4 = "RSLTotalCount_DisabledChildPremiumAwardByTT4";
    protected static final String sRSLPercentageOfHB_DisabledChildPremiumAwardByTT4 = "RSLPercentageOfHB_DisabledChildPremiumAwardByTT4";
    protected static final String sRSLPercentageOfTT_DisabledChildPremiumAwardByTT4 = "RSLPercentageOfTT_DisabledChildPremiumAwardByTT4";
    // EnhancedDisabilityPremiumAwardByTT4
    protected static final String sRSLTotalCount_EnhancedDisabilityPremiumAwardByTT4 = "RSLTotalCount_EnhancedDisabilityPremiumAwardByTT4";
    protected static final String sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardByTT4 = "RSLPercentageOfHB_EnhancedDisabilityPremiumAwardByTT4";
    protected static final String sRSLPercentageOfTT_EnhancedDisabilityPremiumAwardByTT4 = "RSLPercentageOfTT_EnhancedDisabilityPremiumAwardByTT4";
    // DisabilityAwards
    protected static final String sRSLTotalCount_DisabilityAward = "RSLTotalCount_DisabilityAward";
    protected static final String sRSLPercentageOfHB_DisabilityAward = "RSLPercentageOfHB_DisabilityAward";
    // DisabilityPremiumAwards
    protected static final String sRSLTotalCount_DisabilityPremiumAward = "RSLTotalCount_DisabilityPremiumAward";
    protected static final String sRSLPercentageOfHB_DisabilityPremiumAward = "RSLPercentageOfHB_DisabilityPremiumAward";
    // SevereDisabilityPremiumAwards
    protected static final String sRSLTotalCount_SevereDisabilityPremiumAward = "RSLTotalCount_SevereDisabilityPremiumAward";
    protected static final String sRSLPercentageOfHB_SevereDisabilityPremiumAward = "RSLPercentageOfHB_SevereDisabilityPremiumAward";
    // DisabledChildPremiumAwards
    protected static final String sRSLTotalCount_DisabledChildPremiumAward = "RSLTotalCount_DisabledChildPremiumAward";
    protected static final String sRSLPercentageOfHB_DisabledChildPremiumAward = "RSLPercentageOfHB_DisabledChildPremiumAward";
    // EnhancedDisabilityPremiumAwards
    protected static final String sRSLTotalCount_EnhancedDisabilityPremiumAward = "RSLTotalCount_EnhancedDisabilityPremiumAward";
    protected static final String sRSLPercentageOfHB_EnhancedDisabilityPremiumAward = "RSLPercentageOfHB_EnhancedDisabilityPremiumAward";
    // DisabilityPremiumAwardHBTTs
    protected static final String sRSLTotalCount_DisabilityPremiumAwardHBTTs = "RSLTotalCount_DisabilityPremiumAwardHBTTs";
    protected static final String sRSLPercentageOfHB_DisabilityPremiumAwardHBTTs = "RSLPercentageOfHB_DisabilityPremiumAwardHBTTs";
    // SevereDisabilityPremiumAwardHBTTs
    protected static final String sRSLTotalCount_SevereDisabilityPremiumAwardHBTTs = "RSLTotalCount_SevereDisabilityPremiumAwardHBTTs";
    protected static final String sRSLPercentageOfHB_SevereDisabilityPremiumAwardHBTTs = "RSLPercentageOfHB_SevereDisabilityPremiumAwardHBTTs";
    // DisabledChildPremiumAwardHBTTs
    protected static final String sRSLTotalCount_DisabledChildPremiumAwardHBTTs = "RSLTotalCount_DisabledChildPremiumAwardHBTTs";
    protected static final String sRSLPercentageOfHB_DisabledChildPremiumAwardHBTTs = "RSLPercentageOfHB_DisabledChildPremiumAwardHBTTs";
    // EnhancedDisabilityPremiumAwardHBTTs
    protected static final String sRSLTotalCount_EnhancedDisabilityPremiumAwardHBTTs = "RSLTotalCount_EnhancedDisabilityPremiumAwardHBTTs";
    protected static final String sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs = "RSLPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs";
    // DisabilityPremiumAwardCTBTTs
    protected static final String sRSLTotalCount_DisabilityPremiumAwardCTBTTs = "RSLTotalCount_DisabilityPremiumAwardCTBTTs";
    protected static final String sRSLPercentageOfCTB_DisabilityPremiumAwardCTBTTs = "RSLPercentageOfCTB_DisabilityPremiumAwardCTBTTs";
    // SevereDisabilityPremiumAwardCTBTTs
    protected static final String sRSLTotalCount_SevereDisabilityPremiumAwardCTBTTs = "RSLTotalCount_SevereDisabilityPremiumAwardCTBTTs";
    protected static final String sRSLPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs = "RSLPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs";
    // DisabledChildPremiumAwardCTBTTs
    protected static final String sRSLTotalCount_DisabledChildPremiumAwardCTBTTs = "RSLTotalCount_DisabledChildPremiumAwardCTBTTs";
    protected static final String sRSLPercentageOfCTB_DisabledChildPremiumAwardCTBTTs = "RSLPercentageOfCTB_DisabledChildPremiumAwardCTBTTs";
    // EnhancedDisabilityPremiumAwardCTBTTs
    protected static final String sRSLTotalCount_EnhancedDisabilityPremiumAwardCTBTTs = "RSLTotalCount_EnhancedDisabilityPremiumAwardCTBTTs";
    protected static final String sRSLPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs = "RSLPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs";
    // DisabilityPremiumAwardSocialTTs
    protected static final String sRSLTotalCount_DisabilityPremiumAwardSocialTTs = "RSLTotalCount_DisabilityPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfHB_DisabilityPremiumAwardSocialTTs = "RSLPercentageOfHB_DisabilityPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs = "RSLPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs";
    // SevereDisabilityPremiumAwardSocialTTs
    protected static final String sRSLTotalCount_SevereDisabilityPremiumAwardSocialTTs = "RSLTotalCount_SevereDisabilityPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs = "RSLPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs = "RSLPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs";
    // DisabledChildPremiumAwardSocialTTs
    protected static final String sRSLTotalCount_DisabledChildPremiumAwardSocialTTs = "RSLTotalCount_DisabledChildPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfHB_DisabledChildPremiumAwardSocialTTs = "RSLPercentageOfHB_DisabledChildPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs = "RSLPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs";
    // EnhancedDisabilityPremiumAwardSocialTTs
    protected static final String sRSLTotalCount_EnhancedDisabilityPremiumAwardSocialTTs = "RSLTotalCount_EnhancedDisabilityPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs = "RSLPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs";
    protected static final String sRSLPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs = "RSLPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs";
    // DisabilityAwardByTT4
    protected static final String sRSLTotalCount_DisabilityAwardByTT4 = "RSLTotalCount_DisabilityAwardByTT4";
    protected static final String sRSLPercentageOfHB_DisabilityAwardByTT4 = "RSLPercentageOfHB_DisabilityAwardByTT4";
    protected static final String sRSLPercentageOfTT_DisabilityAwardByTT4 = "RSLPercentageOfTT_DisabilityAwardByTT4";
    // DisabilityAwardHBTTs
    protected static final String sRSLTotalCount_DisabilityAwardHBTTs = "RSLTotalCount_DisabilityAwardHBTTs";
    protected static final String sRSLPercentageOfHB_DisabilityAwardHBTTs = "RSLPercentageOfHB_DisabilityAwardHBTTs";
    // DisabilityAwardSocialTTs
    protected static final String sRSLTotalCount_DisabilityAwardSocialTTs = "RSLTotalCount_DisabilityAwardSocialTTs";
    protected static final String sRSLPercentageOfHB_DisabilityAwardSocialTTs = "RSLPercentageOfHB_DisabilityAwardSocialTTs";
    protected static final String sRSLPercentageOfSocialTTs_DisabilityAwardSocialTTs = "RSLsPercentageOfSocialTTs_DisabilityAwardSocialTTs";
    // WeeklyHBEntitlement
    public static final String sRSLTotalWeeklyHBEntitlement = "RSLTotalWeeklyHBEntitlement";
    public static final String sRSLTotalCount_WeeklyHBEntitlementNonZero = "RSLTotalCount_WeeklyHBEntitlementNonZero";
    public static final String sRSLTotalCount_WeeklyHBEntitlementZero = "RSLTotalCount_WeeklyHBEntitlementZero";
    public static final String sRSLAverageWeeklyHBEntitlement = "RSLAverageWeeklyHBEntitlement";
    // WeeklyCTBEntitlement
    public static final String sRSLTotalWeeklyCTBEntitlement = "RSLTotalWeeklyCTBEntitlement";
    public static final String sRSLTotalCount_WeeklyCTBEntitlementNonZero = "RSLTotalCount_WeeklyCTBEntitlementNonZero";
    public static final String sRSLTotalCount_WeeklyCTBEntitlementZero = "RSLTotalCount_WeeklyCTBEntitlementZero";
    public static final String sRSLAverageWeeklyCTBEntitlement = "RSLAverageWeeklyCTBEntitlement";
    // WeeklyEligibleRentAmount
    public static final String sRSLTotalWeeklyEligibleRentAmount = "RSLTotalWeeklyEligibleRentAmount";
    public static final String sRSLTotalCount_WeeklyEligibleRentAmountNonZero = "RSLTotalCount_WeeklyEligibleRentAmountNonZero";
    public static final String sRSLTotalCount_WeeklyEligibleRentAmountZero = "RSLTotalCount_WeeklyEligibleRentAmountZero";
    public static final String sRSLAverageWeeklyEligibleRentAmount = "RSLAverageWeeklyEligibleRentAmount";
    public static final String sRSLTotalWeeklyEligibleRentAmountTT4 = "RSLHBTotalWeeklyEligibleRentAmountTT4";
    public static final String sRSLTotalCount_WeeklyEligibleRentAmountNonZeroTT4 = "RSLHBTotalCount_WeeklyEligibleRentAmountNonZeroTT4";
    public static final String sRSLTotalCount_WeeklyEligibleRentAmountZeroTT4 = "RSLHBTotalCount_WeeklyEligibleRentAmountZeroTT4";
    public static final String sRSLAverageWeeklyEligibleRentAmountTT4 = "RSLHBAverageWeeklyEligibleRentAmountTT4";
    // WeeklyEligibleCouncilTaxAmount
    protected static final String sRSLTotalWeeklyEligibleCouncilTaxAmount = "RSLTotalCount_WeeklyEligibleCouncilTaxAmount";
    protected static final String sRSLTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = "RSLTotalCount_WeeklyEligibleCouncilTaxAmountNonZero";
    protected static final String sRSLTotalCount_WeeklyEligibleCouncilTaxAmountZero = "RSLTotalCount_WeeklyEligibleCouncilTaxAmountZero";
    protected static final String sRSLAverageWeeklyEligibleCouncilTaxAmount = "RSLAverageWeeklyEligibleCouncilTaxAmount";
    // ContractualRentAmount
    protected static final String sRSLTotalContractualRentAmount = "RSLTotalContractualRentAmount";
    protected static final String sRSLTotalCountContractualRentAmountNonZeroCount = "RSLTotalCount_ContractualRentAmountNonZero";
    protected static final String sRSLTotalCountContractualRentAmountZeroCount = "RSLTotalCount_ContractualRentAmountZero";
    protected static final String sRSLAverageContractualRentAmount = "RSLAverageContractualRentAmount";
    // WeeklyAdditionalDiscretionaryPayment
    protected static final String sRSLTotalWeeklyAdditionalDiscretionaryPayment = "RSLTotalWeeklyAdditionalDiscretionaryPayment";
    protected static final String sRSLTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero = "RSLTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero";
    protected static final String sRSLTotalCount_WeeklyAdditionalDiscretionaryPaymentZero = "RSLTotalCount_WeeklyAdditionalDiscretionaryPaymentZero";
    protected static final String sRSLAverageWeeklyAdditionalDiscretionaryPayment = "RSLAverageWeeklyAdditionalDiscretionaryPayment";
    // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
    protected static final String sRSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected static final String sRSLTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "RSLTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    protected static final String sRSLTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "RSLTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    protected static final String sRSLAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "RSLAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    // Employment
    protected static final String sRSLTotalCount_ClaimantsEmployed = "RSLTotalCount_ClaimantsEmployed";
    protected static final String sRSLPercentageOfHB_ClaimantsEmployed = "RSLPercentageOfHB_ClaimantsEmployed";
    protected static final String sRSLTotalCountClaimantsSelfEmployed = "RSLTotalCount_ClaimantsSelfEmployed";
    protected static final String sRSLPercentageOfHB_ClaimantsSelfEmployed = "RSLPercentageOfHB_ClaimantsSelfEmployed";
    protected static final String sRSLTotalCountClaimantsStudents = "RSLTotalCount_ClaimantsStudents";
    protected static final String sRSLPercentageOfHB_ClaimantsStudents = "RSLPercentageOfHB_ClaimantsStudents";
    protected static final String sRSLTotalCount_LHACases = "RSLTotalCount_LHACases";
    protected static final String sRSLPercentageOfHB_LHACases = "RSLPercentageOfHB_LHACases";
    // Counts
    protected static final String sRSLCount00 = "RSLCount00";
    protected static final String sRSLCount0 = "RSLCount0";
    protected static final String sRSLCount1 = "RSLCount1";
    protected static final String sRSLTotalCount_SocialTTsClaimant = "RSLTotalCount_SocialTTsClaimant";
    protected static final String sRSLPercentageOfAll_SocialTTsClaimant = "RSLPercentageOfAll_SocialTTsClaimant";
    protected static final String sRSLPercentageOfHB_SocialTTsClaimant = "RSLPercentageOfHB_SocialTTsClaimant";
    // Income
    public static final String sRSLHBTotalIncomeTT4 = "RSLHBTotalIncomeTT4";
    public static final String sRSLHBTotalCount_IncomeNonZeroTT4 = "RSLHBTotalCount_IncomeNonZeroTT4";
    public static final String sRSLHBTotalCount_IncomeZeroTT4 = "RSLHBTotalCount_IncomeZeroTT4";
    public static final String sRSLHBAverageIncomeTT4 = "RSLHBAverageIncomeTT4";

    // Files
    protected static final String sCouncilFilename00 = "CouncilFilename00";
    protected static final String sCouncilFilename0 = "CouncilFilename0";
    protected static final String sCouncilFilename1 = "CouncilFilename1";
    protected static final String sRSLFilename00 = "RSLFilename00";
    protected static final String sRSLFilename0 = "RSLFilename0";
    protected static final String sRSLFilename1 = "RSLFilename1";

    // Strings0
    // All
//    protected static final String sAllUOCount00 = "AllUOCount00";
//    protected static final String sAllUOCount0 = "AllUOCount0";
//    protected static final String sAllUOCount1 = "AllUOCount1";
    protected static final String sAllUOLinkedRecordCount00 = "AllUOLinkedRecordCount00";
    protected static final String sAllUOLinkedRecordCount0 = "AllUOLinkedRecordCount0";
    protected static final String sAllUOLinkedRecordCount1 = "AllUOLinkedRecordCount1";
    // Council
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
    protected static final String sRSLLinkedRecordCount00 = "RSLLinkedRecordCount00";
    protected static final String sRSLLinkedRecordCount0 = "RSLLinkedRecordCount0";
    protected static final String sRSLLinkedRecordCount1 = "RSLLinkedRecordCount1";

    // Demographics
    // Ethnicity
    // Council
    protected static String[] sCouncilTotalCount_EthnicGroupClaimant;
    protected static String[] sCouncilPercentageOfHB_EthnicGroupClaimant;
    // RSL
    protected static String[] sRSLTotalCount_EthnicGroupClaimant;
    protected static String[] sRSLPercentageOfHB_EthnicGroupClaimant;

    // Key Counts
    // Council
    protected static String[] sCouncilTotalCount_ClaimantTT;
    protected static String sCouncilPercentageOfLinkedRecords_ClaimantTT1 = "CouncilPercentageOfLinkedRecords_ClaimantTT1";
    protected static String sCouncilPercentageOfTT_ClaimantTT1 = "sCouncilPercentageOfTT_ClaimantTT1";
    // RSL
    protected static String[] sRSLTotalCount_ClaimantTT;
    protected static String sRSLPercentageOfLinkedRecords_ClaimantTT4 = "sRSLPercentageOfLinkedRecords_ClaimantTT4";
    protected static String sRSLPercentageOfTT_ClaimantTT4 = "sRSLPercentageOfTT_ClaimantTT4";

    // Postcode
    // Council
    protected static String sCouncilTotalCount_PostcodeValidFormat;
    protected static String sCouncilTotalCount_PostcodeValid;
    // RSL
    protected static String sRSLTotalCount_PostcodeValidFormat;
    protected static String sRSLTotalCount_PostcodeValid;

    // Compare 2 Times
    // Council
    // HB TT
    protected static final String sCouncilTotalCount_TTChangeClaimant = "CouncilTotalCount_TTChangeClaimant";
    protected static final String sCouncilPercentageOfHB_TTChangeClaimant = "CouncilPercentageOfHB_TTChangeClaimant";
    protected static final String sCouncilTotalCount_Minus999TTToTT1 = "CouncilTotalCount_Minus999TTToTT1";
    protected static final String sCouncilTotalCount_TT1ToMinus999TT = "CouncilTotalCount_TT1ToMinus999TT";

    protected static final String sCouncilTotalCount_TT1ToHBTTs = "CouncilTotalCount_TT1ToHBTTs";
    protected static final String sCouncilPercentageOfHB_HBTTsToTT1 = "CouncilPercentageOfHB_HBTTsToTT1";
    protected static final String sCouncilTotalCount_TT1ToPrivateDeregulatedTTs = "CouncilTotalCount_TT1ToPrivateDeregulatedTTs";
    protected static final String sCouncilPercentageOfTT1_TT1ToPrivateDeregulatedTTs = "CouncilPercentageOfTT1_TT1ToPrivateDeregulatedTTs";
    protected static final String sCouncilTotalCount_PrivateDeregulatedTTsToTT1 = "CouncilTotalCount_PrivateDeregulatedTTsToTT1";
    protected static final String sCouncilTotalCount_TT1ToTT4 = "CouncilTotalCount_TT1ToTT4";
    protected static final String sCouncilPercentageOfTT1_TT1ToTT4 = "CouncilPercentageOfTT1_TT1ToTT4";
    protected static final String sCouncilTotalCount_TT4ToTT1 = "CouncilTotalCount_TT4ToTT1";
    protected static final String sCouncilPercentageOfTT4_TT4ToTT1 = "CouncilPercentageOfTT4_TT4ToTT1";
    protected static final String sCouncilTotalCount_PostcodeChangeWithinTT1 = "CouncilTotalCount_PostcodeChangeWithinTT1";
    protected static final String sCouncilPercentageOfTT1_PostcodeChangeWithinTT1 = "CouncilPercentageOfTT1_PostcodeChangeWithinTT1";
    // HB Postcode
    protected static final String sCouncilTotalCount_Postcode0ValidPostcode1Valid = "CouncilTotalCount_Postcode0ValidPostcode1Valid";
    protected static final String sCouncilPercentagePostcode0ValidPostcode1Valid = "CouncilPercentagePostcode0ValidPostcode1Valid";
    protected static final String sCouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = "CouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sCouncilPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged = "CouncilPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sCouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeChange = "CouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeChange";
    protected static final String sCouncilPercentagePostcode0ValidPostcode1ValidPostcodeChange = "CouncilPercentagePostcode0ValidPostcode1ValidPostcodeChange";
    protected static final String sCouncilTotalCount_Postcode0ValidPostcode1NotValid = "CouncilTotalCount_Postcode0ValidPostcode1NotValid";
    protected static final String sCouncilPercentagePostcode0ValidPostcode1NotValid = "CouncilPercentagePostcode0ValidPostcode1NotValid";
    protected static final String sCouncilTotalCount_Postcode0NotValidPostcode1Valid = "CouncilTotalCount_Postcode0NotValidPostcode1Valid";
    protected static final String sCouncilPercentagePostcode0NotValidPostcode1Valid = "CouncilPercentagePostcode0NotValidPostcode1Valid";
    protected static final String sCouncilTotalCount_Postcode0NotValidPostcode1NotValid = "CouncilTotalCount_Postcode0NotValidPostcode1NotValid";
    protected static final String sCouncilPercentagePostcode0NotValidPostcode1NotValid = "CouncilPercentagePostcode0NotValidPostcode1NotValid";
    protected static final String sCouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = "CouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sCouncilPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged = "CouncilPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sCouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = "CouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged";
    protected static final String sCouncilPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged = "CouncilPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged";
    // RSL
    // HB TT
    protected static final String sRSLTotalCount_TTChangeClaimant = "RSLTotalCount_TTChangeClaimant";
    protected static final String sRSLPercentageOfHB_TTChangeClaimant = "RSLPercentageOfHB_TTChangeClaimant";
    protected static final String sRSLTotalCount_Minus999TTToTT4 = "RSLTotalCount_Minus999TTToTT4";
    protected static final String sRSLTotalCount_TT4ToMinus999TT = "RSLTotalCount_TT4ToMinus999TT";

    protected static final String sRSLTotalCount_TT4ToHBTTs = "RSLTotalCount_TT4ToHBTTs";
    protected static final String sRSLPercentageOfHB_HBTTsToTT4 = "RSLPercentageOfHB_HBTTsToTT4";
    protected static final String sRSLTotalCount_TT4ToPrivateDeregulatedTTs = "RSLTotalCount_TT4ToPrivateDeregulatedTTs";
    protected static final String sRSLPercentageOfTT4_TT4ToPrivateDeregulatedTTs = "RSLPercentageOfTT4_TT4ToPrivateDeregulatedTTs";
    protected static final String sRSLTotalCount_PrivateDeregulatedTTsToTT4 = "RSLTotalCount_PrivateDeregulatedTTsToTT4";
    protected static final String sRSLTotalCount_TT4ToTT1 = "RSLTotalCount_TT4ToTT1";
    protected static final String sRSLPercentageOfTT4_TT4ToTT1 = "RSLPercentageOfTT4_TT4ToTT1";
    protected static final String sRSLTotalCount_TT1ToTT4 = "RSLTotalCount_TT1ToTT4";
    protected static final String sRSLPercentageOfTT1_TT1ToTT4 = "RSLPercentageOfTT1_TT1ToTT4";
    protected static final String sRSLTotalCount_PostcodeChangeWithinTT4 = "RSLTotalCount_PostcodeChangeWithinTT4";
    protected static final String sRSLPercentageOfTT4_PostcodeChangeWithinTT4 = "RSLPercentageOfTT4_PostcodeChangeWithinTT4";

    // HB Postcode
    protected static final String sRSLTotalCount_Postcode0ValidPostcode1Valid = "RSLTotalCount_Postcode0ValidPostcode1Valid";
    protected static final String sRSLPercentagePostcode0ValidPostcode1Valid = "RSLPercentagePostcode0ValidPostcode1Valid";
    protected static final String sRSLTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = "RSLTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sRSLPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged = "RSLPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged";
    protected static final String sRSLTotalCount_Postcode0ValidPostcode1ValidPostcodeChange = "RSLTotalCount_Postcode0ValidPostcode1ValidPostcodeChange";
    protected static final String sRSLPercentagePostcode0ValidPostcode1ValidPostcodeChange = "RSLPercentagePostcode0ValidPostcode1ValidPostcodeChange";
    protected static final String sRSLTotalCount_Postcode0ValidPostcode1NotValid = "RSLTotalCount_Postcode0ValidPostcode1NotValid";
    protected static final String sRSLPercentagePostcode0ValidPostcode1NotValid = "RSLPercentagePostcode0ValidPostcode1NotValid";
    protected static final String sRSLTotalCount_Postcode0NotValidPostcode1Valid = "RSLTotalCount_Postcode0NotValidPostcode1Valid";
    protected static final String sRSLPercentagePostcode0NotValidPostcode1Valid = "RSLPercentagePostcode0NotValidPostcode1Valid";
    protected static final String sRSLTotalCount_Postcode0NotValidPostcode1NotValid = "RSLTotalCount_Postcode0NotValidPostcode1NotValid";
    protected static final String sRSLPercentagePostcode0NotValidPostcode1NotValid = "RSLPercentagePostcode0NotValidPostcode1NotValid";
    protected static final String sRSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = "RSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sRSLPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged = "RSLPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected static final String sRSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = "RSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged";
    protected static final String sRSLPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged = "RSLPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged";

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
    // All
    protected static int AllUOAllCount00;
    protected static int AllUOAllCount0;
    protected static int AllUOAllCount1;
//    protected static int CouncilAllCount1;
//    protected static int  RSLCount1;
    // Council
    // Single Time
    // HB
    protected static double CouncilTotalWeeklyHBEntitlement;
    protected static int CouncilTotalCount_WeeklyHBEntitlementNonZero;
    protected static int CouncilTotalCount_WeeklyHBEntitlementZero;
    protected static double CouncilTotalWeeklyCTBEntitlement;
    protected static int CouncilTotalCount_WeeklyCTBEntitlementNonZero;
    protected static int CouncilTotalWeeklyCTBEntitlementZeroCount;
    protected static double CouncilTotalWeeklyEligibleRentAmount;
    protected static int CouncilTotalWeeklyEligibleRentAmountNonZeroCount;
    protected static int CouncilTotalWeeklyEligibleRentAmountZeroCount;

    protected static double CouncilTotalWeeklyEligibleRentAmountTT1;
    protected static int CouncilTotalWeeklyEligibleRentAmountNonZeroCountTT1;
    protected static int CouncilTotalWeeklyEligibleRentAmountZeroCountTT1;

    protected static double CouncilTotalWeeklyEligibleCouncilTaxAmount;
    protected static int CouncilTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
    protected static int CouncilTotalCount_WeeklyEligibleCouncilTaxAmountZero;
    protected static double CouncilTotalContractualRentAmount;
    protected static int CouncilTotalContractualRentAmountNonZeroCount;
    protected static int CouncilTotalContractualRentAmountZeroCount;
    protected static double CouncilTotalWeeklyAdditionalDiscretionaryPayment;
    protected static int CouncilTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    protected static int CouncilTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    protected static double CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    protected static int CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    protected static int CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;
    // Employment Education Training
    protected static int CouncilTotalCount_EmployedClaimants;
    protected static int CouncilTotalCount_SelfEmployedClaimants;
    protected static int CouncilTotalCount_StudentsClaimants;
    // HLA
    protected static int CouncilTotalCount_LHACases;
    protected static int[] CouncilTotalCount_DisabilityPremiumAwardByTT;
    protected static int[] CouncilTotalCount_SevereDisabilityPremiumAwardByTT;
    protected static int[] CouncilTotalCount_DisabledChildPremiumAwardByTT;
    protected static int[] CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT;
    protected static int[] CouncilTotalCount_DisabilityAwardByTT;
    protected static int[] CouncilTotalCount_PSI;
    protected static int[] CouncilTotalCount_PSIByTT1;
    protected static int[] CouncilTotalCount_TTClaimant1;
    protected static int[] CouncilTotalCount_TTClaimant0;
    protected static long CouncilTotalHouseholdSize;
//    protected static int CouncilAllCount1;
//    protected static Integer CouncilAllCount0;
    // HB
    protected static int CouncilCount1;
    protected static Integer CouncilCount0;
    protected static int[] CouncilEthnicGroupCount;
    protected static int[] CouncilEthnicGroupCountByTT;
    protected static int CouncilTotalCount_PostcodeValidFormat;
    protected static int CouncilTotalCount_PostcodeValid;
    // Other summary stats
    protected static double CouncilTotal_RentArrears;
    protected static double CouncilTotalCount_RentArrears;
    protected static int CouncilTotalCount_RentArrearsNonZero;
    protected static int CouncilTotalCount_RentArrearsZero;
    // RSL
    // Single Time
    // HB
    protected static double RSLTotalWeeklyHBEntitlement;
    protected static int RSLTotalCount_WeeklyHBEntitlementNonZero;
    protected static int RSLTotalCount_WeeklyHBEntitlementZero;
    protected static double RSLTotalWeeklyCTBEntitlement;
    protected static int RSLTotalCount_WeeklyCTBEntitlementNonZero;
    protected static int RSLTotalWeeklyCTBEntitlementZeroCount;
    protected static double RSLTotalWeeklyEligibleRentAmount;
    protected static int RSLTotalWeeklyEligibleRentAmountNonZeroCount;
    protected static int RSLTotalWeeklyEligibleRentAmountZeroCount;
    protected static double RSLTotalWeeklyEligibleRentAmountTT4;
    protected static int RSLTotalWeeklyEligibleRentAmountNonZeroCountTT4;
    protected static int RSLTotalWeeklyEligibleRentAmountZeroCountTT4;
    protected static double RSLTotalWeeklyEligibleCouncilTaxAmount;
    protected static int RSLTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
    protected static int RSLTotalCount_WeeklyEligibleCouncilTaxAmountZero;
    protected static double RSLTotalContractualRentAmount;
    protected static int RSLTotalContractualRentAmountNonZeroCount;
    protected static int RSLTotalContractualRentAmountZeroCount;
    protected static double RSLTotalWeeklyAdditionalDiscretionaryPayment;
    protected static int RSLTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    protected static int RSLTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    protected static double RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    protected static int RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    protected static int RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;
    // Employment Education Training
    protected static int RSLTotalCount_EmployedClaimants;
    protected static int RSLTotalCount_SelfEmployedClaimants;
    protected static int RSLTotalCount_StudentsClaimants;
    // HLA
    protected static int RSLTotalCount_LHACases;
    // Disability
    protected static int[] RSLTotalCount_DisabilityPremiumAwardByTT;
    protected static int[] RSLTotalCount_SevereDisabilityPremiumAwardByTT;
    protected static int[] RSLTotalCount_DisabledChildPremiumAwardByTT;
    protected static int[] RSLTotalCount_EnhancedDisabilityPremiumAwardByTT;
    protected static int[] RSLTotalCount_DisabilityAwardByTT;
    // Key Counts
    protected static int[] RSLTotalCount_PSI;
    protected static int[] RSLTotalCount_PSIByTT4;
    protected static int[] RSLTotalCount_TTClaimant1;
    protected static int[] RSLTotalCount_TTClaimant0;
    protected static long RSLTotalHouseholdSize;
    // HB
    protected static int RSLCount1;
    protected static Integer RSLCount0;
    protected static int[] RSLEthnicGroupCount;
    protected static int[] RSLEthnicGroupCountByTT;
    protected static int RSLTotalCount_PostcodeValidFormat;
    protected static int RSLTotalCount_PostcodeValid;

    // Compare 2 Times
    // Council
    protected static int CouncilTotalCount_TT1ToMinus999TT;
    protected static int CouncilTotalCount_Minus999TTToTT1;
    protected static int CouncilTotalCount_TT1ToPrivateDeregulatedTTs;
    protected static int CouncilTotalCount_PrivateDeregulatedTTsToTT1;
    protected static int CouncilTotalCount_TT1ToCTBTTs;
    protected static int CouncilTotalCount_CTBTTsToTT1;
    protected static int CouncilTotalCount_TT1ToTT4;
    protected static int CouncilTotalCount_TT4ToTT1;
    protected static int CouncilTotalCount_TTChangeClaimant;
    protected static int CouncilTotalCount_PostcodeChangeWithinTT1;
    protected static int CouncilTotalCount_Postcode0ValidPostcode1Valid;
    protected static int CouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged;
    protected static int CouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged;
    protected static int CouncilTotalCount_Postcode0ValidPostcode1NotValid;
    protected static int CouncilTotalCount_Postcode0NotValidPostcode1Valid;
    protected static int CouncilTotalCount_Postcode0NotValidPostcode1NotValid;
    protected static int CouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged;
    protected static int CouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged;
    // RSL
    protected static int RSLTotalCount_TT4ToMinus999TT;
    protected static int RSLTotalCount_Minus999TTToTT4;
    protected static int RSLTotalCount_TT4ToPrivateDeregulatedTTs;
    protected static int RSLTotalCount_PrivateDeregulatedTTsToTT4;
    protected static int RSLTotalCount_TT4ToCTBTTs;
    protected static int RSLTotalCount_CTBTTsToTT4;
    protected static int RSLTotalCount_TT4ToTT1;
    protected static int RSLTotalCount_TT1ToTT4;
    protected static int RSLTotalCount_TTChangeClaimant;
    protected static int RSLTotalCount_PostcodeChangeWithinTT4;
    protected static int RSLTotalCount_Postcode0ValidPostcode1Valid;
    protected static int RSLTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged;
    protected static int RSLTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged;
    protected static int RSLTotalCount_Postcode0ValidPostcode1NotValid;
    protected static int RSLTotalCount_Postcode0NotValidPostcode1Valid;
    protected static int RSLTotalCount_Postcode0NotValidPostcode1NotValid;
    protected static int RSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged;
    protected static int RSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged;

//    protected static int AllUOAllCount00;
//    protected static int AllUOAllCount0;
//    protected static int AllUOAllCount1;
    protected static int AllUOLinkedRecordCount00;
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
            int nTT,
            int nEG,
            int nPSI,
            boolean handleOutOfMemoryError) {
        super(env, collectionHandler, nTT, nEG, nPSI, handleOutOfMemoryError);
        init(nTT, nEG, nPSI);
    }

    @Override
    protected void init(int nTT, int nEG, int nPSI) {
        initSingleTimeStrings(nTT, nEG, nPSI);
        initCompare3TimesStrings(nTT, nEG);
        // Council
        CouncilTotalCount_DisabilityPremiumAwardByTT = new int[nTT];
        CouncilTotalCount_SevereDisabilityPremiumAwardByTT = new int[nTT];
        CouncilTotalCount_DisabledChildPremiumAwardByTT = new int[nTT];
        CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT = new int[nTT];
        CouncilTotalCount_DisabilityAwardByTT = new int[nTT];
        CouncilTotalCount_PSI = new int[nPSI];
        CouncilTotalCount_PSIByTT1 = new int[nPSI];
        CouncilTotalCount_TTClaimant1 = new int[nTT];
        CouncilTotalCount_TTClaimant0 = new int[nTT];
        CouncilEthnicGroupCount = new int[nEG];
        // RSL
        RSLTotalCount_DisabilityPremiumAwardByTT = new int[nTT];
        RSLTotalCount_SevereDisabilityPremiumAwardByTT = new int[nTT];
        RSLTotalCount_DisabledChildPremiumAwardByTT = new int[nTT];
        RSLTotalCount_EnhancedDisabilityPremiumAwardByTT = new int[nTT];
        RSLTotalCount_DisabilityAwardByTT = new int[nTT];
        RSLTotalCount_PSI = new int[nPSI];
        RSLTotalCount_PSIByTT4 = new int[nPSI];
        RSLTotalCount_TTClaimant1 = new int[nTT];
        RSLTotalCount_TTClaimant0 = new int[nTT];
        RSLEthnicGroupCount = new int[nEG];
    }

    @Override
    protected void initSingleTimeStrings(int nTT, int nEG, int nPSI) {
        super.initSingleTimeStrings(nTT, nEG, nPSI);
        // Council
        sCouncilTotalCount_PSI = new String[nPSI];
        sCouncilPercentageOfHB_PSI = new String[nPSI];
        sCouncilTotalCount_PSIByTT1 = new String[nPSI];
        sCouncilPercentageOfHB_PSIByTT1 = new String[nPSI];
        sCouncilPercentageOfTT_PSIByTT1 = new String[nPSI];
        for (int i = 1; i < nPSI; i++) {
            sCouncilTotalCount_PSI[i] = "CouncilTotalCount_PSI" + i;
            sCouncilPercentageOfHB_PSI[i] = "CouncilPercentageOfHB_PSI" + i;
            sCouncilTotalCount_PSIByTT1[i] = "CouncilTotalCount_PSI" + i + "TT1";
            sCouncilPercentageOfHB_PSIByTT1[i] = "CouncilPercentageOfHB_PSI" + i + "TT1";
            sCouncilPercentageOfTT_PSIByTT1[i] = "CouncilPercentageOfTT_PSI" + i + "TT1";
        }
        // All
        sCouncilTotalCount_EthnicGroupClaimant = new String[nEG];
        sCouncilPercentageOfHB_EthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            String EGN = tDW_SHBE_Handler.getEthnicityGroupName(i);
            sCouncilTotalCount_EthnicGroupClaimant[i] = "CouncilTotalCount_EthnicGroup_" + EGN + "_Claimant";
            sCouncilPercentageOfHB_EthnicGroupClaimant[i] = "CouncilPercentageOfAll_EthnicGroup_" + EGN + "_Claimant";
        }
        sCouncilTotalCount_ClaimantTT = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            // Claimants
            sCouncilTotalCount_ClaimantTT[i] = "CouncilTotalCount_ClaimantTT" + i;
        }
        sCouncilTotalCount_PostcodeValidFormat = "CouncilTotalCount_PostcodeValidFormat";
        sCouncilTotalCount_PostcodeValid = "CouncilTotalCount_PostcodeValid";
        // RSL
        sRSLTotalCount_PSI = new String[nPSI];
        sRSLPercentageOfHB_PSI = new String[nPSI];
        sRSLTotalCount_PSIByTT4 = new String[nPSI];
        sRSLPercentageOfHB_PSIByTT4 = new String[nPSI];
        sRSLPercentageOfTT_PSIByTT4 = new String[nPSI];
        for (int i = 1; i < nPSI; i++) {
            sRSLTotalCount_PSI[i] = "RSLTotalCount_PSI" + i;
            sRSLPercentageOfHB_PSI[i] = "RSLPercentageOfHB_PSI" + i;
            sRSLTotalCount_PSIByTT4[i] = "RSLTotalCount_PSI" + i + "TT4";
            sRSLPercentageOfHB_PSIByTT4[i] = "RSLPercentageOfHB_PSI" + i + "TT4";
            sRSLPercentageOfTT_PSIByTT4[i] = "RSLPercentageOfTT_PSI" + i + "TT4";
        }
        sRSLTotalCount_ClaimantTT = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            // Claimants
            sRSLTotalCount_ClaimantTT[i] = "RSLTotalCount_ClaimantTT" + i;
        }
        sRSLTotalCount_PostcodeValidFormat = "RSLTotalCount_PostcodeValidFormat";
        sRSLTotalCount_PostcodeValid = "RSLTotalCount_PostcodeValid";
        sRSLTotalCount_EthnicGroupClaimant = new String[nEG];
        sRSLPercentageOfHB_EthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            sRSLTotalCount_EthnicGroupClaimant[i] = "RSLTotalCount_EthnicGroup" + i + "Claimant";
            sRSLPercentageOfHB_EthnicGroupClaimant[i] = "RSLPercentageOfHB_EthnicGroup" + i + "Claimant";
        }
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

        AllUOAllCount1 = 0;

        // Council
        for (int i = 1; i < nPSI; i++) {
            CouncilTotalCount_PSI[i] = 0;
            CouncilTotalCount_PSIByTT1[i] = 0;
        }
        // HB
        CouncilTotalWeeklyHBEntitlement = 0.0d;
        CouncilTotalCount_WeeklyHBEntitlementNonZero = 0;
        CouncilTotalCount_WeeklyHBEntitlementZero = 0;
        CouncilTotalWeeklyCTBEntitlement = 0.0d;
        CouncilTotalCount_WeeklyCTBEntitlementNonZero = 0;
        CouncilTotalWeeklyCTBEntitlementZeroCount = 0;
        CouncilTotalWeeklyEligibleRentAmount = 0.0d;
        CouncilTotalWeeklyEligibleRentAmountNonZeroCount = 0;
        CouncilTotalWeeklyEligibleRentAmountZeroCount = 0;
        CouncilTotalWeeklyEligibleCouncilTaxAmount = 0.0d;
        CouncilTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = 0;
        CouncilTotalCount_WeeklyEligibleCouncilTaxAmountZero = 0;
        CouncilTotalContractualRentAmount = 0.0d;
        CouncilTotalContractualRentAmountNonZeroCount = 0;
        CouncilTotalContractualRentAmountZeroCount = 0;
        CouncilTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        CouncilTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        CouncilTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;
        // Demographic Counts
        CouncilTotalCount_EmployedClaimants = 0;
        CouncilTotalCount_SelfEmployedClaimants = 0;
        CouncilTotalCount_StudentsClaimants = 0;
        CouncilTotalCount_LHACases = 0;
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
            CouncilEthnicGroupCount[i] = 0;
        }
        // Key Counts
        CouncilCount1 = 0;
        // Postcode Counts
        CouncilTotalCount_PostcodeValidFormat = 0;
        CouncilTotalCount_PostcodeValid = 0;
        CouncilTotalCount_TTChangeClaimant = 0;
        // Household Size
        CouncilTotalHouseholdSize = 0L;

        // RSL
        for (int i = 1; i < nPSI; i++) {
            RSLTotalCount_PSI[i] = 0;
            RSLTotalCount_PSIByTT4[i] = 0;
        }
        // HB
        RSLTotalWeeklyHBEntitlement = 0.0d;
        RSLTotalCount_WeeklyHBEntitlementNonZero = 0;
        RSLTotalCount_WeeklyHBEntitlementZero = 0;
        RSLTotalWeeklyCTBEntitlement = 0.0d;
        RSLTotalCount_WeeklyCTBEntitlementNonZero = 0;
        RSLTotalWeeklyCTBEntitlementZeroCount = 0;
        RSLTotalWeeklyEligibleRentAmount = 0.0d;
        RSLTotalWeeklyEligibleRentAmountNonZeroCount = 0;
        RSLTotalWeeklyEligibleRentAmountZeroCount = 0;
        RSLTotalWeeklyEligibleCouncilTaxAmount = 0.0d;
        RSLTotalCount_WeeklyEligibleCouncilTaxAmountNonZero = 0;
        RSLTotalCount_WeeklyEligibleCouncilTaxAmountZero = 0;
        RSLTotalContractualRentAmount = 0.0d;
        RSLTotalContractualRentAmountNonZeroCount = 0;
        RSLTotalContractualRentAmountZeroCount = 0;
        RSLTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        RSLTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        RSLTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;
        // Demographic Counts
        RSLTotalCount_EmployedClaimants = 0;
        RSLTotalCount_SelfEmployedClaimants = 0;
        RSLTotalCount_StudentsClaimants = 0;
        RSLTotalCount_LHACases = 0;
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
            RSLEthnicGroupCount[i] = 0;
        }
        // Key Counts
        RSLCount1 = 0;
        // Postcode Counts
        RSLTotalCount_PostcodeValidFormat = 0;
        RSLTotalCount_PostcodeValid = 0;
        RSLTotalCount_TTChangeClaimant = 0;
        // Household Size
        RSLTotalHouseholdSize = 0L;

        // UO only
//        AllUOAllCount1 = 0;
//        CouncilAllCount1 = 0;
//        RSLCount1 = 0;
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
        // General HB related
        CouncilTotalCount_Minus999TTToTT1 = 0;
        CouncilTotalCount_TT1ToMinus999TT = 0;
        CouncilTotalCount_TT1ToPrivateDeregulatedTTs = 0;
        CouncilTotalCount_PrivateDeregulatedTTsToTT1 = 0;
        CouncilTotalCount_TT1ToCTBTTs = 0;
        CouncilTotalCount_CTBTTsToTT1 = 0;
        CouncilTotalCount_TT1ToTT4 = 0;
        CouncilTotalCount_TT4ToTT1 = 0;
        CouncilTotalCount_PostcodeChangeWithinTT1 = 0;
        // HB
        CouncilTotalCount_TTChangeClaimant = 0;
        CouncilTotalCount_Postcode0ValidPostcode1Valid = 0;
        CouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        CouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged = 0;
        CouncilTotalCount_Postcode0ValidPostcode1NotValid = 0;
        CouncilTotalCount_Postcode0NotValidPostcode1Valid = 0;
        CouncilTotalCount_Postcode0NotValidPostcode1NotValid = 0;
        CouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        CouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = 0;
        //  RSL
        // General
        // General HB related
        RSLTotalCount_Minus999TTToTT4 = 0;
        RSLTotalCount_TT4ToMinus999TT = 0;
        RSLTotalCount_TT4ToPrivateDeregulatedTTs = 0;
        RSLTotalCount_PrivateDeregulatedTTsToTT4 = 0;
        RSLTotalCount_TT4ToCTBTTs = 0;
        RSLTotalCount_CTBTTsToTT4 = 0;
        RSLTotalCount_TT1ToTT4 = 0;
        RSLTotalCount_TT4ToTT1 = 0;
        RSLTotalCount_PostcodeChangeWithinTT4 = 0;
        // HB
        RSLTotalCount_TTChangeClaimant = 0;
        RSLTotalCount_Postcode0ValidPostcode1Valid = 0;
        RSLTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        RSLTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged = 0;
        RSLTotalCount_Postcode0ValidPostcode1NotValid = 0;
        RSLTotalCount_Postcode0NotValidPostcode1Valid = 0;
        RSLTotalCount_Postcode0NotValidPostcode1NotValid = 0;
        RSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        RSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged = 0;
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

    public HashSet<ID_TenancyType> getID_TenancyTypeSet(
            TreeMap<DW_ID, DW_SHBE_Record> D_Records,
            DW_UO_Set councilUnderOccupiedSet,
            DW_UO_Set RSLUnderOccupiedSet,
            HashMap<DW_PersonID, DW_ID> DW_PersonIDToIDLookup) {
        HashSet<ID_TenancyType> result;
        result = new HashSet<ID_TenancyType>();
        TreeMap<DW_ID, DW_UO_Record> RSLMap;
        RSLMap = RSLUnderOccupiedSet.getMap();
        TreeMap<DW_ID, DW_UO_Record> councilMap;
        councilMap = councilUnderOccupiedSet.getMap();
        Iterator<DW_ID> ite;
        ite = councilMap.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            if (D_Records.containsKey(ClaimID)) {
                DW_SHBE_D_Record D_Record;
                D_Record = D_Records.get(ClaimID).getDRecord();
                DW_PersonID DW_PersonID;
                DW_PersonID = tDW_SHBE_Handler.getClaimantDW_PersonID(D_Record);
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
                // @TODO Keep CTBRef in a collection
            }
        }
        ite = RSLMap.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            if (D_Records.containsKey(ClaimID)) {
                DW_SHBE_D_Record D_Record;
                D_Record = D_Records.get(ClaimID).getDRecord();
                DW_PersonID DW_PersonID;
                DW_PersonID = tDW_SHBE_Handler.getClaimantDW_PersonID(D_Record);
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
                // @TODO Keep CTBRef in a collection
            }
        }
        return result;
    }

    public HashSet<ID_PostcodeID> getID_PostcodeIDSet(
            TreeMap<DW_ID, DW_SHBE_Record> D_Records,
            DW_UO_Set councilUnderOccupiedSet,
            DW_UO_Set RSLUnderOccupiedSet,
            HashMap<DW_PersonID, DW_ID> DW_PersonIDToIDLookup,
            HashMap<String, DW_ID> PostcodeToPostcodeIDLookup) {
        HashSet<ID_PostcodeID> result;
        result = new HashSet<ID_PostcodeID>();
        TreeMap<DW_ID, DW_UO_Record> RSLMap;
        RSLMap = RSLUnderOccupiedSet.getMap();
        TreeMap<DW_ID, DW_UO_Record> councilMap;
        councilMap = councilUnderOccupiedSet.getMap();
        Iterator<DW_ID> ite;
        ite = councilMap.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            DW_SHBE_D_Record D_Record;
            if (D_Records.containsKey(ClaimID)) {
                D_Record = D_Records.get(ClaimID).getDRecord();
                DW_PersonID DW_PersonID;
                DW_PersonID = tDW_SHBE_Handler.getClaimantDW_PersonID(D_Record);
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
                // @TODO Keep CTBRef in a collection
            }
        }
        ite = RSLMap.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            DW_SHBE_D_Record D_Record;
            if (D_Records.containsKey(ClaimID)) {
                D_Record = D_Records.get(ClaimID).getDRecord();
                DW_PersonID DW_PersonID;
                DW_PersonID = tDW_SHBE_Handler.getClaimantDW_PersonID(D_Record);
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
                // @TODO Keep CTBRef in a collection
            }
        }
        return result;
    }

    public HashSet<ID_TenancyType_PostcodeID> getID_TenancyType_PostcodeIDSet(
            TreeMap<DW_ID, DW_SHBE_Record> D_Records,
            DW_UO_Set councilUnderOccupiedSet,
            DW_UO_Set RSLUnderOccupiedSet,
            HashMap<DW_PersonID, DW_ID> DW_PersonIDToIDLookup,
            HashMap<String, DW_ID> PostcodeToPostcodeIDLookup) {
        HashSet<ID_TenancyType_PostcodeID> result;
        result = new HashSet<ID_TenancyType_PostcodeID>();
        TreeMap<DW_ID, DW_UO_Record> RSLMap;
        RSLMap = RSLUnderOccupiedSet.getMap();
        TreeMap<DW_ID, DW_UO_Record> councilMap;
        councilMap = councilUnderOccupiedSet.getMap();
        Iterator<DW_ID> ite;
        ite = councilMap.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            if (D_Records.containsKey(ClaimID)) {
                DW_SHBE_D_Record D_Record;
                D_Record = D_Records.get(ClaimID).getDRecord();
                DW_PersonID DW_PersonID;
                DW_PersonID = tDW_SHBE_Handler.getClaimantDW_PersonID(D_Record);
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
                // @TODO Keep CTBRef in a collection
            }
        }
        ite = RSLMap.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            if (D_Records.containsKey(ClaimID)) {
                DW_SHBE_D_Record D_Record;
                D_Record = D_Records.get(ClaimID).getDRecord();
                DW_PersonID DW_PersonID;
                DW_PersonID = tDW_SHBE_Handler.getClaimantDW_PersonID(D_Record);
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
                // @TODO Keep CTBRef in a collection
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
        // Council
        // HB
        d = CouncilCount0;
        // Tenancy Type
        summary.put(
                sCouncilTotalCount_TTChangeClaimant,
                Integer.toString(CouncilTotalCount_TTChangeClaimant));
        summary.put(
                sCouncilTotalCount_Minus999TTToTT1,
                Integer.toString(CouncilTotalCount_Minus999TTToTT1));
        summary.put(
                sCouncilTotalCount_TT1ToMinus999TT,
                Integer.toString(CouncilTotalCount_TT1ToMinus999TT));
        if (d > 0) {
            percentage = (CouncilTotalCount_TTChangeClaimant * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_TTChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // Postcode
        summary.put(
                sCouncilTotalCount_Postcode0ValidPostcode1Valid,
                Integer.toString(CouncilTotalCount_Postcode0ValidPostcode1Valid));
        summary.put(
                sCouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged,
                Integer.toString(CouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged));
        summary.put(
                sCouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeChange,
                Integer.toString(CouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged));
        summary.put(
                sCouncilTotalCount_Postcode0ValidPostcode1NotValid,
                Integer.toString(CouncilTotalCount_Postcode0ValidPostcode1NotValid));
        summary.put(
                sCouncilTotalCount_Postcode0NotValidPostcode1Valid,
                Integer.toString(CouncilTotalCount_Postcode0NotValidPostcode1Valid));
        summary.put(
                sCouncilTotalCount_Postcode0NotValidPostcode1NotValid,
                Integer.toString(CouncilTotalCount_Postcode0NotValidPostcode1NotValid));
        summary.put(
                sCouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged,
                Integer.toString(CouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged));
        summary.put(
                sCouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged,
                Integer.toString(CouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged));
        if (d > 0) {
            percentage = (CouncilTotalCount_Postcode0ValidPostcode1Valid * 100.0d) / d;
            summary.put(
                    sCouncilPercentagePostcode0ValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged * 100.0d) / d;
            summary.put(
                    sCouncilPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged * 100.0d) / d;
            summary.put(
                    sCouncilPercentagePostcode0ValidPostcode1ValidPostcodeChange,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CouncilTotalCount_Postcode0ValidPostcode1NotValid * 100.0d) / d;
            summary.put(
                    sCouncilPercentagePostcode0ValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CouncilTotalCount_Postcode0NotValidPostcode1Valid * 100.0d) / d;
            summary.put(
                    sCouncilPercentagePostcode0NotValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CouncilTotalCount_Postcode0NotValidPostcode1NotValid * 100.0d) / d;
            summary.put(
                    sCouncilPercentagePostcode0NotValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged * 100.0d) / d;
            summary.put(
                    sCouncilPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged * 100.0d) / d;
            summary.put(
                    sCouncilPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        summary.put(
                sCouncilTotalCount_PrivateDeregulatedTTsToTT1,
                Integer.toString(CouncilTotalCount_PrivateDeregulatedTTsToTT1));
        d = CouncilTotalCount_TTClaimant0[1];
        summary.put(
                sCouncilTotalCount_TT1ToTT4,
                Integer.toString(CouncilTotalCount_TT1ToTT4));
        summary.put(
                sCouncilTotalCount_TT1ToPrivateDeregulatedTTs,
                Integer.toString(CouncilTotalCount_TT1ToPrivateDeregulatedTTs));
        summary.put(
                sCouncilTotalCount_PostcodeChangeWithinTT1,
                Integer.toString(CouncilTotalCount_PostcodeChangeWithinTT1));
        if (d > 0) {
            percentage = (CouncilTotalCount_TT1ToPrivateDeregulatedTTs * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfTT1_TT1ToPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CouncilTotalCount_PostcodeChangeWithinTT1 * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfTT1_PostcodeChangeWithinTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (CouncilTotalCount_TT1ToTT4 * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfTT1_TT1ToTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // RSL
        // HB
        // Tenancy Type
        summary.put(
                sRSLTotalCount_TTChangeClaimant,
                Integer.toString(RSLTotalCount_TTChangeClaimant));
        summary.put(
                sRSLTotalCount_Minus999TTToTT4,
                Integer.toString(RSLTotalCount_Minus999TTToTT4));
        if (d > 0) {
            percentage = (RSLTotalCount_TTChangeClaimant * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_TTChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // Postcode
        summary.put(
                sRSLTotalCount_Postcode0ValidPostcode1Valid,
                Integer.toString(RSLTotalCount_Postcode0ValidPostcode1Valid));
        summary.put(
                sRSLTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged,
                Integer.toString(RSLTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged));
        summary.put(
                sRSLTotalCount_Postcode0ValidPostcode1ValidPostcodeChange,
                Integer.toString(RSLTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged));
        summary.put(
                sRSLTotalCount_Postcode0ValidPostcode1NotValid,
                Integer.toString(RSLTotalCount_Postcode0ValidPostcode1NotValid));
        summary.put(
                sRSLTotalCount_Postcode0NotValidPostcode1Valid,
                Integer.toString(RSLTotalCount_Postcode0NotValidPostcode1Valid));
        summary.put(
                sRSLTotalCount_Postcode0NotValidPostcode1NotValid,
                Integer.toString(RSLTotalCount_Postcode0NotValidPostcode1NotValid));
        summary.put(
                sRSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged,
                Integer.toString(RSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged));
        summary.put(
                sRSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged,
                Integer.toString(RSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged));
        if (d > 0) {
            percentage = (RSLTotalCount_Postcode0ValidPostcode1Valid * 100.0d) / d;
            summary.put(
                    sRSLPercentagePostcode0ValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (RSLTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged * 100.0d) / d;
            summary.put(
                    sRSLPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (RSLTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged * 100.0d) / d;
            summary.put(
                    sRSLPercentagePostcode0ValidPostcode1ValidPostcodeChange,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (RSLTotalCount_Postcode0ValidPostcode1NotValid * 100.0d) / d;
            summary.put(
                    sRSLPercentagePostcode0ValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (RSLTotalCount_Postcode0NotValidPostcode1Valid * 100.0d) / d;
            summary.put(
                    sRSLPercentagePostcode0NotValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (RSLTotalCount_Postcode0NotValidPostcode1NotValid * 100.0d) / d;
            summary.put(
                    sRSLPercentagePostcode0NotValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (RSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged * 100.0d) / d;
            summary.put(
                    sRSLPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (RSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged * 100.0d) / d;
            summary.put(
                    sRSLPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // Private Deregulated
        summary.put(
                sRSLTotalCount_PrivateDeregulatedTTsToTT4,
                Integer.toString(RSLTotalCount_PrivateDeregulatedTTsToTT4));
        // Social
        //d = RSLTotalCount_TTClaimant0[1] + RSLTotalCount_TTClaimant0[4];
        d = RSLTotalCount_TTClaimant0[1];
        summary.put(
                sRSLTotalCount_TT1ToTT4,
                Integer.toString(RSLTotalCount_TT1ToTT4));
        if (d > 0) {
            percentage = (RSLTotalCount_TT1ToTT4 * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfTT1_TT1ToTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLTotalCount_TTClaimant0[4];
        summary.put(
                sRSLTotalCount_TT4ToTT1,
                Integer.toString(RSLTotalCount_TT4ToTT1));
        summary.put(
                sRSLTotalCount_TT4ToPrivateDeregulatedTTs,
                Integer.toString(RSLTotalCount_TT4ToPrivateDeregulatedTTs));
        summary.put(
                sRSLTotalCount_PostcodeChangeWithinTT4,
                Integer.toString(RSLTotalCount_PostcodeChangeWithinTT4));
        if (d > 0) {
            percentage = (RSLTotalCount_TT4ToPrivateDeregulatedTTs * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfTT4_TT4ToPrivateDeregulatedTTs,
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
//        for (int TT = 0; TT < nTT; TT++) {
//            TotalCount_TTClaimant0[TT] = TotalCount_TTClaimant1[TT];
//        }
        System.arraycopy(TotalCount_TTClaimant1, 0, TotalCount_TTClaimant0, 0, nTT);

        // Set the last results
        CouncilCount0 = CouncilCount1;

        RSLCount0 = RSLCount1;

        CouncilLinkedRecordCount0 = CouncilLinkedRecordCount1;
        RSLLinkedRecordCount0 = RSLLinkedRecordCount1;
        AllUOLinkedRecordCount0 = AllUOLinkedRecordCount1;
//        for (int TT = 0; TT < nTT; TT++) {
//            CouncilTotalCount_TTClaimant0[TT] = CouncilTotalCount_TTClaimant1[TT];
//            RSLTotalCount_TTClaimant0[TT] = RSLTotalCount_TTClaimant1[TT];
//        }
        System.arraycopy(CouncilTotalCount_TTClaimant1, 0, CouncilTotalCount_TTClaimant0, 0, nTT);
        System.arraycopy(RSLTotalCount_TTClaimant1, 0, RSLTotalCount_TTClaimant0, 0, nTT);
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

    @Override
    protected void addToSummarySingleTimeCounts0(
            HashMap<String, String> summary) {
        super.addToSummarySingleTimeCounts0(summary);
        // Council
//        CouncilAllCount1 = CouncilCount1 + CouncilCTBCount1;
        summary.put(sCouncilCount1, Integer.toString(CouncilCount1));
        // RSL
//        RSLCount1 = RSLCount1 + RSLCTBCount1;
        summary.put(sRSLCount1, Integer.toString(RSLCount1));
    }

    @Override
    protected void addToSummarySingleTimeRates0(
            int nTT,
            HashMap<String, String> summary) {
        super.addToSummarySingleTimeRates0(nTT, summary);
        double ave;
        // Council
        // HouseholdSize
        summary.put(sCouncilTotalHouseholdSize,
                Long.toString(CouncilTotalHouseholdSize));
        if (CouncilCount1 > 0) {
            ave = CouncilTotalHouseholdSize / (double) CouncilCount1;
            summary.put(
                    sCouncilAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilAverageHouseholdSize,
                    s0);
        }
        // RSL
        // HouseholdSize
        summary.put(sRSLTotalHouseholdSize,
                Long.toString(RSLTotalHouseholdSize));
        if (RSLCount1 > 0) {
            ave = RSLTotalHouseholdSize / (double) RSLCount1;
            summary.put(
                    sRSLAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLAverageHouseholdSize,
                    s0);
        }
    }

    @Override
    protected void addToSummarySingleTimePSICounts(
            int nTT,
            int nPSI,
            HashMap<String, String> summary) {
        super.addToSummarySingleTimePSICounts(nTT, nPSI, summary);
        // Council
        for (int i = 1; i < nPSI; i++) {
            summary.put(sCouncilTotalCount_PSI[i],
                    Long.toString(CouncilTotalCount_PSI[i]));
            summary.put(sCouncilTotalCount_PSIByTT1[i],
                    Long.toString(CouncilTotalCount_PSIByTT1[i]));
        }
        // RSL
        for (int i = 1; i < nPSI; i++) {
            summary.put(sRSLTotalCount_PSI[i],
                    Long.toString(RSLTotalCount_PSI[i]));
            summary.put(sRSLTotalCount_PSIByTT4[i],
                    Long.toString(RSLTotalCount_PSIByTT4[i]));
        }
    }

    @Override
    protected void addToSummarySingleTimePSIRates(
            int nTT,
            int nPSI,
            HashMap<String, String> summary) {
        super.addToSummarySingleTimePSIRates(nTT, nPSI, summary);
        double ave;
        double d;
        double n;
        // Council
        for (int i = 1; i < nPSI; i++) {
            n = Integer.valueOf(summary.get(sCouncilTotalCount_PSI[i]));
            d = CouncilCount1;
            if (d > 0) {
                ave = (n * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfHB_PSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            n = Integer.valueOf(summary.get(sCouncilTotalCount_PSIByTT1[i]));
            d = CouncilCount1;
            if (d > 0) {
                ave = (n * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfHB_PSIByTT1[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            d = CouncilTotalCount_TTClaimant1[1];
            if (d > 0) {
                ave = (n * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfTT_PSIByTT1[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
        }
        // RSL
        for (int i = 1; i < nPSI; i++) {
            n = Integer.valueOf(summary.get(sRSLTotalCount_PSI[i]));
            d = RSLCount1;
            if (d > 0) {
                ave = (n * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfHB_PSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            n = Integer.valueOf(summary.get(sRSLTotalCount_PSIByTT4[i]));
            d = RSLCount1;
            if (d > 0) {
                ave = (n * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfHB_PSIByTT4[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            d = RSLTotalCount_TTClaimant1[4];
            if (d > 0) {
                ave = (n * 100.0d) / d;
                summary.put(
                        sRSLPercentageOfTT_PSIByTT4[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
        }
    }

    @Override
    protected void addToSummarySingleTimeDisabilityCounts(
            int nTT,
            HashMap<String, String> summary) {
        super.addToSummarySingleTimeDisabilityCounts(nTT, summary);
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
        // DisabilityPremiumAwardSocialTTs
        t = CouncilTotalCount_DisabilityPremiumAwardByTT[1] + CouncilTotalCount_DisabilityPremiumAwardByTT[4];
        summary.put(
                sCouncilTotalCount_DisabilityPremiumAwardSocialTTs,
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
        // SevereDisabilityPremiumAwardSocialTTs
        t = CouncilTotalCount_SevereDisabilityPremiumAwardByTT[1] + CouncilTotalCount_SevereDisabilityPremiumAwardByTT[4];
        summary.put(
                sCouncilTotalCount_SevereDisabilityPremiumAwardSocialTTs,
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
        // DisabledChildPremiumAwardSocialTTs
        t = CouncilTotalCount_DisabledChildPremiumAwardByTT[1] + CouncilTotalCount_DisabledChildPremiumAwardByTT[4];
        summary.put(
                sCouncilTotalCount_DisabledChildPremiumAwardSocialTTs,
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
        // EnhancedDisabilityPremiumAwardSocialTTs
        t = CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[1] + CouncilTotalCount_EnhancedDisabilityPremiumAwardByTT[4];
        summary.put(
                sCouncilTotalCount_EnhancedDisabilityPremiumAwardSocialTTs,
                Integer.toString(t));
        // ByTT
        int i = 1;
        summary.put(
                sCouncilTotalCount_DisabilityAwardByTT1,
                Integer.toString(TotalCount_DisabilityAwardByTT[i]));
        summary.put(
                sCouncilTotalCount_DisabilityPremiumAwardByTT1,
                Integer.toString(TotalCount_DisabilityPremiumAwardByTT[i]));
        summary.put(
                sCouncilTotalCount_SevereDisabilityPremiumAwardByTT1,
                Integer.toString(TotalCount_SevereDisabilityPremiumAwardByTT[i]));
        summary.put(
                sCouncilTotalCount_DisabledChildPremiumAwardByTT1,
                Integer.toString(TotalCount_DisabledChildPremiumAwardByTT[i]));
        summary.put(
                sCouncilTotalCount_EnhancedDisabilityPremiumAwardByTT1,
                Integer.toString(TotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
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
        // DisabilityAwardSocialTTs
        t = RSLTotalCount_DisabilityAwardByTT[1] + RSLTotalCount_DisabilityAwardByTT[4];
        summary.put(
                sRSLTotalCount_DisabilityAwardSocialTTs,
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
        // ByTT
        i = 4;
        summary.put(
                sRSLTotalCount_DisabilityAwardByTT4,
                Integer.toString(TotalCount_DisabilityAwardByTT[i]));
        summary.put(
                sRSLTotalCount_DisabilityPremiumAwardByTT4,
                Integer.toString(TotalCount_DisabilityPremiumAwardByTT[i]));
        summary.put(
                sRSLTotalCount_SevereDisabilityPremiumAwardByTT4,
                Integer.toString(TotalCount_SevereDisabilityPremiumAwardByTT[i]));
        summary.put(
                sRSLTotalCount_DisabledChildPremiumAwardByTT4,
                Integer.toString(TotalCount_DisabledChildPremiumAwardByTT[i]));
        summary.put(
                sRSLTotalCount_EnhancedDisabilityPremiumAwardByTT4,
                Integer.toString(TotalCount_EnhancedDisabilityPremiumAwardByTT[i]));
    }

    @Override
    protected void addToSummarySingleTimeDisabilityRates(
            int nTT,
            HashMap<String, String> summary) {
        super.addToSummarySingleTimeDisabilityRates(nTT, summary);
        double percentage;
        double d;
        double n;
        // Council
        // DisabilityAward
        n = Double.valueOf(summary.get(sCouncilTotalCount_DisabilityAwardHBTTs));
        d = CouncilCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabilityAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityAwardSocialTTs
        n = Double.valueOf(summary.get(sCouncilTotalCount_DisabilityAwardSocialTTs));
        d = CouncilCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabilityAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilTotalCount_TTClaimant0[1] + CouncilTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfSocialTTs_DisabilityAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAwardHBTTs
        n = Double.valueOf(summary.get(sCouncilTotalCount_DisabilityPremiumAwardHBTTs));
        d = CouncilCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAwardSocialTTs
        n = Double.valueOf(summary.get(sCouncilTotalCount_DisabilityPremiumAwardSocialTTs));
        d = CouncilCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilTotalCount_TTClaimant0[1] + CouncilTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereSevereDisabilityPremiumAwardHBTTs
        n = Double.valueOf(summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardHBTTs));
        d = CouncilCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_SevereDisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabilityPremiumAwardSocialTTs
        n = Double.valueOf(summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardSocialTTs));
        d = CouncilCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilTotalCount_TTClaimant0[1] + CouncilTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAward
        n = Double.valueOf(summary.get(sCouncilTotalCount_DisabledChildPremiumAward));
        d = CouncilCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabledChildPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAwardHBTTs
        n = Integer.valueOf(summary.get(sCouncilTotalCount_DisabledChildPremiumAwardHBTTs));
        d = CouncilCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabledChildPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAwardSocialTTs
        n = Double.valueOf(summary.get(sCouncilTotalCount_DisabledChildPremiumAwardSocialTTs));
        d = CouncilCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabledChildPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilTotalCount_TTClaimant0[1] + CouncilTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAwardHBTTs
        n = Double.valueOf(summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardHBTTs));
        d = CouncilCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAwardSocialTTs
        n = Double.valueOf(summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardSocialTTs));
        d = CouncilCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CouncilTotalCount_TTClaimant0[1] + CouncilTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // ByTT
        int i = 1;
        // CouncilCount1;
        d = CouncilCount1;
        n = Double.valueOf(summary.get(sCouncilTotalCount_DisabilityAwardByTT1));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabilityAwardByTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        n = Double.valueOf(summary.get(sCouncilTotalCount_DisabilityPremiumAwardByTT1));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabilityPremiumAwardByTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        n = Double.valueOf(summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardByTT1));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_SevereDisabilityPremiumAwardByTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        n = Double.valueOf(summary.get(sCouncilTotalCount_DisabledChildPremiumAwardByTT1));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_DisabledChildPremiumAwardByTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        n = Double.valueOf(summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardByTT1));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_EnhancedDisabilityPremiumAwardByTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // TT
        d = CouncilTotalCount_TTClaimant1[i];
        n = Double.valueOf(summary.get(sCouncilTotalCount_DisabilityAwardByTT1));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfTT_DisabilityAwardByTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        n = Double.valueOf(summary.get(sCouncilTotalCount_DisabilityPremiumAwardByTT1));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfTT_DisabilityPremiumAwardByTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        n = Double.valueOf(summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardByTT1));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfTT_SevereDisabilityPremiumAwardByTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        n = Double.valueOf(summary.get(sCouncilTotalCount_DisabledChildPremiumAwardByTT1));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfTT_DisabledChildPremiumAwardByTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        n = Double.valueOf(summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardByTT1));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfTT_EnhancedDisabilityPremiumAwardByTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // RSL
        // DisabilityAwardHBTTs
        n = Double.valueOf(summary.get(sRSLTotalCount_DisabilityAwardHBTTs));
        d = RSLCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabilityAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityAwardSocialTTs
        n = Double.valueOf(summary.get(sRSLTotalCount_DisabilityAwardSocialTTs));
        d = RSLCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabilityAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLTotalCount_TTClaimant0[1] + RSLTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfSocialTTs_DisabilityAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAwardHBTTs
        d = RSLCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAwardSocialTTs
        d = RSLCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLTotalCount_TTClaimant0[1] + RSLTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabilityPremiumAward
        d = RSLCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_SevereDisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereDisabilityPremiumAwardSocialTTs
        n = Double.valueOf(summary.get(sRSLTotalCount_SevereDisabilityPremiumAwardSocialTTs));
        d = RSLCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLTotalCount_TTClaimant0[1] + RSLTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAward
        n = Double.valueOf(summary.get(sRSLTotalCount_DisabledChildPremiumAward));
        d = RSLCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabledChildPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAwardSocialTTs
        n = Double.valueOf(summary.get(sRSLTotalCount_DisabledChildPremiumAwardSocialTTs));
        d = RSLCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabledChildPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLTotalCount_TTClaimant0[1] + RSLTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAwardHBTTs
        n = Double.valueOf(summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardHBTTs));
        d = RSLCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAwardSocialTTs
        n = Double.valueOf(summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardSocialTTs));
        d = RSLCount1;
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = RSLTotalCount_TTClaimant0[1] + RSLTotalCount_TTClaimant0[4];
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // ByTT
        i = 4;
        d = RSLCount1;
        n = Double.valueOf(summary.get(sRSLTotalCount_DisabilityAwardByTT4));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabilityAwardByTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        n = Double.valueOf(summary.get(sRSLTotalCount_DisabilityPremiumAwardByTT4));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabilityPremiumAwardByTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        n = Double.valueOf(summary.get(sRSLTotalCount_SevereDisabilityPremiumAwardByTT4));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_SevereDisabilityPremiumAwardByTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        n = Double.valueOf(summary.get(sRSLTotalCount_DisabledChildPremiumAwardByTT4));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_DisabledChildPremiumAwardByTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        n = Integer.valueOf(summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardByTT4));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_EnhancedDisabilityPremiumAwardByTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // TT
        d = RSLTotalCount_TTClaimant1[i];
        n = Double.valueOf(summary.get(sRSLTotalCount_DisabilityAwardByTT4));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfTT_DisabilityAwardByTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        n = Double.valueOf(summary.get(sRSLTotalCount_DisabilityPremiumAwardByTT4));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfTT_DisabilityPremiumAwardByTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        n = Double.valueOf(summary.get(sRSLTotalCount_SevereDisabilityPremiumAwardByTT4));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfTT_SevereDisabilityPremiumAwardByTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        n = Double.valueOf(summary.get(sRSLTotalCount_DisabledChildPremiumAwardByTT4));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfTT_DisabledChildPremiumAwardByTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        n = Integer.valueOf(summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardByTT4));
        if (d > 0) {
            percentage = (n * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfTT_EnhancedDisabilityPremiumAwardByTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
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
                BigDecimal.valueOf(CouncilTotalWeeklyHBEntitlement).toPlainString());
        summary.put(
                sCouncilTotalCount_WeeklyHBEntitlementNonZero,
                Integer.toString(CouncilTotalCount_WeeklyHBEntitlementNonZero));
        summary.put(
                sCouncilTotalCount_WeeklyHBEntitlementZero,
                Integer.toString(CouncilTotalCount_WeeklyHBEntitlementZero));
        // WeeklyCTBEntitlement
        summary.put(
                sCouncilTotalWeeklyCTBEntitlement,
                BigDecimal.valueOf(CouncilTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(
                sCouncilTotalCount_WeeklyCTBEntitlementNonZero,
                Integer.toString(CouncilTotalCount_WeeklyCTBEntitlementNonZero));
        summary.put(
                sCouncilTotalCount_WeeklyCTBEntitlementZero,
                Integer.toString(CouncilTotalWeeklyCTBEntitlementZeroCount));
        // WeeklyEligibleRentAmount
        summary.put(
                sCouncilTotalWeeklyEligibleRentAmount,
                BigDecimal.valueOf(CouncilTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                sCouncilTotalCount_WeeklyEligibleRentAmountNonZero,
                Integer.toString(CouncilTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(
                sCouncilTotalCount_WeeklyEligibleRentAmountZero,
                Integer.toString(CouncilTotalWeeklyEligibleRentAmountZeroCount));
        summary.put(
                sCouncilTotalWeeklyEligibleRentAmountTT1,
                BigDecimal.valueOf(CouncilTotalWeeklyEligibleRentAmountTT1).toPlainString());
        summary.put(
                sCouncilTotalCount_WeeklyEligibleRentAmountNonZeroTT1,
                Integer.toString(CouncilTotalWeeklyEligibleRentAmountNonZeroCountTT1));
        summary.put(
                sCouncilTotalCount_WeeklyEligibleRentAmountZeroTT1,
                Integer.toString(CouncilTotalWeeklyEligibleRentAmountZeroCountTT1));
        // WeeklyEligibleCouncilTaxAmount
        summary.put(
                sCouncilTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(CouncilTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(
                sCouncilTotalCount_WeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(CouncilTotalCount_WeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(
                sCouncilTotalCount_WeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(CouncilTotalCount_WeeklyEligibleCouncilTaxAmountZero));
        // ContractualRentAmount
        summary.put(
                sCouncilTotalContractualRentAmount,
                BigDecimal.valueOf(CouncilTotalContractualRentAmount).toPlainString());
        summary.put(
                sCouncilTotalCountContractualRentAmountNonZeroCount,
                Integer.toString(CouncilTotalContractualRentAmountNonZeroCount));
        summary.put(
                sCouncilTotalCountContractualRentAmountZeroCount,
                Integer.toString(CouncilTotalContractualRentAmountZeroCount));
        // WeeklyAdditionalDiscretionaryPayment
        summary.put(
                sCouncilTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(CouncilTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sCouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(CouncilTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sCouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(CouncilTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        summary.put(
                sCouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sCouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sCouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        // Employed
        summary.put(
                sCouncilTotalCount_ClaimantsEmployed,
                Integer.toString(CouncilTotalCount_EmployedClaimants));
        // Self Employed
        summary.put(
                sCouncilTotalCountClaimantsSelfEmployed,
                Integer.toString(CouncilTotalCount_SelfEmployedClaimants));
        // Students
        summary.put(
                sCouncilTotalCountClaimantsStudents,
                Integer.toString(CouncilTotalCount_StudentsClaimants));
        // LHACases
        summary.put(
                sCouncilTotalCount_LHACases,
                Integer.toString(CouncilTotalCount_LHACases));
        // RSL
        // WeeklyHBEntitlement
        summary.put(
                sRSLTotalWeeklyHBEntitlement,
                BigDecimal.valueOf(RSLTotalWeeklyHBEntitlement).toPlainString());
        summary.put(
                sRSLTotalCount_WeeklyHBEntitlementNonZero,
                Integer.toString(RSLTotalCount_WeeklyHBEntitlementNonZero));
        summary.put(
                sRSLTotalCount_WeeklyHBEntitlementZero,
                Integer.toString(RSLTotalCount_WeeklyHBEntitlementZero));
        // WeeklyCTBEntitlement
        summary.put(
                sRSLTotalWeeklyCTBEntitlement,
                BigDecimal.valueOf(RSLTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(
                sRSLTotalCount_WeeklyCTBEntitlementNonZero,
                Integer.toString(RSLTotalCount_WeeklyCTBEntitlementNonZero));
        summary.put(
                sRSLTotalCount_WeeklyCTBEntitlementZero,
                Integer.toString(RSLTotalWeeklyCTBEntitlementZeroCount));
        // WeeklyEligibleRentAmount
        summary.put(
                sRSLTotalWeeklyEligibleRentAmount,
                BigDecimal.valueOf(RSLTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                sRSLTotalCount_WeeklyEligibleRentAmountNonZero,
                Integer.toString(RSLTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(
                sRSLTotalCount_WeeklyEligibleRentAmountZero,
                Integer.toString(RSLTotalWeeklyEligibleRentAmountZeroCount));
        // WeeklyEligibleCouncilTaxAmount
        summary.put(
                sRSLTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(RSLTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(
                sRSLTotalCount_WeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(RSLTotalCount_WeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(
                sRSLTotalCount_WeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(RSLTotalCount_WeeklyEligibleCouncilTaxAmountZero));
        // ContractualRentAmount
        summary.put(
                sRSLTotalContractualRentAmount,
                BigDecimal.valueOf(RSLTotalContractualRentAmount).toPlainString());
        summary.put(
                sRSLTotalCountContractualRentAmountNonZeroCount,
                Integer.toString(RSLTotalContractualRentAmountNonZeroCount));
        summary.put(
                sRSLTotalCountContractualRentAmountZeroCount,
                Integer.toString(RSLTotalContractualRentAmountZeroCount));
        // WeeklyAdditionalDiscretionaryPayment
        summary.put(
                sRSLTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(RSLTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sRSLTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(RSLTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sRSLTotalCount_WeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(RSLTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        summary.put(
                sRSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sRSLTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sRSLTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        // Employed
        summary.put(
                sRSLTotalCount_ClaimantsEmployed,
                Integer.toString(RSLTotalCount_EmployedClaimants));
        // Self Employed
        summary.put(
                sRSLTotalCountClaimantsSelfEmployed,
                Integer.toString(RSLTotalCount_SelfEmployedClaimants));
        // Students
        summary.put(
                sRSLTotalCountClaimantsStudents,
                Integer.toString(RSLTotalCount_StudentsClaimants));
        // LHACases
        summary.put(
                sRSLTotalCount_LHACases,
                Integer.toString(HBTotalCount_LHACases));
    }

    @Override
    protected void addToSummarySingleTimeRates1(
            HashMap<String, String> summary) {
        super.addToSummarySingleTimeRates1(summary);
        double ave;
        double d;
        double t;
        // Council
        // WeeklyHBEntitlement
        t = Double.valueOf(summary.get(sCouncilTotalWeeklyHBEntitlement));
        d = CouncilTotalCount_WeeklyHBEntitlementNonZero;
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
        // WeeklyCTBEntitlement
        t = Double.valueOf(summary.get(sCouncilTotalWeeklyCTBEntitlement));
        d = CouncilTotalCount_WeeklyCTBEntitlementNonZero;
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
        // WeeklyEligibleRentAmount
        t = Double.valueOf(summary.get(sCouncilTotalWeeklyEligibleRentAmount));
        d = CouncilTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilAverageWeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilAverageWeeklyEligibleRentAmount,
                    s0);
        }
        // WeeklyEligibleCouncilTaxAmount
        t = Double.valueOf(summary.get(sCouncilTotalWeeklyEligibleCouncilTaxAmount));
        d = CouncilTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilAverageWeeklyEligibleCouncilTaxAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilAverageWeeklyEligibleCouncilTaxAmount,
                    s0);
        }
        // ContractualRentAmount
        t = Double.valueOf(summary.get(sCouncilTotalContractualRentAmount));
        d = CouncilTotalContractualRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilAverageContractualRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilAverageContractualRentAmount,
                    s0);
        }
        // WeeklyAdditionalDiscretionaryPayment
        t = Double.valueOf(summary.get(sCouncilTotalWeeklyAdditionalDiscretionaryPayment));
        d = CouncilTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilAverageWeeklyAdditionalDiscretionaryPayment,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilAverageWeeklyAdditionalDiscretionaryPayment,
                    s0);
        }
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        t = Double.valueOf(summary.get(sCouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability));
        d = CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sCouncilAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    s0);
        }
        // Employed
        t = Integer.valueOf(summary.get(sCouncilTotalCount_ClaimantsEmployed));
        d = CouncilCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_ClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilPercentageOfHB_ClaimantsEmployed,
                    s0);
        }
        // Self Employed
        t = Integer.valueOf(summary.get(sCouncilTotalCountClaimantsSelfEmployed));
        d = CouncilCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_ClaimantsSelfEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilPercentageOfHB_ClaimantsSelfEmployed,
                    s0);
        }
        // Students
        t = Integer.valueOf(summary.get(sCouncilTotalCountClaimantsStudents));
        d = CouncilCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_ClaimantsStudents,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilPercentageOfHB_ClaimantsStudents,
                    s0);
        }
        // LHACases
        t = Integer.valueOf(summary.get(sCouncilTotalCount_LHACases));
        d = CouncilCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sCouncilPercentageOfHB_LHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCouncilPercentageOfHB_LHACases,
                    s0);
        }
        // RSL
        // WeeklyHBEntitlement
        t = Double.valueOf(summary.get(sRSLTotalWeeklyHBEntitlement));
        d = RSLTotalCount_WeeklyHBEntitlementNonZero;
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
        // WeeklyCTBEntitlement
        t = Double.valueOf(summary.get(sRSLTotalWeeklyCTBEntitlement));
        d = RSLTotalCount_WeeklyCTBEntitlementNonZero;
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
        // WeeklyEligibleRentAmount
        t = Double.valueOf(summary.get(sRSLTotalWeeklyEligibleRentAmount));
        d = RSLTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLAverageWeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLAverageWeeklyEligibleRentAmount,
                    s0);
        }
        // WeeklyEligibleCouncilTaxAmount
        t = Double.valueOf(summary.get(sRSLTotalWeeklyEligibleCouncilTaxAmount));
        d = RSLTotalCount_WeeklyEligibleCouncilTaxAmountNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLAverageWeeklyEligibleCouncilTaxAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLAverageWeeklyEligibleCouncilTaxAmount,
                    s0);
        }
        // ContractualRentAmount
        t = Double.valueOf(summary.get(sRSLTotalContractualRentAmount));
        d = RSLTotalContractualRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLAverageContractualRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLAverageContractualRentAmount,
                    s0);
        }
        // WeeklyAdditionalDiscretionaryPayment
        t = Double.valueOf(summary.get(sRSLTotalWeeklyAdditionalDiscretionaryPayment));
        d = RSLTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLAverageWeeklyAdditionalDiscretionaryPayment,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLAverageWeeklyAdditionalDiscretionaryPayment,
                    s0);
        }
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        t = Double.valueOf(summary.get(sRSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability));
        d = RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(
                    sRSLAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    s0);
        }
        // Employed
        t = Integer.valueOf(summary.get(sRSLTotalCount_ClaimantsEmployed));
        d = RSLCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_ClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLPercentageOfHB_ClaimantsEmployed,
                    s0);
        }
        // Self Employed
        t = Integer.valueOf(summary.get(sRSLTotalCountClaimantsSelfEmployed));
        d = RSLCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_ClaimantsSelfEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLPercentageOfHB_ClaimantsSelfEmployed,
                    s0);
        }
        // Students
        t = Integer.valueOf(summary.get(sRSLTotalCountClaimantsStudents));
        d = RSLCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_ClaimantsStudents,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLPercentageOfHB_ClaimantsStudents,
                    s0);
        }
        // LHACases
        t = Integer.valueOf(summary.get(sRSLTotalCount_LHACases));
        d = RSLCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sRSLPercentageOfHB_LHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sRSLPercentageOfHB_LHACases,
                    s0);
        }
    }

    @Override
    protected void addToSummarySingleTimeEthnicityCounts(
            int nEG,
            int nTT,
            HashMap<String, String> summary) {
        super.addToSummarySingleTimeEthnicityCounts(nEG, nTT, summary);
        // Council
        for (int i = 1; i < nEG; i++) {
            summary.put(
                    sCouncilTotalCount_EthnicGroupClaimant[i],
                    Integer.toString(CouncilEthnicGroupCount[i]));
        }
        // RSL
        for (int i = 1; i < nEG; i++) {
            summary.put(sRSLTotalCount_EthnicGroupClaimant[i],
                    Integer.toString(RSLEthnicGroupCount[i]));
        }
    }

    @Override
    protected void addToSummarySingleTimeEthnicityRates(
            int nEG,
            int nTT,
            HashMap<String, String> summary) {
        super.addToSummarySingleTimeEthnicityRates(nEG, nTT, summary);
        double percentage;
        double all;
        double d;
        // Council
        for (int i = 1; i < nEG; i++) {
            all = Integer.valueOf(summary.get(sCouncilTotalCount_EthnicGroupClaimant[i]));
            d = CouncilCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(
                        sCouncilPercentageOfHB_EthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
        }
        // RSL
        for (int i = 1; i < nEG; i++) {
            all = Integer.valueOf(summary.get(sRSLTotalCount_EthnicGroupClaimant[i]));
            d = RSLCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(sRSLPercentageOfHB_EthnicGroupClaimant[i],
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
        super.addToSummarySingleTimeTTCounts(nTT, summary);
        int all;
        // Council
        // TT
        for (int i = 1; i < nTT; i++) {
            all = CouncilTotalCount_TTClaimant1[i];
            summary.put(
                    sCouncilTotalCount_ClaimantTT[i],
                    Integer.toString(all));
        }
        summary.put(sCouncilCount1, Integer.toString(CouncilCount1));
        summary.put(sCouncilTotalCount_PostcodeValidFormat,
                Integer.toString(CouncilTotalCount_PostcodeValidFormat));
        summary.put(sCouncilTotalCount_PostcodeValid,
                Integer.toString(CouncilTotalCount_PostcodeValid));
        // RSL
        // TT
        for (int i = 1; i < nTT; i++) {
            all = RSLTotalCount_TTClaimant1[i];
            summary.put(
                    sRSLTotalCount_ClaimantTT[i],
                    Integer.toString(all));
        }
        summary.put(sRSLCount1, Integer.toString(RSLCount1));
        summary.put(sRSLTotalCount_PostcodeValidFormat,
                Integer.toString(RSLTotalCount_PostcodeValidFormat));
        summary.put(sRSLTotalCount_PostcodeValid,
                Integer.toString(RSLTotalCount_PostcodeValid));
    }

    @Override
    protected void addToSummarySingleTimeTTRates(
            int nTT,
            HashMap<String, String> summary) {
        super.addToSummarySingleTimeTTRates(nTT, summary);
        double percentage;
        double d;
        int all;
        // Council
        // TT
        all = Integer.valueOf(summary.get(sCouncilTotalCount_ClaimantTT[1]));
        d = CouncilLinkedRecordCount1;
        if (d > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(sCouncilPercentageOfLinkedRecords_ClaimantTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // RSL
        // TT
        all = Integer.valueOf(summary.get(sRSLTotalCount_ClaimantTT[4]));
        d = RSLLinkedRecordCount1;
        if (d > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(sRSLPercentageOfLinkedRecords_ClaimantTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
    }

    protected void doCouncilCompare2TimesCounts(
            boolean addToSingelTimeMetrics,
            DW_SHBE_D_Record D_Record0,
            DW_SHBE_D_Record D_Record1,
            String yM30v,
            String yM31v) {
        //super.doCompare2TimesCounts(D_Record0, D_Record1, yM30v, yM31v);
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();
        boolean isHBClaim;
        isHBClaim = false;
        if (D_Record1 != null && addToSingelTimeMetrics) {
            doCouncilSingleTimeCount(D_Record1, yM30v);
        }
        if (D_Record1 != null) {
            isHBClaim = tDW_SHBE_Handler.isHBClaim(D_Record1);
        }
//        CouncilAllCount1 = CouncilCount1 + CouncilCTBCount1;
//        RSLCount1 = RSLCount1 + RSLCTBCount1;
        String postcode0;
        postcode0 = null;
        int TT0;
        TT0 = tDW_SHBE_TenancyType_Handler.iMinus999;
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
        TT1 = tDW_SHBE_TenancyType_Handler.iMinus999;
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
            doCouncilCompare2TimesHBCount(
                    TT0,
                    postcode0,
                    isValidPostcode0,
                    TT1,
                    postcode1,
                    isValidPostcode1);
        }
    }

    protected void doRSLCompare2TimesCounts(
            boolean addToSingleTimeMetrics,
            DW_SHBE_D_Record D_Record0,
            DW_SHBE_D_Record D_Record1,
            String yM30v,
            String yM31v) {
        //super.doCompare2TimesCounts(D_Record0, D_Record1, yM30v, yM31v);
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();
        boolean isHBClaim;
        isHBClaim = false;
        if (D_Record1 != null && addToSingleTimeMetrics) {
            doRSLSingleTimeCount(D_Record1, yM30v);
        }
        if (D_Record1 != null) {
            isHBClaim = tDW_SHBE_Handler.isHBClaim(D_Record1);
        }
//        CouncilAllCount1 = CouncilCount1 + CouncilCTBCount1;
//        RSLCount1 = RSLCount1 + RSLCTBCount1;
        String postcode0;
        postcode0 = null;
        int TT0;
        TT0 = tDW_SHBE_TenancyType_Handler.iMinus999;
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
        TT1 = tDW_SHBE_TenancyType_Handler.iMinus999;
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
            doRSLCompare2TimesHBCount(
                    TT0,
                    postcode0,
                    isValidPostcode0,
                    TT1,
                    postcode1,
                    isValidPostcode1);
        }
    }

    protected void doCouncilSingleTimeCount(
            DW_SHBE_D_Record D_Record,
            String yM30v) {

        super.doSingleTimeCount(D_Record, yM30v);

        int ClaimantsEthnicGroup0;
        int TT;
        String postcode;
        int DisabilityPremiumAwarded;
        int SevereDisabilityPremiumAwarded;
        int DisabledChildPremiumAwarded;
        int EnhancedDisabilityPremiumAwarded;
        int PSI;
        long HouseholdSize;
        int WeeklyHousingBenefitEntitlement;
        int WeeklyCouncilTaxBenefitBenefitEntitlement;
        int WeeklyEligibleRentAmount;
        int WeeklyEligibleCouncilTaxAmount;
        int ContractualRentAmount;
        int WeeklyAdditionalDiscretionaryPayment;
        int WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
        int ClaimantsNetWeeklyIncomeFromEmployment;
        int ClaimantsNetWeeklyIncomeFromSelfEmployment;
        String ClaimantsStudentIndicator;
        String LHARegulationsApplied;
        //ClaimantsEthnicGroup0 = D_Record.getClaimantsEthnicGroup();
        ClaimantsEthnicGroup0 = tDW_SHBE_Handler.getEthnicityGroup(D_Record);
        // All unfiltered counts
        TT = D_Record.getTenancyType();
        postcode = D_Record.getClaimantsPostcode();
        CouncilTotalCount_TTClaimant1[TT]++;
        // Disability
        DisabilityPremiumAwarded = D_Record.getDisabilityPremiumAwarded();
        if (DisabilityPremiumAwarded == 1) {
            CouncilTotalCount_DisabilityPremiumAwardByTT[TT]++;
        }
        SevereDisabilityPremiumAwarded = D_Record.getSevereDisabilityPremiumAwarded();
        if (SevereDisabilityPremiumAwarded == 1) {
            CouncilTotalCount_SevereDisabilityPremiumAwardByTT[TT]++;
        }
        DisabledChildPremiumAwarded = D_Record.getDisabledChildPremiumAwarded();
        if (DisabledChildPremiumAwarded == 1) {
            CouncilTotalCount_DisabledChildPremiumAwardByTT[TT]++;
        }
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
        PSI = D_Record.getPassportedStandardIndicator();
        // Household size
        HouseholdSize = tDW_SHBE_Handler.getHouseholdSize(D_Record);
        // Entitlements
        WeeklyHousingBenefitEntitlement = D_Record.getWeeklyHousingBenefitEntitlement();
//        WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
//        // Eligible Amounts
//        WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
//        WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
//        ContractualRentAmount = D_Record.getContractualRentAmount();
//        // Additional Payments
//        WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
//        WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
        // HBClaim only counts
        if (tDW_SHBE_Handler.isHBClaim(D_Record)) {
            CouncilTotalCount_PSI[PSI]++;
            CouncilTotalCount_PSIByTT1[PSI]++;
            //if (HBRef.equalsIgnoreCase(CTBRef)) {
            CouncilTotalHouseholdSize += HouseholdSize;
            ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
            if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
                CouncilTotalCount_EmployedClaimants++;
            }
            ClaimantsNetWeeklyIncomeFromSelfEmployment = D_Record.getClaimantsNetWeeklyIncomeFromSelfEmployment();
            if (ClaimantsNetWeeklyIncomeFromSelfEmployment > 0) {
                CouncilTotalCount_SelfEmployedClaimants++;
            }
            ClaimantsStudentIndicator = D_Record.getClaimantsStudentIndicator();
            if (ClaimantsStudentIndicator != null) {
                if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                    CouncilTotalCount_StudentsClaimants++;
                }
            }
            LHARegulationsApplied = D_Record.getLHARegulationsApplied();
            if (LHARegulationsApplied != null) {
                if (LHARegulationsApplied.equalsIgnoreCase("1")
                        || LHARegulationsApplied.equalsIgnoreCase("Yes")) {
                    CouncilTotalCount_LHACases++;
                }
            }
            if (WeeklyHousingBenefitEntitlement > 0) {
                CouncilTotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
                CouncilTotalCount_WeeklyHBEntitlementNonZero++;
            } else {
                CouncilTotalCount_WeeklyHBEntitlementZero++;
            }
            WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
            if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
                CouncilTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
                CouncilTotalCount_WeeklyCTBEntitlementNonZero++;
            } else {
                CouncilTotalWeeklyCTBEntitlementZeroCount++;
            }
            WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
            if (WeeklyEligibleRentAmount > 0) {
                CouncilTotalWeeklyEligibleRentAmount += WeeklyEligibleRentAmount;
                CouncilTotalWeeklyEligibleRentAmountNonZeroCount++;
            } else {
                CouncilTotalWeeklyEligibleRentAmountZeroCount++;
            }
            if (TT == 1) {
                if (WeeklyEligibleRentAmount > 0) {
                    CouncilTotalWeeklyEligibleRentAmountTT1 += WeeklyEligibleRentAmount;
                    CouncilTotalWeeklyEligibleRentAmountNonZeroCountTT1++;
                } else {
                    CouncilTotalWeeklyEligibleRentAmountZeroCountTT1++;
                }
            }
            WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
            if (WeeklyEligibleCouncilTaxAmount > 0) {
                CouncilTotalWeeklyEligibleCouncilTaxAmount += WeeklyEligibleCouncilTaxAmount;
                CouncilTotalCount_WeeklyEligibleCouncilTaxAmountNonZero++;
            } else {
                CouncilTotalCount_WeeklyEligibleCouncilTaxAmountZero++;
            }
            ContractualRentAmount = D_Record.getContractualRentAmount();
            if (ContractualRentAmount > 0) {
                CouncilTotalContractualRentAmount += ContractualRentAmount;
                CouncilTotalContractualRentAmountNonZeroCount++;
            } else {
                CouncilTotalContractualRentAmountZeroCount++;
            }
            WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
            if (WeeklyAdditionalDiscretionaryPayment > 0) {
                CouncilTotalWeeklyAdditionalDiscretionaryPayment += WeeklyAdditionalDiscretionaryPayment;
                CouncilTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount++;
            } else {
                CouncilTotalWeeklyAdditionalDiscretionaryPaymentZeroCount++;
            }
            WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
            if (WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability > 0) {
                CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability += WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
                CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount++;
            } else {
                CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount++;
            }
            doCouncilSingleTimeHBCount(
                    ClaimantsEthnicGroup0,
                    TT,
                    postcode,
                    yM30v);
        }
    }

    protected void doRSLSingleTimeCount(
            DW_SHBE_D_Record D_Record,
            String yM30v) {
        super.doSingleTimeCount(D_Record, yM30v);
        int ClaimantsEthnicGroup0;
        int TT;
        String postcode;
        int DisabilityPremiumAwarded;
        int SevereDisabilityPremiumAwarded;
        int DisabledChildPremiumAwarded;
        int EnhancedDisabilityPremiumAwarded;
        int PSI;
        long HouseholdSize;
        int WeeklyHousingBenefitEntitlement;
        int WeeklyCouncilTaxBenefitBenefitEntitlement;
        int WeeklyEligibleRentAmount;
        int WeeklyEligibleCouncilTaxAmount;
        int ContractualRentAmount;
        int WeeklyAdditionalDiscretionaryPayment;
        int WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
        int ClaimantsNetWeeklyIncomeFromEmployment;
        int ClaimantsNetWeeklyIncomeFromSelfEmployment;
        String ClaimantsStudentIndicator;
        String LHARegulationsApplied;
        TT = D_Record.getTenancyType();
        //ClaimantsEthnicGroup0 = D_Record.getClaimantsEthnicGroup();
        ClaimantsEthnicGroup0 = tDW_SHBE_Handler.getEthnicityGroup(D_Record);
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
        // Household size
        HouseholdSize = tDW_SHBE_Handler.getHouseholdSize(D_Record);
        // Entitlements
        WeeklyHousingBenefitEntitlement = D_Record.getWeeklyHousingBenefitEntitlement();
//        WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
//        // Eligible Amounts
//        WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
//        WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
//        ContractualRentAmount = D_Record.getContractualRentAmount();
//        // Additional Payments
//        WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
//        WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
        // HBClaim only counts
        if (tDW_SHBE_Handler.isHBClaim(D_Record)) {
            RSLTotalCount_PSI[PSI]++;
            RSLTotalCount_PSIByTT4[PSI]++;
            //if (HBRef.equalsIgnoreCase(CTBRef)) {
            RSLTotalHouseholdSize += HouseholdSize;
            ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
            if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
                RSLTotalCount_EmployedClaimants++;
            }
            ClaimantsNetWeeklyIncomeFromSelfEmployment = D_Record.getClaimantsNetWeeklyIncomeFromSelfEmployment();
            if (ClaimantsNetWeeklyIncomeFromSelfEmployment > 0) {
                RSLTotalCount_SelfEmployedClaimants++;
            }
            ClaimantsStudentIndicator = D_Record.getClaimantsStudentIndicator();
            if (ClaimantsStudentIndicator != null) {
                if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                    RSLTotalCount_StudentsClaimants++;
                }
            }
            LHARegulationsApplied = D_Record.getLHARegulationsApplied();
            if (LHARegulationsApplied != null) {
                if (LHARegulationsApplied.equalsIgnoreCase("1")
                        || LHARegulationsApplied.equalsIgnoreCase("Yes")) {
                    RSLTotalCount_LHACases++;
                }
            }
            if (WeeklyHousingBenefitEntitlement > 0) {
                RSLTotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
                RSLTotalCount_WeeklyHBEntitlementNonZero++;
            } else {
                RSLTotalCount_WeeklyHBEntitlementZero++;
            }
            WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
            if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
                RSLTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
                RSLTotalCount_WeeklyCTBEntitlementNonZero++;
            } else {
                RSLTotalWeeklyCTBEntitlementZeroCount++;
            }
            WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
            if (WeeklyEligibleRentAmount > 0) {
                RSLTotalWeeklyEligibleRentAmount += WeeklyEligibleRentAmount;
                RSLTotalWeeklyEligibleRentAmountNonZeroCount++;
            } else {
                RSLTotalWeeklyEligibleRentAmountZeroCount++;
            }
            if (TT == 4) {
                if (WeeklyEligibleRentAmount > 0) {
                    RSLTotalWeeklyEligibleRentAmountTT4 += WeeklyEligibleRentAmount;
                    RSLTotalWeeklyEligibleRentAmountNonZeroCountTT4++;
                } else {
                    RSLTotalWeeklyEligibleRentAmountZeroCountTT4++;
                }
            }

            WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
            if (WeeklyEligibleCouncilTaxAmount > 0) {
                RSLTotalWeeklyEligibleCouncilTaxAmount += WeeklyEligibleCouncilTaxAmount;
                RSLTotalCount_WeeklyEligibleCouncilTaxAmountNonZero++;
            } else {
                RSLTotalCount_WeeklyEligibleCouncilTaxAmountZero++;
            }
            ContractualRentAmount = D_Record.getContractualRentAmount();
            if (ContractualRentAmount > 0) {
                RSLTotalContractualRentAmount += ContractualRentAmount;
                RSLTotalContractualRentAmountNonZeroCount++;
            } else {
                RSLTotalContractualRentAmountZeroCount++;
            }
            WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
            if (WeeklyAdditionalDiscretionaryPayment > 0) {
                RSLTotalWeeklyAdditionalDiscretionaryPayment += WeeklyAdditionalDiscretionaryPayment;
                RSLTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount++;
            } else {
                RSLTotalWeeklyAdditionalDiscretionaryPaymentZeroCount++;
            }
            WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
            if (WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability > 0) {
                RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability += WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
                RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount++;
            } else {
                RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount++;
            }
            doRSLSingleTimeHBCount(
                    ClaimantsEthnicGroup0,
                    TT,
                    postcode,
                    yM30v);
        }
    }

    protected void doSingleTimeRentArrearsCount(DW_UO_Record UORec) {
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

    protected void doCouncilCompare2TimesHBCount(
            Integer tenancyType0,
            String postcode0,
            boolean isValidPostcode0,
            Integer tenancyType1,
            String postcode1,
            boolean isValidPostcode1) {
        super.doCompare2TimesHBCount(tenancyType0, postcode0, isValidPostcode0, tenancyType1, postcode1, isValidPostcode1);
        if (isValidPostcode0) {
            if (isValidPostcode1) {
                CouncilTotalCount_Postcode0ValidPostcode1Valid++;
                if (postcode0.equalsIgnoreCase(postcode1)) {
                    CouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged++;
                } else {
                    CouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged++;
                }
            } else {
                CouncilTotalCount_Postcode0ValidPostcode1NotValid++;
            }
        } else if (isValidPostcode1) {
            CouncilTotalCount_Postcode0NotValidPostcode1Valid++;
        } else {
            CouncilTotalCount_Postcode0NotValidPostcode1NotValid++;
            if (postcode0 == null) {
                CouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
            } else if (postcode1 == null) {
                CouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
            } else if (postcode0.equalsIgnoreCase(postcode1)) {
                CouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
            } else {
                CouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged++;
            }
        }
        if (tenancyType0.compareTo(tenancyType1) != 0) {
            if (!(tenancyType0 == tDW_SHBE_TenancyType_Handler.iMinus999
                    || tenancyType1 == tDW_SHBE_TenancyType_Handler.iMinus999)) {
                CouncilTotalCount_TTChangeClaimant++;
            }
            if (tenancyType0 == tDW_SHBE_TenancyType_Handler.iMinus999) {
                if (tenancyType1 == 1) {
                    CouncilTotalCount_Minus999TTToTT1++;
                }
            }
            if (tenancyType1 == tDW_SHBE_TenancyType_Handler.iMinus999) {
                if (tenancyType0 == 1) {
                    CouncilTotalCount_TT1ToMinus999TT++;
                }
            }
            if (tenancyType0 == 1) {
                if (tenancyType1 == 3 || tenancyType0 == 6) {
                    CouncilTotalCount_TT1ToPrivateDeregulatedTTs++;
                    if (tenancyType0 == 1) {
                        CouncilTotalCount_TT1ToPrivateDeregulatedTTs++;
                    }
                }
            }
            if (tenancyType0 == 3 || tenancyType0 == 6) {
                if (tenancyType1 == 1) {
                    CouncilTotalCount_PrivateDeregulatedTTsToTT1++;
                    if (tenancyType1 == 1) {
                        CouncilTotalCount_PrivateDeregulatedTTsToTT1++;
                    }
                }
            }
        }
        if (tenancyType0 == 1 && tenancyType1 == 1) {
            if (isValidPostcode0 && isValidPostcode1) {
                if (!postcode0.equalsIgnoreCase(postcode1)) {
                    CouncilTotalCount_PostcodeChangeWithinTT1++;
                }
            }
        }
        if (tenancyType0 == 1 && tenancyType1 == 4) {
            CouncilTotalCount_TT1ToTT4++;
        }
        if (tenancyType0 == 4 && tenancyType1 == 1) {
            CouncilTotalCount_TT4ToTT1++;
        }
        if (tenancyType0 == 5 || tenancyType0 == 7) {
            if (tenancyType1 == 1) {
                CouncilTotalCount_CTBTTsToTT1++;
            }
        }
    }

    protected void doRSLCompare2TimesHBCount(
            Integer tenancyType0,
            String postcode0,
            boolean isValidPostcode0,
            Integer tenancyType1,
            String postcode1,
            boolean isValidPostcode1) {
        super.doCompare2TimesHBCount(tenancyType0, postcode0, isValidPostcode0,
                tenancyType1, postcode1, isValidPostcode1);
        if (isValidPostcode0) {
            if (isValidPostcode1) {
                RSLTotalCount_Postcode0ValidPostcode1Valid++;
                if (postcode0.equalsIgnoreCase(postcode1)) {
                    RSLTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged++;
                } else {
                    RSLTotalCount_Postcode0ValidPostcode1ValidPostcodeChanged++;
                }
            } else {
                RSLTotalCount_Postcode0ValidPostcode1NotValid++;
            }
        } else if (isValidPostcode1) {
            RSLTotalCount_Postcode0NotValidPostcode1Valid++;
        } else {
            RSLTotalCount_Postcode0NotValidPostcode1NotValid++;
            if (postcode0 == null) {
                RSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
            } else if (postcode1 == null) {
                RSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
            } else if (postcode0.equalsIgnoreCase(postcode1)) {
                RSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged++;
            } else {
                RSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged++;
            }
        }
        if (tenancyType0.compareTo(tenancyType1) != 0) {
            if (!(tenancyType0 == tDW_SHBE_TenancyType_Handler.iMinus999
                    || tenancyType1 == tDW_SHBE_TenancyType_Handler.iMinus999)) {
                RSLTotalCount_TTChangeClaimant++;
            }
            if (tenancyType0 == tDW_SHBE_TenancyType_Handler.iMinus999) {
                if (tenancyType1 == 4) {
                    RSLTotalCount_Minus999TTToTT4++;
                }
            }
            if (tenancyType1 == tDW_SHBE_TenancyType_Handler.iMinus999) {
                if (tenancyType0 == 4) {
                    RSLTotalCount_TT4ToMinus999TT++;
                }
            }
            if (tenancyType0 == 4) {
                if (tenancyType1 == 3 || tenancyType0 == 6) {
                    RSLTotalCount_TT4ToPrivateDeregulatedTTs++;
                }
            }
            if (tenancyType0 == 3 || tenancyType0 == 6) {
                if (tenancyType1 == 4) {
                    RSLTotalCount_PrivateDeregulatedTTsToTT4++;
                }
            }
        }
        if (tenancyType0 == 4 && tenancyType1 == 4) {
            if (isValidPostcode0 && isValidPostcode1) {
                if (!postcode0.equalsIgnoreCase(postcode1)) {
                    RSLTotalCount_PostcodeChangeWithinTT4++;
                }
            }
        }
        if (tenancyType0 == 1 && tenancyType1 == 4) {
            RSLTotalCount_TT1ToTT4++;
        }
        if (tenancyType0 == 4 && tenancyType1 == 1) {
            RSLTotalCount_TT4ToTT1++;
        }
        if (tenancyType0 == 5 || tenancyType0 == 7) {
            if (tenancyType1 == 4) {
                RSLTotalCount_CTBTTsToTT4++;
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
    protected void doCouncilSingleTimeHBCount(
            int tEG,
            int tTT,
            String tP,
            String yM3v) {
        super.doSingleTimeHBCount(tEG, tTT, tP, yM3v);
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();
        CouncilCount1++;
        CouncilEthnicGroupCount[tEG]++;
        if (tP != null) {
            boolean isValidPostcodeFormat;
            isValidPostcodeFormat = Generic_UKPostcode_Handler.isValidPostcodeForm(tP);
            boolean isValidPostcode;
            isValidPostcode = tDW_Postcode_Handler.isValidPostcode(yM3v, tP);
            if (isValidPostcodeFormat) {
                CouncilTotalCount_PostcodeValidFormat++;
            }
            if (isValidPostcode) {
                CouncilTotalCount_PostcodeValid++;
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
    protected void doRSLSingleTimeHBCount(
            int tEG,
            int tTT,
            String tP,
            String yM3v) {
        super.doSingleTimeHBCount(tEG, tTT, tP, yM3v);
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();
        // RSL
        RSLCount1++;
        RSLEthnicGroupCount[tEG]++;
        if (tP != null) {
            boolean isValidPostcodeFormat;
            isValidPostcodeFormat = Generic_UKPostcode_Handler.isValidPostcodeForm(tP);
            boolean isValidPostcode;
            isValidPostcode = tDW_Postcode_Handler.isValidPostcode(yM3v, tP);
            if (isValidPostcodeFormat) {
                RSLTotalCount_PostcodeValidFormat++;
            }
            if (isValidPostcode) {
                RSLTotalCount_PostcodeValid++;
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
            TreeMap<DW_ID, DW_SHBE_Record> Records,
            DW_UO_Set councilUnderOccupiedSet,
            DW_UO_Set RSLUnderOccupiedSet
    ) {
        // TODO Do implementation for going through allUO, council and RSL.
//        TreeMap<String, DW_SHBE_Record> Records,
//            DW_UO_Set councilUnderOccupiedSet,
//            DW_UO_Set RSLUnderOccupiedSet
//        Integer[] counts;
//        counts = getCountsIDPostcode(
//                SHBEFilenames,
//                include,
//                yM31,
//                tClaimantIDPostcodes,
//                tClaimantIDPostcodes1);
//        summary.put(sSamePostcodeIII,
//                "" + counts[0]);
//        summary.put(sSamePostcodeIOI,
//                "" + counts[1]);
//        summary.put(sSamePostcodeOIO,
//                "" + counts[2]);
//        Object[] countsIDTT = getCountsIDTT(
//                nTT,
//                SHBEFilenames,
//                include,
//                yM31,
//                tClaimantIDTTs,
//                tClaimantIDTT1);
//        Integer[] mainCounts;
//        mainCounts = (Integer[]) countsIDTT[0];
//        Integer[] IIITTCounts;
//        Integer[] IOITTCounts;
//        Integer[] OIOTTCounts;
//        IIITTCounts = (Integer[]) countsIDTT[1];
//        IOITTCounts = (Integer[]) countsIDTT[2];
//        OIOTTCounts = (Integer[]) countsIDTT[3];
//        if (mainCounts == null) { // If mainCounts is null, then so are other counts
//            summary.put(sSameTenancyIII,
//                    null);
//            summary.put(sSameTenancyIOI,
//                    null);
//            summary.put(sSameTenancyOIO,
//                    null);
//            for (int TT = 1; TT < nTT; TT++) {
//                summary.put(
//                        sSameTenancyIIITT[TT],
//                        null);
//                summary.put(
//                        sSameTenancyIOITT[TT],
//                        null);
//                summary.put(
//                        sSameTenancyOIOTT[TT],
//                        null);
//            }
//        } else {
//            summary.put(sSameTenancyIII,
//                    "" + mainCounts[0]);
//            summary.put(sSameTenancyIOI,
//                    "" + mainCounts[1]);
//            summary.put(sSameTenancyOIO,
//                    "" + mainCounts[2]);
//            for (int TT = 1; TT < nTT; TT++) {
//                summary.put(
//                        sSameTenancyIIITT[TT],
//                        "" + IIITTCounts[TT]);
//                summary.put(
//                        sSameTenancyIOITT[TT],
//                        "" + IOITTCounts[TT]);
//                summary.put(
//                        sSameTenancyOIOTT[TT],
//                        "" + OIOTTCounts[TT]);
//            }
//        }
//        Object[] countsIDPostcodeTT = getCountsIDPostcodeTT(
//                nTT,
//                SHBEFilenames,
//                include,
//                yM31,
//                tClaimantIDPostcodeTTs,
//                tClaimantIDPostcodeTTs1);
//        mainCounts = (Integer[]) countsIDPostcodeTT[0];
//        IIITTCounts = (Integer[]) countsIDPostcodeTT[1];
//        IOITTCounts = (Integer[]) countsIDPostcodeTT[2];
//        OIOTTCounts = (Integer[]) countsIDPostcodeTT[3];
//        if (mainCounts == null) { // If mainCounts is null, then so are other counts
//            summary.put(sSameTenancyAndPostcodeIII,
//                    null);
//            summary.put(sSameTenancyAndPostcodeIOI,
//                    null);
//            summary.put(sSameTenancyAndPostcodeOIO,
//                    null);
//            for (int TT = 1; TT < nTT; TT++) {
//                summary.put(
//                        sSameTenancyAndPostcodeIIITT[TT],
//                        null);
//                summary.put(
//                        sSameTenancyAndPostcodeIOITT[TT],
//                        null);
//                summary.put(
//                        sSameTenancyAndPostcodeOIOTT[TT],
//                        null);
//            }
//        } else {
//            summary.put(sSameTenancyAndPostcodeIII,
//                    "" + mainCounts[0]);
//            summary.put(sSameTenancyAndPostcodeIOI,
//                    "" + mainCounts[1]);
//            summary.put(sSameTenancyAndPostcodeOIO,
//                    "" + mainCounts[2]);
//            for (int TT = 1; TT < nTT; TT++) {
//                summary.put(
//                        sSameTenancyAndPostcodeIIITT[TT],
//                        "" + IIITTCounts[TT]);
//                summary.put(
//                        sSameTenancyAndPostcodeIOITT[TT],
//                        "" + IOITTCounts[TT]);
//                summary.put(
//                        sSameTenancyAndPostcodeOIOTT[TT],
//                        "" + OIOTTCounts[TT]);
//            }
//        }
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
     * @param DW_UO_Data
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
            DW_UO_Data DW_UO_Data,
            HashMap<DW_PersonID, DW_ID> tDW_PersonIDToIDLookup,
            //HashMap<String, DW_longID> tPostcodeToPostcodeIDLookup,
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
        TreeMap<String, DW_UO_Set> councilUnderOccupiedSets;
        TreeMap<String, DW_UO_Set> RSLUnderOccupiedSets;
        councilUnderOccupiedSets = DW_UO_Data.getCouncilSets();
        RSLUnderOccupiedSets = DW_UO_Data.getRSLSets();
        // Initialise first data
        Iterator<Integer> includeIte;
        DW_SHBE_Collection tSHBEData1 = null;
        includeIte = include.iterator();
        boolean initFirst = false;
        String yM31 = "";
        String yM31v;
        //yM31v = "";
        DW_UO_Set councilUnderOccupiedSet1 = null;
        DW_UO_Set RSLUnderOccupiedSet1 = null;
        String filename1 = null;
        int i;
        while (!initFirst) {
            i = includeIte.next();
            // Load first data
            filename1 = SHBEFilenames[i];
            yM31 = tDW_SHBE_Handler.getYM3(filename1);
            councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(yM31);
            if (councilUnderOccupiedSet1 != null) {
                RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(yM31);
                System.out.println("Load " + filename1);
                tSHBEData1 = new DW_SHBE_Collection(env, filename1, paymentType);
                initFirst = true;
            }
        }
        if (tSHBEData1 == null) {
            return result;
        }
        
        Object[] filenames;
        filenames = env.getDW_UO_Handler().getInputFilenames();
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
            yM3 = tDW_SHBE_Handler.getYM3(SHBEFilenames[j]);
            DW_UO_Set aCouncilUnderOccupiedSet;
            aCouncilUnderOccupiedSet = councilUnderOccupiedSets.get(yM3);
            if (aCouncilUnderOccupiedSet != null) {
                HashMap<String, String> summary;
                summary = new HashMap<String, String>();
                String key;
                key = tDW_SHBE_Handler.getYearMonthNumber(SHBEFilenames[j]);
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
        DW_UO_Set councilUnderOccupiedSet0;
        DW_UO_Set RSLUnderOccupiedSet0;

        if (includeIte.hasNext()) {
            i = includeIte.next();

            filename0 = filename1;
            yM30 = yM31;
            yM30v = yM31v;
            tSHBEData0 = tSHBEData1;
            councilUnderOccupiedSet0 = councilUnderOccupiedSet1;
            RSLUnderOccupiedSet0 = RSLUnderOccupiedSet1;

            filename1 = SHBEFilenames[i];
            yM31 = tDW_SHBE_Handler.getYM3(filename1);
            yM31v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
            councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(yM31);
            RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(yM31);
            System.out.println("Load " + filename1);
            tSHBEData1 = new DW_SHBE_Collection(env, filename1, paymentType);

            //initCounts(nTT, nEG, nPSI);
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
                yM31 = tDW_SHBE_Handler.getYM3(filename1);
                yM31v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
                councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(yM31);
                RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(yM31);
                System.out.println("Load " + filename1);
                tSHBEData1 = new DW_SHBE_Collection(env, filename1, paymentType);

                //initCounts(nTT, nEG, nPSI);
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

//                //DO SOME SUMMARY
//                doPartSummaryCompare3Times(
//                        SHBEFilenames,
//                        include,
//                        tSHBEData1,
//                        yM31,
//                        yM31v,
//                        filename1,
//                        tSHBEData0,
//                        yM30,
//                        yM30v,
//                        filename0,
//                        yM300,
//                        filename00,
//                        forceNewSummaries,
//                        paymentType,
//                        nTT,
//                        nEG,
//                        nPSI,
//                        councilFilenames,
//                        RSLFilenames,
//                        councilUnderOccupiedSet1,
//                        RSLUnderOccupiedSet1,
//                        councilUnderOccupiedSet0,
//                        RSLUnderOccupiedSet0,
//                        result,
//                        tClaimantIDPostcodes,
//                        tClaimantIDTTs,
//                        tClaimantIDPostcodeTTs,
//                        tDW_PersonIDToIDLookup,
//                        tPostcodeToPostcodeIDLookup);
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
            DW_UO_Set tCouncilUnderOccupiedSet1,
            DW_UO_Set tRSLUnderOccupiedSet1,
            DW_UO_Set tCouncilUnderOccupiedSet0,
            DW_UO_Set tRSLUnderOccupiedSet0,
            TreeMap<String, HashMap<String, String>> result,
            TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes,
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTTs,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTTs,
            HashMap<DW_PersonID, DW_ID> tDW_PersonIDToIDLookup,
            HashMap<String, DW_ID> tPostcodeToPostcodeIDLookup) {
        // Set counters
        CouncilLinkedRecordCount00 = CouncilLinkedRecordCount0;
        RSLLinkedRecordCount00 = RSLLinkedRecordCount0;
        AllUOLinkedRecordCount00 = AllUOLinkedRecordCount0;
//        AllUOAllCount00 = AllUOAllCount0;

        //initCounts(nTT, nEG, nPSI);
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
        key = tDW_SHBE_Handler.getYearMonthNumber(filename1);
        HashMap<String, String> summary;
        summary = result.get(key);
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
                tClaimantIDPostcodeTTs1,
                tSHBEData1.getRecords(),
                tCouncilUnderOccupiedSet1,
                tRSLUnderOccupiedSet1);
        summary.put(sSHBEFilename00, filename00);
        summary.put(sSHBEFilename0, filename0);
        summary.put(sSHBEFilename1, filename1);
        summary.put(sCouncilFilename00, tCouncilFilenames.get(yM300));
        summary.put(sRSLFilename00, tRSLFilenames.get(yM300));
        summary.put(sCouncilLinkedRecordCount00, Integer.toString(CouncilLinkedRecordCount00));
        summary.put(sRSLLinkedRecordCount00, Integer.toString(RSLLinkedRecordCount00));
        summary.put(sAllUOLinkedRecordCount00, Integer.toString(CouncilLinkedRecordCount00 + RSLLinkedRecordCount00));
        summary.put(sCouncilFilename0, tCouncilFilenames.get(yM30));
        summary.put(sRSLFilename0, tRSLFilenames.get(yM30));
        summary.put(sCouncilLinkedRecordCount0, Integer.toString(CouncilLinkedRecordCount0));
        summary.put(sRSLLinkedRecordCount0, Integer.toString(RSLLinkedRecordCount0));
        summary.put(sAllUOLinkedRecordCount0, Integer.toString(CouncilLinkedRecordCount0 + RSLLinkedRecordCount0));
        summary.put(sCouncilFilename1, tCouncilFilenames.get(yM31));
        summary.put(sRSLFilename1, tRSLFilenames.get(yM31));
        summary.put(sCouncilLinkedRecordCount1, Integer.toString(CouncilLinkedRecordCount1));
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
            DW_UO_Set councilUnderOccupiedSet1,
            DW_UO_Set RSLUnderOccupiedSet1,
            DW_UO_Set councilUnderOccupiedSet0,
            DW_UO_Set RSLUnderOccupiedSet0,
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

        TreeMap<DW_ID, DW_SHBE_Record> tDRecords1;
        tDRecords1 = tSHBEData1.getRecords();
        TreeMap<DW_ID, DW_SHBE_Record> tDRecords0;
        tDRecords0 = tSHBEData0.getRecords();

        TreeMap<DW_ID, DW_UO_Record> councilUnderOccupiedSet0Map;
        councilUnderOccupiedSet0Map = councilUnderOccupiedSet0.getMap();
        TreeMap<DW_ID, DW_UO_Record> RSLUnderOccupiedSet0Map;
        RSLUnderOccupiedSet0Map = RSLUnderOccupiedSet0.getMap();
        TreeMap<DW_ID, DW_UO_Record> councilUnderOccupiedSet1Map;
        councilUnderOccupiedSet1Map = councilUnderOccupiedSet1.getMap();
        TreeMap<DW_ID, DW_UO_Record> RSLUnderOccupiedSet1Map;
        RSLUnderOccupiedSet1Map = RSLUnderOccupiedSet1.getMap();

        Iterator<String> ite;
//        // Go through all those in D_Records0 and do non single time counts for 
//        // those in current.
//        ite = tDRecords0.keySet().iterator();
//        while (ite.hasNext()) {
//            String CTBRef;
//            CTBRef = ite.next();
//            DW_SHBE_Record Record0;
//            Record0 = tDRecords0.get(CTBRef);
//            if (councilUnderOccupiedSet1Map.containsKey(CTBRef) || RSLUnderOccupiedSet1Map.containsKey(CTBRef)) {
//                DW_SHBE_Record Record1;
//                Record1 = tDRecords0.get(CTBRef);
//                DW_SHBE_D_Record D_Record0;
//                D_Record0 = null;
//                if (Record0 != null) {
//                    D_Record0 = Record0.getDRecord();
//                }
//                DW_SHBE_D_Record D_Record1;
//                D_Record1 = null;
//                if (D_Record1 != null) {
//                    D_Record1 = Record1.getDRecord();
//                    super.doCompare2TimesCounts(D_Record0, D_Record1, yM30v, yM31v);
//                }
//            }
//        }

        // Loop over underoccupancy data
        // Loop over Council
        doCouncilCompare2TimesLoopOverSet(
                councilUnderOccupiedSet0Map,
                councilUnderOccupiedSet1Map,
                tDRecords0,
                tDRecords1,
                yM30v,
                yM31v);
        // Loop over RSL
        doRSLCompare2TimesLoopOverSet(
                RSLUnderOccupiedSet0Map,
                RSLUnderOccupiedSet1Map,
                tDRecords0,
                tDRecords1,
                yM30v,
                yM31v);

        String key;
        key = tDW_SHBE_Handler.getYearMonthNumber(filename1);
        HashMap<String, String> summary;
        summary = summaries.get(key);

        addToSummaryCompare2Times(nTT, nEG, nPSI, summary);

        // All
        summary.put(sSHBEFilename0, filename0);
        summary.put(sSHBEFilename1, filename1);
        summary.put(sCouncilFilename0, councilFilenames.get(yM30));
        summary.put(sRSLFilename0, RSLFilenames.get(yM30));
        summary.put(sCouncilLinkedRecordCount0, Integer.toString(CouncilLinkedRecordCount0));
        summary.put(sRSLLinkedRecordCount0, Integer.toString(RSLLinkedRecordCount0));
        summary.put(sAllUOLinkedRecordCount0, Integer.toString(CouncilLinkedRecordCount0 + RSLLinkedRecordCount0));
        summary.put(sCouncilFilename1, councilFilenames.get(yM31));
        summary.put(sRSLFilename1, RSLFilenames.get(yM31));
        summary.put(sCouncilLinkedRecordCount1, Integer.toString(CouncilLinkedRecordCount1));
        summary.put(sRSLLinkedRecordCount1, Integer.toString(RSLLinkedRecordCount1));
        summary.put(sAllUOLinkedRecordCount1, Integer.toString(CouncilLinkedRecordCount1 + RSLLinkedRecordCount1));
    }

    protected void addToSetsForComparisons(
            String yM3,
            TreeMap<DW_ID, DW_SHBE_Record> D_Records,
            DW_UO_Set councilUnderOccupiedSet,
            DW_UO_Set RSLUnderOccupiedSet,
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
//            tClaimantIDPostcodes0 = tClaimantIDPostcodes.get(yM3);
        } else {
            tClaimantIDPostcodes0 = getID_PostcodeIDSet(
                    D_Records, councilUnderOccupiedSet, RSLUnderOccupiedSet,
                    DW_PersonIDToIDLookup, PostcodeToPostcodeIDLookup);
            tClaimantIDPostcodes.put(yM3, tClaimantIDPostcodes0);
        }
        if (tClaimantIDTTs.containsKey(yM3)) {
//            tClaimantIDTTs0 = tClaimantIDTTs.get(yM3);
        } else {
            tClaimantIDTTs0 = getID_TenancyTypeSet(
                    D_Records, councilUnderOccupiedSet, RSLUnderOccupiedSet,
                    DW_PersonIDToIDLookup);
            tClaimantIDTTs.put(yM3, tClaimantIDTTs0);
        }
        if (tClaimantIDPostcodeTTs.containsKey(yM3)) {
//            tClaimantIDPostcodeTTs0 = tClaimantIDPostcodeTTs.get(yM3);
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
            DW_UO_Set underOccupiedSetCouncil,
            DW_UO_Set underOccupiedSetRSL,
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
//        AllUOAllCount0 = AllUOAllCount1;
//        CouncilAllCount0 = CouncilAllCount1;
//        RSLCount0 = RSLCount1;
//        CouncilLinkedRecordCount0 = CouncilLinkedRecordCount1;
//        RSLLinkedRecordCount0 = RSLLinkedRecordCount1;
//        AllCount0 = AllCount1;
//        AllUOLinkedRecordCount0 = AllUOLinkedRecordCount1;
////        for (int TT = 0; TT < nTT; TT++) {
////            AllTotalCountTTClaimant0[TT] = AllTotalCountTTClaimant1[TT];
////        }
//        System.arraycopy(TotalCount_TTClaimant1, 0, TotalCount_TTClaimant0, 0, nTT);

        TreeMap<DW_ID, DW_SHBE_Record> tDRecords;
        tDRecords = tSHBEData.getRecords();
        String key;
        key = tDW_SHBE_Handler.getYearMonthNumber(filename);
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
                underOccupiedSetCouncil,
                underOccupiedSetRSL,
                tClaimantIDPostcodes,
                tClaimantIDTTs,
                tClaimantIDPostcodeTTs,
                tDW_PersonIDToIDLookup,
                tPostcodeToPostcodeIDLookup);

        TreeMap<DW_ID, DW_UO_Record> underOccupiedSetMapCouncil;
        underOccupiedSetMapCouncil = underOccupiedSetCouncil.getMap();
        TreeMap<DW_ID, DW_UO_Record> underOccupiedSetMapRSL;
        underOccupiedSetMapRSL = underOccupiedSetRSL.getMap();
        // Loop over underoccupancy data
        // Loop over Council
        CouncilLinkedRecordCount1 = doCouncilSingleTimeLoopOverSet(
                underOccupiedSetMapCouncil, tDRecords, yM3v);
        // To calculate just council percentages and rates we should do this here.
        // Loop over RSL
        RSLLinkedRecordCount1 = doRSLSingleTimeLoopOverSet(
                underOccupiedSetMapRSL, tDRecords, yM3v);
        // The above adds to the councilo calculate just council percentages and rates we should do this here.
        // Prepare vars
        CouncilCount1 = underOccupiedSetMapCouncil.size();
        RSLCount1 = underOccupiedSetMapRSL.size();
        AllUOAllCount1 = CouncilCount1 + RSLCount1;
        AllUOLinkedRecordCount1 = CouncilLinkedRecordCount1 + RSLLinkedRecordCount1;
        // Add to counts
        HashMap<String, BigDecimal> incomeAndRentSummaryAllUO;
        incomeAndRentSummaryAllUO = tDW_SHBE_Handler.getIncomeAndRentSummary(
                tSHBEData,
                paymentType,
                filename,
                underOccupiedSetCouncil,
                underOccupiedSetRSL,
                true,
                true,
                true,
                forceNewSummaries);
        HashMap<String, BigDecimal> incomeAndRentSummaryCouncil;
        incomeAndRentSummaryCouncil = tDW_SHBE_Handler.getIncomeAndRentSummary(
                tSHBEData,
                paymentType,
                filename,
                underOccupiedSetCouncil,
                null, //underOccupiedSetRSL,
                true,
                true,
                false,
                forceNewSummaries);
        HashMap<String, BigDecimal> incomeAndRentSummaryRSL;
        incomeAndRentSummaryRSL = tDW_SHBE_Handler.getIncomeAndRentSummary(
                tSHBEData,
                paymentType,
                filename,
                null, //underOccupiedSetCouncil, 
                underOccupiedSetRSL,
                true,
                false,
                true,
                forceNewSummaries);
        addToSummarySingleTimeIncomeAndRent(
                summary,
                incomeAndRentSummaryAllUO,
                incomeAndRentSummaryCouncil,
                incomeAndRentSummaryRSL);
        addToSummarySingleTime(nTT, nEG, nPSI, summary);
        addToSummarySingleTimeRentArrears(summary);
        summary.put(sSHBEFilename1, filename);
        summary.put(sCouncilFilename1, councilFilenames.get(yM3));
        summary.put(sRSLFilename1, RSLFilenames.get(yM3));
        summary.put(sCouncilLinkedRecordCount1, Integer.toString(CouncilLinkedRecordCount1));
        summary.put(sRSLLinkedRecordCount1, Integer.toString(RSLLinkedRecordCount1));

        summary.put(sAllCount1, Integer.toString(AllCount1));

        summary.put(sAllUOAllCount1, Integer.toString(AllUOAllCount1));

        summary.put(sAllUOLinkedRecordCount1, Integer.toString(AllUOLinkedRecordCount1));
    }

    public void doCouncilCompare2TimesLoopOverSet(
            TreeMap<DW_ID, DW_UO_Record> map0,
            TreeMap<DW_ID, DW_UO_Record> map1,
            TreeMap<DW_ID, DW_SHBE_Record> D_Records0,
            TreeMap<DW_ID, DW_SHBE_Record> D_Records1,
            String yM30v,
            String yM31v) {
        Iterator<DW_ID> ite;
        // Go through all those in D_Records0 and do non single time counts for 
        // those in current.
        ite = D_Records0.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            if (map1.containsKey(ClaimID)) {
                doCouncilCompare2TimesCountsAsNecessary(
                        ClaimID,
                        D_Records0,
                        D_Records1,
                        yM30v,
                        yM31v);
            }
        }
        // Go through all those in current UO data.      
        ite = map1.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            DW_UO_Record UORec;
            UORec = map1.get(ClaimID);
            // Rent Arrears Summary
            doSingleTimeRentArrearsCount(UORec);
//            String CTBRef;
//            CTBRef = UORec.getClaimReferenceNumber();
            if (!map0.keySet().contains(ClaimID)) {
            doCouncilCompare2TimesCountsAsNecessary(
                    ClaimID,
                    D_Records0,
                    D_Records1,
                    yM30v,
                    yM31v);
            }
        }
        // Go through all those that were in the UO data, but have come out.
        HashSet<DW_ID> claimsOutOfUO;
        claimsOutOfUO = new HashSet<DW_ID>();
        claimsOutOfUO.addAll(map0.keySet());
        claimsOutOfUO.removeAll(map1.keySet());
        ite = claimsOutOfUO.iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            DW_UO_Record UORec;
            UORec = map0.get(ClaimID);
            // Rent Arrears Summary
            doSingleTimeRentArrearsCount(UORec);
//            String CTBRef;
//            CTBRef = UORec.getClaimReferenceNumber();
            doCouncilCompare2TimesCountsAsNecessary(
                    ClaimID,
                    D_Records0,
                    D_Records1,
                    yM30v,
                    yM31v);
        }
    }

    private void doCouncilCompare2TimesCountsAsNecessary(
            DW_ID ClaimID,
            TreeMap<DW_ID, DW_SHBE_Record> D_Records0,
            TreeMap<DW_ID, DW_SHBE_Record> D_Records1,
            String yM30v,
            String yM31v) {
        DW_SHBE_Record Record0;
        Record0 = D_Records0.get(ClaimID);
        DW_SHBE_Record Record1;
        Record1 = D_Records1.get(ClaimID);
        DW_SHBE_D_Record D_Record0;
        D_Record0 = null;
        if (Record0 != null) {
            D_Record0 = Record0.getDRecord();
        }
        DW_SHBE_D_Record D_Record1;
//        D_Record1 = null;
        if (Record1 != null) {
            D_Record1 = Record1.getDRecord();
            //if (D_Record0 == null) {
            doCouncilCompare2TimesCounts(
                    false,
                    D_Record0,
                    D_Record1,
                    yM30v,
                    yM31v);
            //}
        }
    }

    public void doRSLCompare2TimesLoopOverSet(
            TreeMap<DW_ID, DW_UO_Record> map0,
            TreeMap<DW_ID, DW_UO_Record> map1,
            TreeMap<DW_ID, DW_SHBE_Record> D_Records0,
            TreeMap<DW_ID, DW_SHBE_Record> D_Records1,
            String yM30v,
            String yM31v) {
        Iterator<DW_ID> ite;
        // Go through all those in D_Records0 and do non single time counts for 
        // all those that are in map1
        ite = D_Records0.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            if (map1.containsKey(ClaimID)) {
                doRSLCompare2TimesCountsAsNecessary(
                        ClaimID,
                        D_Records0,
                        D_Records1,
                        yM30v,
                        yM31v);
            }
        }
        // Go through all those in current UO data.
        ite = map1.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            DW_UO_Record UORec;
            UORec = map1.get(ClaimID);
            // Rent Arrears Summary
            doSingleTimeRentArrearsCount(UORec);
//            String CTBRef;
//            CTBRef = UORec.getClaimReferenceNumber();
            if (!map0.keySet().contains(ClaimID)) {
                doRSLCompare2TimesCountsAsNecessary(
                        ClaimID,
                        D_Records0,
                        D_Records1,
                        yM30v,
                        yM31v);
            }
        }
        // Go through all those that were in the UO data, but have come out.
        HashSet<DW_ID> claimsOutOfUO;
        claimsOutOfUO = new HashSet<DW_ID>();
        claimsOutOfUO.addAll(map0.keySet());
        claimsOutOfUO.removeAll(map1.keySet());
        ite = claimsOutOfUO.iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            DW_UO_Record UORec;
            UORec = map0.get(ClaimID);
            // Rent Arrears Summary
            doSingleTimeRentArrearsCount(UORec);
//            String CTBRef;
//            CTBRef = UORec.getClaimReferenceNumber();
            doRSLCompare2TimesCountsAsNecessary(
                    ClaimID,
                    D_Records0,
                    D_Records1,
                    yM30v,
                    yM31v);
        }
    }

    private void doRSLCompare2TimesCountsAsNecessary(
            DW_ID ClaimID,
            TreeMap<DW_ID, DW_SHBE_Record> D_Records0,
            TreeMap<DW_ID, DW_SHBE_Record> D_Records1,
            String yM30v,
            String yM31v) {
        DW_SHBE_Record Record0;
        Record0 = D_Records0.get(ClaimID);
        DW_SHBE_Record Record1;
        Record1 = D_Records1.get(ClaimID);
        DW_SHBE_D_Record D_Record0;
        D_Record0 = null;
        if (Record0 != null) {
            D_Record0 = Record0.getDRecord();
        }
        DW_SHBE_D_Record D_Record1;
        D_Record1 = null;
        if (Record1 == null) {
            doRSLCompare2TimesCounts(
                    false,
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
    public int doCouncilSingleTimeLoopOverSet(
            TreeMap<DW_ID, DW_UO_Record> map,
            TreeMap<DW_ID, DW_SHBE_Record> D_Records,
            String yM30v) {
        int linkedRecords;
        linkedRecords = 0;
        Iterator<DW_ID> ite;
        ite = map.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            DW_UO_Record UORec;
            UORec = map.get(ClaimID);
            // Rent Arrears Summary
            doSingleTimeRentArrearsCount(UORec);
            DW_SHBE_Record Record;
            Record = D_Records.get(ClaimID);
            if (Record == null) {
                //tDRecordsCTBRefDW_SHBE_RecordNullCount++;
                int count = 1;
            } else {
                DW_SHBE_D_Record D_Record;
                D_Record = Record.getDRecord();
//                boolean isHBClaim;
//                isHBClaim = false;
//                boolean isCTBOnlyClaim;
//                isCTBOnlyClaim = false;
                if (D_Record != null) {
                    doCouncilSingleTimeCount(D_Record, yM30v);
//                    isHBClaim = DW_SHBE_Handler.isHBClaim(D_Record);
//                    isCTBOnlyClaim = DW_SHBE_Handler.isCTBOnlyClaim(D_Record);
//                    int tEG;
//                    tEG = D_Record.getClaimantsEthnicGroup();
//                    String tP;
//                    tP = D_Record.getClaimantsPostcode();
//                    if (isCTBOnlyClaim) {
//                        doCouncilSingleTimeCTBCount(
//                                tEG,
//                                tP,
//                                yM30v);
//                    } else {
//                        doCouncilSingleTimeHBCount(
//                                tEG,
//                                tP,
//                                yM30v);
//                    }
                }
                linkedRecords++;
            }
        }
        return linkedRecords;
    }

    /**
     *
     * @param map
     * @param D_Records
     * @param yM30v
     * @return
     */
    public int doRSLSingleTimeLoopOverSet(
            TreeMap<DW_ID, DW_UO_Record> map,
            TreeMap<DW_ID, DW_SHBE_Record> D_Records,
            String yM30v) {
        int linkedRecords;
        linkedRecords = 0;
        Iterator<DW_ID> ite;
        ite = map.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            DW_UO_Record UORec;
            UORec = map.get(ClaimID);
            // Rent Arrears Summary
            doSingleTimeRentArrearsCount(UORec);
            DW_SHBE_Record Record;
            Record = D_Records.get(ClaimID);
            if (Record == null) {
                //tDRecordsCTBRefDW_SHBE_RecordNullCount++;
                int count = 1;
            } else {
                DW_SHBE_D_Record D_Record;
                D_Record = Record.getDRecord();
//                boolean isHBClaim;
//                isHBClaim = false;
//                boolean isCTBOnlyClaim;
//                isCTBOnlyClaim = false;
                if (D_Record != null) {
                    doRSLSingleTimeCount(D_Record, yM30v);
//                    isHBClaim = DW_SHBE_Handler.isHBClaim(D_Record);
//                    isCTBOnlyClaim = DW_SHBE_Handler.isCTBOnlyClaim(D_Record);
//                    int tEG;
//                    tEG = D_Record.getClaimantsEthnicGroup();
//                    String tP;
//                    tP = D_Record.getClaimantsPostcode();
//                    if (isCTBOnlyClaim) {
//                        doRSLSingleTimeCTBCount(
//                                tEG,
//                                tP,
//                                yM30v);
//                    } else {
//                        doRSLSingleTimeHBCount(
//                                tEG,
//                                tP,
//                                yM30v);
//                    }
                }
                linkedRecords++;
            }
        }
        return linkedRecords;
    }

    protected void addToSummarySingleTimeIncomeAndRent(
            HashMap<String, String> summary,
            HashMap<String, BigDecimal> incomeAndRentSummaryAllUO,
            HashMap<String, BigDecimal> incomeAndRentSummaryCouncil,
            HashMap<String, BigDecimal> incomeAndRentSummaryRSL) {
        Iterator<String> incomeAndRentSummaryKeySetIte;
        incomeAndRentSummaryKeySetIte = incomeAndRentSummaryAllUO.keySet().iterator();
        while (incomeAndRentSummaryKeySetIte.hasNext()) {
            String name;
            name = incomeAndRentSummaryKeySetIte.next();
            String value;
            value = Generic_BigDecimal.roundIfNecessary(
                    incomeAndRentSummaryAllUO.get(name), 2, RoundingMode.HALF_UP).toPlainString();
            summary.put(
                    "AllUO" + name,
                    value);
        }
        incomeAndRentSummaryKeySetIte = incomeAndRentSummaryCouncil.keySet().iterator();
        while (incomeAndRentSummaryKeySetIte.hasNext()) {
            String name;
            name = incomeAndRentSummaryKeySetIte.next();
            String value;
            value = Generic_BigDecimal.roundIfNecessary(
                    incomeAndRentSummaryCouncil.get(name), 2, RoundingMode.HALF_UP).toPlainString();
            summary.put(
                    "Council" + name,
                    value);
        }
        incomeAndRentSummaryKeySetIte = incomeAndRentSummaryRSL.keySet().iterator();
        while (incomeAndRentSummaryKeySetIte.hasNext()) {
            String name;
            name = incomeAndRentSummaryKeySetIte.next();
            String value;
            value = Generic_BigDecimal.roundIfNecessary(
                    incomeAndRentSummaryRSL.get(name), 2, RoundingMode.HALF_UP).toPlainString();
            summary.put(
                    "RSL" + name,
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
        ONSPDFiles = env.getDW_Postcode_Handler().getONSPDFiles();
        String name;
        name = "Compare3Times";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
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
        header += sAllCount00 + ", ";
        header += sAllUOLinkedRecordCount00 + ", ";
        header += sCouncilFilename0 + ", ";
        header += sRSLFilename0 + ", ";
        header += sCouncilCount0 + ", ";
        header += sRSLCount0 + ", ";
        header += sAllCount0 + ", ";
        header += sAllUOLinkedRecordCount1 + ", ";
        header += sCouncilFilename1 + ", ";
        header += sRSLFilename1 + ", ";
        header += sCouncilCount1 + ", ";
        header += sRSLCount1 + ", ";
        header += sAllCount1 + ", ";
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
                        tDW_SHBE_Handler.getYM3(filename00));
                PostCodeLookupFile00Name = ONSPDFiles.get(PostCodeLookupDate00).getName();
            }
            line += PostCodeLookupDate00 + ", " + PostCodeLookupFile00Name + ", ";
            String PostCodeLookupDate0 = null;
            String PostCodeLookupFile0Name = null;
            if (filename0 != null) {
                PostCodeLookupDate0 = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                        tDW_SHBE_Handler.getYM3(filename0));
                PostCodeLookupFile0Name = ONSPDFiles.get(PostCodeLookupDate0).getName();
            }
            line += PostCodeLookupDate0 + ", " + PostCodeLookupFile0Name + ", ";
            String PostCodeLookupDate1 = null;
            String PostCodeLookupFile1Name = null;
            if (filename1 != null) {
                PostCodeLookupDate1 = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                        tDW_SHBE_Handler.getYM3(filename1));
                PostCodeLookupFile1Name = ONSPDFiles.get(PostCodeLookupDate1).getName();
            }
            line += PostCodeLookupDate1 + ", " + PostCodeLookupFile1Name + ", ";
            line += summary.get(sCouncilFilename00) + ", ";
            line += summary.get(sRSLFilename00) + ", ";
            line += summary.get(sCouncilCount00) + ", ";
            line += summary.get(sRSLCount00) + ", ";
            line += summary.get(sAllCount00) + ", ";
            line += summary.get(sAllUOLinkedRecordCount00) + ", ";
            line += summary.get(sCouncilFilename0) + ", ";
            line += summary.get(sRSLFilename0) + ", ";
            line += summary.get(sCouncilCount0) + ", ";
            line += summary.get(sRSLCount0) + ", ";
            line += summary.get(sAllCount0) + ", ";
            line += summary.get(sAllUOLinkedRecordCount0) + ", ";
            line += summary.get(sCouncilFilename1) + ", ";
            line += summary.get(sRSLFilename1) + ", ";
            line += summary.get(sCouncilCount1) + ", ";
            line += summary.get(sRSLCount1) + ", ";
            line += summary.get(sAllCount1) + ", ";
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
        ONSPDFiles = env.getDW_Postcode_Handler().getONSPDFiles();
        String name;
        name = "Compare2Times";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = getHeaderCompare2TimesGeneric();
//        header += getHeaderCompare2TimesTTChange();
        header += getHeaderCompare2TimesTTChangeCouncil();
        header += getHeaderCompare2TimesTTChangeRSL();
//        header += getHeaderCompare2TimesPostcodeChange();
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
            line = getLineCompare2TimesGeneric(summary, ONSPDFiles);
//            line += getLineCompare2TimesTTChange(summary);
            line += getLineCompare2TimesTTChangeCouncil(summary);
            line += getLineCompare2TimesTTChangeRSL(summary);
//            line += getLineCompare2TimesPostcodeChange(summary);
            line += getLineCompare2TimesPostcodeChangeCouncil(summary);
            line += getLineCompare2TimesPostcodeChangeRSL(summary);
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    protected String getHeaderCompare2TimesPostcodeChangeCouncil() {
        String header = "";
        header += sCouncilTotalCount_Postcode0ValidPostcode1Valid + ", ";
        header += sCouncilPercentagePostcode0ValidPostcode1Valid + ", ";
        header += sCouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sCouncilPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sCouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sCouncilPercentagePostcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sCouncilTotalCount_Postcode0ValidPostcode1NotValid + ", ";
        header += sCouncilPercentagePostcode0ValidPostcode1NotValid + ", ";
        header += sCouncilTotalCount_Postcode0NotValidPostcode1Valid + ", ";
        header += sCouncilPercentagePostcode0NotValidPostcode1Valid + ", ";
        header += sCouncilTotalCount_Postcode0NotValidPostcode1NotValid + ", ";
        header += sCouncilPercentagePostcode0NotValidPostcode1NotValid + ", ";
        header += sCouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sCouncilPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sCouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sCouncilPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        return header;
    }

    protected String getHeaderCompare2TimesPostcodeChangeRSL() {
        String header = "";
        header += sRSLTotalCount_Postcode0ValidPostcode1Valid + ", ";
        header += sRSLPercentagePostcode0ValidPostcode1Valid + ", ";
        header += sRSLTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sRSLPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sRSLTotalCount_Postcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sRSLPercentagePostcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sRSLTotalCount_Postcode0ValidPostcode1NotValid + ", ";
        header += sRSLPercentagePostcode0ValidPostcode1NotValid + ", ";
        header += sRSLTotalCount_Postcode0NotValidPostcode1Valid + ", ";
        header += sRSLPercentagePostcode0NotValidPostcode1Valid + ", ";
        header += sRSLTotalCount_Postcode0NotValidPostcode1NotValid + ", ";
        header += sRSLPercentagePostcode0NotValidPostcode1NotValid + ", ";
        header += sRSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sRSLPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sRSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sRSLPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        return header;
    }

    protected String getLineCompare2TimesPostcodeChangeCouncil(HashMap<String, String> summary) {
        String line = "";
        line += summary.get(sCouncilTotalCount_Postcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sCouncilPercentagePostcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sCouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sCouncilPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sCouncilTotalCount_Postcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sCouncilPercentagePostcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sCouncilTotalCount_Postcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sCouncilPercentagePostcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sCouncilTotalCount_Postcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sCouncilPercentagePostcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sCouncilTotalCount_Postcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sCouncilPercentagePostcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sCouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sCouncilPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sCouncilTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        line += summary.get(sCouncilPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        return line;
    }

    protected String getLineCompare2TimesPostcodeChangeRSL(HashMap<String, String> summary) {
        String line = "";
        line += summary.get(sRSLTotalCount_Postcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sRSLPercentagePostcode0ValidPostcode1Valid) + ", ";
        line += summary.get(sRSLTotalCount_Postcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sRSLPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
        line += summary.get(sRSLTotalCount_Postcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sRSLPercentagePostcode0ValidPostcode1ValidPostcodeChange) + ", ";
        line += summary.get(sRSLTotalCount_Postcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sRSLPercentagePostcode0ValidPostcode1NotValid) + ", ";
        line += summary.get(sRSLTotalCount_Postcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sRSLPercentagePostcode0NotValidPostcode1Valid) + ", ";
        line += summary.get(sRSLTotalCount_Postcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sRSLPercentagePostcode0NotValidPostcode1NotValid) + ", ";
        line += summary.get(sRSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sRSLPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
        line += summary.get(sRSLTotalCount_Postcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        line += summary.get(sRSLPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
        return line;
    }

    public String getHeaderCompare2TimesTTChangeCouncil() {
        String header = "";
        header += sCouncilTotalCount_TTChangeClaimant + ", ";
        header += sCouncilPercentageOfHB_TTChangeClaimant + ", ";
        header += sCouncilTotalCount_Minus999TTToTT1 + ", ";
        header += sCouncilTotalCount_TT1ToMinus999TT + ", ";
        header += sCouncilTotalCount_TT1ToPrivateDeregulatedTTs + ", ";
        header += sCouncilPercentageOfTT1_TT1ToPrivateDeregulatedTTs + ", ";
        header += sCouncilTotalCount_TT1ToTT4 + ", ";
        header += sCouncilPercentageOfTT1_TT1ToTT4 + ", ";
        header += sCouncilTotalCount_TT4ToTT1 + ", ";
        header += sCouncilPercentageOfTT4_TT4ToTT1 + ", ";
        header += sCouncilTotalCount_PostcodeChangeWithinTT1 + ", ";
        header += sCouncilPercentageOfTT1_PostcodeChangeWithinTT1 + ", ";
        return header;
    }

    public String getHeaderCompare2TimesTTChangeRSL() {
        String header = "";
        header += sRSLTotalCount_TTChangeClaimant + ", ";
        header += sRSLPercentageOfHB_TTChangeClaimant + ", ";
        header += sRSLTotalCount_Minus999TTToTT4 + ", ";
        header += sRSLTotalCount_TT4ToMinus999TT + ", ";
        header += sRSLTotalCount_TT4ToPrivateDeregulatedTTs + ", ";
        header += sRSLPercentageOfTT4_TT4ToPrivateDeregulatedTTs + ", ";
        header += sRSLTotalCount_TT1ToTT4 + ", ";
        header += sRSLPercentageOfTT1_TT1ToTT4 + ", ";
        header += sRSLTotalCount_TT4ToTT1 + ", ";
        header += sRSLPercentageOfTT4_TT4ToTT1 + ", ";
        header += sRSLTotalCount_PostcodeChangeWithinTT4 + ", ";
        header += sRSLPercentageOfTT4_PostcodeChangeWithinTT4 + ", ";
        return header;
    }

    protected String getLineCompare2TimesTTChangeCouncil(HashMap<String, String> summary) {
        String line = "";
        line += summary.get(sCouncilTotalCount_TTChangeClaimant) + ", ";
        line += summary.get(sCouncilPercentageOfHB_TTChangeClaimant) + ", ";
        line += summary.get(sCouncilTotalCount_Minus999TTToTT1) + ", ";
        line += summary.get(sCouncilTotalCount_TT1ToMinus999TT) + ", ";
        line += summary.get(sCouncilTotalCount_TT1ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sCouncilPercentageOfTT1_TT1ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sCouncilTotalCount_TT1ToTT4) + ", ";
        line += summary.get(sCouncilPercentageOfTT1_TT1ToTT4) + ", ";
        line += summary.get(sCouncilTotalCount_TT4ToTT1) + ", ";
        line += summary.get(sCouncilPercentageOfTT4_TT4ToTT1) + ", ";
        line += summary.get(sCouncilTotalCount_PostcodeChangeWithinTT1) + ", ";
        line += summary.get(sCouncilPercentageOfTT1_PostcodeChangeWithinTT1) + ", ";
        return line;
    }

    protected String getLineCompare2TimesTTChangeRSL(HashMap<String, String> summary) {
        String line = "";
        line += summary.get(sRSLTotalCount_TTChangeClaimant) + ", ";
        line += summary.get(sRSLPercentageOfHB_TTChangeClaimant) + ", ";
        line += summary.get(sRSLTotalCount_Minus999TTToTT4) + ", ";
        line += summary.get(sRSLTotalCount_TT4ToMinus999TT) + ", ";
        line += summary.get(sRSLTotalCount_TT4ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sRSLPercentageOfTT4_TT4ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sRSLTotalCount_TT1ToTT4) + ", ";
        line += summary.get(sRSLPercentageOfTT1_TT1ToTT4) + ", ";
        line += summary.get(sRSLTotalCount_TT4ToTT1) + ", ";
        line += summary.get(sRSLPercentageOfTT4_TT4ToTT1) + ", ";
        line += summary.get(sRSLTotalCount_PostcodeChangeWithinTT4) + ", ";
        line += summary.get(sRSLPercentageOfTT4_PostcodeChangeWithinTT4) + ", ";
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
        ONSPDFiles = env.getDW_Postcode_Handler().getONSPDFiles();
        String name;
        name = "Compare2TimesTT";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header = "";
//        header += getHeaderCompare2TimesGeneric();
//        header += getHeaderCompare2TimesTTChange();
        header += getHeaderCompare2TimesTTChangeCouncil();
        header += getHeaderCompare2TimesTTChangeRSL();
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
            line = "";
            //line += getLineCompare2TimesGeneric(summary, ONSPDFiles);
//            line += getLineCompare2TimesTTChange(summary);
            line += getLineCompare2TimesTTChangeCouncil(summary);
            line += getLineCompare2TimesTTChangeRSL(summary);
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

//        header += sAllCount0 + ", ";
        header += sAllUOLinkedRecordCount0 + ", ";
        header += sCouncilFilename1 + ", ";
        header += sRSLFilename1 + ", ";
        header += sCouncilCount1 + ", ";
        header += sCouncilLinkedRecordCount1 + ", ";
        header += sRSLCount1 + ", ";
        header += sRSLLinkedRecordCount1 + ", ";

//        header += sAllCount1 + ", ";
        header += sAllUOAllCount1 + ", ";

        header += sAllUOLinkedRecordCount1 + ", ";
//        header += sAllCount0 + ", ";
//        header += sHBCount0 + ", ";
//        header += sCTBCount0 + ", ";
//        header += sAllCount1 + ", ";
//        header += sHBCount1 + ", ";
//        header += sCTBCount1 + ", ";
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
            line += tDW_SHBE_Handler.getYearMonthNumber(filename0) + ", ";
            month0 = tDW_SHBE_Handler.getMonth3(filename0);
            year0 = tDW_SHBE_Handler.getYear(filename0);
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
            line += tDW_SHBE_Handler.getYearMonthNumber(filename1) + ", ";
            month1 = tDW_SHBE_Handler.getMonth3(filename1);
            year1 = tDW_SHBE_Handler.getYear(filename1);
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
                    tDW_SHBE_Handler.getYM3(filename0));
            PostCodeLookupFile0Name = ONSPDFiles.get(PostCodeLookupDate0).getName();
        }
        line += PostCodeLookupDate0 + ", " + PostCodeLookupFile0Name + ", ";
        String PostCodeLookupDate1 = null;
        String PostCodeLookupFile1Name = null;
        if (filename1 != null) {
            PostCodeLookupDate1 = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                    tDW_SHBE_Handler.getYM3(filename1));
            PostCodeLookupFile1Name = ONSPDFiles.get(PostCodeLookupDate1).getName();
        }
        line += PostCodeLookupDate1 + ", " + PostCodeLookupFile1Name + ", ";
        line += summary.get(sCouncilFilename0) + ", ";
        line += summary.get(sRSLFilename0) + ", ";
        line += summary.get(sCouncilCount0) + ", ";
        line += summary.get(sCouncilLinkedRecordCount0) + ", ";
        line += summary.get(sRSLCount0) + ", ";
        line += summary.get(sRSLLinkedRecordCount0) + ", ";

//        line += summary.get(sAllCount0) + ", ";
        line += summary.get(sAllUOLinkedRecordCount0) + ", ";
        line += summary.get(sCouncilFilename1) + ", ";
        line += summary.get(sRSLFilename1) + ", ";
        line += summary.get(sCouncilCount1) + ", ";
        line += summary.get(sCouncilLinkedRecordCount1) + ", ";
        line += summary.get(sRSLCount1) + ", ";
        line += summary.get(sRSLLinkedRecordCount1) + ", ";

        line += summary.get(sAllUOAllCount1) + ", ";

//        line += summary.get(sAllCount1) + ", ";
        line += summary.get(sAllUOLinkedRecordCount1) + ", ";
//        line += summary.get(sAllCount0) + ", ";
//        line += summary.get(sHBCount0) + ", ";
//        line += summary.get(sCTBCount0) + ", ";
//        line += summary.get(sAllCount1) + ", ";
//        line += summary.get(sHBCount1) + ", ";
//        line += summary.get(sCTBCount1) + ", ";
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
        ONSPDFiles = env.getDW_Postcode_Handler().getONSPDFiles();
        String name;
        name = "Compare2TimesPostcode";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = getHeaderCompare2TimesGeneric();
//        header += getHeaderCompare2TimesPostcodeChange();
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
            line = getLineCompare2TimesGeneric(summary, ONSPDFiles);
//            line += getLineCompare2TimesPostcodeChange(summary);
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
        ONSPDFiles = env.getDW_Postcode_Handler().getONSPDFiles();
        String name;
        name = "SingleTimeGenericCounts";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric();
        header += "PostCodeLookupDate, ";
        header += "PostCodeLookupFile, ";
        header += sHBTotalHouseholdSize + ", ";
//Not correct at present, wrong denominator        header += sHBAverageHouseholdSize + ", ";
        header += sAllTotalHouseholdSizeByTT[1] + ", ";
        header += sAllAverageHouseholdSizeByTT[1] + ", ";
        header += sAllTotalHouseholdSizeByTT[4] + ", ";
        header += sAllAverageHouseholdSizeByTT[4] + ", ";
        header += sAllTotalCount_PostcodeValidFormat + ", ";
        header += sAllTotalCount_PostcodeValid + ", ";
        header += sHBTotalCount_PostcodeValidFormat + ", ";
        header += sHBTotalCount_PostcodeValid + ", ";
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
            line += filename1 + ", ";
            line += getLineSingleTimeGeneric(key, summary);
            line += getPostcodeLookupDateAndFilenameLinePart(filename1, ONSPDFiles);
            line += summary.get(sHBTotalHouseholdSize) + ", ";
//            line += summary.get(sHBAverageHouseholdSize) + ", ";
            line += summary.get(sAllTotalHouseholdSizeByTT[1]) + ", ";
            line += summary.get(sAllAverageHouseholdSizeByTT[1]) + ", ";
            line += summary.get(sAllTotalHouseholdSizeByTT[4]) + ", ";
            line += summary.get(sAllAverageHouseholdSizeByTT[4]) + ", ";
            line += summary.get(sAllTotalCount_PostcodeValidFormat) + ", ";
            line += summary.get(sAllTotalCount_PostcodeValid) + ", ";
            line += summary.get(sHBTotalCount_PostcodeValidFormat) + ", ";
            line += summary.get(sHBTotalCount_PostcodeValid) + ", ";
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
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
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
        header += sHBTotalWeeklyEligibleRentAmount + ", ";
        header += sHBTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sHBTotalCount_WeeklyEligibleRentAmountZero + ", ";
        header += sHBAverageWeeklyEligibleRentAmount + ", ";
        // WeeklyEligibleCouncilTaxAmount
        header += sHBTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sHBTotalCount_WeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sHBAverageWeeklyEligibleCouncilTaxAmount + ", ";
        // ContractualRentAmount
        header += sHBTotalContractualRentAmount + ", ";
        header += sHBTotalCountContractualRentAmountNonZeroCount + ", ";
        header += sHBTotalCountContractualRentAmountZeroCount + ", ";
        header += sHBAverageContractualRentAmount + ", ";
        // WeeklyAdditionalDiscretionaryPayment
        header += sHBTotalWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sHBAverageWeeklyAdditionalDiscretionaryPayment + ", ";
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        header += sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        // Council
        header += sCouncilTotalWeeklyHBEntitlement + ", ";
        header += sCouncilTotalCount_WeeklyHBEntitlementNonZero + ", ";
        header += sCouncilTotalCount_WeeklyHBEntitlementZero + ", ";
        header += sCouncilAverageWeeklyHBEntitlement + ", ";
        // WeeklyEligibleRentAmount
        header += sCouncilTotalWeeklyEligibleRentAmount + ", ";
        header += sCouncilTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sCouncilTotalCount_WeeklyEligibleRentAmountZero + ", ";
        header += sCouncilAverageWeeklyEligibleRentAmount + ", ";
        // WeeklyEligibleCouncilTaxAmount
        header += sCouncilTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sCouncilTotalCount_WeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sCouncilTotalCount_WeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sCouncilAverageWeeklyEligibleCouncilTaxAmount + ", ";
        // ContractualRentAmount
        header += sCouncilTotalContractualRentAmount + ", ";
        header += sCouncilTotalCountContractualRentAmountNonZeroCount + ", ";
        header += sCouncilTotalCountContractualRentAmountZeroCount + ", ";
        header += sCouncilAverageContractualRentAmount + ", ";
        // WeeklyAdditionalDiscretionaryPayment
        header += sCouncilTotalWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sCouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sCouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sCouncilAverageWeeklyAdditionalDiscretionaryPayment + ", ";
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        header += sCouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sCouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sCouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sCouncilAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        // RSL
        header += sRSLTotalWeeklyHBEntitlement + ", ";
        header += sRSLTotalCount_WeeklyHBEntitlementNonZero + ", ";
        header += sRSLTotalCount_WeeklyHBEntitlementZero + ", ";
        header += sRSLAverageWeeklyHBEntitlement + ", ";
        // WeeklyEligibleRentAmount
        header += sRSLTotalWeeklyEligibleRentAmount + ", ";
        header += sRSLTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sRSLTotalCount_WeeklyEligibleRentAmountZero + ", ";
        header += sRSLAverageWeeklyEligibleRentAmount + ", ";
        // WeeklyEligibleCouncilTaxAmount
        header += sRSLTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sRSLTotalCount_WeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sRSLTotalCount_WeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sRSLAverageWeeklyEligibleCouncilTaxAmount + ", ";
        // ContractualRentAmount
        header += sRSLTotalContractualRentAmount + ", ";
        header += sRSLTotalCountContractualRentAmountNonZeroCount + ", ";
        header += sRSLTotalCountContractualRentAmountZeroCount + ", ";
        header += sRSLAverageContractualRentAmount + ", ";
        // WeeklyAdditionalDiscretionaryPayment
        header += sRSLTotalWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sRSLTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sRSLTotalCount_WeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sRSLAverageWeeklyAdditionalDiscretionaryPayment + ", ";
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        header += sRSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sRSLTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sRSLTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sRSLAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
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
            line += filename1 + ", ";
            line += getLineSingleTimeGeneric(key, summary);
            // All UO
            line += summary.get(sTotalWeeklyHBEntitlement) + ", ";
            line += summary.get(sTotalCount_WeeklyHBEntitlementNonZero) + ", ";
            line += summary.get(sTotalCount_WeeklyHBEntitlementZero) + ", ";
            line += summary.get(sAverageWeeklyHBEntitlement) + ", ";
            // WeeklyEligibleRentAmount
            line += summary.get(sHBTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sHBTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sHBTotalCount_WeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sHBAverageWeeklyEligibleRentAmount) + ", ";
            // WeeklyEligibleCouncilTaxAmount
            line += summary.get(sHBTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sHBTotalCount_WeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sHBTotalCount_WeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sHBAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            // ContractualRentAmount
            line += summary.get(sHBTotalContractualRentAmount) + ", ";
            line += summary.get(sHBTotalCountContractualRentAmountNonZeroCount) + ", ";
            line += summary.get(sHBTotalCountContractualRentAmountZeroCount) + ", ";
            line += summary.get(sHBAverageContractualRentAmount) + ", ";
            // WeeklyAdditionalDiscretionaryPayment
            line += summary.get(sHBTotalWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sHBAverageWeeklyAdditionalDiscretionaryPayment) + ", ";
            // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
            line += summary.get(sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sHBTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            // Council
            line += summary.get(sCouncilTotalWeeklyHBEntitlement) + ", ";
            line += summary.get(sCouncilTotalCount_WeeklyHBEntitlementNonZero) + ", ";
            line += summary.get(sCouncilTotalCount_WeeklyHBEntitlementZero) + ", ";
            line += summary.get(sCouncilAverageWeeklyHBEntitlement) + ", ";
            // WeeklyEligibleRentAmount
            line += summary.get(sCouncilTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sCouncilTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sCouncilTotalCount_WeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sCouncilAverageWeeklyEligibleRentAmount) + ", ";
            // WeeklyEligibleCouncilTaxAmount
            line += summary.get(sCouncilTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sCouncilTotalCount_WeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sCouncilTotalCount_WeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sCouncilAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            // ContractualRentAmount
            line += summary.get(sCouncilTotalContractualRentAmount) + ", ";
            line += summary.get(sCouncilTotalCountContractualRentAmountNonZeroCount) + ", ";
            line += summary.get(sCouncilTotalCountContractualRentAmountZeroCount) + ", ";
            line += summary.get(sCouncilAverageContractualRentAmount) + ", ";
            // WeeklyAdditionalDiscretionaryPayment
            line += summary.get(sCouncilTotalWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sCouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sCouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sCouncilAverageWeeklyAdditionalDiscretionaryPayment) + ", ";
            // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
            line += summary.get(sCouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sCouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sCouncilTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sCouncilAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            // RSL
            line += summary.get(sRSLTotalWeeklyHBEntitlement) + ", ";
            line += summary.get(sRSLTotalCount_WeeklyHBEntitlementNonZero) + ", ";
            line += summary.get(sRSLTotalCount_WeeklyHBEntitlementZero) + ", ";
            line += summary.get(sRSLAverageWeeklyHBEntitlement) + ", ";
            // WeeklyEligibleRentAmount
            line += summary.get(sRSLTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sRSLTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sRSLTotalCount_WeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sRSLAverageWeeklyEligibleRentAmount) + ", ";
            // WeeklyEligibleCouncilTaxAmount
            line += summary.get(sRSLTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sRSLTotalCount_WeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sRSLTotalCount_WeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sRSLAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            // ContractualRentAmount
            line += summary.get(sRSLTotalContractualRentAmount) + ", ";
            line += summary.get(sRSLTotalCountContractualRentAmountNonZeroCount) + ", ";
            line += summary.get(sRSLTotalCountContractualRentAmountZeroCount) + ", ";
            line += summary.get(sRSLAverageContractualRentAmount) + ", ";
            // WeeklyAdditionalDiscretionaryPayment
            line += summary.get(sRSLTotalWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sRSLTotalCount_WeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sRSLTotalCount_WeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sRSLAverageWeeklyAdditionalDiscretionaryPayment) + ", ";
            // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
            line += summary.get(sRSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sRSLTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sRSLTotalCount_WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sRSLAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
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
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
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
        header += sCouncilTotalCount_ClaimantsEmployed + ", ";
        header += sCouncilPercentageOfHB_ClaimantsEmployed + ", ";
        header += sCouncilTotalCountClaimantsSelfEmployed + ", ";
        header += sCouncilPercentageOfHB_ClaimantsSelfEmployed + ", ";
        header += sCouncilTotalCountClaimantsStudents + ", ";
        header += sCouncilPercentageOfHB_ClaimantsStudents + ", ";
        // RSL
        header += sRSLTotalCount_ClaimantsEmployed + ", ";
        header += sRSLPercentageOfHB_ClaimantsEmployed + ", ";
        header += sRSLTotalCountClaimantsSelfEmployed + ", ";
        header += sRSLPercentageOfHB_ClaimantsSelfEmployed + ", ";
        header += sRSLTotalCountClaimantsStudents + ", ";
        header += sRSLPercentageOfHB_ClaimantsStudents + ", ";
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
            line += summary.get(sCouncilTotalCount_ClaimantsEmployed) + ", ";
            line += summary.get(sCouncilPercentageOfHB_ClaimantsEmployed) + ", ";
            line += summary.get(sCouncilTotalCountClaimantsSelfEmployed) + ", ";
            line += summary.get(sCouncilPercentageOfHB_ClaimantsSelfEmployed) + ", ";
            line += summary.get(sCouncilTotalCountClaimantsStudents) + ", ";
            line += summary.get(sCouncilPercentageOfHB_ClaimantsStudents) + ", ";
            // RSL
            line += summary.get(sRSLTotalCount_ClaimantsEmployed) + ", ";
            line += summary.get(sRSLPercentageOfHB_ClaimantsEmployed) + ", ";
            line += summary.get(sRSLTotalCountClaimantsSelfEmployed) + ", ";
            line += summary.get(sRSLPercentageOfHB_ClaimantsSelfEmployed) + ", ";
            line += summary.get(sRSLTotalCountClaimantsStudents) + ", ";
            line += summary.get(sRSLPercentageOfHB_ClaimantsStudents) + ", ";
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
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric();
//        // All UO
//        // All
//        header += sAllUO + sAllTotalIncome + ", ";
//        header += sAllUO + sAllTotalCount_IncomeNonZero + ", ";
//        header += sAllUO + sAllTotalCount_IncomeZero + ", ";
//        header += sAllUO + sAllAverageIncome + ", ";
//        header += sAllUO + sAllTotalWeeklyEligibleRentAmount + ", ";
//        header += sAllUO + sAllTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
//        header += sAllUO + sAllAverageWeeklyEligibleRentAmount + ", ";
//        for (int i = 1; i < nTT; i++) {
//            header += sAllUO + sAllTotalIncomeTT[i] + ", ";
//            header += sAllUO + sAllTotalCount_IncomeNonZeroTT[i] + ", ";
//            header += sAllUO + sAllTotalCount_IncomeZeroTT[i] + ", ";
//            header += sAllUO + sAllAverageIncomeTT[i] + ", ";
//            header += sAllUO + sAllTotalWeeklyEligibleRentAmountTT[i] + ", ";
//            header += sAllUO + sAllTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] + ", ";
//            header += sAllUO + sAllAverageWeeklyEligibleRentAmountTT[i] + ", ";
//        }
        // Council
        // All
        header += sCouncilHBTotalIncomeTT1 + ", ";
        header += sCouncilHBTotalCount_IncomeNonZeroTT1 + ", ";
        header += sCouncilHBTotalCount_IncomeZeroTT1 + ", ";
        header += sCouncilHBAverageIncomeTT1 + ", ";
        header += sCouncilTotalWeeklyEligibleRentAmount + ", ";
        header += sCouncilTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sCouncilAverageWeeklyEligibleRentAmount + ", ";
        header += sCouncilHBTotalIncomeTT1 + ", ";
        header += sCouncilHBTotalCount_IncomeNonZeroTT1 + ", ";
        header += sCouncilHBTotalCount_IncomeZeroTT1 + ", ";
        header += sCouncilHBAverageIncomeTT1 + ", ";
        header += sCouncilTotalWeeklyEligibleRentAmountTT1 + ", ";
        header += sCouncilTotalCount_WeeklyEligibleRentAmountNonZeroTT1 + ", ";
        header += sCouncilAverageWeeklyEligibleRentAmountTT1 + ", ";
        // RSL
        // All
        header += sRSLHBTotalIncomeTT4 + ", ";
        header += sRSLHBTotalCount_IncomeNonZeroTT4 + ", ";
        header += sRSLHBTotalCount_IncomeZeroTT4 + ", ";
        header += sRSLHBAverageIncomeTT4 + ", ";
        header += sRSLTotalWeeklyEligibleRentAmount + ", ";
        header += sRSLTotalCount_WeeklyEligibleRentAmountNonZero + ", ";
        header += sRSLAverageWeeklyEligibleRentAmount + ", ";
        header += sRSLHBTotalIncomeTT4 + ", ";
        header += sRSLHBTotalCount_IncomeNonZeroTT4 + ", ";
        header += sRSLHBTotalCount_IncomeZeroTT4 + ", ";
        header += sRSLHBAverageIncomeTT4 + ", ";
        header += sRSLTotalWeeklyEligibleRentAmountTT4 + ", ";
        header += sRSLTotalCount_WeeklyEligibleRentAmountNonZeroTT4 + ", ";
        header += sRSLAverageWeeklyEligibleRentAmountTT4 + ", ";
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
            line += filename1 + ", ";
            line += getLineSingleTimeGeneric(key, summary);
            // All UO
//            // All
//            line += summary.get(sAllUO + sAllTotalIncome) + ", ";
//            line += summary.get(sAllUO + sAllTotalCount_IncomeNonZero) + ", ";
//            line += summary.get(sAllUO + sAllTotalCount_IncomeZero) + ", ";
//            line += summary.get(sAllUO + sAllAverageIncome) + ", ";
//            line += summary.get(sAllUO + sAllTotalWeeklyEligibleRentAmount) + ", ";
//            line += summary.get(sAllUO + sAllTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
//            line += summary.get(sAllUO + sAllAverageWeeklyEligibleRentAmount) + ", ";
//            for (int i = 1; i < nTT; i++) {
//                line += summary.get(sTotalIncomeTT[i]) + ", ";
//                line += summary.get(sTotalCount_IncomeNonZeroTT[i]) + ", ";
//                line += summary.get(sTotalCount_IncomeZeroTT[i]) + ", ";
//                line += summary.get(sAverageIncomeTT[i]) + ", ";
//                line += summary.get(sTotalWeeklyEligibleRentAmountTT[i]) + ", ";
//                line += summary.get(sTotalCount_WeeklyEligibleRentAmountNonZeroTT[i]) + ", ";
//                line += summary.get(sAverageWeeklyEligibleRentAmountTT[i]) + ", ";
//            }
            // Council
            // All
            line += summary.get(sCouncilHBTotalIncomeTT1) + ", ";
            line += summary.get(sCouncilHBTotalCount_IncomeNonZeroTT1) + ", ";
            line += summary.get(sCouncilHBTotalCount_IncomeZeroTT1) + ", ";
            line += summary.get(sCouncilHBAverageIncomeTT1) + ", ";
            line += summary.get(sCouncilTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sCouncilTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sCouncilAverageWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sCouncilHBTotalIncomeTT1) + ", ";
            line += summary.get(sCouncilHBTotalCount_IncomeNonZeroTT1) + ", ";
            line += summary.get(sCouncilHBTotalCount_IncomeZeroTT1) + ", ";
            line += summary.get(sCouncilHBAverageIncomeTT1) + ", ";
            line += summary.get(sCouncilTotalWeeklyEligibleRentAmountTT1) + ", ";
            line += summary.get(sCouncilTotalCount_WeeklyEligibleRentAmountNonZeroTT1) + ", ";
            line += summary.get(sCouncilAverageWeeklyEligibleRentAmountTT1) + ", ";
            // RSL
            // All
            line += summary.get(sRSLHBTotalIncomeTT4) + ", ";
            line += summary.get(sRSLHBTotalCount_IncomeNonZeroTT4) + ", ";
            line += summary.get(sRSLHBTotalCount_IncomeZeroTT4) + ", ";
            line += summary.get(sRSLHBAverageIncomeTT4) + ", ";
            line += summary.get(sRSLTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sRSLTotalCount_WeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sRSLAverageWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sRSLHBTotalIncomeTT4) + ", ";
            line += summary.get(sRSLHBTotalCount_IncomeNonZeroTT4) + ", ";
            line += summary.get(sRSLHBTotalCount_IncomeZeroTT4) + ", ";
            line += summary.get(sRSLHBAverageIncomeTT4) + ", ";
            line += summary.get(sRSLTotalWeeklyEligibleRentAmountTT4) + ", ";
            line += summary.get(sRSLTotalCount_WeeklyEligibleRentAmountNonZeroTT4) + ", ";
            line += summary.get(sRSLAverageWeeklyEligibleRentAmountTT4) + ", ";
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
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header = "year-month, ";
        header += sCouncilFilename1 + ", ";
        header += sCouncilCount1 + ", ";
        header += sCouncilLinkedRecordCount1 + ", ";
        header += "Month Year, ";
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
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + ", ";
            line += key + ", ";
            line += summary.get(sCouncilFilename1) + ", ";
            line += summary.get(sCouncilCount1) + ", ";
            line += summary.get(sCouncilLinkedRecordCount1) + ", ";
            String[] split;
            split = key.split("-");
            line += Generic_Time.getMonth3Letters(split[1]);
            line += " " + split[0] + ", ";
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
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric();
        // All UO
        for (int i = 1; i < nEG; i++) {
            header += sAllTotalCount_EthnicGroupClaimant[i] + ", ";
            header += sAllPercentageOfAll_EthnicGroupClaimant[i] + ", ";
            header += sAllTotalCount_EthnicGroupSocialTTClaimant[i] + ", ";
            header += sAllPercentageOfSocialTT_EthnicGroupSocialTTClaimant[i] + ", ";
        }
//        for (int i = 1; i < nEG; i++) {
//            header += sHBTotalCount_EthnicGroupClaimant[i] + ", ";
//            header += sHBPercentageOfHB_EthnicGroupClaimant[i] + ", ";
//        }
//        for (int i = 1; i < nEG; i++) {
//            header += sCTBTotalCount_EthnicGroupClaimant[i] + ", ";
//            header += sCTBPercentageOfCTB_EthnicGroupClaimant[i] + ", ";
//        }
        // Council
        for (int i = 1; i < nEG; i++) {
            header += sCouncilTotalCount_EthnicGroupClaimant[i] + ", ";
            header += sCouncilPercentageOfHB_EthnicGroupClaimant[i] + ", ";
        }
        // RSL
        for (int i = 1; i < nEG; i++) {
            header += sRSLTotalCount_EthnicGroupClaimant[i] + ", ";
            header += sRSLPercentageOfHB_EthnicGroupClaimant[i] + ", ";
        }
        for (int i = 1; i < nEG; i++) {
//            for (int j = 1; j < nTT; j++) {
//                header += sAllTotalCount_EthnicGroupClaimantByTT[i][j] + ", ";
//                header += sAllPercentageOfTT_EthnicGroupClaimantByTT[i][j] + ", ";
//                header += sAllPercentageOfEthnicGroup_EthnicGroupClaimantByTT[i][j] + ", ";
//            }
            header += sAllTotalCount_EthnicGroupClaimantByTT[i][1] + ", ";
            header += sAllPercentageOfTT_EthnicGroupClaimantByTT[i][1] + ", ";
            header += sAllPercentageOfEthnicGroup_EthnicGroupClaimantByTT[i][1] + ", ";
            header += sAllTotalCount_EthnicGroupClaimantByTT[i][4] + ", ";
            header += sAllPercentageOfTT_EthnicGroupClaimantByTT[i][4] + ", ";
            header += sAllPercentageOfEthnicGroup_EthnicGroupClaimantByTT[i][4] + ", ";
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
            line += filename1 + ", ";
            line += getLineSingleTimeGeneric(key, summary);
            // All UO
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sAllTotalCount_EthnicGroupSocialTTClaimant[i]) + ", ";
                line += summary.get(sAllPercentageOfSocialTT_EthnicGroupSocialTTClaimant[i]) + ", ";
                line += summary.get(sAllTotalCount_EthnicGroupPrivateDeregulatedTTClaimant[i]) + ", ";
                line += summary.get(sAllPercentageOfPrivateDeregulatedTT_EthnicGroupPrivateDeregulatedTTClaimant[i]) + ", ";
            }
//            for (int i = 1; i < nEG; i++) {
//                line += summary.get(sHBTotalCount_EthnicGroupClaimant[i]) + ", ";
//                line += summary.get(sHBPercentageOfHB_EthnicGroupClaimant[i]) + ", ";
//            }
//            for (int i = 1; i < nEG; i++) {
//                line += summary.get(sCTBTotalCount_EthnicGroupClaimant[i]) + ", ";
//                line += summary.get(sCTBPercentageOfCTB_EthnicGroupClaimant[i]) + ", ";
//            }
            // Council
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sCouncilTotalCount_EthnicGroupClaimant[i]) + ", ";
                line += summary.get(sCouncilPercentageOfHB_EthnicGroupClaimant[i]) + ", ";
            }
            // RSL
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sRSLTotalCount_EthnicGroupClaimant[i]) + ", ";
                line += summary.get(sRSLPercentageOfHB_EthnicGroupClaimant[i]) + ", ";
            }
            for (int i = 1; i < nEG; i++) {
//                for (int j = 1; j < nTT; j++) {
//                    line += summary.get(sAllTotalCount_EthnicGroupClaimantByTT[i][j]) + ", ";
//                    line += summary.get(sAllPercentageOfTT_EthnicGroupClaimantByTT[i][j]) + ", ";
//                    line += summary.get(sAllPercentageOfEthnicGroup_EthnicGroupClaimantByTT[i][j]) + ", ";
//                }
                line += summary.get(sAllTotalCount_EthnicGroupClaimantByTT[i][1]) + ", ";
                line += summary.get(sAllPercentageOfTT_EthnicGroupClaimantByTT[i][1]) + ", ";
                line += summary.get(sAllPercentageOfEthnicGroup_EthnicGroupClaimantByTT[i][1]) + ", ";
                line += summary.get(sAllTotalCount_EthnicGroupClaimantByTT[i][4]) + ", ";
                line += summary.get(sAllPercentageOfTT_EthnicGroupClaimantByTT[i][4]) + ", ";
                line += summary.get(sAllPercentageOfEthnicGroup_EthnicGroupClaimantByTT[i][4]) + ", ";
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
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric();
//        // All UO
//        header += sTotalCount_SocialTTsClaimant + ", ";
//        header += sPercentageOfAll_SocialTTsClaimant + ", ";
//        header += sPercentageOfHB_SocialTTsClaimant + ", ";
//        //header += sTotalCount_PrivateDeregulatedTTsClaimant + ", ";
//        //header += sPercentageOfAll_PrivateDeregulatedTTsClaimant + ", ";
//        //header += sPercentageOfHB_PrivateDeregulatedTTsClaimant + ", ";
//        for (int i = 1; i < nTT; i++) {
//            header += sTotalCount_ClaimantTT[i] + ", ";
//            header += sPercentageOfAll_ClaimantTT[i] + ", ";
//            if (i == 5 || i == 7) {
//                header += sPercentageOfCTB_ClaimantTT[i] + ", ";
//            } else {
//                header += sPercentageOfHB_ClaimantTT[i] + ", ";
//            }
//        }
//        header += sAllTotalCount_LHACases + ", ";
//        header += sAllPercentageOfAll_LHACases + ", ";
//        header += sHBTotalCount_LHACases + ", ";
//        header += sHBPercentageOfHB_LHACases + ", ";
//        //header += sCTBTotalCount_LHACases + ", ";
//        //header += sCTBPercentageOfCTB_LHACases + ", ";
        // Council
//        header += sCouncilTotalCount_SocialTTsClaimant + ", ";
//        header += sCouncilPercentageOfAll_SocialTTsClaimant + ", ";
//        header += sCouncilPercentageOfHB_SocialTTsClaimant + ", ";
        //header += sCouncilTotalCount_PrivateDeregulatedTTsClaimant + ", ";
        //header += sCouncilPercentageOfAll_PrivateDeregulatedTTsClaimant + ", ";
        //header += sCouncilPercentageOfHB_PrivateDeregulatedTTsClaimant + ", ";
        for (int i = 1; i < nTT; i++) {
            header += sCouncilTotalCount_ClaimantTT[i] + ", ";
            if (i == 1) {
                header += sCouncilPercentageOfLinkedRecords_ClaimantTT1 + ", ";
            }
        }
        header += sCouncilTotalCount_LHACases + ", ";
//        header += sCouncilAllPercentageOfAll_LHACases + ", ";
//        header += sCouncilTotalCount_LHACases + ", ";
//        header += sCouncilPercentageOfHB_LHACases + ", ";
        //header += sCouncilCTBTotalCount_LHACases + ", ";
        //header += sCouncilCTBPercentageOfCTB_LHACases + ", ";
        // RSL
//        header += sRSLTotalCount_SocialTTsClaimant + ", ";
//        header += sRSLPercentageOfAll_SocialTTsClaimant + ", ";
//        header += sRSLPercentageOfHB_SocialTTsClaimant + ", ";
        //header += sRSLTotalCount_PrivateDeregulatedTTsClaimant + ", ";
        //header += sRSLPercentageOfAll_PrivateDeregulatedTTsClaimant + ", ";
        //header += sRSLPercentageOfHB_PrivateDeregulatedTTsClaimant + ", ";
        for (int i = 1; i < nTT; i++) {
            header += sRSLTotalCount_ClaimantTT[i] + ", ";
            if (i == 4) {
                header += sRSLPercentageOfLinkedRecords_ClaimantTT4 + ", ";
            }
        }
        header += sRSLTotalCount_LHACases + ", ";
//        header += sRSLPercentageOfAll_LHACases + ", ";
//        header += sRSLTotalCount_LHACases + ", ";
//        header += sRSLPercentageOfHB_LHACases + ", ";
        //header += sRSLCTBTotalCount_LHACases + ", ";
        //header += sRSLCTBPercentageOfCTB_LHACases + ", ";
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
            line += filename1 + ", ";
            line += getLineSingleTimeGeneric(key, summary);
//            // All UO
//            line += summary.get(sTotalCount_SocialTTsClaimant) + ", ";
//            line += summary.get(sPercentageOfAll_SocialTTsClaimant) + ", ";
//            line += summary.get(sPercentageOfHB_SocialTTsClaimant) + ", ";
//            //line += summary.get(sTotalCount_PrivateDeregulatedTTsClaimant) + ", ";
//            //line += summary.get(sPercentageOfAll_PrivateDeregulatedTTsClaimant) + ", ";
//            //line += summary.get(sPercentageOfHB_PrivateDeregulatedTTsClaimant) + ", ";
//            for (int i = 1; i < nTT; i++) {
//                line += summary.get(sTotalCount_ClaimantTT[i]) + ", ";
//                line += summary.get(sPercentageOfAll_ClaimantTT[i]) + ", ";
//                if (i == 5 || i == 7) {
//                    line += summary.get(sPercentageOfCTB_ClaimantTT[i]) + ", ";
//                } else {
//                    line += summary.get(sPercentageOfHB_ClaimantTT[i]) + ", ";
//                }
//            }
//            line += summary.get(sAllTotalCount_LHACases) + ", ";
//            line += summary.get(sAllPercentageOfAll_LHACases) + ", ";
//            line += summary.get(sHBTotalCount_LHACases) + ", ";
//            line += summary.get(sHBPercentageOfHB_LHACases) + ", ";
//            //line += summary.get(sCTBTotalCount_LHACases) + ", ";
//            //line += summary.get(sCTBPercentageOfCTB_LHACases) + ", ";
            // Council
//            line += summary.get(sCouncilTotalCount_SocialTTsClaimant) + ", ";
//            line += summary.get(sCouncilPercentageOfAll_SocialTTsClaimant) + ", ";
//            line += summary.get(sCouncilPercentageOfHB_SocialTTsClaimant) + ", ";
            //line += summary.get(sCouncilTotalCount_PrivateDeregulatedTTsClaimant) + ", ";
            //line += summary.get(sCouncilPercentageOfAll_PrivateDeregulatedTTsClaimant) + ", ";
            //line += summary.get(sCouncilPercentageOfHB_PrivateDeregulatedTTsClaimant) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sCouncilTotalCount_ClaimantTT[i]) + ", ";
                if (i == 1) {
                    line += summary.get(sCouncilPercentageOfLinkedRecords_ClaimantTT1) + ", ";
                }
            }
            line += summary.get(sCouncilTotalCount_LHACases) + ", ";
//            line += summary.get(sCouncilAllPercentageOfAll_LHACases) + ", ";
//            line += summary.get(sCouncilTotalCount_LHACases) + ", ";
//            line += summary.get(sCouncilPercentageOfHB_LHACases) + ", ";
            //line += summary.get(sCouncilCTBTotalCount_LHACases) + ", ";
            //line += summary.get(sCouncilCTBPercentageOfCTB_LHACases) + ", ";
            // RSL
//            line += summary.get(sRSLTotalCount_SocialTTsClaimant) + ", ";
//            line += summary.get(sRSLPercentageOfAll_SocialTTsClaimant) + ", ";
//            line += summary.get(sRSLPercentageOfHB_SocialTTsClaimant) + ", ";
            //line += summary.get(sRSLTotalCount_PrivateDeregulatedTTsClaimant) + ", ";
            //line += summary.get(sRSLPercentageOfAll_PrivateDeregulatedTTsClaimant) + ", ";
            //line += summary.get(sRSLPercentageOfHB_PrivateDeregulatedTTsClaimant) + ", ";

            for (int i = 1; i < nTT; i++) {
                line += summary.get(sRSLTotalCount_ClaimantTT[i]) + ", ";
                if (i == 4) {
                    line += summary.get(sRSLPercentageOfLinkedRecords_ClaimantTT4) + ", ";
                }
            }
            line += summary.get(sRSLTotalCount_LHACases) + ", ";
//            line += summary.get(sRSLPercentageOfAll_LHACases) + ", ";
//            line += summary.get(sRSLTotalCount_LHACases) + ", ";
//            line += summary.get(sRSLPercentageOfHB_LHACases) + ", ";
            //line += summary.get(sRSLCTBTotalCount_LHACases) + ", ";
            //line += summary.get(sRSLCTBPercentageOfCTB_LHACases) + ", ";
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
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric();
        // All UO
        for (int i = 1; i < nPSI; i++) {
            header += sHBTotalCount_PSI[i] + ", ";
            header += sHBPercentageOfHB_PSI[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            for (int j = 1; j < nTT; j++) {
                if (j == 1 || j == 4) {
                    header += sTotalCount_PSIByTT[i][j] + ", ";
                    header += sPercentageOfHB_PSIByTT[i][j] + ", ";
                    header += sPercentageOfTT_PSIByTT[i][j] + ", ";
                }
            }
        }
        // Council
        for (int i = 1; i < nPSI; i++) {
            header += sCouncilTotalCount_PSI[i] + ", ";
            header += sCouncilPercentageOfHB_PSI[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            header += sCouncilTotalCount_PSIByTT1[i] + ", ";
            header += sCouncilPercentageOfHB_PSIByTT1[i] + ", ";
            header += sCouncilPercentageOfTT_PSIByTT1[i] + ", ";
        }
        // RSL
        for (int i = 1; i < nPSI; i++) {
            header += sRSLTotalCount_PSI[i] + ", ";
            header += sRSLPercentageOfHB_PSI[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            header += sRSLTotalCount_PSIByTT4[i] + ", ";
            header += sRSLPercentageOfHB_PSIByTT4[i] + ", ";
            header += sRSLPercentageOfTT_PSIByTT4[i] + ", ";
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
            line += filename1 + ", ";
            line += getLineSingleTimeGeneric(key, summary);
            // All UO
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sHBTotalCount_PSI[i]) + ", ";
                line += summary.get(sHBPercentageOfHB_PSI[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                for (int j = 1; j < nTT; j++) {
                    if (j == 1 || j == 4) {
                        line += summary.get(sTotalCount_PSIByTT[i][j]) + ", ";
                        line += summary.get(sPercentageOfHB_PSIByTT[i][j]) + ", ";
                        line += summary.get(sPercentageOfTT_PSIByTT[i][j]) + ", ";
                    }
                }
            }
            // Council
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sCouncilTotalCount_PSI[i]) + ", ";
                line += summary.get(sCouncilPercentageOfHB_PSI[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sCouncilTotalCount_PSIByTT1[i]) + ", ";
                line += summary.get(sCouncilPercentageOfHB_PSIByTT1[i]) + ", ";
                line += summary.get(sCouncilPercentageOfTT_PSIByTT1[i]) + ", ";
            }
            // RSL
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sRSLTotalCount_PSI[i]) + ", ";
                line += summary.get(sRSLPercentageOfHB_PSI[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sRSLTotalCount_PSIByTT4[i]) + ", ";
                line += summary.get(sRSLPercentageOfHB_PSIByTT4[i]) + ", ";
                line += summary.get(sRSLPercentageOfTT_PSIByTT4[i]) + ", ";
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
        int i;
        String name;
        name = "SingleTimeDisability";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable, paymentType, includeKey, underOccupancy);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric();
        // All UO
        // SocialTTs
        // DisabilityAward
        header += sTotalCount_DisabilityAwardSocialTTs + ", ";
        header += sPercentageOfSocialTTs_DisabilityAwardSocialTTs + ", ";
        // DisabilityPremiumAward
        header += sTotalCount_DisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs + ", ";
        // SevereDisabilityPremiumAward
        header += sTotalCount_SevereDisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs + ", ";
        // DisabledChildPremiumAward
        header += sTotalCount_DisabledChildPremiumAwardSocialTTs + ", ";
        header += sPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs + ", ";
        // EnhancedDisabilityPremiumAward
        header += sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs + ", ";
        header += sPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs + ", ";
//        for (i = 1; i < nTT; i++) {
//            if (i == 1 || i == 4) {
//            // DisabilityAward
//            header += sTotalCount_DisabilityAwardByTT[i] + ", ";
//            header += sPercentageOfAll_DisabilityAwardByTT[i] + ", ";
//                header += sPercentageOfHB_DisabilityAwardByTT[i] + ", ";
//            header += sPercentageOfTT_DisabilityAwardByTT[i] + ", ";
//            // DisabilityPremiumAward
//            header += sTotalCount_DisabilityPremiumAwardByTT[i] + ", ";
//            header += sPercentageOfAll_DisabilityPremiumAwardByTT[i] + ", ";
//                header += sPercentageOfHB_DisabilityPremiumAwardByTT[i] + ", ";
//            header += sPercentageOfTT_DisabilityPremiumAwardByTT[i] + ", ";
//            // SevereDisabilityPremiumAward
//            header += sTotalCount_SevereDisabilityPremiumAwardByTT[i] + ", ";
//            header += sPercentageOfAll_SevereDisabilityPremiumAwardByTT[i] + ", ";
//                header += sPercentageOfHB_SevereDisabilityPremiumAwardByTT[i] + ", ";
//            header += sPercentageOfTT_SevereDisabilityPremiumAwardByTT[i] + ", ";
//            // DisabledChildPremiumAward
//            header += sTotalCount_DisabledChildPremiumAwardByTT[i] + ", ";
//            header += sPercentageOfAll_DisabledChildPremiumAwardByTT[i] + ", ";
//                header += sPercentageOfHB_DisabledChildPremiumAwardByTT[i] + ", ";
//            header += sPercentageOfTT_DisabledChildPremiumAwardByTT[i] + ", ";
//            // EnhancedDisabilityPremiumAward
//            header += sTotalCount_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
//            header += sPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
//                header += sPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
//            header += sPercentageOfTT_EnhancedDisabilityPremiumAwardByTT[i] + ", ";
//            }
//        }
        // Council
        i = 1;
        // DisabilityAward
        header += sCouncilTotalCount_DisabilityAwardByTT1 + ", ";
        header += sCouncilPercentageOfTT_DisabilityAwardByTT1 + ", ";
        // DisabilityPremiumAward
        header += sCouncilTotalCount_DisabilityPremiumAwardByTT1 + ", ";
        header += sCouncilPercentageOfTT_DisabilityPremiumAwardByTT1 + ", ";
        // SevereDisabilityPremiumAward
        header += sCouncilTotalCount_SevereDisabilityPremiumAwardByTT1 + ", ";
        header += sCouncilPercentageOfTT_SevereDisabilityPremiumAwardByTT1 + ", ";
        // DisabledChildPremiumAward
        header += sCouncilTotalCount_DisabledChildPremiumAwardByTT1 + ", ";
        header += sCouncilPercentageOfTT_DisabledChildPremiumAwardByTT1 + ", ";
        // EnhancedDisabilityPremiumAward
        header += sCouncilTotalCount_EnhancedDisabilityPremiumAwardByTT1 + ", ";
        header += sCouncilPercentageOfTT_EnhancedDisabilityPremiumAwardByTT1 + ", ";
        // RSL
        // DisabilityAward
        header += sRSLTotalCount_DisabilityAwardByTT4 + ", ";
        header += sRSLPercentageOfTT_DisabilityAwardByTT4 + ", ";
        // DisabilityPremiumAward
        header += sRSLTotalCount_DisabilityPremiumAwardByTT4 + ", ";
        header += sRSLPercentageOfTT_DisabilityPremiumAwardByTT4 + ", ";
        // SevereDisabilityPremiumAward
        header += sRSLTotalCount_SevereDisabilityPremiumAwardByTT4 + ", ";
        header += sRSLPercentageOfTT_SevereDisabilityPremiumAwardByTT4 + ", ";
        // DisabledChildPremiumAward
        header += sRSLTotalCount_DisabledChildPremiumAwardByTT4 + ", ";
        header += sRSLPercentageOfTT_DisabledChildPremiumAwardByTT4 + ", ";
        // EnhancedDisabilityPremiumAward
        header += sRSLTotalCount_EnhancedDisabilityPremiumAwardByTT4 + ", ";
        header += sRSLPercentageOfTT_EnhancedDisabilityPremiumAwardByTT4 + ", ";
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
            line += filename1 + ", ";
            line += getLineSingleTimeGeneric(key, summary);
            // All UO
            // SocialTTs
            // DisabilityAward
            line += summary.get(sTotalCount_DisabilityAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfSocialTTs_DisabilityAwardSocialTTs) + ", ";
            // DisabilityPremiumAward
            line += summary.get(sTotalCount_DisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs) + ", ";
            // SevereDisabilityPremiumAward
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs) + ", ";
            // DisabledChildPremiumAward
            line += summary.get(sTotalCount_DisabledChildPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs) + ", ";
            // EnhancedDisabilityPremiumAward
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs) + ", ";
            line += summary.get(sPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs) + ", ";
//            for (i = 1; i < nTT; i++) {
//                // DisabilityAward
//                line += summary.get(sTotalCount_DisabilityAwardByTT[i]) + ", ";
//                line += summary.get(sPercentageOfAll_DisabilityAwardByTT[i]) + ", ";
//                    line += summary.get(sPercentageOfHB_DisabilityAwardByTT[i]) + ", ";
//                line += summary.get(sPercentageOfTT_DisabilityAwardByTT[i]) + ", ";
//                // DisabilityPremiumAward
//                line += summary.get(sTotalCount_DisabilityPremiumAwardByTT[i]) + ", ";
//                line += summary.get(sPercentageOfAll_DisabilityPremiumAwardByTT[i]) + ", ";
//                    line += summary.get(sPercentageOfHB_DisabilityPremiumAwardByTT[i]) + ", ";
//                line += summary.get(sPercentageOfTT_DisabilityPremiumAwardByTT[i]) + ", ";
//                // SevereDisabilityPremiumAward
//                line += summary.get(sTotalCount_SevereDisabilityPremiumAwardByTT[i]) + ", ";
//                line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardByTT[i]) + ", ";
//                    line += summary.get(sPercentageOfHB_SevereDisabilityPremiumAwardByTT[i]) + ", ";
//                line += summary.get(sPercentageOfTT_SevereDisabilityPremiumAwardByTT[i]) + ", ";
//                // DisabledChildPremiumAward
//                line += summary.get(sTotalCount_DisabledChildPremiumAwardByTT[i]) + ", ";
//                line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardByTT[i]) + ", ";
//                    line += summary.get(sPercentageOfHB_DisabledChildPremiumAwardByTT[i]) + ", ";
//                line += summary.get(sPercentageOfTT_DisabledChildPremiumAwardByTT[i]) + ", ";
//                // EnhancedDisabilityPremiumAward
//                line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
//                line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
//                    line += summary.get(sPercentageOfHB_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
//                line += summary.get(sPercentageOfTT_EnhancedDisabilityPremiumAwardByTT[i]) + ", ";
//            }
            // Council
            // SocialTTs
            i = 1;
            // DisabilityAward
            line += summary.get(sCouncilTotalCount_DisabilityAwardByTT1) + ", ";
            line += summary.get(sCouncilPercentageOfTT_DisabilityAwardByTT1) + ", ";
            // DisabilityPremiumAward
            line += summary.get(sCouncilTotalCount_DisabilityPremiumAwardByTT1) + ", ";
            line += summary.get(sCouncilPercentageOfTT_DisabilityPremiumAwardByTT1) + ", ";
            // SevereDisabilityPremiumAward
            line += summary.get(sCouncilTotalCount_SevereDisabilityPremiumAwardByTT1) + ", ";
            line += summary.get(sCouncilPercentageOfTT_SevereDisabilityPremiumAwardByTT1) + ", ";
            // DisabledChildPremiumAward
            line += summary.get(sCouncilTotalCount_DisabledChildPremiumAwardByTT1) + ", ";
            line += summary.get(sCouncilPercentageOfTT_DisabledChildPremiumAwardByTT1) + ", ";
            // EnhancedDisabilityPremiumAward
            line += summary.get(sCouncilTotalCount_EnhancedDisabilityPremiumAwardByTT1) + ", ";
            line += summary.get(sCouncilPercentageOfTT_EnhancedDisabilityPremiumAwardByTT1) + ", ";
            // RSL
            // DisabilityAward
            line += summary.get(sRSLTotalCount_DisabilityAwardByTT4) + ", ";
            line += summary.get(sRSLPercentageOfTT_DisabilityAwardByTT4) + ", ";
            // DisabilityPremiumAward
            line += summary.get(sRSLTotalCount_DisabilityPremiumAwardByTT4) + ", ";
            line += summary.get(sRSLPercentageOfTT_DisabilityPremiumAwardByTT4) + ", ";
            // SevereDisabilityPremiumAward
            line += summary.get(sRSLTotalCount_SevereDisabilityPremiumAwardByTT4) + ", ";
            line += summary.get(sRSLPercentageOfTT_SevereDisabilityPremiumAwardByTT4) + ", ";
            // DisabledChildPremiumAward
            line += summary.get(sRSLTotalCount_DisabledChildPremiumAwardByTT4) + ", ";
            line += summary.get(sRSLPercentageOfTT_DisabledChildPremiumAwardByTT4) + ", ";
            // EnhancedDisabilityPremiumAward
            line += summary.get(sRSLTotalCount_EnhancedDisabilityPremiumAwardByTT4) + ", ";
            line += summary.get(sRSLPercentageOfTT_EnhancedDisabilityPremiumAwardByTT4) + ", ";
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    @Override
    protected String getHeaderSingleTimeGeneric() {
        String result;
        result = "year-month, ";
        result += sCouncilFilename1 + ", ";
        result += sRSLFilename1 + ", ";

        //result += sAllCount1 + ", ";
        result += sAllUOAllCount1 + ", ";

        result += sAllUOLinkedRecordCount1 + ", ";
        result += sCouncilCount1 + ", ";
        result += sCouncilLinkedRecordCount1 + ", ";
        result += sRSLCount1 + ", ";
        result += sRSLLinkedRecordCount1 + ", ";
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
        result += summary.get(sRSLFilename1) + ", ";

        //result += summary.get(sAllCount1) + ", ";
        result += summary.get(sAllUOAllCount1) + ", ";

        result += summary.get(sAllUOLinkedRecordCount1) + ", ";
        result += summary.get(sCouncilCount1) + ", ";
        result += summary.get(sCouncilLinkedRecordCount1) + ", ";
        result += summary.get(sRSLCount1) + ", ";
        result += summary.get(sRSLLinkedRecordCount1) + ", ";
        String[] split;
        split = key.split("-");
        result += Generic_Time.getMonth3Letters(split[1]);
        result += " " + split[0] + ", ";
        return result;
    }
}
