/*
 * Copyright (C) 2016 geoagdt.
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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core;

import java.util.ArrayList;
import java.util.HashSet;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Strings;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.core.SHBE_Strings;

/**
 * A class for holding all Strings used in the DigitalWelfare project. It is
 * intended that there is only one instance of this class which is accessed via
 * the common instance of DW_Environment.
 *
 * @author geoagdt
 */
public class DW_Strings extends Generic_Strings {

    public SHBE_Strings SHBE_Strings;

    /**
     * Short code for sCommaSpace.
     */
    public String sCommaSpace = ", ";

    /**
     * Short code for BinaryFileExtension.
     */
    public String sBinaryFileExtension = ".dat";

    /**
     * Short code for All.
     */
    public final String sA = "A";

//    /**
//     * Short code for All.
//     */
//    public final String sAll = "All";
    /**
     * Short code for Both.
     */
    public final String sB = "B";

    /**
     * Short code for Council.
     */
    public final String sCouncil = "C";
    //public final String sCouncil = "Council";

    /**
     * Short code for CheckedPreviousPostcode.
     */
    public final String sCheckedPreviousPostcode = "CPPY";
    //public final String sCheckedPreviousPostcode = "CheckedPreviousPostcode";

    /**
     * Short code for NotCheckedPreviousPostcode.
     */
    public final String sCheckedPreviousPostcodeNo = "CPPN";
    //public final String sCheckedPreviousPostcodeNo = "NotCheckedPreviousPostcode";

    /**
     * Short code for CheckedPreviousTenancyType.
     */
    public final String sCheckedPreviousTenancyType = "CPTTY";
    //public final String sCheckedPreviousTenancyType = "CheckedPreviousTenancyType";

    /**
     * Short code for NotCheckedPreviousTenancyType.
     */
    public final String sCheckedPreviousTenancyTypeNo = "CPTTN";
    //public final String sCheckedPreviousTenancyTypeNo = "NotCheckedPreviousTenancyType";

    /**
     * Short code for Council Tax Relief Benefit.
     */
    public final String sCTB = "CR";

    /**
     * Short code for GroupedYes.
     */
    public final String sGrouped = "GY";
    //public final String sGrouped = "Grouped";

    /**
     * Short code for GroupedNo.
     */
    public final String sGroupedNo = "GN";
    //public final String sGroupedNo = "Ungrouped";

    /**
     * Short code for Housing Benefit.
     */
    public final String sHB = "HB";

    /**
     * Short code for Include999.
     */
    public final String sInclude999 = "Include999";

    /**
     * Short code for Exclude999.
     */
    public final String sExclude999 = "Exclude999";

    /**
     * Short code for HBGeneralAggregateStatistics.
     */
    public final String sHBGeneralAggregateStatistics = "HBGeneralAggregateStatistics";

    /**
     * Short code for IncludeSameTenancy.
     */
    public final String sIncludeSameTenancy = "IncludeSameTenancy";

    /**
     * Short code for NotIncludeSameTenancy.
     */
    public final String sNotIncludeSameTenancy = "NotIncludeSameTenancy";

    /**
     * Short code for IncludeMonthlySinceApril2013.
     */
    public final String sIncludeMonthlySinceApril2013 = "IMU";

    /**
     * Short code for sInclude2MonthlySinceApril2013Offset0.
     */
    public final String sInclude2MonthlySinceApril2013Offset0 = "I2MU0";

    /**
     * Short code for sInclude2MonthlySinceApril2013Offset0.
     */
    public final String sInclude2MonthlySinceApril2013Offset1 = "I2MU1";

    /**
     * Short code for sIncludeStartEndSinceApril2013.
     */
    public final String sIncludeStartEndSinceApril2013 = "ISEU";

    /**
     * Short code for sIncludingTenancyTransitionBreaks.
     */
    public final String sIncludingTenancyTransitionBreaks = "ITTBY";

    /**
     * Short code for sIncludingTenancyTransitionBreaksNo.
     */
    public final String sIncludingTenancyTransitionBreaksNo = "ITTBN";

    /**
     * Short code for Include6Monthly.
     */
    public final String sInclude6Monthly = "I6M";

    /**
     * Short code for IncludeMonthly.
     */
    public final String sIncludeMonthly = "IM";

    /**
     * Short code for IncludeApril2013May2013.
     */
    public final String sIncludeApril2013May2013 = "IncludeApril2013May2013";

    /**
     * Short code for IncludeYearly.
     */
    public final String sIncludeYearly = "IY";

    /**
     * Short code for IncludeAll.
     */
    public final String sIncludeAll = "IA";

    /**
     * Short code for Include3Monthly.
     */
    public final String sInclude3Monthly = "I3M";


    /**
     * Short code for PostcodeChanged.
     */
    public final String sPostcodeChanged = "PCY";
    //public final String sPostcodeChanged = "PostcodeChanged";

    /**
     * Short code for PostcodeChanges.
     */
    public final String sPostcodeChanges = "PCs";
    //public final String sPostcodeChanges = "PostcodeChanges";

    /**
     * Short code for PostcodeUnchanged.
     */
    public final String sPostcodeChangedNo = "PCN";
    //public final String sPostcodeChangedNo = "PostcodeUnchanged";

    /**
     * Short code for Registered Social Landlord.
     */
    public final String sRSL = "R";
    //public final String sRSL = "RSL";

    /**
     * Short code for StyleCommon.
     */
    public final String sStyleCommon = "SC";

    /**
     * Short code for StyleIndividual.
     */
    public final String sStyleIndividual = "SI";

    /**
     * Short code for Tenancy.
     */
    public final String sTenancy = "T";
    //public final String sTenancy = "Tenancy";

    /**
     * Short code for TenancyType.
     */
    public final String sTenancyType = "TT";

    /**
     * Short code for TenancyAndPostcodeChanges.
     */
    public final String sTenancyAndPostcodeChanges = "TAPC";
    //public final String sTenancyAndPostcodeChanges = "TenancyAndPostcodeChanges";

    /**
     * Short code for TenancyTypeTransition.
     */
    public final String sTenancyTypeTransition = sTenancyType + "T";
    //public final String sTenancyTypeTransition = "TenancyTypeTransition";

    /**
     * Short code for TenancyTypeTransitionLineGraph.
     */
    public final String sTenancyTypeTransitionLineGraph = sTenancyTypeTransition + "LG";
    //public final String sTenancyTypeTransitionLineGraph = "TenancyTypeTransitionLineGraph";

    /**
     * Short code for UnderOccupied.
     */
    public final String sU = "U";

    /**
     * "Data".
     */
    public final String sData = "Data";

    /**
     * For storing sOA, sLSOA, sMSOA, sStatisticalWard.
     */
    protected HashSet<String> CensusAreaAggregations;

    /**
     * "OA" - Abbreviation of Output Area.
     */
    public final String sOA = "OA";

    /**
     * "LSOA" - Abbreviation of Lower-layer Super Output Area.
     */
    public final String sLSOA = "LSOA";

    /**
     * "MSOA" - Abbreviation of Middle-layer Super Output Area.
     */
    public final String sMSOA = "MSOA";

    /**
     * StatisticalWard
     */
    public final String sStatisticalWard = "StatisticalWard";

    /**
     * Parliamentary Constituency.
     */
    public final String sParliamentaryConstituency = "ParliamentaryConstituency";

    /**
     * PostcodeUnit
     */
    public final String sPostcodeUnit = "PostcodeUnit";

    /**
     * PostcodeSector
     */
    public final String sPostcodeSector = "PostcodeSector";

    /**
     * PostcodeDistrict
     */
    public final String sPostcodeDistrict = "PostcodeDistrict";

    public final String sCountOfNewSHBEClaimsPSI = "CountOfNewSHBEClaimsPSI";

    public final String sTotal_Income = "Total_Income";
    public final String sAverage_NonZero_Income = "Average_NonZero_Income";
    public final String sTotalCount_IncomeZero = "TotalCount_IncomeZero";
    public final String sTotalCount_IncomeNonZero = "TotalCount_IncomeNonZero";
    public final String sTotalCount_WeeklyEligibleRentAmountNonZero = "TotalCount_WeeklyEligibleRentAmountNonZero";
    public final String sTotal_WeeklyEligibleRentAmount = "Total_WeeklyEligibleRentAmount";
    public final String sTotalCount_WeeklyEligibleRentAmountZero = "TotalCount_WeeklyEligibleRentAmountZero";
    public final String sAverage_NonZero_WeeklyEligibleRentAmount = "Average_NonZero_WeeklyEligibleRentAmount";
    public final String sAverage_NonZero_HBIncome = "Average_NonZero_HBIncome";
    public String[] sTotal_IncomeTT;
    public String[] sTotalCount_IncomeZeroTT;
    public String[] sTotalCount_IncomeNonZeroTT;
    public String[] sAverage_NonZero_IncomeTT;
    public final String sTotalCount_WeeklyHBEntitlementZero = "TotalCount_WeeklyHBEntitlementZero";
    public final String sAverage_NonZero_WeeklyHBEntitlement = "Average_NonZero_WeeklyHBEntitlement";
    public final String sTotal_WeeklyCTBEntitlement = "Total_WeeklyCTBEntitlement";
    public final String sAverage_NonZero_WeeklyCTBEntitlement = "Average_NonZero_WeeklyCTBEntitlement";
    public final String sTotalCount_WeeklyHBEntitlementNonZero = "TotalCount_WeeklyHBEntitlementNonZero";
    public String[] sTotal_WeeklyEligibleRentAmountTT;
    public String[] sTotalCount_WeeklyEligibleRentAmountNonZeroTT;
    public String[] sTotalCount_WeeklyEligibleRentAmountZeroTT;
    public String[] sAverage_NonZero_WeeklyEligibleRentAmountTT;
    public final String sTotalCount_WeeklyCTBEntitlementZero = "TotalCount_WeeklyCTBEntitlementZero";
    public final String sTotal_WeeklyHBEntitlement = "Total_WeeklyHBEntitlement";

    public final String sTotal_HBIncome = "Total_HBIncome";
    public final String sTotalCount_HBIncomeZero = "TotalCount_HBIncomeZero";
    public String[] sAverage_NonZero_HBIncomeTT;
    public String[] sTotalCount_HBIncomeNonZeroTT;
    public String[] sTotal_HBIncomeTT;
    public final String sTotal_HBWeeklyCTBEntitlement = "Total_HBWeeklyCTBEntitlement";
    public final String sTotalCount_HBIncomeNonZero = "TotalCount_HBIncomeNonZero";
    public String[] sTotalCount_HBIncomeZeroTT;
    public final String sTotalCount_HBWeeklyHBEntitlementNonZero = "TotalCount_HBWeeklyHBEntitlementNonZero";
    public final String sTotalCount_HBWeeklyHBEntitlementZero = "TotalCount_HBWeeklyHBEntitlementZero";
    public String[] sTotal_HBWeeklyEligibleRentAmountTT;
    public final String sAverage_NonZero_HBWeeklyHBEntitlement = "Average_NonZero_HBWeeklyHBEntitlement";
    public final String sAverage_NonZero_HBWeeklyCTBEntitlement = "Average_NonZero_HBWeeklyCTBEntitlement";
    public String[] sAverage_NonZero_HBWeeklyEligibleRentAmountTT;
    public final String sTotal_HBWeeklyEligibleRentAmount = "Total_HBWeeklyEligibleRentAmount";
    public final String sTotalCount_HBWeeklyEligibleRentAmountZero = "TotalCount_HBWeeklyEligibleRentAmountZero";
    public final String sTotalCount_HBWeeklyEligibleRentAmountNonZero = "TotalCount_HBWeeklyEligibleRentAmountNonZero";
    public final String sAverage_NonZero_HBWeeklyEligibleRentAmount = "Average_NonZero_HBWeeklyEligibleRentAmount";
    public String[] sTotalCount_HBWeeklyEligibleRentAmountZeroTT;
    public String[] sTotalCount_HBWeeklyEligibleRentAmountNonZeroTT;
    public final String sTotalCount_HBWeeklyCTBEntitlementZero = "TotalCount_HBWeeklyCTBEntitlementZero";
    public final String sTotalCount_HBWeeklyCTBEntitlementNonZero = "TotalCount_HBWeeklyCTBEntitlementNonZero";
    public final String sTotal_HBWeeklyHBEntitlement = "Total_HBWeeklyHBEntitlement";

    public final String sTotal_CTBIncome = "Total_CTBIncome";
    public final String sTotalCount_CTBIncomeZero = "TotalCount_CTBIncomeZero";
    public final String sTotalCount_CTBIncomeNonZero = "TotalCount_CTBIncomeNonZero";
    public String[] sTotalCount_CTBIncomeZeroTT;
    public String[] sTotalCount_CTBIncomeNonZeroTT;
    public String[] sTotal_CTBIncomeTT;
    public String[] sAverage_Non_Zero_CTBIncomeTT;
    public final String sAverage_NonZero_CTBIncome = "Average_NonZero_CTBIncome";
    public final String sTotal_CTBWeeklyEligibleRentAmount = "Total_CTBWeeklyEligibleRentAmount";
    public final String sAverage_NonZero_CTBWeeklyCTBEntitlement = "Average_NonZero_CTBWeeklyCTBEntitlement";
    public String[] sTotalCount_CTBWeeklyEligibleRentAmountNonZeroTT;
    public final String sTotalCount_CTBWeeklyCTBEntitlementNonZero = "TotalCount_CTBWeeklyCTBEntitlementNonZero";
    public String[] sTotal_CTBWeeklyEligibleRentAmountTT;
    public final String sTotalCount_CTBWeeklyCTBEntitlementZero = "TotalCount_CTBWeeklyCTBEntitlementZero";
    public final String sTotalCount_CTBWeeklyHBEntitlementNonZero = "TotalCount_CTBWeeklyHBEntitlementNonZero";
    public String[] sTotalCount_CTBWeeklyEligibleRentAmountZeroTT;
    public final String sTotal_CTBWeeklyCTBEntitlement = "Total_CTBWeeklyCTBEntitlement";
    public final String sAverage_NonZero_CTBWeeklyHBEntitlement = "Average_NonZero_CTBWeeklyHBEntitlement";
    public final String sAverage_NonZero_CTBWeeklyEligibleRentAmount = "Average_NonZero_CTBWeeklyEligibleRentAmount";
    public String[] sAverage_NonZero_CTBWeeklyEligibleRentAmountTT;
    public final String sTotal_CTBWeeklyHBEntitlement = "Total_CTBWeeklyHBEntitlement";
    public final String sTotalCount_CTBWeeklyHBEntitlementZero = "TotalCount_CTBWeeklyHBEntitlementZero";
    public final String sTotalCount_WeeklyCTBEntitlementNonZero = "TotalCount_WeeklyCTBEntitlementNonZero";
    public final String sTotalCount_CTBWeeklyEligibleRentAmountZero = "TotalCount_CTBWeeklyEligibleRentAmountZero";
    public final String sTotalCount_CTBWeeklyEligibleRentAmountNonZero = "TotalCount_CTBWeeklyEligibleRentAmountNonZero";

    /**
     * "BigDecimal"
     */
    public final String sBigDecimal = "BigDecimal";

    /**
     * "HashMap"
     */
    public final String sHashMap = "HashMap";

    /**
     * "String"
     */
    public final String sString = "String";

    /**
     * "IncomeAndRentSummary"
     */
    public final String sIncomeAndRentSummary = "IncomeAndRentSummary";

    /**
     * "All".
     */
    public String sAll = "All";

    /**
     * Short code for Maybe Postcode Changed.
     */
    public final String sWithOrWithoutPostcodeChange = "MPC"; // Maybe postcode changed

    /**
     * "CountOfCTBAndHBClaims".
     */
    public final String sCountOfCTBAndHBClaims = "CountOfCTBAndHBClaims";

    /**
     * "CountOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim".
     */
    public final String sCountOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim = "CountOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim";

    /**
     * "CountOfClaimsWithClaimantsThatArePartnersInAnotherClaim".
     */
    public final String sCountOfClaimsWithClaimantsThatArePartnersInAnotherClaim = "CountOfClaimsWithClaimantsThatArePartnersInAnotherClaim";

    /**
     * "CountOfClaimsWithPartnersThatAreClaimantsInAnotherClaim".
     */
    public final String sCountOfClaimsWithPartnersThatAreClaimantsInAnotherClaim = "CountOfClaimsWithPartnersThatAreClaimantsInAnotherClaim";

    /**
     * "CountOfClaimsWithPartnersThatArePartnersInAnotherClaim".
     */
    public final String sCountOfClaimsWithPartnersThatArePartnersInAnotherClaim = "CountOfClaimsWithPartnersThatArePartnersInAnotherClaim";

    /**
     * "CountOfClaimantsInMultipleClaimsInAMonth".
     */
    public final String sCountOfClaimantsInMultipleClaimsInAMonth = "CountOfClaimantsInMultipleClaimsInAMonth";

    /**
     * "CountOfDependentsInAllClaims".
     */
    public final String sCountOfDependentsInAllClaims = "CountOfDependentsInAllClaims";

    /**
     * "CountOfPartnersInMultipleClaimsInAMonth".
     */
    public final String sCountOfPartnersInMultipleClaimsInAMonth = "CountOfPartnersInMultipleClaimsInAMonth";

    /**
     * "CountOfNonDependentsInMultipleClaimsInAMonth".
     */
    public final String sCountOfNonDependentsInMultipleClaimsInAMonth = "CountOfNonDependentsInMultipleClaimsInAMonth";

    

    /**
     * "CountOfNewClaimantPostcodes".
     */
    public final String sCountOfNewClaimantPostcodes = "CountOfNewClaimantPostcodes";

    /**
     * "CountOfNewValidMappableClaimantPostcodes".
     */
    public final String sCountOfNewValidMappableClaimantPostcodes = "CountOfNewValidMappableClaimantPostcodes";

    /**
     * "CountOfValidMappableClaimantPostcodes".
     */
    public final String sCountOfMappableClaimantPostcodes = "CountOfMappableClaimantPostcodes";

    /**
     * "CountOfNonMappableClaimantPostcodes".
     */
    public final String sCountOfNonMappableClaimantPostcodes = "CountOfNonMappableClaimantPostcodes";

    /**
     * "CountOfInvalidFormatClaimantPostcodes".
     */
    public final String sCountOfInvalidFormatClaimantPostcodes = "CountOfInvalidFormatClaimantPostcodes";

    /**
     * "CountOfClaims".
     */
    public final String sCountOfClaims = "CountOfClaims";

    /**
     * "CountOfClaimsWithPartners".
     */
    public final String sCountOfClaimsWithPartners = "CountOfClaimsWithPartners";

    /**
     * "CountOfCTBClaims".
     */
    public final String sCountOfCTBClaims = "CountOfCTBClaims";

    /**
     * "CountOfRecords".
     */
    public final String sCountOfRecords = "CountOfRecords";

    /**
     * "CountOfHBClaims".
     */
    public final String sCountOfHBClaims = "CountOfHBClaims";

    /**
     * "CountOfIncompleteDRecords".
     */
    public final String sCountOfIncompleteDRecords = "CountOfIncompleteDRecords";

    /**
     * "CountOfNonDependentsInAllClaims".
     */
    public final String sCountOfNonDependentsInAllClaims = "CountOfNonDependentsInAllClaims";

    /**
     * "CountOfUniqueDependents".
     */
    public final String sCountOfUniqueDependents = "CountOfUniqueDependents";

    /**
     * "CountOfUniqueNonDependents".
     */
    public final String sCountOfUniqueNonDependents = "CountOfUniqueNonDependents";

    /**
     * "CountOfUniqueClaimants".
     */
    public final String sCountOfUniqueClaimants = "CountOfUniqueClaimants";

    /**
     * "CountOfUniquePartners".
     */
    public final String sCountOfUniquePartners = "CountOfUniquePartners";

    /**
     * "CountOfSRecords".
     */
    public final String sCountOfSRecords = "CountOfSRecords";

    /**
     * "CountOfSRecordsWithoutDRecord".
     */
    public final String sCountOfSRecordsWithoutDRecord = "CountOfSRecordsWithoutDRecord";

    /**
     * "CountOfRecordIDsNotLoaded".
     */
    public final String sCountOfRecordIDsNotLoaded = "CountOfRecordIDsNotLoaded";

    /**
     * "CountOfSRecordsNotLoaded".
     */
    public final String sCountOfSRecordsNotLoaded = "CountOfSRecordsNotLoaded";

    /**
     * "DW_UO_Data"
     */
    public final String sDW_UO_Data = "DW_UO_Data";

    /**
     * "DW_UO_Set"
     */
    public final String sDW_UO_Set = "DW_UO_Set";

    /**
     * "LineCount".
     */
    public final String sLineCount = "LineCount";

    /**
     * "TotalIncomeGreaterThanZeroCount".
     */
    public final String sTotalIncomeGreaterThanZeroCount = "TotalIncomeGreaterThanZeroCount";

    /**
     * "TotalWeeklyEligibleRentAmount".
     */
    public final String sTotalWeeklyEligibleRentAmount = "TotalWeeklyEligibleRentAmount";

    /**
     * "TotalWeeklyEligibleRentAmountGreaterThanZeroCount".
     */
    public final String sTotalWeeklyEligibleRentAmountGreaterThanZeroCount = "TotalWeeklyEligibleRentAmountGreaterThanZeroCount";

    /**
     * "TotalIncome".
     */
    public final String sTotalIncome = "TotalIncome";

    /**
     * "CountIndividuals".
     */
    public final String sCountOfIndividuals = "CountOfIndividuals";

    /**
     * "Records".
     */
    public final String sRecords = "Records";

    /**
     * "Unit".
     */
    public final String sUnit = "Unit";

    /**
     * "ReturnFlow".
     */
    public final String sReturnFlow = "ReturnFlow";
    
    /**
     * "AAN_NAA".
     */
    public final String sAAN_NAA = "AAN_NAA";
    
    /**
     * UnderOccupied in April 2013
     */
    public final String sUOInApril2013 = "UInApr13";
    
    /**
     * All
     */
    public final String sAllClaimants = "All";
    
    /**
     * OutDistanceChurn
     */
    public final String sOutDistanceChurn = "OutDistanceChurn";
    
    /**
     * AllOutChurn
     */
    public final String sAllOutChurn = "AllOutChurn";
    
    /**
     * Unknown
     */
    public final String sUnknown = "Unknown";
    
    /**
     * WithinDistanceChurn
     */
    public final String sWithinDistanceChurn = "WithinDistanceChurn";
    
    /**
     * InDistanceChurn
     */
    public final String sInDistanceChurn = "InDistanceChurn";
    
    /**
     * AllInChurn
     */
    public final String sAllInChurn = "AllInChurn";
    
    /**
     * -999
     */
    public final String sMinus999 = "-999";
    
    /**
     * Stable
     */
    public final String sStable = "Stable";
    
    /**
     * null
     */
    public final String snull = "null";
    
    /**
     * OnFlow
     */
    public final String sOnFlow = "OnFlow";
    
    /**
     * NewEntrant
     */
    public final String sNewEntrant = "NewEntrant";
    /**
     * Short code for Input.
     */
    public final String sInput = "Input";
    /**
     * Short code for CodePoint.
     */
    public final String sCodePoint = "CodePoint";
    /**
     * Short code for ONS Postcode Directory
     */
    public final String S_ONSPD = "ONSPD";
    /**
     * Short code for sGrids.
     */
    public final String sGrids = "Grids";
    /**
     * Short code for sGrids.
     */
    public final String sGridDoubleFactory = "GridDoubleFactory";
    /**
     * Short code for SHBE.
     */
    public final String sLogs = "Logs";
    /**
     * Short code for Look Up Tables.
     */
    public final String sLUTs = "LUTs";
    /**
     * Short code for Line.
     */
    public final String sLine = "Line";
    /**
     * Short code for UnderOccupied.
     */
    public final String sUnderOccupied = "UnderOccupied";
    /**
     * Short code for Choropleth.
     */
    public final String sChoropleth = "Choropleth";
    /**
     * Short code for AttributeData.
     */
    public final String sAttributeData = "AttributeData";
    /**
     * Short code for Maps.
     */
    public final String sMaps = "Maps";
    /**
     * Short code for 2011.
     */
    public final String s2011 = "2011";
    /**
     * Short code for BoundaryData.
     */
    public final String sBoundaryData = "BoundaryData";
    /**
     * Short code for Swap.
     */
    public final String sSwap = "Swap";
    /**
     * Short code for Output.
     */
    public final String sOutput = "Output";
    /**
     * Short code for Generated.
     */
    public final String sGenerated = "Generated";
    /**
     * Short code for AdviceLeeds.
     */
    public final String sAdviceLeeds = "AdviceLeeds";
    /**
     * Short code for Tables.
     */
    public final String sTables = "Tables";
    /**
     * Short code for Density.
     */
    public final String sDensity = "Density";
    /**
     * Short code for LeedsCityCouncil.
     */
    public final String sLCC = "LCC";
    /**
     * Short code for Census.
     */
    public final String sCensus = "Census";
    /**
     * Short code for SHBE.
     */
    public final String sSHBE = "SHBE";
    /**
     * Short code for PostcodeChanged.
     */
    public final String sPostcode = "Postcode";
    /**
     * Short code for Plots.
     */
    public final String sPlots = "Plots";
    
    

//    /**
//     * Short code for UnderOccupied.
//     */
//    public final String sUO = "UO";
    public DW_Strings() {
    }

    /**
     * For getting an {@code ArrayList<String>} of PaymentTypes.
     *
     * @return
     */
    public HashSet<String> getCensusAreaAggregations() {
        if (CensusAreaAggregations == null) {
            CensusAreaAggregations = new HashSet<>();
            CensusAreaAggregations.add(sOA);
            CensusAreaAggregations.add(sLSOA);
            CensusAreaAggregations.add(sMSOA);
            CensusAreaAggregations.add(sStatisticalWard);
        }
        return CensusAreaAggregations;
    }


    public ArrayList<String> getHB_CTB() {
        ArrayList<String> result;
        result = new ArrayList<>();
        result.add(sHB);
        result.add(sCTB);
        return result;
    }

}
