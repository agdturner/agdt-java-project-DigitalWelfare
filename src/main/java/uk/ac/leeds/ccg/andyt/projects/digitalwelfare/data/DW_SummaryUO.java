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
import uk.ac.leeds.ccg.andyt.generic.data.onspd.core.ONSPD_ID;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.core.SHBE_ID;
import uk.ac.leeds.ccg.andyt.math.Generic_BigDecimal;
import uk.ac.leeds.ccg.andyt.generic.util.Generic_Time;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Records;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_D_Record;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Set;

/**
 *
 * @author geoagdt
 */
public class DW_SummaryUO extends DW_Summary {

    protected final String sAllUO = "AllUO";
    protected final String sCouncil = "Council";
    protected final String sRSL = "RSL";
    protected final String sAllUOAllCount1 = sAllUO + "AllCount1";
    // Council Counter Strings
    // HouseholdSize
    protected final String sTotal_CouncilHouseholdSize = "Total_CouncilHouseholdSize";
    protected final String sAverage_NonZero_CouncilHouseholdSize = "Average_NonZero_CouncilHouseholdSize";
    // PSI
    protected String[] sTotalCount_CouncilPSI;
    protected String[] sPercentageOfCouncilCount1_CouncilPSI;
    // DisabilityAwards
    protected final String sTotalCount_CouncilDisabilityAward = "TotalCount_CouncilDisabilityAward";
    protected final String sPercentageOfCouncilCount1_CouncilDisabilityAward = "PercentageOfCouncilCount1_CouncilDisabilityAward";
    // DisabilityPremiumAwards
    protected final String sTotalCount_CouncilDisabilityPremiumAward = "TotalCount_CouncilDisabilityPremiumAward";
    protected final String sPercentageOfCouncilCount1_CouncilDisabilityPremiumAward = "PercentageOfCouncilCount1_CouncilDisabilityPremiumAward";
    // SevereDisabilityPremiumAwards
    protected final String sTotalCount_CouncilSevereDisabilityPremiumAward = "TotalCount_CouncilSevereDisabilityPremiumAward";
    protected final String sPercentageOfCouncilCount1_CouncilSevereDisabilityPremiumAward = "PercentageOfCouncilCount1_CouncilSevereDisabilityPremiumAward";
    // DisabledChildPremiumAwards
    protected final String sTotalCount_CouncilDisabledChildPremiumAward = "TotalCount_CouncilDisabledChildPremiumAward";
    protected final String sPercentageOfCouncilCount1_CouncilDisabledChildPremiumAward = "PercentageOfCouncilCount1_CouncilDisabledChildPremiumAward";
    // EnhancedDisabilityPremiumAwards
    protected final String sTotalCount_CouncilEnhancedDisabilityPremiumAward = "TotalCount_CouncilEnhancedDisabilityPremiumAward";
    protected final String sPercentageOfCouncilCount1_CouncilEnhancedDisabilityPremiumAward = "PercentageOfCouncilCount1_CouncilEnhancedDisabilityPremiumAward";
    // WeeklyHBEntitlement
    public final String sTotal_CouncilWeeklyHBEntitlement = "Total_CouncilWeeklyHBEntitlement";
    public final String sTotalCount_CouncilWeeklyHBEntitlementNonZero = "TotalCount_CouncilWeeklyHBEntitlementNonZero";
    public final String sTotalCount_CouncilWeeklyHBEntitlementZero = "TotalCount_CouncilWeeklyHBEntitlementZero";
    public final String sAverage_NonZero_CouncilWeeklyHBEntitlement = "Average_NonZero_CouncilWeeklyHBEntitlement";
    // WeeklyCTBEntitlement
    public final String sTotal_CouncilWeeklyCTBEntitlement = "Total_CouncilWeeklyCTBEntitlement";
    public final String sTotalCount_CouncilWeeklyCTBEntitlementNonZero = "TotalCount_CouncilWeeklyCTBEntitlementNonZero";
    public final String sTotalCount_CouncilWeeklyCTBEntitlementZero = "TotalCount_CouncilWeeklyCTBEntitlementZero";
    public final String sAverage_Non_Zero_CouncilWeeklyCTBEntitlement = "Average_Non_Zero_CouncilWeeklyCTBEntitlement";
    // WeeklyEligibleRentAmount
    public final String sTotal_CouncilWeeklyEligibleRentAmount = "Total_CouncilWeeklyEligibleRentAmount";
    public final String sTotalCount_CouncilWeeklyEligibleRentAmountNonZero = "TotalCount_CouncilWeeklyEligibleRentAmountNonZero";
    public final String sTotalCount_CouncilWeeklyEligibleRentAmountZero = "TotalCount_CouncilWeeklyEligibleRentAmountZero";
    public final String sAverage_NonZero_CouncilAverageWeeklyEligibleRentAmount = "Average_NonZero_CouncilAverageWeeklyEligibleRentAmount";
    // WeeklyEligibleCouncilTaxAmount
    protected final String sTotal_CouncilWeeklyEligibleCouncilTaxAmount = "Total_CouncilWeeklyEligibleCouncilTaxAmount";
    protected final String sTotalCount_CouncilWeeklyEligibleCouncilTaxAmountNonZero = "TotalCount_CouncilWeeklyEligibleCouncilTaxAmountNonZero";
    protected final String sTotalCount_CouncilWeeklyEligibleCouncilTaxAmountZero = "TotalCount_CouncilWeeklyEligibleCouncilTaxAmountZero";
    protected final String sAverage_NonZero_CouncilWeeklyEligibleCouncilTaxAmount = "Average_NonZero_CouncilWeeklyEligibleCouncilTaxAmount";
    // ContractualRentAmount
    protected final String sTotal_CouncilContractualRentAmount = "Total_CouncilContractualRentAmount";
    protected final String sTotalCount_CouncilContractualRentAmountNonZero = "TotalCount_CouncilContractualRentAmountNonZero";
    protected final String sTotalCount_CouncilContractualRentAmountZero = "TotalCount_CouncilContractualRentAmountZero";
    protected final String sAverage_NonZero_CouncilContractualRentAmount = "Average_NonZero_CouncilContractualRentAmount";
    // WeeklyAdditionalDiscretionaryPayment
    protected final String sTotal_CouncilWeeklyAdditionalDiscretionaryPayment = "Total_CouncilWeeklyAdditionalDiscretionaryPayment";
    protected final String sTotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentNonZero = "TotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentNonZero";
    protected final String sTotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentZero = "TotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentZero";
    protected final String sAverage_NonZero_CouncilWeeklyAdditionalDiscretionaryPayment = "Average_NonZero_CouncilWeeklyAdditionalDiscretionaryPayment";
    // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
    protected final String sTotal_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "Total_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected final String sTotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "TotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    protected final String sTotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "TotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    protected final String sAverage_NonZero_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "Average_NonZero_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    // Employment
    protected final String sTotalCount_CouncilClaimantsEmployed = "TotalCount_CouncilClaimantsEmployed";
    protected final String sPercentageOfCouncilCount1_CouncilClaimantsEmployed = "PercentageOfCouncilCount1_CouncilClaimantsEmployed";
    protected final String sTotalCount_CouncilClaimantsSelfEmployed = "TotalCount_CouncilClaimantsSelfEmployed";
    protected final String sPercentageOfCouncilCount1_CouncilClaimantsSelfEmployed = "PercentageOfCouncilCount1_CouncilClaimantsSelfEmployed";
    protected final String sTotalCount_CouncilClaimantsStudents = "TotalCount_CouncilClaimantsStudents";
    protected final String sPercentageOfCouncilCount1_CouncilClaimantsStudents = "PercentageOfCouncilCount1_CouncilClaimantsStudents";
    protected final String sTotalCount_CouncilLHACases = "TotalCount_CouncilLHACases";
    protected final String sPercentageOfCouncilCount1_CouncilLHACases = "PercentageOfCouncilCount1_CouncilLHACases";
    // Counts
    protected final String sCouncilCount0 = "CouncilCount0";
    protected final String sCouncilCount1 = "CouncilCount1";
    protected final String sCouncilHBPTICount1 = "CouncilHBPTICount1";
    protected final String sCouncilHBPTSCount1 = "CouncilHBPTSCount1";
    protected final String sCouncilHBPTOCount1 = "CouncilHBPTOCount1";
    protected final String sCouncilCTBPTICount1 = "CouncilCTBPTICount1";
    protected final String sCouncilCTBPTSCount1 = "CouncilCTBPTSCount1";
    protected final String sCouncilCTBPTOCount1 = "CouncilCTBPTOCount1";

    // Income
    public final String sTotal_CouncilHBPTIIncome = "Total_CouncilHBPTIIncome";
    public final String sTotalCount_CouncilHBPTIIncomeNonZero = "TotalCount_CouncilHBPTIIncomeNonZero";
    public final String sTotalCount_CouncilHBPTIIncomeZero = "TotalCount_CouncilHBPTIIncomeZero";
    public final String sAverage_NonZero_CouncilHBPTIIncome = "Average_NonZero_CouncilHBPTIIncome";
    public final String sTotal_CouncilHBPTSIncome = "Total_CouncilHBPTSIncome";
    public final String sTotalCount_CouncilHBPTSIncomeNonZero = "TotalCount_CouncilHBPTSIncomeNonZero";
    public final String sTotalCount_CouncilHBPTSIncomeZero = "TotalCount_CouncilHBPTSIncomeZero";
    public final String sAverage_NonZero_CouncilHBPTSIncome = "Average_NonZero_CouncilHBPTSIncome";
    public final String sTotal_CouncilHBPTOIncome = "Total_CouncilHBPTOIncome";
    public final String sTotalCount_CouncilHBPTOIncomeNonZero = "TotalCount_CouncilHBPTOIncomeNonZero";
    public final String sTotalCount_CouncilHBPTOIncomeZero = "TotalCount_CouncilHBPTOIncomeZero";
    public final String sAverage_NonZero_CouncilHBPTOIncome = "Average_NonZero_CouncilHBPTOIncome";

    // RSL Counter Strings
    // HouseholdSize
    protected final String sTotal_RSLHouseholdSize = "Total_RSLHouseholdSize";
    protected final String sAverage_NonZero_RSLHouseholdSize = "Average_NonZero_RSLHouseholdSize";
    // PSI
    protected String[] sTotalCount_RSLPSI;
    protected String[] sPercentageOfRSLCount1_RSLPSI;
    // DisabilityAwards
    protected final String sTotalCount_RSLDisabilityAward = "TotalCount_RSLDisabilityAward";
    protected final String sPercentageOfRSLCount1_RSLDisabilityAward = "PercentageOfRSLCount1_RSLDisabilityAward";
    // DisabilityPremiumAward
    protected final String sTotalCount_RSLDisabilityPremiumAward = "TotalCount_RSLDisabilityPremiumAward";
    protected final String sPercentageOfRSLCount1_RSLDisabilityPremiumAward = "PercentageOfRSLCount1_RSLDisabilityPremiumAward";
    // SevereDisabilityPremiumAward
    protected final String sTotalCount_RSLSevereDisabilityPremiumAward = "TotalCount_RSLSevereDisabilityPremiumAward";
    protected final String sPercentageOfRSLCount1_RSLSevereDisabilityPremiumAward = "PercentageOfRSLCount1_RSLSevereDisabilityPremiumAward";
    // DisabledChildPremiumAward
    protected final String sTotalCount_RSLDisabledChildPremiumAward = "TotalCount_RSLDisabledChildPremiumAward";
    protected final String sPercentageOfRSLCount1_RSLDisabledChildPremiumAward = "PercentageOfRSLCount1_RSLDisabledChildPremiumAward";
    // EnhancedDisabilityPremiumAward
    protected final String sTotalCount_RSLEnhancedDisabilityPremiumAward = "TotalCount_RSLEnhancedDisabilityPremiumAward";
    protected final String sPercentageOfRSLCount1_RSLEnhancedDisabilityPremiumAward = "PercentageOfRSLCount1_RSLEnhancedDisabilityPremiumAward";
    // WeeklyHBEntitlement
    public final String sTotal_RSLWeeklyHBEntitlement = "Total_RSLWeeklyHBEntitlement";
    public final String sTotalCount_RSLWeeklyHBEntitlementNonZero = "TotalCount_RSLWeeklyHBEntitlementNonZero";
    public final String sTotalCount_RSLWeeklyHBEntitlementZero = "TotalCount_RSLWeeklyHBEntitlementZero";
    public final String sAverage_NonZero_RSLWeeklyHBEntitlement = "Average_NonZero_RSLWeeklyHBEntitlement";
    // WeeklyCTBEntitlement
    public final String sTotal_RSLWeeklyCTBEntitlement = "Total_RSLWeeklyCTBEntitlement";
    public final String sTotalCount_RSLWeeklyCTBEntitlementNonZero = "TotalCount_RSLWeeklyCTBEntitlementNonZero";
    public final String sTotalCount_RSLWeeklyCTBEntitlementZero = "TotalCount_RSLWeeklyCTBEntitlementZero";
    public final String sAverage_NonZero_RSLWeeklyCTBEntitlement = "Average_NonZero_RSLWeeklyCTBEntitlement";
    // WeeklyEligibleRentAmount
    public final String sTotal_RSLWeeklyEligibleRentAmount = "Total_RSLWeeklyEligibleRentAmount";
    public final String sTotalCount_RSLWeeklyEligibleRentAmountNonZero = "TotalCount_RSLWeeklyEligibleRentAmountNonZero";
    public final String sTotalCount_RSLWeeklyEligibleRentAmountZero = "TotalCount_RSLWeeklyEligibleRentAmountZero";
    public final String sAverage_NonZero_RSLWeeklyEligibleRentAmount = "Average_NonZero_RSLWeeklyEligibleRentAmount";
    // WeeklyEligibleCouncilTaxAmount
    protected final String sTotal_RSLWeeklyEligibleCouncilTaxAmount = "Total_RSLWeeklyEligibleCouncilTaxAmount";
    protected final String sTotalCount_RSLWeeklyEligibleCouncilTaxAmountNonZero = "TotalCount_RSLWeeklyEligibleCouncilTaxAmountNonZero";
    protected final String sTotalCount_RSLWeeklyEligibleCouncilTaxAmountZero = "TotalCount_RSLWeeklyEligibleCouncilTaxAmountZero";
    protected final String sAverage_NonZero_RSLWeeklyEligibleCouncilTaxAmount = "Average_NonZero_RSLWeeklyEligibleCouncilTaxAmount";
    // ContractualRentAmount
    protected final String sTotal_RSLContractualRentAmount = "Total_RSLContractualRentAmount";
    protected final String sTotalCount_RSLContractualRentAmountNonZero = "TotalCount_RSLContractualRentAmountNonZero";
    protected final String sTotalCount_ContractualRentAmountZero = "TotalCount_RSLContractualRentAmountZero";
    protected final String sAverage_NonZero_RSLContractualRentAmount = "Average_NonZero_RSLContractualRentAmount";
    // WeeklyAdditionalDiscretionaryPayment
    protected final String sTotal_RSLWeeklyAdditionalDiscretionaryPayment = "Total_RSLWeeklyAdditionalDiscretionaryPayment";
    protected final String sTotalCount_RSLWeeklyAdditionalDiscretionaryPaymentNonZero = "TotalCount_RSLWeeklyAdditionalDiscretionaryPaymentNonZero";
    protected final String sTotalCount_RSLWeeklyAdditionalDiscretionaryPaymentZero = "TotalCount_RSLWeeklyAdditionalDiscretionaryPaymentZero";
    protected final String sAverage_NonZero_RSLWeeklyAdditionalDiscretionaryPayment = "Average_NonZero_RSLWeeklyAdditionalDiscretionaryPayment";
    // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
    protected final String sTotal_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "Total_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected final String sTotalCount_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "TotalCount_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    protected final String sTotalCount_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "TotalCount_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    protected final String sAverage_NonZero_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "Average_NonZero_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    // Employment
    protected final String sTotalCount_RSLClaimantsEmployed = "TotalCount_RSLClaimantsEmployed";
    protected final String sPercentageOfRSLCount1_RSLClaimantsEmployed = "PercentageOfRSLCount1_RSLClaimantsEmployed";
    protected final String sTotalCount_RSLClaimantsSelfEmployed = "TotalCount_RSLClaimantsSelfEmployed";
    protected final String sPercentageOfRSLCount1_RSLClaimantsSelfEmployed = "PercentageOfRSLCount1_RSLClaimantsSelfEmployed";
    protected final String sTotalCount_RSLClaimantsStudents = "TotalCount_RSLClaimantsStudents";
    protected final String sPercentageOfRSLCount1_RSLClaimantsStudents = "PercentageOfRSLCount1_RSLClaimantsStudents";
    // LHA
    protected final String sTotalCount_RSLLHACases = "TotalCount_RSLLHACases";
    protected final String sPercentageOfRSLCount1_RSLLHACases = "PercentageOfRSLCount1_RSLLHACases";
    // Counts
    protected final String sRSLCount0 = "RSLCount0";
    protected final String sRSLCount1 = "RSLCount1";
    protected final String sRSLHBPTICount1 = "RSLHBPTICount1";
    protected final String sRSLHBPTSCount1 = "RSLHBPTSCount1";
    protected final String sRSLHBPTOCount1 = "RSLHBPTOCount1";
    protected final String sRSLCTBPTICount1 = "RSLCTBPTICount1";
    protected final String sRSLCTBPTSCount1 = "RSLCTBPTSCount1";
    protected final String sRSLCTBPTOCount1 = "RSLCTBPTOCount1";
    public final String sTotal_RSLHBPTIIncome = "Total_RSLHBPTIIncome";
    public final String sTotal_RSLHBPTSIncome = "Total_RSLHBPTSIncome";
    public final String sTotal_RSLHBPTOIncome = "Total_RSLHBPTOIncome";
    public final String sTotalCount_RSLHBPTIIncomeNonZero = "TotalCount_RSLHBPTIIncomeNonZero";
    public final String sAverage_NonZero_RSLIncome = "Average_NonZero_RSLIncome";

    // Files
    protected final String sCouncilFilename0 = "CouncilFilename0";
    protected final String sCouncilFilename1 = "CouncilFilename1";
    protected final String sRSLFilename0 = "RSLFilename0";
    protected final String sRSLFilename1 = "RSLFilename1";

    // Strings0
    // All
//    protected final String sAllUOCount0 = "AllUOCount0";
//    protected final String sAllUOCount1 = "AllUOCount1";
    protected final String sAllUOLinkedRecordCount0 = "AllUOLinkedRecordCount0";
    protected final String sAllUOLinkedRecordCount1 = "AllUOLinkedRecordCount1";
    // Council
    protected final String sCouncilLinkedRecordCount0 = "CouncilLinkedRecordCount0";
    protected final String sCouncilLinkedRecordCount1 = "CouncilLinkedRecordCount1";
    protected final String sCouncilTotal_RentArrears = "CouncilTotal_RentArrears";
    protected final String sTotalCount_CouncilRentArrears = "TotalCount_CouncilRentArrears";
    protected final String sTotalCount_CouncilRentArrearsZero = "TotalCount_CouncilRentArrearsZero";
    protected final String sTotalCount_CouncilRentArrearsNonZero = "TotalCount_CouncilRentArrearsNonZero";
    protected final String sAverage_CouncilRentArrears = "Average_CouncilRentArrears";
    protected final String sAverage_NonZero_CouncilRentArrears = "Average_NonZero_CouncilRentArrears";
    // RSL
    protected final String sRSLLinkedRecordCount0 = "RSLLinkedRecordCount0";
    protected final String sRSLLinkedRecordCount1 = "RSLLinkedRecordCount1";

    // Demographics
    // Ethnicity
    // Council
    protected String[] sTotalCount_CouncilEthnicGroupClaimant;
    protected String[] sPercentageOfCouncilCount1_CouncilEthnicGroupClaimant;
    // RSL
    protected String[] sTotalCount_RSLEthnicGroupClaimant;
    protected String[] sPercentageOfRSLCount1_RSLEthnicGroupClaimant;

    // Key Counts
    // Council
    protected String[] sTotalCount_CouncilClaimantTT;
    // RSL
    protected String[] sTotalCount_RSLClaimantTT;

    // Postcode
    // Council
    protected final String sTotalCount_CouncilPostcodeValidFormat = "TotalCount_CouncilPostcodeValidFormat";
    protected final String sPercentageOfCouncilCount1_CouncilPostcodeValidFormat = "PercentageOfCouncilCount1_CouncilPostcodeValidFormat";
    protected final String sTotalCount_CouncilPostcodeValid = "TotalCount_CouncilPostcodeValid";
    protected final String sPercentageOfCouncilCount1_CouncilPostcodeValid = "PercentageOfCouncilCount1_CouncilPostcodeValid";
    // RSL
    protected final String sTotalCount_RSLPostcodeValidFormat = "TotalCount_RSLPostcodeValidFormat";
    protected final String sPercentageOfRSLCount1_RSLPostcodeValidFormat = "PercentageOfRSLCount1_RSLPostcodeValidFormat";
    protected final String sTotalCount_RSLPostcodeValid = "TotalCount_RSLPostcodeValid";
    protected final String sPercentageOfRSLCount1_RSLPostcodeValid = "PercentageOfRSLCount1_RSLPostcodeValid";

    /**
     * Compare 2 Times
     */
    /*
     * Council
     */
    // Arrears
    protected final String sTotalCount_CouncilInBoth = "TotalCount_CouncilInBoth";
    protected final String sSumArrearsDiffInBoth = "SumArrearsDiffInBoth";
    protected final String sTotalCount_CouncilInBothG0 = "TotalCount_CouncilInBothG0";
    protected final String sSumArrearsDiffInBothG0 = "SumArrearsDiffInBothG0";
    protected final String sTotalCount_CouncilInBothG1 = "TotalCount_CouncilInBothG1";
    protected final String sSumArrearsDiffInBothG1 = "SumArrearsDiffInBothG1";
    // TT
    protected final String sTotalCount_CouncilTTChangeClaimant = "TotalCount_CouncilTTChangeClaimant";
    protected final String sPercentageOfCouncilCount0_CouncilTTChangeClaimant = "PercentageOfCouncilHB_CouncilTTChangeClaimant";
    protected final String sTotalCount_CouncilMinus999TTToTT1 = "TotalCount_CouncilMinus999TTToTT1";
    protected final String sTotalCount_CouncilTT1ToMinus999TT = "TotalCount_CouncilTT1ToMinus999TT";
    protected final String sTotalCount_CouncilTT1ToPrivateDeregulatedTTs = "TotalCount_CouncilTT1ToPrivateDeregulatedTTs";
    protected final String sPercentageOfCouncilCount0_CouncilTT1ToPrivateDeregulatedTTs = "PercentageOfCouncilCount0_CouncilTT1ToPrivateDeregulatedTTs";
    protected final String sTotalCount_CouncilPrivateDeregulatedTTsToTT1 = "TotalCount_CouncilPrivateDeregulatedTTsToTT1";
    protected final String sTotalCount_CouncilTT1ToTT4 = "TotalCount_CouncilTT1ToTT4";
    protected final String sPercentageOfCouncilTT1_CouncilTT1ToTT4 = "PercentageOfCouncilTT1_CouncilTT1ToTT4";
    protected final String sTotalCount_CouncilTT4ToTT1 = "TotalCount_CouncilTT4ToTT1";
    protected final String sPercentageOfCouncilTT4_CouncilTT4ToTT1 = "PercentageOfCouncilTT4_CouncilTT4ToTT1";
    protected final String sTotalCount_CouncilPostcodeChangeWithinTT1 = "TotalCount_CouncilPostcodeChangeWithinTT1";
    protected final String sPercentageOfCouncilTT1_CouncilPostcodeChangeWithinTT1 = "PercentageOfCouncilTT1_CouncilPostcodeChangeWithinTT1";
    // Postcode
    protected final String sTotalCount_CouncilPostcodeF0MappablePostcodeF1Mappable = "TotalCount_CouncilPostcodeF0MappablePostcodeF1Mappable";
    protected final String sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1Mappable = "PercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1Mappable";
    protected final String sTotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged = "TotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged";
    protected final String sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged = "PercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged";
    protected final String sTotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeChange = "TotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeChange";
    protected final String sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeChange = "PercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeChange";
    protected final String sTotalCount_CouncilPostcodeF0MappablePostcodeF1NotMappable = "TotalCount_CouncilPostcodeF0MappablePostcodeF1NotMappable";
    protected final String sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1NotMappable = "PercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1NotMappable";
    protected final String sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1Mappable = "TotalCount_CouncilPostcodeF0NotMappablePostcodeF1Mappable";
    protected final String sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1Mappable = "PercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1Mappable";
    protected final String sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappable = "TotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappable";
    protected final String sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1NotMappable = "PercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1NotMappable";
    protected final String sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged = "TotalCount_CouncilPostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected final String sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcode1NotMappablePostcodeNotChanged = "sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcode1NotMappablePostcodeNotChanged";
    protected final String sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged = "sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged";
    protected final String sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged = "sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged";
    protected final String sTotalCount_CouncilPostcodeF0MappablePostcodeF1DNE = "sTotalCount_CouncilPostcodeF0MappablePostcodeF1DNE";
    protected final String sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1DNE = "sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1DNE";
    protected final String sTotalCount_CouncilPostcodeF0DNEPostcodeF1Mappable = "sTotalCount_CouncilPostcodeF0DNEPostcodeF1Mappable";
    protected final String sPercentageOfCouncilCount0_CouncilPostcodeF0DNEPostcodeF1Mappable = "sPercentageOfCouncilCount0_CouncilPostcodeF0DNEPostcodeF1Mappable";
    protected final String sTotalCount_CouncilPostcodeF0DNEPostcodeF1DNE = "sTotalCount_CouncilPostcodeF0DNEPostcodeF1DNE";
    protected final String sPercentageOfCouncilCount0_CouncilPostcodeF0DNEPostcodeF1DNE = "sPercentageOfCouncilCount0_CouncilPostcodeF0DNEPostcodeF1DNE";
    protected final String sTotalCount_CouncilPostcodeF0DNEPostcodeF1NotMappable = "sTotalCount_CouncilPostcodeF0DNEPostcodeF1NotMappable";
    protected final String sPercentageOfCouncilCount0_CouncilPostcodeF0DNEPostcodeF1NotMappable = "sPercentageOfCouncilCount0_CouncilPostcodeF0DNEPostcodeF1NotMappable";
    protected final String sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1DNE = "sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1DNE";
    protected final String sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1DNE = "sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1DNE";

    /*
     * RSL
     */
    // TT
    protected final String sTotalCount_RSLTTChangeClaimant = "TotalCount_RSLTTChangeClaimant";
    protected final String sPercentageOfRSLHB_RSLTTChangeClaimant = "PercentageOfRSLHB_RSLTTChangeClaimant";
    protected final String sTotalCount_RSLMinus999TTToTT4 = "TotalCount_RSLMinus999TTToTT4";
    protected final String sTotalCount_RSLTT4ToMinus999TT = "TotalCount_RSLTT4ToMinus999TT";
    protected final String sTotalCount_RSLTT4ToHBTTs = "TotalCount_RSLTT4ToHBTTs";
    protected final String sPercentageOfRSLHB_RSLHBTTsToTT4 = "RSLPercentageOfHB_HBTTsToTT4";
    protected final String sTotalCount_RSLTT4ToPrivateDeregulatedTTs = "TotalCount_RSLTT4ToPrivateDeregulatedTTs";
    protected final String sPercentageOfRSLTT4_RSLTT4ToPrivateDeregulatedTTs = "RSLPercentageOfTT4_TT4ToPrivateDeregulatedTTs";
    protected final String sTotalCount_RSLPrivateDeregulatedTTsToTT4 = "TotalCount_RSLPrivateDeregulatedTTsToTT4";
    protected final String sTotalCount_RSLTT4ToTT1 = "TotalCount_RSLRSLTT4ToTT1";
    protected final String sPercentageOfRSLTT4_RSLTT4ToTT1 = "RSLPercentageOfRSLTT4_RSLTT4ToTT1";
    protected final String sTotalCount_RSLTT1ToTT4 = "TotalCount_RSLTT1ToTT4";
    protected final String sRSLPercentageOfRSLTT1_TT1ToTT4 = "PercentageOfRSLTT1_RSLTT1ToTT4";
    protected final String sTotalCount_RSLPostcodeChangeWithinTT4 = "TotalCount_RSLPostcodeChangeWithinTT4";
    protected final String sPercentageOfRSLTT4_RSLPostcodeChangeWithinTT4 = "PercentageOfRSLTT4_RSLPostcodeChangeWithinTT4";
    // Postcode
    protected final String sTotalCount_RSLPostcodeF0MappablePostcodeF1Mappable = "TotalCount_RSLPostcodeF0MappablePostcodeF1Mappable";
    protected final String sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1Mappable = "PercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1Mappable";
    protected final String sTotalCount_RSLPostcodeF0MappablePostcode1MappablePostcodeNotChanged = "TotalCount_RSLPostcodeF0MappablePostcode1MappablePostcodeNotChanged";
    protected final String sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged = "PercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged";
    protected final String sTotalCount_RSLPostcodeF0MappablePostcodeF1MappablePostcodeChanged = "TotalCount_RSLPostcodeF0MappablePostcodeF1MappablePostcodeChanged";
    protected final String sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1MappablePostcodeChanged = "PercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1MappablePostcodeChanged";
    protected final String sTotalCount_RSLPostcodeF0MappablePostcodeF1NotMappable = "TotalCount_RSLPostcodeF0MappablePostcodeF1NotMappable";
    protected final String sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1NotMappable = "PercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1NotMappable";
    protected final String sTotalCount_RSLPostcodeF0NotMappablePostcodeF1Mappable = "TotalCount_RSLPostcodeF0NotMappablePostcodeF1Mappable";
    protected final String sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1Mappable = "PercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1Mappable";
    protected final String sTotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappable = "TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappable";
    protected final String sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1NotMappable = "PercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1NotMappable";
    protected final String sTotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged = "TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged";
    protected final String sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged = "PercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged";
    protected final String sTotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged = "TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged";
    protected final String sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged = "PercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged";
    protected final String sTotalCount_RSLPostcodeF0MappablePostcodeF1DNE = "TotalCount_RSLPostcodeF0MappablePostcodeF1DNE";
    protected final String sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1DNE = "PercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1DNE";
    protected final String sTotalCount_RSLPostcodeF0DNEPostcodeF1Mappable = "TotalCount_RSLPostcodeF0DNEPostcodeF1Mappable";
    protected final String sPercentageOfRSLCount0_RSLPostcodeF0DNEPostcodeF1Mappable = "PercentageOfRSLCount0_RSLPostcodeF0DNEPostcodeF1Mappable";
    protected final String sTotalCount_RSLPostcodeF0DNEPostcodeF1DNE = "TotalCount_RSLPostcodeF0DNEPostcodeF1DNE";
    protected final String sPercentageOfRSLCount0_RSLPostcodeF0DNEPostcodeF1DNE = "PercentageOfRSLCount0_RSLPostcodeF0DNEPostcodeF1DNE";
    protected final String sTotalCount_RSLPostcodeF0DNEPostcodeF1NotMappable = "TotalCount_RSLPostcodeF0DNEPostcodeF1NotMappable";
    protected final String sPercentageOfRSLCount0_RSLPostcodeF0DNEPostcodeF1NotMappable = "PercentageOfRSLCount0_RSLPostcodeF0DNEPostcodeF1NotMappable";
    protected final String sTotalCount_RSLPostcodeF0NotMappablePostcodeF1DNE = "TotalCount_RSLPostcodeF0NotMappablePostcodeF1DNE";
    protected final String sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1DNE = "PercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1DNE";

    // Counters
    // All
    protected int AllUOAllCount1;
//    protected int CouncilAllCount1;
//    protected int  RSLCount1;
    // Council
    // Single Time
    protected double CouncilPTITotalIncome;
    protected int TotalCount_CouncilPTITotalIncomeNonZero;
    protected int TotalCount_CouncilPTITotalIncomeZero;
    protected double CouncilPTSTotalIncome;
    protected int TotalCount_CouncilPTSTotalIncomeNonZero;
    protected int TotalCount_CouncilPTSTotalIncomeZero;
    protected double CouncilPTOTotalIncome;
    protected int TotalCount_CouncilPTOTotalIncomeNonZero;
    protected int TotalCount_CouncilPTOTotalIncomeZero;
    protected double CouncilTotalWeeklyHBEntitlement;
    protected int TotalCount_CouncilWeeklyHBEntitlementNonZero;
    protected int TotalCount_CouncilWeeklyHBEntitlementZero;
    protected double CouncilTotalWeeklyCTBEntitlement;
    protected int TotalCount_CouncilWeeklyCTBEntitlementNonZero;
    protected int TotalCount_CouncilWeeklyCTBEntitlementZero;
    protected double CouncilTotalWeeklyEligibleRentAmount;
    protected int TotalCount_CouncilTotalWeeklyEligibleRentAmountNonZero;
    protected int TotalCount_CouncilTotalWeeklyEligibleRentAmountZero;
    protected double CouncilTotalWeeklyEligibleCouncilTaxAmount;
    protected int TotalCount_CouncilWeeklyEligibleCouncilTaxAmountNonZero;
    protected int TotalCount_CouncilWeeklyEligibleCouncilTaxAmountZero;
    protected double CouncilTotalContractualRentAmount;
    protected int TotalCount_CouncilTotalContractualRentAmountNonZero;
    protected int TotalCount_CouncilTotalContractualRentAmountZero;
    protected double CouncilTotalWeeklyAdditionalDiscretionaryPayment;
    protected int TotalCount_CouncilTotalWeeklyAdditionalDiscretionaryPaymentNonZero;
    protected int TotalCount_CouncilTotalWeeklyAdditionalDiscretionaryPaymentZero;
    protected double CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    protected int TotalCount_CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero;
    protected int TotalCount_CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero;
    // Employment Education Training
    protected int TotalCount_CouncilEmployedClaimants;
    protected int TotalCount_CouncilSelfEmployedClaimants;
    protected int TotalCount_CouncilStudentsClaimants;
    // HLA
    protected int TotalCount_CouncilLHACases;
    protected int[] TotalCount_CouncilDisabilityPremiumAwardTT;
    protected int[] TotalCount_CouncilSevereDisabilityPremiumAwardTT;
    protected int[] TotalCount_CouncilDisabledChildPremiumAwardTT;
    protected int[] TotalCount_CouncilEnhancedDisabilityPremiumAwardTT;
    protected int[] TotalCount_CouncilDisabilityAwardTT;
    protected int[] TotalCount_CouncilPSI;
    protected int[] TotalCount_CouncilTTClaimant1;
    protected int[] TotalCount_CouncilTTClaimant0;
    protected long CouncilTotalHouseholdSize;
//    protected int CouncilAllCount1;
//    protected Integer CouncilAllCount0;
    // HB
    protected int CouncilCount1;
    protected int CouncilHBPTICount1;
    protected int CouncilHBPTSCount1;
    protected int CouncilHBPTOCount1;
    protected int CouncilCTBPTICount1;
    protected int CouncilCTBPTSCount1;
    protected int CouncilCTBPTOCount1;
    protected int CouncilCount0;
    protected int CouncilHBPTICount0;
    protected int CouncilHBPTSCount0;
    protected int CouncilHBPTOCount0;
    protected int CouncilCTBPTICount0;
    protected int CouncilCTBPTSCount0;
    protected int CouncilCTBPTOCount0;
    protected int[] CouncilEthnicGroupCount;
    protected int[] CouncilEthnicGroupCountTT;
    protected int TotalCount_CouncilPostcodeValidFormat;
    protected int TotalCount_CouncilPostcodeValid;
    // Other summary stats
    protected double CouncilTotal_RentArrears;
    protected double TotalCount_CouncilRentArrears;
    protected int TotalCount_CouncilRentArrearsNonZero;
    protected int TotalCount_CouncilRentArrearsZero;
    // RSL
    // Single Time
    // Income
    // PTI
    protected double RSLPTITotalIncome;
    protected int TotalCount_RSLHBPTITotalIncomeNonZero;
    protected int TotalCount_RSLHBPTITotalIncomeZero;
    // PTS
    protected double RSLPTSTotalIncome;
    protected int TotalCount_RSLHBPTSTotalIncomeNonZero;
    protected int TotalCount_RSLHBPTSTotalIncomeZero;
    // PTO
    protected double RSLPTOTotalIncome;
    protected int TotalCount_RSLHBPTOTotalIncomeNonZero;
    protected int TotalCount_RSLHBPTOTotalIncomeZero;
    protected double RSLTotalWeeklyHBEntitlement;
    protected int TotalCount_RSLWeeklyHBEntitlementNonZero;
    protected int TotalCount_RSLWeeklyHBEntitlementZero;
    protected double RSLTotalWeeklyCTBEntitlement;
    protected int TotalCount_RSLWeeklyCTBEntitlementNonZero;
    protected int RSLTotalWeeklyCTBEntitlementZeroCount;
    protected double RSLTotalWeeklyEligibleRentAmount;
    protected int RSLTotalWeeklyEligibleRentAmountNonZeroCount;
    protected int RSLTotalWeeklyEligibleRentAmountZeroCount;
    protected double RSLTotalWeeklyEligibleCouncilTaxAmount;
    protected int TotalCount_RSLWeeklyEligibleCouncilTaxAmountNonZero;
    protected int TotalCount_RSLWeeklyEligibleCouncilTaxAmountZero;
    protected double RSLTotalContractualRentAmount;
    protected int RSLTotalContractualRentAmountNonZeroCount;
    protected int RSLTotalContractualRentAmountZeroCount;
    protected double RSLTotalWeeklyAdditionalDiscretionaryPayment;
    protected int RSLTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    protected int RSLTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    protected double RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    protected int RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    protected int RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;
    // Employment Education Training
    protected int TotalCount_RSLEmployedClaimants;
    protected int TotalCount_RSLSelfEmployedClaimants;
    protected int TotalCount_RSLStudentsClaimants;
    // HLA
    protected int TotalCount_RSLLHACases;
    // Disability
    protected int[] TotalCount_RSLDisabilityPremiumAwardTT;
    protected int[] TotalCount_RSLSevereDisabilityPremiumAwardTT;
    protected int[] TotalCount_RSLDisabledChildPremiumAwardTT;
    protected int[] TotalCount_RSLEnhancedDisabilityPremiumAwardTT;
    protected int[] TotalCount_RSLDisabilityAwardTT;
    // Key Counts
    protected int[] TotalCount_RSLPSI;
    protected int[] TotalCount_RSLTTClaimant1;
    protected int[] TotalCount_RSLTTClaimant0;
    protected long RSLTotalHouseholdSize;
    // HB
    protected int RSLCount1;
    protected int RSLHBPTICount1;
    protected int RSLHBPTSCount1;
    protected int RSLHBPTOCount1;
    protected int RSLCTBPTICount1;
    protected int RSLCTBPTSCount1;
    protected int RSLCTBPTOCount1;
    protected int RSLCount0;
    protected int RSLHBPTICount0;
    protected int RSLHBPTSCount0;
    protected int RSLHBPTOCount0;
    protected int RSLCTBPTICount0;
    protected int RSLCTBPTSCount0;
    protected int RSLCTBPTOCount0;
    protected int[] RSLEthnicGroupCount;
    protected int[] RSLEthnicGroupCountTT;
    protected int TotalCount_RSLPostcodeFValidFormat;
    protected int TotalCount_RSLPostcodeFMappable;

    // Compare 2 Times
    // Council
    protected int TotalCount_CouncilInBoth;
    protected double SumArrearsDiffInBoth;

    protected int TotalCount_CouncilInBothG0;
    protected double SumArrearsDiffInBothG0;
    protected int TotalCount_CouncilInBothG1;
    protected double SumArrearsDiffInBothG1;

    protected int TotalCount_CouncilTT1ToMinus999TT;
    protected int TotalCount_CouncilMinus999TTToTT1;
    protected int TotalCount_CouncilTT1ToPrivateDeregulatedTTs;
    protected int TotalCount_CouncilPrivateDeregulatedTTsToTT1;
    protected int TotalCount_CouncilTT1ToCTBTTs;
    protected int TotalCount_CouncilCTBTTsToTT1;
    protected int TotalCount_CouncilTT1ToTT4;
    protected int TotalCount_CouncilTT4ToTT1;
    protected int TotalCount_CouncilTTChangeClaimant;
    protected int TotalCount_CouncilPostcodeChangeWithinTT1;
    protected int TotalCount_CouncilPostcodeF0MappablePostcodeF1Mappable;
    protected int TotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged;
    protected int TotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeChanged;
    protected int TotalCount_CouncilPostcodeF0MappablePostcodeF1NotMappable;
    protected int TotalCount_CouncilPostcodeF0NotMappablePostcodeF1Mappable;
    protected int TotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappable;
    protected int TotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged;
    protected int TotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged;
    protected int TotalCount_CouncilPostcodeF0MappablePostcodeF1DNE;
    protected int TotalCount_CouncilPostcodeF0DNEPostcodeF1Mappable;
    protected int TotalCount_CouncilPostcodeF0DNEPostcodeF1DNE;
    protected int TotalCount_CouncilPostcodeF0DNEPostcodeF1NotMappable;
    protected int TotalCount_CouncilPostcodeF0NotMappablePostcodeF1DNE;
    // RSL
    protected int TotalCount_RSLTT4ToMinus999TT;
    protected int TotalCount_RSLMinus999TTToTT4;
    protected int TotalCount_RSLTT4ToPrivateDeregulatedTTs;
    protected int TotalCount_RSLPrivateDeregulatedTTsToTT4;
    protected int TotalCount_RSLTT4ToCTBTTs;
    protected int TotalCount_RSLCTBTTsToTT4;
    protected int TotalCount_RSLTT4ToTT1;
    protected int TotalCount_RSLTT1ToTT4;
    protected int TotalCount_RSLTTChangeClaimant;
    protected int TotalCount_RSLPostcodeChangeWithinTT4;
    protected int TotalCount_RSLPostcodeF0MappablePostcodeF1Mappable;
    protected int TotalCount_RSLPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged;
    protected int TotalCount_RSLPostcodeF0MappablePostcodeF1MappablePostcodeChanged;
    protected int TotalCount_RSLPostcodeF0MappablePostcodeF1NotMappable;
    protected int TotalCount_RSLPostcodeF0NotMappablePostcodeF1Mappable;
    protected int TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappable;
    protected int TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged;
    protected int TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged;
    protected int TotalCount_RSLPostcodeF0MappablePostcodeF1DNE;
    protected int TotalCount_RSLPostcodeF0DNEPostcodeF1Mappable;
    protected int TotalCount_RSLPostcodeF0DNEPostcodeF1DNE;
    protected int TotalCount_RSLPostcodeF0DNEPostcodeF1NotMappable;
    protected int TotalCount_RSLPostcodeF0NotMappablePostcodeF1DNE;
//    protected int AllUOAllCount00;
//    protected int AllUOAllCount0;
//    protected int AllUOAllCount1;
    protected int AllUOLinkedRecordCount0;
    protected int AllUOLinkedRecordCount1;
    protected int CouncilLinkedRecordCount0;
    protected int CouncilLinkedRecordCount1;
    protected int RSLLinkedRecordCount0;
    protected int RSLLinkedRecordCount1;

    public DW_SummaryUO(
            DW_Environment env,
            int nTT,
            int nEG,
            int nPSI,
            boolean handleOutOfMemoryError) {
        super(env, nTT, nEG, nPSI, handleOutOfMemoryError);
        init(nTT, nEG, nPSI);
    }

    private void init(int nTT, int nEG, int nPSI) {
        initSingleTimeStrings(nTT, nEG, nPSI);
        // Council
        TotalCount_CouncilDisabilityPremiumAwardTT = new int[nTT];
        TotalCount_CouncilSevereDisabilityPremiumAwardTT = new int[nTT];
        TotalCount_CouncilDisabledChildPremiumAwardTT = new int[nTT];
        TotalCount_CouncilEnhancedDisabilityPremiumAwardTT = new int[nTT];
        TotalCount_CouncilDisabilityAwardTT = new int[nTT];
        TotalCount_CouncilPSI = new int[nPSI];
        TotalCount_CouncilTTClaimant1 = new int[nTT];
        TotalCount_CouncilTTClaimant0 = new int[nTT];
        CouncilEthnicGroupCount = new int[nEG];
        // RSL
        TotalCount_RSLDisabilityPremiumAwardTT = new int[nTT];
        TotalCount_RSLSevereDisabilityPremiumAwardTT = new int[nTT];
        TotalCount_RSLDisabledChildPremiumAwardTT = new int[nTT];
        TotalCount_RSLEnhancedDisabilityPremiumAwardTT = new int[nTT];
        TotalCount_RSLDisabilityAwardTT = new int[nTT];
        TotalCount_RSLPSI = new int[nPSI];
        TotalCount_RSLTTClaimant1 = new int[nTT];
        TotalCount_RSLTTClaimant0 = new int[nTT];
        RSLEthnicGroupCount = new int[nEG];
    }

    @Override
    protected void initSingleTimeStrings(int nTT, int nEG, int nPSI) {
        super.initSingleTimeStrings(nTT, nEG, nPSI);
        // Council
        sTotalCount_CouncilPSI = new String[nPSI];
        sPercentageOfCouncilCount1_CouncilPSI = new String[nPSI];
        for (int i = 1; i < nPSI; i++) {
            sTotalCount_CouncilPSI[i] = "TotalCount_CouncilPSI" + i;
            sPercentageOfCouncilCount1_CouncilPSI[i] = "PercentageOfCouncilCount1_CouncilPSI" + i;
        }
        // All
        sTotalCount_CouncilEthnicGroupClaimant = new String[nEG];
        sPercentageOfCouncilCount1_CouncilEthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            String EGN = SHBE_Handler.getEthnicityGroupName(i);
            sTotalCount_CouncilEthnicGroupClaimant[i] = "TotalCount_CouncilEthnicGroupClaimant" + EGN;
            sPercentageOfCouncilCount1_CouncilEthnicGroupClaimant[i] = "PercentageOfCouncilCount1_CouncilEthnicGroupClaimant" + EGN;
        }
        sTotalCount_CouncilClaimantTT = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            // Claimants
            sTotalCount_CouncilClaimantTT[i] = "TotalCount_CouncilClaimantTT" + i;
        }
        // RSL
        sTotalCount_RSLPSI = new String[nPSI];
        sPercentageOfRSLCount1_RSLPSI = new String[nPSI];
        for (int i = 1; i < nPSI; i++) {
            sTotalCount_RSLPSI[i] = "TotalCount_RSLPSI" + i;
            sPercentageOfRSLCount1_RSLPSI[i] = "PercentageOfRSLCount1_PSI" + i;
        }
        sTotalCount_RSLClaimantTT = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            // Claimants
            sTotalCount_RSLClaimantTT[i] = "TotalCount_RSLClaimantTT" + i;
        }
        sTotalCount_RSLEthnicGroupClaimant = new String[nEG];
        sPercentageOfRSLCount1_RSLEthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            String EGN = SHBE_Handler.getEthnicityGroupName(i);
            sTotalCount_RSLEthnicGroupClaimant[i] = "TotalCount_RSLEthnicGroupClaimant" + EGN;
            sPercentageOfRSLCount1_RSLEthnicGroupClaimant[i] = "PercentageOfRSLCount_RSLEthnicGroupClaimant" + EGN;
        }
    }

    @Override
    protected void initCounts(int nTT, int nEG, int nPSI) {
        initSingleTimeCounts(nTT, nEG, nPSI);
        initCompare2TimesCounts();
    }

    @Override
    protected void initSingleTimeCounts(int nTT, int nEG, int nPSI) {
        super.initSingleTimeCounts(nTT, nEG, nPSI);
        AllUOAllCount1 = 0;
        // Council
        for (int i = 1; i < nPSI; i++) {
            TotalCount_CouncilPSI[i] = 0;
        }
        CouncilPTITotalIncome = 0L;
        TotalCount_CouncilPTITotalIncomeNonZero = 0;
        TotalCount_CouncilPTITotalIncomeZero = 0;
        CouncilPTSTotalIncome = 0L;
        TotalCount_CouncilPTSTotalIncomeNonZero = 0;
        TotalCount_CouncilPTSTotalIncomeZero = 0;
        CouncilPTOTotalIncome = 0L;
        TotalCount_CouncilPTOTotalIncomeNonZero = 0;
        TotalCount_CouncilPTOTotalIncomeZero = 0;
        CouncilTotalWeeklyHBEntitlement = 0.0d;
        TotalCount_CouncilWeeklyHBEntitlementNonZero = 0;
        TotalCount_CouncilWeeklyHBEntitlementZero = 0;
        CouncilTotalWeeklyCTBEntitlement = 0.0d;
        TotalCount_CouncilWeeklyCTBEntitlementNonZero = 0;
        TotalCount_CouncilWeeklyCTBEntitlementZero = 0;
        CouncilTotalWeeklyEligibleRentAmount = 0.0d;
        TotalCount_CouncilTotalWeeklyEligibleRentAmountNonZero = 0;
        TotalCount_CouncilTotalWeeklyEligibleRentAmountZero = 0;
        CouncilTotalWeeklyEligibleCouncilTaxAmount = 0.0d;
        TotalCount_CouncilWeeklyEligibleCouncilTaxAmountNonZero = 0;
        TotalCount_CouncilWeeklyEligibleCouncilTaxAmountZero = 0;
        CouncilTotalContractualRentAmount = 0.0d;
        TotalCount_CouncilTotalContractualRentAmountNonZero = 0;
        TotalCount_CouncilTotalContractualRentAmountZero = 0;
        CouncilTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        TotalCount_CouncilTotalWeeklyAdditionalDiscretionaryPaymentNonZero = 0;
        TotalCount_CouncilTotalWeeklyAdditionalDiscretionaryPaymentZero = 0;
        CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        TotalCount_CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = 0;
        // Demographic Counts
        TotalCount_CouncilEmployedClaimants = 0;
        TotalCount_CouncilSelfEmployedClaimants = 0;
        TotalCount_CouncilStudentsClaimants = 0;
        TotalCount_CouncilLHACases = 0;
        for (int i = 1; i < nTT; i++) {
            TotalCount_CouncilTTClaimant1[i] = 0;
            TotalCount_CouncilDisabilityPremiumAwardTT[i] = 0;
            TotalCount_CouncilSevereDisabilityPremiumAwardTT[i] = 0;
            TotalCount_CouncilDisabledChildPremiumAwardTT[i] = 0;
            TotalCount_CouncilEnhancedDisabilityPremiumAwardTT[i] = 0;
            TotalCount_CouncilDisabilityAwardTT[i] = 0;
        }
        // Ethnicity
        for (int i = 1; i < nEG; i++) {
            CouncilEthnicGroupCount[i] = 0;
        }
        // Key Counts
        //CouncilCount0 = 0;
        CouncilCount1 = 0;
        CouncilHBPTICount1 = 0;
        CouncilHBPTSCount1 = 0;
        CouncilHBPTOCount1 = 0;
        CouncilCTBPTICount1 = 0;
        CouncilCTBPTSCount1 = 0;
        CouncilCTBPTOCount1 = 0;
        //CouncilLinkedRecordCount0 = 0;
        CouncilLinkedRecordCount1 = 0;
        // Postcode Counts
        TotalCount_CouncilPostcodeValidFormat = 0;
        TotalCount_CouncilPostcodeValid = 0;
        TotalCount_CouncilTTChangeClaimant = 0;
        // Household Size
        CouncilTotalHouseholdSize = 0L;
        // RSL
        for (int i = 1; i < nPSI; i++) {
            TotalCount_RSLPSI[i] = 0;
        }
        RSLTotalWeeklyHBEntitlement = 0.0d;
        RSLPTITotalIncome = 0L;
        TotalCount_RSLHBPTITotalIncomeZero = 0;
        TotalCount_RSLHBPTITotalIncomeNonZero = 0;
        RSLPTSTotalIncome = 0L;
        TotalCount_RSLHBPTSTotalIncomeZero = 0;
        TotalCount_RSLHBPTSTotalIncomeNonZero = 0;
        RSLPTOTotalIncome = 0L;
        TotalCount_RSLHBPTOTotalIncomeZero = 0;
        TotalCount_RSLHBPTOTotalIncomeNonZero = 0;
        TotalCount_RSLWeeklyHBEntitlementNonZero = 0;
        TotalCount_RSLWeeklyHBEntitlementZero = 0;
        RSLTotalWeeklyCTBEntitlement = 0.0d;
        TotalCount_RSLWeeklyCTBEntitlementNonZero = 0;
        RSLTotalWeeklyCTBEntitlementZeroCount = 0;
        RSLTotalWeeklyEligibleRentAmount = 0.0d;
        RSLTotalWeeklyEligibleRentAmountNonZeroCount = 0;
        RSLTotalWeeklyEligibleRentAmountZeroCount = 0;
        RSLTotalWeeklyEligibleCouncilTaxAmount = 0.0d;
        TotalCount_RSLWeeklyEligibleCouncilTaxAmountNonZero = 0;
        TotalCount_RSLWeeklyEligibleCouncilTaxAmountZero = 0;
        RSLTotalContractualRentAmount = 0.0d;
        RSLTotalContractualRentAmountNonZeroCount = 0;
        RSLTotalContractualRentAmountZeroCount = 0;
        RSLTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        RSLTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        RSLTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;
        // Demographic Counts
        TotalCount_RSLEmployedClaimants = 0;
        TotalCount_RSLSelfEmployedClaimants = 0;
        TotalCount_RSLStudentsClaimants = 0;
        TotalCount_RSLLHACases = 0;
        for (int i = 1; i < nTT; i++) {
            TotalCount_RSLTTClaimant1[i] = 0;
            TotalCount_RSLDisabilityPremiumAwardTT[i] = 0;
            TotalCount_RSLSevereDisabilityPremiumAwardTT[i] = 0;
            TotalCount_RSLDisabledChildPremiumAwardTT[i] = 0;
            TotalCount_RSLEnhancedDisabilityPremiumAwardTT[i] = 0;
            TotalCount_RSLDisabilityAwardTT[i] = 0;
        }
        // Ethnicity
        for (int i = 1; i < nEG; i++) {
            RSLEthnicGroupCount[i] = 0;
        }
        // Key Counts
        //RSLCount0 = 0;
        RSLCount1 = 0;
        RSLHBPTICount1 = 0;
        RSLHBPTSCount1 = 0;
        RSLHBPTOCount1 = 0;
        RSLCTBPTICount1 = 0;
        RSLCTBPTSCount1 = 0;
        RSLCTBPTOCount1 = 0;
        //RSLLinkedRecordCount0 = 0;
        RSLLinkedRecordCount1 = 0;
        // Postcode Counts
        TotalCount_RSLPostcodeFValidFormat = 0;
        TotalCount_RSLPostcodeFMappable = 0;
        TotalCount_RSLTTChangeClaimant = 0;
        // Household Size
        RSLTotalHouseholdSize = 0L;

        // UO only
//        AllUOAllCount1 = 0;
//        CouncilAllCount1 = 0;
        //AllUOLinkedRecordCount0 = 0;
        AllUOLinkedRecordCount1 = 0;
        CouncilLinkedRecordCount1 = 0;
        RSLLinkedRecordCount1 = 0;
        // RentArrears
        CouncilTotal_RentArrears = 0.0d;
        TotalCount_CouncilRentArrears = 0;
        TotalCount_CouncilRentArrearsZero = 0;
        TotalCount_CouncilRentArrearsNonZero = 0;
    }

    @Override
    protected void initCompare2TimesCounts() {
        super.initCompare2TimesCounts();
        // Council
        TotalCount_CouncilInBoth = 0;
        SumArrearsDiffInBoth = 0;
        TotalCount_CouncilInBothG0 = 0;
        SumArrearsDiffInBothG0 = 0;
        TotalCount_CouncilInBothG1 = 0;
        SumArrearsDiffInBothG1 = 0;
        TotalCount_CouncilMinus999TTToTT1 = 0;
        TotalCount_CouncilTT1ToMinus999TT = 0;
        TotalCount_CouncilTT1ToPrivateDeregulatedTTs = 0;
        TotalCount_CouncilPrivateDeregulatedTTsToTT1 = 0;
        TotalCount_CouncilTT1ToCTBTTs = 0;
        TotalCount_CouncilCTBTTsToTT1 = 0;
        TotalCount_CouncilTT1ToTT4 = 0;
        TotalCount_CouncilTT4ToTT1 = 0;
        TotalCount_CouncilPostcodeChangeWithinTT1 = 0;
        TotalCount_CouncilTTChangeClaimant = 0;
        TotalCount_CouncilPostcodeF0MappablePostcodeF1Mappable = 0;
        TotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged = 0;
        TotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeChanged = 0;
        TotalCount_CouncilPostcodeF0MappablePostcodeF1NotMappable = 0;
        TotalCount_CouncilPostcodeF0NotMappablePostcodeF1Mappable = 0;
        TotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappable = 0;
        TotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged = 0;
        TotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged = 0;
        TotalCount_CouncilPostcodeF0MappablePostcodeF1DNE = 0;
        TotalCount_CouncilPostcodeF0DNEPostcodeF1Mappable = 0;
        TotalCount_CouncilPostcodeF0DNEPostcodeF1DNE = 0;
        TotalCount_CouncilPostcodeF0DNEPostcodeF1NotMappable = 0;
        TotalCount_CouncilPostcodeF0NotMappablePostcodeF1DNE = 0;
        //  RSL
        TotalCount_RSLMinus999TTToTT4 = 0;
        TotalCount_RSLTT4ToMinus999TT = 0;
        TotalCount_RSLTT4ToPrivateDeregulatedTTs = 0;
        TotalCount_RSLPrivateDeregulatedTTsToTT4 = 0;
        TotalCount_RSLTT4ToCTBTTs = 0;
        TotalCount_RSLCTBTTsToTT4 = 0;
        TotalCount_RSLTT1ToTT4 = 0;
        TotalCount_RSLTT4ToTT1 = 0;
        TotalCount_RSLPostcodeChangeWithinTT4 = 0;
        TotalCount_RSLTTChangeClaimant = 0;
        TotalCount_RSLPostcodeF0MappablePostcodeF1Mappable = 0;
        TotalCount_RSLPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged = 0;
        TotalCount_RSLPostcodeF0MappablePostcodeF1MappablePostcodeChanged = 0;
        TotalCount_RSLPostcodeF0MappablePostcodeF1NotMappable = 0;
        TotalCount_RSLPostcodeF0NotMappablePostcodeF1Mappable = 0;
        TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappable = 0;
        TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged = 0;
        TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged = 0;
        TotalCount_RSLPostcodeF0MappablePostcodeF1DNE = 0;
        TotalCount_RSLPostcodeF0DNEPostcodeF1Mappable = 0;
        TotalCount_RSLPostcodeF0DNEPostcodeF1DNE = 0;
        TotalCount_RSLPostcodeF0DNEPostcodeF1NotMappable = 0;
        TotalCount_RSLPostcodeF0NotMappablePostcodeF1DNE = 0;
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
        summary.put(sTotalCount_CouncilInBoth,
                Integer.toString(TotalCount_CouncilInBoth));
        summary.put(
                sSumArrearsDiffInBoth,
                Double.toString(SumArrearsDiffInBoth));

        summary.put(sTotalCount_CouncilInBothG0,
                Integer.toString(TotalCount_CouncilInBothG0));
        summary.put(
                sSumArrearsDiffInBothG0,
                Double.toString(SumArrearsDiffInBothG0));

        summary.put(sTotalCount_CouncilInBothG1,
                Integer.toString(TotalCount_CouncilInBothG1));
        summary.put(
                sSumArrearsDiffInBothG1,
                Double.toString(SumArrearsDiffInBothG1));

        d = CouncilCount0;
        // Tenancy Type
        summary.put(
                sTotalCount_CouncilTTChangeClaimant,
                Integer.toString(TotalCount_CouncilTTChangeClaimant));
        summary.put(
                sTotalCount_CouncilMinus999TTToTT1,
                Integer.toString(TotalCount_CouncilMinus999TTToTT1));
        summary.put(
                sTotalCount_CouncilTT1ToMinus999TT,
                Integer.toString(TotalCount_CouncilTT1ToMinus999TT));
        if (d > 0) {
            percentage = (TotalCount_CouncilTTChangeClaimant * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount0_CouncilTTChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // Postcode
        summary.put(sTotalCount_CouncilPostcodeF0MappablePostcodeF1Mappable,
                Integer.toString(TotalCount_CouncilPostcodeF0MappablePostcodeF1Mappable));
        summary.put(sTotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged,
                Integer.toString(TotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged));
        summary.put(sTotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeChange,
                Integer.toString(TotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeChanged));
        summary.put(sTotalCount_CouncilPostcodeF0MappablePostcodeF1NotMappable,
                Integer.toString(TotalCount_CouncilPostcodeF0MappablePostcodeF1NotMappable));
        summary.put(sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1Mappable,
                Integer.toString(TotalCount_CouncilPostcodeF0NotMappablePostcodeF1Mappable));
        summary.put(sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappable,
                Integer.toString(TotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappable));
        summary.put(sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged,
                Integer.toString(TotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged));
        summary.put(sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged,
                Integer.toString(TotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged));
        summary.put(sTotalCount_CouncilPostcodeF0MappablePostcodeF1DNE,
                Integer.toString(TotalCount_CouncilPostcodeF0MappablePostcodeF1DNE));
        summary.put(sTotalCount_CouncilPostcodeF0DNEPostcodeF1Mappable,
                Integer.toString(TotalCount_CouncilPostcodeF0DNEPostcodeF1Mappable));
        summary.put(sTotalCount_CouncilPostcodeF0DNEPostcodeF1DNE,
                Integer.toString(TotalCount_CouncilPostcodeF0DNEPostcodeF1DNE));
        summary.put(sTotalCount_CouncilPostcodeF0DNEPostcodeF1NotMappable,
                Integer.toString(TotalCount_CouncilPostcodeF0DNEPostcodeF1NotMappable));
        summary.put(sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1DNE,
                Integer.toString(TotalCount_CouncilPostcodeF0NotMappablePostcodeF1DNE));
        if (d > 0) {
            percentage = (TotalCount_CouncilPostcodeF0MappablePostcodeF1Mappable * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1Mappable,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeChanged * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeChange,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CouncilPostcodeF0MappablePostcodeF1NotMappable * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1NotMappable,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CouncilPostcodeF0NotMappablePostcodeF1Mappable * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1Mappable,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappable * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1NotMappable,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcode1NotMappablePostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CouncilPostcodeF0MappablePostcodeF1DNE * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1DNE,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CouncilPostcodeF0DNEPostcodeF1Mappable * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount0_CouncilPostcodeF0DNEPostcodeF1Mappable,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CouncilPostcodeF0DNEPostcodeF1DNE * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount0_CouncilPostcodeF0DNEPostcodeF1DNE,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CouncilPostcodeF0DNEPostcodeF1NotMappable * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount0_CouncilPostcodeF0DNEPostcodeF1NotMappable,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CouncilPostcodeF0NotMappablePostcodeF1DNE * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1DNE,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());

        }
        summary.put(
                sTotalCount_CouncilPrivateDeregulatedTTsToTT1,
                Integer.toString(TotalCount_CouncilPrivateDeregulatedTTsToTT1));
        d = TotalCount_CouncilTTClaimant0[1];
        summary.put(
                sTotalCount_CouncilTT1ToTT4,
                Integer.toString(TotalCount_CouncilTT1ToTT4));
        summary.put(
                sTotalCount_CouncilTT1ToPrivateDeregulatedTTs,
                Integer.toString(TotalCount_CouncilTT1ToPrivateDeregulatedTTs));
        summary.put(
                sTotalCount_CouncilPostcodeChangeWithinTT1,
                Integer.toString(TotalCount_CouncilPostcodeChangeWithinTT1));
        if (d > 0) {
            percentage = (TotalCount_CouncilTT1ToPrivateDeregulatedTTs * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount0_CouncilTT1ToPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CouncilPostcodeChangeWithinTT1 * 100.0d) / d;
            summary.put(sPercentageOfCouncilTT1_CouncilPostcodeChangeWithinTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CouncilTT1ToTT4 * 100.0d) / d;
            summary.put(sPercentageOfCouncilTT1_CouncilTT1ToTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // RSL
        // HB
        // Tenancy Type
        summary.put(sTotalCount_RSLTTChangeClaimant,
                Integer.toString(TotalCount_RSLTTChangeClaimant));
        summary.put(sTotalCount_RSLMinus999TTToTT4,
                Integer.toString(TotalCount_RSLMinus999TTToTT4));
        if (d > 0) {
            percentage = (TotalCount_RSLTTChangeClaimant * 100.0d) / d;
            summary.put(sPercentageOfRSLHB_RSLTTChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // Postcode
        summary.put(sTotalCount_RSLPostcodeF0MappablePostcodeF1Mappable,
                Integer.toString(TotalCount_RSLPostcodeF0MappablePostcodeF1Mappable));
        summary.put(sTotalCount_RSLPostcodeF0MappablePostcode1MappablePostcodeNotChanged,
                Integer.toString(TotalCount_RSLPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged));
        summary.put(sTotalCount_RSLPostcodeF0MappablePostcodeF1MappablePostcodeChanged,
                Integer.toString(TotalCount_RSLPostcodeF0MappablePostcodeF1MappablePostcodeChanged));
        summary.put(sTotalCount_RSLPostcodeF0MappablePostcodeF1NotMappable,
                Integer.toString(TotalCount_RSLPostcodeF0MappablePostcodeF1NotMappable));
        summary.put(sTotalCount_RSLPostcodeF0NotMappablePostcodeF1Mappable,
                Integer.toString(TotalCount_RSLPostcodeF0NotMappablePostcodeF1Mappable));
        summary.put(sTotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappable,
                Integer.toString(TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappable));
        summary.put(sTotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged,
                Integer.toString(TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged));
        summary.put(sTotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged,
                Integer.toString(TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged));
        summary.put(sTotalCount_RSLPostcodeF0MappablePostcodeF1DNE,
                Integer.toString(TotalCount_RSLPostcodeF0MappablePostcodeF1DNE));
        summary.put(sTotalCount_RSLPostcodeF0DNEPostcodeF1Mappable,
                Integer.toString(TotalCount_RSLPostcodeF0DNEPostcodeF1Mappable));
        summary.put(sTotalCount_RSLPostcodeF0DNEPostcodeF1DNE,
                Integer.toString(TotalCount_RSLPostcodeF0DNEPostcodeF1DNE));
        summary.put(sTotalCount_RSLPostcodeF0DNEPostcodeF1NotMappable,
                Integer.toString(TotalCount_RSLPostcodeF0DNEPostcodeF1NotMappable));
        summary.put(sTotalCount_RSLPostcodeF0NotMappablePostcodeF1DNE,
                Integer.toString(TotalCount_RSLPostcodeF0NotMappablePostcodeF1DNE));
        d = RSLCount0;
        if (d > 0) {
            percentage = (TotalCount_RSLPostcodeF0MappablePostcodeF1Mappable * 100.0d) / d;
            summary.put(sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1Mappable,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_RSLPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged * 100.0d) / d;
            summary.put(sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_RSLPostcodeF0MappablePostcodeF1MappablePostcodeChanged * 100.0d) / d;
            summary.put(sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1MappablePostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_RSLPostcodeF0MappablePostcodeF1NotMappable * 100.0d) / d;
            summary.put(sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1NotMappable,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_RSLPostcodeF0NotMappablePostcodeF1Mappable * 100.0d) / d;
            summary.put(sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1Mappable,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappable * 100.0d) / d;
            summary.put(sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1NotMappable,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged * 100.0d) / d;
            summary.put(sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged * 100.0d) / d;
            summary.put(sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_RSLPostcodeF0MappablePostcodeF1DNE * 100.0d) / d;
            summary.put(sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1DNE,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_RSLPostcodeF0DNEPostcodeF1Mappable * 100.0d) / d;
            summary.put(sPercentageOfRSLCount0_RSLPostcodeF0DNEPostcodeF1Mappable,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_RSLPostcodeF0DNEPostcodeF1DNE * 100.0d) / d;
            summary.put(sPercentageOfRSLCount0_RSLPostcodeF0DNEPostcodeF1DNE,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_RSLPostcodeF0DNEPostcodeF1NotMappable * 100.0d) / d;
            summary.put(sPercentageOfRSLCount0_RSLPostcodeF0DNEPostcodeF1NotMappable,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_RSLPostcodeF0NotMappablePostcodeF1DNE * 100.0d) / d;
            summary.put(sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1DNE,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // Private Deregulated
        summary.put(sTotalCount_RSLPrivateDeregulatedTTsToTT4,
                Integer.toString(TotalCount_RSLPrivateDeregulatedTTsToTT4));
        // Social
        //d = TotalCount_RSLTTClaimant0[1];
        //d = TotalCount_RSLTTClaimant0[1] + TotalCount_RSLTTClaimant0[4];
        summary.put(sTotalCount_RSLTT1ToTT4,
                Integer.toString(TotalCount_RSLTT1ToTT4));
        if (d > 0) {
            percentage = (TotalCount_RSLTT1ToTT4 * 100.0d) / d;
            summary.put(sRSLPercentageOfRSLTT1_TT1ToTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = TotalCount_RSLTTClaimant0[4];
        summary.put(sTotalCount_RSLTT4ToTT1,
                Integer.toString(TotalCount_RSLTT4ToTT1));
        summary.put(sTotalCount_RSLTT4ToPrivateDeregulatedTTs,
                Integer.toString(TotalCount_RSLTT4ToPrivateDeregulatedTTs));
        summary.put(sTotalCount_RSLPostcodeChangeWithinTT4,
                Integer.toString(TotalCount_RSLPostcodeChangeWithinTT4));
        if (d > 0) {
            percentage = (TotalCount_RSLTT4ToPrivateDeregulatedTTs * 100.0d) / d;
            summary.put(sPercentageOfRSLTT4_RSLTT4ToPrivateDeregulatedTTs,
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
                sTotalCount_CouncilRentArrears,
                "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                        BigDecimal.valueOf(TotalCount_CouncilRentArrears),
                        2, RoundingMode.HALF_UP));
        summary.put(
                sTotalCount_CouncilRentArrearsNonZero,
                "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                        BigDecimal.valueOf(TotalCount_CouncilRentArrearsNonZero),
                        2, RoundingMode.HALF_UP));
        summary.put(
                sTotalCount_CouncilRentArrearsZero,
                "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                        BigDecimal.valueOf(TotalCount_CouncilRentArrearsZero),
                        2, RoundingMode.HALF_UP));
        if (TotalCount_CouncilRentArrears != 0.0d) {
            summary.put(sAverage_CouncilRentArrears,
                    "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                            BigDecimal.valueOf(CouncilTotal_RentArrears / (double) TotalCount_CouncilRentArrears),
                            2, RoundingMode.HALF_UP));
        }
        if (TotalCount_CouncilRentArrearsNonZero != 0.0d) {
            summary.put(sAverage_NonZero_CouncilRentArrears,
                    "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                            BigDecimal.valueOf(CouncilTotal_RentArrears / TotalCount_CouncilRentArrearsNonZero),
                            2, RoundingMode.HALF_UP));
        }
    }

    @Override
    protected void addToSummarySingleTimeCounts0(
            int nTT,
            HashMap<String, String> summary) {
        super.addToSummarySingleTimeCounts0(nTT, summary);
        // Council
//        CouncilAllCount1 = CouncilCount1 + CouncilCTBCount1;
        summary.put(sCouncilCount1, Integer.toString(CouncilCount1));
        summary.put(sCouncilHBPTICount1, Integer.toString(CouncilHBPTICount1));
        summary.put(sCouncilHBPTSCount1, Integer.toString(CouncilHBPTSCount1));
        summary.put(sCouncilHBPTOCount1, Integer.toString(CouncilHBPTOCount1));
        summary.put(sCouncilCTBPTICount1, Integer.toString(CouncilCTBPTICount1));
        summary.put(sCouncilCTBPTSCount1, Integer.toString(CouncilCTBPTSCount1));
        summary.put(sCouncilCTBPTOCount1, Integer.toString(CouncilCTBPTOCount1));
        // RSL
//        RSLCount1 = RSLCount1 + RSLCTBCount1;
        summary.put(sRSLCount1, Integer.toString(RSLCount1));
        summary.put(sRSLHBPTICount1, Integer.toString(RSLHBPTICount1));
        summary.put(sRSLHBPTSCount1, Integer.toString(RSLHBPTSCount1));
        summary.put(sRSLHBPTOCount1, Integer.toString(RSLHBPTOCount1));
        summary.put(sRSLCTBPTICount1, Integer.toString(RSLCTBPTICount1));
        summary.put(sRSLCTBPTSCount1, Integer.toString(RSLCTBPTSCount1));
        summary.put(sRSLCTBPTOCount1, Integer.toString(RSLCTBPTOCount1));
    }

    @Override
    protected void addToSummarySingleTimeRates0(
            int nTT,
            HashMap<String, String> summary) {
        super.addToSummarySingleTimeRates0(nTT, summary);
        double ave;
        // Council
        // HouseholdSize
        summary.put(sTotal_CouncilHouseholdSize,
                Long.toString(CouncilTotalHouseholdSize));
        if (CouncilCount1 > 0) {
            ave = CouncilTotalHouseholdSize / (double) CouncilCount1;
            summary.put(sAverage_NonZero_CouncilHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_CouncilHouseholdSize,
                    s0);
        }
        // RSL
        // HouseholdSize
        summary.put(sTotal_RSLHouseholdSize,
                Long.toString(RSLTotalHouseholdSize));
        if (RSLCount1 > 0) {
            ave = RSLTotalHouseholdSize / (double) RSLCount1;
            summary.put(sAverage_NonZero_RSLHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_RSLHouseholdSize,
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
            summary.put(sTotalCount_CouncilPSI[i],
                    Long.toString(TotalCount_CouncilPSI[i]));
        }
        // RSL
        for (int i = 1; i < nPSI; i++) {
            summary.put(sTotalCount_RSLPSI[i],
                    Long.toString(TotalCount_RSLPSI[i]));
        }
    }

    @Override
    protected void addToSummarySingleTimePSIRates(
            int nTT,
            int nPSI,
            HashMap<String, String> summary) {
        //super.addToSummarySingleTimePSIRates(nTT, nPSI, summary); // This should not be done :-)
        double ave;
        double d;
        double n;
        // All
        for (int i = 1; i < nPSI; i++) {
            n = Integer.valueOf(summary.get(sTotalCount_AllPSI[i]));
            d = CouncilCount1 + RSLCount1;
            if (d > 0) {
                ave = (n * 100.0d) / d;
                summary.put(sPercentageOfAll_AllPSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
        }
        // Council
        for (int i = 1; i < nPSI; i++) {
            n = Integer.valueOf(summary.get(sTotalCount_CouncilPSI[i]));
            d = CouncilCount1;
            if (d > 0) {
                ave = (n * 100.0d) / d;
                summary.put(sPercentageOfCouncilCount1_CouncilPSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
        }
        // RSL
        for (int i = 1; i < nPSI; i++) {
            n = Integer.valueOf(summary.get(sTotalCount_RSLPSI[i]));
            d = RSLCount1;
            if (d > 0) {
                ave = (n * 100.0d) / d;
                summary.put(sPercentageOfRSLCount1_RSLPSI[i],
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
        t = TotalCount_CouncilDisabilityAwardTT[1]
                + TotalCount_CouncilDisabilityAwardTT[2]
                + TotalCount_CouncilDisabilityAwardTT[3]
                + TotalCount_CouncilDisabilityAwardTT[4]
                + TotalCount_CouncilDisabilityAwardTT[5]
                + TotalCount_CouncilDisabilityAwardTT[6]
                + TotalCount_CouncilDisabilityAwardTT[7]
                + TotalCount_CouncilDisabilityAwardTT[8]
                + TotalCount_CouncilDisabilityAwardTT[9];
        summary.put(
                sTotalCount_CouncilDisabilityAward,
                Integer.toString(t));
        // DisabilityPremiumAward
        t = TotalCount_CouncilDisabilityPremiumAwardTT[1]
                + TotalCount_CouncilDisabilityPremiumAwardTT[2]
                + TotalCount_CouncilDisabilityPremiumAwardTT[3]
                + TotalCount_CouncilDisabilityPremiumAwardTT[4]
                + TotalCount_CouncilDisabilityPremiumAwardTT[5]
                + TotalCount_CouncilDisabilityPremiumAwardTT[6]
                + TotalCount_CouncilDisabilityPremiumAwardTT[7]
                + TotalCount_CouncilDisabilityPremiumAwardTT[8]
                + TotalCount_CouncilDisabilityPremiumAwardTT[9];
        summary.put(
                sTotalCount_CouncilDisabilityPremiumAward,
                Integer.toString(t));
        // SevereDisabilityPremiumAward
        t = TotalCount_CouncilSevereDisabilityPremiumAwardTT[1]
                + TotalCount_CouncilSevereDisabilityPremiumAwardTT[2]
                + TotalCount_CouncilSevereDisabilityPremiumAwardTT[3]
                + TotalCount_CouncilSevereDisabilityPremiumAwardTT[4]
                + TotalCount_CouncilSevereDisabilityPremiumAwardTT[5]
                + TotalCount_CouncilSevereDisabilityPremiumAwardTT[6]
                + TotalCount_CouncilSevereDisabilityPremiumAwardTT[7]
                + TotalCount_CouncilSevereDisabilityPremiumAwardTT[8]
                + TotalCount_CouncilSevereDisabilityPremiumAwardTT[9];
        summary.put(
                sTotalCount_CouncilSevereDisabilityPremiumAward,
                Integer.toString(t));
        // DisabledChildPremiumAward
        t = TotalCount_CouncilDisabledChildPremiumAwardTT[1]
                + TotalCount_CouncilDisabledChildPremiumAwardTT[2]
                + TotalCount_CouncilDisabledChildPremiumAwardTT[3]
                + TotalCount_CouncilDisabledChildPremiumAwardTT[4]
                + TotalCount_CouncilDisabledChildPremiumAwardTT[5]
                + TotalCount_CouncilDisabledChildPremiumAwardTT[6]
                + TotalCount_CouncilDisabledChildPremiumAwardTT[7]
                + TotalCount_CouncilDisabledChildPremiumAwardTT[8]
                + TotalCount_CouncilDisabledChildPremiumAwardTT[9];
        summary.put(
                sTotalCount_CouncilDisabledChildPremiumAward,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAward
        t = TotalCount_CouncilEnhancedDisabilityPremiumAwardTT[1]
                + TotalCount_CouncilEnhancedDisabilityPremiumAwardTT[2]
                + TotalCount_CouncilEnhancedDisabilityPremiumAwardTT[3]
                + TotalCount_CouncilEnhancedDisabilityPremiumAwardTT[4]
                + TotalCount_CouncilEnhancedDisabilityPremiumAwardTT[5]
                + TotalCount_CouncilEnhancedDisabilityPremiumAwardTT[6]
                + TotalCount_CouncilEnhancedDisabilityPremiumAwardTT[7]
                + TotalCount_CouncilEnhancedDisabilityPremiumAwardTT[8]
                + TotalCount_CouncilEnhancedDisabilityPremiumAwardTT[9];
        summary.put(
                sTotalCount_CouncilEnhancedDisabilityPremiumAward,
                Integer.toString(t));
        // RSL
        // DisabilityAward
        t = TotalCount_RSLDisabilityAwardTT[1]
                + TotalCount_RSLDisabilityAwardTT[2]
                + TotalCount_RSLDisabilityAwardTT[3]
                + TotalCount_RSLDisabilityAwardTT[4]
                + TotalCount_RSLDisabilityAwardTT[5]
                + TotalCount_RSLDisabilityAwardTT[6]
                + TotalCount_RSLDisabilityAwardTT[7]
                + TotalCount_RSLDisabilityAwardTT[8]
                + TotalCount_RSLDisabilityAwardTT[9];
        summary.put(sTotalCount_RSLDisabilityAward,
                Integer.toString(t));
        // DisabilityPremiumAward
        t = TotalCount_RSLDisabilityPremiumAwardTT[1]
                + TotalCount_RSLDisabilityPremiumAwardTT[2]
                + TotalCount_RSLDisabilityPremiumAwardTT[3]
                + TotalCount_RSLDisabilityPremiumAwardTT[4]
                + TotalCount_RSLDisabilityPremiumAwardTT[5]
                + TotalCount_RSLDisabilityPremiumAwardTT[6]
                + TotalCount_RSLDisabilityPremiumAwardTT[7]
                + TotalCount_RSLDisabilityPremiumAwardTT[8]
                + TotalCount_RSLDisabilityPremiumAwardTT[9];
        summary.put(sTotalCount_RSLDisabilityPremiumAward,
                Integer.toString(t));
        // SevereDisabilityPremiumAward
        t = TotalCount_RSLSevereDisabilityPremiumAwardTT[1]
                + TotalCount_RSLSevereDisabilityPremiumAwardTT[2]
                + TotalCount_RSLSevereDisabilityPremiumAwardTT[3]
                + TotalCount_RSLSevereDisabilityPremiumAwardTT[4]
                + TotalCount_RSLSevereDisabilityPremiumAwardTT[5]
                + TotalCount_RSLSevereDisabilityPremiumAwardTT[6]
                + TotalCount_RSLSevereDisabilityPremiumAwardTT[7]
                + TotalCount_RSLSevereDisabilityPremiumAwardTT[8]
                + TotalCount_RSLSevereDisabilityPremiumAwardTT[9];
        summary.put(sTotalCount_RSLSevereDisabilityPremiumAward,
                Integer.toString(t));
        // DisabledChildPremiumAward
        t = TotalCount_RSLDisabledChildPremiumAwardTT[1]
                + TotalCount_RSLDisabledChildPremiumAwardTT[2]
                + TotalCount_RSLDisabledChildPremiumAwardTT[3]
                + TotalCount_RSLDisabledChildPremiumAwardTT[4]
                + TotalCount_RSLDisabledChildPremiumAwardTT[5]
                + TotalCount_RSLDisabledChildPremiumAwardTT[6]
                + TotalCount_RSLDisabledChildPremiumAwardTT[7]
                + TotalCount_RSLDisabledChildPremiumAwardTT[8]
                + TotalCount_RSLDisabledChildPremiumAwardTT[9];
        summary.put(sTotalCount_RSLDisabledChildPremiumAward,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAward
        t = TotalCount_RSLEnhancedDisabilityPremiumAwardTT[1]
                + TotalCount_RSLEnhancedDisabilityPremiumAwardTT[2]
                + TotalCount_RSLEnhancedDisabilityPremiumAwardTT[3]
                + TotalCount_RSLEnhancedDisabilityPremiumAwardTT[4]
                + TotalCount_RSLEnhancedDisabilityPremiumAwardTT[5]
                + TotalCount_RSLEnhancedDisabilityPremiumAwardTT[6]
                + TotalCount_RSLEnhancedDisabilityPremiumAwardTT[7]
                + TotalCount_RSLEnhancedDisabilityPremiumAwardTT[8]
                + TotalCount_RSLEnhancedDisabilityPremiumAwardTT[9];
        summary.put(sTotalCount_RSLEnhancedDisabilityPremiumAward,
                Integer.toString(t));
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
        d = CouncilCount1;
        // DisabilityAward
        if (d > 0) {
            n = Double.valueOf(summary.get(sTotalCount_CouncilDisabilityAward));
            percentage = (n * 100.0d) / d;
            summary.put(
                    sPercentageOfCouncilCount1_CouncilDisabilityAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAward
        if (d > 0) {
            n = Double.valueOf(summary.get(sTotalCount_CouncilDisabilityPremiumAward));
            percentage = (n * 100.0d) / d;
            summary.put(
                    sPercentageOfCouncilCount1_CouncilDisabilityPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereSevereDisabilityPremiumAward
        if (d > 0) {
            n = Double.valueOf(summary.get(sTotalCount_CouncilSevereDisabilityPremiumAward));
            percentage = (n * 100.0d) / d;
            summary.put(
                    sPercentageOfCouncilCount1_CouncilSevereDisabilityPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAward
        if (d > 0) {
            n = Double.valueOf(summary.get(sTotalCount_CouncilDisabledChildPremiumAward));
            percentage = (n * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount1_CouncilDisabledChildPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAward
        if (d > 0) {
            n = Double.valueOf(summary.get(sTotalCount_CouncilEnhancedDisabilityPremiumAward));
            percentage = (n * 100.0d) / d;
            summary.put(
                    sPercentageOfCouncilCount1_CouncilEnhancedDisabilityPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // RSL
        d = RSLCount1;
        // DisabilityAward
        if (d > 0) {
            n = Double.valueOf(summary.get(sTotalCount_RSLDisabilityAward));
            percentage = (n * 100.0d) / d;
            summary.put(
                    sPercentageOfRSLCount1_RSLDisabilityAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabilityPremiumAward
        if (d > 0) {
            n = Double.valueOf(summary.get(sTotalCount_RSLDisabilityPremiumAward));
            percentage = (n * 100.0d) / d;
            summary.put(
                    sPercentageOfRSLCount1_RSLDisabilityPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // SevereSevereDisabilityPremiumAward
        if (d > 0) {
            n = Double.valueOf(summary.get(sTotalCount_RSLSevereDisabilityPremiumAward));
            percentage = (n * 100.0d) / d;
            summary.put(
                    sPercentageOfRSLCount1_RSLSevereDisabilityPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // DisabledChildPremiumAward
        if (d > 0) {
            n = Double.valueOf(summary.get(sTotalCount_RSLDisabledChildPremiumAward));
            percentage = (n * 100.0d) / d;
            summary.put(sPercentageOfRSLCount1_RSLDisabledChildPremiumAward,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // EnhancedDisabilityPremiumAward
        if (d > 0) {
            n = Double.valueOf(summary.get(sTotalCount_RSLEnhancedDisabilityPremiumAward));
            percentage = (n * 100.0d) / d;
            summary.put(
                    sPercentageOfRSLCount1_RSLEnhancedDisabilityPremiumAward,
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
        // Income
        // PTI
        summary.put(sTotal_CouncilHBPTIIncome,
                BigDecimal.valueOf(CouncilPTITotalIncome).toPlainString());
        summary.put(sTotalCount_CouncilHBPTIIncomeNonZero,
                BigDecimal.valueOf(TotalCount_CouncilPTITotalIncomeNonZero).toPlainString());
        summary.put(sTotalCount_CouncilHBPTIIncomeZero,
                BigDecimal.valueOf(TotalCount_CouncilPTITotalIncomeZero).toPlainString());
        // PTS
        summary.put(sTotal_CouncilHBPTSIncome,
                BigDecimal.valueOf(CouncilPTSTotalIncome).toPlainString());
        summary.put(sTotalCount_CouncilHBPTSIncomeNonZero,
                BigDecimal.valueOf(TotalCount_CouncilPTSTotalIncomeNonZero).toPlainString());
        summary.put(sTotalCount_CouncilHBPTSIncomeZero,
                BigDecimal.valueOf(TotalCount_CouncilPTSTotalIncomeZero).toPlainString());
        // PTO
        summary.put(sTotal_CouncilHBPTOIncome,
                BigDecimal.valueOf(CouncilPTOTotalIncome).toPlainString());
        summary.put(sTotalCount_CouncilHBPTOIncomeNonZero,
                BigDecimal.valueOf(TotalCount_CouncilPTOTotalIncomeNonZero).toPlainString());
        summary.put(sTotalCount_CouncilHBPTOIncomeZero,
                BigDecimal.valueOf(TotalCount_CouncilPTOTotalIncomeZero).toPlainString());
        // WeeklyHBEntitlement
        summary.put(sTotal_CouncilWeeklyHBEntitlement,
                BigDecimal.valueOf(CouncilTotalWeeklyHBEntitlement).toPlainString());
        summary.put(
                sTotalCount_CouncilWeeklyHBEntitlementNonZero,
                Integer.toString(TotalCount_CouncilWeeklyHBEntitlementNonZero));
        summary.put(
                sTotalCount_CouncilWeeklyHBEntitlementZero,
                Integer.toString(TotalCount_CouncilWeeklyHBEntitlementZero));
        // WeeklyCTBEntitlement
        summary.put(sTotal_CouncilWeeklyCTBEntitlement,
                BigDecimal.valueOf(CouncilTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(
                sTotalCount_CouncilWeeklyCTBEntitlementNonZero,
                Integer.toString(TotalCount_CouncilWeeklyCTBEntitlementNonZero));
        summary.put(sTotalCount_CouncilWeeklyCTBEntitlementZero,
                Integer.toString(TotalCount_CouncilWeeklyCTBEntitlementZero));
        // WeeklyEligibleRentAmount
        summary.put(sTotal_CouncilWeeklyEligibleRentAmount,
                BigDecimal.valueOf(CouncilTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(sTotalCount_CouncilWeeklyEligibleRentAmountNonZero,
                Integer.toString(TotalCount_CouncilTotalWeeklyEligibleRentAmountNonZero));
        summary.put(sTotalCount_CouncilWeeklyEligibleRentAmountZero,
                Integer.toString(TotalCount_CouncilTotalWeeklyEligibleRentAmountZero));
        // WeeklyEligibleCouncilTaxAmount
        summary.put(sTotal_CouncilWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(CouncilTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(
                sTotalCount_CouncilWeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(TotalCount_CouncilWeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(
                sTotalCount_CouncilWeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(TotalCount_CouncilWeeklyEligibleCouncilTaxAmountZero));
        // ContractualRentAmount
        summary.put(sTotal_CouncilContractualRentAmount,
                BigDecimal.valueOf(CouncilTotalContractualRentAmount).toPlainString());
        summary.put(sTotalCount_CouncilContractualRentAmountNonZero,
                Integer.toString(TotalCount_CouncilTotalContractualRentAmountNonZero));
        summary.put(sTotalCount_CouncilContractualRentAmountZero,
                Integer.toString(TotalCount_CouncilTotalContractualRentAmountZero));
        // WeeklyAdditionalDiscretionaryPayment
        summary.put(sTotal_CouncilWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(CouncilTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(sTotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(TotalCount_CouncilTotalWeeklyAdditionalDiscretionaryPaymentNonZero));
        summary.put(sTotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(TotalCount_CouncilTotalWeeklyAdditionalDiscretionaryPaymentZero));
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        summary.put(sTotal_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(sTotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(TotalCount_CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero));
        summary.put(sTotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(TotalCount_CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero));
        // Employed
        summary.put(sTotalCount_CouncilClaimantsEmployed,
                Integer.toString(TotalCount_CouncilEmployedClaimants));
        // Self Employed
        summary.put(sTotalCount_CouncilClaimantsSelfEmployed,
                Integer.toString(TotalCount_CouncilSelfEmployedClaimants));
        // Students
        summary.put(sTotalCount_CouncilClaimantsStudents,
                Integer.toString(TotalCount_CouncilStudentsClaimants));
        // LHACases
        summary.put(
                sTotalCount_CouncilLHACases,
                Integer.toString(TotalCount_CouncilLHACases));
        // RSL
        // Income
        summary.put(sTotal_RSLHBPTIIncome,
                BigDecimal.valueOf(RSLPTITotalIncome).toPlainString());
        summary.put(sTotal_RSLHBPTSIncome,
                BigDecimal.valueOf(RSLPTSTotalIncome).toPlainString());
        summary.put(sTotal_RSLHBPTOIncome,
                BigDecimal.valueOf(RSLPTOTotalIncome).toPlainString());
        summary.put(sTotalCount_RSLHBPTIIncomeNonZero,
                BigDecimal.valueOf(TotalCount_RSLHBPTITotalIncomeNonZero).toPlainString());
        // WeeklyHBEntitlement
        summary.put(sTotal_RSLWeeklyHBEntitlement,
                BigDecimal.valueOf(RSLTotalWeeklyHBEntitlement).toPlainString());
        summary.put(sTotalCount_RSLWeeklyHBEntitlementNonZero,
                Integer.toString(TotalCount_RSLWeeklyHBEntitlementNonZero));
        summary.put(sTotalCount_RSLWeeklyHBEntitlementZero,
                Integer.toString(TotalCount_RSLWeeklyHBEntitlementZero));
        // WeeklyCTBEntitlement
        summary.put(sTotal_RSLWeeklyCTBEntitlement,
                BigDecimal.valueOf(RSLTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(
                sTotalCount_RSLWeeklyCTBEntitlementNonZero,
                Integer.toString(TotalCount_RSLWeeklyCTBEntitlementNonZero));
        summary.put(
                sTotalCount_RSLWeeklyCTBEntitlementZero,
                Integer.toString(RSLTotalWeeklyCTBEntitlementZeroCount));
        // WeeklyEligibleRentAmount
        summary.put(sTotal_RSLWeeklyEligibleRentAmount,
                BigDecimal.valueOf(RSLTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                sTotalCount_RSLWeeklyEligibleRentAmountNonZero,
                Integer.toString(RSLTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(
                sTotalCount_RSLWeeklyEligibleRentAmountZero,
                Integer.toString(RSLTotalWeeklyEligibleRentAmountZeroCount));
        // WeeklyEligibleCouncilTaxAmount
        summary.put(sTotal_RSLWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(RSLTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(
                sTotalCount_RSLWeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(TotalCount_RSLWeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(
                sTotalCount_RSLWeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(TotalCount_RSLWeeklyEligibleCouncilTaxAmountZero));
        // ContractualRentAmount
        summary.put(sTotal_RSLContractualRentAmount,
                BigDecimal.valueOf(RSLTotalContractualRentAmount).toPlainString());
        summary.put(sTotalCount_RSLContractualRentAmountNonZero,
                Integer.toString(RSLTotalContractualRentAmountNonZeroCount));
        summary.put(sTotalCount_ContractualRentAmountZero,
                Integer.toString(RSLTotalContractualRentAmountZeroCount));
        // WeeklyAdditionalDiscretionaryPayment
        summary.put(sTotal_RSLWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(RSLTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sTotalCount_RSLWeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(RSLTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sTotalCount_RSLWeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(RSLTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        summary.put(sTotal_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sTotalCount_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sTotalCount_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        // Employed
        summary.put(
                sTotalCount_RSLClaimantsEmployed,
                Integer.toString(TotalCount_RSLEmployedClaimants));
        // Self Employed
        summary.put(sTotalCount_RSLClaimantsSelfEmployed,
                Integer.toString(TotalCount_RSLSelfEmployedClaimants));
        // Students
        summary.put(sTotalCount_RSLClaimantsStudents,
                Integer.toString(TotalCount_RSLStudentsClaimants));
        // LHACases
        summary.put(
                sTotalCount_RSLLHACases,
                Integer.toString(TotalCount_HBLHACases));
    }

    @Override
    protected void addToSummarySingleTimeRates1(
            HashMap<String, String> summary) {
        super.addToSummarySingleTimeRates1(summary);
        double ave;
        double d;
        double t;
        // Council
        // Income
        // PTI
        t = Double.valueOf(summary.get(sTotal_CouncilHBPTIIncome));
        d = TotalCount_CouncilPTITotalIncomeNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_NonZero_CouncilHBPTIIncome,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_CouncilHBPTIIncome,
                    s0);
        }
        // PTS
        t = Double.valueOf(summary.get(sTotal_CouncilHBPTSIncome));
        d = TotalCount_CouncilPTSTotalIncomeNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_NonZero_CouncilHBPTSIncome,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_CouncilHBPTSIncome,
                    s0);
        }
        // PTO
        t = Double.valueOf(summary.get(sTotal_CouncilHBPTOIncome));
        d = TotalCount_CouncilPTOTotalIncomeNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_NonZero_CouncilHBPTOIncome,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_CouncilHBPTOIncome,
                    s0);
        }
        // WeeklyHBEntitlement
        t = Double.valueOf(summary.get(sTotal_CouncilWeeklyHBEntitlement));
        d = TotalCount_CouncilWeeklyHBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_NonZero_CouncilWeeklyHBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_CouncilWeeklyHBEntitlement,
                    s0);
        }
        // WeeklyCTBEntitlement
        t = Double.valueOf(summary.get(sTotal_CouncilWeeklyCTBEntitlement));
        d = TotalCount_CouncilWeeklyCTBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_Non_Zero_CouncilWeeklyCTBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_Non_Zero_CouncilWeeklyCTBEntitlement,
                    s0);
        }
        // WeeklyEligibleRentAmount
        t = Double.valueOf(summary.get(sTotal_CouncilWeeklyEligibleRentAmount));
        d = TotalCount_CouncilTotalWeeklyEligibleRentAmountNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_NonZero_CouncilAverageWeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_CouncilAverageWeeklyEligibleRentAmount,
                    s0);
        }
        // WeeklyEligibleCouncilTaxAmount
        t = Double.valueOf(summary.get(sTotal_CouncilWeeklyEligibleCouncilTaxAmount));
        d = TotalCount_CouncilWeeklyEligibleCouncilTaxAmountNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_NonZero_CouncilWeeklyEligibleCouncilTaxAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_CouncilWeeklyEligibleCouncilTaxAmount,
                    s0);
        }
        // ContractualRentAmount
        t = Double.valueOf(summary.get(sTotal_CouncilContractualRentAmount));
        d = TotalCount_CouncilTotalContractualRentAmountNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_NonZero_CouncilContractualRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_CouncilContractualRentAmount,
                    s0);
        }
        // WeeklyAdditionalDiscretionaryPayment
        t = Double.valueOf(summary.get(sTotal_CouncilWeeklyAdditionalDiscretionaryPayment));
        d = TotalCount_CouncilTotalWeeklyAdditionalDiscretionaryPaymentNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_NonZero_CouncilWeeklyAdditionalDiscretionaryPayment,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_CouncilWeeklyAdditionalDiscretionaryPayment,
                    s0);
        }
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        t = Double.valueOf(summary.get(sTotal_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability));
        d = TotalCount_CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_NonZero_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    s0);
        }
        // Employed
        t = Integer.valueOf(summary.get(sTotalCount_CouncilClaimantsEmployed));
        d = CouncilCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount1_CouncilClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sPercentageOfCouncilCount1_CouncilClaimantsEmployed,
                    s0);
        }
        // Self Employed
        t = Integer.valueOf(summary.get(sTotalCount_CouncilClaimantsSelfEmployed));
        d = CouncilCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount1_CouncilClaimantsSelfEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sPercentageOfCouncilCount1_CouncilClaimantsSelfEmployed,
                    s0);
        }
        // Students
        t = Integer.valueOf(summary.get(sTotalCount_CouncilClaimantsStudents));
        d = CouncilCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount1_CouncilClaimantsStudents,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sPercentageOfCouncilCount1_CouncilClaimantsStudents,
                    s0);
        }
        // LHACases
        t = Integer.valueOf(summary.get(sTotalCount_CouncilLHACases));
        d = CouncilCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(sPercentageOfCouncilCount1_CouncilLHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sPercentageOfCouncilCount1_CouncilLHACases,
                    s0);
        }
        // RSL
        // Income
        t = Double.valueOf(summary.get(sTotal_RSLHBPTIIncome));
        d = TotalCount_RSLHBPTITotalIncomeNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_NonZero_RSLIncome,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_RSLIncome,
                    s0);
        }
        // WeeklyHBEntitlement
        t = Double.valueOf(summary.get(sTotal_RSLWeeklyHBEntitlement));
        d = TotalCount_RSLWeeklyHBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_NonZero_RSLWeeklyHBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_RSLWeeklyHBEntitlement,
                    s0);
        }
        // WeeklyCTBEntitlement
        t = Double.valueOf(summary.get(sTotal_RSLWeeklyCTBEntitlement));
        d = TotalCount_RSLWeeklyCTBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_NonZero_RSLWeeklyCTBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_RSLWeeklyCTBEntitlement,
                    s0);
        }
        // WeeklyEligibleRentAmount
        t = Double.valueOf(summary.get(sTotal_RSLWeeklyEligibleRentAmount));
        d = RSLTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_NonZero_RSLWeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_RSLWeeklyEligibleRentAmount,
                    s0);
        }
        // WeeklyEligibleCouncilTaxAmount
        t = Double.valueOf(summary.get(sTotal_RSLWeeklyEligibleCouncilTaxAmount));
        d = TotalCount_RSLWeeklyEligibleCouncilTaxAmountNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_NonZero_RSLWeeklyEligibleCouncilTaxAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_RSLWeeklyEligibleCouncilTaxAmount,
                    s0);
        }
        // ContractualRentAmount
        t = Double.valueOf(summary.get(sTotal_RSLContractualRentAmount));
        d = RSLTotalContractualRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_NonZero_RSLContractualRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_RSLContractualRentAmount,
                    s0);
        }
        // WeeklyAdditionalDiscretionaryPayment
        t = Double.valueOf(summary.get(sTotal_RSLWeeklyAdditionalDiscretionaryPayment));
        d = RSLTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_NonZero_RSLWeeklyAdditionalDiscretionaryPayment,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_RSLWeeklyAdditionalDiscretionaryPayment,
                    s0);
        }
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        t = Double.valueOf(summary.get(sTotal_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability));
        d = RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(sAverage_NonZero_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sAverage_NonZero_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                    s0);
        }
        // Employed
        t = Integer.valueOf(summary.get(sTotalCount_RSLClaimantsEmployed));
        d = RSLCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfRSLCount1_RSLClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentageOfRSLCount1_RSLClaimantsEmployed,
                    s0);
        }
        // Self Employed
        t = Integer.valueOf(summary.get(sTotalCount_RSLClaimantsSelfEmployed));
        d = RSLCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfRSLCount1_RSLClaimantsSelfEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentageOfRSLCount1_RSLClaimantsSelfEmployed,
                    s0);
        }
        // Students
        t = Integer.valueOf(summary.get(sTotalCount_RSLClaimantsStudents));
        d = RSLCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(sPercentageOfRSLCount1_RSLClaimantsStudents,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sPercentageOfRSLCount1_RSLClaimantsStudents,
                    s0);
        }
        // LHACases
        t = Integer.valueOf(summary.get(sTotalCount_RSLLHACases));
        d = RSLCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(sPercentageOfRSLCount1_RSLLHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sPercentageOfRSLCount1_RSLLHACases,
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
                    sTotalCount_CouncilEthnicGroupClaimant[i],
                    Integer.toString(CouncilEthnicGroupCount[i]));
        }
        // RSL
        for (int i = 1; i < nEG; i++) {
            summary.put(sTotalCount_RSLEthnicGroupClaimant[i],
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
            all = Integer.valueOf(summary.get(sTotalCount_CouncilEthnicGroupClaimant[i]));
            d = CouncilCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(sPercentageOfCouncilCount1_CouncilEthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
        }
        // RSL
        for (int i = 1; i < nEG; i++) {
            all = Integer.valueOf(summary.get(sTotalCount_RSLEthnicGroupClaimant[i]));
            d = RSLCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(sPercentageOfRSLCount1_RSLEthnicGroupClaimant[i],
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
            all = TotalCount_CouncilTTClaimant1[i];
            summary.put(
                    sTotalCount_CouncilClaimantTT[i],
                    Integer.toString(all));
        }
        summary.put(sTotalCount_CouncilPostcodeValidFormat,
                Integer.toString(TotalCount_CouncilPostcodeValidFormat));
        summary.put(sTotalCount_CouncilPostcodeValid,
                Integer.toString(TotalCount_CouncilPostcodeValid));
        double d;
        double n;
        d = CouncilCount1;
        if (d > 0) {
            n = (TotalCount_CouncilPostcodeValidFormat / d) * 100.0d;
            summary.put(sPercentageOfCouncilCount1_CouncilPostcodeValidFormat,
                    Double.toString(n));
            n = (TotalCount_CouncilPostcodeValid / d) * 100.0d;
            summary.put(sPercentageOfCouncilCount1_CouncilPostcodeValid,
                    Double.toString(n));
        }
        // RSL
        // TT
        for (int i = 1; i < nTT; i++) {
            all = TotalCount_RSLTTClaimant1[i];
            summary.put(sTotalCount_RSLClaimantTT[i],
                    Integer.toString(all));
        }
        summary.put(sTotalCount_RSLPostcodeValidFormat,
                Integer.toString(TotalCount_RSLPostcodeFValidFormat));
        summary.put(sTotalCount_RSLPostcodeValid,
                Integer.toString(TotalCount_RSLPostcodeFMappable));
        d = RSLCount1;
        if (d > 0) {
            n = (TotalCount_RSLPostcodeFValidFormat / d) * 100.0d;
            summary.put(sPercentageOfRSLCount1_RSLPostcodeValidFormat,
                    Double.toString(n));
            n = (TotalCount_RSLPostcodeFMappable / d) * 100.0d;
            summary.put(sPercentageOfRSLCount1_RSLPostcodeValid,
                    Double.toString(n));
        }
    }

    /**
     * Distinguish between those claims that are Housing Benefit (HB) claims in
     * both periods, those that are only in one, and those that are in none
     * (those in none are claims that are new claims extracted in the Under
     * Occupying data, but which were not in the SHBE at the time it was
     * extracted). Claims that are HB claims in only one period may be Council
     * Tax Support claims in the other. For claims in at least one UO data, Rent
     * Arrears are used.
     *
     * @param ClaimID The ClaimID of the claim to be processed.
     * @param Group
     * @param DW_UO_Set0CouncilMap Keys are ClaimIDs values are Council
     * DW_UO_Record for the former time period.
     * @param DW_UO_Set1CouncilMap Keys are ClaimIDs values are Council
     * DW_UO_Record for the latter time period.
     * @param Records0 Keys are ClaimIDs values are SHBE_Record for the
 former time period.
     * @param Records1 Keys are ClaimIDs values are SHBE_Record for the
 latter time period.
     */
    protected void doCouncilCompare2TimesCounts(
            SHBE_ID ClaimID,
            HashSet<SHBE_ID> Group,
            HashMap<SHBE_ID, SHBE_Record> Records0,
            HashMap<SHBE_ID, DW_UO_Record> DW_UO_Set0CouncilMap,
            HashMap<SHBE_ID, SHBE_Record> Records1,
            HashMap<SHBE_ID, DW_UO_Record> DW_UO_Set1CouncilMap
    ) {
        /**
         * Initialise: Record0, Record1, DRecord0, DRecord1, DW_UO_Record0,
         * DW_UO_Record1.
         */
        SHBE_Record Record0;
        SHBE_Record Record1;
        SHBE_D_Record DRecord0;
        SHBE_D_Record DRecord1;
        DW_UO_Record DW_UO_Record0;
        DW_UO_Record DW_UO_Record1;
        Record0 = Records0.get(ClaimID);
        Record1 = Records1.get(ClaimID);
        DRecord0 = null;
        if (Record0 != null) {
            DRecord0 = Record0.getDRecord();
        }
        DRecord1 = null;
        if (Record1 != null) {
            DRecord1 = Record1.getDRecord();
        }
        DW_UO_Record0 = DW_UO_Set0CouncilMap.get(ClaimID);
        DW_UO_Record1 = DW_UO_Set1CouncilMap.get(ClaimID);
        // Call super.
        super.doCompare2TimesCounts(Record0, DRecord0, Record1, DRecord1);
        /**
         * Initialise: isHBClaim0, isHBClaim1, isCTBOnlyClaim0, isCTBOnlyClaim1,
         * TT0, TT1.
         */
        boolean isHBClaim0;
        boolean isHBClaim1;
        isHBClaim0 = false;
        isHBClaim1 = false;
        boolean isCTBOnlyClaim0;
        boolean isCTBOnlyClaim1;
        isCTBOnlyClaim1 = false;
        isCTBOnlyClaim0 = false;
        int TT0;
        int TT1;
        TT0 = SHBE_TenancyType_Handler.iMinus999;
        if (DRecord0 != null) {
            isHBClaim0 = SHBE_Handler.isHBClaim(DRecord0);
            isCTBOnlyClaim0 = SHBE_Handler.isCTBOnlyClaim(DRecord0);
            TT0 = DRecord0.getTenancyType();
        }
        TT1 = SHBE_TenancyType_Handler.iMinus999;
        if (DRecord1 != null) {
            isHBClaim1 = SHBE_Handler.isHBClaim(DRecord1);
            isCTBOnlyClaim1 = SHBE_Handler.isCTBOnlyClaim(DRecord1);
            TT1 = DRecord1.getTenancyType();
        }
        // Deal with different cases.
        if (isHBClaim1) {
            if (Record0 != null && Record1 != null) {
                // Initialise: RentArrears0, RentArrears1
                Double RentArrears0 = null;
                Double RentArrears1 = null;
                if (DW_UO_Record0 != null) {
                    RentArrears0 = DW_UO_Record0.getTotalRentArrears();
                }
                if (DW_UO_Record1 != null) {
                    RentArrears1 = DW_UO_Record1.getTotalRentArrears();
                }
                if (Record1 == null) {
                    if (isHBClaim0) {
                        // Case when Record1 == null and isHBClaim0.
                        doCouncilCompare2TimesHBCount(
                                ClaimID,
                                Group,
                                RentArrears0,
                                TT0,
                                Record0.getPostcodeID(),
                                Record0.isClaimPostcodeFValidFormat(),
                                Record0.isClaimPostcodeFMappable(),
                                RentArrears1,
                                TT1,
                                null,
                                false,
                                false);
                    } else if (Record0 == null) {
                        // Case when Record1 == null and Record0 == null.
                        doCouncilCompare2TimesHBCount(
                                ClaimID,
                                Group,
                                RentArrears0,
                                TT0,
                                null,
                                false,
                                false,
                                RentArrears1,
                                TT1,
                                null,
                                false,
                                false);
                    }
                } else if (Record0 == null) {
                    // Case when Record1 != null and Record0 == null.
                    doCouncilCompare2TimesHBCount(
                            ClaimID,
                            Group,
                            RentArrears0,
                            TT0,
                            null,
                            false,
                            false,
                            RentArrears1,
                            TT1,
                            Record1.getPostcodeID(),
                            Record1.isClaimPostcodeFValidFormat(),
                            Record1.isClaimPostcodeFMappable());
                } else if (isHBClaim0) {
                    // Case when Record1 != null and isHBClaim0.
                    doCouncilCompare2TimesHBCount(
                            ClaimID,
                            Group,
                            RentArrears0,
                            TT0,
                            Record0.getPostcodeID(),
                            Record0.isClaimPostcodeFValidFormat(),
                            Record0.isClaimPostcodeFMappable(),
                            RentArrears1,
                            TT1,
                            Record1.getPostcodeID(),
                            Record1.isClaimPostcodeFValidFormat(),
                            Record1.isClaimPostcodeFMappable());
                }
            }
        }
        if (isCTBOnlyClaim1) {
            // Ignore any Rent Arrears for now if there were any in the former period.
            if (Record0 != null && Record1 != null) {
                if (Record1 == null) {
                    if (isCTBOnlyClaim0) {
                        doCouncilCompare2TimesCTBCount(
                                TT0,
                                Record0.getPostcodeID(),
                                Record0.isClaimPostcodeFValidFormat(),
                                Record0.isClaimPostcodeFMappable(),
                                TT1,
                                null,
                                false,
                                false);
                    } else if (Record0 == null) {
                        doCouncilCompare2TimesCTBCount(
                                TT0,
                                null,
                                false,
                                false,
                                TT1,
                                null,
                                false,
                                false);
                    }
                } else if (Record0 == null) {
                    doCouncilCompare2TimesCTBCount(
                            TT0,
                            null,
                            false,
                            false,
                            TT1,
                            Record1.getPostcodeID(),
                            Record1.isClaimPostcodeFValidFormat(),
                            Record1.isClaimPostcodeFMappable());
                } else if (isCTBOnlyClaim0) {
                    doCouncilCompare2TimesCTBCount(
                            TT0,
                            Record0.getPostcodeID(),
                            Record0.isClaimPostcodeFValidFormat(),
                            Record0.isClaimPostcodeFMappable(),
                            TT1,
                            Record1.getPostcodeID(),
                            Record1.isClaimPostcodeFValidFormat(),
                            Record1.isClaimPostcodeFMappable());
                }
            }
        }
    }

    protected void doRSLCompare2TimesCounts(
            SHBE_Record Record0,
            SHBE_D_Record D_Record0,
            SHBE_Record Record1,
            SHBE_D_Record D_Record1) {
        super.doCompare2TimesCounts(Record0, D_Record0, Record1, D_Record1);
        boolean isHBClaim1;
        isHBClaim1 = false;
        boolean isCTBOnlyClaim1;
        isCTBOnlyClaim1 = false;
        int TT1;
        TT1 = SHBE_TenancyType_Handler.iMinus999;
        if (D_Record1 != null) {
            isHBClaim1 = SHBE_Handler.isHBClaim(D_Record1);
            isCTBOnlyClaim1 = SHBE_Handler.isCTBOnlyClaim(D_Record1);
            TT1 = D_Record1.getTenancyType();
        }
//        CouncilAllCount1 = CouncilCount1 + CouncilCTBCount1;
//        RSLCount1 = RSLCount1 + RSLCTBCount1;
        boolean isHBClaim0;
        isHBClaim0 = false;
        boolean isCTBOnlyClaim0;
        isCTBOnlyClaim0 = false;
        int TT0;
        TT0 = SHBE_TenancyType_Handler.iMinus999;
        if (D_Record0 != null) {
            isHBClaim0 = SHBE_Handler.isHBClaim(D_Record0);
            isCTBOnlyClaim0 = SHBE_Handler.isCTBOnlyClaim(D_Record0);
            TT0 = D_Record0.getTenancyType();
        }
        if (isHBClaim1 || D_Record1 == null) {
            if (Record0 != null && Record1 != null) {
                if (Record1 == null) {
                    if (isHBClaim0) {
                        doRSLCompare2TimesHBCount(
                                TT0,
                                Record0.getPostcodeID(),
                                Record0.isClaimPostcodeFValidFormat(),
                                Record0.isClaimPostcodeFMappable(),
                                TT1,
                                null,
                                false,
                                false);
                    } else if (Record0 == null) {
                        doRSLCompare2TimesHBCount(
                                TT0,
                                null,
                                false,
                                false,
                                TT1,
                                null,
                                false,
                                false);
                    }
                } else if (Record0 == null) {
                    doRSLCompare2TimesHBCount(
                            TT0,
                            null,
                            false,
                            false,
                            TT1,
                            Record1.getPostcodeID(),
                            Record1.isClaimPostcodeFValidFormat(),
                            Record1.isClaimPostcodeFMappable());
                } else if (isHBClaim0) {
                    doRSLCompare2TimesHBCount(
                            TT0,
                            Record0.getPostcodeID(),
                            Record0.isClaimPostcodeFValidFormat(),
                            Record0.isClaimPostcodeFMappable(),
                            TT1,
                            Record1.getPostcodeID(),
                            Record1.isClaimPostcodeFValidFormat(),
                            Record1.isClaimPostcodeFMappable());
                }
            }
        }
        if (isCTBOnlyClaim1 || D_Record1 == null) {
            if (Record0 != null && Record1 != null) {
                if (Record1 == null) {
                    if (isCTBOnlyClaim0) {
                        doRSLCompare2TimesCTBCount(
                                TT0,
                                Record0.getPostcodeID(),
                                Record0.isClaimPostcodeFValidFormat(),
                                Record0.isClaimPostcodeFMappable(),
                                TT1,
                                null,
                                false,
                                false);
                    } else if (Record0 == null) {
                        doRSLCompare2TimesCTBCount(
                                TT0,
                                null,
                                false,
                                false,
                                TT1,
                                null,
                                false,
                                false);
                    }
                } else if (Record0 == null) {
                    doRSLCompare2TimesCTBCount(
                            TT0,
                            null,
                            false,
                            false,
                            TT1,
                            Record1.getPostcodeID(),
                            Record1.isClaimPostcodeFValidFormat(),
                            Record1.isClaimPostcodeFMappable());
                } else if (isCTBOnlyClaim0) {
                    doRSLCompare2TimesCTBCount(
                            TT0,
                            Record0.getPostcodeID(),
                            Record0.isClaimPostcodeFValidFormat(),
                            Record0.isClaimPostcodeFMappable(),
                            TT1,
                            Record1.getPostcodeID(),
                            Record1.isClaimPostcodeFValidFormat(),
                            Record1.isClaimPostcodeFMappable());
                }
            }
        }
    }

    protected void doCouncilSingleTimeCount(
            SHBE_Record Record,
            SHBE_D_Record D_Record) {
        super.doSingleTimeCount(Record, D_Record);
        // Declaration
        int EG;
        int TT;
        int DisabilityPremiumAwarded;
        int SevereDisabilityPremiumAwarded;
        int DisabledChildPremiumAwarded;
        int EnhancedDisabilityPremiumAwarded;
        int PSI;
        long HouseholdSize;
        double HouseholdIncome;
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
        // Initialisation
        EG = SHBE_Handler.getEthnicityGroup(D_Record);
        TT = D_Record.getTenancyType();
        DisabilityPremiumAwarded = D_Record.getDisabilityPremiumAwarded();
        SevereDisabilityPremiumAwarded = D_Record.getSevereDisabilityPremiumAwarded();
        DisabledChildPremiumAwarded = D_Record.getDisabledChildPremiumAwarded();
        EnhancedDisabilityPremiumAwarded = D_Record.getEnhancedDisabilityPremiumAwarded();
        PSI = D_Record.getPassportedStandardIndicator();
        HouseholdSize = SHBE_Handler.getHouseholdSize(D_Record);
        HouseholdIncome = SHBE_Handler.getHouseholdIncomeTotal(Record, D_Record);
        WeeklyHousingBenefitEntitlement = D_Record.getWeeklyHousingBenefitEntitlement();
        WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
        WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
        WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
        ContractualRentAmount = D_Record.getContractualRentAmount();
        WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
        WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
        ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
        ClaimantsNetWeeklyIncomeFromSelfEmployment = D_Record.getClaimantsNetWeeklyIncomeFromSelfEmployment();
        ClaimantsStudentIndicator = D_Record.getClaimantsStudentIndicator();
        LHARegulationsApplied = D_Record.getLHARegulationsApplied();
        WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
        WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
        WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
        ContractualRentAmount = D_Record.getContractualRentAmount();
        WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
        WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
        // Counts
        CouncilCount1++;
        switch (D_Record.getStatusOfHBClaimAtExtractDate()) {
            case 1:
                CouncilHBPTICount1++;
                break;
            case 2:
                CouncilHBPTSCount1++;
                break;
            default:
                CouncilHBPTOCount1++;
                break;
        }
        switch (D_Record.getStatusOfCTBClaimAtExtractDate()) {
            case 1:
                CouncilCTBPTICount1++;
                break;
            case 2:
                CouncilCTBPTSCount1++;
                break;
            default:
                CouncilCTBPTOCount1++;
                break;
        }
        CouncilEthnicGroupCount[EG]++;
        if (Record.isClaimPostcodeFValidFormat()) {
            TotalCount_CouncilPostcodeValidFormat++;
        }
        if (Record.isClaimPostcodeFMappable()) {
            TotalCount_CouncilPostcodeValid++;
        }
        TotalCount_CouncilTTClaimant1[TT]++;
        // Disability
        if (DisabilityPremiumAwarded == 1) {
            TotalCount_CouncilDisabilityPremiumAwardTT[TT]++;
        }
        if (SevereDisabilityPremiumAwarded == 1) {
            TotalCount_CouncilSevereDisabilityPremiumAwardTT[TT]++;
        }
        if (DisabledChildPremiumAwarded == 1) {
            TotalCount_CouncilDisabledChildPremiumAwardTT[TT]++;
        }
        if (EnhancedDisabilityPremiumAwarded == 1) {
            TotalCount_CouncilEnhancedDisabilityPremiumAwardTT[TT]++;
        }
        // General Household Disability Flag
        if (DisabilityPremiumAwarded == 1
                || SevereDisabilityPremiumAwarded == 1
                || DisabledChildPremiumAwarded == 1
                || EnhancedDisabilityPremiumAwarded == 1) {
            TotalCount_CouncilDisabilityAwardTT[TT]++;
        }
        TotalCount_CouncilPSI[PSI]++;
        //if (HBRef.equalsIgnoreCase(CTBRef)) {
        CouncilTotalHouseholdSize += HouseholdSize;
        if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
            TotalCount_CouncilEmployedClaimants++;
        }
        if (ClaimantsNetWeeklyIncomeFromSelfEmployment > 0) {
            TotalCount_CouncilSelfEmployedClaimants++;
        }
        if (ClaimantsStudentIndicator != null) {
            if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                TotalCount_CouncilStudentsClaimants++;
            }
        }
        if (LHARegulationsApplied != null) {
            if (LHARegulationsApplied.equalsIgnoreCase("1")
                    || LHARegulationsApplied.equalsIgnoreCase("Yes")) {
                TotalCount_CouncilLHACases++;
            }
        }
        int StatusOfHBClaimAtExtractDate;
        StatusOfHBClaimAtExtractDate = D_Record.getStatusOfHBClaimAtExtractDate();
        if (HouseholdIncome > 0) {
            switch (StatusOfHBClaimAtExtractDate) {
                case 1:
                    CouncilPTITotalIncome += HouseholdIncome;
                    TotalCount_CouncilPTITotalIncomeNonZero++;
                    break;
                case 2:
                    CouncilPTSTotalIncome += HouseholdIncome;
                    TotalCount_CouncilPTSTotalIncomeNonZero++;
                    break;
                default:
                    CouncilPTOTotalIncome += HouseholdIncome;
                    TotalCount_CouncilPTSTotalIncomeNonZero++;
                    break;
            }
        } else {
            switch (StatusOfHBClaimAtExtractDate) {
                case 1:
                    TotalCount_CouncilPTITotalIncomeZero++;
                    break;
                case 2:
                    TotalCount_CouncilPTSTotalIncomeZero++;
                    break;
                default:
                    TotalCount_CouncilPTSTotalIncomeZero++;
                    break;
            }
        }
        if (WeeklyHousingBenefitEntitlement > 0) {
            CouncilTotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
            TotalCount_CouncilWeeklyHBEntitlementNonZero++;
        } else {
            TotalCount_CouncilWeeklyHBEntitlementZero++;
        }
        if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
            CouncilTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
            TotalCount_CouncilWeeklyCTBEntitlementNonZero++;
        } else {
            TotalCount_CouncilWeeklyCTBEntitlementZero++;
        }
        if (WeeklyEligibleRentAmount > 0) {
            CouncilTotalWeeklyEligibleRentAmount += WeeklyEligibleRentAmount;
            TotalCount_CouncilTotalWeeklyEligibleRentAmountNonZero++;
        } else {
            TotalCount_CouncilTotalWeeklyEligibleRentAmountZero++;
        }
        if (WeeklyEligibleCouncilTaxAmount > 0) {
            CouncilTotalWeeklyEligibleCouncilTaxAmount += WeeklyEligibleCouncilTaxAmount;
            TotalCount_CouncilWeeklyEligibleCouncilTaxAmountNonZero++;
        } else {
            TotalCount_CouncilWeeklyEligibleCouncilTaxAmountZero++;
        }
        if (ContractualRentAmount > 0) {
            CouncilTotalContractualRentAmount += ContractualRentAmount;
            TotalCount_CouncilTotalContractualRentAmountNonZero++;
        } else {
            TotalCount_CouncilTotalContractualRentAmountZero++;
        }
        if (WeeklyAdditionalDiscretionaryPayment > 0) {
            CouncilTotalWeeklyAdditionalDiscretionaryPayment += WeeklyAdditionalDiscretionaryPayment;
            TotalCount_CouncilTotalWeeklyAdditionalDiscretionaryPaymentNonZero++;
        } else {
            TotalCount_CouncilTotalWeeklyAdditionalDiscretionaryPaymentZero++;
        }
        if (WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability > 0) {
            CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability += WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
            TotalCount_CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero++;
        } else {
            TotalCount_CouncilTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero++;
        }
    }

    protected void doRSLSingleTimeCount(
            SHBE_Record Record,
            SHBE_D_Record D_Record) {
        super.doSingleTimeCount(Record, D_Record);
        // Declaration
        int StatusOfHBClaimAtExtractDate;
        int EG;
        int TT;
        int DisabilityPremiumAwarded;
        int SevereDisabilityPremiumAwarded;
        int DisabledChildPremiumAwarded;
        int EnhancedDisabilityPremiumAwarded;
        int PSI;
        long HouseholdSize;
        long HouseholdIncome;
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
        // Initialisation
        StatusOfHBClaimAtExtractDate = D_Record.getStatusOfHBClaimAtExtractDate();
        StatusOfHBClaimAtExtractDate = D_Record.getStatusOfCTBClaimAtExtractDate();
        EG = SHBE_Handler.getEthnicityGroup(D_Record);
        TT = D_Record.getTenancyType();
        DisabilityPremiumAwarded = D_Record.getDisabilityPremiumAwarded();
        SevereDisabilityPremiumAwarded = D_Record.getSevereDisabilityPremiumAwarded();
        DisabledChildPremiumAwarded = D_Record.getDisabledChildPremiumAwarded();
        EnhancedDisabilityPremiumAwarded = D_Record.getEnhancedDisabilityPremiumAwarded();
        PSI = D_Record.getPassportedStandardIndicator();
        HouseholdSize = SHBE_Handler.getHouseholdSize(D_Record);
        HouseholdIncome = SHBE_Handler.getHouseholdIncomeTotal(Record, D_Record);
        WeeklyHousingBenefitEntitlement = D_Record.getWeeklyHousingBenefitEntitlement();
        WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
        WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
        WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
        ContractualRentAmount = D_Record.getContractualRentAmount();
        WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
        WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
        ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
        ClaimantsNetWeeklyIncomeFromSelfEmployment = D_Record.getClaimantsNetWeeklyIncomeFromSelfEmployment();
        ClaimantsStudentIndicator = D_Record.getClaimantsStudentIndicator();
        LHARegulationsApplied = D_Record.getLHARegulationsApplied();
        WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
        WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
        WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
        ContractualRentAmount = D_Record.getContractualRentAmount();
        WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
        WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
        // Counts
        RSLCount1++;
        switch (D_Record.getStatusOfHBClaimAtExtractDate()) {
            case 1:
                RSLHBPTICount1++;
                break;
            case 2:
                RSLHBPTSCount1++;
                break;
            default:
                RSLHBPTOCount1++;
                break;
        }
        switch (D_Record.getStatusOfCTBClaimAtExtractDate()) {
            case 1:
                RSLCTBPTICount1++;
                break;
            case 2:
                RSLCTBPTSCount1++;
                break;
            default:
                RSLCTBPTOCount1++;
                break;
        }
        RSLEthnicGroupCount[EG]++;
        if (Record.isClaimPostcodeFValidFormat()) {
            TotalCount_RSLPostcodeFValidFormat++;
        }
        if (Record.isClaimPostcodeFMappable()) {
            TotalCount_RSLPostcodeFMappable++;
        }
        TotalCount_RSLTTClaimant1[TT]++;
        // Disability
        if (DisabilityPremiumAwarded == 1) {
            TotalCount_RSLDisabilityPremiumAwardTT[TT]++;
        }
        if (SevereDisabilityPremiumAwarded == 1) {
            TotalCount_RSLSevereDisabilityPremiumAwardTT[TT]++;
        }
        if (DisabledChildPremiumAwarded == 1) {
            TotalCount_RSLDisabledChildPremiumAwardTT[TT]++;
        }
        if (EnhancedDisabilityPremiumAwarded == 1) {
            TotalCount_RSLEnhancedDisabilityPremiumAwardTT[TT]++;
        }
        // General Household Disability Flag
        if (DisabilityPremiumAwarded == 1
                || SevereDisabilityPremiumAwarded == 1
                || DisabledChildPremiumAwarded == 1
                || EnhancedDisabilityPremiumAwarded == 1) {
            TotalCount_RSLDisabilityAwardTT[TT]++;
        }
        TotalCount_RSLPSI[PSI]++;
        //if (HBRef.equalsIgnoreCase(CTBRef)) {
        RSLTotalHouseholdSize += HouseholdSize;
        if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
            TotalCount_RSLEmployedClaimants++;
        }
        if (ClaimantsNetWeeklyIncomeFromSelfEmployment > 0) {
            TotalCount_RSLSelfEmployedClaimants++;
        }
        if (ClaimantsStudentIndicator != null) {
            if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                TotalCount_RSLStudentsClaimants++;
            }
        }
        if (LHARegulationsApplied != null) {
            if (LHARegulationsApplied.equalsIgnoreCase("1")
                    || LHARegulationsApplied.equalsIgnoreCase("Yes")) {
                TotalCount_RSLLHACases++;
            }
        }
        if (HouseholdIncome > 0) {
            switch (StatusOfHBClaimAtExtractDate) {
                case 1:
                    RSLPTITotalIncome += HouseholdIncome;
                    TotalCount_RSLHBPTITotalIncomeNonZero++;
                    break;
                case 2:
                    RSLPTSTotalIncome += HouseholdIncome;
                    TotalCount_RSLHBPTSTotalIncomeNonZero++;
                    break;
                default:
                    RSLPTOTotalIncome += HouseholdIncome;
                    TotalCount_RSLHBPTOTotalIncomeNonZero++;
                    break;
            }
        } else {
            switch (StatusOfHBClaimAtExtractDate) {
                case 1:
                    TotalCount_RSLHBPTITotalIncomeZero++;
                    break;
                case 2:
                    TotalCount_RSLHBPTSTotalIncomeZero++;
                    break;
                default:
                    TotalCount_RSLHBPTOTotalIncomeZero++;
                    break;
            }
        }
        if (WeeklyHousingBenefitEntitlement > 0) {
            RSLTotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
            TotalCount_RSLWeeklyHBEntitlementNonZero++;
        } else {
            TotalCount_RSLWeeklyHBEntitlementZero++;
        }
        if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
            RSLTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
            TotalCount_RSLWeeklyCTBEntitlementNonZero++;
        } else {
            RSLTotalWeeklyCTBEntitlementZeroCount++;
        }
        if (WeeklyEligibleRentAmount > 0) {
            RSLTotalWeeklyEligibleRentAmount += WeeklyEligibleRentAmount;
            RSLTotalWeeklyEligibleRentAmountNonZeroCount++;
        } else {
            RSLTotalWeeklyEligibleRentAmountZeroCount++;
        }
        if (WeeklyEligibleCouncilTaxAmount > 0) {
            RSLTotalWeeklyEligibleCouncilTaxAmount += WeeklyEligibleCouncilTaxAmount;
            TotalCount_RSLWeeklyEligibleCouncilTaxAmountNonZero++;
        } else {
            TotalCount_RSLWeeklyEligibleCouncilTaxAmountZero++;
        }
        if (ContractualRentAmount > 0) {
            RSLTotalContractualRentAmount += ContractualRentAmount;
            RSLTotalContractualRentAmountNonZeroCount++;
        } else {
            RSLTotalContractualRentAmountZeroCount++;
        }
        if (WeeklyAdditionalDiscretionaryPayment > 0) {
            RSLTotalWeeklyAdditionalDiscretionaryPayment += WeeklyAdditionalDiscretionaryPayment;
            RSLTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount++;
        } else {
            RSLTotalWeeklyAdditionalDiscretionaryPaymentZeroCount++;
        }
        if (WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability > 0) {
            RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability += WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
            RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount++;
        } else {
            RSLTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount++;
        }
    }

    protected void doSingleTimeRentArrearsCount(DW_UO_Record UORec) {
        Double totalRA;
        totalRA = UORec.getTotalRentArrears();
        if (totalRA != null) {
            CouncilTotal_RentArrears += totalRA;
            TotalCount_CouncilRentArrears += 1.0d;
            if (totalRA > 0.0d) {
                TotalCount_CouncilRentArrearsNonZero++;
            } else {
                TotalCount_CouncilRentArrearsZero++;
            }
        }
    }

    protected void doCouncilCompare2TimesHBCount(
            SHBE_ID ClaimID,
            HashSet<SHBE_ID> Group,
            Double Arrears0,
            Integer TT0,
            ONSPD_ID PostcodeID0,
            boolean ClaimPostcodeFValidPostcodeFormat0,
            boolean ClaimPostcodeFMappable0,
            Double Arrears1,
            Integer TT1,
            ONSPD_ID PostcodeID1,
            boolean ClaimPostcodeFValidPostcodeFormat1,
            boolean ClaimPostcodeFMappable1) {
        super.doCompare2TimesHBCount(
                TT0,
                PostcodeID0,
                ClaimPostcodeFValidPostcodeFormat0,
                ClaimPostcodeFMappable0,
                TT1,
                PostcodeID1,
                ClaimPostcodeFValidPostcodeFormat1,
                ClaimPostcodeFMappable1);
        addToCouncilPostcodeValidityCounts(TT0, PostcodeID0, ClaimPostcodeFValidPostcodeFormat0, ClaimPostcodeFMappable0, TT1, PostcodeID1, ClaimPostcodeFValidPostcodeFormat1, ClaimPostcodeFMappable1);
        if (Arrears0 != null && Arrears1 != null) {
            SumArrearsDiffInBoth += (Arrears1 - Arrears0);
            TotalCount_CouncilInBoth++;
            if (Group.contains(ClaimID)) {
                SumArrearsDiffInBothG0 += (Arrears1 - Arrears0);
                TotalCount_CouncilInBothG0++;
            } else {
                SumArrearsDiffInBothG1 += (Arrears1 - Arrears0);
                TotalCount_CouncilInBothG1++;
            }
//        } else {
//            System.out.println("Arrears0 " + Arrears0);
//            System.out.println("Arrears1 " + Arrears1);
        }
    }

    protected void doCouncilCompare2TimesCTBCount(
            Integer TT0,
            ONSPD_ID PostcodeID0,
            boolean ClaimPostcodeFValidPostcodeFormat0,
            boolean ClaimPostcodeFMappable0,
            Integer TT1,
            ONSPD_ID PostcodeID1,
            boolean ClaimPostcodeFValidPostcodeFormat1,
            boolean ClaimPostcodeFMappable1) {
        super.doCompare2TimesCTBCount(
                TT0,
                PostcodeID0,
                ClaimPostcodeFValidPostcodeFormat0,
                ClaimPostcodeFMappable0,
                TT1,
                PostcodeID1,
                ClaimPostcodeFValidPostcodeFormat1,
                ClaimPostcodeFMappable1);
        addToCouncilPostcodeValidityCounts(TT0, PostcodeID0, ClaimPostcodeFValidPostcodeFormat0, ClaimPostcodeFMappable0, TT1, PostcodeID1, ClaimPostcodeFValidPostcodeFormat1, ClaimPostcodeFMappable1);
    }

    public void addToCouncilPostcodeValidityCounts(
            Integer TT0,
            ONSPD_ID PostcodeID0,
            boolean ClaimPostcodeFValidPostcodeFormat0,
            boolean ClaimPostcodeFMappable0,
            Integer TT1,
            ONSPD_ID PostcodeID1,
            boolean ClaimPostcodeFValidPostcodeFormat1,
            boolean ClaimPostcodeFMappable1) {
        if (ClaimPostcodeFMappable0) {
            if (ClaimPostcodeFMappable1) {
                TotalCount_CouncilPostcodeF0MappablePostcodeF1Mappable++;
                if (PostcodeID0.equals(PostcodeID1)) {
                    TotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged++;
                } else {
                    TotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeChanged++;
                }
            } else {
                TotalCount_CouncilPostcodeF0MappablePostcodeF1NotMappable++;
            }
        } else if (ClaimPostcodeFMappable1) {
            TotalCount_CouncilPostcodeF0NotMappablePostcodeF1Mappable++;
        } else {
            TotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappable++;
            if (PostcodeID0 == null) {
                TotalCount_CouncilPostcodeF0DNEPostcodeF1NotMappable++;
            } else if (PostcodeID1 == null) {
                TotalCount_CouncilPostcodeF0NotMappablePostcodeF1DNE++;
            } else if (PostcodeID0.equals(PostcodeID1)) {
                TotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged++;
            } else {
                TotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged++;
            }
        }
        if (TT0.compareTo(TT1) != 0) {
            if (!(TT0 == SHBE_TenancyType_Handler.iMinus999
                    || TT1 == SHBE_TenancyType_Handler.iMinus999)) {
                TotalCount_CouncilTTChangeClaimant++;
            }
            if (TT0 == SHBE_TenancyType_Handler.iMinus999) {
                if (TT1 == 1) {
                    TotalCount_CouncilMinus999TTToTT1++;
                }
            }
            if (TT1 == SHBE_TenancyType_Handler.iMinus999) {
                if (TT0 == 1) {
                    TotalCount_CouncilTT1ToMinus999TT++;
                }
            }
            if (TT0 == 1) {
                if (TT1 == 3 || TT0 == 6) {
                    TotalCount_CouncilTT1ToPrivateDeregulatedTTs++;
                    if (TT0 == 1) {
                        TotalCount_CouncilTT1ToPrivateDeregulatedTTs++;
                    }
                }
            }
            if (TT0 == 3 || TT0 == 6) {
                if (TT1 == 1) {
                    TotalCount_CouncilPrivateDeregulatedTTsToTT1++;
                    if (TT1 == 1) {
                        TotalCount_CouncilPrivateDeregulatedTTsToTT1++;
                    }
                }
            }
        }
        if (TT0 == 1 && TT1 == 1) {
            if (ClaimPostcodeFMappable0 && ClaimPostcodeFMappable1) {
                if (!PostcodeID0.equals(PostcodeID1)) {
                    TotalCount_CouncilPostcodeChangeWithinTT1++;
                }
            }
        }
        if (TT0 == 1 && TT1 == 4) {
            TotalCount_CouncilTT1ToTT4++;
        }
        if (TT0 == 4 && TT1 == 1) {
            TotalCount_CouncilTT4ToTT1++;
        }
        if (TT0 == 5 || TT0 == 7) {
            if (TT1 == 1) {
                TotalCount_CouncilCTBTTsToTT1++;
            }
        }
    }

    protected void doRSLCompare2TimesHBCount(
            Integer TT0,
            ONSPD_ID PostcodeID0,
            boolean ClaimPostcodeFValidPostcodeFormat0,
            boolean ClaimPostcodeFMappable0,
            Integer TT1,
            ONSPD_ID PostcodeID1,
            boolean ClaimPostcodeFValidPostcodeFormat1,
            boolean ClaimPostcodeFMappable1) {
        super.doCompare2TimesHBCount(
                TT0,
                PostcodeID0,
                ClaimPostcodeFValidPostcodeFormat0,
                ClaimPostcodeFMappable0,
                TT1,
                PostcodeID1,
                ClaimPostcodeFValidPostcodeFormat1,
                ClaimPostcodeFMappable1);
        addToRSLPostcodeValidityCounts(TT0, PostcodeID0, ClaimPostcodeFValidPostcodeFormat0, ClaimPostcodeFMappable0, TT1, PostcodeID1, ClaimPostcodeFValidPostcodeFormat1, ClaimPostcodeFMappable1);
    }

    protected void doRSLCompare2TimesCTBCount(
            Integer TT0,
            ONSPD_ID PostcodeID0,
            boolean ClaimPostcodeFValidPostcodeFormat0,
            boolean ClaimPostcodeFMappable0,
            Integer TT1,
            ONSPD_ID PostcodeID1,
            boolean ClaimPostcodeFValidPostcodeFormat1,
            boolean ClaimPostcodeFMappable1) {
        super.doCompare2TimesCTBCount(
                TT0,
                PostcodeID0,
                ClaimPostcodeFValidPostcodeFormat0,
                ClaimPostcodeFMappable0,
                TT1,
                PostcodeID1,
                ClaimPostcodeFValidPostcodeFormat1,
                ClaimPostcodeFMappable1);
        addToRSLPostcodeValidityCounts(TT0, PostcodeID0, ClaimPostcodeFValidPostcodeFormat0, ClaimPostcodeFMappable0, TT1, PostcodeID1, ClaimPostcodeFValidPostcodeFormat1, ClaimPostcodeFMappable1);
    }

    protected void addToRSLPostcodeValidityCounts(
            Integer TT0,
            ONSPD_ID PostcodeID0,
            boolean ClaimPostcodeFValidPostcodeFormat0,
            boolean ClaimPostcodeFMappable0,
            Integer TT1,
            ONSPD_ID PostcodeID1,
            boolean ClaimPostcodeFValidPostcodeFormat1,
            boolean ClaimPostcodeFMappable1) {
        if (ClaimPostcodeFMappable0) {
            if (ClaimPostcodeFMappable1) {
                TotalCount_RSLPostcodeF0MappablePostcodeF1Mappable++;
                if (PostcodeID0.equals(PostcodeID1)) {
                    TotalCount_RSLPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged++;
                } else {
                    TotalCount_RSLPostcodeF0MappablePostcodeF1MappablePostcodeChanged++;
                }
            } else {
                TotalCount_RSLPostcodeF0MappablePostcodeF1NotMappable++;
            }
        } else if (ClaimPostcodeFMappable1) {
            TotalCount_RSLPostcodeF0NotMappablePostcodeF1Mappable++;
        } else {
            TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappable++;
            if (PostcodeID0 == null) {
                TotalCount_RSLPostcodeF0DNEPostcodeF1NotMappable++;
            } else if (PostcodeID1 == null) {
                TotalCount_RSLPostcodeF0NotMappablePostcodeF1DNE++;
            } else if (PostcodeID0.equals(PostcodeID1)) {
                TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged++;
            } else {
                TotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged++;
            }
        }
        if (TT0.compareTo(TT1) != 0) {
            if (!(TT0 == SHBE_TenancyType_Handler.iMinus999
                    || TT1 == SHBE_TenancyType_Handler.iMinus999)) {
                TotalCount_RSLTTChangeClaimant++;
            }
            if (TT0 == SHBE_TenancyType_Handler.iMinus999) {
                if (TT1 == 4) {
                    TotalCount_RSLMinus999TTToTT4++;
                }
            }
            if (TT1 == SHBE_TenancyType_Handler.iMinus999) {
                if (TT0 == 4) {
                    TotalCount_RSLTT4ToMinus999TT++;
                }
            }
            if (TT0 == 4) {
                if (TT1 == 3 || TT0 == 6) {
                    TotalCount_RSLTT4ToPrivateDeregulatedTTs++;
                }
            }
            if (TT0 == 3 || TT0 == 6) {
                if (TT1 == 4) {
                    TotalCount_RSLPrivateDeregulatedTTsToTT4++;
                }
            }
        }
        if (TT0 == 4 && TT1 == 4) {
            if (ClaimPostcodeFMappable0 && ClaimPostcodeFMappable1) {
                if (!PostcodeID0.equals(PostcodeID1)) {
                    TotalCount_RSLPostcodeChangeWithinTT4++;
                }
            }
        }
        if (TT0 == 1 && TT1 == 4) {
            TotalCount_RSLTT1ToTT4++;
        }
        if (TT0 == 4 && TT1 == 1) {
            TotalCount_RSLTT4ToTT1++;
        }
        if (TT0 == 5 || TT0 == 7) {
            if (TT1 == 4) {
                TotalCount_RSLCTBTTsToTT4++;
            }
        }
    }

    /**
     *
     * @param Group
     * @param SHBEFilenames
     * @param include
     * @param forceNewSummaries
     * @param HB_CTB
     * @param PTs
     * @param nTT
     * @param nEG
     * @param nPSI
     * @param DW_UO_Data
     * @param handleOutOfMemoryError
     * @return
     */
    public TreeMap<String, HashMap<String, String>> getSummaryTable(
            HashSet<SHBE_ID> Group,
            String[] SHBEFilenames,
            ArrayList<Integer> include,
            boolean forceNewSummaries,
            ArrayList<String> HB_CTB,
            ArrayList<String> PTs,
            int nTT,
            int nEG,
            int nPSI,
            DW_UO_Data DW_UO_Data,
            boolean handleOutOfMemoryError
    ) {
        String methodName = "getSummaryTable(...)";
        Env.logO("<" + methodName + ">", true);

//        AllCount0 = 0;
//        HBCount0 = 0;
//        CTBCount0 = 0;
//        CouncilCount0 = 0;
//        RSLCount0 = 0;
        initCounts(nTT, nEG, nPSI);

        // Declare variables
        TreeMap<String, HashMap<String, String>> result;
        int i;
        Iterator<Integer> includeIte;
        HashMap<String, String> summary;
        String key;
        String filename0;
        String filename1 = "";
        ONSPD_YM3 YM30 = null;
        ONSPD_YM3 YM31 = null;
        SHBE_Records SHBE_Records0;
        SHBE_Records SHBE_Records1 = null;
        HashMap<SHBE_ID, SHBE_Record> Records0;
        HashMap<SHBE_ID, SHBE_Record> Records1 = null;
        TreeMap<ONSPD_YM3, DW_UO_Set> CouncilUOSets;
        TreeMap<ONSPD_YM3, DW_UO_Set> RSLUOSets;
        DW_UO_Set CouncilUOSet0;
        DW_UO_Set RSLUOSet0;
        DW_UO_Set CouncilUOSet1 = null;
        DW_UO_Set RSLUOSet1 = null;
        Object[] filenames;
        TreeMap<ONSPD_YM3, String> CouncilFilenames;
        TreeMap<ONSPD_YM3, String> RSLFilenames;

        // Initialise result
        result = new TreeMap<>();
        includeIte = include.iterator();
        while (includeIte.hasNext()) {
            i = includeIte.next();
            summary = new HashMap<>();
            key = SHBE_Handler.getYearMonthNumber(SHBEFilenames[i]);
            result.put(key, summary);
        }

        // Initialise UO
        CouncilUOSets = DW_UO_Data.getCouncilUOSets();
        RSLUOSets = DW_UO_Data.getRSLUOSets();
        filenames = Env.getUO_Handler().getInputFilenames();
        CouncilFilenames = (TreeMap<ONSPD_YM3, String>) filenames[0];
        RSLFilenames = (TreeMap<ONSPD_YM3, String>) filenames[1];

        // Load first data
        includeIte = include.iterator();
        boolean initFirst = false;
        while (!initFirst) {
            i = includeIte.next();
            filename1 = SHBEFilenames[i];
            key = SHBE_Handler.getYearMonthNumber(filename1);
            YM31 = SHBE_Handler.getYM3(filename1);
            CouncilUOSet1 = CouncilUOSets.get(YM31);
            if (CouncilUOSet1 != null) {
                RSLUOSet1 = RSLUOSets.get(YM31);
                Env.logO("Load " + YM31, true);
                SHBE_Records1 = SHBE_Handler.getRecords(YM31, Env.HOOME);
                Records1 = SHBE_Records1.getRecords(Env.HOOME);
                initFirst = true;
            }
        }
        if (Records1 == null) {
            return result;
        }

        // Go through CouncilUOSet1 and initialise Group
        HashMap<SHBE_ID, DW_UO_Record> map;
        map = CouncilUOSet1.getMap();
        Iterator<SHBE_ID> ite;
        ite = map.keySet().iterator();
        SHBE_ID ClaimID;
        DW_UO_Record DW_UO_Record;
        Double tra;
        while (ite.hasNext()) {
            ClaimID = ite.next();
            DW_UO_Record = map.get(ClaimID);
            tra = DW_UO_Record.getTotalRentArrears();
            if (tra != null) {
                if (tra < 50) {
                    //System.out.println(tra);
                    Group.add(ClaimID);
                }
            }
        }

        // Summarise first data
        doPartSummarySingleTime(
                SHBE_Records1,
                YM31,
                filename1,
                forceNewSummaries,
                HB_CTB,
                PTs,
                nTT,
                nEG,
                nPSI,
                CouncilFilenames,
                RSLFilenames,
                CouncilUOSet1,
                RSLUOSet1,
                result);

        filename0 = filename1;
        //YM30 = YM31;
        YM30 = new ONSPD_YM3(YM31);
        SHBE_Records0 = SHBE_Records1;
        Records0 = Records1;
        CouncilUOSet0 = CouncilUOSet1;
        RSLUOSet0 = RSLUOSet1;
        incrementCounts(nTT);
        initCounts(nTT, nEG, nPSI);

        while (includeIte.hasNext()) {
            i = includeIte.next();
            filename1 = SHBEFilenames[i];
            YM31 = SHBE_Handler.getYM3(filename1);
            CouncilUOSet1 = CouncilUOSets.get(YM31);
            RSLUOSet1 = RSLUOSets.get(YM31);
            // Load next data
            Env.logO("Load " + YM31, true);
            SHBE_Records1 = SHBE_Handler.getRecords(YM31, Env.HOOME);
            Records1 = SHBE_Records1.getRecords(Env.HOOME);
            // doPartSummaryCompare2Times
            doPartSummaryCompare2Times(
                    Group,
                    SHBE_Records0,
                    Records0,
                    YM30,
                    filename0,
                    CouncilUOSet0,
                    RSLUOSet0,
                    SHBE_Records1,
                    Records1,
                    YM31,
                    filename1,
                    CouncilUOSet1,
                    RSLUOSet1,
                    forceNewSummaries,
                    HB_CTB,
                    PTs,
                    nTT,
                    nEG,
                    nPSI,
                    CouncilFilenames,
                    RSLFilenames,
                    result);
            // Set up vars for next iteration
            if (includeIte.hasNext()) {
                filename0 = filename1;
                //YM30 = YM31;
                YM30 = new ONSPD_YM3(YM31);
                SHBE_Records0 = SHBE_Records1;
                CouncilUOSet0 = CouncilUOSet1;
                RSLUOSet0 = RSLUOSet1;
                incrementCounts(nTT);
                initCounts(nTT, nEG, nPSI);
            }
        }
        return result;
    }

    @Override
    protected void incrementCounts(int nTT) {
        super.incrementCounts(nTT);
        //        for (int TT = 0; TT < nTT; TT++) {
//            TotalCount_TTClaimant0[TT] = TotalCount_TTClaimant1[TT];
//        }
        CouncilCount0 = CouncilCount1;
        CouncilHBPTICount0 = CouncilHBPTICount1;
        CouncilHBPTSCount0 = CouncilHBPTSCount1;
        CouncilHBPTOCount0 = CouncilHBPTOCount1;
        CouncilCTBPTICount0 = CouncilCTBPTICount1;
        CouncilCTBPTSCount0 = CouncilCTBPTSCount1;
        CouncilCTBPTOCount0 = CouncilCTBPTOCount1;
        RSLCount0 = RSLCount1;
        RSLHBPTICount0 = RSLHBPTICount1;
        RSLHBPTSCount0 = RSLHBPTSCount1;
        RSLHBPTOCount0 = RSLHBPTOCount1;
        RSLCTBPTICount0 = RSLCTBPTICount1;
        RSLCTBPTSCount0 = RSLCTBPTSCount1;
        RSLCTBPTOCount0 = RSLCTBPTOCount1;
        RSLLinkedRecordCount0 = RSLLinkedRecordCount1;
        RSLLinkedRecordCount0 = RSLLinkedRecordCount1;
        AllUOLinkedRecordCount0 = AllUOLinkedRecordCount1;
//        for (int TT = 0; TT < nTT; TT++) {
//            TotalCount_CouncilTTClaimant0[TT] = TotalCount_CouncilTTClaimant1[TT];
//            TotalCount_RSLTTClaimant0[TT] = TotalCount_RSLTTClaimant1[TT];
//        }
        System.arraycopy(TotalCount_CouncilTTClaimant1, 0, TotalCount_CouncilTTClaimant0, 0, nTT);
        System.arraycopy(TotalCount_RSLTTClaimant1, 0, TotalCount_RSLTTClaimant0, 0, nTT);
    }

    /**
     * @param Group
     * @param SHBE_Records0
     * @param Records0
     * @param YM30
     * @param filename0
     * @param SHBE_Records1
     * @param YM31
     * @param filename1
     * @param Records1
     * @param forceNewSummaries
     * @param nTT
     * @param nEG
     * @param nPSI
     * @param CouncilFilenames
     * @param HB_CTB
     * @param PTs
     * @param RSLFilenames
     * @param CouncilUOSet1
     * @param RSLUOSet1
     * @param CouncilUOSet0
     * @param RSLUOSet0
     * @param summaries
     */
    protected void doPartSummaryCompare2Times(
            HashSet<SHBE_ID> Group,
            SHBE_Records SHBE_Records0,
            HashMap<SHBE_ID, SHBE_Record> Records0,
            ONSPD_YM3 YM30,
            String filename0,
            DW_UO_Set CouncilUOSet0,
            DW_UO_Set RSLUOSet0,
            SHBE_Records SHBE_Records1,
            HashMap<SHBE_ID, SHBE_Record> Records1,
            ONSPD_YM3 YM31,
            String filename1,
            DW_UO_Set CouncilUOSet1,
            DW_UO_Set RSLUOSet1,
            boolean forceNewSummaries,
            ArrayList<String> HB_CTB,
            ArrayList<String> PTs,
            int nTT,
            int nEG,
            int nPSI,
            TreeMap<ONSPD_YM3, String> CouncilFilenames,
            TreeMap<ONSPD_YM3, String> RSLFilenames,
            TreeMap<String, HashMap<String, String>> summaries) {

        doPartSummarySingleTime(
                SHBE_Records1,
                YM31,
                filename1,
                forceNewSummaries,
                HB_CTB,
                PTs,
                nTT,
                nEG,
                nPSI,
                CouncilFilenames,
                RSLFilenames,
                CouncilUOSet1,
                RSLUOSet1,
                summaries);

        HashMap<SHBE_ID, DW_UO_Record> DW_UO_Set0CouncilMap;
        DW_UO_Set0CouncilMap = CouncilUOSet0.getMap();
        HashMap<SHBE_ID, DW_UO_Record> DW_UO_Set0RSLMap;
        DW_UO_Set0RSLMap = RSLUOSet0.getMap();
        HashMap<SHBE_ID, DW_UO_Record> DW_UO_Set1CouncilMap;
        DW_UO_Set1CouncilMap = CouncilUOSet1.getMap();
        HashMap<SHBE_ID, DW_UO_Record> DW_UO_Set1RSLMap;
        DW_UO_Set1RSLMap = RSLUOSet1.getMap();

        // Loop over underoccupancy data
        // Loop over Council
        doCouncilCompare2TimesLoopOverSet(
                Group,
                DW_UO_Set0CouncilMap,
                DW_UO_Set1CouncilMap,
                Records0,
                Records1);
        // Loop over RSL
        doRSLCompare2TimesLoopOverSet(
                DW_UO_Set0RSLMap,
                DW_UO_Set1RSLMap,
                Records0,
                Records1);

        String key;
        key = SHBE_Handler.getYearMonthNumber(filename1);
        HashMap<String, String> summary;
        summary = summaries.get(key);

        addToSummaryCompare2Times(nTT, nEG, nPSI, summary);

        // All
        summary.put(sSHBEFilename0, filename0);
        summary.put(sSHBEFilename1, filename1);
        summary.put(sCouncilFilename0, CouncilFilenames.get(YM30));
        summary.put(sRSLFilename0, RSLFilenames.get(YM30));
        summary.put(sCouncilCount0, Integer.toString(CouncilCount0));
        summary.put(sCouncilLinkedRecordCount0, Integer.toString(CouncilLinkedRecordCount0));
        summary.put(sRSLCount0, Integer.toString(RSLCount0));
        summary.put(sRSLLinkedRecordCount0, Integer.toString(RSLLinkedRecordCount0));
        summary.put(sAllUOLinkedRecordCount0, Integer.toString(CouncilLinkedRecordCount0 + RSLLinkedRecordCount0));
        summary.put(sCouncilFilename1, CouncilFilenames.get(YM31));
        summary.put(sRSLFilename1, RSLFilenames.get(YM31));
        summary.put(sCouncilLinkedRecordCount1, Integer.toString(CouncilLinkedRecordCount1));
        summary.put(sRSLLinkedRecordCount1, Integer.toString(RSLLinkedRecordCount1));
        summary.put(sAllUOLinkedRecordCount1, Integer.toString(CouncilLinkedRecordCount1 + RSLLinkedRecordCount1));
    }

    protected void doPartSummarySingleTime(
            SHBE_Records SHBE_Records,
            ONSPD_YM3 YM3,
            String filename,
            boolean forceNewSummaries,
            ArrayList<String> HB_CTB,
            ArrayList<String> PTs,
            int nTT,
            int nEG,
            int nPSI,
            TreeMap<ONSPD_YM3, String> CouncilFilenames,
            TreeMap<ONSPD_YM3, String> RSLFilenames,
            DW_UO_Set CouncilUOSet,
            DW_UO_Set RSLUOSet,
            TreeMap<String, HashMap<String, String>> summaries
    ) {
        // Declare variables
        String key;
        HashMap<String, String> summary;
        HashMap<String, Number> LoadSummary;
        HashMap<SHBE_ID, DW_UO_Record> DW_UO_SetCouncilMap;
        HashMap<SHBE_ID, DW_UO_Record> DW_UO_SetRSLMap;
        HashMap<SHBE_ID, SHBE_Record> Records;
        HashMap<String, BigDecimal> IncomeAndRentSummaryAllUO;
        HashMap<String, BigDecimal> IncomeAndRentSummaryCouncil;
        HashMap<String, BigDecimal> IncomeAndRentSummaryRSL;
        // Initialise variables
        key = SHBE_Handler.getYearMonthNumber(filename);
        summary = summaries.get(key);
        LoadSummary = SHBE_Records.getLoadSummary(Env.HOOME);
        addToSummary(summary, LoadSummary);
        DW_UO_SetCouncilMap = CouncilUOSet.getMap();
        DW_UO_SetRSLMap = RSLUOSet.getMap();
        Records = SHBE_Records.getRecords(Env.HOOME);
        // Loop over Council
        CouncilLinkedRecordCount1 = doCouncilSingleTimeLoopOverSet(
                DW_UO_SetCouncilMap, Records);
        // Loop over RSL
        RSLLinkedRecordCount1 = doRSLSingleTimeLoopOverSet(
                DW_UO_SetRSLMap, Records);
        // Prepare vars
        CouncilCount1 = DW_UO_SetCouncilMap.size();
        RSLCount1 = DW_UO_SetRSLMap.size();
        AllUOAllCount1 = CouncilCount1 + RSLCount1;
        AllUOLinkedRecordCount1 = CouncilLinkedRecordCount1 + RSLLinkedRecordCount1;
        // Add to counts
        DW_IncomeAndRentSummary iars;
        iars = new DW_IncomeAndRentSummary(Env);
        IncomeAndRentSummaryAllUO = iars.getIncomeAndRentSummary(
                SHBE_Records, HB_CTB, PTs, YM3, CouncilUOSet, RSLUOSet, true,
                true, true, forceNewSummaries);
        IncomeAndRentSummaryCouncil = iars.getIncomeAndRentSummary(
                SHBE_Records, HB_CTB, PTs, YM3, CouncilUOSet, null, true,
                true, false, forceNewSummaries);
        IncomeAndRentSummaryRSL = iars.getIncomeAndRentSummary(
                SHBE_Records, HB_CTB, PTs, YM3, null, RSLUOSet, true, false,
                true, forceNewSummaries);
        addToSummarySingleTimeIncomeAndRent(summary, IncomeAndRentSummaryAllUO, 
                IncomeAndRentSummaryCouncil, IncomeAndRentSummaryRSL);
        addToSummarySingleTime(nTT, nEG, nPSI, summary);
        addToSummarySingleTimeRentArrears(summary);
        summary.put(sSHBEFilename1, filename);
        summary.put(sCouncilFilename1, CouncilFilenames.get(YM3));
        summary.put(sRSLFilename1, RSLFilenames.get(YM3));
        summary.put(sCouncilLinkedRecordCount1, Integer.toString(CouncilLinkedRecordCount1));
        summary.put(sRSLLinkedRecordCount1, Integer.toString(RSLLinkedRecordCount1));
        summary.put(sAllCount1, Integer.toString(AllCount1));
        summary.put(sAllUOAllCount1, Integer.toString(AllUOAllCount1));
        summary.put(sAllUOLinkedRecordCount1, Integer.toString(AllUOLinkedRecordCount1));
    }

    /**
     * Processes in the following order: 1) Goes through all those formerly and
     * latterly Under Occupied (these are claims with records in both
     * DW_UO_Set0CouncilMap and DW_UO_Set1CouncilMap); 2) Goes through all those
     * latterly but not formerly Under Occupied; 3) Goes through all those
     * formerly but not latterly Under Occupied.
     *
     * @param Group
     * @param DW_UO_Set0CouncilMap Keys are ClaimIDs values are Council
     * DW_UO_Record for the former time period.
     * @param DW_UO_Set1CouncilMap Keys are ClaimIDs values are Council
     * DW_UO_Record for the latter time period.
     * @param Records0 Keys are ClaimIDs values are SHBE_Record for the
 former time period.
     * @param Records1 Keys are ClaimIDs values are SHBE_Record for the
 latter time period.
     */
    public void doCouncilCompare2TimesLoopOverSet(
            HashSet<SHBE_ID> Group,
            HashMap<SHBE_ID, DW_UO_Record> DW_UO_Set0CouncilMap,
            HashMap<SHBE_ID, DW_UO_Record> DW_UO_Set1CouncilMap,
            HashMap<SHBE_ID, SHBE_Record> Records0,
            HashMap<SHBE_ID, SHBE_Record> Records1) {
        Iterator<SHBE_ID> ite;
        // Go through all those currently and previously UO.
        ite = DW_UO_Set1CouncilMap.keySet().iterator();
        while (ite.hasNext()) {
            SHBE_ID ClaimID;
            ClaimID = ite.next();
            if (DW_UO_Set0CouncilMap.containsKey(ClaimID)) {
                doCouncilCompare2TimesCounts(
                        ClaimID,
                        Group,
                        Records0,
                        DW_UO_Set0CouncilMap,
                        Records1,
                        DW_UO_Set1CouncilMap);
            }
        }
        // Go through all those currently UO but that were not previously UO.      
        ite = DW_UO_Set1CouncilMap.keySet().iterator();
        while (ite.hasNext()) {
            SHBE_ID ClaimID;
            ClaimID = ite.next();
            DW_UO_Record UORec;
            UORec = DW_UO_Set1CouncilMap.get(ClaimID);
            // Rent Arrears DW_Summary
            doSingleTimeRentArrearsCount(UORec);
            if (!DW_UO_Set0CouncilMap.keySet().contains(ClaimID)) {
                doCouncilCompare2TimesCounts(
                        ClaimID,
                        Group,
                        Records0,
                        DW_UO_Set0CouncilMap,
                        Records1,
                        DW_UO_Set1CouncilMap);
            }
        }
        // Go through all those that were UO but are not currently UO.
        ite = DW_UO_Set0CouncilMap.keySet().iterator();
        while (ite.hasNext()) {
            SHBE_ID ClaimID;
            ClaimID = ite.next();
            if (!DW_UO_Set1CouncilMap.containsKey(ClaimID)) {
//                DW_UO_Record UORec;
//                UORec = DW_UO_Set0CouncilMap.get(ClaimID);
//                // Rent Arrears DW_Summary
//                doSingleTimeRentArrearsCount(UORec);
                doCouncilCompare2TimesCounts(
                        ClaimID,
                        Group,
                        Records0,
                        DW_UO_Set0CouncilMap,
                        Records1,
                        DW_UO_Set1CouncilMap);
            }
        }
    }

    public void doRSLCompare2TimesLoopOverSet(
            HashMap<SHBE_ID, DW_UO_Record> map0,
            HashMap<SHBE_ID, DW_UO_Record> map1,
            HashMap<SHBE_ID, SHBE_Record> Records0,
            HashMap<SHBE_ID, SHBE_Record> Records1) {
        Iterator<SHBE_ID> ite;
        SHBE_ID ClaimID;
        // Go through all those currently and previously UO.
        ite = map1.keySet().iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            if (map0.containsKey(ClaimID)) {
                doRSLCompare2TimesCountsAsNecessary(
                        ClaimID,
                        Records0,
                        Records1);
            }
        }
        // Go through all those currently UO but that were not previously UO.      
        ite = map1.keySet().iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            DW_UO_Record UORec;
            UORec = map1.get(ClaimID);
            // Rent Arrears DW_Summary
            doSingleTimeRentArrearsCount(UORec);
            if (!map0.keySet().contains(ClaimID)) {
                doRSLCompare2TimesCountsAsNecessary(
                        ClaimID,
                        Records0,
                        Records1);
            }
        }
        // Go through all those that were UO but are not currently UO.
        ite = map0.keySet().iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            if (!map1.containsKey(ClaimID)) {
//                DW_UO_Record UORec;
//                UORec = map0.get(ClaimID);
//                // Rent Arrears DW_Summary
//                doSingleTimeRentArrearsCount(UORec);
                doRSLCompare2TimesCountsAsNecessary(
                        ClaimID,
                        Records0,
                        Records1);
            }
        }
    }

    private void doRSLCompare2TimesCountsAsNecessary(
            SHBE_ID ClaimID,
            HashMap<SHBE_ID, SHBE_Record> Records0,
            HashMap<SHBE_ID, SHBE_Record> Records1) {
        SHBE_Record Record0;
        Record0 = Records0.get(ClaimID);
        SHBE_Record Record1;
        Record1 = Records1.get(ClaimID);
        SHBE_D_Record D_Record0;
        D_Record0 = null;
        if (Record0 != null) {
            D_Record0 = Record0.getDRecord();
        }
        SHBE_D_Record D_Record1;
        D_Record1 = null;
        if (Record1 == null) {
            doRSLCompare2TimesCounts(
                    Record0,
                    D_Record0,
                    Record1,
                    D_Record1);
        }
    }

    /**
     *
     * @param map
     * @param Records
     * @return
     */
    public int doCouncilSingleTimeLoopOverSet(
            HashMap<SHBE_ID, DW_UO_Record> map,
            HashMap<SHBE_ID, SHBE_Record> Records) {
        int linkedRecords;
        linkedRecords = 0;
        Iterator<SHBE_ID> ite;
        ite = map.keySet().iterator();
        while (ite.hasNext()) {
            SHBE_ID ClaimID;
            ClaimID = ite.next();
            DW_UO_Record DW_UO_Record;
            DW_UO_Record = map.get(ClaimID);
            // Rent Arrears DW_Summary
            doSingleTimeRentArrearsCount(DW_UO_Record);
            SHBE_Record Record;
            Record = Records.get(ClaimID);
            if (Record != null) {
                SHBE_D_Record D_Record;
                D_Record = Record.getDRecord();
                if (D_Record != null) {
                    doCouncilSingleTimeCount(Record, D_Record);
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
     * @return
     */
    public int doRSLSingleTimeLoopOverSet(
            HashMap<SHBE_ID, DW_UO_Record> map,
            HashMap<SHBE_ID, SHBE_Record> D_Records) {
        int linkedRecords;
        linkedRecords = 0;
        Iterator<SHBE_ID> ite;
        ite = map.keySet().iterator();
        while (ite.hasNext()) {
            SHBE_ID ClaimID;
            ClaimID = ite.next();
            DW_UO_Record UORec;
            UORec = map.get(ClaimID);
            // Rent Arrears DW_Summary
            doSingleTimeRentArrearsCount(UORec);
            SHBE_Record Record;
            Record = D_Records.get(ClaimID);
            if (Record != null) {
                SHBE_D_Record D_Record;
                D_Record = Record.getDRecord();
                if (D_Record != null) {
                    doRSLSingleTimeCount(Record, D_Record);
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
     * @param PTs
     * @param includeKey
     * @param nTT
     * @param nEG
     * @param nPSI
     */
    //@Override
    public void writeSummaryTables(
            TreeMap<String, HashMap<String, String>> summaryTable,
            ArrayList<String> PTs,
            String includeKey,
            int nTT,
            int nEG,
            int nPSI
    ) {
        writeSummaryTableCompare2TimesRentArrears(
                summaryTable,
                includeKey,
                nTT, nEG);
        writeSummaryTableCompare2TimesTT(
                summaryTable,
                includeKey,
                nTT, nEG);
        writeSummaryTableCompare2TimesPostcode(
                summaryTable,
                includeKey,
                nTT, nEG);
        writeSummaryTableSingleTimeGenericCounts(
                summaryTable,
                includeKey,
                nTT, nEG, nPSI);
        writeSummaryTableSingleTimeHouseholdSizes(
                summaryTable,
                includeKey,
                nTT, nEG, nPSI);
        writeSummaryTableSingleTimeEntitlementEligibleAmountContractualAmount(
                summaryTable,
                includeKey,
                nTT, nEG);
        writeSummaryTableSingleTimePSI(
                summaryTable,
                includeKey,
                nTT, nEG, nPSI);
        writeSummaryTableSingleTimeRentAndIncome(
                summaryTable,
                includeKey,
                nTT, nEG);
        writeSummaryTableSingleTimeTT(
                summaryTable,
                includeKey,
                nTT, nEG);
        writeSummaryTableSingleTimeRentArrears(
                summaryTable,
                //PTs,
                includeKey,
                nTT, nEG);
        writeSummaryTableSingleTimeEthnicity(
                summaryTable,
                includeKey,
                nTT, nEG);
        writeSummaryTableSingleTimeDisability(
                summaryTable,
                includeKey,
                nTT, nEG);
    }

    /**
     * Provides comparisons with the previous period
     *
     * @param summaryTable
     * @param includeKey
     * @param nTT
     * @param nEG
     */
    public void writeSummaryTableCompare2TimesRentArrears(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            int nTT,
            int nEG
    ) {
        TreeMap<ONSPD_YM3, File> ONSPDFiles;
        ONSPDFiles = Env.ONSPD_Env.Files.getInputONSPDFiles();
        String name;
        name = "Compare2TimesRentArrears";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                // paymentType, 
                includeKey, true);
        // Write headers
        String header;
        header = getHeaderCompare2TimesGeneric();
        header += sTotalCount_CouncilInBoth + ",";
        header += sSumArrearsDiffInBoth + ",";
        header += "AverageArrearsDiffInBoth,";
        header += sTotalCount_CouncilInBothG0 + ",";
        header += sSumArrearsDiffInBothG0 + ",";
        header += "AverageArrearsDiffInBothG0,";
        header += sTotalCount_CouncilInBothG1 + ",";
        header += sSumArrearsDiffInBothG1 + ",";
        header += "AverageArrearsDiffInBothG1, ";
//        header += getHeaderCompare2TimesTTChangeCouncil();
//        header += getHeaderCompare2TimesTTChangeRSL();
//        header += getHeaderCompare2TimesPostcodeChangeCouncil();
//        header += getHeaderCompare2TimesPostcodeChangeRSL();
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
            String sCount;
            Double count = null;
            String sTotalArrearsDiff;
            Double TotalArrearsDiff;
            // Arrears all
            sCount = summary.get(sTotalCount_CouncilInBoth);
            if (sCount != null) {
                if (!sCount.equalsIgnoreCase("null")) {
                    count = Double.parseDouble(sCount);
                    line += count + ", ";
                } else {
                    line += ", ";
                }
            }
            sTotalArrearsDiff = summary.get(sSumArrearsDiffInBoth);
            TotalArrearsDiff = null;
            if (count != null) {
                if (count != 0) {
                    TotalArrearsDiff = Double.parseDouble(sTotalArrearsDiff);
                    line += TotalArrearsDiff + ",";
                    double a;
                    a = TotalArrearsDiff / (double) count;
                    line += a + ",";
                } else {
                    line += ",,";
                }
            } else {
                line += ",,";
            }
            // Arrears Group G0
            sCount = summary.get(sTotalCount_CouncilInBothG0);
            if (sCount != null) {
                if (!sCount.equalsIgnoreCase("null")) {
                    count = Double.parseDouble(sCount);
                    line += count + ", ";
                } else {
                    line += ", ";
                }
            }
            sTotalArrearsDiff = summary.get(sSumArrearsDiffInBothG0);
            TotalArrearsDiff = null;
            if (count != null) {
                if (count != 0) {
                    TotalArrearsDiff = Double.parseDouble(sTotalArrearsDiff);
                    line += TotalArrearsDiff + ",";
                    double a;
                    a = TotalArrearsDiff / (double) count;
                    line += a + ",";
                } else {
                    line += ",,";
                }
            } else {
                line += ",,";
            }
            // Arrears Group G1
            sCount = summary.get(sTotalCount_CouncilInBothG1);
            if (sCount != null) {
                if (!sCount.equalsIgnoreCase("null")) {
                    count = Double.parseDouble(sCount);
                    line += count + ", ";
                } else {
                    line += ", ";
                }
            }
            sTotalArrearsDiff = summary.get(sSumArrearsDiffInBothG1);
            TotalArrearsDiff = null;
            if (count != null) {
                if (count != 0) {
                    TotalArrearsDiff = Double.parseDouble(sTotalArrearsDiff);
                    line += TotalArrearsDiff + ",";
                    double a;
                    a = TotalArrearsDiff / (double) count;
                    line += a + ",";
                } else {
                    line += ",,";
                }
            } else {
                line += ",,";
            }
//            line += getLineCompare2TimesTTChangeCouncil(summary);
//            line += getLineCompare2TimesTTChangeRSL(summary);
//            line += getLineCompare2TimesPostcodeChangeCouncil(summary);
//            line += getLineCompare2TimesPostcodeChangeRSL(summary);
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    protected String getHeaderCompare2TimesPostcodeChangeCouncil() {
        String header = "";
        header += sTotalCount_CouncilPostcodeF0MappablePostcodeF1Mappable + ", ";
        header += sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1Mappable + ", ";
        header += sTotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged + ", ";
        header += sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged + ", ";
        header += sTotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeChange + ", ";
        header += sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeChange + ", ";
        header += sTotalCount_CouncilPostcodeF0MappablePostcodeF1NotMappable + ", ";
        header += sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1NotMappable + ", ";
        header += sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1Mappable + ", ";
        header += sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1Mappable + ", ";
        header += sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappable + ", ";
        header += sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1NotMappable + ", ";
        header += sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged + ", ";
        header += sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcode1NotMappablePostcodeNotChanged + ", ";
        header += sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged + ", ";
        header += sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged + ", ";
        header += sTotalCount_CouncilPostcodeF0MappablePostcodeF1DNE + ", ";
        header += sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1DNE + ", ";
        header += sTotalCount_CouncilPostcodeF0DNEPostcodeF1Mappable + ", ";
        header += sPercentageOfCouncilCount0_CouncilPostcodeF0DNEPostcodeF1Mappable + ", ";
        header += sTotalCount_CouncilPostcodeF0DNEPostcodeF1DNE + ", ";
        header += sPercentageOfCouncilCount0_CouncilPostcodeF0DNEPostcodeF1DNE + ", ";
        header += sTotalCount_CouncilPostcodeF0DNEPostcodeF1NotMappable + ", ";
        header += sPercentageOfCouncilCount0_CouncilPostcodeF0DNEPostcodeF1NotMappable + ", ";
        header += sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1DNE + ", ";
        header += sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1DNE + ", ";
        return header;
    }

    protected String getHeaderCompare2TimesPostcodeChangeRSL() {
        String header = "";
        header += sTotalCount_RSLPostcodeF0MappablePostcodeF1Mappable + ", ";
        header += sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1Mappable + ", ";
        header += sTotalCount_RSLPostcodeF0MappablePostcode1MappablePostcodeNotChanged + ", ";
        header += sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged + ", ";
        header += sTotalCount_RSLPostcodeF0MappablePostcodeF1MappablePostcodeChanged + ", ";
        header += sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1MappablePostcodeChanged + ", ";
        header += sTotalCount_RSLPostcodeF0MappablePostcodeF1NotMappable + ", ";
        header += sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1NotMappable + ", ";
        header += sTotalCount_RSLPostcodeF0NotMappablePostcodeF1Mappable + ", ";
        header += sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1Mappable + ", ";
        header += sTotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappable + ", ";
        header += sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1NotMappable + ", ";
        header += sTotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged + ", ";
        header += sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged + ", ";
        header += sTotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged + ", ";
        header += sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged + ", ";
        header += sTotalCount_RSLPostcodeF0MappablePostcodeF1DNE + ", ";
        header += sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1DNE + ", ";
        header += sTotalCount_RSLPostcodeF0DNEPostcodeF1Mappable + ", ";
        header += sPercentageOfRSLCount0_RSLPostcodeF0DNEPostcodeF1Mappable + ", ";
        header += sTotalCount_RSLPostcodeF0DNEPostcodeF1DNE + ", ";
        header += sPercentageOfRSLCount0_RSLPostcodeF0DNEPostcodeF1DNE + ", ";
        header += sTotalCount_RSLPostcodeF0DNEPostcodeF1NotMappable + ", ";
        header += sPercentageOfRSLCount0_RSLPostcodeF0DNEPostcodeF1NotMappable + ", ";
        header += sTotalCount_RSLPostcodeF0NotMappablePostcodeF1DNE + ", ";
        header += sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1DNE + ", ";
        return header;
    }

    protected String getLineCompare2TimesPostcodeChangeCouncil(HashMap<String, String> summary) {
        String line = "";
        line += summary.get(sTotalCount_CouncilPostcodeF0MappablePostcodeF1Mappable) + ", ";
        line += summary.get(sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1Mappable) + ", ";
        line += summary.get(sTotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged) + ", ";
        line += summary.get(sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged) + ", ";
        line += summary.get(sTotalCount_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeChange) + ", ";
        line += summary.get(sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1MappablePostcodeChange) + ", ";
        line += summary.get(sTotalCount_CouncilPostcodeF0MappablePostcodeF1NotMappable) + ", ";
        line += summary.get(sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1NotMappable) + ", ";
        line += summary.get(sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1Mappable) + ", ";
        line += summary.get(sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1Mappable) + ", ";
        line += summary.get(sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappable) + ", ";
        line += summary.get(sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1NotMappable) + ", ";
        line += summary.get(sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged) + ", ";
        line += summary.get(sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcode1NotMappablePostcodeNotChanged) + ", ";
        line += summary.get(sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged) + ", ";
        line += summary.get(sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged) + ", ";
        line += summary.get(sTotalCount_CouncilPostcodeF0MappablePostcodeF1DNE) + ", ";
        line += summary.get(sPercentageOfCouncilCount0_CouncilPostcodeF0MappablePostcodeF1DNE) + ", ";
        line += summary.get(sTotalCount_CouncilPostcodeF0DNEPostcodeF1Mappable) + ", ";
        line += summary.get(sPercentageOfCouncilCount0_CouncilPostcodeF0DNEPostcodeF1Mappable) + ", ";
        line += summary.get(sTotalCount_CouncilPostcodeF0DNEPostcodeF1DNE) + ", ";
        line += summary.get(sPercentageOfCouncilCount0_CouncilPostcodeF0DNEPostcodeF1DNE) + ", ";
        line += summary.get(sTotalCount_CouncilPostcodeF0DNEPostcodeF1NotMappable) + ", ";
        line += summary.get(sPercentageOfCouncilCount0_CouncilPostcodeF0DNEPostcodeF1NotMappable) + ", ";
        line += summary.get(sTotalCount_CouncilPostcodeF0NotMappablePostcodeF1DNE) + ", ";
        line += summary.get(sPercentageOfCouncilCount0_CouncilPostcodeF0NotMappablePostcodeF1DNE) + ", ";
        return line;
    }

    protected String getLineCompare2TimesPostcodeChangeRSL(HashMap<String, String> summary) {
        String line = "";
        line += summary.get(sTotalCount_RSLPostcodeF0MappablePostcodeF1Mappable) + ", ";
        line += summary.get(sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1Mappable) + ", ";
        line += summary.get(sTotalCount_RSLPostcodeF0MappablePostcode1MappablePostcodeNotChanged) + ", ";
        line += summary.get(sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1MappablePostcodeNotChanged) + ", ";
        line += summary.get(sTotalCount_RSLPostcodeF0MappablePostcodeF1MappablePostcodeChanged) + ", ";
        line += summary.get(sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1MappablePostcodeChanged) + ", ";
        line += summary.get(sTotalCount_RSLPostcodeF0MappablePostcodeF1NotMappable) + ", ";
        line += summary.get(sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1NotMappable) + ", ";
        line += summary.get(sTotalCount_RSLPostcodeF0NotMappablePostcodeF1Mappable) + ", ";
        line += summary.get(sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1Mappable) + ", ";
        line += summary.get(sTotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappable) + ", ";
        line += summary.get(sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1NotMappable) + ", ";
        line += summary.get(sTotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged) + ", ";
        line += summary.get(sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeNotChanged) + ", ";
        line += summary.get(sTotalCount_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged) + ", ";
        line += summary.get(sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1NotMappablePostcodeChanged) + ", ";
        line += summary.get(sTotalCount_RSLPostcodeF0MappablePostcodeF1DNE) + ", ";
        line += summary.get(sPercentageOfRSLCount0_RSLPostcodeF0MappablePostcodeF1DNE) + ", ";
        line += summary.get(sTotalCount_RSLPostcodeF0DNEPostcodeF1Mappable) + ", ";
        line += summary.get(sPercentageOfRSLCount0_RSLPostcodeF0DNEPostcodeF1Mappable) + ", ";
        line += summary.get(sTotalCount_RSLPostcodeF0DNEPostcodeF1DNE) + ", ";
        line += summary.get(sPercentageOfRSLCount0_RSLPostcodeF0DNEPostcodeF1DNE) + ", ";
        line += summary.get(sTotalCount_RSLPostcodeF0DNEPostcodeF1NotMappable) + ", ";
        line += summary.get(sPercentageOfRSLCount0_RSLPostcodeF0DNEPostcodeF1NotMappable) + ", ";
        line += summary.get(sTotalCount_RSLPostcodeF0NotMappablePostcodeF1DNE) + ", ";
        line += summary.get(sPercentageOfRSLCount0_RSLPostcodeF0NotMappablePostcodeF1DNE) + ", ";
        return line;
    }

    public String getHeaderCompare2TimesTTChangeCouncil() {
        String header = "";
        header += sTotalCount_CouncilTTChangeClaimant + ", ";
        header += sPercentageOfCouncilCount0_CouncilTTChangeClaimant + ", ";
        header += sTotalCount_CouncilMinus999TTToTT1 + ", ";
        header += sTotalCount_CouncilTT1ToMinus999TT + ", ";
        header += sTotalCount_CouncilTT1ToPrivateDeregulatedTTs + ", ";
        header += sPercentageOfCouncilCount0_CouncilTT1ToPrivateDeregulatedTTs + ", ";
        header += sTotalCount_CouncilTT1ToTT4 + ", ";
        header += sPercentageOfCouncilTT1_CouncilTT1ToTT4 + ", ";
        header += sTotalCount_CouncilTT4ToTT1 + ", ";
        header += sPercentageOfCouncilTT4_CouncilTT4ToTT1 + ", ";
        header += sTotalCount_CouncilPostcodeChangeWithinTT1 + ", ";
        header += sPercentageOfCouncilTT1_CouncilPostcodeChangeWithinTT1 + ", ";
        return header;
    }

    public String getHeaderCompare2TimesTTChangeRSL() {
        String header = "";
        header += sTotalCount_RSLTTChangeClaimant + ", ";
        header += sPercentageOfRSLHB_RSLTTChangeClaimant + ", ";
        header += sTotalCount_RSLMinus999TTToTT4 + ", ";
        header += sTotalCount_RSLTT4ToMinus999TT + ", ";
        header += sTotalCount_RSLTT4ToPrivateDeregulatedTTs + ", ";
        header += sPercentageOfRSLTT4_RSLTT4ToPrivateDeregulatedTTs + ", ";
        header += sTotalCount_RSLTT1ToTT4 + ", ";
        header += sRSLPercentageOfRSLTT1_TT1ToTT4 + ", ";
        header += sTotalCount_RSLTT4ToTT1 + ", ";
        header += sPercentageOfRSLTT4_RSLTT4ToTT1 + ", ";
        header += sTotalCount_RSLPostcodeChangeWithinTT4 + ", ";
        header += sPercentageOfRSLTT4_RSLPostcodeChangeWithinTT4 + ", ";
        return header;
    }

    protected String getLineCompare2TimesTTChangeCouncil(HashMap<String, String> summary) {
        String line = "";
        line += summary.get(sTotalCount_CouncilTTChangeClaimant) + ", ";
        line += summary.get(sPercentageOfCouncilCount0_CouncilTTChangeClaimant) + ", ";
        line += summary.get(sTotalCount_CouncilMinus999TTToTT1) + ", ";
        line += summary.get(sTotalCount_CouncilTT1ToMinus999TT) + ", ";
        line += summary.get(sTotalCount_CouncilTT1ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sPercentageOfCouncilCount0_CouncilTT1ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sTotalCount_CouncilTT1ToTT4) + ", ";
        line += summary.get(sPercentageOfCouncilTT1_CouncilTT1ToTT4) + ", ";
        line += summary.get(sTotalCount_CouncilTT4ToTT1) + ", ";
        line += summary.get(sPercentageOfCouncilTT4_CouncilTT4ToTT1) + ", ";
        line += summary.get(sTotalCount_CouncilPostcodeChangeWithinTT1) + ", ";
        line += summary.get(sPercentageOfCouncilTT1_CouncilPostcodeChangeWithinTT1) + ", ";
        return line;
    }

    protected String getLineCompare2TimesTTChangeRSL(HashMap<String, String> summary) {
        String line = "";
        line += summary.get(sTotalCount_RSLTTChangeClaimant) + ", ";
        line += summary.get(sPercentageOfRSLHB_RSLTTChangeClaimant) + ", ";
        line += summary.get(sTotalCount_RSLMinus999TTToTT4) + ", ";
        line += summary.get(sTotalCount_RSLTT4ToMinus999TT) + ", ";
        line += summary.get(sTotalCount_RSLTT4ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sPercentageOfRSLTT4_RSLTT4ToPrivateDeregulatedTTs) + ", ";
        line += summary.get(sTotalCount_RSLTT1ToTT4) + ", ";
        line += summary.get(sRSLPercentageOfRSLTT1_TT1ToTT4) + ", ";
        line += summary.get(sTotalCount_RSLTT4ToTT1) + ", ";
        line += summary.get(sPercentageOfRSLTT4_RSLTT4ToTT1) + ", ";
        line += summary.get(sTotalCount_RSLPostcodeChangeWithinTT4) + ", ";
        line += summary.get(sPercentageOfRSLTT4_RSLPostcodeChangeWithinTT4) + ", ";
        return line;
    }

    /**
     * Provides comparisons with the previous period
     *
     * @param summaryTable
     * @param includeKey
     * @param nTT
     * @param nEG
     */
    @Override
    public void writeSummaryTableCompare2TimesTT(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            int nTT,
            int nEG
    ) {
        TreeMap<ONSPD_YM3, File> ONSPDFiles;
        ONSPDFiles = Env.ONSPD_Env.Files.getInputONSPDFiles();
        String name;
        name = "Compare2TimesTT";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType, 
                includeKey, true);
        // Write headers
        String header = "";
        header += getHeaderCompare2TimesGeneric();
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
            line += getLineCompare2TimesGeneric(summary, ONSPDFiles);
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
            TreeMap<ONSPD_YM3, File> ONSPDFiles) {
        String line = "";
        String filename0;
        filename0 = summary.get(sSHBEFilename0);
        line += filename0 + ", ";
        String month0;
        String year0;
        if (filename0 != null) {
            line += SHBE_Handler.getYearMonthNumber(filename0) + ", ";
            month0 = SHBE_Handler.getMonth3(filename0);
            year0 = SHBE_Handler.getYear(filename0);
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
            line += SHBE_Handler.getYearMonthNumber(filename1) + ", ";
            month1 = SHBE_Handler.getMonth3(filename1);
            year1 = SHBE_Handler.getYear(filename1);
            line += month1 + " " + year1 + ", ";
        } else {
            month1 = "null";
            year1 = "null";
            line += "null, ";
            line += "null, ";
        }
        ONSPD_YM3 PostCodeLookupDate0 = null;
        String PostCodeLookupFile0Name = null;
        if (filename0 != null) {
            PostCodeLookupDate0 = Postcode_Handler.getNearestYM3ForONSPDLookup(
                    SHBE_Handler.getYM3(filename0));
            PostCodeLookupFile0Name = ONSPDFiles.get(PostCodeLookupDate0).getName();
        }
        line += PostCodeLookupDate0 + ", " + PostCodeLookupFile0Name + ", ";
        ONSPD_YM3 PostCodeLookupDate1 = null;
        String PostCodeLookupFile1Name = null;
        if (filename1 != null) {
            PostCodeLookupDate1 = Postcode_Handler.getNearestYM3ForONSPDLookup(
                    SHBE_Handler.getYM3(filename1));
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
     * @param includeKey
     * @param nTT
     * @param nEG
     */
    @Override
    public void writeSummaryTableCompare2TimesPostcode(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            int nTT,
            int nEG
    ) {
        TreeMap<ONSPD_YM3, File> ONSPDFiles;
        ONSPDFiles = Env.ONSPD_Env.Files.getInputONSPDFiles();
        String name;
        name = "Compare2TimesPostcode";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType, 
                includeKey, true);
        // Write headers
        String header;
        header = getHeaderCompare2TimesGeneric();
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
            line += getLineCompare2TimesPostcodeChangeCouncil(summary);
            line += getLineCompare2TimesPostcodeChangeRSL(summary);
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    /**
     *
     * @param summaryTable
     * @param includeKey
     * @param nTT
     * @param nEG
     * @param nPSI
     */
    @Override
    public void writeSummaryTableSingleTimeGenericCounts(
            TreeMap<String, HashMap<String, String>> summaryTable,
            //String paymentType,
            String includeKey,
            int nTT,
            int nEG,
            int nPSI
    ) {
        TreeMap<ONSPD_YM3, File> ONSPDFiles;
        ONSPDFiles = Env.ONSPD_Env.Files.getInputONSPDFiles();
        String name;
        name = "SingleTimeGenericCounts";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType, 
                includeKey, true);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric();
        header += "PostCodeLookupDate, ";
        header += "PostCodeLookupFile, ";
        header += sCouncilCount1 + ", ";
        header += sCouncilHBPTICount1 + ", ";
        header += sCouncilHBPTSCount1 + ", ";
        header += sCouncilHBPTOCount1 + ", ";
        header += sCouncilCTBPTICount1 + ", ";
        header += sCouncilCTBPTSCount1 + ", ";
        header += sCouncilCTBPTOCount1 + ", ";
        header += sRSLCount1 + ", ";
        header += sRSLHBPTICount1 + ", ";
        header += sRSLHBPTSCount1 + ", ";
        header += sRSLHBPTOCount1 + ", ";
        header += sRSLCTBPTICount1 + ", ";
        header += sRSLCTBPTSCount1 + ", ";
        header += sRSLCTBPTOCount1 + ", ";
//        header += sTotalCount_CouncilPostcodeValidFormat + ", ";
//        header += sPercentageOfCouncilCount1_CouncilPostcodeValidFormat + ", ";
//        header += sTotalCount_CouncilPostcodeValid + ", ";
//        header += sPercentageOfCouncilCount1_CouncilPostcodeValid + ", ";
//        header += sTotalCount_RSLPostcodeValidFormat + ", ";
//        header += sPercentageOfRSLCount1_RSLPostcodeValidFormat + ", ";
//        header += sTotalCount_RSLPostcodeValid + ", ";
//        header += sPercentageOfRSLCount1_RSLPostcodeValid + ", ";
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
            line += summary.get(sCouncilCount1) + ", ";
            line += summary.get(sCouncilHBPTICount1) + ", ";
            line += summary.get(sCouncilHBPTSCount1) + ", ";
            line += summary.get(sCouncilHBPTOCount1) + ", ";
            line += summary.get(sCouncilCTBPTICount1) + ", ";
            line += summary.get(sCouncilCTBPTSCount1) + ", ";
            line += summary.get(sCouncilCTBPTOCount1) + ", ";
            line += summary.get(sRSLCount1) + ", ";
            line += summary.get(sRSLHBPTICount1) + ", ";
            line += summary.get(sRSLHBPTSCount1) + ", ";
            line += summary.get(sRSLHBPTOCount1) + ", ";
            line += summary.get(sRSLCTBPTICount1) + ", ";
            line += summary.get(sRSLCTBPTSCount1) + ", ";
            line += summary.get(sRSLCTBPTOCount1) + ", ";
//            line += summary.get(sTotalCount_CouncilPostcodeValidFormat) + ", ";
//            line += summary.get(sPercentageOfCouncilCount1_CouncilPostcodeValidFormat) + ", ";
//            line += summary.get(sTotalCount_CouncilPostcodeValid) + ", ";
//            line += summary.get(sPercentageOfCouncilCount1_CouncilPostcodeValid) + ", ";
//            line += summary.get(sTotalCount_RSLPostcodeValidFormat) + ", ";
//            line += summary.get(sPercentageOfRSLCount1_RSLPostcodeValidFormat) + ", ";
//            line += summary.get(sTotalCount_RSLPostcodeValid) + ", ";
//            line += summary.get(sPercentageOfRSLCount1_RSLPostcodeValid) + ", ";
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    /**
     *
     * @param summaryTable
     * @param includeKey
     * @param nTT
     * @param nEG
     * @param nPSI
     */
    @Override
    public void writeSummaryTableSingleTimeHouseholdSizes(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            int nTT,
            int nEG,
            int nPSI
    ) {
        TreeMap<ONSPD_YM3, File> ONSPDFiles;
        ONSPDFiles = Env.ONSPD_Env.Files.getInputONSPDFiles();
        String name;
        name = "SingleTimeHouseholdSizes";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType, 
                includeKey, true);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric();
        header += "PostCodeLookupDate, ";
        header += "PostCodeLookupFile, ";
        header += sAllTotalHouseholdSize + ", ";
        header += sAllAverageHouseholdSize + ", ";
        header += sTotal_HouseholdSizeTT[1] + ", ";
        header += sAverage_HouseholdSizeTT[1] + ", ";
        header += sTotal_HouseholdSizeTT[4] + ", ";
        header += sAverage_HouseholdSizeTT[4] + ", ";
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
            line += summary.get(sAllTotalHouseholdSize) + ", ";
            line += summary.get(sAllAverageHouseholdSize) + ", ";
            line += summary.get(sTotal_HouseholdSizeTT[1]) + ", ";
            line += summary.get(sAverage_HouseholdSizeTT[1]) + ", ";
            line += summary.get(sTotal_HouseholdSizeTT[4]) + ", ";
            line += summary.get(sAverage_HouseholdSizeTT[4]) + ", ";
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    /**
     *
     * @param summaryTable
     * @param includeKey
     * @param nTT
     * @param nEG
     */
    @Override
    public void writeSummaryTableSingleTimeEntitlementEligibleAmountContractualAmount(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeEntitlementEligibleAmountContractualAmount";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType,
                includeKey, true);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric();
        // All UO
        header += Strings.sTotal_WeeklyHBEntitlement + ", ";
        header += Strings.sTotalCount_WeeklyHBEntitlementNonZero + ", ";
        header += Strings.sTotalCount_WeeklyHBEntitlementZero + ", ";
        header += Strings.sAverage_NonZero_WeeklyHBEntitlement + ", ";
        // WeeklyEligibleRentAmount
        header += Strings.sTotal_HBWeeklyEligibleRentAmount + ", ";
        header += Strings.sTotalCount_HBWeeklyEligibleRentAmountNonZero + ", ";
        header += Strings.sTotalCount_HBWeeklyEligibleRentAmountZero + ", ";
        header += Strings.sAverage_NonZero_HBWeeklyEligibleRentAmount + ", ";
        // WeeklyEligibleCouncilTaxAmount
        header += sHBTotalWeeklyEligibleCouncilTaxAmount + ", ";
        header += sTotalCount_HBWeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sTotalCount_HBWeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sHBAverageWeeklyEligibleCouncilTaxAmount + ", ";
        // ContractualRentAmount
        header += sHBTotalContractualRentAmount + ", ";
        header += sTotalCount_HBContractualRentAmountNonZeroCount + ", ";
        header += sTotalCount_HBContractualRentAmountZeroCount + ", ";
        header += sHBAverageContractualRentAmount + ", ";
        // WeeklyAdditionalDiscretionaryPayment
        header += sHBTotalWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sHBAverageWeeklyAdditionalDiscretionaryPayment + ", ";
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        header += sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        // Council
        header += sTotal_CouncilWeeklyHBEntitlement + ", ";
        header += sTotalCount_CouncilWeeklyHBEntitlementNonZero + ", ";
        header += sTotalCount_CouncilWeeklyHBEntitlementZero + ", ";
        header += sAverage_NonZero_CouncilWeeklyHBEntitlement + ", ";
        // WeeklyEligibleRentAmount
        header += sTotal_CouncilWeeklyEligibleRentAmount + ", ";
        header += sTotalCount_CouncilWeeklyEligibleRentAmountNonZero + ", ";
        header += sTotalCount_CouncilWeeklyEligibleRentAmountZero + ", ";
        header += sAverage_NonZero_CouncilAverageWeeklyEligibleRentAmount + ", ";
        // WeeklyEligibleCouncilTaxAmount
        header += sTotal_CouncilWeeklyEligibleCouncilTaxAmount + ", ";
        header += sTotalCount_CouncilWeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sTotalCount_CouncilWeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sAverage_NonZero_CouncilWeeklyEligibleCouncilTaxAmount + ", ";
        // ContractualRentAmount
        header += sTotal_CouncilContractualRentAmount + ", ";
        header += sTotalCount_CouncilContractualRentAmountNonZero + ", ";
        header += sTotalCount_CouncilContractualRentAmountZero + ", ";
        header += sAverage_NonZero_CouncilContractualRentAmount + ", ";
        // WeeklyAdditionalDiscretionaryPayment
        header += sTotal_CouncilWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sTotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sTotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sAverage_NonZero_CouncilWeeklyAdditionalDiscretionaryPayment + ", ";
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        header += sTotal_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sTotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sTotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sAverage_NonZero_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        // RSL
        header += sTotal_RSLWeeklyHBEntitlement + ", ";
        header += sTotalCount_RSLWeeklyHBEntitlementNonZero + ", ";
        header += sTotalCount_RSLWeeklyHBEntitlementZero + ", ";
        header += sAverage_NonZero_RSLWeeklyHBEntitlement + ", ";
        // WeeklyEligibleRentAmount
        header += sTotal_RSLWeeklyEligibleRentAmount + ", ";
        header += sTotalCount_RSLWeeklyEligibleRentAmountNonZero + ", ";
        header += sTotalCount_RSLWeeklyEligibleRentAmountZero + ", ";
        header += sAverage_NonZero_RSLWeeklyEligibleRentAmount + ", ";
        // WeeklyEligibleCouncilTaxAmount
        header += sTotal_RSLWeeklyEligibleCouncilTaxAmount + ", ";
        header += sTotalCount_RSLWeeklyEligibleCouncilTaxAmountNonZero + ", ";
        header += sTotalCount_RSLWeeklyEligibleCouncilTaxAmountZero + ", ";
        header += sAverage_NonZero_RSLWeeklyEligibleCouncilTaxAmount + ", ";
        // ContractualRentAmount
        header += sTotal_RSLContractualRentAmount + ", ";
        header += sTotalCount_RSLContractualRentAmountNonZero + ", ";
        header += sTotalCount_ContractualRentAmountZero + ", ";
        header += sAverage_NonZero_RSLContractualRentAmount + ", ";
        // WeeklyAdditionalDiscretionaryPayment
        header += sTotal_RSLWeeklyAdditionalDiscretionaryPayment + ", ";
        header += sTotalCount_RSLWeeklyAdditionalDiscretionaryPaymentNonZero + ", ";
        header += sTotalCount_RSLWeeklyAdditionalDiscretionaryPaymentZero + ", ";
        header += sAverage_NonZero_RSLWeeklyAdditionalDiscretionaryPayment + ", ";
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        header += sTotal_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
        header += sTotalCount_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + ", ";
        header += sTotalCount_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + ", ";
        header += sAverage_NonZero_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + ", ";
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
            line += summary.get(Strings.sTotal_WeeklyHBEntitlement) + ", ";
            line += summary.get(Strings.sTotalCount_WeeklyHBEntitlementNonZero) + ", ";
            line += summary.get(Strings.sTotalCount_WeeklyHBEntitlementZero) + ", ";
            line += summary.get(Strings.sAverage_NonZero_WeeklyHBEntitlement) + ", ";
            // WeeklyEligibleRentAmount
            line += summary.get(Strings.sTotal_HBWeeklyEligibleRentAmount) + ", ";
            line += summary.get(Strings.sTotalCount_HBWeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(Strings.sTotalCount_HBWeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(Strings.sAverage_NonZero_HBWeeklyEligibleRentAmount) + ", ";
            // WeeklyEligibleCouncilTaxAmount
            line += summary.get(sHBTotalWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sTotalCount_HBWeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sTotalCount_HBWeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sHBAverageWeeklyEligibleCouncilTaxAmount) + ", ";
            // ContractualRentAmount
            line += summary.get(sHBTotalContractualRentAmount) + ", ";
            line += summary.get(sTotalCount_HBContractualRentAmountNonZeroCount) + ", ";
            line += summary.get(sTotalCount_HBContractualRentAmountZeroCount) + ", ";
            line += summary.get(sHBAverageContractualRentAmount) + ", ";
            // WeeklyAdditionalDiscretionaryPayment
            line += summary.get(sHBTotalWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sHBAverageWeeklyAdditionalDiscretionaryPayment) + ", ";
            // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
            line += summary.get(sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            // Council
            line += summary.get(sTotal_CouncilWeeklyHBEntitlement) + ", ";
            line += summary.get(sTotalCount_CouncilWeeklyHBEntitlementNonZero) + ", ";
            line += summary.get(sTotalCount_CouncilWeeklyHBEntitlementZero) + ", ";
            line += summary.get(sAverage_NonZero_CouncilWeeklyHBEntitlement) + ", ";
            // WeeklyEligibleRentAmount
            line += summary.get(sTotal_CouncilWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sTotalCount_CouncilWeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sTotalCount_CouncilWeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sAverage_NonZero_CouncilAverageWeeklyEligibleRentAmount) + ", ";
            // WeeklyEligibleCouncilTaxAmount
            line += summary.get(sTotal_CouncilWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sTotalCount_CouncilWeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sTotalCount_CouncilWeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sAverage_NonZero_CouncilWeeklyEligibleCouncilTaxAmount) + ", ";
            // ContractualRentAmount
            line += summary.get(sTotal_CouncilContractualRentAmount) + ", ";
            line += summary.get(sTotalCount_CouncilContractualRentAmountNonZero) + ", ";
            line += summary.get(sTotalCount_CouncilContractualRentAmountZero) + ", ";
            line += summary.get(sAverage_NonZero_CouncilContractualRentAmount) + ", ";
            // WeeklyAdditionalDiscretionaryPayment
            line += summary.get(sTotal_CouncilWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sTotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sTotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sAverage_NonZero_CouncilWeeklyAdditionalDiscretionaryPayment) + ", ";
            // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
            line += summary.get(sTotal_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sTotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sTotalCount_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sAverage_NonZero_CouncilWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            // RSL
            line += summary.get(sTotal_RSLWeeklyHBEntitlement) + ", ";
            line += summary.get(sTotalCount_RSLWeeklyHBEntitlementNonZero) + ", ";
            line += summary.get(sTotalCount_RSLWeeklyHBEntitlementZero) + ", ";
            line += summary.get(sAverage_NonZero_RSLWeeklyHBEntitlement) + ", ";
            // WeeklyEligibleRentAmount
            line += summary.get(sTotal_RSLWeeklyEligibleRentAmount) + ", ";
            line += summary.get(sTotalCount_RSLWeeklyEligibleRentAmountNonZero) + ", ";
            line += summary.get(sTotalCount_RSLWeeklyEligibleRentAmountZero) + ", ";
            line += summary.get(sAverage_NonZero_RSLWeeklyEligibleRentAmount) + ", ";
            // WeeklyEligibleCouncilTaxAmount
            line += summary.get(sTotal_RSLWeeklyEligibleCouncilTaxAmount) + ", ";
            line += summary.get(sTotalCount_RSLWeeklyEligibleCouncilTaxAmountNonZero) + ", ";
            line += summary.get(sTotalCount_RSLWeeklyEligibleCouncilTaxAmountZero) + ", ";
            line += summary.get(sAverage_NonZero_RSLWeeklyEligibleCouncilTaxAmount) + ", ";
            // ContractualRentAmount
            line += summary.get(sTotal_RSLContractualRentAmount) + ", ";
            line += summary.get(sTotalCount_RSLContractualRentAmountNonZero) + ", ";
            line += summary.get(sTotalCount_ContractualRentAmountZero) + ", ";
            line += summary.get(sAverage_NonZero_RSLContractualRentAmount) + ", ";
            // WeeklyAdditionalDiscretionaryPayment
            line += summary.get(sTotal_RSLWeeklyAdditionalDiscretionaryPayment) + ", ";
            line += summary.get(sTotalCount_RSLWeeklyAdditionalDiscretionaryPaymentNonZero) + ", ";
            line += summary.get(sTotalCount_RSLWeeklyAdditionalDiscretionaryPaymentZero) + ", ";
            line += summary.get(sAverage_NonZero_RSLWeeklyAdditionalDiscretionaryPayment) + ", ";
            // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
            line += summary.get(sTotal_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line += summary.get(sTotalCount_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + ", ";
            line += summary.get(sTotalCount_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + ", ";
            line += summary.get(sAverage_NonZero_RSLWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + ", ";
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    /**
     *
     * @param summaryTable
     * @param includeKey
     * @param nTT
     * @param nEG
     */
    @Override
    public void writeSummaryTableSingleTimeEmploymentEducationTraining(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeEmploymentEducationTraining";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType, 
                includeKey, true);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric();
        // All UO
        header += sTotalCount_AllClaimantsEmployed + ", ";
        header += sPercentageOfAll_AllClaimantsEmployed + ", ";
        header += sTotalCount_AllClaimantsSelfEmployed + ", ";
        header += sPercentageOfAll_AllClaimantsSelfEmployed + ", ";
        header += sTotalCount_AllClaimantsStudents + ", ";
        header += sPercentageOfAll_AllClaimantsStudents + ", ";
        header += sTotalCount_HBClaimantsEmployed + ", ";
        header += sPercentageOfHB_HBClaimantsEmployed + ", ";
        header += sTotalCount_HBClaimantsSelfEmployed + ", ";
        header += sPercentageOfHB_HBClaimantsSelfEmployed + ", ";
        header += sTotalCount_HBClaimantsStudents + ", ";
        header += sPercentageOfHB_HBClaimantsStudents + ", ";
        header += sTotalCount_CTBClaimantsEmployed + ", ";
        header += sPercentageOfCTB_CTBClaimantsEmployed + ", ";
        header += sTotalCount_CTBClaimantsSelfEmployed + ", ";
        header += sPercentageOfCTB_CTBClaimantsSelfEmployed + ", ";
        header += sTotalCount_CTBClaimantsStudents + ", ";
        header += sPercentageOfCTB_CTBClaimantsStudents + ", ";
        // Council
        header += sTotalCount_CouncilClaimantsEmployed + ", ";
        header += sPercentageOfCouncilCount1_CouncilClaimantsEmployed + ", ";
        header += sTotalCount_CouncilClaimantsSelfEmployed + ", ";
        header += sPercentageOfCouncilCount1_CouncilClaimantsSelfEmployed + ", ";
        header += sTotalCount_CouncilClaimantsStudents + ", ";
        header += sPercentageOfCouncilCount1_CouncilClaimantsStudents + ", ";
        // RSL
        header += sTotalCount_RSLClaimantsEmployed + ", ";
        header += sPercentageOfRSLCount1_RSLClaimantsEmployed + ", ";
        header += sTotalCount_RSLClaimantsSelfEmployed + ", ";
        header += sPercentageOfRSLCount1_RSLClaimantsSelfEmployed + ", ";
        header += sTotalCount_RSLClaimantsStudents + ", ";
        header += sPercentageOfRSLCount1_RSLClaimantsStudents + ", ";
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
            line += summary.get(sTotalCount_AllClaimantsEmployed) + ", ";
            line += summary.get(sPercentageOfAll_AllClaimantsEmployed) + ", ";
            line += summary.get(sTotalCount_AllClaimantsSelfEmployed) + ", ";
            line += summary.get(sPercentageOfAll_AllClaimantsSelfEmployed) + ", ";
            line += summary.get(sTotalCount_AllClaimantsStudents) + ", ";
            line += summary.get(sPercentageOfAll_AllClaimantsStudents) + ", ";
            line += summary.get(sTotalCount_HBClaimantsEmployed) + ", ";
            line += summary.get(sPercentageOfHB_HBClaimantsEmployed) + ", ";
            line += summary.get(sTotalCount_HBClaimantsSelfEmployed) + ", ";
            line += summary.get(sPercentageOfHB_HBClaimantsSelfEmployed) + ", ";
            line += summary.get(sTotalCount_HBClaimantsStudents) + ", ";
            line += summary.get(sPercentageOfHB_HBClaimantsStudents) + ", ";
            line += summary.get(sTotalCount_CTBClaimantsEmployed) + ", ";
            line += summary.get(sPercentageOfCTB_CTBClaimantsEmployed) + ", ";
            line += summary.get(sTotalCount_CTBClaimantsSelfEmployed) + ", ";
            line += summary.get(sPercentageOfCTB_CTBClaimantsSelfEmployed) + ", ";
            line += summary.get(sTotalCount_CTBClaimantsStudents) + ", ";
            line += summary.get(sPercentageOfCTB_CTBClaimantsStudents) + ", ";
            line += summary.get(sTotalCount_CTBLHACases) + ", ";
            line += summary.get(sPercentageOfCTB_CTBLHACases) + ", ";
            // Council
            line += summary.get(sTotalCount_CouncilClaimantsEmployed) + ", ";
            line += summary.get(sPercentageOfCouncilCount1_CouncilClaimantsEmployed) + ", ";
            line += summary.get(sTotalCount_CouncilClaimantsSelfEmployed) + ", ";
            line += summary.get(sPercentageOfCouncilCount1_CouncilClaimantsSelfEmployed) + ", ";
            line += summary.get(sTotalCount_CouncilClaimantsStudents) + ", ";
            line += summary.get(sPercentageOfCouncilCount1_CouncilClaimantsStudents) + ", ";
            // RSL
            line += summary.get(sTotalCount_RSLClaimantsEmployed) + ", ";
            line += summary.get(sPercentageOfRSLCount1_RSLClaimantsEmployed) + ", ";
            line += summary.get(sTotalCount_RSLClaimantsSelfEmployed) + ", ";
            line += summary.get(sPercentageOfRSLCount1_RSLClaimantsSelfEmployed) + ", ";
            line += summary.get(sTotalCount_RSLClaimantsStudents) + ", ";
            line += summary.get(sPercentageOfRSLCount1_RSLClaimantsStudents) + ", ";
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    /**
     *
     * @param summaryTable
     * @param includeKey
     * @param nTT
     * @param nEG
     */
    public void writeSummaryTableSingleTimeRentAndIncome(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeRentAndIncome";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                // paymentType,
                includeKey, true);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric();
        Iterator<String> PTsIte;
        // Council
        header += sTotal_CouncilHBPTIIncome + Strings.special_commaSpace;
        header += sTotalCount_CouncilHBPTIIncomeNonZero + Strings.special_commaSpace;
        header += sTotalCount_CouncilHBPTIIncomeZero + Strings.special_commaSpace;
        header += sAverage_NonZero_CouncilHBPTIIncome + Strings.special_commaSpace;
        header += sTotal_CouncilHBPTSIncome + Strings.special_commaSpace;
        header += sTotalCount_CouncilHBPTSIncomeNonZero + Strings.special_commaSpace;
        header += sTotalCount_CouncilHBPTSIncomeZero + Strings.special_commaSpace;
        header += sAverage_NonZero_CouncilHBPTSIncome + Strings.special_commaSpace;
        header += sTotal_CouncilHBPTOIncome + Strings.special_commaSpace;
        header += sTotalCount_CouncilHBPTOIncomeNonZero + Strings.special_commaSpace;
        header += sTotalCount_CouncilHBPTOIncomeZero + Strings.special_commaSpace;
        header += sAverage_NonZero_CouncilHBPTOIncome + Strings.special_commaSpace;
        header += sTotal_CouncilWeeklyEligibleRentAmount + Strings.special_commaSpace;
        header += sTotalCount_CouncilWeeklyEligibleRentAmountNonZero + Strings.special_commaSpace;
        header += sAverage_NonZero_CouncilAverageWeeklyEligibleRentAmount + Strings.special_commaSpace;
        // RSL
        header += sTotal_RSLHBPTIIncome + Strings.special_commaSpace;
        header += sTotalCount_RSLHBPTIIncomeNonZero + Strings.special_commaSpace;
        header += sAverage_NonZero_RSLIncome + Strings.special_commaSpace;
        header += sTotal_RSLHBPTSIncome + Strings.special_commaSpace;
        header += sTotal_RSLHBPTOIncome + Strings.special_commaSpace;
        header += sTotal_RSLWeeklyEligibleRentAmount + Strings.special_commaSpace;
        header += sTotalCount_RSLWeeklyEligibleRentAmountNonZero + Strings.special_commaSpace;
        header += sAverage_NonZero_RSLWeeklyEligibleRentAmount + Strings.special_commaSpace;
        header = header.substring(0, header.length() - 2);
        // All UO
        // All
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
            // Council
            // All
            line += summary.get(sTotal_CouncilHBPTIIncome) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CouncilHBPTIIncomeNonZero) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CouncilHBPTIIncomeZero) + Strings.special_commaSpace;
            line += summary.get(sAverage_NonZero_CouncilHBPTIIncome) + Strings.special_commaSpace;
            line += summary.get(sTotal_CouncilHBPTSIncome) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CouncilHBPTSIncomeNonZero) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CouncilHBPTSIncomeZero) + Strings.special_commaSpace;
            line += summary.get(sAverage_NonZero_CouncilHBPTSIncome) + Strings.special_commaSpace;
            line += summary.get(sTotal_CouncilHBPTOIncome) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CouncilHBPTOIncomeNonZero) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CouncilHBPTOIncomeZero) + Strings.special_commaSpace;
            line += summary.get(sAverage_NonZero_CouncilHBPTOIncome) + Strings.special_commaSpace;
            line += summary.get(sTotal_CouncilWeeklyEligibleRentAmount) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CouncilWeeklyEligibleRentAmountNonZero) + Strings.special_commaSpace;
            line += summary.get(sAverage_NonZero_CouncilAverageWeeklyEligibleRentAmount) + Strings.special_commaSpace;
            // RSL
            // All
            line += summary.get(sTotal_RSLHBPTIIncome) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_RSLHBPTIIncomeNonZero) + Strings.special_commaSpace;
            line += summary.get(sAverage_NonZero_RSLIncome) + Strings.special_commaSpace;
            line += summary.get(sTotal_RSLHBPTSIncome) + Strings.special_commaSpace;
            line += summary.get(sTotal_RSLHBPTOIncome) + Strings.special_commaSpace;
            line += summary.get(sTotal_RSLWeeklyEligibleRentAmount) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_RSLWeeklyEligibleRentAmountNonZero) + Strings.special_commaSpace;
            line += summary.get(sAverage_NonZero_RSLWeeklyEligibleRentAmount) + Strings.special_commaSpace;
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    /**
     *
     * @param summaryTable
     * @param includeKey
     * @param nTT
     * @param nEG
     */
    public void writeSummaryTableSingleTimeRentArrears(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeCouncilRentArrears";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType,
                includeKey, true);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += "year-month, ";
        header += sCouncilFilename1 + ", ";
        header += sCouncilCount1 + ", ";
        header += sCouncilLinkedRecordCount1 + ", ";
        header += "Month Year, ";
        header += sCouncilTotal_RentArrears + ", ";
        header += sTotalCount_CouncilRentArrearsNonZero + ", ";
        header += sTotalCount_CouncilRentArrearsZero + ", ";
        header += sAverage_CouncilRentArrears + ", ";
        header += sAverage_NonZero_CouncilRentArrears + ", ";
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
            line += summary.get(sTotalCount_CouncilRentArrearsNonZero) + ", ";
            line += summary.get(sTotalCount_CouncilRentArrearsZero) + ", ";
            line += summary.get(sAverage_CouncilRentArrears) + ", ";
            line += summary.get(sAverage_NonZero_CouncilRentArrears) + ", ";
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    /**
     *
     * @param summaryTable
     * @param includeKey
     * @param nTT
     * @param nEG
     */
    @Override
    public void writeSummaryTableSingleTimeEthnicity(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeEthnicity";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType, 
                includeKey, true);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric();
        // All UO
        for (int i = 1; i < nEG; i++) {
            header += sTotalCount_AllEthnicGroupClaimant[i] + ", ";
            header += sPercentageOfAll_EthnicGroupClaimant[i] + ", ";
        }
        // Council
        for (int i = 1; i < nEG; i++) {
            header += sTotalCount_CouncilEthnicGroupClaimant[i] + ", ";
            header += sPercentageOfCouncilCount1_CouncilEthnicGroupClaimant[i] + ", ";
        }
        // RSL
        for (int i = 1; i < nEG; i++) {
            header += sTotalCount_RSLEthnicGroupClaimant[i] + ", ";
            header += sPercentageOfRSLCount1_RSLEthnicGroupClaimant[i] + ", ";
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
                line += summary.get(sTotalCount_AllEthnicGroupSocialTTClaimant[i]) + ", ";
                line += summary.get(sPercentageOfSocialTT_EthnicGroupSocialTTClaimant[i]) + ", ";
            }
            // Council
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sTotalCount_CouncilEthnicGroupClaimant[i]) + ", ";
                line += summary.get(sPercentageOfCouncilCount1_CouncilEthnicGroupClaimant[i]) + ", ";
            }
            // RSL
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sTotalCount_RSLEthnicGroupClaimant[i]) + ", ";
                line += summary.get(sPercentageOfRSLCount1_RSLEthnicGroupClaimant[i]) + ", ";
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    /**
     *
     * @param summaryTable
     * @param includeKey
     * @param nTT
     * @param nEG
     */
    @Override
    public void writeSummaryTableSingleTimeTT(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeTT";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType,
                includeKey, true);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric();
        // Council
        header += sTotalCount_CouncilLHACases + ", ";
        header += sPercentageOfCouncilCount1_CouncilLHACases + ", ";
        // RSL
        header += sTotalCount_RSLLHACases + ", ";
        header += sPercentageOfRSLCount1_RSLLHACases + ", ";
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
            line += summary.get(sTotalCount_CouncilLHACases) + ", ";
            line += summary.get(sPercentageOfCouncilCount1_CouncilLHACases) + ", ";
            line += summary.get(sTotalCount_RSLLHACases) + ", ";
            line += summary.get(sPercentageOfRSLCount1_RSLLHACases) + ", ";
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    /**
     *
     * @param summaryTable
     * @param includeKey
     * @param nTT
     * @param nEG
     * @param nPSI
     */
    @Override
    public void writeSummaryTableSingleTimePSI(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            int nTT,
            int nEG,
            int nPSI
    ) {
        String name;
        name = "SingleTimePSI";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType, 
                includeKey, true);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + ", ";
        header += getHeaderSingleTimeGeneric();
        // Council
        for (int i = 1; i < nPSI; i++) {
            header += sTotalCount_CouncilPSI[i] + ", ";
            header += sPercentageOfCouncilCount1_CouncilPSI[i] + ", ";
        }
        // RSL
        for (int i = 1; i < nPSI; i++) {
            header += sTotalCount_RSLPSI[i] + ", ";
            header += sPercentageOfRSLCount1_RSLPSI[i] + ", ";
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
            // Council
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sTotalCount_CouncilPSI[i]) + ", ";
                line += summary.get(sPercentageOfCouncilCount1_CouncilPSI[i]) + ", ";
            }
            // RSL
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sTotalCount_RSLPSI[i]) + ", ";
                line += summary.get(sPercentageOfRSLCount1_RSLPSI[i]) + ", ";
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    /**
     *
     * @param summaryTable
     * @param includeKey
     * @param nTT
     * @param nEG
     */
    @Override
    public void writeSummaryTableSingleTimeDisability(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            int nTT,
            int nEG
    ) {
        int i;
        String name;
        name = "SingleTimeDisability";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType,
                includeKey, true);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + Strings.special_commaSpace;
        header += getHeaderSingleTimeGeneric();
        // All UO
        // DisabilityAward
        header += sTotalCount_AllDisabilityAward + Strings.special_commaSpace;
        header += sPercentageOfAll_AllDisabilityAward + Strings.special_commaSpace;
        // DisabilityPremiumAward
        header += sTotalCount_AllDisabilityPremiumAward + Strings.special_commaSpace;
        header += sPercentageOfAll_AllDisabilityPremiumAward + Strings.special_commaSpace;
        // SevereDisabilityPremiumAward
        header += sTotalCount_AllSevereDisabilityPremiumAward + Strings.special_commaSpace;
        header += sPercentageOfAll_AllSevereDisabilityPremiumAward + Strings.special_commaSpace;
        // DisabledChildPremiumAward
        header += sTotalCount_AllDisabledChildPremiumAward + Strings.special_commaSpace;
        header += sPercentageOfAll_AllDisabledChildPremiumAward + Strings.special_commaSpace;
        // EnhancedDisabilityPremiumAward
        header += sTotalCount_AllEnhancedDisabilityPremiumAward + Strings.special_commaSpace;
        header += sPercentageOfAll_AllEnhancedDisabilityPremiumAward + Strings.special_commaSpace;
        // Council
        // DisabilityAward
        header += sTotalCount_CouncilDisabilityAward + Strings.special_commaSpace;
        header += sPercentageOfCouncilCount1_CouncilDisabilityAward + Strings.special_commaSpace;
        // DisabilityPremiumAward
        header += sTotalCount_CouncilDisabilityPremiumAward + Strings.special_commaSpace;
        header += sPercentageOfCouncilCount1_CouncilDisabilityPremiumAward + Strings.special_commaSpace;
        // SevereDisabilityPremiumAward
        header += sTotalCount_CouncilSevereDisabilityPremiumAward + Strings.special_commaSpace;
        header += sPercentageOfCouncilCount1_CouncilSevereDisabilityPremiumAward + Strings.special_commaSpace;
        // DisabledChildPremiumAward
        header += sTotalCount_CouncilDisabledChildPremiumAward + Strings.special_commaSpace;
        header += sPercentageOfCouncilCount1_CouncilDisabledChildPremiumAward + Strings.special_commaSpace;
        // EnhancedDisabilityPremiumAward
        header += sTotalCount_CouncilEnhancedDisabilityPremiumAward + Strings.special_commaSpace;
        header += sPercentageOfCouncilCount1_CouncilEnhancedDisabilityPremiumAward + Strings.special_commaSpace;
        // RSL
        // DisabilityAward
        header += sTotalCount_RSLDisabilityAward + Strings.special_commaSpace;
        header += sPercentageOfRSLCount1_RSLDisabilityAward + Strings.special_commaSpace;
        // DisabilityPremiumAward
        header += sTotalCount_RSLDisabilityPremiumAward + Strings.special_commaSpace;
        header += sPercentageOfRSLCount1_RSLDisabilityPremiumAward + Strings.special_commaSpace;
        // SevereDisabilityPremiumAward
        header += sTotalCount_RSLSevereDisabilityPremiumAward + Strings.special_commaSpace;
        header += sPercentageOfRSLCount1_RSLSevereDisabilityPremiumAward + Strings.special_commaSpace;
        // DisabledChildPremiumAward
        header += sTotalCount_RSLDisabledChildPremiumAward + Strings.special_commaSpace;
        header += sPercentageOfRSLCount1_RSLDisabledChildPremiumAward + Strings.special_commaSpace;
        // EnhancedDisabilityPremiumAward
        header += sTotalCount_RSLEnhancedDisabilityPremiumAward + Strings.special_commaSpace;
        header += sPercentageOfRSLCount1_RSLEnhancedDisabilityPremiumAward + Strings.special_commaSpace;
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
            // DisabilityAward
            line += summary.get(sTotalCount_DisabilityAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfSocialTTs_DisabilityAwardSocialTTs) + Strings.special_commaSpace;
            // DisabilityPremiumAward
            line += summary.get(sTotalCount_DisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;
            // SevereDisabilityPremiumAward
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;
            // DisabledChildPremiumAward
            line += summary.get(sTotalCount_DisabledChildPremiumAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs) + Strings.special_commaSpace;
            // EnhancedDisabilityPremiumAward
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;

            // Council
            // DisabilityAward
            line += summary.get(sTotalCount_CouncilDisabilityAward) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfCouncilCount1_CouncilDisabilityAward) + Strings.special_commaSpace;
            // DisabilityPremiumAward
            line += summary.get(sTotalCount_CouncilDisabilityPremiumAward) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfCouncilCount1_CouncilDisabilityPremiumAward) + Strings.special_commaSpace;
            // SevereDisabilityPremiumAward
            line += summary.get(sTotalCount_CouncilSevereDisabilityPremiumAward) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfCouncilCount1_CouncilSevereDisabilityPremiumAward) + Strings.special_commaSpace;
            // DisabledChildPremiumAward
            line += summary.get(sTotalCount_CouncilDisabledChildPremiumAward) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfCouncilCount1_CouncilDisabledChildPremiumAward) + Strings.special_commaSpace;
            // EnhancedDisabilityPremiumAward
            line += summary.get(sTotalCount_CouncilEnhancedDisabilityPremiumAward) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfCouncilCount1_CouncilEnhancedDisabilityPremiumAward) + Strings.special_commaSpace;
            // RSL
            // DisabilityAward
            line += summary.get(sTotalCount_RSLDisabilityAward) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfRSLCount1_RSLDisabilityAward) + Strings.special_commaSpace;
            // DisabilityPremiumAward
            line += summary.get(sTotalCount_RSLDisabilityPremiumAward) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfRSLCount1_RSLDisabilityPremiumAward) + Strings.special_commaSpace;
            // SevereDisabilityPremiumAward
            line += summary.get(sTotalCount_RSLSevereDisabilityPremiumAward) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfRSLCount1_RSLSevereDisabilityPremiumAward) + Strings.special_commaSpace;
            // DisabledChildPremiumAward
            line += summary.get(sTotalCount_RSLDisabledChildPremiumAward) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfRSLCount1_RSLDisabledChildPremiumAward) + Strings.special_commaSpace;
            // EnhancedDisabilityPremiumAward
            line += summary.get(sTotalCount_RSLEnhancedDisabilityPremiumAward) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfRSLCount1_RSLEnhancedDisabilityPremiumAward) + Strings.special_commaSpace;
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
