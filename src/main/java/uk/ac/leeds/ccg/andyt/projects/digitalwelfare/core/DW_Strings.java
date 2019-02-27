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

/**
 * A class for holding all Strings used in the DigitalWelfare project. It is
 * intended that there is only one instance of this class which is accessed via
 * the common instance of DW_Environment.
 *
 * @author geoagdt
 */
public class DW_Strings extends Generic_Strings {

    public static final String sBinaryFileExtension = ".dat";

    /**
     * Shortcode for Council
     */
    public static final String sCouncil = "C";

    /**
     * Short code for CheckedPreviousPostcode.
     */
    public static final String sCheckedPreviousPostcode = "CPPY";

    /**
     * Short code for NotCheckedPreviousPostcode.
     */
    public static final String sCheckedPreviousPostcodeNo = "CPPN";

    /**
     * Short code for CheckedPreviousTenancyType.
     */
    public static final String sCheckedPreviousTenancyType = "CPTTY";

    /**
     * Short code for NotCheckedPreviousTenancyType.
     */
    public static final String sCheckedPreviousTenancyTypeNo = "CPTTN";

    /**
     * Short code for Council Tax Relief Benefit.
     */
    public static final String sCTB = "CR";

    /**
     * Short code for GroupedYes.
     */
    public static final String sGrouped = "GY";

    /**
     * Short code for GroupedNo.
     */
    public static final String sGroupedNo = "GN";

    /**
     * Short code for Housing Benefit.
     */
    public static final String sHB = "HB";

    /**
     * Short code for Include999.
     */
    public static final String sInclude999 = "Include999";

    /**
     * Short code for Exclude999.
     */
    public static final String sExclude999 = "Exclude999";

    /**
     * Short code for HBGeneralAggregateStatistics.
     */
    public static final String sHBGeneralAggregateStatistics = "HBGeneralAggregateStatistics";

    /**
     * Short code for PostcodeChanged.
     */
    public static final String sPostcodeChanged = "PCY";
    //public final String sPostcodeChanged = "PostcodeChanged";

    /**
     * Short code for PostcodeChanges.
     */
    public static final String sPostcodeChanges = "PCs";

    /**
     * Short code for PostcodeUnchanged.
     */
    public static final String sPostcodeChangedNo = "PCN";

    /**
     * Short code for Registered Social Landlord.
     */
    public static final String sRSL = "R";

    /**
     * Short code for StyleCommon.
     */
    public static final String sStyleCommon = "SC";

    /**
     * Short code for StyleIndividual.
     */
    public static final String sStyleIndividual = "SI";

    /**
     * Short code for Tenancy.
     */
    public static final String sTenancy = "T";

    /**
     * Short code for TenancyType.
     */
    public static final String sTenancyType = "TT";

    /**
     * Short code for TenancyAndPostcodeChanges.
     */
    public static final String sTenancyAndPostcodeChanges = "TAPC";

    /**
     * Short code for TenancyTypeTransition.
     */
    public static final String sTenancyTypeTransition = sTenancyType + "T";

    /**
     * Short code for TenancyTypeTransitionLineGraph.
     */
    public static final String sTenancyTypeTransitionLineGraph = sTenancyTypeTransition + "LG";

    /**
     * Short code for UnderOccupied.
     */
    public static final String sU = "U";

    /**
     * For storing sOA, sLSOA, sMSOA, sStatisticalWard.
     */
    protected HashSet<String> CensusAreaAggregations;

    /**
     * "OA" - Abbreviation of Output Area.
     */
    public static final String sOA = "OA";

    /**
     * "LSOA" - Abbreviation of Lower-layer Super Output Area.
     */
    public static final String sLSOA = "LSOA";

    /**
     * "MSOA" - Abbreviation of Middle-layer Super Output Area.
     */
    public static final String sMSOA = "MSOA";

    /**
     * StatisticalWard
     */
    public static final String sStatisticalWard = "StatisticalWard";

    /**
     * Parliamentary Constituency.
     */
    public static final String sParliamentaryConstituency = "ParliamentaryConstituency";

    /**
     * PostcodeUnit
     */
    public static final String sPostcodeUnit = "PostcodeUnit";

    /**
     * PostcodeSector
     */
    public static final String sPostcodeSector = "PostcodeSector";

    /**
     * PostcodeDistrict
     */
    public static final String sPostcodeDistrict = "PostcodeDistrict";

    /**
     * "IncomeAndRentSummary"
     */
    public static final String sIncomeAndRentSummary = "IncomeAndRentSummary";

    /**
     * "All".
     */
    public static final String sAll = "All";

    /**
     * Short code for Maybe Postcode Changed.
     */
    public static final String sWithOrWithoutPostcodeChange = "MPC"; // Maybe postcode changed

    /**
     * "DW_UO_Data"
     */
    public static final String sDW_UO_Data = "DW_UO_Data";

    /**
     * "DW_UO_Set"
     */
    public static final String sDW_UO_Set = "DW_UO_Set";

    /**
     * "LineCount".
     */
    public static final String sLineCount = "LineCount";

    /**
     * "Unit".
     */
    public static final String sUnit = "Unit";

    /**
     * "ReturnFlow".
     */
    public static final String sReturnFlow = "ReturnFlow";

    /**
     * "AAN_NAA".
     */
    public static final String sAAN_NAA = "AAN_NAA";

    /**
     * UnderOccupied in April 2013
     */
    public static final String sUOInApril2013 = "UInApr13";

    /**
     * All
     */
    public static final String sAllClaimants = "All";

    /**
     * OutDistanceChurn
     */
    public static final String sOutDistanceChurn = "OutDistanceChurn";

    /**
     * AllOutChurn
     */
    public static final String sAllOutChurn = "AllOutChurn";

    /**
     * Unknown
     */
    public static final String sUnknown = "Unknown";

    /**
     * WithinDistanceChurn
     */
    public static final String sWithinDistanceChurn = "WithinDistanceChurn";

    /**
     * InDistanceChurn
     */
    public static final String sInDistanceChurn = "InDistanceChurn";

    /**
     * AllInChurn
     */
    public static final String sAllInChurn = "AllInChurn";

    /**
     * -999
     */
    public static final String sMinus999 = "-999";

    /**
     * Stable
     */
    public static final String sStable = "Stable";

    /**
     * null
     */
    public static final String snull = "null";

    /**
     * OnFlow
     */
    public static final String sOnFlow = "OnFlow";

    /**
     * NewEntrant
     */
    public static final String sNewEntrant = "NewEntrant";
    /**
     * Short code for Input.
     */
    public static final String sInput = "Input";
    /**
     * Short code for CodePoint.
     */
    public static final String sCodePoint = "CodePoint";
    /**
     * Short code for ONS Postcode Directory
     */
    public static final String S_ONSPD = "ONSPD";
    /**
     * Short code for sGrids.
     */
    public static final String sGrids = "Grids";
    /**
     * Short code for sGrids.
     */
    public static final String sGridDoubleFactory = "GridDoubleFactory";
    /**
     * Short code for SHBE.
     */
    public static final String sLogs = "Logs";
    /**
     * Short code for Look Up Tables.
     */
    public static final String sLUTs = "LUTs";
    /**
     * Short code for Line.
     */
    public static final String sLine = "Line";
    /**
     * Short code for UnderOccupied.
     */
    public static final String sUnderOccupied = "UnderOccupied";
    /**
     * Short code for Choropleth.
     */
    public static final String sChoropleth = "Choropleth";
    /**
     * Short code for AttributeData.
     */
    public static final String sAttributeData = "AttributeData";
    /**
     * Short code for Maps.
     */
    public static final String sMaps = "Maps";
    /**
     * Short code for 2011.
     */
    public static final String s2011 = "2011";
    /**
     * Short code for BoundaryData.
     */
    public static final String sBoundaryData = "BoundaryData";
    /**
     * Short code for Swap.
     */
    public static final String sSwap = "Swap";
    /**
     * Short code for Output.
     */
    public static final String sOutput = "Output";
    /**
     * Short code for Generated.
     */
    public static final String sGenerated = "Generated";
    /**
     * Short code for AdviceLeeds.
     */
    public static final String sAdviceLeeds = "AdviceLeeds";
    /**
     * Short code for Tables.
     */
    public static final String sTables = "Tables";
    /**
     * Short code for Density.
     */
    public static final String sDensity = "Density";
    /**
     * Short code for LeedsCityCouncil.
     */
    public static final String sLCC = "LCC";
    /**
     * Short code for Census.
     */
    public static final String sCensus = "Census";
    /**
     * Short code for SHBE.
     */
    public static final String sSHBE = "SHBE";
    /**
     * Short code for PostcodeChanged.
     */
    public static final String sPostcode = "Postcode";
    /**
     * Short code for Plots.
     */
    public static final String sPlots = "Plots";

    public static final String sTotal_Income = "Total_Income";
    public static final String sTotalCount_IncomeZero = "TotalCount_IncomeZero";
    public static final String sTotalCount_IncomeNonZero = "TotalCount_IncomeNonZero";
    public static final String sTotalCount_WeeklyEligibleRentAmountNonZero = "TotalCount_WeeklyEligibleRentAmountNonZero";
    public static final String sTotal_WeeklyEligibleRentAmount = "Total_WeeklyEligibleRentAmount";
    public static final String sTotalCount_WeeklyEligibleRentAmountZero = "TotalCount_WeeklyEligibleRentAmountZero";
    public static final String sAverage_NonZero_HBIncome = "Average_NonZero_HBIncome";
    public String[] sTotal_IncomeTT;
    public String[] sTotalCount_IncomeZeroTT;
    public String[] sTotalCount_IncomeNonZeroTT;
    public static final String sAverage_NonZero_Income = "Average_NonZero_Income";
    public String[] sAverage_NonZero_IncomeTT;
    public static final String sTotalCount_WeeklyHBEntitlementZero = "TotalCount_WeeklyHBEntitlementZero";
    public static final String sAverage_NonZero_WeeklyHBEntitlement = "Average_NonZero_WeeklyHBEntitlement";
    public static final String sTotal_WeeklyCTBEntitlement = "Total_WeeklyCTBEntitlement";
    public static final String sAverage_NonZero_WeeklyCTBEntitlement = "Average_NonZero_WeeklyCTBEntitlement";
    public static final String sTotalCount_WeeklyHBEntitlementNonZero = "TotalCount_WeeklyHBEntitlementNonZero";
    public String[] sTotal_WeeklyEligibleRentAmountTT;
    public String[] sTotalCount_WeeklyEligibleRentAmountNonZeroTT;
    public String[] sTotalCount_WeeklyEligibleRentAmountZeroTT;
    public static final String sAverage_NonZero_WeeklyEligibleRentAmount = "Average_NonZero_WeeklyEligibleRentAmount";
    public String[] sAverage_NonZero_WeeklyEligibleRentAmountTT;
    public static final String sTotalCount_WeeklyCTBEntitlementZero = "TotalCount_WeeklyCTBEntitlementZero";
    public static final String sTotal_WeeklyHBEntitlement = "Total_WeeklyHBEntitlement";

    public static final String sTotal_HBIncome = "Total_HBIncome";
    public static final String sTotalCount_HBIncomeZero = "TotalCount_HBIncomeZero";
    public String[] sAverage_NonZero_HBIncomeTT;
    public String[] sTotalCount_HBIncomeNonZeroTT;
    public String[] sTotal_HBIncomeTT;
    public static final String sTotal_HBWeeklyCTBEntitlement = "Total_HBWeeklyCTBEntitlement";
    public static final String sTotalCount_HBIncomeNonZero = "TotalCount_HBIncomeNonZero";
    public String[] sTotalCount_HBIncomeZeroTT;
    public static final String sTotalCount_HBWeeklyHBEntitlementNonZero = "TotalCount_HBWeeklyHBEntitlementNonZero";
    public static final String sTotalCount_HBWeeklyHBEntitlementZero = "TotalCount_HBWeeklyHBEntitlementZero";
    public String[] sTotal_HBWeeklyEligibleRentAmountTT;
    public static final String sAverage_NonZero_HBWeeklyHBEntitlement = "Average_NonZero_HBWeeklyHBEntitlement";
    public static final String sAverage_NonZero_HBWeeklyCTBEntitlement = "Average_NonZero_HBWeeklyCTBEntitlement";
    public String[] sAverage_NonZero_HBWeeklyEligibleRentAmountTT;
    public static final String sTotal_HBWeeklyEligibleRentAmount = "Total_HBWeeklyEligibleRentAmount";
    public static final String sTotalCount_HBWeeklyEligibleRentAmountZero = "TotalCount_HBWeeklyEligibleRentAmountZero";
    public static final String sTotalCount_HBWeeklyEligibleRentAmountNonZero = "TotalCount_HBWeeklyEligibleRentAmountNonZero";
    public static final String sAverage_NonZero_HBWeeklyEligibleRentAmount = "Average_NonZero_HBWeeklyEligibleRentAmount";
    public String[] sTotalCount_HBWeeklyEligibleRentAmountZeroTT;
    public String[] sTotalCount_HBWeeklyEligibleRentAmountNonZeroTT;
    public static final String sTotalCount_HBWeeklyCTBEntitlementZero = "TotalCount_HBWeeklyCTBEntitlementZero";
    public static final String sTotalCount_HBWeeklyCTBEntitlementNonZero = "TotalCount_HBWeeklyCTBEntitlementNonZero";
    public static final String sTotal_HBWeeklyHBEntitlement = "Total_HBWeeklyHBEntitlement";

    public static final String sTotal_CTBIncome = "Total_CTBIncome";
    public static final String sTotalCount_CTBIncomeZero = "TotalCount_CTBIncomeZero";
    public static final String sTotalCount_CTBIncomeNonZero = "TotalCount_CTBIncomeNonZero";
    public String[] sTotalCount_CTBIncomeZeroTT;
    public String[] sTotalCount_CTBIncomeNonZeroTT;
    public String[] sTotal_CTBIncomeTT;
    public String[] sAverage_Non_Zero_CTBIncomeTT;
    public static final String sAverage_NonZero_CTBIncome = "Average_NonZero_CTBIncome";
    public static final String sTotal_CTBWeeklyEligibleRentAmount = "Total_CTBWeeklyEligibleRentAmount";
    public static final String sAverage_NonZero_CTBWeeklyCTBEntitlement = "Average_NonZero_CTBWeeklyCTBEntitlement";
    public String[] sTotalCount_CTBWeeklyEligibleRentAmountNonZeroTT;
    public static final String sTotalCount_CTBWeeklyCTBEntitlementNonZero = "TotalCount_CTBWeeklyCTBEntitlementNonZero";
    public String[] sTotal_CTBWeeklyEligibleRentAmountTT;
    public static final String sTotalCount_CTBWeeklyCTBEntitlementZero = "TotalCount_CTBWeeklyCTBEntitlementZero";
    public static final String sTotalCount_CTBWeeklyHBEntitlementNonZero = "TotalCount_CTBWeeklyHBEntitlementNonZero";
    public String[] sTotalCount_CTBWeeklyEligibleRentAmountZeroTT;
    public static final String sTotal_CTBWeeklyCTBEntitlement = "Total_CTBWeeklyCTBEntitlement";
    public static final String sAverage_NonZero_CTBWeeklyHBEntitlement = "Average_NonZero_CTBWeeklyHBEntitlement";
    public static final String sAverage_NonZero_CTBWeeklyEligibleRentAmount = "Average_NonZero_CTBWeeklyEligibleRentAmount";
    public String[] sAverage_NonZero_CTBWeeklyEligibleRentAmountTT;
    public static final String sTotal_CTBWeeklyHBEntitlement = "Total_CTBWeeklyHBEntitlement";
    public static final String sTotalCount_CTBWeeklyHBEntitlementZero = "TotalCount_CTBWeeklyHBEntitlementZero";
    public static final String sTotalCount_WeeklyCTBEntitlementNonZero = "TotalCount_WeeklyCTBEntitlementNonZero";
    public static final String sTotalCount_CTBWeeklyEligibleRentAmountZero = "TotalCount_CTBWeeklyEligibleRentAmountZero";
    public static final String sTotalCount_CTBWeeklyEligibleRentAmountNonZero = "TotalCount_CTBWeeklyEligibleRentAmountNonZero";

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

    public static ArrayList<String> getHB_CTB() {
        ArrayList<String> r;
        r = new ArrayList<>();
        r.add(sHB);
        r.add(sCTB);
        return r;
    }

}
