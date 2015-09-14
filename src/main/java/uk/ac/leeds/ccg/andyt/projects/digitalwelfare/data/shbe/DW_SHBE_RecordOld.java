/*
 * Copyright (C) 2014 geoagdt.
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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe;

import java.io.Serializable;
import java.util.HashSet;

/**
 *
 * @author geoagdt
 */
public class DW_SHBE_RecordOld implements Serializable {

    /**
     * SRecords associated with a DRecord
     */
    protected HashSet<DW_SHBE_RecordOld> SRecords;
    /**
     * 0 RecordID
     */
    protected long RecordID;
    /**
     * 0 1 RecordType
     */
    private String RecordType;
    /**
     * 1 2 HousingBenefitClaimReferenceNumber
     */
    private String HousingBenefitClaimReferenceNumber;
    //private Long HousingBenefitClaimReferenceNumber;
    /**
     * 2 3 CouncilTaxBenefitClaimReferenceNumber
     */
    private String CouncilTaxBenefitClaimReferenceNumber;
    //private Long CouncilTaxBenefitClaimReferenceNumber;
    /**
     * 3 4 ClaimantsNationalInsuranceNumber
     */
    private String ClaimantsNationalInsuranceNumber;
    //private String ClaimantsTitle;
    //private String ClaimantsSurname;
    //private String ClaimantsFirstForename;
    /**
     * 4 8 ClaimantsDateOfBirth
     */
    private String ClaimantsDateOfBirth;
    /**
     * 5 9 TenancyType
     */
    private int TenancyType;
    //private String ClaimantsAddressLine1;
    /**
     * 6 11 ClaimantsPostcode
     */
    private String ClaimantsPostcode;
    /**
     * 7 12 PassportedStandardIndicator
     */
    private int PassportedStandardIndicator;
    /**
     * 8 13 NumberOfChildDependents
     */
    private int NumberOfChildDependents;
    /**
     * 9 14 NumberOfNonDependents
     */
    private int NumberOfNonDependents;
    // Empty Field
    /**
     * 11 16 NonDependentStatus
     */
    private int NonDependentStatus;
    /**
     * 12 17 NonDependentDeductionAmountApplied
     */
    private int NonDependentDeductionAmountApplied;
    // Empty Fields
    /**
     * 28 33 StatusOfHBClaimAtExtractDate
     */
    private int StatusOfHBClaimAtExtractDate;
    /**
     * 29 34 StatusOfCTBClaimAtExtractDate
     */
    private int StatusOfCTBClaimAtExtractDate;
    /**
     * 30 35 DateMostRecentHBClaimWasReceived
     */
    private String DateMostRecentHBClaimWasReceived;
    /**
     * 31 36 DateMostRecentCTBClaimWasReceived
     */
    private String DateMostRecentCTBClaimWasReceived;
    /**
     * 32 37 DateOfFirstDecisionOnMostRecentHBClaim
     */
    private String DateOfFirstDecisionOnMostRecentHBClaim;
    /**
     * 33 38 DateOfFirstDecisionOnMostRecentCTBClaim
     */
    private String DateOfFirstDecisionOnMostRecentCTBClaim;
    /**
     * 34 39 OutcomeOfFirstDecisionOnMostRecentHBClaim
     */
    private int OutcomeOfFirstDecisionOnMostRecentHBClaim;
    /**
     * 35 40 OutcomeOfFirstDecisionOnMostRecentCTBClaim
     */
    private int OutcomeOfFirstDecisionOnMostRecentCTBClaim;
    /**
     * 36 41 HBClaimEntitlementStartDate
     */
    private String HBClaimEntitlementStartDate;
    // Empty Field
    /**
     * 38 43 WeeklyHousingBenefitEntitlement
     */
    private int WeeklyHousingBenefitEntitlement;
    /**
     * 39 44 WeeklyCouncilTaxBenefitEntitlement
     */
    private int WeeklyCouncilTaxBenefitEntitlement;
    /**
     * 40 45 FrequencyOfPaymentOfHB
     */
    private int FrequencyOfPaymentOfHB;
    /**
     * 41 46 FrequencyOfPaymentOfCTB
     */
    private int FrequencyOfPaymentOfCTB;
    /**
     * 42 47 PreDeterminationAmountOfHB
     */
    private int PreDeterminationAmountOfHB;
    /**
     * 43 48 PreDeterminationAmountOfCTB
     */
    private int PreDeterminationAmountOfCTB;
    // Empty Field
    /**
     * 45 50 WeeklyHBEntitlementBeforeChange
     */
    private int WeeklyHBEntitlementBeforeChange;
    // Empty Field
    /**
     * 47 52 WeeklyCTBEntitlementBeforeChange
     */
    private int WeeklyCTBEntitlementBeforeChange;
    /**
     * 48 53 ReasonForDirectPayment
     */
    private int ReasonForDirectPayment;
    /**
     * 49 54 TimingOfPaymentOfRentAllowance
     */
    private int TimingOfPaymentOfRentAllowance;
    /**
     * 50 55 ExtendedPaymentCase
     */
    private int ExtendedPaymentCase;
    /**
     * 51 56 CouncilTaxBand
     */
    private String CouncilTaxBand;
    /**
     * 52 57 WeeklyEligibleRentAmount
     */
    private int WeeklyEligibleRentAmount;
    /**
     * 53 58 WeeklyEligibleCouncilTaxAmount
     */
    private int WeeklyEligibleCouncilTaxAmount;
    /**
     * 54 59 ClaimantsStudentIndicator
     */
    private String ClaimantsStudentIndicator;
    /**
     * 55 60 SecondAdultRebate
     */
    private int SecondAdultRebate;
    /**
     * 56 61 RebatePercentageWhereASecondAdultRebateHasBeenAwarded
     */
    private int RebatePercentageWhereASecondAdultRebateHasBeenAwarded;
    /**
     * 57 62 LHARegulationsApplied
     */
    private String LHARegulationsApplied;
    /**
     * 58 63 IsThisCaseSubjectToLRROrSRRSchemes
     */
    private int IsThisCaseSubjectToLRROrSRRSchemes;
    /**
     * 59 64 WeeklyLocalReferenceRent
     */
    private int WeeklyLocalReferenceRent;
    /**
     * 60 65 WeeklySingleRoomRent
     */
    private int WeeklySingleRoomRent;
    /**
     * 61 66 WeeklyClaimRelatedRent
     */
    private int WeeklyClaimRelatedRent;
    /**
     * 62 67 RentOfficerDeterminationOfIneligibleCharges
     */
    private int RentOfficerDeterminationOfIneligibleCharges;
    /**
     * 63 68 WeeklyMaximumRent
     */
    private int WeeklyMaximumRent;
    /**
     * 64 69 TotalDeductionForMeals
     */
    private int TotalDeductionForMeals;
    /**
     * 65 70 WeeklyAdditionalDiscretionaryPayment
     */
    private int WeeklyAdditionalDiscretionaryPayment;
    /**
     * 66 71 ThirteenOrFiftyTwoWeekProtectionApplies
     */
    private int ThirteenOrFiftyTwoWeekProtectionApplies;
    /**
     * 67 72 DateOfFirstPaymentOnMostRecentHBClaimFollowingAFullDecision
     */
    private String DateOfFirstPaymentOnMostRecentHBClaimFollowingAFullDecision;
    /**
     * 68 73 ClaimantsAssessedIncomeFigure
     */
    private int ClaimantsAssessedIncomeFigure;
    /**
     * 69 74 ClaimantsAdjustedAssessedIncomeFigure
     */
    private int ClaimantsAdjustedAssessedIncomeFigure;
    /**
     * 70 75 ClaimantsTotalCapital
     */
    private int ClaimantsTotalCapital;
    /**
     * 71 76 ClaimantsGrossWeeklyIncomeFromEmployment
     */
    private int ClaimantsGrossWeeklyIncomeFromEmployment;
    /**
     * 72 77 ClaimantsNetWeeklyIncomeFromEmployment
     */
    private int ClaimantsNetWeeklyIncomeFromEmployment;
    /**
     * 73 78 ClaimantsGrossWeeklyIncomeFromSelfEmployment
     */
    private int ClaimantsGrossWeeklyIncomeFromSelfEmployment;
    /**
     * 74 79 ClaimantsNetWeeklyIncomeFromSelfEmployment
     */
    private int ClaimantsNetWeeklyIncomeFromSelfEmployment;
    /**
     * 75 80 ClaimantsTotalAmountOfEarningsDisregarded
     */
    private int ClaimantsTotalAmountOfEarningsDisregarded;
    /**
     * 76 81 ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded
     */
    private int ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded;
    /**
     * 77 82 ClaimantsIncomeFromAttendanceAllowance
     */
    private int ClaimantsIncomeFromAttendanceAllowance;
    /**
     * 78 83 ClaimantsIncomeFromBusinessStartUpAllowance
     */
    private int ClaimantsIncomeFromBusinessStartUpAllowance;
    /**
     * 79 84 ClaimantsIncomeFromChildBenefit
     */
    private int ClaimantsIncomeFromChildBenefit;
    /**
     * 80 85 ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent
     */
    private int ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent;
    /**
     * 81 86 ClaimantsIncomeFromPersonalPension
     */
    private int ClaimantsIncomeFromPersonalPension;
    /**
     * 82 87 ClaimantsIncomeFromSevereDisabilityAllowance
     */
    private int ClaimantsIncomeFromSevereDisabilityAllowance;
    /**
     * 83 88 ClaimantsIncomeFromMaternityAllowance
     */
    private int ClaimantsIncomeFromMaternityAllowance;
    /**
     * 84 89 ClaimantsIncomeFromContributionBasedJobSeekersAllowance
     */
    private int ClaimantsIncomeFromContributionBasedJobSeekersAllowance;
    /**
     * 85 90 ClaimantsIncomeFromStudentGrantLoan
     */
    private int ClaimantsIncomeFromStudentGrantLoan;
    /**
     * 86 91 ClaimantsIncomeFromSubTenants
     */
    private int ClaimantsIncomeFromSubTenants;
    /**
     * 87 92 ClaimantsIncomeFromBoarders
     */
    private int ClaimantsIncomeFromBoarders;
    /**
     * 88 93 ClaimantsIncomeFromTrainingForWorkCommunityAction
     */
    private int ClaimantsIncomeFromTrainingForWorkCommunityAction;
    /**
     * 89 94 ClaimantsIncomeFromIncapacityBenefitShortTermLower
     */
    private int ClaimantsIncomeFromIncapacityBenefitShortTermLower;
    /**
     * 90 95 ClaimantsIncomeFromIncapacityBenefitShortTermHigher
     */
    private int ClaimantsIncomeFromIncapacityBenefitShortTermHigher;
    /**
     * 91 96 ClaimantsIncomeFromIncapacityBenefitLongTerm
     */
    private int ClaimantsIncomeFromIncapacityBenefitLongTerm;
    /**
     * 92 97 ClaimantsIncomeFromNewDeal50PlusEmploymentCredit
     */
    private int ClaimantsIncomeFromNewDeal50PlusEmploymentCredit;
    /**
     * 93 98 ClaimantsIncomeFromNewTaxCredits
     */
    private int ClaimantsIncomeFromNewTaxCredits;
    /**
     * 94 99 ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent
     */
    private int ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent;
    /**
     * 95 100 ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent
     */
    private int ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent;
    /**
     * 96 101 ClaimantsIncomeFromGovernemntTraining
     */
    private int ClaimantsIncomeFromGovernemntTraining;
    /**
     * 97 102 ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit
     */
    private int ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit;
    /**
     * 98 103 ClaimantsIncomeFromCarersAllowance
     */
    private int ClaimantsIncomeFromCarersAllowance;
    /**
     * 99 104 ClaimantsIncomeFromStatutoryMaternityPaternityPay
     */
    private int ClaimantsIncomeFromStatutoryMaternityPaternityPay;
    /**
     * 100 105
     * ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc
     */
    private int ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc;
    /**
     * 101 106 ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP
     */
    private int ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP;
    /**
     * 102 107 ClaimantsIncomeFromWarMobilitySupplement
     */
    private int ClaimantsIncomeFromWarMobilitySupplement;
    /**
     * 103 108 ClaimantsIncomeFromWidowsWidowersPension
     */
    private int ClaimantsIncomeFromWidowsWidowersPension;
    /**
     * 104 109 ClaimantsIncomeFromBereavementAllowance
     */
    private int ClaimantsIncomeFromBereavementAllowance;
    /**
     * 105 110 ClaimantsIncomeFromWidowedParentsAllowance
     */
    private int ClaimantsIncomeFromWidowedParentsAllowance;
    /**
     * 106 111 ClaimantsIncomeFromYouthTrainingScheme
     */
    private int ClaimantsIncomeFromYouthTrainingScheme;
    /**
     * 107 112 ClaimantsIncomeFromStatuatorySickPay
     */
    private int ClaimantsIncomeFromStatuatorySickPay;
    /**
     * 108 113 ClaimantsOtherIncome
     */
    private int ClaimantsOtherIncome;
    /**
     * 109 114 ClaimantsTotalAmountOfIncomeDisregarded
     */
    private int ClaimantsTotalAmountOfIncomeDisregarded;
    /**
     * 110 115 FamilyPremiumAwarded
     */
    private int FamilyPremiumAwarded;
    /**
     * 111 116 FamilyLoneParentPremiumAwarded
     */
    private int FamilyLoneParentPremiumAwarded;
    /**
     * 112 117 DisabilityPremiumAwarded
     */
    private int DisabilityPremiumAwarded;
    /**
     * 113 118 SevereDisabilityPremiumAwarded
     */
    private int SevereDisabilityPremiumAwarded;
    /**
     * 114 119 DisabledChildPremiumAwarded
     */
    private int DisabledChildPremiumAwarded;
    /**
     * 115 120 CarePremiumAwarded
     */
    private int CarePremiumAwarded;
    /**
     * 116 121 EnhancedDisabilityPremiumAwarded
     */
    private int EnhancedDisabilityPremiumAwarded;
    /**
     * 117 122 BereavementPremiumAwarded
     */
    private int BereavementPremiumAwarded;
    /**
     * 118 123 PartnerFlag
     */
    private int PartnerFlag;
    /**
     * 119 124 PartnersStartDate
     */
    private String PartnersStartDate;
    /**
     * 120 125 PartnersEndDate
     */
    private String PartnersEndDate;
    /**
     * 121 126 PartnersNationalInsuranceNumber
     */
    private String PartnersNationalInsuranceNumber;
    // PartnersTitle
    // PartnersSurname
    // PartnersForename
    /**
     * 122 130 PartnersStudentIndicator
     */
    private String PartnersStudentIndicator;
    /**
     * 123 131 PartnersAssessedIncomeFigure
     */
    private int PartnersAssessedIncomeFigure;
    /**
     * 124 132 PartnersAdjustedAssessedIncomeFigure
     */
    private int PartnersAdjustedAssessedIncomeFigure;
    /**
     * 125 133 PartnersGrossWeeklyIncomeFromEmployment
     */
    private int PartnersGrossWeeklyIncomeFromEmployment;
    /**
     * 126 134 PartnersNetWeeklyIncomeFromEmployment
     */
    private int PartnersNetWeeklyIncomeFromEmployment;
    /**
     * 127 135 PartnersGrossWeeklyIncomeFromSelfEmployment
     */
    private int PartnersGrossWeeklyIncomeFromSelfEmployment;
    /**
     * 128 136 PartnersNetWeeklyIncomeFromSelfEmployment
     */
    private int PartnersNetWeeklyIncomeFromSelfEmployment;
    /**
     * 129 137 PartnersTotalAmountOfEarningsDisregarded
     */
    private int PartnersTotalAmountOfEarningsDisregarded;
    /**
     * 130 138 PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded
     */
    private int PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded;
    /**
     * 131 139 PartnersIncomeFromAttendanceAllowance
     */
    private int PartnersIncomeFromAttendanceAllowance;
    /**
     * 132 140 PartnersIncomeFromBusinessStartUpAllowance
     */
    private int PartnersIncomeFromBusinessStartUpAllowance;
    /**
     * 133 140 PartnersIncomeFromChildBenefit
     */
    private int PartnersIncomeFromChildBenefit;
    /**
     * 134 141 PartnersIncomeFromPersonalPension
     */
    private int PartnersIncomeFromPersonalPension;
    /**
     * 135 142 PartnersIncomeFromSevereDisabilityAllowance
     */
    private int PartnersIncomeFromSevereDisabilityAllowance;
    /**
     * 136 143 PartnersIncomeFromMaternityAllowance
     */
    private int PartnersIncomeFromMaternityAllowance;
    /**
     * 137 144 PartnersIncomeFromContributionBasedJobSeekersAllowance
     */
    private int PartnersIncomeFromContributionBasedJobSeekersAllowance;
    /**
     * 138 145 PartnersIncomeFromStudentGrantLoan
     */
    private int PartnersIncomeFromStudentGrantLoan;
    /**
     * 139 146 PartnersIncomeFromSubTenants
     */
    private int PartnersIncomeFromSubTenants;
    /**
     * 140 147 PartnersIncomeFromBoarders
     */
    private int PartnersIncomeFromBoarders;
    /**
     * 141 148 PartnersIncomeFromTrainingForWorkCommunityAction
     */
    private int PartnersIncomeFromTrainingForWorkCommunityAction;
    /**
     * 142 150 PartnersIncomeFromIncapacityBenefitShortTermLower
     */
    private int PartnersIncomeFromIncapacityBenefitShortTermLower;
    /**
     * 143 151 PartnersIncomeFromIncapacityBenefitShortTermHigher
     */
    private int PartnersIncomeFromIncapacityBenefitShortTermHigher;
    /**
     * 144 152 PartnersIncomeFromIncapacityBenefitLongTerm
     */
    private int PartnersIncomeFromIncapacityBenefitLongTerm;
    /**
     * 145 153 PartnersIncomeFromNewDeal50PlusEmploymentCredit
     */
    private int PartnersIncomeFromNewDeal50PlusEmploymentCredit;
    /**
     * 146 154 PartnersIncomeFromNewTaxCredits
     */
    private int PartnersIncomeFromNewTaxCredits;
    /**
     * 147 155 PartnersIncomeFromDisabilityLivingAllowanceCareComponent
     */
    private int PartnersIncomeFromDisabilityLivingAllowanceCareComponent;
    /**
     * 148 156 PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent
     */
    private int PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent;
    /**
     * 149 157 PartnersIncomeFromGovernemntTraining
     */
    private int PartnersIncomeFromGovernemntTraining;
    /**
     * 150 158 PartnersIncomeFromIndustrialInjuriesDisablementBenefit
     */
    private int PartnersIncomeFromIndustrialInjuriesDisablementBenefit;
    /**
     * 151 159 PartnersIncomeFromCarersAllowance
     */
    private int PartnersIncomeFromCarersAllowance;
    /**
     * 152 160 PartnersIncomeFromStatuatorySickPay
     */
    private int PartnersIncomeFromStatuatorySickPay;
    /**
     * 153 160 PartnersIncomeFromStatutoryMaternityPaternityPay
     */
    private int PartnersIncomeFromStatutoryMaternityPaternityPay;
    /**
     * 154 161
     * PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc
     */
    private int PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc;
    /**
     * 155 163 PartnersIncomeFromWarDisablementPensionArmedForcesGIP
     */
    private int PartnersIncomeFromWarDisablementPensionArmedForcesGIP;
    /**
     * 156 164 PartnersIncomeFromWarMobilitySupplement
     */
    private int PartnersIncomeFromWarMobilitySupplement;
    /**
     * 157 165 PartnersIncomeFromWidowsWidowersPension
     */
    private int PartnersIncomeFromWidowsWidowersPension;
    /**
     * 158 166 PartnersIncomeFromBereavementAllowance
     */
    private int PartnersIncomeFromBereavementAllowance;
    /**
     * 159 167 PartnersIncomeFromWidowedParentsAllowance
     */
    private int PartnersIncomeFromWidowedParentsAllowance;
    /**
     * 160 168 PartnersIncomeFromYouthTrainingScheme
     */
    private int PartnersIncomeFromYouthTrainingScheme;
    /**
     * 161 169 PartnersOtherIncome
     */
    private int PartnersOtherIncome;
    /**
     * 162 170 PartnersTotalAmountOfIncomeDisregarded
     */
    private int PartnersTotalAmountOfIncomeDisregarded;
    /**
     * 163 171 DateOverPaymentDetectionActivityInitiatedOnCase
     */
    private String DateOverPaymentDetectionActivityInitiatedOnCase;
    /**
     * 164 172 ReasonForOverpaymentDetectionActivity
     */
    private int ReasonForOverpaymentDetectionActivity;
    /**
     * 165 173 MethodOfOverpayentDetectionActivity
     */
    private int MethodOfOverpayentDetectionActivity;
    /**
     * 166 174 DoesTheOverpaymentDetectionActivityConstituteAFullReview
     */
    private String DoesTheOverpaymentDetectionActivityConstituteAFullReview;
    /**
     * 167 175 DateOverPaymentDetectionActivityIsCompleted
     */
    private String DateOverPaymentDetectionActivityIsCompleted;
    /**
     * 168 176 OutcomeOfOverPaymentDetectionActivity
     */
    private int OutcomeOfOverPaymentDetectionActivity;
    /**
     * 169 177 ClaimantsGender
     */
    private String ClaimantsGender;
    /**
     * 170 178 PartnersDateOfBirth
     */
    private String PartnersDateOfBirth;
    /**
     * 171 179 RentAllowanceMethodOfPayment
     */
    private int RentAllowanceMethodOfPayment;
    /**
     * 172 180 RentAllowancePaymentDestination
     */
    private int RentAllowancePaymentDestination;
    /**
     * 173 181 ContractualRentAmount
     */
    private int ContractualRentAmount;
    /**
     * 174 182 TimePeriodContractualRentFigureCovers
     */
    private int TimePeriodContractualRentFigureCovers;
    /**
     * 175 183 ClaimantsIncomeFromPensionCreditSavingsCredit
     */
    private int ClaimantsIncomeFromPensionCreditSavingsCredit;
    /**
     * 176 184 PartnersIncomeFromPensionCreditSavingsCredit
     */
    private int PartnersIncomeFromPensionCreditSavingsCredit;
    /**
     * 177 185 ClaimantsIncomeFromMaintenancePayments
     */
    private int ClaimantsIncomeFromMaintenancePayments;
    /**
     * 178 186 PartnersIncomeFromMaintenancePayments
     */
    private int PartnersIncomeFromMaintenancePayments;
    /**
     * 179 187 ClaimantsIncomeFromOccupationalPension
     */
    private int ClaimantsIncomeFromOccupationalPension;
    /**
     * 180 188 PartnersIncomeFromOccupationalPension
     */
    private int PartnersIncomeFromOccupationalPension;
    /**
     * 181 189 ClaimantsIncomeFromWidowsBenefit
     */
    private int ClaimantsIncomeFromWidowsBenefit;
    /**
     * 182 190 PartnersIncomeFromWidowsBenefit
     */
    private int PartnersIncomeFromWidowsBenefit;
    /**
     * 193 201 CTBClaimEntitlementStartDate
     */
    private String CTBClaimEntitlementStartDate;
    // Empty Fields
    /**
     * 194 202 DateHBClaimClosedWithdrawnDecidedUnsuccessfulDefective
     */
    private String DateHBClaimClosedWithdrawnDecidedUnsuccessfulDefective;
    /**
     * 195 203 DateCTBClaimClosedWithdrawnDecidedUnsuccessfulDefective
     */
    private String DateCTBClaimClosedWithdrawnDecidedUnsuccessfulDefective;
    /**
     * 196 204 TotalNumberOfRooms
     */
    private int TotalNumberOfRooms;
    /**
     * 197 205 NonSelfContainedAccomodation
     */
    private int NonSelfContainedAccomodation;
    /**
     * 198 206 TypeOfLHANumberOfRoomsEntitedTo
     */
    private int TypeOfLHANumberOfRoomsEntitedTo;
    /**
     * 199 207 TransitionalProtectionFromNationalRolloutOfLHA
     */
    private int TransitionalProtectionFromNationalRolloutOfLHA;
    /**
     * 200 208 Locality
     */
    private String Locality;
    /**
     * 201 209 ValueOfLHA
     */
    private int ValueOfLHA;
    /**
     * 202 210 ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective
     */
    private int ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective;
    /**
     * 203 211 ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective
     */
    private int ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective;
    // ClaimantsTelephoneNumber
    /**
     * 204 213 PartnersGender
     */
    private String PartnersGender;
    /**
     * 205 214 NonDependantGrossWeeklyIncomeFromRemunerativeWork
     */
    private int NonDependantGrossWeeklyIncomeFromRemunerativeWork;
    // Empty Fields
    /**
     * 211 220 HBClaimTreatAsDateMade
     */
    private String HBClaimTreatAsDateMade;
    /**
     * 212 221 SourceOfMostRecentHBClaim
     */
    private int SourceOfTheMostRecentHBClaim;
    /**
     * 213 222 DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim
     */
    private int DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim;
    /**
     * 214 223 DateOfFirstHBPaymentRentAllowanceOnly
     */
    private String DateOfFirstHBPaymentRentAllowanceOnly;
    /**
     * 215 224 WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly
     */
    private int WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly;
    /**
     * 216 225 WasThereABackdatedAwardMadeOnTheHBClaim
     */
    private int WasThereABackdatedAwardMadeOnTheHBClaim;
    /**
     * 217 226 DateHBBackdatingFrom
     */
    private String DateHBBackdatingFrom;
    /**
     * 218 227 DateHBBackdatingTo
     */
    private String DateHBBackdatingTo;
    /**
     * 219 228 TotalAmountOfBackdatedHBAwarded
     */
    private int TotalAmountOfBackdatedHBAwarded;
    /**
     * 220 229 CTBClaimTreatAsMadeDate
     */
    private String CTBClaimTreatAsMadeDate;
    /**
     * 221 230 SourceOfTheMostRecentCTBClaim
     */
    private int SourceOfTheMostRecentCTBClaim;
    /**
     * 222 231 DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim
     */
    private int DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim;
    /**
     * 223 232 WasThereABackdatedAwardMadeOnTheCTBClaim
     */
    private int WasThereABackdatedAwardMadeOnTheCTBClaim;
    /**
     * 224 233 DateCTBBackdatingFrom
     */
    private String DateCTBBackdatingFrom;
    /**
     * 225 234 DateCTBBackdatingTo
     */
    private String DateCTBBackdatingTo;
    /**
     * 226 235 TotalAmountOfBackdatedCTBAwarded
     */
    private int TotalAmountOfBackdatedCTBAwarded;
    /**
     * 227 236 InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly
     */
    private int InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly;
    /**
     * 228 237 IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation
     */
    private int IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation;
    /**
     * 229 238
     * DateOfTransferFromLocalAuthorityLandlordToHousingAssociationRSLIfSubjectToStockTRansferOrLSVT
     */
    private String DateOfTransferFromLocalAuthorityLandlordToHousingAssociationRSLIfSubjectToStockTRansferOrLSVT;
    /**
     * 230 239 PartnersTotalCapital
     */
    private int PartnersTotalCapital;
    /**
     * 231 240 WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure
     */
    private int WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure;
    /**
     * 232 241 ClaimantsTotalHoursOfRemunerativeWorkPerWeek
     */
    private double ClaimantsTotalHoursOfRemunerativeWorkPerWeek;
    /**
     * 233 242 PartnersTotalHoursOfRemunerativeWorkPerWeek
     */
    private double PartnersTotalHoursOfRemunerativeWorkPerWeek;
    // Empty Fields
    /*
     * 236 245 TotalHBPaymentsCreditsSinceLastExtract
     */
    private int TotalHBPaymentsCreditsSinceLastExtract;
    /**
     * 237 246 TotalCTBPaymentsCreditsSinceLastExtract
     */
    private int TotalCTBPaymentsCreditsSinceLastExtract;
    /**
     * 238 247 ClaimantsEthnicGroup
     */
    private int ClaimantsEthnicGroup;
    /**
     * 239 248 NewWeeklyHBEntitlementAfterTheChange
     */
    private int NewWeeklyHBEntitlementAfterTheChange;
    /**
     * 240 249 NewWeeklyCTBEntitlementAfterTheChange
     */
    private int NewWeeklyCTBEntitlementAfterTheChange;
    /**
     * 241 250 TypeOfChange
     */
    private int TypeOfChange;
    /**
     * 242 251 DateLAFirstNotifiedOfChangeInClaimDetails
     */
    private String DateLAFirstNotifiedOfChangeInClaimDetails;
    //Empty Field
    /**
     * 244 253 DateChangeOfDetailsAreEffectiveFrom
     */
    private String DateChangeOfDetailsAreEffectiveFrom;
    /**
     * 245 254 IfNotAnnualUpratingHowWasTheChangeIdentified
     */
    private int IfNotAnnualUpratingHowWasTheChangeIdentified;
    /**
     * 246 255 DateSupercessionDecisionWasMadeOnTheHBClaim
     */
    private String DateSupercessionDecisionWasMadeOnTheHBClaim;
    /**
     * 247 256 DateSupercessionDecisionWasMadeOnTheCTBClaim
     */
    private String DateSupercessionDecisionWasMadeOnTheCTBClaim;
    //Empty Fields
    /**
     * 250 259 DateApplicationForRevisionReconsiderationReceived
     */
    private String DateApplicationForRevisionReconsiderationReceived;
    /**
     * 251 260 DateClaimantNotifiedAboutTheDecisionOfTheRevisionReconsideration
     */
    private String DateClaimantNotifiedAboutTheDecisionOfTheRevisionReconsideration;
    /**
     * 252 261 DateAppealApplicationWasLodged
     */
    private String DateAppealApplicationWasLodged;
    /**
     * 253 262 OutcomeOfAppealApplication
     */
    private int OutcomeOfAppealApplication;
    /**
     * 254 263 DateOfOutcomeOfAppealApplication
     */
    private String DateOfOutcomeOfAppealApplication;
    // Empty Fields
    /**
     * 260 269
     * DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentHBClaim
     */
    private String DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentHBClaim;
    /**
     * 261 270
     * DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentHBClaim
     */
    private String DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentHBClaim;
    /**
     * 262 271 DateCouncilTaxPayable
     */
    private String DateCouncilTaxPayable;
    /**
     * 263 272
     * DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentCTBClaim
     */
    private String DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentCTBClaim;
    /**
     * 264 273
     * DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentCTBClaim
     */
    private String DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentCTBClaim;
    /**
     * 265 274 SoftwareProvider
     */
    private int SoftwareProvider;
    /**
     * 266 275 StaffingFTE
     */
    private String StaffingFTE;
    /**
     * 267 276 ContractingOutHandlingAndMaintenanceOfHBCTB
     */
    private int ContractingOutHandlingAndMaintenanceOfHBCTB;
    /**
     * 268 277 ContractingOutCounterFraudWorkRelatingToHBCTB
     */
    private int ContractingOutCounterFraudWorkRelatingToHBCTB;
    /**
     * 269 278 NumberOfBedroomsForLHARolloutCasesOnly
     */
    private int NumberOfBedroomsForLHARolloutCasesOnly;
    // ClaimantsForename2
    // ClaimantsAddressLine2
    // ClaimantsAddressLine3
    // ClaimantsAddressLine4
    // PartnersForename2
    /**
     * 270 284 PartnersDateOfDeath
     */
    private String PartnersDateOfDeath;
    /**
     * 271 285 JointTenancyFlag
     */
    private int JointTenancyFlag;
    /**
     * 272 286 AppointeeFlag
     */
    private int AppointeeFlag;
    /**
     * 273 287 RentFreeWeeksIndicator
     */
    private int RentFreeWeeksIndicator;
    //BankOrBuildingSocietySortCode
    //BankOrBuildingSocietyAccountNumber
    /**
     * 274 290 LastPaidToDate
     */
    private String LastPaidToDate;
    /**
     * 275 291 WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
     */
    private int WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    /**
     * 276 292 ClaimantsWeeklyIncomeFromESABasicElement
     */
    private int ClaimantsWeeklyIncomeFromESABasicElement;
    /**
     * 277 293 PartnersWeeklyIncomeFromESABasicElement
     */
    private int PartnersWeeklyIncomeFromESABasicElement;
    /**
     * 278 294 ClaimantsWeeklyIncomeFromESAWRAGElement
     */
    private int ClaimantsWeeklyIncomeFromESAWRAGElement;
    /**
     * 279 295 PartnersWeeklyIncomeFromESAWRAGElement
     */
    private int PartnersWeeklyIncomeFromESAWRAGElement;
    /**
     * 280 296 ClaimantsWeeklyIncomeFromESASCGElement
     */
    private int ClaimantsWeeklyIncomeFromESASCGElement;
    /**
     * 281 297 PartnersWeeklyIncomeFromESASCGElement
     */
    private int PartnersWeeklyIncomeFromESASCGElement;
    /**
     * 282 298 WRAGPremiumFlag
     */
    private String WRAGPremiumFlag;
    /**
     * 283 299 SCGPremiumFlag
     */
    private String SCGPremiumFlag;
    //LandlordsTitle
    //LandlordsSurname
    //LandlordsForename
    //LandlordsAddressLine1
    //LandlordsAddressLine2
    //LandlordsAddressLine3
    //LandlordsAddressLine4
    /**
     * In type1 but not type0 283 307
     */
    private String LandlordPostcode;
    /**
     * 284 308 SubRecordType
     */
    private int SubRecordType;
    /**
     * 285 309 SubRecordChildReferenceNumberOrNINO
     */
    private String SubRecordChildReferenceNumberOrNINO;
    /**
     * 286 310 SubRecordStartDate
     */
    private String SubRecordStartDate;
    /**
     * 287 311 SubRecordEndDate
     */
    private String SubRecordEndDate;
    // SubRecordTitle
    // SubRecordSurname
    // SubRecordForename
    /**
     * In type1 but not type0 288 315 SubRecordDateOfBirth
     */
    private String SubRecordDateOfBirth;
    /**
     * 289 316 IfThisActivityResolvesAnHBMSReferralProvideRMSNumber
     */
    private String IfThisActivityResolvesAnHBMSReferralProvideRMSNumber;
    /**
     * 290 317 HBMSRuleScanID
     */
    private String HBMSRuleScanID;
    /**
     * 291 318 DateOfHBMSMatch
     */
    private String DateOfHBMSMatch;
    /**
     * 292 319
     * IfResolutionOfHBMSReferralDoesNotResultInAFinancialAdjustmentPleaseIndicateTheReasonWhy
     */
    private String IfResolutionOfHBMSReferralDoesNotResultInAFinancialAdjustmentPleaseIndicateTheReasonWhy;
    /**
     * 293 320 UniqueTRecordIdentifier
     */
    private String UniqueTRecordIdentifier;
    /**
     * 294 321 OverpaymentReasonCapital
     */
    private String OverpaymentReasonCapital;
    /**
     * 295 322 OverpaymentReasonClaimentPartnersEarnedIncome
     */
    private String OverpaymentReasonClaimentPartnersEarnedIncome;
    /**
     * 296 323 OverpaymentReasonNonDependentsEarnedIncome
     */
    private String OverpaymentReasonNonDependentsEarnedIncome;
    /**
     * 297 324 OverpaymentReasonPassportingStatus
     */
    private String OverpaymentReasonPassportingStatus;
    /**
     * 298 325 OverpaymentReasonIncomeFromDWPBenefits
     */
    private String OverpaymentReasonIncomeFromDWPBenefits;
    /**
     * 299 326 OverpaymentReasonTaxCredits
     */
    private String OverpaymentReasonTaxCredits;
    /**
     * 300 327 OverpaymentReasonOtherIncome
     */
    private String OverpaymentReasonOtherIncome;
    /**
     * 301 328 OverpaymentReasonLivingTogetherAsHusbandAndWife
     */
    private String OverpaymentReasonLivingTogetherAsHusbandAndWife;
    /**
     * 302 329 OverpaymentReasonNumberOfNonDependents
     */
    private String OverpaymentReasonNumberOfNonDependents;
    /**
     * 303 330 OverpaymentReasonNumberOfDependents
     */
    private String OverpaymentReasonNumberOfDependents;
    /**
     * 304 331 OverpaymentReasonNonResidence
     */
    private String OverpaymentReasonNonResidence;
    /**
     * 305 332 OverpaymentReasonEligibleRentCouncilTax
     */
    private String OverpaymentReasonEligibleRentCouncilTax;
    /**
     * 306 333 OverpaymentReasonIneligible
     */
    private String OverpaymentReasonIneligible;
    /**
     * 307 334 OverpaymentReasonIdentityDeath
     */
    private String OverpaymentReasonIdentityDeath;
    /**
     * 308 335 OverpaymentReasonOther
     */
    private String OverpaymentReasonOther;
    /**
     * 309 336 BenefitThatThisPaymentErrorRelatesTo
     */
    private String BenefitThatThisPaymentErrorRelatesTo;
    /**
     * 310 337 TotalValueOfPaymentError
     */
    private long TotalValueOfPaymentError;
    /**
     * 311 338 WeeklyBenefitDiscrepancyAtStartOfPaymentError
     */
    private String WeeklyBenefitDiscrepancyAtStartOfPaymentError;
    /**
     * 312 339 StartDateOfPaymentErrorPeriod
     */
    private String StartDateOfPaymentErrorPeriod;
    /**
     * 313 340 EndDateOfPaymentErrorPeriod
     */
    private String EndDateOfPaymentErrorPeriod;
    /**
     * 314 341 WhatWasTheCauseOfTheOverPayment
     */
    private String WhatWasTheCauseOfTheOverPayment;

    /**
     * Creates a null record in case this is needed
     *
     * @param RecordID
     */
    public DW_SHBE_RecordOld(
            long RecordID) {
        this.RecordID = RecordID;
    }

    /**
     * @param RecordID
     * @param type ------------------------------------------------------------
     * The type is worked out by reading the first line of the data. type1 has:
     * LandlordPostcode and SubRecordDateOfBirth for S Record; and,
     * LandlordPostcode for D Record.
     * type0-------------------------------------------------------------------
     * 1,2,3,4,8,9, 11,12,13,14,15,16,17,18,19, 20,21,22,23,24,25,26,27,28,29,
     * 30,31,32,33,34,35,36,37,38,39, 40,41,42,43,44,45,46,47,48,49,
     * 50,51,52,53,54,55,56,57,58,59, 60,61,62,63,64,65,66,67,68,69,
     * 70,71,72,73,74,75,76,77,78,79, 80,81,82,83,84,85,86,87,88,89,
     * 90,91,92,93,94,95,96,97,98,99, 100,101,102,103,104,105,106,107,108,109,
     * 110,111,112,113,114,115,116,117,118,119, 120,121,122,123,124,125,126,
     * 130,131,132,133,134,135,136,137,138,139,
     * 140,141,142,143,144,145,146,147,148,149,
     * 150,151,152,153,154,155,156,157,158,159,
     * 160,161,162,163,164,165,166,167,168,169,
     * 170,171,172,173,174,175,176,177,178,179,
     * 180,181,182,183,184,185,186,187,188,189,
     * 190,191,192,193,194,195,196,197,198,199,
     * 200,201,202,203,204,205,206,207,208,209,
     * 210,211,213,214,215,216,217,218,219,
     * 220,221,222,223,224,225,226,227,228,229,
     * 230,231,232,233,234,235,236,237,238,239,
     * 240,241,242,243,244,245,246,247,248,249,
     * 250,251,252,253,254,255,256,257,258,259,
     * 260,261,262,263,264,265,266,267,268,269,
     * 270,271,272,273,274,275,276,277,278, 284,285,286,287,
     * 290,291,292,293,294,295,296,297,298,299, 308,309,
     * 310,311,316,317,318,319, 320,321,322,323,324,325,326,327,328,329,
     * 330,331,332,333,334,335,336,337,338,339, 340,341
     * type1-------------------------------------------------------------------
     * 1,2,3,4,8,9, 11,12,13,14,15,16,17,18,19, 20,21,22,23,24,25,26,27,28,29,
     * 30,31,32,33,34,35,36,37,38,39, 40,41,42,43,44,45,46,47,48,49,
     * 50,51,52,53,54,55,56,57,58,59, 60,61,62,63,64,65,66,67,68,69,
     * 70,71,72,73,74,75,76,77,78,79, 80,81,82,83,84,85,86,87,88,89,
     * 90,91,92,93,94,95,96,97,98,99, 100,101,102,103,104,105,106,107,108,109,
     * 110,111,112,113,114,115,116,117,118,119, 120,121,122,123,124,125,126,
     * 130,131,132,133,134,135,136,137,138,139,
     * 140,141,142,143,144,145,146,147,148,149,
     * 150,151,152,153,154,155,156,157,158,159,
     * 160,161,162,163,164,165,166,167,168,169,
     * 170,171,172,173,174,175,176,177,178,179,
     * 180,181,182,183,184,185,186,187,188,189,
     * 190,191,192,193,194,195,196,197,198,199,
     * 200,201,202,203,204,205,206,207,208,209,
     * 210,211,213,214,215,216,217,218,219,
     * 220,221,222,223,224,225,226,227,228,229,
     * 230,231,232,233,234,235,236,237,238,239,
     * 240,241,242,243,244,245,246,247,248,249,
     * 250,251,252,253,254,255,256,257,258,259,
     * 260,261,262,263,264,265,266,267,268,269,
     * 270,271,272,273,274,275,276,277,278, 284,285,286,287,
     * 290,291,292,293,294,295,296,297,298,299, 307,308,309,
     * 310,311,315,316,317,318,319, 320,321,322,323,324,325,326,327,328,329,
     * 330,331,332,333,334,335,336,337,338,339, 340,341
     * type1-------------------------------------------------------------------
     * 1,2,3,4,8,9,11,12,13,14,15,16,17,18,19, 20,21,22,23,24,25,26,27,28,29,
     * 30,31,32,33,34,35,36,37,38,39, 40,41,42,43,44,45,46,47,48,49,
     * 50,51,52,53,54,55,56,57,58,59, 60,61,62,63,64,65,66,67,68,69,
     * 70,71,72,73,74,75,76,77,78,79, 80,81,82,83,84,85,86,87,88,89,
     * 90,91,92,93,94,95,96,97,98,99, 100,101,102,103,104,105,106,107,108,109,
     * 110,111,112,113,114,115,116,117,118,119, 120,121,122,123,124,125,126,
     * 130,131,132,133,134,135,136,137,138,139,
     * 140,141,142,143,144,145,146,147,148,149,
     * 150,151,152,153,154,155,156,157,158,159,
     * 160,161,162,163,164,165,166,167,168,169,
     * 170,171,172,173,174,175,176,177,178,179,
     * 180,181,182,183,184,185,186,187,188,189,
     * 190,191,192,193,194,195,196,197,198,199,
     * 200,201,202,203,204,205,206,207,208,209,
     * 210,211,213,214,215,216,217,218,219,
     * 220,221,222,223,224,225,226,227,228,229,
     * 230,231,232,233,234,235,236,237,238,239,
     * 240,241,242,243,244,245,246,247,248,249,
     * 250,251,252,253,254,255,256,257,258,259,
     * 260,261,262,263,264,265,266,267,268,269,
     * 270,271,272,273,274,275,276,277,278, 284,285,286,287,
     * 290,291,292,293,294,295,296,297,298,299, 307,308,309,
     * 310,311,315,316,317,318,319, 320,321,322,323,324,325,326,327,328,329,
     * 330,331,332,333,334,335,336,337,338,339, 340,341
     * @param line
     * @param handler
     * @throws java.lang.Exception
     */
    public DW_SHBE_RecordOld(
            long RecordID,
            int type,
            String line,
            DW_SHBE_Handler handler) throws Exception {
        this.RecordID = RecordID;
        if (line.startsWith("S")) {
            //System.out.println("S Record");
            generateSRecord(
                    type,
                    line,
                    handler);
        } else {
            //line.startsWith("D");
            //System.out.println("D Record");
            generateDRecord(
                    type,
                    line,
                    handler);
        }
    }

    private void generateSRecord(
            int type,
            String line,
            DW_SHBE_Handler handler) throws Exception {
        String[] fields = line.split(",");
        int n = 0;
//        int expectedFieldsLength = 290;
//        try {
//            if (fields.length != expectedFieldsLength) {
//                System.out.println("fields.length " + fields.length);
//                System.out.println("RecordID " + RecordID);
//                System.out.println(line);
//            }
        if (n < fields.length) {
            RecordType = fields[n];
            if (!handler.getRecordTypes().contains(RecordType)) {
                System.err.println("RecordType " + RecordType);
                System.err.println("!handler.getRecordTypes().contains(RecordType)");
                System.err.println("handler.toString() " + handler.toString());
                throw new Exception("!handler.getRecordTypes().contains(RecordType)");
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setHousingBenefitClaimReferenceNumber(fields[n]);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setCouncilTaxBenefitClaimReferenceNumber(fields[n]);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setClaimantsNationalInsuranceNumber(fields[n]);
        } else {
            return;
        }
//            n++;
//            if (n < fields.length) {
//            setClaimantsTitle(fields[n]);
//            } else {
//                return;
//            }
//            n++;
//            if (n < fields.length) {
//            ClaimantsSurname(fields[n]);
//            } else {
//                return;
//            }
//            n++;
//            if (n < fields.length) {
//            ClaimantsFirstForename(fields[n]);
//            } else {
//                return;
//            }
        n++;
        if (n < fields.length) {
            setClaimantsDateOfBirth(fields[n]);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setTenancyType(n, fields);
        } else {
            return;
        }
//            n++;
//            if (n < fields.length) {
//                setClaimantsAddressLine1(fields[n]);
//            } else {
//                return;
//            }
        n++;
        if (n < fields.length) {
            setClaimantsPostcode(fields[n]);
        } else {
            return;
        }
        n++; //7
        if (n < fields.length) {
            setPassportedStandardIndicator(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                NumberOfChildDependents = 0;
            } else {
                NumberOfChildDependents = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                NumberOfNonDependents = 0;
            } else {
                NumberOfNonDependents = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n += 2; //10
        if (n < fields.length) {
            setNonDependentStatus(n, fields);
        } else {
            return;
        }
        n++; //12
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                NonDependentDeductionAmountApplied = 0;
            } else {
                NonDependentDeductionAmountApplied = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n = 28;
        if (n < fields.length) {
            setStatusOfHBClaimAtExtractDate(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setStatusOfCTBClaimAtExtractDate(n, fields);
        } else {
            return;
        }
        n++; //30
        if (n < fields.length) {
            DateMostRecentHBClaimWasReceived = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateMostRecentCTBClaimWasReceived = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateOfFirstDecisionOnMostRecentHBClaim = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateOfFirstDecisionOnMostRecentCTBClaim = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setOutcomeOfFirstDecisionOnMostRecentHBClaim(n, fields);
        } else {
            return;
        }
        n++; //35
        if (n < fields.length) {
            setOutcomeOfFirstDecisionOnMostRecentCTBClaim(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            HBClaimEntitlementStartDate = fields[n];
        } else {
            return;
        }
        n += 2;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyHousingBenefitEntitlement = 0;
            } else {
                WeeklyHousingBenefitEntitlement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyCouncilTaxBenefitEntitlement = 0;
            } else {
                WeeklyCouncilTaxBenefitEntitlement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //40
        if (n < fields.length) {
            setFrequencyOfPaymentOfHB(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setFrequencyOfPaymentOfCTB(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PreDeterminationAmountOfHB = 0;
            } else {
                PreDeterminationAmountOfHB = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //43
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PreDeterminationAmountOfCTB = 0;
            } else {
                PreDeterminationAmountOfCTB = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n += 2; //45
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyHBEntitlementBeforeChange = 0;
            } else {
                WeeklyHBEntitlementBeforeChange = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n += 2; //47
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyCTBEntitlementBeforeChange = 0;
            } else {
                WeeklyCTBEntitlementBeforeChange = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setReasonForDirectPayment(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setTimingOfPaymentOfRentAllowance(n, fields);
        } else {
            return;
        }
        n++; //50
        if (n < fields.length) {
            setExtendedPaymentCase(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            CouncilTaxBand = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyEligibleRentAmount = 0;
            } else {
                WeeklyEligibleRentAmount = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyEligibleCouncilTaxAmount = 0;
            } else {
                WeeklyEligibleCouncilTaxAmount = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].equalsIgnoreCase("Y")) {
                ClaimantsStudentIndicator = "Y";
            } else {
                ClaimantsStudentIndicator = "N";
            }
        } else {
            return;
        }
        n++; //55
        if (n < fields.length) {
            setSecondAdultRebate(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setRebatePercentageWhereASecondAdultRebateHasBeenAwarded(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                LHARegulationsApplied = "No";
            } else {
                LHARegulationsApplied = "Yes";
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setIsThisCaseSubjectToLRROrSRRSchemes(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyLocalReferenceRent = 0;
            } else {
                WeeklyLocalReferenceRent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //60
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklySingleRoomRent = 0;
            } else {
                WeeklySingleRoomRent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyClaimRelatedRent = 0;
            } else {
                WeeklyClaimRelatedRent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                RentOfficerDeterminationOfIneligibleCharges = 0;
            } else {
                RentOfficerDeterminationOfIneligibleCharges = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyMaximumRent = 0;
            } else {
                WeeklyMaximumRent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                TotalDeductionForMeals = 0;
            } else {
                TotalDeductionForMeals = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //65
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyAdditionalDiscretionaryPayment = 0;
            } else {
                WeeklyAdditionalDiscretionaryPayment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setThirteenOrFiftyTwoWeekProtectionApplies(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateOfFirstPaymentOnMostRecentHBClaimFollowingAFullDecision = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsAssessedIncomeFigure = 0;
            } else {
                ClaimantsAssessedIncomeFigure = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsAdjustedAssessedIncomeFigure = 0;
            } else {
                ClaimantsAdjustedAssessedIncomeFigure = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //70
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsTotalCapital = 0;
            } else {
                ClaimantsTotalCapital = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsGrossWeeklyIncomeFromEmployment = 0;
            } else {
                ClaimantsGrossWeeklyIncomeFromEmployment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsNetWeeklyIncomeFromEmployment = 0;
            } else {
                ClaimantsNetWeeklyIncomeFromEmployment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsGrossWeeklyIncomeFromSelfEmployment = 0;
            } else {
                ClaimantsGrossWeeklyIncomeFromSelfEmployment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsNetWeeklyIncomeFromSelfEmployment = 0;
            } else {
                ClaimantsNetWeeklyIncomeFromSelfEmployment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //75
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsTotalAmountOfEarningsDisregarded = 0;
            } else {
                ClaimantsTotalAmountOfEarningsDisregarded = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = 0;
            } else {
                ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromAttendanceAllowance = 0;
            } else {
                ClaimantsIncomeFromAttendanceAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromBusinessStartUpAllowance = 0;
            } else {
                ClaimantsIncomeFromBusinessStartUpAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromChildBenefit = 0;
            } else {
                ClaimantsIncomeFromChildBenefit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //80
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent = 0;
            } else {
                ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromPersonalPension = 0;
            } else {
                ClaimantsIncomeFromPersonalPension = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromSevereDisabilityAllowance = 0;
            } else {
                ClaimantsIncomeFromSevereDisabilityAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromMaternityAllowance = 0;
            } else {
                ClaimantsIncomeFromMaternityAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromContributionBasedJobSeekersAllowance = 0;
            } else {
                ClaimantsIncomeFromContributionBasedJobSeekersAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //85
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromStudentGrantLoan = 0;
            } else {
                ClaimantsIncomeFromStudentGrantLoan = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromSubTenants = 0;
            } else {
                ClaimantsIncomeFromSubTenants = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromBoarders = 0;
            } else {
                ClaimantsIncomeFromBoarders = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromTrainingForWorkCommunityAction = 0;
            } else {
                ClaimantsIncomeFromTrainingForWorkCommunityAction = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromIncapacityBenefitShortTermLower = 0;
            } else {
                ClaimantsIncomeFromIncapacityBenefitShortTermLower = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //90
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromIncapacityBenefitShortTermHigher = 0;
            } else {
                ClaimantsIncomeFromIncapacityBenefitShortTermHigher = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromIncapacityBenefitLongTerm = 0;
            } else {
                ClaimantsIncomeFromIncapacityBenefitLongTerm = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromNewDeal50PlusEmploymentCredit = 0;
            } else {
                ClaimantsIncomeFromNewDeal50PlusEmploymentCredit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromNewTaxCredits = 0;
            } else {
                ClaimantsIncomeFromNewTaxCredits = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent = 0;
            } else {
                ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //95
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent = 0;
            } else {
                ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromGovernemntTraining = 0;
            } else {
                ClaimantsIncomeFromGovernemntTraining = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit = 0;
            } else {
                ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromCarersAllowance = 0;
            } else {
                ClaimantsIncomeFromCarersAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromStatutoryMaternityPaternityPay = 0;
            } else {
                ClaimantsIncomeFromStatutoryMaternityPaternityPay = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //100
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = 0;
            } else {
                ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP = 0;
            } else {
                ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromWarMobilitySupplement = 0;
            } else {
                ClaimantsIncomeFromWarMobilitySupplement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromWidowsWidowersPension = 0;
            } else {
                ClaimantsIncomeFromWidowsWidowersPension = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromBereavementAllowance = 0;
            } else {
                ClaimantsIncomeFromBereavementAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //105
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromWidowedParentsAllowance = 0;
            } else {
                ClaimantsIncomeFromWidowedParentsAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromYouthTrainingScheme = 0;
            } else {
                ClaimantsIncomeFromYouthTrainingScheme = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromStatuatorySickPay = 0;
            } else {
                ClaimantsIncomeFromStatuatorySickPay = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsOtherIncome = 0;
            } else {
                ClaimantsOtherIncome = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsTotalAmountOfIncomeDisregarded = 0;
            } else {
                ClaimantsTotalAmountOfIncomeDisregarded = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //110
        if (n < fields.length) {
            setFamilyPremiumAwarded(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setFamilyLoneParentPremiumAwarded(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setDisabilityPremiumAwarded(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setSevereDisabilityPremiumAwarded(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setDisabledChildPremiumAwarded(n, fields);
        } else {
            return;
        }
        n++; //115
        if (n < fields.length) {
            setCarePremiumAwarded(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setEnhancedDisabilityPremiumAwarded(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setBereavementPremiumAwarded(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setPartnerFlag(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            PartnersStartDate = fields[n];
        } else {
            return;
        }
        n++; //120
        if (n < fields.length) {
            PartnersEndDate = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            PartnersNationalInsuranceNumber = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersStudentIndicator = "N";
            } else {
                PartnersStudentIndicator = "Y";
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersAssessedIncomeFigure = 0;
            } else {
                PartnersAssessedIncomeFigure = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersAdjustedAssessedIncomeFigure = 0;
            } else {
                PartnersAdjustedAssessedIncomeFigure = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //125
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersGrossWeeklyIncomeFromEmployment = 0;
            } else {
                PartnersGrossWeeklyIncomeFromEmployment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersNetWeeklyIncomeFromEmployment = 0;
            } else {
                PartnersNetWeeklyIncomeFromEmployment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersGrossWeeklyIncomeFromSelfEmployment = 0;
            } else {
                PartnersGrossWeeklyIncomeFromSelfEmployment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersNetWeeklyIncomeFromSelfEmployment = 0;
            } else {
                PartnersNetWeeklyIncomeFromSelfEmployment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersTotalAmountOfEarningsDisregarded = 0;
            } else {
                PartnersTotalAmountOfEarningsDisregarded = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //130
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = 0;
            } else {
                PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromAttendanceAllowance = 0;
            } else {
                PartnersIncomeFromAttendanceAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromBusinessStartUpAllowance = 0;
            } else {
                PartnersIncomeFromBusinessStartUpAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromChildBenefit = 0;
            } else {
                PartnersIncomeFromChildBenefit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromPersonalPension = 0;
            } else {
                PartnersIncomeFromPersonalPension = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //135
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromSevereDisabilityAllowance = 0;
            } else {
                PartnersIncomeFromSevereDisabilityAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromMaternityAllowance = 0;
            } else {
                PartnersIncomeFromMaternityAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromContributionBasedJobSeekersAllowance = 0;
            } else {
                PartnersIncomeFromContributionBasedJobSeekersAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromStudentGrantLoan = 0;
            } else {
                PartnersIncomeFromStudentGrantLoan = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromSubTenants = 0;
            } else {
                PartnersIncomeFromSubTenants = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //140
        if (n < fields.length) {
            if (fields[n].trim().equalsIgnoreCase("")) {
                PartnersIncomeFromBoarders = 0;
            } else {
                PartnersIncomeFromBoarders = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromTrainingForWorkCommunityAction = 0;
            } else {
                PartnersIncomeFromTrainingForWorkCommunityAction = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromIncapacityBenefitShortTermLower = 0;
            } else {
                PartnersIncomeFromIncapacityBenefitShortTermLower = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromIncapacityBenefitShortTermHigher = 0;
            } else {
                PartnersIncomeFromIncapacityBenefitShortTermHigher = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromIncapacityBenefitLongTerm = 0;
            } else {
                PartnersIncomeFromIncapacityBenefitLongTerm = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //145
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromNewDeal50PlusEmploymentCredit = 0;
            } else {
                PartnersIncomeFromNewDeal50PlusEmploymentCredit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromNewTaxCredits = 0;
            } else {
                PartnersIncomeFromNewTaxCredits = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromDisabilityLivingAllowanceCareComponent = 0;
            } else {
                PartnersIncomeFromDisabilityLivingAllowanceCareComponent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent = 0;
            } else {
                PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromGovernemntTraining = 0;
            } else {
                PartnersIncomeFromGovernemntTraining = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //150
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromIndustrialInjuriesDisablementBenefit = 0;
            } else {
                PartnersIncomeFromIndustrialInjuriesDisablementBenefit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromCarersAllowance = 0;
            } else {
                PartnersIncomeFromCarersAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromStatuatorySickPay = 0;
            } else {
                PartnersIncomeFromStatuatorySickPay = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromStatutoryMaternityPaternityPay = 0;
            } else {
                PartnersIncomeFromStatutoryMaternityPaternityPay = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = 0;
            } else {
                PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //155
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromWarDisablementPensionArmedForcesGIP = 0;
            } else {
                PartnersIncomeFromWarDisablementPensionArmedForcesGIP = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromWarMobilitySupplement = 0;
            } else {
                PartnersIncomeFromWarMobilitySupplement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromWidowsWidowersPension = 0;
            } else {
                PartnersIncomeFromWidowsWidowersPension = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromBereavementAllowance = 0;
            } else {
                PartnersIncomeFromBereavementAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromWidowedParentsAllowance = 0;
            } else {
                PartnersIncomeFromWidowedParentsAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //160
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromYouthTrainingScheme = 0;
            } else {
                PartnersIncomeFromYouthTrainingScheme = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersOtherIncome = 0;
            } else {
                PartnersOtherIncome = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersTotalAmountOfIncomeDisregarded = 0;
            } else {
                PartnersTotalAmountOfIncomeDisregarded = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateOverPaymentDetectionActivityInitiatedOnCase = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setMethodOfOverpayentDetectionActivity(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setReasonForOverpaymentDetectionActivity(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DoesTheOverpaymentDetectionActivityConstituteAFullReview = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateOverPaymentDetectionActivityIsCompleted = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setOutcomeOfOverPaymentDetectionActivity(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            ClaimantsGender = fields[n];
        } else {
            return;
        }
        n++; //170
        if (n < fields.length) {
            PartnersDateOfBirth = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setRentAllowanceMethodOfPayment(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setRentAllowancePaymentDestination(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ContractualRentAmount = 0;
            } else {
                ContractualRentAmount = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setTimePeriodContractualRentFigureCovers(n, fields);
        } else {
            return;
        }
        n++; //175
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromPensionCreditSavingsCredit = 0;
            } else {
                ClaimantsIncomeFromPensionCreditSavingsCredit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromPensionCreditSavingsCredit = 0;
            } else {
                PartnersIncomeFromPensionCreditSavingsCredit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromMaintenancePayments = 0;
            } else {
                ClaimantsIncomeFromMaintenancePayments = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromMaintenancePayments = 0;
            } else {
                PartnersIncomeFromMaintenancePayments = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromOccupationalPension = 0;
            } else {
                ClaimantsIncomeFromOccupationalPension = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //180
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromOccupationalPension = 0;
            } else {
                PartnersIncomeFromOccupationalPension = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromWidowsBenefit = 0;
            } else {
                ClaimantsIncomeFromWidowsBenefit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //182
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromWidowsBenefit = 0;
            } else {
                PartnersIncomeFromWidowsBenefit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n = 193; //193
        if (n < fields.length) {
            CTBClaimEntitlementStartDate = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateHBClaimClosedWithdrawnDecidedUnsuccessfulDefective = fields[n];
        } else {
            return;
        }
        n++; //195
        if (n < fields.length) {
            DateCTBClaimClosedWithdrawnDecidedUnsuccessfulDefective = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                TotalNumberOfRooms = 0;
            } else {
                TotalNumberOfRooms = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setNonSelfContainedAccomodation(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                TypeOfLHANumberOfRoomsEntitedTo = 0;
            } else {
                TypeOfLHANumberOfRoomsEntitedTo = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setTransitionalProtectionFromNationalRolloutOfLHA(n, fields);
        } else {
            return;
        }
        n++; //200
        if (n < fields.length) {
            Locality = fields[n];
            n++;
            if (fields[n].trim().isEmpty()) {
                ValueOfLHA = 0;
            } else {
                ValueOfLHA = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            PartnersGender = fields[n];
        } else {
            return;
        }
        n++; //205
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                NonDependantGrossWeeklyIncomeFromRemunerativeWork = 0;
            } else {
                NonDependantGrossWeeklyIncomeFromRemunerativeWork = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n = 211;
        if (n < fields.length) {
            HBClaimTreatAsDateMade = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setSourceOfTheMostRecentHBClaim(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setDidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateOfFirstHBPaymentRentAllowanceOnly = fields[n];
        } else {
            return;
        }
        n++; //215
        if (n < fields.length) {
            setWasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setWasThereABackdatedAwardMadeOnTheHBClaim(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateHBBackdatingFrom = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateHBBackdatingTo = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                TotalAmountOfBackdatedHBAwarded = 0;
            } else {
                TotalAmountOfBackdatedHBAwarded = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //220
        if (n < fields.length) {
            CTBClaimTreatAsMadeDate = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setSourceOfTheMostRecentCTBClaim(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setDidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setWasThereABackdatedAwardMadeOnTheCTBClaim(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateCTBBackdatingFrom = fields[n];
        } else {
            return;
        }
        n++; //225
        if (n < fields.length) {
            DateCTBBackdatingTo = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                TotalAmountOfBackdatedCTBAwarded = 0;
            } else {
                TotalAmountOfBackdatedCTBAwarded = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setInThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setIfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateOfTransferFromLocalAuthorityLandlordToHousingAssociationRSLIfSubjectToStockTRansferOrLSVT = fields[n];
        } else {
            return;
        }
        n++; //230
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersTotalCapital = 0;
            } else {
                PartnersTotalCapital = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure = 0;
            } else {
                WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsTotalHoursOfRemunerativeWorkPerWeek = 0;
            } else {
                try {
                    ClaimantsTotalHoursOfRemunerativeWorkPerWeek = Double.valueOf(fields[n]);
                } catch (NumberFormatException e) {
                    int debug = 1;
                    e.printStackTrace(System.err);
                    throw e;
                }
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ") || fields[n].equalsIgnoreCase("  ")) {
                PartnersTotalHoursOfRemunerativeWorkPerWeek = 0;
            } else {
                try {
                    PartnersTotalHoursOfRemunerativeWorkPerWeek = Double.valueOf(fields[n]);
                } catch (NumberFormatException e) {
                    int debug = 1;
                    e.printStackTrace(System.err);
                    throw e;
                }
            }
        } else {
            return;
        }
        n = 236;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                TotalHBPaymentsCreditsSinceLastExtract = 0;
            } else {
                TotalHBPaymentsCreditsSinceLastExtract = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                TotalCTBPaymentsCreditsSinceLastExtract = 0;
            } else {
                TotalCTBPaymentsCreditsSinceLastExtract = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setClaimantsEthnicGroup(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                NewWeeklyHBEntitlementAfterTheChange = 0;
            } else {
                NewWeeklyHBEntitlementAfterTheChange = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //240
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                NewWeeklyCTBEntitlementAfterTheChange = 0;
            } else {
                NewWeeklyCTBEntitlementAfterTheChange = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setTypeOfChange(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateLAFirstNotifiedOfChangeInClaimDetails = fields[n];
            n += 2;
        } else {
            return;
        }
        if (n < fields.length) {
            DateChangeOfDetailsAreEffectiveFrom = fields[n];
        } else {
            return;
        }
        n++; //245
        if (n < fields.length) {
            setIfNotAnnualUpratingHowWasTheChangeIdentified(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateSupercessionDecisionWasMadeOnTheHBClaim = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateSupercessionDecisionWasMadeOnTheCTBClaim = fields[n];
        } else {
            return;
        }
        n += 3; //250
        if (n < fields.length) {
            DateApplicationForRevisionReconsiderationReceived = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateClaimantNotifiedAboutTheDecisionOfTheRevisionReconsideration = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateAppealApplicationWasLodged = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setOutcomeOfAppealApplication(n, fields);
        } else {
            return;
        }
        n++; //254
        if (n < fields.length) {
            DateOfOutcomeOfAppealApplication = fields[n];
        } else {
            return;
        }
        n += 6; //260
        if (n < fields.length) {
            DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentHBClaim = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentHBClaim = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateCouncilTaxPayable = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentCTBClaim = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentCTBClaim = fields[n];
        } else {
            return;
        }
        n++; //265
        if (n < fields.length) {
            setSoftwareProvider(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            StaffingFTE = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setContractingOutHandlingAndMaintenanceOfHBCTB(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setContractingOutCounterFraudWorkRelatingToHBCTB(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                NumberOfBedroomsForLHARolloutCasesOnly = 0;
            } else {
                NumberOfBedroomsForLHARolloutCasesOnly = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //270
        if (n < fields.length) {
            PartnersDateOfDeath = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setJointTenancyFlag(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setAppointeeFlag(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                RentFreeWeeksIndicator = 0;
            } else {
                RentFreeWeeksIndicator = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //274
        if (n < fields.length) {
            if (n < fields.length) {
                LastPaidToDate = fields[n];
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (n < fields.length) {
                if (fields[n].trim().isEmpty()) {
                    WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0;
                } else {
                    WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = Integer.valueOf(fields[n]);
                }
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsWeeklyIncomeFromESABasicElement = 0;
            } else {
                ClaimantsWeeklyIncomeFromESABasicElement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersWeeklyIncomeFromESABasicElement = 0;
            } else {
                PartnersWeeklyIncomeFromESABasicElement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsWeeklyIncomeFromESAWRAGElement = 0;
            } else {
                ClaimantsWeeklyIncomeFromESAWRAGElement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //279
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersWeeklyIncomeFromESAWRAGElement = 0;
            } else {
                PartnersWeeklyIncomeFromESAWRAGElement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsWeeklyIncomeFromESASCGElement = 0;
            } else {
                ClaimantsWeeklyIncomeFromESASCGElement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersWeeklyIncomeFromESASCGElement = 0;
            } else {
                PartnersWeeklyIncomeFromESASCGElement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            WRAGPremiumFlag = fields[n];
        } else {
            return;
        }
        n++; //283
        if (n < fields.length) {
            SCGPremiumFlag = fields[n];
        } else {
            return;
        }
        if (type == 1) {
            n++;
            if (n < fields.length) {
                LandlordPostcode = fields[n];
            } else {
                return;
            }
        }
        n++;
        if (n < fields.length) {
            setSubRecordType(n, fields);
        } else {
            return;
        }
        if (type == 1) {
            n++;
            if (n < fields.length) {
                SubRecordDateOfBirth = fields[n];
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            SubRecordChildReferenceNumberOrNINO = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            SubRecordStartDate = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            SubRecordEndDate = fields[n];
        } else {
            return;
        }
        if (type == 1) {
            n++;
            if (n < fields.length) {
                SubRecordDateOfBirth = fields[n];
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            IfThisActivityResolvesAnHBMSReferralProvideRMSNumber = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            HBMSRuleScanID = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateOfHBMSMatch = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            IfResolutionOfHBMSReferralDoesNotResultInAFinancialAdjustmentPleaseIndicateTheReasonWhy = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            UniqueTRecordIdentifier = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonCapital = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonClaimentPartnersEarnedIncome = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonNonDependentsEarnedIncome = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonPassportingStatus = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonIncomeFromDWPBenefits = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonTaxCredits = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonOtherIncome = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonLivingTogetherAsHusbandAndWife = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonNumberOfNonDependents = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonNumberOfDependents = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonNonResidence = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonEligibleRentCouncilTax = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonIneligible = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonIdentityDeath = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonOther = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            BenefitThatThisPaymentErrorRelatesTo = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                TotalValueOfPaymentError = 0;
            } else {
                try {
                    TotalValueOfPaymentError = Integer.valueOf(fields[n]);
                } catch (NumberFormatException e) {
                    e.printStackTrace(System.err);
                    throw e;
                }
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            WeeklyBenefitDiscrepancyAtStartOfPaymentError = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            StartDateOfPaymentErrorPeriod = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            EndDateOfPaymentErrorPeriod = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            WhatWasTheCauseOfTheOverPayment = fields[n];
        }
//        } catch (ArrayIndexOutOfBoundsException e) {
//            int debug = 1;
//            System.err.println("RecordID " + RecordID);
//            System.err.println(e.getMessage());
//            System.err.println("Expected " + expectedFieldsLength + " fields");
//            System.err.println("fields.length = " + fields.length);
//            System.err.println("n = " + n);
//        }
    }

    private void generateDRecord(
            int type,
            String line,
            DW_SHBE_Handler handler) throws Exception {
        String[] fields = line.split(",");
        int exceptionalType = 0;
        int n = 0;
//        int expectedFieldsLength = 275;
//        try {
        // Normal record length is 275
//            if (fields.length < 250) {
////            if (fields.length < expectedFieldsLength) {
//                System.out.println("fields.length " + fields.length);
//                System.out.println("RecordID " + RecordID);
//                System.out.println(line);
//            }
        if (n < fields.length) {
            RecordType = fields[n];
        } else {
            return;
        }
        if (!handler.getRecordTypes().contains(RecordType)) {
            System.out.println("RecordType " + RecordType);
            System.out.println("!handler.getRecordTypes().contains(RecordType)");
            System.out.println("handler.toString() " + handler.toString());
            throw new Exception("!handler.getRecordTypes().contains(RecordType)");
        }
        n++;
        if (n < fields.length) {
            HousingBenefitClaimReferenceNumber = fields[n];
            //HousingBenefitClaimReferenceNumber = Long.valueOf(fields[n]);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            CouncilTaxBenefitClaimReferenceNumber = fields[n];
            //CouncilTaxBenefitClaimReferenceNumber = Long.valueOf(fields[n]);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            ClaimantsNationalInsuranceNumber = fields[n];
        } else {
            return;
        }
        //ClaimantsTitle = fields[n];
        //ClaimantsSurname = fields[n];
        //ClaimantsFirstForename = fields[n;
        n++;
        if (fields[n].isEmpty()) {
            n++;
            exceptionalType = 1;
        }
        if (n < fields.length) {
            ClaimantsDateOfBirth = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (exceptionalType == 0) {
                setTenancyType(n, fields);
            } else {
                if (exceptionalType == 1) {
                    System.out.println("Ignoring fields " + n + ": " + fields[n]);
                }
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            ClaimantsPostcode = fields[n];
        } else {
            return;
        }
        n++; //7
        if (n < fields.length) {
            setPassportedStandardIndicator(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                NumberOfChildDependents = 0;
            } else {
                NumberOfChildDependents = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                NumberOfNonDependents = 0;
            } else {
                NumberOfNonDependents = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n += 2; //10
        if (n < fields.length) {
            setNonDependentStatus(n, fields);
        } else {
            return;
        }
        n++; //12
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                NonDependentDeductionAmountApplied = 0;
            } else {
                NonDependentDeductionAmountApplied = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n = 28;
        if (n < fields.length) {
            setStatusOfHBClaimAtExtractDate(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setStatusOfCTBClaimAtExtractDate(n, fields);
        } else {
            return;
        }
        n++; //30
        if (n < fields.length) {
            DateMostRecentHBClaimWasReceived = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateMostRecentCTBClaimWasReceived = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateOfFirstDecisionOnMostRecentHBClaim = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateOfFirstDecisionOnMostRecentCTBClaim = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setOutcomeOfFirstDecisionOnMostRecentHBClaim(n, fields);
        } else {
            return;
        }
        n++; //35
        if (n < fields.length) {
            setOutcomeOfFirstDecisionOnMostRecentCTBClaim(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            HBClaimEntitlementStartDate = fields[n];
        } else {
            return;
        }
        n += 2;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyHousingBenefitEntitlement = 0;
            } else {
                WeeklyHousingBenefitEntitlement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyCouncilTaxBenefitEntitlement = 0;
            } else {
                WeeklyCouncilTaxBenefitEntitlement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //40
        if (n < fields.length) {
            if (exceptionalType == 1) {
                int debug = 1;
            }
            setFrequencyOfPaymentOfHB(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setFrequencyOfPaymentOfCTB(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PreDeterminationAmountOfHB = 0;
            } else {
                PreDeterminationAmountOfHB = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //43
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PreDeterminationAmountOfCTB = 0;
            } else {
                PreDeterminationAmountOfCTB = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n += 2; //45
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyHBEntitlementBeforeChange = 0;
            } else {
                WeeklyHBEntitlementBeforeChange = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n += 2; //47
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyCTBEntitlementBeforeChange = 0;
            } else {
                WeeklyCTBEntitlementBeforeChange = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setReasonForDirectPayment(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setTimePeriodContractualRentFigureCovers(n, fields);
        } else {
            return;
        }
        n++; //50
        if (n < fields.length) {
            setExtendedPaymentCase(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            CouncilTaxBand = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyEligibleRentAmount = 0;
            } else {
                WeeklyEligibleRentAmount = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyEligibleCouncilTaxAmount = 0;
            } else {
                WeeklyEligibleCouncilTaxAmount = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].equalsIgnoreCase("Y")) {
                ClaimantsStudentIndicator = "Y";
            } else {
                ClaimantsStudentIndicator = "N";
            }
        } else {
            return;
        }
        n++; //55
        if (n < fields.length) {
            setSecondAdultRebate(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setRebatePercentageWhereASecondAdultRebateHasBeenAwarded(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                LHARegulationsApplied = "No";
            } else {
                LHARegulationsApplied = "Yes";
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setIsThisCaseSubjectToLRROrSRRSchemes(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyLocalReferenceRent = 0;
            } else {
                WeeklyLocalReferenceRent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //60
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklySingleRoomRent = 0;
            } else {
                WeeklySingleRoomRent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyClaimRelatedRent = 0;
            } else {
                WeeklyClaimRelatedRent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                RentOfficerDeterminationOfIneligibleCharges = 0;
            } else {
                RentOfficerDeterminationOfIneligibleCharges = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyMaximumRent = 0;
            } else {
                WeeklyMaximumRent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                TotalDeductionForMeals = 0;
            } else {
                TotalDeductionForMeals = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //65
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyAdditionalDiscretionaryPayment = 0;
            } else {
                WeeklyAdditionalDiscretionaryPayment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setThirteenOrFiftyTwoWeekProtectionApplies(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateOfFirstPaymentOnMostRecentHBClaimFollowingAFullDecision = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsAssessedIncomeFigure = 0;
            } else {
                ClaimantsAssessedIncomeFigure = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsAdjustedAssessedIncomeFigure = 0;
            } else {
                ClaimantsAdjustedAssessedIncomeFigure = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //70
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsTotalCapital = 0;
            } else {
                ClaimantsTotalCapital = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsGrossWeeklyIncomeFromEmployment = 0;
            } else {
                ClaimantsGrossWeeklyIncomeFromEmployment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsNetWeeklyIncomeFromEmployment = 0;
            } else {
                ClaimantsNetWeeklyIncomeFromEmployment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsGrossWeeklyIncomeFromSelfEmployment = 0;
            } else {
                ClaimantsGrossWeeklyIncomeFromSelfEmployment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsNetWeeklyIncomeFromSelfEmployment = 0;
            } else {
                ClaimantsNetWeeklyIncomeFromSelfEmployment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //75
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsTotalAmountOfEarningsDisregarded = 0;
            } else {
                ClaimantsTotalAmountOfEarningsDisregarded = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = 0;
            } else {
                ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromAttendanceAllowance = 0;
            } else {
                ClaimantsIncomeFromAttendanceAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromBusinessStartUpAllowance = 0;
            } else {
                ClaimantsIncomeFromBusinessStartUpAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromChildBenefit = 0;
            } else {
                ClaimantsIncomeFromChildBenefit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //80
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent = 0;
            } else {
                ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromPersonalPension = 0;
            } else {
                ClaimantsIncomeFromPersonalPension = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromSevereDisabilityAllowance = 0;
            } else {
                ClaimantsIncomeFromSevereDisabilityAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromMaternityAllowance = 0;
            } else {
                ClaimantsIncomeFromMaternityAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromContributionBasedJobSeekersAllowance = 0;
            } else {
                ClaimantsIncomeFromContributionBasedJobSeekersAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //85
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromStudentGrantLoan = 0;
            } else {
                ClaimantsIncomeFromStudentGrantLoan = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromSubTenants = 0;
            } else {
                ClaimantsIncomeFromSubTenants = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromBoarders = 0;
            } else {
                ClaimantsIncomeFromBoarders = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromTrainingForWorkCommunityAction = 0;
            } else {
                ClaimantsIncomeFromTrainingForWorkCommunityAction = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromIncapacityBenefitShortTermLower = 0;
            } else {
                ClaimantsIncomeFromIncapacityBenefitShortTermLower = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //90
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromIncapacityBenefitShortTermHigher = 0;
            } else {
                ClaimantsIncomeFromIncapacityBenefitShortTermHigher = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromIncapacityBenefitLongTerm = 0;
            } else {
                ClaimantsIncomeFromIncapacityBenefitLongTerm = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromNewDeal50PlusEmploymentCredit = 0;
            } else {
                ClaimantsIncomeFromNewDeal50PlusEmploymentCredit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromNewTaxCredits = 0;
            } else {
                ClaimantsIncomeFromNewTaxCredits = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent = 0;
            } else {
                ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //95
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent = 0;
            } else {
                ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromGovernemntTraining = 0;
            } else {
                ClaimantsIncomeFromGovernemntTraining = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit = 0;
            } else {
                ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromCarersAllowance = 0;
            } else {
                ClaimantsIncomeFromCarersAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromStatutoryMaternityPaternityPay = 0;
            } else {
                ClaimantsIncomeFromStatutoryMaternityPaternityPay = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //100
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = 0;
            } else {
                ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP = 0;
            } else {
                ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromWarMobilitySupplement = 0;
            } else {
                ClaimantsIncomeFromWarMobilitySupplement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromWidowsWidowersPension = 0;
            } else {
                ClaimantsIncomeFromWidowsWidowersPension = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromBereavementAllowance = 0;
            } else {
                ClaimantsIncomeFromBereavementAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //105
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromWidowedParentsAllowance = 0;
            } else {
                ClaimantsIncomeFromWidowedParentsAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromYouthTrainingScheme = 0;
            } else {
                ClaimantsIncomeFromYouthTrainingScheme = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromStatuatorySickPay = 0;
            } else {
                ClaimantsIncomeFromStatuatorySickPay = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsOtherIncome = 0;
            } else {
                ClaimantsOtherIncome = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsTotalAmountOfIncomeDisregarded = 0;
            } else {
                ClaimantsTotalAmountOfIncomeDisregarded = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //110
        if (n < fields.length) {
            setFamilyPremiumAwarded(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setFamilyLoneParentPremiumAwarded(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setDisabilityPremiumAwarded(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setSevereDisabilityPremiumAwarded(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setDisabledChildPremiumAwarded(n, fields);
        } else {
            return;
        }
        n++; //115
        if (n < fields.length) {
            setCarePremiumAwarded(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setEnhancedDisabilityPremiumAwarded(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setBereavementPremiumAwarded(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setPartnerFlag(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            PartnersStartDate = fields[n];
        } else {
            return;
        }
        n++; //120
        if (n < fields.length) {
            PartnersEndDate = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            PartnersNationalInsuranceNumber = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersStudentIndicator = "N";
            } else {
                PartnersStudentIndicator = "Y";
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersAssessedIncomeFigure = 0;
            } else {
                PartnersAssessedIncomeFigure = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersAdjustedAssessedIncomeFigure = 0;
            } else {
                PartnersAdjustedAssessedIncomeFigure = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //125
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersGrossWeeklyIncomeFromEmployment = 0;
            } else {
                PartnersGrossWeeklyIncomeFromEmployment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersNetWeeklyIncomeFromEmployment = 0;
            } else {
                PartnersNetWeeklyIncomeFromEmployment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersGrossWeeklyIncomeFromSelfEmployment = 0;
            } else {
                PartnersGrossWeeklyIncomeFromSelfEmployment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersNetWeeklyIncomeFromSelfEmployment = 0;
            } else {
                PartnersNetWeeklyIncomeFromSelfEmployment = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersTotalAmountOfEarningsDisregarded = 0;
            } else {
                PartnersTotalAmountOfEarningsDisregarded = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //130
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = 0;
            } else {
                PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromAttendanceAllowance = 0;
            } else {
                PartnersIncomeFromAttendanceAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromBusinessStartUpAllowance = 0;
            } else {
                PartnersIncomeFromBusinessStartUpAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromChildBenefit = 0;
            } else {
                PartnersIncomeFromChildBenefit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromPersonalPension = 0;
            } else {
                PartnersIncomeFromPersonalPension = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //135
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromSevereDisabilityAllowance = 0;
            } else {
                PartnersIncomeFromSevereDisabilityAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromMaternityAllowance = 0;
            } else {
                PartnersIncomeFromMaternityAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromContributionBasedJobSeekersAllowance = 0;
            } else {
                PartnersIncomeFromContributionBasedJobSeekersAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromStudentGrantLoan = 0;
            } else {
                PartnersIncomeFromStudentGrantLoan = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromSubTenants = 0;
            } else {
                PartnersIncomeFromSubTenants = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //140
        if (n < fields.length) {
            if (fields[140].equalsIgnoreCase("")) {
                PartnersIncomeFromBoarders = 0;
            } else {
                PartnersIncomeFromBoarders = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromTrainingForWorkCommunityAction = 0;
            } else {
                PartnersIncomeFromTrainingForWorkCommunityAction = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromIncapacityBenefitShortTermLower = 0;
            } else {
                PartnersIncomeFromIncapacityBenefitShortTermLower = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromIncapacityBenefitShortTermHigher = 0;
            } else {
                PartnersIncomeFromIncapacityBenefitShortTermHigher = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromIncapacityBenefitLongTerm = 0;
            } else {
                PartnersIncomeFromIncapacityBenefitLongTerm = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //145
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromNewDeal50PlusEmploymentCredit = 0;
            } else {
                PartnersIncomeFromNewDeal50PlusEmploymentCredit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromNewTaxCredits = 0;
            } else {
                PartnersIncomeFromNewTaxCredits = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromDisabilityLivingAllowanceCareComponent = 0;
            } else {
                PartnersIncomeFromDisabilityLivingAllowanceCareComponent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent = 0;
            } else {
                PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromGovernemntTraining = 0;
            } else {
                PartnersIncomeFromGovernemntTraining = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //150
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromIndustrialInjuriesDisablementBenefit = 0;
            } else {
                PartnersIncomeFromIndustrialInjuriesDisablementBenefit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromCarersAllowance = 0;
            } else {
                PartnersIncomeFromCarersAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromStatuatorySickPay = 0;
            } else {
                PartnersIncomeFromStatuatorySickPay = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromStatutoryMaternityPaternityPay = 0;
            } else {
                PartnersIncomeFromStatutoryMaternityPaternityPay = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = 0;
            } else {
                PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //155
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromWarDisablementPensionArmedForcesGIP = 0;
            } else {
                PartnersIncomeFromWarDisablementPensionArmedForcesGIP = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromWarMobilitySupplement = 0;
            } else {
                PartnersIncomeFromWarMobilitySupplement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromWidowsWidowersPension = 0;
            } else {
                PartnersIncomeFromWidowsWidowersPension = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromBereavementAllowance = 0;
            } else {
                PartnersIncomeFromBereavementAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromWidowedParentsAllowance = 0;
            } else {
                PartnersIncomeFromWidowedParentsAllowance = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //160
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromYouthTrainingScheme = 0;
            } else {
                PartnersIncomeFromYouthTrainingScheme = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersOtherIncome = 0;
            } else {
                PartnersOtherIncome = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersTotalAmountOfIncomeDisregarded = 0;
            } else {
                PartnersTotalAmountOfIncomeDisregarded = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateOverPaymentDetectionActivityInitiatedOnCase = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setMethodOfOverpayentDetectionActivity(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setReasonForOverpaymentDetectionActivity(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DoesTheOverpaymentDetectionActivityConstituteAFullReview = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateOverPaymentDetectionActivityIsCompleted = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setOutcomeOfOverPaymentDetectionActivity(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            ClaimantsGender = fields[n];
        } else {
            return;
        }
        n++; //170
        if (n < fields.length) {
            PartnersDateOfBirth = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setRentAllowanceMethodOfPayment(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setRentAllowancePaymentDestination(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ContractualRentAmount = 0;
            } else {
                ContractualRentAmount = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setTimePeriodContractualRentFigureCovers(n, fields);
        } else {
            return;
        }
        n++; //175
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromPensionCreditSavingsCredit = 0;
            } else {
                ClaimantsIncomeFromPensionCreditSavingsCredit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromPensionCreditSavingsCredit = 0;
            } else {
                PartnersIncomeFromPensionCreditSavingsCredit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromMaintenancePayments = 0;
            } else {
                ClaimantsIncomeFromMaintenancePayments = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromMaintenancePayments = 0;
            } else {
                PartnersIncomeFromMaintenancePayments = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromOccupationalPension = 0;
            } else {
                ClaimantsIncomeFromOccupationalPension = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //180
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromOccupationalPension = 0;
            } else {
                PartnersIncomeFromOccupationalPension = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsIncomeFromWidowsBenefit = 0;
            } else {
                ClaimantsIncomeFromWidowsBenefit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //182
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersIncomeFromWidowsBenefit = 0;
            } else {
                PartnersIncomeFromWidowsBenefit = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n = 193; //193
        if (n < fields.length) {
            CTBClaimEntitlementStartDate = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateHBClaimClosedWithdrawnDecidedUnsuccessfulDefective = fields[n];
        } else {
            return;
        }
        n++; //195
        if (n < fields.length) {
            DateCTBClaimClosedWithdrawnDecidedUnsuccessfulDefective = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                TotalNumberOfRooms = 0;
            } else {
                TotalNumberOfRooms = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setNonSelfContainedAccomodation(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                TypeOfLHANumberOfRoomsEntitedTo = 0;
            } else {
                TypeOfLHANumberOfRoomsEntitedTo = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setTransitionalProtectionFromNationalRolloutOfLHA(n, fields);
        } else {
            return;
        }
        n++; //200
        if (n < fields.length) {
            Locality = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ValueOfLHA = 0;
            } else {
                ValueOfLHA = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            PartnersGender = fields[n];
        } else {
            return;
        }
        n++; //205
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                NonDependantGrossWeeklyIncomeFromRemunerativeWork = 0;
            } else {
                NonDependantGrossWeeklyIncomeFromRemunerativeWork = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n = 211;
        if (n < fields.length) {
            HBClaimTreatAsDateMade = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setSourceOfTheMostRecentHBClaim(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setDidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateOfFirstHBPaymentRentAllowanceOnly = fields[n];
        } else {
            return;
        }
        n++; //215
        if (n < fields.length) {
            setWasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setWasThereABackdatedAwardMadeOnTheHBClaim(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateHBBackdatingFrom = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateHBBackdatingTo = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                TotalAmountOfBackdatedHBAwarded = 0;
            } else {
                TotalAmountOfBackdatedHBAwarded = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //220
        if (n < fields.length) {
            CTBClaimTreatAsMadeDate = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setSourceOfTheMostRecentCTBClaim(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setDidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setWasThereABackdatedAwardMadeOnTheCTBClaim(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateCTBBackdatingFrom = fields[n];
        } else {
            return;
        }
        n++; //225
        if (n < fields.length) {
            DateCTBBackdatingTo = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                TotalAmountOfBackdatedCTBAwarded = 0;
            } else {
                TotalAmountOfBackdatedCTBAwarded = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setInThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setIfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateOfTransferFromLocalAuthorityLandlordToHousingAssociationRSLIfSubjectToStockTRansferOrLSVT = fields[n];
        } else {
            return;
        }
        n++; //230
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersTotalCapital = 0;
            } else {
                PartnersTotalCapital = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure = 0;
            } else {
                WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsTotalHoursOfRemunerativeWorkPerWeek = 0;
            } else {
                try {
                    ClaimantsTotalHoursOfRemunerativeWorkPerWeek = Double.valueOf(fields[n]);
                } catch (NumberFormatException e) {
                    e.printStackTrace(System.err);
                    throw e;
                }
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].equalsIgnoreCase("") || fields[n].isEmpty() || fields[n].equalsIgnoreCase(" ") || fields[n].equalsIgnoreCase("  ")) {
                PartnersTotalHoursOfRemunerativeWorkPerWeek = 0;
            } else {
                try {
                    PartnersTotalHoursOfRemunerativeWorkPerWeek = Double.valueOf(fields[n]);
                } catch (NumberFormatException e) {
                    e.printStackTrace(System.err);
                    throw e;
                }
            }
        } else {
            return;
        }
        n = 236;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                TotalHBPaymentsCreditsSinceLastExtract = 0;
            } else {
                TotalHBPaymentsCreditsSinceLastExtract = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                TotalCTBPaymentsCreditsSinceLastExtract = 0;
            } else {
                TotalCTBPaymentsCreditsSinceLastExtract = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setClaimantsEthnicGroup(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                NewWeeklyHBEntitlementAfterTheChange = 0;
            } else {
                NewWeeklyHBEntitlementAfterTheChange = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //240
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                NewWeeklyCTBEntitlementAfterTheChange = 0;
            } else {
                NewWeeklyCTBEntitlementAfterTheChange = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setTypeOfChange(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateLAFirstNotifiedOfChangeInClaimDetails = fields[n];
        } else {
            return;
        }
        n += 2;
        if (n < fields.length) {
            DateChangeOfDetailsAreEffectiveFrom = fields[n];
        } else {
            return;
        }
        n++; //245
        if (n < fields.length) {
            setIfNotAnnualUpratingHowWasTheChangeIdentified(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateSupercessionDecisionWasMadeOnTheHBClaim = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateSupercessionDecisionWasMadeOnTheCTBClaim = fields[n];
        } else {
            return;
        }
        n += 3; //250
        if (n < fields.length) {
            DateApplicationForRevisionReconsiderationReceived = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateClaimantNotifiedAboutTheDecisionOfTheRevisionReconsideration = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateAppealApplicationWasLodged = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setOutcomeOfAppealApplication(n, fields);
        } else {
            return;
        }
        n++; //254
        if (n < fields.length) {
            DateOfOutcomeOfAppealApplication = fields[n];
        } else {
            return;
        }
        n += 6; //260
        if (n < fields.length) {
            DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentHBClaim = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentHBClaim = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateCouncilTaxPayable = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentCTBClaim = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentCTBClaim = fields[n];
        } else {
            return;
        }
        n++; //265
        if (n < fields.length) {
            setSoftwareProvider(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            StaffingFTE = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setContractingOutHandlingAndMaintenanceOfHBCTB(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setContractingOutCounterFraudWorkRelatingToHBCTB(n, fields);
        } else {
            return;
        }
        n++; //269
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                NumberOfBedroomsForLHARolloutCasesOnly = 0;
            } else {
                NumberOfBedroomsForLHARolloutCasesOnly = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //270
        if (n < fields.length) {
            PartnersDateOfDeath = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setJointTenancyFlag(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            setAppointeeFlag(n, fields);
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                RentFreeWeeksIndicator = 0;
            } else {
                RentFreeWeeksIndicator = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //274
        if (n < fields.length) {
            LastPaidToDate = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = 0;
            } else {
                WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsWeeklyIncomeFromESABasicElement = 0;
            } else {
                ClaimantsWeeklyIncomeFromESABasicElement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersWeeklyIncomeFromESABasicElement = 0;
            } else {
                PartnersWeeklyIncomeFromESABasicElement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsWeeklyIncomeFromESAWRAGElement = 0;
            } else {
                ClaimantsWeeklyIncomeFromESAWRAGElement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++; //279
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersWeeklyIncomeFromESAWRAGElement = 0;
            } else {
                PartnersWeeklyIncomeFromESAWRAGElement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                ClaimantsWeeklyIncomeFromESASCGElement = 0;
            } else {
                ClaimantsWeeklyIncomeFromESASCGElement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                PartnersWeeklyIncomeFromESASCGElement = 0;
            } else {
                PartnersWeeklyIncomeFromESASCGElement = Integer.valueOf(fields[n]);
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            WRAGPremiumFlag = fields[n];
        } else {
            return;
        }
        n++; //283
        if (n < fields.length) {
            SCGPremiumFlag = fields[n];
        } else {
            return;
        }
        if (type == 1) {
            n++;
            if (n < fields.length) {
                LandlordPostcode = fields[n];
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            n = setSubRecordType(n, fields);
        } else {
            return;
        }
        if (type == 1) {
            n++;
            if (n < fields.length) {
                SubRecordDateOfBirth = fields[n];
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            SubRecordChildReferenceNumberOrNINO = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            SubRecordStartDate = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            SubRecordEndDate = fields[n];
        } else {
            return;
        }
        if (type == 1) {
            n++;
            if (n < fields.length) {
                SubRecordDateOfBirth = fields[n];
            } else {
                return;
            }
        }
        n++;
        if (n < fields.length) {
            IfThisActivityResolvesAnHBMSReferralProvideRMSNumber = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            HBMSRuleScanID = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            DateOfHBMSMatch = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            IfResolutionOfHBMSReferralDoesNotResultInAFinancialAdjustmentPleaseIndicateTheReasonWhy = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            UniqueTRecordIdentifier = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonCapital = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonClaimentPartnersEarnedIncome = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonNonDependentsEarnedIncome = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonPassportingStatus = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonIncomeFromDWPBenefits = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonTaxCredits = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonOtherIncome = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonLivingTogetherAsHusbandAndWife = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonNumberOfNonDependents = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonNumberOfDependents = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonNonResidence = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonEligibleRentCouncilTax = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonIneligible = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonIdentityDeath = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            OverpaymentReasonOther = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            BenefitThatThisPaymentErrorRelatesTo = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            if (fields[n].trim().isEmpty()) {
                TotalValueOfPaymentError = 0;
            } else {
                try {
                    TotalValueOfPaymentError = Integer.valueOf(fields[n]);
                } catch (NumberFormatException e) {
                    System.err.println("RecordID " + RecordID);
                    System.err.println("TotalValueOfPaymentError " + fields[n]);
                    e.printStackTrace(System.err);
                    throw e;
                }
            }
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            WeeklyBenefitDiscrepancyAtStartOfPaymentError = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            StartDateOfPaymentErrorPeriod = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            EndDateOfPaymentErrorPeriod = fields[n];
        } else {
            return;
        }
        n++;
        if (n < fields.length) {
            WhatWasTheCauseOfTheOverPayment = fields[n];
        }
//        } catch (ArrayIndexOutOfBoundsException e) {
//            int debug = 1;
//            System.err.println("RecordID " + RecordID);
//            System.err.println(e.getMessage());
//            System.err.println("Expected " + expectedFieldsLength + " fields");
//            System.err.println("fields.length = " + fields.length);
//            System.err.println("n = " + n);
//        }
    }

    @Override
    public String toString() {
        return "RecordID " + RecordID
                + ",RecordType " + RecordType
                + ",HousingBenefitClaimReferenceNumber " + HousingBenefitClaimReferenceNumber
                + ",CouncilTaxBenefitClaimReferenceNumber " + CouncilTaxBenefitClaimReferenceNumber
                + ",ClaimantsNationalInsuranceNumber " + ClaimantsNationalInsuranceNumber
                + ",ClaimantsDateOfBirth " + ClaimantsDateOfBirth
                + ",TenancyType " + TenancyType
                + ",ClaimantsPostcode " + ClaimantsPostcode
                + ",PassportedStandardIndicator " + PassportedStandardIndicator
                + ",NumberOfChildDependents " + NumberOfChildDependents
                + ",NumberOfNonDependents " + NumberOfNonDependents
                + ",NonDependentStatus " + NonDependentStatus
                + ",NonDependentDeductionAmountApplied " + NonDependentDeductionAmountApplied
                + ",StatusOfHBClaimAtExtractDate " + StatusOfHBClaimAtExtractDate
                + ",StatusOfCTBClaimAtExtractDate " + StatusOfCTBClaimAtExtractDate
                + ",DateMostRecentHBClaimWasReceived " + DateMostRecentHBClaimWasReceived
                + ",DateMostRecentCTBClaimWasReceived " + DateMostRecentCTBClaimWasReceived
                + ",DateOfFirstDecisionOnMostRecentHBClaim " + DateOfFirstDecisionOnMostRecentHBClaim
                + ",DateOfFirstDecisionOnMostRecentCTBClaim " + DateOfFirstDecisionOnMostRecentCTBClaim
                + ",OutcomeOfFirstDecisionOnMostRecentHBClaim " + OutcomeOfFirstDecisionOnMostRecentHBClaim
                + ",OutcomeOfFirstDecisionOnMostRecentCTBClaim " + OutcomeOfFirstDecisionOnMostRecentCTBClaim
                + ",HBClaimEntitlementStartDate " + HBClaimEntitlementStartDate
                + ",WeeklyHousingBenefitEntitlement " + WeeklyHousingBenefitEntitlement
                + ",WeeklyCouncilTaxBenefitEntitlement " + WeeklyCouncilTaxBenefitEntitlement
                + ",FrequencyOfPaymentOfHB " + FrequencyOfPaymentOfHB
                + ",FrequencyOfPaymentOfCTB " + FrequencyOfPaymentOfCTB
                + ",PreDeterminationAmountOfHB " + PreDeterminationAmountOfHB
                + ",PreDeterminationAmountOfCTB " + PreDeterminationAmountOfCTB
                + ",WeeklyHBEntitlementBeforeChange " + WeeklyHBEntitlementBeforeChange
                + ",WeeklyCTBEntitlementBeforeChange " + WeeklyCTBEntitlementBeforeChange
                + ",ReasonForDirectPayment " + ReasonForDirectPayment
                + ",TimingOfPaymentOfRentAllowance " + TimingOfPaymentOfRentAllowance
                + ",ExtendedPaymentCase " + ExtendedPaymentCase
                + ",CouncilTaxBand " + CouncilTaxBand
                + ",WeeklyEligibleRentAmount " + WeeklyEligibleRentAmount
                + ",WeeklyEligibleCouncilTaxAmount " + WeeklyEligibleCouncilTaxAmount
                + ",ClaimantsStudentIndicator " + ClaimantsStudentIndicator
                + ",RebatePercentageWhereASecondAdultRebateHasBeenAwarded " + RebatePercentageWhereASecondAdultRebateHasBeenAwarded
                + ",SecondAdultRebate " + SecondAdultRebate
                + ",LHARegulationsApplied " + LHARegulationsApplied
                + ",IsThisCaseSubjectToLRROrSRRSchemes " + IsThisCaseSubjectToLRROrSRRSchemes
                + ",WeeklyLocalReferenceRent " + WeeklyLocalReferenceRent
                + ",WeeklySingleRoomRent " + WeeklySingleRoomRent
                + ",WeelklyClaimRelatedRent " + WeeklyClaimRelatedRent
                + ",RentOfficerDeterminationOfIneligibleCharges " + RentOfficerDeterminationOfIneligibleCharges
                + ",WeeklyMaximumRent " + WeeklyMaximumRent
                + ",TotalDeductionForMeals " + TotalDeductionForMeals
                + ",WeeklyAdditionalDiscretionaryPayment " + WeeklyAdditionalDiscretionaryPayment
                + ",ThirteenOrFiftyTwoWeekProtectionApplies " + ThirteenOrFiftyTwoWeekProtectionApplies
                + ",DateOfFirstPaymentOnMostRecentHBClaimFollowingAFullDecision " + DateOfFirstPaymentOnMostRecentHBClaimFollowingAFullDecision
                + ",ClaimantsAssessedIncomeFigure " + ClaimantsAssessedIncomeFigure
                + ",ClaimantsAdjustedAssessedIncomeFigure " + ClaimantsAdjustedAssessedIncomeFigure
                + ",ClaimantsTotalCapital " + ClaimantsTotalCapital
                + ",ClaimantsGrossWeeklyIncomeFromEmployment " + ClaimantsGrossWeeklyIncomeFromEmployment
                + ",ClaimantsNetWeeklyIncomeFromEmployment " + ClaimantsNetWeeklyIncomeFromEmployment
                + ",ClaimantsGrossWeeklyIncomeFromSelfEmployment " + ClaimantsGrossWeeklyIncomeFromSelfEmployment
                + ",ClaimantsNetWeeklyIncomeFromSelfEmployment " + ClaimantsNetWeeklyIncomeFromSelfEmployment
                + ",ClaimantsTotalAmountOfEarningsDisregarded " + ClaimantsTotalAmountOfEarningsDisregarded
                + ",ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded " + ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded
                + ",ClaimantsIncomeFromAttendanceAllowance " + ClaimantsIncomeFromAttendanceAllowance
                + ",ClaimantsIncomeFromBusinessStartUpAllowance " + ClaimantsIncomeFromBusinessStartUpAllowance
                + ",ClaimantsIncomeFromChildBenefit " + ClaimantsIncomeFromChildBenefit
                + ",ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent " + ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent
                + ",ClaimantsIncomeFromPersonalPension " + ClaimantsIncomeFromPersonalPension
                + ",ClaimantsIncomeFromSevereDisabilityAllowance " + ClaimantsIncomeFromSevereDisabilityAllowance
                + ",ClaimantsIncomeFromMaternityAllowance " + ClaimantsIncomeFromMaternityAllowance
                + ",ClaimantsIncomeFromContributionBasedJobSeekersAllowance " + ClaimantsIncomeFromContributionBasedJobSeekersAllowance
                + ",ClaimantsIncomeFromStudentGrantLoan " + ClaimantsIncomeFromStudentGrantLoan
                + ",ClaimantsIncomeFromSubTenants " + ClaimantsIncomeFromSubTenants
                + ",ClaimantsIncomeFromBoarders " + ClaimantsIncomeFromBoarders
                + ",ClaimantsIncomeFromTrainingForWorkCommunityAction " + ClaimantsIncomeFromTrainingForWorkCommunityAction
                + ",ClaimantsIncomeFromIncapacityBenefitShortTermLower " + ClaimantsIncomeFromIncapacityBenefitShortTermLower
                + ",ClaimantsIncomeFromIncapacityBenefitShortTermHigher " + ClaimantsIncomeFromIncapacityBenefitShortTermHigher
                + ",ClaimantsIncomeFromIncapacityBenefitLongTerm " + ClaimantsIncomeFromIncapacityBenefitLongTerm
                + ",ClaimantsIncomeFromNewDeal50PlusEmploymentCredit " + ClaimantsIncomeFromNewDeal50PlusEmploymentCredit
                + ",ClaimantsIncomeFromNewTaxCredits " + ClaimantsIncomeFromNewTaxCredits
                + ",ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent " + ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent
                + ",ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent " + ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent
                + ",ClaimantsIncomeFromGovernemntTraining " + ClaimantsIncomeFromGovernemntTraining
                + ",ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit " + ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit
                + ",ClaimantsIncomeFromCarersAllowance " + ClaimantsIncomeFromCarersAllowance
                + ",ClaimantsIncomeFromStatutoryMaternityPaternityPay " + ClaimantsIncomeFromStatutoryMaternityPaternityPay
                + ",ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc " + ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc
                + ",ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP " + ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP
                + ",ClaimantsIncomeFromWarMobilitySupplement " + ClaimantsIncomeFromWarMobilitySupplement
                + ",ClaimantsIncomeFromBereavementAllowance " + ClaimantsIncomeFromBereavementAllowance
                + ",ClaimantsIncomeFromWidowedParentsAllowance " + ClaimantsIncomeFromWidowedParentsAllowance
                + ",ClaimantsIncomeFromYouthTrainingScheme " + ClaimantsIncomeFromYouthTrainingScheme
                + ",ClaimantsIncomeFromStatuatorySickPay " + ClaimantsIncomeFromStatuatorySickPay
                + ",ClaimantsOtherIncome " + ClaimantsOtherIncome
                + ",ClaimantsTotalAmountOfIncomeDisregarded " + ClaimantsTotalAmountOfIncomeDisregarded
                + ",FamilyPremiumAwarded " + FamilyPremiumAwarded
                + ",FamilyLoneParentPremiumAwarded " + FamilyLoneParentPremiumAwarded
                + ",DisabilityPremiumAwarded " + DisabilityPremiumAwarded
                + ",SevereDisabilityPremiumAwarded " + SevereDisabilityPremiumAwarded
                + ",DisabledChildPremiumAwarded " + DisabledChildPremiumAwarded
                + ",CarePremiumAwarded " + CarePremiumAwarded
                + ",EnhancedDisabilityPremiumAwarded " + EnhancedDisabilityPremiumAwarded
                + ",BereavementPremiumAwarded " + BereavementPremiumAwarded
                + ",PartnerFlag " + PartnerFlag
                + ",PartnersStartDate " + PartnersStartDate
                + ",PartnersEndDate " + PartnersEndDate
                + ",PartnersNationalInsuranceNumber " + PartnersNationalInsuranceNumber
                + ",PartnersStudentIndicator " + PartnersStudentIndicator
                + ",PartnersAssessedIncomeFigure " + PartnersAssessedIncomeFigure
                + ",PartnersAdjustedAssessedIncomeFigure " + PartnersAdjustedAssessedIncomeFigure
                + ",PartnersGrossWeeklyIncomeFromEmployment " + PartnersGrossWeeklyIncomeFromEmployment
                + ",PartnersNetWeeklyIncomeFromEmployment " + PartnersNetWeeklyIncomeFromEmployment
                + ",PartnersGrossWeeklyIncomeFromSelfEmployment " + PartnersGrossWeeklyIncomeFromSelfEmployment
                + ",PartnersNetWeeklyIncomeFromSelfEmployment " + PartnersNetWeeklyIncomeFromSelfEmployment
                + ",PartnersTotalAmountOfEarningsDisregarded " + PartnersTotalAmountOfEarningsDisregarded
                + ",PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded " + PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded
                + ",PartnersIncomeFromAttendanceAllowance " + PartnersIncomeFromAttendanceAllowance
                + ",PartnersIncomeFromBusinessStartUpAllowance " + PartnersIncomeFromBusinessStartUpAllowance
                + ",PartnersIncomeFromChildBenefit " + PartnersIncomeFromChildBenefit
                + ",PartnersIncomeFromPersonalPension " + PartnersIncomeFromPersonalPension
                + ",PartnersIncomeFromSevereDisabilityAllowance " + PartnersIncomeFromSevereDisabilityAllowance
                + ",PartnersIncomeFromMaternityAllowance " + PartnersIncomeFromMaternityAllowance
                + ",PartnersIncomeFromContributionBasedJobSeekersAllowance " + PartnersIncomeFromContributionBasedJobSeekersAllowance
                + ",PartnersIncomeFromStudentGrantLoan " + PartnersIncomeFromStudentGrantLoan
                + ",PartnersIncomeFromSubTenants " + PartnersIncomeFromSubTenants
                + ",PartnersIncomeFromBoarders " + PartnersIncomeFromBoarders
                + ",PartnersIncomeFromTrainingForWorkCommunityAction " + PartnersIncomeFromTrainingForWorkCommunityAction
                + ",PartnersIncomeFromIncapacityBenefitShortTermLower " + PartnersIncomeFromIncapacityBenefitShortTermLower
                + ",PartnersIncomeFromIncapacityBenefitShortTermHigher " + PartnersIncomeFromIncapacityBenefitShortTermHigher
                + ",PartnersIncomeFromIncapacityBenefitLongTerm " + PartnersIncomeFromIncapacityBenefitLongTerm
                + ",PartnersIncomeFromNewDeal50PlusEmploymentCredit " + PartnersIncomeFromNewDeal50PlusEmploymentCredit
                + ",PartnersIncomeFromNewTaxCredits " + PartnersIncomeFromNewTaxCredits
                + ",PartnersIncomeFromDisabilityLivingAllowanceCareComponent " + PartnersIncomeFromDisabilityLivingAllowanceCareComponent
                + ",PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent " + PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent
                + ",PartnersIncomeFromGovernemntTraining " + PartnersIncomeFromGovernemntTraining
                + ",PartnersIncomeFromIndustrialInjuriesDisablementBenefit " + PartnersIncomeFromIndustrialInjuriesDisablementBenefit
                + ",PartnersIncomeFromCarersAllowance " + PartnersIncomeFromCarersAllowance
                + ",PartnersIncomeFromStatuatorySickPay " + PartnersIncomeFromStatuatorySickPay
                + ",PartnersIncomeFromStatutoryMaternityPaternityPay " + PartnersIncomeFromStatutoryMaternityPaternityPay
                + ",PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc " + PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc
                + ",PartnersIncomeFromWarDisablementPensionArmedForcesGIP " + PartnersIncomeFromWarDisablementPensionArmedForcesGIP
                + ",PartnersIncomeFromWarMobilitySupplement " + PartnersIncomeFromWarMobilitySupplement
                + ",PartnersIncomeFromWidowsWidowersPension " + PartnersIncomeFromWidowsWidowersPension
                + ",PartnersIncomeFromBereavementAllowance " + PartnersIncomeFromBereavementAllowance
                + ",PartnersIncomeFromWidowedParentsAllowance " + PartnersIncomeFromWidowedParentsAllowance
                + ",PartnersIncomeFromYouthTrainingScheme " + PartnersIncomeFromYouthTrainingScheme
                + ",PartnersOtherIncome " + PartnersOtherIncome
                + ",PartnersTotalAmountOfIncomeDisregarded " + PartnersTotalAmountOfIncomeDisregarded
                + ",DateOverPaymentDetectionActivityInitiatedOnCase " + DateOverPaymentDetectionActivityInitiatedOnCase
                + ",ReasonForOverpaymentDetectionActivity " + ReasonForOverpaymentDetectionActivity
                + ",MethodOfOverpayentDetectionActivity " + MethodOfOverpayentDetectionActivity
                + ",DoesTheOverpaymentDetectionActivityConstituteAFullReview " + DoesTheOverpaymentDetectionActivityConstituteAFullReview
                + ",DateOverPaymentDetectionActivityIsCompleted " + DateOverPaymentDetectionActivityIsCompleted
                + ",OutcomeOfOverPaymentDetectionActivity " + OutcomeOfOverPaymentDetectionActivity
                + ",ClaimantsGender " + ClaimantsGender
                + ",PartnersDateOfBirth " + PartnersDateOfBirth
                + ",RentAllowanceMethodOfPayment " + RentAllowanceMethodOfPayment
                + ",RentAllowancePaymentDestination " + RentAllowancePaymentDestination
                + ",ContractualRentAmount " + ContractualRentAmount
                + ",TimePeriodContractualRentFigureCovers " + TimePeriodContractualRentFigureCovers
                + ",ClaimantsIncomeFromPensionCreditSavingsCredit " + ClaimantsIncomeFromPensionCreditSavingsCredit
                + ",PartnersIncomeFromPensionCreditSavingsCredit " + PartnersIncomeFromPensionCreditSavingsCredit
                + ",ClaimantsIncomeFromMaintenancePayments " + ClaimantsIncomeFromMaintenancePayments
                + ",PartnersIncomeFromMaintenancePayments " + PartnersIncomeFromMaintenancePayments
                + ",ClaimantsIncomeFromOccupationalPension " + ClaimantsIncomeFromOccupationalPension
                + ",PartnersIncomeFromOccupationalPension " + PartnersIncomeFromOccupationalPension
                + ",ClaimantsIncomeFromWidowsBenefit " + ClaimantsIncomeFromWidowsBenefit
                + ",PartnersIncomeFromWidowsBenefit " + PartnersIncomeFromWidowsBenefit
                + ",CTBClaimEntitlementStartDate " + CTBClaimEntitlementStartDate
                + ",DateHBClaimClosedWithdrawnDecidedUnsuccessfulDefective " + DateHBClaimClosedWithdrawnDecidedUnsuccessfulDefective
                + ",DateCTBClaimClosedWithdrawnDecidedUnsuccessfulDefective " + DateCTBClaimClosedWithdrawnDecidedUnsuccessfulDefective
                + ",TotalNumberOfRooms " + TotalNumberOfRooms
                + ",NonSelfContainedAccomodation " + NonSelfContainedAccomodation
                + ",TypeOfLHANumberOfRoomsEntitedTo " + TypeOfLHANumberOfRoomsEntitedTo
                + ",TransitionalProtectionFromNationalRolloutOfLHA " + TransitionalProtectionFromNationalRolloutOfLHA
                + ",Locality " + Locality
                + ",ValueOfLHA " + ValueOfLHA
                + ",ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective " + ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective
                + ",ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective " + ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective
                + ",PartnersGender " + PartnersGender
                + ",NonDependantGrossWeeklyIncomeFromRemunerativeWork " + NonDependantGrossWeeklyIncomeFromRemunerativeWork
                + ",HBClaimTreatAsDateMade " + HBClaimTreatAsDateMade
                //              + ",SourceOfMostRecentHBClaim " + SourceOfMostRecentHBClaim
                + ",DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim " + DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim
                + ",DateOfFirstHBPaymentRentAllowanceOnly " + DateOfFirstHBPaymentRentAllowanceOnly
                + ",WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly " + WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly
                + ",WasThereABackdatedAwardMadeOnTheHBClaim " + WasThereABackdatedAwardMadeOnTheHBClaim
                + ",DateHBBackdatingFrom " + DateHBBackdatingFrom
                + ",DateHBBackdatingTo " + DateHBBackdatingTo
                + ",TotalAmountOfBackdatesHBAwarded " + TotalAmountOfBackdatedHBAwarded
                + ",CTBClaimTreatAsMadeDate " + CTBClaimTreatAsMadeDate
                + ",SourceOfTheMostRecentCTBClaim " + SourceOfTheMostRecentCTBClaim
                + ",DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim " + DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim
                + ",WasThereABackdatedAwardMadeOnTheCTBClaim " + WasThereABackdatedAwardMadeOnTheCTBClaim
                + ",DateCTBBackdatingFrom " + DateCTBBackdatingFrom
                + ",DateCTBBackdatingTo " + DateCTBBackdatingTo
                + ",TotalAmountOfBackdatedCTBAwarded " + TotalAmountOfBackdatedCTBAwarded
                + ",InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly " + InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly
                + ",IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation " + IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation
                + ",DateOfTransferFromLocalAuthorityLandlordToHousingAssociationRSLIfSubjectToStockTRansferOrLSVT " + DateOfTransferFromLocalAuthorityLandlordToHousingAssociationRSLIfSubjectToStockTRansferOrLSVT
                + ",PartnersTotalCapital " + PartnersTotalCapital
                + ",WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure " + WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure
                + ",ClaimantsTotalHoursOfRemunerativeWorkPerWeek " + ClaimantsTotalHoursOfRemunerativeWorkPerWeek
                + ",PartnersTotalHoursOfRemunerativeWorkPerWeek " + PartnersTotalHoursOfRemunerativeWorkPerWeek
                + ",TotalHBPaymentsCreditsSinceLastExtract " + TotalHBPaymentsCreditsSinceLastExtract
                + ",TotalCTBPaymentsCreditsSinceLastExtract " + TotalCTBPaymentsCreditsSinceLastExtract
                + ",ClaimantsEthnicGroup " + ClaimantsEthnicGroup
                + ",NewWeeklyHBEntitlementAfterTheChange " + NewWeeklyHBEntitlementAfterTheChange
                + ",NewWeeklyCTBEntitlementAfterTheChange " + NewWeeklyCTBEntitlementAfterTheChange
                + ",TypeOfChange " + TypeOfChange
                + ",DateLAFirstNotifiedOfChangeInClaimDetails " + DateLAFirstNotifiedOfChangeInClaimDetails
                + ",DateChangeOfDetailsAreEffectiveFrom " + DateChangeOfDetailsAreEffectiveFrom
                + ",IfNotAnnualUpratingHowWasTheChangeIdentified " + IfNotAnnualUpratingHowWasTheChangeIdentified
                + ",DateSupercessionDecisionWasMadeOnTheHBClaim " + DateSupercessionDecisionWasMadeOnTheHBClaim
                + ",DateSupercessionDecisionWasMadeOnTheCTBClaim " + DateSupercessionDecisionWasMadeOnTheCTBClaim
                + ",DateApplicationForRevisionReconsiderationReceived " + DateApplicationForRevisionReconsiderationReceived
                + ",DateClaimantNotifiedAboutTheDecisionOfTheRevisionReconsideration " + DateClaimantNotifiedAboutTheDecisionOfTheRevisionReconsideration
                + ",DateAppealApplicationWasLodged " + DateAppealApplicationWasLodged
                + ",OutcomeOfAppealApplication " + OutcomeOfAppealApplication
                + ",DateOfOutcomeOfAppealApplication " + DateOfOutcomeOfAppealApplication
                + ",DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentHBClaim " + DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentHBClaim
                + ",DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentHBClaim " + DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentHBClaim
                + ",DateCouncilTaxPayable " + DateCouncilTaxPayable
                + ",DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentCTBClaim " + DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentCTBClaim
                + ",DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentCTBClaim " + DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentCTBClaim
                + ",SoftwareProvider " + SoftwareProvider
                + ",StaffingFTE " + StaffingFTE
                + ",ContractingOutHandlingAndMaintenanceOfHBCTB " + ContractingOutHandlingAndMaintenanceOfHBCTB
                + ",ContractingOutCounterFraudWorkRelatingToHBCTB " + ContractingOutCounterFraudWorkRelatingToHBCTB
                + ",NumberOfBedroomsForLHARolloutCasesOnly " + NumberOfBedroomsForLHARolloutCasesOnly
                + ",PartnersDateOfDeath " + PartnersDateOfDeath
                + ",JointTenancyFlag " + JointTenancyFlag
                + ",AppointeeFlag " + AppointeeFlag
                + ",RentFreeWeeksIndicator " + RentFreeWeeksIndicator
                + ",LastPaidToDate " + LastPaidToDate
                + ",WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability " + WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
                + ",ClaimantsWeeklyIncomeFromESABasicElement " + ClaimantsWeeklyIncomeFromESABasicElement
                + ",PartnersWeeklyIncomeFromESABasicElement " + PartnersWeeklyIncomeFromESABasicElement
                + ",ClaimantsWeeklyIncomeFromESAWRAGElement " + ClaimantsWeeklyIncomeFromESAWRAGElement
                + ",PartnersWeeklyIncomeFromESAWRAGElement " + PartnersWeeklyIncomeFromESAWRAGElement
                + ",ClaimantsWeeklyIncomeFromESASCGElement " + ClaimantsWeeklyIncomeFromESASCGElement
                + ",PartnersWeeklyIncomeFromESASCGElement " + PartnersWeeklyIncomeFromESASCGElement
                + ",WRAGPremiumFlag " + WRAGPremiumFlag
                + ",SCGPremiumFlag " + SCGPremiumFlag
                + ",LandlordPostcode " + LandlordPostcode
                + ",SubRecordType " + SubRecordType;
    }

    /**
     * @return the RecordID
     */
    public long getRecordID() {
        return RecordID;
    }

    /**
     * @param RecordID the RecordID to set
     */
    public void setRecordID(long RecordID) {
        this.RecordID = RecordID;
    }

    /**
     * @return the RecordType
     */
    public String getRecordType() {
        return RecordType;
    }

    /**
     * @param RecordType the RecordType to set
     */
    public void setRecordType(String RecordType) {
        this.RecordType = RecordType;
    }

    /**
     * @return the HousingBenefitClaimReferenceNumber
     */
    public String getHousingBenefitClaimReferenceNumber() {
        return HousingBenefitClaimReferenceNumber;
    }

    /**
     * @param HousingBenefitClaimReferenceNumber the
     * HousingBenefitClaimReferenceNumber to set
     */
    public void setHousingBenefitClaimReferenceNumber(String HousingBenefitClaimReferenceNumber) {
        this.HousingBenefitClaimReferenceNumber = HousingBenefitClaimReferenceNumber;
    }

    /**
     * @return the CouncilTaxBenefitClaimReferenceNumber
     */
    public String getCouncilTaxBenefitClaimReferenceNumber() {
        return CouncilTaxBenefitClaimReferenceNumber;
    }

    /**
     * @param CouncilTaxBenefitClaimReferenceNumber the
     * CouncilTaxBenefitClaimReferenceNumber to set
     */
    public void setCouncilTaxBenefitClaimReferenceNumber(String CouncilTaxBenefitClaimReferenceNumber) {
        this.CouncilTaxBenefitClaimReferenceNumber = CouncilTaxBenefitClaimReferenceNumber;
    }

    /**
     * @return the ClaimantsNationalInsuranceNumber
     */
    public String getClaimantsNationalInsuranceNumber() {
        return ClaimantsNationalInsuranceNumber;
    }

    /**
     * @param ClaimantsNationalInsuranceNumber the
     * ClaimantsNationalInsuranceNumber to set
     */
    public void setClaimantsNationalInsuranceNumber(String ClaimantsNationalInsuranceNumber) {
        this.ClaimantsNationalInsuranceNumber = ClaimantsNationalInsuranceNumber;
    }

    /**
     * @return the ClaimantsDateOfBirth
     */
    public String getClaimantsDateOfBirth() {
        return ClaimantsDateOfBirth;
    }

    /**
     * @param ClaimantsDateOfBirth the ClaimantsDateOfBirth to set
     */
    public void setClaimantsDateOfBirth(String ClaimantsDateOfBirth) {
        this.ClaimantsDateOfBirth = ClaimantsDateOfBirth;
    }

    /**
     * @return the TenancyType
     */
    public int getTenancyType() {
        return TenancyType;
    }

    /**
     * @param TenancyType the TenancyType to set
     */
    public void setTenancyType(int TenancyType) {
        this.TenancyType = TenancyType;
    }

    /**
     * @param n
     * @param fields
     * @throws Exception
     */
    private void setTenancyType(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            TenancyType = -999;
        } else {
            try {
            TenancyType = Integer.valueOf(fields[n]);
            if (TenancyType > 9 || TenancyType < 1) {
                System.err.println("RecordID " + RecordID);
                System.err.println("TenancyType " + fields[n]);
                System.err.println("TenancyType > 9 || TenancyType < 1");
                throw new Exception("TenancyType > 9 || TenancyType < 1");
            }
            } catch (NumberFormatException e) {
                // In September 2014 there are cases where there is an 
                // additional comma before claimants date of birth.
                if (ClaimantsDateOfBirth.trim().isEmpty()) {
                    ClaimantsDateOfBirth = fields[n];
                    System.err.println("ClaimantsDateOfBirth set to " + ClaimantsDateOfBirth);
                    TenancyType = -999;
                    System.err.println("TenancyType set to " + TenancyType);
                }
            }
        }
    }

    /**
     * @return the ClaimantsPostcode
     */
    public String getClaimantsPostcode() {
        return ClaimantsPostcode;
    }

    /**
     * @param ClaimantsPostcode the ClaimantsPostcode to set
     */
    public void setClaimantsPostcode(String ClaimantsPostcode) {
        this.ClaimantsPostcode = ClaimantsPostcode;
    }

    /**
     * @return the PassportedStandardIndicator
     */
    public int getPassportedStandardIndicator() {
        return PassportedStandardIndicator;
    }

    /**
     * @param PassportedStandardIndicator the PassportedStandardIndicator to set
     */
    public void setPassportedStandardIndicator(int PassportedStandardIndicator) {
        this.PassportedStandardIndicator = PassportedStandardIndicator;
    }

    private void setPassportedStandardIndicator(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            setPassportedStandardIndicator(0);
        } else {
            try {
                setPassportedStandardIndicator(Integer.valueOf(fields[n]));
            } catch (NumberFormatException e) {
                // This happened with September 2014 as part of the address for
                // some records was left in the data set instead of being 
                // redacted.
                System.err.println("RecordID " + RecordID);
                System.err.println("ClaimantsPostcode originally set to " + ClaimantsPostcode);
                ClaimantsPostcode = fields[n];
                System.err.println("ClaimantsPostcode now set to " + ClaimantsPostcode);
                n++;
                setPassportedStandardIndicator(n, fields);
                System.err.println("PassportedStandardIndicator set to " + PassportedStandardIndicator);
            }
            if (PassportedStandardIndicator > 5 || PassportedStandardIndicator < 0) {
                System.err.println("RecordID " + RecordID);
                System.err.println("PassportedStandardIndicator " + PassportedStandardIndicator);
                System.err.println("PassportedStandardIndicator > 5 || PassportedStandardIndicator < 0");
                throw new Exception("PassportedStandardIndicator > 5 || PassportedStandardIndicator < 0");
            }
        }
    }

    /**
     * @return the NumberOfChildDependents
     */
    public int getNumberOfChildDependents() {
        return NumberOfChildDependents;
    }

    /**
     * @param NumberOfChildDependents the NumberOfChildDependents to set
     */
    public void setNumberOfChildDependents(int NumberOfChildDependents) {
        this.NumberOfChildDependents = NumberOfChildDependents;
    }

    /**
     * @return the NumberOfNonDependents
     */
    public int getNumberOfNonDependents() {
        return NumberOfNonDependents;
    }

    /**
     * @param NumberOfNonDependents the NumberOfNonDependents to set
     */
    public void setNumberOfNonDependents(int NumberOfNonDependents) {
        this.NumberOfNonDependents = NumberOfNonDependents;
    }

    /**
     * @return the NonDependentStatus
     */
    public int getNonDependentStatus() {
        return NonDependentStatus;
    }

    /**
     * @param NonDependentStatus the NonDependentStatus to set
     */
    public void setNonDependentStatus(int NonDependentStatus) {
        this.NonDependentStatus = NonDependentStatus;
    }

    private void setNonDependentStatus(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            NonDependentStatus = 0;
        } else {
            NonDependentStatus = Integer.valueOf(fields[n]);
        }
        if (NonDependentStatus > 8 || NonDependentStatus < 0) {
            System.err.println("RecordID " + RecordID);
            System.err.println("NonDependentStatus " + NonDependentStatus);
            System.err.println("NonDependentStatus > 8 || NonDependentStatus < 0");
            throw new Exception("NonDependentStatus > 8 || NonDependentStatus < 0");
        }
    }

    /**
     * @return the NonDependentDeductionAmountApplied
     */
    public int getNonDependentDeductionAmountApplied() {
        return NonDependentDeductionAmountApplied;
    }

    /**
     * @param NonDependentDeductionAmountApplied the
     * NonDependentDeductionAmountApplied to set
     */
    public void setNonDependentDeductionAmountApplied(int NonDependentDeductionAmountApplied) {
        this.NonDependentDeductionAmountApplied = NonDependentDeductionAmountApplied;
    }

    /**
     * @return the StatusOfHBClaimAtExtractDate
     */
    public int getStatusOfHBClaimAtExtractDate() {
        return StatusOfHBClaimAtExtractDate;
    }

    /**
     * @param StatusOfHBClaimAtExtractDate the StatusOfHBClaimAtExtractDate to
     * set
     */
    public void setStatusOfHBClaimAtExtractDate(int StatusOfHBClaimAtExtractDate) {
        this.StatusOfHBClaimAtExtractDate = StatusOfHBClaimAtExtractDate;
    }

    private void setStatusOfHBClaimAtExtractDate(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            setStatusOfHBClaimAtExtractDate(-999);
        } else {
            setStatusOfHBClaimAtExtractDate(Integer.valueOf(fields[n]));
            if (StatusOfHBClaimAtExtractDate > 2 || StatusOfHBClaimAtExtractDate < 0) {
                System.err.println("RecordID " + RecordID);
                System.err.println("StatusOfHBClaimAtExtractDate " + StatusOfHBClaimAtExtractDate);
                System.err.println("StatusOfHBClaimAtExtractDate > 2 || StatusOfHBClaimAtExtractDate < 0");
                throw new Exception("StatusOfHBClaimAtExtractDate > 2 || StatusOfHBClaimAtExtractDate < 0");
            }
        }
    }

    /**
     * @return the StatusOfCTBClaimAtExtractDate
     */
    public int getStatusOfCTBClaimAtExtractDate() {
        return StatusOfCTBClaimAtExtractDate;
    }

    /**
     * @param StatusOfCTBClaimAtExtractDate the StatusOfCTBClaimAtExtractDate to
     * set
     */
    public void setStatusOfCTBClaimAtExtractDate(int StatusOfCTBClaimAtExtractDate) {
        this.StatusOfCTBClaimAtExtractDate = StatusOfCTBClaimAtExtractDate;
    }

    private void setStatusOfCTBClaimAtExtractDate(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            StatusOfCTBClaimAtExtractDate = -999;
        } else {
            StatusOfCTBClaimAtExtractDate = Integer.valueOf(fields[n]);
            if (StatusOfCTBClaimAtExtractDate > 2 || StatusOfCTBClaimAtExtractDate < 0) {
                System.err.println("StatusOfCTBClaimAtExtractDate " + StatusOfCTBClaimAtExtractDate);
                System.err.println("StatusOfCTBClaimAtExtractDate > 2 || StatusOfCTBClaimAtExtractDate < 0");
                throw new Exception("StatusOfCTBClaimAtExtractDate > 2 || StatusOfCTBClaimAtExtractDate < 0");
            }
        }
    }

    /**
     * @return the DateMostRecentHBClaimWasReceived
     */
    public String getDateMostRecentHBClaimWasReceived() {
        return DateMostRecentHBClaimWasReceived;
    }

    /**
     * @param DateMostRecentHBClaimWasReceived the
     * DateMostRecentHBClaimWasReceived to set
     */
    public void setDateMostRecentHBClaimWasReceived(String DateMostRecentHBClaimWasReceived) {
        this.DateMostRecentHBClaimWasReceived = DateMostRecentHBClaimWasReceived;
    }

    /**
     * @return the DateMostRecentCTBClaimWasReceived
     */
    public String getDateMostRecentCTBClaimWasReceived() {
        return DateMostRecentCTBClaimWasReceived;
    }

    /**
     * @param DateMostRecentCTBClaimWasReceived the
     * DateMostRecentCTBClaimWasReceived to set
     */
    public void setDateMostRecentCTBClaimWasReceived(String DateMostRecentCTBClaimWasReceived) {
        this.DateMostRecentCTBClaimWasReceived = DateMostRecentCTBClaimWasReceived;
    }

    /**
     * @return the DateOfFirstDecisionOnMostRecentHBClaim
     */
    public String getDateOfFirstDecisionOnMostRecentHBClaim() {
        return DateOfFirstDecisionOnMostRecentHBClaim;
    }

    /**
     * @param DateOfFirstDecisionOnMostRecentHBClaim the
     * DateOfFirstDecisionOnMostRecentHBClaim to set
     */
    public void setDateOfFirstDecisionOnMostRecentHBClaim(String DateOfFirstDecisionOnMostRecentHBClaim) {
        this.DateOfFirstDecisionOnMostRecentHBClaim = DateOfFirstDecisionOnMostRecentHBClaim;
    }

    /**
     * @return the DateOfFirstDecisionOnMostRecentCTBClaim
     */
    public String getDateOfFirstDecisionOnMostRecentCTBClaim() {
        return DateOfFirstDecisionOnMostRecentCTBClaim;
    }

    /**
     * @param DateOfFirstDecisionOnMostRecentCTBClaim the
     * DateOfFirstDecisionOnMostRecentCTBClaim to set
     */
    public void setDateOfFirstDecisionOnMostRecentCTBClaim(String DateOfFirstDecisionOnMostRecentCTBClaim) {
        this.DateOfFirstDecisionOnMostRecentCTBClaim = DateOfFirstDecisionOnMostRecentCTBClaim;
    }

    /**
     * @return the OutcomeOfFirstDecisionOnMostRecentHBClaim
     */
    public int getOutcomeOfFirstDecisionOnMostRecentHBClaim() {
        return OutcomeOfFirstDecisionOnMostRecentHBClaim;
    }

    /**
     * @param OutcomeOfFirstDecisionOnMostRecentHBClaim the
     * OutcomeOfFirstDecisionOnMostRecentHBClaim to set
     */
    public void setOutcomeOfFirstDecisionOnMostRecentHBClaim(int OutcomeOfFirstDecisionOnMostRecentHBClaim) {
        this.OutcomeOfFirstDecisionOnMostRecentHBClaim = OutcomeOfFirstDecisionOnMostRecentHBClaim;
    }

    public void setOutcomeOfFirstDecisionOnMostRecentHBClaim(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            OutcomeOfFirstDecisionOnMostRecentHBClaim = 0;
        } else {
            OutcomeOfFirstDecisionOnMostRecentHBClaim = Integer.valueOf(fields[n]);
        }
        if (OutcomeOfFirstDecisionOnMostRecentHBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentHBClaim < 0) {
            System.err.println("OutcomeOfFirstDecisionOnMostRecentHBClaim " + OutcomeOfFirstDecisionOnMostRecentHBClaim);
            System.err.println("OutcomeOfFirstDecisionOnMostRecentHBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentHBClaim < 0");
            throw new Exception("OutcomeOfFirstDecisionOnMostRecentHBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentHBClaim < 0");
        }
    }

    /**
     * @return the OutcomeOfFirstDecisionOnMostRecentCTBClaim
     */
    public int getOutcomeOfFirstDecisionOnMostRecentCTBClaim() {
        return OutcomeOfFirstDecisionOnMostRecentCTBClaim;
    }

    /**
     * @param OutcomeOfFirstDecisionOnMostRecentCTBClaim the
     * OutcomeOfFirstDecisionOnMostRecentCTBClaim to set
     */
    public void setOutcomeOfFirstDecisionOnMostRecentCTBClaim(int OutcomeOfFirstDecisionOnMostRecentCTBClaim) {
        this.OutcomeOfFirstDecisionOnMostRecentCTBClaim = OutcomeOfFirstDecisionOnMostRecentCTBClaim;
    }

    public void setOutcomeOfFirstDecisionOnMostRecentCTBClaim(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            OutcomeOfFirstDecisionOnMostRecentCTBClaim = 0;
        } else {
            OutcomeOfFirstDecisionOnMostRecentCTBClaim = Integer.valueOf(fields[n]);
            if (OutcomeOfFirstDecisionOnMostRecentCTBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentCTBClaim < 0) {
                System.out.println("OutcomeOfFirstDecisionOnMostRecentCTBClaim " + OutcomeOfFirstDecisionOnMostRecentCTBClaim);
                System.out.println("OutcomeOfFirstDecisionOnMostRecentCTBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentCTBClaim < 0");
                throw new Exception("OutcomeOfFirstDecisionOnMostRecentCTBClaim > 6 || OutcomeOfFirstDecisionOnMostRecentCTBClaim < 0");
            }
        }
    }

    /**
     * @return the HBClaimEntitlementStartDate
     */
    public String getHBClaimEntitlementStartDate() {
        return HBClaimEntitlementStartDate;
    }

    /**
     * @param HBClaimEntitlementStartDate the HBClaimEntitlementStartDate to set
     */
    public void setHBClaimEntitlementStartDate(String HBClaimEntitlementStartDate) {
        this.HBClaimEntitlementStartDate = HBClaimEntitlementStartDate;
    }

    /**
     * @return the WeeklyHousingBenefitEntitlement
     */
    public int getWeeklyHousingBenefitEntitlement() {
        return WeeklyHousingBenefitEntitlement;
    }

    /**
     * @param WeeklyHousingBenefitEntitlement the
     * WeeklyHousingBenefitEntitlement to set
     */
    public void setWeeklyHousingBenefitEntitlement(int WeeklyHousingBenefitEntitlement) {
        this.WeeklyHousingBenefitEntitlement = WeeklyHousingBenefitEntitlement;
    }

    /**
     * @return the WeeklyCouncilTaxBenefitEntitlement
     */
    public int getWeeklyCouncilTaxBenefitEntitlement() {
        return WeeklyCouncilTaxBenefitEntitlement;
    }

    /**
     * @param WeeklyCouncilTaxBenefitEntitlement the
     * WeeklyCouncilTaxBenefitEntitlement to set
     */
    public void setWeeklyCouncilTaxBenefitEntitlement(int WeeklyCouncilTaxBenefitEntitlement) {
        this.WeeklyCouncilTaxBenefitEntitlement = WeeklyCouncilTaxBenefitEntitlement;
    }

    /**
     * @return the FrequencyOfPaymentOfHB
     */
    public int getFrequencyOfPaymentOfHB() {
        return FrequencyOfPaymentOfHB;
    }

    /**
     * @param FrequencyOfPaymentOfHB the FrequencyOfPaymentOfHB to set
     */
    public void setFrequencyOfPaymentOfHB(int FrequencyOfPaymentOfHB) {
        this.FrequencyOfPaymentOfHB = FrequencyOfPaymentOfHB;
    }

    public void setFrequencyOfPaymentOfHB(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            FrequencyOfPaymentOfHB = 0;
        } else {
            FrequencyOfPaymentOfHB = Integer.valueOf(fields[n]);
            if (FrequencyOfPaymentOfHB > 99 || FrequencyOfPaymentOfHB < 0) {
                System.err.println("FrequencyOfPaymentOfHB " + fields[n]);
                System.err.println("FrequencyOfPaymentOfHB > 99 || FrequencyOfPaymentOfHB < 0");
                throw new Exception("FrequencyOfPaymentOfHB > 99 || FrequencyOfPaymentOfHB < 0");
            }
        }
    }

    /**
     * @return the FrequencyOfPaymentOfCTB
     */
    public int getFrequencyOfPaymentOfCTB() {
        return FrequencyOfPaymentOfCTB;
    }

    /**
     * @param FrequencyOfPaymentOfCTB the FrequencyOfPaymentOfCTB to set
     */
    public void setFrequencyOfPaymentOfCTB(int FrequencyOfPaymentOfCTB) {
        this.FrequencyOfPaymentOfCTB = FrequencyOfPaymentOfCTB;
    }

    public void setFrequencyOfPaymentOfCTB(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            FrequencyOfPaymentOfCTB = 0;
        } else {
            FrequencyOfPaymentOfCTB = Integer.valueOf(fields[n]);
        }
//        if (FrequencyOfPaymentOfCTB != 3 && FrequencyOfPaymentOfCTB != 0) {
//            System.err.println("FrequencyOfPaymentOfCTB " + FrequencyOfPaymentOfCTB);
//            System.err.println("FrequencyOfPaymentOfCTB != 3 && FrequencyOfPaymentOfCTB != 0");
//            throw new Exception("FrequencyOfPaymentOfCTB != 3 && FrequencyOfPaymentOfCTB != 0");
//        }
    }

    /**
     * @return the PreDeterminationAmountOfHB
     */
    public int getPreDeterminationAmountOfHB() {
        return PreDeterminationAmountOfHB;
    }

    /**
     * @param PreDeterminationAmountOfHB the PreDeterminationAmountOfHB to set
     */
    public void setPreDeterminationAmountOfHB(int PreDeterminationAmountOfHB) {
        this.PreDeterminationAmountOfHB = PreDeterminationAmountOfHB;
    }

    /**
     * @return the PreDeterminationAmountOfCTB
     */
    public int getPreDeterminationAmountOfCTB() {
        return PreDeterminationAmountOfCTB;
    }

    /**
     * @param PreDeterminationAmountOfCTB the PreDeterminationAmountOfCTB to set
     */
    public void setPreDeterminationAmountOfCTB(int PreDeterminationAmountOfCTB) {
        this.PreDeterminationAmountOfCTB = PreDeterminationAmountOfCTB;
    }

    /**
     * @return the WeeklyHBEntitlementBeforeChange
     */
    public int getWeeklyHBEntitlementBeforeChange() {
        return WeeklyHBEntitlementBeforeChange;
    }

    /**
     * @param WeeklyHBEntitlementBeforeChange the
     * WeeklyHBEntitlementBeforeChange to set
     */
    public void setWeeklyHBEntitlementBeforeChange(int WeeklyHBEntitlementBeforeChange) {
        this.WeeklyHBEntitlementBeforeChange = WeeklyHBEntitlementBeforeChange;
    }

    /**
     * @return the WeeklyCTBEntitlementBeforeChange
     */
    public int getWeeklyCTBEntitlementBeforeChange() {
        return WeeklyCTBEntitlementBeforeChange;
    }

    /**
     * @param WeeklyCTBEntitlementBeforeChange the
     * WeeklyCTBEntitlementBeforeChange to set
     */
    public void setWeeklyCTBEntitlementBeforeChange(int WeeklyCTBEntitlementBeforeChange) {
        this.WeeklyCTBEntitlementBeforeChange = WeeklyCTBEntitlementBeforeChange;
    }

    /**
     * @return the ReasonForDirectPayment
     */
    public int getReasonForDirectPayment() {
        return ReasonForDirectPayment;
    }

    /**
     * @param ReasonForDirectPayment the ReasonForDirectPayment to set
     */
    public void setReasonForDirectPayment(int ReasonForDirectPayment) {
        this.ReasonForDirectPayment = ReasonForDirectPayment;
    }

    public void setReasonForDirectPayment(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            ReasonForDirectPayment = 0;
        } else {
            ReasonForDirectPayment = Integer.valueOf(fields[n]);
            if (ReasonForDirectPayment > 8 || ReasonForDirectPayment < 0) {
                System.err.println("RecordID " + RecordID);
                System.err.println("ReasonForDirectPayment " + ReasonForDirectPayment);
                System.err.println("ReasonForDirectPayment > 8 || ReasonForDirectPayment < 0");
                throw new Exception("ReasonForDirectPayment " + ReasonForDirectPayment + " > 8 || < 0");
            }
        }
    }

    /**
     * @return the TimingOfPaymentOfRentAllowance
     */
    public int getTimingOfPaymentOfRentAllowance() {
        return TimingOfPaymentOfRentAllowance;
    }

    /**
     * @param TimingOfPaymentOfRentAllowance the TimingOfPaymentOfRentAllowance
     * to set
     */
    public void setTimingOfPaymentOfRentAllowance(int TimingOfPaymentOfRentAllowance) {
        this.TimingOfPaymentOfRentAllowance = TimingOfPaymentOfRentAllowance;
    }

    public void setTimingOfPaymentOfRentAllowance(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            TimingOfPaymentOfRentAllowance = 0;
        } else {
            TimingOfPaymentOfRentAllowance = Integer.valueOf(fields[n]);
//            if (TimingOfPaymentOfRentAllowance > 4 || TimingOfPaymentOfRentAllowance < 0) {
//                System.err.println("TimingOfPaymentOfRentAllowance " + TimingOfPaymentOfRentAllowance);
//                System.err.println("TimingOfPaymentOfRentAllowance > 4 || TimingOfPaymentOfRentAllowance < 0");
//                throw new Exception("TimingOfPaymentOfRentAllowance > 4 || TimingOfPaymentOfRentAllowance < 0");
//            }
        }
    }

    /**
     * @return the ExtendedPaymentCase
     */
    public int getExtendedPaymentCase() {
        return ExtendedPaymentCase;
    }

    /**
     * @param ExtendedPaymentCase the ExtendedPaymentCase to set
     */
    public void setExtendedPaymentCase(int ExtendedPaymentCase) {
        this.ExtendedPaymentCase = ExtendedPaymentCase;
    }

    public void setExtendedPaymentCase(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            ExtendedPaymentCase = 0;
        } else {
            ExtendedPaymentCase = Integer.valueOf(fields[n]);
            if (ExtendedPaymentCase > 4 || ExtendedPaymentCase < 0) {
                System.err.println("ExtendedPaymentCase " + ExtendedPaymentCase);
                System.err.println("ExtendedPaymentCase > 4 || ExtendedPaymentCase < 0");
                throw new Exception("ExtendedPaymentCase > 4 || ExtendedPaymentCase < 0");
            }
        }
    }

    /**
     * @return the CouncilTaxBand
     */
    public String getCouncilTaxBand() {
        return CouncilTaxBand;
    }

    /**
     * @param CouncilTaxBand the CouncilTaxBand to set
     */
    public void setCouncilTaxBand(String CouncilTaxBand) {
        this.CouncilTaxBand = CouncilTaxBand;
    }

    /**
     * @return the WeeklyEligibleRentAmount
     */
    public int getWeeklyEligibleRentAmount() {
        return WeeklyEligibleRentAmount;
    }

    /**
     * @param WeeklyEligibleRentAmount the WeeklyEligibleRentAmount to set
     */
    public void setWeeklyEligibleRentAmount(int WeeklyEligibleRentAmount) {
        this.WeeklyEligibleRentAmount = WeeklyEligibleRentAmount;
    }

    /**
     * @return the WeeklyEligibleCouncilTaxAmount
     */
    public int getWeeklyEligibleCouncilTaxAmount() {
        return WeeklyEligibleCouncilTaxAmount;
    }

    /**
     * @param WeeklyEligibleCouncilTaxAmount the WeeklyEligibleCouncilTaxAmount
     * to set
     */
    public void setWeeklyEligibleCouncilTaxAmount(int WeeklyEligibleCouncilTaxAmount) {
        this.WeeklyEligibleCouncilTaxAmount = WeeklyEligibleCouncilTaxAmount;
    }

    /**
     * @return the ClaimantsStudentIndicator
     */
    public String getClaimantsStudentIndicator() {
        return ClaimantsStudentIndicator;
    }

    /**
     * @param ClaimantsStudentIndicator the ClaimantsStudentIndicator to set
     */
    public void setClaimantsStudentIndicator(String ClaimantsStudentIndicator) {
        this.ClaimantsStudentIndicator = ClaimantsStudentIndicator;
    }

    /**
     * @return the RebatePercentageWhereASecondAdultRebateHasBeenAwarded
     */
    public int getRebatePercentageWhereASecondAdultRebateHasBeenAwarded() {
        return RebatePercentageWhereASecondAdultRebateHasBeenAwarded;
    }

    /**
     * @param RebatePercentageWhereASecondAdultRebateHasBeenAwarded the
     * RebatePercentageWhereASecondAdultRebateHasBeenAwarded to set
     */
    public void setRebatePercentageWhereASecondAdultRebateHasBeenAwarded(int RebatePercentageWhereASecondAdultRebateHasBeenAwarded) {
        this.RebatePercentageWhereASecondAdultRebateHasBeenAwarded = RebatePercentageWhereASecondAdultRebateHasBeenAwarded;
    }

    public void setRebatePercentageWhereASecondAdultRebateHasBeenAwarded(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            RebatePercentageWhereASecondAdultRebateHasBeenAwarded = 0;
        } else {
            RebatePercentageWhereASecondAdultRebateHasBeenAwarded = Integer.valueOf(fields[n]);
            if (RebatePercentageWhereASecondAdultRebateHasBeenAwarded > 4 || RebatePercentageWhereASecondAdultRebateHasBeenAwarded < 0) {
                System.err.println("RebatePercentageWhereASecondAdultRebateHasBeenAwarded " + RebatePercentageWhereASecondAdultRebateHasBeenAwarded);
                System.err.println("RebatePercentageWhereASecondAdultRebateHasBeenAwarded > 4 || RebatePercentageWhereASecondAdultRebateHasBeenAwarded < 0");
                throw new Exception("RebatePercentageWhereASecondAdultRebateHasBeenAwarded > 4 || RebatePercentageWhereASecondAdultRebateHasBeenAwarded < 0");
            }
        }
    }

    /**
     * @return the SecondAdultRebate
     */
    public int getSecondAdultRebate() {
        return SecondAdultRebate;
    }

    /**
     * @param SecondAdultRebate the SecondAdultRebate to set
     */
    public void setSecondAdultRebate(int SecondAdultRebate) {
        this.SecondAdultRebate = SecondAdultRebate;
    }

    public void setSecondAdultRebate(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            SecondAdultRebate = -999;
        } else {
            SecondAdultRebate = Integer.valueOf(fields[n]);
            if (SecondAdultRebate > 2 || SecondAdultRebate < 0) {
                System.err.println("SecondAdultRebate " + SecondAdultRebate);
                System.err.println("SecondAdultRebate > 2 || SecondAdultRebate < 1");
                throw new Exception("SecondAdultRebate > 2 || SecondAdultRebate < 1");
            }
        }
    }

    /**
     * @return the LHARegulationsApplied
     */
    public String getLHARegulationsApplied() {
        return LHARegulationsApplied;
    }

    /**
     * @param LHARegulationsApplied the LHARegulationsApplied to set
     */
    public void setLHARegulationsApplied(String LHARegulationsApplied) {
        this.LHARegulationsApplied = LHARegulationsApplied;
    }

    /**
     * @return the IsThisCaseSubjectToLRROrSRRSchemes
     */
    public int getIsThisCaseSubjectToLRROrSRRSchemes() {
        return IsThisCaseSubjectToLRROrSRRSchemes;
    }

    /**
     * @param IsThisCaseSubjectToLRROrSRRSchemes the
     * IsThisCaseSubjectToLRROrSRRSchemes to set
     */
    public void setIsThisCaseSubjectToLRROrSRRSchemes(int IsThisCaseSubjectToLRROrSRRSchemes) {
        this.IsThisCaseSubjectToLRROrSRRSchemes = IsThisCaseSubjectToLRROrSRRSchemes;
    }

    public void setIsThisCaseSubjectToLRROrSRRSchemes(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            IsThisCaseSubjectToLRROrSRRSchemes = -999;
        } else {
            IsThisCaseSubjectToLRROrSRRSchemes = Integer.valueOf(fields[n]);
            if (IsThisCaseSubjectToLRROrSRRSchemes > 4 || IsThisCaseSubjectToLRROrSRRSchemes < 1) {
                System.err.println("IsThisCaseSubjectToLRROrSRRSchemes " + IsThisCaseSubjectToLRROrSRRSchemes);
                System.err.println("IsThisCaseSubjectToLRROrSRRSchemes > 4 || IsThisCaseSubjectToLRROrSRRSchemes < 1");
                throw new Exception("IsThisCaseSubjectToLRROrSRRSchemes > 4 || IsThisCaseSubjectToLRROrSRRSchemes < 1");
            }
        }
    }

    /**
     * @return the WeeklyLocalReferenceRent
     */
    public int getWeeklyLocalReferenceRent() {
        return WeeklyLocalReferenceRent;
    }

    /**
     * @param WeeklyLocalReferenceRent the WeeklyLocalReferenceRent to set
     */
    public void setWeeklyLocalReferenceRent(int WeeklyLocalReferenceRent) {
        this.WeeklyLocalReferenceRent = WeeklyLocalReferenceRent;
    }

    /**
     * @return the WeeklySingleRoomRent
     */
    public int getWeeklySingleRoomRent() {
        return WeeklySingleRoomRent;
    }

    /**
     * @param WeeklySingleRoomRent the WeeklySingleRoomRent to set
     */
    public void setWeeklySingleRoomRent(int WeeklySingleRoomRent) {
        this.WeeklySingleRoomRent = WeeklySingleRoomRent;
    }

    /**
     * @return the WeeklyClaimRelatedRent
     */
    public int getWeelklyClaimRelatedRent() {
        return WeeklyClaimRelatedRent;
    }

    /**
     * @param WeeklyClaimRelatedRent the WeeklyClaimRelatedRent to set
     */
    public void setWeeklyClaimRelatedRent(int WeeklyClaimRelatedRent) {
        this.WeeklyClaimRelatedRent = WeeklyClaimRelatedRent;
    }

    /**
     * @return the RentOfficerDeterminationOfIneligibleCharges
     */
    public int getRentOfficerDeterminationOfIneligibleCharges() {
        return RentOfficerDeterminationOfIneligibleCharges;
    }

    /**
     * @param RentOfficerDeterminationOfIneligibleCharges the
     * RentOfficerDeterminationOfIneligibleCharges to set
     */
    public void setRentOfficerDeterminationOfIneligibleCharges(int RentOfficerDeterminationOfIneligibleCharges) {
        this.RentOfficerDeterminationOfIneligibleCharges = RentOfficerDeterminationOfIneligibleCharges;
    }

    /**
     * @return the WeeklyMaximumRent
     */
    public int getWeeklyMaximumRent() {
        return WeeklyMaximumRent;
    }

    /**
     * @param WeeklyMaximumRent the WeeklyMaximumRent to set
     */
    public void setWeeklyMaximumRent(int WeeklyMaximumRent) {
        this.WeeklyMaximumRent = WeeklyMaximumRent;
    }

    /**
     * @return the TotalDeductionForMeals
     */
    public int getTotalDeductionForMeals() {
        return TotalDeductionForMeals;
    }

    /**
     * @param TotalDeductionForMeals the TotalDeductionForMeals to set
     */
    public void setTotalDeductionForMeals(int TotalDeductionForMeals) {
        this.TotalDeductionForMeals = TotalDeductionForMeals;
    }

    /**
     * @return the WeeklyAdditionalDiscretionaryPayment
     */
    public int getWeeklyAdditionalDiscretionaryPayment() {
        return WeeklyAdditionalDiscretionaryPayment;
    }

    /**
     * @param WeeklyAdditionalDiscretionaryPayment the
     * WeeklyAdditionalDiscretionaryPayment to set
     */
    public void setWeeklyAdditionalDiscretionaryPayment(int WeeklyAdditionalDiscretionaryPayment) {
        this.WeeklyAdditionalDiscretionaryPayment = WeeklyAdditionalDiscretionaryPayment;
    }

    /**
     * @return the ThirteenOrFiftyTwoWeekProtectionApplies
     */
    public int getThirteenOrFiftyTwoWeekProtectionApplies() {
        return ThirteenOrFiftyTwoWeekProtectionApplies;
    }

    /**
     * @param ThirteenOrFiftyTwoWeekProtectionApplies the
     * ThirteenOrFiftyTwoWeekProtectionApplies to set
     */
    public void setThirteenOrFiftyTwoWeekProtectionApplies(int ThirteenOrFiftyTwoWeekProtectionApplies) {
        this.ThirteenOrFiftyTwoWeekProtectionApplies = ThirteenOrFiftyTwoWeekProtectionApplies;
    }

    public void setThirteenOrFiftyTwoWeekProtectionApplies(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            ThirteenOrFiftyTwoWeekProtectionApplies = 0;
        } else {
            ThirteenOrFiftyTwoWeekProtectionApplies = Integer.valueOf(fields[n]);
            if (ThirteenOrFiftyTwoWeekProtectionApplies > 1 || ThirteenOrFiftyTwoWeekProtectionApplies < 0) {
                System.err.println("ThirteenOrFiftyTwoWeekProtectionApplies " + ThirteenOrFiftyTwoWeekProtectionApplies);
                System.err.println("ThirteenOrFiftyTwoWeekProtectionApplies > 1 || ThirteenOrFiftyTwoWeekProtectionApplies < 0");
                throw new Exception("ThirteenOrFiftyTwoWeekProtectionApplies > 1 || ThirteenOrFiftyTwoWeekProtectionApplies < 0");
            }
        }
    }

    /**
     * @return the DateOfFirstPaymentOnMostRecentHBClaimFollowingAFullDecision
     */
    public String getDateOfFirstPaymentOnMostRecentHBClaimFollowingAFullDecision() {
        return DateOfFirstPaymentOnMostRecentHBClaimFollowingAFullDecision;
    }

    /**
     * @param DateOfFirstPaymentOnMostRecentHBClaimFollowingAFullDecision the
     * DateOfFirstPaymentOnMostRecentHBClaimFollowingAFullDecision to set
     */
    public void setDateOfFirstPaymentOnMostRecentHBClaimFollowingAFullDecision(String DateOfFirstPaymentOnMostRecentHBClaimFollowingAFullDecision) {
        this.DateOfFirstPaymentOnMostRecentHBClaimFollowingAFullDecision = DateOfFirstPaymentOnMostRecentHBClaimFollowingAFullDecision;
    }

    /**
     * @return the ClaimantsAssessedIncomeFigure
     */
    public int getClaimantsAssessedIncomeFigure() {
        return ClaimantsAssessedIncomeFigure;
    }

    /**
     * @param ClaimantsAssessedIncomeFigure the ClaimantsAssessedIncomeFigure to
     * set
     */
    public void setClaimantsAssessedIncomeFigure(int ClaimantsAssessedIncomeFigure) {
        this.ClaimantsAssessedIncomeFigure = ClaimantsAssessedIncomeFigure;
    }

    /**
     * @return the ClaimantsAdjustedAssessedIncomeFigure
     */
    public int getClaimantsAdjustedAssessedIncomeFigure() {
        return ClaimantsAdjustedAssessedIncomeFigure;
    }

    /**
     * @param ClaimantsAdjustedAssessedIncomeFigure the
     * ClaimantsAdjustedAssessedIncomeFigure to set
     */
    public void setClaimantsAdjustedAssessedIncomeFigure(int ClaimantsAdjustedAssessedIncomeFigure) {
        this.ClaimantsAdjustedAssessedIncomeFigure = ClaimantsAdjustedAssessedIncomeFigure;
    }

    /**
     * @return the ClaimantsTotalCapital
     */
    public int getClaimantsTotalCapital() {
        return ClaimantsTotalCapital;
    }

    /**
     * @param ClaimantsTotalCapital the ClaimantsTotalCapital to set
     */
    public void setClaimantsTotalCapital(int ClaimantsTotalCapital) {
        this.ClaimantsTotalCapital = ClaimantsTotalCapital;
    }

    /**
     * @return the ClaimantsGrossWeeklyIncomeFromEmployment
     */
    public int getClaimantsGrossWeeklyIncomeFromEmployment() {
        return ClaimantsGrossWeeklyIncomeFromEmployment;
    }

    /**
     * @param ClaimantsGrossWeeklyIncomeFromEmployment the
     * ClaimantsGrossWeeklyIncomeFromEmployment to set
     */
    public void setClaimantsGrossWeeklyIncomeFromEmployment(int ClaimantsGrossWeeklyIncomeFromEmployment) {
        this.ClaimantsGrossWeeklyIncomeFromEmployment = ClaimantsGrossWeeklyIncomeFromEmployment;
    }

    /**
     * @return the ClaimantsNetWeeklyIncomeFromEmployment
     */
    public int getClaimantsNetWeeklyIncomeFromEmployment() {
        return ClaimantsNetWeeklyIncomeFromEmployment;
    }

    /**
     * @param ClaimantsNetWeeklyIncomeFromEmployment the
     * ClaimantsNetWeeklyIncomeFromEmployment to set
     */
    public void setClaimantsNetWeeklyIncomeFromEmployment(int ClaimantsNetWeeklyIncomeFromEmployment) {
        this.ClaimantsNetWeeklyIncomeFromEmployment = ClaimantsNetWeeklyIncomeFromEmployment;
    }

    /**
     * @return the ClaimantsGrossWeeklyIncomeFromSelfEmployment
     */
    public int getClaimantsGrossWeeklyIncomeFromSelfEmployment() {
        return ClaimantsGrossWeeklyIncomeFromSelfEmployment;
    }

    /**
     * @param ClaimantsGrossWeeklyIncomeFromSelfEmployment the
     * ClaimantsGrossWeeklyIncomeFromSelfEmployment to set
     */
    public void setClaimantsGrossWeeklyIncomeFromSelfEmployment(int ClaimantsGrossWeeklyIncomeFromSelfEmployment) {
        this.ClaimantsGrossWeeklyIncomeFromSelfEmployment = ClaimantsGrossWeeklyIncomeFromSelfEmployment;
    }

    /**
     * @return the ClaimantsNetWeeklyIncomeFromSelfEmployment
     */
    public int getClaimantsNetWeeklyIncomeFromSelfEmployment() {
        return ClaimantsNetWeeklyIncomeFromSelfEmployment;
    }

    /**
     * @param ClaimantsNetWeeklyIncomeFromSelfEmployment the
     * ClaimantsNetWeeklyIncomeFromSelfEmployment to set
     */
    public void setClaimantsNetWeeklyIncomeFromSelfEmployment(int ClaimantsNetWeeklyIncomeFromSelfEmployment) {
        this.ClaimantsNetWeeklyIncomeFromSelfEmployment = ClaimantsNetWeeklyIncomeFromSelfEmployment;
    }

    /**
     * @return the ClaimantsTotalAmountOfEarningsDisregarded
     */
    public int getClaimantsTotalAmountOfEarningsDisregarded() {
        return ClaimantsTotalAmountOfEarningsDisregarded;
    }

    /**
     * @param ClaimantsTotalAmountOfEarningsDisregarded the
     * ClaimantsTotalAmountOfEarningsDisregarded to set
     */
    public void setClaimantsTotalAmountOfEarningsDisregarded(int ClaimantsTotalAmountOfEarningsDisregarded) {
        this.ClaimantsTotalAmountOfEarningsDisregarded = ClaimantsTotalAmountOfEarningsDisregarded;
    }

    /**
     * @return the
     * ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded
     */
    public int getClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded() {
        return ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded;
    }

    /**
     * @param ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded
     * the ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded to
     * set
     */
    public void setClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded(int ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded) {
        this.ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = ClaimantsIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded;
    }

    /**
     * @return the ClaimantsIncomeFromAttendanceAllowance
     */
    public int getClaimantsIncomeFromAttendanceAllowance() {
        return ClaimantsIncomeFromAttendanceAllowance;
    }

    /**
     * @param ClaimantsIncomeFromAttendanceAllowance the
     * ClaimantsIncomeFromAttendanceAllowance to set
     */
    public void setClaimantsIncomeFromAttendanceAllowance(int ClaimantsIncomeFromAttendanceAllowance) {
        this.ClaimantsIncomeFromAttendanceAllowance = ClaimantsIncomeFromAttendanceAllowance;
    }

    /**
     * @return the ClaimantsIncomeFromBusinessStartUpAllowance
     */
    public int getClaimantsIncomeFromBusinessStartUpAllowance() {
        return ClaimantsIncomeFromBusinessStartUpAllowance;
    }

    /**
     * @param ClaimantsIncomeFromBusinessStartUpAllowance the
     * ClaimantsIncomeFromBusinessStartUpAllowance to set
     */
    public void setClaimantsIncomeFromBusinessStartUpAllowance(int ClaimantsIncomeFromBusinessStartUpAllowance) {
        this.ClaimantsIncomeFromBusinessStartUpAllowance = ClaimantsIncomeFromBusinessStartUpAllowance;
    }

    /**
     * @return the ClaimantsIncomeFromChildBenefit
     */
    public int getClaimantsIncomeFromChildBenefit() {
        return ClaimantsIncomeFromChildBenefit;
    }

    /**
     * @param ClaimantsIncomeFromChildBenefit the
     * ClaimantsIncomeFromChildBenefit to set
     */
    public void setClaimantsIncomeFromChildBenefit(int ClaimantsIncomeFromChildBenefit) {
        this.ClaimantsIncomeFromChildBenefit = ClaimantsIncomeFromChildBenefit;
    }

    /**
     * @return the ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent
     */
    public int getClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent() {
        return ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent;
    }

    /**
     * @param ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent the
     * ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent to set
     */
    public void setClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent(int ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent) {
        this.ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent = ClaimantsIncomeFromOneParentBenefitChildBenefitLoneParent;
    }

    /**
     * @return the ClaimantsIncomeFromPersonalPension
     */
    public int getClaimantsIncomeFromPersonalPension() {
        return ClaimantsIncomeFromPersonalPension;
    }

    /**
     * @param ClaimantsIncomeFromPersonalPension the
     * ClaimantsIncomeFromPersonalPension to set
     */
    public void setClaimantsIncomeFromPersonalPension(int ClaimantsIncomeFromPersonalPension) {
        this.ClaimantsIncomeFromPersonalPension = ClaimantsIncomeFromPersonalPension;
    }

    /**
     * @return the ClaimantsIncomeFromSevereDisabilityAllowance
     */
    public int getClaimantsIncomeFromSevereDisabilityAllowance() {
        return ClaimantsIncomeFromSevereDisabilityAllowance;
    }

    /**
     * @param ClaimantsIncomeFromSevereDisabilityAllowance the
     * ClaimantsIncomeFromSevereDisabilityAllowance to set
     */
    public void setClaimantsIncomeFromSevereDisabilityAllowance(int ClaimantsIncomeFromSevereDisabilityAllowance) {
        this.ClaimantsIncomeFromSevereDisabilityAllowance = ClaimantsIncomeFromSevereDisabilityAllowance;
    }

    /**
     * @return the ClaimantsIncomeFromMaternityAllowance
     */
    public int getClaimantsIncomeFromMaternityAllowance() {
        return ClaimantsIncomeFromMaternityAllowance;
    }

    /**
     * @param ClaimantsIncomeFromMaternityAllowance the
     * ClaimantsIncomeFromMaternityAllowance to set
     */
    public void setClaimantsIncomeFromMaternityAllowance(int ClaimantsIncomeFromMaternityAllowance) {
        this.ClaimantsIncomeFromMaternityAllowance = ClaimantsIncomeFromMaternityAllowance;
    }

    /**
     * @return the ClaimantsIncomeFromContributionBasedJobSeekersAllowance
     */
    public int getClaimantsIncomeFromContributionBasedJobSeekersAllowance() {
        return ClaimantsIncomeFromContributionBasedJobSeekersAllowance;
    }

    /**
     * @param ClaimantsIncomeFromContributionBasedJobSeekersAllowance the
     * ClaimantsIncomeFromContributionBasedJobSeekersAllowance to set
     */
    public void setClaimantsIncomeFromContributionBasedJobSeekersAllowance(int ClaimantsIncomeFromContributionBasedJobSeekersAllowance) {
        this.ClaimantsIncomeFromContributionBasedJobSeekersAllowance = ClaimantsIncomeFromContributionBasedJobSeekersAllowance;
    }

    /**
     * @return the ClaimantsIncomeFromStudentGrantLoan
     */
    public int getClaimantsIncomeFromStudentGrantLoan() {
        return ClaimantsIncomeFromStudentGrantLoan;
    }

    /**
     * @param ClaimantsIncomeFromStudentGrantLoan the
     * ClaimantsIncomeFromStudentGrantLoan to set
     */
    public void setClaimantsIncomeFromStudentGrantLoan(int ClaimantsIncomeFromStudentGrantLoan) {
        this.ClaimantsIncomeFromStudentGrantLoan = ClaimantsIncomeFromStudentGrantLoan;
    }

    /**
     * @return the ClaimantsIncomeFromSubTenants
     */
    public int getClaimantsIncomeFromSubTenants() {
        return ClaimantsIncomeFromSubTenants;
    }

    /**
     * @param ClaimantsIncomeFromSubTenants the ClaimantsIncomeFromSubTenants to
     * set
     */
    public void setClaimantsIncomeFromSubTenants(int ClaimantsIncomeFromSubTenants) {
        this.ClaimantsIncomeFromSubTenants = ClaimantsIncomeFromSubTenants;
    }

    /**
     * @return the ClaimantsIncomeFromBoarders
     */
    public int getClaimantsIncomeFromBoarders() {
        return ClaimantsIncomeFromBoarders;
    }

    /**
     * @param ClaimantsIncomeFromBoarders the ClaimantsIncomeFromBoarders to set
     */
    public void setClaimantsIncomeFromBoarders(int ClaimantsIncomeFromBoarders) {
        this.ClaimantsIncomeFromBoarders = ClaimantsIncomeFromBoarders;
    }

    /**
     * @return the ClaimantsIncomeFromTrainingForWorkCommunityAction
     */
    public int getClaimantsIncomeFromTrainingForWorkCommunityAction() {
        return ClaimantsIncomeFromTrainingForWorkCommunityAction;
    }

    /**
     * @param ClaimantsIncomeFromTrainingForWorkCommunityAction the
     * ClaimantsIncomeFromTrainingForWorkCommunityAction to set
     */
    public void setClaimantsIncomeFromTrainingForWorkCommunityAction(int ClaimantsIncomeFromTrainingForWorkCommunityAction) {
        this.ClaimantsIncomeFromTrainingForWorkCommunityAction = ClaimantsIncomeFromTrainingForWorkCommunityAction;
    }

    /**
     * @return the ClaimantsIncomeFromIncapacityBenefitShortTermLower
     */
    public int getClaimantsIncomeFromIncapacityBenefitShortTermLower() {
        return ClaimantsIncomeFromIncapacityBenefitShortTermLower;
    }

    /**
     * @param ClaimantsIncomeFromIncapacityBenefitShortTermLower the
     * ClaimantsIncomeFromIncapacityBenefitShortTermLower to set
     */
    public void setClaimantsIncomeFromIncapacityBenefitShortTermLower(int ClaimantsIncomeFromIncapacityBenefitShortTermLower) {
        this.ClaimantsIncomeFromIncapacityBenefitShortTermLower = ClaimantsIncomeFromIncapacityBenefitShortTermLower;
    }

    /**
     * @return the ClaimantsIncomeFromIncapacityBenefitShortTermHigher
     */
    public int getClaimantsIncomeFromIncapacityBenefitShortTermHigher() {
        return ClaimantsIncomeFromIncapacityBenefitShortTermHigher;
    }

    /**
     * @param ClaimantsIncomeFromIncapacityBenefitShortTermHigher the
     * ClaimantsIncomeFromIncapacityBenefitShortTermHigher to set
     */
    public void setClaimantsIncomeFromIncapacityBenefitShortTermHigher(int ClaimantsIncomeFromIncapacityBenefitShortTermHigher) {
        this.ClaimantsIncomeFromIncapacityBenefitShortTermHigher = ClaimantsIncomeFromIncapacityBenefitShortTermHigher;
    }

    /**
     * @return the ClaimantsIncomeFromIncapacityBenefitLongTerm
     */
    public int getClaimantsIncomeFromIncapacityBenefitLongTerm() {
        return ClaimantsIncomeFromIncapacityBenefitLongTerm;
    }

    /**
     * @param ClaimantsIncomeFromIncapacityBenefitLongTerm the
     * ClaimantsIncomeFromIncapacityBenefitLongTerm to set
     */
    public void setClaimantsIncomeFromIncapacityBenefitLongTerm(int ClaimantsIncomeFromIncapacityBenefitLongTerm) {
        this.ClaimantsIncomeFromIncapacityBenefitLongTerm = ClaimantsIncomeFromIncapacityBenefitLongTerm;
    }

    /**
     * @return the ClaimantsIncomeFromNewDeal50PlusEmploymentCredit
     */
    public int getClaimantsIncomeFromNewDeal50PlusEmploymentCredit() {
        return ClaimantsIncomeFromNewDeal50PlusEmploymentCredit;
    }

    /**
     * @param ClaimantsIncomeFromNewDeal50PlusEmploymentCredit the
     * ClaimantsIncomeFromNewDeal50PlusEmploymentCredit to set
     */
    public void setClaimantsIncomeFromNewDeal50PlusEmploymentCredit(int ClaimantsIncomeFromNewDeal50PlusEmploymentCredit) {
        this.ClaimantsIncomeFromNewDeal50PlusEmploymentCredit = ClaimantsIncomeFromNewDeal50PlusEmploymentCredit;
    }

    /**
     * @return the ClaimantsIncomeFromNewTaxCredits
     */
    public int getClaimantsIncomeFromNewTaxCredits() {
        return ClaimantsIncomeFromNewTaxCredits;
    }

    /**
     * @param ClaimantsIncomeFromNewTaxCredits the
     * ClaimantsIncomeFromNewTaxCredits to set
     */
    public void setClaimantsIncomeFromNewTaxCredits(int ClaimantsIncomeFromNewTaxCredits) {
        this.ClaimantsIncomeFromNewTaxCredits = ClaimantsIncomeFromNewTaxCredits;
    }

    /**
     * @return the ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent
     */
    public int getClaimantsIncomeFromDisabilityLivingAllowanceCareComponent() {
        return ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent;
    }

    /**
     * @param ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent the
     * ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent to set
     */
    public void setClaimantsIncomeFromDisabilityLivingAllowanceCareComponent(int ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent) {
        this.ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent = ClaimantsIncomeFromDisabilityLivingAllowanceCareComponent;
    }

    /**
     * @return the ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent
     */
    public int getClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent() {
        return ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent;
    }

    /**
     * @param ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent the
     * ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent to set
     */
    public void setClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent(int ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent) {
        this.ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent = ClaimantsIncomeFromDisabilityLivingAllowanceMobilityComponent;
    }

    /**
     * @return the ClaimantsIncomeFromGovernemntTraining
     */
    public int getClaimantsIncomeFromGovernemntTraining() {
        return ClaimantsIncomeFromGovernemntTraining;
    }

    /**
     * @param ClaimantsIncomeFromGovernemntTraining the
     * ClaimantsIncomeFromGovernemntTraining to set
     */
    public void setClaimantsIncomeFromGovernemntTraining(int ClaimantsIncomeFromGovernemntTraining) {
        this.ClaimantsIncomeFromGovernemntTraining = ClaimantsIncomeFromGovernemntTraining;
    }

    /**
     * @return the ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit
     */
    public int getClaimantsIncomeFromIndustrialInjuriesDisablementBenefit() {
        return ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit;
    }

    /**
     * @param ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit the
     * ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit to set
     */
    public void setClaimantsIncomeFromIndustrialInjuriesDisablementBenefit(int ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit) {
        this.ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit = ClaimantsIncomeFromIndustrialInjuriesDisablementBenefit;
    }

    /**
     * @return the ClaimantsIncomeFromCarersAllowance
     */
    public int getClaimantsIncomeFromCarersAllowance() {
        return ClaimantsIncomeFromCarersAllowance;
    }

    /**
     * @param ClaimantsIncomeFromCarersAllowance the
     * ClaimantsIncomeFromCarersAllowance to set
     */
    public void setClaimantsIncomeFromCarersAllowance(int ClaimantsIncomeFromCarersAllowance) {
        this.ClaimantsIncomeFromCarersAllowance = ClaimantsIncomeFromCarersAllowance;
    }

    /**
     * @return the ClaimantsIncomeFromStatutoryMaternityPaternityPay
     */
    public int getClaimantsIncomeFromStatutoryMaternityPaternityPay() {
        return ClaimantsIncomeFromStatutoryMaternityPaternityPay;
    }

    /**
     * @param ClaimantsIncomeFromStatutoryMaternityPaternityPay the
     * ClaimantsIncomeFromStatutoryMaternityPaternityPay to set
     */
    public void setClaimantsIncomeFromStatutoryMaternityPaternityPay(int ClaimantsIncomeFromStatutoryMaternityPaternityPay) {
        this.ClaimantsIncomeFromStatutoryMaternityPaternityPay = ClaimantsIncomeFromStatutoryMaternityPaternityPay;
    }

    /**
     * @return the
     * ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc
     */
    public int getClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc() {
        return ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc;
    }

    /**
     * @param
     * ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc
     * the
     * ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc
     * to set
     */
    public void setClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc(int ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc) {
        this.ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = ClaimantsIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc;
    }

    /**
     * @return the ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP
     */
    public int getClaimantsIncomeFromWarDisablementPensionArmedForcesGIP() {
        return ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP;
    }

    /**
     * @param ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP the
     * ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP to set
     */
    public void setClaimantsIncomeFromWarDisablementPensionArmedForcesGIP(int ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP) {
        this.ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP = ClaimantsIncomeFromWarDisablementPensionArmedForcesGIP;
    }

    /**
     * @return the ClaimantsIncomeFromWarMobilitySupplement
     */
    public int getClaimantsIncomeFromWarMobilitySupplement() {
        return ClaimantsIncomeFromWarMobilitySupplement;
    }

    /**
     * @param ClaimantsIncomeFromWarMobilitySupplement the
     * ClaimantsIncomeFromWarMobilitySupplement to set
     */
    public void setClaimantsIncomeFromWarMobilitySupplement(int ClaimantsIncomeFromWarMobilitySupplement) {
        this.ClaimantsIncomeFromWarMobilitySupplement = ClaimantsIncomeFromWarMobilitySupplement;
    }

    /**
     * @return the ClaimantsIncomeFromWidowsWidowersPension
     */
    public int getClaimantsIncomeFromWidowsWidowersPension() {
        return ClaimantsIncomeFromWidowsWidowersPension;
    }

    /**
     * @param ClaimantsIncomeFromWidowsWidowersPension the
     * ClaimantsIncomeFromWidowsWidowersPension to set
     */
    public void setClaimantsIncomeFromWidowsWidowersPension(int ClaimantsIncomeFromWidowsWidowersPension) {
        this.ClaimantsIncomeFromWidowsWidowersPension = ClaimantsIncomeFromWidowsWidowersPension;
    }

    /**
     * @return the ClaimantsIncomeFromBereavementAllowance
     */
    public int getClaimantsIncomeFromBereavementAllowance() {
        return ClaimantsIncomeFromBereavementAllowance;
    }

    /**
     * @param ClaimantsIncomeFromBereavementAllowance the
     * ClaimantsIncomeFromBereavementAllowance to set
     */
    public void setClaimantsIncomeFromBereavementAllowance(int ClaimantsIncomeFromBereavementAllowance) {
        this.ClaimantsIncomeFromBereavementAllowance = ClaimantsIncomeFromBereavementAllowance;
    }

    /**
     * @return the ClaimantsIncomeFromWidowedParentsAllowance
     */
    public int getClaimantsIncomeFromWidowedParentsAllowance() {
        return ClaimantsIncomeFromWidowedParentsAllowance;
    }

    /**
     * @param ClaimantsIncomeFromWidowedParentsAllowance the
     * ClaimantsIncomeFromWidowedParentsAllowance to set
     */
    public void setClaimantsIncomeFromWidowedParentsAllowance(int ClaimantsIncomeFromWidowedParentsAllowance) {
        this.ClaimantsIncomeFromWidowedParentsAllowance = ClaimantsIncomeFromWidowedParentsAllowance;
    }

    /**
     * @return the ClaimantsIncomeFromYouthTrainingScheme
     */
    public int getClaimantsIncomeFromYouthTrainingScheme() {
        return ClaimantsIncomeFromYouthTrainingScheme;
    }

    /**
     * @param ClaimantsIncomeFromYouthTrainingScheme the
     * ClaimantsIncomeFromYouthTrainingScheme to set
     */
    public void setClaimantsIncomeFromYouthTrainingScheme(int ClaimantsIncomeFromYouthTrainingScheme) {
        this.ClaimantsIncomeFromYouthTrainingScheme = ClaimantsIncomeFromYouthTrainingScheme;
    }

    /**
     * @return the ClaimantsIncomeFromStatuatorySickPay
     */
    public int getClaimantsIncomeFromStatuatorySickPay() {
        return ClaimantsIncomeFromStatuatorySickPay;
    }

    /**
     * @param ClaimantsIncomeFromStatuatorySickPay the
     * ClaimantsIncomeFromStatuatorySickPay to set
     */
    public void setClaimantsIncomeFromStatuatorySickPay(int ClaimantsIncomeFromStatuatorySickPay) {
        this.ClaimantsIncomeFromStatuatorySickPay = ClaimantsIncomeFromStatuatorySickPay;
    }

    /**
     * @return the ClaimantsOtherIncome
     */
    public int getClaimantsOtherIncome() {
        return ClaimantsOtherIncome;
    }

    /**
     * @param ClaimantsOtherIncome the ClaimantsOtherIncome to set
     */
    public void setClaimantsOtherIncome(int ClaimantsOtherIncome) {
        this.ClaimantsOtherIncome = ClaimantsOtherIncome;
    }

    /**
     * @return the ClaimantsTotalAmountOfIncomeDisregarded
     */
    public int getClaimantsTotalAmountOfIncomeDisregarded() {
        return ClaimantsTotalAmountOfIncomeDisregarded;
    }

    /**
     * @param ClaimantsTotalAmountOfIncomeDisregarded the
     * ClaimantsTotalAmountOfIncomeDisregarded to set
     */
    public void setClaimantsTotalAmountOfIncomeDisregarded(int ClaimantsTotalAmountOfIncomeDisregarded) {
        this.ClaimantsTotalAmountOfIncomeDisregarded = ClaimantsTotalAmountOfIncomeDisregarded;
    }

    /**
     * @return the FamilyPremiumAwarded
     */
    public int getFamilyPremiumAwarded() {
        return FamilyPremiumAwarded;
    }

    /**
     * @param FamilyPremiumAwarded the FamilyPremiumAwarded to set
     */
    public void setFamilyPremiumAwarded(int FamilyPremiumAwarded) {
        this.FamilyPremiumAwarded = FamilyPremiumAwarded;
    }

    public void setFamilyPremiumAwarded(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            FamilyPremiumAwarded = 0;
        } else {
            FamilyPremiumAwarded = Integer.valueOf(fields[n]);
            if (FamilyPremiumAwarded > 1 || FamilyPremiumAwarded < 0) {
                System.err.println("FamilyPremiumAwarded " + FamilyPremiumAwarded);
                System.err.println("FamilyPremiumAwarded > 1 || FamilyPremiumAwarded < 0");
                throw new Exception("FamilyPremiumAwarded > 1 || FamilyPremiumAwarded < 0");
            }
        }
    }

    /**
     * @return the FamilyLoneParentPremiumAwarded
     */
    public int getFamilyLoneParentPremiumAwarded() {
        return FamilyLoneParentPremiumAwarded;
    }

    /**
     * @param FamilyLoneParentPremiumAwarded the FamilyLoneParentPremiumAwarded
     * to set
     */
    public void setFamilyLoneParentPremiumAwarded(int FamilyLoneParentPremiumAwarded) {
        this.FamilyLoneParentPremiumAwarded = FamilyLoneParentPremiumAwarded;
    }

    public void setFamilyLoneParentPremiumAwarded(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            FamilyLoneParentPremiumAwarded = 0;
        } else {
            FamilyLoneParentPremiumAwarded = Integer.valueOf(fields[n]);
            if (FamilyLoneParentPremiumAwarded > 1 || FamilyLoneParentPremiumAwarded < 0) {
                System.err.println("FamilyLoneParentPremiumAwarded " + FamilyLoneParentPremiumAwarded);
                System.err.println("FamilyLoneParentPremiumAwarded > 1 || FamilyLoneParentPremiumAwarded < 0");
                throw new Exception("FamilyLoneParentPremiumAwarded > 1 || FamilyLoneParentPremiumAwarded < 0");
            }
        }
    }

    /**
     * @return the DisabilityPremiumAwarded
     */
    public int getDisabilityPremiumAwarded() {
        return DisabilityPremiumAwarded;
    }

    /**
     * @param DisabilityPremiumAwarded the DisabilityPremiumAwarded to set
     */
    public void setDisabilityPremiumAwarded(int DisabilityPremiumAwarded) {
        this.DisabilityPremiumAwarded = DisabilityPremiumAwarded;
    }

    public void setDisabilityPremiumAwarded(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            DisabilityPremiumAwarded = 0;
        } else {
            DisabilityPremiumAwarded = Integer.valueOf(fields[n]);
            if (DisabilityPremiumAwarded > 1 || DisabilityPremiumAwarded < 0) {
                System.err.println("DisabilityPremiumAwarded " + DisabilityPremiumAwarded);
                System.err.println("DisabilityPremiumAwarded > 1 || DisabilityPremiumAwarded < 0");
                throw new Exception("DisabilityPremiumAwarded > 1 || DisabilityPremiumAwarded < 0");
            }
        }
    }

    /**
     * @return the SevereDisabilityPremiumAwarded
     */
    public int getSevereDisabilityPremiumAwarded() {
        return SevereDisabilityPremiumAwarded;
    }

    /**
     * @param SevereDisabilityPremiumAwarded the SevereDisabilityPremiumAwarded
     * to set
     */
    public void setSevereDisabilityPremiumAwarded(int SevereDisabilityPremiumAwarded) {
        this.SevereDisabilityPremiumAwarded = SevereDisabilityPremiumAwarded;
    }

    public void setSevereDisabilityPremiumAwarded(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            SevereDisabilityPremiumAwarded = 0;
        } else {
            SevereDisabilityPremiumAwarded = Integer.valueOf(fields[n]);
            if (SevereDisabilityPremiumAwarded > 1 || SevereDisabilityPremiumAwarded < 0) {
                System.err.println("SevereDisabilityPremiumAwarded " + SevereDisabilityPremiumAwarded);
                System.err.println("SevereDisabilityPremiumAwarded > 1 || SevereDisabilityPremiumAwarded < 0");
                throw new Exception("SevereDisabilityPremiumAwarded > 1 || SevereDisabilityPremiumAwarded < 0");
            }
        }
    }

    /**
     * @return the DisabledChildPremiumAwarded
     */
    public int getDisabledChildPremiumAwarded() {
        return DisabledChildPremiumAwarded;
    }

    /**
     * @param DisabledChildPremiumAwarded the DisabledChildPremiumAwarded to set
     */
    public void setDisabledChildPremiumAwarded(int DisabledChildPremiumAwarded) {
        this.DisabledChildPremiumAwarded = DisabledChildPremiumAwarded;
    }

    public void setDisabledChildPremiumAwarded(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            DisabledChildPremiumAwarded = 0;
        } else {
            DisabledChildPremiumAwarded = Integer.valueOf(fields[n]);
            if (DisabledChildPremiumAwarded > 1 || DisabledChildPremiumAwarded < 0) {
                System.err.println("DisabledChildPremiumAwarded " + DisabledChildPremiumAwarded);
                System.err.println("DisabledChildPremiumAwarded > 1 || DisabiledChildPremiumAwarded < 0");
                throw new Exception("DisabledChildPremiumAwarded > 1 || DisabiledChildPremiumAwarded < 0");
            }
        }
    }

    /**
     * @return the CarePremiumAwarded
     */
    public int getCarePremiumAwarded() {
        return CarePremiumAwarded;
    }

    /**
     * @param CarePremiumAwarded the CarePremiumAwarded to set
     */
    public void setCarePremiumAwarded(int CarePremiumAwarded) {
        this.CarePremiumAwarded = CarePremiumAwarded;
    }

    public void setCarePremiumAwarded(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            CarePremiumAwarded = 0;
        } else {
            CarePremiumAwarded = Integer.valueOf(fields[n]);
            if (CarePremiumAwarded > 1 || CarePremiumAwarded < 0) {
                System.err.println("CarePremiumAwarded " + CarePremiumAwarded);
                System.err.println("CarePremiumAwarded > 1 || CarePremiumAwarded < 0");
                throw new Exception("CarePremiumAwarded > 1 || CarePremiumAwarded < 0");
            }
        }
    }

    /**
     * @return the EnhancedDisabilityPremiumAwarded
     */
    public int getEnhancedDisabilityPremiumAwarded() {
        return EnhancedDisabilityPremiumAwarded;
    }

    /**
     * @param EnhancedDisabilityPremiumAwarded the
     * EnhancedDisabilityPremiumAwarded to set
     */
    public void setEnhancedDisabilityPremiumAwarded(int EnhancedDisabilityPremiumAwarded) {
        this.EnhancedDisabilityPremiumAwarded = EnhancedDisabilityPremiumAwarded;
    }

    public void setEnhancedDisabilityPremiumAwarded(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            EnhancedDisabilityPremiumAwarded = 0;
        } else {
            EnhancedDisabilityPremiumAwarded = Integer.valueOf(fields[n]);
            if (EnhancedDisabilityPremiumAwarded > 1 || EnhancedDisabilityPremiumAwarded < 0) {
                System.err.println("EnhancedDisabilityPremiumAwarded " + EnhancedDisabilityPremiumAwarded);
                System.err.println("EnhancedDisabilityPremiumAwarded > 1 || EnhancedDisabilityPremiumAwarded < 0");
                throw new Exception("EnhancedDisabilityPremiumAwarded > 1 || EnhancedDisabilityPremiumAwarded < 0");
            }
        }
    }

    /**
     * @return the BereavementPremiumAwarded
     */
    public int getBereavementPremiumAwarded() {
        return BereavementPremiumAwarded;
    }

    /**
     * @param BereavementPremiumAwarded the BereavementPremiumAwarded to set
     */
    public void setBereavementPremiumAwarded(int BereavementPremiumAwarded) {
        this.BereavementPremiumAwarded = BereavementPremiumAwarded;
    }

    public void setBereavementPremiumAwarded(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            BereavementPremiumAwarded = 0;
        } else {
            BereavementPremiumAwarded = Integer.valueOf(fields[n]);
            if (BereavementPremiumAwarded > 1 || BereavementPremiumAwarded < 0) {
                System.err.println("BereavementPremiumAwarded " + BereavementPremiumAwarded);
                System.err.println("BereavementPremiumAwarded > 1 || BereavementPremiumAwarded < 0");
                throw new Exception("BereavementPremiumAwarded > 1 || BereavementPremiumAwarded < 0");
            }
        }
    }

    /**
     * @return the PartnerFlag
     */
    public int getPartnerFlag() {
        return PartnerFlag;
    }

    /**
     * @param PartnerFlag the PartnerFlag to set
     */
    public void setPartnerFlag(int PartnerFlag) {
        this.PartnerFlag = PartnerFlag;
    }

    public void setPartnerFlag(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            PartnerFlag = 0;
        } else {
            PartnerFlag = Integer.valueOf(fields[n]);
            if (PartnerFlag > 2 || PartnerFlag < 0) {
                System.err.println("PartnerFlag " + PartnerFlag);
                System.err.println("PartnerFlag > 2 || PartnerFlag < 0");
                throw new Exception("PartnerFlag > 2 || PartnerFlag < 0");
            }
        }
    }

    /**
     * @return the PartnersStartDate
     */
    public String getPartnersStartDate() {
        return PartnersStartDate;
    }

    /**
     * @param PartnersStartDate the PartnersStartDate to set
     */
    public void setPartnersStartDate(String PartnersStartDate) {
        this.PartnersStartDate = PartnersStartDate;
    }

    /**
     * @return the PartnersEndDate
     */
    public String getPartnersEndDate() {
        return PartnersEndDate;
    }

    /**
     * @param PartnersEndDate the PartnersEndDate to set
     */
    public void setPartnersEndDate(String PartnersEndDate) {
        this.PartnersEndDate = PartnersEndDate;
    }

    /**
     * @return the PartnersNationalInsuranceNumber
     */
    public String getPartnersNationalInsuranceNumber() {
        return PartnersNationalInsuranceNumber;
    }

    /**
     * @param PartnersNationalInsuranceNumber the
     * PartnersNationalInsuranceNumber to set
     */
    public void setPartnersNationalInsuranceNumber(String PartnersNationalInsuranceNumber) {
        this.PartnersNationalInsuranceNumber = PartnersNationalInsuranceNumber;
    }

    /**
     * @return the PartnersStudentIndicator
     */
    public String getPartnersStudentIndicator() {
        return PartnersStudentIndicator;
    }

    /**
     * @param PartnersStudentIndicator the PartnersStudentIndicator to set
     */
    public void setPartnersStudentIndicator(String PartnersStudentIndicator) {
        this.PartnersStudentIndicator = PartnersStudentIndicator;
    }

    /**
     * @return the PartnersAssessedIncomeFigure
     */
    public int getPartnersAssessedIncomeFigure() {
        return PartnersAssessedIncomeFigure;
    }

    /**
     * @param PartnersAssessedIncomeFigure the PartnersAssessedIncomeFigure to
     * set
     */
    public void setPartnersAssessedIncomeFigure(int PartnersAssessedIncomeFigure) {
        this.PartnersAssessedIncomeFigure = PartnersAssessedIncomeFigure;
    }

    /**
     * @return the PartnersAdjustedAssessedIncomeFigure
     */
    public int getPartnersAdjustedAssessedIncomeFigure() {
        return PartnersAdjustedAssessedIncomeFigure;
    }

    /**
     * @param PartnersAdjustedAssessedIncomeFigure the
     * PartnersAdjustedAssessedIncomeFigure to set
     */
    public void setPartnersAdjustedAssessedIncomeFigure(int PartnersAdjustedAssessedIncomeFigure) {
        this.PartnersAdjustedAssessedIncomeFigure = PartnersAdjustedAssessedIncomeFigure;
    }

    /**
     * @return the PartnersGrossWeeklyIncomeFromEmployment
     */
    public int getPartnersGrossWeeklyIncomeFromEmployment() {
        return PartnersGrossWeeklyIncomeFromEmployment;
    }

    /**
     * @param PartnersGrossWeeklyIncomeFromEmployment the
     * PartnersGrossWeeklyIncomeFromEmployment to set
     */
    public void setPartnersGrossWeeklyIncomeFromEmployment(int PartnersGrossWeeklyIncomeFromEmployment) {
        this.PartnersGrossWeeklyIncomeFromEmployment = PartnersGrossWeeklyIncomeFromEmployment;
    }

    /**
     * @return the PartnersNetWeeklyIncomeFromEmployment
     */
    public int getPartnersNetWeeklyIncomeFromEmployment() {
        return PartnersNetWeeklyIncomeFromEmployment;
    }

    /**
     * @param PartnersNetWeeklyIncomeFromEmployment the
     * PartnersNetWeeklyIncomeFromEmployment to set
     */
    public void setPartnersNetWeeklyIncomeFromEmployment(int PartnersNetWeeklyIncomeFromEmployment) {
        this.PartnersNetWeeklyIncomeFromEmployment = PartnersNetWeeklyIncomeFromEmployment;
    }

    /**
     * @return the PartnersGrossWeeklyIncomeFromSelfEmployment
     */
    public int getPartnersGrossWeeklyIncomeFromSelfEmployment() {
        return PartnersGrossWeeklyIncomeFromSelfEmployment;
    }

    /**
     * @param PartnersGrossWeeklyIncomeFromSelfEmployment the
     * PartnersGrossWeeklyIncomeFromSelfEmployment to set
     */
    public void setPartnersGrossWeeklyIncomeFromSelfEmployment(int PartnersGrossWeeklyIncomeFromSelfEmployment) {
        this.PartnersGrossWeeklyIncomeFromSelfEmployment = PartnersGrossWeeklyIncomeFromSelfEmployment;
    }

    /**
     * @return the PartnersNetWeeklyIncomeFromSelfEmployment
     */
    public int getPartnersNetWeeklyIncomeFromSelfEmployment() {
        return PartnersNetWeeklyIncomeFromSelfEmployment;
    }

    /**
     * @param PartnersNetWeeklyIncomeFromSelfEmployment the
     * PartnersNetWeeklyIncomeFromSelfEmployment to set
     */
    public void setPartnersNetWeeklyIncomeFromSelfEmployment(int PartnersNetWeeklyIncomeFromSelfEmployment) {
        this.PartnersNetWeeklyIncomeFromSelfEmployment = PartnersNetWeeklyIncomeFromSelfEmployment;
    }

    /**
     * @return the PartnersTotalAmountOfEarningsDisregarded
     */
    public int getPartnersTotalAmountOfEarningsDisregarded() {
        return PartnersTotalAmountOfEarningsDisregarded;
    }

    /**
     * @param PartnersTotalAmountOfEarningsDisregarded the
     * PartnersTotalAmountOfEarningsDisregarded to set
     */
    public void setPartnersTotalAmountOfEarningsDisregarded(int PartnersTotalAmountOfEarningsDisregarded) {
        this.PartnersTotalAmountOfEarningsDisregarded = PartnersTotalAmountOfEarningsDisregarded;
    }

    /**
     * @return the
     * PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded
     */
    public int getPartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded() {
        return PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded;
    }

    /**
     * @param PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded
     * the PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded to
     * set
     */
    public void setPartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded(int PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded) {
        this.PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded = PartnersIfChildcareDisregardAllowedWeeklyAmountBeingDisregarded;
    }

    /**
     * @return the PartnersIncomeFromAttendanceAllowance
     */
    public int getPartnersIncomeFromAttendanceAllowance() {
        return PartnersIncomeFromAttendanceAllowance;
    }

    /**
     * @param PartnersIncomeFromAttendanceAllowance the
     * PartnersIncomeFromAttendanceAllowance to set
     */
    public void setPartnersIncomeFromAttendanceAllowance(int PartnersIncomeFromAttendanceAllowance) {
        this.PartnersIncomeFromAttendanceAllowance = PartnersIncomeFromAttendanceAllowance;
    }

    /**
     * @return the PartnersIncomeFromBusinessStartUpAllowance
     */
    public int getPartnersIncomeFromBusinessStartUpAllowance() {
        return PartnersIncomeFromBusinessStartUpAllowance;
    }

    /**
     * @param PartnersIncomeFromBusinessStartUpAllowance the
     * PartnersIncomeFromBusinessStartUpAllowance to set
     */
    public void setPartnersIncomeFromBusinessStartUpAllowance(int PartnersIncomeFromBusinessStartUpAllowance) {
        this.PartnersIncomeFromBusinessStartUpAllowance = PartnersIncomeFromBusinessStartUpAllowance;
    }

    /**
     * @return the PartnersIncomeFromChildBenefit
     */
    public int getPartnersIncomeFromChildBenefit() {
        return PartnersIncomeFromChildBenefit;
    }

    /**
     * @param PartnersIncomeFromChildBenefit the PartnersIncomeFromChildBenefit
     * to set
     */
    public void setPartnersIncomeFromChildBenefit(int PartnersIncomeFromChildBenefit) {
        this.PartnersIncomeFromChildBenefit = PartnersIncomeFromChildBenefit;
    }

    /**
     * @return the PartnersIncomeFromPersonalPension
     */
    public int getPartnersIncomeFromPersonalPension() {
        return PartnersIncomeFromPersonalPension;
    }

    /**
     * @param PartnersIncomeFromPersonalPension the
     * PartnersIncomeFromPersonalPension to set
     */
    public void setPartnersIncomeFromPersonalPension(int PartnersIncomeFromPersonalPension) {
        this.PartnersIncomeFromPersonalPension = PartnersIncomeFromPersonalPension;
    }

    /**
     * @return the PartnersIncomeFromSevereDisabilityAllowance
     */
    public int getPartnersIncomeFromSevereDisabilityAllowance() {
        return PartnersIncomeFromSevereDisabilityAllowance;
    }

    /**
     * @param PartnersIncomeFromSevereDisabilityAllowance the
     * PartnersIncomeFromSevereDisabilityAllowance to set
     */
    public void setPartnersIncomeFromSevereDisabilityAllowance(int PartnersIncomeFromSevereDisabilityAllowance) {
        this.PartnersIncomeFromSevereDisabilityAllowance = PartnersIncomeFromSevereDisabilityAllowance;
    }

    /**
     * @return the PartnersIncomeFromMaternityAllowance
     */
    public int getPartnersIncomeFromMaternityAllowance() {
        return PartnersIncomeFromMaternityAllowance;
    }

    /**
     * @param PartnersIncomeFromMaternityAllowance the
     * PartnersIncomeFromMaternityAllowance to set
     */
    public void setPartnersIncomeFromMaternityAllowance(int PartnersIncomeFromMaternityAllowance) {
        this.PartnersIncomeFromMaternityAllowance = PartnersIncomeFromMaternityAllowance;
    }

    /**
     * @return the PartnersIncomeFromContributionBasedJobSeekersAllowance
     */
    public int getPartnersIncomeFromContributionBasedJobSeekersAllowance() {
        return PartnersIncomeFromContributionBasedJobSeekersAllowance;
    }

    /**
     * @param PartnersIncomeFromContributionBasedJobSeekersAllowance the
     * PartnersIncomeFromContributionBasedJobSeekersAllowance to set
     */
    public void setPartnersIncomeFromContributionBasedJobSeekersAllowance(int PartnersIncomeFromContributionBasedJobSeekersAllowance) {
        this.PartnersIncomeFromContributionBasedJobSeekersAllowance = PartnersIncomeFromContributionBasedJobSeekersAllowance;
    }

    /**
     * @return the PartnersIncomeFromStudentGrantLoan
     */
    public int getPartnersIncomeFromStudentGrantLoan() {
        return PartnersIncomeFromStudentGrantLoan;
    }

    /**
     * @param PartnersIncomeFromStudentGrantLoan the
     * PartnersIncomeFromStudentGrantLoan to set
     */
    public void setPartnersIncomeFromStudentGrantLoan(int PartnersIncomeFromStudentGrantLoan) {
        this.PartnersIncomeFromStudentGrantLoan = PartnersIncomeFromStudentGrantLoan;
    }

    /**
     * @return the PartnersIncomeFromSubTenants
     */
    public int getPartnersIncomeFromSubTenants() {
        return PartnersIncomeFromSubTenants;
    }

    /**
     * @param PartnersIncomeFromSubTenants the PartnersIncomeFromSubTenants to
     * set
     */
    public void setPartnersIncomeFromSubTenants(int PartnersIncomeFromSubTenants) {
        this.PartnersIncomeFromSubTenants = PartnersIncomeFromSubTenants;
    }

    /**
     * @return the PartnersIncomeFromBoarders
     */
    public int getPartnersIncomeFromBoarders() {
        return PartnersIncomeFromBoarders;
    }

    /**
     * @param PartnersIncomeFromBoarders the PartnersIncomeFromBoarders to set
     */
    public void setPartnersIncomeFromBoarders(int PartnersIncomeFromBoarders) {
        this.PartnersIncomeFromBoarders = PartnersIncomeFromBoarders;
    }

    /**
     * @return the PartnersIncomeFromTrainingForWorkCommunityAction
     */
    public int getPartnersIncomeFromTrainingForWorkCommunityAction() {
        return PartnersIncomeFromTrainingForWorkCommunityAction;
    }

    /**
     * @param PartnersIncomeFromTrainingForWorkCommunityAction the
     * PartnersIncomeFromTrainingForWorkCommunityAction to set
     */
    public void setPartnersIncomeFromTrainingForWorkCommunityAction(int PartnersIncomeFromTrainingForWorkCommunityAction) {
        this.PartnersIncomeFromTrainingForWorkCommunityAction = PartnersIncomeFromTrainingForWorkCommunityAction;
    }

    /**
     * @return the PartnersIncomeFromIncapacityBenefitShortTermLower
     */
    public int getPartnersIncomeFromIncapacityBenefitShortTermLower() {
        return PartnersIncomeFromIncapacityBenefitShortTermLower;
    }

    /**
     * @param PartnersIncomeFromIncapacityBenefitShortTermLower the
     * PartnersIncomeFromIncapacityBenefitShortTermLower to set
     */
    public void setPartnersIncomeFromIncapacityBenefitShortTermLower(int PartnersIncomeFromIncapacityBenefitShortTermLower) {
        this.PartnersIncomeFromIncapacityBenefitShortTermLower = PartnersIncomeFromIncapacityBenefitShortTermLower;
    }

    /**
     * @return the PartnersIncomeFromIncapacityBenefitShortTermHigher
     */
    public int getPartnersIncomeFromIncapacityBenefitShortTermHigher() {
        return PartnersIncomeFromIncapacityBenefitShortTermHigher;
    }

    /**
     * @param PartnersIncomeFromIncapacityBenefitShortTermHigher the
     * PartnersIncomeFromIncapacityBenefitShortTermHigher to set
     */
    public void setPartnersIncomeFromIncapacityBenefitShortTermHigher(int PartnersIncomeFromIncapacityBenefitShortTermHigher) {
        this.PartnersIncomeFromIncapacityBenefitShortTermHigher = PartnersIncomeFromIncapacityBenefitShortTermHigher;
    }

    /**
     * @return the PartnersIncomeFromIncapacityBenefitLongTerm
     */
    public int getPartnersIncomeFromIncapacityBenefitLongTerm() {
        return PartnersIncomeFromIncapacityBenefitLongTerm;
    }

    /**
     * @param PartnersIncomeFromIncapacityBenefitLongTerm the
     * PartnersIncomeFromIncapacityBenefitLongTerm to set
     */
    public void setPartnersIncomeFromIncapacityBenefitLongTerm(int PartnersIncomeFromIncapacityBenefitLongTerm) {
        this.PartnersIncomeFromIncapacityBenefitLongTerm = PartnersIncomeFromIncapacityBenefitLongTerm;
    }

    /**
     * @return the PartnersIncomeFromNewDeal50PlusEmploymentCredit
     */
    public int getPartnersIncomeFromNewDeal50PlusEmploymentCredit() {
        return PartnersIncomeFromNewDeal50PlusEmploymentCredit;
    }

    /**
     * @param PartnersIncomeFromNewDeal50PlusEmploymentCredit the
     * PartnersIncomeFromNewDeal50PlusEmploymentCredit to set
     */
    public void setPartnersIncomeFromNewDeal50PlusEmploymentCredit(int PartnersIncomeFromNewDeal50PlusEmploymentCredit) {
        this.PartnersIncomeFromNewDeal50PlusEmploymentCredit = PartnersIncomeFromNewDeal50PlusEmploymentCredit;
    }

    /**
     * @return the PartnersIncomeFromNewTaxCredits
     */
    public int getPartnersIncomeFromNewTaxCredits() {
        return PartnersIncomeFromNewTaxCredits;
    }

    /**
     * @param PartnersIncomeFromNewTaxCredits the
     * PartnersIncomeFromNewTaxCredits to set
     */
    public void setPartnersIncomeFromNewTaxCredits(int PartnersIncomeFromNewTaxCredits) {
        this.PartnersIncomeFromNewTaxCredits = PartnersIncomeFromNewTaxCredits;
    }

    /**
     * @return the PartnersIncomeFromDisabilityLivingAllowanceCareComponent
     */
    public int getPartnersIncomeFromDisabilityLivingAllowanceCareComponent() {
        return PartnersIncomeFromDisabilityLivingAllowanceCareComponent;
    }

    /**
     * @param PartnersIncomeFromDisabilityLivingAllowanceCareComponent the
     * PartnersIncomeFromDisabilityLivingAllowanceCareComponent to set
     */
    public void setPartnersIncomeFromDisabilityLivingAllowanceCareComponent(int PartnersIncomeFromDisabilityLivingAllowanceCareComponent) {
        this.PartnersIncomeFromDisabilityLivingAllowanceCareComponent = PartnersIncomeFromDisabilityLivingAllowanceCareComponent;
    }

    /**
     * @return the PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent
     */
    public int getPartnersIncomeFromDisabilityLivingAllowanceMobilityComponent() {
        return PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent;
    }

    /**
     * @param PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent the
     * PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent to set
     */
    public void setPartnersIncomeFromDisabilityLivingAllowanceMobilityComponent(int PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent) {
        this.PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent = PartnersIncomeFromDisabilityLivingAllowanceMobilityComponent;
    }

    /**
     * @return the PartnersIncomeFromGovernemntTraining
     */
    public int getPartnersIncomeFromGovernemntTraining() {
        return PartnersIncomeFromGovernemntTraining;
    }

    /**
     * @param PartnersIncomeFromGovernemntTraining the
     * PartnersIncomeFromGovernemntTraining to set
     */
    public void setPartnersIncomeFromGovernemntTraining(int PartnersIncomeFromGovernemntTraining) {
        this.PartnersIncomeFromGovernemntTraining = PartnersIncomeFromGovernemntTraining;
    }

    /**
     * @return the PartnersIncomeFromIndustrialInjuriesDisablementBenefit
     */
    public int getPartnersIncomeFromIndustrialInjuriesDisablementBenefit() {
        return PartnersIncomeFromIndustrialInjuriesDisablementBenefit;
    }

    /**
     * @param PartnersIncomeFromIndustrialInjuriesDisablementBenefit the
     * PartnersIncomeFromIndustrialInjuriesDisablementBenefit to set
     */
    public void setPartnersIncomeFromIndustrialInjuriesDisablementBenefit(int PartnersIncomeFromIndustrialInjuriesDisablementBenefit) {
        this.PartnersIncomeFromIndustrialInjuriesDisablementBenefit = PartnersIncomeFromIndustrialInjuriesDisablementBenefit;
    }

    /**
     * @return the PartnersIncomeFromCarersAllowance
     */
    public int getPartnersIncomeFromCarersAllowance() {
        return PartnersIncomeFromCarersAllowance;
    }

    /**
     * @param PartnersIncomeFromCarersAllowance the
     * PartnersIncomeFromCarersAllowance to set
     */
    public void setPartnersIncomeFromCarersAllowance(int PartnersIncomeFromCarersAllowance) {
        this.PartnersIncomeFromCarersAllowance = PartnersIncomeFromCarersAllowance;
    }

    /**
     * @return the PartnersIncomeFromStatuatorySickPay
     */
    public int getPartnersIncomeFromStatuatorySickPay() {
        return PartnersIncomeFromStatuatorySickPay;
    }

    /**
     * @param PartnersIncomeFromStatuatorySickPay the
     * PartnersIncomeFromStatuatorySickPay to set
     */
    public void setPartnersIncomeFromStatuatorySickPay(int PartnersIncomeFromStatuatorySickPay) {
        this.PartnersIncomeFromStatuatorySickPay = PartnersIncomeFromStatuatorySickPay;
    }

    /**
     * @return the PartnersIncomeFromStatutoryMaternityPaternityPay
     */
    public int getPartnersIncomeFromStatutoryMaternityPaternityPay() {
        return PartnersIncomeFromStatutoryMaternityPaternityPay;
    }

    /**
     * @param PartnersIncomeFromStatutoryMaternityPaternityPay the
     * PartnersIncomeFromStatutoryMaternityPaternityPay to set
     */
    public void setPartnersIncomeFromStatutoryMaternityPaternityPay(int PartnersIncomeFromStatutoryMaternityPaternityPay) {
        this.PartnersIncomeFromStatutoryMaternityPaternityPay = PartnersIncomeFromStatutoryMaternityPaternityPay;
    }

    /**
     * @return the
     * PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc
     */
    public int getPartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc() {
        return PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc;
    }

    /**
     * @param
     * PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc
     * the
     * PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc
     * to set
     */
    public void setPartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc(int PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc) {
        this.PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc = PartnersIncomeFromStateRetirementPensionIncludingSERPsGraduatedPensionetc;
    }

    /**
     * @return the PartnersIncomeFromWarDisablementPensionArmedForcesGIP
     */
    public int getPartnersIncomeFromWarDisablementPensionArmedForcesGIP() {
        return PartnersIncomeFromWarDisablementPensionArmedForcesGIP;
    }

    /**
     * @param PartnersIncomeFromWarDisablementPensionArmedForcesGIP the
     * PartnersIncomeFromWarDisablementPensionArmedForcesGIP to set
     */
    public void setPartnersIncomeFromWarDisablementPensionArmedForcesGIP(int PartnersIncomeFromWarDisablementPensionArmedForcesGIP) {
        this.PartnersIncomeFromWarDisablementPensionArmedForcesGIP = PartnersIncomeFromWarDisablementPensionArmedForcesGIP;
    }

    /**
     * @return the PartnersIncomeFromWarMobilitySupplement
     */
    public int getPartnersIncomeFromWarMobilitySupplement() {
        return PartnersIncomeFromWarMobilitySupplement;
    }

    /**
     * @param PartnersIncomeFromWarMobilitySupplement the
     * PartnersIncomeFromWarMobilitySupplement to set
     */
    public void setPartnersIncomeFromWarMobilitySupplement(int PartnersIncomeFromWarMobilitySupplement) {
        this.PartnersIncomeFromWarMobilitySupplement = PartnersIncomeFromWarMobilitySupplement;
    }

    /**
     * @return the PartnersIncomeFromWidowsWidowersPension
     */
    public int getPartnersIncomeFromWidowsWidowersPension() {
        return PartnersIncomeFromWidowsWidowersPension;
    }

    /**
     * @param PartnersIncomeFromWidowsWidowersPension the
     * PartnersIncomeFromWidowsWidowersPension to set
     */
    public void setPartnersIncomeFromWidowsWidowersPension(int PartnersIncomeFromWidowsWidowersPension) {
        this.PartnersIncomeFromWidowsWidowersPension = PartnersIncomeFromWidowsWidowersPension;
    }

    /**
     * @return the PartnersIncomeFromBereavementAllowance
     */
    public int getPartnersIncomeFromBereavementAllowance() {
        return PartnersIncomeFromBereavementAllowance;
    }

    /**
     * @param PartnersIncomeFromBereavementAllowance the
     * PartnersIncomeFromBereavementAllowance to set
     */
    public void setPartnersIncomeFromBereavementAllowance(int PartnersIncomeFromBereavementAllowance) {
        this.PartnersIncomeFromBereavementAllowance = PartnersIncomeFromBereavementAllowance;
    }

    /**
     * @return the PartnersIncomeFromWidowedParentsAllowance
     */
    public int getPartnersIncomeFromWidowedParentsAllowance() {
        return PartnersIncomeFromWidowedParentsAllowance;
    }

    /**
     * @param PartnersIncomeFromWidowedParentsAllowance the
     * PartnersIncomeFromWidowedParentsAllowance to set
     */
    public void setPartnersIncomeFromWidowedParentsAllowance(int PartnersIncomeFromWidowedParentsAllowance) {
        this.PartnersIncomeFromWidowedParentsAllowance = PartnersIncomeFromWidowedParentsAllowance;
    }

    /**
     * @return the PartnersIncomeFromYouthTrainingScheme
     */
    public int getPartnersIncomeFromYouthTrainingScheme() {
        return PartnersIncomeFromYouthTrainingScheme;
    }

    /**
     * @param PartnersIncomeFromYouthTrainingScheme the
     * PartnersIncomeFromYouthTrainingScheme to set
     */
    public void setPartnersIncomeFromYouthTrainingScheme(int PartnersIncomeFromYouthTrainingScheme) {
        this.PartnersIncomeFromYouthTrainingScheme = PartnersIncomeFromYouthTrainingScheme;
    }

    /**
     * @return the PartnersOtherIncome
     */
    public int getPartnersOtherIncome() {
        return PartnersOtherIncome;
    }

    /**
     * @param PartnersOtherIncome the PartnersOtherIncome to set
     */
    public void setPartnersOtherIncome(int PartnersOtherIncome) {
        this.PartnersOtherIncome = PartnersOtherIncome;
    }

    /**
     * @return the PartnersTotalAmountOfIncomeDisregarded
     */
    public int getPartnersTotalAmountOfIncomeDisregarded() {
        return PartnersTotalAmountOfIncomeDisregarded;
    }

    /**
     * @param PartnersTotalAmountOfIncomeDisregarded the
     * PartnersTotalAmountOfIncomeDisregarded to set
     */
    public void setPartnersTotalAmountOfIncomeDisregarded(int PartnersTotalAmountOfIncomeDisregarded) {
        this.PartnersTotalAmountOfIncomeDisregarded = PartnersTotalAmountOfIncomeDisregarded;
    }

    /**
     * @return the DateOverPaymentDetectionActivityInitiatedOnCase
     */
    public String getDateOverPaymentDetectionActivityInitiatedOnCase() {
        return DateOverPaymentDetectionActivityInitiatedOnCase;
    }

    /**
     * @param DateOverPaymentDetectionActivityInitiatedOnCase the
     * DateOverPaymentDetectionActivityInitiatedOnCase to set
     */
    public void setDateOverPaymentDetectionActivityInitiatedOnCase(String DateOverPaymentDetectionActivityInitiatedOnCase) {
        this.DateOverPaymentDetectionActivityInitiatedOnCase = DateOverPaymentDetectionActivityInitiatedOnCase;
    }

    /**
     * @return the ReasonForOverpaymentDetectionActivity
     */
    public int getReasonForOverpaymentDetectionActivity() {
        return ReasonForOverpaymentDetectionActivity;
    }

    /**
     * @param ReasonForOverpaymentDetectionActivity the
     * ReasonForOverpaymentDetectionActivity to set
     */
    public void setReasonForOverpaymentDetectionActivity(int ReasonForOverpaymentDetectionActivity) {
        this.ReasonForOverpaymentDetectionActivity = ReasonForOverpaymentDetectionActivity;
    }

    public void setReasonForOverpaymentDetectionActivity(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            ReasonForOverpaymentDetectionActivity = 0;
        } else {
            ReasonForOverpaymentDetectionActivity = Integer.valueOf(fields[n]);
            if (ReasonForOverpaymentDetectionActivity > 99 || ReasonForOverpaymentDetectionActivity < 0) {
                System.err.println("ReasonForOverpaymentDetectionActivity " + ReasonForOverpaymentDetectionActivity);
                System.err.println("ReasonForOverpaymentDetectionActivity > 99 || ReasonForOverpaymentDetectionActivity < 0");
                throw new Exception("ReasonForOverpaymentDetectionActivity > 99 || ReasonForOverpaymentDetectionActivity < 0");
            }
        }
    }

    /**
     * @return the MethodOfOverpayentDetectionActivity
     */
    public int getMethodOfOverpayentDetectionActivity() {
        return MethodOfOverpayentDetectionActivity;
    }

    /**
     * @param MethodOfOverpayentDetectionActivity the
     * MethodOfOverpayentDetectionActivity to set
     */
    public void setMethodOfOverpayentDetectionActivity(int MethodOfOverpayentDetectionActivity) {
        this.MethodOfOverpayentDetectionActivity = MethodOfOverpayentDetectionActivity;
    }

    public void setMethodOfOverpayentDetectionActivity(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            MethodOfOverpayentDetectionActivity = 0;
        } else {
            MethodOfOverpayentDetectionActivity = Integer.valueOf(fields[n]);
            if (MethodOfOverpayentDetectionActivity > 99 || MethodOfOverpayentDetectionActivity < 0) {
                System.err.println("MethodOfOverpayentDetectionActivity " + MethodOfOverpayentDetectionActivity);
                System.err.println("MethodOfOverpayentDetectionActivity > 99 || MethodOfOverpayentDetectionActivity < 0");
                throw new Exception("MethodOfOverpayentDetectionActivity > 99 || MethodOfOverpayentDetectionActivity < 0");
            }
        }
    }

    /**
     * @return the DoesTheOverpaymentDetectionActivityConstituteAFullReview
     */
    public String getDoesTheOverpaymentDetectionActivityConstituteAFullReview() {
        return DoesTheOverpaymentDetectionActivityConstituteAFullReview;
    }

    /**
     * @param DoesTheOverpaymentDetectionActivityConstituteAFullReview the
     * DoesTheOverpaymentDetectionActivityConstituteAFullReview to set
     */
    public void setDoesTheOverpaymentDetectionActivityConstituteAFullReview(String DoesTheOverpaymentDetectionActivityConstituteAFullReview) {
        this.DoesTheOverpaymentDetectionActivityConstituteAFullReview = DoesTheOverpaymentDetectionActivityConstituteAFullReview;
    }

    /**
     * @return the DateOverPaymentDetectionActivityIsCompleted
     */
    public String getDateOverPaymentDetectionActivityIsCompleted() {
        return DateOverPaymentDetectionActivityIsCompleted;
    }

    /**
     * @param DateOverPaymentDetectionActivityIsCompleted the
     * DateOverPaymentDetectionActivityIsCompleted to set
     */
    public void setDateOverPaymentDetectionActivityIsCompleted(String DateOverPaymentDetectionActivityIsCompleted) {
        this.DateOverPaymentDetectionActivityIsCompleted = DateOverPaymentDetectionActivityIsCompleted;
    }

    /**
     * @return the OutcomeOfOverPaymentDetectionActivity
     */
    public int getOutcomeOfOverPaymentDetectionActivity() {
        return OutcomeOfOverPaymentDetectionActivity;
    }

    /**
     * @param OutcomeOfOverPaymentDetectionActivity the
     * OutcomeOfOverPaymentDetectionActivity to set
     */
    public void setOutcomeOfOverPaymentDetectionActivity(int OutcomeOfOverPaymentDetectionActivity) {
        this.OutcomeOfOverPaymentDetectionActivity = OutcomeOfOverPaymentDetectionActivity;
    }

    public void setOutcomeOfOverPaymentDetectionActivity(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            OutcomeOfOverPaymentDetectionActivity = 0;
        } else {
            OutcomeOfOverPaymentDetectionActivity = Integer.valueOf(fields[n]);
            if (OutcomeOfOverPaymentDetectionActivity > 99 || OutcomeOfOverPaymentDetectionActivity < 0) {
                System.err.println("OutcomeOfOverPaymentDetectionActivity " + OutcomeOfOverPaymentDetectionActivity);
                System.err.println("OutcomeOfOverPaymentDetectionActivity > 99 || OutcomeOfOverPaymentDetectionActivity < 0");
                throw new Exception("OutcomeOfOverPaymentDetectionActivity > 99 || OutcomeOfOverPaymentDetectionActivity < 0");
            }
        }
    }

    /**
     * @return the ClaimantsGender
     */
    public String getClaimantsGender() {
        return ClaimantsGender;
    }

    /**
     * @param ClaimantsGender the ClaimantsGender to set
     */
    public void setClaimantsGender(String ClaimantsGender) {
        this.ClaimantsGender = ClaimantsGender;
    }

    /**
     * @return the PartnersDateOfBirth
     */
    public String getPartnersDateOfBirth() {
        return PartnersDateOfBirth;
    }

    /**
     * @param PartnersDateOfBirth the PartnersDateOfBirth to set
     */
    public void setPartnersDateOfBirth(String PartnersDateOfBirth) {
        this.PartnersDateOfBirth = PartnersDateOfBirth;
    }

    /**
     * @return the RentAllowanceMethodOfPayment
     */
    public int getRentAllowanceMethodOfPayment() {
        return RentAllowanceMethodOfPayment;
    }

    /**
     * @param RentAllowanceMethodOfPayment the RentAllowanceMethodOfPayment to
     * set
     */
    public void setRentAllowanceMethodOfPayment(int RentAllowanceMethodOfPayment) {
        this.RentAllowanceMethodOfPayment = RentAllowanceMethodOfPayment;
    }

    public void setRentAllowanceMethodOfPayment(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            RentAllowanceMethodOfPayment = 0;
        } else {
            RentAllowanceMethodOfPayment = Integer.valueOf(fields[n]);
            if (RentAllowanceMethodOfPayment > 99 || RentAllowanceMethodOfPayment < 0) {
                System.err.println("RentAllowanceMethodOfPayment " + RentAllowanceMethodOfPayment);
                System.err.println("RentAllowanceMethodOfPayment > 99 || RentAllowanceMethodOfPayment < 0");
                throw new Exception("RentAllowanceMethodOfPayment > 99 || RentAllowanceMethodOfPayment < 0");
            }
        }
    }

    /**
     * @return the RentAllowancePaymentDestination
     */
    public int getRentAllowancePaymentDestination() {
        return RentAllowancePaymentDestination;
    }

    /**
     * @param RentAllowancePaymentDestination the
     * RentAllowancePaymentDestination to set
     */
    public void setRentAllowancePaymentDestination(int RentAllowancePaymentDestination) {
        this.RentAllowancePaymentDestination = RentAllowancePaymentDestination;
    }

    public void setRentAllowancePaymentDestination(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            RentAllowancePaymentDestination = 0;
        } else {
            RentAllowancePaymentDestination = Integer.valueOf(fields[n]);
            if (RentAllowancePaymentDestination > 99 || RentAllowancePaymentDestination < 0) {
                System.err.println("RentAllowancePaymentDestination " + RentAllowancePaymentDestination);
                System.err.println("RentAllowancePaymentDestination > 99 || RentAllowancePaymentDestination < 0");
                throw new Exception("RentAllowancePaymentDestination > 99 || RentAllowancePaymentDestination < 0");
            }
        }
    }

    /**
     * @return the ContractualRentAmount
     */
    public int getContractualRentAmount() {
        return ContractualRentAmount;
    }

    /**
     * @param ContractualRentAmount the ContractualRentAmount to set
     */
    public void setContractualRentAmount(int ContractualRentAmount) {
        this.ContractualRentAmount = ContractualRentAmount;
    }

    /**
     * @return the TimePeriodContractualRentFigureCovers
     */
    public int getTimePeriodContractualRentFigureCovers() {
        return TimePeriodContractualRentFigureCovers;
    }

    /**
     * @param TimePeriodContractualRentFigureCovers the
     * TimePeriodContractualRentFigureCovers to set
     */
    public void setTimePeriodContractualRentFigureCovers(int TimePeriodContractualRentFigureCovers) {
        this.TimePeriodContractualRentFigureCovers = TimePeriodContractualRentFigureCovers;
    }

    public void setTimePeriodContractualRentFigureCovers(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            TimePeriodContractualRentFigureCovers = 0;
        } else {
            TimePeriodContractualRentFigureCovers = Integer.valueOf(fields[n]);
//            if (TimePeriodContractualRentFigureCovers > 4 || TimePeriodContractualRentFigureCovers < 0) {
//                System.err.println("TimePeriodContractualRentFigureCovers " + TimePeriodContractualRentFigureCovers);
//                System.err.println("TimePeriodContractualRentFigureCovers > 4 || TimePeriodContractualRentFigureCovers < 0");
//                throw new Exception("TimePeriodContractualRentFigureCovers > 4 || TimePeriodContractualRentFigureCovers < 0");
//            }
        }
    }

    /**
     * @return the ClaimantsIncomeFromPensionCreditSavingsCredit
     */
    public int getClaimantsIncomeFromPensionCreditSavingsCredit() {
        return ClaimantsIncomeFromPensionCreditSavingsCredit;
    }

    /**
     * @param ClaimantsIncomeFromPensionCreditSavingsCredit the
     * ClaimantsIncomeFromPensionCreditSavingsCredit to set
     */
    public void setClaimantsIncomeFromPensionCreditSavingsCredit(int ClaimantsIncomeFromPensionCreditSavingsCredit) {
        this.ClaimantsIncomeFromPensionCreditSavingsCredit = ClaimantsIncomeFromPensionCreditSavingsCredit;
    }

    /**
     * @return the PartnersIncomeFromPensionCreditSavingsCredit
     */
    public int getPartnersIncomeFromPensionCreditSavingsCredit() {
        return PartnersIncomeFromPensionCreditSavingsCredit;
    }

    /**
     * @param PartnersIncomeFromPensionCreditSavingsCredit the
     * PartnersIncomeFromPensionCreditSavingsCredit to set
     */
    public void setPartnersIncomeFromPensionCreditSavingsCredit(int PartnersIncomeFromPensionCreditSavingsCredit) {
        this.PartnersIncomeFromPensionCreditSavingsCredit = PartnersIncomeFromPensionCreditSavingsCredit;
    }

    /**
     * @return the ClaimantsIncomeFromMaintenancePayments
     */
    public int getClaimantsIncomeFromMaintenancePayments() {
        return ClaimantsIncomeFromMaintenancePayments;
    }

    /**
     * @param ClaimantsIncomeFromMaintenancePayments the
     * ClaimantsIncomeFromMaintenancePayments to set
     */
    public void setClaimantsIncomeFromMaintenancePayments(int ClaimantsIncomeFromMaintenancePayments) {
        this.ClaimantsIncomeFromMaintenancePayments = ClaimantsIncomeFromMaintenancePayments;
    }

    /**
     * @return the PartnersIncomeFromMaintenancePayments
     */
    public int getPartnersIncomeFromMaintenancePayments() {
        return PartnersIncomeFromMaintenancePayments;
    }

    /**
     * @param PartnersIncomeFromMaintenancePayments the
     * PartnersIncomeFromMaintenancePayments to set
     */
    public void setPartnersIncomeFromMaintenancePayments(int PartnersIncomeFromMaintenancePayments) {
        this.PartnersIncomeFromMaintenancePayments = PartnersIncomeFromMaintenancePayments;
    }

    /**
     * @return the ClaimantsIncomeFromOccupationalPension
     */
    public int getClaimantsIncomeFromOccupationalPension() {
        return ClaimantsIncomeFromOccupationalPension;
    }

    /**
     * @param ClaimantsIncomeFromOccupationalPension the
     * ClaimantsIncomeFromOccupationalPension to set
     */
    public void setClaimantsIncomeFromOccupationalPension(int ClaimantsIncomeFromOccupationalPension) {
        this.ClaimantsIncomeFromOccupationalPension = ClaimantsIncomeFromOccupationalPension;
    }

    /**
     * @return the PartnersIncomeFromOccupationalPension
     */
    public int getPartnersIncomeFromOccupationalPension() {
        return PartnersIncomeFromOccupationalPension;
    }

    /**
     * @param PartnersIncomeFromOccupationalPension the
     * PartnersIncomeFromOccupationalPension to set
     */
    public void setPartnersIncomeFromOccupationalPension(int PartnersIncomeFromOccupationalPension) {
        this.PartnersIncomeFromOccupationalPension = PartnersIncomeFromOccupationalPension;
    }

    /**
     * @return the ClaimantsIncomeFromWidowsBenefit
     */
    public int getClaimantsIncomeFromWidowsBenefit() {
        return ClaimantsIncomeFromWidowsBenefit;
    }

    /**
     * @param ClaimantsIncomeFromWidowsBenefit the
     * ClaimantsIncomeFromWidowsBenefit to set
     */
    public void setClaimantsIncomeFromWidowsBenefit(int ClaimantsIncomeFromWidowsBenefit) {
        this.ClaimantsIncomeFromWidowsBenefit = ClaimantsIncomeFromWidowsBenefit;
    }

    /**
     * @return the PartnersIncomeFromWidowsBenefit
     */
    public int getPartnersIncomeFromWidowsBenefit() {
        return PartnersIncomeFromWidowsBenefit;
    }

    /**
     * @param PartnersIncomeFromWidowsBenefit the
     * PartnersIncomeFromWidowsBenefit to set
     */
    public void setPartnersIncomeFromWidowsBenefit(int PartnersIncomeFromWidowsBenefit) {
        this.PartnersIncomeFromWidowsBenefit = PartnersIncomeFromWidowsBenefit;
    }

    /**
     * @return the CTBClaimEntitlementStartDate
     */
    public String getCTBClaimEntitlementStartDate() {
        return CTBClaimEntitlementStartDate;
    }

    /**
     * @param CTBClaimEntitlementStartDate the CTBClaimEntitlementStartDate to
     * set
     */
    public void setCTBClaimEntitlementStartDate(String CTBClaimEntitlementStartDate) {
        this.CTBClaimEntitlementStartDate = CTBClaimEntitlementStartDate;
    }

    /**
     * @return the DateHBClaimClosedWithdrawnDecidedUnsuccessfulDefective
     */
    public String getDateHBClaimClosedWithdrawnDecidedUnsuccessfulDefective() {
        return DateHBClaimClosedWithdrawnDecidedUnsuccessfulDefective;
    }

    /**
     * @param DateHBClaimClosedWithdrawnDecidedUnsuccessfulDefective the
     * DateHBClaimClosedWithdrawnDecidedUnsuccessfulDefective to set
     */
    public void setDateHBClaimClosedWithdrawnDecidedUnsuccessfulDefective(String DateHBClaimClosedWithdrawnDecidedUnsuccessfulDefective) {
        this.DateHBClaimClosedWithdrawnDecidedUnsuccessfulDefective = DateHBClaimClosedWithdrawnDecidedUnsuccessfulDefective;
    }

    /**
     * @return the DateCTBClaimClosedWithdrawnDecidedUnsuccessfulDefective
     */
    public String getDateCTBClaimClosedWithdrawnDecidedUnsuccessfulDefective() {
        return DateCTBClaimClosedWithdrawnDecidedUnsuccessfulDefective;
    }

    /**
     * @param DateCTBClaimClosedWithdrawnDecidedUnsuccessfulDefective the
     * DateCTBClaimClosedWithdrawnDecidedUnsuccessfulDefective to set
     */
    public void setDateCTBClaimClosedWithdrawnDecidedUnsuccessfulDefective(String DateCTBClaimClosedWithdrawnDecidedUnsuccessfulDefective) {
        this.DateCTBClaimClosedWithdrawnDecidedUnsuccessfulDefective = DateCTBClaimClosedWithdrawnDecidedUnsuccessfulDefective;
    }

    /**
     * @return the TotalNumberOfRooms
     */
    public int getTotalNumberOfRooms() {
        return TotalNumberOfRooms;
    }

    /**
     * @param TotalNumberOfRooms the TotalNumberOfRooms to set
     */
    public void setTotalNumberOfRooms(int TotalNumberOfRooms) {
        this.TotalNumberOfRooms = TotalNumberOfRooms;
    }

    /**
     * @return the NonSelfContainedAccomodation
     */
    public int getNonSelfContainedAccomodation() {
        return NonSelfContainedAccomodation;
    }

    /**
     * @param NonSelfContainedAccomodation the NonSelfContainedAccomodation to
     * set
     */
    public void setNonSelfContainedAccomodation(int NonSelfContainedAccomodation) {
        this.NonSelfContainedAccomodation = NonSelfContainedAccomodation;
    }

    public void setNonSelfContainedAccomodation(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            NonSelfContainedAccomodation = 0;
        } else {
            NonSelfContainedAccomodation = Integer.valueOf(fields[n]);
            if (NonSelfContainedAccomodation > 1 || NonSelfContainedAccomodation < 0) {
                System.err.println("NonSelfContainedAccomodation " + NonSelfContainedAccomodation);
                System.err.println("NonSelfContainedAccomodation > 1 || NonSelfContainedAccomodation < 0");
                throw new Exception("NonSelfContainedAccomodation > 1 || NonSelfContainedAccomodation < 0");
            }
        }
    }

    /**
     * @return the TypeOfLHANumberOfRoomsEntitedTo
     */
    public int getTypeOfLHANumberOfRoomsEntitedTo() {
        return TypeOfLHANumberOfRoomsEntitedTo;
    }

    /**
     * @param TypeOfLHANumberOfRoomsEntitedTo the
     * TypeOfLHANumberOfRoomsEntitedTo to set
     */
    public void setTypeOfLHANumberOfRoomsEntitedTo(int TypeOfLHANumberOfRoomsEntitedTo) {
        this.TypeOfLHANumberOfRoomsEntitedTo = TypeOfLHANumberOfRoomsEntitedTo;
    }

    /**
     * @return the TransitionalProtectionFromNationalRolloutOfLHA
     */
    public int getTransitionalProtectionFromNationalRolloutOfLHA() {
        return TransitionalProtectionFromNationalRolloutOfLHA;
    }

    /**
     * @param TransitionalProtectionFromNationalRolloutOfLHA the
     * TransitionalProtectionFromNationalRolloutOfLHA to set
     */
    public void setTransitionalProtectionFromNationalRolloutOfLHA(int TransitionalProtectionFromNationalRolloutOfLHA) {
        this.TransitionalProtectionFromNationalRolloutOfLHA = TransitionalProtectionFromNationalRolloutOfLHA;
    }

    public void setTransitionalProtectionFromNationalRolloutOfLHA(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            TransitionalProtectionFromNationalRolloutOfLHA = 0;
        } else {
            TransitionalProtectionFromNationalRolloutOfLHA = Integer.valueOf(fields[n]);
            if (TransitionalProtectionFromNationalRolloutOfLHA > 1 || TransitionalProtectionFromNationalRolloutOfLHA < 0) {
                System.err.println("TransitionalProtectionFromNationalRolloutOfLHA " + TransitionalProtectionFromNationalRolloutOfLHA);
                System.err.println("TransitionalProtectionFromNationalRolloutOfLHA > 1 || TransitionalProtectionFromNationalRolloutOfLHA < 0");
                throw new Exception("TransitionalProtectionFromNationalRolloutOfLHA > 1 || TransitionalProtectionFromNationalRolloutOfLHA < 0");
            }
        }
    }

    /**
     * @return the Locality
     */
    public String getLocality() {
        return Locality;
    }

    /**
     * @param Locality the Locality to set
     */
    public void setLocality(String Locality) {
        this.Locality = Locality;
    }

    /**
     * @return the ValueOfLHA
     */
    public int getValueOfLHA() {
        return ValueOfLHA;
    }

    /**
     * @param ValueOfLHA the ValueOfLHA to set
     */
    public void setValueOfLHA(int ValueOfLHA) {
        this.ValueOfLHA = ValueOfLHA;
    }

    /**
     * @return the
     * ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective
     */
    public int getReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective() {
        return ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective;
    }

    /**
     * @param ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective
     * the ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective to
     * set
     */
    public void setReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective(int ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective) {
        this.ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective = ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective;
    }

    public void setReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective = 0;
        } else {
            ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective = Integer.valueOf(fields[n]);
            if (ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0) {
                System.err.println("ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective " + ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective);
                System.err.println("ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0");
                throw new Exception("ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatHBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0");
            }
        }
    }

    /**
     * @return the
     * ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective
     */
    public int getReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective() {
        return ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective;
    }

    /**
     * @param ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective
     * the ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective to
     * set
     */
    public void setReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective(int ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective) {
        this.ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective = ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective;
    }

    public void setReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective = 0;
        } else {
            ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective = Integer.valueOf(fields[n]);
            if (ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0) {
                System.err.println("ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective " + ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective);
                System.err.println("ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0");
                throw new Exception("ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective > 9 || ReasonsThatCTBClaimWasClosedWithdrawnDecidedUnsuccessfulDefective < 0");
            }
        }
    }

    /**
     * @return the PartnersGender
     */
    public String getPartnersGender() {
        return PartnersGender;
    }

    /**
     * @param PartnersGender the PartnersGender to set
     */
    public void setPartnersGender(String PartnersGender) {
        this.PartnersGender = PartnersGender;
    }

    /**
     * @return the NonDependantGrossWeeklyIncomeFromRemunerativeWork
     */
    public int getNonDependantGrossWeeklyIncomeFromRemunerativeWork() {
        return NonDependantGrossWeeklyIncomeFromRemunerativeWork;
    }

    /**
     * @param NonDependantGrossWeeklyIncomeFromRemunerativeWork the
     * NonDependantGrossWeeklyIncomeFromRemunerativeWork to set
     */
    public void setNonDependantGrossWeeklyIncomeFromRemunerativeWork(int NonDependantGrossWeeklyIncomeFromRemunerativeWork) {
        this.NonDependantGrossWeeklyIncomeFromRemunerativeWork = NonDependantGrossWeeklyIncomeFromRemunerativeWork;
    }

    /**
     * @return the HBClaimTreatAsDateMade
     */
    public String getHBClaimTreatAsDateMade() {
        return HBClaimTreatAsDateMade;
    }

    /**
     * @param HBClaimTreatAsDateMade the HBClaimTreatAsDateMade to set
     */
    public void setHBClaimTreatAsDateMade(String HBClaimTreatAsDateMade) {
        this.HBClaimTreatAsDateMade = HBClaimTreatAsDateMade;
    }

//    /**
//     * @return the SourceOfMostRecentHBClaim
//     */
//    public int getSourceOfMostRecentHBClaim() {
//        return SourceOfMostRecentHBClaim;
//    }
//
//    /**
//     * @param SourceOfMostRecentHBClaim the SourceOfMostRecentHBClaim to set
//     */
//    public void setSourceOfMostRecentHBClaim(int SourceOfMostRecentHBClaim) {
//        this.SourceOfMostRecentHBClaim = SourceOfMostRecentHBClaim;
//    }
    /**
     * @return the DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim
     */
    public int getDidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim() {
        return DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim;
    }

    /**
     * @param DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim the
     * DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim to set
     */
    public void setDidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim(int DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim) {
        this.DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim = DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim;
    }

    public void setDidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim = 0;
        } else {
            DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim = Integer.valueOf(fields[n]);
            if (DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim > 1 || DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim < 0) {
                System.err.println("DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim " + DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim);
                System.err.println("DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim > 1 || DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim < 0");
                throw new Exception("DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim > 1 || DidVerificationIdentifyAnyIncorrectInformationOnTheHBClaim < 0");
            }
        }
    }

    /**
     * @return the DateOfFirstHBPaymentRentAllowanceOnly
     */
    public String getDateOfFirstHBPaymentRentAllowanceOnly() {
        return DateOfFirstHBPaymentRentAllowanceOnly;
    }

    /**
     * @param DateOfFirstHBPaymentRentAllowanceOnly the
     * DateOfFirstHBPaymentRentAllowanceOnly to set
     */
    public void setDateOfFirstHBPaymentRentAllowanceOnly(String DateOfFirstHBPaymentRentAllowanceOnly) {
        this.DateOfFirstHBPaymentRentAllowanceOnly = DateOfFirstHBPaymentRentAllowanceOnly;
    }

    /**
     * @return the WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly
     */
    public int getWasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly() {
        return WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly;
    }

    /**
     * @param WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly the
     * WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly to set
     */
    public void setWasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly(int WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly) {
        this.WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly = WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly;
    }

    public void setWasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly = 0;
        } else {
            WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly = Integer.valueOf(fields[n]);
            if (WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly > 1 || WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly < 0) {
                System.err.println("WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly " + WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly);
                System.err.println("WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly > 1 || WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly < 0");
                throw new Exception("WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly > 1 || WasTheFirstPaymentAPaymentOnAccountRentAllowanceOnly < 0");
            }
        }
    }

    /**
     * @return the WasThereABackdatedAwardMadeOnTheHBClaim
     */
    public int getWasThereABackdatedAwardMadeOnTheHBClaim() {
        return WasThereABackdatedAwardMadeOnTheHBClaim;
    }

    /**
     * @param WasThereABackdatedAwardMadeOnTheHBClaim the
     * WasThereABackdatedAwardMadeOnTheHBClaim to set
     */
    public void setWasThereABackdatedAwardMadeOnTheHBClaim(int WasThereABackdatedAwardMadeOnTheHBClaim) {
        this.WasThereABackdatedAwardMadeOnTheHBClaim = WasThereABackdatedAwardMadeOnTheHBClaim;
    }

    public void setWasThereABackdatedAwardMadeOnTheHBClaim(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            WasThereABackdatedAwardMadeOnTheHBClaim = 0;
        } else {
            WasThereABackdatedAwardMadeOnTheHBClaim = Integer.valueOf(fields[n]);
            if (WasThereABackdatedAwardMadeOnTheHBClaim > 1 || WasThereABackdatedAwardMadeOnTheHBClaim < 0) {
                System.err.println("WasThereABackdatedAwardMadeOnTheHBClaim " + WasThereABackdatedAwardMadeOnTheHBClaim);
                System.err.println("WasThereABackdatedAwardMadeOnTheHBClaim > 1 || WasThereABackdatedAwardMadeOnTheHBClaim < 0");
                throw new Exception("WasThereABackdatedAwardMadeOnTheHBClaim > 1 || WasThereABackdatedAwardMadeOnTheHBClaim < 0");
            }
        }
    }

    /**
     * @return the DateHBBackdatingFrom
     */
    public String getDateHBBackdatingFrom() {
        return DateHBBackdatingFrom;
    }

    /**
     * @param DateHBBackdatingFrom the DateHBBackdatingFrom to set
     */
    public void setDateHBBackdatingFrom(String DateHBBackdatingFrom) {
        this.DateHBBackdatingFrom = DateHBBackdatingFrom;
    }

    /**
     * @return the DateHBBackdatingTo
     */
    public String getDateHBBackdatingTo() {
        return DateHBBackdatingTo;
    }

    /**
     * @param DateHBBackdatingTo the DateHBBackdatingTo to set
     */
    public void setDateHBBackdatingTo(String DateHBBackdatingTo) {
        this.DateHBBackdatingTo = DateHBBackdatingTo;
    }

    /**
     * @return the TotalAmountOfBackdatedHBAwarded
     */
    public int getTotalAmountOfBackdatedHBAwarded() {
        return TotalAmountOfBackdatedHBAwarded;
    }

    /**
     * @param TotalAmountOfBackdatedHBAwarded the
     * TotalAmountOfBackdatedHBAwarded to set
     */
    public void setTotalAmountOfBackdatesHBAwarded(int TotalAmountOfBackdatedHBAwarded) {
        this.TotalAmountOfBackdatedHBAwarded = TotalAmountOfBackdatedHBAwarded;
    }

    /**
     * @return the CTBClaimTreatAsMadeDate
     */
    public String getCTBClaimTreatAsMadeDate() {
        return CTBClaimTreatAsMadeDate;
    }

    /**
     * @param CTBClaimTreatAsMadeDate the CTBClaimTreatAsMadeDate to set
     */
    public void setCTBClaimTreatAsMadeDate(String CTBClaimTreatAsMadeDate) {
        this.CTBClaimTreatAsMadeDate = CTBClaimTreatAsMadeDate;
    }

    /**
     * @return the SourceOfTheMostRecentCTBClaim
     */
    public int getSourceOfTheMostRecentCTBClaim() {
        return SourceOfTheMostRecentCTBClaim;
    }

    /**
     * @param SourceOfTheMostRecentCTBClaim the SourceOfTheMostRecentCTBClaim to
     * set
     */
    public void setSourceOfTheMostRecentCTBClaim(int SourceOfTheMostRecentCTBClaim) {
        this.SourceOfTheMostRecentCTBClaim = SourceOfTheMostRecentCTBClaim;
    }

    public void setSourceOfTheMostRecentCTBClaim(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            SourceOfTheMostRecentCTBClaim = 0;
        } else {
            SourceOfTheMostRecentCTBClaim = Integer.valueOf(fields[n]);
            if (SourceOfTheMostRecentCTBClaim > 99 || SourceOfTheMostRecentCTBClaim < 0) {
                System.err.println("SourceOfTheMostRecentCTBClaim " + SourceOfTheMostRecentCTBClaim);
                System.err.println("SourceOfTheMostRecentCTBClaim > 99 || SourceOfTheMostRecentCTBClaim < 0");
                throw new Exception("SourceOfTheMostRecentCTBClaim > 99 || SourceOfTheMostRecentCTBClaim < 0");
            }
        }
    }

    /**
     * @return the SourceOfTheMostRecentHBClaim
     */
    public int getSourceOfTheMostRecentHBClaim() {
        return SourceOfTheMostRecentHBClaim;
    }

    /**
     * @param SourceOfTheMostRecentHBClaim the SourceOfTheMostRecentHBClaim to
     * set
     */
    public void setSourceOfTheMostRecentHBClaim(int SourceOfTheMostRecentHBClaim) {
        this.SourceOfTheMostRecentHBClaim = SourceOfTheMostRecentCTBClaim;
    }

    public void setSourceOfTheMostRecentHBClaim(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            SourceOfTheMostRecentHBClaim = 0;
        } else {
            SourceOfTheMostRecentHBClaim = Integer.valueOf(fields[n]);
            if (SourceOfTheMostRecentHBClaim > 99 || SourceOfTheMostRecentHBClaim < 0) {
                System.err.println("SourceOfTheMostRecentHBClaim " + SourceOfTheMostRecentHBClaim);
                System.err.println("SourceOfTheMostRecentHBClaim > 99 || SourceOfTheMostRecentHBClaim < 0");
                throw new Exception("SourceOfTheMostRecentHBClaim > 99 || SourceOfTheMostRecentHBClaim < 0");
            }
        }
    }

    /**
     * @return the DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim
     */
    public int getDidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim() {
        return DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim;
    }

    /**
     * @param DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim the
     * DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim to set
     */
    public void setDidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim(int DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim) {
        this.DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim = DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim;
    }

    public void setDidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim = 0;
        } else {
            DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim = Integer.valueOf(fields[n]);
            if (DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim > 1 || DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim < 0) {
                System.err.println("DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim " + DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim);
                System.err.println("DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim > 10 || DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim < 0");
                throw new Exception("DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim > 10 || DidVerificationIdentifyAnyIncorrectInformationOnTheCTBClaim < 0");
            }
        }
    }

    /**
     * @return the WasThereABackdatedAwardMadeOnTheCTBClaim
     */
    public int getWasThereABackdatedAwardMadeOnTheCTBClaim() {
        return WasThereABackdatedAwardMadeOnTheCTBClaim;
    }

    /**
     * @param WasThereABackdatedAwardMadeOnTheCTBClaim the
     * WasThereABackdatedAwardMadeOnTheCTBClaim to set
     */
    public void setWasThereABackdatedAwardMadeOnTheCTBClaim(int WasThereABackdatedAwardMadeOnTheCTBClaim) {
        this.WasThereABackdatedAwardMadeOnTheCTBClaim = WasThereABackdatedAwardMadeOnTheCTBClaim;
    }

    public void setWasThereABackdatedAwardMadeOnTheCTBClaim(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            WasThereABackdatedAwardMadeOnTheCTBClaim = 0;
        } else {
            WasThereABackdatedAwardMadeOnTheCTBClaim = Integer.valueOf(fields[n]);
            if (WasThereABackdatedAwardMadeOnTheCTBClaim > 1 || WasThereABackdatedAwardMadeOnTheCTBClaim < 0) {
                System.err.println("WasThereABackdatedAwardMadeOnTheCTBClaim " + WasThereABackdatedAwardMadeOnTheCTBClaim);
                System.err.println("WasThereABackdatedAwardMadeOnTheCTBClaim > 1 || WasThereABackdatedAwardMadeOnTheCTBClaim < 0");
                throw new Exception("WasThereABackdatedAwardMadeOnTheCTBClaim > 1 || WasThereABackdatedAwardMadeOnTheCTBClaim < 0");
            }
        }
    }

    /**
     * @return the DateCTBBackdatingFrom
     */
    public String getDateCTBBackdatingFrom() {
        return DateCTBBackdatingFrom;
    }

    /**
     * @param DateCTBBackdatingFrom the DateCTBBackdatingFrom to set
     */
    public void setDateCTBBackdatingFrom(String DateCTBBackdatingFrom) {
        this.DateCTBBackdatingFrom = DateCTBBackdatingFrom;
    }

    /**
     * @return the DateCTBBackdatingTo
     */
    public String getDateCTBBackdatingTo() {
        return DateCTBBackdatingTo;
    }

    /**
     * @param DateCTBBackdatingTo the DateCTBBackdatingTo to set
     */
    public void setDateCTBBackdatingTo(String DateCTBBackdatingTo) {
        this.DateCTBBackdatingTo = DateCTBBackdatingTo;
    }

    /**
     * @return the TotalAmountOfBackdatedCTBAwarded
     */
    public int getTotalAmountOfBackdatedCTBAwarded() {
        return TotalAmountOfBackdatedCTBAwarded;
    }

    /**
     * @param TotalAmountOfBackdatedCTBAwarded the
     * TotalAmountOfBackdatedCTBAwarded to set
     */
    public void setTotalAmountOfBackdatedCTBAwarded(int TotalAmountOfBackdatedCTBAwarded) {
        this.TotalAmountOfBackdatedCTBAwarded = TotalAmountOfBackdatedCTBAwarded;
    }

    /**
     * @return the InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly
     */
    public int getInThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly() {
        return InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly;
    }

    /**
     * @param InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly the
     * InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly to set
     */
    public void setInThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly(int InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly) {
        this.InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly = InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly;
    }

    public void setInThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly = 0;
        } else {
            InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly = Integer.valueOf(fields[n]);
            if (InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly > 1 || InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly < 0) {
                System.err.println("InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly " + InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly);
                System.err.println("InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly > 1 || InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly < 0");
                throw new Exception("InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly > 1 || InThisCaseSubjectToNonHRAThresholdAndCapsNonHRACasesOnly < 0");
            }
        }
    }

    /**
     * @return the IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation
     */
    public int getIfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation() {
        return IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation;
    }

    /**
     * @param IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation the
     * IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation to set
     */
    public void setIfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation(int IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation) {
        this.IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation = IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation;
    }

    public void setIfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation = 0;
        } else {
            IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation = Integer.valueOf(fields[n]);
            if (IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation > 99 || IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation < 0) {
                System.err.println("IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation " + IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation);
                System.err.println("IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation > 99 || IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation < 0");
                throw new Exception("IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation > 99 || IfSubjectToTheNonHRAThresholdAndCapsStateTypeOfAccommodation < 0");
            }
        }
    }

    /**
     * @return the
     * DateOfTransferFromLocalAuthorityLandlordToHousingAssociationRSLIfSubjectToStockTRansferOrLSVT
     */
    public String getDateOfTransferFromLocalAuthorityLandlordToHousingAssociationRSLIfSubjectToStockTRansferOrLSVT() {
        return DateOfTransferFromLocalAuthorityLandlordToHousingAssociationRSLIfSubjectToStockTRansferOrLSVT;
    }

    /**
     * @param
     * DateOfTransferFromLocalAuthorityLandlordToHousingAssociationRSLIfSubjectToStockTRansferOrLSVT
     * the
     * DateOfTransferFromLocalAuthorityLandlordToHousingAssociationRSLIfSubjectToStockTRansferOrLSVT
     * to set
     */
    public void setDateOfTransferFromLocalAuthorityLandlordToHousingAssociationRSLIfSubjectToStockTRansferOrLSVT(String DateOfTransferFromLocalAuthorityLandlordToHousingAssociationRSLIfSubjectToStockTRansferOrLSVT) {
        this.DateOfTransferFromLocalAuthorityLandlordToHousingAssociationRSLIfSubjectToStockTRansferOrLSVT = DateOfTransferFromLocalAuthorityLandlordToHousingAssociationRSLIfSubjectToStockTRansferOrLSVT;
    }

    /**
     * @return the PartnersTotalCapital
     */
    public int getPartnersTotalCapital() {
        return PartnersTotalCapital;
    }

    /**
     * @param PartnersTotalCapital the PartnersTotalCapital to set
     */
    public void setPartnersTotalCapital(int PartnersTotalCapital) {
        this.PartnersTotalCapital = PartnersTotalCapital;
    }

    /**
     * @return the
     * WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure
     */
    public int getWeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure() {
        return WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure;
    }

    /**
     * @param WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure
     * the WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure to
     * set
     */
    public void setWeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure(int WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure) {
        this.WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure = WeeklyNotionalIncomeFromCapitalClaimantAndPartnerCombinedFigure;
    }

    /**
     * @return the ClaimantsTotalHoursOfRemunerativeWorkPerWeek
     */
    public double getClaimantsTotalHoursOfRemunerativeWorkPerWeek() {
        return ClaimantsTotalHoursOfRemunerativeWorkPerWeek;
    }

    /**
     * @param ClaimantsTotalHoursOfRemunerativeWorkPerWeek the
     * ClaimantsTotalHoursOfRemunerativeWorkPerWeek to set
     */
    public void setClaimantsTotalHoursOfRemunerativeWorkPerWeek(double ClaimantsTotalHoursOfRemunerativeWorkPerWeek) {
        this.ClaimantsTotalHoursOfRemunerativeWorkPerWeek = ClaimantsTotalHoursOfRemunerativeWorkPerWeek;
    }

    /**
     * @return the PartnersTotalHoursOfRemunerativeWorkPerWeek
     */
    public double getPartnersTotalHoursOfRemunerativeWorkPerWeek() {
        return PartnersTotalHoursOfRemunerativeWorkPerWeek;
    }

    /**
     * @param PartnersTotalHoursOfRemunerativeWorkPerWeek the
     * PartnersTotalHoursOfRemunerativeWorkPerWeek to set
     */
    public void setPartnersTotalHoursOfRemunerativeWorkPerWeek(double PartnersTotalHoursOfRemunerativeWorkPerWeek) {
        this.PartnersTotalHoursOfRemunerativeWorkPerWeek = PartnersTotalHoursOfRemunerativeWorkPerWeek;
    }

    /**
     * @return the TotalHBPaymentsCreditsSinceLastExtract
     */
    public int getTotalHBPaymentsCreditsSinceLastExtract() {
        return TotalHBPaymentsCreditsSinceLastExtract;
    }

    /**
     * @param TotalHBPaymentsCreditsSinceLastExtract the
     * TotalHBPaymentsCreditsSinceLastExtract to set
     */
    public void setTotalHBPaymentsCreditsSinceLastExtract(int TotalHBPaymentsCreditsSinceLastExtract) {
        this.TotalHBPaymentsCreditsSinceLastExtract = TotalHBPaymentsCreditsSinceLastExtract;
    }

    /**
     * @return the TotalCTBPaymentsCreditsSinceLastExtract
     */
    public int getTotalCTBPaymentsCreditsSinceLastExtract() {
        return TotalCTBPaymentsCreditsSinceLastExtract;
    }

    /**
     * @param TotalCTBPaymentsCreditsSinceLastExtract the
     * TotalCTBPaymentsCreditsSinceLastExtract to set
     */
    public void setTotalCTBPaymentsCreditsSinceLastExtract(int TotalCTBPaymentsCreditsSinceLastExtract) {
        this.TotalCTBPaymentsCreditsSinceLastExtract = TotalCTBPaymentsCreditsSinceLastExtract;
    }

    /**
     * @return the ClaimantsEthnicGroup
     */
    public int getClaimantsEthnicGroup() {
        return ClaimantsEthnicGroup;
    }

    /**
     * @param ClaimantsEthnicGroup the ClaimantsEthnicGroup to set
     */
    public void setClaimantsEthnicGroup(int ClaimantsEthnicGroup) {
        this.ClaimantsEthnicGroup = ClaimantsEthnicGroup;
    }

    public void setClaimantsEthnicGroup(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            ClaimantsEthnicGroup = 0;
        } else {
            ClaimantsEthnicGroup = Integer.valueOf(fields[n]);
            if (ClaimantsEthnicGroup > 99 || ClaimantsEthnicGroup < 0) {
                System.err.println("ClaimantsEthnicGroup " + ClaimantsEthnicGroup);
                System.err.println("ClaimantsEthnicGroup > 99 || ClaimantsEthnicGroup < 0");
                throw new Exception("ClaimantsEthnicGroup > 99 || ClaimantsEthnicGroup < 0");
            }
        }
    }

    /**
     * @return the NewWeeklyHBEntitlementAfterTheChange
     */
    public int getNewWeeklyHBEntitlementAfterTheChange() {
        return NewWeeklyHBEntitlementAfterTheChange;
    }

    /**
     * @param NewWeeklyHBEntitlementAfterTheChange the
     * NewWeeklyHBEntitlementAfterTheChange to set
     */
    public void setNewWeeklyHBEntitlementAfterTheChange(int NewWeeklyHBEntitlementAfterTheChange) {
        this.NewWeeklyHBEntitlementAfterTheChange = NewWeeklyHBEntitlementAfterTheChange;
    }

    /**
     * @return the NewWeeklyCTBEntitlementAfterTheChange
     */
    public int getNewWeeklyCTBEntitlementAfterTheChange() {
        return NewWeeklyCTBEntitlementAfterTheChange;
    }

    /**
     * @param NewWeeklyCTBEntitlementAfterTheChange the
     * NewWeeklyCTBEntitlementAfterTheChange to set
     */
    public void setNewWeeklyCTBEntitlementAfterTheChange(int NewWeeklyCTBEntitlementAfterTheChange) {
        this.NewWeeklyCTBEntitlementAfterTheChange = NewWeeklyCTBEntitlementAfterTheChange;
    }

    /**
     * @return the TypeOfChange
     */
    public int getTypeOfChange() {
        return TypeOfChange;
    }

    /**
     * @param TypeOfChange the TypeOfChange to set
     */
    public void setTypeOfChange(int TypeOfChange) {
        this.TypeOfChange = TypeOfChange;
    }

    public void setTypeOfChange(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            TypeOfChange = 0;
        } else {
            TypeOfChange = Integer.valueOf(fields[n]);
            if (TypeOfChange > 2 || TypeOfChange < 0) {
                System.err.println("TypeOfChange " + TypeOfChange);
                System.err.println("TypeOfChange > 2 || TypeOfChange < 0");
                throw new Exception("TypeOfChange > 2 || TypeOfChange < 0");
            }
        }
    }

    /**
     * @return the DateLAFirstNotifiedOfChangeInClaimDetails
     */
    public String getDateLAFirstNotifiedOfChangeInClaimDetails() {
        return DateLAFirstNotifiedOfChangeInClaimDetails;
    }

    /**
     * @param DateLAFirstNotifiedOfChangeInClaimDetails the
     * DateLAFirstNotifiedOfChangeInClaimDetails to set
     */
    public void setDateLAFirstNotifiedOfChangeInClaimDetails(String DateLAFirstNotifiedOfChangeInClaimDetails) {
        this.DateLAFirstNotifiedOfChangeInClaimDetails = DateLAFirstNotifiedOfChangeInClaimDetails;
    }

    /**
     * @return the DateChangeOfDetailsAreEffectiveFrom
     */
    public String getDateChangeOfDetailsAreEffectiveFrom() {
        return DateChangeOfDetailsAreEffectiveFrom;
    }

    /**
     * @param DateChangeOfDetailsAreEffectiveFrom the
     * DateChangeOfDetailsAreEffectiveFrom to set
     */
    public void setDateChangeOfDetailsAreEffectiveFrom(String DateChangeOfDetailsAreEffectiveFrom) {
        this.DateChangeOfDetailsAreEffectiveFrom = DateChangeOfDetailsAreEffectiveFrom;
    }

    /**
     * @return the IfNotAnnualUpratingHowWasTheChangeIdentified
     */
    public int getIfNotAnnualUpratingHowWasTheChangeIdentified() {
        return IfNotAnnualUpratingHowWasTheChangeIdentified;
    }

    /**
     * @param IfNotAnnualUpratingHowWasTheChangeIdentified the
     * IfNotAnnualUpratingHowWasTheChangeIdentified to set
     */
    public void setIfNotAnnualUpratingHowWasTheChangeIdentified(int IfNotAnnualUpratingHowWasTheChangeIdentified) {
        this.IfNotAnnualUpratingHowWasTheChangeIdentified = IfNotAnnualUpratingHowWasTheChangeIdentified;
    }

    public void setIfNotAnnualUpratingHowWasTheChangeIdentified(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            IfNotAnnualUpratingHowWasTheChangeIdentified = 0;
        } else {
            IfNotAnnualUpratingHowWasTheChangeIdentified = Integer.valueOf(fields[n]);
            if (IfNotAnnualUpratingHowWasTheChangeIdentified > 99 || IfNotAnnualUpratingHowWasTheChangeIdentified < 0) {
                System.err.println("IfNotAnnualUpratingHowWasTheChangeIdentified " + IfNotAnnualUpratingHowWasTheChangeIdentified);
                System.err.println("IfNotAnnualUpratingHowWasTheChangeIdentified > 99 || IfNotAnnualUpratingHowWasTheChangeIdentified < 0");
                throw new Exception("IfNotAnnualUpratingHowWasTheChangeIdentified > 99 || IfNotAnnualUpratingHowWasTheChangeIdentified < 0");
            }
        }
    }

    /**
     * @return the DateSupercessionDecisionWasMadeOnTheHBClaim
     */
    public String getDateSupercessionDecisionWasMadeOnTheHBClaim() {
        return DateSupercessionDecisionWasMadeOnTheHBClaim;
    }

    /**
     * @param DateSupercessionDecisionWasMadeOnTheHBClaim the
     * DateSupercessionDecisionWasMadeOnTheHBClaim to set
     */
    public void setDateSupercessionDecisionWasMadeOnTheHBClaim(String DateSupercessionDecisionWasMadeOnTheHBClaim) {
        this.DateSupercessionDecisionWasMadeOnTheHBClaim = DateSupercessionDecisionWasMadeOnTheHBClaim;
    }

    /**
     * @return the DateSupercessionDecisionWasMadeOnTheCTBClaim
     */
    public String getDateSupercessionDecisionWasMadeOnTheCTBClaim() {
        return DateSupercessionDecisionWasMadeOnTheCTBClaim;
    }

    /**
     * @param DateSupercessionDecisionWasMadeOnTheCTBClaim the
     * DateSupercessionDecisionWasMadeOnTheCTBClaim to set
     */
    public void setDateSupercessionDecisionWasMadeOnTheCTBClaim(String DateSupercessionDecisionWasMadeOnTheCTBClaim) {
        this.DateSupercessionDecisionWasMadeOnTheCTBClaim = DateSupercessionDecisionWasMadeOnTheCTBClaim;
    }

    /**
     * @return the DateApplicationForRevisionReconsiderationReceived
     */
    public String getDateApplicationForRevisionReconsiderationReceived() {
        return DateApplicationForRevisionReconsiderationReceived;
    }

    /**
     * @param DateApplicationForRevisionReconsiderationReceived the
     * DateApplicationForRevisionReconsiderationReceived to set
     */
    public void setDateApplicationForRevisionReconsiderationReceived(String DateApplicationForRevisionReconsiderationReceived) {
        this.DateApplicationForRevisionReconsiderationReceived = DateApplicationForRevisionReconsiderationReceived;
    }

    /**
     * @return the
     * DateClaimantNotifiedAboutTheDecisionOfTheRevisionReconsideration
     */
    public String getDateClaimantNotifiedAboutTheDecisionOfTheRevisionReconsideration() {
        return DateClaimantNotifiedAboutTheDecisionOfTheRevisionReconsideration;
    }

    /**
     * @param DateClaimantNotifiedAboutTheDecisionOfTheRevisionReconsideration
     * the DateClaimantNotifiedAboutTheDecisionOfTheRevisionReconsideration to
     * set
     */
    public void setDateClaimantNotifiedAboutTheDecisionOfTheRevisionReconsideration(String DateClaimantNotifiedAboutTheDecisionOfTheRevisionReconsideration) {
        this.DateClaimantNotifiedAboutTheDecisionOfTheRevisionReconsideration = DateClaimantNotifiedAboutTheDecisionOfTheRevisionReconsideration;
    }

    /**
     * @return the DateAppealApplicationWasLodged
     */
    public String getDateAppealApplicationWasLodged() {
        return DateAppealApplicationWasLodged;
    }

    /**
     * @param DateAppealApplicationWasLodged the DateAppealApplicationWasLodged
     * to set
     */
    public void setDateAppealApplicationWasLodged(String DateAppealApplicationWasLodged) {
        this.DateAppealApplicationWasLodged = DateAppealApplicationWasLodged;
    }

    /**
     * @return the OutcomeOfAppealApplication
     */
    public int getOutcomeOfAppealApplication() {
        return OutcomeOfAppealApplication;
    }

    /**
     * @param OutcomeOfAppealApplication the OutcomeOfAppealApplication to set
     */
    public void setOutcomeOfAppealApplication(int OutcomeOfAppealApplication) {
        this.OutcomeOfAppealApplication = OutcomeOfAppealApplication;
    }

    public void setOutcomeOfAppealApplication(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            OutcomeOfAppealApplication = 0;
        } else {
            OutcomeOfAppealApplication = Integer.valueOf(fields[n]);
            if (OutcomeOfAppealApplication > 99 || OutcomeOfAppealApplication < 0) {
                System.err.println("OutcomeOfAppealApplication " + OutcomeOfAppealApplication);
                System.err.println("OutcomeOfAppealApplication > 99 || OutcomeOfAppealApplication < 0");
                throw new Exception("OutcomeOfAppealApplication > 99 || OutcomeOfAppealApplication < 0");
            }
        }
    }

    /**
     * @return the DateOfOutcomeOfAppealApplication
     */
    public String getDateOfOutcomeOfAppealApplication() {
        return DateOfOutcomeOfAppealApplication;
    }

    /**
     * @param DateOfOutcomeOfAppealApplication the
     * DateOfOutcomeOfAppealApplication to set
     */
    public void setDateOfOutcomeOfAppealApplication(String DateOfOutcomeOfAppealApplication) {
        this.DateOfOutcomeOfAppealApplication = DateOfOutcomeOfAppealApplication;
    }

    /**
     * @return the
     * DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentHBClaim
     */
    public String getDateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentHBClaim() {
        return DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentHBClaim;
    }

    /**
     * @param
     * DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentHBClaim
     * the
     * DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentHBClaim
     * to set
     */
    public void setDateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentHBClaim(String DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentHBClaim) {
        this.DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentHBClaim = DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentHBClaim;
    }

    /**
     * @return the
     * DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentHBClaim
     */
    public String getDateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentHBClaim() {
        return DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentHBClaim;
    }

    /**
     * @param
     * DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentHBClaim
     * the
     * DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentHBClaim
     * to set
     */
    public void setDateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentHBClaim(String DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentHBClaim) {
        this.DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentHBClaim = DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentHBClaim;
    }

    /**
     * @return the DateCouncilTaxPayable
     */
    public String getDateCouncilTaxPayable() {
        return DateCouncilTaxPayable;
    }

    /**
     * @param DateCouncilTaxPayable the DateCouncilTaxPayable to set
     */
    public void setDateCouncilTaxPayable(String DateCouncilTaxPayable) {
        this.DateCouncilTaxPayable = DateCouncilTaxPayable;
    }

    /**
     * @return the
     * DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentCTBClaim
     */
    public String getDateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentCTBClaim() {
        return DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentCTBClaim;
    }

    /**
     * @param
     * DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentCTBClaim
     * the
     * DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentCTBClaim
     * to set
     */
    public void setDateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentCTBClaim(String DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentCTBClaim) {
        this.DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentCTBClaim = DateThatAllInformationWasRecievedFromTheClaimantToEnableADecisionOnTheMostRecentCTBClaim;
    }

    /**
     * @return the
     * DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentCTBClaim
     */
    public String getDateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentCTBClaim() {
        return DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentCTBClaim;
    }

    /**
     * @param
     * DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentCTBClaim
     * the
     * DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentCTBClaim
     * to set
     */
    public void setDateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentCTBClaim(String DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentCTBClaim) {
        this.DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentCTBClaim = DateThatAllInformationWasRecievedFromThirdPartiesToEnableADecisionOnTheMostRecentCTBClaim;
    }

    /**
     * @return the SoftwareProvider
     */
    public int getSoftwareProvider() {
        return SoftwareProvider;
    }

    /**
     * @param SoftwareProvider the SoftwareProvider to set
     */
    public void setSoftwareProvider(int SoftwareProvider) {
        this.SoftwareProvider = SoftwareProvider;
    }

    public void setSoftwareProvider(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            SoftwareProvider = 0;
        } else {
            SoftwareProvider = Integer.valueOf(fields[n]);
            if (SoftwareProvider > 3 || SoftwareProvider < 0) {
                System.err.println("SoftwareProvider " + SoftwareProvider);
                System.err.println("SoftwareProvider > 3 || SoftwareProvider < 0");
                throw new Exception("SoftwareProvider > 3 || SoftwareProvider < 0");
            }
        }
    }

    /**
     * @return the StaffingFTE
     */
    public String getStaffingFTE() {
        return StaffingFTE;
    }

    /**
     * @param StaffingFTE the StaffingFTE to set
     */
    public void setStaffingFTE(String StaffingFTE) {
        this.StaffingFTE = StaffingFTE;
    }

    /**
     * @return the ContractingOutHandlingAndMaintenanceOfHBCTB
     */
    public int getContractingOutHandlingAndMaintenanceOfHBCTB() {
        return ContractingOutHandlingAndMaintenanceOfHBCTB;
    }

    /**
     * @param ContractingOutHandlingAndMaintenanceOfHBCTB the
     * ContractingOutHandlingAndMaintenanceOfHBCTB to set
     */
    public void setContractingOutHandlingAndMaintenanceOfHBCTB(int ContractingOutHandlingAndMaintenanceOfHBCTB) {
        this.ContractingOutHandlingAndMaintenanceOfHBCTB = ContractingOutHandlingAndMaintenanceOfHBCTB;
    }

    public void setContractingOutHandlingAndMaintenanceOfHBCTB(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            ContractingOutHandlingAndMaintenanceOfHBCTB = 0;
        } else {
            ContractingOutHandlingAndMaintenanceOfHBCTB = Integer.valueOf(fields[n]);
            if (ContractingOutHandlingAndMaintenanceOfHBCTB > 9 || ContractingOutHandlingAndMaintenanceOfHBCTB < 0) {
                System.err.println("ContractingOutHandlingAndMaintenanceOfHBCTB " + ContractingOutHandlingAndMaintenanceOfHBCTB);
                System.err.println("ContractingOutHandlingAndMaintenanceOfHBCTB > 9 || ContractingOutHandlingAndMaintenanceOfHBCTB < 0");
                throw new Exception("ContractingOutHandlingAndMaintenanceOfHBCTB > 9 || ContractingOutHandlingAndMaintenanceOfHBCTB < 0");
            }
        }
    }

    /**
     * @return the ContractingOutCounterFraudWorkRelatingToHBCTB
     */
    public int getContractingOutCounterFraudWorkRelatingToHBCTB() {
        return ContractingOutCounterFraudWorkRelatingToHBCTB;
    }

    /**
     * @param ContractingOutCounterFraudWorkRelatingToHBCTB the
     * ContractingOutCounterFraudWorkRelatingToHBCTB to set
     */
    public void setContractingOutCounterFraudWorkRelatingToHBCTB(int ContractingOutCounterFraudWorkRelatingToHBCTB) {
        this.ContractingOutCounterFraudWorkRelatingToHBCTB = ContractingOutCounterFraudWorkRelatingToHBCTB;
    }

    public void setContractingOutCounterFraudWorkRelatingToHBCTB(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            ContractingOutCounterFraudWorkRelatingToHBCTB = 0;
        } else {
            ContractingOutCounterFraudWorkRelatingToHBCTB = Integer.valueOf(fields[n]);
            if (ContractingOutCounterFraudWorkRelatingToHBCTB > 9 || ContractingOutCounterFraudWorkRelatingToHBCTB < 0) {
                System.err.println("ContractingOutCounterFraudWorkRelatingToHBCTB " + ContractingOutCounterFraudWorkRelatingToHBCTB);
                System.err.println("ContractingOutCounterFraudWorkRelatingToHBCTB > 9 || ContractingOutCounterFraudWorkRelatingToHBCTB < 0");
                throw new Exception("ContractingOutCounterFraudWorkRelatingToHBCTB > 9 || ContractingOutCounterFraudWorkRelatingToHBCTB < 0");
            }
        }
    }

    /**
     * @return the NumberOfBedroomsForLHARolloutCasesOnly
     */
    public int getNumberOfBedroomsForLHARolloutCasesOnly() {
        return NumberOfBedroomsForLHARolloutCasesOnly;
    }

    /**
     * @param NumberOfBedroomsForLHARolloutCasesOnly the
     * NumberOfBedroomsForLHARolloutCasesOnly to set
     */
    public void setNumberOfBedroomsForLHARolloutCasesOnly(int NumberOfBedroomsForLHARolloutCasesOnly) {
        this.NumberOfBedroomsForLHARolloutCasesOnly = NumberOfBedroomsForLHARolloutCasesOnly;
    }

    /**
     * @return the PartnersDateOfDeath
     */
    public String getPartnersDateOfDeath() {
        return PartnersDateOfDeath;
    }

    /**
     * @param PartnersDateOfDeath the PartnersDateOfDeath to set
     */
    public void setPartnersDateOfDeath(String PartnersDateOfDeath) {
        this.PartnersDateOfDeath = PartnersDateOfDeath;
    }

    /**
     * @return the JointTenancyFlag
     */
    public int getJointTenancyFlag() {
        return JointTenancyFlag;
    }

    /**
     * @param JointTenancyFlag the JointTenancyFlag to set
     */
    public void setJointTenancyFlag(int JointTenancyFlag) {
        this.JointTenancyFlag = JointTenancyFlag;
    }

    public void setJointTenancyFlag(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            JointTenancyFlag = 0;
        } else {
            JointTenancyFlag = Integer.valueOf(fields[n]);
            if (JointTenancyFlag > 2 || JointTenancyFlag < 0) {
                System.err.println("JointTenancyFlag " + JointTenancyFlag);
                System.err.println("JointTenancyFlag > 2 || JointTenancyFlag < 0");
                throw new Exception("JointTenancyFlag > 2 || JointTenancyFlag < 0");
            }
        }
    }

    /**
     * @return the AppointeeFlag
     */
    public int getAppointeeFlag() {
        return AppointeeFlag;
    }

    /**
     * @param AppointeeFlag the AppointeeFlag to set
     */
    public void setAppointeeFlag(int AppointeeFlag) {
        this.AppointeeFlag = AppointeeFlag;
    }

    public void setAppointeeFlag(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            AppointeeFlag = 0;
        } else {
            AppointeeFlag = Integer.valueOf(fields[n]);
            if (AppointeeFlag > 2 || AppointeeFlag < 0) {
                System.err.println("AppointeeFlag " + AppointeeFlag);
                System.err.println("AppointeeFlag > 2 || AppointeeFlag < 0");
                throw new Exception("AppointeeFlag > 2 || AppointeeFlag < 0");
            }
        }
    }

    /**
     * @return the RentFreeWeeksIndicator
     */
    public int getRentFreeWeeksIndicator() {
        return RentFreeWeeksIndicator;
    }

    /**
     * @param RentFreeWeeksIndicator the RentFreeWeeksIndicator to set
     */
    public void setRentFreeWeeksIndicator(int RentFreeWeeksIndicator) {
        this.RentFreeWeeksIndicator = RentFreeWeeksIndicator;
    }

    /**
     * @return the LastPaidToDate
     */
    public String getLastPaidToDate() {
        return LastPaidToDate;
    }

    /**
     * @param LastPaidToDate the LastPaidToDate to set
     */
    public void setLastPaidToDate(String LastPaidToDate) {
        this.LastPaidToDate = LastPaidToDate;
    }

    /**
     * @return the WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability
     */
    public int getWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability() {
        return WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    }

    /**
     * @param WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability the
     * WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability to set
     */
    public void setWeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability(int WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability) {
        this.WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability = WeeklyAdditionalDiscretionaryPaymentForCouncilTaxLiability;
    }

    /**
     * @return the ClaimantsWeeklyIncomeFromESABasicElement
     */
    public int getClaimantsWeeklyIncomeFromESABasicElement() {
        return ClaimantsWeeklyIncomeFromESABasicElement;
    }

    /**
     * @param ClaimantsWeeklyIncomeFromESABasicElement the
     * ClaimantsWeeklyIncomeFromESABasicElement to set
     */
    public void setClaimantsWeeklyIncomeFromESABasicElement(int ClaimantsWeeklyIncomeFromESABasicElement) {
        this.ClaimantsWeeklyIncomeFromESABasicElement = ClaimantsWeeklyIncomeFromESABasicElement;
    }

    /**
     * @return the PartnersWeeklyIncomeFromESABasicElement
     */
    public int getPartnersWeeklyIncomeFromESABasicElement() {
        return PartnersWeeklyIncomeFromESABasicElement;
    }

    /**
     * @param PartnersWeeklyIncomeFromESABasicElement the
     * PartnersWeeklyIncomeFromESABasicElement to set
     */
    public void setPartnersWeeklyIncomeFromESABasicElement(int PartnersWeeklyIncomeFromESABasicElement) {
        this.PartnersWeeklyIncomeFromESABasicElement = PartnersWeeklyIncomeFromESABasicElement;
    }

    /**
     * @return the ClaimantsWeeklyIncomeFromESAWRAGElement
     */
    public int getClaimantsWeeklyIncomeFromESAWRAGElement() {
        return ClaimantsWeeklyIncomeFromESAWRAGElement;
    }

    /**
     * @param ClaimantsWeeklyIncomeFromESAWRAGElement the
     * ClaimantsWeeklyIncomeFromESAWRAGElement to set
     */
    public void setClaimantsWeeklyIncomeFromESAWRAGElement(int ClaimantsWeeklyIncomeFromESAWRAGElement) {
        this.ClaimantsWeeklyIncomeFromESAWRAGElement = ClaimantsWeeklyIncomeFromESAWRAGElement;
    }

    /**
     * @return the PartnersWeeklyIncomeFromESAWRAGElement
     */
    public int getPartnersWeeklyIncomeFromESAWRAGElement() {
        return PartnersWeeklyIncomeFromESAWRAGElement;
    }

    /**
     * @param PartnersWeeklyIncomeFromESAWRAGElement the
     * PartnersWeeklyIncomeFromESAWRAGElement to set
     */
    public void setPartnersWeeklyIncomeFromESAWRAGElement(int PartnersWeeklyIncomeFromESAWRAGElement) {
        this.PartnersWeeklyIncomeFromESAWRAGElement = PartnersWeeklyIncomeFromESAWRAGElement;
    }

    /**
     * @return the ClaimantsWeeklyIncomeFromESASCGElement
     */
    public int getClaimantsWeeklyIncomeFromESASCGElement() {
        return ClaimantsWeeklyIncomeFromESASCGElement;
    }

    /**
     * @param ClaimantsWeeklyIncomeFromESASCGElement the
     * ClaimantsWeeklyIncomeFromESASCGElement to set
     */
    public void setClaimantsWeeklyIncomeFromESASCGElement(int ClaimantsWeeklyIncomeFromESASCGElement) {
        this.ClaimantsWeeklyIncomeFromESASCGElement = ClaimantsWeeklyIncomeFromESASCGElement;
    }

    /**
     * @return the PartnersWeeklyIncomeFromESASCGElement
     */
    public int getPartnersWeeklyIncomeFromESASCGElement() {
        return PartnersWeeklyIncomeFromESASCGElement;
    }

    /**
     * @param PartnersWeeklyIncomeFromESASCGElement the
     * PartnersWeeklyIncomeFromESASCGElement to set
     */
    public void setPartnersWeeklyIncomeFromESASCGElement(int PartnersWeeklyIncomeFromESASCGElement) {
        this.PartnersWeeklyIncomeFromESASCGElement = PartnersWeeklyIncomeFromESASCGElement;
    }

    /**
     * @return the WRAGPremiumFlag
     */
    public String getWRAGPremiumFlag() {
        return WRAGPremiumFlag;
    }

    /**
     * @param WRAGPremiumFlag the WRAGPremiumFlag to set
     */
    public void setWRAGPremiumFlag(String WRAGPremiumFlag) {
        this.WRAGPremiumFlag = WRAGPremiumFlag;
    }

    /**
     * @return the SCGPremiumFlag
     */
    public String getSCGPremiumFlag() {
        return SCGPremiumFlag;
    }

    /**
     * @param SCGPremiumFlag the SCGPremiumFlag to set
     */
    public void setSCGPremiumFlag(String SCGPremiumFlag) {
        this.SCGPremiumFlag = SCGPremiumFlag;
    }

    /**
     * @return the LandlordPostcode
     */
    public String getLandlordPostcode() {
        return LandlordPostcode;
    }

    /**
     * @param LandlordPostcode the LandlordPostcode to set
     */
    public void setLandlordPostcode(String LandlordPostcode) {
        this.LandlordPostcode = LandlordPostcode;
    }

    /**
     * @return the SubRecordType
     */
    public int getSubRecordType() {
        return SubRecordType;
    }

    /**
     * @param SubRecordType the SubRecordType to set
     */
    public void setSubRecordType(int SubRecordType) {
        this.SubRecordType = SubRecordType;
    }

    public int setSubRecordType(
            int n,
            String[] fields) throws Exception {
        if (fields[n].trim().isEmpty()) {
            SubRecordType = 0;
        } else {
            try {
                SubRecordType = Integer.valueOf(fields[n]);
            } catch (NumberFormatException e) {
                System.err.println("Assuming LandlordPostcode was set to County Name");
                System.err.println("LandlordPostcode " + LandlordPostcode);
                if (LandlordPostcode.trim().equalsIgnoreCase("LEEDS")) {
                    int debug = 1;
                    n++;
                }
                LandlordPostcode = fields[n];
                System.err.println("LandlordPostcode set to " + fields[n]);
                n++;
                setSubRecordType(n, fields);
                System.err.println("SubRecordType set to " + SubRecordType);
                System.err.println("RecordID " + RecordID);
            }
            if (SubRecordType > 2 || SubRecordType < 0) {
                System.err.println("SubRecordType " + SubRecordType);
                System.err.println("SubRecordType > 2 || SubRecordType < 0");
                throw new Exception("SubRecordType > 2 || SubRecordType < 0");
            }
        }
        return n;
    }

    /**
     * @return the SubRecordDateOfBirth
     */
    public String getSubRecordDateOfBirth() {
        return SubRecordDateOfBirth;
    }

    /**
     * @param SubRecordDateOfBirth the SubRecordDateOfBirth to set
     */
    public void setSubRecordDateOfBirth(String SubRecordDateOfBirth) {
        this.SubRecordDateOfBirth = SubRecordDateOfBirth;
    }

    public String getSubRecordChildReferenceNumberOrNINO() {
        return SubRecordChildReferenceNumberOrNINO;
    }

    public void setSubRecordChildREferenceNumberOrNINO(String SubRecordChildREferenceNumberOrNINO) {
        this.SubRecordChildReferenceNumberOrNINO = SubRecordChildREferenceNumberOrNINO;
    }

    public String getSubRecordStartDate() {
        return SubRecordStartDate;
    }

    public void setSubRecordStartDate(String SubRecordStartDate) {
        this.SubRecordStartDate = SubRecordStartDate;
    }

    public String getSubRecordEndDate() {
        return SubRecordEndDate;
    }

    public void setSubRecordEndDate(String SubRecordEndDate) {
        this.SubRecordEndDate = SubRecordEndDate;
    }

    public String getIfThisActivityResolvesAnHBMSReferralProvideRMSNumber() {
        return IfThisActivityResolvesAnHBMSReferralProvideRMSNumber;
    }

    public void setIfThisActivityResolvesAnHBMSReferralProvideRMSNumber(String IfThisActivityResolvesAnHBMSReferralProvideRMSNumber) {
        this.IfThisActivityResolvesAnHBMSReferralProvideRMSNumber = IfThisActivityResolvesAnHBMSReferralProvideRMSNumber;
    }

    public String getHBMSRuleScanID() {
        return HBMSRuleScanID;
    }

    public void setHBMSRuleScanID(String HBMSRuleScanID) {
        this.HBMSRuleScanID = HBMSRuleScanID;
    }

    public String getDateOfHBMSMatch() {
        return DateOfHBMSMatch;
    }

    public void setDateOfHBMSMatch(String DateOfHBMSMatch) {
        this.DateOfHBMSMatch = DateOfHBMSMatch;
    }

    public String getIfResolutionOfHBMSReferralDoesNotResultInAFinancialAdjustmentPleaseIndicateTheReasonWhy() {
        return IfResolutionOfHBMSReferralDoesNotResultInAFinancialAdjustmentPleaseIndicateTheReasonWhy;
    }

    public void setIfResolutionOfHBMSReferralDoesNotResultInAFinancialAdjustmentPleaseIndicateTheReasonWhy(String IfResolutionOfHBMSReferralDoesNotResultInAFinancialAdjustmentPleaseIndicateTheReasonWhy) {
        this.IfResolutionOfHBMSReferralDoesNotResultInAFinancialAdjustmentPleaseIndicateTheReasonWhy = IfResolutionOfHBMSReferralDoesNotResultInAFinancialAdjustmentPleaseIndicateTheReasonWhy;
    }

    public String getUniqueTRecordIdentifier() {
        return UniqueTRecordIdentifier;
    }

    public void setUniqueTRecordIdentifier(String UniqueTRecordIdentifier) {
        this.UniqueTRecordIdentifier = UniqueTRecordIdentifier;
    }

    public String getOverpaymentReasonCapital() {
        return OverpaymentReasonCapital;
    }

    public void setOverpaymentReasonCapital(String OverpaymentReasonCapital) {
        this.OverpaymentReasonCapital = OverpaymentReasonCapital;
    }

    public String getOverpaymentReasonClaimentPartnersEarnedIncome() {
        return OverpaymentReasonClaimentPartnersEarnedIncome;
    }

    public void setOverpaymentReasonClaimentPartnersEarnedIncome(String OverpaymentReasonClaimentPartnersEarnedIncome) {
        this.OverpaymentReasonClaimentPartnersEarnedIncome = OverpaymentReasonClaimentPartnersEarnedIncome;
    }

    public String getOverpaymentReasonNonDependentsEarnedIncome() {
        return OverpaymentReasonNonDependentsEarnedIncome;
    }

    public void setOverpaymentReasonNonDependentsEarnedIncome(String OverpaymentReasonNonDependentsEarnedIncome) {
        this.OverpaymentReasonNonDependentsEarnedIncome = OverpaymentReasonNonDependentsEarnedIncome;
    }

    public String getOverpaymentReasonPassportingStatus() {
        return OverpaymentReasonPassportingStatus;
    }

    public void setOverpaymentReasonPassportingStatus(String OverpaymentReasonPassportingStatus) {
        this.OverpaymentReasonPassportingStatus = OverpaymentReasonPassportingStatus;
    }

    public String getOverpaymentReasonIncomeFromDWPBenefits() {
        return OverpaymentReasonIncomeFromDWPBenefits;
    }

    public void setOverpaymentReasonIncomeFromDWPBenefits(String OverpaymentReasonIncomeFromDWPBenefits) {
        this.OverpaymentReasonIncomeFromDWPBenefits = OverpaymentReasonIncomeFromDWPBenefits;
    }

    public String getOverpaymentReasonTaxCredits() {
        return OverpaymentReasonTaxCredits;
    }

    public void setOverpaymentReasonTaxCredits(String OverpaymentReasonTaxCredits) {
        this.OverpaymentReasonTaxCredits = OverpaymentReasonTaxCredits;
    }

    public String getOverpaymentReasonOtherIncome() {
        return OverpaymentReasonOtherIncome;
    }

    public void setOverpaymentReasonOtherIncome(String OverpaymentReasonOtherIncome) {
        this.OverpaymentReasonOtherIncome = OverpaymentReasonOtherIncome;
    }

    public String getOverpaymentReasonLivingTogetherAsHusbandAndWife() {
        return OverpaymentReasonLivingTogetherAsHusbandAndWife;
    }

    public void setOverpaymentReasonLivingTogetherAsHusbandAndWife(String OverpaymentReasonLivingTogetherAsHusbandAndWife) {
        this.OverpaymentReasonLivingTogetherAsHusbandAndWife = OverpaymentReasonLivingTogetherAsHusbandAndWife;
    }

    public String getOverpaymentReasonNumberOfNonDependents() {
        return OverpaymentReasonNumberOfNonDependents;
    }

    public void setOverpaymentReasonNumberOfNonDependents(String OverpaymentReasonNumberOfNonDependents) {
        this.OverpaymentReasonNumberOfNonDependents = OverpaymentReasonNumberOfNonDependents;
    }

    public String getOverpaymentReasonNumberOfDependents() {
        return OverpaymentReasonNumberOfDependents;
    }

    public void setOverpaymentReasonNumberOfDependents(String OverpaymentReasonNumberOfDependents) {
        this.OverpaymentReasonNumberOfDependents = OverpaymentReasonNumberOfDependents;
    }

    public String getOverpaymentReasonNonResidence() {
        return OverpaymentReasonNonResidence;
    }

    public void setOverpaymentReasonNonResidence(String OverpaymentReasonNonResidence) {
        this.OverpaymentReasonNonResidence = OverpaymentReasonNonResidence;
    }

    public String getOverpaymentReasonEligibleRentCouncilTax() {
        return OverpaymentReasonEligibleRentCouncilTax;
    }

    public void setOverpaymentReasonEligibleRentCouncilTax(String OverpaymentReasonEligibleRentCouncilTax) {
        this.OverpaymentReasonEligibleRentCouncilTax = OverpaymentReasonEligibleRentCouncilTax;
    }

    public String getOverpaymentReasonIneligible() {
        return OverpaymentReasonIneligible;
    }

    public void setOverpaymentReasonIneligible(String OverpaymentReasonIneligible) {
        this.OverpaymentReasonIneligible = OverpaymentReasonIneligible;
    }

    public String getOverpaymentReasonIdentityDeath() {
        return OverpaymentReasonIdentityDeath;
    }

    public void setOverpaymentReasonIdentityDeath(String OverpaymentReasonIdentityDeath) {
        this.OverpaymentReasonIdentityDeath = OverpaymentReasonIdentityDeath;
    }

    public String getOverpaymentReasonOther() {
        return OverpaymentReasonOther;
    }

    public void setOverpaymentReasonOther(String OverpaymentReasonOther) {
        this.OverpaymentReasonOther = OverpaymentReasonOther;
    }

    public String getBenefitThatThisPaymentErrorRelatesTo() {
        return BenefitThatThisPaymentErrorRelatesTo;
    }

    public void setBenefitThatThisPaymentErrorRelatesTo(String BenefitThatThisPaymentErrorRelatesTo) {
        this.BenefitThatThisPaymentErrorRelatesTo = BenefitThatThisPaymentErrorRelatesTo;
    }

    public long getTotalValueOfPaymentError() {
        return TotalValueOfPaymentError;
    }

    public void setTotalValueOfPaymentError(long TotalValueOfPaymentError) {
        this.TotalValueOfPaymentError = TotalValueOfPaymentError;
    }

    public String getWeeklyBenefitDiscrepancyAtStartOfPaymentError() {
        return WeeklyBenefitDiscrepancyAtStartOfPaymentError;
    }

    public void setWeeklyBenefitDiscrepancyAtStartOfPaymentError(String WeeklyBenefitDiscrepancyAtStartOfPaymentError) {
        this.WeeklyBenefitDiscrepancyAtStartOfPaymentError = WeeklyBenefitDiscrepancyAtStartOfPaymentError;
    }

    public String getStartDateOfPaymentErrorPeriod() {
        return StartDateOfPaymentErrorPeriod;
    }

    public void setStartDateOfPaymentErrorPeriod(String StartDateOfPaymentErrorPeriod) {
        this.StartDateOfPaymentErrorPeriod = StartDateOfPaymentErrorPeriod;
    }

    public String getEndDateOfPaymentErrorPeriod() {
        return EndDateOfPaymentErrorPeriod;
    }

    public void setEndDateOfPaymentErrorPeriod(String EndDateOfPaymentErrorPeriod) {
        this.EndDateOfPaymentErrorPeriod = EndDateOfPaymentErrorPeriod;
    }

    public String getWhatWasTheCauseOfTheOverPayment() {
        return WhatWasTheCauseOfTheOverPayment;
    }

    public void setWhatWasTheCauseOfTheOverPayment(String WhatWasTheCauseOfTheOverPayment) {
        this.WhatWasTheCauseOfTheOverPayment = WhatWasTheCauseOfTheOverPayment;
    }

    /**
     * @return the SRecords
     */
    public HashSet<DW_SHBE_RecordOld> getSRecords() {
        if (SRecords == null) {
            SRecords = new HashSet<DW_SHBE_RecordOld>();
        }
        return SRecords;
    }

    /**
     * @param SRecords the SRecords to set
     */
    public void setSRecords(HashSet<DW_SHBE_RecordOld> SRecords) {
        this.SRecords = SRecords;
    }

}
