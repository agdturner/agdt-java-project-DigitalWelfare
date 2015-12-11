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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
//import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.PostcodeID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_D_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.ID_PostcodeID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.ID_TenancyType;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.ID_TenancyType_PostcodeID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Set;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class Summary {

    private final DW_SHBE_Handler tDW_SHBE_Handler;

    private static final String postcodeLS277NS = "LS27 7NS";
    private static final String s0 = "0";
    // Counter Strings
    // Single
    private static final String sCountTT5or7 = "CountTT5or7";
    private static final String sCountHBRefNullTT5or7 = "CountHBRefNullTT5or7";
    private static final String sCountHBRefNullTTNot5or7 = "CountHBRefNullTTNot5or7";
    private static final String sCountHBRefNotNullTT5or7 = "CountHBRefNotNullTT5or7";
    private static final String sCountHBRefNotNullTTNot5or7 = "CountHBRefNotNullTTNot5or7";

    // All
    private static final String sAllTotalHouseholdSize = "AllTotalHouseholdSize";
    private static final String sAllAverageHouseholdSize = "AllAverageHouseholdSize";
    private static final String sHBTotalHouseholdSize = "HBTotalHouseholdSize";
    private static final String sHBAverageHouseholdSize = "HBAverageHouseholdSize";
    private static final String sCTBTotalHouseholdSize = "CTBTotalHouseholdSize";
    private static final String sCTBAverageHouseholdSize = "CTBAverageHouseholdSize";

    private static String[] TotalAllPassportedStandardIndicatorCountString;
    private static String[] TotalHBPassportedStandardIndicatorCountString;
    private static String[] TotalCTBPassportedStandardIndicatorCountString;
    private static String[] PercentageAllPassportedStandardIndicatorCountString;
    private static String[] PercentageHBPassportedStandardIndicatorCountString;
    private static String[] PercentageCTBPassportedStandardIndicatorCountString;
    private static String[][] TotalAllPassportedStandardIndicatorByTenancyTypeCountString;
    private static String[][] TotalHBPassportedStandardIndicatorByTenancyTypeCountString;
    private static String[][] TotalCTBPassportedStandardIndicatorByTenancyTypeCountString;
    private static String[][] PercentageAllPassportedStandardIndicatorByTenancyTypeCountString;
    private static String[][] PercentageHBPassportedStandardIndicatorByTenancyTypeCountString;
    private static String[][] PercentageCTBPassportedStandardIndicatorByTenancyTypeCountString;

    private static String[] AllTotalDisabilityPremiumAwardByTenancyTypeCountString;
    private static String[] HBTotalDisabilityPremiumAwardByTenancyTypeCountString;
    private static String[] CTBTotalDisabilityPremiumAwardByTenancyTypeCountString;
    private static String[] AllPercentageDisabilityPremiumAwardByTenancyTypeCountString;
    private static String[] HBPercentageDisabilityPremiumAwardByTenancyTypeCountString;
    private static String[] CTBPercentageDisabilityPremiumAwardByTenancyTypeCountString;
    private static String[] AllTotalSevereDisabilityPremiumAwardByTenancyTypeCountString;
    private static String[] HBTotalSevereDisabilityPremiumAwardByTenancyTypeCountString;
    private static String[] CTBTotalSevereDisabilityPremiumAwardByTenancyTypeCountString;
    private static String[] AllPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString;
    private static String[] HBPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString;
    private static String[] CTBPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString;
    private static String[] AllTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString;
    private static String[] HBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString;
    private static String[] CTBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString;
    private static String[] AllPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString;
    private static String[] HBPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString;
    private static String[] CTBPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString;

    private static final String sTotalWeeklyHBEntitlement = "TotalWeeklyHBEntitlement";
    private static final String sTotalCountWeeklyHBEntitlementNonZero = "TotalCountWeeklyHBEntitlementNonZero";
    private static final String sAverageWeeklyHBEntitlement = "AverageWeeklyHBEntitlement";
    private static final String sTotalWeeklyCTBEntitlement = "TotalWeeklyCTBEntitlement";
    private static final String sTotalCountWeeklyCTBEntitlementNonZero = "TotalCountWeeklyCTBEntitlementNonZero";
    private static final String sAverageWeeklyCTBEntitlement = "AverageWeeklyCTBEntitlement";
    private static final String sTotalWeeklyEligibleRentAmount = "TotalWeeklyEligibleRentAmount";
    private static final String sTotalCountWeeklyEligibleRentAmountNonZero = "TotalCountWeeklyEligibleRentAmountNonZero";
    private static final String sAverageWeeklyEligibleRentAmount = "AverageWeeklyEligibleRentAmount";
    private static final String sTotalWeeklyEligibleCouncilTaxAmount = "TotalWeeklyEligibleCouncilTaxAmount";
    private static final String sTotalCountWeeklyEligibleCouncilTaxAmountNonZero = "TotalCountWeeklyEligibleCouncilTaxAmountNonZero";
    private static final String sAverageWeeklyEligibleCouncilTaxAmount = "AverageWeeklyEligibleCouncilTaxAmount";
    private static final String sTotalContractualRentAmount = "TotalContractualRentAmount";
    private static final String sTotalCountContractualRentAmountNonZeroCount = "TotalCountContractualRentAmountNonZero";
    private static final String sAverageContractualRentAmount = "AverageContractualRentAmount";
    private static final String sTotalWeeklyAdditionalDiscretionaryPayment = "TotalWeeklyAdditionalDiscretionaryPayment";
    private static final String sTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero = "TotalCountWeeklyAdditionalDiscretionaryPaymentNonZero";
    private static final String sAverageWeeklyAdditionalDiscretionaryPayment = "AverageWeeklyAdditionalDiscretionaryPayment";
    private static final String sTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    private static final String sTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "TotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    private static final String sAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "AverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    private static final String sHBTotalCountClaimantsEmployed = "HBTotalCountClaimantsEmployed";
    private static final String sHBPercentageClaimantsEmployed = "HBPercentageClaimantsEmployed";
    private static final String sCTBTotalCountClaimantsEmployed = "CTBTotalCountClaimantsEmployed";
    private static final String sCTBPercentageClaimantsEmployed = "CTBPercentageClaimantsEmployed";
    private static final String sHBTotalCountClaimantsSelfEmployed = "HBTotalCountClaimantsSelfEmployed";
    private static final String sHBPercentageClaimantsSelfEmployed = "HBPercentageClaimantsSelfEmployed";
    private static final String sCTBTotalCountClaimantsSelfEmployed = "CTBTotalCountClaimantsSelfEmployed";
    private static final String sCTBPercentageClaimantsSelfEmployed = "CTBPercentageClaimantsSelfEmployed";
    private static final String sHBTotalCountClaimantsStudents = "HBTotalCountClaimantsStudents";
    private static final String sHBPercentageClaimantsStudents = "HBPercentageClaimantsStudents";
    private static final String sCTBTotalCountClaimantsStudents = "CTBTotalCountClaimantsStudents";
    private static final String sCTBPercentageClaimantsStudents = "CTBPercentageClaimantsStudents";
    private static final String sHBTotalCountLHACases = "HBTotalCountLHACases";
    private static final String sHBPercentageLHACases = "HBPercentageLHACases";
    private static final String sCTBTotalCountLHACases = "CTBTotalCountLHACases";
    private static final String sCTBPercentageLHACases = "CTBPercentageLHACases";

    private static final String sAllCount = "AllCount";
    private static final String sHBCount = "HBCount";
    private static final String sCTBCount = "CTBOnlyCount";

    private static String[] sAllTotalCountEthnicGroupClaimant;
    private static String[] sAllPercentageEthnicGroupClaimant;
    private static String[] sAllTotalCountTenancyTypeClaimant;
    private static String[] sAllPercentageTenancyTypeClaimant;
    private static String sAllTotalCountTenancyType8InLS277NSClaimant;
    private static String sAllPostcodeValidFormatCount;
    private static String sAllPostcodeValidCount;

    private static final String TotalIncomeString = "TotalIncome";
    private static final String TotalIncomeGreaterThanZeroCountString = "TotalIncomeGreaterThanZeroCount";
    private static final String AverageIncomeString = "AverageIncome";

    private static final String SHBEFilename00String = "SHBEFilename00";
    private static final String SHBEFilename0String = "SHBEFilename0";
    private static final String SHBEFilename1String = "SHBEFilename1";
    private static final String CouncilFilename00String = "CouncilFilename00";
    private static final String CouncilFilename0String = "CouncilFilename0";
    private static final String CouncilFilename1String = "CouncilFilename1";
    private static final String RSLFilename00String = "RSLFilename00";
    private static final String RSLFilename0String = "RSLFilename0";
    private static final String RSLFilename1String = "RSLFilename1";

    private static final String AllCount00String = "AllCount00";
    private static final String AllCount0String = "AllCount0";
    private static final String AllCount1String = "AllCount1";
    private static final String CouncilCount00String = "CouncilCount00";
    private static final String CouncilCount0String = "CouncilCount0";
    private static final String CouncilCount1String = "CouncilCount1";
    private static final String RSLCount00String = "RSLCount00";
    private static final String RSLCount0String = "RSLCount0";
    private static final String RSLCount1String = "RSLCount1";
    private static final String AllLinkedRecordCount00String = "AllLinkedRecordCount00";
    private static final String AllLinkedRecordCount0String = "AllLinkedRecordCount0";
    private static final String AllLinkedRecordCount1String = "AllLinkedRecordCount1";
    private static final String CouncilLinkedRecordCount00String = "CouncilLinkedRecordCount00";
    private static final String CouncilLinkedRecordCount0String = "CouncilLinkedRecordCount0";
    private static final String CouncilLinkedRecordCount1String = "CouncilLinkedRecordCount1";
    private static final String RSLLinkedRecordCount00String = "RSLLinkedRecordCount00";
    private static final String RSLLinkedRecordCount0String = "RSLLinkedRecordCount0";
    private static final String RSLLinkedRecordCount1String = "RSLLinkedRecordCount1";
    // Under Occupancy Only
//    private static final String councilCountString = "CouncilCount";
//    private static final String RSLCountString = "RSLCount";
    private static String TotalRentArrearsString;
    private static String AverageRentArrearsString;
    private static String GreaterThan0AverageRentArrearsString;

    // HB
//    private static String HBFemaleClaimantCountString;
//    private static String HBMaleClaimantCountString;
//    private static String HBDisabledClaimantCountString;
//    private static String HBFemaleDisabledClaimantCountString;
//    private static String HBMaleDisabledClaimantCountString;
    private static String[] sHBTotalCountEthnicGroupClaimant;
    private static String[] sHBPercentageEthnicGroupClaimant;
    private static String[] sHBTotalCountTenancyTypeClaimant;
    private static String[] sHBPercentageTenancyTypeClaimant;
    private static String sHBTotalCountTenancyType8InLS277NSClaimant;
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
    private static String[] sCTBTotalCountTenancyTypeClaimant;
    private static String[] sCTBPercentageTenancyTypeClaimant;
    private static String sCTBTotalCountTenancyType8InLS277NSClaimant;
    private static String sCTBPostcodeValidFormatCount;
    private static String sCTBPostcodeValidCount;

    // Compare 2 Times
    // All
    private static final String sAllTotalCountTenancyTypeChangeClaimant = "AllTotalCountTenancyTypeChangeClaimant";
    private static final String sAllPercentageTenancyTypeChangeClaimant = "AllPercentageTenancyTypeChangeClaimant";
    // HB
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

    private static final String sHBTotalCountTenancyTypeChangeClaimant = "HBTotalCountTenancyTypeChangeClaimant";
    private static final String sHBPercentageTenancyTypeChangeClaimant = "HBPercentageTenancyTypeChangeClaimant";
    private static final String sHBTotalCountTenancyTypeChangeClaimantHousing = "HBTotalCountTenancyTypeChangeClaimantHousing";
    private static final String sHBPercentageTenancyTypeChangeClaimantHousing = "HBPercentageTenancyTypeChangeClaimantHousing";
    private static final String sHBTotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes = "HBTotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes";
    private static final String sHBPercentageSocialTenancyTypesToPrivateDeregulatedTenancyTypes = "HBPercentageSocialTenancyTypesToPrivateDeregulatedTenancyTypes";
    private static final String sHBTotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes = "HBTotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes";
    private static final String sHBPercentagePrivateDeregulatedTenancyTypesToSocialTenancyTypes = "HBPercentagePrivateDeregulatedTenancyTypesToSocialTenancyTypes";
    private static final String sHBTotalCountPostcodeChangeWithinSocialTenancyTypes = "HBTotalCountPostcodeChangeWithinSocialTenancyTypes";
    private static final String sHBPercentagePostcodeChangeWithinSocialTenancyTypes = "HBPercentagePostcodeChangeWithinSocialTenancyTypes";
    private static final String sHBTotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes = "HBTotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes";
    private static final String sHBPercentagePostcodeChangeWithinPrivateDeregulatedTenancyTypes = "HBPercentagePostcodeChangeWithinPrivateDeregulatedTenancyTypes";
    // CTB
    private static final String sCTBTotalCountPostcode0ValidPostcode1Valid = "CTBTotalCountPostcode0ValidPostcode1Valid";
    private static final String sCTBPercentagePostcode0ValidPostcode1Valid = "CTBPercentagePostcode0ValidPostcode1Valid";
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
    private static final String sCTBTotalCountTenancyTypeChangeClaimant = "CTBTotalCountTenancyTypeChangeClaimant";
    private static final String sCTBPercentageTenancyTypeChangeClaimant = "CTBPercentageTenancyTypeChangeClaimant";
    private static final String sCTBTotalCountTenancyTypeChangeClaimantHousing = "CTBTotalCountTenancyTypeChangeClaimantHousing";
    private static final String sCTBPercentageTenancyTypeChangeClaimantHousing = "CTBPercentageTenancyTypeChangeClaimantHousing";
    private static final String sCTBTotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes = "CTBTotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes";
    private static final String sCTBPercentageSocialTenancyTypesToPrivateDeregulatedTenancyTypes = "CTBPercentageSocialTenancyTypesToPrivateDeregulatedTenancyTypes";
    private static final String sCTBTotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes = "CTBTotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes";
    private static final String sCTBPercentagePrivateDeregulatedTenancyTypesToSocialTenancyTypes = "CTBPercentagePrivateDeregulatedTenancyTypesToSocialTenancyTypes";
    private static final String sCTBTotalCountPostcodeChangeWithinSocialTenancyTypes = "CTBTotalCountPostcodeChangeWithinSocialTenancyTypes";
    private static final String sCTBPercentagePostcodeChangeWithinSocialTenancyTypes = "CTBPercentagePostcodeChangeWithinSocialTenancyTypes";
    private static final String sCTBTotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes = "CTBTotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes";
    private static final String sCTBPercentagePostcodeChangeWithinPrivateDeregulatedTenancyTypes = "CTBPercentagePostcodeChangeWithinPrivateDeregulatedTenancyTypes";

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
    private static String[] SameTenancyIIITenancyTypeString;
    private static String[] SameTenancyIOITenancyTypeString;
    private static String[] SameTenancyOIOTenancyTypeString;
    private static String[] SameTenancyAndPostcodeIIITenancyTypeString;
    private static String[] SameTenancyAndPostcodeIOITenancyTypeString;
    private static String[] SameTenancyAndPostcodeOIOTenancyTypeString;

    // Counters
    // Single Time
    // All
    private static double TotalWeeklyHBEntitlement;
    private static int TotalWeeklyHBEntitlementNonZeroCount;
    private static double TotalWeeklyCTBEntitlement;
    private static int TotalWeeklyCTBEntitlementNonZeroCount;
    private static double TotalWeeklyEligibleRentAmount;
    private static int TotalWeeklyEligibleRentAmountNonZeroCount;
    private static double TotalWeeklyEligibleCouncilTaxAmount;
    private static int TotalWeeklyEligibleCouncilTaxAmountNonZeroCount;
    private static double TotalContractualRentAmount;
    private static int TotalContractualRentAmountNonZeroCount;
    private static double TotalWeeklyAdditionalDiscretionaryPayment;
    private static int TotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    private static double TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    private static int TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    private static int TotalHBClaimantsEmployed;
    private static double PercentageHBClaimantsEmployed;
    private static int TotalCTBClaimantsEmployed;
    private static double PercentageCTBClaimantsEmployed;
    private static int TotalHBClaimantsSelfEmployed;
    private static double PercentageHBClaimantsSelfEmployed;
    private static int TotalCTBClaimantsSelfEmployed;
    private static double PercentageCTBClaimantsSelfEmployed;
    private static int TotalHBClaimantsStudents;
    private static double PercentageHBClaimantsStudents;
    private static int TotalCTBClaimantsStudents;
    private static double PercentageCTBClaimantsStudents;
    private static int TotalHBLHACases;
    private static double PercentageHBLHACases;
    private static int TotalCTBLHACases;
    private static double PercentageCTBLHACases;

    private static int[] AllTotalDisabilityPremiumAwardByTenancyTypeCount;
    private static int[] HBTotalDisabilityPremiumAwardByTenancyTypeCount;
    private static int[] CTBTotalDisabilityPremiumAwardByTenancyTypeCount;
    private static int[] AllTotalSevereDisabilityPremiumAwardByTenancyTypeCount;
    private static int[] HBTotalSevereDisabilityPremiumAwardByTenancyTypeCount;
    private static int[] CTBTotalSevereDisabilityPremiumAwardByTenancyTypeCount;
    private static int[] AllTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount;
    private static int[] HBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount;
    private static int[] CTBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount;
    private static int[] TotalAllPassportedStandardIndicatorCount;
    private static int[] TotalHBPassportedStandardIndicatorCount;
    private static int[] TotalCTBPassportedStandardIndicatorCount;
    private static int[][] TotalAllPassportedStandardIndicatorByTenancyTypeCount;
    private static int[][] TotalHBPassportedStandardIndicatorByTenancyTypeCount;
    private static int[][] TotalCTBPassportedStandardIndicatorByTenancyTypeCount;

    //private static int[] AllTenancyTypeClaimantCount;
    private static long AllTotalHouseholdSize;
    private static long HBTotalHouseholdSize;
    private static long CTBTotalHouseholdSize;
    // HB
    private static int HBCount;
    private static int[] HBTenancyTypeCount;
    private static int[] HBEthnicGroupCount;
    private static int HBTenancyType8InLS277NSClaimantCount;
    private static int HBPostcodeValidFormatCount;
    private static int HBPostcodeValidCount;
    // CTB
    private static int CTBCount;
    private static int[] CTBTenancyTypeCount;
    private static int[] CTBEthnicGroupCount;
    private static int CTBTenancyType8InLS277NSClaimantCount;
    private static int CTBPostcodeValidFormatCount;
    private static int CTBPostcodeValidCount;
    // Other summary stats
    private static double totalRentArrears;
    private static double rentArrearsCount;
    private static double greaterThan0RentArrearsCount;
    private static int tDRecordsCTBRefDW_SHBE_RecordNullCount;

    // Compare 2 Times
    // All
    private static int AllTotalCountTenancyTypeChangeClaimant;
    // HB
    private static int HBTotalCountPostcode0ValidPostcode1Valid;
    private static int HBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged;
    private static int HBTotalCountPostcode0ValidPostcode1ValidPostcodeChange;
    private static int HBTotalCountPostcode0ValidPostcode1NotValid;
    private static int HBTotalCountPostcode0NotValidPostcode1Valid;
    private static int HBTotalCountPostcode0NotValidPostcode1NotValid;
    private static int HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged;
    private static int HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged;
    private static int HBTotalCountTenancyTypeChangeClaimant;
    private static int HBTenancyTypeChangeHousingCount;
    private static int HBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount;
    private static int HBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount;
    private static int HBPostcodeChangeWithinSocialTenancyTypesCount;
    private static int HBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount;
    // CTB
    private static int CTBTotalCountPostcode0ValidPostcode1Valid;
    private static int CTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged;
    private static int CTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged;
    private static int CTBTotalCountPostcode0ValidPostcode1NotValid;
    private static int CTBTotalCountPostcode0NotValidPostcode1Valid;
    private static int CTBTotalCountPostcode0NotValidPostcode1NotValid;
    private static int CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged;
    private static int CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged;
    private static int CTBTenancyTypeChangeCount;
    private static int CTBTenancyTypeChangeHousingCount;
    private static int CTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount;
    private static int CTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount;
    private static int CTBPostcodeChangeWithinSocialTenancyTypesCount;
    private static int CTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount;

    private static int CountTT5or7;
    private static int CountHBRefNullTT5or7;
    private static int CountHBRefNullTTNot5or7;
    private static int CountHBRefNotNullTT5or7;
    private static int CountHBRefNotNullTTNot5or7;

    private static int AllCount00;
    private static int AllCount0;
    private static int AllCount1;
    private static int CouncilCount00;
    private static int CouncilCount0;
    private static int CouncilCount1;
    private static int RSLCount00;
    private static int RSLCount0;
    private static int RSLCount1;
    private static int AllLinkedRecordCount00;
    private static int AllLinkedRecordCount0;
    private static int AllLinkedRecordCount1;
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
    private static int[] SameTenancyIIITenancyType;
    private static int[] SameTenancyIOITenancyType;
    private static int[] SameTenancyOIOTenancyType;
    private static int[] SameTenancyAndPostcodeIIITenancyType;
    private static int[] SameTenancyAndPostcodeIOITenancyType;
    private static int[] SameTenancyAndPostcodeOIOTenancyType;

    public Summary(DW_SHBE_Handler tDW_SHBE_Handler, int nTT, int nEG, int nPSI) {
        this.tDW_SHBE_Handler = tDW_SHBE_Handler;
        init(nTT, nEG, nPSI);
    }

    protected final void init(int nTT, int nEG, int nPSI) {
        initSingleTimeStrings(nTT, nEG, nPSI);
        initCompare3TimesStrings(nTT, nEG);
        AllTotalDisabilityPremiumAwardByTenancyTypeCount = new int[nTT];
        HBTotalDisabilityPremiumAwardByTenancyTypeCount = new int[nTT];
        CTBTotalDisabilityPremiumAwardByTenancyTypeCount = new int[nTT];
        AllTotalSevereDisabilityPremiumAwardByTenancyTypeCount = new int[nTT];
        HBTotalSevereDisabilityPremiumAwardByTenancyTypeCount = new int[nTT];
        CTBTotalSevereDisabilityPremiumAwardByTenancyTypeCount = new int[nTT];
        AllTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount = new int[nTT];
        HBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount = new int[nTT];
        CTBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount = new int[nTT];
        TotalAllPassportedStandardIndicatorCount = new int[nPSI];
        TotalHBPassportedStandardIndicatorCount = new int[nPSI];
        TotalCTBPassportedStandardIndicatorCount = new int[nPSI];
        TotalAllPassportedStandardIndicatorByTenancyTypeCount = new int[nPSI][nTT];
        TotalHBPassportedStandardIndicatorByTenancyTypeCount = new int[nPSI][nTT];
        TotalCTBPassportedStandardIndicatorByTenancyTypeCount = new int[nPSI][nTT];
        HBEthnicGroupCount = new int[nEG];
        CTBEthnicGroupCount = new int[nEG];
        HBTenancyTypeCount = new int[nTT];
        CTBTenancyTypeCount = new int[nTT];
    }

    protected final void initSingleTimeStrings(int nTT, int nEG, int nPSI) {
        TotalAllPassportedStandardIndicatorCountString = new String[nPSI];
        TotalHBPassportedStandardIndicatorCountString = new String[nPSI];
        TotalCTBPassportedStandardIndicatorCountString = new String[nPSI];
        PercentageAllPassportedStandardIndicatorCountString = new String[nPSI];
        PercentageHBPassportedStandardIndicatorCountString = new String[nPSI];
        PercentageCTBPassportedStandardIndicatorCountString = new String[nPSI];
        TotalAllPassportedStandardIndicatorByTenancyTypeCountString = new String[nPSI][nTT];
        TotalHBPassportedStandardIndicatorByTenancyTypeCountString = new String[nPSI][nTT];
        TotalCTBPassportedStandardIndicatorByTenancyTypeCountString = new String[nPSI][nTT];
        PercentageAllPassportedStandardIndicatorByTenancyTypeCountString = new String[nPSI][nTT];
        PercentageHBPassportedStandardIndicatorByTenancyTypeCountString = new String[nPSI][nTT];
        PercentageCTBPassportedStandardIndicatorByTenancyTypeCountString = new String[nPSI][nTT];
        for (int i = 1; i < nPSI; i++) {
            TotalAllPassportedStandardIndicatorCountString[i] = "TotalAllPassportedStandardIndicator" + i + "Count";
            TotalHBPassportedStandardIndicatorCountString[i] = "TotalHBPassportedStandardIndicator" + i + "Count";
            TotalCTBPassportedStandardIndicatorCountString[i] = "TotalCTBPassportedStandardIndicator" + i + "Count";
            PercentageAllPassportedStandardIndicatorCountString[i] = "PercentageAllPassportedStandardIndicator" + i + "Count";
            PercentageHBPassportedStandardIndicatorCountString[i] = "PercentageHBPassportedStandardIndicator" + i + "Count";
            PercentageCTBPassportedStandardIndicatorCountString[i] = "PercentageCTBPassportedStandardIndicator" + i + "Count";
            for (int j = 1; j < nTT; j++) {
                TotalAllPassportedStandardIndicatorByTenancyTypeCountString[i][j] = "TotalAllPassportedStandardIndicator" + i + "TenancyType" + j + "Count";
                TotalHBPassportedStandardIndicatorByTenancyTypeCountString[i][j] = "TotalHBPassportedStandardIndicator" + i + "TenancyType" + j + "Count";
                TotalCTBPassportedStandardIndicatorByTenancyTypeCountString[i][j] = "TotalCTBPassportedStandardIndicator" + i + "TenancyType" + j + "Count";
                PercentageAllPassportedStandardIndicatorByTenancyTypeCountString[i][j] = "PercentageAllPassportedStandardIndicator" + i + "TenancyType" + j + "Count";
                PercentageHBPassportedStandardIndicatorByTenancyTypeCountString[i][j] = "PercentageHBPassportedStandardIndicator" + i + "TenancyType" + j + "Count";
                PercentageCTBPassportedStandardIndicatorByTenancyTypeCountString[i][j] = "PercentageCTBPassportedStandardIndicator" + i + "TenancyType" + j + "Count";
            }
        }

        // All
        sAllTotalCountEthnicGroupClaimant = new String[nEG];
        sAllPercentageEthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            sAllTotalCountEthnicGroupClaimant[i] = "AllTotalCountEthnicGroup" + i + "Claimant";
            sAllPercentageEthnicGroupClaimant[i] = "AllPercentageEthnicGroup" + i + "Claimant";
        }

        AllTotalDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        HBTotalDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        CTBTotalDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        AllPercentageDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        HBPercentageDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        CTBPercentageDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        AllTotalSevereDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        HBTotalSevereDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        CTBTotalSevereDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        AllPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        HBPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        CTBPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        AllTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        HBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        CTBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        AllPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        HBPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        CTBPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString = new String[nTT];
        sAllTotalCountTenancyTypeClaimant = new String[nTT];
        sAllPercentageTenancyTypeClaimant = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            sAllTotalCountTenancyTypeClaimant[i] = "AllTenancyType" + i + "ClaimantCount";
            AllTotalDisabilityPremiumAwardByTenancyTypeCountString[i] = "AllTotalDisabilityPremiumAwardByTenancyType" + i + "Count";
            HBTotalDisabilityPremiumAwardByTenancyTypeCountString[i] = "HBTotalDisabilityPremiumAwardByTenancyType" + i + "Count";
            CTBTotalDisabilityPremiumAwardByTenancyTypeCountString[i] = "CTBTotalDisabilityPremiumAwardByTenancyType" + i + "Count";
            AllPercentageDisabilityPremiumAwardByTenancyTypeCountString[i] = "AllPercentageDisabilityPremiumAwardByTenancyType" + i + "Count";
            HBPercentageDisabilityPremiumAwardByTenancyTypeCountString[i] = "HBPercentageDisabilityPremiumAwardByTenancyType" + i + "Count";
            CTBPercentageDisabilityPremiumAwardByTenancyTypeCountString[i] = "CTBPercentageDisabilityPremiumAwardByTenancyType" + i + "Count";
            AllTotalSevereDisabilityPremiumAwardByTenancyTypeCountString[i] = "AllTotalSevereDisabilityPremiumAwardByTenancyType" + i + "Count";
            HBTotalSevereDisabilityPremiumAwardByTenancyTypeCountString[i] = "HBTotalSevereDisabilityPremiumAwardByTenancyType" + i + "Count";
            CTBTotalSevereDisabilityPremiumAwardByTenancyTypeCountString[i] = "CTBTotalSevereDisabilityPremiumAwardByTenancyType" + i + "Count";
            AllPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i] = "AllPercentageSevereDisabilityPremiumAwardByTenancyType" + i + "Count";
            HBPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i] = "HBPercentageSevereDisabilityPremiumAwardByTenancyType" + i + "Count";
            CTBPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i] = "CTBPercentageSevereDisabilityPremiumAwardByTenancyType" + i + "Count";
            AllTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] = "AllTotalEnhancedDisabilityPremiumAwardByTenancyType" + i + "Count";
            HBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] = "HBTotalEnhancedDisabilityPremiumAwardByTenancyType" + i + "Count";
            CTBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] = "CTBTotalEnhancedDisabilityPremiumAwardByTenancyType" + i + "Count";
            AllPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] = "AllPercentageEnhancedDisabilityPremiumAwardByTenancyType" + i + "Count";
            HBPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] = "HBPercentageEnhancedDisabilityPremiumAwardByTenancyType" + i + "Count";
            CTBPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] = "CTBPercentageEnhancedDisabilityPremiumAwardByTenancyType" + i + "Count";
        }
        sAllTotalCountTenancyType8InLS277NSClaimant = "AllTenancyType8InLS277NSClaimantCount";
        sAllPostcodeValidFormatCount = "AllPostcodeValidFormatCount";
        sAllPostcodeValidCount = "AllPostcodeValidCount";

        // HB
        sHBTotalCountEthnicGroupClaimant = new String[nEG];
        sHBPercentageEthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            sHBTotalCountEthnicGroupClaimant[i] = "HBTotalCountEthnicGroup" + i + "Claimant";
            sHBPercentageEthnicGroupClaimant[i] = "HBPercentageEthnicGroup" + i + "Claimant";
        }
        sHBTotalCountTenancyTypeClaimant = new String[nTT];
        sHBPercentageTenancyTypeClaimant = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            sHBTotalCountTenancyTypeClaimant[i] = "HBTotalCountTenancyType" + i + "Claimant";
            sHBPercentageTenancyTypeClaimant[i] = "HBPercentageTenancyType" + i + "Claimant";
        }
        sHBTotalCountTenancyType8InLS277NSClaimant = "HBTenancyType8InLS277NSClaimantCount";
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
        sCTBTotalCountTenancyTypeClaimant = new String[nTT];
        sCTBPercentageTenancyTypeClaimant = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            sCTBTotalCountTenancyTypeClaimant[i] = "CTBTotalCountTenancyType" + i + "Claimant";
            sCTBPercentageTenancyTypeClaimant[i] = "CTBPercentageTenancyType" + i + "Claimant";
        }
        sCTBTotalCountTenancyType8InLS277NSClaimant = "CTBTenancyType8InLS277NSClaimantCount";
        sCTBPostcodeValidFormatCount = "CTBPostcodeValidFormatCount";
        sCTBPostcodeValidCount = "CTBPostcodeValidCount";
//        CTBFemaleClaimantCountString = "CTBFemaleClaimantCount";
//        CTBDisabledClaimantCountString = "CTBDisabledClaimantCount";
//        CTBFemaleDisabledClaimantCountString = "CTBDisabledFemaleClaimantCount";
//        CTBMaleDisabledClaimantCountString = "CTBDisabledMaleClaimantCount";

        // Under Occupancy
        TotalRentArrearsString = "TotalRentArrears";
        AverageRentArrearsString = "AverageRentArrears";
        GreaterThan0AverageRentArrearsString = "GreaterThan0AverageRentArrears";
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
        SameTenancyIIITenancyTypeString = new String[nTT];
        SameTenancyIOITenancyTypeString = new String[nTT];
        SameTenancyOIOTenancyTypeString = new String[nTT];
        SameTenancyAndPostcodeIIITenancyTypeString = new String[nTT];
        SameTenancyAndPostcodeIOITenancyTypeString = new String[nTT];
        SameTenancyAndPostcodeOIOTenancyTypeString = new String[nTT];
        for (int i = 0; i < nTT; i++) {
            SameTenancyIIITenancyTypeString[i] = "SameTenancyIIITenancyType" + i;
            SameTenancyIOITenancyTypeString[i] = "SameTenancyIOITenancyType" + i;
            SameTenancyOIOTenancyTypeString[i] = "SameTenancyOIOTenancyType" + i;
            SameTenancyAndPostcodeIIITenancyTypeString[i] = "SameTenancyAndPostcodeIIITenancyType" + i;
            SameTenancyAndPostcodeIOITenancyTypeString[i] = "SameTenancyAndPostcodeIOITenancyType" + i;
            SameTenancyAndPostcodeOIOTenancyTypeString[i] = "SameTenancyAndPostcodeOIOTenancyType" + i;
        }
    }

    private void initCounts(int nTT, int nEG, int nPSI) {
        initSingleTimeCounts(nTT, nEG, nPSI);
        initCompare2TimesCounts(nTT, nEG);
        initCompare3TimesCounts(nTT, nEG);
    }

    private void initSingleTimeCounts(int nTT, int nEG, int nPSI) {

        for (int i = 1; i < nPSI; i++) {
            TotalAllPassportedStandardIndicatorCount[i] = 0;
            TotalHBPassportedStandardIndicatorCount[i] = 0;
            TotalCTBPassportedStandardIndicatorCount[i] = 0;
            for (int j = 1; j < nTT; j++) {
                TotalAllPassportedStandardIndicatorByTenancyTypeCount[i][j] = 0;
                TotalHBPassportedStandardIndicatorByTenancyTypeCount[i][j] = 0;
                TotalCTBPassportedStandardIndicatorByTenancyTypeCount[i][j] = 0;
            }
        }

        // All
        CountTT5or7 = 0;
        CountHBRefNullTT5or7 = 0;
        CountHBRefNullTTNot5or7 = 0;
        CountHBRefNotNullTT5or7 = 0;
        CountHBRefNotNullTTNot5or7 = 0;

        TotalWeeklyHBEntitlement = 0.0d;
        TotalWeeklyHBEntitlementNonZeroCount = 0;
        TotalWeeklyCTBEntitlement = 0.0d;
        TotalWeeklyCTBEntitlementNonZeroCount = 0;
        TotalWeeklyEligibleRentAmount = 0.0d;
        TotalWeeklyEligibleRentAmountNonZeroCount = 0;
        TotalWeeklyEligibleCouncilTaxAmount = 0.0d;
        TotalWeeklyEligibleCouncilTaxAmountNonZeroCount = 0;
        TotalContractualRentAmount = 0.0d;
        TotalContractualRentAmountNonZeroCount = 0;
        TotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        TotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;
        TotalHBClaimantsEmployed = 0;
        PercentageHBClaimantsEmployed = 0.0d;
        TotalCTBClaimantsEmployed = 0;
        PercentageCTBClaimantsEmployed = 0.0d;
        TotalHBClaimantsSelfEmployed = 0;
        PercentageHBClaimantsSelfEmployed = 0.0d;
        TotalCTBClaimantsSelfEmployed = 0;
        PercentageCTBClaimantsSelfEmployed = 0.0d;
        TotalHBClaimantsStudents = 0;
        PercentageHBClaimantsStudents = 0.0d;
        TotalCTBClaimantsStudents = 0;
        PercentageCTBClaimantsStudents = 0.0d;
        TotalHBLHACases = 0;
        PercentageHBLHACases = 0.0d;
        TotalCTBLHACases = 0;
        PercentageCTBLHACases = 0.0d;
        for (int i = 1; i < nTT; i++) {
            AllTotalDisabilityPremiumAwardByTenancyTypeCount[i] = 0;
            HBTotalDisabilityPremiumAwardByTenancyTypeCount[i] = 0;
            CTBTotalDisabilityPremiumAwardByTenancyTypeCount[i] = 0;
            AllTotalSevereDisabilityPremiumAwardByTenancyTypeCount[i] = 0;
            HBTotalSevereDisabilityPremiumAwardByTenancyTypeCount[i] = 0;
            CTBTotalSevereDisabilityPremiumAwardByTenancyTypeCount[i] = 0;
            AllTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[i] = 0;
            HBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[i] = 0;
            CTBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[i] = 0;
            HBTenancyTypeCount[i] = 0;
        }
        // HB
        for (int i = 1; i < nEG; i++) {
            HBEthnicGroupCount[i] = 0;
        }
        for (int i = 1; i < nTT; i++) {
            HBTenancyTypeCount[i] = 0;
        }
        HBCount = 0;
        HBTenancyType8InLS277NSClaimantCount = 0;
        HBPostcodeValidFormatCount = 0;
        HBPostcodeValidCount = 0;

        AllTotalHouseholdSize = 0L;
        HBTotalHouseholdSize = 0L;
        CTBTotalHouseholdSize = 0L;

        // CTB
        for (int i = 1; i < nEG; i++) {
            CTBEthnicGroupCount[i] = 0;
        }
        for (int i = 1; i < nTT; i++) {
            CTBTenancyTypeCount[i] = 0;
        }
        CTBCount = 0;
        CTBTenancyType8InLS277NSClaimantCount = 0;
        CTBPostcodeValidFormatCount = 0;
        CTBPostcodeValidCount = 0;

        AllCount1 = 0;
        CouncilCount1 = 0;
        RSLCount1 = 0;
        AllLinkedRecordCount1 = 0;
        CouncilLinkedRecordCount1 = 0;
        RSLLinkedRecordCount1 = 0;
    }

    private void initCompare2TimesCounts(int nTT, int nEG) {
        // All
        AllTotalCountTenancyTypeChangeClaimant = 0;

        // HB
        HBTotalCountPostcode0ValidPostcode1Valid = 0;
        HBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        HBTotalCountPostcode0ValidPostcode1ValidPostcodeChange = 0;
        HBTotalCountPostcode0ValidPostcode1NotValid = 0;
        HBTotalCountPostcode0NotValidPostcode1Valid = 0;
        HBTotalCountPostcode0NotValidPostcode1NotValid = 0;
        HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged = 0;
        HBTotalCountTenancyTypeChangeClaimant = 0;
        HBTenancyTypeChangeHousingCount = 0;
        HBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount = 0;
        HBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount = 0;
        HBPostcodeChangeWithinSocialTenancyTypesCount = 0;
        HBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount = 0;
        // CTB
        CTBTotalCountPostcode0ValidPostcode1Valid = 0;
        CTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        CTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged = 0;
        CTBTotalCountPostcode0ValidPostcode1NotValid = 0;
        CTBTotalCountPostcode0NotValidPostcode1Valid = 0;
        CTBTotalCountPostcode0NotValidPostcode1NotValid = 0;
        CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged = 0;
        CTBTenancyTypeChangeCount = 0;
        CTBTenancyTypeChangeHousingCount = 0;
        CTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount = 0;
        CTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount = 0;
        CTBPostcodeChangeWithinSocialTenancyTypesCount = 0;
        CTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount = 0;
        // Under Occupancy
        totalRentArrears = 0.0d;
        rentArrearsCount = 0.0d;
        greaterThan0RentArrearsCount = 0.0d;
        tDRecordsCTBRefDW_SHBE_RecordNullCount = 0;
    }

    private void initCompare3TimesCounts(int nTT, int nEG) {
        SamePostcodeIII = 0;
        SamePostcodeIOI = 0;
        SamePostcodeOIO = 0;
        SameTenancyIII = 0;
        SameTenancyIOI = 0;
        SameTenancyOIO = 0;
        SameTenancyAndPostcodeIII = 0;
        SameTenancyAndPostcodeIOI = 0;
        SameTenancyAndPostcodeOIO = 0;
        SameTenancyIIITenancyType = new int[nTT];
        SameTenancyIOITenancyType = new int[nTT];
        SameTenancyOIOTenancyType = new int[nTT];
        SameTenancyAndPostcodeIIITenancyType = new int[nTT];
        SameTenancyAndPostcodeIOITenancyType = new int[nTT];
        SameTenancyAndPostcodeOIOTenancyType = new int[nTT];
        for (int i = 0; i < nTT; i++) {
            SameTenancyIIITenancyType[i] = 0;
            SameTenancyIOITenancyType[i] = 0;
            SameTenancyOIOTenancyType[i] = 0;
            SameTenancyAndPostcodeIIITenancyType[i] = 0;
            SameTenancyAndPostcodeIOITenancyType[i] = 0;
            SameTenancyAndPostcodeOIOTenancyType[i] = 0;
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
            HashMap<String, DW_ID> NINOToIDLookup) {
        HashSet<ID_TenancyType> result;
        result = new HashSet<ID_TenancyType>();
        TreeMap<String, DW_UnderOccupiedReport_Record> RSLMap;
        RSLMap = RSLUnderOccupiedSet.getMap();
        TreeMap<String, DW_UnderOccupiedReport_Record> councilMap;
        councilMap = councilUnderOccupiedSet.getMap();
        Iterator<String> ite;
        ite = councilMap.keySet().iterator();
        while (ite.hasNext()) {
            String CTBRef;
            CTBRef = ite.next();
            if (D_Records.containsKey(CTBRef)) {
                DW_SHBE_D_Record D_Record;
                D_Record = D_Records.get(CTBRef).getDRecord();
                DW_ID ID;
                ID = NINOToIDLookup.get(D_Record.getClaimantsNationalInsuranceNumber());
                int TenancyType;
                TenancyType = D_Record.getTenancyType();
                ID_TenancyType ID_TenancyType;
                ID_TenancyType = new ID_TenancyType(ID, TenancyType);
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
                DW_ID ID;
                ID = NINOToIDLookup.get(D_Record.getClaimantsNationalInsuranceNumber());
                int TenancyType;
                TenancyType = D_Record.getTenancyType();
                ID_TenancyType ID_TenancyType;
                ID_TenancyType = new ID_TenancyType(ID, TenancyType);
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
            HashMap<String, DW_ID> NINOToIDLookup,
            HashMap<String, DW_ID> PostcodeToPostcodeIDLookup) {
        HashSet<ID_PostcodeID> result;
        result = new HashSet<ID_PostcodeID>();
        TreeMap<String, DW_UnderOccupiedReport_Record> RSLMap;
        RSLMap = RSLUnderOccupiedSet.getMap();
        TreeMap<String, DW_UnderOccupiedReport_Record> councilMap;
        councilMap = councilUnderOccupiedSet.getMap();
        Iterator<String> ite;
        ite = councilMap.keySet().iterator();
        while (ite.hasNext()) {
            String CTBRef;
            CTBRef = ite.next();
            DW_SHBE_D_Record D_Record;
            if (D_Records.containsKey(CTBRef)) {
                D_Record = D_Records.get(CTBRef).getDRecord();
                DW_ID ID;
                ID = NINOToIDLookup.get(D_Record.getClaimantsNationalInsuranceNumber());
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
                DW_ID ID;
                ID = NINOToIDLookup.get(D_Record.getClaimantsNationalInsuranceNumber());
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
            HashMap<String, DW_ID> NINOToIDLookup,
            HashMap<String, DW_ID> PostcodeToPostcodeIDLookup) {
        HashSet<ID_TenancyType_PostcodeID> result;
        result = new HashSet<ID_TenancyType_PostcodeID>();
        TreeMap<String, DW_UnderOccupiedReport_Record> RSLMap;
        RSLMap = RSLUnderOccupiedSet.getMap();
        TreeMap<String, DW_UnderOccupiedReport_Record> councilMap;
        councilMap = councilUnderOccupiedSet.getMap();
        Iterator<String> ite;
        ite = councilMap.keySet().iterator();
        while (ite.hasNext()) {
            String CTBRef;
            CTBRef = ite.next();
            if (D_Records.containsKey(CTBRef)) {
                DW_SHBE_D_Record D_Record;
                D_Record = D_Records.get(CTBRef).getDRecord();
                DW_ID ID;
                ID = NINOToIDLookup.get(D_Record.getClaimantsNationalInsuranceNumber());
                int TenancyType;
                TenancyType = D_Record.getTenancyType();
                ID_TenancyType ID_TenancyType;
                ID_TenancyType = new ID_TenancyType(ID, TenancyType);
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
                DW_ID ID;
                ID = NINOToIDLookup.get(D_Record.getClaimantsNationalInsuranceNumber());
                int TenancyType;
                TenancyType = D_Record.getTenancyType();
                ID_TenancyType ID_TenancyType;
                ID_TenancyType = new ID_TenancyType(ID, TenancyType);
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
     * @param tClaimantIDPostcodeTenancyTypes
     * @param tClaimantIDPostcodeTenancyTypes1
     * @return
     */
    public static Object[] getCountsIDPostcodeTenancyType(
            int nTT,
            String[] SHBEFilenames,
            ArrayList<Integer> include,
            String yM31,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTenancyTypes,
            HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTenancyTypes1) {
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
            HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTenancyTypes00;
            tClaimantIDPostcodeTenancyTypes00 = tClaimantIDPostcodeTenancyTypes.get((String) previousYM3s[1]);
            HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTenancyTypes0;
            tClaimantIDPostcodeTenancyTypes0 = tClaimantIDPostcodeTenancyTypes.get((String) previousYM3s[2]);
            Object[] memberships;
            //memberships = Summary.getMembershipsID_TenancyType_PostcodeID(tClaimantIDPostcodeTenancyTypes00, tClaimantIDPostcodeTenancyTypes0, tClaimantIDPostcodeTenancyTypes1);
            memberships = Summary.getMemberships(
                    tClaimantIDPostcodeTenancyTypes00,
                    tClaimantIDPostcodeTenancyTypes0,
                    tClaimantIDPostcodeTenancyTypes1);
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
     * @param tClaimantIDTenancyTypes
     * @param tClaimantIDTenancyTypes1
     * @return
     */
    public static Object[] getCountsIDTenancyType(
            int nTT, String[] SHBEFilenames,
            ArrayList<Integer> include,
            String yM31,
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTenancyTypes,
            HashSet<ID_TenancyType> tClaimantIDTenancyTypes1) {
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
            HashSet<ID_TenancyType> tClaimantIDTenancyTypes00;
            tClaimantIDTenancyTypes00 = tClaimantIDTenancyTypes.get((String) previousYM3s[1]);
            HashSet<ID_TenancyType> tClaimantIDTenancyTypes0;
            tClaimantIDTenancyTypes0 = tClaimantIDTenancyTypes.get((String) previousYM3s[2]);
            Object[] memberships;
            //memberships = Summary.getMembershipsID_TenancyType(tClaimantIDTenancyTypes00, tClaimantIDTenancyTypes0, tClaimantIDTenancyTypes1);
            memberships = Summary.getMemberships(
                    tClaimantIDTenancyTypes00,
                    tClaimantIDTenancyTypes0,
                    tClaimantIDTenancyTypes1);
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

    /**
     * Loads SHBE Data from filename.
     *
     * @param filename
     * @param paymentType
     * @return Object[16] result {@code
     * result[0] is a TreeMap<String, DW_SHBE_Record> CTBRef, DRecords;
     * result[1] is a TreeMap<String, DW_SHBE_Record> CTBRef, SRecords without DRecords;
     * result[2] is a HashSet<DW_ID> tClaimantIDs;
     * result[3] is a HashSet<DW_ID> tPartnerIDs;
     * result[4] is a HashSet<DW_ID> tDependentsIDs;
     * result[5] is a HashSet<DW_ID> tNonDependentsIDs;
     * result[6] is a HashSet<DW_ID> allHouseholdIDs;
     * result[7] is a HashMap<DW_ID, Long> tClaimantIDToRecordIDLookup;
     * result[8] is a HashMap<DW_ID, String> tClaimantIDToPostcodeLookup;
     * result[9] is a HashMap<DW_ID, Integer> tClaimantIDToTenancyTypeLookup;
     * result[10] is a HashMap<String, DW_ID> CTBRefToClaimantIDLookup;
     * result[11] is a HashMap<DW_ID, String> tClaimantIDToCTBRefLookup;
     * result[12] is a HashMap<String, Integer> tLoadSummary;
     * result[13] is a HashSet<ID_TenancyType> tClaimantIDAndPostcode.
     * result[14] is a HashSet<ID_TenancyType> tClaimantIDAndTenancyType.
     * result[15] is a HashSet<ID_TenancyType> tClaimantIDAndPostcodeAndTenancyType.
     * }
     *
     */
    public Object[] getSHBEData(
            String filename,
            String paymentType) {
        System.out.println("Loading SHBE from " + filename);
        Object[] result = tDW_SHBE_Handler.loadInputData(
                DW_Files.getInputSHBEDir(),
                filename,
                paymentType,
                false);
        return result;
    }

    private void addToSummaryCompare2Times(
            int nTT,
            int nEG,
            int nPSI,
            HashMap<String, String> summary) {
        addToSummarySingleTime(nTT, nEG, nPSI, summary);
        double percentage;
        double AllCount;
        AllCount = HBCount + CTBCount;
        //All
        summary.put(
                sAllTotalCountTenancyTypeChangeClaimant,
                Integer.toString(AllTotalCountTenancyTypeChangeClaimant));
        if (AllCount > 0) {
            percentage = (AllTotalCountTenancyTypeChangeClaimant * 100.0d) / AllCount;
            summary.put(
                    sAllPercentageTenancyTypeChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAllPercentageTenancyTypeChangeClaimant,
                    s0);
        }
        // HB
        summary.put(
                sHBTotalCountPostcode0ValidPostcode1Valid,
                Integer.toString(HBTotalCountPostcode0ValidPostcode1Valid));
        if (HBCount > 0) {
            percentage = (HBTotalCountPostcode0ValidPostcode1Valid * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1Valid,
                    s0);
        }
        summary.put(
                sHBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged,
                Integer.toString(HBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged));
        if (HBCount > 0) {
            percentage = (HBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged,
                    s0);
        }
        summary.put(
                sHBTotalCountPostcode0ValidPostcode1ValidPostcodeChange,
                Integer.toString(HBTotalCountPostcode0ValidPostcode1ValidPostcodeChange));
        if (HBCount > 0) {
            percentage = (HBTotalCountPostcode0ValidPostcode1ValidPostcodeChange * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1ValidPostcodeChange,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1ValidPostcodeChange,
                    s0);
        }
        summary.put(
                sHBTotalCountPostcode0ValidPostcode1NotValid,
                Integer.toString(HBTotalCountPostcode0ValidPostcode1NotValid));
        if (HBCount > 0) {
            percentage = (HBTotalCountPostcode0ValidPostcode1NotValid * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1NotValid,
                    s0);
        }
        summary.put(
                sHBTotalCountPostcode0NotValidPostcode1Valid,
                Integer.toString(HBTotalCountPostcode0NotValidPostcode1Valid));
        if (HBCount > 0) {
            percentage = (HBTotalCountPostcode0NotValidPostcode1Valid * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1Valid,
                    s0);
        }
        summary.put(
                sHBTotalCountPostcode0NotValidPostcode1NotValid,
                Integer.toString(HBTotalCountPostcode0NotValidPostcode1NotValid));
        if (HBCount > 0) {
            percentage = (HBTotalCountPostcode0NotValidPostcode1NotValid * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1NotValid,
                    s0);
        }
        summary.put(
                sHBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                Integer.toString(HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged));
        if (HBCount > 0) {
            percentage = (HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    s0);
        }
        summary.put(
                sHBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged,
                Integer.toString(HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged));
        if (HBCount > 0) {
            percentage = (HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged,
                    s0);
        }
        summary.put(
                sHBTotalCountTenancyTypeChangeClaimant,
                Integer.toString(HBTotalCountTenancyTypeChangeClaimant));
        if (HBCount > 0) {
            percentage = (HBTotalCountTenancyTypeChangeClaimant * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentageTenancyTypeChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentageTenancyTypeChangeClaimant,
                    s0);
        }
        summary.put(
                sHBTotalCountTenancyTypeChangeClaimantHousing,
                Integer.toString(HBTenancyTypeChangeHousingCount));
        if (HBCount > 0) {
            percentage = (HBTenancyTypeChangeHousingCount * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentageTenancyTypeChangeClaimantHousing,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentageTenancyTypeChangeClaimantHousing,
                    s0);
        }
        summary.put(
                sHBTotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes,
                Integer.toString(HBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount));
        if (HBCount > 0) {
            percentage = (HBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentageSocialTenancyTypesToPrivateDeregulatedTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentageSocialTenancyTypesToPrivateDeregulatedTenancyTypes,
                    s0);
        }
        summary.put(
                sHBTotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes,
                Integer.toString(HBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount));
        if (HBCount > 0) {
            percentage = (HBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentagePrivateDeregulatedTenancyTypesToSocialTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentagePrivateDeregulatedTenancyTypesToSocialTenancyTypes,
                    s0);
        }
        summary.put(
                sHBTotalCountPostcodeChangeWithinSocialTenancyTypes,
                Integer.toString(HBPostcodeChangeWithinSocialTenancyTypesCount));
        if (HBCount > 0) {
            percentage = (HBPostcodeChangeWithinSocialTenancyTypesCount * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentagePostcodeChangeWithinSocialTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentagePostcodeChangeWithinSocialTenancyTypes,
                    s0);
        }
        summary.put(
                sHBTotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes,
                Integer.toString(HBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount));
        if (HBCount > 0) {
            percentage = (HBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentagePostcodeChangeWithinPrivateDeregulatedTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentagePostcodeChangeWithinPrivateDeregulatedTenancyTypes,
                    s0);
        }
        // CTB
        summary.put(
                sCTBTotalCountPostcode0ValidPostcode1Valid,
                Integer.toString(CTBTotalCountPostcode0ValidPostcode1Valid));
        if (CTBCount > 0) {
            percentage = (CTBTotalCountPostcode0ValidPostcode1Valid * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1Valid,
                    s0);
        }
        summary.put(
                sCTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged,
                Integer.toString(CTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged));
        if (CTBCount > 0) {
            percentage = (CTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged,
                    s0);
        }
        summary.put(
                sCTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged,
                Integer.toString(CTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged));
        if (CTBCount > 0) {
            percentage = (CTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged,
                    s0);
        }
        summary.put(
                sCTBTotalCountPostcode0ValidPostcode1NotValid,
                Integer.toString(CTBTotalCountPostcode0ValidPostcode1NotValid));
        if (CTBCount > 0) {
            percentage = (CTBTotalCountPostcode0ValidPostcode1NotValid * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1NotValid,
                    s0);
        }
        summary.put(
                sCTBTotalCountPostcode0NotValidPostcode1Valid,
                Integer.toString(CTBTotalCountPostcode0NotValidPostcode1Valid));
        if (CTBCount > 0) {
            percentage = (CTBTotalCountPostcode0NotValidPostcode1Valid * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1Valid,
                    s0);
        }
        summary.put(
                sCTBTotalCountPostcode0NotValidPostcode1NotValid,
                Integer.toString(CTBTotalCountPostcode0NotValidPostcode1NotValid));
        if (CTBCount > 0) {
            percentage = (CTBTotalCountPostcode0NotValidPostcode1NotValid * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1NotValid,
                    s0);
        }
        summary.put(
                sCTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                Integer.toString(CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged));
        if (CTBCount > 0) {
            percentage = (CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    s0);
        }
        summary.put(
                sCTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged,
                Integer.toString(CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged));
        if (CTBCount > 0) {
            percentage = (CTBTenancyTypeChangeCount * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged,
                    s0);
        }
        summary.put(
                sCTBTotalCountTenancyTypeChangeClaimant,
                Integer.toString(CTBTenancyTypeChangeCount));
        if (CTBCount > 0) {
            percentage = (CTBTenancyTypeChangeCount * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentageTenancyTypeChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentageTenancyTypeChangeClaimant,
                    s0);
        }
        summary.put(
                sCTBTotalCountTenancyTypeChangeClaimantHousing,
                Integer.toString(CTBTenancyTypeChangeHousingCount));
        if (CTBCount > 0) {
            percentage = (CTBTenancyTypeChangeHousingCount * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentageTenancyTypeChangeClaimantHousing,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentageTenancyTypeChangeClaimantHousing,
                    s0);
        }
        summary.put(
                sCTBTotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes,
                Integer.toString(CTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount));
        if (CTBCount > 0) {
            percentage = (CTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentageSocialTenancyTypesToPrivateDeregulatedTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentageSocialTenancyTypesToPrivateDeregulatedTenancyTypes,
                    s0);
        }
        summary.put(
                sCTBTotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes,
                Integer.toString(CTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount));
        if (CTBCount > 0) {
            percentage = (CTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentagePrivateDeregulatedTenancyTypesToSocialTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentagePrivateDeregulatedTenancyTypesToSocialTenancyTypes,
                    s0);
        }
        summary.put(
                sCTBTotalCountPostcodeChangeWithinSocialTenancyTypes,
                Integer.toString(CTBPostcodeChangeWithinSocialTenancyTypesCount));
        if (CTBCount > 0) {
            percentage = (CTBPostcodeChangeWithinSocialTenancyTypesCount * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentagePostcodeChangeWithinSocialTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentagePostcodeChangeWithinSocialTenancyTypes,
                    s0);
        }
        summary.put(
                sCTBTotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes,
                Integer.toString(CTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount));
        if (CTBCount > 0) {
            percentage = (CTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentagePostcodeChangeWithinPrivateDeregulatedTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentagePostcodeChangeWithinPrivateDeregulatedTenancyTypes,
                    s0);
        }
    }

    private void addToSummarySingleTimeRentArrears0(
            HashMap<String, String> summary) {
        summary.put(
                TotalRentArrearsString,
                "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                        BigDecimal.valueOf(totalRentArrears),
                        2, RoundingMode.HALF_UP));
        if (rentArrearsCount != 0.0d) {
            summary.put(
                    AverageRentArrearsString,
                    "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                            BigDecimal.valueOf(totalRentArrears / rentArrearsCount),
                            2, RoundingMode.HALF_UP));
        } else {
            summary.put(
                    AverageRentArrearsString,
                    s0);
        }
        if (greaterThan0RentArrearsCount != 0.0d) {
            summary.put(
                    GreaterThan0AverageRentArrearsString,
                    "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                            BigDecimal.valueOf(totalRentArrears / greaterThan0RentArrearsCount),
                            2, RoundingMode.HALF_UP));
        } else {
            summary.put(
                    GreaterThan0AverageRentArrearsString,
                    s0);
        }
    }

    private void addToSummarySingleTime(
            int nTT,
            int nEG,
            int nPSI,
            HashMap<String, String> summary) {
        addToSummarySingleTime0(summary);
        addToSummarySingleTimeDisability(nTT, summary);
        addToSummarySingleTimeDemographics(nEG, summary);
        addToSummarySingleTimeTenancyType(nTT, nEG, summary);
        addToSummarySingleTimePassportedStandardIndicator(nTT, nEG, nPSI, summary);
        addToSummarySingleTime1(summary);

    }

    private void addToSummarySingleTime0(
            HashMap<String, String> summary) {

        summary.put(
                sCountTT5or7,
                Long.toString(CountTT5or7));
        summary.put(
                sCountHBRefNullTT5or7,
                Long.toString(CountHBRefNullTT5or7));
        summary.put(
                sCountHBRefNullTTNot5or7,
                Long.toString(CountHBRefNullTTNot5or7));
        summary.put(
                sCountHBRefNotNullTT5or7,
                Long.toString(CountHBRefNotNullTT5or7));
        summary.put(
                sCountHBRefNotNullTTNot5or7,
                Long.toString(CountHBRefNotNullTTNot5or7));

        // All
        double AllCount;
        AllCount = HBCount + CTBCount;
        summary.put(sAllCount, Integer.toString((int) AllCount));
        summary.put(
                sAllTotalHouseholdSize,
                Long.toString(AllTotalHouseholdSize));
        double ave;
        // All HouseholdSize
        if (AllCount > 0) {
            ave = AllTotalHouseholdSize / AllCount;
            summary.put(
                    sAllAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAllAverageHouseholdSize,
                    s0);
        }
        // HB HouseholdSize
        summary.put(
                sHBTotalHouseholdSize,
                Long.toString(HBTotalHouseholdSize));
        if (HBCount > 0) {
            ave = HBTotalHouseholdSize / (double) HBCount;
            summary.put(
                    sHBAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBAverageHouseholdSize,
                    s0);
        }
        // CTB HouseholdSize
        summary.put(
                sCTBTotalHouseholdSize,
                Long.toString(CTBTotalHouseholdSize));
        if (CTBCount > 0) {
            ave = CTBTotalHouseholdSize / (double) CTBCount;
            summary.put(
                    sCTBAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBAverageHouseholdSize,
                    s0);
        }
    }

    private void addToSummarySingleTimePassportedStandardIndicator(
            int nTT,
            int nEG,
            int nPSI,
            HashMap<String, String> summary) {
        double AllCount;
        AllCount = HBCount + CTBCount;
        double ave;
        // PassportStandardIndicator
        for (int i = 0; i < nPSI; i++) {
            summary.put(
                    TotalAllPassportedStandardIndicatorCountString[i],
                    Long.toString(TotalAllPassportedStandardIndicatorCount[i]));
            if (AllCount > 0) {
                ave = (TotalAllPassportedStandardIndicatorCount[i] * 100.0d) / AllCount;
                summary.put(
                        PercentageAllPassportedStandardIndicatorCountString[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        PercentageAllPassportedStandardIndicatorCountString[i],
                        "0.0");
            }
            summary.put(
                    TotalHBPassportedStandardIndicatorCountString[i],
                    Long.toString(TotalHBPassportedStandardIndicatorCount[i]));
            if (HBCount > 0) {
                ave = (TotalHBPassportedStandardIndicatorCount[i] * 100.0d) / (double) HBCount;
                summary.put(
                        PercentageHBPassportedStandardIndicatorCountString[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        PercentageHBPassportedStandardIndicatorCountString[i],
                        "0.0");
            }
            summary.put(
                    TotalCTBPassportedStandardIndicatorCountString[i],
                    Long.toString(TotalCTBPassportedStandardIndicatorCount[i]));
            if (CTBCount > 0) {
                ave = (TotalCTBPassportedStandardIndicatorCount[i] * 100.0d) / (double) CTBCount;
                summary.put(
                        PercentageCTBPassportedStandardIndicatorCountString[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        PercentageCTBPassportedStandardIndicatorCountString[i],
                        s0);
            }
            for (int j = 1; j < nTT; j++) {
                summary.put(
                        TotalAllPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                        Long.toString(TotalAllPassportedStandardIndicatorByTenancyTypeCount[i][j]));
                if (AllCount > 0) {
                    ave = (TotalAllPassportedStandardIndicatorByTenancyTypeCount[i][j] * 100.0d) / AllCount;
                    summary.put(
                            PercentageAllPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
                } else {
                    summary.put(
                            PercentageAllPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                            s0);
                }
                summary.put(
                        TotalHBPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                        Long.toString(TotalHBPassportedStandardIndicatorByTenancyTypeCount[i][j]));
                if (HBCount > 0) {
                    ave = (TotalHBPassportedStandardIndicatorByTenancyTypeCount[i][j] * 100.0d) / (double) HBCount;
                    summary.put(
                            PercentageHBPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
                } else {
                    summary.put(
                            PercentageHBPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                            s0);
                }
                summary.put(
                        TotalCTBPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                        Long.toString(TotalCTBPassportedStandardIndicatorByTenancyTypeCount[i][j]));
                if (CTBCount > 0) {
                    ave = (TotalCTBPassportedStandardIndicatorByTenancyTypeCount[i][j] * 100.0d) / (double) CTBCount;
                    summary.put(
                            PercentageCTBPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
                } else {
                    summary.put(
                            PercentageCTBPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                            s0);
                }
            }
        }
    }

    private void addToSummarySingleTimeDisability(
            int nTT,
            HashMap<String, String> summary) {
        double AllCount = HBCount + CTBCount;
        double percentage;
        for (int i = 0; i < nTT; i++) {
            summary.put(
                    AllTotalDisabilityPremiumAwardByTenancyTypeCountString[i],
                    Integer.toString(AllTotalDisabilityPremiumAwardByTenancyTypeCount[i]));
            summary.put(
                    AllTotalSevereDisabilityPremiumAwardByTenancyTypeCountString[i],
                    Integer.toString(AllTotalSevereDisabilityPremiumAwardByTenancyTypeCount[i]));
            summary.put(
                    AllTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i],
                    Integer.toString(AllTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[i]));
            if (AllCount > 0) {
                percentage = (AllTotalDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / AllCount;
                summary.put(
                        AllPercentageDisabilityPremiumAwardByTenancyTypeCountString[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
                percentage = (AllTotalSevereDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / AllCount;
                summary.put(
                        AllPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
                percentage = (AllTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / AllCount;
                summary.put(
                        AllPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        AllPercentageDisabilityPremiumAwardByTenancyTypeCountString[i],
                        "0.0");
                summary.put(
                        AllPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i],
                        "0.0");
                summary.put(
                        AllPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i],
                        "0.0");
            }
            summary.put(
                    HBTotalDisabilityPremiumAwardByTenancyTypeCountString[i],
                    Integer.toString(HBTotalDisabilityPremiumAwardByTenancyTypeCount[i]));
            summary.put(
                    HBTotalSevereDisabilityPremiumAwardByTenancyTypeCountString[i],
                    Integer.toString(HBTotalSevereDisabilityPremiumAwardByTenancyTypeCount[i]));
            summary.put(
                    HBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i],
                    Integer.toString(HBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[i]));
            if (HBCount > 0) {
                percentage = (HBTotalDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / (double) HBCount;
                summary.put(
                        HBPercentageDisabilityPremiumAwardByTenancyTypeCountString[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
                percentage = (HBTotalSevereDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / (double) HBCount;
                summary.put(
                        HBPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
                percentage = (HBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / (double) HBCount;
                summary.put(
                        HBPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        HBPercentageDisabilityPremiumAwardByTenancyTypeCountString[i],
                        "0.0");
                summary.put(
                        HBPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i],
                        "0.0");
                summary.put(
                        HBPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i],
                        "0.0");
            }
            summary.put(
                    CTBTotalDisabilityPremiumAwardByTenancyTypeCountString[i],
                    Integer.toString(CTBTotalDisabilityPremiumAwardByTenancyTypeCount[i]));
            summary.put(
                    CTBTotalSevereDisabilityPremiumAwardByTenancyTypeCountString[i],
                    Integer.toString(CTBTotalSevereDisabilityPremiumAwardByTenancyTypeCount[i]));
            summary.put(
                    CTBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i],
                    Integer.toString(CTBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[i]));
            if (CTBCount > 0) {
                percentage = (CTBTotalDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / (double) CTBCount;
                summary.put(
                        CTBPercentageDisabilityPremiumAwardByTenancyTypeCountString[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
                percentage = (CTBTotalSevereDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / (double) CTBCount;
                summary.put(
                        CTBPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
                percentage = (CTBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / (double) CTBCount;
                summary.put(
                        CTBPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        CTBPercentageDisabilityPremiumAwardByTenancyTypeCountString[i],
                        "0.0");
                summary.put(
                        CTBPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i],
                        "0.0");
                summary.put(
                        CTBPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i],
                        "0.0");
            }
        }
    }

    private void addToSummarySingleTime1(
            HashMap<String, String> summary) {
        double ave;
        // WeeklyHBEntitlement
        summary.put(
                sTotalWeeklyHBEntitlement,
                BigDecimal.valueOf(TotalWeeklyHBEntitlement).toPlainString());
        summary.put(
                sTotalCountWeeklyHBEntitlementNonZero,
                Integer.toString(TotalWeeklyHBEntitlementNonZeroCount));
        if (TotalWeeklyHBEntitlementNonZeroCount > 0) {
            ave = TotalWeeklyHBEntitlement / (double) TotalWeeklyHBEntitlementNonZeroCount;
            summary.put(
                    sAverageWeeklyHBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAverageWeeklyHBEntitlement,
                    s0);
        }
        // WeeklyCTBEntitlement
        summary.put(
                sTotalWeeklyCTBEntitlement,
                BigDecimal.valueOf(TotalWeeklyCTBEntitlement).toPlainString());
        summary.put(
                sTotalCountWeeklyCTBEntitlementNonZero,
                Integer.toString(TotalWeeklyCTBEntitlementNonZeroCount));
        if (TotalWeeklyCTBEntitlementNonZeroCount > 0) {
            ave = TotalWeeklyCTBEntitlement / (double) TotalWeeklyCTBEntitlementNonZeroCount;
            summary.put(
                    sAverageWeeklyCTBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAverageWeeklyCTBEntitlement,
                    s0);
        }
        // WeeklyEligibleRentAmount
        summary.put(
                sTotalWeeklyEligibleRentAmount,
                BigDecimal.valueOf(TotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                sTotalCountWeeklyEligibleRentAmountNonZero,
                Integer.toString(TotalWeeklyEligibleRentAmountNonZeroCount));
        if (TotalWeeklyEligibleRentAmountNonZeroCount > 0) {
            ave = TotalWeeklyEligibleRentAmount / (double) TotalWeeklyEligibleRentAmountNonZeroCount;
            summary.put(
                    sAverageWeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAverageWeeklyEligibleRentAmount,
                    s0);
        }
        // WeeklyEligibleCouncilTaxAmount
        summary.put(
                sTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(TotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(
                sTotalCountWeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(TotalWeeklyEligibleCouncilTaxAmountNonZeroCount));
        if (TotalWeeklyEligibleCouncilTaxAmountNonZeroCount > 0) {
            ave = TotalWeeklyEligibleCouncilTaxAmount / (double) TotalWeeklyEligibleCouncilTaxAmountNonZeroCount;
            summary.put(
                    sAverageWeeklyEligibleCouncilTaxAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAverageWeeklyEligibleCouncilTaxAmount,
                    s0);
        }
        // ContractualRentAmount
        summary.put(
                sTotalContractualRentAmount,
                BigDecimal.valueOf(TotalContractualRentAmount).toPlainString());
        summary.put(
                sTotalCountContractualRentAmountNonZeroCount,
                Integer.toString(TotalContractualRentAmountNonZeroCount));
        if (TotalContractualRentAmountNonZeroCount > 0) {
            ave = TotalContractualRentAmount / (double) TotalContractualRentAmountNonZeroCount;
            summary.put(
                    sAverageContractualRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAverageContractualRentAmount,
                    s0);
        }
        // WeeklyAdditionalDiscretionaryPayment
        summary.put(
                sTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(TotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(TotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        if (TotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount > 0) {
            ave = TotalWeeklyAdditionalDiscretionaryPayment / (double) TotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
            summary.put(
                    sAverageWeeklyAdditionalDiscretionaryPayment,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAverageWeeklyAdditionalDiscretionaryPayment,
                    s0);
        }
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        summary.put(
                sTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        if (TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount > 0) {
            ave = TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability / (double) TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
            summary.put(
                    sAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    s0);
        }
        // Employed        
        summary.put(
                sHBTotalCountClaimantsEmployed,
                Integer.toString(TotalHBClaimantsEmployed));
        if (HBCount > 0) {
            ave = (TotalHBClaimantsEmployed * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentageClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentageClaimantsEmployed,
                    s0);
        }
        summary.put(
                sCTBTotalCountClaimantsEmployed,
                Integer.toString(TotalCTBClaimantsEmployed));
        if (CTBCount > 0) {
            ave = (TotalCTBClaimantsEmployed * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentageClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentageClaimantsEmployed,
                    s0);
        }
        // Self Employed
        summary.put(
                sHBTotalCountClaimantsSelfEmployed,
                Integer.toString(TotalHBClaimantsSelfEmployed));
        if (HBCount > 0) {
            ave = (TotalHBClaimantsSelfEmployed * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentageClaimantsSelfEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentageClaimantsSelfEmployed,
                    s0);
        }
        summary.put(
                sCTBTotalCountClaimantsSelfEmployed,
                Integer.toString(TotalCTBClaimantsSelfEmployed));
        if (CTBCount > 0) {
            ave = (TotalCTBClaimantsSelfEmployed * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentageClaimantsSelfEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentageClaimantsSelfEmployed,
                    s0);
        }
        // Students
        summary.put(
                sHBTotalCountClaimantsStudents,
                Integer.toString(TotalHBClaimantsStudents));
        if (HBCount > 0) {
            ave = (TotalHBClaimantsStudents * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentageClaimantsStudents,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentageClaimantsStudents,
                    s0);
        }
        summary.put(
                sCTBTotalCountClaimantsStudents,
                Integer.toString(TotalCTBClaimantsStudents));
        if (CTBCount > 0) {
            ave = (TotalCTBClaimantsStudents * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentageClaimantsStudents,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentageClaimantsStudents,
                    s0);
        }
        // LHACases
        summary.put(
                sHBTotalCountLHACases,
                Integer.toString(TotalHBLHACases));
        if (HBCount > 0) {
            ave = (TotalHBLHACases * 100.0d) / (double) HBCount;
            summary.put(
                    sHBPercentageLHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentageLHACases,
                    s0);
        }
        summary.put(
                sCTBTotalCountLHACases,
                Integer.toString(TotalCTBLHACases));
        if (CTBCount > 0) {
            ave = (TotalCTBLHACases * 100.0d) / (double) CTBCount;
            summary.put(
                    sCTBPercentageLHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentageLHACases,
                    s0);
        }
    }

    private void addToSummarySingleTimeDemographics(
            int nEG,
            HashMap<String, String> summary) {
        double AllCount = HBCount + CTBCount;
        // Ethnicity
        double percentage;
        for (int i = 1; i < nEG; i++) {
            int all;
            all = HBEthnicGroupCount[i] + CTBEthnicGroupCount[i];
            summary.put(
                    sAllTotalCountEthnicGroupClaimant[i],
                    Integer.toString(all));

            if (AllCount > 0) {
                percentage = (all * 100.0d) / AllCount;
                summary.put(
                        sAllPercentageEthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sAllPercentageEthnicGroupClaimant[i],
                        s0);
            }
            summary.put(
                    sHBTotalCountEthnicGroupClaimant[i],
                    Integer.toString(HBEthnicGroupCount[i]));
            if (HBCount > 0) {
                percentage = (HBEthnicGroupCount[i] * 100.0d) / (double) HBCount;
                summary.put(
                        sHBPercentageEthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sHBPercentageEthnicGroupClaimant[i],
                        s0);
            }
            summary.put(
                    sCTBTotalCountEthnicGroupClaimant[i],
                    Integer.toString(CTBEthnicGroupCount[i]));
            if (CTBCount > 0) {
                percentage = (CTBEthnicGroupCount[i] * 100.0d) / (double) CTBCount;
                summary.put(
                        sCTBPercentageEthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sCTBPercentageEthnicGroupClaimant[i],
                        s0);
            }
        }
    }

    private void addToSummarySingleTimeTenancyType(
            int nTT,
            int nEG,
            HashMap<String, String> summary) {
        double AllCount = HBCount + CTBCount;
        // Ethnicity
        double percentage;

        // TenancyType
        for (int i = 1; i < nTT; i++) {
            int all;
            all = HBTenancyTypeCount[i] + CTBTenancyTypeCount[i];
            summary.put(
                    sAllTotalCountTenancyTypeClaimant[i],
                    Integer.toString(all));
            if (AllCount > 0) {
                percentage = (all * 100.0d) / AllCount;
                summary.put(
                        sAllPercentageTenancyTypeClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sAllPercentageTenancyTypeClaimant[i],
                        s0);
            }
            summary.put(
                    sHBTotalCountTenancyTypeClaimant[i],
                    Integer.toString(HBTenancyTypeCount[i]));
            if (HBCount > 0) {
                percentage = (HBTenancyTypeCount[i] * 100.0d) / (double) HBCount;
                summary.put(
                        sHBPercentageTenancyTypeClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sHBPercentageTenancyTypeClaimant[i],
                        s0);
            }
            summary.put(
                    sCTBTotalCountTenancyTypeClaimant[i],
                    Integer.toString(CTBTenancyTypeCount[i]));
            if (CTBCount > 0) {
                percentage = (CTBTenancyTypeCount[i] * 100.0d) / (double) CTBCount;
                summary.put(
                        sCTBPercentageTenancyTypeClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), 2, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sCTBPercentageTenancyTypeClaimant[i],
                        s0);
            }
        }
        summary.put(
                sAllTotalCountTenancyType8InLS277NSClaimant,
                Integer.toString(HBTenancyType8InLS277NSClaimantCount + CTBTenancyType8InLS277NSClaimantCount));
        summary.put(
                sAllPostcodeValidFormatCount,
                Integer.toString(HBPostcodeValidFormatCount + CTBPostcodeValidFormatCount));
        summary.put(
                sAllPostcodeValidCount,
                Integer.toString(HBPostcodeValidCount + CTBPostcodeValidCount));
        // HB
        summary.put(sHBCount, Integer.toString(HBCount));
        summary.put(
                sHBTotalCountTenancyType8InLS277NSClaimant,
                Integer.toString(HBTenancyType8InLS277NSClaimantCount));
        summary.put(
                sHBPostcodeValidFormatCount,
                Integer.toString(HBPostcodeValidFormatCount));
        summary.put(
                sHBPostcodeValidCount,
                Integer.toString(HBPostcodeValidCount));
        // CTB
        summary.put(sCTBCount, Integer.toString(CTBCount));
        summary.put(
                sCTBTotalCountTenancyType8InLS277NSClaimant,
                Integer.toString(CTBTenancyType8InLS277NSClaimantCount));
        summary.put(
                sCTBPostcodeValidFormatCount,
                Integer.toString(CTBPostcodeValidFormatCount));
        summary.put(
                sCTBPostcodeValidCount,
                Integer.toString(CTBPostcodeValidCount));
    }

    private void doCompare2TimesCounts(
            DW_SHBE_D_Record D_Record,
            String CTBRef,
            HashMap<String, DW_ID> CTBRefID0,
            HashMap<String, DW_ID> CTBRefID1,
            HashMap<DW_ID, Integer> tIDByTenancyType0,
            HashMap<DW_ID, Integer> tIDByTenancyType1,
            HashMap<DW_ID, String> tIDByPostcode0,
            HashMap<DW_ID, String> tIDByPostcode1,
            String yM30v,
            String yM31v) {
        doSingleTimeCount(
                D_Record, CTBRef,
                CTBRefID0,
                tIDByTenancyType0,
                tIDByPostcode0,
                yM30v);
        DW_ID tID0;
        tID0 = CTBRefID0.get(CTBRef);
        String postcode0 = null;
        boolean isValidPostcode0;
        isValidPostcode0 = false;
        Integer tenancyType0 = 0;
        if (tID0 != null) {
            postcode0 = tIDByPostcode0.get(tID0);
            if (postcode0 != null) {
                isValidPostcode0 = DW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
            }
            tenancyType0 = tIDByTenancyType0.get(tID0);
            if (tenancyType0 == null) {
                tenancyType0 = 0;
            }
        }
        DW_ID tID1;
        tID1 = CTBRefID1.get(CTBRef);
        boolean isValidPostcode1;
        isValidPostcode1 = false;
        String postcode1 = null;
        Integer tenancyType1 = 0;
        if (tID1 != null) {
            postcode1 = tIDByPostcode1.get(tID1);
            if (postcode1 != null) {
                isValidPostcode1 = DW_Postcode_Handler.isValidPostcode(yM31v, postcode1);
            }
            tenancyType1 = tIDByTenancyType1.get(tID1);
            if (tenancyType1 == null) {
                tenancyType1 = 0;
            }
        }
        if (DW_SHBE_Handler.isHBClaim(D_Record)) {
            doCompare2TimesHBCount(
                    tenancyType0,
                    postcode0,
                    isValidPostcode1,
                    tenancyType1,
                    postcode1,
                    isValidPostcode0);
        }
        if (DW_SHBE_Handler.isCTBOnlyClaim(D_Record)) {
            doCompare2TimesCTBCount(
                    tenancyType0,
                    postcode0,
                    isValidPostcode1,
                    tenancyType1,
                    postcode1,
                    isValidPostcode0);
        }
    }

    private void doSingleTimeCount(
            DW_SHBE_D_Record D_Record,
            String CTBRef,
            HashMap<String, DW_ID> CTBRefID0,
            HashMap<DW_ID, Integer> tIDByTenancyType0,
            HashMap<DW_ID, String> tIDByPostcode0,
            String yM30v) {
        int ClaimantsEthnicGroup0;
        ClaimantsEthnicGroup0 = D_Record.getClaimantsEthnicGroup();
        DW_ID tID0;
        tID0 = CTBRefID0.get(CTBRef);
        String postcode0 = null;
        Integer tenancyType0 = 0;
        if (tID0 != null) {
            tenancyType0 = tIDByTenancyType0.get(tID0);
            if (tenancyType0 == null) {
                tenancyType0 = 0;
            }
            postcode0 = tIDByPostcode0.get(tID0);
        }
        if (tenancyType0 == 5 || tenancyType0 == 7) {
                CountTT5or7++;
        }
        if (D_Record.getHousingBenefitClaimReferenceNumber().isEmpty()) {
            if (tenancyType0 == 5 || tenancyType0 == 7) {
                CountHBRefNullTT5or7++;
            } else {
                CountHBRefNullTTNot5or7++;
            }
        } else {
            if (tenancyType0 == 5 || tenancyType0 == 7) {
                CountHBRefNotNullTT5or7++;
            } else {
                CountHBRefNotNullTTNot5or7 ++;
            }
        }

        int DisabilityPremiumAwarded;
        DisabilityPremiumAwarded = D_Record.getDisabilityPremiumAwarded();
        if (DisabilityPremiumAwarded == 1) {
            AllTotalDisabilityPremiumAwardByTenancyTypeCount[tenancyType0]++;
        }
        int SevereDisabilityPremiumAwarded;
        SevereDisabilityPremiumAwarded = D_Record.getSevereDisabilityPremiumAwarded();
        if (SevereDisabilityPremiumAwarded == 1) {
            AllTotalSevereDisabilityPremiumAwardByTenancyTypeCount[tenancyType0]++;
        }
        int EnhancedDisabilityPremiumAwarded;
        EnhancedDisabilityPremiumAwarded = D_Record.getEnhancedDisabilityPremiumAwarded();
        if (EnhancedDisabilityPremiumAwarded == 1) {
            AllTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[tenancyType0]++;
        }
        int PassportedStandardIndicator;
        PassportedStandardIndicator = D_Record.getPassportedStandardIndicator();
        TotalAllPassportedStandardIndicatorCount[PassportedStandardIndicator]++;
        TotalAllPassportedStandardIndicatorByTenancyTypeCount[PassportedStandardIndicator][tenancyType0]++;
        long HouseholdSize;
        HouseholdSize = DW_SHBE_Handler.getHouseholdSize(D_Record);
        AllTotalHouseholdSize += HouseholdSize;
        int WeeklyHousingBenefitEntitlement;
        WeeklyHousingBenefitEntitlement = D_Record.getWeeklyHousingBenefitEntitlement();
        if (WeeklyHousingBenefitEntitlement > 0) {
            TotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
            TotalWeeklyHBEntitlementNonZeroCount++;
        }
        int WeeklyCouncilTaxBenefitBenefitEntitlement;
        WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
        if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
            TotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
            TotalWeeklyCTBEntitlementNonZeroCount++;
        }
        int WeeklyEligibleRentAmount;
        WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
        if (WeeklyEligibleRentAmount > 0) {
            TotalWeeklyEligibleRentAmount += WeeklyEligibleRentAmount;
            TotalWeeklyEligibleRentAmountNonZeroCount++;
        }
        int WeeklyEligibleCouncilTaxAmount;
        WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
        if (WeeklyEligibleCouncilTaxAmount > 0) {
            TotalWeeklyEligibleCouncilTaxAmount += WeeklyEligibleCouncilTaxAmount;
            TotalWeeklyEligibleCouncilTaxAmountNonZeroCount++;
        }
        int ContractualRentAmount;
        ContractualRentAmount = D_Record.getContractualRentAmount();
        if (ContractualRentAmount > 0) {
            TotalContractualRentAmount += ContractualRentAmount;
            TotalContractualRentAmountNonZeroCount++;
        }
        int WeeklyAdditionalDiscretionaryPayment;
        WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
        if (WeeklyAdditionalDiscretionaryPayment > 0) {
            TotalWeeklyAdditionalDiscretionaryPayment += WeeklyAdditionalDiscretionaryPayment;
            TotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount++;
        }
        int WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
        WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
        if (WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability > 0) {
            TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability += WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
            TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount++;
        }
        if (DW_SHBE_Handler.isHBClaim(D_Record)) {
            if (DisabilityPremiumAwarded == 1) {
                HBTotalDisabilityPremiumAwardByTenancyTypeCount[tenancyType0]++;
            }
            if (SevereDisabilityPremiumAwarded == 1) {
                HBTotalSevereDisabilityPremiumAwardByTenancyTypeCount[tenancyType0]++;
            }
            if (EnhancedDisabilityPremiumAwarded == 1) {
                HBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[tenancyType0]++;
            }
            TotalHBPassportedStandardIndicatorCount[PassportedStandardIndicator]++;
            TotalHBPassportedStandardIndicatorByTenancyTypeCount[PassportedStandardIndicator][tenancyType0]++;
            //if (HBRef.equalsIgnoreCase(CTBRef)) {
            HBTotalHouseholdSize += HouseholdSize;
            int ClaimantsNetWeeklyIncomeFromEmployment;
            ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
            if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
                TotalHBClaimantsEmployed++;
            }
            int ClaimantsNetWeeklyIncomeFromSelfEmployment;
            ClaimantsNetWeeklyIncomeFromSelfEmployment = D_Record.getClaimantsNetWeeklyIncomeFromSelfEmployment();
            if (ClaimantsNetWeeklyIncomeFromSelfEmployment > 0) {
                TotalHBClaimantsSelfEmployed++;
            }
            String ClaimantsStudentIndicator;
            ClaimantsStudentIndicator = D_Record.getClaimantsStudentIndicator();
            if (ClaimantsStudentIndicator != null) {
                if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                    TotalHBClaimantsStudents++;
                }
            }
            String LHARegulationsApplied;
            LHARegulationsApplied = D_Record.getLHARegulationsApplied();
            if (LHARegulationsApplied != null) {
                if (LHARegulationsApplied.equalsIgnoreCase("1")
                        || LHARegulationsApplied.equalsIgnoreCase("Yes")) {
                    TotalHBLHACases++;
                }
            }
            doSingleTimeHBCount(
                    ClaimantsEthnicGroup0,
                    tenancyType0,
                    postcode0,
                    yM30v);
        }
        if (DW_SHBE_Handler.isCTBOnlyClaim(D_Record)) {
            if (DisabilityPremiumAwarded == 1) {
                CTBTotalDisabilityPremiumAwardByTenancyTypeCount[tenancyType0]++;
            }
            if (SevereDisabilityPremiumAwarded == 1) {
                CTBTotalSevereDisabilityPremiumAwardByTenancyTypeCount[tenancyType0]++;
            }
            if (EnhancedDisabilityPremiumAwarded == 1) {
                CTBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[tenancyType0]++;
            }
            TotalCTBPassportedStandardIndicatorCount[PassportedStandardIndicator]++;
            TotalCTBPassportedStandardIndicatorByTenancyTypeCount[PassportedStandardIndicator][tenancyType0]++;
            CTBTotalHouseholdSize += HouseholdSize;
            int ClaimantsNetWeeklyIncomeFromEmployment;
            ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
            if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
                TotalCTBClaimantsEmployed++;
            }
            int ClaimantsNetWeeklyIncomeFromSelfEmployment;
            ClaimantsNetWeeklyIncomeFromSelfEmployment = D_Record.getClaimantsNetWeeklyIncomeFromSelfEmployment();
            if (ClaimantsNetWeeklyIncomeFromSelfEmployment > 0) {
                TotalCTBClaimantsSelfEmployed++;
            }
            String ClaimantsStudentIndicator;
            ClaimantsStudentIndicator = D_Record.getClaimantsStudentIndicator();
            if (ClaimantsStudentIndicator != null) {
                if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                    TotalCTBClaimantsStudents++;
                }
            }
            String LHARegulationsApplied;
            LHARegulationsApplied = D_Record.getLHARegulationsApplied();
            if (LHARegulationsApplied != null) {
                if (LHARegulationsApplied.equalsIgnoreCase("1")) {
                    TotalCTBLHACases++;
                }
            }
            doSingleTimeCTBCount(
                    ClaimantsEthnicGroup0,
                    tenancyType0,
                    postcode0,
                    yM30v);
        }
    }

    private void doSingleTimeRentArrearsCount(DW_UnderOccupiedReport_Record UORec) {
        Double totalRA;
        totalRA = UORec.getTotalRentArrears();
        if (totalRA != null) {
            totalRentArrears += totalRA;
            rentArrearsCount += 1.0d;
            if (totalRA > 0.0d) {
                greaterThan0RentArrearsCount++;
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
                    HBTotalCountPostcode0ValidPostcode1ValidPostcodeChange++;
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
            if (!(tenancyType0 == 0 || tenancyType1 == 0)) {
                HBTotalCountTenancyTypeChangeClaimant++;
                AllTotalCountTenancyTypeChangeClaimant++;
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
                    HBTenancyTypeChangeHousingCount++;
                }
            }
            if ((tenancyType0 == 1
                    || tenancyType0 == 4)
                    && tenancyType1 == 3) {
                HBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount++;
            }
            if (tenancyType0 == 3
                    && (tenancyType1 == 1
                    || tenancyType1 == 4)) {
                HBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount++;
            }
        }
        if ((tenancyType0 == 1 && tenancyType1 == 1)
                || (tenancyType0 == 1 && tenancyType1 == 4)
                || (tenancyType0 == 4 && tenancyType1 == 1)
                || (tenancyType0 == 4 && tenancyType1 == 4)) {
            if (isValidPostcode0 && isValidPostcode1) {
                if (!postcode0.equalsIgnoreCase(postcode1)) {
                    HBPostcodeChangeWithinSocialTenancyTypesCount++;
                }
            }
        }
        if (tenancyType0 == 3 && tenancyType1 == 3) {
            if (isValidPostcode0 && isValidPostcode1) {
                if (!postcode0.equalsIgnoreCase(postcode1)) {
                    HBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount++;
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
            Integer tTT,
            String tP,
            String yM3v) {
        HBCount++;
        HBEthnicGroupCount[tEG]++;
        HBTenancyTypeCount[tTT]++;
        if (tTT == 8) {
            if (tP != null) {
                if (tP.equalsIgnoreCase(postcodeLS277NS)) {
                    HBTenancyType8InLS277NSClaimantCount++;
                }
            }
        }
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
            if (!(tenancyType0 == 0 || tenancyType1 == 0)) {
                CTBTenancyTypeChangeCount++;
                AllTotalCountTenancyTypeChangeClaimant++;
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
                    CTBTenancyTypeChangeHousingCount++;
                }
            }
            if ((tenancyType0 == 1 || tenancyType0 == 4)
                    && (tenancyType1 == 3 || tenancyType1 == 3)) {
                CTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount++;
            }
            if ((tenancyType0 == 3 || tenancyType0 == 6)
                    && (tenancyType1 == 1 || tenancyType1 == 4)) {
                CTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount++;
            }
        }
        if ((tenancyType0 == 1 && tenancyType1 == 1)
                || (tenancyType0 == 1 && tenancyType1 == 4)
                || (tenancyType0 == 4 && tenancyType1 == 1)
                || (tenancyType0 == 4 && tenancyType1 == 4)) {
            if (isValidPostcode0 && isValidPostcode1) {
                if (!postcode0.equalsIgnoreCase(postcode1)) {
                    CTBPostcodeChangeWithinSocialTenancyTypesCount++;
                }
            }
        }
        if ((tenancyType0 == 3 && tenancyType1 == 3)
                || (tenancyType0 == 6 && tenancyType1 == 6)
                || (tenancyType0 == 3 && tenancyType1 == 6)
                || (tenancyType0 == 6 && tenancyType1 == 3)) {
            if (isValidPostcode0 && isValidPostcode1) {
                if (!postcode0.equalsIgnoreCase(postcode1)) {
                    CTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount++;
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
    private void doSingleTimeCTBCount(
            int tEG,
            Integer tTT,
            String tP,
            String yM3v) {
        CTBCount++;
        CTBEthnicGroupCount[tEG]++;
        CTBTenancyTypeCount[tTT]++;
        if (tTT == 8) {
            if (tP != null) {
                if (tP.equalsIgnoreCase(postcodeLS277NS)) {
                    CTBTenancyType8InLS277NSClaimantCount++;
                }
            }
        }
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
     * @return
     */
    public TreeMap<String, HashMap<String, String>> getSummaryTable(
            String[] SHBEFilenames,
            ArrayList<Integer> include,
            boolean forceNewSummaries,
            String paymentType,
            int nTT,
            int nEG,
            int nPSI
    ) {
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
        Object[] tSHBEData0;
        tSHBEData0 = getSHBEData(filename0, paymentType);
        /*
         * result[0] is a TreeMap<String, DW_SHBE_Record> CTBRef, DRecords;
         * result[1] is a TreeMap<String, DW_SHBE_Record> CTBRef, SRecords without DRecords;
         * result[2] is a HashSet<DW_ID> tClaimantIDs;
         * result[3] is a HashSet<DW_ID> tPartnerIDs;
         * result[4] is a HashSet<DW_ID> tDependentsIDs;
         * result[5] is a HashSet<DW_ID> tNonDependentsIDs;
         * result[6] is a HashSet<DW_ID> allHouseholdIDs;
         * result[7] is a HashMap<DW_ID, Long> tClaimantIDToRecordIDLookup;
         * result[8] is a HashMap<DW_ID, String> tClaimantIDToPostcodeLookup;
         * result[9] is a HashMap<DW_ID, Integer> tClaimantIDToTenancyTypeLookup;
         * result[10] is a HashMap<String, DW_ID> CTBRefToClaimantIDLookup;
         * result[11] is a HashMap<DW_ID, String> tClaimantIDToCTBRefLookup;
         * result[12] is a HashMap<String, Integer> tLoadSummary;
         * result[13] is a HashSet<ID_TenancyType> tClaimantIDAndPostcode.
         * result[14] is a HashSet<ID_TenancyType> tClaimantIDAndTenancyType.
         * result[15] is a HashSet<ID_TenancyType> tClaimantIDAndPostcodeAndTenancyType.
         */
        // These could be returned to save time recreating them for other includes.
        // This would involve feeding them in to the method too per se.
        String yM30v;
        yM30v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM30);
        String key;
        key = DW_SHBE_Handler.getYearMonthNumber(SHBEFilenames[i]);
        HashMap<String, String> summary;
        summary = result.get(key);
        TreeMap<String, HashSet<DW_ID>> tClaimantIDs;
        tClaimantIDs = new TreeMap<String, HashSet<DW_ID>>();
        TreeMap<String, HashSet<DW_ID>> AllIDs;
        AllIDs = new TreeMap<String, HashSet<DW_ID>>();
        TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes;
        tClaimantIDPostcodes = new TreeMap<String, HashSet<ID_PostcodeID>>();
        TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTenancyTypes;
        tClaimantIDTenancyTypes = new TreeMap<String, HashSet<ID_TenancyType>>();
        TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTenancyTypes;
        tClaimantIDPostcodeTenancyTypes = new TreeMap<String, HashSet<ID_TenancyType_PostcodeID>>();
        TreeMap<String, DW_SHBE_Record> tDRecords0;
        tDRecords0 = (TreeMap<String, DW_SHBE_Record>) tSHBEData0[0];
        HashSet<DW_ID> tClaimantIDs0;
        tClaimantIDs0 = (HashSet<DW_ID>) tSHBEData0[2];
        tClaimantIDs.put(yM30, tClaimantIDs0);
        HashSet<DW_ID> AllIDs0;
        AllIDs0 = (HashSet<DW_ID>) tSHBEData0[6];
        AllIDs.put(yM30, AllIDs0);
        HashMap<DW_ID, String> tIDByPostcode0;
        tIDByPostcode0 = (HashMap<DW_ID, String>) tSHBEData0[8];
        //tIDByPostcode0 = loadIDByPostcode(loadData, filename, i);
        HashMap<DW_ID, Integer> tIDByTenancyType0;
        tIDByTenancyType0 = (HashMap<DW_ID, Integer>) tSHBEData0[9];
        //tIDByTenancyType0 = loadIDByTenancyType(loadData, filename, i);
        HashMap<String, DW_ID> CTBRefToIDLookup0;
        CTBRefToIDLookup0 = (HashMap<String, DW_ID>) tSHBEData0[10];
        //tCTBRefID0 = loadCTBRefByID(loadData, filename, i);        
        HashMap<String, Integer> tLoadSummary;
        tLoadSummary = (HashMap<String, Integer>) tSHBEData0[12];
        addToSummary(summary, tLoadSummary);
        HashSet<ID_PostcodeID> tClaimantIDPostcodes0;
        tClaimantIDPostcodes0 = (HashSet<ID_PostcodeID>) tSHBEData0[13];
        tClaimantIDPostcodes.put(yM30, tClaimantIDPostcodes0);
        HashSet<ID_TenancyType> tClaimantIDTenancyType0;
        tClaimantIDTenancyType0 = (HashSet<ID_TenancyType>) tSHBEData0[14];
        tClaimantIDTenancyTypes.put(yM30, tClaimantIDTenancyType0);
        HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTenancyTypes0;
        tClaimantIDPostcodeTenancyTypes0 = (HashSet<ID_TenancyType_PostcodeID>) tSHBEData0[15];
        tClaimantIDPostcodeTenancyTypes.put(yM30, tClaimantIDPostcodeTenancyTypes0);
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
                    D_Record, CTBRef,
                    CTBRefToIDLookup0,
                    tIDByTenancyType0,
                    tIDByPostcode0,
                    yM30v);
        }
        summary.put(SHBEFilename1String, filename0); // This looks odd but is right!
        HashMap<String, BigDecimal> incomeAndRentSummary0;
        incomeAndRentSummary0 = DW_SHBE_Handler.getIncomeAndRentSummary(
                tSHBEData0,
                filename0,
                paymentType,
                null,
                false,
                forceNewSummaries);
        addToSummarySingleTimeIncomeAndRent0(
                summary,
                incomeAndRentSummary0);
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
            Object[] tSHBEData1;
            tSHBEData1 = getSHBEData(filename1, paymentType);
            /*
             * result[0] is a TreeMap<String, DW_SHBE_Record> CTBRef, DRecords;
             * result[1] is a TreeMap<String, DW_SHBE_Record> CTBRef, SRecords without DRecords;
             * result[2] is a HashSet<DW_ID> tClaimantIDs;
             * result[3] is a HashSet<DW_ID> tPartnerIDs;
             * result[4] is a HashSet<DW_ID> tDependentsIDs;
             * result[5] is a HashSet<DW_ID> tNonDependentsIDs;
             * result[6] is a HashSet<DW_ID> allHouseholdIDs;
             * result[7] is a HashMap<DW_ID, Long> tClaimantIDToRecordIDLookup;
             * result[8] is a HashMap<DW_ID, String> tClaimantIDToPostcodeLookup;
             * result[9] is a HashMap<DW_ID, Integer> tClaimantIDToTenancyTypeLookup;
             * result[10] is a HashMap<String, DW_ID> CTBRefToClaimantIDLookup;
             * result[11] is a HashMap<DW_ID, String> tClaimantIDToCTBRefLookup;
             * result[12] is a HashMap<String, Integer> tLoadSummary;
             * result[13] is a HashSet<ID_TenancyType> tClaimantIDAndPostcode.
             * result[14] is a HashSet<ID_TenancyType> tClaimantIDAndTenancyType.
             * result[15] is a HashSet<ID_TenancyType> tClaimantIDAndPostcodeAndTenancyType.
             */
            TreeMap<String, DW_SHBE_Record> tDRecords1;
            tDRecords1 = (TreeMap<String, DW_SHBE_Record>) tSHBEData1[0];
            HashSet<DW_ID> tClaimantIDs1;
            tClaimantIDs1 = (HashSet<DW_ID>) tSHBEData1[2];
            tClaimantIDs.put(yM31, tClaimantIDs1);
            HashSet<DW_ID> AllIDs1;
            AllIDs1 = (HashSet<DW_ID>) tSHBEData1[6];
            AllIDs.put(yM31, AllIDs1);
            HashMap<DW_ID, String> tIDByPostcode1;
            tIDByPostcode1 = (HashMap<DW_ID, String>) tSHBEData1[8];
            HashMap<DW_ID, Integer> tIDByTenancyType1;
            tIDByTenancyType1 = (HashMap<DW_ID, Integer>) tSHBEData1[9];
            HashMap<String, DW_ID> CTBRefToIDLookup1;
            CTBRefToIDLookup1 = (HashMap<String, DW_ID>) tSHBEData1[10];
            HashSet<ID_PostcodeID> tClaimantIDPostcodes1;
            tClaimantIDPostcodes1 = (HashSet<ID_PostcodeID>) tSHBEData1[13];
            tClaimantIDPostcodes.put(yM31, tClaimantIDPostcodes1);
            HashSet<ID_TenancyType> tClaimantIDTenancyType1;
            tClaimantIDTenancyType1 = (HashSet<ID_TenancyType>) tSHBEData1[14];
            tClaimantIDTenancyTypes.put(yM31, tClaimantIDTenancyType1);
            HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTenancyTypes1;
            tClaimantIDPostcodeTenancyTypes1 = (HashSet<ID_TenancyType_PostcodeID>) tSHBEData1[15];
            tClaimantIDPostcodeTenancyTypes.put(yM31, tClaimantIDPostcodeTenancyTypes1);

            key = DW_SHBE_Handler.getYearMonthNumber(filename1);
            summary = result.get(key);
            // Counters
            tLoadSummary = (HashMap<String, Integer>) tSHBEData1[12];
            addToSummary(summary, tLoadSummary);
            HashMap<String, BigDecimal> incomeAndRentSummary1;
            incomeAndRentSummary1 = DW_SHBE_Handler.getIncomeAndRentSummary(
                    tSHBEData1,
                    filename1,
                    paymentType,
                    null,
                    false,
                    forceNewSummaries);
            addToSummarySingleTimeIncomeAndRent0(
                    summary,
                    incomeAndRentSummary1);
            initCounts(nTT, nEG, nPSI);
            // Loop over CTBRefToNationalInsuranceNumberIDLookup1
            ite = CTBRefToIDLookup1.keySet().iterator();
            while (ite.hasNext()) {
                String CTBRef;
                CTBRef = ite.next();
                DW_SHBE_D_Record D_Record;
                D_Record = tDRecords1.get(CTBRef).getDRecord();
                doCompare2TimesCounts(
                        D_Record,
                        CTBRef,
                        CTBRefToIDLookup0,
                        CTBRefToIDLookup1,
                        tIDByTenancyType0,
                        tIDByTenancyType1,
                        tIDByPostcode0,
                        tIDByPostcode1,
                        yM30v,
                        yM31v);
            }
            summary.put(SHBEFilename00String, filename00);
            summary.put(SHBEFilename0String, filename0);
            summary.put(SHBEFilename1String, filename1);

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
                    tClaimantIDTenancyTypes,
                    tClaimantIDTenancyType1,
                    tClaimantIDPostcodeTenancyTypes,
                    tClaimantIDPostcodeTenancyTypes1);
            filename00 = filename0;
            filename0 = filename1;
            tSHBEData0 = tSHBEData1;
            tDRecords0 = tDRecords1;
            tIDByPostcode0 = tIDByPostcode1;
            tIDByTenancyType0 = tIDByTenancyType1;
            CTBRefToIDLookup0 = CTBRefToIDLookup1;
            yM30 = yM31;
            yM30v = yM31v;
            // Not used at present. incomeAndRentSummary0 = incomeAndRentSummary1;
        }
        return result;
    }

    public void addToSummary(
            HashMap<String, String> summary,
            HashMap<String, Integer> tLoadSummary) {
        summary.put(
                "LineCount",
                Integer.toString(tLoadSummary.get("lineCount")));
        summary.put(
                "LineNotLoadedCount",
                Integer.toString(tLoadSummary.get("tRecordIDsNotLoaded.size()")));
        summary.put(
                "DRecordCount",
                Integer.toString(tLoadSummary.get("countDRecords")));
        summary.put(
                "SRecordCount",
                Integer.toString(tLoadSummary.get("countSRecords")));
        summary.put(
                "SRecordWithoutDRecordCount",
                Integer.toString(tLoadSummary.get("countOfSRecordsWithoutDRecord")));
        summary.put(
                "UniqueClaimantNationalInsuranceNumberCount",
                Integer.toString(tLoadSummary.get("uniqueClaimantNationalInsuranceNumberCount")));
        summary.put(
                "UniquePartnerNationalInsuranceNumberCount",
                Integer.toString(tLoadSummary.get("uniquePartnerNationalInsuranceNumbersCount")));
        summary.put(
                "UniqueDependentNationalInsuranceNumberCount",
                Integer.toString(tLoadSummary.get("uniqueDependentsNationalInsuranceNumbersCount")));
        summary.put(
                "UniqueNonDependentNationalInsuranceNumberCount",
                Integer.toString(tLoadSummary.get("uniqueNon-DependentsNationalInsuranceNumbersCount")));
        summary.put(
                "UniqueAllHouseholdNationalInsuranceNumberCount",
                Integer.toString(tLoadSummary.get("uniqueAllHouseholdNationalInsuranceNumbersCount")));

    }

    public void doCompare3TimesCounts(
            HashMap<String, String> summary,
            int nTT,
            String[] SHBEFilenames,
            ArrayList<Integer> include,
            String yM31,
            TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes,
            HashSet<ID_PostcodeID> tClaimantIDPostcodes1,
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTenancyTypes,
            HashSet<ID_TenancyType> tClaimantIDTenancyTypes1,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTenancyTypes,
            HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTenancyTypes1
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
        Object[] countsIDTenancyType = getCountsIDTenancyType(
                nTT,
                SHBEFilenames,
                include,
                yM31,
                tClaimantIDTenancyTypes,
                tClaimantIDTenancyTypes1);
        Integer[] mainCounts;
        mainCounts = (Integer[]) countsIDTenancyType[0];
        Integer[] IIITTCounts;
        Integer[] IOITTCounts;
        Integer[] OIOTTCounts;
        IIITTCounts = (Integer[]) countsIDTenancyType[1];
        IOITTCounts = (Integer[]) countsIDTenancyType[2];
        OIOTTCounts = (Integer[]) countsIDTenancyType[3];
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
                        SameTenancyIIITenancyTypeString[TT],
                        null);
                summary.put(
                        SameTenancyIOITenancyTypeString[TT],
                        null);
                summary.put(
                        SameTenancyOIOTenancyTypeString[TT],
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
                        SameTenancyIIITenancyTypeString[TT],
                        "" + IIITTCounts[TT]);
                summary.put(
                        SameTenancyIOITenancyTypeString[TT],
                        "" + IOITTCounts[TT]);
                summary.put(
                        SameTenancyOIOTenancyTypeString[TT],
                        "" + OIOTTCounts[TT]);
            }
        }
        Object[] countsIDPostcodeTenancyType = getCountsIDPostcodeTenancyType(
                nTT,
                SHBEFilenames,
                include,
                yM31,
                tClaimantIDPostcodeTenancyTypes,
                tClaimantIDPostcodeTenancyTypes1);
        mainCounts = (Integer[]) countsIDPostcodeTenancyType[0];
        IIITTCounts = (Integer[]) countsIDPostcodeTenancyType[1];
        IOITTCounts = (Integer[]) countsIDPostcodeTenancyType[2];
        OIOTTCounts = (Integer[]) countsIDPostcodeTenancyType[3];
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
                        SameTenancyAndPostcodeIIITenancyTypeString[TT],
                        null);
                summary.put(
                        SameTenancyAndPostcodeIOITenancyTypeString[TT],
                        null);
                summary.put(
                        SameTenancyAndPostcodeOIOTenancyTypeString[TT],
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
                        SameTenancyAndPostcodeIIITenancyTypeString[TT],
                        "" + IIITTCounts[TT]);
                summary.put(
                        SameTenancyAndPostcodeIOITenancyTypeString[TT],
                        "" + IOITTCounts[TT]);
                summary.put(
                        SameTenancyAndPostcodeOIOTenancyTypeString[TT],
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
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTenancyTypes,
            HashSet<ID_TenancyType> tClaimantIDTenancyType1,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTenancyTypes,
            HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTenancyTypes1,
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
        Object[] countsIDTenancyType = getCountsIDTenancyType(
                nTT,
                SHBEFilenames,
                include,
                yM31,
                tClaimantIDTenancyTypes,
                tClaimantIDTenancyType1);
        Integer[] mainCounts;
        mainCounts = (Integer[]) countsIDTenancyType[0];
        Integer[] IIITTCounts;
        Integer[] IOITTCounts;
        Integer[] OIOTTCounts;
        IIITTCounts = (Integer[]) countsIDTenancyType[1];
        IOITTCounts = (Integer[]) countsIDTenancyType[2];
        OIOTTCounts = (Integer[]) countsIDTenancyType[3];
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
                        SameTenancyIIITenancyTypeString[TT],
                        null);
                summary.put(
                        SameTenancyIOITenancyTypeString[TT],
                        null);
                summary.put(
                        SameTenancyOIOTenancyTypeString[TT],
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
                        SameTenancyIIITenancyTypeString[TT],
                        "" + IIITTCounts[TT]);
                summary.put(
                        SameTenancyIOITenancyTypeString[TT],
                        "" + IOITTCounts[TT]);
                summary.put(
                        SameTenancyOIOTenancyTypeString[TT],
                        "" + OIOTTCounts[TT]);
            }
        }
        Object[] countsIDPostcodeTenancyType = getCountsIDPostcodeTenancyType(
                nTT,
                SHBEFilenames,
                include,
                yM31,
                tClaimantIDPostcodeTenancyTypes,
                tClaimantIDPostcodeTenancyTypes1);
        mainCounts = (Integer[]) countsIDPostcodeTenancyType[0];
        IIITTCounts = (Integer[]) countsIDPostcodeTenancyType[1];
        IOITTCounts = (Integer[]) countsIDPostcodeTenancyType[2];
        OIOTTCounts = (Integer[]) countsIDPostcodeTenancyType[3];
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
                        SameTenancyAndPostcodeIIITenancyTypeString[TT],
                        null);
                summary.put(
                        SameTenancyAndPostcodeIOITenancyTypeString[TT],
                        null);
                summary.put(
                        SameTenancyAndPostcodeOIOTenancyTypeString[TT],
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
                        SameTenancyAndPostcodeIIITenancyTypeString[TT],
                        "" + IIITTCounts[TT]);
                summary.put(
                        SameTenancyAndPostcodeIOITenancyTypeString[TT],
                        "" + IOITTCounts[TT]);
                summary.put(
                        SameTenancyAndPostcodeOIOTenancyTypeString[TT],
                        "" + OIOTTCounts[TT]);
            }
        }
    }

    /**
     *
     * @param SHBEFilenames
     * @param include
     * @param forceNewSummaries
     * @param nTT
     * @param nEG
     * @param underOccupiedData
     * @param NINOToIDLookup
     * @param PostcodeToPostcodeIDLookup
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
            Object[] underOccupiedData,
            HashMap<String, DW_ID> NINOToIDLookup,
            HashMap<String, DW_ID> PostcodeToPostcodeIDLookup
    ) {
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
        Object[] tSHBEData0 = null;
        includeIte = include.iterator();
        boolean initFirst = false;
        String yM30 = "";
        String yM30v = "";
        DW_UnderOccupiedReport_Set councilUnderOccupiedSet0 = null;
        DW_UnderOccupiedReport_Set RSLUnderOccupiedSet0 = null;
        String filename0 = null;
        int i;
        while (!initFirst) {
            i = includeIte.next();
            // Load first data
            filename0 = SHBEFilenames[i];
            yM30 = DW_SHBE_Handler.getYM3(filename0);
            councilUnderOccupiedSet0 = councilUnderOccupiedSets.get(yM30);
            if (councilUnderOccupiedSet0 != null) {
                RSLUnderOccupiedSet0 = RSLUnderOccupiedSets.get(yM30);
                tSHBEData0 = getSHBEData(filename0, paymentType);
                initFirst = true;
            }
        }
        if (tSHBEData0 == null) {
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
            DW_UnderOccupiedReport_Set aCouncilUnderOccupiedSet0;
            aCouncilUnderOccupiedSet0 = councilUnderOccupiedSets.get(yM3);
            if (aCouncilUnderOccupiedSet0 != null) {
                HashMap<String, String> summary;
                summary = new HashMap<String, String>();
                String key;
                key = DW_SHBE_Handler.getYearMonthNumber(SHBEFilenames[j]);
                result.put(key, summary);
            }
        }
        // These could be returned to save time recreating them for other includes.
        // This would involve feeding them in to the method too per se.
        TreeMap<String, HashSet<DW_ID>> tClaimantIDs;
        tClaimantIDs = new TreeMap<String, HashSet<DW_ID>>();
        TreeMap<String, HashSet<DW_ID>> AllIDs;
        AllIDs = new TreeMap<String, HashSet<DW_ID>>();
        TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes;
        tClaimantIDPostcodes = new TreeMap<String, HashSet<ID_PostcodeID>>();
        TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTenancyTypes;
        tClaimantIDTenancyTypes = new TreeMap<String, HashSet<ID_TenancyType>>();
        TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTenancyTypes;
        tClaimantIDPostcodeTenancyTypes = new TreeMap<String, HashSet<ID_TenancyType_PostcodeID>>();

        // Specify Data
//        TreeMap<String, DW_SHBE_Record> tDRecords0;
//        tDRecords0 = (TreeMap<String, DW_SHBE_Record>) tSHBEData0[0];
        HashSet<DW_ID> tClaimantIDs0;
        tClaimantIDs0 = (HashSet<DW_ID>) tSHBEData0[2];
        tClaimantIDs.put(yM30, tClaimantIDs0);
        HashSet<DW_ID> AllIDs0;
        AllIDs0 = (HashSet<DW_ID>) tSHBEData0[6];
        AllIDs.put(yM30, AllIDs0);
//        HashMap<DW_ID, String> tIDByPostcode0;
//        tIDByPostcode0 = (HashMap<DW_ID, String>) tSHBEData0[8];
        //tIDByPostcode0 = loadIDByPostcode(loadData, filename, i);
//        HashMap<DW_ID, Integer> tIDByTenancyType0;
//        tIDByTenancyType0 = (HashMap<DW_ID, Integer>) tSHBEData0[9];
        //tIDByTenancyType0 = loadIDByTenancyType(loadData, filename, i);
//        HashMap<String, DW_ID> CTBRefToIDLookup0;
//        CTBRefToIDLookup0 = (HashMap<String, DW_ID>) tSHBEData0[10];
//        HashSet<ID_PostcodeID> tClaimantIDPostcodes0;
//        tClaimantIDPostcodes0 = (HashSet<ID_PostcodeID>) tSHBEData0[13];
//        tClaimantIDPostcodes.put(yM30, tClaimantIDPostcodes0);
//        HashSet<ID_TenancyType> tClaimantIDTenancyType0;
//        tClaimantIDTenancyType0 = (HashSet<ID_TenancyType>) tSHBEData0[14];
//        tClaimantIDTenancyTypes.put(yM30, tClaimantIDTenancyType0);
//        HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTenancyTypes0;
//        tClaimantIDPostcodeTenancyTypes0 = (HashSet<ID_TenancyType_PostcodeID>) tSHBEData0[15];
//        tClaimantIDPostcodeTenancyTypes.put(yM30, tClaimantIDPostcodeTenancyTypes0);

        yM30v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM30);
        partSummarySingleTime(
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
                councilUnderOccupiedSet0,
                RSLUnderOccupiedSet0,
                result,
                tClaimantIDPostcodes,
                tClaimantIDTenancyTypes,
                tClaimantIDPostcodeTenancyTypes,
                NINOToIDLookup,
                PostcodeToPostcodeIDLookup);

        if (!includeIte.hasNext()) {
            return result;
        }
        i = includeIte.next();
        // Second data
        String filename00 = filename0;
        String yM300 = yM30;
        String yM300v = yM30v;
        Object[] tSHBEData00 = tSHBEData0;
        DW_UnderOccupiedReport_Set councilUnderOccupiedSet00;
        councilUnderOccupiedSet00 = councilUnderOccupiedSet0;
        DW_UnderOccupiedReport_Set RSLUnderOccupiedSet00;
        RSLUnderOccupiedSet00 = RSLUnderOccupiedSet0;

        filename0 = SHBEFilenames[i];
        yM30 = DW_SHBE_Handler.getYM3(filename0);
        councilUnderOccupiedSet0 = councilUnderOccupiedSets.get(yM30);
        RSLUnderOccupiedSet0 = RSLUnderOccupiedSets.get(yM30);
        tSHBEData0 = getSHBEData(filename0, paymentType);

        //DO SOME SUMMARY
        partSummaryCompare2Times(
                tSHBEData00,
                yM30,
                yM30v,
                filename0,
                tSHBEData0,
                yM300,
                yM300v,
                filename00,
                forceNewSummaries,
                paymentType,
                nTT,
                nEG,
                nPSI,
                councilFilenames,
                RSLFilenames,
                councilUnderOccupiedSet0,
                RSLUnderOccupiedSet0,
                councilUnderOccupiedSet00,
                RSLUnderOccupiedSet00,
                result,
                tClaimantIDPostcodes,
                tClaimantIDTenancyTypes,
                tClaimantIDPostcodeTenancyTypes,
                NINOToIDLookup,
                PostcodeToPostcodeIDLookup);
        filename00 = filename0;
        yM300 = yM30;
        yM300v = yM30v;
        tSHBEData00 = tSHBEData0;
        councilUnderOccupiedSet00 = councilUnderOccupiedSet0;
        RSLUnderOccupiedSet00 = RSLUnderOccupiedSet0;

        while (includeIte.hasNext()) {
            i = includeIte.next();

            String filename1 = SHBEFilenames[i];
            String yM31 = DW_SHBE_Handler.getYM3(filename1);
            String yM31v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet1;
            councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(yM31);
            DW_UnderOccupiedReport_Set RSLUnderOccupiedSet1;
            RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(yM31);
            Object[] tSHBEData1 = getSHBEData(filename1, paymentType);

            //DO SOME SUMMARY
            partSummaryCompare3Times(
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
                    tSHBEData00,
                    yM300,
                    yM300v,
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
                    councilUnderOccupiedSet00,
                    RSLUnderOccupiedSet00,
                    result,
                    tClaimantIDPostcodes,
                    tClaimantIDTenancyTypes,
                    tClaimantIDPostcodeTenancyTypes,
                    NINOToIDLookup,
                    PostcodeToPostcodeIDLookup);
            councilUnderOccupiedSet00 = councilUnderOccupiedSet0;
            councilUnderOccupiedSet0 = councilUnderOccupiedSet1;
            RSLUnderOccupiedSet00 = RSLUnderOccupiedSet0;
            RSLUnderOccupiedSet0 = RSLUnderOccupiedSet1;
            filename00 = filename0;
            filename0 = filename1;
            tSHBEData0 = tSHBEData1;
            yM300 = yM30;
            yM30 = yM31;
            yM300v = yM30v;
            yM30v = yM31v;
            // Not used at present. incomeAndRentSummary0 = incomeAndRentSummary1;
        }
        return result;
    }

    private void partSummaryCompare3Times(
            String[] SHBEFilenames,
            ArrayList<Integer> include,
            Object[] tSHBEData1,
            String yM31,
            String yM31v,
            String filename1,
            Object[] tSHBEData0,
            String yM30,
            String yM30v,
            String filename0,
            Object[] tSHBEData00,
            String yM300,
            String yM300v,
            String filename00,
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
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet00,
            DW_UnderOccupiedReport_Set RSLUnderOccupiedSet00,
            TreeMap<String, HashMap<String, String>> result,
            TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes,
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTenancyTypes,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTenancyTypes,
            HashMap<String, DW_ID> NINOToIDLookup,
            HashMap<String, DW_ID> PostcodeToPostcodeIDLookup) {
        CouncilLinkedRecordCount00 = CouncilLinkedRecordCount0;
        RSLLinkedRecordCount00 = RSLLinkedRecordCount0;
        AllLinkedRecordCount00 = AllLinkedRecordCount0;
        AllCount00 = AllCount0;
        CouncilCount00 = CouncilCount0;
        RSLCount00 = RSLCount0;
        partSummaryCompare2Times(
                tSHBEData1, yM31, yM31v, filename1, tSHBEData0, yM30, yM30v,
                filename0, forceNewSummaries,
                paymentType, nTT, nEG, nPSI, councilFilenames,
                RSLFilenames, councilUnderOccupiedSet1, RSLUnderOccupiedSet1,
                councilUnderOccupiedSet0, RSLUnderOccupiedSet0, result,
                tClaimantIDPostcodes, tClaimantIDTenancyTypes,
                tClaimantIDPostcodeTenancyTypes, NINOToIDLookup,
                PostcodeToPostcodeIDLookup);

        String key;
        key = DW_SHBE_Handler.getYearMonthNumber(filename1);
        HashMap<String, String> summary;
        summary = result.get(key);

        TreeMap<String, DW_SHBE_Record> tDRecords1;
        tDRecords1 = (TreeMap<String, DW_SHBE_Record>) tSHBEData1[0];

        HashSet<ID_PostcodeID> tClaimantIDPostcodes1;
        tClaimantIDPostcodes1 = tClaimantIDPostcodes.get(yM31);
        HashSet<ID_TenancyType> tClaimantIDTenancyTypes1;
        tClaimantIDTenancyTypes1 = tClaimantIDTenancyTypes.get(yM31);
        HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTenancyTypes1;
        tClaimantIDPostcodeTenancyTypes1 = tClaimantIDPostcodeTenancyTypes.get(yM31);

        doCompare3TimesCounts(
                summary,
                nTT,
                SHBEFilenames,
                include,
                yM31,
                tClaimantIDPostcodes,
                tClaimantIDPostcodes1,
                tClaimantIDTenancyTypes,
                tClaimantIDTenancyTypes1,
                tClaimantIDPostcodeTenancyTypes,
                tClaimantIDPostcodeTenancyTypes1);
        summary.put(SHBEFilename00String, filename00);
        summary.put(SHBEFilename0String, filename0);
        summary.put(SHBEFilename1String, filename1);
        summary.put(CouncilFilename00String, councilFilenames.get(yM300));
        summary.put(RSLFilename00String, RSLFilenames.get(yM300));
        summary.put(CouncilCount00String, Integer.toString(CouncilCount00));
        summary.put(CouncilLinkedRecordCount00String, Integer.toString(CouncilLinkedRecordCount00));
        summary.put(RSLCount00String, Integer.toString(RSLCount00));
        summary.put(RSLLinkedRecordCount00String, Integer.toString(RSLLinkedRecordCount00));
        summary.put(AllLinkedRecordCount00String, Integer.toString(CouncilLinkedRecordCount00 + RSLLinkedRecordCount00));
        summary.put(CouncilFilename0String, councilFilenames.get(yM30));
        summary.put(RSLFilename0String, RSLFilenames.get(yM30));
        summary.put(CouncilCount0String, Integer.toString(CouncilCount0));
        summary.put(CouncilLinkedRecordCount0String, Integer.toString(CouncilLinkedRecordCount0));
        summary.put(RSLCount0String, Integer.toString(RSLCount0));
        summary.put(RSLLinkedRecordCount0String, Integer.toString(RSLLinkedRecordCount0));
        summary.put(AllLinkedRecordCount0String, Integer.toString(CouncilLinkedRecordCount0 + RSLLinkedRecordCount0));
        summary.put(CouncilFilename1String, councilFilenames.get(yM31));
        summary.put(RSLFilename1String, RSLFilenames.get(yM31));
        summary.put(CouncilCount1String, Integer.toString(CouncilCount1));
        summary.put(CouncilLinkedRecordCount1String, Integer.toString(CouncilLinkedRecordCount1));
        summary.put(RSLCount1String, Integer.toString(RSLCount1));
        summary.put(RSLLinkedRecordCount1String, Integer.toString(RSLLinkedRecordCount1));
        summary.put(AllLinkedRecordCount1String, Integer.toString(CouncilLinkedRecordCount1 + RSLLinkedRecordCount1));
    }

    /**
     * Bit odd but filename0 has the data for now. filename00 has the data for
     * the previous time.
     *
     * @param tSHBEData0
     * @param yM30
     * @param yM30v
     * @param filename0
     * @param tSHBEData00
     * @param yM300
     * @param yM300v
     * @param filename00
     * @param forceNewSummaries
     * @param nTT
     * @param nEG
     * @param councilFilenames
     * @param RSLFilenames
     * @param councilUnderOccupiedSet0
     * @param RSLUnderOccupiedSet0
     * @param councilUnderOccupiedSet00
     * @param RSLUnderOccupiedSet00
     * @param result
     */
    private void partSummaryCompare2Times(
            Object[] tSHBEData0,
            String yM30,
            String yM30v,
            String filename0,
            Object[] tSHBEData00,
            String yM300,
            String yM300v,
            String filename00,
            boolean forceNewSummaries,
            String paymentType,
            int nTT,
            int nEG,
            int nPSI,
            TreeMap<String, String> councilFilenames,
            TreeMap<String, String> RSLFilenames,
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet0,
            DW_UnderOccupiedReport_Set RSLUnderOccupiedSet0,
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet00,
            DW_UnderOccupiedReport_Set RSLUnderOccupiedSet00,
            TreeMap<String, HashMap<String, String>> result,
            TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes,
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTenancyTypes,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTenancyTypes,
            HashMap<String, DW_ID> NINOToIDLookup,
            HashMap<String, DW_ID> PostcodeToPostcodeIDLookup) {
        AllCount0 = AllCount1;
        CouncilCount0 = CouncilCount1;
        RSLCount0 = RSLCount1;
        CouncilLinkedRecordCount0 = CouncilLinkedRecordCount1;
        RSLLinkedRecordCount0 = RSLLinkedRecordCount1;
        AllLinkedRecordCount0 = AllLinkedRecordCount1;
        partSummarySingleTime(tSHBEData0, yM30, yM30v, filename0,
                forceNewSummaries,
                paymentType,
                nTT, nEG, nPSI, councilFilenames, RSLFilenames,
                councilUnderOccupiedSet0, RSLUnderOccupiedSet0, result,
                tClaimantIDPostcodes, tClaimantIDTenancyTypes,
                tClaimantIDPostcodeTenancyTypes, NINOToIDLookup,
                PostcodeToPostcodeIDLookup);

        TreeMap<String, DW_SHBE_Record> tDRecords0;
        tDRecords0 = (TreeMap<String, DW_SHBE_Record>) tSHBEData0[0];
        HashMap<DW_ID, String> tIDByPostcode0;
        tIDByPostcode0 = (HashMap<DW_ID, String>) tSHBEData0[8];
        HashMap<DW_ID, Integer> tIDByTenancyType0;
        tIDByTenancyType0 = (HashMap<DW_ID, Integer>) tSHBEData0[9];
        HashMap<String, DW_ID> CTBRefToIDLookup0;
        CTBRefToIDLookup0 = (HashMap<String, DW_ID>) tSHBEData0[10];

        HashMap<DW_ID, String> tIDByPostcode00;
        tIDByPostcode00 = (HashMap<DW_ID, String>) tSHBEData00[8];
        HashMap<DW_ID, Integer> tIDByTenancyType00;
        tIDByTenancyType00 = (HashMap<DW_ID, Integer>) tSHBEData00[9];
        HashMap<String, DW_ID> CTBRefToIDLookup00;
        CTBRefToIDLookup00 = (HashMap<String, DW_ID>) tSHBEData00[10];

        TreeMap<String, DW_UnderOccupiedReport_Record> councilUnderOccupiedSet0Map;
        councilUnderOccupiedSet0Map = councilUnderOccupiedSet0.getMap();
        TreeMap<String, DW_UnderOccupiedReport_Record> RSLUnderOccupiedSet0Map;
        RSLUnderOccupiedSet0Map = RSLUnderOccupiedSet0.getMap();
        // Loop over underoccupancy data
        // Loop over Council
        doCompare2TimesLoopOverSet(
                councilUnderOccupiedSet0Map,
                tDRecords0,
                CTBRefToIDLookup00,
                CTBRefToIDLookup0,
                tIDByTenancyType00,
                tIDByTenancyType0,
                tIDByPostcode00,
                tIDByPostcode0,
                yM300v,
                yM30v);
        // Loop over RSL
        doCompare2TimesLoopOverSet(
                RSLUnderOccupiedSet0Map,
                tDRecords0,
                CTBRefToIDLookup00,
                CTBRefToIDLookup0,
                tIDByTenancyType00,
                tIDByTenancyType0,
                tIDByPostcode00,
                tIDByPostcode0,
                yM300v,
                yM30v);

        String key;
        key = DW_SHBE_Handler.getYearMonthNumber(filename0);
        HashMap<String, String> summary;
        summary = result.get(key);

        addToSummaryCompare2Times(nTT, nEG, nPSI, summary);

        // All
        summary.put(SHBEFilename0String, filename00); // This looks wierd but is right!
        summary.put(SHBEFilename1String, filename0); // This looks wierd but is right!
        summary.put(CouncilFilename0String, councilFilenames.get(yM300)); // This looks wierd but is right!
        summary.put(RSLFilename0String, RSLFilenames.get(yM300)); // This looks wierd but is right!
        summary.put(CouncilCount0String, Integer.toString(CouncilCount0));
        summary.put(CouncilLinkedRecordCount0String, Integer.toString(CouncilLinkedRecordCount0));
        summary.put(RSLCount0String, Integer.toString(RSLCount0));
        summary.put(RSLLinkedRecordCount0String, Integer.toString(RSLLinkedRecordCount0));
        summary.put(AllLinkedRecordCount0String, Integer.toString(CouncilLinkedRecordCount0 + RSLLinkedRecordCount0));
        summary.put(CouncilFilename1String, councilFilenames.get(yM30)); // This looks wierd but is right!
        summary.put(RSLFilename1String, RSLFilenames.get(yM30)); // This looks wierd but is right!
        summary.put(CouncilCount1String, Integer.toString(CouncilCount1));
        summary.put(CouncilLinkedRecordCount1String, Integer.toString(CouncilLinkedRecordCount1));
        summary.put(RSLCount1String, Integer.toString(RSLCount1));
        summary.put(RSLLinkedRecordCount1String, Integer.toString(RSLLinkedRecordCount1));
        summary.put(AllLinkedRecordCount1String, Integer.toString(CouncilLinkedRecordCount1 + RSLLinkedRecordCount1));
    }

    private void addToSetsForComparisons(
            String yM3,
            TreeMap<String, DW_SHBE_Record> D_Records,
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet,
            DW_UnderOccupiedReport_Set RSLUnderOccupiedSet,
            TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes,
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTenancyTypes,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTenancyTypes,
            HashMap<String, DW_ID> NINOToIDLookup,
            HashMap<String, DW_ID> PostcodeToPostcodeIDLookup
    ) {
        HashSet<ID_PostcodeID> tClaimantIDPostcodes0;
        HashSet<ID_TenancyType> tClaimantIDTenancyTypes0;
        HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTenancyTypes0;
        if (tClaimantIDPostcodes.containsKey(yM3)) {
            tClaimantIDPostcodes0 = tClaimantIDPostcodes.get(yM3);
        } else {
            tClaimantIDPostcodes0 = getID_PostcodeIDSet(
                    D_Records, councilUnderOccupiedSet, RSLUnderOccupiedSet,
                    NINOToIDLookup, PostcodeToPostcodeIDLookup);
            tClaimantIDPostcodes.put(yM3, tClaimantIDPostcodes0);
        }
        if (tClaimantIDTenancyTypes.containsKey(yM3)) {
            tClaimantIDTenancyTypes0 = tClaimantIDTenancyTypes.get(yM3);
        } else {
            tClaimantIDTenancyTypes0 = getID_TenancyTypeSet(
                    D_Records, councilUnderOccupiedSet, RSLUnderOccupiedSet,
                    NINOToIDLookup);
            tClaimantIDTenancyTypes.put(yM3, tClaimantIDTenancyTypes0);
        }
        if (tClaimantIDPostcodeTenancyTypes.containsKey(yM3)) {
            tClaimantIDPostcodeTenancyTypes0 = tClaimantIDPostcodeTenancyTypes.get(yM3);
        } else {
            tClaimantIDPostcodeTenancyTypes0 = getID_TenancyType_PostcodeIDSet(
                    D_Records, councilUnderOccupiedSet, RSLUnderOccupiedSet,
                    NINOToIDLookup, PostcodeToPostcodeIDLookup);
            tClaimantIDPostcodeTenancyTypes.put(yM3, tClaimantIDPostcodeTenancyTypes0);
        }
    }

    private void partSummarySingleTime(
            //            HashMap<String, String> summary,
            Object[] tSHBEData,
            //            TreeMap<String, DW_SHBE_Record> tDRecords0,
            //            HashMap<DW_ID, String> tIDByPostcode0,
            //            HashMap<DW_ID, Integer> tIDByTenancyType0,
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
            TreeMap<String, HashMap<String, String>> result,
            TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes,
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTenancyTypes,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTenancyTypes,
            HashMap<String, DW_ID> NINOToIDLookup,
            HashMap<String, DW_ID> PostcodeToPostcodeIDLookup) {

        TreeMap<String, DW_SHBE_Record> tDRecords0;
        tDRecords0 = (TreeMap<String, DW_SHBE_Record>) tSHBEData[0];
        HashMap<DW_ID, String> tIDByPostcode0;
        tIDByPostcode0 = (HashMap<DW_ID, String>) tSHBEData[8];
        //tIDByPostcode0 = loadIDByPostcode(loadData, filename, i);
        HashMap<DW_ID, Integer> tIDByTenancyType0;
        tIDByTenancyType0 = (HashMap<DW_ID, Integer>) tSHBEData[9];
        //tIDByTenancyType0 = loadIDByTenancyType(loadData, filename, i);
        HashMap<String, DW_ID> CTBRefToIDLookup0;
        CTBRefToIDLookup0 = (HashMap<String, DW_ID>) tSHBEData[10];
        //tCTBRefID0 = loadCTBRefByID(loadData, filename, i);
        String key;
        key = DW_SHBE_Handler.getYearMonthNumber(filename);
        HashMap<String, String> summary;
        summary = result.get(key);
        HashMap<String, Integer> tLoadSummary;
        tLoadSummary = (HashMap<String, Integer>) tSHBEData[12];
        addToSummary(summary, tLoadSummary);
        initCounts(nTT, nEG, nPSI);

        // Add to tClaimantIDPostcodes, tClaimantIDTenancyTypes, tClaimantIDPostcodeTenancyTypes for later comparisons
        addToSetsForComparisons(
                yM3,
                tDRecords0,
                councilUnderOccupiedSet,
                RSLUnderOccupiedSet,
                tClaimantIDPostcodes,
                tClaimantIDTenancyTypes,
                tClaimantIDPostcodeTenancyTypes,
                NINOToIDLookup,
                PostcodeToPostcodeIDLookup);

        TreeMap<String, DW_UnderOccupiedReport_Record> councilUnderOccupiedSet0Map;
        councilUnderOccupiedSet0Map = councilUnderOccupiedSet.getMap();
        TreeMap<String, DW_UnderOccupiedReport_Record> RSLUnderOccupiedSet0Map;
        RSLUnderOccupiedSet0Map = RSLUnderOccupiedSet.getMap();
        // Loop over underoccupancy data
        // Loop over Council
        CouncilLinkedRecordCount1 = doSingleTimeLoopOverSet(
                councilUnderOccupiedSet0Map, tDRecords0, CTBRefToIDLookup0,
                tIDByTenancyType0, tIDByPostcode0, yM3v);
        // Loop over RSL
        RSLLinkedRecordCount1 = doSingleTimeLoopOverSet(
                RSLUnderOccupiedSet0Map, tDRecords0, CTBRefToIDLookup0,
                tIDByTenancyType0, tIDByPostcode0, yM3v);
        HashMap<String, BigDecimal> incomeAndRentSummary0;
        incomeAndRentSummary0 = DW_SHBE_Handler.getIncomeAndRentSummary(
                tSHBEData,
                filename,
                paymentType,
                councilUnderOccupiedSet,
                true,
                forceNewSummaries);
        addToSummarySingleTimeIncomeAndRent0(
                summary,
                incomeAndRentSummary0);

        // All
        CouncilCount1 = councilUnderOccupiedSet0Map.size();
        RSLCount1 = RSLUnderOccupiedSet0Map.size();
        AllCount1 = CouncilCount1 + RSLCount1;
        AllLinkedRecordCount1 = CouncilLinkedRecordCount1 + RSLLinkedRecordCount1;
        summary.put(SHBEFilename1String, filename);
        summary.put(CouncilFilename1String, councilFilenames.get(yM3));
        summary.put(RSLFilename1String, RSLFilenames.get(yM3));
        summary.put(CouncilCount1String, Integer.toString(councilUnderOccupiedSet0Map.size()));
        summary.put(CouncilLinkedRecordCount1String, Integer.toString(CouncilLinkedRecordCount1));
        summary.put(RSLCount1String, Integer.toString(RSLUnderOccupiedSet0Map.size()));
        summary.put(RSLLinkedRecordCount1String, Integer.toString(RSLLinkedRecordCount1));
        summary.put(AllCount1String, Integer.toString(AllCount1));
        summary.put(AllLinkedRecordCount1String, Integer.toString(AllLinkedRecordCount1));
        addToSummarySingleTime(nTT, nEG, nPSI, summary);
        addToSummarySingleTimeRentArrears0(summary);
    }

    public void doCompare2TimesLoopOverSet(
            TreeMap<String, DW_UnderOccupiedReport_Record> map,
            TreeMap<String, DW_SHBE_Record> D_Records0,
            HashMap<String, DW_ID> CTBRefID0,
            HashMap<String, DW_ID> CTBRefID1,
            HashMap<DW_ID, Integer> tIDByTenancyType0,
            HashMap<DW_ID, Integer> tIDByTenancyType1,
            HashMap<DW_ID, String> tIDByPostcode0,
            HashMap<DW_ID, String> tIDByPostcode1,
            String yM30v,
            String yM31v) {
        Iterator<String> ite;
        ite = map.keySet().iterator();
        while (ite.hasNext()) {
            String tID;
            tID = ite.next();
            DW_UnderOccupiedReport_Record UORec;
            UORec = map.get(tID);
            // Rent Arrears Summary
            doSingleTimeRentArrearsCount(UORec);
            String CTBRef;
            CTBRef = UORec.getClaimReferenceNumber();
            DW_SHBE_Record tDRecordsCTBRefDW_SHBE_Record;
            tDRecordsCTBRefDW_SHBE_Record = D_Records0.get(CTBRef);
            if (tDRecordsCTBRefDW_SHBE_Record == null) {
                // Count this!
                tDRecordsCTBRefDW_SHBE_RecordNullCount++;
            } else {
                DW_SHBE_D_Record D_Record;
                D_Record = tDRecordsCTBRefDW_SHBE_Record.getDRecord();
                doCompare2TimesCounts(
                        D_Record,
                        CTBRef,
                        CTBRefID0,
                        CTBRefID1,
                        tIDByTenancyType0,
                        tIDByTenancyType1,
                        tIDByPostcode0,
                        tIDByPostcode1,
                        yM30v,
                        yM31v);
            }
        }
    }

    public int doSingleTimeLoopOverSet(
            TreeMap<String, DW_UnderOccupiedReport_Record> map,
            TreeMap<String, DW_SHBE_Record> D_Records,
            HashMap<String, DW_ID> CTBRefToIDLookup0,
            HashMap<DW_ID, Integer> tIDByTenancyType0,
            HashMap<DW_ID, String> tIDByPostcode0,
            String yM30v) {
        int linkedRecords;
        linkedRecords = 0;
        Iterator<String> ite;
        ite = map.keySet().iterator();
        while (ite.hasNext()) {
            String tID;
            tID = ite.next();
            DW_UnderOccupiedReport_Record UORec;
            UORec = map.get(tID);
            // Rent Arrears Summary
            doSingleTimeRentArrearsCount(UORec);
            String CTBRef;
            CTBRef = UORec.getClaimReferenceNumber();
            DW_SHBE_Record tDRecordsCTBRefDW_SHBE_Record;
            tDRecordsCTBRefDW_SHBE_Record = D_Records.get(CTBRef);
            if (tDRecordsCTBRefDW_SHBE_Record == null) {
                tDRecordsCTBRefDW_SHBE_RecordNullCount++;
            } else {
                DW_SHBE_D_Record D_Record;
                D_Record = tDRecordsCTBRefDW_SHBE_Record.getDRecord();
                doSingleTimeCount(
                        D_Record,
                        CTBRef,
                        CTBRefToIDLookup0,
                        tIDByTenancyType0,
                        tIDByPostcode0,
                        yM30v);
                linkedRecords++;
            }
        }
        return linkedRecords;
    }

    private void addToSummarySingleTimeIncomeAndRent0(
            HashMap<String, String> summary,
            HashMap<String, BigDecimal> incomeAndRentSummary0) {
        Iterator<String> incomeAndRentSummaryKeySetIte;
        incomeAndRentSummaryKeySetIte = incomeAndRentSummary0.keySet().iterator();
        while (incomeAndRentSummaryKeySetIte.hasNext()) {
            String name;
            name = incomeAndRentSummaryKeySetIte.next();
            String value;
            value = Generic_BigDecimal.roundIfNecessary(
                    incomeAndRentSummary0.get(name), 2, RoundingMode.HALF_UP).toPlainString();
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
        writeSummaryTableSingleTime0(
                summaryTable,
                paymentType,
                includeKey,
                doUnderOccupancy,
                nTT, nEG, nPSI);
        writeSummaryTableSingleTime1(
                summaryTable,
                paymentType,
                includeKey,
                doUnderOccupancy,
                nTT, nEG);
        writeSummaryTableSingleTimePassportedStandardIndicator(
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
        writeSummaryTableSingleTimeTenancyType(
                summaryTable,
                paymentType,
                includeKey,
                doUnderOccupancy,
                nTT, nEG);
        writeSummaryTableSingleTimeDemographics(
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
        File dirOut;
        dirOut = getSummaryTableDir(paymentType, includeKey, underOccupancy);
        File outFile;
        outFile = new File(
                dirOut,
                summaryTable.firstKey() + "To" + summaryTable.lastKey() + "Compare3Times.csv");
        PrintWriter pw;
        pw = Generic_StaticIO.getPrintWriter(outFile, false);
        // Write headers
        String header;
        header = "";
        header += SHBEFilename00String + ", ";
        header += SHBEFilename0String + ", ";
        header += SHBEFilename1String + ", ";
        header += "PostCodeLookupDate00, PostCodeLookupFile00, "
                + "PostCodeLookupDate0, PostCodeLookupFile0, "
                + "PostCodeLookupDate1, PostCodeLookupFile1, ";
        if (underOccupancy) {
            header += CouncilFilename00String + ", ";
            header += RSLFilename00String + ", ";
            header += CouncilCount00String + ", ";
            header += RSLCount00String + ", ";
            header += AllCount00String + ", ";
            header += AllLinkedRecordCount00String + ", ";
            header += CouncilFilename0String + ", ";
            header += RSLFilename0String + ", ";
            header += CouncilCount0String + ", ";
            header += RSLCount0String + ", ";
            header += AllCount0String + ", ";
            header += AllLinkedRecordCount1String + ", ";
            header += CouncilFilename1String + ", ";
            header += RSLFilename1String + ", ";
            header += CouncilCount1String + ", ";
            header += RSLCount1String + ", ";
            header += AllCount1String + ", ";
            header += AllLinkedRecordCount1String + ", ";
        }
        header += SamePostcodeIIIString + ", ";
        header += SamePostcodeIOIString + ", ";
        header += SamePostcodeOIOString + ", ";
        header += SameTenancyIIIString + ", ";
        header += SameTenancyIOIString + ", ";
        header += SameTenancyOIOString + ", ";
        for (int i = 1; i < nTT; i++) {
            header += SameTenancyIIITenancyTypeString[i] + ", ";
            header += SameTenancyIOITenancyTypeString[i] + ", ";
            header += SameTenancyOIOTenancyTypeString[i] + ", ";
        }
        header += SameTenancyAndPostcodeIIIString + ", ";
        header += SameTenancyAndPostcodeIOIString + ", ";
        header += SameTenancyAndPostcodeOIOString + ", ";
        for (int i = 1; i < nTT; i++) {
            header += SameTenancyAndPostcodeIIITenancyTypeString[i] + ", ";
            header += SameTenancyAndPostcodeIOITenancyTypeString[i] + ", ";
            header += SameTenancyAndPostcodeOIOTenancyTypeString[i] + ", ";
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
            String filename00;
            filename00 = summary.get(SHBEFilename00String);
            String filename0;
            filename0 = summary.get(SHBEFilename0String);
            String filename1;
            filename1 = summary.get(SHBEFilename1String);
            line += filename00 + "," + filename0 + ", " + filename1 + ", ";
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
                line += summary.get(CouncilFilename00String) + ", ";
                line += summary.get(RSLFilename00String) + ", ";
                line += summary.get(CouncilCount00String) + ", ";
                line += summary.get(RSLCount00String) + ", ";
                line += summary.get(AllCount00String) + ", ";
                line += summary.get(AllLinkedRecordCount00String) + ", ";
                line += summary.get(CouncilFilename0String) + ", ";
                line += summary.get(RSLFilename0String) + ", ";
                line += summary.get(CouncilCount0String) + ", ";
                line += summary.get(RSLCount0String) + ", ";
                line += summary.get(AllCount0String) + ", ";
                line += summary.get(AllLinkedRecordCount0String) + ", ";
                line += summary.get(CouncilFilename1String) + ", ";
                line += summary.get(RSLFilename1String) + ", ";
                line += summary.get(CouncilCount1String) + ", ";
                line += summary.get(RSLCount1String) + ", ";
                line += summary.get(AllCount1String) + ", ";
                line += summary.get(AllLinkedRecordCount1String) + ", ";
            }
            line += summary.get(SamePostcodeIIIString) + ", ";
            line += summary.get(SamePostcodeIOIString) + ", ";
            line += summary.get(SamePostcodeOIOString) + ", ";
            line += summary.get(SameTenancyIIIString) + ", ";
            line += summary.get(SameTenancyIOIString) + ", ";
            line += summary.get(SameTenancyOIOString) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(SameTenancyIIITenancyTypeString[i]) + ", ";
                line += summary.get(SameTenancyIOITenancyTypeString[i]) + ", ";
                line += summary.get(SameTenancyOIOTenancyTypeString[i]) + ", ";
            }
            line += summary.get(SameTenancyAndPostcodeIIIString) + ", ";
            line += summary.get(SameTenancyAndPostcodeIOIString) + ", ";
            line += summary.get(SameTenancyAndPostcodeOIOString) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(SameTenancyAndPostcodeIIITenancyTypeString[i]) + ", ";
                line += summary.get(SameTenancyAndPostcodeIOITenancyTypeString[i]) + ", ";
                line += summary.get(SameTenancyAndPostcodeOIOTenancyTypeString[i]) + ", ";
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
        File dirOut;
        dirOut = getSummaryTableDir(paymentType, includeKey, underOccupancy);
        File outFile;
        outFile = new File(
                dirOut,
                summaryTable.firstKey() + "To" + summaryTable.lastKey() + "Compare2Times.csv");
        PrintWriter pw;
        pw = Generic_StaticIO.getPrintWriter(outFile, false);
        // Write headers
        String header;
        header = "";
        header += SHBEFilename0String + ", ";
        header += "Year0-Month0, ";
        header += "Month0 Year0, ";
        header += SHBEFilename1String + ", ";
        header += "Year1-Month1, ";
        header += "Month1 Year1, ";
        header += "PostCodeLookupDate0, PostCodeLookupFile0, "
                + "PostCodeLookupDate1, PostCodeLookupFile1, ";
        if (underOccupancy) {
            header += CouncilFilename0String + ", ";
            header += RSLFilename0String + ", ";
            header += CouncilCount0String + ", ";
            header += RSLCount0String + ", ";
            header += AllCount0String + ", ";
            header += AllLinkedRecordCount1String + ", ";
            header += CouncilFilename1String + ", ";
            header += RSLFilename1String + ", ";
            header += CouncilCount1String + ", ";
            header += RSLCount1String + ", ";
            header += AllCount1String + ", ";
            header += AllLinkedRecordCount1String + ", ";
        }
        header += sAllTotalCountTenancyTypeChangeClaimant + ", ";
        header += sAllPercentageTenancyTypeChangeClaimant + ", ";
        header += sHBTotalCountPostcode0ValidPostcode1Valid + ", ";
        header += sHBPercentagePostcode0ValidPostcode1Valid + ", ";
        header += sHBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sHBTotalCountPostcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sHBPercentagePostcode0ValidPostcode1ValidPostcodeChange + ", ";
        header += sHBTotalCountPostcode0ValidPostcode1NotValid + ", ";
        header += sHBPercentagePostcode0ValidPostcode1NotValid + ", ";
        header += sHBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sHBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sHBTotalCountTenancyTypeChangeClaimant + ", ";
        header += sHBPercentageTenancyTypeChangeClaimant + ", ";
        header += sHBTotalCountTenancyTypeChangeClaimantHousing + ", ";
        header += sHBPercentageTenancyTypeChangeClaimantHousing + ", ";
        header += sHBTotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes + ", ";
        header += sHBPercentageSocialTenancyTypesToPrivateDeregulatedTenancyTypes + ", ";
        header += sHBTotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes + ", ";
        header += sHBPercentagePrivateDeregulatedTenancyTypesToSocialTenancyTypes + ", ";
        header += sHBTotalCountPostcodeChangeWithinSocialTenancyTypes + ", ";
        header += sHBPercentagePostcodeChangeWithinSocialTenancyTypes + ", ";
        header += sHBTotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes + ", ";
        header += sHBPercentagePostcodeChangeWithinPrivateDeregulatedTenancyTypes + ", ";
        header += sCTBTotalCountPostcode0ValidPostcode1Valid + ", ";
        header += sCTBPercentagePostcode0ValidPostcode1Valid + ", ";
        header += sCTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged + ", ";
        header += sCTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged + ", ";
        header += sCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged + ", ";
        header += sCTBTotalCountPostcode0ValidPostcode1NotValid + ", ";
        header += sCTBPercentagePostcode0ValidPostcode1NotValid + ", ";
        header += sCTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged + ", ";
        header += sCTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged + ", ";
        header += sCTBTotalCountTenancyTypeChangeClaimant + ", ";
        header += sCTBPercentageTenancyTypeChangeClaimant + ", ";
        header += sCTBTotalCountTenancyTypeChangeClaimantHousing + ", ";
        header += sCTBPercentageTenancyTypeChangeClaimantHousing + ", ";
        header += sCTBTotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes + ", ";
        header += sCTBPercentageSocialTenancyTypesToPrivateDeregulatedTenancyTypes + ", ";
        header += sCTBTotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes + ", ";
        header += sCTBPercentagePrivateDeregulatedTenancyTypesToSocialTenancyTypes + ", ";
        header += sCTBTotalCountPostcodeChangeWithinSocialTenancyTypes + ", ";
        header += sCTBPercentagePostcodeChangeWithinSocialTenancyTypes + ", ";
        header += sCTBTotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes + ", ";
        header += sCTBPercentagePostcodeChangeWithinPrivateDeregulatedTenancyTypes;
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
            String filename0;
            filename0 = summary.get(SHBEFilename0String);
            line += filename0 + ", ";
            if (filename0 != null) {
                line += DW_SHBE_Handler.getYearMonthNumber(filename0) + ", ";
                line += DW_SHBE_Handler.getMonth(filename0) + " " + DW_SHBE_Handler.getYear(filename0) + ", ";
            } else {
                line += "null, ";
                line += "null, ";
            }
            String filename1;
            filename1 = summary.get(SHBEFilename1String);
            line += filename1 + ", ";
            line += DW_SHBE_Handler.getYearMonthNumber(filename1) + ", ";
            line += DW_SHBE_Handler.getMonth(filename1) + " " + DW_SHBE_Handler.getYear(filename1) + ", ";
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
                line += summary.get(CouncilFilename0String) + ", ";
                line += summary.get(CouncilCount0String) + ", ";
                line += summary.get(RSLFilename0String) + ", ";
                line += summary.get(RSLCount0String) + ", ";
                line += summary.get(AllCount0String) + ", ";
                line += summary.get(AllLinkedRecordCount0String) + ", ";
                line += summary.get(CouncilFilename1String) + ", ";
                line += summary.get(RSLFilename1String) + ", ";
                line += summary.get(CouncilCount1String) + ", ";
                line += summary.get(RSLCount1String) + ", ";
                line += summary.get(AllCount1String) + ", ";
                line += summary.get(AllLinkedRecordCount1String) + ", ";
                line += summary.get(CouncilFilename1String) + ", ";
            }
            line += summary.get(sAllTotalCountTenancyTypeChangeClaimant) + ", ";
            line += summary.get(sAllPercentageTenancyTypeChangeClaimant) + ", ";
            line += summary.get(sHBTotalCountPostcode0ValidPostcode1Valid) + ", ";
            line += summary.get(sHBPercentagePostcode0ValidPostcode1Valid) + ", ";
            line += summary.get(sHBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
            line += summary.get(sHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
            line += summary.get(sHBTotalCountPostcode0ValidPostcode1ValidPostcodeChange) + ", ";
            line += summary.get(sHBPercentagePostcode0ValidPostcode1ValidPostcodeChange) + ", ";
            line += summary.get(sHBTotalCountPostcode0ValidPostcode1NotValid) + ", ";
            line += summary.get(sHBPercentagePostcode0ValidPostcode1NotValid) + ", ";
            line += summary.get(sHBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
            line += summary.get(sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
            line += summary.get(sHBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
            line += summary.get(sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
            line += summary.get(sHBTotalCountTenancyTypeChangeClaimant) + ", ";
            line += summary.get(sHBPercentageTenancyTypeChangeClaimant) + ", ";
            line += summary.get(sHBTotalCountTenancyTypeChangeClaimantHousing) + ", ";
            line += summary.get(sHBPercentageTenancyTypeChangeClaimantHousing) + ", ";
            line += summary.get(sHBTotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes) + ", ";
            line += summary.get(sHBPercentageSocialTenancyTypesToPrivateDeregulatedTenancyTypes) + ", ";
            line += summary.get(sHBTotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes) + ", ";
            line += summary.get(sHBPercentagePrivateDeregulatedTenancyTypesToSocialTenancyTypes) + ", ";
            line += summary.get(sHBTotalCountPostcodeChangeWithinSocialTenancyTypes) + ", ";
            line += summary.get(sHBPercentagePostcodeChangeWithinSocialTenancyTypes) + ", ";
            line += summary.get(sHBTotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes) + ", ";
            line += summary.get(sHBPercentagePostcodeChangeWithinPrivateDeregulatedTenancyTypes) + ", ";
            line += summary.get(sCTBTotalCountPostcode0ValidPostcode1Valid) + ", ";
            line += summary.get(sCTBPercentagePostcode0ValidPostcode1Valid) + ", ";
            line += summary.get(sCTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
            line += summary.get(sCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged) + ", ";
            line += summary.get(sCTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged) + ", ";
            line += summary.get(sCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged) + ", ";
            line += summary.get(sCTBTotalCountPostcode0ValidPostcode1NotValid) + ", ";
            line += summary.get(sCTBPercentagePostcode0ValidPostcode1NotValid) + ", ";
            line += summary.get(sCTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
            line += summary.get(sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged) + ", ";
            line += summary.get(sCTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
            line += summary.get(sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged) + ", ";
            line += summary.get(sCTBTotalCountTenancyTypeChangeClaimant) + ", ";
            line += summary.get(sCTBPercentageTenancyTypeChangeClaimant) + ", ";
            line += summary.get(sCTBTotalCountTenancyTypeChangeClaimantHousing) + ", ";
            line += summary.get(sCTBPercentageTenancyTypeChangeClaimantHousing) + ", ";
            line += summary.get(sCTBTotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes) + ", ";
            line += summary.get(sCTBPercentageSocialTenancyTypesToPrivateDeregulatedTenancyTypes) + ", ";
            line += summary.get(sCTBTotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes) + ", ";
            line += summary.get(sCTBPercentagePrivateDeregulatedTenancyTypesToSocialTenancyTypes) + ", ";
            line += summary.get(sCTBTotalCountPostcodeChangeWithinSocialTenancyTypes) + ", ";
            line += summary.get(sCTBPercentagePostcodeChangeWithinSocialTenancyTypes) + ", ";
            line += summary.get(sCTBTotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes) + ", ";
            line += summary.get(sCTBPercentagePostcodeChangeWithinPrivateDeregulatedTenancyTypes);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTime0(
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
        File dirOut;
        dirOut = getSummaryTableDir(paymentType, includeKey, underOccupancy);
        File outFile;
        outFile = new File(
                dirOut,
                summaryTable.firstKey() + "To" + summaryTable.lastKey() + "SingleTime0.csv");
        PrintWriter pw;
        pw = Generic_StaticIO.getPrintWriter(outFile, false);
        // Write headers
        String header;
        header = "";
        header += SHBEFilename1String + ", ";
        header += getSingleTimeGenericHeaderPart(underOccupancy);
        header += sCountTT5or7 + ", ";
        header += sCountHBRefNullTT5or7 + ", ";
        header += sCountHBRefNullTTNot5or7 + ", ";
        header += sCountHBRefNotNullTT5or7 + ", ";
        header += sCountHBRefNotNullTTNot5or7 + ", ";
        if (!underOccupancy) {
            header += "PostCodeLookupDate, ";
            header += "PostCodeLookupFile, ";
            header += "LineCount, ";
            header += "LineNotLoadedCount, ";
            header += "DRecordCount, ";
            header += "SRecordCount, ";
            header += "UniqueClaimantNationalInsuranceNumberCount, ";
            header += "UniquePartnerNationalInsuranceNumberCount, ";
            header += "TotalClaimantAndPartnerNationalInsuranceNumberCount, ";
            header += "UniqueAllHouseholdNationalInsuranceNumberCount, ";
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
            filename1 = summary.get(SHBEFilename1String);
            line += filename1 + ", ";
            line += getSingleTimeGenericLinePart(key, summary, underOccupancy);
            line += summary.get(sCountTT5or7) + ", ";
            line += summary.get(sCountHBRefNullTT5or7) + ", ";
            line += summary.get(sCountHBRefNullTTNot5or7) + ", ";
            line += summary.get(sCountHBRefNotNullTT5or7) + ", ";
            line += summary.get(sCountHBRefNotNullTTNot5or7) + ", ";            
            if (!underOccupancy) {
                line += getPostcodeLookupDateAndFilenameLinePart(filename1, ONSPDFiles);
                line += summary.get("LineCount") + ", ";
                line += summary.get("LineNotLoadedCount") + ", ";
                line += summary.get("DRecordCount") + ", ";
                line += summary.get("SRecordCount") + ", ";
                line += summary.get("UniqueClaimantNationalInsuranceNumberCount") + ", ";
                line += summary.get("UniquePartnerNationalInsuranceNumberCount") + ", ";
                line += Integer.toString(
                        Integer.valueOf(summary.get("UniqueClaimantNationalInsuranceNumberCount"))
                        + Integer.valueOf(summary.get("UniquePartnerNationalInsuranceNumberCount")))
                        + ", ";
                line += summary.get("UniqueAllHouseholdNationalInsuranceNumberCount") + ", ";
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

    public void writeSummaryTableSingleTime1(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean doUnderOccupancy,
            int nTT,
            int nEG
    ) {
        File dirOut;
        dirOut = getSummaryTableDir(paymentType, includeKey, doUnderOccupancy);

        File outFile;
        outFile = new File(
                dirOut,
                summaryTable.firstKey() + "To" + summaryTable.lastKey() + "SingleTime1.csv");
        PrintWriter pw;
        pw = Generic_StaticIO.getPrintWriter(outFile, false);
        // Write headers
        String header;
        header = "";
        header += SHBEFilename1String + ", ";
        header += getSingleTimeGenericHeaderPart(doUnderOccupancy);
        header += sTotalWeeklyHBEntitlement + ", ";
        header += sTotalCountWeeklyHBEntitlementNonZero + ", ";
        header += sAverageWeeklyHBEntitlement + ", ";
        header += sTotalWeeklyEligibleRentAmount + ", ";
        header += sTotalCountWeeklyEligibleRentAmountNonZero + ", ";
        header += sAverageWeeklyEligibleRentAmount + ", ";
        header += sTotalWeeklyEligibleRentAmount + ", ";
        header += sTotalCountWeeklyEligibleRentAmountNonZero + ", ";
        header += sAverageWeeklyEligibleRentAmount + ", ";
        header += sTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sTotalCountWeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sAverageWeeklyEligibleCouncilTaxAmount + ", ";
        header += sTotalContractualRentAmount + ", ";
        header += sTotalCountContractualRentAmountNonZeroCount + ", ";
        header += sAverageContractualRentAmount + ", ";
        header += sTotalWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sAverageWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sHBTotalCountClaimantsEmployed + ", ";
        header += sHBPercentageClaimantsEmployed + ", ";
        header += sHBTotalCountClaimantsSelfEmployed + ", ";
        header += sHBPercentageClaimantsSelfEmployed + ", ";
        header += sHBTotalCountClaimantsStudents + ", ";
        header += sHBPercentageClaimantsStudents + ", ";
        header += sHBTotalCountLHACases + ", ";
        header += sHBPercentageLHACases + ", ";
        header += sCTBTotalCountClaimantsEmployed + ", ";
        header += sCTBPercentageClaimantsEmployed + ", ";
        header += sCTBTotalCountClaimantsSelfEmployed + ", ";
        header += sCTBPercentageClaimantsSelfEmployed + ", ";
        header += sCTBTotalCountClaimantsStudents + ", ";
        header += sCTBPercentageClaimantsStudents + ", ";
        header += sCTBTotalCountLHACases + ", ";
        header += sCTBPercentageLHACases + ", ";
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
            filename1 = summary.get(SHBEFilename1String);
            line += filename1 + ", ";
            line += getSingleTimeGenericLinePart(key, summary, doUnderOccupancy);
            line += summary.get(sTotalWeeklyHBEntitlement) + ", ";
            line += summary.get(sTotalCountWeeklyHBEntitlementNonZero) + ", ";
            line += summary.get(sAverageWeeklyHBEntitlement) + ", ";
            line += summary.get(sTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sTotalCountWeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sAverageWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sTotalCountWeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sAverageWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sTotalCountWeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sTotalContractualRentAmount) + ", ";
            line += summary.get(sTotalCountContractualRentAmountNonZeroCount) + ", ";
            line += summary.get(sAverageContractualRentAmount) + ", ";
            line += summary.get(sTotalWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sAverageWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sHBTotalCountClaimantsEmployed) + ", ";
            line += summary.get(sHBPercentageClaimantsEmployed) + ", ";
            line += summary.get(sHBTotalCountClaimantsSelfEmployed) + ", ";
            line += summary.get(sHBPercentageClaimantsSelfEmployed) + ", ";
            line += summary.get(sHBTotalCountClaimantsStudents) + ", ";
            line += summary.get(sHBPercentageClaimantsStudents) + ", ";
            line += summary.get(sHBTotalCountLHACases) + ", ";
            line += summary.get(sHBPercentageLHACases) + ", ";
            line += summary.get(sCTBTotalCountClaimantsEmployed) + ", ";
            line += summary.get(sCTBPercentageClaimantsEmployed) + ", ";
            line += summary.get(sCTBTotalCountClaimantsSelfEmployed) + ", ";
            line += summary.get(sCTBPercentageClaimantsSelfEmployed) + ", ";
            line += summary.get(sCTBTotalCountClaimantsStudents) + ", ";
            line += summary.get(sCTBPercentageClaimantsStudents) + ", ";
            line += summary.get(sCTBTotalCountLHACases) + ", ";
            line += summary.get(sCTBPercentageLHACases) + ", ";
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
            boolean doUnderOccupancy,
            int nTT,
            int nEG
    ) {
        File dirOut;
        dirOut = getSummaryTableDir(paymentType, includeKey, doUnderOccupancy);

        File outFile;
        outFile = new File(
                dirOut,
                summaryTable.firstKey() + "To" + summaryTable.lastKey() + "SingleTimeRentAndIncome.csv");
        PrintWriter pw;
        pw = Generic_StaticIO.getPrintWriter(outFile, false);
        // Write headers
        String header;
        header = "";
        header += SHBEFilename1String + ", ";
        header += getSingleTimeGenericHeaderPart(doUnderOccupancy);
        header += TotalIncomeString + ", ";
        header += TotalIncomeGreaterThanZeroCountString + ", ";
        header += AverageIncomeString + ", ";
        header += sTotalWeeklyEligibleRentAmount + ", ";
        header += sTotalCountWeeklyEligibleRentAmountNonZero + ", ";
        header += sAverageWeeklyEligibleRentAmount + ", ";
        for (int i = 1; i < nTT; i++) {
            header += TotalIncomeString + "TenancyType" + i + ", ";
            header += TotalIncomeGreaterThanZeroCountString + "TenancyType" + i + ", ";
            header += AverageIncomeString + "TenancyType" + i + ", ";
            header += sTotalWeeklyEligibleRentAmount + "TenancyType" + i + ", ";
            header += sTotalCountWeeklyEligibleRentAmountNonZero + "TenancyType" + i + ", ";
            header += sAverageWeeklyEligibleRentAmount + "TenancyType" + i + ", ";
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
            filename1 = summary.get(SHBEFilename1String);
            line += filename1 + ", ";
            line += getSingleTimeGenericLinePart(key, summary, doUnderOccupancy);
            line += summary.get(TotalIncomeString) + ", ";
            line += summary.get(TotalIncomeGreaterThanZeroCountString) + ", ";
            line += summary.get(AverageIncomeString) + ", ";
            line += summary.get(sTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sTotalCountWeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sAverageWeeklyEligibleRentAmount) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(TotalIncomeString + "TenancyType" + i) + ", ";
                line += summary.get(TotalIncomeGreaterThanZeroCountString + "TenancyType" + i) + ", ";
                line += summary.get(AverageIncomeString + "TenancyType" + i) + ", ";
                line += summary.get(sTotalWeeklyEligibleRentAmount + "TenancyType" + i) + ", ";
                line += summary.get(sTotalCountWeeklyEligibleRentAmountNonZero + "TenancyType" + i) + ", ";
                line += summary.get(sAverageWeeklyEligibleRentAmount + "TenancyType" + i) + ", ";
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimeDemographics(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        File dirOut;
        dirOut = getSummaryTableDir(paymentType, includeKey, underOccupancy);

        File outFile;
        outFile = new File(
                dirOut,
                summaryTable.firstKey() + "To" + summaryTable.lastKey() + "SingleTimeDemographics.csv");
        PrintWriter pw;
        pw = Generic_StaticIO.getPrintWriter(outFile, false);
        // Write headers
        String header;
        header = "";
        header += SHBEFilename1String + ", ";
        header += getSingleTimeGenericHeaderPart(underOccupancy);
        for (int i = 1; i < nEG; i++) {
            header += sAllTotalCountEthnicGroupClaimant[i] + ", ";
            header += sAllPercentageEthnicGroupClaimant[i] + ", ";
        }
        for (int i = 1; i < nEG; i++) {
            header += sHBTotalCountEthnicGroupClaimant[i] + ", ";
            header += sHBPercentageEthnicGroupClaimant[i] + ", ";
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
            filename1 = summary.get(SHBEFilename1String);
            line += filename1 + ", ";
            line += getSingleTimeGenericLinePart(key,
                    summary, underOccupancy);
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sAllTotalCountEthnicGroupClaimant[i]) + ", ";
                line += summary.get(sAllPercentageEthnicGroupClaimant[i]) + ", ";
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sHBTotalCountEthnicGroupClaimant[i]) + ", ";
                line += summary.get(sHBPercentageEthnicGroupClaimant[i]) + ", ";
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

    public void writeSummaryTableSingleTimeTenancyType(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        File dirOut;
        dirOut = getSummaryTableDir(paymentType, includeKey, underOccupancy);

        File outFile;
        outFile = new File(
                dirOut,
                summaryTable.firstKey() + "To" + summaryTable.lastKey() + "SingleTimeTenancyType.csv");
        PrintWriter pw;
        pw = Generic_StaticIO.getPrintWriter(outFile, false);
        // Write headers
        String header;
        header = "";
        header += SHBEFilename1String + ", ";
        header += getSingleTimeGenericHeaderPart(underOccupancy);
        for (int i = 1; i < nTT; i++) {
            header += sAllTotalCountTenancyTypeClaimant[i] + ", ";
            header += sAllPercentageTenancyTypeClaimant[i] + ", ";
            if (i == 8) {
                header += sAllTotalCountTenancyType8InLS277NSClaimant + ", ";
            }
        }
        for (int i = 1; i < nTT; i++) {
            header += sHBTotalCountTenancyTypeClaimant[i] + ", ";
            header += sHBPercentageTenancyTypeClaimant[i] + ", ";
            if (i == 8) {
                header += sHBTotalCountTenancyType8InLS277NSClaimant + ", ";
            }
        }
        for (int i = 1; i < nTT; i++) {
            header += sCTBTotalCountTenancyTypeClaimant[i] + ", ";
            header += sCTBPercentageTenancyTypeClaimant[i] + ", ";
            if (i == 8) {
                header += sCTBTotalCountTenancyType8InLS277NSClaimant + ", ";
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
            filename1 = summary.get(SHBEFilename1String);
            line += filename1 + ", ";
            line += getSingleTimeGenericLinePart(key,
                    summary, underOccupancy);
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sAllTotalCountTenancyTypeClaimant[i]) + ", ";
                line += summary.get(sAllPercentageTenancyTypeClaimant[i]) + ", ";
                if (i == 8) {
                    line += summary.get(sAllTotalCountTenancyType8InLS277NSClaimant) + ", ";
                }
            }
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sHBTotalCountTenancyTypeClaimant[i]) + ", ";
                line += summary.get(sHBPercentageTenancyTypeClaimant[i]) + ", ";
                if (i == 8) {
                    line += summary.get(sHBTotalCountTenancyType8InLS277NSClaimant) + ", ";
                }
            }
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sCTBTotalCountTenancyTypeClaimant[i]) + ", ";
                line += summary.get(sCTBPercentageTenancyTypeClaimant[i]) + ", ";
                if (i == 8) {
                    line += summary.get(sCTBTotalCountTenancyType8InLS277NSClaimant) + ", ";
                }
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimePassportedStandardIndicator(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String paymentType,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG,
            int nPSI
    ) {
        File dirOut;
        dirOut = getSummaryTableDir(paymentType, includeKey, underOccupancy);
        File outFile;
        outFile = new File(
                dirOut,
                summaryTable.firstKey() + "To" + summaryTable.lastKey() + "SingleTimePassportedStandardIndicator.csv");
        PrintWriter pw;
        pw = Generic_StaticIO.getPrintWriter(outFile, false);
        // Write headers
        String header;
        header = "";
        header += SHBEFilename1String + ", ";
        header += getSingleTimeGenericHeaderPart(underOccupancy);
        for (int i = 1; i < nPSI; i++) {
            header += TotalAllPassportedStandardIndicatorCountString[i] + ", ";
            header += PercentageAllPassportedStandardIndicatorCountString[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            header += TotalHBPassportedStandardIndicatorCountString[i] + ", ";
            header += PercentageHBPassportedStandardIndicatorCountString[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            header += TotalCTBPassportedStandardIndicatorCountString[i] + ", ";
            header += PercentageCTBPassportedStandardIndicatorCountString[i] + ", ";
        }
        for (int i = 1; i < nTT; i++) {
            header += AllTotalDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += AllPercentageDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += AllTotalSevereDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += AllPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += AllTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += AllPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
        }
        for (int i = 1; i < nTT; i++) {
            header += HBTotalDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += HBPercentageDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += HBTotalSevereDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += HBPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += HBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += HBPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
        }
        for (int i = 1; i < nTT; i++) {
            header += CTBTotalDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += CTBPercentageDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += CTBTotalSevereDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += CTBPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += CTBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += CTBPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
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
            filename1 = summary.get(SHBEFilename1String);
            line += filename1 + ", ";
            line += getSingleTimeGenericLinePart(key,
                    summary, underOccupancy);
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(TotalAllPassportedStandardIndicatorCountString[i]) + ", ";
                line += summary.get(PercentageAllPassportedStandardIndicatorCountString[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(TotalHBPassportedStandardIndicatorCountString[i]) + ", ";
                line += summary.get(PercentageHBPassportedStandardIndicatorCountString[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(TotalCTBPassportedStandardIndicatorCountString[i]) + ", ";
                line += summary.get(PercentageCTBPassportedStandardIndicatorCountString[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                for (int j = 1; j < nTT; j++) {
                    line += summary.get(TotalAllPassportedStandardIndicatorByTenancyTypeCountString[i][j]) + ", ";
                    line += summary.get(PercentageAllPassportedStandardIndicatorByTenancyTypeCountString[i][j]) + ", ";
                }
            }
            for (int i = 1; i < nPSI; i++) {
                for (int j = 1; j < nTT; j++) {
                    line += summary.get(TotalHBPassportedStandardIndicatorByTenancyTypeCountString[i][j]) + ", ";
                    line += summary.get(PercentageHBPassportedStandardIndicatorByTenancyTypeCountString[i][j]) + ", ";
                }
            }
            for (int i = 1; i < nPSI; i++) {
                for (int j = 1; j < nTT; j++) {
                    line += summary.get(TotalCTBPassportedStandardIndicatorByTenancyTypeCountString[i][j]) + ", ";
                    line += summary.get(PercentageCTBPassportedStandardIndicatorByTenancyTypeCountString[i][j]) + ", ";
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
        File dirOut;
        dirOut = getSummaryTableDir(paymentType, includeKey, underOccupancy);
        File outFile;
        outFile = new File(
                dirOut,
                summaryTable.firstKey() + "To" + summaryTable.lastKey() + "SingleTimeDisability.csv");
        PrintWriter pw;
        pw = Generic_StaticIO.getPrintWriter(outFile, false);
        // Write headers
        String header;
        header = "";
        header += SHBEFilename1String + ", ";
        header += getSingleTimeGenericHeaderPart(underOccupancy);
        for (int i = 1; i < nTT; i++) {
            header += AllTotalDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += AllPercentageDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += AllTotalSevereDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += AllPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += AllTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += AllPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
        }
        for (int i = 1; i < nTT; i++) {
            header += HBTotalDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += HBPercentageDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += HBTotalSevereDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += HBPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += HBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += HBPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
        }
        for (int i = 1; i < nTT; i++) {
            header += CTBTotalDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += CTBPercentageDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += CTBTotalSevereDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += CTBPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += CTBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
            header += CTBPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i] + ", ";
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
            filename1 = summary.get(SHBEFilename1String);
            line += filename1 + ", ";
            line += getSingleTimeGenericLinePart(key,
                    summary, underOccupancy);
            for (int i = 1; i < nTT; i++) {
                line += summary.get(AllTotalDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
                line += summary.get(AllPercentageDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
                line += summary.get(AllTotalSevereDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
                line += summary.get(AllPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
                line += summary.get(AllTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
                line += summary.get(AllPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
            }
            for (int i = 1; i < nTT; i++) {
                line += summary.get(HBTotalDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
                line += summary.get(HBPercentageDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
                line += summary.get(HBTotalSevereDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
                line += summary.get(HBPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
                line += summary.get(HBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
                line += summary.get(HBPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
            }
            for (int i = 1; i < nTT; i++) {
                line += summary.get(CTBTotalDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
                line += summary.get(CTBPercentageDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
                line += summary.get(CTBTotalSevereDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
                line += summary.get(CTBPercentageSevereDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
                line += summary.get(CTBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
                line += summary.get(CTBPercentageEnhancedDisabilityPremiumAwardByTenancyTypeCountString[i]) + ", ";
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    private String getSingleTimeGenericHeaderPart(boolean doUnderoccupancy) {
        String result;
        result = "year-month, ";
        if (doUnderoccupancy) {
            result += "CouncilFilename, "
                    + "CouncilCount, "
                    + "CouncilLinkedRecordsCount,"
                    + "RSLFilename, "
                    //+ sRSLCount + ", "
                    + "RSLCount, "
                    + "RSLLinkedRecordsCount, "
                    + "AllCount, "
                    + "AllLinkedRecordsCount";
        } else {
            result += sAllCount + ", ";
            result += sHBCount + ", ";
            result += sCTBCount + ", ";
        }
        return result;
    }

    private String getSingleTimeGenericLinePart(
            String key,
            HashMap<String, String> summary,
            boolean doUnderOccupancy) {
        String result;
        result = key + ", ";
        if (doUnderOccupancy) {
            result += summary.get(CouncilFilename1String) + ", ";
            result += summary.get(CouncilCount1String) + ", ";
            result += summary.get(CouncilLinkedRecordCount1String) + ", ";
            result += summary.get(RSLFilename1String) + ", ";
            result += summary.get(RSLCount1String) + ", ";
            result += summary.get(RSLLinkedRecordCount1String) + ", ";
            result += summary.get(AllCount1String) + ", ";
//                    (Integer.valueOf(summary.get("CouncilCount"))
//                    + Integer.valueOf(summary.get("RSLCount")))) + ", ";
            result += summary.get(AllLinkedRecordCount1String) + ", ";
        } else {
            result += summary.get(sAllCount) + ", ";
            result += summary.get(sHBCount) + ", ";
            result += summary.get(sCTBCount) + ", ";
        }
        return result;
    }
}
