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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Record;
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
    // Single
    private static String[] sCountTT;

    // All
    private static final String sAllTotalHouseholdSize = "AllTotalHouseholdSize";
    private static final String sAllAverageHouseholdSize = "AllAverageHouseholdSize";
    private static final String sHBTotalHouseholdSize = "HBTotalHouseholdSize";
    private static final String sHBAverageHouseholdSize = "HBAverageHouseholdSize";
    private static final String sCTBTotalHouseholdSize = "CTBTotalHouseholdSize";
    private static final String sCTBAverageHouseholdSize = "CTBAverageHouseholdSize";

    private static String[] sTotalAllPassportedStandardIndicatorCountString;
    private static String[] sTotalHBPassportedStandardIndicatorCountString;
    private static String[] sTotalCTBPassportedStandardIndicatorCountString;
    private static String[] sPercentageAllPassportedStandardIndicatorCountString;
    private static String[] sPercentageHBPassportedStandardIndicatorCountString;
    private static String[] sPercentageCTBPassportedStandardIndicatorCountString;
    private static String[][] sTotalAllPassportedStandardIndicatorByTenancyTypeCountString;
    private static String[][] sTotalHBPassportedStandardIndicatorByTenancyTypeCountString;
    private static String[][] sTotalCTBPassportedStandardIndicatorByTenancyTypeCountString;
    private static String[][] sPercentageAllPassportedStandardIndicatorByTenancyTypeCountString;
    private static String[][] sPercentageHBPassportedStandardIndicatorByTenancyTypeCountString;
    private static String[][] sPercentageCTBPassportedStandardIndicatorByTenancyTypeCountString;

    private static String[] sAllTotalCountDisabilityPremiumAwardByTenancyType;
    private static String[] sHBTotalCountDisabilityPremiumAwardByTenancyType;
    private static String[] sCTBTotalCountDisabilityPremiumAwardByTenancyType;
    private static String[] sAllPercentageDisabilityPremiumAwardByTenancyType;
    private static String[] sHBPercentageDisabilityPremiumAwardByTenancyType;
    private static String[] sCTBPercentageDisabilityPremiumAwardByTenancyType;
    private static String[] sAllTotalCountSevereDisabilityPremiumAwardByTenancyType;
    private static String[] sHBTotalCountSevereDisabilityPremiumAwardByTenancyType;
    private static String[] sCTBTotalCountSevereDisabilityPremiumAwardByTenancyType;
    private static String[] sAllPercentageSevereDisabilityPremiumAwardByTenancyType;
    private static String[] sHBPercentageSevereDisabilityPremiumAwardByTenancyType;
    private static String[] sCTBPercentageSevereDisabilityPremiumAwardByTenancyType;
    private static String[] sAllTotalCountEnhancedDisabilityPremiumAwardByTenancyType;
    private static String[] sHBTotalCountEnhancedDisabilityPremiumAwardByTenancyType;
    private static String[] sCTBTotalCountEnhancedDisabilityPremiumAwardByTenancyType;
    private static String[] sAllPercentageEnhancedDisabilityPremiumAwardByTenancyType;
    private static String[] sHBPercentageEnhancedDisabilityPremiumAwardByTenancyType;
    private static String[] sCTBPercentageEnhancedDisabilityPremiumAwardByTenancyType;

    private static final String sTotalWeeklyHBEntitlement = "TotalWeeklyHBEntitlement";
    private static final String sTotalCountWeeklyHBEntitlementNonZero = "TotalCountWeeklyHBEntitlementNonZero";
    private static final String sTotalCountWeeklyHBEntitlementZero = "TotalCountWeeklyHBEntitlementZero";
    private static final String sAverageWeeklyHBEntitlement = "AverageWeeklyHBEntitlement";
    private static final String sTotalWeeklyCTBEntitlement = "TotalWeeklyCTBEntitlement";
    private static final String sTotalCountWeeklyCTBEntitlementNonZero = "TotalCountWeeklyCTBEntitlementNonZero";
    private static final String sTotalCountWeeklyCTBEntitlementZero = "TotalCountWeeklyCTBEntitlementZero";
    private static final String sAverageWeeklyCTBEntitlement = "AverageWeeklyCTBEntitlement";
    private static final String sTotalWeeklyEligibleRentAmount = "TotalWeeklyEligibleRentAmount";
    private static final String sTotalCountWeeklyEligibleRentAmountNonZero = "TotalCountWeeklyEligibleRentAmountNonZero";
    private static final String sTotalCountWeeklyEligibleRentAmountZero = "TotalCountWeeklyEligibleRentAmountZero";
    private static final String sAverageWeeklyEligibleRentAmount = "AverageWeeklyEligibleRentAmount";

    private static final String sHBTotalWeeklyHBEntitlement = "HBTotalWeeklyHBEntitlement";
    private static final String sHBTotalCountWeeklyHBEntitlementNonZero = "HBTotalCountWeeklyHBEntitlementNonZero";
    private static final String sHBTotalCountWeeklyHBEntitlementZero = "HBTotalCountWeeklyHBEntitlementZero";
    private static final String sHBAverageWeeklyHBEntitlement = "HBAverageWeeklyHBEntitlement";
    private static final String sHBTotalWeeklyCTBEntitlement = "HBTotalWeeklyCTBEntitlement";
    private static final String sHBTotalCountWeeklyCTBEntitlementNonZero = "HBTotalCountWeeklyCTBEntitlementNonZero";
    private static final String sHBTotalCountWeeklyCTBEntitlementZero = "HBTotalCountWeeklyCTBEntitlementZero";
    private static final String sHBAverageWeeklyCTBEntitlement = "HBAverageWeeklyCTBEntitlement";
    private static final String sHBTotalWeeklyEligibleRentAmount = "HBTotalWeeklyEligibleRentAmount";
    private static final String sHBTotalCountWeeklyEligibleRentAmountNonZero = "HBTotalCountWeeklyEligibleRentAmountNonZero";
    private static final String sHBTotalCountWeeklyEligibleRentAmountZero = "HBTotalCountWeeklyEligibleRentAmountZero";
    private static final String sHBAverageWeeklyEligibleRentAmount = "HBAverageWeeklyEligibleRentAmount";

    private static final String sCTBTotalWeeklyHBEntitlement = "CTBTotalWeeklyHBEntitlement";
    private static final String sCTBTotalCountWeeklyHBEntitlementNonZero = "CTBTotalCountWeeklyHBEntitlementNonZero";
    private static final String sCTBTotalCountWeeklyHBEntitlementZero = "CTBTotalCountWeeklyHBEntitlementZero";
    private static final String sCTBAverageWeeklyHBEntitlement = "CTBAverageWeeklyHBEntitlement";
    private static final String sCTBTotalWeeklyCTBEntitlement = "CTBTotalWeeklyCTBEntitlement";
    private static final String sCTBTotalCountWeeklyCTBEntitlementNonZero = "CTBTotalCountWeeklyCTBEntitlementNonZero";
    private static final String sCTBTotalCountWeeklyCTBEntitlementZero = "CTBTotalCountWeeklyCTBEntitlementZero";
    private static final String sCTBAverageWeeklyCTBEntitlement = "CTBAverageWeeklyCTBEntitlement";
    private static final String sCTBTotalWeeklyEligibleRentAmount = "CTBTotalWeeklyEligibleRentAmount";
    private static final String sCTBTotalCountWeeklyEligibleRentAmountNonZero = "CTBTotalCountWeeklyEligibleRentAmountNonZero";
    private static final String sCTBTotalCountWeeklyEligibleRentAmountZero = "CTBTotalCountWeeklyEligibleRentAmountZero";
    private static final String sCTBAverageWeeklyEligibleRentAmount = "CTBAverageWeeklyEligibleRentAmount";
    // All
    private static final String sAllTotalWeeklyEligibleCouncilTaxAmount = "AllTotalWeeklyEligibleCouncilTaxAmount";
    private static final String sAllTotalCountWeeklyEligibleCouncilTaxAmountNonZero = "AllTotalCountWeeklyEligibleCouncilTaxAmountNonZero";
    private static final String sAllTotalCountWeeklyEligibleCouncilTaxAmountZero = "AllTotalCountWeeklyEligibleCouncilTaxAmountZero";
    private static final String sAllAverageWeeklyEligibleCouncilTaxAmount = "AllAverageWeeklyEligibleCouncilTaxAmount";
    private static final String sAllTotalContractualRentAmount = "AllTotalContractualRentAmount";
    private static final String sAllTotalCountContractualRentAmountNonZeroCount = "AllTotalCountContractualRentAmountNonZero";
    private static final String sAllTotalCountContractualRentAmountZeroCount = "AllTotalCountContractualRentAmountZero";
    private static final String sAllAverageContractualRentAmount = "AllAverageContractualRentAmount";
    private static final String sAllTotalWeeklyAdditionalDiscretionaryPayment = "AllTotalWeeklyAdditionalDiscretionaryPayment";
    private static final String sAllTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero = "AllTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero";
    private static final String sAllTotalCountWeeklyAdditionalDiscretionaryPaymentZero = "AllTotalCountWeeklyAdditionalDiscretionaryPaymentZero";
    private static final String sAllAverageWeeklyAdditionalDiscretionaryPayment = "AllAverageWeeklyAdditionalDiscretionaryPayment";
    private static final String sAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    private static final String sAllTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "AllTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    private static final String sAllTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "AllTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    private static final String sAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "AllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    private static final String sAllTotalCountClaimantsEmployed = "AllTotalCountClaimantsEmployed";
    private static final String sAllPercentageClaimantsEmployed = "AllPercentageClaimantsEmployed";
    private static final String sAllTotalCountClaimantsSelfEmployed = "AllTotalCountClaimantsSelfEmployed";
    private static final String sAllPercentageClaimantsSelfEmployed = "AllPercentageClaimantsSelfEmployed";
    private static final String sAllTotalCountClaimantsStudents = "AllTotalCountClaimantsStudents";
    private static final String sAllPercentageClaimantsStudents = "AllPercentageClaimantsStudents";
    private static final String sAllTotalCountLHACases = "AllTotalCountLHACases";
    private static final String sAllPercentageLHACases = "AllPercentageLHACases";
    // HB
    private static final String sHBTotalWeeklyEligibleCouncilTaxAmount = "HBTotalCountWeeklyEligibleCouncilTaxAmount";
    private static final String sHBTotalCountWeeklyEligibleCouncilTaxAmountNonZero = "HBTotalCountWeeklyEligibleCouncilTaxAmountNonZero";
    private static final String sHBTotalCountWeeklyEligibleCouncilTaxAmountZero = "HBTotalCountWeeklyEligibleCouncilTaxAmountZero";
    private static final String sHBAverageWeeklyEligibleCouncilTaxAmount = "HBAverageWeeklyEligibleCouncilTaxAmount";
    private static final String sHBTotalContractualRentAmount = "HBTotalContractualRentAmount";
    private static final String sHBTotalCountContractualRentAmountNonZeroCount = "HBTotalCountContractualRentAmountNonZero";
    private static final String sHBTotalCountContractualRentAmountZeroCount = "HBTotalCountContractualRentAmountZero";
    private static final String sHBAverageContractualRentAmount = "HBAverageContractualRentAmount";
    private static final String sHBTotalWeeklyAdditionalDiscretionaryPayment = "HBTotalWeeklyAdditionalDiscretionaryPayment";
    private static final String sHBTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero = "HBTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero";
    private static final String sHBTotalCountWeeklyAdditionalDiscretionaryPaymentZero = "HBTotalCountWeeklyAdditionalDiscretionaryPaymentZero";
    private static final String sHBAverageWeeklyAdditionalDiscretionaryPayment = "HBAverageWeeklyAdditionalDiscretionaryPayment";
    private static final String sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    private static final String sHBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "HBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    private static final String sHBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "HBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    private static final String sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "HBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    private static final String sHBTotalCountClaimantsEmployed = "HBTotalCountClaimantsEmployed";
    private static final String sHBPercentageClaimantsEmployed = "HBPercentageClaimantsEmployed";
    private static final String sHBTotalCountClaimantsSelfEmployed = "HBTotalCountClaimantsSelfEmployed";
    private static final String sHBPercentageClaimantsSelfEmployed = "HBPercentageClaimantsSelfEmployed";
    private static final String sHBTotalCountClaimantsStudents = "HBTotalCountClaimantsStudents";
    private static final String sHBPercentageClaimantsStudents = "HBPercentageClaimantsStudents";
    private static final String sHBTotalCountLHACases = "HBTotalCountLHACases";
    private static final String sHBPercentageLHACases = "HBPercentageLHACases";
    // CTB
    private static final String sCTBTotalWeeklyEligibleCouncilTaxAmount = "CTBTotalWeeklyEligibleCouncilTaxAmount";
    private static final String sCTBTotalCountWeeklyEligibleCouncilTaxAmountNonZero = "CTBTotalCountWeeklyEligibleCouncilTaxAmountNonZero";
    private static final String sCTBTotalCountWeeklyEligibleCouncilTaxAmountZero = "CTBTotalCountWeeklyEligibleCouncilTaxAmountZero";
    private static final String sCTBAverageWeeklyEligibleCouncilTaxAmount = "CTBAverageWeeklyEligibleCouncilTaxAmount";
    private static final String sCTBTotalContractualRentAmount = "CTBTotalContractualRentAmount";
    private static final String sCTBTotalCountContractualRentAmountNonZeroCount = "CTBTotalCountContractualRentAmountNonZero";
    private static final String sCTBTotalCountContractualRentAmountZeroCount = "CTBTotalCountContractualRentAmountZero";
    private static final String sCTBAverageContractualRentAmount = "CTBAverageContractualRentAmount";
    private static final String sCTBTotalWeeklyAdditionalDiscretionaryPayment = "CTBTotalWeeklyAdditionalDiscretionaryPayment";
    private static final String sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero = "CTBTotalCountWeeklyAdditionalDiscretionaryPaymentNonZero";
    private static final String sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentZero = "CTBTotalCountWeeklyAdditionalDiscretionaryPaymentZero";
    private static final String sCTBAverageWeeklyAdditionalDiscretionaryPayment = "CTBAverageWeeklyAdditionalDiscretionaryPayment";
    private static final String sCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    private static final String sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "CTBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    private static final String sCTBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "CTBTotalCountWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    private static final String sCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "CTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    private static final String sCTBTotalCountClaimantsEmployed = "CTBTotalCountClaimantsEmployed";
    private static final String sCTBPercentageClaimantsEmployed = "CTBPercentageClaimantsEmployed";
    private static final String sCTBTotalCountClaimantsSelfEmployed = "CTBTotalCountClaimantsSelfEmployed";
    private static final String sCTBPercentageClaimantsSelfEmployed = "CTBPercentageClaimantsSelfEmployed";
    private static final String sCTBTotalCountClaimantsStudents = "CTBTotalCountClaimantsStudents";
    private static final String sCTBPercentageClaimantsStudents = "CTBPercentageClaimantsStudents";
    private static final String sCTBTotalCountLHACases = "CTBTotalCountLHACases";
    private static final String sCTBPercentageLHACases = "CTBPercentageLHACases";

    private static final String sAllCount0 = "AllCount0";
    private static final String sHBCount0 = "HBCount0";
    private static final String sCTBCount0 = "CTBOnlyCount0";
    private static final String sAllCount1 = "AllCount1";
    private static final String sHBCount1 = "HBCount1";
    private static final String sCTBCount1 = "CTBOnlyCount1";

    private static String[] sAllTotalCountEthnicGroupClaimant;
    private static String[] sAllPercentageEthnicGroupClaimant;
    private static String[] sAllTotalCountTenancyTypeClaimant;
    private static String[] sAllPercentageTenancyTypeClaimant;
    private static String sAllPostcodeValidFormatCount;
    private static String sAllPostcodeValidCount;

    private static final String sTotalIncome = "TotalIncome";
    private static final String sTotalIncomeGreaterThanZeroCount = "TotalIncomeGreaterThanZeroCount";
    private static final String sAverageIncome = "AverageIncome";

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
    private static final String aAllUOLinkedRecordCount0 = "AllLinkedRecordCount0";
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
    private static String sTotalRentArrears;
    private static String sAverageRentArrears;
    private static String sGreaterThan0AverageRentArrears;

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
    private static String sCTBPostcodeValidFormatCount;
    private static String sCTBPostcodeValidCount;

    // Compare 2 Times
    // General
    private static final String sTotalCountHBTenancyTypesToCTBTenancyTypes = "TotalCountHBTenancyTypesToCTBTenancyTypes";
    private static final String sTotalCountCTBTenancyTypesToHBTenancyTypes = "TotalCountCTBTenancyTypesToHBBTenancyTypes";
    // General HB related
    private static final String sTotalCountHBTenancyTypesToHBTenancyTypes = "TotalCountHBTenancyTypesToHBTenancyTypes";
    private static final String sTotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes = "TotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes";
    private static final String sPercentageSocialTenancyTypesToPrivateDeregulatedTenancyTypes = "PercentageSocialTenancyTypesToPrivateDeregulatedTenancyTypes";
    private static final String sTotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes = "TotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes";
    private static final String sPercentagePrivateDeregulatedTenancyTypesToSocialTenancyTypes = "PercentagePrivateDeregulatedTenancyTypesToSocialTenancyTypes";
    private static final String sTotalCountTenancyType1ToPrivateDeregulatedTenancyTypes = "TotalCountTenancyType1ToPrivateDeregulatedTenancyTypes";
    private static final String sPercentageTenancyType1ToPrivateDeregulatedTenancyTypes = "PercentageTenancyType1ToPrivateDeregulatedTenancyTypes";
    private static final String sTotalCountTenancyType4ToPrivateDeregulatedTenancyTypes = "TotalCountTenancyType4ToPrivateDeregulatedTenancyTypes";
    private static final String sPercentageTenancyType4ToPrivateDeregulatedTenancyTypes = "PercentageTenancyType4ToPrivateDeregulatedTenancyTypes";
    private static final String sTotalCountPrivateDeregulatedTenancyTypesToTenancyType1 = "TotalCountPrivateDeregulatedTenancyTypesToTenancyType1";
    private static final String sPercentagePrivateDeregulatedTenancyTypesToTenancyType1 = "PercentagePrivateDeregulatedTenancyTypesToTenancyType1";
    private static final String sTotalCountPrivateDeregulatedTenancyTypesToTenancyType4 = "TotalCountPrivateDeregulatedTenancyTypesToTenancyType4";
    private static final String sPercentagePrivateDeregulatedTenancyTypesToTenancyType4 = "PercentagePrivateDeregulatedTenancyTypesToTenancyType4";
    private static final String sTotalCountPostcodeChangeWithinSocialTenancyTypes = "TotalCountPostcodeChangeWithinSocialTenancyTypes";
    private static final String sPercentagePostcodeChangeWithinSocialTenancyTypes = "PercentagePostcodeChangeWithinSocialTenancyTypes";
    private static final String sTotalCountPostcodeChangeWithinTenancyType1 = "TotalCountPostcodeChangeWithinTenancyType1";
    private static final String sPercentagePostcodeChangeWithinTenancyType1 = "PercentagePostcodeChangeWithinTenancyType1";
    private static final String sTotalCountPostcodeChangeWithinTenancyType4 = "TotalCountPostcodeChangeWithinTenancyType4";
    private static final String sPercentagePostcodeChangeWithinTenancyType4 = "PercentagePostcodeChangeWithinTenancyType4";
    private static final String sTotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes = "TotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes";
    private static final String sPercentagePostcodeChangeWithinPrivateDeregulatedTenancyTypes = "PercentagePostcodeChangeWithinPrivateDeregulatedTenancyTypes";
    // General CTB related
    private static final String sTotalCountSocialTenancyTypesToCTBTenancyTypes = "TotalCountSocialTenancyTypesToCTBTenancyTypes";
    private static final String sPercentageSocialTenancyTypesToCTBTenancyTypes = "PercentageSocialTenancyTypesToCTBTenancyTypes";
    private static final String sTotalCountTenancyType1ToCTBTenancyTypes = "TotalCountTenancyType1ToCTBTenancyTypes";
    private static final String sPercentageTenancyType1ToCTBTenancyTypes = "PercentageTenancyType1ToCTBTenancyTypes";
    private static final String sTotalCountTenancyType4ToCTBTenancyTypes = "TotalCountTenancyType4ToCTBTenancyTypes";
    private static final String sPercentageTenancyType4ToCTBTenancyTypes = "PercentageTenancyType4ToCTBTenancyTypes";
    private static final String sTotalCountPrivateDeregulatedTenancyTypesToCTBTenancyTypes = "TotalCountPrivateDeregulatedTenancyTypesToCTBTenancyTypes";
    private static final String sPercentagePrivateDeregulatedTenancyTypesToCTBTenancyTypes = "PercentagePrivateDeregulatedTenancyTypesToCTBTenancyTypes";
    private static final String sTotalCountCTBTenancyTypesToSocialTenancyTypes = "TotalCountCTBTenancyTypesToSocialTenancyTypes";
    private static final String sPercentageCTBTenancyTypesToSocialTenancyTypes = "PercentageCTBTenancyTypesToSocialTenancyTypes";
    private static final String sTotalCountCTBTenancyTypesToTenancyType1 = "TotalCountCTBTenancyTypesToTenancyType1";
    private static final String sPercentageCTBTenancyTypesToTenancyType1 = "PercentageCTBTenancyTypesToTenancyType1";
    private static final String sTotalCountCTBTenancyTypesToTenancyType4 = "TotalCountCTBTenancyTypesToTenancyType4";
    private static final String sPercentageCTBTenancyTypesToTenancyType4 = "PercentageCTBTenancyTypesToTenancyType4";
    private static final String sTotalCountCTBTenancyTypesToPrivateDeregulatedTenancyTypes = "TotalCountCTBTenancyTypesToPrivateDeregulatedTypes";
    private static final String sPercentageCTBTenancyTypesToPrivateDeregulatedTenancyTypes = "PercentageCTBTenancyTypesToPrivateDeregulatedTypes";
    // All
    private static final String sAllTotalCountTenancyTypeChangeClaimant = "AllTotalCountTenancyTypeChangeClaimant";
    private static final String sAllPercentageTenancyTypeChangeClaimant = "AllPercentageTenancyTypeChangeClaimant";
    // HB
    private static final String sHBTotalCountTenancyTypeChangeClaimant = "HBTotalCountTenancyTypeChangeClaimant";
    private static final String sHBPercentageTenancyTypeChangeClaimant = "HBPercentageTenancyTypeChangeClaimant";
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
    // CTB
    private static final String sCTBTotalCountTenancyTypeChangeClaimant = "CTBTotalCountTenancyTypeChangeClaimant";
    private static final String sCTBPercentageTenancyTypeChangeClaimant = "CTBPercentageTenancyTypeChangeClaimant";
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
    private static double AllTotalWeeklyHBEntitlement;
    private static int AllTotalWeeklyHBEntitlementNonZeroCount;
    private static int AllTotalWeeklyHBEntitlementZeroCount;
    private static double AllTotalWeeklyCTBEntitlement;
    private static int AllTotalWeeklyCTBEntitlementNonZeroCount;
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
    private static int HBTotalWeeklyHBEntitlementNonZeroCount;
    private static int HBTotalWeeklyHBEntitlementZeroCount;
    private static double HBTotalWeeklyCTBEntitlement;
    private static int HBTotalWeeklyCTBEntitlementNonZeroCount;
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
    private static int CTBTotalWeeklyHBEntitlementNonZeroCount;
    private static int CTBTotalWeeklyHBEntitlementZeroCount;
    private static double CTBTotalWeeklyCTBEntitlement;
    private static int CTBTotalWeeklyCTBEntitlementNonZeroCount;
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
    private static int HBTotalCountEmployedClaimants;
    private static int HBTotalCountSelfEmployedClaimants;
    private static int HBTotalCountStudentsClaimants;
    private static int CTBTotalCountCTBEmployedClaimants;
    private static int CTBTotalCountSelfEmployedClaimants;
    private static int CTBTotalCountStudentsClaimants;
    // HLA
    private static int HBTotalCountLHACases;
    private static int CTBTotalCountLHACases;

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

    private static int[] AllTotalCountTenancyTypeClaimant1;
    private static int[] AllTotalCountTenancyTypeClaimant0;
    private static long AllTotalHouseholdSize;
    private static long HBTotalHouseholdSize;
    private static long CTBTotalHouseholdSize;
    private static int AllCount1;
    private static int AllCount0;
    // HB
    private static int HBCount1;
    private static int HBCount0;
    private static int[] HBTenancyTypeCount;
    private static int[] HBEthnicGroupCount;
    private static int HBPostcodeValidFormatCount;
    private static int HBPostcodeValidCount;
    // CTB
    private static int CTBCount1;
    private static int CTBCount0;
    private static int[] CTBTenancyTypeCount;
    private static int[] CTBEthnicGroupCount;
    private static int CTBPostcodeValidFormatCount;
    private static int CTBPostcodeValidCount;
    // Other summary stats
    private static double totalRentArrears;
    private static double rentArrearsCount;
    private static double greaterThan0RentArrearsCount;

    // Compare 2 Times
    // General
    private static int TotalCountHBTenancyTypesToCTBTenancyTypes;
    private static int TotalCountCTBTenancyTypesToHBTenancyTypes;
    // General HB related
    private static int TotalCountHBTenancyTypesToHBTenancyTypes;
    private static int TotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes;
    private static int TotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes;
    private static int TotalCountTenancyType1ToPrivateDeregulatedTenancyTypes;
    private static int TotalCountTenancyType4ToPrivateDeregulatedTenancyTypes;
    private static int TotalCountPrivateDeregulatedTenancyTypesToTenancyType1;
    private static int TotalCountPrivateDeregulatedTenancyTypesToTenancyType4;
    private static int TotalCountPostcodeChangeWithinSocialTenancyTypes;
    private static int TotalCountPostcodeChangeWithinTenancyType1;
    private static int TotalCountPostcodeChangeWithinTenancyType4;
    private static int TotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes;
    // General CTB related
    private static int TotalCountSocialTenancyTypesToCTBTenancyTypes;
    private static int TotalCountTenancyType1ToCTBTenancyTypes;
    private static int TotalCountTenancyType4ToCTBTenancyTypes;
    private static int TotalCountPrivateDeregulatedTenancyTypesToCTBTenancyTypes;
    private static int TotalCountCTBTenancyTypesToSocialTenancyTypes;
    private static int TotalCountCTBTenancyTypesToTenancyType1;
    private static int TotalCountCTBTenancyTypesToTenancyType4;
    private static int TotalCountCTBTenancyTypesToPrivateDeregulatedTenancyTypes;
    // All
    private static int AllTotalCountTenancyTypeChangeClaimant;
    // HB
    private static int HBTotalCountTenancyTypeChangeClaimant;
    private static int HBTotalCountPostcode0ValidPostcode1Valid;
    private static int HBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged;
    private static int HBTotalCountPostcode0ValidPostcode1ValidPostcodeChange;
    private static int HBTotalCountPostcode0ValidPostcode1NotValid;
    private static int HBTotalCountPostcode0NotValidPostcode1Valid;
    private static int HBTotalCountPostcode0NotValidPostcode1NotValid;
    private static int HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged;
    private static int HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged;
    // CTB
    private static int CTBTotalCountTenancyTypeChangeClaimant;
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
    private static int[] SameTenancyIIITenancyType;
    private static int[] SameTenancyIOITenancyType;
    private static int[] SameTenancyOIOTenancyType;
    private static int[] SameTenancyAndPostcodeIIITenancyType;
    private static int[] SameTenancyAndPostcodeIOITenancyType;
    private static int[] SameTenancyAndPostcodeOIOTenancyType;

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
        AllTotalCountTenancyTypeClaimant1 = new int[nTT];
        AllTotalCountTenancyTypeClaimant0 = new int[nTT];
        HBEthnicGroupCount = new int[nEG];
        CTBEthnicGroupCount = new int[nEG];
        HBTenancyTypeCount = new int[nTT];
        CTBTenancyTypeCount = new int[nTT];
    }

    protected final void initSingleTimeStrings(int nTT, int nEG, int nPSI) {
        sCountTT = new String[nTT];
        sTotalAllPassportedStandardIndicatorCountString = new String[nPSI];
        sTotalHBPassportedStandardIndicatorCountString = new String[nPSI];
        sTotalCTBPassportedStandardIndicatorCountString = new String[nPSI];
        sPercentageAllPassportedStandardIndicatorCountString = new String[nPSI];
        sPercentageHBPassportedStandardIndicatorCountString = new String[nPSI];
        sPercentageCTBPassportedStandardIndicatorCountString = new String[nPSI];
        sTotalAllPassportedStandardIndicatorByTenancyTypeCountString = new String[nPSI][nTT];
        sTotalHBPassportedStandardIndicatorByTenancyTypeCountString = new String[nPSI][nTT];
        sTotalCTBPassportedStandardIndicatorByTenancyTypeCountString = new String[nPSI][nTT];
        sPercentageAllPassportedStandardIndicatorByTenancyTypeCountString = new String[nPSI][nTT];
        sPercentageHBPassportedStandardIndicatorByTenancyTypeCountString = new String[nPSI][nTT];
        sPercentageCTBPassportedStandardIndicatorByTenancyTypeCountString = new String[nPSI][nTT];
        for (int i = 1; i < nPSI; i++) {
            sTotalAllPassportedStandardIndicatorCountString[i] = "TotalAllPassportedStandardIndicator" + i + "Count";
            sTotalHBPassportedStandardIndicatorCountString[i] = "TotalHBPassportedStandardIndicator" + i + "Count";
            sTotalCTBPassportedStandardIndicatorCountString[i] = "TotalCTBPassportedStandardIndicator" + i + "Count";
            sPercentageAllPassportedStandardIndicatorCountString[i] = "PercentageAllPassportedStandardIndicator" + i + "Count";
            sPercentageHBPassportedStandardIndicatorCountString[i] = "PercentageHBPassportedStandardIndicator" + i + "Count";
            sPercentageCTBPassportedStandardIndicatorCountString[i] = "PercentageCTBPassportedStandardIndicator" + i + "Count";
            for (int j = 1; j < nTT; j++) {
                sTotalAllPassportedStandardIndicatorByTenancyTypeCountString[i][j] = "TotalAllPassportedStandardIndicator" + i + "TenancyType" + j + "Count";
                sTotalHBPassportedStandardIndicatorByTenancyTypeCountString[i][j] = "TotalHBPassportedStandardIndicator" + i + "TenancyType" + j + "Count";
                sTotalCTBPassportedStandardIndicatorByTenancyTypeCountString[i][j] = "TotalCTBPassportedStandardIndicator" + i + "TenancyType" + j + "Count";
                sPercentageAllPassportedStandardIndicatorByTenancyTypeCountString[i][j] = "PercentageAllPassportedStandardIndicator" + i + "TenancyType" + j + "Count";
                sPercentageHBPassportedStandardIndicatorByTenancyTypeCountString[i][j] = "PercentageHBPassportedStandardIndicator" + i + "TenancyType" + j + "Count";
                sPercentageCTBPassportedStandardIndicatorByTenancyTypeCountString[i][j] = "PercentageCTBPassportedStandardIndicator" + i + "TenancyType" + j + "Count";
            }
        }

        // All
        sAllTotalCountEthnicGroupClaimant = new String[nEG];
        sAllPercentageEthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            sAllTotalCountEthnicGroupClaimant[i] = "AllTotalCountEthnicGroup" + i + "Claimant";
            sAllPercentageEthnicGroupClaimant[i] = "AllPercentageEthnicGroup" + i + "Claimant";
        }

        sAllTotalCountDisabilityPremiumAwardByTenancyType = new String[nTT];
        sHBTotalCountDisabilityPremiumAwardByTenancyType = new String[nTT];
        sCTBTotalCountDisabilityPremiumAwardByTenancyType = new String[nTT];
        sAllPercentageDisabilityPremiumAwardByTenancyType = new String[nTT];
        sHBPercentageDisabilityPremiumAwardByTenancyType = new String[nTT];
        sCTBPercentageDisabilityPremiumAwardByTenancyType = new String[nTT];
        sAllTotalCountSevereDisabilityPremiumAwardByTenancyType = new String[nTT];
        sHBTotalCountSevereDisabilityPremiumAwardByTenancyType = new String[nTT];
        sCTBTotalCountSevereDisabilityPremiumAwardByTenancyType = new String[nTT];
        sAllPercentageSevereDisabilityPremiumAwardByTenancyType = new String[nTT];
        sHBPercentageSevereDisabilityPremiumAwardByTenancyType = new String[nTT];
        sCTBPercentageSevereDisabilityPremiumAwardByTenancyType = new String[nTT];
        sAllTotalCountEnhancedDisabilityPremiumAwardByTenancyType = new String[nTT];
        sHBTotalCountEnhancedDisabilityPremiumAwardByTenancyType = new String[nTT];
        sCTBTotalCountEnhancedDisabilityPremiumAwardByTenancyType = new String[nTT];
        sAllPercentageEnhancedDisabilityPremiumAwardByTenancyType = new String[nTT];
        sHBPercentageEnhancedDisabilityPremiumAwardByTenancyType = new String[nTT];
        sCTBPercentageEnhancedDisabilityPremiumAwardByTenancyType = new String[nTT];
        sAllTotalCountTenancyTypeClaimant = new String[nTT];
        sAllPercentageTenancyTypeClaimant = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            sAllTotalCountTenancyTypeClaimant[i] = "AllTenancyType" + i + "ClaimantCount";
            sAllTotalCountDisabilityPremiumAwardByTenancyType[i] = "AllTotalCountDisabilityPremiumAwardByTenancyType" + i;
            sHBTotalCountDisabilityPremiumAwardByTenancyType[i] = "HBTotalCountDisabilityPremiumAwardByTenancyType" + i;
            sCTBTotalCountDisabilityPremiumAwardByTenancyType[i] = "CTBTotalCountDisabilityPremiumAwardByTenancyType" + i;
            sAllPercentageDisabilityPremiumAwardByTenancyType[i] = "AllPercentageDisabilityPremiumAwardByTenancyType" + i;
            sHBPercentageDisabilityPremiumAwardByTenancyType[i] = "HBPercentageDisabilityPremiumAwardByTenancyType" + i;
            sCTBPercentageDisabilityPremiumAwardByTenancyType[i] = "CTBPercentageDisabilityPremiumAwardByTenancyType" + i;
            sAllTotalCountSevereDisabilityPremiumAwardByTenancyType[i] = "AllTotalCountSevereDisabilityPremiumAwardByTenancyType" + i;
            sHBTotalCountSevereDisabilityPremiumAwardByTenancyType[i] = "HBTotalCountSevereDisabilityPremiumAwardByTenancyType" + i;
            sCTBTotalCountSevereDisabilityPremiumAwardByTenancyType[i] = "CTBTotalCountSevereDisabilityPremiumAwardByTenancyType" + i;
            sAllPercentageSevereDisabilityPremiumAwardByTenancyType[i] = "AllPercentageSevereDisabilityPremiumAwardByTenancyType" + i;
            sHBPercentageSevereDisabilityPremiumAwardByTenancyType[i] = "HBPercentageSevereDisabilityPremiumAwardByTenancyType" + i;
            sCTBPercentageSevereDisabilityPremiumAwardByTenancyType[i] = "CTBPercentageSevereDisabilityPremiumAwardByTenancyType" + i;
            sAllTotalCountEnhancedDisabilityPremiumAwardByTenancyType[i] = "AllTotalCountEnhancedDisabilityPremiumAwardByTenancyType" + i;
            sHBTotalCountEnhancedDisabilityPremiumAwardByTenancyType[i] = "HBTotalCountEnhancedDisabilityPremiumAwardByTenancyType" + i;
            sCTBTotalCountEnhancedDisabilityPremiumAwardByTenancyType[i] = "CTBTotalCountEnhancedDisabilityPremiumAwardByTenancyType" + i;
            sAllPercentageEnhancedDisabilityPremiumAwardByTenancyType[i] = "AllPercentageEnhancedDisabilityPremiumAwardByTenancyType" + i;
            sHBPercentageEnhancedDisabilityPremiumAwardByTenancyType[i] = "HBPercentageEnhancedDisabilityPremiumAwardByTenancyType" + i;
            sCTBPercentageEnhancedDisabilityPremiumAwardByTenancyType[i] = "CTBPercentageEnhancedDisabilityPremiumAwardByTenancyType" + i;
        }
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
        sCTBPostcodeValidFormatCount = "CTBPostcodeValidFormatCount";
        sCTBPostcodeValidCount = "CTBPostcodeValidCount";
//        CTBFemaleClaimantCountString = "CTBFemaleClaimantCount";
//        CTBDisabledClaimantCountString = "CTBDisabledClaimantCount";
//        CTBFemaleDisabledClaimantCountString = "CTBDisabledFemaleClaimantCount";
//        CTBMaleDisabledClaimantCountString = "CTBDisabledMaleClaimantCount";

        // Under Occupancy
        sTotalRentArrears = "TotalRentArrears";
        sAverageRentArrears = "AverageRentArrears";
        sGreaterThan0AverageRentArrears = "GreaterThan0AverageRentArrears";
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
        initCompare2TimesCounts();
        initCompare3TimesCounts(nTT);
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
        AllTotalWeeklyHBEntitlement = 0.0d;
        AllTotalWeeklyHBEntitlementNonZeroCount = 0;
        AllTotalWeeklyHBEntitlementZeroCount = 0;
        AllTotalWeeklyCTBEntitlement = 0.0d;
        AllTotalWeeklyCTBEntitlementNonZeroCount = 0;
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
        HBTotalWeeklyHBEntitlementNonZeroCount = 0;
        HBTotalWeeklyHBEntitlementZeroCount = 0;
        HBTotalWeeklyCTBEntitlement = 0.0d;
        HBTotalWeeklyCTBEntitlementNonZeroCount = 0;
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
        CTBTotalWeeklyHBEntitlementNonZeroCount = 0;
        CTBTotalWeeklyHBEntitlementZeroCount = 0;
        CTBTotalWeeklyCTBEntitlement = 0.0d;
        CTBTotalWeeklyCTBEntitlementNonZeroCount = 0;
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

        HBTotalCountEmployedClaimants = 0;
        CTBTotalCountCTBEmployedClaimants = 0;
        HBTotalCountSelfEmployedClaimants = 0;
        CTBTotalCountSelfEmployedClaimants = 0;
        HBTotalCountStudentsClaimants = 0;
        CTBTotalCountStudentsClaimants = 0;
        HBTotalCountLHACases = 0;
        CTBTotalCountLHACases = 0;
        for (int i = 1; i < nTT; i++) {
            AllTotalCountTenancyTypeClaimant1[i] = 0;
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
        // All
        //AllCount0 = AllCount1;
        AllCount1 = 0;

        // HB
        for (int i = 1; i < nEG; i++) {
            HBEthnicGroupCount[i] = 0;
        }
        for (int i = 1; i < nTT; i++) {
            HBTenancyTypeCount[i] = 0;
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
        for (int i = 1; i < nTT; i++) {
            CTBTenancyTypeCount[i] = 0;
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
    }

    private void initCompare2TimesCounts() {
        // Compare 2 Times
        // General
        TotalCountHBTenancyTypesToCTBTenancyTypes = 0;
        TotalCountCTBTenancyTypesToHBTenancyTypes = 0;
        // General HB related
        TotalCountHBTenancyTypesToHBTenancyTypes = 0;
        TotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes = 0;
        TotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes = 0;
        TotalCountTenancyType1ToPrivateDeregulatedTenancyTypes = 0;
        TotalCountTenancyType4ToPrivateDeregulatedTenancyTypes = 0;
        TotalCountPrivateDeregulatedTenancyTypesToTenancyType1 = 0;
        TotalCountPrivateDeregulatedTenancyTypesToTenancyType4 = 0;
        TotalCountPostcodeChangeWithinSocialTenancyTypes = 0;
        TotalCountPostcodeChangeWithinTenancyType1 = 0;
        TotalCountPostcodeChangeWithinTenancyType4 = 0;
        TotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes = 0;
        // General CTB related
        TotalCountSocialTenancyTypesToCTBTenancyTypes = 0;
        TotalCountTenancyType1ToCTBTenancyTypes = 0;
        TotalCountTenancyType4ToCTBTenancyTypes = 0;
        TotalCountPrivateDeregulatedTenancyTypesToCTBTenancyTypes = 0;
        TotalCountCTBTenancyTypesToSocialTenancyTypes = 0;
        TotalCountCTBTenancyTypesToTenancyType1 = 0;
        TotalCountCTBTenancyTypesToTenancyType4 = 0;
        TotalCountCTBTenancyTypesToPrivateDeregulatedTenancyTypes = 0;
        // All
        AllTotalCountTenancyTypeChangeClaimant = 0;
        // HB
        HBTotalCountTenancyTypeChangeClaimant = 0;
        HBTotalCountPostcode0ValidPostcode1Valid = 0;
        HBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        HBTotalCountPostcode0ValidPostcode1ValidPostcodeChange = 0;
        HBTotalCountPostcode0ValidPostcode1NotValid = 0;
        HBTotalCountPostcode0NotValidPostcode1Valid = 0;
        HBTotalCountPostcode0NotValidPostcode1NotValid = 0;
        HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged = 0;
        // CTB
        CTBTotalCountTenancyTypeChangeClaimant = 0;
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
            HashMap<DW_PersonID, DW_ID> DW_PersonIDToIDLookup) {
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
                DW_PersonID DW_PersonID;
                DW_PersonID = DW_SHBE_Handler.getClaimantDW_PersonID(D_Record);
                DW_ID ID;
                ID = DW_PersonIDToIDLookup.get(DW_PersonID);
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
                DW_PersonID DW_PersonID;
                DW_PersonID = DW_SHBE_Handler.getClaimantDW_PersonID(D_Record);
                DW_ID ID;
                ID = DW_PersonIDToIDLookup.get(DW_PersonID);
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
            HashMap<DW_PersonID, DW_ID> DW_PersonIDToIDLookup,
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
                DW_PersonID DW_PersonID;
                DW_PersonID = DW_SHBE_Handler.getClaimantDW_PersonID(D_Record);
                DW_ID ID;
                ID = DW_PersonIDToIDLookup.get(DW_PersonID);
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
                DW_PersonID DW_PersonID;
                DW_PersonID = DW_SHBE_Handler.getClaimantDW_PersonID(D_Record);
                DW_ID ID;
                ID = DW_PersonIDToIDLookup.get(DW_PersonID);
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
     * @param handleOutOfMemoryError
     * @return
     */
    public DW_SHBE_Collection getSHBEData(
            String filename,
            String paymentType,
            boolean handleOutOfMemoryError) {
        System.out.println("Loading SHBE from " + filename);
        DW_SHBE_Collection result;
        DW_SHBE_CollectionHandler handler;
        handler = new DW_SHBE_CollectionHandler(
                env,
                filename);
        result = new DW_SHBE_Collection(
                //this.tDW_SHBE_Handler,
                handler.getNextID(handleOutOfMemoryError),
                handler,
                DW_Files.getInputSHBEDir(),
                filename,
                paymentType,
                false);
//        Object[] result = tDW_SHBE_Handler.loadInputData(
//                DW_Files.getInputSHBEDir(),
//                filename,
//                paymentType,
//                false,
//                this);
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
        //All
        summary.put(
                sAllTotalCountTenancyTypeChangeClaimant,
                Integer.toString(AllTotalCountTenancyTypeChangeClaimant));
        if (AllCount0 > 0) {
            percentage = (AllTotalCountTenancyTypeChangeClaimant * 100.0d) / (double) AllCount0;
            summary.put(
                    sAllPercentageTenancyTypeChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAllPercentageTenancyTypeChangeClaimant,
                    s0);
        }
        // HB
        summary.put(
                sHBTotalCountPostcode0ValidPostcode1Valid,
                Integer.toString(HBTotalCountPostcode0ValidPostcode1Valid));
        summary.put(
                sHBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged,
                Integer.toString(HBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged));
        summary.put(
                sHBTotalCountPostcode0ValidPostcode1ValidPostcodeChange,
                Integer.toString(HBTotalCountPostcode0ValidPostcode1ValidPostcodeChange));
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
        summary.put(
                sHBTotalCountTenancyTypeChangeClaimant,
                Integer.toString(HBTotalCountTenancyTypeChangeClaimant));
        if (HBCount1 > 0) {
            percentage = (HBTotalCountPostcode0ValidPostcode1Valid * 100.0d) / (double) CTBCount1;
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged * 100.0d) / (double) HBCount1;
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCountTenancyTypeChangeClaimant * 100.0d) / (double) HBCount1;
            summary.put(
                    sHBPercentageTenancyTypeChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCountPostcode0ValidPostcode1ValidPostcodeChange * 100.0d) / (double) HBCount1;
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1ValidPostcodeChange,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCountPostcode0ValidPostcode1NotValid * 100.0d) / (double) HBCount1;
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCountPostcode0NotValidPostcode1Valid * 100.0d) / (double) HBCount1;
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCountPostcode0NotValidPostcode1NotValid * 100.0d) / (double) HBCount1;
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged * 100.0d) / (double) HBCount1;
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (HBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged * 100.0d) / (double) HBCount1;
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1Valid,
                    s0);
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged,
                    s0);
            summary.put(
                    sHBPercentageTenancyTypeChangeClaimant,
                    s0);
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1NotValid,
                    s0);
            summary.put(
                    sHBPercentagePostcode0ValidPostcode1ValidPostcodeChange,
                    s0);
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1Valid,
                    s0);
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1NotValid,
                    s0);
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    s0);
            summary.put(
                    sHBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged,
                    s0);

        }
        summary.put(
                sTotalCountSocialTenancyTypesToCTBTenancyTypes,
                Integer.toString(TotalCountSocialTenancyTypesToCTBTenancyTypes));
        int d;
        d = AllTotalCountTenancyTypeClaimant1[1] + AllTotalCountTenancyTypeClaimant1[4];
        if (d > 0) {
            percentage = (TotalCountSocialTenancyTypesToCTBTenancyTypes * 100.0d) / (double) d;
            summary.put(
                    sPercentageSocialTenancyTypesToCTBTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentageSocialTenancyTypesToCTBTenancyTypes,
                    s0);
        }
        summary.put(
                sTotalCountTenancyType1ToCTBTenancyTypes,
                Integer.toString(TotalCountTenancyType1ToCTBTenancyTypes));
        if (AllTotalCountTenancyTypeClaimant1[1] > 0) {
            percentage = (TotalCountTenancyType1ToCTBTenancyTypes * 100.0d) / (double) AllTotalCountTenancyTypeClaimant1[1];
            summary.put(
                    sPercentageTenancyType1ToCTBTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentageTenancyType1ToCTBTenancyTypes,
                    s0);
        }
        summary.put(
                sTotalCountTenancyType4ToCTBTenancyTypes,
                Integer.toString(TotalCountTenancyType4ToCTBTenancyTypes));
        if (AllTotalCountTenancyTypeClaimant1[4] > 0) {
            percentage = (TotalCountTenancyType4ToCTBTenancyTypes * 100.0d) / (double) AllTotalCountTenancyTypeClaimant1[4];
            summary.put(
                    sPercentageTenancyType4ToCTBTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentageTenancyType4ToCTBTenancyTypes,
                    s0);
        }
        summary.put(
                sTotalCountPrivateDeregulatedTenancyTypesToCTBTenancyTypes,
                Integer.toString(TotalCountPrivateDeregulatedTenancyTypesToCTBTenancyTypes));
        d = AllTotalCountTenancyTypeClaimant1[3] + AllTotalCountTenancyTypeClaimant1[6];
        if (d > 0) {
            percentage = (TotalCountPrivateDeregulatedTenancyTypesToCTBTenancyTypes * 100.0d) / (double) d;
            summary.put(
                    sPercentagePrivateDeregulatedTenancyTypesToCTBTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentagePrivateDeregulatedTenancyTypesToCTBTenancyTypes,
                    s0);
        }
        summary.put(
                sTotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes,
                Integer.toString(TotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes));
        summary.put(
                sTotalCountTenancyType1ToPrivateDeregulatedTenancyTypes,
                Integer.toString(TotalCountTenancyType1ToPrivateDeregulatedTenancyTypes));
        summary.put(
                sTotalCountTenancyType4ToPrivateDeregulatedTenancyTypes,
                Integer.toString(TotalCountTenancyType4ToPrivateDeregulatedTenancyTypes));
        summary.put(
                sTotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes,
                Integer.toString(TotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes));
        summary.put(
                sTotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes,
                Integer.toString(TotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes));
        summary.put(
                sTotalCountPrivateDeregulatedTenancyTypesToTenancyType1,
                Integer.toString(TotalCountPrivateDeregulatedTenancyTypesToTenancyType1));
        summary.put(
                sTotalCountPrivateDeregulatedTenancyTypesToTenancyType4,
                Integer.toString(TotalCountPrivateDeregulatedTenancyTypesToTenancyType4));
        summary.put(
                sTotalCountPostcodeChangeWithinSocialTenancyTypes,
                Integer.toString(TotalCountPostcodeChangeWithinSocialTenancyTypes));
        summary.put(
                sTotalCountPostcodeChangeWithinTenancyType1,
                Integer.toString(TotalCountPostcodeChangeWithinTenancyType1));
        summary.put(
                sTotalCountPostcodeChangeWithinTenancyType4,
                Integer.toString(TotalCountPostcodeChangeWithinTenancyType4));
        summary.put(
                sTotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes,
                Integer.toString(TotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes));
        d = AllTotalCountTenancyTypeClaimant0[1] + AllTotalCountTenancyTypeClaimant0[4];
        if (d > 0) {
            percentage = (TotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes * 100.0d) / d;
            summary.put(
                    sPercentageSocialTenancyTypesToPrivateDeregulatedTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCountPostcodeChangeWithinSocialTenancyTypes * 100.0d) / d;
            summary.put(
                    sPercentagePostcodeChangeWithinSocialTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentageSocialTenancyTypesToPrivateDeregulatedTenancyTypes,
                    s0);
            summary.put(
                    sPercentagePostcodeChangeWithinSocialTenancyTypes,
                    s0);
        }
        if (AllTotalCountTenancyTypeClaimant0[1] > 0) {
            percentage = (TotalCountTenancyType1ToPrivateDeregulatedTenancyTypes * 100.0d) / (double) AllTotalCountTenancyTypeClaimant0[1];
            summary.put(
                    sPercentageTenancyType1ToPrivateDeregulatedTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCountPostcodeChangeWithinTenancyType1 * 100.0d) / (double) AllTotalCountTenancyTypeClaimant0[1];
            summary.put(
                    sPercentagePostcodeChangeWithinTenancyType1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentageTenancyType1ToPrivateDeregulatedTenancyTypes,
                    s0);
            summary.put(
                    sPercentagePostcodeChangeWithinTenancyType1,
                    s0);
        }
        if (AllTotalCountTenancyTypeClaimant0[4] > 0) {
            percentage = (TotalCountTenancyType4ToPrivateDeregulatedTenancyTypes * 100.0d) / (double) AllTotalCountTenancyTypeClaimant0[4];
            summary.put(
                    sPercentageTenancyType4ToPrivateDeregulatedTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCountPostcodeChangeWithinTenancyType4 * 100.0d) / (double) AllTotalCountTenancyTypeClaimant0[4];
            summary.put(
                    sPercentagePostcodeChangeWithinTenancyType4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentageTenancyType4ToPrivateDeregulatedTenancyTypes,
                    s0);
            summary.put(
                    sPercentagePostcodeChangeWithinTenancyType4,
                    s0);
        }
        d = AllTotalCountTenancyTypeClaimant0[3] + AllTotalCountTenancyTypeClaimant0[6];
        if (d > 0) {
            percentage = (TotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes * 100.0d) / d;
            summary.put(
                    sPercentagePrivateDeregulatedTenancyTypesToSocialTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCountPrivateDeregulatedTenancyTypesToTenancyType1 * 100.0d) / d;
            summary.put(
                    sPercentagePrivateDeregulatedTenancyTypesToTenancyType1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCountPrivateDeregulatedTenancyTypesToTenancyType4 * 100.0d) / d;
            summary.put(
                    sPercentagePrivateDeregulatedTenancyTypesToTenancyType4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes * 100.0d) / d;
            summary.put(
                    sPercentagePostcodeChangeWithinPrivateDeregulatedTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentagePrivateDeregulatedTenancyTypesToSocialTenancyTypes,
                    s0);
            summary.put(
                    sPercentagePrivateDeregulatedTenancyTypesToTenancyType1,
                    s0);
            summary.put(
                    sPercentagePrivateDeregulatedTenancyTypesToTenancyType4,
                    s0);
            summary.put(
                    sPercentagePostcodeChangeWithinPrivateDeregulatedTenancyTypes,
                    s0);
        }
        // CTB
        summary.put(
                sCTBTotalCountTenancyTypeChangeClaimant,
                Integer.toString(CTBTotalCountTenancyTypeChangeClaimant));
        summary.put(
                sTotalCountCTBTenancyTypesToSocialTenancyTypes,
                Integer.toString(TotalCountCTBTenancyTypesToSocialTenancyTypes));
        summary.put(
                sTotalCountCTBTenancyTypesToTenancyType4,
                Integer.toString(TotalCountCTBTenancyTypesToTenancyType1));
        summary.put(
                sTotalCountCTBTenancyTypesToTenancyType4,
                Integer.toString(TotalCountCTBTenancyTypesToTenancyType4));
        summary.put(
                sTotalCountCTBTenancyTypesToPrivateDeregulatedTenancyTypes,
                Integer.toString(TotalCountCTBTenancyTypesToPrivateDeregulatedTenancyTypes));
        d = AllTotalCountTenancyTypeClaimant0[5] + AllTotalCountTenancyTypeClaimant0[7];
        if (d > 0) {
            percentage = (CTBTotalCountTenancyTypeChangeClaimant * 100.0d) / d;
            summary.put(
                    sCTBPercentageTenancyTypeChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCountCTBTenancyTypesToSocialTenancyTypes * 100.0d) / d;
            summary.put(
                    sPercentageCTBTenancyTypesToSocialTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCountCTBTenancyTypesToTenancyType1 * 100.0d) / d;
            summary.put(
                    sPercentageCTBTenancyTypesToTenancyType1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCountCTBTenancyTypesToTenancyType4 * 100.0d) / d;
            summary.put(
                    sPercentageCTBTenancyTypesToTenancyType4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCountCTBTenancyTypesToPrivateDeregulatedTenancyTypes * 100.0d) / d;
            summary.put(
                    sPercentageCTBTenancyTypesToPrivateDeregulatedTenancyTypes,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentageTenancyTypeChangeClaimant,
                    s0);
            summary.put(
                    sPercentageCTBTenancyTypesToSocialTenancyTypes,
                    s0);
            summary.put(
                    sPercentageCTBTenancyTypesToTenancyType1,
                    s0);
            summary.put(
                    sPercentageCTBTenancyTypesToTenancyType4,
                    s0);
            summary.put(
                    sPercentageCTBTenancyTypesToPrivateDeregulatedTenancyTypes,
                    s0);
        }
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
                sCTBTotalCountTenancyTypeChangeClaimant,
                Integer.toString(CTBTotalCountTenancyTypeChangeClaimant));
        if (CTBCount1 > 0) {
            percentage = (CTBTotalCountPostcode0ValidPostcode1Valid * 100.0d) / (double) CTBCount1;
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCountPostcode0ValidPostcode1ValidPostcodeNotChanged * 100.0d) / (double) CTBCount1;
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCountPostcode0ValidPostcode1ValidPostcodeChanged * 100.0d) / (double) CTBCount1;
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCountPostcode0ValidPostcode1NotValid * 100.0d) / (double) CTBCount1;
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCountPostcode0NotValidPostcode1Valid * 100.0d) / (double) CTBCount1;
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCountPostcode0NotValidPostcode1NotValid * 100.0d) / (double) CTBCount1;
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeNotChanged * 100.0d) / (double) CTBCount1;
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
            percentage = (CTBTotalCountPostcode0NotValidPostcode1NotValidPostcodeChanged * 100.0d) / (double) CTBCount1;
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1Valid,
                    s0);
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1ValidPostcodeNotChanged,
                    s0);
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1ValidPostcodeChanged,
                    s0);
            summary.put(
                    sCTBPercentagePostcode0ValidPostcode1NotValid,
                    s0);
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1Valid,
                    s0);
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1NotValid,
                    s0);
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    s0);
            summary.put(
                    sCTBPercentagePostcode0NotValidPostcode1NotValidPostcodeChanged,
                    s0);
        }
    }

    private void addToSummarySingleTimeRentArrears0(
            HashMap<String, String> summary) {
        summary.put(
                sTotalRentArrears,
                "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                        BigDecimal.valueOf(totalRentArrears),
                        2, RoundingMode.HALF_UP));
        if (rentArrearsCount != 0.0d) {
            summary.put(
                    sAverageRentArrears,
                    "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                            BigDecimal.valueOf(totalRentArrears / rentArrearsCount),
                            2, RoundingMode.HALF_UP));
        } else {
            summary.put(
                    sAverageRentArrears,
                    s0);
        }
        if (greaterThan0RentArrearsCount != 0.0d) {
            summary.put(
                    sGreaterThan0AverageRentArrears,
                    "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                            BigDecimal.valueOf(totalRentArrears / greaterThan0RentArrearsCount),
                            2, RoundingMode.HALF_UP));
        } else {
            summary.put(
                    sGreaterThan0AverageRentArrears,
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
        addToSummarySingleTimeTenancyType(nTT, summary);
        addToSummarySingleTimePassportedStandardIndicator(nTT, nPSI, summary);
        addToSummarySingleTime1(summary);
        AllCount0 = AllCount1;
        HBCount0 = CTBCount1;
        CTBCount0 = CTBCount1;
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

    private void addToSummarySingleTimePassportedStandardIndicator(
            int nTT,
            int nPSI,
            HashMap<String, String> summary) {
        double ave;
        // PassportStandardIndicator
        for (int i = 0; i < nPSI; i++) {
            summary.put(
                    sTotalAllPassportedStandardIndicatorCountString[i],
                    Long.toString(TotalAllPassportedStandardIndicatorCount[i]));
            if (AllCount1 > 0) {
                ave = (TotalAllPassportedStandardIndicatorCount[i] * 100.0d) / (double) AllCount1;
                summary.put(
                        sPercentageAllPassportedStandardIndicatorCountString[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave), decimalPlacePrecisionForAverage, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sPercentageAllPassportedStandardIndicatorCountString[i],
                        "0.0");
            }
            summary.put(
                    sTotalHBPassportedStandardIndicatorCountString[i],
                    Long.toString(TotalHBPassportedStandardIndicatorCount[i]));
            if (HBCount1 > 0) {
                ave = (TotalHBPassportedStandardIndicatorCount[i] * 100.0d) / (double) HBCount1;
                summary.put(
                        sPercentageHBPassportedStandardIndicatorCountString[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave), decimalPlacePrecisionForAverage, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sPercentageHBPassportedStandardIndicatorCountString[i],
                        "0.0");
            }
            summary.put(
                    sTotalCTBPassportedStandardIndicatorCountString[i],
                    Long.toString(TotalCTBPassportedStandardIndicatorCount[i]));
            if (CTBCount1 > 0) {
                ave = (TotalCTBPassportedStandardIndicatorCount[i] * 100.0d) / (double) CTBCount1;
                summary.put(
                        sPercentageCTBPassportedStandardIndicatorCountString[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave), decimalPlacePrecisionForAverage, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sPercentageCTBPassportedStandardIndicatorCountString[i],
                        s0);
            }
            for (int j = 1; j < nTT; j++) {
                summary.put(
                        sTotalAllPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                        Long.toString(TotalAllPassportedStandardIndicatorByTenancyTypeCount[i][j]));
                if (AllCount1 > 0) {
                    ave = (TotalAllPassportedStandardIndicatorByTenancyTypeCount[i][j] * 100.0d) / (double) AllCount1;
                    summary.put(
                            sPercentageAllPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave), decimalPlacePrecisionForAverage, RoundingMode.HALF_UP).toPlainString());
                } else {
                    summary.put(
                            sPercentageAllPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                            s0);
                }
                summary.put(
                        sTotalHBPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                        Long.toString(TotalHBPassportedStandardIndicatorByTenancyTypeCount[i][j]));
                if (HBCount1 > 0) {
                    ave = (TotalHBPassportedStandardIndicatorByTenancyTypeCount[i][j] * 100.0d) / (double) HBCount1;
                    summary.put(
                            sPercentageHBPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave), decimalPlacePrecisionForAverage, RoundingMode.HALF_UP).toPlainString());
                } else {
                    summary.put(
                            sPercentageHBPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                            s0);
                }
                summary.put(
                        sTotalCTBPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                        Long.toString(TotalCTBPassportedStandardIndicatorByTenancyTypeCount[i][j]));
                if (CTBCount1 > 0) {
                    ave = (TotalCTBPassportedStandardIndicatorByTenancyTypeCount[i][j] * 100.0d) / (double) CTBCount1;
                    summary.put(
                            sPercentageCTBPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave), decimalPlacePrecisionForAverage, RoundingMode.HALF_UP).toPlainString());
                } else {
                    summary.put(
                            sPercentageCTBPassportedStandardIndicatorByTenancyTypeCountString[i][j],
                            s0);
                }
            }
        }
    }

    private void addToSummarySingleTimeDisability(
            int nTT,
            HashMap<String, String> summary) {
        double percentage;
        for (int i = 0; i < nTT; i++) {
            summary.put(
                    sAllTotalCountDisabilityPremiumAwardByTenancyType[i],
                    Integer.toString(AllTotalDisabilityPremiumAwardByTenancyTypeCount[i]));
            summary.put(
                    sAllTotalCountSevereDisabilityPremiumAwardByTenancyType[i],
                    Integer.toString(AllTotalSevereDisabilityPremiumAwardByTenancyTypeCount[i]));
            summary.put(
                    sAllTotalCountEnhancedDisabilityPremiumAwardByTenancyType[i],
                    Integer.toString(AllTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[i]));
            if (AllCount1 > 0) {
                percentage = (AllTotalDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / (double) AllCount1;
                summary.put(
                        sAllPercentageDisabilityPremiumAwardByTenancyType[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
                percentage = (AllTotalSevereDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / (double) AllCount1;
                summary.put(
                        sAllPercentageSevereDisabilityPremiumAwardByTenancyType[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
                percentage = (AllTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / (double) AllCount1;
                summary.put(
                        sAllPercentageEnhancedDisabilityPremiumAwardByTenancyType[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sAllPercentageDisabilityPremiumAwardByTenancyType[i],
                        "0.0");
                summary.put(
                        sAllPercentageSevereDisabilityPremiumAwardByTenancyType[i],
                        "0.0");
                summary.put(
                        sAllPercentageEnhancedDisabilityPremiumAwardByTenancyType[i],
                        "0.0");
            }
            summary.put(
                    sHBTotalCountDisabilityPremiumAwardByTenancyType[i],
                    Integer.toString(HBTotalDisabilityPremiumAwardByTenancyTypeCount[i]));
            summary.put(
                    sHBTotalCountSevereDisabilityPremiumAwardByTenancyType[i],
                    Integer.toString(HBTotalSevereDisabilityPremiumAwardByTenancyTypeCount[i]));
            summary.put(
                    sHBTotalCountEnhancedDisabilityPremiumAwardByTenancyType[i],
                    Integer.toString(HBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[i]));
            if (HBCount1 > 0) {
                percentage = (HBTotalDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / (double) HBCount1;
                summary.put(
                        sHBPercentageDisabilityPremiumAwardByTenancyType[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
                percentage = (HBTotalSevereDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / (double) HBCount1;
                summary.put(
                        sHBPercentageSevereDisabilityPremiumAwardByTenancyType[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
                percentage = (HBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / (double) HBCount1;
                summary.put(
                        sHBPercentageEnhancedDisabilityPremiumAwardByTenancyType[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sHBPercentageDisabilityPremiumAwardByTenancyType[i],
                        "0.0");
                summary.put(
                        sHBPercentageSevereDisabilityPremiumAwardByTenancyType[i],
                        "0.0");
                summary.put(
                        sHBPercentageEnhancedDisabilityPremiumAwardByTenancyType[i],
                        "0.0");
            }
            summary.put(
                    sCTBTotalCountDisabilityPremiumAwardByTenancyType[i],
                    Integer.toString(CTBTotalDisabilityPremiumAwardByTenancyTypeCount[i]));
            summary.put(
                    sCTBTotalCountSevereDisabilityPremiumAwardByTenancyType[i],
                    Integer.toString(CTBTotalSevereDisabilityPremiumAwardByTenancyTypeCount[i]));
            summary.put(
                    sCTBTotalCountEnhancedDisabilityPremiumAwardByTenancyType[i],
                    Integer.toString(CTBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[i]));
            if (CTBCount1 > 0) {
                percentage = (CTBTotalDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / (double) CTBCount1;
                summary.put(
                        sCTBPercentageDisabilityPremiumAwardByTenancyType[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
                percentage = (CTBTotalSevereDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / (double) CTBCount1;
                summary.put(
                        sCTBPercentageSevereDisabilityPremiumAwardByTenancyType[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
                percentage = (CTBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[i] * 100.0d) / (double) CTBCount1;
                summary.put(
                        sCTBPercentageEnhancedDisabilityPremiumAwardByTenancyType[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sCTBPercentageDisabilityPremiumAwardByTenancyType[i],
                        "0.0");
                summary.put(
                        sCTBPercentageSevereDisabilityPremiumAwardByTenancyType[i],
                        "0.0");
                summary.put(
                        sCTBPercentageEnhancedDisabilityPremiumAwardByTenancyType[i],
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
                BigDecimal.valueOf(AllTotalWeeklyHBEntitlement).toPlainString());
        summary.put(
                sTotalCountWeeklyHBEntitlementNonZero,
                Integer.toString(AllTotalWeeklyHBEntitlementNonZeroCount));
        summary.put(
                sTotalCountWeeklyHBEntitlementZero,
                Integer.toString(AllTotalWeeklyHBEntitlementZeroCount));
        if (AllTotalWeeklyHBEntitlementNonZeroCount > 0) {
            ave = AllTotalWeeklyHBEntitlement / (double) AllTotalWeeklyHBEntitlementNonZeroCount;
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
        summary.put(
                sHBTotalCountWeeklyHBEntitlementNonZero,
                Integer.toString(HBTotalWeeklyHBEntitlementNonZeroCount));
        summary.put(
                sHBTotalCountWeeklyHBEntitlementZero,
                Integer.toString(HBTotalWeeklyHBEntitlementZeroCount));
        if (HBTotalWeeklyHBEntitlementNonZeroCount > 0) {
            ave = HBTotalWeeklyHBEntitlement / (double) HBTotalWeeklyHBEntitlementNonZeroCount;
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
        summary.put(
                sCTBTotalCountWeeklyHBEntitlementNonZero,
                Integer.toString(CTBTotalWeeklyHBEntitlementNonZeroCount));
        summary.put(
                sCTBTotalCountWeeklyHBEntitlementZero,
                Integer.toString(CTBTotalWeeklyHBEntitlementZeroCount));
        if (CTBTotalWeeklyHBEntitlementNonZeroCount > 0) {
            ave = CTBTotalWeeklyHBEntitlement / (double) CTBTotalWeeklyHBEntitlementNonZeroCount;
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
                Integer.toString(AllTotalWeeklyCTBEntitlementNonZeroCount));
        summary.put(
                sTotalCountWeeklyCTBEntitlementZero,
                Integer.toString(AllTotalWeeklyCTBEntitlementZeroCount));
        if (AllTotalWeeklyCTBEntitlementNonZeroCount > 0) {
            ave = AllTotalWeeklyCTBEntitlement / (double) AllTotalWeeklyCTBEntitlementNonZeroCount;
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
        summary.put(
                sHBTotalCountWeeklyCTBEntitlementNonZero,
                Integer.toString(HBTotalWeeklyCTBEntitlementNonZeroCount));
        summary.put(
                sHBTotalCountWeeklyCTBEntitlementZero,
                Integer.toString(HBTotalWeeklyCTBEntitlementZeroCount));
        if (HBTotalWeeklyCTBEntitlementNonZeroCount > 0) {
            ave = HBTotalWeeklyCTBEntitlement / (double) HBTotalWeeklyCTBEntitlementNonZeroCount;
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
        summary.put(
                sCTBTotalCountWeeklyCTBEntitlementNonZero,
                Integer.toString(CTBTotalWeeklyCTBEntitlementNonZeroCount));
        summary.put(
                sCTBTotalCountWeeklyCTBEntitlementZero,
                Integer.toString(CTBTotalWeeklyCTBEntitlementZeroCount));
        if (CTBTotalWeeklyCTBEntitlementNonZeroCount > 0) {
            ave = CTBTotalWeeklyCTBEntitlement / (double) CTBTotalWeeklyCTBEntitlementNonZeroCount;
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
                sTotalWeeklyEligibleRentAmount,
                BigDecimal.valueOf(AllTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                sTotalCountWeeklyEligibleRentAmountNonZero,
                Integer.toString(AllTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(
                sTotalCountWeeklyEligibleRentAmountZero,
                Integer.toString(AllTotalWeeklyEligibleRentAmountZeroCount));
        if (AllTotalWeeklyEligibleRentAmountNonZeroCount > 0) {
            ave = AllTotalWeeklyEligibleRentAmount / (double) AllTotalWeeklyEligibleRentAmountNonZeroCount;
            summary.put(
                    sAverageWeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAverageWeeklyEligibleRentAmount,
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
        if (HBTotalWeeklyEligibleRentAmountNonZeroCount > 0) {
            ave = HBTotalWeeklyEligibleRentAmount / (double) HBTotalWeeklyEligibleRentAmountNonZeroCount;
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
        if (CTBTotalWeeklyEligibleRentAmountNonZeroCount > 0) {
            ave = CTBTotalWeeklyEligibleRentAmount / (double) CTBTotalWeeklyEligibleRentAmountNonZeroCount;
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
        if (AllTotalCountWeeklyEligibleCouncilTaxAmountNonZero > 0) {
            ave = AllTotalWeeklyEligibleCouncilTaxAmount / (double) AllTotalCountWeeklyEligibleCouncilTaxAmountNonZero;
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
        if (HBTotalCountWeeklyEligibleCouncilTaxAmountNonZero > 0) {
            ave = HBTotalWeeklyEligibleCouncilTaxAmount / (double) HBTotalCountWeeklyEligibleCouncilTaxAmountNonZero;
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
        if (CTBTotalCountWeeklyEligibleCouncilTaxAmountNonZero > 0) {
            ave = CTBTotalWeeklyEligibleCouncilTaxAmount / (double) CTBTotalCountWeeklyEligibleCouncilTaxAmountNonZero;
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
        if (AllTotalContractualRentAmountNonZeroCount > 0) {
            ave = AllTotalContractualRentAmount / (double) AllTotalContractualRentAmountNonZeroCount;
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
        if (HBTotalContractualRentAmountNonZeroCount > 0) {
            ave = HBTotalContractualRentAmount / (double) HBTotalContractualRentAmountNonZeroCount;
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
        if (CTBTotalContractualRentAmountNonZeroCount > 0) {
            ave = CTBTotalContractualRentAmount / (double) CTBTotalContractualRentAmountNonZeroCount;
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
        if (AllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount > 0) {
            ave = AllTotalWeeklyAdditionalDiscretionaryPayment / (double) AllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
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
        if (AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount > 0) {
            ave = AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability / (double) AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
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
        if (HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount > 0) {
            ave = HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability / (double) HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
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
        if (CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount > 0) {
            ave = CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability / (double) CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
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
        t = HBTotalCountEmployedClaimants + CTBTotalCountCTBEmployedClaimants;
        summary.put(
                sAllTotalCountClaimantsEmployed,
                Integer.toString(t));
        if (AllCount1 > 0) {
            ave = (t * 100.0d) / (double) AllCount1;
            summary.put(
                    sAllPercentageClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAllPercentageClaimantsEmployed,
                    s0);
        }
        summary.put(
                sHBTotalCountClaimantsEmployed,
                Integer.toString(HBTotalCountEmployedClaimants));
        if (HBCount1 > 0) {
            ave = (HBTotalCountEmployedClaimants * 100.0d) / (double) HBCount1;
            summary.put(
                    sHBPercentageClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentageClaimantsEmployed,
                    s0);
        }
        summary.put(
                sCTBTotalCountClaimantsEmployed,
                Integer.toString(CTBTotalCountCTBEmployedClaimants));
        if (CTBCount1 > 0) {
            ave = (CTBTotalCountCTBEmployedClaimants * 100.0d) / (double) CTBCount1;
            summary.put(
                    sCTBPercentageClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentageClaimantsEmployed,
                    s0);
        }
        // Self Employed
        summary.put(
                sHBTotalCountClaimantsSelfEmployed,
                Integer.toString(HBTotalCountSelfEmployedClaimants));
        if (HBCount1 > 0) {
            ave = (HBTotalCountSelfEmployedClaimants * 100.0d) / (double) HBCount1;
            summary.put(
                    sHBPercentageClaimantsSelfEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentageClaimantsSelfEmployed,
                    s0);
        }
        summary.put(
                sCTBTotalCountClaimantsSelfEmployed,
                Integer.toString(CTBTotalCountSelfEmployedClaimants));
        if (CTBCount1 > 0) {
            ave = (CTBTotalCountSelfEmployedClaimants * 100.0d) / (double) CTBCount1;
            summary.put(
                    sCTBPercentageClaimantsSelfEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentageClaimantsSelfEmployed,
                    s0);
        }
        // Students
        summary.put(
                sHBTotalCountClaimantsStudents,
                Integer.toString(HBTotalCountStudentsClaimants));
        if (HBCount1 > 0) {
            ave = (HBTotalCountStudentsClaimants * 100.0d) / (double) HBCount1;
            summary.put(
                    sHBPercentageClaimantsStudents,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentageClaimantsStudents,
                    s0);
        }
        summary.put(
                sCTBTotalCountClaimantsStudents,
                Integer.toString(CTBTotalCountStudentsClaimants));
        if (CTBCount1 > 0) {
            ave = (CTBTotalCountStudentsClaimants * 100.0d) / (double) CTBCount1;
            summary.put(
                    sCTBPercentageClaimantsStudents,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentageClaimantsStudents,
                    s0);
        }
        // LHACases
        t = HBTotalCountLHACases + CTBTotalCountLHACases;
        summary.put(
                sAllTotalCountLHACases,
                Integer.toString(t));
        if (AllCount1 > 0) {
            ave = (t * 100.0d) / (double) HBCount1;
            summary.put(
                    sAllPercentageLHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAllPercentageLHACases,
                    s0);
        }
        summary.put(
                sHBTotalCountLHACases,
                Integer.toString(HBTotalCountLHACases));
        if (HBCount1 > 0) {
            ave = (HBTotalCountLHACases * 100.0d) / (double) HBCount1;
            summary.put(
                    sHBPercentageLHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBPercentageLHACases,
                    s0);
        }
        summary.put(
                sCTBTotalCountLHACases,
                Integer.toString(CTBTotalCountLHACases));
        if (CTBCount1 > 0) {
            ave = (CTBTotalCountLHACases * 100.0d) / (double) CTBCount1;
            summary.put(
                    sCTBPercentageLHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBPercentageLHACases,
                    s0);
        }
    }

    private void addToSummarySingleTimeDemographics(
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
                    sHBTotalCountEthnicGroupClaimant[i],
                    Integer.toString(HBEthnicGroupCount[i]));
            if (HBCount1 > 0) {
                percentage = (HBEthnicGroupCount[i] * 100.0d) / (double) HBCount1;
                summary.put(
                        sHBPercentageEthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage), decimalPlacePrecisionForPercentage, RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sHBPercentageEthnicGroupClaimant[i],
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

    private void addToSummarySingleTimeTenancyType(
            int nTT,
            HashMap<String, String> summary) {
        // Ethnicity
        double percentage;

        // TenancyType
        for (int i = 1; i < nTT; i++) {
            int all;
            all = HBTenancyTypeCount[i] + CTBTenancyTypeCount[i];
            summary.put(
                    sAllTotalCountTenancyTypeClaimant[i],
                    Integer.toString(all));
            if (AllCount1 > 0) {
                percentage = (all * 100.0d) / (double) AllCount1;
                summary.put(
                        sAllPercentageTenancyTypeClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(
                        sAllPercentageTenancyTypeClaimant[i],
                        s0);
            }
            if (i == 5 || i == 7) {
                if (HBCount1 > 0) {
                    percentage = (HBTenancyTypeCount[i] * 100.0d) / (double) HBCount1;
                    summary.put(
                            sHBPercentageTenancyTypeClaimant[i],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(percentage),
                                    decimalPlacePrecisionForPercentage,
                                    RoundingMode.HALF_UP).toPlainString());
                } else {
                    summary.put(
                            sHBPercentageTenancyTypeClaimant[i],
                            s0);
                }
            } else {
                if (CTBCount1 > 0) {
                    percentage = (CTBTenancyTypeCount[i] * 100.0d) / (double) CTBCount1;
                    summary.put(
                            sCTBPercentageTenancyTypeClaimant[i],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(percentage),
                                    decimalPlacePrecisionForPercentage,
                                    RoundingMode.HALF_UP).toPlainString());
                } else {
                    summary.put(
                            sCTBPercentageTenancyTypeClaimant[i],
                            s0);
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
            //            HashMap<DW_ID, Integer> tIDByTenancyType0,
            //            HashMap<DW_ID, Integer> tIDByTenancyType1,
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
                //                    tIDByTenancyType0,
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
//                TT0 = tIDByTenancyType0.get(tID0);
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
//            TT1 = tIDByTenancyType1.get(tID1);
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
        //}
    }

    private void doSingleTimeCount(
            DW_SHBE_D_Record D_Record,
            //            String CTBRef,
            //            HashMap<String, DW_ID> CTBRefID0,
            //            HashMap<DW_ID, Integer> tIDByTenancyType0,
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
//            TT = tIDByTenancyType0.get(tID0);
//            if (TT == null) {
//                TT = 0;
//            }
//            postcode = tIDByPostcode0.get(tID0);
//        }
        // All unfiltered counts
        TT = D_Record.getTenancyType();
        postcode = D_Record.getClaimantsPostcode();
        AllTotalCountTenancyTypeClaimant1[TT]++;

        int DisabilityPremiumAwarded;
        DisabilityPremiumAwarded = D_Record.getDisabilityPremiumAwarded();
        if (DisabilityPremiumAwarded == 1) {
            AllTotalDisabilityPremiumAwardByTenancyTypeCount[TT]++;
        }
        int SevereDisabilityPremiumAwarded;
        SevereDisabilityPremiumAwarded = D_Record.getSevereDisabilityPremiumAwarded();
        if (SevereDisabilityPremiumAwarded == 1) {
            AllTotalSevereDisabilityPremiumAwardByTenancyTypeCount[TT]++;
        }
        int EnhancedDisabilityPremiumAwarded;
        EnhancedDisabilityPremiumAwarded = D_Record.getEnhancedDisabilityPremiumAwarded();
        if (EnhancedDisabilityPremiumAwarded == 1) {
            AllTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[TT]++;
        }
        int PassportedStandardIndicator;
        PassportedStandardIndicator = D_Record.getPassportedStandardIndicator();
        TotalAllPassportedStandardIndicatorCount[PassportedStandardIndicator]++;
        TotalAllPassportedStandardIndicatorByTenancyTypeCount[PassportedStandardIndicator][TT]++;
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
            AllTotalWeeklyCTBEntitlementNonZeroCount++;
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
            if (DisabilityPremiumAwarded == 1) {
                HBTotalDisabilityPremiumAwardByTenancyTypeCount[TT]++;
            }
            if (SevereDisabilityPremiumAwarded == 1) {
                HBTotalSevereDisabilityPremiumAwardByTenancyTypeCount[TT]++;
            }
            if (EnhancedDisabilityPremiumAwarded == 1) {
                HBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[TT]++;
            }
            TotalHBPassportedStandardIndicatorCount[PassportedStandardIndicator]++;
            TotalHBPassportedStandardIndicatorByTenancyTypeCount[PassportedStandardIndicator][TT]++;
            //if (HBRef.equalsIgnoreCase(CTBRef)) {
            HBTotalHouseholdSize += HouseholdSize;
            int ClaimantsNetWeeklyIncomeFromEmployment;
            ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
            if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
                HBTotalCountEmployedClaimants++;
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
                HBTotalWeeklyHBEntitlementNonZeroCount++;
            } else {
                HBTotalWeeklyHBEntitlementZeroCount++;
            }
            WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
            if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
                HBTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
                HBTotalWeeklyCTBEntitlementNonZeroCount++;
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
                    TT,
                    postcode,
                    yM30v);
        }
        if (DW_SHBE_Handler.isCTBOnlyClaim(D_Record)) {
            if (DisabilityPremiumAwarded == 1) {
                CTBTotalDisabilityPremiumAwardByTenancyTypeCount[TT]++;
            }
            if (SevereDisabilityPremiumAwarded == 1) {
                CTBTotalSevereDisabilityPremiumAwardByTenancyTypeCount[TT]++;
            }
            if (EnhancedDisabilityPremiumAwarded == 1) {
                CTBTotalEnhancedDisabilityPremiumAwardByTenancyTypeCount[TT]++;
            }
            TotalCTBPassportedStandardIndicatorCount[PassportedStandardIndicator]++;
            TotalCTBPassportedStandardIndicatorByTenancyTypeCount[PassportedStandardIndicator][TT]++;
            CTBTotalHouseholdSize += HouseholdSize;
            int ClaimantsNetWeeklyIncomeFromEmployment;
            ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
            if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
                CTBTotalCountCTBEmployedClaimants++;
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
                    CTBTotalCountLHACases++;
                }
            }
            if (WeeklyHousingBenefitEntitlement > 0) {
                CTBTotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
                CTBTotalWeeklyHBEntitlementNonZeroCount++;
            } else {
                CTBTotalWeeklyHBEntitlementZeroCount++;
            }
            WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
            if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
                CTBTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
                CTBTotalWeeklyCTBEntitlementNonZeroCount++;
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
                    TT,
                    postcode,
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
            HBTotalCountTenancyTypeChangeClaimant++;
            AllTotalCountTenancyTypeChangeClaimant++;
            if ((tenancyType0 == 5
                    || tenancyType0 == 7)
                    && (tenancyType1 == 1
                    || tenancyType1 == 2
                    || tenancyType1 == 3
                    || tenancyType1 == 4
                    || tenancyType1 == 6
                    || tenancyType1 == 8
                    || tenancyType1 == 9)) {
                TotalCountCTBTenancyTypesToHBTenancyTypes++;
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
                TotalCountHBTenancyTypesToHBTenancyTypes++;
            }
            if (tenancyType0 == 1 || tenancyType0 == 4) {
                if (tenancyType1 == 3 || tenancyType0 == 6) {
                    TotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes++;
                    if (tenancyType0 == 1) {
                        TotalCountTenancyType1ToPrivateDeregulatedTenancyTypes++;
                    }
                    if (tenancyType0 == 4) {
                        TotalCountTenancyType4ToPrivateDeregulatedTenancyTypes++;
                    }
                }
            }
            if (tenancyType0 == 3 || tenancyType0 == 6) {
                if (tenancyType1 == 1 || tenancyType1 == 4) {
                    TotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes++;
                    if (tenancyType1 == 1) {
                        TotalCountPrivateDeregulatedTenancyTypesToTenancyType1++;
                    }
                    if (tenancyType1 == 4) {
                        TotalCountPrivateDeregulatedTenancyTypesToTenancyType4++;
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
                    TotalCountPostcodeChangeWithinSocialTenancyTypes++;
                    if (tenancyType0 == 1 && tenancyType1 == 1) {
                        TotalCountPostcodeChangeWithinTenancyType1++;
                    }
                    if (tenancyType0 == 4 && tenancyType1 == 4) {
                        TotalCountPostcodeChangeWithinTenancyType4++;
                    }
                }
            }
        }
        if ((tenancyType0 == 3 || tenancyType0 == 6) && (tenancyType1 == 3 || tenancyType1 == 6)) {
            if (isValidPostcode0 && isValidPostcode1) {
                if (!postcode0.equalsIgnoreCase(postcode1)) {
                    TotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes++;
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
        HBCount1++;
        HBEthnicGroupCount[tEG]++;
        HBTenancyTypeCount[tTT]++;
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
            CTBTotalCountTenancyTypeChangeClaimant++;
            AllTotalCountTenancyTypeChangeClaimant++;
            if ((tenancyType0 == 1
                    || tenancyType0 == 2
                    || tenancyType0 == 3
                    || tenancyType0 == 4
                    || tenancyType0 == 6
                    || tenancyType0 == 8
                    || tenancyType0 == 9)
                    && (tenancyType1 == 5
                    || tenancyType1 == 7)) {
                TotalCountHBTenancyTypesToCTBTenancyTypes++;
            }
            if ((tenancyType0 == 1 || tenancyType0 == 4)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                TotalCountSocialTenancyTypesToCTBTenancyTypes++;
            }
            if (tenancyType0 == 5 || tenancyType0 == 7) {
                if (tenancyType1 == 1 || tenancyType1 == 4) {
                    TotalCountCTBTenancyTypesToSocialTenancyTypes++;
                    if (tenancyType1 == 1) {
                        TotalCountCTBTenancyTypesToTenancyType1++;
                    }
                    if (tenancyType1 == 4) {
                        TotalCountCTBTenancyTypesToTenancyType4++;
                    }
                }
            }
            if ((tenancyType0 == 3 || tenancyType0 == 6)
                    && (tenancyType1 == 5 || tenancyType1 == 7)) {
                TotalCountPrivateDeregulatedTenancyTypesToCTBTenancyTypes++;
            }
            if ((tenancyType0 == 5 || tenancyType0 == 7)
                    && (tenancyType1 == 3 || tenancyType1 == 6)) {
                TotalCountCTBTenancyTypesToPrivateDeregulatedTenancyTypes++;
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
        CTBCount1++;
        CTBEthnicGroupCount[tEG]++;
        CTBTenancyTypeCount[tTT]++;
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
        DW_SHBE_Collection tSHBEData0;
        tSHBEData0 = getSHBEData(filename0, paymentType, handleOutOfMemoryError);
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
        TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTenancyTypes;
        tClaimantIDTenancyTypes = new TreeMap<String, HashSet<ID_TenancyType>>();
        TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTenancyTypes;
        tClaimantIDPostcodeTenancyTypes = new TreeMap<String, HashSet<ID_TenancyType_PostcodeID>>();
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
        HashMap<DW_ID, Integer> tIDByTenancyType0;
        tIDByTenancyType0 = tSHBEData0.getClaimantIDToTenancyTypeLookup();
        //tIDByTenancyType0 = loadIDByTenancyType(loadData, filename, i);
        HashMap<String, DW_ID> CTBRefToIDLookup0;
        CTBRefToIDLookup0 = tSHBEData0.getCTBRefToClaimantIDLookup();
        //tCTBRefID0 = loadCTBRefByID(loadData, filename, i);        
        HashMap<String, Integer> tLoadSummary;
        tLoadSummary = tSHBEData0.getLoadSummary();
        addToSummary(summary, tLoadSummary);
        HashSet<ID_PostcodeID> tClaimantIDPostcodes0;
        tClaimantIDPostcodes0 = tSHBEData0.getClaimantIDAndPostcodeSet();
        tClaimantIDPostcodes.put(yM30, tClaimantIDPostcodes0);
        HashSet<ID_TenancyType> tClaimantIDTenancyType0;
        tClaimantIDTenancyType0 = tSHBEData0.getClaimantIDAndTenancyTypeSet();
        tClaimantIDTenancyTypes.put(yM30, tClaimantIDTenancyType0);
        HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTenancyTypes0;
        tClaimantIDPostcodeTenancyTypes0 = tSHBEData0.getClaimantIDAndPostcodeAndTenancyTypeSet();
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
                    D_Record,
                    //                    CTBRef,
                    //                    CTBRefToIDLookup0,
                    //                    tIDByTenancyType0,
                    //                    tIDByPostcode0,
                    yM30v);
        }
        summary.put(sSHBEFilename1, filename0); // This looks odd but is right!
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
            DW_SHBE_Collection tSHBEData1;
            tSHBEData1 = getSHBEData(filename1, paymentType, handleOutOfMemoryError);
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
            HashMap<DW_ID, Integer> tIDByTenancyType1;
            tIDByTenancyType1 = tSHBEData1.getClaimantIDToTenancyTypeLookup();
            HashMap<String, DW_ID> CTBRefToIDLookup1;
            CTBRefToIDLookup1 = tSHBEData1.getCTBRefToClaimantIDLookup();
            HashSet<ID_PostcodeID> tClaimantIDPostcodes1;
            tClaimantIDPostcodes1 = tSHBEData1.getClaimantIDAndPostcodeSet();
            tClaimantIDPostcodes.put(yM31, tClaimantIDPostcodes1);
            HashSet<ID_TenancyType> tClaimantIDTenancyType1;
            tClaimantIDTenancyType1 = tSHBEData1.getClaimantIDAndTenancyTypeSet();
            tClaimantIDTenancyTypes.put(yM31, tClaimantIDTenancyType1);
            HashSet<ID_TenancyType_PostcodeID> tClaimantIDPostcodeTenancyTypes1;
            tClaimantIDPostcodeTenancyTypes1 = tSHBEData1.getClaimantIDAndPostcodeAndTenancyTypeSet();
            tClaimantIDPostcodeTenancyTypes.put(yM31, tClaimantIDPostcodeTenancyTypes1);

            key = DW_SHBE_Handler.getYearMonthNumber(filename1);
            summary = result.get(key);
            // Counters
            for (int TT = 0; TT < nTT; TT++) {
                AllTotalCountTenancyTypeClaimant0[TT] = AllTotalCountTenancyTypeClaimant1[TT];
            }
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
            addToSummarySingleTimeIncomeAndRent0(
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
                        //                        tIDByTenancyType0,
                        //                        tIDByTenancyType1,
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
                DW_SHBE_Collection.sLineCount,
                Integer.toString(tLoadSummary.get(DW_SHBE_Collection.sLineCount)));
        summary.put(
                "LineNotLoadedCount",
                Integer.toString(tLoadSummary.get("RecordIDsNotLoaded.size()")));
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
        DW_SHBE_Collection tSHBEData0 = null;
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
                tSHBEData0 = getSHBEData(filename0, paymentType, handleOutOfMemoryError);
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
        TreeMap<String, HashSet<DW_PersonID>> tClaimantIDs;
        tClaimantIDs = new TreeMap<String, HashSet<DW_PersonID>>();
        TreeMap<String, HashSet<DW_PersonID>> AllIDs;
        AllIDs = new TreeMap<String, HashSet<DW_PersonID>>();
        TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes;
        tClaimantIDPostcodes = new TreeMap<String, HashSet<ID_PostcodeID>>();
        TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTenancyTypes;
        tClaimantIDTenancyTypes = new TreeMap<String, HashSet<ID_TenancyType>>();
        TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTenancyTypes;
        tClaimantIDPostcodeTenancyTypes = new TreeMap<String, HashSet<ID_TenancyType_PostcodeID>>();

        // Specify Data
//        TreeMap<String, DW_SHBE_Record> tDRecords0;
//        tDRecords0 = (TreeMap<String, DW_SHBE_Record>) tSHBEData0[0];
        HashSet<DW_PersonID> tClaimantIDs0;
        tClaimantIDs0 = tSHBEData0.getClaimantIDs();
        tClaimantIDs.put(yM30, tClaimantIDs0);
        HashSet<DW_PersonID> AllIDs0;
        AllIDs0 = tSHBEData0.getAllIDs();
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
                tDW_PersonIDToIDLookup,
                tPostcodeToPostcodeIDLookup);

        if (!includeIte.hasNext()) {
            return result;
        }
        i = includeIte.next();
        // Second data
        String filename00 = filename0;
        String yM300 = yM30;
        String yM300v = yM30v;
        DW_SHBE_Collection tSHBEData00 = tSHBEData0;
        DW_UnderOccupiedReport_Set councilUnderOccupiedSet00;
        councilUnderOccupiedSet00 = councilUnderOccupiedSet0;
        DW_UnderOccupiedReport_Set RSLUnderOccupiedSet00;
        RSLUnderOccupiedSet00 = RSLUnderOccupiedSet0;

        filename0 = SHBEFilenames[i];
        yM30 = DW_SHBE_Handler.getYM3(filename0);
        councilUnderOccupiedSet0 = councilUnderOccupiedSets.get(yM30);
        RSLUnderOccupiedSet0 = RSLUnderOccupiedSets.get(yM30);
        tSHBEData0 = getSHBEData(filename0, paymentType, handleOutOfMemoryError);

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
                result,
                tClaimantIDPostcodes,
                tClaimantIDTenancyTypes,
                tClaimantIDPostcodeTenancyTypes,
                tDW_PersonIDToIDLookup,
                tPostcodeToPostcodeIDLookup);
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
            DW_SHBE_Collection tSHBEData1 = getSHBEData(filename1, paymentType, handleOutOfMemoryError);

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
                    tDW_PersonIDToIDLookup,
                    tPostcodeToPostcodeIDLookup);
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
            DW_SHBE_Collection tSHBEData1,
            String yM31,
            String yM31v,
            String filename1,
            DW_SHBE_Collection tSHBEData0,
            String yM30,
            String yM30v,
            String filename0,
            DW_SHBE_Collection tSHBEData00,
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
            HashMap<DW_PersonID, DW_ID> DW_PersonIDToIDLookup,
            HashMap<String, DW_ID> PostcodeToPostcodeIDLookup) {
        CouncilLinkedRecordCount00 = CouncilLinkedRecordCount0;
        RSLLinkedRecordCount00 = RSLLinkedRecordCount0;
        AllLinkedRecordCount00 = AllUOLinkedRecordCount0;
        AllUOCount00 = AllUOCount0;
        CouncilCount00 = CouncilCount0;
        RSLCount00 = RSLCount0;
        partSummaryCompare2Times(
                tSHBEData1,
                yM31, yM31v,
                filename1,
                tSHBEData0,
                yM30,
                yM30v,
                filename0, forceNewSummaries,
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
                tClaimantIDTenancyTypes,
                tClaimantIDPostcodeTenancyTypes,
                DW_PersonIDToIDLookup,
                PostcodeToPostcodeIDLookup);

        String key;
        key = DW_SHBE_Handler.getYearMonthNumber(filename1);
        HashMap<String, String> summary;
        summary = result.get(key);

        TreeMap<String, DW_SHBE_Record> tDRecords1;
        tDRecords1 = tSHBEData1.getRecords();

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
        summary.put(sSHBEFilename00, filename00);
        summary.put(sSHBEFilename0, filename0);
        summary.put(sSHBEFilename1, filename1);
        summary.put(sCouncilFilename00, councilFilenames.get(yM300));
        summary.put(sRSLFilename00, RSLFilenames.get(yM300));
        summary.put(sCouncilCount00, Integer.toString(CouncilCount00));
        summary.put(sCouncilLinkedRecordCount00, Integer.toString(CouncilLinkedRecordCount00));
        summary.put(sRSLCount00, Integer.toString(RSLCount00));
        summary.put(sRSLLinkedRecordCount00, Integer.toString(RSLLinkedRecordCount00));
        summary.put(sAllUOLinkedRecordCount00, Integer.toString(CouncilLinkedRecordCount00 + RSLLinkedRecordCount00));
        summary.put(sCouncilFilename0, councilFilenames.get(yM30));
        summary.put(sRSLFilename0, RSLFilenames.get(yM30));
        summary.put(sCouncilCount0, Integer.toString(CouncilCount0));
        summary.put(sCouncilLinkedRecordCount0, Integer.toString(CouncilLinkedRecordCount0));
        summary.put(sRSLCount0, Integer.toString(RSLCount0));
        summary.put(sRSLLinkedRecordCount0, Integer.toString(RSLLinkedRecordCount0));
        summary.put(aAllUOLinkedRecordCount0, Integer.toString(CouncilLinkedRecordCount0 + RSLLinkedRecordCount0));
        summary.put(sCouncilFilename1, councilFilenames.get(yM31));
        summary.put(sRSLFilename1, RSLFilenames.get(yM31));
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
     * @param summaries
     */
    private void partSummaryCompare2Times(
            DW_SHBE_Collection tSHBEData0,
            String yM30,
            String yM30v,
            String filename0,
            DW_SHBE_Collection tSHBEData00,
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
            TreeMap<String, HashMap<String, String>> summaries,
            TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes,
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTenancyTypes,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTenancyTypes,
            HashMap<DW_PersonID, DW_ID> DW_PersonIDToIDLookup,
            HashMap<String, DW_ID> PostcodeToPostcodeIDLookup) {
        AllUOCount0 = AllUOCount1;
        CouncilCount0 = CouncilCount1;
        RSLCount0 = RSLCount1;
        CouncilLinkedRecordCount0 = CouncilLinkedRecordCount1;
        RSLLinkedRecordCount0 = RSLLinkedRecordCount1;
        AllUOLinkedRecordCount0 = AllUOLinkedRecordCount1;
        for (int TT = 0; TT < nTT; TT++) {
            AllTotalCountTenancyTypeClaimant0[TT] = AllTotalCountTenancyTypeClaimant1[TT];
        }
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
                summaries,
                tClaimantIDPostcodes,
                tClaimantIDTenancyTypes,
                tClaimantIDPostcodeTenancyTypes,
                DW_PersonIDToIDLookup,
                PostcodeToPostcodeIDLookup);

        TreeMap<String, DW_SHBE_Record> tDRecords0;
        tDRecords0 = tSHBEData0.getRecords();
//        HashMap<DW_ID, String> tIDByPostcode0;
//        tIDByPostcode0 = (HashMap<DW_ID, String>) tSHBEData0[8];
//        HashMap<DW_ID, Integer> tIDByTenancyType0;
//        tIDByTenancyType0 = (HashMap<DW_ID, Integer>) tSHBEData0[9];
//        HashMap<String, DW_ID> CTBRefToIDLookup0;
//        CTBRefToIDLookup0 = (HashMap<String, DW_ID>) tSHBEData0[10];

        TreeMap<String, DW_SHBE_Record> tDRecords00;
        tDRecords00 = tSHBEData00.getRecords();
//        HashMap<DW_ID, String> tIDByPostcode00;
//        tIDByPostcode00 = (HashMap<DW_ID, String>) tSHBEData00[8];
//        HashMap<DW_ID, Integer> tIDByTenancyType00;
//        tIDByTenancyType00 = (HashMap<DW_ID, Integer>) tSHBEData00[9];
//        HashMap<String, DW_ID> CTBRefToIDLookup00;
//        CTBRefToIDLookup00 = (HashMap<String, DW_ID>) tSHBEData00[10];

        TreeMap<String, DW_UnderOccupiedReport_Record> councilUnderOccupiedSet0Map;
        councilUnderOccupiedSet0Map = councilUnderOccupiedSet0.getMap();
        TreeMap<String, DW_UnderOccupiedReport_Record> RSLUnderOccupiedSet0Map;
        RSLUnderOccupiedSet0Map = RSLUnderOccupiedSet0.getMap();
        // Loop over underoccupancy data
        // Loop over Council
        doCompare2TimesLoopOverSet(
                councilUnderOccupiedSet0Map,
                tDRecords00,
                tDRecords0,
                //                CTBRefToIDLookup00,
                //                CTBRefToIDLookup0,
                //                tIDByTenancyType00,
                //                tIDByTenancyType0,
                //                tIDByPostcode00,
                //                tIDByPostcode0,
                yM300v,
                yM30v);
        // Loop over RSL
        doCompare2TimesLoopOverSet(
                RSLUnderOccupiedSet0Map,
                tDRecords00,
                tDRecords0,
                //                CTBRefToIDLookup00,
                //                CTBRefToIDLookup0,
                //                tIDByTenancyType00,
                //                tIDByTenancyType0,
                //                tIDByPostcode00,
                //                tIDByPostcode0,
                yM300v,
                yM30v);

        String key;
        key = DW_SHBE_Handler.getYearMonthNumber(filename0);
        HashMap<String, String> summary;
        summary = summaries.get(key);

        addToSummaryCompare2Times(nTT, nEG, nPSI, summary);

        // All
        summary.put(sSHBEFilename0, filename00); // This looks wierd but is right!
        summary.put(sSHBEFilename1, filename0); // This looks wierd but is right!
        summary.put(sCouncilFilename0, councilFilenames.get(yM300)); // This looks wierd but is right!
        summary.put(sRSLFilename0, RSLFilenames.get(yM300)); // This looks wierd but is right!
        summary.put(sCouncilCount0, Integer.toString(CouncilCount0));
        summary.put(sCouncilLinkedRecordCount0, Integer.toString(CouncilLinkedRecordCount0));
        summary.put(sRSLCount0, Integer.toString(RSLCount0));
        summary.put(sRSLLinkedRecordCount0, Integer.toString(RSLLinkedRecordCount0));
        summary.put(aAllUOLinkedRecordCount0, Integer.toString(CouncilLinkedRecordCount0 + RSLLinkedRecordCount0));
        summary.put(sCouncilFilename1, councilFilenames.get(yM30)); // This looks wierd but is right!
        summary.put(sRSLFilename1, RSLFilenames.get(yM30)); // This looks wierd but is right!
        summary.put(sCouncilCount1, Integer.toString(CouncilCount1));
        summary.put(sCouncilLinkedRecordCount1, Integer.toString(CouncilLinkedRecordCount1));
        summary.put(sRSLCount1, Integer.toString(RSLCount1));
        summary.put(sRSLLinkedRecordCount1, Integer.toString(RSLLinkedRecordCount1));
        summary.put(sAllUOLinkedRecordCount1, Integer.toString(CouncilLinkedRecordCount1 + RSLLinkedRecordCount1));
    }

    private void addToSetsForComparisons(
            String yM3,
            TreeMap<String, DW_SHBE_Record> D_Records,
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet,
            DW_UnderOccupiedReport_Set RSLUnderOccupiedSet,
            TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes,
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTenancyTypes,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTenancyTypes,
            HashMap<DW_PersonID, DW_ID> DW_PersonIDToIDLookup,
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
                    DW_PersonIDToIDLookup, PostcodeToPostcodeIDLookup);
            tClaimantIDPostcodes.put(yM3, tClaimantIDPostcodes0);
        }
        if (tClaimantIDTenancyTypes.containsKey(yM3)) {
            tClaimantIDTenancyTypes0 = tClaimantIDTenancyTypes.get(yM3);
        } else {
            tClaimantIDTenancyTypes0 = getID_TenancyTypeSet(
                    D_Records, councilUnderOccupiedSet, RSLUnderOccupiedSet,
                    DW_PersonIDToIDLookup);
            tClaimantIDTenancyTypes.put(yM3, tClaimantIDTenancyTypes0);
        }
        if (tClaimantIDPostcodeTenancyTypes.containsKey(yM3)) {
            tClaimantIDPostcodeTenancyTypes0 = tClaimantIDPostcodeTenancyTypes.get(yM3);
        } else {
            tClaimantIDPostcodeTenancyTypes0 = getID_TenancyType_PostcodeIDSet(
                    D_Records, councilUnderOccupiedSet, RSLUnderOccupiedSet,
                    DW_PersonIDToIDLookup, PostcodeToPostcodeIDLookup);
            tClaimantIDPostcodeTenancyTypes.put(yM3, tClaimantIDPostcodeTenancyTypes0);
        }
    }

    private void partSummarySingleTime(
            //            HashMap<String, String> summary,
            DW_SHBE_Collection tSHBEData,
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
            TreeMap<String, HashMap<String, String>> summaries,
            TreeMap<String, HashSet<ID_PostcodeID>> tClaimantIDPostcodes,
            TreeMap<String, HashSet<ID_TenancyType>> tClaimantIDTenancyTypes,
            TreeMap<String, HashSet<ID_TenancyType_PostcodeID>> tClaimantIDPostcodeTenancyTypes,
            HashMap<DW_PersonID, DW_ID> tDW_PersonIDToIDLookup,
            HashMap<String, DW_ID> tPostcodeToPostcodeIDLookup) {

        TreeMap<String, DW_SHBE_Record> tDRecords0;
        tDRecords0 = tSHBEData.getRecords();
        HashMap<DW_ID, String> tIDByPostcode0;
        tIDByPostcode0 = tSHBEData.getClaimantIDToPostcodeLookup();
        //tIDByPostcode0 = loadIDByPostcode(loadData, filename, i);
        HashMap<DW_ID, Integer> tIDByTenancyType0;
        tIDByTenancyType0 = tSHBEData.getClaimantIDToTenancyTypeLookup();
        //tIDByTenancyType0 = loadIDByTenancyType(loadData, filename, i);
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

        // Add to tClaimantIDPostcodes, tClaimantIDTenancyTypes, tClaimantIDPostcodeTenancyTypes for later comparisons
        addToSetsForComparisons(
                yM3,
                tDRecords0,
                councilUnderOccupiedSet,
                RSLUnderOccupiedSet,
                tClaimantIDPostcodes,
                tClaimantIDTenancyTypes,
                tClaimantIDPostcodeTenancyTypes,
                tDW_PersonIDToIDLookup,
                tPostcodeToPostcodeIDLookup);

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
        AllUOCount1 = CouncilCount1 + RSLCount1;
        AllUOLinkedRecordCount1 = CouncilLinkedRecordCount1 + RSLLinkedRecordCount1;
        summary.put(sAllUOCount1, Integer.toString(AllUOCount1));
        summary.put(sSHBEFilename1, filename);
        summary.put(sCouncilFilename1, councilFilenames.get(yM3));
        summary.put(sRSLFilename1, RSLFilenames.get(yM3));
        summary.put(sCouncilCount1, Integer.toString(councilUnderOccupiedSet0Map.size()));
        summary.put(sCouncilLinkedRecordCount1, Integer.toString(CouncilLinkedRecordCount1));
        summary.put(sRSLCount1, Integer.toString(RSLUnderOccupiedSet0Map.size()));
        summary.put(sRSLLinkedRecordCount1, Integer.toString(RSLLinkedRecordCount1));
        summary.put(sAllUOCount1, Integer.toString(AllUOCount1));
        summary.put(sAllUOLinkedRecordCount1, Integer.toString(AllUOLinkedRecordCount1));
        addToSummarySingleTime(nTT, nEG, nPSI, summary);
        addToSummarySingleTimeRentArrears0(summary);
    }

    public void doCompare2TimesLoopOverSet(
            TreeMap<String, DW_UnderOccupiedReport_Record> map,
            TreeMap<String, DW_SHBE_Record> D_Records0,
            TreeMap<String, DW_SHBE_Record> D_Records1,
            //            HashMap<String, DW_ID> CTBRefID0,
            //            HashMap<String, DW_ID> CTBRefID1,
            //            HashMap<DW_ID, Integer> tIDByTenancyType0,
            //            HashMap<DW_ID, Integer> tIDByTenancyType1,
            //            HashMap<DW_ID, String> tIDByPostcode0,
            //            HashMap<DW_ID, String> tIDByPostcode1,
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
                        //                        tIDByTenancyType0,
                        //                        tIDByTenancyType1,
                        //                        tIDByPostcode0,
                        //                        tIDByPostcode1,
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
                        //                        tIDByTenancyType0,
                        //                        tIDByPostcode0,
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
            filename00 = summary.get(sSHBEFilename00);
            String filename0;
            filename0 = summary.get(sSHBEFilename0);
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
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
                line += summary.get(aAllUOLinkedRecordCount0) + ", ";
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
        header += sAllCount0 + ", ";
        header += sHBCount0 + ", ";
        header += sCTBCount0 + ", ";
        header += sAllCount1 + ", ";
        header += sHBCount1 + ", ";
        header += sCTBCount1 + ", ";
        header += "Month0 Year0 to Month1 Year1, ";
        // General
        // General HB related
        header += sTotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes + ", ";
        header += sTotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes + ", ";
        header += sTotalCountTenancyType1ToPrivateDeregulatedTenancyTypes + ", ";
        header += sPercentageTenancyType1ToPrivateDeregulatedTenancyTypes + ", ";
        header += sTotalCountTenancyType4ToPrivateDeregulatedTenancyTypes + ", ";
        header += sPercentageTenancyType4ToPrivateDeregulatedTenancyTypes + ", ";
        header += sTotalCountPrivateDeregulatedTenancyTypesToTenancyType1 + ", ";
        header += sPercentagePrivateDeregulatedTenancyTypesToTenancyType1 + ", ";
        header += sTotalCountPrivateDeregulatedTenancyTypesToTenancyType4 + ", ";
        header += sPercentagePrivateDeregulatedTenancyTypesToTenancyType4 + ", ";
        header += sTotalCountPostcodeChangeWithinSocialTenancyTypes + ", ";
        header += sTotalCountPostcodeChangeWithinTenancyType1 + ", ";
        header += sPercentagePostcodeChangeWithinTenancyType1 + ", ";
        header += sTotalCountPostcodeChangeWithinTenancyType4 + ", ";
        header += sPercentagePostcodeChangeWithinTenancyType4 + ", ";
        header += sTotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes + ", ";
        // General CTB related
        header += sTotalCountSocialTenancyTypesToCTBTenancyTypes + ", ";
        header += sPercentageSocialTenancyTypesToCTBTenancyTypes + ", ";
        header += sTotalCountTenancyType1ToCTBTenancyTypes + ", ";
        header += sPercentageTenancyType1ToCTBTenancyTypes + ", ";
        header += sTotalCountTenancyType4ToCTBTenancyTypes + ", ";
        header += sPercentageTenancyType4ToCTBTenancyTypes + ", ";
        header += sTotalCountPrivateDeregulatedTenancyTypesToCTBTenancyTypes + ", ";
        header += sPercentagePrivateDeregulatedTenancyTypesToCTBTenancyTypes + ", ";
        header += sTotalCountCTBTenancyTypesToSocialTenancyTypes + ", ";
        header += sPercentageCTBTenancyTypesToSocialTenancyTypes + ", ";
        header += sTotalCountCTBTenancyTypesToTenancyType1 + ", ";
        header += sPercentageCTBTenancyTypesToTenancyType1 + ", ";
        header += sTotalCountCTBTenancyTypesToTenancyType4 + ", ";
        header += sPercentageCTBTenancyTypesToTenancyType4 + ", ";
        header += sTotalCountCTBTenancyTypesToPrivateDeregulatedTenancyTypes + ", ";
        header += sPercentageCTBTenancyTypesToPrivateDeregulatedTenancyTypes + ", ";
        // All
        header += sAllTotalCountTenancyTypeChangeClaimant + ", ";
        header += sAllPercentageTenancyTypeChangeClaimant + ", ";
        // HB
        header += sHBTotalCountTenancyTypeChangeClaimant + ", ";
        header += sHBPercentageTenancyTypeChangeClaimant + ", ";
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
        // CTB
        header += sCTBTotalCountTenancyTypeChangeClaimant + ", ";
        header += sCTBPercentageTenancyTypeChangeClaimant + ", ";
        header += sCTBTotalCountPostcode0ValidPostcode1Valid + ", ";
        header += sCTBPercentagePostcode0ValidPostcode1Valid + ", ";
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
                line += summary.get(sCouncilCount0) + ", ";
                line += summary.get(sRSLFilename0) + ", ";
                line += summary.get(sRSLCount0) + ", ";
                line += summary.get(sAllUOCount0) + ", ";
                line += summary.get(aAllUOLinkedRecordCount0) + ", ";
                line += summary.get(sCouncilFilename1) + ", ";
                line += summary.get(sRSLFilename1) + ", ";
                line += summary.get(sCouncilCount1) + ", ";
                line += summary.get(sRSLCount1) + ", ";
                line += summary.get(sAllUOCount1) + ", ";
                line += summary.get(sAllUOLinkedRecordCount1) + ", ";
                line += summary.get(sCouncilFilename1) + ", ";
            }
            line += summary.get(sAllCount0) + ", ";
            line += summary.get(sHBCount0) + ", ";
            line += summary.get(sCTBCount0) + ", ";
            line += summary.get(sAllCount1) + ", ";
            line += summary.get(sHBCount1) + ", ";
            line += summary.get(sCTBCount1) + ", ";
            line += month0 + " " + year0 + " to " + month1 + " " + year1 + ", ";
            // General
            // General HB related
            line += summary.get(sTotalCountSocialTenancyTypesToPrivateDeregulatedTenancyTypes) + ", ";
            line += summary.get(sTotalCountPrivateDeregulatedTenancyTypesToSocialTenancyTypes) + ", ";
            line += summary.get(sTotalCountTenancyType1ToPrivateDeregulatedTenancyTypes) + ", ";
            line += summary.get(sPercentageTenancyType1ToPrivateDeregulatedTenancyTypes) + ", ";
            line += summary.get(sTotalCountTenancyType4ToPrivateDeregulatedTenancyTypes) + ", ";
            line += summary.get(sPercentageTenancyType4ToPrivateDeregulatedTenancyTypes) + ", ";
            line += summary.get(sTotalCountPrivateDeregulatedTenancyTypesToTenancyType1) + ", ";
            line += summary.get(sPercentagePrivateDeregulatedTenancyTypesToTenancyType1) + ", ";
            line += summary.get(sTotalCountPrivateDeregulatedTenancyTypesToTenancyType4) + ", ";
            line += summary.get(sPercentagePrivateDeregulatedTenancyTypesToTenancyType4) + ", ";
            line += summary.get(sTotalCountPostcodeChangeWithinSocialTenancyTypes) + ", ";
            line += summary.get(sTotalCountPostcodeChangeWithinTenancyType1) + ", ";
            line += summary.get(sPercentagePostcodeChangeWithinTenancyType1) + ", ";
            line += summary.get(sTotalCountPostcodeChangeWithinTenancyType4) + ", ";
            line += summary.get(sPercentagePostcodeChangeWithinTenancyType4) + ", ";
            line += summary.get(sTotalCountPostcodeChangeWithinPrivateDeregulatedTenancyTypes) + ", ";
            // General CTB related
            line += summary.get(sTotalCountSocialTenancyTypesToCTBTenancyTypes) + ", ";
            line += summary.get(sPercentageSocialTenancyTypesToCTBTenancyTypes) + ", ";
            line += summary.get(sTotalCountTenancyType1ToCTBTenancyTypes) + ", ";
            line += summary.get(sPercentageTenancyType1ToCTBTenancyTypes) + ", ";
            line += summary.get(sTotalCountTenancyType4ToCTBTenancyTypes) + ", ";
            line += summary.get(sPercentageTenancyType4ToCTBTenancyTypes) + ", ";
            line += summary.get(sTotalCountPrivateDeregulatedTenancyTypesToCTBTenancyTypes) + ", ";
            line += summary.get(sPercentagePrivateDeregulatedTenancyTypesToCTBTenancyTypes) + ", ";
            line += summary.get(sTotalCountCTBTenancyTypesToSocialTenancyTypes) + ", ";
            line += summary.get(sPercentageCTBTenancyTypesToSocialTenancyTypes) + ", ";
            line += summary.get(sTotalCountCTBTenancyTypesToTenancyType1) + ", ";
            line += summary.get(sPercentageCTBTenancyTypesToTenancyType1) + ", ";
            line += summary.get(sTotalCountCTBTenancyTypesToTenancyType4) + ", ";
            line += summary.get(sPercentageCTBTenancyTypesToTenancyType4) + ", ";
            line += summary.get(sTotalCountCTBTenancyTypesToPrivateDeregulatedTenancyTypes) + ", ";
            line += summary.get(sPercentageCTBTenancyTypesToPrivateDeregulatedTenancyTypes) + ", ";
            // All
            line += summary.get(sAllTotalCountTenancyTypeChangeClaimant) + ", ";
            line += summary.get(sAllPercentageTenancyTypeChangeClaimant) + ", ";
            // HB
            line += summary.get(sHBTotalCountTenancyTypeChangeClaimant) + ", ";
            line += summary.get(sHBPercentageTenancyTypeChangeClaimant) + ", ";
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
            line += summary.get(sCTBTotalCountTenancyTypeChangeClaimant) + ", ";
            line += summary.get(sCTBPercentageTenancyTypeChangeClaimant) + ", ";
            line += summary.get(sCTBTotalCountPostcode0ValidPostcode1Valid) + ", ";
            line += summary.get(sCTBPercentagePostcode0ValidPostcode1Valid) + ", ";
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
        File dirOut;
        dirOut = getSummaryTableDir(paymentType, includeKey, underOccupancy);
        File outFile;
        outFile = new File(
                dirOut,
                summaryTable.firstKey() + "To" + summaryTable.lastKey() + "SingleTimeGenericCounts.csv");
        PrintWriter pw;
        pw = Generic_StaticIO.getPrintWriter(outFile, false);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getSingleTimeGenericHeaderPart(underOccupancy);
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
            line += DW_SHBE_Handler.getMonth(filename1) + " " + DW_SHBE_Handler.getYear(filename1) + ", ";
            line += getSingleTimeGenericLinePart(key, summary, underOccupancy);
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
        File dirOut;
        dirOut = getSummaryTableDir(paymentType, includeKey, underOccupancy);
        File outFile;
        outFile = new File(
                dirOut,
                summaryTable.firstKey() + "To" + summaryTable.lastKey() + "SingleTimeEntitlementEligibleAmountContractualAmount.csv");
        PrintWriter pw;
        pw = Generic_StaticIO.getPrintWriter(outFile, false);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getSingleTimeGenericHeaderPart(underOccupancy);
        header += sTotalWeeklyHBEntitlement + ", ";
        header += sTotalCountWeeklyHBEntitlementNonZero + ", ";
        header += sTotalCountWeeklyHBEntitlementZero + ", ";
        header += sAverageWeeklyHBEntitlement + ", ";
        header += sTotalWeeklyEligibleRentAmount + ", ";
        header += sTotalCountWeeklyEligibleRentAmountNonZero + ", ";
        header += sTotalCountWeeklyEligibleRentAmountZero + ", ";
        header += sAverageWeeklyEligibleRentAmount + ", ";
        header += sTotalWeeklyEligibleRentAmount + ", ";
        header += sTotalCountWeeklyEligibleRentAmountNonZero + ", ";
        header += sTotalCountWeeklyEligibleRentAmountZero + ", ";
        header += sAverageWeeklyEligibleRentAmount + ", ";
        header += sAllTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sAllTotalCountWeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sAllTotalCountWeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sAllAverageWeeklyEligibleCouncilTaxAmount + ", ";
        header += sHBTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sHBTotalCountWeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sHBTotalCountWeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sHBAverageWeeklyEligibleCouncilTaxAmount + ", ";
        header += sCTBTotalContractualRentAmount + ", ";
        header += sCTBTotalCountContractualRentAmountNonZeroCount + ", ";
        header += sCTBTotalCountContractualRentAmountZeroCount + ", ";
        header += sCTBAverageContractualRentAmount + ", ";
        header += sHBTotalContractualRentAmount + ", ";
        header += sHBTotalCountContractualRentAmountNonZeroCount + ", ";
        header += sHBTotalCountContractualRentAmountZeroCount + ", ";
        header += sHBAverageContractualRentAmount + ", ";
        header += sCTBTotalContractualRentAmount + ", ";
        header += sCTBTotalCountContractualRentAmountNonZeroCount + ", ";
        header += sCTBTotalCountContractualRentAmountZeroCount + ", ";
        header += sCTBAverageContractualRentAmount + ", ";
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
            line += getSingleTimeGenericLinePart(key, summary, underOccupancy);
            line += summary.get(sTotalWeeklyHBEntitlement) + ", ";
            line += summary.get(sTotalCountWeeklyHBEntitlementNonZero) + ", ";
            line += summary.get(sTotalCountWeeklyHBEntitlementZero) + ", ";
            line += summary.get(sAverageWeeklyHBEntitlement) + ", ";
            line += summary.get(sTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sTotalCountWeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sTotalCountWeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sAverageWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sTotalCountWeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sTotalCountWeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sAverageWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sAllTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sAllTotalCountWeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sAllTotalCountWeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sAllAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sAllTotalContractualRentAmount) + ", ";
            line += summary.get(sHBTotalCountWeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sHBTotalCountWeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sHBAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sCTBTotalContractualRentAmount) + ", ";
            line += summary.get(sCTBTotalCountContractualRentAmountNonZeroCount) + ", ";
            line += summary.get(sCTBTotalCountContractualRentAmountZeroCount) + ", ";
            line += summary.get(sCTBAverageContractualRentAmount) + ", ";
            line += summary.get(sHBTotalContractualRentAmount) + ", ";
            line += summary.get(sHBTotalCountContractualRentAmountNonZeroCount) + ", ";
            line += summary.get(sHBTotalCountContractualRentAmountZeroCount) + ", ";
            line += summary.get(sHBAverageContractualRentAmount) + ", ";
            line += summary.get(sCTBTotalContractualRentAmount) + ", ";
            line += summary.get(sCTBTotalCountContractualRentAmountNonZeroCount) + ", ";
            line += summary.get(sCTBTotalCountContractualRentAmountZeroCount) + ", ";
            line += summary.get(sCTBAverageContractualRentAmount) + ", ";
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
        File dirOut;
        dirOut = getSummaryTableDir(paymentType, includeKey, underOccupancy);

        File outFile;
        outFile = new File(
                dirOut,
                summaryTable.firstKey() + "To" + summaryTable.lastKey() + "SingleTimeEmploymentEducationTraining.csv");
        PrintWriter pw;
        pw = Generic_StaticIO.getPrintWriter(outFile, false);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getSingleTimeGenericHeaderPart(underOccupancy);
        header += sAllTotalCountClaimantsEmployed + ", ";
        header += sAllPercentageClaimantsEmployed + ", ";
        header += sAllTotalCountClaimantsSelfEmployed + ", ";
        header += sAllPercentageClaimantsSelfEmployed + ", ";
        header += sAllTotalCountClaimantsStudents + ", ";
        header += sAllPercentageClaimantsStudents + ", ";
        header += sHBTotalCountClaimantsEmployed + ", ";
        header += sHBPercentageClaimantsEmployed + ", ";
        header += sHBTotalCountClaimantsSelfEmployed + ", ";
        header += sHBPercentageClaimantsSelfEmployed + ", ";
        header += sHBTotalCountClaimantsStudents + ", ";
        header += sHBPercentageClaimantsStudents + ", ";
        header += sCTBTotalCountClaimantsEmployed + ", ";
        header += sCTBPercentageClaimantsEmployed + ", ";
        header += sCTBTotalCountClaimantsSelfEmployed + ", ";
        header += sCTBPercentageClaimantsSelfEmployed + ", ";
        header += sCTBTotalCountClaimantsStudents + ", ";
        header += sCTBPercentageClaimantsStudents + ", ";
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
            line += getSingleTimeGenericLinePart(key, summary, underOccupancy);
            line += summary.get(sAllTotalCountClaimantsEmployed) + ", ";
            line += summary.get(sAllPercentageClaimantsEmployed) + ", ";
            line += summary.get(sAllTotalCountClaimantsSelfEmployed) + ", ";
            line += summary.get(sAllPercentageClaimantsSelfEmployed) + ", ";
            line += summary.get(sAllTotalCountClaimantsStudents) + ", ";
            line += summary.get(sAllPercentageClaimantsStudents) + ", ";
            line += summary.get(sHBTotalCountClaimantsEmployed) + ", ";
            line += summary.get(sHBPercentageClaimantsEmployed) + ", ";
            line += summary.get(sHBTotalCountClaimantsSelfEmployed) + ", ";
            line += summary.get(sHBPercentageClaimantsSelfEmployed) + ", ";
            line += summary.get(sHBTotalCountClaimantsStudents) + ", ";
            line += summary.get(sHBPercentageClaimantsStudents) + ", ";
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
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        File dirOut;
        dirOut = getSummaryTableDir(paymentType, includeKey, underOccupancy);

        File outFile;
        outFile = new File(
                dirOut,
                summaryTable.firstKey() + "To" + summaryTable.lastKey() + "SingleTimeRentAndIncome.csv");
        PrintWriter pw;
        pw = Generic_StaticIO.getPrintWriter(outFile, false);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getSingleTimeGenericHeaderPart(underOccupancy);
        header += sTotalIncome + ", ";
        header += sTotalIncomeGreaterThanZeroCount + ", ";
        header += sAverageIncome + ", ";
        header += sTotalWeeklyEligibleRentAmount + ", ";
        header += sTotalCountWeeklyEligibleRentAmountNonZero + ", ";
        header += sAverageWeeklyEligibleRentAmount + ", ";
        for (int i = 1; i < nTT; i++) {
            header += sTotalIncome + "TenancyType" + i + ", ";
            header += sTotalIncomeGreaterThanZeroCount + "TenancyType" + i + ", ";
            header += sAverageIncome + "TenancyType" + i + ", ";
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
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + ", ";
            line += getSingleTimeGenericLinePart(key, summary, underOccupancy);
            line += summary.get(sTotalIncome) + ", ";
            line += summary.get(sTotalIncomeGreaterThanZeroCount) + ", ";
            line += summary.get(sAverageIncome) + ", ";
            line += summary.get(sTotalWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sTotalCountWeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sAverageWeeklyEligibleRentAmount) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sTotalIncome + "TenancyType" + i) + ", ";
                line += summary.get(sTotalIncomeGreaterThanZeroCount + "TenancyType" + i) + ", ";
                line += summary.get(sAverageIncome + "TenancyType" + i) + ", ";
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
        header += sSHBEFilename1 + ", ";
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
            filename1 = summary.get(sSHBEFilename1);
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
        header += sSHBEFilename1 + ", ";
        header += getSingleTimeGenericHeaderPart(underOccupancy);
        for (int i = 1; i < nTT; i++) {
            header += sAllTotalCountTenancyTypeClaimant[i] + ", ";
            header += sAllPercentageTenancyTypeClaimant[i] + ", ";
            if (i == 5 || i == 7) {
                header += sCTBPercentageTenancyTypeClaimant[i] + ", ";
            } else {
                header += sHBPercentageTenancyTypeClaimant[i] + ", ";
            }
        }
        header += sAllTotalCountLHACases + ", ";
        header += sAllPercentageLHACases + ", ";
        header += sHBTotalCountLHACases + ", ";
        header += sHBPercentageLHACases + ", ";
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
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + ", ";
            line += getSingleTimeGenericLinePart(key,
                    summary, underOccupancy);
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sAllTotalCountTenancyTypeClaimant[i]) + ", ";
                line += summary.get(sAllPercentageTenancyTypeClaimant[i]) + ", ";
                if (i == 5 || i == 7) {
                    line += summary.get(sCTBPercentageTenancyTypeClaimant[i]) + ", ";
                } else {
                    line += summary.get(sHBPercentageTenancyTypeClaimant[i]) + ", ";
                }
            }
            line += summary.get(sAllTotalCountLHACases) + ", ";
            line += summary.get(sAllPercentageLHACases) + ", ";
            line += summary.get(sHBTotalCountLHACases) + ", ";
            line += summary.get(sHBPercentageLHACases) + ", ";
            line += summary.get(sCTBTotalCountLHACases) + ", ";
            line += summary.get(sCTBPercentageLHACases) + ", ";
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
        header += sSHBEFilename1 + ", ";
        header += getSingleTimeGenericHeaderPart(underOccupancy);
        for (int i = 1; i < nPSI; i++) {
            header += sTotalAllPassportedStandardIndicatorCountString[i] + ", ";
            header += sPercentageAllPassportedStandardIndicatorCountString[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            header += sTotalHBPassportedStandardIndicatorCountString[i] + ", ";
            header += sPercentageHBPassportedStandardIndicatorCountString[i] + ", ";
        }
        for (int i = 1; i < nPSI; i++) {
            header += sTotalCTBPassportedStandardIndicatorCountString[i] + ", ";
            header += sPercentageCTBPassportedStandardIndicatorCountString[i] + ", ";
        }
        for (int i = 1; i < nTT; i++) {
            header += sAllTotalCountDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sAllPercentageDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sAllTotalCountSevereDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sAllPercentageSevereDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sAllTotalCountEnhancedDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sAllPercentageEnhancedDisabilityPremiumAwardByTenancyType[i] + ", ";
        }
        for (int i = 1; i < nTT; i++) {
            header += sHBTotalCountDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sHBPercentageDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sHBTotalCountSevereDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sHBPercentageSevereDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sHBTotalCountEnhancedDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sHBPercentageEnhancedDisabilityPremiumAwardByTenancyType[i] + ", ";
        }
        for (int i = 1; i < nTT; i++) {
            header += sCTBTotalCountDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sCTBPercentageDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sCTBTotalCountSevereDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sCTBPercentageSevereDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sCTBTotalCountEnhancedDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sCTBPercentageEnhancedDisabilityPremiumAwardByTenancyType[i] + ", ";
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
            line += getSingleTimeGenericLinePart(key,
                    summary, underOccupancy);
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sTotalAllPassportedStandardIndicatorCountString[i]) + ", ";
                line += summary.get(sPercentageAllPassportedStandardIndicatorCountString[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sTotalHBPassportedStandardIndicatorCountString[i]) + ", ";
                line += summary.get(sPercentageHBPassportedStandardIndicatorCountString[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sTotalCTBPassportedStandardIndicatorCountString[i]) + ", ";
                line += summary.get(sPercentageCTBPassportedStandardIndicatorCountString[i]) + ", ";
            }
            for (int i = 1; i < nPSI; i++) {
                for (int j = 1; j < nTT; j++) {
                    line += summary.get(sTotalAllPassportedStandardIndicatorByTenancyTypeCountString[i][j]) + ", ";
                    line += summary.get(sPercentageAllPassportedStandardIndicatorByTenancyTypeCountString[i][j]) + ", ";
                }
            }
            for (int i = 1; i < nPSI; i++) {
                for (int j = 1; j < nTT; j++) {
                    line += summary.get(sTotalHBPassportedStandardIndicatorByTenancyTypeCountString[i][j]) + ", ";
                    line += summary.get(sPercentageHBPassportedStandardIndicatorByTenancyTypeCountString[i][j]) + ", ";
                }
            }
            for (int i = 1; i < nPSI; i++) {
                for (int j = 1; j < nTT; j++) {
                    line += summary.get(sTotalCTBPassportedStandardIndicatorByTenancyTypeCountString[i][j]) + ", ";
                    line += summary.get(sPercentageCTBPassportedStandardIndicatorByTenancyTypeCountString[i][j]) + ", ";
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
        header += sSHBEFilename1 + ", ";
        header += getSingleTimeGenericHeaderPart(underOccupancy);
        for (int i = 1; i < nTT; i++) {
            header += sAllTotalCountDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sAllPercentageDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sAllTotalCountSevereDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sAllPercentageSevereDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sAllTotalCountEnhancedDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sAllPercentageEnhancedDisabilityPremiumAwardByTenancyType[i] + ", ";
        }
        for (int i = 1; i < nTT; i++) {
            header += sHBTotalCountDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sHBPercentageDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sHBTotalCountSevereDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sHBPercentageSevereDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sHBTotalCountEnhancedDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sHBPercentageEnhancedDisabilityPremiumAwardByTenancyType[i] + ", ";
        }
        for (int i = 1; i < nTT; i++) {
            header += sCTBTotalCountDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sCTBPercentageDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sCTBTotalCountSevereDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sCTBPercentageSevereDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sCTBTotalCountEnhancedDisabilityPremiumAwardByTenancyType[i] + ", ";
            header += sCTBPercentageEnhancedDisabilityPremiumAwardByTenancyType[i] + ", ";
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
            line += getSingleTimeGenericLinePart(key,
                    summary, underOccupancy);
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sAllTotalCountDisabilityPremiumAwardByTenancyType[i]) + ", ";
                line += summary.get(sAllPercentageDisabilityPremiumAwardByTenancyType[i]) + ", ";
                line += summary.get(sAllTotalCountSevereDisabilityPremiumAwardByTenancyType[i]) + ", ";
                line += summary.get(sAllPercentageSevereDisabilityPremiumAwardByTenancyType[i]) + ", ";
                line += summary.get(sAllTotalCountEnhancedDisabilityPremiumAwardByTenancyType[i]) + ", ";
                line += summary.get(sAllPercentageEnhancedDisabilityPremiumAwardByTenancyType[i]) + ", ";
            }
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sHBTotalCountDisabilityPremiumAwardByTenancyType[i]) + ", ";
                line += summary.get(sHBPercentageDisabilityPremiumAwardByTenancyType[i]) + ", ";
                line += summary.get(sHBTotalCountSevereDisabilityPremiumAwardByTenancyType[i]) + ", ";
                line += summary.get(sHBPercentageSevereDisabilityPremiumAwardByTenancyType[i]) + ", ";
                line += summary.get(sHBTotalCountEnhancedDisabilityPremiumAwardByTenancyType[i]) + ", ";
                line += summary.get(sHBPercentageEnhancedDisabilityPremiumAwardByTenancyType[i]) + ", ";
            }
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sCTBTotalCountDisabilityPremiumAwardByTenancyType[i]) + ", ";
                line += summary.get(sCTBPercentageDisabilityPremiumAwardByTenancyType[i]) + ", ";
                line += summary.get(sCTBTotalCountSevereDisabilityPremiumAwardByTenancyType[i]) + ", ";
                line += summary.get(sCTBPercentageSevereDisabilityPremiumAwardByTenancyType[i]) + ", ";
                line += summary.get(sCTBTotalCountEnhancedDisabilityPremiumAwardByTenancyType[i]) + ", ";
                line += summary.get(sCTBPercentageEnhancedDisabilityPremiumAwardByTenancyType[i]) + ", ";
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
            result += sAllCount1 + ", ";
            result += sHBCount1 + ", ";
            result += sCTBCount1 + ", ";
        }
        result = "Month Year, ";
        return result;
    }

    private String getSingleTimeGenericLinePart(
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
        result = Generic_Time.getMonth3Letters(split[1]);
        result += " " + split[0] + ", ";
        return result;
    }
}
