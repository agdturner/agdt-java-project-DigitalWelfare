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
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;
import uk.ac.leeds.ccg.andyt.math.Generic_BigDecimal;
import uk.ac.leeds.ccg.andyt.generic.util.Generic_Time;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.core.SHBE_ID;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_PersonID;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Records;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_D_Record;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Data;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Handler;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Record;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 * A class for summarising SHBE data.
 *
 * @author geoagdt
 */
public class DW_Summary extends DW_Object {

    // For convenience.
    protected SHBE_Data SHBE_Data;
    protected SHBE_Handler SHBE_Handler;
    protected DW_Strings Strings;
    protected DW_Files Files;
    protected ONSPD_Postcode_Handler Postcode_Handler;
    protected SHBE_TenancyType_Handler SHBE_TenancyType_Handler;

    protected final int decimalPlacePrecisionForAverage = 3;
    protected final int decimalPlacePrecisionForPercentage = 3;
    // Special vars
    protected final String postcodeLS277NS = "LS27 7NS";
    protected final String s0 = "0";
    protected final String sSummaryTables = "SummaryTables";
    protected final String sSpace = " ";

    protected final String sSingleTimePSI = "SingleTimePSI";

    // Compare2Times PTI
    // CTB
    protected int TotalCount_CTB_PTIToPTI;
    protected int TotalCount_CTB_PTIToPTS;
    protected int TotalCount_CTB_PTIToPTO;
    protected int TotalCount_CTB_PTIToNull;
    protected int TotalCount_CTB_PTSToPTI;
    protected int TotalCount_CTB_PTSToPTS;
    protected int TotalCount_CTB_PTSToPTO;
    protected int TotalCount_CTB_PTSToNull;
    protected int TotalCount_CTB_PTOToPTI;
    protected int TotalCount_CTB_PTOToPTS;
    protected int TotalCount_CTB_PTOToPTO;
    protected int TotalCount_CTB_PTOToNull;
    protected int TotalCount_CTB_NotSuspendedToNotSuspended;
    protected int TotalCount_CTB_NotSuspendedToSuspended;
    protected int TotalCount_CTB_NotSuspendedToNull;
    protected int TotalCount_CTB_SuspendedToNotSuspended;
    protected int TotalCount_CTB_SuspendedToSuspended;
    protected int TotalCount_CTB_SuspendedToNull;

    // HB
    protected int TotalCount_HB_PTIToPTI;
    protected int TotalCount_HB_PTIToPTS;
    protected int TotalCount_HB_PTIToPTO;
    protected int TotalCount_HB_PTIToNull;
    protected int TotalCount_HB_PTSToPTI;
    protected int TotalCount_HB_PTSToPTS;
    protected int TotalCount_HB_PTSToPTO;
    protected int TotalCount_HB_PTSToNull;
    protected int TotalCount_HB_PTOToPTI;
    protected int TotalCount_HB_PTOToPTS;
    protected int TotalCount_HB_PTOToPTO;
    protected int TotalCount_HB_PTOToNull;
    protected int TotalCount_HB_NotSuspendedToNotSuspended;
    protected int TotalCount_HB_NotSuspendedToSuspended;
    protected int TotalCount_HB_NotSuspendedToNull;
    protected int TotalCount_HB_SuspendedToNotSuspended;
    protected int TotalCount_HB_SuspendedToSuspended;
    protected int TotalCount_HB_SuspendedToNull;

    // Compare2Times PTI
    // CTB
    protected final String sTotalCount_CTB_NotSuspendedToNotSuspended = "TotalCount_CTB_NotSuspendedToNotSuspended";
    protected final String sTotalCount_CTB_NotSuspendedToSuspended = "TotalCount_CTB_NotSuspendedToSuspended";
    protected final String sTotalCount_CTB_NotSuspendedToNull = "TotalCount_CTB_NotSuspendedToNull";
    protected final String sTotalCount_CTB_SuspendedToNotSuspended = "TotalCount_CTB_SuspendedToNotSuspended";
    protected final String sTotalCount_CTB_SuspendedToSuspended = "TotalCount_CTB_SuspendedToSuspended";
    protected final String sTotalCount_CTB_SuspendedToNull = "TotalCount_CTB_SuspendedToNull";
    protected final String sTotalCount_CTB_PTIToPTI = "TotalCount_CTB_PTIToPTI";
    protected final String sTotalCount_CTB_PTIToPTS = "TotalCount_CTB_PTIToPTS";
    protected final String sTotalCount_CTB_PTIToPTO = "TotalCount_CTB_PTIToPTO";
    protected final String sTotalCount_CTB_PTIToNull = "TotalCount_CTB_PTIToNull";
    protected final String sTotalCount_CTB_PTSToPTI = "TotalCount_CTB_PTSToPTI";
    protected final String sTotalCount_CTB_PTSToPTS = "TotalCount_CTB_PTSToPTS";
    protected final String sTotalCount_CTB_PTSToPTO = "TotalCount_CTB_PTSToPTO";
    protected final String sTotalCount_CTB_PTSToNull = "TotalCount_CTB_PTSToNull";
    protected final String sTotalCount_CTB_PTOToPTI = "TotalCount_CTB_PTOToPTI";
    protected final String sTotalCount_CTB_PTOToPTS = "TotalCount_CTB_PTOToPTS";
    protected final String sTotalCount_CTB_PTOToPTO = "TotalCount_CTB_PTOToPTO";
    protected final String sTotalCount_CTB_PTOToNull = "TotalCount_CTB_PTOToNull";
    // HB
    protected final String sTotalCount_HB_NotSuspendedToNotSuspended = "TotalCount_HB_NotSuspendedToNotSuspended";
    protected final String sTotalCount_HB_NotSuspendedToSuspended = "TotalCount_HB_NotSuspendedToSuspended";
    protected final String sTotalCount_HB_NotSuspendedToNull = "TotalCount_HB_NotSuspendedToNull";
    protected final String sTotalCount_HB_SuspendedToNotSuspended = "TotalCount_HB_SuspendedToNotSuspended";
    protected final String sTotalCount_HB_SuspendedToSuspended = "TotalCount_HB_SuspendedToSuspended";
    protected final String sTotalCount_HB_SuspendedToNull = "TotalCount_HB_SuspendedToNull";
    protected final String sTotalCount_HB_PTIToPTI = "TotalCount_HB_PTIToPTI";
    protected final String sTotalCount_HB_PTIToPTS = "TotalCount_HB_PTIToPTS";
    protected final String sTotalCount_HB_PTIToPTO = "TotalCount_HB_PTIToPTO";
    protected final String sTotalCount_HB_PTIToNull = "TotalCount_HB_PTIToNull";
    protected final String sTotalCount_HB_PTSToPTI = "TotalCount_HB_PTSToPTI";
    protected final String sTotalCount_HB_PTSToPTS = "TotalCount_HB_PTSToPTS";
    protected final String sTotalCount_HB_PTSToPTO = "TotalCount_HB_PTSToPTO";
    protected final String sTotalCount_HB_PTSToNull = "TotalCount_HB_PTSToNull";
    protected final String sTotalCount_HB_PTOToPTI = "TotalCount_HB_PTOToPTI";
    protected final String sTotalCount_HB_PTOToPTS = "TotalCount_HB_PTOToPTS";
    protected final String sTotalCount_HB_PTOToPTO = "TotalCount_HB_PTOToPTO";
    protected final String sTotalCount_HB_PTOToNull = "TotalCount_HB_PTOToNull";

    // Main vars
    // Counter Strings
    // HouseholdSize
    protected final String sAllTotalHouseholdSize = "AllTotalHouseholdSize";
    protected final String sAllAverageHouseholdSize = "AllAverageHouseholdSize";
    protected final String sHBTotalHouseholdSize = "HBTotalHouseholdSize";
    protected final String sHBAverageHouseholdSize = "HBAverageHouseholdSize";
    protected final String sCTBTotalHouseholdSize = "CTBTotalHouseholdSize";
    protected final String sCTBAverageHouseholdSize = "CTBAverageHouseholdSize";
    protected String[] sTotal_HouseholdSizeTT;
    protected String[] sAverage_HouseholdSizeTT;
    // PSI
    protected String[] sTotalCount_AllPSI;
    protected String[] sTotalCount_HBPSI;
    protected String[] sTotalCount_CTBPSI;
    protected String[] sPercentageOfAll_AllPSI;
    protected String[] sPercentageOfHB_HBPSI;
    protected String[] sPercentageOfCTB_CTBPSI;
    // PSITT
    protected String[][] sTotalCount_PSITT;
    protected String[][] sPercentageOfAll_PSITT;
    protected String[][] sPercentageOfTT_PSITT;
    protected String[][] sPercentageOfHB_PSITT;
    protected String[][] sPercentageOfCTB_PSITT;
    // DisabilityPremiumAwardTT
    protected String[] sTotalCount_DisabilityPremiumAwardTT;
    protected String[] sPercentageOfAll_DisabilityPremiumAwardTT;
    protected String[] sPercentageOfHB_DisabilityPremiumAwardTT;
    protected String[] sPercentageOfCTB_DisabilityPremiumAwardTT;
    protected String[] sPercentageOfTT_DisabilityPremiumAwardTT;
    // SevereDisabilityPremiumAwardTT
    protected String[] sTotalCount_SevereDisabilityPremiumAwardTT;
    protected String[] sPercentageOfAll_SevereDisabilityPremiumAwardTT;
    protected String[] sPercentageOfHB_SevereDisabilityPremiumAwardTT;
    protected String[] sPercentageOfCTB_SevereDisabilityPremiumAwardTT;
    protected String[] sPercentageOfTT_SevereDisabilityPremiumAwardTT;
    // DisabledChildPremiumAwardTT
    protected String[] sTotalCount_DisabledChildPremiumAwardTT;
    protected String[] sPercentageOfAll_DisabledChildPremiumAwardTT;
    protected String[] sPercentageOfHB_DisabledChildPremiumAwardTT;
    protected String[] sPercentageOfCTB_DisabledChildPremiumAwardTT;
    protected String[] sPercentageOfTT_DisabledChildPremiumAwardTT;
    // EnhancedDisabilityPremiumAwardTT
    protected String[] sTotalCount_EnhancedDisabilityPremiumAwardTT;
    protected String[] sPercentageOfAll_EnhancedDisabilityPremiumAwardTT;
    protected String[] sPercentageOfHB_EnhancedDisabilityPremiumAwardTT;
    protected String[] sPercentageOfCTB_EnhancedDisabilityPremiumAwardTT;
    protected String[] sPercentageOfTT_EnhancedDisabilityPremiumAwardTT;
    // DisabilityAwards
    protected final String sTotalCount_AllDisabilityAward = "TotalCount_AllDisabilityAward";
    protected final String sPercentageOfAll_AllDisabilityAward = "PercentageOfAll_AllDisabilityAward";
    // DisabilityPremiumAwards
    protected final String sTotalCount_AllDisabilityPremiumAward = "TotalCount_AllDisabilityPremiumAward";
    protected final String sPercentageOfAll_AllDisabilityPremiumAward = "PercentageOfAll_AllDisabilityPremiumAward";
    // SevereDisabilityPremiumAwards
    protected final String sTotalCount_AllSevereDisabilityPremiumAward = "TotalCount_AllSevereDisabilityPremiumAward";
    protected final String sPercentageOfAll_AllSevereDisabilityPremiumAward = "PercentageOfAll_AllSevereDisabilityPremiumAward";
    // DisabledChildPremiumAwards
    protected final String sTotalCount_AllDisabledChildPremiumAward = "TotalCount_AllDisabledChildPremiumAward";
    protected final String sPercentageOfAll_AllDisabledChildPremiumAward = "PercentageOfAll_AllDisabledChildPremiumAward";
    // EnhancedDisabilityPremiumAwards
    protected final String sTotalCount_AllEnhancedDisabilityPremiumAward = "TotalCount_AllEnhancedDisabilityPremiumAward";
    protected final String sPercentageOfAll_AllEnhancedDisabilityPremiumAward = "PercentageOfAll_AllEnhancedDisabilityPremiumAward";
    // DisabilityPremiumAwardHBTTs
    protected final String sTotalCount_DisabilityPremiumAwardHBTTs = "TotalCount_DisabilityPremiumAwardHBTTs";
    protected final String sPercentageOfAll_DisabilityPremiumAwardHBTTs = "PercentageOfAll_DisabilityPremiumAwardHBTTs";
    protected final String sPercentageOfHB_DisabilityPremiumAwardHBTTs = "PercentageOfHB_DisabilityPremiumAwardHBTTs";
    // SevereDisabilityPremiumAwardHBTTs
    protected final String sTotalCount_SevereDisabilityPremiumAwardHBTTs = "TotalCount_SevereDisabilityPremiumAwardHBTTs";
    protected final String sPercentageOfAll_SevereDisabilityPremiumAwardHBTTs = "PercentageOfAll_SevereDisabilityPremiumAwardHBTTs";
    protected final String sPercentageOfHB_SevereDisabilityPremiumAwardHBTTs = "PercentageOfHB_SevereDisabilityPremiumAwardHBTTs";
    // DisabledChildPremiumAwardHBTTs
    protected final String sTotalCount_DisabledChildPremiumAwardHBTTs = "TotalCount_DisabledChildPremiumAwardHBTTs";
    protected final String sPercentageOfAll_DisabledChildPremiumAwardHBTTs = "PercentageOfAll_DisabledChildPremiumAwardHBTTs";
    protected final String sPercentageOfHB_DisabledChildPremiumAwardHBTTs = "PercentageOfHB_DisabledChildPremiumAwardHBTTs";
    // EnhancedDisabilityPremiumAwardHBTTs
    protected final String sTotalCount_EnhancedDisabilityPremiumAwardHBTTs = "TotalCount_EnhancedDisabilityPremiumAwardHBTTs";
    protected final String sPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs = "PercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs";
    protected final String sPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs = "PercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs";
    // DisabilityPremiumAwardCTBTTs
    protected final String sTotalCount_DisabilityPremiumAwardCTBTTs = "TotalCount_DisabilityPremiumAwardCTBTTs";
    protected final String sPercentageOfAll_DisabilityPremiumAwardCTBTTs = "PercentageOfAll_DisabilityPremiumAwardCTBTTs";
    protected final String sPercentageOfCTB_DisabilityPremiumAwardCTBTTs = "PercentageOfCTB_DisabilityPremiumAwardCTBTTs";
    // SevereDisabilityPremiumAwardCTBTTs
    protected final String sTotalCount_SevereDisabilityPremiumAwardCTBTTs = "TotalCount_SevereDisabilityPremiumAwardCTBTTs";
    protected final String sPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs = "PercentageOfAll_SevereDisabilityPremiumAwardCTBTTs";
    protected final String sPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs = "PercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs";
    // DisabledChildPremiumAwardCTBTTs
    protected final String sTotalCount_DisabledChildPremiumAwardCTBTTs = "TotalCount_DisabledChildPremiumAwardCTBTTs";
    protected final String sPercentageOfAll_DisabledChildPremiumAwardCTBTTs = "PercentageOfAll_DisabledChildPremiumAwardCTBTTs";
    protected final String sPercentageOfCTB_DisabledChildPremiumAwardCTBTTs = "PercentageOfCTB_DisabledChildPremiumAwardCTBTTs";
    // EnhancedDisabilityPremiumAwardCTBTTs
    protected final String sTotalCount_EnhancedDisabilityPremiumAwardCTBTTs = "TotalCount_EnhancedDisabilityPremiumAwardCTBTTs";
    protected final String sPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs = "PercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs";
    protected final String sPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs = "PercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs";
    // DisabilityPremiumAwardSocialTTs
    protected final String sTotalCount_DisabilityPremiumAwardSocialTTs = "TotalCount_DisabilityPremiumAwardSocialTTs";
    protected final String sPercentageOfAll_DisabilityPremiumAwardSocialTTs = "PercentageOfAll_DisabilityPremiumAwardSocialTTs";
    protected final String sPercentageOfHB_DisabilityPremiumAwardSocialTTs = "PercentageOfHB_DisabilityPremiumAwardSocialTTs";
    protected final String sPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs = "PercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs";
    // DisabilityPremiumAwardPrivateDeregulatedTTs
    protected final String sTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs = "TotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs";
    protected final String sPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs";
    protected final String sPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs";
    protected final String sPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs";
    // SevereDisabilityPremiumAwardSocialTTs
    protected final String sTotalCount_SevereDisabilityPremiumAwardSocialTTs = "TotalCount_SevereDisabilityPremiumAwardSocialTTs";
    protected final String sPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs = "PercentageOfAll_SevereDisabilityPremiumAwardSocialTTs";
    protected final String sPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs = "PercentageOfHB_SevereDisabilityPremiumAwardSocialTTs";
    protected final String sPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs = "PercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs";
    // SevereDisabilityPremiumAwardPrivateDeregulatedTTs
    protected final String sTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "TotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected final String sPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected final String sPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected final String sPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs";
    // DisabledChildPremiumAwardSocialTTs
    protected final String sTotalCount_DisabledChildPremiumAwardSocialTTs = "TotalCount_DisabledChildPremiumAwardSocialTTs";
    protected final String sPercentageOfAll_DisabledChildPremiumAwardSocialTTs = "PercentageOfAll_DisabledChildPremiumAwardSocialTTs";
    protected final String sPercentageOfHB_DisabledChildPremiumAwardSocialTTs = "PercentageOfHB_DisabledChildPremiumAwardSocialTTs";
    protected final String sPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs = "PercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs";
    // DisabledChildPremiumAwardPrivateDeregulatedTTs
    protected final String sTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs = "TotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    protected final String sPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs = "PercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    protected final String sPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs = "PercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    protected final String sPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs = "PercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs";
    // EnhancedDisabilityPremiumAwardSocialTTs
    protected final String sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs = "TotalCount_EnhancedDisabilityPremiumAwardSocialTTs";
    protected final String sPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs = "PercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs";
    protected final String sPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs = "PercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs";
    protected final String sPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs = "PercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs";
    // EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs
    protected final String sTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "TotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected final String sPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected final String sPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    protected final String sPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs = "PercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs";
    // DisabilityAwardTT
    protected String[] sTotalCount_DisabilityAwardTT;
    protected String[] sPercentageOfAll_DisabilityAwardTT;
    protected String[] sPercentageOfHB_DisabilityAwardTT;
    protected String[] sPercentageOfCTB_DisabilityAwardTT;
    protected String[] sPercentageOfTT_DisabilityAwardTT;
    // DisabilityAwardHBTTs
    protected final String sTotalCount_DisabilityAwardHBTTs = "TotalCount_DisabilityAwardHBTTs";
    protected final String sPercentageOfAll_DisabilityAwardHBTTs = "PercentageOfAll_DisabilityAwardHBTTs";
    protected final String sPercentageOfHB_DisabilityAwardHBTTs = "PercentageOfHB_DisabilityAwardHBTTs";
    // DisabilityAwardCTBTTs
    protected final String sTotalCount_DisabilityAwardCTBTTs = "TotalCount_DisabilityAwardCTBTTs";
    protected final String sPercentageOfAll_DisabilityAwardCTBTTs = "PercentageOfAll_DisabilityAwardCTBTTs";
    protected final String sPercentageOfCTB_DisabilityAwardCTBTTs = "PercentageOfCTB_DisabilityAwardCTBTTs";
    // DisabilityAwardSocialTTs
    protected final String sTotalCount_DisabilityAwardSocialTTs = "TotalCount_DisabilityAwardSocialTTs";
    protected final String sPercentageOfAll_DisabilityAwardSocialTTs = "PercentageOfAll_DisabilityAwardSocialTTs";
    protected final String sPercentageOfHB_DisabilityAwardSocialTTs = "PercentageOfHB_DisabilityAwardSocialTTs";
    protected final String sPercentageOfSocialTTs_DisabilityAwardSocialTTs = "PercentageOfSocialTTs_DisabilityAwardSocialTTs";
    // DisabilityAwardPrivateDeregulatedTTs
    protected final String sTotalCount_DisabilityAwardPrivateDeregulatedTTs = "TotalCount_DisabilityAwardPrivateDeregulatedTTs";
    protected final String sPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs = "PercentageOfAll_DisabilityAwardPrivateDeregulatedTTs";
    protected final String sPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs = "PercentageOfHB_DisabilityAwardPrivateDeregulatedTTs";
    protected final String sPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs = "PercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs";
    // WeeklyEligibleCouncilTaxAmount
    protected final String sAllTotalWeeklyEligibleCouncilTaxAmount = "AllTotalWeeklyEligibleCouncilTaxAmount";
    protected final String sTotalCount_AllWeeklyEligibleCouncilTaxAmountNonZero = "TotalCount_AllWeeklyEligibleCouncilTaxAmountNonZero";
    protected final String sTotalCount_AllWeeklyEligibleCouncilTaxAmountZero = "TotalCount_AllWeeklyEligibleCouncilTaxAmountZero";
    protected final String sAllAverageWeeklyEligibleCouncilTaxAmount = "AllAverageWeeklyEligibleCouncilTaxAmount";
    protected final String sHBTotalWeeklyEligibleCouncilTaxAmount = "TotalCount_HBWeeklyEligibleCouncilTaxAmount";
    protected final String sTotalCount_HBWeeklyEligibleCouncilTaxAmountNonZero = "TotalCount_HBWeeklyEligibleCouncilTaxAmountNonZero";
    protected final String sTotalCount_HBWeeklyEligibleCouncilTaxAmountZero = "TotalCount_HBWeeklyEligibleCouncilTaxAmountZero";
    protected final String sHBAverageWeeklyEligibleCouncilTaxAmount = "HBAverageWeeklyEligibleCouncilTaxAmount";
    protected final String sCTBTotalWeeklyEligibleCouncilTaxAmount = "CTBTotalWeeklyEligibleCouncilTaxAmount";
    protected final String sTotalCount_CTBWeeklyEligibleCouncilTaxAmountNonZero = "TotalCount_CTBWeeklyEligibleCouncilTaxAmountNonZero";
    protected final String sTotalCount_CTBWeeklyEligibleCouncilTaxAmountZero = "TotalCount_CTBWeeklyEligibleCouncilTaxAmountZero";
    protected final String sCTBAverageWeeklyEligibleCouncilTaxAmount = "CTBAverageWeeklyEligibleCouncilTaxAmount";
    // ContractualRentAmount
    protected final String sAllTotalContractualRentAmount = "AllTotalContractualRentAmount";
    protected final String sAllTotalCountContractualRentAmountNonZeroCount = "TotalCount_AllContractualRentAmountNonZero";
    protected final String sAllTotalCountContractualRentAmountZeroCount = "TotalCount_AllContractualRentAmountZero";
    protected final String sAllAverageContractualRentAmount = "AllAverageContractualRentAmount";
    protected final String sHBTotalContractualRentAmount = "HBTotalContractualRentAmount";
    protected final String sTotalCount_HBContractualRentAmountNonZeroCount = "TotalCount_HBContractualRentAmountNonZero";
    protected final String sTotalCount_HBContractualRentAmountZeroCount = "TotalCount_HBContractualRentAmountZero";
    protected final String sHBAverageContractualRentAmount = "HBAverageContractualRentAmount";
    protected final String sCTBTotalContractualRentAmount = "CTBTotalContractualRentAmount";
    protected final String sTotalCount_CTBContractualRentAmountNonZeroCount = "TotalCount_CTBContractualRentAmountNonZero";
    protected final String sTotalCount_CTBContractualRentAmountZeroCount = "TotalCount_CTBContractualRentAmountZero";
    protected final String sCTBAverageContractualRentAmount = "CTBAverageContractualRentAmount";
    // WeeklyAdditionalDiscretionaryPayment
    protected final String sAllTotalWeeklyAdditionalDiscretionaryPayment = "AllTotalWeeklyAdditionalDiscretionaryPayment";
    protected final String sTotalCount_AllWeeklyAdditionalDiscretionaryPaymentNonZero = "TotalCount_AllWeeklyAdditionalDiscretionaryPaymentNonZero";
    protected final String sTotalCount_AllWeeklyAdditionalDiscretionaryPaymentZero = "TotalCount_AllWeeklyAdditionalDiscretionaryPaymentZero";
    protected final String sAllAverageWeeklyAdditionalDiscretionaryPayment = "AllAverageWeeklyAdditionalDiscretionaryPayment";
    protected final String sHBTotalWeeklyAdditionalDiscretionaryPayment = "HBTotalWeeklyAdditionalDiscretionaryPayment";
    protected final String sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentNonZero = "TotalCount_HBWeeklyAdditionalDiscretionaryPaymentNonZero";
    protected final String sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentZero = "TotalCount_HBWeeklyAdditionalDiscretionaryPaymentZero";
    protected final String sHBAverageWeeklyAdditionalDiscretionaryPayment = "HBAverageWeeklyAdditionalDiscretionaryPayment";
    protected final String sCTBTotalWeeklyAdditionalDiscretionaryPayment = "CTBTotalWeeklyAdditionalDiscretionaryPayment";
    protected final String sTotalCount_CTBWeeklyAdditionalDiscretionaryPaymentNonZero = "TotalCount_CTBWeeklyAdditionalDiscretionaryPaymentNonZero";
    protected final String sTotalCount_CTBWeeklyAdditionalDiscretionaryPaymentZero = "TotalCount_CTBWeeklyAdditionalDiscretionaryPaymentZero";
    protected final String sCTBAverageWeeklyAdditionalDiscretionaryPayment = "CTBAverageWeeklyAdditionalDiscretionaryPayment";
    // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
    protected final String sAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected final String sTotalCount_AllWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "TotalCount_AllWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    protected final String sTotalCount_AllWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "TotalCount_AllWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    protected final String sAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "AllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected final String sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected final String sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "TotalCount_HBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    protected final String sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "TotalCount_HBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    protected final String sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "HBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected final String sCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    protected final String sTotalCount_CTBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero = "TotalCount_CTBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero";
    protected final String sTotalCount_CTBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero = "TotalCount_CTBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero";
    protected final String sCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = "CTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability";
    // Employment
    protected final String sTotalCount_AllClaimantsEmployed = "TotalCount_AllClaimantsEmployed";
    protected final String sPercentageOfAll_AllClaimantsEmployed = "PercentageOfAll_AllClaimantsEmployed";
    protected final String sTotalCount_AllClaimantsSelfEmployed = "TotalCount_AllClaimantsSelfEmployed";
    protected final String sPercentageOfAll_AllClaimantsSelfEmployed = "PercentageOfAll_AllClaimantsSelfEmployed";
    protected final String sTotalCount_AllClaimantsStudents = "TotalCount_AllClaimantsStudents";
    protected final String sPercentageOfAll_AllClaimantsStudents = "PercentageOfAll_AllClaimantsStudents";
    protected final String sTotalCount_AllLHACases = "TotalCount_AllLHACases";
    protected final String sPercentageOfAll_AllLHACases = "PercentageOfAll_AllLHACases";
    protected final String sTotalCount_HBClaimantsEmployed = "TotalCount_HBClaimantsEmployed";
    protected final String sPercentageOfHB_HBClaimantsEmployed = "PercentageOfHB_HBClaimantsEmployed";
    protected final String sTotalCount_HBClaimantsSelfEmployed = "TotalCount_HBClaimantsSelfEmployed";
    protected final String sPercentageOfHB_HBClaimantsSelfEmployed = "PercentageOfHB_HBClaimantsSelfEmployed";
    protected final String sTotalCount_HBClaimantsStudents = "TotalCount_HBClaimantsStudents";
    protected final String sPercentageOfHB_HBClaimantsStudents = "PercentageOfHB_HBClaimantsStudents";
    protected final String sTotalCount_HBLHACases = "TotalCount_HBLHACases";
    protected final String sPercentageOfHB_HBLHACases = "PercentageOfHB_HBLHACases";
    protected final String sTotalCount_CTBClaimantsEmployed = "TotalCount_CTBClaimantsEmployed";
    protected final String sPercentageOfCTB_CTBClaimantsEmployed = "PercentageOfCTB_CTBClaimantsEmployed";
    protected final String sTotalCount_CTBClaimantsSelfEmployed = "TotalCount_CTBClaimantsSelfEmployed";
    protected final String sPercentageOfCTB_CTBClaimantsSelfEmployed = "PercentageOfCTB_CTBClaimantsSelfEmployed";
    protected final String sTotalCount_CTBClaimantsStudents = "TotalCount_CTBClaimantsStudents";
    protected final String sPercentageOfCTB_CTBClaimantsStudents = "PercentageOfCTB_CTBClaimantsStudents";
    protected final String sTotalCount_CTBLHACases = "TotalCount_CTBLHACases";
    protected final String sPercentageOfCTB_CTBLHACases = "PercentageOfCTB_CTBLHACases";
    // Counts
    protected final String sAllCount0 = "AllCount0";
    protected final String sHBCount0 = "HBAndCTBCount0";
    protected final String sCTBCount0 = "CTBOnlyCount0";
    protected final String sAllCount1 = "AllCount1";
    protected final String sHBCount1 = "HBAndCTBCount1";
    protected final String sHBPTICount1 = "HBPTICount1";
    protected final String sHBPTSCount1 = "HBPTSCount1";
    protected final String sHBPTOCount1 = "HBPTOCount1";
    protected final String sCTBCount1 = "CTBOnlyCount1";
    protected final String sCTBPTICount1 = "CTBPTICount1";
    protected final String sCTBPTSCount1 = "CTBPTSCount1";
    protected final String sCTBPTOCount1 = "CTBPTOCount1";
    protected final String sTotalCount_SocialTTsClaimant = "TotalCount_SocialTTsClaimant";
    protected final String sPercentageOfAll_SocialTTsClaimant = "PercentageOfAll_SocialTTsClaimant";
    protected final String sPercentageOfHB_SocialTTsClaimant = "PercentageOfHB_SocialTTsClaimant";
    protected final String sTotalCount_PrivateDeregulatedTTsClaimant = "TotalCount_PrivateDeregulatedTTsClaimant";
    protected final String sPercentageOfAll_PrivateDeregulatedTTsClaimant = "PercentageOfAll_PrivateDeregulatedTTsClaimant";
    protected final String sPercentageOfHB_PrivateDeregulatedTTsClaimant = "PercentageOfHB_PrivateDeregulatedTTsClaimant";
    protected String[] sTotalCount_AllEthnicGroupClaimant;
    protected String[] sTotalCount_AllEthnicGroupSocialTTClaimant;
    protected String[] sTotalCount_AllEthnicGroupPrivateDeregulatedTTClaimant;
    protected String[] sPercentageOfAll_EthnicGroupClaimant;
    protected String[] sPercentageOfSocialTT_EthnicGroupSocialTTClaimant;
    protected String[] sPercentageOfPrivateDeregulatedTT_EthnicGroupPrivateDeregulatedTTClaimant;
    protected String[][] sTotalCount_AllEthnicGroupClaimantTT;
    protected String[][] sPercentageOfEthnicGroup_EthnicGroupClaimantTT;
    protected String[][] sPercentageOfTT_EthnicGroupClaimantTT;

    // Demographics
    // HB
    protected String[] sTotalCount_HBEthnicGroupClaimant;
    protected String[] sPercentageOfHB_HBEthnicGroupClaimant;
    // CTB
    protected String[] sTotalCount_CTBEthnicGroupClaimant;
    protected String[] sPercentageOfCTB_CTBEthnicGroupClaimant;
    // Files
    protected final String sSHBEFilename00 = "SHBEFilename00";
    protected final String sSHBEFilename0 = "SHBEFilename0";
    protected final String sSHBEFilename1 = "SHBEFilename1";
    // Key Counts
    protected String[] sTotalCount_ClaimantTT;
    protected String[] sPercentageOfAll_ClaimantTT;
    protected String[] sPercentageOfHB_ClaimantTT;
    protected String[] sPercentageOfCTB_ClaimantTT;
    // Postcode
    protected final String sTotalCount_AllPostcodeValidFormat = "TotalCount_AllPostcodeValidFormat";
    protected final String sTotalCount_AllPostcodeInvalidFormat = "TotalCount_AllPostcodeInvalidFormat";
    protected final String sPercentageOfAllCount1_AllPostcodeValidFormat = "PercentageOfAllCount1_AllPostcodeValidFormat";
    protected final String sTotalCount_AllPostcodeValid = "sTotalCount_AllPostcodeValid";
    protected final String sTotalCount_AllPostcodeInvalid = "sTotalCount_AllPostcodeInvalid";
    protected final String sPercentageOfAllCount1_AllPostcodeValid = "sPercentageOfAllCount1_AllPostcodeValid";
    protected final String sTotalCount_HBPostcodeValidFormat = "TotalCount_HBPostcodeValidFormat";
    protected final String sTotalCount_HBPostcodeInvalidFormat = "TotalCount_HBPostcodeInvalidFormat";
    protected final String sPercentageOfHBCount1_HBPostcodeValidFormat = "PercentageOfHBCount1_HBPostcodeValidFormat";
    protected final String sTotalCount_HBPostcodeValid = "sTotalCount_HBPostcodeValid";
    protected final String sTotalCount_HBPostcodeInvalid = "sTotalCount_HBPostcodeInvalid";
    protected final String sPercentageOfHBCount1_HBPostcodeValid = "sPercentageOfHBCount1_HBPostcodeValid";
    protected final String sTotalCount_CTBPostcodeValidFormat = "TotalCount_CTBPostcodeValidFormat";
    protected final String sTotalCount_CTBPostcodeInvalidFormat = "TotalCount_CTBPostcodeInvalidFormat";
    protected final String sPercentageOfCTBCount1_CTBPostcodeValidFormat = "PercentageOfCTBCount1_CTBPostcodeValidFormat";
    protected final String sTotalCount_CTBPostcodeValid = "sTotalCount_CTBPostcodeValid";
    protected final String sTotalCount_CTBPostcodeInvalid = "sTotalCount_CTBPostcodeInvalid";
    protected final String sPercentageOfCTBCount1_CTBPostcodeValid = "sPercentageOfCTBCount1_CTBPostcodeValid";

    // Compare 2 Times
    // All TT
    protected final String sTotalCount_AllTTChangeClaimant = "TotalCount_AllTTChangeClaimant";
    protected final String sAllPercentageOfAll_TTChangeClaimant = "AllPercentageOfAll_TTChangeClaimant";
    protected final String sTotalCount_HBTTsToCTBTTs = "TotalCount_HBTTsToCTBTTs";
    protected final String sPercentageOfHBCount0_HBTTsToCTBTTs = "PercentageOfHBCount0_HBTTsToCTBTTs";
    protected final String sTotalCount_CTBTTsToHBTTs = "TotalCount_CTBTTsToHBTTs";
    protected final String sPercentageOfCTBCount0_CTBTTsToHBTTs = "PercentageOfCTBCount0_CTBTTsToHBTTs";
    // HB TT
    protected final String sTotalCount_HBTTChangeClaimant = "TotalCount_HBTTChangeClaimant";
    protected final String sPercentageOfHBCount0_HBTTChangeClaimant = "PercentageOfHBCount0_HBTTChangeClaimant";
    protected final String sTotalCount_Minus999TTToSocialTTs = "TotalCount_Minus999TTToSocialTTs";
    protected final String sTotalCount_Minus999TTToPrivateDeregulatedTTs = "TotalCount_Minus999TTToPrivateDeregulatedTTs";
    protected final String sTotalCount_HBTTsToHBTTs = "TotalCount_HBTTsToHBTTs";
    protected final String sPercentageOfHBCount0_HBTTsToHBTTs = "PercentageOfHBCount0_HBTTsToHBTTs";
    protected final String sTotalCount_HBTTsToMinus999TT = "TotalCount_HBTTsToMinus999TT";
    protected final String sPercentageOfHBCount0_HBTTsToMinus999TT = "PercentageOfHBCount0_HBTTsToMinus999TT";
    protected final String sTotalCount_SocialTTsToPrivateDeregulatedTTs = "TotalCount_SocialTTsToPrivateDeregulatedTTs";
    protected final String sPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs = "PercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs";
    protected final String sTotalCount_SocialTTsToMinus999TT = "TotalCount_SocialTTsToMinus999TT";
    protected final String sPercentageOfSocialTTs_SocialTTsToMinus999TT = "PercentageOfSocialTTs_SocialTTsToMinus999TT";
    protected final String sTotalCount_PrivateDeregulatedTTsToSocialTTs = "TotalCount_PrivateDeregulatedTTsToSocialTTs";
    protected final String sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs = "PercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs";
    protected final String sTotalCount_PrivateDeregulatedTTsToMinus999TT = "TotalCount_PrivateDeregulatedTTsToMinus999TT";
    protected final String sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT = "PercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT";
    protected final String sTotalCount_TT1ToPrivateDeregulatedTTs = "TotalCount_TT1ToPrivateDeregulatedTTs";
    protected final String sPercentageOfTT1_TT1ToPrivateDeregulatedTTs = "PercentageOfTT1_TT1ToPrivateDeregulatedTTs";
    protected final String sTotalCount_TT4ToPrivateDeregulatedTTs = "TotalCount_TT4ToPrivateDeregulatedTTs";
    protected final String sPercentageOfTT4_TT4ToPrivateDeregulatedTTs = "PercentageOfTT4_TT4ToPrivateDeregulatedTTs";
    protected final String sTotalCount_PrivateDeregulatedTTsToTT1 = "TotalCount_PrivateDeregulatedTTsToTT1";
    protected final String sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1 = "PercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1";
    protected final String sTotalCount_PrivateDeregulatedTTsToTT4 = "TotalCount_PrivateDeregulatedTTsToTT4";
    protected final String sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4 = "PercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4";
    protected final String sTotalCount_TT1ToTT4 = "TotalCount_TT1ToTT4";
    protected final String sPercentageOfTT1_TT1ToTT4 = "PercentageOfTT1_TT1ToTT4";
    protected final String sTotalCount_TT4ToTT1 = "TotalCount_TT4ToTT1";
    protected final String sPercentageOfTT4_TT4ToTT1 = "PercentageOfTT4_TT4ToTT1";
    protected final String sTotalCount_PostcodeChangeWithinSocialTTs = "TotalCount_PostcodeChangeWithinSocialTTs";
    protected final String sPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs = "PercentageOfSocialTTs_PostcodeChangeWithinSocialTTs";
    protected final String sTotalCount_PostcodeChangeWithinTT1 = "TotalCount_PostcodeChangeWithinTT1";
    protected final String sPercentageOfTT1_PostcodeChangeWithinTT1 = "PercentageOfTT1_PostcodeChangeWithinTT1";
    protected final String sTotalCount_PostcodeChangeWithinTT4 = "TotalCount_PostcodeChangeWithinTT4";
    protected final String sPercentageOfTT4_PostcodeChangeWithinTT4 = "PercentageOfTT4_PostcodeChangeWithinTT4";
    protected final String sTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs = "TotalCount_PostcodeChangeWithinPrivateDeregulatedTTs";
    protected final String sPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs = "PercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs";
    // CTB TT
    protected final String sTotalCount_CTBTTChangeClaimant = "TotalCount_CTBTTChangeClaimant";
    protected final String PercentageOfCTBCount0_CTBTTChangeClaimant = "PercentageOfCTB_CTBTTChangeClaimant";
    protected final String sTotalCount_Minus999TTToCTBTTs = "TotalCount_Minus999TTToCTBTTs";
    protected final String sTotalCount_SocialTTsToCTBTTs = "TotalCount_SocialTTsToCTBTTs";
    protected final String sPercentageOfSocialTTs_SocialTTsToCTBTTs = "PercentageOfSocialTTs_SocialTTsToCTBTTs";
    protected final String sTotalCount_TT1ToCTBTTs = "TotalCount_TT1ToCTBTTs";
    protected final String sPercentageOfTT1_TT1ToCTBTTs = "PercentageOfTT1_TT1ToCTBTTs";
    protected final String sTotalCount_TT4ToCTBTTs = "TotalCount_TT4ToCTBTTs";
    protected final String sPercentageOfTT4_TT4ToCTBTTs = "PercentageOfTT4_TT4ToCTBTTs";
    protected final String sTotalCount_PrivateDeregulatedTTsToCTBTTs = "TotalCount_PrivateDeregulatedTTsToCTBTTs";
    protected final String sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs = "PercentageOfPrivateDeregulated_PrivateDeregulatedTTsToCTBTTs";
    protected final String sTotalCount_CTBTTsToSocialTTs = "TotalCount_CTBTTsToSocialTTs";
    protected final String sTotalCount_CTBTTsToMinus999TT = "TotalCount_CTBTTsToMinus999TT";
    protected final String sPercentageOfCTBCount0_CTBTTsToSocialTTs = "PercentageOfCTBCount0_CTBTTsToSocialTTs";
    protected final String sPercentageOfCTBCount0_CTBTTsToMinus999TT = "PercentageOfCTBCount0_CTBTTsToMinus999TT";
    protected final String sTotalCount_CTBTTsToTT1 = "TotalCount_CTBTTsToTT1";
    protected final String sPercentageOfCTBCount0_CTBTTsToTT1 = "PercentageOfCTBCount0_CTBTTsToTT1";
    protected final String sTotalCount_CTBTTsToTT4 = "TotalCount_CTBTTsToTT4";
    protected final String sPercentageOfCTBCount0_CTBTTsToTT4 = "PercentageOfCTBCount0_CTBTTsToTT4";
    protected final String sTotalCount_CTBTTsToPrivateDeregulatedTTs = "TotalCount_CTBTTsToPrivateDeregulatedTypes";
    protected final String sPercentageOfCTBCount0_CTBTTsToPrivateDeregulatedTTs = "PercentageOfCTBCount0_CTBTTsToPrivateDeregulatedTypes";
    // All Postcode
    protected final String sTotalCount_AllPostcode0ValidPostcode1Valid = "TotalCount_AllPostcode0ValidPostcode1Valid";
    protected final String sPercentageOfAllCount1_AllPostcode0ValidPostcode1Valid = "PercentageOfAllCount1_AllPostcode0ValidPostcode1Valid";
    protected final String sTotalCount_AllPostcode0ValidPostcode1ValidPostcodeNotChanged = "TotalCount_AllPostcode0ValidPostcode1ValidPostcodeNotChanged";
    protected final String sPercentageOfAllCount1_AllPostcode0ValidPostcode1ValidPostcodeNotChanged = "PercentageOfAllCount1_AllPostcode0ValidPostcode1ValidPostcodeNotChanged";
    protected final String sTotalCount_AllPostcode0ValidPostcode1ValidPostcodeChange = "TotalCount_AllPostcode0ValidPostcode1ValidPostcodeChange";
    protected final String sPercentageOfAllCount1_AllPostcode0ValidPostcode1ValidPostcodeChange = "PercentageOfAllCount1_AllPostcode0ValidPostcode1ValidPostcodeChange";
    protected final String sTotalCount_AllPostcode0ValidPostcode1NotValid = "TotalCount_AllPostcode0ValidPostcode1NotValid";
    protected final String sPercentageOfAllCount1_AllPostcode0ValidPostcode1NotValid = "PercentageOfAllCount1_AllPostcode0ValidPostcode1NotValid";
    protected final String sTotalCount_AllPostcode0NotValidPostcode1Valid = "TotalCount_AllPostcode0NotValidPostcode1Valid";
    protected final String sPercentageOfAllCount1_AllPostcode0NotValidPostcode1Valid = "PercentageOfAllCount1_AllPostcode0NotValidPostcode1Valid";
    protected final String sTotalCount_AllPostcode0NotValidPostcode1NotValid = "TotalCount_AllPostcode0NotValidPostcode1NotValid";
    protected final String sPercentageOfAllCount1_AllPostcode0NotValidPostcode1NotValid = "PercentageOfAllCount1_AllPostcode0NotValidPostcode1NotValid";
    protected final String sTotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeNotChanged = "TotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected final String sPercentageOfAllCount1_AllPostcode0NotValidPostcode1NotValidPostcodeNotChanged = "PercentageOfAllCount1_AllPostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected final String sTotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeChanged = "TotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeChanged";
    protected final String sPercentageOfAllCount1_AllPostcode0NotValidPostcode1NotValidPostcodeChanged = "PercentageOfAllCount1_AllPostcode0NotValidPostcode1NotValidPostcodeChanged";
    protected final String sTotalCount_AllPostcode0ValidPostcode1DNE = "TotalCount_AllPostcode0ValidPostcode1DNE";
    protected final String sPercentageOfAllCount1_AllPostcode0ValidPostcode1DNE = "PercentageOfAllCount1_AllPostcode0ValidPostcode1DNE";
    protected final String sTotalCount_AllPostcode0DNEPostcode1Valid = "TotalCount_AllPostcode0DNEPostcode1Valid";
    protected final String sPercentageOfAllCount1_AllPostcode0DNEPostcode1Valid = "PercentageOfAllCount1_AllPostcode0DNEPostcode1Valid";
    protected final String sTotalCount_AllPostcode0DNEPostcode1DNE = "TotalCount_AllPostcode0DNEPostcode1DNE";
    protected final String sPercentageOfAllCount1_AllPostcode0DNEPostcode1DNE = "PercentageOfAllCount1_AllPostcode0DNEPostcode1DNE";
    protected final String sTotalCount_AllPostcode0DNEPostcode1NotValid = "TotalCount_AllPostcode0DNEPostcode1NotValid";
    protected final String sPercentageOfAllCount1_AllPostcode0DNEPostcode1NotValid = "PercentageOfAllCount1_AllPostcode0DNEPostcode1NotValid";
    protected final String sTotalCount_AllPostcode0NotValidPostcode1DNE = "TotalCount_AllPostcode0NotValidPostcode1DNE";
    protected final String sPercentageOfAllCount1_AllPostcode0NotValidPostcode1DNE = "PercentageOfAllCount1_AllPostcode0NotValidPostcode1DNE";
    // HB Postcode
    protected final String sTotalCount_HBPostcode0ValidPostcode1Valid = "TotalCount_HBPostcode0ValidPostcode1Valid";
    protected final String sPercentageOfHBCount1_HBPostcode0ValidPostcode1Valid = "PercentageOfHBCount1_HBPostcode0ValidPostcode1Valid";
    protected final String sTotalCount_HBPostcode0ValidPostcode1ValidPostcodeNotChanged = "TotalCount_HBPostcode0ValidPostcode1ValidPostcodeNotChanged";
    protected final String sPercentageOfHBCount1_HBPostcode0ValidPostcode1ValidPostcodeNotChanged = "PercentageOfHBCount1_HBPostcode0ValidPostcode1ValidPostcodeNotChanged";
    protected final String sTotalCount_HBPostcode0ValidPostcode1ValidPostcodeChange = "TotalCount_HBPostcode0ValidPostcode1ValidPostcodeChange";
    protected final String sPercentageOfHBCount1_HBPostcode0ValidPostcode1ValidPostcodeChange = "PercentageOfHBCount1_HBPostcode0ValidPostcode1ValidPostcodeChange";
    protected final String sTotalCount_HBPostcode0ValidPostcode1NotValid = "TotalCount_HBPostcode0ValidPostcode1NotValid";
    protected final String sPercentageOfHBCount1_HBPostcode0ValidPostcode1NotValid = "PercentageOfHBCount1_HBPostcode0ValidPostcode1NotValid";
    protected final String sTotalCount_HBPostcode0NotValidPostcode1Valid = "TotalCount_HBPostcode0NotValidPostcode1Valid";
    protected final String sPercentageOfHBCount1_HBPostcode0NotValidPostcode1Valid = "PercentageOfHBCount1_HBPostcode0NotValidPostcode1Valid";
    protected final String sTotalCount_HBPostcode0NotValidPostcode1NotValid = "TotalCount_HBPostcode0NotValidPostcode1NotValid";
    protected final String sPercentageOfHBCount1_HBPostcode0NotValidPostcode1NotValid = "PercentageOfHBCount1_HBPostcode0NotValidPostcode1NotValid";
    protected final String sTotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeNotChanged = "TotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected final String sPercentageOfHBCount1_HBPostcode0NotValidPostcode1NotValidPostcodeNotChanged = "PercentageOfHBCount1_HBPostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected final String sTotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeChanged = "TotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeChanged";
    protected final String sPercentageOfHBCount1_HBPostcode0NotValidPostcode1NotValidPostcodeChanged = "PercentageOfHBCount1_HBPostcode0NotValidPostcode1NotValidPostcodeChanged";
    protected final String sTotalCount_HBPostcode0ValidPostcode1DNE = "TotalCount_HBPostcode0ValidPostcode1DNE";
    protected final String sPercentageOfHBCount1_HBPostcode0ValidPostcode1DNE = "PercentageOfHBCount1_HBPostcode0ValidPostcode1DNE";
    protected final String sTotalCount_HBPostcode0DNEPostcode1Valid = "TotalCount_HBPostcode0DNEPostcode1Valid";
    protected final String sPercentageOfHBCount1_HBPostcode0DNEPostcode1Valid = "PercentageOfHBCount1_HBPostcode0DNEPostcode1Valid";
    protected final String sTotalCount_HBPostcode0DNEPostcode1DNE = "TotalCount_HBPostcode0DNEPostcode1DNE";
    protected final String sPercentageOfHBCount1_HBPostcode0DNEPostcode1DNE = "PercentageOfHBCount1_HBPostcode0DNEPostcode1DNE";
    protected final String sTotalCount_HBPostcode0DNEPostcode1NotValid = "TotalCount_HBPostcode0DNEPostcode1NotValid";
    protected final String sPercentageOfHBCount1_HBPostcode0DNEPostcode1NotValid = "PercentageOfHBCount1_HBPostcode0DNEPostcode1NotValid";
    protected final String sTotalCount_HBPostcode0NotValidPostcode1DNE = "TotalCount_HBPostcode0NotValidPostcode1DNE";
    protected final String sPercentageOfHBCount1_HBPostcode0NotValidPostcode1DNE = "PercentageOfHBCount1_HBPostcode0NotValidPostcode1DNE";
    // CTB Postcode
    protected final String sTotalCount_CTBPostcode0ValidPostcode1Valid = "TotalCount_CTBPostcode0ValidPostcode1Valid";
    protected final String sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1Valid = "PercentageOfCTBCount1_CTBPostcode0ValidPostcode1Valid";
    protected final String sTotalCount_CTBPostcode0ValidPostcode1ValidPostcodeNotChanged = "TotalCount_CTBPostcode0ValidPostcode1ValidPostcodeNotChanged";
    protected final String sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1ValidPostcodeNotChanged = "PercentageOfCTBCount1_CTBPostcode0ValidPostcode1ValidPostcodeNotChanged";
    protected final String sTotalCount_CTBPostcode0ValidPostcode1ValidPostcodeChanged = "TotalCount_CTBPostcode0ValidPostcode1ValidPostcodeChanged";
    protected final String sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1ValidPostcodeChanged = "PercentageOfCTBCount1_CTBPostcode0ValidPostcode1ValidPostcodeChanged";
    protected final String sTotalCount_CTBPostcode0ValidPostcode1NotValid = "TotalCount_CTBPostcode0ValidPostcode1NotValid";
    protected final String sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1NotValid = "PercentageOfCTBCount1_CTBPostcode0ValidPostcode1NotValid";
    protected final String sTotalCount_CTBPostcode0NotValidPostcode1Valid = "TotalCount_CTBPostcode0NotValidPostcode1Valid";
    protected final String sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1Valid = "PercentageOfCTBCount1_CTBPostcode0NotValidPostcode1Valid";
    protected final String sTotalCount_CTBPostcode0NotValidPostcode1NotValid = "TotalCount_CTBPostcode0NotValidPostcode1NotValid";
    protected final String sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1NotValid = "PercentageOfCTBCount1_CTBPostcode0NotValidPostcode1NotValid";
    protected final String sTotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeNotChanged = "TotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected final String sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1NotValidPostcodeNotChanged = "PercentageOfCTBCount1_CTBPostcode0NotValidPostcode1NotValidPostcodeNotChanged";
    protected final String sTotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeChanged = "TotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeChanged";
    protected final String sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1NotValidPostcodeChanged = "PercentageOfCTBCount1_CTBPostcode0NotValidPostcode1NotValidPostcodeChanged";
    protected final String sTotalCount_CTBPostcode0ValidPostcode1DNE = "TotalCount_CTBPostcode0ValidPostcode1DNE";
    protected final String sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1DNE = "PercentageOfCTBCount1_CTBPostcode0ValidPostcode1DNE";
    protected final String sTotalCount_CTBPostcode0DNEPostcode1Valid = "TotalCount_CTBPostcode0DNEPostcode1Valid";
    protected final String sPercentageOfCTBCount1_CTBPostcode0DNEPostcode1Valid = "PercentageOfCTBCount1_CTBPostcode0DNEPostcode1Valid";
    protected final String sTotalCount_CTBPostcode0DNEPostcode1DNE = "TotalCount_CTBPostcode0DNEPostcode1DNE";
    protected final String sPercentageOfCTBCount1_CTBPostcode0DNEPostcode1DNE = "PercentageOfCTBCount1_CTBPostcode0DNEPostcode1DNE";
    protected final String sTotalCount_CTBPostcode0DNEPostcode1NotValid = "TotalCount_CTBPostcode0DNEPostcode1NotValid";
    protected final String sPercentageOfCTBCount1_CTBPostcode0DNEPostcode1NotValid = "PercentageOfCTBCount1_CTBPostcode0DNEPostcode1NotValid";
    protected final String sTotalCount_CTBPostcode0NotValidPostcode1DNE = "TotalCount_CTBPostcode0NotValidPostcode1DNE";
    protected final String sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1DNE = "PercentageOfCTBCount1_CTBPostcode0NotValidPostcode1DNE";

    // Counters
    // Single Time
    // All
    protected double AllTotalWeeklyHBEntitlement;
    protected int AllTotalWeeklyHBEntitlementNonZeroCount;
    protected int AllTotalWeeklyHBEntitlementZeroCount;
    protected double AllTotalWeeklyCTBEntitlement;
    protected int TotalCount_AllWeeklyCTBEntitlementNonZero;
    protected int AllTotalWeeklyCTBEntitlementZeroCount;
    protected double AllTotalWeeklyEligibleRentAmount;
    protected int AllTotalWeeklyEligibleRentAmountNonZeroCount;
    protected int AllTotalWeeklyEligibleRentAmountZeroCount;
    protected double AllTotalWeeklyEligibleCouncilTaxAmount;
    protected int TotalCount_AllWeeklyEligibleCouncilTaxAmountNonZero;
    protected int TotalCount_AllWeeklyEligibleCouncilTaxAmountZero;
    protected double AllTotalContractualRentAmount;
    protected int AllTotalContractualRentAmountNonZeroCount;
    protected int AllTotalContractualRentAmountZeroCount;
    protected double AllTotalWeeklyAdditionalDiscretionaryPayment;
    protected int AllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    protected int AllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    protected double AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    protected int AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    protected int AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;
    // HB
    protected double HBTotalWeeklyHBEntitlement;
    protected int TotalCount_HBWeeklyHBEntitlementNonZero;
    protected int TotalCount_HBWeeklyHBEntitlementZero;
    protected double HBTotalWeeklyCTBEntitlement;
    protected int TotalCount_HBWeeklyCTBEntitlementNonZero;
    protected int HBTotalWeeklyCTBEntitlementZeroCount;
    protected double HBTotalWeeklyEligibleRentAmount;
    protected int HBTotalWeeklyEligibleRentAmountNonZeroCount;
    protected int HBTotalWeeklyEligibleRentAmountZeroCount;
    protected double HBTotalWeeklyEligibleCouncilTaxAmount;
    protected int TotalCount_HBWeeklyEligibleCouncilTaxAmountNonZero;
    protected int TotalCount_HBWeeklyEligibleCouncilTaxAmountZero;
    protected double HBTotalContractualRentAmount;
    protected int HBTotalContractualRentAmountNonZeroCount;
    protected int HBTotalContractualRentAmountZeroCount;
    protected double HBTotalWeeklyAdditionalDiscretionaryPayment;
    protected int HBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    protected int HBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    protected double HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    protected int HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    protected int HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;
    // CTB
    protected double CTBTotalWeeklyHBEntitlement;
    protected int TotalCount_CTBWeeklyHBEntitlementNonZero;
    protected int CTBTotalWeeklyHBEntitlementZeroCount;
    protected double CTBTotalWeeklyCTBEntitlement;
    protected int TotalCount_CTBWeeklyCTBEntitlementNonZero;
    protected int CTBTotalWeeklyCTBEntitlementZeroCount;
    protected double CTBTotalWeeklyEligibleRentAmount;
    protected int CTBTotalWeeklyEligibleRentAmountNonZeroCount;
    protected int CTBTotalWeeklyEligibleRentAmountZeroCount;
    protected double CTBTotalWeeklyEligibleCouncilTaxAmount;
    protected int TotalCount_CTBWeeklyEligibleCouncilTaxAmountNonZero;
    protected int TotalCount_CTBWeeklyEligibleCouncilTaxAmountZero;
    protected double CTBTotalContractualRentAmount;
    protected int CTBTotalContractualRentAmountNonZeroCount;
    protected int CTBTotalContractualRentAmountZeroCount;
    protected double CTBTotalWeeklyAdditionalDiscretionaryPayment;
    protected int CTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount;
    protected int CTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount;
    protected double CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    protected int CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount;
    protected int CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount;
    // Employment Education Training
    protected int TotalCount_HBEmployedClaimants;
    protected int TotalCount_HBSelfEmployedClaimants;
    protected int TotalCount_HBStudentsClaimants;
    protected int TotalCount_CTBEmployedClaimants;
    protected int TotalCount_CTBSelfEmployedClaimants;
    protected int TotalCount_CTBStudentsClaimants;
    // HLA
    protected int TotalCount_HBLHACases;
    protected int TotalCount_CTBLHACases;
    // Disability
    protected int[] TotalCount_DisabilityPremiumAwardTT;
    protected int[] TotalCount_SevereDisabilityPremiumAwardTT;
    protected int[] TotalCount_DisabledChildPremiumAwardTT;
    protected int[] TotalCount_EnhancedDisabilityPremiumAwardTT;
    protected int[] TotalCount_DisabilityAwardTT;
    // PSI
    protected int[] TotalCount_AllPSI;
    protected int[] TotalCount_HBPSI;
    protected int[] TotalCount_CTBPSI;
    protected int[][] TotalCount_AllPSITT;
    protected int[][] TotalCount_HBPSITT;
    protected int[][] TotalCount_CTBPSITT;
    // Total Count Claimant
    protected int[] TotalCount_TTClaimant1;
    protected int[] TotalCount_TTClaimant0;
    // Household Size
    protected long AllTotalHouseholdSize;
    protected long HBTotalHouseholdSize;
    protected long CTBTotalHouseholdSize;
    protected long[] AllTotalHouseholdSizeTT;
    // All
    protected int AllCount1;
    protected Integer AllCount0;
    protected int[][] TotalCount_AllEthnicGroupClaimantTT;
    protected int TotalCount_AllPostcodeValidFormat;
    protected int TotalCount_AllPostcodeInvalidFormat;
    protected int TotalCount_AllPostcodeValid;
    protected int TotalCount_AllPostcodeInvalid;

    // HB
    protected int HBCount1;
    protected int HBPTICount1;
    protected int HBPTSCount1;
    protected int HBPTOCount1;
    protected int HBCount0;
    protected int HBPTICount0;
    protected int HBPTSCount0;
    protected int HBPTOCount0;
    protected int[] HBEthnicGroupCount;
    protected int TotalCount_HBPostcodeValidFormat;
    protected int TotalCount_HBPostcodeInvalidFormat;
    protected int TotalCount_HBPostcodeValid;
    protected int TotalCount_HBPostcodeInvalid;
    // CTB
    protected int CTBCount1;
    protected int CTBPTICount1;
    protected int CTBPTSCount1;
    protected int CTBPTOCount1;
    protected int CTBCount0;
    protected int CTBPTICount0;
    protected int CTBPTSCount0;
    protected int CTBPTOCount0;
    protected int[] CTBEthnicGroupCount;
    protected int TotalCount_CTBPostcodeValidFormat;
    protected int TotalCount_CTBPostcodeInvalidFormat;
    protected int TotalCount_CTBPostcodeValid;
    protected int TotalCount_CTBPostcodeInvalid;

    // Compare 2 Times
    // General
    protected int TotalCount_HBTTsToCTBTTs;
    protected int TotalCount_CTBTTsToHBTTs;
    // General HB related
    protected int TotalCount_Minus999TTToSocialTTs;
    protected int TotalCount_Minus999TTToPrivateDeregulatedTTs;
    protected int TotalCount_HBTTsToHBTTs;
    protected int TotalCount_HBTTsToMinus999TT;
    protected int TotalCount_SocialTTsToPrivateDeregulatedTTs;
    protected int TotalCount_SocialTTsToMinus999TT;
    protected int TotalCount_PrivateDeregulatedTTsToSocialTTs;
    protected int TotalCount_PrivateDeregulatedTTsToMinus999TT;
    protected int TotalCount_TT1ToPrivateDeregulatedTTs;
    protected int TotalCount_TT4ToPrivateDeregulatedTTs;
    protected int TotalCount_PrivateDeregulatedTTsToTT1;
    protected int TotalCount_PrivateDeregulatedTTsToTT4;
    protected int TotalCount_PostcodeChangeWithinSocialTTs;
    protected int TotalCount_PostcodeChangeWithinTT1;
    protected int TotalCount_PostcodeChangeWithinTT4;
    protected int TotalCount_PostcodeChangeWithinPrivateDeregulatedTTs;
    // General CTB related
    protected int TotalCount_SocialTTsToCTBTTs;
    protected int TotalCount_Minus999TTToCTBTTs;
    protected int TotalCount_TT1ToCTBTTs;
    protected int TotalCount_TT4ToCTBTTs;
    protected int TotalCount_PrivateDeregulatedTTsToCTBTTs;
    protected int TotalCount_CTBTTsToSocialTTs;
    protected int TotalCount_CTBTTsToMinus999TT;
    protected int TotalCount_CTBTTsToTT1;
    protected int TotalCount_CTBTTsToTT4;
    protected int TotalCount_CTBTTsToPrivateDeregulatedTTs;
    // TT1 TT4
    protected int TotalCount_TT1ToTT4;
    protected int TotalCount_TT4ToTT1;
    // All
    protected int TotalCount_AllTTChangeClaimant;
    //protected int TotalCount_AllTTChangeClaimantIgnoreMinus999;
    protected int TotalCount_AllPostcode0ValidPostcode1Valid;
    protected int TotalCount_AllPostcode0ValidPostcode1ValidPostcodeNotChanged;
    protected int TotalCount_AllPostcode0ValidPostcode1ValidPostcodeChanged;
    protected int TotalCount_AllPostcode0ValidPostcode1NotValid;
    protected int TotalCount_AllPostcode0NotValidPostcode1Valid;
    protected int TotalCount_AllPostcode0NotValidPostcode1NotValid;
    protected int TotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeNotChanged;
    protected int TotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeChanged;
    protected int TotalCount_AllPostcode0ValidPostcode1DNE;
    protected int TotalCount_AllPostcode0DNEPostcode1Valid;
    protected int TotalCount_AllPostcode0DNEPostcode1DNE;
    protected int TotalCount_AllPostcode0DNEPostcode1NotValid;
    protected int TotalCount_AllPostcode0NotValidPostcode1DNE;
    // HB
    protected int TotalCount_HBTTChangeClaimant;
    //protected int TotalCount_HBTTChangeClaimantIgnoreMinus999;
    protected int TotalCount_HBPostcode0ValidPostcode1Valid;
    protected int TotalCount_HBPostcode0ValidPostcode1ValidPostcodeNotChanged;
    protected int TotalCount_HBPostcode0ValidPostcode1ValidPostcodeChanged;
    protected int TotalCount_HBPostcode0ValidPostcode1NotValid;
    protected int TotalCount_HBPostcode0NotValidPostcode1Valid;
    protected int TotalCount_HBPostcode0NotValidPostcode1NotValid;
    protected int TotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeNotChanged;
    protected int TotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeChanged;
    protected int TotalCount_HBPostcode0ValidPostcode1DNE;
    protected int TotalCount_HBPostcode0DNEPostcode1Valid;
    protected int TotalCount_HBPostcode0DNEPostcode1DNE;
    protected int TotalCount_HBPostcode0DNEPostcode1NotValid;
    protected int TotalCount_HBPostcode0NotValidPostcode1DNE;
    // CTB
    protected int TotalCount_CTBTTChangeClaimant;
    //protected int TotalCount_CTBTTChangeClaimantIgnoreMinus999;
    protected int TotalCount_CTBPostcode0ValidPostcode1Valid;
    protected int TotalCount_CTBPostcode0ValidPostcode1ValidPostcodeNotChanged;
    protected int TotalCount_CTBPostcode0ValidPostcode1ValidPostcodeChanged;
    protected int TotalCount_CTBPostcode0ValidPostcode1NotValid;
    protected int TotalCount_CTBPostcode0NotValidPostcode1Valid;
    protected int TotalCount_CTBPostcode0NotValidPostcode1NotValid;
    protected int TotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeNotChanged;
    protected int TotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeChanged;
    protected int TotalCount_CTBPostcode0ValidPostcode1DNE;
    protected int TotalCount_CTBPostcode0DNEPostcode1Valid;
    protected int TotalCount_CTBPostcode0DNEPostcode1DNE;
    protected int TotalCount_CTBPostcode0DNEPostcode1NotValid;
    protected int TotalCount_CTBPostcode0NotValidPostcode1DNE;

    public DW_Summary(DW_Environment env) {
        super(env);
        this.SHBE_Data = env.getSHBE_Data();
        this.SHBE_Handler = env.getSHBE_Handler();
        this.Strings = env.getStrings();
        this.Files = env.getFiles();
        this.Postcode_Handler = env.getPostcode_Handler();
        this.SHBE_TenancyType_Handler = env.getSHBE_TenancyType_Handler();
    }

    public DW_Summary(
            DW_Environment env,
            int nTT,
            int nEG,
            int nPSI,
            boolean handleOutOfMemoryError) {
        this(env);
        init(nTT, nEG, nPSI);
    }

    private void init(int nTT, int nEG, int nPSI) {
        initSingleTimeStrings(nTT, nEG, nPSI);
        TotalCount_DisabilityPremiumAwardTT = new int[nTT];
        TotalCount_SevereDisabilityPremiumAwardTT = new int[nTT];
        TotalCount_DisabledChildPremiumAwardTT = new int[nTT];
        TotalCount_EnhancedDisabilityPremiumAwardTT = new int[nTT];
        TotalCount_DisabilityAwardTT = new int[nTT];
        AllTotalHouseholdSizeTT = new long[nTT];
        TotalCount_AllPSI = new int[nPSI];
        TotalCount_HBPSI = new int[nPSI];
        TotalCount_CTBPSI = new int[nPSI];
        TotalCount_AllPSITT = new int[nPSI][nTT];
        TotalCount_HBPSITT = new int[nPSI][nTT];
        TotalCount_CTBPSITT = new int[nPSI][nTT];
        TotalCount_TTClaimant1 = new int[nTT];
        TotalCount_TTClaimant0 = new int[nTT];
        TotalCount_AllEthnicGroupClaimantTT = new int[nEG][nTT];
        HBEthnicGroupCount = new int[nEG];
        CTBEthnicGroupCount = new int[nEG];
    }

    protected void initSingleTimeStrings(int nTT, int nEG, int nPSI) {
        Strings.sTotal_IncomeTT = new String[nTT];
        Strings.sTotalCount_IncomeNonZeroTT = new String[nTT];
        Strings.sTotalCount_IncomeZeroTT = new String[nTT];
        Strings.sAverage_NonZero_IncomeTT = new String[nTT];
        Strings.sTotal_HBIncomeTT = new String[nTT];
        Strings.sTotalCount_HBIncomeNonZeroTT = new String[nTT];
        Strings.sTotalCount_HBIncomeZeroTT = new String[nTT];
        Strings.sAverage_NonZero_HBIncomeTT = new String[nTT];
        Strings.sTotal_CTBIncomeTT = new String[nTT];
        Strings.sTotalCount_CTBIncomeNonZeroTT = new String[nTT];
        Strings.sTotalCount_CTBIncomeZeroTT = new String[nTT];
        Strings.sAverage_Non_Zero_CTBIncomeTT = new String[nTT];
        Strings.sTotal_WeeklyEligibleRentAmountTT = new String[nTT];
        Strings.sTotalCount_WeeklyEligibleRentAmountNonZeroTT = new String[nTT];
        Strings.sTotalCount_WeeklyEligibleRentAmountZeroTT = new String[nTT];
        Strings.sAverage_NonZero_WeeklyEligibleRentAmountTT = new String[nTT];
        Strings.sTotal_HBWeeklyEligibleRentAmountTT = new String[nTT];
        Strings.sTotalCount_HBWeeklyEligibleRentAmountNonZeroTT = new String[nTT];
        Strings.sTotalCount_HBWeeklyEligibleRentAmountZeroTT = new String[nTT];
        Strings.sAverage_NonZero_HBWeeklyEligibleRentAmountTT = new String[nTT];
        Strings.sTotal_CTBWeeklyEligibleRentAmountTT = new String[nTT];
        Strings.sTotalCount_CTBWeeklyEligibleRentAmountNonZeroTT = new String[nTT];
        Strings.sTotalCount_CTBWeeklyEligibleRentAmountZeroTT = new String[nTT];
        Strings.sAverage_NonZero_CTBWeeklyEligibleRentAmountTT = new String[nTT];
        sTotal_HouseholdSizeTT = new String[nTT];
        sAverage_HouseholdSizeTT = new String[nTT];
        sTotalCount_AllPSI = new String[nPSI];
        sTotalCount_HBPSI = new String[nPSI];
        sTotalCount_CTBPSI = new String[nPSI];
        sPercentageOfAll_AllPSI = new String[nPSI];
        sPercentageOfHB_HBPSI = new String[nPSI];
        sPercentageOfCTB_CTBPSI = new String[nPSI];
        sTotalCount_PSITT = new String[nPSI][nTT];
        sPercentageOfAll_PSITT = new String[nPSI][nTT];
        sPercentageOfTT_PSITT = new String[nPSI][nTT];
        sPercentageOfHB_PSITT = new String[nPSI][nTT];
        sPercentageOfCTB_PSITT = new String[nPSI][nTT];
        for (int i = 1; i < nPSI; i++) {
            sTotalCount_AllPSI[i] = "TotalCount_AllPSI" + i;
            sTotalCount_HBPSI[i] = "TotalCount_HBPSI" + i;
            sTotalCount_CTBPSI[i] = "TotalCount_CTBPSI" + i;
            sPercentageOfAll_AllPSI[i] = "PercentageOfAll_AllPSI" + i;
            sPercentageOfHB_HBPSI[i] = "PercentageOfHB_HBPSI" + i;
            sPercentageOfCTB_CTBPSI[i] = "PercentageOfCTB_CTBPSI" + i;
            for (int j = 1; j < nTT; j++) {
                sTotalCount_PSITT[i][j] = "TotalCount_PSI" + i + "TT" + j;
                sPercentageOfAll_PSITT[i][j] = "PercentageOfAll_PSI" + i + "TT" + j;
                if (j == 5 || j == 7) {
                    sPercentageOfCTB_PSITT[i][j] = "PercentageOfCTB_PSI" + i + "TT" + j;
                } else {
                    sPercentageOfHB_PSITT[i][j] = "PercentageOfHB_PSI" + i + "TT" + j;
                }
                sPercentageOfTT_PSITT[i][j] = "PercentageOfTT_PSI" + i + "TT" + j;
            }
        }
        // All
        sTotalCount_ClaimantTT = new String[nTT];
        sPercentageOfAll_ClaimantTT = new String[nTT];
        sPercentageOfHB_ClaimantTT = new String[nTT];
        sPercentageOfCTB_ClaimantTT = new String[nTT];
        // DisabilityAward
        sTotalCount_DisabilityAwardTT = new String[nTT];
        sPercentageOfAll_DisabilityAwardTT = new String[nTT];
        sPercentageOfHB_DisabilityAwardTT = new String[nTT];
        sPercentageOfCTB_DisabilityAwardTT = new String[nTT];
        sPercentageOfTT_DisabilityAwardTT = new String[nTT];
        // DisabilityPremiumAward
        sTotalCount_DisabilityPremiumAwardTT = new String[nTT];
        sPercentageOfAll_DisabilityPremiumAwardTT = new String[nTT];
        sPercentageOfHB_DisabilityPremiumAwardTT = new String[nTT];
        sPercentageOfCTB_DisabilityPremiumAwardTT = new String[nTT];
        sPercentageOfTT_DisabilityPremiumAwardTT = new String[nTT];
        // SevereDisabilityPremiumAward
        sTotalCount_SevereDisabilityPremiumAwardTT = new String[nTT];
        sPercentageOfAll_SevereDisabilityPremiumAwardTT = new String[nTT];
        sPercentageOfHB_SevereDisabilityPremiumAwardTT = new String[nTT];
        sPercentageOfCTB_SevereDisabilityPremiumAwardTT = new String[nTT];
        sPercentageOfTT_SevereDisabilityPremiumAwardTT = new String[nTT];
        // DisabledChildPremiumAward
        sTotalCount_DisabledChildPremiumAwardTT = new String[nTT];
        sPercentageOfAll_DisabledChildPremiumAwardTT = new String[nTT];
        sPercentageOfHB_DisabledChildPremiumAwardTT = new String[nTT];
        sPercentageOfCTB_DisabledChildPremiumAwardTT = new String[nTT];
        sPercentageOfTT_DisabledChildPremiumAwardTT = new String[nTT];
        // EnhancedDisabilityPremiumAward
        sTotalCount_EnhancedDisabilityPremiumAwardTT = new String[nTT];
        sPercentageOfAll_EnhancedDisabilityPremiumAwardTT = new String[nTT];
        sPercentageOfHB_EnhancedDisabilityPremiumAwardTT = new String[nTT];
        sPercentageOfCTB_EnhancedDisabilityPremiumAwardTT = new String[nTT];
        sPercentageOfTT_EnhancedDisabilityPremiumAwardTT = new String[nTT];
        for (int i = 1; i < nTT; i++) {
            // HouseholdSize
            sTotal_HouseholdSizeTT[i] = "Total_HouseholdSizeTT" + i;
            sAverage_HouseholdSizeTT[i] = "Average_HouseholdSizeTT" + i;
            // Claimants
            sTotalCount_ClaimantTT[i] = "TotalCount_ClaimantTT" + i;
            sPercentageOfAll_ClaimantTT[i] = "PercentageOfAll_ClaimantTT" + i;
            sPercentageOfHB_ClaimantTT[i] = "PercentageOfHB_ClaimantTT" + i;
            sPercentageOfCTB_ClaimantTT[i] = "PercentageOfCTB_ClaimantTT" + i;
            // Income
            Strings.sTotal_IncomeTT[i] = "Total_IncomeTT" + i;
            Strings.sTotalCount_IncomeNonZeroTT[i] = "TotalCount_IncomeNonZeroTT" + i;
            Strings.sTotalCount_IncomeZeroTT[i] = "TotalCount_IncomeZeroTT" + i;
            Strings.sAverage_NonZero_IncomeTT[i] = "Average_NonZero_IncomeTT" + i;
            Strings.sTotal_HBIncomeTT[i] = "Total_HBIncomeTT" + i;
            Strings.sTotalCount_HBIncomeNonZeroTT[i] = "TotalCount_HBIncomeNonZeroTT" + i;
            Strings.sTotalCount_HBIncomeZeroTT[i] = "TotalCount_HBIncomeZeroTT" + i;
            Strings.sAverage_NonZero_HBIncomeTT[i] = "Average_NonZero_HBIncomeTT" + i;
            Strings.sTotal_CTBIncomeTT[i] = "Total_CTBIncomeTT" + i;
            Strings.sTotalCount_CTBIncomeNonZeroTT[i] = "TotalCount_CTBIncomeNonZeroTT" + i;
            Strings.sTotalCount_CTBIncomeZeroTT[i] = "TotalCount_CTBIncomeZeroTT" + i;
            Strings.sAverage_Non_Zero_CTBIncomeTT[i] = "Average_Non_Zero_CTBIncomeTT" + i;
            // WeeklyEligibleRentAmountTT
            Strings.sTotal_WeeklyEligibleRentAmountTT[i] = "Total_WeeklyEligibleRentAmountTT" + i;
            Strings.sTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] = "TotalCount_WeeklyEligibleRentAmountNonZeroTT" + i;
            Strings.sTotalCount_WeeklyEligibleRentAmountZeroTT[i] = "TotalCount_WeeklyEligibleRentAmountZeroTT" + i;
            Strings.sAverage_NonZero_WeeklyEligibleRentAmountTT[i] = "Average_NonZero_WeeklyEligibleRentAmountTT" + i;
            Strings.sTotal_HBWeeklyEligibleRentAmountTT[i] = "Total_HBWeeklyEligibleRentAmountTT" + i;
            Strings.sTotalCount_HBWeeklyEligibleRentAmountNonZeroTT[i] = "TotalCount_HBWeeklyEligibleRentAmountNonZeroTT" + i;
            Strings.sTotalCount_HBWeeklyEligibleRentAmountZeroTT[i] = "TotalCount_HBWeeklyEligibleRentAmountZeroTT" + i;
            Strings.sAverage_NonZero_HBWeeklyEligibleRentAmountTT[i] = "Average_NonZero_HBWeeklyEligibleRentAmountTT" + i;
            Strings.sTotal_CTBWeeklyEligibleRentAmountTT[i] = "Total_CTBWeeklyEligibleRentAmountTT" + i;
            Strings.sTotalCount_CTBWeeklyEligibleRentAmountNonZeroTT[i] = "TotalCount_CTBWeeklyEligibleRentAmountNonZeroTT" + i;
            Strings.sTotalCount_CTBWeeklyEligibleRentAmountZeroTT[i] = "TotalCount_CTBWeeklyEligibleRentAmountZeroTT" + i;
            Strings.sAverage_NonZero_CTBWeeklyEligibleRentAmountTT[i] = "Average_NonZero_CTBWeeklyEligibleRentAmountTT" + i;
            // DisabilityAwardTT
            sTotalCount_DisabilityAwardTT[i] = "TotalCount_DisabilityAwardTT" + i;
            sPercentageOfAll_DisabilityAwardTT[i] = "PercentageOfAll_DisabilityAwardTT" + i;
            sPercentageOfHB_DisabilityAwardTT[i] = "PercentageOfHB_DisabilityAwardTT" + i;
            sPercentageOfCTB_DisabilityAwardTT[i] = "PercentageOfCTB_DisabilityAwardTT" + i;
            sPercentageOfTT_DisabilityAwardTT[i] = "PercentageOfTT_DisabilityAwardTT" + i;
            // DisabilityPremiumAwardTT
            sTotalCount_DisabilityPremiumAwardTT[i] = "TotalCount_DisabilityPremiumAwardTT" + i;
            sPercentageOfAll_DisabilityPremiumAwardTT[i] = "PercentageOfAll_DisabilityPremiumAwardTT" + i;
            sPercentageOfHB_DisabilityPremiumAwardTT[i] = "PercentageOfHB_DisabilityPremiumAwardTT" + i;
            sPercentageOfCTB_DisabilityPremiumAwardTT[i] = "PercentageOfCTB_DisabilityPremiumAwardTT" + i;
            sPercentageOfTT_DisabilityPremiumAwardTT[i] = "PercentageOfTT_DisabilityPremiumAwardTT" + i;
            // SevereDisabilityPremiumAwardTT
            sTotalCount_SevereDisabilityPremiumAwardTT[i] = "TotalCount_SevereDisabilityPremiumAwardTT" + i;
            sPercentageOfAll_SevereDisabilityPremiumAwardTT[i] = "PercentageOfAll_SevereDisabilityPremiumAwardTT" + i;
            sPercentageOfHB_SevereDisabilityPremiumAwardTT[i] = "PercentageOfHB_SevereDisabilityPremiumAwardTT" + i;
            sPercentageOfCTB_SevereDisabilityPremiumAwardTT[i] = "PercentageOfCTB_SevereDisabilityPremiumAwardTT" + i;
            sPercentageOfTT_SevereDisabilityPremiumAwardTT[i] = "PercentageOfTT_SevereDisabilityPremiumAwardTT" + i;
            // DisabledChildPremiumAwardTT
            sTotalCount_DisabledChildPremiumAwardTT[i] = "TotalCount_DisabledChildPremiumAwardTT" + i;
            sPercentageOfAll_DisabledChildPremiumAwardTT[i] = "PercentageOfAll_DisabledChildPremiumAwardTT" + i;
            sPercentageOfHB_DisabledChildPremiumAwardTT[i] = "PercentageOfHB_DisabledChildPremiumAwardTT" + i;
            sPercentageOfCTB_DisabledChildPremiumAwardTT[i] = "PercentageOfCTB_DisabledChildPremiumAwardTT" + i;
            sPercentageOfTT_DisabledChildPremiumAwardTT[i] = "PercentageOfTT_DisabledChildPremiumAwardTT" + i;
            // EnhancedDisabilityPremiumAwardTT
            sTotalCount_EnhancedDisabilityPremiumAwardTT[i] = "TotalCount_EnhancedDisabilityPremiumAwardTT" + i;
            sPercentageOfAll_EnhancedDisabilityPremiumAwardTT[i] = "PercentageOfAll_EnhancedDisabilityPremiumAwardTT" + i;
            sPercentageOfHB_EnhancedDisabilityPremiumAwardTT[i] = "PercentageOfHB_EnhancedDisabilityPremiumAwardTT" + i;
            sPercentageOfCTB_EnhancedDisabilityPremiumAwardTT[i] = "PercentageOfCTB_EnhancedDisabilityPremiumAwardTT" + i;
            sPercentageOfTT_EnhancedDisabilityPremiumAwardTT[i] = "PercentageOfTT_EnhancedDisabilityPremiumAwardTT" + i;
        }
        // EthnicGroup
        sTotalCount_AllEthnicGroupClaimant = new String[nEG];
        sTotalCount_AllEthnicGroupSocialTTClaimant = new String[nEG];
        sTotalCount_AllEthnicGroupPrivateDeregulatedTTClaimant = new String[nEG];
        sPercentageOfAll_EthnicGroupClaimant = new String[nEG];
        sPercentageOfSocialTT_EthnicGroupSocialTTClaimant = new String[nEG];
        sPercentageOfPrivateDeregulatedTT_EthnicGroupPrivateDeregulatedTTClaimant = new String[nEG];
        sPercentageOfAll_EthnicGroupClaimant = new String[nEG];
        sTotalCount_AllEthnicGroupClaimantTT = new String[nEG][nTT];
        sPercentageOfEthnicGroup_EthnicGroupClaimantTT = new String[nEG][nTT];
        sPercentageOfTT_EthnicGroupClaimantTT = new String[nEG][nTT];
        sTotalCount_HBEthnicGroupClaimant = new String[nEG];
        sPercentageOfHB_HBEthnicGroupClaimant = new String[nEG];
        sTotalCount_CTBEthnicGroupClaimant = new String[nEG];
        sPercentageOfCTB_CTBEthnicGroupClaimant = new String[nEG];
        for (int i = 1; i < nEG; i++) {
            String EGN = SHBE_Handler.getEthnicityGroupName(i);
            for (int j = 1; j < nTT; j++) {
                sTotalCount_AllEthnicGroupClaimantTT[i][j] = "TotalCount_All_ClaimantEthnicGroup_" + EGN + "__ClaimantTT" + j;
                sPercentageOfEthnicGroup_EthnicGroupClaimantTT[i][j] = "PercentageOfEthnicGroup_" + EGN + "___ClaimantEthnicGroup_" + EGN + "__ClaimantTT" + j;
                sPercentageOfTT_EthnicGroupClaimantTT[i][j] = "PercentageOfTT" + j + "__EthnicGroup _" + EGN + "__ClaimantTT" + j;
            }
            sTotalCount_AllEthnicGroupClaimant[i] = "TotalCount_AllEthnicGroup_" + EGN + "_Claimant";
            sTotalCount_AllEthnicGroupSocialTTClaimant[i] = "TotalCount_AllEthnicGroup_" + EGN + "_SocialTTClaimant";
            sTotalCount_AllEthnicGroupPrivateDeregulatedTTClaimant[i] = "TotalCount_AllEthnicGroup_" + EGN + "_PrivateDeregulatedTTClaimant";
            sPercentageOfAll_EthnicGroupClaimant[i] = "PercentageOfAll_EthnicGroup_" + EGN + "_Claimant";
            sPercentageOfSocialTT_EthnicGroupSocialTTClaimant[i] = "PercentageOfSocialTT_EthnicGroup_" + EGN + "_SocialTTClaimant";
            sPercentageOfPrivateDeregulatedTT_EthnicGroupPrivateDeregulatedTTClaimant[i] = "PercentageOfPrivateDeregulatedTT_EthnicGroup_" + EGN + "_PrivateDeregulatedTTClaimant";
            sTotalCount_HBEthnicGroupClaimant[i] = "TotalCount_HBEthnicGroup_" + EGN + "_Claimant";
            sPercentageOfHB_HBEthnicGroupClaimant[i] = "PercentageOfHB_HBEthnicGroup_" + EGN + "_Claimant";
            sTotalCount_CTBEthnicGroupClaimant[i] = "TotalCount_CTBClaimantEthnicGroup_" + EGN + "_Claimant";
            sPercentageOfCTB_CTBEthnicGroupClaimant[i] = "PercentageOfCTB_CTBClaimantEthnicGroup_" + EGN + "_Claimant";
        }
    }

    protected void initCounts(int nTT, int nEG, int nPSI) {
        initSingleTimeCounts(nTT, nEG, nPSI);
        initCompare2TimesCounts();
    }

    protected void initSingleTimeCounts(int nTT, int nEG, int nPSI) {
        HBPTICount1 = 0;
        HBPTSCount1 = 0;
        HBPTOCount1 = 0;
        CTBPTICount1 = 0;
        CTBPTSCount1 = 0;
        CTBPTOCount1 = 0;
        // PSI
        for (int i = 1; i < nPSI; i++) {
            TotalCount_AllPSI[i] = 0;
            TotalCount_HBPSI[i] = 0;
            TotalCount_CTBPSI[i] = 0;
            for (int j = 1; j < nTT; j++) {
                TotalCount_AllPSITT[i][j] = 0;
                TotalCount_HBPSITT[i][j] = 0;
                TotalCount_CTBPSITT[i][j] = 0;
            }
        }
        // All
        AllTotalWeeklyHBEntitlement = 0.0d;
        AllTotalWeeklyHBEntitlementNonZeroCount = 0;
        AllTotalWeeklyHBEntitlementZeroCount = 0;
        AllTotalWeeklyCTBEntitlement = 0.0d;
        TotalCount_AllWeeklyCTBEntitlementNonZero = 0;
        AllTotalWeeklyCTBEntitlementZeroCount = 0;
        AllTotalWeeklyEligibleRentAmount = 0.0d;
        AllTotalWeeklyEligibleRentAmountNonZeroCount = 0;
        AllTotalWeeklyEligibleRentAmountZeroCount = 0;
        AllTotalWeeklyEligibleCouncilTaxAmount = 0.0d;
        TotalCount_AllWeeklyEligibleCouncilTaxAmountNonZero = 0;
        TotalCount_AllWeeklyEligibleCouncilTaxAmountZero = 0;
        AllTotalContractualRentAmount = 0.0d;
        AllTotalContractualRentAmountNonZeroCount = 0;
        AllTotalContractualRentAmountZeroCount = 0;
        AllTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        AllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        AllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;
        // HB
        TotalCount_HB_PTIToPTI = 0;
        TotalCount_HB_PTIToPTS = 0;
        TotalCount_HB_PTIToPTO = 0;
        TotalCount_HB_PTIToNull = 0;
        TotalCount_HB_PTSToPTI = 0;
        TotalCount_HB_PTSToPTS = 0;
        TotalCount_HB_PTSToPTO = 0;
        TotalCount_HB_PTSToNull = 0;
        TotalCount_HB_PTOToPTI = 0;
        TotalCount_HB_PTOToPTS = 0;
        TotalCount_HB_PTOToPTO = 0;
        TotalCount_HB_PTOToNull = 0;
        TotalCount_HB_NotSuspendedToNotSuspended = 0;
        TotalCount_HB_NotSuspendedToSuspended = 0;
        TotalCount_HB_NotSuspendedToNull = 0;
        TotalCount_HB_SuspendedToNotSuspended = 0;
        TotalCount_HB_SuspendedToSuspended = 0;
        TotalCount_HB_SuspendedToNull = 0;
        HBTotalWeeklyHBEntitlement = 0.0d;
        TotalCount_HBWeeklyHBEntitlementNonZero = 0;
        TotalCount_HBWeeklyHBEntitlementZero = 0;
        HBTotalWeeklyCTBEntitlement = 0.0d;
        TotalCount_HBWeeklyCTBEntitlementNonZero = 0;
        HBTotalWeeklyCTBEntitlementZeroCount = 0;
        HBTotalWeeklyEligibleRentAmount = 0.0d;
        HBTotalWeeklyEligibleRentAmountNonZeroCount = 0;
        HBTotalWeeklyEligibleRentAmountZeroCount = 0;
        HBTotalWeeklyEligibleCouncilTaxAmount = 0.0d;
        TotalCount_HBWeeklyEligibleCouncilTaxAmountNonZero = 0;
        TotalCount_HBWeeklyEligibleCouncilTaxAmountZero = 0;
        HBTotalContractualRentAmount = 0.0d;
        HBTotalContractualRentAmountNonZeroCount = 0;
        HBTotalContractualRentAmountZeroCount = 0;
        HBTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        HBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        HBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;
        // CTB
        TotalCount_CTB_PTIToPTI = 0;
        TotalCount_CTB_PTIToPTS = 0;
        TotalCount_CTB_PTIToPTO = 0;
        TotalCount_CTB_PTIToNull = 0;
        TotalCount_CTB_PTSToPTI = 0;
        TotalCount_CTB_PTSToPTS = 0;
        TotalCount_CTB_PTSToPTO = 0;
        TotalCount_CTB_PTSToNull = 0;
        TotalCount_CTB_PTOToPTI = 0;
        TotalCount_CTB_PTOToPTS = 0;
        TotalCount_CTB_PTOToPTO = 0;
        TotalCount_CTB_PTOToNull = 0;
        TotalCount_CTB_NotSuspendedToNotSuspended = 0;
        TotalCount_CTB_NotSuspendedToSuspended = 0;
        TotalCount_CTB_NotSuspendedToNull = 0;
        TotalCount_CTB_SuspendedToNotSuspended = 0;
        TotalCount_CTB_SuspendedToSuspended = 0;
        TotalCount_CTB_SuspendedToNull = 0;
        CTBTotalWeeklyHBEntitlement = 0.0d;
        TotalCount_CTBWeeklyHBEntitlementNonZero = 0;
        CTBTotalWeeklyHBEntitlementZeroCount = 0;
        CTBTotalWeeklyCTBEntitlement = 0.0d;
        TotalCount_CTBWeeklyCTBEntitlementNonZero = 0;
        CTBTotalWeeklyCTBEntitlementZeroCount = 0;
        CTBTotalWeeklyEligibleRentAmount = 0.0d;
        CTBTotalWeeklyEligibleRentAmountNonZeroCount = 0;
        CTBTotalWeeklyEligibleRentAmountZeroCount = 0;
        CTBTotalWeeklyEligibleCouncilTaxAmount = 0.0d;
        TotalCount_CTBWeeklyEligibleCouncilTaxAmountNonZero = 0;
        TotalCount_CTBWeeklyEligibleCouncilTaxAmountZero = 0;
        CTBTotalContractualRentAmount = 0.0d;
        CTBTotalContractualRentAmountNonZeroCount = 0;
        CTBTotalContractualRentAmountZeroCount = 0;
        CTBTotalWeeklyAdditionalDiscretionaryPayment = 0.0d;
        CTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount = 0;
        CTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount = 0;
        CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0.0d;
        CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount = 0;
        // Employment
        TotalCount_HBEmployedClaimants = 0;
        TotalCount_CTBEmployedClaimants = 0;
        TotalCount_HBSelfEmployedClaimants = 0;
        TotalCount_CTBSelfEmployedClaimants = 0;
        TotalCount_HBStudentsClaimants = 0;
        TotalCount_CTBStudentsClaimants = 0;
        // LHA
        TotalCount_HBLHACases = 0;
        TotalCount_CTBLHACases = 0;
        // Disability
        for (int i = 1; i < nTT; i++) {
            TotalCount_TTClaimant1[i] = 0;
            TotalCount_DisabilityPremiumAwardTT[i] = 0;
            TotalCount_SevereDisabilityPremiumAwardTT[i] = 0;
            TotalCount_DisabledChildPremiumAwardTT[i] = 0;
            TotalCount_EnhancedDisabilityPremiumAwardTT[i] = 0;
            TotalCount_DisabilityAwardTT[i] = 0;
            AllTotalHouseholdSizeTT[i] = 0;
        }
        //AllCount0 = AllCount1;
        AllCount1 = 0;
        //HBCount0 = HBCount1;
        HBCount1 = 0;
        //CTBCount0 = CTBCount1;
        CTBCount1 = 0;
        // Ethnicity
        for (int i = 1; i < nEG; i++) {
            for (int j = 1; j < nTT; j++) {
                TotalCount_AllEthnicGroupClaimantTT[i][j] = 0;
            }
            HBEthnicGroupCount[i] = 0;
            CTBEthnicGroupCount[i] = 0;
        }
        TotalCount_HBTTChangeClaimant = 0;
        //TotalCount_HBTTChangeClaimantIgnoreMinus999 = 0;
        TotalCount_CTBTTChangeClaimant = 0;
        //TotalCount_CTBTTChangeClaimantIgnoreMinus999 = 0;
        // Household Size
        AllTotalHouseholdSize = 0L;
        HBTotalHouseholdSize = 0L;
        CTBTotalHouseholdSize = 0L;
        TotalCount_AllPostcodeValidFormat = 0;
        TotalCount_AllPostcodeInvalidFormat = 0;
        TotalCount_AllPostcodeValid = 0;
        TotalCount_AllPostcodeInvalid = 0;
        TotalCount_HBPostcodeValidFormat = 0;
        TotalCount_HBPostcodeInvalidFormat = 0;
        TotalCount_HBPostcodeValid = 0;
        TotalCount_HBPostcodeInvalid = 0;
        TotalCount_CTBPostcodeValidFormat = 0;
        TotalCount_CTBPostcodeInvalidFormat = 0;
        TotalCount_CTBPostcodeValid = 0;
        TotalCount_CTBPostcodeInvalid = 0;
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
        TotalCount_AllTTChangeClaimant = 0;
        TotalCount_AllPostcode0ValidPostcode1Valid = 0;
        TotalCount_AllPostcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        TotalCount_AllPostcode0ValidPostcode1ValidPostcodeChanged = 0;
        TotalCount_AllPostcode0ValidPostcode1NotValid = 0;
        TotalCount_AllPostcode0NotValidPostcode1Valid = 0;
        TotalCount_AllPostcode0NotValidPostcode1NotValid = 0;
        TotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        TotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeChanged = 0;
        TotalCount_AllPostcode0ValidPostcode1DNE = 0;
        TotalCount_AllPostcode0DNEPostcode1Valid = 0;
        TotalCount_AllPostcode0DNEPostcode1DNE = 0;
        TotalCount_AllPostcode0DNEPostcode1NotValid = 0;
        TotalCount_AllPostcode0NotValidPostcode1DNE = 0;
        // HB
        TotalCount_HBTTChangeClaimant = 0;
        //TotalCount_HBTTChangeClaimantIgnoreMinus999 = 0;
        TotalCount_HBPostcode0ValidPostcode1Valid = 0;
        TotalCount_HBPostcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        TotalCount_HBPostcode0ValidPostcode1ValidPostcodeChanged = 0;
        TotalCount_HBPostcode0ValidPostcode1NotValid = 0;
        TotalCount_HBPostcode0NotValidPostcode1Valid = 0;
        TotalCount_HBPostcode0NotValidPostcode1NotValid = 0;
        TotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        TotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeChanged = 0;
        TotalCount_HBPostcode0ValidPostcode1DNE = 0;
        TotalCount_HBPostcode0DNEPostcode1Valid = 0;
        TotalCount_HBPostcode0DNEPostcode1DNE = 0;
        TotalCount_HBPostcode0DNEPostcode1NotValid = 0;
        TotalCount_HBPostcode0NotValidPostcode1DNE = 0;
        // CTB
        TotalCount_CTBTTChangeClaimant = 0;
        TotalCount_CTBPostcode0ValidPostcode1Valid = 0;
        TotalCount_CTBPostcode0ValidPostcode1ValidPostcodeNotChanged = 0;
        TotalCount_CTBPostcode0ValidPostcode1ValidPostcodeChanged = 0;
        TotalCount_CTBPostcode0ValidPostcode1NotValid = 0;
        TotalCount_CTBPostcode0NotValidPostcode1Valid = 0;
        TotalCount_CTBPostcode0NotValidPostcode1NotValid = 0;
        TotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeNotChanged = 0;
        TotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeChanged = 0;
        TotalCount_CTBPostcode0ValidPostcode1DNE = 0;
        TotalCount_CTBPostcode0DNEPostcode1Valid = 0;
        TotalCount_CTBPostcode0DNEPostcode1DNE = 0;
        TotalCount_CTBPostcode0DNEPostcode1NotValid = 0;
        TotalCount_CTBPostcode0NotValidPostcode1DNE = 0;

    }

    public Object[] getMemberships(
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
        // All
        if (AllCount0 == null) {
            summary.put(sAllCount0, "null");
            summary.put(sHBCount0, "null");
            summary.put(sCTBCount0, "null");
        } else {
            summary.put(sAllCount0, Integer.toString(AllCount0));
            summary.put(sHBCount0, Integer.toString(HBCount0));
            summary.put(sCTBCount0, Integer.toString(CTBCount0));
        }
        // HB
        summary.put(sTotalCount_HB_NotSuspendedToNotSuspended,
                Integer.toString(TotalCount_HB_NotSuspendedToNotSuspended));
        summary.put(sTotalCount_HB_NotSuspendedToSuspended,
                Integer.toString(TotalCount_HB_NotSuspendedToSuspended));
        summary.put(sTotalCount_HB_NotSuspendedToNull,
                Integer.toString(TotalCount_HB_NotSuspendedToNull));
        summary.put(sTotalCount_HB_SuspendedToNotSuspended,
                Integer.toString(TotalCount_HB_SuspendedToNotSuspended));
        summary.put(sTotalCount_HB_SuspendedToSuspended,
                Integer.toString(TotalCount_HB_SuspendedToSuspended));
        summary.put(sTotalCount_HB_SuspendedToNull,
                Integer.toString(TotalCount_HB_SuspendedToNull));
        summary.put(sTotalCount_HB_PTIToPTI,
                Integer.toString(TotalCount_HB_PTIToPTI));
        summary.put(sTotalCount_HB_PTIToPTS,
                Integer.toString(TotalCount_HB_PTIToPTS));
        summary.put(sTotalCount_HB_PTIToPTO,
                Integer.toString(TotalCount_HB_PTIToPTO));
        summary.put(sTotalCount_HB_PTIToNull,
                Integer.toString(TotalCount_HB_PTIToNull));
        summary.put(sTotalCount_HB_PTSToPTI,
                Integer.toString(TotalCount_HB_PTSToPTI));
        summary.put(sTotalCount_HB_PTSToPTS,
                Integer.toString(TotalCount_HB_PTSToPTS));
        summary.put(sTotalCount_HB_PTSToPTO,
                Integer.toString(TotalCount_HB_PTSToPTO));
        summary.put(sTotalCount_HB_PTSToNull,
                Integer.toString(TotalCount_HB_PTSToNull));
        summary.put(sTotalCount_HB_PTOToPTI,
                Integer.toString(TotalCount_HB_PTOToPTI));
        summary.put(sTotalCount_HB_PTOToPTS,
                Integer.toString(TotalCount_HB_PTOToPTS));
        summary.put(sTotalCount_HB_PTOToPTO,
                Integer.toString(TotalCount_HB_PTOToPTO));
        summary.put(sTotalCount_HB_PTOToNull,
                Integer.toString(TotalCount_HB_PTOToNull));
        // CTB
        summary.put(sTotalCount_CTB_NotSuspendedToNotSuspended,
                Integer.toString(TotalCount_CTB_NotSuspendedToNotSuspended));
        summary.put(sTotalCount_CTB_NotSuspendedToSuspended,
                Integer.toString(TotalCount_CTB_NotSuspendedToSuspended));
        summary.put(sTotalCount_CTB_NotSuspendedToNull,
                Integer.toString(TotalCount_CTB_NotSuspendedToNull));
        summary.put(sTotalCount_CTB_SuspendedToNotSuspended,
                Integer.toString(TotalCount_CTB_SuspendedToNotSuspended));
        summary.put(sTotalCount_CTB_SuspendedToSuspended,
                Integer.toString(TotalCount_CTB_SuspendedToSuspended));
        summary.put(sTotalCount_CTB_SuspendedToNull,
                Integer.toString(TotalCount_CTB_SuspendedToNull));
        summary.put(sTotalCount_CTB_PTIToPTI,
                Integer.toString(TotalCount_CTB_PTIToPTI));
        summary.put(sTotalCount_CTB_PTIToPTS,
                Integer.toString(TotalCount_CTB_PTIToPTS));
        summary.put(sTotalCount_CTB_PTIToPTO,
                Integer.toString(TotalCount_CTB_PTIToPTO));
        summary.put(sTotalCount_CTB_PTIToNull,
                Integer.toString(TotalCount_CTB_PTIToNull));
        summary.put(sTotalCount_CTB_PTSToPTI,
                Integer.toString(TotalCount_CTB_PTSToPTI));
        summary.put(sTotalCount_CTB_PTSToPTS,
                Integer.toString(TotalCount_CTB_PTSToPTS));
        summary.put(sTotalCount_CTB_PTSToPTO,
                Integer.toString(TotalCount_CTB_PTSToPTO));
        summary.put(sTotalCount_CTB_PTSToNull,
                Integer.toString(TotalCount_CTB_PTSToNull));
        summary.put(sTotalCount_CTB_PTOToPTI,
                Integer.toString(TotalCount_CTB_PTOToPTI));
        summary.put(sTotalCount_CTB_PTOToPTS,
                Integer.toString(TotalCount_CTB_PTOToPTS));
        summary.put(sTotalCount_CTB_PTOToPTO,
                Integer.toString(TotalCount_CTB_PTOToPTO));
        summary.put(sTotalCount_CTB_PTOToNull,
                Integer.toString(TotalCount_CTB_PTOToNull));
        double percentage;
        double d;
        // All
        summary.put(
                sTotalCount_AllTTChangeClaimant,
                Integer.toString(TotalCount_AllTTChangeClaimant));
//        summary.put(
//                sTotalCount_AllTTChangeClaimantIgnoreMinus999,
//                Integer.toString(TotalCount_AllTTChangeClaimantIgnoreMinus999));
        d = AllCount0;
        if (d > 0) {
            percentage = (TotalCount_AllTTChangeClaimant * 100.0d) / d;
            summary.put(
                    sAllPercentageOfAll_TTChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
//            percentage = (TotalCount_AllTTChangeClaimantIgnoreMinus999 * 100.0d) / d;
//            summary.put(
//                    sAllPercentageOfAll_TTChangeClaimantIgnoreMinus999,
//                    Generic_BigDecimal.roundIfNecessary(
//                            BigDecimal.valueOf(percentage),
//                            decimalPlacePrecisionForPercentage,
//                            RoundingMode.HALF_UP).toPlainString());
        }
        // Postcode
        summary.put(
                sTotalCount_AllPostcode0ValidPostcode1Valid,
                Integer.toString(TotalCount_HBPostcode0ValidPostcode1Valid + TotalCount_CTBPostcode0ValidPostcode1Valid));
        summary.put(
                sTotalCount_AllPostcode0ValidPostcode1ValidPostcodeNotChanged,
                Integer.toString(TotalCount_AllPostcode0ValidPostcode1ValidPostcodeNotChanged));
        summary.put(
                sTotalCount_AllPostcode0ValidPostcode1ValidPostcodeChange,
                Integer.toString(TotalCount_AllPostcode0ValidPostcode1ValidPostcodeChanged));
        summary.put(
                sTotalCount_AllPostcode0ValidPostcode1NotValid,
                Integer.toString(TotalCount_AllPostcode0ValidPostcode1NotValid));
        summary.put(
                sTotalCount_AllPostcode0NotValidPostcode1Valid,
                Integer.toString(TotalCount_AllPostcode0NotValidPostcode1Valid));
        summary.put(
                sTotalCount_AllPostcode0NotValidPostcode1NotValid,
                Integer.toString(TotalCount_AllPostcode0NotValidPostcode1NotValid));
        summary.put(
                sTotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                Integer.toString(TotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeNotChanged));
        summary.put(
                sTotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeChanged,
                Integer.toString(TotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeChanged));
        summary.put(
                sTotalCount_AllPostcode0ValidPostcode1DNE,
                Integer.toString(TotalCount_AllPostcode0ValidPostcode1DNE));
        summary.put(
                sTotalCount_AllPostcode0DNEPostcode1Valid,
                Integer.toString(TotalCount_AllPostcode0DNEPostcode1Valid));
        summary.put(
                sTotalCount_AllPostcode0DNEPostcode1DNE,
                Integer.toString(TotalCount_AllPostcode0DNEPostcode1DNE));
        summary.put(
                sTotalCount_AllPostcode0DNEPostcode1NotValid,
                Integer.toString(TotalCount_AllPostcode0DNEPostcode1NotValid));
        summary.put(
                sTotalCount_AllPostcode0NotValidPostcode1DNE,
                Integer.toString(TotalCount_AllPostcode0NotValidPostcode1DNE));
        d = AllCount1;
        if (d > 0) {
            percentage = (TotalCount_AllPostcode0ValidPostcode1Valid * 100.0d) / d;
            summary.put(sPercentageOfAllCount1_AllPostcode0ValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_AllPostcode0ValidPostcode1ValidPostcodeNotChanged * 100.0d) / d;
            summary.put(sPercentageOfAllCount1_AllPostcode0ValidPostcode1ValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_AllPostcode0ValidPostcode1ValidPostcodeChanged * 100.0d) / d;
            summary.put(sPercentageOfAllCount1_AllPostcode0ValidPostcode1ValidPostcodeChange,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_AllPostcode0ValidPostcode1NotValid * 100.0d) / d;
            summary.put(sPercentageOfAllCount1_AllPostcode0ValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_AllPostcode0NotValidPostcode1Valid * 100.0d) / d;
            summary.put(sPercentageOfAllCount1_AllPostcode0NotValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_AllPostcode0NotValidPostcode1NotValid * 100.0d) / d;
            summary.put(sPercentageOfAllCount1_AllPostcode0NotValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeNotChanged * 100.0d) / d;
            summary.put(sPercentageOfAllCount1_AllPostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeChanged * 100.0d) / d;
            summary.put(sPercentageOfAllCount1_AllPostcode0NotValidPostcode1NotValidPostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_AllPostcode0ValidPostcode1DNE * 100.0d) / d;
            summary.put(sPercentageOfAllCount1_AllPostcode0ValidPostcode1DNE,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_AllPostcode0DNEPostcode1Valid * 100.0d) / d;
            summary.put(sPercentageOfAllCount1_AllPostcode0DNEPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_AllPostcode0DNEPostcode1DNE * 100.0d) / d;
            summary.put(sPercentageOfAllCount1_AllPostcode0DNEPostcode1DNE,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_AllPostcode0DNEPostcode1NotValid * 100.0d) / d;
            summary.put(sPercentageOfAllCount1_AllPostcode0DNEPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_AllPostcode0NotValidPostcode1DNE * 100.0d) / d;
            summary.put(sPercentageOfAllCount1_AllPostcode0NotValidPostcode1DNE,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // HB
        d = HBCount0;
        // Tenancy Type
        summary.put(
                sTotalCount_HBTTChangeClaimant,
                Integer.toString(TotalCount_HBTTChangeClaimant));
//        summary.put(
//                sTotalCount_HBTTChangeClaimantIgnoreMinus999,
//                Integer.toString(TotalCount_HBTTChangeClaimantIgnoreMinus999));

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
            percentage = (TotalCount_HBTTChangeClaimant * 100.0d) / d;
            summary.put(sPercentageOfHBCount0_HBTTChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
//            percentage = (TotalCount_HBTTChangeClaimantIgnoreMinus999 * 100.0d) / d;
//            summary.put(
//                    sPercentageOfHB_HBTTChangeClaimantIgnoreMinus999,
//                    Generic_BigDecimal.roundIfNecessary(
//                            BigDecimal.valueOf(percentage),
//                            decimalPlacePrecisionForPercentage,
//                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_HBTTsToCTBTTs * 100.0d) / d;
            summary.put(sPercentageOfHBCount0_HBTTsToCTBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_HBTTsToHBTTs * 100.0d) / d;
            summary.put(sPercentageOfHBCount0_HBTTsToHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_HBTTsToMinus999TT * 100.0d) / d;
            summary.put(sPercentageOfHBCount0_HBTTsToMinus999TT,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        // Postcode
        summary.put(
                sTotalCount_HBPostcode0ValidPostcode1Valid,
                Integer.toString(TotalCount_HBPostcode0ValidPostcode1Valid));
        summary.put(
                sTotalCount_HBPostcode0ValidPostcode1ValidPostcodeNotChanged,
                Integer.toString(TotalCount_HBPostcode0ValidPostcode1ValidPostcodeNotChanged));
        summary.put(
                sTotalCount_HBPostcode0ValidPostcode1ValidPostcodeChange,
                Integer.toString(TotalCount_HBPostcode0ValidPostcode1ValidPostcodeChanged));
        summary.put(
                sTotalCount_HBPostcode0ValidPostcode1NotValid,
                Integer.toString(TotalCount_HBPostcode0ValidPostcode1NotValid));
        summary.put(
                sTotalCount_HBPostcode0NotValidPostcode1Valid,
                Integer.toString(TotalCount_HBPostcode0NotValidPostcode1Valid));
        summary.put(
                sTotalCount_HBPostcode0NotValidPostcode1NotValid,
                Integer.toString(TotalCount_HBPostcode0NotValidPostcode1NotValid));
        summary.put(
                sTotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                Integer.toString(TotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeNotChanged));
        summary.put(
                sTotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeChanged,
                Integer.toString(TotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeChanged));
        summary.put(
                sTotalCount_HBPostcode0ValidPostcode1DNE,
                Integer.toString(TotalCount_HBPostcode0ValidPostcode1DNE));
        summary.put(
                sTotalCount_HBPostcode0DNEPostcode1Valid,
                Integer.toString(TotalCount_HBPostcode0DNEPostcode1Valid));
        summary.put(
                sTotalCount_HBPostcode0DNEPostcode1DNE,
                Integer.toString(TotalCount_HBPostcode0DNEPostcode1DNE));
        summary.put(
                sTotalCount_HBPostcode0DNEPostcode1NotValid,
                Integer.toString(TotalCount_HBPostcode0DNEPostcode1NotValid));
        summary.put(
                sTotalCount_HBPostcode0NotValidPostcode1DNE,
                Integer.toString(TotalCount_HBPostcode0NotValidPostcode1DNE));
        d = HBCount1;
        if (d > 0) {
            percentage = (TotalCount_HBPostcode0ValidPostcode1Valid * 100.0d) / d;
            summary.put(sPercentageOfHBCount1_HBPostcode0ValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_HBPostcode0ValidPostcode1ValidPostcodeNotChanged * 100.0d) / d;
            summary.put(sPercentageOfHBCount1_HBPostcode0ValidPostcode1ValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_HBPostcode0ValidPostcode1ValidPostcodeChanged * 100.0d) / d;
            summary.put(sPercentageOfHBCount1_HBPostcode0ValidPostcode1ValidPostcodeChange,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_HBPostcode0ValidPostcode1NotValid * 100.0d) / d;
            summary.put(sPercentageOfHBCount1_HBPostcode0ValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_HBPostcode0NotValidPostcode1Valid * 100.0d) / d;
            summary.put(sPercentageOfHBCount1_HBPostcode0NotValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_HBPostcode0NotValidPostcode1NotValid * 100.0d) / d;
            summary.put(sPercentageOfHBCount1_HBPostcode0NotValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeNotChanged * 100.0d) / d;
            summary.put(sPercentageOfHBCount1_HBPostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeChanged * 100.0d) / d;
            summary.put(sPercentageOfHBCount1_HBPostcode0NotValidPostcode1NotValidPostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_HBPostcode0ValidPostcode1DNE * 100.0d) / d;
            summary.put(sPercentageOfHBCount1_HBPostcode0ValidPostcode1DNE,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_HBPostcode0DNEPostcode1Valid * 100.0d) / d;
            summary.put(sPercentageOfHBCount1_HBPostcode0DNEPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_HBPostcode0DNEPostcode1DNE * 100.0d) / d;
            summary.put(sPercentageOfHBCount1_HBPostcode0DNEPostcode1DNE,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_HBPostcode0DNEPostcode1NotValid * 100.0d) / d;
            summary.put(sPercentageOfHBCount1_HBPostcode0DNEPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_HBPostcode0NotValidPostcode1DNE * 100.0d) / d;
            summary.put(sPercentageOfHBCount1_HBPostcode0NotValidPostcode1DNE,
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
                sTotalCount_CTBPostcode0ValidPostcode1Valid,
                Integer.toString(TotalCount_CTBPostcode0ValidPostcode1Valid));
        summary.put(
                sTotalCount_CTBPostcode0ValidPostcode1ValidPostcodeNotChanged,
                Integer.toString(TotalCount_CTBPostcode0ValidPostcode1ValidPostcodeNotChanged));
        summary.put(
                sTotalCount_CTBPostcode0ValidPostcode1ValidPostcodeChanged,
                Integer.toString(TotalCount_CTBPostcode0ValidPostcode1ValidPostcodeChanged));
        summary.put(
                sTotalCount_CTBPostcode0ValidPostcode1NotValid,
                Integer.toString(TotalCount_CTBPostcode0ValidPostcode1NotValid));
        summary.put(
                sTotalCount_CTBPostcode0NotValidPostcode1Valid,
                Integer.toString(TotalCount_CTBPostcode0NotValidPostcode1Valid));
        summary.put(
                sTotalCount_CTBPostcode0NotValidPostcode1NotValid,
                Integer.toString(TotalCount_CTBPostcode0NotValidPostcode1NotValid));
        summary.put(
                sTotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                Integer.toString(TotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeNotChanged));
        summary.put(sTotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeChanged,
                Integer.toString(TotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeChanged));
        summary.put(
                sTotalCount_CTBPostcode0ValidPostcode1DNE,
                Integer.toString(TotalCount_CTBPostcode0ValidPostcode1DNE));
        summary.put(
                sTotalCount_CTBPostcode0DNEPostcode1Valid,
                Integer.toString(TotalCount_CTBPostcode0DNEPostcode1Valid));
        summary.put(
                sTotalCount_CTBPostcode0DNEPostcode1DNE,
                Integer.toString(TotalCount_CTBPostcode0DNEPostcode1DNE));
        summary.put(
                sTotalCount_CTBPostcode0DNEPostcode1NotValid,
                Integer.toString(TotalCount_CTBPostcode0DNEPostcode1NotValid));
        summary.put(
                sTotalCount_CTBPostcode0NotValidPostcode1DNE,
                Integer.toString(TotalCount_CTBPostcode0NotValidPostcode1DNE));
        summary.put(sTotalCount_CTBTTChangeClaimant,
                Integer.toString(TotalCount_CTBTTChangeClaimant));
//        summary.put(
//                sTotalCount_CTBTTChangeClaimantIgnoreMinus999,
//                Integer.toString(TotalCount_CTBTTChangeClaimantIgnoreMinus999));

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
        //d = TotalCount_TTClaimant1[5] + TotalCount_TTClaimant1[7];
        d = CTBCount1;
        if (d > 0) {
            percentage = (TotalCount_CTBPostcode0ValidPostcode1Valid * 100.0d) / d;
            summary.put(sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBPostcode0ValidPostcode1ValidPostcodeNotChanged * 100.0d) / d;
            summary.put(sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1ValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBPostcode0ValidPostcode1ValidPostcodeChanged * 100.0d) / d;
            summary.put(sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1ValidPostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBPostcode0ValidPostcode1NotValid * 100.0d) / d;
            summary.put(sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBPostcode0NotValidPostcode1Valid * 100.0d) / d;
            summary.put(sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBPostcode0NotValidPostcode1NotValid * 100.0d) / d;
            summary.put(sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeNotChanged * 100.0d) / d;
            summary.put(sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1NotValidPostcodeNotChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeChanged * 100.0d) / d;
            summary.put(sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1NotValidPostcodeChanged,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBPostcode0ValidPostcode1DNE * 100.0d) / d;
            summary.put(sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1DNE,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBPostcode0DNEPostcode1Valid * 100.0d) / d;
            summary.put(sPercentageOfCTBCount1_CTBPostcode0DNEPostcode1Valid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBPostcode0DNEPostcode1DNE * 100.0d) / d;
            summary.put(sPercentageOfCTBCount1_CTBPostcode0DNEPostcode1DNE,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBPostcode0DNEPostcode1NotValid * 100.0d) / d;
            summary.put(sPercentageOfCTBCount1_CTBPostcode0DNEPostcode1NotValid,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBPostcode0NotValidPostcode1DNE * 100.0d) / d;
            summary.put(sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1DNE,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = CTBCount0;
        if (d > 0) {
            percentage = (TotalCount_CTBTTChangeClaimant * 100.0d) / d;
            summary.put(PercentageOfCTBCount0_CTBTTChangeClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
//            percentage = (TotalCount_CTBTTChangeClaimantIgnoreMinus999 * 100.0d) / d;
//            summary.put(
//                    sPercentageOfCTB_CTBTTChangeClaimantIgnoreMinus999,
//                    Generic_BigDecimal.roundIfNecessary(
//                            BigDecimal.valueOf(percentage),
//                            decimalPlacePrecisionForPercentage,
//                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBTTsToSocialTTs * 100.0d) / d;
            summary.put(sPercentageOfCTBCount0_CTBTTsToSocialTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());

            percentage = (TotalCount_CTBTTsToMinus999TT * 100.0d) / d;
            summary.put(sPercentageOfCTBCount0_CTBTTsToMinus999TT,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBTTsToTT1 * 100.0d) / d;
            summary.put(sPercentageOfCTBCount0_CTBTTsToTT1,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBTTsToTT4 * 100.0d) / d;
            summary.put(sPercentageOfCTBCount0_CTBTTsToTT4,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBTTsToPrivateDeregulatedTTs * 100.0d) / d;
            summary.put(sPercentageOfCTBCount0_CTBTTsToPrivateDeregulatedTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
            percentage = (TotalCount_CTBTTsToHBTTs * 100.0d) / d;
            summary.put(sPercentageOfCTBCount0_CTBTTsToHBTTs,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
    }

    /**
     *
     * @param nTT
     * @param nEG
     * @param nPSI
     * @param summary
     */
    protected void addToSummarySingleTime(
            int nTT,
            int nEG,
            int nPSI,
            HashMap<String, String> summary) {
        // Add Counts
        addToSummarySingleTimeCounts0(nTT, summary);
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

    protected void addToSummarySingleTimeCounts0(
            int nTT,
            HashMap<String, String> summary) {
        AllCount1 = HBCount1 + CTBCount1;
        summary.put(sAllCount1, Integer.toString(AllCount1));
        summary.put(sHBCount1, Integer.toString(HBCount1));
        summary.put(sCTBCount1, Integer.toString(CTBCount1));
        summary.put(
                sAllTotalHouseholdSize,
                Long.toString(AllTotalHouseholdSize));
        double d;
        double n;
        d = AllCount1;
        if (d > 0) {
            n = AllTotalHouseholdSize / d;
            summary.put(
                    sAllAverageHouseholdSize,
                    Double.toString(n));
        }
        summary.put(
                sHBTotalHouseholdSize,
                Long.toString(HBTotalHouseholdSize));
        summary.put(
                sCTBTotalHouseholdSize,
                Long.toString(CTBTotalHouseholdSize));
        for (int TT = 1; TT < nTT; TT++) {
            summary.put(sTotal_HouseholdSizeTT[TT],
                    Long.toString(AllTotalHouseholdSizeTT[TT]));
        }
        TotalCount_AllPostcodeValidFormat = TotalCount_HBPostcodeValidFormat + TotalCount_CTBPostcodeValidFormat;
        TotalCount_AllPostcodeInvalidFormat = TotalCount_HBPostcodeInvalidFormat + TotalCount_CTBPostcodeInvalidFormat;
        TotalCount_AllPostcodeValid = TotalCount_HBPostcodeValid + TotalCount_CTBPostcodeValid;
        TotalCount_AllPostcodeInvalid = TotalCount_HBPostcodeInvalid + TotalCount_CTBPostcodeInvalid;
        summary.put(
                sTotalCount_AllPostcodeValidFormat,
                Long.toString(TotalCount_AllPostcodeValidFormat));
        summary.put(
                sTotalCount_AllPostcodeInvalidFormat,
                Long.toString(TotalCount_AllPostcodeInvalidFormat));
        summary.put(
                sTotalCount_AllPostcodeValid,
                Long.toString(TotalCount_AllPostcodeValid));
        summary.put(
                sTotalCount_AllPostcodeInvalid,
                Long.toString(TotalCount_AllPostcodeInvalid));
        summary.put(
                sTotalCount_HBPostcodeValidFormat,
                Long.toString(TotalCount_HBPostcodeValidFormat));
        summary.put(
                sTotalCount_HBPostcodeInvalidFormat,
                Long.toString(TotalCount_HBPostcodeInvalidFormat));
        summary.put(
                sTotalCount_HBPostcodeValid,
                Long.toString(TotalCount_HBPostcodeValid));
        summary.put(
                sTotalCount_HBPostcodeInvalid,
                Long.toString(TotalCount_HBPostcodeInvalid));
        summary.put(
                sTotalCount_CTBPostcodeValidFormat,
                Long.toString(TotalCount_CTBPostcodeValidFormat));
        summary.put(
                sTotalCount_CTBPostcodeInvalidFormat,
                Long.toString(TotalCount_CTBPostcodeInvalidFormat));
        summary.put(
                sTotalCount_CTBPostcodeValid,
                Long.toString(TotalCount_CTBPostcodeValid));
        summary.put(
                sTotalCount_CTBPostcodeInvalid,
                Long.toString(TotalCount_CTBPostcodeInvalid));

        d = AllCount1;
        if (d > 0) {
            n = (TotalCount_AllPostcodeValidFormat / d) * 100.0d;
            summary.put(
                    sPercentageOfAllCount1_AllPostcodeValidFormat,
                    Double.toString(n));
            n = (TotalCount_AllPostcodeValid / d) * 100.0d;
            summary.put(
                    sPercentageOfAllCount1_AllPostcodeValid,
                    Double.toString(n));
        }
        d = HBCount1;
        if (d > 0) {
            n = (TotalCount_HBPostcodeValidFormat / d) * 100.0d;
            summary.put(
                    sPercentageOfHBCount1_HBPostcodeValidFormat,
                    Double.toString(n));
            n = (TotalCount_HBPostcodeValid / d) * 100.0d;
            summary.put(
                    sPercentageOfHBCount1_HBPostcodeValid,
                    Double.toString(n));
        }
        d = CTBCount1;
        if (d > 0) {
            n = (TotalCount_CTBPostcodeValidFormat / d) * 100.0d;
            summary.put(
                    sPercentageOfCTBCount1_CTBPostcodeValidFormat,
                    Double.toString(n));
            n = (TotalCount_CTBPostcodeValid / d) * 100.0d;
            summary.put(
                    sPercentageOfCTBCount1_CTBPostcodeValid,
                    Double.toString(n));
        }
    }

    protected void addToSummarySingleTimeRates0(
            int nTT,
            HashMap<String, String> summary) {
        double ave;
        double d;
        double n;
        // All HouseholdSize
        d = AllCount1;
        n = AllTotalHouseholdSize;
        if (d > 0) {
            ave = n / d;
            summary.put(
                    sAllAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sAllAverageHouseholdSize,
                    s0);
        }
        // HB HouseholdSize
        d = HBCount1;
        n = HBTotalHouseholdSize;
        if (d > 0) {
            ave = n / d;
            summary.put(
                    sHBAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sHBAverageHouseholdSize,
                    s0);
        }
        // CTB HouseholdSize
        d = CTBCount1;
        n = CTBTotalHouseholdSize;
        if (d > 0) {
            ave = n / d;
            summary.put(
                    sCTBAverageHouseholdSize,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sCTBAverageHouseholdSize,
                    s0);
        }
        // TotalHouseholdSizeTT
        for (int i = 1; i < nTT; i++) {
            d = TotalCount_TTClaimant1[i];
            n = AllTotalHouseholdSizeTT[i];
            if (d > 0) {
                ave = n / d;
                summary.put(sAverage_HouseholdSizeTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            } else {
                summary.put(sAverage_HouseholdSizeTT[i],
                        s0);
            }
        }
    }

    protected void addToSummarySingleTimePSICounts(
            int nTT,
            int nPSI,
            HashMap<String, String> summary) {
        // PassportStandardIndicator
        for (int i = 1; i < nPSI; i++) {
            summary.put(
                    sTotalCount_AllPSI[i],
                    Long.toString(TotalCount_AllPSI[i]));
            summary.put(
                    sTotalCount_HBPSI[i],
                    Long.toString(TotalCount_HBPSI[i]));
            summary.put(
                    sTotalCount_CTBPSI[i],
                    Long.toString(TotalCount_CTBPSI[i]));
            for (int j = 1; j < nTT; j++) {
                summary.put(
                        sTotalCount_PSITT[i][j],
                        Long.toString(TotalCount_AllPSITT[i][j]));
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
            all = Integer.valueOf(summary.get(sTotalCount_AllPSI[i]));
            d = AllCount1;
            if (d > 0) {
                ave = (all * 100.0d) / d;
                summary.put(sPercentageOfAll_AllPSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sTotalCount_HBPSI[i]));
            d = HBCount1;
            if (d > 0) {
                ave = (all * 100.0d) / d;
                summary.put(
                        sPercentageOfHB_HBPSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sTotalCount_CTBPSI[i]));
            d = CTBCount1;
            if (d > 0) {
                ave = (all * 100.0d) / d;
                summary.put(
                        sPercentageOfCTB_CTBPSI[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(ave),
                                decimalPlacePrecisionForAverage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            for (int j = 1; j < nTT; j++) {
                all = Integer.valueOf(summary.get(sTotalCount_PSITT[i][j]));
                d = AllCount1;
                if (d > 0) {
                    ave = (all * 100.0d) / d;
                    summary.put(
                            sPercentageOfAll_PSITT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
                d = TotalCount_TTClaimant1[j];
                if (d > 0) {
                    ave = (all * 100.0d) / d;
                    summary.put(
                            sPercentageOfTT_PSITT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
                d = HBCount1;
                if (d > 0) {
                    ave = (all * 100.0d) / d;
                    summary.put(
                            sPercentageOfHB_PSITT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(ave),
                                    decimalPlacePrecisionForAverage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
                d = CTBCount1;
                if (d > 0) {
                    ave = (all * 100.0d) / d;
                    summary.put(
                            sPercentageOfCTB_PSITT[i][j],
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
        t = TotalCount_DisabilityAwardTT[1]
                + TotalCount_DisabilityAwardTT[2]
                + TotalCount_DisabilityAwardTT[3]
                + TotalCount_DisabilityAwardTT[4]
                + TotalCount_DisabilityAwardTT[5]
                + TotalCount_DisabilityAwardTT[6]
                + TotalCount_DisabilityAwardTT[7]
                + TotalCount_DisabilityAwardTT[8]
                + TotalCount_DisabilityAwardTT[9];
        summary.put(sTotalCount_AllDisabilityAward,
                Integer.toString(t));
        // DisabilityAwardHBTTs
        t = TotalCount_DisabilityAwardTT[1]
                + TotalCount_DisabilityAwardTT[2]
                + TotalCount_DisabilityAwardTT[3]
                + TotalCount_DisabilityAwardTT[4]
                + TotalCount_DisabilityAwardTT[6]
                + TotalCount_DisabilityAwardTT[8]
                + TotalCount_DisabilityAwardTT[9];
        summary.put(
                sTotalCount_DisabilityAwardHBTTs,
                Integer.toString(t));
        // DisabilityAwardCTBTTs
        t = TotalCount_DisabilityAwardTT[5]
                + TotalCount_DisabilityAwardTT[7];
        summary.put(
                sTotalCount_DisabilityAwardCTBTTs,
                Integer.toString(t));
        // DisabilityAwardSocialTTs
        t = TotalCount_DisabilityAwardTT[1] + TotalCount_DisabilityAwardTT[4];
        summary.put(
                sTotalCount_DisabilityAwardSocialTTs,
                Integer.toString(t));
        // DisabilityAwardPrivateDeregulatedTTs
        t = TotalCount_DisabilityAwardTT[3] + TotalCount_DisabilityAwardTT[6];
        summary.put(
                sTotalCount_DisabilityAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        // DisabilityPremiumAward
        t = TotalCount_DisabilityPremiumAwardTT[1]
                + TotalCount_DisabilityPremiumAwardTT[2]
                + TotalCount_DisabilityPremiumAwardTT[3]
                + TotalCount_DisabilityPremiumAwardTT[4]
                + TotalCount_DisabilityPremiumAwardTT[5]
                + TotalCount_DisabilityPremiumAwardTT[6]
                + TotalCount_DisabilityPremiumAwardTT[7]
                + TotalCount_DisabilityPremiumAwardTT[8]
                + TotalCount_DisabilityPremiumAwardTT[9];
        summary.put(sTotalCount_AllDisabilityPremiumAward,
                Integer.toString(t));
        // DisabilityPremiumAwardHBTTs
        t = TotalCount_DisabilityPremiumAwardTT[1]
                + TotalCount_DisabilityPremiumAwardTT[2]
                + TotalCount_DisabilityPremiumAwardTT[3]
                + TotalCount_DisabilityPremiumAwardTT[4]
                + TotalCount_DisabilityPremiumAwardTT[6]
                + TotalCount_DisabilityPremiumAwardTT[8]
                + TotalCount_DisabilityPremiumAwardTT[9];
        summary.put(
                sTotalCount_DisabilityPremiumAwardHBTTs,
                Integer.toString(t));
        // DisabilityPremiumAwardCTBTTs
        t = TotalCount_DisabilityPremiumAwardTT[5]
                + TotalCount_DisabilityPremiumAwardTT[7];
        summary.put(
                sTotalCount_DisabilityPremiumAwardCTBTTs,
                Integer.toString(t));
        // DisabilityPremiumAwardSocialTTs
        t = TotalCount_DisabilityPremiumAwardTT[1] + TotalCount_DisabilityPremiumAwardTT[4];
        summary.put(
                sTotalCount_DisabilityPremiumAwardSocialTTs,
                Integer.toString(t));
        // DisabilityPremiumAwardPrivateDeregulatedTTs
        t = TotalCount_DisabilityPremiumAwardTT[3] + TotalCount_DisabilityPremiumAwardTT[6];
        summary.put(
                sTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        // SevereDisabilityPremiumAward
        t = TotalCount_SevereDisabilityPremiumAwardTT[1]
                + TotalCount_SevereDisabilityPremiumAwardTT[2]
                + TotalCount_SevereDisabilityPremiumAwardTT[3]
                + TotalCount_SevereDisabilityPremiumAwardTT[4]
                + TotalCount_SevereDisabilityPremiumAwardTT[5]
                + TotalCount_SevereDisabilityPremiumAwardTT[6]
                + TotalCount_SevereDisabilityPremiumAwardTT[7]
                + TotalCount_SevereDisabilityPremiumAwardTT[8]
                + TotalCount_SevereDisabilityPremiumAwardTT[9];
        summary.put(sTotalCount_AllSevereDisabilityPremiumAward,
                Integer.toString(t));
        // SevereSevereDisabilityPremiumAwardHBTTs
        t = TotalCount_SevereDisabilityPremiumAwardTT[1]
                + TotalCount_SevereDisabilityPremiumAwardTT[2]
                + TotalCount_SevereDisabilityPremiumAwardTT[3]
                + TotalCount_SevereDisabilityPremiumAwardTT[4]
                + TotalCount_SevereDisabilityPremiumAwardTT[6]
                + TotalCount_SevereDisabilityPremiumAwardTT[8]
                + TotalCount_SevereDisabilityPremiumAwardTT[9];
        summary.put(
                sTotalCount_SevereDisabilityPremiumAwardHBTTs,
                Integer.toString(t));
        // SevereDisabilityPremiumAwardCTBTTs
        t = TotalCount_SevereDisabilityPremiumAwardTT[5]
                + TotalCount_SevereDisabilityPremiumAwardTT[7];
        summary.put(
                sTotalCount_SevereDisabilityPremiumAwardCTBTTs,
                Integer.toString(t));
        // SevereDisabilityPremiumAwardSocialTTs
        t = TotalCount_SevereDisabilityPremiumAwardTT[1] + TotalCount_SevereDisabilityPremiumAwardTT[4];
        summary.put(
                sTotalCount_SevereDisabilityPremiumAwardSocialTTs,
                Integer.toString(t));
        // SevereDisabilityPremiumAwardPrivateDeregulatedTTs
        t = TotalCount_SevereDisabilityPremiumAwardTT[3] + TotalCount_SevereDisabilityPremiumAwardTT[6];
        summary.put(
                sTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        // DisabledChildPremiumAward
        t = TotalCount_DisabledChildPremiumAwardTT[1]
                + TotalCount_DisabledChildPremiumAwardTT[2]
                + TotalCount_DisabledChildPremiumAwardTT[3]
                + TotalCount_DisabledChildPremiumAwardTT[4]
                + TotalCount_DisabledChildPremiumAwardTT[5]
                + TotalCount_DisabledChildPremiumAwardTT[6]
                + TotalCount_DisabledChildPremiumAwardTT[7]
                + TotalCount_DisabledChildPremiumAwardTT[8]
                + TotalCount_DisabledChildPremiumAwardTT[9];
        summary.put(sTotalCount_AllDisabledChildPremiumAward,
                Integer.toString(t));
        // SevereDisabledChildPremiumAwardHBTTs
        t = TotalCount_DisabledChildPremiumAwardTT[1]
                + TotalCount_DisabledChildPremiumAwardTT[2]
                + TotalCount_DisabledChildPremiumAwardTT[3]
                + TotalCount_DisabledChildPremiumAwardTT[4]
                + TotalCount_DisabledChildPremiumAwardTT[6]
                + TotalCount_DisabledChildPremiumAwardTT[8]
                + TotalCount_DisabledChildPremiumAwardTT[9];
        summary.put(
                sTotalCount_DisabledChildPremiumAwardHBTTs,
                Integer.toString(t));
        // DisabledChildPremiumAwardCTBTTs
        t = TotalCount_DisabledChildPremiumAwardTT[5]
                + TotalCount_DisabledChildPremiumAwardTT[7];
        summary.put(
                sTotalCount_DisabledChildPremiumAwardCTBTTs,
                Integer.toString(t));
        // DisabledChildPremiumAwardSocialTTs
        t = TotalCount_DisabledChildPremiumAwardTT[1] + TotalCount_DisabledChildPremiumAwardTT[4];
        summary.put(
                sTotalCount_DisabledChildPremiumAwardSocialTTs,
                Integer.toString(t));
        // DisabledChildPremiumAwardPrivateDeregulatedTTs
        t = TotalCount_DisabledChildPremiumAwardTT[3] + TotalCount_DisabledChildPremiumAwardTT[6];
        summary.put(
                sTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAward
        t = TotalCount_EnhancedDisabilityPremiumAwardTT[1]
                + TotalCount_EnhancedDisabilityPremiumAwardTT[2]
                + TotalCount_EnhancedDisabilityPremiumAwardTT[3]
                + TotalCount_EnhancedDisabilityPremiumAwardTT[4]
                + TotalCount_EnhancedDisabilityPremiumAwardTT[5]
                + TotalCount_EnhancedDisabilityPremiumAwardTT[6]
                + TotalCount_EnhancedDisabilityPremiumAwardTT[7]
                + TotalCount_EnhancedDisabilityPremiumAwardTT[8]
                + TotalCount_EnhancedDisabilityPremiumAwardTT[9];
        summary.put(sTotalCount_AllEnhancedDisabilityPremiumAward,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAwardHBTTs
        t = TotalCount_EnhancedDisabilityPremiumAwardTT[1]
                + TotalCount_EnhancedDisabilityPremiumAwardTT[2]
                + TotalCount_EnhancedDisabilityPremiumAwardTT[3]
                + TotalCount_EnhancedDisabilityPremiumAwardTT[4]
                + TotalCount_EnhancedDisabilityPremiumAwardTT[6]
                + TotalCount_EnhancedDisabilityPremiumAwardTT[8]
                + TotalCount_EnhancedDisabilityPremiumAwardTT[9];
        summary.put(
                sTotalCount_EnhancedDisabilityPremiumAwardHBTTs,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAwardCTBTTs
        t = TotalCount_EnhancedDisabilityPremiumAwardTT[5]
                + TotalCount_EnhancedDisabilityPremiumAwardTT[7];
        summary.put(
                sTotalCount_EnhancedDisabilityPremiumAwardCTBTTs,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAwardSocialTTs
        t = TotalCount_EnhancedDisabilityPremiumAwardTT[1] + TotalCount_EnhancedDisabilityPremiumAwardTT[4];
        summary.put(
                sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs,
                Integer.toString(t));
        // EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs
        t = TotalCount_EnhancedDisabilityPremiumAwardTT[3] + TotalCount_EnhancedDisabilityPremiumAwardTT[6];
        summary.put(
                sTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs,
                Integer.toString(t));
        // TT
        for (int i = 1; i < nTT; i++) {
            summary.put(
                    sTotalCount_DisabilityAwardTT[i],
                    Integer.toString(TotalCount_DisabilityAwardTT[i]));
            summary.put(
                    sTotalCount_DisabilityPremiumAwardTT[i],
                    Integer.toString(TotalCount_DisabilityPremiumAwardTT[i]));
            summary.put(
                    sTotalCount_SevereDisabilityPremiumAwardTT[i],
                    Integer.toString(TotalCount_SevereDisabilityPremiumAwardTT[i]));
            summary.put(
                    sTotalCount_DisabledChildPremiumAwardTT[i],
                    Integer.toString(TotalCount_DisabledChildPremiumAwardTT[i]));
            summary.put(
                    sTotalCount_EnhancedDisabilityPremiumAwardTT[i],
                    Integer.toString(TotalCount_EnhancedDisabilityPremiumAwardTT[i]));
        }
    }

    protected void addToSummarySingleTimeDisabilityRates(
            int nTT,
            HashMap<String, String> summary) {
        double percentage;
        double d;
        int t;
        // DisabilityAward
        t = Integer.valueOf(summary.get(sTotalCount_AllDisabilityAward));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(sPercentageOfAll_AllDisabilityAward,
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
        t = Integer.valueOf(summary.get(sTotalCount_AllDisabilityPremiumAward));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(sPercentageOfAll_AllDisabilityPremiumAward,
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
        t = Integer.valueOf(summary.get(sTotalCount_AllSevereDisabilityPremiumAward));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(sPercentageOfAll_AllSevereDisabilityPremiumAward,
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
        t = Integer.valueOf(summary.get(sTotalCount_AllDisabledChildPremiumAward));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(sPercentageOfAll_AllDisabledChildPremiumAward,
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
        t = Integer.valueOf(summary.get(sTotalCount_AllEnhancedDisabilityPremiumAward));
        d = AllCount1;
        if (d > 0) {
            percentage = (t * 100.0d) / d;
            summary.put(sPercentageOfAll_AllEnhancedDisabilityPremiumAward,
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
        // TT
        for (int i = 1; i < nTT; i++) {
            // AllCount1
            d = AllCount1;
            t = Integer.valueOf(summary.get(sTotalCount_DisabilityAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfAll_DisabilityAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_DisabilityPremiumAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfAll_DisabilityPremiumAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_SevereDisabilityPremiumAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfAll_SevereDisabilityPremiumAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_DisabledChildPremiumAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfAll_DisabledChildPremiumAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_EnhancedDisabilityPremiumAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfAll_EnhancedDisabilityPremiumAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            // HBCount1;
            d = HBCount1;
            t = Integer.valueOf(summary.get(sTotalCount_DisabilityAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfHB_DisabilityAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_DisabilityPremiumAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfHB_DisabilityPremiumAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_SevereDisabilityPremiumAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfHB_SevereDisabilityPremiumAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_DisabledChildPremiumAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfHB_DisabledChildPremiumAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_EnhancedDisabilityPremiumAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfHB_EnhancedDisabilityPremiumAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            // CTBCount1
            d = CTBCount1;
            t = Integer.valueOf(summary.get(sTotalCount_DisabilityAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfCTB_DisabilityAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_DisabilityPremiumAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfCTB_DisabilityPremiumAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_SevereDisabilityPremiumAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfCTB_SevereDisabilityPremiumAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_DisabledChildPremiumAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfCTB_DisabledChildPremiumAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_EnhancedDisabilityPremiumAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfCTB_EnhancedDisabilityPremiumAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            // TT
            d = TotalCount_TTClaimant1[i];
            t = Integer.valueOf(summary.get(sTotalCount_DisabilityAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfTT_DisabilityAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_DisabilityPremiumAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfTT_DisabilityPremiumAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_SevereDisabilityPremiumAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfTT_SevereDisabilityPremiumAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_DisabledChildPremiumAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfTT_DisabledChildPremiumAwardTT[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            t = Integer.valueOf(summary.get(sTotalCount_EnhancedDisabilityPremiumAwardTT[i]));
            if (d > 0) {
                percentage = (t * 100.0d) / d;
                summary.put(
                        sPercentageOfTT_EnhancedDisabilityPremiumAwardTT[i],
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
        summary.put(Strings.sTotal_WeeklyHBEntitlement,
                BigDecimal.valueOf(AllTotalWeeklyHBEntitlement).toPlainString());
        summary.put(Strings.sTotalCount_WeeklyHBEntitlementNonZero,
                Integer.toString(AllTotalWeeklyHBEntitlementNonZeroCount));
        summary.put(Strings.sTotalCount_WeeklyHBEntitlementZero,
                Integer.toString(AllTotalWeeklyHBEntitlementZeroCount));
        summary.put(Strings.sTotal_HBWeeklyHBEntitlement,
                BigDecimal.valueOf(HBTotalWeeklyHBEntitlement).toPlainString());
        summary.put(Strings.sTotalCount_HBWeeklyHBEntitlementNonZero,
                Integer.toString(TotalCount_HBWeeklyHBEntitlementNonZero));
        summary.put(Strings.sTotalCount_HBWeeklyHBEntitlementZero,
                Integer.toString(TotalCount_HBWeeklyHBEntitlementZero));
        summary.put(Strings.sTotal_CTBWeeklyHBEntitlement,
                BigDecimal.valueOf(CTBTotalWeeklyHBEntitlement).toPlainString());
        summary.put(Strings.sTotalCount_CTBWeeklyHBEntitlementNonZero,
                Integer.toString(TotalCount_CTBWeeklyHBEntitlementNonZero));
        summary.put(Strings.sTotalCount_CTBWeeklyHBEntitlementZero,
                Integer.toString(CTBTotalWeeklyHBEntitlementZeroCount));
        // WeeklyCTBEntitlement
        summary.put(Strings.sTotal_WeeklyCTBEntitlement,
                BigDecimal.valueOf(AllTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(Strings.sTotalCount_WeeklyCTBEntitlementNonZero,
                Integer.toString(TotalCount_AllWeeklyCTBEntitlementNonZero));
        summary.put(Strings.sTotalCount_WeeklyCTBEntitlementZero,
                Integer.toString(AllTotalWeeklyCTBEntitlementZeroCount));
        summary.put(Strings.sTotal_HBWeeklyCTBEntitlement,
                BigDecimal.valueOf(HBTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(Strings.sTotalCount_HBWeeklyCTBEntitlementNonZero,
                Integer.toString(TotalCount_HBWeeklyCTBEntitlementNonZero));
        summary.put(Strings.sTotalCount_HBWeeklyCTBEntitlementZero,
                Integer.toString(HBTotalWeeklyCTBEntitlementZeroCount));
        summary.put(Strings.sTotal_CTBWeeklyCTBEntitlement,
                BigDecimal.valueOf(CTBTotalWeeklyCTBEntitlement).toPlainString());
        summary.put(Strings.sTotalCount_CTBWeeklyCTBEntitlementNonZero,
                Integer.toString(TotalCount_CTBWeeklyCTBEntitlementNonZero));
        summary.put(Strings.sTotalCount_CTBWeeklyCTBEntitlementZero,
                Integer.toString(CTBTotalWeeklyCTBEntitlementZeroCount));
        // WeeklyEligibleRentAmount
        summary.put(Strings.sTotal_WeeklyEligibleRentAmount,
                BigDecimal.valueOf(AllTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(Strings.sTotalCount_WeeklyEligibleRentAmountNonZero,
                Integer.toString(AllTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(Strings.sTotalCount_WeeklyEligibleRentAmountZero,
                Integer.toString(AllTotalWeeklyEligibleRentAmountZeroCount));
        summary.put(Strings.sTotal_HBWeeklyEligibleRentAmount,
                BigDecimal.valueOf(HBTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(Strings.sTotalCount_HBWeeklyEligibleRentAmountNonZero,
                Integer.toString(HBTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(Strings.sTotalCount_HBWeeklyEligibleRentAmountZero,
                Integer.toString(HBTotalWeeklyEligibleRentAmountZeroCount));
        summary.put(Strings.sTotal_CTBWeeklyEligibleRentAmount,
                BigDecimal.valueOf(CTBTotalWeeklyEligibleRentAmount).toPlainString());
        summary.put(Strings.sTotalCount_CTBWeeklyEligibleRentAmountNonZero,
                Integer.toString(CTBTotalWeeklyEligibleRentAmountNonZeroCount));
        summary.put(Strings.sTotalCount_CTBWeeklyEligibleRentAmountZero,
                Integer.toString(CTBTotalWeeklyEligibleRentAmountZeroCount));
        // WeeklyEligibleCouncilTaxAmount
        summary.put(
                sAllTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(AllTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(sTotalCount_AllWeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(TotalCount_AllWeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(sTotalCount_AllWeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(TotalCount_AllWeeklyEligibleCouncilTaxAmountZero));
        summary.put(
                sHBTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(HBTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(sTotalCount_HBWeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(TotalCount_HBWeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(sTotalCount_HBWeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(TotalCount_HBWeeklyEligibleCouncilTaxAmountZero));
        summary.put(
                sCTBTotalWeeklyEligibleCouncilTaxAmount,
                BigDecimal.valueOf(CTBTotalWeeklyEligibleCouncilTaxAmount).toPlainString());
        summary.put(sTotalCount_CTBWeeklyEligibleCouncilTaxAmountNonZero,
                Integer.toString(TotalCount_CTBWeeklyEligibleCouncilTaxAmountNonZero));
        summary.put(sTotalCount_CTBWeeklyEligibleCouncilTaxAmountZero,
                Integer.toString(TotalCount_CTBWeeklyEligibleCouncilTaxAmountZero));
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
                sTotalCount_HBContractualRentAmountNonZeroCount,
                Integer.toString(HBTotalContractualRentAmountNonZeroCount));
        summary.put(
                sTotalCount_HBContractualRentAmountZeroCount,
                Integer.toString(HBTotalContractualRentAmountZeroCount));
        summary.put(
                sCTBTotalContractualRentAmount,
                BigDecimal.valueOf(CTBTotalContractualRentAmount).toPlainString());
        summary.put(
                sTotalCount_CTBContractualRentAmountNonZeroCount,
                Integer.toString(CTBTotalContractualRentAmountNonZeroCount));
        summary.put(
                sTotalCount_CTBContractualRentAmountZeroCount,
                Integer.toString(CTBTotalContractualRentAmountZeroCount));
        // WeeklyAdditionalDiscretionaryPayment
        summary.put(
                sAllTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(AllTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sTotalCount_AllWeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(AllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sTotalCount_AllWeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(AllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        summary.put(
                sHBTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(HBTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(HBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(HBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        summary.put(
                sCTBTotalWeeklyAdditionalDiscretionaryPayment,
                BigDecimal.valueOf(CTBTotalWeeklyAdditionalDiscretionaryPayment).toPlainString());
        summary.put(
                sTotalCount_CTBWeeklyAdditionalDiscretionaryPaymentNonZero,
                Integer.toString(CTBTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount));
        summary.put(
                sTotalCount_CTBWeeklyAdditionalDiscretionaryPaymentZero,
                Integer.toString(CTBTotalWeeklyAdditionalDiscretionaryPaymentZeroCount));
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        summary.put(
                sAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sTotalCount_AllWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sTotalCount_AllWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        summary.put(
                sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(HBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        summary.put(
                sCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability,
                BigDecimal.valueOf(CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability).toPlainString());
        summary.put(
                sTotalCount_CTBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero,
                Integer.toString(CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount));
        summary.put(
                sTotalCount_CTBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero,
                Integer.toString(CTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount));
        // Employed
        int t;
        t = TotalCount_HBEmployedClaimants + TotalCount_CTBEmployedClaimants;
        summary.put(
                sTotalCount_AllClaimantsEmployed,
                Integer.toString(t));
        summary.put(
                sTotalCount_HBClaimantsEmployed,
                Integer.toString(TotalCount_HBEmployedClaimants));
        summary.put(sTotalCount_CTBClaimantsEmployed,
                Integer.toString(TotalCount_CTBEmployedClaimants));
        // Self Employed
        summary.put(sTotalCount_HBClaimantsSelfEmployed,
                Integer.toString(TotalCount_HBSelfEmployedClaimants));
        summary.put(sTotalCount_CTBClaimantsSelfEmployed,
                Integer.toString(TotalCount_CTBSelfEmployedClaimants));
        // Students
        summary.put(sTotalCount_HBClaimantsStudents,
                Integer.toString(TotalCount_HBStudentsClaimants));
        summary.put(sTotalCount_CTBClaimantsStudents,
                Integer.toString(TotalCount_CTBStudentsClaimants));
        // LHACases
        t = TotalCount_HBLHACases + TotalCount_CTBLHACases;
        summary.put(
                sTotalCount_AllLHACases,
                Integer.toString(t));
        summary.put(sTotalCount_HBLHACases,
                Integer.toString(TotalCount_HBLHACases));
        summary.put(
                sTotalCount_CTBLHACases,
                Integer.toString(TotalCount_CTBLHACases));
    }

    protected void addToSummarySingleTimeRates1(
            HashMap<String, String> summary) {
        double ave;
        double d;
        double t;
        // WeeklyHBEntitlement
        t = Double.valueOf(summary.get(Strings.sTotal_WeeklyHBEntitlement));
        d = AllTotalWeeklyHBEntitlementNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(Strings.sAverage_NonZero_WeeklyHBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(Strings.sAverage_NonZero_WeeklyHBEntitlement,
                    s0);
        }
        t = Double.valueOf(summary.get(Strings.sTotal_HBWeeklyHBEntitlement));
        d = TotalCount_HBWeeklyHBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(Strings.sAverage_NonZero_HBWeeklyHBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(Strings.sAverage_NonZero_HBWeeklyHBEntitlement,
                    s0);
        }
        t = Double.valueOf(summary.get(Strings.sTotal_CTBWeeklyHBEntitlement));
        d = TotalCount_CTBWeeklyHBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(Strings.sAverage_NonZero_CTBWeeklyHBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(Strings.sAverage_NonZero_CTBWeeklyHBEntitlement,
                    s0);
        }
        // WeeklyCTBEntitlement
        t = Double.valueOf(summary.get(Strings.sTotal_WeeklyCTBEntitlement));
        d = TotalCount_AllWeeklyCTBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(Strings.sAverage_NonZero_WeeklyCTBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(Strings.sAverage_NonZero_WeeklyCTBEntitlement,
                    s0);
        }
        t = Double.valueOf(summary.get(Strings.sTotal_HBWeeklyCTBEntitlement));
        d = TotalCount_HBWeeklyCTBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(Strings.sAverage_NonZero_HBWeeklyCTBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(Strings.sAverage_NonZero_HBWeeklyCTBEntitlement,
                    s0);
        }
        t = Double.valueOf(summary.get(Strings.sTotal_CTBWeeklyCTBEntitlement));
        d = TotalCount_CTBWeeklyCTBEntitlementNonZero;
        if (d > 0) {
            ave = t / d;
            summary.put(Strings.sAverage_NonZero_CTBWeeklyCTBEntitlement,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(Strings.sAverage_NonZero_CTBWeeklyCTBEntitlement,
                    s0);
        }
        // WeeklyEligibleRentAmount
        t = Double.valueOf(summary.get(Strings.sTotal_WeeklyEligibleRentAmount));
        d = AllTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(Strings.SHBE_Strings.sAverage_NonZero_WeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(Strings.SHBE_Strings.sAverage_NonZero_WeeklyEligibleRentAmount,
                    s0);
        }
        t = Double.valueOf(summary.get(Strings.sTotal_HBWeeklyEligibleRentAmount));
        d = HBTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(Strings.sAverage_NonZero_HBWeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(Strings.sAverage_NonZero_HBWeeklyEligibleRentAmount,
                    s0);
        }
        t = Double.valueOf(summary.get(Strings.sTotal_CTBWeeklyEligibleRentAmount));
        d = CTBTotalWeeklyEligibleRentAmountNonZeroCount;
        if (d > 0) {
            ave = t / d;
            summary.put(Strings.sAverage_NonZero_CTBWeeklyEligibleRentAmount,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(Strings.sAverage_NonZero_CTBWeeklyEligibleRentAmount,
                    s0);
        }
        // WeeklyEligibleCouncilTaxAmount
        t = Double.valueOf(summary.get(sAllTotalWeeklyEligibleCouncilTaxAmount));
        d = TotalCount_AllWeeklyEligibleCouncilTaxAmountNonZero;
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
        d = TotalCount_HBWeeklyEligibleCouncilTaxAmountNonZero;
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
        d = TotalCount_CTBWeeklyEligibleCouncilTaxAmountNonZero;
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
        t = Integer.valueOf(summary.get(sTotalCount_AllClaimantsEmployed));
        d = AllCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_AllClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentageOfAll_AllClaimantsEmployed,
                    s0);
        }
        t = Integer.valueOf(summary.get(sTotalCount_HBClaimantsEmployed));
        d = HBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_HBClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentageOfHB_HBClaimantsEmployed,
                    s0);
        }
        t = Integer.valueOf(summary.get(sTotalCount_CTBClaimantsEmployed));
        d = CTBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfCTB_CTBClaimantsEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentageOfCTB_CTBClaimantsEmployed,
                    s0);
        }
        // Self Employed
        t = Integer.valueOf(summary.get(sTotalCount_HBClaimantsSelfEmployed));
        d = HBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_HBClaimantsSelfEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentageOfHB_HBClaimantsSelfEmployed,
                    s0);
        }
        t = Integer.valueOf(summary.get(sTotalCount_CTBClaimantsSelfEmployed));
        d = CTBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfCTB_CTBClaimantsSelfEmployed,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentageOfCTB_CTBClaimantsSelfEmployed,
                    s0);
        }
        // Students
        t = Integer.valueOf(summary.get(sTotalCount_HBClaimantsStudents));
        d = HBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_HBClaimantsStudents,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentageOfHB_HBClaimantsStudents,
                    s0);
        }
        t = Integer.valueOf(summary.get(sTotalCount_CTBClaimantsStudents));
        d = CTBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfCTB_CTBClaimantsStudents,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentageOfCTB_CTBClaimantsStudents,
                    s0);
        }
        // LHACases
        t = Integer.valueOf(summary.get(sTotalCount_AllLHACases));
        d = AllCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(sPercentageOfAll_AllLHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(sPercentageOfAll_AllLHACases,
                    s0);
        }
        t = Integer.valueOf(summary.get(sTotalCount_HBLHACases));
        d = HBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfHB_HBLHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentageOfHB_HBLHACases,
                    s0);
        }
        t = Integer.valueOf(summary.get(sTotalCount_CTBLHACases));
        d = CTBCount1;
        if (d > 0) {
            ave = (t * 100.0d) / d;
            summary.put(
                    sPercentageOfCTB_CTBLHACases,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(ave),
                            decimalPlacePrecisionForAverage,
                            RoundingMode.HALF_UP).toPlainString());
        } else {
            summary.put(
                    sPercentageOfCTB_CTBLHACases,
                    s0);
        }
    }

    protected void addToSummarySingleTimeEthnicityCounts(
            int nEG,
            int nTT,
            HashMap<String, String> summary) {
        for (int i = 1; i < nEG; i++) {
            int all;
            all = HBEthnicGroupCount[i] + CTBEthnicGroupCount[i];
            summary.put(
                    sTotalCount_AllEthnicGroupClaimant[i],
                    Integer.toString(all));
            summary.put(
                    sTotalCount_HBEthnicGroupClaimant[i],
                    Integer.toString(HBEthnicGroupCount[i]));
            summary.put(
                    sTotalCount_CTBEthnicGroupClaimant[i],
                    Integer.toString(CTBEthnicGroupCount[i]));
            for (int j = 0; j < nTT; j++) {
                summary.put(
                        sTotalCount_AllEthnicGroupClaimantTT[i][j],
                        Integer.toString(TotalCount_AllEthnicGroupClaimantTT[i][j]));
            }
            summary.put(
                    sTotalCount_AllEthnicGroupSocialTTClaimant[i],
                    Integer.toString(TotalCount_AllEthnicGroupClaimantTT[i][1] + TotalCount_AllEthnicGroupClaimantTT[i][4]));
            summary.put(
                    sTotalCount_AllEthnicGroupPrivateDeregulatedTTClaimant[i],
                    Integer.toString(TotalCount_AllEthnicGroupClaimantTT[i][3] + TotalCount_AllEthnicGroupClaimantTT[i][6]));
        }
    }

    protected void addToSummarySingleTimeEthnicityRates(
            int nEG,
            int nTT,
            HashMap<String, String> summary) {
        // Ethnicity
        double percentage;
        double all;
        double d;
        for (int i = 1; i < nEG; i++) {
            all = Integer.valueOf(summary.get(sTotalCount_AllEthnicGroupClaimant[i]));
            d = AllCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(sPercentageOfAll_EthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sTotalCount_HBEthnicGroupClaimant[i]));
            d = HBCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(sPercentageOfHB_HBEthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sTotalCount_CTBEthnicGroupClaimant[i]));
            d = CTBCount1;
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(sPercentageOfCTB_CTBEthnicGroupClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            for (int j = 1; j < nTT; j++) {
                all = Integer.valueOf(summary.get(sTotalCount_AllEthnicGroupClaimantTT[i][j]));
                d = TotalCount_TTClaimant1[j];
                if (d > 0) {
                    percentage = (all * 100.0d) / d;
                    summary.put(sPercentageOfTT_EthnicGroupClaimantTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(percentage),
                                    decimalPlacePrecisionForPercentage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
                d = Integer.valueOf(summary.get(sTotalCount_AllEthnicGroupClaimant[i]));
                if (d > 0) {
                    percentage = (all * 100.0d) / d;
                    summary.put(sPercentageOfEthnicGroup_EthnicGroupClaimantTT[i][j],
                            Generic_BigDecimal.roundIfNecessary(
                                    BigDecimal.valueOf(percentage),
                                    decimalPlacePrecisionForPercentage,
                                    RoundingMode.HALF_UP).toPlainString());
                }
            }
            all = Integer.valueOf(summary.get(sTotalCount_AllEthnicGroupSocialTTClaimant[i]));
            d = TotalCount_TTClaimant1[1] + TotalCount_TTClaimant1[4];
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(sPercentageOfSocialTT_EthnicGroupSocialTTClaimant[i],
                        Generic_BigDecimal.roundIfNecessary(
                                BigDecimal.valueOf(percentage),
                                decimalPlacePrecisionForPercentage,
                                RoundingMode.HALF_UP).toPlainString());
            }
            all = Integer.valueOf(summary.get(sTotalCount_AllEthnicGroupPrivateDeregulatedTTClaimant[i]));
            d = TotalCount_TTClaimant1[3] + TotalCount_TTClaimant1[6];
            if (d > 0) {
                percentage = (all * 100.0d) / d;
                summary.put(sPercentageOfPrivateDeregulatedTT_EthnicGroupPrivateDeregulatedTTClaimant[i],
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
        // HB
        summary.put(sHBCount1, Integer.toString(HBCount1));
        summary.put(sHBPTICount1, Integer.toString(HBPTICount1));
        summary.put(sHBPTSCount1, Integer.toString(HBPTSCount1));
        summary.put(sHBPTOCount1, Integer.toString(HBPTOCount1));
        // CTB
        summary.put(sCTBCount1, Integer.toString(CTBCount1));
        summary.put(sCTBPTICount1, Integer.toString(CTBPTICount1));
        summary.put(sCTBPTSCount1, Integer.toString(CTBPTSCount1));
        summary.put(sCTBPTOCount1, Integer.toString(CTBPTOCount1));
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
        if (d > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_SocialTTsClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
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
        if (d > 0) {
            percentage = (all * 100.0d) / d;
            summary.put(
                    sPercentageOfAll_PrivateDeregulatedTTsClaimant,
                    Generic_BigDecimal.roundIfNecessary(
                            BigDecimal.valueOf(percentage),
                            decimalPlacePrecisionForPercentage,
                            RoundingMode.HALF_UP).toPlainString());
        }
        d = HBCount1;
        if (d > 0) {
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
            SHBE_Record Record0,
            SHBE_D_Record D_Record0,
            SHBE_Record Record1,
            SHBE_D_Record D_Record1) {
        boolean isHBClaim1;
        isHBClaim1 = false;
        boolean isCTBOnlyClaim1;
        isCTBOnlyClaim1 = false;
        int TT1;
        TT1 = SHBE_TenancyType_Handler.iMinus999;
        if (D_Record1 != null) {
            //doSingleTimeCount(Record1, D_Record1);
            isHBClaim1 = SHBE_Handler.isHBClaim(D_Record1);
            isCTBOnlyClaim1 = SHBE_Handler.isCTBOnlyClaim(D_Record1);
            TT1 = D_Record1.getTenancyType();
        }
//        AllCount1 = HBCount1 + CTBCount1;
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
            if (Record1 != null || Record0 != null) {
                if (Record1 == null) {
                    if (isHBClaim0) {
                        doCompare2TimesHBCount(
                                TT0,
                                Record0.getPostcodeID(),
                                Record0.isClaimPostcodeFValidFormat(),
                                Record0.isClaimPostcodeFMappable(),
                                TT1,
                                null,
                                false,
                                false);
                    } else if (Record0 == null) {
                        doCompare2TimesHBCount(
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
                    doCompare2TimesHBCount(
                            TT0,
                            null,
                            false,
                            false,
                            TT1,
                            Record1.getPostcodeID(),
                            Record1.isClaimPostcodeFValidFormat(),
                            Record1.isClaimPostcodeFMappable());
                } else if (isHBClaim0) {
                    doCompare2TimesHBCount(
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
            if (Record1 != null || Record0 != null) {
                if (Record1 == null) {
                    if (isCTBOnlyClaim0) {
                        doCompare2TimesCTBCount(
                                TT0,
                                Record0.getPostcodeID(),
                                Record0.isClaimPostcodeFValidFormat(),
                                Record0.isClaimPostcodeFMappable(),
                                TT1,
                                null,
                                false,
                                false);
                    } else if (Record0 == null) {
                        doCompare2TimesCTBCount(
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
                    doCompare2TimesCTBCount(
                            TT0,
                            null,
                            false,
                            false,
                            TT1,
                            Record1.getPostcodeID(),
                            Record1.isClaimPostcodeFValidFormat(),
                            Record1.isClaimPostcodeFMappable());
                } else if (isCTBOnlyClaim0) {
                    doCompare2TimesCTBCount(
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
        TotalCount_AllTTChangeClaimant = TotalCount_HBTTChangeClaimant + TotalCount_CTBTTChangeClaimant;
        //TotalCount_AllTTChangeClaimantIgnoreMinus999 = TotalCount_HBTTChangeClaimantIgnoreMinus999 + TotalCount_CTBTTChangeClaimantIgnoreMinus999;
        TotalCount_AllPostcode0ValidPostcode1Valid = TotalCount_HBPostcode0ValidPostcode1Valid + TotalCount_CTBPostcode0ValidPostcode1Valid;
        TotalCount_AllPostcode0ValidPostcode1ValidPostcodeNotChanged = TotalCount_HBPostcode0ValidPostcode1ValidPostcodeNotChanged + TotalCount_CTBPostcode0ValidPostcode1ValidPostcodeNotChanged;
        TotalCount_AllPostcode0ValidPostcode1ValidPostcodeChanged = TotalCount_HBPostcode0ValidPostcode1ValidPostcodeChanged + TotalCount_CTBPostcode0ValidPostcode1ValidPostcodeChanged;
        TotalCount_AllPostcode0ValidPostcode1NotValid = TotalCount_HBPostcode0ValidPostcode1NotValid + TotalCount_CTBPostcode0ValidPostcode1NotValid;
        TotalCount_AllPostcode0NotValidPostcode1Valid = TotalCount_HBPostcode0NotValidPostcode1Valid + TotalCount_CTBPostcode0NotValidPostcode1Valid;
        TotalCount_AllPostcode0NotValidPostcode1NotValid = TotalCount_HBPostcode0NotValidPostcode1NotValid + TotalCount_CTBPostcode0NotValidPostcode1NotValid;
        TotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeNotChanged = TotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeNotChanged + TotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeNotChanged;
        TotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeChanged = TotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeChanged + TotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeChanged;
        TotalCount_AllPostcode0ValidPostcode1DNE = TotalCount_HBPostcode0ValidPostcode1DNE + TotalCount_CTBPostcode0ValidPostcode1DNE;
        TotalCount_AllPostcode0DNEPostcode1Valid = TotalCount_HBPostcode0DNEPostcode1Valid + TotalCount_CTBPostcode0DNEPostcode1Valid;
        TotalCount_AllPostcode0DNEPostcode1DNE = TotalCount_HBPostcode0DNEPostcode1DNE + TotalCount_CTBPostcode0DNEPostcode1DNE;
        TotalCount_AllPostcode0DNEPostcode1NotValid = TotalCount_HBPostcode0DNEPostcode1NotValid + TotalCount_CTBPostcode0DNEPostcode1NotValid;
        TotalCount_AllPostcode0NotValidPostcode1DNE = TotalCount_HBPostcode0NotValidPostcode1DNE + TotalCount_CTBPostcode0NotValidPostcode1DNE;

    }

    protected void doSingleTimeCount(
            SHBE_Record Record,
            SHBE_D_Record D_Record) {
        int EG;
        int TT;
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
        //ClaimantsEthnicGroup0 = D_Record.getClaimantsEthnicGroup();
        EG = SHBE_Handler.getEthnicityGroup(D_Record);
        // All unfiltered counts
        TT = D_Record.getTenancyType();
        TotalCount_TTClaimant1[TT]++;
        // Disability
        DisabilityPremiumAwarded = D_Record.getDisabilityPremiumAwarded();
        if (DisabilityPremiumAwarded == 1) {
            TotalCount_DisabilityPremiumAwardTT[TT]++;
        }
        SevereDisabilityPremiumAwarded = D_Record.getSevereDisabilityPremiumAwarded();
        if (SevereDisabilityPremiumAwarded == 1) {
            TotalCount_SevereDisabilityPremiumAwardTT[TT]++;
        }
        DisabledChildPremiumAwarded = D_Record.getDisabledChildPremiumAwarded();
        if (DisabledChildPremiumAwarded == 1) {
            TotalCount_DisabledChildPremiumAwardTT[TT]++;
        }
        EnhancedDisabilityPremiumAwarded = D_Record.getEnhancedDisabilityPremiumAwarded();
        if (EnhancedDisabilityPremiumAwarded == 1) {
            TotalCount_EnhancedDisabilityPremiumAwardTT[TT]++;
        }
        // General Household Disability Flag
        if (DisabilityPremiumAwarded == 1
                || SevereDisabilityPremiumAwarded == 1
                || DisabledChildPremiumAwarded == 1
                || EnhancedDisabilityPremiumAwarded == 1) {
            TotalCount_DisabilityAwardTT[TT]++;
        }
        // Passported Standard Indicator
        PSI = D_Record.getPassportedStandardIndicator();
        TotalCount_AllPSI[PSI]++;
        TotalCount_AllPSITT[PSI][TT]++;
        // Household size
        HouseholdSize = SHBE_Handler.getHouseholdSize(D_Record);
        AllTotalHouseholdSize += HouseholdSize;
        // Entitlements
        WeeklyHousingBenefitEntitlement = D_Record.getWeeklyHousingBenefitEntitlement();
        if (WeeklyHousingBenefitEntitlement > 0) {
            AllTotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
            AllTotalWeeklyHBEntitlementNonZeroCount++;
        } else {
            AllTotalWeeklyHBEntitlementZeroCount++;
        }
        WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
        if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
            AllTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
            TotalCount_AllWeeklyCTBEntitlementNonZero++;
        } else {
            AllTotalWeeklyCTBEntitlementZeroCount++;
        }
        // Eligible Amounts
        WeeklyEligibleRentAmount = D_Record.getWeeklyEligibleRentAmount();
        if (WeeklyEligibleRentAmount > 0) {
            AllTotalWeeklyEligibleRentAmount += WeeklyEligibleRentAmount;
            AllTotalWeeklyEligibleRentAmountNonZeroCount++;
        } else {
            AllTotalWeeklyEligibleRentAmountZeroCount++;
        }
        WeeklyEligibleCouncilTaxAmount = D_Record.getWeeklyEligibleCouncilTaxAmount();
        if (WeeklyEligibleCouncilTaxAmount > 0) {
            AllTotalWeeklyEligibleCouncilTaxAmount += WeeklyEligibleCouncilTaxAmount;
            TotalCount_AllWeeklyEligibleCouncilTaxAmountNonZero++;
        } else {
            TotalCount_AllWeeklyEligibleCouncilTaxAmountZero++;
        }
        ContractualRentAmount = D_Record.getContractualRentAmount();
        if (ContractualRentAmount > 0) {
            AllTotalContractualRentAmount += ContractualRentAmount;
            AllTotalContractualRentAmountNonZeroCount++;
        } else {
            AllTotalContractualRentAmountZeroCount++;
        }
        // Additional Payments
        WeeklyAdditionalDiscretionaryPayment = D_Record.getWeeklyAdditionalDiscretionaryPayment();
        if (WeeklyAdditionalDiscretionaryPayment > 0) {
            AllTotalWeeklyAdditionalDiscretionaryPayment += WeeklyAdditionalDiscretionaryPayment;
            AllTotalWeeklyAdditionalDiscretionaryPaymentNonZeroCount++;
        } else {
            AllTotalWeeklyAdditionalDiscretionaryPaymentZeroCount++;
        }
        WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = D_Record.getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability();
        if (WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability > 0) {
            AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability += WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
            AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZeroCount++;
        } else {
            AllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZeroCount++;
        }
        // HBClaim only counts
        if (SHBE_Handler.isHBClaim(D_Record)) {
            TotalCount_HBPSI[PSI]++;
            TotalCount_HBPSITT[PSI][TT]++;
            //if (HBRef.equalsIgnoreCase(CTBRef)) {
            HBTotalHouseholdSize += HouseholdSize;
            AllTotalHouseholdSizeTT[TT] += HouseholdSize;
            int ClaimantsNetWeeklyIncomeFromEmployment;
            ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
            if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
                TotalCount_HBEmployedClaimants++;
            }
            int ClaimantsNetWeeklyIncomeFromSelfEmployment;
            ClaimantsNetWeeklyIncomeFromSelfEmployment = D_Record.getClaimantsNetWeeklyIncomeFromSelfEmployment();
            if (ClaimantsNetWeeklyIncomeFromSelfEmployment > 0) {
                TotalCount_HBSelfEmployedClaimants++;
            }
            String ClaimantsStudentIndicator;
            ClaimantsStudentIndicator = D_Record.getClaimantsStudentIndicator();
            if (ClaimantsStudentIndicator != null) {
                if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                    TotalCount_HBStudentsClaimants++;
                }
            }
            String LHARegulationsApplied;
            LHARegulationsApplied = D_Record.getLHARegulationsApplied();
            if (LHARegulationsApplied != null) {
                if (LHARegulationsApplied.equalsIgnoreCase("1")
                        || LHARegulationsApplied.equalsIgnoreCase("Yes")) {
                    TotalCount_HBLHACases++;
                }
            }
            if (WeeklyHousingBenefitEntitlement > 0) {
                HBTotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
                TotalCount_HBWeeklyHBEntitlementNonZero++;
            } else {
                TotalCount_HBWeeklyHBEntitlementZero++;
            }
            WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
            if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
                HBTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
                TotalCount_HBWeeklyCTBEntitlementNonZero++;
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
                TotalCount_HBWeeklyEligibleCouncilTaxAmountNonZero++;
            } else {
                TotalCount_HBWeeklyEligibleCouncilTaxAmountZero++;
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
                    D_Record.getStatusOfHBClaimAtExtractDate(),
                    EG,
                    TT,
                    Record.isClaimPostcodeFValidFormat(),
                    Record.isClaimPostcodeFMappable());
        }
        // CTB Claim only counts
        if (SHBE_Handler.isCTBOnlyClaim(D_Record)) {
            TotalCount_CTBPSI[PSI]++;
            TotalCount_CTBPSITT[PSI][TT]++;
            CTBTotalHouseholdSize += HouseholdSize;
            AllTotalHouseholdSizeTT[TT] += HouseholdSize;
            int ClaimantsNetWeeklyIncomeFromEmployment;
            ClaimantsNetWeeklyIncomeFromEmployment = D_Record.getClaimantsNetWeeklyIncomeFromEmployment();
            if (ClaimantsNetWeeklyIncomeFromEmployment > 0) {
                TotalCount_CTBEmployedClaimants++;
            }
            int ClaimantsNetWeeklyIncomeFromSelfEmployment;
            ClaimantsNetWeeklyIncomeFromSelfEmployment = D_Record.getClaimantsNetWeeklyIncomeFromSelfEmployment();
            if (ClaimantsNetWeeklyIncomeFromSelfEmployment > 0) {
                TotalCount_CTBSelfEmployedClaimants++;
            }
            String ClaimantsStudentIndicator;
            ClaimantsStudentIndicator = D_Record.getClaimantsStudentIndicator();
            if (ClaimantsStudentIndicator != null) {
                if (ClaimantsStudentIndicator.equalsIgnoreCase("Y")) {
                    TotalCount_CTBStudentsClaimants++;
                }
            }
            String LHARegulationsApplied;
            LHARegulationsApplied = D_Record.getLHARegulationsApplied();
            if (LHARegulationsApplied != null) {
                if (LHARegulationsApplied.equalsIgnoreCase("1")) {
                    TotalCount_CTBLHACases++;
                }
            }
            if (WeeklyHousingBenefitEntitlement > 0) {
                CTBTotalWeeklyHBEntitlement += WeeklyHousingBenefitEntitlement;
                TotalCount_CTBWeeklyHBEntitlementNonZero++;
            } else {
                CTBTotalWeeklyHBEntitlementZeroCount++;
            }
            WeeklyCouncilTaxBenefitBenefitEntitlement = D_Record.getWeeklyCouncilTaxBenefitEntitlement();
            if (WeeklyCouncilTaxBenefitBenefitEntitlement > 0) {
                CTBTotalWeeklyCTBEntitlement += WeeklyCouncilTaxBenefitBenefitEntitlement;
                TotalCount_CTBWeeklyCTBEntitlementNonZero++;
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
                TotalCount_CTBWeeklyEligibleCouncilTaxAmountNonZero++;
            } else {
                TotalCount_CTBWeeklyEligibleCouncilTaxAmountZero++;
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
                    D_Record.getStatusOfCTBClaimAtExtractDate(),
                    EG,
                    TT,
                    Record.isClaimPostcodeFValidFormat(),
                    Record.isClaimPostcodeFMappable());
        }
//        AllCount1 = HBCount1 + CTBCount1;
    }

    protected void doCompare2TimesHBCount(
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
                TotalCount_HBPostcode0ValidPostcode1Valid++;
                if (PostcodeID0.equals(PostcodeID1)) {
                    TotalCount_HBPostcode0ValidPostcode1ValidPostcodeNotChanged++;
                } else {
                    TotalCount_HBPostcode0ValidPostcode1ValidPostcodeChanged++;
                }
            } else if (PostcodeID1 == null) {
                TotalCount_HBPostcode0ValidPostcode1DNE++;
            } else {
                TotalCount_HBPostcode0ValidPostcode1NotValid++;
            }
        } else if (ClaimPostcodeFMappable1) {
            if (PostcodeID0 == null) {
                TotalCount_HBPostcode0DNEPostcode1Valid++;
            } else {
                TotalCount_HBPostcode0NotValidPostcode1Valid++;
            }
        } else if (PostcodeID0 == null) {
            if (PostcodeID1 == null) {
                TotalCount_HBPostcode0DNEPostcode1DNE++;
            } else {
                TotalCount_HBPostcode0DNEPostcode1NotValid++;
            }
        } else if (PostcodeID1 == null) {
            TotalCount_HBPostcode0NotValidPostcode1DNE++;
        } else {
            TotalCount_HBPostcode0NotValidPostcode1NotValid++;
            if (PostcodeID0.equals(PostcodeID1)) {
                TotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeNotChanged++;
            } else {
                TotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeChanged++;
            }
        }
        if (TT0.compareTo(TT1) != 0) {
            if (!(TT0 == SHBE_TenancyType_Handler.iMinus999
                    || TT1 == SHBE_TenancyType_Handler.iMinus999)) {
                TotalCount_HBTTChangeClaimant++;
                //TotalCount_HBTTChangeClaimantIgnoreMinus999++;
            }
            if (TT0 == SHBE_TenancyType_Handler.iMinus999) {
                if (TT1 == 1 || TT1 == 4) {
                    TotalCount_Minus999TTToSocialTTs++;
                }
                if (TT1 == 3 || TT1 == 6) {
                    TotalCount_Minus999TTToPrivateDeregulatedTTs++;
                }
                if (TT1 == 1
                        || TT1 == 2
                        || TT1 == 3
                        || TT1 == 4
                        || TT1 == 6
                        || TT1 == 8
                        || TT1 == 9) {
                    TotalCount_HBTTsToMinus999TT++;
                }
            }
            if (TT1 == SHBE_TenancyType_Handler.iMinus999) {
                if (TT0 == 1 || TT0 == 4) {
                    TotalCount_SocialTTsToMinus999TT++;
                }
                if (TT0 == 3 || TT0 == 6) {
                    TotalCount_PrivateDeregulatedTTsToMinus999TT++;
                }
            }
            if ((TT0 == 5
                    || TT0 == 7)
                    && (TT1 == 1
                    || TT1 == 2
                    || TT1 == 3
                    || TT1 == 4
                    || TT1 == 6
                    || TT1 == 8
                    || TT1 == 9)) {
                TotalCount_CTBTTsToHBTTs++;
            }
            if ((TT0 == 1
                    || TT0 == 2
                    || TT0 == 3
                    || TT0 == 4
                    || TT0 == 6
                    || TT0 == 8
                    || TT0 == 9)
                    && (TT1 == 1
                    || TT1 == 2
                    || TT1 == 3
                    || TT1 == 4
                    || TT1 == 6
                    || TT1 == 8
                    || TT1 == 9)) {
                TotalCount_HBTTsToHBTTs++;
            }
            if (TT0 == 1 || TT0 == 4) {
                if (TT1 == 3 || TT0 == 6) {
                    TotalCount_SocialTTsToPrivateDeregulatedTTs++;
                    if (TT0 == 1) {
                        TotalCount_TT1ToPrivateDeregulatedTTs++;
                    }
                    if (TT0 == 4) {
                        TotalCount_TT4ToPrivateDeregulatedTTs++;
                    }
                }
            }
            if (TT0 == 3 || TT0 == 6) {
                if (TT1 == 1 || TT1 == 4) {
                    TotalCount_PrivateDeregulatedTTsToSocialTTs++;
                    if (TT1 == 1) {
                        TotalCount_PrivateDeregulatedTTsToTT1++;
                    }
                    if (TT1 == 4) {
                        TotalCount_PrivateDeregulatedTTsToTT4++;
                    }
                }
            }
        }
        if ((TT0 == 1 && TT1 == 1)
                || (TT0 == 1 && TT1 == 4)
                || (TT0 == 4 && TT1 == 1)
                || (TT0 == 4 && TT1 == 4)) {
            if (ClaimPostcodeFMappable0 && ClaimPostcodeFMappable1) {
                if (!PostcodeID0.equals(PostcodeID1)) {
                    TotalCount_PostcodeChangeWithinSocialTTs++;
                    if (TT0 == 1 && TT1 == 1) {
                        TotalCount_PostcodeChangeWithinTT1++;
                    }
                    if (TT0 == 4 && TT1 == 4) {
                        TotalCount_PostcodeChangeWithinTT4++;
                    }
                }
            }
        }
        if ((TT0 == 3 && TT1 == 3)
                || (TT0 == 3 && TT1 == 6)
                || (TT0 == 6 && TT1 == 3)
                || (TT0 == 6 && TT1 == 6)) {
            if (ClaimPostcodeFMappable0 && ClaimPostcodeFMappable1) {
                if (!PostcodeID0.equals(PostcodeID1)) {
                    TotalCount_PostcodeChangeWithinPrivateDeregulatedTTs++;
                }
            }
        }
        if ((TT0 == 5 || TT0 == 7)
                && (TT1 == 3 || TT1 == 6)) {
            TotalCount_CTBTTsToPrivateDeregulatedTTs++;
        }
        if (TT0 == 1 && TT1 == 4) {
            TotalCount_TT1ToTT4++;
        }
        if (TT0 == 4 && TT1 == 1) {
            TotalCount_TT4ToTT1++;
        }
        if (TT0 == 5 || TT0 == 7) {
            if (TT1 == 1 || TT1 == 4) {
                TotalCount_CTBTTsToSocialTTs++;
                if (TT1 == 1) {
                    TotalCount_CTBTTsToTT1++;
                }
                if (TT1 == 4) {
                    TotalCount_CTBTTsToTT4++;
                }
            }
        }
    }

    /**
     *
     * @param StatusOfHBClaimAtExtractDate
     * @param EG The Ethnic Group
     * @param TT
     * @param isValidClaimantPostcodeFormat
     * @param isValidClaimantPostcode
     */
    protected void doSingleTimeHBCount(
            int StatusOfHBClaimAtExtractDate,
            int EG,
            int TT,
            boolean isValidClaimantPostcodeFormat,
            boolean isValidClaimantPostcode) {
        HBCount1++;
        switch (StatusOfHBClaimAtExtractDate) {
            case 1:
                HBPTICount1++;
                break;
            case 2:
                HBPTSCount1++;
                break;
            default:
                HBPTOCount1++;
                break;
        }
        HBEthnicGroupCount[EG]++;
        TotalCount_AllEthnicGroupClaimantTT[EG][TT]++;
        if (isValidClaimantPostcodeFormat) {
            TotalCount_HBPostcodeValidFormat++;
        } else {
            TotalCount_HBPostcodeInvalidFormat++;
        }
        if (isValidClaimantPostcode) {
            TotalCount_HBPostcodeValid++;
        } else {
            TotalCount_HBPostcodeInvalid++;
        }
    }

    protected void doCompare2TimesCTBCount(
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
                TotalCount_CTBPostcode0ValidPostcode1Valid++;
                if (PostcodeID0.equals(PostcodeID1)) {
                    TotalCount_CTBPostcode0ValidPostcode1ValidPostcodeNotChanged++;
                } else {
                    TotalCount_CTBPostcode0ValidPostcode1ValidPostcodeChanged++;
                }
            } else if (PostcodeID1 == null) {
                TotalCount_CTBPostcode0ValidPostcode1DNE++;
            } else {
                TotalCount_CTBPostcode0ValidPostcode1NotValid++;
            }
        } else if (ClaimPostcodeFMappable1) {
            if (PostcodeID0 == null) {
                TotalCount_CTBPostcode0DNEPostcode1Valid++;
            } else {
                TotalCount_HBPostcode0NotValidPostcode1Valid++;
            }
        } else if (PostcodeID0 == null) {
            if (PostcodeID1 == null) {
                TotalCount_CTBPostcode0DNEPostcode1DNE++;
            } else {
                TotalCount_CTBPostcode0DNEPostcode1NotValid++;
            }
        } else if (PostcodeID1 == null) {
            TotalCount_CTBPostcode0NotValidPostcode1DNE++;
        } else {
            TotalCount_CTBPostcode0NotValidPostcode1NotValid++;
            if (PostcodeID0.equals(PostcodeID1)) {
                TotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeNotChanged++;
            } else {
                TotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeChanged++;
            }
        }
        if (TT0.compareTo(TT1) != 0) {
            if (!(TT0 == SHBE_TenancyType_Handler.iMinus999
                    || TT1 == SHBE_TenancyType_Handler.iMinus999)) {
                TotalCount_CTBTTChangeClaimant++;
                //TotalCount_CTBTTChangeClaimantIgnoreMinus999++;
            }
            if (TT1 == SHBE_TenancyType_Handler.iMinus999) {
                if (TT0 == 5 || TT0 == 7) {
                    TotalCount_CTBTTsToMinus999TT++;
                }
            }
            if (TT0 == SHBE_TenancyType_Handler.iMinus999) {
                TotalCount_Minus999TTToCTBTTs++;
            }
            if ((TT0 == 1
                    || TT0 == 2
                    || TT0 == 3
                    || TT0 == 4
                    || TT0 == 6
                    || TT0 == 8
                    || TT0 == 9)
                    && (TT1 == 5 || TT1 == 7)) {
                TotalCount_HBTTsToCTBTTs++;
            }
            if ((TT0 == 1 || TT0 == 4)
                    && (TT1 == 5 || TT1 == 7)) {
                TotalCount_SocialTTsToCTBTTs++;
            }
            if ((TT0 == 1)
                    && (TT1 == 5 || TT1 == 7)) {
                TotalCount_TT1ToCTBTTs++;
            }
            if ((TT0 == 4)
                    && (TT1 == 5 || TT1 == 7)) {
                TotalCount_TT4ToCTBTTs++;
            }
            if ((TT0 == 3 || TT0 == 6)
                    && (TT1 == 5 || TT1 == 7)) {
                TotalCount_PrivateDeregulatedTTsToCTBTTs++;
            }
        }
    }

    /**
     *
     * @param StatusOfCTBClaimAtExtractDate
     * @param EG The Ethnic Group
     * @param TT
     * @param isValidClaimantPostcodeFormat
     * @param isValidClaimantPostcode
     */
    protected void doSingleTimeCTBCount(
            int StatusOfCTBClaimAtExtractDate,
            int EG,
            int TT,
            boolean isValidClaimantPostcodeFormat,
            boolean isValidClaimantPostcode) {
        CTBCount1++;
        switch (StatusOfCTBClaimAtExtractDate) {
            case 1:
                CTBPTICount1++;
                break;
            case 2:
                CTBPTSCount1++;
                break;
            default:
                CTBPTOCount1++;
                break;
        }
        CTBEthnicGroupCount[EG]++;
        TotalCount_AllEthnicGroupClaimantTT[EG][TT]++;
        if (isValidClaimantPostcodeFormat) {
            TotalCount_CTBPostcodeValidFormat++;
        } else {
            TotalCount_CTBPostcodeInvalidFormat++;
        }
        if (isValidClaimantPostcode) {
            TotalCount_CTBPostcodeValid++;
        } else {
            TotalCount_CTBPostcodeInvalid++;
        }
    }

    /**
     *
     * @param SHBEFilenames
     * @param include
     * @param forceNewSummaries
     * @param HB_CTB
     * @param PTs Payment Types
     * @param nTT
     * @param nEG
     * @param nPSI
     * @param hoome If true there should be an attempt to handle
     * OutOfMemoryErrors if they are encountered.
     * @return
     */
    public TreeMap<String, HashMap<String, String>> getSummaryTable(
            String[] SHBEFilenames, ArrayList<Integer> include,
            boolean forceNewSummaries, ArrayList<String> HB_CTB,
            ArrayList<String> PTs, int nTT, int nEG, int nPSI, boolean hoome
    ) {
        String methodName = "getSummaryTable(...)";
        Env.log("<" + methodName + ">");

        // Declare variables
        // Declare result
        TreeMap<String, HashMap<String, String>> r;
        int i;
        Iterator<Integer> includeIte;
        HashMap<String, String> summary;
        String key;
        String filename0;
        String filename1;
        ONSPD_YM3 YM30;
        ONSPD_YM3 YM31;
        SHBE_Records SHBE_Records0;
        SHBE_Records SHBE_Records1;
        HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateInPayment0;
        HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateSuspended0;
        HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateOther0;
        HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateInPayment0;
        HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateSuspended0;
        HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateOther0;
        HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateInPayment1;
        HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateSuspended1;
        HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateOther1;
        HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateInPayment1;
        HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateSuspended1;
        HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateOther1;

        // Initialise counts
        initCounts(nTT, nEG, nPSI);

        // Initialise result
        r = new TreeMap<>();
        includeIte = include.iterator();
        while (includeIte.hasNext()) {
            i = includeIte.next();
            summary = new HashMap<>();
            key = SHBE_Handler.getYearMonthNumber(SHBEFilenames[i]);
            r.put(key, summary);
        }

        // Load first data
        includeIte = include.iterator();
        i = includeIte.next();
        filename1 = SHBEFilenames[i];
        //key = SHBE_Handler.getYearMonthNumber(filename1);
        YM31 = SHBE_Handler.getYM3(filename1);
        Env.log("Load " + YM31);

        SHBE_Records1 = SHBE_Data.getRecords(YM31, Env.HOOME);

        ClaimIDsWithStatusOfHBAtExtractDateInPayment1 = SHBE_Records1.getClaimIDsWithStatusOfHBAtExtractDateInPayment(Env.HOOME);
        ClaimIDsWithStatusOfHBAtExtractDateSuspended1 = SHBE_Records1.getClaimIDsWithStatusOfHBAtExtractDateSuspended(Env.HOOME);
        ClaimIDsWithStatusOfHBAtExtractDateOther1 = SHBE_Records1.getClaimIDsWithStatusOfHBAtExtractDateOther(Env.HOOME);
        ClaimIDsWithStatusOfCTBAtExtractDateInPayment1 = SHBE_Records1.getClaimIDsWithStatusOfCTBAtExtractDateInPayment(Env.HOOME);
        ClaimIDsWithStatusOfCTBAtExtractDateSuspended1 = SHBE_Records1.getClaimIDsWithStatusOfCTBAtExtractDateSuspended(Env.HOOME);
        ClaimIDsWithStatusOfCTBAtExtractDateOther1 = SHBE_Records1.getClaimIDsWithStatusOfCTBAtExtractDateOther(Env.HOOME);

        // Summarise first data
        doPartSummarySingleTime(SHBE_Records1,
                SHBE_Records1.getRecords(hoome), YM31,
                filename1, forceNewSummaries, HB_CTB, PTs, nTT, nEG, nPSI, r);

        filename0 = filename1;
        SHBE_Records0 = SHBE_Records1;
        ClaimIDsWithStatusOfHBAtExtractDateInPayment0 = ClaimIDsWithStatusOfHBAtExtractDateInPayment1;
        ClaimIDsWithStatusOfHBAtExtractDateSuspended0 = ClaimIDsWithStatusOfHBAtExtractDateSuspended1;
        ClaimIDsWithStatusOfHBAtExtractDateOther0 = ClaimIDsWithStatusOfHBAtExtractDateOther1;
        ClaimIDsWithStatusOfCTBAtExtractDateInPayment0 = ClaimIDsWithStatusOfCTBAtExtractDateInPayment1;
        ClaimIDsWithStatusOfCTBAtExtractDateSuspended0 = ClaimIDsWithStatusOfCTBAtExtractDateSuspended1;
        ClaimIDsWithStatusOfCTBAtExtractDateOther0 = ClaimIDsWithStatusOfCTBAtExtractDateOther1;
        //YM30 = YM31;
        YM30 = new ONSPD_YM3(YM31);
        incrementCounts(nTT);
        initCounts(nTT, nEG, nPSI);

        while (includeIte.hasNext()) {
            i = includeIte.next();
            filename1 = SHBEFilenames[i];
            //key = SHBE_Handler.getYearMonthNumber(filename1);
            YM31 = SHBE_Handler.getYM3(filename1);
            // Load next data
            Env.log("Load " + YM31);
            SHBE_Records1 = SHBE_Data.getRecords(YM31, Env.HOOME);
            ClaimIDsWithStatusOfHBAtExtractDateInPayment1 = SHBE_Records1.getClaimIDsWithStatusOfHBAtExtractDateInPayment(Env.HOOME);
            ClaimIDsWithStatusOfHBAtExtractDateSuspended1 = SHBE_Records1.getClaimIDsWithStatusOfHBAtExtractDateSuspended(Env.HOOME);
            ClaimIDsWithStatusOfHBAtExtractDateOther1 = SHBE_Records1.getClaimIDsWithStatusOfHBAtExtractDateOther(Env.HOOME);
            ClaimIDsWithStatusOfCTBAtExtractDateInPayment1 = SHBE_Records1.getClaimIDsWithStatusOfCTBAtExtractDateInPayment(Env.HOOME);
            ClaimIDsWithStatusOfCTBAtExtractDateSuspended1 = SHBE_Records1.getClaimIDsWithStatusOfCTBAtExtractDateSuspended(Env.HOOME);
            ClaimIDsWithStatusOfCTBAtExtractDateOther1 = SHBE_Records1.getClaimIDsWithStatusOfCTBAtExtractDateOther(Env.HOOME);
            // doPartSummaryCompare2Times
            doPartSummaryCompare2Times(SHBE_Records0,
                    ClaimIDsWithStatusOfHBAtExtractDateInPayment0,
                    ClaimIDsWithStatusOfHBAtExtractDateSuspended0,
                    ClaimIDsWithStatusOfHBAtExtractDateOther0,
                    ClaimIDsWithStatusOfCTBAtExtractDateInPayment0,
                    ClaimIDsWithStatusOfCTBAtExtractDateSuspended0,
                    ClaimIDsWithStatusOfCTBAtExtractDateOther0,
                    YM30, filename0, SHBE_Records1,
                    ClaimIDsWithStatusOfHBAtExtractDateInPayment1,
                    ClaimIDsWithStatusOfHBAtExtractDateSuspended1,
                    ClaimIDsWithStatusOfHBAtExtractDateOther1,
                    ClaimIDsWithStatusOfCTBAtExtractDateInPayment1,
                    ClaimIDsWithStatusOfCTBAtExtractDateSuspended1,
                    ClaimIDsWithStatusOfCTBAtExtractDateOther1, YM31, filename1,
                    forceNewSummaries, HB_CTB, PTs, nTT, nEG, nPSI, r);
            // Set up vars for next iteration
            if (includeIte.hasNext()) {
                filename0 = filename1;
                SHBE_Records0 = SHBE_Records1;
                ClaimIDsWithStatusOfHBAtExtractDateInPayment0 = ClaimIDsWithStatusOfHBAtExtractDateInPayment1;
                ClaimIDsWithStatusOfHBAtExtractDateSuspended0 = ClaimIDsWithStatusOfHBAtExtractDateSuspended1;
                ClaimIDsWithStatusOfHBAtExtractDateOther0 = ClaimIDsWithStatusOfHBAtExtractDateOther1;
                ClaimIDsWithStatusOfCTBAtExtractDateInPayment0 = ClaimIDsWithStatusOfCTBAtExtractDateInPayment1;
                ClaimIDsWithStatusOfCTBAtExtractDateSuspended0 = ClaimIDsWithStatusOfCTBAtExtractDateSuspended1;
                ClaimIDsWithStatusOfCTBAtExtractDateOther0 = ClaimIDsWithStatusOfCTBAtExtractDateOther1;
                //YM30 = YM31;
                YM30 = new ONSPD_YM3(YM31);
                incrementCounts(nTT);
                initCounts(nTT, nEG, nPSI);
                // Not used at present. incomeAndRentSummary0 = incomeAndRentSummary1;
            }
        }
        Env.log("</" + methodName + ">");
        return r;
    }

    protected void incrementCounts(int nTT) {
        System.arraycopy(TotalCount_TTClaimant1, 0, TotalCount_TTClaimant0, 0, nTT);
        AllCount0 = AllCount1;
        HBCount0 = HBCount1;
        HBPTICount0 = HBPTICount1;
        HBPTSCount0 = HBPTSCount1;
        HBPTOCount0 = HBPTOCount1;
        CTBCount0 = CTBCount1;
        CTBPTICount0 = CTBPTICount1;
        CTBPTSCount0 = CTBPTSCount1;
        CTBPTOCount0 = CTBPTOCount1;
    }

    protected void doPartSummarySingleTime(
            SHBE_Records SHBE_Records,
            HashMap<SHBE_ID, SHBE_Record> Records,
            ONSPD_YM3 YM3,
            String filename,
            boolean forceNewSummaries,
            ArrayList<String> HB_CTB,
            ArrayList<String> PTs,
            int nTT,
            int nEG,
            int nPSI,
            TreeMap<String, HashMap<String, String>> summaries
    ) {
        // Declare variables
        String key;
        HashMap<String, String> summary;
        HashMap<String, Number> LoadSummary;
        HashMap<String, BigDecimal> IncomeAndRentSummary;
        HashSet<SHBE_ID> ClaimIDsOfNewSHBEClaims;

        // Initialise variables
        key = SHBE_Handler.getYearMonthNumber(filename);
        summary = summaries.get(key);
        LoadSummary = SHBE_Records.getLoadSummary(Env.HOOME);
        DW_IncomeAndRentSummary tIncomeAndRentSummary;
        tIncomeAndRentSummary = new DW_IncomeAndRentSummary(Env);
        IncomeAndRentSummary = tIncomeAndRentSummary.getIncomeAndRentSummary(
                SHBE_Records, HB_CTB, PTs, YM3, null, null, false, false,
                false, forceNewSummaries);
        ClaimIDsOfNewSHBEClaims = SHBE_Records.getClaimIDsOfNewSHBEClaims(Env.HOOME);
        // Add load summary to summary
        addToSummary(summary, LoadSummary);

        /**
         * Add things not quite added right from load summary
         */
        HashSet<SHBE_ID> ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore;
        ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore = SHBE_Records.getClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore(Env.HOOME);
        summary.put(Strings.SHBE_Strings.sCountOfNewSHBEClaimsWhereClaimantWasPartnerBefore,
                Integer.toString(ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore.size()));
        HashSet<SHBE_PersonID> set;
        // Add unique Partners
        set = SHBE_Handler.getUniquePersonIDs0(
                SHBE_Records.getClaimIDToPartnerPersonIDLookup(Env.HOOME));
        summary.put(Strings.SHBE_Strings.sCountOfUniquePartners, "" + set.size());
        // Add unique Dependents
        set = SHBE_Handler.getUniquePersonIDs(
                SHBE_Records.getClaimIDToDependentPersonIDsLookup(Env.HOOME));
        summary.put(Strings.SHBE_Strings.sCountOfUniqueDependents, "" + set.size());
        // Add unique NonDependents
        set = SHBE_Handler.getUniquePersonIDs(
                SHBE_Records.getClaimIDToNonDependentPersonIDsLookup(Env.HOOME));
        summary.put(Strings.SHBE_Strings.sCountOfUniqueNonDependents, "" + set.size());
        /**
         * Counts of: ClaimsWithClaimantsThatAreClaimantsInAnotherClaim
         * ClaimsWithClaimantsThatArePartnersInAnotherClaim
         * ClaimsWithPartnersThatAreClaimantsInAnotherClaim
         * ClaimsWithPartnersThatArePartnersInAnotherClaim
         * ClaimantsInMultipleClaimsInAMonth PartnersInMultipleClaimsInAMonth
         * NonDependentsInMultipleClaimsInAMonth
         */
        summary.put(Strings.SHBE_Strings.sCountOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim,
                "" + SHBE_Records.getClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim(Env.HOOME).size());
        summary.put(Strings.SHBE_Strings.sCountOfClaimsWithClaimantsThatArePartnersInAnotherClaim,
                "" + SHBE_Records.getClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim(Env.HOOME).size());
        summary.put(Strings.SHBE_Strings.sCountOfClaimsWithPartnersThatAreClaimantsInAnotherClaim,
                "" + SHBE_Records.getClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim(Env.HOOME).size());
        summary.put(Strings.SHBE_Strings.sCountOfClaimsWithPartnersThatArePartnersInAnotherClaim,
                "" + SHBE_Records.getClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim(Env.HOOME).size());
        summary.put(Strings.SHBE_Strings.sCountOfClaimantsInMultipleClaimsInAMonth,
                "" + SHBE_Records.getClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup(Env.HOOME).size());
        summary.put(Strings.SHBE_Strings.sCountOfPartnersInMultipleClaimsInAMonth,
                "" + SHBE_Records.getPartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup(Env.HOOME).size());
        summary.put(Strings.SHBE_Strings.sCountOfNonDependentsInMultipleClaimsInAMonth,
                "" + SHBE_Records.getNonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup(Env.HOOME).size());

        addToSummary(summary, ClaimIDsOfNewSHBEClaims, Records);
        // doSingleTimeLoopOverSet
        doSingleTimeLoopOverSet(Records);
//        AllCount1 = HBCount1 + CTBCount1;
        summary.put(sSHBEFilename1, filename);
        addToSummarySingleTimeIncomeAndRent(
                summary,
                IncomeAndRentSummary);
        addToSummarySingleTime(nTT, nEG, nPSI, summary);
    }

    protected void doPartSummaryCompare2Times(
            SHBE_Records SHBE_Records0,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateInPayment0,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateSuspended0,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateOther0,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateInPayment0,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateSuspended0,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateOther0,
            ONSPD_YM3 YM30,
            String filename0,
            SHBE_Records SHBE_Records1,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateInPayment1,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateSuspended1,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateOther1,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateInPayment1,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateSuspended1,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateOther1,
            ONSPD_YM3 YM31,
            String filename1,
            boolean forceNewSummaries,
            ArrayList<String> HB_CTB,
            ArrayList<String> PTs,
            //String PT,
            int nTT,
            int nEG,
            int nPSI,
            TreeMap<String, HashMap<String, String>> summaries) {

        doPartSummarySingleTime(
                SHBE_Records1,
                SHBE_Records1.getRecords(Env.HOOME),
                YM31,
                filename1,
                forceNewSummaries,
                HB_CTB,
                PTs,
                nTT,
                nEG,
                nPSI,
                summaries);

        doCompare2TimesLoopOverSet(
                SHBE_Records0,
                ClaimIDsWithStatusOfHBAtExtractDateInPayment0,
                ClaimIDsWithStatusOfHBAtExtractDateSuspended0,
                ClaimIDsWithStatusOfHBAtExtractDateOther0,
                ClaimIDsWithStatusOfCTBAtExtractDateInPayment0,
                ClaimIDsWithStatusOfCTBAtExtractDateSuspended0,
                ClaimIDsWithStatusOfCTBAtExtractDateOther0,
                SHBE_Records1,
                ClaimIDsWithStatusOfHBAtExtractDateInPayment1,
                ClaimIDsWithStatusOfHBAtExtractDateSuspended1,
                ClaimIDsWithStatusOfHBAtExtractDateOther1,
                ClaimIDsWithStatusOfCTBAtExtractDateInPayment1,
                ClaimIDsWithStatusOfCTBAtExtractDateSuspended1,
                ClaimIDsWithStatusOfCTBAtExtractDateOther1);

        String key;
        key = SHBE_Handler.getYearMonthNumber(filename1);
        HashMap<String, String> summary;
        summary = summaries.get(key);

        addToSummaryCompare2Times(nTT, nEG, nPSI, summary);

        // All
        summary.put(sSHBEFilename0, filename0);
        summary.put(sSHBEFilename1, filename1);
    }

    protected void doPartSummaryCompare2Times(
            SHBE_Records SHBE_Records0,
            HashMap<SHBE_ID, SHBE_Record> Records0,
            ONSPD_YM3 YM30,
            String filename0,
            SHBE_Records SHBE_Records1,
            HashMap<SHBE_ID, SHBE_Record> Records1,
            ONSPD_YM3 YM31,
            String filename1,
            boolean forceNewSummaries,
            ArrayList<String> HB_CTB,
            ArrayList<String> PTs,
            int nTT,
            int nEG,
            int nPSI,
            TreeMap<String, HashMap<String, String>> summaries) {

        doPartSummarySingleTime(
                SHBE_Records1,
                Records1,
                YM31,
                filename1,
                forceNewSummaries,
                HB_CTB,
                PTs,
                nTT,
                nEG,
                nPSI,
                summaries);

        doCompare2TimesLoopOverSet(
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
    }

    /**
     *
     * @param SHBE_Records0
     * @param ClaimIDsWithStatusOfHBAtExtractDateInPayment0
     * @param ClaimIDsWithStatusOfHBAtExtractDateSuspended0
     * @param ClaimIDsWithStatusOfHBAtExtractDateOther0
     * @param ClaimIDsWithStatusOfCTBAtExtractDateInPayment0
     * @param ClaimIDsWithStatusOfCTBAtExtractDateSuspended0
     * @param ClaimIDsWithStatusOfCTBAtExtractDateOther0
     * @param SHBE_Records1
     * @param ClaimIDsWithStatusOfHBAtExtractDateInPayment1
     * @param ClaimIDsWithStatusOfHBAtExtractDateSuspended1
     * @param ClaimIDsWithStatusOfHBAtExtractDateOther1
     * @param ClaimIDsWithStatusOfCTBAtExtractDateInPayment1
     * @param ClaimIDsWithStatusOfCTBAtExtractDateSuspended1
     * @param ClaimIDsWithStatusOfCTBAtExtractDateOther1
     */
    public void doCompare2TimesLoopOverSet(
            SHBE_Records SHBE_Records0,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateInPayment0,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateSuspended0,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateOther0,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateInPayment0,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateSuspended0,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateOther0,
            SHBE_Records SHBE_Records1,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateInPayment1,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateSuspended1,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfHBAtExtractDateOther1,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateInPayment1,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateSuspended1,
            HashSet<SHBE_ID> ClaimIDsWithStatusOfCTBAtExtractDateOther1) {
        Iterator<SHBE_ID> ite;
        SHBE_ID ClaimID;
        SHBE_Record Record0;
        SHBE_D_Record D_Record0;
        SHBE_Record Record1;
        SHBE_D_Record D_Record1;

        HashMap<SHBE_ID, SHBE_Record> Records0;
        Records0 = SHBE_Records0.getRecords(Env.HOOME);
        // Go through previous records
        ite = Records0.keySet().iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            // HB
            if (ClaimIDsWithStatusOfHBAtExtractDateInPayment0.contains(ClaimID)) {
                if (ClaimIDsWithStatusOfHBAtExtractDateInPayment1.contains(ClaimID)) {
                    TotalCount_HB_PTIToPTI++;
                    TotalCount_HB_NotSuspendedToNotSuspended++;
                } else if (ClaimIDsWithStatusOfHBAtExtractDateSuspended1.contains(ClaimID)) {
                    TotalCount_HB_PTIToPTS++;
                    TotalCount_HB_NotSuspendedToSuspended++;
                } else if (ClaimIDsWithStatusOfHBAtExtractDateOther1.contains(ClaimID)) {
                    TotalCount_HB_PTIToPTO++;
                    TotalCount_HB_NotSuspendedToNotSuspended++;
                } else {
                    TotalCount_HB_PTIToNull++;
                    TotalCount_HB_NotSuspendedToNull++;
                }
            } else if (ClaimIDsWithStatusOfHBAtExtractDateSuspended0.contains(ClaimID)) {
                if (ClaimIDsWithStatusOfHBAtExtractDateInPayment1.contains(ClaimID)) {
                    TotalCount_HB_PTSToPTI++;
                    TotalCount_HB_SuspendedToNotSuspended++;
                } else if (ClaimIDsWithStatusOfHBAtExtractDateSuspended1.contains(ClaimID)) {
                    TotalCount_HB_PTSToPTS++;
                    TotalCount_HB_SuspendedToSuspended++;
                } else if (ClaimIDsWithStatusOfHBAtExtractDateOther1.contains(ClaimID)) {
                    TotalCount_HB_PTSToPTO++;
                    TotalCount_HB_SuspendedToNotSuspended++;
                } else {
                    TotalCount_HB_PTSToNull++;
                    TotalCount_HB_SuspendedToNull++;
                }
            } else if (ClaimIDsWithStatusOfHBAtExtractDateOther0.contains(ClaimID)) {
                if (ClaimIDsWithStatusOfHBAtExtractDateInPayment1.contains(ClaimID)) {
                    TotalCount_HB_PTOToPTI++;
                    TotalCount_HB_NotSuspendedToNotSuspended++;
                } else if (ClaimIDsWithStatusOfHBAtExtractDateSuspended1.contains(ClaimID)) {
                    TotalCount_HB_PTOToPTS++;
                    TotalCount_HB_NotSuspendedToSuspended++;
                } else if (ClaimIDsWithStatusOfHBAtExtractDateOther1.contains(ClaimID)) {
                    TotalCount_HB_PTOToPTO++;
                    TotalCount_HB_NotSuspendedToNotSuspended++;
                } else {
                    TotalCount_HB_PTOToNull++;
                    TotalCount_HB_NotSuspendedToNull++;
                }
            }
            // CTB
            if (ClaimIDsWithStatusOfCTBAtExtractDateInPayment0.contains(ClaimID)) {
                if (ClaimIDsWithStatusOfCTBAtExtractDateInPayment1.contains(ClaimID)) {
                    TotalCount_CTB_PTIToPTI++;
                    TotalCount_CTB_NotSuspendedToNotSuspended++;
                } else if (ClaimIDsWithStatusOfCTBAtExtractDateSuspended1.contains(ClaimID)) {
                    TotalCount_CTB_PTIToPTS++;
                    TotalCount_CTB_NotSuspendedToSuspended++;
                } else if (ClaimIDsWithStatusOfCTBAtExtractDateOther1.contains(ClaimID)) {
                    TotalCount_CTB_PTIToPTO++;
                    TotalCount_CTB_NotSuspendedToNotSuspended++;
                } else {
                    TotalCount_CTB_PTIToNull++;
                    TotalCount_CTB_NotSuspendedToNull++;
                }
            } else if (ClaimIDsWithStatusOfCTBAtExtractDateSuspended0.contains(ClaimID)) {
                if (ClaimIDsWithStatusOfCTBAtExtractDateInPayment1.contains(ClaimID)) {
                    TotalCount_CTB_PTSToPTI++;
                    TotalCount_CTB_SuspendedToNotSuspended++;
                } else if (ClaimIDsWithStatusOfCTBAtExtractDateSuspended1.contains(ClaimID)) {
                    TotalCount_CTB_PTSToPTS++;
                    TotalCount_CTB_SuspendedToSuspended++;
                } else if (ClaimIDsWithStatusOfCTBAtExtractDateOther1.contains(ClaimID)) {
                    TotalCount_CTB_PTSToPTO++;
                    TotalCount_CTB_SuspendedToNotSuspended++;
                } else {
                    TotalCount_CTB_PTSToNull++;
                    TotalCount_CTB_SuspendedToNull++;
                }
            } else if (ClaimIDsWithStatusOfCTBAtExtractDateOther0.contains(ClaimID)) {
                if (ClaimIDsWithStatusOfCTBAtExtractDateInPayment1.contains(ClaimID)) {
                    TotalCount_CTB_PTOToPTI++;
                    TotalCount_CTB_NotSuspendedToNotSuspended++;
                } else if (ClaimIDsWithStatusOfCTBAtExtractDateSuspended1.contains(ClaimID)) {
                    TotalCount_CTB_PTOToPTS++;
                    TotalCount_CTB_NotSuspendedToSuspended++;
                } else if (ClaimIDsWithStatusOfCTBAtExtractDateOther1.contains(ClaimID)) {
                    TotalCount_CTB_PTOToPTO++;
                    TotalCount_CTB_NotSuspendedToNotSuspended++;
                } else {
                    TotalCount_CTB_PTOToNull++;
                    TotalCount_CTB_NotSuspendedToNull++;
                }
            }
        }
        HashMap<SHBE_ID, SHBE_Record> Records1;
        Records1 = SHBE_Records1.getRecords(Env.HOOME);
        // Go through current records
        ite = Records1.keySet().iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            Record1 = Records1.get(ClaimID);
            D_Record1 = Record1.getDRecord();
            // HB
            if (ClaimIDsWithStatusOfHBAtExtractDateInPayment0.contains(ClaimID)) {
                Record0 = Records0.get(ClaimID);
                D_Record0 = Record0.getDRecord();
                doCompare2TimesCounts(
                        Record0,
                        D_Record0,
                        Record1,
                        D_Record1);
            } else if (ClaimIDsWithStatusOfHBAtExtractDateSuspended0.contains(ClaimID)) {
                Record0 = Records0.get(ClaimID);
                D_Record0 = Record0.getDRecord();
                doCompare2TimesCounts(
                        Record0,
                        D_Record0,
                        Record1,
                        D_Record1);
            } else if (ClaimIDsWithStatusOfHBAtExtractDateOther0.contains(ClaimID)) {
                Record0 = Records0.get(ClaimID);
                D_Record0 = Record0.getDRecord();
                doCompare2TimesCounts(
                        Record0,
                        D_Record0,
                        Record1,
                        D_Record1);
            } else {
                doCompare2TimesCounts(
                        null,
                        null,
                        Record1,
                        D_Record1);
            }
            // CTB
            if (ClaimIDsWithStatusOfCTBAtExtractDateInPayment0.contains(ClaimID)) {
                Record0 = Records0.get(ClaimID);
                D_Record0 = Record0.getDRecord();
                doCompare2TimesCounts(
                        Record0,
                        D_Record0,
                        Record1,
                        D_Record1);
            } else if (ClaimIDsWithStatusOfCTBAtExtractDateSuspended0.contains(ClaimID)) {
                Record0 = Records0.get(ClaimID);
                D_Record0 = Record0.getDRecord();
                doCompare2TimesCounts(
                        Record0,
                        D_Record0,
                        Record1,
                        D_Record1);
            } else if (ClaimIDsWithStatusOfCTBAtExtractDateOther0.contains(ClaimID)) {
                Record0 = Records0.get(ClaimID);
                D_Record0 = Record0.getDRecord();
                doCompare2TimesCounts(
                        Record0,
                        D_Record0,
                        Record1,
                        D_Record1);
            } else {
                doCompare2TimesCounts(
                        null,
                        null,
                        Record1,
                        D_Record1);
            }
        }

    }

    public void doCompare2TimesLoopOverSet(
            HashMap<SHBE_ID, SHBE_Record> Records0,
            HashMap<SHBE_ID, SHBE_Record> Records1) {
        Iterator<SHBE_ID> ite;
        SHBE_ID ClaimID;
        SHBE_Record Record0;
        SHBE_D_Record D_Record0;
        SHBE_Record Record1;
        /**
         * Loop over Records0
         */
        ite = Records0.keySet().iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            Record0 = Records0.get(ClaimID);
            D_Record0 = null;
            if (Record0 != null) {
                D_Record0 = Record0.getDRecord();
            }
            Record1 = Records1.get(ClaimID);
            //SHBE_D_Record D_Record1;
            //D_Record1 = null;
            /**
             * doCompare2TimesCounts only for those Claims that are no longer in
             * this PT (they may no longer be Claims at all).
             */
            if (Record1 == null) {
                doCompare2TimesCounts(
                        Record0,
                        D_Record0,
                        null,//Record1,
                        null);//D_Record1);
            }
        }

        /**
         * Loop over Records1
         */
        ite = Records1.keySet().iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            Record0 = Records0.get(ClaimID);
            D_Record0 = null;
            if (Record0 != null) {
                D_Record0 = Record0.getDRecord();
            }
            Record1 = Records1.get(ClaimID);
            SHBE_D_Record D_Record1;
            D_Record1 = Record1.getDRecord();
            doCompare2TimesCounts(
                    Record0,
                    D_Record0,
                    Record1,
                    D_Record1);
        }
    }

    /**
     *
     * @param Records
     */
    public void doSingleTimeLoopOverSet(
            HashMap<SHBE_ID, SHBE_Record> Records) {
        Iterator<SHBE_ID> ite;
        ite = Records.keySet().iterator();
        while (ite.hasNext()) {
            SHBE_ID ClaimID = ite.next();
            SHBE_Record Record = Records.get(ClaimID);
            SHBE_D_Record D_Record = Record.getDRecord();
            doSingleTimeCount(Record, D_Record);
        }
    }

    /**
     * Adds LoadSummary to summary
     *
     * @param summary
     * @param LoadSummary
     */
    public void addToSummary(
            HashMap<String, String> summary,
            HashMap<String, Number> LoadSummary) {
        String key;
        Iterator<String> ite;
        ite = LoadSummary.keySet().iterator();
        while (ite.hasNext()) {
            key = ite.next();
            summary.put(key, LoadSummary.get(key).toString());
        }
        summary.put(sAllCount0, Integer.toString(AllCount1));
        summary.put(sHBCount0, Integer.toString(HBCount1));
        summary.put(sCTBCount0, Integer.toString(CTBCount1));
    }

    /**
     * Adds to summary counts by PSI for new claims.
     *
     * @param summary
     * @param ClaimIDsOfNewSHBEClaims
     * @param Records
     */
    public void addToSummary(
            HashMap<String, String> summary,
            HashSet<SHBE_ID> ClaimIDsOfNewSHBEClaims,
            HashMap<SHBE_ID, SHBE_Record> Records) {
        Iterator<SHBE_ID> ite;
        ite = ClaimIDsOfNewSHBEClaims.iterator();
        SHBE_ID ClaimID;
        SHBE_Record SHBE_Record;
        int PSI;
        int nPSI;
        nPSI = SHBE_Handler.getNumberOfPassportedStandardIndicators();
        int[] counts;
        counts = new int[nPSI];
        while (ite.hasNext()) {
            ClaimID = ite.next();
            SHBE_Record = Records.get(ClaimID);
            PSI = SHBE_Record.getDRecord().getPassportedStandardIndicator();
            counts[PSI] += 1;
        }
        for (int i = 1; i < nPSI; i++) {
            summary.put(Strings.SHBE_Strings.sCountOfNewSHBEClaimsPSI + i, "" + counts[i]);
            //System.out.println(Strings.SHBE_Strings.sCountOfNewSHBEClaimsPSI + "[" + i + "] " + counts[i]);
        }
    }

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
     * @param HB_CTB
     * @param PTs
     * @param includeKey
     * @param nTT
     * @param nEG
     * @param nPSI
     */
    public void writeSummaryTables(
            TreeMap<String, HashMap<String, String>> summaryTable,
            ArrayList<String> HB_CTB,
            ArrayList<String> PTs,
            String includeKey,
            int nTT,
            int nEG,
            int nPSI
    ) {
        Env.logO("<writeSummaryTables>", true);
        writeSummaryTableCompare2TimesPaymentTypes(
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
                HB_CTB,
                PTs,
                includeKey,
                nTT, nEG);
        writeSummaryTableSingleTimeTT(
                summaryTable,
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
        Env.logO("</writeSummaryTables>", true);
    }

    /**
     *
     * @param name
     * @param name2
     * @param summaryTable
     * @param includeKey
     * @param underOccupancy
     * @return
     */
    protected PrintWriter getPrintWriter(
            String name,
            String name2,
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            boolean underOccupancy) {
        PrintWriter result;
        File dirOut;
        dirOut = Env.getFiles().getOutputSHBETableDir(
                name2,
                includeKey,
                underOccupancy);
        String outFilename;
        outFilename = includeKey + Strings.symbol_underscore;
        if (underOccupancy) {
            outFilename += Strings.sU + Strings.symbol_underscore;
        }
        outFilename += summaryTable.firstKey() + "_To_" + summaryTable.lastKey() + Strings.symbol_underscore + name + ".csv";
        File outFile;
        outFile = new File(dirOut, outFilename);
        result = Generic_IO.getPrintWriter(outFile, false);
        return result;
    }

    /**
     * Provides comparisons with the previous period
     *
     * @param summaryTable
     * @param includeKey
     * @param nTT
     * @param nEG
     */
    public void writeSummaryTableCompare2TimesPaymentTypes(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            int nTT,
            int nEG
    ) {
        TreeMap<ONSPD_YM3, File> ONSPDFiles;
        ONSPDFiles = Files.getONSPD_Files().getInputONSPDFiles();
        String name;
        name = "Compare2TimesPaymentTypes";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType, 
                includeKey, false);
        // Write headers
        String header;
        header = getHeaderCompare2TimesGeneric();
        // HB
        header += sTotalCount_HB_NotSuspendedToNotSuspended + Strings.special_commaSpace;
        header += sTotalCount_HB_NotSuspendedToSuspended + Strings.special_commaSpace;
        header += sTotalCount_HB_NotSuspendedToNull + Strings.special_commaSpace;
        header += sTotalCount_HB_SuspendedToNotSuspended + Strings.special_commaSpace;
        header += sTotalCount_HB_SuspendedToSuspended + Strings.special_commaSpace;
        header += sTotalCount_HB_SuspendedToNull + Strings.special_commaSpace;
        header += sTotalCount_HB_PTIToPTI + Strings.special_commaSpace;
        header += sTotalCount_HB_PTIToPTS + Strings.special_commaSpace;
        header += sTotalCount_HB_PTIToPTO + Strings.special_commaSpace;
        header += sTotalCount_HB_PTIToNull + Strings.special_commaSpace;
        header += sTotalCount_HB_PTSToPTI + Strings.special_commaSpace;
        header += sTotalCount_HB_PTSToPTS + Strings.special_commaSpace;
        header += sTotalCount_HB_PTSToPTO + Strings.special_commaSpace;
        header += sTotalCount_HB_PTSToNull + Strings.special_commaSpace;
        header += sTotalCount_HB_PTOToPTI + Strings.special_commaSpace;
        header += sTotalCount_HB_PTOToPTS + Strings.special_commaSpace;
        header += sTotalCount_HB_PTOToPTO + Strings.special_commaSpace;
        header += sTotalCount_HB_PTOToNull + Strings.special_commaSpace;
        // CTB
        header += sTotalCount_CTB_NotSuspendedToNotSuspended + Strings.special_commaSpace;
        header += sTotalCount_CTB_NotSuspendedToSuspended + Strings.special_commaSpace;
        header += sTotalCount_CTB_NotSuspendedToNull + Strings.special_commaSpace;
        header += sTotalCount_CTB_SuspendedToNotSuspended + Strings.special_commaSpace;
        header += sTotalCount_CTB_SuspendedToSuspended + Strings.special_commaSpace;
        header += sTotalCount_CTB_SuspendedToNull + Strings.special_commaSpace;
        header += sTotalCount_CTB_PTIToPTI + Strings.special_commaSpace;
        header += sTotalCount_CTB_PTIToPTS + Strings.special_commaSpace;
        header += sTotalCount_CTB_PTIToPTO + Strings.special_commaSpace;
        header += sTotalCount_CTB_PTIToNull + Strings.special_commaSpace;
        header += sTotalCount_CTB_PTSToPTI + Strings.special_commaSpace;
        header += sTotalCount_CTB_PTSToPTS + Strings.special_commaSpace;
        header += sTotalCount_CTB_PTSToPTO + Strings.special_commaSpace;
        header += sTotalCount_CTB_PTSToNull + Strings.special_commaSpace;
        header += sTotalCount_CTB_PTOToPTI + Strings.special_commaSpace;
        header += sTotalCount_CTB_PTOToPTS + Strings.special_commaSpace;
        header += sTotalCount_CTB_PTOToPTO + Strings.special_commaSpace;
        header += sTotalCount_CTB_PTOToNull + Strings.special_commaSpace;
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
            // HB
            line += summary.get(sTotalCount_HB_NotSuspendedToNotSuspended) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HB_NotSuspendedToSuspended) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HB_NotSuspendedToNull) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HB_SuspendedToNotSuspended) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HB_SuspendedToSuspended) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HB_SuspendedToNull) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HB_PTIToPTI) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HB_PTIToPTS) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HB_PTIToPTO) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HB_PTIToNull) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HB_PTSToPTI) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HB_PTSToPTS) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HB_PTSToPTO) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HB_PTSToNull) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HB_PTOToPTI) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HB_PTOToPTS) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HB_PTOToPTO) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HB_PTOToNull) + Strings.special_commaSpace;
            // CTB
            line += summary.get(sTotalCount_CTB_NotSuspendedToNotSuspended) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTB_NotSuspendedToSuspended) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTB_NotSuspendedToNull) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTB_SuspendedToNotSuspended) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTB_SuspendedToSuspended) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTB_SuspendedToNull) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTB_PTIToPTI) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTB_PTIToPTS) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTB_PTIToPTO) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTB_PTIToNull) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTB_PTSToPTI) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTB_PTSToPTS) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTB_PTSToPTO) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTB_PTSToNull) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTB_PTOToPTI) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTB_PTOToPTS) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTB_PTOToPTO) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTB_PTOToNull) + Strings.special_commaSpace;
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    protected String getHeaderCompare2TimesPostcodeChange() {
        String header = "";
        // All Postcode Related
        header += sTotalCount_AllPostcode0ValidPostcode1Valid + Strings.special_commaSpace;
        header += sPercentageOfAllCount1_AllPostcode0ValidPostcode1Valid + Strings.special_commaSpace;
        header += sTotalCount_AllPostcode0ValidPostcode1ValidPostcodeNotChanged + Strings.special_commaSpace;
        header += sPercentageOfAllCount1_AllPostcode0ValidPostcode1ValidPostcodeNotChanged + Strings.special_commaSpace;
        header += sTotalCount_AllPostcode0ValidPostcode1ValidPostcodeChange + Strings.special_commaSpace;
        header += sPercentageOfAllCount1_AllPostcode0ValidPostcode1ValidPostcodeChange + Strings.special_commaSpace;
        header += sTotalCount_AllPostcode0ValidPostcode1NotValid + Strings.special_commaSpace;
        header += sPercentageOfAllCount1_AllPostcode0ValidPostcode1NotValid + Strings.special_commaSpace;
        header += sTotalCount_AllPostcode0NotValidPostcode1Valid + Strings.special_commaSpace;
        header += sPercentageOfAllCount1_AllPostcode0NotValidPostcode1Valid + Strings.special_commaSpace;
        header += sTotalCount_AllPostcode0NotValidPostcode1NotValid + Strings.special_commaSpace;
        header += sPercentageOfAllCount1_AllPostcode0NotValidPostcode1NotValid + Strings.special_commaSpace;
        header += sTotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeNotChanged + Strings.special_commaSpace;
        header += sPercentageOfAllCount1_AllPostcode0NotValidPostcode1NotValidPostcodeNotChanged + Strings.special_commaSpace;
        header += sTotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeChanged + Strings.special_commaSpace;
        header += sPercentageOfAllCount1_AllPostcode0NotValidPostcode1NotValidPostcodeChanged + Strings.special_commaSpace;
        header += sTotalCount_AllPostcode0ValidPostcode1DNE + Strings.special_commaSpace;
        header += sPercentageOfAllCount1_AllPostcode0ValidPostcode1DNE + Strings.special_commaSpace;
        header += sTotalCount_AllPostcode0DNEPostcode1Valid + Strings.special_commaSpace;
        header += sPercentageOfAllCount1_AllPostcode0DNEPostcode1Valid + Strings.special_commaSpace;
        header += sTotalCount_AllPostcode0DNEPostcode1DNE + Strings.special_commaSpace;
        header += sPercentageOfAllCount1_AllPostcode0DNEPostcode1DNE + Strings.special_commaSpace;
        header += sTotalCount_AllPostcode0DNEPostcode1NotValid + Strings.special_commaSpace;
        header += sPercentageOfAllCount1_AllPostcode0DNEPostcode1NotValid + Strings.special_commaSpace;
        header += sTotalCount_AllPostcode0NotValidPostcode1DNE + Strings.special_commaSpace;
        header += sPercentageOfAllCount1_AllPostcode0NotValidPostcode1DNE + Strings.special_commaSpace;
        // HB Postcode Related
        header += sTotalCount_HBPostcode0ValidPostcode1Valid + Strings.special_commaSpace;
        header += sPercentageOfHBCount1_HBPostcode0ValidPostcode1Valid + Strings.special_commaSpace;
        header += sTotalCount_HBPostcode0ValidPostcode1ValidPostcodeNotChanged + Strings.special_commaSpace;
        header += sPercentageOfHBCount1_HBPostcode0ValidPostcode1ValidPostcodeNotChanged + Strings.special_commaSpace;
        header += sTotalCount_HBPostcode0ValidPostcode1ValidPostcodeChange + Strings.special_commaSpace;
        header += sPercentageOfHBCount1_HBPostcode0ValidPostcode1ValidPostcodeChange + Strings.special_commaSpace;
        header += sTotalCount_HBPostcode0ValidPostcode1NotValid + Strings.special_commaSpace;
        header += sPercentageOfHBCount1_HBPostcode0ValidPostcode1NotValid + Strings.special_commaSpace;
        header += sTotalCount_HBPostcode0NotValidPostcode1Valid + Strings.special_commaSpace;
        header += sPercentageOfHBCount1_HBPostcode0NotValidPostcode1Valid + Strings.special_commaSpace;
        header += sTotalCount_HBPostcode0NotValidPostcode1NotValid + Strings.special_commaSpace;
        header += sPercentageOfHBCount1_HBPostcode0NotValidPostcode1NotValid + Strings.special_commaSpace;
        header += sTotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeNotChanged + Strings.special_commaSpace;
        header += sPercentageOfHBCount1_HBPostcode0NotValidPostcode1NotValidPostcodeNotChanged + Strings.special_commaSpace;
        header += sTotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeChanged + Strings.special_commaSpace;
        header += sPercentageOfHBCount1_HBPostcode0NotValidPostcode1NotValidPostcodeChanged + Strings.special_commaSpace;
        header += sTotalCount_HBPostcode0ValidPostcode1DNE + Strings.special_commaSpace;
        header += sPercentageOfHBCount1_HBPostcode0ValidPostcode1DNE + Strings.special_commaSpace;
        header += sTotalCount_HBPostcode0DNEPostcode1Valid + Strings.special_commaSpace;
        header += sPercentageOfHBCount1_HBPostcode0DNEPostcode1Valid + Strings.special_commaSpace;
        header += sTotalCount_HBPostcode0DNEPostcode1DNE + Strings.special_commaSpace;
        header += sPercentageOfHBCount1_HBPostcode0DNEPostcode1DNE + Strings.special_commaSpace;
        header += sTotalCount_HBPostcode0DNEPostcode1NotValid + Strings.special_commaSpace;
        header += sPercentageOfHBCount1_HBPostcode0DNEPostcode1NotValid + Strings.special_commaSpace;
        header += sTotalCount_HBPostcode0NotValidPostcode1DNE + Strings.special_commaSpace;
        header += sPercentageOfHBCount1_HBPostcode0NotValidPostcode1DNE + Strings.special_commaSpace;
        // CTB Postcode Related
        header += sTotalCount_CTBPostcode0ValidPostcode1Valid + Strings.special_commaSpace;
        header += sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1Valid + Strings.special_commaSpace;
        header += sTotalCount_CTBPostcode0ValidPostcode1ValidPostcodeNotChanged + Strings.special_commaSpace;
        header += sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1ValidPostcodeNotChanged + Strings.special_commaSpace;
        header += sTotalCount_CTBPostcode0ValidPostcode1ValidPostcodeChanged + Strings.special_commaSpace;
        header += sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1ValidPostcodeChanged + Strings.special_commaSpace;
        header += sTotalCount_CTBPostcode0ValidPostcode1NotValid + Strings.special_commaSpace;
        header += sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1NotValid + Strings.special_commaSpace;
        header += sTotalCount_CTBPostcode0NotValidPostcode1Valid + Strings.special_commaSpace;
        header += sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1Valid + Strings.special_commaSpace;
        header += sTotalCount_CTBPostcode0NotValidPostcode1NotValid + Strings.special_commaSpace;
        header += sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1NotValid + Strings.special_commaSpace;
        header += sTotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeNotChanged + Strings.special_commaSpace;
        header += sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1NotValidPostcodeNotChanged + Strings.special_commaSpace;
        header += sTotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeChanged + Strings.special_commaSpace;
        header += sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1NotValidPostcodeChanged + Strings.special_commaSpace;
        header += sTotalCount_CTBPostcode0ValidPostcode1DNE + Strings.special_commaSpace;
        header += sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1DNE + Strings.special_commaSpace;
        header += sTotalCount_CTBPostcode0DNEPostcode1Valid + Strings.special_commaSpace;
        header += sPercentageOfCTBCount1_CTBPostcode0DNEPostcode1Valid + Strings.special_commaSpace;
        header += sTotalCount_CTBPostcode0DNEPostcode1DNE + Strings.special_commaSpace;
        header += sPercentageOfCTBCount1_CTBPostcode0DNEPostcode1DNE + Strings.special_commaSpace;
        header += sTotalCount_CTBPostcode0DNEPostcode1NotValid + Strings.special_commaSpace;
        header += sPercentageOfCTBCount1_CTBPostcode0DNEPostcode1NotValid + Strings.special_commaSpace;
        header += sTotalCount_CTBPostcode0NotValidPostcode1DNE + Strings.special_commaSpace;
        header += sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1DNE + Strings.special_commaSpace;
        return header;
    }

    protected String getLineCompare2TimesPostcodeChange(HashMap<String, String> summary) {
        String line = "";
        // All
        line += summary.get(sTotalCount_AllPostcode0ValidPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfAllCount1_AllPostcode0ValidPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_AllPostcode0ValidPostcode1ValidPostcodeNotChanged) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfAllCount1_AllPostcode0ValidPostcode1ValidPostcodeNotChanged) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_AllPostcode0ValidPostcode1ValidPostcodeChange) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfAllCount1_AllPostcode0ValidPostcode1ValidPostcodeChange) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_AllPostcode0ValidPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfAllCount1_AllPostcode0ValidPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_AllPostcode0NotValidPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfAllCount1_AllPostcode0NotValidPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_AllPostcode0NotValidPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfAllCount1_AllPostcode0NotValidPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeNotChanged) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfAllCount1_AllPostcode0NotValidPostcode1NotValidPostcodeNotChanged) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_AllPostcode0NotValidPostcode1NotValidPostcodeChanged) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfAllCount1_AllPostcode0NotValidPostcode1NotValidPostcodeChanged) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_AllPostcode0ValidPostcode1DNE) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfAllCount1_AllPostcode0ValidPostcode1DNE) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_AllPostcode0DNEPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfAllCount1_AllPostcode0DNEPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_AllPostcode0DNEPostcode1DNE) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfAllCount1_AllPostcode0DNEPostcode1DNE) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_AllPostcode0DNEPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfAllCount1_AllPostcode0DNEPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_AllPostcode0NotValidPostcode1DNE) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfAllCount1_AllPostcode0NotValidPostcode1DNE) + Strings.special_commaSpace;
        // HB
        line += summary.get(sTotalCount_HBPostcode0ValidPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfHBCount1_HBPostcode0ValidPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_HBPostcode0ValidPostcode1ValidPostcodeNotChanged) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfHBCount1_HBPostcode0ValidPostcode1ValidPostcodeNotChanged) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_HBPostcode0ValidPostcode1ValidPostcodeChange) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfHBCount1_HBPostcode0ValidPostcode1ValidPostcodeChange) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_HBPostcode0ValidPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfHBCount1_HBPostcode0ValidPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_HBPostcode0NotValidPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfHBCount1_HBPostcode0NotValidPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_HBPostcode0NotValidPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfHBCount1_HBPostcode0NotValidPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeNotChanged) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfHBCount1_HBPostcode0NotValidPostcode1NotValidPostcodeNotChanged) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_HBPostcode0NotValidPostcode1NotValidPostcodeChanged) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfHBCount1_HBPostcode0NotValidPostcode1NotValidPostcodeChanged) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_HBPostcode0ValidPostcode1DNE) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfHBCount1_HBPostcode0ValidPostcode1DNE) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_HBPostcode0DNEPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfHBCount1_HBPostcode0DNEPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_HBPostcode0DNEPostcode1DNE) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfHBCount1_HBPostcode0DNEPostcode1DNE) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_HBPostcode0DNEPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfHBCount1_HBPostcode0DNEPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_HBPostcode0NotValidPostcode1DNE) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfHBCount1_HBPostcode0NotValidPostcode1DNE) + Strings.special_commaSpace;
        // CTB
        line += summary.get(sTotalCount_CTBPostcode0ValidPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBPostcode0ValidPostcode1ValidPostcodeNotChanged) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1ValidPostcodeNotChanged) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBPostcode0ValidPostcode1ValidPostcodeChanged) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1ValidPostcodeChanged) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBPostcode0ValidPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBPostcode0NotValidPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBPostcode0NotValidPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeNotChanged) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1NotValidPostcodeNotChanged) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBPostcode0NotValidPostcode1NotValidPostcodeChanged) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1NotValidPostcodeChanged) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBPostcode0ValidPostcode1DNE) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount1_CTBPostcode0ValidPostcode1DNE) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBPostcode0DNEPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount1_CTBPostcode0DNEPostcode1Valid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBPostcode0DNEPostcode1DNE) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount1_CTBPostcode0DNEPostcode1DNE) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBPostcode0DNEPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount1_CTBPostcode0DNEPostcode1NotValid) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBPostcode0NotValidPostcode1DNE) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount1_CTBPostcode0NotValidPostcode1DNE) + Strings.special_commaSpace;
        return line;
    }

    public String getHeaderCompare2TimesTTChange() {
        String header = "";
        // General
        // All
        header += sTotalCount_AllTTChangeClaimant + Strings.special_commaSpace;
        header += sAllPercentageOfAll_TTChangeClaimant + Strings.special_commaSpace;
//        header += sTotalCount_AllTTChangeClaimantIgnoreMinus999 + Strings.special_commaSpace;
//        header += sAllPercentageOfAll_TTChangeClaimantIgnoreMinus999 + Strings.special_commaSpace;
        // General HB related
        header += sTotalCount_HBTTChangeClaimant + Strings.special_commaSpace;
        header += sPercentageOfHBCount0_HBTTChangeClaimant + Strings.special_commaSpace;
//        header += sTotalCount_HBTTChangeClaimantIgnoreMinus999 + Strings.special_commaSpace;
//        header += sPercentageOfHB_HBTTChangeClaimantIgnoreMinus999 + Strings.special_commaSpace;

        header += sTotalCount_Minus999TTToSocialTTs + Strings.special_commaSpace;
        header += sTotalCount_Minus999TTToPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sTotalCount_HBTTsToMinus999TT + Strings.special_commaSpace;
        header += sPercentageOfHBCount0_HBTTsToMinus999TT + Strings.special_commaSpace;
        header += sTotalCount_SocialTTsToMinus999TT + Strings.special_commaSpace;
        header += sPercentageOfSocialTTs_SocialTTsToMinus999TT + Strings.special_commaSpace;
        header += sTotalCount_PrivateDeregulatedTTsToMinus999TT + Strings.special_commaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT + Strings.special_commaSpace;

        header += sTotalCount_HBTTsToHBTTs + Strings.special_commaSpace;
        header += sPercentageOfHBCount0_HBTTsToHBTTs + Strings.special_commaSpace;
        header += sTotalCount_SocialTTsToPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sTotalCount_PrivateDeregulatedTTsToSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs + Strings.special_commaSpace;
        header += sTotalCount_TT1ToPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfTT1_TT1ToPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sTotalCount_TT4ToPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfTT4_TT4ToPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sTotalCount_PrivateDeregulatedTTsToTT1 + Strings.special_commaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1 + Strings.special_commaSpace;
        header += sTotalCount_PrivateDeregulatedTTsToTT4 + Strings.special_commaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4 + Strings.special_commaSpace;
        header += sTotalCount_TT1ToTT4 + Strings.special_commaSpace;
        header += sPercentageOfTT1_TT1ToTT4 + Strings.special_commaSpace;
        header += sTotalCount_TT4ToTT1 + Strings.special_commaSpace;
        header += sPercentageOfTT4_TT4ToTT1 + Strings.special_commaSpace;
        header += sTotalCount_PostcodeChangeWithinSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs + Strings.special_commaSpace;
        header += sTotalCount_PostcodeChangeWithinTT1 + Strings.special_commaSpace;
        header += sPercentageOfTT1_PostcodeChangeWithinTT1 + Strings.special_commaSpace;
        header += sTotalCount_PostcodeChangeWithinTT4 + Strings.special_commaSpace;
        header += sPercentageOfTT4_PostcodeChangeWithinTT4 + Strings.special_commaSpace;
        header += sTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs + Strings.special_commaSpace;

        header += sTotalCount_HBTTsToCTBTTs + Strings.special_commaSpace;
        header += sPercentageOfHBCount0_HBTTsToCTBTTs + Strings.special_commaSpace;
        // General CTB related
        header += sTotalCount_CTBTTChangeClaimant + Strings.special_commaSpace;
        header += PercentageOfCTBCount0_CTBTTChangeClaimant + Strings.special_commaSpace;
//        header += sTotalCount_CTBTTChangeClaimantIgnoreMinus999 + Strings.special_commaSpace;
//        header += sPercentageOfCTB_CTBTTChangeClaimantIgnoreMinus999 + Strings.special_commaSpace;

        header += sTotalCount_Minus999TTToCTBTTs + Strings.special_commaSpace;
        header += sTotalCount_CTBTTsToMinus999TT + Strings.special_commaSpace;
        header += sPercentageOfCTBCount0_CTBTTsToMinus999TT + Strings.special_commaSpace;

        header += sTotalCount_SocialTTsToCTBTTs + Strings.special_commaSpace;
        header += sPercentageOfSocialTTs_SocialTTsToCTBTTs + Strings.special_commaSpace;
        header += sTotalCount_TT1ToCTBTTs + Strings.special_commaSpace;
        header += sPercentageOfTT1_TT1ToCTBTTs + Strings.special_commaSpace;
        header += sTotalCount_TT4ToCTBTTs + Strings.special_commaSpace;
        header += sPercentageOfTT4_TT4ToCTBTTs + Strings.special_commaSpace;
        header += sTotalCount_PrivateDeregulatedTTsToCTBTTs + Strings.special_commaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs + Strings.special_commaSpace;
        header += sTotalCount_CTBTTsToSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfCTBCount0_CTBTTsToSocialTTs + Strings.special_commaSpace;
        header += sTotalCount_CTBTTsToTT1 + Strings.special_commaSpace;
        header += sPercentageOfCTBCount0_CTBTTsToTT1 + Strings.special_commaSpace;
        header += sTotalCount_CTBTTsToTT4 + Strings.special_commaSpace;
        header += sPercentageOfCTBCount0_CTBTTsToTT4 + Strings.special_commaSpace;
        header += sTotalCount_CTBTTsToPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfCTBCount0_CTBTTsToPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sTotalCount_CTBTTsToHBTTs + Strings.special_commaSpace;
        header += sPercentageOfCTBCount0_CTBTTsToHBTTs + Strings.special_commaSpace;
        return header;
    }

    protected String getLineCompare2TimesTTChange(HashMap<String, String> summary) {
        String line = "";
        // All
        line += summary.get(sTotalCount_AllTTChangeClaimant) + Strings.special_commaSpace;
        line += summary.get(sAllPercentageOfAll_TTChangeClaimant) + Strings.special_commaSpace;
//        line += summary.get(sTotalCount_AllTTChangeClaimantIgnoreMinus999) + Strings.special_commaSpace;
//        line += summary.get(sAllPercentageOfAll_TTChangeClaimantIgnoreMinus999) + Strings.special_commaSpace;
        // General HB related
        line += summary.get(sTotalCount_HBTTChangeClaimant) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfHBCount0_HBTTChangeClaimant) + Strings.special_commaSpace;
//        line += summary.get(sTotalCount_HBTTChangeClaimantIgnoreMinus999) + Strings.special_commaSpace;
//        line += summary.get(sPercentageOfHB_HBTTChangeClaimantIgnoreMinus999) + Strings.special_commaSpace;

        line += summary.get(sTotalCount_Minus999TTToSocialTTs) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_Minus999TTToPrivateDeregulatedTTs) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_HBTTsToMinus999TT) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfHBCount0_HBTTsToMinus999TT) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_SocialTTsToMinus999TT) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfSocialTTs_SocialTTsToMinus999TT) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_PrivateDeregulatedTTsToMinus999TT) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToMinus999TT) + Strings.special_commaSpace;

        line += summary.get(sTotalCount_HBTTsToHBTTs) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfHBCount0_HBTTsToHBTTs) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_SocialTTsToPrivateDeregulatedTTs) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfSocialTTs_SocialTTsToPrivateDeregulatedTTs) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_PrivateDeregulatedTTsToSocialTTs) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToSocialTTs) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_TT1ToPrivateDeregulatedTTs) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfTT1_TT1ToPrivateDeregulatedTTs) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_TT4ToPrivateDeregulatedTTs) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfTT4_TT4ToPrivateDeregulatedTTs) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_PrivateDeregulatedTTsToTT1) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT1) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_PrivateDeregulatedTTsToTT4) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToTT4) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_TT1ToTT4) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfTT1_TT1ToTT4) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_TT4ToTT1) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfTT4_TT4ToTT1) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_PostcodeChangeWithinSocialTTs) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfSocialTTs_PostcodeChangeWithinSocialTTs) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_PostcodeChangeWithinTT1) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfTT1_PostcodeChangeWithinTT1) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_PostcodeChangeWithinTT4) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfTT4_PostcodeChangeWithinTT4) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_PostcodeChangeWithinPrivateDeregulatedTTs) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PostcodeChangeWithinPrivateDeregulatedTTs) + Strings.special_commaSpace;

        line += summary.get(sTotalCount_HBTTsToCTBTTs) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfHBCount0_HBTTsToCTBTTs) + Strings.special_commaSpace;
        // General CTB related
        line += summary.get(sTotalCount_CTBTTChangeClaimant) + Strings.special_commaSpace;
        line += summary.get(PercentageOfCTBCount0_CTBTTChangeClaimant) + Strings.special_commaSpace;
//        line += summary.get(sTotalCount_CTBTTChangeClaimantIgnoreMinus999) + Strings.special_commaSpace;
//        line += summary.get(sPercentageOfCTB_CTBTTChangeClaimantIgnoreMinus999) + Strings.special_commaSpace;

        line += summary.get(sTotalCount_Minus999TTToCTBTTs) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBTTsToMinus999TT) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount0_CTBTTsToMinus999TT) + Strings.special_commaSpace;

        line += summary.get(sTotalCount_SocialTTsToCTBTTs) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfSocialTTs_SocialTTsToCTBTTs) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_TT1ToCTBTTs) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfTT1_TT1ToCTBTTs) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_TT4ToCTBTTs) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfTT4_TT4ToCTBTTs) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_PrivateDeregulatedTTsToCTBTTs) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfPrivateDeregulatedTTs_PrivateDeregulatedTTsToCTBTTs) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBTTsToSocialTTs) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount0_CTBTTsToSocialTTs) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBTTsToTT1) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount0_CTBTTsToTT1) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBTTsToTT4) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount0_CTBTTsToTT4) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBTTsToPrivateDeregulatedTTs) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount0_CTBTTsToPrivateDeregulatedTTs) + Strings.special_commaSpace;
        line += summary.get(sTotalCount_CTBTTsToHBTTs) + Strings.special_commaSpace;
        line += summary.get(sPercentageOfCTBCount0_CTBTTsToHBTTs) + Strings.special_commaSpace;
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
    public void writeSummaryTableCompare2TimesTT(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            int nTT,
            int nEG
    ) {
        TreeMap<ONSPD_YM3, File> ONSPDFiles;
        ONSPDFiles = Files.getONSPD_Files().getInputONSPDFiles();
        String name;
        name = "Compare2TimesTT";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType, 
                includeKey, false);
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

    public String getHeaderCompare2TimesGeneric() {
        String header;
        header = "";
        header += sSHBEFilename0 + Strings.special_commaSpace;
        header += "Year0-Month0, ";
        header += "Month0 Year0, ";
        header += sSHBEFilename1 + Strings.special_commaSpace;
        header += "Year1-Month1, ";
        header += "Month1 Year1, ";
        header += "PostCodeLookupDate0, PostCodeLookupFile0, "
                + "PostCodeLookupDate1, PostCodeLookupFile1, ";
        header += sAllCount0 + Strings.special_commaSpace;
        header += sHBCount0 + Strings.special_commaSpace;
        header += sCTBCount0 + Strings.special_commaSpace;
        header += sAllCount1 + Strings.special_commaSpace;
        header += sHBCount1 + Strings.special_commaSpace;
        header += sCTBCount1 + Strings.special_commaSpace;
        header += "Month0 Year0 to Month1 Year1, ";
        return header;
    }

    public String getLineCompare2TimesGeneric(
            HashMap<String, String> summary,
            TreeMap<ONSPD_YM3, File> ONSPDFiles) {
        String line = "";
        String filename0;
        filename0 = summary.get(sSHBEFilename0);
        line += filename0 + Strings.special_commaSpace;
        String month0;
        String year0;
        if (filename0 != null) {
            line += SHBE_Handler.getYearMonthNumber(filename0) + Strings.special_commaSpace;
            month0 = SHBE_Handler.getMonth3(filename0);
            year0 = SHBE_Handler.getYear(filename0);
            line += month0 + " " + year0 + Strings.special_commaSpace;
        } else {
            month0 = "null";
            year0 = "null";
            line += "null, ";
            line += "null, ";
        }
        String filename1;
        filename1 = summary.get(sSHBEFilename1);
        line += filename1 + Strings.special_commaSpace;
        String month1;
        String year1;
        if (filename1 != null) {
            line += SHBE_Handler.getYearMonthNumber(filename1) + Strings.special_commaSpace;
            month1 = SHBE_Handler.getMonth3(filename1);
            year1 = SHBE_Handler.getYear(filename1);
            line += month1 + " " + year1 + Strings.special_commaSpace;
        } else {
            month1 = "null";
            year1 = "null";
            line += "null, ";
            line += "null, ";
        }
        ONSPD_YM3 PostCodeLookupDate0 = null;
        String PostCodeLookupFile0Name = null;
        if (filename0 != null) {
            PostCodeLookupDate0 = Postcode_Handler.getNearestYM3ForONSPDLookup(SHBE_Handler.getYM3(filename0));
            PostCodeLookupFile0Name = ONSPDFiles.get(PostCodeLookupDate0).getName();
        }
        line += PostCodeLookupDate0 + Strings.special_commaSpace + PostCodeLookupFile0Name + Strings.special_commaSpace;
        ONSPD_YM3 PostCodeLookupDate1 = null;
        String PostCodeLookupFile1Name = null;
        if (filename1 != null) {
            PostCodeLookupDate1 = Postcode_Handler.getNearestYM3ForONSPDLookup(SHBE_Handler.getYM3(filename1));
            PostCodeLookupFile1Name = ONSPDFiles.get(PostCodeLookupDate1).getName();
        }
        line += PostCodeLookupDate1 + Strings.special_commaSpace + PostCodeLookupFile1Name + Strings.special_commaSpace;
        line += summary.get(sAllCount0) + Strings.special_commaSpace;
        line += summary.get(sHBCount0) + Strings.special_commaSpace;
        line += summary.get(sCTBCount0) + Strings.special_commaSpace;
        line += summary.get(sAllCount1) + Strings.special_commaSpace;
        line += summary.get(sHBCount1) + Strings.special_commaSpace;
        line += summary.get(sCTBCount1) + Strings.special_commaSpace;
        line += month0 + " " + year0 + " to " + month1 + " " + year1 + Strings.special_commaSpace;
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
    public void writeSummaryTableCompare2TimesPostcode(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey,
            int nTT,
            int nEG
    ) {
        TreeMap<ONSPD_YM3, File> ONSPDFiles;
        ONSPDFiles = Files.getONSPD_Files().getInputONSPDFiles();
        String name;
        name = "Compare2TimesPostcode";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType,
                includeKey, false);
        // Write headers
        String header;
        header = getHeaderCompare2TimesGeneric();
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
                    ONSPDFiles);
            line += getLineCompare2TimesPostcodeChange(summary);
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
    public void writeSummaryTableSingleTimeGenericCounts(
            TreeMap<String, HashMap<String, String>> summaryTable,
            String includeKey, int nTT, int nEG, int nPSI) {
        TreeMap<ONSPD_YM3, File> ONSPDFiles;
        ONSPDFiles = Files.getONSPD_Files().getInputONSPDFiles();
        String name;
        name = "SingleTimeGenericCounts";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType, 
                includeKey, false);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + Strings.special_commaSpace;
        header += getHeaderSingleTimeGeneric();
        header += "PostCodeLookupDate, ";
        header += "PostCodeLookupFile, ";
        //header += Strings.SHBE_Strings.sCountOfClaims + Strings.special_commaSpace;
        //header += Strings.SHBE_Strings.sCountOfCTBAndHBClaims + Strings.special_commaSpace;
        //header += Strings.SHBE_Strings.sCountOfCTBClaims + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfNewSHBEClaims + Strings.special_commaSpace;
        for (int i = 1; i < nPSI; i++) {
            header += Strings.SHBE_Strings.sCountOfNewSHBEClaimsPSI + i + Strings.special_commaSpace;
        }
        header += Strings.SHBE_Strings.sCountOfNewSHBEClaimsWhereClaimantWasClaimantBefore + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfNewSHBEClaimsWhereClaimantWasPartnerBefore + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfNewSHBEClaimsWhereClaimantWasNonDependentBefore + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfNewSHBEClaimsWhereClaimantIsNew + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfClaimsWithClaimantsThatArePartnersInAnotherClaim + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfClaimsWithPartnersThatAreClaimantsInAnotherClaim + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfClaimsWithPartnersThatArePartnersInAnotherClaim + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfClaimantsInMultipleClaimsInAMonth + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfPartnersInMultipleClaimsInAMonth + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfNonDependentsInMultipleClaimsInAMonth + Strings.special_commaSpace;
        header += sHBPTICount1 + Strings.special_commaSpace;
        header += sHBPTSCount1 + Strings.special_commaSpace;
        header += sHBPTOCount1 + Strings.special_commaSpace;
        header += sCTBPTICount1 + Strings.special_commaSpace;
        header += sCTBPTSCount1 + Strings.special_commaSpace;
        header += sCTBPTOCount1 + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfSRecords + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfUniqueClaimants + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfClaimsWithPartners + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfUniquePartners + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfDependentsInAllClaims + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfUniqueDependents + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfNonDependentsInAllClaims + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfUniqueNonDependents + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfIndividuals + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfNewClaimantPostcodes + Strings.special_commaSpace;
        //header += Strings.SHBE_Strings.sCountOfNewValidMappableClaimantPostcodes + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfMappableClaimantPostcodes + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sCountOfNonMappableClaimantPostcodes + Strings.special_commaSpace;
//        //header += Strings.SHBE_Strings.sCountOfInvalidFormatClaimantPostcodes + Strings.special_commaSpace;
//        header += sTotalCount_AllPostcodeValidFormat + Strings.special_commaSpace;
//        header += sPercentageOfAllCount1_AllPostcodeValidFormat + Strings.special_commaSpace;
//        //header += sTotalCount_AllPostcodeInvalidFormat + Strings.special_commaSpace;
//        header += sTotalCount_AllPostcodeValid + Strings.special_commaSpace;
//        header += sPercentageOfAllCount1_AllPostcodeValid + Strings.special_commaSpace;
//        //header += sTotalCount_AllPostcodeInvalid + Strings.special_commaSpace;
//        //header += sTotalCount_HBPostcodeValidFormat + Strings.special_commaSpace;
//        //header += sPercentageOfHBCount1_HBPostcodeValidFormat + Strings.special_commaSpace;
//        //header += sTotalCount_HBPostcodeInvalidFormat + Strings.special_commaSpace;
//        header += sTotalCount_HBPostcodeValid + Strings.special_commaSpace;
//        header += sPercentageOfHBCount1_HBPostcodeValid + Strings.special_commaSpace;
//        //header += sTotalCount_HBPostcodeInvalid + Strings.special_commaSpace;
//        //header += sTotalCount_CTBPostcodeValidFormat + Strings.special_commaSpace;
//        //header += sPercentageOfCTBCount1_CTBPostcodeValidFormat + Strings.special_commaSpace;
//        //header += sTotalCount_CTBPostcodeInvalidFormat + Strings.special_commaSpace;
//        header += sTotalCount_CTBPostcodeValid + Strings.special_commaSpace;
//        header += sPercentageOfCTBCount1_CTBPostcodeValid + Strings.special_commaSpace;
//        //header += sTotalCount_CTBPostcodeInvalid + Strings.special_commaSpace;
//        header = header.substring(0, header.length() - 2);
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
            line += filename1 + Strings.special_commaSpace;
            line += getLineSingleTimeGeneric(key, summary);
            line += getPostcodeLookupDateAndFilenameLinePart(filename1, ONSPDFiles);
            //line += summary.get(Strings.SHBE_Strings.sCountOfClaims) + Strings.special_commaSpace;
            //line += summary.get(Strings.SHBE_Strings.sCountOfCTBAndHBClaims) + Strings.special_commaSpace;
            //line += summary.get(Strings.SHBE_Strings.sCountOfCTBClaims) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfNewSHBEClaims) + Strings.special_commaSpace;
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(Strings.SHBE_Strings.sCountOfNewSHBEClaimsPSI + i) + Strings.special_commaSpace;
            }
            line += summary.get(Strings.SHBE_Strings.sCountOfNewSHBEClaimsWhereClaimantWasClaimantBefore) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfNewSHBEClaimsWhereClaimantWasPartnerBefore) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfNewSHBEClaimsWhereClaimantWasNonDependentBefore) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfNewSHBEClaimsWhereClaimantIsNew) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfClaimsWithClaimantsThatArePartnersInAnotherClaim) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfClaimsWithPartnersThatAreClaimantsInAnotherClaim) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfClaimsWithPartnersThatArePartnersInAnotherClaim) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfClaimantsInMultipleClaimsInAMonth) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfPartnersInMultipleClaimsInAMonth) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfNonDependentsInMultipleClaimsInAMonth) + Strings.special_commaSpace;
            line += summary.get(sHBPTICount1) + Strings.special_commaSpace;
            line += summary.get(sHBPTSCount1) + Strings.special_commaSpace;
            line += summary.get(sHBPTOCount1) + Strings.special_commaSpace;
            line += summary.get(sCTBPTICount1) + Strings.special_commaSpace;
            line += summary.get(sCTBPTSCount1) + Strings.special_commaSpace;
            line += summary.get(sCTBPTOCount1) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfSRecords) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfUniqueClaimants) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfClaimsWithPartners) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfUniquePartners) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfDependentsInAllClaims) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfUniqueDependents) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfNonDependentsInAllClaims) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfUniqueNonDependents) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfIndividuals) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfNewClaimantPostcodes) + Strings.special_commaSpace;
            //line += summary.get(Strings.SHBE_Strings.sCountOfNewValidMappableClaimantPostcodes) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfMappableClaimantPostcodes) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sCountOfNonMappableClaimantPostcodes) + Strings.special_commaSpace;
            //line += summary.get(Strings.SHBE_Strings.sCountOfInvalidFormatClaimantPostcodes) + Strings.special_commaSpace;
//            line += summary.get(sTotalCount_AllPostcodeValidFormat) + Strings.special_commaSpace;
//            line += summary.get(sPercentageOfAllCount1_AllPostcodeValidFormat) + Strings.special_commaSpace;
//            line += summary.get(sTotalCount_AllPostcodeInvalidFormat) + Strings.special_commaSpace;
//            line += summary.get(sTotalCount_AllPostcodeValid) + Strings.special_commaSpace;
//            line += summary.get(sPercentageOfAllCount1_AllPostcodeValid) + Strings.special_commaSpace;
//            line += summary.get(sTotalCount_AllPostcodeInvalid) + Strings.special_commaSpace;
//            line += summary.get(sTotalCount_HBPostcodeValidFormat) + Strings.special_commaSpace;
//            line += summary.get(sPercentageOfHBCount1_HBPostcodeValidFormat) + Strings.special_commaSpace;
//            line += summary.get(sTotalCount_HBPostcodeInvalidFormat) + Strings.special_commaSpace;
//            line += summary.get(sTotalCount_HBPostcodeValid) + Strings.special_commaSpace;
//            line += summary.get(sPercentageOfHBCount1_HBPostcodeValid) + Strings.special_commaSpace;
//            line += summary.get(sTotalCount_HBPostcodeInvalid) + Strings.special_commaSpace;
//            line += summary.get(sTotalCount_CTBPostcodeValidFormat) + Strings.special_commaSpace;
//            line += summary.get(sPercentageOfCTBCount1_CTBPostcodeValidFormat) + Strings.special_commaSpace;
//            line += summary.get(sTotalCount_CTBPostcodeInvalidFormat) + Strings.special_commaSpace;
//            line += summary.get(sTotalCount_CTBPostcodeValid) + Strings.special_commaSpace;
//            line += summary.get(sPercentageOfCTBCount1_CTBPostcodeValid) + Strings.special_commaSpace;
//            line += summary.get(sTotalCount_CTBPostcodeInvalid) + Strings.special_commaSpace;
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
    public void writeSummaryTableSingleTimeHouseholdSizes(
            TreeMap<String, HashMap<String, String>> summaryTable,
            //String paymentType,
            String includeKey,
            int nTT,
            int nEG,
            int nPSI
    ) {
        TreeMap<ONSPD_YM3, File> ONSPDFiles;
        ONSPDFiles = Files.getONSPD_Files().getInputONSPDFiles();
        String name;
        name = "SingleTimeHouseholdSizes";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType, 
                includeKey, false);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + Strings.special_commaSpace;
        header += getHeaderSingleTimeGeneric();
        header += "PostCodeLookupDate, ";
        header += "PostCodeLookupFile, ";
        header += sAllTotalHouseholdSize + Strings.special_commaSpace;
        header += sAllAverageHouseholdSize + Strings.special_commaSpace;
        header += sHBTotalHouseholdSize + Strings.special_commaSpace;
        header += sHBAverageHouseholdSize + Strings.special_commaSpace;
        header += sCTBTotalHouseholdSize + Strings.special_commaSpace;
        header += sCTBAverageHouseholdSize + Strings.special_commaSpace;
        for (int i = 1; i < nTT; i++) {
            header += sTotal_HouseholdSizeTT[i] + Strings.special_commaSpace;
            header += sAverage_HouseholdSizeTT[i] + Strings.special_commaSpace;
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
            line += filename1 + Strings.special_commaSpace;
            line += getLineSingleTimeGeneric(key, summary);
            line += getPostcodeLookupDateAndFilenameLinePart(filename1, ONSPDFiles);
            line += summary.get(sAllTotalHouseholdSize) + Strings.special_commaSpace;
            line += summary.get(sAllAverageHouseholdSize) + Strings.special_commaSpace;
            line += summary.get(sHBTotalHouseholdSize) + Strings.special_commaSpace;
            line += summary.get(sHBAverageHouseholdSize) + Strings.special_commaSpace;
            line += summary.get(sCTBTotalHouseholdSize) + Strings.special_commaSpace;
            line += summary.get(sCTBAverageHouseholdSize) + Strings.special_commaSpace;
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sTotal_HouseholdSizeTT[i]) + Strings.special_commaSpace;
                line += summary.get(sAverage_HouseholdSizeTT[i]) + Strings.special_commaSpace;
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimeEntitlementEligibleAmountContractualAmount(
            TreeMap<String, HashMap<String, String>> summaryTable,
            //String paymentType,
            String includeKey,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeEntitlementEligibleAmountContractualAmount";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType,
                includeKey, false);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + Strings.special_commaSpace;
        header += getHeaderSingleTimeGeneric();
        header += Strings.sTotal_WeeklyHBEntitlement + Strings.special_commaSpace;
        header += Strings.sTotalCount_WeeklyHBEntitlementNonZero + Strings.special_commaSpace;
        header += Strings.sTotalCount_WeeklyHBEntitlementZero + Strings.special_commaSpace;
        header += Strings.sAverage_NonZero_WeeklyHBEntitlement + Strings.special_commaSpace;
        // WeeklyEligibleRentAmount
        header += Strings.sTotal_WeeklyEligibleRentAmount + Strings.special_commaSpace;
        header += Strings.sTotalCount_WeeklyEligibleRentAmountNonZero + Strings.special_commaSpace;
        header += Strings.sTotalCount_WeeklyEligibleRentAmountZero + Strings.special_commaSpace;
        header += Strings.SHBE_Strings.sAverage_NonZero_WeeklyEligibleRentAmount + Strings.special_commaSpace;
        header += Strings.sTotal_HBWeeklyEligibleRentAmount + Strings.special_commaSpace;
        header += Strings.sTotalCount_HBWeeklyEligibleRentAmountNonZero + Strings.special_commaSpace;
        header += Strings.sTotalCount_HBWeeklyEligibleRentAmountZero + Strings.special_commaSpace;
        header += Strings.sAverage_NonZero_HBWeeklyEligibleRentAmount + Strings.special_commaSpace;
        header += Strings.sTotal_CTBWeeklyEligibleRentAmount + Strings.special_commaSpace;
        header += Strings.sTotalCount_CTBWeeklyEligibleRentAmountNonZero + Strings.special_commaSpace;
        header += Strings.sTotalCount_CTBWeeklyEligibleRentAmountZero + Strings.special_commaSpace;
        header += Strings.sAverage_NonZero_CTBWeeklyEligibleRentAmount + Strings.special_commaSpace;
        // WeeklyEligibleCouncilTaxAmount
        header += sAllTotalWeeklyEligibleCouncilTaxAmount + Strings.special_commaSpace;
        header += sTotalCount_AllWeeklyEligibleCouncilTaxAmountNonZero + Strings.special_commaSpace;
        header += sTotalCount_AllWeeklyEligibleCouncilTaxAmountZero + Strings.special_commaSpace;
        header += sAllAverageWeeklyEligibleCouncilTaxAmount + Strings.special_commaSpace;
        header += sHBTotalWeeklyEligibleCouncilTaxAmount + Strings.special_commaSpace;
        header += sTotalCount_HBWeeklyEligibleCouncilTaxAmountNonZero + Strings.special_commaSpace;
        header += sTotalCount_HBWeeklyEligibleCouncilTaxAmountZero + Strings.special_commaSpace;
        header += sHBAverageWeeklyEligibleCouncilTaxAmount + Strings.special_commaSpace;
        header += sCTBTotalWeeklyEligibleCouncilTaxAmount + Strings.special_commaSpace;
        header += sTotalCount_CTBWeeklyEligibleCouncilTaxAmountNonZero + Strings.special_commaSpace;
        header += sTotalCount_CTBWeeklyEligibleCouncilTaxAmountZero + Strings.special_commaSpace;
        header += sCTBAverageWeeklyEligibleCouncilTaxAmount + Strings.special_commaSpace;
        // ContractualRentAmount
        header += sAllTotalContractualRentAmount + Strings.special_commaSpace;
        header += sAllTotalCountContractualRentAmountNonZeroCount + Strings.special_commaSpace;
        header += sAllTotalCountContractualRentAmountZeroCount + Strings.special_commaSpace;
        header += sAllAverageContractualRentAmount + Strings.special_commaSpace;
        header += sHBTotalContractualRentAmount + Strings.special_commaSpace;
        header += sTotalCount_HBContractualRentAmountNonZeroCount + Strings.special_commaSpace;
        header += sTotalCount_HBContractualRentAmountZeroCount + Strings.special_commaSpace;
        header += sHBAverageContractualRentAmount + Strings.special_commaSpace;
        header += sCTBTotalContractualRentAmount + Strings.special_commaSpace;
        header += sTotalCount_CTBContractualRentAmountNonZeroCount + Strings.special_commaSpace;
        header += sTotalCount_CTBContractualRentAmountZeroCount + Strings.special_commaSpace;
        header += sCTBAverageContractualRentAmount + Strings.special_commaSpace;
        // WeeklyAdditionalDiscretionaryPayment
        header += sAllTotalWeeklyAdditionalDiscretionaryPayment + Strings.special_commaSpace;
        header += sTotalCount_AllWeeklyAdditionalDiscretionaryPaymentNonZero + Strings.special_commaSpace;
        header += sTotalCount_AllWeeklyAdditionalDiscretionaryPaymentZero + Strings.special_commaSpace;
        header += sAllAverageWeeklyAdditionalDiscretionaryPayment + Strings.special_commaSpace;
        header += sHBTotalWeeklyAdditionalDiscretionaryPayment + Strings.special_commaSpace;
        header += sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentNonZero + Strings.special_commaSpace;
        header += sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentZero + Strings.special_commaSpace;
        header += sHBAverageWeeklyAdditionalDiscretionaryPayment + Strings.special_commaSpace;
        header += sCTBTotalWeeklyAdditionalDiscretionaryPayment + Strings.special_commaSpace;
        header += sTotalCount_CTBWeeklyAdditionalDiscretionaryPaymentNonZero + Strings.special_commaSpace;
        header += sTotalCount_CTBWeeklyAdditionalDiscretionaryPaymentZero + Strings.special_commaSpace;
        header += sCTBAverageWeeklyAdditionalDiscretionaryPayment + Strings.special_commaSpace;
        // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
        header += sAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + Strings.special_commaSpace;
        header += sTotalCount_AllWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + Strings.special_commaSpace;
        header += sTotalCount_AllWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + Strings.special_commaSpace;
        header += sAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + Strings.special_commaSpace;
        header += sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + Strings.special_commaSpace;
        header += sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + Strings.special_commaSpace;
        header += sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + Strings.special_commaSpace;
        header += sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + Strings.special_commaSpace;
        header += sCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + Strings.special_commaSpace;
        header += sTotalCount_CTBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero + Strings.special_commaSpace;
        header += sTotalCount_CTBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero + Strings.special_commaSpace;
        header += sCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability + Strings.special_commaSpace;
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
            line += filename1 + Strings.special_commaSpace;
            line += getLineSingleTimeGeneric(key, summary);
            line += summary.get(Strings.sTotal_WeeklyHBEntitlement) + Strings.special_commaSpace;
            line += summary.get(Strings.sTotalCount_WeeklyHBEntitlementNonZero) + Strings.special_commaSpace;
            line += summary.get(Strings.sTotalCount_WeeklyHBEntitlementZero) + Strings.special_commaSpace;
            line += summary.get(Strings.sAverage_NonZero_WeeklyHBEntitlement) + Strings.special_commaSpace;
            // WeeklyEligibleRentAmount
            line += summary.get(Strings.sTotal_WeeklyEligibleRentAmount) + Strings.special_commaSpace;
            line += summary.get(Strings.sTotalCount_WeeklyEligibleRentAmountNonZero) + Strings.special_commaSpace;
            line += summary.get(Strings.sTotalCount_WeeklyEligibleRentAmountZero) + Strings.special_commaSpace;
            line += summary.get(Strings.SHBE_Strings.sAverage_NonZero_WeeklyEligibleRentAmount) + Strings.special_commaSpace;
            line += summary.get(Strings.sTotal_HBWeeklyEligibleRentAmount) + Strings.special_commaSpace;
            line += summary.get(Strings.sTotalCount_HBWeeklyEligibleRentAmountNonZero) + Strings.special_commaSpace;
            line += summary.get(Strings.sTotalCount_HBWeeklyEligibleRentAmountZero) + Strings.special_commaSpace;
            line += summary.get(Strings.sAverage_NonZero_HBWeeklyEligibleRentAmount) + Strings.special_commaSpace;
            line += summary.get(Strings.sTotal_CTBWeeklyEligibleRentAmount) + Strings.special_commaSpace;
            line += summary.get(Strings.sTotalCount_CTBWeeklyEligibleRentAmountNonZero) + Strings.special_commaSpace;
            line += summary.get(Strings.sTotalCount_CTBWeeklyEligibleRentAmountZero) + Strings.special_commaSpace;
            line += summary.get(Strings.sAverage_NonZero_CTBWeeklyEligibleRentAmount) + Strings.special_commaSpace;
            // WeeklyEligibleCouncilTaxAmount
            line += summary.get(sAllTotalWeeklyEligibleCouncilTaxAmount) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_AllWeeklyEligibleCouncilTaxAmountNonZero) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_AllWeeklyEligibleCouncilTaxAmountZero) + Strings.special_commaSpace;
            line += summary.get(sAllAverageWeeklyEligibleCouncilTaxAmount) + Strings.special_commaSpace;
            line += summary.get(sHBTotalWeeklyEligibleCouncilTaxAmount) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HBWeeklyEligibleCouncilTaxAmountNonZero) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HBWeeklyEligibleCouncilTaxAmountZero) + Strings.special_commaSpace;
            line += summary.get(sHBAverageWeeklyEligibleCouncilTaxAmount) + Strings.special_commaSpace;
            line += summary.get(sCTBTotalWeeklyEligibleCouncilTaxAmount) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTBWeeklyEligibleCouncilTaxAmountNonZero) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTBWeeklyEligibleCouncilTaxAmountZero) + Strings.special_commaSpace;
            line += summary.get(sCTBAverageWeeklyEligibleCouncilTaxAmount) + Strings.special_commaSpace;
            // ContractualRentAmount
            line += summary.get(sAllTotalContractualRentAmount) + Strings.special_commaSpace;
            line += summary.get(sAllTotalCountContractualRentAmountNonZeroCount) + Strings.special_commaSpace;
            line += summary.get(sAllTotalCountContractualRentAmountZeroCount) + Strings.special_commaSpace;
            line += summary.get(sAllAverageContractualRentAmount) + Strings.special_commaSpace;
            line += summary.get(sHBTotalContractualRentAmount) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HBContractualRentAmountNonZeroCount) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HBContractualRentAmountZeroCount) + Strings.special_commaSpace;
            line += summary.get(sHBAverageContractualRentAmount) + Strings.special_commaSpace;
            line += summary.get(sCTBTotalContractualRentAmount) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTBContractualRentAmountNonZeroCount) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTBContractualRentAmountZeroCount) + Strings.special_commaSpace;
            line += summary.get(sCTBAverageContractualRentAmount) + Strings.special_commaSpace;
            // WeeklyAdditionalDiscretionaryPayment
            line += summary.get(sAllTotalWeeklyAdditionalDiscretionaryPayment) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_AllWeeklyAdditionalDiscretionaryPaymentNonZero) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_AllWeeklyAdditionalDiscretionaryPaymentZero) + Strings.special_commaSpace;
            line += summary.get(sAllAverageWeeklyAdditionalDiscretionaryPayment) + Strings.special_commaSpace;
            line += summary.get(sHBTotalWeeklyAdditionalDiscretionaryPayment) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentNonZero) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentZero) + Strings.special_commaSpace;
            line += summary.get(sHBAverageWeeklyAdditionalDiscretionaryPayment) + Strings.special_commaSpace;
            line += summary.get(sCTBTotalWeeklyAdditionalDiscretionaryPayment) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTBWeeklyAdditionalDiscretionaryPaymentNonZero) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTBWeeklyAdditionalDiscretionaryPaymentZero) + Strings.special_commaSpace;
            line += summary.get(sCTBAverageWeeklyAdditionalDiscretionaryPayment) + Strings.special_commaSpace;
            // WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
            line += summary.get(sAllTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_AllWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_AllWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + Strings.special_commaSpace;
            line += summary.get(sAllAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + Strings.special_commaSpace;
            line += summary.get(sHBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + Strings.special_commaSpace;
            line += summary.get(sHBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + Strings.special_commaSpace;
            line += summary.get(sCTBTotalWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityNonZero) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTBWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiabilityZero) + Strings.special_commaSpace;
            line += summary.get(sCTBAverageWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) + Strings.special_commaSpace;
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimeEmploymentEducationTraining(
            TreeMap<String, HashMap<String, String>> summaryTable,
            //String paymentType,
            String includeKey,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeEmploymentEducationTraining";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType, 
                includeKey, false);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + Strings.special_commaSpace;
        header += getHeaderSingleTimeGeneric();
        header += sTotalCount_AllClaimantsEmployed + Strings.special_commaSpace;
        header += sPercentageOfAll_AllClaimantsEmployed + Strings.special_commaSpace;
        header += sTotalCount_AllClaimantsSelfEmployed + Strings.special_commaSpace;
        header += sPercentageOfAll_AllClaimantsSelfEmployed + Strings.special_commaSpace;
        header += sTotalCount_AllClaimantsStudents + Strings.special_commaSpace;
        header += sPercentageOfAll_AllClaimantsStudents + Strings.special_commaSpace;
        header += sTotalCount_HBClaimantsEmployed + Strings.special_commaSpace;
        header += sPercentageOfHB_HBClaimantsEmployed + Strings.special_commaSpace;
        header += sTotalCount_HBClaimantsSelfEmployed + Strings.special_commaSpace;
        header += sPercentageOfHB_HBClaimantsSelfEmployed + Strings.special_commaSpace;
        header += sTotalCount_HBClaimantsStudents + Strings.special_commaSpace;
        header += sPercentageOfHB_HBClaimantsStudents + Strings.special_commaSpace;
        header += sTotalCount_CTBClaimantsEmployed + Strings.special_commaSpace;
        header += sPercentageOfCTB_CTBClaimantsEmployed + Strings.special_commaSpace;
        header += sTotalCount_CTBClaimantsSelfEmployed + Strings.special_commaSpace;
        header += sPercentageOfCTB_CTBClaimantsSelfEmployed + Strings.special_commaSpace;
        header += sTotalCount_CTBClaimantsStudents + Strings.special_commaSpace;
        header += sPercentageOfCTB_CTBClaimantsStudents + Strings.special_commaSpace;
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
            line += filename1 + Strings.special_commaSpace;
            line += getLineSingleTimeGeneric(key, summary);
            line += summary.get(sTotalCount_AllClaimantsEmployed) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_AllClaimantsEmployed) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_AllClaimantsSelfEmployed) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_AllClaimantsSelfEmployed) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_AllClaimantsStudents) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_AllClaimantsStudents) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HBClaimantsEmployed) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_HBClaimantsEmployed) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HBClaimantsSelfEmployed) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_HBClaimantsSelfEmployed) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HBClaimantsStudents) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_HBClaimantsStudents) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTBClaimantsEmployed) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfCTB_CTBClaimantsEmployed) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTBClaimantsSelfEmployed) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfCTB_CTBClaimantsSelfEmployed) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTBClaimantsStudents) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfCTB_CTBClaimantsStudents) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTBLHACases) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfCTB_CTBLHACases) + Strings.special_commaSpace;
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    protected String getPostcodeLookupDateAndFilenameLinePart(
            String filename,
            TreeMap<ONSPD_YM3, File> ONSPDFiles) {
        String result;
        ONSPD_YM3 PostCodeLookupDate0 = null;
        String PostCodeLookupFile0Name = null;
        if (filename != null) {
            PostCodeLookupDate0 = Postcode_Handler.getNearestYM3ForONSPDLookup(SHBE_Handler.getYM3(filename));
            PostCodeLookupFile0Name = ONSPDFiles.get(PostCodeLookupDate0).getName();
        }
        result = PostCodeLookupDate0 + Strings.special_commaSpace + PostCodeLookupFile0Name + Strings.special_commaSpace;
        return result;
    }

    public void writeSummaryTableSingleTimeRentAndIncome(
            TreeMap<String, HashMap<String, String>> summaryTable,
            ArrayList<String> HB_CTB,
            ArrayList<String> PTs,
            String includeKey,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeRentAndIncome";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType,
                includeKey, false);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + Strings.special_commaSpace;
        header += getHeaderSingleTimeGeneric();
        String sHB_CTB;
        String PT;
        String nameSuffix;
        Iterator<String> HB_CTBIte;
        HB_CTBIte = HB_CTB.iterator();
        Iterator<String> PTsIte;
        while (HB_CTBIte.hasNext()) {
            sHB_CTB = HB_CTBIte.next();
            PTsIte = PTs.iterator();
            while (PTsIte.hasNext()) {
                PT = PTsIte.next();
                nameSuffix = sHB_CTB + PT;
                header += Strings.sTotal_Income + nameSuffix + Strings.special_commaSpace;
                header += Strings.sTotalCount_IncomeNonZero + nameSuffix + Strings.special_commaSpace;
                header += Strings.sTotalCount_IncomeZero + nameSuffix + Strings.special_commaSpace;
                header += Strings.SHBE_Strings.sAverage_NonZero_Income + nameSuffix + Strings.special_commaSpace;
                header += Strings.sTotal_WeeklyEligibleRentAmount + nameSuffix + Strings.special_commaSpace;
                header += Strings.sTotalCount_WeeklyEligibleRentAmountNonZero + nameSuffix + Strings.special_commaSpace;
                header += Strings.SHBE_Strings.sAverage_NonZero_WeeklyEligibleRentAmount + nameSuffix + Strings.special_commaSpace;
                if (sHB_CTB.equalsIgnoreCase(Strings.sAll)) {
                    for (int i = 1; i < nTT; i++) {
                        header += Strings.sTotal_IncomeTT[i] + nameSuffix + Strings.special_commaSpace;
                        header += Strings.sTotalCount_IncomeNonZeroTT[i] + nameSuffix + Strings.special_commaSpace;
                        header += Strings.sTotalCount_IncomeZeroTT[i] + nameSuffix + Strings.special_commaSpace;
                        header += Strings.sAverage_NonZero_IncomeTT[i] + nameSuffix + Strings.special_commaSpace;
                        header += Strings.sTotal_WeeklyEligibleRentAmountTT[i] + nameSuffix + Strings.special_commaSpace;
                        header += Strings.sTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] + nameSuffix + Strings.special_commaSpace;
                        header += Strings.sAverage_NonZero_WeeklyEligibleRentAmountTT[i] + nameSuffix + Strings.special_commaSpace;
                    }
                } else if (sHB_CTB.equalsIgnoreCase(Strings.sHB)) {
                    for (int i = 1; i < nTT; i++) {
                        if (!(i == 5 || i == 7)) {
                            header += Strings.sTotal_IncomeTT[i] + nameSuffix + Strings.special_commaSpace;
                            header += Strings.sTotalCount_IncomeNonZeroTT[i] + nameSuffix + Strings.special_commaSpace;
                            header += Strings.sTotalCount_IncomeZeroTT[i] + nameSuffix + Strings.special_commaSpace;
                            header += Strings.sAverage_NonZero_IncomeTT[i] + nameSuffix + Strings.special_commaSpace;
                            header += Strings.sTotal_WeeklyEligibleRentAmountTT[i] + nameSuffix + Strings.special_commaSpace;
                            header += Strings.sTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] + nameSuffix + Strings.special_commaSpace;
                            header += Strings.sAverage_NonZero_WeeklyEligibleRentAmountTT[i] + nameSuffix + Strings.special_commaSpace;
                        }
                    }
                } else {
                    for (int i = 1; i < nTT; i++) {
                        if (i == 5 || i == 7) {
                            header += Strings.sTotal_IncomeTT[i] + nameSuffix + Strings.special_commaSpace;
                            header += Strings.sTotalCount_IncomeNonZeroTT[i] + nameSuffix + Strings.special_commaSpace;
                            header += Strings.sTotalCount_IncomeZeroTT[i] + nameSuffix + Strings.special_commaSpace;
                            header += Strings.sAverage_NonZero_IncomeTT[i] + nameSuffix + Strings.special_commaSpace;
                            header += Strings.sTotal_WeeklyEligibleRentAmountTT[i] + nameSuffix + Strings.special_commaSpace;
                            header += Strings.sTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] + nameSuffix + Strings.special_commaSpace;
                            header += Strings.sAverage_NonZero_WeeklyEligibleRentAmountTT[i] + nameSuffix + Strings.special_commaSpace;
                        }
                    }
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
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + Strings.special_commaSpace;
            line += getLineSingleTimeGeneric(key, summary);
            HB_CTBIte = HB_CTB.iterator();
            while (HB_CTBIte.hasNext()) {
                sHB_CTB = HB_CTBIte.next();
                PTsIte = PTs.iterator();
                while (PTsIte.hasNext()) {
                    PT = PTsIte.next();
                    nameSuffix = sHB_CTB + PT;
                    line += summary.get(Strings.sTotal_Income + nameSuffix) + Strings.special_commaSpace;
                    line += summary.get(Strings.sTotalCount_IncomeNonZero + nameSuffix) + Strings.special_commaSpace;
                    line += summary.get(Strings.sTotalCount_IncomeZero + nameSuffix) + Strings.special_commaSpace;
                    line += summary.get(Strings.SHBE_Strings.sAverage_NonZero_Income + nameSuffix) + Strings.special_commaSpace;
                    line += summary.get(Strings.sTotal_WeeklyEligibleRentAmount + nameSuffix) + Strings.special_commaSpace;
                    line += summary.get(Strings.sTotalCount_WeeklyEligibleRentAmountNonZero + nameSuffix) + Strings.special_commaSpace;
                    line += summary.get(Strings.SHBE_Strings.sAverage_NonZero_WeeklyEligibleRentAmount + nameSuffix) + Strings.special_commaSpace;
                    if (sHB_CTB.equalsIgnoreCase(Strings.sAll)) {
                        for (int i = 1; i < nTT; i++) {
                            line += summary.get(Strings.sTotal_IncomeTT[i] + nameSuffix) + Strings.special_commaSpace;
                            line += summary.get(Strings.sTotalCount_IncomeNonZeroTT[i] + nameSuffix) + Strings.special_commaSpace;
                            line += summary.get(Strings.sTotalCount_IncomeZeroTT[i] + nameSuffix) + Strings.special_commaSpace;
                            line += summary.get(Strings.sAverage_NonZero_IncomeTT[i] + nameSuffix) + Strings.special_commaSpace;
                            line += summary.get(Strings.sTotal_WeeklyEligibleRentAmountTT[i] + nameSuffix) + Strings.special_commaSpace;
                            line += summary.get(Strings.sTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] + nameSuffix) + Strings.special_commaSpace;
                            line += summary.get(Strings.sAverage_NonZero_WeeklyEligibleRentAmountTT[i] + nameSuffix) + Strings.special_commaSpace;
                        }
                    } else if (sHB_CTB.equalsIgnoreCase(Strings.sHB)) {
                        for (int i = 1; i < nTT; i++) {
                            if (!(i == 5 || i == 7)) {
                                line += summary.get(Strings.sTotal_IncomeTT[i] + nameSuffix) + Strings.special_commaSpace;
                                line += summary.get(Strings.sTotalCount_IncomeNonZeroTT[i] + nameSuffix) + Strings.special_commaSpace;
                                line += summary.get(Strings.sTotalCount_IncomeZeroTT[i] + nameSuffix) + Strings.special_commaSpace;
                                line += summary.get(Strings.sAverage_NonZero_IncomeTT[i] + nameSuffix) + Strings.special_commaSpace;
                                line += summary.get(Strings.sTotal_WeeklyEligibleRentAmountTT[i] + nameSuffix) + Strings.special_commaSpace;
                                line += summary.get(Strings.sTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] + nameSuffix) + Strings.special_commaSpace;
                                line += summary.get(Strings.sAverage_NonZero_WeeklyEligibleRentAmountTT[i] + nameSuffix) + Strings.special_commaSpace;
                            }
                        }
                    } else {
                        for (int i = 5; i < nTT; i++) {
                            if (i == 5 || i == 7) {
                                line += summary.get(Strings.sTotal_IncomeTT[i] + nameSuffix) + Strings.special_commaSpace;
                                line += summary.get(Strings.sTotalCount_IncomeNonZeroTT[i] + nameSuffix) + Strings.special_commaSpace;
                                line += summary.get(Strings.sTotalCount_IncomeZeroTT[i] + nameSuffix) + Strings.special_commaSpace;
                                line += summary.get(Strings.sAverage_NonZero_IncomeTT[i] + nameSuffix) + Strings.special_commaSpace;
                                line += summary.get(Strings.sTotal_WeeklyEligibleRentAmountTT[i] + nameSuffix) + Strings.special_commaSpace;
                                line += summary.get(Strings.sTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] + nameSuffix) + Strings.special_commaSpace;
                                line += summary.get(Strings.sAverage_NonZero_WeeklyEligibleRentAmountTT[i] + nameSuffix) + Strings.special_commaSpace;
                            }
                        }
                    }
                }
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimeEthnicity(
            TreeMap<String, HashMap<String, String>> summaryTable,
            //String paymentType,
            String includeKey,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeEthnicity";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType, 
                includeKey, false);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + Strings.special_commaSpace;
        header += getHeaderSingleTimeGeneric();
        for (int i = 1; i < nEG; i++) {
            header += sTotalCount_AllEthnicGroupClaimant[i] + Strings.special_commaSpace;
            header += sPercentageOfAll_EthnicGroupClaimant[i] + Strings.special_commaSpace;
            header += sTotalCount_AllEthnicGroupSocialTTClaimant[i] + Strings.special_commaSpace;
            header += sPercentageOfSocialTT_EthnicGroupSocialTTClaimant[i] + Strings.special_commaSpace;
            header += sTotalCount_AllEthnicGroupPrivateDeregulatedTTClaimant[i] + Strings.special_commaSpace;
            header += sPercentageOfPrivateDeregulatedTT_EthnicGroupPrivateDeregulatedTTClaimant[i] + Strings.special_commaSpace;
        }
        for (int i = 1; i < nEG; i++) {
            header += sTotalCount_HBEthnicGroupClaimant[i] + Strings.special_commaSpace;
            header += sPercentageOfHB_HBEthnicGroupClaimant[i] + Strings.special_commaSpace;
        }
        for (int i = 1; i < nEG; i++) {
            header += sTotalCount_CTBEthnicGroupClaimant[i] + Strings.special_commaSpace;
            header += sPercentageOfCTB_CTBEthnicGroupClaimant[i] + Strings.special_commaSpace;
        }
        for (int i = 1; i < nEG; i++) {
            for (int j = 1; j < nTT; j++) {
                header += sTotalCount_AllEthnicGroupClaimantTT[i][j] + Strings.special_commaSpace;
                header += sPercentageOfTT_EthnicGroupClaimantTT[i][j] + Strings.special_commaSpace;
                header += sPercentageOfEthnicGroup_EthnicGroupClaimantTT[i][j] + Strings.special_commaSpace;
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
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + Strings.special_commaSpace;
            line += getLineSingleTimeGeneric(key, summary);
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sTotalCount_AllEthnicGroupClaimant[i]) + Strings.special_commaSpace;
                line += summary.get(sPercentageOfAll_EthnicGroupClaimant[i]) + Strings.special_commaSpace;
                line += summary.get(sTotalCount_AllEthnicGroupSocialTTClaimant[i]) + Strings.special_commaSpace;
                line += summary.get(sPercentageOfSocialTT_EthnicGroupSocialTTClaimant[i]) + Strings.special_commaSpace;
                line += summary.get(sTotalCount_AllEthnicGroupPrivateDeregulatedTTClaimant[i]) + Strings.special_commaSpace;
                line += summary.get(sPercentageOfPrivateDeregulatedTT_EthnicGroupPrivateDeregulatedTTClaimant[i]) + Strings.special_commaSpace;
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sTotalCount_HBEthnicGroupClaimant[i]) + Strings.special_commaSpace;
                line += summary.get(sPercentageOfHB_HBEthnicGroupClaimant[i]) + Strings.special_commaSpace;
            }
            for (int i = 1; i < nEG; i++) {
                line += summary.get(sTotalCount_CTBEthnicGroupClaimant[i]) + Strings.special_commaSpace;
                line += summary.get(sPercentageOfCTB_CTBEthnicGroupClaimant[i]) + Strings.special_commaSpace;
            }
            for (int i = 1; i < nEG; i++) {
                for (int j = 1; j < nTT; j++) {
                    line += summary.get(sTotalCount_AllEthnicGroupClaimantTT[i][j]) + Strings.special_commaSpace;
                    line += summary.get(sPercentageOfTT_EthnicGroupClaimantTT[i][j]) + Strings.special_commaSpace;
                    line += summary.get(sPercentageOfEthnicGroup_EthnicGroupClaimantTT[i][j]) + Strings.special_commaSpace;
                }
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimeTT(
            TreeMap<String, HashMap<String, String>> summaryTable,
            //String paymentType,
            String includeKey,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeTT";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType, 
                includeKey, false);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + Strings.special_commaSpace;
        header += getHeaderSingleTimeGeneric();
        header += sTotalCount_SocialTTsClaimant + Strings.special_commaSpace;
        header += sPercentageOfAll_SocialTTsClaimant + Strings.special_commaSpace;
        header += sPercentageOfHB_SocialTTsClaimant + Strings.special_commaSpace;
        header += sTotalCount_PrivateDeregulatedTTsClaimant + Strings.special_commaSpace;
        header += sPercentageOfAll_PrivateDeregulatedTTsClaimant + Strings.special_commaSpace;
        header += sPercentageOfHB_PrivateDeregulatedTTsClaimant + Strings.special_commaSpace;
        for (int i = 1; i < nTT; i++) {
            header += sTotalCount_ClaimantTT[i] + Strings.special_commaSpace;
            header += sPercentageOfAll_ClaimantTT[i] + Strings.special_commaSpace;
            if ((i == 5 || i == 7)) {
                header += sPercentageOfCTB_ClaimantTT[i] + Strings.special_commaSpace;
            } else {
                header += sPercentageOfHB_ClaimantTT[i] + Strings.special_commaSpace;
            }
        }
        header += sTotalCount_AllLHACases + Strings.special_commaSpace;
        header += sPercentageOfAll_AllLHACases + Strings.special_commaSpace;
        header += sTotalCount_HBLHACases + Strings.special_commaSpace;
        header += sPercentageOfHB_HBLHACases + Strings.special_commaSpace;
        header += sTotalCount_CTBLHACases + Strings.special_commaSpace;
        header += sPercentageOfCTB_CTBLHACases + Strings.special_commaSpace;
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
            line += filename1 + Strings.special_commaSpace;
            line += getLineSingleTimeGeneric(key, summary);
            line += summary.get(sTotalCount_SocialTTsClaimant) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_SocialTTsClaimant) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_SocialTTsClaimant) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_PrivateDeregulatedTTsClaimant) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_PrivateDeregulatedTTsClaimant) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_PrivateDeregulatedTTsClaimant) + Strings.special_commaSpace;
            for (int i = 1; i < nTT; i++) {
                line += summary.get(sTotalCount_ClaimantTT[i]) + Strings.special_commaSpace;
                line += summary.get(sPercentageOfAll_ClaimantTT[i]) + Strings.special_commaSpace;
                if ((i == 5 || i == 7)) {
                    line += summary.get(sPercentageOfCTB_ClaimantTT[i]) + Strings.special_commaSpace;
                } else {
                    line += summary.get(sPercentageOfHB_ClaimantTT[i]) + Strings.special_commaSpace;
                }
            }
            line += summary.get(sTotalCount_AllLHACases) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_AllLHACases) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_HBLHACases) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_HBLHACases) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_CTBLHACases) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfCTB_CTBLHACases) + Strings.special_commaSpace;
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimePSI(
            TreeMap<String, HashMap<String, String>> summaryTable,
            //String paymentType,
            String includeKey,
            int nTT,
            int nEG,
            int nPSI
    ) {
        String name;
        name = sSingleTimePSI;
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType, 
                includeKey, false);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + Strings.special_commaSpace;
        header += getHeaderSingleTimeGeneric();
        for (int i = 1; i < nPSI; i++) {
            header += sTotalCount_AllPSI[i] + Strings.special_commaSpace;
            header += sPercentageOfAll_AllPSI[i] + Strings.special_commaSpace;
        }
        for (int i = 1; i < nPSI; i++) {
            header += sTotalCount_HBPSI[i] + Strings.special_commaSpace;
            header += sPercentageOfHB_HBPSI[i] + Strings.special_commaSpace;
        }
        for (int i = 1; i < nPSI; i++) {
            header += sTotalCount_CTBPSI[i] + Strings.special_commaSpace;
            header += sPercentageOfCTB_CTBPSI[i] + Strings.special_commaSpace;
        }
        for (int i = 1; i < nPSI; i++) {
            for (int j = 1; j < nTT; j++) {
                header += sTotalCount_PSITT[i][j] + Strings.special_commaSpace;
                header += sPercentageOfAll_PSITT[i][j] + Strings.special_commaSpace;
                if (j == 5 || j == 7) {
                    header += sPercentageOfCTB_PSITT[i][j] + Strings.special_commaSpace;
                } else {
                    header += sPercentageOfHB_PSITT[i][j] + Strings.special_commaSpace;
                }
                header += sPercentageOfTT_PSITT[i][j] + Strings.special_commaSpace;
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
            String filename1;
            filename1 = summary.get(sSHBEFilename1);
            line += filename1 + Strings.special_commaSpace;
            line += getLineSingleTimeGeneric(key, summary);
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sTotalCount_AllPSI[i]) + Strings.special_commaSpace;
                line += summary.get(sPercentageOfAll_AllPSI[i]) + Strings.special_commaSpace;
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sTotalCount_HBPSI[i]) + Strings.special_commaSpace;
                line += summary.get(sPercentageOfHB_HBPSI[i]) + Strings.special_commaSpace;
            }
            for (int i = 1; i < nPSI; i++) {
                line += summary.get(sTotalCount_CTBPSI[i]) + Strings.special_commaSpace;
                line += summary.get(sPercentageOfCTB_CTBPSI[i]) + Strings.special_commaSpace;
            }
            for (int i = 1; i < nPSI; i++) {
                for (int j = 1; j < nTT; j++) {
                    line += summary.get(sTotalCount_PSITT[i][j]) + Strings.special_commaSpace;
                    line += summary.get(sPercentageOfAll_PSITT[i][j]) + Strings.special_commaSpace;
                    if (j == 5 || j == 7) {
                        line += summary.get(sPercentageOfCTB_PSITT[i][j]) + Strings.special_commaSpace;
                    } else {
                        line += summary.get(sPercentageOfHB_PSITT[i][j]) + Strings.special_commaSpace;
                    }
                    line += summary.get(sPercentageOfTT_PSITT[i][j]) + Strings.special_commaSpace;
                }
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    public void writeSummaryTableSingleTimeDisability(
            TreeMap<String, HashMap<String, String>> summaryTable,
            //String paymentType,
            String includeKey,
            int nTT,
            int nEG
    ) {
        String name;
        name = "SingleTimeDisability";
        PrintWriter pw;
        pw = getPrintWriter(name, sSummaryTables, summaryTable,
                //paymentType,
                includeKey, false);
        // Write headers
        String header;
        header = "";
        header += sSHBEFilename1 + Strings.special_commaSpace;
        header += getHeaderSingleTimeGeneric();
        // General
        // DisabilityAward
        header += sTotalCount_AllDisabilityAward + Strings.special_commaSpace;
        header += sPercentageOfAll_AllDisabilityAward + Strings.special_commaSpace;
        header += sTotalCount_DisabilityAwardHBTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_DisabilityAwardHBTTs + Strings.special_commaSpace;
        header += sPercentageOfHB_DisabilityAwardHBTTs + Strings.special_commaSpace;
        header += sTotalCount_DisabilityAwardCTBTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_DisabilityAwardCTBTTs + Strings.special_commaSpace;
        header += sPercentageOfCTB_DisabilityAwardCTBTTs + Strings.special_commaSpace;
        // DisabilityPremiumAward
        header += sTotalCount_AllDisabilityPremiumAward + Strings.special_commaSpace;
        header += sPercentageOfAll_AllDisabilityPremiumAward + Strings.special_commaSpace;
        header += sTotalCount_DisabilityPremiumAwardHBTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_DisabilityPremiumAwardHBTTs + Strings.special_commaSpace;
        header += sPercentageOfHB_DisabilityPremiumAwardHBTTs + Strings.special_commaSpace;
        header += sTotalCount_DisabilityPremiumAwardCTBTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_DisabilityPremiumAwardCTBTTs + Strings.special_commaSpace;
        header += sPercentageOfCTB_DisabilityPremiumAwardCTBTTs + Strings.special_commaSpace;
        // SevereDisabilityPremiumAward
        header += sTotalCount_AllSevereDisabilityPremiumAward + Strings.special_commaSpace;
        header += sPercentageOfAll_AllSevereDisabilityPremiumAward + Strings.special_commaSpace;
        header += sTotalCount_SevereDisabilityPremiumAwardHBTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_SevereDisabilityPremiumAwardHBTTs + Strings.special_commaSpace;
        header += sPercentageOfHB_SevereDisabilityPremiumAwardHBTTs + Strings.special_commaSpace;
        header += sTotalCount_SevereDisabilityPremiumAwardCTBTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs + Strings.special_commaSpace;
        header += sPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs + Strings.special_commaSpace;
        // DisabledChildPremiumAward
        header += sTotalCount_AllDisabledChildPremiumAward + Strings.special_commaSpace;
        header += sPercentageOfAll_AllDisabledChildPremiumAward + Strings.special_commaSpace;
        header += sTotalCount_DisabledChildPremiumAwardHBTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_DisabledChildPremiumAwardHBTTs + Strings.special_commaSpace;
        header += sPercentageOfHB_DisabledChildPremiumAwardHBTTs + Strings.special_commaSpace;
        header += sTotalCount_DisabledChildPremiumAwardCTBTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_DisabledChildPremiumAwardCTBTTs + Strings.special_commaSpace;
        header += sPercentageOfCTB_DisabledChildPremiumAwardCTBTTs + Strings.special_commaSpace;
        // EnhancedDisabilityPremiumAward
        header += sTotalCount_AllEnhancedDisabilityPremiumAward + Strings.special_commaSpace;
        header += sPercentageOfAll_AllEnhancedDisabilityPremiumAward + Strings.special_commaSpace;
        header += sTotalCount_EnhancedDisabilityPremiumAwardHBTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs + Strings.special_commaSpace;
        header += sPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs + Strings.special_commaSpace;
        header += sTotalCount_EnhancedDisabilityPremiumAwardCTBTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs + Strings.special_commaSpace;
        header += sPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs + Strings.special_commaSpace;
        // SocialTTs
        // DisabilityAward
        header += sTotalCount_DisabilityAwardSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_DisabilityAwardSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfHB_DisabilityAwardSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfSocialTTs_DisabilityAwardSocialTTs + Strings.special_commaSpace;
        // DisabilityPremiumAward
        header += sTotalCount_DisabilityPremiumAwardSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_DisabilityPremiumAwardSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfHB_DisabilityPremiumAwardSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs + Strings.special_commaSpace;
        // SevereDisabilityPremiumAward
        header += sTotalCount_SevereDisabilityPremiumAwardSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs + Strings.special_commaSpace;
        // DisabledChildPremiumAward
        header += sTotalCount_DisabledChildPremiumAwardSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_DisabledChildPremiumAwardSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfHB_DisabledChildPremiumAwardSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs + Strings.special_commaSpace;
        // EnhancedDisabilityPremiumAward
        header += sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs + Strings.special_commaSpace;
        header += sPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs + Strings.special_commaSpace;
        // PrivateDeregulatedTTs
        // DisabilityAward
        header += sTotalCount_DisabilityAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        // DisabilityPremiumAward
        header += sTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        // SevereDisabilityPremiumAward
        header += sTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        // DisabledChildPremiumAward
        header += sTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        // EnhancedDisabilityPremiumAward
        header += sTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        header += sPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs + Strings.special_commaSpace;
        for (int i = 1; i < nTT; i++) {
            // DisabilityAward
            header += sTotalCount_DisabilityAwardTT[i] + Strings.special_commaSpace;
            header += sPercentageOfAll_DisabilityAwardTT[i] + Strings.special_commaSpace;
            if (i == 5 || i == 7) {
                header += sPercentageOfCTB_DisabilityAwardTT[i] + Strings.special_commaSpace;
            } else {
                header += sPercentageOfHB_DisabilityAwardTT[i] + Strings.special_commaSpace;
            }
            header += sPercentageOfTT_DisabilityAwardTT[i] + Strings.special_commaSpace;
            // DisabilityPremiumAward
            header += sTotalCount_DisabilityPremiumAwardTT[i] + Strings.special_commaSpace;
            header += sPercentageOfAll_DisabilityPremiumAwardTT[i] + Strings.special_commaSpace;
            if (i == 5 || i == 7) {
                header += sPercentageOfCTB_DisabilityPremiumAwardTT[i] + Strings.special_commaSpace;
            } else {
                header += sPercentageOfHB_DisabilityPremiumAwardTT[i] + Strings.special_commaSpace;
            }
            header += sPercentageOfTT_DisabilityPremiumAwardTT[i] + Strings.special_commaSpace;
            // SevereDisabilityPremiumAward
            header += sTotalCount_SevereDisabilityPremiumAwardTT[i] + Strings.special_commaSpace;
            header += sPercentageOfAll_SevereDisabilityPremiumAwardTT[i] + Strings.special_commaSpace;
            if (i == 5 || i == 7) {
                header += sPercentageOfCTB_SevereDisabilityPremiumAwardTT[i] + Strings.special_commaSpace;
            } else {
                header += sPercentageOfHB_SevereDisabilityPremiumAwardTT[i] + Strings.special_commaSpace;
            }
            header += sPercentageOfTT_SevereDisabilityPremiumAwardTT[i] + Strings.special_commaSpace;
            // DisabledChildPremiumAward
            header += sTotalCount_DisabledChildPremiumAwardTT[i] + Strings.special_commaSpace;
            header += sPercentageOfAll_DisabledChildPremiumAwardTT[i] + Strings.special_commaSpace;
            if (i == 5 || i == 7) {
                header += sPercentageOfCTB_DisabledChildPremiumAwardTT[i] + Strings.special_commaSpace;
            } else {
                header += sPercentageOfHB_DisabledChildPremiumAwardTT[i] + Strings.special_commaSpace;
            }
            header += sPercentageOfTT_DisabledChildPremiumAwardTT[i] + Strings.special_commaSpace;
            // EnhancedDisabilityPremiumAward
            header += sTotalCount_EnhancedDisabilityPremiumAwardTT[i] + Strings.special_commaSpace;
            header += sPercentageOfAll_EnhancedDisabilityPremiumAwardTT[i] + Strings.special_commaSpace;
            if (i == 5 || i == 7) {
                header += sPercentageOfCTB_EnhancedDisabilityPremiumAwardTT[i] + Strings.special_commaSpace;
            } else {
                header += sPercentageOfHB_EnhancedDisabilityPremiumAwardTT[i] + Strings.special_commaSpace;
            }
            header += sPercentageOfTT_EnhancedDisabilityPremiumAwardTT[i] + Strings.special_commaSpace;
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
            line += filename1 + Strings.special_commaSpace;
            line += getLineSingleTimeGeneric(key,
                    summary);
            // General
            // DisabilityAward
            line += summary.get(sTotalCount_AllDisabilityAward) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_AllDisabilityAward) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_DisabilityAwardHBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_DisabilityAwardHBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_DisabilityAwardHBTTs) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_DisabilityAwardCTBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_DisabilityAwardCTBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfCTB_DisabilityAwardCTBTTs) + Strings.special_commaSpace;
            // DisabilityPremiumAward
            line += summary.get(sTotalCount_AllDisabilityPremiumAward) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_AllDisabilityPremiumAward) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_DisabilityPremiumAwardHBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_DisabilityPremiumAwardHBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_DisabilityPremiumAwardHBTTs) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_DisabilityPremiumAwardCTBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_DisabilityPremiumAwardCTBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfCTB_DisabilityPremiumAwardCTBTTs) + Strings.special_commaSpace;
            // SevereDisabilityPremiumAward
            line += summary.get(sTotalCount_AllSevereDisabilityPremiumAward) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_AllSevereDisabilityPremiumAward) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardHBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardHBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_SevereDisabilityPremiumAwardHBTTs) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardCTBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardCTBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfCTB_SevereDisabilityPremiumAwardCTBTTs) + Strings.special_commaSpace;
            // DisabledChildPremiumAward
            line += summary.get(sTotalCount_AllDisabledChildPremiumAward) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_AllDisabledChildPremiumAward) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_DisabledChildPremiumAwardHBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardHBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_DisabledChildPremiumAwardHBTTs) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_DisabledChildPremiumAwardCTBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardCTBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfCTB_DisabledChildPremiumAwardCTBTTs) + Strings.special_commaSpace;
            // EnhancedDisabilityPremiumAward
            line += summary.get(sTotalCount_AllEnhancedDisabilityPremiumAward) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_AllEnhancedDisabilityPremiumAward) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardHBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardHBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_EnhancedDisabilityPremiumAwardHBTTs) + Strings.special_commaSpace;
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardCTBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardCTBTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfCTB_EnhancedDisabilityPremiumAwardCTBTTs) + Strings.special_commaSpace;
            // SocialTTs
            // DisabilityAward
            line += summary.get(sTotalCount_DisabilityAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_DisabilityAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_DisabilityAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfSocialTTs_DisabilityAwardSocialTTs) + Strings.special_commaSpace;
            // DisabilityPremiumAward
            line += summary.get(sTotalCount_DisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_DisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_DisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfSocialTTs_DisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;
            // SevereDisabilityPremiumAward
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_SevereDisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfSocialTTs_SevereDisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;
            // DisabledChildPremiumAward
            line += summary.get(sTotalCount_DisabledChildPremiumAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_DisabledChildPremiumAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfSocialTTs_DisabledChildPremiumAwardSocialTTs) + Strings.special_commaSpace;
            // EnhancedDisabilityPremiumAward
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_EnhancedDisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfSocialTTs_EnhancedDisabilityPremiumAwardSocialTTs) + Strings.special_commaSpace;
            // PrivateDeregulatedTTs
            // DisabilityAward
            line += summary.get(sTotalCount_DisabilityAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_DisabilityAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_DisabilityAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfPrivateDeregulatedTTs_DisabilityAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            // DisabilityPremiumAward
            line += summary.get(sTotalCount_DisabilityPremiumAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_DisabilityPremiumAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_DisabilityPremiumAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfPrivateDeregulatedTTs_DisabilityPremiumAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            // SevereDisabilityPremiumAward
            line += summary.get(sTotalCount_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfPrivateDeregulatedTTs_SevereDisabilityPremiumAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            // DisabledChildPremiumAward
            line += summary.get(sTotalCount_DisabledChildPremiumAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_DisabledChildPremiumAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfPrivateDeregulatedTTs_DisabledChildPremiumAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            // EnhancedDisabilityPremiumAward
            line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfHB_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            line += summary.get(sPercentageOfPrivateDeregulatedTTs_EnhancedDisabilityPremiumAwardPrivateDeregulatedTTs) + Strings.special_commaSpace;
            for (int i = 1; i < nTT; i++) {
                // DisabilityAward
                line += summary.get(sTotalCount_DisabilityAwardTT[i]) + Strings.special_commaSpace;
                line += summary.get(sPercentageOfAll_DisabilityAwardTT[i]) + Strings.special_commaSpace;
                if (i == 5 || i == 7) {
                    line += summary.get(sPercentageOfCTB_DisabilityAwardTT[i]) + Strings.special_commaSpace;
                } else {
                    line += summary.get(sPercentageOfHB_DisabilityAwardTT[i]) + Strings.special_commaSpace;
                }
                line += summary.get(sPercentageOfTT_DisabilityAwardTT[i]) + Strings.special_commaSpace;
                // DisabilityPremiumAward
                line += summary.get(sTotalCount_DisabilityPremiumAwardTT[i]) + Strings.special_commaSpace;
                line += summary.get(sPercentageOfAll_DisabilityPremiumAwardTT[i]) + Strings.special_commaSpace;
                if (i == 5 || i == 7) {
                    line += summary.get(sPercentageOfCTB_DisabilityPremiumAwardTT[i]) + Strings.special_commaSpace;
                } else {
                    line += summary.get(sPercentageOfHB_DisabilityPremiumAwardTT[i]) + Strings.special_commaSpace;
                }
                line += summary.get(sPercentageOfTT_DisabilityPremiumAwardTT[i]) + Strings.special_commaSpace;
                // SevereDisabilityPremiumAward
                line += summary.get(sTotalCount_SevereDisabilityPremiumAwardTT[i]) + Strings.special_commaSpace;
                line += summary.get(sPercentageOfAll_SevereDisabilityPremiumAwardTT[i]) + Strings.special_commaSpace;
                if (i == 5 || i == 7) {
                    line += summary.get(sPercentageOfCTB_SevereDisabilityPremiumAwardTT[i]) + Strings.special_commaSpace;
                } else {
                    line += summary.get(sPercentageOfHB_SevereDisabilityPremiumAwardTT[i]) + Strings.special_commaSpace;
                }
                line += summary.get(sPercentageOfTT_SevereDisabilityPremiumAwardTT[i]) + Strings.special_commaSpace;
                // DisabledChildPremiumAward
                line += summary.get(sTotalCount_DisabledChildPremiumAwardTT[i]) + Strings.special_commaSpace;
                line += summary.get(sPercentageOfAll_DisabledChildPremiumAwardTT[i]) + Strings.special_commaSpace;
                if (i == 5 || i == 7) {
                    line += summary.get(sPercentageOfCTB_DisabledChildPremiumAwardTT[i]) + Strings.special_commaSpace;
                } else {
                    line += summary.get(sPercentageOfHB_DisabledChildPremiumAwardTT[i]) + Strings.special_commaSpace;
                }
                line += summary.get(sPercentageOfTT_DisabledChildPremiumAwardTT[i]) + Strings.special_commaSpace;
                // EnhancedDisabilityPremiumAward
                line += summary.get(sTotalCount_EnhancedDisabilityPremiumAwardTT[i]) + Strings.special_commaSpace;
                line += summary.get(sPercentageOfAll_EnhancedDisabilityPremiumAwardTT[i]) + Strings.special_commaSpace;
                if (i == 5 || i == 7) {
                    line += summary.get(sPercentageOfCTB_EnhancedDisabilityPremiumAwardTT[i]) + Strings.special_commaSpace;
                } else {
                    line += summary.get(sPercentageOfHB_EnhancedDisabilityPremiumAwardTT[i]) + Strings.special_commaSpace;
                }
                line += summary.get(sPercentageOfTT_EnhancedDisabilityPremiumAwardTT[i]) + Strings.special_commaSpace;
            }
            line = line.substring(0, line.length() - 2);
            pw.println(line);
        }
        pw.close();
    }

    protected String getHeaderSingleTimeGeneric() {
        String result;
        result = "year-month" + Strings.special_commaSpace;
        result += sAllCount1 + Strings.special_commaSpace;
        result += sHBCount1 + Strings.special_commaSpace;
        result += sCTBCount1 + Strings.special_commaSpace;
        result += "Month Year" + Strings.special_commaSpace;
        return result;
    }

    protected String getLineSingleTimeGeneric(
            String key,
            HashMap<String, String> summary) {
        String result;
        result = key + Strings.special_commaSpace;
        result += summary.get(sAllCount1) + Strings.special_commaSpace;
        result += summary.get(sHBCount1) + Strings.special_commaSpace;
        result += summary.get(sCTBCount1) + Strings.special_commaSpace;
        String[] split;
        split = key.split("-");
        result += Generic_Time.getMonth3Letters(split[1]);
        result += sSpace + split[0] + Strings.special_commaSpace;
        return result;
    }
}
