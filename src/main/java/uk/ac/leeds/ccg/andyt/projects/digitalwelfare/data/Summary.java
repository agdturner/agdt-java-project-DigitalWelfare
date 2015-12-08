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

    // Counter Strings
    // Single
    // All
    private static final String AllTotalHouseholdSizeString = "AllTotalHouseholdSize";
    private static final String AllAverageHouseholdSizeString = "AllAverageHouseholdSize";
    private static final String HBTotalHouseholdSizeString = "HBTotalHouseholdSize";
    private static final String HBAverageHouseholdSizeString = "HBAverageHouseholdSize";
    private static final String CTBTotalHouseholdSizeString = "CTBTotalHouseholdSize";
    private static final String CTBAverageHouseholdSizeString = "CTBAverageHouseholdSize";

    private static String[] TotalAllPassportedStandardIndicatorCountString;
    private static String[] TotalHBPassportedStandardIndicatorCountString;
    private static String[] TotalCTBPassportedStandardIndicatorCountString;
    private static String[] PercentageAllPassportedStandardIndicatorCountString;
    private static String[] PercentageHBPassportedStandardIndicatorCountString;
    private static String[] PercentageCTBPassportedStandardIndicatorCountString;

    private static final String TotalWeeklyHBEntitlementString = "TotalWeeklyHBEntitlement";
    private static final String TotalWeeklyHBEntitlementNonZeroCountString = "TotalWeeklyHBEntitlementNonZeroCount";
    private static final String AverageWeeklyHBEntitlementString = "AverageWeeklyHBEntitlement";
    private static final String TotalWeeklyCTBEntitlementString = "TotalWeeklyCTBEntitlement";
    private static final String TotalWeeklyCTBEntitlementNonZeroCountString = "TotalWeeklyCTBEntitlementNonZeroCount";
    private static final String AverageWeeklyCTBEntitlementString = "AverageWeeklyCTBEntitlement";
    private static final String TotalWeeklyEligibleRentAmountString = "TotalWeeklyEligibleRentAmount";
    private static final String TotalWeeklyEligibleRentAmountNonZeroCountString = "TotalWeeklyEligibleRentAmountNonZeroCount";
    private static final String AverageWeeklyEligibleRentAmountString = "AverageWeeklyEligibleRentAmount";
    private static final String TotalWeeklyEligibleCouncilTaxAmountString = "TotalWeeklyEligibleCouncilTaxAmount";
    private static final String TotalWeeklyEligibleCouncilTaxAmountNonZeroCountString = "TotalWeeklyEligibleCouncilTaxAmountNonZeroCount";
    private static final String AverageWeeklyEligibleCouncilTaxAmountString = "AverageWeeklyEligibleCouncilTaxAmount";
    private static final String TotalContractualRentAmountString = "ContractualRentAmount";
    private static final String TotalContractualRentAmountNonZeroCountString = "ContractualRentAmountNonZeroCount";
    private static final String AverageContractualRentAmountString = "AverageContractualRentAmount";
    private static final String TotalWeeklyAdditionalDiscretionaryPaymentString = "TotalWeeklyAdditionalDiscretionaryPayment";
    private static final String TotalWeeklyAdditionalDiscretionaryPaymentNonZeroCountString = "TotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount";
    private static final String AverageWeeklyAdditionalDiscretionaryPaymentString = "AverageWeeklyAdditionalDiscretionaryPayment";
    private static final String TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityString = "TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    private static final String TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCountString = "TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount";
    private static final String AverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityString = "AverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    private static final String TotalHBClaimantsEmployedString = "HBTotalClaimantsEmployed";
    private static final String PercentageHBClaimantsEmployedString = "HBPercentageClaimantsEmployed";
    private static final String TotalCTBClaimantsEmployedString = "CTBTotalClaimantsEmployed";
    private static final String PercentageCTBClaimantsEmployedString = "CTBPercentageClaimantsEmployed";
    private static final String TotalHBClaimantsSelfEmployedString = "HBTotalClaimantsSelfEmployed";
    private static final String PercentageHBClaimantsSelfEmployedString = "HBPercentageClaimantsSelfEmployed";
    private static final String TotalCTBClaimantsSelfEmployedString = "CTBTotalClaimantsSelfEmployed";
    private static final String PercentageCTBClaimantsSelfEmployedString = "CTBPercentageClaimantsSelfEmployed";
    private static final String TotalHBClaimantsStudentsString = "HBTotalClaimantsStudents";
    private static final String PercentageHBClaimantsStudentsString = "HBPercentageClaimantsStudents";
    private static final String TotalCTBClaimantsStudentsString = "CTBTotalClaimantsStudents";
    private static final String PercentageCTBClaimantsStudentsString = "CTBPercentageClaimantsStudents";
    private static final String TotalHBLHACasesString = "HBTotalLHACases";
    private static final String PercentageHBLHACasesString = "HBPercentageLHACases";
    private static final String TotalCTBLHACasesString = "CTBTotalLHACases";
    private static final String PercentageCTBLHACasesString = "CTBPercentageLHACases";

    private static final String AllCountString = "AllCount";
    private static final String HBCountString = "HBCount";
    private static final String CTBCountString = "CTBOnlyCount";

    private static String[] AllEthnicGroupCountString;
    private static String[] AllTenancyTypeClaimantCountString;
    private static String AllTenancyType8InLS277NSClaimantCountString;
    private static String AllPostcodeValidFormatCountString;
    private static String AllPostcodeValidCountString;

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
    private static String[] HBEthnicGroupCountString;
    private static String[] HBTenancyTypeClaimantCountString;
    private static String HBTenancyType8InLS277NSClaimantCountString;
    private static String HBPostcodeValidFormatCountString;
    private static String HBPostcodeValidCountString;
    // CTB
//    private static String CTBFemaleClaimantCountString;
//    private static String CTBMaleClaimantCountString;
//    private static String CTBDisabledClaimantCountString;
//    private static String CTBFemaleDisabledClaimantCountString;
//    private static String CTBMaleDisabledClaimantCountString;
    private static String[] CTBEthnicGroupCountString;
    private static String[] CTBTenancyTypeClaimantCountString;
    private static String CTBTenancyType8InLS277NSClaimantCountString;
    private static String CTBPostcodeValidFormatCountString;
    private static String CTBPostcodeValidCountString;

    // Compare 2 Times
    // All
    private static String AllTenancyTypeChangeCountString;
    // HB
    private static String HBPostcode0ValidPostcode1ValidCountString;
    private static String HBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString;
    private static String HBPostcode0ValidPostcode1ValidPostcodeChangeCountString;
    private static String HBPostcode0ValidPostcode1NotValidCountString;
    private static String HBPostcode0NotValidPostcode1ValidCountString;
    private static String HBPostcode0NotValidPostcode1NotValidCountString;
    private static String HBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString;
    private static String HBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString;
    private static String HBTenancyTypeChangeCountString;
    private static String HBTenancyTypeChangeHousingCountString;
    private static String HBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString;
    private static String HBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString;
    private static String HBPostcodeChangeWithinSocialTenancyTypesCountString;
    private static String HBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString;
    // CTB
    private static String CTBPostcode0ValidPostcode1ValidCountString;
    private static String CTBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString;
    private static String CTBPostcode0ValidPostcode1ValidPostcodeChangeCountString;
    private static String CTBPostcode0ValidPostcode1NotValidCountString;
    private static String CTBPostcode0NotValidPostcode1ValidCountString;
    private static String CTBPostcode0NotValidPostcode1NotValidCountString;
    private static String CTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString;
    private static String CTBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString;
    private static String CTBTenancyTypeChangeCountString;
    private static String CTBTenancyTypeChangeHousingCountString;
    private static String CTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString;
    private static String CTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString;
    private static String CTBPostcodeChangeWithinSocialTenancyTypesCountString;
    private static String CTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString;

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

    private static int[] TotalAllPassportedStandardIndicatorCount;
    private static int[] TotalHBPassportedStandardIndicatorCount;
    private static int[] TotalCTBPassportedStandardIndicatorCount;

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
    private static int AllTenancyTypeChangeCount;
    // HB
    private static int HBPostcode0ValidPostcode1ValidCount;
    private static int HBPostcode0ValidPostcode1ValidPostcodeNotChangedCount;
    private static int HBPostcode0ValidPostcode1ValidPostcodeChangeCount;
    private static int HBPostcode0ValidPostcode1NotValidCount;
    private static int HBPostcode0NotValidPostcode1ValidCount;
    private static int HBPostcode0NotValidPostcode1NotValidCount;
    private static int HBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount;
    private static int HBPostcode0NotValidPostcode1NotValidPostcodeChangedCount;
    private static int HBTenancyTypeChangeCount;
    private static int HBTenancyTypeChangeHousingCount;
    private static int HBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount;
    private static int HBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount;
    private static int HBPostcodeChangeWithinSocialTenancyTypesCount;
    private static int HBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount;
    // CTB
    private static int CTBPostcode0ValidPostcode1ValidCount;
    private static int CTBPostcode0ValidPostcode1ValidPostcodeNotChangedCount;
    private static int CTBPostcode0ValidPostcode1ValidPostcodeChangeCount;
    private static int CTBPostcode0ValidPostcode1NotValidCount;
    private static int CTBPostcode0NotValidPostcode1ValidCount;
    private static int CTBPostcode0NotValidPostcode1NotValidCount;
    private static int CTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount;
    private static int CTBPostcode0NotValidPostcode1NotValidPostcodeChangedCount;
    private static int CTBTenancyTypeChangeCount;
    private static int CTBTenancyTypeChangeHousingCount;
    private static int CTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount;
    private static int CTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount;
    private static int CTBPostcodeChangeWithinSocialTenancyTypesCount;
    private static int CTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount;

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
        initCompare2TimesStrings(nTT, nEG);
        initCompare3TimesStrings(nTT, nEG);
        TotalAllPassportedStandardIndicatorCount = new int[nPSI];
        TotalHBPassportedStandardIndicatorCount = new int[nPSI];
        TotalCTBPassportedStandardIndicatorCount = new int[nPSI];
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
        for (int i = 1; i < nPSI; i++) {
            TotalAllPassportedStandardIndicatorCountString[i] = "TotalAllPassportedStandardIndicator" + i + "Count";
            TotalHBPassportedStandardIndicatorCountString[i] = "TotalHBPassportedStandardIndicator" + i + "Count";
            TotalCTBPassportedStandardIndicatorCountString[i] = "TotalCTBPassportedStandardIndicator" + i + "Count";
            PercentageAllPassportedStandardIndicatorCountString[i] = "PercentageAllPassportedStandardIndicator" + i + "Count";
            PercentageHBPassportedStandardIndicatorCountString[i] = "PercentageHBPassportedStandardIndicator" + i + "Count";
            PercentageCTBPassportedStandardIndicatorCountString[i] = "PercentageCTBPassportedStandardIndicator" + i + "Count";
        }

        // All
        AllEthnicGroupCountString = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            AllEthnicGroupCountString[i] = "ClaimantEthnicGroup" + i + "Count";
        }
        AllTenancyTypeClaimantCountString = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            AllTenancyTypeClaimantCountString[i] = "AllTenancyType" + i + "ClaimantCount";
        }
        AllTenancyType8InLS277NSClaimantCountString = "AllTenancyType8InLS277NSClaimantCount";
        AllPostcodeValidFormatCountString = "AllPostcodeValidFormatCount";
        AllPostcodeValidCountString = "AllPostcodeValidCount";

        // HB
        HBEthnicGroupCountString = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            HBEthnicGroupCountString[i] = "HBClaimantEthnicGroup" + i + "Count";
        }
        HBTenancyTypeClaimantCountString = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            HBTenancyTypeClaimantCountString[i] = "HBTenancyType" + i + "ClaimantCount";
        }
        HBTenancyType8InLS277NSClaimantCountString = "HBTenancyType8InLS277NSClaimantCount";
        HBPostcodeValidFormatCountString = "HBPostcodeValidFormatCount";
        HBPostcodeValidCountString = "HBPostcodeValidCount";
//        HBFemaleClaimantCountString = "HBFemaleClaimantCount";
//        HBDisabledClaimantCountString = "HBDisabledClaimantCount";
//        HBFemaleDisabledClaimantCountString = "HBDisabledFemaleClaimantCount";
//        HBMaleDisabledClaimantCountString = "HBDisabledMaleClaimantCount";

        // CTB
        CTBEthnicGroupCountString = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            CTBEthnicGroupCountString[i] = "CTBClaimantEthnicGroup" + i + "Count";
        }
        CTBTenancyTypeClaimantCountString = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            CTBTenancyTypeClaimantCountString[i] = "CTBTenancyType" + i + "ClaimantCount";
        }
        CTBTenancyType8InLS277NSClaimantCountString = "CTBTenancyType8InLS277NSClaimantCount";
        CTBPostcodeValidFormatCountString = "CTBPostcodeValidFormatCount";
        CTBPostcodeValidCountString = "CTBPostcodeValidCount";
//        CTBFemaleClaimantCountString = "CTBFemaleClaimantCount";
//        CTBDisabledClaimantCountString = "CTBDisabledClaimantCount";
//        CTBFemaleDisabledClaimantCountString = "CTBDisabledFemaleClaimantCount";
//        CTBMaleDisabledClaimantCountString = "CTBDisabledMaleClaimantCount";

        // Under Occupancy
        TotalRentArrearsString = "TotalRentArrears";
        AverageRentArrearsString = "AverageRentArrears";
        GreaterThan0AverageRentArrearsString = "GreaterThan0AverageRentArrears";
    }

    protected final void initCompare2TimesStrings(int nTT, int nEG) {
        // All
        AllTenancyTypeChangeCountString = "AllTenancyTypeChangeCount";

        // HB
        HBPostcode0ValidPostcode1ValidCountString = "HBPostcode0ValidPostcode1ValidCount";
        HBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString = "HBPostcode0ValidPostcode1ValidPostcodeNotChangedCount";
        HBPostcode0ValidPostcode1ValidPostcodeChangeCountString = "HBPostcode0ValidPostcode1ValidPostcodeChangeCount";
        HBPostcode0ValidPostcode1NotValidCountString = "HBPostcode0ValidPostcode1NotValidCount";
        HBPostcode0NotValidPostcode1ValidCountString = "HBPostcode0NotValidPostcode1ValidCount";
        HBPostcode0NotValidPostcode1NotValidCountString = "HBPostcode0NotValidPostcode1NotValidCount";
        HBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString = "HBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount";
        HBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString = "HBPostcode0NotValidPostcode1NotValidPostcodeChangedCount";

        HBTenancyTypeChangeCountString = "HBTenancyTypeChangeCount";
        HBTenancyTypeChangeHousingCountString = "HBTenancyTypeChangeHousingCount";
        HBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString = "HBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount";
        HBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString = "HBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount";
        HBPostcodeChangeWithinSocialTenancyTypesCountString = "HBPostcodeChangeWithinSocialTenancyTypesCount";
        HBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString = "HBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount";

        // CTB
        CTBPostcode0ValidPostcode1ValidCountString = "CTBPostcode0ValidPostcode1ValidCount";
        CTBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString = "CTBPostcode0ValidPostcode1ValidPostcodeNotChangedCount";
        CTBPostcode0ValidPostcode1ValidPostcodeChangeCountString = "CTBPostcode0ValidPostcode1ValidPostcodeChangeCount";
        CTBPostcode0ValidPostcode1NotValidCountString = "CTBPostcode0ValidPostcode1NotValidCount";
        CTBPostcode0NotValidPostcode1ValidCountString = "CTBPostcode0NotValidPostcode1ValidCount";
        CTBPostcode0NotValidPostcode1NotValidCountString = "CTBPostcode0NotValidPostcode1NotValidCount";
        CTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString = "CTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount";
        CTBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString = "CTBPostcode0NotValidPostcode1NotValidPostcodeChangedCount";

        CTBTenancyTypeChangeCountString = "CTBTenancyTypeChangeCount";
        CTBTenancyTypeChangeHousingCountString = "CTBTenancyTypeChangeHousingCount";
        CTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString = "CTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount";
        CTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString = "CTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount";
        CTBPostcodeChangeWithinSocialTenancyTypesCountString = "CTBPostcodeChangeWithinSocialTenancyTypesCount";
        CTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString = "CTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount";
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
        }

        // All
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
        AllTenancyTypeChangeCount = 0;

        // HB
        HBPostcode0ValidPostcode1ValidCount = 0;
        HBPostcode0ValidPostcode1ValidPostcodeNotChangedCount = 0;
        HBPostcode0ValidPostcode1ValidPostcodeChangeCount = 0;
        HBPostcode0ValidPostcode1NotValidCount = 0;
        HBPostcode0NotValidPostcode1ValidCount = 0;
        HBPostcode0NotValidPostcode1NotValidCount = 0;
        HBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount = 0;
        HBPostcode0NotValidPostcode1NotValidPostcodeChangedCount = 0;
        HBTenancyTypeChangeCount = 0;
        HBTenancyTypeChangeHousingCount = 0;
        HBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount = 0;
        HBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount = 0;
        HBPostcodeChangeWithinSocialTenancyTypesCount = 0;
        HBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount = 0;
        // CTB
        CTBPostcode0ValidPostcode1ValidCount = 0;
        CTBPostcode0ValidPostcode1ValidPostcodeNotChangedCount = 0;
        CTBPostcode0ValidPostcode1ValidPostcodeChangeCount = 0;
        CTBPostcode0ValidPostcode1NotValidCount = 0;
        CTBPostcode0NotValidPostcode1ValidCount = 0;
        CTBPostcode0NotValidPostcode1NotValidCount = 0;
        CTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount = 0;
        CTBPostcode0NotValidPostcode1NotValidPostcodeChangedCount = 0;
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
            String filename) {
        System.out.println("Loading SHBE from " + filename);
        Object[] result = tDW_SHBE_Handler.loadInputData(
                DW_Files.getInputSHBEDir(),
                filename,
                false);
        return result;
    }

    private void addToSummaryCompare2Times(
            int nTT,
            int nEG,
            int nPSI,
            HashMap<String, String> summary) {
        addToSummarySingleTime(nTT, nEG, nPSI, summary);
        //All
        summary.put(
                AllTenancyTypeChangeCountString,
                Integer.toString(AllTenancyTypeChangeCount));
        // HB
        summary.put(
                HBPostcode0ValidPostcode1ValidCountString,
                Integer.toString(HBPostcode0ValidPostcode1ValidCount));
        summary.put(
                HBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString,
                Integer.toString(HBPostcode0ValidPostcode1ValidPostcodeNotChangedCount));
        summary.put(
                HBPostcode0ValidPostcode1ValidPostcodeChangeCountString,
                Integer.toString(HBPostcode0ValidPostcode1ValidPostcodeChangeCount));
        summary.put(
                HBPostcode0ValidPostcode1NotValidCountString,
                Integer.toString(HBPostcode0ValidPostcode1NotValidCount));
        summary.put(
                HBPostcode0NotValidPostcode1ValidCountString,
                Integer.toString(HBPostcode0NotValidPostcode1ValidCount));
        summary.put(
                HBPostcode0NotValidPostcode1NotValidCountString,
                Integer.toString(HBPostcode0NotValidPostcode1NotValidCount));
        summary.put(
                HBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString,
                Integer.toString(HBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount));
        summary.put(
                HBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString,
                Integer.toString(HBPostcode0NotValidPostcode1NotValidPostcodeChangedCount));
        summary.put(
                HBTenancyTypeChangeCountString,
                Integer.toString(HBTenancyTypeChangeCount));
        summary.put(
                HBTenancyTypeChangeHousingCountString,
                Integer.toString(HBTenancyTypeChangeHousingCount));
        summary.put(
                HBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString,
                Integer.toString(HBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount));
        summary.put(
                HBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString,
                Integer.toString(HBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount));
        summary.put(
                HBPostcodeChangeWithinSocialTenancyTypesCountString,
                Integer.toString(HBPostcodeChangeWithinSocialTenancyTypesCount));
        summary.put(
                HBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString,
                Integer.toString(HBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount));
        // CTB
        summary.put(
                CTBPostcode0ValidPostcode1ValidCountString,
                Integer.toString(CTBPostcode0ValidPostcode1ValidCount));
        summary.put(
                CTBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString,
                Integer.toString(CTBPostcode0ValidPostcode1ValidPostcodeNotChangedCount));
        summary.put(
                CTBPostcode0ValidPostcode1ValidPostcodeChangeCountString,
                Integer.toString(CTBPostcode0ValidPostcode1ValidPostcodeChangeCount));
        summary.put(
                CTBPostcode0ValidPostcode1NotValidCountString,
                Integer.toString(CTBPostcode0ValidPostcode1NotValidCount));
        summary.put(
                CTBPostcode0NotValidPostcode1ValidCountString,
                Integer.toString(CTBPostcode0NotValidPostcode1ValidCount));
        summary.put(
                CTBPostcode0NotValidPostcode1NotValidCountString,
                Integer.toString(CTBPostcode0NotValidPostcode1NotValidCount));
        summary.put(
                CTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString,
                Integer.toString(CTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount));
        summary.put(
                CTBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString,
                Integer.toString(CTBPostcode0NotValidPostcode1NotValidPostcodeChangedCount));
        summary.put(
                CTBTenancyTypeChangeCountString,
                Integer.toString(CTBTenancyTypeChangeCount));
        summary.put(
                CTBTenancyTypeChangeHousingCountString,
                Integer.toString(CTBTenancyTypeChangeHousingCount));
        summary.put(
                CTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString,
                Integer.toString(CTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount));
        summary.put(
                CTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString,
                Integer.toString(CTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount));
        summary.put(
                CTBPostcodeChangeWithinSocialTenancyTypesCountString,
                Integer.toString(CTBPostcodeChangeWithinSocialTenancyTypesCount));
        summary.put(
                CTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString,
                Integer.toString(CTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount));
    }

    private void addToSummarySingleTimeRentArrears(
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
                    "0");
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
                    "0");
        }
    }

    private void addToSummarySingleTime(
            int nTT,
            int nEG,
            int nPSI,
            HashMap<String, String> summary) {
        addToSummarySingleTime0(nTT, nEG, nPSI, summary);
        addToSummarySingleTimeDemographics0(nTT, nEG, summary);
        addToSummarySingleTimeTenancyType0(nTT, nEG, summary);
        addToSummarySingleTime1(nTT, nEG, summary);
    }

    private void addToSummarySingleTime0(
            int nTT,
            int nEG,
            int nPSI,
            HashMap<String, String> summary) {
        // All
        double AllCount;
        AllCount = HBCount + CTBCount;
        summary.put(AllCountString, Integer.toString((int) AllCount));
        summary.put(
                AllTotalHouseholdSizeString,
                Long.toString(AllTotalHouseholdSize));
        double ave;
        // All HouseholdSize
        if (AllCount > 0) {
            ave = AllTotalHouseholdSize / AllCount;
            summary.put(
                    AllAverageHouseholdSizeString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    AllAverageHouseholdSizeString,
                    "0");
        }
        // HB HouseholdSize
        summary.put(
                HBTotalHouseholdSizeString,
                Long.toString(HBTotalHouseholdSize));
        if (HBCount > 0) {
            ave = HBTotalHouseholdSize / (double) HBCount;
            summary.put(
                    HBAverageHouseholdSizeString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    HBAverageHouseholdSizeString,
                    "0");
        }
        // CTB HouseholdSize
        summary.put(
                CTBTotalHouseholdSizeString,
                Long.toString(CTBTotalHouseholdSize));
        if (CTBCount > 0) {
            ave = CTBTotalHouseholdSize / (double) CTBCount;
            summary.put(
                    CTBAverageHouseholdSizeString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    CTBAverageHouseholdSizeString,
                    "0");
        }
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
                        "0");
            }
        }
    }

    private void addToSummarySingleTime1(
            int nTT,
            int nEG,
            HashMap<String, String> summary) {
        double ave;
        // WeeklyHBEntitlement
        summary.put(
                TotalWeeklyHBEntitlementString,
                BigDecimal.valueOf(TotalWeeklyHBEntitlement).toPlainString());
        summary.put(
                TotalWeeklyHBEntitlementNonZeroCountString,
                Integer.toString(TotalWeeklyHBEntitlementNonZeroCount));
        if (TotalWeeklyHBEntitlementNonZeroCount > 0) {
            ave = TotalWeeklyHBEntitlement / (double) TotalWeeklyHBEntitlementNonZeroCount;
            summary.put(
                    AverageWeeklyHBEntitlementString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    AverageWeeklyHBEntitlementString,
                    "0");
        }
        // WeeklyCTBEntitlement
        summary.put(
                TotalWeeklyCTBEntitlementString,
                BigDecimal.valueOf(TotalWeeklyCTBEntitlement).toPlainString());
        summary.put(
                TotalWeeklyCTBEntitlementNonZeroCountString,
                Integer.toString(TotalWeeklyCTBEntitlementNonZeroCount));
        if (TotalWeeklyCTBEntitlementNonZeroCount > 0) {
            ave = TotalWeeklyCTBEntitlement / (double) TotalWeeklyCTBEntitlementNonZeroCount;
            summary.put(
                    AverageWeeklyCTBEntitlementString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    AverageWeeklyCTBEntitlementString,
                    "0");
        }
        // WeeklyEligibleRentAmount
        summary.put(
                TotalWeeklyEligibleRentAmountString,
                BigDecimal.valueOf(TotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(
                TotalWeeklyEligibleRentAmountNonZeroCountString,
                Integer.toString(TotalWeeklyEligibleRentAmountNonZeroCount));
        if (TotalWeeklyEligibleRentAmountNonZeroCount > 0) {
            ave = TotalWeeklyEligibleRentAmount / (double) TotalWeeklyEligibleRentAmountNonZeroCount;
            summary.put(
                    AverageWeeklyEligibleRentAmountString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    AverageWeeklyEligibleRentAmountString,
                    "0");
        }
        // WeeklyEligibleCouncilTaxAmount
        summary.put(
                TotalWeeklyEligibleCouncilTaxAmountString,
                BigDecimal.valueOf(TotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(
                TotalWeeklyEligibleCouncilTaxAmountNonZeroCountString,
                Integer.toString(TotalWeeklyEligibleCouncilTaxAmountNonZeroCount));
        if (TotalWeeklyEligibleCouncilTaxAmountNonZeroCount > 0) {
            ave = TotalWeeklyEligibleCouncilTaxAmount / (double) TotalWeeklyEligibleCouncilTaxAmountNonZeroCount;
            summary.put(
                    AverageWeeklyEligibleCouncilTaxAmountString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    AverageWeeklyEligibleCouncilTaxAmountString,
                    "0");
        }
        // ContractualRentAmount
        summary.put(
                TotalContractualRentAmountString,
                BigDecimal.valueOf(TotalContractualRentAmount).toPlainString());
        summary.put(
                TotalContractualRentAmountNonZeroCountString,
                Integer.toString(TotalContractualRentAmountNonZeroCount));
        if (TotalContractualRentAmountNonZeroCount > 0) {
            ave = TotalContractualRentAmount / (double) TotalContractualRentAmountNonZeroCount;
            summary.put(
                    AverageContractualRentAmountString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    AverageContractualRentAmountString,
                    "0");
        }
        // WeeklyAdditionalDiscretionaryPayment
        summary.put(
                TotalWeeklyAdditionalDiscretionaryPaymentString,
                BigDecimal.valueOf(TotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                TotalWeeklyAdditionalDiscretionaryPaymentNonZeroCountString,
                Integer.toString(TotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        if (TotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount > 0) {
            ave = TotalWeeklyAdditionalDiscretionaryPayment / (double) TotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
            summary.put(
                    AverageWeeklyAdditionalDiscretionaryPaymentString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    AverageWeeklyAdditionalDiscretionaryPaymentString,
                    "0");
        }
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        summary.put(
                TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityString,
                BigDecimal.valueOf(TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCountString,
                Integer.toString(TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        if (TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount > 0) {
            ave = TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability / (double) TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
            summary.put(
                    AverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    AverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityString,
                    "0");
        }
        // Employed        
        summary.put(
                TotalHBClaimantsEmployedString,
                Integer.toString(TotalHBClaimantsEmployed));
        if (HBCount > 0) {
            ave = (TotalHBClaimantsEmployed * 100.0d) / (double) HBCount;
            summary.put(
                    PercentageHBClaimantsEmployedString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    PercentageHBClaimantsEmployedString,
                    "0");
        }
        summary.put(
                TotalCTBClaimantsEmployedString,
                Integer.toString(TotalCTBClaimantsEmployed));
        if (CTBCount > 0) {
            ave = (TotalCTBClaimantsEmployed * 100.0d) / (double) CTBCount;
            summary.put(
                    PercentageCTBClaimantsEmployedString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    PercentageCTBClaimantsEmployedString,
                    "0");
        }
        // Self Employed
        summary.put(
                TotalHBClaimantsSelfEmployedString,
                Integer.toString(TotalHBClaimantsSelfEmployed));
        if (HBCount > 0) {
            ave = (TotalHBClaimantsSelfEmployed * 100.0d) / (double) HBCount;
            summary.put(
                    PercentageHBClaimantsSelfEmployedString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    PercentageHBClaimantsSelfEmployedString,
                    "0");
        }
        summary.put(
                TotalCTBClaimantsSelfEmployedString,
                Integer.toString(TotalCTBClaimantsSelfEmployed));
        if (CTBCount > 0) {
            ave = (TotalCTBClaimantsSelfEmployed * 100.0d) / (double) CTBCount;
            summary.put(
                    PercentageCTBClaimantsSelfEmployedString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    PercentageCTBClaimantsSelfEmployedString,
                    "0");
        }
        // Students
        summary.put(
                TotalHBClaimantsStudentsString,
                Integer.toString(TotalHBClaimantsStudents));
        if (HBCount > 0) {
            ave = (TotalHBClaimantsStudents * 100.0d) / (double) HBCount;
            summary.put(
                    PercentageHBClaimantsStudentsString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    PercentageHBClaimantsStudentsString,
                    "0");
        }
        summary.put(
                TotalCTBClaimantsStudentsString,
                Integer.toString(TotalCTBClaimantsStudents));
        if (CTBCount > 0) {
            ave = (TotalCTBClaimantsStudents * 100.0d) / (double) CTBCount;
            summary.put(
                    PercentageCTBClaimantsStudentsString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    PercentageCTBClaimantsStudentsString,
                    "0");
        }
        // LHACases
        summary.put(
                TotalHBLHACasesString,
                Integer.toString(TotalHBLHACases));
        if (HBCount > 0) {
            ave = (TotalHBLHACases * 100.0d) / (double) HBCount;
            summary.put(
                    PercentageHBLHACasesString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    PercentageHBLHACasesString,
                    "0");
        }
        summary.put(
                TotalCTBLHACasesString,
                Integer.toString(TotalCTBLHACases));
        if (CTBCount > 0) {
            ave = (TotalCTBLHACases * 100.0d) / (double) CTBCount;
            summary.put(
                    PercentageCTBLHACasesString,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave), 2, RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    PercentageCTBLHACasesString,
                    "0");
        }
    }

    private void addToSummarySingleTimeDemographics0(
            int nTT,
            int nEG,
            HashMap<String, String> summary) {
        // Ethnicity
        for (int i = 1; i < nEG; i++) {
            summary.put(
                    AllEthnicGroupCountString[i],
                    Integer.toString(HBEthnicGroupCount[i] + CTBEthnicGroupCount[i]));
            summary.put(
                    HBEthnicGroupCountString[i],
                    Integer.toString(HBEthnicGroupCount[i]));
            summary.put(
                    CTBEthnicGroupCountString[i],
                    Integer.toString(CTBEthnicGroupCount[i]));
        }
    }

    private void addToSummarySingleTimeTenancyType0(
            int nTT,
            int nEG,
            HashMap<String, String> summary) {
        // TenancyType
        for (int i = 1; i < nTT; i++) {
            summary.put(
                    AllTenancyTypeClaimantCountString[i],
                    Integer.toString(HBTenancyTypeCount[i] + CTBTenancyTypeCount[i]));
            summary.put(
                    HBTenancyTypeClaimantCountString[i],
                    Integer.toString(HBTenancyTypeCount[i]));
            summary.put(
                    CTBTenancyTypeClaimantCountString[i],
                    Integer.toString(CTBTenancyTypeCount[i]));
        }
        summary.put(
                AllTenancyType8InLS277NSClaimantCountString,
                Integer.toString(HBTenancyType8InLS277NSClaimantCount + CTBTenancyType8InLS277NSClaimantCount));
        summary.put(
                AllPostcodeValidFormatCountString,
                Integer.toString(HBPostcodeValidFormatCount + CTBPostcodeValidFormatCount));
        summary.put(
                AllPostcodeValidCountString,
                Integer.toString(HBPostcodeValidCount + CTBPostcodeValidCount));
        // HB
        summary.put(HBCountString, Integer.toString(HBCount));
        summary.put(
                HBTenancyType8InLS277NSClaimantCountString,
                Integer.toString(HBTenancyType8InLS277NSClaimantCount));
        summary.put(
                HBPostcodeValidFormatCountString,
                Integer.toString(HBPostcodeValidFormatCount));
        summary.put(
                HBPostcodeValidCountString,
                Integer.toString(HBPostcodeValidCount));
        // CTB
        summary.put(CTBCountString, Integer.toString(CTBCount));
        summary.put(
                CTBTenancyType8InLS277NSClaimantCountString,
                Integer.toString(CTBTenancyType8InLS277NSClaimantCount));
        summary.put(
                CTBPostcodeValidFormatCountString,
                Integer.toString(CTBPostcodeValidFormatCount));
        summary.put(
                CTBPostcodeValidCountString,
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
        boolean isCurrentHBClaimInPayment;
        boolean isCurrentCTBClaimInPayment;
        isCurrentHBClaimInPayment = DW_SHBE_Handler.isCurrentHBClaimInPayment(D_Record);
        isCurrentCTBClaimInPayment = DW_SHBE_Handler.isCurrentCTBClaimInPayment(D_Record);
        if (isCurrentHBClaimInPayment || isCurrentCTBClaimInPayment) {
            doSingleTimeCount(
                    D_Record, CTBRef,
                    CTBRefID0,
                    tIDByTenancyType0,
                    tIDByPostcode0,
                    yM30v);
//            String HBRef;
//            HBRef = D_Record.getHousingBenefitClaimReferenceNumber();
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
            //if (HBRef.equalsIgnoreCase(CTBRef)) {
            if (isCurrentHBClaimInPayment) {
                doCompare2TimesHBCount(
                        tenancyType0,
                        postcode0,
                        isValidPostcode1,
                        tenancyType1,
                        postcode1,
                        isValidPostcode0);
            }
            //} else {
            if (isCurrentCTBClaimInPayment) {
                doCompare2TimesCTBCount(
                        tenancyType0,
                        postcode0,
                        isValidPostcode1,
                        tenancyType1,
                        postcode1,
                        isValidPostcode0);
            }
        }
    }

    private void doSingleTimeCount(
            DW_SHBE_D_Record D_Record,
            String CTBRef,
            HashMap<String, DW_ID> CTBRefID0,
            HashMap<DW_ID, Integer> tIDByTenancyType0,
            HashMap<DW_ID, String> tIDByPostcode0,
            String yM30v) {
        boolean isCurrentHBClaimInPayment;
        boolean isCurrentCTBClaimInPayment;
        isCurrentHBClaimInPayment = DW_SHBE_Handler.isCurrentHBClaimInPayment(D_Record);
        isCurrentCTBClaimInPayment = DW_SHBE_Handler.isCurrentCTBClaimInPayment(D_Record);
        if (isCurrentHBClaimInPayment || isCurrentCTBClaimInPayment) {
            int PassportedStandardIndicator;
            PassportedStandardIndicator = D_Record.getPassportedStandardIndicator();
            TotalAllPassportedStandardIndicatorCount[PassportedStandardIndicator]++;
            //String HBRef = D_Record.getHousingBenefitClaimReferenceNumber();
            int ClaimantsEthnicGroup0;
            ClaimantsEthnicGroup0 = D_Record.getClaimantsEthnicGroup();
//        int DisabilityPremiumAwarded0;
//        DisabilityPremiumAwarded0 = D_Record.getDisabilityPremiumAwarded();
//        int SevereDisabilityPremiumAwarded0;
//        SevereDisabilityPremiumAwarded0 = D_Record.getSevereDisabilityPremiumAwarded();
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
            if (isCurrentHBClaimInPayment) {
                TotalHBPassportedStandardIndicatorCount[PassportedStandardIndicator]++;
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
                if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                    TotalHBClaimantsStudents++;
                }
                String LHARegulationsApplied;
                LHARegulationsApplied = D_Record.getLHARegulationsApplied();
                if (LHARegulationsApplied.equalsIgnoreCase("1")
                        || LHARegulationsApplied.equalsIgnoreCase("Yes")) {
                    TotalHBLHACases++;
                }
                doSingleTimeHBCount(
                        ClaimantsEthnicGroup0,
                        tenancyType0,
                        postcode0,
                        yM30v);
            }
            //} else {
            if (isCurrentCTBClaimInPayment) {
                TotalCTBPassportedStandardIndicatorCount[PassportedStandardIndicator]++;
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
                if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                    TotalCTBClaimantsStudents++;
                }
                String LHARegulationsApplied;
                LHARegulationsApplied = D_Record.getLHARegulationsApplied();
                if (LHARegulationsApplied.equalsIgnoreCase("1")) {
                    TotalCTBLHACases++;
                }
                doSingleTimeCTBCount(
                        ClaimantsEthnicGroup0,
                        tenancyType0,
                        postcode0,
                        yM30v);
            }
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
                HBPostcode0ValidPostcode1ValidCount++;
                if (postcode0.equalsIgnoreCase(postcode1)) {
                    HBPostcode0ValidPostcode1ValidPostcodeNotChangedCount++;
                } else {
                    HBPostcode0ValidPostcode1ValidPostcodeChangeCount++;
                }
            } else {
                HBPostcode0ValidPostcode1NotValidCount++;
            }
        } else {
            if (isValidPostcode1) {
                HBPostcode0NotValidPostcode1ValidCount++;
            } else {
                HBPostcode0NotValidPostcode1NotValidCount++;
                if (postcode0 == null) {
                    HBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount++;
                } else {
                    if (postcode1 == null) {
                        HBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount++;
                    } else {
                        if (postcode0.equalsIgnoreCase(postcode1)) {
                            HBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount++;
                        } else {
                            HBPostcode0NotValidPostcode1NotValidPostcodeChangedCount++;
                        }
                    }
                }
            }
        }
        if (tenancyType0.compareTo(tenancyType1) != 0) {
            if (!(tenancyType0 == 0 || tenancyType1 == 0)) {
                HBTenancyTypeChangeCount++;
                AllTenancyTypeChangeCount++;
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

    private void doSingleTimeHBCount(
            int ClaimantsEthnicGroup0,
            Integer tenancyType0,
            String postcode0,
            String yM30v) {
        HBCount++;
        HBEthnicGroupCount[ClaimantsEthnicGroup0]++;
        HBTenancyTypeCount[tenancyType0]++;
        if (tenancyType0 == 8) {
            if (postcode0 != null) {
                if (postcode0.equalsIgnoreCase("LS27 7NS")) {
                    HBTenancyType8InLS277NSClaimantCount++;
                }
            }
        }
        if (postcode0 != null) {
            boolean isValidPostcodeFormat;
            isValidPostcodeFormat = Generic_UKPostcode_Handler.isValidPostcodeForm(postcode0);
            boolean isValidPostcode;
            isValidPostcode = DW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
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
                CTBPostcode0ValidPostcode1ValidCount++;
                if (postcode0.equalsIgnoreCase(postcode1)) {
                    CTBPostcode0ValidPostcode1ValidPostcodeNotChangedCount++;
                } else {
                    CTBPostcode0ValidPostcode1ValidPostcodeChangeCount++;
                }
            } else {
                CTBPostcode0ValidPostcode1NotValidCount++;
            }
        } else {
            if (isValidPostcode1) {
                CTBPostcode0NotValidPostcode1ValidCount++;
            } else {
                CTBPostcode0NotValidPostcode1NotValidCount++;
                if (postcode0 == null) {
                    CTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount++;
                } else {
                    if (postcode1 == null) {
                        CTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount++;
                    } else {
                        if (postcode0.equalsIgnoreCase(postcode1)) {
                            CTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount++;
                        } else {
                            CTBPostcode0NotValidPostcode1NotValidPostcodeChangedCount++;
                        }
                    }
                }
            }
        }
        if (tenancyType0.compareTo(tenancyType1) != 0) {
            if (!(tenancyType0 == 0 || tenancyType1 == 0)) {
                CTBTenancyTypeChangeCount++;
                AllTenancyTypeChangeCount++;
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
            if ((tenancyType0 == 1
                    || tenancyType0 == 4)
                    && tenancyType1 == 3) {
                CTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount++;
            }
            if (tenancyType0 == 3
                    && (tenancyType1 == 1
                    || tenancyType1 == 4)) {
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
        if (tenancyType0 == 3 && tenancyType1 == 3) {
            if (isValidPostcode0 && isValidPostcode1) {
                if (!postcode0.equalsIgnoreCase(postcode1)) {
                    CTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount++;
                }
            }
        }
    }

    private void doSingleTimeCTBCount(
            int ClaimantsEthnicGroup0,
            Integer tenancyType0,
            String postcode0,
            String yM30v) {
        CTBCount++;
        CTBEthnicGroupCount[ClaimantsEthnicGroup0]++;
        CTBTenancyTypeCount[tenancyType0]++;
        if (tenancyType0 == 8) {
            if (postcode0 != null) {
                if (postcode0.equalsIgnoreCase("LS27 7NS")) {
                    CTBTenancyType8InLS277NSClaimantCount++;
                }
            }
        }
        if (postcode0 != null) {
            boolean isValidPostcodeFormat;
            isValidPostcodeFormat = Generic_UKPostcode_Handler.isValidPostcodeForm(postcode0);
            boolean isValidPostcode;
            isValidPostcode = DW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
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
     * @param include
     * @param forceNewSummaries
     * @param nTT
     * @param nEG
     * @return
     */
    public TreeMap<String, HashMap<String, String>> getSummaryTable(
            String[] SHBEFilenames,
            ArrayList<Integer> include,
            boolean forceNewSummaries,
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
        tSHBEData0 = getSHBEData(filename0);
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
                null,
                false,
                forceNewSummaries);
        addToSummarySingleTimeIncomeAndRent(
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
            tSHBEData1 = getSHBEData(filename1);
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
                tSHBEData0 = getSHBEData(filename0);
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
        tSHBEData0 = getSHBEData(filename0);

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
            Object[] tSHBEData1 = getSHBEData(filename1);

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
                filename0, forceNewSummaries, nTT, nEG, nPSI, councilFilenames,
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
                forceNewSummaries, nTT, nEG, nPSI, councilFilenames, RSLFilenames,
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
                councilUnderOccupiedSet,
                true,
                forceNewSummaries);
        addToSummarySingleTimeIncomeAndRent(
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
        addToSummarySingleTimeRentArrears(summary);
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

    private void addToSummarySingleTimeIncomeAndRent(
            HashMap<String, String> summary,
            HashMap<String, BigDecimal> incomeAndRentSummary0) {
        Iterator<String> incomeAndRentSummaryKeySetIte;
        incomeAndRentSummaryKeySetIte = incomeAndRentSummary0.keySet().iterator();
        while (incomeAndRentSummaryKeySetIte.hasNext()) {
            String name;
            name = incomeAndRentSummaryKeySetIte.next();
            String value;
            value = Generic_BigDecimal.roundIfNecessary(incomeAndRentSummary0.get(name), 2, RoundingMode.HALF_UP).toPlainString();
            summary.put(
                    name,
                    value);
        }

    }

    private File getSummaryTableDir(
            String includeKey,
            boolean doUnderOccupancy) {
        File result;
        result = new File(
                DW_Files.getOutputSHBETablesDir(),
                "SummaryTables");
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
     * @param includeKey
     * @param underOccupancy
     * @param nTT
     * @param nEG
     */
    public void writeSummaryTableCompare3Times(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        TreeMap<String, File> ONSPDFiles;
        ONSPDFiles = DW_Postcode_Handler.getONSPDFiles();
        File dirOut;
        dirOut = getSummaryTableDir(includeKey, underOccupancy);
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
     * @param includeKey
     * @param underOccupancy
     * @param nTT
     * @param nEG
     */
    public void writeSummaryTableCompare2Times(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        TreeMap<String, File> ONSPDFiles;
        ONSPDFiles = DW_Postcode_Handler.getONSPDFiles();
        File dirOut;
        dirOut = getSummaryTableDir(includeKey, underOccupancy);
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
        header += SHBEFilename1String + ", ";
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
        header += AllTenancyTypeChangeCountString + ", ";
        header += HBPostcode0ValidPostcode1ValidCountString + ", ";
        header += HBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString + ", ";
        header += HBPostcode0ValidPostcode1ValidPostcodeChangeCountString + ", ";
        header += HBPostcode0ValidPostcode1NotValidCountString + ", ";
        header += HBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString + ", ";
        header += HBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString + ", ";
        header += HBTenancyTypeChangeCountString + ", ";
        header += HBTenancyTypeChangeHousingCountString + ", ";
        header += HBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString + ", ";
        header += HBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString + ", ";
        header += HBPostcodeChangeWithinSocialTenancyTypesCountString + ", ";
        header += HBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString + ", ";
        header += CTBPostcode0ValidPostcode1ValidCountString + ", ";
        header += CTBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString + ", ";
        header += CTBPostcode0ValidPostcode1ValidPostcodeChangeCountString + ", ";
        header += CTBPostcode0ValidPostcode1NotValidCountString + ", ";
        header += CTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString + ", ";
        header += CTBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString + ", ";
        header += CTBTenancyTypeChangeCountString + ", ";
        header += CTBTenancyTypeChangeHousingCountString + ", ";
        header += CTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString + ", ";
        header += CTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString + ", ";
        header += CTBPostcodeChangeWithinSocialTenancyTypesCountString + ", ";
        header += CTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString;
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
            String filename1;
            filename1 = summary.get(SHBEFilename1String);
            line += filename0 + ", " + filename1 + ", ";
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
            line += summary.get(AllTenancyTypeChangeCountString) + ", ";
            line += summary.get(HBPostcode0ValidPostcode1ValidCountString) + ", ";
            line += summary.get(HBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString) + ", ";
            line += summary.get(HBPostcode0ValidPostcode1ValidPostcodeChangeCountString) + ", ";
            line += summary.get(HBPostcode0ValidPostcode1NotValidCountString) + ", ";
            line += summary.get(HBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString) + ", ";
            line += summary.get(HBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString) + ", ";
            line += summary.get(HBTenancyTypeChangeCountString) + ", ";
            line += summary.get(HBTenancyTypeChangeHousingCountString) + ", ";
            line += summary.get(HBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString) + ", ";
            line += summary.get(HBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString) + ", ";
            line += summary.get(HBPostcodeChangeWithinSocialTenancyTypesCountString) + ", ";
            line += summary.get(HBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString) + ", ";
            line += summary.get(CTBPostcode0ValidPostcode1ValidCountString) + ", ";
            line += summary.get(CTBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString) + ", ";
            line += summary.get(CTBPostcode0ValidPostcode1ValidPostcodeChangeCountString) + ", ";
            line += summary.get(CTBPostcode0ValidPostcode1NotValidCountString) + ", ";
            line += summary.get(CTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString) + ", ";
            line += summary.get(CTBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString) + ", ";
            line += summary.get(CTBTenancyTypeChangeCountString) + ", ";
            line += summary.get(CTBTenancyTypeChangeHousingCountString) + ", ";
            line += summary.get(CTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString) + ", ";
            line += summary.get(CTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString) + ", ";
            line += summary.get(CTBPostcodeChangeWithinSocialTenancyTypesCountString) + ", ";
            line += summary.get(CTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTime0(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG,
            int nPSI
    ) {
        TreeMap<String, File> ONSPDFiles;
        ONSPDFiles = DW_Postcode_Handler.getONSPDFiles();
        File dirOut;
        dirOut = getSummaryTableDir(includeKey, underOccupancy);
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
        header += AllTotalHouseholdSizeString + ", ";
        header += AllAverageHouseholdSizeString + ", ";
        header += HBTotalHouseholdSizeString + ", ";
        header += HBAverageHouseholdSizeString + ", ";
        header += CTBTotalHouseholdSizeString + ", ";
        header += CTBAverageHouseholdSizeString + ", ";
        header += AllPostcodeValidFormatCountString + ", ";
        header += AllPostcodeValidCountString + ", ";
        header += HBPostcodeValidFormatCountString + ", ";
        header += HBPostcodeValidCountString + ", ";
        header += CTBPostcodeValidFormatCountString + ", ";
        header += CTBPostcodeValidCountString + ", ";
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
            line += summary.get(AllTotalHouseholdSizeString) + ", ";
            line += summary.get(AllAverageHouseholdSizeString) + ", ";
            line += summary.get(HBTotalHouseholdSizeString) + ", ";
            line += summary.get(HBAverageHouseholdSizeString) + ", ";
            line += summary.get(CTBTotalHouseholdSizeString) + ", ";
            line += summary.get(CTBAverageHouseholdSizeString) + ", ";
            line += summary.get(AllPostcodeValidFormatCountString) + ", ";
            line += summary.get(AllPostcodeValidCountString) + ", ";
            line += summary.get(HBPostcodeValidFormatCountString) + ", ";
            line += summary.get(HBPostcodeValidCountString) + ", ";
            line += summary.get(CTBPostcodeValidFormatCountString) + ", ";
            line += summary.get(CTBPostcodeValidCountString) + ", ";
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
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTime1(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            boolean doUnderOccupancy,
            int nTT,
            int nEG
    ) {
        File dirOut;
        dirOut = getSummaryTableDir(includeKey, doUnderOccupancy);

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
        header += TotalWeeklyHBEntitlementString + ", ";
        header += TotalWeeklyHBEntitlementNonZeroCountString + ", ";
        header += AverageWeeklyHBEntitlementString + ", ";
        header += TotalWeeklyEligibleRentAmountString + ", ";
        header += TotalWeeklyEligibleRentAmountNonZeroCountString + ", ";
        header += AverageWeeklyEligibleRentAmountString + ", ";
        header += TotalWeeklyEligibleRentAmountString + ", ";
        header += TotalWeeklyEligibleRentAmountNonZeroCountString + ", ";
        header += AverageWeeklyEligibleRentAmountString + ", ";
        header += TotalWeeklyEligibleCouncilTaxAmountString + ", ";
        header += TotalWeeklyEligibleCouncilTaxAmountNonZeroCountString + ", ";
        header += AverageWeeklyEligibleCouncilTaxAmountString + ", ";
        header += TotalContractualRentAmountString + ", ";
        header += TotalContractualRentAmountNonZeroCountString + ", ";
        header += AverageContractualRentAmountString + ", ";
        header += TotalWeeklyAdditionalDiscretionaryPaymentString + ", ";
        header += TotalWeeklyAdditionalDiscretionaryPaymentNonZeroCountString + ", ";
        header += AverageWeeklyAdditionalDiscretionaryPaymentString + ", ";
        header += TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityString + ", ";
        header += TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCountString + ", ";
        header += AverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityString + ", ";
        header += TotalHBClaimantsEmployedString + ", ";
        header += PercentageHBClaimantsEmployedString + ", ";
        header += TotalHBClaimantsSelfEmployedString + ", ";
        header += PercentageHBClaimantsSelfEmployedString + ", ";
        header += TotalHBClaimantsStudentsString + ", ";
        header += PercentageHBClaimantsStudentsString + ", ";
        header += TotalHBLHACasesString + ", ";
        header += PercentageHBLHACasesString + ", ";
        header += TotalCTBClaimantsEmployedString + ", ";
        header += PercentageCTBClaimantsEmployedString + ", ";
        header += TotalCTBClaimantsSelfEmployedString + ", ";
        header += PercentageCTBClaimantsSelfEmployedString + ", ";
        header += TotalCTBClaimantsStudentsString + ", ";
        header += PercentageCTBClaimantsStudentsString + ", ";
        header += TotalCTBLHACasesString + ", ";
        header += PercentageCTBLHACasesString + ", ";
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
            line += summary.get(TotalWeeklyHBEntitlementString) + ", ";
            line += summary.get(TotalWeeklyHBEntitlementNonZeroCountString) + ", ";
            line += summary.get(AverageWeeklyHBEntitlementString) + ", ";
            line += summary.get(TotalWeeklyEligibleRentAmountString) + ", ";
            line += summary.get(TotalWeeklyEligibleRentAmountNonZeroCountString) + ", ";
            line += summary.get(AverageWeeklyEligibleRentAmountString) + ", ";
            line += summary.get(TotalWeeklyEligibleRentAmountString) + ", ";
            line += summary.get(TotalWeeklyEligibleRentAmountNonZeroCountString) + ", ";
            line += summary.get(AverageWeeklyEligibleRentAmountString) + ", ";
            line += summary.get(TotalWeeklyEligibleCouncilTaxAmountString) + ", ";
            line += summary.get(TotalWeeklyEligibleCouncilTaxAmountNonZeroCountString) + ", ";
            line += summary.get(AverageWeeklyEligibleCouncilTaxAmountString) + ", ";
            line += summary.get(TotalContractualRentAmountString) + ", ";
            line += summary.get(TotalContractualRentAmountNonZeroCountString) + ", ";
            line += summary.get(AverageContractualRentAmountString) + ", ";
            line += summary.get(TotalWeeklyAdditionalDiscretionaryPaymentString) + ", ";
            line += summary.get(TotalWeeklyAdditionalDiscretionaryPaymentNonZeroCountString) + ", ";
            line += summary.get(AverageWeeklyAdditionalDiscretionaryPaymentString) + ", ";
            line += summary.get(TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityString) + ", ";
            line += summary.get(TotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCountString) + ", ";
            line += summary.get(AverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityString) + ", ";
            line += summary.get(TotalHBClaimantsEmployedString) + ", ";
            line += summary.get(PercentageHBClaimantsEmployedString) + ", ";
            line += summary.get(TotalHBClaimantsSelfEmployedString) + ", ";
            line += summary.get(PercentageHBClaimantsSelfEmployedString) + ", ";
            line += summary.get(TotalHBClaimantsStudentsString) + ", ";
            line += summary.get(PercentageHBClaimantsStudentsString) + ", ";
            line += summary.get(TotalHBLHACasesString) + ", ";
            line += summary.get(PercentageHBLHACasesString) + ", ";
            line += summary.get(TotalCTBClaimantsEmployedString) + ", ";
            line += summary.get(PercentageCTBClaimantsEmployedString) + ", ";
            line += summary.get(TotalCTBClaimantsSelfEmployedString) + ", ";
            line += summary.get(PercentageCTBClaimantsSelfEmployedString) + ", ";
            line += summary.get(TotalCTBClaimantsStudentsString) + ", ";
            line += summary.get(PercentageCTBClaimantsStudentsString) + ", ";
            line += summary.get(TotalCTBLHACasesString) + ", ";
            line += summary.get(PercentageCTBLHACasesString) + ", ";
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
            String includeKey,
            boolean doUnderOccupancy,
            int nTT,
            int nEG
    ) {
        File dirOut;
        dirOut = getSummaryTableDir(includeKey, doUnderOccupancy);

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
        header += TotalWeeklyEligibleRentAmountString + ", ";
        header += TotalWeeklyEligibleRentAmountNonZeroCountString + ", ";
        header += AverageWeeklyEligibleRentAmountString + ", ";
        for (int i = 1; i < nTT; i++) {
            header += TotalIncomeString + "TenancyType" + i + ", ";
            header += TotalIncomeGreaterThanZeroCountString + "TenancyType" + i + ", ";
            header += AverageIncomeString + "TenancyType" + i + ", ";
            header += TotalWeeklyEligibleRentAmountString + "TenancyType" + i + ", ";
            header += TotalWeeklyEligibleRentAmountNonZeroCountString + "TenancyType" + i + ", ";
            header += AverageWeeklyEligibleRentAmountString + "TenancyType" + i + ", ";
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
            line += summary.get(TotalWeeklyEligibleRentAmountString) + ", ";
            line += summary.get(TotalWeeklyEligibleRentAmountNonZeroCountString) + ", ";
            line += summary.get(AverageWeeklyEligibleRentAmountString) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(TotalIncomeString + "TenancyType" + i) + ", ";
                line += summary.get(TotalIncomeGreaterThanZeroCountString + "TenancyType" + i) + ", ";
                line += summary.get(AverageIncomeString + "TenancyType" + i) + ", ";
                line += summary.get(TotalWeeklyEligibleRentAmountString + "TenancyType" + i) + ", ";
                line += summary.get(TotalWeeklyEligibleRentAmountNonZeroCountString + "TenancyType" + i) + ", ";
                line += summary.get(AverageWeeklyEligibleRentAmountString + "TenancyType" + i) + ", ";
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimeDemographics(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        File dirOut;
        dirOut = getSummaryTableDir(includeKey, underOccupancy);

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
            header += AllEthnicGroupCountString[i] + ", ";
        }
        for (int i = 1; i < nEG; i++) {
            header += HBEthnicGroupCountString[i] + ", ";
        }
        for (int i = 1; i < nEG; i++) {
            header += CTBEthnicGroupCountString[i] + ", ";
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
                line += summary.get(AllEthnicGroupCountString[i]) + ", ";
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(HBEthnicGroupCountString[i]) + ", ";
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(CTBEthnicGroupCountString[i]) + ", ";
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimeTenancyType(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        File dirOut;
        dirOut = getSummaryTableDir(includeKey, underOccupancy);

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
            header += AllTenancyTypeClaimantCountString[i] + ", ";
            if (i == 8) {
                header += AllTenancyType8InLS277NSClaimantCountString + ", ";
            }
        }
        for (int i = 1; i < nTT; i++) {
            header += HBTenancyTypeClaimantCountString[i] + ", ";
            if (i == 8) {
                header += HBTenancyType8InLS277NSClaimantCountString + ", ";
            }
        }
        for (int i = 1; i < nTT; i++) {
            header += CTBTenancyTypeClaimantCountString[i] + ", ";
            if (i == 8) {
                header += CTBTenancyType8InLS277NSClaimantCountString + ", ";
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
                line += summary.get(AllTenancyTypeClaimantCountString[i]) + ", ";
                if (i == 8) {
                    line += summary.get(AllTenancyType8InLS277NSClaimantCountString) + ", ";
                }
            }
            for (int i = 1; i < nTT; i++) {
                line += summary.get(HBTenancyTypeClaimantCountString[i]) + ", ";
                if (i == 8) {
                    line += summary.get(HBTenancyType8InLS277NSClaimantCountString) + ", ";
                }
            }
            for (int i = 1; i < nTT; i++) {
                line += summary.get(CTBTenancyTypeClaimantCountString[i]) + ", ";
                if (i == 8) {
                    line += summary.get(CTBTenancyType8InLS277NSClaimantCountString) + ", ";
                }
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
                    + "RSLCount, "
                    + "RSLLinkedRecordsCount, "
                    + "AllCount, "
                    + "AllLinkedRecordsCount";
        } else {
            result += AllCountString + ", ";
            result += HBCountString + ", ";
            result += CTBCountString + ", ";
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
            result += summary.get(AllCountString) + ", ";
            result += summary.get(HBCountString) + ", ";
            result += summary.get(CTBCountString) + ", ";
        }
        return result;
    }
}
