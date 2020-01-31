package uk.ac.leeds.ccg.projects.dw.data;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import uk.ac.leeds.ccg.data.ukp.data.id.UKP_RecordID;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;
import uk.ac.leeds.ccg.data.shbe.data.id.SHBE_ClaimID;
import uk.ac.leeds.ccg.data.shbe.core.SHBE_Strings;
import uk.ac.leeds.ccg.math.Math_BigDecimal;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.core.DW_Object;
import uk.ac.leeds.ccg.data.shbe.data.id.SHBE_PersonID;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Records;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_D_Record;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Handler;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Record;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_S_Record;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.projects.dw.core.DW_Strings;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Data;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Record;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Handler;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Set;

/**
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_TenancyChangesUO extends DW_Object {

    /**
     * For convenience;
     */
    SHBE_Handler shbeHandler;
    SHBE_TenancyType_Handler ttHandler;
    Map<SHBE_ClaimID, String> cid2c;
    Map<String, SHBE_ClaimID> c2cid;
    Map<String, UKP_RecordID> p2pid;

    HashSet<String> ValidPostcodes;

    String sAggregateStatistics = "AggregateStatistics";
//    String sUnderOccupancyGroupTables = "UOGT";
    String sUnderOccupancyGroupTables = "UnderOccupancyGroupTables";
    String defaultPostcode = "AAN NAA";
    String sU = "U";
    String sUnderOccupancy = "UnderOccupancy";
    String sTT = "TT";
    String sP = "Postcode";
    String sWHBE = "WeeklyHousingBenefitEntitlement";
    String sWERA = "WeeklyEligibleRentAmount";
    String sPSI = "PassportedStandardIndicator";
    String sSHBC = "StatusOfHBClaim";
    String sRTHBCC = "ReasonThatHBClaimClosed";
    String sCEG = "ClaimantEthnicGroup";
    String sHS = "HouseholdSize";
    String sND = "NonDependents";
    String sCD = "ChildDependents";
    String sCDoB = "ClaimantsDoB";
    String sCA = "ClaimantsAge";
    String sPDoB = "PartnersDoB";
    String sPA = "PartnersAge";
    String sCG = "ClaimantsGender";
    String sPG = "PartnersGender";
    String sD = "D";
    String sDisability = "Disability";
    String sDP = "DP";
    String sDisabilityPremium = "DisabilityPremium";
    String sDS = "DS";
    String sDisabilitySevere = "DisabilitySevere";
    String sDE = "DE";
    String sDisabilityEnhanced = "DisabilityEnhanced";
    String sDC = "DC";
    String sDisabledChild = "DisabledChild";
    String sPDeath = "PartnerDeath";
    String sHBDP = "HBDiscretionaryPayment";
    String sIncludesPreUnderOccupancyValues = "IncludesPreUnderOccupancyValues";
    //String sCTBDP = "CTBDiscretionaryPayment";
    String sA = "Arrears";
    String s = "";
    String s0 = "0"; // zero
    String s0Dot0 = "0.0"; // zeros
    String s0Dot00 = "0.00"; // zeros

    String sTT_;
    String sBR = "BedroomRequirement";
    String sNB = "NumberOfBedrooms";
    String sNDUO = "NonDependents(UO)";
    String sCO16 = "ChildrenOver16";
    String sFCU10 = "FemaleChildrenUnder10";
    String sMCU10 = "MaleChildrenUnder10";
    String sFC10To16 = "FemaleChildren10to16";
    String sMC10To16 = "MaleChildren10to16";

    String sNotUOInApril2013ThenUOAndUOInLatestMonth = "NotUOInApril2013ThenUOAndUOInLatestMonth";
    String sPercentageOfUO_ReceivingDHPInLatestMonth = "PercentageOfUO_ReceivingDHPInLatestMonth";
    String sPercentageOfCouncilUO_InArrearsInLatestMonth = "PercentageOfCouncilUO_InArrearsInLatestMonth";
    String sPercentageOfCouncilUO_InArrears0To10InLatestMonth = "PercentageOfCouncilUO_InArrears0To10InLatestMonth";
    String sPercentageOfCouncilUO_InArrears10To100InLatestMonth = "PercentageOfCouncilUO_InArrears10To100InLatestMonth";
    String sPercentageOfCouncilUO_InArrears100To500InLatestMonth = "PercentageOfCouncilUO_InArrears100To500InLatestMonth";
    String sPercentageOfCouncilUO_InArrearsOver500InLatestMonth = "PercentageOfCouncilUO_InArrearsOver500InLatestMonth";
    String sPercentageOfCouncilUO_InArrearsAndReceivingDHPInLatestMonth = "PercentageOfCouncilUO_InArrearsAndReceivingDHPInLatestMonth";
    String sPercentageOfUO_ReceivingDHPInLatestMonthThatWereUOInApril2013 = "PercentageOfUO_ReceivingDHPInLatestMonthThatWereUOInApril2013";
    String sPercentageOfCouncilUO_InArrearsInLatestMonthThatWereUOInApril2013 = "PercentageOfCouncilUO_InArrearsInLatestMonthThatWereUOInApril2013";
    String sPercentageOfCouncilUO_InArrears0To10InLatestMonthThatWereUOInApril2013 = "PercentageOfCouncilUO_InArrears0To10InLatestMonthThatWereUOInApril2013";
    String sPercentageOfCouncilUO_InArrears10To100InLatestMonthThatWereUOInApril2013 = "PercentageOfCouncilUO_InArrears10To100InLatestMonthThatWereUOInApril2013";
    String sPercentageOfCouncilUO_InArrears100To500InLatestMonthThatWereUOInApril2013 = "PercentageOfCouncilUO_InArrears100To500InLatestMonthThatWereUOInApril2013";
    String sPercentageOfCouncilUO_InArrearsOver500InLatestMonthThatWereUOInApril2013 = "PercentageOfCouncilUO_InArrearsOver500InLatestMonthThatWereUOInApril2013";
    String sPercentageOfCouncilUO_InArrearsAndReceivingDHPInLatestMonthThatWereUOInApril2013 = "PercentageOfCouncilUO_InArrearsAndReceivingDHPInLatestMonthThatWereUOInApril2013";

    String sUniqueIndividualsEffected = "UniqueIndividualsEffected";

    String sNoValidPostcodeChange = "NoValidPostcodeChange";
    String sChangedTT = "ChangedTT";
    String sUOAtSomePoint = "UOAtSomePoint";
    String sUOTT1AtSomePoint = "UOTT1AtSomePoint";
    String sUOTT4AtSomePoint = "UOTT4AtSomePoint";
    String sAlwaysUOTT1FromStart = "AlwaysUOTT1FromStart";
    String sAlwaysUOTT1FromStartExceptWhenSuspended = "AlwaysUOTT1FromStartExceptWhenSuspended";
    String sAlwaysUOTT1FromWhenStarted = "AlwaysUOTT1FromWhenStarted";
    String sAlwaysUOTT4FromStart = "AlwaysUOTT4FromStart";
    String sAlwaysUOTT4FromStartExceptWhenSuspended = "AlwaysUOTT4FromStartExceptWhenSuspended";
    String sAlwaysUOTT4FromWhenStarted = "AlwaysUOTT4FromWhenStarted";
    String sIntermitantUO = "IntermitantUO";
    String sPermanantlyLeftUOButRemainedInSHBE = "PermanantlyLeftUOButRemainedInSHBE";
    String sPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged = "PermanantlyLeftUOButRemainedInSHBE_PostcodeChanged";
    String sPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased = "PermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased";
    String sTravellers;
    String sTTNot1Or4AndUnderOccupying;
    String sTT3OrTT6_To_TT1;
    String sTT1_To_TT3OrTT6;
    String sTT3OrTT6_To_TT4;
    String sTT4_To_TT3OrTT6;
    String sAlwaysUOFromStart__NoValidPostcodeChange_NotChangedTT;
    String sAlwaysUOFromStart__ChangedTT;
    String sAlwaysUOFromStart__ValidPostcodeChange_NotChangedTT;
    String sAlwaysUOFromWhenStarted__NoValidPostcodeChange_NotChangedTT;
    String sAlwaysUOFromWhenStarted__ChangedTT;
    String sAlwaysUOFromWhenStarted__ValidPostcodeChange_NotChangedTT;
    String sIntermitantUO__ChangedTT;
    String sIntermitantUO__ValidPostcodeChange_NotChangedTT;
    String sIntermitantUO__NoValidPostcodeChange_NotChangedTT;

    String sUO_NotUO_UO = "UO_NotUO_UO";
    String sUO_NotUO_UO_NotUO = "UO_NotUO_UO_NotUO";
    String sUO_NotUO_UO_NotUO_UO = "UO_NotUO_UO_NotUO_UO";
    String sUO_NotUO_UO_NotUO_UO_NotUO = "UO_NotUO_UO_NotUO_UO_NotUO";
    String sUO_NotUO_UO_NotUO_UO_NotUO_UO = "UO_NotUO_UO_NotUO_UO_NotUO_UO";
    String sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO = "UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO";
    String sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO = "UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO";

    String sUOTT1_To_NotUO_InSHBE_PostcodeChanged = "UOTT1_To_NotUO_InSHBE_PostcodeChanged";
    String sUOTT1_To_UOTT1_PostcodeChanged = "UOTT1_To_UOTT1_PostcodeChanged";
    String sUOTT1_To_TT1_PostcodeChanged = "UOTT1_To_TT1_PostcodeChanged";
    String sUOTT1_To_UOTT4_PostcodeChanged = "UOTT1_To_UOTT4_PostcodeChanged";
    String sUOTT1_To_TT4_PostcodeChanged = "UOTT1_To_TT4_PostcodeChanged";
    String sUOTT4_To_NotUO_InSHBE_PostcodeChanged = "UOTT4_To_NotUO_InSHBE_PostcodeChanged";
    String sUOTT4_To_UOTT1_PostcodeChanged = "UOTT4_To_UOTT1_PostcodeChanged";
    String sUOTT4_To_TT1_PostcodeChanged = "UOTT4_To_TT1_PostcodeChanged";
    String sUOTT4_To_UOTT4_PostcodeChanged = "UOTT4_To_UOTT4_PostcodeChanged";
    String sUOTT4_To_TT4_PostcodeChanged = "UOTT4_To_TT4_PostcodeChanged";

    String sUO_To_LeftSHBEAtSomePoint = "UO_To_LeftSHBEAtSomePoint";
    String sUOTT1_To_LeftSHBE = "UOTT1_To_LeftSHBE";
    String sUOTT4_To_LeftSHBE = "UOTT4_To_LeftSHBE";
    String sUOTT3OrTT6_To_LeftSHBE = "UOTT3OrTT6_To_LeftSHBE";
    String sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE = "UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE";
    String sUOTT1_To_TT3OrTT6 = "UOTT1_To_TT3OrTT6";
    String sUOTT1_To_TT3OrTT6AtSomePoint = "UOTT1_To_TT3OrTT6AtSomePoint";
    String sUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999 = "UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999";
    String sUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint = "UOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint";
    String sUOTT4_To_TT3OrTT6 = "UOTT4_To_TT3OrTT6";
    String sUOTT4_To_TT3OrTT6AtSomePoint = "UOTT4_To_TT3OrTT6AtSomePoint";
    String sUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999 = "UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999";
    String sUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint = "UOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint";
    String sTT3OrTT6_To_UOTT1 = "TT3OrTT6_To_UOTT1";
    String sTT3OrTT6_To_UOTT4 = "TT3OrTT6_To_UOTT4";

    //String sUOTT1OrTT1_To_UOTT4 = "UOTT1OrTT1_To_UOTT4";
    String sUOTT1_To_UOTT4 = "UOTT1_To_UOTT4";
    String sTT1_To_UOTT4 = "TT1_To_UOTT4";
    String sTT1_To_UOTT4GettingDHP = "TT1_To_UOTT4GettingDHP";
    //String sUOTT4OrTT4_To_UOTT1 = "UOTT4OrTT4_To_UOTT1";
    //String sUOTT4OrTT4_To_UOTT1InArrears = "UOTT4OrTT4_To_UOTT1InArrears";
    //String sUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP = "UOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP";
    String sUOTT4_To_UOTT1 = "UOTT4_To_UOTT1";
    String sUOTT4_To_UOTT1InArrears = "UOTT4_To_UOTT1InArrears";
    String sUOTT4_To_UOTT1GettingDHP = "UOTT4_To_UOTT1GettingDHP";
    String sUOTT4_To_UOTT1InArrearsAndGettingDHP = "UOTT4_To_UOTT1InArrearsAndGettingDHP";
    String sTT4_To_UOTT1 = "TT4_To_UOTT1";
    String sTT4_To_UOTT1InArrears = "TT4_To_UOTT1InArrears";
    String sTT4_To_UOTT1GettingDHP = "TT4_To_UOTT1GettingDHP";
    String sTT4_To_UOTT1InArrearsAndGettingDHP = "TT4_To_UOTT1InArrearsAndGettingDHP";
    String sInArrearsAtSomePoint = "InArrearsAtSomePoint";
    String sDHPAtSomePoint = "DHPAtSomePoint";
    String sInArrearsAtSomePoint_And_DHPAtSomePoint = "InArrearsAtSomePoint_And_DHPAtSomePoint";

    String sTT1_To_UOTT1_PostcodeUnchanged = "TT1_To_UOTT1_PostcodeUnchanged";
    //String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1 = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1 = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4 = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4 = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8 = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9 = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months";
    String sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months = "TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months";
    String sTT4_To_UOTT4_PostcodeUnchanged = "TT4_To_UOTT4_PostcodeUnchanged";
    //String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1 = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1 = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4 = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4 = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OtTT7";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8 = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9 = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months";
    String sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months = "TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months";
    String sUOTT1_To_TT1_PostcodeUnchanged = "UOTT1_To_TT1_PostcodeUnchanged";
    //String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months";
    String sUOTT4_To_TT4_PostcodeUnchanged = "UOTT4_To_TT4_PostcodeUnchanged";
    //String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1 = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1 = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4 = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4 = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8 = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9 = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months";
    String sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months = "UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months";
    //String sUOTT1_To_TT1_PostcodeChanged = "UOTT1_To_TT1_PostcodeChanged";
    //String sUOTT1_To_UOTT1_PostcodeChanged = "UOTT1_To_UOTT1_PostcodeChanged";
    //String sUOTT4_To_TT4_PostcodeChanged = "UOTT4_To_TT4_PostcodeChanged";
    //String sUOTT4_To_UOTT4_PostcodeChanged = "UOTT4_To_UOTT4_PostcodeChanged";
    String sTT1_To_UOTT1_PostcodeChanged = "TT1_To_UOTT1_PostcodeChanged";
    String sTT4_To_UOTT4_PostcodeChanged = "TT4_To_UOTT4_PostcodeChanged";

    String sUO_To_LeftSHBETheVeryNextMonth = "UO_To_LeftSHBETheVeryNextMonth";
    //String sUO_To_LeftSHBEBetweenOneAndTwoMonths = "UO_To_LeftSHBEBetweenOneAndTwoMonths";
    //String sUO_To_LeftSHBEBetweenTwoAndThreeMonths = "UO_To_LeftSHBEBetweenTwoAndThreeMonths";
    String sUO_To_LeftSHBE_NotReturned = "UO_To_LeftSHBE_NotReturned";
    String sUOTT1_To_LeftSHBE_NotReturned = "UOTT1_To_LeftSHBE_NotReturned";
    String sUOTT4_To_LeftSHBE_NotReturned = "UOTT4_To_LeftSHBE_NotReturned";
    String sUOTT3OrTT6_To_LeftSHBE_NotReturned = "UOTT3OrTT6_To_LeftSHBE_NotReturned";
    String sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned = "UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned";
    String sUOTT1_To_LeftSHBE_ReturnedAsUOTT1 = "UOTT1_To_LeftSHBE_ReturnedAsUOTT1";
    String sUOTT1_To_LeftSHBE_ReturnedAsTT1 = "UOTT1_To_LeftSHBE_ReturnedAsTT1";
    String sUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6 = "UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6";
    String sUOTT1_To_LeftSHBE_ReturnedAsUOTT4 = "UOTT1_To_LeftSHBE_ReturnedAsUOTT4";
    String sUOTT1_To_LeftSHBE_ReturnedAsTT4 = "UOTT1_To_LeftSHBE_ReturnedAsTT4";
    String sUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7 = "UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7";
    String sUOTT1_To_LeftSHBE_ReturnedAsTT8 = "UOTT1_To_LeftSHBE_ReturnedAsTT8";
    String sUOTT1_To_LeftSHBE_ReturnedAsTT9 = "UOTT1_To_LeftSHBE_ReturnedAsTT9";
    String sUOTT4_To_LeftSHBE_ReturnedAsUOTT1 = "UOTT4_To_LeftSHBE_ReturnedAsUOTT1";
    String sUOTT4_To_LeftSHBE_ReturnedAsTT1 = "UOTT4_To_LeftSHBE_ReturnedAsTT1";
    String sUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6 = "UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6";
    String sUOTT4_To_LeftSHBE_ReturnedAsUOTT4 = "UOTT4_To_LeftSHBE_ReturnedAsUOTT4";
    String sUOTT4_To_LeftSHBE_ReturnedAsTT4 = "UOTT4_To_LeftSHBE_ReturnedAsTT4";
    String sUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7 = "UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7";
    String sUOTT4_To_LeftSHBE_ReturnedAsTT8 = "UOTT4_To_LeftSHBE_ReturnedAsTT8";
    String sUOTT4_To_LeftSHBE_ReturnedAsTT9 = "UOTT4_To_LeftSHBE_ReturnedAsTT9";
    String sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1 = "UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1";
    String sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1 = "UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1";
    String sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6 = "UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6";
    String sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4 = "UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4";
    String sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4 = "UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4";
    String sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7 = "UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7";
    String sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8 = "UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8";
    String sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9 = "UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9";
    String sUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint = "UOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint";
    String sUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint = "UOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint";
    String sUOTT1_To_TT3OrTT6BetweenOneAndTwoMonths = "UOTT1_To_TT3OrTT6BetweenOneAndTwoMonths";
    String sUOTT1_To_TT3OrTT6BetweenTwoAndThreeMonths = "UOTT1_To_TT3OrTT6BetweenTwoAndThreeMonths";
    String sUOTT4_To_TT3OrTT6BetweenOneAndTwoMonths = "UOTT4_To_TT3OrTT6BetweenOneAndTwoMonths";
    String sUOTT4_To_TT3OrTT6BetweenTwoAndThreeMonths = "UOTT4_To_TT3OrTT6BetweenTwoAndThreeMonths";
    String sTT3OrTT6_To_UOTT1AsNextTTIncludingBreaks = "TT3OrTT6_To_UOTT1AsNextTTIncludingBreaks";
    String sTT3OrTT6_To_UOTT4AsNextTTIncludingBreaks = "TT3OrTT6_To_UOTT4AsNextTTIncludingBreaks";

    String sTT1_To_UOTT1 = "TT1_To_UOTT1";
    String sTT1_To_UOTT1IncludingBreaks = "TT1_To_UOTT1IncludingBreaks";
    String sTT1_To_UOTT1_PostcodeChangedAfter1MonthButStillUOTT1 = "TT1_To_UOTT1_PostcodeChangedAfter1MonthButStillUOTT1";
    String sTT1_To_UOTT1_PostcodeChangedAfter2MonthsButStillUOTT1 = "TT1_To_UOTT1_PostcodeChangedAfter2MonthsButStillUOTT1";
    String sTT1_To_UOTT1_PostcodeChangedAfter3MonthsButStillUOTT1 = "TT1_To_UOTT1_PostcodeChangedAfter3MonthsButStillUOTT1";

    String sTT4_To_UOTT4 = "TT4_To_UOTT4";
    String sTT4_To_UOTT4IncludingBreaks = "TT4_To_UOTT4IncludingBreaks";
    String sTT4_To_UOTT4_PostcodeChangedAfter1MonthButStillUOTT4 = "TT4_To_UOTT4_PostcodeChangedAfter1MonthButStillUOTT4";
    String sTT4_To_UOTT4_PostcodeChangedAfter2MonthsButStillUOTT4 = "TT4_To_UOTT4_PostcodeChangedAfter2MonthsButStillUOTT4";
    String sTT4_To_UOTT4_PostcodeChangedAfter3MonthsButStillUOTT4 = "TT4_To_UOTT4_PostcodeChangedAfter3MonthsButStillUOTT4";

    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthButStillTT1 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthButStillTT1";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2MonthsButStillTT1 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2MonthsButStillTT1";
    String sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3MonthsButStillTT1 = "UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3MonthsButStillTT1";

    // For Aggregate Statistics
    String sTotal_DHP = "Total_DHP";
    String sTotalCount_DHP = "TotalCount_DHP";
    String sTotal_HBLossDueToUO = "Total_HBLossDueToUO";
    String sTotalCount_HBLossDueToUO = "TotalCount_HBLossDueToUO";
    String sMax_Arrears = "Max_Arrears";
    String sTotalCount_InArrears = "TotalCount_InArrears";
    String sTotalCount_InArrears0To10 = "TotalCount_InArrears0To10";
    String sTotalCount_InArrears10To100 = "TotalCount_InArrears10To100";
    String sTotalCount_InArrears100To500 = "TotalCount_InArrears100To500";
    String sTotalCount_InArrearsOver500 = "TotalCount_InArrearsOver500";
    String sTotalCount_UnderOccupancy = "TotalCount_UnderOccupancy";

    // For General Statistics
    String sUOClaimsRecievingDHP = "UOClaimsRecievingDHP";
    String sUOTT1ClaimsInRentArrearsAtSomePoint = "UOTT1ClaimsInRentArrearsAtSomePoint";
    String sUOTT1ClaimsInRentArrearsOver500AtSomePoint = "UOTT1ClaimsInRentArrearsOver500AtSomePoint";
    String sUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint = "UOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint";

    String sTotalCount_AlwaysUOTT1FromWhenStarted = "TotalCount_AlwaysUOTT1FromWhenStarted";
    String sTotalCount_AlwaysUOTT1FromStart = "TotalCount_AlwaysUOTT1FromStart";
    String sTotalCount_AlwaysUOTT1FromStartExceptWhenSuspended = "TotalCount_AlwaysUOTT1FromStartExceptWhenSuspended";

    String sTotalCount_AlwaysUOTT4FromWhenStarted = "TotalCount_AlwaysUOTT4FromWhenStarted";
    String sTotalCount_AlwaysUOTT4FromStart = "TotalCount_AlwaysUOTT4FromStart";
    String sTotalCount_AlwaysUOTT4FromStartExceptWhenSuspended = "TotalCount_AlwaysUOTT4FromStartExceptWhenSuspended";
//    String sTotalCount_ExistingSHBEClaimsThatBecameUOInApril2013 = "TotalCount_ExistingSHBEClaimsThatBecameUOInApril2013";
//    String sTotalCount_ExistingSHBEClaimsThatBecameUOAfterApril2013 = "TotalCount_ExistingSHBEClaimsThatBecameUOAfterApril2013";
//    String sTotalCount_ExistingSHBEClaimsThatBecameUOAfterChangePostcodeAndOrTT = "TotalCount_ExistingSHBEClaimsThatBecameUOAfterChangePostcodeAndOrTT";
//    String sTotalCount_ChangePostcodeAndOrTTToAvoidUO = "TotalCount_ChangePostcodeAndOrTToAvoidUO";
//    String sTotalCount_StayPutAndAvoidedUO = "TotalCount_StayPutAndAvoidedUO";
    // All
    String sTotalCount_ClaimsEffectedByUnderOccupancy = "TotalCount_ClaimsEffectedByUnderOccupancy";
    String sTotalCount_UniqueIndividualsEffectedByUnderOccupancy = "TotalCount_UniqueIndividualsEffectedByUnderOccupancy";
    String sTotalCount_UniqueDependentsAgedUnder10EffectedByUnderOccupancy = "TotalCount_UniqueDependentsAgedUnder10EffectedByUnderOccupancy";
    String sTotalCount_UniqueDependentsAgedOver10EffectedByUnderOccupancy = "TotalCount_UniqueDependentsAgedOver10EffectedByUnderOccupancy";
    String sTotalCount_UniqueDependentsEffectedByUnderOccupancy = "TotalCount_UniqueDependentsEffectedByUnderOccupancy";
    // Council
    String sTotalCount_CouncilClaimsEffectedByUnderOccupancy = "TotalCount_CouncilClaimsEffectedByUnderOccupancy";
    String sTotalCount_CouncilUniqueIndividualsEffectedByUnderOccupancy = "TotalCount_CouncilUniqueIndividualsEffectedByUnderOccupancy";
    String sTotalCount_CouncilUniqueClaimantsEffectedByUnderOccupancy = "TotalCount_CouncilUniqueClaimantsEffectedByUnderOccupancy";
    String sTotalCount_CouncilUniquePartnersEffectedByUnderOccupancy = "TotalCount_CouncilUniquePartnersEffectedByUnderOccupancy";
    String sTotalCount_CouncilDependentsAgedUnder10EffectedByUnderOccupancy = "TotalCount_CouncilDependentsAgedUnder10EffectedByUnderOccupancy";
    String sTotalCount_CouncilDependentsAgedOver10EffectedByUnderOccupancy = "TotalCount_CouncilDependentsAgedOver10EffectedByUnderOccupancy";
    String sTotalCount_CouncilUniqueNonDependentsEffectedByUnderOccupancy = "TotalCount_CouncilUniqueNonDependentsEffectedByUnderOccupancy";
    // RSL
    String sTotalCount_RSLClaimsEffectedByUnderOccupancy = "TotalCount_RSLClaimsEffectedByUnderOccupancy";
    String sTotalCount_RSLUniqueIndividualsEffectedByUnderOccupancy = "TotalCount_RSLUniqueIndividualsEffectedByUnderOccupancy";
    String sTotalCount_RSLUniqueClaimantsEffectedByUnderOccupancy = "TotalCount_RSLUniqueClaimantsEffectedByUnderOccupancy";
    String sTotalCount_RSLUniquePartnersEffectedByUnderOccupancy = "TotalCount_RSLUniquePartnersEffectedByUnderOccupancy";
    String sTotalCount_RSLDependentsUnder10EffectedByUnderOccupancy = "TotalCount_RSLDependentsAgedUnder10EffectedByUnderOccupancy";
    String sTotalCount_RSLDependentsOver10EffectedByUnderOccupancy = "TotalCount_RSLDependentsAgedOver10EffectedByUnderOccupancy";
    String sTotalCount_RSLUniqueNonDependentsEffectedByUnderOccupancy = "TotalCount_RSLUniqueNonDependentsEffectedByUnderOccupancy";

    String sAverageHouseholdSizeOfThoseUOTT1AlwaysFromStart = "AverageHouseholdSizeOfThoseUOTT1AlwaysFromStart";
    String sAverageHouseholdSizeOfThoseUOTT4AlwaysFromStart = "AverageHouseholdSizeOfThoseUOTT4AlwaysFromStart";

    HashMap<String, String> generalStatisticDescriptions;

    // TimeStatistics
    String sTotalCount_CumulativeUniqueClaims = "TotalCount_cumulativeUniqueClaims";
    String sTotalCount_UOClaims = "TotalCounts_UOClaims";
    String sTotalCount_UOCouncilClaims = "TotalCounts_UOCouncilClaims";
    String sTotalCount_UORSLClaims = "TotalCounts_UORSLClaims";
    String sTotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE = "TotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE";
    String sTotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO = "TotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO";
    String sTotalCount_UOClaimsCouncil = "TotalCount_UOClaimsCouncil";
    String sTotalCount_UOClaimsRSL = "TotalCount_UOClaimsRSL";

    String sAverageHouseholdSizeCouncilSHBE = "AverageHouseholdSizeCouncilSHBE";
    String sAverageHouseholdSizeCouncilUO = "AverageHouseholdSizeCouncilUO";
    String sAverageHouseholdSizeRSLSHBE = "AverageHouseholdSizeRSLSHBE";
    String sAverageHouseholdSizeRSLUO = "AverageHouseholdSizeRSLUO";
    String sTotalHouseholdSizeCouncilSHBE = "sTotalHouseholdSizeCouncilSHBE";
    String sTotalHouseholdSizeCouncilUO = "sTotalHouseholdSizeCouncilUO";
    String sTotalHouseholdSizeRSLSHBE = "sTotalHouseholdSizeRSLSHBE";
    String sTotalHouseholdSizeRSLUO = "sTotalHouseholdSizeRSLUO";
    String sTotalHouseholdSizeExcludingPartnersCouncilSHBE = "sTotalHouseholdSizeExcludingPartnersCouncilSHBE";
    String sTotalHouseholdSizeExcludingPartnersCouncilUO = "sTotalHouseholdSizeExcludingPartnersCouncilUO";
    String sTotalHouseholdSizeExcludingPartnersRSLSHBE = "sTotalHouseholdSizeExcludingPartnersRSLSHBE";
    String sTotalHouseholdSizeExcludingPartnersRSLUO = "sTotalHouseholdSizeExcludingPartnersRSLUO";
//    String sTotalAggregateHouseholdSize_UOClaims = "TotalAggregateHouseholdSize_UOClaims";
//    String sAverageHouseholdSize_UOClaims = "TotalAverageHouseholdSize_UOClaims";
//    String sTotalAggregateHouseholdSize_UOClaimsCouncil = "TotalAggregateHouseholdSize_UOClaimsCouncil";
//    String sTotalAverageHouseholdSize_UOClaimsCouncil = "TotalAverageHouseholdSize_UOClaimsCouncil";
//    String sTotalAggregateHouseholdSize_UOClaimsRSL = "TotalAggregateHouseholdSize_UOClaimsRSL";
//    String sTotalAverageHouseholdSize_UOClaimsRSL = "TotalAverageHouseholdSize_UOClaimsRSL";

    protected HashMap<String, String> getGeneralStatisticDescriptions(
            String startMonth,
            String startYear,
            String endMonth,
            String endYear) {
        String dates;
        dates = startMonth + " " + startYear + " to " + endMonth + " " + endYear;
        generalStatisticDescriptions = new HashMap<>();
        generalStatisticDescriptions.put(
                sTotalCount_AlwaysUOTT1FromStart,
                "\"Total count of claims that were always TT1 and in the "
                + "under-occupancy from " + dates
                + " (includes claims that were not in under-occupancy "
                + "for months when their Housing Benefit Claim Status was In "
                + "Suspension).\"");
        generalStatisticDescriptions.put(
                sTotalCount_AlwaysUOTT1FromWhenStarted,
                "\"Total count of claims that were always TT1 and in the "
                + "under-occupancy data once they started being claims, but "
                + " only became claims after " + startMonth + " " + startYear
                + " (includes claims that were not in under-occupancy "
                + "for months when their Housing Benefit Claim Status was In "
                + "Suspension).\"");
        generalStatisticDescriptions.put(
                sTotalCount_AlwaysUOTT4FromStart,
                "\"Total count of claims that were always TT4 and in the "
                + "under-occupancy from " + dates
                + " (includes claims that were not in under-occupancy "
                + "for months when their Housing Benefit Claim Status was In "
                + "Suspension).\"");
        generalStatisticDescriptions.put(
                sTotalCount_AlwaysUOTT4FromWhenStarted,
                "\"Total count of claims that were always TT4 and in the "
                + "under-occupancy data once they started being claims, but "
                + " only became claims after " + startMonth + " " + startYear
                + " (includes claims that were not in under-occupancy "
                + "for months when their Housing Benefit Claim Status was In "
                + "Suspension).\"");
        generalStatisticDescriptions.put(
                sAverageHouseholdSizeOfThoseUOTT1AlwaysFromStart,
                "\"Average Household Size of claims that were always TT1 in under-"
                + "occupancy from " + dates + " in " + endMonth + " "
                + endYear + ".\"");
        generalStatisticDescriptions.put(
                sAverageHouseholdSizeOfThoseUOTT4AlwaysFromStart,
                "\"Average Household Size of claims that were always TT4 in under-"
                + "occupancy from " + dates + " in " + endMonth + " "
                + endYear + ".\"");
        // All
        generalStatisticDescriptions.put(
                sTotalCount_ClaimsEffectedByUnderOccupancy,
                "\"Total count of claims that were effected by under-occupancy "
                + "(claims can have different Tenancy Types at different times).\"");
        generalStatisticDescriptions.put(
                sTotalCount_UniqueIndividualsEffectedByUnderOccupancy,
                "\"Total count of unique individuals effected by under-occupancy. "
                + "(Uniqueness is based on date of birth DoB and National "
                + "Insurance Number (NINO). Those Dependents in the same "
                + "household with the same NINO and the same DOB are "
                + "distinguished, but where there are multiple Partners for a "
                + "claim, only one Partner is accounted for. If a child has "
                + "become 10 years of age, then they are not counted twice in "
                + "this metric.\"");
        generalStatisticDescriptions.put(sTotalCount_UniqueDependentsAgedUnder10EffectedByUnderOccupancy,
                "\"Total count of unique Dependents aged under 10"
                + " effected by under-occupancy.\"");
        generalStatisticDescriptions.put(sTotalCount_UniqueDependentsAgedOver10EffectedByUnderOccupancy,
                "\"Total count of unique Dependents aged over 10"
                + " effected by under-occupancy.\"");
        generalStatisticDescriptions.put(sTotalCount_UniqueDependentsEffectedByUnderOccupancy,
                "\"Total count of unique Dependents effected by under-occupancy.\"");

        // Council
        generalStatisticDescriptions.put(
                sTotalCount_CouncilClaimsEffectedByUnderOccupancy,
                "\"Total count of claims effected by under-occupancy at some time "
                + "between " + dates + " and in Council tenancies.\"");
        generalStatisticDescriptions.put(
                sTotalCount_CouncilUniqueIndividualsEffectedByUnderOccupancy,
                "\"Total count of unique individuals effected by under-occupancy "
                + "and in Council tenancies.\"");
        generalStatisticDescriptions.put(
                sTotalCount_CouncilUniqueClaimantsEffectedByUnderOccupancy,
                "\"Total count of unique claimants effected by under-occupancy "
                + "and in Council tenancies.\"");
        generalStatisticDescriptions.put(
                sTotalCount_CouncilUniquePartnersEffectedByUnderOccupancy,
                "\"Total count of unique partners effected by under-occupancy "
                + "and in Council tenancies (this only deals with main "
                + "partners, not for all partners in claims where there are "
                + "multiple partners).\"");
        generalStatisticDescriptions.put(sTotalCount_CouncilDependentsAgedUnder10EffectedByUnderOccupancy,
                "\"Total count of unique Dependents aged under 10 effected by under-occupancy "
                + "in Council tenancies.\"");
        generalStatisticDescriptions.put(sTotalCount_CouncilDependentsAgedOver10EffectedByUnderOccupancy,
                "\"Total count of unique Dependents aged over 10 effected by under-occupancy "
                + "in Council tenancies.\"");
        generalStatisticDescriptions.put(
                sTotalCount_CouncilUniqueNonDependentsEffectedByUnderOccupancy,
                "\"Total count of unique NonDependents effected by under-"
                + "occupancy in Council tenancies.\"");
        // RSL
        generalStatisticDescriptions.put(
                sTotalCount_RSLClaimsEffectedByUnderOccupancy,
                "\"Total count of claims effected by under-occupancy at  "
                + "some time between " + dates + " and in Registered Social "
                + "Landlord tenancies.\"");
        generalStatisticDescriptions.put(
                sTotalCount_RSLUniqueIndividualsEffectedByUnderOccupancy,
                "\"Total count of unique individuals effected by under-occupancy "
                + "and in Registered Social Landlord tenancies.\"");
        generalStatisticDescriptions.put(
                sTotalCount_RSLUniqueClaimantsEffectedByUnderOccupancy,
                "\"Total count of unique claimants effected by under-occupancy "
                + "and in Registered Social Landlord tenancies.\"");
        generalStatisticDescriptions.put(
                sTotalCount_RSLUniquePartnersEffectedByUnderOccupancy,
                "\"Total count of unique partners effected by under-occupancy "
                + "and in Registered Social Landlord tenancies (this only "
                + "deals with main partners, not for all partners in claims "
                + "where there are multiple partners).\"");
        generalStatisticDescriptions.put(
                sTotalCount_RSLDependentsUnder10EffectedByUnderOccupancy,
                "\"Total count of unique Dependents aged under 10 effected by under-occupancy "
                + "in RSL tenancies.\"");
        generalStatisticDescriptions.put(
                sTotalCount_RSLDependentsOver10EffectedByUnderOccupancy,
                "\"Total count of unique Dependents aged over 10 effected by under-occupancy "
                + "in RSL tenancies.\"");
        generalStatisticDescriptions.put(
                sTotalCount_RSLUniqueNonDependentsEffectedByUnderOccupancy,
                "\"Total count of unique NonDependents effected by under-"
                + "occupancy in Registered Social Landlord tenancies.\"");

        generalStatisticDescriptions.put(sNotUOInApril2013ThenUOAndUOInLatestMonth,
                "\"Total count of under-occupancy claims that were not "
                + "under-occupancy claims in April 2013, but were "
                + "still under-occupancy claims in the latest month.\"");
        generalStatisticDescriptions.put(sPercentageOfUO_ReceivingDHPInLatestMonth,
                "\"Percentage of under-occupancy claims receiving a weekly "
                + "Discretionary Housing Payment for Housing Benefit in the latest month.\"");
        generalStatisticDescriptions.put(sPercentageOfCouncilUO_InArrearsInLatestMonth,
                "\"Percentage of Council under-occupancy claims in arrears in the latest month.\"");
        generalStatisticDescriptions.put(sPercentageOfCouncilUO_InArrears0To10InLatestMonth,
                "\"Percentage of Council under-occupancy claims in arrears 0 to 10 in the latest month.\"");
        generalStatisticDescriptions.put(sPercentageOfCouncilUO_InArrears10To100InLatestMonth,
                "\"Percentage of Council under-occupancy claims in arrears 10 to 100 in the latest month.\"");
        generalStatisticDescriptions.put(sPercentageOfCouncilUO_InArrears100To500InLatestMonth,
                "\"Percentage of Council under-occupancy claims in arrears 100 to 500 in the latest month.\"");
        generalStatisticDescriptions.put(sPercentageOfCouncilUO_InArrearsOver500InLatestMonth,
                "\"Percentage of Council under-occupancy claims in arrears Over 500 in the latest month.\"");
        generalStatisticDescriptions.put(sPercentageOfCouncilUO_InArrearsAndReceivingDHPInLatestMonth,
                "\"Percentage of Council under-occupancy claims in arrears and "
                + "receiving a weekly Discretionary Housing Payment for Housing "
                + "Benefit in the latest month.\"");

        generalStatisticDescriptions.put(sPercentageOfUO_ReceivingDHPInLatestMonthThatWereUOInApril2013,
                "\"Percentage of under-occupancy claims (that were under-"
                + "occupancy claims in April 2013) receiving a weekly "
                + "discretionary Payment for Housing Benefit in the latest month.\"");
        generalStatisticDescriptions.put(sPercentageOfCouncilUO_InArrearsInLatestMonthThatWereUOInApril2013,
                "\"Percentage of Council under-occupancy claims (that were under-"
                + "occupancy claims in April 2013) in arrears in the latest month.\"");
        generalStatisticDescriptions.put(sPercentageOfCouncilUO_InArrears0To10InLatestMonthThatWereUOInApril2013,
                "\"Percentage of Council under-occupancy claims (that were under-"
                + "occupancy claims in April 2013) in arrears 0 to 10 in the latest month.\"");
        generalStatisticDescriptions.put(sPercentageOfCouncilUO_InArrears10To100InLatestMonthThatWereUOInApril2013,
                "\"Percentage of Council under-occupancy claims (that were under-"
                + "occupancy claims in April 2013) in arrears 10 to 100 in the latest month.\"");
        generalStatisticDescriptions.put(sPercentageOfCouncilUO_InArrears100To500InLatestMonthThatWereUOInApril2013,
                "\"Percentage of Council under-occupancy claims (that were under-"
                + "occupancy claims in April 2013) in arrears 100 to 500 in the latest month.\"");
        generalStatisticDescriptions.put(sPercentageOfCouncilUO_InArrearsOver500InLatestMonthThatWereUOInApril2013,
                "\"Percentage of Council under-occupancy claims (that were under-"
                + "occupancy claims in April 2013) in arrears Over 500 in the latest month.\"");
        generalStatisticDescriptions.put(sPercentageOfCouncilUO_InArrearsAndReceivingDHPInLatestMonthThatWereUOInApril2013,
                "\"Percentage of Council under-occupancy claims (that were under-"
                + "occupancy claims in April 2013) in arrears and receiving a "
                + "weekly discretionary Payment for Housing Benefit in the latest month.\"");

        // LeftSHBE
        generalStatisticDescriptions.put(
                sUO_To_LeftSHBEAtSomePoint,
                "\"Total count of under-occupancy claims that have left SHBE at "
                + "some time between " + dates + " after becoming under-"
                + "occupied.\"");
        generalStatisticDescriptions.put(sUO_To_LeftSHBETheVeryNextMonth,
                "\"Total count of UO_To_LeftSHBE claims.\"");
//        generalStatisticDescriptions.put(
//                sUO_To_LeftSHBEBetweenOneAndTwoMonths,
//                "\"Total count of UO_To_LeftSHBE (not the next month, but the "
//                + "month after) claims.\"");
//        generalStatisticDescriptions.put(
//                sUO_To_LeftSHBEBetweenTwoAndThreeMonths,
//                "\"Total count of UO_To_LeftSHBE (not the next month, or the "
//                + "month after, but the month after that) claims.\"");
        generalStatisticDescriptions.put(
                sUO_To_LeftSHBE_NotReturned,
                "\"Total count of UO_To_LeftSHBE and not returned claims "
                + "(this may be bias and much of the count might be for "
                + "those leaving in the last few months and which return "
                + "at a later date).\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_NotReturned,
                "\"Total count of UOTT1_To_LeftSHBE and not returned claims "
                + "(again this may be bias and much of the count might "
                + "be for those leaving in the last few months and which return "
                + "at a later date).\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_NotReturned,
                "\"Total count of UOTT4_To_LeftSHBE and not returned claims "
                + "(again this may be bias and much of the count might "
                + "be for those leaving in the last months and which return "
                + "at a later date).\"");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_NotReturned,
                "\"Total count of UOTT3OrTT6_To_LeftSHBE and not returned claims "
                + "(again this may be bias and much of the count might "
                + "be for those leaving in the last months and which return "
                + "at a later date).\"");
        generalStatisticDescriptions.put(
                sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned,
                "\"Total count of UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE and not "
                + "returned claims "
                + "(again this may be bias and much of the count might "
                + "be for those leaving in the last months and which return "
                + "at a later date).\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAsUOTT1,
                "\"Total count of UOTT1_To_LeftSHBE claims that next returned "
                + "to the SHBE as UOTT1 "
                + "(unique claims: if this happens more than once for "
                + "the same claim, it is just counted once).\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT1,
                "\"Total count of UOTT1_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT1 "
                + "(unique claims: if this happens more than once for "
                + "the same claim, it is just counted once).\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
                "\"Total count of UOTT1_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT3OrTT6.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAsUOTT4,
                "\"Total count of UOTT1_To_LeftSHBE claims that next returned "
                + "to the SHBE as UOTT4.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT4,
                "\"Total count of UOTT1_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT4.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
                "\"Total count of UOTT1_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT5OrTT7.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT8,
                "\"Total count of UOTT1_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT8.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT9,
                "\"Total count of UOTT1_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT9.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAsUOTT1,
                "\"Total count of UOTT4_To_LeftSHBE claims that next returned "
                + "to the SHBE as UOTT1.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT1,
                "\"Total count of UOTT4_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT1.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6,
                "\"Total count of UOTT4_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT3OrTT6.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAsUOTT4,
                "\"Total count of UOTT4_To_LeftSHBE claims that next returned "
                + "to the SHBE as UOTT4.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT4,
                "\"Total count of UOTT4_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT4.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7,
                "\"Total count of UOTT4_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT5OrTT7.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT8,
                "\"Total count of UOTT4_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT8.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT9,
                "\"Total count of UOTT4_To_LeftSHBE claims that next returned "
                + "to the SHBE as TT9.\"");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1,
                "\"Total count of UOTT3OrTT6_To_LeftSHBE claims that next "
                + "returned to the SHBE as UOTT1.\"");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1,
                "\"Total count of UOTT3OrTT6_To_LeftSHBE claims that next "
                + "returned to the SHBE as TT1.\"");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6,
                "\"Total count of UOTT3OrTT6_To_LeftSHBE claims that next "
                + "returned to the SHBE as TT3OrTT6.\"");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4,
                "\"Total count of UOTT3OrTT6_To_LeftSHBE claims that next "
                + "returned to the SHBE as UOTT4.\"");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4,
                "\"Total count of UOTT3OrTT6_To_LeftSHBE claims that next "
                + "returned to the SHBE as TT4.\"");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7,
                "\"Total count of UOTT3OrTT6_To_LeftSHBE claims that next "
                + "returned to the SHBE as TT5OrTT7.\"");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8,
                "\"Total count of UOTT3OrTT6_To_LeftSHBE claims that next "
                + "returned to the SHBE as TT8.\"");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9,
                "\"Total count of UOTT3OrTT6_To_LeftSHBE claims that next "
                + "returned to the SHBE as TT9.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                "\"Total count of UOTT1_To_LeftSHBE claims that returned and "
                + "became under-occupancy again at some time between "
                + dates + ".\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                "\"Total count of UOTT4_To_LeftSHBE claims that returned and "
                + "became under-occupancy again at  some time between "
                + dates + ".\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_LeftSHBE,
                "\"Total count of UOTT1_To_LeftSHBE claims.\"");
//        generalStatisticDescriptions.put(
//                sUOTT1_To_LeftSHBEReturnedAsTT1orTT4,
//                "\"UOTT1_To_LeftSHBEReturnedAsTT1orTT4");
//        generalStatisticDescriptions.put(
//                sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
//                "\"UOTT1_To_LeftSHBEReturnedAsTT3OrTT6");
        generalStatisticDescriptions.put(
                sUOTT4_To_LeftSHBE,
                "\"Total count of UOTT4_To_LeftSHBE claims.\"");
//        generalStatisticDescriptions.put(
//                sUOTT4_To_LeftSHBEReturnedAsTT1orTT4,
//                "\"UOTT4_To_LeftSHBEReturnedAsTT1orTT4");
//        generalStatisticDescriptions.put(
//                sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
//                "\"UOTT4_To_LeftSHBEReturnedAsTT3OrTT6");
        generalStatisticDescriptions.put(
                sUOTT3OrTT6_To_LeftSHBE,
                "\"Total count of UOTT3OrTT6_To_LeftSHBE claims.\"");
        generalStatisticDescriptions.put(
                sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE,
                "\"Total count of NotTT1OrTT3OrTT4OrTT6_To_LeftSHBE claims.\"");

        generalStatisticDescriptions.put(
                sUO_NotUO_UO,
                "\"Total count of sUO_NotUO_UO claims.\"");
        generalStatisticDescriptions.put(
                sUO_NotUO_UO_NotUO_UO,
                "\"Total count of sUO_NotUO_UO_NotUO_UO claims.\"");
        generalStatisticDescriptions.put(
                sUO_NotUO_UO_NotUO_UO_NotUO_UO,
                "\"Total count of sUO_NotUO_UO_NotUO_UO_NotUO_UO claims.\"");
        generalStatisticDescriptions.put(
                sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO,
                "\"Total count of sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO claims "
                + "(including claims with even more _NotUO_UO occurrences).\"");

        generalStatisticDescriptions.put(
                sUOTT1_To_NotUO_InSHBE_PostcodeChanged,
                "\"Total count of UOTT1_To_NotUO_InSHBE_PostcodeChanged claims "
                + "(only postcode changes where both the origin and "
                + "destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeChanged,
                "\"Total count of UOTT1_To_TT1_PostcodeChanged claims "
                + "(only postcode changes where both the origin and "
                + "destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT4_PostcodeChanged,
                "\"Total count of UOTT1_To_TT4_PostcodeChanged claims "
                + "(only postcode changes where both the origin and "
                + "destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_NotUO_InSHBE_PostcodeChanged,
                "\"Total count of UOTT4_To_NotUO_InSHBE_PostcodeChanged claims "
                + "(only postcode changes where both the origin and "
                + "destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT1_PostcodeChanged,
                "\"Total count of UOTT4_To_TT1_PostcodeChanged claims "
                + "(only postcode changes where both the origin and "
                + "destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeChanged,
                "\"Total count of UOTT4_To_TT4_PostcodeChanged claims "
                + "(only postcode changes where both the origin and "
                + "destination postcodes validate are included).\"");

        // UOTT1_To_TT3OrTT6
        generalStatisticDescriptions.put(
                sUOTT1_To_TT3OrTT6,
                "\"Total count of UOTT1_To_TT3OrTT6 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT3OrTT6BetweenOneAndTwoMonths,
                "\"Total count of UOTT1_To_TT3OrTT6BetweenOneAndTwoMonths claims.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT3OrTT6BetweenTwoAndThreeMonths,
                "\"Total count of UOTT1_To_TT3OrTT6BetweenTwoAndThreeMonths claims.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT3OrTT6AtSomePoint,
                "\"Total count of under-occupied TT1 claims that became TT3OrTT6 at  "
                + "some time between " + dates + ".\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                "\"Total count of under-occupied TT1 claims that became TT3OrTT6 "
                + "in the next TT change ignoring those times they "
                + "came out of the SHBE if indeed they have.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint,
                "\"Total count of under-occupied TT1 claims that became TT3OrTT6 "
                + "in the next TT change ignoring those times they "
                + "came out of the SHBE if indeed they have and which returned "
                + "at some later point to be either TT1 or UOTT1.\"");

        // UOTT4_To_TT3OrTT6
        generalStatisticDescriptions.put(
                sUOTT4_To_TT3OrTT6,
                "\"Total count of UOTT4_To_TT3OrTT6 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT3OrTT6BetweenOneAndTwoMonths,
                "\"Total count of UOTT4_To_TT3OrTT6BetweenOneAndTwoMonths claims.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT3OrTT6BetweenTwoAndThreeMonths,
                "\"Total count of UOTT4_To_TT3OrTT6BetweenTwoAndThreeMonths claims.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT3OrTT6AtSomePoint,
                "\"Total count of under-occupied TT4 claims that became TT3OrTT6 at "
                + "some time between " + dates + ".\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                "\"Total count of under-occupied TT4 claims that became TT3OrTT6 "
                + "in the next TT change ignoring those times they "
                + "came out of the SHBE if indeed they have.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint,
                "\"Total count of under-occupied TT4 claims that became TT3OrTT6 "
                + "in the next TT change ignoring those times they "
                + "came out of the SHBE if indeed they have and which returned "
                + "at some later point to be either TT4 or UOTT4.\"");

        // TT3OrTT6_To_UOTT1
        generalStatisticDescriptions.put(
                sTT3OrTT6_To_UOTT1,
                "\"Total count of TT3OrTT6_To_UOTT1 claims.\"");
        generalStatisticDescriptions.put(
                sTT3OrTT6_To_UOTT1AsNextTTIncludingBreaks,
                "\"Total count of TT3OrTT6_To_UOTT1AsNextTTIncludingBreaks claims.\"");
        // TT3OrTT6_To_UOTT4        
        generalStatisticDescriptions.put(
                sTT3OrTT6_To_UOTT4,
                "\"Total count of TT3OrTT6_To_UOTT4 claims.\"");
        generalStatisticDescriptions.put(
                sTT3OrTT6_To_UOTT4AsNextTTIncludingBreaks,
                "\"Total count of TT3OrTT6_To_UOTT4AsNextTTIncludingBreaks claims.\"");
        // UOTT1_To_TT1
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchanged,
                "\"Total count of UOTT1_To_TT1_PostcodeUnchanged claims.\"");
//        generalStatisticDescriptions.put(
//                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
//                "\"Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month claims.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                "\"Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                "\"Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                "\"Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                "\"Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                "\"Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                "\"Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                "\"Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                "\"Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
                "\"Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months claims.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months,
                "\"Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months claims.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthButStillTT1,
                "\"Total count of UOTT1_To_TT1_PostcodeChangedAfter1MonthButStillTT1 separate from -999 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2MonthsButStillTT1,
                "\"Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2MonthsButStillTT1 separate from -999 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3MonthsButStillTT1,
                "\"Total count of UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3MonthsButStillTT1 separate from -999 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT1_To_TT1_PostcodeChanged,
                "\"Total count of UOTT1_To_TT1_PostcodeChanged claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        // UOTT4_To_TT4
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchanged,
                "\"Total count of UOTT4_To_TT4_PostcodeUnchanged claims.\"");
//        generalStatisticDescriptions.put(
//                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
//                "\"Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month claims.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                "\"Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                "\"Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                "\"Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                "\"Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                "\"Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                "\"Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                "\"Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                "\"Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9 claims.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
                "\"Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months claims.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months,
                "\"Total count of UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months claims.\"");
        generalStatisticDescriptions.put(
                sUOTT4_To_TT4_PostcodeChanged,
                "\"Total count of UOTT4_To_TT4_PostcodeChanged claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        // TT1_To_UOTT1        
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1,
                "\"Total count of TT1_To_UOTT1 claims.\"");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1IncludingBreaks,
                "\"Total count of TT1_To_UOTT1 including breaks claims.\"");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchanged,
                "\"Total count of TT1_To_UOTT1_PostcodeUnchanged claims.\"");
//        generalStatisticDescriptions.put(
//                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
//                "\"Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month claims "
//                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                "\"Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                "\"Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                "\"Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                "\"Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                "\"Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                "\"Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                "\"Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                "\"Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
                "\"Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months,
                "\"Total count of TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(sTT1_To_UOTT1_PostcodeChangedAfter1MonthButStillUOTT1,
                "\"Total count of TT1_To_UOTT1_PostcodeChangedAfter1MonthButStillTT1UO claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(sTT1_To_UOTT1_PostcodeChangedAfter2MonthsButStillUOTT1,
                "\"Total count of TT1_To_UOTT1_PostcodeChangedAfter2MonthsButStillTT1UO claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(sTT1_To_UOTT1_PostcodeChangedAfter3MonthsButStillUOTT1,
                "\"Total count of TT1_To_UOTT1_PostcodeChangedAfter3MonthsButStillTT1UO claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT1_To_UOTT1_PostcodeChanged,
                "\"Total count of TT1_To_UOTT1_PostcodeChanged claims "
                + "(includes corrections of postcodes) "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");

        generalStatisticDescriptions.put(
                sUOTT1_To_UOTT1_PostcodeChanged,
                "\"Total count of UOTT1_To_UOTT1_PostcodeChanged claims "
                + "(includes corrections of postcodes) "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");

        // TT1_To_UOTT4
        //generalStatisticDescriptions.put(sUOTT1OrTT1_To_UOTT4,
        //        "Total count of UOTT1OrTT1_To_UOTT4 claims.\"");
        generalStatisticDescriptions.put(sUOTT1_To_UOTT4,
                "\"Total count of UOTT1_To_UOTT4 claims.\"");
        generalStatisticDescriptions.put(sTT1_To_UOTT4,
                "\"Total count of TT1_To_UOTT4 claims.\"");
        generalStatisticDescriptions.put(sTT1_To_UOTT4GettingDHP,
                "\"Total count of TT1_To_UOTT4 claims receiving "
                + "Housing Benefit Discretionary Payment "
                + "some time between " + dates + ".\"");

        // TT4_To_UOTT4
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4,
                "\"Total count of TT4_To_UOTT4 claims.\"");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4IncludingBreaks,
                "\"Total count of TT4_To_UOTT4 including breaks claims.\"");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchanged,
                "\"Total count of TT4_To_UOTT4_PostcodeUnchanged claims.\"");
//        generalStatisticDescriptions.put(
//                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
//                "\"Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month claims "
//                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                "\"Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                "\"Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                "\"Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                "\"Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                "\"Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                "\"Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
                "\"Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months,
                "\"Total count of TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeChangedAfter1MonthButStillUOTT4,
                "\"Total count of TT4_To_UOTT4_PostcodeChangedAfter1MonthButStillUOTT4 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeChangedAfter2MonthsButStillUOTT4,
                "\"Total count of TT4_To_UOTT4_PostcodeChangedAfter2MonthsButStillUOTT4 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeChangedAfter3MonthsButStillUOTT4,
                "\"Total count of TT4_To_UOTT4_PostcodeChangedAfter3MonthsButStillUOTT4 claims "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");
        generalStatisticDescriptions.put(
                sTT4_To_UOTT4_PostcodeChanged,
                "\"Total count of TT4_To_UOTT4_PostcodeChanged claims "
                + "(includes corrections of postcodes) "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");

        generalStatisticDescriptions.put(
                sUOTT4_To_UOTT4_PostcodeChanged,
                "\"Total count of UOTT4_To_UOTT4_PostcodeChanged claims "
                + "(includes corrections of postcodes) "
                + "(only postcode changes where both the origin and destination postcodes validate are included).\"");

        // TT4_To_UOTT1
        //generalStatisticDescriptions.put(sUOTT4OrTT4_To_UOTT1,
        //        "Total count of UOTT4OrTT4_To_UOTT1 claims.\"");
        //generalStatisticDescriptions.put(sUOTT4OrTT4_To_UOTT1InArrears,
        //        "Total count of UOTT4OrTT4_To_UOTT1 claims in arrears at  "
        //        + "some time between " + dates + ".\"");
        //generalStatisticDescriptions.put(sUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
        //        "Total count of UOTT4OrTT4_To_UOTT1 claims simultaneously in "
        //        + "arrears and receiving Housing Benefit Discretionary "
        //        + "Payment at  "
        //        + "some time between " + dates + ".\"");
        generalStatisticDescriptions.put(sUOTT4_To_UOTT1,
                "\"Total count of UOTT4_To_UOTT1 claims.\"");
        generalStatisticDescriptions.put(sUOTT4_To_UOTT1InArrears,
                "\"Total count of UOTT4_To_UOTT1 claims in arrears "
                + "some time between " + dates + ".\"");
        generalStatisticDescriptions.put(sUOTT4_To_UOTT1GettingDHP,
                "\"Total count of UOTT4_To_UOTT1 claims receiving "
                + "Housing Benefit Discretionary Payment "
                + "some time between " + dates + ".\"");
        generalStatisticDescriptions.put(sUOTT4_To_UOTT1InArrearsAndGettingDHP,
                "\"Total count of UOTT4_To_UOTT1 claims simultaneously in "
                + "arrears and receiving Housing Benefit Discretionary "
                + "Payment "
                + "some time between " + dates + ".\"");
        generalStatisticDescriptions.put(sTT4_To_UOTT1,
                "\"Total count of TT4_To_UOTT1 claims.\"");
        generalStatisticDescriptions.put(sTT4_To_UOTT1InArrears,
                "\"Total count of UOTT4OrTT4_To_UOTT1 claims in arrears  "
                + "some time between " + dates + ".\"");
        generalStatisticDescriptions.put(sTT4_To_UOTT1GettingDHP,
                "\"Total count of TT4_To_UOTT1 claims receiving "
                + "Housing Benefit Discretionary Payment "
                + "some time between " + dates + ".\"");
        generalStatisticDescriptions.put(sTT4_To_UOTT1InArrearsAndGettingDHP,
                "\"Total count of UOTT4OrTT4_To_UOTT1 claims simultaneously in "
                + "arrears and receiving Housing Benefit Discretionary "
                + "Payment "
                + "some time between " + dates + ".\"");
        generalStatisticDescriptions.put(sInArrearsAtSomePoint_And_DHPAtSomePoint,
                "\"Claims in arrears at some time between " + dates + " and also "
                + "receiving a Housing Benefit Discretionary Payment "
                + "some time between " + dates + ".\"");
        generalStatisticDescriptions.put(
                sUOClaimsRecievingDHP,
                "\"Total count of claims receiving Housing Benefit Discretionary "
                + "Payment some time between " + dates + ".\"");
        generalStatisticDescriptions.put(
                sUOTT1ClaimsInRentArrearsAtSomePoint,
                "\"Total count of under-occupied TT1 claims in rent arrears at "
                + "some time between " + dates + ".\"");
        generalStatisticDescriptions.put(
                sUOTT1ClaimsInRentArrearsOver500AtSomePoint,
                "\"Total count of under-occupied TT1 claims in rent arrears of "
                + "more than 500 at some time between " + dates + ".\"");
        generalStatisticDescriptions.put(
                sUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint,
                "\"Total count of under-occupied TT1 claims in rent arrears and "
                + "receiving DHP simultaneously some time between " + dates + ".\"");
        generalStatisticDescriptions.put(
                sPermanantlyLeftUOButRemainedInSHBE,
                "\"Total count of under-occupied claims that stopped being "
                + "under-occupied and "
                + " which have not returned to under-occupancy.\"");
        generalStatisticDescriptions.put(
                sPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged,
                "\"Total count of under-occupied claims that stopped being "
                + "under-occupied and changed postcode and "
                + " which have not returned to under-occupancy.\"");
        generalStatisticDescriptions.put(
                sPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased,
                "\"Total count of under-occupied claims that stopped being "
                + "under-occupied because of an increase in household size and"
                + " which have not returned to under-occupancy.\"");
        return generalStatisticDescriptions;
    }

    private void initString() {
        sTT_ = sTT + DW_Strings.symbol_underscore;

        sTravellers = "_a_Travellers"; // Letter_ added for ordering purposes.
        sTTNot1Or4AndUnderOccupying = "_b_TTNot1Or4AndUnderOccupying";

        sTT1_To_TT3OrTT6 = "_e_TT1_To_TT3OrTT6";
        sTT4_To_TT3OrTT6 = "_f_TT4_To_TT3OrTT6";
        sTT3OrTT6_To_TT1 = "_g_TT3OrTT6_To_TT1";
        sTT3OrTT6_To_TT4 = "_h_TT3OrTT6_To_TT4";

        sAlwaysUOFromStart__NoValidPostcodeChange_NotChangedTT = "_k_AlwaysUOFromStart__NoValidPostcodeChange_NotChangedTT";
        sAlwaysUOFromStart__ChangedTT = "_l_AlwaysUOFromStart__ChangedTT";
        sAlwaysUOFromStart__ValidPostcodeChange_NotChangedTT = "_m_AlwaysUOFromStart__ValidPostcodeChange_NotChangedTT";

        sAlwaysUOFromWhenStarted__NoValidPostcodeChange_NotChangedTT = "_p_AlwaysUOFromWhenStarted__NoValidPostcodeChange_NotChangedTT";
        sAlwaysUOFromWhenStarted__ChangedTT = "_q_AlwaysUOFromWhenStarted__ChangedTT";
        sAlwaysUOFromWhenStarted__ValidPostcodeChange_NotChangedTT = "_r_AlwaysUOFromWhenStarted__ValidPostcodeChange_NotChangedTT";

        sIntermitantUO__NoValidPostcodeChange_NotChangedTT = "_u_" + "IntermitantUO__NoValidPostcodeChange_NotChangedTT";
        sIntermitantUO__ChangedTT = "_v_" + "IntermitantUO__ChangedTT";
        sIntermitantUO__ValidPostcodeChange_NotChangedTT = "_w_" + "IntermitantUO__ValidPostcodeChange_NotChangedTT";
    }

    public DW_TenancyChangesUO(
            DW_Environment env) {
        super(env);
    }

    public DW_TenancyChangesUO(DW_Environment env, boolean hoome) 
            throws IOException, Exception, ClassNotFoundException {
        this(env);
        this.shbeHandler = env.getSHBE_Handler();
        this.ttHandler = env.getSHBE_TenancyType_Handler();
        //this.p2pid = tPostcodeToPostcodeIDLookup;
        this.cid2c = shbeHandler.getCid2c();
        this.c2cid = shbeHandler.getC2cid();
        initString();
    }

    protected TreeMap<String, String> getPreUnderOccupancyValues(
            HashSet<SHBE_ClaimID> ClaimIDs,
            String[] SHBEFilenames,
            ArrayList<Integer> NotMonthlyUO
    ) throws IOException, ClassNotFoundException {
        TreeMap<String, String> r;
        r = new TreeMap<>();
        // Init result
        String s_ = DW_Strings.symbol_underscore;
        SHBE_ClaimID ClaimID;
        String ClaimRef;
        Iterator<SHBE_ClaimID> ite;
        ite = ClaimIDs.iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            ClaimRef = cid2c.get(ClaimID);
            r.put(ClaimRef + s_ + sTT, s);
            r.put(ClaimRef + s_ + sUnderOccupancy, s);
            r.put(ClaimRef + s_ + sP, s);
            r.put(ClaimRef + s_ + sWHBE, s);
            r.put(ClaimRef + s_ + sWERA, s);
            r.put(ClaimRef + s_ + sPSI, s);
            r.put(ClaimRef + s_ + sSHBC, s);
            r.put(ClaimRef + s_ + sRTHBCC, s);
            r.put(ClaimRef + s_ + sCEG, s);
            r.put(ClaimRef + s_ + sHS, s);
            r.put(ClaimRef + s_ + sND, s);
            r.put(ClaimRef + s_ + sCD, s);
            r.put(ClaimRef + s_ + sNDUO, s);
            r.put(ClaimRef + s_ + sCO16, s);
            r.put(ClaimRef + s_ + sFCU10, s);
            r.put(ClaimRef + s_ + sMCU10, s);
            r.put(ClaimRef + s_ + sFC10To16, s);
            r.put(ClaimRef + s_ + sMC10To16, s);
            r.put(ClaimRef + s_ + sBR, s);
            r.put(ClaimRef + s_ + sNB, s);
            r.put(ClaimRef + s_ + sCDoB, s);
            r.put(ClaimRef + s_ + sCA, s);
            r.put(ClaimRef + s_ + sPDoB, s);
            r.put(ClaimRef + s_ + sPA, s);
            r.put(ClaimRef + s_ + sCG, s);
            r.put(ClaimRef + s_ + sPG, s);
            r.put(ClaimRef + s_ + sDisability, s);
            r.put(ClaimRef + s_ + sDisabilityPremium, s);
            r.put(ClaimRef + s_ + sDisabilitySevere, s);
            r.put(ClaimRef + s_ + sDisabilityEnhanced, s);
            r.put(ClaimRef + s_ + sDisabledChild, s);
            r.put(ClaimRef + s_ + sPDeath, s);
            r.put(ClaimRef + s_ + sHBDP, s);
            r.put(ClaimRef + s_ + sA, s);
        }
        Iterator<Integer> tNotMonthlyUOIte;
        SHBE_Records SHBE_Records;
        int i;
        String key;
        String aS;
        int j;
        String bS;
        boolean b;
        Map<SHBE_ClaimID, SHBE_Record> records;
        String year;
        String month;
        UKP_YM3 yM3;
        SHBE_Record record;
        SHBE_D_Record d;
        String s3 = DW_Strings.special_commaSpace;
        tNotMonthlyUOIte = NotMonthlyUO.iterator();
        while (tNotMonthlyUOIte.hasNext()) {
            i = tNotMonthlyUOIte.next();
            year = shbeHandler.getYear(SHBEFilenames[i]);
            month = shbeHandler.getMonthNumber(SHBEFilenames[i]);
            yM3 = shbeHandler.getYM3(SHBEFilenames[i]);
            SHBE_Records = shbeHandler.getRecords(yM3, env.HOOME);
            records = SHBE_Records.getRecords(env.HOOME);
            ite = ClaimIDs.iterator();
            while (ite.hasNext()) {
                ClaimID = ite.next();
                ClaimRef = cid2c.get(ClaimID);
                record = records.get(ClaimID);
                if (record != null) {
                    d = record.getDRecord();
                    // Tenancy Type
                    key = ClaimRef + s_ + sTT;
                    aS = r.get(key);
                    j = d.getTenancyType();
                    aS += s3 + sTT_ + j;
                    r.put(key, aS);
                    // Under Occupancy
                    key = ClaimRef + s_ + sUnderOccupancy;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Postcode
                    key = ClaimRef + s_ + sP;
                    aS = r.get(key);
                    bS = d.getClaimantsPostcode();
                    aS += s3 + bS;
                    r.put(key, aS);
                    // Weekly Housing Benefit Entitlement
                    key = ClaimRef + s_ + sWHBE;
                    aS = r.get(key);
                    j = d.getWeeklyHousingBenefitEntitlement();
                    aS += s3 + decimalise(j);
                    r.put(key, aS);
                    // Weekly Eligible Rent Amount
                    key = ClaimRef + s_ + sWERA;
                    aS = r.get(key);
                    j = d.getWeeklyEligibleRentAmount();
                    aS += s3 + decimalise(j);
                    r.put(key, aS);
                    // PassportedStandardIndicator
                    key = ClaimRef + s_ + sPSI;
                    aS = r.get(key);
                    j = d.getPassportedStandardIndicator();
                    aS += s3 + j;
                    r.put(key, aS);
                    // StatusOfHBClaim
                    key = ClaimRef + s_ + sSHBC;
                    aS = r.get(key);
                    j = d.getStatusOfHBClaimAtExtractDate();
                    aS += s3 + j;
                    r.put(key, aS);
                    // ReasonThatHBClaimClosed
                    key = ClaimRef + s_ + sRTHBCC;
                    aS = r.get(key);
                    j = d.getReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective();
                    if (j == 0) {
                        aS += s3;
                    } else {
                        aS += s3 + j;
                    }
                    r.put(key, aS);
                    // ClaimantEthnicGroup
                    key = ClaimRef + s_ + sCEG;
                    aS = r.get(key);
                    //j = dRecord.getClaimantsEthnicGroup();
                    j = shbeHandler.getEthnicityGroup(d);
                    aS += s3 + j;
                    r.put(key, aS);
                    // Household Size
                    key = ClaimRef + s_ + sHS;
                    aS = r.get(key);
                    j = (int) shbeHandler.getHouseholdSize(d);
                    aS += s3 + j;
                    r.put(key, aS);
                    // NonDependents
                    key = ClaimRef + s_ + sND;
                    aS = r.get(key);
                    j = d.getNumberOfNonDependents();
                    if (j == 0) {
                        aS += s3;
                    } else {
                        aS += s3 + j;
                    }
                    r.put(key, aS);
                    // Child Dependents
                    key = ClaimRef + s_ + sCD;
                    aS = r.get(key);
                    j = d.getNumberOfChildDependents();
                    if (j == 0) {
                        aS += s3;
                    } else {
                        aS += s3 + j;
                    }
                    r.put(key, aS);
                    // NonDependents (UO)
                    key = ClaimRef + s_ + sNDUO;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // ChildrenOver16
                    key = ClaimRef + s_ + sCO16;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // FemaleChildrenUnder10";
                    key = ClaimRef + s_ + sFCU10;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // MaleChildrenUnder10";
                    key = ClaimRef + s_ + sMCU10;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // FemaleChildren10to16";
                    key = ClaimRef + s_ + sFC10To16;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // MaleChildren10to16
                    key = ClaimRef + s_ + sMC10To16;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Number of Bedrooms
                    key = ClaimRef + s_ + sNB;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Bedroom Requirement
                    key = ClaimRef + s_ + sBR;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Claimants Date Of Birth
                    key = ClaimRef + s_ + sCDoB;
                    aS = r.get(key);
                    bS = d.getClaimantsDateOfBirth();
                    aS += s3 + bS;
                    r.put(key, aS);
                    // ClaimantsAge
                    key = ClaimRef + s_ + sCA;
                    aS = r.get(key);
                    bS = shbeHandler.getClaimantsAge(year, month, d);
                    aS += s3 + bS;
                    r.put(key, aS);
                    // Partners Date Of Birth
                    key = ClaimRef + s_ + sPDoB;
                    aS = r.get(key);
                    bS = d.getPartnersDateOfBirth();
                    aS += s3 + bS;
                    r.put(key, aS);
                    // PartnersAge
                    key = ClaimRef + s_ + sPA;
                    aS = r.get(key);
                    bS = shbeHandler.getPartnersAge(year, month, d);
                    aS += s3 + bS;
                    r.put(key, aS);
                    // ClaimantsGender
                    key = ClaimRef + s_ + sCG;
                    aS = r.get(key);
                    bS = d.getClaimantsGender();
                    aS += s3 + bS;
                    r.put(key, aS);
                    // PartnersGender
                    key = ClaimRef + s_ + sPG;
                    aS = r.get(key);
                    bS = d.getPartnersGender();
                    aS += s3 + bS;
                    r.put(key, aS);
                    // Disability
                    key = ClaimRef + s_ + sDisability;
                    aS = r.get(key);
                    b = shbeHandler.getDisability(d);
                    if (b == true) {
                        aS += s3 + sDisability;
                    } else {
                        aS += s3;
                    }
                    r.put(key, aS);
                    // Disability Premium
                    key = ClaimRef + s_ + sDisabilityPremium;
                    aS = r.get(key);
                    j = d.getDisabilityPremiumAwarded();
                    if (j == 1) {
                        aS += s3 + sDP;
                    } else {
                        aS += s3;
                    }
                    r.put(key, aS);
                    // Disability Severe
                    key = ClaimRef + s_ + sDisabilitySevere;
                    aS = r.get(key);
                    j = d.getSevereDisabilityPremiumAwarded();
                    if (j == 1) {
                        aS += s3 + sDS;
                    } else {
                        aS += s3;
                    }
                    r.put(key, aS);
                    // Disability Enhanced
                    key = ClaimRef + s_ + sDisabilityEnhanced;
                    aS = r.get(key);
                    j = d.getEnhancedDisabilityPremiumAwarded();
                    if (j == 1) {
                        aS += s3 + sDE;
                    } else {
                        aS += s3;
                    }
                    r.put(key, aS);
                    // Child Disability
                    key = ClaimRef + s_ + sDisabledChild;
                    aS = r.get(key);
                    j = d.getDisabledChildPremiumAwarded();
                    if (j == 1) {
                        aS += s3 + sDC;
                    } else {
                        aS += s3;
                    }
                    r.put(key, aS);
                    // Partner Death
                    key = ClaimRef + s_ + sPDeath;
                    aS = r.get(key);
                    bS = d.getPartnersDateOfDeath();
                    if (bS == null) {
                        aS += s3;
                    } else if (bS.isEmpty()) {
                        aS += s3;
                    } else {
                        aS += s3 + sPDeath + s_ + bS;
                    }
                    r.put(key, aS);
                    // HB Discretionary Payment
                    key = ClaimRef + s_ + sHBDP;
                    aS = r.get(key);
                    j = d.getWeeklyAdditionalDiscretionaryPayment();
                    aS += s3 + decimalise(j);
                    r.put(key, aS);
                    // Arrears
                    key = ClaimRef + s_ + sA;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                } else {
                    // Tenancy Type
                    key = ClaimRef + s_ + sTT;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Under Occupancy
                    key = ClaimRef + s_ + sUnderOccupancy;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Postcode
                    key = ClaimRef + s_ + sP;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Weekly Housing Benefit Entitlement
                    key = ClaimRef + s_ + sWHBE;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Weekly Eligible Rent Amount
                    key = ClaimRef + s_ + sWERA;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // PassportedStandardIndicator
                    key = ClaimRef + s_ + sPSI;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // StatusOfHBClaim
                    key = ClaimRef + s_ + sSHBC;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // ReasonThatHBClaimClosed
                    key = ClaimRef + s_ + sRTHBCC;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // ClaimantEthnicGroup
                    key = ClaimRef + s_ + sCEG;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Household Size
                    key = ClaimRef + s_ + sHS;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // NonDependents
                    key = ClaimRef + s_ + sND;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // ChildDependents
                    key = ClaimRef + s_ + sCD;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // NonDependents (UO)
                    key = ClaimRef + s_ + sNDUO;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // ChildrenOver16
                    key = ClaimRef + s_ + sCO16;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // FemaleChildrenUnder10";
                    key = ClaimRef + s_ + sFCU10;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // MaleChildrenUnder10";
                    key = ClaimRef + s_ + sMCU10;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // FemaleChildren10to16";
                    key = ClaimRef + s_ + sFC10To16;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // MaleChildren10to16
                    key = ClaimRef + s_ + sMC10To16;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Number of Bedrooms
                    key = ClaimRef + s_ + sNB;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Bedroom Requirement
                    key = ClaimRef + s_ + sBR;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Claimants Date Of Birth
                    key = ClaimRef + s_ + sCDoB;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // ClaimantsAge
                    key = ClaimRef + s_ + sCA;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Partners Date Of Birth
                    key = ClaimRef + s_ + sPDoB;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Partners Age
                    key = ClaimRef + s_ + sPA;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // ClaimantsGender
                    key = ClaimRef + s_ + sCG;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Partners Gender
                    key = ClaimRef + s_ + sPG;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Disability
                    key = ClaimRef + s_ + sDisability;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Disability Premium
                    key = ClaimRef + s_ + sDisabilityPremium;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Disability Severe
                    key = ClaimRef + s_ + sDisabilitySevere;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Disability Enhanced
                    key = ClaimRef + s_ + sDisabilityEnhanced;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Child Disability
                    key = ClaimRef + s_ + sDisabledChild;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Partner Death
                    key = ClaimRef + s_ + sPDeath;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // HB Discretionary Payment
                    key = ClaimRef + s_ + sHBDP;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                    // Arrears
                    key = ClaimRef + s_ + sA;
                    aS = r.get(key);
                    aS += s3;
                    r.put(key, aS);
                }
            }
        }
        return r;
    }

    public String decimalise(String s) {
        if (s.isEmpty()) {
            return s;
        }
        String t;
        t = s.trim();
        if (t.equalsIgnoreCase(s0)) {
            return s0;
        }
        int l;
        l = t.length();
        if (l == 1) {
            return s0Dot0 + t;
        }
        if (l == 2) {
            return "0." + t;
        }
        if (l == 3) {
            return t.substring(0, l - 2) + "." + t.substring(l - 2, l);
        }
        //if (l > 2) {
        return t.substring(0, l - 2) + "." + t.substring(l - 2, l);
        //}
    }

    public String decimalise(int i) {
        return decimalise(Integer.toString(i));
    }

    /**
     *
     * @param DW_UO_Data
     * @param SHBEFilenames
     * @param include
     * @param includePreUnderOccupancyValues If true then
     * @return Object[8] result where: result[0] = Header; result[1] =
     * TableValues; result[2] = ClaimIDs; result[3] = Groups; result[4] =
     * PreUnderOccupancyValues; result[5] = AggregateStatistics; result[6] =
     * GeneralStatistics; result[7] = TimeStatistics;
     */
    public Object[] getTable(
            DW_UO_Data DW_UO_Data,
            String[] SHBEFilenames,
            ArrayList<Integer> include,
            boolean includePreUnderOccupancyValues
    ) throws IOException, ClassNotFoundException {
        env.ge.log("<getTable>", true);
        Object[] result;
        result = new Object[12];
        ValidPostcodes = new HashSet<>();

        // Initialise result part 1
        TreeMap<String, String> TableValues;
        TableValues = new TreeMap<>();

        TreeMap<UKP_YM3, DW_UO_Set> CouncilUOSets;
        TreeMap<UKP_YM3, DW_UO_Set> RSLUOSets;
        CouncilUOSets = DW_UO_Data.getCouncilUOSets();
        RSLUOSets = DW_UO_Data.getRSLUOSets();

        HashSet<SHBE_ClaimID> UOClaims;
        UOClaims = new HashSet<>();

        // Init Time Statistics
        TreeMap<String, TreeMap<String, ?>> TimeStatistics;
        TimeStatistics = new TreeMap<>();
        TreeMap<String, Integer> TotalCount_CumulativeUniqueClaims;
        TotalCount_CumulativeUniqueClaims = new TreeMap<>();
        TimeStatistics.put(sTotalCount_CumulativeUniqueClaims,
                TotalCount_CumulativeUniqueClaims);
        int totalCount_UOClaims;
        TreeMap<String, Integer> TotalCount_UOClaims;
        TotalCount_UOClaims = new TreeMap<>();
        TimeStatistics.put(
                sTotalCount_UOClaims,
                TotalCount_UOClaims);
        int totalCount_UOCouncilClaims;
        TreeMap<String, Integer> TotalCount_UOCouncilClaims;
        TotalCount_UOCouncilClaims = new TreeMap<>();
        TimeStatistics.put(
                sTotalCount_UOCouncilClaims,
                TotalCount_UOCouncilClaims);
        int totalCount_UORSLClaims;
        TreeMap<String, Integer> TotalCount_UORSLClaims;
        TotalCount_UORSLClaims = new TreeMap<>();
        TimeStatistics.put(
                sTotalCount_UORSLClaims,
                TotalCount_UORSLClaims);
        int totalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE;
        TreeMap<String, Integer> TotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE;
        TotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE = new TreeMap<>();
        TimeStatistics.put(sTotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE,
                TotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE);
        int totalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO;
        TreeMap<String, Integer> TotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO;
        TotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO = new TreeMap<>();
        TimeStatistics.put(sTotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO,
                TotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO);

        int totalHouseholdSizeExcludingPartnersCouncilSHBE;
        TreeMap<String, Integer> TotalHouseholdSizeExcludingPartnersCouncilSHBEs;
        TotalHouseholdSizeExcludingPartnersCouncilSHBEs = new TreeMap<>();
        TimeStatistics.put(sTotalHouseholdSizeExcludingPartnersCouncilSHBE,
                TotalHouseholdSizeExcludingPartnersCouncilSHBEs);

        int totalHouseholdSizeCouncilSHBE;
        TreeMap<String, Integer> TotalHouseholdSizeCouncilSHBEs;
        TotalHouseholdSizeCouncilSHBEs = new TreeMap<>();
        TimeStatistics.put(sTotalHouseholdSizeCouncilSHBE,
                TotalHouseholdSizeCouncilSHBEs);

        int totalHouseholdSizeExcludingPartnersCouncilUO;
        TreeMap<String, Integer> TotalHouseholdSizeExcludingPartnersCouncilUOs;
        TotalHouseholdSizeExcludingPartnersCouncilUOs = new TreeMap<>();
        TimeStatistics.put(sTotalHouseholdSizeExcludingPartnersCouncilUO,
                TotalHouseholdSizeExcludingPartnersCouncilUOs);

        int totalHouseholdSizeCouncilUO;
        TreeMap<String, Integer> TotalHouseholdSizeCouncilUOs;
        TotalHouseholdSizeCouncilUOs = new TreeMap<>();
        TimeStatistics.put(sTotalHouseholdSizeCouncilUO,
                TotalHouseholdSizeCouncilUOs);

        int totalHouseholdSizeExcludingPartnersRSLSHBE;
        TreeMap<String, Integer> TotalHouseholdSizeExcludingPartnersRSLSHBEs;
        TotalHouseholdSizeExcludingPartnersRSLSHBEs = new TreeMap<>();
        TimeStatistics.put(sTotalHouseholdSizeExcludingPartnersRSLSHBE,
                TotalHouseholdSizeExcludingPartnersRSLSHBEs);

        int totalHouseholdSizeRSLSHBE;
        TreeMap<String, Integer> TotalHouseholdSizeRSLSHBEs;
        TotalHouseholdSizeRSLSHBEs = new TreeMap<>();
        TimeStatistics.put(sTotalHouseholdSizeRSLSHBE,
                TotalHouseholdSizeRSLSHBEs);

        int totalHouseholdSizeExcludingPartnersRSLUO;
        TreeMap<String, Integer> TotalHouseholdSizeExcludingPartnersRSLUOs;
        TotalHouseholdSizeExcludingPartnersRSLUOs = new TreeMap<>();
        TimeStatistics.put(sTotalHouseholdSizeExcludingPartnersRSLUO,
                TotalHouseholdSizeExcludingPartnersRSLUOs);

        int totalHouseholdSizeRSLUO;
        TreeMap<String, Integer> TotalHouseholdSizeRSLUOs;
        TotalHouseholdSizeRSLUOs = new TreeMap<>();
        TimeStatistics.put(sTotalHouseholdSizeRSLUO,
                TotalHouseholdSizeRSLUOs);

//        int totalAggregateHouseholdSize_UOClaims;
//        TreeMap<String, Integer> totalAggregateHouseholdSize_UOClaimss;
//        totalAggregateHouseholdSize_UOClaimss = new TreeMap<String, Integer>();
//        timeStatistics.put(
//                sTotalAggregateHouseholdSize_UOClaims,
//                totalAggregateHouseholdSize_UOClaimss);
//        double averageHouseholdSize_UOClaims;
//        TreeMap<String, Double> averageHouseholdSize_UOClaimss;
//        averageHouseholdSize_UOClaimss = new TreeMap<String, Double>();
//        timeStatistics.put(sAverageHouseholdSize_UOClaims,
//                totalCounts_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedrooms);
//         String sTotalAggregateHouseholdSize_UOClaimsCouncil = "TotalAggregateHouseholdSize_UOClaimsCouncil";
//    String sTotalAverageHouseholdSize_UOClaimsCouncil = "TotalAverageHouseholdSize_UOClaimsCouncil";
//    String sTotalAggregateHouseholdSize_UOClaimsRSL = "TotalAggregateHouseholdSize_UOClaimsRSL";
//    String sTotalAverageHouseholdSize_UOClaimsRSL = "TotalAverageHouseholdSize_UOClaimsRSL";
        HashSet<SHBE_ClaimID>[] AllClaimIDs;
        AllClaimIDs = getUOClaimIDs(
                CouncilUOSets,
                RSLUOSets,
                SHBEFilenames,
                include);
        HashSet<SHBE_ClaimID> CouncilClaimIDs;
        CouncilClaimIDs = AllClaimIDs[0];
        HashSet<SHBE_ClaimID> RSLClaimIDs;
        RSLClaimIDs = AllClaimIDs[1];
        HashSet<SHBE_ClaimID> ClaimIDs;
        ClaimIDs = new HashSet<>();
        ClaimIDs.addAll(CouncilClaimIDs);
        ClaimIDs.addAll(RSLClaimIDs);

        HashSet<SHBE_ClaimID>[] StartUOClaimIDsX;
        StartUOClaimIDsX = getStartUOClaimIDs(
                CouncilUOSets,
                RSLUOSets,
                SHBEFilenames,
                include);
        HashSet<SHBE_ClaimID> StartUOClaimIDs;
        StartUOClaimIDs = new HashSet<>();
        StartUOClaimIDs.addAll(StartUOClaimIDsX[0]);
        StartUOClaimIDs.addAll(StartUOClaimIDsX[1]);

        HashSet<SHBE_ClaimID>[] EndUOClaimIDsX;
        EndUOClaimIDsX = getEndUOClaimIDs(
                CouncilUOSets,
                RSLUOSets,
                SHBEFilenames,
                include);
        HashSet<SHBE_ClaimID> EndUOClaimIDs;
        EndUOClaimIDs = new HashSet<>();
        EndUOClaimIDs.addAll(EndUOClaimIDsX[0]);
        EndUOClaimIDs.addAll(EndUOClaimIDsX[1]);

        HashSet<SHBE_ClaimID> SubsequentlyEffectedUOStillUOInLatestSHBE;
        SubsequentlyEffectedUOStillUOInLatestSHBE = new HashSet<>();
        SubsequentlyEffectedUOStillUOInLatestSHBE.addAll(ClaimIDs);
        SubsequentlyEffectedUOStillUOInLatestSHBE.removeAll(StartUOClaimIDs);
        SubsequentlyEffectedUOStillUOInLatestSHBE.retainAll(EndUOClaimIDs);

        TreeMap<String, BigDecimal> GeneralStatistics;
        GeneralStatistics = new TreeMap<>();
        GeneralStatistics.put(sNotUOInApril2013ThenUOAndUOInLatestMonth,
                BigDecimal.valueOf(SubsequentlyEffectedUOStillUOInLatestSHBE.size()));
        HashSet<SHBE_ClaimID> EndUOThatWereAlsoStartUOClaimRefs;
        EndUOThatWereAlsoStartUOClaimRefs = new HashSet<>();
        EndUOThatWereAlsoStartUOClaimRefs.addAll(EndUOClaimIDs);
        EndUOThatWereAlsoStartUOClaimRefs.retainAll(StartUOClaimIDs);

        TreeMap<String, ArrayList<Integer>> includes = shbeHandler.getIncludes();
        ArrayList<Integer> MonthlyUO;
        MonthlyUO = includes.get(SHBE_Strings.s_IncludeMonthlySinceApril2013);
        ArrayList<Integer> All = includes.get(SHBE_Strings.s_IncludeAll);
        ArrayList<Integer> NotMonthlyUO = new ArrayList<>();
        NotMonthlyUO.addAll(All);
        NotMonthlyUO.removeAll(MonthlyUO);

        TreeMap<String, String> preUnderOccupancyValues = null;
        if (includePreUnderOccupancyValues) {
            preUnderOccupancyValues = getPreUnderOccupancyValues(ClaimIDs,
                    SHBEFilenames,                    NotMonthlyUO);
            result[4] = preUnderOccupancyValues;
        }

        SHBE_Records shbeRecs1;
        UKP_YM3 YM3Start = null;
        UKP_YM3 YM30 = null;
        String year0 = s;
        String month0 = s;
        UKP_YM3 YM31;
        String year1;
        String month1;
        DW_UO_Set CouncilUOSet0 = null;
        DW_UO_Set RSLUOSet0 = null;
        DW_UO_Set CouncilUOSet1;
        DW_UO_Set RSLUOSet1;
        String SHBEFilename1;

        Iterator<Integer> iteX;
        iteX = include.iterator();
        int j = 0;
        while (iteX.hasNext()) {
            j = iteX.next();
            if (YM3Start == null) {
                SHBEFilename1 = SHBEFilenames[j];
                YM3Start = shbeHandler.getYM3(SHBEFilename1);
            }
        }
        SHBEFilename1 = SHBEFilenames[j];
        YM31 = shbeHandler.getYM3(SHBEFilename1);
        year1 = shbeHandler.getYear(SHBEFilename1);
        month1 = shbeHandler.getMonthNumber(SHBEFilename1);
        CouncilUOSet1 = CouncilUOSets.get(YM31);
        RSLUOSet1 = RSLUOSets.get(YM31);
        shbeRecs1 = shbeHandler.getRecords(YM31, env.HOOME);
        Map<SHBE_ClaimID, SHBE_Record> recs1 = shbeRecs1.getRecords(env.HOOME);
        HashMap<SHBE_ClaimID, DW_UO_Record> CouncilUOSetMap1;
        CouncilUOSetMap1 = CouncilUOSet1.getMap();

        int TotalCount_InArrears1;
        int TotalCount_InArrears0To101;
        int TotalCount_InArrears10To1001;
        int TotalCount_InArrears100To5001;
        int TotalCount_InArrearsOver5001;
        int ReceivingDHPCount1;
        int InArrearsAndReceivingDHPCount1;
        SHBE_D_Record DRecord1;
        /*
        * Iterate over records in EndUOClaimRefs
         */
        TotalCount_InArrears1 = 0;
        TotalCount_InArrears0To101 = 0;
        TotalCount_InArrears10To1001 = 0;
        TotalCount_InArrears100To5001 = 0;
        TotalCount_InArrearsOver5001 = 0;
        ReceivingDHPCount1 = 0;
        InArrearsAndReceivingDHPCount1 = 0;
        Iterator<SHBE_ClaimID> ite;
        ite = EndUOClaimIDs.iterator();
        while (ite.hasNext()) {
            SHBE_ClaimID ClaimID;
            ClaimID = ite.next();
           SHBE_Record r1 = recs1.get(ClaimID);
            if (r1 != null) {
                DRecord1 = r1.getDRecord();
                int DHP1;
                DHP1 = DRecord1.getWeeklyAdditionalDiscretionaryPayment();
                if (DHP1 > 0) {
                    ReceivingDHPCount1++;
                }
                if (CouncilUOSetMap1.containsKey(ClaimID)) {
                    DW_UO_Record UORec;
                    UORec = CouncilUOSetMap1.get(ClaimID);
                    double arrears;
                    arrears = UORec.getTotalRentArrears();
                    if (arrears > 0) {
                        TotalCount_InArrears1++;
                        if (arrears < 10) {
                            TotalCount_InArrears0To101++;
                        } else if (arrears < 100) {
                            TotalCount_InArrears10To1001++;
                        } else if (arrears < 500) {
                            TotalCount_InArrears100To5001++;
                        } else {
                            TotalCount_InArrearsOver5001++;
                        }
                        if (DHP1 > 0) {
                            InArrearsAndReceivingDHPCount1++;
                        }
                    }
                }
            }
        }
        double p;
        double d;
        p = ((double) ReceivingDHPCount1) * 100.0d / (double) EndUOClaimIDs.size();
        GeneralStatistics.put(sPercentageOfUO_ReceivingDHPInLatestMonth,
                Math_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        d = (double) CouncilUOSetMap1.size();
        p = ((double) TotalCount_InArrears1) * 100.0d / d;
        GeneralStatistics.put(sPercentageOfCouncilUO_InArrearsInLatestMonth,
                Math_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        p = ((double) TotalCount_InArrears0To101) * 100.0d / d;
        GeneralStatistics.put(sPercentageOfCouncilUO_InArrears0To10InLatestMonth,
                Math_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        p = ((double) TotalCount_InArrears10To1001) * 100.0d / d;
        GeneralStatistics.put(sPercentageOfCouncilUO_InArrears10To100InLatestMonth,
                Math_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        p = ((double) TotalCount_InArrears100To5001) * 100.0d / d;
        GeneralStatistics.put(sPercentageOfCouncilUO_InArrears100To500InLatestMonth,
                Math_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        p = ((double) TotalCount_InArrearsOver5001) * 100.0d / d;
        GeneralStatistics.put(sPercentageOfCouncilUO_InArrearsOver500InLatestMonth,
                Math_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));

        p = ((double) InArrearsAndReceivingDHPCount1) * 100.0d / d;
        GeneralStatistics.put(sPercentageOfCouncilUO_InArrearsAndReceivingDHPInLatestMonth,
                Math_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));

        /*
        * Iterate over records in EndUOThatWereAlsoStartUOClaimRefs
         */
        TotalCount_InArrears1 = 0;
        TotalCount_InArrears0To101 = 0;
        TotalCount_InArrears10To1001 = 0;
        TotalCount_InArrears100To5001 = 0;
        TotalCount_InArrearsOver5001 = 0;
        ReceivingDHPCount1 = 0;
        InArrearsAndReceivingDHPCount1 = 0;
        ite = EndUOThatWereAlsoStartUOClaimRefs.iterator();
        while (ite.hasNext()) {
            SHBE_ClaimID ClaimID;
            ClaimID = ite.next();
            SHBE_Record r1 = recs1.get(ClaimID);
            if (r1 != null) {
                DRecord1 = r1.getDRecord();
                int DHP1;
                DHP1 = DRecord1.getWeeklyAdditionalDiscretionaryPayment();
                if (DHP1 > 0) {
                    ReceivingDHPCount1++;
                }
                if (CouncilUOSetMap1.containsKey(ClaimID)) {
                    DW_UO_Record UORec;
                    UORec = CouncilUOSetMap1.get(ClaimID);
                    double arrears;
                    arrears = UORec.getTotalRentArrears();
                    if (arrears > 0) {
                        TotalCount_InArrears1++;
                        if (arrears < 10) {
                            TotalCount_InArrears0To101++;
                        } else if (arrears < 100) {
                            TotalCount_InArrears10To1001++;
                        } else if (arrears < 500) {
                            TotalCount_InArrears100To5001++;
                        } else {
                            TotalCount_InArrearsOver5001++;
                        }
                        if (DHP1 > 0) {
                            InArrearsAndReceivingDHPCount1++;
                        }
                    }
                }
            }
        }
        p = ((double) ReceivingDHPCount1) * 100.0d / (double) EndUOClaimIDs.size();
        GeneralStatistics.put(sPercentageOfUO_ReceivingDHPInLatestMonthThatWereUOInApril2013,
                Math_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        d = (double) CouncilUOSetMap1.size();
        p = ((double) TotalCount_InArrears1) * 100.0d / d;
        GeneralStatistics.put(sPercentageOfCouncilUO_InArrearsInLatestMonthThatWereUOInApril2013,
                Math_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        p = ((double) TotalCount_InArrears0To101) * 100.0d / d;
        GeneralStatistics.put(sPercentageOfCouncilUO_InArrears0To10InLatestMonthThatWereUOInApril2013,
                Math_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        p = ((double) TotalCount_InArrears10To1001) * 100.0d / d;
        GeneralStatistics.put(sPercentageOfCouncilUO_InArrears10To100InLatestMonthThatWereUOInApril2013,
                Math_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        p = ((double) TotalCount_InArrears100To5001) * 100.0d / d;
        GeneralStatistics.put(sPercentageOfCouncilUO_InArrears100To500InLatestMonthThatWereUOInApril2013,
                Math_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        p = ((double) TotalCount_InArrearsOver5001) * 100.0d / d;
        GeneralStatistics.put(sPercentageOfCouncilUO_InArrearsOver500InLatestMonthThatWereUOInApril2013,
                Math_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));
        p = ((double) InArrearsAndReceivingDHPCount1) * 100.0d / d;
        GeneralStatistics.put(sPercentageOfCouncilUO_InArrearsAndReceivingDHPInLatestMonthThatWereUOInApril2013,
                Math_BigDecimal.roundIfNecessary(BigDecimal.valueOf(p), 3, RoundingMode.HALF_UP));

//        TreeMap<String, ArrayList<Integer>> includes;
//        includes = shbeHandler.getIncludes();
//        ArrayList<Integer> MonthlyUO;
//        MonthlyUO = includes.get(shbeHandler.sIncludeMonthlySinceApril2013);
//        ArrayList<Integer> All;
//        All = includes.get(shbeHandler.sIncludeAll);
//        ArrayList<Integer> NotMonthlyUO;
//        NotMonthlyUO = new ArrayList<Integer>();
//        NotMonthlyUO.addAll(All);
//        NotMonthlyUO.removeAll(MonthlyUO);
//
//        TreeMap<String, String> preUnderOccupancyValues;
//        preUnderOccupancyValues = null;
//        if (includePreUnderOccupancyValues) {
//            preUnderOccupancyValues = getPreUnderOccupancyValues(tClaimRefs,
//                    SHBEFilenames,
//                    NotMonthlyUO);
//            result[4] = preUnderOccupancyValues;
//        }
        // All
        HashSet<SHBE_PersonID> UniqueIndividualsEffectedPersonIDs;
        UniqueIndividualsEffectedPersonIDs = new HashSet<>();
        HashSet<SHBE_PersonID> UniqueDependentsAgedUnder10EffectedPersonIDs;
        UniqueDependentsAgedUnder10EffectedPersonIDs = new HashSet<>();
        HashSet<SHBE_PersonID> UniqueDependentsAgedOver10EffectedPersonIDs;
        UniqueDependentsAgedOver10EffectedPersonIDs = new HashSet<>();
        HashSet<SHBE_PersonID> UniqueDependentsEffectedPersonIDs;
        UniqueDependentsEffectedPersonIDs = new HashSet<>();
        // Council
        HashSet<SHBE_PersonID> CouncilUniqueIndividualsEffectedPersonIDs;
        CouncilUniqueIndividualsEffectedPersonIDs = new HashSet<>();
        HashSet<SHBE_PersonID> CouncilUniqueClaimantsEffectedPersonIDs;
        CouncilUniqueClaimantsEffectedPersonIDs = new HashSet<>();
        HashSet<SHBE_PersonID> CouncilUniquePartnersEffectedPersonIDs;
        CouncilUniquePartnersEffectedPersonIDs = new HashSet<>();
        HashSet<SHBE_PersonID> CouncilUniqueDependentChildrenUnder10EffectedPersonIDs;
        CouncilUniqueDependentChildrenUnder10EffectedPersonIDs = new HashSet<>();
        HashSet<SHBE_PersonID> CouncilUniqueDependentChildrenOver10EffectedPersonIDs;
        CouncilUniqueDependentChildrenOver10EffectedPersonIDs = new HashSet<>();
        HashSet<SHBE_PersonID> CouncilUniqueNonDependentsEffectedPersonIDs;
        CouncilUniqueNonDependentsEffectedPersonIDs = new HashSet<>();
        HashMap<SHBE_ClaimID, Integer> CouncilMaxNumberOfDependentsInClaimWhenUO;
        CouncilMaxNumberOfDependentsInClaimWhenUO = new HashMap<>();
        // RSL
        HashSet<SHBE_PersonID> RSLUniqueIndividualsEffectedPersonIDs;
        RSLUniqueIndividualsEffectedPersonIDs = new HashSet<>();
        HashSet<SHBE_PersonID> RSLUniqueClaimantsEffectedPersonIDs;
        RSLUniqueClaimantsEffectedPersonIDs = new HashSet<>();
        HashSet<SHBE_PersonID> RSLUniquePartnersEffectedPersonIDs;
        RSLUniquePartnersEffectedPersonIDs = new HashSet<>();
        HashSet<SHBE_PersonID> RSLUniqueDependentChildrenUnder10EffectedPersonIDs;
        RSLUniqueDependentChildrenUnder10EffectedPersonIDs = new HashSet<>();
        HashSet<SHBE_PersonID> RSLUniqueDependentChildrenOver10EffectedPersonIDs;
        RSLUniqueDependentChildrenOver10EffectedPersonIDs = new HashSet<>();
        HashSet<SHBE_PersonID> RSLUniqueIndividualsEffectedNonDependentsEffectedPersonIDs;
        RSLUniqueIndividualsEffectedNonDependentsEffectedPersonIDs = new HashSet<>();
        HashMap<SHBE_ClaimID, Integer> RSLMaxNumberOfDependentsInClaimWhenUO;
        RSLMaxNumberOfDependentsInClaimWhenUO = new HashMap<>();

        // Groups help order the table output. Keys are the group type and 
        // values are ordered sets of keys for writing rows.
        HashMap<String, HashSet<SHBE_ClaimID>> Groups;
        Groups = new HashMap<>();

        HashSet<SHBE_ClaimID> PermanantlyLeftUOButRemainedInSHBEClaimIDs;
        PermanantlyLeftUOButRemainedInSHBEClaimIDs = new HashSet<>();
        Groups.put(sPermanantlyLeftUOButRemainedInSHBE, PermanantlyLeftUOButRemainedInSHBEClaimIDs);

        HashSet<SHBE_ClaimID> PermanantlyLeftUOButRemainedInSHBEPostcodeChangedClaimIDs;
        PermanantlyLeftUOButRemainedInSHBEPostcodeChangedClaimIDs = new HashSet<>();
        Groups.put(sPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged, PermanantlyLeftUOButRemainedInSHBEPostcodeChangedClaimIDs);

        HashSet<SHBE_ClaimID> PermanantlyLeftUOButRemainedInSHBEHouseholdSizeIncreasedClaimIDs;
        PermanantlyLeftUOButRemainedInSHBEHouseholdSizeIncreasedClaimIDs = new HashSet<>();
        Groups.put(sPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased, PermanantlyLeftUOButRemainedInSHBEHouseholdSizeIncreasedClaimIDs);

        HashSet<SHBE_ClaimID> TravellerClaimIDs;
        TravellerClaimIDs = new HashSet<>();
        Groups.put(sTravellers, TravellerClaimIDs);

        HashSet<SHBE_ClaimID> TTNot1Or4AndUnderOccupyingClaimIDs;
        TTNot1Or4AndUnderOccupyingClaimIDs = new HashSet<>();
        Groups.put(sTTNot1Or4AndUnderOccupying, TTNot1Or4AndUnderOccupyingClaimIDs);

        HashSet<SHBE_ClaimID> TT1_To_TT3OrTT6ClaimIDs;
        TT1_To_TT3OrTT6ClaimIDs = new HashSet<>();
        Groups.put(sTT1_To_TT3OrTT6, TT1_To_TT3OrTT6ClaimIDs);

        HashSet<SHBE_ClaimID> TT4_To_TT3OrTT6ClaimIDs;
        TT4_To_TT3OrTT6ClaimIDs = new HashSet<>();
        Groups.put(sTT4_To_TT3OrTT6, TT4_To_TT3OrTT6ClaimIDs);

        HashSet<SHBE_ClaimID> TT3OrTT6_To_TT1ClaimIDs;
        TT3OrTT6_To_TT1ClaimIDs = new HashSet<>();
        Groups.put(sTT3OrTT6_To_TT1, TT3OrTT6_To_TT1ClaimIDs);

        HashSet<SHBE_ClaimID> TT3OrTT6_To_TT4ClaimIDs;
        TT3OrTT6_To_TT4ClaimIDs = new HashSet<>();
        Groups.put(sTT3OrTT6_To_TT4, TT3OrTT6_To_TT4ClaimIDs);

//        HashSet<String> NoValidPostcodeChange;
//        NoValidPostcodeChange = new HashSet<String>();
//        NoValidPostcodeChange.addAll(tClaimRefs);
//        groups.put(sNoValidPostcodeChange, NoValidPostcodeChange);
        HashSet<SHBE_ClaimID> ValidPostcodeChangeClaimIDs;
        ValidPostcodeChangeClaimIDs = new HashSet<>();
        Groups.put(sNoValidPostcodeChange, ValidPostcodeChangeClaimIDs);

        HashSet<SHBE_ClaimID> ChangedTTClaimIDs;
        ChangedTTClaimIDs = new HashSet<>();
        Groups.put(sChangedTT, ChangedTTClaimIDs);

        HashSet<SHBE_ClaimID> UOAtSomePointClaimIDs;
        UOAtSomePointClaimIDs = new HashSet<>();
        Groups.put(sUOAtSomePoint, UOAtSomePointClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1AtSomePointClaimIDs;
        UOTT1AtSomePointClaimIDs = new HashSet<>();
        Groups.put(sUOTT1AtSomePoint, UOTT1AtSomePointClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4AtSomePointClaimIDs;
        UOTT4AtSomePointClaimIDs = new HashSet<>();
        Groups.put(sUOTT4AtSomePoint, UOTT4AtSomePointClaimIDs);

        HashSet<SHBE_ClaimID> AlwaysUOTT1FromStartClaimIDs;
        AlwaysUOTT1FromStartClaimIDs = new HashSet<>();
        AlwaysUOTT1FromStartClaimIDs.addAll(CouncilClaimIDs);
        Groups.put(sAlwaysUOTT1FromStart, AlwaysUOTT1FromStartClaimIDs);

        HashSet<SHBE_ClaimID> AlwaysUOTT1FromStartExceptWhenSuspendedClaimIDs;
        AlwaysUOTT1FromStartExceptWhenSuspendedClaimIDs = new HashSet<>();
        AlwaysUOTT1FromStartExceptWhenSuspendedClaimIDs.addAll(CouncilClaimIDs);
        Groups.put(sAlwaysUOTT1FromStartExceptWhenSuspended, AlwaysUOTT1FromStartExceptWhenSuspendedClaimIDs);

        HashSet<SHBE_ClaimID> AlwaysUOTT1FromWhenStartedClaimIDs;
        AlwaysUOTT1FromWhenStartedClaimIDs = new HashSet<>();
        //AlwaysUOFromWhenStarted.addAll(tClaimRefs);
        Groups.put(sAlwaysUOTT1FromWhenStarted, AlwaysUOTT1FromWhenStartedClaimIDs);

        HashSet<SHBE_ClaimID> AlwaysUOTT4FromStartClaimIDs;
        AlwaysUOTT4FromStartClaimIDs = new HashSet<>();
        AlwaysUOTT4FromStartClaimIDs.addAll(RSLClaimIDs);
        Groups.put(sAlwaysUOTT4FromStart, AlwaysUOTT4FromStartClaimIDs);

        HashSet<SHBE_ClaimID> AlwaysUOTT4FromStartExceptWhenSuspendedClaimIDs;
        AlwaysUOTT4FromStartExceptWhenSuspendedClaimIDs = new HashSet<>();
        AlwaysUOTT4FromStartExceptWhenSuspendedClaimIDs.addAll(RSLClaimIDs);
        Groups.put(sAlwaysUOTT4FromStartExceptWhenSuspended, AlwaysUOTT4FromStartExceptWhenSuspendedClaimIDs);

        HashSet<SHBE_ClaimID> AlwaysUOTT4FromWhenStartedClaimIDs;
        AlwaysUOTT4FromWhenStartedClaimIDs = new HashSet<>();
        //AlwaysUOFromWhenStarted.addAll(tClaimRefs);
        Groups.put(sAlwaysUOTT4FromWhenStarted, AlwaysUOTT4FromWhenStartedClaimIDs);

        HashSet<SHBE_ClaimID> IntermitantUOClaimIDs;
        IntermitantUOClaimIDs = new HashSet<>();
        Groups.put(sIntermitantUO, IntermitantUOClaimIDs);

        HashSet<SHBE_ClaimID> UO_To_LeftSHBEAtSomePointClaimIDs;
        UO_To_LeftSHBEAtSomePointClaimIDs = new HashSet<>();
        Groups.put(sUO_To_LeftSHBEAtSomePoint, UO_To_LeftSHBEAtSomePointClaimIDs);

        HashSet<SHBE_ClaimID> UO_To_LeftSHBETheVeryNextMonthClaimIDs;
        UO_To_LeftSHBETheVeryNextMonthClaimIDs = new HashSet<>();
        Groups.put(sUO_To_LeftSHBETheVeryNextMonth, UO_To_LeftSHBETheVeryNextMonthClaimIDs);

//        HashSet<SHBE_ClaimID> UO_To_LeftSHBEBetweenOneAndTwoMonthsClaimIDs;
//        UO_To_LeftSHBEBetweenOneAndTwoMonthsClaimIDs = new HashSet<SHBE_ClaimID>();
//        Groups.put(sUO_To_LeftSHBEBetweenOneAndTwoMonths, UO_To_LeftSHBEBetweenOneAndTwoMonthsClaimIDs);
//
//        HashSet<SHBE_ClaimID> UO_To_LeftSHBEBetweenTwoAndThreeMonthsClaimIDs;
//        UO_To_LeftSHBEBetweenTwoAndThreeMonthsClaimIDs = new HashSet<SHBE_ClaimID>();
//        Groups.put(sUO_To_LeftSHBEBetweenTwoAndThreeMonths, UO_To_LeftSHBEBetweenTwoAndThreeMonthsClaimIDs);
        HashSet<SHBE_ClaimID> UO_To_LeftSHBEAndNotReturnedClaimIDs;
        UO_To_LeftSHBEAndNotReturnedClaimIDs = new HashSet<>();
        Groups.put(sUO_To_LeftSHBE_NotReturned, UO_To_LeftSHBEAndNotReturnedClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBEAndNotReturnedClaimIDs;
        UOTT1_To_LeftSHBEAndNotReturnedClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_LeftSHBE_NotReturned, UOTT1_To_LeftSHBEAndNotReturnedClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBEAndNotReturnedClaimIDs;
        UOTT4_To_LeftSHBEAndNotReturnedClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_LeftSHBE_NotReturned, UOTT4_To_LeftSHBEAndNotReturnedClaimIDs);

        HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBEAndNotReturnedClaimIDs;
        UOTT3OrTT6_To_LeftSHBEAndNotReturnedClaimIDs = new HashSet<>();
        Groups.put(sUOTT3OrTT6_To_LeftSHBE_NotReturned, UOTT3OrTT6_To_LeftSHBEAndNotReturnedClaimIDs);

        HashSet<SHBE_ClaimID> UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEAndNotReturnedClaimIDs;
        UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEAndNotReturnedClaimIDs = new HashSet<>();
        Groups.put(sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned, UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEAndNotReturnedClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs;
        UOTT1_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_LeftSHBE_ReturnedAsUOTT1, UOTT1_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT1ClaimIDs;
        UOTT1_To_LeftSHBE_ReturnedAsTT1ClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT1, UOTT1_To_LeftSHBE_ReturnedAsTT1ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs;
        UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6, UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs;
        UOTT1_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_LeftSHBE_ReturnedAsUOTT4, UOTT1_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT4ClaimIDs;
        UOTT1_To_LeftSHBE_ReturnedAsTT4ClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT4, UOTT1_To_LeftSHBE_ReturnedAsTT4ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs;
        UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7, UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT8ClaimIDs;
        UOTT1_To_LeftSHBE_ReturnedAsTT8ClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT8, UOTT1_To_LeftSHBE_ReturnedAsTT8ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT9ClaimIDs;
        UOTT1_To_LeftSHBE_ReturnedAsTT9ClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_LeftSHBE_ReturnedAsTT9, UOTT1_To_LeftSHBE_ReturnedAsTT9ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs;
        UOTT4_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_LeftSHBE_ReturnedAsUOTT1, UOTT4_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT1ClaimIDs;
        UOTT4_To_LeftSHBE_ReturnedAsTT1ClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT1, UOTT4_To_LeftSHBE_ReturnedAsTT1ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs;
        UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6, UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs;
        UOTT4_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_LeftSHBE_ReturnedAsUOTT4, UOTT4_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT4ClaimIDs;
        UOTT4_To_LeftSHBE_ReturnedAsTT4ClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT4, UOTT4_To_LeftSHBE_ReturnedAsTT4ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs;
        UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7, UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT8ClaimIDs;
        UOTT4_To_LeftSHBE_ReturnedAsTT8ClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT8, UOTT4_To_LeftSHBE_ReturnedAsTT8ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT9ClaimIDs;
        UOTT4_To_LeftSHBE_ReturnedAsTT9ClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_LeftSHBE_ReturnedAsTT9, UOTT4_To_LeftSHBE_ReturnedAsTT9ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs;
        UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs = new HashSet<>();
        Groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1, UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1ClaimIDs;
        UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1ClaimIDs = new HashSet<>();
        Groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1, UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs;
        UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs = new HashSet<>();
        Groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6, UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs;
        UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs = new HashSet<>();
        Groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4, UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4ClaimIDs;
        UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4ClaimIDs = new HashSet<>();
        Groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4, UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs;
        UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs = new HashSet<>();
        Groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7, UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8ClaimIDs;
        UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8ClaimIDs = new HashSet<>();
        Groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8, UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9ClaimIDs;
        UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9ClaimIDs = new HashSet<>();
        Groups.put(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9, UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePointClaimIDs;
        UOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePointClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                UOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePointClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePointClaimIDs;
        UOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePointClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                UOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePointClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBEClaimIDs;
        UOTT1_To_LeftSHBEClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_LeftSHBE, UOTT1_To_LeftSHBEClaimIDs);

//        HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBEReturnedAsTT1orTT4;
//        UOTT1_To_LeftSHBEReturnedAsTT1orTT4 = new HashSet<SHBE_ClaimID>();
//        groups.put(sUOTT1_To_LeftSHBEReturnedAsTT1orTT4, UOTT1_To_LeftSHBEReturnedAsTT1orTT4);
//
//        HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBEReturnedAsTT3OrTT6;
//        UOTT1_To_LeftSHBEReturnedAsTT3OrTT6 = new HashSet<SHBE_ClaimID>();
//        groups.put(sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6, UOTT1_To_LeftSHBEReturnedAsTT3OrTT6);
        HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBEClaimIDs;
        UOTT4_To_LeftSHBEClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_LeftSHBE, UOTT4_To_LeftSHBEClaimIDs);

//        HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBEReturnedAsTT1orTT4;
//        UOTT4_To_LeftSHBEReturnedAsTT1orTT4 = new HashSet<SHBE_ClaimID>();
//        groups.put(sUOTT4_To_LeftSHBEReturnedAsTT1orTT4, UOTT4_To_LeftSHBEReturnedAsTT1orTT4);
//
//        HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBEReturnedAsTT3OrTT6;
//        UOTT4_To_LeftSHBEReturnedAsTT3OrTT6 = new HashSet<SHBE_ClaimID>();
//        groups.put(sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6, UOTT4_To_LeftSHBEReturnedAsTT3OrTT6);
        HashSet<SHBE_ClaimID> UO_NotUOClaimIDs;
        UO_NotUOClaimIDs = new HashSet<>();

        HashSet<SHBE_ClaimID> UO_NotUO_UO_NotUOClaimIDs;
        UO_NotUO_UO_NotUOClaimIDs = new HashSet<>();
        Groups.put(sUO_NotUO_UO_NotUO, UO_NotUO_UO_NotUOClaimIDs);

        HashSet<SHBE_ClaimID> UO_NotUO_UO_NotUO_UO_NotUOClaimIDs;
        UO_NotUO_UO_NotUO_UO_NotUOClaimIDs = new HashSet<>();
        Groups.put(sUO_NotUO_UO_NotUO_UO_NotUO, UO_NotUO_UO_NotUO_UO_NotUOClaimIDs);

        HashSet<SHBE_ClaimID> UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUOClaimIDs;
        UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUOClaimIDs = new HashSet<>();
        Groups.put(sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO, UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUOClaimIDs);

        HashSet<SHBE_ClaimID> UO_NotUO_UOClaimIDs;
        UO_NotUO_UOClaimIDs = new HashSet<>();
        Groups.put(sUO_NotUO_UO, UO_NotUO_UOClaimIDs);

        HashSet<SHBE_ClaimID> UO_NotUO_UO_NotUO_UOClaimIDs;
        UO_NotUO_UO_NotUO_UOClaimIDs = new HashSet<>();
        Groups.put(sUO_NotUO_UO_NotUO_UO, UO_NotUO_UO_NotUO_UOClaimIDs);

        HashSet<SHBE_ClaimID> UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs;
        UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs = new HashSet<>();
        Groups.put(sUO_NotUO_UO_NotUO_UO_NotUO_UO, UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs);

        HashSet<SHBE_ClaimID> UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs;
        UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs = new HashSet<>();
        Groups.put(sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO, UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_NotUO_InSHBE_PostcodeChangedClaimIDs;
        UOTT1_To_NotUO_InSHBE_PostcodeChangedClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_NotUO_InSHBE_PostcodeChanged, UOTT1_To_NotUO_InSHBE_PostcodeChangedClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_UOTT1_PostcodeChangedClaimIDs;
        UOTT1_To_UOTT1_PostcodeChangedClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_UOTT1_PostcodeChanged, UOTT1_To_UOTT1_PostcodeChangedClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeChangedClaimIDs;
        UOTT1_To_TT1_PostcodeChangedClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_TT1_PostcodeChanged, UOTT1_To_TT1_PostcodeChangedClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_UOTT4_PostcodeChangedClaimIDs;
        UOTT1_To_UOTT4_PostcodeChangedClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_UOTT4_PostcodeChanged, UOTT1_To_UOTT4_PostcodeChangedClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_TT4_PostcodeChangedClaimIDs;
        UOTT1_To_TT4_PostcodeChangedClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_TT4_PostcodeChanged, UOTT1_To_TT4_PostcodeChangedClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_NotUO_InSHBE_PostcodeChangedClaimIDs;
        UOTT4_To_NotUO_InSHBE_PostcodeChangedClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_NotUO_InSHBE_PostcodeChanged, UOTT4_To_NotUO_InSHBE_PostcodeChangedClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_UOTT1_PostcodeChangedClaimIDs;
        UOTT4_To_UOTT1_PostcodeChangedClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_UOTT1_PostcodeChanged, UOTT4_To_UOTT1_PostcodeChangedClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_TT1_PostcodeChangedClaimIDs;
        UOTT4_To_TT1_PostcodeChangedClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_TT1_PostcodeChanged, UOTT4_To_TT1_PostcodeChangedClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_UOTT4_PostcodeChangedClaimIDs;
        UOTT4_To_UOTT4_PostcodeChangedClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_UOTT4_PostcodeChanged, UOTT4_To_UOTT4_PostcodeChangedClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeChangedClaimIDs;
        UOTT4_To_TT4_PostcodeChangedClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_TT4_PostcodeChanged, UOTT4_To_TT4_PostcodeChangedClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_TT3OrTT6ClaimIDs;
        UOTT1_To_TT3OrTT6ClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_TT3OrTT6, UOTT1_To_TT3OrTT6ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_TT3OrTT6AtSomePointClaimIDs;
        UOTT1_To_TT3OrTT6AtSomePointClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_TT3OrTT6AtSomePoint, UOTT1_To_TT3OrTT6AtSomePointClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs;
        UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999, UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePointClaimIDs;
        UOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePointClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint, UOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePointClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_TT3OrTT6NotDoneNextChangeClaimIDs;
        UOTT1_To_TT3OrTT6NotDoneNextChangeClaimIDs = new HashSet<>();

        HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBEClaimIDs;
        UOTT3OrTT6_To_LeftSHBEClaimIDs = new HashSet<>();
        Groups.put(sUOTT3OrTT6_To_LeftSHBE, UOTT3OrTT6_To_LeftSHBEClaimIDs);

        HashSet<SHBE_ClaimID> UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEClaimIDs;
        UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEClaimIDs = new HashSet<>();
        Groups.put(sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE, UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_TT3OrTT6ClaimIDs;
        UOTT4_To_TT3OrTT6ClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_TT3OrTT6, UOTT4_To_TT3OrTT6ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_TT3OrTT6AtSomePointClaimIDs;
        UOTT4_To_TT3OrTT6AtSomePointClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_TT3OrTT6AtSomePoint, UOTT4_To_TT3OrTT6AtSomePointClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs;
        UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999, UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePointClaimIDs;
        UOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePointClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint, UOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePointClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_TT3OrTT6NotDoneNextChangeClaimIDs;
        UOTT4_To_TT3OrTT6NotDoneNextChangeClaimIDs = new HashSet<>();

        HashSet<SHBE_ClaimID> TT3OrTT6_To_UOTT1ClaimIDs;
        TT3OrTT6_To_UOTT1ClaimIDs = new HashSet<>();
        Groups.put(sTT3OrTT6_To_UOTT1, TT3OrTT6_To_UOTT1ClaimIDs);

        HashSet<SHBE_ClaimID> TT3OrTT6_To_UOTT4ClaimIDs;
        TT3OrTT6_To_UOTT4ClaimIDs = new HashSet<>();
        Groups.put(sTT3OrTT6_To_UOTT4, TT3OrTT6_To_UOTT4ClaimIDs);

        HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedClaimIDs;
        TT1_To_UOTT1_PostcodeUnchangedClaimIDs = new HashSet<>();
        Groups.put(sTT1_To_UOTT1_PostcodeUnchanged, TT1_To_UOTT1_PostcodeUnchangedClaimIDs);
//        HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month;
//        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month = new HashSet<SHBE_ClaimID>();
//        groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
//                TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month);
        HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs;
        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs = new HashSet<>();
        Groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs);
        HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs;
        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs = new HashSet<>();
        Groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs);
        HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs;
        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs = new HashSet<>();
        Groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs);
        HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs;
        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs = new HashSet<>();
        Groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs);
        HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs;
        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs = new HashSet<>();
        Groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs);
        HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs;
        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs = new HashSet<>();
        Groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs);
        HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs;
        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs = new HashSet<>();
        Groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs);
        HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs;
        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs = new HashSet<>();
        Groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs);

        HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2MonthsClaimIDs;
        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2MonthsClaimIDs = new HashSet<>();
        Groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
                TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2MonthsClaimIDs);
        HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3MonthsClaimIDs;
        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3MonthsClaimIDs = new HashSet<>();
        Groups.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months,
                TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3MonthsClaimIDs);

        //HashSet<SHBE_ClaimID> UOTT1OrTT1_To_UOTT4;
        //UOTT1OrTT1_To_UOTT4 = new HashSet<SHBE_ClaimID>();
        //groups.put(sUOTT1OrTT1_To_UOTT4, UOTT1OrTT1_To_UOTT4);
        HashSet<SHBE_ClaimID> UOTT1_To_UOTT4ClaimIDs;
        UOTT1_To_UOTT4ClaimIDs = new HashSet<>();
        Groups.put(sUOTT1_To_UOTT4, UOTT1_To_UOTT4ClaimIDs);
        HashSet<SHBE_ClaimID> TT1_To_UOTT4ClaimIDs;
        TT1_To_UOTT4ClaimIDs = new HashSet<>();
        Groups.put(sTT1_To_UOTT4, TT1_To_UOTT4ClaimIDs);
        HashSet<SHBE_ClaimID> TT1_To_UOTT4GettingDHPClaimIDs;
        TT1_To_UOTT4GettingDHPClaimIDs = new HashSet<>();
        Groups.put(sTT1_To_UOTT4GettingDHP, TT1_To_UOTT4GettingDHPClaimIDs);

        HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedClaimIDs;
        TT4_To_UOTT4_PostcodeUnchangedClaimIDs = new HashSet<>();
        Groups.put(sTT4_To_UOTT4_PostcodeUnchanged, TT4_To_UOTT4_PostcodeUnchangedClaimIDs);
//        HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month;
//        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month = new HashSet<SHBE_ClaimID>();
//        groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
//                TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month);        
        HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs;
        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs = new HashSet<>();
        Groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs);
        HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs;
        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs = new HashSet<>();
        Groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs);
        HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs;
        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs = new HashSet<>();
        Groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs);
        HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs;
        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs = new HashSet<>();
        Groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs);
        HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs;
        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs = new HashSet<>();
        Groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs);
        HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs;
        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs = new HashSet<>();
        Groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs);
        HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs;
        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs = new HashSet<>();
        Groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs);
        HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs
                = new HashSet<>();
        Groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs);

        HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2MonthsClaimIDs
                = new HashSet<>();
        Groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
                TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2MonthsClaimIDs);
        HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3MonthsClaimIDs
                = new HashSet<>();
        Groups.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months,
                TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3MonthsClaimIDs);

        //HashSet<SHBE_ClaimID> UOTT4OrTT4_To_UOTT1;
        //UOTT4OrTT4_To_UOTT1 = new HashSet<SHBE_ClaimID>();
        //groups.put(sUOTT4OrTT4_To_UOTT1, UOTT4OrTT4_To_UOTT1);
        //HashSet<SHBE_ClaimID> UOTT4OrTT4_To_UOTT1InArrears;
        //UOTT4OrTT4_To_UOTT1InArrears = new HashSet<SHBE_ClaimID>();
        //groups.put(sUOTT4OrTT4_To_UOTT1InArrears, UOTT4OrTT4_To_UOTT1InArrears);
        //HashSet<SHBE_ClaimID> UOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP;
        //UOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP = new HashSet<SHBE_ClaimID>();
        //groups.put(sUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
        //        UOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP);
        HashSet<SHBE_ClaimID> UOTT4_To_UOTT1ClaimIDs = new HashSet<>();
        Groups.put(sUOTT4_To_UOTT1, UOTT4_To_UOTT1ClaimIDs);
        HashSet<SHBE_ClaimID> UOTT4_To_UOTT1InArrearsClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT4_To_UOTT1InArrears, UOTT4_To_UOTT1InArrearsClaimIDs);
        HashSet<SHBE_ClaimID> UOTT4_To_UOTT1GettingDHPClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT4_To_UOTT1GettingDHP,
                UOTT4_To_UOTT1GettingDHPClaimIDs);
        HashSet<SHBE_ClaimID> UOTT4_To_UOTT1InArrearsAndGettingDHPClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT4_To_UOTT1InArrearsAndGettingDHP,
                UOTT4_To_UOTT1InArrearsAndGettingDHPClaimIDs);
        HashSet<SHBE_ClaimID> TT4_To_UOTT1ClaimIDs = new HashSet<>();
        Groups.put(sTT4_To_UOTT1, TT4_To_UOTT1ClaimIDs);
        HashSet<SHBE_ClaimID> TT4_To_UOTT1InArrearsClaimIDs = new HashSet<>();
        Groups.put(sTT4_To_UOTT1InArrears, TT4_To_UOTT1InArrearsClaimIDs);
        HashSet<SHBE_ClaimID> TT4_To_UOTT1GettingDHPClaimIDs = new HashSet<>();
        Groups.put(sTT4_To_UOTT1GettingDHP,
                TT4_To_UOTT1GettingDHPClaimIDs);
        HashSet<SHBE_ClaimID> TT4_To_UOTT1InArrearsAndGettingDHPClaimIDs
                = new HashSet<>();
        Groups.put(sTT4_To_UOTT1InArrearsAndGettingDHP,
                TT4_To_UOTT1InArrearsAndGettingDHPClaimIDs);

        HashSet<SHBE_ClaimID> InArrearsAtSomePointClaimIDs = new HashSet<>();
        HashSet<SHBE_ClaimID> DHPAtSomePoint                = new HashSet<>();
        HashSet<SHBE_ClaimID> InArrearsAtSomePoint_And_DHPAtSomePointClaimIDs
                = new HashSet<>();
        Groups.put(sInArrearsAtSomePoint_And_DHPAtSomePoint,
                InArrearsAtSomePoint_And_DHPAtSomePointClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT1_To_TT1_PostcodeUnchanged, UOTT1_To_TT1_PostcodeUnchangedClaimIDs);
//        HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month
//                = new HashSet<SHBE_ClaimID>();
//        groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
//                UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month);
        HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs);
        HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs);
        HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs);
        HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs);
        HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs);
        HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs);
        HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs);
        HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2MonthsClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
                UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2MonthsClaimIDs);
        HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3MonthsClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months,
                UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3MonthsClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT4_To_TT4_PostcodeUnchanged, UOTT4_To_TT4_PostcodeUnchangedClaimIDs);
//        HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month
//                = new HashSet<SHBE_ClaimID>();
//        groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
//                UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month);
        HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs);
        HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs);
        HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs);
        HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs);
        HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs);
        HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs);
        HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs);
        HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs);
        HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2MonthsClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
                UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2MonthsClaimIDs);
        HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3MonthsClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months,
                UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3MonthsClaimIDs);

        HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchanged1MonthPreviousClaimIDs
                = new HashSet<>();
        HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchanged2MonthsPreviousClaimIDs
                = new HashSet<>();
        HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchanged3MonthsPreviousClaimIDs
                = new HashSet<>();

        HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchanged1MonthPreviousClaimIDs
                = new HashSet<>();
        HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchanged2MonthsPreviousClaimIDs
                = new HashSet<>();
        HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchanged3MonthsPreviousClaimIDs
                = new HashSet<>();

        HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchanged1MonthPreviousClaimIDs
                = new HashSet<>();
        HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchanged2MonthsPreviousClaimIDs
                = new HashSet<>();
        HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchanged3MonthsPreviousClaimIDs
                = new HashSet<>();

        HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchanged1MonthPreviousClaimIDs
                = new HashSet<>();
        HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchanged2MonthsPreviousClaimIDs
                = new HashSet<>();
        HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchanged3MonthsPreviousClaimIDs
                = new HashSet<>();

        HashSet<SHBE_ClaimID> UOTT1_ToTT1_PostcodeChangedClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT1_To_TT1_PostcodeChanged, UOTT1_ToTT1_PostcodeChangedClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1_ToUOTT1_PostcodeChangedClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT1_To_UOTT1_PostcodeChanged, UOTT1_ToUOTT1_PostcodeChangedClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_ToTT4_PostcodeChangedClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT4_To_TT4_PostcodeChanged, UOTT4_ToTT4_PostcodeChangedClaimIDs);

        HashSet<SHBE_ClaimID> UOTT4_ToUOTT4_PostcodeChangedClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT4_To_UOTT4_PostcodeChanged, UOTT4_ToUOTT4_PostcodeChangedClaimIDs);

        HashSet<SHBE_ClaimID> TT1_ToUOTT1_PostcodeChangedClaimIDs
                = new HashSet<>();
        Groups.put(sTT1_To_UOTT1_PostcodeChanged, TT1_ToUOTT1_PostcodeChangedClaimIDs);

        HashSet<SHBE_ClaimID> TT4_ToUOTT4_PostcodeChangedClaimIDs
                = new HashSet<>();
        Groups.put(sTT4_To_UOTT4_PostcodeChanged, TT4_ToUOTT4_PostcodeChangedClaimIDs);

        HashSet<SHBE_ClaimID> UOClaimsRecievingDHPClaimIDs
                = new HashSet<>();
        Groups.put(sUOClaimsRecievingDHP, UOClaimsRecievingDHPClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1ClaimsInRentArrearsAtSomePointClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT1ClaimsInRentArrearsAtSomePoint, UOTT1ClaimsInRentArrearsAtSomePointClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1ClaimsInRentArrearsOver500AtSomePointClaimIDs
                = new HashSet<>();
        Groups.put(sUOTT1ClaimsInRentArrearsOver500AtSomePoint, UOTT1ClaimsInRentArrearsOver500AtSomePointClaimIDs);

        HashSet<SHBE_ClaimID> UOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePointClaimIDs;
        UOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePointClaimIDs = new HashSet<>();
        Groups.put(sUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint, UOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePointClaimIDs);

        SHBE_ClaimID ClaimID;
        String ClaimRef;
        // Initialise aggregateStatistics and generalStatistics
        TreeMap<String, BigDecimal> AggregateStatistics = new TreeMap<>();
        String s_ = DW_Strings.symbol_underscore;
        ite = ClaimIDs.iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            ClaimRef = cid2c.get(ClaimID);
            AggregateStatistics.put(ClaimRef + s_ + sTotal_DHP, BigDecimal.ZERO);
            AggregateStatistics.put(ClaimRef + s_ + sTotalCount_DHP, BigDecimal.ZERO);
            AggregateStatistics.put(ClaimRef + s_ + sTotal_HBLossDueToUO, BigDecimal.ZERO);
            AggregateStatistics.put(ClaimRef + s_ + sTotalCount_HBLossDueToUO, BigDecimal.ZERO);
            AggregateStatistics.put(ClaimRef + s_ + sMax_Arrears, BigDecimal.ZERO);
            AggregateStatistics.put(ClaimRef + s_ + sTotalCount_InArrears, BigDecimal.ZERO);
            AggregateStatistics.put(ClaimRef + s_ + sTotalCount_InArrears0To10, BigDecimal.ZERO);
            AggregateStatistics.put(ClaimRef + s_ + sTotalCount_InArrears10To100, BigDecimal.ZERO);
            AggregateStatistics.put(ClaimRef + s_ + sTotalCount_InArrears100To500, BigDecimal.ZERO);
            AggregateStatistics.put(ClaimRef + s_ + sTotalCount_InArrearsOver500, BigDecimal.ZERO);
            AggregateStatistics.put(ClaimRef + s_ + sTotalCount_UnderOccupancy, BigDecimal.ZERO);
        }

        // Use sets?
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOInApril2013, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOAfterApril2013, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOAfterChangePostcodeAndOrTT, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ChangePostcodeAndOrTTToAvoidUO, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_StayPutAndAvoidedUO, BigDecimal.ZERO);
//        generalStatistics.put(sCostOfUOToTaxPayer, BigDecimal.ZERO);
        HashMap<SHBE_ClaimID, Integer> DHP_Totals;
        DHP_Totals = new HashMap<>();
        // Initialise tableValues (part 2) and DHP_Totals
        ite = ClaimIDs.iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            ClaimRef = cid2c.get(ClaimID);
            TableValues.put(ClaimRef + s_ + sTT, s);
            TableValues.put(ClaimRef + s_ + sUnderOccupancy, s);
            TableValues.put(ClaimRef + s_ + sP, s);
            TableValues.put(ClaimRef + s_ + sWHBE, s);
            TableValues.put(ClaimRef + s_ + sWERA, s);
            TableValues.put(ClaimRef + s_ + sPSI, s);
            TableValues.put(ClaimRef + s_ + sSHBC, s);
            TableValues.put(ClaimRef + s_ + sRTHBCC, s);
            TableValues.put(ClaimRef + s_ + sCEG, s);
            TableValues.put(ClaimRef + s_ + sHS, s);
            TableValues.put(ClaimRef + s_ + sND, s);
            TableValues.put(ClaimRef + s_ + sCD, s);
            TableValues.put(ClaimRef + s_ + sNDUO, s);
            TableValues.put(ClaimRef + s_ + sCO16, s);
            TableValues.put(ClaimRef + s_ + sFCU10, s);
            TableValues.put(ClaimRef + s_ + sMCU10, s);
            TableValues.put(ClaimRef + s_ + sFC10To16, s);
            TableValues.put(ClaimRef + s_ + sMC10To16, s);
            TableValues.put(ClaimRef + s_ + sNB, s);
            TableValues.put(ClaimRef + s_ + sBR, s);
            TableValues.put(ClaimRef + s_ + sCDoB, s);
            TableValues.put(ClaimRef + s_ + sCA, s);
            TableValues.put(ClaimRef + s_ + sPDoB, s);
            TableValues.put(ClaimRef + s_ + sPA, s);
            TableValues.put(ClaimRef + s_ + sCG, s);
            TableValues.put(ClaimRef + s_ + sPG, s);
            TableValues.put(ClaimRef + s_ + sDisability, s);
            TableValues.put(ClaimRef + s_ + sDisabilityPremium, s);
            TableValues.put(ClaimRef + s_ + sDisabilitySevere, s);
            TableValues.put(ClaimRef + s_ + sDisabilityEnhanced, s);
            TableValues.put(ClaimRef + s_ + sDisabledChild, s);
            TableValues.put(ClaimRef + s_ + sPDeath, s);
            TableValues.put(ClaimRef + s_ + sHBDP, s);
            TableValues.put(ClaimRef + s_ + sA, s);
            DHP_Totals.put(ClaimID, 0);
        }

        Iterator<Integer> includeIte = include.iterator();
        int i;

        String header = "ClaimRef, ";
        if (includePreUnderOccupancyValues) {
            Iterator<Integer> ite2 = NotMonthlyUO.iterator();
            while (ite2.hasNext()) {
                i = ite2.next();
                UKP_YM3 YM3 = shbeHandler.getYM3(SHBEFilenames[i]);
                header += YM3 + DW_Strings.special_commaSpace;
            }
        }

        /**
         * Initialise arrearsDiffs and arrearsDiffCounts. These store the
         * aggregate differences in arrears from one time to the next and the
         * counts of the number of times there is a difference (only when claim
         * is Council tenant under occupying consecutively). The keys are YM3
         * and the differences are from the previous YM3.
         */
        HashMap<UKP_YM3, Double> arrears;
        HashMap<UKP_YM3, Double> arrearsCounts;
        HashMap<UKP_YM3, Double> arrearsDiffs;
        HashMap<UKP_YM3, Double> arrearsDiffCounts;
        arrears = new HashMap<>();
        arrearsCounts = new HashMap<>();
        arrearsDiffs = new HashMap<>();
        arrearsDiffCounts = new HashMap<>();
        arrearsDiffs.put(YM30, 0.0d);
        arrearsDiffCounts.put(YM30, 0.0d);

        boolean initFirst;
        initFirst = false;
        // Load first data
        while (!initFirst) {
            i = includeIte.next();
            SHBEFilename1 = SHBEFilenames[i];
            YM31 = shbeHandler.getYM3(SHBEFilename1);
            year1 = shbeHandler.getYear(SHBEFilename1);
            month1 = shbeHandler.getMonthNumber(SHBEFilename1);
            CouncilUOSet1 = CouncilUOSets.get(YM31);
            if (CouncilUOSet1 != null) {
                RSLUOSet1 = RSLUOSets.get(YM31);
                shbeRecs1 = shbeHandler.getRecords(YM31, env.HOOME);
                initFirst = true;
                //arrearsDiffs.put(YM3, 0.0d);
                //arrearsDiffCounts.put(YM3, 0.0d);
            }
            header += YM31;
        }
        //TreeMap<String, SHBE_Record> aRecords;
        recs1 = shbeRecs1.getRecords(env.HOOME);
        Map<SHBE_ClaimID, SHBE_Record> recs0 = null;
//        HashMap<SHBE_ClaimID, SHBE_Record> cRecords;
//        cRecords = null;

        //SHBE_Record aSHBE_Record;
        HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedThisMonth
                = new HashSet<>();
        HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedThisMonth
                = new HashSet<>();
        HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedThisMonth
                = new HashSet<>();
        HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedThisMonth
                = new HashSet<>();

        Map<SHBE_ClaimID, SHBE_PersonID> claimIDToClaimantPersonIDLookup
                = shbeRecs1.getClaimIDToClaimantPersonIDLookup(env.HOOME);
        Map<SHBE_ClaimID, SHBE_PersonID> claimIDToPartnerPersonIDLookup
                = shbeRecs1.getClaimIDToPartnerPersonIDLookup(env.HOOME);

        // Add TT of all ClaimRefs to result
        Object[] processResult;
        totalCount_UOClaims = 0;
        totalCount_UOCouncilClaims = 0;
        totalCount_UORSLClaims = 0;
        totalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE = 0;
        totalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO = 0;
        totalHouseholdSizeExcludingPartnersCouncilSHBE = 0;
        totalHouseholdSizeCouncilSHBE = 0;
        totalHouseholdSizeExcludingPartnersCouncilUO = 0;
        totalHouseholdSizeCouncilUO = 0;
        totalHouseholdSizeExcludingPartnersRSLSHBE = 0;
        totalHouseholdSizeRSLSHBE = 0;
        totalHouseholdSizeExcludingPartnersRSLUO = 0;
        totalHouseholdSizeRSLUO = 0;
        ite = ClaimIDs.iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            ClaimRef = cid2c.get(ClaimID);
           SHBE_Record r1 = recs1.get(ClaimID);
            processResult = process(
                    claimIDToClaimantPersonIDLookup,
                    claimIDToPartnerPersonIDLookup,
                    UOClaims, AggregateStatistics, GeneralStatistics,
                    ClaimID, ClaimRef,
                    year0, month0, YM30,
                    year1, month1, YM31,
                    r1, recs0,
                    //cRecords,
                    TableValues,
                    CouncilUOSet0,
                    RSLUOSet0,
                    CouncilUOSet1,
                    RSLUOSet1,
                    arrears,
                    arrearsCounts,
                    arrearsDiffs,
                    arrearsDiffCounts,
                    CouncilUniqueIndividualsEffectedPersonIDs,
                    CouncilUniqueClaimantsEffectedPersonIDs,
                    CouncilUniquePartnersEffectedPersonIDs,
                    CouncilUniqueDependentChildrenUnder10EffectedPersonIDs,
                    CouncilUniqueDependentChildrenOver10EffectedPersonIDs,
                    CouncilUniqueNonDependentsEffectedPersonIDs,
                    CouncilMaxNumberOfDependentsInClaimWhenUO,
                    RSLUniqueIndividualsEffectedPersonIDs,
                    RSLUniqueClaimantsEffectedPersonIDs,
                    RSLUniquePartnersEffectedPersonIDs,
                    RSLUniqueDependentChildrenUnder10EffectedPersonIDs,
                    RSLUniqueDependentChildrenOver10EffectedPersonIDs,
                    RSLUniqueIndividualsEffectedNonDependentsEffectedPersonIDs,
                    RSLMaxNumberOfDependentsInClaimWhenUO,
                    PermanantlyLeftUOButRemainedInSHBEClaimIDs,
                    PermanantlyLeftUOButRemainedInSHBEPostcodeChangedClaimIDs,
                    PermanantlyLeftUOButRemainedInSHBEHouseholdSizeIncreasedClaimIDs,
                    TravellerClaimIDs,
                    TTNot1Or4AndUnderOccupyingClaimIDs,
                    TT1_To_TT3OrTT6ClaimIDs,
                    TT4_To_TT3OrTT6ClaimIDs,
                    TT3OrTT6_To_TT1ClaimIDs,
                    TT3OrTT6_To_TT4ClaimIDs,
                    ValidPostcodeChangeClaimIDs,
                    ChangedTTClaimIDs,
                    UOAtSomePointClaimIDs,
                    UOTT1AtSomePointClaimIDs,
                    UOTT4AtSomePointClaimIDs,
                    AlwaysUOTT1FromStartClaimIDs,
                    AlwaysUOTT1FromStartExceptWhenSuspendedClaimIDs,
                    AlwaysUOTT1FromWhenStartedClaimIDs,
                    AlwaysUOTT4FromStartClaimIDs,
                    AlwaysUOTT4FromStartExceptWhenSuspendedClaimIDs,
                    AlwaysUOTT4FromWhenStartedClaimIDs,
                    IntermitantUOClaimIDs,
                    UO_To_LeftSHBEAtSomePointClaimIDs,
                    UOTT1_To_LeftSHBEClaimIDs,
                    UOTT4_To_LeftSHBEClaimIDs,
                    UOTT3OrTT6_To_LeftSHBEClaimIDs,
                    UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEClaimIDs,
                    //UOTT1_To_LeftSHBEReturnedAsTT1orTT4,
                    //UOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
                    //UOTT4_To_LeftSHBEAndHaveNotReturned,
                    //UOTT4_To_LeftSHBEReturnedAsTT1orTT4,
                    //UOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
                    //UO_To_LeftSHBETheVeryNextMonth,
                    UO_To_LeftSHBETheVeryNextMonthClaimIDs,
                    //UO_To_LeftSHBEBetweenOneAndTwoMonthsClaimIDs,
                    //UO_To_LeftSHBEBetweenTwoAndThreeMonthsClaimIDs,
                    UO_To_LeftSHBEAndNotReturnedClaimIDs,
                    UOTT1_To_LeftSHBEAndNotReturnedClaimIDs,
                    UOTT4_To_LeftSHBEAndNotReturnedClaimIDs,
                    UOTT3OrTT6_To_LeftSHBEAndNotReturnedClaimIDs,
                    UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEAndNotReturnedClaimIDs,
                    UOTT1_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs,
                    UOTT1_To_LeftSHBE_ReturnedAsTT1ClaimIDs,
                    UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs,
                    UOTT1_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs,
                    UOTT1_To_LeftSHBE_ReturnedAsTT4ClaimIDs,
                    UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs,
                    UOTT1_To_LeftSHBE_ReturnedAsTT8ClaimIDs,
                    UOTT1_To_LeftSHBE_ReturnedAsTT9ClaimIDs,
                    UOTT4_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs,
                    UOTT4_To_LeftSHBE_ReturnedAsTT1ClaimIDs,
                    UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs,
                    UOTT4_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs,
                    UOTT4_To_LeftSHBE_ReturnedAsTT4ClaimIDs,
                    UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs,
                    UOTT4_To_LeftSHBE_ReturnedAsTT8ClaimIDs,
                    UOTT4_To_LeftSHBE_ReturnedAsTT9ClaimIDs,
                    UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs,
                    UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1ClaimIDs,
                    UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs,
                    UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs,
                    UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4ClaimIDs,
                    UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs,
                    UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8ClaimIDs,
                    UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9ClaimIDs,
                    UOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePointClaimIDs,
                    UOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePointClaimIDs,
                    UO_NotUOClaimIDs,
                    UO_NotUO_UOClaimIDs,
                    UO_NotUO_UO_NotUOClaimIDs,
                    UO_NotUO_UO_NotUO_UOClaimIDs,
                    UO_NotUO_UO_NotUO_UO_NotUOClaimIDs,
                    UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs,
                    UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUOClaimIDs,
                    UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs,
                    UOTT1_To_NotUO_InSHBE_PostcodeChangedClaimIDs,
                    UOTT1_To_UOTT1_PostcodeChangedClaimIDs,
                    UOTT1_To_TT1_PostcodeChangedClaimIDs,
                    UOTT1_To_UOTT4_PostcodeChangedClaimIDs,
                    UOTT1_To_TT4_PostcodeChangedClaimIDs,
                    UOTT4_To_NotUO_InSHBE_PostcodeChangedClaimIDs,
                    UOTT4_To_UOTT1_PostcodeChangedClaimIDs,
                    UOTT4_To_TT1_PostcodeChangedClaimIDs,
                    UOTT4_To_UOTT4_PostcodeChangedClaimIDs,
                    UOTT4_To_TT4_PostcodeChangedClaimIDs,
                    UOTT1_To_TT3OrTT6ClaimIDs,
                    UOTT1_To_TT3OrTT6AtSomePointClaimIDs,
                    UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs,
                    UOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePointClaimIDs,
                    UOTT1_To_TT3OrTT6NotDoneNextChangeClaimIDs,
                    UOTT4_To_TT3OrTT6ClaimIDs,
                    UOTT4_To_TT3OrTT6AtSomePointClaimIDs,
                    UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs,
                    UOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePointClaimIDs,
                    UOTT4_To_TT3OrTT6NotDoneNextChangeClaimIDs,
                    TT3OrTT6_To_UOTT1ClaimIDs,
                    TT3OrTT6_To_UOTT4ClaimIDs,
                    TT1_To_UOTT1_PostcodeUnchangedClaimIDs,
                    TT1_To_UOTT1_PostcodeUnchangedClaimIDs,
                    //TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
                    TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs,
                    TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs,
                    TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs,
                    TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs,
                    TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs,
                    TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs,
                    TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs,
                    TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs,
                    TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2MonthsClaimIDs,
                    TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3MonthsClaimIDs,
                    TT1_To_UOTT1_PostcodeUnchanged1MonthPreviousClaimIDs,
                    TT1_To_UOTT1_PostcodeUnchanged2MonthsPreviousClaimIDs,
                    TT1_To_UOTT1_PostcodeUnchanged3MonthsPreviousClaimIDs,
                    //UOTT1OrTT1_To_UOTT4,
                    UOTT1_To_UOTT4ClaimIDs,
                    TT1_To_UOTT4ClaimIDs,
                    TT1_To_UOTT4GettingDHPClaimIDs,
                    TT4_To_UOTT4_PostcodeUnchangedClaimIDs,
                    TT4_To_UOTT4_PostcodeUnchangedThisMonth,
                    //TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
                    TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs,
                    TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs,
                    TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs,
                    TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs,
                    TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs,
                    TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs,
                    TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs,
                    TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs,
                    TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2MonthsClaimIDs,
                    TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3MonthsClaimIDs,
                    TT4_To_UOTT4_PostcodeUnchanged1MonthPreviousClaimIDs,
                    TT4_To_UOTT4_PostcodeUnchanged2MonthsPreviousClaimIDs,
                    TT4_To_UOTT4_PostcodeUnchanged3MonthsPreviousClaimIDs,
                    //UOTT4OrTT4_To_UOTT1,
                    //UOTT4OrTT4_To_UOTT1InArrears,
                    //UOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
                    UOTT4_To_UOTT1ClaimIDs,
                    UOTT4_To_UOTT1InArrearsClaimIDs,
                    UOTT4_To_UOTT1GettingDHPClaimIDs,
                    UOTT4_To_UOTT1InArrearsAndGettingDHPClaimIDs,
                    TT4_To_UOTT1ClaimIDs,
                    TT4_To_UOTT1InArrearsClaimIDs,
                    TT4_To_UOTT1GettingDHPClaimIDs,
                    TT4_To_UOTT1InArrearsAndGettingDHPClaimIDs,
                    InArrearsAtSomePointClaimIDs,
                    DHPAtSomePoint,
                    UOTT1_To_TT1_PostcodeUnchangedClaimIDs,
                    UOTT1_To_TT1_PostcodeUnchangedThisMonth,
                    //UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
                    UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs,
                    UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs,
                    UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs,
                    UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs,
                    UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs,
                    UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs,
                    UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs,
                    UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs,
                    UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2MonthsClaimIDs,
                    UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3MonthsClaimIDs,
                    UOTT1_To_TT1_PostcodeUnchanged1MonthPreviousClaimIDs,
                    UOTT1_To_TT1_PostcodeUnchanged2MonthsPreviousClaimIDs,
                    UOTT1_To_TT1_PostcodeUnchanged3MonthsPreviousClaimIDs,
                    UOTT4_To_TT4_PostcodeUnchangedClaimIDs,
                    UOTT4_To_TT4_PostcodeUnchangedThisMonth,
                    //UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
                    UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs,
                    UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs,
                    UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs,
                    UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs,
                    UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs,
                    UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs,
                    UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs,
                    UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs,
                    UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2MonthsClaimIDs,
                    UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3MonthsClaimIDs,
                    UOTT4_To_TT4_PostcodeUnchanged1MonthPreviousClaimIDs,
                    UOTT4_To_TT4_PostcodeUnchanged2MonthsPreviousClaimIDs,
                    UOTT4_To_TT4_PostcodeUnchanged3MonthsPreviousClaimIDs,
                    UOTT1_ToTT1_PostcodeChangedClaimIDs,
                    UOTT1_ToUOTT1_PostcodeChangedClaimIDs,
                    UOTT4_ToTT4_PostcodeChangedClaimIDs,
                    UOTT4_ToUOTT4_PostcodeChangedClaimIDs,
                    TT1_ToUOTT1_PostcodeChangedClaimIDs,
                    TT4_ToUOTT4_PostcodeChangedClaimIDs,
                    UOClaimsRecievingDHPClaimIDs,
                    UOTT1ClaimsInRentArrearsAtSomePointClaimIDs,
                    UOTT1ClaimsInRentArrearsOver500AtSomePointClaimIDs,
                    UOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePointClaimIDs);
            if ((Boolean) processResult[0]) {
                totalCount_UOClaims++;
            }
            if ((Boolean) processResult[1]) {
                totalCount_UOCouncilClaims++;
                totalHouseholdSizeExcludingPartnersCouncilSHBE += (Integer) processResult[5];
                totalHouseholdSizeExcludingPartnersCouncilUO += (Integer) processResult[6];
                totalHouseholdSizeCouncilSHBE += (Integer) processResult[7];
                totalHouseholdSizeCouncilUO += (Integer) processResult[8];
            }
            if ((Boolean) processResult[2]) {
                totalCount_UORSLClaims++;
                totalHouseholdSizeExcludingPartnersRSLSHBE += (Integer) processResult[5];
                totalHouseholdSizeExcludingPartnersRSLUO += (Integer) processResult[6];
                totalHouseholdSizeRSLSHBE += (Integer) processResult[7];
                totalHouseholdSizeRSLUO += (Integer) processResult[8];
            }
            if ((Boolean) processResult[3]) {
                totalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE++;
            }
            if ((Boolean) processResult[4]) {
                totalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO++;
            }
        }

        TT1_To_UOTT1_PostcodeUnchanged1MonthPreviousClaimIDs = TT1_To_UOTT1_PostcodeUnchangedThisMonth;
        TT4_To_UOTT4_PostcodeUnchanged1MonthPreviousClaimIDs = TT4_To_UOTT4_PostcodeUnchangedThisMonth;
        TT1_To_UOTT1_PostcodeUnchangedThisMonth = new HashSet<>();
        TT4_To_UOTT4_PostcodeUnchangedThisMonth = new HashSet<>();
        UOTT1_To_TT1_PostcodeUnchanged1MonthPreviousClaimIDs = UOTT1_To_TT1_PostcodeUnchangedThisMonth;
        UOTT4_To_TT4_PostcodeUnchanged1MonthPreviousClaimIDs = UOTT4_To_TT4_PostcodeUnchangedThisMonth;
        UOTT1_To_TT1_PostcodeUnchangedThisMonth = new HashSet<>();
        UOTT4_To_TT4_PostcodeUnchangedThisMonth = new HashSet<>();

        String yearMonth1;
        yearMonth1 = year1 + "-" + month1;
        TotalCount_CumulativeUniqueClaims.put(
                yearMonth1,
                UOClaims.size());
        TotalCount_UOClaims.put(
                yearMonth1,
                totalCount_UOClaims);
        TotalCount_UOCouncilClaims.put(
                yearMonth1,
                totalCount_UOCouncilClaims);
        TotalCount_UORSLClaims.put(
                yearMonth1,
                totalCount_UORSLClaims);
        TotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE.put(
                yearMonth1,
                totalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE);
        TotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO.put(
                yearMonth1,
                totalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO);
        TotalHouseholdSizeExcludingPartnersCouncilSHBEs.put(
                yearMonth1,
                totalHouseholdSizeExcludingPartnersCouncilSHBE);
        TotalHouseholdSizeCouncilSHBEs.put(
                yearMonth1,
                totalHouseholdSizeCouncilSHBE);
        TotalHouseholdSizeExcludingPartnersCouncilUOs.put(
                yearMonth1,
                totalHouseholdSizeExcludingPartnersCouncilUO);
        TotalHouseholdSizeCouncilUOs.put(
                yearMonth1,
                totalHouseholdSizeCouncilUO);
        TotalHouseholdSizeExcludingPartnersRSLSHBEs.put(
                yearMonth1,
                totalHouseholdSizeExcludingPartnersRSLSHBE);
        TotalHouseholdSizeRSLSHBEs.put(
                yearMonth1,
                totalHouseholdSizeRSLSHBE);
        TotalHouseholdSizeExcludingPartnersRSLUOs.put(
                yearMonth1,
                totalHouseholdSizeExcludingPartnersRSLUO);
        TotalHouseholdSizeRSLUOs.put(
                yearMonth1,
                totalHouseholdSizeRSLUO);

        // Iterate over the rest of the data
        while (includeIte.hasNext()) {
            totalCount_UOClaims = 0;
            totalCount_UOCouncilClaims = 0;
            totalCount_UORSLClaims = 0;
            totalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE = 0;
            totalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO = 0;
            totalHouseholdSizeExcludingPartnersCouncilSHBE = 0;
            totalHouseholdSizeExcludingPartnersCouncilUO = 0;
            totalHouseholdSizeExcludingPartnersRSLSHBE = 0;
            totalHouseholdSizeExcludingPartnersRSLUO = 0;
            totalHouseholdSizeCouncilSHBE = 0;
            totalHouseholdSizeCouncilUO = 0;
            totalHouseholdSizeRSLSHBE = 0;
            totalHouseholdSizeRSLUO = 0;

            //SHBEFilename0 = SHBEFilename1;
            //YM30 = YM31;
            YM30 = new UKP_YM3(YM31);
            year0 = year1;
            month0 = month1;
            //SHBE_Records0 = SHBE_Records1;
            recs0 = recs1;
            CouncilUOSet0 = CouncilUOSet1;
            RSLUOSet0 = RSLUOSet1;

            i = includeIte.next();
            SHBEFilename1 = SHBEFilenames[i];
            YM31 = shbeHandler.getYM3(SHBEFilename1);
            year1 = shbeHandler.getYear(SHBEFilename1);
            month1 = shbeHandler.getMonthNumber(SHBEFilename1);
            shbeRecs1 = shbeHandler.getRecords(YM31, env.HOOME);
            //cRecords = Records0;
            recs1 = shbeRecs1.getRecords(env.HOOME);
            CouncilUOSet1 = CouncilUOSets.get(YM31);
            RSLUOSet1 = RSLUOSets.get(YM31);
            header += DW_Strings.special_commaSpace + YM31;
            ite = ClaimIDs.iterator();
            while (ite.hasNext()) {
                ClaimID = ite.next();
                ClaimRef = cid2c.get(ClaimID);
               SHBE_Record r1 = recs1.get(ClaimID);
                processResult = process(
                        claimIDToClaimantPersonIDLookup,
                        claimIDToPartnerPersonIDLookup,
                        UOClaims, AggregateStatistics, GeneralStatistics,
                        ClaimID, ClaimRef,
                        year0, month0, YM30,
                        year1, month1, YM31,
                        r1, recs0,
                        //cRecords,
                        TableValues,
                        CouncilUOSet0,
                        RSLUOSet0,
                        CouncilUOSet1,
                        RSLUOSet1,
                        arrears,
                        arrearsCounts,
                        arrearsDiffs,
                        arrearsDiffCounts,
                        CouncilUniqueIndividualsEffectedPersonIDs,
                        CouncilUniqueClaimantsEffectedPersonIDs,
                        CouncilUniquePartnersEffectedPersonIDs,
                        CouncilUniqueDependentChildrenUnder10EffectedPersonIDs,
                        CouncilUniqueDependentChildrenOver10EffectedPersonIDs,
                        CouncilUniqueNonDependentsEffectedPersonIDs,
                        CouncilMaxNumberOfDependentsInClaimWhenUO,
                        RSLUniqueIndividualsEffectedPersonIDs,
                        RSLUniqueClaimantsEffectedPersonIDs,
                        RSLUniquePartnersEffectedPersonIDs,
                        RSLUniqueDependentChildrenUnder10EffectedPersonIDs,
                        RSLUniqueDependentChildrenOver10EffectedPersonIDs,
                        RSLUniqueIndividualsEffectedNonDependentsEffectedPersonIDs,
                        RSLMaxNumberOfDependentsInClaimWhenUO,
                        PermanantlyLeftUOButRemainedInSHBEClaimIDs,
                        PermanantlyLeftUOButRemainedInSHBEPostcodeChangedClaimIDs,
                        PermanantlyLeftUOButRemainedInSHBEHouseholdSizeIncreasedClaimIDs,
                        TravellerClaimIDs,
                        TTNot1Or4AndUnderOccupyingClaimIDs,
                        TT1_To_TT3OrTT6ClaimIDs,
                        TT4_To_TT3OrTT6ClaimIDs,
                        TT3OrTT6_To_TT1ClaimIDs,
                        TT3OrTT6_To_TT4ClaimIDs,
                        ValidPostcodeChangeClaimIDs,
                        ChangedTTClaimIDs,
                        UOAtSomePointClaimIDs,
                        UOTT1AtSomePointClaimIDs,
                        UOTT4AtSomePointClaimIDs,
                        AlwaysUOTT1FromStartClaimIDs,
                        AlwaysUOTT1FromStartExceptWhenSuspendedClaimIDs,
                        AlwaysUOTT1FromWhenStartedClaimIDs,
                        AlwaysUOTT4FromStartClaimIDs,
                        AlwaysUOTT4FromStartExceptWhenSuspendedClaimIDs,
                        AlwaysUOTT4FromWhenStartedClaimIDs,
                        IntermitantUOClaimIDs,
                        UO_To_LeftSHBEAtSomePointClaimIDs,
                        UOTT1_To_LeftSHBEClaimIDs,
                        UOTT4_To_LeftSHBEClaimIDs,
                        UOTT3OrTT6_To_LeftSHBEClaimIDs,
                        UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEClaimIDs,
                        //UOTT1_To_LeftSHBEReturnedAsTT1orTT4,
                        //UOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
                        //UOTT4_To_LeftSHBEAndHaveNotReturned,
                        //UOTT4_To_LeftSHBEReturnedAsTT1orTT4,
                        //UOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
                        //UO_To_LeftSHBETheVeryNextMonth,
                        UO_To_LeftSHBETheVeryNextMonthClaimIDs,
                        //UO_To_LeftSHBEBetweenOneAndTwoMonthsClaimIDs,
                        //UO_To_LeftSHBEBetweenTwoAndThreeMonthsClaimIDs,
                        UO_To_LeftSHBEAndNotReturnedClaimIDs,
                        UOTT1_To_LeftSHBEAndNotReturnedClaimIDs,
                        UOTT4_To_LeftSHBEAndNotReturnedClaimIDs,
                        UOTT3OrTT6_To_LeftSHBEAndNotReturnedClaimIDs,
                        UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEAndNotReturnedClaimIDs,
                        UOTT1_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs,
                        UOTT1_To_LeftSHBE_ReturnedAsTT1ClaimIDs,
                        UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs,
                        UOTT1_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs,
                        UOTT1_To_LeftSHBE_ReturnedAsTT4ClaimIDs,
                        UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs,
                        UOTT1_To_LeftSHBE_ReturnedAsTT8ClaimIDs,
                        UOTT1_To_LeftSHBE_ReturnedAsTT9ClaimIDs,
                        UOTT4_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs,
                        UOTT4_To_LeftSHBE_ReturnedAsTT1ClaimIDs,
                        UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs,
                        UOTT4_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs,
                        UOTT4_To_LeftSHBE_ReturnedAsTT4ClaimIDs,
                        UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs,
                        UOTT4_To_LeftSHBE_ReturnedAsTT8ClaimIDs,
                        UOTT4_To_LeftSHBE_ReturnedAsTT9ClaimIDs,
                        UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs,
                        UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1ClaimIDs,
                        UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs,
                        UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs,
                        UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4ClaimIDs,
                        UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs,
                        UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8ClaimIDs,
                        UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9ClaimIDs,
                        UOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePointClaimIDs,
                        UOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePointClaimIDs,
                        UO_NotUOClaimIDs,
                        UO_NotUO_UOClaimIDs,
                        UO_NotUO_UO_NotUOClaimIDs,
                        UO_NotUO_UO_NotUO_UOClaimIDs,
                        UO_NotUO_UO_NotUO_UO_NotUOClaimIDs,
                        UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs,
                        UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUOClaimIDs,
                        UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs,
                        UOTT1_To_NotUO_InSHBE_PostcodeChangedClaimIDs,
                        UOTT1_To_UOTT1_PostcodeChangedClaimIDs,
                        UOTT1_To_TT1_PostcodeChangedClaimIDs,
                        UOTT1_To_UOTT4_PostcodeChangedClaimIDs,
                        UOTT1_To_TT4_PostcodeChangedClaimIDs,
                        UOTT4_To_NotUO_InSHBE_PostcodeChangedClaimIDs,
                        UOTT4_To_UOTT1_PostcodeChangedClaimIDs,
                        UOTT4_To_TT1_PostcodeChangedClaimIDs,
                        UOTT4_To_UOTT4_PostcodeChangedClaimIDs,
                        UOTT4_To_TT4_PostcodeChangedClaimIDs,
                        UOTT1_To_TT3OrTT6ClaimIDs,
                        UOTT1_To_TT3OrTT6AtSomePointClaimIDs,
                        UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs,
                        UOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePointClaimIDs,
                        UOTT1_To_TT3OrTT6NotDoneNextChangeClaimIDs,
                        UOTT4_To_TT3OrTT6ClaimIDs,
                        UOTT4_To_TT3OrTT6AtSomePointClaimIDs,
                        UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs,
                        UOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePointClaimIDs,
                        UOTT4_To_TT3OrTT6NotDoneNextChangeClaimIDs,
                        TT3OrTT6_To_UOTT1ClaimIDs,
                        TT3OrTT6_To_UOTT4ClaimIDs,
                        TT1_To_UOTT1_PostcodeUnchangedClaimIDs,
                        TT1_To_UOTT1_PostcodeUnchangedClaimIDs,
                        //TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
                        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs,
                        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs,
                        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs,
                        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs,
                        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs,
                        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs,
                        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs,
                        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs,
                        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2MonthsClaimIDs,
                        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3MonthsClaimIDs,
                        TT1_To_UOTT1_PostcodeUnchanged1MonthPreviousClaimIDs,
                        TT1_To_UOTT1_PostcodeUnchanged2MonthsPreviousClaimIDs,
                        TT1_To_UOTT1_PostcodeUnchanged3MonthsPreviousClaimIDs,
                        //UOTT1OrTT1_To_UOTT4,
                        UOTT1_To_UOTT4ClaimIDs,
                        TT1_To_UOTT4ClaimIDs,
                        TT1_To_UOTT4GettingDHPClaimIDs,
                        TT4_To_UOTT4_PostcodeUnchangedClaimIDs,
                        TT4_To_UOTT4_PostcodeUnchangedThisMonth,
                        //TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
                        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs,
                        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs,
                        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs,
                        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs,
                        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs,
                        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs,
                        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs,
                        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs,
                        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2MonthsClaimIDs,
                        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3MonthsClaimIDs,
                        TT4_To_UOTT4_PostcodeUnchanged1MonthPreviousClaimIDs,
                        TT4_To_UOTT4_PostcodeUnchanged2MonthsPreviousClaimIDs,
                        TT4_To_UOTT4_PostcodeUnchanged3MonthsPreviousClaimIDs,
                        //UOTT4OrTT4_To_UOTT1,
                        //UOTT4OrTT4_To_UOTT1InArrears,
                        //UOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
                        UOTT4_To_UOTT1ClaimIDs,
                        UOTT4_To_UOTT1InArrearsClaimIDs,
                        UOTT4_To_UOTT1GettingDHPClaimIDs,
                        UOTT4_To_UOTT1InArrearsAndGettingDHPClaimIDs,
                        TT4_To_UOTT1ClaimIDs,
                        TT4_To_UOTT1InArrearsClaimIDs,
                        TT4_To_UOTT1GettingDHPClaimIDs,
                        TT4_To_UOTT1InArrearsAndGettingDHPClaimIDs,
                        InArrearsAtSomePointClaimIDs,
                        DHPAtSomePoint,
                        UOTT1_To_TT1_PostcodeUnchangedClaimIDs,
                        UOTT1_To_TT1_PostcodeUnchangedThisMonth,
                        //UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
                        UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs,
                        UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs,
                        UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs,
                        UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs,
                        UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs,
                        UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs,
                        UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs,
                        UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs,
                        UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2MonthsClaimIDs,
                        UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3MonthsClaimIDs,
                        UOTT1_To_TT1_PostcodeUnchanged1MonthPreviousClaimIDs,
                        UOTT1_To_TT1_PostcodeUnchanged2MonthsPreviousClaimIDs,
                        UOTT1_To_TT1_PostcodeUnchanged3MonthsPreviousClaimIDs,
                        UOTT4_To_TT4_PostcodeUnchangedClaimIDs,
                        UOTT4_To_TT4_PostcodeUnchangedThisMonth,
                        //UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
                        UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs,
                        UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs,
                        UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs,
                        UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs,
                        UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs,
                        UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs,
                        UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs,
                        UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs,
                        UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2MonthsClaimIDs,
                        UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3MonthsClaimIDs,
                        UOTT4_To_TT4_PostcodeUnchanged1MonthPreviousClaimIDs,
                        UOTT4_To_TT4_PostcodeUnchanged2MonthsPreviousClaimIDs,
                        UOTT4_To_TT4_PostcodeUnchanged3MonthsPreviousClaimIDs,
                        UOTT1_ToTT1_PostcodeChangedClaimIDs,
                        UOTT1_ToUOTT1_PostcodeChangedClaimIDs,
                        UOTT4_ToTT4_PostcodeChangedClaimIDs,
                        UOTT4_ToUOTT4_PostcodeChangedClaimIDs,
                        TT1_ToUOTT1_PostcodeChangedClaimIDs,
                        TT4_ToUOTT4_PostcodeChangedClaimIDs,
                        UOClaimsRecievingDHPClaimIDs,
                        UOTT1ClaimsInRentArrearsAtSomePointClaimIDs,
                        UOTT1ClaimsInRentArrearsOver500AtSomePointClaimIDs,
                        UOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePointClaimIDs);
                if ((Boolean) processResult[0]) {
                    totalCount_UOClaims++;
                }
                if ((Boolean) processResult[1]) {
                    totalCount_UOCouncilClaims++;
                    totalHouseholdSizeExcludingPartnersCouncilSHBE += (Integer) processResult[5];
                    totalHouseholdSizeExcludingPartnersCouncilUO += (Integer) processResult[6];
                    totalHouseholdSizeCouncilSHBE += (Integer) processResult[7];
                    totalHouseholdSizeCouncilUO += (Integer) processResult[8];
                }
                if ((Boolean) processResult[2]) {
                    totalCount_UORSLClaims++;
                    totalHouseholdSizeExcludingPartnersRSLSHBE += (Integer) processResult[5];
                    totalHouseholdSizeExcludingPartnersRSLUO += (Integer) processResult[6];
                    totalHouseholdSizeRSLSHBE += (Integer) processResult[7];
                    totalHouseholdSizeRSLUO += (Integer) processResult[8];
                }
                if ((Boolean) processResult[3]) {
                    totalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE++;
                }
                if ((Boolean) processResult[4]) {
                    totalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO++;
                }
            }
            TT1_To_UOTT1_PostcodeUnchanged3MonthsPreviousClaimIDs = TT1_To_UOTT1_PostcodeUnchanged2MonthsPreviousClaimIDs;
            TT4_To_UOTT4_PostcodeUnchanged3MonthsPreviousClaimIDs = TT4_To_UOTT4_PostcodeUnchanged2MonthsPreviousClaimIDs;
            TT1_To_UOTT1_PostcodeUnchanged2MonthsPreviousClaimIDs = TT1_To_UOTT1_PostcodeUnchanged1MonthPreviousClaimIDs;
            TT4_To_UOTT4_PostcodeUnchanged2MonthsPreviousClaimIDs = TT4_To_UOTT4_PostcodeUnchanged1MonthPreviousClaimIDs;
            TT1_To_UOTT1_PostcodeUnchanged1MonthPreviousClaimIDs = TT1_To_UOTT1_PostcodeUnchangedThisMonth;
            TT4_To_UOTT4_PostcodeUnchanged1MonthPreviousClaimIDs = TT4_To_UOTT4_PostcodeUnchangedThisMonth;
            TT1_To_UOTT1_PostcodeUnchangedThisMonth = new HashSet<>();
            TT4_To_UOTT4_PostcodeUnchangedThisMonth = new HashSet<>();

            UOTT1_To_TT1_PostcodeUnchanged3MonthsPreviousClaimIDs = UOTT1_To_TT1_PostcodeUnchanged2MonthsPreviousClaimIDs;
            UOTT4_To_TT4_PostcodeUnchanged3MonthsPreviousClaimIDs = UOTT4_To_TT4_PostcodeUnchanged2MonthsPreviousClaimIDs;
            UOTT1_To_TT1_PostcodeUnchanged2MonthsPreviousClaimIDs = UOTT1_To_TT1_PostcodeUnchanged1MonthPreviousClaimIDs;
            UOTT4_To_TT4_PostcodeUnchanged2MonthsPreviousClaimIDs = UOTT4_To_TT4_PostcodeUnchanged1MonthPreviousClaimIDs;
            UOTT1_To_TT1_PostcodeUnchanged1MonthPreviousClaimIDs = UOTT1_To_TT1_PostcodeUnchangedThisMonth;
            UOTT4_To_TT4_PostcodeUnchanged1MonthPreviousClaimIDs = UOTT4_To_TT4_PostcodeUnchangedThisMonth;
            UOTT1_To_TT1_PostcodeUnchangedThisMonth = new HashSet<>();
            UOTT4_To_TT4_PostcodeUnchangedThisMonth = new HashSet<>();

            yearMonth1 = year1 + "-" + month1;
            TotalCount_CumulativeUniqueClaims.put(
                    yearMonth1,
                    UOClaims.size());
            TotalCount_UOClaims.put(
                    yearMonth1,
                    totalCount_UOClaims);
            TotalCount_UOCouncilClaims.put(
                    yearMonth1,
                    totalCount_UOCouncilClaims);
            TotalCount_UORSLClaims.put(
                    yearMonth1,
                    totalCount_UORSLClaims);
            TotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE.put(
                    yearMonth1,
                    totalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE);
            TotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO.put(
                    yearMonth1,
                    totalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO);
            TotalHouseholdSizeExcludingPartnersCouncilSHBEs.put(
                    yearMonth1,
                    totalHouseholdSizeExcludingPartnersCouncilSHBE);
            TotalHouseholdSizeCouncilSHBEs.put(
                    yearMonth1,
                    totalHouseholdSizeCouncilSHBE);
            TotalHouseholdSizeExcludingPartnersCouncilUOs.put(
                    yearMonth1,
                    totalHouseholdSizeExcludingPartnersCouncilUO);
            TotalHouseholdSizeCouncilUOs.put(
                    yearMonth1,
                    totalHouseholdSizeCouncilUO);
            TotalHouseholdSizeExcludingPartnersRSLSHBEs.put(
                    yearMonth1,
                    totalHouseholdSizeExcludingPartnersRSLSHBE);
            TotalHouseholdSizeRSLSHBEs.put(
                    yearMonth1,
                    totalHouseholdSizeRSLSHBE);
            TotalHouseholdSizeExcludingPartnersRSLUOs.put(
                    yearMonth1,
                    totalHouseholdSizeExcludingPartnersRSLUO);
            TotalHouseholdSizeRSLUOs.put(
                    yearMonth1,
                    totalHouseholdSizeRSLUO);
        }

        InArrearsAtSomePoint_And_DHPAtSomePointClaimIDs.addAll(InArrearsAtSomePointClaimIDs);
        InArrearsAtSomePoint_And_DHPAtSomePointClaimIDs.retainAll(DHPAtSomePoint);

//        UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.removeAll(UOTT1_To_LeftSHBEAndNotReturned);
//        UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999.removeAll(UOTT4_To_LeftSHBEAndNotReturned);
        UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs.retainAll(UOTT1_To_TT3OrTT6AtSomePointClaimIDs);
        UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs.retainAll(UOTT4_To_TT3OrTT6AtSomePointClaimIDs);

        header += DW_Strings.special_commaSpace + "HBDPTotal";

//        HashSet<SHBE_ClaimID> ValidPostcodeChangeClaimIDs; // Calculate by removing all from NoValidPostcodeChange.
//        ValidPostcodeChangeClaimIDs = new HashSet<SHBE_ClaimID>();
//        ValidPostcodeChangeClaimIDs.addAll(tClaimRefs);
//        ValidPostcodeChangeClaimIDs.removeAll(NoValidPostcodeChange);
//        groups.put("ValidPostcodeChange", ValidPostcodeChangeClaimIDs);
        HashSet<SHBE_ClaimID> NoValidPostcodeChange; // Calculate by removing all from NoValidPostcodeChange.
        NoValidPostcodeChange = new HashSet<>();
        NoValidPostcodeChange.addAll(ClaimIDs);
        NoValidPostcodeChange.removeAll(ValidPostcodeChangeClaimIDs);
        Groups.put(sNoValidPostcodeChange, ValidPostcodeChangeClaimIDs);

        HashSet<SHBE_ClaimID> NotChangedTT; // Calculate by removing all from ChangedTTClaimIDs.
        NotChangedTT = new HashSet<>();
        NotChangedTT.addAll(ClaimIDs);
        NotChangedTT.removeAll(ChangedTTClaimIDs);
        Groups.put("NotChangedTT", NotChangedTT);

        HashSet<SHBE_ClaimID> AlwaysUOFromStartNoValidPostcodeChangeNotChangedTT; // Calculate by intersect of sets.
        AlwaysUOFromStartNoValidPostcodeChangeNotChangedTT = new HashSet<>();
        AlwaysUOFromStartNoValidPostcodeChangeNotChangedTT.addAll(AlwaysUOTT1FromStartClaimIDs);
        AlwaysUOFromStartNoValidPostcodeChangeNotChangedTT.addAll(AlwaysUOTT4FromStartClaimIDs);
        AlwaysUOFromStartNoValidPostcodeChangeNotChangedTT.retainAll(NoValidPostcodeChange);
        AlwaysUOFromStartNoValidPostcodeChangeNotChangedTT.retainAll(NotChangedTT);
        Groups.put(sAlwaysUOFromStart__NoValidPostcodeChange_NotChangedTT, AlwaysUOFromStartNoValidPostcodeChangeNotChangedTT);

        HashSet<SHBE_ClaimID> AlwaysUOFromStartChangedTT; // Calculate by intersect of sets.
        AlwaysUOFromStartChangedTT = new HashSet<>();
        AlwaysUOFromStartChangedTT.addAll(ChangedTTClaimIDs);
        AlwaysUOFromStartChangedTT.retainAll(AlwaysUOTT1FromStartClaimIDs);
        AlwaysUOFromStartChangedTT.retainAll(AlwaysUOTT4FromStartClaimIDs);
        Groups.put(sAlwaysUOFromStart__ChangedTT, AlwaysUOFromStartChangedTT);

        HashSet<SHBE_ClaimID> AlwaysUOFromStartValidPostcodeChangeNotChangedTT; // Calculate by intersect of sets.
        AlwaysUOFromStartValidPostcodeChangeNotChangedTT = new HashSet<>();
        AlwaysUOFromStartValidPostcodeChangeNotChangedTT.addAll(AlwaysUOTT1FromStartClaimIDs);
        AlwaysUOFromStartValidPostcodeChangeNotChangedTT.addAll(AlwaysUOTT4FromStartClaimIDs);
        AlwaysUOFromStartValidPostcodeChangeNotChangedTT.removeAll(AlwaysUOFromStartChangedTT);
        AlwaysUOFromStartValidPostcodeChangeNotChangedTT.retainAll(ValidPostcodeChangeClaimIDs);
        Groups.put(sAlwaysUOFromStart__ValidPostcodeChange_NotChangedTT, AlwaysUOFromStartValidPostcodeChangeNotChangedTT);

        String aS;
        String key;
        ite = ClaimIDs.iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            ClaimRef = cid2c.get(ClaimID);
            key = ClaimRef + DW_Strings.symbol_underscore + sUnderOccupancy;
            aS = TableValues.get(key);
            if (aS.endsWith(DW_Strings.special_commaSpace)) {
                IntermitantUOClaimIDs.add(ClaimID);
            }
        }

        AlwaysUOTT1FromWhenStartedClaimIDs.removeAll(AlwaysUOTT1FromStartClaimIDs);
        AlwaysUOTT1FromWhenStartedClaimIDs.removeAll(IntermitantUOClaimIDs);
        AlwaysUOTT4FromWhenStartedClaimIDs.removeAll(AlwaysUOTT4FromStartClaimIDs);
        AlwaysUOTT4FromWhenStartedClaimIDs.removeAll(IntermitantUOClaimIDs);

        HashSet<SHBE_ClaimID> AlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT; // Calculate by intersect of sets.
        AlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT = new HashSet<>();
        AlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT.addAll(AlwaysUOTT1FromWhenStartedClaimIDs);
        AlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT.addAll(AlwaysUOTT4FromWhenStartedClaimIDs);
        AlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT.removeAll(ChangedTTClaimIDs);
        AlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT.retainAll(NoValidPostcodeChange);
        Groups.put(sAlwaysUOFromWhenStarted__NoValidPostcodeChange_NotChangedTT, AlwaysUOFromWhenStartedNoValidPostcodeChangeNotChangedTT);

        HashSet<SHBE_ClaimID> AlwaysUOFromWhenStartedChangedTT; // Calculate by intersect of sets.
        AlwaysUOFromWhenStartedChangedTT = new HashSet<>();
        AlwaysUOFromWhenStartedChangedTT.addAll(AlwaysUOTT1FromWhenStartedClaimIDs);
        AlwaysUOFromWhenStartedChangedTT.addAll(AlwaysUOTT4FromWhenStartedClaimIDs);
        AlwaysUOFromWhenStartedChangedTT.retainAll(ChangedTTClaimIDs);
        Groups.put(sAlwaysUOFromWhenStarted__ChangedTT, AlwaysUOFromWhenStartedChangedTT);

        HashSet<SHBE_ClaimID> AlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT; // Calculate by intersect of sets.
        AlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT = new HashSet<>();
        AlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT.addAll(AlwaysUOTT1FromWhenStartedClaimIDs);
        AlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT.addAll(AlwaysUOTT4FromWhenStartedClaimIDs);
        AlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT.removeAll(NoValidPostcodeChange);
        AlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT.removeAll(ChangedTTClaimIDs);
        Groups.put(sAlwaysUOFromWhenStarted__ValidPostcodeChange_NotChangedTT, AlwaysUOFromWhenStartedValidPostcodeChangeNotChangedTT);

        HashSet<SHBE_ClaimID> IntermitantUONoValidPostcodeChangeNotChangedTT;
        IntermitantUONoValidPostcodeChangeNotChangedTT = new HashSet<>();
        IntermitantUONoValidPostcodeChangeNotChangedTT.addAll(IntermitantUOClaimIDs);
        IntermitantUONoValidPostcodeChangeNotChangedTT.retainAll(NoValidPostcodeChange);
        IntermitantUONoValidPostcodeChangeNotChangedTT.retainAll(NotChangedTT);
        Groups.put(sIntermitantUO__NoValidPostcodeChange_NotChangedTT, IntermitantUONoValidPostcodeChangeNotChangedTT);

        HashSet<SHBE_ClaimID> IntermitantUOChangedTT;
        IntermitantUOChangedTT = new HashSet<>();
        IntermitantUOChangedTT.addAll(IntermitantUOClaimIDs);
        IntermitantUOChangedTT.retainAll(ChangedTTClaimIDs);
        Groups.put(sIntermitantUO__ChangedTT, IntermitantUOChangedTT);

        HashSet<SHBE_ClaimID> IntermitantUOValidPostcodeChangeNotChangedTT;
        IntermitantUOValidPostcodeChangeNotChangedTT = new HashSet<>();
        IntermitantUOValidPostcodeChangeNotChangedTT.addAll(IntermitantUOClaimIDs);
        IntermitantUOValidPostcodeChangeNotChangedTT.removeAll(NoValidPostcodeChange);
        IntermitantUOValidPostcodeChangeNotChangedTT.removeAll(ChangedTTClaimIDs);
        Groups.put(sIntermitantUO__ValidPostcodeChange_NotChangedTT, IntermitantUOValidPostcodeChangeNotChangedTT);

        checkSetsAndAddToGeneralStatistics(
                GeneralStatistics,
                ClaimIDs,
                YM3Start,
                YM31,
                UniqueIndividualsEffectedPersonIDs,
                UniqueDependentsAgedUnder10EffectedPersonIDs,
                UniqueDependentsAgedOver10EffectedPersonIDs,
                UniqueDependentsEffectedPersonIDs,
                CouncilClaimIDs,
                CouncilUniqueIndividualsEffectedPersonIDs,
                CouncilUniqueClaimantsEffectedPersonIDs,
                CouncilUniquePartnersEffectedPersonIDs,
                CouncilUniqueDependentChildrenUnder10EffectedPersonIDs,
                CouncilUniqueDependentChildrenOver10EffectedPersonIDs,
                CouncilUniqueNonDependentsEffectedPersonIDs,
                CouncilMaxNumberOfDependentsInClaimWhenUO,
                RSLClaimIDs,
                RSLUniqueIndividualsEffectedPersonIDs,
                RSLUniqueClaimantsEffectedPersonIDs,
                RSLUniquePartnersEffectedPersonIDs,
                RSLUniqueDependentChildrenUnder10EffectedPersonIDs,
                RSLUniqueDependentChildrenOver10EffectedPersonIDs,
                RSLUniqueIndividualsEffectedNonDependentsEffectedPersonIDs,
                RSLMaxNumberOfDependentsInClaimWhenUO,
                Groups);

        long totalHouseholdSize;
        double averageHouseholdSizeOfThoseUOAlwaysFromStart;
        Iterator<SHBE_ClaimID> iteS;
        // TT1
        totalHouseholdSize = 0;
        d = 0.0d;
        iteS = AlwaysUOTT1FromStartClaimIDs.iterator();
        while (iteS.hasNext()) {
            ClaimID = iteS.next();
            SHBE_Record rec = recs1.get(ClaimID);
            if (rec != null) {
                totalHouseholdSize += shbeHandler.getHouseholdSize(rec);
                d += 1.0d;
            }
        }
        if (d > 0) {
            //averageHouseholdSizeOfThoseUOAlwaysFromStart = (double) totalHouseholdSize / (double) AlwaysUOTT1FromStart.size();
            averageHouseholdSizeOfThoseUOAlwaysFromStart = (double) totalHouseholdSize / d;
        } else {
            averageHouseholdSizeOfThoseUOAlwaysFromStart = 0.0d;
        }
        GeneralStatistics.put(sAverageHouseholdSizeOfThoseUOTT1AlwaysFromStart,
                BigDecimal.valueOf(averageHouseholdSizeOfThoseUOAlwaysFromStart));
        // TT4
        totalHouseholdSize = 0;
        d = 0.0d;
        iteS = AlwaysUOTT4FromStartClaimIDs.iterator();
        while (iteS.hasNext()) {
            ClaimID = iteS.next();
            SHBE_Record rec = recs1.get(ClaimID);
            if (rec != null) {
                totalHouseholdSize += shbeHandler.getHouseholdSize(rec);
                d += 1.0d;
            }
        }
        if (d > 0) {
            //averageHouseholdSizeOfThoseUOAlwaysFromStart = (double) totalHouseholdSize / (double) AlwaysUOTT4FromStart.size();
            averageHouseholdSizeOfThoseUOAlwaysFromStart = (double) totalHouseholdSize / d;
        } else {
            averageHouseholdSizeOfThoseUOAlwaysFromStart = 0.0d;
        }
        GeneralStatistics.put(sAverageHouseholdSizeOfThoseUOTT4AlwaysFromStart,
                BigDecimal.valueOf(averageHouseholdSizeOfThoseUOAlwaysFromStart));

        GeneralStatistics.put(sTotalCount_AlwaysUOTT1FromStart,
                BigDecimal.valueOf(AlwaysUOTT1FromStartClaimIDs.size()));
        GeneralStatistics.put(sTotalCount_AlwaysUOTT1FromStartExceptWhenSuspended,
                BigDecimal.valueOf(AlwaysUOTT1FromStartExceptWhenSuspendedClaimIDs.size()));
        GeneralStatistics.put(sTotalCount_AlwaysUOTT1FromWhenStarted,
                BigDecimal.valueOf(AlwaysUOTT1FromWhenStartedClaimIDs.size()));
        GeneralStatistics.put(sTotalCount_AlwaysUOTT4FromStart,
                BigDecimal.valueOf(AlwaysUOTT4FromStartClaimIDs.size()));
        GeneralStatistics.put(sTotalCount_AlwaysUOTT4FromStartExceptWhenSuspended,
                BigDecimal.valueOf(AlwaysUOTT4FromStartExceptWhenSuspendedClaimIDs.size()));
        GeneralStatistics.put(sTotalCount_AlwaysUOTT4FromWhenStarted,
                BigDecimal.valueOf(AlwaysUOTT4FromWhenStartedClaimIDs.size()));

// Use sets?
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOInApril2013, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOAfterApril2013, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ExistingSHBEClaimsThatBecameUOAfterChangePostcodeAndOrTT, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_ChangePostcodeAndOrTTToAvoidUO, BigDecimal.ZERO);
//        generalStatistics.put(sTotalCount_StayPutAndAvoidedUO, BigDecimal.ZERO);
//        generalStatistics.put(sCostOfUOToTaxPayer, BigDecimal.ZERO);
        result[0] = header;
        result[1] = TableValues;
        result[2] = ClaimIDs;
        result[3] = Groups;
        result[4] = preUnderOccupancyValues;
        result[5] = AggregateStatistics;
        result[6] = GeneralStatistics;
        result[7] = TimeStatistics;
        result[8] = arrears;
        result[9] = arrearsCounts;
        result[10] = arrearsDiffs;
        result[11] = arrearsDiffCounts;
        return result;
    }

    protected void checkSetsAndAddToGeneralStatistics(
            TreeMap<String, BigDecimal> GeneralStatistics,
            HashSet<SHBE_ClaimID> ClaimRefs,
            UKP_YM3 YM3Start,
            UKP_YM3 YM3End,
            HashSet<SHBE_PersonID> UniqueIndividualsEffectedPersonIDs,
            HashSet<SHBE_PersonID> UniqueDependentsAgedUnder10EffectedPersonIDs,
            HashSet<SHBE_PersonID> UniqueDependentsAgedOver10EffectedPersonIDs,
            HashSet<SHBE_PersonID> UniqueDependentsEffectedPersonIDs,
            HashSet<SHBE_ClaimID> CouncilClaimRefs,
            HashSet<SHBE_PersonID> CouncilUniqueIndividualsEffectedPersonIDs,
            HashSet<SHBE_PersonID> CouncilUniqueClaimantsEffectedPersonIDs,
            HashSet<SHBE_PersonID> CouncilUniquePartnersEffectedPersonIDs,
            HashSet<SHBE_PersonID> CouncilUniqueDependentsAgedUnder10EffectedPersonIDs,
            HashSet<SHBE_PersonID> CouncilUniqueDependentsAgedOver10EffectedPersonIDs,
            HashSet<SHBE_PersonID> CouncilUniqueNonDependentsEffectedPersonIDs,
            HashMap<SHBE_ClaimID, Integer> CouncilMaxNumberOfDependentsInClaimWhenUO,
            HashSet<SHBE_ClaimID> RSLClaimRefs,
            HashSet<SHBE_PersonID> RSLUniqueIndividualsEffectedPersonIDs,
            HashSet<SHBE_PersonID> RSLUniqueClaimantsEffectedPersonIDs,
            HashSet<SHBE_PersonID> RSLUniquePartnersEffectedPersonIDs,
            HashSet<SHBE_PersonID> RSLUniqueDependentsAgedUnder10EffectedPersonIDs,
            HashSet<SHBE_PersonID> RSLUniqueDependentsAgedOver10EffectedPersonIDs,
            HashSet<SHBE_PersonID> RSLUniqueNonDependentsEffectedPersonIDs,
            HashMap<SHBE_ClaimID, Integer> RSLMaxNumberOfDependentsInClaimWhenUO,
            HashMap<String, HashSet<SHBE_ClaimID>> Groups) {
        SHBE_ClaimID ClaimID;
        int m;
        Iterator<SHBE_ClaimID> ite;

        long TotalCount_CouncilEffectedDependents;
        TotalCount_CouncilEffectedDependents = 0;
        ite = CouncilMaxNumberOfDependentsInClaimWhenUO.keySet().iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            m = CouncilMaxNumberOfDependentsInClaimWhenUO.get(ClaimID);
            TotalCount_CouncilEffectedDependents += m;
        }

        long TotalCount_RSLEffectedDependents;
        TotalCount_RSLEffectedDependents = 0;
        ite = RSLMaxNumberOfDependentsInClaimWhenUO.keySet().iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            m = RSLMaxNumberOfDependentsInClaimWhenUO.get(ClaimID);
            TotalCount_RSLEffectedDependents += m;
        }

        env.ge.log("From " + YM3Start + " to " + YM3End + " there were "
                + "the following counts of claims and individuals effected by "
                + "UnderOccupancy:", true);
        GeneralStatistics.put(sUO_To_LeftSHBEAtSomePoint,
                BigDecimal.valueOf(Groups.get(sUO_To_LeftSHBEAtSomePoint).size()));
        GeneralStatistics.put(
                sUOTT1_To_LeftSHBE,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_LeftSHBE).size()));
        GeneralStatistics.put(
                sUOTT4_To_LeftSHBE,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_LeftSHBE).size()));
        GeneralStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE,
                BigDecimal.valueOf(Groups.get(sUOTT3OrTT6_To_LeftSHBE).size()));
        GeneralStatistics.put(
                sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE,
                BigDecimal.valueOf(Groups.get(sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE).size()));
        GeneralStatistics.put(sUO_To_LeftSHBETheVeryNextMonth,
                BigDecimal.valueOf(Groups.get(sUO_To_LeftSHBETheVeryNextMonth).size()));
// Work would need to be done so that the sets contain the ClaimIDs...      
//        GeneralStatistics.put(
//                sUO_To_LeftSHBEBetweenOneAndTwoMonths,
//                BigDecimal.valueOf(Groups.get(sUO_To_LeftSHBEBetweenOneAndTwoMonths).size()));
//        GeneralStatistics.put(
//                sUO_To_LeftSHBEBetweenTwoAndThreeMonths,
//                BigDecimal.valueOf(Groups.get(sUO_To_LeftSHBEBetweenTwoAndThreeMonths).size()));
        GeneralStatistics.put(sUO_To_LeftSHBE_NotReturned,
                BigDecimal.valueOf(Groups.get(sUO_To_LeftSHBE_NotReturned).size()));
        GeneralStatistics.put(sUOTT1_To_LeftSHBE_NotReturned,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_LeftSHBE_NotReturned).size()));
        GeneralStatistics.put(sUOTT4_To_LeftSHBE_NotReturned,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_LeftSHBE_NotReturned).size()));
        GeneralStatistics.put(sUOTT3OrTT6_To_LeftSHBE_NotReturned,
                BigDecimal.valueOf(Groups.get(sUOTT3OrTT6_To_LeftSHBE_NotReturned).size()));
        GeneralStatistics.put(sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned,
                BigDecimal.valueOf(Groups.get(sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned).size()));
        GeneralStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAsUOTT1,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_LeftSHBE_ReturnedAsUOTT1).size()));
        GeneralStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT1,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_LeftSHBE_ReturnedAsTT1).size()));
        GeneralStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6).size()));
        GeneralStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAsUOTT4,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_LeftSHBE_ReturnedAsUOTT4).size()));
        GeneralStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT4,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_LeftSHBE_ReturnedAsTT4).size()));
        GeneralStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7).size()));
        GeneralStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT8,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_LeftSHBE_ReturnedAsTT8).size()));
        GeneralStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAsTT9,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_LeftSHBE_ReturnedAsTT9).size()));
        GeneralStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAsUOTT1,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_LeftSHBE_ReturnedAsUOTT1).size()));
        GeneralStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT1,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_LeftSHBE_ReturnedAsTT1).size()));
        GeneralStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6).size()));
        GeneralStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAsUOTT4,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_LeftSHBE_ReturnedAsUOTT4).size()));
        GeneralStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT4,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_LeftSHBE_ReturnedAsTT4).size()));
        GeneralStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7).size()));
        GeneralStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT8,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_LeftSHBE_ReturnedAsTT8).size()));
        GeneralStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAsTT9,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_LeftSHBE_ReturnedAsTT9).size()));
        GeneralStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1,
                BigDecimal.valueOf(Groups.get(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1).size()));
        GeneralStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1,
                BigDecimal.valueOf(Groups.get(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1).size()));
        GeneralStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6,
                BigDecimal.valueOf(Groups.get(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6).size()));
        GeneralStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4,
                BigDecimal.valueOf(Groups.get(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4).size()));
        GeneralStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4,
                BigDecimal.valueOf(Groups.get(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4).size()));
        GeneralStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7,
                BigDecimal.valueOf(Groups.get(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7).size()));
        GeneralStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8,
                BigDecimal.valueOf(Groups.get(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8).size()));
        GeneralStatistics.put(
                sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9,
                BigDecimal.valueOf(Groups.get(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9).size()));
        GeneralStatistics.put(
                sUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint).size()));
        GeneralStatistics.put(
                sUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint).size()));
//                sUOTT1_To_LeftSHBE,
//                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBE).size()));
//        generalStatistics.put(
//                sUOTT1_To_LeftSHBE,
//                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBE).size()));
//        generalStatistics.put(
//                sUOTT1_To_LeftSHBEAndHaveNotReturned,
//                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBEAndHaveNotReturned).size()));
//        generalStatistics.put(
//                sUOTT1_To_LeftSHBEReturnedAsTT1orTT4,
//                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBEReturnedAsTT1orTT4).size()));
//        generalStatistics.put(
//                sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
//                BigDecimal.valueOf(groups.get(sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6).size()));
//        generalStatistics.put(
//                sUOTT4_To_LeftSHBE,
//                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBE).size()));
//        generalStatistics.put(
//                sUOTT4_To_LeftSHBEAndHaveNotReturned,
//                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBEAndHaveNotReturned).size()));
//        generalStatistics.put(
//                sUOTT4_To_LeftSHBEReturnedAsTT1orTT4,
//                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBEReturnedAsTT1orTT4).size()));
//        generalStatistics.put(
//                sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
//                BigDecimal.valueOf(groups.get(sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6).size()));

        GeneralStatistics.put(
                sUO_NotUO_UO,
                BigDecimal.valueOf(
                        Groups.get(sUO_NotUO_UO).size()
                        + Groups.get(sUO_NotUO_UO_NotUO).size()));
        GeneralStatistics.put(
                sUO_NotUO_UO_NotUO_UO,
                BigDecimal.valueOf(
                        Groups.get(sUO_NotUO_UO_NotUO_UO).size()
                        + Groups.get(sUO_NotUO_UO_NotUO_UO_NotUO).size()));
        GeneralStatistics.put(
                sUO_NotUO_UO_NotUO_UO_NotUO_UO,
                BigDecimal.valueOf(Groups.get(sUO_NotUO_UO_NotUO_UO_NotUO_UO).size()
                        + Groups.get(sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO).size()));
        GeneralStatistics.put(
                sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO,
                BigDecimal.valueOf(Groups.get(sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO).size()));

        GeneralStatistics.put(
                sUOTT1_To_NotUO_InSHBE_PostcodeChanged,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_NotUO_InSHBE_PostcodeChanged).size()));
        GeneralStatistics.put(
                sUOTT1_To_TT1_PostcodeChanged,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT1_PostcodeChanged).size()));
        GeneralStatistics.put(
                sUOTT1_To_TT4_PostcodeChanged,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT4_PostcodeChanged).size()));
        GeneralStatistics.put(
                sUOTT4_To_NotUO_InSHBE_PostcodeChanged,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_NotUO_InSHBE_PostcodeChanged).size()));
        GeneralStatistics.put(
                sUOTT4_To_TT1_PostcodeChanged,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT1_PostcodeChanged).size()));
        GeneralStatistics.put(
                sUOTT4_To_TT4_PostcodeChanged,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT4_PostcodeChanged).size()));

        GeneralStatistics.put(
                sUOTT1_To_TT3OrTT6,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT3OrTT6).size()));
        GeneralStatistics.put(
                sUOTT1_To_TT3OrTT6AtSomePoint,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT3OrTT6AtSomePoint).size()));
        GeneralStatistics.put(
                sUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999).size()));
        GeneralStatistics.put(
                sUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint).size()));

        GeneralStatistics.put(
                sUOTT4_To_TT3OrTT6,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT3OrTT6).size()));
        GeneralStatistics.put(
                sUOTT4_To_TT3OrTT6AtSomePoint,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT3OrTT6AtSomePoint).size()));
        GeneralStatistics.put(
                sUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999).size()));
        GeneralStatistics.put(
                sUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint).size()));

        GeneralStatistics.put(
                sTT3OrTT6_To_UOTT1,
                BigDecimal.valueOf(Groups.get(sTT3OrTT6_To_UOTT1).size()));
        GeneralStatistics.put(
                sTT3OrTT6_To_UOTT4,
                BigDecimal.valueOf(Groups.get(sTT3OrTT6_To_UOTT4).size()));

        GeneralStatistics.put(sTT1_To_UOTT1_PostcodeUnchanged,
                BigDecimal.valueOf(Groups.get(sTT1_To_UOTT1_PostcodeUnchanged).size()));
//        generalStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
//                BigDecimal.valueOf(groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month).size()));
        GeneralStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                BigDecimal.valueOf(Groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1).size()));
        GeneralStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                BigDecimal.valueOf(Groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1).size()));
        GeneralStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                BigDecimal.valueOf(Groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6).size()));
        GeneralStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                BigDecimal.valueOf(Groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4).size()));
        GeneralStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                BigDecimal.valueOf(Groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4).size()));
        GeneralStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                BigDecimal.valueOf(Groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7).size()));
        GeneralStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                BigDecimal.valueOf(Groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8).size()));
        GeneralStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                BigDecimal.valueOf(Groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9).size()));
        GeneralStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
                BigDecimal.valueOf(Groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months).size()));
        GeneralStatistics.put(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months,
                BigDecimal.valueOf(Groups.get(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months).size()));

        //generalStatistics.put(sUOTT1OrTT1_To_UOTT4,
        //        BigDecimal.valueOf(groups.get(sUOTT1OrTT1_To_UOTT4).size()));
        GeneralStatistics.put(sUOTT1_To_UOTT4,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_UOTT4).size()));
        GeneralStatistics.put(sTT1_To_UOTT4,
                BigDecimal.valueOf(Groups.get(sTT1_To_UOTT4).size()));
        GeneralStatistics.put(sTT1_To_UOTT4GettingDHP,
                BigDecimal.valueOf(Groups.get(sTT1_To_UOTT4GettingDHP).size()));
//        generalStatistics.put(sTT1_To_UOTT4InArrears,
//                BigDecimal.valueOf(groups.get(sTT1_To_UOTT4InArrears).size()));
//        generalStatistics.put(sTT1_To_UOTT4InArrearsAndGettingDHP,
//                BigDecimal.valueOf(groups.get(sTT1_To_UOTT4InArrearsAndGettingDHP).size()));

        GeneralStatistics.put(sTT4_To_UOTT4_PostcodeUnchanged,
                BigDecimal.valueOf(Groups.get(sTT4_To_UOTT4_PostcodeUnchanged).size()));
//        generalStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
//                BigDecimal.valueOf(groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month).size()));
        GeneralStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                BigDecimal.valueOf(Groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1).size()));
        GeneralStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                BigDecimal.valueOf(Groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6).size()));
        GeneralStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                BigDecimal.valueOf(Groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4).size()));
        GeneralStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                BigDecimal.valueOf(Groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7).size()));
        GeneralStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                BigDecimal.valueOf(Groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8).size()));
        GeneralStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                BigDecimal.valueOf(Groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9).size()));
        GeneralStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
                BigDecimal.valueOf(Groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months).size()));
        GeneralStatistics.put(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months,
                BigDecimal.valueOf(Groups.get(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months).size()));

        //generalStatistics.put(sUOTT4OrTT4_To_UOTT1,
        //        BigDecimal.valueOf(groups.get(sUOTT4OrTT4_To_UOTT1).size()));
        //generalStatistics.put(sUOTT4OrTT4_To_UOTT1InArrears,
        //        BigDecimal.valueOf(groups.get(sUOTT4OrTT4_To_UOTT1InArrears).size()));
        //generalStatistics.put(sUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
        //        BigDecimal.valueOf(groups.get(sUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP).size()));
        GeneralStatistics.put(sUOTT4_To_UOTT1,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_UOTT1).size()));
        GeneralStatistics.put(sUOTT4_To_UOTT1InArrears,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_UOTT1InArrears).size()));
        GeneralStatistics.put(sUOTT4_To_UOTT1GettingDHP,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_UOTT1GettingDHP).size()));
        GeneralStatistics.put(sUOTT4_To_UOTT1InArrearsAndGettingDHP,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_UOTT1InArrearsAndGettingDHP).size()));
        GeneralStatistics.put(sTT4_To_UOTT1,
                BigDecimal.valueOf(Groups.get(sTT4_To_UOTT1).size()));
        GeneralStatistics.put(sTT4_To_UOTT1InArrears,
                BigDecimal.valueOf(Groups.get(sTT4_To_UOTT1InArrears).size()));
        GeneralStatistics.put(sTT4_To_UOTT1GettingDHP,
                BigDecimal.valueOf(Groups.get(sTT4_To_UOTT1GettingDHP).size()));
        GeneralStatistics.put(sTT4_To_UOTT1InArrearsAndGettingDHP,
                BigDecimal.valueOf(Groups.get(sTT4_To_UOTT1InArrearsAndGettingDHP).size()));

        GeneralStatistics.put(sUOTT1_To_TT1_PostcodeUnchanged,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT1_PostcodeUnchanged).size()));
//        generalStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
//                BigDecimal.valueOf(groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month).size()));
        GeneralStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1).size()));
        GeneralStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1).size()));
        GeneralStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6).size()));
        GeneralStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4).size()));
        GeneralStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4).size()));
        GeneralStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7).size()));
        GeneralStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8).size()));
        GeneralStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9).size()));
        GeneralStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months).size()));
        GeneralStatistics.put(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months).size()));

        GeneralStatistics.put(sUOTT4_To_TT4_PostcodeUnchanged,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT4_PostcodeUnchanged).size()));
//        generalStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
//                BigDecimal.valueOf(groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month).size()));
        GeneralStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1).size()));
        GeneralStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1).size()));
        GeneralStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6).size()));
        GeneralStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4).size()));
        GeneralStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4).size()));
        GeneralStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7).size()));
        GeneralStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8).size()));
        GeneralStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9).size()));
        GeneralStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months).size()));
        GeneralStatistics.put(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months).size()));

        GeneralStatistics.put(sUOTT1_To_TT1_PostcodeChanged,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_TT1_PostcodeChanged).size()));
        GeneralStatistics.put(sUOTT1_To_UOTT1_PostcodeChanged,
                BigDecimal.valueOf(Groups.get(sUOTT1_To_UOTT1_PostcodeChanged).size()));
        GeneralStatistics.put(sUOTT4_To_TT4_PostcodeChanged,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_TT4_PostcodeChanged).size()));
        GeneralStatistics.put(sUOTT4_To_UOTT4_PostcodeChanged,
                BigDecimal.valueOf(Groups.get(sUOTT4_To_UOTT4_PostcodeChanged).size()));
        GeneralStatistics.put(sTT1_To_UOTT1_PostcodeChanged,
                BigDecimal.valueOf(Groups.get(sTT1_To_UOTT1_PostcodeChanged).size()));
        GeneralStatistics.put(sTT4_To_UOTT4_PostcodeChanged,
                BigDecimal.valueOf(Groups.get(sTT4_To_UOTT4_PostcodeChanged).size()));
        // Council
        CouncilUniqueIndividualsEffectedPersonIDs.addAll(CouncilUniqueClaimantsEffectedPersonIDs);
        CouncilUniqueIndividualsEffectedPersonIDs.addAll(CouncilUniquePartnersEffectedPersonIDs);
        CouncilUniqueIndividualsEffectedPersonIDs.addAll(CouncilUniqueDependentsAgedUnder10EffectedPersonIDs);
        CouncilUniqueIndividualsEffectedPersonIDs.addAll(CouncilUniqueDependentsAgedOver10EffectedPersonIDs);
        CouncilUniqueIndividualsEffectedPersonIDs.addAll(CouncilUniqueNonDependentsEffectedPersonIDs);
        GeneralStatistics.put(
                sTotalCount_CouncilUniqueIndividualsEffectedByUnderOccupancy,
                BigDecimal.valueOf(CouncilUniqueIndividualsEffectedPersonIDs.size()));
        GeneralStatistics.put(
                sTotalCount_CouncilUniqueClaimantsEffectedByUnderOccupancy,
                BigDecimal.valueOf(CouncilUniqueClaimantsEffectedPersonIDs.size()));
        GeneralStatistics.put(
                sTotalCount_CouncilUniquePartnersEffectedByUnderOccupancy,
                BigDecimal.valueOf(CouncilUniquePartnersEffectedPersonIDs.size()));
        GeneralStatistics.put(sTotalCount_CouncilDependentsAgedUnder10EffectedByUnderOccupancy,
                BigDecimal.valueOf(CouncilUniqueDependentsAgedUnder10EffectedPersonIDs.size()));
        GeneralStatistics.put(sTotalCount_CouncilDependentsAgedOver10EffectedByUnderOccupancy,
                BigDecimal.valueOf(CouncilUniqueDependentsAgedOver10EffectedPersonIDs.size()));
        GeneralStatistics.put(
                sTotalCount_CouncilUniqueNonDependentsEffectedByUnderOccupancy,
                BigDecimal.valueOf(CouncilUniqueNonDependentsEffectedPersonIDs.size()));
        // RSL
        RSLUniqueIndividualsEffectedPersonIDs.addAll(RSLUniqueClaimantsEffectedPersonIDs);
        RSLUniqueIndividualsEffectedPersonIDs.addAll(RSLUniquePartnersEffectedPersonIDs);
        RSLUniqueIndividualsEffectedPersonIDs.addAll(RSLUniqueDependentsAgedUnder10EffectedPersonIDs);
        RSLUniqueIndividualsEffectedPersonIDs.addAll(RSLUniqueDependentsAgedOver10EffectedPersonIDs);
        RSLUniqueIndividualsEffectedPersonIDs.addAll(RSLUniqueNonDependentsEffectedPersonIDs);
        GeneralStatistics.put(
                sTotalCount_RSLUniqueIndividualsEffectedByUnderOccupancy,
                BigDecimal.valueOf(RSLUniqueIndividualsEffectedPersonIDs.size()));
        GeneralStatistics.put(
                sTotalCount_RSLUniqueClaimantsEffectedByUnderOccupancy,
                BigDecimal.valueOf(RSLUniqueClaimantsEffectedPersonIDs.size()));
        GeneralStatistics.put(
                sTotalCount_RSLUniquePartnersEffectedByUnderOccupancy,
                BigDecimal.valueOf(RSLUniquePartnersEffectedPersonIDs.size()));
        GeneralStatistics.put(
                sTotalCount_RSLDependentsUnder10EffectedByUnderOccupancy,
                BigDecimal.valueOf(RSLUniqueDependentsAgedUnder10EffectedPersonIDs.size()));
        GeneralStatistics.put(
                sTotalCount_RSLDependentsOver10EffectedByUnderOccupancy,
                BigDecimal.valueOf(RSLUniqueDependentsAgedOver10EffectedPersonIDs.size()));
        GeneralStatistics.put(
                sTotalCount_RSLUniqueNonDependentsEffectedByUnderOccupancy,
                BigDecimal.valueOf(RSLUniqueNonDependentsEffectedPersonIDs.size()));
        // All
        GeneralStatistics.put(
                sTotalCount_ClaimsEffectedByUnderOccupancy,
                BigDecimal.valueOf(ClaimRefs.size()));
        GeneralStatistics.put(
                sTotalCount_CouncilClaimsEffectedByUnderOccupancy,
                BigDecimal.valueOf(CouncilClaimRefs.size()));
        GeneralStatistics.put(
                sTotalCount_RSLClaimsEffectedByUnderOccupancy,
                BigDecimal.valueOf(RSLClaimRefs.size()));
        UniqueIndividualsEffectedPersonIDs.addAll(CouncilUniqueIndividualsEffectedPersonIDs);
        UniqueIndividualsEffectedPersonIDs.addAll(RSLUniqueIndividualsEffectedPersonIDs);
        GeneralStatistics.put(
                sTotalCount_UniqueIndividualsEffectedByUnderOccupancy,
                BigDecimal.valueOf(UniqueIndividualsEffectedPersonIDs.size()));
        UniqueDependentsAgedUnder10EffectedPersonIDs.addAll(RSLUniqueDependentsAgedUnder10EffectedPersonIDs);
        UniqueDependentsAgedUnder10EffectedPersonIDs.addAll(CouncilUniqueDependentsAgedUnder10EffectedPersonIDs);
        GeneralStatistics.put(
                sTotalCount_UniqueDependentsAgedUnder10EffectedByUnderOccupancy,
                BigDecimal.valueOf(UniqueDependentsAgedUnder10EffectedPersonIDs.size()));
        UniqueDependentsAgedOver10EffectedPersonIDs.addAll(RSLUniqueDependentsAgedOver10EffectedPersonIDs);
        UniqueDependentsAgedOver10EffectedPersonIDs.addAll(CouncilUniqueDependentsAgedOver10EffectedPersonIDs);
        GeneralStatistics.put(
                sTotalCount_UniqueDependentsAgedOver10EffectedByUnderOccupancy,
                BigDecimal.valueOf(UniqueDependentsAgedOver10EffectedPersonIDs.size()));
        UniqueDependentsEffectedPersonIDs.addAll(UniqueDependentsAgedOver10EffectedPersonIDs);
        UniqueDependentsEffectedPersonIDs.addAll(UniqueDependentsAgedUnder10EffectedPersonIDs);
        GeneralStatistics.put(
                sTotalCount_UniqueDependentsEffectedByUnderOccupancy,
                BigDecimal.valueOf(UniqueDependentsEffectedPersonIDs.size()));

        GeneralStatistics.put(
                sUOClaimsRecievingDHP,
                BigDecimal.valueOf(Groups.get(sUOClaimsRecievingDHP).size()));
        GeneralStatistics.put(
                sUOTT1ClaimsInRentArrearsAtSomePoint,
                BigDecimal.valueOf(Groups.get(sUOTT1ClaimsInRentArrearsAtSomePoint).size()));
        GeneralStatistics.put(
                sUOTT1ClaimsInRentArrearsOver500AtSomePoint,
                BigDecimal.valueOf(Groups.get(sUOTT1ClaimsInRentArrearsOver500AtSomePoint).size()));

        GeneralStatistics.put(
                sUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint,
                BigDecimal.valueOf(Groups.get(sUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint).size()));
        GeneralStatistics.put(
                sInArrearsAtSomePoint_And_DHPAtSomePoint,
                BigDecimal.valueOf(Groups.get(sInArrearsAtSomePoint_And_DHPAtSomePoint).size()));

        GeneralStatistics.put(
                sPermanantlyLeftUOButRemainedInSHBE,
                BigDecimal.valueOf(Groups.get(sPermanantlyLeftUOButRemainedInSHBE).size()));
        GeneralStatistics.put(
                sPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged,
                BigDecimal.valueOf(Groups.get(sPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged).size()));
        GeneralStatistics.put(
                sPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased,
                BigDecimal.valueOf(Groups.get(sPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased).size()));
        env.ge.log("</getTable>", true);
    }

    /**
     *
     * @param claimIDToClaimantPersonIDLookup
     * @param claimIDToPartnerPersonIDLookup
     * @param tUOClaims
     * @param AggregateStatistics
     * @param generalStatistics
     * @param ClaimID
     * @param ClaimRef
     * @param year0
     * @param month0
     * @param YM30
     * @param year1
     * @param month1
     * @param YM31
     * @param Record1
     * @param recs0
     * @param TableValues
     * @param CouncilUOSet0
     * @param RSLUOSet0
     * @param CouncilUOSet1
     * @param RSLUOSet1
     * @param arrears
     * @param arrearsCounts
     * @param CouncilUniqueIndividualsEffected
     * @param arrearsDiffs
     * @param CouncilUniqueClaimantsEffectedPersonIDs
     * @param arrearsDiffCounts
     * @param CouncilUniquePartnersEffectedPersonIDs
     * @param CouncilUniqueDependentChildrenUnder10EffectedPersonIDs
     * @param CouncilUniqueDependentChildrenOver10EffectedPersonIDs
     * @param CouncilUniqueNonDependentsEffectedPersonIDs
     * @param CouncilMaxNumberOfDependentsInClaimWhenUO
     * @param RSLUniqueIndividualsEffected
     * @param RSLUniqueClaimantsEffectedPersonIDs
     * @param RSLUniquePartnersEffectedPersonIDs
     * @param RSLUniqueDependentChildrenUnder10EffectedPersonIDs
     * @param RSLUniqueDependentChildrenOver10EffectedPersonIDs
     * @param RSLUniqueNonDependentsEffectedPersonIDs
     * @param RSLMaxNumberOfDependentsInClaimWhenUO
     * @param PermanantlyLeftUOButRemainedInSHBEClaimIDs
     * @param PermanantlyLeftUOButRemainedInSHBEPostcodeChangedClaimIDs
     * @param PermanantlyLeftUOButRemainedInSHBEHouseholdSizeIncreasedClaimIDs
     * @param TravellerClaimIDs
     * @param TTNot1Or4AndUnderOccupyingClaimIDs
     * @param TT1_To_TT3OrTT6ClaimIDs
     * @param TT4_To_TT3OrTT6ClaimIDs
     * @param TT3OrTT6_To_TT1ClaimIDs
     * @param TT3OrTT6_To_TT4ClaimIDs
     * @param ValidPostcodeChangeClaimIDs
     * @param ChangedTTClaimIDs
     * @param UOAtSomePointClaimIDs
     * @param UOTT1AtSomePointClaimIDs
     * @param UOTT4AtSomePointClaimIDs
     * @param AlwaysUOTT1FromStartClaimIDs
     * @param AlwaysUOTT1FromStartExceptWhenSuspendedClaimIDs
     * @param AlwaysUOTT1FromWhenStartedClaimIDs
     * @param AlwaysUOTT4FromStartClaimIDs
     * @param AlwaysUOTT4FromStartExceptWhenSuspendedClaimIDs
     * @param AlwaysUOTT4FromWhenStartedClaimIDs
     * @param IntermitantUOClaimIDs
     * @param UO_To_LeftSHBEAtSomePointClaimIDs
     * @param UOTT1_To_LeftSHBEClaimIDs
     * @param UOTT4_To_LeftSHBEClaimIDs
     * @param UOTT3OrTT6_To_LeftSHBEClaimIDs
     * @param UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEClaimIDs
     * @param UO_To_LeftSHBETheVeryNextMonthClaimIDs
     * @param UO_To_LeftSHBE_NotReturnedClaimIDs
     * @param UOTT1_To_LeftSHBE_NotReturnedClaimIDs
     * @param UOTT4_To_LeftSHBE_NotReturnedClaimIDs
     * @param UOTT3OrTT6_To_LeftSHBE_NotReturnedClaimIDs
     * @param UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturnedClaimIDs
     * @param UOTT1_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs
     * @param UOTT1_To_LeftSHBE_ReturnedAsTT1ClaimIDs
     * @param UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs
     * @param UOTT1_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs
     * @param UOTT1_To_LeftSHBE_ReturnedAsTT4ClaimIDs
     * @param UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs
     * @param UOTT1_To_LeftSHBE_ReturnedAsTT8ClaimIDs
     * @param UOTT1_To_LeftSHBE_ReturnedAsTT9ClaimIDs
     * @param UOTT4_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs
     * @param UOTT4_To_LeftSHBE_ReturnedAsTT1ClaimIDs
     * @param UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs
     * @param UOTT4_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs
     * @param UOTT4_To_LeftSHBE_ReturnedAsTT4ClaimIDs
     * @param UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs
     * @param UOTT4_To_LeftSHBE_ReturnedAsTT8ClaimIDs
     * @param UOTT4_To_LeftSHBE_ReturnedAsTT9ClaimIDs
     * @param UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs
     * @param UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1ClaimIDs
     * @param UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs
     * @param UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs
     * @param UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4ClaimIDs
     * @param UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs
     * @param UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8ClaimIDs
     * @param UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9ClaimIDs
     * @param UOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePointClaimIDs
     * @param UOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePointClaimIDs
     * @param UO_NotUOClaimIDs
     * @param UO_NotUO_UOClaimIDs
     * @param UO_NotUO_UO_NotUOClaimIDs
     * @param UO_NotUO_UO_NotUO_UOClaimIDs
     * @param UO_NotUO_UO_NotUO_UO_NotUOClaimIDs
     * @param UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs
     * @param UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUOClaimIDs
     * @param UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs
     * @param UOTT1_To_NotUO_InSHBE_PostcodeChangedClaimIDs
     * @param UOTT1_To_UOTT1_PostcodeChangedClaimIDs
     * @param UOTT1_To_TT1_PostcodeChangedClaimIDs
     * @param UOTT1_To_UOTT4_PostcodeChangedClaimIDs
     * @param UOTT1_To_TT4_PostcodeChangedClaimIDs
     * @param UOTT4_To_NotUO_InSHBE_PostcodeChangedClaimIDs
     * @param UOTT4_To_UOTT1_PostcodeChangedClaimIDs
     * @param UOTT4_To_TT1_PostcodeChangedClaimIDs
     * @param UOTT4_To_UOTT4_PostcodeChangedClamIDs
     * @param UOTT4_To_TT4_PostcodeChangedClaimIDs
     * @param UOTT1_To_TT3OrTT6ClaimIDs
     * @param UOTT1_To_TT3OrTT6AtSomePointClaimIDs
     * @param UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs
     * @param UOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePointClaimIDs
     * @param UOTT1_To_TT3OrTT6NotDoneNextChangeClaimIDs
     * @param UOTT4_To_TT3OrTT6ClaimIDs
     * @param UOTT4_To_TT3OrTT6AtSomePointClaimIDs
     * @param UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs
     * @param UOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePointClaimIDs
     * @param UOTT4_To_TT3OrTT6NotDoneNextChangeClaimIDs
     * @param TT3OrTT6_To_UOTT1ClaimIDs
     * @param TT3OrTT6_To_UOTT4ClaimIDs
     * @param TT1_To_UOTT1_PostcodeUnchangedClaimIDs
     * @param TT1_To_UOTT1_PostcodeUnchangedThisMonthClaimIDs
     * @param TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs
     * @param TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs
     * @param
     * TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs
     * @param TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs
     * @param TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs
     * @param
     * TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs
     * @param TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs
     * @param TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs
     * @param TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2MonthsClaimIDs
     * @param TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3MonthsClaimIDs
     * @param TT1_To_UOTT1_PostcodeUnchanged1MonthPreviousClaimIDs
     * @param TT1_To_UOTT1_PostcodeUnchanged2MonthsPreviousClaimIDs
     * @param TT1_To_UOTT1_PostcodeUnchanged3MonthsPreviousClaimIDs
     * @param UOTT1_To_UOTT4ClaimIDs
     * @param TT1_To_UOTT4ClaimIDs
     * @param TT1_To_UOTT4GettingDHPClaimIDs
     * @param TT4_To_UOTT4_PostcodeUnchangedClaimIDs
     * @param TT4_To_UOTT4_PostcodeUnchangedThisMonthClaimIDs
     * @param TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs
     * @param TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs
     * @param
     * TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs
     * @param TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs
     * @param TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs
     * @param
     * TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs
     * @param TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs
     * @param TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs
     * @param TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2MonthsClaimIDs
     * @param TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3MonthsClaimIDs
     * @param TT4_To_UOTT4_PostcodeUnchanged1MonthPreviousClaimIDs
     * @param TT4_To_UOTT4_PostcodeUnchanged2MonthsPreviousClaimIDs
     * @param TT4_To_UOTT4_PostcodeUnchanged3MonthsPreviousClaimIDs
     * @param UOTT4_To_UOTT1ClaimIDs
     * @param UOTT4_To_UOTT1InArrearsClaimIDs
     * @param UOTT4_To_UOTT1GettingDHPClaimIDs
     * @param UOTT4_To_UOTT1InArrearsAndGettingDHPClaimIDs
     * @param TT4_To_UOTT1ClaimIDs
     * @param TT4_To_UOTT1InArrearsClaimIDs
     * @param TT4_To_UOTT1GettingDHPClaimIDs
     * @param TT4_To_UOTT1InArrearsAndGettingDHPClaimIDs
     * @param InArrearsAtSomePointClaimIDs
     * @param DHPAtSomePointClaimIDs
     * @param UOTT1_To_TT1_PostcodeUnchangedClaimIDs
     * @param UOTT1_To_TT1_PostcodeUnchangedThisMonthClaimIDs
     * @param UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs
     * @param UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs
     * @param
     * UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs
     * @param UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs
     * @param UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs
     * @param
     * UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs
     * @param UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs
     * @param UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs
     * @param UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2MonthsClaimIDs
     * @param UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3MonthsClaimIDs
     * @param UOTT1_To_TT1_PostcodeUnchanged1MonthPreviousClaimIDs
     * @param UOTT1_To_TT1_PostcodeUnchanged2MonthsPreviousClaimIDs
     * @param UOTT1_To_TT1_PostcodeUnchanged3MonthsPreviousClaimIDs
     * @param UOTT4_To_TT4_PostcodeUnchangedClaimIDs
     * @param UOTT4_To_TT4_PostcodeUnchangedThisMonthClaimIDs
     * @param UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs
     * @param UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs
     * @param
     * UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs
     * @param UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs
     * @param UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs
     * @param
     * UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs
     * @param UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs
     * @param UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs
     * @param UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2MonthsClaimIDs
     * @param UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3MonthsClaimIDs
     * @param UOTT4_To_TT4_PostcodeUnchanged1MonthPreviousClaimIDs
     * @param UOTT4_To_TT4_PostcodeUnchanged2MonthsPreviousClaimIDs
     * @param UOTT4_To_TT4_PostcodeUnchanged3MonthsPreviousClaimIDs
     * @param UOTT1_ToTT1_PostcodeChangedClaimIDs
     * @param UOTT1_ToUOTT1_PostcodeChangedClaimIDs
     * @param UOTT4_ToTT4_PostcodeChangedClaimIDs
     * @param UOTT4_ToUOTT4_PostcodeChangedClaimIDs
     * @param TT1_ToUOTT1_PostcodeChangedClaimIDs
     * @param TT4_ToUOTT4_PostcodeChangedClaimIDs
     * @param UOClaimsRecievingDHPClaimIDs
     * @param UOTT1ClaimsInRentArrearsAtSomePointClaimIDs
     * @param UOTT1ClaimsInRentArrearsOver500AtSomePointClaimIDs
     * @param UOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePointClaimIDs
     * @return {@code Object[] result:
     * result[1] = boolean true; iff ClaimID is a Council record
     * result[2] = boolean true; iff ClaimID is a RSL record
     * result[3] = boolean true; iff householdSize from SHBE data is greater than or equal to the number of bedrooms in UO data
     * result[4] = boolean true; iff householdSize from UO data is greater than or equal to the number of bedrooms in UO data
     * result[5] = int household size excluding partners from SHBE data
     * result[6] = int household size excluding partners from UO data
     * result[7] = int household size from SHBE data
     * result[8] = int household size from UO data + PartnerFlag from SHBE data
     */
    public Object[] process(
            Map<SHBE_ClaimID, SHBE_PersonID> claimIDToClaimantPersonIDLookup,
            Map<SHBE_ClaimID, SHBE_PersonID> claimIDToPartnerPersonIDLookup,
            HashSet<SHBE_ClaimID> tUOClaims,
            TreeMap<String, BigDecimal> AggregateStatistics,
            TreeMap<String, BigDecimal> generalStatistics,
            SHBE_ClaimID ClaimID, String ClaimRef, String year0, String month0,
            UKP_YM3 YM30, String year1, String month1, UKP_YM3 YM31,
            SHBE_Record Record1, Map<SHBE_ClaimID, SHBE_Record> recs0,
            //HashMap<SHBE_ClaimID, SHBE_Record> cRecords,
            TreeMap<String, String> TableValues, DW_UO_Set CouncilUOSet0,
            DW_UO_Set RSLUOSet0, DW_UO_Set CouncilUOSet1,
            DW_UO_Set RSLUOSet1, HashMap<UKP_YM3, Double> arrears,
            HashMap<UKP_YM3, Double> arrearsCounts,
            HashMap<UKP_YM3, Double> arrearsDiffs,
            HashMap<UKP_YM3, Double> arrearsDiffCounts,
            HashSet<SHBE_PersonID> CouncilUniqueIndividualsEffected,
            HashSet<SHBE_PersonID> CouncilUniqueClaimantsEffectedPersonIDs,
            HashSet<SHBE_PersonID> CouncilUniquePartnersEffectedPersonIDs,
            HashSet<SHBE_PersonID> CouncilUniqueDependentChildrenUnder10EffectedPersonIDs,
            HashSet<SHBE_PersonID> CouncilUniqueDependentChildrenOver10EffectedPersonIDs,
            HashSet<SHBE_PersonID> CouncilUniqueNonDependentsEffectedPersonIDs,
            HashMap<SHBE_ClaimID, Integer> CouncilMaxNumberOfDependentsInClaimWhenUO,
            HashSet<SHBE_PersonID> RSLUniqueIndividualsEffected,
            HashSet<SHBE_PersonID> RSLUniqueClaimantsEffectedPersonIDs,
            HashSet<SHBE_PersonID> RSLUniquePartnersEffectedPersonIDs,
            HashSet<SHBE_PersonID> RSLUniqueDependentChildrenUnder10EffectedPersonIDs,
            HashSet<SHBE_PersonID> RSLUniqueDependentChildrenOver10EffectedPersonIDs,
            HashSet<SHBE_PersonID> RSLUniqueNonDependentsEffectedPersonIDs,
            HashMap<SHBE_ClaimID, Integer> RSLMaxNumberOfDependentsInClaimWhenUO,
            HashSet<SHBE_ClaimID> PermanantlyLeftUOButRemainedInSHBEClaimIDs,
            HashSet<SHBE_ClaimID> PermanantlyLeftUOButRemainedInSHBEPostcodeChangedClaimIDs,
            HashSet<SHBE_ClaimID> PermanantlyLeftUOButRemainedInSHBEHouseholdSizeIncreasedClaimIDs,
            HashSet<SHBE_ClaimID> TravellerClaimIDs,
            HashSet<SHBE_ClaimID> TTNot1Or4AndUnderOccupyingClaimIDs,
            HashSet<SHBE_ClaimID> TT1_To_TT3OrTT6ClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_TT3OrTT6ClaimIDs,
            HashSet<SHBE_ClaimID> TT3OrTT6_To_TT1ClaimIDs,
            HashSet<SHBE_ClaimID> TT3OrTT6_To_TT4ClaimIDs,
            HashSet<SHBE_ClaimID> ValidPostcodeChangeClaimIDs,
            HashSet<SHBE_ClaimID> ChangedTTClaimIDs,
            HashSet<SHBE_ClaimID> UOAtSomePointClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1AtSomePointClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4AtSomePointClaimIDs,
            HashSet<SHBE_ClaimID> AlwaysUOTT1FromStartClaimIDs,
            HashSet<SHBE_ClaimID> AlwaysUOTT1FromStartExceptWhenSuspendedClaimIDs,
            HashSet<SHBE_ClaimID> AlwaysUOTT1FromWhenStartedClaimIDs,
            HashSet<SHBE_ClaimID> AlwaysUOTT4FromStartClaimIDs,
            HashSet<SHBE_ClaimID> AlwaysUOTT4FromStartExceptWhenSuspendedClaimIDs,
            HashSet<SHBE_ClaimID> AlwaysUOTT4FromWhenStartedClaimIDs,
            HashSet<SHBE_ClaimID> IntermitantUOClaimIDs,
            HashSet<SHBE_ClaimID> UO_To_LeftSHBEAtSomePointClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBEClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBEClaimIDs,
            HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBEClaimIDs,
            HashSet<SHBE_ClaimID> UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEClaimIDs,
            HashSet<SHBE_ClaimID> UO_To_LeftSHBETheVeryNextMonthClaimIDs,
            //            HashSet<SHBE_ClaimID> UO_To_LeftSHBEBetweenOneAndTwoMonthsClaimIDs,
            //            HashSet<SHBE_ClaimID> UO_To_LeftSHBEBetweenTwoAndThreeMonthsClaimIDs,
            HashSet<SHBE_ClaimID> UO_To_LeftSHBE_NotReturnedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_NotReturnedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_NotReturnedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBE_NotReturnedClaimIDs,
            HashSet<SHBE_ClaimID> UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturnedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT1ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT4ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT8ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT9ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT1ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT4ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT8ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT9ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePointClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePointClaimIDs,
            //HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBEReturnedAsTT1orTT4,
            //HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
            //HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBEReturnedAsTT1orTT4,
            //HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
            HashSet<SHBE_ClaimID> UO_NotUOClaimIDs,
            HashSet<SHBE_ClaimID> UO_NotUO_UOClaimIDs,
            HashSet<SHBE_ClaimID> UO_NotUO_UO_NotUOClaimIDs,
            HashSet<SHBE_ClaimID> UO_NotUO_UO_NotUO_UOClaimIDs,
            HashSet<SHBE_ClaimID> UO_NotUO_UO_NotUO_UO_NotUOClaimIDs,
            HashSet<SHBE_ClaimID> UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs,
            HashSet<SHBE_ClaimID> UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUOClaimIDs,
            HashSet<SHBE_ClaimID> UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_NotUO_InSHBE_PostcodeChangedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_UOTT1_PostcodeChangedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeChangedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_UOTT4_PostcodeChangedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT4_PostcodeChangedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_NotUO_InSHBE_PostcodeChangedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_UOTT1_PostcodeChangedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT1_PostcodeChangedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_UOTT4_PostcodeChangedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeChangedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT3OrTT6ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT3OrTT6AtSomePointClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePointClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT3OrTT6NotDoneNextChangeClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT3OrTT6ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT3OrTT6AtSomePointClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePointClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT3OrTT6NotDoneNextChangeClaimIDs,
            HashSet<SHBE_ClaimID> TT3OrTT6_To_UOTT1ClaimIDs,
            HashSet<SHBE_ClaimID> TT3OrTT6_To_UOTT4ClaimIDs,
            HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedClaimIDs,
            HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedThisMonthClaimIDs,
            //HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
            HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs,
            HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs,
            HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs,
            HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs,
            HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs,
            HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs,
            HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs,
            HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs,
            HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2MonthsClaimIDs,
            HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3MonthsClaimIDs,
            HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchanged1MonthPreviousClaimIDs,
            HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchanged2MonthsPreviousClaimIDs,
            HashSet<SHBE_ClaimID> TT1_To_UOTT1_PostcodeUnchanged3MonthsPreviousClaimIDs,
            //HashSet<SHBE_ClaimID> UOTT1OrTT1_To_UOTT4,
            HashSet<SHBE_ClaimID> UOTT1_To_UOTT4ClaimIDs,
            HashSet<SHBE_ClaimID> TT1_To_UOTT4ClaimIDs,
            HashSet<SHBE_ClaimID> TT1_To_UOTT4GettingDHPClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedThisMonthClaimIDs,
            //HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
            HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2MonthsClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3MonthsClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchanged1MonthPreviousClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchanged2MonthsPreviousClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT4_PostcodeUnchanged3MonthsPreviousClaimIDs,
            //HashSet<SHBE_ClaimID> UOTT4OrTT4_To_UOTT1,
            //HashSet<SHBE_ClaimID> UOTT4OrTT4_To_UOTT1InArrears,
            //HashSet<SHBE_ClaimID> UOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
            HashSet<SHBE_ClaimID> UOTT4_To_UOTT1ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_UOTT1InArrearsClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_UOTT1GettingDHPClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_UOTT1InArrearsAndGettingDHPClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT1ClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT1InArrearsClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT1GettingDHPClaimIDs,
            HashSet<SHBE_ClaimID> TT4_To_UOTT1InArrearsAndGettingDHPClaimIDs,
            HashSet<SHBE_ClaimID> InArrearsAtSomePointClaimIDs,
            HashSet<SHBE_ClaimID> DHPAtSomePointClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedThisMonthClaimIDs,
            //HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
            HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2MonthsClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3MonthsClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchanged1MonthPreviousClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchanged2MonthsPreviousClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_TT1_PostcodeUnchanged3MonthsPreviousClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedThisMonthClaimIDs,
            //HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
            HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2MonthsClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3MonthsClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchanged1MonthPreviousClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchanged2MonthsPreviousClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_TT4_PostcodeUnchanged3MonthsPreviousClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_ToTT1_PostcodeChangedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_ToUOTT1_PostcodeChangedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_ToTT4_PostcodeChangedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_ToUOTT4_PostcodeChangedClaimIDs,
            HashSet<SHBE_ClaimID> TT1_ToUOTT1_PostcodeChangedClaimIDs,
            HashSet<SHBE_ClaimID> TT4_ToUOTT4_PostcodeChangedClaimIDs,
            HashSet<SHBE_ClaimID> UOClaimsRecievingDHPClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1ClaimsInRentArrearsAtSomePointClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1ClaimsInRentArrearsOver500AtSomePointClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePointClaimIDs
    ) throws IOException, ClassNotFoundException {
        Object[] result = new Object[9];
        result[0] = false; // UnderOcuupied
        result[1] = false; // UnderOcuupied
        result[2] = false; // UnderOcuupied
        result[3] = false; // 
        result[4] = false; // 
        result[5] = 0; // 
        result[6] = 0; // 
        result[7] = 0; // 
        result[8] = 0; //
        int cumulativeClaims;
        String aS;
        String key;
        // Declare
        SHBE_D_Record DRecord1 = null;
        int TT1;
        String PC1;
        int Status1;
        int WHBE1;
        int WERA1;
        int PSI1;
        int SHBC1;
        int RTHBCC1;
        int CEG1;
        long HS1;
        long ND1;
        long CD1;
        String PDD1;
        String CDoB1;
        String PDoB1;
        String CA1;
        String PA1;
        String CG1;
        String PG1;
        String D1;
        String DP1;
        String DS1;
        String DC1;
        String DE1;
        int HBDP1;
        Double Arrears1;
        SHBE_D_Record DRecord0;
        int TT0;
        String PC0;
        int Status0;
        int WHBE0;
        int WERA0;
        int PSI0;
        int SHBC0;
        int RTHBCC0;
        int CEG0;
        long HS0;
        long ND0;
        long CD0;
        String PDD0;
        String CDoB0;
        String PDoB0;
        String CA0;
        String PA0;
        Double Arrears0;
        SHBE_Record Record0;
        if (recs0 == null) {
            Record0 = null;
        } else {
            Record0 = recs0.get(ClaimID);
        }
//        SHBE_D_Record cD_Record;
//        int cTT;
//        String cPC;
//        int cStatus;
//        SHBE_Record cRecord;
//        if (cRecords == null) {
//            cRecord = null;
//        } else {
//            cRecord = cRecords.get(ClaimID);
//        }
        // Init
        if (Record1 == null) {
            TT1 = ttHandler.iMinus999;
            PC1 = defaultPostcode;
            Status1 = 0;
            WHBE1 = 0;
            WERA1 = 0;
            PSI1 = 0;
            SHBC1 = 0;
            RTHBCC1 = 0;
            CEG1 = 0;
            HS1 = 0;
            ND1 = 0;
            CD1 = 0;
            PDD1 = s;
            CDoB1 = s;
            PDoB1 = s;
            CA1 = s;
            PA1 = s;
            CG1 = s;
            PG1 = s;
            D1 = s;
            DP1 = s;
            DS1 = s;
            DC1 = s;
            DE1 = s;
            HBDP1 = 0;
        } else {
            DRecord1 = Record1.getDRecord();
            TT1 = DRecord1.getTenancyType();
            PC1 = DRecord1.getClaimantsPostcode();
            if (Record1.isClaimPostcodeFMappable()) {
                ValidPostcodes.add(PC1);
            }
            Status1 = DRecord1.getStatusOfHBClaimAtExtractDate();
            WHBE1 = DRecord1.getWeeklyHousingBenefitEntitlement();
            WERA1 = DRecord1.getWeeklyEligibleRentAmount();
            PSI1 = DRecord1.getPassportedStandardIndicator();
            SHBC1 = DRecord1.getStatusOfHBClaimAtExtractDate();
            RTHBCC1 = DRecord1.getReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective();
            CEG1 = DRecord1.getClaimantsEthnicGroup();
            HS1 = shbeHandler.getHouseholdSize(DRecord1);
            ND1 = DRecord1.getNumberOfNonDependents();
            CD1 = DRecord1.getNumberOfChildDependents();
            CDoB1 = DRecord1.getClaimantsDateOfBirth();
            PDoB1 = DRecord1.getPartnersDateOfBirth();
            if (shbeHandler.getDisability(DRecord1)) {
                D1 = sDisability;
            } else {
                D1 = s;
            }
            // Disability Premium
            if (DRecord1.getDisabilityPremiumAwarded() == 1) {
                DP1 = sDP;
            } else {
                DP1 = s;
            }
            // Disability Severe
            if (DRecord1.getSevereDisabilityPremiumAwarded() == 1) {
                DS1 = sDS;
            } else {
                DS1 = s;
            }
            // Disability Enhanced
            if (DRecord1.getEnhancedDisabilityPremiumAwarded() == 1) {
                DE1 = sDE;
            } else {
                DE1 = s;
            }
            // Disabiled Child
            if (DRecord1.getDisabledChildPremiumAwarded() == 1) {
                DC1 = sDC;
            } else {
                DC1 = s;
            }
            CA1 = shbeHandler.getClaimantsAge(year1, month1, DRecord1);
            PA1 = shbeHandler.getPartnersAge(year1, month1, DRecord1);
            CG1 = DRecord1.getClaimantsGender();
            PG1 = DRecord1.getPartnersGender();
            PDD1 = DRecord1.getPartnersDateOfDeath();
            HBDP1 = DRecord1.getWeeklyAdditionalDiscretionaryPayment();
        }
        if (Record0 == null) {
            TT0 = ttHandler.iMinus999;
            PC0 = defaultPostcode;
            Status0 = 0;
            WHBE0 = 0;
            WERA0 = 0;
            PSI0 = 0;
            SHBC0 = 0;
            RTHBCC0 = 0;
            CEG0 = 0;
            HS0 = 0;
            ND0 = 0;
            CD0 = 0;
            PDD0 = s;
            CDoB0 = s;
            PDoB0 = s;
            CA0 = s;
            PA0 = s;
        } else {
            DRecord0 = Record0.getDRecord();
            TT0 = DRecord0.getTenancyType();
            PC0 = DRecord0.getClaimantsPostcode();
            if (PC0.isEmpty()) {
                PC0 = defaultPostcode;
            }
            if (Record0.isClaimPostcodeFMappable()) {
                ValidPostcodes.add(PC0);
            }
            Status0 = DRecord0.getStatusOfHBClaimAtExtractDate();
            WHBE0 = DRecord0.getWeeklyHousingBenefitEntitlement();
            WERA0 = DRecord0.getWeeklyEligibleRentAmount();
            PSI0 = DRecord0.getPassportedStandardIndicator();
            SHBC0 = DRecord0.getStatusOfHBClaimAtExtractDate();
            RTHBCC0 = DRecord0.getReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective();
            //bCEG = bSHBE_D_Record.getClaimantsEthnicGroup();
            CEG0 = shbeHandler.getEthnicityGroup(DRecord0);
            HS0 = shbeHandler.getHouseholdSize(DRecord0);
            ND0 = DRecord0.getNumberOfNonDependents();
            CD0 = DRecord0.getNumberOfChildDependents();
            PDD0 = DRecord0.getPartnersDateOfDeath();
            CDoB0 = DRecord0.getClaimantsDateOfBirth();
            PDoB0 = DRecord0.getPartnersDateOfBirth();
            CA0 = shbeHandler.getClaimantsAge(year0, month0, DRecord0);
            PA0 = shbeHandler.getPartnersAge(year0, month0, DRecord0);
        }
//        if (cRecord == null) {
//            cTT = ttHandler.iMinus999;
//            cPC = defaultPostcode;
//            cStatus = 0;
//        } else {
//            cD_Record = cRecord.getDRecord();
//            cTT = cD_Record.getTenancyType();
//            cPC = cD_Record.getClaimantsPostcode();
//            if (cPC.isEmpty()) {
//                cPC = defaultPostcode;
//            }
//            if (cRecord.isClaimPostcodeFMappable()) {
//                ValidPostcodes.add(cPC);
//            }
//            cStatus = cD_Record.getStatusOfHBClaimAtExtractDate();
//        }
        key = ClaimRef + DW_Strings.symbol_underscore + sUnderOccupancy;
        aS = TableValues.get(key);

        boolean UO00;
        UO00 = aS.endsWith(sU + DW_Strings.special_commaSpace + sU)
                || aS.endsWith(sU + DW_Strings.special_commaSpace);
        boolean UO0;
        UO0 = aS.endsWith(sU);

        boolean UO1;
        UO1 = (CouncilUOSet1.getMap().keySet().contains(ClaimID)
                || RSLUOSet1.getMap().keySet().contains(ClaimID));
        if (UO1) {
            UOAtSomePointClaimIDs.add(ClaimID);
            if (TT1 == 1) {
                UOTT1AtSomePointClaimIDs.add(ClaimID);
            } else if (TT1 == 4) {
                UOTT4AtSomePointClaimIDs.add(ClaimID);
            }
        }

        if (HBDP1 > 0) {
            UOClaimsRecievingDHPClaimIDs.add(ClaimID);
        }

        // TenancyType
        key = ClaimRef + DW_Strings.symbol_underscore + sTT;
        aS = TableValues.get(key);
        if (TT0 != TT1) {
            if (TT0 == ttHandler.iMinus999
                    || TT1 == ttHandler.iMinus999) {
                if (TT0 == ttHandler.iMinus999) {
                    // Check if there is another TT in aS
                    boolean isAnotherTT;
                    isAnotherTT = isAnotherTT(TT1, aS);
                    if (isAnotherTT) {
                        ChangedTTClaimIDs.add(ClaimID);
                    }
                    if (UOTT1_To_LeftSHBEClaimIDs.contains(ClaimID)) {
                        if (TT1 != 3 || TT1 != 6) {
                            if (UOTT1_To_TT3OrTT6NotDoneNextChangeClaimIDs.contains(ClaimID)) {
                                UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs.add(ClaimID);
                            }
                        }
                    }
                    if (UOTT4_To_LeftSHBEClaimIDs.contains(ClaimID)) {
                        if (TT1 != 3 || TT1 != 6) {
                            if (UOTT4_To_TT3OrTT6NotDoneNextChangeClaimIDs.contains(ClaimID)) {
                                UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs.add(ClaimID);
                            }
                        }
                    }
                    doX(ClaimID,
                            TT1, UO00, UO0, UO1, Status0,
                            UO_To_LeftSHBE_NotReturnedClaimIDs,
                            UOTT1_To_LeftSHBE_NotReturnedClaimIDs,
                            UOTT1_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs,
                            UOTT1_To_LeftSHBE_ReturnedAsTT1ClaimIDs,
                            UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs,
                            UOTT1_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs,
                            UOTT1_To_LeftSHBE_ReturnedAsTT4ClaimIDs,
                            UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs,
                            UOTT1_To_LeftSHBE_ReturnedAsTT8ClaimIDs,
                            UOTT1_To_LeftSHBE_ReturnedAsTT9ClaimIDs,
                            UOTT4_To_LeftSHBE_NotReturnedClaimIDs,
                            UOTT4_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs,
                            UOTT4_To_LeftSHBE_ReturnedAsTT1ClaimIDs,
                            UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs,
                            UOTT4_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs,
                            UOTT4_To_LeftSHBE_ReturnedAsTT4ClaimIDs,
                            UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs,
                            UOTT4_To_LeftSHBE_ReturnedAsTT8ClaimIDs,
                            UOTT4_To_LeftSHBE_ReturnedAsTT9ClaimIDs);
//                            UOTT1_To_UOTT4ClaimIDs,
//                            TT1_To_UOTT4ClaimIDs,
//                            TT1_To_UOTT4GettingDHPClaimIDs);
                }
                if (TT1 == ttHandler.iMinus999) {
                    if (UOAtSomePointClaimIDs.contains(ClaimID)) {
                        UO_To_LeftSHBEAtSomePointClaimIDs.add(ClaimID);
                    }
                    /*
                     * If previously UO (or previously not UO but status 2 and 
                     * prior to previously UO) then add to from UO to left the 
                     * SHBE.
                     */
                    boolean doA;
                    doA = false;
                    if (UO0) {
                        doA = true;
                    } else if (Status0 != 1) {
                        if (UO00) {
                            doA = true;
                        }
                    }
                    if (doA) {
                        UO_To_LeftSHBE_NotReturnedClaimIDs.add(ClaimID);
                        if (TT0 == 1) {
                            UOTT1_To_LeftSHBEClaimIDs.add(ClaimID);
                            UOTT1_To_LeftSHBE_NotReturnedClaimIDs.add(ClaimID);
                            UOTT1_To_TT3OrTT6NotDoneNextChangeClaimIDs.add(ClaimID);
                            UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs.add(ClaimID);
                        } else if (TT0 == 4) {
                            UOTT4_To_LeftSHBEClaimIDs.add(ClaimID);
                            UOTT4_To_LeftSHBE_NotReturnedClaimIDs.add(ClaimID);
                            UOTT4_To_TT3OrTT6NotDoneNextChangeClaimIDs.add(ClaimID);
                            UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs.add(ClaimID);
                        } else if (TT0 == 3 || TT0 == 6) {
                            UOTT3OrTT6_To_LeftSHBEClaimIDs.add(ClaimID);
                            UOTT3OrTT6_To_LeftSHBE_NotReturnedClaimIDs.add(ClaimID);
                        } else {
                            UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBEClaimIDs.add(ClaimID);
                            UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturnedClaimIDs.add(ClaimID);
                        }
                    }
                }
            } else {
                ChangedTTClaimIDs.add(ClaimID);
                if (UO1) {
                    if (TT0 == 4 && TT1 == 1) {
                        //UOTT1OrTT1_To_UOTT4.add(aClaimRef);
                        if (UO0) {
                            UOTT4_To_UOTT1ClaimIDs.add(ClaimID);
                            /*
                             * Looking forward, we may see that this claim 
                             * actually comes out of being UO...
                             */
                        } else if (Status1 != 1 && UO00) {
                            UOTT4_To_UOTT1ClaimIDs.add(ClaimID);
                            /*
                             * Looking forward, we may see that this claim 
                             * actually comes out of being UO...
                             */
                        } else {
                            TT4_To_UOTT1ClaimIDs.add(ClaimID);
                            if (HBDP1 > 0) {
                                TT4_To_UOTT1GettingDHPClaimIDs.add(ClaimID);
                            }
                        }
                    }
                }
                doX(ClaimID,
                        TT1, UO00, UO0, UO1, Status0,
                        UO_To_LeftSHBE_NotReturnedClaimIDs,
                        UOTT1_To_LeftSHBE_NotReturnedClaimIDs,
                        UOTT1_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs,
                        UOTT1_To_LeftSHBE_ReturnedAsTT1ClaimIDs,
                        UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs,
                        UOTT1_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs,
                        UOTT1_To_LeftSHBE_ReturnedAsTT4ClaimIDs,
                        UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs,
                        UOTT1_To_LeftSHBE_ReturnedAsTT8ClaimIDs,
                        UOTT1_To_LeftSHBE_ReturnedAsTT9ClaimIDs,
                        UOTT4_To_LeftSHBE_NotReturnedClaimIDs,
                        UOTT4_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs,
                        UOTT4_To_LeftSHBE_ReturnedAsTT1ClaimIDs,
                        UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs,
                        UOTT4_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs,
                        UOTT4_To_LeftSHBE_ReturnedAsTT4ClaimIDs,
                        UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs,
                        UOTT4_To_LeftSHBE_ReturnedAsTT8ClaimIDs,
                        UOTT4_To_LeftSHBE_ReturnedAsTT9ClaimIDs);
//                            UOTT1_To_UOTT4ClaimIDs,
//                            TT1_To_UOTT4ClaimIDs,
//                            TT1_To_UOTT4GettingDHPClaimIDs);
                if (UOTT4_To_LeftSHBE_NotReturnedClaimIDs.contains(ClaimID)) {
                    UOTT4_To_LeftSHBE_NotReturnedClaimIDs.remove(ClaimID);
                    if (TT1 == 1) {
                        if (UO1) {
                            UOTT4_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs.add(ClaimID);
                        } else {
                            UOTT4_To_LeftSHBE_ReturnedAsTT1ClaimIDs.add(ClaimID);
                        }
                    } else if (TT1 == 4) {
                        if (UO1) {
                            UOTT4_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs.add(ClaimID);
                        } else {
                            UOTT4_To_LeftSHBE_ReturnedAsTT4ClaimIDs.add(ClaimID);
                        }
                    } else if (TT1 == 3 || TT1 == 6) {
                        UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs.add(ClaimID);
                    } else if (TT1 == 5 || TT1 == 7) {
                        UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs.add(ClaimID);
                    } else if (TT1 == 8) {
                        UOTT4_To_LeftSHBE_ReturnedAsTT8ClaimIDs.add(ClaimID);
                    } else if (TT1 == 9) {
                        UOTT4_To_LeftSHBE_ReturnedAsTT9ClaimIDs.add(ClaimID);
                    }
                }
                if (UOTT3OrTT6_To_LeftSHBE_NotReturnedClaimIDs.contains(ClaimID)) {
                    UOTT3OrTT6_To_LeftSHBE_NotReturnedClaimIDs.remove(ClaimID);
                    if (TT1 == 1) {
                        if (UO1) {
                            UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1ClaimIDs.add(ClaimID);
                        } else {
                            UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1ClaimIDs.add(ClaimID);
                        }
                    } else if (TT1 == 4) {
                        if (UO1) {
                            UOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs.add(ClaimID);
                        } else {
                            UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4ClaimIDs.add(ClaimID);
                        }
                    } else if (TT1 == 3 || TT1 == 6) {
                        UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs.add(ClaimID);
                    } else if (TT1 == 5 || TT1 == 7) {
                        UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs.add(ClaimID);
                    } else if (TT1 == 8) {
                        UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8ClaimIDs.add(ClaimID);
                    } else if (TT1 == 9) {
                        UOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9ClaimIDs.add(ClaimID);
                    }
                }
                if (UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturnedClaimIDs.contains(ClaimID)) {
                    UONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturnedClaimIDs.remove(ClaimID);
                }
                if (UO_To_LeftSHBE_NotReturnedClaimIDs.contains(ClaimID)) {
                    UO_To_LeftSHBE_NotReturnedClaimIDs.remove(ClaimID);
                }
                if (TT1 == 3 || TT1 == 6) {
                    if (UOTT1_To_LeftSHBEClaimIDs.contains(ClaimID)) {
                        if (UOTT1_To_TT3OrTT6NotDoneNextChangeClaimIDs.contains(ClaimID)) {
                            UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs.add(ClaimID);
                        }
                    }
                    if (UOTT4_To_LeftSHBEClaimIDs.contains(ClaimID)) {
                        if (UOTT4_To_TT3OrTT6NotDoneNextChangeClaimIDs.contains(ClaimID)) {
                            UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs.add(ClaimID);
                        }
                    }
                    if (UOTT1AtSomePointClaimIDs.contains(ClaimID)) {
                        UOTT1_To_TT3OrTT6AtSomePointClaimIDs.add(ClaimID);
                    }
                    if (UOTT4AtSomePointClaimIDs.contains(ClaimID)) {
                        UOTT4_To_TT3OrTT6AtSomePointClaimIDs.add(ClaimID);
                    }
                    if (TT0 == 1) {
                        TT1_To_TT3OrTT6ClaimIDs.add(ClaimID);
                        // If previously UO then add to set of those that move from UO TT1 to TT3 or TT6
                        if (UO0) {
                            UOTT1_To_TT3OrTT6ClaimIDs.add(ClaimID);
                            UOTT1_To_TT3OrTT6NotDoneNextChangeClaimIDs.add(ClaimID);
                            UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs.add(ClaimID);
                        } else if (Status0 != 1) {
                            if (UO00) {
                                UOTT1_To_TT3OrTT6ClaimIDs.add(ClaimID);
                                UOTT1_To_TT3OrTT6NotDoneNextChangeClaimIDs.add(ClaimID);
                                UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs.add(ClaimID);
                            }
                        }
                    } else if (TT0 == 4) {
                        TT4_To_TT3OrTT6ClaimIDs.add(ClaimID);
                        // If previously UO then add to set of those that move from UO TT4 to TT3 or TT6
                        if (UO0) {
                            UOTT4_To_TT3OrTT6ClaimIDs.add(ClaimID);
                            UOTT4_To_TT3OrTT6NotDoneNextChangeClaimIDs.add(ClaimID);
                            UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs.add(ClaimID);
                        } else if (Status0 != 1) {
                            if (UO00) {
                                UOTT4_To_TT3OrTT6ClaimIDs.add(ClaimID);
                                UOTT4_To_TT3OrTT6NotDoneNextChangeClaimIDs.add(ClaimID);
                                UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs.add(ClaimID);
                            }
                        }
                    }
                } else {
                    if (UOTT1_To_TT3OrTT6NotDoneNextChangeClaimIDs.contains(ClaimID)) {
                        if (UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs.contains(ClaimID)) {
                            UOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs.add(ClaimID);
                        }
                    }
                    if (UOTT4_To_TT3OrTT6NotDoneNextChangeClaimIDs.contains(ClaimID)) {
                        if (UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs.contains(ClaimID)) {
                            UOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999ClaimIDs.remove(ClaimID);
                        }
                    }
                    if (TT0 == 3 || TT0 == 6) {
                        if (TT1 == 1) {
                            // If UO add to set that move from the PRS to Council UO.
                            if (UO1) {
                                TT3OrTT6_To_UOTT1ClaimIDs.add(ClaimID);  // Looking forward, we may see that this claim actually comes out of being UO almost immediately. This is kind of different to those claims that get stuck in UO for a significant period.
                            } else {
                                TT3OrTT6_To_TT1ClaimIDs.add(ClaimID);
                            }
                        } else if (TT1 == 4) {
                            // If UO add to set that move from the PRS to RSL UO.
                            if (UO1) {
                                TT3OrTT6_To_UOTT4ClaimIDs.add(ClaimID);  // Looking forward, we may see that this claim actually comes out of being UO almost immediately. This is kind of different to those claims that get stuck in UO for a significant period.
                            } else {
                                TT3OrTT6_To_TT4ClaimIDs.add(ClaimID);
                            }
                        }
                    }
                }
            }
        }
        aS += DW_Strings.special_commaSpace + sTT_ + TT1;

        TableValues.put(key, aS);

        // UnderOccupancy
        key = ClaimRef + DW_Strings.symbol_underscore + sUnderOccupancy;
        aS = TableValues.get(key);
        if (UO1) {
            PermanantlyLeftUOButRemainedInSHBEClaimIDs.remove(ClaimID);
            PermanantlyLeftUOButRemainedInSHBEPostcodeChangedClaimIDs.remove(ClaimID);
            PermanantlyLeftUOButRemainedInSHBEHouseholdSizeIncreasedClaimIDs.remove(ClaimID);
            result[0] = true;
            tUOClaims.add(ClaimID);
            if (TT1 == 1) {
                AlwaysUOTT1FromWhenStartedClaimIDs.add(ClaimID);
            } else if (TT1 == 4) {
                AlwaysUOTT4FromWhenStartedClaimIDs.add(ClaimID);
            }
            aS += DW_Strings.special_commaSpace + sU;
            BigDecimal bd;
            String key2 = ClaimRef + DW_Strings.symbol_underscore + sTotalCount_UnderOccupancy;
            bd = AggregateStatistics.get(key2);
            bd = bd.add(BigDecimal.ONE);
            AggregateStatistics.put(key2, bd);
            if (DRecord1 != null) {
                if (CouncilUOSet1.getMap().keySet().contains(ClaimID)) {
                    result[1] = true;
                    // Add to Council
                    addToSets(ClaimID, CouncilUniqueClaimantsEffectedPersonIDs,
                            CouncilUniquePartnersEffectedPersonIDs,
                            CouncilUniqueDependentChildrenUnder10EffectedPersonIDs,
                            CouncilUniqueDependentChildrenOver10EffectedPersonIDs,
                            CouncilUniqueNonDependentsEffectedPersonIDs,
                            CouncilMaxNumberOfDependentsInClaimWhenUO,
                            year1, month1, YM31, Record1.getSRecords(),
                            DRecord1, claimIDToClaimantPersonIDLookup);
                    DW_UO_Record rec = CouncilUOSet1.getMap().get(ClaimID);
                    int bedrooms = rec.getBedroomsInProperty();
                    int householdSizeSHBE;
                    householdSizeSHBE = shbeHandler.getHouseholdSizeExcludingPartnersint(DRecord1);
                    result[5] = householdSizeSHBE;
                    if (householdSizeSHBE >= bedrooms) {
                        result[3] = true;
                    }
                    int householdSizeUO;
                    householdSizeUO = DW_UO_Handler.getHouseholdSizeExcludingPartners(rec);
                    if (householdSizeUO >= bedrooms) {
                        result[4] = true;
                    }
                    result[6] = householdSizeUO;
                    int partner;
                    partner = DRecord1.getPartnerFlag();
                    householdSizeSHBE += partner;
                    householdSizeUO += partner;
                    result[7] = householdSizeSHBE;
                    result[8] = householdSizeUO;
                }
                if (RSLUOSet1.getMap().keySet().contains(ClaimID)) {
                    result[2] = true;
                    addToSets(
                            ClaimID,
                            RSLUniqueClaimantsEffectedPersonIDs,
                            RSLUniquePartnersEffectedPersonIDs,
                            RSLUniqueDependentChildrenUnder10EffectedPersonIDs,
                            RSLUniqueDependentChildrenOver10EffectedPersonIDs,
                            RSLUniqueNonDependentsEffectedPersonIDs,
                            RSLMaxNumberOfDependentsInClaimWhenUO,
                            year1,
                            month1,
                            YM31,
                            Record1.getSRecords(),
                            DRecord1,
                            claimIDToClaimantPersonIDLookup);
                    DW_UO_Record rec = RSLUOSet1.getMap().get(ClaimID);
                    int bedrooms = rec.getBedroomsInProperty();
                    int householdSizeSHBE;
                    householdSizeSHBE = shbeHandler.getHouseholdSizeExcludingPartnersint(DRecord1);
                    result[5] = householdSizeSHBE;
                    if (householdSizeSHBE >= bedrooms) {
                        result[3] = true;
                    }
                    int householdSizeUO;
                    householdSizeUO = DW_UO_Handler.getHouseholdSizeExcludingPartners(rec);
                    if (householdSizeUO >= bedrooms) {
                        result[4] = true;
                    }
                    result[6] = householdSizeUO;
                    int partner;
                    partner = DRecord1.getPartnerFlag();
                    householdSizeSHBE += partner;
                    householdSizeUO += partner;
                    result[7] = householdSizeSHBE;
                    result[8] = householdSizeUO;
                }
            }
            if (!(TT1 == 1 || TT1 == 4 || TT1 == ttHandler.iMinus999)) {
                TTNot1Or4AndUnderOccupyingClaimIDs.add(ClaimID);
            }
            if (!UO0) {
                if (!(Status0 == 2 && UO00)) {
                    // UO OnFlow
                    if (TT1 == TT0) {
                        // Became UO staying in the same TT and postcode.
                        // Here we only count room requirement changes at the same 
                        // postcode (postcode changes are dealt with below).
                        if (PC1.equalsIgnoreCase(PC0)) {
                            if (TT1 == 1) {
                                TT1_To_UOTT1_PostcodeUnchangedClaimIDs.add(ClaimID);
                                TT1_To_UOTT1_PostcodeUnchangedThisMonthClaimIDs.add(ClaimID);
                            } else if (TT1 == 4) {
                                TT4_To_UOTT4_PostcodeUnchangedClaimIDs.add(ClaimID);
                                TT4_To_UOTT4_PostcodeUnchangedThisMonthClaimIDs.add(ClaimID);
                            }
                        }
                    }
                    if (UO_NotUOClaimIDs.contains(ClaimID)) {
                        UO_NotUO_UOClaimIDs.add(ClaimID);
                        UO_NotUOClaimIDs.remove(ClaimID);
                    } else if (UO_NotUO_UO_NotUOClaimIDs.contains(ClaimID)) {
                        UO_NotUO_UO_NotUO_UOClaimIDs.add(ClaimID);
                        UO_NotUO_UO_NotUOClaimIDs.remove(ClaimID);
                    } else if (UO_NotUO_UO_NotUO_UO_NotUOClaimIDs.contains(ClaimID)) {
                        UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs.add(ClaimID);
                        UO_NotUO_UO_NotUO_UO_NotUOClaimIDs.remove(ClaimID);
                    } else if (UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUOClaimIDs.contains(ClaimID)) {
                        UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs.add(ClaimID);
                        UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUOClaimIDs.remove(ClaimID);
                    }
                }
            }
            if (UOTT1_To_LeftSHBEClaimIDs.contains(ClaimID)) {
                UOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePointClaimIDs.add(ClaimID);
            }
            if (UOTT4_To_LeftSHBEClaimIDs.contains(ClaimID)) {
                UOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePointClaimIDs.add(ClaimID);
            }
        } else {
            aS += DW_Strings.special_commaSpace;
            AlwaysUOTT1FromStartClaimIDs.remove(ClaimID);
            AlwaysUOTT4FromStartClaimIDs.remove(ClaimID);
            if (Status1 == 2) {
                // Filter added as suspended claims that were UO are probably still UO
            } else {
                AlwaysUOTT1FromStartExceptWhenSuspendedClaimIDs.remove(ClaimID);
                AlwaysUOTT4FromStartExceptWhenSuspendedClaimIDs.remove(ClaimID);
                if (aS.contains(sU)) {
                    AlwaysUOTT1FromWhenStartedClaimIDs.remove(ClaimID);
                    AlwaysUOTT4FromWhenStartedClaimIDs.remove(ClaimID);
                    if (aS.contains(sU + DW_Strings.special_commaSpace + DW_Strings.special_commaSpace)) {
                        // ..., U, ,
                        IntermitantUOClaimIDs.add(ClaimID);
                    }
                }
                if (UO0) {
                    if (UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs.contains(ClaimID)) {
                        UO_NotUO_UO_NotUO_UO_NotUO_UO_NotUOClaimIDs.add(ClaimID);
                        UO_NotUO_UO_NotUO_UO_NotUO_UOClaimIDs.remove(ClaimID);
                    } else if (UO_NotUO_UO_NotUO_UOClaimIDs.contains(ClaimID)) {
                        UO_NotUO_UO_NotUO_UO_NotUOClaimIDs.add(ClaimID);
                        UO_NotUO_UO_NotUO_UOClaimIDs.remove(ClaimID);
                    } else if (UO_NotUO_UOClaimIDs.contains(ClaimID)) {
                        UO_NotUO_UO_NotUOClaimIDs.add(ClaimID);
                        UO_NotUO_UOClaimIDs.remove(ClaimID);
                    } else {
                        UO_NotUOClaimIDs.add(ClaimID);
                    }
                    if (TT1 == ttHandler.iMinus999) {
                        UO_To_LeftSHBETheVeryNextMonthClaimIDs.add(ClaimID);
                    } else {
                        PermanantlyLeftUOButRemainedInSHBEClaimIDs.add(ClaimID);
                        if (HS1 > HS0) {
                            PermanantlyLeftUOButRemainedInSHBEHouseholdSizeIncreasedClaimIDs.add(ClaimID);
                        }
                        if (!PC1.equalsIgnoreCase(PC0)) {
                            if (Record1 != null && Record0 != null) {
                                if (Record1.isClaimPostcodeFMappable() && Record0.isClaimPostcodeFMappable()) {
                                    PermanantlyLeftUOButRemainedInSHBEPostcodeChangedClaimIDs.add(ClaimID);
                                    if (TT0 == 1) {
                                        UOTT1_To_NotUO_InSHBE_PostcodeChangedClaimIDs.add(ClaimID);
                                    }
                                    if (TT1 == 1) {
                                        if (TT0 == 1) {
                                            UOTT1_To_TT1_PostcodeChangedClaimIDs.add(ClaimID);
                                        } else if (TT0 == 4) {
                                            UOTT4_To_TT1_PostcodeChangedClaimIDs.add(ClaimID);
                                        }
                                    } else if (TT1 == 4) {
                                        UOTT4_To_NotUO_InSHBE_PostcodeChangedClaimIDs.add(ClaimID);
                                        if (TT0 == 1) {
                                            UOTT1_To_TT4_PostcodeChangedClaimIDs.add(ClaimID);
                                        } else if (TT0 == 4) {
                                            UOTT4_To_TT4_PostcodeChangedClaimIDs.add(ClaimID);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (TT1 == TT0) {
                        // SolvedUO problem staying in same TT
                        // Cases involving a postcode change are dealt with below.
                        if (PC1.equalsIgnoreCase(PC0)) {
                            // Resolved UO without moving
                            // Room requirement changed or number of rooms reduced or both?
                            if (TT1 == 1) {
                                UOTT1_To_TT1_PostcodeUnchangedClaimIDs.add(ClaimID);
                                UOTT1_To_TT1_PostcodeUnchangedThisMonthClaimIDs.add(ClaimID);
                            } else if (TT1 == 4) {
                                UOTT4_To_TT4_PostcodeUnchangedClaimIDs.add(ClaimID);
                                UOTT4_To_TT4_PostcodeUnchangedThisMonthClaimIDs.add(ClaimID);
                            }
                        }
                    }
                }
            }
        }

        TableValues.put(key, aS);

        // Postcode
        key = ClaimRef + DW_Strings.symbol_underscore + sP;
        aS = TableValues.get(key);

        if (PC1.equalsIgnoreCase(PC0)) {
            aS += DW_Strings.special_commaSpace;
        } else {
            boolean aSContainsaPC = aS.contains(PC1);
            aS += DW_Strings.special_commaSpace + PC1;
            if (!PC1.equalsIgnoreCase(defaultPostcode)) {
                boolean containsAnotherPostcode;
                if (PC0.equalsIgnoreCase(defaultPostcode)) {
                    containsAnotherPostcode = getContainsAnotherPostcode(aS, PC1);
                    if (aSContainsaPC) {
                        if (containsAnotherPostcode) {
                            boolean likelyTraveller;
                            likelyTraveller = getLikelyTraveller(aS, PC1);
                            if (likelyTraveller) {
                                TravellerClaimIDs.add(ClaimID);
                            }
                            if (Record1 != null && Record0 != null) {
                                if (Record1.isClaimPostcodeFMappable() && Record0.isClaimPostcodeFMappable()) {
                                    ValidPostcodeChangeClaimIDs.add(ClaimID);
                                }
                            }
                        }
                    } else if (containsAnotherPostcode) {
                        if (Record1 != null && Record0 != null) {
                            if (Record1.isClaimPostcodeFMappable() && Record0.isClaimPostcodeFMappable()) {
                                ValidPostcodeChangeClaimIDs.add(ClaimID);
                            }
                        }
                    }
                } else {
                    if (Record1 != null && Record0 != null) {
                        if (Record1.isClaimPostcodeFMappable() && Record0.isClaimPostcodeFMappable()) {
                            ValidPostcodeChangeClaimIDs.add(ClaimID);
                        }
                    }
                    if (aSContainsaPC) {
//                        containsAnotherPostcode = getContainsAnotherPostcode(aS, aPC);
//                        if (containsAnotherPostcode) {
                        boolean likelyTraveller;
                        likelyTraveller = getLikelyTraveller(aS, PC1);
                        if (likelyTraveller) {
                            TravellerClaimIDs.add(ClaimID);
                        }
//                        }
//                        NoValidPostcodeChange.remove(aClaimRef);
//                    } else {
//                        containsAnotherPostcode = getContainsAnotherPostcode(aS, aPC);
//                        if (containsAnotherPostcode) {
//                            NoValidPostcodeChange.remove(aClaimRef);
//                        }
                    }
                    if (UO0 && !UO1) {
                        if (TT1 == TT0) {
                            if (TT1 == 1) {
                                if (Record1 != null) {
                                    if (Record1.isClaimPostcodeFMappable()) {
                                        UOTT1_ToTT1_PostcodeChangedClaimIDs.add(ClaimID);
                                    }
                                }
                            } else if (TT1 == 4) {
                                if (Record1 != null) {
                                    if (Record1.isClaimPostcodeFMappable()) {
                                        UOTT4_ToTT4_PostcodeChangedClaimIDs.add(ClaimID);
                                    }
                                }
                            }
                        }
                    } else if (!UO0 && UO1) {
                        if (TT1 == TT0) {
                            if (TT1 == 1) {
                                if (Record1 != null) {
                                    if (Record1.isClaimPostcodeFMappable()) {
                                        TT1_ToUOTT1_PostcodeChangedClaimIDs.add(ClaimID);
                                    }
                                }
                            } else if (TT1 == 4) {
                                if (Record1 != null) {
                                    if (Record1.isClaimPostcodeFMappable()) {
                                        TT4_ToUOTT4_PostcodeChangedClaimIDs.add(ClaimID);
                                    }
                                }
                            }
                        }
                    } else if (UO0 && UO1) {
                        if (TT1 == TT0) {
                            if (TT1 == 1) {
                                if (Record1.isClaimPostcodeFMappable()) {
                                    UOTT1_ToUOTT1_PostcodeChangedClaimIDs.add(ClaimID);
                                }
                            } else if (TT1 == 4) {
                                if (Record1.isClaimPostcodeFMappable()) {
                                    UOTT4_ToUOTT4_PostcodeChangedClaimIDs.add(ClaimID);
                                }
                            }
                        }
                    }
                    if (Record1 != null && Record0 != null) {

                        if (Record1.isClaimPostcodeFMappable() && Record0.isClaimPostcodeFMappable()) {
                            if (TT1_To_UOTT1_PostcodeUnchanged1MonthPreviousClaimIDs.contains(ClaimID)) {
                                if (TT1 == 1) {
                                    //TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month.add(aClaimRef);
                                    if (UO1) {
                                        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs.add(ClaimID);
                                    } else {
                                        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs.add(ClaimID);
                                    }
                                } else if (TT1 == 3 || TT1 == 6) {
                                    TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs.add(ClaimID);
                                } else if (TT1 == 4) {
                                    if (UO1) {
                                        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs.add(ClaimID);
                                    } else {
                                        TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs.add(ClaimID);
                                    }
                                } else if (TT1 == 5 || TT1 == 7) {
                                    TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs.add(ClaimID);
                                } else if (TT1 == 8) {
                                    TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs.add(ClaimID);
                                } else if (TT1 == 9) {
                                    TT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs.add(ClaimID);
                                }
                            }
                            if (TT4_To_UOTT4_PostcodeUnchanged1MonthPreviousClaimIDs.contains(ClaimID)) {
                                if (TT1 == 1) {
                                    //TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month.add(aClaimRef);
                                    if (UO1) {
                                        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs.add(ClaimID);
                                    } else {
                                        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs.add(ClaimID);
                                    }
                                } else if (TT1 == 3 || TT1 == 6) {
                                    TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs.add(ClaimID);
                                } else if (TT1 == 4) {
                                    if (UO1) {
                                        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs.add(ClaimID);
                                    } else {
                                        TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs.add(ClaimID);
                                    }
                                } else if (TT1 == 5 || TT1 == 7) {
                                    TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs.add(ClaimID);
                                } else if (TT1 == 8) {
                                    TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs.add(ClaimID);
                                } else if (TT1 == 9) {
                                    TT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs.add(ClaimID);
                                }
                            }

                            if (TT4_To_UOTT4_PostcodeUnchanged2MonthsPreviousClaimIDs.contains(ClaimID)) {
                                if (TT1 == 4) {
                                    TT4_To_UOTT4_PostcodeUnchangedButChangedAfter2MonthsClaimIDs.add(ClaimID);
                                }
                            }
                            if (TT1_To_UOTT1_PostcodeUnchanged2MonthsPreviousClaimIDs.contains(ClaimID)) {
                                if (TT1 == 1) { // Additional filter added as we only want those that are in the same TT.
                                    TT1_To_UOTT1_PostcodeUnchangedButChangedAfter2MonthsClaimIDs.add(ClaimID);
                                }
                            }
                            if (TT4_To_UOTT4_PostcodeUnchanged3MonthsPreviousClaimIDs.contains(ClaimID)) {
                                if (TT1 == 4) { // Additional filter added as we only want those that are in the same TT.
                                    TT4_To_UOTT4_PostcodeUnchangedButChangedAfter3MonthsClaimIDs.add(ClaimID);
                                }
                            }
                            if (TT1_To_UOTT1_PostcodeUnchanged3MonthsPreviousClaimIDs.contains(ClaimID)) {
                                if (TT1 == 1) {
                                    TT1_To_UOTT1_PostcodeUnchangedButChangedAfter3MonthsClaimIDs.add(ClaimID);
                                }
                            }
                            if (UOTT4_To_TT4_PostcodeUnchanged1MonthPreviousClaimIDs.contains(ClaimID)) {
                                if (TT1 == 1) {
                                    if (UO1) {
                                        UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs.add(ClaimID);
                                    } else {
                                        UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs.add(ClaimID);
                                    }
                                } else if (TT1 == 4) {
                                    if (UO1) {
                                        UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs.add(ClaimID);
                                    } else {
                                        UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs.add(ClaimID);
                                    }
                                } else if (TT1 == 3 || TT1 == 6) {
                                    UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs.add(ClaimID);
                                } else if (TT1 == 5 || TT1 == 7) {
                                    UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs.add(ClaimID);
                                } else if (TT1 == 8) {
                                    UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs.add(ClaimID);
                                } else if (TT1 == 9) {
                                    UOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs.add(ClaimID);
                                }
                            }
                            if (UOTT1_To_TT1_PostcodeUnchanged1MonthPreviousClaimIDs.contains(ClaimID)) {
                                if (TT1 == 1) {
                                    if (UO1) {
                                        UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1ClaimIDs.add(ClaimID);
                                    } else {
                                        UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1ClaimIDs.add(ClaimID);
                                    }
                                } else if (TT1 == 4) {
                                    if (UO1) {
                                        UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4ClaimIDs.add(ClaimID);
                                    } else {
                                        UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4ClaimIDs.add(ClaimID);
                                    }
                                } else if (TT1 == 3 || TT1 == 6) {
                                    UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6ClaimIDs.add(ClaimID);
                                } else if (TT1 == 5 || TT1 == 7) {
                                    UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7ClaimIDs.add(ClaimID);
                                } else if (TT1 == 8) {
                                    UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8ClaimIDs.add(ClaimID);
                                } else if (TT1 == 9) {
                                    UOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9ClaimIDs.add(ClaimID);
                                }
                            }
                            if (UOTT4_To_TT4_PostcodeUnchanged2MonthsPreviousClaimIDs.contains(ClaimID)) {
                                if (TT1 == 4) { // Additional filter added as we only want those that are in the same TT.
                                    UOTT4_To_TT4_PostcodeUnchangedButChangedAfter2MonthsClaimIDs.add(ClaimID);
                                }
                            }
                            if (UOTT1_To_TT1_PostcodeUnchanged2MonthsPreviousClaimIDs.contains(ClaimID)) {
                                if (TT1 == 1) { // Additional filter added as we only want those that are in the same TT.
                                    UOTT1_To_TT1_PostcodeUnchangedButChangedAfter2MonthsClaimIDs.add(ClaimID);
                                }
                            }
                            if (UOTT4_To_TT4_PostcodeUnchanged3MonthsPreviousClaimIDs.contains(ClaimID)) {
                                if (TT1 == 4) { // Additional filter added as we only want those that are in the same TT.
                                    UOTT4_To_TT4_PostcodeUnchangedButChangedAfter3MonthsClaimIDs.add(ClaimID);
                                }
                            }
                            if (UOTT1_To_TT1_PostcodeUnchanged3MonthsPreviousClaimIDs.contains(ClaimID)) {
                                if (TT1 == 1) { // Additional filter added as we only want those that are in the same TT.
                                    UOTT1_To_TT1_PostcodeUnchangedButChangedAfter3MonthsClaimIDs.add(ClaimID);
                                }
                            }
                        }
                    }
                }
            }
        }

        TableValues.put(key, aS);

        // HB Entitlement
        key = ClaimRef + DW_Strings.symbol_underscore + sWHBE;
        aS = TableValues.get(key);
        if (WHBE1 == WHBE0) {
            aS += DW_Strings.special_commaSpace;
        } else {
            aS += DW_Strings.special_commaSpace + decimalise(WHBE1);
        }

        TableValues.put(key, aS);

        // ERA
        key = ClaimRef + DW_Strings.symbol_underscore + sWERA;
        aS = TableValues.get(key);
        if (WERA1 == WERA0) {
            aS += DW_Strings.special_commaSpace;
        } else {
            aS += DW_Strings.special_commaSpace + decimalise(WERA1);
        }

        TableValues.put(key, aS);

        // PassportedStandardIndicator
        key = ClaimRef + DW_Strings.symbol_underscore + sPSI;
        aS = TableValues.get(key);
        if (PSI1 == PSI0) {
            aS += DW_Strings.special_commaSpace;
        } else {
            aS += DW_Strings.special_commaSpace + PSI1;
        }

        TableValues.put(key, aS);

        // StatusOfHBClaim
        key = ClaimRef + DW_Strings.symbol_underscore + sSHBC;
        aS = TableValues.get(key);
        if (SHBC1 == SHBC0) {
            aS += DW_Strings.special_commaSpace;
        } else {
            aS += DW_Strings.special_commaSpace + SHBC1;
        }

        TableValues.put(key, aS);

        // ReasonThatHBClaimClosed
        key = ClaimRef + DW_Strings.symbol_underscore + sRTHBCC;
        aS = TableValues.get(key);
        if (RTHBCC1 == RTHBCC0) {
            aS += DW_Strings.special_commaSpace;
        } else {
            aS += DW_Strings.special_commaSpace + RTHBCC1;
        }

        TableValues.put(key, aS);

        // ClaimantEthnicGroup
        key = ClaimRef + DW_Strings.symbol_underscore + sCEG;
        aS = TableValues.get(key);
        if (CEG1 == CEG0) {
            aS += DW_Strings.special_commaSpace;
        } else {
            aS += DW_Strings.special_commaSpace + CEG1;
        }

        TableValues.put(key, aS);

        // HS
        key = ClaimRef + DW_Strings.symbol_underscore + sHS;
        aS = TableValues.get(key);
        if (HS1 == HS0) {
            aS += DW_Strings.special_commaSpace;
        } else {
            aS += DW_Strings.special_commaSpace + HS1;
        }

        TableValues.put(key, aS);

        // NonDependents
        key = ClaimRef + DW_Strings.symbol_underscore + sND;
        aS = TableValues.get(key);
        if (ND1 == ND0) {
            aS += DW_Strings.special_commaSpace;
        } else {
            aS += DW_Strings.special_commaSpace + ND1;
        }

        TableValues.put(key, aS);

        // ChildDependents
        key = ClaimRef + DW_Strings.symbol_underscore + sCD;
        aS = TableValues.get(key);
        if (CD1 == CD0) {
            aS += DW_Strings.special_commaSpace;
        } else {
            aS += DW_Strings.special_commaSpace + CD1;
        }

        TableValues.put(key, aS);

        // UO
        DW_UO_Record aDW_UOReport_Record;

        if (CouncilUOSet1.getMap().keySet().contains(ClaimID)
                || RSLUOSet1.getMap().keySet().contains(ClaimID)) {
            if (CouncilUOSet1.getMap().keySet().contains(ClaimID)) {
                aDW_UOReport_Record = CouncilUOSet1.getMap().get(ClaimID);
            } else {
                aDW_UOReport_Record = RSLUOSet1.getMap().get(ClaimID);
            }
            // NonDependents
            key = ClaimRef + DW_Strings.symbol_underscore + sNDUO;
            aS = TableValues.get(key);
            aS += DW_Strings.special_commaSpace + aDW_UOReport_Record.getNonDependents();
            TableValues.put(key, aS);
            // Children 16 +
            key = ClaimRef + DW_Strings.symbol_underscore + sCO16;
            aS = TableValues.get(key);
            aS += DW_Strings.special_commaSpace + aDW_UOReport_Record.getChildrenOver16();
            TableValues.put(key, aS);
            // FemaleChildrenUnder10
            key = ClaimRef + DW_Strings.symbol_underscore + sFCU10;
            aS = TableValues.get(key);
            aS += DW_Strings.special_commaSpace + aDW_UOReport_Record.getFemaleChildrenUnder10();
            TableValues.put(key, aS);
            // MaleChildrenUnder10
            key = ClaimRef + DW_Strings.symbol_underscore + sMCU10;
            aS = TableValues.get(key);
            aS += DW_Strings.special_commaSpace + aDW_UOReport_Record.getMaleChildrenUnder10();
            TableValues.put(key, aS);
            // FemaleChildren10to16
            key = ClaimRef + DW_Strings.symbol_underscore + sFC10To16;
            aS = TableValues.get(key);
            aS += DW_Strings.special_commaSpace + aDW_UOReport_Record.getFemaleChildren10to16();
            TableValues.put(key, aS);
            // MaleChildren10to16
            key = ClaimRef + DW_Strings.symbol_underscore + sMC10To16;
            aS = TableValues.get(key);
            aS += DW_Strings.special_commaSpace + aDW_UOReport_Record.getMaleChildren10to16();
            TableValues.put(key, aS);
            // Number of Bedrooms
            key = ClaimRef + DW_Strings.symbol_underscore + sNB;
            aS = TableValues.get(key);
            aS += DW_Strings.special_commaSpace + aDW_UOReport_Record.getBedroomsInProperty();
            TableValues.put(key, aS);
            // Bedroom Requirement
            key = ClaimRef + DW_Strings.symbol_underscore + sBR;
            aS = TableValues.get(key);
            aS += DW_Strings.special_commaSpace + aDW_UOReport_Record.getBedroomRequirement();
            TableValues.put(key, aS);
            int loss;
            loss = WERA1 - WHBE1;
            key = ClaimRef + DW_Strings.symbol_underscore + sTotal_HBLossDueToUO;
            BigDecimal bd;
            bd = AggregateStatistics.get(key);
            bd = bd.add(BigDecimal.valueOf(loss));
            AggregateStatistics.put(key, bd);
            if (loss > 0) {
                key = ClaimRef + DW_Strings.symbol_underscore + sTotalCount_HBLossDueToUO;
                bd = AggregateStatistics.get(key);
                bd = bd.add(BigDecimal.ONE);
                AggregateStatistics.put(key, bd);
            }
        } else {
            // NonDependents
            key = ClaimRef + DW_Strings.symbol_underscore + sNDUO;
            aS = TableValues.get(key);
            aS += DW_Strings.special_commaSpace;
            TableValues.put(key, aS);
            // Children 16 +
            key = ClaimRef + DW_Strings.symbol_underscore + sCO16;
            aS = TableValues.get(key);
            aS += DW_Strings.special_commaSpace;
            TableValues.put(key, aS);
            // FemaleChildrenUnder10
            key = ClaimRef + DW_Strings.symbol_underscore + sFCU10;
            aS = TableValues.get(key);
            aS += DW_Strings.special_commaSpace;
            TableValues.put(key, aS);
            // MaleChildrenUnder10
            key = ClaimRef + DW_Strings.symbol_underscore + sMCU10;
            aS = TableValues.get(key);
            aS += DW_Strings.special_commaSpace;
            TableValues.put(key, aS);
            // FemaleChildren10to16
            key = ClaimRef + DW_Strings.symbol_underscore + sFC10To16;
            aS = TableValues.get(key);
            aS += DW_Strings.special_commaSpace;
            TableValues.put(key, aS);
            // MaleChildren10to16
            key = ClaimRef + DW_Strings.symbol_underscore + sMC10To16;
            aS = TableValues.get(key);
            aS += DW_Strings.special_commaSpace;
            TableValues.put(key, aS);
            // Number of Bedrooms
            key = ClaimRef + DW_Strings.symbol_underscore + sNB;
            aS = TableValues.get(key);
            aS += DW_Strings.special_commaSpace;
            TableValues.put(key, aS);
            // Bedroom Requirement
            key = ClaimRef + DW_Strings.symbol_underscore + sBR;
            aS = TableValues.get(key);
            aS += DW_Strings.special_commaSpace;
            TableValues.put(key, aS);
        }
        // Claimants DoB
        key = ClaimRef + DW_Strings.symbol_underscore + sCDoB;
        aS = TableValues.get(key);

        if (CDoB1.equalsIgnoreCase(CDoB0)) {
            aS += DW_Strings.special_commaSpace;
        } else {
            aS += DW_Strings.special_commaSpace + CDoB1;
        }

        TableValues.put(key, aS);
        // Claimants Age
        key = ClaimRef + DW_Strings.symbol_underscore + sCA;
        aS = TableValues.get(key);

        if (CA1.equalsIgnoreCase(CA0)) {
            aS += DW_Strings.special_commaSpace;
        } else {
            aS += DW_Strings.special_commaSpace + CA1;
        }

        TableValues.put(key, aS);
        // Partners DoB
        key = ClaimRef + DW_Strings.symbol_underscore + sPDoB;
        aS = TableValues.get(key);

        if (PDoB1.equalsIgnoreCase(PDoB0)) {
            aS += DW_Strings.special_commaSpace;
        } else {
            aS += DW_Strings.special_commaSpace + PDoB1;
        }

        TableValues.put(key, aS);
        // Partners Age
        key = ClaimRef + DW_Strings.symbol_underscore + sPA;
        aS = TableValues.get(key);

        if (PA1.equalsIgnoreCase(PA0)) {
            aS += DW_Strings.special_commaSpace;
        } else {
            aS += DW_Strings.special_commaSpace + PA1;
        }

        TableValues.put(key, aS);
        // ClaimantsGender
        key = ClaimRef + DW_Strings.symbol_underscore + sCG;
        aS = TableValues.get(key);

        if (CA1.equalsIgnoreCase(CA0)) {
            aS += DW_Strings.special_commaSpace;
        } else {
            aS += DW_Strings.special_commaSpace + CG1;
        }

        TableValues.put(key, aS);
        // PartnersGender
        key = ClaimRef + DW_Strings.symbol_underscore + sPG;
        aS = TableValues.get(key);

        if (PA1.equalsIgnoreCase(PA0)) {
            aS += DW_Strings.special_commaSpace;
        } else {
            aS += DW_Strings.special_commaSpace + PG1;
        }

        TableValues.put(key, aS);
        // Disability
        key = ClaimRef + DW_Strings.symbol_underscore + sDisability;
        aS = TableValues.get(key);
        aS += DW_Strings.special_commaSpace + D1;

        TableValues.put(key, aS);
        // Disability Premium
        key = ClaimRef + DW_Strings.symbol_underscore + sDisabilityPremium;
        aS = TableValues.get(key);
        aS += DW_Strings.special_commaSpace + DP1;

        TableValues.put(key, aS);
        // Disability Severe
        key = ClaimRef + DW_Strings.symbol_underscore + sDisabilitySevere;
        aS = TableValues.get(key);
        aS += DW_Strings.special_commaSpace + DS1;

        TableValues.put(key, aS);
        // Disability Enhanced
        key = ClaimRef + DW_Strings.symbol_underscore + sDisabilityEnhanced;
        aS = TableValues.get(key);
        aS += DW_Strings.special_commaSpace + DE1;

        TableValues.put(key, aS);
        // Child Disability
        key = ClaimRef + DW_Strings.symbol_underscore + sDisabledChild;
        aS = TableValues.get(key);
        aS += DW_Strings.special_commaSpace + DC1;

        TableValues.put(key, aS);
        // Partner Death
        key = ClaimRef + DW_Strings.symbol_underscore + sPDeath;
        aS = TableValues.get(key);

        if (PDD1 == null) {
            aS += DW_Strings.special_commaSpace;
        } else if (PDD1.equalsIgnoreCase(PDD0)) {
            aS += DW_Strings.special_commaSpace;
        } else if (PDD1.isEmpty()) {
            aS += DW_Strings.special_commaSpace;
        } else {
            aS += DW_Strings.special_commaSpace + sPDeath + DW_Strings.symbol_underscore + PDD1;
        }

        TableValues.put(key, aS);

        // HBDP
        BigDecimal bd;
        key = ClaimRef + DW_Strings.symbol_underscore + sTotal_DHP;
        bd = AggregateStatistics.get(key);
        bd = bd.add(BigDecimal.valueOf(HBDP1));

        AggregateStatistics.put(key, bd);
        if (HBDP1
                > 0) {
            DHPAtSomePointClaimIDs.add(ClaimID);
            key = ClaimRef + DW_Strings.symbol_underscore + sTotalCount_DHP;
            bd = AggregateStatistics.get(key);
            bd = bd.add(BigDecimal.ONE);
            AggregateStatistics.put(key, bd);
        }

//        // CTBDP
//        key = aClaimRef + symbol_underscore + sCTBDP;
//        aS = tableValues.get(key);
//        if (aCTBDP > 0) {
//            aS += special_commaSpace + sCTBDP + symbol_underscore + aCTBDP;
//        } else {
//            aS += special_commaSpace;
//        }
//        tableValues.put(key, aS);
        // Arrears
        key = ClaimRef + DW_Strings.symbol_underscore + sA;
        aS = TableValues.get(key);

        if (CouncilUOSet1.getMap().keySet().contains(ClaimID)) {
            DW_UO_Record UORec;
            UORec = CouncilUOSet1.getMap().get(ClaimID);
            if (UORec == null) {
                aS += DW_Strings.special_commaSpace;
            } else {
                Arrears1 = UORec.getTotalRentArrears();
                if (Arrears1 == null) {
                    aS += DW_Strings.special_commaSpace;
                } else {
                    Double arrearsD;
                    arrearsD = arrears.get(YM31);
                    if (arrearsD == null) {
                        arrearsD = 0.0d;
                    }
                    arrearsD += Arrears1;
                    arrears.put(YM31, arrearsD);
                    Double arrearsCountD;
                    arrearsCountD = arrearsCounts.get(YM31);
                    if (arrearsCountD == null) {
                        arrearsCountD = 0.0d;
                    }
                    arrearsCountD++;
                    arrearsCounts.put(YM31, arrearsCountD);
                    if (CouncilUOSet0 != null) {
                        DW_UO_Record bUORec;
                        bUORec = CouncilUOSet0.getMap().get(ClaimID);
                        if (bUORec != null) {
                            Double arrearsDiff = arrearsDiffs.get(YM31);
                            if (arrearsDiff == null) {
                                arrearsDiff = 0.0d;
                            }
                            Double arrearsDiffCount = arrearsDiffCounts.get(YM31);
                            if (arrearsDiffCount == null) {
                                arrearsDiffCount = 0.0d;
                            }
                            Arrears0 = bUORec.getTotalRentArrears();
                            if (Arrears0 != null) {
                                arrearsDiff += Arrears1 - Arrears0;
                                arrearsDiffs.put(YM31, arrearsDiff);
                                arrearsDiffCount++;
                                arrearsDiffCounts.put(YM31, arrearsDiffCount);
                            }
                        }
                    }
                    aS += DW_Strings.special_commaSpace + Arrears1;
                    key = ClaimRef + DW_Strings.symbol_underscore + sMax_Arrears;
                    bd = AggregateStatistics.get(key);
                    bd = bd.max(BigDecimal.valueOf(Arrears1));
                    AggregateStatistics.put(key, bd);
                    if (Arrears1 > 0) {
                        InArrearsAtSomePointClaimIDs.add(ClaimID);
                        key = ClaimRef + DW_Strings.symbol_underscore + sTotalCount_InArrears;
                        bd = AggregateStatistics.get(key);
                        bd = bd.add(BigDecimal.ONE);
                        AggregateStatistics.put(key, bd);
                        if (Arrears1 < 10) {
                            key = ClaimRef + DW_Strings.symbol_underscore + sTotalCount_InArrears0To10;
                        } else if (Arrears1 < 100) {
                            key = ClaimRef + DW_Strings.symbol_underscore + sTotalCount_InArrears10To100;
                        } else if (Arrears1 < 500) {
                            key = ClaimRef + DW_Strings.symbol_underscore + sTotalCount_InArrears100To500;
                        } else {
                            key = ClaimRef + DW_Strings.symbol_underscore + sTotalCount_InArrearsOver500;
                        }
                        bd = AggregateStatistics.get(key);
                        bd = bd.add(BigDecimal.ONE);
                        AggregateStatistics.put(key, bd);
                    }
                    if (HBDP1 > 0) {
                        if (Arrears1 > 0) {
                            UOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePointClaimIDs.add(ClaimID);
                        }
                    }
                    if (Arrears1 > 0) {
                        UOTT1ClaimsInRentArrearsAtSomePointClaimIDs.add(ClaimID);
                        if (Arrears1 > 500) {
                            UOTT1ClaimsInRentArrearsOver500AtSomePointClaimIDs.add(ClaimID);
                        }
                    }
                    if (UO1) {
                        if (TT1 == 1 && TT0 == 4) {
                            //UOTT4OrTT4_To_UOTT1.add(aClaimRef);
                            //if (aArrears > 0) {
                            //    UOTT4OrTT4_To_UOTT1InArrears.add(aClaimRef);
                            //    if (aHBDP > 0) {
                            //        UOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP.add(aClaimRef);
                            //    }
                            //}
                            if (UO0) {
                                UOTT4_To_UOTT1ClaimIDs.add(ClaimID);
                                if (HBDP1 > 0) {
                                    UOTT4_To_UOTT1GettingDHPClaimIDs.add(ClaimID);
                                }
                                if (Arrears1 > 0) {
                                    UOTT4_To_UOTT1InArrearsClaimIDs.add(ClaimID);
                                    if (HBDP1 > 0) {
                                        UOTT4_To_UOTT1InArrearsAndGettingDHPClaimIDs.add(ClaimID);
                                    }
                                }
                            } else {
                                TT4_To_UOTT1ClaimIDs.add(ClaimID);
                                if (HBDP1 > 0) {
                                    TT4_To_UOTT1GettingDHPClaimIDs.add(ClaimID);
                                }
                                if (Arrears1 > 0) {
                                    TT4_To_UOTT1InArrearsClaimIDs.add(ClaimID);
                                    if (HBDP1 > 0) {
                                        TT4_To_UOTT1InArrearsAndGettingDHPClaimIDs.add(ClaimID);
                                    }
                                }
                            }
                        }
                    }
                }
                key = ClaimRef + DW_Strings.symbol_underscore + sA;
                TableValues.put(key, aS);
            }
        } else {
            aS += DW_Strings.special_commaSpace;
        }

        if (UOTT1_To_TT1_PostcodeChangedClaimIDs.contains(ClaimID)) {
            if (Record0 != null && Record1 != null) {
                if (!(PC0.equalsIgnoreCase(PC1))
                        && (Record0.isClaimPostcodeFMappable() && Record1.isClaimPostcodeFMappable())) {
                    if (UO1) {
                        UOTT1_To_TT1_PostcodeChangedClaimIDs.remove(ClaimID);
                        if (TT1 == 1) {
                            UOTT1_To_UOTT1_PostcodeChangedClaimIDs.add(ClaimID);
                        } else if (TT1 == 4) {
                            UOTT1_To_UOTT4_PostcodeChangedClaimIDs.add(ClaimID);
                        }
                    }
                }
            }
        }

        if (UOTT4_To_TT4_PostcodeChangedClaimIDs.contains(ClaimID)) {
            if (Record0 != null && Record1 != null) {
                if (!(PC0.equalsIgnoreCase(PC1))
                        && (Record0.isClaimPostcodeFMappable() && Record1.isClaimPostcodeFMappable())) {
                    if (UO1) {
                        UOTT4_To_TT4_PostcodeChangedClaimIDs.remove(ClaimID);
                        if (TT1 == 1) {
                            UOTT4_To_UOTT1_PostcodeChangedClaimIDs.add(ClaimID);
                        } else if (TT1 == 4) {
                            UOTT4_To_UOTT4_PostcodeChangedClaimIDs.add(ClaimID);
                        }
                    }
                }
            }
        }

        if (UOTT1_To_TT3OrTT6ClaimIDs.contains(ClaimID)) {
            if (TT1 == 1) {
                UOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePointClaimIDs.add(ClaimID);
            }
        }

        if (UOTT4_To_TT3OrTT6ClaimIDs.contains(ClaimID)) {
            if (TT1 == 4) {
                UOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePointClaimIDs.add(ClaimID);
            }
        }

        TableValues.put(key, aS);
        return result;
    }

    private void doX(
            SHBE_ClaimID ClaimID,
            //int HBDP1,
            int TT1,
            boolean UO00,
            boolean UO0,
            boolean UO1,
            int Status0,
            HashSet<SHBE_ClaimID> UO_To_LeftSHBE_NotReturnedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_NotReturnedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT1ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT4ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT8ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT1_To_LeftSHBE_ReturnedAsTT9ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_NotReturnedClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT1ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT4ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT8ClaimIDs,
            HashSet<SHBE_ClaimID> UOTT4_To_LeftSHBE_ReturnedAsTT9ClaimIDs
    //            ,
    //            HashSet<SHBE_ClaimID> UOTT1_To_UOTT4ClaimIDs,
    //            HashSet<SHBE_ClaimID> UOTT1_To_TT4ClaimIDs,
    //            HashSet<SHBE_ClaimID> TT1_To_UOTT4ClaimIDs,
    //            HashSet<SHBE_ClaimID> UOTT4_To_UOTT1ClaimIDs,
    //            HashSet<SHBE_ClaimID> UOTT4_To_TT1ClaimIDs,
    //            HashSet<SHBE_ClaimID> TT4_To_UOTT1ClaimIDs,
    //            HashSet<SHBE_ClaimID> TT1_To_UOTT1GettingDHPClaimIDs,
    //            HashSet<SHBE_ClaimID> TT1_To_UOTT4GettingDHPClaimIDs,
    //            HashSet<SHBE_ClaimID> TT4_To_UOTT1GettingDHPClaimIDs,
    //            HashSet<SHBE_ClaimID> TT4_To_UOTT4GettingDHPClaimIDs
    ) {
        if (UOTT1_To_LeftSHBE_NotReturnedClaimIDs.contains(ClaimID)) {
            UO_To_LeftSHBE_NotReturnedClaimIDs.remove(ClaimID);
            UOTT1_To_LeftSHBE_NotReturnedClaimIDs.remove(ClaimID);
            if (TT1 == 1) {
                if (UO1) {
                    UOTT1_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs.add(ClaimID);
                } else if (Status0 != 1 && UO00) { // Not sure this covers everything! TT0 might be -999?
                    UOTT1_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs.add(ClaimID);
                } else {
                    UOTT1_To_LeftSHBE_ReturnedAsTT1ClaimIDs.add(ClaimID);
                }
            } else if (TT1 == 3 || TT1 == 6) {
//                if (UO1) {
//                   UOTT1_To_LeftSHBE_ReturnedAsUOTT3OrTT6ClaimIDs.add(ClaimID);
//                } else {
//                   UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs.add(ClaimID);
//                }                
                UOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs.add(ClaimID);
            } else if (TT1 == 4) {
                if (UO1) {
                    UOTT1_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs.add(ClaimID);
                } else {
                    UOTT1_To_LeftSHBE_ReturnedAsTT4ClaimIDs.add(ClaimID);
                }
            } else if (TT1 == 5 || TT1 == 7) {
                UOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs.add(ClaimID);
            } else if (TT1 == 8) {
                UOTT1_To_LeftSHBE_ReturnedAsTT8ClaimIDs.add(ClaimID);
            } else if (TT1 == 9) {
                UOTT1_To_LeftSHBE_ReturnedAsTT9ClaimIDs.add(ClaimID);
            }
        } else if (UOTT4_To_LeftSHBE_NotReturnedClaimIDs.contains(ClaimID)) {
            UO_To_LeftSHBE_NotReturnedClaimIDs.remove(ClaimID);
            UOTT4_To_LeftSHBE_NotReturnedClaimIDs.remove(ClaimID);
            if (TT1 == 1) {
                if (UO1) {
                    UOTT4_To_LeftSHBE_ReturnedAsUOTT1ClaimIDs.add(ClaimID);
                } else {
                    UOTT4_To_LeftSHBE_ReturnedAsTT1ClaimIDs.add(ClaimID);
                }
            } else if (TT1 == 3 || TT1 == 6) {
//                if (UO1) {
//                   UOTT4_To_LeftSHBE_ReturnedAsUOTT3OrTT6ClaimIDs.add(ClaimID);
//                } else {
//                   UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs.add(ClaimID);
//                }                
                UOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6ClaimIDs.add(ClaimID);
            } else if (TT1 == 4) {
                if (UO1) {
                    UOTT4_To_LeftSHBE_ReturnedAsUOTT4ClaimIDs.add(ClaimID);
                } else {
                    UOTT4_To_LeftSHBE_ReturnedAsTT4ClaimIDs.add(ClaimID);
                }
            } else if (TT1 == 5 || TT1 == 7) {
                UOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7ClaimIDs.add(ClaimID);
            } else if (TT1 == 8) {
                UOTT4_To_LeftSHBE_ReturnedAsTT8ClaimIDs.add(ClaimID);
            } else if (TT1 == 9) {
                UOTT4_To_LeftSHBE_ReturnedAsTT9ClaimIDs.add(ClaimID);
            }
        }
    }

    /**
     * Already in the context from where this is called, it is known that aS
     * prior to adding aPC at the end contained another instance of aPC.
     *
     * @param aS
     * @param aPC
     * @return
     */
    protected boolean getLikelyTraveller(
            String aS,
            String aPC) {
        boolean result;
        result = false;
        String[] split;
        split = aS.split(DW_Strings.symbol_comma);
        boolean firstIsTheSame = false;
        String s1;
        for (int i = split.length - 2; i > -1; i--) { // We don't go from the very end as we already added aPC to the end!
            s1 = split[i].trim();
            if (!s1.isEmpty()) {
                if (!(s1.equalsIgnoreCase(defaultPostcode))) {
                    if (s1.equalsIgnoreCase(aPC)) {
                        firstIsTheSame = true;
                    } else if (!firstIsTheSame) {
                        return true;
                    }
                }
            }
        }
        return result;
    }

    public boolean getContainsAnotherPostcode(String aS, String aPC) {
        boolean result;
        result = false;
        String[] split;
        split = aS.split(DW_Strings.symbol_comma);
        String s1;
        for (int i = 0; i < split.length; i++) {
            //for (String split1 : split) {
            //    s1 = split1.trim();
            s1 = split[i];
            if (!s1.isEmpty()) {
                if (!(s1.equalsIgnoreCase(defaultPostcode))) {
                    if (!(s1.equalsIgnoreCase(aPC))) {
                        if (ValidPostcodes.contains(s1)) {
                            return true;
                        }
                    }
                }
            }
        }
        return result;
    }

    public boolean isAnotherTT(int aTT, String aS) {
        boolean result = false;
        switch (aTT) {
            case 1:
                if (aS.contains("TT_2")
                        || aS.contains("TT_3")
                        || aS.contains("TT_4")
                        || aS.contains("TT_5")
                        || aS.contains("TT_6")
                        || aS.contains("TT_7")
                        || aS.contains("TT_8")
                        || aS.contains("TT_9")) {
                    return true;
                }
                break;
            case 2:
                if (aS.contains("TT_1")
                        || aS.contains("TT_3")
                        || aS.contains("TT_4")
                        || aS.contains("TT_5")
                        || aS.contains("TT_6")
                        || aS.contains("TT_7")
                        || aS.contains("TT_8")
                        || aS.contains("TT_9")) {
                    return true;
                }
                break;
            case 3:
                if (aS.contains("TT_1")
                        || aS.contains("TT_2")
                        || aS.contains("TT_4")
                        || aS.contains("TT_5")
                        || aS.contains("TT_6")
                        || aS.contains("TT_7")
                        || aS.contains("TT_8")
                        || aS.contains("TT_9")) {
                    return true;
                }
                break;
            case 4:
                if (aS.contains("TT_1")
                        || aS.contains("TT_2")
                        || aS.contains("TT_3")
                        || aS.contains("TT_5")
                        || aS.contains("TT_6")
                        || aS.contains("TT_7")
                        || aS.contains("TT_8")
                        || aS.contains("TT_9")) {
                    return true;
                }
                break;
            case 5:
                if (aS.contains("TT_1")
                        || aS.contains("TT_2")
                        || aS.contains("TT_3")
                        || aS.contains("TT_4")
                        || aS.contains("TT_6")
                        || aS.contains("TT_7")
                        || aS.contains("TT_8")
                        || aS.contains("TT_9")) {
                    return true;
                }
                break;
            case 6:
                if (aS.contains("TT_1")
                        || aS.contains("TT_2")
                        || aS.contains("TT_3")
                        || aS.contains("TT_4")
                        || aS.contains("TT_5")
                        || aS.contains("TT_7")
                        || aS.contains("TT_8")
                        || aS.contains("TT_9")) {
                    return true;
                }
                break;
            case 7:
                if (aS.contains("TT_1")
                        || aS.contains("TT_2")
                        || aS.contains("TT_3")
                        || aS.contains("TT_4")
                        || aS.contains("TT_5")
                        || aS.contains("TT_6")
                        || aS.contains("TT_8")
                        || aS.contains("TT_9")) {
                    return true;
                }
                break;
            case 8:
                if (aS.contains("TT_1")
                        || aS.contains("TT_2")
                        || aS.contains("TT_3")
                        || aS.contains("TT_4")
                        || aS.contains("TT_5")
                        || aS.contains("TT_6")
                        || aS.contains("TT_7")
                        || aS.contains("TT_9")) {
                    return true;
                }
                break;
            case 9:
                if (aS.contains("TT_1")
                        || aS.contains("TT_2")
                        || aS.contains("TT_3")
                        || aS.contains("TT_4")
                        || aS.contains("TT_5")
                        || aS.contains("TT_6")
                        || aS.contains("TT_7")
                        || aS.contains("TT_8")) {
                    return true;
                }
                break;
        }
        return result;
    }

    /**
     * This returns the Claims of the UnderOccupying claims in the first include
     * period.
     *
     * @TODO For overall computational efficiency, this should probably only be
     * calculated once for each period and stored in a file then re-read from
     * file when needed.
     * @param councilUnderOccupiedSets
     * @param RSLUnderOccupiedSets
     * @param SHBEFilenames
     * @param include
     * @return
     */
    public HashSet<SHBE_ClaimID>[] getStartUOClaimIDs(
            TreeMap<UKP_YM3, DW_UO_Set> councilUnderOccupiedSets,
            TreeMap<UKP_YM3, DW_UO_Set> RSLUnderOccupiedSets,
            String[] SHBEFilenames,
            ArrayList<Integer> include
    ) {
        HashSet<SHBE_ClaimID>[] result;
        result = new HashSet[2];
        result[0] = new HashSet<>();
        result[1] = new HashSet<>();
        UKP_YM3 yM31 ;
        DW_UO_Set councilUnderOccupiedSet1;
        DW_UO_Set RSLUnderOccupiedSet1 ;
        String filename1 ;
        Iterator<Integer> includeIte;
        includeIte = include.iterator();
        int i;
        i = includeIte.next();
        filename1 = SHBEFilenames[i];
        yM31 = shbeHandler.getYM3(filename1);
        councilUnderOccupiedSet1 = councilUnderOccupiedSets.get(yM31);
        if (councilUnderOccupiedSet1 != null) {
            RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(yM31);
            // Add to result
            result[0].addAll(councilUnderOccupiedSet1.getMap().keySet());
            result[1].addAll(RSLUnderOccupiedSet1.getMap().keySet());
        }
        return result;
    }

    /**
     * This returns the Claims of the UnderOccupying claims in the last include
     * period.
     *
     * @TODO For overall computational efficiency, this should probably only be
     * calculated once for each period and stored in a file then re-read from
     * file when needed.
     * @param CouncilUnderOccupiedSets
     * @param RSLUnderOccupiedSets
     * @param SHBEFilenames
     * @param include
     * @return
     */
    public HashSet<SHBE_ClaimID>[] getEndUOClaimIDs(
            TreeMap<UKP_YM3, DW_UO_Set> CouncilUnderOccupiedSets,
            TreeMap<UKP_YM3, DW_UO_Set> RSLUnderOccupiedSets,
            String[] SHBEFilenames,
            ArrayList<Integer> include
    ) {
        HashSet<SHBE_ClaimID>[] result;
        result = new HashSet[2];
        result[0] = new HashSet<>();
        result[1] = new HashSet<>();
        UKP_YM3 yM31 ;
        DW_UO_Set councilUnderOccupiedSet1 ;
        DW_UO_Set RSLUnderOccupiedSet1 ;
        String filename1 ;
        Iterator<Integer> includeIte;
        includeIte = include.iterator();
        int i;
        i = includeIte.next();
        while (includeIte.hasNext()) {
            i = includeIte.next();
        }
        filename1 = SHBEFilenames[i];
        yM31 = shbeHandler.getYM3(filename1);
        councilUnderOccupiedSet1 = CouncilUnderOccupiedSets.get(yM31);
        if (councilUnderOccupiedSet1 != null) {
            RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(yM31);
            // Add to result
            result[0].addAll(councilUnderOccupiedSet1.getMap().keySet());
            result[1].addAll(RSLUnderOccupiedSet1.getMap().keySet());
        }
        return result;
    }

    public HashSet<SHBE_ClaimID>[] getUOClaimIDs(
            TreeMap<UKP_YM3, DW_UO_Set> CouncilUnderOccupiedSets,
            TreeMap<UKP_YM3, DW_UO_Set> RSLUnderOccupiedSets,
            String[] SHBEFilenames,
            ArrayList<Integer> include
    ) {
        HashSet<SHBE_ClaimID>[] result;
        result = new HashSet[2];
        result[0] = new HashSet<>();
        result[1] = new HashSet<>();
        UKP_YM3 yM31;
        DW_UO_Set CouncilUnderOccupiedSet1;
        DW_UO_Set RSLUnderOccupiedSet1;
        String filename1;
        Iterator<Integer> includeIte;
        includeIte = include.iterator();
        int i;
        while (includeIte.hasNext()) {
            i = includeIte.next();
            filename1 = SHBEFilenames[i];
            yM31 = shbeHandler.getYM3(filename1);
            CouncilUnderOccupiedSet1 = CouncilUnderOccupiedSets.get(yM31);
            if (CouncilUnderOccupiedSet1 != null) {
                RSLUnderOccupiedSet1 = RSLUnderOccupiedSets.get(yM31);
                // Add to result
                result[0].addAll(CouncilUnderOccupiedSet1.getMap().keySet());
                result[1].addAll(RSLUnderOccupiedSet1.getMap().keySet());
            }
        }
        return result;
    }

    public ArrayList<String> getKeys(String ClaimRef) {
        ArrayList<String> r;
        r = new ArrayList<>();
        String s_ = DW_Strings.symbol_underscore;
        // TenancyType
        r.add(ClaimRef + s_ + sTT);
        // UnderOccupancy
        r.add(ClaimRef + s_ + sUnderOccupancy);
        // Postcode
        r.add(ClaimRef + s_ + sP);
        // WeeklyHousingBenefitEntitlement
        r.add(ClaimRef + s_ + sWHBE);
        // WeeklyHousingBenefitEntitlement
        r.add(ClaimRef + s_ + sWERA);
        // PassportedStandardIndicator
        r.add(ClaimRef + s_ + sPSI);
        // StatusOfHBClaim
        r.add(ClaimRef + s_ + sSHBC);
        // ReasonThatHBClaimClosed
        r.add(ClaimRef + s_ + sRTHBCC);
        // ClaimantEthnicGroup
        r.add(ClaimRef + s_ + sCEG);
        // Arrears
        r.add(ClaimRef + s_ + sA);
        // HB DiscretionaryPayment
        r.add(ClaimRef + s_ + sHBDP);
        // Disability
        r.add(ClaimRef + s_ + sDisability);
        // Disability Premium
        r.add(ClaimRef + s_ + sDisabilityPremium);
        // Disability Severe
        r.add(ClaimRef + s_ + sDisabilitySevere);
        // Disability Enhanced
        r.add(ClaimRef + s_ + sDisabilityEnhanced);
        // Child Disability
        r.add(ClaimRef + s_ + sDisabledChild);
        // Partner Death
        r.add(ClaimRef + s_ + sPDeath);
        // Household Size
        r.add(ClaimRef + s_ + sHS);
        // NonDependents
        r.add(ClaimRef + s_ + sND);
        // ChildDependents
        r.add(ClaimRef + s_ + sCD);
        // NonDependents (UO)
        r.add(ClaimRef + s_ + sNDUO);
        // Children 16 +
        r.add(ClaimRef + s_ + sCO16);
        // FemaleChildrenUnder10
        r.add(ClaimRef + s_ + sFCU10);
        // MaleChildrenUnder10
        r.add(ClaimRef + s_ + sMCU10);
        // FemaleChildren10to16
        r.add(ClaimRef + s_ + sFC10To16);
        // MaleChildren10to16
        r.add(ClaimRef + s_ + sMC10To16);
        // Number of Bedrooms
        r.add(ClaimRef + s_ + sNB);
        // Bedroom Requirement
        r.add(ClaimRef + s_ + sBR);
        // Claimants DoB
        r.add(ClaimRef + s_ + sCDoB);
        // Claimants Age
        r.add(ClaimRef + s_ + sCA);
        // Claimants Gender
        r.add(ClaimRef + s_ + sCG);
        // Partners DoB
        r.add(ClaimRef + s_ + sPDoB);
        // Partners Age
        r.add(ClaimRef + s_ + sPA);
        // Partners Gender
        r.add(ClaimRef + s_ + sPG);
        return r;
    }

    private void writeLine(
            String generalStatistic,
            TreeMap<String, BigDecimal> generalStatistics,
            HashMap<String, String> generalStatisticsDescriptions,
            PrintWriter pw) {
        String line;
        line = generalStatistic + DW_Strings.special_commaSpace
                + generalStatistics.get(generalStatistic) + DW_Strings.special_commaSpace
                + generalStatisticsDescriptions.get(generalStatistic);
        pw.println(line);
    }

    /**
     *
     * @param table table[0] = header; table[1] = TableValues; table[2] =
     * ClaimDs; table[3] = Groups; table[4] = preUnderOccupancyValues; table[5]
     * = AggregateStatistics; table[6] = GeneralStatistics; table[7] =
     * TimeStatistics;
     * @param includePreUnderOccupancyValues
     * @param startMonth
     * @param startYear
     * @param endMonth
     * @param endYear
     */
    public void writeTenancyChangeTables(
            Object[] table,
            boolean includePreUnderOccupancyValues,
            String startMonth,
            String startYear,
            String endMonth,
            String endYear
    ) throws IOException {
        env.ge.log("<WriteTenancyChangeTables>", true);
        String Header;
        Header = (String) table[0];
        TreeMap<String, String> tableValues;
        tableValues = (TreeMap<String, String>) table[1];
        HashSet<SHBE_ClaimID> ClaimIDs;
        ClaimIDs = (HashSet<SHBE_ClaimID>) table[2];
        HashMap<String, HashSet<SHBE_ClaimID>> groups;
        groups = (HashMap<String, HashSet<SHBE_ClaimID>>) table[3];
        TreeMap<String, String> preUnderOccupancyValues;
        preUnderOccupancyValues = (TreeMap<String, String>) table[4];
        TreeMap<String, BigDecimal> aggregateStatistics;
        aggregateStatistics = (TreeMap<String, BigDecimal>) table[5];
        TreeMap<String, BigDecimal> generalStatistics;
        generalStatistics = (TreeMap<String, BigDecimal>) table[6];
        TreeMap<String, TreeMap<String, Integer>> TimeStatistics;
        TimeStatistics = (TreeMap<String, TreeMap<String, Integer>>) table[7];
        HashMap<String, Double> arrears;
        arrears = (HashMap<String, Double>) table[8];
        HashMap<String, Double> arrearsCounts;
        arrearsCounts = (HashMap<String, Double>) table[9];
        HashMap<String, Double> arrearsDiffs;
        arrearsDiffs = (HashMap<String, Double>) table[10];
        HashMap<String, Double> arrearsDiffCounts;
        arrearsDiffCounts = (HashMap<String, Double>) table[11];

        String dirName;
        dirName = startMonth + startYear + "_To_" + endMonth + endYear;

        env.ge.log("<WriteTimeStatistics>", true);
        String s2 = DW_Strings.special_commaSpace;
        PrintWriter pw5;
        pw5 = getPrintWriter("__TimeStatistics", dirName);
        pw5.println("Date, " + sTotalCount_CumulativeUniqueClaims
                + s2 + sTotalCount_UOClaims
                + s2 + sTotalCount_UOCouncilClaims
                + s2 + sTotalCount_UORSLClaims
                + s2 + sTotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE
                + s2 + sTotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO
                + s2 + sAverageHouseholdSizeCouncilSHBE
                + s2 + sAverageHouseholdSizeCouncilUO
                + s2 + sAverageHouseholdSizeRSLSHBE
                + s2 + sAverageHouseholdSizeRSLUO
                + s2 + "TotalCouncilRentArrears"
                + s2 + "AverageCouncilRentArrears"
                + s2 + "TotalCouncilRentArrearsDiff"
                + s2 + "AverageCouncilRentArrearsDiff");
        String date;
        int cumulativeCount;
        TreeMap<String, Integer> totalCounts_cumulativeUniqueClaims;
        totalCounts_cumulativeUniqueClaims = TimeStatistics.get(sTotalCount_CumulativeUniqueClaims);
        int UOCount;
        int UOCouncilCount;
        int UORSLCount;
        TreeMap<String, Integer> totalCounts_UOClaims;
        totalCounts_UOClaims = TimeStatistics.get(
                sTotalCount_UOClaims);
        TreeMap<String, Integer> totalCounts_UOCouncilClaims;
        totalCounts_UOCouncilClaims = TimeStatistics.get(
                sTotalCount_UOCouncilClaims);
        TreeMap<String, Integer> totalCounts_UORSLClaims;
        totalCounts_UORSLClaims = TimeStatistics.get(
                sTotalCount_UORSLClaims);
        int UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE;
        int UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO;

        //int totalHouseholdSizeExcludingPartnersCouncilSHBE;
        //int totalHouseholdSizeExcludingPartnersCouncilUO;
        //int totalHouseholdSizeExcludingPartnersRSLSHBE;
        //int totalHouseholdSizeExcludingPartnersRSLUO;
        int totalHouseholdSizeCouncilSHBE;
        int totalHouseholdSizeCouncilUO;
        int totalHouseholdSizeRSLSHBE;
        int totalHouseholdSizeRSLUO;
        double averageHouseholdSizeCouncilSHBE;
        double averageHouseholdSizeCouncilUO;
        double averageHouseholdSizeRSLSHBE;
        double averageHouseholdSizeRSLUO;

        double arrearsD;
        double arrearsCount;
        double averageArrears;
        double arrearsDiff;
        double arrearsDiffCount;
        double averageArrearsDiff;

        TreeMap<String, Integer> totalCounts_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE;
        totalCounts_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE = TimeStatistics.get(sTotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE);
        TreeMap<String, Integer> totalCounts_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO;
        totalCounts_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO = TimeStatistics.get(sTotalCount_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO);

//        TreeMap<String, Integer> TotalHouseholdSizeExcludingPartnersCouncilSHBEs;
//        TotalHouseholdSizeExcludingPartnersCouncilSHBEs = TimeStatistics.get(
//                sTotalHouseholdSizeExcludingPartnersCouncilSHBE);
//        TreeMap<String, Integer> TotalHouseholdSizeExcludingPartnersCouncilUOs;
//        TotalHouseholdSizeExcludingPartnersCouncilUOs = TimeStatistics.get(
//                sTotalHouseholdSizeExcludingPartnersCouncilUO);
//        TreeMap<String, Integer> TotalHouseholdSizeExcludingPartnersRSLSHBEs;
//        TotalHouseholdSizeExcludingPartnersRSLSHBEs = TimeStatistics.get(
//                sTotalHouseholdSizeExcludingPartnersRSLSHBE);
//        TreeMap<String, Integer> TotalHouseholdSizeExcludingPartnersRSLUOs;
//        TotalHouseholdSizeExcludingPartnersRSLUOs = TimeStatistics.get(
//                sTotalHouseholdSizeExcludingPartnersRSLUO);
        TreeMap<String, Integer> TotalHouseholdSizeCouncilSHBEs;
        TotalHouseholdSizeCouncilSHBEs = TimeStatistics.get(
                sTotalHouseholdSizeCouncilSHBE);
        TreeMap<String, Integer> TotalHouseholdSizeCouncilUOs;
        TotalHouseholdSizeCouncilUOs = TimeStatistics.get(
                sTotalHouseholdSizeCouncilUO);
        TreeMap<String, Integer> TotalHouseholdSizeRSLSHBEs;
        TotalHouseholdSizeRSLSHBEs = TimeStatistics.get(
                sTotalHouseholdSizeRSLSHBE);
        TreeMap<String, Integer> TotalHouseholdSizeRSLUOs;
        TotalHouseholdSizeRSLUOs = TimeStatistics.get(
                sTotalHouseholdSizeRSLUO);

        Iterator<String> ite;
        ite = totalCounts_cumulativeUniqueClaims.keySet().iterator();
        while (ite.hasNext()) {
            date = ite.next();
            if (date == null) {
                System.out.println("Oh dear, date is null!");
            } else if (date.equalsIgnoreCase("null")) {
                System.out.println("Oh dear, date is \"null\"!");
            } else {
                System.out.println("date " + date);
                cumulativeCount = totalCounts_cumulativeUniqueClaims.get(date);
                UOCount = totalCounts_UOClaims.get(date);
                UOCouncilCount = totalCounts_UOCouncilClaims.get(date);
                UORSLCount = totalCounts_UORSLClaims.get(date);
                UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE = totalCounts_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE.get(date);
                UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO = totalCounts_UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO.get(date);
                //totalHouseholdSizeExcludingPartnersCouncilSHBE = TotalHouseholdSizeExcludingPartnersCouncilSHBEs.get(date);
                //totalHouseholdSizeExcludingPartnersCouncilUO = TotalHouseholdSizeExcludingPartnersCouncilUOs.get(date);
                //totalHouseholdSizeExcludingPartnersRSLSHBE = TotalHouseholdSizeExcludingPartnersRSLSHBEs.get(date);
                //totalHouseholdSizeExcludingPartnersRSLUO = TotalHouseholdSizeExcludingPartnersRSLUOs.get(date);
                totalHouseholdSizeCouncilSHBE = TotalHouseholdSizeCouncilSHBEs.get(date);
                totalHouseholdSizeCouncilUO = TotalHouseholdSizeCouncilUOs.get(date);
                totalHouseholdSizeRSLSHBE = TotalHouseholdSizeRSLSHBEs.get(date);
                totalHouseholdSizeRSLUO = TotalHouseholdSizeRSLUOs.get(date);
                averageHouseholdSizeCouncilSHBE = totalHouseholdSizeCouncilSHBE / (double) UOCouncilCount;
                averageHouseholdSizeCouncilUO = totalHouseholdSizeCouncilUO / (double) UOCouncilCount;
                averageHouseholdSizeRSLSHBE = totalHouseholdSizeRSLSHBE / (double) UORSLCount;
                averageHouseholdSizeRSLUO = totalHouseholdSizeRSLUO / (double) UORSLCount;

                String YM3;
                YM3 = shbeHandler.getYM3FromYearMonthNumber(date);

                System.out.println("YM3 " + YM3);

                arrearsD = 0;

                if (!arrears.containsKey(YM3)) {
                    System.out.println("arrears does not contain YM3 " + YM3);
                } else {
                    arrearsCount = arrearsCounts.get(YM3);
                    averageArrears = 0.0d;
                    if (arrearsCount > 0) {
                        averageArrears = arrearsD / arrearsCount;
                    }

                    if (arrearsDiffs != null) {
                        Double d;
                        d = arrearsDiffs.get(YM3);
                        if (d != null) {
                            arrearsDiff = d;
                            arrearsDiffCount = arrearsDiffCounts.get(YM3);
                            averageArrearsDiff = 0.0d;
                            if (arrearsDiffCount > 0) {
                                averageArrearsDiff = arrearsDiff / arrearsDiffCount;
                            }
                        } else {
                            arrearsDiff = 0.0d;
                            averageArrearsDiff = 0.0d;
                        }
                    } else {
                        arrearsDiff = 0.0d;
                        averageArrearsDiff = 0.0d;
                    }

                    pw5.println(date + s2 + Integer.toString(cumulativeCount)
                            + s2 + Integer.toString(UOCount)
                            + s2 + Integer.toString(UOCouncilCount)
                            + s2 + Integer.toString(UORSLCount)
                            + s2 + Integer.toString(UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsSHBE)
                            + s2 + Integer.toString(UOClaimsInHouseholdsWithHouseholdSizeExcludingPartnersGreaterThanOrEqualToNumberOfBedroomsUO)
                            + s2 + Double.toString(averageHouseholdSizeCouncilSHBE)
                            + s2 + Double.toString(averageHouseholdSizeCouncilUO)
                            + s2 + Double.toString(averageHouseholdSizeRSLSHBE)
                            + s2 + Double.toString(averageHouseholdSizeRSLUO)
                            + s2 + Double.toString(arrearsD)
                            + s2 + Double.toString(averageArrears)
                            + s2 + Double.toString(arrearsDiff)
                            + s2 + Double.toString(averageArrearsDiff)
                    );
                }
            }
        }
        pw5.close();
        env.ge.log("</WriteTimeStatistics>", true);

        TreeMap<String, String> GroupNameDescriptions;
        GroupNameDescriptions = getGroupNameDescriptions(groups.keySet());

        String AggregateStatisticsHeader;
        //aggregateStatisticsHeader = "ClaimRef, DHP_Total, Housing Benefit Loss as a Result of UnderOccupancy, Max_Arrears, NumberOfUnderOccupancyMonths";
        AggregateStatisticsHeader = "ClaimRef " + s2
                + sTotal_DHP + s2
                + sTotalCount_DHP + s2
                + sTotal_HBLossDueToUO + s2
                + sTotalCount_HBLossDueToUO + s2
                + sMax_Arrears + s2
                + sTotalCount_InArrears + s2
                + sTotalCount_InArrears0To10 + s2
                + sTotalCount_InArrears10To100 + s2
                + sTotalCount_InArrears100To500 + s2
                + sTotalCount_InArrearsOver500 + s2
                + sTotalCount_UnderOccupancy;

        env.ge.log("<WriteGeneralStatistics>", true);
        String GeneralStatisticsHeader;
        GeneralStatisticsHeader = "GeneralStatistic, Value, GeneralStatisticDescription";
        pw5 = getPrintWriter(
                "__GeneralStatistics",
                dirName);
        pw5.println(GeneralStatisticsHeader);
        HashMap<String, String> generalStatisticsDescriptions;
        generalStatisticsDescriptions = getGeneralStatisticDescriptions(
                startMonth, startYear, endMonth, endYear);
        writeLine(sTotalCount_AlwaysUOTT1FromStart,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_AlwaysUOTT1FromWhenStarted,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_AlwaysUOTT4FromStart,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_AlwaysUOTT4FromWhenStarted,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sAverageHouseholdSizeOfThoseUOTT1AlwaysFromStart,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sAverageHouseholdSizeOfThoseUOTT4AlwaysFromStart,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_ClaimsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_UniqueIndividualsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_UniqueDependentsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_UniqueDependentsAgedUnder10EffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_UniqueDependentsAgedOver10EffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_CouncilClaimsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_CouncilUniqueIndividualsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_CouncilUniqueClaimantsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_CouncilUniquePartnersEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_CouncilDependentsAgedUnder10EffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_CouncilDependentsAgedOver10EffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_CouncilUniqueNonDependentsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_RSLClaimsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_RSLUniqueIndividualsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_RSLUniqueClaimantsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_RSLUniquePartnersEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_RSLDependentsUnder10EffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_RSLDependentsOver10EffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTotalCount_RSLUniqueNonDependentsEffectedByUnderOccupancy,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sNotUOInApril2013ThenUOAndUOInLatestMonth,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOClaimsRecievingDHP,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageOfUO_ReceivingDHPInLatestMonth,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1ClaimsInRentArrearsAtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1ClaimsInRentArrearsOver500AtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageOfCouncilUO_InArrearsInLatestMonth,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageOfCouncilUO_InArrears0To10InLatestMonth,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageOfCouncilUO_InArrears10To100InLatestMonth,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageOfCouncilUO_InArrears100To500InLatestMonth,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageOfCouncilUO_InArrearsOver500InLatestMonth,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageOfCouncilUO_InArrearsAndReceivingDHPInLatestMonth,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageOfUO_ReceivingDHPInLatestMonthThatWereUOInApril2013,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageOfCouncilUO_InArrearsInLatestMonthThatWereUOInApril2013,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageOfCouncilUO_InArrears0To10InLatestMonthThatWereUOInApril2013,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageOfCouncilUO_InArrears10To100InLatestMonthThatWereUOInApril2013,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageOfCouncilUO_InArrears100To500InLatestMonthThatWereUOInApril2013,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageOfCouncilUO_InArrearsOver500InLatestMonthThatWereUOInApril2013,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1ClaimsInRentArrearsAndRecievingDHPAtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sInArrearsAtSomePoint_And_DHPAtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPercentageOfCouncilUO_InArrearsAndReceivingDHPInLatestMonthThatWereUOInApril2013,
                generalStatistics, generalStatisticsDescriptions, pw5);
        // UO to left SHBE
        writeLine(sUO_To_LeftSHBEAtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUO_To_LeftSHBETheVeryNextMonth,
                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sUO_To_LeftSHBEBetweenOneAndTwoMonths,
//                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sUO_To_LeftSHBEBetweenTwoAndThreeMonths,
//                generalStatistics, generalStatisticsDescriptions, pw5);
        // UOTT1 to left SHBE
        writeLine(sUOTT1_To_LeftSHBE,
                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sUOTT1_To_LeftSHBEReturnedAsTT1orTT4,
//                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sUOTT1_To_LeftSHBEReturnedAsTT3OrTT6,
//                generalStatistics, generalStatisticsDescriptions, pw5);
        // UOTT4 to left SHBE
        writeLine(sUOTT4_To_LeftSHBE,
                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sUOTT4_To_LeftSHBEReturnedAsTT1orTT4,
//                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sUOTT4_To_LeftSHBEReturnedAsTT3OrTT6,
//                generalStatistics, generalStatisticsDescriptions, pw5);
        // UOTT3OrTT6 to left SHBE
        writeLine(sUOTT3OrTT6_To_LeftSHBE,
                generalStatistics, generalStatisticsDescriptions, pw5);
        // UONotTT1OrTT3OrTT4OrTT6 to left SHBE
        writeLine(sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUO_To_LeftSHBE_NotReturned,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_NotReturned,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_NotReturned,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_NotReturned,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUONotTT1OrTT3OrTT4OrTT6_To_LeftSHBE_NotReturned,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAsUOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAsTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAsTT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAsUOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAsTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAsTT5OrTT7,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAsTT8,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAsTT9,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAsUOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAsTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAsTT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAsUOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAsTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAsTT5OrTT7,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAsTT8,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAsTT9,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsUOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT5OrTT7,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT8,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT3OrTT6_To_LeftSHBE_ReturnedAsTT9,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_LeftSHBE_ReturnedAndBecameUOAgainAtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sUO_NotUO_UO,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUO_NotUO_UO_NotUO_UO,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUO_NotUO_UO_NotUO_UO_NotUO_UO,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUO_NotUO_UO_NotUO_UO_NotUO_UO_NotUO_UO,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sUOTT1_To_NotUO_InSHBE_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT4_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_NotUO_InSHBE_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT1_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sUOTT1_To_TT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT3OrTT6AtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT3OrTT6_To_TT1OrUOTT1AtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sUOTT4_To_TT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT3OrTT6AtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT3OrTT6AsNextTTChangeIgnoreMinus999,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT3OrTT6_To_TT4OrUOTT4AtSomePoint,
                generalStatistics, generalStatisticsDescriptions, pw5);

        writeLine(sTT3OrTT6_To_UOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT3OrTT6_To_UOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        // TT1
        writeLine(sTT1_To_UOTT1_PostcodeUnchanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1Month,
//                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter2Months,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeUnchangedButChangedAfter3Months,
                generalStatistics, generalStatisticsDescriptions, pw5);
        //writeLine(sUOTT1OrTT1_To_UOTT4,
        //        generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_UOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT4GettingDHP,
                generalStatistics, generalStatisticsDescriptions, pw5);

        // TT4
        writeLine(sTT4_To_UOTT4_PostcodeUnchanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1Month,
//                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter2Months,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeUnchangedButChangedAfter3Months,
                generalStatistics, generalStatisticsDescriptions, pw5);
        //writeLine(sUOTT4OrTT4_To_UOTT1,
        //        generalStatistics, generalStatisticsDescriptions, pw5);
        //writeLine(sUOTT4OrTT4_To_UOTT1InArrears,
        //        generalStatistics, generalStatisticsDescriptions, pw5);
        //writeLine(sUOTT4OrTT4_To_UOTT1InArrearsAndGettingDHP,
        //        generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_UOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_UOTT1InArrears,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_UOTT1GettingDHP,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_UOTT1InArrearsAndGettingDHP,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT1InArrears,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT1GettingDHP,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT1InArrearsAndGettingDHP,
                generalStatistics, generalStatisticsDescriptions, pw5);
        // Postcode Unchanged
        writeLine(sUOTT1_To_TT1_PostcodeUnchanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1Month,
//                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT8,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter1MonthTT9,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter2Months,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_TT1_PostcodeUnchangedButChangedAfter3Months,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
//        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1Month,
//                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT1,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT3OrTT6,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthUOTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT4,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT5OrTT7,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT8,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter1MonthTT9,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter2Months,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeUnchangedButChangedAfter3Months,
                generalStatistics, generalStatisticsDescriptions, pw5);
        // Postcode Changed
        writeLine(sUOTT1_To_TT1_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT1_To_UOTT1_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_TT4_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sUOTT4_To_UOTT4_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT1_To_UOTT1_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sTT4_To_UOTT4_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPermanantlyLeftUOButRemainedInSHBE_PostcodeChanged,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPermanantlyLeftUOButRemainedInSHBE,
                generalStatistics, generalStatisticsDescriptions, pw5);
        writeLine(sPermanantlyLeftUOButRemainedInSHBE_HouseholdSizeIncreased,
                generalStatistics, generalStatisticsDescriptions, pw5);
        pw5.close();
        env.ge.log("</WriteGeneralStatistics>", true);

        HashSet<SHBE_ClaimID> ClaimIDsCheck;
        ClaimIDsCheck = new HashSet<>();
        boolean check;

        String GroupName;
        PrintWriter pw;
        PrintWriter pw2;
        PrintWriter pwAggregateStatistics;
        PrintWriter pwAggregateStatistics2;
        String name2;
        String GroupNameDescription;
        HashSet<SHBE_ClaimID> Group;
        SHBE_ClaimID ClaimID;
        String ClaimRef;

        Iterator<String> iteG;
        //Iterator<String> ite;
        int counter;

        env.ge.log("Group Size, Number in the group that have not previously been counted, Group Name", true);

        iteG = GroupNameDescriptions.keySet().iterator();
        while (iteG.hasNext()) {
            GroupName = iteG.next();
            name2 = GroupName;
            if (includePreUnderOccupancyValues) {
                name2 += DW_Strings.symbol_underscore + sIncludesPreUnderOccupancyValues;
            }
            pw = getPrintWriter(name2, dirName);
            pwAggregateStatistics = getPrintWriter(name2 + sAggregateStatistics, dirName);
            name2 += DW_Strings.symbol_underscore + "WithDuplicates";
            pw2 = getPrintWriter(name2, dirName);
            pwAggregateStatistics2 = getPrintWriter(name2 + sAggregateStatistics, dirName);
            // Write header
            GroupNameDescription = GroupNameDescriptions.get(GroupName);
            pw.println(GroupNameDescription);
            pw.println(Header);
            pwAggregateStatistics.println(GroupNameDescription);
            pwAggregateStatistics.println(AggregateStatisticsHeader);
            pw2.println(GroupNameDescription);
            pw2.println(Header);
            pwAggregateStatistics2.println(GroupNameDescription);
            pwAggregateStatistics2.println(AggregateStatisticsHeader);
            Group = groups.get(GroupName);
            counter = 0;
            Iterator<SHBE_ClaimID> iteID;
            iteID = Group.iterator();
            while (iteID.hasNext()) {
                ClaimID = iteID.next();
                ClaimRef = cid2c.get(ClaimID);
                check = ClaimIDsCheck.add(ClaimID);
                if (check == false) {
                    String otherGroupName;
                    HashSet<SHBE_ClaimID> otherGroup;
                    if (GroupName.equalsIgnoreCase(sTTNot1Or4AndUnderOccupying)) {
                        otherGroupName = sTravellers;
                        otherGroup = groups.get(otherGroupName);
                        if (otherGroup.contains(ClaimID)) {
                            writeRecordCollection(
                                    tableValues,
                                    includePreUnderOccupancyValues,
                                    preUnderOccupancyValues,
                                    ClaimRef,
                                    pw2);
                            writeAggregateRecords(
                                    aggregateStatistics,
                                    ClaimRef,
                                    pwAggregateStatistics2);
                        } else {
                            env.ge.log("ClaimRef " + ClaimRef
                                    + " is in group " + GroupName
                                    + " and is in one of the not "
                                    + "expected other groups "
                                    + "previously written out.", true);
                        }
                    } else if (GroupName.equalsIgnoreCase(sTT1_To_TT3OrTT6)) {
                        otherGroupName = sTravellers;
                        otherGroup = groups.get(otherGroupName);
                        if (otherGroup.contains(ClaimID)) {
                            writeRecordCollection(
                                    tableValues,
                                    includePreUnderOccupancyValues,
                                    preUnderOccupancyValues,
                                    ClaimRef,
                                    pw2);
                            writeAggregateRecords(
                                    aggregateStatistics,
                                    ClaimRef,
                                    pwAggregateStatistics2);
                        } else {
                            otherGroupName = sTTNot1Or4AndUnderOccupying;
                            otherGroup = groups.get(otherGroupName);
                            if (otherGroup.contains(ClaimID)) {
                                writeRecordCollection(
                                        tableValues,
                                        includePreUnderOccupancyValues,
                                        preUnderOccupancyValues,
                                        ClaimRef,
                                        pw2);
                                writeAggregateRecords(
                                        aggregateStatistics,
                                        ClaimRef,
                                        pwAggregateStatistics2);
                            } else {
                                env.ge.log("ClaimRef " + ClaimRef
                                        + " is in group " + GroupName
                                        + " and is in one of the not "
                                        + "expected other groups "
                                        + "previously written out.", true);
                            }
                        }
                    } else if (GroupName.equalsIgnoreCase(sTT4_To_TT3OrTT6)) {
                        otherGroupName = sTravellers;
                        otherGroup = groups.get(otherGroupName);
                        if (otherGroup.contains(ClaimID)) {
                            writeRecordCollection(
                                    tableValues,
                                    includePreUnderOccupancyValues,
                                    preUnderOccupancyValues,
                                    ClaimRef,
                                    pw2);
                            writeAggregateRecords(
                                    aggregateStatistics,
                                    ClaimRef,
                                    pwAggregateStatistics2);
                        } else {
                            otherGroupName = sTTNot1Or4AndUnderOccupying;
                            otherGroup = groups.get(otherGroupName);
                            if (otherGroup.contains(ClaimID)) {
                                writeRecordCollection(
                                        tableValues,
                                        includePreUnderOccupancyValues,
                                        preUnderOccupancyValues,
                                        ClaimRef,
                                        pw2);
                                writeAggregateRecords(
                                        aggregateStatistics,
                                        ClaimRef,
                                        pwAggregateStatistics2);
                            } else {
                                otherGroupName = sTT1_To_TT3OrTT6;
                                otherGroup = groups.get(otherGroupName);
                                if (otherGroup.contains(ClaimID)) {
                                    writeRecordCollection(
                                            tableValues,
                                            includePreUnderOccupancyValues,
                                            preUnderOccupancyValues,
                                            ClaimRef,
                                            pw2);
                                    writeAggregateRecords(
                                            aggregateStatistics,
                                            ClaimRef,
                                            pwAggregateStatistics2);
                                } else {
                                    env.ge.log("ClaimRef " + ClaimRef
                                            + " is in group " + GroupName
                                            + " and is in one of the not "
                                            + "expected other groups "
                                            + "previously written out.", true);
                                }
                            }
                        }
                    } else if (GroupName.equalsIgnoreCase(sTT3OrTT6_To_TT1)) {
                        otherGroupName = sTravellers;
                        otherGroup = groups.get(otherGroupName);
                        if (otherGroup.contains(ClaimID)) {
                            writeRecordCollection(
                                    tableValues,
                                    includePreUnderOccupancyValues,
                                    preUnderOccupancyValues,
                                    ClaimRef,
                                    pw2);
                            writeAggregateRecords(
                                    aggregateStatistics,
                                    ClaimRef,
                                    pwAggregateStatistics2);
                        } else {
                            otherGroupName = sTTNot1Or4AndUnderOccupying;
                            otherGroup = groups.get(otherGroupName);
                            if (otherGroup.contains(ClaimID)) {
                                writeRecordCollection(
                                        tableValues,
                                        includePreUnderOccupancyValues,
                                        preUnderOccupancyValues,
                                        ClaimRef,
                                        pw2);
                                writeAggregateRecords(
                                        aggregateStatistics,
                                        ClaimRef,
                                        pwAggregateStatistics2);
                            } else {
                                otherGroupName = sTT1_To_TT3OrTT6;
                                otherGroup = groups.get(otherGroupName);
                                if (otherGroup.contains(ClaimID)) {
                                    writeRecordCollection(
                                            tableValues,
                                            includePreUnderOccupancyValues,
                                            preUnderOccupancyValues,
                                            ClaimRef,
                                            pw2);
                                    writeAggregateRecords(
                                            aggregateStatistics,
                                            ClaimRef,
                                            pwAggregateStatistics2);
                                } else {
                                    otherGroupName = sTT4_To_TT3OrTT6;
                                    otherGroup = groups.get(otherGroupName);
                                    if (otherGroup.contains(ClaimID)) {
                                        writeRecordCollection(
                                                tableValues,
                                                includePreUnderOccupancyValues,
                                                preUnderOccupancyValues,
                                                ClaimRef,
                                                pw2);
                                        writeAggregateRecords(
                                                aggregateStatistics,
                                                ClaimRef,
                                                pwAggregateStatistics2);
                                    } else {
                                        env.ge.log("ClaimRef " + ClaimRef
                                                + " is in group " + GroupName
                                                + " and is in one of the not "
                                                + "expected other groups "
                                                + "previously written out.", true);
                                    }
                                }
                            }
                        }
                    } else if (GroupName.equalsIgnoreCase(sTT3OrTT6_To_TT4)) {
                        otherGroupName = sTravellers;
                        otherGroup = groups.get(otherGroupName);
                        if (otherGroup.contains(ClaimID)) {
                            writeRecordCollection(
                                    tableValues,
                                    includePreUnderOccupancyValues,
                                    preUnderOccupancyValues,
                                    ClaimRef,
                                    pw2);
                            writeAggregateRecords(
                                    aggregateStatistics,
                                    ClaimRef,
                                    pwAggregateStatistics2);
                        } else {
                            otherGroupName = sTTNot1Or4AndUnderOccupying;
                            otherGroup = groups.get(otherGroupName);
                            if (otherGroup.contains(ClaimID)) {
                                writeRecordCollection(
                                        tableValues,
                                        includePreUnderOccupancyValues,
                                        preUnderOccupancyValues,
                                        ClaimRef,
                                        pw2);
                                writeAggregateRecords(
                                        aggregateStatistics,
                                        ClaimRef,
                                        pwAggregateStatistics2);
                            } else {
                                otherGroupName = sTT1_To_TT3OrTT6;
                                otherGroup = groups.get(otherGroupName);
                                if (otherGroup.contains(ClaimID)) {
                                    writeRecordCollection(
                                            tableValues,
                                            includePreUnderOccupancyValues,
                                            preUnderOccupancyValues,
                                            ClaimRef,
                                            pw2);
                                    writeAggregateRecords(
                                            aggregateStatistics,
                                            ClaimRef,
                                            pwAggregateStatistics2);
                                } else {
                                    otherGroupName = sTT4_To_TT3OrTT6;
                                    otherGroup = groups.get(otherGroupName);
                                    if (otherGroup.contains(ClaimID)) {
                                        writeRecordCollection(
                                                tableValues,
                                                includePreUnderOccupancyValues,
                                                preUnderOccupancyValues,
                                                ClaimRef,
                                                pw2);
                                        writeAggregateRecords(
                                                aggregateStatistics,
                                                ClaimRef,
                                                pwAggregateStatistics2);
                                    } else {
                                        otherGroupName = sTT3OrTT6_To_TT1;
                                        otherGroup = groups.get(otherGroupName);
                                        if (otherGroup.contains(ClaimID)) {
                                            writeRecordCollection(
                                                    tableValues,
                                                    includePreUnderOccupancyValues,
                                                    preUnderOccupancyValues,
                                                    ClaimRef,
                                                    pw2);
                                            writeAggregateRecords(
                                                    aggregateStatistics,
                                                    ClaimRef,
                                                    pwAggregateStatistics2);
                                        } else {
                                            env.ge.log("ClaimRef " + ClaimRef
                                                    + " is in group " + GroupName
                                                    + " and is in one of the not "
                                                    + "expected other groups "
                                                    + "previously written out.", true);
                                        }
                                    }
                                }
                            }
                        }
                    } else if (GroupName.equalsIgnoreCase(sAlwaysUOFromWhenStarted__ChangedTT)) {
                        otherGroupName = sTravellers;
                        otherGroup = groups.get(otherGroupName);
                        if (otherGroup.contains(ClaimID)) {
                            writeRecordCollection(
                                    tableValues,
                                    includePreUnderOccupancyValues,
                                    preUnderOccupancyValues,
                                    ClaimRef,
                                    pw2);
                            writeAggregateRecords(
                                    aggregateStatistics,
                                    ClaimRef,
                                    pwAggregateStatistics2);
                        } else {
                            otherGroupName = sTTNot1Or4AndUnderOccupying;
                            otherGroup = groups.get(otherGroupName);
                            if (otherGroup.contains(ClaimID)) {
                                writeRecordCollection(
                                        tableValues,
                                        includePreUnderOccupancyValues,
                                        preUnderOccupancyValues,
                                        ClaimRef,
                                        pw2);
                                writeAggregateRecords(
                                        aggregateStatistics,
                                        ClaimRef,
                                        pwAggregateStatistics2);
                            } else {
                                otherGroupName = sTT1_To_TT3OrTT6;
                                otherGroup = groups.get(otherGroupName);
                                if (otherGroup.contains(ClaimID)) {
                                    writeRecordCollection(
                                            tableValues,
                                            includePreUnderOccupancyValues,
                                            preUnderOccupancyValues,
                                            ClaimRef,
                                            pw2);
                                    writeAggregateRecords(
                                            aggregateStatistics,
                                            ClaimRef,
                                            pwAggregateStatistics2);
                                } else {
                                    otherGroupName = sTT4_To_TT3OrTT6;
                                    otherGroup = groups.get(otherGroupName);
                                    if (otherGroup.contains(ClaimID)) {
                                        writeRecordCollection(
                                                tableValues,
                                                includePreUnderOccupancyValues,
                                                preUnderOccupancyValues,
                                                ClaimRef,
                                                pw2);
                                        writeAggregateRecords(
                                                aggregateStatistics,
                                                ClaimRef,
                                                pwAggregateStatistics2);
                                    } else {
                                        otherGroupName = sTT3OrTT6_To_TT1;
                                        otherGroup = groups.get(otherGroupName);
                                        if (otherGroup.contains(ClaimID)) {
                                            writeRecordCollection(
                                                    tableValues,
                                                    includePreUnderOccupancyValues,
                                                    preUnderOccupancyValues,
                                                    ClaimRef,
                                                    pw2);
                                            writeAggregateRecords(
                                                    aggregateStatistics,
                                                    ClaimRef,
                                                    pwAggregateStatistics2);
                                        } else {
                                            otherGroupName = sTT3OrTT6_To_TT4;
                                            otherGroup = groups.get(otherGroupName);
                                            if (otherGroup.contains(ClaimID)) {
                                                writeRecordCollection(
                                                        tableValues,
                                                        includePreUnderOccupancyValues,
                                                        preUnderOccupancyValues,
                                                        ClaimRef,
                                                        pw2);
                                                writeAggregateRecords(
                                                        aggregateStatistics,
                                                        ClaimRef,
                                                        pwAggregateStatistics2);
                                            } else {
                                                env.ge.log("ClaimRef " + ClaimRef
                                                        + " is in group " + GroupName
                                                        + " and is in one of the not "
                                                        + "expected other groups "
                                                        + "previously written out.", true);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (GroupName.equalsIgnoreCase(sAlwaysUOFromWhenStarted__ValidPostcodeChange_NotChangedTT)) {
                        otherGroupName = sTravellers;
                        otherGroup = groups.get(otherGroupName);
                        if (otherGroup.contains(ClaimID)) {
                            writeRecordCollection(
                                    tableValues,
                                    includePreUnderOccupancyValues,
                                    preUnderOccupancyValues,
                                    ClaimRef,
                                    pw2);
                            writeAggregateRecords(
                                    aggregateStatistics,
                                    ClaimRef,
                                    pwAggregateStatistics2);
                        } else {
                            otherGroupName = sTTNot1Or4AndUnderOccupying;
                            otherGroup = groups.get(otherGroupName);
                            if (otherGroup.contains(ClaimID)) {
                                writeRecordCollection(
                                        tableValues,
                                        includePreUnderOccupancyValues,
                                        preUnderOccupancyValues,
                                        ClaimRef,
                                        pw2);
                                writeAggregateRecords(
                                        aggregateStatistics,
                                        ClaimRef,
                                        pwAggregateStatistics2);
                            } else {
                                env.ge.log("ClaimRef " + ClaimRef
                                        + " is in group " + GroupName
                                        + " and is in one of the not "
                                        + "expected other groups "
                                        + "previously written out.", true);
                            }
                        }
                    } else if (GroupName.equalsIgnoreCase(sAlwaysUOFromWhenStarted__NoValidPostcodeChange_NotChangedTT)) {
                        otherGroupName = sTravellers;
                        otherGroup = groups.get(otherGroupName);
                        if (otherGroup.contains(ClaimID)) {
                            writeRecordCollection(
                                    tableValues,
                                    includePreUnderOccupancyValues,
                                    preUnderOccupancyValues,
                                    ClaimRef,
                                    pw2);
                            writeAggregateRecords(
                                    aggregateStatistics,
                                    ClaimRef,
                                    pwAggregateStatistics2);
                        } else {
                            otherGroupName = sTTNot1Or4AndUnderOccupying;
                            otherGroup = groups.get(otherGroupName);
                            if (otherGroup.contains(ClaimID)) {
                                writeRecordCollection(
                                        tableValues,
                                        includePreUnderOccupancyValues,
                                        preUnderOccupancyValues,
                                        ClaimRef,
                                        pw2);
                                writeAggregateRecords(
                                        aggregateStatistics,
                                        ClaimRef,
                                        pwAggregateStatistics2);
                            } else {
                                env.ge.log("ClaimRef " + ClaimRef
                                        + " is in group " + GroupName
                                        + " and is in one of the not "
                                        + "expected other groups "
                                        + "previously written out.", true);
                            }
                        }
                    } else if (GroupName.equalsIgnoreCase(sIntermitantUO__ChangedTT)) {
                        otherGroupName = sTravellers;
                        otherGroup = groups.get(otherGroupName);
                        if (otherGroup.contains(ClaimID)) {
                            writeRecordCollection(
                                    tableValues,
                                    includePreUnderOccupancyValues,
                                    preUnderOccupancyValues,
                                    ClaimRef,
                                    pw2);
                            writeAggregateRecords(
                                    aggregateStatistics,
                                    ClaimRef,
                                    pwAggregateStatistics2);
                        } else {
                            otherGroupName = sTTNot1Or4AndUnderOccupying;
                            otherGroup = groups.get(otherGroupName);
                            if (otherGroup.contains(ClaimID)) {
                                writeRecordCollection(
                                        tableValues,
                                        includePreUnderOccupancyValues,
                                        preUnderOccupancyValues,
                                        ClaimRef,
                                        pw2);
                                writeAggregateRecords(
                                        aggregateStatistics,
                                        ClaimRef,
                                        pwAggregateStatistics2);
                            } else {
                                otherGroupName = sTT1_To_TT3OrTT6;
                                otherGroup = groups.get(otherGroupName);
                                if (otherGroup.contains(ClaimID)) {
                                    writeRecordCollection(
                                            tableValues,
                                            includePreUnderOccupancyValues,
                                            preUnderOccupancyValues,
                                            ClaimRef,
                                            pw2);
                                    writeAggregateRecords(
                                            aggregateStatistics,
                                            ClaimRef,
                                            pwAggregateStatistics2);
                                } else {
                                    otherGroupName = sTT4_To_TT3OrTT6;
                                    otherGroup = groups.get(otherGroupName);
                                    if (otherGroup.contains(ClaimID)) {
                                        writeRecordCollection(
                                                tableValues,
                                                includePreUnderOccupancyValues,
                                                preUnderOccupancyValues,
                                                ClaimRef,
                                                pw2);
                                        writeAggregateRecords(
                                                aggregateStatistics,
                                                ClaimRef,
                                                pwAggregateStatistics2);
                                    } else {
                                        otherGroupName = sTT3OrTT6_To_TT1;
                                        otherGroup = groups.get(otherGroupName);
                                        if (otherGroup.contains(ClaimID)) {
                                            writeRecordCollection(
                                                    tableValues,
                                                    includePreUnderOccupancyValues,
                                                    preUnderOccupancyValues,
                                                    ClaimRef,
                                                    pw2);
                                            writeAggregateRecords(
                                                    aggregateStatistics,
                                                    ClaimRef,
                                                    pwAggregateStatistics2);
                                        } else {
                                            otherGroupName = sTT3OrTT6_To_TT4;
                                            otherGroup = groups.get(otherGroupName);
                                            if (otherGroup.contains(ClaimID)) {
                                                writeRecordCollection(
                                                        tableValues,
                                                        includePreUnderOccupancyValues,
                                                        preUnderOccupancyValues,
                                                        ClaimRef,
                                                        pw2);
                                                writeAggregateRecords(
                                                        aggregateStatistics,
                                                        ClaimRef,
                                                        pwAggregateStatistics2);
                                            } else {
                                                env.ge.log("ClaimRef " + ClaimRef
                                                        + " is in group " + GroupName
                                                        + " and is in one of the not "
                                                        + "expected other groups "
                                                        + "previously written out.", true);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (GroupName.equalsIgnoreCase(sIntermitantUO__ValidPostcodeChange_NotChangedTT)) {
                        otherGroupName = sTravellers;
                        otherGroup = groups.get(otherGroupName);
                        if (otherGroup.contains(ClaimID)) {
                            writeRecordCollection(
                                    tableValues,
                                    includePreUnderOccupancyValues,
                                    preUnderOccupancyValues,
                                    ClaimRef,
                                    pw2);
                            writeAggregateRecords(
                                    aggregateStatistics,
                                    ClaimRef,
                                    pwAggregateStatistics2);
                        } else {
                            otherGroupName = sTTNot1Or4AndUnderOccupying;
                            otherGroup = groups.get(otherGroupName);
                            if (otherGroup.contains(ClaimID)) {
                                writeRecordCollection(
                                        tableValues,
                                        includePreUnderOccupancyValues,
                                        preUnderOccupancyValues,
                                        ClaimRef,
                                        pw2);
                                writeAggregateRecords(
                                        aggregateStatistics,
                                        ClaimRef,
                                        pwAggregateStatistics2);
                            } else {
                                env.ge.log("ClaimRef " + ClaimRef
                                        + " is in group " + GroupName
                                        + " and is in one of the not "
                                        + "expected other groups "
                                        + "previously written out.", true);
                            }
                        }
                    } else {
//                                                            env.ge.log("ClaimRef " + aClaimRef + " already added to"
//                                                                    + " another group and in " + groupNameDescription);
//                                                            writeRecordCollectionToStdOut(
//                                                                    tableValues,
//                                                                    includePreUnderOccupancyValues,
//                                                                    preUnderOccupancyValues,
//                                                                    aClaimRef);
                        writeRecordCollection(
                                tableValues,
                                includePreUnderOccupancyValues,
                                preUnderOccupancyValues,
                                ClaimRef,
                                pw);
                        writeAggregateRecords(
                                aggregateStatistics,
                                ClaimRef,
                                pwAggregateStatistics);
                        writeRecordCollection(
                                tableValues,
                                includePreUnderOccupancyValues,
                                preUnderOccupancyValues,
                                ClaimRef,
                                pw2);
                        writeAggregateRecords(
                                aggregateStatistics,
                                ClaimRef,
                                pwAggregateStatistics2);
                    } //                                        sAlwaysUOFromStart__NoValidPostcodeChange_NotChangedTT
                    //                                        sIntermitantUO__NoValidPostcodeChange_NotChangedTT                                                    
                } else {
                    counter++;
                    writeRecordCollection(
                            tableValues,
                            includePreUnderOccupancyValues,
                            preUnderOccupancyValues,
                            ClaimRef,
                            pw);
                    writeRecordCollection(
                            tableValues,
                            includePreUnderOccupancyValues,
                            preUnderOccupancyValues,
                            ClaimRef,
                            pw2);
                    writeAggregateRecords(
                            aggregateStatistics,
                            ClaimRef,
                            pwAggregateStatistics);
                    writeAggregateRecords(
                            aggregateStatistics,
                            ClaimRef,
                            pwAggregateStatistics2);
                }
            }
            env.ge.log(Group.size() + ", " + counter + ", " + GroupNameDescription, true);
            pw.close();
            pwAggregateStatistics.close();
            pw2.close();
            pwAggregateStatistics2.close();
        }

        // Check size of ClaimRefsCheck
        if (ClaimIDsCheck.size() != ClaimIDs.size()) {
            System.out.println("ClaimRefsCheck.size() != ClaimRefs.size()");
            System.out.println("" + ClaimIDsCheck.size() + " ClaimRefsCheck.size()");
            System.out.println("" + ClaimIDs.size() + " ClaimRefs.size()");
        }

        HashSet<SHBE_ClaimID> remainder;
        remainder = new HashSet<>();
        remainder.addAll(ClaimIDs);
        remainder.removeAll(ClaimIDsCheck);

        GroupName = "remainder";
        name2 = GroupName;
        pw = getPrintWriter(name2, dirName);
        pw.println(Header);
        Iterator<SHBE_ClaimID> iteID;
        iteID = remainder.iterator();
        while (iteID.hasNext()) {
            ClaimID = iteID.next();
            ClaimRef = cid2c.get(ClaimID);
            writeRecordCollection(
                    tableValues,
                    includePreUnderOccupancyValues,
                    preUnderOccupancyValues,
                    ClaimRef,
                    pw);
        }
        pw.close();
        env.ge.log("</WriteTenancyChangeTables>", true);
    }

    protected void writeAggregateRecords(TreeMap<String, BigDecimal> aggregateStatistics,
            String ClaimRef, PrintWriter pw) {
        String s2 = DW_Strings.special_commaSpace;
        String s_ = DW_Strings.symbol_underscore;
        String line;
        line = "" + ClaimRef;
        line += s2 + decimalise(aggregateStatistics.get(ClaimRef + s_ + sTotal_DHP).intValue());
        line += s2 + aggregateStatistics.get(ClaimRef + s_ + sTotalCount_DHP);
        line += s2 + decimalise(aggregateStatistics.get(ClaimRef + s_ + sTotal_HBLossDueToUO).intValue());
        line += s2 + aggregateStatistics.get(ClaimRef + s_ + sTotalCount_HBLossDueToUO);
        line += s2 + aggregateStatistics.get(ClaimRef + s_ + sMax_Arrears);
        line += s2 + aggregateStatistics.get(ClaimRef + s_ + sTotalCount_InArrears);
        line += s2 + aggregateStatistics.get(ClaimRef + s_ + sTotalCount_InArrears0To10);
        line += s2 + aggregateStatistics.get(ClaimRef + s_ + sTotalCount_InArrears10To100);
        line += s2 + aggregateStatistics.get(ClaimRef + s_ + sTotalCount_InArrears100To500);
        line += s2 + aggregateStatistics.get(ClaimRef + s_ + sTotalCount_InArrearsOver500);
        line += s2 + aggregateStatistics.get(ClaimRef + s_ + sTotalCount_UnderOccupancy);
        pw.println(line);
    }

    protected void writeRecordCollection(
            TreeMap<String, String> tableValues,
            boolean includePreUnderOccupancyValues,
            TreeMap<String, String> preUnderOccupancyValues,
            String ClaimRef,
            PrintWriter pw) {
        ArrayList<String> keys;
        keys = getKeys(ClaimRef);
        String key;
        String line;
        Iterator<String> ite;
        ite = keys.iterator();
        while (ite.hasNext()) {
            key = ite.next();
            line = key;
            if (includePreUnderOccupancyValues) {
                line += preUnderOccupancyValues.get(key);
            }
            line += tableValues.get(key);
            pw.println(line);
        }
        pw.println();
    }

    private void writeRecordCollectionToStdOut(
            TreeMap<String, String> tableValues,
            boolean includePreUnderOccupancyValues,
            TreeMap<String, String> preUnderOccupancyValues,
            String ClaimRef) {
        ArrayList<String> keys;
        keys = getKeys(ClaimRef);
        String key;
        String line;
        Iterator<String> ite;
        ite = keys.iterator();
        while (ite.hasNext()) {
            key = ite.next();
            line = key;
            if (includePreUnderOccupancyValues) {
                line += preUnderOccupancyValues.get(key);
            }
            line += tableValues.get(key);
            System.out.println(line);
        }
        System.out.println();
    }

    protected PrintWriter getPrintWriter(String name, String dirName)
            throws IOException {
        Path dirOut = Paths.get(files.getOutputSHBETablesDir().toString(),
                sUnderOccupancyGroupTables, dirName);
        Files.createDirectories(dirOut);
        String outFilename = name + ".csv";
        Path outFile = Paths.get(dirOut.toString(), outFilename);
        return Generic_IO.getPrintWriter(outFile, false);
    }

    public TreeMap<String, String> getGroupNameDescriptions(Set<String> GroupNames) {
        TreeMap<String, String> r = new TreeMap<>();
        Iterator<String> ite = GroupNames.iterator();
        while (ite.hasNext()) {
            String groupName = ite.next();
            r.put(groupName, "Type: " + groupName);
        }
        return r;

    }

    protected void addToSets(SHBE_ClaimID ClaimID,
            HashSet<SHBE_PersonID> ClaimantPersonIDs,
            HashSet<SHBE_PersonID> PartnersPersonIDs,
            HashSet<SHBE_PersonID> DependentChildrenUnder10,
            HashSet<SHBE_PersonID> DependentChildrenOver10,
            HashSet<SHBE_PersonID> NonDependentPersonIDs,
            HashMap<SHBE_ClaimID, Integer> MaxNumberOfDependentsInClaimWhenUO,
            String year, String month, UKP_YM3 YM3, 
            ArrayList<SHBE_S_Record> SRecords,            SHBE_D_Record d,
            Map<SHBE_ClaimID, SHBE_PersonID> ClaimIDToClaimantPersonIDLookup) 
            throws IOException, ClassNotFoundException {
        SHBE_PersonID ClaimantPersonID;
        ClaimantPersonID = ClaimIDToClaimantPersonIDLookup.get(ClaimID);
        ClaimantPersonIDs.add(ClaimantPersonID);
        if (d.getPartnerFlag() == 1) {
            PartnersPersonIDs.add(shbeHandler.getPartnerPersonID(d));
        }
        if (SRecords != null) {
            int index = 0;
            SHBE_S_Record SRecord;
            Iterator<SHBE_S_Record> sIte;
            sIte = SRecords.iterator();
            while (sIte.hasNext()) {
                SRecord = sIte.next();
                int subRecordType;
                subRecordType = SRecord.getSubRecordType();
//            int nonDependentStatus;
//            nonDependentStatus = SRecord.getNonDependentStatus();
//            if (nonDependentStatus > 0 && nonDependentStatus < 9) {
                if (subRecordType == 1) {
                    Integer m;
                    m = MaxNumberOfDependentsInClaimWhenUO.get(ClaimID);
                    if (m == null) {
                        m = 0;
                    }
                    int numberOfChildDependents;
                    numberOfChildDependents = d.getNumberOfChildDependents();
                    int max;
                    max = Math.max(m, numberOfChildDependents);
                    MaxNumberOfDependentsInClaimWhenUO.put(ClaimID, max);
                    String DoB = SRecord.getSubRecordDateOfBirth();
                    int age = Integer.valueOf(shbeHandler.getAge(year, month, DoB));
                    if (age < 10) {
                        DependentChildrenUnder10.add(shbeHandler.getDependentPersonID(SRecord, index));
                    } else {
                        DependentChildrenOver10.add(shbeHandler.getDependentPersonID(SRecord, index));
                    }
                } else {
                    NonDependentPersonIDs.add(shbeHandler.getNonDependentPersonID(SRecord));
                }
                index++;
            }
        }
    }

}
