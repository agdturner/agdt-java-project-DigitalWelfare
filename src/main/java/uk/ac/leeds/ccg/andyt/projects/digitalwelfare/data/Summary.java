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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.PostcodeID;
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
//    private static final String councilCountString = "CouncilCount";
//    private static final String RSLCountString = "RSLCount";
    private static final String tAllCountString = "AllCount";
    private static final String tHBCountString = "HBCount";
    private static final String tCTBCountString = "CTBOnlyCount";

    private static String[] tAllEthnicGroupCountString;
    private static String[] tAllTenancyTypeClaimantCountString;
    private static String tAllTenancyType8InLS277NSClaimantCountString;
    private static String tAllPostcodeValidFormatCountString;
    private static String tAllPostcodeValidCountString;

    private static final String tTotalIncomeString = "TotalIncome";
    private static final String tTotalIncomeGreaterThanZeroCountString = "TotalIncomeGreaterThanZeroCount";
    private static final String tAverageIncomeString = "AverageIncome";
    private static final String tTotalWeeklyEligibleRentAmountString = "TotalWeeklyEligibleRentAmount";
    private static final String tTotalWeeklyEligibleRentAmountGreaterThanZeroCountString = "TotalWeeklyEligibleRentAmountGreaterThanZeroCount";
    private static final String tAverageWeeklyEligibleRentAmountString = "AverageWeeklyEligibleRentAmount";

    private static final String SHBEFilename00String = "SHBEFilename00";
    private static final String SHBEFilename0String = "SHBEFilename0";
    private static final String SHBEFilename1String = "SHBEFilename1";
    private static final String CouncilFilename00String = "CouncilFilename00";
    private static final String CouncilFilename0String = "CouncilFilename0";
    private static final String CouncilFilename1String = "CouncilFilename1";
    private static final String RSLFilename00String = "RSLFilename00";
    private static final String RSLFilename0String = "RSLFilename0";
    private static final String RSLFilename1String = "RSLFilename1";

    private static final String tAllCount00String = "AllCount00";
    private static final String tAllCount0String = "AllCount0";
    private static final String tAllCount1String = "AllCount1";
    private static final String tCouncilCount00String = "CouncilCount00";
    private static final String tCouncilCount0String = "CouncilCount0";
    private static final String tCouncilCount1String = "CouncilCount1";
    private static final String tRSLCount00String = "RSLCount00";
    private static final String tRSLCount0String = "RSLCount0";
    private static final String tRSLCount1String = "RSLCount1";
    private static final String tAllLinkedRecordCount00String = "AllLinkedRecordCount00";
    private static final String tAllLinkedRecordCount0String = "AllLinkedRecordCount0";
    private static final String tAllLinkedRecordCount1String = "AllLinkedRecordCount1";
    private static final String tCouncilLinkedRecordCount00String = "CouncilLinkedRecordCount00";
    private static final String tCouncilLinkedRecordCount0String = "CouncilLinkedRecordCount0";
    private static final String tCouncilLinkedRecordCount1String = "CouncilLinkedRecordCount1";
    private static final String tRSLLinkedRecordCount00String = "RSLLinkedRecordCount00";
    private static final String tRSLLinkedRecordCount0String = "RSLLinkedRecordCount0";
    private static final String tRSLLinkedRecordCount1String = "RSLLinkedRecordCount1";

    // HB
//    private static String tHBFemaleClaimantCountString;
//    private static String tHBMaleClaimantCountString;
//    private static String tHBDisabledClaimantCountString;
//    private static String tHBFemaleDisabledClaimantCountString;
//    private static String tHBMaleDisabledClaimantCountString;
    private static String[] tHBEthnicGroupCountString;
    private static String[] tHBTenancyTypeClaimantCountString;
    private static String tHBTenancyType8InLS277NSClaimantCountString;
    private static String tHBPostcodeValidFormatCountString;
    private static String tHBPostcodeValidCountString;
    // CTB
//    private static String tCTBFemaleClaimantCountString;
//    private static String tCTBMaleClaimantCountString;
//    private static String tCTBDisabledClaimantCountString;
//    private static String tCTBFemaleDisabledClaimantCountString;
//    private static String tCTBMaleDisabledClaimantCountString;
    private static String[] tCTBEthnicGroupCountString;
    private static String[] tCTBTenancyTypeClaimantCountString;
    private static String tCTBTenancyType8InLS277NSClaimantCountString;
    private static String tCTBPostcodeValidFormatCountString;
    private static String tCTBPostcodeValidCountString;

    // Under Occupancy Only
    private static String totalRentArrearsString;
    private static String averageRentArrearsString;
    private static String greaterThan0AverageRentArrearsString;

    // 2 Comparisons
    // All
    // HB
    private static String tHBPostcode0ValidPostcode1ValidCountString;
    private static String tHBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString;
    private static String tHBPostcode0ValidPostcode1ValidPostcodeChangeCountString;
    private static String tHBPostcode0ValidPostcode1NotValidCountString;
    private static String tHBPostcode0NotValidPostcode1ValidCountString;
    private static String tHBPostcode0NotValidPostcode1NotValidCountString;
    private static String tHBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString;
    private static String tHBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString;
    private static String tHBTenancyTypeChangeCountString;
    private static String tHBTenancyTypeChangeHousingCountString;
    private static String tHBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString;
    private static String tHBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString;
    private static String tHBPostcodeChangeWithinSocialTenancyTypesCountString;
    private static String tHBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString;
    // CTB
    private static String tCTBPostcode0ValidPostcode1ValidCountString;
    private static String tCTBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString;
    private static String tCTBPostcode0ValidPostcode1ValidPostcodeChangeCountString;
    private static String tCTBPostcode0ValidPostcode1NotValidCountString;
    private static String tCTBPostcode0NotValidPostcode1ValidCountString;
    private static String tCTBPostcode0NotValidPostcode1NotValidCountString;
    private static String tCTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString;
    private static String tCTBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString;
    private static String tCTBTenancyTypeChangeCountString;
    private static String tCTBTenancyTypeChangeHousingCountString;
    private static String tCTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString;
    private static String tCTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString;
    private static String tCTBPostcodeChangeWithinSocialTenancyTypesCountString;
    private static String tCTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString;

    // 3 Comparisons
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
    // Single
    // All
    //private static int[] tAllTenancyTypeClaimantCount;
    // HB
    private static int tHBCount;
    private static int[] tHBTenancyTypeCount;
    private static int[] tHBEthnicGroupCount;
    private static int tHBTenancyType8InLS277NSClaimantCount;
    private static int tHBPostcodeValidFormatCount;
    private static int tHBPostcodeValidCount;
    // CTB
    private static int tCTBCount;
    private static int[] tCTBTenancyTypeCount;
    private static int[] tCTBEthnicGroupCount;
    private static int tCTBTenancyType8InLS277NSClaimantCount;
    private static int tCTBPostcodeValidFormatCount;
    private static int tCTBPostcodeValidCount;
    // Other summary stats
    private static double totalRentArrears;
    private static double rentArrearsCount;
    private static double greaterThan0RentArrearsCount;
    private static int tDRecordsCTBRefDW_SHBE_RecordNullCount;

    // 2 Comparisons
    // HB
    private static int tHBPostcode0ValidPostcode1ValidCount;
    private static int tHBPostcode0ValidPostcode1ValidPostcodeNotChangedCount;
    private static int tHBPostcode0ValidPostcode1ValidPostcodeChangeCount;
    private static int tHBPostcode0ValidPostcode1NotValidCount;
    private static int tHBPostcode0NotValidPostcode1ValidCount;
    private static int tHBPostcode0NotValidPostcode1NotValidCount;
    private static int tHBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount;
    private static int tHBPostcode0NotValidPostcode1NotValidPostcodeChangedCount;
    private static int tHBTenancyTypeChangeCount;
    private static int tHBTenancyTypeChangeHousingCount;
    private static int tHBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount;
    private static int tHBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount;
    private static int tHBPostcodeChangeWithinSocialTenancyTypesCount;
    private static int tHBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount;
    // CTB
    private static int tCTBPostcode0ValidPostcode1ValidCount;
    private static int tCTBPostcode0ValidPostcode1ValidPostcodeNotChangedCount;
    private static int tCTBPostcode0ValidPostcode1ValidPostcodeChangeCount;
    private static int tCTBPostcode0ValidPostcode1NotValidCount;
    private static int tCTBPostcode0NotValidPostcode1ValidCount;
    private static int tCTBPostcode0NotValidPostcode1NotValidCount;
    private static int tCTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount;
    private static int tCTBPostcode0NotValidPostcode1NotValidPostcodeChangedCount;
    private static int tCTBTenancyTypeChangeCount;
    private static int tCTBTenancyTypeChangeHousingCount;
    private static int tCTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount;
    private static int tCTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount;
    private static int tCTBPostcodeChangeWithinSocialTenancyTypesCount;
    private static int tCTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount;

    private static int tAllCount00;
    private static int tAllCount0;
    private static int tAllCount1;
    private static int tCouncilCount00;
    private static int tCouncilCount0;
    private static int tCouncilCount1;
    private static int tRSLCount00;
    private static int tRSLCount0;
    private static int tRSLCount1;
    private static int tAllLinkedRecordCount00;
    private static int tAllLinkedRecordCount0;
    private static int tAllLinkedRecordCount1;
    private static int tCouncilLinkedRecordCount00;
    private static int tCouncilLinkedRecordCount0;
    private static int tCouncilLinkedRecordCount1;
    private static int tRSLLinkedRecordCount00;
    private static int tRSLLinkedRecordCount0;
    private static int tRSLLinkedRecordCount1;

    // 3 Comparisons
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

    public Summary(DW_SHBE_Handler tDW_SHBE_Handler, int nTT, int nEG) {
        this.tDW_SHBE_Handler = tDW_SHBE_Handler;
        init(nTT, nEG);
    }

    protected final void init(int nTT, int nEG) {
        initSingleTimeStrings(nTT, nEG);
        initCompare2TimesStrings(nTT, nEG);
        initCompare3TimesStrings(nTT, nEG);
        tHBEthnicGroupCount = new int[nEG];
        tCTBEthnicGroupCount = new int[nEG];
        tHBTenancyTypeCount = new int[nTT];
        tCTBTenancyTypeCount = new int[nTT];
    }

    protected final void initSingleTimeStrings(int nTT, int nEG) {
        // All
        tAllEthnicGroupCountString = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            tAllEthnicGroupCountString[i] = "ClaimantEthnicGroup" + i + "Count";
        }
        tAllTenancyTypeClaimantCountString = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            tAllTenancyTypeClaimantCountString[i] = "AllTenancyType" + i + "ClaimantCount";
        }
        tAllTenancyType8InLS277NSClaimantCountString = "AllTenancyType8InLS277NSClaimantCount";
        tAllPostcodeValidFormatCountString = "AllPostcodeValidFormatCount";
        tAllPostcodeValidCountString = "AllPostcodeValidCount";

        // HB
        tHBEthnicGroupCountString = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            tHBEthnicGroupCountString[i] = "HBClaimantEthnicGroup" + i + "Count";
        }
        tHBTenancyTypeClaimantCountString = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            tHBTenancyTypeClaimantCountString[i] = "HBTenancyType" + i + "ClaimantCount";
        }
        tHBTenancyType8InLS277NSClaimantCountString = "HBTenancyType8InLS277NSClaimantCount";
        tHBPostcodeValidFormatCountString = "HBPostcodeValidFormatCount";
        tHBPostcodeValidCountString = "HBPostcodeValidCount";
//        tHBFemaleClaimantCountString = "HBFemaleClaimantCount";
//        tHBDisabledClaimantCountString = "HBDisabledClaimantCount";
//        tHBFemaleDisabledClaimantCountString = "HBDisabledFemaleClaimantCount";
//        tHBMaleDisabledClaimantCountString = "HBDisabledMaleClaimantCount";

        // CTB
        tCTBEthnicGroupCountString = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            tCTBEthnicGroupCountString[i] = "CTBClaimantEthnicGroup" + i + "Count";
        }
        tCTBTenancyTypeClaimantCountString = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            tCTBTenancyTypeClaimantCountString[i] = "CTBTenancyType" + i + "ClaimantCount";
        }
        tCTBTenancyType8InLS277NSClaimantCountString = "CTBTenancyType8InLS277NSClaimantCount";
        tCTBPostcodeValidFormatCountString = "CTBPostcodeValidFormatCount";
        tCTBPostcodeValidCountString = "CTBPostcodeValidCount";
//        tCTBFemaleClaimantCountString = "CTBFemaleClaimantCount";
//        tCTBDisabledClaimantCountString = "CTBDisabledClaimantCount";
//        tCTBFemaleDisabledClaimantCountString = "CTBDisabledFemaleClaimantCount";
//        tCTBMaleDisabledClaimantCountString = "CTBDisabledMaleClaimantCount";

        // Under Occupancy
        totalRentArrearsString = "TotalRentArrears";
        averageRentArrearsString = "AverageRentArrears";
        greaterThan0AverageRentArrearsString = "GreaterThan0AverageRentArrears";
    }

    protected final void initCompare2TimesStrings(int nTT, int nEG) {
        // HB
        tHBPostcode0ValidPostcode1ValidCountString = "HBPostcode0ValidPostcode1ValidCount";
        tHBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString = "HBPostcode0ValidPostcode1ValidPostcodeNotChangedCount";
        tHBPostcode0ValidPostcode1ValidPostcodeChangeCountString = "HBPostcode0ValidPostcode1ValidPostcodeChangeCount";
        tHBPostcode0ValidPostcode1NotValidCountString = "HBPostcode0ValidPostcode1NotValidCount";
        tHBPostcode0NotValidPostcode1ValidCountString = "HBPostcode0NotValidPostcode1ValidCount";
        tHBPostcode0NotValidPostcode1NotValidCountString = "HBPostcode0NotValidPostcode1NotValidCount";
        tHBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString = "HBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount";
        tHBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString = "HBPostcode0NotValidPostcode1NotValidPostcodeChangedCount";

        tHBTenancyTypeChangeCountString = "HBTenancyTypeChangeCount";
        tHBTenancyTypeChangeHousingCountString = "HBTenancyTypeChangeHousingCount";
        tHBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString = "HBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount";
        tHBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString = "HBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount";
        tHBPostcodeChangeWithinSocialTenancyTypesCountString = "HBPostcodeChangeWithinSocialTenancyTypesCount";
        tHBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString = "HBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount";

        // CTB
        tCTBPostcode0ValidPostcode1ValidCountString = "CTBPostcode0ValidPostcode1ValidCount";
        tCTBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString = "CTBPostcode0ValidPostcode1ValidPostcodeNotChangedCount";
        tCTBPostcode0ValidPostcode1ValidPostcodeChangeCountString = "CTBPostcode0ValidPostcode1ValidPostcodeChangeCount";
        tCTBPostcode0ValidPostcode1NotValidCountString = "CTBPostcode0ValidPostcode1NotValidCount";
        tCTBPostcode0NotValidPostcode1ValidCountString = "CTBPostcode0NotValidPostcode1ValidCount";
        tCTBPostcode0NotValidPostcode1NotValidCountString = "CTBPostcode0NotValidPostcode1NotValidCount";
        tCTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString = "CTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount";
        tCTBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString = "CTBPostcode0NotValidPostcode1NotValidPostcodeChangedCount";

        tCTBTenancyTypeChangeCountString = "CTBTenancyTypeChangeCount";
        tCTBTenancyTypeChangeHousingCountString = "CTBTenancyTypeChangeHousingCount";
        tCTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString = "CTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount";
        tCTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString = "CTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount";
        tCTBPostcodeChangeWithinSocialTenancyTypesCountString = "CTBPostcodeChangeWithinSocialTenancyTypesCount";
        tCTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString = "CTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount";
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

    private void initCounts(int nTT, int nEG) {
        initSingleTimeCounts(nTT, nEG);
        initCompare2TimesCounts(nTT, nEG);
        initCompare3TimesCounts(nTT, nEG);
    }

    private void initSingleTimeCounts(int nTT, int nEG) {
        // HB
        for (int i = 1; i < nEG; i++) {
            tHBEthnicGroupCount[i] = 0;
        }
        for (int i = 1; i < nTT; i++) {
            tHBTenancyTypeCount[i] = 0;
        }
        tHBCount = 0;
        tHBTenancyType8InLS277NSClaimantCount = 0;
        tHBPostcodeValidFormatCount = 0;
        tHBPostcodeValidCount = 0;
        // CTB
        for (int i = 1; i < nEG; i++) {
            tCTBEthnicGroupCount[i] = 0;
        }
        for (int i = 1; i < nTT; i++) {
            tCTBTenancyTypeCount[i] = 0;
        }
        tCTBCount = 0;
        tCTBTenancyType8InLS277NSClaimantCount = 0;
        tCTBPostcodeValidFormatCount = 0;
        tCTBPostcodeValidCount = 0;

//        tAllCount00 = 0;
//        tAllCount0 = 0;
        tAllCount1 = 0;
//        tCouncilCount00 = 0;
//        tCouncilCount0 = 0;
        tCouncilCount1 = 0;
//        tRSLCount00 = 0;
//        tRSLCount0 = 0;
        tRSLCount1 = 0;
//        tAllLinkedRecordCount00 = 0;
//        tAllLinkedRecordCount0 = 0;
        tAllLinkedRecordCount1 = 0;
//        tCouncilLinkedRecordCount00 = 0;
//        tCouncilLinkedRecordCount0 = 0;
        tCouncilLinkedRecordCount1 = 0;
//        tRSLLinkedRecordCount00 = 0;
//        tRSLLinkedRecordCount0 = 0;
        tRSLLinkedRecordCount1 = 0;
    }

    private void initCompare2TimesCounts(int nTT, int nEG) {
        // HB
        tHBPostcode0ValidPostcode1ValidCount = 0;
        tHBPostcode0ValidPostcode1ValidPostcodeNotChangedCount = 0;
        tHBPostcode0ValidPostcode1ValidPostcodeChangeCount = 0;
        tHBPostcode0ValidPostcode1NotValidCount = 0;
        tHBPostcode0NotValidPostcode1ValidCount = 0;
        tHBPostcode0NotValidPostcode1NotValidCount = 0;
        tHBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount = 0;
        tHBPostcode0NotValidPostcode1NotValidPostcodeChangedCount = 0;
        tHBTenancyTypeChangeCount = 0;
        tHBTenancyTypeChangeHousingCount = 0;
        tHBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount = 0;
        tHBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount = 0;
        tHBPostcodeChangeWithinSocialTenancyTypesCount = 0;
        tHBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount = 0;
        // CTB
        tCTBPostcode0ValidPostcode1ValidCount = 0;
        tCTBPostcode0ValidPostcode1ValidPostcodeNotChangedCount = 0;
        tCTBPostcode0ValidPostcode1ValidPostcodeChangeCount = 0;
        tCTBPostcode0ValidPostcode1NotValidCount = 0;
        tCTBPostcode0NotValidPostcode1ValidCount = 0;
        tCTBPostcode0NotValidPostcode1NotValidCount = 0;
        tCTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount = 0;
        tCTBPostcode0NotValidPostcode1NotValidPostcodeChangedCount = 0;
        tCTBTenancyTypeChangeCount = 0;
        tCTBTenancyTypeChangeHousingCount = 0;
        tCTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount = 0;
        tCTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount = 0;
        tCTBPostcodeChangeWithinSocialTenancyTypesCount = 0;
        tCTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount = 0;
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
     * result[10] is a HashMap<String, DW_ID> tCTBRefToClaimantIDLookup;
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

    /**
     *
     * @param SHBEFilenames
     * @param include
     * @param underOccupiedData
     * @param nTT
     * @param nEG
     * @return
     */
    @Deprecated
    public TreeMap<String, HashMap<String, String>> getSummaryTable(
            String[] SHBEFilenames,
            ArrayList<Integer> include,
            Object[] underOccupiedData,
            int nTT,
            int nEG
    ) {
        // Initialise summaryTable
        TreeMap<String, HashMap<String, String>> result;
        result = new TreeMap<String, HashMap<String, String>>();
        Object[] filenames;
        filenames = DW_UnderOccupiedReport_Handler.getFilenames();
        TreeMap<String, String> councilFilenames;
        TreeMap<String, String> RSLFilenames;
        councilFilenames = (TreeMap<String, String>) filenames[0];
        RSLFilenames = (TreeMap<String, String>) filenames[1];
        TreeMap<String, DW_UnderOccupiedReport_Set> councilUnderOccupiedSets = null;
        TreeMap<String, DW_UnderOccupiedReport_Set> RSLUnderOccupiedSets = null;
        councilUnderOccupiedSets = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[0];
        RSLUnderOccupiedSets = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[1];
        Iterator<Integer> includeIte;
        includeIte = include.iterator();
        int i;
        while (includeIte.hasNext()) {
            i = includeIte.next();
            String yM30;
            yM30 = DW_SHBE_Handler.getYM3(SHBEFilenames[i]);
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet0;
            councilUnderOccupiedSet0 = councilUnderOccupiedSets.get(yM30);
            if (councilUnderOccupiedSet0 != null) {
                HashMap<String, String> summary;
                summary = new HashMap<String, String>();
                String key;
                key = DW_SHBE_Handler.getYearMonthNumber(SHBEFilenames[i]);
                result.put(key, summary);
            }
        }
        includeIte = include.iterator();
        //int i = 0;
        DW_UnderOccupiedReport_Set councilUnderOccupiedSet0;
        DW_UnderOccupiedReport_Set RSLUnderOccupiedSet0;
        Object[] tSHBEData0;
        String key;
        HashMap<String, String> summary;
        HashMap<String, DW_ID> tCTBRefID0 = null;
        TreeMap<String, DW_SHBE_Record> tDRecords0 = null;
        HashMap<DW_ID, String> tIDByPostcode0 = null;
        HashMap<DW_ID, Integer> tIDByTenancyType0 = null;
        String filename0 = null;
        boolean initFirst = false;
        String yM30 = "";
        String yM30v = "";
        while (!initFirst) {
            i = includeIte.next();
            yM30 = DW_SHBE_Handler.getYM3(SHBEFilenames[i]);
            councilUnderOccupiedSet0 = councilUnderOccupiedSets.get(yM30);
            if (councilUnderOccupiedSet0 != null) {
                RSLUnderOccupiedSet0 = RSLUnderOccupiedSets.get(yM30);
                // Load first SHBEData
                filename0 = SHBEFilenames[i];
                tSHBEData0 = getSHBEData(filename0);
                /*
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
                 * result[10] is a HashMap<String, DW_ID> tCTBRefToClaimantIDLookup;
                 * result[11] is a HashMap<DW_ID, String> tClaimantIDToCTBRefLookup;
                 * result[12] is a HashMap<String, Integer> tLoadSummary;
                 * result[13] is a HashSet<ID_TenancyType> tClaimantIDAndPostcode.
                 * result[14] is a HashSet<ID_TenancyType> tClaimantIDAndTenancyType.
                 * result[15] is a HashSet<ID_TenancyType> tClaimantIDAndPostcodeAndTenancyType.
                 */
                tDRecords0 = (TreeMap<String, DW_SHBE_Record>) tSHBEData0[0];
                tIDByPostcode0 = (HashMap<DW_ID, String>) tSHBEData0[8];
                //tIDByPostcode0 = loadIDByPostcode(loadData, filename, i);
                tIDByTenancyType0 = (HashMap<DW_ID, Integer>) tSHBEData0[9];
                //tIDByTenancyType0 = loadIDByTenancyType(loadData, filename, i);
                tCTBRefID0 = (HashMap<String, DW_ID>) tSHBEData0[10];
                //tCTBRefID0 = loadCTBRefByID(loadData, filename, i);
                key = DW_SHBE_Handler.getYearMonthNumber(SHBEFilenames[i]);
                summary = result.get(key);
                yM30v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                        DW_SHBE_Handler.getYM3(filename0));
                // Set Counters to 0
                initCounts(nTT, nEG);
                TreeMap<String, DW_UnderOccupiedReport_Record> councilUnderOccupiedSet0Map;
                councilUnderOccupiedSet0Map = councilUnderOccupiedSet0.getMap();
                TreeMap<String, DW_UnderOccupiedReport_Record> RSLUnderOccupiedSet0Map;
                RSLUnderOccupiedSet0Map = RSLUnderOccupiedSet0.getMap();
                // Loop over underoccupancy data
                // Loop over Council
                tCouncilLinkedRecordCount0 = doSingleTimeLoopOverSet(
                        councilUnderOccupiedSet0Map,
                        tDRecords0, tCTBRefID0, tIDByTenancyType0,
                        tIDByPostcode0, yM30v);
                // Loop over RSL
                doSingleTimeLoopOverSet(
                        RSLUnderOccupiedSet0Map, tDRecords0,
                        tCTBRefID0, tIDByTenancyType0, tIDByPostcode0, yM30v);
                // All
//                summary.put(SHBEFilename1String, filename0); // This looks wierd but is right!
//                summary.put(CouncilFilename1String, councilFilenames.get(yM30));
//
//                summary.put(CouncilFilename, councilFilenames.get(yM30));
//
//                summary.put("RSLFilename", RSLFilenames.get(yM30));

//                summary.put(councilCountString, Integer.toString(councilUnderOccupiedSet0Map.size()));
//                summary.put(RSLCountString, Integer.toString(RSLUnderOccupiedSet0Map.size()));
                addToSummarySingleTime(nTT, nEG, summary);
                addToSummarySingleTimeRentArrears(summary);
                //tDRecordsCTBRefDW_SHBE_RecordNullCount
                initFirst = true;
            }
        }
        while (includeIte.hasNext()) {
            i = includeIte.next();
            String yM31;
            yM31 = DW_SHBE_Handler.getYM3(SHBEFilenames[i]);
            String yM31v;
            yM31v = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
            DW_UnderOccupiedReport_Set councilUnderOccupiedSet1;
            councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(yM31);
            if (councilUnderOccupiedSet1 != null) {
                // Load next data
                String filename1;
                filename1 = SHBEFilenames[i];
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
                 * result[10] is a HashMap<String, DW_ID> tCTBRefToClaimantIDLookup;
                 * result[11] is a HashMap<DW_ID, String> tClaimantIDToCTBRefLookup;
                 * result[12] is a HashMap<String, Integer> tLoadSummary;
                 * result[13] is a HashSet<ID_TenancyType> tClaimantIDAndPostcode.
                 * result[14] is a HashSet<ID_TenancyType> tClaimantIDAndTenancyType.
                 * result[15] is a HashSet<ID_TenancyType> tClaimantIDAndPostcodeAndTenancyType.
                 */
                TreeMap<String, DW_SHBE_Record> tDRecords1;
                tDRecords1 = (TreeMap<String, DW_SHBE_Record>) tSHBEData1[0];
                HashMap<DW_ID, String> tIDByPostcode1;
                tIDByPostcode1 = (HashMap<DW_ID, String>) tSHBEData1[8];
                HashMap<DW_ID, Integer> tIDByTenancyType1;
                tIDByTenancyType1 = (HashMap<DW_ID, Integer>) tSHBEData1[9];
                HashMap<String, DW_ID> tCTBRefID1;
                tCTBRefID1 = (HashMap<String, DW_ID>) tSHBEData1[10];
                key = DW_SHBE_Handler.getYearMonthNumber(SHBEFilenames[i]);
                summary = result.get(key);
                initCounts(nTT, nEG);

                DW_UnderOccupiedReport_Set RSLUnderOccupiedSet1;
                RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(yM31);
                TreeMap<String, DW_UnderOccupiedReport_Record> councilUnderOccupiedSet1Map;
                councilUnderOccupiedSet1Map = councilUnderOccupiedSet1.getMap();
                TreeMap<String, DW_UnderOccupiedReport_Record> RSLUnderOccupiedSet1Map;
                RSLUnderOccupiedSet1Map = RSLUnderOccupiedSet1.getMap();
                // Loop over underoccupancy data
                // Loop over Council
                doCompare2TimesLoopOverSet(
                        councilUnderOccupiedSet1Map,
                        tDRecords0,
                        tCTBRefID0,
                        tCTBRefID1,
                        tIDByTenancyType0,
                        tIDByTenancyType1,
                        tIDByPostcode0,
                        tIDByPostcode1,
                        yM30v,
                        yM31v);
                // Loop over RSL
                doCompare2TimesLoopOverSet(
                        RSLUnderOccupiedSet1Map,
                        tDRecords0,
                        tCTBRefID0,
                        tCTBRefID1,
                        tIDByTenancyType0,
                        tIDByTenancyType1,
                        tIDByPostcode0,
                        tIDByPostcode1,
                        yM30v,
                        yM31v);
                // All
                summary.put("SHBEFilename0", filename0);
                summary.put("SHBEFilename1", filename1);
                summary.put("CouncilFilename0", councilFilenames.get(yM30));
                summary.put("RSLFilename0", RSLFilenames.get(yM30));
                summary.put("CouncilCount0", Integer.toString(councilUnderOccupiedSet1Map.size()));
                summary.put("RSLCount0", Integer.toString(RSLUnderOccupiedSet1Map.size()));
                summary.put("CouncilFilename1", councilFilenames.get(yM31));
                summary.put("RSLFilename1", RSLFilenames.get(yM31));
                summary.put("CouncilCount1", Integer.toString(councilUnderOccupiedSet1Map.size()));
                summary.put("RSLCount1", Integer.toString(RSLUnderOccupiedSet1Map.size()));
                addToSummaryCompare2Times(nTT, nEG, summary);
                addToSummarySingleTimeRentArrears(summary);
                //tDRecordsCTBRefDW_SHBE_RecordNullCount
                yM30 = yM31;
                yM30v = yM31v;
                filename0 = filename1;
                tSHBEData0 = tSHBEData1;
                tDRecords0 = tDRecords1;
                tIDByPostcode0 = tIDByPostcode1;
                tIDByTenancyType0 = tIDByTenancyType1;
                tCTBRefID0 = tCTBRefID1;
            }
        }
        return result;
    }

    private void addToSummaryCompare2Times(
            int nTT,
            int nEG,
            HashMap<String, String> summary) {
        addToSummarySingleTime(nTT, nEG, summary);
        // HB
        summary.put(
                tHBPostcode0ValidPostcode1ValidCountString,
                Integer.toString(tHBPostcode0ValidPostcode1ValidCount));
        summary.put(
                tHBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString,
                Integer.toString(tHBPostcode0ValidPostcode1ValidPostcodeNotChangedCount));
        summary.put(
                tHBPostcode0ValidPostcode1ValidPostcodeChangeCountString,
                Integer.toString(tHBPostcode0ValidPostcode1ValidPostcodeChangeCount));
        summary.put(
                tHBPostcode0ValidPostcode1NotValidCountString,
                Integer.toString(tHBPostcode0ValidPostcode1NotValidCount));
        summary.put(
                tHBPostcode0NotValidPostcode1ValidCountString,
                Integer.toString(tHBPostcode0NotValidPostcode1ValidCount));
        summary.put(
                tHBPostcode0NotValidPostcode1NotValidCountString,
                Integer.toString(tHBPostcode0NotValidPostcode1NotValidCount));
        summary.put(
                tHBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString,
                Integer.toString(tHBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount));
        summary.put(
                tHBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString,
                Integer.toString(tHBPostcode0NotValidPostcode1NotValidPostcodeChangedCount));
        summary.put(
                tHBTenancyTypeChangeCountString,
                Integer.toString(tHBTenancyTypeChangeCount));
        summary.put(
                tHBTenancyTypeChangeHousingCountString,
                Integer.toString(tHBTenancyTypeChangeHousingCount));
        summary.put(
                tHBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString,
                Integer.toString(tHBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount));
        summary.put(
                tHBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString,
                Integer.toString(tHBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount));
        summary.put(
                tHBPostcodeChangeWithinSocialTenancyTypesCountString,
                Integer.toString(tHBPostcodeChangeWithinSocialTenancyTypesCount));
        summary.put(
                tHBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString,
                Integer.toString(tHBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount));
        // CTB
        summary.put(
                tCTBPostcode0ValidPostcode1ValidCountString,
                Integer.toString(tCTBPostcode0ValidPostcode1ValidCount));
        summary.put(
                tCTBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString,
                Integer.toString(tCTBPostcode0ValidPostcode1ValidPostcodeNotChangedCount));
        summary.put(
                tCTBPostcode0ValidPostcode1ValidPostcodeChangeCountString,
                Integer.toString(tCTBPostcode0ValidPostcode1ValidPostcodeChangeCount));
        summary.put(
                tCTBPostcode0ValidPostcode1NotValidCountString,
                Integer.toString(tCTBPostcode0ValidPostcode1NotValidCount));
        summary.put(
                tCTBPostcode0NotValidPostcode1ValidCountString,
                Integer.toString(tCTBPostcode0NotValidPostcode1ValidCount));
        summary.put(
                tCTBPostcode0NotValidPostcode1NotValidCountString,
                Integer.toString(tCTBPostcode0NotValidPostcode1NotValidCount));
        summary.put(
                tCTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString,
                Integer.toString(tCTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount));
        summary.put(
                tCTBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString,
                Integer.toString(tCTBPostcode0NotValidPostcode1NotValidPostcodeChangedCount));
        summary.put(
                tCTBTenancyTypeChangeCountString,
                Integer.toString(tCTBTenancyTypeChangeCount));
        summary.put(
                tCTBTenancyTypeChangeHousingCountString,
                Integer.toString(tCTBTenancyTypeChangeHousingCount));
        summary.put(
                tCTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString,
                Integer.toString(tCTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount));
        summary.put(
                tCTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString,
                Integer.toString(tCTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount));
        summary.put(
                tCTBPostcodeChangeWithinSocialTenancyTypesCountString,
                Integer.toString(tCTBPostcodeChangeWithinSocialTenancyTypesCount));
        summary.put(
                tCTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString,
                Integer.toString(tCTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount));
    }

    private void addToSummarySingleTimeRentArrears(
            HashMap<String, String> summary) {
        summary.put(
                totalRentArrearsString,
                "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                        BigDecimal.valueOf(totalRentArrears), 
                        2, RoundingMode.HALF_UP));
        if (rentArrearsCount != 0.0d) {
            summary.put(
                    averageRentArrearsString,
                    "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                            BigDecimal.valueOf(totalRentArrears / rentArrearsCount), 
                            2, RoundingMode.HALF_UP));
        } else {
            summary.put(
                    averageRentArrearsString,
                    "0");
        }
        if (greaterThan0RentArrearsCount != 0.0d) {
            summary.put(
                    greaterThan0AverageRentArrearsString,
                    "" + Generic_BigDecimal.roundToAndSetDecimalPlaces(
                            BigDecimal.valueOf(totalRentArrears / greaterThan0RentArrearsCount), 
                            2, RoundingMode.HALF_UP));
        } else {
            summary.put(
                    greaterThan0AverageRentArrearsString,
                    "0");
        }
    }

    private void addToSummarySingleTime(
            int nTT,
            int nEG,
            HashMap<String, String> summary) {
        summary.put(tAllCountString, Integer.toString(tHBCount + tCTBCount));
        // Ethnicity
        for (int i = 1; i < nEG; i++) {
            summary.put(
                    tAllEthnicGroupCountString[i],
                    Integer.toString(tHBEthnicGroupCount[i] + tCTBEthnicGroupCount[i]));
            summary.put(
                    tHBEthnicGroupCountString[i],
                    Integer.toString(tHBEthnicGroupCount[i]));
            summary.put(
                    tCTBEthnicGroupCountString[i],
                    Integer.toString(tCTBEthnicGroupCount[i]));
        }
        // TenancyType
        for (int i = 1; i < nTT; i++) {
            summary.put(
                    tAllTenancyTypeClaimantCountString[i],
                    Integer.toString(tHBTenancyTypeCount[i] + tCTBTenancyTypeCount[i]));
            summary.put(
                    tHBTenancyTypeClaimantCountString[i],
                    Integer.toString(tHBTenancyTypeCount[i]));
            summary.put(
                    tCTBTenancyTypeClaimantCountString[i],
                    Integer.toString(tCTBTenancyTypeCount[i]));
        }
        summary.put(
                tAllTenancyType8InLS277NSClaimantCountString,
                Integer.toString(tHBTenancyType8InLS277NSClaimantCount + tCTBTenancyType8InLS277NSClaimantCount));
        summary.put(
                tAllPostcodeValidFormatCountString,
                Integer.toString(tHBPostcodeValidFormatCount + tCTBPostcodeValidFormatCount));
        summary.put(
                tAllPostcodeValidCountString,
                Integer.toString(tHBPostcodeValidCount + tCTBPostcodeValidCount));
        // HB
        summary.put(tHBCountString, Integer.toString(tHBCount));
        summary.put(
                tHBTenancyType8InLS277NSClaimantCountString,
                Integer.toString(tHBTenancyType8InLS277NSClaimantCount));
        summary.put(
                tHBPostcodeValidFormatCountString,
                Integer.toString(tHBPostcodeValidFormatCount));
        summary.put(
                tHBPostcodeValidCountString,
                Integer.toString(tHBPostcodeValidCount));
        // CTB
        summary.put(tCTBCountString, Integer.toString(tCTBCount));
        summary.put(
                tCTBTenancyType8InLS277NSClaimantCountString,
                Integer.toString(tCTBTenancyType8InLS277NSClaimantCount));
        summary.put(
                tCTBPostcodeValidFormatCountString,
                Integer.toString(tCTBPostcodeValidFormatCount));
        summary.put(
                tCTBPostcodeValidCountString,
                Integer.toString(tCTBPostcodeValidCount));
    }

    private void doCompare2TimesCounts(
            DW_SHBE_D_Record D_Record,
            String CTBRef,
            HashMap<String, DW_ID> tCTBRefID0,
            HashMap<String, DW_ID> tCTBRefID1,
            HashMap<DW_ID, Integer> tIDByTenancyType0,
            HashMap<DW_ID, Integer> tIDByTenancyType1,
            HashMap<DW_ID, String> tIDByPostcode0,
            HashMap<DW_ID, String> tIDByPostcode1,
            String yM30v,
            String yM31v) {
        String HBRef;
        HBRef = D_Record.getHousingBenefitClaimReferenceNumber();
        DW_ID tID0;
        tID0 = tCTBRefID0.get(CTBRef);
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
        tID1 = tCTBRefID1.get(CTBRef);
        int ClaimantsEthnicGroup1;
        ClaimantsEthnicGroup1 = D_Record.getClaimantsEthnicGroup();
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
        if (HBRef.equalsIgnoreCase(CTBRef)) {
            doCompare2TimesHBCount(
                    tenancyType0,
                    postcode0,
                    yM31v,
                    ClaimantsEthnicGroup1,
                    isValidPostcode1,
                    tenancyType1,
                    postcode1,
                    isValidPostcode0);
        } else {
            doCompare2TimesCTBCount(
                    tenancyType0,
                    postcode0,
                    yM31v,
                    ClaimantsEthnicGroup1,
                    isValidPostcode1,
                    tenancyType1,
                    postcode1,
                    isValidPostcode0);
        }
    }

    private void doSingleTimeCount(
            DW_SHBE_D_Record D_Record,
            String CTBRef,
            HashMap<String, DW_ID> tCTBRefID0,
            HashMap<DW_ID, Integer> tIDByTenancyType0,
            HashMap<DW_ID, String> tIDByPostcode0,
            String yM30v) {
        String HBRef = D_Record.getHousingBenefitClaimReferenceNumber();

        int ClaimantsEthnicGroup0;
        ClaimantsEthnicGroup0 = D_Record.getClaimantsEthnicGroup();
//        int DisabilityPremiumAwarded0;
//        DisabilityPremiumAwarded0 = D_Record.getDisabilityPremiumAwarded();
//        int SevereDisabilityPremiumAwarded0;
//        SevereDisabilityPremiumAwarded0 = D_Record.getSevereDisabilityPremiumAwarded();

        DW_ID tID0;
        tID0 = tCTBRefID0.get(CTBRef);
        String postcode0 = null;
        Integer tenancyType0 = 0;
        if (tID0 != null) {
            tenancyType0 = tIDByTenancyType0.get(tID0);
            if (tenancyType0 == null) {
                tenancyType0 = 0;
            }
            postcode0 = tIDByPostcode0.get(tID0);
        }
        if (HBRef.equalsIgnoreCase(CTBRef)) {
            doSingleTimeHBCount(
                    ClaimantsEthnicGroup0,
                    tenancyType0,
                    postcode0,
                    yM30v);
        } else {
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
            String yM31v,
            int ClaimantsEthnicGroup1,
            boolean isValidPostcode1,
            Integer tenancyType1,
            String postcode1,
            boolean isValidPostcode0) {
        doSingleTimeHBCount(ClaimantsEthnicGroup1, tenancyType1, postcode1, yM31v);
        if (isValidPostcode0) {
            if (isValidPostcode1) {
                tHBPostcode0ValidPostcode1ValidCount++;
                if (postcode0.equalsIgnoreCase(postcode1)) {
                    tHBPostcode0ValidPostcode1ValidPostcodeNotChangedCount++;
                } else {
                    tHBPostcode0ValidPostcode1ValidPostcodeChangeCount++;
                }
            } else {
                tHBPostcode0ValidPostcode1NotValidCount++;
            }
        } else {
            if (isValidPostcode1) {
                tHBPostcode0NotValidPostcode1ValidCount++;
            } else {
                tHBPostcode0NotValidPostcode1NotValidCount++;
                if (postcode0 == null) {
                    tHBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount++;
                } else {
                    if (postcode1 == null) {
                        tHBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount++;
                    } else {
                        if (postcode0.equalsIgnoreCase(postcode1)) {
                            tHBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount++;
                        } else {
                            tHBPostcode0NotValidPostcode1NotValidPostcodeChangedCount++;
                        }
                    }
                }
            }
        }
        if (tenancyType0.compareTo(tenancyType1) != 0) {
            if (!(tenancyType0 == 0 || tenancyType1 == 0)) {
                tHBTenancyTypeChangeCount++;
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
                    tHBTenancyTypeChangeHousingCount++;
                }
            }
            if ((tenancyType0 == 1
                    || tenancyType0 == 4)
                    && tenancyType1 == 3) {
                tHBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount++;
            }
            if (tenancyType0 == 3
                    && (tenancyType1 == 1
                    || tenancyType1 == 4)) {
                tHBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount++;
            }
        }
        if ((tenancyType0 == 1 && tenancyType1 == 1)
                || (tenancyType0 == 1 && tenancyType1 == 4)
                || (tenancyType0 == 4 && tenancyType1 == 1)
                || (tenancyType0 == 4 && tenancyType1 == 4)) {
            if (isValidPostcode0 && isValidPostcode1) {
                if (!postcode0.equalsIgnoreCase(postcode1)) {
                    tHBPostcodeChangeWithinSocialTenancyTypesCount++;
                }
            }
        }
        if (tenancyType0 == 3 && tenancyType1 == 3) {
            if (isValidPostcode0 && isValidPostcode1) {
                if (!postcode0.equalsIgnoreCase(postcode1)) {
                    tHBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount++;
                }
            }
        }
    }

    private void doSingleTimeHBCount(
            int ClaimantsEthnicGroup0,
            Integer tenancyType0,
            String postcode0,
            String yM30v) {
        tHBCount++;
        tHBEthnicGroupCount[ClaimantsEthnicGroup0]++;
        tHBTenancyTypeCount[tenancyType0]++;
        if (tenancyType0 == 8) {
            if (postcode0 != null) {
                if (postcode0.equalsIgnoreCase("LS27 7NS")) {
                    tHBTenancyType8InLS277NSClaimantCount++;
                }
            }
        }
        if (postcode0 != null) {
            boolean isValidPostcodeFormat;
            isValidPostcodeFormat = Generic_UKPostcode_Handler.isValidPostcodeForm(postcode0);
            boolean isValidPostcode;
            isValidPostcode = DW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
            if (isValidPostcodeFormat) {
                tHBPostcodeValidFormatCount++;
            }
            if (isValidPostcode) {
                tHBPostcodeValidCount++;
            }
        }
    }

    private void doCompare2TimesCTBCount(
            Integer tenancyType0,
            String postcode0,
            String yM31v,
            int ClaimantsEthnicGroup1,
            boolean isValidPostcode1,
            Integer tenancyType1,
            String postcode1,
            boolean isValidPostcode0) {
        doSingleTimeCTBCount(ClaimantsEthnicGroup1, tenancyType1, postcode1, yM31v);
        if (isValidPostcode0) {
            if (isValidPostcode1) {
                tCTBPostcode0ValidPostcode1ValidCount++;
                if (postcode0.equalsIgnoreCase(postcode1)) {
                    tCTBPostcode0ValidPostcode1ValidPostcodeNotChangedCount++;
                } else {
                    tCTBPostcode0ValidPostcode1ValidPostcodeChangeCount++;
                }
            } else {
                tCTBPostcode0ValidPostcode1NotValidCount++;
            }
        } else {
            if (isValidPostcode1) {
                tCTBPostcode0NotValidPostcode1ValidCount++;
            } else {
                tCTBPostcode0NotValidPostcode1NotValidCount++;
                if (postcode0 == null) {
                    tCTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount++;
                } else {
                    if (postcode1 == null) {
                        tCTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount++;
                    } else {
                        if (postcode0.equalsIgnoreCase(postcode1)) {
                            tCTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCount++;
                        } else {
                            tCTBPostcode0NotValidPostcode1NotValidPostcodeChangedCount++;
                        }
                    }
                }
            }
        }
        if (tenancyType0.compareTo(tenancyType1) != 0) {
            if (!(tenancyType0 == 0 || tenancyType1 == 0)) {
                tCTBTenancyTypeChangeCount++;
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
                    tCTBTenancyTypeChangeHousingCount++;
                }
            }
            if ((tenancyType0 == 1
                    || tenancyType0 == 4)
                    && tenancyType1 == 3) {
                tCTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCount++;
            }
            if (tenancyType0 == 3
                    && (tenancyType1 == 1
                    || tenancyType1 == 4)) {
                tCTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCount++;
            }
        }
        if ((tenancyType0 == 1 && tenancyType1 == 1)
                || (tenancyType0 == 1 && tenancyType1 == 4)
                || (tenancyType0 == 4 && tenancyType1 == 1)
                || (tenancyType0 == 4 && tenancyType1 == 4)) {
            if (isValidPostcode0 && isValidPostcode1) {
                if (!postcode0.equalsIgnoreCase(postcode1)) {
                    tCTBPostcodeChangeWithinSocialTenancyTypesCount++;
                }
            }
        }
        if (tenancyType0 == 3 && tenancyType1 == 3) {
            if (isValidPostcode0 && isValidPostcode1) {
                if (!postcode0.equalsIgnoreCase(postcode1)) {
                    tCTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCount++;
                }
            }
        }
    }

    private void doSingleTimeCTBCount(
            int ClaimantsEthnicGroup0,
            Integer tenancyType0,
            String postcode0,
            String yM30v) {
        tCTBCount++;
        tCTBEthnicGroupCount[ClaimantsEthnicGroup0]++;
        tCTBTenancyTypeCount[tenancyType0]++;
        if (tenancyType0 == 8) {
            if (postcode0 != null) {
                if (postcode0.equalsIgnoreCase("LS27 7NS")) {
                    tCTBTenancyType8InLS277NSClaimantCount++;
                }
            }
        }
        if (postcode0 != null) {
            boolean isValidPostcodeFormat;
            isValidPostcodeFormat = Generic_UKPostcode_Handler.isValidPostcodeForm(postcode0);
            boolean isValidPostcode;
            isValidPostcode = DW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
            if (isValidPostcodeFormat) {
                tCTBPostcodeValidFormatCount++;
            }
            if (isValidPostcode) {
                tCTBPostcodeValidCount++;
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
            int nEG
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
         * result[10] is a HashMap<String, DW_ID> tCTBRefToClaimantIDLookup;
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
        TreeMap<String, HashSet<DW_ID>> tAllIDs;
        tAllIDs = new TreeMap<String, HashSet<DW_ID>>();
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
        HashSet<DW_ID> tAllIDs0;
        tAllIDs0 = (HashSet<DW_ID>) tSHBEData0[6];
        tAllIDs.put(yM30, tAllIDs0);
        HashMap<DW_ID, String> tIDByPostcode0;
        tIDByPostcode0 = (HashMap<DW_ID, String>) tSHBEData0[8];
        //tIDByPostcode0 = loadIDByPostcode(loadData, filename, i);
        HashMap<DW_ID, Integer> tIDByTenancyType0;
        tIDByTenancyType0 = (HashMap<DW_ID, Integer>) tSHBEData0[9];
        //tIDByTenancyType0 = loadIDByTenancyType(loadData, filename, i);
        HashMap<String, DW_ID> tCTBRefToIDLookup0;
        tCTBRefToIDLookup0 = (HashMap<String, DW_ID>) tSHBEData0[10];
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
        initCounts(nTT, nEG);
        Iterator<String> ite;
        ite = tCTBRefToIDLookup0.keySet().iterator();
        while (ite.hasNext()) {
            String CTBRef;
            CTBRef = ite.next();
            String HBRef;
            DW_SHBE_D_Record D_Record;
            D_Record = tDRecords0.get(CTBRef).getDRecord();
            HBRef = D_Record.getHousingBenefitClaimReferenceNumber();
            DW_ID tID0;
            tID0 = tCTBRefToIDLookup0.get(CTBRef);
            String postcode0 = null;
            int ClaimantsEthnicGroup0 = 0;
            Integer tenancyType0 = 0;
            if (tID0 != null) {
                tenancyType0 = tIDByTenancyType0.get(tID0);
                if (tenancyType0 == null) {
                    tenancyType0 = 0;
                }
                postcode0 = tIDByPostcode0.get(tID0);
                ClaimantsEthnicGroup0 = D_Record.getClaimantsEthnicGroup();
            } else {
                System.out.println("No SHBE record for CTBRef " + CTBRef);
            }
            if (HBRef.equalsIgnoreCase(CTBRef)) {
                doSingleTimeHBCount(ClaimantsEthnicGroup0, tenancyType0, postcode0, yM30v);
            } else {
                doSingleTimeCTBCount(ClaimantsEthnicGroup0, tenancyType0, postcode0, yM30v);
            }
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
        addToSummarySingleTime(nTT, nEG, summary);
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
             * result[10] is a HashMap<String, DW_ID> tCTBRefToClaimantIDLookup;
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
            HashSet<DW_ID> tAllIDs1;
            tAllIDs1 = (HashSet<DW_ID>) tSHBEData1[6];
            tAllIDs.put(yM31, tAllIDs1);
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
            initCounts(nTT, nEG);
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
                        tCTBRefToIDLookup0,
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
            addToSummaryCompare2Times(nTT, nEG, summary);
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
            tCTBRefToIDLookup0 = CTBRefToIDLookup1;
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
        TreeMap<String, HashSet<DW_ID>> tAllIDs;
        tAllIDs = new TreeMap<String, HashSet<DW_ID>>();
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
        HashSet<DW_ID> tAllIDs0;
        tAllIDs0 = (HashSet<DW_ID>) tSHBEData0[6];
        tAllIDs.put(yM30, tAllIDs0);
//        HashMap<DW_ID, String> tIDByPostcode0;
//        tIDByPostcode0 = (HashMap<DW_ID, String>) tSHBEData0[8];
        //tIDByPostcode0 = loadIDByPostcode(loadData, filename, i);
//        HashMap<DW_ID, Integer> tIDByTenancyType0;
//        tIDByTenancyType0 = (HashMap<DW_ID, Integer>) tSHBEData0[9];
        //tIDByTenancyType0 = loadIDByTenancyType(loadData, filename, i);
//        HashMap<String, DW_ID> tCTBRefToIDLookup0;
//        tCTBRefToIDLookup0 = (HashMap<String, DW_ID>) tSHBEData0[10];
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
tCouncilLinkedRecordCount00 = tCouncilLinkedRecordCount0;
        tRSLLinkedRecordCount00 = tRSLLinkedRecordCount0;
        tAllLinkedRecordCount00 = tAllLinkedRecordCount0;
        tCouncilCount00 = tCouncilCount0;
        tRSLCount00 = tRSLCount0;
        partSummaryCompare2Times(
                tSHBEData1, yM31, yM31v, filename1, tSHBEData0, yM30, yM30v, 
                filename0, forceNewSummaries, nTT, nEG, councilFilenames,
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
        summary.put(tCouncilCount00String, Integer.toString(tCouncilCount00));
        summary.put(tCouncilLinkedRecordCount00String, Integer.toString(tCouncilLinkedRecordCount00));
        summary.put(tRSLCount00String, Integer.toString(tRSLCount00));
        summary.put(tRSLLinkedRecordCount00String, Integer.toString(tRSLLinkedRecordCount00));
        summary.put(tAllLinkedRecordCount00String, Integer.toString(tCouncilLinkedRecordCount00 + tRSLLinkedRecordCount00));
        summary.put(CouncilFilename0String, councilFilenames.get(yM30));
        summary.put(RSLFilename0String, RSLFilenames.get(yM30));
        summary.put(tCouncilCount0String, Integer.toString(tCouncilCount0));
        summary.put(tCouncilLinkedRecordCount0String, Integer.toString(tCouncilLinkedRecordCount0));
        summary.put(tRSLCount0String, Integer.toString(tRSLCount0));
        summary.put(tRSLLinkedRecordCount0String, Integer.toString(tRSLLinkedRecordCount0));
        summary.put(tAllLinkedRecordCount0String, Integer.toString(tCouncilLinkedRecordCount0 + tRSLLinkedRecordCount0));
        summary.put(CouncilFilename1String, councilFilenames.get(yM31));
        summary.put(RSLFilename1String, RSLFilenames.get(yM31));
        summary.put(tCouncilCount1String, Integer.toString(tCouncilCount1));
        summary.put(tCouncilLinkedRecordCount1String, Integer.toString(tCouncilLinkedRecordCount1));
        summary.put(tRSLCount1String, Integer.toString(tRSLCount1));
        summary.put(tRSLLinkedRecordCount1String, Integer.toString(tRSLLinkedRecordCount1));
        summary.put(tAllLinkedRecordCount1String, Integer.toString(tCouncilLinkedRecordCount1 + tRSLLinkedRecordCount1));
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
        tCouncilCount0 = tCouncilCount1;
        tRSLCount0 = tRSLCount1;
        tAllCount0 = tAllCount1;
        tCouncilLinkedRecordCount0 = tCouncilLinkedRecordCount1;
        tRSLLinkedRecordCount0 = tRSLLinkedRecordCount1;
        tAllLinkedRecordCount0 = tAllLinkedRecordCount1;
        partSummarySingleTime(tSHBEData0, yM30, yM30v, filename0, 
                forceNewSummaries, nTT, nEG, councilFilenames, RSLFilenames, 
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
        HashMap<String, DW_ID> tCTBRefToIDLookup0;
        tCTBRefToIDLookup0 = (HashMap<String, DW_ID>) tSHBEData0[10];

        HashMap<DW_ID, String> tIDByPostcode00;
        tIDByPostcode00 = (HashMap<DW_ID, String>) tSHBEData00[8];
        HashMap<DW_ID, Integer> tIDByTenancyType00;
        tIDByTenancyType00 = (HashMap<DW_ID, Integer>) tSHBEData00[9];
        HashMap<String, DW_ID> tCTBRefToIDLookup00;
        tCTBRefToIDLookup00 = (HashMap<String, DW_ID>) tSHBEData00[10];

        TreeMap<String, DW_UnderOccupiedReport_Record> councilUnderOccupiedSet0Map;
        councilUnderOccupiedSet0Map = councilUnderOccupiedSet0.getMap();
        TreeMap<String, DW_UnderOccupiedReport_Record> RSLUnderOccupiedSet0Map;
        RSLUnderOccupiedSet0Map = RSLUnderOccupiedSet0.getMap();
        // Loop over underoccupancy data
        // Loop over Council
        doCompare2TimesLoopOverSet(
                councilUnderOccupiedSet0Map,
                tDRecords0,
                tCTBRefToIDLookup00,
                tCTBRefToIDLookup0,
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
                tCTBRefToIDLookup00,
                tCTBRefToIDLookup0,
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
        
        addToSummaryCompare2Times(nTT, nEG, summary);
        
        // All
        summary.put(SHBEFilename0String, filename00); // This looks wierd but is right!
        summary.put(SHBEFilename1String, filename0); // This looks wierd but is right!
        summary.put(CouncilFilename0String, councilFilenames.get(yM300)); // This looks wierd but is right!
        summary.put(RSLFilename0String, RSLFilenames.get(yM300)); // This looks wierd but is right!
        summary.put(tCouncilCount0String, Integer.toString(tCouncilCount0));
        summary.put(tCouncilLinkedRecordCount0String, Integer.toString(tCouncilLinkedRecordCount0));
        summary.put(tRSLCount0String, Integer.toString(tRSLCount0));
        summary.put(tRSLLinkedRecordCount0String, Integer.toString(tRSLLinkedRecordCount0));
        summary.put(tAllLinkedRecordCount0String, Integer.toString(tCouncilLinkedRecordCount0 + tRSLLinkedRecordCount0));
        summary.put(CouncilFilename1String, councilFilenames.get(yM30)); // This looks wierd but is right!
        summary.put(RSLFilename1String, RSLFilenames.get(yM30)); // This looks wierd but is right!
        summary.put(tCouncilCount1String, Integer.toString(tCouncilCount1));
        summary.put(tCouncilLinkedRecordCount1String, Integer.toString(tCouncilLinkedRecordCount1));
        summary.put(tRSLCount1String, Integer.toString(tRSLCount1));
        summary.put(tRSLLinkedRecordCount1String, Integer.toString(tRSLLinkedRecordCount1));
        summary.put(tAllLinkedRecordCount1String, Integer.toString(tCouncilLinkedRecordCount1 + tRSLLinkedRecordCount1));
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
            //            HashMap<String, DW_ID> tCTBRefToIDLookup0,
            //            HashMap<String, Integer> tLoadSummary,
            String yM3,
            String yM3v,
            String filename,
            boolean forceNewSummaries,
            int nTT,
            int nEG,
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
        HashMap<String, DW_ID> tCTBRefToIDLookup0;
        tCTBRefToIDLookup0 = (HashMap<String, DW_ID>) tSHBEData[10];
        //tCTBRefID0 = loadCTBRefByID(loadData, filename, i);
        String key;
        key = DW_SHBE_Handler.getYearMonthNumber(filename);
        HashMap<String, String> summary;
        summary = result.get(key);
        HashMap<String, Integer> tLoadSummary;
        tLoadSummary = (HashMap<String, Integer>) tSHBEData[12];
        addToSummary(summary, tLoadSummary);
        initCounts(nTT, nEG);

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
        tCouncilLinkedRecordCount1 = doSingleTimeLoopOverSet(
                councilUnderOccupiedSet0Map, tDRecords0, tCTBRefToIDLookup0,
                tIDByTenancyType0, tIDByPostcode0, yM3v);
        // Loop over RSL
        tRSLLinkedRecordCount1 = doSingleTimeLoopOverSet(
                RSLUnderOccupiedSet0Map, tDRecords0, tCTBRefToIDLookup0,
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
        tCouncilCount1 = councilUnderOccupiedSet0Map.size();
        tRSLCount1 = RSLUnderOccupiedSet0Map.size();
        tAllCount1 = tCouncilCount1 + tRSLCount1;
        tAllLinkedRecordCount1 = tCouncilLinkedRecordCount1 + tRSLLinkedRecordCount1;
        summary.put(SHBEFilename1String, filename);
        summary.put(CouncilFilename1String, councilFilenames.get(yM3));
        summary.put(RSLFilename1String, RSLFilenames.get(yM3));
        summary.put(tCouncilCount1String, Integer.toString(councilUnderOccupiedSet0Map.size()));
        summary.put(tCouncilLinkedRecordCount1String, Integer.toString(tCouncilLinkedRecordCount1));
        summary.put(tRSLCount1String, Integer.toString(RSLUnderOccupiedSet0Map.size()));
        summary.put(tRSLLinkedRecordCount1String, Integer.toString(tRSLLinkedRecordCount1));
        summary.put(tAllCount1String, Integer.toString(tAllCount1));
        summary.put(tAllLinkedRecordCount1String, Integer.toString(tAllLinkedRecordCount1));
        addToSummarySingleTime(nTT, nEG, summary);
        addToSummarySingleTimeRentArrears(summary);
    }

    public void doCompare2TimesLoopOverSet(
            TreeMap<String, DW_UnderOccupiedReport_Record> map,
            TreeMap<String, DW_SHBE_Record> D_Records0,
            HashMap<String, DW_ID> tCTBRefID0,
            HashMap<String, DW_ID> tCTBRefID1,
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
                        tCTBRefID0,
                        tCTBRefID1,
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
            HashMap<String, DW_ID> tCTBRefToIDLookup0,
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
                        tCTBRefToIDLookup0,
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
            header += tCouncilCount00String + ", ";
            header += tRSLCount00String + ", ";
            header += tAllCount00String + ", ";
            header += tAllLinkedRecordCount00String + ", ";
            header += CouncilFilename0String + ", ";
            header += RSLFilename0String + ", ";
            header += tCouncilCount0String + ", ";
            header += tRSLCount0String + ", ";
            header += tAllCount0String + ", ";
            header += tAllLinkedRecordCount1String + ", ";
            header += CouncilFilename1String + ", ";
            header += RSLFilename1String + ", ";
            header += tCouncilCount1String + ", ";
            header += tRSLCount1String + ", ";
            header += tAllCount1String + ", ";
            header += tAllLinkedRecordCount1String + ", ";
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
                line += summary.get(tCouncilCount00String) + ", ";
                line += summary.get(tRSLCount00String) + ", ";
                line += summary.get(tAllCount00String) + ", ";
                line += summary.get(tAllLinkedRecordCount00String) + ", ";
                line += summary.get(CouncilFilename0String) + ", ";
                line += summary.get(RSLFilename0String) + ", ";
                line += summary.get(tCouncilCount0String) + ", ";
                line += summary.get(tRSLCount0String) + ", ";
                line += summary.get(tAllCount0String) + ", ";
                line += summary.get(tAllLinkedRecordCount0String) + ", ";
                line += summary.get(CouncilFilename1String) + ", ";
                line += summary.get(RSLFilename1String) + ", ";
                line += summary.get(tCouncilCount1String) + ", ";
                line += summary.get(tRSLCount1String) + ", ";
                line += summary.get(tAllCount1String) + ", ";
                line += summary.get(tAllLinkedRecordCount1String) + ", ";
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
            header += tCouncilCount0String + ", ";
            header += tRSLCount0String + ", ";
            header += tAllCount0String + ", ";
            header += tAllLinkedRecordCount1String + ", ";
            header += CouncilFilename1String + ", ";
            header += RSLFilename1String + ", ";
            header += tCouncilCount1String + ", ";
            header += tRSLCount1String + ", ";
            header += tAllCount1String + ", ";
            header += tAllLinkedRecordCount1String + ", ";
        }
        header += tHBPostcode0ValidPostcode1ValidCountString + ", ";
        header += tHBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString + ", ";
        header += tHBPostcode0ValidPostcode1ValidPostcodeChangeCountString + ", ";
        header += tHBPostcode0ValidPostcode1NotValidCountString + ", ";
        header += tHBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString + ", ";
        header += tHBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString + ", ";
        header += tHBTenancyTypeChangeCountString + ", ";
        header += tHBTenancyTypeChangeHousingCountString + ", ";
        header += tHBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString + ", ";
        header += tHBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString + ", ";
        header += tHBPostcodeChangeWithinSocialTenancyTypesCountString + ", ";
        header += tHBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString + ", ";
        header += tCTBPostcode0ValidPostcode1ValidCountString + ", ";
        header += tCTBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString + ", ";
        header += tCTBPostcode0ValidPostcode1ValidPostcodeChangeCountString + ", ";
        header += tCTBPostcode0ValidPostcode1NotValidCountString + ", ";
        header += tCTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString + ", ";
        header += tCTBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString + ", ";
        header += tCTBTenancyTypeChangeCountString + ", ";
        header += tCTBTenancyTypeChangeHousingCountString + ", ";
        header += tCTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString + ", ";
        header += tCTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString + ", ";
        header += tCTBPostcodeChangeWithinSocialTenancyTypesCountString + ", ";
        header += tCTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString;
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
                line += summary.get(tCouncilCount0String) + ", ";
                line += summary.get(RSLFilename0String) + ", ";
                line += summary.get(tRSLCount0String) + ", ";
                line += summary.get(tAllCount0String) + ", ";
                line += summary.get(tAllLinkedRecordCount0String) + ", ";
                line += summary.get(CouncilFilename1String) + ", ";
                line += summary.get(RSLFilename1String) + ", ";
                line += summary.get(tCouncilCount1String) + ", ";
                line += summary.get(tRSLCount1String) + ", ";
                line += summary.get(tAllCount1String) + ", ";
                line += summary.get(tAllLinkedRecordCount1String) + ", ";
                line += summary.get(CouncilFilename1String) + ", ";
            }
            line += summary.get(tHBPostcode0ValidPostcode1ValidCountString) + ", ";
            line += summary.get(tHBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString) + ", ";
            line += summary.get(tHBPostcode0ValidPostcode1ValidPostcodeChangeCountString) + ", ";
            line += summary.get(tHBPostcode0ValidPostcode1NotValidCountString) + ", ";
            line += summary.get(tHBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString) + ", ";
            line += summary.get(tHBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString) + ", ";
            line += summary.get(tHBTenancyTypeChangeCountString) + ", ";
            line += summary.get(tHBTenancyTypeChangeHousingCountString) + ", ";
            line += summary.get(tHBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString) + ", ";
            line += summary.get(tHBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString) + ", ";
            line += summary.get(tHBPostcodeChangeWithinSocialTenancyTypesCountString) + ", ";
            line += summary.get(tHBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString) + ", ";
            line += summary.get(tCTBPostcode0ValidPostcode1ValidCountString) + ", ";
            line += summary.get(tCTBPostcode0ValidPostcode1ValidPostcodeNotChangedCountString) + ", ";
            line += summary.get(tCTBPostcode0ValidPostcode1ValidPostcodeChangeCountString) + ", ";
            line += summary.get(tCTBPostcode0ValidPostcode1NotValidCountString) + ", ";
            line += summary.get(tCTBPostcode0NotValidPostcode1NotValidPostcodeNotChangedCountString) + ", ";
            line += summary.get(tCTBPostcode0NotValidPostcode1NotValidPostcodeChangedCountString) + ", ";
            line += summary.get(tCTBTenancyTypeChangeCountString) + ", ";
            line += summary.get(tCTBTenancyTypeChangeHousingCountString) + ", ";
            line += summary.get(tCTBSocialTenancyTypesToPrivateDeregulatedTenancyTypesCountString) + ", ";
            line += summary.get(tCTBPrivateDeregulatedTenancyTypesToSocialTenancyTypesCountString) + ", ";
            line += summary.get(tCTBPostcodeChangeWithinSocialTenancyTypesCountString) + ", ";
            line += summary.get(tCTBPostcodeChangeWithinPrivateDeregulatedTenancyTypesCountString);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTime(
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
                summaryTable.firstKey() + "To" + summaryTable.lastKey() + "SingleTime.csv");
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
        header += tAllPostcodeValidFormatCountString + ", ";
        header += tAllPostcodeValidCountString + ", ";
        header += tHBPostcodeValidFormatCountString + ", ";
        header += tHBPostcodeValidCountString + ", ";
        header += tCTBPostcodeValidFormatCountString + ", ";
        header += tCTBPostcodeValidCountString;
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
            line += summary.get(tAllPostcodeValidFormatCountString) + ", ";
            line += summary.get(tAllPostcodeValidCountString) + ", ";
            line += summary.get(tHBPostcodeValidFormatCountString) + ", ";
            line += summary.get(tHBPostcodeValidCountString) + ", ";
            line += summary.get(tCTBPostcodeValidFormatCountString) + ", ";
            line += summary.get(tCTBPostcodeValidCountString);
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
            boolean underOccupancy,
            int nTT,
            int nEG
    ) {
        File dirOut;
        dirOut = getSummaryTableDir(includeKey, underOccupancy);

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
        header += getSingleTimeGenericHeaderPart(underOccupancy);
        header += tTotalIncomeString + ", ";
        header += tTotalIncomeGreaterThanZeroCountString + ", ";
        header += tAverageIncomeString + ", ";
        header += tTotalWeeklyEligibleRentAmountString + ", ";
        header += tTotalWeeklyEligibleRentAmountGreaterThanZeroCountString + ", ";
        header += tAverageWeeklyEligibleRentAmountString + ", ";
        for (int i = 1; i < nTT; i++) {
            header += tTotalIncomeString + "TenancyType" + i + ", ";
            header += tTotalIncomeGreaterThanZeroCountString + "TenancyType" + i + ", ";
            header += tAverageIncomeString + "TenancyType" + i + ", ";
            header += tTotalWeeklyEligibleRentAmountString + "TenancyType" + i + ", ";
            header += tTotalWeeklyEligibleRentAmountGreaterThanZeroCountString + "TenancyType" + i + ", ";
            header += tAverageWeeklyEligibleRentAmountString + "TenancyType" + i + ", ";
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
            line += summary.get(tTotalIncomeString) + ", ";
            line += summary.get(tTotalIncomeGreaterThanZeroCountString) + ", ";
            line += summary.get(tAverageIncomeString) + ", ";
            line += summary.get(tTotalWeeklyEligibleRentAmountString) + ", ";
            line += summary.get(tTotalWeeklyEligibleRentAmountGreaterThanZeroCountString) + ", ";
            line += summary.get(tAverageWeeklyEligibleRentAmountString) + ", ";
            for (int i = 1; i < nTT; i++) {
                line += summary.get(tTotalIncomeString + "TenancyType" + i) + ", ";
                line += summary.get(tTotalIncomeGreaterThanZeroCountString + "TenancyType" + i) + ", ";
                line += summary.get(tAverageIncomeString + "TenancyType" + i) + ", ";
                line += summary.get(tTotalWeeklyEligibleRentAmountString + "TenancyType" + i) + ", ";
                line += summary.get(tTotalWeeklyEligibleRentAmountGreaterThanZeroCountString + "TenancyType" + i) + ", ";
                line += summary.get(tAverageWeeklyEligibleRentAmountString + "TenancyType" + i) + ", ";
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
            header += tAllEthnicGroupCountString[i] + ", ";
        }
        for (int i = 1; i < nEG; i++) {
            header += tHBEthnicGroupCountString[i] + ", ";
        }
        for (int i = 1; i < nEG; i++) {
            header += tCTBEthnicGroupCountString[i] + ", ";
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
                line += summary.get(tAllEthnicGroupCountString[i]) + ", ";
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(tHBEthnicGroupCountString[i]) + ", ";
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(tCTBEthnicGroupCountString[i]) + ", ";
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
            header += tAllTenancyTypeClaimantCountString[i] + ", ";
            if (i == 8) {
                header += tAllTenancyType8InLS277NSClaimantCountString + ", ";
            }
        }
        for (int i = 1; i < nTT; i++) {
            header += tHBTenancyTypeClaimantCountString[i] + ", ";
            if (i == 8) {
                header += tHBTenancyType8InLS277NSClaimantCountString + ", ";
            }
        }
        for (int i = 1; i < nTT; i++) {
            header += tCTBTenancyTypeClaimantCountString[i] + ", ";
            if (i == 8) {
                header += tCTBTenancyType8InLS277NSClaimantCountString + ", ";
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
                line += summary.get(tAllTenancyTypeClaimantCountString[i]) + ", ";
                if (i == 8) {
                    line += summary.get(tAllTenancyType8InLS277NSClaimantCountString) + ", ";
                }
            }
            for (int i = 1; i < nTT; i++) {
                line += summary.get(tHBTenancyTypeClaimantCountString[i]) + ", ";
                if (i == 8) {
                    line += summary.get(tHBTenancyType8InLS277NSClaimantCountString) + ", ";
                }
            }
            for (int i = 1; i < nTT; i++) {
                line += summary.get(tCTBTenancyTypeClaimantCountString[i]) + ", ";
                if (i == 8) {
                    line += summary.get(tCTBTenancyType8InLS277NSClaimantCountString) + ", ";
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
            result += tAllCountString + ", ";
            result += tHBCountString + ", ";
            result += tCTBCountString + ", ";
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
            result += summary.get(tCouncilCount1String) + ", ";
            result += summary.get(tCouncilLinkedRecordCount1String) + ", ";
            result += summary.get(RSLFilename1String) + ", ";
            result += summary.get(tRSLCount1String) + ", ";
            result += summary.get(tRSLLinkedRecordCount1String) + ", ";
            result += summary.get(tAllCount1String) + ", ";
//                    (Integer.valueOf(summary.get("CouncilCount"))
//                    + Integer.valueOf(summary.get("RSLCount")))) + ", ";
            result += summary.get(tAllLinkedRecordCount1String) + ", ";
        } else {
            result += summary.get(tAllCountString) + ", ";
            result += summary.get(tHBCountString) + ", ";
            result += summary.get(tCTBCountString) + ", ";
        }
        return result;
    }
}
